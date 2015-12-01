/*
 * @(#)OraclePacientesUrgenciasPorValorarDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import com.princetonsa.dao.PacientesUrgenciasPorValorarDao;
import com.princetonsa.dao.sqlbase.SqlBasePacientesUrgenciasPorValorarDao;

/**
 * Implementación oracle de las funciones de acceso a la fuente de datos
 * para pacientes de urgencias por valorar
 *
 * @version 1.0, Jun 3 / 2006
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 */
public class OraclePacientesUrgenciasPorValorarDao implements PacientesUrgenciasPorValorarDao
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
	public HashMap consultarPacientesUrgPorValorar(Connection con, int codigoCentroAtencion, int institucion) throws SQLException
	{
		return SqlBasePacientesUrgenciasPorValorarDao.consultarPacientesUrgPorValorar(con, codigoCentroAtencion, institucion);
	}
}