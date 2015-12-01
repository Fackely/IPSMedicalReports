/*
 * @(#)PostgresqlMenuDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.MenuDao;
import com.princetonsa.dao.sqlbase.SqlBaseMenuDao;

/**
 * Esta clase implementa el contrato estipulado en <code>MenuDao</code>, proporcionando los servicios
 * de acceso a una base de datos PostgreSQL requeridos por la clase <code>MenuFilter</code>
 *
 * @version 1.0, Sep 20, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class PostgresqlMenuDao implements MenuDao 
{
	 /**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(PostgresqlMenuDao.class);

	/**
	 * Cadena constante con la consulta necesaria para obtener los roles del usuario.
	 */
	private static final String obtenerRolesStr = "SELECT nombre_func AS nombreFuncionalidad, archivo_func AS archivoFuncionalidad, codigo_func AS codigoFuncionalidad FROM funcionalidades WHERE codigo_func IN (SELECT codigo_func FROM roles_funcionalidades WHERE nombre_rol IN (SELECT nombre_rol FROM roles_usuarios WHERE login = ?) GROUP BY codigo_func) GROUP BY funcionalidades.nombre_func, funcionalidades.archivo_func, funcionalidades.codigo_func ORDER BY nombre_func";

	/**
	 * Dado un login, obtiene los roles asociados a su respectivo usuario, y el URL del archivo con el menu
	 * correspondiente a dicho rol. Este metodo recibe una conexion abierta con una base de datos PostgreSQL,
	 * y la mantiene abierta despues de ejecutarse. Si no existe, la crea.
	 * @param con una conexion abierta con una base de datos PostgreSQL
	 * @param principal la representacion como cadena de texto de un objeto <code>java.security.Principal</code>
	 * @return un objeto <code>Answer</code> con el <code>ResultSet</code> con los roles y la URL con el  menu
	 * del usuario, y una conexion abierta
	 */
	public ArrayList<HashMap<String, String>> obtenerRoles (Connection con, String principal) throws SQLException 
	{
		return SqlBaseMenuDao.obtenerRoles (con, principal) ;
	}
	
	/**
	 * Método para cargar los menús con sus respectivas funcionalidades
	 * para poder asignarlos al HashTable del MenuFilter
	 * @param con
	 * @return
	 */
	public HashMap obtenerMenus ()
	{
		Connection con=PostgresqlUtilidadesBDDao.abrirConexion();
		HashMap mapa=new HashMap();
        if(con==null)
        {
            logger.error("Error abriendo la conexion ");
        }
        else
        {
        	mapa = SqlBaseMenuDao.obtenerMenus(con);
			PostgresqlUtilidadesBDDao.cerrarConexion(con);
        }
        return mapa;
	}

}