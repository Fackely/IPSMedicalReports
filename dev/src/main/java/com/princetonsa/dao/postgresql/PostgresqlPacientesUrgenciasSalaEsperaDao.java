/*
 * @(#)PostgresqlPacientesUrgenciasSalaEsperaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ValoresPorDefecto;

import com.princetonsa.dao.PacientesUrgenciasSalaEsperaDao;
import com.princetonsa.dao.sqlbase.SqlBasePacientesUrgenciasSalaEsperaDao;

/**
 * Implementación postgres de las funciones de acceso a la fuente de datos
 * para pacientes de urgencias con conducta a seguir "Sala de Espera"
 *
 * @version 1.0, Julio 24 / 2006
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 */
public class PostgresqlPacientesUrgenciasSalaEsperaDao implements PacientesUrgenciasSalaEsperaDao
{
	/**
	 * Cadena con el statement necesario para consultar todos los datos de los pacientes de urgencias con conducta a seguir "Sala de Espera"
	 */
	private static final String consultarPacientesUrgSalaEsperaStr ="SELECT " +
																	"to_char(current_timestamp - to_timestamp(au.fecha_admision||' '||au.hora_admision, 'YYYY-MM-DD HH24:MI'), 'DD HH24:MI') as tiemposalaespera, " +
																	"au.codigo as codigoadmision, " +
																	"au.fecha_admision as fechaadmision,  " +
																	"to_char(au.fecha_admision, 'dd/mm/yyyy') as fechaadmisionapp,  " +
																	"au.hora_admision as horaadmision, " +
																	"to_char(au.fecha_admision, 'DD/MM/YYYY') || ' - ' || substr(au.hora_admision, 1,5) as fechahoraadmision,  " +
																	"cue.codigo_paciente as codigopaciente,  " +
																	"cue.id as idcuenta,  " +
																	"getnombrepersona(cue.codigo_paciente) as nombrepaciente,  " +
																	"per.tipo_identificacion as tipoid,  " +
																	"per.numero_identificacion as numeroid,  " +
																	"getDescripcionUltimConducta(cue.id, valur.codigo_conducta_valoracion) as descripcion,  " +
																	"case when ct.nombre is null then ''  else ct.nombre end as calificaciontriage,  " +
																	"case when colt.nombre is null then '' else colt.nombre end as nombrecolor, " +
																	"getNombrePersona(valora.codigo_medico) AS nombremedico " +
																 "FROM admisiones_urgencias au  " +
																	"INNER JOIN cuentas cue ON (au.cuenta = cue.id)  " +
																	"INNER JOIN ingresos ing ON (cue.id_ingreso=ing.id)  " +
																	"INNER JOIN solicitudes sol ON (cue.id=sol.cuenta)  " +
																	"INNER JOIN centros_costo cc ON (cue.area=cc.codigo)  " +
																	"INNER JOIN valoraciones_urgencias valur ON (sol.numero_solicitud=valur.numero_solicitud)  " +
																	"INNER JOIN valoraciones valora ON (valora.numero_solicitud=valur.numero_solicitud)  " +
																	"INNER JOIN personas per ON (cue.codigo_paciente=per.codigo)  " +
																	"LEFT OUTER JOIN triage t ON (t.consecutivo=au.consecutivo_triage AND t.consecutivo_fecha=au.consecutivo_triage_fecha)  " +
																	"LEFT OUTER JOIN categorias_triage ct ON (t.categoria_triage=ct.consecutivo)  " +
																	"LEFT OUTER JOIN colores_triage colt ON (ct.color=colt.codigo)  " +
																"WHERE  au.origen_admision_urgencias = " + ConstantesBD.codigoOrigenAdmisionHospitalariaEsUrgencias + 
																	" AND cue.via_ingreso = " + ConstantesBD.codigoViaIngresoUrgencias +
																	" AND cue.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaActiva +
																	" AND cc.centro_atencion=?  " +
																	" AND cc.institucion=?   " +
																	" AND ing.estado= '"+ConstantesIntegridadDominio.acronimoEstadoAbierto + "' "  +
																"UNION "+
																	"SELECT  " +
																	"to_char(current_timestamp - to_timestamp(au.fecha_admision||' '||au.hora_admision, 'YYYY-MM-DD HH24:MI'), 'DD HH24:MI') as tiemposalaespera, " +
																	"au.codigo as codigoadmision, " +
																	"au.fecha_admision as fechaadmision,  " +
																	"to_char(au.fecha_admision, 'dd/mm/yyyy') as fechaadmisionapp,  " +
																	"au.hora_admision as horaadmision, " +
																	"to_char(au.fecha_admision, 'DD/MM/YYYY') || ' - ' || substr(au.hora_admision, 1,5) as fechahoraadmision,  " +
																	"cue.codigo_paciente as codigopaciente,  cue.id as idcuenta,  " +
																	"getnombrepersona(cue.codigo_paciente) as nombrepaciente,  " +
																	"per.tipo_identificacion as tipoid,  " +
																	"per.numero_identificacion as numeroid,  " +
																	"getDescripcionUltimConducta(cue.id, valur.codigo_conducta_valoracion) as descripcion,  " +
																	"case when ct.nombre is null then ''  else ct.nombre end as calificaciontriage,  " +
																	"case when colt.nombre is null then '' else colt.nombre end as nombrecolor, " +
																	"getNombrePersona(valora.codigo_medico) AS nombremedico " +
																 "FROM admisiones_urgencias au  " +
																	"INNER JOIN cuentas cue ON (au.cuenta = cue.id)  " +
																	"INNER JOIN asocios_cuenta asocue ON (asocue.cuenta_inicial = cue.id  AND asocue.usuario_desasocio is null )  " +
																	"INNER JOIN ingresos ing ON (cue.id_ingreso=ing.id)  " +
																	"INNER JOIN solicitudes sol ON (cue.id=sol.cuenta)  " +
																	"INNER JOIN centros_costo cc ON (cue.area=cc.codigo)  " +
																	"INNER JOIN valoraciones_urgencias valur ON (sol.numero_solicitud=valur.numero_solicitud)  " +
																	"INNER JOIN valoraciones valora ON (valora.numero_solicitud=valur.numero_solicitud)  " +
																	"INNER JOIN personas per ON (cue.codigo_paciente=per.codigo)  " +
																	"LEFT OUTER JOIN triage t ON (t.consecutivo=au.consecutivo_triage AND t.consecutivo_fecha=au.consecutivo_triage_fecha)  " +
																	"LEFT OUTER JOIN categorias_triage ct ON (t.categoria_triage=ct.consecutivo)  " +
																	"LEFT OUTER JOIN colores_triage colt ON (ct.color=colt.codigo)  " +
																 "WHERE  " +
																 	 " au.origen_admision_urgencias = " + ConstantesBD.codigoOrigenAdmisionHospitalariaEsUrgencias +
																 	 " AND cue.via_ingreso = " + ConstantesBD.codigoViaIngresoUrgencias +
																	 " AND cue.estado_cuenta = " + ConstantesBD.codigoEstadoCuentaAsociada  +
																	 " AND cc.centro_atencion=?  " +
																	 " AND cc.institucion=?   " +
																	 " AND ing.estado= '"+ConstantesIntegridadDominio.acronimoEstadoAbierto + "' " + 
																	 " AND asocue.cuenta_final is null " +
																	 " AND asocue.activo='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' ";
	
