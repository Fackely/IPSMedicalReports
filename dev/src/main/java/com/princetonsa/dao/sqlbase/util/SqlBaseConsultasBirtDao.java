package com.princetonsa.dao.sqlbase.util;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.axioma.util.log.Log4JManager;

import com.princetonsa.dao.sqlbase.carteraPaciente.SqlBaseReportePagosCarteraPacienteDao;
import com.princetonsa.dao.sqlbase.glosas.SqlBaseGlosasDao;
import com.princetonsa.dto.carteraPaciente.DtoDeudor;
import com.princetonsa.dto.carteraPaciente.DtoDocumentosGarantia;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.ConstantesBDFacturacion;

public class SqlBaseConsultasBirtDao {
	
	/**
	 * Método que retorna la sentencia SQL para crear el reporte de 
	 * FURIPS (Formato Unico de Reclamación de los Prestadores de Servicios de Salud)
	 * Para Accidente de Transito
	 * @param cuenta
	 * @return consulta SQL
	 */
	public static String furipsAccidenteDeTransito(int factura, int codigoMedicoTratante, int codigoReclamacion){		
		String cadenaSql = "SELECT " +
								"CASE WHEN reclam.respuesta_glosa IS NOT NULL THEN 'X' ELSE '' END AS rg, " +
								"reclam.num_radica_anterior AS nro_radicado_anterior, " +
								"f.consecutivo_factura AS nro_factura, " +
								"ins.razon_social AS razon_social, " +
								"ins.cod_min_salud AS cod_habilitacion, " +
								"ins.nit AS nit, " +
								"pp.primer_apellido AS primer_ape_pac, " +
								"pp.segundo_apellido AS segundo_ape_pac, " +
								"pp.primer_nombre AS primer_nom_pac, " +
								"pp.segundo_nombre AS segundo_nom_pac, " +
								"pp.tipo_identificacion AS tipo_id_pac, " +
								"CASE pp.tipo_identificacion WHEN 'AS' THEN  rat.departamento_accidente||rat.ciudad_accidente||'NN'||pac.historia_clinica WHEN 'MS' THEN rat.departamento_accidente||rat.ciudad_accidente||'NN'||pac.historia_clinica ELSE pp.numero_identificacion END AS numero_id_pac, " +
								"to_char(pp.fecha_nacimiento, 'DD/MM/YYYY') AS fecha_nacimiento, " +
								"pp.direccion AS direccion_residencia, " +
								"getdescripcionsexo(pp.sexo) AS sexo_pac, " +
								"pp.codigo_departamento_vivienda AS cod_depto_pac, " +
								"getnombredepto(pp.codigo_pais_vivienda, pp.codigo_departamento_vivienda) AS nom_depto_pac, " +
								"pp.telefono AS telefono_pac, " +
								"pp.codigo_ciudad_vivienda AS cod_municipio_pac, " +
								"getnombreciudad(pp.codigo_pais_vivienda, pp.codigo_departamento_vivienda, pp.codigo_ciudad_vivienda) AS nom_municipio_pac, " +
								"CASE " +
									"WHEN rat.condicion_accidentado in('"+ConstantesIntegridadDominio.acronimoConductorVehiculo+"', '"+ConstantesIntegridadDominio.acronimoConductorMotocicleta+"') THEN 'Conductor' " +
									"WHEN rat.condicion_accidentado='"+ConstantesIntegridadDominio.acronimoPeaton+"' THEN 'Peaton' " +
									"WHEN rat.condicion_accidentado in('"+ConstantesIntegridadDominio.acronimoVehiculo+"', '"+ConstantesIntegridadDominio.acronimoMotocicleta+"') THEN 'Ocupante' " +
									"WHEN rat.condicion_accidentado='"+ConstantesIntegridadDominio.acronimoCiclista+"' THEN 'Ciclista' " +
								"END AS condicion_accidentado, " +
								"'AT' AS naturaleza_evento, " +
								"'' AS cual, " +
								"rat.lugar_accidente AS direccion_ocurrencia, " +
								"to_char(rat.fecha_accidente, 'DD/MM/YYYY') AS fecha_accidente, " +
								"rat.hora_accidente AS hora_accidente, " +
								"rat.departamento_accidente AS cod_depto_accidente, " +
								"getnombredepto(rat.pais_accidente, rat.departamento_accidente)  AS nom_depto_accidente, " +
								"rat.ciudad_accidente AS cod_municipio_accidente, " +
								"getnombreciudad(rat.pais_accidente,rat.departamento_accidente, rat.ciudad_accidente) AS nom_municipio_accidente, " +
								"getintegridaddominio(rat.zona_accidente) AS zona_accidente, " +
								"rat.descripcion_ocurrencia AS descripcion_evento, " +
								"getEstadoAseguramiento(rat.asegurado, rat.poliza) AS estado_aseguramiento, " +
								"rat.marca_vehiculo AS marca, " +
								"rat.placa AS placa, " +
								"tsv.descripcion AS tipo_servicio, " +
								"coalesce(conv.cod_aseguradora,'') AS cod_aseguradora, " +
								"rat.numero_poliza AS numero_poliza, " +
								"to_char(rat.fecha_inicial_poliza, 'DD/MM/YYYY') AS poliza_desde, " +
								"to_char(rat.fecha_final_poliza, 'DD/MM/YYYY') AS poliza_hasta, " +
								"getintegridaddominio(rat.intervencion_autoridad) AS intervencion_autoridad, " +
								"getintegridaddominio(rat.cobro_excedente_poliza) AS excedente_poliza, " +
								"rat.primer_apellido_prop AS primer_ape_prop, " +
								"rat.segundo_apellido_prop AS segundo_ape_prop, " +
								"rat.primer_nombre_prop AS primer_nom_prop, " +
								"rat.segundo_nombre_prop AS segundo_nom_prop, " +
								"rat.tipo_id_prop AS tipo_id_prop, " +
								"rat.numero_id_prop AS numero_id_prop, " +
								"rat.direccion_prop AS direccion_prop, " +
								"rat.departamento_prop AS cod_dep_prop, " +
								"getnombredepto(rat.pais_prop, rat.departamento_prop) AS nom_depto_prop, " +
								"rat.telefono_prop AS telefono_prop, " +
								"rat.ciudad_prop AS cod_mun_prop, " +
								"getnombreciudad(rat.pais_prop, rat.departamento_prop, rat.ciudad_prop) AS nom_municipio_prop, " +
								"rat.primer_apellido_conductor AS primer_ape_cond, " +
								"rat.segundo_apellido_conductor AS segundo_ape_cond, " +
								"rat.primer_nombre_conductor AS primer_nom_cond, " +
								"rat.segundo_nombre_conductor AS segundo_nom_cond, " +
								"rat.tipo_id_conductor AS tipo_id_cond, " +
								"rat.numero_id_conductor AS numero_id_cond, " +
								"rat.direccion_conductor AS direccion_cond, " +
								"rat.departamento_conductor AS cod_dep_cond, " +
								"getnombredepto(rat.pais_conductor, rat.departamento_conductor) AS nom_depto_cond, " +
								"rat.telefono_conductor AS telefono_cond, " +
								"rat.ciudad_conductor AS cod_mun_cond, " +
								"getnombreciudad(rat.pais_conductor, rat.departamento_conductor, rat.ciudad_conductor) AS nom_municipio_cond, " +
								"getintegridaddominio(rat.tipo_referencia) AS tipo_referencia, " +
								"to_char(rat.fecha_remision, 'DD/MM/YYYY') AS fecha_remision, " +
								"rat.hora_remision AS hora_remision, " +
								"getdescripcioninstitucionsirc(rat.cod_inscrip_remitente, i.institucion) AS prestador_remite, " +
								"rat.cod_inscrip_remitente AS cod_inscrip_remitente, " +
								"rat.profesional_remite AS profesional_remite, " +
								"rat.cargo_profesional_remitente AS cargo_profesional_remitente, " +
								"to_char(rat.fecha_aceptacion, 'DD/MM/YYYY') AS fecha_aceptacion, " +
								"rat.hora_aceptacion AS hora_aceptacion, " +
								"getdescripcioninstitucionsirc(rat.cod_inscrip_receptor, i.institucion) AS prestador_recibe, " +
								"rat.cod_inscrip_receptor AS cod_inscrip_receptor, " +
								"rat.profesional_recibe AS profesional_recibe, " +
								"rat.cargo_profesional_recibe AS cargo_profesional_recibe, " +
								"rat.placa_vehiculo_tranporta AS placa_vehiculo_tranporta, " +
								"rat.transporta_victima_desde AS transporta_victima_desde, " +
								"rat.transporta_victima_hasta AS transporta_victima_hasta, " +
								"getintegridaddominio(rat.tipo_transporte) AS tipo_transporte, " +
								"rat.direccion_transporta AS lugar_recoge_victima, " +
								"TO_CHAR(getfechaingreso(c.id, c.via_ingreso),'DD/MM/YYYY')  AS fecha_ingreso, " +
								"gethoraingreso(c.id, c.via_ingreso) AS hora_ingreso, " +
								"CASE WHEN c.via_ingreso=2 THEN to_char(i.fecha_ingreso, 'DD/MM/YYYY') WHEN c.via_ingreso=4 THEN to_char(i.fecha_ingreso, 'DD/MM/YYYY') ELSE to_char(e.fecha_egreso, 'DD/MM/YYYY') END AS fecha_egreso, " +
								"CASE WHEN c.via_ingreso=1 THEN i.hora_ingreso WHEN c.via_ingreso=3 THEN e.hora_egreso ELSE i.hora_ingreso END AS hora_egreso, " +
								"cert.nro_registro_medico AS registro_medico, " +
								"cert.primer_apellido_medico AS primer_ape_med, " +
								"cert.segundo_apellido_medico AS segundo_ape_med, " +
								"cert.primer_nombre_medico AS primer_nom_med, " +
								"cert.segundo_nombre_medico AS segundo_nom_med, " +
								"cert.tipo_documento_medico AS tipo_id_med, " +
								"cert.nro_documento_medico AS numero_id_med, " +
								"cert.acronimo_dx_ingreso||' - '||cert.tipo_cie_dx_ingreso AS dx_ingreso, " +
								"cert.acronimo_dx_rel1_ingreso||' - '||cert.tipo_cie_dx_rel1_ingreso AS dx_ingreso_rel_1, " +
								"cert.acronimo_dx_rel2_ingreso||' - '||cert.tipo_cie_dx_rel2_ingreso AS dx_ingreso_rel_2, " +
								"cert.acronimo_dx_egreso||' - '||cert.tipo_cie_dx_egreso AS dx_egreso, " +
								"cert.acronimo_dx_rel1_egreso||' - '||cert.tipo_cie_dx_rel1_egreso AS dx_egreso_rel_1," +
								"cert.acronimo_dx_rel2_egreso||' - '||cert.tipo_cie_dx_rel2_egreso AS dx_egreso_rel_2, " +
								"ampxrec.total_fac_amp_gast_medqx AS total_fac_amp_qx, " +
								"ampxrec.total_rec_amp_gast_medqx AS total_reclama_amp_qx, " +
								"ampxrec.total_fac_amp_gast_transmov AS total_fac_amp_tx, " +
								"ampxrec.total_rec_amp_gast_transmov AS total_reclama_amp_tx " +
							"FROM " +
								"ingresos i " +
							"INNER JOIN cuentas c ON (c.id_ingreso = i.id) "+
							"INNER JOIN ingresos_registro_accidentes ira ON (ira.ingreso = i.id) " +
							"INNER JOIN registro_accidentes_transito rat ON (rat.codigo = ira.codigo_registro) " +
							"INNER JOIN manejopaciente.reclamaciones_acc_eve_fact reclam ON (reclam.codigo_accidente=rat.codigo and reclam.codigo_pk="+codigoReclamacion+") " +
							"INNER JOIN manejopaciente.amparo_x_reclamar ampxrec on (ampxrec.codigo_reclamacion=reclam.codigo_pk) " +
							"INNER JOIN manejopaciente.cert_aten_medica_furips cert on (cert.codigo_reclamacion=reclam.codigo_pk) " +
							"INNER JOIN pacientes pac ON (pac.codigo_paciente = i.codigo_paciente) " +
							"LEFT OUTER JOIN facturas f ON (f.cuenta = c.id) " +
							"LEFT OUTER JOIN instituciones ins ON (ins.codigo = i.institucion) " +
							"LEFT OUTER JOIN personas pp ON (pp.codigo = i.codigo_paciente) " +
							"LEFT OUTER JOIN egresos e ON (e.cuenta = c.id) " +
							"LEFT OUTER JOIN tipo_serv_vehiculos tsv ON (tsv.codigo = rat.tipo_serv_v) " +	
							"LEFT OUTER JOIN convenios conv on (rat.aseguradora=conv.codigo) " +	
							"WHERE " +" f.consecutivo_factura = " + factura + " "+
							   "AND (((c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaFacturada+" or c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaFacturadaParcial+")) or (c.estado_cuenta!="+ConstantesBD.codigoEstadoCuentaFacturada+" and c.estado_cuenta!="+ConstantesBD.codigoEstadoCuentaFacturadaParcial+"))";
	
		
		return cadenaSql;
	}
	
	/**
	 * Método que retorna la sentencia SQL para crear el reporte de 
	 * FURIPS (Formato Unico de Reclamación de los Prestadores de Servicios de Salud)
	 * Para Accidente de Transito
	 * @param cuenta
	 * @return consulta SQL
	 */
	public static String informeAccidenteDeTransito(int ingreso, int codigoMedicoTratante){		
		String cadenaSql = "SELECT " +
								"'' AS rg, " +
								"'' AS nro_radicado_anterior, " +
								"'' AS nro_factura, " +
								"ins.razon_social AS razon_social, " +
								"ins.cod_min_salud AS cod_habilitacion, " +
								"ins.nit AS nit, " +
								"pp.primer_apellido AS primer_ape_pac, " +
								"pp.segundo_apellido AS segundo_ape_pac, " +
								"pp.primer_nombre AS primer_nom_pac, " +
								"pp.segundo_nombre AS segundo_nom_pac, " +
								"pp.tipo_identificacion AS tipo_id_pac, " +
								"CASE pp.tipo_identificacion WHEN 'AS' THEN  rat.departamento_accidente||rat.ciudad_accidente||'NN'||pac.historia_clinica WHEN 'MS' THEN rat.departamento_accidente||rat.ciudad_accidente||'NN'||pac.historia_clinica ELSE pp.numero_identificacion END AS numero_id_pac, " +
								"to_char(pp.fecha_nacimiento, 'DD/MM/YYYY') AS fecha_nacimiento, " +
								"pp.direccion AS direccion_residencia, " +
								"getdescripcionsexo(pp.sexo) AS sexo_pac, " +
								"pp.codigo_departamento_vivienda AS cod_depto_pac, " +
								"getnombredepto(pp.codigo_pais_vivienda, pp.codigo_departamento_vivienda) AS nom_depto_pac, " +
								"pp.telefono AS telefono_pac, " +
								"pp.codigo_ciudad_vivienda AS cod_municipio_pac, " +
								"getnombreciudad(pp.codigo_pais_vivienda, pp.codigo_departamento_vivienda, pp.codigo_ciudad_vivienda) AS nom_municipio_pac, " +
								"CASE " +
									"WHEN rat.condicion_accidentado in('"+ConstantesIntegridadDominio.acronimoConductorVehiculo+"', '"+ConstantesIntegridadDominio.acronimoConductorMotocicleta+"') THEN 'Conductor' " +
									"WHEN rat.condicion_accidentado='"+ConstantesIntegridadDominio.acronimoPeaton+"' THEN 'Peaton' " +
									"WHEN rat.condicion_accidentado in('"+ConstantesIntegridadDominio.acronimoVehiculo+"', '"+ConstantesIntegridadDominio.acronimoMotocicleta+"') THEN 'Ocupante' " +
									"WHEN rat.condicion_accidentado='"+ConstantesIntegridadDominio.acronimoCiclista+"' THEN 'Ciclista' " +
								"END AS condicion_accidentado, " +
								"'AT' AS naturaleza_evento, " +
								"'' AS cual, " +
								"rat.lugar_accidente AS direccion_ocurrencia, " +
								"to_char(rat.fecha_accidente, 'DD/MM/YYYY') AS fecha_accidente, " +
								"rat.hora_accidente AS hora_accidente, " +
								"rat.departamento_accidente AS cod_depto_accidente, " +
								"getnombredepto(rat.pais_accidente, rat.departamento_accidente)  AS nom_depto_accidente, " +
								"rat.ciudad_accidente AS cod_municipio_accidente, " +
								"getnombreciudad(rat.pais_accidente,rat.departamento_accidente, rat.ciudad_accidente) AS nom_municipio_accidente, " +
								"getintegridaddominio(rat.zona_accidente) AS zona_accidente, " +
								"rat.descripcion_ocurrencia AS descripcion_evento, " +
								"getEstadoAseguramiento(rat.asegurado, rat.poliza) AS estado_aseguramiento, " +
								"rat.marca_vehiculo AS marca, " +
								"rat.placa AS placa, " +
								"tsv.descripcion AS tipo_servicio, " +
								"coalesce(conv.cod_aseguradora,'') AS cod_aseguradora, " +
								"rat.numero_poliza AS numero_poliza, " +
								"to_char(rat.fecha_inicial_poliza, 'DD/MM/YYYY') AS poliza_desde, " +
								"to_char(rat.fecha_final_poliza, 'DD/MM/YYYY') AS poliza_hasta, " +
								"getintegridaddominio(rat.intervencion_autoridad) AS intervencion_autoridad, " +
								"getintegridaddominio(rat.cobro_excedente_poliza) AS excedente_poliza, " +
								"rat.primer_apellido_prop AS primer_ape_prop, " +
								"rat.segundo_apellido_prop AS segundo_ape_prop, " +
								"rat.primer_nombre_prop AS primer_nom_prop, " +
								"rat.segundo_nombre_prop AS segundo_nom_prop, " +
								"rat.tipo_id_prop AS tipo_id_prop, " +
								"rat.numero_id_prop AS numero_id_prop, " +
								"rat.direccion_prop AS direccion_prop, " +
								"rat.departamento_prop AS cod_dep_prop, " +
								"getnombredepto(rat.pais_prop, rat.departamento_prop) AS nom_depto_prop, " +
								"rat.telefono_prop AS telefono_prop, " +
								"rat.ciudad_prop AS cod_mun_prop, " +
								"getnombreciudad(rat.pais_prop, rat.departamento_prop, rat.ciudad_prop) AS nom_municipio_prop, " +
								"rat.primer_apellido_conductor AS primer_ape_cond, " +
								"rat.segundo_apellido_conductor AS segundo_ape_cond, " +
								"rat.primer_nombre_conductor AS primer_nom_cond, " +
								"rat.segundo_nombre_conductor AS segundo_nom_cond, " +
								"rat.tipo_id_conductor AS tipo_id_cond, " +
								"rat.numero_id_conductor AS numero_id_cond, " +
								"rat.direccion_conductor AS direccion_cond, " +
								"rat.departamento_conductor AS cod_dep_cond, " +
								"getnombredepto(rat.pais_conductor, rat.departamento_conductor) AS nom_depto_cond, " +
								"rat.telefono_conductor AS telefono_cond, " +
								"rat.ciudad_conductor AS cod_mun_cond, " +
								"getnombreciudad(rat.pais_conductor, rat.departamento_conductor, rat.ciudad_conductor) AS nom_municipio_cond, " +
								"getintegridaddominio(rat.tipo_referencia) AS tipo_referencia, " +
								"to_char(rat.fecha_remision, 'DD/MM/YYYY') AS fecha_remision, " +
								"rat.hora_remision AS hora_remision, " +
								"getdescripcioninstitucionsirc(rat.cod_inscrip_remitente, i.institucion) AS prestador_remite, " +
								"rat.cod_inscrip_remitente AS cod_inscrip_remitente, " +
								"rat.profesional_remite AS profesional_remite, " +
								"rat.cargo_profesional_remitente AS cargo_profesional_remitente, " +
								"to_char(rat.fecha_aceptacion, 'DD/MM/YYYY') AS fecha_aceptacion, " +
								"rat.hora_aceptacion AS hora_aceptacion, " +
								"getdescripcioninstitucionsirc(rat.cod_inscrip_receptor, i.institucion) AS prestador_recibe, " +
								"rat.cod_inscrip_receptor AS cod_inscrip_receptor, " +
								"rat.profesional_recibe AS profesional_recibe, " +
								"rat.cargo_profesional_recibe AS cargo_profesional_recibe, " +
								"rat.placa_vehiculo_tranporta AS placa_vehiculo_tranporta, " +
								"rat.transporta_victima_desde AS transporta_victima_desde, " +
								"rat.transporta_victima_hasta AS transporta_victima_hasta, " +
								"getintegridaddominio(rat.tipo_transporte) AS tipo_transporte, " +
								"rat.direccion_transporta AS lugar_recoge_victima, " +
								"TO_CHAR(getfechaingreso(c.id, c.via_ingreso),'DD/MM/YYYY')  AS fecha_ingreso, " +
								"gethoraingreso(c.id, c.via_ingreso) AS hora_ingreso, " +
								"CASE WHEN c.via_ingreso=2 THEN to_char(i.fecha_ingreso, 'DD/MM/YYYY') WHEN c.via_ingreso=4 THEN to_char(i.fecha_ingreso, 'DD/MM/YYYY') ELSE to_char(e.fecha_egreso, 'DD/MM/YYYY') END AS fecha_egreso, " +
								"CASE WHEN c.via_ingreso=1 THEN i.hora_ingreso WHEN c.via_ingreso=3 THEN e.hora_egreso ELSE i.hora_ingreso END AS hora_egreso, " +
								"'' AS registro_medico, " +
								"'' AS primer_ape_med, " +
								"'' AS segundo_ape_med, " +
								"'' AS primer_nom_med, " +
								"'' AS segundo_nom_med, " +
								"'' AS tipo_id_med, " +
								"'' AS numero_id_med, " +
								"'' AS dx_ingreso, " +
								"'' AS dx_ingreso_rel_1, " +
								"'' AS dx_ingreso_rel_2, " +
								"'' AS dx_egreso, " +
								"'' AS dx_egreso_rel_1," +
								"'' AS dx_egreso_rel_2, " +
								"'' AS total_fac_amp_qx, " +
								"'' AS total_reclama_amp_qx, " +
								"'' AS total_fac_amp_tx, " +
								"'' AS total_reclama_amp_tx " +
							"FROM " +
								"ingresos i " +
							"INNER JOIN cuentas c ON (c.id_ingreso = i.id) "+
							"INNER JOIN ingresos_registro_accidentes ira ON (ira.ingreso = i.id) " +
							"INNER JOIN registro_accidentes_transito rat ON (rat.codigo = ira.codigo_registro) " +
							"INNER JOIN pacientes pac ON (pac.codigo_paciente = i.codigo_paciente) " +
							"LEFT OUTER JOIN facturas f ON (f.cuenta = c.id) " +
							"LEFT OUTER JOIN instituciones ins ON (ins.codigo = i.institucion) " +
							"LEFT OUTER JOIN personas pp ON (pp.codigo = i.codigo_paciente) " +
							"LEFT OUTER JOIN egresos e ON (e.cuenta = c.id) " +
							"LEFT OUTER JOIN tipo_serv_vehiculos tsv ON (tsv.codigo = rat.tipo_serv_v) " +	
							"LEFT OUTER JOIN convenios conv on (rat.aseguradora=conv.codigo) " +	
							"WHERE " +
								"i.id = " + ingreso + " "+
								"AND c.id = (SELECT MAX(cu.id) FROM cuentas cu WHERE cu.id_ingreso = "+ingreso+") "+
								"AND (((c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaFacturada+" or c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaFacturadaParcial+") and f.codigo = (SELECT MAX(fac.codigo) FROM facturas fac WHERE fac.cuenta = c.id) ) or (c.estado_cuenta!="+ConstantesBD.codigoEstadoCuentaFacturada+" and c.estado_cuenta!="+ConstantesBD.codigoEstadoCuentaFacturadaParcial+"))";
		
		return cadenaSql;
	}
	
	/**
	 * Método que retorna la sentencia SQL para crear el reporte de 
	 * FURIPS (Formato Unico de Reclamación de los Prestadores de Servicios de Salud)
	 * Para Evento Catastrófico
	 * @param cuenta
	 * @return consulta SQL
	 */
	public static String furipsEventoCatastrofico(int ingreso, int codigoMedicoTratante, int codigoReclamacion){		
		String cadenaSql = "SELECT " +
								"CASE WHEN reclam.respuesta_glosa IS NOT NULL THEN 'X' ELSE '' END AS rg, " +
								"reclam.num_radica_anterior AS nro_radicado_anterior, " +
								"f.consecutivo_factura AS nro_factura, " +
								"ins.razon_social AS razon_social, " +
								"ins.cod_min_salud AS cod_habilitacion, " +
								"ins.nit AS nit, " +
								"pp.primer_apellido AS primer_ape_pac, " +
								"pp.segundo_apellido AS segundo_ape_pac, " +
								"pp.primer_nombre AS primer_nom_pac, " +
								"pp.segundo_nombre AS segundo_nom_pac, " +
								"pp.tipo_identificacion AS tipo_id_pac, " +
								"CASE pp.tipo_identificacion WHEN 'AS' THEN  rec.departamento_evento||rec.ciudad_evento||'NN'||pac.historia_clinica WHEN 'MS' THEN rec.departamento_evento||rec.ciudad_evento||'NN'||pac.historia_clinica ELSE pp.numero_identificacion END AS numero_id_pac, " +
								"to_char(pp.fecha_nacimiento, 'DD/MM/YYYY') AS fecha_nacimiento, " +
								"pp.direccion AS direccion_residencia, " +
								"getdescripcionsexo(pp.sexo) AS sexo_pac, " +
								"pp.codigo_departamento_vivienda AS cod_depto_pac, " +
								"getnombredepto(pp.codigo_pais_vivienda, pp.codigo_departamento_vivienda) AS nom_depto_pac, " +
								"pp.telefono AS telefono_pac, " +
								"pp.codigo_ciudad_vivienda AS cod_municipio_pac, " +
								"getnombreciudad(pp.codigo_pais_vivienda, pp.codigo_departamento_vivienda, pp.codigo_ciudad_vivienda) AS nom_municipio_pac, " +
								"'' AS condicion_accidentado, " +
								"'EC' AS naturaleza_evento, " +
								"rec.desc_otro_evento AS cual, " +
								"rec.direccion_evento AS direccion_ocurrencia, " +
								"to_char(rec.fecha_evento, 'DD/MM/YYYY') AS fecha_accidente, " +
								"rec.hora_ocurrencia AS hora_accidente," +
								"rec.departamento_evento AS cod_depto_accidente, " +
								"getnombredepto(rec.pais_evento, rec.departamento_evento)  AS nom_depto_accidente, " +
								"rec.ciudad_evento AS cod_municipio_accidente, " +
								"getnombreciudad(rec.pais_evento, rec.departamento_evento, rec.ciudad_evento) AS nom_municipio_accidente, " +
								"getintegridaddominio(rec.zona_urbana_evento) AS zona_accidente, " +
								"rec.desc_ocurrencia AS descripcion_evento, " +
								"'' AS estado_aseguramiento, " +
								"'' AS marca," +
								"'' AS placa, " +
								"'' AS tipo_servicio, " +
								"'' AS cod_aseguradora, " +
								"'' AS numero_poliza, " +
								"'' AS poliza_desde, " +
								"'' AS poliza_hasta, " +
								"'' AS intervencion_autoridad, " +
								"'' AS excedente_poliza," +
								"'' AS primer_ape_prop, " +
								"'' AS segundo_ape_prop, " +
								"'' AS primer_nom_prop, " +
								"'' AS segundo_nom_prop, " +
								"'' AS tipo_id_prop, " +
								"'' AS numero_id_prop, " +
								"'' AS direccion_prop, " +
								"'' AS cod_dep_prop, " +
								"'' AS nom_depto_prop, " +
								"'' AS telefono_prop, " +
								"'' AS cod_mun_prop," +
								"'' AS nom_municipio_prop, " +
								"'' AS primer_ape_cond, " +
								"'' AS segundo_ape_cond, " +
								"'' AS primer_nom_cond, " +
								"'' AS segundo_nom_cond, " +
								"'' AS tipo_id_cond, " +
								"'' AS numero_id_cond, " +
								"'' AS direccion_cond, " +
								"'' AS cod_dep_cond, " +
								"'' AS nom_depto_cond, " +
								"'' AS telefono_cond, " +
								"'' AS cod_mun_cond, " +
								"'' AS nom_municipio_cond, " +
								"getintegridaddominio(rec.tipo_referencia) AS tipo_referencia, " +
								"to_char(rec.fecha_remision, 'DD/MM/YYYY') AS fecha_remision, " +
								"rec.hora_remision AS hora_remision, " +
								"getdescripcioninstitucionsirc(rec.cod_inscrip_remitente, i.institucion) AS prestador_remite, " +
								"rec.cod_inscrip_remitente AS cod_inscrip_remitente, " +
								"rec.profesional_remite AS profesional_remite, " +
								"rec.cargo_profesional_remitente AS cargo_profesional_remitente, " +
								"to_char(rec.fecha_aceptacion, 'DD/MM/YYYY') AS fecha_aceptacion, " +
								"rec.hora_aceptacion AS hora_aceptacion, " +
								"getdescripcioninstitucionsirc(rec.cod_inscrip_receptor, i.institucion) AS prestador_recibe, " +
								"rec.cod_inscrip_receptor AS cod_inscrip_receptor, " +
								"rec.profesional_recibe AS profesional_recibe, " +
								"rec.cargo_profesional_recibe AS cargo_profesional_recibe, " +
								"rec.placa_vehiculo_tranporta AS placa_vehiculo_tranporta, " +
								"rec.transporta_victima_desde AS transporta_victima_desde, " +
								"rec.transporta_victima_hasta AS transporta_victima_hasta, " +
								"getintegridaddominio(rec.tipo_transporte) AS tipo_transporte, " +
								"rec.direccion_transporta AS lugar_recoge_victima, " +
								"TO_CHAR(getfechaingreso(c.id, c.via_ingreso),'DD/MM/YYYY') AS fecha_ingreso, "+								
								"gethoraingreso(c.id, c.via_ingreso) AS hora_ingreso, " +
								"CASE WHEN c.via_ingreso=2 THEN to_char(i.fecha_ingreso, 'DD/MM/YYYY') WHEN c.via_ingreso=4 THEN to_char(i.fecha_ingreso, 'DD/MM/YYYY') ELSE to_char(e.fecha_egreso, 'DD/MM/YYYY') END AS fecha_egreso, " +
								"CASE WHEN c.via_ingreso=1 THEN i.hora_ingreso WHEN c.via_ingreso=3 THEN i.hora_ingreso ELSE e.hora_egreso END AS hora_egreso, " +
								"cert.nro_registro_medico AS registro_medico, " +
								"cert.primer_apellido_medico AS primer_ape_med, " +
								"cert.segundo_apellido_medico AS segundo_ape_med, " +
								"cert.primer_nombre_medico AS primer_nom_med, " +
								"cert.segundo_nombre_medico AS segundo_nom_med, " +
								"cert.tipo_documento_medico AS tipo_id_med, " +
								"cert.nro_documento_medico AS numero_id_med, " +
								"cert.acronimo_dx_ingreso||' - '||cert.tipo_cie_dx_ingreso AS dx_ingreso, " +
								"cert.acronimo_dx_rel1_ingreso||' - '||cert.tipo_cie_dx_rel1_ingreso AS dx_ingreso_rel_1, " +
								"cert.acronimo_dx_rel2_ingreso||' - '||cert.tipo_cie_dx_rel2_ingreso AS dx_ingreso_rel_2, " +
								"cert.acronimo_dx_egreso||' - '||cert.tipo_cie_dx_egreso AS dx_egreso, " +
								"cert.acronimo_dx_rel1_egreso||' - '||cert.tipo_cie_dx_rel1_egreso AS dx_egreso_rel_1," +
								"cert.acronimo_dx_rel2_egreso||' - '||cert.tipo_cie_dx_rel2_egreso AS dx_egreso_rel_2, " +
								"ampxrec.total_fac_amp_gast_medqx AS total_fac_amp_qx, " +
								"ampxrec.total_rec_amp_gast_medqx AS total_reclama_amp_qx, " +
								"ampxrec.total_fac_amp_gast_transmov AS total_fac_amp_tx, " +
								"ampxrec.total_rec_amp_gast_transmov AS total_reclama_amp_tx " +
							"FROM " +
								"ingresos i " +
							"INNER JOIN cuentas c ON (c.id_ingreso = i.id) "+
							"INNER JOIN facturas f ON (f.cuenta = c.id) " +	
							"INNER JOIN ingresos_reg_even_catastrofico irec ON (irec.ingreso = i.id) " +
							"INNER JOIN registro_evento_catastrofico rec ON (rec.codigo = irec.codigo_registro) " +
							"INNER JOIN manejopaciente.reclamaciones_acc_eve_fact reclam ON (reclam.codigo_evento=rec.codigo and reclam.codigo_pk="+codigoReclamacion+") " +
							"INNER JOIN manejopaciente.amparo_x_reclamar ampxrec on (ampxrec.codigo_reclamacion=reclam.codigo_pk) " +
							"INNER JOIN manejopaciente.cert_aten_medica_furips cert on (cert.codigo_reclamacion=reclam.codigo_pk) " +
							"INNER JOIN instituciones ins ON (ins.codigo = i.institucion) " +
							"INNER JOIN personas pp ON (pp.codigo = i.codigo_paciente) " +
							"INNER JOIN pacientes pac ON (pac.codigo_paciente = i.codigo_paciente) " +
							"LEFT OUTER JOIN egresos e ON (e.cuenta = c.id) " +
							"WHERE " +
								"i.id = " + ingreso + " "+
								"AND c.id = (SELECT MAX(cu.id) FROM cuentas cu WHERE cu.id_ingreso = "+ingreso+") "+
								"AND f.codigo = (SELECT MAX(fac.codigo) FROM facturas fac WHERE fac.cuenta = c.id)";
					
			return cadenaSql;
		}
	
