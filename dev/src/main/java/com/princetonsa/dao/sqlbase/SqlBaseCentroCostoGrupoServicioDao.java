/*
 * Creado en May 10, 2006
 * Andrés Mauricio Ruiz Vélez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.RespuestaHashMap;
import util.UtilidadBD;

public class SqlBaseCentroCostoGrupoServicioDao
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseCentroCostoGrupoServicioDao.class);
	
	/**
	 * Método que cargar los centros de costo x grupos de servicio parametrizados en la institución 
	 * para el centro de atención seleccionado
	 * @param con
	 * @param centroAtencion
	 * @return HashMap
	 */
	public static HashMap consultarCentrosCostoGrupoServicio(Connection con, int centroAtencion)
	{
		String consultaStr="SELECT " +
								"gs.codigo AS codigo_gservicio, " +
								"gs.descripcion AS nombre_gservicio, " +
								"cc.codigo AS codigo_ccosto, " +
								"cc.identificador AS identificador, " +
								"cc.nombre AS nombre_ccosto, " +
								"caa.codigo AS codigo_catencion, " +
								"caa.descripcion AS nombre_catencion, " +
								"ccgs.centro_atencion AS consecutivo_catencion, " +
								"ccgs.consecutivo AS consecutivo, " + //61826
								"1 AS esta_grabado " +
							"FROM " +
								"centro_costo_grupo_ser ccgs " +
								"INNER JOIN grupos_servicios gs ON (ccgs.grupo_servicio=gs.codigo) " +
								"INNER JOIN centros_costo cc ON (ccgs.centro_costo=cc.codigo) " +
								"INNER JOIN centro_atencion caa ON (cc.centro_atencion=caa.consecutivo) " +
							"WHERE " +
								"ccgs.centro_atencion = "+centroAtencion+" "+ 
								"AND cc.tipo_area = "+ConstantesBD.codigoTipoAreaDirecto+" " +
							"ORDER BY " +
								"gs.descripcion, identificador";	
					
		//---Columnas---//
		String[] columnas = {"codigo_gservicio", "nombre_gservicio", "codigo_ccosto", "identificador", "nombre_ccosto", "codigo_catencion", "nombre_catencion", "consecutivo_catencion", "consecutivo", "esta_grabado"};
		logger.info("999999999999999999999999999");
		logger.info("====>Consulta Centros de Costo por Grupo: "+consultaStr);
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			return listado.getMapa();
		}
		catch (SQLException e)
		{
			logger.error("Error en consultarCentrosCostoGrupoServicio de SqlBaseCentroCostoGrupoServicioDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método que inserta el centros de costo x grupo de servicio al centro de atención seleccionado
	 * @param con
	 * @param centroAtencion
	 * @param grupoServicio
	 * @param centroCosto
	 * @return
	 */
	public static int insertarCentroCostoGrupoServicio(Connection con, int centroAtencion, int grupoServicio, int centroCosto, int consecutivo) {
		PreparedStatementDecorator ps;
		int resp=-1;
		
		try {
			String	consultaStr="INSERT INTO centro_costo_grupo_ser (centro_atencion, grupo_servicio, centro_costo, consecutivo) " +
															" VALUES (?, ?, ?, ?)" ;
					
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, centroAtencion);
			ps.setInt(2, grupoServicio);
			ps.setInt(3, centroCosto);
			ps.setInt(4, consecutivo);
			
			logger.info(">> cadena a Insertar: " + consultaStr);
									
			resp = ps.executeUpdate();
		}
		catch(SQLException e) {
				logger.warn(e+" Error en la inserción de datos en insertarCentroCostoGrupoServicio : SqlBaseCentroCostoGrupoServicioDao "+e.toString() );
				resp = -1;
		}
		return resp;
	}
	
	
	
	/**
	 * Método que elimina el registro seleccionado de centros costo x grupo servicio
	 * @param con
	 * @param centroAtencionElim
	 * @param grupoServicioElim
	 * @param centroCostoElim
	 * @return
	 */
	public static int eliminarCentroCostoGrupoServicio(Connection con, int centroAtencionElim, int grupoServicioElim, int centroCostoElim)
	{
		PreparedStatementDecorator ps;
		int resp=-1;
		
		try
		{
			String	consultaStr="DELETE FROM centro_costo_grupo_ser " +
													"WHERE centro_atencion = ? AND grupo_servicio = ? AND centro_costo = ?" ;
					
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, centroAtencionElim);
			ps.setInt(2, grupoServicioElim);
			ps.setInt(3, centroCostoElim);

			logger.info(">> cadena a Eliminar: " + consultaStr);
			resp = ps.executeUpdate();
		}
		catch(SQLException e)
		{
				logger.warn(e+" Error en la eliminación del registro de Centros Costo x Grupos Servicio: SqlBaseCentroCostoGrupoServicioDao "+e.toString() );
				resp = -1;
		}
		return resp;
	}
	
	

	

	/**
	 * Método que actualiza el consecutivo del centro de costo x grupo de servicio al centro de atención seleccionado
	 * @param con
	 * @param centroAtencion
	 * @param grupoServicio
	 * @param centroCosto
	 * @return
	 */
	public static int actualizarCentroCostoGrupoServicio(Connection con, int centroAtencion, int grupoServicio, int centroCosto, int consecutivo) {
		PreparedStatementDecorator ps;
		int resp=-1;
		
		try {
			String	consultaStr="UPDATE centro_costo_grupo_ser SET consecutivo=? WHERE " +
					" centro_atencion=? AND " +
					" grupo_servicio=? AND " +
					" centro_costo=? ";

			ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, consecutivo);
			ps.setInt(2, centroAtencion);
			ps.setInt(3, grupoServicio);
			ps.setInt(4, centroCosto);
			
			logger.info(">> cadena a Actualizar: " + consultaStr);
									
			resp = ps.executeUpdate();
		}
		catch(SQLException e) {
				logger.warn(e+" Error en la inserción de datos en insertarCentroCostoGrupoServicio : SqlBaseCentroCostoGrupoServicioDao "+e.toString() );
				resp = -1;
		}
		return resp;
	}
	
	
	
	
}