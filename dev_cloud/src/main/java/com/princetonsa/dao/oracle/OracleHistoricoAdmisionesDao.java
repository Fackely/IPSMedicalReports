/*
 * @(#)OracleHistoricoAdmisionesDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import com.princetonsa.dao.HistoricoAdmisionesDao;
import com.princetonsa.dao.sqlbase.SqlBaseHistoricoAdmisionesDao;

/**
 * Esta clase implementa <code>HistoricoAdmisionesDao</code>
 * y proporciona acceso a BD para <code>HistoricoAdmisiones</code>.
 *
 * @version Ago 22, 2003
 * @author 	<a href="mailto:Sandra@PrincetonSA.com">Sandra Moya</a>
 */

public class OracleHistoricoAdmisionesDao implements HistoricoAdmisionesDao 
{
	/**
	 * Implementación de cargar historico evoluciones dada la cuenta
	 * para BD Oracle
	 * 
	 * @see com.princetonsa.dao.HistoricoAdmisionesDao#cargar(Connection, int)
	 */
	public Collection cargarAdmisionesHospitalarias(Connection con, String numeroIdPaciente, String tipoIdPaciente, int codigoInstitucion) throws SQLException 
	{
		return SqlBaseHistoricoAdmisionesDao.cargarAdmisionesHospitalarias(con,numeroIdPaciente,tipoIdPaciente,codigoInstitucion);
	}
	
	public Collection cargarAdmisionesUrgencias(Connection con, String numeroIdPaciente, String tipoIdPaciente, int codigoInstitucion) throws SQLException 
	{
		return SqlBaseHistoricoAdmisionesDao.cargarAdmisionesUrgencias(con,numeroIdPaciente,tipoIdPaciente,codigoInstitucion);
	}
}