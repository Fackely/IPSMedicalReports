/*
 * @(#)OraclePacientesTriageUrgenciasDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.PacientesTriageUrgenciasDao;
import com.princetonsa.dao.sqlbase.SqlBasePacientesTriageUrgenciasDao;

/**
 * Implementación oracle de las funciones de acceso a la fuente de datos
 * para pacientes triage urgencias
 *
 * @version 1.0, Jun 2 / 2006
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class OraclePacientesTriageUrgenciasDao implements PacientesTriageUrgenciasDao
{
	/**
	 * listado de pacientes triage urgencias
	 * @param con
	 * @param codigoCentroAtencion
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap listado(	Connection con, 
	       					String codigoCentroAtencion,
	       					int codigoInstitucion
						 )
	{
		return SqlBasePacientesTriageUrgenciasDao.listado(con, codigoCentroAtencion, codigoInstitucion);
	}

	/**
	 * listado de pacientes triage urgencias
	 * para un rango de fechas determinado
	 * @param con
	 * @param codigoCentroAtencion
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap listadoBusquedaAvanzada(	Connection con, 
	       					String codigoCentroAtencion,
	       					int codigoInstitucion,
	       					String fechaInicial,
	       					String fechaFinal
						 )
	{
		return SqlBasePacientesTriageUrgenciasDao.listadoBusquedaAvanzada(con, codigoCentroAtencion, codigoInstitucion, fechaInicial, fechaFinal);
	}
	
}