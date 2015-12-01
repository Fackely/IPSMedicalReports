/*
 * @(#)SqlBaseResumenAtencionesDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.historiaClinica.DtoEspecialidadesHC;
import com.princetonsa.dto.historiaClinica.DtoViasIngresoHC;
import com.princetonsa.mundo.CuentasPaciente;

/**
 * Esta clase implementa la funcionalidad comï¿½n a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estï¿½ndar. Mï¿½todos particulares a ResumenAtenciones
 *
 *	@version 1.0, Mar 24, 2004
 *
 *	Modificado por Jose Eduardo Arias Doncel. Abril 2008. Anexo 550.
 */
@SuppressWarnings("rawtypes")
public class SqlBaseResumenAtencionesDao
{

	/**
	 * Para manejar los logs de esta clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseResumenAtencionesDao.class);
	
	private static final String existenCitasStr="SELECT " +
		"count(1) as cuenta " +
		"from solicitudes s " +
		"INNER JOIN servicios_cita sc ON(sc.numero_solicitud=s.numero_solicitud)  " +
		"INNER JOIN cita ci ON(ci.codigo=sc.codigo_cita) " +
		"INNER JOIN agenda a ON(a.codigo=ci.codigo_agenda) " +
		"where s.cuenta = ? ";
	/*
	 * Cadena constante con el <i>statement</i> necesario para buscar
	 * la informaciï¿½n necesaria para todos los ingresos de este paciente.  
	 */
	//Consulta antes de revisar los asocios de cuenta
	//private static final String busquedaIngresosStr=" select cue1.id_ingreso as idIngreso, cue1.fecha_apertura as fechaApertura, cue1.hora_apertura as horaApertura, eg.fecha_egreso as fechaEgreso, eg.hora_egreso as horaEgreso from cuentas cue1 INNER JOIN (SELECT min(cue.id) as cuentaInicial, max(cue.id) as cuentaFinal from ingresos ing INNER JOIN cuentas cue ON (ing.id=cue.id_ingreso) where ing.codigo_paciente =? ) rest ON ( cue1.id=rest.cuentaInicial) LEFT OUTER JOIN egresos eg ON (eg.cuenta=rest.cuentaFinal)";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar
	 * la informaciï¿½n necesaria para todas las cuentas de este paciente.  
	 */
	private static final String busquedaCuentasStr=" " +
		"SELECT "+
		"cue.id as cuenta, "+ 
		"CASE WHEN i.preingreso IS NOT NULL THEN i.preingreso ELSE '' END AS indpre, " +
		"CASE WHEN i.reingreso IS NOT NULL THEN i.reingreso ELSE 0 END AS indrei, " +
		"ec.nombre as estadoCuenta, "+ 
		"getEstadoAsocio(cue.id,cue.tipo_paciente) AS codigoViaIngreso, "+ 
		"getNombreViaIngresoTipoPac(cue.id) as viaIngreso, "+ 
		"cue.fecha_apertura as fechaCuenta, " +
		"getconsecutivoingreso(cue.id_ingreso) AS consecutivoIngreso, "+ 
		"cue.hora_apertura as horaCuenta, "+ 
		"getFechaIngAsocio(cue.id,getEstadoAsocio(cue.id,cue.tipo_paciente)) AS fechaAdmisionHospitalaria, "+ 
		"getHoraIngAsocio(cue.id,getEstadoAsocio(cue.id,cue.tipo_paciente)) AS horaAdmisionHospitalaria, "+ 
		"adu.fecha_admision as fechaAdmisionUrgencias, "+ 
		"adu.hora_admision as horaAdmisionUrgencias, "+ 
		"eg.fecha_egreso as fechaEgreso, "+ 
		"eg.hora_egreso as horaEgreso, "+ 
		"CASE WHEN cue.via_ingreso IN ("+ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoUrgencias+") THEN eg.diagnostico_principal || ' - ' ||getnombrediagnostico(eg.diagnostico_principal, eg.DIAGNOSTICO_PRINCIPAL_CIE) ELSE getnomdiagconsultaexterna(cue.id) END as diagnostico, "+
		"getresumenespecialidad(cue.via_ingreso,cue.id) as especialidad, "+
		"getnomcentroatencion(ccos.centro_atencion) AS centroatencion," +
		"CASE WHEN cue.id_ingreso IS NULL THEN '' ELSE coalesce(getDescripEntidadSubXingreso(cue.id_ingreso),'') END AS descripcionentidadsub," +
		" coalesce(gettransplante(cue.id_ingreso),'') As transplante,	 " +
		" i.fecha_ingreso as fecha_ingreso," +
		"    i.hora_ingreso as hora_ingreso,  case when i.estado='ABI'  then 'Abierto'   when i.estado='CER'  then 'Cerrado'  when i.estado='ANU' then 'Anulado'  when i.estado='IGAR'  then 'Ingreso por Garantias'  else ''   end as estado_ingreso, " +
		" i.preingreso as preingreso,  i.reingreso as reingreso ," +
		"   es.codigo_pk ,es.razon_social ," +
		"    eg.diagnostico_principal as codigoDx, " +
		"     eg.DIAGNOSTICO_PRINCIPAL_CIE as codigoCie," +
		"     getnombrediagnostico(eg.diagnostico_principal, eg.DIAGNOSTICO_PRINCIPAL_CIE) as descripcionDx  " +
		"  from cuentas cue "+ 
		"INNER JOIN estados_cuenta ec ON (cue.estado_cuenta=ec.codigo) "+ 
		"LEFT OUTER JOIN admisiones_hospi adh ON (cue.id=adh.cuenta) "+  
		"LEFT OUTER JOIN admisiones_urgencias adu ON (cue.id=adu.cuenta) "+ 
		"LEFT OUTER JOIN egresos eg ON (cue.id=eg.cuenta) "+
		"LEFT OUTER JOIN ingresos i ON (cue.id_ingreso=i.id) "+
		"INNER JOIN centros_costo ccos ON (cue.area=ccos.codigo)  " +
		" LEFT JOIN pac_entidades_subcontratadas pes   " +
		" on(i.PAC_ENTIDADES_SUBCONTRATADAS = pes.CONSECUTIVO)  " +
		" LEFT JOIN entidades_subcontratadas es   " +
		" on (pes.ENTIDAD_SUBCONTRATADA = es.codigo_pk ) "+ 
		"WHERE " +
		"cue.codigo_paciente=? and " +
		"getcuentafinal(cue.id) IS NULL AND " +
		"i.estado IN ('"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"','"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"') and "+
		//Filtro para que no se muestren las cuentas que se han cerrado de un asocio
		"esCuentaCerradaAsocio(cue.id) = '"+ConstantesBD.acronimoNo+"' "+
		"order by cue.fecha_apertura desc, cue.hora_apertura desc";
	
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para saber
	 * si este paciente tiene antecedentes o no.  
	 */
	private static final String tieneAntecedentesPacienteStr="SELECT count(1) as numResultados from antecedentes_pacientes where codigo_paciente=?";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para saber
	 * antecedentes tiene este paciente .  
	 */
	private static final String queAntecedentesExistenStr="	SELECT  alergias.codigo_paciente as alergias, familiares.codigo_paciente as familiares,										" +
														  " 		gineco.codigo_paciente as gineco, medicamentos.codigo_paciente as medicamentos,										" +
														  " 		morbidos.codigo_paciente as morbidos, pediatricos.codigo_paciente as pediatricos, 									" +
														  "			toxicos.codigo_paciente as toxicos, transfusionales.codigo_paciente as transfusionales,								" +
														  " 		varios.codigo_paciente as varios, odontologicos.cod_paciente as odontologicos, 										" +
														  "			vacunas.codigo_paciente as vacunas " +
														  " 		FROM antecedentes_pacientes ap 																						" +
														  "				 LEFT OUTER JOIN antecedentes_alergias alergias ON (ap.codigo_paciente=alergias.codigo_paciente)				" +
														  "				 LEFT OUTER JOIN antecedentes_familiares familiares ON (ap.codigo_paciente=familiares.codigo_paciente) 			" +
														  "				 LEFT OUTER JOIN ant_gineco_obste gineco ON (ap.codigo_paciente=gineco.codigo_paciente)							" +
														  "				 LEFT OUTER JOIN ant_medicamentos medicamentos ON (ap.codigo_paciente=medicamentos.codigo_paciente) 			" +
														  "				 LEFT OUTER JOIN antecedentes_morbidos morbidos ON (ap.codigo_paciente=morbidos.codigo_paciente)				" +
														  "				 LEFT OUTER JOIN antecedentes_pediatricos pediatricos ON (ap.codigo_paciente=pediatricos.codigo_paciente) 		" +
														  "				 LEFT OUTER JOIN antecedentes_toxicos toxicos ON (ap.codigo_paciente=toxicos.codigo_paciente) 					" +
														  "				 LEFT OUTER JOIN antece_transfusionales transfusionales ON (ap.codigo_paciente=transfusionales.codigo_paciente)	" +
														  "				 LEFT OUTER JOIN antecedentes_varios varios ON (ap.codigo_paciente=varios.codigo_paciente) 						" +
														  "				 LEFT OUTER JOIN antecedente_odontologia odontologicos ON (ap.codigo_paciente=odontologicos.cod_paciente)		" +
														  "				 LEFT OUTER JOIN antecedentes_vacunas vacunas ON (ap.codigo_paciente=vacunas.codigo_paciente)		" +
														  "						   WHERE ap.codigo_paciente = ?																			";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar
	 * todas las interconsultas que tiene este paciente .  
	 */
	/*private static final String busquedaInterconsultasStr="SELECT sol.fecha_solicitud as fechaSolicitud, sol.hora_solicitud as horaSolicitud, cc.nombre as centroCostoSolicitado, esp.nombre as especialidadSolicitada, per.primer_nombre || ' ' || per.segundo_nombre || ' ' || per.primer_apellido || ' ' || per.segundo_apellido AS medico, estcl.nombre as estadoClinico from solicitudes sol INNER JOIN cuentas cue ON(sol.cuenta=cue.id) INNER JOIN centros_costo cc ON (sol.centro_costo_solicitado=cc.codigo) INNER JOIN solicitudes_inter solinter ON(sol.numero_solicitud=solinter.numero_solicitud) INNER JOIN servicios serv ON(solinter.codigo_servicio_solicitado=serv.codigo) INNER JOIN especialidades esp ON (serv.especialidad=esp.codigo ) INNER JOIN personas per ON (sol.codigo_medico=per.codigo) INNER JOIN estados_sol_his_cli estcl ON (sol.estado_historia_clinica=estcl.codigo) where sol.tipo=" + ConstantesBD.codigoTipoSolicitudInterconsulta + " and cue.codigo_paciente=?";*/

	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar
	 * todos los datos de una admisiï¿½n de urgencias dada una cuenta  
	 */
	private static final String busquedaDatosAdmisionUrgenciasStr="SELECT adu.codigo as numeroAdmision, adu.fecha_admision as fechaAdmision, adu.hora_admision as horaAdmision,  fecha_ingreso_observacion as fechaIngresoObservacion, hora_ingreso_observacion as horaIngresoObservacion, oriurg.nombre as origenAdmision, cext.nombre as causaExterna, adu.numero_autorizacion as numeroAutorizacion from admisiones_urgencias adu INNER JOIN ori_admision_hospi oriurg ON(adu.origen_admision_urgencias=oriurg.codigo) INNER JOIN causas_externas cext ON (adu.causa_externa=cext.codigo) where adu.cuenta =?";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar
	 * todos los datos de una admisiï¿½n de hospitalizaciï¿½n dada una cuenta  
	 */
	private static final String busquedaDatosAdmisionHospitalizacionStr="SELECT adh.codigo as numeroAdmision,adh.fecha_admision as fechaAdmision, adh.hora_admision as horaAdmision, orh.nombre as origenAdmision, cext.nombre as causaExterna, diag.nombre as diagnostico, diag.tipo_cie as tipoCieDiagnostico, diag.acronimo as acronimoDiagnostico, numero_autorizacion as numeroAutorizacion from admisiones_hospi adh INNER JOIN ori_admision_hospi orh ON (adh.origen_admision_hospitalaria=orh.codigo) INNER JOIN causas_externas cext ON (adh.causa_externa=cext.codigo) INNER JOIN diagnosticos diag ON (adh. diagnostico_admision=diag.acronimo and adh.diagnostico_cie_admision=diag.tipo_cie) where adh.cuenta=?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para saber
	 * si una cuenta tiene llena su valoraciï¿½n inicial (Antes de utilizar
	 * este String en una consulta, por favor agregar el tipo de valoraciï¿½n
	 * inicial buscado - de ConstantesBD - a el final de este String)  
	 */
	private static final String tieneValoracionInicialStr="SELECT count(1) as numResultados from valoraciones val INNER JOIN  solicitudes sol ON (val.numero_solicitud=sol.numero_solicitud) where sol.cuenta=? and sol.tipo IN ";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para saber
	 * si una cuenta tiene una solicitud de algï¿½n tipo determinado  
	 * (Antes de utilizar este String en una consulta, por favor agregar 
	 * el tipo de solicitud buscado- de ConstantesBD - a el final de 
	 * este String)
	 */
	private static final String tieneSolicitudDeStr="SELECT count(1) as numResultados from solicitudes s " +
			"LEFT OUTER JOIN solicitudes_cirugia sc ON (sc.numero_solicitud = s.numero_solicitud) " +
			"where " +
			"(sc.numero_solicitud IS NULL " +
			"OR (sc.numero_solicitud IS NOT NULL AND s.tipo = "+ConstantesBD.codigoTipoSolicitudCirugia+" AND " +
					"(sc.ind_qx = '"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia+"' OR sc.ind_qx = '"+ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento+"'))) " +
			"AND s.cuenta = ? ";

	/**
	 * Cadena constante con el <i>statement</i> necesario para saber
	 * si una cuenta tiene al menos una evolucion
	 */	
	private static final String tieneEvolucionesStr="SELECT count(1) as numResultados from evoluciones evol INNER JOIN solicitudes sol ON  (evol.valoracion=sol.numero_solicitud) where sol.cuenta=?";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para saber
	 * que tipo de valoraciï¿½n es la inicial
	 */
	private static final String numeroValoracionInicialStr="SELECT "+
		"val.numero_solicitud as val " +
		"from solicitudes sol  " +
		"INNER JOIN valoraciones val on(val.numero_solicitud=sol.numero_solicitud) " +
		"where sol.cuenta=? and (sol.tipo=? or sol.tipo=? ) "+
		//MT-5571
		"AND (sol.interpretacion IS NULL OR sol.interpretacion <> '"+ConstantesBD.textoInterpretacionAutomatica+"') "+
		"order by sol.numero_solicitud";
	/**
	 * Cadena que permite saber cual es el diagnï¿½stico principal de un semiEgreso
	 */
	private static final String busquedaDiagnosticoPrincipalStr="SELECT diagnostico_principal AS diagnostico FROM egresos WHERE cuenta=?";
	
	/**
	 * Cadena con el statement necesario para insertar un docuemnto adjunto de una cuenta
	 */
	private static final String adjutarDocumentoStr=" INSERT INTO adjuntos_historia_atenciones" +
													" (paciente, nombre_original, nombre_archivo) " +
													" VALUES(?,?,?) ";
	
	/**
	 * Cadena con el statement necesario para saber  si hay adjuntos para una cuenta
	 */
	private static final String existenAdjuntosStr=" SELECT count(1) as docAdjuntos " +
												   " FROM adjuntos_historia_atenciones  aha " +
												   " WHERE aha.paciente=?";
	
	/**
	 * Cadena con el statement necesaio para consultar todos los documentos adjuntos de una cuenta
	 */
	private static final String consultarDocumentosAdjuntosStr=" SELECT aha.nombre_original as nombreOriginal, " +
															   " aha.nombre_archivo as nombreArchivo " +
															   " FROM adjuntos_historia_atenciones aha " +
															   " WHERE aha.paciente=?";
	
	/**
	 * Cadena para verificar si la cuenta tiene registro de enfermerï¿½a
	 */
	private static final String tieneRegistroEnfermeriaStr = "select codigo from registro_enfermeria where cuenta = ?";
	/**
	 * Cadena para verificar si la cuenta tiene registro de enfermerï¿½a por un rango de fecha especï¿½fico
	 */
	private static final String tieneRegistroEnfermeriaSegunFechasStr = "select count(1) as cuenta from enca_histo_registro_enfer where registro_enfer = ? ";
	