	/**
	 * Método que retorna la sentencia SQL para crear el reporte de 
	 * FURIPS (Formato Unico de Reclamación de los Prestadores de Servicios de Salud)
	 * Para Evento Catastrófico
	 * @param cuenta
	 * @return consulta SQL
	 */
	public static String informeEventoCatastrofico(int ingreso, int codigoMedicoTratante){		
		String cadenaSql = "SELECT " +
								"'' AS rg, " +
								"'' AS nro_radicado_anterior, " +
								"'' AS nro_factura, " +
								"ins.razon_social AS razon_social, " +
								"ins.cod_min_salud AS cod_habilitacion, " +
								"ins.nit AS nit, " +
								"pp.primer_apellido AS primer_ape_pac, " +
								"pp.segundo_apellido AS segundo_ape_pac, " +
								"pp.primer_nombre AS primer_nom_pac, " +
								"pp.segundo_nombre AS segundo_nom_pac, " +
								"pp.tipo_identificacion AS tipo_id_pac, " +
								"CASE pp.tipo_identificacion WHEN 'AS' THEN  rec.departamento_evento||rec.ciudad_evento||'NN'||pac.historia_clinica WHEN 'MS' THEN rec.departamento_evento||rec.ciudad_evento||'NN'||pac.historia_clinica ELSE pp.numero_identificacion END AS numero_id_pac, " +
								"to_char(pp.fecha_nacimiento, 'DD/MM/YYYY') AS fecha_nacimiento, " +
								"pp.direccion AS direccion_residencia, " +
								"getdescripcionsexo(pp.sexo) AS sexo_pac, " +
								"pp.codigo_departamento_vivienda AS cod_depto_pac, " +
								"getnombredepto(pp.codigo_pais_vivienda, pp.codigo_departamento_vivienda) AS nom_depto_pac, " +
								"pp.telefono AS telefono_pac, " +
								"pp.codigo_ciudad_vivienda AS cod_municipio_pac, " +
								"getnombreciudad(pp.codigo_pais_vivienda, pp.codigo_departamento_vivienda, pp.codigo_ciudad_vivienda) AS nom_municipio_pac, " +
								"'' AS condicion_accidentado, " +
								"'EC' AS naturaleza_evento, " +
								"rec.desc_otro_evento AS cual, " +
								"rec.direccion_evento AS direccion_ocurrencia, " +
								"to_char(rec.fecha_evento, 'DD/MM/YYYY') AS fecha_accidente, " +
								"rec.hora_ocurrencia AS hora_accidente," +
								"rec.departamento_evento AS cod_depto_accidente, " +
								"getnombredepto(rec.pais_evento, rec.departamento_evento)  AS nom_depto_accidente, " +
								"rec.ciudad_evento AS cod_municipio_accidente, " +
								"getnombreciudad(rec.pais_evento, rec.departamento_evento, rec.ciudad_evento) AS nom_municipio_accidente, " +
								"getintegridaddominio(rec.zona_urbana_evento) AS zona_accidente, " +
								"rec.desc_ocurrencia AS descripcion_evento, " +
								"'' AS estado_aseguramiento, " +
								"'' AS marca," +
								"'' AS placa, " +
								"'' AS tipo_servicio, " +
								"'' AS cod_aseguradora, " +
								"'' AS numero_poliza, " +
								"'' AS poliza_desde, " +
								"'' AS poliza_hasta, " +
								"'' AS intervencion_autoridad, " +
								"'' AS excedente_poliza," +
								"'' AS primer_ape_prop, " +
								"'' AS segundo_ape_prop, " +
								"'' AS primer_nom_prop, " +
								"'' AS segundo_nom_prop, " +
								"'' AS tipo_id_prop, " +
								"'' AS numero_id_prop, " +
								"'' AS direccion_prop, " +
								"'' AS cod_dep_prop, " +
								"'' AS nom_depto_prop, " +
								"'' AS telefono_prop, " +
								"'' AS cod_mun_prop," +
								"'' AS nom_municipio_prop, " +
								"'' AS primer_ape_cond, " +
								"'' AS segundo_ape_cond, " +
								"'' AS primer_nom_cond, " +
								"'' AS segundo_nom_cond, " +
								"'' AS tipo_id_cond, " +
								"'' AS numero_id_cond, " +
								"'' AS direccion_cond, " +
								"'' AS cod_dep_cond, " +
								"'' AS nom_depto_cond, " +
								"'' AS telefono_cond, " +
								"'' AS cod_mun_cond, " +
								"'' AS nom_municipio_cond, " +
								"getintegridaddominio(rec.tipo_referencia) AS tipo_referencia, " +
								"to_char(rec.fecha_remision, 'DD/MM/YYYY') AS fecha_remision, " +
								"rec.hora_remision AS hora_remision, " +
								"getdescripcioninstitucionsirc(rec.cod_inscrip_remitente, i.institucion) AS prestador_remite, " +
								"rec.cod_inscrip_remitente AS cod_inscrip_remitente, " +
								"rec.profesional_remite AS profesional_remite, " +
								"rec.cargo_profesional_remitente AS cargo_profesional_remitente, " +
								"to_char(rec.fecha_aceptacion, 'DD/MM/YYYY') AS fecha_aceptacion, " +
								"rec.hora_aceptacion AS hora_aceptacion, " +
								"getdescripcioninstitucionsirc(rec.cod_inscrip_receptor, i.institucion) AS prestador_recibe, " +
								"rec.cod_inscrip_receptor AS cod_inscrip_receptor, " +
								"rec.profesional_recibe AS profesional_recibe, " +
								"rec.cargo_profesional_recibe AS cargo_profesional_recibe, " +
								"rec.placa_vehiculo_tranporta AS placa_vehiculo_tranporta, " +
								"rec.transporta_victima_desde AS transporta_victima_desde, " +
								"rec.transporta_victima_hasta AS transporta_victima_hasta, " +
								"getintegridaddominio(rec.tipo_transporte) AS tipo_transporte, " +
								"rec.direccion_transporta AS lugar_recoge_victima, " +
								"TO_CHAR(getfechaingreso(c.id, c.via_ingreso),'DD/MM/YYYY') AS fecha_ingreso, "+								
								"gethoraingreso(c.id, c.via_ingreso) AS hora_ingreso, " +
								"CASE WHEN c.via_ingreso=2 THEN to_char(i.fecha_ingreso, 'DD/MM/YYYY') WHEN c.via_ingreso=4 THEN to_char(i.fecha_ingreso, 'DD/MM/YYYY') ELSE to_char(e.fecha_egreso, 'DD/MM/YYYY') END AS fecha_egreso, " +
								"CASE WHEN c.via_ingreso=1 THEN i.hora_ingreso WHEN c.via_ingreso=3 THEN i.hora_ingreso ELSE e.hora_egreso END AS hora_egreso, " +
								"'' AS registro_medico, " +
								"'' AS primer_ape_med, " +
								"'' AS segundo_ape_med, " +
								"'' AS primer_nom_med, " +
								"'' AS segundo_nom_med, " +
								"'' AS tipo_id_med, " +
								"'' AS numero_id_med, " +
								"'' AS dx_ingreso, " +
								"'' AS dx_ingreso_rel_1, " +
								"'' AS dx_ingreso_rel_2, " +
								"'' AS dx_egreso, " +
								"'' AS dx_egreso_rel_1," +
								"'' AS dx_egreso_rel_2, " +
								"'' AS total_fac_amp_qx, " +
								"'' AS total_reclama_amp_qx, " +
								"'' AS total_fac_amp_tx, " +
								"'' AS total_reclama_amp_tx " +
							"FROM " +
								"ingresos i " +
							"INNER JOIN cuentas c ON (c.id_ingreso = i.id) "+
							"INNER JOIN facturas f ON (f.cuenta = c.id) " +	
							"INNER JOIN ingresos_reg_even_catastrofico irec ON (irec.ingreso = i.id) " +
							"INNER JOIN registro_evento_catastrofico rec ON (rec.codigo = irec.codigo_registro) " +
							"INNER JOIN instituciones ins ON (ins.codigo = i.institucion) " +
							"INNER JOIN personas pp ON (pp.codigo = i.codigo_paciente) " +
							"INNER JOIN pacientes pac ON (pac.codigo_paciente = i.codigo_paciente) " +
							"LEFT OUTER JOIN egresos e ON (e.cuenta = c.id) " +
							"WHERE " +
								"i.id = " + ingreso + " "+
								"AND c.id = (SELECT MAX(cu.id) FROM cuentas cu WHERE cu.id_ingreso = "+ingreso+") "+
								"AND f.codigo = (SELECT MAX(fac.codigo) FROM facturas fac WHERE fac.cuenta = c.id)";
					
			return cadenaSql;
		}
	
		/**
		 * Método que retorna la sentencia SQL para crear el reporte de 
		 * FURPRO (Formato Unico de Reclamación para victimas de eventos terroristas 
		 * por servicios de  rehabilitacion y adaptación de prótesis)
		 * @param cuenta
		 * @return consulta SQL
		 */
		public static String furpro(int ingreso, int codigoReclamacion){		
			String cadenaSql = "SELECT " +
									"CASE WHEN reclam.respuesta_glosa IS NOT NULL THEN 'X' ELSE '' END AS rg, " +
									"reclam.num_radica_anterior AS nro_radicado_anterior, " +
									"f.consecutivo_factura AS nro_factura, " +
									"ins.razon_social AS razon_social, " +
									"ins.cod_min_salud AS cod_habilitacion, " +
									"ins.nit AS nit, " +
									"ins.direccion AS direccion, " +
									"ins.telefono AS telefono, " +
									"getnombredepto(ins.pais, ins.departamento) AS nom_depto_ins, " +
									"ins.departamento AS cod_depto_ins, " +
									"getnombreciudad(ins.pais, ins.departamento, ins.ciudad) AS nom_municipio_ins, " +
									"ins.ciudad AS cod_municipio_ins, " +
									"pp.primer_apellido AS primer_ape_pac, " +
									"pp.segundo_apellido AS segundo_ape_pac, " +
									"pp.primer_nombre AS primer_nom_pac, " +
									"pp.segundo_nombre AS segundo_nom_pac, " +
									"pp.tipo_identificacion AS tipo_id_pac, " +
									"CASE pp.tipo_identificacion WHEN 'AS' THEN  rec.departamento_evento||rec.ciudad_evento||'NN'||pac.historia_clinica WHEN 'MS' THEN rec.departamento_evento||rec.ciudad_evento||'NN'||pac.historia_clinica ELSE pp.numero_identificacion END AS numero_id_pac, " +
									"to_char(pp.fecha_nacimiento, 'DD/MM/YYYY') AS fecha_nacimiento, " +
									"pp.direccion AS direccion_residencia, " +
									"getdescripcionsexo(pp.sexo) AS sexo_pac, " +
									"pp.codigo_departamento_vivienda AS cod_depto_pac, " +
									"getnombredepto(pp.codigo_pais_vivienda, pp.codigo_departamento_vivienda) AS nom_depto_pac, " +
									"pp.telefono AS telefono_pac, " +
									"pp.codigo_ciudad_vivienda AS cod_municipio_pac, " +
									"getnombreciudad(pp.codigo_pais_vivienda, pp.codigo_departamento_vivienda, pp.codigo_ciudad_vivienda) AS nom_municipio_pac, " +
									"getintegridaddominio(rec.sgsss) AS sgsss, " +
									"CASE WHEN rec.sgsss='"+ConstantesIntegridadDominio.acronimoNoAfiliadoSgsss+"' THEN '' ELSE getintegridaddominio(rec.tipo_regimen) END AS tipo_regimen, " +
									"CASE WHEN rec.sgsss='"+ConstantesIntegridadDominio.acronimoNoAfiliadoSgsss+"' THEN '' ELSE getnombreconvenio(sc.convenio) END AS entidad_afiliado, " +
									"CASE " +
										"WHEN rec.naturaleza_evento="+ConstantesBD.codigoNatEventoSismo +" then '02' " +
										"WHEN rec.naturaleza_evento="+ConstantesBD.codigoNatEventoMaremotos +" then '03' " +
										"WHEN rec.naturaleza_evento="+ConstantesBD.codigoNatEventoEruoVolcanicas +" then '04' " +
										"WHEN rec.naturaleza_evento="+ConstantesBD.codigoNatEventoDeslizamientoTierra +" then '05' " +
										"WHEN rec.naturaleza_evento in("+ConstantesBD.codigoNatEventoInundaciones+", "+ConstantesBD.codigoNatEventoSustanciasToxicas+", "+ConstantesBD.codigoNatEventoCorrosivos+", "+ConstantesBD.codigoNatEventoEnvenenamiento+") then '06' " +
										"WHEN rec.naturaleza_evento="+ConstantesBD.codigoNatEventoAvalanchas +" then '07' " +
										"WHEN rec.naturaleza_evento in("+ConstantesBD.codigoNatEventoIncendioNatural +", "+ConstantesBD.codigoNatEventoIncendio+") then '08' " +
										"WHEN rec.naturaleza_evento="+ConstantesBD.codigoNatEventoExplosionTerrorista +" then '09' " +
										"WHEN rec.naturaleza_evento="+ConstantesBD.codigoNatEventoIncendioTerrorista +" then '10' " +
										"WHEN rec.naturaleza_evento="+ConstantesBD.codigoNatEventoCombate +" then '11' " +
										"WHEN rec.naturaleza_evento="+ConstantesBD.codigoNatEventoTomaGuerrillera +" then '12' " +
										"WHEN rec.naturaleza_evento="+ConstantesBD.codigoNatEventoMasacre +" then '13' " +
										"WHEN rec.naturaleza_evento="+ConstantesBD.codigoNatEventoMinaAntipersonal +" then '15' " +
										"WHEN rec.naturaleza_evento="+ConstantesBD.codigoNatEventoHuracan +" then '16' " +
										"WHEN rec.naturaleza_evento in("+ConstantesBD.codigoNatEventoOtros +", "+ConstantesBD.codigoNatEventoOtrosEnat+", "+ConstantesBD.codigoNatEventoOtrosEtec+") then '17' " +
									"END AS naturaleza_evento, " +
									"rec.desc_otro_evento AS cual, " +
									"rec.direccion_evento AS direccion_ocurrencia, " +
									"to_char(rec.fecha_evento, 'DD/MM/YYYY') AS fecha_accidente, " +
									"rec.hora_ocurrencia AS hora_accidente, " +
									"rec.departamento_evento AS cod_depto_accidente, " +
									"getnombredepto(rec.pais_evento, rec.departamento_evento)  AS nom_depto_accidente, " +
									"rec.ciudad_evento AS cod_municipio_accidente, " +
									"getnombreciudad(rec.pais_evento, rec.departamento_evento, rec.ciudad_evento) AS nom_municipio_accidente, " +
									"getintegridaddominio(rec.zona_urbana_evento) AS zona_accidente, " +
									"rec.desc_ocurrencia AS descripcion_evento, " +
									"cert.acronimo_dx_egreso||' - '||cert.tipo_cie_dx_egreso AS dx_ingreso, " +  //en el formato del pdf esta quemado el alias y como ingreso, en lugar de egreso, por eso no modifico el alias, para no modificar la plantilla
									"cert.acronimo_dx_rel1_egreso||' - '||cert.tipo_cie_dx_rel1_egreso AS dx_ingreso_rel_1, " +  //en el formato del pdf esta quemado el alias y como ingreso, en lugar de egreso, por eso no modifico el alias, para no modificar la plantilla
									"cert.acronimo_dx_rel2_egreso||' - '||cert.tipo_cie_dx_rel2_egreso AS dx_ingreso_rel_2, " +  //en el formato del pdf esta quemado el alias y como ingreso, en lugar de egreso, por eso no modifico el alias, para no modificar la plantilla
									"cert.acronimo_dx_rel3_egreso||' - '||cert.tipo_cie_dx_rel3_egreso AS dx_ingreso_rel_3, " +  //en el formato del pdf esta quemado el alias y como ingreso, en lugar de egreso, por eso no modifico el alias, para no modificar la plantilla
									"cert.acronimo_dx_rel4_egreso||' - '||cert.tipo_cie_dx_rel4_egreso AS dx_ingreso_rel_4, " +  //en el formato del pdf esta quemado el alias y como ingreso, en lugar de egreso, por eso no modifico el alias, para no modificar la plantilla
									"servrec.protesis AS protesis, " +
									"servrec.valor_protesis AS valor_protesis, " +
									"servrec.adaptacion_protesis AS adaptacion_protesis, " +
									"servrec.valor_adap_protesis AS valor_adaptacion_protesis, " +
									"servrec.rehabilitacion AS rehabilitacion, " +
									"servrec.valor_rehabilitacion AS valor_rehabilitacion, " +
									"(servrec.valor_protesis + servrec.valor_adap_protesis + servrec.valor_rehabilitacion) AS valor_reclamado " +
								"FROM ingresos i " +
								"INNER JOIN cuentas c ON (c.id_ingreso = i.id) " +
								"INNER JOIN facturas f ON (f.cuenta = c.id) " +
								"INNER JOIN ingresos_reg_even_catastrofico irec ON (irec.ingreso = i.id) " +
								"INNER JOIN registro_evento_catastrofico rec ON (rec.codigo = irec.codigo_registro) " +
								"INNER JOIN manejopaciente.reclamaciones_acc_eve_fact reclam ON (reclam.codigo_evento=rec.codigo and reclam.codigo_pk="+codigoReclamacion+") " +
								"INNER JOIN manejopaciente.servicios_reclamados servrec on (servrec.codigo_reclamacion=reclam.codigo_pk) " +
								"INNER JOIN manejopaciente.cert_aten_medica_furpro cert on (cert.codigo_reclamacion=reclam.codigo_pk) " +
								"INNER JOIN instituciones ins ON (ins.codigo = i.institucion) " +
								"INNER JOIN personas pp ON (pp.codigo = i.codigo_paciente) " +
								"INNER JOIN pacientes pac ON (pac.codigo_paciente = i.codigo_paciente) " +
								"INNER JOIN sub_cuentas sc ON (sc.ingreso = i.id AND sc.nro_prioridad=1) " +
								"LEFT OUTER JOIN egresos e ON (e.cuenta = c.id) " +
								"WHERE " +
									"i.id = "+ingreso+" " +
									"AND c.id = (SELECT MAX(cu.id) FROM cuentas cu WHERE cu.id_ingreso =  "+ingreso+"  ) " +
									"AND f.codigo = (SELECT MAX(fac.codigo) FROM facturas fac WHERE fac.cuenta = c.id)";
			return cadenaSql;
		}
		
		/**
		 * Método que retorna la sentencia SQL para crear el reporte de 
		 * FURPRO (Formato Unico de Reclamación para victimas de eventos terroristas 
		 * por servicios de  rehabilitacion y adaptación de prótesis)
		 * @param cuenta
		 * @return consulta SQL
		 */
		public static String informeFurpro(int ingreso){		
			String cadenaSql = "SELECT " +
									"'' AS rg, " +
									"'' AS nro_radicado_anterior, " +
									"'' AS nro_factura, " +
									"ins.razon_social AS razon_social, " +
									"ins.cod_min_salud AS cod_habilitacion, " +
									"ins.nit AS nit, " +
									"ins.direccion AS direccion, " +
									"ins.telefono AS telefono, " +
									"getnombredepto(ins.pais, ins.departamento) AS nom_depto_ins, " +
									"ins.departamento AS cod_depto_ins, " +
									"getnombreciudad(ins.pais, ins.departamento, ins.ciudad) AS nom_municipio_ins, " +
									"ins.ciudad AS cod_municipio_ins, " +
									"pp.primer_apellido AS primer_ape_pac, " +
									"pp.segundo_apellido AS segundo_ape_pac, " +
									"pp.primer_nombre AS primer_nom_pac, " +
									"pp.segundo_nombre AS segundo_nom_pac, " +
									"pp.tipo_identificacion AS tipo_id_pac, " +
									"CASE pp.tipo_identificacion WHEN 'AS' THEN  rec.departamento_evento||rec.ciudad_evento||'NN'||pac.historia_clinica WHEN 'MS' THEN rec.departamento_evento||rec.ciudad_evento||'NN'||pac.historia_clinica ELSE pp.numero_identificacion END AS numero_id_pac, " +
									"to_char(pp.fecha_nacimiento, 'DD/MM/YYYY') AS fecha_nacimiento, " +
									"pp.direccion AS direccion_residencia, " +
									"getdescripcionsexo(pp.sexo) AS sexo_pac, " +
									"pp.codigo_departamento_vivienda AS cod_depto_pac, " +
									"getnombredepto(pp.codigo_pais_vivienda, pp.codigo_departamento_vivienda) AS nom_depto_pac, " +
									"pp.telefono AS telefono_pac, " +
									"pp.codigo_ciudad_vivienda AS cod_municipio_pac, " +
									"getnombreciudad(pp.codigo_pais_vivienda, pp.codigo_departamento_vivienda, pp.codigo_ciudad_vivienda) AS nom_municipio_pac, " +
									"getintegridaddominio(rec.sgsss) AS sgsss, " +
									"CASE WHEN rec.sgsss='"+ConstantesIntegridadDominio.acronimoNoAfiliadoSgsss+"' THEN '' ELSE getintegridaddominio(rec.tipo_regimen) END AS tipo_regimen, " +
									"CASE WHEN rec.sgsss='"+ConstantesIntegridadDominio.acronimoNoAfiliadoSgsss+"' THEN '' ELSE getnombreconvenio(sc.convenio) END AS entidad_afiliado, " +
									"CASE " +
										"WHEN rec.naturaleza_evento="+ConstantesBD.codigoNatEventoSismo +" then '02' " +
										"WHEN rec.naturaleza_evento="+ConstantesBD.codigoNatEventoMaremotos +" then '03' " +
										"WHEN rec.naturaleza_evento="+ConstantesBD.codigoNatEventoEruoVolcanicas +" then '04' " +
										"WHEN rec.naturaleza_evento="+ConstantesBD.codigoNatEventoDeslizamientoTierra +" then '05' " +
										"WHEN rec.naturaleza_evento in("+ConstantesBD.codigoNatEventoInundaciones+", "+ConstantesBD.codigoNatEventoSustanciasToxicas+", "+ConstantesBD.codigoNatEventoCorrosivos+", "+ConstantesBD.codigoNatEventoEnvenenamiento+") then '06' " +
										"WHEN rec.naturaleza_evento="+ConstantesBD.codigoNatEventoAvalanchas +" then '07' " +
										"WHEN rec.naturaleza_evento in("+ConstantesBD.codigoNatEventoIncendioNatural +", "+ConstantesBD.codigoNatEventoIncendio+") then '08' " +
										"WHEN rec.naturaleza_evento="+ConstantesBD.codigoNatEventoExplosionTerrorista +" then '09' " +
										"WHEN rec.naturaleza_evento="+ConstantesBD.codigoNatEventoIncendioTerrorista +" then '10' " +
										"WHEN rec.naturaleza_evento="+ConstantesBD.codigoNatEventoCombate +" then '11' " +
										"WHEN rec.naturaleza_evento="+ConstantesBD.codigoNatEventoTomaGuerrillera +" then '12' " +
										"WHEN rec.naturaleza_evento="+ConstantesBD.codigoNatEventoMasacre +" then '13' " +
										"WHEN rec.naturaleza_evento="+ConstantesBD.codigoNatEventoMinaAntipersonal +" then '15' " +
										"WHEN rec.naturaleza_evento="+ConstantesBD.codigoNatEventoHuracan +" then '16' " +
										"WHEN rec.naturaleza_evento in("+ConstantesBD.codigoNatEventoOtros +", "+ConstantesBD.codigoNatEventoOtrosEnat+", "+ConstantesBD.codigoNatEventoOtrosEtec+") then '17' " +
									"END AS naturaleza_evento, " +
									"rec.desc_otro_evento AS cual, " +
									"rec.direccion_evento AS direccion_ocurrencia, " +
									"to_char(rec.fecha_evento, 'DD/MM/YYYY') AS fecha_accidente, " +
									"rec.hora_ocurrencia AS hora_accidente, " +
									"rec.departamento_evento AS cod_depto_accidente, " +
									"getnombredepto(rec.pais_evento, rec.departamento_evento)  AS nom_depto_accidente, " +
									"rec.ciudad_evento AS cod_municipio_accidente, " +
									"getnombreciudad(rec.pais_evento, rec.departamento_evento, rec.ciudad_evento) AS nom_municipio_accidente, " +
									"getintegridaddominio(rec.zona_urbana_evento) AS zona_accidente, " +
									"rec.desc_ocurrencia AS descripcion_evento, " +
									"'' AS dx_ingreso, " +  //en el formato del pdf esta quemado el alias y como ingreso, en lugar de egreso, por eso no modifico el alias, para no modificar la plantilla
									"'' AS dx_ingreso_rel_1, " +  //en el formato del pdf esta quemado el alias y como ingreso, en lugar de egreso, por eso no modifico el alias, para no modificar la plantilla
									"'' AS dx_ingreso_rel_2, " +  //en el formato del pdf esta quemado el alias y como ingreso, en lugar de egreso, por eso no modifico el alias, para no modificar la plantilla
									"'' AS dx_ingreso_rel_3, " +  //en el formato del pdf esta quemado el alias y como ingreso, en lugar de egreso, por eso no modifico el alias, para no modificar la plantilla
									"'' AS dx_ingreso_rel_4, " +  //en el formato del pdf esta quemado el alias y como ingreso, en lugar de egreso, por eso no modifico el alias, para no modificar la plantilla
									"'' AS protesis, " +
									"'' AS valor_protesis, " +
									"'' AS adaptacion_protesis, " +
									"'' AS valor_adaptacion_protesis, " +
									"'' AS rehabilitacion, " +
									"'' AS valor_rehabilitacion, " +
									"'' AS valor_reclamado " +
								"FROM ingresos i " +
								"INNER JOIN cuentas c ON (c.id_ingreso = i.id) " +
								"INNER JOIN facturas f ON (f.cuenta = c.id) " +
								"INNER JOIN ingresos_reg_even_catastrofico irec ON (irec.ingreso = i.id) " +
								"INNER JOIN registro_evento_catastrofico rec ON (rec.codigo = irec.codigo_registro) " +
								"INNER JOIN instituciones ins ON (ins.codigo = i.institucion) " +
								"INNER JOIN personas pp ON (pp.codigo = i.codigo_paciente) " +
								"INNER JOIN pacientes pac ON (pac.codigo_paciente = i.codigo_paciente) " +
								"INNER JOIN sub_cuentas sc ON (sc.ingreso = i.id AND sc.nro_prioridad=1) " +
								"LEFT OUTER JOIN egresos e ON (e.cuenta = c.id) " +
								"WHERE " +
									"i.id = "+ingreso+" " +
									"AND c.id = (SELECT MAX(cu.id) FROM cuentas cu WHERE cu.id_ingreso =  "+ingreso+"  ) " +
									"AND f.codigo = (SELECT MAX(fac.codigo) FROM facturas fac WHERE fac.cuenta = c.id)";
			return cadenaSql;
		}
			
		/**
		 * Método que retorna la sentencia SQL para crear el reporte de 
		 * Perfil de Farmacoterapia
		 * @param ingreso
		 * @param mes en formato 'nombreMes YYYY'
		 * @return consulta SQL
		 */
		public static String perfilDeFarmacoterapia(int ingreso, String mes){
			String cadenaSQL = "SELECT " +
									"lower(getdescarticulosincodigo(a.codigo)) AS nombre_art, " +
									"a.codigo AS codigo_art, " +
									"a.codigo_interfaz AS cod_interfaz_art, " +
									"ds.frecuencia||' '||ds.tipo_frecuencia AS frecuencia, " +
									"ds.dosis || coalesce(' '||uxa.unidad_medida,'') AS dosis, " +
									"ds.via AS via, " +
									"getnombremes(to_char(da.fecha, 'MM'))||' '||to_char(da.fecha, 'YYYY') AS mes, " +
									"to_char(da.fecha, 'YYYY')||to_char(da.fecha, 'MM') AS cod_mes, " +
									"to_char(da.fecha, 'DD') AS dia, " +
									"da.cantidad AS cantidad " +
								"FROM " +
									"cuentas c " +
								"INNER JOIN " +
									"solicitudes s ON (s.cuenta = c.id) " +
								"INNER JOIN " +
									"detalle_solicitudes ds ON (ds.numero_solicitud = s.numero_solicitud) " +
								"INNER JOIN " +
									"admin_medicamentos am ON (am.numero_solicitud = s.numero_solicitud) " +
								"INNER JOIN " +
									"detalle_admin da ON (da.administracion = am.codigo AND da.articulo=ds.articulo) " +
								"INNER JOIN " +
									"articulo a ON (a.codigo = da.articulo) " +
								"LEFT OUTER JOIN " +
									"unidosis_x_articulo uxa on(uxa.codigo = ds.unidosis_articulo) " +
								"WHERE " +
									"c.id_ingreso = " + ingreso +
									"AND s.tipo= " + ConstantesBD.codigoTipoSolicitudMedicamentos + " " +
									"AND estado_historia_clinica<> " + ConstantesBD.codigoEstadoHCAnulada+" " +
									"AND (getnombremes(to_char(da.fecha, 'MM'))||' '||to_char(da.fecha, 'YYYY'))='"+mes+"' " +
									"AND da.cantidad <> 0 " +
								"ORDER BY " +
									"nombre_art, " +
									"dosis ";
			return cadenaSQL;
		}
		
		
		
