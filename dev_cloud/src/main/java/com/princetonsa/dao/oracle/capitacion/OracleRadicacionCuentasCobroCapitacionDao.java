/*
 * @(#)OracleRadicacionCuentasCobroCapitacionDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.oracle.capitacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import com.princetonsa.dao.capitacion.RadicacionCuentasCobroCapitacionDao;
import com.princetonsa.dao.sqlbase.capitacion.SqlBaseRadicacionCuentasCobroCapitacionDao;

/**
 * Implementación Oracle de las funciones de acceso a la fuente de datos
 * para radicacion cuentas cobro capitacion
 *
 * @version 1.0, Julio 04 / 2006
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class OracleRadicacionCuentasCobroCapitacionDao implements RadicacionCuentasCobroCapitacionDao
{

	/**
	 * Busqueda de las cuentas cobro a radicar
	 * @param con
	 * @param criteriosBusquedaMap ( keys= cuentaCobro, codigoConvenio, fechaInicial, fechaFinal), 
	 * 								las fechas deben estar en formato aplicacion
	 * @return
	 * @throws SQLException
	 */
	public Collection busquedaCuentasCobroARadicar(	Connection con,
													HashMap criteriosBusquedaMap
												 ) throws SQLException
	{
		return SqlBaseRadicacionCuentasCobroCapitacionDao.busquedaCuentasCobroARadicar(con, criteriosBusquedaMap);
	}
	
	/**
	 * metodo que inserta la radicacion de cuentas cobro capitacion
	 * @param fechaRadicacionFormatApp
	 * @param numeroRadicacion
	 * @param loginUsuario
	 * @param observaciones
	 * @param institucion
	 * @return
	 */
	public boolean insertarRadicacionCxC (	Connection con, String fechaRadicacionFormatApp, 
											String numeroRadicacion, String loginUsuario, 
											String observaciones, String numeroCuentaCobro, 
											int institucion)
	{
		return SqlBaseRadicacionCuentasCobroCapitacionDao.insertarRadicacionCxC(con, fechaRadicacionFormatApp, numeroRadicacion, loginUsuario, observaciones, numeroCuentaCobro, institucion);
	}
	

}