	private static final String consultarPacientesUrgSalaEsperaOrdenamientoStr ="SELECT " +
	"current_timestamp - to_timestamp(au.fecha_admision||' '||au.hora_admision, 'YYYY-MM-DD HH24:MI') as tiemposalaespera, " +
	"au.codigo as codigoadmision, " +
	"au.fecha_admision as fechaadmision,  " +
	"to_char(au.fecha_admision, 'dd/mm/yyyy') as fechaadmisionapp,  " +
	"au.hora_admision as horaadmision, " +
	"to_char(au.fecha_admision, 'DD/MM/YYYY') || ' - ' || substr(au.hora_admision, 1,5) as fechahoraadmision,  " +
	"cue.codigo_paciente as codigopaciente,  " +
	"cue.id as idcuenta,  " +
	"getnombrepersona(cue.codigo_paciente) as nombrepaciente,  " +
	"per.tipo_identificacion as tipoid,  " +
	"per.numero_identificacion as numeroid,  " +
	"getDescripcionUltimConducta(cue.id, valur.codigo_conducta_valoracion) as descripcion,  " +
	"case when ct.nombre is null then ''  else ct.nombre end as calificaciontriage,  " +
	"case when colt.nombre is null then '' else colt.nombre end as nombrecolor, " +
	"getNombrePersona(valora.codigo_medico) AS nombremedico " +
	"FROM admisiones_urgencias au  " +
	"INNER JOIN cuentas cue ON (au.cuenta = cue.id)  " +
	"INNER JOIN ingresos ing ON (cue.id_ingreso=ing.id)  " +
	"INNER JOIN solicitudes sol ON (cue.id=sol.cuenta)  " +
	"INNER JOIN centros_costo cc ON (cue.area=cc.codigo)  " +
	"INNER JOIN valoraciones_urgencias valur ON (sol.numero_solicitud=valur.numero_solicitud)  " +
	"INNER JOIN valoraciones valora ON (valora.numero_solicitud=valur.numero_solicitud)  " +
	"INNER JOIN personas per ON (cue.codigo_paciente=per.codigo)  " +
	"LEFT OUTER JOIN triage t ON (t.consecutivo=au.consecutivo_triage AND t.consecutivo_fecha=au.consecutivo_triage_fecha)  " +
	"LEFT OUTER JOIN categorias_triage ct ON (t.categoria_triage=ct.consecutivo)  " +
	"LEFT OUTER JOIN colores_triage colt ON (ct.color=colt.codigo)  " +
	"WHERE  au.origen_admision_urgencias = " + ConstantesBD.codigoOrigenAdmisionHospitalariaEsUrgencias + 
	" AND cue.via_ingreso = " + ConstantesBD.codigoViaIngresoUrgencias +
	" AND cue.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaActiva +
	" AND cc.centro_atencion=?  " +
	" AND cc.institucion=?   " +
	" AND ing.estado= '"+ConstantesIntegridadDominio.acronimoEstadoAbierto + "' "  +
	"UNION "+
	"SELECT  " +
	"current_timestamp - to_timestamp(au.fecha_admision||' '||au.hora_admision, 'YYYY-MM-DD HH24:MI') as tiemposalaespera, " +
	"au.codigo as codigoadmision, " +
	"au.fecha_admision as fechaadmision,  " +
	"to_char(au.fecha_admision, 'dd/mm/yyyy') as fechaadmisionapp,  " +
	"au.hora_admision as horaadmision, " +
	"to_char(au.fecha_admision, 'DD/MM/YYYY') || ' - ' || substr(au.hora_admision, 1,5) as fechahoraadmision,  " +
	"cue.codigo_paciente as codigopaciente,  cue.id as idcuenta,  " +
	"getnombrepersona(cue.codigo_paciente) as nombrepaciente,  " +
	"per.tipo_identificacion as tipoid,  " +
	"per.numero_identificacion as numeroid,  " +
	"getDescripcionUltimConducta(cue.id, valur.codigo_conducta_valoracion) as descripcion,  " +
	"case when ct.nombre is null then ''  else ct.nombre end as calificaciontriage,  " +
	"case when colt.nombre is null then '' else colt.nombre end as nombrecolor, " +
	"getNombrePersona(valora.codigo_medico) AS nombremedico " +
	"FROM admisiones_urgencias au  " +
	"INNER JOIN cuentas cue ON (au.cuenta = cue.id)  " +
	"INNER JOIN asocios_cuenta asocue ON (asocue.cuenta_inicial = cue.id  AND asocue.usuario_desasocio is null )  " +
	"INNER JOIN ingresos ing ON (cue.id_ingreso=ing.id)  " +
	"INNER JOIN solicitudes sol ON (cue.id=sol.cuenta)  " +
	"INNER JOIN centros_costo cc ON (cue.area=cc.codigo)  " +
	"INNER JOIN valoraciones_urgencias valur ON (sol.numero_solicitud=valur.numero_solicitud)  " +
	"INNER JOIN valoraciones valora ON (valora.numero_solicitud=valur.numero_solicitud)  " +
	"INNER JOIN personas per ON (cue.codigo_paciente=per.codigo)  " +
	"LEFT OUTER JOIN triage t ON (t.consecutivo=au.consecutivo_triage AND t.consecutivo_fecha=au.consecutivo_triage_fecha)  " +
	"LEFT OUTER JOIN categorias_triage ct ON (t.categoria_triage=ct.consecutivo)  " +
	"LEFT OUTER JOIN colores_triage colt ON (ct.color=colt.codigo)  " +
	"WHERE  " +
	" au.origen_admision_urgencias = " + ConstantesBD.codigoOrigenAdmisionHospitalariaEsUrgencias +
	" AND cue.via_ingreso = " + ConstantesBD.codigoViaIngresoUrgencias +
	" AND cue.estado_cuenta = " + ConstantesBD.codigoEstadoCuentaAsociada  +
	" AND cc.centro_atencion=?  " +
	" AND cc.institucion=?   " +
	" AND ing.estado= '"+ConstantesIntegridadDominio.acronimoEstadoAbierto + "' " + 
	" AND asocue.cuenta_final is null " +
	" AND asocue.activo='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"'" +
	"  ";
	
