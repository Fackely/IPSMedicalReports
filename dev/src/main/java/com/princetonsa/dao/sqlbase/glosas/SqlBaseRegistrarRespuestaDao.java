package com.princetonsa.dao.sqlbase.glosas;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

public class SqlBaseRegistrarRespuestaDao
{
	/**
	 * Manejo de Logs
	 */
	private static Logger logger = Logger.getLogger(SqlBaseRegistrarRespuestaDao.class);
	
	/**
	 * Consulta la glosa con la respuesta asociada y sus facturas
	 */
	private static String consultaStrGlosaEncabezad = "SELECT " +
								"rg.codigo AS codigo, coalesce(rg.valor_glosa||'','') AS valor_glosa, " +
								"coalesce(rg.glosa_sistema,'')  AS glosa_sistema, " +
								"coalesce(to_char(rg.fecha_registro_glosa,'"+ConstantesBD.formatoFechaAp+"'),'') AS fecha_registro_glosa, " +
								"rg.convenio AS codigo_convenio, " +
								"getnombreconvenio(rg.convenio) AS nombre_convenio, " +
								"coalesce(rg.glosa_entidad,'') AS glosa_entidad, " +
								"coalesce(to_char(rg.fecha_notificacion,'"+ConstantesBD.formatoFechaAp+"'),'') AS fecha_notificacion, " +
								"coalesce(rg.observaciones,'') AS observaciones, " +
								"coalesce(rg.usuario_glosa,'') AS usuario_glosa, " +
								"coalesce(rg.usuario_auditor,'') AS usuario_auditor, " +
								"coalesce(to_char(rg.fecha_auditoria,'"+ConstantesBD.formatoFechaAp+"'),'') AS fecha_auditoria, " +
								"coalesce(rg.hora_auditoria,'') AS hora_auditoria, " +
								"rg.estado AS estado, " +
								"to_char(rg.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_modifica, " +
								"rg.hora_modifica AS hora_modifica, " +
								"rg.usuario_modifica AS usuario_modifica, " +
								"rg.institucion AS codigo_institucion, " +
								"rg.contrato AS contrato, " +
								"rsg.codigo AS codigoresp, " +
								"rsg.respuesta_consecutivo AS respuesta, " +
								"coalesce(to_char(rsg.fecha_respuesta,'"+ConstantesBD.formatoFechaAp+"'),'') AS fecharesp, " +
								"rsg.conciliacion AS conciliacion, " +
								"rsg.observaciones AS observacionesresp, " +
								"coalesce(to_char(rsg.fecha_radicacion,'"+ConstantesBD.formatoFechaAp+"'),'') AS fecharadresp, " +
								"ag.fue_auditada AS indicativoglosa " +
							"FROM registro_glosas rg " +
								"LEFT OUTER JOIN respuesta_glosa rsg ON (rg.codigo=rsg.glosa) " +
								"LEFT OUTER JOIN auditorias_glosas ag ON (rg.codigo=ag.glosa) " +
							"WHERE rg.glosa_sistema = ? ";
	
	/**
	 * Consulta el encabezado de la Respuesta de una glosa con las facturas asociadas
	 */
	private static String consultaStrRespuesta="SELECT " +
								"rsg.codigo AS codresp, " +
								"rsg.respuesta_consecutivo AS respuesta, " +
								"coalesce(to_char(rsg.fecha_respuesta,'"+ConstantesBD.formatoFechaAp+"'),'') AS fecharesp, " +
								"rsg.hora_respuesta AS horaresp, " +
								"rsg.usuario_respuesta AS usuarioresp, " +
								"rsg.conciliacion AS conciliacion, " +
								"rsg.observaciones AS observaciones, " +
								"rsg.estado AS estado, " +
								"rsg.glosa AS codglosa, " +
								"frg.factura AS codfactura, " +
								"frg.codigo AS codfacresp, " +
								"f.consecutivo_factura AS factura, " +
								"ag.codigo AS codaudi, " +
								"ag.fecha_elaboracion_fact AS fechaelab, " +
								"ag.numero_cuenta_cobro AS cuentacobro, " +
								"ag.fecha_radicacion_cxc AS fecharadicacion, " +
								"f.tipo_factura_sistema AS sistema, " +
								"coalesce(f.valor_total,0)" +
									"+" +
									"coalesce(f.ajustes_debito,0)" +
										"-" +
										"coalesce(f.ajustes_credito,0)" +
											"+" +
											"coalesce(cc.ajuste_debito,0)" +
												"-" +
												"coalesce(cc.ajuste_credito,0)" +
													"-" +
													"coalesce(cc.pagos,0) AS saldofactura, " +
								"ag.valor_glosa_factura AS valorglosa, " +
								"gettodosconceptosglosas(ag.codigo,?) AS conceptos, " +
								"'"+ConstantesBD.acronimoSi+"' AS esconsulta, " +
								"'"+ConstantesBD.acronimoNo+"' AS eliminar, " +
								"coalesce(getconceptodevolucionfactura(ag.codigo),'-1') AS esdevolucion " +
							"FROM respuesta_glosa rsg " +
								"INNER JOIN fact_respuesta_glosa frg ON (frg.respuesta_glosa=rsg.codigo) " +
								"LEFT OUTER JOIN facturas f ON (f.codigo=frg.factura) " +
								"LEFT OUTER JOIN auditorias_glosas ag ON (frg.auditoria_glosa=ag.codigo) " +
								"LEFT OUTER JOIN cuentas_cobro cc ON (f.numero_cuenta_cobro=cc.numero_cuenta_cobro AND f.institucion=cc.institucion) " +
							"WHERE rsg.glosa = ? AND rsg.estado='"+ConstantesIntegridadDominio.acronimoEstadoRespuestaGlosaRegistrada+"' ";
	
