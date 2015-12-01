/*
 * @(#)PacientesUrgenciasPorHospitalizarDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
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
 *  Interfaz para el acceder a la fuente de datos de pacientes de urgencias
 * por hospitalizar
 *
 * @version 1.0, Jul 25 / 2006
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 */
public interface PacientesUrgenciasPorHospitalizarDao 
{
	/**
	 * Método para consultar los todos los datos de los pacientes de via de ingreso de
	 * urgencias pendientes por Hospitalizar
	 * @param con
	 * @param codigoCentroAtencion
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultarPacientesUrgPorHospitalizar(Connection con, int codigoCentroAtencion, int institucion) throws SQLException;
	
}
