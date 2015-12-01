/*
 * @(#)PacientesUrgenciasSalaEsperaDao.java
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
 *  pendientes por valoracion
 *
 * @version 1.0, Jun 3 / 2006
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 */
public interface PacientesUrgenciasSalaEsperaDao 
{
	/**
	 * Método para consultar los todos los datos de los pacientes de via de ingreso de
	 * urgencias con conducta a seguir "Sala de Espera"
	 * @param con
	 * @param codigoCentroAtencion
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultarPacientesUrgSalaEspera(Connection con, int codigoCentroAtencion, int institucion) throws SQLException;
	
	/**
	 * Consulta de ordenamiento asc o desc 
	 * @param con
	 * @param codigoCentroAtencion
	 * @param institucion
	 * @param orderBy
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultarPacientesUrgSalaEsperaOrdenamientoPorTiempoSalaEspera(Connection con, int codigoCentroAtencion, int institucion,Integer orderBy) throws SQLException;
}
