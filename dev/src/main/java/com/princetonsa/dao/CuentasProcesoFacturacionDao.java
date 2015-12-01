/*
 * @(#)CuentasProcesoFacturacionDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2006. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;

/**
 *  Interfaz para el acceder a la fuente de datos 
 *
 * @version 1.0, Julio 6 / 2006
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public interface CuentasProcesoFacturacionDao 
{
	/**
	 * listado X Paciente
	 * @param con
	 * @param codigoCentroAtencion
	 * @param codigoInstitucion
	 * @param codigoPersona
	 * @return
	 */
	public HashMap listadoXPaciente(	Connection con, 
	       								String codigoCentroAtencion,
	       								int codigoInstitucion,
	       								String codigoPersona
	       							);
	
	/**
	 * listado X Todos
	 * @param con
	 * @param codigoCentroAtencion
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap listadoXTodos(	Connection con, 
	       							String codigoCentroAtencion,
	       							int codigoInstitucion
	       						);

}
