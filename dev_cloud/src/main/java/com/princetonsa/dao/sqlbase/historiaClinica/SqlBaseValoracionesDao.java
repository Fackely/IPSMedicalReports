/*
 * Abr 29, 2008
 */
package com.princetonsa.dao.sqlbase.historiaClinica;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.sql.PreparedStatement;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.historiaClinica.DtoRevisionSistema;
import com.princetonsa.dto.historiaClinica.DtoValoracion;
import com.princetonsa.dto.historiaClinica.DtoValoracionHospitalizacion;
import com.princetonsa.dto.historiaClinica.DtoValoracionObservaciones;
import com.princetonsa.dto.historiaClinica.DtoValoracionUrgencias;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.princetonsa.mundo.atencion.SignoVital;
import com.princetonsa.dto.historiaClinica.ValoracionConductaObservacionesDto;

/**
 * 
 *  @author Sebastián Gómez 
 *
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la funcionalidad VALORACIONES
 *
 */
public class SqlBaseValoracionesDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseValoracionesDao.class);
	
	/**
	 * Vector que contiene los indices del histórico
	 */
	private static final String[] indicesHistorico = {"consecutivo_","tipo_","codigoTipo_","fecha_","fechaBd_","hora_","viaIngreso_"};
	

	/**
	 * Cadena usada para insertar una valoración base
	 */
	private static final String insertarValoracionBase_Str = "INSERT INTO valoraciones (" +
		"numero_solicitud," +
		"edad," +
		"fecha_grabacion," +
		"hora_grabacion," +
		"fecha_valoracion," +
		"hora_valoracion," +
		"causa_externa," +
		"codigo_medico," +
		"control_post_operatorio_cx," +
		"cuidado_especial," +
		"observacion_capitacion" +
		") VALUES (?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Cadena usada para hacer una acutualizacion de la tabla Solicitudes ( agrega la especialidad ) 
	 */
	private static final String insertarEspecialidadProfesionalRespondeStr = "UPDATE solicitudes SET especialidad_solicitada = ?  WHERE numero_solicitud = ? ";
	
	/**
	 * Cadena usada para insertar una observacion de valoracion
	 */
	private static final String insertarObservacionValoracion_Str = "INSERT INTO valoracion_observaciones (" +
		"consecutivo," +
		"numero_solicitud," +
		"tipo," +
		"valor," +
		"fecha," +
		"hora," +
		"usuario" +
		") values (?,?,?,?,?,?,?)";
	
	/**
	 * Cadena usada para insertar un diagnostico de valoracion
	 */
	private static final String insertarDiagnosticoValoracion_Str = "INSERT INTO val_diagnosticos (" +
		"valoracion," +
		"acronimo_diagnostico," +
		"tipo_cie_diagnostico," +
		"principal," +
		"definitivo," +
		"numero" +
		") values (?,?,?,?,?,?)";
	
	/**
	 * Cadena para insertar una revisión por sistema único
	 */
	private static final String insertarRevisionXSistemaUnico_str = "INSERT INTO val_revisiones_sistemas (valoracion,revision_sistema,descripcion,valor,estado_normal) values (?,?,?,?,?)";
	
	/**
	 * Cadena para insertar una revisión por sistema multiple
	 */
	private static final String insertarRevisionXSistemaMultipleEnc_Str = "INSERT INTO val_rev_sistemas_enc (valoracion,tipo_rev_sistema,descripcion,estado_normal) VALUES (?,?,?,?)";
	
	/**
	 * Cadena para insertar una opcion de una revisión por sistema
	 */
	private static final String insertarRevisionXSistemaMultipleOp_Str = "INSERT INTO val_rev_sistemas_op (valoracion,rev_sistema_op) VALUES (?,?)";
	
	/**
	 * Cadena usada para insertar un signo vital
	 */
	private static final String insertarSignoVital_Str = "INSERT INTO val_signos_vitales (valoracion,signo_vital,descripcion,valor,estado_normal) VALUES (?,?,?,?,?)";
	
	/**
	 * Método implementado para insertar ina valoracion de consulta
	 */
	private static final String insertarValoracionConsulta_Str = "INSERT INTO valoraciones_consulta (" +
		"numero_solicitud," +
		"primera_vez," +
		"finalidad_consulta," +
		"tipo_diagnostico_principal," +
		"tipo_recargo," +
		"fecha_proximo_control," +
		"num_dias_incapacidad," +
		"observaciones_incapacidad" +
		") VALUES (?,?,?,?,?,?,?,?)";
	
	/**
	 * Cadena para insertar un documento adjunto
	 */
	private static final String insertarDocumentoAdjuntoStr = "INSERT INTO doc_adj_solicitud (" +
		"codigo," +
		"numero_solicitud," +
		"nombre_archivo," +
		"nombre_original," +
		"es_solicitud," +
		"codigo_medico," +
		"es_codigo_resp_sol" +
		") values (?,?,?,?,"+ValoresPorDefecto.getValorFalseParaConsultas()+",?,?)";
	

	
	/**
	 * Cadena que inserta una valoración de hospitalizacion
	 */
	private static final String insertarValoracionHospitalizacion_Str = "INSERT INTO val_hospitalizacion (" +
		"numero_solicitud," +
		"texto_origen_no_urgencias," +
		"codigo_origen_admision_hosp," +
		"diagnostico_ingreso," +
		"diagnostico_cie_ingreso" +
		") VALUES (?,?,?,?,?)";
	
	/**
	 * Cadena que carga valoracion base
	 */
	private static final String cargarBase_Str = "SELECT "+ 
		"v.numero_solicitud, " +
		"s.cuenta as cuenta, "+
		"coalesce(v.edad,'') as edad, "+
		"to_char(v.fecha_grabacion,'"+ConstantesBD.formatoFechaAp+"') as fecha_grabacion, "+
		"substr(v.hora_grabacion,0,6) AS hora_grabacion, "+
		"to_char(v.fecha_valoracion,'"+ConstantesBD.formatoFechaAp+"') as fecha_valoracion, "+
		"substr(v.hora_valoracion,0,6) as hora_valoracion, "+
		"coalesce(v.causa_externa,"+ConstantesBD.codigoNuncaValido+") as codigo_causa_externa, "+
		"coalesce(getnombrecausaexterna(v.causa_externa),'') as nombre_causa_externa, "+
		"coalesce(v.codigo_medico,"+ConstantesBD.codigoNuncaValido+") as codigo_medico, " +
		"coalesce(s.especialidad_solicitada,"+ConstantesBD.codigoNuncaValido+") as especialidad_solicitada "+ 
		"FROM valoraciones v inner join solicitudes s on(s.numero_solicitud=v.numero_solicitud) WHERE v.numero_solicitud = ?";
	
	/**
	 * Cadena que carga observaciones
	 */
	private static final String cargarObservaciones_Str = "SELECT "+ 
		"consecutivo, "+
		"numero_solicitud, "+
		"tipo, "+
		"valor, "+
		"to_char(fecha,'"+ConstantesBD.formatoFechaAp+"') as fecha_registro, "+
		"hora, "+
		"usuario "+ 
		"from valoracion_observaciones WHERE numero_solicitud = ? " +
		"order by tipo,fecha,hora";
	
	/**
	 * Cadena para carga los diagnosticos de la valoracion
	 */
	private static final String cargarDiagnosticos_Str = "SELECT "+ 
		"acronimo_diagnostico as acronimo, "+
		"tipo_cie_diagnostico as tipo_cie, "+
		"getnombrediagnostico(acronimo_diagnostico,tipo_cie_diagnostico) as nombre_diagnostico, "+ 
		"principal, "+
		"definitivo, "+
		"numero "+ 
		"from val_diagnosticos WHERE valoracion = ? order by numero";
	
	/**
	 * Cadena para cargar las revisiones por sistemas único
	 */
	private static final String cargarRevisionXSistemasUnico_Str = "SELECT "+ 
		"v.revision_sistema AS codigo, "+
		"coalesce(v.descripcion,'') as descripcion, "+
		"coalesce(v.valor||'','') as valor, "+
		"CASE WHEN v.estado_normal IS NULL THEN '' WHEN v.estado_normal = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END as estado_normal, "+
		"rv.nombre as nombre, "+
		"coalesce(rv.unidad_medida,'') as unidad_medida, "+
		"rv.verdadero, "+
		"rv.falso, "+ 
		"coalesce(rv.tipo_componente,"+ConstantesBD.codigoNuncaValido+") as tipo_componente "+ 
		"FROM val_revisiones_sistemas v "+ 
		"INNER JOIN revisiones_sistemas rv ON(rv.codigo=v.revision_sistema) "+ 
		"WHERE v.valoracion = ? ORDER BY rv.orden";
	
	/**
	 * Cadena para cargar las revision por sistema multiples
	 */
	private static final String cargarRevisionXSistemasMultiple_Str = "SELECT "+ 
		"v.tipo_rev_sistema AS codigo, "+
		"coalesce(v.descripcion,'') as descripcion, "+
		"CASE WHEN v.estado_normal IS NULL THEN '' WHEN v.estado_normal = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END as estado_normal, "+
		"t.nombre as nombre, "+
		"t.verdadero, "+
		"t.falso, "+ 
		"coalesce(t.tipo_componente,"+ConstantesBD.codigoNuncaValido+") as tipo_componente "+ 
		"FROM val_rev_sistemas_enc v "+ 
		"INNER JOIN tipos_rev_sistemas t ON(t.codigo=v.tipo_rev_sistema) "+ 
		"WHERE v.valoracion = ?";
	
	/**
	 * Cadena para cargar las opciones de una revision por sistemas
	 */
	private static final String cargarRevisionXSistemasOpciones_Str = "SELECT "+ 
		"v.rev_sistema_op as codigo, "+
		"r.nombre as nombre "+ 
		"from val_rev_sistemas_op v "+ 
		"INNER JOIN rev_sistemas_op r ON(r.codigo=v.rev_sistema_op) "+ 
		"WHERE "+ 
		"v.valoracion = ? and r.tipo_rev_sistema = ? order by r.nombre";
	
	/**
	 * Cadena para cargar los signos vitales
	 */
	private static final String cargarSignosVitales_Str = "SELECT "+ 
		"v.signo_vital as codigo, "+ 
		"coalesce(v.descripcion,'') as descripcion, "+ 
		"coalesce(v.valor,'') As valor, "+
		"CASE WHEN v.estado_normal IS NULL THEN '' WHEN v.estado_normal = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END as estado_normal, "+ 
		"coalesce(sv.nombre,'') as nombre, "+
		"coalesce(sv.unidad_medida,'') as unidad_medida "+ 
		"FROM val_signos_vitales v "+ 
		"INNER JOIN signos_vitales sv ON(sv.codigo=v.signo_vital) "+ 
		"WHERE v.valoracion = ? order by sv.orden";
	
	/**
	 * Cadena para cargar datos de la consulta
	 */
	private static final String cargarConsulta_Str = "SELECT "+ 
		"CASE WHEN primera_vez IS NULL THEN '' WHEN primera_vez = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END as primera_vez, "+
		"coalesce(finalidad_consulta,'') as codigo_finalidad_consulta, "+
		"coalesce(getnomfinalidadconsulta(finalidad_consulta),'') as nombre_finalidad_consulta, "+
		"coalesce(tipo_diagnostico_principal,"+ConstantesBD.codigoNuncaValido+") as codigo_tipo_diagnostico, "+
		"coalesce(getnombretipodiag(tipo_diagnostico_principal),'') as nombre_tipo_diagnostico, "+ 
		"coalesce(tipo_recargo,"+ConstantesBD.codigoNuncaValido+") as codigo_tipo_recargo, "+
		"coalesce(getnombretiporecargo(tipo_recargo),'') as nombre_tipo_recargo, "+
		"coalesce(to_char(fecha_proximo_control,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha_proximo_control," +
		"coalesce(num_dias_incapacidad||'','') as num_dia_incapacidad, " +
		"coalesce(observaciones_incapacidad,'') AS observaciones_incapacidad "+ 
		"FROM valoraciones_consulta "+ 
		"WHERE numero_solicitud = ?";
	
	/**
	 * Cadena que se usa para consultar los documentos adjuntos de la solicitud
	 */
	private static final String cargarDocumentosAdjuntosStr = "SELECT nombre_archivo AS adj_valoracion,nombre_original AS adj_original_valoracion FROM doc_adj_solicitud WHERE numero_solicitud = ?";
	
	/**
	 * Cadena para cargar los datos de la valoracion de urgencias
	 */
	private static final String cargarUrgencias_Str = "SELECT "+ 
		"CASE WHEN vu.estado_llegada IS NULL THEN '' WHEN vu.estado_llegada = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END as estado_llegada, "+
		"coalesce(vu.desc_estado_llegada,'') AS descripcion_estado_llegada, "+
		"coalesce(vu.nombre_autorizador,'') as nombre_autorizador, "+
		"coalesce(vu.relacion_autorizador,'') as relacion_autorizador, "+
		"coalesce(vu.cod_est_con_llegada,"+ConstantesBD.codigoNuncaValido+") as cod_est_con_llegada, "+
		"coalesce(ec1.nombre,'') as nom_est_con_llegada, "+
		"coalesce(vu.desc_estado_conciencia_llegada,'') as desc_estado_conciencia_llegada, "+
		"coalesce(vu.codigo_estado_conciencia,"+ConstantesBD.codigoNuncaValido+") as codigo_estado_conciencia, "+
		"coalesce(ec2.nombre,'') as nombre_estado_conciencia, "+
		"coalesce(vu.desc_estado_conciencia,'') as desc_estado_conciencia, "+
		"coalesce(vu.codigo_conducta_valoracion,"+ConstantesBD.codigoNuncaValido+") as codigo_conducta_valoracion, "+
		"coalesce(cv.nombre,'') as nombre_conducta_valoracion, "+
		"coalesce(vu.desc_conducta_valoracion,'') as desc_conducta_valoracion, "+
		"coalesce(vu.codigo_categoria_triage,"+ConstantesBD.codigoNuncaValido+") as codigo_categoria_triage, "+
		"coalesce(vu.desc_categoria_triage,'') as desc_categoria_triage, "+
		"CASE WHEN vu.estado_embriaguez IS NULL THEN '' WHEN vu.estado_embriaguez = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END as estado_embriaguez," +
		"coalesce(vu.tipo_monitoreo,"+ConstantesBD.codigoNuncaValido+") AS  codigo_tipo_monitoreo, " +
		"coalesce(getnombretipomonitoreo(vu.tipo_monitoreo),'') AS nombre_tipo_monitoreo, " +
		"to_char(vu.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_modifica," +
		"vu.hora_modifica AS hora_modifica "+ 
		"FROM valoraciones_urgencias vu "+ 
		"LEFT OUTER JOIN estados_conciencia ec1 ON(ec1.codigo=vu.cod_est_con_llegada) "+
		"LEFT OUTER JOIN estados_conciencia ec2 ON(ec2.codigo=vu.codigo_estado_conciencia) "+ 
		"LEFT OUTER JOIN conductas_valoracion cv ON(cv.codigo=vu.codigo_conducta_valoracion) "+ 
		"WHERE vu.numero_solicitud = ?";
	
	/**
	 * Cadena para cargar la valoracion de hospitalizacion
	 */
	private static final String cargarHospitalizacion_Str = "SELECT "+ 
		"coalesce(texto_origen_no_urgencias,'') as texto_origen_no_urgencias, "+
		"codigo_origen_admision_hosp, "+
		"getnomorigenadmision(codigo_origen_admision_hosp) as nombre_origen_admision_hosp, "+
		"diagnostico_ingreso AS acronimo, "+
		"diagnostico_cie_ingreso as tipo_cie, "+
		"getnombrediagnostico(diagnostico_ingreso,diagnostico_cie_ingreso) as nombre_diagnostico "+ 
		"FROM val_hospitalizacion "+ 
		"WHERE numero_solicitud = ?";
	
	/**
	 * Cadena usada para insertar la epicrisis
	 */
	private static final String insertarPrimeraVersionEpicrisis_Str = "INSERT INTO epicrisis (ingreso,va_urgencias,va_hospitalizacion,codigo_medico) values (?,?,?,?)";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para actualizar 
	 * la primera versión de la epicrisis. Se dejan los dos valores por defecto en false
	 */
	private static final String actualizarPrimeraVersionEpicrisisStr = "UPDATE epicrisis set codigo_medico=?, va_urgencias=?, va_hospitalizacion=? where ingreso=?";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para revisar 
	 * si ya existe en la BD la primera versión de la epicrisis
	 */
	private static final String existePrimeraVersionEpicrisisStr="SELECT count(1) as numResultados from epicrisis where ingreso=?";
	
	/**
	 * Cadena para cargar el histórico de las atenciones del asocio
	 */
	private static final String cargarHistoricoStr = "SELECT "+ 
		"t.consecutivo, "+
		"t.tipo, " +
		"t.codigo_tipo, "+
		"to_char(t.fecha,'"+ConstantesBD.formatoFechaAp+"') AS fecha, " +
		"t.fecha AS fecha_bd, "+
		"t.hora, "+
		"t.via_ingreso "+ 
		"FROM "+ 
		"( "+
			//*************CONSULTA DE LAS VALORACIONES DEL ASOCIO**********************************************************
			"SELECT "+ 
			"s.numero_solicitud AS consecutivo, "+
			"getnomtiposolicitud(s.tipo) as tipo, " +
			"s.tipo AS codigo_tipo, "+
			"v.fecha_valoracion AS fecha, "+
			"substr(v.hora_valoracion,0,6) AS hora, "+
			"c.via_ingreso as via_ingreso "+ 
			"FROM cuentas c "+ 
			"inner join asocios_cuenta ac ON(ac.cuenta_inicial = c.id) "+
			"INNER JOIN solicitudes s ON(s.cuenta = c.id and s.tipo in ("+ConstantesBD.codigoTipoSolicitudInicialUrgencias+","+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+")) "+ 
			"INNER JOIN valoraciones v ON(v.numero_solicitud=s.numero_solicitud) "+ 
			"WHERE c.id_ingreso = ? "+ 
			"UNION "+ 
			//********CONSULTA DE LAS EVOLUCIONES DEL ASOCIO*****************************************************************
			"SELECT "+ 
			"e.codigo AS consecutivo, "+
			"'Evolución' AS tipo, " +
			ConstantesBD.codigoTipoSolicitudEvolucion+" AS codigo_tipo, "+
			"e.fecha_evolucion as fecha, "+
			"substr(e.hora_evolucion,0,6) AS hora, "+
			"c.via_ingreso as via_ingreso "+ 
			"FROM cuentas c "+ 
			"INNER JOIN solicitudes s ON(s.cuenta = c.id AND s.tipo IN ("+ConstantesBD.codigoTipoSolicitudInicialUrgencias+","+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+")) "+ 
			"INNER JOIN valoraciones v ON(v.numero_solicitud = s.numero_solicitud) "+
			"INNER JOIN evoluciones e ON(e.valoracion= v.numero_solicitud) "+ 
			"WHERE c.id_ingreso = ? "+
		") t "+ 
		"ORDER BY fecha, hora";
	
	/**
	 * Cadena para consultar el codigo del registro de cuidado especial
	 */
	private static final String obtenerCodigoRegistroCuidadoEspecialStr = "SELECT codigo FROM ingresos_cuidados_especiales WHERE ingreso = ? AND estado = '"+ConstantesIntegridadDominio.acronimoEstadoActivo+"'";
	
	
	/**
	 * Cadena para obtener la causa externa de la valoración 
	 */
	private static final String obtenerCausaExternaValoracionStr = "SELECT v.causa_externa AS codigo_causa_externa, getnomtiposolicitud(s.tipo) AS nombre_tipo_solicitud FROM solicitudes s INNER JOIN valoraciones v on(v.numero_solicitud=s.numero_solicitud) WHERE s.cuenta = ? ";
	
	/**
	 * Método para obtener la finalidad de la consulta para la valoración
	 */
	private static final String obtenerFinalidadConsultaValoracionStr = "SELECT "+ 
		"vc.finalidad_consulta AS codigo_finalidad_consulta, "+ 
		"getnomfinalidadconsulta(vc.finalidad_consulta) AS nombre_finalidad_consulta "+  
		"FROM solicitudes s "+ 
		"INNER JOIN valoraciones_consulta vc ON(s.numero_solicitud = vc.numero_solicitud) "+  
		"WHERE s.cuenta =  ? ";
	
	/**
	 * Cadena para consultar si una solicitud es de ciudados especiales, ya que existe otra con tipo 2,
	 * pero es de hospitalizacion
	 */
	private static final String esSolicitudCuidadosEspeciales_Str = "SELECT "+ 
		" count(*) as total from solicitudes where numero_solicitud = ? "+
		" and tipo = "+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+ 
		" and interpretacion is null and fecha_interpretacion is null and hora_interpretacion is null and " +
		" numero_solicitud in (select numero_solicitud from valoraciones where cuidado_especial = '"+ConstantesBD.acronimoSi+"') "; 
	
	
	/**
	 * Consulta para visulizacion de valoraciones 
	 * de las observaciones
	 * mt 5749
	 * Alberto Ovalle 
	 */
	 private static final String obtenerVistaValoracionObservaciones_str ="select " +
	 		"vo.consecutivo, " + 
			"vo.numero_solicitud, " +
	 		" vo.tipo, " +
	 		" vo.valor, " +
	 		"to_char(fecha,'"+ConstantesBD.formatoFechaAp+"') as fecha_registro, " +
	 		" vo.hora, " +
	 		" vo.usuario " +
	 		"from valoracion_observaciones vo " +	 		
	 		"where vo.numero_solicitud = ? and vo.tipo <> ? " +
	 		"order by vo.fecha,vo.hora,vo.consecutivo";
	      
	
	 /**
		 * Consulta para visulizacion de valoraciones
		 * de la conducta 
		 * mt 5749
		 * Alberto Ovalle 
		 */
		 private static final String obtenerVistaValoracionConducta_str ="select vu.numero_solicitud, " +
		 		"vu.desc_conducta_valoracion " +
		 		"from valoraciones_urgencias vu " +	 		
		 		"where vu.numero_solicitud = ? "; 
		 		
		
	 
	 
	
	/**
	 * Método que inserta una nueva valoración
	 * @param con
	 * @param valoracionHospitalizacion
	 * @return
	 */
	public static ResultadoBoolean insertarHospitalizacion(Connection con,DtoValoracionHospitalizacion valoracionHospitalizacion)
	{
		//***********SE INSERTA VALORACION BÁSICA**************************************
		String actualizacionSolicitudes=insertarEspecialidadProfesionalRespondeStr;
		String observacionCapitacion = null;
		ResultadoBoolean resp = insertarBase(con, valoracionHospitalizacion, observacionCapitacion);
		PreparedStatementDecorator ps =null;
		PreparedStatementDecorator pst = null;
		Statement stmt = null;
		try
		{
			if(System.getProperty("TIPOBD").equals("POSTGRESQL"))
    		{
	        	stmt = con.createStatement();
	            stmt.execute("SET standard_conforming_strings TO off");
	           
    		}
			
			if(resp.isTrue())
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(insertarValoracionHospitalizacion_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				
				/**
				 * INSERT INTO val_hospitalizacion (" +
							"numero_solicitud," +
							"texto_origen_no_urgencias," +
							"codigo_origen_admision_hosp," +
							"diagnostico_ingreso," +
							"diagnostico_cie_ingreso" +
							") VALUES (?,?,?,?,?)
				 */
				
				pst.setInt(1,Utilidades.convertirAEntero(valoracionHospitalizacion.getNumeroSolicitud()));
				if(!valoracionHospitalizacion.getTextoOrigenNoUrgencias().equals(""))
					pst.setString(2,valoracionHospitalizacion.getTextoOrigenNoUrgencias());
				else
				pst.setNull(2,Types.VARCHAR);
				pst.setInt(3,valoracionHospitalizacion.getCodigoOrigenAdmision());
				pst.setString(4,valoracionHospitalizacion.getDiagnosticoIngreso().getAcronimo());
				pst.setInt(5,valoracionHospitalizacion.getDiagnosticoIngreso().getTipoCIE());
				
				if(pst.executeUpdate()<=0)
				{
					resp.setResultado(false);
					resp.setDescripcion("Problemas ingresando el registro de la valoración de hospitalización. Proceso Cancelado");
				}else
				{
					try{
						ps =  new PreparedStatementDecorator(con.prepareStatement(actualizacionSolicitudes, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));	
					    ps.setInt(1, Utilidades.convertirAEntero(valoracionHospitalizacion.getEspecialidadProfResponde()));
					    ps.setInt(2, Utilidades.convertirAEntero(valoracionHospitalizacion.getNumeroSolicitud()));
					    ps.executeUpdate();
					    
					}
					catch(SQLException e)
					{
						logger.error("Error en Actualizar Solicitud con la Especialidad : "+e);
						resp.setResultado(false);
						resp.setDescripcion("Problemas ingresando la valoración de hospitalización: "+e);
					}
					
					
				}
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarHospitalizacion: "+e);
			resp.setResultado(false);
			resp.setDescripcion("Problemas ingresando la valoración de hospitalización: "+e);
		}finally{			
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseDiagnosticosOdontologicosATratarDao " + sqlException.toString() );
				}
			}
			if (pst != null){
				try{
					pst.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseDiagnosticosOdontologicosATratarDao " + sqlException.toString() );
				}
			}
			 try {
				 if(stmt!=null){
					 stmt.close();
				 }
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return resp;
	}
	
	/**
	 * Método implementado para insertar una valoración de urgencias
	 * @param con
	 * @param valoracionUrgencias
	 * @return
	 */
	public static ResultadoBoolean insertarUrgencias(Connection con,DtoValoracionUrgencias valoracionUrgencias)
	{
		//***********SE INSERTA VALORACION BÁSICA**************************************
		String actualizacionSolicitudes=insertarEspecialidadProfesionalRespondeStr;
		String observacionCapitacion = null;
		ResultadoBoolean resp = insertarBase(con, valoracionUrgencias, observacionCapitacion);
		PreparedStatementDecorator pst =null;
		PreparedStatementDecorator ps = null;
		try
		{
			if(System.getProperty("TIPOBD").equals("POSTGRESQL"))
    		{
	        	Statement stmt = con.createStatement();
	            stmt.execute("SET standard_conforming_strings TO off");
	            stmt.close();
    		}
			if(resp.isTrue())
			{
				String insertarValoracionUrgenciasStr = "INSERT INTO valoraciones_urgencias ( "+
				"numero_solicitud, "+
				"estado_llegada, "+
				"desc_estado_llegada, "+
				"nombre_autorizador, "+
				"relacion_autorizador, "+
				"cod_est_con_llegada, "+
				"desc_estado_conciencia_llegada, "+
				"codigo_estado_conciencia, "+
				"desc_estado_conciencia, "+
				"codigo_conducta_valoracion," +
				"desc_conducta_valoracion, "+
				"codigo_categoria_triage, "+
				"desc_categoria_triage, "+
				"estado_embriaguez," +
				"tipo_monitoreo," +
				"fecha_modifica," +
				"hora_modifica," +
				"usuario_modifica "+
				") Values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?)";
				
				logger.info("insertar valoracion urgencias: "+insertarValoracionUrgenciasStr);
				pst =  new PreparedStatementDecorator(con.prepareStatement(insertarValoracionUrgenciasStr));
				
				
				pst.setInt(1,Utilidades.convertirAEntero(valoracionUrgencias.getNumeroSolicitud()));
				if(valoracionUrgencias.getEstadoLlegada()!=null)
				{
					if(valoracionUrgencias.getEstadoLlegada().booleanValue())
						pst.setObject(2, ValoresPorDefecto.getValorTrueCortoParaConsultas());
					else
						pst.setObject(2, ValoresPorDefecto.getValorFalseCortoParaConsultas());
				}
				else
					pst.setObject(2,null);
				
				if(!valoracionUrgencias.getDescripcionEstadoLlegada().equals(""))
					pst.setString(3,valoracionUrgencias.getDescripcionEstadoLlegada());
				else
					pst.setObject(3,null);
				
				if(!valoracionUrgencias.getNombreAutorizador().equals(""))
					pst.setString(4,valoracionUrgencias.getNombreAutorizador());
				else
					pst.setObject(4,null);
				
				if(!valoracionUrgencias.getRelacionAutorizador().equals(""))
					pst.setString(5,valoracionUrgencias.getRelacionAutorizador());
				else
					pst.setObject(5,null);
				
				if(valoracionUrgencias.getCodigoEstadoConcienciaLlegada()>0||
					valoracionUrgencias.getCodigoEstadoConcienciaLlegada()==ConstantesBD.codigoNuncaValido)
					pst.setInt(6,valoracionUrgencias.getCodigoEstadoConcienciaLlegada());
				else
					pst.setObject(6,null);
				
				if(!valoracionUrgencias.getDescripcionEstadoConcienciaLlegada().equals(""))
					pst.setString(7,valoracionUrgencias.getDescripcionEstadoConcienciaLlegada());
				else
					pst.setObject(7,null);
				
				if(valoracionUrgencias.getCodigoEstadoConciencia()>0||
					valoracionUrgencias.getCodigoEstadoConciencia()==ConstantesBD.codigoNuncaValido)
					pst.setInt(8,valoracionUrgencias.getCodigoEstadoConciencia());
				else
					pst.setObject(8,null);
				
				if(!valoracionUrgencias.getDescripcionEstadoConciencia().equals(""))
					pst.setString(9,valoracionUrgencias.getDescripcionEstadoConciencia());
				else
					pst.setObject(9,null);
				
				if(valoracionUrgencias.getCodigoConductaValoracion()>0)
					pst.setInt(10,valoracionUrgencias.getCodigoConductaValoracion());
				else
					pst.setObject(10,null);
				//Se editan la descripcion de la conducta a seguir de manera especial
				pst.setString(11,agregarDatosControlUsuarioConductaValoracion(con, valoracionUrgencias));
				
				if(valoracionUrgencias.getCodigoCategoriaTriage()>0)
					pst.setInt(12,valoracionUrgencias.getCodigoCategoriaTriage());
				else
					pst.setObject(12,null);
				
				if(!valoracionUrgencias.getDescripcionCategoriaTriage().equals(""))
					pst.setString(13,valoracionUrgencias.getDescripcionCategoriaTriage());
				else
					pst.setObject(13,null);
				
				if(valoracionUrgencias.getEstadoEmbriaguez()!=null)
				{
					if(valoracionUrgencias.getEstadoEmbriaguez().booleanValue())
						pst.setObject(14, ValoresPorDefecto.getValorTrueCortoParaConsultas());
					else
						pst.setObject(14, ValoresPorDefecto.getValorFalseCortoParaConsultas());
				}
				else
					pst.setObject(14,null);
				
				if(valoracionUrgencias.getCodigoTipoMonitoreo()>0)
					pst.setInt(15,valoracionUrgencias.getCodigoTipoMonitoreo());
				else
					pst.setObject(15,null);
				
				pst.setString(16,valoracionUrgencias.getProfesional().getLoginUsuario());
				
				if(pst.executeUpdate()<=0)
				{
					resp.setResultado(false);
					resp.setDescripcion("Problemas ingresando el registro de valoración urgencias. Proceso cancelado");
				}
				else
				{
					logger.error("\n\nNUMERO SOLICITUD VALORACION URGENCIAS  : "+valoracionUrgencias.getNumeroSolicitud()+" Especialidad : "+valoracionUrgencias.getEspecialidadResponde());
					try{
						ps =  new PreparedStatementDecorator(con.prepareStatement(actualizacionSolicitudes));	
					    ps.setInt(1, Utilidades.convertirAEntero(valoracionUrgencias.getEspecialidadResponde()));
					    ps.setInt(2, Utilidades.convertirAEntero(valoracionUrgencias.getNumeroSolicitud()));
					    ps.executeUpdate();
					    
					}
					catch(SQLException e)
					{
						logger.error("Error en Actualizar Solicitud con la Especialidad : "+e);
						resp.setResultado(false);
						resp.setDescripcion("Problemas ingresando la valoración de Urgencias");
					}
					
					
				}
				
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarUrgencias: "+e);
			e.printStackTrace();
			resp.setResultado(false);
			resp.setDescripcion("Problemas insertando la valoración de urgencias: "+e);
		}finally{			
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseDiagnosticosOdontologicosATratarDao " + sqlException.toString() );
				}
			}
			if (pst != null){
				try{
					pst.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseDiagnosticosOdontologicosATratarDao " + sqlException.toString() );
				}
			}
		}
		return resp;
	}
	
	/**
	 *
	 * @param con 
	 * @param usuarioMedico
	 * @param cadena
	 * @return
	 */
	private static String agregarDatosControlUsuarioConductaValoracion(Connection con, DtoValoracionUrgencias valoracionUrgencias)
	{
		valoracionUrgencias.setNombreConductaValoracion(UtilidadesHistoriaClinica.obtenerNombreConductaValoracion(con, valoracionUrgencias.getCodigoConductaValoracion()));
		String cadena = valoracionUrgencias.getNombreConductaValoracion() + (valoracionUrgencias.getDescripcionConductaValoracion().equals("")?"":" : "+valoracionUrgencias.getDescripcionConductaValoracion());
		
		String fechaSistema = UtilidadFecha.getFechaActual(con);
		String horaSistema = UtilidadFecha.getHoraActual(con); 
		
		//Obteniendo los datos del usuario de la salud
		String nombreMedico = valoracionUrgencias.getProfesional().getNombreUsuario();
		String registroMedico = valoracionUrgencias.getProfesional().getNumeroRegistroMedico();
		String especialidadesMedico = "";
		if((valoracionUrgencias.getProfesional().getEspecialidades() != null)&& (valoracionUrgencias.getProfesional().getEspecialidades().length > 0))
		{
			for(int i=0; i<valoracionUrgencias.getProfesional().getEspecialidades().length; i++)
				especialidadesMedico+=valoracionUrgencias.getProfesional().getEspecialidades()[i].getNombre()+", ";
			especialidadesMedico = ":"+especialidadesMedico.substring(0,especialidadesMedico.length()-2);
		}
		//retornando la cadena con los datos de control. El formato es:
		//<fecha>|<hora>|<nombreMedico>:<especialidades separadas por coma>|<numRegistro>|<observacion>
		return ""+fechaSistema+"|"+horaSistema+"|"+nombreMedico+especialidadesMedico+"|"+registroMedico+"|"+cadena;
	}
	
	/**
	 * Método que realiza la inserción de una valoración base
	 * @param con
	 * @param valoracionBase
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static ResultadoBoolean insertarBase(Connection con,DtoValoracion valoracionBase, String observacionCapitacion)
	{
		ResultadoBoolean resp = new ResultadoBoolean(true);
		PreparedStatementDecorator pst = null;
		try
		{
			if(System.getProperty("TIPOBD").equals("POSTGRESQL"))
    		{
	        	Statement stmt = con.createStatement();
	            stmt.execute("SET standard_conforming_strings TO off");
	            stmt.close();
    		}
			
			String fechaActual = UtilidadFecha.getFechaActual(con);
			String horaActual = UtilidadFecha.getHoraActual(con);
			
			//*******************************SE INSERTA LA VALORACION BASE***********************************************************************
			pst =  new PreparedStatementDecorator(con.prepareStatement(insertarValoracionBase_Str));
			pst.setInt(1,Utilidades.convertirAEntero(valoracionBase.getNumeroSolicitud()));
			if(!valoracionBase.getEdad().equals(""))
				pst.setString(2,valoracionBase.getEdad());
			else
				pst.setNull(2,Types.VARCHAR);
			if(!valoracionBase.getFechaGrabacion().equals(""))
				pst.setDate(3,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(valoracionBase.getFechaGrabacion())));
			else
				pst.setDate(3,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaActual)));
			
			if(!valoracionBase.getHoraGrabacion().equals(""))
				pst.setString(4,valoracionBase.getHoraGrabacion());
			else
				pst.setString(4,horaActual);
			
			pst.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(valoracionBase.getFechaValoracion())));
			
			pst.setString(6,valoracionBase.getHoraValoracion());
			if(valoracionBase.getCodigoCausaExterna()>=0)
				pst.setInt(7,valoracionBase.getCodigoCausaExterna());
			else
				pst.setNull(7,Types.INTEGER);
			if(valoracionBase.getProfesional().getCodigoPersona()>0)
				pst.setInt(8,valoracionBase.getProfesional().getCodigoPersona());
			else
				pst.setNull(8,Types.INTEGER);
			if(!valoracionBase.getControlPostOperatorio().equals(""))
				pst.setInt(9,Utilidades.convertirAEntero(valoracionBase.getControlPostOperatorio()));
			else
				pst.setNull(9,Types.INTEGER);
			
			pst.setString(10,valoracionBase.isCuidadoEspecial()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			
			pst.setString(11,observacionCapitacion);
			
			if(pst.executeUpdate()<=0)
			{
				resp.setResultado(false);
				resp.setDescripcion("Problemas insertando encabezado de valoración. Proceso cancelado");
			}
				
			//*****************************************************************************************************
			
			
			//*************SE INSERTAN LAS OBSERVACIONES DE LA VALORACION*************************************
			if(resp.isTrue())
				resp = insertarObservaciones(con, valoracionBase.getObservaciones(), valoracionBase.getNumeroSolicitud(), fechaActual, horaActual);
			//************************************************************************************************
			
			//**********SE INSERTAN LOS DIAGNÓSTICOS DE LA VALORACION******************************************
			for(Diagnostico diagnostico:valoracionBase.getDiagnosticos())
			{
				if(resp.isTrue())
				{
					logger.info("valor de diag: "+valoracionBase.getNumeroSolicitud()+" "+diagnostico.getAcronimo()+" "+diagnostico.getTipoCIE()+" "+diagnostico.isPrincipal()+" "+diagnostico.isDefinitivo()+" "+(diagnostico.isPrincipal()?0:diagnostico.getNumero()));
					pst =  new PreparedStatementDecorator(con.prepareStatement(insertarDiagnosticoValoracion_Str));
					
					pst.setInt(1,Utilidades.convertirAEntero(valoracionBase.getNumeroSolicitud()));
					pst.setString(2,diagnostico.getAcronimo());
					pst.setInt(3,diagnostico.getTipoCIE());
					pst.setBoolean(4,diagnostico.isPrincipal());
					pst.setBoolean(5, diagnostico.isDefinitivo());
					pst.setInt(6,diagnostico.isPrincipal()?0:diagnostico.getNumero());
					
					if(pst.executeUpdate()<=0)
					{
						resp.setResultado(false);
						resp.setDescripcion("Problemas insertando el diagnóstico "+diagnostico.getAcronimo()+"-"+diagnostico.getTipoCIE()+". Proceso cancelado");
					}
				}
			}
			//*************************************************************************************************
			
			//********SE INSERTAN LAS REVISIONES POR SISTEMAS UNICAS (que no tienen opciones)********************
			for(DtoRevisionSistema revisionSistema:valoracionBase.getRevisionesSistemas())
				//Tiene que ser una revisión que no es de opciones  y se haya ingresado informacion
				if(resp.isTrue() && !revisionSistema.isMultiple() && (!revisionSistema.getValor().equals("")||revisionSistema.getEstadoNormal()!=null||!revisionSistema.getDescripcion().equals("")))
				{
					pst =  new PreparedStatementDecorator(con.prepareStatement(insertarRevisionXSistemaUnico_str));
					
					/**
					 * INSERT INTO val_revisiones_sistemas (valoracion,revision_sistema,descripcion,valor,estado_normal) values (?,?,?,?,?)
					 */
					
					pst.setInt(1,Utilidades.convertirAEntero(valoracionBase.getNumeroSolicitud()));
					pst.setInt(2,revisionSistema.getCodigo());
					pst.setString(3,revisionSistema.getDescripcion());
					if(Utilidades.convertirADouble(revisionSistema.getValor())>=0)
						pst.setDouble(4, Utilidades.convertirADouble(revisionSistema.getValor()));
					else
						pst.setNull(4, Types.DOUBLE);
					if(revisionSistema.getEstadoNormal()!=null)
						pst.setBoolean(5, revisionSistema.getEstadoNormal());
					else
						pst.setObject(5,null);
					if(pst.executeUpdate()<=0)
					{
						resp.setResultado(false);
						resp.setDescripcion("Problemas insertando la revisión por sistema "+revisionSistema.getNombre()+". Proceso cancelado");
					}
				}
			//***************************************************************************************************
			
			//*********	SE INSERTAN LAS REVISIONES POR SISTEMAS MÚLTIPLES (con opciones)**********************
			for(DtoRevisionSistema revisionSistema:valoracionBase.getRevisionesSistemas())
				//Tiene que ser una revisión es de opciones  y se haya ingresado opciones
				if(resp.isTrue() && revisionSistema.isMultiple() && revisionSistema.tieneOpcionesActivadas())
				{
					pst =  new PreparedStatementDecorator(con.prepareStatement(insertarRevisionXSistemaMultipleEnc_Str));
					
					/**
					 * INSERT INTO val_rev_sistemas_enc (valoracion,tipo_rev_sistema,descripcion,estado_normal) VALUES (?,?,?,?)
					 */
					
					pst.setInt(1,Utilidades.convertirAEntero(valoracionBase.getNumeroSolicitud()));
					pst.setInt(2,revisionSistema.getCodigo());
					pst.setString(3,revisionSistema.getDescripcion());
					if(revisionSistema.getEstadoNormal()!=null)
						pst.setBoolean(4, revisionSistema.getEstadoNormal());
					else
						pst.setObject(4,null);
					if(pst.executeUpdate()<=0)
					{
						resp.setResultado(false);
						resp.setDescripcion("Problemas insertando la revisión "+revisionSistema.getNombre()+". Proceso cancelado");
					}
						
					
					if(resp.isTrue())
					{
						for(InfoDatosInt opcion:revisionSistema.getOpciones())
							//SE verifica que la opcion se haya activado
							if(resp.isTrue()&&UtilidadTexto.getBoolean(opcion.getActivoStr()))
							{
								pst =  new PreparedStatementDecorator(con.prepareStatement(insertarRevisionXSistemaMultipleOp_Str));
								pst.setInt(1,Utilidades.convertirAEntero(valoracionBase.getNumeroSolicitud()));
								pst.setInt(2,opcion.getCodigo());
								
								if(pst.executeUpdate()<=0)
								{
									resp.setResultado(false);
									resp.setDescripcion("Problemas insertando la opción "+opcion.getNombre()+" de la revisión por sistema "+revisionSistema.getNombre()+". Proceso cancelado");
								}
							}
					}
					
				}
			
			//*************************************************************************************************
			
			//********* SE INSERTAN LOS SIGNOS VITALES*****************************************************
			for(SignoVital signoVital:valoracionBase.getSignosVitales())
				if(resp.isTrue()&&!signoVital.getValorSignoVital().equals(""))
				{
					pst =  new PreparedStatementDecorator(con.prepareStatement(insertarSignoVital_Str));
					
					/**
					 * INSERT INTO val_signos_vitales (valoracion,signo_vital,descripcion,valor,estado_normal) VALUES (?,?,?,?,?)
					 */
					
					pst.setInt(1,Utilidades.convertirAEntero(valoracionBase.getNumeroSolicitud()));
					pst.setInt(2,signoVital.getCodigo());
					pst.setString(3,signoVital.getDescripcion());
					pst.setString(4,signoVital.getValorSignoVital());
					if(signoVital.getEstadoNormal()!=null)
						pst.setBoolean(5, signoVital.getEstadoNormal().booleanValue());
					else
						pst.setObject(5,null);
					if(pst.executeUpdate()<=0)
					{
						resp.setResultado(false);
						resp.setDescripcion("Problemas insertando el signo vital "+signoVital.getNombre()+". Proceso cancelado");
					}
					
				}
			//********************************************************************************************
			
			//********SE INSERTA VALORACION CONSULTA***************************************************
			if(resp.isTrue())
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(insertarValoracionConsulta_Str));
				
				/**
				 * INSERT INTO valoraciones_consulta (" +
						"numero_solicitud," +
						"primera_vez," +
						"finalidad_consulta," +
						"tipo_diagnostico_principal," +
						"tipo_recargo," +
						"fecha_proximo_control," +
						"num_dias_incapacidad," +
						"observaciones_incapacidad" +
						") VALUES (?,?,?,?,?,?,?,?)
				 */
				
				pst.setInt(1,Utilidades.convertirAEntero(valoracionBase.getNumeroSolicitud()));
				
				if(valoracionBase.getValoracionConsulta().getPrimeraVez()!=null)
					pst.setBoolean(2, valoracionBase.getValoracionConsulta().getPrimeraVez().booleanValue());
				else
					pst.setObject(2,null);
				
				if(!valoracionBase.getValoracionConsulta().getCodigoFinalidadConsulta().equals(""))
					pst.setString(3,valoracionBase.getValoracionConsulta().getCodigoFinalidadConsulta());
				else
					pst.setNull(3, Types.CHAR);
				
				if(valoracionBase.getValoracionConsulta().getCodigoTipoDiagnostico()>0)
					pst.setInt(4,valoracionBase.getValoracionConsulta().getCodigoTipoDiagnostico());
				else
					pst.setNull(4,Types.INTEGER);
				
				if(valoracionBase.getValoracionConsulta().getCodigoTipoRecargo()>0)
					pst.setInt(5,valoracionBase.getValoracionConsulta().getCodigoTipoRecargo());
				else
					pst.setInt(5,ConstantesBD.codigoTipoRecargoSinRecargo);
				
				if(!valoracionBase.getValoracionConsulta().getFechaProximoControl().equals(""))
					pst.setDate(6, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(valoracionBase.getValoracionConsulta().getFechaProximoControl())));
				else
					pst.setNull(6,Types.DATE);
				
				if(Utilidades.convertirAEntero(valoracionBase.getValoracionConsulta().getNumeroDiasIncapacidad(),true)>0)
					pst.setInt(7,Utilidades.convertirAEntero(valoracionBase.getValoracionConsulta().getNumeroDiasIncapacidad()));
				else
					pst.setNull(7,Types.INTEGER);
				
				if(!valoracionBase.getValoracionConsulta().getObservacionesIncapacidad().equals(""))
					pst.setString(8,valoracionBase.getValoracionConsulta().getObservacionesIncapacidad());
				else
					pst.setNull(8,Types.VARCHAR);
				
				if(pst.executeUpdate()<=0)
				{
					resp.setResultado(false);
					resp.setDescripcion("Problemas insertando la valoración consulta. Proceso cancelado");
				}
			}
			//*****************************************************************************************
			
			//*************SE INSERTAN LOS ARCHIVOS ADJUNTOS*********************************************
			//Utilidades.imprimirMapa(valoracionBase.getValoracionConsulta().getArchivosAdjuntos());
			for(int i=0;i<valoracionBase.getValoracionConsulta().getNumArchivosAdjuntos();i++)
				//Se verifica que el archivo no se haya borrado y que la transaccion vaya bien
				if(!UtilidadTexto.isEmpty(valoracionBase.getValoracionConsulta().getArchivosAdjuntos("adjOriginalValoracion_"+i)+"")&&resp.isTrue())
				{
					pst =  new PreparedStatementDecorator(con.prepareStatement(insertarDocumentoAdjuntoStr));
					
					/**
					 * INSERT INTO doc_adj_solicitud (" +
							"codigo," +
							"numero_solicitud," +
							"nombre_archivo," +
							"nombre_original," +
							"es_solicitud," +
							"codigo_medico," +
							"es_codigo_resp_sol" +
							") values (?,?,?,?,"+ValoresPorDefecto.getValorFalseParaConsultas()+",?,?)
					 */
					
					int consecutivoAdjuntos = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_doc_adj_solicitud");
					
					pst.setInt(1,consecutivoAdjuntos);
					pst.setInt(2,Utilidades.convertirAEntero(valoracionBase.getNumeroSolicitud()));
					pst.setString(3,valoracionBase.getValoracionConsulta().getArchivosAdjuntos("adjValoracion_"+i).toString());
					pst.setString(4,valoracionBase.getValoracionConsulta().getArchivosAdjuntos("adjOriginalValoracion_"+i).toString());
					pst.setInt(5,valoracionBase.getProfesional().getCodigoPersona());
					pst.setNull(6,Types.NUMERIC); //no se asigna codigo de respuesta de procedimientos
					
					if(pst.executeUpdate()<=0)
					{
						resp.setResultado(false);
						resp.setDescripcion("Problemas insertando el documento adjunto "+valoracionBase.getValoracionConsulta().getArchivosAdjuntos("adjOriginalValoracion_"+i).toString()+". Proceso cancelado");
					}
				}
			//*******************************************************************************************
			
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarBase: "+e);
			e.printStackTrace();
			resp.setResultado(false);
			resp.setDescripcion("Error al insertar valoracion básica: "+e);
		}
		finally{			
			if (pst != null){
				try{
					pst.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso  " + sqlException.toString() );
				}
			}
			
		}
		return resp;
	}
	
	
	/**
	 * Método implementado para insertar observaciones
	 * @param con
	 * @param observaciones
	 * @param numeroSolicitud
	 * @param fechaActual
	 * @param horaActual
	 * @return
	 */
	public static ResultadoBoolean insertarObservaciones(Connection con, ArrayList<DtoValoracionObservaciones> observaciones, String numeroSolicitud, String fechaActual, String horaActual)
	{
		ResultadoBoolean resp = new ResultadoBoolean(true,"");
		PreparedStatementDecorator pst = null;
		try
		{
			if(System.getProperty("TIPOBD").equals("POSTGRESQL"))
    		{
	        	Statement stmt = con.createStatement();
	            stmt.execute("SET standard_conforming_strings TO off");
	            stmt.close();
    		}
			
			//*************SE INSERTAN LAS OBSERVACIONES DE LA VALORACION*************************************
			for(DtoValoracionObservaciones valObservacion:observaciones)
				//Se verifica que se hayan insertado las observaciones
				if(resp.isTrue()&&!valObservacion.getValor().equals("")&&valObservacion.getConsecutivo().equals(""))
				{
					pst =  new PreparedStatementDecorator(con.prepareStatement(insertarObservacionValoracion_Str));
					
					/**
					 * INSERT INTO valoracion_observaciones (" +
							"consecutivo," +
							"numero_solicitud," +
							"tipo," +
							"valor," +
							"fecha," +
							"hora," +
							"usuario" +
							") values (?,?,?,?,?,?,?)
					 */
					
					int secuenciaObservacion = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_val_observaciones");
					pst.setDouble(1,Utilidades.convertirADouble(secuenciaObservacion+""));
					pst.setInt(2,Utilidades.convertirAEntero(numeroSolicitud));
					pst.setString(3,valObservacion.getTipo());
					pst.setString(4,valObservacion.getValor());
					
					if(!valObservacion.getFecha().equals(""))
						pst.setDate(5,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(valObservacion.getFecha())));
					else
						pst.setDate(5,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaActual)));
					
					if(!valObservacion.getHora().equals(""))
						pst.setString(6,valObservacion.getHora());
					else
						pst.setString(6,horaActual);
					pst.setString(7,valObservacion.getProfesional().getLoginUsuario());
					
					if(pst.executeUpdate()<=0)
					{
						resp.setResultado(false);
						resp.setDescripcion("Problemas insertando las observaciones de "+ValoresPorDefecto.getIntegridadDominio(valObservacion.getTipo())+". Proceso cancelado");
					}
				}
			//************************************************************************************************
			
		}
		catch(SQLException e)
		{
			logger.error("Error insertarObservaciones:"+e);
			e.printStackTrace();
		}finally{			
			if (pst != null){
				try{
					pst.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseDiagnosticosOdontologicosATratarDao " + sqlException.toString() );
				}
			}
			
		}
		
		return resp;
	}
	
	/**
	 * Método para cargar la valoracion de hospitalizacion
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static DtoValoracionHospitalizacion cargarHospitalizacion(Connection con,String numeroSolicitud)
	{
		DtoValoracionHospitalizacion valoracionHospitalizacion = (DtoValoracionHospitalizacion)cargarBase(con, numeroSolicitud,false);
		PreparedStatementDecorator pst  = null;
		ResultSetDecorator rs  = null;
		try
		{
			//*****************SE CARGA DATOS DE LA VALORACION DE URGENCIAS**********************************************+
			pst =  new PreparedStatementDecorator(con.prepareStatement(cargarHospitalizacion_Str)); 
			pst.setInt(1,Utilidades.convertirAEntero(valoracionHospitalizacion.getNumeroSolicitud()));
			rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				valoracionHospitalizacion.setTextoOrigenNoUrgencias(rs.getString("texto_origen_no_urgencias"));
				valoracionHospitalizacion.setCodigoOrigenAdmision(rs.getInt("codigo_origen_admision_hosp"));
				valoracionHospitalizacion.setNombreOrigenAdmision(rs.getString("nombre_origen_admision_hosp"));
				valoracionHospitalizacion.getDiagnosticoIngreso().setAcronimo(rs.getString("acronimo"));
				valoracionHospitalizacion.getDiagnosticoIngreso().setTipoCIE(rs.getInt("tipo_cie"));
				valoracionHospitalizacion.getDiagnosticoIngreso().setNombre(rs.getString("nombre_diagnostico"));
				
			}
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
			//************************************************************************************************************
			
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarHospitalizacion: "+e);
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
		}
		
		return valoracionHospitalizacion;
	}
	
	
	/**
	 * Método para cargar la valoración de urgencias
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static DtoValoracionUrgencias cargarUrgencias(Connection con,String numeroSolicitud)
	{
		DtoValoracionUrgencias valoracionUrgencias = (DtoValoracionUrgencias)cargarBase(con, numeroSolicitud,true);
		PreparedStatementDecorator pst =  null;
		ResultSetDecorator rs = null;
		try
		{
			String aux = "";
			//*****************SE CARGA DATOS DE LA VALORACION DE URGENCIAS********************************************
			pst =  new PreparedStatementDecorator(con.prepareStatement(cargarUrgencias_Str));
			pst.setInt(1,Utilidades.convertirAEntero(valoracionUrgencias.getNumeroSolicitud()));
			rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				aux="";
				if(rs.getString("estado_llegada")!=null)
				{
					aux = rs.getString("estado_llegada");
				}
				if(!aux.equals(""))
					valoracionUrgencias.setEstadoLlegada(UtilidadTexto.getBoolean(aux));
				valoracionUrgencias.setDescripcionEstadoLlegada(rs.getString("descripcion_estado_llegada"));
				valoracionUrgencias.setNombreAutorizador(rs.getString("nombre_autorizador"));
				valoracionUrgencias.setRelacionAutorizador(rs.getString("relacion_autorizador"));
				valoracionUrgencias.setNombreAutorizadorAnterior(valoracionUrgencias.getNombreAutorizador());
				valoracionUrgencias.setRelacionAutorizadorAnterior(valoracionUrgencias.getRelacionAutorizador());
				
				valoracionUrgencias.setCodigoEstadoConcienciaLlegada(rs.getInt("cod_est_con_llegada"));
				valoracionUrgencias.setNombreEstadoConcienciaLlegada(rs.getString("nom_est_con_llegada"));
				valoracionUrgencias.setDescripcionEstadoConcienciaLlegada(rs.getString("desc_estado_conciencia_llegada"));
				valoracionUrgencias.setCodigoEstadoConciencia(rs.getInt("codigo_estado_conciencia"));
				valoracionUrgencias.setNombreEstadoConciencia(rs.getString("nombre_estado_conciencia"));
				valoracionUrgencias.setDescripcionEstadoConciencia(rs.getString("desc_estado_conciencia"));
				valoracionUrgencias.setCodigoConductaValoracion(rs.getInt("codigo_conducta_valoracion"));
				valoracionUrgencias.setNombreConductaValoracion(rs.getString("nombre_conducta_valoracion"));
				valoracionUrgencias.setCodigoConductaValoracionAnterior(valoracionUrgencias.getCodigoConductaValoracion());
				valoracionUrgencias.setNombreConductaValoracionAnterior(valoracionUrgencias.getNombreConductaValoracion());
				
				valoracionUrgencias.setDescripcionConductaValoracion(rs.getString("desc_conducta_valoracion"));
				valoracionUrgencias.setCodigoCategoriaTriage(rs.getInt("codigo_categoria_triage"));
				valoracionUrgencias.setNombreCategoriaTriage(rs.getString("desc_categoria_triage"));
				aux="";
				if(rs.getString("estado_embriaguez")!=null)
				{
					aux = rs.getString("estado_embriaguez");
				}
				if(!aux.equals(""))
					valoracionUrgencias.setEstadoEmbriaguez(UtilidadTexto.getBoolean(aux));
				valoracionUrgencias.setCodigoTipoMonitoreo(rs.getInt("codigo_tipo_monitoreo"));
				valoracionUrgencias.setNombreTipoMonitoreo(rs.getString("nombre_tipo_monitoreo"));
				valoracionUrgencias.setFechaModifica(rs.getString("fecha_modifica"));
				valoracionUrgencias.setHoraModifica(rs.getString("hora_modifica"));
				
				cargarHistoricoConductasValoracion(con,valoracionUrgencias);
				
			}
			//**********************************************************************************************************
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarUrgencias: "+e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
		}
		
		return valoracionUrgencias;
	}
	
	/**
	 * Método para cargar el histórico de las conductas de valoración
	 * @param con
	 * @param valoracionUrgencias
	 */
	private static void cargarHistoricoConductasValoracion(Connection con, DtoValoracionUrgencias valoracionUrgencias) 
	{
		
		/*
		   Antes de mostrar la cadena de observaciones se debe interpretar la informacion que esta guardada, teniendo en cuenta
		   que las observaciones se guardan en el siguiente formato :
		   <fecha>|<hora>|<nombreMedico>|<numRegistro>|<observacion>_&_<fecha>|<hora>|<nombreMedico>|<numRegistro>|<observacion>
		*/
		String observaciones = valoracionUrgencias.getDescripcionConductaValoracion();
		StringBuffer observacionesFinales = new StringBuffer();
		
		if(!observaciones.equals(""))
		{
			//Se reemplaza el símbolo _&_ por @
			observaciones = observaciones.replaceAll("_&_", "@");
			
			StringTokenizer observacionesToken = new StringTokenizer(observaciones, "@");

			while (observacionesToken.hasMoreTokens()) 
			{
				//Se extrae cada resumen de conducta valoracion
				String elemento = observacionesToken.nextToken();

				StringTokenizer datosElemento = new StringTokenizer(elemento, "|");
				
				//Se toma la fecha de grabacion
				String fecha = "";
				if(datosElemento.hasMoreTokens())
					fecha = datosElemento.nextToken();
				//SE toma la hora de grabacion
				String hora = "";
				if(datosElemento.hasMoreTokens())
					hora = datosElemento.nextToken();
				//El medico va a tener las especialidades, formato:
				//[nombre_medico]:[especi1], [espe2]
				// Estos dos puntos aparecen en caso de que tenga especialidades
				String profesional = "";
				if(datosElemento.hasMoreTokens())
					profesional = datosElemento.nextToken();
				
				//Se verifica si el médico tenía especialidades para insertarlas mejor
				String especialidades = "";
				if(profesional.indexOf(':') != -1)
				{
					especialidades = ". Especialidades: " + profesional.substring(profesional.indexOf(':')+1);
					profesional = profesional.substring(0, profesional.indexOf(':'));
				}
				
				///El registro del medico incluye el texto 'RM' y 'MP' que indica si el numero es un Registro medico
				//o una matriculo profesional
				String registroMedico = "";
				if(datosElemento.hasMoreTokens())
					registroMedico = datosElemento.nextToken();
				
				//Se sacan las observaciones
				String observacion = "";
				if(datosElemento.hasMoreTokens())
					observacion = datosElemento.nextToken();
				
				//OJO arreglar el formato en que se va a mostrar
				observacionesFinales.append(fecha).append(", ").append(hora).append("\n").append(observacion).append("\n").append(profesional).append(". ").append(registroMedico).append(especialidades).append(". ").append("\n\n");

			}
		}
		
		valoracionUrgencias.setHistoricoConductasValoracion(observacionesFinales);
		valoracionUrgencias.setDescripcionConductaValoracion("");
		
	}
	
	/**
	 * Método para cargar las observaciones
	 * @param con
	 * @param numeroSolicitud
	 */
	public static List<DtoValoracionObservaciones> cargarObservaciones (Connection con,String numeroSolicitud){
		
		PreparedStatementDecorator pst = null;
		ResultSetDecorator rs = null;
		List<DtoValoracionObservaciones> observacionesList = null;
		
		try {
		//************************************************************************************************************************
		//***************SE CARGA OBSERVACIONES*****************************************************************************
		pst =  new PreparedStatementDecorator(con.prepareStatement(cargarObservaciones_Str));
		pst.setInt(1,Utilidades.convertirAEntero(numeroSolicitud));
		rs = new ResultSetDecorator(pst.executeQuery());
		
		observacionesList = new ArrayList<DtoValoracionObservaciones>() ;
		
		while(rs.next())
		{
			DtoValoracionObservaciones observaciones = new DtoValoracionObservaciones();
			observaciones.setConsecutivo(rs.getString("consecutivo"));
			observaciones.setTipo(rs.getString("tipo"));
			observaciones.setLabel(ValoresPorDefecto.getIntegridadDominio(observaciones.getTipo()).toString());
			observaciones.setValor(rs.getString("valor"));
			observaciones.setFecha(rs.getString("fecha_registro"));
			observaciones.setHora(rs.getString("hora"));
			observaciones.getProfesional().cargarUsuarioBasico(con, rs.getString("usuario"));
			observacionesList.add(observaciones);
		}
		} catch (SQLException e) {
			logger.error("SQLException: cargarObservaciones: " + e);
		} catch(Exception ex){
			logger.error("Exception: cargarObservaciones: " + ex);
		}finally {
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
		}
		return observacionesList;
	}

	/**
	 * Método implementado para cargar la valoracion base
	 * @param con
	 * @param numeroSolicitud
	 * @param esUrgencias 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static DtoValoracion cargarBase(Connection con,String numeroSolicitud, Boolean esUrgencias)
	{
		DtoValoracion valoracionBase = null;
		//logger.info("\n\n numero solicitud >> "+numeroSolicitud);
		
		if(esUrgencias == null)
			valoracionBase = new DtoValoracion();
		else if(esUrgencias.booleanValue())
			valoracionBase = new DtoValoracionUrgencias();
		else
			valoracionBase = new DtoValoracionHospitalizacion();
		
		/*PreparedStatementDecorator pst = null; 
		PreparedStatementDecorator pst1 = null;
		ResultSetDecorator rs = null;
		ResultSetDecorator rs1 = null;*/
		try
		{
			String aux = "";
			
			//****************SE CARGA VALORACION BASE************************************************************************
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarBase_Str));
			pst.setInt(1,Utilidades.convertirAEntero(numeroSolicitud));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				valoracionBase.setNumeroSolicitud(rs.getString("numero_solicitud"));
				//valoracionBase.setIdCuenta(rs.getInt("cuenta"));
				valoracionBase.setEdad(rs.getString("edad"));
				valoracionBase.setFechaGrabacion(rs.getString("fecha_grabacion"));
				valoracionBase.setHoraGrabacion(rs.getString("hora_grabacion"));
				valoracionBase.setFechaValoracion(rs.getString("fecha_valoracion"));
				valoracionBase.setHoraValoracion(rs.getString("hora_valoracion"));
				valoracionBase.setCodigoCausaExterna(rs.getInt("codigo_causa_externa"));
				valoracionBase.setNombreCausaExterna(rs.getString("nombre_causa_externa"));
				valoracionBase.getProfesional().cargarUsuarioBasico(con, rs.getInt("codigo_medico"));
				valoracionBase.setEspecialidadProfResponde(rs.getString("especialidad_solicitada"));
			}
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
			//************************************************************************************************************************
			//***************SE CARGA OBSERVACIONES*****************************************************************************
			PreparedStatementDecorator pst2 =  new PreparedStatementDecorator(con.prepareStatement(cargarObservaciones_Str));
			pst2.setInt(1,Utilidades.convertirAEntero(numeroSolicitud));
			ResultSetDecorator rs2 = new ResultSetDecorator(pst2.executeQuery());
			
			while(rs2.next())
			{
				DtoValoracionObservaciones observaciones = new DtoValoracionObservaciones();
				observaciones.setConsecutivo(rs2.getString("consecutivo"));
				observaciones.setTipo(rs2.getString("tipo"));
				observaciones.setLabel(ValoresPorDefecto.getIntegridadDominio(observaciones.getTipo()).toString());
				observaciones.setValor(rs2.getString("valor"));
				observaciones.setFecha(rs2.getString("fecha_registro"));
				observaciones.setHora(rs2.getString("hora"));
				observaciones.getProfesional().cargarUsuarioBasico(con, rs2.getString("usuario"));
				valoracionBase.getObservaciones().add(observaciones);
			}
			UtilidadBD.cerrarObjetosPersistencia(pst2, rs2, null);
			//******************************************************************************************************************
			//************SE CARGA DIAGNOSTICOS VALORACION**********************************************************************
			pst =  new PreparedStatementDecorator(con.prepareStatement(cargarDiagnosticos_Str));
			pst.setInt(1,Utilidades.convertirAEntero(numeroSolicitud));
			rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{
				Diagnostico diagnostico = new Diagnostico();
				diagnostico.setAcronimo(rs.getString("acronimo"));
				diagnostico.setTipoCIE(rs.getInt("tipo_cie"));
				diagnostico.setNombre(rs.getString("nombre_diagnostico"));
				diagnostico.setPrincipal(rs.getBoolean("principal"));
				diagnostico.setDefinitivo(rs.getBoolean("definitivo"));
				diagnostico.setNumero(rs.getInt("numero"));
				valoracionBase.getDiagnosticos().add(diagnostico);
			}
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
			//*******************************************************************************************************************
			//************SE CARGAN LAS REVISION POR SISTEMAS UNICAS (que no son por opciones)**********************************
			pst =  new PreparedStatementDecorator(con.prepareStatement(cargarRevisionXSistemasUnico_Str));
			pst.setInt(1,Utilidades.convertirAEntero(numeroSolicitud));
			rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{
				DtoRevisionSistema revisionSistema =new DtoRevisionSistema();
				revisionSistema.setMultiple(false); //no es por opciones
				
				revisionSistema.setCodigo(rs.getInt("codigo"));
				revisionSistema.setDescripcion(rs.getString("descripcion"));
				revisionSistema.setValor(rs.getString("valor"));
				aux="";
				if(rs.getString("estado_normal")!=null)
				{
					aux = rs.getString("estado_normal");
				}
				if(!aux.equals(""))
					revisionSistema.setEstadoNormal(UtilidadTexto.getBoolean(aux));
				revisionSistema.setNombre(rs.getString("nombre"));
				revisionSistema.setUnidadMedida(rs.getString("unidad_medida"));
				revisionSistema.setValorVerdadero(rs.getString("verdadero"));
				revisionSistema.setValorFalso(rs.getString("falso"));
				revisionSistema.setTipoComponente(rs.getInt("tipo_componente"));
				valoracionBase.getRevisionesSistemas().add(revisionSistema);
			}
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
			//******************************************************************************************************************
			//************SE CARGAN LAS REVISIONES POR SISTEMAS MULTIPLES (que contienen opciones)*******************************
			pst =  new PreparedStatementDecorator(con.prepareStatement(cargarRevisionXSistemasMultiple_Str));
			pst.setInt(1,Utilidades.convertirAEntero(numeroSolicitud));
			rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{
				DtoRevisionSistema revisionSistema =new DtoRevisionSistema();
				revisionSistema.setMultiple(true); //es por opciones
				
				revisionSistema.setCodigo(rs.getInt("codigo"));
				revisionSistema.setDescripcion(rs.getString("descripcion"));
				aux="";
				if(rs.getString("estado_normal")!=null)
				{
					aux = rs.getString("estado_normal");
				}
				if(!aux.equals(""))
					revisionSistema.setEstadoNormal(UtilidadTexto.getBoolean(aux));
				revisionSistema.setNombre(rs.getString("nombre"));
				revisionSistema.setValorVerdadero(rs.getString("verdadero"));
				revisionSistema.setValorFalso(rs.getString("falso"));
				revisionSistema.setTipoComponente(rs.getInt("tipo_componente"));
				
				//*******************SE CARGAN LAS OPCIONES DE LA REVISION X SISTEMAS*******************************************************
				PreparedStatementDecorator pst1 =  new PreparedStatementDecorator(con.prepareStatement(cargarRevisionXSistemasOpciones_Str));
				pst1.setInt(1,Utilidades.convertirAEntero(numeroSolicitud));
				pst1.setInt(2,revisionSistema.getCodigo());
				ResultSetDecorator rs1 = new ResultSetDecorator(pst1.executeQuery());
				
			
				while(rs1.next())
				{
					InfoDatosInt opcion = new InfoDatosInt();
					opcion.setCodigo(rs1.getInt("codigo"));
					opcion.setNombre(rs1.getString("nombre"));
					
					revisionSistema.getOpciones().add(opcion);
				}
				UtilidadBD.cerrarObjetosPersistencia(pst1, rs1, null);
				//****************************************************************************************************************************
				logger.info("SE CARGÓ L AREVISION X SISTEMAS MÚLTIPLE "+revisionSistema.getNombre()+" con las opciones: "+revisionSistema.getOpciones().size());
				valoracionBase.getRevisionesSistemas().add(revisionSistema);
			}
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
			//*********************************************************************************************************************
			//*************SE CARGAN LOS SIGNOS VITALES*****************************************************************************
			pst =  new PreparedStatementDecorator(con.prepareStatement(cargarSignosVitales_Str));
			pst.setInt(1,Utilidades.convertirAEntero(numeroSolicitud));
			rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{
				SignoVital signoVital = new SignoVital();
				signoVital.setCodigo(rs.getInt("codigo"));
				signoVital.setDescripcion(rs.getString("descripcion"));
				signoVital.setValorSignoVital(rs.getString("valor"));
				aux="";
				if(rs.getString("estado_normal")!=null)
				{
					aux = rs.getString("estado_normal");
				}
				if(!aux.equals(""))
					signoVital.setEstadoNormal(UtilidadTexto.getBoolean(aux));
				signoVital.setNombre(rs.getString("nombre"));
				signoVital.setUnidadMedida(rs.getString("unidad_medida"));
				
				valoracionBase.getSignosVitales().add(signoVital);
			}
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
			//**********************************************************************************************************************
			//***********SE CARGAN LOS DATOS DE LA VALORACION DE CONSULTA***********************************************************
			pst =  new PreparedStatementDecorator(con.prepareStatement(cargarConsulta_Str));
			pst.setInt(1,Utilidades.convertirAEntero(numeroSolicitud));
			rs = new ResultSetDecorator(pst.executeQuery());
			
			//logger.info("SE REALIZARÁ LA CONSULTA DE LA VALORACION DE CONSULTA=> "+cargarConsulta_Str+", numeroSolicitud: "+numeroSolicitud);
			if(rs.next())
			{
				aux="";
				if(rs.getString("primera_vez")!=null)
				{
					aux = rs.getString("primera_vez");
				}
				if(!aux.equals(""))
					valoracionBase.getValoracionConsulta().setPrimeraVez(UtilidadTexto.getBoolean(aux));
				valoracionBase.getValoracionConsulta().setCodigoFinalidadConsulta(rs.getString("codigo_finalidad_consulta"));
				valoracionBase.getValoracionConsulta().setNombreFinalidadConsulta(rs.getString("nombre_finalidad_consulta"));
				valoracionBase.getValoracionConsulta().setCodigoTipoDiagnostico(rs.getInt("codigo_tipo_diagnostico"));
				valoracionBase.getValoracionConsulta().setNombreTipoDiagnostico(rs.getString("nombre_tipo_diagnostico"));
				valoracionBase.getValoracionConsulta().setCodigoTipoRecargo(rs.getInt("codigo_tipo_recargo"));
				valoracionBase.getValoracionConsulta().setNombreTipoRecargo(rs.getString("nombre_tipo_recargo"));
				valoracionBase.getValoracionConsulta().setFechaProximoControl(rs.getString("fecha_proximo_control"));
				valoracionBase.getValoracionConsulta().setNumeroDiasIncapacidad(rs.getString("num_dia_incapacidad"));
				valoracionBase.getValoracionConsulta().setObservacionesIncapacidad(rs.getString("observaciones_incapacidad"));
				
				//logger.info("NOMBRE DE LA FINALIDAD DE CONSULTA=> "+valoracionBase.getValoracionConsulta().getNombreFinalidadConsulta());
				//logger.info("NOMBRE DEL TIPO DE DIAGNÓSTICO=> "+valoracionBase.getValoracionConsulta().getNombreTipoDiagnostico());
			}
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
			//**********************************************************************************************************************
			//**************SE CARGAN LOS DOCUMENTOS ADJUNTOS DE LA VALORACION*******************************************************
			pst =  new PreparedStatementDecorator(con.prepareStatement(cargarDocumentosAdjuntosStr));
			pst.setInt(1,Utilidades.convertirAEntero(numeroSolicitud));
			rs = new ResultSetDecorator(pst.executeQuery());
			
			valoracionBase.getValoracionConsulta().setArchivosAdjuntos(UtilidadBD.cargarValueObject(rs, true, true));
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
			//***********************************************************************************************************************
			
		}
		catch(SQLException e)
		{
			logger.error("Error al cargarBase: "+e);
		}
		return valoracionBase;
	}
	
	/**
	 * Método para consultar la información triage
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static InfoDatosString consultarInformacionTriageUrgencias(Connection con,String idCuenta)
	{
		InfoDatosString datos = new InfoDatosString();
		PreparedStatementDecorator pst =   null;
		ResultSetDecorator rs =  null;
		try
		{
			String consecutivo = "", consecutivoFecha = "";
			//*************SE VERIFICA SI EXISTE INFORMACIÓN DE TRIAGE PARA LA ADMISION************************
			String consulta = "SELECT " +
				"consecutivo_triage," +
				"consecutivo_triage_fecha " +
				"from admisiones_urgencias " +
				"where cuenta=? and consecutivo_triage is not null and consecutivo_triage_fecha is not null";
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			pst.setInt(1,Utilidades.convertirAEntero(idCuenta));
			rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				consecutivo = rs.getString("consecutivo_triage");
				consecutivoFecha = rs.getString("consecutivo_triage_fecha");
			}
			//*************************************************************************************************
			//***********SE CONSULTAN LOS DATOS DEL TRIAGE****************************************************
			if(!consecutivo.equals("")&&!consecutivoFecha.equals(""))
			{
				consulta = "SELECT "+
					"ct.nombre As clasificacion, "+
					"ct.color AS codigo_color, "+
					"colt.nombre AS color, "+ 
					"coalesce(t.observaciones_generales,'') AS observaciones "+ 
					"FROM triage t "+ 
					"inner join categorias_triage ct on (ct.consecutivo=t.categoria_triage and ct.institucion=t.institucion) "+ 
					"inner join colores_triage colt on (colt.codigo=ct.color) "+ 
					"WHERE t.consecutivo = ? and t.consecutivo_fecha = ?";
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
				pst.setInt(1,Utilidades.convertirAEntero(consecutivo));
				pst.setString(2,consecutivoFecha);
				
				rs = new ResultSetDecorator(pst.executeQuery());
				if(rs.next())
				{
					datos.setNombre(rs.getString("clasificacion"));
					datos.setCodigo(rs.getString("color"));
					datos.setDescripcion(rs.getString("observaciones"));
				}
			}
			//***********************************************************************************************
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarInformacionTriageUrgencias: "+e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
		}
		return datos;
	}
	
	/**
	 * Método implementado para insertar la primera versión de epicrisis
	 * @param con
	 * @param codigoIngreso
	 * @param codigoProfesional
	 * @param isUrgencias
	 * @return
	 */
	public static int insertarPrimeraVersionEpicrisis(Connection con, String codigoIngreso,int codigoProfesional,boolean isUrgencias)
	{
		int resultado = ConstantesBD.codigoNuncaValido;
		PreparedStatementDecorator pst = null;
		try
		{
			if(!existePrimeraVersionEpicrisis(con, codigoIngreso))
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(insertarPrimeraVersionEpicrisis_Str));
				pst.setInt(1,Utilidades.convertirAEntero(codigoIngreso));
				if(isUrgencias)
				{
					pst.setBoolean(2,true);
					pst.setBoolean(3,false);
				}
				else
				{
					pst.setBoolean(2,false);
					pst.setBoolean(3,true);
				}
				pst.setInt(4,codigoProfesional);
				
				resultado = pst.executeUpdate();
			}
			else
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(actualizarPrimeraVersionEpicrisisStr));
				pst.setInt(1,codigoProfesional);
				if(isUrgencias)
				{
					pst.setBoolean(2,true);
					pst.setBoolean(3,false);
				}
				else
				{
					pst.setBoolean(2,false);
					pst.setBoolean(3,true);
				}
				pst.setInt(4,Utilidades.convertirAEntero(codigoIngreso));
				
				resultado = pst.executeUpdate();
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarPrimeraVersionEpicrisis: "+e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(pst, null, null);
		}
		return resultado;
	}
	
	/**
	 * Método que verifica si existe primera versión de epicrisis
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	private static boolean existePrimeraVersionEpicrisis(Connection con,String codigoIngreso)
	{
		boolean existe = false;
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(existePrimeraVersionEpicrisisStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(codigoIngreso));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				if(rs.getInt("numResultados")>0)
					existe=true;
		}
		catch(SQLException e)
		{
			logger.error("Error en existePrimeraVersionEpicrisis: "+e);
		}
		return existe;
	}
	
	/**
	 * Método implementado para modificar la valroación de urgencias
	 * @param con
	 * @param valoracionUrgencias
	 * @return
	 */
	public static ResultadoBoolean modificarUrgencias(Connection con,DtoValoracionUrgencias valoracionUrgencias)
	{
		
		ResultadoBoolean resultado = new ResultadoBoolean(true,"");
		try
		{
			if(System.getProperty("TIPOBD").equals("POSTGRESQL"))
    		{
	        	Statement stmt = con.createStatement();
	            stmt.execute("SET standard_conforming_strings TO off");
	            stmt.close();
    		}
			//**********************VERIFICA POSIBLE MODIFICACIÓN DE LA VALORACIÓN DE URGENCIAS*******************************
			String consulta = "UPDATE valoraciones_urgencias SET ";
			String seccionSET = "";
			
			//SE verifica si se modificó Con Aprobación de...
			if(!valoracionUrgencias.getNombreAutorizador().equals(valoracionUrgencias.getNombreAutorizadorAnterior()))
				seccionSET += " nombre_autorizador = '" + valoracionUrgencias.getNombreAutorizador() + "' ";
			
			if(!valoracionUrgencias.getRelacionAutorizador().equals(valoracionUrgencias.getRelacionAutorizadorAnterior()))
				seccionSET +=  (seccionSET.length()>0?",":"") + " relacion_autorizador = '" + valoracionUrgencias.getRelacionAutorizador() + "' ";
			
			//Se verifica si se modificó la conducta de la valoracion
			if(valoracionUrgencias.getCodigoConductaValoracion()!=valoracionUrgencias.getCodigoConductaValoracionAnterior())
			{
				seccionSET +=  (seccionSET.length()>0?",":"") ;
				seccionSET += " codigo_conducta_valoracion = " + valoracionUrgencias.getCodigoConductaValoracion() + " ";
				seccionSET += " , desc_conducta_valoracion = desc_conducta_valoracion || '_&_' || '" + agregarDatosControlUsuarioConductaValoracion(con, valoracionUrgencias)+ "' ";
				
				//Modificacion del tipo de monitoreo
				if(valoracionUrgencias.getCodigoConductaValoracion()==ConstantesBD.codigoConductaSeguirTrasladoCuidadoEspecial)
					seccionSET += ", tipo_monitoreo = "+valoracionUrgencias.getCodigoTipoMonitoreo()+" ";
				else
					seccionSET += ", tipo_monitoreo = null ";
				
				seccionSET += " , fecha_modifica = CURRENT_DATE, hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" , usuario_modifica = '"+valoracionUrgencias.getProfesional().getLoginUsuario()+"' ";
			}
			
			if(seccionSET.length()>0)
			{
				consulta = consulta + seccionSET + " WHERE numero_solicitud = "+ valoracionUrgencias.getNumeroSolicitud();
				Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
				if(st.executeUpdate(consulta)<=0)
				{
					resultado.setResultado(false);
					resultado.setDescripcion("Problemas actualizando la información de la valoración de urgencias. Proceso cancelado");
				}
			}
			//***************++Posible actualización de la seccion de INCAPACIDAD***********************************************
			//Si fue exitoso se continua
			if(resultado.isTrue())
			{
				if(
						//Si la conducta que se cambió no fue conducta que genera egreso automático
						(valoracionUrgencias.getCodigoConductaValoracion()!=ConstantesBD.codigoConductaSeguirSalidaSinObservacion&&
								valoracionUrgencias.getCodigoConductaValoracion()!=ConstantesBD.codigoConductaSeguirRemitirMayorComplejidad)
						||
						//o la salida fue Muerto o no Hubo Salida
						!UtilidadTexto.getBoolean(valoracionUrgencias.getEstadoSalida())
						
					)
				{
					//Se quitan los valores de la seccion de incapacidad
					valoracionUrgencias.getValoracionConsulta().setNumeroDiasIncapacidad("");
					valoracionUrgencias.getValoracionConsulta().setObservacionesIncapacidad("");
				}
				
				consulta = "UPDATE valoraciones_consulta SET num_dias_incapacidad = ?, observaciones_incapacidad = ? WHERE numero_solicitud = ?";
				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				if(Utilidades.convertirAEntero(valoracionUrgencias.getValoracionConsulta().getNumeroDiasIncapacidad())>0)
					pst.setInt(1,Utilidades.convertirAEntero(valoracionUrgencias.getValoracionConsulta().getNumeroDiasIncapacidad()));
				else
					pst.setNull(1,Types.INTEGER);
				if(!valoracionUrgencias.getValoracionConsulta().getObservacionesIncapacidad().equals(""))
					pst.setString(2,valoracionUrgencias.getValoracionConsulta().getObservacionesIncapacidad());
				else
					pst.setNull(2,Types.VARCHAR);
				pst.setInt(3,Utilidades.convertirAEntero(valoracionUrgencias.getNumeroSolicitud()));
				
				if(pst.executeUpdate()<=0)
				{
					resultado.setResultado(false);
					resultado.setDescripcion("Problemas actualizando la información de la incapacidad. Proceso Cancelado");
				}
			}
			
			//****************************************************************************************************************
			
			//***************************************************************************************************************
			
			//*******************SE INSERTAN OBSERVACIONES DE LA VALORACION*************************************************
			/**
			 * Alberto Ovalle 
			 * mt 5749
			 * se realiza modificacion ya se esta insertando las observaciones
			 */
//			
//			if(resultado.isTrue())
//			{
//		
//				String fechaActual = UtilidadFecha.getFechaActual(con);
//				String horaActual = UtilidadFecha.getHoraActual(con);
//				resultado = insertarObservaciones(con, valoracionUrgencias.getObservaciones(), valoracionUrgencias.getNumeroSolicitud(), fechaActual, horaActual);
//			}
			//*************************************************************************************************************
			
		}
		catch(SQLException e)
		{
			logger.error("Error modificarUrgencias: "+e);
			resultado.setResultado(false);
			resultado.setDescripcion("Problemas realizando la modificación de la valoración de urgencias: "+e);
		}
		return resultado;
	}
	
	/**
	 * Método implementado para cargar los históricos de valoraciones y/o evoluciones en caso de asocio
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> cargarHistorico(Connection con,String idIngreso)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator pst =  null;
		resultados.put("numRegistros","0");
		try
		{
			pst =  new PreparedStatementDecorator(con.prepareStatement(cargarHistoricoStr));
			pst.setInt(1,Utilidades.convertirAEntero(idIngreso));
			pst.setInt(2,Utilidades.convertirAEntero(idIngreso));
			//logger.info("cargarHistorico=> "+cargarHistoricoStr+", codigoIngreso=> "+idIngreso);
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			//Se agregan los índices 
			resultados.put("INDICES",indicesHistorico);
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarHistorico: "+e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(pst, null, null);
		}
		return resultados;
	}
	
	/**
	 * Método implementado para actualizar la valoracion en el registro cuidado especial
	 * @param con
	 * @param codigoIngreso
	 * @param numeroSolicitud
	 * @return
	 */
	public static ResultadoBoolean actualizarValoracionRegistroCuidadoEspecial(Connection con,String codigoIngreso,String numeroSolicitud,String loginUsuario,boolean obligatorio)
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true,"");
		PreparedStatementDecorator pst =  null;
		ResultSetDecorator rs = null;
		try
		{
			//Primero se consulta el codigo del registro de ciudado especial en estado activo (si existe)
			String codigoRegistro = "";
			pst =  new PreparedStatementDecorator(con.prepareStatement(obtenerCodigoRegistroCuidadoEspecialStr));
			pst.setInt(1,Utilidades.convertirAEntero(codigoIngreso));
			rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				codigoRegistro = rs.getString("codigo");
				
				String consulta = "UPDATE ingresos_cuidados_especiales SET " +
					"valoracion_orden = CASE WHEN valoracion_orden IS NULL THEN "+numeroSolicitud+" ELSE valoracion_orden END," +
					"valoracion = "+numeroSolicitud+", " +
					"usuario_resp = ?, " +
					"fecha_resp = CURRENT_DATE, " +
					"hora_resp = "+ValoresPorDefecto.getSentenciaHoraActualBD()+"   " +
					"WHERE codigo = ?";
				
				//Si existe un registro se actualiza la valoración de hospitalización ese registro
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
				pst.setString(1,loginUsuario);
				pst.setInt(2,Utilidades.convertirAEntero(codigoRegistro));
				
				int resp = pst.executeUpdate();
				
				if(resp<=0)
				{
					resultado.setResultado(false);
					resultado.setDescripcion("Problemas tratando de actualizar la valoración en el registro de cuidados especiales");
				}
			}
			//Si era obligatorio hacer la actualización se genera error
			else if(obligatorio)
			{
				resultado.setResultado(false);
				resultado.setDescripcion("No se encontró un registro de cuidado especial para asociar la valoración. Por favor verifique");
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarValoracionRegistroCuidadoEspecial: "+e);
			resultado.setResultado(false);
			resultado.setDescripcion("Problemas tratando de actualizar la valoración en el registro de cuidados especiales");
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
		}
		return resultado;
	}
	
	/**
	 * Método para obtener la causa externa de la primera valoración
	 * @param con
	 * @param idCuenta
	 * @param seccionOrderBy
	 * @return
	 */
	public static InfoDatosInt obtenerCausaExternaValoracion(Connection con,String idCuenta,String seccionOrderBy)
	{
		InfoDatosInt resultado = new InfoDatosInt();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consulta = obtenerCausaExternaValoracionStr + seccionOrderBy;
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			pst.setInt(1,Utilidades.convertirAEntero(idCuenta));
			
			rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				resultado.setCodigo(rs.getInt("codigo_causa_externa"));
				resultado.setDescripcion("Causa externa postulada de "+rs.getString("nombre_tipo_solicitud"));
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCausaExternaValoracion: "+e);
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
		}
		
		return resultado;
	}	
	/**
	 * Método para obtener la finalidad de la consulta de la valoracion
	 * @param con
	 * @param idCuenta
	 * @param tipoBD
	 * @return
	 */
	public static InfoDatosString obtenerFinalidadConsultaValoracion(Connection con,String idCuenta,int tipoBD)
	{
		InfoDatosString resultado = new InfoDatosString(); 
		PreparedStatement pst = null;
		ResultSet rs = null;
		try
		{
			String consulta = obtenerFinalidadConsultaValoracionStr;
			
			if (System.getProperty("TIPOBD").equals("ORACLE")) 
			{
				consulta += " AND rownum = 1 ORDER BY vc.numero_solicitud DESC";
			}else 
			{
				consulta += " ORDER BY vc.numero_solicitud DESC "+ValoresPorDefecto.getValorLimit1()+" 1";
			}
				
			
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Integer.parseInt(idCuenta));
			
			rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				resultado.setCodigo(rs.getString("codigo_finalidad_consulta"));
				resultado.setNombre(rs.getString("nombre_finalidad_consulta"));
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerFinalidadConsultaValoracion: "+e);
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
		}
		return resultado;
	}
	
	/**
	 * Método implementado para obtener la fecha/hora grabacion de la valoración de urgencias
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static String[] obtenerFechahoraGrabacionValoracionUrgencias(Connection con,String idCuenta)
	{
		String[] fechaHora = {"",""};
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			String consulta = "SELECT " +
				"to_char(v.fecha_grabacion,'"+ConstantesBD.formatoFechaAp+"') AS fecha_grabacion, " +
				"substr(v.hora_grabacion,0,6) AS hora_grabacion " +
				"FROM solicitudes s " +
				"INNER JOIN valoraciones v on(v.numero_solicitud=s.numero_solicitud) " +
				"WHERE s.cuenta = "+idCuenta+" AND s.tipo = "+ConstantesBD.codigoTipoSolicitudInicialUrgencias;
			st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
			{
				fechaHora[0] = rs.getString("fecha_grabacion");
				fechaHora[1] = rs.getString("hora_grabacion");
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerFechahoraGrabacionValoracionUrgencias: "+e);
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(st, rs, null);
		}
		
		return fechaHora;
	}

	public static void ponerPacienteEnEvaloracion(Connection con, int paciente, boolean estaEnValoracion, String usuario, String sessionId) {
		if(estaEnValoracion)
		{
			String sentencia="INSERT INTO historiaclinica.pacientes_en_valoracion values(?,CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?, ?)";
//			logger.info("\n--------------------------------------------------------------------------------------------------------------------\n"+sentencia);
			PreparedStatementDecorator psd=null;
			try {
				psd=new PreparedStatementDecorator(con.prepareStatement(sentencia));
				psd.setInt(1, paciente);
				psd.setString(2, usuario);
				psd.setString(3, sessionId);
				psd.executeUpdate();
			
			} catch (SQLException e) {
				logger.error("Problema cambiando el estado del paciente a 'en valoracion'",e);
			}finally{
				UtilidadBD.cerrarObjetosPersistencia(psd, null, null);
			}
		}
		else
		{
			String sentencia="DELETE FROM historiaclinica.pacientes_en_valoracion WHERE paciente=?";
//			logger.info("\n--------------------------------------------------------------------------------------------------------------------\n"+sentencia);
			PreparedStatementDecorator psd= null;
			try {
				psd=new PreparedStatementDecorator(con.prepareStatement(sentencia));
				psd.setInt(1, paciente);
				psd.executeUpdate();
				
			} catch (SQLException e) {
				logger.error("Problema eliminando el estado del paciente 'en valoracion'", e);
			}finally{
				UtilidadBD.cerrarObjetosPersistencia(psd, null, null);
			}
		}
	}

	public static int cancelarPacientesEnEvaloracion(Connection con) {
		try {
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con.prepareStatement("DELETE FROM historiaclinica.pacientes_en_valoracion"));
			int resultado=psd.executeUpdate();
			psd.close();
			return resultado;
		} catch (SQLException e) {
			logger.error("Error eliminando",e);
		}
		return 0;
	}
	
	/**
	 * 
	 * @param con
	 * @param solicitud
	 * @param pool
	 * @return
	 */
	public static boolean actualizarPoolSolicitudXAgenda(Connection con, int solicitud, int pool)
	{
		boolean transaccionExitosa=false;
		PreparedStatementDecorator pst =  null;
		try
		{
			String consulta=	"UPDATE " +
									"ordenes.solicitudes " +
								"SET " +
									"pool=? " +
								"WHERE " +
									"numero_solicitud=?";
			
			pst = new PreparedStatementDecorator(con.prepareStatement(consulta));
			pst.setInt(1,pool);
			pst.setInt(2,solicitud);
			
			if (pst.executeUpdate()>0)
				transaccionExitosa=true;
			

		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarPoolSolicitudXAgenda: "+e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(pst, null, null);
		}
		return transaccionExitosa;
	}
	
	/**
	 * @param con
	 * @param numeroSolcitud
	 * @return si tiene una valoracion o no asocada al numero de solicitud 
	 */
	public static Boolean tieneValoraciones(Connection con,String numeroSolcitud){
		Boolean indicadorTieneValoraciones=false;
		PreparedStatementDecorator pst=null;
		ResultSet res = null;
		try {
			String consulta = "select * from valoraciones val where val.numero_solicitud = ?";
			pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet ));
			pst.setString(1, numeroSolcitud);
			res = pst.executeQuery();
			if (res.next()) {
				indicadorTieneValoraciones=true;
			}
		}catch(SQLException e)
		{
			logger.error("Error en actualizarPoolSolicitudXAgenda: "+e);
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(pst, res, null);
		 }
		return indicadorTieneValoraciones;
	}		
	
	/**
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static DtoValoracionUrgencias cargarDiagnosticosValoracion(Connection con,String numeroSolicitud)
	{
		 	DtoValoracionUrgencias valoracionUrgencias = new DtoValoracionUrgencias();
		 	PreparedStatementDecorator pst=null;
		 	ResultSetDecorator rs=null;
		 try{	
			 //******************************************************************************************************************
				//************SE CARGA DIAGNOSTICOS VALORACION**********************************************************************
				pst =  new PreparedStatementDecorator(con.prepareStatement(cargarDiagnosticos_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setInt(1,Utilidades.convertirAEntero(numeroSolicitud));
				rs = new ResultSetDecorator(pst.executeQuery());
				
				while(rs.next())
				{
					Diagnostico diagnostico = new Diagnostico();
					diagnostico.setAcronimo(rs.getString("acronimo"));
					diagnostico.setTipoCIE(rs.getInt("tipo_cie"));
					diagnostico.setNombre(rs.getString("nombre_diagnostico"));
					diagnostico.setPrincipal(rs.getBoolean("principal"));
					diagnostico.setDefinitivo(rs.getBoolean("definitivo"));
					diagnostico.setNumero(rs.getInt("numero"));
					valoracionUrgencias.getDiagnosticos().add(diagnostico);
				}
		 }
		 catch (SQLException e) {
			Log4JManager.error(e.getMessage(), e);
		 }
		 finally{
			 try {
			 if(rs != null){
				rs.close();
			 }
			 if(pst != null){
				 pst.close();
			 }
			 } catch (SQLException e) {
				Log4JManager.error(e.getMessage(), e);
			}
		 }
		return valoracionUrgencias;
	}
	
	/**
	 * Método que valida si dicha solicitud es de tipo de Hospitalizacion-Cuidados Especiales  
	 * @param con
	 * @param idSolicitud
	 * @return boolean
	 */
	public static boolean esSolicitudDeCuidadosEspeciales(Connection con, int idSolicitud) {

		boolean respuesta = false;
		PreparedStatementDecorator pst = null;
		ResultSetDecorator rs = null;
		try {
			pst = new PreparedStatementDecorator(con.prepareStatement(esSolicitudCuidadosEspeciales_Str));
			pst.setInt(1, idSolicitud);
			rs = new ResultSetDecorator(pst.executeQuery());

			while (rs.next()) {
				if (rs.getInt("total") > 0) {
					respuesta = true;
				}
			}
		} catch (SQLException e) {
			Log4JManager.error(e.getMessage(), e);
		} finally {
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
		}
		return respuesta;

	}
	
	/**
	 * Alberto Ovalle
	 * mt5749
	 * metodo que consulta las valoraciones 
	 * de las observaciones y las urgencias por numero solicitud 
	 * @param numeroSolicitud
	 * @return List
	 */
	public static List<DtoValoracion> cargarObservacionesVistaValoracion (Connection con,String numeroSolicitud) {
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<DtoValoracion> observacionesList = null;
		
		
		try {
			
		//************************************************************************************************************************
		//***************SE CARGAN VISUALIZACION OBSERVACIONES VALORACIONES*****************************************************************************
		pst = con.prepareStatement(obtenerVistaValoracionObservaciones_str);
		pst.setInt(1,Utilidades.convertirAEntero(numeroSolicitud));
		pst.setString(2,ConstantesIntegridadDominio.acronimoMotivoConsulta);
		rs = pst.executeQuery();
		observacionesList = new ArrayList<DtoValoracion>() ;
		
			while (rs.next()) {
				
				DtoValoracion observaciones = new DtoValoracion();
				observaciones.setConsecutivo(rs.getString("consecutivo"));
				observaciones.setNumeroSolicitud(rs.getString("numero_solicitud"));
				observaciones.setTipo(rs.getString("tipo"));
				observaciones.setValor(rs.getString("valor"));
				//observaciones.setValorObservaciones(rs.getString("valor"));
				observaciones.setFechaValoracionObservacion(rs.getString("fecha_registro"));
				observaciones.setHoraValoracionObservacion(rs.getString("hora"));
				observaciones.setLabel(ValoresPorDefecto.getIntegridadDominio(observaciones.getTipo()).toString());
				//observaciones.setEtiquetaObservaciones(rs.getString("tipo"));
				observaciones.getProfesional().cargarUsuarioBasico(con, rs.getString("usuario"));
				observacionesList.add(observaciones);
			}
		
		} catch (SQLException e) {
			logger.error("SQLException: cargarVistaObservaciones: " + e);
		} catch(Exception ex){
			logger.error("Exception: cargarVistaObservaciones: " + ex);
		} 
		return observacionesList;
	}
	
	/**
	 * Alberto Ovalle
	 * mt5749
	 * metodo para obtener la cadena Registro ConductaValoracion
	 * @param con
	 * @param numeroSolicitud
	 * @return String
	 */
	
	public static String selecionarCadenaRegistroConductaValoracion(Connection con,String numeroSolicitud ){
	
		String cadenaConductaValoracion = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
		
		pst = con.prepareStatement(obtenerVistaValoracionConducta_str);
		pst.setInt(1,Utilidades.convertirAEntero(numeroSolicitud));
		rs = pst.executeQuery();
			while (rs.next()) {
				ValoracionConductaObservacionesDto observaciones = new ValoracionConductaObservacionesDto();
				observaciones.setNumeroSolicitud(rs.getString("numero_solicitud"));
				observaciones.setDesc_conducta_valoracion(rs.getString("desc_conducta_valoracion"));
				cadenaConductaValoracion=observaciones.getDesc_conducta_valoracion();	
			}
		
		} catch (SQLException e) {
			logger.error("SQLException: obtenerVistaValoracionConducta_str: " + e);
		} catch(Exception ex){
			logger.error("Exception: selecionarCadenaRegistroConductaValoracion: " + ex);
		} finally {
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
			
		}
			return cadenaConductaValoracion;	
        }

	/**
	 * Alberto Ovalle 
	 * mt 5749
	 * metodo que modifica las observaciones de Urgencias
	 * @param con
	 * @param dtoValoracionObservaciones
	 * @param valoracionUrgencias
	 * @param fechaActual
	 * @param horaActual
	 */
	public static void modificarObservaciones(Connection con,DtoValoracionObservaciones dtoValoracionObservaciones,DtoValoracionUrgencias valoracionUrgencias,  String fechaActual, String horaActual){
		PreparedStatement pst = null;
		try	{

			if (!dtoValoracionObservaciones.getPlanDiagnostico().equals("")) {
				pst = con.prepareStatement(insertarObservacionValoracion_Str);
				int secuenciaObservacion = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_val_observaciones");
				pst.setDouble(1,Utilidades.convertirADouble(secuenciaObservacion+""));
				pst.setInt(2,Utilidades.convertirAEntero(valoracionUrgencias.getNumeroSolicitud()));
				pst.setString(3,ConstantesIntegridadDominio.acronimoPlanDiagnosticoTerapeutico);
				pst.setString(4,dtoValoracionObservaciones.getPlanDiagnostico());	
				if(!dtoValoracionObservaciones.getFecha().equals(""))
					pst.setDate(5,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dtoValoracionObservaciones.getFecha())));
				else
					pst.setDate(5,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaActual)));
				
				if(!dtoValoracionObservaciones.getHora().equals(""))
					pst.setString(6,dtoValoracionObservaciones.getHora());
				else
					pst.setString(6,horaActual);
				pst.setString(7,valoracionUrgencias.getProfesional().getLoginUsuario());
				
				pst.executeUpdate();
			}
			
	                  
	                    
	                    
	                    
	    					if (!dtoValoracionObservaciones.getComentariosGenerales().equals("")) {
	    						pst = con.prepareStatement(insertarObservacionValoracion_Str);
	    						int secuenciaObservacion = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_val_observaciones");
	    						pst.setDouble(1,Utilidades.convertirADouble(secuenciaObservacion+""));
	    						pst.setInt(2,Utilidades.convertirAEntero(valoracionUrgencias.getNumeroSolicitud()));
	    						pst.setString(3,ConstantesIntegridadDominio.acronimoComentariosGenerales);
	    						pst.setString(4,dtoValoracionObservaciones.getComentariosGenerales());	
	    						if(!dtoValoracionObservaciones.getFecha().equals(""))
	    							pst.setDate(5,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dtoValoracionObservaciones.getFecha())));
	    						else
	    							pst.setDate(5,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaActual)));
	    						
	    						if(!dtoValoracionObservaciones.getHora().equals(""))
	    							pst.setString(6,dtoValoracionObservaciones.getHora());
	    						else
	    							pst.setString(6,horaActual);
	    						pst.setString(7,valoracionUrgencias.getProfesional().getLoginUsuario());
	    						
	    						pst.executeUpdate();

	    					}	
	    					
	    					  if (!dtoValoracionObservaciones.getExpliqueDosDeberesDerechos().equals("")) {
	    							pst = con.prepareStatement(insertarObservacionValoracion_Str);
	    							int secuenciaObservacion = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_val_observaciones");
	    							pst.setDouble(1,Utilidades.convertirADouble(secuenciaObservacion+""));
	    							pst.setInt(2,Utilidades.convertirAEntero(valoracionUrgencias.getNumeroSolicitud()));
	    							pst.setString(3,ConstantesIntegridadDominio.acronimoPronostico);
	    							pst.setString(4,dtoValoracionObservaciones.getExpliqueDosDeberesDerechos());	
	    							if(!dtoValoracionObservaciones.getFecha().equals(""))
	    								pst.setDate(5,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dtoValoracionObservaciones.getFecha())));
	    							else
	    								pst.setDate(5,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaActual)));
	    							
	    							if(!dtoValoracionObservaciones.getHora().equals(""))
	    								pst.setString(6,dtoValoracionObservaciones.getHora());
	    							else
	    								pst.setString(6,horaActual);
	    							pst.setString(7,valoracionUrgencias.getProfesional().getLoginUsuario());
	    							
	    							pst.executeUpdate();
	    						
	    						}
	    					
					
	                    
				

		}
		catch (SQLException e) {
			logger.error("Error modificarObservaciones:"+e);
			e.printStackTrace();
		}
		finally {
			UtilidadBD.cerrarObjetosPersistencia(pst, null, null);

		}

	}
	
	/**
	 * Alberto Ovalle 
	 * mt 5749
	 * metodo que modifica las observaciones de Hospitalizacion
	 * @param con
	 * @param dtoValoracionObservaciones
	 * @param valoracionUrgencias
	 * @param fechaActual
	 * @param horaActual
	 */
	public static void modificarObsevacionesHospitalizacion(Connection con,DtoValoracionObservaciones dtoValoracionObservaciones,DtoValoracionHospitalizacion dtoValoracionHospitalizacion,  String fechaActual, String horaActual){
		PreparedStatement pst = null;
	
		try	{
						
			if (!dtoValoracionObservaciones.getPlanDiagnostico().equals("")) {
				pst = con.prepareStatement(insertarObservacionValoracion_Str);
				int secuenciaObservacion = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_val_observaciones");
				pst.setDouble(1,Utilidades.convertirADouble(secuenciaObservacion+""));
				pst.setInt(2,Utilidades.convertirAEntero(dtoValoracionHospitalizacion.getNumeroSolicitud()));
				pst.setString(3,ConstantesIntegridadDominio.acronimoPlanDiagnosticoTerapeutico);
				pst.setString(4,dtoValoracionObservaciones.getPlanDiagnostico());	
				if(!dtoValoracionObservaciones.getFecha().equals(""))
					pst.setDate(5,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dtoValoracionObservaciones.getFecha())));
				else
					pst.setDate(5,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaActual)));
				
				if(!dtoValoracionObservaciones.getHora().equals(""))
					pst.setString(6,dtoValoracionObservaciones.getHora());
				else
					pst.setString(6,horaActual);
				pst.setString(7,dtoValoracionHospitalizacion.getProfesional().getLoginUsuario());
										
				pst.executeUpdate();

			}
					
			
			
			if (!dtoValoracionObservaciones.getComentariosGenerales().equals("")) {
				pst = con.prepareStatement(insertarObservacionValoracion_Str);
				int secuenciaObservacion = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_val_observaciones");
				pst.setDouble(1,Utilidades.convertirADouble(secuenciaObservacion+""));
				pst.setInt(2,Utilidades.convertirAEntero(dtoValoracionHospitalizacion.getNumeroSolicitud()));
				pst.setString(3,ConstantesIntegridadDominio.acronimoComentariosGenerales);
				pst.setString(4,dtoValoracionObservaciones.getComentariosGenerales());	
				if(!dtoValoracionObservaciones.getFecha().equals(""))
					pst.setDate(5,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dtoValoracionObservaciones.getFecha())));
				else
					pst.setDate(5,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaActual)));
				
				if(!dtoValoracionObservaciones.getHora().equals(""))
					pst.setString(6,dtoValoracionObservaciones.getHora());
				else
					pst.setString(6,horaActual);
				pst.setString(7,dtoValoracionHospitalizacion.getProfesional().getLoginUsuario());
				
				pst.executeUpdate();
				
			}	
	                    if (!dtoValoracionObservaciones.getExpliqueDosDeberesDerechos().equals("")) {
						pst = con.prepareStatement(insertarObservacionValoracion_Str);
						int secuenciaObservacion = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_val_observaciones");
						pst.setDouble(1,Utilidades.convertirADouble(secuenciaObservacion+""));
						pst.setInt(2,Utilidades.convertirAEntero(dtoValoracionHospitalizacion.getNumeroSolicitud()));
						pst.setString(3,ConstantesIntegridadDominio.acronimoPronostico);
						pst.setString(4,dtoValoracionObservaciones.getExpliqueDosDeberesDerechos());	
						if(!dtoValoracionObservaciones.getFecha().equals(""))
							pst.setDate(5,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dtoValoracionObservaciones.getFecha())));
						else
							pst.setDate(5,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaActual)));
						
						if(!dtoValoracionObservaciones.getHora().equals(""))
							pst.setString(6,dtoValoracionObservaciones.getHora());
						else
							pst.setString(6,horaActual);
						pst.setString(7,dtoValoracionHospitalizacion.getProfesional().getLoginUsuario());
						
						pst.executeUpdate();
						
					}


		}
		catch(SQLException e) {
			logger.error("Error modificarObservaciones Hospitalizacion:"+e);
			e.printStackTrace();
		}
		finally {
			UtilidadBD.cerrarObjetosPersistencia(pst, null, null);
			
		}
		
		
	}
	
}	

 