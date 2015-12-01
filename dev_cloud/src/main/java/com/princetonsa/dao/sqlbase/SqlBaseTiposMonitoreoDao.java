/*
 * Created on Aug 30, 2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;
import util.RespuestaHashMap;
import util.UtilidadBD;
import util.UtilidadTexto;

/**
 * @author Sebastián Gómez R
 *
 * Objeto usado para el acceso común a la fuente de datos
 * de la funcionalidad Tipos de Monitoreo
 */
public class SqlBaseTiposMonitoreoDao {
	
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseTiposMonitoreoDao.class);
	
	
	/**
	 * Cadena para consultar los tipos de monitoreo
	 */
	private static final String cargarTiposMonitoreoStr="SELECT tm.codigo As codigo, " +
																"tm.nombre As nombre, " +
																"tm.prioridad_cobro As prioridad, " +
																"tm.servicio AS servicio, " +
																"tm.requiere_valoracion AS reqval " +
														"FROM tipo_monitoreo tm " +
														"WHERE tm.institucion=? " +
														"ORDER BY tm.nombre ";
	
	/**
	 * Consulta los centros de costo por cada tipo de monitoreo
	 */
	private static String cargarCentrosPorTipo="SELECT ctm.tipo_monitoreo AS tipo," +
														"ctm.centro_costo AS centro," +
														"cc.identificador || ' - ' || cc.nombre AS nomcc, " +
														"cc.identificador AS idcc, " +
														"ca.codigo AS codca, " +
														"ca.descripcion AS descca," +
														"esUsadoCCTipoMonitoreo(ctm.tipo_monitoreo,ctm.centro_costo) as usado " +
												"FROM centros_costo_x_tipo_m ctm " +
														"INNER JOIN centros_costo cc ON (ctm.centro_costo=cc.codigo) " +
														"INNER JOIN centro_atencion ca ON (cc.centro_atencion=ca.consecutivo) ";
	
	/**
	 * Cadena para actualizar tipos de monitoreo
	 */
	private static final String actualizarTiposMonitoreoStr="UPDATE tipo_monitoreo SET nombre=?,prioridad_cobro=?,servicio=?,requiere_valoracion=? WHERE codigo=?";
	
	/**
	 * Cadena para actualizar los centros por tipo de monitoreo
	 */
	private static String actualizarCentros="UPDATE centros_costo_x_tipo_m SET centro_costo=? WHERE tipo_monitoreo=? AND centro_costo=? ";
	
	/**
	 * Cadena que elimina un registro de la tabla tipo_monitoreo
	 */
	private static final String eliminarTipoMonitoreoStr="DELETE FROM tipo_monitoreo WHERE codigo=?";

	/**
	 * Cadena para consultar un tipo de monitoreo específico por código
	 */
	private static final String cargarTipoMonitoreoStr="SELECT tm.codigo As codigo," +
																"tm.nombre As nombre," +
																"tm.prioridad_cobro As prioridad," +
																"tm.servicio AS servicio, " +
																"tm.requiere_valoracion AS reqval, " +
																"ctm.centro_costo AS cc " +
														"FROM tipo_monitoreo tm " +
																"INNER JOIN centros_costo_x_tipo_m ctm ON (tm.codigo=ctm.tipo_monitoreo) " +
														"WHERE codigo=? ";
	
	/**
	 * Cadena para verificar si el tipo de monitoreose está
	 * usando en otras tablas (orden_tipo_monitoreo y servicios_cama)
	 */
	private static final String revisarUsoTipoMonitoreoStr="SELECT " +
		"CASE WHEN " +
			"t.codigo IN (SELECT tipo_monitoreo FROM orden_tipo_monitoreo) OR " +
			"t.codigo IN (SELECT tipo_monitoreo FROM servicios_cama) " +
		"THEN 'true' ELSE 'false' END AS ocupado " +
		"FROM tipo_monitoreo t WHERE t.codigo=?";
	
	/**
	 * Cadena para Insercion de los Centros de Costo por cada Tipo de Monitoreo
	 */
	private static String insertarCentros="INSERT INTO centros_costo_x_tipo_m VALUES (?,?) ";
	
	/**
	 * Cadena para eliminar los centros de costo por Tipo de monitoreo 
	 */
	private static String eliminarCentro="DELETE FROM centros_costo_x_tipo_m WHERE tipo_monitoreo=? AND centro_costo=? ";
	
	/**
	 * Cadena para eliminar un todos los centros de un tipo de monitoreo
	 */
	private static String eliminarCentrosPorTipo="DELETE FROM centros_costo_x_tipo_m WHERE tipo_monitoreo=? ";
	
	
	/**
	 * Método usado para cargar los tipos de monitoreo por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap cargarTiposMonitoreo(Connection con,int institucion)
	{
		//columnas del listado
		String[] columnas={
				"codigo",
				"nombre",
				"prioridad",
				"servicio",
				"reqval"
			};
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(cargarTiposMonitoreoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,institucion);
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			return listado.getMapa();
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarTiposMonitoreo de SqlBaseUtilidadesDao: "+e);
			return null;
		}
	}
	
	/**
	 * Metodo que consulta los centros de costo por cada tipo de monitoreo
	 * @param con
	 * @return
	 */
	public static HashMap cargarCentrosPorTipo(Connection con)
	{
		String[] indices={"tipo_","centro_","nomcc_","idcc_","codca_","descca_","usado_"};
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator pst2;
		
		try{
			pst2 =  new PreparedStatementDecorator(con.prepareStatement(cargarCentrosPorTipo, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst2.executeQuery()),true, true);
			
			resultados.put("INDICES",indices);
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de centros por Tipo de Monitoreo");
		}
		return resultados;
	}
	
	/**
	 * Método usado para la actualización de los datos de un registros 
	 * en tipos monitoreos
	 * @param con
	 * @param codigo
	 * @param nombre
	 * @param prioridad
	 * @param servicio
	 * @return
	 */
	public static int actualizarTipoMonitoreo(Connection con,int codigo,String nombre,int prioridad,int servicio, int centroCosto,String check)
	{
		int rs=0;
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(actualizarTiposMonitoreoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,nombre);
			if(prioridad>0)
				pst.setInt(2,prioridad);
			else
				pst.setNull(2,Types.INTEGER);
			if(servicio>0)
				pst.setInt(3,servicio);
			else
				pst.setNull(3,Types.INTEGER);
			if(check.equals("1"))
				pst.setString(4,"S");
			else
				pst.setString(4,"N");
			pst.setInt(5,codigo);
			
			rs=pst.executeUpdate();
			
			if(rs>0)
				return 1;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarTiposMonitoreo de SqlBaseTiposMonitoreoDao: "+e);
		}
		return -1;
	}
	
	/**
	 * Método para insertar un nuevo tipo de monitoreo
	 * @param con
	 * @param nombre
	 * @param prioridad
	 * @param servicio
	 * @param institucion
	 * @param consulta
	 * @return
	 */
	public static int insertarTipoMonitoreo(
			Connection con,
			String nombre,
			int prioridad,
			int servicio,
			String check,
			int institucion,			
			String consulta)
	{
		try
		{
			int resp=0;
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,nombre);
			if(prioridad>0)
				pst.setInt(2,prioridad);
			else
				pst.setNull(2,Types.INTEGER);
			if(servicio>0)
				pst.setInt(3,servicio);
			else
				pst.setNull(3,Types.INTEGER);
			pst.setInt(4,institucion);
			if(check.equals("1"))
				pst.setString(5,"S");
			else
				pst.setString(5,"N");

			resp=pst.executeUpdate();
			if(resp>0)
				return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(con,"seq_tipo_monitoreo");
			else
				return -1;
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarTipoMonitoreo de SqlBaseTiposMonitoreoDao: "+e);
			return -1;
		}
	}
	
	/**
	 * Metodo para insertar los centros de costo por cada tipo de monitoreo
	 * @param con
	 * @param tipo
	 * @param centro
	 * @return
	 */
	public static boolean insertarCentros(Connection con, int tipo, int centro)
	{
		int resp=0;
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(insertarCentros,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,tipo);
			pst.setInt(2,centro);

			resp=pst.executeUpdate();
			if(resp>0)
				return true;
		}
		catch(SQLException e)
		{
			logger.info("");
		}
		return false;
	}
	
	/**
	 * Método para eliminar un registro de la tabla tipo_monitoreo
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static int eliminarTipoMonitoreo(Connection con,int codigo)
	{
		int res=0,res1=0;
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(eliminarTipoMonitoreoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigo);
			
			PreparedStatementDecorator pst1= new PreparedStatementDecorator(con.prepareStatement(eliminarCentrosPorTipo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst1.setInt(1,codigo);
			
			res1=pst1.executeUpdate();
			res=pst.executeUpdate();
			
			if(res>0 && res1>0)
				return 1;
		}
		catch(SQLException e)
		{
			logger.error("Error en eliminarTipoMonitoreo de SqlBaseTiposMonitoreoDao: "+e);
		}
		return -1;
	}
	
	/**
	 * Metodo para eliminar centros de costo por tipo  de monitoreo
	 * @param con
	 * @param tipo
	 * @param centro
	 * @return
	 */
	public static int eliminarCentro(Connection con, int tipo, int centro)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(eliminarCentro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,tipo);
			pst.setInt(2,centro);
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en eliminarCentros por Tipo de SqlBaseTiposMonitoreoDao: "+e);
			return -1;
		}
	}
	
	/**
	 * Método usado para cargar un tipo de monitoreo por su codigo
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static HashMap cargarTipoMonitoreo(Connection con,int codigo)
	{
		//columnas del listado
		String[] columnas={
				"codigo",
				"nombre",
				"prioridad",
				"servicio",
				"reqval",
				"cc"
			};
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(cargarTipoMonitoreoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigo);
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			return listado.getMapa();
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarTipoMonitoreo de SqlBaseUtilidadesDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método usado para revisar si el tipo de monitoreo se está utilizando
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean revisarUsoTipoMonitoreo(Connection con,int codigo)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(revisarUsoTipoMonitoreoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigo);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				return UtilidadTexto.getBoolean(rs.getString("ocupado"));
			else
				return false;
		}
		catch(SQLException e)
		{
			logger.error("Error en revisarUsoTipoMonitoreo de SqlBaseTiposMonitoreoDao: "+e);
			return false;
		}
	}

}