	/**
	 * Cadena que consulta las consultas PYP de la cuenta
	 */
	private static final String consultarConsultasPYPStr = "SELECT "+  
		"s.numero_solicitud AS numero_solicitud, "+ 
		"s.fecha_solicitud AS fecha_solicitud, "+
		"getnomtiposolicitud(s.tipo) As tipo, "+
		"getnombreespecialidad(getcodigoespecialidad(sc.codigo_servicio_solicitado)) As especialidad, " +
		"getcodigoespecialidad(sc.codigo_servicio_solicitado) AS codigo_especialidad, "+ 
		"getnomcentrocosto(s.centro_costo_solicitado) As centro_costo_solicitado, "+
		"getestadosolhis(s.estado_historia_clinica) AS estado_medico, "+
		"s.consecutivo_ordenes_medicas As orden," +
		"getnombreservicio(sc.codigo_servicio_solicitado,"+ConstantesBD.codigoTarifarioCups+")  As servicio "+ 
		"from solicitudes s "+ 
		"INNER JOIN solicitudes_consulta sc ON(sc.numero_solicitud=s.numero_solicitud) "+
		"where "+ 
		"s.cuenta = ? and "+ 
		"s.pyp = "+ValoresPorDefecto.getValorTrueParaConsultas();
	
	/**
	 * Cadena que verifica si existe registro de accidente de transito
	 */
	private static final String tieneRegistroAccidenteTransitoStr = "SELECT " +
		"rat.ingreso " +
		"FROM view_registro_accid_transito rat " +
		"INNER JOIN cuentas c ON(c.id_ingreso=rat.ingreso) " +
		"WHERE " +
		"c.id_ingreso = ? and " +
		"c.indicativo_acc_transito = "+ValoresPorDefecto.getValorTrueParaConsultas()+" AND rat.estado IN ";
	
	/**
	 * Cadena que verifica si existe registro de accidente de transito
	 */
	private static final String tieneRegistroEventoCatastroficoStr = "SELECT " +
		"rec.ingreso " +
		"FROM view_reg_evento_catastrofico rec " +
		"INNER JOIN cuentas c ON(c.id_ingreso=rec.ingreso) " +
		"WHERE " +
		"c.id_ingreso = ? and " +
		"c.tipo_evento = '"+ConstantesIntegridadDominio.acronimoEventoCatastrofico+"' AND rec.estado IN ";
	
	/**
	 * Cadena que verifica si existe ordenes mï¿½dicas
	 */
	private static final String existeOrdenesMedicasStr = "SELECT "+ 
		"count(1) as cuenta "+ 
		"from ordenes_medicas om "+ 
		"inner join encabezado_histo_orden_m ehom on(ehom.orden_medica=om.codigo) "+ 
		"inner join cuentas c on(c.id=om.cuenta) "+ 
		"where c.id=? and c.id_ingreso=? ";
	
	/**
	 * Cadena que verifica si existe ordenes ambulatorias
	 */
	private static final String existeOrdenesAmbulatoriasStr = "SELECT "+ 
		"count(codigo) as orden from ordenes_ambulatorias where (cuenta_solicitante=? or ingreso=?) ";
	
	/**
	 * Cadena que verifican si hay sisnos vitales
	 */
	private static final String existeSignosVitales1Str = "SELECT "+ 
		"count(1) as cuenta "+ 
		"FROM registro_enfermeria re "+ 
		"INNER JOIN enca_histo_registro_enfer ehre ON(ehre.registro_enfer=re.codigo) "+ 
		"INNER JOIN signo_vital_reg_enfer svre ON(svre.codigo_histo_enfer=ehre.codigo) "+ 
		"WHERE re.cuenta = ? ";
	private static final String existeSignosVitales2Str = "SELECT "+ 
		"count(1) as cuenta "+ 
		"FROM registro_enfermeria re "+ 
		"INNER JOIN enca_histo_registro_enfer ehre ON(ehre.registro_enfer=re.codigo) "+ 
		"INNER JOIN signo_vit_reg_enfer_par svre ON(svre.codigo_histo_enca=ehre.codigo) "+ 
		"WHERE re.cuenta = ? ";
	
	
	/**
	 * Cadena que verifica si existe resultado laboratorio
	 */
	private static final String existeResultadosLaboratorioEnfermeriaStr = "SELECT count(1) as resultados FROM MANEJOPACIENTE.resultado_laboratorio_regenf rle INNER JOIN enca_histo_registro_enfer ere ON (rle.CODIGO_HISTO_ENCA=ere.codigo) INNER JOIN registro_enfermeria re ON (ere.registro_enfer=re.codigo) WHERE re.cuenta IN (SELECT id FROM cuentas WHERE id_ingreso=?   ) ";
	
	/**
	 * Cadena que verifica si existe resultado laboratorio
	 */
	private static final String existeResultadosLaboratorioOrdenMedicaStr = "SELECT count(1) as resultados FROM manejopaciente.resultado_laboratorio_orden rlo  inner join encabezado_histo_orden_m eh on (rlo.CODIGO_HISTO_ENCA=eh.codigo)  INNER JOIN ordenes_medicas om ON (eh.orden_medica=om.codigo) where om.cuenta in (select id from cuentas where id_ingreso=?)";

	/**
	 * 
	 */
	private static final String existeValoracionEnfermeriaStr = " SELECT count(1) as valoracion FROM manejopaciente.valoracion_enfermeria ve inner join enca_histo_registro_enfer eh on (ve.CODIGO_HISTO_ENCA=eh.codigo) INNER JOIN registro_enfermeria re ON (eh.registro_enfer=re.codigo) where re.cuenta in (select id from cuentas where id_ingreso=?) ";
	
	
	/**
	 * Cadena que verifica si hay soporte respiratorio
	 */
	private static final String existeSoporteRespiratorioStr = "SELECT "+ 
		"count(1) as cuenta "+ 
		"FROM registro_enfermeria re "+ 
		"INNER JOIN enca_histo_registro_enfer ehre ON(ehre.registro_enfer=re.codigo) "+ 
		"INNER JOIN soporte_resp_enfer_valor srev ON(srev.codigo_histo_enfer=ehre.codigo) "+ 
		"WHERE re.cuenta = ? ";
	/**
	 * Cadena que verifica si hay catater sonda
	 */
	private static final String existeCateterSondaStr = "SELECT "+
		"count(1) as cuenta "+ 
		"FROM registro_enfermeria re "+ 
		"INNER JOIN enca_histo_registro_enfer ehre ON(ehre.registro_enfer=re.codigo) "+ 
		"INNER JOIN cateter_sonda_reg_enfer csre ON(csre.codigo_histo_enfer=ehre.codigo) "+ 
		"WHERE re.cuenta = ? ";
	
	/**
	 * Cadenas que verifican si existen cuidados especiales
	 */
	private static final String existeCuidadesEspeciales1Str="SELECT "+ 
		"count(1) as cuenta "+ 
		"FROM registro_enfermeria re "+ 
		"INNER JOIN enca_histo_registro_enfer ehre ON(ehre.registro_enfer=re.codigo) "+ 
		"INNER JOIN detalle_cuidado_reg_enfer dcre ON (dcre.codigo_histo_enfer=ehre.codigo) "+
		"WHERE re.cuenta = ?";
	private static final String existeCuidadesEspeciales2Str="SELECT "+ 
		"count(1) as cuenta "+ 
		"FROM ordenes_medicas om "+ 
		"INNER JOIN encabezado_histo_orden_m eho ON(eho.orden_medica=om.codigo) "+ 
		"INNER JOIN detalle_cuidado_enfer dce ON (dce.cod_histo_enca=eho.codigo) "+ 
		"WHERE om.cuenta = ? ";
	private static final String existeCuidadesEspeciales3Str="SELECT "+ 
		"count(1) as cuenta "+ 
		"FROM ordenes_medicas om "+ 
		"INNER JOIN encabezado_histo_orden_m eho ON(eho.orden_medica=om.codigo) "+ 
		"INNER JOIN detalle_otro_cuidado_enf doce ON (doce.cod_histo_cuidado_enfer=eho.codigo) "+ 
		"WHERE om.cuenta = ? ";
	private static final String existeCuidadesEspeciales4Str="SELECT "+ 
		"count(1) as cuenta "+ 
		"FROM registro_enfermeria re "+ 
		"INNER JOIN enca_histo_registro_enfer ehre ON(ehre.registro_enfer=re.codigo) "+ 
		"INNER JOIN detalle_otro_cuidado_renf docre ON (docre.codigo_histo_enfer=ehre.codigo) "+ 
		"WHERE re.cuenta = ? ";
	
	/**
	 * Cadena que verifica si hay anotaciones enfermeria
	 */
	private static final String existeAnotacionesEnfermeriaStr = "SELECT "+ 
		"count(1) as cuenta "+ 
		"FROM registro_enfermeria re "+ 
		"INNER JOIN enca_histo_registro_enfer ehre ON (ehre.registro_enfer=re.codigo) "+ 
		"INNER JOIN anotaciones_reg_enfer are ON (ehre.codigo=are.codigo_histo_enfer) "+ 
		"WHERE re.cuenta = ? ";
	
	/**
	 * Cadenas que verifican si existe hoja neurologica
	 */
	private static final String existeHojaNeurologica1Str = "SELECT "+ 
		"count(1) as cuenta "+ 
		"FROM registro_enfermeria re "+ 
		"INNER JOIN enca_histo_registro_enfer ehre ON (ehre.registro_enfer=re.codigo) "+ 
		"INNER JOIN detalle_escala_glasgow deg ON(deg.enca_histo_reg_enfer=ehre.codigo) "+
		"WHERE re.cuenta = ? ";
	private static final String existeHojaNeurologica2Str = "SELECT "+ 
		"count(1) as cuenta "+ 
		"FROM registro_enfermeria re "+ 
		"INNER JOIN enca_histo_registro_enfer ehre ON (ehre.registro_enfer=re.codigo) "+ 
		"INNER JOIN detalle_caracte_pupila dcp ON(dcp.enca_histo_reg_enfer=ehre.codigo) "+ 
		"WHERE re.cuenta = ? ";
	private static final String existeHojaNeurologica3Str = "SELECT "+ 
		"count(1) as cuenta "+ 
		"FROM registro_enfermeria re "+ 
		"INNER JOIN enca_histo_registro_enfer ehre ON (ehre.registro_enfer=re.codigo) "+ 
		"INNER JOIN detalle_convulsion dc ON(dc.enca_histo_reg_enfer=ehre.codigo) "+ 
		"WHERE re.cuenta = ? ";
	private static final String existeHojaNeurologica4Str = "SELECT "+ 
		"count(1) as cuenta "+ 
		"FROM registro_enfermeria re "+ 
		"INNER JOIN enca_histo_registro_enfer ehre ON (ehre.registro_enfer=re.codigo) "+ 
		"INNER JOIN detalle_control_esfinteres dce ON(dce.enca_histo_reg_enfer=ehre.codigo) "+
		"WHERE re.cuenta = ? ";
	private static final String existeHojaNeurologica5Str = "SELECT "+ 
		"count(1) as cuenta "+ 
		"FROM registro_enfermeria re "+ 
		"INNER JOIN enca_histo_registro_enfer ehre ON (ehre.registro_enfer=re.codigo) "+ 
		"INNER JOIN detalle_fuerza_muscular dfm ON(dfm.enca_histo_reg_enfer=ehre.codigo) "+ 
		"WHERE re.cuenta = ? ";
	
	/**
	 * Cadena que valida si existe administracion de medicamentos
	 */
	private static final String existeAdminMedicamentosStr = "SELECT "+ 
		"count(1) as cuenta "+ 
		"FROM solicitudes s "+ 
		"INNER JOIN admin_medicamentos am on(am.numero_solicitud=s.numero_solicitud) "+ 
		"INNER JOIN detalle_admin da on(da.administracion=am.codigo) "+ 
		"INNER JOIN articulo a ON(a.codigo=da.articulo) " +
		"INNER JOIN naturaleza_articulo na on(na.acronimo=a.naturaleza and na.institucion=a.institucion and na.es_medicamento='"+ConstantesBD.acronimoSi+"')"+ 
		"WHERE s.cuenta = ?  ";
	
	/**
	 * Cadena que valida si existe consumo de insumos
	 */
	private static final String existeConsumoInsumosStr = "SELECT "+ 
		"count(1) as cuenta "+ 
		"from solicitudes s "+ 
		"INNER JOIN admin_medicamentos am on(am.numero_solicitud=s.numero_solicitud) "+ 
		"INNER JOIN detalle_admin da on(da.administracion=am.codigo) "+ 
		"INNER JOIN articulo a ON(a.codigo=da.articulo) " +
		"INNER JOIN naturaleza_articulo na ON(na.acronimo=a.naturaleza and a.institucion=na.institucion) "+ 
		"where s.cuenta = ? and na.es_medicamento='"+ConstantesBD.acronimoNo+"' ";
	
	/**
	 * Cadena que verifica si hay respuesta o interpretacion de interconsultas
	 */
	private static final String existeRespIntInterconsultaStr = "SELECT "+ 
		"count(1) as cuenta "+ 
		"FROM solicitudes s "+ 
		"inner join valoraciones v ON(v.numero_solicitud=s.numero_solicitud) "+ 
		"WHERE s.cuenta = ? and s.tipo = "+ConstantesBD.codigoTipoSolicitudInterconsulta+" ";
	
	/**
	 * Cadena que verifica si hay repsuesta de procedimientos
	 */
	private static final String existeRespuestaProcedimientosStr = "SELECT "+ 
		"count(1) as cuenta "+ 
		"from solicitudes s "+ 
		"inner join res_sol_proc rsp on(rsp.numero_solicitud=s.numero_solicitud) "+ 
		"WHERE s.cuenta = ? ";
	
	/**
	 * Cadena que verifica si existe respuesta de cirugias
	 */
	private static final String existeRespuestaCirugiasStr = "SELECT "+
		"count(1) as cuenta "+ 
		"FROM solicitudes s "+ 
		"INNER JOIN solicitudes_cirugia sc ON(sc.numero_solicitud=s.numero_solicitud " +
		"	AND sc.ind_qx IN('"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia+"','"+ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento+"') ) "+ 
		"LEFT OUTER JOIN hoja_quirurgica hq ON(hq.numero_solicitud=sc.numero_solicitud) " +
		"LEFT OUTER JOIN hoja_anestesia ha ON(ha.numero_solicitud=sc.numero_solicitud) "+ 
		"WHERE "+ 
		"s.cuenta = ?  ";
	
	/**
	 * Cadena que consulta el listado de citas 
	 */
	private static final String listarCitasStr = "SELECT DISTINCT "+ 
		"c.codigo AS codigo_cita, "+ 
		"a.centro_atencion as codigo_centro_atencion, "+ 
		"getnomcentroatencion(a.centro_atencion) as nombre_centro_atencion, "+ 
		"c.unidad_consulta AS codigo_unidad_consulta, "+
		"getnombreunidadconsulta(c.unidad_consulta) AS nombre_unidad_consulta, "+
		"a.fecha AS fecha_inicio, "+ 
		"a.hora_inicio AS hora_inicio, "+ 
		"coalesce(getnombrepersona(a.codigo_medico),'') AS nombre_completo_medico, "+ 
		"coalesce(a.codigo_medico,0) AS codigo_medico, "+ 
		"co.descripcion AS nombre_consultorio, "+ 
		"ec.nombre AS nombre_estado_cita, "+ 
		"c.estado_cita AS codigo_estado_cita, "+ 
		"el.nombre AS nombre_estado_liquidacion "+ 
		"FROM cita c "+ 
		"INNER JOIN agenda a ON(c.codigo_agenda=a.codigo) "+ 
		"INNER JOIN consultorios co ON(a.consultorio=co.codigo) "+ 
		"INNER JOIN estados_cita ec ON(c.estado_cita=ec.codigo) "+ 
		"INNER JOIN estados_liquidacion el ON(c.estado_liquidacion=el.acronimo) "+ 
		"WHERE "+ 
		"c.codigo IN (select sc.codigo_cita from servicios_cita sc inner join solicitudes s on(s.numero_solicitud=sc.numero_solicitud) WHERE s.cuenta = ?) ";
	
