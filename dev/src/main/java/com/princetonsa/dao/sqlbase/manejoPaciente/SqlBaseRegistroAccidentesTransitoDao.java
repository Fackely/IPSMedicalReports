package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dto.manejoPaciente.DtoRegistroAccidentesTransito;

public class SqlBaseRegistroAccidentesTransitoDao
{

	 /**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger( SqlBaseRegistroAccidentesTransitoDao.class);
	
	/**
	 * 
	 */
	public static String cadenaConsultaGeneralAccidentesTransito=" SELECT " +
																		" codigo as codigo," +
																		" ingreso as ingreso," +
																		" empresa_trabaja as empresatrabaja," +
																		" direccion_empresa as direccionempresa," +
																		" telefono_empresa as telefonoempresa," +
																		" case when ciudad_empresa is null then '' else ciudad_empresa end as ciudadempresa," +
																		" case when departamento_empresa is null then '' else departamento_empresa end as departamentoempresa," +
																		" ocupante as ocupante," +
																		" condicion_accidentado as condicionaccidentado," +
																		" resulta_lesionado_al as resultalesionadoal," +
																		" placa_vehiculo_ocasiona as placavehiculoocasiona," +
																		" lugar_accidente as lugaraccidente," +
																		" lugar_accidente as lugaraccidente," +
																		" case when fecha_accidente is null then '' else fecha_accidente||'' end as fechaaccidente," +
																		" hora_accidente as horaaccidente," +
																		" case when ciudad_accidente is null then '' else ciudad_accidente end as ciudadaccidente," +
																		" case when departamento_accidente is null then '' else departamento_accidente end as departamentoaccidente," +
																		" zona_accidente as zonaaccidente," +
																		" informacion_accidente as informacionaccidente," +
																		" marca_vehiculo as marcavehiculo," +
																		" placa as placavehiculo," +
																		" coalesce(tipo_serv_v,"+ConstantesBD.codigoNuncaValido+") as tipovehiculo," +
																		" aseguradora as aseguradora," +
																		" case when agencia is null then '' else agencia end as agencia," +
																		" asegurado as asegurado, " +
																		" numero_poliza as numeropoliza, " +
																		" case when fecha_inicial_poliza is null then '' else fecha_inicial_poliza||'' end as fechainicialpoliza," +
																		" case when fecha_final_poliza is null then '' else fecha_final_poliza||'' end as fechafinalpoliza," +
																		" primer_apellido_conductor as primerapellidoconductor,"+
																		" segundo_apellido_conductor as segundoapellidoconductor,"+
																		" primer_nombre_conductor as primernombreconductor,"+
																		" segundo_nombre_conductor as segundonombreconductor,"+
																		" tipo_id_conductor as tipoidconductor," +
																		" numero_id_conductor as numeroidconductor," +
																		" case when ciu_expedicion_id_conductor is null then '' else ciu_expedicion_id_conductor end as ciuexpidconductor," +
																		" case when dep_expedicion_id_conductor is null then '' else dep_expedicion_id_conductor end as depexpidconductor," +
																		" direccion_conductor as direccionconductor," +
																		" case when ciudad_conductor  is null then '' else ciudad_conductor end as ciudadconductor," +
																		" case when departamento_conductor is null then '' else departamento_conductor end as departamentoconductor," +
																		" telefono_conductor as telefonoconductor," +
																		" apellido_nombre_declarante as apellidonombredeclarante," +
																		" tipo_id_declarante as tipoiddeclarante, " +
																		" numero_id_declarante as numeroiddeclarante," +
																		" case when ciu_expedicion_id_declarante is null then '' else ciu_expedicion_id_declarante end as ciuexpiddeclarante," +
																		" case when dep_expedicion_id_declarante is null then '' else dep_expedicion_id_declarante end as depexpiddeclarante," +
																		" fecha_grabacion as fechagrabacion," +
																		" hora_grabacion as horagrabacion," +
																		" usuario_grabacion as usuariograbacion," +
																		" estado as estado," +
																		" poliza as poliza, " +
																		" primer_apellido_prop as primerapellidoprop," +
																		" segundo_apellido_prop as segundoapellidoprop," +
																		" primer_nombre_prop as primernombreprop," +
																		" segundo_nombre_prop as segundonombreprop," +
																		" coalesce(tipo_id_prop,'') as tipoidprop,"+
																		" numero_id_prop as numeroidprop," +
																		" coalesce(ciudad_exp_id_prop,'') as ciudadexpidprop," +
																		" coalesce(depto_exp_id_prop,'') as deptoexpidprop," + 
																		" direccion_prop as direccionprop,"+
																		" telefono_prop as telefonoprop,"+
																		" coalesce(ciudad_prop,'') as ciudadprop,"+
																		" coalesce(departamento_prop,'') as departamentoprop," +																		
																		" case when fecha_anulacion is null then '' else to_char(fecha_anulacion,'DD/MM/YYYY') end as fechaanulacion, " +
																		" case when hora_anulacion is null then '' else hora_anulacion end as horaanulacion, " +
																		" case when usuario_anulacion is null then '' else usuario_anulacion end as usuarioanulacion, " +
																		" apellido_nombre_transporta as apellidonombretransporta," +
																		" coalesce(tipo_id_transporta,'') as tipoidtransporta," +
																		" numero_id_transporta as numeroidtransporta," +
																		" coalesce(ciudad_exp_id_transporta,'') as ciudadexpidprop," +
																		" coalesce(depto_exp_id_transporta,'') as deptoexpidprop," + 
																		" telefono_transporta as telefonotransporta," +
																		" direccion_transporta as direcciontransporta, " +
																		" coalesce(ciudad_transporta,'') as ciudadtransporta, " +
																		" coalesce(departamento_transporta,'') as departamentotransporta, " +
																		" transporta_victima_desde as transportavictimadesde, " +
																		" transporta_victima_hasta as transportavictimahasta, " +
																		" tipo_transporte as tipotransporte, " +
																		" placa_vehiculo_tranporta as placavehiculotranporta, " +
																		" coalesce(pais_empresa,'') as paisempresa, " +
																		" coalesce(pais_accidente,'') as paisaccidente, " +
																		" coalesce(pais_expedicion_id_conductor,'') as paisexpidconductor, " +
																		" coalesce(pais_conductor,'') as paisconductor, " +
																		" coalesce(pais_expedicion_id_declarante,'') as paisexpiddeclarante, " +
																		" coalesce(pais_exp_id_prop,'') as paisexpidprop, " +
																		" coalesce(pais_exp_id_transporta,'') as paisexpidtransporta, " +
																		" coalesce(pais_prop,'') as paisprop, " +
																		" coalesce(pais_transporta,'') as paistransporta," +
																		" coalesce(descripcion_ocurrencia,'') As descripcionbreveocurrencia," +
																		" otro_tipo_serv_v  As otrotiposervv, " +
																		" coalesce(intervencion_autoridad,'') As intervencionautoridad, " +
																		" coalesce(cobro_excedente_poliza,'') As cobroexcedentepoliza, " +
																		" coalesce(cantidad_otros_vehi_acc,0) As cantidadotrosvehiacc, " +
																		" CASE WHEN cantidad_otros_vehi_acc IS NULL OR cantidad_otros_vehi_acc=0 THEN '"+ConstantesBD.acronimoNo+"' ELSE '"+ConstantesBD.acronimoSi+"' END As existenotrosvehiacc," +
																		" coalesce(placa_2_vehiculo,'') As placa2vehiculo," +
																		" coalesce(tipo_id_2_vehiculo,'') AS tipoid2vehiculo, " +
																		" coalesce(nro_id_2_vehiculo,'') As nroid2vehiculo, " +
																		" coalesce(placa_3_vehiculo,'') As placa3vehiculo," +
																		" coalesce(tipo_id_3_vehiculo,'') AS tipoid3vehiculo, " +
																		" coalesce(nro_id_3_vehiculo,'') As nroid3vehiculo," +
																		" coalesce(tipo_referencia,'') As tiporeferencia," +
																		" coalesce(to_char(fecha_remision,'DD/MM/YYYY'),'') As fecharemision," +
																		" coalesce(hora_remision,'') As horaremision," +
																		" coalesce(cod_inscrip_remitente,'') As codinscripremitente," +
																		" coalesce(profesional_remite,'') As profesionalremite," +
																		" coalesce(cargo_profesional_remitente,'') As cargoprofesionalremitente," +
																		" coalesce(to_char(fecha_aceptacion,'DD/MM/YYYY'),'') As fechaaceptacion," +
																		" coalesce(hora_aceptacion,'') As horaaceptacion, " +
																		" coalesce(cod_inscrip_receptor,'') As codinscripreceptor, " +
																		" coalesce(profesional_recibe,'') As profesionalrecibe," +
																		" coalesce(cargo_profesional_recibe,'') As cargoprofesionalrecibe," +
																		" coalesce(otro_tipo_trans,'') As otrotipotrans," +
																		" coalesce(zona_transporte,'') As zonatransporte," +
																		" institucion As institucion," +
																		" total_fac_amp_qx As totalfacampqx," +
																		" total_reclamo_amp_qx As totalreclamoampqx," +
																		" total_fac_amp_tx As totalfacamptx," +
																		" total_reclamo_amp_tx As totalreclamoamptx," +
																		" es_reclamacion As esreclamacion, " +//desde aqui
																		" furips As furips," +
																		" furtran As furtran," +
																		" coalesce(resp_glosa,'') As respglosa," +
																		" nro_radicado_anterior As nroradicadoanterior, " +
																		" nro_cons_reclamacion As nroconsreclamacion " +
																	" from " +
																		" view_registro_accid_transito vrat " +
																	" where 1=1 ";
	
	

	/**
	 *encargado de consultar el listado de accidented de transito
	 *en estado procesado; 
	 */
	private static final String strConsultaListadoAccidentesTransito=" SELECT " +
																		" case when vrat.fecha_accidente is null then '' else to_char(vrat.fecha_accidente,'YYYY/MM/DD') end as fecha_accidente0," +
																		" case when vrat.departamento_accidente is null then '' else getnombredepto(vrat.pais_accidente,vrat.departamento_accidente) end as departamento_accidente1," +
																		" case when vrat.ciudad_accidente is null then '' else getnombreciudad(vrat.pais_accidente,vrat.departamento_accidente,vrat.ciudad_accidente) end as ciudad_accidente2," +
																		" vrat.lugar_accidente as lugar_accidente3," +
																		" case when vrat.asegurado='"+ConstantesBD.acronimoNo+"' then 'No' else case when  vrat.asegurado='"+ConstantesBD.acronimoSi+"' then 'Si' else case when vrat.asegurado='"+ConstantesIntegridadDominio.acronimoAseguradoFantasma+"' then 'Fantasma' end end end as asegurado4," +
																		" vrat.primer_apellido_conductor||' '||vrat.segundo_apellido_conductor||' '||vrat.primer_nombre_conductor||' '||vrat.segundo_nombre_conductor  As nombre_conductor5," +
																		" vrat.primer_apellido_prop||' '||vrat.segundo_apellido_prop||' '||vrat.primer_nombre_prop||' '||vrat.segundo_nombre_prop As nombre_propietario6," +
																		" vrat.codigo As codigo7, " +
																		" i.id as idingreso,"+
																		"  i.consecutivo as consecutivoingreso,"+
																		"  to_char(i.fecha_ingreso,'dd/mm/yyyy') as fechaingreso,"+
																		"  i.estado as estadoingreso,"+
																		"  i.centro_atencion as centroatencion,"+
																		"  cc.descripcion as desccentroatencion, "+
																		"  vi.identificador AS viaingreso " +  
																		" FROM  view_registro_accid_transito vrat" +
																		" INNER JOIN ingresos i ON (i.id=vrat.ingreso) " +
																		" inner join centro_atencion cc on(cc.consecutivo=i.centro_atencion) "+
																		" INNER JOIN cuentas cu ON cu.id_ingreso=i.ID "+
																		" INNER JOIN vias_ingreso vi ON vi.codigo=cu.via_ingreso ";
	/**
	 * string encargado de asociar el ingreso a un accidente
	 */												
	private static final String strAsociarAccidente =" INSERT INTO ingresos_registro_accidentes (ingreso,codigo_registro) VALUES (?,?)";

