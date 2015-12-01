/*
 * @(#)TarifaISSDao.java
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
 *  Interfaz para el acceder a la fuente de datos de una tarifa ISS 
 * 
 * @version 1.0, Mayo 03 / 2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public interface TarifaISSDao
{
	
	
	
	/**
	 * Inserta una tarifa iss
	 * @param con, Connection, conexi�n abierta con la fuente de datos
	 * @param codigoEsquemaTarifario. int, c�digo del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, c�digo del servicio asociado a esta tarifa
	 * @param valorTarifa. double, valor de esta tarifa
	 * @param login usuario
	 * @param porcentajeIva. double, porcentaje del iva de esta tarifa
	 * @param codigo tipo liquidacion
	 * @param unidades
	 * @param liquidacion asocios
	 * @param tipo liquidacion original
	 * @param valor tarifa original
	 * @param liq_asocios Original
	 * @return ResultadoBoolean, true si la inserci�n fue exitosa, false y con la descripci�n 
	 * de lo contrario
	 * @see com.princetonsa.dao.TarifaISSDao#insertar(java.sql.Connection, int, int, int, double, double)
	 */
	public ResultadoBoolean insertar(	Connection con,
															int codigoEsquemaTarifario,
															int codigoServicio,
															double valorTarifa,
															String loginUsuario,
															double porcentajeIva,
															int codigoTipoLiquidacion,
															double unidades,
															String liqAsocios,
															int tipoLiquidacionOriginal,
															double valorTArifaOriginal,
															String liqAsociosOriginal,
															String fecha_vigencia);

	/**
	 * Inserta una tarifa iss dentro de una transacci�n dado su estado.
	 * @param con, Connection, conexi�n abierta con la fuente de datos
	 * @param codigoEsquemaTarifario. int, c�digo del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, c�digo del servicio asociado a esta tarifa
	 * @param valorTarifa. double, valor de esta tarifa
	 * @param login usuario
	 * @param porcentajeIva. double, porcentaje del iva de esta tarifa
	 * @param codigo tipo liquidacion
	 * @param unidades
	 * @param liqAsocios
	 * @param tipo liquidacion original
	 * @param valor tarifa original
	 * @param liqAsociosOriginal
	 * @param estado. String, estado dentro de la transacci�n
	 * @param insertarStr
	 * @return ResultadoBoolean, true si la inserci�n fue exitosa, false y con la descripci�n 
	 * de lo contrario
	 * @see com.princetonsa.dao.TarifaISSDao#insertarTransaccional(java.sql.Connection, int, int, int, double, double, java.lang.String)
	 */
	public ResultadoBoolean insertarTransaccional(	Connection con,
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
																					String fecha_vigencia) throws SQLException;

	
	/**
	 * Modifica una tarifa iss dado su c�digo
	 * @param con, Connection, conexi�n abierta con la fuente de datos
	 * @param codigoTarifa, int, c�digo de la tarifa a modificar
	 * @param codigoEsquemaTarifario. int, c�digo del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, c�digo del servicio asociado a esta tarifa
	 * @param valorTarifa. double, valor de esta tarifa
	 * @param login Usuario
	 * @param porcentajeIva. double, porcentaje del iva de esta tarifa
	 * @param codigo Tipo Liquidacion
	 * @param unidades
	 * @param tipo liquidacion original
	 * @param valor tarifa original
	 * @param Liquidar Asocios 
	 * @param Liquidar Asocios Original
	 * @return ResultadoBoolean, true si la modificaci�n fue exitosa, false y con la descripci�n 
	 * de lo contrario
	 * @see com.princetonsa.dao.TarifaISSDao#modificar(java.sql.Connection, int, int, int, int, double, double)
	 */
	public ResultadoBoolean modificar(	Connection con,
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
																		String fecha_vigencia);
	

	/**
	 * Modifica una tarifa iss dado su c�digo dentro de una transacci�n dado su estado.
	 * @param con, Connection, conexi�n abierta con la fuente de datos
	 * @param codigoTarifa, int, c�digo de la tarifa a modificar
	 * @param codigoEsquemaTarifario. int, c�digo del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, c�digo del servicio asociado a esta tarifa
	 * @param valorTarifa. double, valor de esta tarifa
	 * @param login usuario
	 * @param porcentajeIva. double, porcentaje del iva de esta tarifa
	 * @param codigo tipo liquidacion
	 * @param unidades
	 * @param Liquidacion Asocios
	 * @param tipo liquidacion original
	 * @param valor tarifa original
	 * @param liquidacion asocios original
	 * @param estado. String, estado dentro de la transacci�n
	 * @return ResultadoBoolean, true si la modificaci�n fue exitosa, false y con la descripci�n 
	 * de lo contrario
	 * @see com.princetonsa.dao.TarifaISSDao#modificarTransaccional(java.sql.Connection, int, int, int, int, double, double, java.lang.String)
	 */
	public ResultadoBoolean modificarTransaccional(	Connection con,
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
																								String fecha_vigencia) throws SQLException;

	/**
	 * Elimina una tarifa dado el c�digo
	 * @param con, Connection, conexi�n abierta con la fuente de datos
	 * @param codigoTarifa. int, c�digo de la tarifa a eliminar
	 * @return ResultadoBoolean, true si la modificaci�n fue exitosa, false y con la descripci�n 
	 * de lo contrario
	 * @see com.princetonsa.dao.TarifaISSDao#eliminar(java.sql.Connection, int)
	 */
	public ResultadoBoolean eliminar(Connection con, int codigoTarifa);

	/**
	 * Consulta una tarifa iss dado el c�digo
	 * @param con, Connection, conexi�n abierta con la fuente de datos
	 * @param codigoTarifa. int, c�digo de la tarifa a consultar
	 * @return ResultadoCollectionDB, true y con la colecci�n de HashMap con el resultado si fue exitosa
	 * la consulta, false y con la descripci�n de lo contrario
	 * @see com.princetonsa.dao.TarifaISSDao#consultar(java.sql.Connection, int)
	 */
	public ResultadoCollectionDB consultar(Connection con, int codigoTarifa);
	
	/**
	 * Consulta una tarifa iss dado el c�digo del esquema tarifario y el codigo del servicio
	 * @param con, Connection, conexi�n abierta con la fuente de datos
	 * @param codigoEsquemaTarifario. int, c�digo del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, c�digo del servicio asociado a esta tarifa
	 * @return ResultadoCollectionDB, true y con la colecci�n de HashMap con el resultado si fue exitosa
	 * la consulta, false y con la descripci�n de lo contrario
	 * @see com.princetonsa.dao.TarifaISSDao#consultar(java.sql.Connection, int, int)
	 */
	public ResultadoCollectionDB consultar(Connection con, int codigoEsquemaTarifario, int codigoServicio);	

	/**
	 * Dice si existe o no una tarifa dada por el esquema tarifario y el codigo del servicio asociado
	 * @param con, Connection, conexi�n abierta con la fuente de datos
	 * @param codigoEsquemaTarifario. int, c�digo del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, c�digo del servicio asociado a esta tarifa
	 * @return ResultadoBoolean, true si existe la tarifa, false y sin descripci�n si no existe o false y con descripci�n
	 * si se presento un problema en la consulta
	 * @see com.princetonsa.dao.TarifaISSDao#existeTarifaDefinida(java.sql.Connection, int, int)
	 */
	public ResultadoBoolean existeTarifaDefinida(Connection con, int codigoEsquemaTarifario, int codigoServicio);
	
	/**
	 * M�todo para cargar una tarifa de ISS dado el c�digo del servicio
	 * y el esquema tarifario
	 * @param con conexi�n con la BD
	 * @param servicio C�digo del servicio 
	 * @param esquemaTarifario C�digo del esquema tarifario
	 * @return true si se carg� correctamente
	 */
	public Collection cargar(Connection con, int servicio, int esquemaTarifario, String fechaVigenciaOPCIONAL);
	
	/**
	 * Metodo que consulta todas las tarifas por esquema tarifario y servicio
	 * @param con
	 * @param esquemaTarifario
	 * @param codServicio
	 * @return
	 */
	public HashMap consultaTodasTarifas(Connection con, int esquemaTarifario, String codServicio, String tipoCodigoTarifario);
}