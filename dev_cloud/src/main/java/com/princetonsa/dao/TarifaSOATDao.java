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
	 * @param con, Connection, conexi�n abierta con la fuente de datos
	 * @param codigoEsquemaTarifario. int, c�digo del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, c�digo del servicio asociado a esta tarifa
	 * @param grupo. int, grupo de esta tarifa
	 * @param codigoTipoLiquidacion, int, c�digo del tipo de liquidaci�n aplicable a esta tarifa
	 * @param valorTarifa. double, valor de esta tarifa
	 * @param porcentajeIva. double, porcentaje del iva de esta tarifa
	 * @param liquidar Asocio
	 * @return ResultadoBoolean, true si la inserci�n fue exitosa, false y con la descripci�n 
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
	 * Inserta una tarifa soat dentro de una transacci�n dado el estado
	 * @param con, Connection, conexi�n abierta con la fuente de datos
	 * @param codigoEsquemaTarifario. int, c�digo del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, c�digo del servicio asociado a esta tarifa
	 * @param grupo. int, grupo de esta tarifa
	 * @param codigoTipoLiquidacion, int, c�digo del tipo de liquidaci�n aplicable a esta tarifa
	 * @param valorTarifa. double, valor de esta tarifa
	 * @param porcentajeIva. double, porcentaje del iva de esta tarifa
	 * @param estado. String, estado dentro de la transacci�n
	 * @return ResultadoBoolean, true si la inserci�n fue exitosa, false y con la descripci�n 
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
	 * Modifica una tarifa soat dado su c�digo
	 * @param con, Connection, conexi�n abierta con la fuente de datos
	 * @param codigoTarifa, int, c�digo de la tarifa a modificar
	 * @param codigoEsquemaTarifario. int, c�digo del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, c�digo del servicio asociado a esta tarifa
	 * @param grupo. int, grupo de esta tarifa
	 * @param codigoTipoLiquidacion, int, c�digo del tipo de liquidaci�n aplicable a esta tarifa
	 * @param valorTarifa. double, valor de esta tarifa
	 * @param porcentajeIva. double, porcentaje del iva de esta tarifa
	 * @param liq_asocios 
	 * @return ResultadoBoolean, true si la modificaci�n fue exitosa, false y con la descripci�n 
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
	 * Modifica una tarifa soat dado su c�digo dentro de una transacci�n dado el estado
	 * @param con, Connection, conexi�n abierta con la fuente de datos
	 * @param codigoTarifa, int, c�digo de la tarifa a modificar
	 * @param codigoEsquemaTarifario. int, c�digo del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, c�digo del servicio asociado a esta tarifa
	 * @param grupo. int, grupo de esta tarifa
	 * @param codigoTipoLiquidacion, int, c�digo del tipo de liquidaci�n aplicable a esta tarifa
	 * @param valorTarifa. double, valor de esta tarifa
	 * @param porcentajeIva. double, porcentaje del iva de esta tarifa
	 * @param estado. String, estado dentro de la transacci�n
	 * @param Liquidar Asocio
	 * @param Liquidar Asocios Original
	 * @return ResultadoBoolean, true si la modificaci�n fue exitosa, false y con la descripci�n 
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
	 * Elimina una tarifa dado el c�digo
	 * @param con, Connection, conexi�n abierta con la fuente de datos
	 * @param codigoTarifa. int, c�digo de la tarifa a eliminar
	 * @return ResultadoBoolean, true si la modificaci�n fue exitosa, false y con la descripci�n 
	 * de lo contrario
	 * @see com.princetonsa.dao.TarifaSOATDao#eliminar(java.sql.Connection, int)
	 */
	public ResultadoBoolean eliminar(Connection con, int codigoTarifa);

	/**
	 * Consulta una tarifa SOAT dado el c�digo
	 * @param con, Connection, conexi�n abierta con la fuente de datos
	 * @param codigoTarifa. int, c�digo de la tarifa a consultar
	 * @return ResultadoCollectionDB, true y con la colecci�n de HashMap con el resultado si fue exitosa
	 * la consulta, false y con la descripci�n de lo contrario
	 * @see com.princetonsa.dao.TarifaSOATDao#consultar(java.sql.Connection, int)
	 */
	public ResultadoCollectionDB consultar(Connection con, int codigoTarifa);

	/**
	 * Consulta una tarifa SOAT dado el c�digo del esquema tarifario y el codigo del servicio
	 * @param con, Connection, conexi�n abierta con la fuente de datos
	 * @param codigoEsquemaTarifario. int, c�digo del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, c�digo del servicio asociado a esta tarifa
	 * @return ResultadoCollectionDB, true y con la colecci�n de HashMap con el resultado si fue exitosa
	 * la consulta, false y con la descripci�n de lo contrario
	 * @see com.princetonsa.dao.TarifaSOATDao#consultar(java.sql.Connection, int, int)
	 */
	public ResultadoCollectionDB consultar(Connection con, int codigoEsquemaTarifario, int codigoServicio);

	/**
	 * Dice si existe o no una tarifa dada por el esquema tarifario y el codigo del servicio asociado
	 * @param con, Connection, conexi�n abierta con la fuente de datos
	 * @param codigoEsquemaTarifario. int, c�digo del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, c�digo del servicio asociado a esta tarifa
	 * @return ResultadoBoolean, true si existe la tarifa, false y sin descripci�n si no existe o false y con descripci�n
	 * si se presento un problema en la consulta
	 * @see com.princetonsa.dao.TarifaSOATDao#existeTarifaDefinida(java.sql.Connection, int, int)
	 */
	public ResultadoBoolean existeTarifaDefinida(Connection con, int codigoEsquemaTarifario, int codigoServicio);
	
	/**
	 * M�todo para cargar una tarifa de SOAT dado el c�digo del servicio
	 * y el esquema tarifario
	 * @param con conexi�n con la BD
	 * @param servicio C�digo del servicio 
	 * @param esquemaTarifario C�digo del esquema tarifario
	 * @return true si se carg� correctamente
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
