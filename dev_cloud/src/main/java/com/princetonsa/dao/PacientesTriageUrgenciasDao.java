/*
 * @(#)PacientesTriageUrgenciasDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;

/**
 *  Interfaz para el acceder a la fuente de datos de pacientes triage x sistema
 *
 * @version 1.0, Jun 1 / 2006
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public interface PacientesTriageUrgenciasDao 
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
						 );
	
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
						 );
	
}