	/**
	 * Cadena que consulta el listado de Solicitudes citas 
	 */
	private static final String listarSolicitudesCitasStr = "SELECT "+
	"s.numero_solicitud AS numero_solicitud, "+
	"coalesce(s.consecutivo_ordenes_medicas||'','') as consecutivo_ordenes_medicas,  "+
	"'(' || ser.especialidad || '-' || ser.codigo || ') ' || getnombreservicio(ser.codigo,"+ConstantesBD.codigoTarifarioCups+") AS nombre_servicio,  "+
	"s.estado_historia_clinica AS codigo_estado_medico,  "+
	"getestadosolhis(s.estado_historia_clinica) AS nombre_estado_medico,  "+
	"coalesce(s.pyp,"+ValoresPorDefecto.getValorFalseParaConsultas()+") AS pyp, "+
	"ser.tipo_servicio AS tipo_servicio, "+
	"coalesce(sci.especialidad,ser.especialidad) AS codigo_especialidad "+
	"FROM "+
	"ordenes.solicitudes s "+
	"inner join ordenes.solicitudes_consulta sc  "+
	"ON (s.numero_solicitud = sc.numero_solicitud) "+
	"inner join facturacion.servicios ser "+
	"ON (sc.codigo_servicio_solicitado = ser.codigo) "+
	"INNER JOIN servicios_cita sci " +
	"ON (sci.numero_solicitud = s.numero_solicitud) "+
	"where s.tipo = 3 "+
	"AND s.cuenta = ? ";
	
	/**
	 * Cadena que lista los servicios de la cita
	 */
	private static final String listarServiciosCitaStr = "SELECT "+ 
		"c.codigo_paciente AS codigo_paciente, "+ 
		"getnombreunidadconsulta(c.unidad_consulta) AS nombre_unidad_consulta, "+
		"c.estado_cita AS codigo_estado_cita, "+
		"s.estado_historia_clinica AS codigo_estado_medico, "+
		"getestadosolhis(s.estado_historia_clinica) AS nombre_estado_medico, "+
		"'(' || ser.especialidad || '-' || ser.codigo || ') ' || getnombreservicio(sc.servicio,"+ConstantesBD.codigoTarifarioCups+") AS nombre_servicio, "+
		"coalesce(a.codigo_medico,0) AS codigo_medico, "+  
		"ser.tipo_servicio as tipo_servicio, "+
		"coalesce(s.consecutivo_ordenes_medicas||'','') as consecutivo_ordenes_medicas, "+ 
		"coalesce(sc.numero_solicitud,0) AS numero_solicitud, "+ 
		"coalesce(s.cuenta,0) AS cuenta, "+ 
		"getnombrepersona(c.codigo_paciente) AS nombre_completo_persona, "+ 
		"coalesce(sc.observaciones,'') AS observaciones, "+ 
		"coalesce(s.pyp,"+ValoresPorDefecto.getValorFalseParaConsultas()+") AS pyp," +
		"coalesce(res.codigo,0) As codigo_respuesta, " +
		"coalesce(sc.especialidad,"+ConstantesBD.codigoNuncaValido+") AS codigo_especialidad, "+
		"coalesce(c.motivo_noatencion,'') AS motivo_no_atencion "+ 
		"FROM cita c "+ 
		"INNER JOIN agenda a ON(c.codigo_agenda=a.codigo) "+ 
		"INNER JOIN servicios_cita sc ON(sc.codigo_cita=c.codigo) "+
		"INNER JOIN solicitudes s on(s.numero_solicitud=sc.numero_solicitud) "+ 
		"INNER JOIN servicios ser ON(ser.codigo=sc.servicio) "+ 
		"LEFT OUTER JOIN res_sol_proc res ON (res.numero_solicitud = sc.numero_solicitud) " +
		"WHERE "+ 
		"c.codigo = ?";
	
	/**
	 * Mï¿½todo que consulta los archivos adjuntos de la respuesta de procedimientos
	 */
	private static final String consultarAdjuntosProcedimientosStr = "SELECT nombre_archivo,nombre_original FROM doc_adj_solicitud WHERE numero_solicitud = ? AND es_codigo_resp_sol = ?";
	
	/**
	 * Cadena que verifica si el paciente tiene triage
	 */
	private static final String existeTriageStr = "select count(1) as cuenta from triage where tipo_identificacion = ? and numero_identificacion = ?";
	
	/**
	 * Cadena que consulta los registros triage del paciente
	 */
	private static final String cargarListadoTriageStr = "SELECT "+  
		"consecutivo, "+ 
		"consecutivo_fecha, "+ 
		"to_char(fecha,'DD/MM/YYYY') As fecha, "+ 
		"substr(hora,0,6) As hora, "+ 
		"primer_nombre, "+ 
		"segundo_nombre, "+ 
		"primer_apellido, "+ 
		"segundo_apellido, "+ 
		"acro_tipo_identificacion, "+ 
		"numero_identificacion, "+ 
		"case when numero_triage is null then 0 else numero_triage end as numero_triage, "+ 
		"case when categoria_triage is null then '' else categoria_triage end as categoria_triage, "+ 
		"case when destino is null then '' else destino end as destino, "+ 
		"case when colornombre is null then '' else colornombre end as colornombre, "+ 
		"case when nombresala is null then '' else nombresala end as nombresala, "+ 
		"case when urgencias is null then '' else urgencias end as urgencias, "+ 
		"case when numeroadmision is null then '' else numeroadmision || '' END as numeroadmision "+ 
		"from view_triage "+ 
		"where acro_tipo_identificacion = ? and numero_identificacion = ?";
	
	
	/**
	 * Cadena de consulta del Historial de Consentimientos Informados 
	 * */
	private static final String consultaHistorialConsentimientoInf = "SELECT " +			
			"ingreso," +
			"servicio," +
			"servicio || ' - ' || getcodigopropservicio2(servicio,"+ConstantesBD.codigoTarifarioCups+") || '  ' || getnombreservicio(servicio,"+ConstantesBD.codigoTarifarioCups+") AS descripcionservicio," +
			"codigoconsentimiento," +
			"institucion," +
			"descripcion," +
			"nombre_archivo AS nombrearchivo," +
			"getnombreusuario(usuario_imprime) AS usuarioimprime," +
			"to_char(fecha_imprime,'DD/MM/YYYY') AS fechaimprime," +
			"hora_imprime AS horaimprime " +
			"FROM historialconsentimientoinf " +
			"WHERE ingreso = ? " +
			"ORDER BY fecha_imprime DESC "; 
	
	
	//Atributos Anexo 550. Cargos Directos*******************************************************************************
	//*******************************************************************************************************************
	
	/**
	 * Cadena de Consulta listado de Solicitudes tipo Cargo Directo
	 * */
	private static final String strConsultarListadoSolCargosDirectos ="SELECT " +
			"s.numero_solicitud," +
			"TO_CHAR(s.fecha_solicitud,'DD/MM/YYYY') AS fecha_solicitud, " +
			"s.hora_solicitud," +
			"s.tipo AS tipo_solicitud," +
			"s.centro_costo_solicitante," +			
			"getnomcentrocosto(s.centro_costo_solicitante) AS desc_cc_solicitante," +
			"s.centro_costo_solicitado," +
			"getnomcentrocosto(s.centro_costo_solicitado) AS desc_cc_solicitado,"+			
			"s.especialidad_solicitante," +			
			"getnombreespecialidad(s.especialidad_solicitante) AS desc_esp_solicitante," +
			"CASE WHEN sc.codigo_peticion IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE sc.codigo_peticion END AS codigo_peticion " +
			"FROM solicitudes s " +
			"LEFT OUTER JOIN solicitudes_cirugia sc ON(sc.numero_solicitud = s.numero_solicitud) " +
			"WHERE " +
			"s.cuenta = ? " +
			"AND s.estado_historia_clinica = "+ConstantesBD.codigoEstadoHCCargoDirecto+" "+
			"AND (sc.numero_solicitud IS NULL OR " +
			"(s.tipo = "+ConstantesBD.codigoTipoSolicitudCirugia+" " +
			" AND sc.ind_qx IN ('"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugiaCargoDirecto+"','"+ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruentoCargoDirecto+"'))) ";			
	
	/**
	 * Consulta el detalle de la Solicitud tipo Cargos Directos Articulos
	 * */
	private static final String strConsultarDetalleArticuloCDirecto = "SELECT " +
			"ds.numero_solicitud," +
			"ds.articulo," +
			"getdescripcionarticulo(ds.articulo) AS descripcion_articulo," +
			"getunidadmedidaarticulo(ds.articulo) AS unidad_articulo," +
			"getdespacho(ds.articulo,ds.numero_solicitud) as cantidad, " +
			"na.es_pos AS espos " +
			"FROM detalle_solicitudes ds " +
			"INNER JOIN articulo a ON (ds.articulo=a.codigo) " +
			"INNER JOIN naturaleza_articulo na ON (a.naturaleza=na.acronimo AND a.institucion=na.institucion) " +
			"WHERE numero_solicitud = ? ";
	
	/**
	 * Consulta el Detalle de la Solicitud Tipo Cargo Directo de Servicios
	 * */
	private static final String strConsultaDetalleServiciosCDirecto = 
			"SELECT "+
			"cd.numero_solicitud," +
			"cd.servicio_solicitado AS servicio," +
			"gettiposervicio(cd.servicio_solicitado) AS tipo_servicio," +
			"getnombreservicio(cd.servicio_solicitado,"+ConstantesBD.codigoTarifarioCups+") AS descripcion_servicio," +
			"getcodigopropservicio(cd.servicio_solicitado,"+ConstantesBD.codigoTarifarioCups+") AS codigo_cups, " +
			"s.espos AS es_pos," +
			"TO_CHAR(cdhc.fecha_solicitud,'DD/MM/YYYY') AS fecha_c_p," +
			"cdhc.codigo AS codigo_cdirecto_hc," +
			"cdhc.hora_solicitud AS hora_c_p," +
			"cdhc.causa_externa," +
			"ce.nombre AS desc_causa_externa, " +
			"cdhc.finalidad_consulta," +
			"fc.nombre AS desc_fin_consulta, " +
			"cdhc.finalidad_procedimiento," +
			"fs.nombre AS desc_fin_procedimiento " +			
			"FROM cargos_directos cd " +
			"INNER JOIN servicios s ON (s.codigo = cd.servicio_solicitado) " +
			"LEFT OUTER JOIN cargos_directos_hc cdhc ON (cd.codigo_datos_hc = cdhc.codigo) " +
			"LEFT OUTER JOIN causas_externas ce ON(ce.codigo = cdhc.causa_externa) " +			
			"LEFT OUTER JOIN finalidades_consulta fc ON(fc.acronimo = cdhc.finalidad_consulta) " +
			"LEFT OUTER JOIN finalidades_servicio fs ON(fs.codigo = cdhc.finalidad_procedimiento) " +			
			"WHERE " +
			"numero_solicitud = ? ";	
	
	/***
	 * Consulta los diagnosticos de la Solicitud Tipo Cargo Directo de Servicios
	 * */
	private static final String strConsultaDiagnosticoServiciosCD = "SELECT " +
			"d.codigo," +
			"d.acronimo_diagnostico," +
			"CASE WHEN d.acronimo_diagnostico IS NULL THEN '' ELSE getnombrediagnostico(d.acronimo_diagnostico,d.tipo_cie_diagnostico) END AS descripcion_diagnostico," +
			"CASE WHEN d.tipo_diagnostico IS NULL THEN '' ELSE getintegridaddominio(td.acronimo) END AS descripcion_tipo_diagnostico," +
			"d.principal," +
			"d.complicacion " +
			"FROM " +
			"diag_cargos_directos_hc d " +
			"LEFT OUTER JOIN tipos_diagnostico td ON (td.acronimo = d.tipo_diagnostico) " +
			"WHERE " +
			"d.codigo_cargo_directo = ? ";	
	
	/**
	 * Consulta los servicios de la solicitud de cirugia
	 * */
	private static final String strConsultaServiciosCirugiaCD = "SELECT " +
			"scps.servicio, " +
			"getcodigopropservicio(scps.servicio,"+ConstantesBD.codigoTarifarioCups+") AS codigo_cups," +
			"getnombreservicio(scps.servicio,"+ConstantesBD.codigoTarifarioCups+") AS descripcion_servicio," +			
			"scps.especialidad," +
			"getnomespecialidadservicio(scps.servicio) AS descripcion_especialidad " +
			"FROM " +
			"sol_cirugia_por_servicio scps " +
			"WHERE numero_solicitud = ? ";
	
	/**
	 * Consulta la informacion de las cuentas de un paciente
	 * */
	private static final String getCuentasPacienteStr = "" +
			"SELECT " +
			"c.via_ingreso AS viaIngreso," +
			"vi.nombre AS descripcionViaIngreso," +
			"c.tipo_paciente AS tipoPaciente," +
			"tp.nombre AS descripcionTipoPaciente," +
			"c.id AS cuenta," +
			"c.estado_cuenta AS estadoCuenta," +
			"getnombreestadocuenta(c.estado_cuenta) AS descripcionEstadoCuenta," +
			"c.id_ingreso AS idIngreso " +			
			"FROM cuentas c " +
			"LEFT OUTER JOIN vias_ingreso vi ON (vi.codigo = c.via_ingreso) " +
			"LEFT OUTER JOIN tipos_paciente tp ON (tp.acronimo = c.tipo_paciente) " +
			"WHERE c.id_ingreso = ? and escuentacerradaasocio(c.id) = '"+ConstantesBD.acronimoNo+"' " +
			"ORDER BY c.fecha_apertura DESC, c.hora_apertura DESC, c.id DESC ";
			
	
	//*******************************************************************************************************************
	//*******************************************************************************************************************
	
	/**
	 * Implementaciï¿½n de la revisiï¿½n de los antecedentes que existen
	 * para paciente dado en una BD Genï¿½rica
	 * 
	 * @see com.princetonsa.dao.ResumenAtencionesDao#queAntecedentesExisten (Connection , int ) throws SQLException
	 */
	public static Collection queAntecedentesExisten (Connection con, int idPaciente) throws SQLException
	{
		if (ejecucionGenericaBoolean (con, idPaciente, tieneAntecedentesPacienteStr) )
		{
			return ejecucionGenerica(con, idPaciente, queAntecedentesExistenStr);
		}
		else
		{
			return null;
		}
	}

	/**
	 * Implementaciï¿½n de la bï¿½squeda de todas las cuentas para un
	 * paciente dado en una BD Genï¿½rica
	 * 
	 * @see com.princetonsa.dao.ResumenAtencionesDao#busquedaCuentas (Connection , int ) throws SQLException
	 */
	public static Collection busquedaCuentas (Connection con, int idPaciente) throws SQLException
	{
		logger.info("busquedaCuentas=> "+busquedaCuentasStr+", idPaciente=> "+idPaciente);
		return ejecucionGenerica(con, idPaciente, busquedaCuentasStr);
	}
	
	/**
	 * Implementaciï¿½n de la bï¿½squeda de los datos de la admisiï¿½n
	 * de urgencias para una cuenta dada en una BD Genï¿½rica
	 * 
	 * @see com.princetonsa.dao.ResumenAtencionesDao#busquedaDatosAdmisionUrgencias (Connection , int ) throws SQLException
	 */
	public static Collection busquedaDatosAdmisionUrgencias (Connection con, int idCuenta) throws SQLException
	{
		return ejecucionGenerica(con, idCuenta, busquedaDatosAdmisionUrgenciasStr);
	}

	/**
	 * Implementaciï¿½n de la bï¿½squeda de los datos de la admisiï¿½n
	 * de hospitalizaciï¿½n para una cuenta dada en una BD Genï¿½rica
	 * 
	 * @see com.princetonsa.dao.ResumenAtencionesDao#busquedaDatosAdmisionHospitalizacion (Connection , int ) throws SQLException
	 */
	public static Collection busquedaDatosAdmisionHospitalizacion (Connection con, int idCuenta) throws SQLException
	{
		return ejecucionGenerica(con, idCuenta, busquedaDatosAdmisionHospitalizacionStr);
	}