	/**
	 * Consulta de las facturas por respuesta glosa
	 */
	private static String consultaStrFacturasRespuestaGlosa="SELECT rsg.codigo AS codresp, " +
																"rsg.respuesta_consecutivo AS respuesta, " +
																"rsg.glosa AS codglosa, " +
																"f.consecutivo_factura AS factura, " +
																"frsg.factura AS codfactura, " +
																"frsg.codigo AS codfacresp, " +
																"f.tipo_factura_sistema AS sistema, " +
																"ag.codigo AS codaudi, " +
																"ag.fecha_elaboracion_fact AS fechaelab, " +
																"ag.numero_cuenta_cobro AS cuentacobro, " +
																"ag.fecha_radicacion_cxc AS fecharadicacion, " +
																"coalesce(f.valor_total,0)" +
																	"+" +
																	"coalesce(f.ajustes_debito,0)" +
																		"-" +
																		"coalesce(f.ajustes_credito,0)" +
																			"+" +
																			"coalesce(cc.ajuste_debito,0)" +
																				"-" +
																				"coalesce(cc.ajuste_credito,0)" +
																					"-" +
																					"coalesce(cc.pagos,0) AS saldofactura, " +
																"getTodosConceptosGlosas(ag.codigo,?) AS conceptos, " +
																"ag.valor_glosa_factura AS valorglosa, " +
																"'"+ConstantesBD.acronimoSi+"' AS esconsulta, " +
																"'"+ConstantesBD.acronimoNo+"' AS eliminar, " +
																"coalesce(getconceptodevolucionfactura(ag.codigo),'-1') AS esdevolucion " +
															"FROM respuesta_glosa rsg " +
																"INNER JOIN fact_respuesta_glosa frsg ON (frsg.respuesta_glosa=rsg.codigo) " +
																"INNER JOIN auditorias_glosas ag ON (frsg.auditoria_glosa=ag.codigo) " +
																"INNER JOIN facturas f ON (frsg.factura=f.codigo) " +
																"LEFT OUTER JOIN cuentas_cobro cc ON (f.numero_cuenta_cobro=cc.numero_cuenta_cobro AND f.institucion=cc.institucion) " +
															"WHERE rsg.codigo=? ";
	
	/**
	 * Cadena para insertar una nueva respuesta
	 */
	private static String insertStrEncabezadoRespuesta="INSERT INTO respuesta_glosa " +
															"(codigo," + // 1
															"respuesta_consecutivo," + // 2
															"fecha_respuesta," + // 3
															"hora_respuesta," +
															"usuario_respuesta," + // 4
															"conciliacion," + // 5
															"observaciones," + // 6
															"estado," + // 7
															"institucion," + // 8
															"fecha_modifica," +
															"hora_modifica," +
															"usuario_modifica," + // 9
															"valor_respuesta," + // 10
															"glosa) " + // 11
														"VALUES (?," + // 1
															"?," + // 2
															"?," + // 3
															""+ValoresPorDefecto.getSentenciaHoraActualBD()+"," +
															"?," + // 4
															"?," + // 5
															"?," + // 6
															"?," + // 7
															"?," + // 8
															"CURRENT_DATE," +
															""+ValoresPorDefecto.getSentenciaHoraActualBD()+"," +
															"?," + // 9
															"?," + // 10
															"?) "; // 11
	
	/**
	 * Cadena para actualizar una respuesta
	 */
	private static String updateStrEncabezadoRespuesta="UPDATE respuesta_glosa SET " +
															"conciliacion=?," + // 1
															"observaciones=?," + // 2
															"fecha_modifica=CURRENT_DATE," +
															"hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+"," +
															"usuario_modifica=?," + // 3
															"valor_respuesta=? " + // 4
														"WHERE codigo=? "; // 5
	
	/**
	 * Cadena para insertar nueva factura asociada a una respuesta glosa
	 */
	private static String insertStrDetalleRespuesta="INSERT INTO fact_respuesta_glosa " +
															"(codigo," + // 1
															"respuesta_glosa," + // 2
															"factura," + // 3
															"auditoria_glosa," + // 4
															"valor_fact_resp," + // 5
															"factura_sistema) " + // 6
														"VALUES (?," + // 1
															"?," + // 2
															"?," + // 3
															"?," + // 4
															"?," + // 5
															"?) "; // 6
	
	/**
	 * Consulta las facturas que estan ligadas a la Glosa
	 */
	private static String consultaStrFacturasGlosa="SELECT ag.glosa AS codglosa, " +
													"f.consecutivo_factura AS factura, " +
													"ag.codigo AS codaudi, " +
													"f.tipo_factura_sistema AS sistema, " +
													"ag.codigo_factura AS codfactura, " +
													"ag.fecha_elaboracion_fact AS fechaelabora, " +
													"ag.numero_cuenta_cobro AS cxc, " +
													"ag.fecha_radicacion_cxc AS fecharad, " +
													"coalesce(f.valor_total,0)" +
														"+" +
														"coalesce(f.ajustes_debito,0)" +
															"-" +
															"coalesce(f.ajustes_credito,0)" +
																"+" +
																"coalesce(cc.ajuste_debito,0)" +
																	"-" +
																	"coalesce(cc.ajuste_credito,0)" +
																		"-" +
																		"coalesce(cc.pagos,0) AS saldofactura, " +
													"getTodosConceptosGlosas(ag.codigo,?) AS conceptos, " +
													"ag.valor_glosa_factura AS valorglosafac, " +
													"'"+ConstantesBD.acronimoNo+"' AS seleccionado " +
												"FROM auditorias_glosas ag " +
													"INNER JOIN facturas f ON (ag.codigo_factura=f.codigo) " +
													"LEFT OUTER JOIN cuentas_cobro cc ON (f.numero_cuenta_cobro=cc.numero_cuenta_cobro AND f.institucion=cc.institucion) " +
												"WHERE ag.glosa=? ";
	
	/**
	 * Cadena para eliminar todos los asocios de respuesta glosa por codigo det fact resp
	 */
	private static String eliminarStrAsociosRespuesta="DELETE FROM asocios_resp_glosa WHERE det_fact_resp_glosa=? ";
	
	/**
	 * Cadena para eliminar todos los detalles factura respuesta por codigo fact_respuesta
	 */
	private static String eliminarStrDetalleFacturaRespuesta="DELETE FROM det_fact_resp_glosa WHERE fact_respuesta_glosa=? ";
	
	/**
	 * Cadena para eliminar el detalle de Facturas Externa Respuesta
	 */
	private static String eliminarStrDetalleFacturaExtRespuesta="DELETE FROM det_fact_ext_resp_glosa WHERE fact_respuesta_glosa=? ";
	
	/**
	 * Cadena para eliminar todas las facturas asociadas a una respuesta por codigo respuesta
	 */
	private static String eliminarStrFacturasRespuesta="DELETE FROM fact_respuesta_glosa WHERE codigo=? ";
	
	/**
	 * Consulta los detalles factura Respuesta por cada factura respuesta
	 */
	private static String consultaStrDetalleFacturaResp="SELECT codigo, fact_respuesta_glosa FROM det_fact_resp_glosa WHERE fact_respuesta_glosa=? ";
	
