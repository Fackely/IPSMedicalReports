package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.ConstantesBDFacturacion;
import util.manejoPaciente.ConstantesBDManejoPaciente;
import util.manejoPaciente.UtilidadesManejoPaciente;

/**
 * 
 * @author lgchavez@princetonsa.com
 *
 */
public class SqlBaseGeneracionArchivoPlanoIndicadoresCalidadDao {
	
	// --------------- ATRIBUTOS
	
	/**
	 * Objeto para manejar log de la clase  
	 * */
	private static Logger logger = Logger.getLogger(SqlBaseGeneracionArchivoPlanoIndicadoresCalidadDao.class);

	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public static String consultarUltimoPath(Connection con)
	{
		String consulta="select max(codigo) as codigo,path_archivo as path from log_arch_ind_calidad group by(path_archivo) order by codigo desc";
		HashMap resultado=new HashMap();
		resultado.put("path_0", "");
		
		try{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			logger.info("\n\n\n\n>>Consulta<<"+consulta+"\n\n\n\n");
		
		}
		catch (SQLException e) {
			logger.info("Error en la consulta postular path " +
								" "+e+" \n\n<Consulta>"+consulta);
		}
		if (Utilidades.convertirAEntero(resultado.get("numRegistros")+"")>0)
			return resultado.get("path_0").toString();
		else
			return "";
	}
	
	
	
	
	