	/**
	 * Implementaciï¿½n de la revisiï¿½n de si existe valoraciï¿½n inicial
	 * para una cuenta dada en una BD Genï¿½rica
	 * 
	 * @see com.princetonsa.dao.ResumenAtencionesDao#tieneValoracionInicial (Connection , int , boolean ) throws SQLException
	 */
	public static boolean tieneValoracionInicial (Connection con, int idCuenta, boolean buscandoValoracionInicialUrgencias,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal) throws SQLException
	{
		String tieneValoracionInicialActualStr=tieneValoracionInicialStr;
		if (buscandoValoracionInicialUrgencias)
		{
			tieneValoracionInicialActualStr=tieneValoracionInicialActualStr + " ("+ ConstantesBD.codigoTipoSolicitudInicialUrgencias+") ";
		}
		else
		{
			tieneValoracionInicialActualStr=tieneValoracionInicialActualStr + " ("+ ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+") ";
		}
		//tieneValoracionInicialActualStr += " AND val.cuidado_especial = '"+ConstantesBD.acronimoNo+"' ";
		//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
		if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
		{
			if(!horaInicial.equals("")&&!horaFinal.equals(""))
				tieneValoracionInicialActualStr += " and (" +
					"to_char(sol.fecha_solicitud,'yyyy-mm-dd')||' '||sol.hora_solicitud between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
					"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";	
			else
				tieneValoracionInicialActualStr += " and (to_char(sol.fecha_solicitud,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
		}
		//***************************************************************************
		//tieneValoracionInicialActualStr += " ORDER BY val.numero_solicitud";
		logger.info("Verificar si hay valoracion inicial=> "+tieneValoracionInicialActualStr);
		return ejecucionGenericaBoolean(con, idCuenta, tieneValoracionInicialActualStr);
	}
	
	/**
	 * Implementaciï¿½n de la revisiï¿½n de si existe una solicitud dado
	 * el tipo para una cuenta dada en una BD Genï¿½rica
	 * 
	 * @see com.princetonsa.dao.ResumenAtencionesDao#tieneSolicitudDe (Connection , int , int ) throws SQLException
	 */
	public static boolean tieneSolicitudDe (Connection con, int idCuenta,int codigoTipoSolicitudBuscada,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal) 
	{
		try
		{
			String tieneValoracionDeParticularStr=tieneSolicitudDeStr ;
			//codigoTipoSolicitudBuscada;
			if(codigoTipoSolicitudBuscada==ConstantesBD.codigoTipoSolicitudInterconsulta)
			{
				tieneValoracionDeParticularStr += " and s.tipo = "+ConstantesBD.codigoTipoSolicitudInterconsulta+" ";
			}
			else
			{
				tieneValoracionDeParticularStr += " and s.tipo = "+codigoTipoSolicitudBuscada;
			}
			
			
			//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
			if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
			{
				if(!horaInicial.equals("")&&!horaFinal.equals(""))
					tieneValoracionDeParticularStr += " and (" +
							"to_char(s.fecha_solicitud,'yyyy-mm-dd')||' '||s.hora_solicitud between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
							"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";
				
				else
					tieneValoracionDeParticularStr += " and (to_char(s.fecha_solicitud,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
				
			}
			//***************************************************************************
			
			return  ejecucionGenericaBoolean(con, idCuenta, tieneValoracionDeParticularStr); 
		}
		catch(SQLException e)
		{
			logger.error("Error en tieneSolicitudDe de SQlBaseResumenAtencionesDao: "+e);
			return false;
		}
	}
	