	/**
	 * Consulta detalle factura, solicitudes servicio/articulo
	 */
	private static String consultaStrSolicitudesDetalleFactura="SELECT dag.codigo AS coddetfact, " +
														"dag.auditoria_glosa AS codaudi, " +
														"dag.det_factura_solicitud AS detfactsol, " +
														"frg.codigo AS codfactresp, " +
														"s.consecutivo_ordenes_medicas AS solicitud, " +
														"s.numero_solicitud AS codsolicitud, " +
														"CASE WHEN dag.servicio IS NULL THEN " +
															"getdescarticulo(dag.articulo) " +
															"ELSE '(' || getcodigopropservicio2(dag.servicio,"+ConstantesBD.codigoTarifarioCups+") || ') ' || getnombreservicio(dag.servicio, "+ConstantesBD.codigoTarifarioCups+") " +
																	"END AS descartserv, " +
														"CASE WHEN dag.servicio IS NULL THEN " +
															"getnomcentrocosto(s.centro_costo_solicitante) " +
															"ELSE getnomcentrocosto(s.centro_costo_solicitado) " +
																	"END AS nomcentrocosto, " +
														"cdag.codigo AS codcdag, " +
														"cg.codigo AS codconcepto, " +
														"cg.descripcion AS descconcepto, " +
														"dag.cantidad_glosa AS cantidadglosa, " +
														"dag.valor_glosa AS valorsolglosa, " +
														"CASE WHEN dfrg.concepto_respuesta IS NOT NULL THEN dfrg.concepto_respuesta || '"+ConstantesBD.separadorSplit+"' || coalesce(ca.naturaleza,"+ConstantesBD.codigoNuncaValido+") || '"+ConstantesBD.separadorSplit+"' || coalesce(ca.codigo,'') ELSE '"+ConstantesBD.codigoNuncaValido+"' END AS codconceptor, " +
														"CASE WHEN dfrg.concepto_respuesta IS NOT NULL THEN '"+ConstantesBD.acronimoNo+"' ELSE '"+ConstantesBD.acronimoSi+"' END AS nuevo, " +
														"dfrg.codigo AS coddetfactresp, " +
														"dfrg.motivo AS motivoconcresp, " +
														"dfrg.cantidad_respuesta AS cantidadresp, " +
														"dfrg.valor_respuesta AS valorresp, " +
														"CASE WHEN dfrg.ajuste IS NOT NULL AND ae.estado<>"+ConstantesBD.codigoEstadoCarteraAnulado+" THEN dfrg.ajuste ELSE 0 END AS tieneajuste, " +
														"ae.consecutivo_ajuste AS ajuste, " +
														"'"+ConstantesBD.acronimoNo+"' AS checkajuste, " +
														"dfs.tipo_solicitud as codigotiposolicitud, " +
														"dfs.codigo AS detfactsolicitud, " +
														"dfs.pool AS pool, " +
														"dfs.codigo_medico AS codigomedico, " +
														"getnomtiposolicitud(dfs.tipo_solicitud) as nombretiposolicitud, " +
														"ag.numero_cuenta_cobro AS numerocxc, " +
														"ag.codigo_factura AS factura, " +
														"CASE WHEN s.tipo="+ConstantesBD.codigoTipoSolicitudCirugia+" THEN 'true' ELSE 'false' END AS escirugia, " +
														"ta.descripcion as desctipoajuste, " +
														"1 as modificable " +
													"FROM det_auditorias_glosas dag " +
														"INNER JOIN auditorias_glosas ag ON (dag.auditoria_glosa=ag.codigo) " +
														"INNER JOIN fact_respuesta_glosa frg ON (frg.auditoria_glosa=ag.codigo) " +
														"INNER JOIN det_factura_solicitud dfs on (dfs.codigo = dag.det_factura_solicitud) " +
														"INNER JOIN solicitudes s ON (dag.solicitud=s.numero_solicitud) " +
														"INNER JOIN conceptos_det_audi_glosas cdag ON (cdag.det_audi_fact=dag.codigo) " +
														"INNER JOIN concepto_glosas cg ON (cdag.concepto_glosa=cg.codigo AND cdag.institucion=cg.institucion) " +
														"LEFT OUTER JOIN det_fact_resp_glosa dfrg ON (dfrg.det_auditoria_glosa=dag.codigo AND dfrg.concepto_glosa=cdag.concepto_glosa) " +
														"LEFT OUTER JOIN concepto_resp_glosas crg ON (dfrg.concepto_respuesta=crg.codigo AND dfrg.institucion=crg.institucion) " +
														"LEFT OUTER JOIN concepto_ajustes_cartera ca ON (crg.concepto_ajuste=ca.codigo AND crg.institucion=ca.institucion) " +
														"LEFT OUTER JOIN cartera.ajus_det_fact_empresa adfe ON(dfrg.ajuste=adfe.codigo_pk) " +
														"LEFT OUTER JOIN ajustes_empresa ae ON (adfe.codigo=ae.codigo) " +
														"LEFT OUTER JOIN tipos_ajuste ta ON(ca.naturaleza=ta.codigo) " +
													"WHERE dag.auditoria_glosa=? ";
	
