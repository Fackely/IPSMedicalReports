/*
 * @(#)SqlBasePacientesUrgenciasPorHospitalizarDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.ValoresPorDefecto;

/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 * para la los pacientes de urgencias por hospitalizar
 *
 * @version 1.0, Julio 25 / 2006
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 */
public class SqlBasePacientesUrgenciasPorHospitalizarDao
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBasePacientesUrgenciasPorHospitalizarDao.class);
	
	/**
	 * Cadena con el statement necesario para consultar todos los datos de los pacientes de urgencias o Hospitalizacion - Cirugia Ambulatoria 
	 * pendientes por Hospitalizar 
	 */
	private static final String consultarPacientesUrgPorHospitalizarStr = 
		"SELECT "+ 
		"e.destino_salida || '"+ConstantesBD.separadorSplit+"evol' AS destino," +
		"cue.id as idcuenta, "+ 
		"cue.codigo_paciente as codigopaciente, "+ 
		"au.codigo as codigoadmision, "+  
		"per.tipo_identificacion as tipoid, "+  
		"per.numero_identificacion as numeroid, per.sexo as codigosexo, "+ 
		"to_char(per.fecha_nacimiento,'yyyy-mm-dd') as fechanacimiento, "+ 
		"per.primer_apellido || CASE WHEN per.segundo_apellido IS NULL THEN ' ' ELSE ' ' || per.segundo_apellido END || "+ 
		"per.primer_nombre || CASE WHEN per.segundo_nombre IS NULL THEN ' ' ELSE ' ' || per.segundo_nombre END AS nombrepaciente, "+
		"getnombretipoidentificacion(per.tipo_identificacion) as nombretipoid, "+ 
		"s.nombre as nombresexo, "+
		"to_char(au.fecha_admision,'yyyy-mm-dd') as fechaadmision, "+ 
		"au.hora_admision as horaadmision, "+ 
		"to_char(au.fecha_admision, '"+ConstantesBD.formatoFechaAp+"')||' - '||au.hora_admision as fechahoraadmision, "+  
		"case when e.fecha_egreso is null then to_char(sol.fecha_solicitud,'yyyy-mm-dd') else to_char(e.fecha_egreso,'yyyy-mm-dd') end as fechaegreso, "+
		"case when e.hora_egreso is null then sol.hora_solicitud else e.hora_egreso end as horaegreso, "+ 
		"getnombrediagnostico(e.diagnostico_principal,e.diagnostico_principal_cie) as diagnostico, "+ 
		"case when e.codigo_medico is null or e.codigo_medico=0 then getnombrepersona(sol.codigo_medico_responde) else getnombrepersona(e.codigo_medico) end as medico, " +
		"CASE WHEN  ingr.pac_entidades_subcontratadas IS NULL THEN 'N' ELSE 'S' END AS espacentsubcontratadas "+ 
		"FROM ingresos ingr " +
		"inner join cuentas cue on(cue.id_ingreso=ingr.id) "+ 
		"INNER JOIN personas per ON(per.codigo=cue.codigo_paciente) "+ 
		"INNER JOIN sexo s ON(s.codigo=per.sexo) "+ 
		"INNER JOIN admisiones_urgencias au ON(au.cuenta=cue.id) "+ 
		"INNER JOIN egresos e ON(e.cuenta=au.cuenta and e.evolucion IS NOT NULL and e.destino_salida IN  ("+ConstantesBD.codigoDestinoSalidaHospitalizacion+","+ConstantesBD.codigoDestinoSalidaCirugiaAmbulatoria+","+ConstantesBD.codigoDestinoSalidaTrasladoCuidadoEspecial+") ) "+ 
		"INNER JOIN solicitudes sol ON(sol.cuenta=e.cuenta AND sol.tipo IN("+ConstantesBD.codigoTipoSolicitudInicialUrgencias+")) "+  
		"INNER JOIN centros_costo cc ON(cc.codigo=sol.centro_costo_solicitado AND cc.centro_atencion = ? AND cc.institucion = ?) "+ 
		"WHERE "+ 
		"cue.id NOT IN (SELECT cuenta_inicial FROM asocios_cuenta WHERE cuenta_final IS NOT NULL and activo="+ValoresPorDefecto.getValorTrueParaConsultas()+") AND "+ 
		"cue.estado_cuenta NOT IN ("+ConstantesBD.codigoEstadoCuentaCerrada+","+ConstantesBD.codigoEstadoCuentaFacturada+","+ConstantesBD.codigoEstadoCuentaProcesoFacturacion+","+ConstantesBD.codigoEstadoCuentaProcesoDistribucion+") " +		
		"UNION "+
		"SELECT " +
		"valur.codigo_conducta_valoracion  || '"+ConstantesBD.separadorSplit+"valo' AS destino, "+ 
		"cue.id as idcuenta, "+ 
		"cue.codigo_paciente as codigopaciente, "+ 
		"au.codigo as codigoadmision, "+  
		"per.tipo_identificacion as tipoid, "+  
		"per.numero_identificacion as numeroid, per.sexo as codigosexo, "+ 
		"to_char(per.fecha_nacimiento,'yyyy-mm-dd') as fechanacimiento, "+ 
		"per.primer_apellido || CASE WHEN per.segundo_apellido IS NULL THEN ' ' ELSE ' ' || per.segundo_apellido END || "+ 
		"per.primer_nombre || CASE WHEN per.segundo_nombre IS NULL THEN ' ' ELSE ' ' || per.segundo_nombre END AS nombrepaciente, "+
		"getnombretipoidentificacion(per.tipo_identificacion) as nombretipoid, "+ 
		"s.nombre as nombresexo, "+
		"to_char(au.fecha_admision,'yyyy-mm-dd') as fechaadmision, "+ 
		"au.hora_admision as horaadmision, "+ 
		"to_char(au.fecha_admision, '"+ConstantesBD.formatoFechaAp+"')||' - '||au.hora_admision as fechahoraadmision, "+  
		"case when e.fecha_egreso is null then to_char(sol.fecha_solicitud,'yyyy-mm-dd') else to_char(e.fecha_egreso,'yyyy-mm-dd') end as fechaegreso, "+
		"case when e.hora_egreso is null then sol.hora_solicitud else e.hora_egreso end as horaegreso, "+ 
		"getnombrediagnostico(e.diagnostico_principal,e.diagnostico_principal_cie) as diagnostico, "+ 
		"case when e.codigo_medico is null or e.codigo_medico=0 then getnombrepersona(sol.codigo_medico_responde) else getnombrepersona(e.codigo_medico) end as medico," +
		"CASE WHEN  ingr.pac_entidades_subcontratadas IS NULL THEN 'N' ELSE 'S' END AS espacentsubcontratadas "+ 
		"FROM ingresos ingr " +
		"inner join cuentas cue on(cue.id_ingreso=ingr.id) "+ 
		"INNER JOIN personas per ON(per.codigo=cue.codigo_paciente) "+ 
		"INNER JOIN sexo s ON(s.codigo=per.sexo) "+ 
		"INNER JOIN admisiones_urgencias au ON(au.cuenta=cue.id) "+ 
		"INNER JOIN egresos e ON(e.cuenta=au.cuenta) "+ 
		"INNER JOIN solicitudes sol ON(sol.cuenta=e.cuenta) "+  
		"INNER JOIN valoraciones_urgencias valur ON(valur.numero_solicitud=sol.numero_solicitud " +
		"AND valur.codigo_conducta_valoracion IN ("+ConstantesBD.codigoConductaSeguirHospitalizarPiso+","+ConstantesBD.codigoConductaSeguirSalaCirugiaAmbulatoria+","+ConstantesBD.codigoConductaSeguirTrasladoCuidadoEspecial+")  ) "+ 
		"INNER JOIN centros_costo cc ON(cc.codigo=sol.centro_costo_solicitado AND cc.centro_atencion = ? AND cc.institucion = ?) "+ 
		"WHERE "+ 
		"cue.id NOT IN (SELECT cuenta_inicial FROM asocios_cuenta WHERE cuenta_final IS NOT NULL) AND "+ 
		"cue.estado_cuenta NOT IN ("+ConstantesBD.codigoEstadoCuentaCerrada+","+ConstantesBD.codigoEstadoCuentaFacturada+","+ConstantesBD.codigoEstadoCuentaProcesoFacturacion+","+ConstantesBD.codigoEstadoCuentaProcesoDistribucion+") " +		
		"UNION "+	
		"SELECT "+ 
		"e.destino_salida  || '"+ConstantesBD.separadorSplit+"evol' AS destino," +		
		"cue.id as idcuenta, "+		
		"cue.codigo_paciente as codigopaciente, "+	
		"ah.codigo as codigoadmision, "+		
		"per.tipo_identificacion as tipoid, "+  
		"per.numero_identificacion as numeroid, per.sexo as codigosexo, "+ 
		"to_char(per.fecha_nacimiento,'yyyy-mm-dd') as fechanacimiento, "+ 
		"per.primer_apellido || CASE WHEN per.segundo_apellido IS NULL THEN ' ' ELSE ' ' || per.segundo_apellido END || "+ 
			"per.primer_nombre || CASE WHEN per.segundo_nombre IS NULL THEN ' ' ELSE ' ' || per.segundo_nombre END AS nombrepaciente, "+
		"getnombretipoidentificacion(per.tipo_identificacion) as nombretipoid, "+ 
		"s.nombre as nombresexo, "+		
		"to_char(ah.fecha_admision,'yyyy-mm-dd') as fechaadmision, "+		
		"ah.hora_admision as horaadmision, "+ 		
		"to_char(ah.fecha_admision, '"+ConstantesBD.formatoFechaAp+"')||' - '||ah.hora_admision as fechahoraadmision, "+		
		"case when e.fecha_egreso is null then to_char(sol.fecha_solicitud,'yyyy-mm-dd') else to_char(e.fecha_egreso,'yyyy-mm-dd') end as fechaegreso, "+		
		"case when e.hora_egreso is null then sol.hora_solicitud else e.hora_egreso end as horaegreso, "+		
		"getnombrediagnostico(e.diagnostico_principal,e.diagnostico_principal_cie) as diagnostico, "+ 
		"case when e.codigo_medico is null or e.codigo_medico=0 then getnombrepersona(sol.codigo_medico_responde) else getnombrepersona(e.codigo_medico) end as medico, " +
		"CASE WHEN  ingr.pac_entidades_subcontratadas IS NULL THEN 'N' ELSE 'S' END AS espacentsubcontratadas "+ 
		"FROM ingresos ingr " +
		"inner join cuentas cue on(cue.id_ingreso=ingr.id) "+ 
		"INNER JOIN personas per ON(per.codigo=cue.codigo_paciente) "+ 
		"INNER JOIN sexo s ON(s.codigo=per.sexo) "+ 
		"INNER JOIN admisiones_hospi ah ON(ah.cuenta=cue.id) "+ 
		"INNER JOIN egresos e ON(e.cuenta=ah.cuenta and e.destino_salida in ("+ConstantesBD.codigoDestinoSalidaHospitalizar+","+ConstantesBD.codigoDestinoSalidaTrasladoCuidadoEspecial+") ) "+ 
		"INNER JOIN solicitudes sol ON(sol.cuenta=e.cuenta AND sol.tipo IN("+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+")) "+  
		"INNER JOIN centros_costo cc ON(cc.codigo=sol.centro_costo_solicitado AND cc.centro_atencion = ? AND cc.institucion = ?) "+ 
		"WHERE "+ 
		"cue.id NOT IN (SELECT cuenta_inicial FROM asocios_cuenta WHERE cuenta_final IS NOT NULL and activo="+ValoresPorDefecto.getValorTrueParaConsultas()+") AND "+ 
		"cue.estado_cuenta NOT IN ("+ConstantesBD.codigoEstadoCuentaCerrada+","+ConstantesBD.codigoEstadoCuentaFacturada+","+ConstantesBD.codigoEstadoCuentaProcesoFacturacion+","+ConstantesBD.codigoEstadoCuentaProcesoDistribucion+") " +
		"AND cue.via_ingreso = "+ConstantesBD.codigoViaIngresoHospitalizacion +" AND cue.tipo_paciente = '"+ConstantesBD.tipoPacienteCirugiaAmbulatoria+"' ";	
				
	
	/**
	 * Método para consultar los todos los datos de los pacientes de via de ingreso de
	 * urgencias pendientes por Hospitalizar
	 * @param con
	 * @param codigoCentroAtencion
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public static ResultSetDecorator consultarPacientesUrgPorHospitalizar(Connection con, int codigoCentroAtencion, int institucion) throws SQLException
	{
		try
		{
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consultarPacientesUrgPorHospitalizarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("--->"+consultarPacientesUrgPorHospitalizarStr);
			logger.info("ccAtencion-->"+codigoCentroAtencion);
			logger.info("inst-->"+institucion);
			
			cargarStatement.setInt(1, codigoCentroAtencion);
			cargarStatement.setInt(2, institucion);
			cargarStatement.setInt(3, codigoCentroAtencion);
			cargarStatement.setInt(4, institucion);
			cargarStatement.setInt(5, codigoCentroAtencion);
			cargarStatement.setInt(6, institucion);
			
			return new ResultSetDecorator(cargarStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en consultarPacientesUrgPorHospitalizar [SqlBasePacientesUrgenciasPorHospitalizarDao]: "+e.toString());
			return null;
		}
	}
	
}

