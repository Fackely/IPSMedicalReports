package com.princetonsa.dao.sqlbase;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.manejoPaciente.DtoObservacionesGeneralesOrdenesMedicas;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.ordenesmedicas.OrdenMedica;
import com.servinte.axioma.dto.historiaClinica.InfoIngresoDto;
import com.servinte.axioma.generadorReporte.historiaClinica.ConstantesImpresionHistoriaClinica;
import com.servinte.axioma.generadorReporte.historiaClinica.DtoDietaHistoriaClinica;
import com.servinte.axioma.generadorReporte.historiaClinica.DtoFiltroImpresionHistoriaClinica;
import com.servinte.axioma.generadorReporte.historiaClinica.DtoResultadoImpresionHistoriaClinica;
 
/**
 * SqlBaseImpresionResumenAtencionesDao
 */
@SuppressWarnings("all")
public class SqlBaseImpresionResumenAtencionesDao
{
	/**
	 * Clave para accder a valor del Mapa
	 */
	public static final String KEY_FECHA_EGRESO = "fecha_Egreso";
	
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseImpresionResumenAtencionesDao.class);

	
	/***
	 * Consulta de los medicamentos administrados
	 */
	public static String consultaAdminMedicamentos="SELECT DISTINCT "+ 
		"am.codigo AS codigo_admin, " +
		"a.codigo AS articulo, "+
		"a.descripcion AS medicamento, "+
		"a.concentracion AS concentracion, "+
		"getnomformafarmaceutica(a.forma_farmaceutica) as forma_farmaceutica, "+
		"getnomunidadmedida(a.unidad_medida) as unidad_medida, "+
		"da.art_principal as artppal " +
		//no debe traer detallado por fecha y hora, este detalle se muestra al detallar la administracion.
		/*"to_char(da.fecha,'DD/MM/YYYY') as fecha, substr(da.hora||'',0,6) as hora, " +
		"da.adelanto_x_necesidad, " +
		"da.nada_via_oral, " +
		"da.usuario_rechazo " +*/
		"FROM solicitudes s  "+ 
		"INNER JOIN cuentas c ON (s.cuenta = c.id)  "+
		"INNER JOIN admin_medicamentos am ON(am.numero_solicitud=s.numero_solicitud) "+ 
		"INNER JOIN detalle_admin da ON (da.administracion=am.codigo) "+  
		"INNER JOIN articulo a ON (a.codigo=da.articulo) " +
		"INNER JOIN naturaleza_articulo na on (na.acronimo=a.naturaleza and a.institucion=na.institucion) "+
		"WHERE EXISTS ( SELECT 'X' FROM naturaleza_articulo na WHERE na.acronimo=a.naturaleza AND a.institucion=na.institucion AND "+
		"c.id_ingreso=? and na.es_medicamento='"+ConstantesBD.acronimoSi+"' ";
	
	/**
	 * Consulta del detalle de adminsitracion de un medicamento
	 */
	private static final String consultaDetalleArticuloAdmin="SELECT "+
		"CASE WHEN am.usuario IS NULL THEN '' ELSE getnombreusuario2(am.usuario) END AS responsable, "+
		"to_char(da.fecha,'DD/MM/YYYY') AS fecha, "+
		"substr(da.hora||'',0,6) as hora, "+
		"da.cantidad AS unidades_consumidas, "+
		"CASE WHEN da.observaciones IS NULL THEN '' ELSE da.observaciones END AS observaciones, "+
		"ds.dosis AS dosis, "+
		"CASE WHEN ua.unidad_medida IS NULL THEN '' ELSE ua.unidad_medida END AS unidad_medida, "+
		"ds.frecuencia AS frecuencia, "+
		"ds.tipo_frecuencia AS tipo_frecuencia, "+
		"ds.via AS via, "+
		"'' as dosis_administrada, "+
		"da.adelanto_x_necesidad, " +
		"da.nada_via_oral, " +
		"da.usuario_rechazo, " +
		" arti.codigo  AS codigo_articulo_principal,  " +
		" s.numero_solicitud as  numero_solicitud " +
		"FROM solicitudes s "+ 
		"INNER JOIN cuentas c ON(s.cuenta=c.id)  "+ 
		"INNER JOIN admin_medicamentos am ON(am.numero_solicitud=s.numero_solicitud) "+ 
		"INNER JOIN detalle_admin da on(da.administracion=am.codigo AND da.articulo = ?) "+  
		"INNER JOIN detalle_solicitudes ds on (ds.numero_solicitud=s.numero_solicitud  and ds.articulo=da.articulo) " +
		" LEFT OUTER  JOIN inventarios.articulo arti on(arti.codigo=ds.articulo_principal) "+ 
		"  LEFT OUTER JOIN unidosis_x_articulo ua ON(ua.codigo=ds.unidosis_articulo) "+ 
		" WHERE "+ 
		" c.id_ingreso= ? ";
	
	private static String consultaDetalleArticuloAdminE="SELECT " +
		"CASE WHEN am.usuario IS NULL THEN '' ELSE getnombreusuario2(am.usuario) END AS responsable, "+
		"to_char(da.fecha,'DD/MM/YYYY') AS fecha, "+
		"substr(da.hora||'',0,6) as hora, "+
		"da.cantidad AS unidades_consumidas, "+
		"CASE WHEN da.observaciones IS NULL THEN '' ELSE da.observaciones END AS observaciones, "+
		"da.adelanto_x_necesidad, " +
		"da.nada_via_oral, " +
		"da.usuario_rechazo " +
		"FROM cuentas c "+ 
		"INNER JOIN solicitudes s ON(s.cuenta=c.id) "+ 
		"INNER JOIN admin_medicamentos am ON(am.numero_solicitud=s.numero_solicitud) "+ 
		"inner join detalle_admin da on(da.administracion=am.codigo) "+
		"WHERE da.art_principal = ? AND c.id_ingreso=? ";

	

	/**
	 * 
	 */
	public static String consultaInsumos="SELECT " +
												" da.articulo as articulo," +
												" a.descripcion as insumo," +
												" da.cantidad as cantidad," +
												" getnombrepersona(codigo_medico) as responsable," +
												" to_char(da.fecha,'dd/mm/yyyy') as fecha," +
												" substr(da.hora||'',0,6) as hora " +
										" from admin_medicamentos am " +
										" inner join detalle_admin da on(da.administracion=am.codigo and da.articulo=da.articulo) " +
										" inner join solicitudes s on(s.numero_solicitud=am.numero_solicitud)  " +
										" inner join cuentas c on(s.cuenta=c.id) " +
										" inner join articulo a on(da.articulo=a.codigo) " +
										" inner join naturaleza_articulo na on(na.acronimo=a.naturaleza and a.institucion=na.institucion) "+
										" where na.es_medicamento = '"+ConstantesBD.acronimoNo+"' and c.id_ingreso=? ";
	




	/**
	 * 
	 */
	public static String consultaRespuestaInterpretacionProcedimientos="SELECT " +
																			" s.numero_solicitud AS numerosolicitud, "+
																			" to_char(rsp.fecha_ejecucion,'dd/mm/yyyy')||' '|| substr(rsp.hora_grabacion||'',0,6) as fechahora," +
																			" sp.codigo_servicio_solicitado ||' - '||getnombreservicio(codigo_servicio_solicitado,0) as servicio," +
																			" rsp.codigo AS codigorespuesta, "+
																			" rsp.resultados as resultado," +
																			" rsp.observaciones as observaciones," +
																			" s.datos_medico_responde as medicoresponde," +
																			" s.interpretacion as interpretacion," +
																			" getnombrepersona(codigo_medico_interpretacion) as medicointerpreta, " +
																			" CASE WHEN getSolicitudTieneIncluidos(s.numero_solicitud) > 0 THEN "+ValoresPorDefecto.getValorTrueParaConsultas()+" ELSE "+ValoresPorDefecto.getValorFalseParaConsultas()+" END AS  incluyeserviciosarticulos "+
																	" from solicitudes s " +
																	" inner join sol_procedimientos sp on(sp.numero_solicitud=s.numero_solicitud) " +
																	" inner join res_sol_proc rsp on(sp.numero_solicitud=rsp.numero_solicitud) " +
																	" inner join cuentas c on(c.id=s.cuenta) " +
																	" where c.id_ingreso=? ";
	
	/**
	 * 
	 */
	public static String consultaOrdenesTipoMonitore="SELECT " +
															" to_char(ehom.fecha_orden,'dd/mm/yyyy') as fecha," +
															" substr(ehom.hora_orden||'',0,6)  as hora, " +
															" tm.nombre as tipomonitoreo " +
														" from ordenes_medicas om " +
														" inner join encabezado_histo_orden_m ehom on(ehom.orden_medica=om.codigo) " +
														" inner join orden_tipo_monitoreo otm on(otm.codigo_histo_encabezado = ehom.codigo) " +
														" inner join cuentas c on(c.id=om.cuenta) " +
														" inner join tipo_monitoreo tm on(tm.codigo=otm.tipo_monitoreo) " +
														" where c.id_ingreso=? ";

	/**
	 * 
	 */
	public static String consultaOrdenesSoporteRespiratorio="SELECT " +
																	" to_char(ehom.fecha_orden,'dd/mm/yyyy') as fecha," +
																	" substr(ehom.hora_orden||'',0,6)  as hora, " +
																	" osr.cantidad as cantidad," +
																	" ee.descripcion as equipo," +
																	" om.descripcion_soporte as descripcion " +
															" from ordenes_medicas om " +
															" inner join encabezado_histo_orden_m ehom on(ehom.orden_medica=om.codigo) " +
															" inner join orden_soporte_respira osr on(osr.codigo_histo_enca = ehom.codigo) " +
															" inner join cuentas c on(c.id=om.cuenta) " +
															" inner join equipo_elemento_cc_inst eeci on(eeci.codigo=osr.equipo_elemento_cc_inst)  " +
															" inner join equipo_elemento ee on (ee.codigo=eeci.equipo_elemento) " +
															" where c.id_ingreso=? ";


	/**
	 * 
	 */
	public static String consultaCirugia="SELECT " +
											" s.numero_solicitud as numerosolicitud, " +
											" to_char(s.fecha_solicitud,'dd/mm/yyyy') as fecha," +
											"  substr(s.hora_solicitud||'',0,6) as hora, " +
											" getnomcentrocosto(s.centro_costo_solicitado) as centrocosto," +
											" getnombreespecialidad(s.especialidad_solicitante) as especialidad," +
											" case when pq.fecha_cirugia is null then case when sc.fecha_inicial_cx is not null then ''||sc.fecha_inicial_cx else '' end else ''||pq.fecha_cirugia end  as fechaestimada," +
											" case when pq.duracion is null then case when sc.duracion_final_cx is not null then ''||sc.duracion_final_cx else '' end else ''||pq.duracion end as duracion," +
											" case when pq.requiere_uci="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'Si' else 'No' end  as requiereuci, " +
											" case when anulsol.fecha is null then '' else ''||to_char(anulsol.fecha,'dd/mm/yyyy') end as fechaanulacion," +
											" case when anulsol.hora  is null then '' else substr(anulsol.hora||'',0,6) end as horaanulacion," +
											" map.nombre as motivoanulacion, " +
											" case when anulsol.usuario_anula is null then '' else perso.PRIMER_NOMBRE||' '||perso.segundo_nombre||' '||perso.primer_apellido||' '||perso.segundo_apellido    ||' - '||m.numero_registro||' ' ||  GETESPECIALIDADESMEDICO1( usua.codigo_persona,',') " +
											"   end as medicoanulacion, " +
											" getdescsalsalapac(sc.salida_sala_paciente) As salsalpac," +
											" to_char(sc.fecha_salida_sala,'dd/mm/yyyy') as fechasalpac," +
											" sc.hora_salida_sala As horasalpac, " +
											" s.urgente AS urgente"+
										" from solicitudes s " +
										" inner join cuentas c on(s.cuenta=c.id) " +
										" inner join solicitudes_cirugia sc ON(sc.numero_solicitud = s.numero_solicitud AND (sc.ind_qx = '"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia+"' OR sc.ind_qx = '"+ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento+"')) " +
										" inner join peticion_qx  pq on(pq.codigo=sc.codigo_peticion) " +
										" left outer join hoja_quirurgica hq ON(hq.numero_solicitud = s.numero_solicitud) " +
										" left outer join (anulacion_sol_cx anulsol inner join motivos_anul_qx_inst maqi on(anulsol.motivo=maqi.codigo) inner join motivo_anulacion_pet map on(map.codigo=maqi.motivos_anulacion))on(anulsol.numero_solicitud=s.numero_solicitud)  " +
										"   LEFT OUTER JOIN usuarios usua  on(anulsol.usuario_anula = usua.login) LEFT OUTER JOIN medicos m  on( usua.codigo_persona = m.codigo_medico) left outer join personas perso on( usua.codigo_persona =perso.codigo) " +
										" where s.tipo='"+ConstantesBD.codigoTipoSolicitudCirugia+"'  ";
	
	
	/**
	 * 
	 */
	public static String consultaSolicitudesMedicamentosInsumos="SELECT " +
												" s.numero_solicitud as numerosolicitud," +
												" to_char(s.fecha_solicitud,'dd/mm/yyyy') as fecha," +
												" substr(s.hora_solicitud||'',0,6) as hora," +
												" getnomcentrocosto(s.centro_costo_solicitante) as centrocosto, " +
												" getnomcentrocosto(s.centro_costo_solicitado) as farmacia, " +
												" case when anulsol.fecha is null then '' else ''||to_char(anulsol.fecha,'dd/mm/yyyy') end as fechaanulacion," +
												" case when anulsol.hora  is null then '' else substr(anulsol.hora||'',0,6) end as horaanulacion," +
												" anulsol.motivo as motivoanulacion, "+
												" case when anulsol.codigo_medico is null then '' else   perso.PRIMER_NOMBRE ||' ' ||perso.segundo_nombre ||' ' ||perso.primer_apellido ||' ' ||perso.segundo_apellido ||' - ' " +
												"  ||m.numero_registro ||' '  || GETESPECIALIDADESMEDICO1(anulsol.codigo_medico,',')  " +
												"end as medicoanulacion," +
												" s.datos_medico as medico," +
												" sm.observaciones_generales as obsgenerales " +
											" from solicitudes s " +
											" inner join solicitudes_medicamentos sm on (s.numero_solicitud=sm.numero_solicitud) " +
											" inner join cuentas c on(s.cuenta=c.id) " +
											" left outer join anulaciones_solicitud anulsol on(anulsol.solicitud=s.numero_solicitud) " +
											" LEFT OUTER JOIN medicos m ON(anulsol.codigo_medico = m.codigo_medico)  left outer join personas perso on(anulsol.codigo_medico =perso.codigo) " +
											" where s.tipo='"+ConstantesBD.codigoTipoSolicitudMedicamentos+"'   and sm.orden_dieta is null";
	
	/**
	 * 
	 */
	public static String consultaSolicitudesProcedimientos="SELECT " +
																" s.numero_solicitud as numerosolicitud," +
																" to_char(s.fecha_solicitud,'dd/mm/yyyy') as fecha," +
																" substr(s.hora_solicitud||'',0,6) as hora," +
																" getnomcentrocosto(s.centro_costo_solicitante) as centrocostosolicitante," +
																" getCentroAtencionCC(s.centro_costo_solicitante) as centroatencionsolicitante, "+
																" getnomcentrocosto(s.centro_costo_solicitado) as centrocostosolicitado," +
																" getCentroAtencionCC(s.centro_costo_solicitado) as centroatencionsolicitado, "+
																" getnombreespecialidad(s.especialidad_solicitante) as especialidad," +
																" rs.codigo_propietario as codigocups," +
																" rs.servicio as codigoservicio," +
																" rs.descripcion as servicio," +
																" case when ser.espos="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'Si' else 'No' end as pos, " +
																" fs.nombre as finalidad, " +
																" '1' as cantidad," +
																" sp.frecuencia as frecuencia," +
																" getnombretipofrecuencia(sp.tipo_frecuencia) as tipofrecuencia," +
																" case when solicitud_multiple ="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'Si' else 'No' end as multiple," +
																" case when s.urgente  ="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'Si' else 'No' end as urgente," +
																" case when anulsol.fecha is null then '' else ''||to_char(anulsol.fecha,'dd/mm/yyyy') end as fechaanulacion," +
																" case when anulsol.hora  is null then '' else substr(anulsol.hora||'',0,6) end as horaanulacion," +
																" anulsol.motivo as motivoanulacion," +
																" case when anulsol.codigo_medico is null then '' else perso.PRIMER_NOMBRE||' '||perso.segundo_nombre||' '||perso.primer_apellido||' '||perso.segundo_apellido    ||' - '||m.numero_registro||' ' ||  GETESPECIALIDADESMEDICO1(anulsol.codigo_medico,',') " +
																" end as medicoanulacion," +
																" sp.comentario as observaciones," +
																" s.datos_medico as medico " +
														" from solicitudes s " +
														" inner join sol_procedimientos sp on(sp.numero_solicitud=s.numero_solicitud) " +
														" inner join cuentas c on(c.id=s.cuenta) " +
														" inner join servicios ser on (ser.codigo=sp.codigo_servicio_solicitado) " +
														" inner join  referencias_servicio rs on(rs.servicio=ser.codigo and rs.tipo_tarifario='"+ConstantesBD.codigoTarifarioCups+"') " +
														" inner join finalidades_servicio fs on (fs.codigo=sp.finalidad) " +
														" left outer join anulaciones_solicitud anulsol on(anulsol.solicitud=s.numero_solicitud)  " +
														"  LEFT OUTER JOIN medicos m  on(anulsol.codigo_medico = m.codigo_medico)" +
														" left outer join personas perso  on(anulsol.codigo_medico =perso.codigo) " +
														" where s.tipo IN ("+ConstantesBD.codigoTipoSolicitudProcedimiento+","+ConstantesBD.codigoTipoSolicitudCirugia+")  ";
	
	/**
	 * 
	 */
	public static String consultaSolicitudesInterconsultas=" SELECT " +
																	" s.numero_solicitud as numerosolicitud," +
																	" to_char(s.fecha_solicitud,'dd/mm/yyyy') as fecha," +
																	" substr(s.hora_solicitud||'',0,6) as hora," +
																	" getnomcentrocosto(s.centro_costo_solicitante) as centrocostosolicitante," +
																	" getnomcentrocosto(s.centro_costo_solicitado) as centrocostosolicitado," +
																	" getnombreespecialidad(s.especialidad_solicitante) as especialidad," +
																	" rs.codigo_propietario as codigocups," +
																	" rs.descripcion as servicio," +
																	" case when ser.espos="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'Si' else 'No' end as pos, " +
																	" case when s.urgente  ="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'Si' else 'No' end as urgente," +
																	" si.comentario as observaciones, " +
																	" si.motivo_solicitud as motivo," +
																	" omi.nombre as manejo, " +
																	" case when anulsol.fecha is null then '' else ''||to_char(anulsol.fecha,'dd/mm/yyyy') end as fechaanulacion," +
																	" case when anulsol.hora  is null then '' else substr(anulsol.hora||'',0,6) end as horaanulacion," +
																	" anulsol.motivo as motivoanulacion," +
																	" case when anulsol.codigo_medico is null then ''    ELSE perso.PRIMER_NOMBRE||' '||perso.segundo_nombre||' '||perso.primer_apellido||' '||perso.segundo_apellido " +
																	"   ||' - '||m.numero_registro||' ' || " +
																	"  GETESPECIALIDADESMEDICO1(anulsol.codigo_medico,',')  END AS medicoanulacion,   " +
																	" s.datos_medico as medico, " +
																	" case when si.resumen_historia_clinica is null then '' else si.resumen_historia_clinica end as rhc " +
														" from solicitudes s " +
														" inner join solicitudes_inter si On(si.numero_solicitud=s.numero_solicitud) " +
														" inner join cuentas c on(c.id=s.cuenta) " +
														" inner join servicios ser on (ser.codigo=si.codigo_servicio_solicitado) " +
														" inner join  referencias_servicio rs on(rs.servicio=ser.codigo and rs.tipo_tarifario=0) " +
														" inner join op_man_intercon omi on(omi.codigo=si.codigo_manejo_interconsulta) " +
														" left outer join anulaciones_solicitud anulsol on(anulsol.solicitud=s.numero_solicitud)  " +
														" LEFT OUTER JOIN medicos m on(anulsol.codigo_medico = m.codigo_medico) " +
														" left outer join personas perso on(anulsol.codigo_medico =perso.codigo) " +
														" where s.tipo = "+ConstantesBD.codigoTipoSolicitudInterconsulta+"  ";
	
	/**
	 * 
	 */
	public static String consultaOrdenesMedicas="SELECT " +
			 										" ehom.codigo as codigoencabezado," +
			 										" ehom.orden_medica as codigoorden," +
			 										" to_char(ehom.fecha_orden,'dd/mm/yyyy') as fecha," +
			 										" substr(ehom.hora_orden||'',0,6) as hora," +
			 										" ehom.datos_medico," +
			 										" osr.descripcion AS dessoporte," +
			 										" om.descripcion_dieta_oral as descdietaoral," +
			 										" descripcion_dieta_par as descdietapar," +
			 										" om.observaciones_generales as observacionesgenerales, " +
			 										" hnom.observaciones as observacioneshoja, " +
			 										" om.observaciones_generales as observacionesgenerales, " +
			 										" case when hnom.fecha_fin  is null then '' else ''||to_char(hnom.fecha_fin,'dd/mm/yyyy') end as fechasuspencionhoja " +
			 									" from ordenes_medicas om " +
			 									" inner join hoja_neurologica_orden_m hnom on(hnom.orden_medica=om.codigo)" +
			 									" inner join encabezado_histo_orden_m ehom on(ehom.orden_medica=om.codigo) " +
			 									" inner join cuentas c on(c.id=om.cuenta)" +
			 									" LEFT JOIN orden_soporte_respira osr ON(osr.codigo_histo_enca = ehom.codigo) ";
	
	/**
	 * 
	 */
	public static String consultaSoportesRespiratorio="SELECT " +
															" chre.codigo as codigo," +
															" to_char(chre.fecha_registro,'dd/mm/yyyy') as fecha," +
															" substr(chre.hora_registro,0,6) as hora," +
															" chre.obs_soporte as observaciones," +
															" chre.datos_medico as usuario " +
													" from enca_histo_registro_enfer chre " +
													" inner join registro_enfermeria re on(re.codigo=chre.registro_enfer) " +
													" inner join cuentas c on (re.cuenta=c.id) " +
													" inner join  soporte_resp_enfer_valor srev on(srev.codigo_histo_enfer=chre.codigo) " +
													" where c.id_ingreso=?";
	
	
	/**
	 * Cadena para consultar los tipo de notas de recuperacion parametrrizados
	 */
	private static final String consultarTiposNotasRecuperacionStr=" SELECT pnr.codigo as codigoRelacion, " +
																   " pnr.tipo as codigoTipo, " +
																   " tnr.descripcion as nombreTipo, " +
																   " pnr.campo as codigoCampo, " +
																   " (SELECT (select count(1) from param_notas_recup_inst pnri where pnri.tipo=pnr.tipo) from param_notas_recup_inst p where p.codigo=pnr.codigo) as \"rows\", "+
																   " cnr.descripcion as nombreCampo " +
																   " FROM param_notas_recup_inst pnr " +
																   " INNER JOIN tipos_notas_recuperacion tnr ON(pnr.tipo=tnr.codigo) " +
																   " INNER JOIN campos_notas_recuperacion cnr ON(pnr.campo=cnr.codigo) " +
																   " WHERE  pnr.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+
																   " ORDER BY pnr.codigo ";
	
	
	/**
	 * Cadena con el statement necesario para consultar el historico de las notas de recuperacion
	 */
	private static final String consultarHistoricoNotasRecuperacionStr=" SELECT dnr.numero_solicitud as numeroSolicitud, " +
																	   " to_char(dnr.fecha_recuperacion, '"+ConstantesBD.formatoFechaAp+"') as fechaRecuperacion, " +
																	   " dnr.hora_recuperacion as horaRecuperacion, " +
																	   " (to_char(dnr.fecha_recuperacion,'"+ConstantesBD.formatoFechaAp+"')||' - '||substr(dnr.hora_recuperacion, 0,6)) as fechaHoraRecuperacion, "+
																	   " dnr.nota_recup as codigoRelacion, " +
																	   " dnr.valor as valorNota, "+
																	   " to_char(dnr.fecha_grabacion, '"+ConstantesBD.formatoFechaAp+"') as fechaGrabacion, " +
																	   " dnr.hora_grabacion as horaGrabacion, " +
																	   " administracion.getnombremedico(dnr.codigo_enfermera)||' '||med.numero_registro||' ' || " +
																	   " getespecialidadesmedico1(med.codigo_medico,',')   AS enfermera " +
																	   " FROM det_notas_recuperacion dnr  join medicos med " +
																	   " on (dnr.codigo_enfermera=med.codigo_medico)  " +
																	   " WHERE dnr.nota_recup=? " +
																	   " AND dnr.numero_solicitud=? " +
																	   " ORDER BY dnr.fecha_recuperacion DESC, dnr.hora_recuperacion DESC ";
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap consultarAdminMedicamentos(Connection con, HashMap vo,Integer numeroArticulo)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		String cadena=consultaAdminMedicamentos;
		logger.info("\n\nCONSECUTIVO DEL MAPA>>>>>>>>>>>>>>>>>>>"+vo.get("ingreso")+"\n\n");
		PreparedStatementDecorator ps = null; 
		ResultSetDecorator rs = null; 
		try
		{
			if(!(vo.get("cuenta")+"").trim().equals(""))
			{
				if(!(vo.get("cuentaAsocio")+"").trim().equals(""))
				{
					cadena+=" AND c.id IN ('"+ vo.get("cuenta")+"','"+vo.get("cuentaAsocio")+"') ";
				}
				else{
				cadena+=" AND c.id='"+ vo.get("cuenta")+"'";
				}
			}
			
			if((!(vo.get("fechaInicial")+"").trim().equals(""))&&(!(vo.get("fechaFinal")+"").trim().equals("")))
			{
				cadena+=" AND da.fecha  between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"'";
			}
			if((!(vo.get("horaInicial")+"").trim().equals(""))&&(!(vo.get("horaFinal")+"").trim().equals("")))
			{
				cadena+=" AND ( (da.fecha > '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"') or (da.fecha = '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and da.hora >= '"+vo.get("horaInicial")+"') ) " +
						"and ((da.fecha < '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"') or (da.fecha = '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"' and da.hora <= '"+vo.get("horaFinal")+"')) ";
			}
			
			if(!(vo.get("solicitudes")+"").equals("") && !UtilidadTexto.isEmpty(vo.get("solicitudes")+""))
			{
				cadena+=" AND s.numero_solicitud IN ("+ vo.get("solicitudes")+") ";
			}
			
			if(!numeroArticulo.equals(ConstantesBD.codigoNuncaValido)){
				cadena+=" AND a.codigo=? ";
			}
			
			cadena+=" ) ORDER BY a.descripcion";
			
			logger.info("Consulta -->"+cadena);
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setObject(1, vo.get("ingreso"));
			if(!numeroArticulo.equals(ConstantesBD.codigoNuncaValido)){
				ps.setInt(2, numeroArticulo);
			}
			rs = new ResultSetDecorator(ps.executeQuery()); 
			mapa =UtilidadBD.cargarValueObject(rs);
			logger.info("\n\nMAPAAAAAAAAAAAAAAAAAAccccccccccccc"+mapa+"\n\n");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
			
		}
		return mapa;
	}
	
	/**
	 * M�todo que consulta el detalle de adminsitraci�n de un articulo
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap consultarDetalleArticuloAdmin(Connection con, HashMap vo)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		String cadena=consultaDetalleArticuloAdmin;
		
		
		PreparedStatementDecorator ps = null; 
		ResultSetDecorator rs = null; 
		try
		{
			if(!(vo.get("cuenta")+"").trim().equals(""))
			{
				if(!(vo.get("cuentaAsocio")+"").trim().equals(""))
				{
					cadena+=" AND c.id IN ('"+ vo.get("cuenta")+"','"+vo.get("cuentaAsocio")+"') ";
				}
				else{
				cadena+=" AND c.id='"+ vo.get("cuenta")+"'";
				}
				
			}
			
			if((!(vo.get("fechaInicial")+"").trim().equals(""))&&(!(vo.get("fechaFinal")+"").trim().equals("")))
			{
				cadena+=" AND da.fecha  between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"'";
			}
			if((!(vo.get("horaInicial")+"").trim().equals(""))&&(!(vo.get("horaFinal")+"").trim().equals("")))
			{
				cadena+=" AND ( (da.fecha > '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"') or (da.fecha = '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and da.hora >= '"+vo.get("horaInicial")+"') ) " +
						"and ((da.fecha < '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"') or (da.fecha = '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"' and da.hora <= '"+vo.get("horaFinal")+"')) ";
			}
			
			cadena+=" ORDER BY da.fecha ASC, da.hora ASC";
			
			logger.info("Consulta -->"+cadena);
			logger.info("articulo=> "+vo.get("articulo")+" ingreso=> "+vo.get("ingreso"));
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setObject(1, vo.get("articulo"));
			ps.setObject(2, vo.get("ingreso"));
			rs = new ResultSetDecorator(ps.executeQuery());
			mapa =UtilidadBD.cargarValueObject(rs);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		return mapa;
	}
	
	
	public static HashMap consultarDetalleArticuloAdminE(Connection con, int artppal, String ingreso)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		String cadena=consultaDetalleArticuloAdminE;
		logger.info("CODIGO EN CONSULTA SQLLLLLLLLL>>>>>>>>>>>"+artppal);
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, artppal);
			ps.setString(2, ingreso);
			mapa =UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		logger.info("consultaaaDetallEqui>>>>>>>>>>>>>>"+cadena);
		return mapa;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap consultarInsumos(Connection con, HashMap vo)
	{
		
		
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		String cadena=consultaInsumos;
		try
		{
			if((!(vo.get("fechaInicial")+"").trim().equals(""))&&(!(vo.get("fechaFinal")+"").trim().equals("")))
			{
				cadena+=" AND da.fecha  between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"'";
			}
			if((!(vo.get("horaInicial")+"").trim().equals(""))&&(!(vo.get("horaFinal")+"").trim().equals("")))
			{
				cadena+=" AND ( (da.fecha > '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"') or (da.fecha = '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and da.hora >= '"+vo.get("horaInicial")+"') ) " +
				"and ((da.fecha < '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"') or (da.fecha = '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"' and da.hora <= '"+vo.get("horaFinal")+"')) ";
				
			}
			if(!(vo.get("cuenta")+"").trim().equals(""))
			{
				if(!(vo.get("cuentaAsocio")+"").trim().equals(""))
				{
					cadena+=" AND s.cuenta IN ('"+ vo.get("cuenta")+"','"+vo.get("cuentaAsocio")+"') ";
				}
				else{
					cadena+=" AND s.cuenta='"+ vo.get("cuenta")+"'";
				}
				
			}
			cadena+=" order by da.fecha,a.descripcion,da.hora ";
			logger.info("Consulta -->"+cadena);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1, vo.get("ingreso"));
			mapa =UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap consultarRespuestaInterpretacionProcedimientos(Connection con, HashMap vo)
	{
		
		
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		String cadena=consultaRespuestaInterpretacionProcedimientos;
		try
		{
			if((!(vo.get("fechaInicial")+"").trim().equals(""))&&(!(vo.get("fechaFinal")+"").trim().equals("")))
			{
				cadena+=" AND rsp.fecha_ejecucion  between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"'";
			}
			if((!(vo.get("horaInicial")+"").trim().equals(""))&&(!(vo.get("horaFinal")+"").trim().equals("")))
			{
				cadena+=" AND ( (rsp.fecha_ejecucion > '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"') or (rsp.fecha_ejecucion = '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and rsp.hora_grabacion >= '"+vo.get("horaInicial")+"') ) " +
				"and ((rsp.fecha_ejecucion < '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"') or (rsp.fecha_ejecucion = '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"' and rsp.hora_grabacion <= '"+vo.get("horaFinal")+"')) ";
				
			}
			if(!(vo.get("cuenta")+"").trim().equals(""))
			{
				cadena+=" AND c.id='"+ vo.get("cuenta")+"'";
			}
			cadena+=" order by rsp.fecha_ejecucion,rsp.hora_grabacion ";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("\n\n\n\n cadena: "+cadena);
			logger.info("\n 1: "+vo.get("ingreso"));
			ps.setObject(1, vo.get("ingreso"));
			mapa =UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}


	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap consultarOrdenesTipoMonitore(Connection con, HashMap vo)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		String cadena=consultaOrdenesTipoMonitore;
		try
		{
			if((!(vo.get("fechaInicial")+"").trim().equals(""))&&(!(vo.get("fechaFinal")+"").trim().equals("")))
			{
				cadena+=" AND ehom.fecha_orden  between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"'";
			}
			if((!(vo.get("horaInicial")+"").trim().equals(""))&&(!(vo.get("horaFinal")+"").trim().equals("")))
			{
				cadena+=" AND ( (ehom.fecha_orden > '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"') or (ehom.fecha_orden = '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and ehom.hora_orden >= '"+vo.get("horaInicial")+"') ) " +
				"and ((ehom.fecha_orden < '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"') or (ehom.fecha_orden = '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"' and ehom.hora_orden <= '"+vo.get("horaFinal")+"')) ";
			}
			if(!(vo.get("cuenta")+"").trim().equals(""))
			{
				cadena+=" AND c.id='"+ vo.get("cuenta")+"'";
			}
			cadena+=" order by ehom.fecha_orden,ehom.hora_orden ";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1, vo.get("ingreso"));
			mapa =UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}


	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap consultarOrdenesSoporteRespiratorio(Connection con, HashMap vo)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		String cadena=consultaOrdenesSoporteRespiratorio;
		try
		{
			if((!(vo.get("fechaInicial")+"").trim().equals(""))&&(!(vo.get("fechaFinal")+"").trim().equals("")))
			{
				cadena+=" AND ehom.fecha_orden  between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"'";
			}
			if((!(vo.get("horaInicial")+"").trim().equals(""))&&(!(vo.get("horaFinal")+"").trim().equals("")))
			{
				cadena+=" AND ( (ehom.fecha_orden > '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"') or (ehom.fecha_orden = '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and ehom.hora_orden >= '"+vo.get("horaInicial")+"') ) " +
				"and ((ehom.fecha_orden < '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"') or (ehom.fecha_orden = '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"' and ehom.hora_orden <= '"+vo.get("horaFinal")+"')) ";
				
			}
			if(!(vo.get("cuenta")+"").trim().equals(""))
			{
				cadena+=" AND c.id='"+ vo.get("cuenta")+"'";
			}
			cadena+=" order by ehom.fecha_orden,ehom.hora_orden ";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1, vo.get("ingreso"));
			mapa =UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}


	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap consultarOrdenesDieta(Connection con, HashMap vo)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		/*
		String cadena=consultaOrdenesDieta;
		try
		{
			if((!(vo.get("fechaInicial")+"").trim().equals(""))&&(!(vo.get("fechaFinal")+"").trim().equals("")))
			{
				cadena+=" AND ehom.fecha_orden  between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"'";
			}
			if((!(vo.get("horaInicial")+"").trim().equals(""))&&(!(vo.get("horaFinal")+"").trim().equals("")))
			{
				cadena+=" AND ehom.hora_orden  between '"+vo.get("horaInicial")+"' and '"+vo.get("horaFinal")+"'";
			}
			if(!(vo.get("cuenta")+"").trim().equals(""))
			{
				cadena+=" AND c.id='"+ vo.get("cuenta")+"'";
			}
			cadena+=" order by ehom.fecha_orden,ehom.hora_orden ";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1, vo.get("ingreso"));
			mapa =UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		*/
		return mapa;
	}


	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap consultarOrdenesCirugias(Connection con, HashMap vo)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		String cadena=consultaCirugia+" and c.id_ingreso=?";
		try
		{
			if((!(vo.get("fechaInicial")+"").trim().equals(""))&&(!(vo.get("fechaFinal")+"").trim().equals("")))
			{
				cadena+=" AND s.fecha_solicitud  between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"'";
			}
			if((!(vo.get("horaInicial")+"").trim().equals(""))&&(!(vo.get("horaFinal")+"").trim().equals("")))
			{
				cadena+=" AND ( (s.fecha_solicitud > '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"') or (s.fecha_solicitud = '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and s.hora_solicitud >= '"+vo.get("horaInicial")+"') ) " +
				"and ((s.fecha_solicitud < '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"') or (s.fecha_solicitud = '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"' and s.hora_solicitud <= '"+vo.get("horaFinal")+"')) ";
			}
			if(!(vo.get("cuenta")+"").trim().equals(""))
			{
				cadena+=" AND c.id='"+ vo.get("cuenta")+"'";
			}
			
			if(!(vo.get("solicitudes")+"").equals("") && !UtilidadTexto.isEmpty(String.valueOf(vo.get("solicitudes"))) )
			{
				cadena+=" AND s.numero_solicitud IN ("+ vo.get("solicitudes")+") ";
			}
			
			cadena+=" order by s.fecha_solicitud,s.hora_solicitud ";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1, vo.get("ingreso"));
			
			logger.info("\n\n\n valor del mapa >> "+cadena.replace("?",vo.get("ingreso").toString()));
			
			mapa =UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			for(int i=0;i<Integer.parseInt(mapa.get("numRegistros")+"");i++)
			{
				ps= new PreparedStatementDecorator(con.prepareStatement("SELECT rs.codigo_propietario as codigo,rs.descripcion as servicio,getnombreespecialidad(s.especialidad) as especialidad from sol_cirugia_por_servicio scs inner join servicios s on(s.codigo=scs.servicio) inner join referencias_servicio rs on(rs.servicio=s.codigo and rs.tipo_tarifario='"+ConstantesBD.codigoTarifarioCups+"') where numero_solicitud='"+mapa.get("numerosolicitud_"+i)+"'",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				mapa.put("servicios_"+i,UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())));
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap consultarOrdenesCirugias(int numeroSolicitud)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		
		Connection con =UtilidadBD.abrirConexion();
		String cadena=consultaCirugia+" and s.numero_solicitud=?";
		try
		{
			
			cadena+=" order by s.fecha_solicitud,s.hora_solicitud ";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1, numeroSolicitud);
			
			
			mapa =UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			for(int i=0;i<Integer.parseInt(mapa.get("numRegistros")+"");i++)
			{
				ps= new PreparedStatementDecorator(con.prepareStatement("SELECT rs.codigo_propietario as codigo,rs.descripcion as servicio,getnombreespecialidad(s.especialidad) as especialidad from sol_cirugia_por_servicio scs inner join servicios s on(s.codigo=scs.servicio) inner join referencias_servicio rs on(rs.servicio=s.codigo and rs.tipo_tarifario='"+ConstantesBD.codigoTarifarioCups+"') where numero_solicitud='"+mapa.get("numerosolicitud_"+i)+"'",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				mapa.put("servicios_"+i,UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())));
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			UtilidadBD.closeConnection(con);
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap consultarSolicitudesMedicamentoInsumos(Connection con, HashMap vo)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		String cadena=consultaSolicitudesMedicamentosInsumos+"  and c.id_ingreso=?";
		try
		{
			if((!(vo.get("fechaInicial")+"").trim().equals(""))&&(!(vo.get("fechaFinal")+"").trim().equals("")))
			{
				cadena+=" AND s.fecha_solicitud  between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"'";
			}
			if((!(vo.get("horaInicial")+"").trim().equals(""))&&(!(vo.get("horaFinal")+"").trim().equals("")))
			{
				cadena+=" AND ( (s.fecha_solicitud > '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"') or (s.fecha_solicitud = '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and s.hora_solicitud >= '"+vo.get("horaInicial")+"') ) " +
				"and ((s.fecha_solicitud < '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"') or (s.fecha_solicitud = '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"' and s.hora_solicitud <= '"+vo.get("horaFinal")+"')) ";
			}
			if(!(vo.get("cuenta")+"").trim().equals(""))
			{
				cadena+=" AND c.id='"+ vo.get("cuenta")+"' ";
			}
			if(vo.containsKey("solicitudes") && !(vo.get("solicitudes")+"").equals(""))
			{
				cadena+=" AND s.numero_solicitud IN ("+vo.get("solicitudes")+") ";
			}
			cadena+=" order by s.fecha_solicitud,s.hora_solicitud ";

			logger.info("solicitudes - "+vo.get("solicitudes"));
			logger.info("\nconsultarSolicitudesMedicamentoInsumos-->"+cadena);
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(vo.get("ingreso")+""));
			mapa =UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			for(int i=0;i<Integer.parseInt(mapa.get("numRegistros")+"");i++)
			{
				String cadenaTemp="";
				cadenaTemp="SELECT " +
						"ds.articulo as articulo, " +
						"getdescarticulosincodigo(ds.articulo) as medicamento, " +
						"ds.dosis," +
						"uxa.unidad_medida as unidadmedida, " +
						"ds.frecuencia ||' ' ||ds.tipo_frecuencia as frecuencia," +
						"ds.via," +
						"ds.cantidad," +
						"ds.observaciones," +
						"case when sa.fecha is null then '' else ''||to_char(sa.fecha,'dd/mm/yyyy') end as fechasuspencion," +
						"sa.hora as horasuspencion," +
						"sa.motivo as motivosuspencion," +
						"sa.codigo_medico as medicosuspencion," +
						"case when na.es_pos='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' then 'true' else 'false' end as nopos " +
						"from " +
						"detalle_solicitudes  ds " +
						"left outer join unidosis_x_articulo uxa on (ds.UNIDOSIS_ARTICULO=uxa.codigo)" +
						"left outer join suspension_articulo sa on(sa.numero_solicitud=ds.numero_solicitud and sa.articulo=ds.articulo)  " +
						"inner join articulo a on(a.codigo=ds.articulo) " +
						"inner join naturaleza_articulo na on(na.acronimo=a.naturaleza and a.institucion=na.institucion) " +
						"inner join unidad_medida um on(um.acronimo=a.unidad_medida) "+
						"where na.es_medicamento='"+ConstantesBD.acronimoSi+"' and ds.numero_solicitud='"+mapa.get("numerosolicitud_"+i)+"'  and ds.articulo_principal is null";
				logger.info(cadenaTemp);
				ps= new PreparedStatementDecorator(con.prepareStatement(cadenaTemp,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				HashMap mapaMedicamentos=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
				for(int k=0;k<Integer.parseInt(mapaMedicamentos.get("numRegistros")+"");k++)
				{
					if(!UtilidadTexto.getBoolean(mapaMedicamentos.get("nopos_"+k)+""))
					{
						String cadAtributos="SELECT a.nombre as atributo,das.descripcion as valor  from desc_atributos_solicitud das inner join atributos_solicitud a on(das.atributo=a.codigo) where das.numero_solicitud='"+mapa.get("numerosolicitud_"+i)+"' and das.articulo='"+mapaMedicamentos.get("articulo_"+k)+"'";
						logger.info(cadAtributos);
						ps= new PreparedStatementDecorator(con.prepareStatement(cadAtributos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						mapaMedicamentos.put("atributosnopos_"+k,UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())));
						logger.info(mapaMedicamentos.get("atributosnopos_"+k));
					}
				}
				
				mapa.put("medicamentos_"+i,mapaMedicamentos);

				cadenaTemp="SELECT ds.articulo as articulo,a.descripcion as insumo,ds.cantidad as cantidad from detalle_solicitudes  ds  inner join articulo a on(a.codigo=ds.articulo) inner join naturaleza_articulo na on(na.acronimo=a.naturaleza and na.institucion=a.institucion)  where na.es_medicamento='"+ConstantesBD.acronimoNo+"' and ds.numero_solicitud='"+mapa.get("numerosolicitud_"+i)+"'";
				ps= new PreparedStatementDecorator(con.prepareStatement(cadenaTemp,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				mapa.put("insumos_"+i,UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())));
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap consultarSolicitudesMedicamentoInsumos(int numeroSolicitud)
	{
		//FIXME EJEMPLO NUMERO SOLICITUD 
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		Connection con=UtilidadBD.abrirConexion();
		String cadena=consultaSolicitudesMedicamentosInsumos+"  and s.numero_solicitud=?";
		try
		{
			
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
			mapa =UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			for(int i=0;i<Integer.parseInt(mapa.get("numRegistros")+"");i++)
			{
				String cadenaTemp="";
				cadenaTemp="SELECT " +
						"ds.articulo as articulo, " +
						"  getcodigocumarticulo(ds.articulo) as codigocum ,  " +
						"  a.naturaleza as naturaleza, " +
						"  a.codigo_interfaz as codigo_interfaz, " +
						"getdescarticulosincodigo(ds.articulo) as medicamento, " +
						"ds.dosis," +
						"uxa.unidad_medida as unidadmedida, " +
						"ds.frecuencia ||' ' ||ds.tipo_frecuencia as frecuencia," +
						"ds.via," +
						"ds.cantidad," +
						"ds.observaciones," +
						"case when sa.fecha is null then '' else ''||to_char(sa.fecha,'dd/mm/yyyy') end as fechasuspencion," +
						"sa.hora as horasuspencion," +
						"sa.motivo as motivosuspencion," +
						"sa.codigo_medico as medicosuspencion," +
						"case when na.es_pos='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' then 'true' else 'false' end as nopos " +
						"from " +
						"detalle_solicitudes  ds " +
						"left outer join unidosis_x_articulo uxa on (ds.UNIDOSIS_ARTICULO=uxa.codigo)" +
						"left outer join suspension_articulo sa on(sa.numero_solicitud=ds.numero_solicitud and sa.articulo=ds.articulo)  " +
						"inner join articulo a on(a.codigo=ds.articulo) " +
						"inner join naturaleza_articulo na on(na.acronimo=a.naturaleza and a.institucion=na.institucion) " +
						"inner join unidad_medida um on(um.acronimo=a.unidad_medida) "+
						"where na.es_medicamento='"+ConstantesBD.acronimoSi+"' and ds.numero_solicitud='"+mapa.get("numerosolicitud_"+i)+"'  and ds.articulo_principal is null";
				logger.info(" Cadena para consultar articulos --> " + cadenaTemp);
				ps= new PreparedStatementDecorator(con.prepareStatement(cadenaTemp,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				HashMap mapaMedicamentos=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
				for(int k=0;k<Integer.parseInt(mapaMedicamentos.get("numRegistros")+"");k++)
				{
					if(!UtilidadTexto.getBoolean(mapaMedicamentos.get("nopos_"+k)+""))
					{
						String cadAtributos="SELECT a.nombre as atributo,das.descripcion as valor  from desc_atributos_solicitud das inner join atributos_solicitud a on(das.atributo=a.codigo) where das.numero_solicitud='"+mapa.get("numerosolicitud_"+i)+"' and das.articulo='"+mapaMedicamentos.get("articulo_"+k)+"'";
						logger.info(cadAtributos);
						ps= new PreparedStatementDecorator(con.prepareStatement(cadAtributos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						mapaMedicamentos.put("atributosnopos_"+k,UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())));
						logger.info(mapaMedicamentos.get("atributosnopos_"+k));
					}
				}
				
				mapa.put("medicamentos_"+i,mapaMedicamentos);

				cadenaTemp="SELECT ds.articulo as articulo,a.descripcion as insumo,ds.cantidad as cantidad from detalle_solicitudes  ds  inner join articulo a on(a.codigo=ds.articulo) inner join naturaleza_articulo na on(na.acronimo=a.naturaleza and na.institucion=a.institucion)  where na.es_medicamento='"+ConstantesBD.acronimoNo+"' and ds.numero_solicitud='"+mapa.get("numerosolicitud_"+i)+"'";
				ps= new PreparedStatementDecorator(con.prepareStatement(cadenaTemp,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				mapa.put("insumos_"+i,UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())));
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			UtilidadBD.closeConnection(con);
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap consultarSolicitudesProcedimientos(Connection con, HashMap vo)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		String cadena=consultaSolicitudesProcedimientos+"  and c.id_ingreso=?";
		try
		{
			if((!(vo.get("fechaInicial")+"").trim().equals(""))&&(!(vo.get("fechaFinal")+"").trim().equals("")))
			{
				cadena+=" AND s.fecha_solicitud  between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"'";
			}
			if((!(vo.get("horaInicial")+"").trim().equals(""))&&(!(vo.get("horaFinal")+"").trim().equals("")))
			{
				cadena+=" AND ( (s.fecha_solicitud > '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"') or (s.fecha_solicitud = '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and s.hora_solicitud >= '"+vo.get("horaInicial")+"') ) " +
				"and ((s.fecha_solicitud < '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"') or (s.fecha_solicitud = '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"' and s.hora_solicitud <= '"+vo.get("horaFinal")+"')) ";
			}
			if(!(vo.get("cuenta")+"").trim().equals(""))
			{
				cadena+=" AND c.id='"+ vo.get("cuenta")+"'";
			}
			
			if(vo.containsKey("solicitudes") && !(vo.get("solicitudes")+"").equals(""))
			{
				cadena+=" AND s.numero_solicitud IN ("+ vo.get("solicitudes")+") ";
			}
			
			cadena+=" order by s.fecha_solicitud,s.hora_solicitud ";
			logger.info("Cadena de consulta: "+cadena);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1, vo.get("ingreso"));
			mapa =UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap consultarSolicitudesProcedimientos(int numeroSolicitud)throws Exception
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		String cadena=consultaSolicitudesProcedimientos+"  and s.numero_solicitud=?";
		Connection con = UtilidadBD.abrirConexion();
		try
		{
			logger.info("Cadena de consulta: "+cadena);
			if(con!=null && !con.isClosed()){
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
			mapa =UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			}else {
				logger.error("Conexion Nula en consulta de solicitudes de procedimientos procedimientos ");
				throw new Exception("Se presento un problema con la conexion a la Base de datos en la consulta Solicitudes de  procedimientos");
			}
		}
		catch(SQLException e)
		{
			logger.error("Error Consultando SOlicitudes de procedimiento");
			throw new Exception("Error Consultando Solicitudes de procedimiento:"+e.getMessage());
		}
		finally
		{
			UtilidadBD.closeConnection(con);
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap consultarSolicitudesInterconsultas(Connection con, HashMap vo)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		String cadena=consultaSolicitudesInterconsultas+" and c.id_ingreso=?";
		try
		{
			if((!(vo.get("fechaInicial")+"").trim().equals(""))&&(!(vo.get("fechaFinal")+"").trim().equals("")))
			{
				cadena+=" AND s.fecha_solicitud  between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"'";
			}
			if((!(vo.get("horaInicial")+"").trim().equals(""))&&(!(vo.get("horaFinal")+"").trim().equals("")))
			{
				cadena+=" AND ( (s.fecha_solicitud > '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"') or (s.fecha_solicitud = '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and s.hora_solicitud >= '"+vo.get("horaInicial")+"') ) " +
				"and ((s.fecha_solicitud < '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"') or (s.fecha_solicitud = '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"' and s.hora_solicitud <= '"+vo.get("horaFinal")+"')) ";
			}
			if(!(vo.get("cuenta")+"").trim().equals(""))
			{
				cadena+=" AND c.id='"+ vo.get("cuenta")+"'";
			}
			cadena+=" order by s.fecha_solicitud,s.hora_solicitud ";
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1, vo.get("ingreso"));
			mapa =UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap consultarSolicitudesInterconsultas(int numeroSolicitud)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		Connection con=UtilidadBD.abrirConexion();
		String cadena=consultaSolicitudesInterconsultas+" and s.numero_solicitud=?";
		try
		{
			cadena+=" order by s.fecha_solicitud,s.hora_solicitud ";
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,numeroSolicitud);
			mapa =UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			UtilidadBD.closeConnection(con);
		}
		return mapa;
	}
	
	
	
	/**
	 * @param codigoCuenta
	 * @return
	 */
	public static List<DtoObservacionesGeneralesOrdenesMedicas> consultarObservacionesOrdenMedica( int codigoCuenta) {
		Connection con=null;
		List<DtoObservacionesGeneralesOrdenesMedicas> listaObservaciones = new ArrayList<DtoObservacionesGeneralesOrdenesMedicas>();
		try{
		 con=UtilidadBD.abrirConexion();
		
		 
		String consulta = " SELECT  dom.fecha_generacion, "+
			" dom.fecha_orden, "+
			" dom.observacion, "+
			" dom.datos_medico "+
			" FROM ordenes_medicas om "+
			" JOIN ordenes.detalle_observacion_orden_med dom "+
			" ON (om.codigo  = dom.id_orden_medica) "+
			" INNER JOIN hoja_neurologica_orden_m hnom "+
			" ON(hnom.orden_medica=om.codigo) "+
			" INNER JOIN encabezado_histo_orden_m ehom "+
			" ON(ehom.orden_medica=om.codigo) "+
			" INNER JOIN cuentas c "+
			" ON(c.id          =om.cuenta) "+
			" WHERE ehom.codigo=? "+
			" AND dom.encabezado_histo_orden=?"+
			" ORDER BY ehom.fecha_orden, "+
			" ehom.hora_orden";
		PreparedStatement ps = con.prepareStatement(consulta);
		ps.setInt(1, codigoCuenta);
		ps.setInt(2, codigoCuenta);
		ResultSet rs = ps.executeQuery();
		
		while (rs.next()) {
			DtoObservacionesGeneralesOrdenesMedicas dtoObservacionesGeneralesOrdenesMedicas= new DtoObservacionesGeneralesOrdenesMedicas();
			dtoObservacionesGeneralesOrdenesMedicas.setFechaGeneracion(rs.getString("fecha_generacion"));
			dtoObservacionesGeneralesOrdenesMedicas.setFechaOrden(rs.getString("fecha_orden"));
			dtoObservacionesGeneralesOrdenesMedicas.setObservaciones(rs.getString("observacion"));
			dtoObservacionesGeneralesOrdenesMedicas.setDatosMedico(rs.getString("datos_medico"));
			listaObservaciones.add(dtoObservacionesGeneralesOrdenesMedicas);
		}
		
		rs.close();
		ps.close();
		
	}
	catch(SQLException e)
	{
		
	}
	finally
	{
		UtilidadBD.closeConnection(con);
	}
	return listaObservaciones;
		
		
	}
	
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap consultarOrdenMedica(int codigoEncabezado,Integer ingreso,Integer cuenta)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		String cadena=consultaOrdenesMedicas +" where ehom.codigo=?";
		Connection con=UtilidadBD.abrirConexion();
		try
		{
			
			cadena+=" order by ehom.fecha_orden,ehom.hora_orden ";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoEncabezado);
			mapa =UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			
			for(int i=0;i<Integer.parseInt(mapa.get("numRegistros")+"");i++)
			{
				String cadenaTemp="";
				
				
				

				
				
				//consulta de ordenes tipos monitoreo.
				cadenaTemp="SELECT tm.nombre as tipomonitoreo from orden_tipo_monitoreo otm inner join tipo_monitoreo tm on(tm.codigo=otm.tipo_monitoreo) where otm.codigo_histo_encabezado='"+mapa.get("codigoencabezado_"+i)+"'";
				ps= new PreparedStatementDecorator(con.prepareStatement(cadenaTemp,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				mapa.put("tiposmonitoreo_"+i,UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())));

				
				
				//consulta de soportes respiratorios
				//cadenaTemp="SELECT osr.cantidad as cantidad,ee.descripcion as equipo from orden_soporte_respira osr inner join equipo_elemento_cc_inst eeci on(eeci.codigo=osr.equipo_elemento_cc_inst) inner join equipo_elemento ee on (ee.codigo=eeci.equipo_elemento) where osr.codigo_histo_enca='"+mapa.get("codigoencabezado_"+i)+"'";
				cadenaTemp="SELECT osr.cantidad      AS cantidad, ee.descripcion         AS equipo, CASE WHEN osr.oxigeno_terapia = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'Si' ELSE 'No'  END AS oxigeno, " +
						" osr.descripcion AS descripcionSoporte FROM orden_soporte_respira osr LEFT JOIN equipo_elemento_cc_inst eeci ON(eeci.codigo=osr.equipo_elemento_cc_inst)" +
						" LEFT JOIN equipo_elemento ee ON (ee.codigo =eeci.equipo_elemento) , ENCABEZADO_HISTO_ORDEN_M eho,  ORDENES_MEDICAS om WHERE osr.codigo_histo_enca='"+mapa.get("codigoencabezado_"+i)+"'" +
						" AND osr.CODIGO_HISTO_ENCA  =eho.CODIGO AND eho.orden_medica       =om.CODIGO " +
						" AND (((osr.oxigeno_terapia IS NULL OR osr.oxigeno_terapia = "+ValoresPorDefecto.getValorFalseParaConsultas()+" ) AND osr.descripcion IS NOT NULL) OR ( osr.oxigeno_terapia IS NOT NULL AND osr.oxigeno_terapia <> "+ValoresPorDefecto.getValorFalseParaConsultas()+" )) ";			
				
				ps= new PreparedStatementDecorator(con.prepareStatement(cadenaTemp,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				mapa.put("soporterespiratorio_"+i,UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())));
				
				cadenaTemp="" +
							"SELECT " +
								" tce.descripcion AS cuidado, " +
								" COALESCE(' ',dce.descripcion)      AS descripcion,  " +
								" fce.frecuencia       AS frecuencia, " +
								" fce.periodo          AS periodo," +
								" fce.tipo_frecuencia  AS tipoFrecuencia " +
							" FROM detalle_cuidado_enfer dce " +
							" INNER JOIN cuidado_enfer_cc_inst cecci ON (dce.cuidado_enfer_cc_inst=cecci.codigo) " +
							" INNER JOIN tipo_cuidado_enfermeria tce ON(tce.codigo =cecci.cuidado_enfermeria), " +
							" FREC_CUIDADOS_ENFER fce " +
							" WHERE " +
									" dce.presenta    ="+ValoresPorDefecto.getValorTrueParaConsultas()+" " +
									" AND tce.descripcion IS NOT NULL "+
									" AND fce.frecuencia IS NOT NULL"+
									" AND fce.periodo IS NOT NULL"+
									" AND  fce.tipo_frecuencia IS NOT NULL"+
									" AND dce.cod_histo_enca='"+mapa.get("codigoencabezado_"+i)+"' " +
									" AND cecci.codigo=fce.CUIDADO_ENFER_CC_INST "+
									" AND fce.ingreso=+"+String.valueOf(ingreso!=null?ingreso:new Integer (0));
				ps= new PreparedStatementDecorator(con.prepareStatement(cadenaTemp,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				mapa.put("cuidadosespeciales_"+i,UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())));
			
				
				
				
				
				
				ArrayList<DtoDietaHistoriaClinica> dietas = obtenerDietas(con,Integer.valueOf( String.valueOf(mapa.get("codigoencabezado_"+i))), ingreso, cuenta);
				mapa.put("dietasFull_"+i,dietas);
				
				
				
				
				//consulta de la seccion de cuidados especiales.
				cadenaTemp="SELECT od.codigo_histo_enca as codhisenc,case when od.via_oral="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'Si' else 'No' end  as viaoral,case when od.finalizar_dieta="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'Si' else 'No' end  as suspendido, od.suspendido as suspender, od.observaciones as descripcion  from orden_dieta od where od.codigo_histo_enca='"+mapa.get("codigoencabezado_"+i)+"'";
				ps= new PreparedStatementDecorator(con.prepareStatement(cadenaTemp,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				HashMap mapaTemp=new HashMap();
				mapaTemp.put("numRegistros", "0");
				mapaTemp=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
				for(int j=0;j<Integer.parseInt(mapaTemp.get("numRegistros")+"");j++)
				{
					String cadenaTemp1="SELECT tno.nombre from orden_nutricion_oral ono inner join nutricion_oral_cc_inst noci on(noci.codigo=ono.nutricion_oral_cc_inst) inner join tipo_nutricion_oral tno on(tno.codigo=noci.nutricion_oral) where codigo_historico_dieta ='"+mapaTemp.get("codhisenc_"+j)+"'";
					ps= new PreparedStatementDecorator(con.prepareStatement(cadenaTemp1,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					//para tomar las dietas
					ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
					String tiposDietas="";
					int cont=0;
					while(rs.next())
					{
						if(cont>0)
							tiposDietas+=",";
						tiposDietas+=rs.getString(1);
						cont++;
					}
					mapaTemp.put("tiposdieta_"+j,tiposDietas);
				}
				mapa.put("dieta_"+i,mapaTemp);
				
				
				//consulta mezclas parenterales,enterales,homoderivados
				cadenaTemp="SELECT od.codigo_histo_enca as codhisenc,m.codigo||' '||m.nombre as mezcla,tm.nombre as tipo,case when od.finaliza_sol="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'Si' else 'No' end  as  suspendida ,od.volumen_total as volumentotal,od.velocidad_infusion as velocidadinfusion, od.dosificacion as dosificacion from orden_dieta od inner join mezcla m on (od.mezcla=m.consecutivo) inner join tipo_mezcla tm on(tm.codigo=m.cod_tipo_mezcla) where codigo_histo_enca='"+mapa.get("codigoencabezado_"+i)+"'";
				ps= new PreparedStatementDecorator(con.prepareStatement(cadenaTemp,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				mapaTemp=new HashMap();
				mapaTemp.put("numRegistros", "0");
				mapaTemp=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
				for(int j=0;j<Integer.parseInt(mapaTemp.get("numRegistros")+"");j++)
				{
					String cadenaTemp1="SELECT getdescarticulosincodigo(articulo) as articulo,volumen from orden_nutricion_parente where codigo_historico_dieta='"+mapaTemp.get("codhisenc_"+j)+"'";
					ps= new PreparedStatementDecorator(con.prepareStatement(cadenaTemp1,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					mapaTemp.put("articulos_"+j,UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())));
				}
				mapa.put("mezcla_"+i,mapaTemp);
				
				//Consulta de la prescripcion dialisis
				mapa.put("prescripcionDialisis_"+i, OrdenMedica.getPrescripcionDialisis(con, mapa.get("codigoencabezado_"+i).toString()));
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			UtilidadBD.closeConnection(con);
		}
		return mapa;
	}

	
	
	public static ArrayList<DtoDietaHistoriaClinica> obtenerDietas(Connection con,Integer codigoEncabezado,Integer ingreso,Integer numeroCuenta) throws SQLException{
		ArrayList<DtoDietaHistoriaClinica> listaDtoDietas= new ArrayList<DtoDietaHistoriaClinica>();

		String consulta="SELECT   * "+
		" FROM "+
		" (SELECT od.codigo_histo_enca                 AS codigo, "+
		" od.via_oral                                AS via_oral, "+
		" od.finalizar_dieta                         AS finalizar_dieta, "+
		" tno.nombre                                 AS nutricion_oral, "+
		" TO_CHAR(enc.fecha_orden, 'DD/MM/YYYY')     AS fecha_orden, "+
		"  enc.hora_orden                             AS hora_orden, "+
		" TO_CHAR(enc.fecha_grabacion, 'DD/MM/YYYY') AS fecha_grabacion, "+
		" enc.hora_grabacion                         AS hora_grabacion, "+
		" per.primer_nombre "+
		" || ' ' "+
		" || per.segundo_nombre "+
		" ||' ' "+
		" || per.primer_apellido "+
		" ||' ' "+
		" || per.segundo_apellido AS medico  " +
		"  ,descripcion_dieta_oral as descripcion "+
		" FROM orden_nutricion_oral ono "+
		" INNER JOIN nutricion_oral_cc_inst noci "+
		" ON (ono.nutricion_oral_cc_inst=noci.codigo) "+
		" INNER JOIN tipo_nutricion_oral tno "+
		" ON (noci.nutricion_oral=tno.codigo) "+
		" INNER JOIN orden_dieta od "+
		" ON (od.codigo_histo_enca=ono.codigo_historico_dieta) "+
		" INNER JOIN encabezado_histo_orden_m enc "+
		" ON (enc.codigo = od.codigo_histo_enca) "+
		" INNER JOIN ordenes_medicas om "+
		" ON (enc.orden_medica=om.codigo) "+
		" INNER JOIN usuarios us "+
		" ON (enc.login=us.login) "+
		"  INNER JOIN personas per "+
		" ON (us.codigo_persona=per.codigo) "+
		" WHERE om.cuenta      = ? "+
		" AND noci.activo      = ? "+
		" UNION ALL "+
		" SELECT od.codigo_histo_enca                  AS codigo, "+
		"  od.via_oral                                AS via_oral, "+
		"  od.finalizar_dieta                         AS finalizar_dieta, "+
		" ono.descripcion                            AS nutricion_oral, "+
		" TO_CHAR(enc.fecha_orden, 'DD/MM/YYYY')     AS fecha_orden, "+
		"  enc.hora_orden                             AS hora_orden, "+
		" TO_CHAR(enc.fecha_grabacion, 'DD/MM/YYYY') AS fecha_grabacion, "+
		" enc.hora_grabacion                         AS hora_grabacion, "+
		" per.primer_nombre "+
		" || ' ' "+
		" || per.segundo_nombre "+
		" ||' ' "+
		"  || per.primer_apellido "+
		"  ||' ' "+
		"  || per.segundo_apellido AS medico  " +
		"  ,descripcion_dieta_oral as descripcion "+
		" FROM detalle_otro_nutri_oral don "+
		" INNER JOIN otro_nutricion_oral ono "+
		" ON (ono.codigo=don.otro_nutricion_oral) "+
		" INNER JOIN orden_dieta od "+
		"  ON (od.codigo_histo_enca=don.codigo_historico_dieta) "+
		" INNER JOIN encabezado_histo_orden_m enc "+
		" ON (enc.codigo = od.codigo_histo_enca) "+
		" INNER JOIN ordenes_medicas om "+
		" ON (enc.orden_medica=om.codigo) "+
		" INNER JOIN usuarios us "+
		" ON (enc.login=us.login) "+
		" INNER JOIN personas per "+
		" ON (us.codigo_persona=per.codigo) "+
		" WHERE om.cuenta      = ? "+
		" UNION "+
		" SELECT od.codigo_histo_enca                  AS codigo, "+
		"  od.via_oral                                AS via_oral, "+
		" od.finalizar_dieta                         AS finalizar_dieta, "+
		" ''                                         AS nutricion_oral, "+
		" TO_CHAR(enc.fecha_orden, 'DD/MM/YYYY')     AS fecha_orden, "+
		" enc.hora_orden                             AS hora_orden, "+
		" TO_CHAR(enc.fecha_grabacion, 'DD/MM/YYYY') AS fecha_grabacion, "+
		"  enc.hora_grabacion                         AS hora_grabacion, "+
		"  per.primer_nombre "+
		" || ' ' "+
		" || per.segundo_nombre "+
		"  ||' ' "+
		" || per.primer_apellido "+
		" ||' ' "+
		" || per.segundo_apellido AS medico  " +
		"  ,descripcion_dieta_oral as descripcion "+
		" FROM orden_dieta od "+
		" INNER JOIN encabezado_histo_orden_m enc "+
		" ON (enc.codigo = od.codigo_histo_enca) "+
		" INNER JOIN ordenes_medicas om "+
		" ON (enc.orden_medica=om.codigo) "+
		" INNER JOIN usuarios us "+
		" ON (enc.login=us.login) "+
		" INNER JOIN personas per "+
		" ON (us.codigo_persona   =per.codigo) "+
		"  WHERE od.finalizar_dieta=? "+
		" AND om.cuenta           = ? "+
		"  )x "+
		"  where x.codigo=? "+
		" ORDER BY fecha_grabacion DESC, "+
		" hora_grabacion DESC"; 

		PreparedStatement pst;
		
			pst = con.prepareStatement(consulta);

			pst.setInt(1, numeroCuenta);
			pst.setBoolean(2, true);
			pst.setInt(3, numeroCuenta);
			pst.setBoolean(4, true);
			pst.setInt(5, numeroCuenta);
			pst.setInt(6, codigoEncabezado);

			ResultSet rs = pst.executeQuery();

			Integer codigo=new Integer(0);
			String dietas="";
			String desc="";

			//ArrayList<DtoDietaHistoriaClinica> listaDtoDietas= new ArrayList<DtoDietaHistoriaClinica>();
			while (rs.next()) {
				DtoDietaHistoriaClinica tmp = new DtoDietaHistoriaClinica();
				desc=rs.getString("DESCRIPCION");

				codigo=rs.getInt("CODIGO");
				if(dietas.equals("")){
					dietas=rs.getString("NUTRICION_ORAL");
				}else{
					dietas+=" , "+rs.getString("NUTRICION_ORAL");
				}

				tmp.setCodigo(codigo);
				tmp.setNombresDieta(dietas);
				tmp.setDescripcion(desc);
				listaDtoDietas.add(tmp);
			}


			for (int i = 0; i < listaDtoDietas.size(); i++) {


				/*
				 * @hevfacma
				 * @Caso 11963
				 * Se Realiza Ajuste en el select, dado que en PostgreSQL genera error
				 * Por que los campos finalizar_dieta y via_oral son tipo boolean
				 * y estaba validando con 1 y 0
				 */
				String consultaEstadosDieta = "SELECT od.codigo_histo_enca AS codhisenc, "+
				/*" CASE "+
				" WHEN od.via_oral=1 "+
				" THEN 'Si' "+
				" ELSE 'No' "+
				" END AS viaoral, "+
				" CASE "+
				" WHEN od.finalizar_dieta=1 "+
				" THEN 'Si' "+
				" ELSE 'No' "+
				" END              AS suspendido, "+*/
				" od.via_oral AS viaoral, "+
				" od.finalizar_dieta AS suspendido, "+
				" od.suspendido    AS suspender, "+
				" od.observaciones AS descripcion "+
				" FROM orden_dieta od "+
				" WHERE od.codigo_histo_enca=?"; 

				pst = con.prepareStatement(consultaEstadosDieta);
				pst.setString(1,String.valueOf( listaDtoDietas.get(i).getCodigo()));
				rs=pst.executeQuery();
				if(rs.next()){
					//listaDtoDietas.get(i).setViaOral(rs.getString("VIAORAL"));
					//listaDtoDietas.get(i).setSuspendido(rs.getString("SUSPENDIDO"));
					
					if(UtilidadTexto.getBoolean(rs.getString("VIAORAL"))){
						listaDtoDietas.get(i).setViaOral("Si");
					} else {
						listaDtoDietas.get(i).setViaOral("No");
					}
					
					if(UtilidadTexto.getBoolean(rs.getString("SUSPENDIDO"))){
						listaDtoDietas.get(i).setSuspendido("Si");
					} else {
						listaDtoDietas.get(i).setSuspendido("No");
					}
					
					listaDtoDietas.get(i).setSuspender(rs.getString("SUSPENDER"));
					
					/*
					 * Fin Caso 11963
					 */
				}

			}

		return listaDtoDietas;

	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap consultarOrdenMedica(Connection con, HashMap vo,Integer Ingreso)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		String  listaObs = new String();
		String cadena=consultaOrdenesMedicas+" where c.id_ingreso=? ";
		try
		{
			if((!(vo.get("fechaInicial")+"").trim().equals(""))&&(!(vo.get("fechaFinal")+"").trim().equals("")))
			{
				cadena+=" AND ehom.fecha_orden  between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"'";
			}
			if((!(vo.get("horaInicial")+"").trim().equals(""))&&(!(vo.get("horaFinal")+"").trim().equals("")))
			{
				cadena+=" AND ( (ehom.fecha_orden > '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"') or (ehom.fecha_orden = '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and ehom.hora_orden >= '"+vo.get("horaInicial")+"') ) " +
				"and ((ehom.fecha_orden < '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"') or (ehom.fecha_orden = '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"' and ehom.hora_orden <= '"+vo.get("horaFinal")+"')) ";
				
			}
			if(!(vo.get("cuenta")+"").trim().equals(""))
			{
				cadena+=" AND c.id='"+ vo.get("cuenta")+"'";
			}
			cadena+=" order by ehom.fecha_orden,ehom.hora_orden, ehom.codigo  ";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1, vo.get("ingreso"));
			mapa =UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			
			String consultaObservacionesGenerales=" SELECT dom.fecha_generacion "+
			" , "+
			"  dom.fecha_orden "+
			" , "+
			"  dom.observacion "+
			" ,"+
			" dom.datos_medico observacionesgenerales "+
			" FROM ordenes_medicas om "+
			" JOIN ordenes.detalle_observacion_orden_med dom "+
			" ON (om.codigo = dom.id_orden_medica) "+
			" INNER JOIN cuentas c "+
			" ON(c.id           =om.cuenta)  WHERE c.id_ingreso=? ";
			if(!(vo.get("cuenta")+"").trim().equals(""))
			{
				consultaObservacionesGenerales+=" AND c.id='"+ vo.get("cuenta")+"'";
			}
			consultaObservacionesGenerales+=	" ORDER BY dom.id ASC ";

			PreparedStatement psObservaciones = con.prepareStatement(consultaObservacionesGenerales);
			psObservaciones.setInt(1,Integer.valueOf(String.valueOf(vo.get("ingreso"))) );
			
			ResultSet rsObservaciones = psObservaciones.executeQuery();
			while (rsObservaciones.next()) {
				listaObs+="\n\n\n"+(rsObservaciones.getString("fecha_generacion")+"\n"+  rsObservaciones.getString("fecha_orden")+
						"\n"+rsObservaciones.getString("observacion")+"\n"+
						rsObservaciones.getString("observacionesgenerales"));

			}
			
			mapa.put("observacionesgenerales_"+0,listaObs);
			rsObservaciones.close();
			psObservaciones.close();
			
			for(int i=0;i<Integer.parseInt(mapa.get("numRegistros")+"");i++)
			{
				String cadenaTemp="";
				
				
				//consulta de ordenes tipos monitoreo.
				cadenaTemp="SELECT tm.nombre as tipomonitoreo from orden_tipo_monitoreo otm inner join tipo_monitoreo tm on(tm.codigo=otm.tipo_monitoreo) where otm.codigo_histo_encabezado='"+mapa.get("codigoencabezado_"+i)+"'";
				ps= new PreparedStatementDecorator(con.prepareStatement(cadenaTemp,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				mapa.put("tiposmonitoreo_"+i,UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())));

				
				//consulta de soportes respiratorios
				//cadenaTemp="SELECT osr.cantidad as cantidad,ee.descripcion as equipo from orden_soporte_respira osr inner join equipo_elemento_cc_inst eeci on(eeci.codigo=osr.equipo_elemento_cc_inst) inner join equipo_elemento ee on (ee.codigo=eeci.equipo_elemento) where osr.codigo_histo_enca='"+mapa.get("codigoencabezado_"+i)+"'";
				cadenaTemp="SELECT osr.cantidad      AS cantidad, ee.descripcion         AS equipo, osr.oxigeno_terapia    AS oxigeno, " +
						" osr.descripcion AS descripcionSoporte FROM orden_soporte_respira osr LEFT JOIN equipo_elemento_cc_inst eeci ON(eeci.codigo=osr.equipo_elemento_cc_inst)" +
						" LEFT JOIN equipo_elemento ee ON (ee.codigo =eeci.equipo_elemento) , ENCABEZADO_HISTO_ORDEN_M eho,  ORDENES_MEDICAS om WHERE osr.codigo_histo_enca='"+mapa.get("codigoencabezado_"+i)+"'" +
						" AND osr.CODIGO_HISTO_ENCA  =eho.CODIGO AND eho.orden_medica       =om.CODIGO " +
						" AND (((osr.oxigeno_terapia IS NULL OR osr.oxigeno_terapia = "+ValoresPorDefecto.getValorFalseParaConsultas()+" ) AND osr.descripcion IS NOT NULL) OR ( osr.oxigeno_terapia IS NOT NULL AND osr.oxigeno_terapia <> "+ValoresPorDefecto.getValorFalseParaConsultas()+" )) ";
				
				
				
				ps= new PreparedStatementDecorator(con.prepareStatement(cadenaTemp,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				mapa.put("soporterespiratorio_"+i,UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())));
				
				if(Ingreso==null){
				
				//consulta de la seccion de cuidados especiales.
				cadenaTemp="select tce.descripcion as cuidado,dce.descripcion as descripcion from detalle_cuidado_enfer dce inner join cuidado_enfer_cc_inst cecci on (dce.cuidado_enfer_cc_inst=cecci.codigo) inner join tipo_cuidado_enfermeria tce on(tce.codigo=cecci.cuidado_enfermeria) where dce.presenta="+ValoresPorDefecto.getValorTrueParaConsultas()+" and dce.cod_histo_enca='"+mapa.get("codigoencabezado_"+i)+"'";
				ps= new PreparedStatementDecorator(con.prepareStatement(cadenaTemp,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				mapa.put("cuidadosespeciales_"+i,UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())));
				}else{
					//consulta de la seccion de cuidados especiales.
					cadenaTemp="SELECT tce.descripcion AS cuidado, nvl(' ',dce.descripcion)      AS descripcion,  fce.frecuencia       AS frecuencia, " +
							" fce.periodo          AS periodo,fce.tipo_frecuencia  AS tipoFrecuencia FROM detalle_cuidado_enfer dce INNER JOIN cuidado_enfer_cc_inst cecci ON " +
							"(dce.cuidado_enfer_cc_inst=cecci.codigo) INNER JOIN tipo_cuidado_enfermeria tce ON(tce.codigo =cecci.cuidado_enfermeria)," +
							" FREC_CUIDADOS_ENFER fce WHERE dce.presenta    ="+ValoresPorDefecto.getValorTrueParaConsultas()+" " +
							"AND dce.cod_histo_enca='"+mapa.get("codigoencabezado_"+i)+"' AND cecci.codigo=fce.CUIDADO_ENFER_CC_INST AND fce.ingreso="+Ingreso;
					ps= new PreparedStatementDecorator(con.prepareStatement(cadenaTemp,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					mapa.put("cuidadosespeciales_"+i,UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())));
				
				}
				
				
				
				
				
				
				
				
				//consulta de la seccion de cuidados especiales.
				cadenaTemp="SELECT od.codigo_histo_enca as codhisenc,case when od.via_oral="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'Si' else 'No' end  as viaoral,case when od.finalizar_dieta="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'Si' else 'No' end  as suspendido, od.suspendido as suspender, od.observaciones as descripcion  from orden_dieta od where od.codigo_histo_enca='"+mapa.get("codigoencabezado_"+i)+"'";
				ps= new PreparedStatementDecorator(con.prepareStatement(cadenaTemp,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				HashMap mapaTemp=new HashMap();
				mapaTemp.put("numRegistros", "0");
				mapaTemp=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
				for(int j=0;j<Integer.parseInt(mapaTemp.get("numRegistros")+"");j++)
				{
					String cadenaTemp1="SELECT tno.nombre from orden_nutricion_oral ono inner join nutricion_oral_cc_inst noci on(noci.codigo=ono.nutricion_oral_cc_inst) inner join tipo_nutricion_oral tno on(tno.codigo=noci.nutricion_oral) where codigo_historico_dieta ='"+mapaTemp.get("codhisenc_"+j)+"'";
					ps= new PreparedStatementDecorator(con.prepareStatement(cadenaTemp1,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					//para tomar las dietas
					ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
					String tiposDietas="";
					int cont=0;
					while(rs.next())
					{
						if(cont>0)
							tiposDietas+=",";
						tiposDietas+=rs.getString(1);
						cont++;
					}
					mapaTemp.put("tiposdieta_"+j,tiposDietas);
				}
				mapa.put("dieta_"+i,mapaTemp);
				
				
				//consulta mezclas parenterales,enterales,homoderivados
				cadenaTemp="SELECT od.codigo_histo_enca as codhisenc,m.codigo||' '||m.nombre as mezcla,tm.nombre as tipo,case when od.finaliza_sol="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'Si' else 'No' end  as  suspendida ,od.volumen_total as volumentotal,od.velocidad_infusion as velocidadinfusion, od.dosificacion as dosificacion from orden_dieta od inner join mezcla m on (od.mezcla=m.consecutivo) inner join tipo_mezcla tm on(tm.codigo=m.cod_tipo_mezcla) where codigo_histo_enca='"+mapa.get("codigoencabezado_"+i)+"'";
				ps= new PreparedStatementDecorator(con.prepareStatement(cadenaTemp,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				mapaTemp=new HashMap();
				mapaTemp.put("numRegistros", "0");
				mapaTemp=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
				for(int j=0;j<Integer.parseInt(mapaTemp.get("numRegistros")+"");j++)
				{
					String cadenaTemp1="SELECT getdescarticulosincodigo(articulo) as articulo,volumen from orden_nutricion_parente where codigo_historico_dieta='"+mapaTemp.get("codhisenc_"+j)+"'";
					ps= new PreparedStatementDecorator(con.prepareStatement(cadenaTemp1,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					mapaTemp.put("articulos_"+j,UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())));
				}
				mapa.put("mezcla_"+i,mapaTemp);
				
				//Consulta de la prescripcion dialisis
				mapa.put("prescripcionDialisis_"+i, OrdenMedica.getPrescripcionDialisis(con, mapa.get("codigoencabezado_"+i).toString()));
			}
			
		} catch(SQLException e)	{
			Log4JManager.error(e);
		} catch (Exception e) {
			Log4JManager.error(e);
		}
		return mapa;
	}
	

	/**
	 * 
	 * @param con
	 * @param codigoEstado
	 * @param tipoSolicitud
	 * @param vo
	 * @return
	 */
	public static HashMap obtenerSolicitudesEstadoTipoFiltro(Connection con, int[] codigosEstados, int tipoSolicitud, HashMap vo)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		
		String codigosEstadosSeparadosPorComas="";
		for(int w=0; w<codigosEstados.length; w++)
		{
			codigosEstadosSeparadosPorComas+="'"+codigosEstados[w]+"',";
		}
		codigosEstadosSeparadosPorComas+="'-1'";
		
		String cadena="SELECT " +
			"s.numero_solicitud as solicitud, " +
			"CASE WHEN interpretacion IS NULL THEN '' ELSE interpretacion END as interpretacion," +
			"s.consecutivo_ordenes_medicas AS orden, " +
			"getestadosolhis(s.estado_historia_clinica) As estado, " +
			"to_char(s.fecha_solicitud,'DD/MM/YYYY') as fecha, " +
			"s.hora_solicitud AS hora " +
			" from solicitudes  s " ;
		
		if(tipoSolicitud==ConstantesBD.codigoTipoSolicitudInterconsulta||tipoSolicitud==ConstantesBD.codigoTipoSolicitudCita)
			cadena += "inner join valoraciones v ON(v.numero_solicitud=s.numero_solicitud) ";
		
		cadena += "inner join cuentas c on(c.id=s.cuenta) where ";
			
		
		if(tipoSolicitud==ConstantesBD.codigoTipoSolicitudInterconsulta)
		{
			cadena += " s.tipo = "+tipoSolicitud+"  and "; 
		}
		else if(tipoSolicitud==ConstantesBD.codigoTipoSolicitudCita)
		{
			cadena += " s.tipo = "+tipoSolicitud+"  and ";
		}
		else
			cadena += " s.tipo = "+tipoSolicitud+" and ";
		
		cadena += " s.estado_historia_clinica in ("+codigosEstadosSeparadosPorComas+") and c.id_ingreso=?";
		
		if(tipoSolicitud==ConstantesBD.codigoTipoSolicitudInterconsulta||tipoSolicitud==ConstantesBD.codigoTipoSolicitudCita)
		{
			if((!(vo.get("fechaInicial")+"").trim().equals(""))&&(!(vo.get("fechaFinal")+"").trim().equals("")))
			{
				cadena+=" AND v.fecha_valoracion  between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"'";
			}
			if((!(vo.get("horaInicial")+"").trim().equals(""))&&(!(vo.get("horaFinal")+"").trim().equals("")))
			{
				cadena+=" AND ( ( v.fecha_valoracion > '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"') or ( v.fecha_valoracion = '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and  v.hora_valoracion >= '"+vo.get("horaInicial")+"') ) " +
				"and (( v.fecha_valoracion < '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"') or ( v.fecha_valoracion= '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"' and  v.hora_valoracion <= '"+vo.get("horaFinal")+"')) ";
			}
		}
		else
		{
			if((!(vo.get("fechaInicial")+"").trim().equals(""))&&(!(vo.get("fechaFinal")+"").trim().equals("")))
			{
				cadena+=" AND s.fecha_solicitud  between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"'";
			}
			if((!(vo.get("horaInicial")+"").trim().equals(""))&&(!(vo.get("horaFinal")+"").trim().equals("")))
			{
				cadena+=" AND ( (s.fecha_solicitud > '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"') or (s.fecha_solicitud = '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and s.hora_solicitud >= '"+vo.get("horaInicial")+"') ) " +
				"and ((s.fecha_solicitud < '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"') or (s.fecha_solicitud = '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"' and s.hora_solicitud <= '"+vo.get("horaFinal")+"')) ";
			}
		}
		
		
		
		
		if(!(vo.get("cuenta")+"").trim().equals(""))
		{
			cadena+=" AND c.id='"+ vo.get("cuenta")+"'";
		}
		
		if(tipoSolicitud==ConstantesBD.codigoTipoSolicitudInterconsulta||tipoSolicitud==ConstantesBD.codigoTipoSolicitudCita)
			cadena+=" order by v.fecha_valoracion,v.hora_valoracion ";
		else
			cadena+=" order by s.fecha_solicitud,s.hora_solicitud ";
	
		
		
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1, vo.get("ingreso"));
			mapa =UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}

	
	/**
	 * evoluciones
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap obtenerEvoluciones(Connection con, HashMap vo)
	{
		String cadena=	"SELECT " +
				"evol.codigo," +
				"to_char(evol.fecha_evolucion,'DD/MM/YYYY') as fecha, " +
				"evol.hora_evolucion AS hora," +
				"getnombrepersona(evol.codigo_medico) as medico " +
						"FROM solicitudes sol " +
						"INNER JOIN evoluciones evol ON(evol.valoracion = sol.numero_solicitud) " +
						"INNER JOIN cuentas c ON (c.id=sol.cuenta)" +
						"WHERE c.id_ingreso= ?";
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		
		if((!(vo.get("fechaInicial")+"").trim().equals(""))&&(!(vo.get("fechaFinal")+"").trim().equals("")))
		{
			cadena+=" AND to_char(evol.fecha_evolucion,'yyyy-mm-dd')  between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"'";
		}
		if((!(vo.get("horaInicial")+"").trim().equals(""))&&(!(vo.get("horaFinal")+"").trim().equals("")))
		{
			cadena+=" AND ( (to_char(evol.fecha_evolucion,'yyyy-mm-dd') > '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"') or (to_char(evol.fecha_evolucion,'yyyy-mm-dd') = '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and evol.hora_evolucion >= '"+vo.get("horaInicial")+"') ) " +
			"and ((to_char(evol.fecha_evolucion,'yyyy-mm-dd') < '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"') or (to_char(evol.fecha_evolucion,'yyyy-mm-dd') = '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"' and evol.hora_evolucion <= '"+vo.get("horaFinal")+"')) ";
		}
		if(!(vo.get("cuenta")+"").trim().equals(""))
		{
			cadena+=" AND c.id='"+ vo.get("cuenta")+"'";
		}
		cadena+=" ORDER BY evol.fecha_evolucion, evol.hora_evolucion ";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1, vo.get("ingreso"));
			mapa =UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public static HashMap obtenerEncabezadoPaciente(Connection con, String idIngreso)
	{
		String cadena=	"SELECT " +
							"getnombrepersona(p.codigo) AS nombresApellidos, " +
							"p.tipo_identificacion AS tipoId, " +
							"p.numero_identificacion AS numeroId, " +
							"getnombreciudad(p.codigo_pais_id,p.codigo_depto_id, codigo_ciudad_id) as de, " +
							"to_char(fecha_nacimiento, 'DD/MM/YYYY') as fechaNacimiento," +
							"getedadsimple(fecha_nacimiento, '"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"') AS edad, " +							
							"getedad(fecha_nacimiento)||' a�os' AS edad_anios, " +
							"to_char(pac.fecha_muerte,'DD/MM/YYYY') as fecha_muerte, "+
							"to_char(EG.fecha_egreso,'DD/MM/YYYY') as fecha_egreso, "+
							"s.nombre AS nombresexo, " +
							"e.nombre AS estadoCivil, " +
							"o.nombre AS ocupacion, " +
							"p.direccion ||' '|| getnombreciudad(p.codigo_pais_vivienda,p.codigo_departamento_vivienda, p.codigo_ciudad_vivienda) as residencia, " +
							"p.telefono AS telefono, " +
							"conv.nombre AS convenio, " +
							"getnomtiporegimen(conv.tipo_regimen) AS tipoRegimen, " +
							"getnombretipoafiliado(sc.tipo_afiliado) AS tipoAfiliado " +
						"FROM " +
							"sub_cuentas sc  " +
							"INNER JOIN cuentas c ON(sc.ingreso = c.id_ingreso AND sc.nro_prioridad =1)" +
							"INNER JOIN ingresos ing ON (ing.id = c.id_ingreso) " +
							"LEFT JOIN egresos eg ON (eg.cuenta      = c.id) " +
							"INNER JOIN personas p ON (p.codigo=c.codigo_paciente) " +
							"INNER JOIN pacientes pac ON (p.codigo=pac.codigo_paciente) " +
							"INNER JOIN convenios conv ON (conv.codigo=sc.convenio) " +
							"INNER JOIN ocupaciones o ON (o.codigo=pac.ocupacion) " +
							"INNER JOIN estados_civiles e ON (e.acronimo=p.estado_civil) "+
							"INNER JOIN sexo s ON(s.codigo=p.sexo) " +
						"WHERE c.id_ingreso=?";
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		
		logger.info("valor de edad >> "+cadena+" "+idIngreso);
		PreparedStatementDecorator ps = null; 
		ResultSetDecorator rs = null; 
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setString(1, idIngreso);
			rs = new ResultSetDecorator(ps.executeQuery());
			mapa =UtilidadBD.cargarValueObject(rs);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		
		mapa.put("infoAcompananteMap", obtenerInfoAcompanante(con, idIngreso));
		
		return mapa;
	}	
	
	/**
	 * info del acompanante
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	private static HashMap obtenerInfoAcompanante(Connection con, String idIngreso)
	{
		String cadena=	"SELECT " +
							"rp.primer_apellido ||' '|| rp.primer_nombre AS nombreApellidoAcompanante, " +
							"rp.telefono AS telefonoAcomp, " +
							"rp.relacion_paciente AS parentesco " +
						"FROM " +
							"responsables_pacientes rp " +
							"INNER JOIN cuentas c ON(c.codigo_responsable_paciente=rp.codigo) " +
						"WHERE " +
							"c.id_ingreso=? "+ValoresPorDefecto.getValorLimit1()+" 1";
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null; 
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setString(1, idIngreso);
			rs = new ResultSetDecorator(ps.executeQuery());
			mapa =UtilidadBD.cargarValueObject(rs);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		return mapa;
	}

	/**
	 * Metodo para cargar informacion dados unos para metros de busqueda sobre una consulta especifica. 
	 * @param con
	 * @param mp
	 * @return
	 */
	public static HashMap consultarInformacion(Connection con, HashMap mp, int tipoBaseDatos) 
	{
		String consulta =""; 
		int nroConsulta = 0;
		
		if ( UtilidadCadena.noEsVacio(mp.get("nroConsulta")+"")  )
		{
			nroConsulta = UtilidadCadena.vInt(mp.get("nroConsulta")+"");
		}
		else { return (new HashMap()); }

		//-- Los parametros de Busqueda
		String fechaInicio = UtilidadCadena.vString(mp.get("fechaInicio")+""); 
		String horaInicio = UtilidadCadena.vString(mp.get("horaInicio")+""); 
		String fechaFinal = UtilidadCadena.vString(mp.get("fechaFinal")+""); 
		String horaFinal = UtilidadCadena.vString(mp.get("horaFinal")+"");
		


		switch (nroConsulta)
		{
			//-----	
			//----- Antecedentes Alergias. 	
			//-----	
			case 1:  //-- Consultar si existe antecedentes de alergias.
			{
				consulta = "  SELECT a.observaciones as observaciones 										" +
						   "		 FROM antecedentes_alergias a											" +
						   "		 		   WHERE a.codigo_paciente = " + mp.get("paciente");
			}
			break;
			case 2:  //-- Consultar TODA la inforamcion de antecedentes de alergias.
			{
				consulta = " SELECT * FROM (	" +
						   "		SELECT  ca.codigo as cod_categoria, ca.nombre as nom_categoria, ta.nombre as cod_alergia  " +
						   "				FROM ant_alergias_tipos aat 												 	  " +	
						   "					 INNER JOIN tipos_alergias ta ON ( aat.tipo_alergia = ta.codigo )		      " +	
					       "					 INNER JOIN categorias_alergias ca ON ( ta.categoria_alergia = ca.codigo )    " +
					       "						  WHERE aat.codigo_paciente = " + mp.get("paciente") +
					       "		UNION ALL 																				  " +
					       "		SELECT ca.codigo as cod_categoria, ca.nombre as nom_categoria, aao.nombre as alergia 	  " +
						   "			   FROM ant_alergias_otros aao  " +
						   "			        INNER JOIN categorias_alergias ca ON ( aao.codigo_categoria = ca.codigo )	  " +	
						   "					     WHERE aao.codigo_paciente = " + mp.get("paciente")  +
						   "	       ) x ORDER BY 2 ";
			}
			break;
			//-----	
			//----- Antecedentes Familiares. 	
			//-----	
			case 3:  //-- Consultar si existe antecedentes de Familiares.
			{
				consulta = "  SELECT a.observaciones as observaciones, a.fecha as fecha, a.hora as hora " +
						   "		 FROM antecedentes_familiares a " +
						   "		 	       WHERE a.codigo_paciente = " + mp.get("paciente");
			}
			break;
			case 4:  //-- Consultar TODA la inforamcion de antecedentes de Familiares.
			{
				consulta = " SELECT * FROM (	" +
						   "		SELECT  tef.codigo as cod_enfer, tef.nombre as nom_enfer, aft.parentesco as padece  				" +
						   "				FROM ant_familiares_tipos aft 												 	 			" +	
						   "					  INNER JOIN tipos_enf_familiares tef ON ( aft.tipo_enfermedad_familiar = tef.codigo )  " +	
					       "						   WHERE aft.codigo_paciente = " + mp.get("paciente") +
					       "		UNION ALL 																				  			" +
					       "		SELECT  afo.codigo as cod_enfer, afo.nombre as nom_enfer, afo.parentesco as padece	  				" +
						   "			   FROM ant_familiares_otros afo  																" +
						   "						  WHERE afo.codigo_paciente = " + mp.get("paciente") +
						   "	       ) x ORDER BY 2 ";
			}
			break;
			//-----	
			//----- Antecedentes Familiares Oculares. 	
			//-----	
			case 5:  //-- Consultar si existe antecedentes de Familiares Oculares.
			{
				consulta = "  SELECT a.observaciones as observaciones " +
						   "		 FROM ant_oftal_familiares a " +
						   "		 	  	   WHERE a.paciente = " + mp.get("paciente");
			}
			break;
			case 6:  //-- Consultar TODA la inforamcion de antecedentes de Familiares Oculares.
			{
				consulta = " SELECT * FROM (	" +
						   " SELECT eoi.codigo || '-0' as cod_enfer, teo.nombre as nom_enfer, aofdp.parentesco as parentesco,    " +
						   "		tp.nombre as nom_parentesco, aofd.fecha as fecha, aofd.hora as hora 						   " +		
		    			   "	    FROM ant_oftal_fam_detalle aofd																   " +				
		    	   		   "			 INNER JOIN ant_oftal_fam_det_padece aofdp ON (aofdp.codigo_oftal_fam_det = aofd.codigo)   " +
		    	   		   "			 INNER JOIN enfer_oftal_inst eoi ON (aofd.enfer_oftal_inst = eoi.codigo)			       " +     
		    	   		   "			 INNER JOIN tipo_enfer_oftal teo ON (eoi.enfer_oftal = teo.codigo)						   " +     
		    	   		   "			 INNER JOIN tipos_parentesco tp ON ( aofdp.parentesco  = tp.codigo )					   " +
		    	   		   "				  WHERE eoi.institucion = " + mp.get("institucion") +      
						   "		     		AND ( eoi.tipo_antecedente = " + ConstantesBD.codigoTipoAntecedentePerFam + 
						   " 					      OR eoi.tipo_antecedente = " +  ConstantesBD.codigoTipoAntecedenteFamiliar + ") "+
		    	   		   "			 AND aofd.paciente = " + mp.get("paciente") +
		    	   		   " UNION ALL   " + 
		    	   		   " SELECT oeo.codigo || '-1' as cod_enfer, oeo.nombre as nom_enfer, aofdpo.parentesco as parentesco, tp.nombre as nom_parentesco, aofdo.fecha as fecha, aofdo.hora as hora  " +	     
						   "        FROM ant_oftal_fam_det_otro aofdo																				" +	
						   "        INNER JOIN otro_enfer_oftal oeo ON ( aofdo.otro_enfer_oftal = oeo.codigo  )										" +
						   "        	 INNER JOIN ant_oftal_fam_det_pad_otro aofdpo ON ( aofdpo.codigo_oftal_fam_det_otro = aofdo.codigo )		" +
						   "        	 INNER JOIN tipos_parentesco tp ON ( aofdpo.parentesco = tp.codigo )										" +
						   "        		  WHERE aofdo.paciente = " + mp.get("paciente") +  
						   "	       ) x ORDER BY 2 ";
				
				logger.info("\n consulta->"+consulta);
			}
			break;
			
			//-----	
			//----- Antecedentes PERSONALES  Oculares. 	
			//-----	
			case 7:  //-- Consultar si existe antecedentes de PERSONALES Oculares.
			{
				consulta = "  SELECT a.observaciones as observaciones, a.fecha as fecha, a.hora as hora " +
						   "		 FROM ant_oftal_personales a " +
						   "		 	 	   WHERE a.paciente = " + mp.get("paciente");
			}
			break;
			case 8:  //-- Consultar TODA la inforamcion de antecedentes de PERSONALES Oculares.
			{
				consulta = " SELECT * FROM ( " +
						   "	 SELECT  teo.nombre as nom_enfer, aopm.desde_cuando as desde_cuando,  " +
						   " 		 aopm.tratamiento as tratamiento															 " +	
						   "		 FROM ant_oftal_perso_medicos aopm    																 " +
						   "			  INNER JOIN enfer_oftal_inst eoi  ON ( aopm.enfer_oftal_inst = eoi.codigo  )						 " +
						   "			  INNER JOIN      tipo_enfer_oftal teo  ON (eoi.enfer_oftal = teo.codigo)	 " +
	    	    		   "				   WHERE eoi.institucion = " + mp.get("institucion") + 	     
						   "		     		AND ( eoi.tipo_antecedente = " + ConstantesBD.codigoTipoAntecedentePerFam + 
						   " 					      OR eoi.tipo_antecedente = " +  ConstantesBD.codigoTipoAntecedentePersonal + ") "+
	    				   "				    AND aopm.paciente = " + mp.get("paciente") + 
	    				   " UNION ALL																							 " +	         
	    				   " SELECT oeo.nombre as nom_enfer, aopdo.desde_cuando as desde_cuando,  " +
	    				   "		aopdo.tratamiento as tratamiento															 " +			
						   "	    FROM otro_enfer_oftal oeo																	 " +    
						   "	         INNER JOIN ant_oftal_perso_med_otro aopdo ON (aopdo.otro_enfer_oftal = oeo.codigo)		 " +     
						   "		   			   WHERE aopdo.paciente = " + mp.get("paciente") + 
						   " ) z ORDER BY 2 ";
			}
			break;
			case 9:  //-- Consultar TODA la inforamcion de antecedentes de PERSONALES Oculares (Seccion Quirurgicos).
			{
				consulta = "SELECT nombre_procedimiento as nom_quirur, fecha, causa, fecha_ant, hora_ant	 " + 
					   	   "  	   FROM ant_oftal_perso_quirur aopq					 " +
						   "				 WHERE paciente = " + mp.get("paciente") + 
	    				   " 				       ORDER BY nombre_procedimiento ";
			}
			break;
			//-----	
			//----- Antecedentes GinecoObstetricos. 	
			//-----	
			case 10:  //-- Consultar si existe antecedentes.
			{
				consulta = "  SELECT a.observaciones as observaciones 										" +
						   "		 FROM ant_gineco_obste a 												" +
						   "		 	       WHERE a.codigo_paciente = " + mp.get("paciente");
			}
			break;
			//-----	
			//----- Antecedentes de Medicamentos . 	
			//-----	
			case 11:  //-- Consultar si existe antecedentes.
			{
				consulta = "  SELECT a.observaciones as observaciones, a.fecha as fecha, a.hora as hora " +
						   "		 FROM antece_medicamentos a " +
						   "		 	 	   WHERE a.codigo_paciente = " + mp.get("paciente");
			}
			break;
			case 12:  //-- Consultar TODA la inforamcion de antecedentes
			{
				consulta = " SELECT am.nombre, am.dosificacion as dosis, am.frecuencia, am.fecha_inicio, am.fecha_finalizacion		" +
					   	   "		FROM ant_medicamentos am										" +
						   "				  WHERE am.codigo_paciente = " + mp.get("paciente") + 
						   " 				 		ORDER BY am.nombre  ";
			}
			break;
			//-----	
			//----- Antecedentes Medicos y Quirurgicos. 	
			//-----	
			case 13:  //-- Consultar si existe antecedentes.
			{
				consulta = "  SELECT am.observaciones as observaciones, am.fecha as fecha, am.hora as hora 	" +
						   "		 FROM antecedentes_morbidos am 		" +
						   "		 	 	   WHERE am.codigo_paciente = " + mp.get("paciente");
			}
			break;
			case 14:  //-- Consultar TODA la inforamcion de antecedentes
			{
				consulta = "SELECT * FROM (	" +
						   "				SELECT tam.nombre as nombre_med, amm.fecha_inicio as desde_cuando, amm.tratamiento as tratamiento,   " +
						   "					   amm.restriccion_dietaria as restriccion_dietaria, amm.fecha as fecha, amm.hora as hora 		 " + 
						   "					   FROM  ant_morbidos_medicos amm																	 " +
						   "							INNER JOIN tipos_ant_medicos tam ON ( amm.tipo_antecedente_medico = tam.codigo )		 " +
						   "						 	     WHERE amm.codigo_paciente = " + mp.get("paciente") + 
						   " UNION ALL		" +
						   " SELECT ammo.nombre as nombre_med, ammo.fecha_inicio as desde_cuando, ammo.tratamiento as tratamiento,				" +
						   "	    ammo.restriccion_dietaria as restriccion_dietaria, ammo.fecha as fecha, ammo.hora as hora 				    " +  
						   "	    FROM ant_morbido_med_otros ammo																			    " +
						   "			 WHERE ammo.codigo_paciente = " + mp.get("paciente") + 	
						   " 			) z ORDER BY z.nombre_med ";											
			}
			break;
			case 15:  //-- Consultar si existe antecedentes quirurgicos.
			{
				consulta = "  SELECT a.nombre as q_nombre, a.fecha as q_fecha, a.causa as q_causa, a.complicaciones as q_complicaciones,  " +
						   "		 a.recomendaciones as q_recomendaciones, a.fecha_ant as fecha, a.hora_ant as hora 														  " + 
						   "	 	 FROM ant_morbidos_quirurgicos a																  " +	
						   "			  	   WHERE a.codigo_paciente = " + mp.get("paciente") + 
						   "					     ORDER BY a.nombre ";	
			}
			break;
			//-----	
			//----- Antecedentes Toxicos. 	
			//-----	
			case 16:  //-- Consultar si existe antecedentes.
			{
				consulta = "  SELECT a.observaciones as observaciones " +
						   "		 FROM antecedentes_toxicos a " +
						   "		 	 	   WHERE a.codigo_paciente = " + mp.get("paciente");
			}
			break;
			case 17:  //-- Consultar TODA la inforamcion de antecedentes.
			{
				
				
				
				//-- Para insertar las condiciones de Busqueda a  las consultas. 
				String cadFecha = "";
				if ( !fechaInicio.equals("") )
				{
					if ( !horaInicio.equals("") )
					{
						  cadFecha = " AND fecha_grabacion ||'-'|| hora_grabacion >= '" + fechaInicio + "'-'" + horaInicio + "'";
					}
					else
					{
						  cadFecha = "  AND fecha_grabacion >= '" + fechaInicio + "'";
					}
				}
				if ( !fechaFinal.equals("") )
				{
					if ( !horaFinal.equals("") )
					{
						cadFecha   += " AND fecha_grabacion ||'-'|| hora_grabacion <= '" + fechaFinal + "'-'" + horaFinal + "'";
					}
					else
					{
						cadFecha +=   " AND fecha_grabacion <= '" + fechaFinal + "'";
					}
				}				
				
				consulta = " SELECT tat.nombre as nombre, att.actual, att.cantidad, att.tiempo_habito, att.frecuencia,fecha_grabacion as fechagrabacion,hora_grabacion as horagrabacion, att.fecha as fecha, att.hora as hora  " +
						   "		FROM ant_toxicos_tipos att														   " +		
				   		   "			 INNER JOIN tipos_ant_toxicos tat ON ( tat.codigo = att.cod_tipo_antecedente ) " +	
						   "				  WHERE att.codigo_paciente = " + mp.get("paciente") + cadFecha +
						   " UNION ALL    " +
						   " SELECT nombre, actual, cantidad, tiempo_habito, frecuencia	,fecha_grabacion as fechagrabacion,hora_grabacion as horagrabacion, ato.fecha as fecha, ato.hora as hora 						 " +		
						   "	   FROM ant_toxicos_otros ato												     " +														
						   "				  WHERE ato.codigo_paciente = " + mp.get("paciente") + cadFecha;
			}
			break;
			//-----	
			//----- Antecedentes transfusionales 	
			//-----	
			case 18:  //-- Consultar si existe antecedentes.
			{
				consulta = " SELECT	a.observaciones, a.fecha as fecha, a.hora as hora " +
						   "		FROM antece_transfusionales a " +
						   "	 			  WHERE a.codigo_paciente = " + mp.get("paciente");		
			}
			break;
			case 19:  //-- Consultar TODA la inforamcion de antecedentes
			{
				consulta = "SELECT  at.componente_transfundido as nombre, at.fecha_transfusion as fecha,	" +
						   "		at.lugar, at.causa,  at.edad_paciente, at.donante, at.fecha as fecha, at.hora as hora 						" +
						   "		FROM ant_transfusionales at												" +
						   "				  WHERE at.codigo_paciente = " + mp.get("paciente") +  	
						   " 				 	    ORDER BY componente_transfundido					    ";											
			}
			break;
			//-----	
			//----- Antecedentes Vacunas. 	
			//-----	
			case 20:  
			{
				consulta = "	SELECT  a.observaciones 													" +
						   "			FROM antecedentes_vacunas a										" +
						   "					  WHERE a.codigo_paciente = " + mp.get("paciente"); 	
			}
			break;
			//-----	
			//----- Antecedentes Varios 	
			//-----	
			case 21:  //-- Consultar si existe antecedentes.
			{
				
				consulta = " SELECT	ant_varios.descripcion as observaciones			" +
						   " FROM antecedentes_varios ant_varios 	" + getCadenaWhereAntecedentesVarios(mp, tipoBaseDatos);		
			}
			break;
			case 22:  //-- Consultar TODA la informacion de antecedentes
			{
				//-- Para insertar las condiciones de Busqueda a  las consultas.				
				consulta = " SELECT to_char(ant_varios.fecha,'DD/MM/YYYY') as fecha, ant_varios.hora, a.descripcion as nombre, ant_varios.descripcion " +
						   " FROM antecedentes_varios ant_varios	" +
						   " INNER JOIN antecedentes a ON  (ant_varios.tipo = a.id ) " + getCadenaWhereAntecedentesVarios(mp, tipoBaseDatos);
			}
			break;
			//-----	
			//----- Antecedentes Pediatricos 	
			//-----	
			case 23:  //-- Consultar si existe antecedentes.
			{
				consulta = " SELECT	a.observaciones as observaciones			" +
						   "		FROM antecedentes_pediatricos a				" +
						   "	 			  WHERE a.codigo_paciente = " + mp.get("paciente");		
			}
			break;
			//-----	
			//----- Antecedentes Odontologicos 	
			//-----	
			case 24:  //-- Consultar si existe antecedentes.
			{
				consulta = " SELECT	a.observaciones " +
						   "		FROM antecedente_odontologia a " +
						   "	 			  WHERE a.cod_paciente = " + mp.get("paciente");		
			}
			break;
			case 25:  //-- Consultar TODA la inforamcion de antecedentes
			{
				consulta = "	SELECT * FROM (   										" +			 
						   "					SELECT h.nombre,coalesce(h.observaciones,'') as observaciones							" + 
						   "					 	   FROM habito_odo_otr	h			" +				 
						   "					 				 WHERE h.cod_paciente = "  + mp.get("paciente") +
						   "					 UNION ALL								" +			 
						   "					 SELECT tho.nombre,coalesce(ho.observaciones,'') as observaciones						" +	
						   "					 		FROM  tipo_habito_odo_inst thoi					" +			
						   "						 		 INNER JOIN tipo_habito_odo tho ON ( tho.codigo = thoi.cod_tipo_habito_odo )  " +
						   "						 		 INNER JOIN habito_odo ho  ON ( ho.cod_tipo_habito_odo_inst = thoi.codigo ) " +
						   "							 		  WHERE ho.cod_paciente = "  + mp.get("paciente") +		 
						   "							 		  	AND thoi.cod_institucion = "  + mp.get("institucion") + 	 
						   "		           ) z ORDER by nombre"; 	
				
				logger.info("\n consulta odon-->"+consulta);
			}
			break;
			case 26:  //-- Consultar TODA la inforamcion de antecedentes
			{
				consulta = "  SELECT * FROM (  " +			 
						   "				 SELECT t.nombre as tnombre,coalesce(t.observaciones,'') as tobservaciones " + 
						   "				 		FROM traumatismo_odo_otr t								" + 
						   "				 				  WHERE t.cod_paciente = "  + mp.get("paciente") + 		
						   "				 UNION ALL			 											" +
						   "					 SELECT tto.nombre as tnombre,coalesce(tod.observaciones,'') as tobservaciones " +	
						   "					 		FROM  tipo_traumatismo_odo_inst ttoi						    " +
						   "					 			 INNER JOIN tipo_traumatismo_odo tto ON ( tto.codigo = ttoi.cod_tipo_traumatismo_odo ) " +
						   "					 			 INNER JOIN traumatismo_odo tod  ON ( tod.cod_tipo_traumatismo_odo_inst = ttoi.codigo )  " +
						   "						 			  WHERE tod.cod_paciente = "  + mp.get("paciente") + 					 
						   "						 			  	AND ttoi.cod_institucion = "  + mp.get("institucion") +	 
						   "						 			 ) z ORDER by tnombre"; 					
			}
			break;
			case 27:  //-- Consultar TODA la inforamcion de antecedentes
			{
				consulta = " SELECT top.tipo_tratamiento as pnombre, 				" +
						   "		top.fecha_inicio, top.fecha_finalizacion		" +
						   "	    FROM tratamiento_odo_previo top					" +
						   "			 WHERE top.cod_paciente = " + mp.get("paciente") + 	 				 
	 			 		   " 				   ORDER by top.tipo_tratamiento"; 					
			}
			break;
			//-----	
			//----- Hoja Quirurgica 	
			//-----	
			case 28:  //-- Consultar La Informacion del Encabezado de las Hojas Registradas.
			{
				consulta = "SELECT s.numero_solicitud as enca_solicitud,s.consecutivo_ordenes_medicas as enca_orden, s.fecha_solicitud as enca_fecha_solicitud, 			   				  " +
						   "	   s.hora_solicitud as enca_hora_solicitud, s.urgente as enca_urgente,  			  			 				  " + 
						   "		   cc.nombre as enca_centro_costo, s.numero_autorizacion as enca_autorizacion, e.nombre as enca_especialidad" +
						   "		, sal.descripcion AS enca_nombre_sala  " +
						   "		   FROM solicitudes s 																		   				  " +			
						   "			    INNER JOIN solicitudes_cirugia sc ON ( s.numero_solicitud = sc.numero_solicitud )	  				  " +
						   "			    INNER JOIN centros_costo cc ON ( s.centro_costo_solicitado = cc.codigo)				  				  " +	
						   "				INNER JOIN especialidades e ON ( e.codigo = s.especialidad_solicitante )			   				  " +	
						   " 				INNER JOIN hoja_quirurgica hq ON (hq.NUMERO_SOLICITUD = s.numero_solicitud ) " +
						   "				LEFT JOIN salas sal on(hq.sala=sal.consecutivo) " +
						   "	 		    WHERE s.numero_solicitud IN (" + mp.get("solicitudes") + ")   ";			
			}
			break;
			case 29:  //-- Consultar La Informacion de los Diagnosticos.
			{
				consulta = "SELECT dp.numero_solicitud as diag_solicitud, dp.diagnostico as diag_diagnostico, dp.tipo_cie AS diag_tipo_cie, " +
						   "	   d.nombre AS diag_nombre, dp.principal AS diag_principal					 								" +
						   "	   FROM diagnosticos d 																						" +
						   "			INNER JOIN diag_preoperatorio_cx dp ON (d.acronimo=dp.diagnostico AND dp.tipo_cie=d.tipo_cie) 		" +
						   "				 WHERE dp.numero_solicitud IN (" + mp.get("solicitudes") + ")" +
						   "					   ORDER BY dp.numero_solicitud		"; 					
			}
			break;
			case 30:  //-- Consultar Los servicios de Todas las Hojas Quirurgicas
			{
				consulta = "SELECT s.codigo AS cir_servicio, scps.numero_solicitud as cir_solicitud,  rs.codigo_propietario || '  ' || s.especialidad || '-' || s.codigo || '-' || rs.descripcion as cir_cirugia, " +
						   "	   s.espos as cir_espos, getnombrepersona(pc.codigo_profesional) as cir_medico, ind_via_acceso As indviaacc," +
						   "		getDescQxHq(scps.codigo) As infoqx, scps.codigo as codigo_cirugia , espe.nombre AS especialidad_interviene	" +
						   "  	   FROM referencias_servicio rs 											" +
						   "			INNER JOIN sol_cirugia_por_servicio scps	ON ( rs.servicio = scps.servicio )					" +
						   "			INNER JOIN servicios s ON ( s.codigo = scps.servicio )					" +
						   "			LEFT OUTER JOIN profesionales_cirugia pc ON (pc.cod_sol_cx_serv=scps.codigo AND pc.tipo_asocio="+ValoresPorDefecto.getAsocioCirujano(Integer.parseInt(mp.get("institucion")+""))+")	" +
						   "  			INNER JOIN especialidades espe on(espe.codigo=scps.especialidad)   		" +
						   "	   			 WHERE scps.numero_solicitud IN (" + mp.get("solicitudes") + ")" +	
						   "	   			   AND rs.tipo_tarifario =" + ConstantesBD.codigoTarifarioCups; 					
			}
			break;
			case 31:  //-- Consultar los Diagnosticos de los servicios.
			{
				consulta = " SELECT scps.numero_solicitud AS ds_solicitud, scps.servicio as ds_servicio, ds.principal AS ds_principal,			" +
						   "	    ds.complicacion AS ds_complicacion,  ds.diagnostico ||'-'|| ds.tipo_cie ||' '|| d.nombre AS ds_diagnostico	" +
						   "	    FROM diagnosticos d																							" +
						   "		 	 INNER JOIN diag_post_opera_sol_cx ds ON (d.acronimo=ds.diagnostico AND ds.tipo_cie=d.tipo_cie)			" +
						   "             INNER JOIN SALASCIRUGIA.INFORME_QX_POR_ESPECIALIDAD informe ON(ds.COD_INFORME_ESPECIALIDAD=informe.codigo)          " +
						   "		  	 INNER JOIN sol_cirugia_por_servicio scps ON ( scps.COD_INFORME_ESPECIALIDAD = informe.codigo)  		" +	
						   "		   		  WHERE scps.numero_solicitud IN (" + mp.get("solicitudes") + ")" +
						   "						ORDER BY scps.numero_solicitud  DESC	";	
			}
			break;
			case 32:  //-- Consultar los Diagnosticos de los servicios.
			{
				consulta =  "	SELECT h.numero_solicitud as iq_solicitud, p.requiere_uci AS iq_uci, getNombrePersona(p.solicitante) as iq_medico,	" +
							"		   sc.duracion_final_cx as iq_duracion," +
							"		   to_char(sc.fecha_inicial_cx, 'DD/MM/YYYY') AS iq_fi, sc.hora_inicial_cx AS iq_hi,   				" + 
							"		   to_char(sc.fecha_final_cx, 'DD/MM/YYYY') AS iq_ff, sc.hora_final_cx AS iq_hf, h.participo_anestesiologo AS iq_participo,				" +
							"		   ta.descripcion as iq_tipo_anestesia, getdesccampostextohqx(sc.numero_solicitud,'"+ConstantesIntegridadDominio.acronimoTipoInformacionQuirurgica+"') as iq_observaciones," +
							"          getdescsalsalapac(sc.salida_sala_paciente) As salsalpac," +
						    "          to_char(sc.fecha_salida_sala,'dd/mm/yyyy') as fechasalpac," +
							"		   sc.hora_salida_sala As horasalpac, " +
							"		   getdesccampostextohqx(sc.numero_solicitud,'"+ConstantesIntegridadDominio.acronimoTipoNotasAclaratorias+"') as notaclara							" +
							"		   FROM peticion_qx p  																				" +
							"			    INNER JOIN  solicitudes_cirugia sc ON ( p.codigo = sc.codigo_peticion )   							" +
							"			    INNER JOIN hoja_quirurgica h ON ( sc.numero_solicitud = h.numero_solicitud )   							" +
							"		   		LEFT OUTER JOIN hoja_tipo_anestesia hta ON ( hta.numero_solicitud = sc.numero_solicitud  )   			" +
							"		   		LEFT OUTER JOIN tipos_anestesia ta ON ( ta.codigo = h.tipo_anestesia AND ta.mostrar_en_hqx = " + ValoresPorDefecto.getValorTrueParaConsultas() + " ) " +
							"		   				  WHERE h.numero_solicitud IN (" + mp.get("solicitudes") + ")";	
			}
			break;
			case 33:  //-- Consultar los Observaciones y Las Patologias 
			{
				consulta =  "	SELECT sc.numero_solicitud as o_solicitud,  getdesccampostextohqx(sc.numero_solicitud,'"+ConstantesIntegridadDominio.acronimoTipoObservaciones+"') AS o_observaciones," +
							"		   getdesccampostextohqx(sc.numero_solicitud,'"+ConstantesIntegridadDominio.acronimoTipoPatologia+"')AS o_patologia	" +
							"		   FROM solicitudes_cirugia sc 																						" +
							"		   		INNER JOIN hoja_quirurgica h ON ( sc.numero_solicitud = h.numero_solicitud )  								" +
							"					 WHERE h.numero_solicitud IN (" + mp.get("solicitudes") + ")";	
			}
			break;
			//-----	
			//-----   La informacion de notas Generales de Enfermeria. 	
			//-----	
			case 34:   
			{
				consulta = " SELECT ne.numero_solicitud, ne.nota, 																							" + 
						   "	    to_char(ne.fecha_grabacion,'DD/MM/YYYY') AS fecha_grabacion, ne.hora_grabacion AS hora_grabacion, 	" + 
						   "	    getNombrePersona(ne.codigo_enfermera) AS enfermero,													" +
						   "	    getespecialidadesmedico(u.login, ', ') as especialidades												" +	
						   "	    FROM solicitudes_cirugia sc																				" +
						   "	   		 INNER JOIN notas_enfermeria ne  ON (sc.numero_solicitud = ne.numero_solicitud)				" +
						   "	   		 INNER JOIN usuarios u ON ( u.codigo_persona = ne.codigo_enfermera )								" +			
						   "	   		      WHERE ne.numero_solicitud IN (" + mp.get("solicitudes") + ")"; 					
			}
			break;
			//-----	
			//----- Notas de Recuperacion Generales 	
			//-----	
			case 35:  //-- Consultar si existe 
			{
				consulta = "  SELECT sc.numero_solicitud AS o_numero_solicitud, nrg.medicamentos as o_medicamentos, 					" +
						   "		 nrg.observaciones AS o_observaciones																	" +
						   "		 FROM solicitudes_cirugia sc   																		" + 
						   "   		  	  INNER JOIN notas_recuperacion_general nrg ON ( sc.numero_solicitud = nrg.numero_solicitud )	" +	
 						   "				   WHERE nrg.numero_solicitud IN (" + mp.get("solicitudes") +")";
			}
			break;
			case 36:  //-- Consultar TODA la inforamcion 
			{
				consulta = "	SELECT dnr.numero_solicitud, to_char(dnr.fecha_grabacion,'DD/MM/YYYY') as fecha_grabacion,   " + 
						   "		   to_char(dnr.hora_grabacion,'HH24:MI') as  hora_grabacion,							 " +
						   "		    getnombrepersona(dnr.codigo_enfermera) ||' - '||  m.numero_registro  ||' '|| getespecialidadesmedico(u.login, ', ') AS medico, "  +
						   "		   t.descripcion as tipo, c.descripcion as campo, dnr.valor as nota 					 " +
						   "		   FROM det_notas_recuperacion dnr 														 " +
						   "				INNER JOIN usuarios u ON ( u.codigo_persona = dnr.codigo_enfermera )			 " +
						   "				INNER JOIN medicos m ON ( m.codigo_medico = dnr.codigo_enfermera )				 	 " +			
						   "				INNER JOIN param_notas_recup_inst p ON ( p.codigo = dnr.nota_recup )			 " +
						   "				INNER JOIN campos_notas_recuperacion c ON ( c.codigo = p.campo )			 " +
						   "				INNER JOIN tipos_notas_recuperacion t ON ( p.tipo = t.codigo )				 	 " +			
					 	   "				     WHERE dnr.numero_solicitud IN (" + mp.get("solicitudes") +")" +
					 	   "				       AND p.institucion = " + mp.get("institucion")  + 
					 	   "				       AND dnr.valor <> '' " + 
						   "						   ORDER BY dnr.fecha_grabacion, dnr.hora_grabacion 	";
			}
			break;
			case 37:  //-- Embarazo Actual de Antecedentes Pediatricos. 
			{
				consulta = "  SELECT ca.nombre AS categoria, c.nombre AS compli     " +
				    	   "		 FROM cat_emb_opciones c 						" +	
				    	   "		      INNER JOIN categorias_embarazo ca ON ( ca.codigo = c.codigo_categoria_embarazo  )   " + 
				    	   "	          INNER JOIN ant_ped_embarazo a ON ( a.codigo_categoria_embarazo = c.codigo_categoria_embarazo AND a.cod_cat_embarazo_opcion = c.codigo ) " +  
				    	   "		   	   	   WHERE a.codigo_paciente = " + mp.get("paciente")  +  
				    	   "				  	   	 ORDER BY ca.nombre";	
			}
			break;
			
			
			
			
			default :
				return (new HashMap());
		}
		
		PreparedStatementDecorator pst = null;
		ResultSetDecorator rs = null; 
		try
		{
			logger.info("nroConsulta ["+nroConsulta+"]  consulta: "+consulta);
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			rs = new ResultSetDecorator(pst.executeQuery());
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(rs,true,false);
			pst.close();
			return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error en consultarInformacion de SqlBaseImpresionResumenAtencionesDao: "+e); 
			return null;
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
		}

	}

	/**
	 * Retrona el String con el filtro segun condiciones
	 * @param mp
	 * @param tipoBaseDatos
	 * @return
	 * @author javrammo
	 */
	private static String getCadenaWhereAntecedentesVarios(HashMap mp, int tipoBaseDatos) {
		String fechaEgreso = "";
		String where = " WHERE ant_varios.codigo_paciente = " + mp.get("paciente");	
		if(mp.containsKey(KEY_FECHA_EGRESO) && mp.get(KEY_FECHA_EGRESO) instanceof InfoIngresoDto){
			
			InfoIngresoDto infoIngresoDto =  (InfoIngresoDto) mp.get(KEY_FECHA_EGRESO);
			String fechaHoraEgreso = null;
			//Si tiene fecha y hora
			if(infoIngresoDto.getFechaEgreso()!= null){
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				fechaHoraEgreso = sdf.format(infoIngresoDto.getFechaEgreso());
				if(infoIngresoDto.getHoraEgreso()!=null && !infoIngresoDto.getHoraEgreso().trim().equals("")){
					fechaHoraEgreso  = fechaHoraEgreso.concat(" ").concat(infoIngresoDto.getHoraEgreso());					
				}						
				
			}					
			if(fechaHoraEgreso != null){
				String condicionFecha = "";
				if(tipoBaseDatos == DaoFactory.POSTGRESQL){
					condicionFecha = " AND to_timestamp(to_char(ant_varios.fecha,'DD/MM/YYYY') || ' ' || to_char(ant_varios.hora,'HH24:MI'), 'DD/MM/YYYY HH24:MI')  <= to_timestamp('"+fechaHoraEgreso+"', 'DD/MM/YYYY HH24:MI')  ";
				}else if(tipoBaseDatos == DaoFactory.ORACLE){
					condicionFecha = " AND to_date(to_char(ant_varios.fecha,'DD/MM/YYYY') || ' ' || ant_varios.hora, 'DD/MM/YYYY HH24:MI') <= to_date('"+fechaHoraEgreso+"', 'DD/MM/YYYY HH24:MI') ";
				}
				where = where.concat(condicionFecha);
			}
		}
		return where;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap consultarSoporteRespiratorio(Connection con, HashMap vo)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		String cadena=consultaSoportesRespiratorio;
		try
		{
			if((!(vo.get("fechaInicial")+"").trim().equals(""))&&(!(vo.get("fechaFinal")+"").trim().equals("")))
			{
				cadena+=" AND chre.fecha_registro  between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"'";
			}
			if((!(vo.get("horaInicial")+"").trim().equals(""))&&(!(vo.get("horaFinal")+"").trim().equals("")))
			{
				cadena+=" AND ((  chre.fecha_registro > '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"') or ( chre.fecha_registro = '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and to_date(chre.hora_registro,'HH24:MI') >= to_date('"+vo.get("horaInicial")+"','HH24:MI')) ) " +
				"and (( chre.fecha_registro < '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"') or ( chre.fecha_registro = '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"' and to_date(chre.hora_registro,'HH24:MI') <= to_date('"+vo.get("horaFinal")+"','HH24:MI'))) ";
			}
			if(!(vo.get("cuenta")+"").trim().equals(""))
			{
				cadena+=" AND c.id='"+ vo.get("cuenta")+"'";
			}
			cadena+=" GROUP BY chre.codigo,chre.fecha_registro,chre.hora_registro,chre.obs_soporte,chre.datos_medico order by chre.fecha_registro,chre.hora_registro ";

			logger.info(cadena.replace("?", vo.get("ingreso")+""));
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1, vo.get("ingreso"));
			mapa =UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			for(int i=0;i<Integer.parseInt(mapa.get("numRegistros")+"");i++)
			{
				String cadenaTemp="";
				cadenaTemp="SELECT tsr.nombre as parametro,sr.valor as valor from soporte_resp_enfer_valor sr inner join soporte_respira_cc_inst srci on (sr.soporte_respira_cc_inst=srci.codigo) inner join tipo_soporte_respira tsr on(srci.tipo_soporte_respira=tsr.codigo) where codigo_histo_enfer='"+mapa.get("codigo_"+i)+"'" + " ORDER BY parametro";
				ps= new PreparedStatementDecorator(con.prepareStatement(cadenaTemp,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				mapa.put("soportes_"+i,UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())));
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * M�todo que consulta los c�digos de petici�n de cirug�a de acuerdo a los par�metros
	 * de b�squeda en la impresi�n de la historia cl�nica
	 * @param con
	 * @param cuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion
	 * @return
	 */
	public static HashMap consultarPeticionesCirugiaImpresionHc(Connection con, String cuenta, String cuentaAsocio, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion, String solicitudesFactura)
	{
		//FIXME  consulta de historia clinica 
		StringBuffer consultaBuffer=new StringBuffer();
		String fechaHoraInicial="", fechaHoraFinal="";
		
		consultaBuffer.append("SELECT " +
				"sc.codigo_peticion AS codigo_peticion," +
				"sc.numero_solicitud AS numero_solicitud," +
				"to_char(sol.fecha_solicitud,'DD/MM/YYYY') as fecha_solicitud," +
				"substr(sol.hora_solicitud||'',0,6) as hora_solicitud," +
				"getintegridaddominio(sc.ind_qx) as ind_qx," +
				"sol.consecutivo_ordenes_medicas as orden," +
				"getValidacionMostrarProceSolo(sc.numero_solicitud) AS mostrarprocesosolo," +
				"res.codigo AS codigo_respuesta_proc, " +
				"  to_char(sc.FECHA_INICIAL_CX,'DD/MM/YYYY')                              AS fecha_cirugia, " +
				"  per.PRIMER_APELLIDO||' '||per.segundo_apellido||' '||per.primer_nombre||' '||per.segundo_nombre AS medico_solicita, " +
				" estadoSol.nombre                                  AS estadoMedico " +
				" "+
							  "		FROM solicitudes sol "+
							  "			INNER JOIN personas per ON (per.codigo=sol.CODIGO_MEDICO) "+
							  "			INNER JOIN solicitudes_cirugia sc ON (sol.numero_solicitud=sc.numero_solicitud) "+
							  "			LEFT OUTER JOIN res_sol_proc res ON (res.numero_solicitud=sc.numero_solicitud) " +
							  "			LEFT OUTER JOIN hoja_quirurgica hq ON (sc.numero_solicitud=hq.numero_solicitud) " +
							  "LEFT OUTER JOIN hoja_anestesia  ha ON (ha.numero_solicitud=sc.numero_solicitud) " +
							  " INNER JOIN ESTADOS_SOL_HIS_CLI estadoSol ON (estadoSol.codigo=sol.ESTADO_HISTORIA_CLINICA) ");
		
		//-------Si tiene cuenta de asocio -----------//
		if(!cuentaAsocio.equals("0"))
		{
			if(mostrarInformacion.equals("A"))
				consultaBuffer.append(" WHERE sol.cuenta IN ("+cuenta+","+cuentaAsocio+")");
			else if(mostrarInformacion.equals("U"))
				consultaBuffer.append(" WHERE sol.cuenta = "+cuenta);
			else if(mostrarInformacion.equals("H"))
				consultaBuffer.append(" WHERE sol.cuenta = "+cuentaAsocio);
			else if(mostrarInformacion.equals(""))
//				consultaBuffer.append(" WHERE sol.cuenta="+cuenta);
				consultaBuffer.append(" WHERE sol.cuenta IN ("+cuenta+","+cuentaAsocio+")");
			else 
				consultaBuffer.append(" WHERE sol.cuenta="+cuenta);
		}
    	else if (!cuenta.equals(""))
    		consultaBuffer.append(" WHERE sol.cuenta="+cuenta);
		
		//------Si hay filtro de solicitudes
		if(UtilidadCadena.noEsVacio(solicitudesFactura))
			consultaBuffer.append(" AND sc.numero_solicitud IN ("+solicitudesFactura+") ");
		
		//---Si la fecha inicial no es vac?a entonces la fecha final tampoco ------//
		if(UtilidadCadena.noEsVacio(fechaInicial))
			{	
				if(UtilidadCadena.noEsVacio(horaInicial))
				{
					fechaHoraInicial=fechaInicial+"-"+horaInicial;
					fechaHoraFinal=fechaFinal+"-"+horaFinal;
					consultaBuffer.append(" AND (" +
										  "		  (sc.fecha_inicial_cx || '-' || sc.hora_inicial_cx >= '"+fechaHoraInicial+"'" +
										  "         AND sc.fecha_inicial_cx || '-' || sc.hora_inicial_cx <='"+fechaHoraFinal+"'" +
										  "       )" +
										  "     OR " +
										  "		  (sc.fecha_final_cx || '-' || sc.hora_final_cx >= '"+fechaHoraInicial+"'" +
										  "        AND sc.fecha_final_cx || '-' || sc.hora_final_cx <='"+fechaHoraFinal+"'" +
										  "       )" +
										  "    )");
				}
				else
				{
				   fechaHoraInicial=fechaInicial;
				   fechaHoraFinal=fechaFinal;
				   consultaBuffer.append(" AND (" +
							  "		 			 (sc.fecha_inicial_cx >= '"+fechaHoraInicial+"'" +
							  "         		  AND sc.fecha_inicial_cx <='"+fechaHoraFinal+"'" +
							  "        			 )" +
							  "     		   OR " +
							  "		  			 (sc.fecha_final_cx >= '"+fechaHoraInicial+"'" +
							  "        			  AND sc.fecha_final_cx <='"+fechaHoraFinal+"'" +
							  "       			 )" +
							  "    			  )");
				}
		}
			
		consultaBuffer.append(" AND sc.ind_qx IN ('"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia+"','"+ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento+"') " +
							  " ORDER BY sol.fecha_solicitud,sol.hora_solicitud ASC ,sc.codigo_peticion ASC ");
		logger.info("consultarPeticionesCirugiaImpresionHc-->"+consultaBuffer.toString());
		PreparedStatementDecorator stm = null;
		ResultSetDecorator rs = null;
		   try
			{
			stm= new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString()));
			rs = new ResultSetDecorator(stm.executeQuery()); 
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(rs);
			stm.close();
			return mapaRetorno;
			}
			catch (SQLException e)
			{
				logger.error("Error en consultarPeticionesCirugiaImpresionHc [SqlBaseImpresionResumenAtencionesDao] "+e);
				return null;
			}	
		   finally{
			   UtilidadBD.cerrarObjetosPersistencia(stm, rs, null);
	}
	}
	
	
	/**
	 * @param con
	 * @param cuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion
	 * @param solicitudesFactura
	 * @param numeroSolicitud
	 * @return  HashMap con peticiones 
	 */
	public static HashMap consultarPeticionesCirugiaImpresionHcXNumeroSolicitud(Connection con, String cuenta, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion, String solicitudesFactura,Integer numeroSolicitud)
	{
		//FIXME  consulta de historia clinica 
		StringBuffer consultaBuffer=new StringBuffer();
		String fechaHoraInicial="", fechaHoraFinal="";
		
		consultaBuffer.append("SELECT " +
				"sc.codigo_peticion AS codigo_peticion," +
				"sc.numero_solicitud AS numero_solicitud," +
				"to_char(sol.fecha_solicitud,'DD/MM/YYYY') as fecha_solicitud," +
				"substr(sol.hora_solicitud||'',0,6) as hora_solicitud," +
				"getintegridaddominio(sc.ind_qx) as ind_qx," +
				"sol.consecutivo_ordenes_medicas as orden," +
				"getValidacionMostrarProceSolo(sc.numero_solicitud) AS mostrarprocesosolo," +
				"res.codigo AS codigo_respuesta_proc " +
				" "+
							  "		FROM solicitudes sol "+
							  "			INNER JOIN solicitudes_cirugia sc ON (sol.numero_solicitud=sc.numero_solicitud) "+
							  "			LEFT OUTER JOIN res_sol_proc res ON (res.numero_solicitud=sc.numero_solicitud) "+
							  "			LEFT OUTER JOIN hoja_quirurgica hq ON (sc.numero_solicitud=hq.numero_solicitud) " +
							  "			LEFT OUTER JOIN hoja_anestesia ha ON (ha.numero_solicitud=sc.numero_solicitud) ");
		
		if (!cuenta.equals(""))
    		consultaBuffer.append(" WHERE sol.cuenta="+cuenta);
		
		//------Si hay filtro de solicitudes
		if(UtilidadCadena.noEsVacio(solicitudesFactura))
			consultaBuffer.append(" AND ("+UtilidadTexto.convertirVectorACodigosSeparadosXOr(UtilidadTexto.StringToVector(solicitudesFactura),"sc.numero_solicitud" ,false)+")");
		
		//---Si la fecha inicial no es vac?a entonces la fecha final tampoco ------//
		if(UtilidadCadena.noEsVacio(fechaInicial))
			{	
				if(UtilidadCadena.noEsVacio(horaInicial))
				{
					fechaHoraInicial=fechaInicial+"-"+horaInicial;
					fechaHoraFinal=fechaFinal+"-"+horaFinal;
					consultaBuffer.append(" AND (" +
										  "		  (sc.fecha_inicial_cx || '-' || sc.hora_inicial_cx >= '"+fechaHoraInicial+"'" +
										  "         AND sc.fecha_inicial_cx || '-' || sc.hora_inicial_cx <='"+fechaHoraFinal+"'" +
										  "       )" +
										  "     OR " +
										  "		  (sc.fecha_final_cx || '-' || sc.hora_final_cx >= '"+fechaHoraInicial+"'" +
										  "        AND sc.fecha_final_cx || '-' || sc.hora_final_cx <='"+fechaHoraFinal+"'" +
										  "       )" +
										  "    )");
				}
				else
				{
				   fechaHoraInicial=fechaInicial;
				   fechaHoraFinal=fechaFinal;
				   consultaBuffer.append(" AND (" +
							  "		 			 (sc.fecha_inicial_cx >= '"+fechaHoraInicial+"'" +
							  "         		  AND sc.fecha_inicial_cx <='"+fechaHoraFinal+"'" +
							  "        			 )" +
							  "     		   OR " +
							  "		  			 (sc.fecha_final_cx >= '"+fechaHoraInicial+"'" +
							  "        			  AND sc.fecha_final_cx <='"+fechaHoraFinal+"'" +
							  "       			 )" +
							  "    			  )");
				}
		}
			
		consultaBuffer.append(" AND " + "(" + "sc.ind_qx = " + "'" +ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia + "'" + "  OR  " + " sc.ind_qx = " + "'" + ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento +"'" + " ) " + 
							  " and sol.numero_solicitud=?  ORDER BY sol.fecha_solicitud,sol.hora_solicitud ASC ,sc.codigo_peticion ASC ");
		
		PreparedStatementDecorator stm = null;
		ResultSetDecorator rs = null;
		   try
			{
				stm= new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString()));
				stm.setInt(1, numeroSolicitud);
				rs = new ResultSetDecorator(stm.executeQuery());
				HashMap mapaRetorno=UtilidadBD.cargarValueObject(rs);
				
			return mapaRetorno;
			}
			catch (SQLException e)
			{
				logger.error("Error en consultarPeticionesCirugiaImpresionHc [SqlBaseImpresionResumenAtencionesDao] "+e);
				return null;
			}	
		    finally{
			   UtilidadBD.cerrarObjetosPersistencia(stm, rs, null);
	}
	}
	
	
	
	/**
	 * M�todo que consulta los encabezados de cirug�a y preanestesia  para la hoja 
	 * de anestesia de cirug�as
	 * @param con
	 * @param listadoPeticiones
	 * @return
	 */
	public static HashMap consultarEncabezadosHojaAnestesia(Connection con, String listadoPeticiones) 
	{
		StringBuffer consultaBuffer=new StringBuffer();
		
		consultaBuffer.append("SELECT " +
							  "sc.codigo_peticion AS codigo_peticion, " +
							  "sc.numero_solicitud AS numero_solicitud," +
							  "getNombrePersona(sol.codigo_medico) AS profesional_solicita, " +
							  "tpac.nombre AS tipo_paciente, " +
				              "pqx.requiere_uci AS requiere_uci," +
				              "sol.consecutivo_ordenes_medicas AS consecutivo_ordenes_medicas," +
				              "to_char(sc.fecha_inicial_cx,'dd/mm/yyyy') AS fecha_inicial_cx," +
				              "sc.hora_inicial_cx AS hora_inicial_cx," +
				              "to_char(sc.fecha_final_cx,'dd/mm/yyyy') AS fecha_final_cx, " +
				              "sc.hora_final_cx AS hora_final_cx," +
				              "ha.hora_inicia_anestesia AS hora_inicio_anestesia," +
				              "ha.hora_finaliza_anestesia AS hora_fin_anestesia," +
				              "ta.descripcion AS tipo_anestesia," +
				              "administracion.getnombremedico(aha.profesional) AS medico_anestesiologo, " +
				              "to_char(pqx.fecha_peticion,'dd/mm/yyyy') AS fecha_peticion," +
				              "pqx.hora_peticion AS hora_peticion," +
				              "ep.nombre AS estado_peticion," +
				              "to_char(pqx.fecha_cirugia,'dd/mm/yyyy') AS fecha_estimada_cirugia," +
				              "pqx.duracion AS duracion_cirugia," +
				              "to_char(pre.fecha_preanestesia,'dd/mm/yyyy') AS fecha_preanestesia," +
				              "pre.hora_preanestesia AS hora_preanestesia," +
				              "getnombreusuario(pre.usuario) AS medico_preanestesia," +
				              "ha.observaciones_signos_vitales AS observaciones_signos_vitales, " +
				              "getobservhanes(sc.numero_solicitud) AS observaciones_hoja_anes," +
				              "coalesce(pre.observaciones_grales,'') AS observaciones_pre_anes  "+
				              "			 FROM solicitudes sol 	"+
				              "				INNER JOIN cuentas cue ON ( sol.cuenta = cue.id ) "+
							  "				INNER JOIN solicitudes_cirugia sc ON (sol.numero_solicitud=sc.numero_solicitud) "+
							  "				INNER JOIN peticion_qx pqx ON (pqx.codigo=sc.codigo_peticion) "+
							  "				LEFT OUTER JOIN  hoja_anestesia ha ON (ha.numero_solicitud=sol.numero_solicitud) "+
							  "				 LEFT OUTER JOIN anestesiologos_hoja_anes aha ON (aha.numero_solicitud=sc.numero_solicitud AND aha.definitivo='"+ConstantesBD.acronimoSi+"')    "+
							  "				LEFT OUTER JOIN  hoja_tipo_anestesia hta ON (hta.numero_solicitud=sol.numero_solicitud) "+
							  "				LEFT OUTER JOIN tipos_anestesia ta ON (hta.tipo_anestesia=ta.codigo) "+
							  "             INNER JOIN estados_peticion ep ON (pqx.estado_peticion   =ep.codigo)     " +
							  "				INNER JOIN tipos_paciente tpac ON ( cue.tipo_paciente = tpac.acronimo ) " +
							  "				LEFT OUTER JOIN valoracion_preanestesia pre ON (pqx.codigo=pre.peticion_qx) ");
		consultaBuffer.append("					WHERE  ("+UtilidadTexto.convertirVectorACodigosSeparadosXOr(UtilidadTexto.StringToVector(listadoPeticiones),"sc.codigo_peticion " ,false)+")");
		
		consultaBuffer.append("		ORDER BY sc.codigo_peticion");
		
		
		logger.info("EL NUEVO ERROR ES---->"+consultaBuffer);
			
		PreparedStatementDecorator stm = null;
		ResultSetDecorator rs = null;
		   try
			{
			stm= new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString()));
			rs = new ResultSetDecorator(stm.executeQuery());
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(rs);
			
			return mapaRetorno;
			}
			catch (SQLException e)
			{
				logger.error("Error en consultarEncabezadosHojaAnestesia [SqlBaseImpresionResumenAtencionesDao] "+e);
				return null;
			}	
		   finally{
			   UtilidadBD.cerrarObjetosPersistencia(stm, rs, null);
		   }
	}
	
	/**
	 * M�todo que consulta los ex�menes de laboratorio de preanestesia
	 * de acuerdo a la lista de peticiones que cumplen el filtro de impresion hc
	 * @param con
	 * @param listadoPeticiones
	 * @return
	 */
	public static HashMap consultarExamenesLaboratorioPreanestesia(Connection con, String listadoPeticiones) 
	{
		StringBuffer consultaBuffer=new StringBuffer();
		
		consultaBuffer.append(" SELECT * FROM "+
							  "		(SELECT vpel.val_preanestesia AS codigo_peticion, vpel.resultados AS resultados, vpel.observaciones AS observaciones, " +
							  "				0 AS orden, telp.nombre AS examen_laboratorio, vpel.datos_medico AS datos_medico "+
							  "		  		  FROM examen_lab_pre_inst elpi "+
							  "					INNER JOIN  tipos_examen_lab_pre telp ON (elpi.examen_lab_pre = telp.codigo) "+ 
							  "					INNER JOIN  val_pre_exam_lab_par vpelp    ON (vpelp.examen_lab_pre_inst=elpi.codigo)  "+
							  "					INNER JOIN  val_preanestesia_exam_lab vpel  ON (vpelp.val_exam_preanestesia=vpel.codigo)");
		consultaBuffer.append("			WHERE ("+UtilidadTexto.convertirVectorACodigosSeparadosXOr(UtilidadTexto.StringToVector(listadoPeticiones),"vpel.val_preanestesia" ,false)+")"  + "  AND elpi.activo = "+ValoresPorDefecto.getValorTrueParaConsultas());
		
		consultaBuffer.append(" UNION " +
							  " SELECT vpel.val_preanestesia AS codigo_peticion, vpel.resultados AS resultados, vpel.observaciones AS observaciones, " +
							  "		   1 AS orden, vpelo.nombre AS examen_laboratorio,vpel.datos_medico AS datos_medico " +
							  "			FROM val_preanestesia_exam_lab vpel " +
							  "			INNER JOIN val_pre_exam_lab_otro vpelo ON (vpelo.val_exam_preanestesia=vpel.codigo) "); 
		
		consultaBuffer.append("			  WHERE	("+UtilidadTexto.convertirVectorACodigosSeparadosXOr(UtilidadTexto.StringToVector(listadoPeticiones),"vpel.val_preanestesia" ,false)+")" );
		consultaBuffer.append("		 )x "+
							  "			ORDER BY x.codigo_peticion,x.orden");
		
			
		PreparedStatementDecorator stm = null; 
		ResultSetDecorator rs = null; 
	   try
		{
		stm= new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString()));
		rs = new ResultSetDecorator(stm.executeQuery());
		HashMap mapaRetorno=UtilidadBD.cargarValueObject(rs);
		
		return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error en consultarExamenesLaboratorioPreanestesia [SqlBaseImpresionResumenAtencionesDao] "+e);
			return null;
		}	
	   finally{
		   UtilidadBD.cerrarObjetosPersistencia(stm, rs, null);
	   }
	}
	
	/**
	 * M�todo que consulta el hist�rico de los ex�menes f�sicos de preanestesia, text y textarea
	 * de acuerdo a la lista de peticiones que cumplen el filtro de impresion hc
	 * @param con
	 * @param listadoPeticiones
	 * @param esTexto true (examenes fisico tipo text)
	 * 				  false (examenes fisico tipo text-area)
	 * @return
	 */
	public static HashMap consultarHistoExamenesFisicos(Connection con, String listadoPeticiones, boolean esTexto) 
	{
		StringBuffer consultaBuffer=new StringBuffer();
		
		//-------Si son los ex�menes f�sicos tipo text------//
		if(esTexto)
		{
			consultaBuffer.append("SELECT * FROM ( "+
								  "		SELECT vpeft.val_preanestesia AS codigo_peticion,tefp.nombre AS examen_fisico, vpeft.valor AS valor, 0 AS orden "+                  
								  "			FROM  val_pre_exam_fisico_text vpeft "+
								  "				INNER JOIN examen_fisico_pre_inst efpi ON (vpeft.examen_fisico_pre_inst=efpi.codigo) "+
								  "				INNER JOIN tipos_examen_fisico_pre tefp ON (efpi.examen_fisico_pre=tefp.codigo)" +
								  " 				 WHERE vpeft.val_preanestesia IN ("+listadoPeticiones+")");
			consultaBuffer.append(" 	UNION ALL "+       
								  " 	SELECT vp.val_preanestesia  AS codigo_peticion, vp.nombre_otro AS examen_fisico, vp.valor as valor, 1 AS orden "+             
								  "			FROM  val_pre_exam_fisico_otro vp " +
								  "				WHERE vp.val_preanestesia IN ("+listadoPeticiones+")");
			consultaBuffer.append(" 			)x " +
								  "		ORDER BY x.codigo_peticion,x.orden");
		}//if texto
		else
		{
			consultaBuffer.append("SELECT vpefa.val_preanestesia AS codigo_peticion,tefp.nombre AS examen_fisico, vpefa.valor AS valor "+
								  "		FROM  val_pre_exam_fisico_area vpefa "+
								  "			INNER JOIN examen_fisico_pre_inst efpi ON (vpefa.examen_fisico_pre_inst=efpi.codigo) "+
								  "			INNER JOIN tipos_examen_fisico_pre tefp ON (efpi.examen_fisico_pre=tefp.codigo)" +
								  "				WHERE vpefa.val_preanestesia IN ("+listadoPeticiones+")" +
								  "					ORDER BY vpefa.val_preanestesia");
		}
		
		
		   try
			{
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(stm.executeQuery()));
			stm.close();
			return mapaRetorno;
			}
			catch (SQLException e)
			{
				logger.error("Error en consultarHistoExamenesFisicos [SqlBaseImpresionResumenAtencionesDao] "+e);
				return null;
			}	
	}
	
	/**
	 * M�todo que consulta las conclusiones de preanestesia
	 * de acuerdo a la lista de peticiones que cumplen el filtro de impresion hc
	 * @param con
	 * @param listadoPeticiones
	 * @return
	 */
	public static HashMap consultarHistoConclusiones(Connection con, String listadoPeticiones) 
	{
		StringBuffer consultaBuffer=new StringBuffer();
		
		consultaBuffer.append("SELECT vpc.val_preanestesia AS codigo_peticion, tcp.nombre AS nombre_conclusion, vpc.conclusion AS valor_conclusion "+ 
						      "		FROM val_preanestesia_conclu vpc "+
							  "			INNER JOIN preanestesia_conclu_inst pci ON (vpc.tipo_conclu_pre_inst=pci.codigo) "+ 
							  "			INNER JOIN tipos_conclusion_pre tcp ON (pci.tipo_conclusion=tcp.codigo)" +
							  " 			WHERE vpc.val_preanestesia IN ("+listadoPeticiones+") AND pci.activo ="+ValoresPorDefecto.getValorTrueParaConsultas());
		
			
	   try
		{
		PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
		HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(stm.executeQuery()));
		stm.close();
		return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error en consultarHistoConclusiones [SqlBaseImpresionResumenAtencionesDao] "+e);
			return null;
		}	
	}
	
	/**
	 * M�todo que consulta el hist�rico de las secciones de la hoja de anestesia
	 * de acuerdo al parametro nroConsulta y a la lista de solicitudes que cumplen el filtro de impresion hc
	 * @param con
	 * @param listadoSolicitudes
	 * @param nroConsulta
	 * @return
	 */
	public static HashMap consultarHistoSeccionesHojaAnestesia(Connection con, String listadoSolicitudes, int nroConsulta)
	{
		StringBuffer consultaBuffer=new StringBuffer();
		
		//-Seleccionar el tipo de secci�n consultar
	    switch(nroConsulta)
		{
	    	//--------------------- Balance de L�quidos --------------------------//
	        case 1:
	        	consultaBuffer.append("SELECT habl.numero_solicitud AS numero_solicitud, bla.descripcion AS nombre_balance, habl.valor AS valor_balance "+ 
									  "		FROM hoja_anes_balance_liquidos habl "+
									  "  	  INNER JOIN balance_liquidos_anes_inst blai ON (habl.balance=blai.codigo) "+
									  "		  INNER JOIN balance_liquidos_anest bla ON (blai.balance_liquido=bla.codigo)" +
									  " 		 WHERE habl.numero_solicitud IN ("+listadoSolicitudes+") AND blai.activo="+ValoresPorDefecto.getValorTrueParaConsultas());
	        	break;
				
			//--------------------- Medicamentos -----------------------//			
		    case 2:
		    		consultaBuffer.append("SELECT dmha.numero_solicitud AS numero_solicitud, mha.descripcion AS nombre_medicamento, dmha.valor AS valor_medicamento "+ 
										  "		FROM detalle_med_hoja_anes dmha "+
										  " 	 	INNER JOIN medicamentos_hoja_anes_inst mhai ON (dmha.medicamento=mhai.codigo) "+ 
										  "			INNER JOIN medicamentos_hoja_anes mha ON (mhai.medicamento=mha.codigo)" +
										  "				WHERE dmha.numero_solicitud IN ("+listadoSolicitudes+") AND mhai.activo="+ValoresPorDefecto.getValorTrueParaConsultas());
		        	break;
					
		        default :
				{
					logger.warn(" [ERROR] No esta indicando ningun tipo de consulta el rango normal es [1-2]"+ nroConsulta + "\n\n" );
					return null;
				}
			}//switch
		
		
			
	   try
		{
		PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
		HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(stm.executeQuery()));
		stm.close();
		return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error en consultarHistoSeccionesHojaAnestesia [SqlBaseImpresionResumenAtencionesDao] Nro Consulta"+nroConsulta+" "+e);
			return null;
		}	
	}
	
	/**
	 * M�todo que consulta el hist�rico de las t�cnicas de anestesia 
	 * @param con
	 * @param listadoSolicitudes
	 * @return
	 */
	public static Collection consultarHistoTecnicaAnestesia(Connection con, String listadoSolicitudes)
	{
		StringBuffer consultaBuffer=new StringBuffer();
		
		consultaBuffer.append("SELECT dhat.tecnica AS codigo, dhat.valor AS valor, dhat.numero_solicitud AS numero_solicitud "+ 
							  "		FROM det_hoja_anes_tecnica dhat "+ 
							  "			INNER JOIN det_tec_anes_inst dtai ON  (dtai.codigo=dhat.tecnica)" +
							  "			   WHERE dhat.numero_solicitud IN ("+listadoSolicitudes+") AND dtai.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+
							  "				 ORDER BY dhat.numero_solicitud");
		
		
	   try
		{
		PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
		return UtilidadBD.resultSet2Collection(new ResultSetDecorator(stm.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error en consultarHistoTecnicaAnestesia [SqlBaseImpresionResumenAtencionesDao]"+e);
			return null;
		}	
	}
	
	/**
	 * M�todo que consulta el hist�rico de signos vitales, los tiempos y
	 * los valores de cada uno de los signos vitales
	 * @param con
	 * @param listadoSolicitudes
	 * @param nroConsulta  1-> Tiempos Signos Vitales
	 * 					   2-> Valores Signos Vitales	
	 * @return
	 */
	public static Collection consultarHistoSignosVitales(Connection con, String listadoSolicitudes, int nroConsulta) 
	{
		StringBuffer consultaBuffer=new StringBuffer();
		
		//--------Tiempos de los signos vitales--------//
		if(nroConsulta==1)
			consultaBuffer.append("SELECT tsva.solicitud_hoja_anest AS numero_solicitud,tsva.numero_tiempo AS numero_tiempo, " +
    							  "		  tsva.valor_tiempo as valor_tiempo,tsva.observaciones AS observaciones " +
    							  "			FROM tiempo_signo_vital_anest tsva " +
    							  "				WHERE tsva.solicitud_hoja_anest IN ("+listadoSolicitudes+")" +
    							  "					ORDER BY tsva.solicitud_hoja_anest,tsva.numero_tiempo");
		
		//--------Valores de cada uno de los signos vitales----------//
		if(nroConsulta==2)
			consultaBuffer.append("SELECT tsva.solicitud_hoja_anest AS numero_solicitud,tsva.numero_tiempo AS numero_tiempo,  " +
    							  "		  svat.signo_vital_anest_inst AS signo_vital_inst, svat.valor AS valor_signo_vital " +
    							  "			FROM tiempo_signo_vital_anest tsva " +
    							  "				INNER JOIN signo_vital_anes_tiempo svat ON (svat.tiempo_signo_vital_anest=tsva.codigo) " +
    							  "				INNER JOIN signo_vital_anest_inst svai ON (svat.signo_vital_anest_inst=svai.codigo) " +
    							  "					WHERE tsva.solicitud_hoja_anest IN ("+listadoSolicitudes+") AND svai.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+
    							  "						ORDER BY tsva.solicitud_hoja_anest,tsva.numero_tiempo");
		
		
	   try
		{
		PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
		return UtilidadBD.resultSet2Collection(new ResultSetDecorator(stm.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error en consultarHistoSignosVitales [SqlBaseImpresionResumenAtencionesDao]"+e);
			return null;
		}	
	}

	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public static HashMap obtenerEnlacesValoracionesConsultaExterna(Connection con, HashMap vo) 
	{
		String consulta="SELECT " +
			"sol.numero_solicitud AS numerosolicitud, " +
			"coalesce(sc.especialidad,ser.especialidad) AS codigo_especialidad, "+
			"gettiposervicio(sc.servicio) AS tipo_servicio " +
			"FROM solicitudes sol" +
			"INNER JOIN cuentas cu ON(sol.cuenta=cu.id) " +
			"INNER JOIN servicios_cita sc ON(sc.numero_solicitud=sol.numero_solicitud) " +
			"INNER JOIN servicios ser ON(ser.codigo=sc.servicio) " +
			"where ";
		
		
		//se filtra por cuenta o por ingreso
		if(!(vo.get("cuenta")+"").trim().equals(""))
		{
			consulta+=" cu.id='"+ vo.get("cuenta")+"' ";
		}
		else
		{
			consulta+=" cu.id_ingreso= "+vo.get("ingreso")+" ";
		}
		
		consulta += " AND sol.estado_historia_clinica IN ("+ConstantesBD.codigoEstadoHCRespondida+","+ConstantesBD.codigoEstadoHCInterpretada+") ";
		
		if((!(vo.get("fechaInicial")+"").trim().equals(""))&&(!(vo.get("fechaFinal")+"").trim().equals("")))
		{
			consulta+=" AND (sol.fecha_solicitud  between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"' )";
		}
		if((!(vo.get("horaInicial")+"").trim().equals(""))&&(!(vo.get("horaFinal")+"").trim().equals("")))
		{
			consulta+=" AND (  sol.fecha_solicitud >= '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and sol.hora_solicitud >= '"+vo.get("horaInicial")+"'   " +
						"and sol.fecha_solicitud <= '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"' and sol.hora_solicitud <= '"+vo.get("horaFinal")+"' ) ";
		}
		
		consulta+=	" order by sol.fecha_solicitud, sol.hora_solicitud ";
		
		logger.info("\n\n consulta obtenerEnlacesValoracionesConsultaExterna--->"+consulta);
		
		HashMap mapaEnlacesValCE= new HashMap();
		mapaEnlacesValCE.put("numRegistros", "0");
		
		PreparedStatementDecorator stm = null;
		ResultSetDecorator rs = null; 
		
		try
		{
			stm= new PreparedStatementDecorator(con.prepareStatement(consulta));
			rs = new ResultSetDecorator(stm.executeQuery());
			mapaEnlacesValCE=(HashMap) UtilidadBD.cargarValueObject(rs).clone();
		   
		}
		catch (SQLException e)
		{
			logger.error("Error en obtenerEnlacesValoracionesConsultaExterna [SqlBaseImpresionResumenAtencionesDao] "+e);
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(stm, rs, null);
		}
		
		return mapaEnlacesValCE;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap consultarOrdenesAmbulatorias(Connection con, HashMap vo) {
	
		String where="";
		
		//se filtra por cuenta o por ingreso
		if(!(vo.get("cuenta")+"").trim().equals(""))
			where+=" AND oa.cuenta_solicitante='"+ vo.get("cuenta")+"' ";
		else
			where+=" AND oa.ingreso= "+vo.get("ingreso")+" ";
		
		if((!(vo.get("fechaInicial")+"").trim().equals(""))&&(!(vo.get("fechaFinal")+"").trim().equals("")))
			where+=" AND (oa.fecha  between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"' )";
		
		if((!(vo.get("horaInicial")+"").trim().equals(""))&&(!(vo.get("horaFinal")+"").trim().equals("")))
			where+=" AND (  oa.fecha >= '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and oa.hora >= '"+vo.get("horaInicial")+"'   " +
						"and oa.fecha <= '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"' and oa.hora <= '"+vo.get("horaFinal")+"' ) ";
		
		String consulta = "( SELECT " +
								"oa.tipo_orden AS tipoorden, " +
								"oa.consecutivo_orden AS numero, " +
								"oa.fecha AS fecha, " +
								"oa.hora AS hora, " +
								"u.codigo_persona AS codprofesional, " +
								"getnombrepersona(u.codigo_persona) AS profesional, " +
								"administracion.getespecialidadesmedico(oa.usuario_solicita, ', ') AS especialidades, "+
								"oa.observaciones AS observaciones, " +
								"doaa.articulo AS codigo, " +
								"getdescarticulo(doaa.articulo) AS descripcion, " +
								"doaa.dosis||'' AS dosis, " +
								"tf.nombre AS nomtipofrecuencia, " +
								"doaa.frecuencia||'' AS frecuencia, " +
								"doaa.tipo_frecuencia||'' AS tipofrecuencia, " +
								"va.nombre AS nomvia, " +
								"doaa.cantidad AS cantidad, " +
								"doaa.duracion_tratamiento||'' AS duraciontratamiento, " +
								"doaa.observaciones AS indicaciones, " +
								"doaa.unidosis_articulo||'' AS unidosis," +
								"coalesce(roa.resultado,'') as resultado " +
							"FROM ordenes_ambulatorias oa " +
							"LEFT OUTER JOIN det_orden_amb_articulo doaa ON (oa.codigo = doaa.codigo_orden) " +
							"LEFT OUTER JOIN unidosis_x_articulo uxa ON (uxa.codigo = doaa.unidosis_articulo) " +
							"LEFT OUTER JOIN  resultado_orden_ambulatorias roa ON (roa.codigo_orden=oa.codigo) " +
							"INNER JOIN usuarios u ON (oa.usuario_solicita=u.login) " +
							"LEFT OUTER JOIN vias_administracion va ON (va.codigo = doaa.via) " +
							"LEFT OUTER JOIN tipos_frecuencia tf ON (tf.codigo = doaa.tipo_frecuencia) " +
							"WHERE tipo_orden=2 " + where +") "+
							"UNION " +
							"(SELECT " +
								"oa.tipo_orden AS tipoorden, " +
								"oa.consecutivo_orden AS numero, " +
								"oa.fecha AS fecha, " +
								"oa.hora AS hora, " +
								"u.codigo_persona AS codprofesional, " +
								"getnombrepersona(u.codigo_persona) AS profesional, " +
								"administracion.getespecialidadesmedico(oa.usuario_solicita, ', ') AS especialidades, "+
								"oa.observaciones AS observaciones, " +
								"doas.servicio AS codigo, " +
								"facturacion.getnombreservicio(doas.servicio, "+ConstantesBD.codigoTarifarioCups+") AS descripcion, " +
								"'' AS dosis, " +
								"'' AS nomtipofrecuencia, " +
								"'' AS frecuencia, " +
								"'' AS tipofrecuencia, " +
								"'' AS nomvia, " +
								"doas.cantidad AS cantidad, " +
								"'' AS duraciontratamiento, " +
								"'' AS indicaciones, " +
								"'' AS unidosis," +
								"coalesce(roa.resultado,'') as resultado " +
							"FROM ordenes_ambulatorias oa " +
							"LEFT OUTER JOIN  det_orden_amb_servicio doas ON (oa.codigo   = doas.codigo_orden) " +
							"LEFT OUTER JOIN  resultado_orden_ambulatorias roa ON (roa.codigo_orden=oa.codigo) " +
							"INNER JOIN usuarios u ON (oa.usuario_solicita=u.login) " +
							"WHERE tipo_orden=1 "+where+")";
		
		logger.info("\n\n consulta consultarOrdenesAmbulatorias--->"+consulta);
		
		HashMap mapaEnlacesValCE= new HashMap();
		mapaEnlacesValCE.put("numRegistros", "0");
		
		PreparedStatementDecorator stm = null;
		ResultSetDecorator rs = null;
		try
		{
			stm= new PreparedStatementDecorator(con.prepareStatement(consulta));
			rs = new ResultSetDecorator(stm.executeQuery());
			mapaEnlacesValCE=(HashMap) UtilidadBD.cargarValueObject(rs).clone();
		}
		catch (SQLException e)
		{
			logger.error("Error en consultarOrdenesAmbulatorias [SqlBaseImpresionResumenAtencionesDao] "+e);
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(stm, rs, null);
		}
		
		return mapaEnlacesValCE;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap obtenerConsultasPYP(Connection con, HashMap vo) {
		
		logger.info("Cuenta: "+vo.get("cuenta"));
		logger.info("Ingreso: "+vo.get("ingreso"));
		
		String where="s.pyp="+ValoresPorDefecto.getValorTrueParaConsultas()+" ";
		
		if(!(vo.get("cuenta")+"").trim().equals(""))
			where+=" AND s.cuenta="+ vo.get("cuenta")+" ";
		else
			where+=" AND s.cuenta IN (SELECT id FROM cuentas WHERE id_ingreso="+vo.get("ingreso")+") ";
		
		
		if((!(vo.get("fechaInicial")+"").trim().equals(""))&&(!(vo.get("fechaFinal")+"").trim().equals("")))
			where+=" AND (s.fecha_solicitud  between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"' )";
		
		if((!(vo.get("horaInicial")+"").trim().equals(""))&&(!(vo.get("horaFinal")+"").trim().equals("")))
			where+=" AND (  s.fecha_solicitud >= '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and s.hora_solicitud >= '"+vo.get("horaInicial")+"'   " +
						"and s.fecha_solicitud <= '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"' and s.hora_solicitud <= '"+vo.get("horaFinal")+"' ) ";
		
		String consulta = "SELECT " +
								"s.numero_solicitud, " +
								"getnombreservicio(ap.servicio, "+ConstantesBD.codigoTarifarioCups+") as actividad " +
							"FROM " +
								"solicitudes s " +
							"LEFT OUTER JOIN " +
								"act_prog_pyp_pac appp ON (appp.numero_solicitud=s.numero_solicitud) " +
							"LEFT OUTER JOIN " +
								"actividades_pyp ap ON (ap.consecutivo=appp.actividad) " +
							"WHERE "+where;
		
		logger.info("\n\n consulta obtenerConsultasPYP--->"+consulta);
		
		HashMap mapaEnlacesValCE= new HashMap();
		mapaEnlacesValCE.put("numRegistros", "0");
		
		try
		{
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapaEnlacesValCE=(HashMap) UtilidadBD.cargarValueObject(new ResultSetDecorator(stm.executeQuery())).clone();
		}
		catch (SQLException e)
		{
			logger.error("Error en obtenerConsultasPYP [SqlBaseImpresionResumenAtencionesDao] "+e);
		}
		
		return mapaEnlacesValCE;
	}
	
	

	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public static HashMap consultarResultatadosLaboratorios(Connection con,
			String idIngreso, String fechaInicial, String fechaFinal,
			String horaInicial, String horaFinal) {
		
		HashMap mapa=new HashMap();
		
		String cadenaRE = "SELECT " +
																" rle.CODIGO_HISTO_ENCA as codigohistoenca," +
																" coalesce(rle.CODIGO_CAMPO,-1) as codigocampo," +
																" rle.ETIQUIETA_CAMPO as etiquetacampo," +
																" rle.ORDEN as orden," +
																" rle.CENTRO_COSTO as centrocosto, " +
																" rle.VALOR as valor, " +
																" rle.CAMPO_OTRO as campootro, " +
																" to_char(ere.fecha_registro,'dd/mm/yyyy') as fecha, " +
																" substr(ere.hora_registro,0,5) as hora, " +
																" ere.usuario as loginusuario, " +
																" getdatosmedico(ere.usuario) as nombreusuario  " +
															" FROM enca_histo_registro_enfer ere " +
															" INNER JOIN registro_enfermeria re ON (ere.registro_enfer=re.codigo) " +
															" INNER JOIN MANEJOPACIENTE.resultado_laboratorio_regenf rle ON (rle.CODIGO_HISTO_ENCA=ere.codigo) " +
															" WHERE re.cuenta IN (SELECT id FROM cuentas WHERE id_ingreso=?   ) ";
		
		String cadenaO = "SELECT " +
																" rlo.CODIGO_HISTO_ENCA as codigohistoenca," +
																" coalesce(rlo.CODIGO_CAMPO,-1) as codigocampo," +
																" rlo.ETIQUIETA_CAMPO as etiquetacampo," +
																" rlo.ORDEN as orden," +
																" rlo.CENTRO_COSTO as centrocosto, " +
																" rlo.VALOR as valor, " +
																" rlo.CAMPO_OTRO as campootro, " +
																" to_char(eh.fecha_grabacion,'dd/mm/yyyy') as fecha, " +
																" substr(eh.hora_grabacion,0,5) as hora, " +
																" eh.login as loginusuario, " +
																" getdatosmedico(eh.login) as nombreusuario  " +
															" FROM encabezado_histo_orden_m eh  " +
															" INNER JOIN ordenes_medicas om ON (eh.orden_medica=om.codigo) " +
															" INNER JOIN manejopaciente.resultado_laboratorio_orden rlo ON (rlo.CODIGO_HISTO_ENCA=eh.codigo) " +
															" where om.cuenta in (select id from cuentas where id_ingreso=?)";

		
		
		String consulta = cadenaRE;
		
		//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
		if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
		{
			if(!horaInicial.equals("")&&!horaFinal.equals(""))
				consulta += " and (" +
					"ere.fecha_registro || '-' || ere.hora_registro  >= '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"-"+horaInicial+"' and " +
					"ere.fecha_registro || '-' || ere.hora_registro  <= '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"-"+horaFinal+"' ) ";
			else
				consulta += " and (ere.fecha_registro BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
			
		}
		
		
		consulta = consulta+" UNION "+cadenaO;
		
		//****SE VERIFICAN LOS FILTROS DE LAS FECHAS Y HORAS*************************
		if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
		{
			if(!horaInicial.equals("")&&!horaFinal.equals(""))
				consulta += " and (" +
					"eh.fecha_orden || '-' || eh.hora_orden  >= '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"-"+horaInicial+"' and " +
					"eh.fecha_orden || '-' || eh.hora_orden  <= '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"-"+horaFinal+"' ) ";
			else
				consulta += " and (eh.fecha_orden BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
			
		}
		
		consulta=" select * from ("+consulta+") tabla order by fecha desc,hora desc,campootro desc,orden asc";
		
		
		PreparedStatementDecorator ps= new PreparedStatementDecorator(con,consulta);
		ResultSetDecorator rs = null; 
		try 
		{
			ps.setInt(1, Utilidades.convertirAEntero(idIngreso));
			ps.setInt(2, Utilidades.convertirAEntero(idIngreso));
			rs = new ResultSetDecorator(ps.executeQuery());
			mapa=UtilidadBD.cargarValueObject(rs);
			ps.close();
		} 
		catch (SQLException e) 
		{
			Log4JManager.error("error",e);
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public static HashMap consultarrValoracionesEnfermeria(Connection con,String idIngreso, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal) 
	{
		
		HashMap mapa=new HashMap();
		String consulta="SELECT " +
						" CODIGO_HISTO_ENCA as codigohistoenca," +
						" coalesce(CODIGO_CAMPO,-1) as codigocampo," +
						" ETIQUIETA_CAMPO as etiquetacampo," +
						" coalesce(CODIGO_SECCION,-1) as codigoseccion," +
						" ETIQUETA_SECCION as etiquetaseccion," +
						" CASE WHEN (orden_seccion is null or orden_seccion <=0)then 9999999  else orden_seccion end  as ordenseccion,"+
						" ORDEN_CAMPO as ordencampo," +
						" CENTRO_COSTO as centrocosto," +
						" VALOR as valor," +
						" CAMPO_OTRO as campootro," +
						" to_char(eh.fecha_registro,'dd/mm/yyyy') as fecha," +
						" substr(eh.hora_registro,0,5) as hora," +
						" usuario as loginusuario," +
						" getdatosmedico(usuario) as nombreusuario " +
				" FROM enca_histo_registro_enfer eh  " +
				" INNER JOIN registro_enfermeria re ON (eh.registro_enfer=re.codigo) " +
				" INNER JOIN manejopaciente.valoracion_enfermeria ve ON (ve.CODIGO_HISTO_ENCA=eh.codigo)  " +
				" WHERE EXISTS (SELECT id FROM cuentas WHERE re.cuenta = id AND id_ingreso=?)";
		
		if(!fechaInicial.equals("")&&!fechaFinal.equals(""))
		{
			if(!horaInicial.equals("")&&!horaFinal.equals(""))
				consulta += " and (" +
					"eh.fecha_registro || '-' || eh.hora_registro  >= '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"-"+horaInicial+"' and " +
					"eh.fecha_registro || '-' || eh.hora_registro  <= '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"-"+horaFinal+"' ) ";
			else
				consulta += " and (eh.fecha_registro BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"') ";
			
		}
			
		
		consulta=consulta+" order by ordenseccion asc,codigohistoenca desc,campootro desc, ordencampo asc ";
		PreparedStatementDecorator ps= new PreparedStatementDecorator(con,consulta);
		ResultSetDecorator rs = null;
		try 
		{
			ps.setInt(1, Utilidades.convertirAEntero(idIngreso));
			rs = new ResultSetDecorator(ps.executeQuery());
			mapa=UtilidadBD.cargarValueObject(rs);
		} 
		catch (SQLException e) 
		{
			Log4JManager.error("error",e);
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		return mapa;
	}
	

	/**
	 * 
	 * @param con
	 * @param filtro
	 * @return
	 */
	/**
	* Tipo Modificacion: Segun incidencia 5055
	* Autor: Alejandro Aguirre Luna
	* usuario: aleagulu
	* Fecha: 11/02/2013
	* Descripcion: Se le quita la anotacion @Deprecated al metodo 
	* 			   debido a que si esta en uso su utilizaciòn. 
	**/
	public static ArrayList<DtoResultadoImpresionHistoriaClinica> consultarInformacionHistoriaClinica(Connection con, DtoFiltroImpresionHistoriaClinica filtro,int identificadorSeccion) 
	{
		ArrayList<DtoResultadoImpresionHistoriaClinica> resultado=new ArrayList<DtoResultadoImpresionHistoriaClinica>();
		
		String consulta="SELECT " +
							"tabla.fecha, " +
							"tabla.fechabd, " +
							"tabla.hora, " +
							"tabla.codigopk, " +
							"tabla.codigotipoevolucion "+ 
						"FROM" +
							"( ";
		boolean esPrimero=true;
		Boolean existenDatosAConsultar=false;
		if(identificadorSeccion==ConstantesImpresionHistoriaClinica.codigoSeccionEvoluciones)
		{
			
			
			if(filtro.isImprimirValUrgencias())
			{
				existenDatosAConsultar=true;
				if(!esPrimero)
					consulta+=" UNION ";
				//VALORACIONES INICIALES
				consulta+="(";
				consulta+=" SELECT " +
								"to_char(v.fecha_valoracion, 'DD/MM/YYYY') as fecha, " +
								"v.fecha_valoracion as fechabd, " +
								"substr(v.hora_valoracion,1,5) as hora, " +
								"s.numero_solicitud as codigopk, " +
								""+ConstantesImpresionHistoriaClinica.tipoEvolucionValoracionUrgencias+" as codigotipoevolucion, " +
								"1 as ordentipoevolucion " +
							"FROM " +
								"solicitudes s	" +
								"INNER JOIN valoraciones v ON(s.numero_solicitud=v.numero_solicitud)  " +
								"inner join cuentas c ON(c.id=s.cuenta) " +
								"inner join tipos_solicitud t on(t.codigo=s.tipo) " +
								"inner join tipos_paciente tp on(tp.acronimo=c.tipo_paciente) " +
							"WHERE  " +
								"s.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") " +
								"and s.tipo = "+ConstantesBD.codigoTipoSolicitudInicialUrgencias+" " ;
				consulta+=") ";
				esPrimero=false;
			}
			if(filtro.isImprimirValHospitalizacion())
			{
				existenDatosAConsultar=true;
				if(!esPrimero)
					consulta+=" UNION ";
				//VALORACIONES INICIALES
				consulta+="(";
				consulta+=" SELECT " +
								"to_char(v.fecha_valoracion, 'DD/MM/YYYY') as fecha, " +
								"v.fecha_valoracion as fechabd, " +
								"substr(v.hora_valoracion,1,5) as hora, " +
								"s.numero_solicitud as codigopk, " +
								""+ConstantesImpresionHistoriaClinica.tipoEvolucionValoracionHospitalizacion+" as codigotipoevolucion, " +
								"1 as ordentipoevolucion " +
							"FROM " +
								"solicitudes s	" +
								"INNER JOIN valoraciones v ON(s.numero_solicitud=v.numero_solicitud)  " +
								"inner join cuentas c ON(c.id=s.cuenta) " +
								"inner join tipos_solicitud t on(t.codigo=s.tipo) " +
								"inner join tipos_paciente tp on(tp.acronimo=c.tipo_paciente) " +
							"WHERE  " +
								"s.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") " +
								"and s.tipo = "+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+" " +
								"and c.tipo_paciente='"+ConstantesBD.tipoPacienteHospitalizado+"'" ;
				consulta+=") ";
				esPrimero=false;
			}
			
			if(filtro.isImprimirRespuestaInterpretacionInterconsulta())
			{
				existenDatosAConsultar=true;
				if(!esPrimero)
					consulta+=" UNION ";
				consulta+="(";
				consulta+=" SELECT " +
								"to_char(v.fecha_valoracion, 'DD/MM/YYYY') as fecha, " +
								"v.fecha_valoracion as fechabd, " +
								"substr(v.hora_valoracion,1,5) as hora, " +
								"si.numero_solicitud as codigopk, " +
								""+ConstantesImpresionHistoriaClinica.tipoEvolucionRespuestaInterpretacionInterconsulta+" as codigotipoevolucion, " +
								"2 as ordentipoevolucion " +
							"FROM " +
								"solicitudes_inter si	" +
								"INNER JOIN solicitudes s ON(s.numero_solicitud=si.numero_solicitud) " +
								"INNER JOIN valoraciones v ON(s.numero_solicitud=v.numero_solicitud)  " +
							"WHERE  " +
								"s.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") ";
				if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && UtilidadTexto.isEmpty(filtro.getHoraInicial()) && UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND v.fecha_valoracion  between '"+filtro.getFechaInicial()+"' and '"+filtro.getFechaFinal()+"'";
				}
				else if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && !UtilidadTexto.isEmpty(filtro.getHoraInicial()) && !UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND ( ( v.fecha_valoracion > '"+filtro.getFechaInicial()+"') or ( v.fecha_valoracion = '"+filtro.getFechaInicial()+"' and  v.hora_valoracion >= '"+filtro.getHoraInicial()+"') ) " +
								" and (( v.fecha_valoracion < '"+filtro.getFechaFinal()+"') or ( v.fecha_valoracion= '"+filtro.getFechaFinal()+"' and  v.hora_valoracion <= '"+filtro.getHoraFinal()+"')) ";
				}
				consulta+=	" and s.estado_historia_clinica in( "+ConstantesBD.codigoEstadoHCInterpretada+", "+ConstantesBD.codigoEstadoHCRespondida+" ) ";
				consulta+=") ";
				esPrimero=false;
			}
			
			if(filtro.isImprimirEvoluciones())
			{
				existenDatosAConsultar=true;
				if(!esPrimero)
					consulta+=" UNION ";
				consulta+="(";
				consulta+=" SELECT " +
								"to_char(e.fecha_evolucion, 'DD/MM/YYYY') as fecha, " +
								"e.fecha_evolucion as fechabd, " +
								"substr(e.hora_evolucion||'', 1,5) as hora, " +
								"e.codigo as codigopk, " +
								""+ConstantesImpresionHistoriaClinica.tipoEvolucionEvoluciones+" as codigotipoevolucion, " +
								"3 as ordentipoevolucion " +
							"FROM " +
								"evoluciones e " +
								"INNER JOIN solicitudes s ON(s.numero_solicitud=e.valoracion) " +
							"WHERE  " +
								"s.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") ";
				if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && UtilidadTexto.isEmpty(filtro.getHoraInicial()) && UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND e.fecha_evolucion  between '"+filtro.getFechaInicial()+"' and '"+filtro.getFechaFinal()+"'";
				}
				else if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && !UtilidadTexto.isEmpty(filtro.getHoraInicial()) && !UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND ( ( e.fecha_evolucion > '"+filtro.getFechaInicial()+"') or ( e.fecha_evolucion = '"+filtro.getFechaInicial()+"' and  e.hora_evolucion >= '"+filtro.getHoraInicial()+"') ) " +
								" and (( e.fecha_evolucion < '"+filtro.getFechaFinal()+"') or ( e.fecha_evolucion= '"+filtro.getFechaFinal()+"' and  e.hora_evolucion <= '"+filtro.getHoraFinal()+"')) ";
				}
				consulta+=" and s.estado_historia_clinica <> "+ConstantesBD.codigoEstadoHCAnulada+" ";
				consulta+=") ";
				esPrimero=false;
			}
			
			if(filtro.isImprimirValoracionesConsultaExterna())
			{
				existenDatosAConsultar=true;
				if(!esPrimero)
					consulta+=" UNION ";
				consulta+="(";
				consulta+=" SELECT " +
								"to_char(sol.fecha_solicitud, 'DD/MM/YYYY') as fecha, " +
								"sol.fecha_solicitud as fechabd, " +
								"substr(sol.hora_solicitud,1,5) as hora, " +
								"sol.numero_solicitud as codigopk, " +
								""+ConstantesImpresionHistoriaClinica.tipoEvolucionValoracionConsultaExterna+" as codigotipoevolucion, " +
								"4 as ordentipoevolucion " +
							"FROM " +
								"solicitudes sol	" +
								"INNER JOIN servicios_cita sc ON(sc.numero_solicitud=sol.numero_solicitud) " +
								"INNER JOIN servicios ser ON(ser.codigo=sc.servicio) " +
							"WHERE  " +
								"sol.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") ";
				if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && UtilidadTexto.isEmpty(filtro.getHoraInicial()) && UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND sol.fecha_solicitud  between '"+filtro.getFechaInicial()+"' and '"+filtro.getFechaFinal()+"'";
				}
				else if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && !UtilidadTexto.isEmpty(filtro.getHoraInicial()) && !UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND ( ( sol.fecha_solicitud > '"+filtro.getFechaInicial()+"') or ( sol.fecha_solicitud = '"+filtro.getFechaInicial()+"' and  sol.hora_solicitud >= '"+filtro.getHoraInicial()+"') ) " +
								" and (( sol.fecha_solicitud < '"+filtro.getFechaFinal()+"') or ( sol.fecha_solicitud= '"+filtro.getFechaFinal()+"' and  sol.hora_solicitud <= '"+filtro.getHoraFinal()+"')) ";
				}
				consulta+=	" and sol.estado_historia_clinica in( "+ConstantesBD.codigoEstadoHCInterpretada+", "+ConstantesBD.codigoEstadoHCRespondida+" ) ";
				consulta+=") ";
				esPrimero=false;
			}
			if(filtro.isImprimirOrdenesMedicas())
			{
				existenDatosAConsultar=true;
				//procedimientos.
				if(!esPrimero)
					consulta+=" UNION ";
				consulta+="(";
				consulta+=" SELECT " +
								"to_char(sol.fecha_solicitud, 'DD/MM/YYYY') as fecha, " +
								"sol.fecha_solicitud as fechabd, " +
								"substr(sol.hora_solicitud,1,5) as hora, " +
								"sol.numero_solicitud as codigopk, " +
								""+ConstantesImpresionHistoriaClinica.tipoEvolucionSolicitudesProcedimientos+" as codigotipoevolucion, " +
								"5 as ordentipoevolucion " +
							"FROM " +
								" solicitudes sol " +
								" inner join sol_procedimientos sp on(sp.numero_solicitud=sol.numero_solicitud) " +
							"WHERE  " +
								"sol.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") ";
				if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && UtilidadTexto.isEmpty(filtro.getHoraInicial()) && UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND sol.fecha_solicitud  between '"+filtro.getFechaInicial()+"' and '"+filtro.getFechaFinal()+"'";
				}
				else if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && !UtilidadTexto.isEmpty(filtro.getHoraInicial()) && !UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND ( ( sol.fecha_solicitud > '"+filtro.getFechaInicial()+"') or ( sol.fecha_solicitud = '"+filtro.getFechaInicial()+"' and  sol.hora_solicitud >= '"+filtro.getHoraInicial()+"') ) " +
								" and (( sol.fecha_solicitud < '"+filtro.getFechaFinal()+"') or ( sol.fecha_solicitud= '"+filtro.getFechaFinal()+"' and  sol.hora_solicitud <= '"+filtro.getHoraFinal()+"')) ";
				}
				consulta+=	" and sol.tipo='"+ConstantesBD.codigoTipoSolicitudProcedimiento+"' and sol.estado_historia_clinica<>"+ConstantesBD.codigoEstadoHCAnulada+"  ";
				consulta+=") ";
				esPrimero=false;
				
				//medicamentos
				if(!esPrimero)
					consulta+=" UNION ";
				consulta+="(";
				consulta+=" SELECT " +
								"to_char(sol.fecha_solicitud, 'DD/MM/YYYY') as fecha, " +
								"sol.fecha_solicitud as fechabd, " +
								"substr(sol.hora_solicitud,1,5) as hora, " +
								"sol.numero_solicitud as codigopk, " +
								""+ConstantesImpresionHistoriaClinica.tipoEvolucionSolicitudesMedicamentos+" as codigotipoevolucion, " +
								"6 as ordentipoevolucion " +
							"FROM " +
								" solicitudes sol " +
								" inner join solicitudes_medicamentos sm on (sol.numero_solicitud=sm.numero_solicitud) " +
							"WHERE  " +
								"sol.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") ";
				if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && UtilidadTexto.isEmpty(filtro.getHoraInicial()) && UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND sol.fecha_solicitud  between '"+filtro.getFechaInicial()+"' and '"+filtro.getFechaFinal()+"'";
				}
				else if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && !UtilidadTexto.isEmpty(filtro.getHoraInicial()) && !UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND ( ( sol.fecha_solicitud > '"+filtro.getFechaInicial()+"') or ( sol.fecha_solicitud = '"+filtro.getFechaInicial()+"' and  sol.hora_solicitud >= '"+filtro.getHoraInicial()+"') ) " +
								" and (( sol.fecha_solicitud < '"+filtro.getFechaFinal()+"') or ( sol.fecha_solicitud= '"+filtro.getFechaFinal()+"' and  sol.hora_solicitud <= '"+filtro.getHoraFinal()+"')) ";
				}
				consulta+=	"  and sol.tipo='"+ConstantesBD.codigoTipoSolicitudMedicamentos+"' and sol.estado_historia_clinica<>"+ConstantesBD.codigoEstadoHCAnulada+"  ";
				consulta+=") ";
				esPrimero=false;
				
				//cirugias
				if(!esPrimero)
					consulta+=" UNION ";
				consulta+="(";
				consulta+=" SELECT " +
								"to_char(sol.fecha_solicitud, 'DD/MM/YYYY') as fecha, " +
								"sol.fecha_solicitud as fechabd, " +
								"substr(sol.hora_solicitud,1,5) as hora, " +
								"sol.numero_solicitud as codigopk, " +
								""+ConstantesImpresionHistoriaClinica.tipoEvolucionSolicitudesCirugias+" as codigotipoevolucion, " +
								"7 as ordentipoevolucion " +
							"FROM " +
								" solicitudes sol " +
								" inner join solicitudes_cirugia sc ON(sc.numero_solicitud = sol.numero_solicitud AND (sc.ind_qx = '"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia+"' OR sc.ind_qx = '"+ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento+"')) " +
							"WHERE  " +
								"sol.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") ";
				if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && UtilidadTexto.isEmpty(filtro.getHoraInicial()) && UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND sol.fecha_solicitud  between '"+filtro.getFechaInicial()+"' and '"+filtro.getFechaFinal()+"'";
				}
				else if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && !UtilidadTexto.isEmpty(filtro.getHoraInicial()) && !UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND ( ( sol.fecha_solicitud > '"+filtro.getFechaInicial()+"') or ( sol.fecha_solicitud = '"+filtro.getFechaInicial()+"' and  sol.hora_solicitud >= '"+filtro.getHoraInicial()+"') ) " +
								" and (( sol.fecha_solicitud < '"+filtro.getFechaFinal()+"') or ( sol.fecha_solicitud= '"+filtro.getFechaFinal()+"' and  sol.hora_solicitud <= '"+filtro.getHoraFinal()+"')) ";
				}
				consulta+=	" and sol.tipo='"+ConstantesBD.codigoTipoSolicitudCirugia+"' and sol.estado_historia_clinica<>"+ConstantesBD.codigoEstadoHCAnulada+"  ";
				consulta+=") ";
				esPrimero=false;
				
				//interconsultas
				if(!esPrimero)
					consulta+=" UNION ";
				consulta+="(";
				consulta+=" SELECT " +
								"to_char(sol.fecha_solicitud, 'DD/MM/YYYY') as fecha, " +
								"sol.fecha_solicitud as fechabd, " +
								"substr(sol.hora_solicitud,1,5) as hora, " +
								"sol.numero_solicitud as codigopk, " +
								""+ConstantesImpresionHistoriaClinica.tipoEvolucionSolicitudesInterconsultas+" as codigotipoevolucion, " +
								"8 as ordentipoevolucion " +
							"FROM " +
								" solicitudes sol " +
								" inner join solicitudes_inter si On(si.numero_solicitud=sol.numero_solicitud) " +
							"WHERE  " +
								"sol.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") ";
				if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && UtilidadTexto.isEmpty(filtro.getHoraInicial()) && UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND sol.fecha_solicitud  between '"+filtro.getFechaInicial()+"' and '"+filtro.getFechaFinal()+"'";
				}
				else if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && !UtilidadTexto.isEmpty(filtro.getHoraInicial()) && !UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND ( ( sol.fecha_solicitud > '"+filtro.getFechaInicial()+"') or ( sol.fecha_solicitud = '"+filtro.getFechaInicial()+"' and  sol.hora_solicitud >= '"+filtro.getHoraInicial()+"') ) " +
								" and (( sol.fecha_solicitud < '"+filtro.getFechaFinal()+"') or ( sol.fecha_solicitud= '"+filtro.getFechaFinal()+"' and  sol.hora_solicitud <= '"+filtro.getHoraFinal()+"')) ";
				}
				consulta+=	" and sol.tipo='"+ConstantesBD.codigoTipoSolicitudInterconsulta+"' and sol.estado_historia_clinica<>"+ConstantesBD.codigoEstadoHCAnulada+"  ";
				consulta+=") ";
				esPrimero=false;
				
				//informacion de orden medica
				if(!esPrimero)
					consulta+=" UNION ";
				consulta+="(";
				consulta+=" SELECT " +
								"to_char(ehom.fecha_orden, 'DD/MM/YYYY') as fecha, " +
								"ehom.fecha_orden as fechabd, " +
								"substr(ehom.hora_orden,1,5) as hora, " +
								"ehom.codigo as codigopk, " +
								""+ConstantesImpresionHistoriaClinica.tipoEvolucionOrdenesMedicas+" as codigotipoevolucion, " +
								"9 as ordentipoevolucion " +
							" from ordenes_medicas om " +
							" inner join hoja_neurologica_orden_m hnom on(hnom.orden_medica=om.codigo)" +
							" inner join encabezado_histo_orden_m ehom on(ehom.orden_medica=om.codigo) " +
							"WHERE  " +
								"om.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") ";
				if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && UtilidadTexto.isEmpty(filtro.getHoraInicial()) && UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND ehom.fecha_orden  between '"+filtro.getFechaInicial()+"' and '"+filtro.getFechaFinal()+"'";
				}
				else if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && !UtilidadTexto.isEmpty(filtro.getHoraInicial()) && !UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND ( ( ehom.fecha_orden > '"+filtro.getFechaInicial()+"') or ( ehom.fecha_orden = '"+filtro.getFechaInicial()+"' and  ehom.hora_orden >= '"+filtro.getHoraInicial()+"') ) " +
								" and (( ehom.fecha_orden < '"+filtro.getFechaFinal()+"') or ( ehom.fecha_orden= '"+filtro.getFechaFinal()+"' and  ehom.hora_orden <= '"+filtro.getHoraFinal()+"')) ";
				}
				consulta+=") ";
				esPrimero=false;
			}
			if(filtro.isImprimirCirugia())
			{
				existenDatosAConsultar=true;
				if(!esPrimero){
					consulta+=" UNION ";
				}
				consulta+="(";
				
				consulta+="  SELECT  TO_CHAR(sol.fecha_solicitud,'DD/MM/YYYY') AS fecha, " +
						"  coalesce(sc.fecha_inicial_cx,sol.FECHA_GRABACION)  AS fechabd, " +
						"  coalesce( SUBSTR(sc.hora_inicial_cx,1,5),SUBSTR(sol.hora_grabacion,1,5)) AS hora, " +
						" sol.numero_solicitud           AS codigopk," +
						" "+ConstantesImpresionHistoriaClinica.codigoSeccionCirugias+" AS codigotipoevolucion," +
						" 10                           AS ordentipoevolucion " +
						" FROM solicitudes_cirugia sc " +
						" LEFT OUTER JOIN hoja_quirurgica hq " +
						" ON (sc.numero_solicitud=hq.numero_solicitud) " +
						" LEFT OUTER JOIN hoja_anestesia ha " +
						" ON (ha.numero_solicitud=sc.numero_solicitud) " +
						" INNER JOIN solicitudes sol " +
						" ON (sol.numero_solicitud=sc.numero_solicitud) " +
						" LEFT OUTER JOIN res_sol_proc res " +
						" ON (res.numero_solicitud=sc.numero_solicitud) " +
						" WHERE sol.cuenta        in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") " +
						" AND sc.ind_qx          IN ('CX','NCTO')";
				consulta+=") ";
				esPrimero=false;
				
				
			}
				

			consulta+=") tabla order by ";
			consulta+=" tabla.fechabd asc, tabla.hora asc, tabla.ordentipoevolucion ";
		}
		/*
		 * ESTE ORDENAMIENTO DE LAS SECCION ENFERMERIA YA NO APLICA, SEGUN LO QUE CUADRO NURY CON SUSANA, EL ORDENAMIENTO DE ENFERMERIA VA POR TIPO DE SERVICIO NO POR FECHA.
		else if(identificadorSeccion==ConstantesImpresionHistoriaClinica.codigoSeccionEnfermeria)
		{
			if(filtro.isImprimirValoracionesEnfermeria())
			{
				if(!esPrimero)
					consulta+=" UNION ";
				consulta+="(";
				consulta+=" SELECT distinct " +
								"to_char(eh.fecha_registro,'dd/mm/yyyy') as fecha, " +
								"eh.fecha_registro as fechabd, " +
								"substr(eh.hora_registro,1,5) as hora, " +
								ConstantesBD.codigoNuncaValido+" as codigopk, " +
								""+ConstantesImpresionHistoriaClinica.tipoEvolucionValoracionEnfermeria+" as codigotipoevolucion, " +
								"1 as ordentipoevolucion " +
							" FROM manejopaciente.valoracion_enfermeria ve " +
							" inner join enca_histo_registro_enfer eh on (ve.CODIGO_HISTO_ENCA=eh.codigo) " +
							" INNER JOIN registro_enfermeria re ON (eh.registro_enfer=re.codigo) " +
							" where re.cuenta in ("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") ";
				if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && UtilidadTexto.isEmpty(filtro.getHoraInicial()) && UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND eh.fecha_registro  between '"+filtro.getFechaInicial()+"' and '"+filtro.getFechaFinal()+"'";
				}
				else if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && !UtilidadTexto.isEmpty(filtro.getHoraInicial()) && !UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND ( ( eh.fecha_registro > '"+filtro.getFechaInicial()+"') or ( eh.fecha_registro = '"+filtro.getFechaInicial()+"' and  eh.hora_registro >= '"+filtro.getHoraInicial()+"') ) " +
								" and (( eh.fecha_registro < '"+filtro.getFechaFinal()+"') or ( eh.fecha_registro= '"+filtro.getFechaFinal()+"' and  eh.hora_registro <= '"+filtro.getHoraFinal()+"')) ";
				}
				consulta+=") ";
				esPrimero=false;			
				
			}
			
			if(filtro.isImprimirSignosVitales())
			{
				if(!esPrimero)
					consulta+=" UNION ";
				consulta+="(";
				consulta+=" SELECT distinct " +
								" to_char(ehre.fecha_registro, 'dd/mm/yyyy') as fecha, " +
								"ehre.fecha_registro as fechabd, " +
								"substr(ehre.hora_registro,1,5) as hora, " +
								ConstantesBD.codigoNuncaValido+" as codigopk, " +
								""+ConstantesImpresionHistoriaClinica.tipoEvolucionSignosVitales+" as codigotipoevolucion, " +
								"2 as ordentipoevolucion " +
							"FROM  enca_histo_registro_enfer ehre " +
							"INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo)  " +
							"INNER JOIN signo_vit_reg_enfer_par svrep ON (ehre.codigo=svrep.codigo_histo_enca) " +
							"WHERE  " +
								"re.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") ";
				if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && UtilidadTexto.isEmpty(filtro.getHoraInicial()) && UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND ehre.fecha_registro  between '"+filtro.getFechaInicial()+"' and '"+filtro.getFechaFinal()+"'";
				}
				else if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && !UtilidadTexto.isEmpty(filtro.getHoraInicial()) && !UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND ( ( ehre.fecha_registro > '"+filtro.getFechaInicial()+"') or ( ehre.fecha_registro = '"+filtro.getFechaInicial()+"' and  ehre.hora_registro >= '"+filtro.getHoraInicial()+"') ) " +
								" and (( ehre.fecha_registro < '"+filtro.getFechaFinal()+"') or ( ehre.fecha_registro= '"+filtro.getFechaFinal()+"' and  ehre.hora_registro <= '"+filtro.getHoraFinal()+"')) ";
				}
				consulta+=" UNION ";
				
				consulta+=" SELECT distinct " +
								" to_char(ehre.fecha_registro, 'dd/mm/yyyy') as fecha, " +
								"ehre.fecha_registro as fechabd, " +
								"substr(ehre.hora_registro,1,5) as hora, " +
								ConstantesBD.codigoNuncaValido+" as codigopk, " +
								""+ConstantesImpresionHistoriaClinica.tipoEvolucionSignosVitales+" as codigotipoevolucion, " +
								"2 as ordentipoevolucion " +
							"FROM  enca_histo_registro_enfer ehre " +
							"INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo)  " +
							"INNER JOIN signo_vital_reg_enfer svrep ON (ehre.codigo=svrep.codigo_histo_enfer) " +
							"WHERE  " +
								"re.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") ";
				if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && UtilidadTexto.isEmpty(filtro.getHoraInicial()) && UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND ehre.fecha_registro  between '"+filtro.getFechaInicial()+"' and '"+filtro.getFechaFinal()+"'";
				}
				else if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && !UtilidadTexto.isEmpty(filtro.getHoraInicial()) && !UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND ( ( ehre.fecha_registro > '"+filtro.getFechaInicial()+"') or ( ehre.fecha_registro = '"+filtro.getFechaInicial()+"' and  ehre.hora_registro >= '"+filtro.getHoraInicial()+"') ) " +
								" and (( ehre.fecha_registro < '"+filtro.getFechaFinal()+"') or ( ehre.fecha_registro= '"+filtro.getFechaFinal()+"' and  ehre.hora_registro <= '"+filtro.getHoraFinal()+"')) ";
				}
				
				consulta+=") ";
				
				
				esPrimero=false;
				
				
			}
			
			consulta+=") tabla order by ";
			consulta+=" tabla.fechabd asc, tabla.hora asc, tabla.ordentipoevolucion ";

		}
		 */

		PreparedStatement ps=null;
		ResultSet rs= null;
		try 
		{
			/*
			 * Dado que no es necesario actualizar los datos a medida que se va recorriendo, no veo la necesidad de hacer esto.. ademas, que puede
			 * generar excepciones como SQLException : Unsupported syntax for refreshRow() cuando son mas de 10 registros y algunas BD no lo soportan 			 
			 */			
			//ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps= con.prepareStatement(consulta);			
			if (existenDatosAConsultar) {
				rs=ps.executeQuery();
				
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				while(rs.next())
				{
					DtoResultadoImpresionHistoriaClinica dto=new DtoResultadoImpresionHistoriaClinica();
					dto.setFecha(rs.getString("fecha"));
					dto.setFechaBD(format.format(rs.getDate("fechabd")));
					dto.setHora(rs.getString("hora"));
					dto.setCodigoPk(rs.getInt("codigopk"));
					dto.setCodigoTipoEvolucion(rs.getInt("codigotipoevolucion"));
					resultado.add(dto);
				}			
			}
			
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally{
			if(ps != null){
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}		
		
		return resultado;
	}
	
	/** Tipo Modificacion: Segun incidencia 6493
	* Funcion: consultarInformacionHistoriaClinica
	* Autor: Alejandro Aguirre Luna
	* Fecha: 15/02/2013
	* Descripcion: Sobrecarga del metodo consultarInformacionHistoriaClinica
	* 	       con el argumento adicional paciente de tipo PersonaBasica
	* 	       el cual sirve para obtener posteriormente en la clase 
	* 	       SqlBaseImpresionResumenAtencionesDao el 
	* 	       tipoPacienteHospitalizado el cual puede ser de tipo; H,A,C.
	* Argumentos:
	* 	- Connection con	
	* 	- DtoFiltroImpresionHistoriaClinica filtro
	* 	- int identificadorSeccion
	* 	- PersonaBasica paciente
	* Retorno: ArrayList<DtoResultadoImpresionHistoriaClinica>
	* Excepciones: 
	**/
	public static ArrayList<DtoResultadoImpresionHistoriaClinica> consultarInformacionHistoriaClinica(Connection con, DtoFiltroImpresionHistoriaClinica filtro,int identificadorSeccion, PersonaBasica paciente) 
	{
		ArrayList<DtoResultadoImpresionHistoriaClinica> resultado=new ArrayList<DtoResultadoImpresionHistoriaClinica>();
		
		String consulta="SELECT " +
							"tabla.fecha, " +
							"tabla.fechabd, " +
							"tabla.hora, " +
							"tabla.codigopk, " +
							"tabla.codigotipoevolucion "+ 
						"FROM" +
							"( ";
		boolean esPrimero=true;
		Boolean existenDatosAConsultar=false;
		if(identificadorSeccion==ConstantesImpresionHistoriaClinica.codigoSeccionEvoluciones)
		{
			if(filtro.isImprimirValUrgencias())
			{
				existenDatosAConsultar=true;
				if(!esPrimero)
					consulta+=" UNION ";
				//VALORACIONES INICIALES
				consulta+="(";
				consulta+=" SELECT " +
								"to_char(v.fecha_valoracion, 'DD/MM/YYYY') as fecha, " +
								"v.fecha_valoracion as fechabd, " +
								"substr(v.hora_valoracion,1,5) as hora, " +
								"s.numero_solicitud as codigopk, " +
								""+ConstantesImpresionHistoriaClinica.tipoEvolucionValoracionUrgencias+" as codigotipoevolucion, " +
								"1 as ordentipoevolucion " +
							"FROM " +
								"solicitudes s	" +
								"INNER JOIN valoraciones v ON(s.numero_solicitud=v.numero_solicitud)  " +
								"inner join cuentas c ON(c.id=s.cuenta) " +
								"inner join tipos_solicitud t on(t.codigo=s.tipo) " +
								"inner join tipos_paciente tp on(tp.acronimo=c.tipo_paciente) " +
							"WHERE  " +
								"s.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") " +
								"and s.tipo = "+ConstantesBD.codigoTipoSolicitudInicialUrgencias+" " ;
				consulta+=") ";
				esPrimero=false;
			}
			if(filtro.isImprimirValHospitalizacion())
			{
				/**
				* Tipo Modificacion: Segun incidencia 6493
				* Autor: Alejandro Aguirre Luna
				* usuario: aleagulu
				* Fecha: 15/02/2013
				* Descripcion: Se valida si el paciente que esta por hospitalización
				* es tipo paciente = Cirugia Ambulatoria (C). En caso contrario se
				* deja la consulta como estaba antes. 
				**/
				if(paciente.getNombreTipoPaciente().equals("Cirugia Ambulatoria")){
					existenDatosAConsultar=true;
					if(!esPrimero)
						consulta+=" UNION ";
					//VALORACIONES INICIALES
					consulta+="(";
					consulta+=" SELECT " +
									"to_char(v.fecha_valoracion, 'DD/MM/YYYY') as fecha, " +
									"v.fecha_valoracion as fechabd, " +
									"substr(v.hora_valoracion,1,5) as hora, " +
									"s.numero_solicitud as codigopk, " +
									""+ConstantesImpresionHistoriaClinica.tipoEvolucionValoracionHospitalizacion+" as codigotipoevolucion, " +
									"1 as ordentipoevolucion " +
								"FROM " +
									"solicitudes s	" +
									"INNER JOIN valoraciones v ON(s.numero_solicitud=v.numero_solicitud)  " +
									"inner join cuentas c ON(c.id=s.cuenta) " +
									"inner join tipos_solicitud t on(t.codigo=s.tipo) " +
									"inner join tipos_paciente tp on(tp.acronimo=c.tipo_paciente) " +
								"WHERE  " +
									"s.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") " +
									"and s.tipo = "+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+" " +
									"and c.tipo_paciente='"+ConstantesBD.tipoPacienteCirugiaAmbulatoria+"'" ;
					consulta+=") ";
					esPrimero=false;
				}
				else{
					existenDatosAConsultar=true;
					if(!esPrimero)
						consulta+=" UNION ";
					//VALORACIONES INICIALES
					consulta+="(";
					consulta+=" SELECT " +
									"to_char(v.fecha_valoracion, 'DD/MM/YYYY') as fecha, " +
									"v.fecha_valoracion as fechabd, " +
									"substr(v.hora_valoracion,1,5) as hora, " +
									"s.numero_solicitud as codigopk, " +
									""+ConstantesImpresionHistoriaClinica.tipoEvolucionValoracionHospitalizacion+" as codigotipoevolucion, " +
									"1 as ordentipoevolucion " +
								"FROM " +
									"solicitudes s	" +
									"INNER JOIN valoraciones v ON(s.numero_solicitud=v.numero_solicitud)  " +
									"inner join cuentas c ON(c.id=s.cuenta) " +
									"inner join tipos_solicitud t on(t.codigo=s.tipo) " +
									"inner join tipos_paciente tp on(tp.acronimo=c.tipo_paciente) " +
								"WHERE  " +
									"s.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") " +
									"and s.tipo = "+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+" " +
									"and c.tipo_paciente='"+ConstantesBD.tipoPacienteHospitalizado+"'" ;
					consulta+=") ";
					esPrimero=false;
				}
			}
			if(filtro.isImprimirRespuestaInterpretacionInterconsulta())
			{
				existenDatosAConsultar=true;
				if(!esPrimero)
					consulta+=" UNION ";
				consulta+="(";
				consulta+=" SELECT " +
								"to_char(v.fecha_valoracion, 'DD/MM/YYYY') as fecha, " +
								"v.fecha_valoracion as fechabd, " +
								"substr(v.hora_valoracion,1,5) as hora, " +
								"si.numero_solicitud as codigopk, " +
								""+ConstantesImpresionHistoriaClinica.tipoEvolucionRespuestaInterpretacionInterconsulta+" as codigotipoevolucion, " +
								"2 as ordentipoevolucion " +
							"FROM " +
								"solicitudes_inter si	" +
								"INNER JOIN solicitudes s ON(s.numero_solicitud=si.numero_solicitud) " +
								"INNER JOIN valoraciones v ON(s.numero_solicitud=v.numero_solicitud)  " +
							"WHERE  " +
								"s.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") ";
				if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && UtilidadTexto.isEmpty(filtro.getHoraInicial()) && UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND v.fecha_valoracion  between '"+filtro.getFechaInicial()+"' and '"+filtro.getFechaFinal()+"'";
				}
				else if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && !UtilidadTexto.isEmpty(filtro.getHoraInicial()) && !UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND ( ( v.fecha_valoracion > '"+filtro.getFechaInicial()+"') or ( v.fecha_valoracion = '"+filtro.getFechaInicial()+"' and  v.hora_valoracion >= '"+filtro.getHoraInicial()+"') ) " +
								" and (( v.fecha_valoracion < '"+filtro.getFechaFinal()+"') or ( v.fecha_valoracion= '"+filtro.getFechaFinal()+"' and  v.hora_valoracion <= '"+filtro.getHoraFinal()+"')) ";
				}
				consulta+=	" and s.estado_historia_clinica in( "+ConstantesBD.codigoEstadoHCInterpretada+", "+ConstantesBD.codigoEstadoHCRespondida+" ) ";
				consulta+=") ";
				esPrimero=false;
			}
			
			if(filtro.isImprimirEvoluciones())
			{
				existenDatosAConsultar=true;
				if(!esPrimero)
					consulta+=" UNION ";
				consulta+="(";
				consulta+=" SELECT " +
								"to_char(e.fecha_evolucion, 'DD/MM/YYYY') as fecha, " +
								"e.fecha_evolucion as fechabd, " +
								"substr(e.hora_evolucion||'', 1,5) as hora, " +
								"e.codigo as codigopk, " +
								""+ConstantesImpresionHistoriaClinica.tipoEvolucionEvoluciones+" as codigotipoevolucion, " +
								"3 as ordentipoevolucion " +
							"FROM " +
								"evoluciones e " +
								"INNER JOIN solicitudes s ON(s.numero_solicitud=e.valoracion) " +
							"WHERE  " +
								"s.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") ";
				if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && UtilidadTexto.isEmpty(filtro.getHoraInicial()) && UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND e.fecha_evolucion  between '"+filtro.getFechaInicial()+"' and '"+filtro.getFechaFinal()+"'";
				}
				else if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && !UtilidadTexto.isEmpty(filtro.getHoraInicial()) && !UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND ( ( e.fecha_evolucion > '"+filtro.getFechaInicial()+"') or ( e.fecha_evolucion = '"+filtro.getFechaInicial()+"' and  e.hora_evolucion >= '"+filtro.getHoraInicial()+"') ) " +
								" and (( e.fecha_evolucion < '"+filtro.getFechaFinal()+"') or ( e.fecha_evolucion= '"+filtro.getFechaFinal()+"' and  e.hora_evolucion <= '"+filtro.getHoraFinal()+"')) ";
				}
				consulta+=" and s.estado_historia_clinica <> "+ConstantesBD.codigoEstadoHCAnulada+" ";
				consulta+=") ";
				esPrimero=false;
			}
			
			if(filtro.isImprimirValoracionesConsultaExterna()){
				existenDatosAConsultar=true;
				if(!esPrimero)
					consulta+=" UNION ";
				consulta+="(";
				consulta+=" SELECT " +
								"to_char(sol.fecha_solicitud, 'DD/MM/YYYY') as fecha, " +
								"sol.fecha_solicitud as fechabd, " +
								"substr(sol.hora_solicitud,1,5) as hora, " +
								"sol.numero_solicitud as codigopk, " +
								""+ConstantesImpresionHistoriaClinica.tipoEvolucionValoracionConsultaExterna+" as codigotipoevolucion, " +
								"4 as ordentipoevolucion " +
							"FROM " +
								"solicitudes sol	" +
								"INNER JOIN servicios_cita sc ON(sc.numero_solicitud=sol.numero_solicitud) " +
								"INNER JOIN servicios ser ON(ser.codigo=sc.servicio) " +
							"WHERE  " +
								"sol.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") ";
				if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && UtilidadTexto.isEmpty(filtro.getHoraInicial()) && UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND sol.fecha_solicitud  between '"+filtro.getFechaInicial()+"' and '"+filtro.getFechaFinal()+"'";
				}
				else if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && !UtilidadTexto.isEmpty(filtro.getHoraInicial()) && !UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND ( ( sol.fecha_solicitud > '"+filtro.getFechaInicial()+"') or ( sol.fecha_solicitud = '"+filtro.getFechaInicial()+"' and  sol.hora_solicitud >= '"+filtro.getHoraInicial()+"') ) " +
								" and (( sol.fecha_solicitud < '"+filtro.getFechaFinal()+"') or ( sol.fecha_solicitud= '"+filtro.getFechaFinal()+"' and  sol.hora_solicitud <= '"+filtro.getHoraFinal()+"')) ";
				}
				consulta+=	" and sol.estado_historia_clinica in( "+ConstantesBD.codigoEstadoHCInterpretada+", "+ConstantesBD.codigoEstadoHCRespondida+" ) ";
				consulta+=") ";
				esPrimero=false;
			}
			if(filtro.isImprimirOrdenesMedicas())
			{
				existenDatosAConsultar=true;
				//procedimientos.
				if(!esPrimero)
					consulta+=" UNION ";
				consulta+="(";
				consulta+=" SELECT " +
								"to_char(sol.fecha_solicitud, 'DD/MM/YYYY') as fecha, " +
								"sol.fecha_solicitud as fechabd, " +
								"substr(sol.hora_solicitud,1,5) as hora, " +
								"sol.numero_solicitud as codigopk, " +
								""+ConstantesImpresionHistoriaClinica.tipoEvolucionSolicitudesProcedimientos+" as codigotipoevolucion, " +
								"5 as ordentipoevolucion " +
							"FROM " +
								" solicitudes sol " +
								" inner join sol_procedimientos sp on(sp.numero_solicitud=sol.numero_solicitud) " +
							"WHERE  " +
								"sol.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") ";
				if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && UtilidadTexto.isEmpty(filtro.getHoraInicial()) && UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND sol.fecha_solicitud  between '"+filtro.getFechaInicial()+"' and '"+filtro.getFechaFinal()+"'";
				}
				else if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && !UtilidadTexto.isEmpty(filtro.getHoraInicial()) && !UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND ( ( sol.fecha_solicitud > '"+filtro.getFechaInicial()+"') or ( sol.fecha_solicitud = '"+filtro.getFechaInicial()+"' and  sol.hora_solicitud >= '"+filtro.getHoraInicial()+"') ) " +
								" and (( sol.fecha_solicitud < '"+filtro.getFechaFinal()+"') or ( sol.fecha_solicitud= '"+filtro.getFechaFinal()+"' and  sol.hora_solicitud <= '"+filtro.getHoraFinal()+"')) ";
				}
				consulta+=	" and sol.tipo='"+ConstantesBD.codigoTipoSolicitudProcedimiento+"' and sol.estado_historia_clinica<>"+ConstantesBD.codigoEstadoHCAnulada+"  ";
				consulta+=") ";
				esPrimero=false;
				
				//medicamentos
				if(!esPrimero)
					consulta+=" UNION ";
				consulta+="(";
				consulta+=" SELECT " +
								"to_char(sol.fecha_solicitud, 'DD/MM/YYYY') as fecha, " +
								"sol.fecha_solicitud as fechabd, " +
								"substr(sol.hora_solicitud,1,5) as hora, " +
								"sol.numero_solicitud as codigopk, " +
								""+ConstantesImpresionHistoriaClinica.tipoEvolucionSolicitudesMedicamentos+" as codigotipoevolucion, " +
								"6 as ordentipoevolucion " +
							"FROM " +
								" solicitudes sol " +
								" inner join solicitudes_medicamentos sm on (sol.numero_solicitud=sm.numero_solicitud) " +
							"WHERE  " +
								"sol.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") ";
				if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && UtilidadTexto.isEmpty(filtro.getHoraInicial()) && UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND sol.fecha_solicitud  between '"+filtro.getFechaInicial()+"' and '"+filtro.getFechaFinal()+"'";
				}
				else if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && !UtilidadTexto.isEmpty(filtro.getHoraInicial()) && !UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND ( ( sol.fecha_solicitud > '"+filtro.getFechaInicial()+"') or ( sol.fecha_solicitud = '"+filtro.getFechaInicial()+"' and  sol.hora_solicitud >= '"+filtro.getHoraInicial()+"') ) " +
								" and (( sol.fecha_solicitud < '"+filtro.getFechaFinal()+"') or ( sol.fecha_solicitud= '"+filtro.getFechaFinal()+"' and  sol.hora_solicitud <= '"+filtro.getHoraFinal()+"')) ";
				}
				consulta+=	"  and sol.tipo='"+ConstantesBD.codigoTipoSolicitudMedicamentos+"' and sol.estado_historia_clinica<>"+ConstantesBD.codigoEstadoHCAnulada+"  ";
				consulta+=") ";
				esPrimero=false;
				
				//cirugias
				if(!esPrimero)
					consulta+=" UNION ";
				consulta+="(";
				consulta+=" SELECT " +
								"to_char(sol.fecha_solicitud, 'DD/MM/YYYY') as fecha, " +
								"sol.fecha_solicitud as fechabd, " +
								"substr(sol.hora_solicitud,1,5) as hora, " +
								"sol.numero_solicitud as codigopk, " +
								""+ConstantesImpresionHistoriaClinica.tipoEvolucionSolicitudesCirugias+" as codigotipoevolucion, " +
								"7 as ordentipoevolucion " +
							"FROM " +
								" solicitudes sol " +
								" inner join solicitudes_cirugia sc ON(sc.numero_solicitud = sol.numero_solicitud AND (sc.ind_qx = '"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia+"' OR sc.ind_qx = '"+ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento+"')) " +
							"WHERE  " +
								"sol.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") ";
				if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && UtilidadTexto.isEmpty(filtro.getHoraInicial()) && UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND sol.fecha_solicitud  between '"+filtro.getFechaInicial()+"' and '"+filtro.getFechaFinal()+"'";
				}
				else if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && !UtilidadTexto.isEmpty(filtro.getHoraInicial()) && !UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND ( ( sol.fecha_solicitud > '"+filtro.getFechaInicial()+"') or ( sol.fecha_solicitud = '"+filtro.getFechaInicial()+"' and  sol.hora_solicitud >= '"+filtro.getHoraInicial()+"') ) " +
								" and (( sol.fecha_solicitud < '"+filtro.getFechaFinal()+"') or ( sol.fecha_solicitud= '"+filtro.getFechaFinal()+"' and  sol.hora_solicitud <= '"+filtro.getHoraFinal()+"')) ";
				}
				consulta+=	" and sol.tipo='"+ConstantesBD.codigoTipoSolicitudCirugia+"' and sol.estado_historia_clinica<>"+ConstantesBD.codigoEstadoHCAnulada+"  ";
				consulta+=") ";
				esPrimero=false;
				
				//interconsultas
				if(!esPrimero)
					consulta+=" UNION ";
				consulta+="(";
				consulta+=" SELECT " +
								"to_char(sol.fecha_solicitud, 'DD/MM/YYYY') as fecha, " +
								"sol.fecha_solicitud as fechabd, " +
								"substr(sol.hora_solicitud,1,5) as hora, " +
								"sol.numero_solicitud as codigopk, " +
								""+ConstantesImpresionHistoriaClinica.tipoEvolucionSolicitudesInterconsultas+" as codigotipoevolucion, " +
								"8 as ordentipoevolucion " +
							"FROM " +
								" solicitudes sol " +
								" inner join solicitudes_inter si On(si.numero_solicitud=sol.numero_solicitud) " +
							"WHERE  " +
								"sol.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") ";
				if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && UtilidadTexto.isEmpty(filtro.getHoraInicial()) && UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND sol.fecha_solicitud  between '"+filtro.getFechaInicial()+"' and '"+filtro.getFechaFinal()+"'";
				}
				else if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && !UtilidadTexto.isEmpty(filtro.getHoraInicial()) && !UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND ( ( sol.fecha_solicitud > '"+filtro.getFechaInicial()+"') or ( sol.fecha_solicitud = '"+filtro.getFechaInicial()+"' and  sol.hora_solicitud >= '"+filtro.getHoraInicial()+"') ) " +
								" and (( sol.fecha_solicitud < '"+filtro.getFechaFinal()+"') or ( sol.fecha_solicitud= '"+filtro.getFechaFinal()+"' and  sol.hora_solicitud <= '"+filtro.getHoraFinal()+"')) ";
				}
				consulta+=	" and sol.tipo='"+ConstantesBD.codigoTipoSolicitudInterconsulta+"' and sol.estado_historia_clinica<>"+ConstantesBD.codigoEstadoHCAnulada+"  ";
				consulta+=") ";
				esPrimero=false;
				
				//informacion de orden medica
				if(!esPrimero)
					consulta+=" UNION ";
				consulta+="(";
				consulta+=" SELECT " +
								"to_char(ehom.fecha_orden, 'DD/MM/YYYY') as fecha, " +
								"ehom.fecha_orden as fechabd, " +
								"substr(ehom.hora_orden,1,5) as hora, " +
								"ehom.codigo as codigopk, " +
								""+ConstantesImpresionHistoriaClinica.tipoEvolucionOrdenesMedicas+" as codigotipoevolucion, " +
								"9 as ordentipoevolucion " +
							" from ordenes_medicas om " +
							" inner join hoja_neurologica_orden_m hnom on(hnom.orden_medica=om.codigo)" +
							" inner join encabezado_histo_orden_m ehom on(ehom.orden_medica=om.codigo) " +
							"WHERE  " +
								"om.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") ";
				if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && UtilidadTexto.isEmpty(filtro.getHoraInicial()) && UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND ehom.fecha_orden  between '"+filtro.getFechaInicial()+"' and '"+filtro.getFechaFinal()+"'";
				}
				else if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && !UtilidadTexto.isEmpty(filtro.getHoraInicial()) && !UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND ( ( ehom.fecha_orden > '"+filtro.getFechaInicial()+"') or ( ehom.fecha_orden = '"+filtro.getFechaInicial()+"' and  ehom.hora_orden >= '"+filtro.getHoraInicial()+"') ) " +
								" and (( ehom.fecha_orden < '"+filtro.getFechaFinal()+"') or ( ehom.fecha_orden= '"+filtro.getFechaFinal()+"' and  ehom.hora_orden <= '"+filtro.getHoraFinal()+"')) ";
				}
				consulta+=") ";
				esPrimero=false;
			}
			if(filtro.isImprimirCirugia())
			{ /*
				existenDatosAConsultar=true;
				if(!esPrimero){
					consulta+=" UNION ";
				}
				consulta+="(";
				
				consulta+="  SELECT  TO_CHAR(sol.fecha_solicitud,'DD/MM/YYYY') AS fecha, " +
						"  coalesce(sc.fecha_inicial_cx,sol.FECHA_GRABACION)  AS fechabd, " +
						"  coalesce( SUBSTR(sc.hora_inicial_cx,1,5),SUBSTR(sol.hora_grabacion,1,5)) AS hora, " +
						" sol.numero_solicitud           AS codigopk," +
						" "+ConstantesImpresionHistoriaClinica.codigoSeccionCirugias+" AS codigotipoevolucion," +
						" 10                           AS ordentipoevolucion " +
						" FROM solicitudes_cirugia sc " +
						" LEFT OUTER JOIN hoja_quirurgica hq " +
						" ON (sc.numero_solicitud=hq.numero_solicitud) " +
						" LEFT OUTER JOIN hoja_anestesia ha " +
						" ON (ha.numero_solicitud=sc.numero_solicitud) " +
						" INNER JOIN solicitudes sol " +
						" ON (sol.numero_solicitud=sc.numero_solicitud) " +
						" LEFT OUTER JOIN res_sol_proc res " +
						" ON (res.numero_solicitud=sc.numero_solicitud) " +
						" WHERE sol.cuenta        in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") " +
						" AND sc.ind_qx          IN ('CX','NCTO')";
				consulta+=") ";
				esPrimero=false;*/
				
				
				existenDatosAConsultar=true;
				if(!esPrimero){
					consulta+=" UNION ";
				}
				consulta+="(";
				
				/**
				* Tipo Modificacion: Segun incidencia 5055
				* Autor: Alejandro Aguirre Luna
				* usuario: aleagulu
				* Fecha: 22/02/2013
				* Descripcion: Modificacion del filtro de la consulta, ahora en el IN se filtran todos los
				* asocios relacionados al ingreso. 
				**/
				
				consulta+="  SELECT  TO_CHAR(sol.fecha_solicitud,'DD/MM/YYYY') AS fecha, " +
						"  coalesce(sc.fecha_inicial_cx,sol.FECHA_GRABACION)  AS fechabd, " +
						"  coalesce( SUBSTR(sc.hora_inicial_cx,1,5),SUBSTR(sol.hora_grabacion,1,5)) AS hora, " +
						" sol.numero_solicitud           AS codigopk," +
						" "+ConstantesImpresionHistoriaClinica.codigoSeccionCirugias+" AS codigotipoevolucion," +
						" 10                           AS ordentipoevolucion " +
						" FROM solicitudes_cirugia sc " +
						" LEFT OUTER JOIN hoja_quirurgica hq " +
						" ON (sc.numero_solicitud=hq.numero_solicitud) " +
						" LEFT OUTER JOIN hoja_anestesia ha " +
						" ON (ha.numero_solicitud=sc.numero_solicitud) " +
						" INNER JOIN solicitudes sol " +
						" ON (sol.numero_solicitud=sc.numero_solicitud) " +
						" LEFT OUTER JOIN res_sol_proc res " +
						" ON (res.numero_solicitud=sc.numero_solicitud) " +
						//" WHERE sol.cuenta        in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") " +
						" WHERE sol.cuenta in(" +
						" SELECT distinct(c.id)" +
						"   FROM SOLICITUDES s " +
						"  inner join cuentas c ON(c.id=s.cuenta)" +
						" WHERE c.id_ingreso = '"+paciente.getCodigoIngreso()+"'" +
						")" + 
						" AND sc.ind_qx          IN ('CX','NCTO')";
				consulta+=") ";
				esPrimero=false;
				
				/*consulta+="  SELECT  TO_CHAR(sol.fecha_solicitud,'DD/MM/YYYY') AS fecha, " +
						"  coalesce(sc.fecha_inicial_cx,sol.FECHA_GRABACION)  AS fechabd, " +
						"  coalesce( SUBSTR(sc.hora_inicial_cx,1,5),SUBSTR(sol.hora_grabacion,1,5)) AS hora, " +
						" sol.numero_solicitud           AS codigopk," +
						" "+ConstantesImpresionHistoriaClinica.codigoSeccionCirugias+" AS codigotipoevolucion," +
						" 10                           AS ordentipoevolucion " +
						" FROM solicitudes_cirugia sc " +
						" LEFT OUTER JOIN hoja_quirurgica hq " +
						" ON (sc.numero_solicitud=hq.numero_solicitud) " +
						" LEFT OUTER JOIN hoja_anestesia ha " +
						" ON (ha.numero_solicitud=sc.numero_solicitud) " +
						" INNER JOIN solicitudes sol " +
						" ON (sol.numero_solicitud=sc.numero_solicitud) " +
						" LEFT OUTER JOIN res_sol_proc res " +
						" ON (res.numero_solicitud=sc.numero_solicitud) " +
						//" WHERE sol.cuenta        in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") " +
						" WHERE sol.cuenta in(1045797)" + 
						" AND sc.ind_qx          IN ('CX','NCTO')";
				consulta+=") ";
				esPrimero=false;*/
				
			}
				
			/**
			* Tipo Modificacion: Segun incidencia 5055
			* Autor: Alejandro Aguirre Luna
			* usuario: aleagulu
			* Fecha: 22/02/2013
			* Descripcion: se ordena desc debido a que la cuenta (no asociada) debe quedar de primeras
			* para poder imprimir correctamente el pdf cuando tiene se tienen màs de una cuenta asociada. 
			**/
			consulta+=") tabla order by ";
			consulta+=" tabla.fechabd asc, tabla.hora asc, tabla.ordentipoevolucion ";
		}
		PreparedStatement ps=null;
		ResultSet rs= null;
		try 
		{
			/*
			 * Dado que no es necesario actualizar los datos a medida que se va recorriendo, no veo la necesidad de hacer esto.. ademas, que puede
			 * generar excepciones como SQLException : Unsupported syntax for refreshRow() cuando son mas de 10 registros y algunas BD no lo soportan 			 
			 */			
			//ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps= con.prepareStatement(consulta);			
			if (existenDatosAConsultar) {
				rs=ps.executeQuery();
				
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				while(rs.next())
				{
					DtoResultadoImpresionHistoriaClinica dto=new DtoResultadoImpresionHistoriaClinica();
					dto.setFecha(rs.getString("fecha"));
					dto.setFechaBD(format.format(rs.getDate("fechabd")));
					dto.setHora(rs.getString("hora"));
					dto.setCodigoPk(rs.getInt("codigopk"));
					dto.setCodigoTipoEvolucion(rs.getInt("codigotipoevolucion"));
					resultado.add(dto);			
				}				
			}
			
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally{
			if(ps != null){
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}	
		
		return resultado;
	}
	/** Tipo Modificacion: Segun incidencia 5055
	* Funcion: consultarInformacionHistoriaClinica
	* Autor: Alejandro Aguirre Luna
	* Fecha: 25/02/2013
	* Descripcion: Sobrecarga del metodo consultarInformacionHistoriaClinica
	* 	       con el argumento adicional IdIngreso que permite establecer 
	* 		   posteriromente las cuentas del paciente. 
	* Argumentos:
	* 	- Connection con	
	* 	- DtoFiltroImpresionHistoriaClinica filtro
	* 	- int identificadorSeccion
	* 	- PersonaBasica paciente
	*   - String IdIngreso
	* Retorno: ArrayList<DtoResultadoImpresionHistoriaClinica>
	**/
	public static ArrayList<DtoResultadoImpresionHistoriaClinica> consultarInformacionHistoriaClinica(Connection con, DtoFiltroImpresionHistoriaClinica filtro,int identificadorSeccion, PersonaBasica paciente, String IdIngreso) 
	{
		ArrayList<DtoResultadoImpresionHistoriaClinica> resultado=new ArrayList<DtoResultadoImpresionHistoriaClinica>();
		

		/**
		 * MT 8105
		 * @author javrammo
		 * No se utiliza el filtro, si no se conulta directamete las cuentas por el ingreso
		 */
		final String CUENTAS_X_INGRESO = UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, IdIngreso);
		
		
		String consulta="SELECT " +
							"tabla.fecha, " +
							"tabla.fechabd, " +
							"tabla.hora, " +
							"tabla.cuenta, " +
							"tabla.codigopk, " +
							"tabla.codigotipoevolucion "+ 
						"FROM" +
							"( ";
		boolean esPrimero=true;
		Boolean existenDatosAConsultar=false;
		if(identificadorSeccion==ConstantesImpresionHistoriaClinica.codigoSeccionEvoluciones)
		{
			if(filtro.isImprimirValUrgencias())
			{
				existenDatosAConsultar=true;
				if(!esPrimero)
					consulta+=" UNION ";
				//VALORACIONES INICIALES
				consulta+="(";
				consulta+=" SELECT " +
								"to_char(v.fecha_valoracion, 'DD/MM/YYYY') as fecha, " +
								"v.fecha_valoracion as fechabd, " +
								"substr(v.hora_valoracion,1,5) as hora, " +
								"s.numero_solicitud as codigopk, " +
								"s.cuenta as cuenta, " +
								""+ConstantesImpresionHistoriaClinica.tipoEvolucionValoracionUrgencias+" as codigotipoevolucion, " +
								"1 as ordentipoevolucion " +
							"FROM " +
								"solicitudes s	" +
								"INNER JOIN cuentas c ON (c.id=s.cuenta)  " +
								"INNER JOIN valoraciones v ON(s.numero_solicitud=v.numero_solicitud)  " +
								"INNER JOIN tipos_solicitud t ON (t.codigo=s.tipo) " +
								"INNER JOIN tipos_paciente tp  ON (tp.acronimo=c.tipo_paciente) " +
							"WHERE  " +
								//"s.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") " +
								//"s.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") " +
								"s.cuenta in("+CUENTAS_X_INGRESO+") " +
								"and s.tipo = "+ConstantesBD.codigoTipoSolicitudInicialUrgencias+" ";
				/*
				 * Control Cambio 1605 Version 1.3.0
				 * Alberto Ovalle
			     */
				if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && !UtilidadTexto.isEmpty(filtro.getHoraInicial()) && !UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND ( ( v.fecha_valoracion > '"+filtro.getFechaInicial()+"') or ( v.fecha_valoracion = '"+filtro.getFechaInicial()+"' and  v.hora_valoracion >= '"+filtro.getHoraInicial()+"') ) " +
								" and (( v.fecha_valoracion < '"+filtro.getFechaFinal()+"') or ( v.fecha_valoracion= '"+filtro.getFechaFinal()+"' and  v.hora_valoracion <= '"+filtro.getHoraFinal()+"')) ";
				}
				/* ************************* */
				consulta+=") ";
				esPrimero=false;
			}
			if(filtro.isImprimirValHospitalizacion())
			{
				/**
				* Tipo Modificacion: Segun incidencia 6493
				* Autor: Alejandro Aguirre Luna
				* usuario: aleagulu
				* Fecha: 15/02/2013
				* Descripcion: Se valida si el paciente que esta por hospitalización
				* es tipo paciente = Cirugia Ambulatoria (C). En caso contrario se
				* deja la consulta como estaba antes. 
				**/
				
				//hermorhu - MT6544
				//Se elimina condicional de tipo de paciente en valoraciones de Hospitalizacion al no ser excluyentes
				//caso: asocio entre hospitalizacion de cirugia ambulatoria y hospitalizacion-hospitalizacion
				
//				if(paciente.getNombreTipoPaciente().equals("Cirugia Ambulatoria")){
					existenDatosAConsultar=true;
					if(!esPrimero)
						consulta+=" UNION ";
					//VALORACIONES INICIALES
					consulta+="(";
					consulta+=" SELECT " +
									"to_char(v.fecha_valoracion, 'DD/MM/YYYY') as fecha, " +
									"v.fecha_valoracion as fechabd, " +
									"substr(v.hora_valoracion,1,5) as hora, " +
									"s.numero_solicitud as codigopk, " +
									"s.cuenta as cuenta, " +
									""+ConstantesImpresionHistoriaClinica.tipoEvolucionValoracionHospitalizacion+" as codigotipoevolucion, " +
									"1 as ordentipoevolucion " +
								"FROM " +
									"solicitudes s	" +
									"INNER JOIN cuentas c ON (c.id=s.cuenta)  " +
									"INNER JOIN valoraciones v ON(s.numero_solicitud=v.numero_solicitud)  " +
									"INNER JOIN tipos_solicitud t ON (t.codigo=s.tipo) " +
									"INNER JOIN tipos_paciente tp  ON (tp.acronimo=c.tipo_paciente) " +
								"WHERE  " +
									//"s.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+","+filtro.getCuentaAsociada1()+" ) " +
									//"s.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") " +
									"s.cuenta in("+CUENTAS_X_INGRESO+") " +
									"and s.tipo = "+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+" " +
									"and c.tipo_paciente='"+ConstantesBD.tipoPacienteCirugiaAmbulatoria+"'" ;
					/*
					 * Control de cambio 1605
					 * Alberto Ovalle 
					 * */
					if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && !UtilidadTexto.isEmpty(filtro.getHoraInicial()) && !UtilidadTexto.isEmpty(filtro.getHoraFinal()))
					{
						consulta+=" AND ( ( v.fecha_valoracion > '"+filtro.getFechaInicial()+"') or ( v.fecha_valoracion = '"+filtro.getFechaInicial()+"' and  v.hora_valoracion >= '"+filtro.getHoraInicial()+"') ) " +
									" and (( v.fecha_valoracion < '"+filtro.getFechaFinal()+"') or ( v.fecha_valoracion= '"+filtro.getFechaFinal()+"' and  v.hora_valoracion <= '"+filtro.getHoraFinal()+"')) ";
					}
					/* ************************* */
					consulta+=") ";
//					esPrimero=false;
//				}
//				else{
//					existenDatosAConsultar=true;
//					if(!esPrimero)
						consulta+=" UNION ";
					//VALORACIONES INICIALES
					consulta+="(";
					consulta+=" SELECT " +
									"to_char(v.fecha_valoracion, 'DD/MM/YYYY') as fecha, " +
									"v.fecha_valoracion as fechabd, " +
									"substr(v.hora_valoracion,1,5) as hora, " +
									"s.numero_solicitud as codigopk, " +
									"s.cuenta as cuenta, " +
									""+ConstantesImpresionHistoriaClinica.tipoEvolucionValoracionHospitalizacion+" as codigotipoevolucion, " +
									"1 as ordentipoevolucion " +
								"FROM " +
									"solicitudes s	" +
									"INNER JOIN cuentas c ON (c.id=s.cuenta)  " +
									"INNER JOIN valoraciones v ON(s.numero_solicitud=v.numero_solicitud)  " +
									"INNER JOIN tipos_solicitud t ON (t.codigo=s.tipo) " +
									"INNER JOIN tipos_paciente tp  ON (tp.acronimo=c.tipo_paciente) " +
								"WHERE  " +
									//"s.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+","+filtro.getCuentaAsociada1()+" ) " +
									//"s.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") " +
									"s.cuenta in("+CUENTAS_X_INGRESO+") " +
									"and s.tipo = "+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+" " +
									"and c.tipo_paciente='"+ConstantesBD.tipoPacienteHospitalizado+"'" ;
					/*
					 * Control Cambio 1605 Version 1.3.0
					 * Alberto Ovalle
				     */
					if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && !UtilidadTexto.isEmpty(filtro.getHoraInicial()) && !UtilidadTexto.isEmpty(filtro.getHoraFinal()))
					{
						consulta+=" AND ( ( v.fecha_valoracion > '"+filtro.getFechaInicial()+"') or ( v.fecha_valoracion = '"+filtro.getFechaInicial()+"' and  v.hora_valoracion >= '"+filtro.getHoraInicial()+"') ) " +
									" and (( v.fecha_valoracion < '"+filtro.getFechaFinal()+"') or ( v.fecha_valoracion= '"+filtro.getFechaFinal()+"' and  v.hora_valoracion <= '"+filtro.getHoraFinal()+"')) ";
					}
					/* ************************* */
					
					consulta+=") ";
					esPrimero=false;
