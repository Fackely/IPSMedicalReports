/*
 * @(#)OracleTarifasISSDao.java
 * 
 * Created on 04-May-2004
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados
 * 
 * Lenguaje : Java
 * Compilador : J2SDK 1.4
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import util.ResultadoCollectionDB;

import com.princetonsa.dao.TarifasISSDao;
import com.princetonsa.dao.sqlbase.SqlBaseTarifaISSDao;

/**
 * Esta clase implementa <code>TarifasISSDao</code>, proporcionando los servicios
 * de acceso a una base de datos Oracle requeridos por la clase <code>TarifasISS</code>

 * 
 * @version 1.0, 04-May-2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class OracleTarifasISSDao implements TarifasISSDao
{
	/**
	 *	Consulta todas las tarifas iss que cumplan con los parametros ingresados.
	 *
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoTarifa. int, código de la tarifa
	 * @param buscarPorCodigoTarifa, boolean, dice si se debe filtrar por el código de la tarifa o no
	 * @param codigoEsquemaTarifario. int, código del esquema tarifario
	 * @param buscarPorCodigoEsquemaTarifario, boolean, dice si se debe filtrar por el código del esquema tarifario o no
	 * @param codigoServicio. int, código del servicio
	 * @param buscarPorCodigoServicio, boolean, dice si se debe filtrar por el código del servicio o no
	 * @param codigoTipoLiquidacion. int, código del tipo de liquidación
	 * @param buscarPorCodigoTipoLiquidacion, boolean, dice si se debe filtrar por el codigo de tipo de liquidación o no
	 * @param valorTarifa. double, valor de la tarifa
	 * @param buscarPorValorTarifa, boolean, dice si se debe filtrar por el valor de la tarifa o no
	 * @param porcentajeIva. double, porcentaje iva
	 * @param buscarPorPorcentajeIva, boolean, dice si se debe filtrar por el porcentaje de iva o no
	 * @param liqAsocios, boolean buscarPorLiqAsocios
	 * @return ResultadoCollectionDB, true y con la colección de HashMap con el resultado si fue exitosa
	 * la consulta, false y con la descripción de lo contrario
	 */
	public ResultadoCollectionDB consultar(	Connection con,
																		int codigoTarifa,
																		boolean buscarPorCodigoTarifa,
																		int codigoEsquemaTarifario,
																		boolean buscarPorCodigoEsquemaTarifario,
																		String codigoServicio,
																		boolean buscarPorCodigoServicio,
																		double valorTarifa,
																		boolean buscarPorValorTarifa,																		
																		double porcentajeIva,
																		boolean buscarPorPorcentajeIva,
																		String nombreServicio,
																		boolean buscarPorNombreServicio,
																		int codigoTipoLiquidacion,
																		boolean buscarPorCodigoTipoLiquidacion,
																		String liqAsocios,
																		boolean buscarPorLiqAsocios,
																		int institucion) throws SQLException
	{
	
		return SqlBaseTarifaISSDao.consultar(con,
					codigoTarifa,buscarPorCodigoTarifa,
					codigoEsquemaTarifario,buscarPorCodigoEsquemaTarifario,
					codigoServicio,buscarPorCodigoServicio,
					valorTarifa,buscarPorValorTarifa,
					porcentajeIva,buscarPorPorcentajeIva,
					nombreServicio,buscarPorNombreServicio,
					codigoTipoLiquidacion,buscarPorCodigoTipoLiquidacion,
					liqAsocios,buscarPorLiqAsocios,
					institucion, "");	
	}
	
	/**
	 * Metodo que consulta todas las fechas vigencia por esquema servicio
	 * @param con
	 * @param esquemaTarifario
	 * @param servicio
	 * @return
	 */
	public HashMap consultarFechasVigencia(Connection con, String esquemaTarifario, String servicio, String cadenaCodigosServicios)
	{
		return SqlBaseTarifaISSDao.consultarFechasVigencia(con, esquemaTarifario, servicio, cadenaCodigosServicios);
	}
}
