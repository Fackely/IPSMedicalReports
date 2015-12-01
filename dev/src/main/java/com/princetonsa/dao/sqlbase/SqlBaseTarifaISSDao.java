/*
 * @(#)SqlBaseTarifaISSDao.java
 * 
 * Created on 03-May-2004
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados
 * 
 * Lenguaje : Java
 * Compilador : J2SDK 1.4
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.ResultadoCollectionDB;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;


/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 * para una tarifa de tipo iss
 * 
 * @version 1.0, Mayo 03 / 2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class SqlBaseTarifaISSDao
{
	
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseTarifaISSDao.class);
	
	private static String buscarTarifaISSStr = "SELECT " +
													"ti.codigo AS codigo, " +
													"ti.esquema_tarifario AS codigoEsquemaTarifario, " +
													"et.nombre AS nombreEsquemaTarifario, " +
													"ti.valor_tarifa AS valorTarifa, " +
													"ti.porcentaje_iva AS porcentajeIva, " +
													"ti.servicio AS codigoServicio, " +
													"rs.descripcion AS nombreServicio, " +
													"e.codigo AS codigoEspecialidad, " +
													"e.nombre AS nombreEspecialidad, " +
													"ti.tipo_liquidacion AS codigoTipoLiquidacion, " +
													"tl.nombre AS nombreTipoLiquidacion," +
													"ti.unidades AS unidades," +
													"ti.liq_asocios AS liqAsocios, " +
													"ti.fecha_vigencia AS fechavigencia "+
												"FROM " +
													"tarifas_iss ti " +
													"INNER JOIN esquemas_tarifarios et ON (ti.esquema_tarifario = et.codigo) " +
													"INNER JOIN servicios s ON (ti.servicio = s.codigo) " +
													"INNER JOIN referencias_servicio rs ON (rs.servicio = s.codigo) " +
													"INNER JOIN especialidades e ON (s.especialidad = e.codigo) " +
													"INNER JOIN tipos_liquidacion_soat tl ON (tl.codigo = ti.tipo_liquidacion) " +
												"WHERE ";
	
	/**
	 * Consutla que retorna el número de tarifas para el esquema tarifario y el servicio hay definidas. 
	 * Si hay deberia ser 1, pero por si algose mete el contador 
	 */
	private static String consultaExisteTarifaDefinida = 	"SELECT COUNT(*) AS numTarifasDefinidas " +
																					"FROM tarifas_iss ti " +
																					"WHERE ti.esquema_tarifario = ? " +
																					"AND ti.servicio = ? ";
	
	/**
	 * Consulta todos los campos de la tarifa iss dada por el esquema tarifario y el servicio.
	 * Como deberia haber solo una definida para esos datos, retorna 0 o 1 filas.
	 */
	private static String consultaTarifaPorETyS =	"SELECT ti.codigo AS codigo, " +
																			"ti.esquema_tarifario AS codigoEsquemaTarifario, " +
																			"et.nombre AS nombreEsquemaTarifario, " +
																			"ti.valor_tarifa AS valorTarifa, " +
																			"ti.porcentaje_iva AS porcentajeIva, " +
																			"ti.servicio AS codigoServicio, " +
																			"rs.descripcion AS nombreServicio, " +
																			"e.codigo AS codigoEspecialidad, " +
																			"e.nombre AS nombreEspecialidad, " +
																			"ti.tipo_liquidacion AS codigoTipoLiquidacion," +
																			"ti.unidades AS unidades," +
																			"ti.liq_asocios AS liqAsocios " +
																			"FROM tarifas_iss ti, esquemas_tarifarios et, referencias_servicio rs, servicios s, especialidades e " +
																			"WHERE ti.esquema_tarifario = ? " +
																			"AND ti.servicio = ? " +
																			"AND ti.servicio = s.codigo " +
																			"AND et.codigo = ti.esquema_tarifario " +
																			"AND rs.servicio = ti.servicio " +
																			"AND s.especialidad = e.codigo " +
																			"AND rs.tipo_tarifario ="+ConstantesBD.codigoTarifarioCups+" ";
	
	/**
	 * Consulta todos los campos de la tarifa iss dada por el código.
	 * Como deberia haber solo una definida para esos datos, retorna 0 o 1 filas.
	 */
	private static String consultaTarifaPorCodigo =	"SELECT ti.codigo AS codigo, " +
																				"ti.esquema_tarifario AS codigoEsquemaTarifario, " +
																				"et.nombre AS nombreEsquemaTarifario, " +
																				"ti.valor_tarifa AS valorTarifa, " +
																				"ti.porcentaje_iva AS porcentajeIva, " +
																				"ti.servicio AS codigoServicio, " +
																				"rs.descripcion AS nombreServicio, " +
																				"e.codigo AS codigoEspecialidad, " +
																				"e.nombre AS nombreEspecialidad," +
																				"ti.unidades AS unidades, " +
																				"ti.liq_asocios AS liqAsocios " +
																				"FROM tarifas_iss ti, esquemas_tarifarios et, referencias_servicio rs, servicios s, especialidades e " +
																				"WHERE ti.codigo = ? " +
																				"AND ti.servicio = s.codigo " +
																				"AND et.codigo = ti.esquema_tarifario " +
																				"AND rs.servicio = ti.servicio " +
																				"AND s.especialidad = e.codigo " +
																				"AND rs.tipo_tarifario ="+ConstantesBD.codigoTarifarioCups+" ";
	
	private static String modificarTarifaStr = 	"UPDATE tarifas_iss " +
																	"SET esquema_tarifario = ?, " +
																	"valor_tarifa = ?, " +
																	"porcentaje_iva = ?, " +
																	"servicio = ?, " +
																	"tipo_liquidacion = ?, " +
																	"unidades = ?," +
																	"liq_asocios = ?, " +
																	"usuario_modifica = ?," +
																	"fecha_modifica = ?," +
																	"hora_modifica = ?," +
																	"fecha_vigencia = ? " +
																	"WHERE codigo = ? ";
																	
	private static String eliminarTarifaStr = "DELETE FROM tarifas_iss " +
																"WHERE codigo = ? ";
	
	/**
	 * Cadena pa la insercion del log tipo BD de servicios.
	 */
	private static final String cadenaLogStr = "INSERT INTO log_tarifas_servicios " +
												"(codigo, " +
												"esquema_tarifario, " +
												"servicio, " +
												"tipo_liquidacion, " +
												"valor_tarifa, " +
												"liq_asocios, " +
												"tipo_liquidacion_antes, " +
												"valor_tarifa_antes, " +
												"liq_asocios_antes, " +
												"usuario_modifica, " +
												"fecha_modifica, " +
												"hora_modifica, " +
												"fecha_vigencia) " +
											"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+",?)";
	
	/**
	 * Modifica una tarifa iss dado su código
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoTarifa, int, código de la tarifa a modificar
	 * @param codigoEsquemaTarifario. int, código del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, código del servicio asociado a esta tarifa
	 * @param valorTarifa. double, valor de esta tarifa
	 * @param login Usuario
	 * @param porcentajeIva. double, porcentaje del iva de esta tarifa
	 * @param codigo Tipo Liquidacion
	 * @param unidades
	 * @param tipo liquidacion original
	 * @param valor tarifa original
	 * @param Liquidar Asocios 
	 * @param Liquidar Asocios Original
	 * @return ResultadoBoolean, true si la modificación fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.TarifaISSDao#modificar(java.sql.Connection, int, int, int, int, double, double)
	 */
	public static ResultadoBoolean modificar(	Connection con,
																		int codigoTarifa,
																		int codigoEsquemaTarifario,
																		int codigoServicio,
																		double valorTarifa,
																		String loginUsuario,
																		double porcentajeIva,
																		int codigoTipoLiquidacion,
																		double unidades,
																		int tipoLiquidacionOriginal,
																		double valorTarifaOriginal,
																		String liqAsocios,
																		String liqAsociosOriginal,
																		String fecha_vigencia)
	{
		logger.info("\n\nDATOS PARA LA MODIFICACION>>>>> "+codigoTarifa+" >> "+codigoEsquemaTarifario+" >> "+codigoServicio+" >> "+valorTarifa+" " +
				">> "+loginUsuario+" >> "+porcentajeIva+" >> "+codigoTipoLiquidacion+" >> "+unidades+" >> "+tipoLiquidacionOriginal+" " +
						">> "+valorTarifaOriginal+" >> "+liqAsocios+" >> "+liqAsociosOriginal+" >> "+fecha_vigencia);
		logger.info("\n\nCADENA UPDATE>>>>>>>>>"+modificarTarifaStr);
		int insertoLog=ConstantesBD.codigoNuncaValido;
	    try
		{			
			PreparedStatementDecorator modificar =  new PreparedStatementDecorator(con.prepareStatement(modificarTarifaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
			modificar.setInt(1, codigoEsquemaTarifario);
			if(valorTarifa < 0)
				modificar.setNull(2,Types.DOUBLE);
			else
				modificar.setDouble(2, valorTarifa);
			modificar.setDouble(3, porcentajeIva);
			modificar.setInt(4, codigoServicio);
			
			if( codigoTipoLiquidacion <= 0 ) // No definido
				modificar.setString(5, null);
			else
				modificar.setInt(5, codigoTipoLiquidacion);
			
			if( unidades < 0)
				modificar.setNull(6, Types.DOUBLE);
			else
				modificar.setDouble(6,unidades);

			modificar.setObject(7,liqAsocios);
			
			modificar.setObject(8,loginUsuario);
			
			modificar.setObject(9,UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			
			modificar.setObject(10,UtilidadFecha.getHoraActual());
			
			if(!fecha_vigencia.equals(""))
				modificar.setString(11, UtilidadFecha.conversionFormatoFechaABD(fecha_vigencia));
			else
				modificar.setNull(11, Types.DATE);
						
			modificar.setInt(12, codigoTarifa);			
			
			int update = modificar.executeUpdate();
					
			if( update == 0 )
				return new ResultadoBoolean(false);
						
			if(update>0)
			{
				insertoLog=logTarifasServicios(con, codigoEsquemaTarifario, codigoServicio, codigoTipoLiquidacion, valorTarifa, liqAsocios, tipoLiquidacionOriginal, valorTarifaOriginal,liqAsociosOriginal, loginUsuario, fecha_vigencia);
			}
			
			if(update>0 && insertoLog>0)
			{
				return new ResultadoBoolean(true);
			}
			
		}
		catch(SQLException e)
		{
			logger.warn("No se modificó la  tarifa iss"+e);
			return new ResultadoBoolean(false, "No se modificó la tarifa iss\n"+e);
		}
		return new ResultadoBoolean(true);
	}

	/**
	 * Modifica una tarifa iss dado su código dentro de una transacción dado su estado.
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoTarifa, int, código de la tarifa a modificar
	 * @param codigoEsquemaTarifario. int, código del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, código del servicio asociado a esta tarifa
	 * @param valorTarifa. double, valor de esta tarifa
	 * @param login usuario
	 * @param porcentajeIva. double, porcentaje del iva de esta tarifa
	 * @param codigo tipo liquidacion
	 * @param unidades
	 * @param Liquidacion Asocios
	 * @param tipo liquidacion original
	 * @param valor tarifa original
	 * @param liquidacion asocios original
	 * @param estado. String, estado dentro de la transacción
	 * @return ResultadoBoolean, true si la modificación fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.TarifaISSDao#modificarTransaccional(java.sql.Connection, int, int, int, int, double, double, java.lang.String)
	 */
	public static ResultadoBoolean modificarTransaccional(	Connection con,
																								int codigoTarifa,
																								int codigoEsquemaTarifario,
																								int codigoServicio,
																								double valorTarifa,
																								String loginUsuario,
																								double porcentajeIva,
																								int codigoTipoLiquidacion,
																								double unidades,
																								String liqAsocios,
																								int tipoLiquidacionOriginal,
																								double valorTarifaOriginal,
																								String liqAsociosOriginal,
																								String estado,
																								String fecha_vigencia) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		if( estado != null && estado.equals(ConstantesBD.inicioTransaccion) )
		{
			if( !myFactory.beginTransaction(con))
			{
				return new ResultadoBoolean(false);			
			}
		}
		int insertoLog=ConstantesBD.codigoNuncaValido;
		try
		{			
			PreparedStatementDecorator modificar =  new PreparedStatementDecorator(con.prepareStatement(modificarTarifaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
			modificar.setInt(1, codigoEsquemaTarifario);
			if(valorTarifa < 0)
				modificar.setNull(2,Types.DOUBLE);
			else
				modificar.setDouble(2, valorTarifa);
			modificar.setDouble(3, porcentajeIva);
			modificar.setInt(4, codigoServicio);
			
			if( codigoTipoLiquidacion <= 0 ) // No definido
				modificar.setString(5, null);
			else
				modificar.setInt(5, codigoTipoLiquidacion);
			
			if( unidades <= 0)
				modificar.setNull(6, Types.DOUBLE);
			else
				modificar.setDouble(6,unidades);
			
			modificar.setObject(7,liqAsocios);
			
			modificar.setObject(8,loginUsuario);
			
			modificar.setObject(9,UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			
			modificar.setObject(10,UtilidadFecha.getHoraActual());
			
			modificar.setString(11, UtilidadFecha.conversionFormatoFechaABD(fecha_vigencia));
			
			modificar.setInt(12, codigoTarifa);
						
			int update = modificar.executeUpdate();
					
			if( update == 0 )
			{
				myFactory.abortTransaction(con);
				return new ResultadoBoolean(false);
			}
			if(update>0)
			{
				insertoLog=logTarifasServicios(con, codigoEsquemaTarifario, codigoServicio, codigoTipoLiquidacion, valorTarifa, liqAsocios, tipoLiquidacionOriginal, valorTarifaOriginal, liqAsociosOriginal, loginUsuario, fecha_vigencia);
			}
			
			if(update>0 && insertoLog>0)
			{
				return new ResultadoBoolean(true);
			}
		}
		catch(SQLException e)
		{
			myFactory.abortTransaction(con);
			logger.warn("No se insertó la tarifa  iss"+e);
			return new ResultadoBoolean(false, "No se insertó la tarifa iss\n"+e);
		}		
		
		if( estado !=null && estado.equals(ConstantesBD.finTransaccion) )
		{
		    myFactory.endTransaction(con);
		} 
		return new ResultadoBoolean(true);
	}

	/**
	 * Elimina una tarifa dado el código
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoTarifa. int, código de la tarifa a eliminar
	 * @return ResultadoBoolean, true si la modificación fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.TarifaISSDao#eliminar(java.sql.Connection, int)
	 */
	public static ResultadoBoolean eliminar(Connection con, int codigoTarifa)
	{
		try
		{			
			PreparedStatementDecorator eliminar =  new PreparedStatementDecorator(con.prepareStatement(eliminarTarifaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
			eliminar.setInt(1, codigoTarifa);			

			int update = eliminar.executeUpdate();
					
			if( update == 0 )
				return new ResultadoBoolean(false);
		}
		catch(SQLException e)
		{
			logger.warn("No se eliminó la tarifa iss"+e);
			return new ResultadoBoolean(false, "No se eliminó la tarifa iss\n"+e);
		}
		return new ResultadoBoolean(true);
	}

	/**
	 * Consulta una tarifa iss dado el código
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoTarifa. int, código de la tarifa a consultar
	 * @return ResultadoCollectionDB, true y con la colección de HashMap con el resultado si fue exitosa
	 * la consulta, false y con la descripción de lo contrario
	 * @see com.princetonsa.dao.TarifaISSDao#consultar(java.sql.Connection, int)
	 */
	public static ResultadoCollectionDB consultar(Connection con, int codigoTarifa)
	{
		ResultSetDecorator resultado = null;
		try
		{
			PreparedStatementDecorator consultaTarifaSttmnt =  new PreparedStatementDecorator(con.prepareStatement(consultaTarifaPorCodigo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			consultaTarifaSttmnt.setInt(1, codigoTarifa);

			resultado = new ResultSetDecorator(consultaTarifaSttmnt.executeQuery());
			
		}
		catch( SQLException sqe )
		{
			logger.warn("No se pudo realizar la consulta, problemas con la bd "+sqe.getMessage());
			return new ResultadoCollectionDB(false, "No se pudo realizar la consulta, problemas con la bd "+sqe.getMessage());
		}		
		
		try
		{
			Collection col = UtilidadBD.resultSet2Collection(resultado);
			if( col != null && !col.isEmpty() )
			{
				return new ResultadoCollectionDB(true, "", col);
			}
			else
			{
				return new ResultadoCollectionDB(false);
			}
		}
		catch(Exception e)
		{
			logger.warn(" Error en Consultar tarifa iss al convertir el result set a una colección\n"+e.getMessage());
			return new ResultadoCollectionDB(false, " Error en Consultar tarifa iss al convertir el result set a una colección\n"+e.getMessage());
		}
	}

	/**
	 * Consulta una tarifa iss dado el código del esquema tarifario y el codigo del servicio
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoEsquemaTarifario. int, código del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, código del servicio asociado a esta tarifa
	 * @return ResultadoCollectionDB, true y con la colección de HashMap con el resultado si fue exitosa
	 * la consulta, false y con la descripción de lo contrario
	 */
	public static ResultadoCollectionDB consultar(	Connection con, 
																				int codigoEsquemaTarifario,
																				int codigoServicio	)
	{
		ResultSetDecorator resultado = null;
		try
		{
			PreparedStatementDecorator consultaTarifaSttmt =  new PreparedStatementDecorator(con.prepareStatement(consultaTarifaPorETyS,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consultaTarifaSttmt.setInt(1, codigoEsquemaTarifario);
			consultaTarifaSttmt.setInt(2, codigoServicio);
			
			resultado = new ResultSetDecorator(consultaTarifaSttmt.executeQuery());			
		}
		catch( SQLException sqe )
		{
			logger.warn("No se pudo realizar la consulta, problemas con la bd "+sqe.getMessage());
			return new ResultadoCollectionDB(false, "No se pudo realizar la consulta, problemas con la bd "+sqe.getMessage());
		}
		
		try
		{
			Collection col = UtilidadBD.resultSet2Collection(resultado);
			if( col != null && !col.isEmpty() )
			{
				return new ResultadoCollectionDB(true, "", col);
			}
			else
			{
				return new ResultadoCollectionDB(false);
			}
		}
		catch(Exception e)
		{
			logger.warn(" Error en Consultar Tarifa al convertir el result set a una colección\n"+e.getMessage());
			return new ResultadoCollectionDB(false, " Error en Consultar Tarifa al convertir el result set a una colección\n"+e.getMessage());
		}		
	}

	/**
	 * Dice si existe o no una tarifa dada por el esquema tarifario y el codigo del servicio asociado
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoEsquemaTarifario. int, código del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, código del servicio asociado a esta tarifa
	 * @return ResultadoBoolean, true si existe la tarifa, false y sin descripción si no existe o false y con descripción
	 * si se presento un problema en la consulta
	 */
	public static ResultadoBoolean existeTarifaDefinida( 	Connection con, 
																							int codigoEsquemaTarifario,
																							int codigoServicio	)
	{
		try
		{
			PreparedStatementDecorator existeTarifaSttmnt =  new PreparedStatementDecorator(con.prepareStatement(consultaExisteTarifaDefinida,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			existeTarifaSttmnt.setInt(1, codigoEsquemaTarifario);
			existeTarifaSttmnt.setInt(2, codigoServicio);
			
			ResultSetDecorator resultado = new ResultSetDecorator(existeTarifaSttmnt.executeQuery());
			
			if( resultado.next() )
			{
				int numFilas = resultado.getInt("numTarifasDefinidas");
				
				if( numFilas <= 0 )
					return new ResultadoBoolean(false);
				else
					return new ResultadoBoolean(true);
			}
			else
			{
				logger.warn("La consulta de si existe tarifa definida no retorno ningun resultado");
				return new ResultadoBoolean(false, "La consulta de si existe tarifa definida no retorno ningun resultado");
			}			
		}
		catch( SQLException sqe )
		{
			logger.warn("No se pudo realizar la consulta, problemas con la bd "+sqe.getMessage());
			return new ResultadoBoolean(false, "No se pudo realizar la consulta, problemas con la bd "+sqe.getMessage());
		}
	}
	
	
	/**
	 * Busqueda avanzada para tarifas ISS
	 * @param con
	 * @param codigoTarifa
	 * @param buscarPorCodigoTarifa
	 * @param codigoEsquemaTarifario
	 * @param buscarPorCodigoEsquemaTarifario
	 * @param codigoServicio
	 * @param buscarPorCodigoServicio
	 * @param valorTarifa
	 * @param buscarPorValorTarifa
	 * @param porcentajeIva
	 * @param buscarPorPorcentajeIva
	 * @param liqAsocios
	 * @param buscarPorLiqAsocios
	 * @return ResultadoCollectionDB
	 * @throws SQLException
	 */
		public static ResultadoCollectionDB consultar(Connection con,
		int codigoTarifa,boolean buscarPorCodigoTarifa,
		int codigoEsquemaTarifario,boolean buscarPorCodigoEsquemaTarifario,
		String codigoServicio,boolean buscarPorCodigoServicio,
		double valorTarifa,boolean buscarPorValorTarifa,
		double porcentajeIva,boolean buscarPorPorcentajeIva,
		String nombreServicio, boolean buscarPorNombreServicio,
		int codigoTipoLiquidacion, boolean buscarPorCodigoTipoLiquidacion,
		String liqAsocios,boolean buscarPorLiqAsocios, int institucion, String castDate) throws SQLException
	{
		boolean existeAsignacion = false;
		String buscarExcepcionesStr2 = new String(buscarTarifaISSStr);

		if(buscarPorCodigoTarifa){
			if(existeAsignacion){
				buscarExcepcionesStr2=buscarExcepcionesStr2.concat(" AND ");
			}
			buscarExcepcionesStr2=buscarExcepcionesStr2.concat("ti.codigo="+codigoTarifa+" ");
			existeAsignacion=true;
		}
		if(buscarPorCodigoEsquemaTarifario){
			if(existeAsignacion){
				buscarExcepcionesStr2=buscarExcepcionesStr2.concat(" AND ");
			}
			buscarExcepcionesStr2=buscarExcepcionesStr2.concat("ti.esquema_tarifario="+codigoEsquemaTarifario+" ");
			existeAsignacion=true;
		}		
		if(buscarPorCodigoServicio){
			if(existeAsignacion){
				buscarExcepcionesStr2=buscarExcepcionesStr2.concat(" AND ");
			}
			buscarExcepcionesStr2 = buscarExcepcionesStr2.concat("rs.codigo_propietario = '"+codigoServicio+"' ");
			existeAsignacion=true;
		}						
		if(buscarPorValorTarifa){
			if(existeAsignacion){
				buscarExcepcionesStr2=buscarExcepcionesStr2.concat(" AND ");
			}
			buscarExcepcionesStr2=buscarExcepcionesStr2.concat("ti.valor_tarifa="+valorTarifa+" ");
			existeAsignacion=true;
		}			
		if(buscarPorPorcentajeIva){
			if(existeAsignacion){
				buscarExcepcionesStr2=buscarExcepcionesStr2.concat(" AND ");
			}
			buscarExcepcionesStr2=buscarExcepcionesStr2.concat("ti.porcentaje_iva="+porcentajeIva+" ");
			existeAsignacion=true;
		}
		
		if(buscarPorNombreServicio){
			if(existeAsignacion){
				buscarExcepcionesStr2=buscarExcepcionesStr2.concat(" AND ");
			}
			buscarExcepcionesStr2=buscarExcepcionesStr2.concat(" UPPER(rs.descripcion) LIKE UPPER('%"+nombreServicio+"%') ");
			existeAsignacion=true;
		}
		
		if(buscarPorCodigoTipoLiquidacion){
			if(existeAsignacion){
				buscarExcepcionesStr2=buscarExcepcionesStr2.concat(" AND ");
			}
			buscarExcepcionesStr2=buscarExcepcionesStr2.concat("ti.tipo_liquidacion="+codigoTipoLiquidacion+" ");
			existeAsignacion=true;
		}
		
		if(buscarPorLiqAsocios){
			if(existeAsignacion){
				buscarExcepcionesStr2 = buscarExcepcionesStr2.concat(" AND ");
			}
			buscarExcepcionesStr2 = buscarExcepcionesStr2.concat("ti.liq_asocios='"+liqAsocios+"' ");
			existeAsignacion=true;
		}

		//aqui poner condiciones adicionales
		if(existeAsignacion){
			buscarExcepcionesStr2 = buscarExcepcionesStr2.concat("AND ");
		}
		/*buscarExcepcionesStr2 = buscarExcepcionesStr2.concat(" ti.servicio = s.codigo " +
													"AND et.codigo = ti.esquema_tarifario " +
													"AND rs.servicio = ti.servicio " +
													"AND s.especialidad = e.codigo " +
													"AND tl.codigo = ti.tipo_liquidacion " +
													"AND rs.tipo_tarifario = "+ConstantesBD.codigoTarifarioCups+" ORDER BY codigoServicio ASC");
		*/
		buscarExcepcionesStr2 = buscarExcepcionesStr2.concat(" rs.tipo_tarifario = "+ConstantesBD.codigoTarifarioCups+" " +
				" AND ((getobtenerfechavigenciaiss(ti.esquema_tarifario,ti.servicio) IS NOT NULL " +
						"AND ti.fecha_vigencia=(getobtenerfechavigenciaiss(ti.esquema_tarifario,ti.servicio)"+castDate+")) " +
							"OR (getobtenerfechavigenciaiss(ti.esquema_tarifario,ti.servicio) IS NULL AND ti.fecha_vigencia IS NULL)) " +
								"ORDER BY codigoServicio ASC");
		
		logger.info("\n\nCONSULTA TARIFAS ISS-->"+buscarExcepcionesStr2+"\n\n");
		
		PreparedStatementDecorator buscarRecargosStmnt =  new PreparedStatementDecorator(con.prepareStatement(buscarExcepcionesStr2,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		Collection listadoCollection = UtilidadBD.resultSet2Collection(new ResultSetDecorator(buscarRecargosStmnt.executeQuery()));
		return new ResultadoCollectionDB(true, "", listadoCollection);	
	}
		
	/**
	 * Metodo para consultar las fechas vigencia segun esquema tarifario y servicio
	 * @param con
	 * @param esquemaTarifario
	 * @param servicio
	 * @return
	 */
	public static HashMap consultarFechasVigencia(Connection con, String esquemaTarifario, String servicio, String cadenaCodigosServicios)
	{
		logger.info("\n\nCADENA CODIGOS SERVICIOS>>>>>>>>>>>>>"+cadenaCodigosServicios);
		HashMap<String, Object> resultados=new HashMap<String, Object>();
		String cadena="SELECT DISTINCT " +
							"ti.codigo AS codigo, " +
							"ti.esquema_tarifario AS codigoEsquemaTarifario, " +
							"et.nombre AS nombreEsquemaTarifario, " +
							"ti.valor_tarifa AS valorTarifa, " +
							"ti.porcentaje_iva AS porcentajeIva, " +
							"ti.servicio AS codigoServicio, " +
							"rs.descripcion AS nombreServicio, " +
							"e.codigo AS codigoEspecialidad, " +
							"e.nombre AS nombreEspecialidad, " +
							"ti.tipo_liquidacion AS codigoTipoLiquidacion, " +
							"tl.nombre AS nombreTipoLiquidacion," +
							"ti.unidades AS unidades," +
							"ti.liq_asocios AS liqAsocios, " +
							"ti.fecha_vigencia AS fechavigencia "+
						"FROM " +
							"tarifas_iss ti " +
							"INNER JOIN esquemas_tarifarios et ON (ti.esquema_tarifario = et.codigo) " +
							"INNER JOIN servicios s ON (ti.servicio = s.codigo) " +
							"INNER JOIN referencias_servicio rs ON (rs.servicio = s.codigo) " +
							"INNER JOIN especialidades e ON (s.especialidad = e.codigo) " +
							"INNER JOIN tipos_liquidacion_soat tl ON (tl.codigo = ti.tipo_liquidacion) " +
						"WHERE rs.tipo_tarifario="+ConstantesBD.codigoTarifarioCups+" ";
		cadena+="AND ti.esquema_tarifario="+esquemaTarifario;
		if(!servicio.equals(""))
			cadena+=" AND ti.servicio="+servicio+" ORDER BY ti.fecha_vigencia DESC ";
		else
			cadena+=" AND ti.servicio IN ("+cadenaCodigosServicios+") GROUP BY ti.esquema_tarifario," +
						"ti.servicio," +
						"ti.codigo," +
						"et.nombre," +
						"ti.valor_tarifa," +
						"ti.porcentaje_iva," +
						"rs.descripcion," +
						"e.codigo," +
						"e.nombre," +
						"ti.tipo_liquidacion," +
						"tl.nombre," +
						"ti.unidades," +
						"ti.liq_asocios," +
						"ti.fecha_vigencia ORDER BY ti.servicio, ti.fecha_vigencia DESC ";
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true, true);
		} 
		catch (SQLException e)
		{
			logger.info("\n\n ERROR. CONSULTANDO FECHAS VIGENCIA POR ESQUEMA SERVICIO>>>>>"+e);
		}
		logger.info("\n\nCONSULTA FECHAS VIGENCIA SQL >>>>>>>>>>"+cadena);
		return resultados;
	}
	
	/**
	 * Inserta una tarifa iss
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoEsquemaTarifario. int, código del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, código del servicio asociado a esta tarifa
	 * @param valorTarifa. double, valor de esta tarifa
	 * @param login usuario
	 * @param porcentajeIva. double, porcentaje del iva de esta tarifa
	 * @param codigo tipo liquidacion
	 * @param unidades
	 * @param liquidacion asocios
	 * @param tipo liquidacion original
	 * @param valor tarifa original
	 * @param liq_asocios Original
	 * @param insertarStr
	 * @return ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.TarifaISSDao#insertar(java.sql.Connection, int, int, int, double, double)
	 */
	public static ResultadoBoolean insertar(	Connection con,
															int codigoEsquemaTarifario,
															int codigoServicio,
															double valorTarifa,
															String loginUsuario,
															double porcentajeIva,
															int codigoTipoLiquidacion,
															double unidades,
															String liq_asocios,
															int tipoLiquidacionOriginal,
															double valorTarifaOriginal,
															String liq_asociosOriginal,
															String fecha_vigencia,
															String insertarStr)
	{
		
		
		int insertoLog=ConstantesBD.codigoNuncaValido;
		try
		{			
			PreparedStatementDecorator insertar =  new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			
			insertar.setInt(1, codigoEsquemaTarifario);
			if(valorTarifa < 0)
				insertar.setNull(2,Types.DOUBLE);
			else
				insertar.setDouble(2, valorTarifa);
			insertar.setDouble(3, porcentajeIva);
			insertar.setInt(4, codigoServicio);
			
			if( codigoTipoLiquidacion <= 0 )
				insertar.setString(5, null);
			else
				insertar.setInt(5, codigoTipoLiquidacion);
			
			if( unidades < 0)
				insertar.setNull(6, Types.DOUBLE);
			else
				insertar.setDouble(6,unidades);
			
			insertar.setObject(7,liq_asocios);
			
			insertar.setObject(8,loginUsuario);
			
			insertar.setObject(9,UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			
			insertar.setObject(10,UtilidadFecha.getHoraActual());
			
			if(!fecha_vigencia.equals(""))
				insertar.setDate(11, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fecha_vigencia)));
			else
				insertar.setNull(11, Types.DATE);
			
			int insert = insertar.executeUpdate();
			
			if( insert == 0 )
				return new ResultadoBoolean(false);
			
			if(insert>0)
			{
				insertoLog=logTarifasServicios(con, codigoEsquemaTarifario, codigoServicio, codigoTipoLiquidacion, valorTarifa, liq_asocios, tipoLiquidacionOriginal, valorTarifaOriginal, liq_asociosOriginal, loginUsuario, fecha_vigencia);
			}
			
			if(insert>0 && insertoLog>0)
			{
				return new ResultadoBoolean(true);
			}
					
			
		}
		catch(SQLException e)
		{
			logger.warn("No se insertó la tarifa iss "+e);
			return new ResultadoBoolean(false, "No se insertó la tarifa iss \n"+e);
		}
		return new ResultadoBoolean(true);
	}

	/**
	 * Inserta una tarifa iss dentro de una transacción dado su estado.
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoEsquemaTarifario. int, código del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, código del servicio asociado a esta tarifa
	 * @param valorTarifa. double, valor de esta tarifa
	 * @param login usuario
	 * @param porcentajeIva. double, porcentaje del iva de esta tarifa
	 * @param codigo tipo liquidacion
	 * @param unidades
	 * @param liqAsocios
	 * @param tipo liquidacion original
	 * @param valor tarifa original
	 * @param liqAsociosOriginal
	 * @param estado. String, estado dentro de la transacción
	 * @param insertarStr
	 * @return ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.TarifaISSDao#insertarTransaccional(java.sql.Connection, int, int, int, double, double, java.lang.String)
	 */
	public static ResultadoBoolean insertarTransaccional(	Connection con,
																					int codigoEsquemaTarifario,
																					int codigoServicio,
																					double valorTarifa,
																					String loginUsuario,
																					double porcentajeIva,
																					int codigoTipoLiquidacion,
																					double unidades,
																					String liq_asocios,
																					int tipoLiquidacionOriginal,
																					double valorTarifaOriginal,
																					String liq_asociosOriginal,
																					String estado,
																					String fecha_vigencia,
																					String insertarStr
																					) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		ResultadoBoolean resultado;
		if( estado != null && estado.equals(ConstantesBD.inicioTransaccion) )
		{
			resultado = new ResultadoBoolean(myFactory.beginTransaction(con));
			if( !resultado.isTrue() )
			{
				return resultado;			
			}
		}
		int insertoLog=ConstantesBD.codigoNuncaValido;
		try
		{			
			PreparedStatementDecorator insertar =  new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
			insertar.setInt(1, codigoEsquemaTarifario);
			if(valorTarifa < 0)
				insertar.setNull(2,Types.DOUBLE);
			else
				insertar.setDouble(2, valorTarifa);
			insertar.setDouble(3, porcentajeIva);
			insertar.setInt(4, codigoServicio);
			
			if( codigoTipoLiquidacion <= 0 )
				insertar.setString(5, null);
			else
				insertar.setInt(5, codigoTipoLiquidacion);
			
			if( unidades <= 0)
				insertar.setNull(6, Types.DOUBLE);
			else
				insertar.setDouble(6,unidades);
			
			insertar.setObject(7,liq_asocios);
			
			insertar.setObject(8,loginUsuario);
			
			insertar.setObject(9,UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			
			insertar.setObject(10,UtilidadFecha.getHoraActual());
			
			insertar.setDate(11, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fecha_vigencia)));
					
			int insert = insertar.executeUpdate();
					
			if( insert == 0 )
			{
			    myFactory.abortTransaction(con);
				return new ResultadoBoolean(false); 
			}
			if(insert>0)
			{
				insertoLog=logTarifasServicios(con, codigoEsquemaTarifario, codigoServicio, codigoTipoLiquidacion, valorTarifa,liq_asocios, tipoLiquidacionOriginal, valorTarifaOriginal, liq_asociosOriginal, loginUsuario, fecha_vigencia);
			}
			
			if(insert>0 && insertoLog>0)
			{
				return new ResultadoBoolean(true);
			}
		}
		catch(SQLException e)
		{
		    myFactory.abortTransaction(con);
			logger.warn("No se insertó la tarifa iss "+e);
			return new ResultadoBoolean(false, "No se insertó la tarifa iss \n"+e);
		}		
		
		if( estado !=null && estado.equals(ConstantesBD.finTransaccion) )
		{
		    myFactory.endTransaction(con);
		} 
		return new ResultadoBoolean(true);
	}
	
	/**
	 * Método para cargar una tarifa de ISS dado el código del servicio
	 * y el esquema tarifario
	 * @param con conexión con la BD
	 * @param servicio Código del servicio 
	 * @param esquemaTarifario Código del esquema tarifario
	 * @return Coleccion con los datos
	 */
	public static Collection cargar(Connection con, int servicio, int esquemaTarifario, String fechaVigenciaOPCIONAL)
	{
		Collection resultados = null;
		String cargarTarifaISSStr="SELECT " +
				"codigo AS codigo, " +
				"valor_tarifa AS valor, " +
				"porcentaje_iva AS iva, " +
				"tipo_liquidacion as tipo_liquidacion, " +
				"getnomtipoliquidacion(tipo_liquidacion) AS nom_tipo_liquidacion, " +
				"(getNombreServicioTarifa(servicio, 1)||' - '||getNomEspecialidadServicio(servicio)) as nombre_servicio, " +
				"getNomEspecialidadServicio(servicio) as nombre_especialidad, " +
				"getCodigoEspecialidad(servicio) as codigo_especialidad," +
				"unidades AS unidades," +
				"liq_asocios AS liqAsocios " +
				"from tarifas_iss WHERE servicio=? AND esquema_tarifario=? ";
		
		if(UtilidadTexto.isEmpty(fechaVigenciaOPCIONAL))
		{	
			try
			{
				PreparedStatementDecorator cargarStm =  new PreparedStatementDecorator(con.prepareStatement(cargarTarifaISSStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				cargarStm.setInt(1, servicio);
				cargarStm.setInt(2, esquemaTarifario);
				return UtilidadBD.resultSet2Collection(new ResultSetDecorator(cargarStm.executeQuery()));
			}
			catch(SQLException e)
			{
				logger.error("Error cargando la tarifa de ISS para el servicio "+servicio+": "+e);
				return null;
			}
		}
		else
		{
			
			String consultaArmada= cargarTarifaISSStr+" AND to_char(fecha_vigencia,'"+ConstantesBD.formatoFechaBD+"')<='"+UtilidadFecha.conversionFormatoFechaABD(fechaVigenciaOPCIONAL)+"'  order by fecha_vigencia desc" ;
			//") UNION ALL ("+cargarTarifaISSStr+" AND fecha_vigencia IS NULL) ";
			
			logger.info("******************************************************************************************************");
			logger.info("TARIFAS --->"+consultaArmada+" --->SERV->"+servicio+" esquta->"+esquemaTarifario);
			logger.info("******************************************************************************************************");
			
			try
			{
				PreparedStatementDecorator cargarStm =  new PreparedStatementDecorator(con.prepareStatement(consultaArmada,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				cargarStm.setInt(1, servicio);
				cargarStm.setInt(2, esquemaTarifario);
				resultados = UtilidadBD.resultSet2Collection(new ResultSetDecorator(cargarStm.executeQuery()));
				cargarStm.close();
				
				Iterator iterador = resultados.iterator();
				if(!iterador.hasNext())
				{
					consultaArmada=  cargarTarifaISSStr+" AND fecha_vigencia IS NULL";
					cargarStm =  new PreparedStatementDecorator(con.prepareStatement(consultaArmada,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					cargarStm.setInt(1, servicio);
					cargarStm.setInt(2, esquemaTarifario);
					resultados = UtilidadBD.resultSet2Collection(new ResultSetDecorator(cargarStm.executeQuery()));
					cargarStm.close();
				}
				
				return resultados;
			}
			catch(SQLException e)
			{
				logger.error("Error cargando la tarifa de ISS para el servicio "+servicio+": "+e);
				return null;
			}
		}
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param esquemaTarifario
	 * @param tipoLiquidacion
	 * @param valorTarifa
	 * @param loginUsuario
	 * @return
	 */
	public static int logTarifasServicios(Connection con, int esquemaTarifario, int servicio, int tipoLiquidacion, double valorTarifa,String liqAsocios, int tipoLiquidacionOriginal, double valorTarifaOriginal,String liqAsociosOriginal, String loginUsuario, String fecha_vigencia)
	{
		try{
			PreparedStatementDecorator insert1 =  new PreparedStatementDecorator(con.prepareStatement(cadenaLogStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			insert1.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con,"Seq_log_tarifas_servicios"));
			insert1.setInt(2, esquemaTarifario);
			insert1.setInt(3, servicio);
			insert1.setInt(4, tipoLiquidacion);
			insert1.setDouble(5, valorTarifa);
			insert1.setObject(6, liqAsocios);
			insert1.setInt(7, tipoLiquidacionOriginal);
			insert1.setDouble(8, valorTarifaOriginal);
			
			if(!liqAsociosOriginal.equals(""))
				insert1.setObject(9, liqAsociosOriginal);
			else
				insert1.setString(9,ConstantesBD.acronimoNo);							
			
			insert1.setString(10, loginUsuario);
			if(!fecha_vigencia.equals(""))
				insert1.setDate(11, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fecha_vigencia)));
			else
				insert1.setNull(11, Types.DATE);
			logger.info("\n\nINSERTANDO LA TARIFA-->>"+cadenaLogStr);
			return insert1.executeUpdate();
		
		}
		catch(SQLException e)
		{
			logger.error("Error insertando El Log de la tarifa de inventarios: "+e);
			return 0;
		}
	}
	
	/**
	 * Metodo que consulta todas las tarifas por esquema tarifario y servicio
	 * @param con
	 * @param esquemaTarifario
	 * @param codServicio
	 * @return
	 */
	public static HashMap consultarTodasTarifas(Connection con, int esquemaTarifario, String codServicio, String cadenaConsultaTarifas)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator pst;
		 
		try{
			logger.info("\n\n\n cadenaConsultaTarifas->"+cadenaConsultaTarifas+"   esq-> "+esquemaTarifario+" serv->"+codServicio+"  \n\n\n");
			pst =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaTarifas, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1, esquemaTarifario);
			pst.setString(2, codServicio);
			pst.setInt(3, esquemaTarifario);
			pst.setString(4, codServicio);
			
			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
		}
		catch (SQLException e)
		{
			logger.error("\n\n Error en consulta de Todas Tarifas >>>>>"+e);
		}
		return resultados;
	}
}