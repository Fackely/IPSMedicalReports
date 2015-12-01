/*
 * @(#)OracleCuentasProcesoFacturacionDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2006. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2
 *
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.CuentasProcesoFacturacionDao;
import com.princetonsa.dao.sqlbase.SqlBaseCuentasProcesoFacturacionDao;

/**
 * Implementación oracle de las funciones de acceso a la fuente de datos
 * para cuentas en proceso de facturacion
 *
 * @version 1.0, Julio 06 / 2006
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class OracleCuentasProcesoFacturacionDao implements CuentasProcesoFacturacionDao
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
	       							)
	{
		return SqlBaseCuentasProcesoFacturacionDao.listadoXPaciente(con, codigoCentroAtencion, codigoInstitucion, codigoPersona);
	}
	
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
	       						)
	{
		return SqlBaseCuentasProcesoFacturacionDao.listadoXTodos(con, codigoCentroAtencion, codigoInstitucion);
	}

}