	/**
	 * Método para consultar los todos los datos de los pacientes de via de ingreso de
	 * urgencias con conducta a seguir "Sala de Espera"
	 * @param con
	 * @param codigoCentroAtencion
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultarPacientesUrgSalaEspera(Connection con, int codigoCentroAtencion, int institucion) throws SQLException
	{
		return SqlBasePacientesUrgenciasSalaEsperaDao.consultarPacientesUrgSalaEspera(con, codigoCentroAtencion, institucion, this.consultarPacientesUrgSalaEsperaStr);
	}
	
	/**
	 * @see com.princetonsa.dao.PacientesUrgenciasSalaEsperaDao#consultarPacientesUrgSalaEsperaOrdenamientoPorTiempoSalaEspera(java.sql.Connection, int, int, java.lang.Integer)
	 */
	public ResultSetDecorator consultarPacientesUrgSalaEsperaOrdenamientoPorTiempoSalaEspera(Connection con, int codigoCentroAtencion, int institucion,Integer orderBy) throws SQLException
	{
		return SqlBasePacientesUrgenciasSalaEsperaDao.consultarPacientesUrgSalaEsperaOrdenamientoPorTiempoSalaEspera(con, codigoCentroAtencion, institucion, this.consultarPacientesUrgSalaEsperaOrdenamientoStr,orderBy);
	}
}
