/*
 * @(#)HistoriaClinicaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta el servicio
 * de acceso a datos para el objeto <code>HistoriaClinica</code>.
 *
 * @version 1.0, Oct 7, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public interface HistoriaClinicaDao {

	/**
	 * Inserta una historia clinica en una fuente de datos, reutilizando 
	 * una conexion existente, con los datos presentes en los atributos 
	 * de este objeto. No maneja conexiones dentro de s�
	 * @param con una conexion abierta con una fuente de datos
	 * @param tipoId tipo de identificacion del paciente al cual pertenece la historia cl�nica
	 * @param numeroId n�mero de identificacion del paciente al cual pertenece la historia cl�nica
	 * @return n�mero de historias insertadas (debe ser 1)
	 */
	public int insertarHistoriaClinica(Connection con, String historiaClinicaAnterior, int codigoPaciente) throws SQLException;

	/**
	 * Carga los datos de una historia cl�nica desde la fuente de datos
	 * @param con una conexion abierta con una fuente de datos
	 * @param tipoId tipo de identificacion del paciente al cual pertenece la historia cl�nica
	 * @param numeroId n�mero de identificacion del paciente al cual pertenece la historia cl�nica
	 * @return un objeto <code>Answer</code> con los datos pedidos y una conexi�n abierta.
	 */
	public ResultSetDecorator cargarHistoriaClinica(Connection con, int codigoPaciente) throws SQLException;

	/**
	 * Modifica una historia cl�nica ya existente en una fuente de datos, reutilizando 
	 * una conexion existente, con los datos pasados como par�metro.
	 * @param con una conexion abierta con una fuente de datos
	 * @param tipoId tipo de identificacion del paciente al cual pertenece la historia cl�nica
	 * @param anioApertura a�o de apertura de la historia cl�nica
	 * @param mesApertura mes de apertura de la historia cl�nica
	 * @param diaApertura d�a de apertura de la historia cl�nica
	 * @param numeroId n�mero de identificacion del paciente al cual pertenece la historia cl�nica
	 * @return n�mero de historias modificadas (debe ser 1)
	 */
	public int modificarHistoriaClinica(Connection con, int codigoPaciente, String fechaApertura) throws SQLException;

	/**
	 * Carga los datos de todas las instituciones en las cuales este paciente
	 * tiene historia cl�nica de una historia cl�nica desde la fuente de datos
	 * 
	 * @param con una conexion abierta con la fuente de datos
	 * @param codigoPaciente C�digo del paciente con el que se cargan las 
	 * instituciones
	 * @return un ResultSetDecorator con los datos pedidos y una conexi�n abierta.
	 */
	public ResultSetDecorator cargarInstitucionesConHistoria(Connection con, int codigoPaciente) throws SQLException;
	
	/**
	 * M�todo implementado para modificar la descripcion de la historia clinica previa del paciente
	 * @param con
	 * @param codigoPaciente
	 * @param historiaAnterior
	 * @return
	 */
	public int modificarHistoriaPrevia(Connection con,int codigoPaciente,String historiaAnterior);
	
}