/*
 * @(#)PostgresqlPacientesUrgenciasPorHospitalizarDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import com.princetonsa.dao.PacientesUrgenciasPorHospitalizarDao;
import com.princetonsa.dao.sqlbase.SqlBasePacientesUrgenciasPorHospitalizarDao;

/**
 * Implementación oracle de las funciones de acceso a la fuente de datos
 * para pacientes de urgencias pendientes por Hospitalizar
 *
 * @version 1.0, Jul 24 / 2006
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 */
public class PostgresqlPacientesUrgenciasPorHospitalizarDao implements PacientesUrgenciasPorHospitalizarDao
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
	public ResultSetDecorator consultarPacientesUrgPorHospitalizar(Connection con, int codigoCentroAtencion, int institucion) throws SQLException
	{
		return SqlBasePacientesUrgenciasPorHospitalizarDao.consultarPacientesUrgPorHospitalizar(con, codigoCentroAtencion, institucion);
	}
}