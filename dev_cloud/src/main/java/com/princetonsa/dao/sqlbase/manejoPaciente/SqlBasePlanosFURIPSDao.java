package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.princetonsa.dto.manejoPaciente.DtoFURIPS1;
import com.princetonsa.dto.manejoPaciente.DtoFURIPS2;
import com.princetonsa.dto.manejoPaciente.DtoFURPRO;
import com.princetonsa.dto.manejoPaciente.DtoFURTRAN;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.Medico;
import com.princetonsa.mundo.facturacion.ParametrizacionInstitucion;
import com.princetonsa.mundo.manejoPaciente.PlanosFURIPS;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.manejoPaciente.RutasArchivosFURIPS;

/**
 * 
 * @author wilson
 *
 */
public class SqlBasePlanosFURIPSDao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBasePlanosFURIPSDao.class);

	/**
	 * Metodo que carga toda la información pertinente para generar el archivo FURIPS1
	 * @param con
	 * @param mapaBusqueda
	 * @param institucionBasica
	 * @param institucion
	 * @return
	 */
	public static ArrayList<DtoFURIPS1> consultaFURIPS1(Connection con, HashMap<Object, Object> mapaBusqueda, InstitucionBasica institucionBasica, int institucion,String reclamacion)
	{
		boolean institucionPublica=ParametrizacionInstitucion.esInstitucionPublica(con, institucion);
		ArrayList<DtoFURIPS1> array= new ArrayList<DtoFURIPS1>();
		Vector<String> vectorFactura= new Vector<String>();
		vectorFactura.add(ConstantesBD.codigoNuncaValido+"");
		
		String consulta="(" +
							//REGISTRO DE ACCIDENTES DE TRANSITO
							"SELECT " +
								"f.codigo||'' as codigofactura, " +
								"1 as esaccidentetransito, " +
								"0 as eseventocatastrofico, " +
								"0 as esdesplazado, " +
								"c.id as idcuenta, " +
								"getnombreviaingreso(c.via_ingreso) as nomviaingreso, " +
								"coalesce(reclam.num_radica_anterior||'', '') as nroradicadoanterior_1, " +
								"CASE " +
									"WHEN reclam.respuesta_glosa='"+ConstantesIntegridadDominio.acronimoRespuestaGlosaPagoParcia+"' THEN '1' " +
									"WHEN reclam.respuesta_glosa='"+ConstantesIntegridadDominio.acronimoRespuestaGlosaGlosaTotal+"' THEN '0' " +
									"ELSE '' " +
								"END as rg_2, " +
								"f.consecutivo_factura as numerofactura_3, " +
								"coalesce(f.numero_cuenta_cobro||'','') as numerocuentacobro_3, " +
								"coalesce(reclam.nro_reclamacion||'', '') as nroreclamacion_4, " +
								//codigoMinsalud se tiene en la institucionBasica
								"coalesce(p.primer_apellido, 'NN') as primerapellido_6, " +
								"coalesce(p.segundo_apellido, ' ') as segundoapellido_7, " +
								"coalesce(p.primer_nombre, 'NN') as primernombre_8, " +
								"coalesce(p.segundo_nombre, ' ') as segundonombre_9, " +
								"p.tipo_identificacion as tipoidentificacion_10, " +
								
								"CASE WHEN  " +
									"p.tipo_identificacion IN('"+ConstantesBD.acronimoTipoIdMenorSinId+"', '"+ConstantesBD.acronimoTipoIdAdultosSinId+"') " +
								"THEN " +
									"rat.departamento_accidente ||''|| rat.ciudad_accidente||'NN'|| coalesce(substr(pac.historia_clinica,1,9), '') " +
								"ELSE " +
									"p.numero_identificacion||'' " +
								"END as numeroidentificacion_11, " +
								
								"to_char(p.fecha_nacimiento, 'DD/MM/YYYY') as fechanacimiento_12, " +
								"CASE WHEN p.sexo="+ConstantesBD.codigoSexoFemenino+" then 'F' else 'M' end as sexo_13, " +
								"p.direccion as direccion_14, " +
								"coalesce(p.codigo_departamento_vivienda, '') as deptoubicacionvictima_15, " +
								"coalesce(p.codigo_ciudad_vivienda, '') as ciudadubicacionvictima_16, " +
								"coalesce(p.telefono,'') as telefonovictima_17, " +
								"CASE " +
									"WHEN rat.condicion_accidentado='"+ConstantesIntegridadDominio.acronimoConductor+"' THEN '1' " +
									"WHEN rat.condicion_accidentado='"+ConstantesIntegridadDominio.acronimoPeaton+"' THEN '2' " +
									"WHEN rat.condicion_accidentado in('"+ConstantesIntegridadDominio.acronimoVehiculo+"', '"+ConstantesIntegridadDominio.acronimoMotocicleta+"') THEN '3' " +
									"WHEN rat.condicion_accidentado='"+ConstantesIntegridadDominio.acronimoCiclista+"' THEN '4' " +
								"END as  condicionaccidentado_18, " +
								"'01' as naturalezaevento_19, " +
								"'' AS descotroevento_20, "+
								"rat.lugar_accidente as direccionocurrencia_21, " +
								"to_char(rat.fecha_accidente, 'DD/MM/YYYY') as fechaocurrencia_22, " +
								"rat.hora_accidente as horaocurrencia_23, " +
								"coalesce(rat.departamento_accidente,'') as codigodepto_24, " +
								"coalesce(rat.ciudad_accidente,'') as codigomunicipio_25, " +
								"rat.zona_accidente as zona_26, "+
								"CASE " +
									"WHEN rat.asegurado='"+ConstantesBD.acronimoSi+"' and rat.poliza='"+ConstantesIntegridadDominio.acronimoPolizaVigente+"' then '1' " +
									"WHEN rat.asegurado='"+ConstantesBD.acronimoNo+"' then '2' " +
									"WHEN rat.asegurado='"+ConstantesIntegridadDominio.acronimoAseguradoFantasma+"' then '3' " +
									"WHEN rat.asegurado='"+ConstantesBD.acronimoSi+"' and rat.poliza='"+ConstantesIntegridadDominio.acronimoPolizaFalsa+"' then '4' " +
									"WHEN rat.asegurado='"+ConstantesBD.acronimoSi+"' and rat.poliza='"+ConstantesIntegridadDominio.acronimoPolizaVencida+"' then '2' " +
									"else '5' " +
								"END AS estadoaseguramiento_27, " +
								"rat.marca_vehiculo as marcavehiculo_28, " +
								"rat.placa as placavehiculo_29, " +
								"CASE " +
									"WHEN rat.tipo_serv_v="+ConstantesBD.codigoVehiculoServicioParticular+" then '3' " +
									"WHEN rat.tipo_serv_v="+ConstantesBD.codigoVehiculoServicioPublico+" then '4' " +
									"WHEN rat.tipo_serv_v="+ConstantesBD.codigoVehiculoServicioOficial+" then '5' " +
									"WHEN rat.tipo_serv_v="+ConstantesBD.codigoVehiculoEmergencia+" then '6' " +
									"WHEN rat.tipo_serv_v="+ConstantesBD.codigoVehiculoServicioDiplomaticoOConsultar+" then '7' " +
									"WHEN rat.tipo_serv_v="+ConstantesBD.codigoVehiculoTransporteMasivo+" then '8' " +
									"WHEN rat.tipo_serv_v="+ConstantesBD.codigoVehiculoEscolar+" then '9' " +
									"ELSE '' "+
								"END AS tipovehiculo_30, " +
								"coalesce(conv.cod_aseguradora,'') AS codaseguradora_31, " +
								"coalesce(rat.numero_poliza||'', '') as numpolizasoat_32, " +
								"CASE WHEN rat.fecha_inicial_poliza IS NULL THEN '' ELSE to_char(rat.fecha_inicial_poliza, 'DD/MM/YYYY') END as fechainiciovigpoliza_33, " +
								"CASE WHEN rat.fecha_final_poliza IS NULL THEN '' ELSE to_char(rat.fecha_final_poliza, 'DD/MM/YYYY') END as fechafinalvigpoliza_34, " +
								"CASE " +
									"WHEN rat.intervencion_autoridad='"+ConstantesBD.acronimoSi+"' then '1' " +
									"WHEN rat.intervencion_autoridad='"+ConstantesBD.acronimoNo+"' then '0' " +
									"else '' " +
								"END AS intervencionautoridad_35, " +
								"CASE " +
									"WHEN rat.cobro_excedente_poliza='"+ConstantesBD.acronimoSi+"' then '1' " +
									"WHEN rat.cobro_excedente_poliza='"+ConstantesBD.acronimoNo+"' then '0' " +
									"else '' " +	
								"END AS cobroexcedentepoliza_36, " +
								"coalesce(rat.placa_2_vehiculo, '') as placa2_37, " +
								"coalesce(rat.tipo_id_2_vehiculo,'') as tipoid2_38, " +
								"coalesce(rat.nro_id_2_vehiculo,'') as nroid2_39, " +
								"coalesce(rat.placa_3_vehiculo,'') as placa3_40, " +
								"coalesce(rat.tipo_id_3_vehiculo,'') as tipoid3_41, " +
								"coalesce(rat.nro_id_3_vehiculo,'') as nroid3_42, " +
								"coalesce(rat.tipo_id_prop,'') as tipoidpropietario_43, " +
								"rat.numero_id_prop as numeroidprop_44, " +
								"rat.primer_apellido_prop as primerapellidoprop_45, " +
								"rat.segundo_apellido_prop as segundoapellidoprop_46, " +
								"rat.primer_nombre_prop as primernombreprop_47, " +
								"rat.segundo_nombre_prop as segundonombreprop_48, " +
								"rat.direccion_prop as direccionprop_49, " +
								"rat.telefono_prop as telefonoprop_50, " +
								"coalesce(rat.departamento_prop,'') as departamentoprop_51, " +
								"coalesce(rat.ciudad_prop,'') as municipioprop_52, " +
								"rat.primer_apellido_conductor as primerapellidoconductor_53, " +
								"rat.segundo_apellido_conductor as segundoapellidoconductor_54, " +
								"rat.primer_nombre_conductor as primernombreconductor_55, " +
								"rat.segundo_nombre_conductor as segundonombreconductor_56, " +
								"coalesce(rat.tipo_id_conductor,'') as tipo_id_conductor_57, " +
								"rat.numero_id_conductor as numero_id_conductor_58, " +
								"rat.direccion_conductor as direccion_conductor_59, " +
								"coalesce(rat.departamento_conductor,'') as departamento_conductor_60, " +
								"coalesce(rat.ciudad_conductor,'') as ciudad_conductor_61, " +
								"coalesce(rat.telefono_conductor||'','') as telefono_conductor_62, " +
								"CASE " +
									"WHEN rat.tipo_referencia ='"+ConstantesIntegridadDominio.acronimoRemision+"' THEN '1' " +
									"WHEN rat.tipo_referencia ='"+ConstantesIntegridadDominio.acronimoOrdenServicio+"' THEN '2' " +
									"ELSE '' " +
								"END AS tiporeferencia_63, " +
								"CASE WHEN rat.fecha_remision IS NULL THEN ' ' ELSE to_char(rat.fecha_remision, 'DD/MM/YYYY') END as fecharemision_64, " +
								"coalesce(rat.hora_remision,'') as horasalida_65, " +
								"coalesce(rat.cod_inscrip_remitente,'') as codhabilitacionips_66, " +
								"coalesce(rat.profesional_remite,'') as profesional_remite_67, "+
								"coalesce(rat.cargo_profesional_remitente,'') as cargo_profesional_remitente_68, " +
								"CASE WHEN rat.fecha_aceptacion IS NULL THEN '' ELSE to_char(rat.fecha_aceptacion, 'DD/MM/YYYY') END as fechaaceptacion_69, " +
								"coalesce(rat.hora_aceptacion,'') as horaaceptacion_70, " +
								"coalesce(rat.cod_inscrip_receptor,'') as codhabilitacionreceptor_71, "+
								"coalesce(rat.profesional_recibe,'') as profesional_recibe_72, "+
								"coalesce(rat.cargo_profesional_recibe,'') as cargo_profesional_recibe_73, " +
								"rat.placa_vehiculo_tranporta as placatransporta_74, " +
								"rat.transporta_victima_desde as transporta_victima_desde_75, " +
								"rat.transporta_victima_hasta as transporta_victima_hasta_76, " +
								"CASE " +
									"WHEN rat.tipo_transporte='"+ConstantesIntegridadDominio.acronimoTipoTransporteAmbulanciaBasica+"' then '1' " +
									"WHEN rat.tipo_transporte='"+ConstantesIntegridadDominio.acronimoTipoTransporteAmbulanciaMedicalizada+"' then '2' " +
									"ELSE '' " +
								"END as tipo_servicio_77, " +
								"coalesce(rat.zona_accidente,'') as zonarecogevictima_78, " +
								"to_char(i.fecha_ingreso, 'DD/MM/YYYY') as fecha_ingreso_79, " +
								"i.hora_ingreso as hora_ingreso_80, " +
								"CASE " +
									"WHEN c.via_ingreso="+ConstantesBD.codigoViaIngresoAmbulatorios+" THEN to_char(i.fecha_ingreso, 'DD/MM/YYYY') " +
									"WHEN c.via_ingreso="+ConstantesBD.codigoViaIngresoConsultaExterna+" THEN to_char(i.fecha_ingreso, 'DD/MM/YYYY') " +
									"else to_char(i.fecha_egreso, 'DD/MM/YYYY') " +
								"END as fecha_egreso_81, " +
								"to_char(f.fecha,'DD/MM/YYYY') as fecha_factura_81, " +
								"CASE " +
									"WHEN c.via_ingreso="+ConstantesBD.codigoViaIngresoAmbulatorios+" THEN i.hora_ingreso " +
									"WHEN c.via_ingreso="+ConstantesBD.codigoViaIngresoConsultaExterna+" THEN i.hora_ingreso " +
									"else substr(i.hora_egreso||'',1,5) " +
								"END as hora_egreso_82, " +
								"substr(f.hora||'',1,5) as hora_factura_82, " +
								//dx_ppal o  dx_rel_1 o dxrel_2 o dxrel_3 o dx_egreso
								"cert.acronimo_dx_ingreso as dx_ppal_ingreso_83, " +
								"cert.acronimo_dx_rel1_ingreso as dx_rel_1_ingreso_84, " +
								"cert.acronimo_dx_rel2_ingreso as dx_rel_1_ingreso_85, " +
								"cert.acronimo_dx_egreso as dx_ppal_egreso_86, " +
								"cert.acronimo_dx_rel1_egreso as dx_ppal_egreso_rel_1_87, " +
								"cert.acronimo_dx_rel2_egreso as dx_ppal_egreso_rel_2_88, " +
								"cert.primer_apellido_medico as primerapellidomedico_89,"+
								"cert.segundo_apellido_medico as segundoapellidomedico_90,"+
								"cert.primer_nombre_medico as primernombremedico_91,"+
								"cert.segundo_nombre_medico as segundonombremedico_92,"+
								"cert.tipo_documento_medico as tipodocmedico_93,"+
								"cert.nro_documento_medico as numerodocmedico_94,"+
								"cert.nro_registro_medico as numeroregistromedico_95,"+								
								"coalesce(ampxrec.total_fac_amp_gast_medqx||'','') as total_fac_amp_qx_96, " +
								"coalesce(ampxrec.total_rec_amp_gast_medqx ||'','') as total_reclamo_amp_qx_97, " +
								"coalesce(ampxrec.total_fac_amp_gast_transmov ||'', '') as total_fac_amp_tx_98, " +
								"coalesce(ampxrec.total_rec_amp_gast_transmov ||'', '') as total_reclamo_amp_tx_99 " +
							"FROM " +
								"facturas f " +
								"INNER JOIN cuentas c ON(c.id=f.cuenta) " +
								"INNER JOIN ingresos i ON(i.id=c.id_ingreso)" +
								"INNER JOIN ingresos_registro_accidentes ira ON(ira.ingreso=i.id) " +
								"INNER JOIN registro_accidentes_transito rat ON(rat.codigo=ira.codigo_registro) " +
								"INNER JOIN manejopaciente.reclamaciones_acc_eve_fact reclam ON (reclam.codigo_accidente=rat.codigo and reclam.estado!='"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"') " +
								"INNER JOIN manejopaciente.amparo_x_reclamar ampxrec on (ampxrec.codigo_reclamacion=reclam.codigo_pk) " +
								"INNER JOIN manejopaciente.cert_aten_medica_furips cert on (cert.codigo_reclamacion=reclam.codigo_pk) " +
								"INNER JOIN personas p ON (p.codigo=c.codigo_paciente) " +
								"INNER JOIN pacientes pac ON(pac.codigo_paciente=p.codigo) " +
								"INNER JOIN convenios conv ON(conv.codigo=rat.aseguradora) " +
							"WHERE 1=1 ";
							if(mapaBusqueda.containsKey("fechainicial"))
							{
								consulta+=" and f.fecha between '"+UtilidadFecha.conversionFormatoFechaABD(mapaBusqueda.get("fechainicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(mapaBusqueda.get("fechafinal")+"")+"' ";
							}
							consulta+=" and f.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionFacturada+" ";
								
							if(!UtilidadTexto.isEmpty(reclamacion))
								consulta=consulta+" and reclam.codigo_pk in("+reclamacion+") ";
		
							consulta+=(institucionBasica.getEsEmpresaInstitucion())?"and f.empresa_institucion="+institucionBasica.getCodigo():"";	
							consulta+="" +
								"and f.institucion="+institucion+" "+((!UtilidadTexto.getBoolean(ValoresPorDefecto.getPermitirFacturarReclamarCuentasConRegistroPendientes(institucion)))?(" and rat.estado='"+ConstantesIntegridadDominio.acronimoEstadoProcesado+"'"):"") +
						") " +
						"UNION " +
						"(" +
							//REGISTRO DE EVENTOS CATASTROFICOS
							"SELECT " +
								"f.codigo ||'' as codigofactura, " +
								"0 as esaccidentetransito, " +
								"1 as eseventocatastrofico, " +
								"0 as esdesplazado, " +
								"c.id as idcuenta, " +
								"getnombreviaingreso(c.via_ingreso) as nomviaingreso, " +
								"coalesce(reclam.num_radica_anterior||'', '') as nroradicadoanterior_1, " +
								"CASE " +
									"WHEN reclam.respuesta_glosa='"+ConstantesIntegridadDominio.acronimoRespuestaGlosaPagoParcia+"' THEN '1' " +
									"WHEN reclam.respuesta_glosa='"+ConstantesIntegridadDominio.acronimoRespuestaGlosaGlosaTotal+"' THEN '0' " +
									"ELSE '' " +
								"END as rg_2, " +	
								"f.consecutivo_factura as numerofactura_3, " +
								"coalesce(f.numero_cuenta_cobro||'','') as numerocuentacobro_3, " +
								"coalesce(reclam.nro_reclamacion||'','') as nroreclamacion_4, " +
								//codigoMinsalud se tiene en la institucionBasica
								"coalesce(p.primer_apellido, 'NN') as primerapellido_6, " +
								"coalesce(p.segundo_apellido, ' ') as segundoapellido_7, " +
								"coalesce(p.primer_nombre, 'NN') as primernombre_8, " +
								"coalesce(p.segundo_nombre, ' ') as segundonombre_9, " +
								"p.tipo_identificacion as tipoidentificacion_10, " +
								"CASE WHEN  " +
									"p.tipo_identificacion IN('"+ConstantesBD.acronimoTipoIdMenorSinId+"', '"+ConstantesBD.acronimoTipoIdAdultosSinId+"') " +
								"THEN " +
									"rec.departamento_evento ||''|| rec.ciudad_evento||'NN'|| coalesce(substr(pac.historia_clinica,1,9), '') " +
								"ELSE " +
									"p.numero_identificacion||'' " +
								"END as numeroidentificacion_11, " +
								
								"to_char(p.fecha_nacimiento, 'DD/MM/YYYY') as fechanacimiento_12, " +
								"CASE WHEN p.sexo="+ConstantesBD.codigoSexoFemenino+" then 'F' else 'M' end as sexo_13, " +
								"p.direccion as direccion_14, " +
								"coalesce(p.codigo_departamento_vivienda, '') as deptoubicacionvictima_15, " +
								"coalesce(p.codigo_ciudad_vivienda, '') as ciudadubicacionvictima_16, " +
								"coalesce(p.telefono,'') as telefonovictima_17, " +
								"'' as  condicionaccidentado_18, " +
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
								"END AS naturalezaevento_19, " +
								"CASE " +
									"WHEN rec.naturaleza_evento in("+ConstantesBD.codigoNatEventoOtros +", "+ConstantesBD.codigoNatEventoOtrosEnat+", "+ConstantesBD.codigoNatEventoOtrosEtec+", "+ConstantesBD.codigoNatEventoSustanciasToxicas+", "+ConstantesBD.codigoNatEventoCorrosivos+", "+ConstantesBD.codigoNatEventoEnvenenamiento+") then rec.desc_otro_evento " +
								"END AS descotroevento_20, " +
								"rec.direccion_evento as direccionocurrencia_21, " +
								"to_char(rec.fecha_evento, 'DD/MM/YYYY') as fechaocurrencia_22, " +
								"rec.hora_ocurrencia as horaocurrencia_23, " +
								"coalesce(rec.departamento_evento,'') as codigodepto_24, " +
								"coalesce(rec.ciudad_evento,'') as codigomunicipio_25, " +
								"coalesce(rec.zona_urbana_evento,'') as zona_26, " +
								"'' as estadoaseguramiento_27, "+
								"'' as marcavehiculo_28, " +
								"'' as placavehiculo_29, " +
								"'' as tipovehiculo_30, " +
								"'' as codaseguradora_31, " +
								"'' as numpolizasoat_32, " +
								"'' as fechainiciovigpoliza_33, "+
								"'' as fechafinalvigpoliza_34, " +
								"'' as intervencionautoridad_35, " +
								"'' as cobroexcedentepoliza_36," +
								"'' as placa2_37, " +
								"'' as tipoid2_38, " +
								"'' as nroid2_39, " +
								"'' as placa3_40, " +
								"'' as tipoid3_41, " +
								"'' as nroid3_42, " +
								"'' as tipoidpropietario_43, " +
								"'' as numeroidprop_44, " +
								"'' as primerapellidoprop_45, " +
								"'' as segundoapellidoprop_46, " +
								"'' as primernombreprop_47, " +
								"'' as segundonombreprop_48, " +
								"'' as direccionprop_49, " +
								"'' as telefonoprop_50, " +
								"'' as departamentoprop_52, " +
								"'' as municipioprop_53, " +
								"'' as primerapellidoconductor_54, " +
								"'' as segundoapellidoconductor_55, " +
								"'' as primernombreconductor_55, " +
								"'' as segundonombreconductor_56, " +
								"'' as tipo_id_conductor_57, " +
								"'' as numero_id_conductor_58, " +
								"'' as direccion_conductor_59, " +
								"'' as departamento_conductor_60, " +
								"'' as ciudad_conductor_61, " +
								"'' as telefono_conductor_62," +
								"CASE " +
									"WHEN rec.tipo_referencia ='"+ConstantesIntegridadDominio.acronimoRemision+"' THEN '1' " +
									"WHEN rec.tipo_referencia ='"+ConstantesIntegridadDominio.acronimoOrdenServicio+"' THEN '2' " +
									"ELSE '' " +
								"END AS tiporeferencia_63, " +
								"to_char(rec.fecha_remision, 'DD/MM/YYYY') as fecharemision_64, " +
								"coalesce(rec.hora_remision,'') as horasalida_65, " +
								"coalesce(rec.cod_inscrip_remitente,'') as codhabilitacionips_66, "+
								"coalesce(rec.profesional_remite,'') as profesional_remite_67, "+
								"coalesce(rec.cargo_profesional_remitente,'') as cargo_profesional_remitente_68, " +
								"to_char(rec.fecha_aceptacion, 'DD/MM/YYYY') as fechaaceptacion_69, " +
								"coalesce(rec.hora_aceptacion,'') as horaaceptacion_70, " +
								"coalesce(rec.cod_inscrip_receptor,'') as codhabilitacionreceptor_71, "+
								"coalesce(rec.profesional_recibe,'') as profesional_recibe_72, "+
								"coalesce(rec.cargo_profesional_recibe,'') as cargo_profesional_recibe_73, " +
								"rec.placa_vehiculo_tranporta as placatransporta_74, " +
								"rec.transporta_victima_desde as transporta_victima_desde_75, " +
								"rec.transporta_victima_hasta as transporta_victima_hasta_76, " +
								"CASE " +
									"WHEN rec.tipo_transporte='"+ConstantesIntegridadDominio.acronimoTipoTransporteAmbulanciaBasica+"' then '1' " +
									"WHEN rec.tipo_transporte='"+ConstantesIntegridadDominio.acronimoTipoTransporteAmbulanciaMedicalizada+"' then '2' " +
									"ELSE '' " +
								"END as tipo_servicio_77, " +
								"coalesce(rec.zona_urbana_evento,'') as zonarecogevictima_78, " +
								"to_char(i.fecha_ingreso, 'DD/MM/YYYY') as fecha_ingreso_79, " +
								"i.hora_ingreso as hora_ingreso_80, " +
								"CASE " +
									"WHEN c.via_ingreso="+ConstantesBD.codigoViaIngresoAmbulatorios+" THEN to_char(i.fecha_ingreso, 'DD/MM/YYYY') " +
									"WHEN c.via_ingreso="+ConstantesBD.codigoViaIngresoConsultaExterna+" THEN to_char(i.fecha_ingreso, 'DD/MM/YYYY') " +
									"else to_char(i.fecha_egreso,'DD/MM/YYYY') " +
								"END as fecha_egreso_81, " +
								"to_char(f.fecha,'DD/MM/YYYY') as fecha_factura_81, " +
								"CASE " +
									"WHEN c.via_ingreso="+ConstantesBD.codigoViaIngresoAmbulatorios+" THEN i.hora_ingreso " +
									"WHEN c.via_ingreso="+ConstantesBD.codigoViaIngresoConsultaExterna+" THEN i.hora_ingreso " +
								"else substr(i.hora_egreso||'',1,5) " +
								"END as hora_egreso_82, " +
								"substr(f.hora||'',1,5) as hora_factura_82, " +
								//dx_ppal o  dx_rel_1 o dxrel_2 o dxrel_3 o dx_egreso
								"cert.acronimo_dx_ingreso as dx_ppal_ingreso_83, " +
								"cert.acronimo_dx_rel1_ingreso as dx_rel_1_ingreso_84, " +
								"cert.acronimo_dx_rel2_ingreso as dx_rel_1_ingreso_85, " +
								"cert.acronimo_dx_egreso as dx_ppal_egreso_86, " +
								"cert.acronimo_dx_rel1_egreso as dx_ppal_egreso_rel_1_87, " +
								"cert.acronimo_dx_rel2_egreso as dx_ppal_egreso_rel_2_88, " +
								"cert.primer_apellido_medico as primerapellidomedico_89,"+
								"cert.segundo_apellido_medico as segundoapellidomedico_90,"+
								"cert.primer_nombre_medico as primernombremedico_91,"+
								"cert.segundo_nombre_medico as segundonombremedico_92,"+
								"cert.tipo_documento_medico as tipodocmedico_93,"+
								"cert.nro_documento_medico as numerodocmedico_94,"+
								"cert.nro_registro_medico as numeroregistromedico_95,"+
								"coalesce(ampxrec.total_fac_amp_gast_medqx||'','') as total_fac_amp_qx_96, " +
								"coalesce(ampxrec.total_rec_amp_gast_medqx ||'','') as total_reclamo_amp_qx_97, " +
								"coalesce(ampxrec.total_fac_amp_gast_transmov ||'', '') as total_fac_amp_tx_98, " +
								"coalesce(ampxrec.total_rec_amp_gast_transmov ||'', '') as total_reclamo_amp_tx_99 " +
							"FROM " +
								"facturas f " +
								"INNER JOIN cuentas c ON(c.id=f.cuenta) " +
								"INNER JOIN ingresos i ON(i.id=c.id_ingreso)" +
								"INNER JOIN ingresos_reg_even_catastrofico irec ON(irec.ingreso=i.id) " +
								"INNER JOIN registro_evento_catastrofico rec ON(rec.codigo=irec.codigo_registro) " +
								"INNER JOIN manejopaciente.reclamaciones_acc_eve_fact reclam ON (reclam.codigo_evento=rec.codigo  and reclam.estado!='"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"') " +
								"INNER JOIN manejopaciente.amparo_x_reclamar ampxrec on (ampxrec.codigo_reclamacion=reclam.codigo_pk) " +
								"INNER JOIN manejopaciente.cert_aten_medica_furips cert on (cert.codigo_reclamacion=reclam.codigo_pk) " +
								"INNER JOIN personas p ON (p.codigo=c.codigo_paciente) " +
								"INNER JOIN pacientes pac ON(pac.codigo_paciente=p.codigo) " +
							"WHERE 1=1 ";
							if(mapaBusqueda.containsKey("fechainicial"))
							{
								consulta+=" and f.fecha between '"+UtilidadFecha.conversionFormatoFechaABD(mapaBusqueda.get("fechainicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(mapaBusqueda.get("fechafinal")+"")+"' ";
							}
							consulta+=" and f.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionFacturada+" ";
								
							if(!UtilidadTexto.isEmpty(reclamacion))
								consulta=consulta+" and reclam.codigo_pk in("+reclamacion+") ";
							
							consulta+=(institucionBasica.getEsEmpresaInstitucion())?"and f.empresa_institucion="+institucionBasica.getCodigo():"";	
							consulta+="" +
								"and f.institucion="+institucion+" "+((!UtilidadTexto.getBoolean(ValoresPorDefecto.getPermitirFacturarReclamarCuentasConRegistroPendientes(institucion)))?(" and rec.estado='"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"'"):"")+
						") ";
		
		logger.info("\n\n*************************************************************************************************************************************************************************");
		logger.info("CONSULTA FURIPS1 (EVENTOS CATASTROFICOS Y ACCIDENTES DE TRANSITO)--->"+consulta);
		logger.info("*************************************************************************************************************************************************************************\n\n");
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoFURIPS1 dto= new DtoFURIPS1();
				
				vectorFactura.add(rs.getString("codigofactura"));
				dto.setEsAccidenteTransito(UtilidadTexto.getBoolean(rs.getInt("esaccidentetransito")+""));
				dto.setEsDesplazado(UtilidadTexto.getBoolean(rs.getInt("esdesplazado")+""));
				dto.setEsEventoCatastrofico(UtilidadTexto.getBoolean(rs.getInt("eseventocatastrofico")+""));
				dto.setIdCuenta(rs.getInt("idcuenta")+"");
				dto.setNombreViaIngreso(rs.getString("nomviaingreso"));
				
				dto.setNumeroRadicadoAnterior(rs.getString("nroradicadoanterior_1"));
				dto.setRespuestaAGlosa(rs.getString("rg_2"));
				
				if(institucionPublica)
					dto.setNumeroFacturaCuentaCobro(rs.getString("numerocuentacobro_3"));
				else
					dto.setNumeroFacturaCuentaCobro(rs.getString("numerofactura_3"));
				
				dto.setIdFactura(rs.getString("numerofactura_3"));
				dto.setIdCuentaCobro(rs.getString("numerocuentacobro_3"));
				dto.setEsInstitucionPublica(institucionPublica);
				
				dto.setNumeroConsecutivoReclamacion(rs.getString("nroreclamacion_4"));
				dto.setCodigoHabilitacionPrestadorServicioDeSalud(institucionBasica.getCodMinsalud());
				dto.setPrimerApellidoVictima(rs.getString("primerapellido_6"));
				dto.setSegundoApellidoVictima(rs.getString("segundoapellido_7"));
				dto.setPrimerNombreVictima(rs.getString("primernombre_8"));
				dto.setSegundoNombreVictima(rs.getString("segundonombre_9"));
				dto.setTipoDocumentoVictima(rs.getString("tipoidentificacion_10"));
				dto.setNumeroDocumentoVictima(rs.getString("numeroidentificacion_11"));
				dto.setFechaNacimientoVictima(rs.getString("fechanacimiento_12"));
				dto.setSexoVictima(rs.getString("sexo_13"));
				dto.setDireccionResidenciaVictima(rs.getString("direccion_14"));
				dto.setCodigoDepartamentoResidenciaVictima(rs.getString("deptoubicacionvictima_15"));
				dto.setCodigoMunicipioResidenciaVictima(rs.getString("ciudadubicacionvictima_16"));
				dto.setTelefonoVictima(rs.getString("telefonovictima_17"));
				dto.setCodicionAccidentado(rs.getString("condicionaccidentado_18"));
				dto.setNaturalezaEvento(rs.getString("naturalezaevento_19"));
				dto.setDescripcionOtroEvento(rs.getString("descotroevento_20"));
				dto.setDireccionOcurrenciaEvento(rs.getString("direccionocurrencia_21"));
				dto.setFechaOcurrenciaEvento(rs.getString("fechaocurrencia_22"));
				dto.setHoraOcurrenciaEvento(rs.getString("horaocurrencia_23"));
				dto.setCodigoDepartamentoOcurrenciaEvento(rs.getString("codigodepto_24"));
				dto.setCodigoMunicipioOcorrenciaEvento(rs.getString("codigomunicipio_25"));
				dto.setZonaOcurrenciaEvento(rs.getString("zona_26"));
				dto.setEstadoAseguramiento(rs.getString("estadoaseguramiento_27"));
				dto.setMarca(rs.getString("marcavehiculo_28"));
				dto.setPlaca(rs.getString("placavehiculo_29"));
				dto.setTipoVehiculo(rs.getString("tipovehiculo_30"));
				dto.setCodigoAseguradora(rs.getString("codaseguradora_31"));
				dto.setNumeroPolizaSOAT(rs.getString("numpolizasoat_32"));
				dto.setFechaInicioVigenciaPoliza(rs.getString("fechainiciovigpoliza_33"));
				dto.setFechaFinalVigenciaPoliza(rs.getString("fechafinalvigpoliza_34"));
				dto.setIntervencionAutoridad(rs.getString("intervencionautoridad_35"));
				dto.setCobroExcedentePoliza(rs.getString("cobroexcedentepoliza_36"));
				dto.setPlacaSegundoVehiculoInvolucrado(rs.getString("placa2_37"));
				dto.setTipoDocumentoIdentidadPropietarioSegundoVehiculoInvolucrado(rs.getString("tipoid2_38"));
				dto.setNumeroDocumentoIdentidadPropietarioSegundoVehiculoInvolucrado(rs.getString("nroid2_39"));
				dto.setPlacaTercerVehiculoInvolucrado(rs.getString("placa3_40"));
				dto.setTipoDocumentoIdentidadPropietarioTercerVehiculoInvolucrado(rs.getString("tipoid3_41"));
				dto.setNumeroDocumentoIdentidadPropietarioTercerVehiculoInvolucrado(rs.getString("nroid3_42"));
				dto.setTipoDocumentoIdentidadPropietario(rs.getString("tipoidpropietario_43"));
				dto.setNumeroDocumentoIdentidadPropietario(rs.getString("numeroidprop_44"));
				dto.setPrimerApellidoORazonSocialPropietario(rs.getString("primerapellidoprop_45"));
				dto.setSegundoApellidoPropietario(rs.getString("segundoapellidoprop_46"));
				dto.setPrimerNombrePropietario(rs.getString("primernombreprop_47"));
				dto.setSegundoNombrePropietario(rs.getString("segundonombreprop_48"));
				dto.setDireccionResidenciaPropietario(rs.getString("direccionprop_49"));
				dto.setTelefonoResidenciaPropietario(rs.getString("telefonoprop_50"));
				dto.setCodigoDepartamentoResidenciaPropietario(rs.getString("departamentoprop_51"));
				dto.setCodigoMunicipioResidenciaPropietario(rs.getString("municipioprop_52"));
				dto.setPrimerApellidoConductor(rs.getString("primerapellidoconductor_53"));
				dto.setSegundoApellidoConductor(rs.getString("segundoapellidoconductor_54"));
				dto.setPrimerNombreConductor(rs.getString("primernombreconductor_55"));
				dto.setSegundoNombreConductor(rs.getString("segundonombreconductor_56"));
				dto.setTipoDocumentoConductor(rs.getString("tipo_id_conductor_57"));
				dto.setNumeroDocumentoConductor(rs.getString("numero_id_conductor_58"));
				dto.setDireccionResidenciaConductor(rs.getString("direccion_conductor_59"));
				dto.setCodigoDepartamentoResidenciaConductor(rs.getString("departamento_conductor_60"));
				dto.setCodigoMunicipioResidenciaConductor(rs.getString("ciudad_conductor_61"));
				dto.setTelefonoResidenciaConductor(rs.getString("telefono_conductor_62"));
				dto.setTipoReferencia(rs.getString("tiporeferencia_63"));
				dto.setFechaReferencia(rs.getString("fecharemision_64"));
				dto.setHoraSalida(rs.getString("horasalida_65"));
				dto.setCodigoHabilitacionPrestadorServiciosSaludRemitente(rs.getString("codhabilitacionips_66"));
				dto.setProfesionaRemite(rs.getString("profesional_remite_67"));
				dto.setCargoPersonaRemite(rs.getString("cargo_profesional_remitente_68"));
				dto.setFechaIngresoRemision(rs.getString("fechaaceptacion_69"));
				dto.setHoraIngreso(rs.getString("horaaceptacion_70"));
				dto.setCodigoHabilitacionPrestadorServiciosSaludReceptor(rs.getString("codhabilitacionreceptor_71"));
				dto.setProfesionalRecibe(rs.getString("profesional_recibe_72"));
				dto.setCargoPersonaRecibe(rs.getString("cargo_profesional_recibe_73"));
				dto.setPlacaTransporte(rs.getString("placatransporta_74"));
				dto.setTransporteVictimaDesde(rs.getString("transporta_victima_desde_75"));
				dto.setTransporteVictimaHasta(rs.getString("transporta_victima_hasta_76"));
				dto.setTipoServicioAmbulancia(rs.getString("tipo_servicio_77"));
				dto.setZonaDondeRecogeVictima(rs.getString("zonarecogevictima_78"));
				dto.setFechaIngreso(rs.getString("fecha_ingreso_79"));
				dto.setHoraIngreso(rs.getString("hora_ingreso_80"));
				
				if(!UtilidadTexto.isEmpty(rs.getString("fecha_egreso_81")))
					dto.setFechaEgreso(rs.getString("fecha_egreso_81"));
				else
					dto.setFechaEgreso(rs.getString("fecha_factura_81"));
				
				if(!UtilidadTexto.isEmpty(rs.getString("hora_egreso_82")))
					dto.setHoraEgreso(rs.getString("hora_egreso_82"));
				else
					dto.setHoraEgreso(rs.getString("hora_factura_82"));
				
				dto.setCodigoDiagnosticoPrincipalIngreso(rs.getString("dx_ppal_ingreso_83"));
				dto.setCodigoDiagnosticoIngreso1(rs.getString("dx_rel_1_ingreso_84"));
				dto.setCodigoDiagnosticoIngreso2(rs.getString("dx_rel_1_ingreso_85"));
				dto.setCodigoDiagnosticoPrincipalEgreso(rs.getString("dx_ppal_egreso_86"));
				dto.setCodigoDiagnosticoEgreso1(rs.getString("dx_ppal_egreso_rel_1_87"));
				dto.setCodigoDiagnosticoEgreso2(rs.getString("dx_ppal_egreso_rel_2_88"));
				
				dto.setPrimerApellidoMedico(rs.getString("primerapellidomedico_89"));
				dto.setSegundoApellidoMedico(rs.getString("segundoapellidomedico_90"));
				dto.setPrimerNombreMedico(rs.getString("primernombremedico_91"));
				dto.setSegundoNombreMedico(rs.getString("segundonombremedico_92"));
				dto.setTipoDocumentoMedico(rs.getString("tipodocmedico_93"));
				dto.setNumeroDocumentoMedico(rs.getString("numerodocmedico_94"));
				dto.setNumeroRegistroMedico(rs.getString("numeroregistromedico_95"));
				dto.setTotalFacturadoAmparoGastosMedicos(rs.getString("total_fac_amp_qx_96"));
				dto.setTotalReclamadoAmparoGastosMedicos(rs.getString("total_reclamo_amp_qx_97"));
				dto.setTotalFacturadoAmparoTransporteMovilizacion(rs.getString("total_fac_amp_tx_98"));
				dto.setTotalReclamadoAmparoTransporteMovilizacion(rs.getString("total_reclamo_amp_tx_99"));
				dto.setTotalFolios(mapaBusqueda.get("nrofolios_"+PlanosFURIPS.archivosEnum.Furips1+"")+"");
				array.add(dto);
			}
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}	
						
		return array;					
	}
	
	
	/**
	 * Metodo que carga toda la información pertinente para generar el archivo FURIPS2
	 * @param con
	 * @param mapaBusqueda
	 * @param institucionBasica
	 * @param institucion
	 * @return
	 */
	public static ArrayList<DtoFURIPS2> consultaFURIPS2(Connection con, HashMap<Object, Object> mapaBusqueda, InstitucionBasica institucionBasica, int institucion,String reclamacion)
	{
		boolean institucionPublica=ParametrizacionInstitucion.esInstitucionPublica(con, institucion);
		
		logger.info("institucion publica???-->"+institucionPublica);
		
		ArrayList<DtoFURIPS2> array= new ArrayList<DtoFURIPS2>();
		Vector<String> vectorFactura= new Vector<String>();
		vectorFactura.add(ConstantesBD.codigoNuncaValido+"");
		String consulta="(" +
							///PRIMERO CONSULTAMOS LOS ACCIDENTES DE TRANSITO
							"SELECT " +
								"0 as esdesplazado, " +
								"f.codigo||'' as codigofactura, " +
								"p.numero_identificacion||'' as numid, " +
								"p.tipo_identificacion as tipoid, " +
								"getnombrepersona(p.codigo) as nombrepersona, " +
								"c.id||'' as idcuenta, " +
								"CASE WHEN df.servicio IS NOT NULL THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS esservicio, " +
								"getnombreviaingreso(c.via_ingreso) as nomviaingreso, " +
								"f.consecutivo_factura as numerofactura_1, " +
								"coalesce(f.numero_cuenta_cobro||'','') as numerocuentacobro_1, " +
								"coalesce(reclam.nro_reclamacion||'','') as nroreclamacion_2, " +
								"ser.codigo as codigoservicio," +
								"ser.naturaleza_servicio as naturalezaservicio, " +
								"CASE " +
									"WHEN df.servicio IS NOT NULL THEN getcodigoservicio(df.servicio,"+mapaBusqueda.get("tipomanual")+") " +
									"ELSE  coalesce(a.numero_expediente, '')||' '||coalesce(a.cons_present_comercial, '') " +
								"END AS codigoservart, " +
								"a.codigo as codigoarticulo," +
								"natart.es_medicamento as esmedicamento," +
								"a.descripcion as descripcionarticulo,"+
								"df.cantidad_cargo||'' as cantidad, " +
								"(df.valor_cargo - df.valor_dcto_comercial + df.valor_recargo) ||'' as valorunitario, " +
								"(ampxrec.total_fac_amp_gast_medqx + ampxrec.total_fac_amp_gast_transmov) ||'' as valortotalfacturado, " +
								"(ampxrec.total_rec_amp_gast_medqx + ampxrec.total_rec_amp_gast_transmov) ||'' as valortotalreclamado " +
							"FROM " +
								"facturas f " +
								"INNER JOIN det_factura_solicitud df ON(df.factura=f.codigo) " +
								"INNER JOIN cuentas c ON(c.id=f.cuenta) " +
								"INNER JOIN ingresos i ON(i.id=c.id_ingreso) " +
								"INNER JOIN ingresos_registro_accidentes ira ON(ira.ingreso=i.id) " +
								"INNER JOIN registro_accidentes_transito rat ON(rat.codigo=ira.codigo_registro) " +
								"INNER JOIN manejopaciente.reclamaciones_acc_eve_fact reclam ON (reclam.codigo_accidente=rat.codigo and reclam.estado!='"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"') " +
								"INNER JOIN manejopaciente.amparo_x_reclamar ampxrec on (ampxrec.codigo_reclamacion=reclam.codigo_pk) " +
								"INNER JOIN personas p ON (p.codigo=c.codigo_paciente) " +
								"LEFT OUTER JOIN articulo a ON (a.codigo=df.articulo) " +
								"left outer join naturaleza_articulo natart on (a.naturaleza=natart.acronimo and a.institucion=natart.institucion)" +
								"LEFT OUTER JOIN facturacion.servicios ser on (ser.codigo=df.servicio) " +
							"WHERE 1=1 ";
							if(mapaBusqueda.containsKey("fechainicial"))
							{
								consulta+=" and f.fecha between '"+UtilidadFecha.conversionFormatoFechaABD(mapaBusqueda.get("fechainicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(mapaBusqueda.get("fechafinal")+"")+"' ";
							}
							consulta+=" and f.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionFacturada+" ";
								
							
							if(!UtilidadTexto.isEmpty(reclamacion))
								consulta=consulta+" and reclam.codigo_pk in("+reclamacion+") ";
		
							
							consulta+=(institucionBasica.getEsEmpresaInstitucion())?"and f.empresa_institucion="+institucionBasica.getCodigo()+" ":"";	
							consulta+="" +
								"and f.institucion="+institucion+" "+((!UtilidadTexto.getBoolean(ValoresPorDefecto.getPermitirFacturarReclamarCuentasConRegistroPendientes(institucion)))?(" and rat.estado='"+ConstantesIntegridadDominio.acronimoEstadoProcesado+"'"):"") +
							
						")" +
						"UNION " +
						"(" +
						///LUEGO CONSULTAMOS LOS eventos CATASTROFICOS
							"SELECT " +
								"0 as esdesplazado, " +
								"f.codigo||'' as codigofactura, " +
								"p.numero_identificacion||'' as numid, " +
								"p.tipo_identificacion as tipoid, " +
								"getnombrepersona(p.codigo) as nombrepersona, " +
								"c.id||'' as idcuenta, " +
								"CASE WHEN df.servicio IS NOT NULL THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS esservicio, " +
								"getnombreviaingreso(c.via_ingreso) as nomviaingreso, " +
								"f.consecutivo_factura as numerofactura_1, " +
								"coalesce(f.numero_cuenta_cobro||'','') as numerocuentacobro_1, " +
								"coalesce(reclam.nro_reclamacion||'','') as nroreclamacion_2, " +
								"ser.codigo as codigoservicio," +
								"ser.naturaleza_servicio as naturalezaservicio, " +
								"CASE " +
									"WHEN df.servicio IS NOT NULL THEN getcodigoservicio(df.servicio,"+mapaBusqueda.get("tipomanual")+") " +
									"ELSE  coalesce(a.numero_expediente, '')||' '||coalesce(a.cons_present_comercial, '') " +
								"END AS codigoservart, " +
								"a.codigo as codigoarticulo," +
								"natart.es_medicamento as esmedicamento," +
								"a.descripcion as descripcionarticulo,"+
								"df.cantidad_cargo||'' as cantidad, " +
								"(df.valor_cargo - df.valor_dcto_comercial + df.valor_recargo) ||'' as valorunitario, " +
								"(ampxrec.total_fac_amp_gast_medqx + ampxrec.total_fac_amp_gast_transmov) ||'' as valortotalfacturado, " +
								"(ampxrec.total_rec_amp_gast_medqx + ampxrec.total_rec_amp_gast_transmov) ||'' as valortotalreclamado " +
							"FROM " +
								"facturas f " +
								"INNER JOIN det_factura_solicitud df ON(df.factura=f.codigo) " +
								"INNER JOIN cuentas c ON(c.id=f.cuenta) " +
								"INNER JOIN ingresos i ON(i.id=c.id_ingreso) " +
								"INNER JOIN ingresos_reg_even_catastrofico irec ON(irec.ingreso=i.id) " +
								"INNER JOIN registro_evento_catastrofico rec ON(rec.codigo=irec.codigo_registro) " +
								"INNER JOIN manejopaciente.reclamaciones_acc_eve_fact reclam ON (reclam.codigo_evento=rec.codigo  and reclam.estado!='"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"') " +
								"INNER JOIN manejopaciente.amparo_x_reclamar ampxrec on (ampxrec.codigo_reclamacion=reclam.codigo_pk) " +
								"INNER JOIN personas p ON (p.codigo=c.codigo_paciente) " +
								"LEFT OUTER JOIN articulo a ON (a.codigo=df.articulo) " +
								"left outer join naturaleza_articulo natart on (a.naturaleza=natart.acronimo and a.institucion=natart.institucion)" +
								"LEFT OUTER JOIN facturacion.servicios ser on (ser.codigo=df.servicio) " +
							"WHERE 1=1 ";
							if(mapaBusqueda.containsKey("fechainicial"))
							{
								consulta+=" and f.fecha between '"+UtilidadFecha.conversionFormatoFechaABD(mapaBusqueda.get("fechainicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(mapaBusqueda.get("fechafinal")+"")+"' ";
							}
							consulta+=" and f.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionFacturada+" ";
								
							
							if(!UtilidadTexto.isEmpty(reclamacion))
								consulta=consulta+" and reclam.codigo_pk in("+reclamacion+") ";
							
							consulta+=(institucionBasica.getEsEmpresaInstitucion())?"and f.empresa_institucion="+institucionBasica.getCodigo()+" ":"";	
							consulta+="" +
								"and f.institucion="+institucion+" "+((!UtilidadTexto.getBoolean(ValoresPorDefecto.getPermitirFacturarReclamarCuentasConRegistroPendientes(institucion)))?(" and rec.estado='"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"'"):"")+
							"" +
						")";
		
		logger.info("\n\n**********************************************************************************************************************");
		logger.info("CONSULTA FURIPS 2 (EVENTOS Y ACCIDENTES)-->"+consulta);
		logger.info("**********************************************************************************************************************\n\n");
							
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoFURIPS2 dto= new DtoFURIPS2();
				
				vectorFactura.add(rs.getString("codigofactura"));
				
				dto.setEsDesplazado(UtilidadTexto.getBoolean(rs.getString("esdesplazado")));
				dto.setNumeroIdPaciente(rs.getString("numid"));
				dto.setTipoIdPaciente(rs.getString("tipoid"));
				dto.setNombresPaciente(rs.getString("nombrepersona"));
				dto.setIdCuenta(rs.getString("idcuenta"));
				dto.setEsServicio(UtilidadTexto.getBoolean( rs.getString("esservicio")));
				dto.setNombreViaIngreso(rs.getString("nomviaingreso"));
				
				if(institucionPublica)
					dto.setNumeroFacturaNumeroCXC(rs.getString("numerocuentacobro_1"));
				else
					dto.setNumeroFacturaNumeroCXC(rs.getString("numerofactura_1"));
					
				
				dto.setIdCuentaCobro(rs.getString("numerocuentacobro_1"));
				dto.setIdFactura(rs.getString("numerofactura_1"));
				dto.setEsInstitucionPublica(institucionPublica);
				dto.setNumeroConsecutivoReclamacion(rs.getString("nroreclamacion_2"));
				
				if(dto.isEsServicio())
				{
					String natServicio=rs.getString("naturalezaservicio");
					if(!natServicio.equals(ConstantesBD.codigoNaturalezaServicioMaterialesInsumos)&&!natServicio.equals(ConstantesBD.codigoNaturalezaServicioTrasladoPaciente)&&!natServicio.equals(ConstantesBD.codigoNaturalezaServicioNoAplica))
					{
						dto.setTipoServicio("2");
					}
					else if(!natServicio.equals(ConstantesBD.codigoNaturalezaServicioNoAplica))
					{
						if(ValoresPorDefecto.getManejoOxigenoFurips(institucion).equals("MEDICAMENTO"))
						{
							dto.setTipoServicio("1");
						}
						else
						{
							dto.setTipoServicio("5");
						}
					}
					else if(!natServicio.equals(ConstantesBD.codigoNaturalezaServicioTrasladoPaciente))
					{
						if(ValoresPorDefecto.getServiciosManejoTransPrimario().contains(rs.getInt("codigoservicio")))
						{
							dto.setTipoServicio("3");
						}
						else if(ValoresPorDefecto.getServiciosManejoTransSecundario().contains(rs.getInt("codigoservicio")))
						{
							dto.setTipoServicio("4");
						}
					}
				}
				else
				{
					if(UtilidadTexto.getBoolean( rs.getString("esmedicamento")))
					{
						dto.setTipoServicio("1");
					}
					else
					{
						dto.setTipoServicio("5");
					}
				}
				
				if(dto.getTipoServicio().equals("1")||dto.getTipoServicio().equals("2")||dto.getTipoServicio().equals("3"))
				{
					dto.setCodigoServicio(rs.getString("codigoservart"));
				}
				else					
				{
					dto.setCodigoServicio("");
				}
				//falta llenar el codigo del servicio
				
				
				if(dto.getTipoServicio().equals("5"))
				{
					dto.setDescripcionInsumo(rs.getString("descripcionarticulo"));
				}
				
				dto.setCantidadServicio(rs.getString("cantidad"));
				
				dto.setValorUnitario(rs.getString("valorunitario"));
				dto.setValorTotalFacgturado(rs.getString("valortotalfacturado"));
				dto.setValorTotalReclamadoFosyga(rs.getString("valortotalreclamado"));
				
				array.add(dto);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		
		
		return array;
	}
	
	/**
	 * Metodo que carga toda la información pertinente para generar el archivo FURPRO
	 * /**
	 * <%---DESCOMENTARIAR CUANDO SE HAGA EL DESARROLLO DE FURPRO Y FURTRAN (NO TOCA HACER NADA MAS A NIVEL DE VISTA NI ACTION - FORM)
	----POR OTRA PARTE YA SE ESTA HACIENDO TODO EL FLUJO PARA FURIPS1 Y FURIPS2, Y PARA COMPLETAR FURPRO Y FURTRAN 
	----SOLO FALTARIA HACER consultaFURPRO Y consultaFURTRAN (MUNDO) Y EN EL SQL TERMINAR LOS METODOS consultaFURTRAN consultaFURPRO
	---%>
	 * @param con
	 * @param mapaBusqueda
	 * @param institucionBasica
	 * @param institucion
	 * @return
	 */
	public static ArrayList<DtoFURPRO> consultaFURPRO(Connection con, HashMap<Object, Object> mapaBusqueda, InstitucionBasica institucionBasica, int institucion,String reclamacion)
	{
		boolean institucionPublica=ParametrizacionInstitucion.esInstitucionPublica(con, institucion);
		ArrayList<DtoFURPRO> array= new ArrayList<DtoFURPRO>();
		Vector<String> vectorFactura= new Vector<String>();
		vectorFactura.add(ConstantesBD.codigoNuncaValido+"");
		

		String consulta="	SELECT " +
								"f.codigo ||'' as codigofactura, " +
								"0 as esdesplazado, " +
								"c.id as idcuenta, " +
								"getnombreviaingreso(c.via_ingreso) as nomviaingreso, " +
								"coalesce(reclam.num_radica_anterior||'', '') as nroradicadoanterior, " +
								"CASE " +
									"WHEN reclam.respuesta_glosa='"+ConstantesIntegridadDominio.acronimoRespuestaGlosaPagoParcia+"' THEN '1' " +
									"WHEN reclam.respuesta_glosa='"+ConstantesIntegridadDominio.acronimoRespuestaGlosaGlosaTotal+"' THEN '0' " +
									"ELSE '' " +
								"END as rg, " +	
								"f.consecutivo_factura as numerofactura, " +
								"coalesce(f.numero_cuenta_cobro||'','') as numerocuentacobro, "+
								//codigoMinsalud se tiene en la institucionBasica
								"coalesce(p.primer_apellido, 'NN') as primerapellido, " +
								"coalesce(p.segundo_apellido, ' ') as segundoapellido, " +
								"coalesce(p.primer_nombre, 'NN') as primernombre, " +
								"coalesce(p.segundo_nombre, ' ') as segundonombre, " +
								"p.tipo_identificacion as tipoidentificacion, " +
								"CASE WHEN  " +
									"p.tipo_identificacion IN('"+ConstantesBD.acronimoTipoIdMenorSinId+"', '"+ConstantesBD.acronimoTipoIdAdultosSinId+"') " +
								"THEN " +
									"rec.departamento_evento ||''|| rec.ciudad_evento||'NN'|| coalesce(substr(pac.historia_clinica,1,9), '') " +
								"ELSE " +
									"p.numero_identificacion||'' " +
								"END as numeroidentificacion, " +								
								"to_char(p.fecha_nacimiento, 'DD/MM/YYYY') as fechanacimiento, " +
								"CASE WHEN p.sexo="+ConstantesBD.codigoSexoFemenino+" then 'F' else 'M' end as sexo, " +
								"p.direccion as direccion, " +
								"coalesce(p.codigo_departamento_vivienda, '') as deptoubicacionvictima, " +
								"coalesce(p.codigo_ciudad_vivienda, '') as ciudadubicacionvictima, " +
								"coalesce(p.telefono,'') as telefonovictima, " +
								"rec.SGSSS as sgsss,"+
								"rec.TIPO_REGIMEN as tiporegimen,"+
								"'' as codigoepseoc,"+
								"CASE " +
									"WHEN rec.naturaleza_evento="+ConstantesBD.codigoNatEventoExplosionTerrorista +" then '09' " +
									"WHEN rec.naturaleza_evento="+ConstantesBD.codigoNatEventoIncendioTerrorista +" then '10' " +
									"WHEN rec.naturaleza_evento="+ConstantesBD.codigoNatEventoCombate +" then '11' " +
									"WHEN rec.naturaleza_evento="+ConstantesBD.codigoNatEventoTomaGuerrillera +" then '12' " +
									"WHEN rec.naturaleza_evento="+ConstantesBD.codigoNatEventoMasacre +" then '13' " +
									"WHEN rec.naturaleza_evento="+ConstantesBD.codigoNatEventoMinaAntipersonal +" then '15' " +
									"WHEN rec.naturaleza_evento in("+ConstantesBD.codigoNatEventoOtros +", "+ConstantesBD.codigoNatEventoOtrosEnat+", "+ConstantesBD.codigoNatEventoOtrosEtec+") then '17' " +
								"END AS naturalezaevento, " +
								"CASE " +
									"WHEN rec.naturaleza_evento in("+ConstantesBD.codigoNatEventoOtros +", "+ConstantesBD.codigoNatEventoOtrosEnat+", "+ConstantesBD.codigoNatEventoOtrosEtec+", "+ConstantesBD.codigoNatEventoSustanciasToxicas+", "+ConstantesBD.codigoNatEventoCorrosivos+", "+ConstantesBD.codigoNatEventoEnvenenamiento+") then rec.desc_otro_evento " +
								"END AS descotroevento, " +
								"rec.direccion_evento as direccionocurrencia, " +
								"to_char(rec.fecha_evento, 'DD/MM/YYYY') as fechaocurrencia, " +
								"coalesce(rec.departamento_evento,'') as codigodepto, " +
								"coalesce(rec.ciudad_evento,'') as codigomunicipio, " +
								"coalesce(rec.zona_urbana_evento,'') as zona, " +
								"coalesce(rec.DESC_OCURRENCIA,'') as descevento, " +
								"cert.ACRONIMO_DX_EGRESO as dxprincipal," +
								"cert.ACRONIMO_DX_REL1_EGRESO as dxrel1," +
								"cert.ACRONIMO_DX_REL2_EGRESO as dxrel2," +
								"cert.ACRONIMO_DX_REL3_EGRESO as dxrel3," +
								"cert.ACRONIMO_DX_REL4_EGRESO as dxrel4," +
								"servrec.DESC_PROT_SERV_PRES||'' as desproservpres," +
								"servrec.VALOR_PROTESIS||'' as valorprotesis," +
								"servrec.VALOR_ADAP_PROTESIS||'' as valoradapprotesis," +
								"servrec.VALOR_REHABILITACION||'' as valorrehabilitacion," +
								"(servrec.VALOR_PROTESIS+servrec.VALOR_ADAP_PROTESIS+servrec.VALOR_REHABILITACION)||'' as valortotalreclamado " +								
							"FROM " +
								"facturas f " +
								"INNER JOIN cuentas c ON(c.id=f.cuenta) " +
								"INNER JOIN ingresos i ON(i.id=c.id_ingreso)" +
								"INNER JOIN ingresos_reg_even_catastrofico irec ON(irec.ingreso=i.id) " +
								"INNER JOIN registro_evento_catastrofico rec ON(rec.codigo=irec.codigo_registro) " +
								"INNER JOIN manejopaciente.reclamaciones_acc_eve_fact reclam ON (reclam.codigo_evento=rec.codigo and reclam.estado!='"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"') " +
								"INNER JOIN manejopaciente.servicios_reclamados servrec on (servrec.codigo_reclamacion=reclam.codigo_pk) " +
								"INNER JOIN manejopaciente.cert_aten_medica_furpro cert on (cert.codigo_reclamacion=reclam.codigo_pk) " +								
								"INNER JOIN personas p ON (p.codigo=c.codigo_paciente) " +
								"INNER JOIN pacientes pac ON(pac.codigo_paciente=p.codigo) " +
							"WHERE 1=1 "; 
							if(mapaBusqueda.containsKey("fechainicial"))
							{
								consulta+=" and f.fecha between '"+UtilidadFecha.conversionFormatoFechaABD(mapaBusqueda.get("fechainicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(mapaBusqueda.get("fechafinal")+"")+"' ";
							}
							consulta+=" and f.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionFacturada+" ";
							
							if(!UtilidadTexto.isEmpty(reclamacion))
								consulta=consulta+" and reclam.codigo_pk in("+reclamacion+") ";
							
							
							consulta+=(institucionBasica.getEsEmpresaInstitucion())?"and f.empresa_institucion="+institucionBasica.getCodigo():"";	
							consulta+="" +
								"and f.institucion="+institucion+" "+((!UtilidadTexto.getBoolean(ValoresPorDefecto.getPermitirFacturarReclamarCuentasConRegistroPendientes(institucion)))?(" and rec.estado='"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"'"):"")+
						" ";
		
		logger.info("\n\n*************************************************************************************************************************************************************************");
		logger.info("CONSULTA FURPRO --->"+consulta);
		logger.info("*************************************************************************************************************************************************************************\n\n");
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoFURPRO dto= new DtoFURPRO();
				
				vectorFactura.add(rs.getString("codigofactura"));
				dto.setEsDesplazado(UtilidadTexto.getBoolean(rs.getInt("esdesplazado")+""));
				dto.setIdCuenta(rs.getInt("idcuenta")+"");
				dto.setNombreViaIngreso(rs.getString("nomviaingreso"));
				dto.setNroRadicadoAnterior(rs.getString("nroradicadoanterior"));
				dto.setRg(rs.getString("rg"));
				
				if(institucionPublica)
					dto.setNroFactura_nroCxC(rs.getString("numerocuentacobro"));
				else
					dto.setNroFactura_nroCxC(rs.getString("numerofactura"));
				
				dto.setIdFactura(rs.getString("numerofactura"));
				dto.setIdCuentaCobro(rs.getString("numerocuentacobro"));
				dto.setEsInstitucionPublica(institucionPublica);
				dto.setCodHabilitacionPrestadorServ(institucionBasica.getCodMinsalud());
				dto.setPrimerApellidoVictima(rs.getString("primerapellido"));
				dto.setSegundoApellidoVictima(rs.getString("segundoapellido"));
				dto.setPrimerNombreVictima(rs.getString("primernombre"));
				dto.setSegundoNombreVictima(rs.getString("segundonombre"));
				

				dto.setTipoDocIdVictima(rs.getString("tipoidentificacion"));
				dto.setNumDocVictima(rs.getString("numeroidentificacion"));
				dto.setFechaNacVictima(rs.getString("fechanacimiento"));
				dto.setSexoVictima(rs.getString("sexo"));
				dto.setDirResidenciaVictima(rs.getString("direccion"));
				dto.setCodDeptoResidenciaVictima(rs.getString("deptoubicacionvictima"));
				dto.setCodMunicipioResidenciaVictima(rs.getString("ciudadubicacionvictima"));
				dto.setTelVictima(rs.getString("telefonovictima"));
				dto.setSGSSS(rs.getString("sgsss"));
				dto.setRegimenVictima(rs.getString("tiporegimen"));
				dto.setCodEPS_EOC(rs.getString("codigoepseoc"));
				dto.setNaturalezaEvento(rs.getString("naturalezaevento"));
				dto.setDescOtroEvento(rs.getString("descotroevento"));
				dto.setDirOcurrenciaVictima(rs.getString("direccionocurrencia"));
				dto.setFechaEvento(rs.getString("fechaocurrencia"));
				dto.setCodDeptoOcurrenciaEvento(rs.getString("codigodepto"));
				dto.setCodMunicipioOcurrioEvento(rs.getString("codigomunicipio"));
				dto.setZonaEvento(rs.getString("zona"));
				dto.setDescBreveEvento(rs.getString("descevento"));
				dto.setCodDxPpal(rs.getString("dxprincipal"));
				dto.setCodDxAsociado1(rs.getString("dxrel1"));
				dto.setCodDxAsociado2(rs.getString("dxrel2"));
				dto.setCodDxAsociado3(rs.getString("dxrel3"));
				dto.setCodDxAsociado4(rs.getString("dxrel4"));
				dto.setDescProtesisOServ(rs.getString("desproservpres"));
				dto.setValorReclamadoXprotesis(rs.getString("valorprotesis"));
				dto.setValorReclamadoXadaptacionProtesis(rs.getString("valoradapprotesis"));
				dto.setValorReclamadoXrehabilitacion(rs.getString("valorrehabilitacion"));
				dto.setValorTotalReclamado(rs.getString("valortotalreclamado"));				
				dto.setTotalFolios(mapaBusqueda.get("nrofolios_"+PlanosFURIPS.archivosEnum.Furpro1+"")+"");
				array.add(dto);
			}
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}	
						
		return array;
	}
	
	/**
	 * Metodo que carga toda la información pertinente para generar el archivo FURTRAN
	 * @param con
	 * @param mapaBusqueda
	 * @param institucionBasica
	 * @param institucion
	 * @return
	 */
	public static ArrayList<DtoFURTRAN> consultaFURTRAN(Connection con, HashMap<Object, Object> mapaBusqueda, InstitucionBasica institucionBasica, int institucion)
	{
		boolean institucionPublica=ParametrizacionInstitucion.esInstitucionPublica(con, institucion);
		ArrayList<DtoFURTRAN> array= new ArrayList<DtoFURTRAN>();
		Vector<String> vectorFactura= new Vector<String>();
		vectorFactura.add(ConstantesBD.codigoNuncaValido+"");
		
		//@todo pendiente X verificar en documentacion, no va por el momento para shaio
		
		return array;
	}
	
	/**
	 * 
	 * @param con
	 * @param rutasArchivos
	 * @param mapaBusqueda
	 * @param institucion
	 * @param usuario
	 * @return
	 */
	public static boolean insertarLogBD(Connection con, RutasArchivosFURIPS rutasArchivos, HashMap<Object, Object> mapaBusqueda, int institucion, String usuario)
	{
		String insert="INSERT INTO log_furips ( " +
												"codigo , " +  			//1
												"fecha_inicial , " +	//2
												"fecha_final , " +		//3
												"tipo_manual , " +		//4
												"fecha_modifica , " +	
												"hora_modifica , " +	
												"usuario_modifica , " +	//5
												"institucion" +			//6
											") " +
												"VALUES (?,?,?,?,CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?,?) ";
		
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insert,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int codigo= UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_log_furips");
			ps.setDouble(1, codigo);
			ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(mapaBusqueda.get("fechainicial")+"")));
			ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(mapaBusqueda.get("fechafinal")+"")));
			ps.setInt(4, Integer.parseInt(mapaBusqueda.get("tipomanual")+""));
			ps.setString(5, usuario);
			ps.setInt(6, institucion);
			
			if(ps.executeUpdate()>0)
			{	
				if(UtilidadTexto.getBoolean(mapaBusqueda.get(PlanosFURIPS.archivosEnum.Furips1+"")+""))
		    	{
					if(!insertarDetalleLogBD(con, rutasArchivos.getRutaFURIPS1()+rutasArchivos.getNombreArchivoFURIPS1(), codigo))
						return false;
					if(rutasArchivos.isCreoArchivoInconsistenciasFURIPS1())
					{
						if(!insertarDetalleLogBD(con, rutasArchivos.getRutaFURIPS1()+rutasArchivos.getNombreArchivoInconsistenciasFURIPS1(), codigo))
							return false;
					}
		    	}
				if(UtilidadTexto.getBoolean(mapaBusqueda.get(PlanosFURIPS.archivosEnum.Furips2+"")+""))
		    	{
					if(!insertarDetalleLogBD(con, rutasArchivos.getRutaFURIPS1()+rutasArchivos.getNombreArchivoFURIPS2(), codigo))
						return false;
					if(rutasArchivos.isCreoArchivoInconsistenciasFURIPS2())
					{
						if(!insertarDetalleLogBD(con, rutasArchivos.getRutaFURIPS2()+rutasArchivos.getNombreArchivoInconsistenciasFURIPS2(), codigo))
							return false;
					}
		    	}
				if(UtilidadTexto.getBoolean(mapaBusqueda.get(PlanosFURIPS.archivosEnum.Furpro1+"")+""))
		    	{
					if(!insertarDetalleLogBD(con, rutasArchivos.getRutaFURPRO()+rutasArchivos.getNombreArchivoFURPRO(), codigo))
						return false;
					if(rutasArchivos.isCreoArchivoInconsistenciasFURPRO())
					{
						if(!insertarDetalleLogBD(con, rutasArchivos.getRutaFURPRO()+rutasArchivos.getNombreArchivoInconsistenciasFURPRO(), codigo))
							return false;
					}
		    	}
				if(UtilidadTexto.getBoolean(mapaBusqueda.get(PlanosFURIPS.archivosEnum.Furtran+"")+""))
		    	{
					if(!insertarDetalleLogBD(con, rutasArchivos.getRutaFURTRAN()+rutasArchivos.getNombreArchivoFURTRAN(), codigo))
						return false;
					if(rutasArchivos.isCreoArchivoInconsistenciasFURTRAN())
					{
						if(!insertarDetalleLogBD(con, rutasArchivos.getRutaFURTRAN()+rutasArchivos.getNombreArchivoInconsistenciasFURTRAN(), codigo))
							return false;
					}
		    	}
			}
			else
				return false;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param con
	 * @param rutasArchivos
	 */
	private static boolean insertarDetalleLogBD(Connection con,String pathArchivo, double codigoEncabezado) 
	{
		String insert="INSERT INTO det_log_furips (codigo, codigo_log_furips, path_archivo) VALUES (?,?,?) ";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insert,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int codigo= UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_det_log_furips");
			ps.setDouble(1, codigo);
			ps.setDouble(2, codigoEncabezado);
			ps.setString(3, pathArchivo);
			
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
}