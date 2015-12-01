/*
 * @(#)PostgresqlConsultaLogCuposExtraDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import com.princetonsa.dao.ConsultaLogCuposExtraDao;
import com.princetonsa.dao.sqlbase.SqlBaseConsultaLogCuposExtraDao;

/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 09 /May/ 2006
 */
public class PostgresqlConsultaLogCuposExtraDao implements ConsultaLogCuposExtraDao 
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
	public HashMap consultarLogCuposExtra (Connection con, String fechaIncial, String fechaFinal, int codigoMedico, int codigoUnidadConsulta, int centroAtencion, String centrosAtencion, String unidadesAgenda) throws SQLException
	{
		return SqlBaseConsultaLogCuposExtraDao.consultarLogCuposExtra(con, fechaIncial, fechaFinal, codigoMedico, codigoUnidadConsulta, centroAtencion, centrosAtencion, unidadesAgenda);
	}
}