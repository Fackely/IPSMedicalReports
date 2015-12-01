/*
 * @(#)PostgresqlConsultaReferenciaContrareferenciaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK jdk1.5.0_07
 *
 */
package com.princetonsa.dao.postgresql.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.historiaClinica.ConsultaReferenciaContrareferenciaDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseConsultaReferenciaContrareferenciaDao;
import com.princetonsa.mundo.historiaClinica.ConsultaReferenciaContrareferencia;

/**
 * 
 * @author Wilson Rios
 * wrios@princetonsa.com
 */
public class PostgresqlConsultaReferenciaContrareferenciaDao implements ConsultaReferenciaContrareferenciaDao 
{
	/**
	 * 
	 * @param con
	 * @param criteriosBusquedaMap
	 * @param esBusquedaXpaciente
	 * @return
	 */
	public HashMap busquedaReferenciaContrareferencia(Connection con, ConsultaReferenciaContrareferencia mundo)
	{
		return SqlBaseConsultaReferenciaContrareferenciaDao.busquedaReferenciaContrareferencia(con, mundo);
	}
}