	/**
	 * 
	 */
	public static String cadenaModificarRegistroAccidentesTransito="UPDATE registro_accidentes_transito SET " +
																" empresa_trabaja = ?, " +
																" direccion_empresa = ?, " +
																" telefono_empresa = ?, " +
																" ciudad_empresa = ?, " +
																" departamento_empresa = ?, " +
																" ocupante = ?, " +
																" condicion_accidentado = ?, " +
																" resulta_lesionado_al = ?, " +
																" placa_vehiculo_ocasiona = ?, " +
																" lugar_accidente = ?, " +
																" fecha_accidente = ?, " +
																" hora_accidente = ?, " +
																" ciudad_accidente = ?, " +
																" departamento_accidente = ?, " +
																" zona_accidente = ?, " +
																" informacion_accidente = ?, " +
																" marca_vehiculo = ?, " +
																" placa = ?, " +
																" tipo_serv_v = ?, " +
																" aseguradora = ?, " +
																" agencia = ?, " +
																" asegurado = ?, " +
																" numero_poliza = ?, " +
																" fecha_inicial_poliza = ?, " +
																" fecha_final_poliza = ?, " +
																" primer_apellido_conductor = ?, " +
																" segundo_apellido_conductor = ?, " +
																" primer_nombre_conductor = ?, " +
																" segundo_nombre_conductor = ?, " +
																" tipo_id_conductor = ?, " +
																" numero_id_conductor = ?, " +
																" ciu_expedicion_id_conductor = ?, " +
																" dep_expedicion_id_conductor = ?, " +
																" direccion_conductor = ?, " +
																" ciudad_conductor = ?, " +
																" departamento_conductor = ?, " +
																" telefono_conductor = ?, " +
																" apellido_nombre_declarante = ?, " +
																" tipo_id_declarante = ?, " +
																" ciu_expedicion_id_declarante = ?, " +
																" dep_expedicion_id_declarante = ?, " +
																" fecha_grabacion = ?, " +
																" hora_grabacion = ?, " +
																" usuario_grabacion = ?, " +
																" estado = ?, " +
																" numero_id_declarante=?," +
																" poliza=?, " +
																" primer_apellido_prop=?," +
																" segundo_apellido_prop=?," +
																" primer_nombre_prop=?," +
																" segundo_nombre_prop=?," +
																" tipo_id_prop=?,"+
																" numero_id_prop=?,"+
																" ciudad_exp_id_prop =?,"+
																" depto_exp_id_prop  =?,"+
																" direccion_prop=?,"+
																" telefono_prop=?,"+
																" ciudad_prop=?,"+
																" departamento_prop=?," +
																" apellido_nombre_transporta=?," +
																" tipo_id_transporta=?," +
																" numero_id_transporta=?," +
																" ciudad_exp_id_transporta=?," +
																" depto_exp_id_transporta=?," +
																" telefono_transporta=?," +
																" direccion_transporta=?, " +
																" ciudad_transporta=?, " +
																" departamento_transporta=?, " +
																" transporta_victima_desde=?, " +
																" transporta_victima_hasta=?, " +
																" tipo_transporte=?, " +
																" placa_vehiculo_tranporta=?, " +
																" pais_empresa=?, " +
																" pais_accidente=?, " +
																" pais_expedicion_id_conductor=?, " +
																" pais_conductor=?, " +
																" pais_expedicion_id_declarante=?, " +
																" pais_exp_id_prop=?, " +
																" pais_exp_id_transporta=?, " +
																" pais_prop=?, " +
																" pais_transporta=?," +
																" descripcion_ocurrencia=?," +
																" intervencion_autoridad=?," +
																" cobro_excedente_poliza=?," +
																" cantidad_otros_vehi_acc=?," +
																" placa_2_vehiculo=?," +
																" tipo_id_2_vehiculo=?," +
																" nro_id_2_vehiculo=?," +
																" placa_3_vehiculo=?," +
																" tipo_id_3_vehiculo=?," +
																" nro_id_3_vehiculo=?," +
																" tipo_referencia=?," +
																" fecha_remision=?," +
																" hora_remision=?," +
																" cod_inscrip_remitente=?," +
																" profesional_remite=?," +
																" cargo_profesional_remitente=?," +
																" fecha_aceptacion=?," +
																" hora_aceptacion=?, " +
																" cod_inscrip_receptor=?, " +
																" profesional_recibe=?," +
																" cargo_profesional_recibe=?," +
																" otro_tipo_trans=?," +
																" zona_transporte=?, " +
																" institucion=?," +
																" total_fac_amp_qx=? ," +
																" total_reclamo_amp_qx=?," +
																" total_fac_amp_tx=?," +
																" total_reclamo_amp_tx=?," +
																" es_reclamacion=?, " +
																" furips=?," +
																" furtran=?," +
																" resp_glosa=?," +
																" nro_radicado_anterior=?, " +
																" nro_cons_reclamacion=? " +
														" WHERE codigo=? ";
	
	private static String cadenaModificacionAmparos =" UPDATE registro_accidentes_transito SET " +
																" total_fac_amp_qx=?," +
																" total_reclamo_amp_qx=?," +
																" total_fac_amp_tx=?," +
																" total_reclamo_amp_tx=?," +
																" fecha_grabacion = ?, " +
																" hora_grabacion = ?, " +
																" usuario_grabacion = ?  " +
															" WHERE codigo=? ";
	
	private static String cadenaModificacionReclamacion =" UPDATE registro_accidentes_transito SET " +
																" es_reclamacion=?, " +															
																" furips=?," +
																" furtran=?," +
																" resp_glosa=?," +
																" nro_radicado_anterior=?, " +
																" nro_cons_reclamacion=?, " +
																" fecha_grabacion = ?, " +
																" hora_grabacion = ?, " +
																" usuario_grabacion = ?  " +
															" WHERE codigo=? ";
	
	

	
	
	/**
	 * 
	 */
	public static String cadenaListadoAdmisiones=	"SELECT " +
														"r.codigo AS codigo, " +
														"cue.id as cuenta, " +
														"vi.codigo as codigoVia, " +
														"CASE WHEN vi.codigo="+ConstantesBD.codigoViaIngresoHospitalizacion+" THEN 'HOS' " +
														"ELSE CASE WHEN vi.codigo="+ConstantesBD.codigoViaIngresoAmbulatorios+" THEN 'AMB' " +
														"ELSE CASE WHEN vi.codigo="+ConstantesBD.codigoViaIngresoUrgencias+" THEN 'URG' " +
														"ELSE 'CE' " +
														"END " +
														"END " +
														"END as via, " +
														"getnomcentroatencion(ccos.centro_atencion) AS centroatencion, " +
														"getresumenespecialidad(vi.codigo,cue.id) as especialidad, " +
														"CASE WHEN vi.codigo="+ConstantesBD.codigoViaIngresoHospitalizacion+" THEN adh.fecha_admision ||' '|| adh.hora_admision " +
														"ELSE CASE WHEN vi.codigo="+ConstantesBD.codigoViaIngresoUrgencias+" THEN adu.fecha_admision ||' '|| adu.hora_admision " +
														"ELSE cue.fecha_apertura ||' '|| cue.hora_apertura " +
														"END " +
														"END AS fechahoraingresoformatobd, " +
														"CASE WHEN vi.codigo="+ConstantesBD.codigoViaIngresoHospitalizacion+" THEN TO_CHAR(adh.fecha_admision, 'DD/MM/YYYY') ||' '|| SUBSTR(adh.hora_admision, 1,5) " +
														"ELSE CASE WHEN vi.codigo="+ConstantesBD.codigoViaIngresoUrgencias+" THEN TO_CHAR(adu.fecha_admision, 'DD/MM/YYYY') ||' '|| SUBSTR(adu.hora_admision,1,5) " +
														"ELSE TO_CHAR(cue.fecha_apertura, 'DD/MM/YYYY') ||' '|| SUBSTR(cue.hora_apertura,1,5) " +
														"END " +
														"END AS fechahoraingreso, " +
														"CASE WHEN vi.codigo in ("+ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoUrgencias+") THEN eg.fecha_egreso ||' '|| eg.hora_egreso " +
														"ELSE cue.fecha_apertura ||' '|| cue.hora_apertura END AS fechahoraegresoformatobd, " +
														"CASE WHEN vi.codigo in ("+ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoUrgencias+") THEN TO_CHAR(eg.fecha_egreso, 'DD/MM/YYYY') ||' '|| SUBSTR(eg.hora_egreso,1,5) " +
														"ELSE TO_CHAR(cue.fecha_apertura, 'DD/MM/YYYY') ||' '|| SUBSTR(cue.hora_apertura,1,5) " +
														"END AS fechahoraegreso, " +
														"getnombrepersona(cue.codigo_paciente) AS paciente " +
													"FROM " +
														"cuentas cue " +
														"INNER JOIN  montos_cobro mc ON (cue.monto_cobro=mc.codigo) " +
														"INNER JOIN vias_ingreso vi ON(mc.via_ingreso=vi.codigo) " +
														"INNER JOIN view_registro_accid_transito r ON (r.ingreso=cue.id_ingreso) " +
														"LEFT OUTER JOIN admisiones_hospi adh ON (cue.id=adh.cuenta) " +
														"LEFT OUTER JOIN admisiones_urgencias adu ON (cue.id=adu.cuenta) " +
														"LEFT OUTER JOIN egresos eg ON (cue.id=eg.cuenta) " +
														"INNER JOIN centros_costo ccos ON (cue.area=ccos.codigo) " +
													"WHERE " +
														"cue.indicativo_acc_transito="+ValoresPorDefecto.getValorTrueParaConsultas()+" " +
														"AND r.estado='"+ConstantesIntegridadDominio.acronimoEstadoProcesado+"' ";
	
	/**
	 * Cadena que actualiza el estado del registro de accidentes de transito
	 */
	private static final String actualizarEstadoRegistroAccidentesTransitoStr = "UPDATE registro_accidentes_transito SET " +
		"estado = ?, " +
		"fecha_anulacion = ?, " +
		"hora_anulacion = ?, " +
		"usuario_anulacion = ? " +
		"WHERE codigo = ?";
	
	
	private static final String insertarStr= "INSERT INTO registro_accidentes_transito (" +
											" codigo, " +
											" empresa_trabaja, " +
											" direccion_empresa, " +
											" telefono_empresa, " +
											" ciudad_empresa, " +
											" departamento_empresa, " +
											" ocupante, " +
											" condicion_accidentado, " +
											" resulta_lesionado_al, " +
											" placa_vehiculo_ocasiona, " +
											" lugar_accidente, " +
											" fecha_accidente, " +
											" hora_accidente, " +
											" ciudad_accidente, " +
											" departamento_accidente, " +
											" zona_accidente, " +
											" informacion_accidente, " +
											" marca_vehiculo, " +
											" placa, " +
											" tipo_serv_v, " +
											" aseguradora, " +
											" agencia, " +
											" asegurado, " +
											" numero_poliza, " +
											" fecha_inicial_poliza, " +
											" fecha_final_poliza, " +
											" primer_apellido_conductor, " +
											" segundo_apellido_conductor, " +
											" primer_nombre_conductor, " +
											" segundo_nombre_conductor, " +
											" tipo_id_conductor, " +
											" numero_id_conductor, " +
											" ciu_expedicion_id_conductor, " +
											" dep_expedicion_id_conductor, " +
											" direccion_conductor, " +
											" ciudad_conductor, " +
											" departamento_conductor, " +
											" telefono_conductor, " +
											" apellido_nombre_declarante, " +
											" tipo_id_declarante, " +
											" ciu_expedicion_id_declarante, " +
											" dep_expedicion_id_declarante, " +
											" fecha_grabacion, " +
											" hora_grabacion, " +
											" usuario_grabacion, " +
											" estado, " +
											" numero_id_declarante," +
											" poliza, " +
											" primer_apellido_prop," +
											" segundo_apellido_prop," +
											" primer_nombre_prop," +
											" segundo_nombre_prop," +
											" tipo_id_prop,"+
											" numero_id_prop,"+
											" ciudad_exp_id_prop," +
											" depto_exp_id_prop,"+				
											" direccion_prop,"+
											" telefono_prop,"+
											" ciudad_prop,"+
											" departamento_prop," +
											" apellido_nombre_transporta," +
											" tipo_id_transporta," +
											" numero_id_transporta," +
											" ciudad_exp_id_transporta," +
											" depto_exp_id_transporta," +
											" telefono_transporta," +
											" direccion_transporta, " +
											" ciudad_transporta, " +
											" departamento_transporta, " +
											" transporta_victima_desde, " +
											" transporta_victima_hasta, " +
											" tipo_transporte, " +
											" placa_vehiculo_tranporta, " +
											" pais_empresa, " +
											" pais_accidente, " +
											" pais_expedicion_id_conductor, " +
											" pais_conductor, " +
											" pais_expedicion_id_declarante, " +
											" pais_exp_id_prop, " +
											" pais_exp_id_transporta, " +
											" pais_prop, " +
											" pais_transporta," +
											" descripcion_ocurrencia," +
											" intervencion_autoridad," +
											" cobro_excedente_poliza," +
											" cantidad_otros_vehi_acc,  " +
											" placa_2_vehiculo," +
											" tipo_id_2_vehiculo," +
											" nro_id_2_vehiculo," +
											" placa_3_vehiculo," +
											" tipo_id_3_vehiculo," +
											" nro_id_3_vehiculo, " +
											" tipo_referencia," +
											" fecha_remision," +
											" hora_remision," +
											" cod_inscrip_remitente," +
											" profesional_remite," +
											" cargo_profesional_remitente," +
											" fecha_aceptacion," +
											" hora_aceptacion, " +
											" cod_inscrip_receptor, " +
											" profesional_recibe," +
											" cargo_profesional_recibe," +
											" otro_tipo_trans," +
											" zona_transporte, " +
											" institucion, " +
											" total_fac_amp_qx, " +
											" total_reclamo_amp_qx, " +
											" total_fac_amp_tx, " +
											" total_reclamo_amp_tx, " +
											" es_reclamacion, " +
											" furips," +
											" furtran," +
											" resp_glosa," +
											" nro_radicado_anterior, " +
											" nro_cons_reclamacion " +
											") " +
											" VALUES " +
											"( " +
											"?,?,?,?,?,?,?,?,?,?," +
											"?,?,?,?,?,?,?,?,?,?," +
											"?,?,?,?,?,?,?,?,?,?," +
											"?,?,?,?,?,?,?,?,?,?," +
											"?,?,?,?,?,?,?,?,?,?," +
											"?,?,?,?,?,?,?,?,?,?," +
											"?,?,?,?,?,?,?,?,?,?," +
											"?,?,?,?,?,?,?,?,?,?," +
											"?,?,?,?,?,?,?,?,?,?," +
											"?,?,?,?,?,?,?,?,?,?," +
											"?,?,?,?,?,?,?,?,?,?," +
											"?,?,?,?,?,?" +
											")";
	
	
	
	/**
	 * Metodo encargado de asociar un accidente de transito 
	 * a un ingreso
	 * @param connection
	 * @param ingreso
	 * @param codigoAccidente
	 * @return
	 */
	public static boolean asociarAcciedente (Connection connection, String ingreso,String codigoAccidente )
	{
		logger.info("\n entro a asociarAcciedente ingreso --> "+ingreso+" codigo accidente -->"+codigoAccidente);
		
		String cadena=strAsociarAccidente;
		try 
		{
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet ));
			//pk del ingreso
			ps.setInt(1, Utilidades.convertirAEntero(ingreso));
			// pk del accidente
			ps.setDouble(2, Utilidades.convertirADouble(codigoAccidente));
			
