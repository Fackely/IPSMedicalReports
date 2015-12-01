/*
 * @(#)SqlBaseMenuDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */

package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.RespuestaHashMap;
import util.UtilidadBD;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a Menu
 *
 *	@version 1.0, Mar 29, 2004
 */
public class SqlBaseMenuDao 
{

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseMenuDao.class);
	
	
	/**
	 * Cadena constante con la consulta necesaria para obtener los roles del usuario.
	 */
	private static final String obtenerRolesStr = "SELECT etiqueta_func AS nombreFuncionalidad, archivo_func AS archivoFuncionalidad, codigo_func AS codigoFuncionalidad FROM administracion.funcionalidades WHERE codigo_func IN (SELECT codigo_func FROM administracion.roles_funcionalidades WHERE nombre_rol IN (SELECT nombre_rol FROM administracion.roles_usuarios WHERE login = ?) AND debo_imprimir=" + ValoresPorDefecto.getValorTrueParaConsultas() + " GROUP BY codigo_func) GROUP BY funcionalidades.etiqueta_func, funcionalidades.archivo_func, funcionalidades.codigo_func ORDER BY etiqueta_func";
	
	/**
	 * Cadena que consulta los menus con las respectivas funcionalidades
	 */
	private static final String obtenerMenusStr = "SELECT d.funcionalidad AS func, d.modulo AS codigo, m.nombre AS modulo FROM administracion.dep_modulo_func d, administracion.modulos m WHERE m.codigo=d.modulo ";

	/**
	 * Dado un login, obtiene los roles asociados a su respectivo usuario, y el URL del archivo con el menu
	 * correspondiente a dicho rol. Este metodo recibe una conexion abierta con una base de datos Genérica,
	 * y la mantiene abierta despues de ejecutarse. Si no existe, la crea.
	 * @param con una conexion abierta con una base de datos Genérica
	 * @param principal la representacion como cadena de texto de un objeto <code>java.security.Principal</code>
	 * @return un objeto <code>Answer</code> con el <code>ResultSet</code> con los roles y la URL con el  menu
	 * del usuario, y una conexion abierta
	 */
	public static ArrayList<HashMap<String, String>> obtenerRoles (Connection con, String principal) throws SQLException 
	{

		logger.info("\n\nvalor de obtenerRoles >> "+obtenerRolesStr+" >> "+principal);
		ResultSetDecorator rs = null;
		PreparedStatementDecorator obtenerRolesStatement = null;
		ArrayList<HashMap<String, String>> resultados = new ArrayList<HashMap<String,String>>();
		
		try {
			obtenerRolesStatement = new PreparedStatementDecorator(
					con.prepareStatement(obtenerRolesStr,
							ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			obtenerRolesStatement.setString(1, principal);
			rs = new ResultSetDecorator(obtenerRolesStatement.executeQuery());
			
			while (rs.next()) {
				
				HashMap<String, String> temp = new HashMap<String, String>();
				temp.put("nombreFuncionalidad", rs.getString("nombreFuncionalidad"));
				temp.put("archivoFuncionalidad", rs.getString("archivoFuncionalidad"));
				temp.put("codigoFuncionalidad", rs.getString("codigoFuncionalidad"));
				resultados.add(temp);
				
			}
		} catch (SQLException e) {
			Log4JManager.error("Error obteniendo roles " + e);
		} finally {
			try {
				UtilidadBD.cerrarObjetosPersistencia(obtenerRolesStatement, rs, null);
			} catch (Exception e2) {
				Log4JManager.error("Error cerrando objetos de persistencia " + e2);
			}
		}

		return resultados;
	}
	
	/**
	 * Método para cargar los menús con sus respectivas funcionalidades
	 * para poder asignarlos al HashTable del MenuFilter
	 * @param con
	 * @return
	 */
	public static HashMap obtenerMenus (Connection con)
	{
		//columnas del listado
		String[] columnas={
				"func",
				"codigo",
				"modulo"
			};
		try
		{
			Statement st=con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(st.executeQuery(obtenerMenusStr)),false,true);
			return listado.getMapa();
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerMenus de SqlBaseMenuDao: "+e);
			return null;
		}
	}
}
