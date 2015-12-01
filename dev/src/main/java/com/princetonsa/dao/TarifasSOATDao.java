/*
 * @(#)TarifasSOATDao.java
 * 
 * Created on 04-May-2004
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados
 * 
 * Lenguaje : Java
 * Compilador : J2SDK 1.4
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import util.ResultadoCollectionDB;

/**
  * Interfaz para acceder a la fuente de datos del módulo de tarifas soat
 * 
 * @version 1.0, 04-May-2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public interface TarifasSOATDao
{/**
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
										String liquidacionServicio, boolean buscarPorLiquidacionServicio) throws SQLException;
	
	/**
	 * Metodo para consultar todas las fechas de vigencia por esquema servicio
	 * @param con
	 * @param esquemaTarifario
	 * @param servicio
	 * @param cadenaCodigosServicios
	 * @return
	 */
	public HashMap consultarFechasVigencia(Connection con, String esquemaTarifario, String servicio, String cadenaCodigosServicios);
}
