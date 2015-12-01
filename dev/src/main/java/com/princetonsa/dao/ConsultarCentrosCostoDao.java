/*
 * @(#)ConsultarCentrosCostoDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 11 /May/ 2006
 */
public interface ConsultarCentrosCostoDao 
{

	/**
	 * M�todo para realizar la busqueda avanzada de los centros de costo segun los
	 * parametros ingresados
	 * @param con
	 * @param codCentroAtencion
	 * @param identificador
	 * @param descripcion
	 * @param codigoTipoArea
	 * @param manejoCamas
	 * @param acronimoUnidadFuncional
	 * @param activo
	 * @param tipoEntidad 
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarCentrosCosto(Connection con, int codCentroAtencion, String identificador, String descripcion, int codigoTipoArea, String manejoCamas, String acronimoUnidadFuncional, String codigo_interfaz, String activo, String tipoEntidad) throws SQLException;
	
}