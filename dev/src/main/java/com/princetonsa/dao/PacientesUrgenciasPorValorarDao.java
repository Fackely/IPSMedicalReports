/*
 * @(#)PacientesUrgenciasPorValorarDao.java
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
import java.util.HashMap;

/**
 *  Interfaz para el acceder a la fuente de datos de pacientes de urgencias
 *  pendientes por valoracion
 *
 * @version 1.0, Jun 3 / 2006
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 */
public interface PacientesUrgenciasPorValorarDao 
{
	/**
	 * Método para consultar los todos los datos de los pcientes de triage de via de ingreso de
	 * urgencias pendientes de valoracion inicial de urgencias
	 * @param con
	 * @param codigoCentroAtencion
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarPacientesUrgPorValorar(Connection con, int codigoCentroAtencion, int institucion) throws SQLException;
}
