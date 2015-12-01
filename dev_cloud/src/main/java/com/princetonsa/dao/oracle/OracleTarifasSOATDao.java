/*
 * @(#)OracleTarifasSOATDao.java
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

import com.princetonsa.dao.TarifasSOATDao;
import com.princetonsa.dao.sqlbase.SqlBaseTarifaSOATDao;

/**
 * Esta clase implementa <code>TarifasSOATDao</code>, proporcionando los servicios
 * de acceso a una base de datos Oracle requeridos por la clase <code>TarifasSOAT</code>

 * 
 * @version 1.0, 04-May-2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class OracleTarifasSOATDao implements TarifasSOATDao
{
	/**
	 * Busqueda avanzada para tarifas SOAT
	 * @param con
	 * @param codigoTarifa
	 * @param buscarPorCodigoTarifa
	 * @param codigoEsquemaTarifario
	 * @param buscarPorCodigoEsquemaTarifario
	 * @param codigoServicio
	 * @param buscarPorCodigoServicio
	 * @param grupo
	 * @param buscarPorGrupo
	 * @param codigoTipoLiquidacion
	 * @param buscarPorCodigoTipoLiquidacion
	 * @param valorTarifa
	 * @param buscarPorValorTarifa
	 * @param porcentajeIva
	 * @param buscarPorPorcentajeIva
	 * @param liquidacion Servicio
	 * @param buscarPorLiquidacionServicio
	 * @return ResultadoCollectionDB
	 * @throws SQLException
	 */
	public  ResultadoCollectionDB consultar(Connection con,
	int codigoTarifa,boolean buscarPorCodigoTarifa,
	int codigoEsquemaTarifario,boolean buscarPorCodigoEsquemaTarifario,
	String codigoServicio,boolean buscarPorCodigoServicio,
	double grupo,boolean buscarPorGrupo,
	int codigoTipoLiquidacion,boolean buscarPorCodigoTipoLiquidacion,
	double valorTarifa,boolean buscarPorValorTarifa,
	double porcentajeIva,boolean buscarPorPorcentajeIva,
	String nombreServicio, boolean buscarPorNombreServicio,
	String liquidacionServicio, boolean buscarPorLiquidacionServicio) throws SQLException
	{
		return SqlBaseTarifaSOATDao.consultar(con,
		codigoTarifa,
		buscarPorCodigoTarifa,
		codigoEsquemaTarifario,
		buscarPorCodigoEsquemaTarifario,
		codigoServicio,
		buscarPorCodigoServicio,
	 	grupo,
		buscarPorGrupo,
		codigoTipoLiquidacion,
		buscarPorCodigoTipoLiquidacion,														
		valorTarifa,
		buscarPorValorTarifa,
		porcentajeIva,
		buscarPorPorcentajeIva,
		nombreServicio,
		buscarPorNombreServicio,
		liquidacionServicio,
		buscarPorLiquidacionServicio, "");
	}
	
	/**
	 * Metodo que consulta todas las fechas vigencia por esquema servicio
	 * @param con
	 * @param esquemaTarifario
	 * @param servicio
	 * @param cadenaCodigosServicios
	 */
	public HashMap consultarFechasVigencia(Connection con, String esquemaTarifario, String servicio, String cadenaCodigosServicios)
	{
		return SqlBaseTarifaSOATDao.consultarFechasVigencia(con, esquemaTarifario, servicio, cadenaCodigosServicios);
	}
}
