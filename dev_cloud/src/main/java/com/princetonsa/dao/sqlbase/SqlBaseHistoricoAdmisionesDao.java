package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.Collection;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a Historico Admisiones
 *
 *	@version 1.0, Abril 2, 2004
 */
public class SqlBaseHistoricoAdmisionesDao 
{
	/**
	 * Cadena constante con el <i>statement</i> necesario para consultar la lista de Admisiones hospitalarias
	 *  para un paciente en una institución.
	 */
	private static final String consultarAdmisionesHospitalariasPacienteStr = "SELECT "+ 
		"adh.codigo as codigo,' ' as anio, "+ 
		"to_char(adh.fecha_admision,'DD/MM/YYYY') as fechaAdmision, "+ 
		"adh.hora_admision as horaAdmision, "+ 
		"CASE WHEN egr.fecha_egreso IS NULL THEN '' ELSE to_char(egr.fecha_egreso,'DD/MM/YYYY') END as fechaEgreso, "+ 
		"CASE WHEN egr.hora_egreso IS NULL THEN '' ELSE egr.hora_egreso || '' END as horaEgreso, "+ 
		"CASE WHEN egr.usuario_responsable IS NULL THEN '' ELSE egr.usuario_responsable END as login, " +
		"getnomcentroatencion(cc.centro_atencion) AS centro_atencion, "+
		"cue.id AS cuenta "+ 
		"FROM admisiones_hospi adh "+ 
		"INNER JOIN cuentas cue ON(adh.cuenta=cue.id) "+
		"INNER JOIN centros_costo cc ON(cc.codigo=cue.area) "+
		"INNER JOIN ingresos ing ON(cue.id_ingreso=ing.id) "+ 
		"INNER JOIN personas per ON(per.codigo=ing.codigo_paciente) "+
		"LEFT OUTER JOIN egresos egr ON(egr.cuenta=cue.id) "+ 
		"WHERE per.numero_identificacion=? and per.tipo_identificacion=? and ing.institucion=? "+
		"order by adh.fecha_admision DESC,adh.hora_admision DESC";

	/**
	 * Cadena constante con el <i>statement</i> necesario para consultar la lista de Admisiones de urgencias
	 * NO ACTIVAS (que ya tienen egreso y la identificacion del medico es no nula, pues en este caso estaría en medio de una reversión) 
	 * para un paciente en una institución.
	 */
	private static final String consultarAdmisionesUrgenciasPacienteStr = "SELECT "+ 
		"adh.codigo as codigo, "+ 
		"adh.anio as anio, "+ 
		"to_char(adh.fecha_admision,'DD/MM/YYYY') as fechaAdmision, "+ 
		"adh.hora_admision as horaAdmision, "+ 
		"CASE WHEN egr.fecha_egreso IS NULL THEN '' ELSE to_char(egr.fecha_egreso,'DD/MM/YYYY') END as fechaEgreso, "+ 
		"CASE WHEN egr.hora_egreso IS NULL THEN '' ELSE egr.hora_egreso || '' END as horaEgreso, "+ 
		"CASE WHEN egr.usuario_responsable IS NULL THEN '' ELSE egr.usuario_responsable END as login, "+
		"getnomcentroatencion(cc.centro_atencion) AS centro_atencion, "+
		"cue.id AS cuenta "+
		"FROM admisiones_urgencias adh "+ 
		"INNER JOIN cuentas cue ON(adh.cuenta=cue.id) "+ 
		"INNER JOIN centros_costo cc ON(cc.codigo=cue.area) "+
		"INNER JOIN ingresos ing ON(cue.id_ingreso=ing.id) "+ 
		"INNER JOIN personas per ON(per.codigo=ing.codigo_paciente) "+
		"LEFT OUTER JOIN egresos egr ON(egr.cuenta=cue.id) "+ 
		"WHERE "+ 
		"per.numero_identificacion=? and per.tipo_identificacion=? and ing.institucion=? "+ 
		"order by adh.fecha_admision DESC,adh.hora_admision DESC"; 

	/**
	 * Implementación de cargar historico evoluciones dada la cuenta
	 * para BD PostgresSQL o Hsqldb
	 * 
	 * @see com.princetonsa.dao.HistoricoAdmisionesDao#cargar(Connection, int)
	 */
	public static Collection cargarAdmisionesHospitalarias(Connection con, String numeroIdPaciente, String tipoIdPaciente, int codigoInstitucion) throws SQLException 
	{
		PreparedStatementDecorator cargar =  new PreparedStatementDecorator(con.prepareStatement(consultarAdmisionesHospitalariasPacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cargar.setString(1, numeroIdPaciente);
		cargar.setString(2, tipoIdPaciente);
		cargar.setInt(3, codigoInstitucion);
		Collection listaAdmisionesHosp = UtilidadBD.resultSet2Collection(new ResultSetDecorator(cargar.executeQuery()));
		return listaAdmisionesHosp;
	}

	public static Collection cargarAdmisionesUrgencias(Connection con, String numeroIdPaciente, String tipoIdPaciente, int codigoInstitucion) throws SQLException 
	{
		
		PreparedStatementDecorator cargar =  new PreparedStatementDecorator(con.prepareStatement(consultarAdmisionesUrgenciasPacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cargar.setString(1, numeroIdPaciente);
		cargar.setString(2, tipoIdPaciente);
		cargar.setInt(3, codigoInstitucion);
		Collection listaAdmisionesUrg = UtilidadBD.resultSet2Collection(new ResultSetDecorator(cargar.executeQuery()));
		return listaAdmisionesUrg;

	}
	

		
}
