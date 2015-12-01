/*
 * @(#)TarifaSOATDao.java
 * 
 * Created on 03-May-2004
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados
 * 
 * Lenguaje : Java
 * Compilador : J2SDK 1.4
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import util.ResultadoBoolean;
import util.ResultadoCollectionDB;

/**
 *  Interfaz para el acceder a la fuente de datos de una tarifa SOAT 
 * 
 * @version 1.0, Mayo 03 / 2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public interface TarifaSOATDao
{
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
															String fechaVigencia) throws SQLException;

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
																					String fechaVigencia) throws SQLException;
	
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
	public ResultadoBoolean modificar(	Connection con,
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
																String fechaVigencia);

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
	public ResultadoBoolean modificarTransaccional(	Connection con,
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
																						String fechaVigencia) throws SQLException;

	/**
	 * Elimina una tarifa dado el código
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoTarifa. int, código de la tarifa a eliminar
	 * @return ResultadoBoolean, true si la modificación fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.TarifaSOATDao#eliminar(java.sql.Connection, int)
	 */
	public ResultadoBoolean eliminar(Connection con, int codigoTarifa);

	/**
	 * Consulta una tarifa SOAT dado el código
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoTarifa. int, código de la tarifa a consultar
	 * @return ResultadoCollectionDB, true y con la colección de HashMap con el resultado si fue exitosa
	 * la consulta, false y con la descripción de lo contrario
	 * @see com.princetonsa.dao.TarifaSOATDao#consultar(java.sql.Connection, int)
	 */
	public ResultadoCollectionDB consultar(Connection con, int codigoTarifa);

	/**
	 * Consulta una tarifa SOAT dado el código del esquema tarifario y el codigo del servicio
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoEsquemaTarifario. int, código del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, código del servicio asociado a esta tarifa
	 * @return ResultadoCollectionDB, true y con la colección de HashMap con el resultado si fue exitosa
	 * la consulta, false y con la descripción de lo contrario
	 * @see com.princetonsa.dao.TarifaSOATDao#consultar(java.sql.Connection, int, int)
	 */
	public ResultadoCollectionDB consultar(Connection con, int codigoEsquemaTarifario, int codigoServicio);

	/**
	 * Dice si existe o no una tarifa dada por el esquema tarifario y el codigo del servicio asociado
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoEsquemaTarifario. int, código del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, código del servicio asociado a esta tarifa
	 * @return ResultadoBoolean, true si existe la tarifa, false y sin descripción si no existe o false y con descripción
	 * si se presento un problema en la consulta
	 * @see com.princetonsa.dao.TarifaSOATDao#existeTarifaDefinida(java.sql.Connection, int, int)
	 */
	public ResultadoBoolean existeTarifaDefinida(Connection con, int codigoEsquemaTarifario, int codigoServicio);
	
	/**
	 * Método para cargar una tarifa de SOAT dado el código del servicio
	 * y el esquema tarifario
	 * @param con conexión con la BD
	 * @param servicio Código del servicio 
	 * @param esquemaTarifario Código del esquema tarifario
	 * @return true si se cargó correctamente
	 */
	public Collection cargar(Connection con, int servicio, int esquemaTarifario, String fechaVigenciaOPCIONAL);
	
	/**
	 * Metodo que consulta todas las tarifas soat por esquema servicio
	 * @param con
	 * @param esquemaTarifario
	 * @param codServicio
	 * @return
	 */
	public HashMap consultarTodasTarifas(Connection con, int esquemaTarifario, String codServicio);

}
