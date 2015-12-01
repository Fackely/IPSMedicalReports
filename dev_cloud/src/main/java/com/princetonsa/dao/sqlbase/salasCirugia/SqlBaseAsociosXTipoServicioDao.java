package com.princetonsa.dao.sqlbase.salasCirugia;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.ValoresPorDefecto;

/**
 * 
 * @author juan sebastian castaño
 * Clase de SQL base para la funcionalidad de Asocios X Tipo de Servicio
 */

public class SqlBaseAsociosXTipoServicioDao {
	
	/**
	 * Log de la clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseAsociosXTipoServicioDao.class);
	
	private static String indices [] = {"codigo_","tipoServicio_","tipoAsocio_","servicio_","activo_","institucion_","nuevoRegistro_","eliminar_","codigoTipoServicio_","codigoTipoAsocio_","tiposServicioAsocio_","descripcionServicioCups_","codigoStringTipoServicio_"}; 
	
	private static final String  consulta = "select" +
														" sa.codigo as codigo, " +
														" ( select ts.nombre from tipos_servicio ts where ts.acronimo = sa.tipo_servicio) " +
														" as tipo_servicio," +
														" sa.tipo_servicio as codigo_tipo_servicio," +
														" ( select nombre_asocio from tipos_asocio ta where ta.codigo = sa.asocio) " +
														" as tipo_asocio, " +
														" ( select ta.tipos_servicio from tipos_asocio ta where ta.codigo = sa.asocio ) as tipos_servicio_asocio, " +
														" sa.asocio as codigo_tipo_asocio, " +
														" sa.servicio as servicio," +
														" sa.institucion as institucion," +
														
														" ( select rs.descripcion from referencias_servicio rs where servicio = sa.servicio and rs.tipo_tarifario = 0) as descripcion_servicio_cups, " +
														
														" '" + ConstantesBD.acronimoNo +"' as eliminar," +
														" '" + ConstantesBD.acronimoNo + "' as nuevo_Registro, " +
														
														" 'NoAsignado' as codigo_string_tipo_servicio" +
														
														
													" from " +
														" servicios_asocios sa " +
													" where " +
														" sa.institucion = ?";
	
	/**
	 * Consulta e indice sobre los tipos de servicio
	 */
	private static String indicesTiposServicio [] = {"acronimo_","nombre_"};
	
	private static final String consultaTiposServicio = "select" +
														 	" ts.acronimo as acronimo," +
														 	" ts.nombre as nombre" +
														 " From " +
														 	" tipos_servicio ts " +
														 " where " +
														 	" ts.acronimo = 'D' " +
														 " OR " +
														 	" ts.acronimo = 'Q' " +
														 " OR " +
														 	" ts.acronimo = 'R'";
	
	
	private static String indicesTiposAsocios [] = {"consecTipoAsocio_","codigoAsocio_","nombreAsocio_", "tipoServicioAsocio_"};
	
	/*private static final String consultaTiposAsocios = "select " +
															" ta.codigo as consec_tipo_asocio, " +
															" ta.codigo_asocio as codigo_asocio," +
															" ta.nombre_asocio as nombre_asocio" +
														" from " +
															" tipos_asocio ta " +
														" where " +
															" ta.institucion = ?" +
														"order by ta.codigo_asocio";*/
	
	private static final String consultaTiposAsocios = "select " +
															" ta.codigo as consec_tipo_asocio, " +
															" ta.codigo_asocio as codigo_asocio," +
															" ta.nombre_asocio as nombre_asocio," +
															" ta.tipos_servicio as tipo_servicio_asocio" +
														" from " +
															" tipos_asocio ta " +
														" where " +
															" ta.institucion = ?" +
														"order by ta.codigo_asocio";
	
	
	
	/**
	 * Metodo de consulta de todos los registros de servicios asociados
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap<String, Object> cargarServiciosAsocios(Connection con, int institucion)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst;
		
		try{
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1, institucion);
			// Cargar los resultados de la consulta en un hashMap
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de servicios asociados.");
		}
				
		resultados.put("INDICES",indices);
		//UtilidadBD.abortarTransaccion(con);
		return resultados;
	}



	/**
	 * Metodo para cargar todos los tipos de servicio en un select
	 * @param con
	 * @return
	 */
	
	public static HashMap<String, Object> cargarTiposServicio(Connection con)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst;
		
