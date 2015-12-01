/*
 * @(#)OracleRecargoTarifaDao.java
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
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import util.ResultadoBoolean;
import util.ResultadoCollectionDB;

import com.princetonsa.dao.RecargoTarifaDao;
import com.princetonsa.dao.sqlbase.SqlBaseRecargoTarifaDao;

/**
 * Implementaci�n Oracle de las funciones de acceso a la fuente de datos
 * para un recargo de una tarifa
 * 
 * @version 1.0, 04-May-2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class OracleRecargoTarifaDao implements RecargoTarifaDao
{//SIN PROBAR FUNC. SECUENCIA

	/**
	 * Insertar un recargo
	 */
	public static String insertarRecargoStr="INSERT INTO recargos_tarifas(codigo,porcentaje, valor,via_ingreso,especialidad,contrato,servicio,tipo_recargo) " +
																	  "VALUES(seq_recargos_tarifas.nextval,?,?,?,?,?,?,?)";
	/**
	 * Inserta un recargo a una tarifa 
	 * @param con, Connection, conexi�n abierta con la fuente de datos
	 * @param porcentaje. double, porcentaje de recargo de la tarifa
	 * @param valor. double, valor de recargo de la tarifa
	 * @param codigoTipoRecargo. int, c�digo del tipo de recargo
	 * @param codigoServicio. int, c�digo del servicio asociado esta tarifa, 0 es para todos
	 * @param codigoEspecialidad. int, c�digo de la especialidad asociado esta tarifa, 0 es para todas
	 * @param codigoViaIngreso. int, c�digo de la via de ingreso asociado esta tarifa, 0 es para todas
	 * @param codigoContrato. int, c�digo del contrato para el cual es v�lida este recargo
	 * @return ResultadoBoolean, true si la inserci�n fue exitosa, false y con la descripci�n 
	 * de lo contrario
	 * @see com.princetonsa.dao.RecargoTarifaDao#insertar(java.sql.Connection, double, double, int, int, int, int, int)
	 */
	public ResultadoBoolean insertar(	Connection con,
															double porcentaje,
															double valor,
															int codigoTipoRecargo,
															int codigoServicio,
															int codigoEspecialidad,
															int codigoViaIngreso,
															int codigoContrato) throws SQLException
	{
		return SqlBaseRecargoTarifaDao.insertar(	con,
																			porcentaje,
																			valor,
																			codigoTipoRecargo,
																			codigoServicio,
																			codigoEspecialidad,
																			codigoViaIngreso,
																			codigoContrato,
																			insertarRecargoStr);
	}

	/**
	 * Inserta un recargo a una tarifa 
	 * @param con, Connection, conexi�n abierta con la fuente de datos
	 * @param porcentaje. double, porcentaje de recargo de la tarifa
	 * @param valor. double, valor de recargo de la tarifa
	 * @param codigoTipoRecargo. int, c�digo del tipo de recargo
	 * @param codigoServicio. int, c�digo del servicio asociado esta tarifa, 0 es para todos
	 * @param codigoEspecialidad. int, c�digo de la especialidad asociado esta tarifa, 0 es para todas
	 * @param codigoViaIngreso. int, c�digo de la via de ingreso asociado esta tarifa, 0 es para todas
	 * @param codigoContrato. int, c�digo del contrato para el cual es v�lido este recargo
	 * @param estado. String, estado dentro de la transacci�n
	 * @return ResultadoBoolean, true si la inserci�n fue exitosa, false y con la descripci�n 
	 * de lo contrario
	 * @see com.princetonsa.dao.RecargoTarifaDao#insertarTransaccional(java.sql.Connection, double, double, int, int, int, int, int, java.lang.String)
	 */
	public ResultadoBoolean insertarTransaccional(	Connection con,
																					double porcentaje,
																					double valor,
																					int codigoTipoRecargo,
																					int codigoServicio,
																					int codigoEspecialidad,
																					int codigoViaIngreso,
																					int codigoContrato,
																					String estado) throws SQLException
	{
		return SqlBaseRecargoTarifaDao.insertarTransaccional(	con,
																								porcentaje,
																								valor,
																								codigoTipoRecargo,
																								codigoServicio,
																								codigoEspecialidad,
																								codigoViaIngreso,
																								codigoContrato,
																								estado,
																								insertarRecargoStr);
	}

	/**
	 * Modifica un recargo a una tarifa 
	 * @param con, Connection, conexi�n abierta con la fuente de datos
	 * @param codigo, int, c�digo del recargo a modificar
	 * @param porcentaje. double, porcentaje de recargo de la tarifa
	 * @param valor. double, valor de recargo de la tarifa
	 * @param codigoTipoRecargo. int, c�digo del tipo de recargo
	 * @param codigoServicio. int, c�digo del servicio asociado esta tarifa, 0 es para todos
	 * @param codigoEspecialidad. int, c�digo de la especialidad asociado esta tarifa, 0 es para todas
	 * @param codigoViaIngreso. int, c�digo de la via de ingreso asociado esta tarifa, 0 es para todas
	 * @param codigoContrato. int, c�digo del contrato para el cual es v�lido este recargo
	 * @return ResultadoBoolean, true si la modificacion fue exitosa, false y con la descripci�n 
	 * de lo contrario
	 * @see com.princetonsa.dao.RecargoTarifaDao#modificar(java.sql.Connection, int, double, double, int, int, int, int, int)
	 */
	public ResultadoBoolean modificar(	Connection con,
																int codigo,
																double porcentaje,
																double valor,
																int codigoTipoRecargo,
																int codigoServicio,
																int codigoEspecialidad,
																int codigoViaIngreso,
																int codigoContrato) throws SQLException
	{
		return SqlBaseRecargoTarifaDao.modificar(	con,
																				codigo,
																				porcentaje,
																				valor,
																				codigoTipoRecargo,
																				codigoServicio,
																				codigoEspecialidad,
																				codigoViaIngreso,
																				codigoContrato);
	}

	/**
	 * Modifica un recargo a una tarifa 
	 * @param con, Connection, conexi�n abierta con la fuente de datos
	 * @param codigo, int, c�digo del recargo a modificar
	 * @param porcentaje. double, porcentaje de recargo de la tarifa
	 * @param valor. double, valor de recargo de la tarifa
	 * @param codigoTipoRecargo. int, c�digo del tipo de recargo
	 * @param codigoServicio. int, c�digo del servicio asociado esta tarifa, 0 es para todos
	 * @param codigoEspecialidad. int, c�digo de la especialidad asociado esta tarifa, 0 es para todas
	 * @param codigoViaIngreso. int, c�digo de la via de ingreso asociado esta tarifa, 0 es para todas
	 * @param codigoContrato. int, c�digo del contrato para el cual es v�lido este recargo
	 * @param estado. String, estado dentro de la transacci�n
	 * @return ResultadoBoolean, true si la modificaci�n fue exitosa, false y con la descripci�n 
	 * de lo contrario
	 * @see com.princetonsa.dao.RecargoTarifaDao#modificarTransaccional(java.sql.Connection, int, double, double, int, int, int, int, int, java.lang.String)
	 */
	public ResultadoBoolean modificarTransaccional(	Connection con,
																						int codigo,
																						double porcentaje,
																						double valor,
																						int codigoTipoRecargo,
																						int codigoServicio,
																						int codigoEspecialidad,
																						int codigoViaIngreso,
																						int codigoContrato,
																						String estado) throws SQLException
	{
		return SqlBaseRecargoTarifaDao.modificarTransaccional(	con,
																									codigo,
																									porcentaje,
																									valor,
																									codigoTipoRecargo,
																									codigoServicio,
																									codigoEspecialidad,
																									codigoViaIngreso,
																									codigoContrato,
																									estado);
	}

	/**
	 * Elimina un recargo dado el c�digo
	 * @param con, Connection, conexi�n abierta con la fuente de datos
	 * @param codigo. int, c�digo del recargo a eliminar
	 * @return ResultadoBoolean, true si la eliminaci�n fue exitosa, false y con la descripci�n 
	 * de lo contrario
	 * @see com.princetonsa.dao.RecargoTarifaDao#eliminar(java.sql.Connection, int)
	 */
	public ResultadoBoolean eliminar(Connection con, int codigo) throws SQLException
	{
		return SqlBaseRecargoTarifaDao.eliminar(con, codigo);
	}

	/**
	 * Consulta un recargo dado el c�digo
	 * @param con, Connection, conexi�n abierta con la fuente de datos
	 * @param codigo. int, c�digo del recargo a consultar
	 * @return ResultadoCollectionDB, true y con la colecci�n de HashMap con el resultado si fue exitosa
	 * la consulta, false y con la descripci�n de lo contrario
	 * @see com.princetonsa.dao.RecargoTarifaDao#consultar(java.sql.Connection, int)
	 */
	public ResultadoCollectionDB consultar(Connection con, int codigo,boolean conCodigo,int codigoViaIngreso,int codigoEspecialidad,int codigoServicio, int codigoContrato) throws SQLException
	{
		return SqlBaseRecargoTarifaDao.consultar(con, codigo,conCodigo,codigoViaIngreso,codigoEspecialidad,codigoServicio,codigoContrato);
	}
	public boolean existeRecargo(Connection con,
																		int codigoTipoRecargo,
																		int codigoServicio,
																		int codigoEspecialidad,
																		int codigoViaIngreso,
																		int codigoContrato) throws SQLException{
																			return SqlBaseRecargoTarifaDao.existeRecargo(con,
																			codigoTipoRecargo,
																			codigoServicio,
																			codigoEspecialidad,
																			codigoViaIngreso,
																			codigoContrato);
																		}
	
	
	/**
	 * Metodo que realiza la consulta de un registro de tarifa especifico.
	 * @param con, Connection con la fuente de datos.
	 * @param codigo, Codigo del recargo de tarifa.
	 * @return ResultSet.
	 */
	public ResultSetDecorator consultaRecargoTarifa (Connection con, int codigo)
	{
	    return SqlBaseRecargoTarifaDao.consultaRecargoTarifa(con,codigo); 
	}
	
	/**
	 * Carga el siguiente codigo recargo   (table= recargo_tarifas))
	 * @param con Connection con la fuente de datos
	 * @return int ultimoCodigoSequence, 0 no efectivo.
	 */
	public int cargarUltimoCodigoSequence(Connection con)
	{
	    return SqlBaseRecargoTarifaDao.cargarUltimoCodigoSequence(con);   
	}

}
