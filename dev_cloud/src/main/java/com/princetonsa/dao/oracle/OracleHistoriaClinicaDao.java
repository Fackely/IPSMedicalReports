/*
 * @(#)OracleHistoriaClinicaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.dao.oracle;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import com.princetonsa.dao.HistoriaClinicaDao;
import com.princetonsa.dao.sqlbase.SqlBaseHistoriaClinicaDao;

/**
 * Esta clase implementa el contrato estipulado en <code>HistoriaClinicaDao</code>, proporcionando los servicios
 * de acceso a una base de datos Oracle requeridos por la clase <code>HistoriaClinica</code>. A diferencia de 
 * paciente o usuario, en esta clase no inicia transacciones, sino que las termina como finalización del proceso
 * de ingreso de un paciente.
 * 
 * @version 1.0, Oct 16, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class OracleHistoriaClinicaDao implements HistoriaClinicaDao
{

	/**
	 * Inserta una historia clinica en una base de datos OracleSQL, reutilizando 
	 * una conexion existente, con los datos presentes en los atributos 
	 * de este objeto.
	 * @param con una conexion abierta con una base de datos OracleSQL
	 * @param tipoId tipo de identificacion del paciente al cual pertenece la historia clínica
	 * @param codigoPaciente Código que identifica al paciente al cual pertenece la historia clínica
	 * @return número de historias insertadas (debe ser 1)
	 */
	public int insertarHistoriaClinica(Connection con, String historiaClinicaAnterior, int codigoPaciente) throws SQLException
	{
	    return SqlBaseHistoriaClinicaDao.insertarHistoriaClinica(con, historiaClinicaAnterior, codigoPaciente) ;
	}

	/**
	 * Carga los datos de una historia clínica desde la base de datos OracleSQL
	 * @param con una conexion abierta con una base de datos OracleSQL
	 * @param tipoId tipo de identificacion del paciente al cual pertenece la historia clínica
	 * @param numeroId número de identificacion del paciente al cual pertenece la historia clínica
	 * @return un objeto <code>Answer</code> con los datos pedidos y una conexión abierta.
	 */
	public ResultSetDecorator cargarHistoriaClinica(Connection con, int codigoPaciente) throws SQLException
	{
		return SqlBaseHistoriaClinicaDao.cargarHistoriaClinica(con, codigoPaciente) ;
	}

	/**
	 * Modifica una historia clínica ya existente en una base de datos OracleSQL, reutilizando 
	 * una conexion existente, con los datos pasados como parámetro.
	 * @param con una conexion abierta con una base de datos OracleSQL
	 * @param tipoId tipo de identificacion del paciente al cual pertenece la historia clínica
	 * @param anioApertura año de apertura de la historia clínica
	 * @param mesApertura mes de apertura de la historia clínica
	 * @param diaApertura día de apertura de la historia clínica
	 * @param numeroId número de identificacion del paciente al cual pertenece la historia clínica
	 * @return número de historias modificadas (debe ser 1)
	 */
	public int modificarHistoriaClinica(Connection con, int codigoPaciente, String fechaApertura) throws SQLException
	{
		return SqlBaseHistoriaClinicaDao.modificarHistoriaClinica(con, codigoPaciente, fechaApertura) ;
	}

	/**
	 * Carga los datos de todas las instituciones en las cuales este paciente
	 * tiene historia clínica de una historia clínica desde la base de datos
	 * OracleSQL
	 * 
	 * @param con una conexion abierta con una base de datos OracleSQL
	 * @param tipoId tipo de identificacion del paciente al cual pertenece la historia clínica
	 * @param numeroId número de identificacion del paciente al cual pertenece la historia clínica
	 * @return un objeto <code>Answer</code> con los datos pedidos y una conexión abierta.
	 */
	public ResultSetDecorator cargarInstitucionesConHistoria(Connection con, int codigoPaciente) throws SQLException
	{
		return SqlBaseHistoriaClinicaDao.cargarInstitucionesConHistoria(con, codigoPaciente) ;
	}
	
	/**
	 * Método implementado para modificar la descripcion de la historia clinica previa del paciente
	 * @param con
	 * @param codigoPaciente
	 * @param historiaAnterior
	 * @return
	 */
	public int modificarHistoriaPrevia(Connection con,int codigoPaciente,String historiaAnterior)
	{
		return SqlBaseHistoriaClinicaDao.modificarHistoriaPrevia(con,codigoPaciente,historiaAnterior);
	}
}