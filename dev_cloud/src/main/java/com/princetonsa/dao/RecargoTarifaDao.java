/*
 * @(#)RecargoTarifaDao.java
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

import util.ResultadoBoolean;
import util.ResultadoCollectionDB;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 *  Interfaz para el acceder a la fuente de datos de un recargo de tarifa 
 * 
 * @version 1.0, Mayo 03 / 2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public interface RecargoTarifaDao
{
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
	 * @return ResultadoBoolean, true si la inserci�n fue exitosa, false y con la descripci�n 
	 * de lo contrario
	 */
	public ResultadoBoolean insertar(	Connection con,
															double porcentaje,
															double valor,
															int codigoTipoRecargo,
															int codigoServicio,
															int codigoEspecialidad,
															int codigoViaIngreso,
															int codigoContrato	)throws SQLException;

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
	 */
	public ResultadoBoolean insertarTransaccional(	Connection con,
																					double porcentaje,
																					double valor,
																					int codigoTipoRecargo,
																					int codigoServicio,
																					int codigoEspecialidad,
																					int codigoViaIngreso,
																					int codigoContrato,
																					String estado	) throws SQLException;

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
	 */
	public ResultadoBoolean modificar(	Connection con,
																int codigo,
																double porcentaje,
																double valor,
																int codigoTipoRecargo,
																int codigoServicio,
																int codigoEspecialidad,
																int codigoViaIngreso,
																int codigoContrato	) throws SQLException;

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
																						String estado	) throws SQLException;

																						
	/**
	 * Elimina un recargo dado el c�digo
	 * @param con, Connection, conexi�n abierta con la fuente de datos
	 * @param codigo. int, c�digo del recargo a eliminar
	 * @return ResultadoBoolean, true si la eliminaci�n fue exitosa, false y con la descripci�n 
	 * de lo contrario
	 */
	public ResultadoBoolean eliminar(	Connection con,
															int codigo	) throws SQLException;
																				
	/**
	 * Consulta un recargo dado el c�digo
	 * @param con, Connection, conexi�n abierta con la fuente de datos
	 * @param codigo. int, c�digo del recargo a consultar
	 * @return ResultadoCollectionDB, true y con la colecci�n de HashMap con el resultado si fue exitosa
	 * la consulta, false y con la descripci�n de lo contrario
	 */
	public ResultadoCollectionDB consultar(	Connection con, 
																		int codigo,boolean conCodigo,int codigoViaIngreso,int codigoEspecialidad,int codigoServicio, int codigoContrato	) throws SQLException;	
	/**
	 * Method existeRecargo.
	 * @param con
	 * @param i
	 * @param i1
	 * @param i2
	 * @param i3
	 * @param i4
	 * @return boolean
	 */
	public boolean existeRecargo(
		Connection con,
		int i,
		int i1,
		int i2,
		int i3,
		int i4) throws SQLException;
	
	/**
	 * Metodo que realiza la consulta de un registro de tarifa especifico.
	 * @param con, Connection con la fuente de datos.
	 * @param codigo, Codigo del recargo de tarifa.
	 * @return ResultSet.
	 */
	public ResultSetDecorator consultaRecargoTarifa (Connection con, int codigo);
	
	/**
	 * Carga el siguiente codigo recargo   (table= recargo_tarifas))
	 * @param con Connection con la fuente de datos
	 * @return int ultimoCodigoSequence, 0 no efectivo.
	 */
	public int cargarUltimoCodigoSequence(Connection con);
	

}