	/**
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static HashMap consultarDatosEmpresaInstitucion(Connection con, HashMap mapa)
	{
		HashMap resultado=new HashMap();
		String consulta="Select " +
							"	codigo, " +
							"	cod_min_salud, " +
							"	nit, " +
							"	digito_verificacion," +
							"	CASE WHEN institucion_publica='"+ConstantesBD.acronimoSi+"' then '071' ELSE '062' END as tipoins " +
							"from " +
							"	empresas_institucion " +
							"where " +
							"	codigo="+mapa.get("codinstitucion");
		
		try{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			logger.info("\n\n\n\n>>Consulta<<"+consulta+"\n\n\n\n");
		
		}
		catch (SQLException e) {
			logger.info("Error en la consulta datos isntitucion " +
								" "+e+" \n\n<Consulta>"+consulta);
		}
		return resultado;
	}
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static HashMap consultarDatosInstitucion(Connection con, HashMap mapa)
	{
		HashMap resultado=new HashMap();
		String consulta="Select " +
							"	codigo, " +
							"	cod_min_salud, " +
							"	nit, " +
							"	digito_verificacion, " +
							"	CASE WHEN institucion_publica='"+ConstantesBD.acronimoSi+"' then '071' ELSE '062' END as tipoins " +
							"from " +
							"	instituciones " +
							"where " +
							"	codigo="+mapa.get("codinstitucion");
		
		try{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			logger.info("\n\n\n\n>>Consulta<<"+consulta+"\n\n\n\n");
		
		}
		catch (SQLException e) {
			logger.info("Error en la consulta datos isntitucion " +
								" "+e+" \n\n<Consulta>"+consulta);
		}
		return resultado;
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static HashMap consultarOportunidadAsignacionCitasConsulta(Connection con, HashMap mapa)
	{
		HashMap resultado=new HashMap();
		
		String consulta=" SELECT " +
							"	coalesce(SUM(c.fecha_gen-c.fecha_solicitada),0)||'00' AS numerador, " +
							"	count(*)||'00' as denominador, "+
							"	icc.acronimo as especialidad " +
							" FROM " +
							"	cita c " +
							" 		INNER JOIN servicios_cita sc on (c.codigo=sc.codigo_cita) " +
							"		INNER JOIN ind_calidad_especialidad ice on (sc.especialidad=ice.especialidad) " +
							"		INNER JOIN ind_calidad_codigos icc on (ice.ind_calidad_codigo=icc.codigo) " +
							"   WHERE " +
							"		1=1	"+
							" 		AND c.fecha_gen between "+mapa.get("periodo").toString()+" " +
							"  GROUP BY " +
							"	icc.acronimo " +
							"  ORDER BY " +
							"	especialidad ";	
		
		try{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			logger.info("\n\n\n\n>>Consulta<<"+consulta+"\n\n\n\n");
		
		}
		catch (SQLException e) {
			logger.info("Error en la consulta de indicadores parametro " +
								"oportunidad de la asignacion de citas en la consulta Medica general "+e+" \n\n<Consulta>"+consulta);
		}
		
		
		return resultado;
	}
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static HashMap consultaProporcionCancelacionCirugiaProgramada(Connection con, HashMap mapa)
	{
		HashMap resultado=new HashMap();
		String numerador="";
		String denominador="";
		String consulta="SELECT " +
								" count(*)||'00' as numerador " +
							" FROM " +
							"	cancelacion_prog_salas_qx " +
							" WHERE " +
							"	fecha_cancelacion between "+mapa.get("periodo").toString()+" ";	
		
		try{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			logger.info("\n\n\n\n>>Consulta<<"+consulta+"\n\n\n\n");
		}
		catch (SQLException e) {
			logger.info("Error en la consulta de indicadores parametro " +
								"Proporcion de cancelacion de cirugia programada numerador "+e+" \n\n<Consulta>"+consulta);
		}
		numerador=resultado.get("numerador_0").toString();
		
		consulta="SELECT " +
						" count(*)||'00' as denominador " +
					" FROM " +
					"	 programacion_salas_qx psq inner join peticion_qx pq ON (psq.peticion=pq.codigo)" +
					" WHERE " +
					"	fecha_programacion between "+mapa.get("periodo").toString()+" " +
					"	AND pq.estado_peticion IN ("+ConstantesBD.codigoEstadoPeticionProgramada+","+ConstantesBD.codigoEstadoPeticionReprogramada+","+ConstantesBD.codigoEstadoPeticionAtendida+")";	
				
		try{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			logger.info("\n\n\n\n>>Consulta<<"+consulta+"\n\n\n\n");
		}
		catch (SQLException e) {
			logger.info("Error en la consulta de indicadores parametro " +
				"Proporcion de cancelacion de cirugia programada denominador "+e+" \n\n<Consulta>"+consulta);
		}
		denominador=resultado.get("denominador_0").toString();
		
		resultado.put("numerador", numerador);
		resultado.put("denominador", denominador);
		
		return resultado;
	}
	
	
	
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static HashMap consultaOportunidadAtencionConsultaUrgencias(Connection con, HashMap mapa)
	{
		HashMap resultado=new HashMap();
		int numerador=0;
		int denominador=0;
		
		String consulta="	SELECT " +
						"		getobtenerDiferenciaTiempo(1,v.fecha_valoracion,''||v.hora_valoracion,t.fecha,''||t.hora) as numerador "+
						"	FROM triage t " +
						"	INNER JOIN destino_paciente dp ON (dp.consecutivo=t.destino AND dp.indicador_admi_urg="+ValoresPorDefecto.getValorTrueParaConsultas()+")" +
						"	INNER JOIN categorias_triage ct ON (ct.consecutivo = t.categoria_triage)" +
						"	INNER JOIN colores_triage col ON (ct.color=col.codigo)" +
						"	INNER JOIN admisiones_urgencias au ON (t.numero_admision=au.codigo AND t.anio_admision=au.anio)" +
						"	INNER JOIN solicitudes s ON (s.cuenta=au.cuenta)" +
						"	INNER JOIN valoraciones v ON (s.numero_solicitud=v.numero_solicitud)" +
						"WHERE" +
						"	t.no_responde_llamado ='"+ValoresPorDefecto.getValorFalseCortoParaConsultas()+"' "+
						"	AND t.numero_admision IS NOT NULL " +
						"	AND t.institucion="+mapa.get("codinstitucion").toString()+" "+
						"	AND t.fecha  BETWEEN "+mapa.get("periodo").toString()+" " +
						"	AND v.fecha_valoracion IS NOT NULL " +
						"	AND v.hora_valoracion IS NOT NULL " +
						"	AND s.tipo IN ("+ConstantesBD.codigoTipoSolicitudInicialUrgencias+") " +
					" UNION " +
						"	SELECT" +
						"		getobtenerDiferenciaTiempo(1,v.fecha_valoracion,''||v.hora_valoracion,au.fecha_admision,''||au.hora_admision) as numerador "+
						"	FROM  admisiones_urgencias au " +
						"	INNER JOIN solicitudes s ON (s.cuenta=au.cuenta) " +
						"	INNER JOIN valoraciones v ON (s.numero_solicitud=v.numero_solicitud) " +
						" WHERE " +
						"	au.fecha_admision  BETWEEN "+mapa.get("periodo").toString()+" " +
						"	AND v.fecha_valoracion IS NOT NULL " +
						"	AND v.hora_valoracion IS NOT NULL " +
						"	AND s.tipo IN ("+ConstantesBD.codigoTipoSolicitudInicialUrgencias+") " +
						"	AND au.consecutivo_triage IS NULL ";
		
		try{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			logger.info("\n\n\n\n>>Consulta<<"+consulta+"\n\n\n\n");
		}
		catch (SQLException e) {
			logger.info("Error en la consulta de indicadores parametro " +
				"OPORTUNIDAD ATENCION EN CONSULTA URGENCIAS "+e+" \n\n<Consulta>"+consulta);
		}
	
		
		// se itera el mapa resultado para obtener las diferencias y hacer la sumatoria de estas (Numerador)
		//el (denominador) es el numRegistros del mapa
		for (int i=0;i<Utilidades.convertirAEntero(resultado.get("numRegistros").toString());i++)
		{
			numerador=(int) (numerador+Utilidades.convertirADouble(resultado.get("numerador_"+i).toString()));
		}
		
		
		
		HashMap nd=new HashMap();
		nd.put("denominador", resultado.get("numRegistros")+"00");
		nd.put("numerador", numerador+"00");
		
		return nd;
	}
	

	
	
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static HashMap consultaOportunidadAtencionServiciosImagenologia(Connection con, HashMap mapa)
	{
		
	HashMap resultado=new HashMap();
		int numerador=0;
		String consulta=" SELECT " +
							"coalesce(getobtenerFechaRespuestSolicit(s.numero_solicitud)-s.fecha_solicitud, 0)||'00' as numerador " +
							" FROM " +
							"	solicitudes s " +
							"		INNER JOIN ind_calidad_cc icc ON (s.centro_costo_solicitado=icc.centro_costo)	"+	
							"   WHERE " +
							"		1=1	"+
							" 		AND s.fecha_solicitud between "+mapa.get("periodo").toString()+" " +
							"		AND s.estado_historia_clinica IN("+ConstantesBD.codigoEstadoHCRespondida+","+ConstantesBD.codigoEstadoHCInterpretada+") " +
							"		AND s.tipo="+ConstantesBD.codigoTipoSolicitudProcedimiento;
		
		try{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			logger.info("\n\n\n\n>>Consulta<<"+consulta+"\n\n\n\n");
		
		}
		catch (SQLException e) {
			logger.info("Error en la consulta de indicadores parametro " +
								"consulta Oportunidad Atencion Servicios Imagenologia "+e+" \n\n<Consulta>"+consulta);
		}
		
		for (int i=0;i<Utilidades.convertirAEntero(resultado.get("numRegistros").toString());i++)
		{
			numerador=(int) (numerador+Utilidades.convertirADouble(resultado.get("numerador_"+i).toString()));
		}
		HashMap nd=new HashMap();
		
		nd.put("numerador", numerador);
		nd.put("denominador", resultado.get("numRegistros").toString()+"00");
		
		return nd;
	
	}
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static HashMap consultaCirugiaProgramada(Connection con, HashMap mapa)
	{
	
		HashMap resultado=new HashMap();
		
		int numerador=0;
			String consulta=" SELECT " +
								"	coalesce(sc.fecha_inicial_cx-s.fecha_solicitud, 0)||'00' as numerador " +
								" FROM " +
								"	solicitudes s " +
								" 		INNER JOIN solicitudes_cirugia sc ON (s.numero_solicitud=sc.numero_solicitud) " +
								"   WHERE " +
								"		1=1	"+
								" 		AND s.fecha_solicitud between "+mapa.get("periodo").toString()+" " +
								"		AND s.estado_historia_clinica IN("+ConstantesBD.codigoEstadoHCRespondida+","+ConstantesBD.codigoEstadoHCInterpretada+") " +
								"		AND sc.ind_qx='"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia+"' ";
		
			try{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
				logger.info("\n\n\n\n>>Consulta<<"+consulta+"\n\n\n\n");
			
			}
			catch (SQLException e) {
				logger.info("Error en la consulta de indicadores parametro " +
									"consulta Cirugia Programada "+e+" \n\n<Consulta>"+consulta);
			}
			
			for (int i=0;i<Utilidades.convertirAEntero(resultado.get("numRegistros").toString());i++)
			{
				numerador=(int) (numerador+Utilidades.convertirADouble(resultado.get("numerador_"+i).toString()));
			}
			HashMap nd=new HashMap();
			
			nd.put("numerador", numerador);
			nd.put("denominador", resultado.get("numRegistros").toString()+"00");
			
			return nd;
		
	}
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static HashMap consultaTasaReingresoPacientesHospitalizados(Connection con, HashMap mapa)
	{
		
		HashMap resultado=new HashMap();
		
		int numerador=0;
			String consulta="SELECT " +
								"	count(*) as numerador " +
								" FROM egresos e  " +
								" 	INNER JOIN cuentas c on (c.id=e.cuenta) " +
								" WHERE " +
								"	 (SELECT " +
										" 	diagnostico_principal " +
										" FROM " +
										"	egresos ei " +
										"		INNER JOIN cuentas ci on (ci.id=ei.cuenta) " +
										"	WHERE " +
										"		ei.cuenta<>e.cuenta " +
										"		and c.codigo_paciente=ci.codigo_paciente " +
										"		and (ei.fecha_egreso-e.fecha_egreso)<=20 " +
										" 		and ci.via_ingreso="+ConstantesBD.codigoViaIngresoHospitalizacion+" " +
										" 		and ci.tipo_paciente='"+ConstantesBD.tipoPacienteHospitalizado+"' " +
										"		and e.estado_salida<>'"+ValoresPorDefecto.getValorTrueParaConsultas()+"' " +
										" "+ValoresPorDefecto.getValorLimit1()+" 1" +
										") IN (e.diagnostico_principal,e.diagnostico_relacionado1,e.diagnostico_relacionado2,e.diagnostico_relacionado3,e.diagnostico_complicacion)" +
								" AND c.via_ingreso="+ConstantesBD.codigoViaIngresoHospitalizacion+" " +
								" AND c.tipo_paciente='"+ConstantesBD.tipoPacienteHospitalizado+"' "+
								" AND e.fecha_egreso between "+mapa.get("periodo").toString()+" " +
								" AND e.estado_salida<>'"+ValoresPorDefecto.getValorTrueParaConsultas()+"' " ;
			
			
		
			try{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
				logger.info("\n\n\n\n>>Consulta<<"+consulta+"\n\n\n\n");
			
			}
			catch (SQLException e) {
				logger.info("Error en la consulta de indicadores parametro " +
									"Tasa de Reingreso de pacientes hospitalizados "+e+" \n\n<Consulta>"+consulta);
			}
			
			for (int i=0;i<Utilidades.convertirAEntero(resultado.get("numRegistros").toString());i++)
			{
				numerador=(int) (numerador+Utilidades.convertirADouble(resultado.get("numerador_"+i).toString()));
			}
			HashMap nd=new HashMap();
			
			nd.put("numerador", numerador+"00");
			
			
			consulta="SELECT " +
						"	count(*)||'00' as denominador " +
						" FROM egresos e  " +
						" 	INNER JOIN cuentas c on (c.id=e.cuenta) " +
						" WHERE " +
							" c.via_ingreso="+ConstantesBD.codigoViaIngresoHospitalizacion+" " +
							" AND c.tipo_paciente='"+ConstantesBD.tipoPacienteHospitalizado+"' "+
							" AND e.fecha_egreso between "+mapa.get("periodo").toString()+" " +
							" AND e.estado_salida<>'"+ValoresPorDefecto.getValorTrueParaConsultas()+"' " ;

			try{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			logger.info("\n\n\n\n>>Consulta<<"+consulta+"\n\n\n\n");
			
			}
			catch (SQLException e) {
			logger.info("Error en la consulta de indicadores parametro " +
							"Tasa de Reingreso de pacientes hospitalizados "+e+" \n\n<Consulta>"+consulta);
			}
			
			nd.put("denominador", resultado.get("denominador_0").toString());
			
			return nd;
		
		
		
	}
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static HashMap consultaProporcionPacientesHipertencionArterialControlada(Connection con, HashMap mapa)
	{
		HashMap resultado=new HashMap();
		int numerador=0;
		
			String consulta="SELECT " +
								"		count(1)||'00' as numerador  " +
								"	FROM " +
								"	((SELECT distinct " +
								"				t.paciente, " +
								"				sum(t.tad) as tad, " +
								"				sum(t.tas) as tas" +
								"			FROM " +
								"			(	SELECT " +
								"					c.codigo_paciente as paciente, " +
								"					1 as tad, " +
								"					0 AS tas " +
								"					from " +
								"						evoluciones e " +
								"					inner join " +
								"						evol_signos_vitales esv on (esv.evolucion=e.codigo) " +
								"					inner join " +
								"						ind_generales ig on (ig.tension_arterial_diastolica=esv.signo_vital) " +
								"					inner join " +
								"						solicitudes s on (s.numero_solicitud=e.valoracion) " +
								"					inner join " +
								"						cuentas c on (s.cuenta=c.id) " +
								"					where " +
								"						e.fecha_evolucion BETWEEN "+mapa.get("periodo").toString()+" " +
								"						and convertiranumero(esv.valor||'')<=ig.maximo_normal_tad " +
								"						and c.via_ingreso IN ("+ConstantesBD.codigoViaIngresoUrgencias+","+ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoConsultaExterna+") " +
								"						and getPacientConEgresoIndicCalid(c.codigo_paciente,e.fecha_evolucion)>0 " +
								"						and getValidDiagnostEvolucIndCalid(e.codigo,'"+ConstantesBD.acronimoProporcionPacientesHipertensionArterialControlada+"')>0 " +
								"			union " +
								"				SELECT " +
								"					c.codigo_paciente as paciente, " +
								"					0 as tad, " +
								"					1 as tas " +
								"					from " +
								"						evoluciones e " +
								"					inner join " +
								"						evol_signos_vitales esv on (esv.evolucion=e.codigo) " +
								"					inner join " +
								"						ind_generales ig on (ig.tension_arterial_sistolica=esv.signo_vital) " +
								"					inner join " +
								"						solicitudes s on (s.numero_solicitud=e.valoracion) " +
								"					inner join " +
								"						cuentas c on (s.cuenta=c.id) " +
								"					where " +
								"						e.fecha_evolucion BETWEEN "+mapa.get("periodo").toString()+" " +
								"						and convertiranumero(esv.valor||'')<=ig.maximo_normal_tas " +
								"						and c.via_ingreso IN ("+ConstantesBD.codigoViaIngresoUrgencias+","+ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoConsultaExterna+")" +
								"						and getPacientConEgresoIndicCalid(c.codigo_paciente,e.fecha_evolucion)>0" +
								"						and getValidDiagnostEvolucIndCalid(e.codigo,'"+ConstantesBD.acronimoProporcionPacientesHipertensionArterialControlada+"')>0 " +
								"			) t " +
								"			group by t.paciente " +
								"	) " +
								" UNION " +
								"	(SELECT distinct " +
								"			y.paciente, " +
								"			sum(y.tad) as tad, " +
								"			sum(y.tas) as tas " +
								"		FROM " +
								"			(	SELECT " +
								"					c.codigo_paciente as paciente, " +
								"					1 as tad, " +
								"					0 AS tas " +
								"					from " +
								"						valoraciones v " +
								"					inner join " +
								"						val_signos_vitales vsv on (vsv.valoracion=v.numero_solicitud) " +
								"					inner join " +
								"						ind_generales ig on (ig.tension_arterial_diastolica=vsv.signo_vital) " +
								"					inner join " +
								"						solicitudes s on (s.numero_solicitud=v.numero_solicitud) " +
								"					inner join " +
								"						cuentas c on (s.cuenta=c.id) " +
								"					where " +
								"						v.fecha_valoracion BETWEEN "+mapa.get("periodo").toString()+" " +
								"						and convertiranumero(vsv.valor||'')<=ig.maximo_normal_tad " +
								"						and c.via_ingreso IN ("+ConstantesBD.codigoViaIngresoUrgencias+","+ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoConsultaExterna+")" +								
								"						and getPacientConEgresoIndicCalid(c.codigo_paciente,v.fecha_valoracion)>0 " +
								"						and getValidDiagnosValoracIndCalid(v.numero_solicitud,'"+ConstantesBD.acronimoProporcionPacientesHipertensionArterialControlada+"')>0 " +
								"			union " +
								"				SELECT " +
								"					c.codigo_paciente as paciente, " +
								"					0 as tad, " +
								"					1 as tas " +
								"					from " +
								"						valoraciones v " +
								"					inner join " +
								"						val_signos_vitales vsv on (vsv.valoracion=v.numero_solicitud) " +
								"					inner join " +
								"						ind_generales ig on (ig.tension_arterial_sistolica=vsv.signo_vital) " +
								"					inner join " +
								"						solicitudes s on (s.numero_solicitud=v.numero_solicitud) " +
								"					inner join " +
								"						cuentas c on (s.cuenta=c.id) " +
								"					where " +
								"						v.fecha_valoracion BETWEEN "+mapa.get("periodo").toString()+" " +
								"						and convertiranumero(vsv.valor||'')<=ig.maximo_normal_tas" +
								"						and c.via_ingreso IN ("+ConstantesBD.codigoViaIngresoUrgencias+","+ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoConsultaExterna+")" +
								"						and getPacientConEgresoIndicCalid(c.codigo_paciente,v.fecha_valoracion)>0 " +
								"						and getValidDiagnosValoracIndCalid(v.numero_solicitud,'"+ConstantesBD.acronimoProporcionPacientesHipertensionArterialControlada+"')>0 " +
								"			) y " +
								"			group by y.paciente " +
								"	)) x " +
								"WHERE x.tad >0 and x.tas > 0"	;	
 
			try{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
				logger.info("\n\n\n\n>>Consulta<<"+consulta+"\n\n\n\n");
			}
			catch (SQLException e) {
				logger.info("Error en la consulta de indicadores parametro " +
									"consulta Paciente Tension Arterial normal "+e+" \n\n<Consulta>"+consulta);
				e.printStackTrace();
			}
			

			HashMap nd=new HashMap();
			
			nd.put("numerador", resultado.get("numerador_0"));
			
	
			//Consulta Denominador
			
			consulta="SELECT " +
					"		count(*)||'00' as denominador," +
					"	    t.acronimo as acronimo " +
					"		FROM " +
					"		(SELECT " +
					"				distinct " +
					"				c.codigo_paciente as paciente," +
					"				icc.acronimo as acronimo " +
					"			from " +
					"				evoluciones e " +
					"			inner join " +
					"					evol_diagnosticos ed on (ed.evolucion=e.codigo) " +
					"			inner join " +
					"				ind_calidad_dx icd on (icd.acronimo_dx=ed.acronimo_diagnostico and icd.tipo_cie_dx=ed.tipo_cie_diagnostico) " +
					"			inner join " +
					"				ind_calidad_codigos icc on (icc.codigo=icd.ind_calidad_codigo) " +
					"			inner join " +
					"				solicitudes s on (s.numero_solicitud=e.valoracion) " +
					"			inner join " +
					"				cuentas c on (s.cuenta=c.id) " +
					"			where " +
					"				e.fecha_evolucion BETWEEN "+mapa.get("periodo").toString()+" " +
						//	"		and getValidDiagnostEvolucIndCalid(e.codigo,'"+ConstantesBD.acronimoProporcionPacientesHipertensionArterialControlada+"')>0 " +
					"	union " +
					"		SELECT " +
					"				distinct " +
					"				c.codigo_paciente as paciente," +
					"				icc.acronimo as acronimo " +
					"			from " +
					"				valoraciones v " +
					"			inner join " +
					"					val_diagnosticos vd on (vd.valoracion=v.numero_solicitud) " +
					"			inner join " +
					"					ind_calidad_dx icd on (icd.acronimo_dx=vd.acronimo_diagnostico and icd.tipo_cie_dx=vd.tipo_cie_diagnostico) " +
					"			inner join " +
					"				ind_calidad_codigos icc on (icc.codigo=icd.ind_calidad_codigo) " +
					"			inner join " +
					"					solicitudes s on (s.numero_solicitud=v.numero_solicitud) " +
					"			inner join " +
					"					cuentas c on (s.cuenta=c.id) " +
					"			where " +
					"				v.fecha_valoracion BETWEEN "+mapa.get("periodo").toString()+" " +
					//		" 		and getValidDiagnosValoracIndCalid(v.numero_solicitud,'"+ConstantesBD.acronimoProporcionPacientesHipertensionArterialControlada+"' )>0 " +
					") t " +
					"	group by t.acronimo ";	
			
				try{
					PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
					logger.info("\n\n\n\n>>Consulta<<"+consulta+"\n\n\n\n");
				}
				catch (SQLException e) {
					logger.info("Error en la consulta de indicadores parametro " +
									" hipertencion arterial controlada "+e+" \n\n<Consulta>"+consulta);
				}

			resultado.put("numerador", nd.get("numerador"));
			
			return resultado;

	}
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static HashMap consultaTasaMortalidadIntrahospitalariaDespuesDosDias(Connection con, HashMap mapa)
	{
		HashMap resultado=new HashMap();
		int numerador=0;
		String consulta="SELECT " +
						"	count(1)||'00' as numerador " +
						"	from " +
						"		admisiones_hospi ah " +
						"	inner join " +
						"		egresos e on (ah.cuenta=e.cuenta) " +
						"	inner join " +
						"		cuentas c on (e.cuenta=c.id) " +
						"	inner join " +
						"		pacientes p on (c.codigo_paciente=p.codigo_paciente) " +
						"	where " +
						"		ah.fecha_admision BETWEEN "+mapa.get("periodo").toString()+" " +
						"		and e.estado_salida='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' "+
						"		and getobtenerDiferenciaTiempo(2, p.fecha_muerte,''||coalesce(p.hora_muerte,'00:00'),ah.fecha_admision,''||ah.hora_admision) <= 48 ";

			try{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
				logger.info("\n\n\n\n>>Consulta<<"+consulta+"\n\n\n\n");
			
			}
			catch (SQLException e) {
				logger.info("Error en la consulta de indicadores parametro " +
									"consulta Tasa Mortalidad Intrahospitalaria Despues Dos Dias "+e+" \n\n<Consulta>"+consulta);
			}
			HashMap nd=new HashMap();
			nd.put("numerador", resultado.get("numerador_0"));
		
			consulta="SELECT " +
					"	count(1)||'00' as denominador " +
					"	from " +
					"		admisiones_hospi ah " +
					"	inner join " +
					"		cuentas c on (ah.cuenta=c.id) " +
					"	inner join " +
					"		pacientes p on (c.codigo_paciente=p.codigo_paciente) " +
					"	where " +
					"		ah.fecha_admision BETWEEN "+mapa.get("periodo").toString()+" ";
					
			try{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
				logger.info("\n\n\n\n>>Consulta<<"+consulta+"\n\n\n\n");
			
			}
			catch (SQLException e) {
				logger.info("Error en la consulta de indicadores parametro " +
									"consulta Tasa Mortalidad Intrahospitalaria Despues Dos Dias "+e+" \n\n<Consulta>"+consulta);
			}
			
			nd.put("denominador", resultado.get("denominador_0").toString());
			
			return nd;
		
		
		
		
	}
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static HashMap consultaTasaInfeccionIntrahospitalaria(Connection con, HashMap mapa)
	{
		return null;
	}
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static HashMap consultaProporcionVigilanciaEventosAdversos(Connection con, HashMap mapa)
	{
		HashMap resultado=new HashMap();
		int numerador=0;
		String consulta="SELECT " +
						"	count(1)||'00' as numerador " +
						"	from " +
						"		registro_eventos_adversos rea " +
						"	where " +
						"		rea.fecha_registro BETWEEN "+mapa.get("periodo").toString()+" " +
						"		and rea.gestionado='"+ConstantesBD.acronimoSi+"' ";

			try{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
				logger.info("\n\n\n\n>>Consulta<<"+consulta+"\n\n\n\n");
			
			}
			catch (SQLException e) {
				logger.info("Error en la consulta de indicadores parametro " +
									"consultaProporcionVigilanciaEventosAdversos "+e+" \n\n<Consulta>"+consulta);
			}
			HashMap nd=new HashMap();
			nd.put("numerador", resultado.get("numerador_0"));
		
			consulta="SELECT " +
						"	count(1)||'00' as denominador " +
						"	from " +
						"		registro_eventos_adversos rea " +
						"	where " +
						"		rea.fecha_registro BETWEEN "+mapa.get("periodo").toString()+" " ;
			try{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
				logger.info("\n\n\n\n>>Consulta<<"+consulta+"\n\n\n\n");
			
			}
			catch (SQLException e) {
				logger.info("Error en la consulta de indicadores parametro " +
									"consultaProporcionVigilanciaEventosAdversos "+e+" \n\n<Consulta>"+consulta);
			}
			
			nd.put("denominador", resultado.get("denominador_0").toString());
			
			return nd;
		
	}
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static HashMap consultaTasaSatisfaccionGlobal(Connection con, HashMap mapa)
	{
		HashMap resultado=new HashMap();
		int numerador=0;
		String consulta="SELECT " +
						"	count(1)||'00' as numerador " +
						"	from " +
						"		encuesta_calidad ec " +
						"	where " +
						"		ec.fecha_modifica BETWEEN "+mapa.get("periodo").toString()+" " +
						"		and ec.calificacion='"+ConstantesIntegridadDominio.acronimoSatisfaccion+"' ";

			try{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
				logger.info("\n\n\n\n>>Consulta<<"+consulta+"\n\n\n\n");
			
			}
			catch (SQLException e) {
				logger.info("Error en la consulta de indicadores parametro " +
									"consultaProporcionVigilanciaEventosAdversos "+e+" \n\n<Consulta>"+consulta);
			}
			HashMap nd=new HashMap();
			nd.put("numerador", resultado.get("numerador_0"));
		
			 consulta="SELECT " +
						"	count(1)||'00' as denominador " +
						"	from " +
						"		encuesta_calidad ec " +
						"	where " +
						"		ec.fecha_modifica BETWEEN "+mapa.get("periodo").toString()+" ";
	
		try{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
				logger.info("\n\n\n\n>>Consulta<<"+consulta+"\n\n\n\n");
			
			}
			catch (SQLException e) {
				logger.info("Error en la consulta de indicadores parametro " +
									"consultaProporcionVigilanciaEventosAdversos "+e+" \n\n<Consulta>"+consulta);
			}
			
			nd.put("denominador", resultado.get("denominador_0").toString());
			
			return nd;
	}
	
	
	
	
	public static int guardarLog(Connection con, HashMap mapa)
	{
		int r=0;
		String sentencia="INSERT INTO " +
								"	log_arch_ind_calidad (" +
								"codigo," +
								"institucion," +
								"fecha, " +
								"hora," +
								"usuario  ," +
								"primer_periodo," +
								"archivo_generado," +
								"path_archivo )" +
								"VALUES (" +
								""+UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_log_arch_ind_calidad")+","+
								""+mapa.get("institucion")+","+
								"CURRENT_DATE," +
								""+ValoresPorDefecto.getSentenciaHoraActualBD()+","+
								"'"+mapa.get("usuario")+"',"+
								"'"+mapa.get("periodo")+"',"+
								"'"+mapa.get("archivo")+"',"+
								"'"+mapa.get("path")+"' "+
								")";
		
	try{
		
		PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		r=ps.executeUpdate();
	}
	catch (SQLException e)
	{
		e.printStackTrace();
		logger.info("error insertando log archivo indicadores de calidad >>>>"+sentencia);
	}
		return r;
	}
	
	
	
	
}