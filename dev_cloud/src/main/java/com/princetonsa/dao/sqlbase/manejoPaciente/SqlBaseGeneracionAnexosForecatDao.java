/*
 * anril 30, 2007
 */
package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import java.sql.Date;
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

/**
 * @author Sebastián Gómez 
 *
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la funcionalidad Generación Anexos Forecat
 */
public class SqlBaseGeneracionAnexosForecatDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseGeneracionAnexosForecatDao.class);
	
	/**
	 * Cadena para la consulta del archivo AA
	 */
	private static final String consultaAA_Str = "SELECT "+ 
		"f.consecutivo_factura AS consecutivo, "+ 
		"f.cuenta AS cuenta, "+ 
		"administracion.getnombremedico(f.cod_paciente) AS paciente, "+ 
		"getnombreviaingreso(f.via_ingreso) AS via_ingreso, "+ 
		"c.tipo_evento AS tipo_evento, "+
		"c.desplazado AS desplazado, "+
		"to_char(c.fecha_apertura,'"+ConstantesBD.formatoFechaAp+"') AS fecha_apertura, "+
		"p.tipo_identificacion AS tipo_identificacion, "+
		"p.numero_identificacion AS numero_identificacion, "+
		"p.primer_apellido AS primer_apellido, "+
		"CASE WHEN p.segundo_apellido IS NULL THEN '' ELSE p.segundo_apellido END AS segundo_apellido, "+
		"p.primer_nombre AS primer_nombre, "+
		"CASE WHEN p.segundo_nombre IS NULL THEN '' ELSE p.segundo_nombre END AS segundo_nombre, "+
		"to_char(p.fecha_nacimiento,'"+ConstantesBD.formatoFechaAp+"') AS fecha_nacimiento, "+
		"CASE WHEN p.sexo IS NULL THEN '' ELSE p.sexo || '' END AS sexo, "+
		"p.direccion AS direccion_residencia, "+
		"p.codigo_departamento_vivienda AS codigo_depto_residencia, "+
		"p.codigo_ciudad_vivienda AS codigo_muni_residencia, "+
		"CASE WHEN p.telefono IS NULL THEN '' ELSE p.telefono END AS telefono_residencia, "+
		"getNaturalezaEvento(c.tipo_evento,rec.naturaleza_evento) AS naturaleza_evento, "+
		"CASE WHEN c.tipo_evento = '"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"' THEN "+ 
			"CASE WHEN rat.lugar_accidente IS NULL THEN '' ELSE rat.lugar_accidente END "+ 
		"ELSE "+
			"CASE WHEN c.tipo_evento = '"+ConstantesIntegridadDominio.acronimoEventoCatastrofico+"' THEN "+ 
				"CASE WHEN rec.direccion_evento IS NULL THEN '' ELSE rec.direccion_evento END "+	
			"ELSE "+
				"'' "+
			"END "+
		"END AS direccion_evento, "+
		"CASE WHEN c.tipo_evento = '"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"' THEN "+ 
			"CASE WHEN rat.fecha_accidente IS NULL THEN '' ELSE to_char(rat.fecha_accidente,'"+ConstantesBD.formatoFechaAp+"') END "+ 
		"ELSE "+
			"CASE WHEN c.tipo_evento = '"+ConstantesIntegridadDominio.acronimoEventoCatastrofico+"' THEN "+ 
				"CASE WHEN rec.fecha_evento IS NULL THEN '' ELSE to_char(rec.fecha_evento,'"+ConstantesBD.formatoFechaAp+"') END "+	
			"ELSE "+
				"'' "+
			"END "+
		"END AS fecha_evento, "+
		"CASE WHEN c.tipo_evento = '"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"' THEN "+ 
			"CASE WHEN rat.hora_accidente IS NULL THEN '' ELSE rat.hora_accidente END "+ 
		"ELSE "+
			"'' "+
		"END AS hora_evento, "+
		"CASE WHEN c.tipo_evento = '"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"' THEN "+ 
			"CASE WHEN rat.departamento_accidente IS NULL THEN '' ELSE rat.departamento_accidente END "+ 
		"ELSE "+
			"CASE WHEN c.tipo_evento = '"+ConstantesIntegridadDominio.acronimoEventoCatastrofico+"' THEN "+ 
				"CASE WHEN rec.departamento_evento IS NULL THEN '' ELSE rec.departamento_evento END "+	
			"ELSE "+
				"'' "+ 
			"END "+
		"END AS codigo_depto_evento, "+
		"CASE WHEN c.tipo_evento = '"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"' THEN "+ 
			"CASE WHEN rat.ciudad_accidente IS NULL THEN '' ELSE rat.ciudad_accidente END "+ 
		"ELSE "+
			"CASE WHEN c.tipo_evento = '"+ConstantesIntegridadDominio.acronimoEventoCatastrofico+"' THEN "+ 
				"CASE WHEN rec.ciudad_evento IS NULL THEN '' ELSE rec.ciudad_evento END "+	
			"ELSE "+
				"'' "+
			"END "+
		"END AS codigo_muni_evento, "+
		"CASE WHEN c.tipo_evento = '"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"' THEN "+ 
			"CASE WHEN rat.zona_accidente IS NULL THEN '' ELSE rat.zona_accidente END "+ 
		"ELSE "+
			"CASE WHEN c.tipo_evento = '"+ConstantesIntegridadDominio.acronimoEventoCatastrofico+"' THEN "+ 
				"CASE WHEN rec.zona_urbana_evento IS NULL THEN '' ELSE rec.zona_urbana_evento END "+	
			"ELSE "+
				"'' "+
			"END "+
		"END AS zona, "+
		"CASE WHEN c.tipo_evento = '"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"' THEN "+ 
			"CASE WHEN rat.informacion_accidente IS NULL THEN '' ELSE rat.informacion_accidente END "+ 
		"ELSE "+
			"'' "+
		"END AS informe_evento "+ 
		"FROM facturas f "+ 
		"INNER JOIN cuentas c ON(c.id=f.cuenta) "+ 
		"INNER JOIN personas p ON (p.codigo=c.codigo_paciente) "+ 
		"LEFT OUTER JOIN view_registro_accid_transito rat ON(rat.ingreso=c.id_ingreso) "+
		"LEFT OUTER JOIN view_reg_evento_catastrofico rec ON(rec.ingreso=c.id_ingreso) ";
		
		/*"WHERE " +
		"f.convenio = ? AND "+ 
		"f.valor_total <= ? AND "+ 
		"(f.fecha BETWEEN ? AND ?) AND " +
		"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" AND "+
		"((c.tipo_evento='"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"' AND rat.estado='"+ConstantesIntegridadDominio.acronimoEstadoProcesado+"') OR " +
			"(c.tipo_evento='"+ConstantesIntegridadDominio.acronimoEventoCatastrofico+"' AND rec.estado='"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"') OR " +
			"c.desplazado = '"+ConstantesBD.acronimoSi+"') "+ 
		"order by f.consecutivo_factura"; 
		*/
	/**
	 * Cadena WHERE para la consulta del archivo AA
	 */
	private static final String consultaAA_WHERE_Str =" WHERE " +
				"f.convenio = ? AND "+ 
				"f.valor_total <= ? AND "+ 
				"(f.fecha BETWEEN ? AND ?) AND " +
				"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" AND "+
				"((c.tipo_evento='"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"' AND rat.estado='"+ConstantesIntegridadDominio.acronimoEstadoProcesado+"') OR " +
					"(c.tipo_evento='"+ConstantesIntegridadDominio.acronimoEventoCatastrofico+"' AND rec.estado='"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"') OR " +
					"c.desplazado = '"+ConstantesBD.acronimoSi+"') "+ 
				"order by f.consecutivo_factura";
	
	
	/**
	 * Cadena WHERE para la consulta del archivo AA para la consulta RIPS por cuenta Cobro
	 */
	private static final String consultaAACuentaCobro_WHERE_Str = "WHERE " +
									"f.numero_cuenta_cobro = ? AND "+ 
									"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" AND "+
									"((c.tipo_evento='"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"' AND rat.estado='"+ConstantesIntegridadDominio.acronimoEstadoProcesado+"') OR " +
										"(c.tipo_evento='"+ConstantesIntegridadDominio.acronimoEventoCatastrofico+"' AND rec.estado='"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"') OR " +
										"c.desplazado = '"+ConstantesBD.acronimoSi+"') "+ 
									"order by f.consecutivo_factura";
	
	
	/**
	 * Cadena WHERE para la consulta del archivo AA para la interfaz de ax_rips
	 */
	private static final String consultaAAAxRips_WHERE_Str ="WHERE " +
				"f.consecutivo_factura = ? AND "+ 
				"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" AND "+
				"((c.tipo_evento='"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"' AND rat.estado='"+ConstantesIntegridadDominio.acronimoEstadoProcesado+"') OR " +
					"(c.tipo_evento='"+ConstantesIntegridadDominio.acronimoEventoCatastrofico+"' AND rec.estado='"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"') OR " +
					"c.desplazado = '"+ConstantesBD.acronimoSi+"') "+ 
				"order by f.consecutivo_factura";
	
	
	/**
	 * Cadena para la consulta del archivo VH
	 */
	private static final String consultaVH_Str = "SELECT "+ 
		"f.consecutivo_factura AS consecutivo, "+ 
		"f.cuenta AS cuenta, "+ 
		"administracion.getnombremedico(f.cod_paciente) AS paciente, "+ 
		"getnombreviaingreso(f.via_ingreso) AS via_ingreso, "+ 
		"c.tipo_evento AS tipo_evento, "+
		"c.desplazado AS desplazado, "+
		"p.tipo_identificacion AS tipo_identificacion, "+
		"p.numero_identificacion AS numero_identificacion, "+
		"CASE WHEN rat.asegurado = '"+ConstantesBD.acronimoSi+"' AND rat.poliza = '"+ConstantesIntegridadDominio.acronimoPolizaVigente+"' THEN "+ 
			"'1' "+
		"ELSE "+
			"CASE WHEN rat.asegurado = '"+ConstantesBD.acronimoSi+"'  AND rat.poliza = '"+ConstantesIntegridadDominio.acronimoPolizaFalsa+"' THEN "+ 
				"'4' "+
			"ELSE "+
				"CASE WHEN rat.asegurado = '"+ConstantesBD.acronimoSi+"' AND rat.poliza = '"+ConstantesIntegridadDominio.acronimoPolizaVencida+"' THEN "+ 
					"'5' "+
				"ELSE "+
					"CASE WHEN rat.asegurado = '"+ConstantesBD.acronimoNo+"' THEN "+ 
						"'2' "+
					"ELSE "+
						"CASE WHEN rat.asegurado = '"+ConstantesIntegridadDominio.acronimoAseguradoFantasma+"' THEN '3' ELSE '' END "+
					"END "+
				"END "+
			"END "+
		"END AS estado_aseguramiento, "+
		"rat.marca_vehiculo AS marca, "+
		"rat.placa AS placa, "+
		"rat.otro_tipo_serv_v AS clase, "+ //CAMBIO rar.tipo por rar.otro_tipo_serv_v XPLANNER 58843
		"CASE WHEN rat.aseguradora IS NULL THEN '' ELSE getnombreconvenio(rat.aseguradora) END AS nombre_aseguradora, "+
		"rat.numero_poliza AS poliza_soat, "+
		"CASE WHEN rat.fecha_inicial_poliza IS NULL THEN '' ELSE to_char(rat.fecha_inicial_poliza,'"+ConstantesBD.formatoFechaAp+"') END AS fecha_inicio_vigencia, "+
		"CASE WHEN rat.fecha_final_poliza IS NULL THEN '' ELSE to_char(rat.fecha_final_poliza,'"+ConstantesBD.formatoFechaAp+"') END AS fecha_fin_vigencia, "+
		"rat.primer_apellido_prop AS primer_apellido_prop, "+
		"rat.segundo_apellido_prop AS segundo_apellido_prop, "+
		"rat.primer_nombre_prop AS primer_nombre_prop, "+
		"rat.segundo_nombre_prop AS segundo_nombre_prop, "+
		"CASE WHEN rat.tipo_id_prop IS NULL THEN '' ELSE rat.tipo_id_prop END AS tipo_identificacion_prop, "+
		"rat.numero_id_prop AS numero_identificacion_prop, "+
		"CASE WHEN rat.departamento_prop IS NULL THEN '' ELSE rat.departamento_prop END AS codigo_depto_prop, "+
		"CASE WHEN rat.ciudad_prop IS NULL THEN '' ELSE rat.ciudad_prop END AS codigo_muni_prop, "+
		"rat.direccion_prop AS direccion_prop, "+
		"rat.telefono_prop AS telefono_prop, "+
		"rat.primer_apellido_conductor As primer_apellido_cond, "+
		"rat.segundo_apellido_conductor AS segundo_apellido_cond, "+
		"rat.primer_nombre_conductor AS primer_nombre_cond, "+
		"rat.segundo_nombre_conductor AS segundo_nombre_cond, "+
		"CASE WHEN rat.tipo_id_conductor IS NULL THEN '' ELSE rat.tipo_id_conductor END AS tipo_identificacion_cond, "+
		"rat.numero_id_conductor AS numero_identificacion_cond, "+
		"CASE WHEN rat.departamento_conductor IS NULL THEN '' ELSE rat.departamento_conductor END AS codigo_depto_cond, "+
		"CASE WHEN rat.ciudad_conductor IS NULL THEN '' ELSE rat.ciudad_conductor END AS codigo_muni_cond, "+
		"rat.direccion_conductor AS direccion_cond, "+
		"rat.telefono_conductor AS telefono_cond "+ 
		"FROM facturas f "+  
		"INNER JOIN cuentas c ON(c.id=f.cuenta) "+ 
		"INNER JOIN personas p ON (p.codigo=c.codigo_paciente) "+ 
		"INNER JOIN view_registro_accid_transito rat ON(rat.ingreso=c.id_ingreso) ";
		/*+ 
		"WHERE " +
		"f.convenio = ? AND "+ 
		"f.valor_total <= ? AND "+ 
		"(f.fecha BETWEEN ? AND ?) AND "+
		"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" AND "+
		"c.tipo_evento='"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"' AND rat.estado='"+ConstantesIntegridadDominio.acronimoEstadoProcesado+"' "+ 
		"order by f.consecutivo_factura";*/
	
	/**
	 * Cadena WHERE para la consulta del archivo VH
	 */
	private static final String consultaVH_WHERE_Str =  " WHERE " +
													"f.convenio = ? AND "+ 
													"f.valor_total <= ? AND "+ 
													"(f.fecha BETWEEN ? AND ?) AND "+
													"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" AND "+
													"c.tipo_evento='"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"' AND rat.estado='"+ConstantesIntegridadDominio.acronimoEstadoProcesado+"' "+ 
													"order by f.consecutivo_factura";
	
	
	/**
	 * Cadena WHERE para la consulta del archivo VH para la interfaz ax_rips
	 */
	private static final String consultaVHAxRips_WHERE_Str =  " WHERE " +
												"f.consecutivo_factura = ? AND "+ 
												"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" AND "+
												"c.tipo_evento='"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"' AND rat.estado='"+ConstantesIntegridadDominio.acronimoEstadoProcesado+"' "+ 
												"order by f.consecutivo_factura";

	
	/**
	 * Cadena WHERE para la consulta del archivo VH para la consulta RIPS por cuenta Cobro
	 */
	private static final String consultaVHCuentaCobro_WHERE_Str =  " WHERE " +
												"f.numero_cuenta_cobro = ? AND "+ 
												"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" AND "+
												"c.tipo_evento='"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"' AND rat.estado='"+ConstantesIntegridadDominio.acronimoEstadoProcesado+"' "+ 
												"order by f.consecutivo_factura";
	
	/**
	 * Cadena para la consulta de archivo AV sobre Servicios de Consulta
	 */
	private static final String consultaAV_Consultas_Str = "SELECT "+ 
		"f.consecutivo_factura AS consecutivo, "+ 
		"to_char(f.fecha,'"+ConstantesBD.formatoFechaAp+"') AS fecha_factura, "+
		"f.valor_total AS valor_total_factura, "+
		"f.cuenta AS cuenta, "+ 
		"administracion.getnombremedico(f.cod_paciente) AS paciente, "+ 
		"getnombreviaingreso(f.via_ingreso) AS via_ingreso, "+ 
		"c.tipo_evento AS tipo_evento, "+
		"c.desplazado AS desplazado, "+
		"p.tipo_identificacion AS tipo_identificacion, "+
		"p.numero_identificacion AS numero_identificacion, "+
		"dfs.tipo_cargo AS tipo_cargo, "+
		"sol.consecutivo_ordenes_medicas AS orden, "+
		"s.tipo_servicio AS tipo_serv, "+
		"s.naturaleza_servicio AS naturaleza, " +
		"'' AS es_medicamento, "+ //no aplica
		"'' AS es_pos, "+ //no aplica
		"dfs.servicio AS serv_art, "+
		"to_char(sol.fecha_solicitud,'"+ConstantesBD.formatoFechaAp+"') AS fecha_atencion, "+
		"CASE WHEN getcodigopropservicio2(dfs.servicio,?) IS NULL THEN '' ELSE getcodigopropservicio2(dfs.servicio,?) END AS codigo_consulta, "+
		"getAcronimoConsulta(dfs.solicitud,sol.tipo,"+ValoresPorDefecto.getValorTrueParaConsultas()+",0) AS diag_ppal_ingreso, "+
		"CASE WHEN f.via_ingreso = "+ConstantesBD.codigoViaIngresoAmbulatorios+" OR f.via_ingreso = "+ConstantesBD.codigoViaIngresoConsultaExterna+" THEN "+ 
			"getAcronimoConsulta(dfs.solicitud,sol.tipo,"+ValoresPorDefecto.getValorTrueParaConsultas()+",0) "+
		"ELSE "+
			"CASE WHEN f.via_ingreso = "+ConstantesBD.codigoViaIngresoUrgencias+" OR f.via_ingreso = "+ConstantesBD.codigoViaIngresoHospitalizacion+" THEN "+ 
				"getAcronimoEgreso(f.cuenta) "+ 
			"ELSE "+
				"'' "+ 
			"END "+ 
		"END AS diag_ppal_egreso, "+
		"dfs.valor_total || '' AS valor_consulta, "+
		"'' AS codigo_procedimiento, "+
		"'' AS cantidad_procedimiento, "+
		"'' AS valor_procedimiento, "+
		"'' AS tipo_medicamento, "+
		"'' AS codigo_med_pos, "+
		"'' AS nombre_med_pos, "+
		"'' As forma_farmaceutica, "+
		"'' AS concentracion_med_pos, "+
		"'' AS unidad_medida_pos, "+
		"'' AS cantidad_med, "+
		"'' AS valor_total_med, "+
		"'' AS tipo_servicio, "+
		"'' AS cantidad, "+
		"'' AS valor_total_servicio "+ 
		"FROM facturas f "+ 
		"INNER JOIN cuentas c ON(c.id=f.cuenta) "+ 
		"INNER JOIN personas p ON (p.codigo=c.codigo_paciente) "+ 
		"INNER JOIN det_factura_solicitud dfs ON(dfs.factura=f.codigo) "+ 
		"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dfs.solicitud) "+ 
		"INNER JOIN servicios s ON(s.codigo=dfs.servicio AND s.tipo_servicio = '"+ConstantesBD.codigoServicioInterconsulta+"') "+ 
		"LEFT OUTER JOIN view_registro_accid_transito rat ON(rat.ingreso=c.id_ingreso) "+
		"LEFT OUTER JOIN view_reg_evento_catastrofico rec ON(rec.ingreso=c.id_ingreso) ";
		/*+ 
		"WHERE " +
		"f.convenio = ? AND "+ 
		"f.valor_total <= ? AND "+ 
		"(f.fecha BETWEEN ? AND ?) AND "+
		"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" AND "+
		"((c.tipo_evento='"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"' AND rat.estado='"+ConstantesIntegridadDominio.acronimoEstadoProcesado+"') OR " +
			"(c.tipo_evento='"+ConstantesIntegridadDominio.acronimoEventoCatastrofico+"' AND rec.estado='"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"') OR " +
			"c.desplazado = '"+ConstantesBD.acronimoSi+"') "+ 
		"order by f.consecutivo_factura"; 
		*/
	
	/**
	 * Cadena WHERE para la consulta de archivo AV sobre Servicios de Consulta
	 */
	private static final String consultaAV_Consultas_WHERE_Str =	" WHERE " +
									"f.convenio = ? AND "+ 
									"f.valor_total <= ? AND "+ 
									"(f.fecha BETWEEN ? AND ?) AND "+
									"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" AND "+
									"((c.tipo_evento='"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"' AND rat.estado='"+ConstantesIntegridadDominio.acronimoEstadoProcesado+"') OR " +
										"(c.tipo_evento='"+ConstantesIntegridadDominio.acronimoEventoCatastrofico+"' AND rec.estado='"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"') OR " +
										"c.desplazado = '"+ConstantesBD.acronimoSi+"') "+ 
									"order by f.consecutivo_factura"; 
	
	/**
	 * Cadena WHERE para la consulta de archivo AV sobre Servicios de Consulta para la interfaz ax_rips
	 */
	private static final String consultaAV_ConsultasAxRips_WHERE_Str =	" WHERE " +
									"f.consecutivo_factura = ? AND "+									
									"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" AND "+
									"((c.tipo_evento='"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"' AND rat.estado='"+ConstantesIntegridadDominio.acronimoEstadoProcesado+"') OR " +
										"(c.tipo_evento='"+ConstantesIntegridadDominio.acronimoEventoCatastrofico+"' AND rec.estado='"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"') OR " +
										"c.desplazado = '"+ConstantesBD.acronimoSi+"') "+ 
									"order by f.consecutivo_factura"; 
	
	
	/**
	 * Cadena WHERE para la consulta de archivo AV sobre Servicios de Consulta para cuentas cobro
	 */
	private static final String consultaAVCuentaCobro_Consultas_WHERE_Str =	" WHERE " +
									"f.numero_cuenta_cobro = ? AND "+									
									"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" AND "+
									"((c.tipo_evento='"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"' AND rat.estado='"+ConstantesIntegridadDominio.acronimoEstadoProcesado+"') OR " +
										"(c.tipo_evento='"+ConstantesIntegridadDominio.acronimoEventoCatastrofico+"' AND rec.estado='"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"') OR " +
										"c.desplazado = '"+ConstantesBD.acronimoSi+"') "+ 
									"order by f.consecutivo_factura"; 
	
	
	/**
	 * Cadena para la consulta de archivo AV sobre Servicios de Procedimientos
	 */
	private static final String consultaAV_Procedimientos_Str = "SELECT "+ 
		"f.consecutivo_factura AS consecutivo, "+ 
		"to_char(f.fecha,'"+ConstantesBD.formatoFechaAp+"') AS fecha_factura, "+
		"f.valor_total AS valor_total_factura, "+
		"f.cuenta AS cuenta, "+ 
		"administracion.getnombremedico(f.cod_paciente) AS paciente, "+ 
		"getnombreviaingreso(f.via_ingreso) AS via_ingreso, "+ 
		"c.tipo_evento AS tipo_evento, "+
		"c.desplazado AS desplazado, "+
		"p.tipo_identificacion AS tipo_identificacion, "+
		"p.numero_identificacion AS numero_identificacion, "+
		"dfs.tipo_cargo AS tipo_cargo, "+
		"sol.consecutivo_ordenes_medicas AS orden, "+
		"s.tipo_servicio AS tipo_serv, "+
		"s.naturaleza_servicio AS naturaleza, "+
		"'' AS es_medicamento, "+
		"'' AS es_pos, "+ //no aplica
		"dfs.servicio AS serv_art, "+
		"to_char(sol.fecha_solicitud,'"+ConstantesBD.formatoFechaAp+"') AS fecha_atencion, "+
		"'' AS codigo_consulta, "+
		"'' AS diag_ppal_ingreso, "+
		"'' AS diag_ppal_egreso, "+
		"'' AS valor_consulta, "+
		"CASE WHEN getcodigopropservicio2(dfs.servicio,?) IS NULL THEN '' ELSE getcodigopropservicio2(dfs.servicio,?) END AS codigo_procedimiento, "+
		"dfs.cantidad_cargo || '' AS cantidad_procedimiento, "+
		"dfs.valor_total || '' AS valor_procedimiento, "+
		"'' AS tipo_medicamento, "+
		"'' AS codigo_med_pos, "+
		"'' AS nombre_med_pos, "+
		"'' As forma_farmaceutica, "+
		"'' AS concentracion_med_pos, "+
		"'' AS unidad_medida_pos, "+
		"'' AS cantidad_med, "+
		"'' AS valor_total_med, "+
		"'' AS tipo_servicio, "+
		"'' AS cantidad, "+
		"'' AS valor_total_servicio  "+ 
		"FROM facturas f "+ 
		"INNER JOIN cuentas c ON(c.id=f.cuenta) "+ 
		"INNER JOIN personas p ON (p.codigo=c.codigo_paciente) "+ 
		"INNER JOIN det_factura_solicitud dfs ON(dfs.factura=f.codigo) "+ 
		"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dfs.solicitud) "+ 
		"INNER JOIN servicios s ON(s.codigo=dfs.servicio AND s.tipo_servicio IN " +
			"('"+ConstantesBD.codigoServicioProcedimiento+"','"+ConstantesBD.codigoServicioQuirurgico+"'," +
			"'"+ConstantesBD.codigoServicioPartosCesarea+"','"+ConstantesBD.codigoServicioPaquetes+"','"+ConstantesBD.codigoServicioNoCruentos+"')) "+ 
		"LEFT OUTER JOIN view_registro_accid_transito rat ON(rat.ingreso=c.id_ingreso) "+
		"LEFT OUTER JOIN view_reg_evento_catastrofico rec ON(rec.ingreso=c.id_ingreso) ";
	/*+ 
		"WHERE " +
		"f.convenio = ? AND "+ 
		"f.valor_total <= ? AND "+ 
		"(f.fecha BETWEEN ? AND ?) AND "+
		"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" AND "+
		"((c.tipo_evento='"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"' AND rat.estado='"+ConstantesIntegridadDominio.acronimoEstadoProcesado+"') OR " +
		"(c.tipo_evento='"+ConstantesIntegridadDominio.acronimoEventoCatastrofico+"' AND rec.estado='"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"') OR " +
		"c.desplazado = '"+ConstantesBD.acronimoSi+"') "+ 
		"order by f.consecutivo_factura";
	*/
	
	/**
	 * Cadena WHERE para la consulta de archivo AV sobre Servicios de Procedimientos
	 */
	private static final String consultaAV_Procedimientos_WHERE_Str = " WHERE " +
																"f.convenio = ? AND "+ 
																"f.valor_total <= ? AND "+ 
																"(f.fecha BETWEEN ? AND ?) AND "+
																"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" AND "+
																"((c.tipo_evento='"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"' AND rat.estado='"+ConstantesIntegridadDominio.acronimoEstadoProcesado+"') OR " +
																"(c.tipo_evento='"+ConstantesIntegridadDominio.acronimoEventoCatastrofico+"' AND rec.estado='"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"') OR " +
																"c.desplazado = '"+ConstantesBD.acronimoSi+"') "+ 
																"order by f.consecutivo_factura";
	
	/**
	 * Cadena WHERE para la consulta de archivo AV sobre Servicios de Procedimientos para la interfaz de ax_rips
	 */
	private static final String consultaAV_ProcedimientosAxRips_WHERE_Str = " WHERE " +
																"f.consecutivo_factura = ? AND "+ 
																"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" AND "+
																"((c.tipo_evento='"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"' AND rat.estado='"+ConstantesIntegridadDominio.acronimoEstadoProcesado+"') OR " +
																"(c.tipo_evento='"+ConstantesIntegridadDominio.acronimoEventoCatastrofico+"' AND rec.estado='"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"') OR " +
																"c.desplazado = '"+ConstantesBD.acronimoSi+"') "+ 
																"order by f.consecutivo_factura";
	
	
	/**
	 * Cadena WHERE para la consulta de archivo AV sobre Servicios de Procedimientos para cuentas cobro
	 */
	private static final String consultaAVCuentaCobro_Procedimientos_WHERE_Str = " WHERE " +
																"f.numero_cuenta_cobro = ? AND "+ 
																"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" AND "+
																"((c.tipo_evento='"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"' AND rat.estado='"+ConstantesIntegridadDominio.acronimoEstadoProcesado+"') OR " +
																"(c.tipo_evento='"+ConstantesIntegridadDominio.acronimoEventoCatastrofico+"' AND rec.estado='"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"') OR " +
																"c.desplazado = '"+ConstantesBD.acronimoSi+"') "+ 
																"order by f.consecutivo_factura";
	
	/**
	 * Cadena para la consulta de archivo AV sobre Articulos de Medicamentos
	 */
	private static final String consultaAV_Medicamentos_Str = "SELECT "+
		"f.consecutivo_factura AS consecutivo, "+ 
		"to_char(f.fecha,'"+ConstantesBD.formatoFechaAp+"') AS fecha_factura, "+
		"f.valor_total AS valor_total_factura, "+
		"f.cuenta AS cuenta, "+ 
		"administracion.getnombremedico(f.cod_paciente) AS paciente, "+ 
		"getnombreviaingreso(f.via_ingreso) AS via_ingreso, "+ 
		"c.tipo_evento AS tipo_evento, "+
		"c.desplazado AS desplazado, "+
		"p.tipo_identificacion AS tipo_identificacion, "+
		"p.numero_identificacion AS numero_identificacion, "+
		"dfs.tipo_cargo AS tipo_cargo, "+
		"sol.consecutivo_ordenes_medicas AS orden, "+
		"'' AS tipo_serv, "+
		"coalesce(na.codigo_rips,'') AS naturaleza, "+ 
		"na.es_medicamento AS es_medicamento, "+
		"na.es_pos AS es_pos, "+ 
		"dfs.articulo AS serv_art, "+
		"to_char(sol.fecha_solicitud,'"+ConstantesBD.formatoFechaAp+"') AS fecha_atencion, "+
		"'' AS codigo_consulta, "+
		"'' AS diag_ppal_ingreso, "+
		"'' AS diag_ppal_egreso, "+
		"'' AS valor_consulta, "+
		"'' AS codigo_procedimiento, "+
		"'' AS cantidad_procedimiento, "+
		"'' AS valor_procedimiento, "+
		"CASE WHEN na.es_pos = '"+ConstantesBD.acronimoTrueCorto+"' THEN "+ 
			"'1' "+  
		"ELSE "+ 
			"CASE WHEN na.es_pos = '"+ConstantesBD.acronimoFalseCorto+"' OR na.es_pos IS NULL  THEN "+ 
				"'2' "+ 
			"ELSE "+ 
				"'' "+
			"END "+
		"END AS tipo_medicamento, "+
		"CASE WHEN na.es_pos = '"+ConstantesBD.acronimoTrueCorto+"' OR na.es_pos IS NULL THEN a.codigo || '' ELSE '' END AS codigo_med_pos, "+
		"CASE WHEN na.es_pos = '"+ConstantesBD.acronimoFalseCorto+"' OR na.es_pos IS NULL THEN a.descripcion ELSE '' END AS nombre_med_pos, "+
		"CASE WHEN na.es_pos = '"+ConstantesBD.acronimoFalseCorto+"' OR na.es_pos IS NULL THEN getNomFormaFarmaceutica(a.forma_farmaceutica) ELSE '' END As forma_farmaceutica, "+
		"CASE WHEN na.es_pos = '"+ConstantesBD.acronimoFalseCorto+"' OR na.es_pos IS NULL THEN a.concentracion ELSE '' END AS concentracion_med_pos, "+
		"CASE WHEN na.es_pos = '"+ConstantesBD.acronimoFalseCorto+"' OR na.es_pos IS NULL THEN getnomunidadmedida(a.unidad_medida) ELSE '' END AS unidad_medida_pos, "+
		"dfs.cantidad_cargo || '' AS cantidad_med, "+
		"dfs.valor_total || '' AS valor_total_med, "+
		"'' AS tipo_servicio, "+
		"'' AS cantidad, "+
		"'' AS valor_total_servicio "+ 
		"FROM facturas f "+ 
		"INNER JOIN cuentas c ON(c.id=f.cuenta) "+ 
		"INNER JOIN personas p ON (p.codigo=c.codigo_paciente) "+ 
		"INNER JOIN det_factura_solicitud dfs ON(dfs.factura=f.codigo) "+ 
		"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dfs.solicitud) "+ 
		"INNER JOIN articulo a ON(a.codigo=dfs.articulo) " +
		"INNER JOIN naturaleza_articulo na ON(na.acronimo=a.naturaleza AND na.institucion=a.institucion AND na.es_medicamento = '"+ConstantesBD.acronimoSi+"') "+ 
		"LEFT OUTER JOIN view_registro_accid_transito rat ON(rat.ingreso=c.id_ingreso) "+
		"LEFT OUTER JOIN view_reg_evento_catastrofico rec ON(rec.ingreso=c.id_ingreso) ";
	/*+ 
		"WHERE " +
		"f.convenio = ? AND "+ 
		"f.valor_total <= ? AND "+ 
		"(f.fecha BETWEEN ? AND ?) AND "+
		"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" AND "+
		"((c.tipo_evento='"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"' AND rat.estado='"+ConstantesIntegridadDominio.acronimoEstadoProcesado+"') OR " +
		"(c.tipo_evento='"+ConstantesIntegridadDominio.acronimoEventoCatastrofico+"' AND rec.estado='"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"') OR " +
		"c.desplazado = '"+ConstantesBD.acronimoSi+"') "+ 
		"order by f.consecutivo_factura";
	*/
	
	/**
	 * Cadena WHERE para la consulta de archivo AV sobre Articulos de Medicamentos
	 */	
	private static final String consultaAV_Medicamentos_WHERE_Str = " WHERE " +
									"f.convenio = ? AND "+ 
									"f.valor_total <= ? AND "+ 
									"(f.fecha BETWEEN ? AND ?) AND "+
									"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" AND "+
									"((c.tipo_evento='"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"' AND rat.estado='"+ConstantesIntegridadDominio.acronimoEstadoProcesado+"') OR " +
									"(c.tipo_evento='"+ConstantesIntegridadDominio.acronimoEventoCatastrofico+"' AND rec.estado='"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"') OR " +
									"c.desplazado = '"+ConstantesBD.acronimoSi+"') "+ 
									"order by f.consecutivo_factura";
	
	/**
	 * Cadena WHERE para la consulta de archivo AV sobre Articulos de Medicamentos para la interfaz de ax_rips
	 */	
	private static final String consultaAV_MedicamentosAxRips_WHERE_Str = " WHERE " +
									"f.consecutivo_factura = ? AND "+ 
									"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" AND "+
									"((c.tipo_evento='"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"' AND rat.estado='"+ConstantesIntegridadDominio.acronimoEstadoProcesado+"') OR " +
									"(c.tipo_evento='"+ConstantesIntegridadDominio.acronimoEventoCatastrofico+"' AND rec.estado='"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"') OR " +
									"c.desplazado = '"+ConstantesBD.acronimoSi+"') "+ 
									"order by f.consecutivo_factura";
	/**
	 * Cadena WHERE para la consulta de archivo AV sobre Articulos de Medicamentos para cuentas cobro
	 */	
	private static final String consultaAVCuentasCobro_Medicamentos_WHERE_Str = " WHERE " +
									"f.numero_cuenta_cobro = ? AND "+ 
									"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" AND "+
									"((c.tipo_evento='"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"' AND rat.estado='"+ConstantesIntegridadDominio.acronimoEstadoProcesado+"') OR " +
									"(c.tipo_evento='"+ConstantesIntegridadDominio.acronimoEventoCatastrofico+"' AND rec.estado='"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"') OR " +
									"c.desplazado = '"+ConstantesBD.acronimoSi+"') "+ 
									"order by f.consecutivo_factura";
	
	
	/**
	 * Cadena para la consulta de archivo AV para Servicios y Articulos Otros
	 */
	private static final String consultaAV_Otros_Str = "SELECT "+ 
		"f.consecutivo_factura AS consecutivo, "+ 
		"to_char(f.fecha,'DD/MM/YYYY') AS fecha_factura, "+
		"f.valor_total AS valor_total_factura, "+
		"f.cuenta AS cuenta, "+ 
		"administracion.getnombremedico(f.cod_paciente) AS paciente, "+ 
		"getnombreviaingreso(f.via_ingreso) AS via_ingreso, "+ 
		"c.tipo_evento AS tipo_evento, "+
		"c.desplazado AS desplazado, "+
		"p.tipo_identificacion AS tipo_identificacion, "+
		"p.numero_identificacion AS numero_identificacion, "+
		"dfs.tipo_cargo AS tipo_cargo, "+
		"sol.consecutivo_ordenes_medicas AS orden, "+
		"CASE WHEN dfs.tipo_cargo = "+ConstantesBD.codigoTipoCargoServicios+" THEN s.tipo_servicio ELSE '' END AS tipo_serv, "+
		"CASE WHEN dfs.tipo_cargo = "+ConstantesBD.codigoTipoCargoServicios+" THEN s.naturaleza_servicio ELSE coalesce(na.codigo_rips,'') END AS naturaleza, "+
		"CASE WHEN dfs.tipo_cargo = "+ConstantesBD.codigoTipoCargoServicios+" THEN '' ELSE na.es_medicamento END AS es_medicamento, "+
		"'' AS es_pos, "+ //no aplica
		"CASE WHEN dfs.tipo_cargo = "+ConstantesBD.codigoTipoCargoServicios+" THEN dfs.servicio ELSE dfs.articulo END AS serv_art, "+
		"to_char(sol.fecha_solicitud,'"+ConstantesBD.formatoFechaAp+"') AS fecha_atencion, "+
		"'' AS codigo_consulta, "+
		"'' AS diag_ppal_ingreso, "+
		"'' AS diag_ppal_egreso, "+
		"'' AS valor_consulta, "+
		"'' AS codigo_procedimiento, "+
		"'' AS cantidad_procedimiento, "+
		"'' AS valor_procedimiento, "+
		"'' AS tipo_medicamento, "+
		"'' AS codigo_med_pos, "+
		"'' AS nombre_med_pos, "+
		"'' As forma_farmaceutica, "+
		"'' AS concentracion_med_pos, "+
		"'' AS unidad_medida_pos, "+
		"'' AS cantidad_med, "+
		"'' AS valor_total_med, "+
		"CASE WHEN s.naturaleza_servicio = '"+ConstantesBD.codigoNaturalezaServicioTrasladoPaciente+"' THEN "+  
			"'2' "+  
		"ELSE "+
			"CASE WHEN s.naturaleza_servicio = '"+ConstantesBD.codigoNaturalezaServicioEstancias+"' THEN "+ 
				"'3' "+
			"ELSE "+
				"CASE WHEN s.naturaleza_servicio = '"+ConstantesBD.codigoNaturalezaServicioHonorarios+"' THEN "+ 
					"'4' "+
				"ELSE "+
					"CASE WHEN na.es_medicamento = '"+ConstantesBD.acronimoNo+"' THEN "+ 
						"'1' "+
					"ELSE "+
						"'' "+
					"END "+
				"END "+
			"END "+ 
		"END AS tipo_servicio, "+
		"dfs.cantidad_cargo || '' AS cantidad, "+
		"dfs.valor_total || '' AS valor_total_servicio "+ 
		"FROM facturas f "+ 
		"INNER JOIN cuentas c ON(c.id=f.cuenta) "+ 
		"INNER JOIN personas p ON (p.codigo=c.codigo_paciente) "+ 
		"INNER JOIN det_factura_solicitud dfs ON(dfs.factura=f.codigo) "+ 
		"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dfs.solicitud) "+ 
		"LEFT OUTER JOIN articulo a ON(a.codigo=dfs.articulo) "+ 
		"LEFT OUTER JOIN servicios s ON(s.codigo=dfs.servicio) "+ 
		"LEFT OUTER JOIN view_registro_accid_transito rat ON(rat.ingreso=c.id_ingreso) "+
		"LEFT OUTER JOIN view_reg_evento_catastrofico rec ON(rec.ingreso=c.id_ingreso) " +
		"LEFT OUTER JOIN naturaleza_articulo na ON(na.acronimo=a.naturaleza) ";
		/*+ 
		"WHERE " +
		"f.convenio = ? AND "+ 
		"f.valor_total <= ? AND "+ 
		"(f.fecha BETWEEN ? AND ?) AND "+
		"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" AND "+
		"((c.tipo_evento='"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"' AND rat.estado='"+ConstantesIntegridadDominio.acronimoEstadoProcesado+"') OR " +
			"(c.tipo_evento='"+ConstantesIntegridadDominio.acronimoEventoCatastrofico+"' AND rec.estado='"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"') OR " +
			"c.desplazado = '"+ConstantesBD.acronimoSi+"') AND " +
		"(na.es_medicamento = '"+ConstantesBD.acronimoNo+"'  OR " +
			"s.naturaleza_servicio in ('"+ConstantesBD.codigoNaturalezaServicioHonorarios+"','"+ConstantesBD.codigoNaturalezaServicioEstancias+"','"+ConstantesBD.codigoNaturalezaServicioTrasladoPaciente+"')) "+ 
		"order by f.consecutivo_factura";
		*/
	/**
	 * Cadena para la consulta de archivo AV para Servicios y Articulos Otros
	 */
	private static final String consultaAV_Otros_WHERE_Str = "WHERE " +
												"f.convenio = ? AND "+ 
												"f.valor_total <= ? AND "+ 
												"(f.fecha BETWEEN ? AND ?) AND "+
												"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" AND "+
												"((c.tipo_evento='"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"' AND rat.estado='"+ConstantesIntegridadDominio.acronimoEstadoProcesado+"') OR " +
													"(c.tipo_evento='"+ConstantesIntegridadDominio.acronimoEventoCatastrofico+"' AND rec.estado='"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"') OR " +
													"c.desplazado = '"+ConstantesBD.acronimoSi+"') AND " +
												"(na.es_medicamento = '"+ConstantesBD.acronimoNo+"'  OR " +
													"s.naturaleza_servicio in ('"+ConstantesBD.codigoNaturalezaServicioHonorarios+"','"+ConstantesBD.codigoNaturalezaServicioEstancias+"','"+ConstantesBD.codigoNaturalezaServicioTrasladoPaciente+"')) "+ 
												"order by f.consecutivo_factura";
	
	/**
	 * Cadena para la consulta de archivo AV para Servicios y Articulos Otros para la interfaz de ax_rips
	 */
	private static final String consultaAV_OtrosAxRips_WHERE_Str = "WHERE " +
												"f.consecutivo_factura = ? AND "+ 
												"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" AND "+
												"((c.tipo_evento='"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"' AND rat.estado='"+ConstantesIntegridadDominio.acronimoEstadoProcesado+"') OR " +
													"(c.tipo_evento='"+ConstantesIntegridadDominio.acronimoEventoCatastrofico+"' AND rec.estado='"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"') OR " +
													"c.desplazado = '"+ConstantesBD.acronimoSi+"') AND " +
												"(na.es_medicamento = '"+ConstantesBD.acronimoNo+"'  OR " +
													"s.naturaleza_servicio in ('"+ConstantesBD.codigoNaturalezaServicioHonorarios+"','"+ConstantesBD.codigoNaturalezaServicioEstancias+"','"+ConstantesBD.codigoNaturalezaServicioTrasladoPaciente+"')) "+ 
												"order by f.consecutivo_factura";
	
	
	
	/**
	 * Cadena para la consulta de archivo AV para Servicios y Articulos Otros para cuentas cobro
	 */
	private static final String consultaAVCuentasCobro_Otros_WHERE_Str = "WHERE " +
												"f.numero_cuenta_cobro = ? AND "+ 
												"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" AND "+
												"((c.tipo_evento='"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"' AND rat.estado='"+ConstantesIntegridadDominio.acronimoEstadoProcesado+"') OR " +
													"(c.tipo_evento='"+ConstantesIntegridadDominio.acronimoEventoCatastrofico+"' AND rec.estado='"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"') OR " +
													"c.desplazado = '"+ConstantesBD.acronimoSi+"') AND " +
												"(na.es_medicamento = '"+ConstantesBD.acronimoNo+"'  OR " +
													"s.naturaleza_servicio in ('"+ConstantesBD.codigoNaturalezaServicioHonorarios+"','"+ConstantesBD.codigoNaturalezaServicioEstancias+"','"+ConstantesBD.codigoNaturalezaServicioTrasladoPaciente+"')) "+ 
												"order by f.consecutivo_factura";
	
	/**
	 * Método que consulta el archivo AA : ACCIDENTES O EVENTOS CATASTRÓFICOS Y TERRORISTAS
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap consultaAA(Connection con,HashMap campos, boolean esAxRips, boolean esCuentaCobro)
	{
		try
		{
			String consulta = "";
			// verificar si es cuenta cobro
			if (!esCuentaCobro){
				if (!esAxRips)
				{
					consulta = consultaAA_Str + consultaAA_WHERE_Str;
				}
				else
				{
					consulta = consultaAA_Str + consultaAAAxRips_WHERE_Str;
				}
			}
			else
			{
				consulta = consultaAA_Str + consultaAACuentaCobro_WHERE_Str;
			}
			//PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaAA_Str,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			// verificar si es cuenta cobro
			if (!esCuentaCobro){
				if (!esAxRips)
				{
					pst.setInt(1, Utilidades.convertirAEntero(campos.get("convenio").toString()));
					pst.setDouble(2,Utilidades.convertirADouble(campos.get("salarioMinimo").toString()));
					pst.setDate(3,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString())));
					pst.setDate(4,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString())));
				}
				else
				{
					pst.setInt(1, Utilidades.convertirAEntero(campos.get("numeroFactura").toString()));
				}
			}
			else
			{
				pst.setDouble(1, Utilidades.convertirADouble(campos.get("numeroCuentaCobro").toString()));
			}
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,true);
			pst.close();
			return mapaRetorno;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en consultaAA: "+e);
			return null;
		}
	}
	
	/**
	 * Método que consulta el archivo VH: VEHÍCULOS
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap consultaVH(Connection con,HashMap campos, boolean esAxRips, boolean esCuentaCobro)
	{
		try
		{
			
			String consulta = "";
			//			 verificar si es cuenta cobro
			if (!esCuentaCobro){
				//				 verificar si es una consulta de ax_rips			
				if (!esAxRips)
				{
					consulta = consultaVH_Str + consultaVH_WHERE_Str;
				}
				else
				{
					consulta = consultaVH_Str + consultaVHAxRips_WHERE_Str;
				}
			}
			else{
				consulta = consultaVH_Str + consultaVHCuentaCobro_WHERE_Str;
			}
			
			logger.info("ENTRÉ A CONSULTA DEL VH CON CAMPOS=> "+campos);
			
			//PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaVH_Str,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			//			 verificar si es cuenta cobro
			if (!esCuentaCobro){
				if (!esAxRips)
				{
					pst.setInt(1, Utilidades.convertirAEntero(campos.get("convenio").toString()));
					pst.setDouble(2,Utilidades.convertirADouble(campos.get("salarioMinimo").toString()));
					pst.setDate(3,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString())));
					pst.setDate(4,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString())));				
				}
				else
				{
					pst.setInt(1, Utilidades.convertirAEntero(campos.get("numeroFactura").toString()));
				}
			}
			else
			{
				pst.setDouble(1, Utilidades.convertirADouble(campos.get("numeroCuentaCobro").toString()));
			}
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,true);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultaVH: "+e);
			return null;
		}
	}
	
	/**
	 * Método que consulta el archivo AV: ATENCIÓN DE LA VÍCTIMA
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap consultaAV(Connection con,HashMap campos, boolean esAxRips, boolean esCuentaCobro)
	{
		try
		{
			String consulta = "";
			String fechaInicial = "";
			String fechaFinal = "";
			double salarioMinimo = 0;
			int tipoManual = 0;
			int convenio = 0;
			int nrofactura = 0;
			double nroCuentaCobro = 0;
			
			//			 verificar si es cuenta cobro
			if (!esCuentaCobro){
				// verificar si es una consulta de ax_rips			
				if (!esAxRips)
				{
					//obtencion de campos
					fechaInicial = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString());
					fechaFinal = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString());
					salarioMinimo = Utilidades.convertirADouble(campos.get("salarioMinimo").toString());
					tipoManual = Utilidades.convertirAEntero(campos.get("tipoManual").toString());
					convenio = Utilidades.convertirAEntero(campos.get("convenio").toString());
					
					//Agrupacion de las distintas consultas
					consulta = "("+consultaAV_Consultas_Str+consultaAV_Consultas_WHERE_Str+") UNION ("
												+consultaAV_Procedimientos_Str+ consultaAV_Procedimientos_WHERE_Str +"" +
														") UNION ("+consultaAV_Medicamentos_Str+ consultaAV_Medicamentos_WHERE_Str +") " +
																"UNION ("+consultaAV_Otros_Str+consultaAV_Otros_WHERE_Str+")";
					
				}
				else
				{
					//Agrupacion de las distintas consultas
					consulta = "("+consultaAV_Consultas_Str+consultaAV_ConsultasAxRips_WHERE_Str+") UNION ("
												+consultaAV_Procedimientos_Str+ consultaAV_ProcedimientosAxRips_WHERE_Str +"" +
														") UNION ("+consultaAV_Medicamentos_Str+ consultaAV_MedicamentosAxRips_WHERE_Str +") " +
																"UNION ("+consultaAV_Otros_Str+consultaAV_OtrosAxRips_WHERE_Str+")";
				}
			}
			else{
				//Agrupacion de las distintas consultas
				consulta = "("+consultaAV_Consultas_Str+consultaAVCuentaCobro_Consultas_WHERE_Str+") UNION ("
											+consultaAV_Procedimientos_Str+ consultaAVCuentaCobro_Procedimientos_WHERE_Str +"" +
													") UNION ("+consultaAV_Medicamentos_Str+ consultaAVCuentasCobro_Medicamentos_WHERE_Str +") " +
															"UNION ("+consultaAV_Otros_Str+consultaAVCuentasCobro_Otros_WHERE_Str+")";
			}
			
			
			logger.info("ENTRÉ A CONSULTA DEL AV CON CAMPOS=> "+campos);
			
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			//			 verificar si es cuenta cobro
			if (!esCuentaCobro){
				// verificar si es una consulta de ax_rips			
				if (!esAxRips)
				{
					//Seccion de Consultas
					pst.setInt(1,tipoManual);
					pst.setInt(2,tipoManual);
					pst.setInt(3,convenio);
					pst.setDouble(4, salarioMinimo);
					pst.setDate(5,Date.valueOf(fechaInicial));
					pst.setDate(6,Date.valueOf(fechaFinal));
					//Seccion de Procedimientos
					pst.setInt(7,tipoManual);
					pst.setInt(8,tipoManual);
					pst.setInt(9,convenio);
					pst.setDouble(10, salarioMinimo);
					pst.setDate(11,Date.valueOf(fechaInicial));
					pst.setDate(12,Date.valueOf(fechaFinal));
					//Sección de medicamentos
					pst.setInt(13,convenio);
					pst.setDouble(14, salarioMinimo);
					pst.setDate(15,Date.valueOf(fechaInicial));
					pst.setDate(16,Date.valueOf(fechaFinal));
					//Seccion de Otros
					pst.setInt(17,convenio);
					pst.setDouble(18, salarioMinimo);
					pst.setDate(19,Date.valueOf(fechaInicial));
					pst.setDate(20,Date.valueOf(fechaFinal));
				}
				else
				{
					tipoManual = Utilidades.convertirAEntero(campos.get("tipoManual").toString());
					nrofactura = Utilidades.convertirAEntero(campos.get("numeroFactura").toString());
					//				Seccion de Consultas
					pst.setInt(1,tipoManual);
					pst.setInt(2,tipoManual);
					pst.setInt(3,nrofactura);
					//				Seccion de Procedimientos
					pst.setInt(4,tipoManual);
					pst.setInt(5,tipoManual);
					pst.setInt(6,nrofactura);
					//				Sección de medicamentos
					pst.setInt(7,nrofactura);
					//				Seccion de Otros
					pst.setInt(8,nrofactura);
					
				}
			}
			else{
				tipoManual = Utilidades.convertirAEntero(campos.get("tipoManual").toString());
				nroCuentaCobro = Double.parseDouble(campos.get("numeroCuentaCobro").toString());
				//				Seccion de Consultas
				pst.setInt(1,tipoManual);
				pst.setInt(2,tipoManual);
				pst.setDouble(3,nroCuentaCobro);
				//				Seccion de Procedimientos
				pst.setInt(4,tipoManual);
				pst.setInt(5,tipoManual);
				pst.setDouble(6,nroCuentaCobro);
				//				Sección de medicamentos
				pst.setDouble(7,nroCuentaCobro);
				//				Seccion de Otros
				pst.setDouble(8,nroCuentaCobro);
			}
				
			
			logger.info("consulta AV=> "+consulta);
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,true);
			pst.close();
			return mapaRetorno;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en consultaAV: "+e);
			return null;
		}
	}
}
