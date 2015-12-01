/*
 * @(#)ConsultaLogCuposExtraDao.java
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
 *	@version 1.0, 09 /May/ 2006
 */
public interface ConsultaLogCuposExtraDao 
{
	/**
	 * M�todo para consultar el LOG de cupos extras segun los parametros de busqueda definidos
	 * @param con
	 * @param fechaIncial
	 * @param fechaFinal
	 * @param codigoMedico
	 * @param codigoUnidadConsulta
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarLogCuposExtra (Connection con, String fechaIncial, String fechaFinal, int codigoMedico, int codigoUnidadConsulta, int centroAtencion, String centrosAtencion, String unidadesAgenda) throws SQLException;
}