	/**
	 * Consulta Detalle Solicitud, cuando se tienen asocios de Cirugia
	 */
	private static String consultaStrAsociosSolRespuesta="SELECT DISTINCT " +
														"aag.codigo AS codigoasocio, " +
														"aag.asocio_det_factura AS asociodetfactura, " +
														"aag.det_auditoria_glosa AS detauditoriaglosa, " +
														"aag.tipo_asocio AS tipoasocio, " +
														"ta.nombre_asocio AS nombreasocio, " +
														"dag.solicitud AS codsolicitud, " +
														"coalesce (aag.codigo_medico||'','') AS codigomedico, " +
														"getnombrepersona(aag.codigo_medico) AS nombremedico, " +
														"coalesce(aag.cantidad_glosa||'','') AS cantidadglosa, " +
														"coalesce(aag.valor_glosa||'','') AS valorglosa, " +
														"aag.servicio_asocio AS servicioasocio, " +
														"getcodigopropservicio2(aag.servicio_asocio,"+ConstantesBD.codigoTarifarioCups+") AS codigopropservicioasocio, " +
														"coalesce(aag.cantidad||'','') AS cantidad, " +
														"coalesce(aag.valor||'','') AS valor, " +
														"caag.codigo AS codcaag, " +
														"cg.codigo AS codconcepto, " +
														"cg.descripcion AS descconcepto, " +
														"CASE WHEN arg.concepto_respuesta IS NOT NULL THEN arg.concepto_respuesta || '"+ConstantesBD.separadorSplit+"' || coalesce(ca.naturaleza,"+ConstantesBD.codigoNuncaValido+") || '"+ConstantesBD.separadorSplit+"' || coalesce(ca.codigo,'') ELSE '"+ConstantesBD.codigoNuncaValido+"' END AS codconceptor, " +
														"CASE WHEN arg.concepto_respuesta IS NOT NULL THEN '"+ConstantesBD.acronimoNo+"' ELSE '"+ConstantesBD.acronimoSi+"' END AS nuevo, " +
														"arg.motivo AS motivoconcresp, " +
														"arg.cantidad_respuesta AS cantidadresp, " +
														"arg.valor_respuesta AS valorresp, " +
														"CASE WHEN arg.ajuste IS NOT NULL THEN arg.ajuste ELSE 0 END AS tieneajuste, " +
														"ae.consecutivo_ajuste AS ajuste, " +
														"'"+ConstantesBD.acronimoNo+"' AS checkajuste, " +
														"dfs.tipo_solicitud as codigotiposolicitud, " +
														"dfs.codigo AS detfactsolicitud, " +
														"dfs.pool AS pool, " +
														"dfs.codigo_medico AS codigomedico, " +
														"getnomtiposolicitud(dfs.tipo_solicitud) as nombretiposolicitud, " +
														"ag.numero_cuenta_cobro AS numerocxc, " +
														"ag.codigo_factura AS factura, " +
														"CASE WHEN s.tipo="+ConstantesBD.codigoTipoSolicitudCirugia+" THEN 'true' ELSE 'false' END AS escirugia, " +
														"adf.consecutivo AS consecutivoasodetfact, " +
														"adf.servicio_asocio AS servicioaso, " +
														"adf.codigo_medico AS codigomedicoaso, " +
														"adf.pool AS poolaso, " +
														"adf.porcentaje_pool AS porcentajepoolaso " +
													"FROM det_auditorias_glosas dag " +
														"INNER JOIN auditorias_glosas ag ON (dag.auditoria_glosa=ag.codigo) " +
														"INNER JOIN solicitudes s ON (dag.solicitud=s.numero_solicitud) " +
														"INNER JOIN det_factura_solicitud dfs on (dfs.codigo = dag.det_factura_solicitud) " +
														"INNER JOIN asocios_auditorias_glosas aag ON (aag.det_auditoria_glosa=dag.codigo) " +
														"INNER JOIN asocios_det_factura adf ON (aag.asocio_det_factura=adf.consecutivo) " +
														"INNER JOIN tipos_asocio ta ON (aag.tipo_asocio = ta.codigo) " +
														"INNER JOIN conceptos_aso_audi_glosas caag ON (caag.asocio_auditoria_glosa=aag.codigo) " +
														"INNER JOIN concepto_glosas cg ON (caag.concepto_glosa=cg.codigo AND caag.institucion=cg.institucion) " +
														"LEFT OUTER JOIN asocios_resp_glosa arg ON (arg.asocio_auditoria_glosa=aag.codigo AND arg.concepto_glosa=caag.concepto_glosa) " +
														"LEFT OUTER JOIN concepto_resp_glosas crg ON (arg.concepto_respuesta=crg.codigo AND arg.institucion=crg.institucion) " +
														"LEFT OUTER JOIN concepto_ajustes_cartera ca ON (crg.concepto_ajuste=ca.codigo AND crg.institucion=ca.institucion) " +
														"LEFT OUTER JOIN ajustes_empresa ae ON (arg.ajuste=ae.codigo) " +
													"WHERE dag.auditoria_glosa=? ";
	
	/**
	 * Consulta conceptos respuesta
	 */
	private static String consultaStrConceptosRespuesta="SELECT " +
														"cr.codigo, " +
														"cr.concepto_glosa AS conceptoglosa, " +
														"cr.descripcion, " +
														"CASE WHEN ca.naturaleza IS NOT NULL THEN ca.naturaleza ELSE "+ConstantesBD.codigoNuncaValido+" END AS naturaleza, " +
														"ca.codigo AS codajuste, '"+ConstantesBD.acronimoNo+"' AS selec " +
													"FROM concepto_resp_glosas cr " +
														"LEFT OUTER JOIN concepto_ajustes_cartera ca ON (cr.concepto_ajuste=ca.codigo AND cr.institucion=ca.institucion) " +														
													"WHERE tipo_concepto=? ";
	/**
	 * Consulta Detalle Factura Externa
	 */
	private static String consultaStrDetalleFacturaExterna="SELECT ag.codigo AS codaudi, " +
														"fr.codigo AS codfactresp, " +
														"cg.codigo AS codconcepto, " +
														"cg.descripcion AS descconcepto, " +
														"ag.valor_glosa_factura AS valorglosafac, " +
														"CASE WHEN dfe.concepto_respuesta IS NOT NULL THEN dfe.concepto_respuesta || '"+ConstantesBD.separadorSplit+"' || coalesce(ca.naturaleza,"+ConstantesBD.codigoNuncaValido+") || '"+ConstantesBD.separadorSplit+"' || coalesce(ca.codigo,'') ELSE '"+ConstantesBD.codigoNuncaValido+"' END AS codconceptor, " +
														"dfe.motivo AS motivores, " +
														"dfe.valor_respuesta AS valorresp, " +
														"CASE WHEN dfe.ajuste IS NOT NULL THEN dfe.ajuste ELSE 0 END AS tieneajuste, " +
														"ae.consecutivo_ajuste AS ajuste, " +
														"'"+ConstantesBD.acronimoNo+"' AS checkajuste, " +
														"ag.numero_cuenta_cobro AS numerocxc, " +
														"ag.codigo_factura AS factura, " +
														"CASE WHEN dfe.concepto_respuesta IS NOT NULL THEN '"+ConstantesBD.acronimoNo+"' ELSE '"+ConstantesBD.acronimoSi+"' END AS nuevo, " +
														"dfe.codigo AS coddetfactresp " +
													"FROM auditorias_glosas ag " +
														"INNER JOIN conceptos_audi_glosas cag ON (cag.auditoria_glosa=ag.codigo) " +
														"INNER JOIN concepto_glosas cg ON (cag.concepto_glosa=cg.codigo AND cag.institucion=cg.institucion) " +
														"INNER JOIN fact_respuesta_glosa fr ON (fr.auditoria_glosa=ag.codigo) " +
														"LEFT OUTER JOIN det_fact_ext_resp_glosa dfe ON (dfe.fact_respuesta_glosa=fr.codigo AND dfe.concepto_glosa=cag.concepto_glosa) " +
														"LEFT OUTER JOIN concepto_resp_glosas crg ON (dfe.concepto_respuesta=crg.codigo AND dfe.institucion=crg.institucion) " +
														"LEFT OUTER JOIN concepto_ajustes_cartera ca ON (crg.concepto_ajuste=ca.codigo AND crg.institucion=ca.institucion) " +
														"LEFT OUTER JOIN ajustes_empresa ae ON (dfe.ajuste=ae.codigo) " +
													"WHERE ag.codigo=? ";
	