	/**
	 * Implementaciï¿½n de la revisiï¿½n de la bï¿½squeda del tipo de
	 * valoraciï¿½n para una cuenta dada en una BD Genï¿½rica
	 * 
	 * @see com.princetonsa.dao.ResumenAtencionesDao#tipoValoracionInicial (Connection , int ) throws SQLException
	 */
	public static int numeroValoracionInicial (Connection con, int idCuenta) throws SQLException
	{
		int numeroSolicitud = ConstantesBD.codigoNuncaValido;
		PreparedStatementDecorator tipoValoracionInicialStatement= new PreparedStatementDecorator(con.prepareStatement(numeroValoracionInicialStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		tipoValoracionInicialStatement.setInt(1, idCuenta);
		tipoValoracionInicialStatement.setInt(2, ConstantesBD.codigoTipoSolicitudInicialHospitalizacion);
		tipoValoracionInicialStatement.setInt(3, ConstantesBD.codigoTipoSolicitudInicialUrgencias);
		ResultSetDecorator rs=new ResultSetDecorator(tipoValoracionInicialStatement.executeQuery());
		if (rs.next())
		{
			numeroSolicitud = rs.getInt("val");
		}
		//No tiene ni siquiera solicitud
		return numeroSolicitud;
		
	}
	
	/**
	 * Implementaciï¿½n de la revisiï¿½n de si existen evoluciones para 
	 * una cuenta dada en una BD Genï¿½rica
	 * 
	 * @see com.princetonsa.dao.ResumenAtencionesDao#tieneEvoluciones (Connection , int ) throws SQLException
	 */
	public static boolean tieneEvoluciones (Connection con, int idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal) throws SQLException
	{
		String consulta = tieneEvolucionesStr;
		//******SE VALIDAN LAS FECHAS Y LAS HORAS*********************
		if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
		{
			if(!horaInicial.equals("")&&!horaFinal.equals(""))
				consulta += " and (" +
					"to_char(evol.fecha_evolucion,'yyyy-mm-dd')||' '||evol.hora_evolucion between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
					"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";	
			else
				consulta += " and (to_char(evol.fecha_evolucion,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
		}
		
		
		return  ejecucionGenericaBoolean(con, idCuenta, consulta);
	}
	
	
	/**
	 * Adiciï¿½n de Sebastiï¿½n
	 * Mï¿½todo que consulta el diagnositco principal de semiEgreso en Urgencias
	 * para saber si es necesario crear el link "egresos" en el resumen de la Cuenta
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static int busquedaDiagnosticoPrincipal(Connection con,int idCuenta){
		try{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(busquedaDiagnosticoPrincipalStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,idCuenta);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next()){
				String resp=rs.getString("diagnostico");
				if(resp.equals("1")){
					return 0;
				}
				else{
					return -1;
				}
			}
			else{
				return 0;
			}
		}
		catch(SQLException e){
			logger.error("Error buscando diagnostico principal del egreso en SqlBaseResumenAtencionesDao: "+e);
			return 0;
			
		}
	}
	/**
	 * Implementaciï¿½n de la bï¿½squeda de la evoluciï¿½n que generï¿½ el egreso
	 * en una BD Genï¿½rica
	 * 
	 * @see com.princetonsa.dao.ResumenAtencionesDao#codigoEvolucionGeneroEgreso (Connection , int ) throws SQLException
	 */
	public static int codigoEvolucionGeneroEgreso (Connection con, int idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal) throws SQLException
	{
		String busquedaEvolucionGeneroEgresoStr="SELECT evolucion from egresos where cuenta=? and " +
			"((fecha_egreso>fecha_reversion_egreso) OR (fecha_egreso=fecha_reversion_egreso and hora_egreso>hora_reversion_egreso) OR (fecha_reversion_egreso is null and hora_reversion_egreso is null)) ";
		
		//******SE VALIDAN LAS FECHAS Y LAS HORAS*********************
		if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
		{
			if(!horaInicial.equals("")&&!horaFinal.equals(""))
				busquedaEvolucionGeneroEgresoStr += " and (" +
					"to_char(fecha_egreso,'yyyy-mm-dd')||' '||hora_egreso between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
					"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";
			else
				busquedaEvolucionGeneroEgresoStr += " and (to_char(fecha_egreso,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
		}
		
		PreparedStatementDecorator busquedaEvolucionGeneroEgresoStatement= new PreparedStatementDecorator(con.prepareStatement(busquedaEvolucionGeneroEgresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		busquedaEvolucionGeneroEgresoStatement.setInt(1, idCuenta);
		ResultSetDecorator rs=new ResultSetDecorator(busquedaEvolucionGeneroEgresoStatement.executeQuery());
		if (rs.next())
		{
			
			int resp=rs.getInt("evolucion");
			rs.close();
			
			if(resp==0)
				resp = -3; //se utiliza para casos de semiegreso que ya tienen reversion
			
			return resp;
		}
		else
		{
			rs.close();
			
			return 0;
		}
	}

	/**
	 * Debido a que la mayorï¿½a de consultas de esta clase solo involucran
	 * el establecimiento de un parï¿½metro (llaveTabla), se centralizï¿½ toda
	 * la funcionalidad de la consulta como tal, cuando necesitamos un
	 * conjunto como respuesta
	 * 
	 * @param con
	 * @param llaveTabla
	 * @param consulta
	 * @return
	 * @throws SQLException
	 */
	private static Collection ejecucionGenerica (Connection con, int llaveTabla, String consulta) throws SQLException
	{
		logger.info("------->"+consulta);
		
		PreparedStatement ps = con.prepareStatement(consulta);
		ps.setInt(1, llaveTabla);
		ResultSet rs2 = ps.executeQuery();
		
		PreparedStatementDecorator ejecucionGenericaStatement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		ejecucionGenericaStatement.setInt(1, llaveTabla);
		ResultSetDecorator rs=new ResultSetDecorator(ejecucionGenericaStatement.executeQuery());
		Collection aRetornar=UtilidadBD.resultSet2Collection(rs);
		rs.close();
		return aRetornar;
	}


	/**
	 * Funcion para verificar si existen antecedentes Oftalmologicos familiares y/o personales. 
	 * @param con
	 * @param paciente
	 * @return
	 * @throws SQLException
	 */
	public static boolean [] existenAnteOftal (Connection con, int paciente) throws SQLException
	{
		PreparedStatementDecorator ps = null;
		boolean Vec[] = new boolean [2];
		Vec[0] = false; Vec[1] = false;
		
		try
		{	
			//----consulta para saber si hay antecedentes Personales  
			String cad	 = "	SELECT * FROM ant_oftal_personales WHERE paciente = ? "; 
			ps= new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, paciente);		
			ResultSetDecorator resultado=new ResultSetDecorator(ps.executeQuery());

			if(resultado.next())  { Vec [0] = true; } 
			else 				  {	Vec [0] = false; }
			
			//----consulta para saber si hay antecedentes Familiares 
			cad	 = "	SELECT * FROM ant_oftal_familiares WHERE paciente = ? "; 
			ps= new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, paciente);		
			resultado=new ResultSetDecorator(ps.executeQuery());
			if(resultado.next())  { Vec [1] = true; } 
			else 				  {	Vec [1] = false; }
				
			return Vec;    
		}
		catch(SQLException e)
		{
			logger.warn(" Error verificando si el paciente tiene antecedentes personales y/ï¿½ familiares : SqlBaseResumenAtencionesDao "+e.toString());
			return  Vec;
		}
	}

	
	/**
	 * Debido a que la mayorï¿½a de consultas de esta clase solo involucran
	 * el establecimiento de un parï¿½metro (llaveTabla), se centralizï¿½ toda
	 * la funcionalidad de la consulta como tal, cuando necesitamos un
	 * boolean que nos diga si un elemento existe o no 
	 * 
	 * @param con
	 * @param idPaciente
	 * @param consulta
	 * @return
	 * @throws SQLException
	 */
	private static boolean ejecucionGenericaBoolean (Connection con, int llaveTabla, String consulta) throws SQLException
	{
		PreparedStatementDecorator ejecucionGenericaStatement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		ejecucionGenericaStatement.setInt(1, llaveTabla);
		ResultSetDecorator rs=new ResultSetDecorator(ejecucionGenericaStatement.executeQuery());
		if (rs.next())
		{
			if (rs.getInt("numResultados")>0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			throw new SQLException ("Error Ejecutando un count en ejecucionGenericaBoolean (SqlBaseResumenAtencionesDao)");
		}
	}

	/**
	 * Bï¿½squeda de citas para pacientes de consulta externa
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public static boolean existenCitas(Connection con, int idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal) throws SQLException
	{
		String consulta = existenCitasStr;
		boolean existe = false;
		//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
		if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
		{
			if(!horaInicial.equals("")&&!horaFinal.equals(""))
				consulta += " and (" +
						"(to_char(a.fecha,'yyyy-mm-dd')||' '||a.hora_inicio between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
						"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"') OR " +
						"(to_char(a.fecha,'yyyy-mm-dd')||' '||a.hora_fin between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
						"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')" +
					") ";
			else
				consulta += " and (to_char(a.fecha,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
			
		}
		//***************************************************************************
		logger.info("consultaCitas=> "+consulta+", idCuenta=>"+idCuenta);
		PreparedStatementDecorator prepared= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		prepared.setInt(1, idCuenta);
		ResultSetDecorator resultadoCitas=new ResultSetDecorator(prepared.executeQuery());
		if(resultadoCitas.next())
			if(resultadoCitas.getInt("cuenta")>0)
				existe = true;
		
		return existe;
	}
	
	/**
	 * Mï¿½todo para insertar los documentos adjuntos de una cuenta en la 
	 * Historia de Atenciones(Resumen de Atenciones)
	 * @param con
	 * @param idCuenta
	 * @param nombreOriginal
	 * @param nombreArchivo
	 * @return
	 * @throws SQLException
	 */
	public static int adjuntarDocumento(Connection con, int idPaciente, String nombreOriginal, String nombreArchivo) throws SQLException
	{
		int resp=0;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(adjutarDocumentoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, idPaciente);
			ps.setString(2, nombreOriginal);
			ps.setString(3, nombreArchivo);
		
		    resp=ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn("Error insertando los documentos adjuntos : SqlBaseResumenAtencionesDao "+e.toString());
			resp= -1;
		}
		return resp;
		
	}
	
	/**
	 * Mï¿½todo para saber si hay documentos adjuntos
	 * @param con
	 * @param idCuenta
	 * @return
	 * @throws SQLException
	 */
	public static int existenAdjuntos(Connection con, int idPaciente) throws SQLException
	{
		int resp=0;
		ResultSetDecorator rs;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(existenAdjuntosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		ps.setInt(1, idPaciente);
			
		   rs=new ResultSetDecorator(ps.executeQuery());
		   if(rs.next())
		   {
		   	 	resp=rs.getInt("docAdjuntos");
		   }
		}
		catch(SQLException e)
		{
			logger.warn("Error consultando la cantidad de documentos adjuntos : SqlBaseResumenAtencionesDao "+e.toString());
			resp= -1;
		}
		return resp;
		
	}
	
	/**
	 * Mï¿½todod para consultar la fecha - hora de todas las notas de recuperacion
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarDocumentosAdjuntos (Connection con, int idPaciente) throws SQLException
	{
		try
		{
			int numDoc=existenAdjuntos(con,idPaciente);
			if(numDoc>0)
			{
				PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultarDocumentosAdjuntosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1, idPaciente);
				
				HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
				pst.close();
				return mapaRetorno;
			}
			else
			{
				return new HashMap();
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando los documentos adjuntos: "+e);
			return new HashMap();
		}
	}
	
	/**
	 * Mï¿½todo implementado para verificar si a la cuenta se le ingresï¿½
	 * registro de enfermerï¿½a
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static boolean tieneRegistroEnfermeria(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		try
		{
			boolean tiene = false;
			String codigo = "";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(tieneRegistroEnfermeriaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,idCuenta);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				codigo = rs.getString("codigo");
			
			if(!codigo.equals(""))
			{
				String consulta = tieneRegistroEnfermeriaSegunFechasStr;
				//******SE VALIDAN LAS FECHAS Y LAS HORAS*********************
				if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
				{
					if(!horaInicial.equals("")&&!horaFinal.equals(""))
						consulta += " and (" +
							"to_char(fecha_registro,'yyyy-mm-dd')||' '||hora_registro between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
							"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";	
					else
						consulta += " and (to_char(fecha_registro,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
					
				}
				
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setObject(1,codigo);
				
				rs = new ResultSetDecorator(pst.executeQuery());
				
				if(rs.next())
					if(rs.getInt("cuenta")>0)
						tiene = true;
			}
			
			return tiene;
					
		}
		catch(SQLException e)
		{
			logger.error("Error en tieneRegistroEnfermeria de SqlBaseResumenAtencionesDao: "+e);
			return false;
		}
	}
	
	/**
	 * Mï¿½todo implementado para listar las consultas PYP de la cuenta
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static HashMap listarConsultasPYP(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		try
		{
			String consulta = consultarConsultasPYPStr;
			
			//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
			if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
			{
				if(!horaInicial.equals("")&&!horaFinal.equals(""))
					consulta += " and (" +
						"to_char(s.fecha_solicitud ,'yyyy-mm-dd')||' '||s.hora_solicitud between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
						"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";	
				else
					consulta += " and (to_char(s.fecha_solicitud,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
				
			}
			//***************************************************************************
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,idCuenta);
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarConsultasPYP de SqlBaseResumenAtencionesDao: "+e);
			return null;
		}
	}
	
	/**
	 * Mï¿½todo que verifica si el ingreso tiene registro de accidente de transito
	 * @param con
	 * @param idIngreso
	 * @param estado
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public static boolean tieneRegistroAccidenteTransito(Connection con,String idIngreso,String estado,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		try
		{
			String consulta = tieneRegistroAccidenteTransitoStr + " ('"+estado+"') ";
			
			//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
			if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
			{
				if(!horaInicial.equals("")&&!horaFinal.equals(""))
					consulta += " and (" +
						"to_char(rat.fecha_grabacion,'yyyy-mm-dd')||' '||rat.hora_grabacion between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
						"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";
				else
					consulta += " and (to_char(rat.fecha_grabacion,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
				
			}
			//***************************************************************************
			
			
			boolean tiene = false;
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setObject(1,idIngreso);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				tiene = true;
			
			return tiene;
		}
		catch(SQLException e)
		{
			logger.error("Error en tieneRegistroAccidenteTransito de SQlBaseResumenAtencionesDao: "+e);
			return false;
		}
	}
	
	/**
	 * Mï¿½todo implementado para verificar si existen ordenes mï¿½dicas
	 * @param con
	 * @param idIngreso
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public static boolean existeOrdenesMedicas(Connection con,String idIngreso,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		try
		{
			boolean existe = false;
			String consulta = existeOrdenesMedicasStr;
			
			//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
			if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
			{
				if(!horaInicial.equals("")&&!horaFinal.equals(""))
					consulta += " and (" +
						"to_char(ehom.fecha_orden,'yyyy-mm-dd')||' '||ehom.hora_orden between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
						"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";
				else
					consulta += " and (to_char(ehom.fecha_orden,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
				
			}
			//***************************************************************************
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,idCuenta);
			pst.setString(2,idIngreso);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				if(rs.getInt("cuenta")>0)
					existe = true;
			
			return existe;
		}
		catch(SQLException e)
		{
			logger.error("Error en existeOrdenesMedicas de SqlBaseResumenAtencionesDao: "+e);
			return false;
		}
	}
	
	/**
	 * Mï¿½todo implementado para verificar si existe signos vitales
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public static boolean existeSignosVitales(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		try
		{
			int numSignos1 = 0;
			int numSignos2 = 0;
			boolean existe = false;
			String consulta = existeSignosVitales1Str;
			//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
			if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
			{
				if(!horaInicial.equals("")&&!horaFinal.equals(""))
					consulta += " and (" +
						"to_char(ehre.fecha_registro'yyyy-mm-dd')||' '||ehre.hora_registro between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
						"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";
				else
					consulta += " and (to_char(ehre.fecha_registro,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
				
			}
			//********************************************************
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,idCuenta);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				numSignos1 = rs.getInt("cuenta");
			
			//-------------------------------------------------------------
			consulta = existeSignosVitales2Str;
			//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
			if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
			{
				if(!horaInicial.equals("")&&!horaFinal.equals(""))
					consulta += " and (" +
						"to_char(ehre.fecha_registro'yyyy-mm-dd')||' '||ehre.hora_registro between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
						"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";
				else
					consulta += " and (to_char(ehre.fecha_registro,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
				
			}
			//********************************************************
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,idCuenta);
			rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				numSignos2 = rs.getInt("cuenta");
			
			if(numSignos1>0||numSignos2>0)
				existe = true;
			
			return existe;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en existeSignosVitales de SqlBaseResumenAtencionesDao: "+e);
			return false;
		}
	}
	
	/**
	 * Mï¿½todo implementado para verificar si existe soporte respiratorio
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public static boolean existeSoporteRespiratorio(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		try
		{
			boolean existe = false;
			String consulta = existeSoporteRespiratorioStr;
			//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
			if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
			{
				if(!horaInicial.equals("")&&!horaFinal.equals(""))
					consulta += " and (" +
						"to_char(ehre.fecha_registro'yyyy-mm-dd')||' '||ehre.hora_registro between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
						"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";
				else
					consulta += " and (to_char(ehre.fecha_registro,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
				
			}
			//********************************************************
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,idCuenta);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				if(rs.getInt("cuenta")>0)
					existe = true;
			
			return existe;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en existeSoporteRespiratorio de SQlBaseResumenAtencionesDao: "+e);
			return false;
		}
	}
	
	/**
	 * Mï¿½todo que verifica si hay cateter y sonda
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public static boolean existeCateterSonda(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		try
		{
			boolean existe = false;
			String consulta = existeCateterSondaStr;
			//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
			if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
			{
				if(!horaInicial.equals("")&&!horaFinal.equals(""))
					consulta += " and (" +
						"to_char(ehre.fecha_registro'yyyy-mm-dd')||' '||ehre.hora_registro between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
						"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";
				else
					consulta += " and (to_char(ehre.fecha_registro,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
				
			}
			//********************************************************
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,idCuenta);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				if(rs.getInt("cuenta")>0)
					existe = true;
			
			return existe;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en existeCateterSonda de SqlBaseResumenAtencionesDao: "+e);
			return false;
		}
	}
	
	/**
	 * Mï¿½todo que verifica si existen cuidados especiales
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public static boolean existeCuidadosEspeciales(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		try
		{
			boolean existe = false;
			int numCuidados1 = 0;
			int numCuidados2 = 0;
			int numCuidados3 = 0;
			int numCuidados4 = 0;
			//----------------------------------------------------------------------------------------------
			String consulta = existeCuidadesEspeciales1Str;
			//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
			if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
			{
				if(!horaInicial.equals("")&&!horaFinal.equals(""))
					consulta += " and (" +
						"to_char(ehre.fecha_registro'yyyy-mm-dd')||' '||ehre.hora_registro between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
						"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";
				else
					consulta += " and (to_char(ehre.fecha_registro,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
				
			}
			//********************************************************
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,idCuenta);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				numCuidados1 = rs.getInt("cuenta");
			//----------------------------------------------------------------------------------------
			consulta = existeCuidadesEspeciales2Str;
			//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
			if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
			{
				if(!horaInicial.equals("")&&!horaFinal.equals(""))
					consulta += " and (" +
						"to_char(eho.fecha_orden,'yyyy-mm-dd')||' '||eho.hora_orden between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
						"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";	
				else
					consulta += " and (to_char(eho.fecha_orden,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
				
			}
			//********************************************************
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,idCuenta);
			rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				numCuidados2 = rs.getInt("cuenta");
			//----------------------------------------------------------------------------------------
			consulta = existeCuidadesEspeciales3Str;
			//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
			if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
			{
				if(!horaInicial.equals("")&&!horaFinal.equals(""))
					consulta += " and (" +
						"to_char(eho.fecha_orden,'yyyy-mm-dd')||' '||eho.hora_orden between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
						"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";	
				else
					consulta += " and (to_char(eho.fecha_orden,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
				
			}
			//********************************************************
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,idCuenta);
			rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				numCuidados3 = rs.getInt("cuenta");
			//---------------------------------------------------------------------------------
			consulta = existeCuidadesEspeciales4Str;
			//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
			if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
			{
				if(!horaInicial.equals("")&&!horaFinal.equals(""))
					consulta += " and (" +
						"to_char(ehre.fecha_registro,'yyyy-mm-dd')||' '||ehre.hora_registro between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
						"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";
				else
					consulta += " and (to_char(ehre.fecha_registro,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
				
			}
			//********************************************************
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,idCuenta);
			rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				numCuidados4 = rs.getInt("cuenta");
			//--------------------------------------------------------------------
			
			if(numCuidados1>0||numCuidados2>0||numCuidados3>0||numCuidados4>0)
				existe = true;
			
			return existe;
			
			
		}
		catch(SQLException e)
		{
			logger.error("Error en existeCuidadosEspeciales de SqlBaseResumenAtencionesDao: "+e);
			return false;
		}
	}
	
	/**
	 * Mï¿½todo que verifica si hay anotaciones de enfermeria
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public static boolean existeAnotacionesEnfermeria(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		try
		{
			boolean existe = false;
			String consulta = existeAnotacionesEnfermeriaStr;
			//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
			if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
			{
				if(!horaInicial.equals("")&&!horaFinal.equals(""))
					consulta += " and (" +
							"to_char(ehre.fecha_registro,'yyyy-mm-dd')||' '||ehre.hora_registro between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
							"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";
				else
					consulta += " and (to_char(ehre.fecha_registro,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
				
			}
			//********************************************************
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,idCuenta);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				if(rs.getInt("cuenta")>0)
					existe = true;
			
			return existe;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en existeAnotacionesEnfermeria de SqlBaseResumenAtencionesDao: "+e);
			return false;
		}
	}
	
	/**
	 * Mï¿½todo implementado para verificar si existe la hoja neurologica
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public static boolean existeHojaNeurologica(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		try
		{
			boolean existe = false;
			int numHojaN1 = 0;
			int numHojaN2 = 0;
			int numHojaN3 = 0;
			int numHojaN4 = 0;
			int numHojaN5 = 0;
			String consulta = existeHojaNeurologica1Str;
			//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
			if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
			{
				if(!horaInicial.equals("")&&!horaFinal.equals(""))
					consulta += " and (" +
							"to_char(ehre.fecha_registro,'yyyy-mm-dd')||' '||ehre.hora_registro between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
							"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";
				else
					consulta += " and (to_char(ehre.fecha_registro,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
				
			}
			//********************************************************
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,idCuenta);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				numHojaN1 = rs.getInt("cuenta");
			//----------------------------------------------------------------------------------------
			consulta = existeHojaNeurologica2Str;
			//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
			if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
			{
				if(!horaInicial.equals("")&&!horaFinal.equals(""))
					consulta += " and (" +
							"to_char(ehre.fecha_registro,'yyyy-mm-dd')||' '||ehre.hora_registro between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
							"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";
				else
					consulta += " and (to_char(ehre.fecha_registro,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
				
			}
			//********************************************************
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,idCuenta);
			rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				numHojaN2 = rs.getInt("cuenta");
			//----------------------------------------------------------------------------------------
			consulta = existeHojaNeurologica3Str;
			//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
			if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
			{
				if(!horaInicial.equals("")&&!horaFinal.equals(""))
					consulta += " and (" +
							"to_char(ehre.fecha_registro,'yyyy-mm-dd')||' '||ehre.hora_registro between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
							"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";
				else
					consulta += " and (to_char(ehre.fecha_registro,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
				
			}
			//********************************************************
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,idCuenta);
			rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				numHojaN3 = rs.getInt("cuenta");
			//----------------------------------------------------------------------------------------
			consulta = existeHojaNeurologica4Str;
			//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
			if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
			{
				if(!horaInicial.equals("")&&!horaFinal.equals(""))
					consulta += " and (" +
							"to_char(ehre.fecha_registro,'yyyy-mm-dd')||' '||ehre.hora_registro between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
							"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";
				else
					consulta += " and (to_char(ehre.fecha_registro,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
				
			}
			//********************************************************
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,idCuenta);
			rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				numHojaN4 = rs.getInt("cuenta");
			//----------------------------------------------------------------------------------------
			consulta = existeHojaNeurologica5Str;
			//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
			if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
			{
				if(!horaInicial.equals("")&&!horaFinal.equals(""))
					consulta += " and (" +
							"to_char(ehre.fecha_registro,'yyyy-mm-dd')||' '||ehre.hora_registro between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
							"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";
				else
					consulta += " and (to_char(ehre.fecha_registro,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
				
			}
			//********************************************************
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,idCuenta);
			rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				numHojaN5 = rs.getInt("cuenta");
			//----------------------------------------------------------------------------------------
			if(numHojaN1>0||numHojaN2>0||numHojaN3>0||numHojaN4>0||numHojaN5>0)
				existe = true;
			
			return existe;
		}
		catch(SQLException e)
		{
			logger.error("Error en existeHojaNeurologica de SqlBaseResumenAtencionesDao: "+e);
			return false;
		}
	}
	
	/**
	 * Mï¿½todo que verifica si hay adminsitracion de medicamentos
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public static boolean existeAdminMedicamentos(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		try
		{
			boolean existe = false;
			String consulta = existeAdminMedicamentosStr;
			//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
			if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
			{
				if(!horaInicial.equals("")&&!horaFinal.equals(""))
					consulta += " and (" +
						"to_char(da.fecha,'yyyy-mm-dd')||' '||da.hora between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
							"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";
				else
					consulta += " and (to_char(da.fecha,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
				
			}
			//********************************************************
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,idCuenta);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				if(rs.getInt("cuenta")>0)
					existe = true;
			
			return existe;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en existeAdminMedicamentos de SqlBaseResumenAtencionesDao: "+e);
			return false;
		}
	}
	
	/**
	 * Mï¿½todo que verifica si hay consumo de insumos
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public static boolean existeConsumoInsumos(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		try
		{
			boolean existe = false;
			String consulta = existeConsumoInsumosStr;
			//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
			if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
			{
				if(!horaInicial.equals("")&&!horaFinal.equals(""))
					consulta += " and (" +
						"to_char(da.fecha,'yyyy-mm-dd')||' '||da.hora between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
						"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";
				else
					consulta += " and (to_char(da.fecha,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
				
			}
			//********************************************************
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,idCuenta);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				if(rs.getInt("cuenta")>0)
					existe = true;
			
			return existe;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en existeConsumoInsumos de SqlBaseResumenAtencinesDao: "+e);
			return false;
		}
	}
	
	/**
	 * Metodo que verifica si hay respuesta o interpertacion de interconsultas
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public static boolean existeRespIntInterconsulta(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		try
		{
			boolean existe = false;
			String consulta = existeRespIntInterconsultaStr;
			//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
			if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
			{
				if(!horaInicial.equals("")&&!horaFinal.equals(""))
					consulta += " and (" +
						"to_char(v.fecha_valoracion,'yyyy-mm-dd')||' '||v.hora_valoracion  between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
						"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";	
				else
					consulta += " and (to_char(v.fecha_valoracion,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
				
			}
			//********************************************************
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,idCuenta);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				if(rs.getInt("cuenta")>0)
					existe = true;
			
			return existe;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en existeRespIntInterconsulta de SqlBaseResumenAtencionesDao: "+e);
			return false;
		}
	}
	
	/**
	 * Mï¿½todo que verifica si hay respuesta de procedimientos
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public static boolean existeRespuestaProcedimiento(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		try
		{
			boolean existe = false;
			String consulta = existeRespuestaProcedimientosStr;
			//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
			if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
			{
				if(!horaInicial.equals("")&&!horaFinal.equals(""))
					consulta += " and (" +
						"to_char(rsp.fecha_ejecucion,'yyyy-mm-dd')||' '||rsp.hora_grabacion between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
						"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";
				else
					consulta += " and (to_char(rsp.fecha_ejecucion,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
				
			}
			//********************************************************
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,idCuenta);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				if(rs.getInt("cuenta")>0)
					existe = true;
			
			return existe;
		}
		catch(SQLException e)
		{
			logger.error("Error en existeRespuestaProcedimiento de SqlBaseResumenAtencionesDao: "+e);
			return false;
		}
	}
	
	/**
	 * Mï¿½todo que verifica si existe respuesta de cirugias
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public static boolean existeRespuestaCirugias(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		try
		{
			boolean existe = false;
			String consulta = existeRespuestaCirugiasStr;
			//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
			if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
			{
				if(!horaInicial.equals("")&&!horaFinal.equals(""))
					consulta += " AND "+ 
					"( "+
						"( "+
						"to_char(sc.fecha_inicial_cx,'yyyy-mm-dd')||' '||sc.hora_inicial_cx between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
						"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')"+
						") "+
						"OR "+
						"( "+
						"to_char(sc.fecha_final_cx,'yyyy-mm-dd')||' '||sc.hora_final_cx between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
						"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')"+
						") "+
                    ") ";
				else
					consulta += " AND "+ 
						"( "+
							"(to_char(sc.fecha_inicial_cx,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') "+
							" OR "+
							"(to_char(sc.fecha_final_cx,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' ) "+
						") "; 
						
				
			}
			//********************************************************
			logger.info("valor de la consulta >> "+consulta + " : IDCUENTA: " + idCuenta);
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setLong(1,Long.parseLong(idCuenta));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				if(rs.getInt("cuenta")>0)
					existe = true;
			
			return existe;
		}
		catch(SQLException e)
		{
			logger.error("Error en existeRespuestaCirugias de SqlBaseResumenAtencionesDao: "+e);
			return false;
		}
	}
	
	/**
	 * Mï¿½todo implementado para listar las citas de un paciente 
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public static HashMap listarCitas(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		try
		{
			String consulta = listarCitasStr;
			//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
			if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
			{
				if(!horaInicial.equals("")&&!horaFinal.equals(""))
					consulta += " and (" +
							"(to_char(a.fecha,'yyyy-mm-dd')||' '||a.hora_inicio between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
							"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"') OR " +
							"(to_char(a.fecha,'yyyy-mm-dd')||' '||a.hora_fin between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
							"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')" +
						") ";
				else
					consulta += " and (to_char(a.fecha,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
				
			}
			
			consulta += " ORDER BY  nombre_unidad_consulta,a.fecha, a.hora_inicio ";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setObject(1, idCuenta);
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,true);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en listarCitas: "+e);
			return null;
		}
	}
	
	/**
	 * Mï¿½todo implementado para listar las Solicitudes de citas de un paciente 
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public static HashMap listarSolicitudesCitas(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		try
		{
			String consulta = listarSolicitudesCitasStr;
			
			//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
			if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
			{
				if(!horaInicial.equals("")&&!horaFinal.equals(""))
					consulta += " and (" +
							"to_char(s.fecha_solicitud,'yyyy-mm-dd')||' '||s.hora_solicitud between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
							"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";
				else
					consulta += " and (to_char(s.fecha_solicitud,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
				
			}
			
			consulta += " ORDER BY s.fecha_solicitud, s.hora_solicitud ";
			logger.info("CONSULTA:::"+consulta.replace("?", idCuenta));
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setObject(1, idCuenta);
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,true);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en listarCitas: "+e);
			return null;
		}
	}
	
	/**
	 * Mï¿½todo que lista los servicios de la cita
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	public static HashMap listarServiciosCita(Connection con,String codigoCita)
	{
		HashMap resultados = new HashMap();
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(listarServiciosCitaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Integer.parseInt(codigoCita));
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,true);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en listarServiciosCita: "+e);
		}
		return resultados;
	}
	
	/**
	 * Mï¿½todo que consulta los archivos adjuntos de las respuestas de procedimientos
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoRespuesta
	 * @return
	 */
	public static HashMap consultarAdjuntosProcedimientos(Connection con,String numeroSolicitud,String codigoRespuesta)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarAdjuntosProcedimientosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setObject(1,numeroSolicitud);
			pst.setObject(2, codigoRespuesta);
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarAdjuntosProcedimientos: "+e);
			return null;
		}
	}
	
