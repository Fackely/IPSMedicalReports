/*
 * @(#)ConsultaPacientesTriageDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 01 /Jun/ 2006
 */
public interface ConsultaPacientesTriageDao  
{
	/**
	 * Metodo para cargar los pacientes que estan pendientes de atencion triage
	 * para un centro de atencion determinado
	 * @param con
	 * @param centroAtencion
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultarPacientesTriage (Connection con, int centroAtencion) throws SQLException;
	
	/**
	 * Método implementado para cargar los datos de un paciente regitrado para triage
	 * y que se encuentre pendiente
	 * @param con
	 * @param codigoPaciente
	 * @param codigoCentroAtencion
	 * @return
	 */
	public HashMap consultarDatosPacienteTriage (Connection con,int codigoPaciente,int codigoCentroAtencion);
}