		try{
			pst =  new PreparedStatementDecorator(con.prepareStatement(consultaTiposServicio, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			// Cargar los resultados de la consulta en un hashMap
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de servicios asociados.");
		}
				
		resultados.put("INDICES",indices);
	
		
		
		return resultados;
	}
	
	/**
	 * Metodo para cargar todos los tipos de asocio dependiendo de la institucion en un select
	 * @param con
	 * @return
	 */
	
	public static HashMap<String, Object> cargarTiposAsocios(Connection con, int institucion)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();		
		PreparedStatementDecorator pst;		
		try{
			pst =  new PreparedStatementDecorator(con.prepareStatement(consultaTiposAsocios, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1, institucion);
			// Cargar los resultados de la consulta en un hashMap
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de servicios asociados.");
		}				
		resultados.put("INDICES",indicesTiposAsocios);		
		return resultados;
	}

	/**
	 * Metodo de registro de un nuevo asocio por tipo de servicio
	 * @param con
	 * @param institucion
	 * @param usuario
	 * @param secuencia
	 * @return
	 */
	public static boolean insertarTiposAsocios(Connection con,
									String secuencia,
									String tipo_servicio,
									int asocio,
									int servicio,
								
									int institucion, 
									String usuario)
	{
				
		PreparedStatementDecorator pst;
		String queryInsertar = "insert " +
									" into	servicios_asocios" +
									" ( codigo," +
									" tipo_servicio," +
									" asocio," +
									" servicio," +
									" institucion," +
									" fecha_modifica," +
									" hora_modifica," +
									" usuario_modifica) " +
								" values " +
									" ("+ secuencia +",?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?)";
		try{
			pst =  new PreparedStatementDecorator(con.prepareStatement(queryInsertar, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			pst.setString(1, tipo_servicio);
			pst.setInt(2, asocio);
			pst.setInt(3, servicio);
			pst.setInt(4, institucion);
			pst.setString(5, usuario);
			// Ejecutar insercion
			if (pst.executeUpdate() < 0)
				return false;
			else
				return true;
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en insercion de servicios asociados.");
			return false;
		}			
			
	}
	
	
	/**
	 * Metodo de eliminacion de un servicio Asocio
	 * @param con
	 * @param institucion
	 * @param codigo
	 * @return
	 */
	public static boolean eliminarServAsocio(Connection con, int institucion, int codigo)
	{
		PreparedStatementDecorator pst=null;
		String queryEliminacion = "delete " +
									" from" +
										" salascirugia.servicios_asocios " +
									" where " +
										" codigo =  ?" +
									" and" +
										" institucion = ?";
		try
		{
			pst =  new PreparedStatementDecorator(con.prepareStatement(queryEliminacion, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigo);
			pst.setInt(2,institucion);
			
			if(pst.executeUpdate() < 0)
				return false;
			
			return true;
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en la eliminacion de servicio asociado.");
			return false;
		}	
		
	}
	
	/**
	 * Metodo de modificacion de un registro de servicio asocios
	 * @param con
	 * @param codigo
	 * @param tipo_servicio
	 * @param asocio
	 * @param servicio
	 * @param institucion
	 * @param usuario
	 * @return
	 */
	public static boolean modificarServAsocio (Connection con, 
															int codigo,
															String tipo_servicio,
															int asocio,
															int servicio,
															int institucion, 
															String usuario)
	{
		
		System.out.print("  \n ente a modificarServAsocio codigo --> "+codigo+" tipoServicio --> "+tipo_servicio+" asocio--> "+asocio+" servicio -->"+servicio);
		
		String queryModificar = "update " +
									" salascirugia.servicios_asocios " +
								" set " +
									" tipo_servicio = ? , " +
									" asocio = ?," +
									" servicio = ?," +
									" institucion = ?," +
									" fecha_modifica = CURRENT_DATE, " +
									" hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
									" usuario_modifica = ?" +
								" where " +
									" codigo = ?";
		PreparedStatementDecorator pst = null;
		try
		{
			pst =  new PreparedStatementDecorator(con.prepareStatement(queryModificar, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setString(1, tipo_servicio);
			pst.setInt(2, asocio);
			pst.setInt(3, servicio);
			pst.setInt(4, institucion);
			pst.setString(5, usuario);
			
			pst.setInt(6, codigo);
			
			if (pst.executeUpdate() > 0)
				return true;
		}
		catch(SQLException e)
		{
			logger.error(" Error en la modificacion de servicio asociado. "+e);
			return false;
		}
		return false;
	}

}