	/**
	 * Mï¿½todo que verifica si el paciente ha tenido registros de triage
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean existeTriage(Connection con,HashMap campos)
	{
		try
		{
			int cuenta = 0;
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(existeTriageStr));
			pst.setObject(1,campos.get("tipoIdentificacion"));
			pst.setObject(2, campos.get("numeroIdentificacion"));
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				cuenta = rs.getInt("cuenta");
			
			if(cuenta>0)
				return true;
			else
				return false;
		}
		catch(SQLException e)
		{
			logger.error("Error en existeTriage: "+e);
			return false;
		}
	}
	
	/**
	 * Mï¿½todo implementado para consultar los registros triage de un paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap cargarListadoTriage(Connection con,HashMap campos)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarListadoTriageStr));
			pst.setObject(1,campos.get("tipoIdentificacion"));
			pst.setObject(2, campos.get("numeroIdentificacion"));
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarListadoTriage: "+e);
			return null;
		}
	}

	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param estado
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public static boolean tieneRegistroEventoCatastrofico(Connection con, String idIngreso, String estado, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal)
	{
		try
		{
			String consulta = tieneRegistroEventoCatastroficoStr + " ('"+estado+"') ";
			
			//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
			if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
			{
				if(!horaInicial.equals("")&&!horaFinal.equals(""))
					consulta += " and (" +
						"to_char(rev.fecha_modifica ,'yyyy-mm-dd')||' '||rev.hora_modifica between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
						"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";
				else
					consulta += " and (to_char(rev.fecha_modifica,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
				
			}
			//***************************************************************************
			
			
			boolean tiene = false;
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setObject(1,idIngreso);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				tiene = true;
			
			return tiene;
		}
		catch(SQLException e)
		{
			logger.error("Error en tieneRegistroEventoCatastrofico de SQlBaseResumenAtencionesDao: "+e);
			return false;
		}

	}
	
	
	/**
	 * Consulta el historial Consentimiento Informado 
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static HashMap consultaHistorialConsentimientoInf(Connection con, HashMap parametros)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaHistorialConsentimientoInf,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setObject(1,parametros.get("ingreso"));			
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarHistorialConsentimientoInformado: "+e);
			return null;
		}				
	}
	
	//******************************************************************************************************************************
	//Anexo 550. Cargos Directos****************************************************************************************************
	//******************************************************************************************************************************
	
	/**
	 * Verifica si existe Solicitudes de tipo Cargo Directos
	 * @param Connection con
	 * @param int idCuenta
	 * @param int codigoTipoSolicitudBuscada
	 * @param String fechaInicial
	 * @param String fechaFinal
	 * @param String horaInicial
	 * @param String horaFinal
	 */
	public static boolean tieneSolicitudCargoDirectoDe (
			Connection con, 
			int idCuenta,
			int codigoTipoSolicitudBuscada,
			String fechaInicial,
			String fechaFinal,
			String horaInicial,
			String horaFinal) 
	{
		try
		{
			String cadena = "SELECT count(1) as numResultados from solicitudes s ";
			
			//Evalua si el tipo de solicitud es de Cirugia
			if(codigoTipoSolicitudBuscada == ConstantesBD.codigoTipoSolicitudCirugia)
			{
				cadena+=" INNER JOIN solicitudes_cirugia sc ON(sc.numero_solicitud = s.numero_solicitud " +
						" AND (sc.ind_qx = '"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugiaCargoDirecto+"' OR " +
					    "      sc.ind_qx = '"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugiaCargoDirecto+"')) ";
			}
					
			cadena+=" WHERE s.tipo = "+codigoTipoSolicitudBuscada+" AND s.cuenta = ? ";			
							
			//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
			if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
			{
				if(!horaInicial.equals("")&&!horaFinal.equals(""))
					cadena += " AND (" +
						"to_char(s.fecha_solicitud,'yyyy-mm-dd')||' '||s.hora_solicitud between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
						"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";
				else
					cadena += " and (to_char(s.fecha_solicitud,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
			}
			
			logger.info("\n\n\n\n\n\n valor de la cadena >> "+cadena+" >> "+idCuenta);
			
			return  ejecucionGenericaBoolean(con, idCuenta, cadena); 
		}
		catch(SQLException e)
		{
			logger.error("Error en tieneSolicitudCargoDirectoDe de SQlBaseResumenAtencionesDao: "+e);
			return false;
		}
	}	
	
	//******************************************************************************************************************************
	