	/**
	 * Query que inserta un detalle Factura Respuesta
	 */
	private static String insertStrDetalleFacturaRespuesta="INSERT INTO det_fact_resp_glosa (" +
														"codigo," + // 1
														"fact_respuesta_glosa," + // 2
														"det_auditoria_glosa," + // 3
														"det_factura_solicitud," + // 4
														"concepto_glosa," + // 5
														"concepto_respuesta," + // 6
														"institucion," + // 7
														"motivo," + // 8
														"cantidad_respuesta," + // 9
														"valor_respuesta," + // 10
														"ajuste) " + // 11
													"VALUES (?,?,?,?,?,?,?,?,?,?,?) ";
	
	/**
	 * Query que inserta un asocio de un detalle factura Respuesta
	 */
	private static String insertStrAsocioDetalleFacturaRespuesta="INSERT INTO asocios_resp_glosa (" +
														"codigo," + // 1
														"det_fact_resp_glosa," + // 2
														"asocio_auditoria_glosa," + // 3
														"asocio_det_factura," + // 4
														"concepto_glosa," + // 5
														"concepto_respuesta," + // 6
														"institucion," + // 7
														"motivo," + // 8
														"cantidad_respuesta," + // 9
														"valor_respuesta," + // 10
														"ajuste) " + // 11
													"VALUES (?,?,?,?,?,?,?,?,?,?,?) ";
	
	/**
	 * Query que actualiza un detalle Factura Respuesta
	 */
	private static String updateStrDetalleFacturaRespuesta="UPDATE det_fact_resp_glosa SET " +
														"concepto_respuesta=?, " + // 1
														"motivo=?, " + // 2
														"cantidad_respuesta=?, " + // 3
														"valor_respuesta=?, " + // 4
														"ajuste=? " + // 5
													"WHERE codigo=? "; // 6
	
	/**
	 * Query que actualiza un asocio de un detalle factura Respuesta
	 */
	private static String updateStrAsocioDetalleFacturaRespuesta="UPDATE asocios_resp_glosa SET " +
														"concepto_respuesta=?, " + // 1
														"motivo=?, " + // 2
														"cantidad_respuesta=?, " + // 3
														"valor_respuesta=?, " + // 4
														"ajuste=? " + // 5
													"WHERE codigo=? "; // 6
	
	/**
	 * Query que inserta un detalle Factura Externa Respuesta
	 */
	private static String insertStrDetalleFacturaExtRespuesta="INSERT INTO det_fact_ext_resp_glosa (" +
														"codigo," + // 1
														"fact_respuesta_glosa," + // 2
														"concepto_glosa," + // 3
														"concepto_respuesta," + // 4
														"institucion," + // 5
														"motivo," + // 6
														"valor_respuesta," + // 7
														"ajuste) " + // 8
													"VALUES (?,?,?,?,?,?,?,?) ";	
	
	/**
	 * Query que actualiza un detalle Factura Externa Respuesta
	 */
	private static String updateStrDetalleFacturaExtRespuesta="UPDATE det_fact_ext_resp_glosa SET " +
														"concepto_respuesta=?, " + // 1
														"motivo=?, " + // 2
														"cantidad_respuesta=?, " + // 3
														"valor_respuesta=?, " + // 4
														"ajuste=? " + // 5
													"WHERE codigo=? "; // 6
		
	
	
