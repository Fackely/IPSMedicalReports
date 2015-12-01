/*
 * Mayo 31, 2006
 */
package com.princetonsa.dao.sqlbase.historiaClinica;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;


/**
 * @author Sebastián Gómez 
 *
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la parametrización de Destino Triage
 */
public class SqlBaseDestinoTriageDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseDestinoTriageDao.class);
	
	/**
	 * Sección SELECT para la consulta de destinos triage
	 */
	private static final String cargarDestinosSELECT_Str = " SELECT "+ 
		"codigo, "+
		"nombre, "+
		"indicador_admi_urg, "+
		"institucion, " +
		"'true' AS es_bd, "+
		"CASE WHEN getEsUsadoDestinoTriage(consecutivo) > 0 THEN 'true' ELSE 'false' END AS es_usado "+ 
		"FROM destino_paciente ";
	
	/**
	 * Cadena que inserta un nuevo destino triage
	 */
	private static final String insertarDestinoStr = "INSERT INTO destino_paciente " +
		"(consecutivo,codigo,nombre,indicador_admi_urg,institucion) ";
	
	/**
	 * Cadena que modifica un registro destino triage
	 */
	private static final String modificarDestinoStr = "UPDATE destino_paciente " +
		"SET nombre = ?, indicador_admi_urg = ? " +
		"WHERE codigo = ? AND institucion = ?";
	
	/**
	 * Cadena que elimina un registro destino triage
	 */
	private static final String eliminarDestinoStr = "DELETE " +
		"FROM destino_paciente WHERE codigo = ? AND institucion = ?";
	
	/**
	 * Método implementado para cargar los destinos del paciente
	 * @param con
	 * @param institucion
	 * @param codigo
	 * @return
	 */
	public static HashMap cargar(Connection con,String institucion,String codigo)
	{
		Statement st = null;
		try
		{
			String consulta = cargarDestinosSELECT_Str + " WHERE institucion = "+institucion;
			
			//Si el código tiene valor quiere decir que se consultará un
			//destino específico
			if(!codigo.equals(""))
				consulta += " AND codigo = "+codigo;
			
			consulta += " ORDER BY codigo ASC ";
			
			 st = con.createStatement();
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)),true,false);
	     
			return mapaRetorno;

		}
		catch(SQLException e)
		{
			logger.error("Error en cargar de SqlBaseDestinoTriageDao: "+e);
			return null;
		}finally{
			if (st != null){
				try{
					st.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseDestinoTriageDao"+sqlException.toString() );
				}
			}
		}
	}
	
	/**
	 * Método implementado para insertar un destino triage
	 * @param con
	 * @param codigo
	 * @param nombre
	 * @param indicadorAdmiUrg
	 * @param institucion
	 * @param secuencia
	 * @return
	 */
	public static int insertar(Connection con,String codigo,String nombre,boolean indicadorAdmiUrg,String institucion,String secuencia)
	{
		PreparedStatementDecorator pst =   null;
		try
		{
			String consulta = insertarDestinoStr + "  VALUES("+secuencia+",?,?,?,?) ";
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			
			/**
			 * INSERT INTO destino_paciente " +
					"(consecutivo,codigo,nombre,indicador_admi_urg,institucion)
			 */
			
			pst.setDouble(1,Utilidades.convertirADouble(codigo));
			pst.setString(2,nombre);
			pst.setBoolean(3,indicadorAdmiUrg);
			pst.setInt(4,Utilidades.convertirAEntero(institucion));
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en insertar de SqlBaseDestinoTriageDao: "+e);
			return ConstantesBD.codigoNuncaValido;
		}finally{
			if (pst != null){
				try{
					pst.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseDestinoTriageDao"+sqlException.toString() );
				}
			}
		}
	}
	
	/**
	 * Método implementado para modificar un destino Triage
	 * @param con
	 * @param nombre
	 * @param indicadorAdminUrg
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public static int modificar(Connection con,String nombre,boolean indicadorAdminUrg, String codigo,String institucion)
	{PreparedStatementDecorator pst = null;
		try
		{
			pst =  new PreparedStatementDecorator(con.prepareStatement(modificarDestinoStr));
			
			/**
			 * UPDATE destino_paciente " +
		"SET nombre = ?, indicador_admi_urg = ? " +
		"WHERE codigo = ? AND institucion = ?";
			 */
			
			pst.setString(1,nombre);
			pst.setBoolean(2,indicadorAdminUrg);
			pst.setDouble(3,Utilidades.convertirADouble(codigo));
			pst.setInt(4,Utilidades.convertirAEntero(institucion));
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en modificar de SQlBaseDestinoTriageDao: "+e);
			return ConstantesBD.codigoNuncaValido;
		}finally{
			if (pst != null){
				try{
					pst.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseDestinoTriageDao"+sqlException.toString() );
				}
			}
		}
	}
	
	/**
	 * Método implementado para eliminar un registro destino triage
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public static int eliminar(Connection con,String codigo,String institucion)
	{
		PreparedStatementDecorator pst =   null;
		try
		{
			pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarDestinoStr));
			pst.setDouble(1,Utilidades.convertirADouble(codigo));
			pst.setInt(2,Utilidades.convertirAEntero(institucion));
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en eliminar de SqlBaseDestinoTriageDao: "+e);
			return ConstantesBD.codigoNuncaValido;
		}finally{
			if (pst != null){
				try{
					pst.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseDestinoTriageDao"+sqlException.toString() );
				}
			}
		}
	}
	
}
