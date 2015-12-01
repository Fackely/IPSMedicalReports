/*
 * Nov 09, 2006 
 */
package com.princetonsa.dao.sqlbase.historiaClinica;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * @author Sebastián Gómez 
 *
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la parametrización de instituciones SIRC
 */
public class SqlBaseInstitucionesSircDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseInstitucionesSircDao.class);
	
	/**
	 * Cadena que consulta las instituciones sirc parametrizadas por institucion
	 */
	private static final String cargarInstitucionesStr = "SELECT "+		
		"i.codigo, "+
		"i.institucion, "+
		"i.descripcion, "+
		"i.nivel_servicio as codigo_nivel, "+
		"n.descripcion as nombre_nivel, "+
		"i.tipo_red as codigo_tipo_red, " +
		"i.tipo_inst_referencia as codigo_tipo_refe, " +
		"i.tipo_inst_ambulancia as codigo_tipo_ambu, "+		
		"i.activo, "+
		"'true' as existe, "+ 
		"'false'  as es_usado "+  
		"FROM instituciones_sirc i "+ 
		"INNER JOIN nivel_atencion n ON(n.consecutivo=i.nivel_servicio) "+		
		"WHERE i.institucion = ? ORDER BY i.descripcion ";
	
	/**
	 * Cadena que carga los datos de una institucion SIRC
	 */
	private static final String cargarInstitucionStr = "SELECT "+		
		"i.codigo, "+
		"i.institucion, "+
		"i.descripcion, "+
		"i.nivel_servicio as codigo_nivel, "+
		"n.descripcion as nombre_nivel, "+
		"i.tipo_red as codigo_tipo_red, " +
		"i.tipo_inst_referencia as codigo_tipo_refe, " +
		"i.tipo_inst_ambulancia as codigo_tipo_ambu, "+		
		"i.activo, "+
		"'true' as existe, "+ 
		"'false'  as es_usado "+  
		"FROM instituciones_sirc i "+ 
		"INNER JOIN nivel_atencion n ON(n.consecutivo=i.nivel_servicio) "+		
		"WHERE i.codigo =? and i.institucion = ? ORDER BY i.descripcion ";		
	
	/**
	 * Cadena que inserta una institucion SIRC
	 */
	private static final String insertarStr = "INSERT INTO " +
		"instituciones_sirc " +
		"(codigo,institucion,descripcion,nivel_servicio,tipo_red,tipo_inst_referencia,tipo_inst_ambulancia,activo) " +
		"VALUES ";
	
	/**
	 * Cadena que modifica una institucion SIRC
	 */
	private static final String modificarStr = "UPDATE " +
		"instituciones_sirc SET " +
		"descripcion = ?, nivel_servicio = ?, tipo_red = ?, tipo_inst_referencia=?, tipo_inst_ambulancia=?, activo = ? " +
		"WHERE codigo = ? AND institucion = ?";
	
	/**
	 * Cadena que elimina una institucion SIRC
	 */
	private static final String eliminarStr = "DELETE FROM instituciones_sirc WHERE codigo=? AND institucion = ?";
	
	/**
	 * Cadena que carga los niveles de servicio
	 */
	private static final String cargarNivelesServicioStr = "SELECT "+ 
		"consecutivo, "+
		"codigo, "+
		"descripcion "+ 
		"FROM nivel_atencion WHERE institucion = ? and activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" ORDER BY descripcion";
	
	/**
	 * Método que carga las instituciones SIRC por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap cargarInstituciones(Connection con,int institucion)
	{
		PreparedStatementDecorator ps= null;
		try
		{
			 ps =  new PreparedStatementDecorator(con.prepareStatement(cargarInstitucionesStr));
			ps.setInt(1,institucion);
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);
			
			return mapaRetorno;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarInstituciones de SqlBaseInstitucionesSircDao: "+e);
			return null;
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
			
		}
	}
	
	/**
	 * Método que carga los datos de una institucion SIRC
	 * @param con
	 * @param campos (codigo e institucion)
	 * @return
	 */
	public static HashMap cargarInstitucion(Connection con,HashMap campos)
	{
		PreparedStatementDecorator pst= null;
	
		try
		{
			 pst =  new PreparedStatementDecorator(con.prepareStatement(cargarInstitucionStr));
			pst.setString(1,campos.get("codigo")+"");
			pst.setInt(2,Utilidades.convertirAEntero(campos.get("institucion")+""));
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarInstitucion de SqlBaseInstitucionesSircDao: "+e);
			return null;
		}finally{
			if (pst != null){
				try{
					pst.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
			
		}
	}
	
	/**
	 * Método que inserta una institucion SIRC
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int insertar(Connection con,HashMap campos)
	{
		PreparedStatementDecorator pst= null;

		try
		{
			String consulta = insertarStr + " (?,?,?,?,?,?,?,?) ";
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));			
			
			/**
			 * INSERT INTO instituciones_sirc (
			 * codigo,
			 * institucion,
			 * descripcion,
			 * nivel_servicio,
			 * tipo_red,
			 * tipo_inst_referencia,
			 * tipo_inst_ambulancia,
			 * activo) " +
				"VALUES 
			 */
			
			pst.setString(1,campos.get("codigo")+"");
			pst.setInt(2,Utilidades.convertirAEntero(campos.get("institucion")+""));
			pst.setString(3,campos.get("descripcion")+"");
			pst.setDouble(4,Utilidades.convertirADouble(campos.get("nivel")+""));
			pst.setString(5,campos.get("red")+"");
			pst.setString(6,campos.get("referencia")+"");
			pst.setString(7,campos.get("ambulancia")+"");
			pst.setString(8,campos.get("activo")+"");
			
			int resp = pst.executeUpdate();			
			return resp;
		}
		catch(SQLException e)
		{
			logger.error("Error en insertar de SqlBaseInstitucionesSircDao: "+e);
			return 0;
		}finally{
			if (pst != null){
				try{
					pst.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
		
		}
	}
	
	/**
	 * Método que modifica una institucion SIRC
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int modificar(Connection con,HashMap campos)
	{
		PreparedStatementDecorator pst= null;
	
		try
		{
			pst =  new PreparedStatementDecorator(con.prepareStatement(modificarStr));			
			
			/**
			 * UPDATE instituciones_sirc SET descripcion = ?, 
			 * nivel_servicio = ?, 
			 * tipo_red = ?, 
			 * tipo_inst_referencia=?, 
			 * tipo_inst_ambulancia=?, 
			 * activo = ? WHERE codigo = ? AND institucion = ?"
			 */
			
			pst.setString(1,campos.get("descripcion")+"");
			pst.setDouble(2,Utilidades.convertirADouble(campos.get("nivel")+""));
			pst.setString(3,campos.get("red")+"");
			pst.setString(4,campos.get("referencia")+"");
			pst.setString(5,campos.get("ambulancia")+"");
			pst.setString(6,campos.get("activo")+"");
			
			pst.setString(7,campos.get("codigo")+"");
			pst.setInt(8,Utilidades.convertirAEntero(campos.get("institucion")+""));
			
			return pst.executeUpdate();			
		}
		catch(SQLException e)
		{
			logger.error("error en modificar de SqlBaseInstitucionesSircDao: "+e);
			return 0;
		}finally{
			if (pst != null){
				try{
					pst.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
		
		}
	}
	
	/**
	 * Método que elimina una institucion SIRC
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public static int eliminar(Connection con,String codigo, int institucion)
	{
		PreparedStatementDecorator pst= null;
		try
		{
			pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarStr));
			pst.setString(1,codigo);
			pst.setInt(2,institucion);
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en eliminar de SqlBaseInstitucionesSircDao: "+e);
			return 0;
		}finally{
			if (pst != null){
				try{
					pst.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
		
		}
	}
	
	/**
	 * Método que carga los niveles de servicio por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap cargarNivelesServicio(Connection con,int institucion)
	{PreparedStatementDecorator pst= null;
		try
		{
			pst =  new PreparedStatementDecorator(con.prepareStatement(cargarNivelesServicioStr));
			pst.setInt(1,institucion);
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarNivelesServicio de SqlBaseInstitucionesSircDao: "+e);
			return null;
		}finally{
			if (pst != null){
				try{
					pst.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
			
		}
	}
	
}
