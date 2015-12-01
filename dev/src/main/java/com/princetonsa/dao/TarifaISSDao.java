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
	 * @return ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
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
	 * Elimina una tarifa dado el código
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoTarifa. int, código de la tarifa a eliminar
	 * @return ResultadoBoolean, true si la modificación fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.TarifaISSDao#eliminar(java.sql.Connection, int)
	 */
	public ResultadoBoolean eliminar(Connection con, int codigoTarifa);

	/**
	 * Consulta una tarifa iss dado el código
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoTarifa. int, código de la tarifa a consultar
	 * @return ResultadoCollectionDB, true y con la colección de HashMap con el resultado si fue exitosa
	 * la consulta, false y con la descripción de lo contrario
	 * @see com.princetonsa.dao.TarifaISSDao#consultar(java.sql.Connection, int)
	 */
	public ResultadoCollectionDB consultar(Connection con, int codigoTarifa);
	
	/**
	 * Consulta una tarifa iss dado el código del esquema tarifario y el codigo del servicio
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoEsquemaTarifario. int, código del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, código del servicio asociado a esta tarifa
	 * @return ResultadoCollectionDB, true y con la colección de HashMap con el resultado si fue exitosa
	 * la consulta, false y con la descripción de lo contrario
	 * @see com.princetonsa.dao.TarifaISSDao#consultar(java.sql.Connection, int, int)
	 */
	public ResultadoCollectionDB consultar(Connection con, int codigoEsquemaTarifario, int codigoServicio);	

	/**
	 * Dice si existe o no una tarifa dada por el esquema tarifario y el codigo del servicio asociado
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoEsquemaTarifario. int, código del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, código del servicio asociado a esta tarifa
	 * @return ResultadoBoolean, true si existe la tarifa, false y sin descripción si no existe o false y con descripción
	 * si se presento un problema en la consulta
	 * @see com.princetonsa.dao.TarifaISSDao#existeTarifaDefinida(java.sql.Connection, int, int)
	 */
	public ResultadoBoolean existeTarifaDefinida(Connection con, int codigoEsquemaTarifario, int codigoServicio);
	
	/**
	 * Método para cargar una tarifa de ISS dado el código del servicio
	 * y el esquema tarifario
	 * @param con conexión con la BD
	 * @param servicio Código del servicio 
	 * @param esquemaTarifario Código del esquema tarifario
	 * @return true si se cargó correctamente
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