//				}
			}
			if(filtro.isImprimirRespuestaInterpretacionInterconsulta())
			{
				existenDatosAConsultar=true;
				if(!esPrimero)
					consulta+=" UNION ";
				consulta+="(";
				consulta+=" SELECT " +
								"to_char(v.fecha_valoracion, 'DD/MM/YYYY') as fecha, " +
								"v.fecha_valoracion as fechabd, " +
								"substr(v.hora_valoracion,1,5) as hora, " +
								"si.numero_solicitud as codigopk, " +
								"s.cuenta as cuenta, " +
								""+ConstantesImpresionHistoriaClinica.tipoEvolucionRespuestaInterpretacionInterconsulta+" as codigotipoevolucion, " +
								"2 as ordentipoevolucion " +
							"FROM " +
								"solicitudes s	" +
								"INNER JOIN valoraciones v ON(s.numero_solicitud=v.numero_solicitud)  " +
								"INNER JOIN solicitudes_inter si  ON (s.numero_solicitud=si.numero_solicitud)  " +
							"WHERE  " +
								//"s.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") ";
								"s.cuenta in("+CUENTAS_X_INGRESO+") ";
				if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && UtilidadTexto.isEmpty(filtro.getHoraInicial()) && UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND v.fecha_valoracion  between '"+filtro.getFechaInicial()+"' and '"+filtro.getFechaFinal()+"'";
				}
				else if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && !UtilidadTexto.isEmpty(filtro.getHoraInicial()) && !UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND ( ( v.fecha_valoracion > '"+filtro.getFechaInicial()+"') or ( v.fecha_valoracion = '"+filtro.getFechaInicial()+"' and  v.hora_valoracion >= '"+filtro.getHoraInicial()+"') ) " +
								" and (( v.fecha_valoracion < '"+filtro.getFechaFinal()+"') or ( v.fecha_valoracion= '"+filtro.getFechaFinal()+"' and  v.hora_valoracion <= '"+filtro.getHoraFinal()+"')) ";
				}
				consulta+=	" and s.estado_historia_clinica in( "+ConstantesBD.codigoEstadoHCInterpretada+", "+ConstantesBD.codigoEstadoHCRespondida+" ) ";
				consulta+=") ";
				esPrimero=false;
			}
			
			if(filtro.isImprimirEvoluciones())
			{
				existenDatosAConsultar=true;
				if(!esPrimero)
					consulta+=" UNION ";
				consulta+="(";
				consulta+=" SELECT " +
								"to_char(e.fecha_evolucion, 'DD/MM/YYYY') as fecha, " +
								"e.fecha_evolucion as fechabd, " +
								"substr(e.hora_evolucion||'', 1,5) as hora, " +
								"e.codigo as codigopk, " +
								"s.cuenta as cuenta, " +
								""+ConstantesImpresionHistoriaClinica.tipoEvolucionEvoluciones+" as codigotipoevolucion, " +
								"3 as ordentipoevolucion " +
							"FROM " +
								"solicitudes s " +
								"INNER JOIN evoluciones e ON(s.numero_solicitud=e.valoracion)  " +
							"WHERE  " +
								//"s.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+ ","+filtro.getCuentaAsociada1()+" ) ";
								//"s.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") ";
								"s.cuenta in("+CUENTAS_X_INGRESO+") ";

				if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && UtilidadTexto.isEmpty(filtro.getHoraInicial()) && UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND e.fecha_evolucion  between '"+filtro.getFechaInicial()+"' and '"+filtro.getFechaFinal()+"'";
				}
				else if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && !UtilidadTexto.isEmpty(filtro.getHoraInicial()) && !UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND ( ( e.fecha_evolucion > '"+filtro.getFechaInicial()+"') or ( e.fecha_evolucion = '"+filtro.getFechaInicial()+"' and  e.hora_evolucion >= '"+filtro.getHoraInicial()+"') ) " +
								" and (( e.fecha_evolucion < '"+filtro.getFechaFinal()+"') or ( e.fecha_evolucion= '"+filtro.getFechaFinal()+"' and  e.hora_evolucion <= '"+filtro.getHoraFinal()+"')) ";
				}
				consulta+=" and s.estado_historia_clinica <> "+ConstantesBD.codigoEstadoHCAnulada+" ";
				consulta+=") ";
				esPrimero=false;
			}
			
			if(filtro.isImprimirValoracionesConsultaExterna()){
				existenDatosAConsultar=true;
				if(!esPrimero)
					consulta+=" UNION ";
				consulta+="(";
				consulta+=" SELECT " +
								"to_char(sol.fecha_solicitud, 'DD/MM/YYYY') as fecha, " +
								"sol.fecha_solicitud as fechabd, " +
								"substr(sol.hora_solicitud,1,5) as hora, " +
								"sol.numero_solicitud as codigopk, " +
								"sol.cuenta as cuenta, " +
								""+ConstantesImpresionHistoriaClinica.tipoEvolucionValoracionConsultaExterna+" as codigotipoevolucion, " +
								"4 as ordentipoevolucion " +
							"FROM " +
								"solicitudes sol	" +
								"INNER JOIN servicios_cita sc ON(sc.numero_solicitud=sol.numero_solicitud) " +
								"INNER JOIN servicios ser ON(ser.codigo=sc.servicio) " +
							"WHERE  " +
								//"sol.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") ";
								"sol.cuenta in("+CUENTAS_X_INGRESO+") ";
				if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && UtilidadTexto.isEmpty(filtro.getHoraInicial()) && UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND sol.fecha_solicitud  between '"+filtro.getFechaInicial()+"' and '"+filtro.getFechaFinal()+"'";
				}
				else if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && !UtilidadTexto.isEmpty(filtro.getHoraInicial()) && !UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND ( ( sol.fecha_solicitud > '"+filtro.getFechaInicial()+"') or ( sol.fecha_solicitud = '"+filtro.getFechaInicial()+"' and  sol.hora_solicitud >= '"+filtro.getHoraInicial()+"') ) " +
								" and (( sol.fecha_solicitud < '"+filtro.getFechaFinal()+"') or ( sol.fecha_solicitud= '"+filtro.getFechaFinal()+"' and  sol.hora_solicitud <= '"+filtro.getHoraFinal()+"')) ";
				}
				consulta+=	" and sol.estado_historia_clinica in( "+ConstantesBD.codigoEstadoHCInterpretada+", "+ConstantesBD.codigoEstadoHCRespondida+" ) ";
				consulta+=") ";
				esPrimero=false;
			}
			if(filtro.isImprimirOrdenesMedicas())
			{
				existenDatosAConsultar=true;
				//procedimientos.
				if(!esPrimero)
					consulta+=" UNION ";
				consulta+="(";
				consulta+=" SELECT " +
								"to_char(sol.fecha_solicitud, 'DD/MM/YYYY') as fecha, " +
								"sol.fecha_solicitud as fechabd, " +
								"substr(sol.hora_solicitud,1,5) as hora, " +
								"sol.numero_solicitud as codigopk, " +
								"sol.cuenta as cuenta, " +
								""+ConstantesImpresionHistoriaClinica.tipoEvolucionSolicitudesProcedimientos+" as codigotipoevolucion, " +
								"5 as ordentipoevolucion " +
							"FROM " +
								" solicitudes sol " +
								" inner join sol_procedimientos sp on(sp.numero_solicitud=sol.numero_solicitud) " +
							"WHERE  " +
								//"sol.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") ";
								"sol.cuenta in("+CUENTAS_X_INGRESO+") ";
				if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && UtilidadTexto.isEmpty(filtro.getHoraInicial()) && UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND sol.fecha_solicitud  between '"+filtro.getFechaInicial()+"' and '"+filtro.getFechaFinal()+"'";
				}
				else if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && !UtilidadTexto.isEmpty(filtro.getHoraInicial()) && !UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND ( ( sol.fecha_solicitud > '"+filtro.getFechaInicial()+"') or ( sol.fecha_solicitud = '"+filtro.getFechaInicial()+"' and  sol.hora_solicitud >= '"+filtro.getHoraInicial()+"') ) " +
								" and (( sol.fecha_solicitud < '"+filtro.getFechaFinal()+"') or ( sol.fecha_solicitud= '"+filtro.getFechaFinal()+"' and  sol.hora_solicitud <= '"+filtro.getHoraFinal()+"')) ";
				}
				consulta+=	" and (sol.tipo='"+ConstantesBD.codigoTipoSolicitudProcedimiento+"' or (sol.tipo='"+ConstantesBD.codigoTipoSolicitudCirugia+"' AND (select count(numero_solicitud) from sol_procedimientos where numero_solicitud = sol.numero_solicitud) > 0)) " +
						" and sol.estado_historia_clinica<>"+ConstantesBD.codigoEstadoHCAnulada+"  ";
				consulta+=") ";
				esPrimero=false;
				
				//medicamentos
				if(!esPrimero)
					consulta+=" UNION ";
				consulta+="(";
				consulta+=" SELECT " +
								"to_char(sol.fecha_solicitud, 'DD/MM/YYYY') as fecha, " +
								"sol.fecha_solicitud as fechabd, " +
								"substr(sol.hora_solicitud,1,5) as hora, " +
								"sol.numero_solicitud as codigopk, " +
								"sol.cuenta as cuenta, " +
								""+ConstantesImpresionHistoriaClinica.tipoEvolucionSolicitudesMedicamentos+" as codigotipoevolucion, " +
								"6 as ordentipoevolucion " +
							"FROM " +
								" solicitudes sol " +
								" inner join solicitudes_medicamentos sm on (sol.numero_solicitud=sm.numero_solicitud) " +
							"WHERE  " +
								//"sol.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") ";
								"sol.cuenta in("+CUENTAS_X_INGRESO+") ";
				if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && UtilidadTexto.isEmpty(filtro.getHoraInicial()) && UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND sol.fecha_solicitud  between '"+filtro.getFechaInicial()+"' and '"+filtro.getFechaFinal()+"'";
				}
				else if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && !UtilidadTexto.isEmpty(filtro.getHoraInicial()) && !UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND ( ( sol.fecha_solicitud > '"+filtro.getFechaInicial()+"') or ( sol.fecha_solicitud = '"+filtro.getFechaInicial()+"' and  sol.hora_solicitud >= '"+filtro.getHoraInicial()+"') ) " +
								" and (( sol.fecha_solicitud < '"+filtro.getFechaFinal()+"') or ( sol.fecha_solicitud= '"+filtro.getFechaFinal()+"' and  sol.hora_solicitud <= '"+filtro.getHoraFinal()+"')) ";
				}
				consulta+=	"  and sol.tipo='"+ConstantesBD.codigoTipoSolicitudMedicamentos+"' and sol.estado_historia_clinica<>"+ConstantesBD.codigoEstadoHCAnulada+"  ";
				consulta+=") ";
				esPrimero=false;
				
				//cirugias
				if(!esPrimero)
					consulta+=" UNION ";
				consulta+="(";
				consulta+=" SELECT " +
								"to_char(sol.fecha_solicitud, 'DD/MM/YYYY') as fecha, " +
								"sol.fecha_solicitud as fechabd, " +
								"substr(sol.hora_solicitud,1,5) as hora, " +
								"sol.numero_solicitud as codigopk, " +
								"sol.cuenta as cuenta, " +
								""+ConstantesImpresionHistoriaClinica.tipoEvolucionSolicitudesCirugias+" as codigotipoevolucion, " +
								"7 as ordentipoevolucion " +
							"FROM " +
								" solicitudes sol " +
								" inner join solicitudes_cirugia sc ON(sc.numero_solicitud = sol.numero_solicitud AND (sc.ind_qx = '"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia+"' OR sc.ind_qx = '"+ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento+"')) " +
							"WHERE  " +
								//"sol.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") ";
								"sol.cuenta in("+CUENTAS_X_INGRESO+") "+
								/**
								 * Para que no tenga en cuenta las solicitudes de cirugia de tipo no cruento que ahora se responde como
								 * procedimiento y no como Hoja Qx (ralcionado a la MT 1528)
								 * MT 8472
								 * @author javrammo
								 */
								"AND (select count(numero_solicitud) from sol_procedimientos where numero_solicitud = sc.numero_solicitud) = 0 ";
								/**
								 * Fin MT 1528
								 */
				
				
								
								
				if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && UtilidadTexto.isEmpty(filtro.getHoraInicial()) && UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND sol.fecha_solicitud  between '"+filtro.getFechaInicial()+"' and '"+filtro.getFechaFinal()+"'";
				}
				else if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && !UtilidadTexto.isEmpty(filtro.getHoraInicial()) && !UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND ( ( sol.fecha_solicitud > '"+filtro.getFechaInicial()+"') or ( sol.fecha_solicitud = '"+filtro.getFechaInicial()+"' and  sol.hora_solicitud >= '"+filtro.getHoraInicial()+"') ) " +
								" and (( sol.fecha_solicitud < '"+filtro.getFechaFinal()+"') or ( sol.fecha_solicitud= '"+filtro.getFechaFinal()+"' and  sol.hora_solicitud <= '"+filtro.getHoraFinal()+"')) ";
				}
				consulta+=	" and sol.tipo='"+ConstantesBD.codigoTipoSolicitudCirugia+"' and sol.estado_historia_clinica<>"+ConstantesBD.codigoEstadoHCAnulada+"  ";
				consulta+=") ";
				esPrimero=false;
				
				//interconsultas
				if(!esPrimero)
					consulta+=" UNION ";
				consulta+="(";
				consulta+=" SELECT " +
								"to_char(sol.fecha_solicitud, 'DD/MM/YYYY') as fecha, " +
								"sol.fecha_solicitud as fechabd, " +
								"substr(sol.hora_solicitud,1,5) as hora, " +
								"sol.numero_solicitud as codigopk, " +
								"sol.cuenta as cuenta, " +
								""+ConstantesImpresionHistoriaClinica.tipoEvolucionSolicitudesInterconsultas+" as codigotipoevolucion, " +
								"8 as ordentipoevolucion " +
							"FROM " +
								" solicitudes sol " +
								" inner join solicitudes_inter si On(si.numero_solicitud=sol.numero_solicitud) " +
							"WHERE  " +
								//"sol.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") ";
								"sol.cuenta in("+CUENTAS_X_INGRESO+") ";
				if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && UtilidadTexto.isEmpty(filtro.getHoraInicial()) && UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND sol.fecha_solicitud  between '"+filtro.getFechaInicial()+"' and '"+filtro.getFechaFinal()+"'";
				}
				else if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && !UtilidadTexto.isEmpty(filtro.getHoraInicial()) && !UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND ( ( sol.fecha_solicitud > '"+filtro.getFechaInicial()+"') or ( sol.fecha_solicitud = '"+filtro.getFechaInicial()+"' and  sol.hora_solicitud >= '"+filtro.getHoraInicial()+"') ) " +
								" and (( sol.fecha_solicitud < '"+filtro.getFechaFinal()+"') or ( sol.fecha_solicitud= '"+filtro.getFechaFinal()+"' and  sol.hora_solicitud <= '"+filtro.getHoraFinal()+"')) ";
				}
				consulta+=	" and sol.tipo='"+ConstantesBD.codigoTipoSolicitudInterconsulta+"' and sol.estado_historia_clinica<>"+ConstantesBD.codigoEstadoHCAnulada+"  ";
				consulta+=") ";
				esPrimero=false;
				
				//informacion de orden medica
				if(!esPrimero)
					consulta+=" UNION ";
				consulta+="(";
				consulta+=" SELECT " +
								"to_char(ehom.fecha_orden, 'DD/MM/YYYY') as fecha, " +
								"ehom.fecha_orden as fechabd, " +
								"substr(ehom.hora_orden,1,5) as hora, " +
								"ehom.codigo as codigopk, " +
								"om.cuenta as cuenta, " +
								""+ConstantesImpresionHistoriaClinica.tipoEvolucionOrdenesMedicas+" as codigotipoevolucion, " +
								"9 as ordentipoevolucion " +
							" from ordenes_medicas om " +
							" inner join hoja_neurologica_orden_m hnom on(hnom.orden_medica=om.codigo)" +
							" inner join encabezado_histo_orden_m ehom on(ehom.orden_medica=om.codigo) " +
							"WHERE  " +
								//"om.cuenta in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") ";
								"om.cuenta in("+CUENTAS_X_INGRESO+") ";
				if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && UtilidadTexto.isEmpty(filtro.getHoraInicial()) && UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND ehom.fecha_orden  between '"+filtro.getFechaInicial()+"' and '"+filtro.getFechaFinal()+"'";
				}
				else if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && !UtilidadTexto.isEmpty(filtro.getHoraInicial()) && !UtilidadTexto.isEmpty(filtro.getHoraFinal()))
				{
					consulta+=" AND ( ( ehom.fecha_orden > '"+filtro.getFechaInicial()+"') or ( ehom.fecha_orden = '"+filtro.getFechaInicial()+"' and  ehom.hora_orden >= '"+filtro.getHoraInicial()+"') ) " +
								" and (( ehom.fecha_orden < '"+filtro.getFechaFinal()+"') or ( ehom.fecha_orden= '"+filtro.getFechaFinal()+"' and  ehom.hora_orden <= '"+filtro.getHoraFinal()+"')) ";
				}
				consulta+=") ";
				esPrimero=false;
			}
			if(filtro.isImprimirCirugia())
			{ /*
				existenDatosAConsultar=true;
				if(!esPrimero){
					consulta+=" UNION ";
				}
				consulta+="(";
				
				consulta+="  SELECT  TO_CHAR(sol.fecha_solicitud,'DD/MM/YYYY') AS fecha, " +
						"  coalesce(sc.fecha_inicial_cx,sol.FECHA_GRABACION)  AS fechabd, " +
						"  coalesce( SUBSTR(sc.hora_inicial_cx,1,5),SUBSTR(sol.hora_grabacion,1,5)) AS hora, " +
						" sol.numero_solicitud           AS codigopk," +
						" "+ConstantesImpresionHistoriaClinica.codigoSeccionCirugias+" AS codigotipoevolucion," +
						" 10                           AS ordentipoevolucion " +
						" FROM solicitudes_cirugia sc " +
						" LEFT OUTER JOIN hoja_quirurgica hq " +
						" ON (sc.numero_solicitud=hq.numero_solicitud) " +
						" LEFT OUTER JOIN hoja_anestesia ha " +
						" ON (ha.numero_solicitud=sc.numero_solicitud) " +
						" INNER JOIN solicitudes sol " +
						" ON (sol.numero_solicitud=sc.numero_solicitud) " +
						" LEFT OUTER JOIN res_sol_proc res " +
						" ON (res.numero_solicitud=sc.numero_solicitud) " +
						" WHERE sol.cuenta        in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") " +
						" AND sc.ind_qx          IN ('CX','NCTO')";
				consulta+=") ";
				esPrimero=false;*/
				
				
				existenDatosAConsultar=true;
				if(!esPrimero){
					consulta+=" UNION ";
				}
				consulta+="(";
				
				/**
				* Tipo Modificacion: Segun incidencia 5055
				* Autor: Alejandro Aguirre Luna
				* usuario: aleagulu
				* Fecha: 26/02/2013
				* Descripcion: Se especifica en el filtro IN que las cuentas devueltas deben ser
				* solo de historia clinica y en estado no anulado. 
				**/
				consulta+="  SELECT  TO_CHAR(sol.fecha_solicitud,'DD/MM/YYYY') AS fecha, " +
						"  coalesce(sc.fecha_inicial_cx,sol.FECHA_GRABACION)  AS fechabd, " +
						"  coalesce( SUBSTR(sc.hora_inicial_cx,1,5),SUBSTR(sol.hora_grabacion,1,5)) AS hora, " +
						" sol.numero_solicitud           AS codigopk," +
						" sol.cuenta as cuenta, " +
						" "+ConstantesImpresionHistoriaClinica.codigoSeccionCirugias+" AS codigotipoevolucion," +
						" 10                           AS ordentipoevolucion " +
						" FROM solicitudes sol " +
						" INNER JOIN solicitudes_cirugia sc ON (sol.numero_solicitud=sc.numero_solicitud) " +
						" LEFT OUTER JOIN res_sol_proc res ON (res.numero_solicitud=sc.numero_solicitud) " +
						" LEFT OUTER JOIN hoja_quirurgica hq ON (sc.numero_solicitud=hq.numero_solicitud) " +
						" LEFT OUTER JOIN hoja_anestesia ha ON (ha.numero_solicitud=sc.numero_solicitud)  " +
						//" WHERE sol.cuenta        in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") " +
						" WHERE sol.cuenta in(" +
						" SELECT s.cuenta" +
						"   FROM SOLICITUDES s " +
						"WHERE EXISTS (SELECT 'X' FROM cuentas c WHERE c.id =s.cuenta" +
						" AND c.id_ingreso = '"+IdIngreso+"' )";
						if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && UtilidadTexto.isEmpty(filtro.getHoraInicial()) && UtilidadTexto.isEmpty(filtro.getHoraFinal()))
						{
							consulta+=" AND sol.fecha_solicitud  between '"+filtro.getFechaInicial()+"' and '"+filtro.getFechaFinal()+"'";
						}
						else if(!UtilidadTexto.isEmpty(filtro.getFechaInicial()) && !UtilidadTexto.isEmpty(filtro.getFechaFinal()) && !UtilidadTexto.isEmpty(filtro.getHoraInicial()) && !UtilidadTexto.isEmpty(filtro.getHoraFinal()))
						{
							consulta+=" AND ( ( sol.fecha_solicitud > '"+filtro.getFechaInicial()+"') or ( sol.fecha_solicitud = '"+filtro.getFechaInicial()+"' and  sol.hora_solicitud >= '"+filtro.getHoraInicial()+"') ) " +
										" and (( sol.fecha_solicitud < '"+filtro.getFechaFinal()+"') or ( sol.fecha_solicitud= '"+filtro.getFechaFinal()+"' and  sol.hora_solicitud <= '"+filtro.getHoraFinal()+"')) ";
						}
						consulta +=
						" and s.TIPO = "+ConstantesBD.codigoTipoSolicitudCirugia+" " +
						" and s.ESTADO_HISTORIA_CLINICA != "+ConstantesBD.codigoEstadoHCAnulada+
						")" + 
						" AND sc.ind_qx IN ('CX','NCTO') " +
						/**
						 * Para que no tenga en cuenta las solicitudes de cirugia de tipo no cruento que ahora se responde como
						 * procedimiento y no como Hoja Qx (ralcionado a la MT 1528)
						 * MT 8472
						 * @author javrammo
						 */
						"AND (select count(numero_solicitud) from sol_procedimientos where numero_solicitud = sc.numero_solicitud) = 0 ";
						/**
						 * Fin MT 1528
						 */
				
				consulta+=") ";
				esPrimero=false;
				
				/*consulta+="  SELECT  TO_CHAR(sol.fecha_solicitud,'DD/MM/YYYY') AS fecha, " +
						"  coalesce(sc.fecha_inicial_cx,sol.FECHA_GRABACION)  AS fechabd, " +
						"  coalesce( SUBSTR(sc.hora_inicial_cx,1,5),SUBSTR(sol.hora_grabacion,1,5)) AS hora, " +
						" sol.numero_solicitud           AS codigopk," +
						" "+ConstantesImpresionHistoriaClinica.codigoSeccionCirugias+" AS codigotipoevolucion," +
						" 10                           AS ordentipoevolucion " +
						" FROM solicitudes_cirugia sc " +
						" LEFT OUTER JOIN hoja_quirurgica hq " +
						" ON (sc.numero_solicitud=hq.numero_solicitud) " +
						" LEFT OUTER JOIN hoja_anestesia ha " +
						" ON (ha.numero_solicitud=sc.numero_solicitud) " +
						" INNER JOIN solicitudes sol " +
						" ON (sol.numero_solicitud=sc.numero_solicitud) " +
						" LEFT OUTER JOIN res_sol_proc res " +
						" ON (res.numero_solicitud=sc.numero_solicitud) " +
						//" WHERE sol.cuenta        in("+filtro.getCuenta()+","+filtro.getCuentaAsociada()+") " +
						" WHERE sol.cuenta in(1045797)" + 
						" AND sc.ind_qx          IN ('CX','NCTO')";
				consulta+=") ";
				esPrimero=false;*/
				
			}
			consulta+=") tabla order by ";
			consulta+=" tabla.fechabd asc, tabla.hora asc, tabla.ordentipoevolucion ";
		}
		
		PreparedStatement ps=null;
		ResultSet rs= null;
		try 
		{
			/*
			 * Dado que no es necesario actualizar los datos a medida que se va recorriendo, no veo la necesidad de hacer esto.. ademas, que puede
			 * generar excepciones como SQLException : Unsupported syntax for refreshRow() cuando son mas de 10 registros y algunas BD no lo soportan 			 
			 */			
			//ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps= con.prepareStatement(consulta);			
			if (existenDatosAConsultar) {
				rs=ps.executeQuery();
				
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				while(rs.next()){
					DtoResultadoImpresionHistoriaClinica dto=new DtoResultadoImpresionHistoriaClinica();
					dto.setFecha(rs.getString("fecha"));
					dto.setFechaBD(format.format(rs.getDate("fechabd")));
					dto.setHora(rs.getString("hora"));
					dto.setCodigoPk(rs.getInt("codigopk"));
					dto.setCodigoTipoEvolucion(rs.getInt("codigotipoevolucion"));
					dto.setCodigoCuenta(rs.getInt("cuenta"));
					resultado.add(dto);
				}			
			}
			
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally{
			
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
				}
		
		return resultado;
	}

	/**
	 * 
	 * @param codigoRegistroEnfermeria
	 * @return
	 */
	public static Collection consultarSignosVitalesFijosHistoImpresionHC(int codigoRegistroEnfermeria) 
	{
		String consulta="SELECT " +
								" ehre.codigo AS codigo_histo_enfer, " +
								" svre.fc AS fc, " +
								" svre.fr AS fr, " +
								" svre.pas AS pas, " +
								" svre.pad AS pad, " +
								" svre.pam AS pam, " +
								" svre.temp AS temp " +
						" FROM enca_histo_registro_enfer ehre " +
						" INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo) " +
						" INNER JOIN signo_vital_reg_enfer svre ON (ehre.codigo=svre.codigo_histo_enfer) ";
		
		//-------Si tiene cuenta de asocio -----------//
		
		consulta+=" WHERE  ehre.codigo = "+codigoRegistroEnfermeria;
		consulta+=" ORDER BY ehre.fecha_registro, ehre.hora_registro, ehre.codigo";

		Connection con=UtilidadBD.abrirConexion();
		Collection resultado=new ArrayList();
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			resultado=UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));			
		}
		catch (SQLException e)
		{
			logger.error("Error en consultarSignosVitalesFijosHistoImpresionHC: SqlBaseRegistroEnfermeria"+e.toString());
		}
		UtilidadBD.closeConnection(con);
		return resultado;
	}
	/**
	 * 
	 * @param codigoRegistroEnfermeria
	 * @return
	 */
	public static Collection consultarSignosVitalesParamHistoImpresionHC(int codigoRegistroEnfermeria) 
	{

		String consulta="SELECT " +
								" ehre.codigo AS codigo_histo_enfer, " +
								" svrep.signo_vital_cc_inst AS signo_vital_cc_ins, " +
								" sv.codigo AS codigo_tipo, " +
								" svrep.valor AS valor_sig_vital " +
						" FROM  enca_histo_registro_enfer ehre " +
						" INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo) " +
						" INNER JOIN signo_vit_reg_enfer_par svrep ON (ehre.codigo=svrep.codigo_histo_enca) " +
						" INNER JOIN signos_vitales_cc_inst svci ON (svrep.signo_vital_cc_inst=svci.codigo) " +
						" INNER JOIN signos_vitales sv ON (svci.signo_vital = sv.codigo)";
		 

		
		//-------Si tiene cuenta de asocio -----------//
		
		consulta+=" WHERE  ehre.codigo = "+codigoRegistroEnfermeria;
		consulta+=" ORDER BY ehre.fecha_registro, ehre.hora_registro, ehre.codigo";
		
		Connection con=UtilidadBD.abrirConexion();
		Collection resultado=new ArrayList();
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			resultado=UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));			
		}
		catch (SQLException e)
		{
			logger.error("Error en consultarSignosVitalesParamHistoImpresionHC: SqlBaseRegistroEnfermeria"+e.toString());
		}
		UtilidadBD.closeConnection(con);
		return resultado;

	}

	/**
	 * 
	 * @param codigoRegistroEnfermeria
	 * @return
	 */
	public static Collection consultarSignosVitalesHistoTodosImpresionHC(int codigoRegistroEnfermeria) 
	{
		String consulta="SELECT * FROM ( " +
										" SELECT " +
												" ehre.codigo AS codigo_histo_enfer, " +
												" to_char(ehre.fecha_registro, 'dd/mm/yyyy') AS fecha_registro, " +
												" ehre.hora_registro||'' AS hora_registro, " +
												" getnombreusuario(ehre.usuario) AS nombre_usuario," +
												" ehre.fecha_registro || '' || ehre.hora_registro AS fecha " +
										" FROM  enca_histo_registro_enfer ehre  " +
										" INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo)  " +
										" INNER JOIN signo_vit_reg_enfer_par svrep ON (ehre.codigo=svrep.codigo_histo_enca) " +
										" where ehre.codigo=" +codigoRegistroEnfermeria+
										" UNION " +
										" SELECT " +
												" ehre.codigo AS codigo_histo_enfer, " +
												" to_char(ehre.fecha_registro, 'dd/mm/yyyy') AS fecha_registro," +
												" ehre.hora_registro||'' AS hora_registro, " +
												" getnombreusuario(ehre.usuario) AS nombre_usuario, " +
												" ehre.fecha_registro || '' || ehre.hora_registro AS fecha " +
										" FROM enca_histo_registro_enfer ehre " +
										" INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo) " +
										" INNER JOIN signo_vital_reg_enfer svre ON (ehre.codigo=svre.codigo_histo_enfer) " +
										" where ehre.codigo=" +codigoRegistroEnfermeria+
									")x " +
									" GROUP BY x.codigo_histo_enfer,x.fecha_registro,x.hora_registro,x.nombre_usuario,x.fecha " +
									" ORDER BY x.fecha, x.codigo_histo_enfer";
		
		
		Connection con=UtilidadBD.abrirConexion();
		Collection resultado=new ArrayList();
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			resultado=UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));			
		}
		catch (SQLException e)
		{
			logger.error("Error en consultarSignosVitalesHistoTodosImpresionHC: SqlBaseRegistroEnfermeria"+e.toString());
		}
		UtilidadBD.closeConnection(con);
		return resultado;
	}
	
	
	/**
	 * @param con
	 * @param numeroSolicitud
	 * @return informacion de las notas de recuperacion 
	 * @throws SQLException
	 */
	public static HashMap consultarTiposNotasRecuperacion (Connection con, int numeroSolicitud) throws SQLException
	{
		HashMap parametricos=new HashMap();
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultarTiposNotasRecuperacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//logger.info("consulta 1 -->"+consultarTiposNotasRecuperacionStr);
			parametricos=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
			
			int numReg=Integer.parseInt(parametricos.get("numRegistros")+"");
			for(int i = 0 ; i < numReg; i ++) 
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarHistoricoNotasRecuperacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				//logger.info("consulta 2 -->"+consultarHistoricoNotasRecuperacionStr+"---"+Utilidades.convertirAEntero(parametricos.get("codigorelacion_"+i)+"")+"--"+numeroSolicitud);
				ps.setInt(1,Utilidades.convertirAEntero(parametricos.get("codigorelacion_"+i)+""));
				ps.setInt(2, numeroSolicitud);
				parametricos.put("detNota_"+i,UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())));
			}
			return parametricos;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando los tipo de notas recuperacion parametrizados a la instituci�n [SqlBaseNotasRecuperacionDao]: "+e);
			return null;
		}
	}
	
	/**
	 * Metodo encargado de consultar las Valoraciones de un ingreso especifico
	 * @param con
	 * @param vo
	 * @return HashMap<Object, Object>
	 * @author hermorhu
	 */
	public static HashMap<Object, Object> obtenerValoraciones(Connection con, HashMap<Object, Object> parametros) {
		
		String cadena = "SELECT " +
							"v.numero_solicitud, " +
							"to_char(v.fecha_valoracion,'DD/MM/YYYY') AS fecha, " +
							"v.hora_valoracion AS hora, " +
							"getnombrepersona(v.codigo_medico) AS medico, " +
							"CASE WHEN v.cuidado_especial='"+ConstantesBD.acronimoSi+"' THEN 'Cuidado Especial' ELSE 'Hospitalizaci�n' END AS descripcion " +
						"FROM solicitudes s " +
							"INNER JOIN valoraciones v ON (v.numero_solicitud = s.numero_solicitud) " +
							"INNER JOIN cuentas c ON (c.id = s.cuenta) " +
						"WHERE c.id_ingreso = ? " +
							"AND s.tipo = "+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+" ";
		
		PreparedStatementDecorator ps = null;
		HashMap<Object, Object> resultado = null;
		
		try {
			
			if((!(parametros.get("fechaInicial")+"").trim().equals(""))&&(!(parametros.get("fechaFinal")+"").trim().equals(""))) {
				cadena+=" AND to_char(v.fecha_valoracion,'yyyy-mm-dd')  BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinal")+"")+"'";
			}
			if((!(parametros.get("horaInicial")+"").trim().equals(""))&&(!(parametros.get("horaFinal")+"").trim().equals(""))) {
				cadena+=" AND ( (to_char(v.fecha_valoracion,'yyyy-mm-dd') > '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicial")+"")+"') OR (to_char(v.fecha_valoracion,'yyyy-mm-dd') = '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicial")+"")+"' AND v.hora_valoracion >= '"+parametros.get("horaInicial")+"') ) " +
				" AND ((to_char(v.fecha_valoracion,'yyyy-mm-dd') < '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinal")+"")+"') or (to_char(v.fecha_valoracion,'yyyy-mm-dd') = '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinal")+"")+"' AND v.hora_valoracion <= '"+parametros.get("horaFinal")+"')) ";
			}
			if(!(parametros.get("cuenta")+"").trim().equals("")) {
				cadena+=" AND c.id='"+ parametros.get("cuenta")+"'";
			}
			//MT-5571
			cadena+=" AND (s.interpretacion IS NULL OR s.interpretacion <> '"+ConstantesBD.textoInterpretacionAutomatica+"')";
			cadena+=" ORDER BY v.fecha_valoracion, v.hora_valoracion ";
			
			resultado = new HashMap<Object, Object>();
			resultado.put("numRegistros", 0);
			
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1, parametros.get("ingreso"));
			
			resultado =UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		
		}catch(SQLException e){
			Log4JManager.error(e);
		} catch (Exception e) {
			Log4JManager.error(e);
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				Log4JManager.error("Error Close PreparedStatement: ", e);
			}
		}
		
		return resultado;
	}
	
	/**
	 * Metodo encargado de consultar la fecha y hora de la ultima valoracion de Consulta Externa para 
	 * una cuenta determinada
	 * @param con
	 * @param idCuenta
	 * @param consulta
	 * @return InfoIngresoDto
	 * @author hermorhu
	 * @created 24-Abr-2013
	 */
	public static InfoIngresoDto obtenerDatosUltimaValoracionConsultaExternaXCuenta(Connection con, int idCuenta, String consulta){
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		InfoIngresoDto infoIngresoDto = null;
		
		try {
			ps = con.prepareStatement(consulta);
			ps.setInt(1, idCuenta);
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				infoIngresoDto = new InfoIngresoDto(rs.getInt("idIngreso"), rs.getDate("fecha"), rs.getString("hora"));
			}

			return infoIngresoDto;
		}catch(SQLException e){
			Log4JManager.error(e);
		} catch (Exception e) {
			Log4JManager.error(e);
		} finally {
			try {
				if(ps != null) {
					ps.close();
				}
				if(rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				Log4JManager.error("Error Close PreparedStatement: ", e);
			}
		}
		return null;
	}
	
}
