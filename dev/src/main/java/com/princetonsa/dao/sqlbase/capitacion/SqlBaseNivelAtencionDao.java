/*
 * Creado el Jun 13, 2006
 * por Julian Montoya
 */
package com.princetonsa.dao.sqlbase.capitacion;

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

public class SqlBaseNivelAtencionDao {

	/**
	 * Para Manejar los logs.
	 */
	public static Logger logger = Logger.getLogger(SqlBaseNivelAtencionDao.class); 

	/**
	 * Metodo para cargar los niveles de Atencion.
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap cargarInformacion(Connection con, int codigoInstitucion) 
	{
		//@todo : Se deben adicionar las tablas donde se utilice el nivel de atencion
		//		  para no permitir su eliminacion, desde esta funcionalidad.   	
		String consulta="	SELECT ns.consecutivo as consecutivo, ns.codigo as codigo, ns.descripcion as nivel, 					  " +
						"		   ns.descripcion as h_nivel, " +
								   "CASE WHEN (ns.activo||''='1' or ns.activo||''='true') then 'true' else 'false' end as activo, " +
								   "CASE WHEN (ns.activo||''='1' or ns.activo||''='true') then 'true' else 'false' end as h_activo, 	 	 					  " +
						" 	  	   CASE WHEN (nc.nivel_servicio IS NULL and insi.nivel_servicio is null and serv.nivel is null ) THEN 'true' ELSE 'false' END as puedeEliminar   " +						
						"		   FROM capitacion.nivel_atencion ns  										      				 	  " +
						" 		   		LEFT OUTER JOIN (SELECT nivel_servicio FROM capitacion.niveles_contratos GROUP BY nivel_servicio ) nc ON ( nc.nivel_servicio = ns.consecutivo ) " +
						" 		   		LEFT OUTER JOIN (SELECT nivel_servicio FROM instituciones_sirc GROUP BY nivel_servicio ) insi ON ( insi.nivel_servicio = ns.consecutivo ) " +
						" 		   		LEFT OUTER JOIN (SELECT nivel FROM servicios GROUP BY nivel) serv ON ( serv.nivel = ns.consecutivo ) " +
						"				WHERE institucion = " + codigoInstitucion +
						"					  ORDER BY ns.descripcion 												 				  ";

		logger.info("\n Nivel atencion-->"+consulta+"\n");
		PreparedStatementDecorator pst= null;
		try
		{
			pst= new PreparedStatementDecorator(con.prepareStatement(consulta));
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
			return mapaRetorno;

		}
		catch (SQLException e)
		{
			logger.error("Error consultando Informacion de Niveles de Atencion ["+ e + "]");
			return null;
		}finally{
			try{
				if(pst!=null){
					pst.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseNivelAtencionDao "+sqlException.toString() );
			}
			
		}
	}
	
	/**
	 * Metodo para insertar Niveles de Atencion.
	 * @param con
	 * @param tipoOperacion
	 * @param codigo
	 * @param descripcion
	 * @return
	 */
	public static int insertar(Connection con, int tipoOperacion, int codigo, String descripcion, boolean Activo, int institucion)
	{
		String consulta="";
		PreparedStatementDecorator pst= null;
		try
		{
			if ( tipoOperacion == 0 ) //-Para Insercion
			{
				consulta="	INSERT INTO capitacion.nivel_atencion (consecutivo, codigo, descripcion, institucion, activo) 	" +
						 "		 VALUES (" + UtilidadBD.obtenerSiguienteValorSecuencia(con, "capitacion.seq_nivel_servicio") +  ",?,?,?,"+ ValoresPorDefecto.getValorTrueParaConsultas() + ")	";
				
				pst= new PreparedStatementDecorator(con.prepareStatement(consulta));
				pst.setInt(1, codigo);
				pst.setString(2, descripcion);
				pst.setInt(3, institucion);
				return pst.executeUpdate();
			}
			if ( tipoOperacion == 1 ) //-Para Modificacion
			{
				consulta="	UPDATE capitacion.nivel_atencion SET descripcion = ?, activo = ? WHERE consecutivo = ? ";
				pst= new PreparedStatementDecorator(con.prepareStatement(consulta));
				pst.setString(1, descripcion);
				pst.setBoolean(2, Activo);
				pst.setDouble(3, Utilidades.convertirADouble(codigo+""));
				return pst.executeUpdate();
			}
			
			return -1; 
		}
		catch (SQLException e)
		{
			logger.error("Error Ingresando/Modificando Niveles de Atencion ["+e+"]");
			return ConstantesBD.codigoNuncaValido;
		}finally{
			try{
				if(pst!=null){
					pst.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseNivelAtencionDao "+sqlException.toString() );
			}
			
		}
	}
	
	
	/**
	 * Metodo para eliminar un nivel de atencion. 
	 * @param con
	 * @param nroReg
	 * @return
	 */

	public static int eliminar(Connection con, int nroReg)
	{
		PreparedStatementDecorator pst= null;
		try
		{
				String consulta=" DELETE FROM capitacion.nivel_atencion WHERE consecutivo = ? ";
				 pst= new PreparedStatementDecorator(con.prepareStatement(consulta));
				pst.setDouble(1, Utilidades.convertirADouble(nroReg+""));
				return pst.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error Eliminando Niveles de Atencion ["+e+"]");
			return ConstantesBD.codigoNuncaValido;
		}finally{
			try{
				if(pst!=null){
					pst.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseNivelAtencionDao "+sqlException.toString() );
			}
			
		}
	}

}