	/**
	 * Metodo que inserta un detalle Factura Respuesta
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static int insertarDetalleFacturaRespuesta(Connection con, HashMap criterios)
	{
		PreparedStatementDecorator ps;
		
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(insertStrDetalleFacturaRespuesta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	
							
			int valorseq=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_det_fact_resp_glosa");
			ps.setInt(1, valorseq);
			logger.info("\n\nVALOR SEQ------->"+valorseq+"\n\nCRITERIOS----->>"+criterios);
			ps.setInt(2, Utilidades.convertirAEntero(criterios.get("fact_resp_glosa")+""));
			ps.setInt(3, Utilidades.convertirAEntero(criterios.get("det_auditoria_glosa")+""));
			ps.setInt(4, Utilidades.convertirAEntero(criterios.get("det_factura_solicitud")+""));
			ps.setString(5, criterios.get("concepto_glosa")+"");
			ps.setString(6, criterios.get("concepto_respuesta")+"");
			ps.setString(7, criterios.get("institucion")+"");
			ps.setString(8, criterios.get("motivo")+"");
			ps.setInt(9, Utilidades.convertirAEntero(criterios.get("cantidad_respuesta")+""));
			ps.setInt(10, Utilidades.convertirAEntero(criterios.get("valor_respuesta")+""));
			if(Utilidades.convertirADouble(criterios.get("ajuste")+"")>0)
				ps.setString(11, criterios.get("ajuste")+"");
			else
				ps.setNull(11, Types.DOUBLE);
			
			if(ps.executeUpdate()>0)
				return valorseq;
			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. INSERTANDO DETALLE FACTURA RESPUESTA GLOSA------>>>>>>"+e);
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Metodo que inserta un detalle Factura Externa Respuesta
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static int insertarDetalleFacturaExternaRespuesta(Connection con, HashMap criterios)
	{
		PreparedStatementDecorator ps;
		logger.info("\n\nMAPA CRITERIOS INSERTANDO DETALLE FACTURA EXTERNA RESPUESTA>>>>>>>>>>>>"+criterios);
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(insertStrDetalleFacturaExtRespuesta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	
							
			int valorseq=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_det_fact_ext_resp_glosa");
			ps.setInt(1, valorseq);
			ps.setString(2, criterios.get("fact_resp_glosa")+"");
			ps.setString(3, criterios.get("concepto_glosa")+"");
			ps.setString(4, criterios.get("concepto_respuesta")+"");
			ps.setString(5, criterios.get("institucion")+"");
			ps.setString(6, criterios.get("motivo")+"");
			ps.setString(7, criterios.get("valor_respuesta")+"");
			if(Utilidades.convertirADouble(criterios.get("ajuste")+"")>0)
				ps.setString(8, criterios.get("ajuste")+"");
			else
				ps.setNull(8, Types.DOUBLE);
		
			
			if(ps.executeUpdate()>0)
				return valorseq;
			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. INSERTANDO DETALLE FACTURA EXTERNA RESPUESTA GLOSA------>>>>>>"+e);
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Metodo que inserta un Asocio detalle Factura Respuesta
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static int insertarAsocioDetalleFacturaRespuesta(Connection con, HashMap criterios)
	{
		PreparedStatementDecorator ps;
		logger.info("\n\nMAPA CRITERIOS INSERTANDO ASOCIO DETALLE FACTURA RESPUESTA>>>>>>>>>>>>"+criterios);
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(insertStrAsocioDetalleFacturaRespuesta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	
							
			int valorseq=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_asocios_resp_glosa");
			ps.setInt(1, valorseq);
			ps.setString(2, criterios.get("det_fact_resp_glosa")+"");
			ps.setString(3, criterios.get("asocio_auditoria_glosa")+"");
			ps.setString(4, criterios.get("asocio_det_factura")+"");
			ps.setString(5, criterios.get("concepto_glosa")+"");
			ps.setString(6, criterios.get("concepto_respuesta")+"");
			ps.setString(7, criterios.get("institucion")+"");
			ps.setString(8, criterios.get("motivo")+"");
			ps.setString(9, criterios.get("cantidad_respuesta")+"");
			ps.setString(10, criterios.get("valor_respuesta")+"");
			if(Utilidades.convertirADouble(criterios.get("ajuste")+"")>0)
				ps.setString(11, criterios.get("ajuste")+"");
			else
				ps.setNull(11, Types.DOUBLE);
			
			if(ps.executeUpdate()>0)
				return valorseq;
			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. INSERTANDO ASOCIO DETALLE FACTURA RESPUESTA GLOSA------>>>>>>"+e);
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Metodo que actualiza un detalle factura respuesta por concepto
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static boolean actualizarDetalleFacturaRespuesta(Connection con, HashMap criterios)
	{
		PreparedStatementDecorator ps;
		logger.info("\n\nMAPA CRITERIOS ACTUALIZANDO DETALLE FACTURA RESPUESTA>>>>>>>>>>>>"+criterios);
		try
		{			
			ps= new PreparedStatementDecorator(con.prepareStatement(updateStrDetalleFacturaRespuesta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	
						
			ps.setString(1, criterios.get("concepto_respuesta")+"");
			ps.setString(2, criterios.get("motivo")+"");
			ps.setString(3, criterios.get("cantidad_respuesta")+"");
			ps.setString(4, criterios.get("valor_respuesta")+"");
			logger.info("\n\najuste: "+criterios.get("ajuste"));
			if(Utilidades.convertirADouble(criterios.get("ajuste")+"")>0)
				ps.setString(5, criterios.get("ajuste")+"");
			else
				ps.setNull(5, Types.DOUBLE);
			ps.setString(6, criterios.get("codigo")+"");
			
			ps.executeUpdate();
			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. ACTUALIZANDO DETALLE FACTURA RESPUESTA GLOSA------>>>>>>"+e);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Metodo que actualiza un detalle factura externa respuesta por concepto
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static boolean actualizarDetalleFacturaExternaRespuesta(Connection con, HashMap criterios)
	{
		PreparedStatementDecorator ps;
		logger.info("\n\nMAPA CRITERIOS ACTUALIZANDO DETALLE FACTURA EXTERNA RESPUESTA>>>>>>>>>>>>"+criterios);
		try
		{			
			ps= new PreparedStatementDecorator(con.prepareStatement(updateStrDetalleFacturaExtRespuesta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	
						
			ps.setString(1, criterios.get("concepto_respuesta")+"");
			ps.setString(2, criterios.get("motivo")+"");
			ps.setString(3, criterios.get("cantidad_respuesta")+"");
			ps.setString(4, criterios.get("valor_respuesta")+"");
			if(Utilidades.convertirADouble(criterios.get("ajuste")+"")>0)
				ps.setString(5, criterios.get("ajuste")+"");
			else
				ps.setNull(5, Types.DOUBLE);
			ps.setString(6, criterios.get("codigo")+"");
			
			ps.executeUpdate();
			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. ACTUALIZANDO DETALLE FACTURA EXTERNA RESPUESTA GLOSA------>>>>>>"+e);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Metodo que actualiza un asocio detalle factura respuesta por concepto
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static boolean actualizarAsocioDetalleFacturaRespuesta(Connection con, HashMap criterios)
	{
		PreparedStatementDecorator ps;
		logger.info("\n\nMAPA CRITERIOS ACTUALIZANDO ASOCIO DETALLE FACTURA RESPUESTA>>>>>>>>>>>>"+criterios);
		try
		{			
			ps= new PreparedStatementDecorator(con.prepareStatement(updateStrAsocioDetalleFacturaRespuesta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	
						
			ps.setString(1, criterios.get("concepto_respuesta")+"");
			ps.setString(2, criterios.get("motivo")+"");
			ps.setString(3, criterios.get("cantidad_respuesta")+"");
			ps.setString(4, criterios.get("valor_respuesta")+"");
			if(Utilidades.convertirADouble(criterios.get("ajuste")+"")>0)
				ps.setString(5, criterios.get("ajuste")+"");
			else
				ps.setNull(5, Types.DOUBLE);
			ps.setString(6, criterios.get("codigo")+"");
			
			ps.executeUpdate();
			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. ACTUALIZANDO ASOCIO DETALLE FACTURA RESPUESTA GLOSA------>>>>>>"+e);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Metodo que consulta el detalle de una factura externa
	 * @param con
	 * @param codAudi
	 * @return
	 */
	public static HashMap consultaDetalleFacturasExternas(Connection con, String codAudi)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator ps;
		logger.info("\n\nPARAMETRO>>>>>>>>>"+codAudi);
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaStrDetalleFacturaExterna,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codAudi);
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);						
	
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO DETALLE FACTURA EXTERNA------>>>>>>"+e);
			e.printStackTrace();
		}
		logger.info("\n\nMAPA CONSULTANDO DETALLE FACTURA EXTERNA >>>>>>>>>>>"+resultados);
		return resultados;
	}
	
	/**
	 * Metodo que consulta los conceptos respuesta por tipo concepto
	 * @param con
	 * @param tipoConcepto
	 * @return
	 */
	public static HashMap consultaConceptosRespuesta(Connection con, String tipoConcepto)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator ps;
		logger.info("\n\nconsultaConceptosRespuesta / "+consultaStrConceptosRespuesta);
		logger.info("Tipo Concepto = "+tipoConcepto);
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaStrConceptosRespuesta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, tipoConcepto);
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO CONCEPTOS DE RESPUESTA------>>>>>>"+e);
			e.printStackTrace();
		}
		logger.info("\n\nCONSULTA CONCEPTOS RESPUESTA------->"+consultaStrConceptosRespuesta+"\n\nMAPA CONSULTANDO CONCEPTOS DE RESPUESTA >>>>>>>>>>>"+resultados);
		return resultados;
	}
	
	/**
	 * Metodo que consulta todas las solicitudes servicio/articulo asociadas a una factura glosa
	 * @param con
	 * @param codAudi
	 * @return
	 */
	public static HashMap consultaSolicitudesDetalleFactura(Connection con, String codAudi)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator ps;
		logger.info("\n\nPARAMETRO>>>>>>>>>"+codAudi);
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaStrSolicitudesDetalleFactura,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codAudi);
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);						
	
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO SOLICITUDES DETALLE FACTURA------>>>>>>"+e);
			e.printStackTrace();
		}
		logger.info("\n\nMAPA CONSULTANDO SOLICITUDES DETALLE FACTURA >>>>>>>>>>>"+resultados);
		return resultados;
	}
	
	/**
	 * Metodo que consulta todos los asocios de todas las solicitudes de una factura glosa
	 * @param con
	 * @param codAudi
	 * @return
	 */
	public static HashMap consultaAsociosDetalleFactura(Connection con, String codAudi)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator ps;
		logger.info("\n\nPARAMETRO>>>>>>>>>"+codAudi);
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaStrAsociosSolRespuesta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codAudi);
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);						
	
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO ASOCIOS SOLICITUDES DETALLE FACTURA------>>>>>>"+e);
			e.printStackTrace();
		}
		logger.info("\n\nCONSULTA ASOCIOS----->"+consultaStrAsociosSolRespuesta+"\n\nMAPA CONSULTANDO ASOCIOS SOLICITUDES DETALLE FACTURA >>>>>>>>>>>"+resultados);
		return resultados;
	}
	
	/**
	 * Metodo que consulta el detalle factura respuesta por codigo factura respuesta
	 * @param con
	 * @param codfacresp
	 * @return
	 */
	public static HashMap consultaDetalleFacturaRespuesta(Connection con, String codfacresp)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator ps;
		logger.info("\n\nPARAMETRO>>>>>>>>>"+codfacresp);
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaStrDetalleFacturaResp,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codfacresp);
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);						
	
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO DETALLE FACTURA RESPUESTA POR FACTURA RESPUESTA------>>>>>>"+e);
			e.printStackTrace();
		}
		logger.info("\n\nMAPA CONSULTANDO DETALLE FACTURA RESPUESTA POR FACTURA RESPUESTA>>>>>>>>>>>"+resultados);
		return resultados;
	}
	
	/**
	 * Metodo que elimina todos los asocios de respuesta glosa
	 * @param con
	 * @param codigoDetalleFactura
	 * @return
	 */
	public static boolean eliminarAsociosRespuesta(Connection con, String codigoDetalleFactura)
	{
		PreparedStatementDecorator ps;		
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(eliminarStrAsociosRespuesta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigoDetalleFactura);
			
			ps.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. ELIMINANDO ASOCIOS DETALLE FACTURA RESPUESTA GLOSA SQL------>>>>>>"+e);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Metodo que elimina todos los detalles de factura Respuesta
	 * @param con
	 * @param codigoDetalleFactura
	 * @return
	 */
	public static boolean eliminarDetalleFacturaRespuesta(Connection con, String facturaRespuesta)
	{
		PreparedStatementDecorator ps;		
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(eliminarStrDetalleFacturaRespuesta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, facturaRespuesta);
			
			ps.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. ELIMINANDO DETALLES FACTURA RESPUESTA GLOSA SQL------>>>>>>"+e);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Metodo que elimina todos los detalles de facturas Externas Respuesta
	 * @param con
	 * @param codigoDetalleFactura
	 * @return
	 */
	public static boolean eliminarDetalleFacturaExtRespuesta(Connection con, String facturaRespuesta)
	{
		PreparedStatementDecorator ps;		
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(eliminarStrDetalleFacturaExtRespuesta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, facturaRespuesta);
			
			ps.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. ELIMINANDO DETALLES FACTURA EXTERNA RESPUESTA GLOSA SQL------>>>>>>"+e);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Metodo que elimina una factura Respuesta
	 * @param con
	 * @param codigoDetalleFactura
	 * @return
	 */
	public static boolean eliminarFacturaRespuesta(Connection con, String codfacresp)
	{
		PreparedStatementDecorator ps;		
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(eliminarStrFacturasRespuesta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codfacresp);
			
			ps.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. ELIMINANDO FACTURA RESPUESTA GLOSA SQL------>>>>>>"+e);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Metodo que consulta el Encabezado de la Glosa por glosaSistema
	 * @param con
	 * @param glosaSistema
	 * @return
	 */
	public static HashMap consultaEncabezadoGlosa(Connection con, String glosaSistema, String llamado)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator ps;
		String consulta= consultaStrGlosaEncabezad;
		
		if(llamado.equals("Registrar"))
			consulta += "AND rg.estado='"+ConstantesIntegridadDominio.acronimoEstadoGlosaAprobada+"' ";
			
		logger.info("\n\nGLOSA SISTEMA>>>>>>>>>"+glosaSistema);
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, glosaSistema);
						
			logger.info("\n\nCONSULTA DETALLE GLOSA ----->>>>>>>>>>>"+consulta);
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), false, true);						
	
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO ENCABEZADO GLOSA POR GLOSA SISTEMA------>>>>>>"+e);
			e.printStackTrace();
		}
		return resultados;
	}
	
	/**
	 * Metodo que consulta la respuesta de la glosa si la hay con los detalles facturas
	 * @param con
	 * @param glosaSistema
	 * @return
	 */
	public static HashMap consultaRespuestaDetalleGlosa(Connection con, String glosaSistema)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator ps;
		logger.info("\n\nPARAMETRO>>>>>>>>>"+glosaSistema);
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaStrRespuesta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, " - ");
			ps.setString(2, glosaSistema);
						
			logger.info("\n\nCONSULTA RESPUESTA GLOSA DETALLE FACTURA ----->>>>>>>>>>>"+consultaStrRespuesta);
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);						
				
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO RESPUESTA CON DETALLE FACTURAS POR GLOSA SISTEMA------>>>>>>"+e);
			e.printStackTrace();
		}
		logger.info("\n\nMAPA CONSULTA RESPUESTA DETALEE GLOSA>>>>>>>>>>>"+resultados);
		return resultados;
	}
	
	/**
	 * Metodo que consulta todas las facturas asociadas a la respuesta glosa
	 * @param con
	 * @param respuesta
	 * @return
	 */
	public static HashMap consultaFacturasRespuestaGlosa(Connection con, String respuesta)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator ps;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaStrFacturasRespuestaGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, " - ");
			ps.setString(2, respuesta);
						
			logger.info("\n\nCONSULTA FACTURAS ASOC A RESPUESTA GLOSA ----->>>>>>>>>>>"+consultaStrFacturasRespuestaGlosa);
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);						
	
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO FACTURAS ASOC A RESPUESTA GLOSA------>>>>>>"+e);
			e.printStackTrace();
		}
		return resultados;
	}
	
	/**
	 * Metodo que consulta las facturas que estan asociadas a una glosa
	 * @param con
	 * @param codGlosa
	 * @return
	 */
	public static HashMap consultaFacturasPorGlosa(Connection con, String codGlosa, String factura, String fecha)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator ps;
		
		String consulta=consultaStrFacturasGlosa;
		
		if(!factura.equals(""))
			consulta += " AND f.consecutivo_factura= '"+factura+"'";
		if(!fecha.equals(""))
			consulta += " AND f.fecha= '"+UtilidadFecha.conversionFormatoFechaABD(fecha)+"'";
		
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, " - ");
			ps.setString(2, codGlosa);		
			
						
			logger.info("\n\nCONSULTA FACTURAS POR GLOSA ----->>>>>>>>>>>"+consulta);
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);	
			
			logger.info("\n\nRESULTADOS------>"+resultados);
	
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO FACTURAS POR GLOSA------>>>>>>"+e);
			e.printStackTrace();
		}
		return resultados;
	}
	
	/**
	 * Metodo que inserta el encabezado de una Respuesta Glosa
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static int insertarEncabezadoRespuesta(Connection con, HashMap criterios)
	{
		PreparedStatementDecorator ps;
		logger.info("\n\nMAPA CRITERIOS INSERTANDO ENCAB RESPUSTA>>>>>>>>>>>>"+criterios);
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(insertStrEncabezadoRespuesta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	
							
			int valorseq=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_respuesta_glosa");
			ps.setInt(1, valorseq);
			ps.setString(2, criterios.get("consecutivo")+"");
			ps.setString(3, UtilidadFecha.conversionFormatoFechaABD(criterios.get("fecharesp")+""));
			ps.setString(4, criterios.get("usuarioresp")+"");
			ps.setString(5, criterios.get("conciliacion")+"");
			ps.setString(6, criterios.get("observaciones")+"");
			ps.setString(7, criterios.get("estado")+"");
			ps.setString(8, criterios.get("institucion")+"");
			ps.setString(9, criterios.get("usuariomod")+"");
			ps.setString(10, criterios.get("valorresp")+"");
			ps.setString(11, criterios.get("codglosa")+"");
			
			if(ps.executeUpdate()>0)
				return valorseq;
			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. INSERTANDO NUEVO ENCABEZADO RESPUESTA GLOSA------>>>>>>"+e);
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Metodo encargado de actualizar los datos basicos del encabezado de una respuesta
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static boolean actualizarEncabezadoRespuesta(Connection con, HashMap criterios)
	{
		PreparedStatementDecorator ps;
		logger.info("\n\nMAPA CRITERIOS ACTUALIZANDO ENCAB RESPUSTA>>>>>>>>>>>>"+criterios);
		try
		{			
			ps= new PreparedStatementDecorator(con.prepareStatement(updateStrEncabezadoRespuesta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	
						
			ps.setString(1, criterios.get("conciliacion")+"");
			ps.setString(2, criterios.get("observaciones")+"");
			ps.setString(3, criterios.get("usuariomod")+"");
			ps.setDouble(4, Utilidades.convertirADouble(criterios.get("valorresp")+""));
			ps.setString(5, criterios.get("codresp")+"");
			
			ps.executeUpdate();
			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. ACTUALIZANDO ENCABEZADO RESPUESTA GLOSA------>>>>>>"+e);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Metodo para insertar cada detalle factura de la respuesta Glosa
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static int insertarDetalleRespuesta(Connection con, HashMap criterios)
	{
		PreparedStatementDecorator ps;
		logger.info("\n\nMAPA CRITERIOS INSERTANDO DETALLE RESPUSTA>>>>>>>>>>>>"+criterios);
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(insertStrDetalleRespuesta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	
							
			int valorseq=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_fact_respuesta_glosa");
			ps.setInt(1, valorseq);
			ps.setString(2, criterios.get("codresp")+"");
			ps.setString(3, criterios.get("codfactura")+"");
			ps.setString(4, criterios.get("codaudi")+"");
			ps.setString(5, criterios.get("valorfact")+"");
			ps.setString(6, criterios.get("sistema")+"");
			
			if(ps.executeUpdate()>0)
				return valorseq;
			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. INSERTANDO DETALLE RESPUESTA GLOSA------>>>>>>"+e);
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * query para actualizar los valores credito y debito de ajustes
	 */
	private static final String actualizarValoresAjustesEnFacturasStr="UPDATE facturas SET ";

	public static boolean actualizarValoresFacturas(Connection con,int numFactura, double valorAjuste, int institucion, int tipoAjuste) {
		String queryUpdate=actualizarValoresAjustesEnFacturasStr;
	    if(tipoAjuste==ConstantesBD.codigoAjusteCreditoCuentaCobro || tipoAjuste==ConstantesBD.codigoAjusteCreditoFactura)
	        queryUpdate+="ajustes_credito=ajustes_credito+"+valorAjuste+" WHERE codigo="+numFactura+" AND institucion="+institucion;
	    if(tipoAjuste==ConstantesBD.codigoAjusteDebitoCuentaCobro || tipoAjuste==ConstantesBD.codigoAjusteDebitoFactura)
	        queryUpdate+="ajustes_debito=ajustes_debito+"+valorAjuste+" WHERE codigo="+numFactura+" AND institucion="+institucion;
	    int r=0;
	    try
	    {
	      PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(queryUpdate,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	      r = ps.executeUpdate();
	      if(r<=0)
	          return false;
	      else
	          return true;
		}
	    catch(SQLException e)
	    {
			logger.error(e+"Error actualizarValoresFacturas"+e.toString());
			return false;
		}	    
	}
}