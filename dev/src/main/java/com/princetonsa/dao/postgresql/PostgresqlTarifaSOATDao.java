/*
 * @(#)PostgresqlTarifaSOATDao.java
 * 
 * Created on 03-May-2004
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados
 * 
 * Lenguaje : Java
 * Compilador : J2SDK 1.4
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import util.ResultadoBoolean;
import util.ResultadoCollectionDB;
import util.ValoresPorDefecto;

import com.princetonsa.dao.TarifaSOATDao;
import com.princetonsa.dao.sqlbase.SqlBaseTarifaSOATDao;

/**
 * Implementación postgreSQL de las funciones de acceso a la fuente de datos
 * para una tarifa
 *
 * @version 1.0, Mayo 03 / 2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class PostgresqlTarifaSOATDao implements TarifaSOATDao
{
	/**
	 * Statement para la inserción de una tarifa soat
	 */
	private static String insertarStr = 	"INSERT INTO tarifas_soat " +
														"(codigo, " +
														"esquema_tarifario, " +
														"unidades, " +
														"valor_tarifa, " +
														"porcentaje_iva, " +
														"tipo_liquidacion, " +
														"servicio," +
														"liq_asocios," +
														"usuario_modifica," +
														"fecha_modifica," +
														"hora_modifica," +
														"fecha_vigencia) " +
														"VALUES(nextval('seq_tarifas_soat'), ?, ?, ?, ?, ?, ? , ?, ?, ?, ?, ?) ";
	
	/**
	 * Cadena que consulta todas las tarifas por esquema tarifario y servicio
	 */
	private static String cadenaConsultaTarifas="(" +
													"SELECT " +
														"v.codigo AS codigo, " +
														"v.codigo_esquema_tarifario AS codigoEsquemaTarifario, " +
														"v.nombre_esquema_tarifario AS nombreEsquemaTarifario, " +
														"v.valor_tarifa AS valorTarifa, " +
														"v.porcentaje_iva AS porcentajeIva, " +
														"v.codigo_servicio AS codigoServicio, " +
														"v.nombre_servicio AS nombreServicio, " +
														"v.codigo_especialidad AS codigoEspecialidad, " +
														"v.nombre_especialidad AS nombreEspecialidad, " +
														"v.codigo_tipo_liquidacion AS codigoTipoLiquidacion, " +
														"v.nombre_tipo_liquidacion AS nombreTipoLiquidacion," +
														"v.unidades AS unidades," +
														"v.liq_asocios AS liqAsocios, " +
														"v.fecha_vigencia AS fechavigencia "+
													"FROM " +
														"facturacion.view_tarifas_soat v " +
													"WHERE " +
														"v.codigo_esquema_tarifario=? " +
														"AND v.codigo_servicio=? " +
														"AND v.fecha_vigencia<=CURRENT_DATE " +
													""+ValoresPorDefecto.getValorLimit1()+" 1" +
												") " +
												"UNION all " +
												"(" +
													"SELECT " +
														"v.codigo AS codigo, " +
														"v.codigo_esquema_tarifario AS codigoEsquemaTarifario, " +
														"v.nombre_esquema_tarifario AS nombreEsquemaTarifario, " +
														"v.valor_tarifa AS valorTarifa, " +
														"v.porcentaje_iva AS porcentajeIva, " +
														"v.codigo_servicio AS codigoServicio, " +
														"v.nombre_servicio AS nombreServicio, " +
														"v.codigo_especialidad AS codigoEspecialidad, " +
														"v.nombre_especialidad AS nombreEspecialidad, " +
														"v.codigo_tipo_liquidacion AS codigoTipoLiquidacion, " +
														"v.nombre_tipo_liquidacion AS nombreTipoLiquidacion," +
														"v.unidades AS unidades," +
														"v.liq_asocios AS liqAsocios, " +
														"v.fecha_vigencia AS fechavigencia "+
													"FROM " +
														"facturacion.view_tarifas_soat v " +
													"WHERE " +
														"v.codigo_esquema_tarifario=? " +
														"AND v.codigo_servicio=? " +
														"and v.codigo not in(" +
																				"SELECT " +
																					"v1.codigo " +
																				"from " +
																					"facturacion.view_tarifas_soat v1 " +
																				"WHERE " +
																					"v1.codigo_esquema_tarifario=v.codigo_esquema_tarifario " +
																					"AND v1.codigo_servicio=v.codigo_servicio " +
																					"AND v1.fecha_vigencia<=CURRENT_DATE " +
																					""+ValoresPorDefecto.getValorLimit1()+" 1 " +
																			") " +
												") " +
												"";
	

	/**
	 * Inserta una tarifa soat
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoEsquemaTarifario. int, código del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, código del servicio asociado a esta tarifa
	 * @param grupo. int, grupo de esta tarifa
	 * @param codigoTipoLiquidacion, int, código del tipo de liquidación aplicable a esta tarifa
	 * @param valorTarifa. double, valor de esta tarifa
	 * @param porcentajeIva. double, porcentaje del iva de esta tarifa
	 * @param liquidar Asocio
	 * @return ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.TarifaSOATDao#insertar(java.sql.Connection, int, int, int, int, double, double) 
	 */
	public  ResultadoBoolean insertar(	Connection con,
															int codigoEsquemaTarifario,
															int codigoServicio,
															double grupo,
															int codigoTipoLiquidacion,
															double valorTarifa,
															String loginUsuario,
															double porcentajeIva,
															int tipoLiquidacionoriginal,
															double valorTarifaOriginal,
															String liquidarAsocio,
															String liquidarAsocioOriginal,
															String fechaVigencia
															) throws SQLException
	{
	    return SqlBaseTarifaSOATDao.insertar(	con,
				codigoEsquemaTarifario,
				codigoServicio,
				grupo,
				codigoTipoLiquidacion,
				valorTarifa,
				loginUsuario,
				porcentajeIva,
				tipoLiquidacionoriginal,
				valorTarifaOriginal,
				liquidarAsocio,
				liquidarAsocioOriginal,
				fechaVigencia,
				insertarStr) ;
	}

	/**
	 * Inserta una tarifa soat dentro de una transacción dado el estado
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoEsquemaTarifario. int, código del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, código del servicio asociado a esta tarifa
	 * @param grupo. int, grupo de esta tarifa
	 * @param codigoTipoLiquidacion, int, código del tipo de liquidación aplicable a esta tarifa
	 * @param valorTarifa. double, valor de esta tarifa
	 * @param porcentajeIva. double, porcentaje del iva de esta tarifa
	 * @param estado. String, estado dentro de la transacción
	 * @return ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.TarifaSOATDao#insertarTransaccional(java.sql.Connection, int, int, int, int, double, double, java.lang.String)
	 */
	public  ResultadoBoolean insertarTransaccional(	Connection con,
																					int codigoEsquemaTarifario,
																					int codigoServicio,
																					double grupo,
																					int codigoTipoLiquidacion,
																					double valorTarifa,
																					String loginUsuario,
																					double porcentajeIva,
																					int tipoLiquidacionOriginal,
																					double valorTarifaOriginal,
																					String estado,
																					String liquidarAsocio,
																					String liquidarAsocioOriginal,
																					String fechaVigencia) throws SQLException
	{
	    return SqlBaseTarifaSOATDao.insertarTransaccional(	con,
				codigoEsquemaTarifario,
				codigoServicio,
				grupo,
				codigoTipoLiquidacion,
				valorTarifa,
				loginUsuario,
				porcentajeIva,
				tipoLiquidacionOriginal,
				valorTarifaOriginal,
				estado,
				liquidarAsocio,
				liquidarAsocioOriginal,
				fechaVigencia,
				insertarStr) ;
	}

	
	/**
	 * Modifica una tarifa soat dado su código
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoTarifa, int, código de la tarifa a modificar
	 * @param codigoEsquemaTarifario. int, código del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, código del servicio asociado a esta tarifa
	 * @param grupo. int, grupo de esta tarifa
	 * @param codigoTipoLiquidacion, int, código del tipo de liquidación aplicable a esta tarifa
	 * @param valorTarifa. double, valor de esta tarifa
	 * @param porcentajeIva. double, porcentaje del iva de esta tarifa
	 * @param liq_asocios 
	 * @return ResultadoBoolean, true si la modificación fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.TarifaSOATDao#modificar(java.sql.Connection, int, int, int, int, int, double, double)
	 */
	public  ResultadoBoolean modificar(	Connection con,
																int codigoTarifa,
																int codigoEsquemaTarifario,
																int codigoServicio,
																double grupo,
																int codigoTipoLiquidacion,
																double valorTarifa,
																String loginUsuario,
																double porcentajeIva,
																int tipoLiquidacionOriginal,																
																double valorTarifaOriginal,
																String liquidarAsocios,
																String liquidarAsociosOriginal,
																String fechaVigencia)
	{
		return SqlBaseTarifaSOATDao.modificar(	con,
																			codigoTarifa,
																			codigoEsquemaTarifario,
																			codigoServicio,
																			grupo,
																			codigoTipoLiquidacion,
																			valorTarifa,
																			loginUsuario,
																			porcentajeIva,
																			tipoLiquidacionOriginal,
																			valorTarifaOriginal,
																			liquidarAsocios,
																			liquidarAsociosOriginal,
																			fechaVigencia);
	}

	/**
	 * Modifica una tarifa soat dado su código dentro de una transacción dado el estado
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoTarifa, int, código de la tarifa a modificar
	 * @param codigoEsquemaTarifario. int, código del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, código del servicio asociado a esta tarifa
	 * @param grupo. int, grupo de esta tarifa
	 * @param codigoTipoLiquidacion, int, código del tipo de liquidación aplicable a esta tarifa
	 * @param valorTarifa. double, valor de esta tarifa
	 * @param porcentajeIva. double, porcentaje del iva de esta tarifa
	 * @param estado. String, estado dentro de la transacción
	 * @param Liquidar Asocio
	 * @param Liquidar Asocios Original
	 * @return ResultadoBoolean, true si la modificación fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.TarifaSOATDao#modificarTransaccional(java.sql.Connection, int, int, int, int, int, double, double, java.lang.String)
	 */
	public  ResultadoBoolean modificarTransaccional(	Connection con,
																						int codigoTarifa,
																						int codigoEsquemaTarifario,
																						int codigoServicio,
																						double grupo,
																						int codigoTipoLiquidacion,
																						double valorTarifa,
																						String loginUsuario,
																						double porcentajeIva,
																						int tipoLiquidacionOriginal,
																						double valorTarifaOriginal,																						
																						String estado,
																						String liquidarAsocios,
																						String liquidarAsociosOriginal,
																						String fechaVigencia) throws SQLException
	{
		return SqlBaseTarifaSOATDao.modificarTransaccional(	con,
																								codigoTarifa,
																								codigoEsquemaTarifario,
																								codigoServicio,
																								grupo,
																								codigoTipoLiquidacion,
																								valorTarifa,
																								loginUsuario,
																								porcentajeIva,
																								tipoLiquidacionOriginal,
																								valorTarifaOriginal,
																								estado,
																								liquidarAsocios,
																								liquidarAsociosOriginal,
																								fechaVigencia);
	}

	/**
	 * Elimina una tarifa dado el código
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoTarifa. int, código de la tarifa a eliminar
	 * @return ResultadoBoolean, true si la modificación fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.TarifaSOATDao#eliminar(java.sql.Connection, int)
	 */
	public ResultadoBoolean eliminar(Connection con, int codigoTarifa)
	{
		return SqlBaseTarifaSOATDao.eliminar(con, codigoTarifa);
	}

	/**
	 * Consulta una tarifa SOAT dado el código
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoTarifa. int, código de la tarifa a consultar
	 * @return ResultadoCollectionDB, true y con la colección de HashMap con el resultado si fue exitosa
	 * la consulta, false y con la descripción de lo contrario
	 * @see com.princetonsa.dao.TarifaSOATDao#consultar(java.sql.Connection, int)
	 */
	public ResultadoCollectionDB consultar(Connection con, int codigoTarifa)
	{
		return SqlBaseTarifaSOATDao.consultar(con, codigoTarifa);
	}	

	/**
	 * Consulta una tarifa SOAT dado el código del esquema tarifario y el codigo del servicio
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoEsquemaTarifario. int, código del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, código del servicio asociado a esta tarifa
	 * @return ResultadoCollectionDB, true y con la colección de HashMap con el resultado si fue exitosa
	 * la consulta, false y con la descripción de lo contrario
	 * @see com.princetonsa.dao.TarifaSOATDao#consultar(java.sql.Connection, int, int)
	 */
	public ResultadoCollectionDB consultar(Connection con, int codigoEsquemaTarifario, int codigoServicio)
	{
		return SqlBaseTarifaSOATDao.consultar(con, codigoEsquemaTarifario, codigoServicio);
	}

	/**
	 * Dice si existe o no una tarifa dada por el esquema tarifario y el codigo del servicio asociado
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoEsquemaTarifario. int, código del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, código del servicio asociado a esta tarifa
	 * @return ResultadoBoolean, true si existe la tarifa, false y sin descripción si no existe o false y con descripción
	 * si se presento un problema en la consulta
	 * @see com.princetonsa.dao.TarifaSOATDao#existeTarifaDefinida(java.sql.Connection, int, int)
	 */
	public ResultadoBoolean existeTarifaDefinida(Connection con, int codigoEsquemaTarifario, int codigoServicio)
	{
		return SqlBaseTarifaSOATDao.existeTarifaDefinida(con, codigoEsquemaTarifario, codigoServicio);
	}	
	
	/**
	 * Método para cargar una tarifa de SOAT dado el código del servicio
	 * y el esquema tarifario
	 * @param con conexión con la BD
	 * @param servicio Código del servicio 
	 * @param esquemaTarifario Código del esquema tarifario
	 * @return true si se cargó correctamente
	 */
	public Collection cargar(Connection con, int servicio, int esquemaTarifario, String fechaVigenciaOPCIONAL)
	{
		return SqlBaseTarifaSOATDao.cargar(con, servicio, esquemaTarifario, fechaVigenciaOPCIONAL);
	}
	
	/**
	 * Metodo que consulta todas las Tarifas Soat por esquema y por servicio
	 * @param con
	 * @param esquemaTarifario
	 * @param codServicio
	 * @return
	 */
	public HashMap consultarTodasTarifas(Connection con, int esquemaTarifario, String codServicio)
	{
		return SqlBaseTarifaSOATDao.consultarTodasTarifas(con, esquemaTarifario, codServicio, cadenaConsultaTarifas);
	}
}