	/**
	 * Verifica si existe Solicitudes de tipo Cargo Directos
	 * @param Connection con
	 * @param parametros
	 */
	public static HashMap consultarListadoSolCargoDirectoDe (
			Connection con, 
			HashMap parametros) 
	{
		try
		{
			String cadena = strConsultarListadoSolCargosDirectos;						
							
			//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
			if(!parametros.get("fechaInicial").toString().equals("")&&!parametros.get("fechaFinal").toString().equals(""))
			{
				if(!parametros.get("horaInicial").toString().equals("")&&!parametros.get("horaFinal").toString().equals(""))
					cadena += " AND (" +
							"to_char(s.fecha_solicitud,'yyyy-mm-dd')||' '||s.hora_solicitud between '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicial").toString())+" "+parametros.get("horaInicial").toString()+"' and " +
							"'"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinal").toString())+" "+parametros.get("horaFinal").toString()+"')";
				else
					cadena += " and (to_char(s.fecha_solicitud,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicial").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinal").toString())+"') ";				
			}
			//****************************************************************************
			
			cadena+=" ORDER BY s.tipo ASC ";		
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1,parametros.get("idCuenta"));
			parametros = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			
			logger.info("valor de la cadena >> "+cadena);
			 
		}
		catch(SQLException e)
		{
			logger.error("Error en tieneSolicitudCargoDirectoDe de SQlBaseResumenAtencionesDao: "+e);
			return parametros;
		}
		
		return parametros;
	}		
	
	
	//******************************************************************************************************************************
	
	/**
	 * Consulta la informacion de los articulos de una solicitud de cargo directo 
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static HashMap consultarDetalleArticulosDirectos(Connection con, HashMap parametros)
	{
		HashMap mapa = new HashMap();
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultarDetalleArticuloCDirecto,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1,parametros.get("numeroSolicitud").toString());
			
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		
		return mapa;
	}
	
	//******************************************************************************************************************************
	
	/**
	 * Consulta la informaciï¿½n de los servicios de la solicitud de Cargo Directo 
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static HashMap consultarDetalleServicioCDirectos(Connection con, HashMap parametros)
	{		
		HashMap mapa = new HashMap();
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultaDetalleServiciosCDirecto,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1,parametros.get("numeroSolicitud").toString());			
		
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		
		return mapa;
	}
	
	//******************************************************************************************************************************
	
	/**
	 * Consulta la informaciï¿½n de los diagnosticos del servicio de Cargo Directo
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static HashMap consultarDiagnosticosServiciosCDirecto(Connection con, HashMap parametros)
	{		
		HashMap mapa = new HashMap();
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultaDiagnosticoServiciosCD,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1,parametros.get("codigoCargoDirectoHc").toString());		
			
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		
		return mapa;
	}
	
	//******************************************************************************************************************************
	
	/**
	 * Consulta la informaciï¿½n de los diagnosticos del servicio de Cargo Directo
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static HashMap consultarServiciosCirugiaCDirecto(Connection con, HashMap parametros)
	{		
		HashMap mapa = new HashMap();
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultaServiciosCirugiaCD,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1,parametros.get("numeroSolicitud").toString());
			
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		
		return mapa;
	}
	
	//******************************************************************************************************************************
	
	
	public static boolean tieneRegistroEventoAdverso(Connection con, String idIngreso){
		boolean tiene=false;
		String consulta = "SELECT codigo FROM registro_eventos_adversos WHERE ingreso=?";
		try {
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setObject(1,idIngreso);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				tiene=true;
		}
		catch(SQLException e)
		{
			logger.error("Error en tieneRegistroEventoAdverso de SQlBaseResumenAtencionesDao: "+e);
		}
		return tiene;
	}
	
	/**
	 * Mï¿½todo que verifica si existen escalas x ingresos
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean existenEscalasXIngreso(Connection con,HashMap campos)
	{
		boolean existe = false;
		try
		{
			//***************SE RECIBEN CAMPOS*****************************************
			String idIngreso = campos.get("idIngreso").toString();
			String fechaInicial = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString());
			String horaInicial = campos.get("horaInicial").toString();
			String fechaFinal = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString());
			String horaFinal = campos.get("horaFinal").toString();
			//*************************************************************************
			
			
			String consulta = "SELECT "+ 
				"count(1) as cuenta "+ 
				"FROM escalas_ingresos ei "+ 
				"WHERE "+ 
				"ei.ingreso = "+idIngreso;
			
			if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
			{
				if(!horaInicial.equals("")&&!horaFinal.equals(""))
					consulta += " AND (to_char( ei.fecha,'yyyy-mm-dd')||' '||ei.hora between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
								"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";
				else
					consulta += " and (to_char(ei.fecha,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
			}
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
				if(rs.getInt("cuenta")>0)
					existe = true;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en existeEscalasXIngreso: "+e);
		}
		return existe;
	}
	
	/**
	 * Mï¿½todo implementado para cargar las cuentas del ingreso del paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<CuentasPaciente> getCuentasPaciente(Connection con,HashMap campos)
	{
		ArrayList<CuentasPaciente> cuentas = new ArrayList<CuentasPaciente>();
		try
		{
			//**************CAMPOS***************************************************************
			int codigoIngreso = Integer.parseInt(campos.get("codigoIngreso").toString());
			//***********************************************************************************
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(getCuentasPacienteStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoIngreso);
			logger.info("consulta cuenta x ingreso=> "+getCuentasPacienteStr.replace("?", codigoIngreso+""));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				
				CuentasPaciente cuenta = new CuentasPaciente();				
				cuenta.setCodigoViaIngreso(rs.getString("viaIngreso"));
				cuenta.setDescripcionViaIngreso(rs.getString("descripcionViaIngreso"));
				cuenta.setCodigoTipoPaciente(rs.getString("tipoPaciente"));
				cuenta.setDescripcionTipoPaciente(rs.getString("descripcionTipoPaciente"));
				cuenta.setCodigoCuenta(rs.getString("cuenta"));
				cuenta.setEstadoCuenta(rs.getString("estadoCuenta"));
				cuenta.setDescripcionEstadoCuenta(rs.getString("descripcionEstadoCuenta"));
				cuenta.setCodigoIngreso(rs.getString("idIngreso"));
				
				cuentas.add(cuenta);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en getCuentasPaciente: "+e);
		}
		return cuentas;
	}
	
	/**
	 * Mï¿½todo implementado para saber si existen valoraciones de cuidados especiales
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean existenValoracionesCuidadoEspecial(Connection con,HashMap campos)
	{
		boolean existe = false;
		try
		{
			//***************SE RECIBEN CAMPOS*****************************************
			String idIngreso = campos.get("idIngreso").toString();
			String fechaInicial = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString());
			String horaInicial = campos.get("horaInicial").toString();
			String fechaFinal = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString());
			String horaFinal = campos.get("horaFinal").toString();
			//*************************************************************************
			
			String consulta = "SELECT count(1) as cuenta " +
				"from ingresos_cuidados_especiales ice " +
				"INNER JOIN valoraciones v ON(v.numero_solicitud=ice.valoracion) " +
				"WHERE ice.ingreso = "+idIngreso;
			
			if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
			{
				if(!horaInicial.equals("")&&!horaFinal.equals(""))
					consulta += " AND (to_char(v.fecha_valoracion,'yyyy-mm-dd')||' '||v.hora_valoracion between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
								"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";
				else
					consulta += " and (to_char(v.fecha_valoracion,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
			}
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
				if(rs.getInt("cuenta")>0)
					existe = true;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en existenValoracionesCuidadoEspecial: "+e);
			existe = false;
		}
		return existe;
	}
	
	/**
	 * Mï¿½todo implementado para cargar el listado de las valoraciones de cuidado especial
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> cargarValoracionesCuidadoEspecial(Connection con,HashMap campos)
	{
		HashMap resultados = new HashMap();
		try
		{
			//***************SE RECIBEN CAMPOS*****************************************
			String idIngreso = campos.get("idIngreso").toString();
			String fechaInicial = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString());
			String horaInicial = campos.get("horaInicial").toString();
			String fechaFinal = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString());
			String horaFinal = campos.get("horaFinal").toString();
			//*************************************************************************
			
			String consulta = "SELECT " +
				"v.numero_solicitud as numero_solicitud, " +
				"getconsecutivosolicitud(v.numero_solicitud) as orden, " +
				"to_char(v.fecha_valoracion,'"+ConstantesBD.formatoFechaAp+"') AS fecha_valoracion, " +
				"substr(v.hora_valoracion,0,6) AS hora_valoracion, " +
				"coalesce(getnombrepersona(v.codigo_medico),'') AS nombre_profesional " +
				"from ingresos_cuidados_especiales ice " +
				"INNER JOIN valoraciones v ON(v.numero_solicitud=ice.valoracion) " +
				"WHERE ice.ingreso = "+idIngreso;
		
			if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
			{
				if(!horaInicial.equals("")&&!horaFinal.equals(""))
					consulta += " AND (to_char(v.fecha_valoracion,'yyyy-mm-dd')||' '||v.hora_valoracion between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
							"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";
				else
					consulta += " and (to_char(v.fecha_valoracion,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
			}
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)),true,true);
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarValoracionesCuidadoEspecial: "+e);
			resultados.put("numRegistros", "0");
		}
		return resultados;
	}

	/**
	 * Método implementado para saber si se han registrado ordenes ambulatorias
	 * @param con
	 * @param idIngreso
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public static boolean tieneOrdenesAmbulatorias(Connection con,
			String idIngreso, String idCuenta, String fechaInicial,
			String fechaFinal, String horaInicial, String horaFinal) {
		try
		{
			boolean existe = false;
			String consulta = existeOrdenesAmbulatoriasStr;
			
			//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
			if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
			{
				if(!horaInicial.equals("")&&!horaFinal.equals(""))
					consulta += " and (" +
						"to_char(fecha,'yyyy-mm-dd')||' '||hora between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
						"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";	
				else
					consulta += " and (to_char(fecha,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
				
			}
			//***************************************************************************
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,idCuenta);
			pst.setString(2,idIngreso);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				if(rs.getInt("orden")>0)
					existe = true;
			
			return existe;
		}
		catch(SQLException e)
		{
			logger.error("Error en tieneOrdenesAmbulatorias de tieneOrdenesAmbulatorias: "+e);
			return false;
		}
	}
	
	

	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param idIngreso2
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public static boolean existeValoracionEnfermeria(Connection con,String idIngreso, String idIngreso2, String fechaInicial,String fechaFinal, String horaInicial, String horaFinal) 
	{
		try
		{
			boolean existe = false;
			String consulta = existeValoracionEnfermeriaStr;
			
			//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
			if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
			{
				if(!horaInicial.equals("")&&!horaFinal.equals(""))
					consulta += " and (" +
						"to_char(eh.fecha_registro,'yyyy-mm-dd')||' '||eh.hora_registro between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
						"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";
				else
					consulta += " and (to_char(eh.fecha_registro,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
				
			}
			//***************************************************************************
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,idIngreso);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				if(rs.getInt("valoracion")>0)
					existe = true;
			rs.close();
			pst.close();
			return existe;
		}
		catch(SQLException e)
		{
			logger.error("Error en existeValoracionEnfermeria de existeValoracionEnfermeria: "+e);
			return false;
		}
	}

	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param idIngreso2
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public static boolean existeResultadosLaboratorios(Connection con,String idIngreso, String idIngreso2, String fechaInicial,String fechaFinal, String horaInicial, String horaFinal) 
	{
		try
		{
			boolean existe = false;
			String consulta = existeResultadosLaboratorioEnfermeriaStr;
			
			//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
			if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
			{
				if(!horaInicial.equals("")&&!horaFinal.equals(""))
					consulta += " and (" +
						"to_char(ere.fecha_registro,'yyyy-mm-dd')||' '||ere.hora_registro between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
						"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";
				else
					consulta += " and (to_char(ere.fecha_registro,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
				
			}
			//***************************************************************************
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,idIngreso);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				if(rs.getInt("resultados")>0)
					existe = true;
			rs.close();
			if(!existe)
			{
				consulta = existeResultadosLaboratorioOrdenMedicaStr;
				
				//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
				if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
				{
					if(!horaInicial.equals("")&&!horaFinal.equals(""))
						consulta += " and (" +
							"to_char(eh.fecha_orden,'yyyy-mm-dd')||' '||eh.hora_orden between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
							"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')";
					else
						consulta += " and (to_char(eh.fecha_orden,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
					
				}
				//***************************************************************************
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setString(1,idIngreso);
				
				rs = new ResultSetDecorator(pst.executeQuery());
				
				if(rs.next())
					if(rs.getInt("resultados")>0)
						existe = true;
			}
			rs.close();
			pst.close();
			return existe;
		}
		catch(SQLException e)
		{
			logger.error("Error en existeResultadosLaboratorios de existeResultadosLaboratorios: "+e);
			return false;
		}
	}
	
	
	
	/**
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return si existen notas generales
	 */
	public static boolean existeNotasGenerales(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal,Integer idPaciente)
	{
		try
		{
			Boolean flag=true;
			String consultaExisteAsocio="select * from cuentas cu  " +
			" where cu.codigo_paciente=?  " +
			" and cu.estado_cuenta=3";


			PreparedStatement pst3 = con.prepareStatement(consultaExisteAsocio);
			pst3.setInt(1, idPaciente);
			ResultSet rs3 = pst3.executeQuery();
			if(rs3.next()){
				flag=false;
			}


			boolean existe = false;
			if(flag){
				String consulta = "SELECT s.numero_solicitud as numero_solicitud " +
				" FROM solicitudes s " +
				" INNER JOIN solicitudes_cirugia sc " +
				" ON(sc.numero_solicitud=s.numero_solicitud " +
				" AND sc.ind_qx        IN('CX','NCTO') ) " +
				" LEFT OUTER JOIN hoja_quirurgica hq " +
				" ON(hq.numero_solicitud=sc.numero_solicitud) " +
				" LEFT OUTER JOIN hoja_anestesia ha " +
				" ON(ha.numero_solicitud=sc.numero_solicitud) " +
				" WHERE s.cuenta        = ? ";


				String consultaNotas="SELECT  count(1) existen_notas " +
				" FROM notas_enfermeria ne " +
				" WHERE ne.numero_solicitud=? ";

				if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
				{
					if(!horaInicial.equals("")&&!horaFinal.equals(""))
						consulta += " AND "+ 
						"( "+
						"( "+
									"to_char(sc.fecha_inicial_cx,'yyyy-mm-dd')||' '||sc.hora_inicial_cx between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
									"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')"+
						") "+
						"OR "+
						"( "+
									"to_char(sc.fecha_final_cx,'yyyy-mm-dd')||' '||sc.hora_final_cx between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
									"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')"+
						") "+
						") ";
					else
						consulta += " AND "+ 
						"( "+
										"(to_char(sc.fecha_inicial_cx,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') "+
						" OR "+
										"(to_char(sc.fecha_final_cx,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' ) "+
						") "; 


				}


				PreparedStatement pst = con.prepareStatement(consulta);
				pst.setLong(1,Long.parseLong(idCuenta));
				ResultSet rs = pst.executeQuery();
				while(rs.next()){
					Integer numeroSolicitud=rs.getInt("numero_solicitud");
					PreparedStatement pst2 = con.prepareStatement(consultaNotas);
					pst2.setInt(1, numeroSolicitud);
					ResultSet rs2 = pst2.executeQuery();
					if(rs2.next()){
						Integer cantidad=rs2.getInt("existen_notas");
						if(cantidad>0){
							return true;
						}
					}

				}
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en existeNotasGenerales : "+e);
			return false;
		}
		return false;

	}
	
	
	/**
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return si existen notas generales
	 */
	public static boolean existeNotasGeneralesAsocio(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal,Integer idPaciente)
	{
		try
		{
			Boolean flag=true;
			Integer cuentaActiva=0;
			
			
			String consultaExisteAsocio="select * from cuentas cu  " +
			" where cu.codigo_paciente=?  " +
			" and cu.estado_cuenta=3";


			PreparedStatement pst3 = con.prepareStatement(consultaExisteAsocio);
			pst3.setInt(1, idPaciente);
			ResultSet rs3 = pst3.executeQuery();
			if(rs3.next()){
				flag=false;
			}


			boolean existe = false;
			if(!flag){
				
				
				
				
				String consultaCuentaActiva="select cu.id id from cuentas cu " +
						" where cu.codigo_paciente=?  " +
						" and cu.estado_cuenta=0";


				PreparedStatement pst4 = con.prepareStatement(consultaCuentaActiva);
				pst4.setInt(1, idPaciente);
				ResultSet rs4 = pst4.executeQuery();
				if(rs4.next()){
					cuentaActiva=rs4.getInt("id");
				}
				
				
				
				
				
				
				String consulta = "SELECT s.numero_solicitud as numero_solicitud " +
				" FROM solicitudes s " +
				" INNER JOIN solicitudes_cirugia sc " +
				" ON(sc.numero_solicitud=s.numero_solicitud " +
				" AND sc.ind_qx        IN('CX','NCTO') ) " +
				" LEFT OUTER JOIN hoja_quirurgica hq " +
				" ON(hq.numero_solicitud=sc.numero_solicitud) " +
				" LEFT OUTER JOIN hoja_anestesia ha " +
				" ON(ha.numero_solicitud=sc.numero_solicitud) " +
				" WHERE s.cuenta        = ? ";


				String consultaNotas="SELECT  count(1) existen_notas " +
				" FROM notas_enfermeria ne " +
				" WHERE ne.numero_solicitud=? ";

				if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
				{
					if(!horaInicial.equals("")&&!horaFinal.equals(""))
						consulta += " AND "+ 
						"( "+
						"( "+
									"to_char(sc.fecha_inicial_cx,'yyyy-mm-dd')||' '||sc.hora_inicial_cx between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
									"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')"+
						") "+
						"OR "+
						"( "+
									"to_char(sc.fecha_final_cx,'yyyy-mm-dd')||' '||sc.hora_final_cx between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
									"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')"+
						") "+
						") ";
					else
						consulta += " AND "+ 
						"( "+
									"(to_char(sc.fecha_inicial_cx,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') "+
						" OR "+
									"(to_char(sc.fecha_final_cx,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' ) "+
						") "; 


				}


				PreparedStatement pst = con.prepareStatement(consulta);
				pst.setLong(1,Long.parseLong(String.valueOf(cuentaActiva)));
				ResultSet rs = pst.executeQuery();
				while(rs.next()){
					Integer numeroSolicitud=rs.getInt("numero_solicitud");
					PreparedStatement pst2 = con.prepareStatement(consultaNotas);
					pst2.setInt(1, numeroSolicitud);
					ResultSet rs2 = pst2.executeQuery();
					if(rs2.next()){
						Integer cantidad=rs2.getInt("existen_notas");
						if(cantidad>0){
							return true;
						}
					}

				}
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en existeNotasGenerales : "+e);
			return false;
		}
		return false;

	}
	
	/**
	 * @param con
	 * @param idCuenta
	 * @return si existen notas de recuperacion
	 * @throws Exception 
	 */
	public static Boolean existenNotasRecuperacion(Connection con,String idCuenta,Integer idPaciente) throws Exception{




		Boolean flag=true;
		Integer cuentaActiva=0;
		Boolean existenNotas=false;

		String consultaExisteAsocio="select * from cuentas cu  " +
		" where cu.codigo_paciente=?  " +
		" and cu.estado_cuenta=3";


		PreparedStatement pst3 = con.prepareStatement(consultaExisteAsocio);
		pst3.setInt(1, idPaciente);
		ResultSet rs3 = pst3.executeQuery();
		if(rs3.next()){
			flag=false;
		}


		if(flag){





			//EXITEN NOTAS
			String consulta=" SELECT (TO_CHAR(dnr.fecha_recuperacion,'DD/MM/YYYY') "+
			" ||' - ' "+
			" ||dnr.hora_recuperacion) AS fechaHoraRecuperacion "+
			" FROM det_notas_recuperacion dnr join solicitudes s "+
			" on ( dnr.numero_solicitud=s.numero_solicitud) "+
			" WHERE  s.cuenta=?   "+
			" GROUP BY dnr.fecha_grabacion, "+
			" dnr.hora_grabacion, "+
			" dnr.fecha_recuperacion, "+
			" dnr.hora_recuperacion "+
			" ORDER BY dnr.fecha_recuperacion DESC, "+
			" dnr.hora_recuperacion DESC ";


			// OBSERVACIONES
			String consulta2= " SELECT nrg.observaciones AS observacionesGenerales "+
			" FROM notas_recuperacion_general nrg join solicitudes s "+
			" on ( nrg.numero_solicitud=s.numero_solicitud) "+
			" WHERE  s.cuenta=? ";

			// MEDICAMENTOS
			String consulta3 = " SELECT nrg.medicamentos AS medicamentosGenerales "+
			" FROM notas_recuperacion_general nrg join solicitudes s "+
			" on ( nrg.numero_solicitud=s.numero_solicitud) "+
			"  WHERE  s.cuenta=?  ";


			try{

				//CREACION DE PREPARED STATEMENT
				PreparedStatement pst = con.prepareStatement(consulta);

				// VALIDACION Y SETTEO DE PARAMETRO
				if(!UtilidadTexto.isEmpty(idCuenta)){
					pst.setInt(1, Integer.valueOf(idCuenta));
				}else{
					pst.setInt(1,ConstantesBD.codigoNuncaValido);
				}

				//EJECUCION DE QUERY
				ResultSet rs = pst.executeQuery();

				//SI TRAE DATOS RETORN TRUE
				if(rs.next()){
					existenNotas= true;
				}


				pst = con.prepareStatement(consulta2);

				// VALIDACION Y SETTEO DE PARAMETRO
				if(!UtilidadTexto.isEmpty(idCuenta)){
					pst.setInt(1, Integer.valueOf(idCuenta));
				}else{
					pst.setInt(1,ConstantesBD.codigoNuncaValido);
				}

				//EJECUCION DE QUERY
				rs = pst.executeQuery();

				//SI TRAE DATOS RETORN TRUE
				if(rs.next()){
					existenNotas= true;
				}


				pst = con.prepareStatement(consulta3);

				// VALIDACION Y SETTEO DE PARAMETRO
				if(!UtilidadTexto.isEmpty(idCuenta)){
					pst.setInt(1, Integer.valueOf(idCuenta));
				}else{
					pst.setInt(1,ConstantesBD.codigoNuncaValido);
				}

				//EJECUCION DE QUERY
				rs = pst.executeQuery();

				//SI TRAE DATOS RETORN TRUE
				if(rs.next()){
					existenNotas= true;
				}



			}//CONTROL DE ERRORES
			catch(SQLException e)
			{
				logger.error("Error en existeNotas de recuperacion : "+e);
				throw new Exception("error consultando si existen notas de recuperacion "+e.getMessage());
			}
		}

		//SI NO HAY DATOS RETORNA FALSE
		return existenNotas;
	}
	
	public static Boolean existenNotasRecuperacionAsocio(Connection con,String idCuenta,Integer idPaciente) throws Exception{




		Boolean flag=true;
		Integer cuentaActiva=0;
		Boolean existenNotas=false;

		String consultaExisteAsocio="select * from cuentas cu  " +
		" where cu.codigo_paciente=?  " +
		" and cu.estado_cuenta=3";


		PreparedStatement pst3 = con.prepareStatement(consultaExisteAsocio);
		pst3.setInt(1, idPaciente);
		ResultSet rs3 = pst3.executeQuery();
		if(rs3.next()){
			flag=false;
		}


		if(!flag){


			String consultaCuentaActiva="select cu.id id from cuentas cu " +
			" where cu.codigo_paciente=?  " +
			" and cu.estado_cuenta=0";


			PreparedStatement pst4 = con.prepareStatement(consultaCuentaActiva);
			pst4.setInt(1, idPaciente);
			ResultSet rs4 = pst4.executeQuery();
			if(rs4.next()){
				cuentaActiva=rs4.getInt("id");
			}



			//EXITEN NOTAS
			String consulta=" SELECT (TO_CHAR(dnr.fecha_recuperacion,'DD/MM/YYYY') "+
			" ||' - ' "+
			" ||dnr.hora_recuperacion) AS fechaHoraRecuperacion "+
			" FROM det_notas_recuperacion dnr join solicitudes s "+
			" on ( dnr.numero_solicitud=s.numero_solicitud) "+
			" WHERE  s.cuenta=?   "+
			" GROUP BY dnr.fecha_grabacion, "+
			" dnr.hora_grabacion, "+
			" dnr.fecha_recuperacion, "+
			" dnr.hora_recuperacion "+
			" ORDER BY dnr.fecha_recuperacion DESC, "+
			" dnr.hora_recuperacion DESC ";


			// OBSERVACIONES
			String consulta2= " SELECT nrg.observaciones AS observacionesGenerales "+
			" FROM notas_recuperacion_general nrg join solicitudes s "+
			" on ( nrg.numero_solicitud=s.numero_solicitud) "+
			" WHERE  s.cuenta=? ";

			// MEDICAMENTOS
			String consulta3 = " SELECT nrg.medicamentos AS medicamentosGenerales "+
			" FROM notas_recuperacion_general nrg join solicitudes s "+
			" on ( nrg.numero_solicitud=s.numero_solicitud) "+
			"  WHERE  s.cuenta=?  ";


			try{

				//CREACION DE PREPARED STATEMENT
				PreparedStatement pst = con.prepareStatement(consulta);

				// VALIDACION Y SETTEO DE PARAMETRO
				if(!UtilidadTexto.isEmpty(cuentaActiva)){
					pst.setInt(1, Integer.valueOf(cuentaActiva));
				}else{
					pst.setInt(1,ConstantesBD.codigoNuncaValido);
				}

				//EJECUCION DE QUERY
				ResultSet rs = pst.executeQuery();

				//SI TRAE DATOS RETORN TRUE
				if(rs.next()){
					existenNotas= true;
				}


				pst = con.prepareStatement(consulta2);

				// VALIDACION Y SETTEO DE PARAMETRO
				if(!UtilidadTexto.isEmpty(cuentaActiva)){
					pst.setInt(1, Integer.valueOf(cuentaActiva));
				}else{
					pst.setInt(1,ConstantesBD.codigoNuncaValido);
				}

				//EJECUCION DE QUERY
				rs = pst.executeQuery();

				//SI TRAE DATOS RETORN TRUE
				if(rs.next()){
					existenNotas= true;
				}


				pst = con.prepareStatement(consulta3);

				// VALIDACION Y SETTEO DE PARAMETRO
				if(!UtilidadTexto.isEmpty(cuentaActiva)){
					pst.setInt(1, Integer.valueOf(cuentaActiva));
				}else{
					pst.setInt(1,ConstantesBD.codigoNuncaValido);
				}

				//EJECUCION DE QUERY
				rs = pst.executeQuery();

				//SI TRAE DATOS RETORN TRUE
				if(rs.next()){
					existenNotas= true;
				}



			}//CONTROL DE ERRORES
			catch(SQLException e)
			{
				logger.error("Error en existeNotas de recuperacion : "+e);
				throw new Exception("error consultando si existen notas de recuperacion "+e.getMessage());
			}
		}

		//SI NO HAY DATOS RETORNA FALSE
		return existenNotas;
	}
	
	
	/**
	 * Mï¿½todo que verifica si existe respuesta de cirugias
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public static boolean existeRespuestaCirugiasSolicitudes(Connection con,String idCuenta,String fechaInicial,String fechaFinal,String horaInicial,String horaFinal)
	{
		try
		{
			boolean existe = false;
			String consulta = existeRespuestaCirugiasStr;
			//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
			if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
			{
				if(!horaInicial.equals("")&&!horaFinal.equals(""))
					consulta += " AND "+ 
					"( "+
						"( "+
						"to_char(sc.fecha_inicial_cx,'yyyy-mm-dd')||' '||sc.hora_inicial_cx between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
						"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')"+
						") "+
						"OR "+
						"( "+
						"to_char(sc.fecha_final_cx,'yyyy-mm-dd')||' '||sc.hora_final_cx between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"' and " +
						"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"')"+
						") "+
                    ") ";
				else
					consulta += " AND "+ 
						"( "+
							"(to_char(sc.fecha_inicial_cx,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') "+
							" OR "+
							"(to_char(sc.fecha_final_cx,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' ) "+
						") "; 
						
				
			}
			//********************************************************
			logger.info("valor de la consulta >> "+consulta + " : IDCUENTA: " + idCuenta);
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setLong(1,Long.parseLong(idCuenta));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				if(rs.getInt("cuenta")>0)
					existe = true;
			
			return existe;
		}
		catch(SQLException e)
		{
			logger.error("Error en existeRespuestaCirugias de SqlBaseResumenAtencionesDao: "+e);
			return false;
		}
	}
	
	

	
	public static Collection ingresosFiltroBusqueda(Connection con, int idPaciente,String ano,String fechaIngresoInicial,
			String fechaIngresoFinal, String centroAtencion,String viaIngreso,String especialidad) throws SQLException{
		
		String consulta=" SELECT cue.id AS cuenta, "+
			" i.id AS idIngreso, i.FECHA_EGRESO as fechaEgresoIng,  i.HORA_EGRESO as horaEgresoIng, " +		
			" CASE  "+
			"   WHEN i.preingreso IS NOT NULL "+
			"  THEN i.preingreso "+
			"  ELSE '' END AS indpre, "+
			" CASE "+
			" WHEN i.reingreso IS NOT NULL "+
			" THEN i.reingreso "+
			"  ELSE 0  END AS indrei, "+
			" ec.nombre AS estadoCuenta, "+
			" getEstadoAsocio(cue.id,cue.tipo_paciente) AS codigoViaIngreso, "+
			" getNombreViaIngresoTipoPac(cue.id) AS viaIngreso, "+
			" cue.fecha_apertura AS fechaCuenta, "+
			" getconsecutivoingreso(cue.id_ingreso) AS consecutivoIngreso, "+
			" cue.hora_apertura AS horaCuenta, "+
			" getFechaIngAsocio(cue.id,getEstadoAsocio(cue.id,cue.tipo_paciente)) AS fechaAdmisionHospitalaria, "+
			" getHoraIngAsocio(cue.id,getEstadoAsocio(cue.id,cue.tipo_paciente))  AS horaAdmisionHospitalaria, "+
			" adu.fecha_admision AS fechaAdmisionUrgencias, "+
			"  adu.hora_admision AS horaAdmisionUrgencias, "+
			" eg.fecha_egreso AS fechaEgreso, "+
			" eg.hora_egreso AS horaEgreso, "+
			" CASE "+
			" WHEN cue.via_ingreso IN (1,3) "+
			" THEN eg.diagnostico_principal "+
			"  || ' - ' "+
			" ||getnombrediagnostico(eg.diagnostico_principal, eg.DIAGNOSTICO_PRINCIPAL_CIE) "+
			" ELSE getnomdiagconsultaexterna(cue.id) END AS diagnostico, "+
			" getresumenespecialidad(cue.via_ingreso,cue.id) AS especialidad, "+
			" getnomcentroatencion(ccos.centro_atencion) AS centroatencion, "+
			" CASE "+
			"  WHEN cue.id_ingreso IS NULL "+
			" THEN '' "+
			" ELSE COALESCE(getDescripEntidadSubXingreso(cue.id_ingreso),'') "+
			" END AS descripcionentidadsub, "+
			" COALESCE(gettransplante(cue.id_ingreso),'') AS transplante, "+
			" i.fecha_ingreso AS fecha_ingreso, "+
			" i.hora_ingreso AS hora_ingreso, "+
			" CASE "+
			" WHEN i.estado='ABI' "+
			" THEN 'Abierto' "+
			" WHEN i.estado='CER' "+
			" THEN 'Cerrado' "+
			" WHEN i.estado='ANU' "+
			"  THEN 'Anulado' "+
			" WHEN i.estado='IGAR' "+
			" THEN 'Ingreso por Garantias' "+
			"  ELSE '' "+
			" END AS estado_ingreso, " +
			" i.preingreso as preingreso,  i.reingreso as reingreso " +
			" ,  es.codigo_pk  ,es.razon_social" + 
			" FROM cuentas cue "+
			" INNER JOIN estados_cuenta ec "+
			" ON (cue.estado_cuenta=ec.codigo) "+
			" LEFT OUTER JOIN admisiones_hospi adh "+
			" ON (cue.id=adh.cuenta) "+
			" LEFT OUTER JOIN admisiones_urgencias adu "+
			" ON (cue.id=adu.cuenta) "+
			" LEFT OUTER JOIN egresos eg "+
			" ON (cue.id=eg.cuenta) "+
			" LEFT OUTER JOIN ingresos i "+
			" ON (cue.id_ingreso=i.id) "+
			" INNER JOIN centros_costo ccos "+
			" ON (cue.area =ccos.codigo) " +
			" LEFT JOIN pac_entidades_subcontratadas pes  " +
			" on(i.PAC_ENTIDADES_SUBCONTRATADAS = pes.CONSECUTIVO) " +
			" LEFT JOIN entidades_subcontratadas es  " +
			" on (pes.ENTIDAD_SUBCONTRATADA = es.codigo_pk ) "+
			" WHERE cue.codigo_paciente         =? "+
			" AND getcuentafinal(cue.id)       IS NULL "+
			" AND i.estado IN ('ABI','CER') "+
			" AND esCuentaCerradaAsocio(cue.id) = 'N' ";
		
		if(!ano.equals("")){
			consulta+=" AND i.fecha_ingreso between TO_DATE('01/01/"+ano+"','dd/MM/yyyy') AND TO_DATE('31/12/"+ano+"','dd/MM/yyyy') ";
		}
		if(!fechaIngresoInicial.equals("") && !fechaIngresoFinal.equals("")){
			consulta+=" AND i.fecha_ingreso between TO_DATE('"+fechaIngresoInicial+"','dd/MM/yyyy') AND TO_DATE('"+fechaIngresoFinal+"','dd/MM/yyyy') ";
		}
		
		if(!centroAtencion.equals("") && !centroAtencion.equals("-1")){
			consulta+=" AND I.CENTRO_ATENCION = "+centroAtencion;
		}
		if(!viaIngreso.equals("") && !viaIngreso.equals("-1")){
			consulta+=" AND CUE.VIA_INGRESO= "+viaIngreso;
		}
		if(!especialidad.equals("") && !especialidad.equals("-1")){
			consulta+=" AND   upper(getresumenespecialidad(cue.via_ingreso,cue.id))= upper('"+especialidad+"') ";
		}
		consulta+=" ORDER BY cue.fecha_apertura DESC, "+
			"  cue.hora_apertura DESC";
		
		
		
		return ejecucionGenerica(con, idPaciente, consulta);
	}
	
	
	/**
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public static List<DtoViasIngresoHC> obtenerViasIngreso(Connection con) throws SQLException{
		List<DtoViasIngresoHC> resultadoVias = new ArrayList<DtoViasIngresoHC>();
		
		String consulta="select v.codigo,v.nombre from vias_ingreso v";
		
		PreparedStatement ps = con.prepareStatement(consulta);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			DtoViasIngresoHC tmp= new DtoViasIngresoHC();
			tmp.setCodigo(String.valueOf(rs.getInt("CODIGO")));
			tmp.setDescripcion(rs.getString("NOMBRE"));
			resultadoVias.add(tmp);
		}
		
		return resultadoVias;
	}
	
	
	public static List<DtoEspecialidadesHC> obtenerEspecialidades(Connection con)throws SQLException
	{
		List<DtoEspecialidadesHC> resEsp = new ArrayList<DtoEspecialidadesHC>();
		
		String consulta="select e.codigo,e.nombre from especialidades e where e.codigo != -1";
		
		PreparedStatement ps = con.prepareStatement(consulta);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			DtoEspecialidadesHC dtoEspecialidadesHC = new DtoEspecialidadesHC();
			dtoEspecialidadesHC.setCodigo(String.valueOf(rs.getInt("CODIGO")));
			dtoEspecialidadesHC.setDescripcion(rs.getString("NOMBRE"));
			resEsp.add(dtoEspecialidadesHC);
			
		}
		
		return resEsp;
	}
	
	public static List<String> obtenerAnos(Connection con, int idPaciente)throws SQLException
	{
		List<String> resEsp = new ArrayList<String>();
		
		String consulta=" select distinct  anos from ( SELECT distinct   SUBSTR(to_char( i.fecha_ingreso,'DD/MM/YYYY'),7,LENGTH(to_char( i.fecha_ingreso,'DD/MM/YYYY')))  ANOS "+
			" FROM cuentas cue "+
			" INNER JOIN estados_cuenta ec "+
			" ON (cue.estado_cuenta=ec.codigo) "+
			" LEFT OUTER JOIN admisiones_hospi adh "+
			" ON (cue.id=adh.cuenta) "+
			" LEFT OUTER JOIN admisiones_urgencias adu "+
			" ON (cue.id=adu.cuenta) "+
			" LEFT OUTER JOIN egresos eg "+
			" ON (cue.id=eg.cuenta) "+
			" LEFT OUTER JOIN ingresos i "+
			" ON (cue.id_ingreso=i.id) "+
			" INNER JOIN centros_costo ccos "+
			" ON (cue.area                      =ccos.codigo) "+
			" WHERE cue.codigo_paciente         =? "+
			" AND getcuentafinal(cue.id)       IS NULL "+
			" AND i.estado                     IN ('ABI','CER') "+
			" AND esCuentaCerradaAsocio(cue.id) = 'N' "+
			" ORDER BY cue.fecha_apertura DESC, "+
			" cue.hora_apertura DESC ) ";
		
		PreparedStatement ps = con.prepareStatement(consulta);
		ps.setInt(1, idPaciente);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			
			resEsp.add(rs.getString("ANOS"));
			
		}
		
		return resEsp;
	}

	/**
	 * MT-5571 Verfica si la valoracion de hospitalizacion es copia de la de urgencias
	 * @param con
	 * @param idCuenta
	 * @return
	 * @throws SQLException 
	 */
	public static boolean valoracionHospEsCopiaValoracionUrg(Connection con, int idCuenta) throws SQLException
	{
		String sql = 
				"SELECT sol.interpretacion AS interpretacion "+
				"FROM valoraciones val "+
				"INNER JOIN solicitudes sol ON (val.numero_solicitud=sol.numero_solicitud) "+
				"WHERE sol.cuenta = (SELECT cuenta_final FROM asocios_cuenta WHERE cuenta_final = ?) ";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try
		{
			ps = con.prepareStatement(sql);
			ps.setInt(1, idCuenta);
			rs = ps.executeQuery();
			int automaticas = 0;
			int otras = 0;
			while(rs.next())
			{
				if(rs.getString("interpretacion") != null && rs.getString("interpretacion").equals(ConstantesBD.textoInterpretacionAutomatica))
				{
					automaticas++;
				}
				else
				{
					otras++;
				}
			}
			if(automaticas > 0 && otras < 1)
			{
				return true;
			}
		}
		catch (SQLException e)
		{
			throw e;
		}
		finally
		{
			try
			{
				if(ps != null)
				{
					ps.close();
				}
				if(rs != null)
				{
					rs.close();
				}
			}
			catch (Exception e2)
			{
				logger.error(e2);
			}
		}
		return false;
	}
	
	/**
	 * MT-5571 verifica si la solicitud tiene interpretacion automatica
	 * @param con
	 * @param solicitud
	 * @return
	 * @throws SQLException
	 */
	public static boolean valoracionTieneInterpretacionAuto(Connection con, int solicitud) throws SQLException
	{
		String sql = 
				"SELECT count(*) AS cantidad "+
				"FROM   ordenes.solicitudes sol "+
				"WHERE  sol.numero_solicitud = ? "+
				"AND    sol.interpretacion = '" + ConstantesBD.textoInterpretacionAutomatica + "'";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean cerrarConexion = false;
		
		if(con == null)
		{
			cerrarConexion = true;
			con = UtilidadBD.abrirConexion();
		}
		
		try
		{
			ps = con.prepareStatement(sql);
			ps.setInt(1, solicitud);
			rs = ps.executeQuery();
			if(rs.next() && rs.getInt("cantidad") > 0)
			{
				return true;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				if(cerrarConexion && con != null )
				{
					con.close();
				}
				if(ps != null)
				{
					ps.close();
				}
				if(rs != null)
				{
					rs.close();
				}
			}
			catch (Exception e2)
			{
				logger.error(e2);
			}
		}
		return false;
	}
	
	/**
	 * MT13612 - No se imprimen correctamente las observaciones de las valoraciones
	 * consultarNumeroSolicitudXIngreso - Método que retorna un listado con los números de las solicitudes (Urgencias/Hospitalización)
	 *  asociadas a determinado ingreso.
	 * @param con
	 * @param ingreso
	 * @return listado con los números de las solicitudes (Urgencias/Hospitalización)
	 *  asociadas a determinado ingreso.
	 */
	public static List<String> consultarNumeroSolicitudXIngreso(Connection con, String ingreso) {
		String query = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> returnVal = new ArrayList<>();
		try {
			query = "SELECT v.numero_solicitud AS numeroSolicitud " +
					"FROM ingresos i " +
					"LEFT JOIN cuentas c " +
					"ON (i.id = c.id_ingreso) " +
					"LEFT JOIN solicitudes s " +
					"ON (c.id = s.cuenta) " +
					"INNER JOIN valoraciones v " +
					"ON(s.numero_solicitud = v.numero_solicitud) " +
					"WHERE i.id = ? " +
					"AND s.tipo IN ("+ConstantesBD.codigoTipoSolicitudInicialUrgencias+","+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+")"+
					"ORDER BY v.numero_solicitud ASC";
			ps = con.prepareStatement(query);
			ps.setString(1, ingreso);
			rs = ps.executeQuery();
			while (rs.next()) {
				returnVal.add(rs.getString("numeroSolicitud"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return returnVal;
	}
}