		/**
		 * Método que retorna la sentencia SQL para crear el reporte de 
		 * Existencias
		 * @param tipoBusqueda (codigo, descripcion, claseGrupoSubgrupo)
		 * @param almacen
		 * @param clase
		 * @param grupo
		 * @param subgrupo
		 * @param mostrarExt
		 * @param articulo
		 * @param institucion
		 * @param cod_interfaz
		 * @return consulta SQL
		 */
		public static String existenciasDeInventario (String tipoBusqueda, int almacen, int clase, int grupo, int subgrupo, String mostrarExt, String articulo, int institucion){
			String cadenaSQL = "SELECT DISTINCT " +
									"aa.articulo as codigo," +
									"va.codigo_interfaz as codigoint," +
									"getdescarticulosincodigo(va.codigo) as descripcion," +
									"lower(descripcionum) as unidadmedida," +
									"CASE WHEN va.estado="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'Activo' ELSE 'Inactivo' END as estado," +
									"getubicacionarticulo(va.codigo,aa.almacen) as ubicacion," +
									"va.costopromedio as costo," +
									"aa.existencias as existencias," +
									"(va.costopromedio*aa.existencias) as valtotal," +
									"aa.almacen AS almacen " +
								"FROM " +
									"articulos_almacen aa " +
								"INNER JOIN " +
									"view_articulos va on (aa.articulo=va.codigo) " +
								"WHERE " +
									"aa.almacen="+almacen+" " +
									"AND aa.institucion="+institucion+" ";
									//"AND va.estado="+ValoresPorDefecto.getValorTrueParaConsultas()+" ";
			
			
			if (UtilidadTexto.getBoolean(mostrarExt)==false)
				cadenaSQL += 		"AND aa.existencias<>0 ";
			
			if (tipoBusqueda.equals("claseGrupoSubgrupo")){
				if (clase>0)
					cadenaSQL +=	"AND va.clase="+clase+" ";
				if (grupo>0)
					cadenaSQL +=	"AND va.grupo="+grupo+" ";
				if (subgrupo>0)
					cadenaSQL +=	"AND va.subgrupo="+subgrupo+" ";
			}
			
			if (tipoBusqueda.equals("codigo")){
				if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(institucion).equals(ConstantesIntegridadDominio.acronimoAxioma))
					cadenaSQL += 	"AND va.codigo="+articulo+" ";
				else if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(institucion).equals(ConstantesIntegridadDominio.acronimoInterfaz))
					cadenaSQL += 	"AND va.codigo_interfaz='"+articulo+"' ";
			}
			
			if (tipoBusqueda.equals("descripcion")){
				cadenaSQL += 		"AND upper(va.descripcion) like upper('%"+articulo+"%') ";
			}
			
			cadenaSQL += 			"ORDER BY getdescarticulosincodigo(va.codigo)";
	        
			return cadenaSQL;
		}
		
		/**
		 * Método que retorna la sentencia SQL para crear el reporte de 
		 * Existencias
		 * @param tipoBusqueda (codigo, descripcion, claseGrupoSubgrupo)
		 * @param almacen
		 * @param clase
		 * @param grupo
		 * @param subgrupo
		 * @param mostrarExt
		 * @param articulo
		 * @param institucion
		 * @param cod_interfaz
		 * @return consulta SQL
		 */
		public static String existenciasDeInventario2 (String tipoBusqueda, int almacen, int clase, int grupo, int subgrupo, String mostrarExt, String articulo, int institucion){
			String cadenaSQL = "SELECT DISTINCT " +
									"aa.articulo as codigo," +
									"va.codigo_interfaz as codigoint," +
									"getdescarticulosincodigo(va.codigo) as descripcion," +
									"lower(descripcionum) as unidadmedida," +
									"CASE WHEN va.estado="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'Activo' ELSE 'Inactivo' END as estado," +
									"getubicacionarticulo(va.codigo,aa.almacen) as ubicacion," +
									"va.costopromedio as costo," +
									"aa.existencias as existencias," +
									"(va.costopromedio*aa.existencias) as valtotal," +
									"aa.almacen AS almacen " +
								"FROM " +
									"articulos_almacen aa " +
								"INNER JOIN " +
									"view_articulos va on (aa.articulo=va.codigo) " +
								"WHERE " +
									"aa.almacen="+almacen+" " +
									"AND aa.institucion="+institucion+" ";
									//"AND va.estado="+ValoresPorDefecto.getValorTrueParaConsultas()+" ";
			
			
			if (UtilidadTexto.getBoolean(mostrarExt)==false)
				cadenaSQL += 		"AND aa.existencias<>0 ";
			
			if (tipoBusqueda.equals("claseGrupoSubgrupo")){
				if (clase>0)
					cadenaSQL +=	"AND va.clase="+clase+" ";
				if (grupo>0)
					cadenaSQL +=	"AND va.grupo="+grupo+" ";
				if (subgrupo>0)
					cadenaSQL +=	"AND va.subgrupo="+subgrupo+" ";
			}
			
			if (tipoBusqueda.equals("codigo")){
				if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(institucion).equals(ConstantesIntegridadDominio.acronimoAxioma))
					cadenaSQL += 	"AND va.codigo="+articulo+" ";
				else if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(institucion).equals(ConstantesIntegridadDominio.acronimoInterfaz))
					cadenaSQL += 	"AND va.codigo_interfaz='"+articulo+"' ";
			}
			
			if (tipoBusqueda.equals("descripcion")){
				cadenaSQL += 		"AND upper(va.descripcion) like upper('%"+articulo+"%') ";
			}
			
			cadenaSQL += 			"ORDER BY descripcion";
	        
			return cadenaSQL;
		}
		
		/**
		 * 
		 * @param oldQuery
		 * @param consecutivosOrdenesInsertadas
		 * @param institucion
		 * @return
		 */
		public static String modificarConsultaOrdenesAmbServicios(String oldQuery,  Vector<String> consecutivosOrdenesInsertadas, int institucion)
		{
			String newQuery = "SELECT " +
									"getcodigoservicio(doas.servicio, "+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion)+") as codigo_cups, " +
									"getnombreservicio(doas.servicio, "+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion)+") as descripcion_servicio, " +
									"case when s.espos="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'Si' else 'No' end as es_pos, " +
									"getnomfinalidadservicio(doas.finalidad) as finalidad, " +
									"doas.cantidad as cantidad, " +
									"case when oa.urgente = "+ValoresPorDefecto.getValorFalseParaConsultas()+" then 'No' else 'Si' end as urgente " +
								"from " +
									"ordenes_ambulatorias oa " +
									"inner join det_orden_amb_servicio doas on(oa.codigo=doas.codigo_orden) " +
									"inner join servicios s on (doas.servicio=s.codigo) " +
									"inner join referencias_servicio rs on(rs.servicio=s.codigo and tipo_tarifario=0) " +
								"where " +
									"oa.consecutivo_orden in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(consecutivosOrdenesInsertadas, true)+") " +
									"and oa.institucion="+institucion;
	        return newQuery;
		}

		/**
		 * Método que retorna la sentencia SQL para crear el reporte de 
		 * Listado de facturas auditadas
		 * @param con
		 * @param fechaAuditoriaInicial
		 * @param fechaAuditoriaFinal
		 * @param facturaInicial
		 * @param facturaFinal
		 * @param codigoConvenio
		 * @param codigoContrato
		 * @param numeroPreGlosa
		 * @param codigoInstitucionInt
		 */
		public static String listadoFacturasAuditadas(Connection con, String fechaAuditoriaInicial, String fechaAuditoriaFinal, String facturaInicial, String facturaFinal, int codigoConvenio, int codigoContrato, String numeroPreGlosa, int codigoInstitucionInt) 
		{
			String consulta = SqlBaseGlosasDao.getConsultaFacturasAuditadasStr();
			String seccionWHERE = "";
			if(!fechaAuditoriaInicial.equals("")&&!fechaAuditoriaFinal.equals(""))
				seccionWHERE = (seccionWHERE.equals("")?"":" AND ") + " to_char(rg.fecha_auditoria, 'YYYY-MM-DD') between '"+fechaAuditoriaInicial+"' and '"+fechaAuditoriaFinal+"' ";
			if(Utilidades.convertirAEntero(numeroPreGlosa)>0)
				seccionWHERE += (seccionWHERE.equals("")?"":" AND ") + " rg.glosa_sistema = '"+numeroPreGlosa+"' ";
			if(codigoConvenio>0)
				seccionWHERE += (seccionWHERE.equals("")?"":" AND ") + " rg.convenio = "+codigoConvenio+" ";
			seccionWHERE += (seccionWHERE.equals("")?"":" AND ") + " rg.estado = '"+ConstantesIntegridadDominio.acronimoEstadoAuditado+"' and rg.institucion = "+codigoInstitucionInt+"  ";				
			if(codigoContrato>0)
				seccionWHERE += (seccionWHERE.equals("")?"":" AND ") + " ag.contrato = "+codigoContrato+" ";
			if(!facturaInicial.equals("")&&!facturaFinal.equals(""))
				seccionWHERE += (seccionWHERE.equals("")?"":" AND ") + " f.consecutivo_factura between "+facturaInicial+" and "+facturaFinal+" ";
				
			consulta += seccionWHERE + " ORDER BY c.nombre, f.consecutivo_factura ";		
			
			return consulta;
		}
		
		public static String reporteEstadoCarteraYGlosas(String fechaCorte,String tipoConvenio, int convenio, int institucionInt, int consulta, String validacion)
		{
			String consultaEnviar="SELECT tabla."+"nomconvenio"+" AS "+"nomconv"+", "+
									  "tabla."+"tipoconvenio"+" AS "+"tipoconv"+", "+
									  "tabla."+"descripciontipoconvenio"+" AS "+"desctipoconv"+", "+
									  "tabla."+"carterasinobj"+" AS "+"carteraobj"+", "+
									  "tabla."+"glosasoprad"+" AS "+"glosasop"+", "+
									  "tabla."+"carterasinobj"+"+tabla."+"glosasoprad"+" AS "+"totalcorriente"+", "+
									  "tabla."+"facturaspendrad"+" AS "+"factpend"+", "+
									  "tabla."+"glosapenresp"+" AS "+"glosapend"+", "+
									  "tabla."+"facturaspendrad"+"+tabla."+"glosapenresp"+" AS "+"totalnocorriente"+", ";
									  
			if(consulta==1)
				consultaEnviar += "tabla."+"carterasinobj"+"  +tabla."+"glosasoprad"+"+tabla."+"facturaspendrad"+"+tabla."+"glosapenresp"+" AS "+"saldocartera"+" ";
			
			if(consulta==2)			
			{
				consultaEnviar += "tabla."+"carterasinobj"+"  +tabla."+"glosasoprad"+"+tabla."+"facturaspendrad"+"+tabla."+"glosapenresp"+" AS "+"saldocartera"+", "+  
								"tabla."+"consecutivofactura"+" AS "+"factura"+" ";
			}
			
			consultaEnviar +=    "FROM "+
								  "(SELECT DISTINCT getnombreconvenio(f.convenio) AS "+"nomconvenio"+", "+
							       "gettipoconvenio(f.convenio) AS "+"tipoconvenio"+", "+
							       "tc.descripcion AS "+"descripciontipoconvenio"+", ";
							     
		    if(consulta==2)
		    	consultaEnviar += "f.consecutivo_factura AS "+"consecutivofactura"+", ";
							       
							       
		    consultaEnviar += "(SELECT SUM(f.valor_total) AS "+"sumavalores"+" "+
							         "FROM facturas f "+
							            "INNER JOIN cuentas_cobro cc ON(cc.numero_cuenta_cobro=f.numero_cuenta_cobro) "+
							            "LEFT OUTER JOIN auditorias_glosas ag ON(f.codigo= ag.codigo_factura) "+
							            "LEFT OUTER JOIN registro_glosas rg ON(rg.codigo=ag.glosa) "+   
							         "WHERE cc.estado=4 "+
							            "AND (coalesce(f.valor_total,0)	+ "+
							                "coalesce(f.ajustes_debito,0) - "+
							                "coalesce(f.ajustes_credito,0) + "+
							                "coalesce(cc.ajuste_debito,0) - "+
							                "coalesce(cc.ajuste_credito,0)	- "+
							                "coalesce(cc.pagos,0)) > 0 "+
							            "AND ag.fue_auditada='GL' "+ 
							            "AND rg.estado='REGI' ) AS "+"carterasinobj"+", "+
							        "(SELECT SUM(ag.valor_glosa_factura) AS "+"sumavalores"+" "+
							          "FROM registro_glosas rg "+
							            "INNER JOIN auditorias_glosas ag ON(ag.glosa=rg.codigo) "+
							            "INNER JOIN facturas f ON(ag.codigo_factura=f.codigo) "+
							            "INNER JOIN respuesta_glosa re ON(re.glosa=rg.codigo) "+
							          "WHERE  (coalesce(f.valor_total,0)	+ "+
							                  "coalesce(f.ajustes_debito,0) - "+
							                  "coalesce(f.ajustes_credito,0)) > 0 "+
							              "AND re.estado='RADIC') AS "+"glosasoprad"+", "+
							        "(SELECT SUM(f.valor_total) AS "+"sumavalores"+" "+
							          "FROM facturas f "+
							             "LEFT OUTER JOIN cuentas_cobro cc ON(f.numero_cuenta_cobro=cc.numero_cuenta_cobro) "+
							          "WHERE cc.estado "+validacion+" 4) AS "+"facturaspendrad"+", "+
							        "(SELECT SUM(ag.valor_glosa_factura) AS "+"sumavalores"+" "+
							          "FROM registro_glosas rg "+
							            "INNER JOIN auditorias_glosas ag ON(ag.glosa=rg.codigo) "+
							            "INNER JOIN facturas f ON(f.codigo= ag.codigo_factura) "+
							            "INNER JOIN respuesta_glosa re ON(re.glosa=rg.codigo) "+
							          "WHERE rg.estado='APRO' "+
							            "AND (re.estado='REGI' OR re.estado='APRO')) AS "+"glosapenresp"+" "+
							  "FROM facturas f " +
							  "INNER JOIN convenios c ON(c.codigo=f.convenio) " +
							  "INNER JOIN tipos_convenio tc ON(tc.codigo=c.tipo_convenio AND tc.institucion=c.institucion) " +
							  "WHERE to_char(f.fecha,'"+ConstantesBD.formatoFechaAp+"') between '"+UtilidadFecha.conversionFormatoFechaAAp(fechaCorte)+"' AND '"+UtilidadFecha.getFechaActual()+"' "; 
				
				if(!tipoConvenio.equals("-1"))
					consultaEnviar += "AND c.tipo_convenio= '"+tipoConvenio+"' ";
				if(convenio > 0)
					consultaEnviar += "AND f.convenio= "+convenio+" ";
								
				consultaEnviar +="ORDER BY "+"tipoconvenio"+","+"nomconvenio"+" ) tabla ";
				
			return consultaEnviar; 
		}
		
		/**
		 * Método que retorna la sentencia SQL para crear el reporte de 
		 * Listado de facturas auditadas
		 * @param con
		 * @param fechaAuditoriaInicial
		 * @param fechaAuditoriaFinal
		 * @param facturaInicial
		 * @param facturaFinal
		 * @param codigoConvenio
		 * @param codigoContrato
		 * @param numeroPreGlosa
		 * @param codigoInstitucionInt
		 */
		public static String formatoSubaConciliacionEncabezado(Connection con, String codConciliacion) 
		{
			String consulta =" SELECT r.fecha_respuesta  AS fecha, " +
								"r.hora_respuesta AS hora, " +
								"r.nro_acta AS acta, " +
								"r.concepto_respuesta AS conceptoConc, " +
								"r.porcen_soportado AS psoportado, " +
								"r.porcen_aceptado AS paceptado, " +
								"r.observaciones AS observaciones, " +
								"r.representante_convenio AS respconvenio, " +
								"r.cargo_rep_convenio AS cargorepconvenio, " +
								"r.representante_inst AS repinstitucion, " +
								"r.cargo_rep_inst AS cargorepins " +
								"FROM glosas.respuesta_glosa r " +
								"WHERE r.codigo    = "+codConciliacion+"";  		 
												
			return consulta;
		}
		
		/**
		 * Metodo que retorna la sentencia SQL para crear el reporte de 
		 * Listado de facturas auditadas
		 * @param con
		 * @param fechaAuditoriaInicial
		 * @param fechaAuditoriaFinal
		 * @param facturaInicial
		 * @param facturaFinal
		 * @param codigoConvenio
		 * @param codigoContrato
		 * @param numeroPreGlosa
		 * @param codigoInstitucionInt
		 */
		public static String formatoSubaConciliacionFacturas(Connection con, String codConciliacion) 
		{
			String consultaFact=" SELECT f.consecutivo_factura AS factura, " +
			"f.fecha AS fechafactura, " +
			"administracion.getcentroatencioncc(f.centro_aten) AS centroatencion, " +
			"f.valor_total AS valorfactura, " +
			"administracion.getnombrepersona(f.cod_paciente) AS paciente, " +
			"manejopaciente.getnombreviaingreso(f.via_ingreso) AS viaingreso, " +
			"frg.valor_aceptado AS valoraceptadofact, " +
			"frg.valor_soportado AS valorsoportadofact, " +
			"ag.valor_glosa_factura AS valorglosafact " +
			"FROM glosas.fact_respuesta_glosa frg " +
			"INNER JOIN facturacion.facturas f ON(frg.factura=f.codigo)" +
			"INNER JOIN glosas.auditorias_glosas ag ON(frg.auditoria_glosa=ag.codigo) " +
			"WHERE frg.respuesta_glosa= "+codConciliacion+""; 
			
			return consultaFact;
		}
		
		/**
		 * Metodo que retorna la sentencia SQL para crear el reporte de 
		 * Listado de facturas auditadas
		 * @param con
		 * @param fechaAuditoriaInicial
		 * @param fechaAuditoriaFinal
		 * @param facturaInicial
		 * @param facturaFinal
		 * @param codigoConvenio
		 * @param codigoContrato
		 * @param numeroPreGlosa
		 * @param codigoInstitucionInt
		 */
		public static String formatoSubaConciliacionSolicitudes(Connection con, String codConciliacion, String codigoTarifario, int institucion, String tipo) 
		{
			String consultaSol="  SELECT glosas.getdescripcionconceptoglosa(dfrg.concepto_glosa, "+institucion+") AS conceptoglosa, " +
									"CASE " +
									"WHEN dfs.articulo IS NULL " +
									"THEN 'SERV' " +
									"ELSE 'ARTI' " +
									"END AS esarticulo, " +
									"CASE " +
									"WHEN dag.servicio IS NULL " +
									"THEN getdescarticulo(dag.articulo) " +
									" ELSE '(' " +
									" || getcodigopropservicio2(dag.servicio,'"+codigoTarifario+"') " +
									"|| ') ' " +
									"|| getnombreservicio(dag.servicio, "+ConstantesBD.codigoTarifarioCups+") " +
									"END AS descripcionarticuloservicio, " +
									"dag.cantidad_glosa AS cantidad, " +
									"dag.valor_glosa AS valorglosasoli, " +
									"dfrg.valor_soportado AS valorsoportadosol, " +
									"dfrg.valor_aceptado AS valoraceptadosol " +
									"FROM glosas.fact_respuesta_glosa frg " +
									"INNER JOIN glosas.det_fact_resp_glosa dfrg " +
									"ON(dfrg.fact_respuesta_glosa=frg.codigo) " +
									"INNER JOIN glosas.det_auditorias_glosas dag " +
									"ON(dfrg.det_auditoria_glosa=dag.codigo) " +
									"INNER JOIN det_factura_solicitud dfs " +
									"ON (dfs.codigo = dag.det_factura_solicitud) " +
									"WHERE frg.respuesta_glosa= "+codConciliacion+"";
			
			return consultaSol;
		}
		
		/**
		 * Metodo que retorna la sentencia SQL para crear el reporte de 
		 * pagos Cartera Paciente
		 * @param con
		 * @param fechaAuditoriaInicial
		 * @param fechaAuditoriaFinal
		 * @param facturaInicial
		 * @param facturaFinal
		 * @param codigoConvenio
		 * @param codigoContrato
		 * @param numeroPreGlosa
		 * @param codigoInstitucionInt
		 */
		public static String reportePagosCarteraPacientePagos(int codigoPk,String fechaIni, String fechaFin,
				int anioIni, int anioFin, 
				String tipoPeriodo) 
		{
			String consulta = SqlBaseReportePagosCarteraPacienteDao.getConsultarAplicPagos();
				
			consulta+="WHERE datos_financiacion= "+codigoPk+" ";
			
			if(tipoPeriodo.equals(ConstantesIntegridadDominio.acronimoTipoPeriodoMensual))
				consulta += "AND to_char(fecha_aplicacion,'dd/mm/yyyy') BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' ";
			else if(tipoPeriodo.equals(ConstantesIntegridadDominio.acronimoTipoPeriodoAnual))
				consulta += "AND to_char(fecha_aplicacion,'YYYY') BETWEEN "+anioIni+" AND "+anioFin+" ";
			
			return consulta;
		}
		
		/**
		 * Metodo que retorna la sentencia SQL para crear el reporte de 
		 * pagos Cartera Paciente
		 * @param con
		 * @param fechaAuditoriaInicial
		 * @param fechaAuditoriaFinal
		 * @param facturaInicial
		 * @param facturaFinal
		 * @param codigoConvenio
		 * @param codigoContrato
		 * @param numeroPreGlosa
		 * @param codigoInstitucionInt
		 */
		public static String reportePagosCarteraPaciente(String fechaIni, String fechaFin,
				int anioIni, int anioFin, int centroAtencion, String tipoDoc,
				String tipoPeriodo) 
		{
			String consulta ="SELECT DISTINCT getintegridaddominio(dg.tipo_documento) AS tipodoc, "+
										"dg.consecutivo AS consecutivo, "+
										"dg.anio_consecutivo AS aniocons, "+
										"carterapaciente.getnomdeudoringreso(d.ingreso) AS deudor, "+
										"dg.valor AS valordoc, " +
										"dg.fecha_generacion AS fechagen, "+
										"dg.estado AS estado, " +
										"df.codigo_pk AS datosfin, " +
										"ca.descripcion AS centroatencion, ";
						
			if(tipoPeriodo.equals(ConstantesIntegridadDominio.acronimoTipoPeriodoMensual))
				consulta += "carterapaciente.getAplicPagosMensual(df.codigo_pk,'"+UtilidadFecha.conversionFormatoFechaAAp(fechaIni)+"', '"+UtilidadFecha.conversionFormatoFechaAAp(fechaFin)+"') AS pagos ";	
			else if(tipoPeriodo.equals(ConstantesIntegridadDominio.acronimoTipoPeriodoAnual))
				consulta += "carterapaciente.getAplicPagosAnual(df.codigo_pk,"+anioIni+","+anioFin+") AS pagos ";	
										
			consulta += "FROM carterapaciente.deudorco d " +
								"INNER JOIN carterapaciente.deudores_datos_finan ddf ON(ddf.codigo_pk_deudor=d.codigo_pk) " +
								"INNER JOIN carterapaciente.datos_financiacion df ON(ddf.datos_financiacion=df.codigo_pk) "+
								"INNER JOIN carterapaciente.documentos_garantia dg ON(df.codigo_pk_docgarantia=dg.codigo_pk) "+
								"INNER JOIN manejopaciente.ingresos i ON(i.id=dg.ingreso) " +
								"INNER JOIN administracion.centro_atencion ca ON(i.centro_atencion=ca.consecutivo)" ;
			
			consulta += "WHERE 1=1 ";
			
			if(tipoDoc.equals("-1"))
				consulta += "AND (dg.tipo_documento="+ConstantesIntegridadDominio.acronimoTipoDocumentoLetra+" OR dg.tipo_documento="+ConstantesIntegridadDominio.acronimoTipoDocumentoPagare+" ) ";
			if(tipoDoc.equals(ConstantesIntegridadDominio.acronimoTipoDocumentoLetra))
				consulta += "AND dg.tipo_documento= '"+ConstantesIntegridadDominio.acronimoTipoDocumentoLetra+"' ";
			if(tipoDoc.equals(ConstantesIntegridadDominio.acronimoTipoDocumentoPagare))
				consulta += "AND dg.tipo_documento= '"+ConstantesIntegridadDominio.acronimoTipoDocumentoPagare+"' ";
			if(centroAtencion > 0)
				consulta += "AND i.centro_atencion= "+centroAtencion+" ";
						
			return consulta;
		}
		
		
		
		/**
		 * Metodo que retorna la sentencia SQL de facturas pacientes para crear el reporte de 
		 * movimientos totales por tipo de documento
		 * @param con
		 * @param criterios
		 * @return
		 */
		public static String listadoFacturasPacientes(Connection con, HashMap criterios) 
		{
			String consulta = "SELECT tc.descripcion AS tipo_convenio    , " +
							  "SUM(f.valor_total)   AS valor_facturacion, " +
							  "SUM(CASE WHEN af.codigo IS NULL THEN 0 ELSE f.valor_total END) AS valor_anulacion " +
							   "FROM facturacion.facturas f " +
							"INNER JOIN facturacion.convenios c " +
							     "ON (c.codigo = f.convenio) " +
							"INNER JOIN facturacion.tipos_convenio tc " +
							     "ON(tc.codigo  = c.tipo_convenio " +
							"AND tc.institucion = c.institucion) " +
							"LEFT OUTER JOIN facturacion.anulaciones_facturas af " +
							     "ON(af.codigo = f.codigo " +
							"AND to_char(af.fecha_grabacion,'yyyy-mm-dd') BETWEEN '" + UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaInicial").toString()) + "' AND '" + UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaFinal").toString()) + "') " +
							  "WHERE to_char(f.fecha,'yyyy-mm-dd') BETWEEN '" + UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaInicial").toString()) + "' AND '" + UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaFinal").toString()) + "' " +
							"GROUP BY tc.descripcion " +
							"ORDER BY \"tipo_convenio\"";
			
			return consulta;
		}
		
		/**
		 * Metodo que retorna la sentencia SQL de ingresos para crear el reporte de 
		 * movimientos totales por tipo de documento
		 * @param con
		 * @param criterios
		 * @return
		 */
		public static String listadoIngresos(Connection con, HashMap criterios) 
		{
			String consulta = 
				" SELECT t.\"centro_atencion\" as centro_atencion, " +
				  "t.\"grupo\" as grupo, " +
				  "t.\"nombre\" as nombre, " +
				  "t.\"valor_ingreso\" as valor_ingreso, " +
				  "t.\"valor_anulacion\" as valor_anulacion, " +
				  "t.\"valor_ingreso\"-t.\"valor_anulacion\" as valor_neto "+
				   "FROM " +
				  "(SELECT administracion.getnomcentroatencion(f.centro_aten) AS centro_atencion, " +
				    "'grupo servicio'                          AS grupo          , " +
				    "historiaclinica.getnombregruposervicio(s.grupo_servicio)  AS nombre         , " +
				    "SUM(df.valor_total)                       AS valor_ingreso  , " +
				    "SUM(CASE WHEN af.codigo IS NULL THEN 0 ELSE df.valor_total END) AS valor_anulacion " +
				     "FROM facturacion.facturas f " +
				  "INNER JOIN facturacion.det_factura_solicitud df " +
				       "ON(df.factura = f.codigo) " +
				  "INNER JOIN facturacion.servicios s " +
				       "ON(s.codigo = df.servicio) " +
				  "LEFT OUTER JOIN facturacion.anulaciones_facturas af " +
				       "ON(af.codigo = f.codigo " +
				  "AND to_char(af.fecha_grabacion,'yyyy-mm-dd') BETWEEN '" + UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaInicial").toString()) + "' AND '" + UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaFinal").toString()) + "') " +
				    "WHERE to_char(f.fecha,'yyyy-mm-dd') BETWEEN '" + UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaInicial").toString()) + "' AND '" + UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaFinal").toString()) + "' " +
				 "GROUP BY f.centro_aten, " +
				    "s.grupo_servicio " +
				    "" +
				    "UNION " +
				    "" +
				   "SELECT administracion.getnomcentroatencion(f.centro_aten) AS centro_atencion, " +
				    "'clase inventario'                        AS grupo          , " +
				    "facturacion.getnombreclaseinventario(si.clase)        AS nombre         , " +
				    "SUM(df.valor_total)                       AS valor_ingreso  , " +
				    "SUM(CASE WHEN af.codigo IS NULL THEN 0 ELSE df.valor_total END) AS valor_anulacion " +
				     "FROM facturacion.facturas f " +
				  "INNER JOIN facturacion.det_factura_solicitud df " +
				       "ON(df.factura = f.codigo) " +
				  "INNER JOIN inventarios.articulo a " +
				       "ON(a.codigo = df.articulo) " +
				  "INNER JOIN inventarios.subgrupo_inventario si " +
				       "ON(si.codigo = a.subgrupo) " +
				  "LEFT OUTER JOIN facturacion.anulaciones_facturas af " +
				       "ON(af.codigo = f.codigo " +
				  "AND to_char(af.fecha_grabacion,'yyyy-mm-dd') BETWEEN '" + UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaInicial").toString()) + "' AND '" + UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaFinal").toString()) + "') " +
				    "WHERE to_char(f.fecha,'yyyy-mm-dd') BETWEEN '" + UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaInicial").toString()) + "' AND '" + UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaFinal").toString()) + "' " +
				 "GROUP BY f.centro_aten, " +
				    "si.clase " +
				  ") t " +
				"ORDER BY \"centro_atencion\", " +
				  "\"grupo\" DESC            , " +
				  "\"nombre\"";
			return consulta;
		}
		
		/**
		 * Metodo que retorna la sentencia SQL de facturas varias para crear el reporte de 
		 * movimientos totales por tipo de documento
		 * @param con
		 * @param criterios
		 * @return
		 */
		public static String listadoFacturasVarias(Connection con, HashMap criterios) 
		{
			String consulta = 
							" SELECT cfv.descripcion AS concepto, " +
							  "cfv.consecutivo       AS codigo_concepto, " +
							  "SUM(fv.valor_factura) AS valor_facturacion, " +
							  "SUM( " +
							  "CASE " +
							    "WHEN fv.estado_factura = 'ANU' " +
							    "AND to_char(fv.fecha_anulacion,'yyyy-mm-dd') BETWEEN '" + UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaInicial").toString()) + "' AND '" + UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaFinal").toString()) + "' " +
							    "THEN fv.valor_factura " +
							    "ELSE 0 " +
							  "END) AS valor_anulacion, " +
							  "(select " +
									"sum(case when af.tipo_ajuste = 'D' and af.valor_ajuste is not null then af.valor_ajuste else 0 end) as valor_ajuste_debito " +
									"from ajus_facturas_varias af " +
									"inner join facturas_varias fv on (fv.codigo_fac_var = af.factura) " + 
									"INNER JOIN conceptos_facturas_varias cfv   ON(cfv.consecutivo = fv.concepto ) " + 
									"WHERE af.estado = 'APR' and to_char(af.fecha_aprob_anul,'yyyy-mm-dd') between '" + UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaInicial").toString()) + "' " +
											"AND '" + UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaFinal").toString()) + "' " +
													"and cfv.consecutivo = cfv.consecutivo) AS valor_ajuste_debito, " +
									"(select  " +
									"sum(case when af.tipo_ajuste = 'C' and af.valor_ajuste is not null then af.valor_ajuste else 0 end) as valor_ajuste_credito " +
									"from ajus_facturas_varias af " +
									"inner join facturas_varias fv on (fv.codigo_fac_var = af.factura) " + 
									"INNER JOIN conceptos_facturas_varias cfv   ON(cfv.consecutivo = fv.concepto ) " + 
									"WHERE af.estado = 'APR' and to_char(af.fecha_aprob_anul,'yyyy-mm-dd') between '" + UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaInicial").toString()) + "' " +
											"AND '" + UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaFinal").toString()) + "' " +
													"and cfv.consecutivo = cfv.consecutivo) AS valor_ajuste_credito " +
							   "FROM facturasvarias.facturas_varias fv " +
							"INNER JOIN facturasvarias.conceptos_facturas_varias cfv " +
							     "ON(cfv.consecutivo = fv.concepto) " +
							  "WHERE to_char(fv.fecha,'yyyy-mm-dd') BETWEEN '" + UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaInicial").toString()) + "' AND '" + UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaFinal").toString()) + "' " +
							"AND (fv.estado_factura = 'APR' " +
							"OR (fv.estado_factura  = 'ANU' " +
							"AND fv.anula_fac_apro  = 'S')) " +
							"GROUP BY cfv.consecutivo, " +
							  "cfv.descripcion";
			return consulta;
		}
		
		/**
		 * Metodo que retorna la sentencia SQL de facturas pacientes para crear el reporte de 
		 * movimientos totales por tipo de documento
		 * @param con
		 * @param criterios
		 * @return
		 */
		public static String listadoRecibosCaja(Connection con, HashMap criterios) 
		{
			String consulta = " SELECT fp.descripcion AS forma_pago, " +
			  "SUM(dpr.valor)       AS valor_recibo, " +
			  "SUM( " +
			  "CASE " +
			    "WHEN arc.numero_recibo_caja IS NULL " +
			    "THEN 0 " +
			    "ELSE dpr.valor " +
			  "END) AS valor_anulacion " +
			   "FROM recibos_caja rc " +
			"INNER JOIN detalle_pagos_rc dpr " +
			     "ON(dpr.numero_recibo_caja = rc.numero_recibo_caja " +
			"AND dpr.institucion            = rc.institucion) " +
			"INNER JOIN formas_pago fp " +
			     "ON(fp.consecutivo = dpr.forma_pago) " +
			"LEFT OUTER JOIN anulacion_recibos_caja arc " +
			     "ON (arc.numero_recibo_caja = rc.numero_recibo_caja " +
			"AND arc.institucion             = rc.institucion " +
			"AND to_char(arc.fecha,'yyyy-mm-dd') BETWEEN '" + UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaInicial").toString()) + "' AND '" + UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaFinal").toString()) + "') " +
			  "WHERE to_char(rc.fecha,'yyyy-mm-dd') BETWEEN '" + UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaInicial").toString()) + "' AND '" + UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaFinal").toString()) + "' " +
			"GROUP BY fp.descripcion " +
			"ORDER BY fp.descripcion";
			
			return consulta;
		}
		
		/**
		 * Metodo que retorna la sentencia SQL para crear el reporte de 
		 * Impresion Glosa Factura
		 * @param codigoGlosa 
		 */
		
		public static String ImpresionGlosaFactura(Connection con, String codigoFactura, int codigoInstitucionInt, String codigoGlosa)
		{
			String consulta = "SELECT DISTINCT getnombreconvenio(f.convenio) AS nomconvenio, " +
									"f.consecutivo_factura AS codfactura, " +
									"ag.valor_glosa_factura AS valorglosafactura, " +
									"getconsecutivosolicitud(dag.solicitud) AS solicitud, " +
									"CASE WHEN dag.servicio IS NULL THEN dag.articulo || ' ' || getdescripcionarticulo(dag.articulo) " +
										"ELSE dag.servicio || ' ' || getnombreservicio(dag.servicio,"+ConstantesBD.codigoTarifarioCups+") " +
									"END AS servicio, " +
									"dfs.cantidad_cargo AS cantidadsol, " +
									"((coalesce(f.valor_total,0)-coalesce(f.ajustes_credito,0))+coalesce(f.ajustes_debito,0)) AS valorfactura, " +
									"dag.cantidad_glosa AS cantidadglosa, " +
									"dag.valor_glosa AS valorglosa, " +
									"getconceptosdetaudiglosas(cast(dag.codigo as INTEGER)) AS concepto, " +
									"rg.glosa_sistema AS preglosa, " +
									"rg.estado AS estado " +
								"FROM facturas f " +
									"INNER JOIN auditorias_glosas ag ON (f.codigo=ag.codigo_factura) " +
									"INNER JOIN registro_glosas rg ON (ag.glosa=rg.codigo) " +
									"INNER JOIN det_auditorias_glosas dag ON (dag.auditoria_glosa=ag.codigo) " +
									"INNER JOIN det_factura_solicitud dfs ON (dfs.factura=f.codigo) " +
								"WHERE " +
									"rg.codigo="+codigoGlosa+" ORDER BY getconsecutivosolicitud(dag.solicitud) ";
			return consulta;
		}
		
		/**
		 * Metodo encargado de organizar la consulta
		 * de datos gnerales del egreso para la
		 * impresion de la boleta de salida
		 * @param cuenta
		 * @return
		 */
		public static String consultaEgreso (String cuenta)
		{
			String consulta = " SELECT eg.usuario_responsable as usuarioResponsable," +
					" coalesce(ev.fecha_grabacion,eg.fecha_grabacion) as fechaGrabacion, " +
					" coalesce(ev.hora_grabacion,eg.hora_grabacion) as horaGrabacion," +
					" coalesce(ev.fecha_evolucion,eg.fecha_egreso) as fechaEvolucion, " +
					" coalesce(ev.hora_evolucion,eg.hora_egreso) as horaEvolucion, " +
					" autorizacion.numero_autorizacion as numeroAutorizacion, " +
					" CASE WHEN eg.estado_salida="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'Muerto' ELSE 'Vivo' END as estadoSalida, " +
					" eg.destino_salida as codigoDestinoSalida, " +
					" destsal.nombre as destinoSalida, " +
					" eg.otro_destino_salida as otroDestinoSalida, " +
					" eg.diagnostico_muerte as acroDiagM, " +
					" eg.diagnostico_muerte_cie as tipoCieDiagM, " +
					" diagm.nombre as diagM, " +
					" eg.causa_externa as codigoCausaExterna, " +
					" cauext.nombre as causaExterna, " +
					" eg.diagnostico_principal as acroDiagP, " +
					" eg.diagnostico_principal_cie as tipoCieDiagP, " +
					" diagp.nombre as diagP, " +
					" eg.diagnostico_relacionado1 as acroDiagR1, " +
					" eg.diagnostico_relacionado1_cie as tipoCieDiagR1, " +
					" diagr1.nombre as diagR1, " +
					" eg.diagnostico_relacionado2 as acroDiagR2, " +
					" eg.diagnostico_relacionado2_cie as  tipoCieDiagR2, " +
					" diagr2.nombre as diagR2, " +
					" eg.diagnostico_relacionado3 as acroDiagR3, " +
					" eg.diagnostico_relacionado3_cie as tipoCieDiagR3, " +
					" diagr3.nombre as diagR3, " +
					" per.tipo_identificacion as tipoIdMedico, " +
					" per.numero_identificacion as numIdMedico, " +
					//MT6253
					" coalesce(eg.acompanado_por,' ') AS acompanado_por6, " +
					" eg.remitido_a As remitido_a7, " +
					" eg.placa_nro As placa_nro8, " +
					" eg.conductor_ambulancia As conductor_ambulancia9, " +
					" eg.quien_recibe As quien_recibe10, " +
					" eg.observaciones As observaciones11, " +
					" administracion.getnombremedico(getpacientexingreso(getingresoxcuenta(eg.cuenta))) As nombre_paciente, " +
					" to_char(eg.fecha_egreso,'dd/mm/yyy') As fecha_egreso," +
					" eg.hora_egreso As hora_egreso, " +
					" getnombretipoidentificacion(gettipoid (getpacientexingreso(getingresoxcuenta(eg.cuenta)))) AS tipo_id_pac, " +
					" getidentificacionpaciente(getpacientexingreso(getingresoxcuenta(eg.cuenta))) AS id_pac," +
					" getedaddetallada(to_date(getfechanacimientopaciente(getpacientexingreso(getingresoxcuenta(eg.cuenta))),'dd-mm-yyyy'), current_date) As fecha, " +
					" getdescripcionsexo(getsexopaciente(getpacientexingreso(getingresoxcuenta(eg.cuenta)))) As sexo_pac," +
					" getconsecutivoingreso(getingresoxcuenta(eg.cuenta)) As ingreso, " +
					" to_char(getfechaingreso(eg.cuenta,getviaingresocuenta(eg.cuenta)),'dd/mm/yyyy') AS fecha_ingreso, " +
					" substr(gethoraingreso(eg.cuenta,getviaingresocuenta(eg.cuenta)),0,6) AS hora_ingreso, " +
					" getnombreviaingresotipopac(eg.cuenta) As via_ingreso, " +
					" getdatosmedicoespecialidades (eg.usuario_responsable,',') As datos_medico," +
					" getnombreusuario(eg.usuario_responsable) As usuario, "+ 
					//MT6365
					"administracion.getcargousuario(eg.usuario_responsable)  AS cargo"+
	" FROM egresos eg " +
	" INNER JOIN destinos_salida destsal ON (eg.destino_salida=destsal.codigo) " +
	" INNER JOIN diagnosticos diagm ON (eg.diagnostico_muerte=diagm.acronimo AND eg.diagnostico_muerte_cie=diagm.tipo_cie) " +
	" INNER JOIN causas_externas cauext ON (eg.causa_externa=cauext.codigo) " +
	" INNER JOIN diagnosticos diagp ON (eg.diagnostico_principal=diagp.acronimo AND eg.diagnostico_principal_cie=diagp.tipo_cie) " +
	" INNER JOIN diagnosticos diagr1 ON (diagr1.acronimo=eg.diagnostico_relacionado1 AND diagr1.tipo_cie=eg.diagnostico_relacionado1_cie) " +
	" INNER JOIN diagnosticos diagr2 ON (diagr2.acronimo=eg.diagnostico_relacionado2 and diagr2.tipo_cie=eg.diagnostico_relacionado2_cie) " +
	" INNER JOIN diagnosticos diagr3 ON (diagr3.acronimo=eg.diagnostico_relacionado3 and diagr3.tipo_cie=eg.diagnostico_relacionado3_cie) " +
	" INNER JOIN personas per ON (per.codigo=eg.codigo_medico) " +
	" LEFT OUTER JOIN evoluciones ev ON (ev.codigo=eg.evolucion) " +
	" INNER JOIN (select numero_autorizacion,cuenta from admisiones_hospi where cuenta="+cuenta+
	    		" UNION " +
	    		" select numero_autorizacion,cuenta from admisiones_urgencias where cuenta="+cuenta+
	   		" ) autorizacion ON (eg.cuenta=autorizacion.cuenta) " +
	   		" WHERE eg.cuenta="+cuenta;

			return consulta;
		}
		
		
		/**
		 * Metodo encargado de consulta la informacion
		 * de la facturas para imprimir la boleta de salida
		 * @param ingreso
		 * @return
		 */
		public static String consultaFacturas(String ingreso)
		{
			String consulta=" SELECT  coalesce(to_char(fecha_egreso,'dd/mm/yyy'),TO_CHAR(ci.fecha_cierre,'"+ConstantesBD.formatoFechaAp+"'),' ') As fecha_cierre_ingreso0, " +
								" coalesce(substr(hora_egreso,0,6),substr(ci.hora_cierre,0,6),' ') As hora_cierre_ingreso1, " +
								
								//Pendiente crear list en oracle
								//" list(f.consecutivo_factura||' - '||to_char(f.fecha,'dd/mm/yyy')) As facturas_fecha2, " +
								//" '' As facturas_fecha2, " +
								
								" f.consecutivo_factura||' - '||to_char(f.fecha,'dd/mm/yyy') As facturas_fecha2 " +
							" FROM " +
								"ingresos i " +
							" LEFT OUTER JOIN " +
								"facturas f ON (f.cod_paciente=i.codigo_paciente AND f.estado_facturacion!=2) " +
							" LEFT OUTER JOIN cierre_ingresos ci ON (ci.id_ingreso = i.id) "+
							" WHERE " +
								"i.id="+ingreso+" "+
							"GROUP BY " +
								"i.fecha_egreso, " +
								"ci.fecha_cierre, "+
								"i.hora_egreso, " +
								"ci.hora_cierre, "+
								"i.consecutivo," +
								"i.id, " +
								"f.consecutivo_factura||' - '||to_char(f.fecha,'dd/mm/yyy')";
			return consulta;
		}
	
		/**
		 * Metodo encargado de consulta la informacion
		 * de cuenta y cama del paciente
		 * @param ingreso
		 * @return
		 */
		public static String consultaSalida(String ingreso)
		{
			String consulta="     SELECT "+
						"COALESCE(manejopaciente.getcamacuentasalida(getcuentaxestado(i.id),getviaingresocuenta(getcuentaxestado(i.id))),' ') AS cama3, "+
						"manejopaciente.getcodigocamaxcuentasalida(getcuentaxestado(i.id),getviaingresocuenta(getcuentaxestado(i.id)))        AS codigo_cama4, "+
						"getcuentaxestado(i.id) AS codigo_cuenta12 "+
						"FROM ingresos i "+
						"WHERE i.id ="+ingreso;
			return consulta;
		}	
		
		/**
		 * Método que retorna la sentencia SQL para crear el reporte de 
		 * impresion respuesta de glosa 
		 * @param ingreso
		 * @return
		 */
		public static String impresionRespuestaGlosaStandar(String codigoRespuestaGlosa) {
			String consulta = "SELECT " +
									"frg.valor_fact_resp AS valorrespuestaglosa, " +
									"f.consecutivo_factura AS consecutivofactura, " +
									"f.fecha AS fechafactura, " +
									"coalesce(f.valor_total,0) + coalesce(f.ajustes_debito,0) - coalesce(f.ajustes_credito,0) + coalesce(cc.ajuste_debito,0) - coalesce(cc.ajuste_credito,0) - coalesce(cc.pagos,0) AS saldofactura, " +
									"ag.valor_glosa_factura AS valorglosa, " +
									"getcentroatencioncc(c.area) AS centroatencion, " +
									"getnombrepersona(c.codigo_paciente) AS nompaciente, " +
									"getidpaciente(c.codigo_paciente) AS idpaciente, " +
									"dag.solicitud AS solicitud, " +
									"CASE WHEN dag.servicio IS NOT NULL THEN dag.servicio||' '||getnombreservicio(dag.servicio, 0) ELSE getdescarticulo(dag.articulo) END AS descripcion, " +
									"getnomcentrocosto(s.centro_costo_solicitado) AS centrocosto, " +
									"getDescripcionConceptoGlosa(dfrg.concepto_glosa, dfrg.institucion) AS conceptoglosa, " +
									"dag.cantidad_glosa AS cantidadglosa, " +
									"dag.valor_glosa AS valorglosasolicitud, " +
									"getDescripcionConceptoGlosa(dfrg.concepto_respuesta, dfrg.institucion) AS conceptorespuesta, " +
									"dfrg.motivo AS motivo, " +
									"dfrg.cantidad_respuesta AS cantidadRespuesta, " +
									"dfrg.valor_respuesta AS valorrespuesta, " +
									"CASE WHEN ae.valor_ajuste!="+ConstantesBD.codigoEstadoCarteraAnulado+" THEN ae.valor_ajuste||'' ELSE '' END AS valorajuste " +
								"FROM " +
									"fact_respuesta_glosa frg " +
								"INNER JOIN " +
									"facturas f ON (f.codigo=frg.factura) " +
								"INNER JOIN " +
									"cuentas c ON (c.id = f.cuenta) " +
								"INNER JOIN " +
									"auditorias_glosas ag ON (ag.codigo=frg.auditoria_glosa) " +
								"LEFT OUTER JOIN " +
									"cuentas_cobro cc ON (cc.numero_cuenta_cobro=f.numero_cuenta_cobro) " +
								"LEFT OUTER JOIN " +
									"det_fact_resp_glosa dfrg ON (dfrg.fact_respuesta_glosa=frg.codigo) " +
								"LEFT OUTER JOIN " +
									"det_auditorias_glosas dag ON (dag.codigo=dfrg.det_auditoria_glosa) " +
								"LEFT OUTER JOIN " +
									"solicitudes s ON (s.numero_solicitud=dag.solicitud) " +
								"LEFT OUTER JOIN " +
									"ajustes_empresa ae ON (ae.codigo=dfrg.ajuste) " +
								"WHERE " +
									"frg.respuesta_glosa="+codigoRespuestaGlosa;
			return consulta;
		}
		
		public static String consultarDetalleFacturasGlosa(String codigoGlosa,int codigoInstitucion)
		{
			String consulta= "SELECT " + 
								"ag.codigo AS codauditoriaglosa, " +
								"ag.fecha_elaboracion_fact AS fechaelaboracion, " +
								"ag.numero_cuenta_cobro AS cuentacobro, " +
								"ag.fecha_radicacion_cxc AS fecharadicacion, " +
								"ag.saldo_factura AS saldofactura, " +
								"getConceptosAudiGlosas(ag.codigo, "+codigoInstitucion+", '\n') AS conceptos, " +
								"ag.valor_glosa_factura AS valorglosa, " +
								"coalesce(ag.fue_auditada,' ') AS indicativo, " +
								"f.consecutivo_factura AS factura " +
							"FROM " +
								"auditorias_glosas ag " +
							"INNER JOIN " +
								"facturas f ON (f.codigo = ag.codigo_factura) " +
							"WHERE " +
								"ag.glosa="+codigoGlosa;
			return consulta;
		}
		
		public static String impresionInfoPacienteMedicamentosControlEspecial(int codigoPersona)
		{
			String consulta= 	"SELECT " +
									"pe.numero_identificacion as nroid, " +
									"pe.tipo_identificacion as tipoid, " +
									"getnombrepersona(pe.codigo) AS nompersona, " +
									"getedad2(pe.fecha_nacimiento,CURRENT_DATE) as edad, " +
									"se.nombre AS sexo, " +
									"pe.direccion AS direccion, " +
									"pe.telefono, " +
									"ci.descripcion AS ciudad, d.descripcion AS departamento, " +
									"co.nombre AS convenio, tr.nombre AS tiporegimen " +
								"FROM " +
									"personas pe " +
								"INNER JOIN " +
									"ciudades ci ON (ci.codigo_ciudad=pe.codigo_ciudad_vivienda) " +
								"INNER JOIN " +
									"departamentos d ON (d.codigo_departamento=pe.codigo_departamento_vivienda) " +
								"INNER JOIN " +
									"ingresos i ON (i.codigo_paciente=pe.codigo) " +
								"INNER JOIN " +
									"sub_cuentas sc ON (sc.ingreso=i.id) " +
								"INNER JOIN " +
									"convenios co ON (co.codigo=sc.convenio) " +
								"INNER JOIN " +
									"sexo se ON (se.codigo=pe.sexo) " +
								"INNER JOIN " +
									"tipos_regimen tr ON (tr.acronimo=co.tipo_regimen) " +
								"WHERE " +
									"pe.codigo="+codigoPersona+" "+
								"AND " +
									"ci.codigo_departamento=pe.codigo_departamento_vivienda " +
								"AND " +
									"sc.nro_prioridad=1";
			return consulta;
		}
		
		public static String impresionInfoOrden(String nroOrden)
		{
			String consulta=	"SELECT " +
									"a.codigo, " +
									"a.descripcion, " +
									"a.concentracion," +
									"ff.nombre AS formafarma," +
									"dtaa.dosis, va.nombre AS via, " +
									"dtaa.cantidad AS cantidad, " +
									"dtaa.observaciones AS indicaciones, " +
									"dtaa.duracion_tratamiento AS duracion, " +
									"tf.nombre AS tipofrecuencia " +
								"FROM " +
									"det_orden_amb_articulo dtaa " +
								"INNER JOIN " +
									"articulo a ON (a.codigo=dtaa.articulo) " +
								"INNER JOIN " +
									"ordenes_ambulatorias oa ON (oa.codigo=dtaa.codigo_orden) " +
								"INNER JOIN " +
									"vias_administracion va ON (va.codigo=dtaa.via) " +
								"INNER JOIN " +
									"tipos_frecuencia tf ON (tf.codigo=dtaa.tipo_frecuencia) " +
								"INNER JOIN " +
									"forma_farmaceutica ff ON (a.forma_farmaceutica=ff.acronimo) " +
								"WHERE " +
									"oa.consecutivo_orden='"+nroOrden+"'";
			return consulta;
		}
		
		public static String impresionInfoMedico (String idMedico)
		{
			String consulta=	"SELECT " +
									"getnombrepersona(pe.codigo) AS nombremedico, " +
									"pe.tipo_identificacion AS tipoidmedico, " +
									"pe.numero_identificacion AS nroidmedico, " +
									"m.numero_registro AS numregistro, " +
									"getespecialidadesmedico1(pe.codigo,', ') AS especialidades, " +
									"'"+ValoresPorDefecto.getFilePath()+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+"' ||"+
									"m.firma_digital AS firma FROM medicos m " +
								"INNER JOIN " +
									"personas pe ON (pe.codigo=m.codigo_medico) " +
								"WHERE " +
									"m.codigo_medico="+idMedico;
			
			return consulta;
		}
		
		public static String consultarPazYSalvo(String codPyS)
		{
			String consulta= "SELECT ps.consecutivo AS conspys, " +
							"ps.fecha_modifica AS fecha, "+
							"ps.hora_modifica AS hora, "+ 
							"dg.consecutivo AS codgarantia, "+ 																				
							"dg.valor AS valor, "+ 
							"carterapaciente.getnomdeudoringreso(d.ingreso) AS deudor, "+ 
							"d.numero_identificacion AS iddeudor, "+ 
							"administracion.getnombrepersona(p.codigo) AS paciente, "+ 
							"p.numero_identificacion AS idpaciente, " +
							"f.consecutivo_factura AS consecutivo_factura, " +
							"getnombreusuario(ps.usuario_modifica) AS usuario_modifica, " +
							"carterapaciente.getreciboscajadocgarantia(df.codigo_pk,',',"+ConstantesBD.codigoTipoDocumentoPagosReciboCaja+") AS recibo_caja, "+ 
							"getintegridaddominio(dg.tipo_documento) AS tipo_documento " +
							"FROM carterapaciente.paz_y_salvo ps " +
							"INNER JOIN carterapaciente.datos_financiacion df ON(ps.datos_financiacion=df.codigo_pk) " +
							"INNER JOIN carterapaciente.documentos_garantia dg ON(df.codigo_pk_docgarantia=dg.codigo_pk) " +
							"INNER JOIN carterapaciente.deudores_datos_finan ddf ON(ddf.datos_financiacion=df.codigo_pk) " +
							"INNER JOIN carterapaciente.deudorco d ON(ddf.codigo_pk_deudor=d.codigo_pk) " +
							"INNER JOIN personas p ON(d.codigo_paciente=p.codigo) " +
							"INNER JOIN facturas f ON(df.codigo_factura=f.codigo) " +
							"WHERE ps.codigo_pk="+codPyS+"";
						
			return consulta;
		}
		
		public static String consultarDocumentosCarteraPaciente(DtoDocumentosGarantia dto)
		{
			String consulta	=	"SELECT " +
										"dg.ingreso AS ingresodocumento," +
										"dg.consecutivo AS codigogarantia," +
										"dg.anio_consecutivo AS aniodocumento," +
										"getintegridaddominio(dg.tipo_documento) AS tipodocumento," +
										"to_char(dg.fecha_generacion,'dd/mm/yyyy') AS fechagendocumento," +
										"coalesce(dg.valor,0) AS valordocumento," +
										"getintegridaddominio(dg.estado) AS estado, " +
										"f.consecutivo_factura AS consecutivofactura," +
										"d.tipo_identificacion AS tipoiddeudor," +
										"d.numero_identificacion AS numiddeudor, " +
										"coalesce(d.primer_nombre,' ') AS primernomdeudor," +
										"coalesce(d.segundo_nombre,' ') AS segundonomdeudor," +
										"coalesce(d.primer_apellido,' ') AS primerapedeudor," +
										"coalesce(d.segundo_apellido,' ') AS segundoapedeudor," +
										"getnombrepersona(d.codigo_paciente) AS nombrepaciente, " +
										"d.telefono_reside AS telefonodeudor," +
										"d.direccion_reside AS direcciondeudor," +
										"p.tipo_identificacion AS tipoidpaciente," +
										"p.numero_identificacion AS numidpaciente," +
										"dcrc.numero_recibo_caja AS nrorc, " +
										"(df.dias_por_couta*df.nro_coutas) AS diasvigenciadocumento," +
										"to_char((dg.fecha_generacion+(df.dias_por_couta * df.nro_coutas)),'dd/mm/yyyy') AS fechavencimiento,  " +
										"coalesce(dg.valor-coalesce((" +
										"SELECT " +
											"SUM(apcp.valor)" +
										"FROM " +
											"carterapaciente.aplicac_pagos_cartera_pac apcp " +
										"INNER JOIN " +
											"carterapaciente.datos_financiacion df ON(df.codigo_pk =apcp.datos_financiacion) " +
										"INNER JOIN " +
											"carterapaciente.deudores_datos_finan ddf ON(ddf.datos_financiacion=df.codigo_pk) " +
										"WHERE " +
											"ddf.num_id_deudor=d.numero_identificacion" +
										"),0),0) AS saldodocumento," +
										"i.centro_atencion AS codigocentroatencion," +
										"ca.descripcion AS nombrecentroatencion " +
									"FROM " +
										"carterapaciente.deudorco d " +
									"INNER JOIN " +
										"administracion.personas p ON (p.codigo=d.codigo_paciente) " +
									"INNER JOIN " +
										"manejopaciente.ingresos i ON (i.id=d.ingreso) " +
									"INNER JOIN " +
										"administracion.centro_atencion ca ON (ca.consecutivo=i.centro_atencion) " +
									"INNER JOIN " +
										"carterapaciente.deudores_datos_finan ddf ON (ddf.codigo_pk_deudor=d.codigo_pk) " +
									"INNER JOIN " +
										"carterapaciente.datos_financiacion df ON (df.codigo_pk=ddf.datos_financiacion) " +
									"INNER JOIN " +
										"carterapaciente.documentos_garantia dg ON (df.codigo_pk_docgarantia=dg.codigo_pk) " +
									"INNER JOIN " +
										"facturacion.facturas f ON (f.codigo=df.codigo_factura) " +
									"INNER JOIN " +
										"tesoreria.detalle_conceptos_rc dcrc ON (dcrc.ingreso=d.ingreso AND dcrc.inst_deudor=d.institucion AND dcrc.clase_deudorco=d.clase_deudorco AND dcrc.num_id_deudorco=d.numero_identificacion) ";
			
			consulta+=	"WHERE ";
			
			if (!dto.getCentroAtencion().equals(""))
				consulta+="	i.centro_atencion="+dto.getCentroAtencion()+" AND ";
			
			if (!dto.getFechaGen().equals("")&&!dto.getFechaGenFinal().equals(""))
				consulta+="	(to_char(dg.fecha_generacion,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(dto.getFechaGen())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(dto.getFechaGenFinal())+"') AND ";
			
			if (!dto.getTipoDocumento().equals(""))
				consulta+="	dg.tipo_documento='"+dto.getTipoDocumento()+"' AND ";
			
			if (!dto.getEstado().equals(""))
				consulta+="	dg.estado='"+dto.getEstado()+"' AND ";

			consulta+=" 1=1";
			
			return consulta;
			
			
		}
		
		public static String consultarExtractosDeudor(DtoDeudor dto)
		{
			String consulta	=						"SELECT DISTINCT " +
														 " d.institucion                			   ,"+
														 " d.tipo_identificacion    AS tipoiddeudor    ,"+
														 " d.numero_identificacion  AS numiddeudor     ,"+
														 " coalesce(d.primer_nombre,' ')          AS primernomdeudor ,"+
														 " coalesce(d.segundo_nombre,' ')         AS segundonomdeudor,"+
														 " coalesce(d.primer_apellido,' ')        AS primerapedeudor ,"+
														 " coalesce(d.segundo_apellido,' ')       AS segundoapedeudor,"+
														 " d.ingreso                AS ingresodeudor   ,"+
														 " ps.tipo_identificacion   AS tipoidpac       ,"+
														 " ps.numero_identificacion AS numidpac        ,"+
														 " coalesce(ps.primer_nombre,' ')         AS primernompac    ,"+
														 " coalesce(ps.segundo_nombre,' ')        AS segundonompac   ,"+
														 " coalesce(ps.primer_apellido,' ')       AS primerapepac    ,"+
														 " coalesce(ps.segundo_apellido,' ')      AS segundoapepac   ,"+
														 " ca.consecutivo           AS centroatencion  ," +
														 " ca.descripcion           AS nombrecentro    ,"+
														 " df.codigo_pk             AS codigodatofin   ,"+
														 " df.codigo_factura        AS codigofac       ,"+
														 " f.consecutivo_factura    AS consecutivofac  ,"+
														 " dg.ingreso               AS ingresodg       ,"+
														 " dg.consecutivo           AS consecutivodg   ,"+
														 " dg.anio_consecutivo      AS aniodg          ,"+
														 " getintegridaddominio(dg.tipo_documento)        AS tipodocdg,"+
														 " dg.estado                AS estadodg		   ,"+
														 " dg.valor AS valordocgarantia                ,"+
														 " to_char(dg.fecha_generacion,'dd/mm/yyyy') AS fechamovdoc,"+
														 " ap.valor AS valoraplicacion,"+
														 " to_char(ap.fecha_aplicacion,'dd/mm/yyyy') AS fechaplicacion," +
														 " ap.consecutivo AS consecutivoap," +
														 " ap.numero_documento AS rc "+
														 " FROM deudorco d "+
														 " INNER JOIN personas ps ON (d.codigo_paciente=ps.codigo) "+
														 " INNER JOIN ingresos i ON (i.id=d.ingreso) "+
														 " INNER JOIN centro_atencion ca ON (ca.consecutivo=i.centro_atencion) "+
														 " INNER JOIN deudores_datos_finan ddf ON (ddf.codigo_pk_deudor=d.codigo_pk) "+
														 " INNER JOIN datos_financiacion df ON (df.codigo_pk=ddf.datos_financiacion) "+
														 " INNER JOIN documentos_garantia dg ON (dg.codigo_pk_docgarantia=dg.codigo_pk) "+
														 " INNER JOIN facturas f ON (f.codigo  =df.codigo_factura) "+
														 " INNER JOIN aplicac_pagos_cartera_pac ap ON (ap.datos_financiacion=df.codigo_pk) ";
			
			consulta+="	WHERE ";
			
			if (!dto.getNumeroIdentificacionDeu().equals(""))
				consulta+=" d.numero_identificacion='"+dto.getNumeroIdentificacionDeu()+"' AND ";
			
			if (!dto.getDtoDocsGarantia().getConsecutivo().equals(""))
				consulta+=	" dg.estado='"+dto.getDtoDocsGarantia().getEstado()+"' AND ";
			else
				consulta+=	" dg.estado IN ('"+ConstantesIntegridadDominio.acronimoEstadoEntregado+"','"+ConstantesIntegridadDominio.acronimoEstadoCancelado+"') AND ";
			
			consulta+=" 1=1";
			
			consulta+=" ORDER BY f.consecutivo_factura,d.numero_identificacion, dg.consecutivo ";
			
			return consulta;
		}
		
		public static String reporteFacturasReiteradas(String fechaCorte,String tipoConvenio, int convenio, int institucion)
		{			
			String consultaEnviar="SELECT "+
							       	"gettipoconvenio(f.convenio) AS tipoconv, " +
							       	"tc.descripcion AS desctipoconv, "+
								 	"f.consecutivo_factura AS consecutivofactura, "+
									  "getnombreconvenio(f.convenio) AS nomconvenio, "+
									  "to_char (f.fecha,'dd/mm/yyyy') AS fechaelaboracion, "+
									  "to_char (cc.fecha_radicacion,'dd/mm/yyyy') AS fecharadicacion, "+
									  "f.valor_bruto_pac AS valorpaciente, "+
									  "f.valor_convenio AS valorconvenio, "+
									  "f.valor_total AS valortotal, "+
									  "rg.glosa_sistema AS glosasis, "+
									  "rg.glosa_entidad AS glosaent, "+
									  "to_char (rg.fecha_notificacion,'dd/mm/yyyy') AS fechanot, "+
									  "ag.valor_glosa_factura AS valorglosafact, "+
									  "to_char (re.fecha_radicacion,'dd/mm/yyyy') AS fecharesp "+
									"FROM facturas f " +
									"INNER JOIN convenios c ON(f.convenio=c.codigo) "+
									"INNER JOIN cuentas_cobro cc ON(cc.numero_cuenta_cobro=f.numero_cuenta_cobro) "+
									"INNER JOIN auditorias_glosas ag ON(f.codigo=ag.codigo_factura) "+
									"INNER JOIN registro_glosas rg ON(rg.codigo=ag.glosa) " +
									"INNER JOIN tipos_convenio tc ON(tc.codigo=c.tipo_convenio AND tc.institucion=c.institucion) "+
									"LEFT OUTER JOIN respuesta_glosa re ON(re.glosa=rg.codigo) "+
									"WHERE ag.reiterada='S' "; 
						
			if(!tipoConvenio.equals("-1"))
			consultaEnviar += "AND c.tipo_convenio= '"+tipoConvenio+"' ";
			if(convenio > 0)
			consultaEnviar += "AND f.convenio= "+convenio+" ";
			
			
			consultaEnviar += "ORDER BY tc.descripcion, f.consecutivo_factura ";
			
			return consultaEnviar; 
		}

		public static String reporteFacturacionEventoRadicar(String centroAtencion, String convenio, String fechaElabIni,String fechaElabFin, String factIni, String factFin,String viaIngreso, int consulta, String validacion) 
		{
			String consultaEnviar="SELECT f.consecutivo_factura AS consecutivofactura, "+
								       "f.fecha AS fechaelaboracion, "+
								       "getnombreconvenio(f.convenio) AS nomconvenio, "+
								       "vi.nombre AS idviaingreso, "+
								       "getnomcentroatencion(f.centro_aten) AS centroatencion, "+
								       "getidpaciente(f.cod_paciente) AS idpaciente, "+
								       "getnombrepersona2(f.cod_paciente) AS nombrepaciente, "+
								       "f.valor_total AS valortotalfact, ";
						
		if(consulta == 1)
			consultaEnviar += "f.valor_convenio AS valorconvfact ";
								       
		if(consulta == 2)
			consultaEnviar += "f.valor_convenio AS valorconvfact, "+ 
							  "f.numero_cuenta_cobro AS numcuentacobro, "+
							  "current_date - f.fecha AS diassinradicar ";
				
			consultaEnviar += "FROM facturas f "+
								"INNER JOIN vias_ingreso vi ON(f.via_ingreso= vi.codigo) ";
			
		if(consulta == 2)	
			consultaEnviar += "INNER JOIN cuentas_cobro cc ON(cc.numero_cuenta_cobro=f.numero_cuenta_cobro) ";
								
			consultaEnviar += "WHERE f.estado_facturacion = 1 "; 

		if(consulta == 2)
			consultaEnviar += "AND cc.estado "+validacion+" 4 ";
		
		if(consulta == 1)
			consultaEnviar += "AND f.numero_cuenta_cobro is null ";
						
		if(!centroAtencion.equals(""))
			consultaEnviar += "AND f.centro_aten= '"+centroAtencion+"' ";
		if(!convenio.equals("-1"))
			consultaEnviar += "AND f.convenio= "+convenio+" ";
		if(!viaIngreso.equals("-1"))
			consultaEnviar += "AND f.via_ingreso= "+viaIngreso+" ";
		if(!fechaElabIni.equals("") && !fechaElabFin.equals(""))
			consultaEnviar += "AND (to_char(f.fecha,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaElabIni)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaElabFin)+"') ";
		if(!factIni.equals("") && !factFin.equals(""))
			consultaEnviar += "AND  f.consecutivo_factura between "+factIni+" AND "+factFin+" ";
		return consultaEnviar; 
	}
		
	public static String EdadGlosaFechaRadicacion(HashMap criterios, String validacion)
	{
		String fecha=UtilidadFecha.getFechaActual();
		String consultaEnviar= "";
		
		if(criterios.get("consulta").equals("1"))
		{
			consultaEnviar += "SELECT DISTINCT getnombreconvenio(rg.convenio) AS nomconvenio, "+
								        "tc.descripcion AS desctipoconvenio, " +
								        "'' AS label , "+
								        "(SELECT sum(rg.valor_glosa) "+
								         "FROM registro_glosas rg "+ 
								            "LEFT OUTER JOIN respuesta_glosa re ON(re.glosa=rg.codigo) "+ 
								          "WHERE rg.estado='APRO' "+ 
								            "AND re.estado "+validacion+" 'RADIC') AS porradicar, ";
		}
		else if(criterios.get("consulta").equals("2"))
		{
			consultaEnviar += "SELECT DISTINCT getnombreconvenio(rg.convenio) AS "+"desctipoconvenio"+", "+
						       "f.consecutivo_factura AS "+"nomconvenio"+", "+
						       "'Factura' AS label , "+
						       "(SELECT sum(ag.valor_glosa_factura) "+
						        "FROM registro_glosas rg "+ 
						          "LEFT OUTER JOIN respuesta_glosa re ON(re.glosa=rg.codigo) "+ 
						          "INNER JOIN auditorias_glosas ag ON(ag.glosa=rg.codigo) "+ 
						        "WHERE rg.estado='APRO' "+ 
						          "AND re.estado "+validacion+" 'RADIC') AS "+"porradicar"+", ";
		}
		else if(criterios.get("consulta").equals("3"))
		{
			consultaEnviar += "SELECT DISTINCT getnombreconvenio(rg.convenio) AS "+"desctipoconvenio"+", "+
						       "f.numero_cuenta_cobro AS "+"nomconvenio"+", "+
						       "'Cuenta Cobro' AS label , "+
						       "(SELECT sum(ag.valor_glosa_factura) "+
						        "FROM registro_glosas rg "+ 
						          "LEFT OUTER JOIN respuesta_glosa re ON(re.glosa=rg.codigo) "+ 
						          "INNER JOIN auditorias_glosas ag ON(ag.glosa=rg.codigo) "+ 
						        "WHERE rg.estado='APRO' "+ 
						          "AND re.estado "+validacion+" 'RADIC') AS "+"porradicar"+", ";
		}					            
							            
		consultaEnviar += "(SELECT "+ 
				          "sum(CASE WHEN (current_date-re.fecha_radicacion) = 0 THEN re.valor_respuesta ELSE 0 END) "+
				           "FROM respuesta_glosa re "+ 
				            "INNER JOIN registro_glosas rg ON(re.glosa=rg.codigo) "+             
				          "WHERE re.estado='RADIC')  AS "+"edadvalor1"+", "+      
				          "(SELECT "+ 
				          "sum(CASE WHEN (current_date-re.fecha_radicacion) BETWEEN 1 AND 30 THEN re.valor_respuesta ELSE 0 END) "+
				           "FROM respuesta_glosa re "+ 
				            "INNER JOIN registro_glosas rg ON(re.glosa=rg.codigo) "+             
				          "WHERE re.estado='RADIC')  AS "+"edadvalor2"+", "+
				          "(SELECT "+ 
				          "sum(CASE WHEN (current_date-re.fecha_radicacion) BETWEEN 31 AND 60 THEN re.valor_respuesta ELSE 0 END) "+
				           "FROM respuesta_glosa re "+  
				            "INNER JOIN registro_glosas rg ON(re.glosa=rg.codigo) "+             
				          "WHERE re.estado='RADIC')  AS "+"edadvalor3"+", "+   
				          "(SELECT "+ 
				          "sum(CASE WHEN (current_date-re.fecha_radicacion) BETWEEN 61 AND 90 THEN re.valor_respuesta ELSE 0 END) "+
				          "FROM respuesta_glosa re "+ 
				            "INNER JOIN registro_glosas rg ON(re.glosa=rg.codigo) "+             
				          "WHERE re.estado='RADIC')  AS "+"edadvalor4"+", "+
				          "(SELECT "+
				          "sum(CASE WHEN (current_date-re.fecha_radicacion) BETWEEN 91 AND 120 THEN re.valor_respuesta ELSE 0 END) "+
				          "FROM respuesta_glosa re "+ 
				            "INNER JOIN registro_glosas rg ON(re.glosa=rg.codigo) "+             
				          "WHERE re.estado='RADIC')  AS "+"edadvalor5"+", "+
				          "(SELECT "+
				          "sum(CASE WHEN (current_date-re.fecha_radicacion) BETWEEN 121 AND 150 THEN re.valor_respuesta ELSE 0 END) "+
				          "FROM respuesta_glosa re "+ 
				            "INNER JOIN registro_glosas rg ON(re.glosa=rg.codigo) "+             
				          "WHERE re.estado='RADIC')  AS "+"edadvalor6"+", "+
				          "(SELECT "+
				          "sum(CASE WHEN (current_date-re.fecha_radicacion) BETWEEN 151 AND 180 THEN re.valor_respuesta ELSE 0 END) "+
				          "FROM respuesta_glosa re "+ 
				            "INNER JOIN registro_glosas rg ON(re.glosa=rg.codigo) "+             
				          "WHERE re.estado='RADIC')  AS "+"edadvalor7"+", "+
				          "(SELECT "+
				          "sum(CASE WHEN (current_date-re.fecha_radicacion) BETWEEN 181 AND 300 THEN re.valor_respuesta ELSE 0 END) "+
				          "FROM respuesta_glosa re "+ 
				            "INNER JOIN registro_glosas rg ON(re.glosa=rg.codigo) "+             
				          "WHERE re.estado='RADIC')  AS "+"edadvalor8"+", "+
				          "(SELECT "+
				          "sum(CASE WHEN (current_date-re.fecha_radicacion) > 300 THEN re.valor_respuesta ELSE 0 END) "+ 
				          "FROM respuesta_glosa re "+ 
				            "INNER JOIN registro_glosas rg ON(re.glosa=rg.codigo) "+             
				          "WHERE re.estado='RADIC')  AS "+"edadvalor9"+" ";   
						
		if(criterios.get("consulta").equals("1"))
		{				          
			consultaEnviar += "FROM registro_glosas rg "+
							"INNER JOIN convenios c ON(c.codigo=rg.convenio) "+
							"INNER JOIN tipos_convenio tc ON(c.tipo_convenio= tc.codigo AND c.institucion= tc.institucion) " +
							"WHERE (to_char(rg.fecha_registro_glosa,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fecha")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fecha)+"') ";
			if(!criterios.get("tipoConvenio").equals(""))
				consultaEnviar += "AND c.tipo_convenio="+Utilidades.convertirAEntero(criterios.get("tipoConvenio")+"")+" ";
			if(!criterios.get("convenio").equals(""))
				consultaEnviar += "AND c.codigo= "+Utilidades.convertirAEntero(criterios.get("convenio")+"")+" ";
			
			consultaEnviar += "ORDER BY tc.descripcion, getnombreconvenio(rg.convenio) ";
		}
		else if(criterios.get("consulta").equals("2"))
		{
			consultaEnviar += "FROM registro_glosas rg "+
								"INNER JOIN convenios c ON(c.codigo=rg.convenio) "+
								"INNER JOIN auditorias_glosas ag ON(ag.glosa=rg.codigo) "+
								"INNER JOIN facturas f ON(ag.codigo_factura=f.codigo) "+
								"WHERE (to_char(rg.fecha_registro_glosa,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fecha")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fecha)+"') ";
			if(!criterios.get("tipoConvenio").equals(""))
				consultaEnviar += "AND c.tipo_convenio="+Utilidades.convertirAEntero(criterios.get("tipoConvenio")+"")+" ";
			if(!criterios.get("convenio").equals(""))
				consultaEnviar += "AND c.codigo= "+Utilidades.convertirAEntero(criterios.get("convenio")+"")+" ";
			
			consultaEnviar += "ORDER BY getnombreconvenio(rg.convenio),f.consecutivo_factura ";
		}
		else if(criterios.get("consulta").equals("3"))
		{
			consultaEnviar += "FROM registro_glosas rg "+
								"INNER JOIN convenios c ON(c.codigo=rg.convenio) "+
								"INNER JOIN auditorias_glosas ag ON(ag.glosa=rg.codigo) "+
								"INNER JOIN facturas f ON(ag.codigo_factura=f.codigo) "+
								"WHERE (to_char(rg.fecha_registro_glosa,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fecha")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fecha)+"') ";
			if(!criterios.get("tipoConvenio").equals(""))
				consultaEnviar += "AND c.tipo_convenio="+Utilidades.convertirAEntero(criterios.get("tipoConvenio")+"")+" ";
			if(!criterios.get("convenio").equals(""))
				consultaEnviar += "AND c.codigo= "+Utilidades.convertirAEntero(criterios.get("convenio")+"")+" ";
								
			consultaEnviar += "ORDER BY getnombreconvenio(rg.convenio),f.numero_cuenta_cobro ";
		}
		
		return consultaEnviar;
	}
	
	public static String ReporteFacturasVencidasNoObjetadas(HashMap criterios)
	{
		String fecha=UtilidadFecha.getFechaActual();
		String consultaEnviar= "";
		
		consultaEnviar =  "SELECT getdescripciontipoconvenio(f.convenio) AS tipoconvenio, "+
					        "f.consecutivo_factura AS consecutivofact, "+
					        "f.fecha AS fechaelabfact, "+
					        "cc.fecha_radicacion AS fecharadcc, "+
					        "getnombreconvenio(f.convenio) AS nomconvenio, "+
					        "getnombrepersona2(f.cod_paciente) AS nompaciente, "+
					        "f.valor_convenio AS valorconv, "+
					        "f.valor_bruto_pac AS valorbrutopac, "+
					        "coalesce(f.valor_convenio,0) + coalesce(f.ajustes_debito,0) - coalesce(f.ajustes_credito,0) - coalesce(cc.pagos,0) AS saldofact "+        
					"FROM facturas f " +
						"INNER JOIN convenios c ON(f.convenio=c.codigo)"+ 
					    "INNER JOIN cuentas_cobro cc ON(f.numero_cuenta_cobro=cc.numero_cuenta_cobro) "+
					    "LEFT OUTER JOIN auditorias_glosas ag ON(ag.codigo_factura=f.codigo) "+
					    "LEFT OUTER JOIN registro_glosas rg ON(rg.codigo= ag.glosa) "+
					"WHERE (coalesce(f.valor_convenio,0) + coalesce(f.ajustes_debito,0) - coalesce(f.ajustes_credito,0) - coalesce(cc.pagos,0)) > 0 "+
					    "AND rg.estado='"+ConstantesIntegridadDominio.acronimoEstadoGlosaRegistrada+"' "+ 
					    "AND ag.fue_auditada='GL' " +
					    "AND (to_char(f.fecha,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fecha")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fecha)+"') " +
					    "AND (CURRENT_DATE - cc.fecha_radicacion) >= "+criterios.get("parametro")+" "; 
				
		if(!(criterios.get("convenio")+"").equals("-1"))
			consultaEnviar += "AND f.convenio= "+criterios.get("convenio")+" ";
		
		if(!(criterios.get("tipoConvenio")+"").equals("-1"))
			consultaEnviar += "AND c.tipo_convenio= "+criterios.get("tipoConvenio")+" ";
		
		consultaEnviar += "ORDER BY getdescripciontipoconvenio(f.convenio), f.consecutivo_factura";
		
		return consultaEnviar;
	}
	
	/**
	 * Metodo para obtener la consulta de la carta de instrucciones de garantea
	 * @param idIngreso
	 * @return
	 */
	public static String cartaInstruccionesGarantia(String idIngreso)
	{
		String consulta = "";
		
		// consulta utilizada en el reporte de Carta de instrucciones  anexa a titulos valores
		//@parametros ingreso
		consulta += "SELECT "+ 
		//--*************DATOS PAGARE******************************************************************************************
			"dg.consecutivo || CASE WHEN dg.anio_consecutivo = '' then '' ELSE ' - ' || dg.anio_consecutivo END AS consecutivo, "+
			"to_char(dg.fecha_generacion,'DD') AS diagene, "+
		 	"getnombremes(to_char(dg.fecha_generacion,'MM')) AS mesgene , "+
			"to_char(dg.fecha_generacion,'YYYY') as aniogene, "+
		//--********************END DATOS***************************************************************************************

		//--****************DATOS PACIENTE**************************************************************************************
		
			"getnombremedico (p.codigo) AS nombrePacientepc, "+

		//--********************	END DATOS*************************************************************************************

		//--********************DATOS DEUDOR ***********************************************************************************
			"deu.primer_nombre || ' ' || coalesce(deu.segundo_nombre,'') ||' '||deu.primer_apellido|| ' ' || coalesce(deu.segundo_apellido,'') AS nombreDeu, "+
			"deu.tipo_identificacion AS acronimoiddeu, "+
			"deu.numero_identificacion AS identificacionDeu, "+
			"getnombreciudad(deu.codigo_pais,deu.codigo_departamento,deu.codigo_ciudad) AS nombreCidudadIdDeu, "+
			"deu.telefono_oficina AS telefonoOficinaDeu, "+
			"deu.telefono_reside AS telefonoResideDeu, "+



		//--********************END  DATOS **************************************************************************************

		//--********************DATOS CODEUDOR ************************************************************************************
			"CASE WHEN cdeu.primer_nombre = '' THEN '' ELSE coalesce(cdeu.primer_nombre,'') || ' ' || coalesce(cdeu.segundo_nombre,'') ||' '||coalesce(cdeu.primer_apellido,'')|| ' ' || coalesce(cdeu.segundo_apellido,'') END AS nombreCdeu, "+

			"CASE WHEN cdeu.tipo_identificacion = '' THEN '' ELSE coalesce(cdeu.tipo_identificacion,'') END AS acronimoidcdeu, "+

			"CASE WHEN cdeu.numero_identificacion = '' THEN '' ELSE coalesce(cdeu.numero_identificacion,'') END AS identificacionCdeu, "+

			"CASE WHEN cdeu.codigo_pais = '' OR cdeu.codigo_departamento = '' OR cdeu.codigo_ciudad = '' THEN '' ELSE   coalesce(getnombreciudad(cdeu.codigo_pais,cdeu.codigo_departamento,cdeu.codigo_ciudad),'') END AS nombreCidudadIdCdeu, "+

			"CASE WHEN cdeu.telefono_reside = '' THEN '' ELSE coalesce(cdeu.telefono_reside,'') END AS telefonoResideCdeu, "+

			"CASE WHEN cdeu.telefono_oficina = '' THEN '' ELSE coalesce(cdeu.telefono_oficina,'') END AS telefonoOficinaCdeu "+

		//--********************END  DATOS ***************************************************************************************
			"FROM ingresos i INNER JOIN deudorco deu ON (deu.ingreso = i.id AND deu.clase_deudorco='"+ConstantesIntegridadDominio.acronimoDeudor+"') "+
			"INNER JOIN personas p 	ON (p.codigo = i.codigo_paciente) "+
			"INNER JOIN documentos_garantia dg ON (dg.ingreso = i.id AND dg.tipo_documento ='"+ConstantesIntegridadDominio.acronimoTipoDocumentoPagare+"' AND dg.estado != '"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"' ) "+
		 	"LEFT OUTER JOIN deudorco cdeu ON (cdeu.ingreso=dg.ingreso AND cdeu.clase_deudorco='"+ConstantesIntegridadDominio.acronimoCoDeudor+"') "+
		 	"WHERE i.id =  "+idIngreso;
		
		return consulta;
	}
	
	
	/**
	 * Metodo para obtener las consultas de cada DATESET del reporte 
	 * docgarantiaActaCompromiso.rptdesign
	 * @param parametros
	 * @return
	 */
	public static ArrayList<String> actaCompromisoCondicionesIngreso(HashMap parametros)
	{
		ArrayList<String> consultas = new ArrayList<String>();
		
		//*************SE TOMAN LOS PAReMETROS******************************************+
		String idIngreso = parametros.get("idIngreso").toString();
		String tipoDocumento = parametros.get("tipoDocumento").toString();
		String consecutivo = parametros.get("consecutivo").toString();
		String anioConsecutivo = parametros.get("anioConsecutivo").toString();
		//*********************************************************************************
		
		
		//*********************SE TOMA CONSULTA DE DATASET actaCompromiso **********************************
		String consulta = "SELECT "+	
		//--***************DATOS DEUDOR****************************************************************************************
			"deu.primer_nombre || ' ' ||deu.segundo_nombre ||' '  "+
			"||deu.primer_apellido|| ' ' ||deu.segundo_apellido AS \"nombredeu\", "+ 
			"getnombretipoidentificacion(deu.tipo_identificacion) AS \"tipoidentificaciondeu\", "+
			"deu.numero_identificacion AS \"identificaciondeu\", "+
			"getnombreciudad(deu.codigo_pais,deu.codigo_departamento,deu.codigo_ciudad) AS \"nombrecidudaddeu\", "+
			
			"CASE WHEN     p.tipo_identificacion = deu.tipo_identificacion  AND p.numero_identificacion = deu.numero_identificacion "+
			"then ' ' ELSE deu.relacion_paciente END AS \"parentescodeu\", "+	
		//--****************END DATOS DEUDOR************************************************************************************

		//--****************DATOS PACIENTE**************************************************************************************
			"getnombremedico (p.codigo) AS \"nombrepacientepc\", "+
			"getnombretipoidentificacion(p.tipo_identificacion) AS \"tipoidentificacionpac\", "+
			"p.numero_identificacion AS \"identificacionpaciente\", "+
			"getnombreciudad(p.codigo_pais_id,p.codigo_depto_id,p.codigo_ciudad_id) AS \"nombrecidudadpac\", "+
			"p.tipo_identificacion AS \"acronimoidentpac\", "+
			"p.direccion AS \"direccionresidepac\", "+
			"getdescripcionbarrio (p.codigo_barrio_vivienda) As \"barriopac\", "+
		//--***************END DATOS PACIENTE**********************************************************************************

		//--***************DATOS DEUDOR SI ES EL MISMO PACIENTE****************************************************************
			"CASE WHEN     p.tipo_identificacion = deu.tipo_identificacion  AND p.numero_identificacion = deu.numero_identificacion "+
			"AND deu.tipo_ocupacion='"+ConstantesIntegridadDominio.acronimoTipoTrabajadorEmpleado+"' then '<b>Empleado:</b> '||deu.ocupacion  ELSE "+ 
			"CASE WHEN deu.tipo_ocupacion='"+ConstantesIntegridadDominio.acronimoTipoTrabajadorIndependiente+"' then '<b>Independiente:</b> '||deu.ocupacion  ELSE "+ 
			"CASE WHEN deu.tipo_ocupacion='"+ConstantesIntegridadDominio.acronimoOtro+"' then '<b>Otro:</b> '|| deu.ocupacion  ELSE '      ' END END END AS  \"detocupacionempl\", "+

			"CASE WHEN     p.tipo_identificacion = deu.tipo_identificacion  AND p.numero_identificacion = deu.numero_identificacion "+
			"AND deu.tipo_ocupacion='"+ConstantesIntegridadDominio.acronimoTipoTrabajadorIndependiente+"' then deu.ocupacion  ELSE '      ' END AS \"detocupacioninde\", "+

			"CASE WHEN     p.tipo_identificacion = deu.tipo_identificacion  AND p.numero_identificacion = deu.numero_identificacion "+
			"AND deu.tipo_ocupacion='"+ConstantesIntegridadDominio.acronimoOtro+"' then deu.ocupacion  ELSE '      ' END AS \"detocupacionotro\", "+

			"CASE WHEN     p.tipo_identificacion = deu.tipo_identificacion  AND p.numero_identificacion = deu.numero_identificacion "+
			"then coalesce(deu.empresa, '') ELSE ' ' END AS \"empresa\", "+

			"CASE WHEN     p.tipo_identificacion = deu.tipo_identificacion  AND p.numero_identificacion = deu.numero_identificacion "+
			"then coalesce(deu.cargo, '') ELSE ' ' END AS \"cargo\", "+

			"CASE WHEN     p.tipo_identificacion = deu.tipo_identificacion  AND p.numero_identificacion = deu.numero_identificacion "+
			"then coalesce(deu.antiguedad, '') ELSE ' ' END AS \"antiguedad\", "+

			"CASE WHEN     p.tipo_identificacion = deu.tipo_identificacion  AND p.numero_identificacion = deu.numero_identificacion "+
			"then coalesce(deu.direccion_oficina,'') ELSE ' ' END AS \"direccionoficina\", "+

			"CASE WHEN     p.tipo_identificacion = deu.tipo_identificacion  AND p.numero_identificacion = deu.numero_identificacion "+
			"then p.telefono ELSE ' ' END AS \"telefono\", "+
		//--*********************END DATOS DEUDOR SI ES EL MISMO PACIENTE******************************************************

		//--******************DATOS REFERENCIA PERSONAL***********************************************************************
			"CASE WHEN  p.tipo_identificacion = deu.tipo_identificacion  AND p.numero_identificacion = deu.numero_identificacion "+
			"THEN deu.nombres_referencia ELSE ' ' END  AS \"nombresreferencia\", "+

			"CASE WHEN     p.tipo_identificacion = deu.tipo_identificacion  AND p.numero_identificacion = deu.numero_identificacion "+
			"then deu.direccion_referencia ELSE ' ' END AS \"direccionreferencia\", "+

			"CASE WHEN     p.tipo_identificacion = deu.tipo_identificacion  AND p.numero_identificacion = deu.numero_identificacion "+
			"then deu.telefono_referencia ELSE ' ' END AS \"telefonoreferencia\", "+
		//--****************END DATOS REFERENCIA PERSONAL**********************************************************************

		//--****************DATOS PERSONALES DEL RESPONSABLE (DATOS DEUDOR)*******************************************************************
			"CASE WHEN  deu.tipo_ocupacion='"+ConstantesIntegridadDominio.acronimoTipoTrabajadorEmpleado+"' then '<b>Empleado:</b> '||deu.ocupacion  ELSE "+ 
			"CASE WHEN  deu.tipo_ocupacion='"+ConstantesIntegridadDominio.acronimoTipoTrabajadorIndependiente+"' then '<b>Independiente:</b> '||deu.ocupacion  ELSE "+ 
			"CASE WHEN   deu.tipo_ocupacion='"+ConstantesIntegridadDominio.acronimoOtro+"' then '<b>Otro:</b> '||deu.ocupacion  ELSE "+ 
			"' ' END END END AS \"detocupacionempldeu\", "+

			"CASE WHEN  deu.tipo_ocupacion='"+ConstantesIntegridadDominio.acronimoTipoTrabajadorIndependiente+"' then deu.ocupacion  ELSE ' ' END AS \"detocupacionindedeu\", "+

			"CASE WHEN   deu.tipo_ocupacion='"+ConstantesIntegridadDominio.acronimoOtro+"' then deu.ocupacion  ELSE ' ' END AS \"detocupacionotrodeu\", "+
	
			"deu.tipo_identificacion AS \"acronimoidentdeu\", "+
			"deu.direccion_reside AS \"direccionredidedeu\", "+
			"getdescripcionbarrio (deu.codigo_barrio_reside) AS \"barriodeu\", "+
			"getintegridaddominio(deu.tipo_ocupacion) || deu.ocupacion AS \"ocupaciondeu\", "+
			"coalesce(deu.empresa,'') As \"empresadeu\", "+
			"coalesce(deu.cargo,'') AS \"cargodeu\", "+
			"coalesce(deu.antiguedad,'') AS \"antiguedaddeu\", "+
			"coalesce(deu.direccion_oficina,'') AS \"direccionoficinadeu\", "+
			"coalesce(deu.telefono_oficina,'') AS \"telefonooficinadeu\", "+
			"coalesce(deu.telefono_reside,'') AS \"telefonoresidedeu\", "+
			"coalesce(deu.nombres_referencia,'') AS \"nombresreferenciadeu\", "+
			"coalesce(deu.direccion_referencia,'') AS \"direccionreferenciadeu\", "+
			"coalesce(deu.telefono_referencia,'') AS \"telefonoreferenciadeu\" "+ 
		//--**************** END DATOS PERSONALES DEL RESPONSABLE ****************************************************************************

			"FROM ingresos i INNER JOIN deudorco deu ON (deu.ingreso = i.id AND deu.clase_deudorco='"+ConstantesIntegridadDominio.acronimoDeudor+"') "+
			"INNER JOIN personas p 	ON (p.codigo = i.codigo_paciente) "+
		



			"WHERE i.id="+idIngreso;
		consultas.add(consulta);
		//****************************************************************************************
		
		//********SE TOMA CONSULTA DE DATASEt convenio*************************************************
		consulta = "SELECT "+
		//--**********************INFORMACION CONVENIOS*********************************************************
			"getnombreconvenio (hci.convenio) AS \"nombreconvenioic\", "+
			"getNombClasificacTipoConvenio (hci.clasificacion_convenio) AS \"clasificacionconvenioic\", "+
			 "getnomtiporegimen (hci.tipo_regimen) AS \"regimenic\", "+
			 "CASE WHEN hci.clasificacion_convenio="+ConstantesBDFacturacion.codigoClasTipoConvenioParticular+" then ' ' ELSE hci.nro_carnet END AS \"afiliacionic\", "+
			 "CASE WHEN hci.clasificacion_convenio="+ConstantesBDFacturacion.codigoClasTipoConvenioAseguradora+" then hci.nro_poliza ELSE ' ' END AS \"polizaic\", "+
			 "CASE WHEN hci.clasificacion_convenio="+ConstantesBDFacturacion.codigoClasTipoConvenioParticular+" then ' ' ELSE getnombreestrato(hci.clasificacion_socioeconomica) END AS \"estratoic\", "+
			 "hci.plan_beneficios AS \"planescomplementariosic\" "+

		//--***********************END INFORMACION CONVENIOS***************************************************	
			 "FROM ingresos i "+ 
			 "INNER JOIN historial_conveniosxingreso hci On (hci.ingreso = i.id) "+ 
			 "WHERE i.id = "+idIngreso+" AND hci.tipo_documento = '"+tipoDocumento+"' AND hci.tipo_regimen!='"+ConstantesBD.codigoTipoRegimenParticular+"'";
		consultas.add(consulta);
		//*********************************************************************************************
		
		//*********++SE TOMA CONSULTA DE DATASET fechaGeneracion *******************************************
		consulta = "SELECT  "+
			"to_char(dg.fecha_generacion,'DD') AS \"dia\", "+
			"getnombremes(to_char(dg.fecha_generacion,'MM')) AS \"mes\" , "+ 
			"to_char(dg.fecha_generacion,'YYYY') as \"anio\" "+
			"FROM ingresos i "+ 
			"INNER JOIN documentos_garantia dg On (dg.ingreso = i.id) "+
			"WHERE i.id = "+idIngreso+" AND dg.consecutivo = '"+consecutivo+"'  AND dg.anio_consecutivo = '"+anioConsecutivo+"' AND dg.tipo_documento = '"+tipoDocumento+"' ";
		consultas.add(consulta);
		//********************************************************************************************************
		
		
		return consultas;
	}
	
	/**
	 * Metodo para obtener la consulta del DATaset del reporte docGarantiaPaghaere1.rptdesign
	 * @param parametros
	 * @return
	 */
	public static String docGarantiaPagare1(HashMap parametros)
	{
		//************SE TOMAN LOS PAReMETROS*********************************
		String idIngreso = parametros.get("idIngreso").toString();
		String tipoDocumento = parametros.get("tipoDocumento").toString();
		String estado = parametros.get("estado").toString();
		//*********************************************************************
		
		
		String consulta = "SELECT "+ 
		//--***************************DATOS DOCUMENTOS EN GARANTIA*************************************************************************
			
			"dg.consecutivo || CASE WHEN dg.anio_consecutivo = '' then '' ELSE ' - ' || dg.anio_consecutivo END AS consecutivo, "+
			"to_char(dg.fecha_generacion,'"+ConstantesBD.formatoFechaAp+"') AS fechaSus, "+
		//--**********************************END DATOS**************************************************************************************

		//--****************************DATOS PACIENTE***************************************************************************************
			"getnombremedico (p.codigo) AS nombrePacientepc, "+
			"getnombretipoidentificacion(p.tipo_identificacion) AS tipoIdentificacionPac, "+
			"p.numero_identificacion AS identificacionPac, "+

		//--*******************************END DATOS*******************************************************************************************

		//--******************************DATOS DEUDOR Y CODEUDOR****************************************************************************

			"deu.primer_nombre || ' ' || coalesce(deu.segundo_nombre,'') ||' '||deu.primer_apellido|| ' ' || coalesce(deu.segundo_apellido,'') AS nombreDeu, "+
			"getnombretipoidentificacion(deu.tipo_identificacion) AS tipoIdentificacionDeu, "+
			"deu.tipo_identificacion AS acronimoiddeu, "+
			"deu.numero_identificacion AS identificacionDeu, "+
			"getnombreciudad(deu.codigo_pais,deu.codigo_departamento,deu.codigo_ciudad) AS nombreCidudadIdDeu, "+
			"coalesce(deu.telefono_reside,'') AS telefonoResideDeu, "+
			"coalesce(deu.telefono_oficina,'') AS telefonoOficinaDeu, "+
			"coalesce(deu.telefono_referencia,'') AS telefonoReferenciaDeu, "+
			"getnombreciudad(deu.codigo_pais_reside, deu.codigo_departamento_reside, deu.codigo_ciudad_reside) AS nombreCidudadResideDeu, "+
		//--*******************************END DATOS************************************************************************************************

		//--**************************DATOS DEL CODEUDOR**********************************************************************************************
			"CASE WHEN cdeu.primer_nombre is null THEN '' ELSE coalesce(cdeu.primer_nombre,'') || ' ' || coalesce(cdeu.segundo_nombre,'') ||' '||coalesce(cdeu.primer_apellido,'')|| ' ' || coalesce(cdeu.segundo_apellido,'') END AS nombreCdeu, "+

			"CASE WHEN cdeu.tipo_identificacion is null THEN '' ELSE getnombretipoidentificacion(cdeu.tipo_identificacion) END AS tipoIdentificacionCdeu, "+

			"CASE WHEN cdeu.tipo_identificacion is null THEN 'CC' ELSE cdeu.tipo_identificacion END AS acronimoidcdeu, "+

			"CASE WHEN cdeu.numero_identificacion is null THEN '' ELSE cdeu.numero_identificacion END AS identificacionCdeu, "+

			"CASE WHEN cdeu.codigo_pais is null OR cdeu.codigo_departamento is null OR cdeu.codigo_ciudad is null THEN '' ELSE   getnombreciudad(cdeu.codigo_pais,cdeu.codigo_departamento,cdeu.codigo_ciudad) END AS nombreCidudadIdCdeu, "+

			"CASE WHEN cdeu.telefono_reside = '' THEN ' ' ELSE coalesce(cdeu.telefono_reside,' ') END AS telefonoResideCdeu, "+

			"CASE WHEN cdeu.telefono_oficina = '' THEN '' ELSE coalesce(cdeu.telefono_oficina,'') END AS telefonoOficinaCdeu, "+

			"CASE WHEN cdeu.telefono_referencia = '' THEN ' ' ELSE coalesce(cdeu.telefono_referencia,' ') END AS telefonoReferenciaCdeu, "+

			"CASE WHEN cdeu.codigo_pais_reside = '' OR cdeu.codigo_departamento_reside = '' OR cdeu.codigo_ciudad_reside = '' THEN '' ELSE "+
				"getnombreciudad(cdeu.codigo_pais_reside,cdeu.codigo_departamento_reside,cdeu.codigo_ciudad_reside) END AS nombreCidudadResideCdeu "+
		//--*********************************END DATOS**********************************************************************************************


			"FROM documentos_garantia dg INNER JOIN deudorco deu ON (deu.ingreso=dg.ingreso AND deu.clase_deudorco='"+ConstantesIntegridadDominio.acronimoDeudor+"') "+ 
			"LEFT OUTER JOIN deudorco cdeu ON (cdeu.ingreso=dg.ingreso AND cdeu.clase_deudorco='"+ConstantesIntegridadDominio.acronimoCoDeudor+"') "+
			"INNER JOIN personas p 	ON (p.codigo = deu.codigo_paciente) "+
			"WHERE dg.ingreso = "+idIngreso+" AND dg.tipo_documento = '"+tipoDocumento+"' AND dg.estado = '"+estado+"' ";
		
		return consulta;
	}

	
	
	
	
	/**
	 * Metodo para obtener la cosnulta del DATASET de docGarantiaCheque1.rptdesign 
	 * @param parametros
	 * @return
	 */
	public static String docGarantiaCheque1(HashMap parametros)
	{
		//**************SE TOMAN LOS PARAMETROS*******************************
		String idIngreso = parametros.get("idIngreso").toString();
		String consecutivo = parametros.get("consecutivo").toString();
		String tipoDocumento = parametros.get("tipoDocumento").toString();
		String anioConsecutivo = parametros.get("anioConsecutivo").toString();
		String codigoInstitucion = parametros.get("codigoInstitucion").toString();
		
		String consulta = "SELECT " +
			"entidad_financiera, " +
			"numero_cuenta,"+
			"numero_documento,"+ 
			"girador_documento," +
			"valor, " +
			"to_char(fecha_documento,'"+ConstantesBD.formatoFechaAp+"') AS fecha_documento, " +
			"clave_covinoc," +
			"consecutivo || CASE WHEN anio_consecutivo = '' then '' ELSE ' - ' || anio_consecutivo END AS consecutivo " +
			"FROM carterapaciente.documentos_garantia " +
			"WHERE ingreso="+idIngreso+" AND consecutivo='"+consecutivo+"' AND tipo_documento='"+tipoDocumento+"' AND anio_consecutivo='"+anioConsecutivo+"' AND institucion="+codigoInstitucion;
		
		return consulta;
	}
	
	
	/**
	 * Metodo para obtener la consulta del dataset de conceptos de recibos de caja
	 */
	public static String impresionConceptosReciboCaja(HashMap parametros)
	{
		
		String nroRC=parametros.get("nrorc")+"";
		String institucion=parametros.get("institucion")+"";
		
		String consultarConceptosRecibosCaja="SELECT " +
												"crc.consecutivo as consecutivoconceptorc, " +
												"crc.concepto as codigoconceptorc, " +
												"cit.descripcion as descripcionconceptorc, " +
												"cit.codigo_tipo_ingreso as tipoconceptorc," +
												"cit.valor as valortipoconceptorc, " +
												"CASE WHEN ((SELECT cit.valor FROM tesoreria.conceptos_ing_tesoreria cit WHERE cit.codigo = crc.concepto) = 'FACT')  "+
													"THEN (" +
															"SELECT '' || fv.consecutivo " +
															"FROM " +
																"facturacion.historico_encabezado he " +
															"INNER JOIN " +
																"facturasvarias.facturas_varias fv " +
															"ON(fv.historico_encabezado=he.codigo_pk) " +
															"WHERE fv.codigo_fac_var = CAST(crc.doc_soporte AS integer)  " +
														") "+
													"ELSE " +
														"crc.doc_soporte " +
													"END " +
												"as docsoporteconceptorc, " +
												
												// No ponerle el to_char porque se daña la consulta en el birt
												//"to_char(crc.valor, '999,999,999,999,999.00') as valorconceptorc, " +
												"crc.valor as valorconceptorc, " +
												"crc.tipo_id_beneficiario||' - ' as tipoidbenefeciario, " +
												"coalesce(crc.numero_id_beneficiario,'') as numeroidbeneficiario, " +
												"crc.nombre_beneficiario as nombrebeneficiario " +
										"from detalle_conceptos_rc crc " +
										"inner join conceptos_ing_tesoreria cit on (crc.concepto=cit.codigo and crc.institucion=crc.institucion) " +
										"where ";
		
		
		
		consultarConceptosRecibosCaja+=" crc.numero_recibo_caja ='"+nroRC+"' and crc.institucion ="+institucion;

		return consultarConceptosRecibosCaja;
	}
	
	/**
	 * Obtener la consulta de los pagos de recibos de caja
	 */
	public static String impresionTotalesReciboCaja(HashMap parametros)
	{
		String totalPagosEfectivos="SELECT fp.descripcion as descripcion, to_char(dprc.valor, '999,999,999,999,999.00') as valor from detalle_pagos_rc dprc inner join formas_pago fp on(dprc.forma_pago=fp.consecutivo) ";
		String totalPagosCheques="SELECT  fp.descripcion ||' '|| mc.numero_cheque as descripcion, to_char(mc.valor, '999,999,999,999,999.00') as valor from movimientos_cheques mc inner join detalle_pagos_rc dprc on(mc.det_pago_rc=dprc.consecutivo) inner join formas_pago fp on(dprc.forma_pago=fp.consecutivo)";
		String totalTarjetasCredito="SELECT fp.descripcion ||' '|| mt.numero_tarjeta as descripcion, to_char(mt.valor, '999,999,999,999,999.00') as valor from movimientos_tarjetas mt inner join tarjetas_financieras tf on (mt.codigo_tarjeta=tf.consecutivo) inner join detalle_pagos_rc dprc on(mt.det_pago_rc=dprc.consecutivo) inner join formas_pago fp on(dprc.forma_pago=fp.consecutivo) ";
		String totalTarjetasDebito="SELECT fp.descripcion ||' '||mt.numero_tarjeta as descripcion, to_char(mt.valor, '999,999,999,999,999.00') as valor from movimientos_tarjetas mt inner join tarjetas_financieras tf on (mt.codigo_tarjeta=tf.consecutivo) inner join detalle_pagos_rc dprc on(mt.det_pago_rc=dprc.consecutivo) inner join formas_pago fp on(dprc.forma_pago=fp.consecutivo) ";
		
		String totalBonos = "(SELECT fp.descripcion as descripcion, to_char(sum(dprc.valor), '999,999,999,999,999.00') as valor from movimientos_bonos mb inner join detalle_pagos_rc dprc on(mb.det_pago_rc=dprc.consecutivo) inner join formas_pago fp on(dprc.forma_pago=fp.consecutivo) ";
		
		String consulta= "";
		String union=" UNION ";
		
		String numeroReciboCaja=parametros.get("nrorc")+"";
		String institucion=parametros.get("institucion")+"";
		
		// condicion efectivo
		totalPagosEfectivos+="where fp.tipo_detalle="+ConstantesBD.codigoTipoDetalleFormasPagoNinguno+" and dprc.numero_recibo_caja='"+numeroReciboCaja+"' and dprc.institucion="+institucion;
		//condicion cheque
		totalPagosCheques+="where dprc.numero_recibo_caja='"+numeroReciboCaja+"' and dprc.institucion="+institucion;
		//condicion tarjetas Credito
		totalTarjetasCredito+="where tf.tipo_tarjeta_financiera='"+ConstantesBD.tiposTarjetaCredito.getAcronimo()+"' and dprc.numero_recibo_caja='"+numeroReciboCaja+"' and dprc.institucion="+institucion;
		//condicion tarjetas Debito
		totalTarjetasDebito+="where tf.tipo_tarjeta_financiera='"+ConstantesBD.tiposTarjetaDebito.getAcronimo()+"' and dprc.numero_recibo_caja='"+numeroReciboCaja+"' and dprc.institucion="+institucion;
		
		//condicion Bonos
		totalBonos+="where fp.tipo_detalle="+ConstantesBD.codigoTipoDetalleFormasPagoBono+" and dprc.numero_recibo_caja='"+numeroReciboCaja+"' and dprc.institucion="+institucion+" GROUP BY fp.descripcion )";
		
		consulta= totalPagosEfectivos+union+totalPagosCheques+union+totalTarjetasCredito+union+totalTarjetasDebito+union+totalBonos;
		
		Log4JManager.info("------------- consulta para la impresion del recibo de caja " + consulta);
		
		return consulta;
		
	}
	

	public static String honorariosMedicosValFacturaValHonorario(
			HashMap filtros) {
		
		String condicionesGenerales = " WHERE 1=1 ";
		String condicionesEspecificas = " AND dfs.pool is not null AND dfs.valor_pool>0 AND s.codigo_medico_responde is not null ";
		String condicionesEspecificasExcentas = " AND dfse.pool is not null AND dfse.valor_pool>0 AND s.codigo_medico_responde is not null ";
		String condicionesPaquetizacion = " AND pdf.pool is not null AND pdf.valor_pool>0 AND pdf.medico_asocio is not null ";
		String condicionescx = " AND adf.pool is not null AND adf.valor_pool>0 AND adf.codigo_medico is not null ";
		String condicionesExcentasCX = " AND adfe.pool is not null AND adfe.valor_pool>0 AND adfe.codigo_medico is not null ";
		
		String consultaEspecifica = "SELECT " +
							"s.pool as \"pool\", "+
							"po.descripcion as \"descpool\", "+
							"s.codigo_medico_responde as \"codigo_medico_responde\", "+
							"f.consecutivo_factura as \"factura\", " +
							"pac.tipo_identificacion||' '||pac.numero_identificacion as \"tiponumidpac\", " +
							"getnombrepersona(pac.codigo) as \"nombrepac\", " +
							"getnombreservicio(dfs.servicio, "+ConstantesBD.codigoTarifarioCups+") as \"servicio\", " +
							"getnombreconvenio(f.convenio) as \"convenio\", " +
							"f.fecha as \"fecha\", " +
							"getnomcentroatencion(f.centro_aten) as \"centroatencion\", " +
							"getnombrepersona(s.codigo_medico_responde) as \"profesional\", ";
		
		if(filtros.get("valorFacturaCero").toString().equals(ConstantesBD.acronimoSi)){
			consultaEspecifica += 			"0 as \"valorpool\", " +
							"0 as \"valorfactura\", ";
		} else {
			consultaEspecifica += 			"dfs.valor_pool as \"valorpool\", " +
							"dfs.valor_total as \"valorfactura\", ";
		}
		
		consultaEspecifica +=				"s.consecutivo_ordenes_medicas as \"nroorden\", " +
							"to_date('"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicial")+"")+"', 'yyyy-mm-dd') AS \"fecha_inicial_parametro\", " +
							"to_date('"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"', 'yyyy-mm-dd') AS \"fecha_final_parametro\", ";
							
		
		if(filtros.get("anuladasXFechaSinCodigo").toString().equals(ConstantesBD.acronimoSi)){
			consultaEspecifica += "CASE WHEN ( to_char(af.fecha_grabacion, 'YYYY-MM-DD') between '" + 
									UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicial")+"")+"' and '" + 
									UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"' " +
									") " +  
									"THEN af.consecutivo_anulacion ELSE '' END AS \"consecutivo_anulacion\" ";
		} else if(filtros.get("anuladasSinCodigo").toString().equals(ConstantesBD.acronimoSi)){
			consultaEspecifica += " '' AS \"consecutivo_anulacion\" ";
		} else {
			consultaEspecifica += " af.consecutivo_anulacion AS \"consecutivo_anulacion\" ";
		}
			
		consultaEspecifica += "FROM " +
							"facturas f " +
						"INNER JOIN " +
							"det_factura_solicitud dfs on (f.codigo=dfs.factura) " +
						"INNER JOIN " +
							"solicitudes s on(dfs.solicitud=s.numero_solicitud) " +
						"INNER JOIN " +
							"cuentas c on (c.id=s.cuenta) " +
						"INNER JOIN " +
							"personas pac on(c.codigo_paciente=pac.codigo) " +
						"INNER JOIN " +
							"centro_atencion ca on (ca.consecutivo=f.centro_aten) "+
						"INNER JOIN " +
							"pooles po on (po.codigo=s.pool) " +
						"LEFT OUTER JOIN " + 
						 	"anulaciones_facturas af ON (af.consecutivo_factura = f.consecutivo_factura) ";
		
		//
		String consultaEspecificaExcentas = "SELECT " +
									"s.pool as \"pool\", "+
									"po.descripcion as \"descpool\", "+
									"s.codigo_medico_responde as \"codigo_medico_responde\", "+
									"f.consecutivo_factura as \"factura\", " +
									"pac.tipo_identificacion||' '||pac.numero_identificacion as \"tiponumidpac\", " +
									"getnombrepersona(pac.codigo) as \"nombrepac\", " +
									"getnombreservicio(dfse.servicio, "+ConstantesBD.codigoTarifarioCups+") as \"servicio\", " +
									"getnombreconvenio(f.convenio) as \"convenio\", " +
									"f.fecha as \"fecha\", " +
									"getnomcentroatencion(f.centro_aten) as \"centroatencion\", " +
									"getnombrepersona(s.codigo_medico_responde) as \"profesional\", ";

		if(filtros.get("valorFacturaCero").toString().equals(ConstantesBD.acronimoSi)){
				consultaEspecificaExcentas += 	"0 as \"valorpool\", " +
												"0 as \"valorfactura\", ";
		} else {
				consultaEspecificaExcentas += 	"dfse.valor_pool as \"valorpool\", " +
												"dfse.valor_total as \"valorfactura\", ";
		}

		consultaEspecificaExcentas += 	"s.consecutivo_ordenes_medicas as \"nroorden\", " +
								"to_date('"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicial")+"")+"', 'yyyy-mm-dd') AS \"fecha_inicial_parametro\", " +
								"to_date('"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"', 'yyyy-mm-dd') AS \"fecha_final_parametro\", ";
		
		if(filtros.get("anuladasXFechaSinCodigo").toString().equals(ConstantesBD.acronimoSi)){
			consultaEspecificaExcentas += "CASE WHEN ( to_char(af.fecha_grabacion, 'YYYY-MM-DD') between '" + 
									UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicial")+"")+"' and '" + 
									UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"' " +
									") " +  
									"THEN af.consecutivo_anulacion ELSE '' END AS \"consecutivo_anulacion\" ";
		} else if(filtros.get("anuladasSinCodigo").toString().equals(ConstantesBD.acronimoSi)){
			consultaEspecificaExcentas += " '' AS \"consecutivo_anulacion\" ";
		} else {
			consultaEspecificaExcentas += " af.consecutivo_anulacion AS \"consecutivo_anulacion\" ";
		}
								
		consultaEspecificaExcentas +="FROM " +
									"facturas f " +
								"INNER JOIN " +
									"facturacion.det_factura_solicitud_excenta dfse on (f.codigo=dfse.factura) " +
								"INNER JOIN " +
									"solicitudes s on(dfse.solicitud=s.numero_solicitud) " +
								"INNER JOIN " +
									"cuentas c on (c.id=s.cuenta) " +
								"INNER JOIN " +
									"personas pac on(c.codigo_paciente=pac.codigo) " +
								"INNER JOIN " +
									"centro_atencion ca on (ca.consecutivo=f.centro_aten) "+
								"INNER JOIN " +
									"pooles po on (po.codigo=s.pool) " +
								"LEFT OUTER JOIN " + 
								 	"anulaciones_facturas af ON (af.consecutivo_factura = f.consecutivo_factura) ";
		
		//Consulta paquetizacion
		String consultaPaquetizacion = "SELECT " +
							"pdf.pool as \"pool\", "+
							"po.descripcion as \"descpool\", "+
							"pdf.medico_asocio as \"codigo_medico_responde\", "+
							"f.consecutivo_factura as \"factura\", " +
							"pac.tipo_identificacion||' '||pac.numero_identificacion as \"tiponumidpac\", " +
							"getnombrepersona(pac.codigo) as \"nombrepac\", " +
							"case when pdf.tipo_solicitud=" +ConstantesBD.codigoTipoSolicitudCirugia+" then "+
							"getnombreservicio(pdf.servicio_cx, "+ConstantesBD.codigoTarifarioCups+") " +
							" else " +
								"getnombreservicio(pdf.servicio, "+ConstantesBD.codigoTarifarioCups+")  " +
							"end as \"servicio\", " + 	
							"getnombreconvenio(f.convenio) as \"convenio\", " +
							"f.fecha as \"fecha\", " +
							"getnomcentroatencion(f.centro_aten) as \"centroatencion\", " +
							"getnombrepersona(cast (pdf.medico_asocio as integer)) as \"profesional\", ";
		
		if(filtros.get("valorFacturaCero").toString().equals(ConstantesBD.acronimoSi)){
			consultaPaquetizacion += 			"0 as \"valorpool\", " +
					"0 as \"valorfactura\", ";
		} else {
			consultaPaquetizacion += 			"pdf.valor_pool as \"valorpool\", " +
					"pdf.valor_total as \"valorfactura\", ";
		}
		
		consultaPaquetizacion +=				"s.consecutivo_ordenes_medicas as \"nroorden\", " +
							"to_date('"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicial")+"")+"', 'yyyy-mm-dd') AS \"fecha_inicial_parametro\", " +
							"to_date('"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"', 'yyyy-mm-dd') AS \"fecha_final_parametro\", ";
	
		if(filtros.get("anuladasXFechaSinCodigo").toString().equals(ConstantesBD.acronimoSi)){
			consultaPaquetizacion += "CASE WHEN ( to_char(af.fecha_grabacion, 'YYYY-MM-DD') between '" + 
									UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicial")+"")+"' and '" + 
									UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"' " +
									") " +  
									"THEN af.consecutivo_anulacion ELSE '' END AS \"consecutivo_anulacion\" ";
		} else if(filtros.get("anuladasSinCodigo").toString().equals(ConstantesBD.acronimoSi)){
			consultaPaquetizacion += " '' AS \"consecutivo_anulacion\" ";
		} else {
			consultaPaquetizacion += " af.consecutivo_anulacion AS \"consecutivo_anulacion\" ";
		}
		
		consultaPaquetizacion +="FROM " +
							"facturas f " +
						"INNER JOIN " +
							"det_factura_solicitud dfs on (f.codigo=dfs.factura) " +
						"INNER JOIN " +
							"paquetizacion_det_factura pdf on (pdf.codigo_det_fact = dfs.codigo) " +	
						"INNER JOIN " +
							"solicitudes s on(pdf.solicitud=s.numero_solicitud) " +
						"INNER JOIN " +
							"cuentas c on (c.id=s.cuenta) " +
						"INNER JOIN " +
							"personas pac on(c.codigo_paciente=pac.codigo) " +
						"INNER JOIN " +
							"centro_atencion ca on (ca.consecutivo=f.centro_aten) "+
						"INNER JOIN " +
							"pooles po on (po.codigo=pdf.pool) "+
						"LEFT OUTER JOIN " + 
						 	"anulaciones_facturas af ON (af.consecutivo_factura = f.consecutivo_factura) ";
		
		


		String consultacx = "SELECT " +
											"adf.pool as \"pool\", "+
											"po.descripcion as \"descpool\", "+
											"adf.codigo_medico as \"codigo_medico_responde\", "+
											"f.consecutivo_factura as \"factura\", " +
											"pac.tipo_identificacion||' '||pac.numero_identificacion as \"tiponumidpac\", " +
											"getnombrepersona(pac.codigo) as \"nombrepac\", " +
											"getnombreservicio(dfs.servicio, "+ConstantesBD.codigoTarifarioCups+") as \"servicio\", " +
											"getnombreconvenio(f.convenio) as \"convenio\", " +
											"f.fecha as \"fecha\", " +
											"getnomcentroatencion(f.centro_aten) as \"centroatencion\", " +
											"getnombrepersona(adf.codigo_medico) as \"profesional\", ";
								
		if(filtros.get("valorFacturaCero").toString().equals(ConstantesBD.acronimoSi)){
			consultacx += 			"0 as \"valorpool\", " +
			"0 as \"valorfactura\", ";
		} else {
			consultacx += 			"adf.valor_pool as \"valorpool\", " +
			"adf.valor_total as \"valorfactura\", ";
		}
		
		consultacx +=				"s.consecutivo_ordenes_medicas as \"nroorden\", " +
					"to_date('"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicial")+"")+"', 'yyyy-mm-dd') AS \"fecha_inicial_parametro\", " +
					"to_date('"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"', 'yyyy-mm-dd') AS \"fecha_final_parametro\", " ;
					
		if(filtros.get("anuladasXFechaSinCodigo").toString().equals(ConstantesBD.acronimoSi)){
			consultacx += "CASE WHEN ( to_char(af.fecha_grabacion, 'YYYY-MM-DD') between '" + 
									UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicial")+"")+"' and '" + 
									UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"' " +
									") " +  
									"THEN af.consecutivo_anulacion ELSE '' END AS \"consecutivo_anulacion\" ";
		} else if(filtros.get("anuladasSinCodigo").toString().equals(ConstantesBD.acronimoSi)){
			consultacx += " '' AS \"consecutivo_anulacion\" ";
		} else {
			consultacx += " af.consecutivo_anulacion AS \"consecutivo_anulacion\" ";
		}
		
		consultacx +="FROM " +
					"facturas f " +
				"INNER JOIN " +
					"det_factura_solicitud dfs on (f.codigo=dfs.factura) " +
				"INNER JOIN " +
					"asocios_det_factura adf on (adf.codigo = dfs.codigo) " +	
				"INNER JOIN " +
					"solicitudes s on(dfs.solicitud=s.numero_solicitud) " +
				"INNER JOIN " +
					"cuentas c on (c.id=s.cuenta) " +
				"INNER JOIN " +
					"personas pac on(c.codigo_paciente=pac.codigo) " +
				"INNER JOIN " +
					"centro_atencion ca on (ca.consecutivo=f.centro_aten) "+
				"INNER JOIN " +
					"pooles po on (po.codigo=adf.pool) "+
				"LEFT OUTER JOIN " + 
				 	"anulaciones_facturas af ON (af.consecutivo_factura = f.consecutivo_factura) ";

		//Consulta Cirugia Excento						
		String consultaExcentaCX = "SELECT " +
					"adfe.pool as \"pool\", "+
					"po.descripcion as \"descpool\", "+
					"adfe.codigo_medico as \"codigo_medico_responde\", "+
					"f.consecutivo_factura as \"factura\", " +
					"pac.tipo_identificacion||' '||pac.numero_identificacion as \"tiponumidpac\", " +
					"getnombrepersona(pac.codigo) as \"nombrepac\", " +
					"getnombreservicio(adfe.servicio_asocio, "+ConstantesBD.codigoTarifarioCups+") as \"servicio\", " +
					"getnombreconvenio(f.convenio) as \"convenio\", " +
					"f.fecha as \"fecha\", " +
					"getnomcentroatencion(f.centro_aten) as \"centroatencion\", " +
					"getnombrepersona(adfe.codigo_medico) as \"profesional\", ";
		
		if(filtros.get("valorFacturaCero").toString().equals(ConstantesBD.acronimoSi)){
			consultaExcentaCX += 	"0 as \"valorpool\", " +
									"0 as \"valorfactura\", ";
		} else {
			consultaExcentaCX += 	"adfe.valor_pool as \"valorpool\", " +
									"adfe.valor_total as \"valorfactura\", ";
		}
		
		consultaExcentaCX +=
					"s.consecutivo_ordenes_medicas as \"nroorden\", " +
					"to_date('"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicial")+"")+"', 'yyyy-mm-dd') AS \"fecha_inicial_parametro\", " +
					"to_date('"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"', 'yyyy-mm-dd') AS \"fecha_final_parametro\", ";
					
		if(filtros.get("anuladasXFechaSinCodigo").toString().equals(ConstantesBD.acronimoSi)){
			consultaExcentaCX += "CASE WHEN ( to_char(af.fecha_grabacion, 'YYYY-MM-DD') between '" + 
									UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicial")+"")+"' and '" + 
									UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"' " +
									") " +  
									"THEN af.consecutivo_anulacion ELSE '' END AS \"consecutivo_anulacion\" ";
		} else if(filtros.get("anuladasSinCodigo").toString().equals(ConstantesBD.acronimoSi)){
			consultaExcentaCX += " '' AS \"consecutivo_anulacion\" ";
		} else {
			consultaExcentaCX += " af.consecutivo_anulacion AS \"consecutivo_anulacion\" ";
		}
		
		consultaExcentaCX +="FROM " +
						"facturas f " +
					"INNER JOIN " +
						"facturacion.det_factura_solicitud_excenta dfse on (f.codigo=dfse.factura) " +
					"INNER JOIN " +
						"facturacion.asocios_det_factura_excenta adfe on (adfe.codigo = dfse.codigo) " +	
					"INNER JOIN " +
						"solicitudes s on(dfse.solicitud=s.numero_solicitud) " +
					"INNER JOIN " +
						"cuentas c on (c.id=s.cuenta) " +
					"INNER JOIN " +
						"personas pac on(c.codigo_paciente=pac.codigo) " +
					"INNER JOIN " +
						"centro_atencion ca on (ca.consecutivo=f.centro_aten) "+
					"INNER JOIN " +
						"pooles po on (po.codigo=adfe.pool) "+
					"LEFT OUTER JOIN " + 
					 	"anulaciones_facturas af ON (af.consecutivo_factura = f.consecutivo_factura) ";
					
		if (filtros.get("mostrarFacturasAnuladas").toString().equals(ConstantesBD.acronimoNo) &&
				filtros.get("anuladasSinCodigo").toString().equals(ConstantesBD.acronimoNo))
			condicionesGenerales += " and f.estado_facturacion != "+ConstantesBD.codigoEstadoFacturacionAnulada+" ";
		
		if (filtros.get("anuladasSinCodigo").toString().equals(ConstantesBD.acronimoSi) || 
				(filtros.get("mostrarFacturasAnuladas").toString().equals(ConstantesBD.acronimoSi) && 
				filtros.get("valorFacturaCero").toString().equals(ConstantesBD.acronimoSi)))
			condicionesGenerales += " and f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionAnulada+" ";

		if(!(filtros.get("pool")+"").equals("-1")) {
			condicionesEspecificas += " and s.pool="+filtros.get("pool")+" ";
			condicionesEspecificasExcentas += " and s.pool="+filtros.get("pool")+" ";
			condicionesPaquetizacion += " and pdf.pool="+filtros.get("pool")+" ";
			condicionescx += " and adf.pool="+filtros.get("pool")+" ";
			condicionesExcentasCX += " and adfe.pool="+filtros.get("pool")+" ";
			
		}
			
		if(!filtros.get("profesional").toString().equals("-1")){
			if (filtros.get("soloAnuladasXProfesional").toString().equals(ConstantesBD.acronimoNo)) {
				condicionesEspecificas += " and s.codigo_medico_responde="+filtros.get("profesional")+" ";
				condicionesEspecificasExcentas += " and s.codigo_medico_responde="+filtros.get("profesional")+" ";
				condicionesPaquetizacion += " and pdf.medico_asocio="+filtros.get("profesional")+" ";
				}
			condicionescx += " and adf.codigo_medico="+filtros.get("profesional")+" ";
			condicionesExcentasCX += " and adfe.codigo_medico="+filtros.get("profesional")+" ";
		}	
		
		if(!filtros.get("centroAtencion").toString().equals("-1"))
			condicionesGenerales += " and f.centro_aten="+filtros.get("centroAtencion")+" "; 
		
		if(filtros.get("fechaInicial")!=null&&!(filtros.get("fechaInicial")+"").equals("null")&&!(filtros.get("fechaInicial")+"").equals(""))
		{
			if(filtros.get("soloAnuladasXProfesional").toString().equals(ConstantesBD.acronimoSi))
			{
				condicionesGenerales += "  and  ( " +
										" to_char(af.fecha_grabacion, 'YYYY-MM-DD') between '"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"' " +
										") ";
			}
			else
			{
				condicionesGenerales += "  and  ( " +
												"to_char(f.fecha, 'yyyy-mm-dd') between '"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"' " +
												"OR to_char(af.fecha_grabacion, 'YYYY-MM-DD') between '"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"' " +
										") ";
			}
			/**
			 * Se realiza una modificación a la consulta para eliminar la restricción de busqueda 
			 * de facturas por fecha de anulación
			 * MT-1872 - diecorqu
			 */
			/*condicionesGenerales += "  and  ( " +
									"to_char(f.fecha, 'yyyy-mm-dd') between '"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"' " +
									") ";*/
		}
		
		if(filtros.get("anuladasXFecha").toString().equals(ConstantesBD.acronimoSi))
			condicionesGenerales += "  and  ( " +
									" to_char(af.fecha_grabacion, 'YYYY-MM-DD') between '" + 
									UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicial")+"")+"' and '" + 
									UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"' " +
									") ";
		
		if(filtros.get("anuladasSinCodigo").toString().equals(ConstantesBD.acronimoSi))
			condicionesGenerales += "  and  ( " +
									" to_char(af.fecha_grabacion, 'YYYY-MM-DD') > '" + 
									UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"' " +
									") ";
		
		if(filtros.get("facturaInicial")!=null&&!(filtros.get("facturaInicial")+"").equals("null")&&!(filtros.get("facturaInicial")+"").equals(""))
			condicionesGenerales += "  and f.consecutivo_factura between "+filtros.get("facturaInicial")+" and "+filtros.get("facturaFinal")+" ";
		
		if(filtros.get("institucion")!=null&&!(filtros.get("institucion")+"").equals("null")&&!(filtros.get("institucion")+"").equals("")&&!(filtros.get("institucion")+"").equals("-1"))
			condicionesGenerales += "  and f.institucion= "+filtros.get("institucion")+" ";
		
		if(!(filtros.get("nroOrden")+"").equals(""))
			condicionesGenerales += " and s.consecutivo_ordenes_medicas="+filtros.get("nroOrden")+" ";
		
		if(!(filtros.get("ciudad")+"").equals("-1") && !(filtros.get("ciudad")+"").equals(""))
			condicionesGenerales += " and ca.ciudad='"+filtros.get("ciudad")+"' ";
		
		if(!(filtros.get("pais")+"").equals("-1") && !(filtros.get("pais")+"").equals(""))
			condicionesGenerales += " and ca.pais='"+filtros.get("pais")+"' ";
		
		if(!(filtros.get("region")+"").equals("-1") && !(filtros.get("region")+"").equals("") && !UtilidadTexto.isEmpty(filtros.get("region")+""))
			condicionesGenerales += " and ca.region_cobertura='"+filtros.get("region")+"' ";

		if(filtros.get("soloAnuladasXProfesional").toString().equals(ConstantesBD.acronimoSi)){
			condicionesEspecificas += " and f.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionAnulada+" AND s.codigo_medico_responde=? ";
			condicionesEspecificasExcentas += " and f.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionAnulada+" AND s.codigo_medico_responde=? ";
			condicionesPaquetizacion += " and f.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionAnulada+" AND pdf.medico_asocio=? ";
			condicionescx += " and f.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionAnulada+" AND adf.codigo_medico=? ";
			condicionesExcentasCX += " and f.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionAnulada+" AND adfe.codigo_medico=? ";
		}
		
		if(!(filtros.get("especialidad")+"").equals("-1")) 
			condicionesGenerales += " and s.especialidad_solicitada="+filtros.get("especialidad")+" ";
		
		String sql = 	"SELECT * FROM (" +
			"("+
				consultaEspecifica+condicionesGenerales+condicionesEspecificas+
			") UNION ("+
				consultaEspecificaExcentas+condicionesGenerales+condicionesEspecificasExcentas+	
			") UNION ("+
				consultaPaquetizacion+condicionesGenerales+condicionesPaquetizacion+
			") UNION ("+
				consultacx+condicionesGenerales+condicionescx+		
			") UNION ("+
				consultaExcentaCX+condicionesGenerales+condicionesExcentasCX+
			")"+
		") datos";
		
		//sql += " ORDER BY getnombrepersona(s.codigo_medico_responde), f.consecutivo_factura ";
		
		return sql;
	}
	
	public static String honorariosMedicosValHonorario(HashMap filtros) {
		
		String condicionesGenerales = " WHERE 1=1 ";
		String condicionesEspecificas = " AND dfs.pool is not null AND dfs.valor_pool>0 AND s.codigo_medico_responde is not null ";
		String condicionesEspecificasExcentas = " AND dfse.pool is not null AND dfse.valor_pool>0 AND s.codigo_medico_responde is not null ";
		String condicionesPaquetizacion = " AND pdf.pool is not null AND pdf.valor_pool>0 AND pdf.medico_asocio is not null ";
		String condicionescx = " AND adf.pool is not null AND adf.valor_pool>0 AND adf.codigo_medico is not null ";
		String condicionesExentasCX = " AND adfe.pool is not null AND adfe.valor_pool>0 AND adfe.codigo_medico is not null ";
		
		// Consulta Especifica
		String consultaEspecifica = "SELECT " +
							"s.pool as \"pool\", "+
							"po.descripcion as \"descpool\", "+
							"s.codigo_medico_responde as \"codigo_medico_responde\", "+
							"f.consecutivo_factura as \"factura\", " +
							"pac.tipo_identificacion||' '||pac.numero_identificacion as \"tiponumidpac\", " +
							"getnombrepersona(pac.codigo) as \"nombrepac\", " +
							"getnombreservicio(dfs.servicio, "+ConstantesBD.codigoTarifarioCups+") as \"servicio\", " +
							"getnombreconvenio(f.convenio) as \"convenio\", " +
							"f.fecha as \"fecha\", " +
							"getnomcentroatencion(f.centro_aten) as \"centroatencion\", ";
							
		if(filtros.get("valorFacturaCero").toString().equals(ConstantesBD.acronimoSi))
			consultaEspecifica += 			"0 as \"valorpool\", ";
		else
			consultaEspecifica += 			"dfs.valor_pool as \"valorpool\", ";
							
		consultaEspecifica += "getnombrepersona(s.codigo_medico_responde) as \"profesional\"," +
							"s.consecutivo_ordenes_medicas as \"nroorden\", " +
							"to_date('"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicial")+"")+"', 'yyyy-mm-dd') AS \"fecha_inicial_parametro\", " +
							"to_date('"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"', 'yyyy-mm-dd') AS \"fecha_final_parametro\", ";
		
		if(filtros.get("anuladasXFechaSinCodigo").toString().equals(ConstantesBD.acronimoSi)){
			consultaEspecifica += "CASE WHEN ( to_char(af.fecha_grabacion, 'YYYY-MM-DD') between '" + 
									UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicial")+"")+"' and '" + 
									UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"' " +
									") " +  
									"THEN af.consecutivo_anulacion ELSE '' END AS \"consecutivo_anulacion\" ";
		} else if(filtros.get("anuladasSinCodigo").toString().equals(ConstantesBD.acronimoSi)){
			consultaEspecifica += " '' AS \"consecutivo_anulacion\" ";
		} else {
			consultaEspecifica += " af.consecutivo_anulacion AS \"consecutivo_anulacion\" ";
		}					
		
		consultaEspecifica +="FROM " +
							"facturas f " +
						"INNER JOIN " +
							"det_factura_solicitud dfs on (f.codigo=dfs.factura) " +
						"INNER JOIN " +
							"solicitudes s on(dfs.solicitud=s.numero_solicitud) " +
						"INNER JOIN " +
							"cuentas c on (c.id=s.cuenta) " +
						"INNER JOIN " +
							"personas pac on(c.codigo_paciente=pac.codigo) " +
						"INNER JOIN " +
							"centro_atencion ca on (ca.consecutivo=f.centro_aten) "+
						"INNER JOIN " +
							"pooles po on (po.codigo=s.pool) " +
						"LEFT OUTER JOIN " + 
							"anulaciones_facturas af ON (af.consecutivo_factura = f.consecutivo_factura) ";
		
		
		//Consulta Facturas Excentas
		String consultaEspecificaExcentas = 
									"SELECT " +
									"s.pool as \"pool\", "+
									"po.descripcion as \"descpool\", "+
									"s.codigo_medico_responde as \"codigo_medico_responde\", "+
									"f.consecutivo_factura as \"factura\", " +
									"pac.tipo_identificacion||' '||pac.numero_identificacion as \"tiponumidpac\", " +
									"getnombrepersona(pac.codigo) as \"nombrepac\", " +
									"getnombreservicio(dfse.servicio, "+ConstantesBD.codigoTarifarioCups+") as \"servicio\", " +
									"getnombreconvenio(f.convenio) as \"convenio\", " +
									"f.fecha as \"fecha\", " +
									"getnomcentroatencion(f.centro_aten) as \"centroatencion\", ";
		
		if(filtros.get("valorFacturaCero").toString().equals(ConstantesBD.acronimoSi))
			consultaEspecificaExcentas += "0 as \"valorpool\", ";
		else
			consultaEspecificaExcentas += "dfse.valor_pool as \"valorpool\", ";
							
		consultaEspecificaExcentas += 
							"getnombrepersona(s.codigo_medico_responde) as \"profesional\"," +
							"s.consecutivo_ordenes_medicas as \"nroorden\", " +
							"to_date('"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicial")+"")+"', 'yyyy-mm-dd') AS \"fecha_inicial_parametro\", " +
							"to_date('"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"', 'yyyy-mm-dd') AS \"fecha_final_parametro\", ";
							
		if(filtros.get("anuladasXFechaSinCodigo").toString().equals(ConstantesBD.acronimoSi)){
			consultaEspecificaExcentas += "CASE WHEN ( to_char(af.fecha_grabacion, 'YYYY-MM-DD') between '" + 
									UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicial")+"")+"' and '" + 
									UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"' " +
									") " +  
									"THEN af.consecutivo_anulacion ELSE '' END AS \"consecutivo_anulacion\" ";
		} else if(filtros.get("anuladasSinCodigo").toString().equals(ConstantesBD.acronimoSi)){
			consultaEspecificaExcentas += " '' AS \"consecutivo_anulacion\" ";
		} else {
			consultaEspecificaExcentas += " af.consecutivo_anulacion AS \"consecutivo_anulacion\" ";
		}
		
		consultaEspecificaExcentas +="FROM " +
							"facturas f " +
						"INNER JOIN " +
							"facturacion.det_factura_solicitud_excenta dfse on (f.codigo=dfse.factura) " +
						"INNER JOIN " +
							"solicitudes s on(dfse.solicitud=s.numero_solicitud) " +
						"INNER JOIN " +
							"cuentas c on (c.id=s.cuenta) " +
						"INNER JOIN " +
							"personas pac on(c.codigo_paciente=pac.codigo) " +
						"INNER JOIN " +
							"centro_atencion ca on (ca.consecutivo=f.centro_aten) "+
						"INNER JOIN " +
							"pooles po on (po.codigo=s.pool) " +
						"LEFT OUTER JOIN " + 
							"anulaciones_facturas af ON (af.consecutivo_factura = f.consecutivo_factura) ";
		
		
		// Consulta Paquetizacion
		String consultaPaquetizacion = "SELECT " +
							"pdf.pool as \"pool\", "+
							"po.descripcion as \"descpool\", "+
							"pdf.medico_asocio as \"codigo_medico_responde\", "+
							"f.consecutivo_factura as \"factura\", " +
							"pac.tipo_identificacion||' '||pac.numero_identificacion as \"tiponumidpac\", " +
							"getnombrepersona(pac.codigo) as \"nombrepac\", " +
							"case when pdf.tipo_solicitud=" +ConstantesBD.codigoTipoSolicitudCirugia+" then "+
							"getnombreservicio(pdf.servicio_cx, "+ConstantesBD.codigoTarifarioCups+") " +
							" else " +
								"getnombreservicio(pdf.servicio, "+ConstantesBD.codigoTarifarioCups+")  " +
							"end as \"servicio\", " + 	
							"getnombreconvenio(f.convenio) as \"convenio\", " +
							"f.fecha as \"fecha\", " +
							"getnomcentroatencion(f.centro_aten) as \"centroatencion\", ";
							
		if(filtros.get("valorFacturaCero").toString().equals(ConstantesBD.acronimoSi))
			consultaPaquetizacion += 			"0 as \"valorpool\", ";
		else
			consultaPaquetizacion += 			"pdf.valor_pool as \"valorpool\", ";
							
		consultaPaquetizacion += "getnombrepersona(cast (pdf.medico_asocio as integer)) as \"profesional\"," +
							"s.consecutivo_ordenes_medicas as \"nroorden\", " +
							"to_date('"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicial")+"")+"', 'yyyy-mm-dd') AS \"fecha_inicial_parametro\", " +
							"to_date('"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"', 'yyyy-mm-dd') AS \"fecha_final_parametro\", ";
						
		if(filtros.get("anuladasXFechaSinCodigo").toString().equals(ConstantesBD.acronimoSi)){
			consultaPaquetizacion += "CASE WHEN ( to_char(af.fecha_grabacion, 'YYYY-MM-DD') between '" + 
									UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicial")+"")+"' and '" + 
									UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"' " +
									") " +  
									"THEN af.consecutivo_anulacion ELSE '' END AS \"consecutivo_anulacion\" ";
		} else if(filtros.get("anuladasSinCodigo").toString().equals(ConstantesBD.acronimoSi)){
			consultaPaquetizacion += " '' AS \"consecutivo_anulacion\" ";
		} else {
			consultaPaquetizacion += " af.consecutivo_anulacion AS \"consecutivo_anulacion\" ";
		}					
		
		consultaPaquetizacion +="FROM " +
							"facturas f " +
						"INNER JOIN " +
							"det_factura_solicitud dfs on (f.codigo=dfs.factura) " +
						"INNER JOIN " +
							"paquetizacion_det_factura pdf on (pdf.codigo_det_fact = dfs.codigo) " +
						"INNER JOIN " +
							"solicitudes s on(pdf.solicitud=s.numero_solicitud) " +
						"INNER JOIN " +
							"cuentas c on (c.id=s.cuenta) " +
						"INNER JOIN " +
							"personas pac on(c.codigo_paciente=pac.codigo) " +
						"INNER JOIN " +
							"centro_atencion ca on (ca.consecutivo=f.centro_aten) "+
						"INNER JOIN " +
							"pooles po on (po.codigo=pdf.pool) " +
						"LEFT OUTER JOIN " + 
							"anulaciones_facturas af ON (af.consecutivo_factura = f.consecutivo_factura) ";
		
		// Consulta Paquetizacion
		String consultacx = "SELECT " +
							"adf.pool as \"pool\", "+
							"po.descripcion as \"descpool\", "+
							"adf.codigo_medico as \"codigo_medico_responde\", "+
							"f.consecutivo_factura as \"factura\", " +
							"pac.tipo_identificacion||' '||pac.numero_identificacion as \"tiponumidpac\", " +
							"getnombrepersona(pac.codigo) as \"nombrepac\", " +
							"getnombreservicio(dfs.servicio, "+ConstantesBD.codigoTarifarioCups+") as \"servicio\", " +
							"getnombreconvenio(f.convenio) as \"convenio\", " +
							"f.fecha as \"fecha\", " +
							"getnomcentroatencion(f.centro_aten) as \"centroatencion\", ";
							
		if(filtros.get("valorFacturaCero").toString().equals(ConstantesBD.acronimoSi))
			consultacx += 			"0 as \"valorpool\", ";
		else
			consultacx += 			"adf.valor_pool as \"valorpool\", ";
							
		consultacx += "getnombrepersona(adf.codigo_medico) as \"profesional\"," +
							"s.consecutivo_ordenes_medicas as \"nroorden\", " +
							"to_date('"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicial")+"")+"', 'yyyy-mm-dd') AS \"fecha_inicial_parametro\", " +
							"to_date('"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"', 'yyyy-mm-dd') AS \"fecha_final_parametro\", " ;
		
		if(filtros.get("anuladasXFechaSinCodigo").toString().equals(ConstantesBD.acronimoSi)){
			consultacx += "CASE WHEN ( to_char(af.fecha_grabacion, 'YYYY-MM-DD') between '" + 
									UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicial")+"")+"' and '" + 
									UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"' " +
									") " +  
									"THEN af.consecutivo_anulacion ELSE '' END AS \"consecutivo_anulacion\" ";
		} else if(filtros.get("anuladasSinCodigo").toString().equals(ConstantesBD.acronimoSi)){
			consultacx += " '' AS \"consecutivo_anulacion\" ";
		} else {
			consultacx += " af.consecutivo_anulacion AS \"consecutivo_anulacion\" ";
		}							
							
		consultacx += "FROM " +
							"facturas f " +
						"INNER JOIN " +
							"det_factura_solicitud dfs on (f.codigo=dfs.factura) " +
						"INNER JOIN " +
							"asocios_det_factura adf on (adf.codigo = dfs.codigo) " +
						"INNER JOIN " +
							"solicitudes s on(dfs.solicitud=s.numero_solicitud) " +
						"INNER JOIN " +
							"cuentas c on (c.id=s.cuenta) " +
						"INNER JOIN " +
							"personas pac on(c.codigo_paciente=pac.codigo) " +
						"INNER JOIN " +
							"centro_atencion ca on (ca.consecutivo=f.centro_aten) "+
						"INNER JOIN " +
							"pooles po on (po.codigo=adf.pool) " +
						"LEFT OUTER JOIN " + 
							"anulaciones_facturas af ON (af.consecutivo_factura = f.consecutivo_factura) ";
		
		//Consulta Cirugia Facturas Excentas
		String consultaExcentasCX = 
							"SELECT " +
							"adfe.pool as \"pool\", "+
							"po.descripcion as \"descpool\", "+
							"adfe.codigo_medico as \"codigo_medico_responde\", "+
							"f.consecutivo_factura as \"factura\", " +
							"pac.tipo_identificacion||' '||pac.numero_identificacion as \"tiponumidpac\", " +
							"getnombrepersona(pac.codigo) as \"nombrepac\", " +
							"getnombreservicio(adfe.servicio_asocio, "+ConstantesBD.codigoTarifarioCups+") as \"servicio\", " +
							"getnombreconvenio(f.convenio) as \"convenio\", " +
							"f.fecha as \"fecha\", " +
							"getnomcentroatencion(f.centro_aten) as \"centroatencion\", ";
		
		if(filtros.get("valorFacturaCero").toString().equals(ConstantesBD.acronimoSi))
			consultaExcentasCX += 			"0 as \"valorpool\", ";
		else
			consultaExcentasCX += 			"adfe.valor_pool as \"valorpool\", ";
				
		consultaExcentasCX += "getnombrepersona(adfe.codigo_medico) as \"profesional\"," +
							"s.consecutivo_ordenes_medicas as \"nroorden\", " +
							"to_date('"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicial")+"")+"', 'yyyy-mm-dd') AS \"fecha_inicial_parametro\", " +
							"to_date('"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"', 'yyyy-mm-dd') AS \"fecha_final_parametro\", " ;
						
		if(filtros.get("anuladasXFechaSinCodigo").toString().equals(ConstantesBD.acronimoSi)){
			consultaExcentasCX += "CASE WHEN ( to_char(af.fecha_grabacion, 'YYYY-MM-DD') between '" + 
									UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicial")+"")+"' and '" + 
									UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"' " +
									") " +  
									"THEN af.consecutivo_anulacion ELSE '' END AS \"consecutivo_anulacion\" ";
		} else if(filtros.get("anuladasSinCodigo").toString().equals(ConstantesBD.acronimoSi)){
			consultaExcentasCX += " '' AS \"consecutivo_anulacion\" ";
		} else {
			consultaExcentasCX += " af.consecutivo_anulacion AS \"consecutivo_anulacion\" ";
		}						
							
		consultaExcentasCX +="FROM " +
							"facturas f " +
						"INNER JOIN " +
							"facturacion.det_factura_solicitud_excenta dfse on (f.codigo=dfse.factura) " +
						"INNER JOIN " +
							"facturacion.asocios_det_factura_excenta adfe on (adfe.codigo = dfse.codigo) " +
						"INNER JOIN " +
							"solicitudes s on(dfse.solicitud=s.numero_solicitud) " +
						"INNER JOIN " +
							"cuentas c on (c.id=s.cuenta) " +
						"INNER JOIN " +
							"personas pac on(c.codigo_paciente=pac.codigo) " +
						"INNER JOIN " +
							"centro_atencion ca on (ca.consecutivo=f.centro_aten) "+
						"INNER JOIN " +
							"pooles po on (po.codigo=adfe.pool) " +
						"LEFT OUTER JOIN " + 
							"anulaciones_facturas af ON (af.consecutivo_factura = f.consecutivo_factura) ";
				
		
		if (filtros.get("mostrarFacturasAnuladas").toString().equals(ConstantesBD.acronimoNo) &&
				filtros.get("anuladasSinCodigo").toString().equals(ConstantesBD.acronimoNo))
			condicionesGenerales += " and f.estado_facturacion != "+ConstantesBD.codigoEstadoFacturacionAnulada+" ";
		
		if (filtros.get("anuladasSinCodigo").toString().equals(ConstantesBD.acronimoSi) || 
				(filtros.get("mostrarFacturasAnuladas").toString().equals(ConstantesBD.acronimoSi) && 
						filtros.get("valorFacturaCero").toString().equals(ConstantesBD.acronimoSi)))
			condicionesGenerales += " and f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionAnulada+" ";

		if(!(filtros.get("pool")+"").equals("-1")){
			condicionesEspecificas += " and s.pool="+filtros.get("pool")+" ";
			condicionesEspecificasExcentas += " and s.pool="+filtros.get("pool")+" ";
			condicionesPaquetizacion += " and pdf.pool="+filtros.get("pool")+" ";
			condicionescx += " and adf.pool="+filtros.get("pool")+" ";
			condicionesExentasCX += " and adfe.pool="+filtros.get("pool")+" ";
		}	 
	
		if(!filtros.get("profesional").toString().equals("-1")){
			if (filtros.get("soloAnuladasXProfesional").toString().equals(ConstantesBD.acronimoNo)) {
				condicionesEspecificas += " and s.codigo_medico_responde="+filtros.get("profesional")+" "; 
				condicionesEspecificasExcentas += " and s.codigo_medico_responde="+filtros.get("profesional")+" ";
				condicionesPaquetizacion += " and pdf.medico_asocio="+filtros.get("profesional")+" ";
			}
			condicionescx += " and adf.codigo_medico="+filtros.get("profesional")+" ";
			condicionesExentasCX += " and adfe.codigo_medico="+filtros.get("profesional")+" ";
		}	
			
		if(!filtros.get("centroAtencion").toString().equals("-1"))
			condicionesGenerales += " and f.centro_aten="+filtros.get("centroAtencion")+" "; 

		
		if(filtros.get("fechaInicial")!=null&&!(filtros.get("fechaInicial")+"").equals("null")&&!(filtros.get("fechaInicial")+"").equals(""))
		{
			if(filtros.get("soloAnuladasXProfesional").toString().equals(ConstantesBD.acronimoSi))
			{
				condicionesGenerales += "  and  ( " +
										" to_char(af.fecha_grabacion, 'YYYY-MM-DD') between '"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"' " +
										") ";
			}
			else
			{
				condicionesGenerales += "  and  ( " +
												"to_char(f.fecha, 'yyyy-mm-dd') between '"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"' " +
												"OR to_char(af.fecha_grabacion, 'YYYY-MM-DD') between '"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"' " +
										") ";
			}
			/**
			 * Se realiza una modificación a la consulta para eliminar la restricción de busqueda 
			 * de facturas por fecha de anulación
			 * MT-1872 - diecorqu
			 */
			/*condicionesGenerales += "  and  ( " +
											"to_char(f.fecha, 'yyyy-mm-dd') between '"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"' " +
											") ";*/
		}
		
		if(filtros.get("anuladasXFecha").toString().equals(ConstantesBD.acronimoSi))
			condicionesGenerales += "  and  ( " +
									" to_char(af.fecha_grabacion, 'YYYY-MM-DD') between '" + 
									UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicial")+"")+"' and '" + 
									UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"' " +
									") ";
		
		if(filtros.get("anuladasSinCodigo").toString().equals(ConstantesBD.acronimoSi))
			condicionesGenerales += "  and  ( " +
									" to_char(af.fecha_grabacion, 'YYYY-MM-DD') > '" + 
									UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"' " +
									") ";
		
		if(filtros.get("facturaInicial")!=null&&!(filtros.get("facturaInicial")+"").equals("null")&&!(filtros.get("facturaInicial")+"").equals(""))
			condicionesGenerales += "  and f.consecutivo_factura between "+filtros.get("facturaInicial")+" and "+filtros.get("facturaFinal")+" ";
	
		if(filtros.get("institucionSel")!=null&&!(filtros.get("institucionSel")+"").equals("null")&&!(filtros.get("institucionSel")+"").equals("")&&!(filtros.get("institucionSel")+"").equals("-1"))
			condicionesGenerales += "  and f.institucion= "+filtros.get("institucionSel")+" ";
		
		if(!(filtros.get("nroOrden")+"").equals(""))
			condicionesGenerales += " and s.consecutivo_ordenes_medicas="+filtros.get("nroOrden")+" ";
	
		if(!(filtros.get("ciudad")+"").equals("-1") && !(filtros.get("ciudad")+"").equals(""))
			condicionesGenerales += " and ca.ciudad='"+filtros.get("ciudad")+"' ";
	
		if(!(filtros.get("pais")+"").equals("-1") && !(filtros.get("pais")+"").equals(""))
			condicionesGenerales += " and ca.pais='"+filtros.get("pais")+"' ";
		
		if(!(filtros.get("region")+"").equals("-1") && !(filtros.get("region")+"").equals("") && !UtilidadTexto.isEmpty(filtros.get("region")+""))
			condicionesGenerales += " and ca.region_cobertura='"+filtros.get("region")+"' ";
		
		if(filtros.get("soloAnuladasXProfesional").toString().equals(ConstantesBD.acronimoSi)){
			condicionesEspecificas += " and f.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionAnulada+" AND s.codigo_medico_responde=? ";
			condicionesEspecificasExcentas += " and f.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionAnulada+" AND s.codigo_medico_responde=? ";
			condicionesPaquetizacion += " and f.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionAnulada+" AND pdf.medico_asocio=? ";
			condicionescx += " and f.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionAnulada+" AND adf.codigo_medico=? ";
			condicionesExentasCX += " and f.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionAnulada+" AND adfe.codigo_medico=? ";
		}	
		
		if(!(filtros.get("especialidad")+"").equals("-1")) 
			condicionesGenerales += " and s.especialidad_solicitada="+filtros.get("especialidad")+" ";
		
		String sql = 	"SELECT * FROM (" +
							"("+
								consultaEspecifica+condicionesGenerales+condicionesEspecificas+
							") UNION ("+
								consultaEspecificaExcentas+condicionesGenerales+condicionesEspecificasExcentas+
							") UNION ("+
								consultaPaquetizacion+condicionesGenerales+condicionesPaquetizacion+
							") UNION ("+
								consultacx+condicionesGenerales+condicionescx+
							") UNION ("+
								consultaExcentasCX+condicionesGenerales+condicionesExentasCX+
							")"+
						") datos";
		
		//sql += " ORDER BY getnombrepersona(s.codigo_medico_responde), f.consecutivo_factura ";
		
		return sql;
	}
	
	
		
		/**
		 * 
		 * @param codigo
		 * @return
		 */
		public static String consultarNotasPorCodigo(int codigo)
		{
			
			String consulta=
			" select codigo_pk as  codigo  ,"+       
			"usuario_modifica as usuario,"+ 
			"ingreso as  ingreso ,"+           
			"fecha_modifica as fecha,"+  
			"hora_modifica as hora ," + 
			"descripcion_nota as descripcion "+  
			" from  administracion.notas_administrativas where codigo_pk ="+codigo;
			return consulta;
		}

	
		
		/**
		 * 
		 * Impresión Factura Varia
		 */
		public static String impresionFacturaVaria(int institucion,
			int consecutivofacvar, boolean manejaMultiInstitucion) {
			
			//if (manejaMultiInstitucion) {
				String consulta=
				" select " +
					"he.nombre_institucion AS razonSocial,"+
					"he.tipo_identificacion_inst || ' ' || he.nit_institucion || '-' || he.digito_verificacion  as nit,"+
					"he.sucursal AS centroatencion, "+
					"coalesce(he.prefijo_factura, '')||' '||f.consecutivo as codigofactura, "+
					"to_char(f.fecha,'dd/mm/yyyy') as fechageneracion, "+ 
					"he.resolucion as resolucion, "+
					"he.encabezado as encabezado, "+
					"f.hora_modifica as horageneracion, "+
					"getnombreusuario(f.usuario_modifica) as nombreusuario, "+
					"getdeudor(f.deudor) AS responsable, "+
					"case when d.codigo_empresa IS NOT NULL THEN e.direccion WHEN d.codigo_tercero IS NOT NULL THEN d.direccion WHEN d.codigo_paciente IS NOT NULL THEN p.direccion END AS direccion,"+ 
					"case when d.codigo_empresa IS NOT NULL THEN e.telefono WHEN d.codigo_tercero IS NOT NULL THEN d.telefono WHEN d.codigo_paciente IS NOT NULL THEN (coalesce(p.telefono,'')||' '||coalesce(p.telefono_celular||'','')) END AS telefono,"+ 
					"getidentificaciondeudor(f.deudor) as numeroidentificacion, "+
					"getintegridaddominio(f.estado_factura) as estadofactura, "+ 
					"cf.descripcion as descripcionconcepto, "+ 
					"cf.codigo as codconcep, "+ 	
					"f.observaciones as observaciones, "+ 
					"f.valor_factura as valorfactura, "+ 
					"convertirletrasconcentavos(f.valor_factura,'Pesos','Centavos') || ' MCTE' as valorletras, "+
					"case when he.prefijo_factura IS NULL THEN coalesce(he.rango_inicial||'','') else he.prefijo_factura ||'-'||coalesce(he.rango_inicial||'','') END AS rif, "+
					"case when he.prefijo_factura IS NULL THEN coalesce(he.rango_final||'','') else he.prefijo_factura ||'-'||coalesce(he.rango_final||'','') END AS rff, "+ 
					"he.actividad_economica as actividadeconomica, "+
					"he.pie_pagina as pie, "+
					"getnombreusuario(f.usuario_modifica)||' '||to_char(CURRENT_TIMESTAMP, 'dd/mm/yyyy HH24:MI') as usuariofechahoraimpresion "+
				"FROM facturas_varias f "+ 
				"INNER JOIN deudores d on(d.codigo=f.deudor) "+
				"INNER JOIN centro_atencion c on (c.consecutivo=f.centro_atencion) "+
				"LEFT OUTER JOIN terceros t on(t.codigo=d.codigo_tercero) "+ 
				"LEFT OUTER JOIN empresas e on(e.codigo=d.codigo_empresa) "+  
				"LEFT OUTER JOIN personas p on(p.codigo=d.codigo_paciente) "+
				"INNER JOIN conceptos_facturas_varias cf on(cf.consecutivo=f.concepto) "+
				"LEFT OUTER JOIN facturacion.historico_encabezado he ON (he.codigo_pk = f.historico_encabezado) "+
				"WHERE f.consecutivo = ?";
				
				return consulta;
//			}
//			
//			else {
//					String consulta=
//						" select " +
//							"i.razon_social as razonsocial, "+
//							"coalesce(getnombretipoidentificacion (i.tipo_identificacion), '') || ' '|| i.nit || '-'|| i.digito_verificacion  as nit,"+							"getnomcentroatencion(f.centro_atencion) as centroatencion, "+
//							"coalesce(i.pref_factura, '')||' '||f.consecutivo as codigofactura, "+
//							"to_char(f.fecha_modifica,'dd/mm/yyyy') as fechageneracion, "+ 
//							"f.hora_modifica as horageneracion, "+
//							"i.resolucion as resolucion, "+
//							"i.encabezado as encabezado, "+
//							"getnombreusuario(f.usuario_modifica) as nombreusuario, "+ 
//							"getdeudor(f.deudor) AS responsable, "+
//							"case when d.codigo_empresa IS NOT NULL THEN e.direccion WHEN d.codigo_tercero IS NOT NULL THEN d.direccion WHEN d.codigo_paciente IS NOT NULL THEN p.direccion END AS direccion, "+ 
//							"case when d.codigo_empresa IS NOT NULL THEN e.telefono WHEN d.codigo_tercero IS NOT NULL THEN d.telefono WHEN d.codigo_paciente IS NOT NULL THEN (coalesce(p.telefono,'')||' '||coalesce(p.telefono_celular||'','')) END AS telefono, "+ 
//							"getidentificaciondeudor(f.deudor) as numeroidentificacion, "+
//							"getintegridaddominio(f.estado_factura) as estadofactura, "+ 
//							"cf.descripcion as descripcionconcepto, "+ 
//							"cf.codigo as codconcep, "+ 	
//							"f.observaciones as observaciones, "+ 
//							" to_char(f.valor_factura, '999999999999.00') as valorfactura, "+ 							"convertirletras(f.valor_factura) || ' pesos MCTE ' as valorletras, "+
//							"case when i.pref_factura IS NULL THEN coalesce(i.rgo_inic_fact||'','') else i.pref_factura ||'-'||coalesce(i.rgo_inic_fact||'','') END AS rif, "+
//							"case when i.pref_factura IS NULL THEN coalesce(i.rgo_fin_fact||'','') else i.pref_factura ||'-'||coalesce(i.rgo_fin_fact||'','') END AS rff, "+ 
//							"i.actividad_eco as actividadeconomica ,"+
//							"i.pie as pie, "+
//							"getnombreusuario(f.usuario_modifica)||' '||to_char(CURRENT_TIMESTAMP, 'dd/mm/yyyy HH24:MI') as usuariofechahoraimpresión "+
//						"FROM facturas_varias f "+ 
//						"INNER JOIN deudores d on(d.codigo=f.deudor) "+
//						"LEFT OUTER JOIN terceros t on(t.codigo=d.codigo_tercero) "+ 
//						"LEFT OUTER JOIN empresas e on(e.codigo=d.codigo_empresa) "+  
//						"LEFT OUTER JOIN personas p on(p.codigo=d.codigo_paciente) "+
//						"INNER JOIN conceptos_facturas_varias cf on(cf.consecutivo=f.concepto) "+
//						"INNER JOIN instituciones i on (i.codigo=f.institucion) "+
//						"WHERE f.consecutivo = ? AND i.codigo=?	";
//						
//					return consulta;
//		}
		
		
	}
		
		

		/**
		 * 
		 * @param idCuenta
		 * @return
		 */
		public static String consultaUltimosSignosRE(String idCuenta) {
			String consulta="SELECT " +
								" svre.fc||' /min'   AS fc,  " +
								" svre.fr||' /min'   AS fr,  " +
								" svre.pas||' mmHg'  AS pas,  " +
								" svre.pad||' mmHg'  AS pad,   " +
								" svre.pam||' mmHg'  AS pam,   " +
								" svre.temp||' °C' AS temp " +
							" FROM enca_histo_registro_enfer ehre " +
							" INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo) " +
							" INNER JOIN signo_vital_reg_enfer svre ON (ehre.codigo  =svre.codigo_histo_enfer) " +
							" WHERE ehre.codigo=(select max(ehre.codigo) from enca_histo_registro_enfer  ehre INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo) where re.cuenta = "+idCuenta+") "; 
			return consulta;
		}
		
		/**
		 * Metodo que devuelve la consulta del DATASET RompimientoPiso
		 * @return
		 */
		//MT6527 se modifica Join para el numero _solicitud agrega join para el filtro de responsable
		public static String consultarRompimientoPiso(){
			String consulta  = "SELECT    "+ 
					"s.centro_costo_solicitante as codigo_centro_costo, "+      
					"getnomcentrocosto(s.centro_costo_solicitante) as centro_costo,   "+   
					"getConvenioSubcuenta(jar.subcuenta) as codigo_convenio,  "+ 
					"getnombreconvenioresponsable(getConvenioSubcuenta(jar.subcuenta), i.id) as convenio, "+      
					"s.numero_solicitud as solicitud,     "+     
					"s.fecha_solicitud as fecha,  "+       
					"getdescarticulosincodigo(jas.articulo) as articulo,  "+     
					"i.id as ingreso,      "+    
					"jar.subcuenta as subcuenta, "+      
					"getSubcuentaCantidadJus(s.numero_solicitud) as subcuentacantidad,  "+ 
					"c.via_ingreso as codviaingreso,  "+ 
					"c.tipo_paciente as codtipopaciente,     "+ 
					"case when getcuantosTipoPacViaIngreso(c.via_ingreso)>1          then getnombreviaingreso(c.via_ingreso)||' - '||getnombretipopaciente(c.tipo_paciente)    else getnombreviaingreso(c.via_ingreso)||'' end  As viaingresotipopac,  "+         
					"i.fecha_ingreso || '/' || i.hora_ingreso as fechahora,    "+  
					"i.consecutivo as noingreso,      "+ 
					"coalesce(getValorTotalCargoAsociado(s.numero_solicitud,jas.articulo,jar.subcuenta)||'','PENDIENTE') as valor,    "+   
					"getCantidadTotalCargoAsociado(s.numero_solicitud,jas.articulo,jar.subcuenta) as cantidadcargada,       "+   
					"getidpaciente(i.codigo_paciente) as tipoid,      "+ 
					"getnombrepersona(i.codigo_paciente) as nombrepac,  "+ 
					"jas.articulo as codigoart,    "+    
					"c.id as codigocuenta,    "+ 
					"i.codigo_paciente as codpaciente,        "+ 
					"getcamacuenta(c.id,c.via_ingreso) as cama,  "+      
					"jas.consecutivo as nojus,  "+ 
					"jar.cantidad as cantotorden,   "+   
					"getdespacho(jas.articulo, sja.numero_solicitud) as cantotdespacho,  "+      
					"gettotaladminfarmacia(jas.articulo, sja.numero_solicitud,"+ ValoresPorDefecto.getValorFalseParaConsultas()+") as cantotadmin,   "+  
					"jas.tiempo_tratamiento as tiempotratamiento,    "+ 
					"gettotaladminfarresponsable(sja.numero_solicitud,jas.articulo,jar.subcuenta) as cantotconv, "+      
					"getValorTotalCargoAsociado(s.numero_solicitud,jas.articulo,jar.subcuenta) * gettotaladminfarresponsable(sja.numero_solicitud,jas.articulo,jar.subcuenta) as precioventot,  "+ 
					"getcostounitario(sja.numero_solicitud, s.tipo,jas.articulo) as costounitario,   "+    
					"gettotaladminfarmacia(jas.articulo,sja.numero_solicitud,"+ ValoresPorDefecto.getValorFalseParaConsultas()+") * getcostounitario(sja.numero_solicitud, s.tipo,jas.articulo) as preciocostot, "+     
					"jas.articulo as codigo_art,  "+     
					"getnombrepiso(getcodigopisocuenta(c.id,c.via_ingreso)) as piso,  "+        
					"getintegridaddominio(jar.estado) as estadojus ,  "+ 
					"CASE WHEN art.categoria=1 THEN 'NO' WHEN art.categoria=2 THEN 'SI' ELSE '' END AS control "+
				"FROM    "+ 
					"ingresos i   "+ 
				"INNER JOIN    "+  
					"cuentas c on (c.id_ingreso=i.id)  "+  
				"INNER JOIN     "+ 
					"solicitudes s on (c.id=s.cuenta)   "+ 
				"INNER JOIN     "+ 
					"sub_cuentas sc on (sc.ingreso=i.id)  "+  
				//MT6527
				"INNER JOIN         "+ 
					"sol_x_just_art_sol sja  on(s.numero_solicitud=sja.numero_solicitud)  "+
				"INNER JOIN         "+  
					"justificacion_art_sol jas on (sja.codigo_justificacion=jas.codigo)   "+ 
				"INNER JOIN     "+     
					"justificacion_art_resp jar on (jas.codigo=jar.justificacion_art_sol) "+   
				"INNER JOIN         "+ 
					"articulo art on (art.codigo=jas.articulo)  "+  
				"INNER JOIN    "+
					"justificacion_art_fijo jaf on (jas.codigo=jaf.justificacion_art_sol)  "+
				"WHERE     "+ 
					"c.estado_cuenta!=4  "+ 
					"and sc.nro_prioridad=1  "+ 
					"and s.estado_historia_clinica!=4  "+  
					"and 1=2 "+ 
					"and 2=2 "+ 
					"and 3=3 "+ 
					"and 4=4 "+ 
					"and 5=5 "+ 
					"and 6=6 "+ 
					"and 7=7 "+ 
					"and 8=8 "+ 
					"and 9=9 "+ 
					"and 10=10 "+ 
				"GROUP BY  "+ 
					"getnombrepiso(getcodigopisocuenta(c.id,c.via_ingreso)), "+ 
					"sc.convenio, "+ 
					"s.centro_costo_solicitante, "+ 
					"jar.estado, "+ 
					"s.numero_solicitud, "+ 
					"s.fecha_solicitud, "+ 
					"i.id , "+ 
					"sc.sub_cuenta , "+ 
					"c.via_ingreso , "+ 
					"c.tipo_paciente, "+ 
					"i.consecutivo, "+ 
					"jas.articulo, "+ 
					"c.id, "+ 
					"i.codigo_paciente, "+ 
					"jas.consecutivo, "+ 
					"jar.cantidad, "+ 
					"jas.tiempo_tratamiento, "+ 
					"i.fecha_ingreso, "+ 
					"i.hora_ingreso, "+ 
					"sja.numero_solicitud, "+ 
					"s.tipo, "+ 
					"jar.subcuenta, "+
					"art.categoria "+
				"ORDER BY "+ 
					"getnombrepiso(getcodigopisocuenta(c.id,c.via_ingreso)), "+ 
					"getnombreconvenioresponsable(getConvenioSubcuenta(jar.subcuenta), i.id),   "+ 
					"i.fecha_ingreso || '/' || i.hora_ingreso ";
			
			return consulta;
		}
		
		/**
		 * Metodo que devuelve la consulta del DATASET RompimientoPiso1
		 * @return
		 */
		//MT6527 se modifica Join para el numero _solicitud agrega join para el filtro de responsable
		public static String consultarRompimientoPiso1(){
			String consulta= "SELECT     "+
					"s.centro_costo_solicitante as codigo_centro_costo,   "+    
					"getnomcentrocosto(s.centro_costo_solicitante) as centro_costo,  "+   
					"getConvenioSubcuenta(jar.subcuenta) as codigo_convenio,  "+
					"getnombreconvenioresponsable(getConvenioSubcuenta(jar.subcuenta), i.id) as convenio,   "+   
					"s.numero_solicitud as solicitud,     "+    
					"s.fecha_solicitud as fecha,   "+     
					"getdescarticulosincodigo(jas.articulo) as articulo, "+     
					"i.id as ingreso,  "+       
					"jar.subcuenta as subcuenta,    "+  
					"getSubcuentaCantidadJus(s.numero_solicitud) as subcuentacantidad,  "+
					"c.via_ingreso as codviaingreso,  "+
					"c.tipo_paciente as codtipopaciente,   "+  
					"case when getcuantosTipoPacViaIngreso(c.via_ingreso)>1          then getnombreviaingreso(c.via_ingreso)||' - '||getnombretipopaciente(c.tipo_paciente)    else getnombreviaingreso(c.via_ingreso)||'' end  As viaingresotipopac,   "+       
					"i.fecha_ingreso || '/' || i.hora_ingreso as fechahora,  "+   
					"i.consecutivo as noingreso,      "+
					"coalesce(getValorTotalCargoAsociado(s.numero_solicitud,jas.articulo,jar.subcuenta)||'','PENDIENTE') as valor,    "+  
					"getCantidadTotalCargoAsociado(s.numero_solicitud,jas.articulo,jar.subcuenta) as cantidadcargada,     "+    
					"getidpaciente(i.codigo_paciente) as tipoid,      "+
					"getnombrepersona(i.codigo_paciente) as nombrepac,  "+
					"jas.articulo as codigoart,  "+     
					"c.id as codigocuenta,    "+
					"i.codigo_paciente as codpaciente,      "+  
					"getcamacuenta(c.id,c.via_ingreso) as cama,    "+   
					"jas.consecutivo as nojus,  "+
					"jar.cantidad as cantotorden,  "+   
					"getdespacho(jas.articulo, sja.numero_solicitud) as cantotdespacho,  "+     
					"gettotaladminfarmacia(jas.articulo, sja.numero_solicitud,"+ ValoresPorDefecto.getValorFalseParaConsultas()+") as cantotadmin,  "+  
					"jas.tiempo_tratamiento as tiempotratamiento,    "+
					"gettotaladminfarresponsable(sja.numero_solicitud,jas.articulo,jar.subcuenta) as cantotconv,    "+  
					"getValorTotalCargoAsociado(s.numero_solicitud,jas.articulo,jar.subcuenta) * gettotaladminfarresponsable(sja.numero_solicitud,jas.articulo,jar.subcuenta) as precioventot,  "+
					"getcostounitario(sja.numero_solicitud, s.tipo,jas.articulo) as costounitario,  "+    
					"gettotaladminfarmacia(jas.articulo,sja.numero_solicitud,"+ ValoresPorDefecto.getValorFalseParaConsultas()+") * getcostounitario(sja.numero_solicitud, s.tipo,jas.articulo) as preciocostot,   "+  
					"jas.articulo as codigo_art,   "+   
					"getnombrepiso(getcodigopisocuenta(c.id,c.via_ingreso)) as piso,   "+      
					"getintegridaddominio(jar.estado) as estadojus,    "+
					"CASE WHEN art.categoria=1 THEN 'NO' WHEN art.categoria=2 THEN 'SI' ELSE '' END AS control "+
					"FROM    "+ 
					"ingresos i   "+ 
				"INNER JOIN    "+  
					"cuentas c on (c.id_ingreso=i.id)  "+  
				"INNER JOIN     "+ 
					"solicitudes s on (c.id=s.cuenta)   "+ 
				"INNER JOIN     "+ 
					"sub_cuentas sc on (sc.ingreso=i.id)  "+  
				//MT6527
				"INNER JOIN         "+ 
				"sol_x_just_art_sol sja  on(s.numero_solicitud=sja.numero_solicitud)  "+
				"INNER JOIN         "+  
					"justificacion_art_sol jas on (sja.codigo_justificacion=jas.codigo)   "+ 
				"INNER JOIN     "+     
					"justificacion_art_resp jar on (jas.codigo=jar.justificacion_art_sol) "+   
				"INNER JOIN         "+ 
					"articulo art on (art.codigo=jas.articulo)  "+  
				"INNER JOIN    "+
					"justificacion_art_fijo jaf on (jas.codigo=jaf.justificacion_art_sol)  "+
				"WHERE     "+ 
					"c.estado_cuenta!=4  "+ 
					"and sc.nro_prioridad=1  "+ 
					"and s.estado_historia_clinica!=4  "+  
					"and 1=2 "+ 
					"and 2=2 "+ 
					"and 3=3 "+ 
					"and 4=4 "+ 
					"and 5=5 "+ 
					"and 6=6 "+ 
					"and 7=7 "+ 
					"and 8=8 "+ 
					"and 9=9 "+ 
					"and 10=10 "+ 
				"GROUP BY  "+ 
					"getnombrepiso(getcodigopisocuenta(c.id,c.via_ingreso)), "+
					"sc.convenio, "+
					"s.centro_costo_solicitante, "+
					"jar.estado, "+
					"s.numero_solicitud, "+
					"s.fecha_solicitud, "+
					"i.id , "+
					"sc.sub_cuenta , "+
					"c.via_ingreso , "+
					"c.tipo_paciente, "+
					"i.consecutivo, "+
					"jas.articulo, "+
					"c.id, "+
					"i.codigo_paciente, "+
					"jas.consecutivo, "+
					"jar.cantidad, "+
					"jas.tiempo_tratamiento, "+
					"i.fecha_ingreso, "+
					"i.hora_ingreso, "+
					"sja.numero_solicitud, "+
					"s.tipo, "+
					"jar.subcuenta, "+
					"art.categoria "+
				"ORDER BY "+
					"getnombrepiso(getcodigopisocuenta(c.id,c.via_ingreso)), "+
					"getnombreconvenioresponsable(getConvenioSubcuenta(jar.subcuenta), i.id),  "+ 
					"i.fecha_ingreso || '/' || i.hora_ingreso ";
			
			return consulta;
		}
		
		/**
		 * Metodo que devuelve la consulta del DATASET RompimientoCentroCosto
		 * @return
		 */
		//MT6527 se modifica Join para el numero _solicitud agrega join para el filtro de responsable
		public static String consultarRompimientoCentroCosto(){
			String consulta= "SELECT     "+
					"s.centro_costo_solicitante as codigo_centro_costo,    "+   
					"getnomcentrocosto(s.centro_costo_solicitante) as centro_costo,  "+   
					"getConvenioSubcuenta(jar.subcuenta) as codigo_convenio,  "+
					"getnombreconvenioresponsable(getConvenioSubcuenta(jar.subcuenta), i.id) as convenio, "+     
					"s.numero_solicitud as solicitud,         "+
					"s.fecha_solicitud as fecha,    "+    
					"getdescarticulosincodigo(jas.articulo) as articulo,   "+   
					"i.id as ingreso,         "+
					"jar.subcuenta as subcuenta,   "+   
					"getSubcuentaCantidadJus(s.numero_solicitud) as subcuentacantidad,  "+
					"c.via_ingreso as codviaingreso,  "+
					"c.tipo_paciente as codtipopaciente,  "+   
					"case when getcuantosTipoPacViaIngreso(c.via_ingreso)>1          then getnombreviaingreso(c.via_ingreso)||' - '||getnombretipopaciente(c.tipo_paciente)    else getnombreviaingreso(c.via_ingreso)||'' end  As viaingresotipopac,  "+        
					"i.fecha_ingreso || '/' || i.hora_ingreso as fechahora,     "+
					"i.consecutivo as noingreso,     "+ 
					"coalesce(getValorTotalCargoAsociado(s.numero_solicitud,jas.articulo,jar.subcuenta)||'','PENDIENTE') as valor,   "+   
					"getCantidadTotalCargoAsociado(s.numero_solicitud,jas.articulo,jar.subcuenta) as cantidadcargada,     "+    
					"getidpaciente(i.codigo_paciente) as tipoid,   "+   
					"getnombrepersona(i.codigo_paciente) as nombrepac,  "+
					"jas.articulo as codigoart,       "+
					"c.id as codigocuenta,    "+
					"i.codigo_paciente as codpaciente,     "+   
					"getcamacuenta(c.id,c.via_ingreso) as cama,   "+    
					"jas.consecutivo as nojus,  "+
					"jar.cantidad as cantotorden,  "+   
					"getdespacho(jas.articulo, sja.numero_solicitud) as cantotdespacho,  "+     
					"gettotaladminfarmacia(jas.articulo, sja.numero_solicitud,"+ ValoresPorDefecto.getValorFalseParaConsultas()+") as cantotadmin,   "+ 
					"jas.tiempo_tratamiento as tiempotratamiento,    "+
					"gettotaladminfarresponsable(sja.numero_solicitud,jas.articulo,jar.subcuenta) as cantotconv,   "+   
					"getValorTotalCargoAsociado(s.numero_solicitud,jas.articulo,jar.subcuenta) * gettotaladminfarresponsable(sja.numero_solicitud,jas.articulo,jar.subcuenta) as precioventot,  "+
					"getcostounitario(sja.numero_solicitud, s.tipo,jas.articulo) as costounitario,  "+    
					"gettotaladminfarmacia(jas.articulo,sja.numero_solicitud,"+ ValoresPorDefecto.getValorFalseParaConsultas()+") * getcostounitario(sja.numero_solicitud, s.tipo,jas.articulo) as preciocostot,   "+  
					"jas.articulo as codigo_art,      "+
					"getnombrepiso(getcodigopisocuenta(c.id,c.via_ingreso)) as piso,      "+   
					"getintegridaddominio(jar.estado) as estadojus,   "+
					"CASE WHEN art.categoria=1 THEN 'NO' WHEN art.categoria=2 THEN 'SI' ELSE '' END AS control "+
				"FROM    "+
					"ingresos i  "+ 
				"INNER JOIN     "+
					"cuentas c on (c.id_ingreso=i.id)   "+
				"INNER JOIN     "+
					"solicitudes s on (c.id=s.cuenta)   "+
				"INNER JOIN     "+
					"sub_cuentas sc on (sc.ingreso=i.id)   "+
				//MT6527
				"INNER JOIN         "+ 
					"sol_x_just_art_sol sja  on(s.numero_solicitud=sja.numero_solicitud)  "+
				"INNER JOIN         "+  
					"justificacion_art_sol jas on (sja.codigo_justificacion=jas.codigo)   "+ 
				"INNER JOIN         "+
					"justificacion_art_resp jar on (jas.codigo=jar.justificacion_art_sol) "+  
				"INNER JOIN         "+
					"articulo art on (art.codigo=jas.articulo)   "+
				"INNER JOIN    "+
					"justificacion_art_fijo jaf on (jas.codigo=jaf.justificacion_art_sol)  "+	
					"WHERE     "+ 
					"c.estado_cuenta!=4  "+ 
					"and sc.nro_prioridad=1  "+ 
					"and s.estado_historia_clinica!=4  "+  
					"and 1=2 "+ 
					"and 2=2 "+ 
					"and 3=3 "+ 
					"and 4=4 "+ 
					"and 5=5 "+ 
					"and 6=6 "+ 
					"and 7=7 "+ 
					"and 8=8 "+ 
					"and 9=9 "+ 
					"and 10=10 "+ 
				"GROUP BY  "+ 
					"s.centro_costo_solicitante, "+
					"sc.convenio, "+
					"jar.estado, "+
					"getnombrepiso(getcodigopisocuenta(c.id,c.via_ingreso)), "+
					"s.numero_solicitud, "+
					"s.fecha_solicitud, "+
					"i.id , "+
					"sc.sub_cuenta , "+
					"c.via_ingreso , "+
					"c.tipo_paciente, "+
					"i.consecutivo, "+
					"jas.articulo, "+
					"c.id, "+
					"i.codigo_paciente, "+
					"jas.consecutivo, "+
					"jar.cantidad, "+
					"jas.tiempo_tratamiento, "+
					"i.fecha_ingreso, "+
					"i.hora_ingreso, "+
					"sja.numero_solicitud, "+
					"s.tipo, "+
					"jar.subcuenta, "+
					"art.categoria "+
				"ORDER BY "+
					"getnomcentrocosto(s.centro_costo_solicitante), "+
					"getnombreconvenioresponsable(getConvenioSubcuenta(jar.subcuenta), i.id),  "+         
					"i.fecha_ingreso || '/' || i.hora_ingreso  ";
			
			return consulta ;
		}
		
		/**
		 * Metodo que devuelve la consulta del DATASET RompimientoCentroCosto1
		 * @return
		 */
		//MT6527 se modifica Join para el numero _solicitud agrega join para el filtro de responsable
		public static String consultarRompimientoCentroCosto1(){
			String consulta="SELECT   "+  
					"s.centro_costo_solicitante as codigo_centro_costo, "+     
					"getnomcentrocosto(s.centro_costo_solicitante) as centro_costo,     "+
					"getConvenioSubcuenta(jar.subcuenta) as codigo_convenio,  "+
					"getnombreconvenioresponsable(getConvenioSubcuenta(jar.subcuenta), i.id) as convenio,  "+    
					"s.numero_solicitud as solicitud,  "+       
					"s.fecha_solicitud as fecha,    "+    
					"getdescarticulosincodigo(jas.articulo) as articulo,  "+    
					"i.id as ingreso,         "+
					"jar.subcuenta as subcuenta,   "+   
					"getSubcuentaCantidadJus(s.numero_solicitud) as subcuentacantidad,  "+
					"c.via_ingreso as codviaingreso,  "+
					"c.tipo_paciente as codtipopaciente,   "+  
					"case when getcuantosTipoPacViaIngreso(c.via_ingreso)>1          then getnombreviaingreso(c.via_ingreso)||' - '||getnombretipopaciente(c.tipo_paciente)    else getnombreviaingreso(c.via_ingreso)||'' end  As viaingresotipopac,   "+       
					"i.fecha_ingreso || '/' || i.hora_ingreso as fechahora,    "+ 
					"i.consecutivo as noingreso,      "+
					"coalesce(getValorTotalCargoAsociado(s.numero_solicitud,jas.articulo,jar.subcuenta)||'','PENDIENTE') as valor,   "+   
					"getCantidadTotalCargoAsociado(s.numero_solicitud,jas.articulo,jar.subcuenta) as cantidadcargada,     "+    
					"getidpaciente(i.codigo_paciente) as tipoid,   "+   
					"getnombrepersona(i.codigo_paciente) as nombrepac,  "+
					"jas.articulo as codigoart,       "+
					"c.id as codigocuenta,    "+
					"i.codigo_paciente as codpaciente,        "+
					"getcamacuenta(c.id,c.via_ingreso) as cama, "+      
					"jas.consecutivo as nojus,  "+
					"jar.cantidad as cantotorden,     "+
					"getdespacho(jas.articulo, sja.numero_solicitud) as cantotdespacho,   "+    
					"gettotaladminfarmacia(jas.articulo, sja.numero_solicitud,"+ ValoresPorDefecto.getValorFalseParaConsultas()+") as cantotadmin,    "+
					"jas.tiempo_tratamiento as tiempotratamiento,    "+
					"gettotaladminfarresponsable(sja.numero_solicitud,jas.articulo,jar.subcuenta) as cantotconv,  "+    
					"getValorTotalCargoAsociado(s.numero_solicitud,jas.articulo,jar.subcuenta) * gettotaladminfarresponsable(sja.numero_solicitud,jas.articulo,jar.subcuenta) as precioventot,  "+
					"getcostounitario(sja.numero_solicitud, s.tipo,jas.articulo) as costounitario,   "+   
					"gettotaladminfarmacia(jas.articulo,sja.numero_solicitud,"+ ValoresPorDefecto.getValorFalseParaConsultas()+") * getcostounitario(sja.numero_solicitud, s.tipo,jas.articulo) as preciocostot,  "+   
					"jas.articulo as codigo_art,   "+   
					"getnombrepiso(getcodigopisocuenta(c.id,c.via_ingreso)) as piso,  "+       
					"getintegridaddominio(jar.estado) as estadojus, "+  
					"CASE WHEN art.categoria=1 THEN 'NO' WHEN art.categoria=2 THEN 'SI' ELSE '' END AS control "+
					"FROM    "+
					"ingresos i  "+ 
				"INNER JOIN     "+
					"cuentas c on (c.id_ingreso=i.id)   "+
				"INNER JOIN     "+
					"solicitudes s on (c.id=s.cuenta)   "+
				"INNER JOIN     "+
					"sub_cuentas sc on (sc.ingreso=i.id)   "+
				//MT6527
				"INNER JOIN         "+ 
					"sol_x_just_art_sol sja  on(s.numero_solicitud=sja.numero_solicitud)  "+
				"INNER JOIN         "+  
					"justificacion_art_sol jas on (sja.codigo_justificacion=jas.codigo)   "+  
				"INNER JOIN         "+
					"justificacion_art_resp jar on (jas.codigo=jar.justificacion_art_sol) "+  
				"INNER JOIN         "+
					"articulo art on (art.codigo=jas.articulo)   "+
				"INNER JOIN    "+
					"justificacion_art_fijo jaf on (jas.codigo=jaf.justificacion_art_sol)  "+
					"WHERE     "+ 
					"c.estado_cuenta!=4  "+ 
					"and sc.nro_prioridad=1  "+ 
					"and s.estado_historia_clinica!=4  "+  
					"and 1=2 "+ 
					"and 2=2 "+ 
					"and 3=3 "+ 
					"and 4=4 "+ 
					"and 5=5 "+ 
					"and 6=6 "+ 
					"and 7=7 "+ 
					"and 8=8 "+ 
					"and 9=9 "+ 
					"and 10=10 "+ 
				"GROUP BY  "+ 
					"s.centro_costo_solicitante, "+
					"sc.convenio, "+
					"jar.estado, "+
					"getnombrepiso(getcodigopisocuenta(c.id,c.via_ingreso)), "+
					"s.numero_solicitud, "+
					"s.fecha_solicitud, "+
					"i.id , "+
					"sc.sub_cuenta , "+
					"c.via_ingreso , "+
					"c.tipo_paciente, "+
					"i.consecutivo, "+
					"jas.articulo, "+
					"c.id, "+
					"i.codigo_paciente, "+
					"jas.consecutivo, "+
					"jar.cantidad, "+
					"jas.tiempo_tratamiento, "+
					"i.fecha_ingreso, "+
					"i.hora_ingreso, "+
					"sja.numero_solicitud, "+
					"s.tipo, "+
					"jar.subcuenta, "+
					"art.categoria "+
				"ORDER BY "+
					"getnomcentrocosto(s.centro_costo_solicitante), "+
					"getnombreconvenioresponsable(getConvenioSubcuenta(jar.subcuenta), i.id),  "+ 
					"i.fecha_ingreso || '/' || i.hora_ingreso ";
			
			return consulta;
		}
		
		/**
		 * Metodo que devuelve la consulta del DATASET RompimientoEstadoJus
		 * @return
		 */
		//MT6527 se modifica Join para el numero _solicitud agrega join para el filtro de responsable
		public static String consultarRompimientoEstadoJus(){
			
			String consulta ="SELECT "+     
				   	"s.centro_costo_solicitante as codigo_centro_costo, "+     
				   	"getnomcentrocosto(s.centro_costo_solicitante) as centro_costo,    "+
				   	"getConvenioSubcuenta(jar.subcuenta) as codigo_convenio, "+
				   	"getnombreconvenioresponsable(getConvenioSubcuenta(jar.subcuenta), i.id) as convenio,  "+   
				   	"s.numero_solicitud as solicitud, "+       
				   	"s.fecha_solicitud as fecha,   "+    
				   	"getdescarticulosincodigo(jas.articulo) as articulo,    "+ 
				   	"i.id as ingreso,      "+  
				   	"jar.subcuenta as subcuenta,   "+  
				   	"getSubcuentaCantidadJus(s.numero_solicitud) as subcuentacantidad, "+
				   	"c.via_ingreso as codviaingreso, "+
				   	"c.tipo_paciente as codtipopaciente,   "+ 
				   	"case when getcuantosTipoPacViaIngreso(c.via_ingreso)>1          then getnombreviaingreso(c.via_ingreso)||' - '||getnombretipopaciente(c.tipo_paciente)    else getnombreviaingreso(c.via_ingreso)||'' end  As viaingresotipopac,  "+       
				   	"i.fecha_ingreso || '/' || i.hora_ingreso as fechahora, "+   
				   	"i.consecutivo as noingreso,    "+ 
				   	"coalesce(getValorTotalCargoAsociado(s.numero_solicitud,jas.articulo,jar.subcuenta)||'','PENDIENTE') as valor, "+    
				   	"getCantidadTotalCargoAsociado(s.numero_solicitud,jas.articulo,jar.subcuenta) as cantidadcargada,      "+  
				   	"getidpaciente(i.codigo_paciente) as tipoid,     "+
				   	"getnombrepersona(i.codigo_paciente) as nombrepac, "+
				   	"jas.articulo as codigoart,     "+ 
				   	"c.id as codigocuenta,   "+
				   	"i.codigo_paciente as codpaciente,       "+
				   	"getcamacuenta(c.id,c.via_ingreso) as cama,   "+   
				   	"jas.consecutivo as nojus, "+
				   	"jar.cantidad as cantotorden,   "+ 
				   	"getdespacho(jas.articulo, sja.numero_solicitud) as cantotdespacho,   "+   
				   	"gettotaladminfarmacia(jas.articulo, sja.numero_solicitud,"+ ValoresPorDefecto.getValorFalseParaConsultas()+") as cantotadmin,   "+
				   	"jas.tiempo_tratamiento as tiempotratamiento,  "+ 
				   	"gettotaladminfarresponsable(sja.numero_solicitud,jas.articulo,jar.subcuenta) as cantotconv,    "+ 
				   	"getValorTotalCargoAsociado(s.numero_solicitud,jas.articulo,jar.subcuenta) * gettotaladminfarresponsable(sja.numero_solicitud,jas.articulo,jar.subcuenta) as precioventot, "+
				   	"getcostounitario(sja.numero_solicitud, s.tipo,jas.articulo) as costounitario,   "+  
				   	"gettotaladminfarmacia(jas.articulo,sja.numero_solicitud,"+ ValoresPorDefecto.getValorFalseParaConsultas()+") * getcostounitario(sja.numero_solicitud, s.tipo,jas.articulo) as preciocostot,   "+ 
				   	"jas.articulo as codigo_art,    "+ 
				   	"getnombrepiso(getcodigopisocuenta(c.id,c.via_ingreso)) as piso,    "+    
				   	"getintegridaddominio(jar.estado) as estadojus,  "+
					"CASE WHEN art.categoria=1 THEN 'NO' WHEN art.categoria=2 THEN 'SI' ELSE '' END AS control "+
				   "FROM   "+
				   	"ingresos i  "+
				   "INNER JOIN    "+
				   	"cuentas c on (c.id_ingreso=i.id)  "+
				   "INNER JOIN    "+
				   	"solicitudes s on (c.id=s.cuenta)  "+
				   "INNER JOIN    "+
				   	"sub_cuentas sc on (sc.ingreso=i.id)  "+
				  //MT6527
					"INNER JOIN         "+ 
						"sol_x_just_art_sol sja  on(s.numero_solicitud=sja.numero_solicitud)  "+
					"INNER JOIN         "+  
						"justificacion_art_sol jas on (sja.codigo_justificacion=jas.codigo)   "+ 
				   "INNER JOIN        "+
				   "	justificacion_art_resp jar on (jas.codigo=jar.justificacion_art_sol)  "+
				   "INNER JOIN        "+
				  " 	articulo art on (art.codigo=jas.articulo)  "+
				  "INNER JOIN    "+
					"justificacion_art_fijo jaf on (jas.codigo=jaf.justificacion_art_sol)  "+
				  " WHERE    "+
				   "	c.estado_cuenta!=4 "+
				   "	and sc.nro_prioridad=1 "+
				   	"and s.estado_historia_clinica!=4  "+
				   	"and 1=2 "+
				   "	and 2=2 "+
				   "	and 3=3  "+
				  " 	and 4=4 "+
				  " 	and 5=5 "+
				  " 	and 6=6 "+
				  " 	and 7=7 "+
				  " 	and 8=8 "+
				 "  	and 9=9 "+
				  " 	and 10=10 "+
				  " GROUP BY  "+
				  " 	s.centro_costo_solicitante, "+
				  " 	sc.convenio, "+
				  " 	jar.estado, "+
				  " 	getnombrepiso(getcodigopisocuenta(c.id,c.via_ingreso)), "+
				   "	s.numero_solicitud, "+
				   "	s.fecha_solicitud, "+
				  " 	i.id , "+
				  " 	sc.sub_cuenta , "+
				  " 	c.via_ingreso , "+
				  " 	c.tipo_paciente, "+
				  " 	i.consecutivo, "+
				  " 	jas.articulo, "+
				  " 	c.id, "+
				  " 	i.codigo_paciente, "+
				 "  	jas.consecutivo, "+
				 "  	jar.cantidad, "+
				  " 	jas.tiempo_tratamiento, "+
				  " 	i.fecha_ingreso, "+
				  " 	i.hora_ingreso, "+
				  " 	sja.numero_solicitud, "+
				  " 	s.tipo, "+
				  " 	jar.subcuenta, "+
				  "		art.categoria "+
				  " ORDER BY "+
				 "  	getnomcentrocosto(s.centro_costo_solicitante), "+
				   	"getnombreconvenioresponsable(getConvenioSubcuenta(jar.subcuenta), i.id),  "+         
				   	"i.fecha_ingreso || '/' || i.hora_ingreso ";
			
			return consulta;	       
		}
		
		/**
		 * Metodo que devuelve la consulta del DATASET RompimientoEstadoJus1
		 * @return
		 */
		//MT6527 se modifica Join para el numero _solicitud agrega join para el filtro de responsable
		// Se arregla la consulta agregando el form
		public static String consultarRompimientoEstadoJus1(){
			String consulta="SELECT     "+
					"s.centro_costo_solicitante as codigo_centro_costo,    "+   
					"getnomcentrocosto(s.centro_costo_solicitante) as centro_costo,    "+ 
					"getConvenioSubcuenta(jar.subcuenta) as codigo_convenio,  "+
					"getnombreconvenioresponsable(getConvenioSubcuenta(jar.subcuenta), i.id) as convenio,   "+   
					"s.numero_solicitud as solicitud,         "+
					"s.fecha_solicitud as fecha,       "+ 
					"getdescarticulosincodigo(jas.articulo) as articulo,  "+    
					"i.id as ingreso,       "+  
					"jar.subcuenta as subcuenta,    "+  
					"getSubcuentaCantidadJus(s.numero_solicitud) as subcuentacantidad,  "+
					"c.via_ingreso as codviaingreso,  "+
					"c.tipo_paciente as codtipopaciente, "+    
					"case when getcuantosTipoPacViaIngreso(c.via_ingreso)>1          then getnombreviaingreso(c.via_ingreso)||' - '||getnombretipopaciente(c.tipo_paciente)    else getnombreviaingreso(c.via_ingreso)||'' end  As viaingresotipopac,    "+      
					"i.fecha_ingreso || '/' || i.hora_ingreso as fechahora,   "+  
					"i.consecutivo as noingreso,      "+
					"coalesce(getValorTotalCargoAsociado(s.numero_solicitud,jas.articulo,jar.subcuenta)||'','PENDIENTE') as valor,  "+    
					"getCantidadTotalCargoAsociado(s.numero_solicitud,jas.articulo,jar.subcuenta) as cantidadcargada,     "+    
					"getidpaciente(i.codigo_paciente) as tipoid,      "+
					"getnombrepersona(i.codigo_paciente) as nombrepac,  "+
					"jas.articulo as codigoart,     "+  
					"c.id as codigocuenta,    "+
					"i.codigo_paciente as codpaciente,  "+      
					"getcamacuenta(c.id,c.via_ingreso) as cama, "+      
					"jas.consecutivo as nojus,  "+
					"jar.cantidad as cantotorden,     "+
					"getdespacho(jas.articulo, sja.numero_solicitud) as cantotdespacho,     "+  
					"gettotaladminfarmacia(jas.articulo, sja.numero_solicitud,"+ ValoresPorDefecto.getValorFalseParaConsultas()+") as cantotadmin,   "+ 
					"jas.tiempo_tratamiento as tiempotratamiento,   "+ 
					"gettotaladminfarresponsable(sja.numero_solicitud,jas.articulo,jar.subcuenta) as cantotconv,   "+   
					"getValorTotalCargoAsociado(s.numero_solicitud,jas.articulo,jar.subcuenta) * gettotaladminfarresponsable(sja.numero_solicitud,jas.articulo,jar.subcuenta) as precioventot,  "+
					"getcostounitario(sja.numero_solicitud, s.tipo,jas.articulo) as costounitario,    "+  
					"gettotaladminfarmacia(jas.articulo,sja.numero_solicitud,"+ ValoresPorDefecto.getValorFalseParaConsultas()+") * getcostounitario(sja.numero_solicitud, s.tipo,jas.articulo) as preciocostot,     "+
					"jas.articulo as codigo_art,     "+ 
					"getnombrepiso(getcodigopisocuenta(c.id,c.via_ingreso)) as piso,    "+     
					"getintegridaddominio(jar.estado) as estadojus,   "+
					"CASE WHEN art.categoria=1 THEN 'NO' WHEN art.categoria=2 THEN 'SI' ELSE '' END AS control "+
					 "FROM   "+
					   	"ingresos i  "+
					"INNER JOIN    "+
						"cuentas c on (c.id_ingreso=i.id)   "+
				  	"INNER JOIN     "+
				  		"solicitudes s on (c.id=s.cuenta)   "+
				 	"INNER JOIN     "+
				  		"sub_cuentas sc on (sc.ingreso=i.id)   "+
					//MT6527
					"INNER JOIN         "+ 
						"sol_x_just_art_sol sja  on(s.numero_solicitud=sja.numero_solicitud)  "+
					"INNER JOIN         "+  
						"justificacion_art_sol jas on (sja.codigo_justificacion=jas.codigo)   "+  
				"INNER JOIN         "+
					"justificacion_art_resp jar on (jas.codigo=jar.justificacion_art_sol) "+  
				"INNER JOIN         "+
					"articulo art on (art.codigo=jas.articulo)   "+
				"INNER JOIN    "+
					"justificacion_art_fijo jaf on (jas.codigo=jaf.justificacion_art_sol)  "+
					"WHERE     "+ 
					"c.estado_cuenta!=4  "+ 
					"and sc.nro_prioridad=1  "+ 
					"and s.estado_historia_clinica!=4  "+  
					"and 1=2 "+ 
					"and 2=2 "+ 
					"and 3=3 "+ 
					"and 4=4 "+ 
					"and 5=5 "+ 
					"and 6=6 "+ 
					"and 7=7 "+ 
					"and 8=8 "+ 
					"and 9=9 "+ 
					"and 10=10 "+ 
				"GROUP BY  "+ 
					"jar.estado, "+
					"sc.convenio, "+
					"getnombrepiso(getcodigopisocuenta(c.id,c.via_ingreso)), "+
					"s.centro_costo_solicitante, "+
					"s.numero_solicitud, "+
					"s.fecha_solicitud, "+
					"i.id , "+
					"sc.sub_cuenta , "+
					"c.via_ingreso , "+
					"c.tipo_paciente, "+
					"i.consecutivo, "+
					"jas.articulo, "+
					"c.id, "+
					"i.codigo_paciente, "+
					"jas.consecutivo, "+
					"jar.cantidad, "+
					"jas.tiempo_tratamiento, "+
					"i.fecha_ingreso, "+
					"i.hora_ingreso, "+
					"sja.numero_solicitud, "+
					"s.tipo, "+
					"jar.subcuenta, "+
					"art.categoria "+
				"ORDER BY "+
					"getintegridaddominio(jar.estado), "+
					"getnombreconvenioresponsable(getConvenioSubcuenta(jar.subcuenta), i.id),   "+
					"i.fecha_ingreso || '/' || i.hora_ingreso ";
			
			return consulta;
		}
}