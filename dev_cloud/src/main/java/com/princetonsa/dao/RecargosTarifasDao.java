/*
 * @(#)RecargosTarifasDao.java
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

import util.ResultadoCollectionDB;

/**
  * Interfaz para acceder a la fuente de datos del m�dulo de recargos de tarifas 
 * 
 * @version 1.0, 04-May-2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public interface RecargosTarifasDao
{

	/**
	 * Consulta todos los recargos que cumplan con los parametros ingresados.
	 * 
	 * @param con, Connection, conexi�n abierta con la fuente de datos
	 * @param codigo, int, c�digo del recargo a modificar
	 * @param buscarPorCodigo, boolean, dice si se debe filtrar por c�digo o no
	 * @param porcentaje. double, porcentaje de recargo de la tarifa
	 * @param buscarPorPorcentaje, boolean, dice si se debe filtrar por porcentaje o no
	 * @param valor. double, valor de recargo de la tarifa
	 * @param buscarPorValor, boolean, dice si se debe filtrar por valor o no
	 * @param codigoTipoRecargo. int, c�digo del tipo de recargo
	 * @param buscarPorCodigoTipoRecargo, boolean, dice si se debe filtrar por el c�digo del Tipo de Recargo o no
	 * @param codigoServicio. int, c�digo del servicio asociado esta tarifa, 0 es para todos
	 * @param buscarPorCodigoServicio, boolean, dice si se debe filtrar por el c�digo del Servicio o no
	 * @param codigoEspecialidad. int, c�digo de la especialidad asociado esta tarifa, 0 es para todas
	 * @param buscarPorCodigoEspecialidad, boolean, dice si se debe filtrar por el c�digo de la especialidad o no
	 * @param codigoViaIngreso. int, c�digo de la via de ingreso asociado esta tarifa, 0 es para todas
	 * @param buscarPorCodigoViaIngreso, boolean, dice si se debe filtrar por el c�digo de la Via de Ingreso o no
	 * @param codigoContrato. int, c�digo del contrato para el cual es v�lido este recargo
	 * @param buscarPorCodigoContrato, boolean, dice si se debe filtrar por el c�digo de Contrato o no
	 * @return ResultadoBoolean, true si la modificacion fue exitosa, false y con la descripci�n 
	 * de lo contrario
	 */
	public ResultadoCollectionDB consultar(	Connection con,
																int codigo,
																boolean buscarPorCodigo,
																double porcentaje,
																boolean buscarPorPorcentaje,
																double valor,
																boolean buscarPorValor,
																int codigoTipoRecargo,
																boolean buscarPorCodigoTipoRecargo,
																int codigoServicio,
																boolean buscarPorCodigoServicio,
																int codigoEspecialidad,
																boolean buscarPorCodigoEspecialidad,
																int codigoViaIngreso,
																boolean buscarPorCodigoViaIngreso,
																int codigoContrato,
																boolean buscarPorCodigoContrato, 
																int codigoConvenio) throws SQLException;
}