			if (ps.executeUpdate()>0)
				return true;
			
		
		} catch (SQLException e)
		{
		 logger.info("\n problema asociando un accidente al ingreso "+e);
		}
		
		
		
		return false;
	}
	
	/**
	 * Metodo encargado de consultar todos los registros de
	 * accidentes de transito  filtrandolos por el estado. 
	 * @param connection
	 * @param criterios
	 * -------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * -------------------------
	 * -- estado
	 * @return
	 * ---------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * ---------------------------
	 * -- fechaAccidente0_
	 * -- departamentoAccidente1_
	 * -- ciudadAccidente2_
	 * -- lugarAccidente3_
	 * -- asegurado4_
	 * -- nombreConductor5
	 * -- nombrePropietario6
	 */
	public static HashMap cargarListadoAccidentesTransito (Connection connection,HashMap criterios)
	{
		if(criterios.containsKey("esPorIngresos")&&(criterios.get("esPorIngresos")+"").equals(ConstantesBD.acronimoSi))
		{
			return cargarListadoAccidentesIngresos(connection,criterios);
		}
		
		String cadena = strConsultaListadoAccidentesTransito;
		
		cadena=cadena+ " WHERE 1=1 ";
		if(criterios.containsKey("estado") && !UtilidadTexto.isEmpty(criterios.get("estado")+""))
		{
			cadena=cadena+" and vrat.estado= '"+ criterios.get("estado")+"'";
		}
		
		if(criterios.containsKey("paciente") && !UtilidadTexto.isEmpty(criterios.get("paciente")+""))
		{
			cadena=cadena+" and i.codigo_paciente="+ criterios.get("paciente");
		}

		cadena+=" order by fecha_accidente desc";
		HashMap resultado= new HashMap();
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection, cadena);
			
			resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			
		} 
		catch (SQLException  e)
		{
		 logger.info("\n problema en cargarListadoAccidentesTransito "+e);
		}
		
		return resultado;

	}
	
	
	
	/**
	 * Metodo encargado de consultar todos los registros de
	 * accidentes de transito  filtrandolos por el estado. 
	 * @param connection
	 * @param criterios
	 * -------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * -------------------------
	 * -- estado
	 * @return
	 * ---------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * ---------------------------
	 * -- fechaAccidente0_
	 * -- departamentoAccidente1_
	 * -- ciudadAccidente2_
	 * -- lugarAccidente3_
	 * -- asegurado4_
	 * -- nombreConductor5
	 * -- nombrePropietario6
	 */
	public static HashMap cargarListadoAccidentesIngresos (Connection connection,HashMap criterios)
	{
		logger.info("\n entro  cargarListadoAccidentesTransito --> "+criterios);
		String cadena = " SELECT " +
							" case when vrat.fecha_accidente is null then '' else to_char(vrat.fecha_accidente,'YYYY/MM/DD') end as fecha_accidente0," +
							" case when vrat.departamento_accidente is null then '' else getnombredepto(vrat.pais_accidente,vrat.departamento_accidente) end as departamento_accidente1," +
							" case when vrat.ciudad_accidente is null then '' else getnombreciudad(vrat.pais_accidente,vrat.departamento_accidente,vrat.ciudad_accidente) end as ciudad_accidente2," +
							" vrat.lugar_accidente as lugar_accidente3," +
							" case when vrat.asegurado='"+ConstantesBD.acronimoNo+"' then 'No' else case when  vrat.asegurado='"+ConstantesBD.acronimoSi+"' then 'Si' else case when vrat.asegurado='"+ConstantesIntegridadDominio.acronimoAseguradoFantasma+"' then 'Fantasma' end end end as asegurado4," +
							" vrat.primer_apellido_conductor||' '||vrat.segundo_apellido_conductor||' '||vrat.primer_nombre_conductor||' '||vrat.segundo_nombre_conductor  As nombre_conductor5," +
							" vrat.primer_apellido_prop||' '||vrat.segundo_apellido_prop||' '||vrat.primer_nombre_prop||' '||vrat.segundo_nombre_prop As nombre_propietario6," +
							" vrat.codigo As codigo7, " +
							" i.id as idingreso,"+
							"  i.consecutivo as consecutivoingreso,"+
							"  to_char(i.fecha_ingreso,'dd/mm/yyyy') as fechaingreso,"+
							"  i.estado as estadoingreso,"+
							"  i.centro_atencion as centroatencion,"+
							"  cc.descripcion as desccentroatencion, "+
							" vi.identificador AS viaingreso " +
							" FROM ingresos i " +
							" inner join cuentas c on (c.id_ingreso=i.id and c.indicativo_acc_transito="+ValoresPorDefecto.getValorTrueParaConsultas()+")" +
							" left outer JOIN view_registro_accid_transito vrat ON (i.id=vrat.ingreso) " +
							" inner join centro_atencion cc on(cc.consecutivo=i.centro_atencion) " +
							" INNER JOIN vias_ingreso vi ON vi.codigo=c.via_ingreso " +
							" WHERE 1=1 ";
		

		HashMap resultado= new HashMap();
		try 
		{
			//estado del accidente de transito
			//por silicitud de angi, debe mostrar todos los registros de accidentes
			//cadena+=" and  (i.estado ='ABI' or (i.estado ='CER'  and vrat.estado= '"+ConstantesIntegridadDominio.acronimoEstadoPendiente+"'))";
			cadena+="   AND i.codigo_paciente="+criterios.get("paciente");
			cadena+=" order by i.fecha_ingreso desc";
			logger.info("\n consulta --> "+cadena);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			
		} 
		catch (SQLException  e)
		{
		 logger.info("\n problema en cargarListadoAccidentesTransito "+e);
		}
		
		return resultado;
	}
	
	
	
	
	
	
	
	/**
	 * Metodo para insertar el Registro de Accidentes de Transito.
	 * @param con
	 * @param dto
	 * @return
	 */
	public static int insertarRegistroAccidentesTransito(Connection con,DtoRegistroAccidentesTransito dto)
	{
		int codigo=ConstantesBD.codigoNuncaValido;
		try
		{
			codigo=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_reg_acc_tra");
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo+""));
			ps.setString(2, dto.getEmpresaTrabaja());
			ps.setString(3, dto.getDireccionEmpresa());
			ps.setString(4, dto.getTelefonoEmpresa());
			if (UtilidadCadena.noEsVacio(dto.getCiudadEmpresa()))
				ps.setString(5, dto.getCiudadEmpresa());
			else
				ps.setNull(5, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getDepartamentoEmpresa()))
				ps.setString(6,dto.getDepartamentoEmpresa());
			else
				ps.setNull(6, Types.VARCHAR);

			ps.setString(7, dto.getOcupante());
			ps.setString(8, dto.getCodicionAccidentado());
			ps.setString(9, dto.getResultaLesionadoAl());
			ps.setString(10, dto.getPlacaVehiculoOcaciona());
			ps.setString(11, dto.getLugarAccidente());
			
			if (UtilidadCadena.noEsVacio(dto.getFechaAccidente()))
				ps.setDate(12,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dto.getFechaAccidente())));
			else
				ps.setNull(12, Types.DATE);
			
			ps.setString(13, dto.getHoraAccidente());
			
			if (UtilidadCadena.noEsVacio(dto.getCiudadAccidente()))
				ps.setString(14,dto.getCiudadAccidente());
			else
				ps.setNull(14, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getDepartamentoAccidente()))
				ps.setString(15,dto.getDepartamentoAccidente());
			else
				ps.setNull(15, Types.VARCHAR);
			
			ps.setString(16, dto.getZonaAccidente());
			ps.setString(17, dto.getInformacionAccidente());
			ps.setString(18, dto.getMarcaVehiculo());
			
			ps.setString(19, dto.getPlaca());
			
			if (UtilidadCadena.noEsVacio(dto.getTipo()) && Utilidades.convertirAEntero(dto.getTipo())>0)
				ps.setInt(20,Utilidades.convertirAEntero(dto.getTipo()));
			else
				ps.setNull(20, Types.INTEGER);
			
			if (UtilidadCadena.noEsVacio(dto.getAseguradora()))
				ps.setInt(21, Utilidades.convertirAEntero(dto.getAseguradora()));
			else
				ps.setNull(21, Types.INTEGER);
			
			ps.setString(22, dto.getAgencia());
			ps.setString(23, dto.getAsegurado());
			ps.setString(24, dto.getNumeroPoliza());
			
			if (UtilidadCadena.noEsVacio(dto.getFechaInicialPoliza()))
				ps.setDate(25, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dto.getFechaInicialPoliza())));
			else
				ps.setNull(25, Types.DATE);
			
			if (UtilidadCadena.noEsVacio(dto.getFechaFinalPoliza()))
				ps.setDate(26, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dto.getFechaFinalPoliza())));
			else
				ps.setNull(26, Types.DATE);
			
			ps.setString(27, dto.getPrimerApellidoConductor());
			ps.setString(28, dto.getSegundoApellidoConductor());
			ps.setString(29, dto.getPrimerNombreConductor());
			ps.setString(30, dto.getSegundoNombreConductor());
			
			if (UtilidadCadena.noEsVacio( dto.getTipoIdConductor()))
				ps.setString(31,  dto.getTipoIdConductor());
			else
				ps.setNull(31, Types.VARCHAR);
			
			ps.setString(32, dto.getNumeroIdConductor());

			if (UtilidadCadena.noEsVacio(dto.getCiuExpedicionIdConductor()))
				ps.setString(33,  dto.getCiuExpedicionIdConductor());
			else
				ps.setNull(33, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getDepExpedicionIdConductor()))
				ps.setString(34,  dto.getDepExpedicionIdConductor());
			else
				ps.setNull(34, Types.VARCHAR);
			
			ps.setString(35, dto.getDireccionConductor());
			
			if (UtilidadCadena.noEsVacio(dto.getCiudadConductor()))
				ps.setString(36,  dto.getCiudadConductor());
			else
				ps.setNull(36, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getDepartamentoConductor()))
				ps.setString(37,  dto.getDepartamentoConductor());
			else
				ps.setNull(37, Types.VARCHAR);
			
			ps.setString(38, dto.getTelefonoConductor());
			ps.setString(39, dto.getApellidoNombreDeclarante());

			if (UtilidadCadena.noEsVacio( dto.getTipoIdDeclarante()))
				ps.setString(40,   dto.getTipoIdDeclarante());
			else
				ps.setNull(40, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getCiuExpedicionIdDeclarante()))
				ps.setString(41,dto.getCiuExpedicionIdDeclarante());
			else
				ps.setNull(41, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getDepExpedicionIdDeclarante()))
				ps.setString(42,dto.getDepExpedicionIdDeclarante());
			else
				ps.setNull(42, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getFechaGrabacion()))
				ps.setDate(43,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dto.getFechaGrabacion())));
			else
				ps.setNull(43, Types.DATE);
			
			ps.setString(44, dto.getHoraGrabacion());
			ps.setString(45, dto.getUsuarioGrabacion());
			ps.setString(46, dto.getEstadoRegistro());	
			ps.setString(47, dto.getNumeroIdDeclarante());
			ps.setString(48, dto.getPoliza());
			ps.setString(49, dto.getPrimerApellidoProp());
			ps.setString(50, dto.getSegundoApellidoProp());
			ps.setString(51, dto.getPrimerNombreProp());
			ps.setString(52, dto.getSegundoNombreProp());
			
			if (UtilidadCadena.noEsVacio(dto.getTipoIdProp()))
				ps.setString(53,dto.getTipoIdProp());
			else
				ps.setNull(53, Types.VARCHAR);
			
			ps.setString(54, dto.getNumeroIdProp());
			
			if (UtilidadCadena.noEsVacio(dto.getCiudadExpedicionIdProp()))
				ps.setString(55,dto.getCiudadExpedicionIdProp());
			else
				ps.setNull(55, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getDeptoExpedicionIdProp()))
				ps.setString(56,dto.getDeptoExpedicionIdProp());
			else
				ps.setNull(56, Types.VARCHAR);
			
			ps.setString(57, dto.getDireccionProp());
			ps.setString(58, dto.getTelefonoProp());
			
			if (UtilidadCadena.noEsVacio(dto.getCiudadProp()))
				ps.setString(59,dto.getCiudadProp());
			else
				ps.setNull(59, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getDeptoProp()))
				ps.setString(60,dto.getDeptoProp());
			else
				ps.setNull(60, Types.VARCHAR);
			
			ps.setString(61, dto.getApellidoNombreTransporta());
			
			if (UtilidadCadena.noEsVacio(dto.getTipoIdTransporta()))
				ps.setString(62,dto.getTipoIdTransporta());
			else
				ps.setNull(62, Types.VARCHAR);
			
			ps.setString(63, dto.getNumeroIdTransporta());

			if (UtilidadCadena.noEsVacio(dto.getCiudadExpedicionIdTransporta()))
				ps.setString(64,dto.getCiudadExpedicionIdTransporta());
			else
				ps.setNull(64, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getDeptoExpedicionIdTransporta()))
				ps.setString(65,dto.getDeptoExpedicionIdTransporta());
			else
				ps.setNull(65, Types.VARCHAR);
			
			ps.setString(66, dto.getTelefonoTransporta());
			ps.setString(67, dto.getDireccionTransporta());
			
			
			if (UtilidadCadena.noEsVacio(dto.getCiudadTransporta()))
				ps.setString(68,dto.getCiudadTransporta());
			else
				ps.setNull(68, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getDepartamentoTransporta()))
				ps.setString(69,dto.getDepartamentoTransporta());
			else
				ps.setNull(69, Types.VARCHAR);
			
			ps.setString(70, dto.getTransportaVictimaDesde());
			ps.setString(71, dto.getTransportaVictimaHasta());
			ps.setString(72, dto.getTipoTransporte());
			ps.setString(73, dto.getPlacaVehiculoTransporta());
			
			if (UtilidadCadena.noEsVacio(dto.getPaisEmpresa()))
				ps.setString(74,dto.getPaisEmpresa());
			else
				ps.setNull(74, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getPaisAccidente()))
				ps.setString(75,dto.getPaisAccidente());
			else
				ps.setNull(75, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getPaisExpedicionIdConductor()))
				ps.setString(76,dto.getPaisExpedicionIdConductor());
			else
				ps.setNull(76, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getPaisConductor()))
				ps.setString(77,dto.getPaisConductor());
			else
				ps.setNull(77, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getPaisExpedicionIdDeclarante()))
				ps.setString(78,dto.getPaisExpedicionIdDeclarante());
			else
				ps.setNull(78, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getPaisExpedicionIdProp()))
				ps.setString(79,dto.getPaisExpedicionIdProp());
			else
				ps.setNull(79, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getPaisExpedicionIdTransporta()))
				ps.setString(80,dto.getPaisExpedicionIdTransporta());
			else
				ps.setNull(80, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getPaisProp()))
				ps.setString(81,dto.getPaisProp());
			else
				ps.setNull(81, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getPaisTransporta()))
				ps.setString(82,dto.getPaisTransporta());
			else
				ps.setNull(82, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getDescripcionBreveOcurrencia()+""))
				ps.setString(83,dto.getDescripcionBreveOcurrencia());
			else
				ps.setNull(83, Types.VARCHAR);
			
			
			if (UtilidadCadena.noEsVacio(dto.getIntervencionAutoridad()+""))
				ps.setString(84,dto.getIntervencionAutoridad());
			else
				ps.setNull(84, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getCobroExcedentePoliza()+""))
				ps.setString(85,dto.getCobroExcedentePoliza());
			else
				ps.setNull(85, Types.VARCHAR);
		
			if (UtilidadCadena.noEsVacio(dto.getCantidadOtrosVehiAcc()+"") && dto.getCantidadOtrosVehiAcc()>0)
				ps.setInt(86, dto.getCantidadOtrosVehiAcc());
			else
				ps.setNull(86, Types.INTEGER);

			if (UtilidadCadena.noEsVacio(dto.getPlaca2Vehiculo()+""))
				ps.setString(87,dto.getPlaca2Vehiculo());
			else
				ps.setNull(87, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getTipoId2Vehiculo()+""))
				ps.setString(88,dto.getTipoId2Vehiculo());
			else
				ps.setNull(88, Types.VARCHAR);	
			
			if (UtilidadCadena.noEsVacio(dto.getNroId2Vehiculo()+""))
					ps.setString(89,dto.getNroId2Vehiculo());
				else
					ps.setNull(89, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getPlaca3Vehiculo()+""))
					ps.setString(90,dto.getPlaca3Vehiculo());
				else
					ps.setNull(90, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getTipoId3Vehiculo()+""))
					ps.setString(91,dto.getTipoId3Vehiculo());
				else
					ps.setNull(91, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getNroId3Vehiculo()+""))
					ps.setString(92,dto.getNroId3Vehiculo());
				else
					ps.setNull(92, Types.VARCHAR);

			if (UtilidadCadena.noEsVacio(dto.getTipoReferenciaRem()))
				ps.setString(93, dto.getTipoReferenciaRem());
			else
				ps.setNull(93, Types.VARCHAR);

			if (UtilidadCadena.noEsVacio(dto.getFechaRem()))
				ps.setDate(94, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dto.getFechaRem())));
			else
				ps.setNull(94, Types.DATE);
			
			if (UtilidadCadena.noEsVacio(dto.getHoraRem()))
				ps.setString(95, dto.getHoraRem());
			else
				ps.setNull(95, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getPrestadorRem()))
				ps.setString(96, dto.getPrestadorRem());
			else
				ps.setNull(96, Types.VARCHAR);
			
			
			if (UtilidadCadena.noEsVacio(dto.getProfesionalRem()))
				ps.setString(97, dto.getProfesionalRem());
			else
				ps.setNull(97, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getCargoProfesionalRem()))
				ps.setString(98, dto.getCargoProfesionalRem());
			else
				ps.setNull(98, Types.VARCHAR);
			

			if (UtilidadCadena.noEsVacio(dto.getFechaAceptacion()))
				ps.setDate(99, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dto.getFechaAceptacion())));
			else
				ps.setNull(99, Types.DATE);
			
			if (UtilidadCadena.noEsVacio(dto.getHoraAceptacion()))
				ps.setString(100, dto.getHoraAceptacion());
			else
				ps.setNull(100, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getPrestadorRecibe()))
				ps.setString(101, dto.getPrestadorRecibe());
			else
				ps.setNull(101, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getProfesionalRecibe()))
				ps.setString(102, dto.getProfesionalRecibe());
			else
				ps.setNull(102, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getCargoProfesionalRecibe()))
				ps.setString(103, dto.getCargoProfesionalRecibe());
			else
				ps.setNull(103, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getOtroTipoTrans()))
				ps.setString(104, dto.getOtroTipoTrans());
			else
				ps.setNull(104, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getZonaTransporte() ))
				ps.setString(105, dto.getZonaTransporte());
			else
				ps.setNull(105, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getInstitucion()+"") && dto.getInstitucion()>0)
				ps.setInt(106, dto.getInstitucion());
			else
				ps.setNull(106, Types.INTEGER);
			
			////////////////////////////////////////////////////
					
			if (UtilidadCadena.noEsVacio(dto.getTotalFacAmpQx()+"") && dto.getTotalFacAmpQx()>0)
				ps.setDouble(107, dto.getTotalFacAmpQx());
			else
				ps.setNull(107, Types.DOUBLE);
			
			if (UtilidadCadena.noEsVacio(dto.getTotalReclamoAmpQx()+"") && dto.getTotalReclamoAmpQx()>0)
				ps.setDouble(108, dto.getTotalReclamoAmpQx());
			else
				ps.setNull(108, Types.DOUBLE);
			
			if (UtilidadCadena.noEsVacio(dto.getTotalFacAmpTx()+"") && dto.getTotalFacAmpTx()>0)
				ps.setDouble(109, dto.getTotalFacAmpTx());
			else
				ps.setNull(109, Types.DOUBLE);
			
			if (UtilidadCadena.noEsVacio(dto.getTotalReclamoAmpTx()+"") && dto.getTotalReclamoAmpTx()>0)
				ps.setDouble(110, dto.getTotalReclamoAmpTx());
			else
				ps.setNull(110, Types.DOUBLE);
			///////////////////////////////////////
			
			
			if (UtilidadCadena.noEsVacio(dto.getEsReclamacion() ))
				ps.setString(111, dto.getEsReclamacion());
			else
				ps.setNull(111, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getFurips() ))
				ps.setString(112, dto.getFurips());
			else
				ps.setNull(112, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getFurtran() ))
				ps.setString(113, dto.getFurtran());
			else
				ps.setNull(113, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getRespGlosa() ))
				ps.setString(114, dto.getRespGlosa());
			else
				ps.setNull(114, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getNroRadicadoAnterior() ))
				ps.setString(115, dto.getNroRadicadoAnterior());
			else
				ps.setNull(115, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getNroConsReclamacion()+"") && dto.getNroConsReclamacion()>0)
				ps.setLong(116, dto.getNroConsReclamacion());
			else
				ps.setNull(116, Types.NUMERIC);
			
			if(ps.executeUpdate()>0)
			{
				ps= new PreparedStatementDecorator(con.prepareStatement("INSERT INTO ingresos_registro_accidentes(ingreso,codigo_registro) values (?,?)",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, Utilidades.convertirAEntero(dto.getIngreso()));
				ps.setDouble(2, Utilidades.convertirADouble(codigo+""));
				ps.executeUpdate();
			}
		}
		catch(SQLException e)
		{
			logger.error("[insertarRegistroAccidentesTransito - SqlBaseRegistroAccidentesTransito] ERROR INSERTANDO EL REGISTRO ACCIDENTES TRANSITO ");
			e.printStackTrace();
			codigo=ConstantesBD.codigoNuncaValido;
		}
		return codigo;
	}
	
	/**
	 * Metodo encargado de modificar la seccion de maparos
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean modificarAmparos(Connection con,DtoRegistroAccidentesTransito dto)
	{
		logger.info("\n entro a modificarAmparos dto:");
		logger.info("\n dto.getTotalFacAmpQx():"+dto.getTotalFacAmpQx());
		logger.info("\n dto.getTotalReclamoAmpQx():"+dto.getTotalReclamoAmpQx());
		logger.info("\n dto.getTotalFacAmpTx():"+dto.getTotalFacAmpTx());
		logger.info("\n dto.getTotalReclamoAmpTx():"+dto.getTotalReclamoAmpTx());
		logger.info("\n dto.getCodigo():"+dto.getCodigo());
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionAmparos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			if (UtilidadCadena.noEsVacio(dto.getTotalFacAmpQx()+"") && dto.getTotalFacAmpQx()>0)
				ps.setDouble(1, dto.getTotalFacAmpQx());
			else
				ps.setNull(1, Types.DOUBLE);
			
			if (UtilidadCadena.noEsVacio(dto.getTotalReclamoAmpQx()+"") && dto.getTotalReclamoAmpQx()>0)
				ps.setDouble(2, dto.getTotalReclamoAmpQx());
			else
				ps.setNull(2, Types.DOUBLE);
			
			
			if (UtilidadCadena.noEsVacio(dto.getTotalFacAmpTx()+"") && dto.getTotalFacAmpTx()>0)
				ps.setDouble(3, dto.getTotalFacAmpTx());
			else
				ps.setNull(3, Types.DOUBLE);
			
			if (UtilidadCadena.noEsVacio(dto.getTotalReclamoAmpTx()+"") && dto.getTotalReclamoAmpTx()>0)
				ps.setDouble(4, dto.getTotalReclamoAmpTx());
			else
				ps.setNull(4, Types.DOUBLE);
			
			if (UtilidadCadena.noEsVacio(dto.getFechaGrabacion()))
				ps.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dto.getFechaGrabacion())));
			else
				ps.setNull(5, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getHoraGrabacion()))
				ps.setString(6, dto.getHoraGrabacion());
			else
				ps.setNull(6, Types.VARCHAR);
			
			ps.setString(7, dto.getUsuarioGrabacion());
			
			ps.setString(8, dto.getCodigo());
			ps.executeUpdate();
			
		}catch (SQLException e) {
			logger.info("\n problema actualizando los datos de los amparos "+e);
			return false;
		}
		
		return true;
	}
	
	
	
	
	
	
	
	
	/**
	 * Metodo encargado de modificar los datos de la Reclamacion
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean modificarReclamacion(Connection con,DtoRegistroAccidentesTransito dto)
	{
		logger.info("\n entro a modificarReclamacion dto:");
		logger.info("\n getEsReclamacion dto:"+dto.getEsReclamacion());
		logger.info("\n getFurips dto:"+dto.getFurips());
		logger.info("\n getFurtran dto:"+dto.getFurtran());
		logger.info("\n getRespGlosa dto:"+dto.getRespGlosa());
		logger.info("\n getNroRadicadoAnterior dto:"+dto.getNroRadicadoAnterior());
		logger.info("\n getNroConsReclamacion dto:"+dto.getNroConsReclamacion());
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionReclamacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			

			if (UtilidadCadena.noEsVacio(dto.getEsReclamacion() ))
				ps.setString(1, dto.getEsReclamacion());
			else
				ps.setNull(1, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getFurips() ))
				ps.setString(2, dto.getFurips());
			else
				ps.setNull(2, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getFurtran() ))
				ps.setString(3, dto.getFurtran());
			else
				ps.setNull(3, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getRespGlosa() ))
				ps.setString(4, dto.getRespGlosa());
			else
				ps.setNull(4, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getNroRadicadoAnterior() ))
				ps.setString(5, dto.getNroRadicadoAnterior());
			else
				ps.setNull(5, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getNroConsReclamacion()+"") && dto.getNroConsReclamacion()>0)
				ps.setLong(6, dto.getNroConsReclamacion());
			else
				ps.setNull(6, Types.NUMERIC);
			
			if (UtilidadCadena.noEsVacio(dto.getFechaGrabacion()))
				ps.setDate(7, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dto.getFechaGrabacion())));
			else
				ps.setNull(7, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getHoraGrabacion()))
				ps.setString(8, dto.getHoraGrabacion());
			else
				ps.setNull(8, Types.VARCHAR);
			
			ps.setString(9, dto.getUsuarioGrabacion());			
			ps.setString(10, dto.getCodigo());
			
			ps.executeUpdate();
			
		}catch (SQLException e) {
			logger.info("\n problema actualizando los datos de la reclamacion "+e);
			return false;
		}
		
		return true;
	}
	
	
	
	
	
	
	
	
	
	

	/**
	 * Metodo que modifica un registro de accidentes de transito.
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean modificarRegistroAccidentesTransito(Connection con,DtoRegistroAccidentesTransito dto)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificarRegistroAccidentesTransito,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setString(1, dto.getEmpresaTrabaja());
			ps.setString(2, dto.getDireccionEmpresa());
			ps.setString(3, dto.getTelefonoEmpresa());
			
			
			if (UtilidadCadena.noEsVacio(dto.getCiudadEmpresa()))
				ps.setString(4,dto.getCiudadEmpresa());
			else
				ps.setNull(4, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getDepartamentoEmpresa()))
				ps.setString(5,dto.getDepartamentoEmpresa());
			else
				ps.setNull(5, Types.VARCHAR);
			
			ps.setString(6, dto.getOcupante());
			ps.setString(7, dto.getCodicionAccidentado());
			ps.setString(8, dto.getResultaLesionadoAl());
			ps.setString(9, dto.getPlacaVehiculoOcaciona());
			ps.setString(10, dto.getLugarAccidente());
			
			if (UtilidadCadena.noEsVacio(dto.getFechaAccidente()))
				ps.setDate(11,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dto.getFechaAccidente())));
			else
				ps.setNull(11, Types.DATE);
			
			ps.setString(12, dto.getHoraAccidente());
			
			if (UtilidadCadena.noEsVacio(dto.getCiudadAccidente()))
				ps.setString(13,dto.getCiudadAccidente());
			else
				ps.setNull(13, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getDepartamentoAccidente()))
				ps.setString(14,dto.getDepartamentoAccidente());
			else
				ps.setNull(14, Types.VARCHAR);
			
			ps.setString(15, dto.getZonaAccidente());
			ps.setString(16, dto.getInformacionAccidente());
			ps.setString(17, dto.getMarcaVehiculo());
			ps.setString(18, dto.getPlaca());
			
			if (UtilidadCadena.noEsVacio(dto.getTipo()) && Utilidades.convertirAEntero(dto.getTipo())>0)
				ps.setInt(19, Utilidades.convertirAEntero(dto.getTipo()));
			else
				ps.setNull(19, Types.INTEGER);
			
			
			if (UtilidadCadena.noEsVacio(dto.getAseguradora()))
				ps.setInt(20, Utilidades.convertirAEntero(dto.getAseguradora()));
			else
				ps.setNull(20, Types.INTEGER);
				
			ps.setString(21, dto.getAgencia());
			ps.setString(22, dto.getAsegurado());
			ps.setString(23, dto.getNumeroPoliza());
			
			if (UtilidadCadena.noEsVacio(dto.getFechaInicialPoliza()))
				ps.setDate(24, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dto.getFechaInicialPoliza())));
			else
				ps.setNull(24, Types.DATE);
			
			if (UtilidadCadena.noEsVacio(dto.getFechaFinalPoliza()))
				ps.setDate(25, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dto.getFechaFinalPoliza())));
			else
				ps.setNull(25, Types.DATE);
			
			ps.setString(26, dto.getPrimerApellidoConductor());
			ps.setString(27, dto.getSegundoApellidoConductor());
			ps.setString(28, dto.getPrimerNombreConductor());
			ps.setString(29, dto.getSegundoNombreConductor());
			

			if (UtilidadCadena.noEsVacio(dto.getTipoIdConductor()))
				ps.setString(30, dto.getTipoIdConductor());
			else
				ps.setNull(30, Types.VARCHAR);
			
			ps.setString(31, dto.getNumeroIdConductor());
			
			if (UtilidadCadena.noEsVacio(dto.getCiuExpedicionIdConductor()))
				ps.setString(32, dto.getCiuExpedicionIdConductor());
			else
				ps.setNull(32, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getDepExpedicionIdConductor()))
				ps.setString(33, dto.getDepExpedicionIdConductor());
			else
				ps.setNull(33, Types.VARCHAR);
			
			ps.setString(34, dto.getDireccionConductor());
			
			if (UtilidadCadena.noEsVacio(dto.getCiudadConductor()))
				ps.setString(35, dto.getCiudadConductor());
			else
				ps.setNull(35, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getDepartamentoConductor()))
				ps.setString(36, dto.getDepartamentoConductor());
			else
				ps.setNull(36, Types.VARCHAR);
			
			ps.setString(37, dto.getTelefonoConductor());
			ps.setString(38, dto.getApellidoNombreDeclarante());
			
			if (UtilidadCadena.noEsVacio(dto.getTipoIdDeclarante()))
				ps.setString(39, dto.getTipoIdDeclarante());
			else
				ps.setNull(39, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getCiuExpedicionIdDeclarante()))
				ps.setString(40, dto.getCiuExpedicionIdDeclarante());
			else
				ps.setNull(40, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getDepExpedicionIdDeclarante()))
				ps.setString(41, dto.getDepExpedicionIdDeclarante());
			else
				ps.setNull(41, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getFechaGrabacion()))
				ps.setDate(42, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dto.getFechaGrabacion())));
			else
				ps.setNull(42, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getHoraGrabacion()))
				ps.setString(43, dto.getHoraGrabacion());
			else
				ps.setNull(43, Types.VARCHAR);
			
			ps.setString(44, dto.getUsuarioGrabacion());
			ps.setString(45, dto.getEstadoRegistro());	
			ps.setString(46, dto.getNumeroIdDeclarante());
			ps.setString(47, dto.getPoliza());
			ps.setString(48, dto.getPrimerApellidoProp());
			ps.setString(49, dto.getSegundoApellidoProp());
			ps.setString(50, dto.getPrimerNombreProp());
			//Se guarda es el segundo nombre no el segundo apellido tarea 180233
			ps.setString(51, dto.getSegundoNombreProp());
			
			if (UtilidadCadena.noEsVacio(dto.getTipoIdProp()))
				ps.setString (52, dto.getTipoIdProp());
			else
				ps.setNull(52, Types.VARCHAR);
			
			ps.setString(53, dto.getNumeroIdProp());
			
			if (UtilidadCadena.noEsVacio(dto.getCiudadExpedicionIdProp()))
				ps.setString (54, dto.getCiudadExpedicionIdProp());
			else
				ps.setNull(54, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getDeptoExpedicionIdProp()))
				ps.setString (55, dto.getDeptoExpedicionIdProp());
			else
				ps.setNull(55, Types.VARCHAR);
			
			ps.setString(56, dto.getDireccionProp());
			ps.setString(57, dto.getTelefonoProp());
			
			if (UtilidadCadena.noEsVacio(dto.getCiudadProp()))
				ps.setString (58, dto.getCiudadProp());
			else
				ps.setNull(58, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio( dto.getDeptoProp()))
				ps.setString (59,  dto.getDeptoProp());
			else
				ps.setNull(59, Types.VARCHAR);
			
			ps.setString(60, dto.getApellidoNombreTransporta());
			
			if (UtilidadCadena.noEsVacio(dto.getTipoIdTransporta()))
				ps.setString (61,dto.getTipoIdTransporta());
			else
				ps.setNull(61, Types.VARCHAR);
			
			ps.setString(62, dto.getNumeroIdTransporta());
			
			if (UtilidadCadena.noEsVacio(dto.getCiudadExpedicionIdTransporta()))
				ps.setString (63,dto.getCiudadExpedicionIdTransporta());
			else
				ps.setNull(63, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getDeptoExpedicionIdTransporta()))
				ps.setString (64,dto.getDeptoExpedicionIdTransporta());
			else
				ps.setNull(64, Types.VARCHAR);
			
			ps.setString(65, dto.getTelefonoTransporta());
			ps.setString(66, dto.getDireccionTransporta());
			
			if (UtilidadCadena.noEsVacio(dto.getCiudadTransporta()))
				ps.setString (67,dto.getCiudadTransporta());
			else
				ps.setNull(67, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getDepartamentoTransporta()))
				ps.setString (68,dto.getDepartamentoTransporta());
			else
				ps.setNull(68, Types.VARCHAR);
			
			ps.setString(69, dto.getTransportaVictimaDesde());
			ps.setString(70, dto.getTransportaVictimaHasta());
			ps.setString(71, dto.getTipoTransporte());
			ps.setString(72, dto.getPlacaVehiculoTransporta());
			
			if (UtilidadCadena.noEsVacio(dto.getPaisEmpresa()))
				ps.setString (73,dto.getPaisEmpresa());
			else
				ps.setNull(73, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getPaisAccidente()))
				ps.setString (74,dto.getPaisAccidente());
			else
				ps.setNull(74, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getPaisExpedicionIdConductor()))
				ps.setString (75,dto.getPaisExpedicionIdConductor());
			else
				ps.setNull(75, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getPaisConductor()))
				ps.setString (76,dto.getPaisConductor());
			else
				ps.setNull(76, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getPaisExpedicionIdDeclarante()))
				ps.setString (77,dto.getPaisExpedicionIdDeclarante());
			else
				ps.setNull(77, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getPaisExpedicionIdProp()))
				ps.setString (78,dto.getPaisExpedicionIdProp());
			else
				ps.setNull(78, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getPaisExpedicionIdTransporta()))
				ps.setString (79,dto.getPaisExpedicionIdTransporta());
			else
				ps.setNull(79, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getPaisProp()))
				ps.setString (80,dto.getPaisProp());
			else
				ps.setNull(80, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getPaisTransporta()))
				ps.setString (81,dto.getPaisTransporta());
			else
				ps.setNull(81, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getDescripcionBreveOcurrencia()+""))
				ps.setString(82,dto.getDescripcionBreveOcurrencia());
			else
				ps.setNull(82, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getIntervencionAutoridad()+""))
				ps.setString(83,dto.getIntervencionAutoridad());
			else
				ps.setNull(83, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getCobroExcedentePoliza()+""))
				ps.setString(84,dto.getCobroExcedentePoliza());
			else
				ps.setNull(84, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getCantidadOtrosVehiAcc()+"") && dto.getCantidadOtrosVehiAcc()>0)
				ps.setInt(85,dto.getCantidadOtrosVehiAcc());
			else
				ps.setNull(85, Types.INTEGER);
			
			
			if (UtilidadCadena.noEsVacio(dto.getPlaca2Vehiculo()+""))
				ps.setString(86,dto.getPlaca2Vehiculo());
			else
				ps.setNull(86, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getTipoId2Vehiculo()+""))
				ps.setString(87,dto.getTipoId2Vehiculo());
			else
				ps.setNull(87, Types.VARCHAR);	
			
			if (UtilidadCadena.noEsVacio(dto.getNroId2Vehiculo()+""))
					ps.setString(88,dto.getNroId2Vehiculo());
				else
					ps.setNull(88, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getPlaca3Vehiculo()+""))
					ps.setString(89,dto.getPlaca3Vehiculo());
				else
					ps.setNull(89, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getTipoId3Vehiculo()+""))
					ps.setString(90,dto.getTipoId3Vehiculo());
				else
					ps.setNull(90, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getNroId3Vehiculo()+""))
					ps.setString(91,dto.getNroId3Vehiculo());
				else
					ps.setNull(91, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getTipoReferenciaRem()))
				ps.setString(92, dto.getTipoReferenciaRem());
			else
				ps.setNull(92, Types.VARCHAR);

			if (UtilidadCadena.noEsVacio(dto.getFechaRem()))
				ps.setDate(93, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dto.getFechaRem())));
			else
				ps.setNull(93, Types.DATE);
			
			if (UtilidadCadena.noEsVacio(dto.getHoraRem()))
				ps.setString(94, dto.getHoraRem());
			else
				ps.setNull(94, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getPrestadorRem()))
				ps.setString(95, dto.getPrestadorRem());
			else
				ps.setNull(95, Types.VARCHAR);
			
			
			if (UtilidadCadena.noEsVacio(dto.getProfesionalRem()))
				ps.setString(96, dto.getProfesionalRem());
			else
				ps.setNull(96, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getCargoProfesionalRem()))
				ps.setString(97, dto.getCargoProfesionalRem());
			else
				ps.setNull(97, Types.VARCHAR);
			

			if (UtilidadCadena.noEsVacio(dto.getFechaAceptacion()))
				ps.setDate(98, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dto.getFechaAceptacion())));
			else
				ps.setNull(98, Types.DATE);
			
			if (UtilidadCadena.noEsVacio(dto.getHoraAceptacion()))
				ps.setString(99, dto.getHoraAceptacion());
			else
				ps.setNull(99, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getPrestadorRecibe()))
				ps.setString(100, dto.getPrestadorRecibe());
			else
				ps.setNull(100, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getProfesionalRecibe()))
				ps.setString(101, dto.getProfesionalRecibe());
			else
				ps.setNull(101, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getCargoProfesionalRecibe()))
				ps.setString(102, dto.getCargoProfesionalRecibe());
			else
				ps.setNull(102, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getOtroTipoTrans()))
				ps.setString(103, dto.getOtroTipoTrans());
			else
				ps.setNull(103, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getZonaTransporte() ))
				ps.setString(104, dto.getZonaTransporte());
			else
				ps.setNull(104, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getInstitucion()+"") && dto.getInstitucion()>0)
				ps.setInt(105, dto.getInstitucion());
			else
				ps.setNull(105, Types.INTEGER);

			if (UtilidadCadena.noEsVacio(dto.getTotalFacAmpQx()+"") && dto.getTotalFacAmpQx()>0)
				ps.setDouble(106, dto.getTotalFacAmpQx());
			else
				ps.setNull(106, Types.DOUBLE);
			
			if (UtilidadCadena.noEsVacio(dto.getTotalReclamoAmpQx()+"") && dto.getTotalReclamoAmpQx()>0)
				ps.setDouble(107, dto.getTotalReclamoAmpQx());
			else
				ps.setNull(107, Types.DOUBLE);
			
			
			if (UtilidadCadena.noEsVacio(dto.getTotalFacAmpTx()+"") && dto.getTotalFacAmpTx()>0)
				ps.setDouble(108, dto.getTotalFacAmpTx());
			else
				ps.setNull(108, Types.DOUBLE);
			
			if (UtilidadCadena.noEsVacio(dto.getTotalReclamoAmpTx()+"") && dto.getTotalReclamoAmpTx()>0)
				ps.setDouble(109, dto.getTotalReclamoAmpTx());
			else
				ps.setNull(109, Types.DOUBLE);
			
			//////////////////////////////////////	
			
			if (UtilidadCadena.noEsVacio(dto.getEsReclamacion() ))
				ps.setString(110, dto.getEsReclamacion());
			else
				ps.setNull(110, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getFurips() ))
				ps.setString(111, dto.getFurips());
			else
				ps.setNull(111, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getFurtran() ))
				ps.setString(112, dto.getFurtran());
			else
				ps.setNull(112, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getRespGlosa() ))
				ps.setString(113, dto.getRespGlosa());
			else
				ps.setNull(113, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getNroRadicadoAnterior() ))
				ps.setString(114, dto.getNroRadicadoAnterior());
			else
				ps.setNull(114, Types.VARCHAR);
			
			if (UtilidadCadena.noEsVacio(dto.getNroConsReclamacion()+"") && dto.getNroConsReclamacion()>0)
				ps.setLong(115, dto.getNroConsReclamacion());
			else
				ps.setNull(115, Types.NUMERIC);
			
			
			
			
			//////////////////////////////////////////
			
			ps.setString(116, dto.getCodigo());
			ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("[modificarRegistroAccidentesTransito - SqlBaseRegistroAccidentesTransito] ERROR MODIFICANDO EL REGISTRO ACCIDENTES TRANSITO ");
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * metodo que consulta un registro de accidentes de transito dada la llave del registro.
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static DtoRegistroAccidentesTransito consultarRegistrosAccidentesTransito(Connection con,String ingreso)
	{
		DtoRegistroAccidentesTransito dto=new DtoRegistroAccidentesTransito();
		String cadena=cadenaConsultaGeneralAccidentesTransito;
		try
		{
		
				cadena+=" AND ingreso="+ingreso;
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				dto=cargarDTORegistroAccidentesTransito(ConstantesBD.codigoNuncaValido+"",new ResultSetDecorator(ps.executeQuery()));
			ps.close();
		}
		catch (SQLException e)
		{
			logger.error("[consultarRegistrosAccidentesTransito - SqlBaseRegistroAccidentesTransito] ERROR CONSULTANDO EL REGISTRO ACCIDENTES TRANSITO LLAVE ");
			e.printStackTrace();
		}
		
		return dto;
	}
	
	
	/**
	 * metodo que consulta un registro de accidentes de transito dada la llave del registro.
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static DtoRegistroAccidentesTransito consultarRegistrosAccidentesTransitoLlave(Connection con,String codigo)
	{
		DtoRegistroAccidentesTransito dto=new DtoRegistroAccidentesTransito();
		String cadena=cadenaConsultaGeneralAccidentesTransito;
		try
		{
		
				cadena+=" AND codigo="+codigo;
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				dto=cargarDTORegistroAccidentesTransito(ConstantesBD.codigoNuncaValido+"",new ResultSetDecorator(ps.executeQuery()));
			ps.close();
		}
		catch (SQLException e)
		{
			logger.error("[consultarRegistrosAccidentesTransito - SqlBaseRegistroAccidentesTransito] ERROR CONSULTANDO EL REGISTRO ACCIDENTES TRANSITO LLAVE ");
			e.printStackTrace();
		}
		
		return dto;
	}

	
	public static HashMap consultarAccidentesTransito (Connection connection, String estado)
	{
		HashMap result = new HashMap ();
		
			
		
		return result;
	}
	
	/***
	 * 
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static DtoRegistroAccidentesTransito consultarRegistroAccidentesTransitoIngreso(Connection con, String ingreso)
	{
		DtoRegistroAccidentesTransito dto=new DtoRegistroAccidentesTransito();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaGeneralAccidentesTransito+" AND ingreso=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, ingreso);
			dto=cargarDTORegistroAccidentesTransito(ingreso,new ResultSetDecorator(ps.executeQuery()));
			ps.close();
		}
		catch (SQLException e)
		{
			logger.error("[consultarRegistroAccidentesTransitoIngreso - SqlBaseRegistroAccidentesTransito] ERROR CONSULTANDO EL REGISTRO ACCIDENTES TRANSITO INGRESO ");
			e.printStackTrace();
		}
		
		return dto;
	}

	private static DtoRegistroAccidentesTransito cargarDTORegistroAccidentesTransito(String ingreso,ResultSetDecorator rs) 
	{
		DtoRegistroAccidentesTransito dto=new DtoRegistroAccidentesTransito();
		try
		{
			if(rs.next())
			{
				dto.setCodigo(rs.getString("codigo"));
				if(Utilidades.convertirAEntero(ingreso)>0)
					dto.setIngreso(ingreso);
				else
					dto.setIngreso(rs.getString("ingreso"));
				dto.setEmpresaTrabaja(rs.getString("empresatrabaja"));
				dto.setDireccionEmpresa(rs.getString("direccionempresa"));
				dto.setTelefonoEmpresa(rs.getString("telefonoempresa"));
				dto.setCiudadEmpresa(rs.getString("ciudadempresa"));
				dto.setDepartamentoEmpresa(rs.getString("departamentoempresa"));
				dto.setOcupante(rs.getString("ocupante"));
				dto.setCodicionAccidentado(rs.getString("condicionaccidentado"));
				dto.setResultaLesionadoAl(rs.getString("resultalesionadoal"));
				dto.setPlacaVehiculoOcaciona(rs.getString("placavehiculoocasiona"));
				dto.setLugarAccidente(rs.getString("lugaraccidente"));
				dto.setFechaAccidente(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechaaccidente")));
				dto.setHoraAccidente(rs.getString("horaaccidente"));
				dto.setCiudadAccidente(rs.getString("ciudadaccidente"));
				dto.setDepartamentoAccidente(rs.getString("departamentoaccidente"));
				dto.setZonaAccidente(rs.getString("zonaaccidente"));
				dto.setInformacionAccidente(rs.getString("informacionaccidente"));
				dto.setMarcaVehiculo(rs.getString("marcavehiculo"));
				dto.setPlaca(rs.getString("placavehiculo"));
				dto.setTipo(rs.getString("tipovehiculo"));
				dto.setAseguradora(rs.getString("aseguradora"));
				dto.setAgencia(rs.getString("agencia"));
				dto.setAsegurado(rs.getString("asegurado"));
				dto.setNumeroPoliza(rs.getString("numeropoliza"));
				dto.setFechaInicialPoliza(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechainicialpoliza")));
				dto.setFechaFinalPoliza(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechafinalpoliza")));
				dto.setPrimerApellidoConductor(rs.getString("primerapellidoconductor"));
				dto.setSegundoApellidoConductor(rs.getString("segundoapellidoconductor"));
				dto.setPrimerNombreConductor(rs.getString("primernombreconductor"));
				dto.setSegundoNombreConductor(rs.getString("segundonombreconductor"));
				dto.setTipoIdConductor(rs.getString("tipoidconductor"));
				dto.setNumeroIdConductor(rs.getString("numeroidconductor"));
				dto.setCiuExpedicionIdConductor(rs.getString("ciuexpidconductor"));
				dto.setDepExpedicionIdConductor(rs.getString("depexpidconductor"));
				dto.setDireccionConductor(rs.getString("direccionconductor"));
				dto.setCiudadConductor(rs.getString("ciudadconductor"));
				dto.setDepartamentoConductor(rs.getString("departamentoconductor"));
				dto.setTelefonoConductor(rs.getString("telefonoconductor"));
				dto.setApellidoNombreDeclarante(rs.getString("apellidonombredeclarante"));
				dto.setTipoIdDeclarante(rs.getString("tipoiddeclarante"));
				dto.setNumeroIdDeclarante(rs.getString("numeroiddeclarante"));
				dto.setCiuExpedicionIdDeclarante(rs.getString("ciuexpiddeclarante"));
				dto.setDepExpedicionIdDeclarante(rs.getString("depexpiddeclarante"));
				dto.setFechaGrabacion(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechagrabacion")));
				dto.setHoraGrabacion(rs.getString("horagrabacion"));
				dto.setUsuarioGrabacion(rs.getString("usuariograbacion"));
				dto.setEstadoRegistro(rs.getString("estado"));
				dto.setFechaAnulacion(rs.getString("fechaanulacion"));
				dto.setHoraAnulacion(rs.getString("horaanulacion"));
				dto.setUsuarioAnulacion(rs.getString("usuarioanulacion"));
				dto.setPoliza(rs.getString("poliza"));
				dto.setPrimerApellidoProp(rs.getString("primerapellidoprop"));
				dto.setSegundoApellidoProp(rs.getString("segundoapellidoprop"));
				dto.setPrimerNombreProp(rs.getString("primernombreprop"));
				dto.setSegundoNombreProp(rs.getString("segundonombreprop"));
				dto.setTipoIdProp(rs.getString("tipoidprop"));
				dto.setNumeroIdProp(rs.getString("numeroidprop"));
				dto.setCiudadExpedicionIdProp(rs.getString("ciudadexpidprop"));
				dto.setDeptoExpedicionIdProp(rs.getString("deptoexpidprop"));
				dto.setDireccionProp(rs.getString("direccionprop"));
				dto.setTelefonoProp(rs.getString("telefonoprop"));
				dto.setCiudadProp(rs.getString("ciudadprop"));
				dto.setDeptoProp(rs.getString("departamentoprop"));
				dto.setApellidoNombreTransporta(rs.getString("apellidonombretransporta"));
				dto.setTipoIdTransporta(rs.getString("tipoidtransporta"));
				dto.setNumeroIdTransporta(rs.getString("numeroidtransporta"));
				dto.setCiudadExpedicionIdTransporta(rs.getString("ciudadexpidprop"));
				dto.setDeptoExpedicionIdTransporta(rs.getString("deptoexpidprop"));
				dto.setTelefonoTransporta(rs.getString("telefonotransporta"));
				dto.setDireccionTransporta(rs.getString("direcciontransporta"));
				dto.setCiudadTransporta(rs.getString("ciudadtransporta"));
				dto.setDepartamentoTransporta(rs.getString("departamentotransporta"));
				dto.setTransportaVictimaDesde(rs.getString("transportavictimadesde"));
				dto.setTransportaVictimaHasta(rs.getString("transportavictimahasta"));
				dto.setTipoTransporte(rs.getString("tipotransporte"));
				dto.setPlacaVehiculoTransporta(rs.getString("placavehiculotranporta"));
				dto.setPaisEmpresa(rs.getString("paisempresa"));
				dto.setPaisAccidente(rs.getString("paisaccidente"));
				dto.setPaisExpedicionIdConductor(rs.getString("paisexpidconductor"));
				dto.setPaisConductor(rs.getString("paisconductor"));
				dto.setPaisExpedicionIdDeclarante(rs.getString("paisexpiddeclarante"));
				dto.setPaisExpedicionIdProp(rs.getString("paisexpidprop"));
				dto.setPaisExpedicionIdTransporta(rs.getString("paisexpidtransporta"));
				dto.setPaisProp(rs.getString("paisprop"));
				dto.setPaisTransporta(rs.getString("paistransporta"));
				dto.setDescripcionBreveOcurrencia(rs.getString("descripcionbreveocurrencia"));
				dto.setOtroTipoServicioVehiculo(rs.getString("otrotiposervv"));
				dto.setIntervencionAutoridad(rs.getString("intervencionautoridad"));
				dto.setCobroExcedentePoliza(rs.getString("cobroexcedentepoliza"));
				dto.setCantidadOtrosVehiAcc(rs.getInt("cantidadotrosvehiacc"));
				dto.setExistenOtrosVehiAcc(rs.getString("existenotrosvehiacc"));
				dto.setPlaca2Vehiculo(rs.getString("placa2vehiculo"));
				dto.setTipoId2Vehiculo(rs.getString("tipoid2vehiculo"));
				dto.setNroId2Vehiculo(rs.getString("nroid2vehiculo"));
				dto.setPlaca3Vehiculo(rs.getString("placa3vehiculo"));
				dto.setTipoId3Vehiculo(rs.getString("tipoid3vehiculo"));
				dto.setNroId3Vehiculo(rs.getString("nroid3vehiculo"));
				dto.setTipoReferenciaRem(rs.getString("tiporeferencia"));
				dto.setFechaRem(rs.getString("fecharemision"));
				dto.setHoraRem(rs.getString("horaremision"));
				dto.setPrestadorRem(rs.getString("codinscripremitente"));
				dto.setProfesionalRem(rs.getString("profesionalremite"));
				dto.setCargoProfesionalRem(rs.getString("cargoprofesionalremitente"));
				dto.setFechaAceptacion(rs.getString("fechaaceptacion"));
				dto.setHoraAceptacion(rs.getString("horaaceptacion"));
				dto.setPrestadorRecibe(rs.getString("codinscripreceptor"));
				dto.setProfesionalRecibe(rs.getString("profesionalRecibe"));
				dto.setCargoProfesionalRecibe(rs.getString("cargoprofesionalrecibe"));
				dto.setOtroTipoTrans(rs.getString("otrotipotrans"));
				dto.setZonaTransporte(rs.getString("zonatransporte"));
				dto.setInstitucion(rs.getInt("institucion"));
				dto.setTotalFacAmpQx(rs.getDouble("totalfacampqx"));
				dto.setTotalReclamoAmpQx(rs.getDouble("totalreclamoampqx"));
				dto.setTotalFacAmpTx(rs.getDouble("totalfacamptx"));
				dto.setTotalReclamoAmpTx(rs.getDouble("totalreclamoamptx"));
				dto.setEsReclamacion(rs.getString("esreclamacion"));
				dto.setFurips(rs.getString("furips"));
				dto.setFurtran(rs.getString("furtran"));
				dto.setRespGlosa(rs.getString("respglosa"));
				dto.setNroRadicadoAnterior(rs.getString("nroradicadoanterior"));
				dto.setNroConsReclamacion(rs.getLong("nroconsreclamacion"));
				
			}
		}
		catch(SQLException e)
		{
			logger.error("[cargarDTORegistroAccidentesTransito - SqlBaseRegistroAccidentesTransito] ERROR CARGANDO EL DTO ");
			e.printStackTrace();
		}
		return dto;
	}

	/**
	 * 
	 * @param con
	 * @param criteriosBusquedaMap
	 * @return
	 */
	public static HashMap busquedaAvanzada(Connection con, HashMap criteriosBusquedaMap) 
	{
		try
		{
			String consultaArmada= armarConsulta(	 criteriosBusquedaMap	 );
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consultaArmada,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
	        rs.close();
	        ps.close();
	        return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.warn("Error en la búsqueda avanzada" +e.toString());
			return null;
		}
	}

	/**
	 * 
	 * @param criteriosBusquedaMap
	 * @return
	 */
	private static String armarConsulta(HashMap criteriosBusquedaMap) 
	{
		String consultaArmada= cadenaListadoAdmisiones+" ";
		
		if(criteriosBusquedaMap.containsKey("fechaInicial") && criteriosBusquedaMap.containsKey("fechaFinal"))
		{
			if(!criteriosBusquedaMap.get("fechaInicial").toString().equals("") && !criteriosBusquedaMap.get("fechaInicial").toString().equals("null")
					&& !criteriosBusquedaMap.get("fechaFinal").toString().equals("") && !criteriosBusquedaMap.get("fechaFinal").toString().equals("null"))
			{
				consultaArmada+=" AND (r.fecha_grabacion >= '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaInicial").toString())+"'" +
								" AND r.fecha_grabacion <= '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaFinal").toString())+"') ";
			}
		}
		
		if(criteriosBusquedaMap.containsKey("consecutivoInicial") && criteriosBusquedaMap.containsKey("consecutivoFinal"))
		{
			if(!criteriosBusquedaMap.get("consecutivoInicial").toString().equals("") && !criteriosBusquedaMap.get("consecutivoInicial").toString().equals("null")
					&& !criteriosBusquedaMap.get("consecutivoFinal").toString().equals("") && !criteriosBusquedaMap.get("consecutivoFinal").toString().equals("null"))
			{
				consultaArmada+=" AND (r.codigo >= '"+criteriosBusquedaMap.get("consecutivoInicial").toString()+"'" +
								" AND r.codigo <= '"+criteriosBusquedaMap.get("consecutivoFinal").toString()+"') ";
			}
		}
		
		if(criteriosBusquedaMap.containsKey("viaIngreso"))
		{
			if(!criteriosBusquedaMap.get("viaIngreso").toString().equals("") && !criteriosBusquedaMap.get("viaIngreso").toString().equals("null"))
			{
				consultaArmada+=" AND vi.codigo = "+criteriosBusquedaMap.get("viaIngreso").toString();
			}
		}
		
		if(criteriosBusquedaMap.containsKey("idIngresoPaciente"))
		{
			if(!criteriosBusquedaMap.get("idIngresoPaciente").toString().equals("") && !criteriosBusquedaMap.get("idIngresoPaciente").toString().equals("null"))
			{
				consultaArmada+=" AND cue.id_ingreso = "+criteriosBusquedaMap.get("idIngresoPaciente").toString();
			}
		}
		
		
		consultaArmada+=" ORDER BY via, paciente ";
		
		
		return consultaArmada;
	}
	
	/**
	 * Método implementado para actualizar el estado del registro de accidentes de transito,
	 * si el estado es anulación se ingresa la fecha, hora y usuario anulacion
	 * @param con
	 * @param dtoReg
	 * @return
	 */
	public static int actualizarEstadoRegistroAccidenteTransito(Connection con,DtoRegistroAccidentesTransito dtoReg)
	{
		PreparedStatementDecorator pst=null;
		try
		{
			pst =  new PreparedStatementDecorator(con.prepareStatement("SELECT codigo_registro from ingresos_registro_accidentes where ingreso=?"));
			pst.setInt(1,Utilidades.convertirAEntero(dtoReg.getIngreso()));
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				String codigo=rs.getObject(1)+"";
				pst =  new PreparedStatementDecorator(con.prepareStatement(actualizarEstadoRegistroAccidentesTransitoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				/**
				 * UPDATE registro_accidentes_transito SET " +
						"estado = ?, " +
						"fecha_anulacion = ?, " +
						"hora_anulacion = ?, " +
						"usuario_anulacion = ? " +
						"WHERE codigo = ?"
				 */
				
				pst.setString(1,dtoReg.getEstadoRegistro());
				if(dtoReg.getEstadoRegistro().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
				{
					pst.setDate(2,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dtoReg.getFechaAnulacion())));
					pst.setString(3,dtoReg.getHoraAnulacion());
					pst.setString(4,dtoReg.getUsuarioAnulacion());
				}
				else
				{
					pst.setNull(2,Types.DATE);
					pst.setNull(3,Types.VARCHAR);
					pst.setNull(4,Types.VARCHAR);
				}
				pst.setObject(5,codigo);
				
				return pst.executeUpdate();
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarEstadoRegistroAccidenteTransito de SqlBaseRegistroAccidentesTransitoDao: "+e);
		}
		return 0;
	}
	
	
	/**
	 * mapa de generarReporteCertificadoAtencionMedica
	 * @param con
	 * @param criteriosBusquedaMap, keys--> codigoCentroAtencion , codigoInstitucion, idIngreso
	 * @return
	 */
	public static HashMap generarReporteCertificadoAtencionMedica(Connection con, HashMap criteriosBusquedaMap) 
	{
		HashMap reporteCertificadoAtencionMedica=new HashMap();
		String consultaInfoInstitucion= "SELECT 'El suscrito médico del Servicio de Urgencias de la Institución Prestadora de Servicios '|| i.razon_social ||' '|| getnomcentroatencion("+criteriosBusquedaMap.get("codigoCentroAtencion")+") as descripcion1, 'Con Domicilio en '|| i.direccion as descripcion2, 'Departamento '|| d.descripcion ||' '|| c.descripcion as descripcion3 FROM instituciones i INNER JOIN departamentos d ON (d.codigo_departamento=i.departamento and i.pais=d.codigo_pais) INNER JOIN ciudades c on (c.codigo_departamento=i.departamento AND c.codigo_ciudad=i.ciudad and i.pais=c.codigo_pais) WHERE i.codigo="+criteriosBusquedaMap.get("codigoInstitucion");
		reporteCertificadoAtencionMedica.put("consultaInfoInstitucion", generarMapa(con, consultaInfoInstitucion));
		String consultaInfoPaciente= "SELECT 'Certifica que atendió en el servicio de urgencias al señor(a): '|| p.primer_nombre ||' '|| p.segundo_nombre ||' '|| primer_apellido||' '|| segundo_apellido as descipcionPaciente1, 'Identificado con '|| p.tipo_identificacion ||' No '|| p.numero_identificacion ||' de '||getnombreciudad(p.codigo_pais_id,p.codigo_depto_id, p.codigo_ciudad_id) as descipcionPaciente2, 'Residente en '||p.direccion ||' Ciudad '|| getnombreciudad(p.codigo_pais_vivienda,p.codigo_departamento_vivienda, p.codigo_ciudad_vivienda) as descripcionPaciente3, 'Departamento '||getnombredepto(p.codigo_pais_vivienda,p.codigo_departamento_vivienda) as descripcionPaciente4 FROM cuentas c, personas p WHERE c.codigo_paciente= p.codigo and c.id_ingreso="+criteriosBusquedaMap.get("idIngreso");
		reporteCertificadoAtencionMedica.put("consultaInfoPaciente", generarMapa(con, consultaInfoPaciente));
		String consultaInfoDeclarante="SELECT 'Quien según declaración de '||CASE WHEN r.apellido_nombre_declarante IS NULL THEN '' ELSE r.apellido_nombre_declarante END AS declarante, r.tipo_id_declarante ||' No. '||r.numero_id_declarante ||' Expedida en '|| getnombreciudad(pais_expedicion_id_declarante,dep_expedicion_id_declarante, ciu_expedicion_id_declarante) AS infoDeclarante, 'Fué victima del accidente de tránsito ocurrido el '||to_char(r.fecha_accidente, 'DD/MM/YYYY')||' a las '|| substr(r.hora_accidente, 1,5) ||' horas, ingresando al servicio de urgencias de esta institución el día '|| to_char (adu.fecha_admision, 'DD/MM/YYYY')|| ' a las '|| substr(adu.hora_admision, 1,5) ||' horas con los siguientes hallazgos: ' as descripcionHechos  FROM ingresos i INNER JOIN registro_accidentes_transito r ON(r.ingreso=i.id) INNER JOIN cuentas c ON(c.id_ingreso=i.id) INNER JOIN admisiones_urgencias adu ON (c.id=adu.cuenta) WHERE i.id="+criteriosBusquedaMap.get("idIngreso");
		reporteCertificadoAtencionMedica.put("consultaInfoDeclarante", generarMapa(con, consultaInfoDeclarante));
		String consultaInfoSignosVitales="SELECT 'TA '|| getValSignosVitales(numero_solicitud,12)||', '||getValSignosVitales(numero_solicitud,13)||' mmHg  FC '|| getValSignosVitales(numero_solicitud,15)||' x min.  FR '||getValSignosVitales(numero_solicitud,16)||' x min  T° '||getValSignosVitales(numero_solicitud,17)||' C° '   AS signosVitales FROM valoraciones where numero_solicitud=(SELECT numero_solicitud as numeroSolicitud from valoraciones where numero_solicitud=(SELECT min(sol.numero_solicitud) from solicitudes sol INNER JOIN valoraciones val ON (sol.numero_solicitud=val.numero_solicitud) INNER JOIN cuentas c ON (c.id=sol.cuenta) WHERE c.id_ingreso="+criteriosBusquedaMap.get("idIngreso")+"))";
		reporteCertificadoAtencionMedica.put("consultaInfoSignosVitales", generarMapa(con, consultaInfoSignosVitales));
		String consultaDatosPositivos="SELECT (SELECT ip.valor from info_parametrizada ip where ip.codigo_tabla= v.numero_solicitud and ip.parametrizacion_asociada=5) AS cabezaOrganosSentidos, (SELECT ip.valor from info_parametrizada ip where ip.codigo_tabla= v.numero_solicitud and ip.parametrizacion_asociada=59)  AS cuello, (SELECT ip.valor from info_parametrizada ip where ip.codigo_tabla= v.numero_solicitud and ip.parametrizacion_asociada=6) AS toraxYCardiovascular, (SELECT ip.valor from info_parametrizada ip where ip.codigo_tabla= v.numero_solicitud and ip.parametrizacion_asociada=7) AS abdomen, (SELECT ip.valor from info_parametrizada ip where ip.codigo_tabla= v.numero_solicitud and ip.parametrizacion_asociada=8) AS genitourinario, (SELECT ip.valor from info_parametrizada ip where ip.codigo_tabla= v.numero_solicitud and ip.parametrizacion_asociada=60) as pelvis, (SELECT ip.valor from info_parametrizada ip where ip.codigo_tabla= v.numero_solicitud and ip.parametrizacion_asociada=10) AS dorsoYExtremidades,  (SELECT ip.valor from info_parametrizada ip where ip.codigo_tabla= v.numero_solicitud and ip.parametrizacion_asociada=11)  AS neurologico   FROM valoraciones v where v.numero_solicitud=(SELECT v1.numero_solicitud as numeroSolicitud from valoraciones v1 where v1.numero_solicitud=(SELECT min(sol.numero_solicitud) from solicitudes sol INNER JOIN valoraciones val ON (sol.numero_solicitud=val.numero_solicitud) INNER JOIN cuentas c ON (c.id=sol.cuenta) WHERE c.id_ingreso="+criteriosBusquedaMap.get("idIngreso")+"))";
		reporteCertificadoAtencionMedica.put("consultaDatosPositivos", generarMapa(con, consultaDatosPositivos));
		String consultaInfoDiagnosticoEgreso= "SELECT getDiagnostico(e.diagnostico_principal, e.diagnostico_principal_cie) AS diagnosticoPrincipal, CASE WHEN getDiagnostico(e.diagnostico_relacionado1, e.diagnostico_relacionado1_cie)='No seleccionado' THEN '' ELSE getDiagnostico(e.diagnostico_relacionado1, e.diagnostico_relacionado1_cie) END AS diagRelacionado1, CASE WHEN getDiagnostico(e.diagnostico_relacionado2, e.diagnostico_relacionado2_cie)='No seleccionado' THEN '' ELSE getDiagnostico(e.diagnostico_relacionado2, e.diagnostico_relacionado2_cie) END AS diagRelacionado2,  CASE WHEN getDiagnostico(e.diagnostico_relacionado3, e.diagnostico_relacionado3_cie)='No seleccionado' THEN '' ELSE getDiagnostico(e.diagnostico_relacionado3, e.diagnostico_relacionado3_cie) END AS diagRelacionado3 FROM egresos e INNER JOIN cuentas c ON (c.id=e.cuenta) where c.id_ingreso="+criteriosBusquedaMap.get("idIngreso")+" and getDiagnostico(e.diagnostico_principal, e.diagnostico_principal_cie)<>'No seleccionado'";
		reporteCertificadoAtencionMedica.put("consultaInfoDiagnosticoEgreso", generarMapa(con, consultaInfoDiagnosticoEgreso));
		String consultaInfoMedico= "SELECT getnombrepersona(s.codigo_medico_responde), m.numero_registro FROM solicitudes s INNER JOIN medicos m ON m.codigo_medico= s.codigo_medico_responde WHERE s.numero_solicitud=(SELECT numero_solicitud as numeroSolicitud from valoraciones where numero_solicitud=(SELECT min(sol.numero_solicitud) from solicitudes sol INNER JOIN valoraciones val ON (sol.numero_solicitud=val.numero_solicitud) INNER JOIN cuentas c ON (c.id=sol.cuenta) WHERE c.id_ingreso="+criteriosBusquedaMap.get("idIngreso")+"))";
		reporteCertificadoAtencionMedica.put("consultaInfoMedico", generarMapa(con, consultaInfoMedico));
		String consultaEstadoConciencia= "SELECT ec.nombre AS estadoConciencia, CASE WHEN estado_embriaguez ="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'Si' else 'No' end AS estadoEmbriaguez FROM valoraciones_urgencias vu INNER JOIN estados_conciencia ec ON (ec.codigo= vu.codigo_estado_conciencia)WHERE vu.numero_solicitud=(SELECT numero_solicitud as numeroSolicitud from valoraciones where numero_solicitud=(SELECT min(sol.numero_solicitud) from solicitudes sol INNER JOIN valoraciones val ON (sol.numero_solicitud=val.numero_solicitud) INNER JOIN cuentas c ON (c.id=sol.cuenta) WHERE c.id_ingreso="+criteriosBusquedaMap.get("idIngreso")+"))";
		reporteCertificadoAtencionMedica.put("consultaEstadoConciencia", generarMapa(con, consultaEstadoConciencia));
		String consultaGlasgow="SELECT valor FROM val_signos_vitales WHERE signo_vital=18 AND valoracion=(select numero_solicitud  FROM valoraciones where numero_solicitud=(SELECT numero_solicitud as numeroSolicitud from valoraciones where numero_solicitud=(SELECT min(sol.numero_solicitud) from solicitudes sol INNER JOIN valoraciones val ON (sol.numero_solicitud=val.numero_solicitud) INNER JOIN cuentas c ON (c.id=sol.cuenta) WHERE c.id_ingreso="+criteriosBusquedaMap.get("idIngreso")+")))";
		reporteCertificadoAtencionMedica.put("glasgow", generarMapa(con,consultaGlasgow));
		
		return reporteCertificadoAtencionMedica;
	}
	
	/**
	 * 
	 * @param con
	 * @param criteriosBusquedaMap, keys--> codigoCentroAtencion , codigoInstitucion, idIngreso
	 * @return
	 */
	public static HashMap generarReporteFUSOAT01(Connection con, HashMap criteriosBusquedaMap)
	{
		HashMap reporteFUSOAT01=new HashMap();
		
		String  datosCentroAsistencial= "SELECT getnomcentroatencion("+criteriosBusquedaMap.get("codigoCentroAtencion")+") AS nombreCentro, i.nit, i.direccion, getnombreciudad(i.pais,i.departamento, i.ciudad) as ciudad, i.telefono FROM instituciones i WHERE i.codigo="+criteriosBusquedaMap.get("codigoInstitucion");
		reporteFUSOAT01.put("datosCentroAsistencial", generarMapa(con, datosCentroAsistencial));
		String  datosAccidentado= "SELECT getnombrepersona(p.codigo) as apellidosNombres, getedad(p.fecha_nacimiento) ||' '|| CASE WHEN (CURRENT_DATE - 365) < p.fecha_nacimiento THEN 'meses' ELSE 'anios' END AS edad, s.nombre AS nombresexo, p.tipo_identificacion ||' No. '|| p.numero_identificacion AS identificacion, getnombreciudad(p.codigo_pais_id,p.codigo_depto_id, p.codigo_ciudad_id) as de, p.direccion AS dir, getnombreciudad(p.codigo_pais_vivienda,p.codigo_departamento_vivienda, p.codigo_ciudad_vivienda) as ciudad, p.telefono, (SELECT CASE WHEN getintegridaddominio(r1.ocupante) = 'Si' THEN 'Ocupante ' else 'Peaton ' end || getintegridaddominio(r1.condicion_accidentado) || ' quien resulta lesionado al '|| getintegridaddominio(r1.resulta_lesionado_al) || CASE WHEN r1.resulta_lesionado_al='AECV' THEN ' '|| r1.placa_vehiculo_ocasiona else '' end from registro_accidentes_transito r1 WHERE r1.ingreso=i.id) as condicionaccidentado FROM personas p INNER JOIN ingresos i ON (i.codigo_paciente=p.codigo) INNER JOIN cuentas c ON (c.id_ingreso=i.id) INNER JOIN sexo s ON(s.codigo=p.sexo) WHERE i.id="+criteriosBusquedaMap.get("idIngreso");
		reporteFUSOAT01.put("datosAccidentado", generarMapa(con, datosAccidentado));
		String  identificacionAccidente= "SELECT r.lugar_accidente as sitio, to_char(r.fecha_accidente,'DD/MM/YYYY') as fecha, substr(r.hora_accidente, 1,5) as hora, c.descripcion as municipio, d.descripcion AS departamento, getintegridaddominio(r.zona_accidente) as zona, r.informacion_accidente as informeAccidente FROM registro_accidentes_transito r INNER JOIN cuentas c1 ON (c1.id_ingreso=r.ingreso) INNER JOIN ciudades c ON (c.codigo_departamento=r.departamento_accidente AND r.ciudad_accidente=c.codigo_ciudad AND r.pais_accidente=c.codigo_pais) INNER JOIN departamentos d ON (r.departamento_accidente=d.codigo_departamento and r.pais_accidente=d.codigo_pais) WHERE c1.id_ingreso="+criteriosBusquedaMap.get("idIngreso");
		reporteFUSOAT01.put("identificacionAccidente", generarMapa(con, identificacionAccidente));
		String  identificacionVehiculo= "SELECT r.marca_vehiculo AS marca, r.placa, r.tipo, con.nombre AS nombreAseguradora, r.agencia, getintegridaddominio(r.asegurado) as asegurado, r.numero_poliza as numeroPoliza, 'DESDE: '||to_char(r.fecha_inicial_poliza, 'DD/MM/YYYY')||' HASTA: '||to_char(r.fecha_final_poliza, 'DD/MM/YYYY') AS vigenciaPoliza, primer_apellido_conductor||' '||segundo_apellido_conductor||' '||primer_nombre_conductor||' '||segundo_nombre_conductor as apellido_nombre_conductor, r.tipo_id_conductor, r.numero_id_conductor, getnombreciudad(r.pais_expedicion_id_conductor,r.dep_expedicion_id_conductor,r.ciu_expedicion_id_conductor) AS de, r.direccion_conductor, getnombreciudad(r.pais_conductor,r.departamento_conductor, r.ciudad_conductor) AS ciudad_conductor, r.telefono_conductor FROM registro_accidentes_transito r INNER JOIN cuentas c ON (c.id_ingreso=r.ingreso) INNER JOIN convenios con ON (con.codigo=r.aseguradora) WHERE c.id_ingreso="+criteriosBusquedaMap.get("idIngreso");
		reporteFUSOAT01.put("identificacionVehiculo", generarMapa(con, identificacionVehiculo));
		
		//String  remision= "SELECT r.persona_refiere as personaRemitida_de, getnombreciudad(r.departamento_refiere, r.ciudad_refiere) as ciudadRemitida_de, to_char(r.fecha_referencia, 'DD/MM/YYYY') as fechaRemitida_de, r.referido_a as personaRemitida_a, getnombreciudad(r.departamento_referido,r.ciudad_referido) as ciudadRemitida_a, to_char(r.fecha_confirmacion_referido, 'DD/MM/YYYY') as fechaRemitida_a FROM registro_accidentes_transito r INNER JOIN cuentas c ON (c.id_ingreso=r.ingreso) WHERE c.id_ingreso="+criteriosBusquedaMap.get("idIngreso");
		//reporteFUSOAT01.put("remision", Utilidades.obtenerInformacionReferenciaTramite(con, Integer.parseInt(criteriosBusquedaMap.get("codigoPersona")+""), Integer.parseInt(criteriosBusquedaMap.get("idIngreso")+"")));
		
		String  datosAtencionPaciente= "SELECT TO_CHAR(c.fecha_apertura, 'DD/MM/YYYY') AS fechaApertura, SUBSTR(c.hora_apertura, 1,5) AS horaApertura, p.tipo_identificacion ||' '||p.numero_identificacion AS numHistoriaClinica, CASE WHEN c.via_ingreso in ("+ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoUrgencias+") THEN TO_CHAR(eg.fecha_egreso, 'DD/MM/YYYY') ||' '|| SUBSTR(eg.hora_egreso,1,5) ELSE TO_CHAR(c.fecha_apertura, 'DD/MM/YYYY') ||' '|| SUBSTR(c.hora_apertura,1,5)   END AS fechahoraegreso, CASE WHEN c.via_ingreso IN ("+ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoUrgencias+") THEN (c.fecha_apertura-eg.fecha_egreso-1)||'' else '' end AS diasEstancia    FROM  ingresos ingr INNER JOIN cuentas c ON (c.id_ingreso=ingr.id) INNER JOIN personas p ON (p.codigo=c.codigo_paciente) LEFT OUTER JOIN egresos eg ON (c.id=eg.cuenta) WHERE c.id_ingreso="+criteriosBusquedaMap.get("idIngreso");
		reporteFUSOAT01.put("datosAtencionPaciente", generarMapa(con, datosAtencionPaciente));
		
		String  tratamiento= "SELECT (CASE WHEN c.via_ingreso="+ConstantesBD.codigoViaIngresoUrgencias+" and( SELECT count(1) from solicitudes sol1 INNER JOIN valoraciones_urgencias vu1 ON (sol1.numero_solicitud=vu1.numero_solicitud) INNER JOIN cuentas c1 ON (c1.id=sol1.cuenta) WHERE c1.id_ingreso=ingr.id and vu1.codigo_conducta_valoracion=3)>0 THEN  'Observación' ELSE (CASE WHEN c.via_ingreso in ("+ConstantesBD.codigoViaIngresoAmbulatorios+","+ConstantesBD.codigoViaIngresoConsultaExterna+") or ( c.via_ingreso="+ConstantesBD.codigoViaIngresoUrgencias+" and( SELECT count(1) from solicitudes sol1 INNER JOIN valoraciones_urgencias vu1 ON (sol1.numero_solicitud=vu1.numero_solicitud) INNER JOIN cuentas c1 ON (c1.id=sol1.cuenta) WHERE c1.id_ingreso=ingr.id and vu1.codigo_conducta_valoracion=3)=0) THEN  'Ambulat.' ELSE 'Hospitalario' END) END) as descTratamiento FROM  ingresos ingr INNER JOIN cuentas c ON (c.id_ingreso=ingr.id) WHERE c.id_ingreso="+criteriosBusquedaMap.get("idIngreso");
		reporteFUSOAT01.put("tratamiento", generarMapa(con, tratamiento));
		
		String  diagnosticoIngreso="select tabla.diagnosticoIngreso from (select case when c.via_ingreso in ("+ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoUrgencias+","+ConstantesBD.codigoViaIngresoConsultaExterna+") then (SELECT getdiagnostico(vd.acronimo_diagnostico, vd.tipo_cie_diagnostico) from val_diagnosticos vd inner join solicitudes s on(vd.valoracion=s.numero_solicitud) inner join cuentas c on (c.id=s.cuenta) where c.id_ingreso=i.id  "+ValoresPorDefecto.getValorLimit1()+" 1) else (SELECT case when dp.diagnostico is null then '' else getdiagnostico(dp.diagnostico,dp.tipo_cie) end from solicitudes s inner join cuentas c on(c.id=s.cuenta) left outer join sol_cirugia_por_servicio sc on(s.numero_solicitud =sc.numero_solicitud) inner join INFORME_QX_POR_ESPECIALIDAD informe on sc.COD_INFORME_ESPECIALIDAD = informe.codigo inner join diag_post_opera_sol_cx dp on(dp.COD_INFORME_ESPECIALIDAD=informe.codigo) where c.id_ingreso=i.id  "+ValoresPorDefecto.getValorLimit1()+" 1) end as diagnosticoIngreso from ingresos i inner join cuentas c on (i.id=c.id_ingreso) where c.id_ingreso="+criteriosBusquedaMap.get("idIngreso")+")tabla WHERE tabla.diagnosticoIngreso<>'No seleccionado'"; 
		reporteFUSOAT01.put("diagnosticoIngreso", generarMapa(con, diagnosticoIngreso));
		
		String  diagnosticoDefinitivo= "select tabla.diagnosticoDefinitivo from (select CASE WHEN c.via_ingreso in ("+ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoUrgencias+") then (select getdiagnostico(e.diagnostico_principal, e.diagnostico_principal_cie) FROM egresos e WHERE e.cuenta=c.id) else ( select case when c.via_ingreso in ("+ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoUrgencias+","+ConstantesBD.codigoViaIngresoConsultaExterna+") then (SELECT getdiagnostico(vd.acronimo_diagnostico, vd.tipo_cie_diagnostico) from val_diagnosticos vd inner join solicitudes s on(vd.valoracion=s.numero_solicitud) inner join cuentas c on (c.id=s.cuenta) where c.id_ingreso=i.id  "+ValoresPorDefecto.getValorLimit1()+" 1) else (SELECT case when dp.diagnostico is null then '' else getdiagnostico(dp.diagnostico,dp.tipo_cie) end from solicitudes s inner join cuentas c on(c.id=s.cuenta) left outer join sol_cirugia_por_servicio sc on(s.numero_solicitud =sc.numero_solicitud) inner join INFORME_QX_POR_ESPECIALIDAD informe on sc.COD_INFORME_ESPECIALIDAD = informe.codigo inner join diag_post_opera_sol_cx dp on (dp.COD_INFORME_ESPECIALIDAD=informe.codigo) where c.id_ingreso=i.id  "+ValoresPorDefecto.getValorLimit1()+" 1) end) END AS diagnosticoDefinitivo from ingresos i inner join cuentas c on (i.id=c.id_ingreso) where c.id_ingreso="+criteriosBusquedaMap.get("idIngreso")+") tabla where tabla.diagnosticoDefinitivo<>'No seleccionado'";
		reporteFUSOAT01.put("diagnosticoDefinitivo", generarMapa(con, diagnosticoDefinitivo));
		
		String  diagnosticosRelacionados= " SELECT CASE WHEN getdiagnostico(diagnostico_relacionado1, diagnostico_relacionado1_cie)='No seleccionado' THEN '' ELSE getdiagnostico(diagnostico_relacionado1, diagnostico_relacionado1_cie) ||', ' END  || CASE WHEN getdiagnostico(diagnostico_relacionado2, diagnostico_relacionado2_cie)='No seleccionado' THEN '' ELSE  getdiagnostico(diagnostico_relacionado2, diagnostico_relacionado2_cie) ||', ' END || CASE WHEN getdiagnostico(diagnostico_relacionado3, diagnostico_relacionado3_cie)='No seleccionado' THEN '' ELSE getdiagnostico(diagnostico_relacionado3, diagnostico_relacionado3_cie) END as diagRel  from egresos e INNER JOIN cuentas c ON (c.id=e.cuenta) WHERE c.id_ingreso="+criteriosBusquedaMap.get("idIngreso");
		reporteFUSOAT01.put("diagnosticosRelacionados", generarMapa(con, diagnosticosRelacionados));
		
		String datosMuerte= "SELECT getdiagnostico(e.diagnostico_muerte, e.diagnostico_muerte_cie) as getdiagnostico, to_char(ev.fecha_evolucion, 'DD/MM/YYYY') as fechaMuerte, substr(ev.hora_evolucion,1,5) as horaMuerte, getnombrepersona(ev.codigo_medico) as getnombrepersona, m.numero_registro, getnombreciudad(m.codigo_pais_registro,m.codigo_depto_registro, m.codigo_ciudad_registro) as de from egresos e inner join cuentas c  ON (c.id=e.cuenta) inner join evoluciones ev on(e.evolucion=ev.codigo and ev.orden_salida=true) inner join medicos m on (m.codigo_medico=ev.codigo_medico) inner join pacientes pac on (pac.codigo_paciente=c.codigo_paciente) where c.id_ingreso="+criteriosBusquedaMap.get("idIngreso")+" and pac.esta_vivo<>"+ValoresPorDefecto.getValorTrueParaConsultas();
		reporteFUSOAT01.put("datosMuerte", generarMapa(con, datosMuerte) );
		
		return reporteFUSOAT01;
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param criteriosBusquedaMap
	 * @return
	 */
	private static HashMap generarMapa(Connection con, String consultaArmada) 
	{
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consultaArmada,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			logger.info("\n\n FUSOAT --> "+consultaArmada+"\n\n");
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
	        rs.close();
	        ps.close();
	        return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.warn("Error en la búsqueda avanzada" +e.toString());
			return null;
		}
	}
	
}
