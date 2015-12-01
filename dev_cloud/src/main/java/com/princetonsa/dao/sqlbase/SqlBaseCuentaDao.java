/*
 * @(#)SqlBaseCuentaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */

package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.Answer;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.LogsAxioma;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.facturacion.DtoDatosResponsableFacturacion;
import com.princetonsa.dto.facturacion.DtoDetalleCirugiasFacturaAgrupada;
import com.princetonsa.dto.facturacion.DtoFacturaAgrupada;
import com.princetonsa.dto.facturacion.DtoInstitucion;
import com.princetonsa.dto.manejoPaciente.DtoCuentas;
import com.princetonsa.dto.manejoPaciente.DtoRequsitosPaciente;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;

/**
 * Esta clase implementa la funcionalidad comï¿½n a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estï¿½ndar. Mï¿½todos particulares a Cuenta
 *
 *	@version 1.0, Mar 29, 2004
 */
public class SqlBaseCuentaDao 
{
	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar una cuenta usando una BD Genï¿½rica.
	 */
	private static final String cargarCuenta=	"select "+ 
		"p.codigo AS codigoPaciente, "+ 
		"p.numero_identificacion as numeroIdentificacion, "+ 
		"p.tipo_identificacion as codigoTipoIdentificacion, "+ 
		"ti.nombre as tipoIdentificacion, "+ 
		"p.primer_nombre as primerNombre, "+ 
		"p.segundo_nombre as segundoNombre, "+ 
		"p.primer_apellido as primerApellido, "+ 
		"p.segundo_apellido as segundoApellido, "+ 
		"c.fecha_apertura as fechaApertura, "+ 
		"c.hora_apertura AS horaApertura, "+ 
		"sc.monto_cobro as codigoMontoCobro, "+
		"c.estado_cuenta as codigoEstadoCuenta, "+ 
		"getnombreestadocuenta(c.estado_cuenta) as estadoCuenta, "+ 
		"sc.convenio as codigoConvenio,  "+
		"conv.nombre as convenio, "+ 
		"ter.descripcion as empresa, "+ 
		"c.tipo_paciente as codigoTipoPaciente, "+ 
		"getnombretipopaciente(c.tipo_paciente) as tipoPaciente, "+ 
		"c.via_ingreso as codigoViaIngreso, "+ 
		"getnombreviaingreso(c.via_ingreso) as viaIngreso, "+ 
		"conv.tipo_regimen as codigoTipoRegimen, "+ 
		"getnomtiporegimen(conv.tipo_regimen) as tipoRegimen, "+ 
		"sc.tipo_afiliado as codigoTipoAfiliado, "+  
		"getnombretipoafiliado(sc.tipo_afiliado) as tipoAfiliado, "+ 
		"sc.clasificacion_socioeconomica as codigoEstrato, "+ 
		"getnombreestrato(sc.clasificacion_socioeconomica) as estrato, "+ 
		"sc.naturaleza_paciente as codigoNaturalezaPaciente, "+ 
		"getnomnatpacientes(sc.naturaleza_paciente) as naturalezaPaciente, "+ 
		"c.indicativo_acc_transito as IndicativoAccidente, "+  
		"sc.nro_poliza as numeroPoliza, "+ 
		"sc.nro_carnet as numeroCarnet, "+ 
		"c.usuario_modifica AS usuario, "+ 
		"c.id_ingreso as codigoIngreso, "+ 
		"c.area as codigoArea, "+ 
		"getnomcentrocosto(c.area) AS nombreArea, "+ 
		"sc.contrato AS codigoContrato, "+
		"cc.centro_atencion AS codigoCentroAtencion, "+
		"getnomcentroatencion(cc.centro_atencion) AS nombreCentroAtencion, " +
		"c.tipo_evento AS codigoTipoEvento, "+ 
		"CASE WHEN c.convenio_arp_afiliado IS NULL THEN '' ELSE c.convenio_arp_afiliado || '' END AS codigoConvenioArp, "+ 
		"CASE WHEN c.convenio_arp_afiliado IS NULL THEN '' ELSE getnombreconvenio(c.convenio_arp_afiliado) END AS nombreConvenioArp, "+ 
		"c.desplazado AS desplazado, "+  
		"c.origen_admision AS codigoOrigenAdmision, "+ 
		"getNomOrigenAdmision(c.origen_admision) AS nombreOrigenAdmision, "+
		"CASE WHEN c.id_ingreso IS NULL THEN '' ELSE coalesce(getDescripEntidadSubXingreso(c.id_ingreso),'') END AS descripcionentidadsub "+ 
		"FROM cuentas c "+ 
		"INNER JOIN sub_cuentas sc ON(sc.ingreso = c.id_ingreso and sc.nro_prioridad = 1) "+ 
		"INNER JOIN personas p  ON(p.codigo=c.codigo_paciente) "+ 
		"INNER JOIN tipos_identificacion ti ON(ti.acronimo=p.tipo_identificacion) "+ 
		"INNER JOIN convenios conv ON(conv.codigo=sc.convenio) "+ 
		"INNER JOIN empresas emp ON(emp.codigo=conv.empresa) " + 
		"INNER JOIN terceros ter ON(ter.codigo=emp.tercero) "+ 
		"INNER JOIN centros_costo cc ON(cc.codigo=c.area) "+ 
		"where "+ 
		"c.id=?";
	
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar informacion basica de la cuenta usando una BD Genï¿½rica.
	 */
	private static final String cargarInfoBasicaCuenta=	"select " +
			"sc.convenio as codigoConvenio, " +
			"conv.nombre as convenio, " +
			"sc.tipo_afiliado as codigoTipoAfiliado, " +
			"ta.nombre as tipoAfiliado, " +
			"sc.clasificacion_socioeconomica as codigoEstrato, " +
			"es.descripcion as estrato " +
			"FROM cuentas c " +
			"INNER JOIN sub_cuentas sc ON(sc.ingreso = c.id_ingreso and sc.nro_prioridad = 1) " +
			"INNER JOIN convenios conv ON(conv.codigo=sc.convenio) " +
			"INNER JOIN estratos_sociales es on es.codigo=sc.clasificacion_socioeconomica " +
			"INNER JOIN tipos_afiliado ta ON ta.acronimo=sc.tipo_afiliado " +
			"where " +
			"c.id=?" +
			"and c.id_ingreso = ?" +
			"and sc.convenio= ?";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para retornar 
	 * la fecha de creaciï¿½n de esta cuenta.
	 */
	private static final String buscarFechaCreacionCuentaStr="SELECT to_char(fecha_apertura,'"+ConstantesBD.formatoFechaBD+"') as fechaApertura from cuentas where id=?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para dejar una cuenta en estado asociado usando una BD Genï¿½rica.
	 */
	private static final String cambiarEstadoCuentaStr="UPDATE cuentas SET estado_cuenta =? where id=?";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para desactivar un asocio de cuentas usando una BD Genï¿½rica.
	 */
	private static final String desactivarEntradaAsocioCuentaStr="UPDATE asocios_cuenta " +
			"set activo=" + ValoresPorDefecto.getValorFalseParaConsultas() + ", " +
			"usuario_desasocio=?," +
			"fecha_desasocio = CURRENT_DATE," +
			"hora_desasocio = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
			"where activo=" + ValoresPorDefecto.getValorTrueParaConsultas() + " and cuenta_inicial=?";

	/**
	 * Cadena constante con el <i>statemente</i> necesario para consultar los codigos de cuenta asociados a un ingreso
	 */
	private static final String consultarCodigosCuentasIngresoStr = "SELECT id as idCuenta FROM cuentas WHERE id_ingreso=?";

	/**
	 * Cadena constante con el <i>statemente</i> necesario para consultar el nombre del centro de costo tratante
	 */
	private static final String consultarCentroCostoTratanteStr ="SELECT nombre as centroCosto from centros_costo where codigo =?" ;
	

	/**
	 * 
	 */
	private static final String consultarValorTotalCuenta="SELECT getValorTotalCuenta(?) as total";
	
	/**
	 * cadena para registrar el cierre de la cuenta
	 */
	private static final String registrarCierreCuentaStr="INSERT INTO cierre_cuentas (cuenta,fecha,hora,usuario,motivo) VALUES(?,?,?,?,?)";
	
	/**
	 * cadena para cosnultar las cuentas cerradas del paciente
	 */
	private static final String consultarCuentasCerradasPacienteStr="SELECT "+ 
																	"c.id AS id,"+
																	"c.via_ingreso AS codigo_via_ingreso,"+
																	"getnombreviaingresotipopac(c.id) AS via_ingreso,"+
																	"sc.convenio AS codigo_convenio,"+
																	"getnombreconvenio(sc.convenio) AS convenio,"+
																	"c.fecha_apertura AS fecha_apertura,"+
																	"cc.fecha AS fecha_cierre,"+
																	"cc.hora AS hora_cierre,"+
																	"cc.motivo AS motivo,"+
																	"cc.usuario AS usuario_cierre, "+
																	"ca.descripcion as centro_atencion,  "+
																	"CASE WHEN pes.ingreso IS NULL THEN coalesce(getDescEntitadSubContratada(pes.entidad_subcontratada),'') ELSE coalesce(getDescripEntidadSubXingreso(c.id_ingreso),'') END AS descripcionentidadsub "+			   
																	"FROM cuentas c "+ 
																	"	  INNER JOIN sub_cuentas sc ON(sc.ingreso=c.id_ingreso AND sc.nro_prioridad = 1) 				 "+ 
																	"	  INNER JOIN cierre_cuentas cc ON(cc.cuenta=c.id) 						 "+
																	" 	  INNER JOIN centros_costo ceco ON(ceco.codigo=c.area)					 "+ 
																	" 	  INNER JOIN centro_atencion ca ON(ca.consecutivo=ceco.centro_atencion)	 "+
																	" 	  INNER JOIN ingresos i ON(i.id=c.id_ingreso)	 "+
																" 	  LEFT OUTER JOIN pac_entidades_subcontratadas pes ON(pes.consecutivo=i.pac_entidades_subcontratadas)	 "+
																	"WHERE "+ 
																	"c.codigo_paciente=? AND "+ 
																	"c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaCerrada+" "+
																	"ORDER BY cc.fecha DESC, cc.hora DESC";
	
	/**
	 * Cadena que lista las cuentas
	 */
	private static final String consultarCuentasStr =   " SELECT "+ 
														"c.id AS cuenta, "+
														"i.preingreso AS indpre," +
														"i.consecutivo as ingreso," +
														"CASE WHEN i.reingreso IS NOT NULL THEN i.reingreso ELSE 0 END AS indrei, " +
														"c.fecha_apertura AS fecha, "+
														"c.hora_apertura AS hora, " +
														"c.codigo_paciente AS codigo_paciente, "+
														"p.tipo_identificacion AS tipo_identificacion, " +
														"p.numero_identificacion As numero_identificacion, "+
														"getnombreestadocuenta(c.estado_cuenta) AS estadoCuenta, " +
														"c.usuario_modifica AS usuario, "+
														"getnombreviaingresotipopac(c.id) AS viaIngreso, "+
														"getnombrepersona(c.codigo_paciente) AS paciente, " +
														"getintegridaddominio(c.tipo_evento) AS tipo_evento, "+
														"getnombreconvenio(sc.convenio) AS convenio," +
														"getnomcentroatencion(cc.centro_atencion) as centro_atencion," +
														"c.hospital_dia," +
														"CASE WHEN pes.ingreso IS NULL THEN getDescEntitadSubContratada(pes.entidad_subcontratada) ELSE coalesce(getDescripEntidadSubXingreso(c.id_ingreso),'') END AS descripcionentidadsub "+  
														"FROM cuentas c " +
														"INNER JOIN personas p ON(p.codigo=c.codigo_paciente) "+ 
														"INNER JOIN sub_cuentas sc ON(sc.ingreso=c.id_ingreso AND sc.nro_prioridad = 1) " +
														"INNER JOIN ingresos i ON(i.id=c.id_ingreso)	 "+
														"LEFT OUTER JOIN pac_entidades_subcontratadas pes ON(pes.consecutivo=i.pac_entidades_subcontratadas)	 "+
														"INNER JOIN centros_costo cc ON(cc.codigo=c.area) ";
	
	/**
	 * Cadena que consulta el contrato actual de un convenio
	 */
	private static final String consultarContratoStr = "SELECT " +
		"codigo AS codigo " +
		"FROM contratos " +
		"WHERE " +
		"convenio = ? AND ";
	
	/**
	 * Cadena que consulta la ultima cuenta de urgencias
	 */
	private static final String cargarCuentaUrgenciasAsociadaStr = "SELECT "+
		"cu.id as id "+
		"FROM ingresos i "+
		"INNER JOIN cuentas cu ON(cu.id_ingreso=i.id) "+  
		"WHERE i.id=? AND i.estado IN ('"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"','"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"') AND cu.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaAsociada+" AND cu.via_ingreso="+ConstantesBD.codigoViaIngresoUrgencias;
		
	
	
	/**
	 * Para manejar los logs de esta clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseCuentaDao.class);
	
	/**
	 * Cadena que carga los datos de una cuenta
	 */
	private static final String cargarCuentaStr = "SELECT "+ 
		"c.id AS id_cuenta, "+
		"c.codigo_paciente As codigo_paciente, "+
		"getnombrepersona(c.codigo_paciente) As nombre_paciente, "+
		"to_char(c.fecha_apertura,'"+ConstantesBD.formatoFechaAp+"') AS fecha_apertura, "+
		"substr(c.hora_apertura,0,6) AS hora_apertura, "+
		"c.estado_cuenta AS codigo_estado_cuenta, "+
		"getnombreestadocuenta(c.estado_cuenta) As nombre_estado_cuenta, "+
		"c.tipo_paciente AS codigo_tipo_paciente, "+
		"getnombretipopaciente(c.tipo_paciente) As nombre_tipo_paciente, "+
		"c.id_ingreso AS id_ingreso, "+
		"c.usuario_modifica AS usuario_modifica, "+
		"coalesce(c.area,0) AS codigo_area, "+
		"coalesce(getnomcentrocosto(c.area),'') AS nombre_area, "+
		"coalesce(c.tipo_evento,'') AS codigo_tipo_evento, "+
		"coalesce(c.convenio_arp_afiliado,0) AS codigo_arp_afiliado, "+
		"coalesce(getnombreconvenio(c.convenio_arp_afiliado),'') AS nombre_arp_afiliado, "+			
		"c.desplazado AS desplazado, "+
		"c.origen_admision AS codigo_origen_admision, "+
		"getnomorigenadmision(c.origen_admision) AS nombre_origen_admision, "+
		"coalesce(c.tipo_complejidad,0) AS codigo_tipo_complejidad, "+
		"coalesce(getnomtipocomplejidad(c.tipo_complejidad),'') AS nombre_tipo_complejidad, "+
		"c.via_ingreso AS codigo_via_ingreso, " +
		"getnombreviaingresotipopac(c.id) AS nombre_via_ingreso, "+
		"coalesce(c.codigo_responsable_paciente||'','') AS codigo_responsable_paciente, " +
		"c.hospital_dia AS hospital_dia," +
		"CASE WHEN c.id_ingreso IS NULL THEN '' ELSE coalesce(facturacion.getDescripEntidadSubXingreso(c.id_ingreso),'') END AS descripcion_entidadsub," +
		" coalesce(gettransplante(c.id_ingreso),'') As transplante, " +
		"coalesce(c.observaciones,'') as observaciones," +
		"ing.consecutivo as consecutivoingreso  "+ 
		"FROM cuentas c inner join ingresos ing on (c.id_ingreso=ing.id) "+ 
		"WHERE c.id = ?";
	
	/**
	 * Cadena que carga los datos de una sub cuenta
	 */
	private static final String cargarSubCuentaStr = "SELECT "+ 
		"sc.sub_cuenta As sub_cuenta, "+
		"sc.convenio As codigo_convenio, "+
		"getnombreconvenio(sc.convenio) As nombre_convenio, "+
		"sc.naturaleza_paciente AS codigo_naturaleza_paciente, "+
		"getnomnatpacientes(sc.naturaleza_paciente) As nombre_naturaleza_paciente, "+
		"sc.monto_cobro AS codigo_monto_cobro, " +
		"dmc.tipo_detalle as tipodetalle," +
		"sc.tipo_cobro_paciente as tipocobropaciente,"+
		"CASE WHEN dmc.tipo_detalle = '"+ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroGEN+"' THEN getIntegridadDominio('"+ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroGEN+"') ELSE getIntegridadDominio('"+ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroDET+"') END AS nombre_monto_cobro, "+
		"coalesce(sc.nro_poliza,'') AS numero_poliza, "+
		"coalesce(sc.nro_carnet,'') As numero_carnet, "+
		"sc.contrato AS codigo_contrato, "+
		"getnumerocontrato(sc.contrato) As numero_contrato, "+
		"sc.ingreso As id_ingreso, "+
		"sc.tipo_afiliado AS codigo_tipo_afiliado, "+
		"getnombretipoafiliado(sc.tipo_afiliado) AS nombre_tipo_afiliado, "+
		"sc.clasificacion_socioeconomica AS codigo_estrato_social, "+
		"getnombreestrato(sc.clasificacion_socioeconomica) AS nombre_estrato_social, "+
		"coalesce(aut.codigo,"+ConstantesBD.codigoNuncaValido+") AS numero_autorizacion, "+
		"coalesce(to_char(sc.fecha_afiliacion,'DD/MM/YYYY'),'') AS fecha_afiliacion, "+
		"coalesce(sc.semanas_cotizacion,0) AS semanas_cotizacion, "+
		"coalesce(sc.meses_cotizacion,0) AS meses_cotizacion, "+
		"sc.codigo_paciente As codigo_paciente, "+
		"coalesce(sc.valor_utilizado_soat||'','') AS valor_utilizado_soat, "+
		"sc.nro_prioridad AS numero_prioridad, "+
		"sc.facturado AS facturado, " +
		"coalesce(sc.numero_solicitud_volante||'','') AS numero_solicitud_volante," +
		
		
		// consultar el nit del tercero para poder insertarlo en la tabla ax_pacien de la interfaz.
		"(select (select substr(t.numero_identificacion,1,12) from terceros t where t.codigo = em.tercero) as numero_identificacion from convenios co inner join empresas em on (em.codigo = co.empresa) where co.codigo = sc.convenio) as nit," +
		
		
		"CASE WHEN sc.empresas_institucion IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE sc.empresas_institucion END AS empresas_institucion, " +
		"coalesce(sc.tipo_cobertura,"+ConstantesBD.codigoNuncaValido+") as codigo_tipo_cobertura, " +
		"coalesce(getnombrecobertura(sc.tipo_cobertura),'') as nombre_tipo_cobertura, " +
		"vrat.codigo AS codigo_accidente_transito, " +
		"tc.aseg_at_ec AS aseg_at_ec " +
		"FROM sub_cuentas sc " +
		"INNER JOIN cuentas cu ON (cu.id_ingreso = sc.ingreso) " +
		"INNER JOIN convenios conv ON(conv.codigo=sc.convenio) " +
		"INNER JOIN tipos_convenio tc ON(tc.codigo=conv.tipo_convenio) " +
		"LEFT OUTER JOIN detalle_monto dmc ON(dmc.detalle_codigo=sc.monto_cobro) " +
		"LEFT OUTER JOIN autorizaciones  aut ON (aut.cuenta = cu.id AND aut.sub_cuenta = sc.sub_cuenta ) "+
		"LEFT OUTER JOIN manejopaciente.view_registro_accid_transito vrat ON(sc.convenio=vrat.aseguradora AND sc.ingreso=vrat.ingreso) "+
		"WHERE "+ 
		"sc.ingreso = ? AND cu.id = ? ORDER BY sc.nro_prioridad";
	
	/**
	 * Cadena que carga la informacion de poliza de un convenio(subcuenta)
	 */
	private static final String cargarInformacionPolizaStr = "SELECT "+ 
		"codigo, "+
		"sub_cuenta, "+
		"to_char(fecha_autorizacion,'"+ConstantesBD.formatoFechaAp+"') AS fecha_autorizacion, "+
		"numero_autorizacion, "+
		"valor_monto_autorizado, "+
		"usuario "+ 
		"FROM informacion_poliza "+ 
		"WHERE sub_cuenta = ?";	
	
	/**
	 * Cadena que carga la informacion del titular de un convenio(subcuenta)
	 */
	private static final String cargarTitularPolizaStr = "SELECT "+ 
		"tp.sub_cuenta, "+
		"tp.nombres_titular, "+
		"tp.apellidos_titular, "+
		"tp.tipoid_titular As codigo_tipo_id, "+
		"getnombretipoidentificacion(tp.tipoid_titular) AS nombre_tipo_id, "+
		"tp.numeroid_titular, "+
		"tp.direccion_titular, "+
		"tp.telefono_titular "+  
		"FROM titular_poliza tp "+  
		"WHERE tp.sub_cuenta = ?";
	
	/**
	 * Cadena que carga los requisitos del paciente por convenios
	 */
	private static final String cargarRequisitosPacienteStr = "SELECT " +
		"rps.requisito_paciente AS codigo," +
		"rps.subcuenta," +
		"CASE WHEN rps.cumplido = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS cumplido," +
		"rp.tipo_requisito as tipo," +
		"rp.descripcion " +
		"FROM requisitos_pac_subcuenta rps " +
		"INNER JOIN requisitos_paciente rp ON(rp.codigo=rps.requisito_paciente) " +
		"WHERE rps.subcuenta = ?  " +
		"order by rp.tipo_requisito,rp.descripcion";
	
	/**
	 * Cadena que carga la verificacion de derechos
	 */
	private static final String cargarVerificacionDerechosStr = "SELECT "+ 
		"vd.sub_cuenta AS sub_cuenta, " +
		"vd.ingreso AS ingreso, " +
		"vd.convenio AS convenio, "+
		"vd.estado AS estado, "+
		"vd.tipo_verificacion AS tipo_verificacion, "+
		"coalesce(vd.numero_verificacion,'') AS numero_verificacion, "+
		"vd.persona_solicita AS persona_solicita, "+
		"to_char(vd.fecha_solicitud,'"+ConstantesBD.formatoFechaAp+"') AS fecha_solicitud, "+
		"vd.hora_solicitud AS hora_solicitud, "+
		"coalesce(vd.persona_contactada,'') AS persona_contactada, "+
		"to_char(vd.fecha_verificacion,'"+ConstantesBD.formatoFechaAp+"') As fecha_verificacion, "+
		"vd.hora_verificacion AS hora_verificacion, "+
		"coalesce(vd.porcentaje_cobertura||'','') AS porcentaje_cobertura, "+
		"coalesce(vd.cuota_verificacion||'','') As cuota_verificacion, "+
		"coalesce(vd.observaciones,'') AS observaciones, "+
		"vd.usuario_modifica AS usuario_modifica "+ 
		"FROM verificaciones_derechos vd "+ 
		"WHERE vd.ingreso = ? AND vd.convenio = ?";
	
	/**
	 * Cadena que carga los responsables pacientes
	 */
	private static final String cargarResponsablePacienteStr = "SELECT "+ 
	"rp.codigo, "+ 
	"rp.numero_identificacion, "+ 
	"rp.tipo_identificacion," +
	"getnombretipoidentificacion(rp.tipo_identificacion) AS nombre_tipo_identificacion, "+ 
	"coalesce(rp.direccion,'') As direccion, "+ 
	"rp.telefono, "+ 
	"rp.relacion_paciente, "+ 
	"coalesce(rp.codigo_pais_doc,'') AS codigo_pais_doc, "+
	"coalesce(getdescripcionpais(rp.codigo_pais_doc),'') AS descripcion_pais_doc, "+
	"coalesce(rp.codigo_ciudad_doc,'') AS codigo_ciudad_doc, "+
	"coalesce(rp.codigo_depto_doc,'') AS codigo_depto_doc, "+
	"CASE WHEN rp.codigo_ciudad_doc IS NULL THEN '' ELSE getnombreciudad(rp.codigo_pais_doc,rp.codigo_depto_doc,rp.codigo_ciudad_doc) || ' (' || getnombredepto(rp.codigo_pais_doc,rp.codigo_depto_doc) || ')' END AS descripcion_ciudad_doc, "+
	"rp.primer_apellido, "+
	"coalesce(rp.segundo_apellido,'') AS segundo_apellido, "+
	"rp.primer_nombre, "+
	"coalesce(rp.segundo_nombre,'') AS segundo_nombre, "+
	"coalesce(rp.codigo_pais,'') AS codigo_pais, "+
	"coalesce(getdescripcionpais(rp.codigo_pais),'') AS descripcion_pais, "+
	"coalesce(rp.codigo_ciudad,'') AS codigo_ciudad, "+
	"coalesce(rp.codigo_depto,'') AS codigo_depto, "+
	"CASE WHEN rp.codigo_ciudad IS NULL THEN '' ELSE getnombreciudad(rp.codigo_pais,rp.codigo_depto,rp.codigo_ciudad) || ' (' || getnombredepto(rp.codigo_pais,rp.codigo_depto) || ')' END AS descripcion_ciudad, "+
	"coalesce(getdescripcionbarrio(rp.codigo_barrio),'') As descripcion_barrio, "+
	"coalesce(to_char(rp.fecha_nacimiento,'"+ConstantesBD.formatoFechaAp+"'),'') AS fecha_nacimiento," +
	"coalesce(rp.usuario_modifica,'') AS usuario_modifica "+ 
	"FROM responsables_pacientes rp " +
	"WHERE rp.codigo = ?";

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//modificacion por tarea 12918
	/**
	 * Encargado de realizar el asocio de cuentas ingresando los datos de cuenta incial y final 
	 */
	public static final String strIngresoAsocioCuentasTotal=" INSERT INTO asocios_cuenta (codigo,fecha,hora,usuario,cuenta_inicial,cuenta_final,activo,ingreso ) VALUES (?,?,?,?,?,?,?,?)";
		
	/**
	 * encargado de actualizar el ingreso a una cuenta.
	 */
	 public static final String strTrasladoCuentaIngreso = " UPDATE cuentas SET id_ingreso=?, estado_cuenta=? where id_ingreso=?";
	
	 /**
	  * Metodo encargado de realizar un asocio de cuentas 
	  * ingresando la cuenta inicial y final 
	  * @author Jhony Alexander Duque A.
	  * @param connection
	  * @param datos
	  * ------------------------------
	  * KEY'S DEL MAPA DATOS
	  * ------------------------------
	  * -- usuario (Requerido)  
	  * -- cuentaInicial (Requerido)  
	  * -- cuentaFinal (Requerido)  
	  * -- activo (Requerido) 
	  * -- ingreso (Requerido) 
	  * 
	  * @return
	  */
	 public static boolean asociosCuentaTotal (Connection connection,HashMap datos)
	 {
		 logger.info("\n entre a asociosCuentaTotal --> "+datos);
		 
		 String cadena=strIngresoAsocioCuentasTotal;
		 
		 try 
		 {
			 PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			 //cosecutivo 
			 ps.setInt(1,UtilidadBD.obtenerSiguienteValorSecuencia(connection, "seq_asocios_cuenta"));
			 //fecha
			 ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			 //hora
			 ps.setString(3, UtilidadFecha.getHoraActual());
			 //usuario
			 ps.setString(4, datos.get("usuario")+"");
			 //cuenta inicial
			 ps.setInt(5, Utilidades.convertirAEntero(datos.get("cuentaInicial")+""));
			 //cuenta final
			 ps.setInt(6, Utilidades.convertirAEntero(datos.get("cuentaFinal")+""));
			 //activo
			 ps.setBoolean(7, UtilidadTexto.getBoolean(datos.get("activo")+""));
			 //ingreso
			 ps.setInt(8, Utilidades.convertirAEntero(datos.get("ingreso")+""));
			 
			 if (ps.executeUpdate()>0)
				 return true;
			 
		} 
		 catch (SQLException e) 
		 {
			 logger.info("\n problema actualizando el ingreso de la cuenta "+e);
		}
		 
		 return false;
	 }
	 
	 
	 
	 
	 
	 /**
	  * Metodo encargado de trasladarle una cuenta a un ingreso
	  * @author Jhony Alexander Duque A.
	  * @param connection
	  * @param datos
	  * -----------------------------
	  * KEY'S DEL MAPA DATOS
	  * -----------------------------
	  * -- ingresoTraslado (Requerido)--> ingreso a donde se va a trasladar la cuenta
	  * -- estadoCuenta (Requerido) --> estado en el cual queda la cuenta 
	  * -- ingresoCuenta (Requerido) -->  ingreso donde se encuentra la cuenta a trasladar
	  * 
	  * @return
	  */
	 public static boolean trasladarCuentaAingreso (Connection connection,HashMap datos)throws IPSException
	 {
		 logger.info("\n entre a trasladarCuentaAingreso --> "+datos);
		 
		 unificarSubCuentas(connection, datos);
		 
	 	 String cadena=strTrasladoCuentaIngreso;
	 
		 try 
		 {
			 PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			 //id ingreso donde se va a trasladar la cuenta
			 ps.setInt(1, Utilidades.convertirAEntero(datos.get("ingresoTraslado")+""));
			 //estado en el cual va a quedar la cuenta
			 ps.setInt(2, Utilidades.convertirAEntero(datos.get("estadoCuenta")+""));
			 //id ingreso de la cuenta
			 ps.setInt(3, Utilidades.convertirAEntero(datos.get("ingresoCuenta")+""));
			 
			 if (ps.executeUpdate()>0)
				 return true;
			 
		 } 
		 catch (SQLException e) 
		 {
			 logger.info("\n problema actualizando el ingreso de la cuenta "+e);
		 }
		 
		 return false;
	 }
	 
	 /**
	  * metodo que verifica la existencia de una subcuenta
	  * @param connection
	  * @param datos
	  */
	 private static void unificarSubCuentas(Connection connection, HashMap datos) throws IPSException
	 {
		 logger.info("\n\n****************************UNIFICAR SUBCUENTAS***************************************************************************");
		 ArrayList<DtoSubCuentas> dtoSubCuentasVectorTraslado= UtilidadesHistoriaClinica.obtenerResponsablesIngreso(connection, Integer.parseInt(datos.get("ingresoTraslado")+""),false,new String[0],false,"" /*subCuenta*/,ConstantesBD.codigoNuncaValido /*Vï¿½a de ingresp*/);
		 ArrayList<DtoSubCuentas> dtoSubCuentasVectorCuenta= UtilidadesHistoriaClinica.obtenerResponsablesIngreso(connection, Integer.parseInt(datos.get("ingresoCuenta")+""),false,new String[0],false,"" /*subCuenta*/,ConstantesBD.codigoNuncaValido /*Vï¿½a de ingreso*/);
		 
		 for(int w=0; w<dtoSubCuentasVectorCuenta.size(); w++)
		 {
			 boolean actualizo=false;
			 for(int x=0; x<dtoSubCuentasVectorTraslado.size();x++)
			 {
				 if(dtoSubCuentasVectorCuenta.get(w).getConvenio().getCodigo()== dtoSubCuentasVectorTraslado.get(x).getConvenio().getCodigo()
				 	&& dtoSubCuentasVectorCuenta.get(w).getContrato()== dtoSubCuentasVectorTraslado.get(x).getContrato())
				 {
					 logger.info("SON CONTRATOS CONVENIOS IGUALES codigo-->"+dtoSubCuentasVectorCuenta.get(w).getConvenio().getCodigo()+"!!!");
					 //ACTUALIZAMOS LAS SOLICITUDES SUBCUENTA
					 
					 ///XPLANNNER 67806 ----- ESTABA DEJANDO CUENTAS SIN VALORACIONES, Y OTRAS CON N VALORACIONES
					 
					 int cuentaTraslado= obtenerCuentaSubcuenta(connection, dtoSubCuentasVectorTraslado.get(w));
					 //if(cuentaTraslado>0)
					 //actualizarSolicitudes(connection, cuentaTraslado, dtoSubCuentasVectorCuenta.get(x).getSubCuenta());
					 
					 actualizarSubCuentas(connection, dtoSubCuentasVectorCuenta.get(x), dtoSubCuentasVectorTraslado.get(w));
					 actualizarCargos(connection, dtoSubCuentasVectorCuenta.get(x), dtoSubCuentasVectorTraslado.get(w));
					 
					 actualizo=true;
				 }
			 }
			 if(!actualizo)
			 {
				 logger.info("el contarto no esta entonces se pasa al ingreso activo--> subcuenta->"+dtoSubCuentasVectorCuenta.get(w).getSubCuenta());
				 trasladarSubCuentaAIngreso(connection, datos.get("ingresoTraslado")+"", dtoSubCuentasVectorCuenta.get(w).getSubCuenta());
			 }	 
		 }
		 logger.info("****************************fin UNIFICAR SUBCUENTAS***************************************************************************\n\n");
	 }	

	/**
	  * 
	  * @param connection
	  * @param dtoSubCuenta
	  * @param dtoSubCuentaTraslado
	  * @return
	  */
	 private static boolean actualizarSubCuentas(Connection connection, DtoSubCuentas dtoSubCuenta, DtoSubCuentas dtoSubCuentaTraslado) 
	 {
		 String consulta="";
		 //int cuenta= obtenerCuentaSubcuenta(connection, dtoSubCuentaTraslado);
		 
		 //if(cuenta>0)
		 //	 consulta="UPDATE solicitudes_subcuenta set sub_cuenta=?, cuenta="+cuenta+" where sub_cuenta = ?";
		 //else
		 consulta="UPDATE solicitudes_subcuenta set sub_cuenta=? where sub_cuenta = ?";
		 
		 logger.info("actualizarSubCuentas-->"+consulta+" ->"+dtoSubCuentaTraslado.getSubCuenta()+"->"+dtoSubCuenta.getSubCuenta());
		 try 
		 {
			 PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			 //id ingreso donde se va a trasladar la sub_cuenta
			 ps.setInt(1, Utilidades.convertirAEntero(dtoSubCuentaTraslado.getSubCuenta()+""));
			 
			 //id ingreso de la cuenta
			 ps.setInt(2, Utilidades.convertirAEntero(dtoSubCuenta.getSubCuenta()+""));
			 
			 if (ps.executeUpdate()>0)
			 {	 
				  return true;
			 }	 
		 } 
		 catch (SQLException e) 
		 {
			 e.printStackTrace();
		 }
		 return false;
	 }

	 /**
	  * 
	  * @param connection
	  * @param dtoSubCuenta
	  * @return
	  */
	 private static int obtenerCuentaSubcuenta(Connection connection, DtoSubCuentas dtoSubCuenta) 
	 {
		 String consulta=" select cuenta from solicitudes_subcuenta where sub_cuenta=? limit 1 ";
		 try 
		 {
			 PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			 //id ingreso de la cuenta
			 ps.setInt(1, Utilidades.convertirAEntero(dtoSubCuenta.getSubCuenta()+""));
			 
			 ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			 if(rs.next())
				 return rs.getInt(1);
		 } 
		 catch (SQLException e) 
		 {
			 e.printStackTrace();
		 }
		 return ConstantesBD.codigoNuncaValido;
	 }

	/**
	  * 
	  * @param connection
	  * @param dtoSubCuenta
	  * @param dtoSubCuentaTraslado
	  * @return
	  */
	 private static boolean actualizarCargos(Connection connection, DtoSubCuentas dtoSubCuenta, DtoSubCuentas dtoSubCuentaTraslado) 
	 {
		 String consulta="UPDATE det_cargos set sub_cuenta=? where sub_cuenta = ?";
		 logger.info("actualizarCargos-->"+consulta+" ->"+dtoSubCuentaTraslado.getSubCuenta()+"->"+dtoSubCuenta.getSubCuenta());
		 try 
		 {
			 PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			 //id ingreso donde se va a trasladar la sub_cuenta
			 ps.setInt(1, Utilidades.convertirAEntero(dtoSubCuentaTraslado.getSubCuenta()+""));
			 //id ingreso de la cuenta
			 ps.setInt(2, Utilidades.convertirAEntero(dtoSubCuenta.getSubCuenta()+""));
			 
			 if (ps.executeUpdate()>0)
				 return true;
		 } 
		 catch (SQLException e) 
		 {
			 e.printStackTrace();
		 }
		 return false;
	 }
	
	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @param subCuenta
	 * @return
	 */ 
	private static boolean actualizarSolicitudes(Connection con, int cuenta, String subCuenta)
	{
		String consulta="UPDATE solicitudes set cuenta=? where numero_solicitud in (select solicitud from solicitudes_subcuenta where sub_cuenta="+subCuenta+") ";
		
		logger.info("actualizarSolicitudes-->"+consulta+" ->"+cuenta);
		 try 
		 {
			 PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			 //id ingreso donde se va a trasladar la sub_cuenta
			 ps.setInt(1, cuenta);
			 
			 if (ps.executeUpdate()>0)
				 return true;
		 } 
		 catch (SQLException e) 
		 {
			 e.printStackTrace();
		 }
		 return false;
	}
	
	 
	 
	/**
	  * 
	  * @param connection
	  * @param datos
	  */
	 private static boolean trasladarSubCuentaAIngreso(Connection connection, String ingresoTraslado, String subCuenta ) 
	 {
		 String strTrasladoSubCuentaIngreso = "UPDATE sub_cuentas SET ingreso=?, nro_prioridad=(select coalesce(max(nro_prioridad), 0)+1 from sub_cuentas where ingreso=?)  WHERE sub_cuenta= ? ";
		 try 
		 {
			 logger.info("trasladarSubCuentaAIngreso-->"+strTrasladoSubCuentaIngreso+" ->"+ingresoTraslado+"->"+subCuenta);
			 PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(strTrasladoSubCuentaIngreso, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			 //id ingreso donde se va a trasladar la sub_cuenta
			 ps.setInt(1, Utilidades.convertirAEntero(ingresoTraslado));
			 ps.setInt(2, Utilidades.convertirAEntero(ingresoTraslado));
			 //id ingreso de la cuenta
			 ps.setInt(3, Utilidades.convertirAEntero(subCuenta));
			 
			 if (ps.executeUpdate()>0)
				 return true;
			 
		 } 
		 catch (SQLException e) 
		 {
			 logger.info("\n problema actualizando el ingreso de la cuenta "+e);
		 }
		 return false;
	 }





	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 
	/**
	 * Carga una cuenta desde la base de datos PostgresSQL.
	 * @param con una conexion abierta con una base de datos PostgresSQL
	 * @param idCuenta nï¿½mero de identificaciï¿½n de esta cuenta en la base de datos PostgresSQL
	 * @return objeto <code>Answer</code> con la nformaciï¿½n solicitada y una conexiï¿½n abierta con la base de datos PostgresSQL
	 */
	public static Answer cargarCuenta(Connection con, String idCuenta) throws SQLException
	{
		ResultSetDecorator rs=null;
		if (con == null || con.isClosed()) {
			DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			con = myFactory.getConnection();
		}
		PreparedStatementDecorator cargarCuentaStatement= new PreparedStatementDecorator(con.prepareStatement(cargarCuenta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cargarCuentaStatement.setString(1, idCuenta);
		rs=new ResultSetDecorator(cargarCuentaStatement.executeQuery());
		return new Answer(rs, con);
	}

	/**
	 * Carga informacion basica de la cuenta 
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param idCuenta numero de identificacion de esta cuenta en la fuente de datos
	 * @param idIngreso numero de ingreso de la cuenta en la fuente de datos
	 * @param idConvenio numero de convenio de la sub cuenta en la fuente de datos
	 * @return objeto <code>Answer</code> con la nformaciï¿½n solicitada y una conexiï¿½n abierta con la fuente de datos
	 */
	public static Answer cargarAfiliacionClasificacionSocEconomica(Connection con, String idCuenta,String idIngreso, String idConvenio)throws SQLException{
		ResultSetDecorator rs=null;
		if (con == null || con.isClosed()) {
			DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			con = myFactory.getConnection();
		}
		PreparedStatementDecorator cargarCuentaStatement= new PreparedStatementDecorator(con.prepareStatement(cargarInfoBasicaCuenta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cargarCuentaStatement.setString(1, idCuenta);
		cargarCuentaStatement.setString(2, idIngreso);
		cargarCuentaStatement.setString(3, idConvenio);
		rs=new ResultSetDecorator(cargarCuentaStatement.executeQuery());
		return new Answer(rs, con);
	}
	

	/**
	 * Implementaciï¿½n de la inserciï¿½n de la busqueda de la fecha
	 * de creaciï¿½n de la cuenta en una BD Genï¿½rica
	 * 
	 * @see com.princetonsa.dao.CuentaDao#buscarFechaCreacionCuenta (Connection, int)
	 */
	public static String buscarFechaCreacionCuenta (Connection con, int idCuenta) throws SQLException
	{
		PreparedStatementDecorator buscarFechaCreacionCuentaStatement= new PreparedStatementDecorator(con.prepareStatement(buscarFechaCreacionCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		buscarFechaCreacionCuentaStatement.setInt(1, idCuenta);
		
		ResultSetDecorator rs=new ResultSetDecorator(buscarFechaCreacionCuentaStatement.executeQuery());

		if (rs.next())
		{
			return rs.getString("fechaApertura");
		}
		else
		{
			//Si esta consulta no da un resultado es porque algo muy, pero
			//muy malo esta pasando
			throw new SQLException ("Error Desconocido en la busqueda de la fecha de creaciï¿½n de la cuenta");
		}
	}

	/**
	 * Implementaciï¿½n de la inserciï¿½n de la busqueda de la fecha
	 * de creaciï¿½n de la cuenta en una BD Genï¿½rica
	 * 
	 * @see com.princetonsa.dao.CuentaDao#asociarCuenta (con, int ) throws SQLException
	 */
	public static int asociarCuenta (Connection con, int idCuenta,int idIngreso, String usuario, String crearEntradaAsocioCuentaStr) throws SQLException
	{
		int resp1=0, resp2=0;
		boolean resp0=false;
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		
		
		resp0=myFactory.beginTransaction(con);
		
		try
		{
			PreparedStatementDecorator crearEntradaAsocioCuentaStatement= new PreparedStatementDecorator(con.prepareStatement(crearEntradaAsocioCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			crearEntradaAsocioCuentaStatement.setInt(1, idCuenta);
			crearEntradaAsocioCuentaStatement.setString(2, usuario);
			crearEntradaAsocioCuentaStatement.setInt(3,idIngreso);
			resp1=crearEntradaAsocioCuentaStatement.executeUpdate();
			
			PreparedStatementDecorator asociarCuentaStatement= new PreparedStatementDecorator(con.prepareStatement(cambiarEstadoCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			asociarCuentaStatement.setInt(1, ConstantesBD.codigoEstadoCuentaAsociada);
			asociarCuentaStatement.setInt(2, idCuenta);
			resp2=asociarCuentaStatement.executeUpdate();
			
			if (!resp0||resp1<=0||resp2<=0)
			{
				myFactory.abortTransaction(con);
			}
			else
			{
				myFactory.endTransaction(con);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			myFactory.abortTransaction(con);
		}
		
		return resp2;
	}

	/**
	 * Mï¿½todo que obtiene el cï¿½digo de la cuenta recientemente asociada
	 * 
	 * @param con Conexiï¿½n con la fuente de datos
	 * @param idIngreso 
	 * @return
	 * @throws SQLException
	 */
	private static int obtenerCodigoCuentaRecienAsociada (Connection con, String idIngreso,String viaIngreso) throws SQLException
	{
		int idCuenta;
		
		String temporal = ConstantesBD.codigoViaIngresoUrgencias+"";		
		if(viaIngreso.equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
			temporal = ConstantesBD.codigoViaIngresoHospitalizacion+"";		
		
		String obtenerCodigoCuentaStr="SELECT id from cuentas where id_ingreso =? and estado_cuenta IN (" + ConstantesBD.codigoEstadoCuentaAsociada +","+ ConstantesBD.codigoEstadoCuentaFacturadaParcial+") and via_ingreso = "+temporal;
		
		PreparedStatementDecorator obtenerCodigoCuentaStatement= new PreparedStatementDecorator(con.prepareStatement(obtenerCodigoCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		obtenerCodigoCuentaStatement.setInt(1, Utilidades.convertirAEntero(idIngreso));
		ResultSetDecorator rs=new ResultSetDecorator(obtenerCodigoCuentaStatement.executeQuery());
		if (rs.next())
		{
			idCuenta=rs.getInt("id");
			if (idCuenta<=0)
			{
				return 0;
			}
			else
			{
				return idCuenta;
			}
		}
		else
		{
			return 0;
		}
	}

	/**
	 * Implementaciï¿½n del mï¿½todo que completa el asocio de cuenta
	 * transaccional en una BD Genï¿½rica
	 * 
	 * @see com.princetonsa.dao.CuentaDao#completarAsocioCuentaTransaccional(Connection , int , int , String ) throws SQLException
	 */
	public static int completarAsocioCuentaTransaccional(Connection con, int nuevaCuenta, String estado, String idIngreso, String viaIngreso) throws SQLException
	{
		boolean resp0=false;
		int resp1=0;
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int cuentaVieja=obtenerCodigoCuentaRecienAsociada (con, idIngreso, viaIngreso);
		if (cuentaVieja<=0)
		{
			return 0;
		}

		/* Iniciamos la transacciï¿½n */
		if(estado.equals("empezar")) {
			resp0 = myFactory.beginTransaction(con);
		}
		else {
			resp0 = true;
		}


		String completarAsocioCuentaStr="UPDATE asocios_cuenta set cuenta_final =? where activo=" + ValoresPorDefecto.getValorTrueParaConsultas() + " and cuenta_inicial=?";
		PreparedStatementDecorator completarAsocioCuentaStatement= new PreparedStatementDecorator(con.prepareStatement(completarAsocioCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		completarAsocioCuentaStatement.setInt(1, nuevaCuenta);
		completarAsocioCuentaStatement.setInt(2, cuentaVieja);
		resp1=completarAsocioCuentaStatement.executeUpdate();
		if (resp0 && resp1>0) 
		{
			if (estado.equals("finalizar")) 
			{
				myFactory.endTransaction(con);
			}
		}

		else 
		{
			myFactory.abortTransaction(con);
			resp1 = 0;
		}

		//Si llego acï¿½ todo saliï¿½ bien luego retorno el nï¿½mero 
		//de la cuenta vieja
		return cuentaVieja;
	}

	/**
	 * Implementaciï¿½n de la inserciï¿½n de la busqueda de la fecha
	 * de creaciï¿½n de la cuenta en una BD Genï¿½rica
	 * 
	 * @see com.princetonsa.dao.CuentaDao#asociarCuenta (con, int, int ) throws SQLException
	 */
	public static int desAsociarCuenta (Connection con,String usuario, String idIngreso, String viaIngreso) throws SQLException
	{
		int resp1=0, resp2=0, idCuenta=0;
		
		boolean resp0=true;
		
		idCuenta=obtenerCodigoCuentaRecienAsociada (con, idIngreso, viaIngreso);
		if (idCuenta<=0)
		{
			return 0;
		}
		
		try
		{
			PreparedStatementDecorator desactivarEntradaAsocioCuentaStatement= new PreparedStatementDecorator(con.prepareStatement(desactivarEntradaAsocioCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			desactivarEntradaAsocioCuentaStatement.setString(1, usuario);
			desactivarEntradaAsocioCuentaStatement.setInt(2, idCuenta);
			resp1=desactivarEntradaAsocioCuentaStatement.executeUpdate();
			
			PreparedStatementDecorator desAsociarCuentaStatement= new PreparedStatementDecorator(con.prepareStatement(cambiarEstadoCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			desAsociarCuentaStatement.setInt(1, ConstantesBD.codigoEstadoCuentaActiva);
			desAsociarCuentaStatement.setInt(2, idCuenta);
			String log="Desasocio de la cuenta "+ idCuenta;
			LogsAxioma.enviarLog(ConstantesBD.logAsocioCuentasCodigo,log,ConstantesBD.tipoRegistroLogModificacion, usuario);
			resp2=desAsociarCuentaStatement.executeUpdate();
			
			if (!resp0||resp1<=0||resp2<=0)
				resp2 = 0;
			else
				resp2 = 1;
		}
		catch (SQLException e)
		{
			logger.info("Error >> "+e);
		}
		
		return resp2;
	}

	/**
	 * Implementaciï¿½n de la consulta de codigos de cuentas asociados a un numero de ingreso dado
	 * @see com.princetonsa.dao.CuentaDao#getCodigosCuentasIngreso (con, int ) throws SQLException
	 * @version Sept. 11 de 2003
	 */
	public static ResultSetDecorator getCodigosCuentasIngreso(Connection con, int idIngreso) throws SQLException
	{
		if (con == null || con.isClosed()) 
		{
			logger.warn("La conexiï¿½n llegï¿½ cerrada. Se lanzï¿½ la siguiente excepciï¿½n:\nError SQL: Conexiï¿½n cerrada");
			throw new SQLException ("Error SQL: Conexiï¿½n cerrada");
		}
		
		try
		{			
			PreparedStatementDecorator consultarCodigosCuentasIngresoStatement= new PreparedStatementDecorator(con.prepareStatement(consultarCodigosCuentasIngresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consultarCodigosCuentasIngresoStatement.setInt(1, idIngreso);				
			return new ResultSetDecorator(consultarCodigosCuentasIngresoStatement.executeQuery());
		} 
		catch (SQLException sql) 
		{
			logger.warn("Error consultando cuentas asociadas al ingreso "+idIngreso+" en la tabla 'cuentas'.\n Se lanzï¿½ la siguiente excepciï¿½n:\n"+sql);
			throw sql;
		}		
	}

	/**
	 * Implementaciï¿½n de la consulta del centro de costo tratante para un numero de cuenta dado
	 * @see com.princetonsa.dao.CuentaDao#getCodigosCuentasIngreso (con, int ) throws SQLException
	 * @version Sept. 23 de 2003
	 */	
	public static String getCentroCostoTratante(Connection con, int idCuenta, int codigoCentroCostoTratante) throws SQLException
	{
		ResultSetDecorator rs;
		if (con == null || con.isClosed()) 
		{
			logger.warn("La conexiï¿½n llegï¿½ cerrada. Se lanzï¿½ la siguiente excepciï¿½n:\nError SQL: Conexiï¿½n cerrada");
			throw new SQLException ("Error SQL: Conexiï¿½n cerrada");
		}
		
		try
		{	
			PreparedStatementDecorator consultarCentroCostoTratanteStatement= new PreparedStatementDecorator(con.prepareStatement(consultarCentroCostoTratanteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consultarCentroCostoTratanteStatement.setInt(1,codigoCentroCostoTratante);
			rs = new ResultSetDecorator(consultarCentroCostoTratanteStatement.executeQuery());
			if(rs.next())
			{
				return rs.getString("centroCosto");
			}
			else
			{
				return "";
			}
		} 
		catch (SQLException sql) 
		{
			logger.warn("Error consultando centro de costo tratante para la cuenta  "+idCuenta+" en la tabla 'cuentas'.\n Se lanzï¿½ la siguiente excepciï¿½n:\n"+sql);
			throw sql;
		}
	}
	
	/**
	 * Mï¿½todo que cambia el estado de la cuenta 
	 * @param con
	 * @param codigoEstadoCuenta
	 * @param idCuenta
	 * @return
	 */
	public static int cambiarEstadoCuenta(	Connection con, int codigoEstadoCuenta, int idCuenta)
	{
	    int resp=0;	
	    PreparedStatement pst=null;
	    try
	    {
	        if (con == null || con.isClosed()) 
	        {
	            throw new SQLException ("Error SQL: Conexiï¿½n cerrada");
	        }
	        pst= con.prepareStatement(cambiarEstadoCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
	        pst.setInt(1,codigoEstadoCuenta);
	        pst.setInt(2,idCuenta);
	        resp=pst.executeUpdate();
	    }
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR cambiarEstadoCuenta",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR cambiarEstadoCuenta", e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}	
	    return resp;	
	}
	
	
	
	
	
	
	
	
	

	/**
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static float getValorTotalCuenta(Connection con, int idCuenta)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarValorTotalCuenta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,idCuenta);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getFloat("total");
			return -1;
		}
		catch(SQLException e)
		{
			logger.error("ERROR CONSULTA EL VALOR TOTAL DE UNA CUENTA "+idCuenta+"\n"+e.getStackTrace());
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * Adiciï¿½n Sebastiï¿½n
	 * Mï¿½todo usado para cargar la ï¿½ltima cuenta del paciente
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public static String cargarCuentaUrgenciasAsociada(Connection con,int codigoIngreso){
		
		PreparedStatement pst=null;
		ResultSet rs=null;
		String cuenta = "";
		try{
			pst= con.prepareStatement(cargarCuentaUrgenciasAsociadaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1,codigoIngreso);
			
			rs = pst.executeQuery();
			
			if(rs.next()){
				cuenta = (rs.getString("id")!=null)?rs.getString("id"):"";
			}
			return cuenta;
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR cargarCuentaUrgenciasAsociada",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR cargarCuentaUrgenciasAsociada", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return cuenta;
	}
	
	/**
	 * Adiciï¿½n Sebastiï¿½n
	 * Mï¿½todo para registrar el LOG tipo base de datos del cierre de la cuenta
	 * en el mï¿½dulo de facturaciï¿½n
	 * @param con
	 * @param idCuenta
	 * @param usuario
	 * @param motivo
	 * @param estado
	 * @return
	 */
	public static int registrarCierreCuenta(Connection con,int idCuenta, String usuario, String motivo, String estado){
		
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		try{
			int resp=0;
			
			if(estado.equals(ConstantesBD.inicioTransaccion))
				myFactory.beginTransaction(con);
			
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(registrarCierreCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,idCuenta);
			pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			pst.setString(3,UtilidadFecha.getHoraActual());
			pst.setString(4,usuario);
			pst.setString(5,motivo);
			
			resp=pst.executeUpdate();
			
			if(estado.equals(ConstantesBD.finTransaccion))
				myFactory.endTransaction(con);
			
			
			return resp;
		}
		catch(SQLException e){
			
			logger.error("Problemas registrando el cierre de cuenta en SqlBaseCuentaDao: "+e);
			return -1;
		}
	}
	
	/**
	 * Adiciï¿½n Sebastiï¿½n
	 * Mï¿½todo para consultar las cuentas cerradas del paciente
	 * @param con
	 * @param codigoPaciente
	 * @return listado de cuentas cerradas
	 */
	public static ResultSetDecorator consultarCuentasCerradasPaciente(Connection con,int codigoPaciente)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultarCuentasCerradasPacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoPaciente);
			
			logger.info("\n CONSULTA CUENTAS CERRADAS-->"+consultarCuentasCerradasPacienteStr+" codpac->"+codigoPaciente);
			return new ResultSetDecorator(pst.executeQuery());
		}
		catch(SQLException e)
		{
			logger.error("Error consultando los cuentas cerradas del paciente en SqlBaseCuentaDao: "+e);
			return null;
		}
	}
	
	/**
	 * Adiciï¿½n Sebastiï¿½n
	 * Mï¿½todo para consultar las cuentas cerradas por rangos de fechas y/o id's de cuentas
	 * @param con
	 * @param fechaCierreInicial
	 * @param fechaCierreFinal
	 * @param idCuentaInicial
	 * @param idCuentaFinal
	 * @param centroAtencion 
	 * @param String codigoEntidadSub
	 * @return listado de cuentas cerradas (cuenta,paciente(nombre,id,numero),via_ingreso,responsable,fecha_apertura,fecha_hora_cierre,usuario,motivo)
	 */
	public static ResultSetDecorator consultarCuentasCerradas(Connection con,String fechaCierreInicial,String fechaCierreFinal,int idCuentaInicial,int idCuentaFinal, int centroAtencion, String codigoEntidadSub)
	{
		/**
		 * cadena para consultar las cuentas cerradas por rangos
		 */
		String consultarCuentasCerradasStr="SELECT "+
			"c.id AS id,"+
			"p.primer_apellido || ' ' || p.segundo_apellido || ' ' || p.primer_nombre || ' ' || p.segundo_nombre AS nombre_paciente,"+
			"p.tipo_identificacion AS tipo_identificacion,"+
			"p.numero_identificacion AS numero_identificacion,"+ 
			"c.via_ingreso AS codigo_via_ingreso,"+
			"getnombreviaingresotipopac(c.id) AS via_ingreso,"+
			"sc.convenio AS codigo_convenio,"+
			"getnombreconvenio(sc.convenio) AS convenio,"+
			"c.fecha_apertura AS fecha_apertura,"+
			"cc.fecha AS fecha_cierre,"+
			"cc.hora AS hora_cierre,"+
			"cc.motivo AS motivo,"+
			"cc.usuario AS usuario_cierre, " +
			"ca.descripcion as centro_atencion,  " +
			"CASE WHEN pes.ingreso IS NULL THEN coalesce(getDescEntitadSubContratada(pes.entidad_subcontratada),'') ELSE coalesce(getDescripEntidadSubXingreso(c.id_ingreso),'') END AS descripcionentidadsub "+			   
			"FROM cuentas c "+ 
			"	  INNER JOIN sub_cuentas sc ON(sc.ingreso=c.id_ingreso AND sc.nro_prioridad = 1) 				 " + 
			"	  INNER JOIN cierre_cuentas cc ON(cc.cuenta=c.id) 						 " + 
			"	  INNER JOIN ingresos i ON(i.id=c.id_ingreso)	 "+
			"	  LEFT OUTER JOIN pac_entidades_subcontratadas pes ON(pes.consecutivo=i.pac_entidades_subcontratadas)	 "+
			"	  INNER JOIN personas p ON(p.codigo=c.codigo_paciente) 					 " +
			"	  INNER JOIN centros_costo ceco ON(ceco.codigo=c.area)					 " + 
			"	  INNER JOIN centro_atencion ca ON(ca.consecutivo=ceco.centro_atencion)	 ";
			
		try
		{
			int indicador=0;
			
			
			if(!fechaCierreInicial.equals("")&&!fechaCierreFinal.equals(""))
			{
				indicador=1;
				consultarCuentasCerradasStr+="WHERE (cc.fecha>=? AND cc.fecha<=?)";
			}
			
			if(idCuentaInicial!=0&&idCuentaFinal!=0)
			{
				if(indicador==1)
				{
					consultarCuentasCerradasStr+=" AND (c.id>=? AND c.id<=?)";
					indicador=2;
				}
				else
				{
					consultarCuentasCerradasStr+="WHERE (c.id>=? AND c.id<=?)";
					indicador=3;
				}
			}
			
			if(centroAtencion!=0)
			{
				if(indicador>0)
				{					
					consultarCuentasCerradasStr+=" AND (ca.consecutivo = " + centroAtencion + ")";
				}
				else
				{
					consultarCuentasCerradasStr+=" WHERE (ca.consecutivo = " + centroAtencion + ")";
					indicador=4;
				}
			}
			
			if( !UtilidadTexto.isEmpty(codigoEntidadSub))
			{				
				if(indicador>0 )
				{					
					consultarCuentasCerradasStr+=" AND (getCodigoEntidadSubXingreso(c.id_ingreso) = " + codigoEntidadSub + ")";
				}
				else
				{
					consultarCuentasCerradasStr+=" WHERE (getCodigoEntidadSubXingreso(c.id_ingreso) = " + codigoEntidadSub + ")";
				}				
			}
				
			
			consultarCuentasCerradasStr+=" ORDER BY cc.fecha DESC, cc.hora DESC";
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultarCuentasCerradasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			logger.info("CONSULTA CUENTAS CERRADAS POR RANGOS >>> "+consultarCuentasCerradasStr);
			switch(indicador)
			{
				case 1:
				pst.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaCierreInicial));
				pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaCierreFinal));	
				break;
				
				case 2:
				pst.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaCierreInicial));
				pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaCierreFinal));
				pst.setInt(3,idCuentaInicial);
				pst.setInt(4,idCuentaFinal);
				break;
				
				case 3:
				pst.setInt(1,idCuentaInicial);
				pst.setInt(2,idCuentaFinal);
				break;
			}
			
			return new ResultSetDecorator(pst.executeQuery());
			
		}
		catch(SQLException e)
		{
			logger.error("Error consultando las cuentas cerradas por rangos en SqlBaseCuentaDao: "+e);
			return null;
		}
	}
	
	/**
	 * Mï¿½todo que deshace el asocio en la facturacion independiente
	 * @param con
	 * @param codigoCuentaFinal
	 * @param usuario
	 * @return
	 */
	public static int desAsociarCuentaFacturacion(Connection con, int codigoCuenta, String usuario)
	{
		String desasocioCuentaFacturacionStr="UPDATE asocios_cuenta " +
				"set activo="+ValoresPorDefecto.getValorFalseParaConsultas()+", " +
				"usuario_desasocio=?," +
				"fecha_desasocio = CURRENT_DATE," +
				"hora_desasocio = "+ValoresPorDefecto.getSentenciaHoraActualBD()+"  " +
				"where activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" AND cuenta_final=?";
		try
		{
			PreparedStatementDecorator desasocioCuentaFacturacionStm= new PreparedStatementDecorator(con.prepareStatement(desasocioCuentaFacturacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			desasocioCuentaFacturacionStm.setString(1, usuario);
			desasocioCuentaFacturacionStm.setInt(2, codigoCuenta);
			return desasocioCuentaFacturacionStm.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error desasociando la cuenta : "+e);
			return -1;
		}
	}
	
	/**
	 * Mï¿½todo implementado para consultar el listado de cuentas
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param cuentaInicial
	 * @param cuentaFinal
	 * @param estadoCuenta
	 * @param usuario
	 * @param viaIngreso
	 * @param convenio
	 * @param centroAtencion 
	 * @param String codigoEntidadSubContratada
	 * @return
	 */
	public static HashMap consultarCuentas(Connection con,String fechaInicial,String fechaFinal,int cuentaInicial,int cuentaFinal,int estadoCuenta,String usuario,int viaIngreso, String tipoPaciente,int convenio, int centroAtencion,String codigoTipoEvento, String codigoEntidadSubContratada)
	{
		try
		{
			String consulta = consultarCuentasStr;
			
			//se asignan los parï¿½metros de busqueda requeridos
			consulta += " WHERE (c.fecha_apertura BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"' ) ";
			
			//se verifica el estado de la cuenta
			if(estadoCuenta>=0)
				consulta+= " AND c.estado_cuenta= "+estadoCuenta;
			
			//se verifica rango de cuentas
			if(cuentaInicial>0&&cuentaFinal>0)
				consulta += " AND (c.id BETWEEN "+cuentaInicial+" AND "+cuentaFinal+") ";
			
			//se verifica via de ingreso
			if(viaIngreso>0)
				consulta += " AND c.via_ingreso = "+viaIngreso;
			
			//se verifica convenio
			if(convenio>0)
				consulta += " AND getExisteConvenioIngreso(c.id_ingreso,"+convenio+") = '"+ConstantesBD.acronimoSi+"' ";
			
			//se verifica usuario
			if(!usuario.equals(""))
				consulta += " AND c.usuario_modifica = '"+usuario+"'";
			
			//se verifica tipo evento
			if(!codigoTipoEvento.equals(""))
				consulta += " AND c.tipo_evento = '"+codigoTipoEvento+"'";
			
			//se verifica Centro Atenciï¿½n
			if(centroAtencion != 0)
				consulta += " AND cc.centro_atencion = "+centroAtencion;
			
			//se verifica Codigo Entidad Subcontratada
			if(!codigoEntidadSubContratada.equals(""))
				consulta += "AND getCodigoEntidadSubXingreso(c.id_ingreso) = '"+codigoEntidadSubContratada+"' ";
		
//			se verifica si hay tipo paciente
			logger.info("TIPO PACIENTE >>> "+tipoPaciente);
			if(!tipoPaciente.equals("-1"))
				consulta += " AND tipo_paciente = '"+tipoPaciente+"' ";
		
			
			consulta += " ORDER BY c.id";
			

			logger.info("Consulta >>> "+consulta);
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );		
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)),true,false);
	        st.close();
			return mapaRetorno;

			
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarCuentas de SqlBaseCuentaDao: "+e);
			return null;
		}
	}
	
	/**
	 * Mï¿½todo implementado para consultar las cuentas del paciente
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static HashMap consultarCuentas(Connection con,int codigoPaciente)
	{
		try
		{
			String consulta = consultarCuentasStr;
			consulta += " WHERE c.codigo_paciente = "+codigoPaciente+" ORDER BY c.id DESC ";
			
			logger.info("************consultarCuenta-->"+consulta);
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)),true,false);
	        st.close();
			return mapaRetorno;

			
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarCuentas (2) de SqlBaseCuentaDao: "+e);
			return null;
		}
	}
	
	
	
	
	/**
	 * Mï¿½todo implementado para guardar la informaciï¿½n de la cuenta
	 * @param con
	 * @param secuenciaPoliza 
	 * @param secuenciaResponsable 
	 * @return
	 */
	public static ResultadoBoolean guardar(Connection con,DtoCuentas cuenta,String secuenciaCuenta, String secuenciaSubCuenta, String secuenciaPoliza, String secuenciaResponsable, int tipoBD)
	{
		ResultadoBoolean respuesta = new ResultadoBoolean(false,"Error ingresando informaciï¿½n de la cuenta");
		
		int resp0 = 0, resp1 = 0, resp2 = 0, resp3 = 0, resp4 = 0, resp5 = 0;
		
		
		//**************INSERCION/MODIFICACION DE LA CUENTA***********************************************
		resp0 = insercionModificacionCuenta(con,cuenta,secuenciaCuenta);
		
		//************INSERCIï¿½N/MODIFICACION DE LOS CONVENIOS*******************************************
		if(resp0>0)
		{
			//Si no hay convenios para ingresar/modificar se continua
			if(cuenta.getConvenios().length==0)
			{
				resp1 = 1;
				resp2 = 1;
			}
			//de lo contrario se realizan transacciones para convenios
			else
			{
			
				for(int i=0;i<cuenta.getConvenios().length;i++)
				{
					
					//*******************************TABLA SUB_CUENTAS***************************************************
					resp1 = insercionModificacionSubCuenta(con,cuenta,secuenciaSubCuenta,i);
					
					//********************************************************************************************************
					
					//SE verifica si se debe modificar/insertar la informaciï¿½n de poliza
					if(resp1<=0)
						i = cuenta.getConvenios().length;
					else
					{
						resp2 = 1;
						
						//****************INSERCION DE LOS DATOS DE LA POLIZA******************************************************
/*						resp2 = insercionModificacionEliminacionPoliza(con,cuenta,secuenciaPoliza,i);
						
						if(resp2<=0)
							i = cuenta.getConvenios().length;
*/
						//***************************************************************************************************
					}
				}
			}
			
			if(resp1<=0)
				respuesta = new ResultadoBoolean(false,"Problemas ingresando informaciï¿½n de los convenios de la cuenta. Proceso Cancelado");
			else if(resp2<=0)
				respuesta = new ResultadoBoolean(false,"Problemas ingresando informaciï¿½n de la poliza . Proceso Cancelado");
			
		}
		else
			respuesta = new ResultadoBoolean(false,"Problemas ingresando informaciï¿½n de la cuenta. Proceso Cancelado");
		
		//********************INSERCION DE LOS REQUISITOS DEL PACIENTE*******************************************
		if(resp1 > 0 && resp2 > 0)
		{
			//Si no hay convenios para ingresar/modificar se continua
			if(cuenta.getConvenios().length==0)
			{
				resp3 = 1;
			}
			//de lo contrario se realizan transacciones para convenios
			else
			{
				for(int i=0;i<cuenta.getConvenios().length;i++)
				{
					resp3 = insertarRequisitosPaciente(con,cuenta,i);
					
					if(resp3 <=0)
						i = cuenta.getConvenios().length;
					
				}
			}
			
			if(resp3 <=0)
				respuesta = new ResultadoBoolean(false,"Problemas ingresando los requisitos del paciente de la cuenta. Proceso Cancelado");
		}
		//*******************************************************************************************************
		
		//********************INSERCION / MODIFICACION VERIFICACION DE DERECHOS**********************************+
		if(resp3 > 0)
		{
			//Si no hay convenios para ingresar/modificar se continua
			if(cuenta.getConvenios().length==0)
			{
				resp4 = 1;
			}
			//de lo contrario se realizan transacciones para convenios
			else
			{
				for(int i=0;i<cuenta.getConvenios().length;i++)
				{
					resp4 = insertarModificarVerificacionDerechos(con,cuenta,i, tipoBD);
					
					if(resp4 <= 0)
						i = cuenta.getConvenios().length;
				}
			}
			
			if(resp4<=0)
				respuesta = new ResultadoBoolean(false,"Problemas ingresando la verificacion de derechos de la cuenta. Proceso Cancelado");
		}
		//********************************************************************************************************
		
		//********************INSERCION/MODIFICACION RESPONSABLE PACIENTE*****************************************
		if(resp4 > 0)
		{
			resp5 = insertarModificarEliminarResponsablePaciente(con,cuenta,secuenciaResponsable, tipoBD);
			
		}
		//********************************************************************************************************
		
		logger.info("resp0: "+resp0+", resp1: "+resp1+", resp2=> "+resp2+", resp3=> "+resp3+", resp4 => "+resp4+", resp5=> "+resp5);
		//*******VALIDACIONES DE LA TRANSACCION*********************************
		if(resp0 >0 && resp1 > 0 && resp2 > 0 && resp3 > 0 && resp4 > 0 && resp5 > 0)
		{
			
			//Se asigna como respuesta el id de la cuenta
			respuesta.setDescripcion(cuenta.getIdCuenta());
			respuesta.setResultado(true);
		}
		
		return respuesta;
		
	}
	
	/**
	 * Mï¿½todo que inserta/modifica/elimina responsable paciente
	 * @param con
	 * @param cuenta
	 * @param secuenciaResponsable
	 * @param tipoBD 
	 * @return
	 */
	private static int insertarModificarEliminarResponsablePaciente(Connection con, DtoCuentas cuenta, String secuenciaResponsable, int tipoBD) 
	{
		String consulta = "";
		int resp5 = 0;
		PreparedStatement ps = null;
		PreparedStatement pst2 = null;
		PreparedStatement pst3 = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		
		try
		{
			//Se pregunta 
			if(cuenta.isTieneResponsablePaciente())
			{
				if(cuenta.getResponsablePaciente().getCodigo() == null || cuenta.getResponsablePaciente().getCodigo().equals(""))
				{
					//INSERCION
					switch(tipoBD)
					{
					case DaoFactory.ORACLE:
						consulta = "INSERT INTO responsables_pacientes " +
						"(codigo,numero_identificacion,tipo_identificacion,direccion,telefono,relacion_paciente," +
						"codigo_pais_doc,codigo_ciudad_doc,codigo_depto_doc,primer_apellido,segundo_apellido," +
						"primer_nombre,segundo_nombre,codigo_pais,codigo_ciudad,codigo_depto,codigo_barrio," +
						"fecha_nacimiento,fecha_modifica,hora_modifica,usuario_modifica) " +
						"VALUES " +
						"("+secuenciaResponsable+",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,CURRENT_DATE,("+ValoresPorDefecto.getSentenciaHoraActualBD()+"),?)";
						break;
					case DaoFactory.POSTGRESQL:
						consulta = "INSERT INTO responsables_pacientes " +
						"(codigo,numero_identificacion,tipo_identificacion,direccion,telefono,relacion_paciente," +
						"codigo_pais_doc,codigo_ciudad_doc,codigo_depto_doc,primer_apellido,segundo_apellido," +
						"primer_nombre,segundo_nombre,codigo_pais,codigo_ciudad,codigo_depto,codigo_barrio," +
						"fecha_nacimiento,fecha_modifica,hora_modifica,usuario_modifica) " +
						"VALUES " +
						"("+secuenciaResponsable+",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?)";
						break;
					default:
						break;
					}
					
					ps =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					ps.setString(1,cuenta.getResponsablePaciente().getNumeroIdentificacion());
					ps.setString(2,cuenta.getResponsablePaciente().getTipoIdentificacion());
					
					if(!cuenta.getResponsablePaciente().getDireccion().equals(""))
						ps.setString(3,cuenta.getResponsablePaciente().getDireccion());
					else
						ps.setNull(3,Types.VARCHAR);
					
					ps.setString(4,cuenta.getResponsablePaciente().getTelefono());
					ps.setString(5,cuenta.getResponsablePaciente().getRelacionPaciente());
					
					if(!cuenta.getResponsablePaciente().getCodigoPaisExpedicion().equals("")&&
						!cuenta.getResponsablePaciente().getCodigoDeptoExpedicion().equals("")&&
						!cuenta.getResponsablePaciente().getCodigoCiudadExpedicion().equals(""))
					{
						ps.setString(6,cuenta.getResponsablePaciente().getCodigoPaisExpedicion());
						ps.setString(7,cuenta.getResponsablePaciente().getCodigoCiudadExpedicion());
						ps.setString(8,cuenta.getResponsablePaciente().getCodigoDeptoExpedicion());
					}
					else
					{
						ps.setNull(6,Types.VARCHAR);
						ps.setNull(7,Types.VARCHAR);
						ps.setNull(8,Types.VARCHAR);
					}
					
					ps.setString(9,cuenta.getResponsablePaciente().getPrimerApellido());
					
					if(!cuenta.getResponsablePaciente().getSegundoApellido().equals(""))
						ps.setString(10,cuenta.getResponsablePaciente().getSegundoApellido());
					else
						ps.setNull(10,Types.VARCHAR);
					
					ps.setString(11,cuenta.getResponsablePaciente().getPrimerNombre());
					
					if(!cuenta.getResponsablePaciente().getSegundoNombre().equals(""))
						ps.setString(12,cuenta.getResponsablePaciente().getSegundoNombre());
					else
						ps.setNull(12,Types.VARCHAR);
					
					if(!cuenta.getResponsablePaciente().getCodigoPais().equals("")&&
						!cuenta.getResponsablePaciente().getCodigoDepto().equals("")&&
						!cuenta.getResponsablePaciente().getCodigoCiudad().equals(""))
					{
						ps.setString(13,cuenta.getResponsablePaciente().getCodigoPais());
						ps.setString(14,cuenta.getResponsablePaciente().getCodigoCiudad());
						ps.setString(15,cuenta.getResponsablePaciente().getCodigoDepto());
					}
					else
					{
						ps.setNull(13,Types.VARCHAR);
						ps.setNull(14,Types.VARCHAR);
						ps.setNull(15,Types.VARCHAR);
					}
					
					if(!cuenta.getResponsablePaciente().getCodigoBarrio().equals(""))
						ps.setInt(16,Integer.parseInt(cuenta.getResponsablePaciente().getCodigoBarrio()));
					else
						ps.setNull(16,Types.INTEGER);
					
					ps.setDate(17,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(cuenta.getResponsablePaciente().getFechaNacimiento())));
					ps.setString(18,cuenta.getResponsablePaciente().getLoginUsuario());
					logger.info("voy a insertar mi responsable-------------------------<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
					resp5 = ps.executeUpdate();
					logger.info("inserte mi responsable--------------->>>>>>>>>>>>>>>>>>>>");
					//Se consulta el consecutivo del responsable recien insertado
					consulta = "SELECT max(codigo) As codigo FROM responsables_pacientes WHERE tipo_identificacion = ? AND numero_identificacion = ?";
					pst2 =  con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet);
					pst2.setString(1,cuenta.getResponsablePaciente().getTipoIdentificacion());
					pst2.setString(2,cuenta.getResponsablePaciente().getNumeroIdentificacion());
					
					
					rs2 =pst2.executeQuery();
					if(rs2.next())
						cuenta.getResponsablePaciente().setCodigo(rs2.getString("codigo"));
					
					//Se actualiza el responsable paciente en la cuenta
					if(resp5>0)
					{
						consulta = "UPDATE cuentas SET codigo_responsable_paciente = ? WHERE id = ?";
						pst3 = con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet);
						pst3.setInt(1,Utilidades.convertirAEntero(cuenta.getResponsablePaciente().getCodigo()));
						pst3.setInt(2,Utilidades.convertirAEntero(cuenta.getIdCuenta()));
						resp5 = pst3.executeUpdate();
					}
					
				}
				else
				{
					//Se pregunta si se debe eliminar o modificar el responsable
					if(!cuenta.getResponsablePaciente().isEliminar())
					{
						//MODIFICACION
						switch(tipoBD)
						{
						case DaoFactory.ORACLE:
							consulta = "UPDATE responsables_pacientes SET " +
							"direccion = ?, telefono = ?, relacion_paciente = ?, codigo_pais_doc = ?, codigo_ciudad_doc = ?, " +
							"codigo_depto_doc = ?, primer_apellido = ?, segundo_apellido = ?, primer_nombre = ?, segundo_nombre = ?, " +
							"codigo_pais = ?, codigo_ciudad = ?, codigo_depto = ?, codigo_barrio = ?, fecha_nacimiento = ?, " +
							"fecha_modifica = CURRENT_DATE, hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", usuario_modifica = ? " +
							"WHERE codigo = ?";
							break;
						case DaoFactory.POSTGRESQL:
							consulta = "UPDATE responsables_pacientes SET " +
							"direccion = ?, telefono = ?, relacion_paciente = ?, codigo_pais_doc = ?, codigo_ciudad_doc = ?, " +
							"codigo_depto_doc = ?, primer_apellido = ?, segundo_apellido = ?, primer_nombre = ?, segundo_nombre = ?, " +
							"codigo_pais = ?, codigo_ciudad = ?, codigo_depto = ?, codigo_barrio = ?, fecha_nacimiento = ?, " +
							"fecha_modifica = CURRENT_DATE, hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", usuario_modifica = ? " +
							"WHERE codigo = ?";
							break;
						default:
							break;
						}
						
						ps =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
						if(!cuenta.getResponsablePaciente().getDireccion().equals(""))
							ps.setString(1,cuenta.getResponsablePaciente().getDireccion());
						else
							ps.setNull(1,Types.VARCHAR);
						
						ps.setString(2,cuenta.getResponsablePaciente().getTelefono());
						ps.setString(3,cuenta.getResponsablePaciente().getRelacionPaciente());
						
						if(!cuenta.getResponsablePaciente().getCodigoPaisExpedicion().equals("")&&
							!cuenta.getResponsablePaciente().getCodigoDeptoExpedicion().equals("")&&
							!cuenta.getResponsablePaciente().getCodigoCiudadExpedicion().equals(""))
						{
							ps.setString(4,cuenta.getResponsablePaciente().getCodigoPaisExpedicion());
							ps.setString(5,cuenta.getResponsablePaciente().getCodigoCiudadExpedicion());
							ps.setString(6,cuenta.getResponsablePaciente().getCodigoDeptoExpedicion());
						}
						else
						{
							ps.setNull(4,Types.VARCHAR);
							ps.setNull(5,Types.VARCHAR);
							ps.setNull(6,Types.VARCHAR);
						}
						
						ps.setString(7,cuenta.getResponsablePaciente().getPrimerApellido());
						
						if(!cuenta.getResponsablePaciente().getSegundoApellido().equals(""))
							ps.setString(8,cuenta.getResponsablePaciente().getSegundoApellido());
						else
							ps.setNull(8,Types.VARCHAR);
						
						ps.setString(9,cuenta.getResponsablePaciente().getPrimerNombre());
						
						if(!cuenta.getResponsablePaciente().getSegundoNombre().equals(""))
							ps.setString(10,cuenta.getResponsablePaciente().getSegundoNombre());
						else
							ps.setNull(10,Types.VARCHAR);
						
						if(!cuenta.getResponsablePaciente().getCodigoPais().equals("")&&
							!cuenta.getResponsablePaciente().getCodigoDepto().equals("")&&
							!cuenta.getResponsablePaciente().getCodigoCiudad().equals(""))
						{
							ps.setString(11,cuenta.getResponsablePaciente().getCodigoPais());
							ps.setString(12,cuenta.getResponsablePaciente().getCodigoCiudad());
							ps.setString(13,cuenta.getResponsablePaciente().getCodigoDepto());
						}
						else
						{
							ps.setNull(11,Types.VARCHAR);
							ps.setNull(12,Types.VARCHAR);
							ps.setNull(13,Types.VARCHAR);
						}
						
						if(!cuenta.getResponsablePaciente().getCodigoBarrio().equals(""))
							ps.setInt(14,Integer.parseInt(cuenta.getResponsablePaciente().getCodigoBarrio()));
						else
							ps.setNull(14,Types.INTEGER);
						
						ps.setDate(15,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(cuenta.getResponsablePaciente().getFechaNacimiento())));
						ps.setString(16,cuenta.getResponsablePaciente().getLoginUsuario());
						ps.setString(17,cuenta.getResponsablePaciente().getCodigo());
						
						resp5 = ps.executeUpdate();
						
						//Se actualiza el responsable paciente en la cuenta
						if(resp5>0)
						{
							consulta = "UPDATE cuentas SET codigo_responsable_paciente = ? WHERE id = ?";
							pst2 =  con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet);
							pst2.setInt(1,Utilidades.convertirAEntero(cuenta.getResponsablePaciente().getCodigo()));
							pst2.setInt(2,Utilidades.convertirAEntero(cuenta.getIdCuenta()));
							resp5 = pst2.executeUpdate();
						}
					}
					else
					{
						//ELIMINAR
						//Se quita el responsable de la tabla cuentas
						consulta = "UPDATE cuentas SET codigo_responsable_paciente = null WHERE id = ?";
						pst3 =  con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet);
						pst3.setInt(1,Utilidades.convertirAEntero(cuenta.getIdCuenta()));
						resp5 = pst3.executeUpdate();
					}
					
				}
				
				
				
			}
			else
				resp5 = 1;
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerCondicionesTomaXServicio", e);
		}
		finally{
			try{
				if(rs3 != null){
					rs3.close();
	}
				if(pst3 != null){
					pst3.close();
				}
				if(rs2 != null){
					rs2.close();
				}
				if(pst2 != null){
					pst2.close();
				}
				if(ps != null){
					ps.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return resp5;
	}

	/**
	 * Mï¿½todo que realiza la insercion/modificacion de la verificaciï¿½n de derechos
	 * @param con
	 * @param cuenta
	 * @param i
	 * @return
	 */
	private static int insertarModificarVerificacionDerechos(Connection con, DtoCuentas cuenta, int i, int tipoBD) 
	{
		int resp4 = 0;
		String consulta = "";
		PreparedStatement ps = null;
		try
		{
			if(cuenta.getConvenios()[i].isSubCuentaVerificacionDerechos())
			{
				if(cuenta.getConvenios()[i].getVerificacionDerechos().getSubCuenta().equals(""))
				{
					//INSERCIï¿½N
					
					switch(tipoBD)
					{
					case DaoFactory.ORACLE:
						consulta = "INSERT INTO verificaciones_derechos " +
								"(sub_cuenta,estado,tipo_verificacion,numero_verificacion,persona_solicita,fecha_solicitud,hora_solicitud," +
								"persona_contactada,fecha_verificacion,hora_verificacion,porcentaje_cobertura,cuota_verificacion,observaciones," +
								"usuario_modifica,fecha_modifica,hora_modifica,ingreso,convenio) " +
								"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?)";
						break;
					case DaoFactory.POSTGRESQL:
						consulta = "INSERT INTO verificaciones_derechos " +
								"(sub_cuenta,estado,tipo_verificacion,numero_verificacion,persona_solicita,fecha_solicitud,hora_solicitud," +
								"persona_contactada,fecha_verificacion,hora_verificacion,porcentaje_cobertura,cuota_verificacion,observaciones," +
								"usuario_modifica,fecha_modifica,hora_modifica,ingreso,convenio) " +
								"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?)";
						break;
					
					default:
						break;
					}
					
					ps =  con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet);
					ps.setInt(1,Utilidades.convertirAEntero(cuenta.getConvenios()[i].getSubCuenta()));
					ps.setString(2,cuenta.getConvenios()[i].getVerificacionDerechos().getCodigoEstado());
					ps.setString(3,cuenta.getConvenios()[i].getVerificacionDerechos().getCodigoTipo());
					
					if(!cuenta.getConvenios()[i].getVerificacionDerechos().getNumero().equals(""))
						ps.setString(4,cuenta.getConvenios()[i].getVerificacionDerechos().getNumero());
					else
						ps.setNull(4,Types.VARCHAR);
					
					ps.setString(5,cuenta.getConvenios()[i].getVerificacionDerechos().getPersonaSolicita());
					ps.setDate(6,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(cuenta.getConvenios()[i].getVerificacionDerechos().getFechaSolicitud())));
					ps.setString(7,cuenta.getConvenios()[i].getVerificacionDerechos().getHoraSolicitud());
					
					if(!cuenta.getConvenios()[i].getVerificacionDerechos().getPersonaContactada().equals(""))
						ps.setString(8,cuenta.getConvenios()[i].getVerificacionDerechos().getPersonaContactada());
					else
						ps.setNull(8,Types.VARCHAR);
					
					ps.setDate(9,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(cuenta.getConvenios()[i].getVerificacionDerechos().getFechaVerificacion())));
					ps.setString(10,cuenta.getConvenios()[i].getVerificacionDerechos().getHoraVerificacion());
					
					if(!cuenta.getConvenios()[i].getVerificacionDerechos().getPorcentajeCobertura().equals(""))
						ps.setDouble(11,Utilidades.convertirADouble(cuenta.getConvenios()[i].getVerificacionDerechos().getPorcentajeCobertura()));
					else
						ps.setNull(11,Types.NUMERIC);
					
					if(!cuenta.getConvenios()[i].getVerificacionDerechos().getCuotaVerificacion().equals(""))
						ps.setDouble(12,Utilidades.convertirADouble(cuenta.getConvenios()[i].getVerificacionDerechos().getCuotaVerificacion()));
					else
						ps.setNull(12,Types.NUMERIC);
					
					if(!cuenta.getConvenios()[i].getVerificacionDerechos().getObservaciones().equals(""))
						ps.setString(13,cuenta.getConvenios()[i].getVerificacionDerechos().getObservaciones());
					else
						ps.setNull(13,Types.VARCHAR);
					
					ps.setString(14, cuenta.getConvenios()[i].getVerificacionDerechos().getLoginUsuario());
					
					ps.setInt(15, cuenta.getConvenios()[i].getIngreso());
					ps.setInt(16, cuenta.getConvenios()[i].getConvenio().getCodigo());

					resp4 = ps.executeUpdate();

					
					
				}
				else
				{
					if(!cuenta.getConvenios()[i].getVerificacionDerechos().isEliminar())
					{
						//MODIFICACION
						switch(tipoBD)
						{
						case DaoFactory.ORACLE:
							consulta = "UPDATE verificaciones_derechos SET " +
							"estado = ?, tipo_verificacion = ?, numero_verificacion = ?, persona_solicita = ?, " +
							"fecha_solicitud = ?, hora_solicitud = ?, persona_contactada = ?, fecha_verificacion = ?, " +
							"hora_verificacion = ?, porcentaje_cobertura = ?, cuota_verificacion = ?, observaciones = ?, " +
							"usuario_modifica = ?, fecha_modifica = CURRENT_DATE, hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", convenio = ? "+ 
							"WHERE sub_cuenta = ? "; 
							break;
						case DaoFactory.POSTGRESQL:
							consulta = "UPDATE verificaciones_derechos SET " +
							"estado = ?, tipo_verificacion = ?, numero_verificacion = ?, persona_solicita = ?, " +
							"fecha_solicitud = ?, hora_solicitud = ?, persona_contactada = ?, fecha_verificacion = ?, " +
							"hora_verificacion = ?, porcentaje_cobertura = ?, cuota_verificacion = ?, observaciones = ?, " +
							"usuario_modifica = ?, fecha_modifica = CURRENT_DATE, hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", convenio = ? "+ 
							"WHERE sub_cuenta = ? "; 
							break;
						default:
							break;
						}
						
						
						ps =  con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet);
						
						ps.setString(1,cuenta.getConvenios()[i].getVerificacionDerechos().getCodigoEstado());
						ps.setString(2,cuenta.getConvenios()[i].getVerificacionDerechos().getCodigoTipo());
						
						if(!cuenta.getConvenios()[i].getVerificacionDerechos().getNumero().equals(""))
							ps.setString(3,cuenta.getConvenios()[i].getVerificacionDerechos().getNumero());
						else
							ps.setNull(3,Types.VARCHAR);
						
						ps.setString(4,cuenta.getConvenios()[i].getVerificacionDerechos().getPersonaSolicita());
						ps.setDate(5,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(cuenta.getConvenios()[i].getVerificacionDerechos().getFechaSolicitud())));
						ps.setString(6,cuenta.getConvenios()[i].getVerificacionDerechos().getHoraSolicitud());
						
						if(!cuenta.getConvenios()[i].getVerificacionDerechos().getPersonaContactada().equals(""))
							ps.setString(7,cuenta.getConvenios()[i].getVerificacionDerechos().getPersonaContactada());
						else
							ps.setNull(7,Types.VARCHAR);
						
						ps.setDate(8,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(cuenta.getConvenios()[i].getVerificacionDerechos().getFechaVerificacion())));
						ps.setString(9,cuenta.getConvenios()[i].getVerificacionDerechos().getHoraVerificacion());
						
						if(!cuenta.getConvenios()[i].getVerificacionDerechos().getPorcentajeCobertura().equals(""))
							ps.setDouble(10,Utilidades.convertirADouble(cuenta.getConvenios()[i].getVerificacionDerechos().getPorcentajeCobertura()));
						else
							ps.setNull(10,Types.NUMERIC);
						
						if(!cuenta.getConvenios()[i].getVerificacionDerechos().getCuotaVerificacion().equals(""))
							ps.setDouble(11,Utilidades.convertirADouble(cuenta.getConvenios()[i].getVerificacionDerechos().getCuotaVerificacion()));
						else
							ps.setNull(11,Types.NUMERIC);
						
						if(!cuenta.getConvenios()[i].getVerificacionDerechos().getObservaciones().equals(""))
							ps.setString(12,cuenta.getConvenios()[i].getVerificacionDerechos().getObservaciones());
						else
							ps.setNull(12,Types.VARCHAR);
						
						ps.setString(13, cuenta.getConvenios()[i].getVerificacionDerechos().getLoginUsuario());
						
						ps.setInt(14, cuenta.getConvenios()[i].getConvenio().getCodigo());
						ps.setString(15, cuenta.getConvenios()[i].getVerificacionDerechos().getSubCuenta());
						
						
						resp4 = ps.executeUpdate();
					}
					else
					{
						//ELIMINAR
						consulta = "DELETE FROM verificaciones_derechos WHERE ingreso = ? AND convenio = ?";
						
						ps =  con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet);
						
						ps.setInt(1, cuenta.getConvenios()[i].getIngreso());
						ps.setInt(2, cuenta.getConvenios()[i].getConvenio().getCodigo());
						
						resp4 = ps.executeUpdate();
					}
					
				}
			}
			else
				resp4 = 1;
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerCondicionesTomaXServicio", e);
		}
		finally{
			try{
				if(ps != null){
					ps.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return resp4;
	}

	/**
	 * Mï¿½todo que realiza la inserciï¿½n de los requisitos del paciente
	 * @param con
	 * @param cuenta
	 * @param i
	 * @return
	 */
	private static int insertarRequisitosPaciente(Connection con, DtoCuentas cuenta, int i) 
	{
		int resp3 = 0;
		String consulta = "";
		PreparedStatement pst = null;
		PreparedStatement pst2 = null;
		
		try
		{
			//1) Se eliminan los requisitos de la subCuenta
			consulta = "DELETE FROM requisitos_pac_subcuenta WHERE subcuenta = "+cuenta.getConvenios()[i].getSubCuenta();
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.executeUpdate();
			
			//2) Se inserta cada requisito
			consulta = "INSERT INTO requisitos_pac_subcuenta (requisito_paciente,subcuenta,cumplido) VALUES (?,?,?)";
			
			//Se verifica si sï¿½ se insertaron requisitos
			if(cuenta.getConvenios()[i].getRequisitosPaciente().size()>0)
			{
			
				for(int j=0;j<cuenta.getConvenios()[i].getRequisitosPaciente().size();j++)
				{
					DtoRequsitosPaciente requisitos = (DtoRequsitosPaciente)cuenta.getConvenios()[i].getRequisitosPaciente().get(j);
					pst2 = con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet);
					pst2.setInt(1,requisitos.getCodigo());
					pst2.setInt(2,Utilidades.convertirAEntero(cuenta.getConvenios()[i].getSubCuenta()));
					pst2.setBoolean(3,requisitos.isCumplido());
					resp3 = pst2.executeUpdate();
					
					if(resp3<=0)
						j = cuenta.getConvenios()[i].getRequisitosPaciente().size();
				}
			}
			else
				resp3 = 1;
		}
		catch(Exception e){
			logger.error("############## ERROR insertarRequisitosPaciente", e);
		}
		finally{
			try{
				if(pst2 != null){
					pst2.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		
		return resp3;
	}

	/**
	 * Mï¿½todo que realiza la insercion/modificacion/eliminacion de la poliza
	 * @param con
	 * @param cuenta
	 * @param secuenciaPoliza
	 * @param i
	 * @return
	 */
	private static int insercionModificacionEliminacionPoliza(Connection con, DtoCuentas cuenta, String secuenciaPoliza, int i) 
	{
		int resp2 = 0;
		String consulta = "";
		PreparedStatementDecorator ps = null;
		
		try
		{
			if(cuenta.getConvenios()[i].isSubCuentaPoliza())
			{
				//*********************TABLA TITULAR_POLIZA************************************************************
				if(!cuenta.getConvenios()[i].getTitularPoliza().isExisteBd())
				{
					//INSERCION
					consulta = "INSERT INTO titular_poliza " +
						"(sub_cuenta,nombres_titular,apellidos_titular,tipoid_titular,numeroid_titular,direccion_titular,telefono_titular) VALUES (?,?,?,?,?,?,?)";
					ps =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					ps.setInt(1,Utilidades.convertirAEntero(cuenta.getConvenios()[i].getSubCuenta()));
					ps.setString(2,cuenta.getConvenios()[i].getTitularPoliza().getNombres());
					ps.setString(3,cuenta.getConvenios()[i].getTitularPoliza().getApellidos());
					ps.setString(4,cuenta.getConvenios()[i].getTitularPoliza().getCodigoTipoIdentificacion());
					ps.setString(5,cuenta.getConvenios()[i].getTitularPoliza().getNumeroIdentificacion());
					ps.setString(6,cuenta.getConvenios()[i].getTitularPoliza().getDireccion());
					ps.setString(7,cuenta.getConvenios()[i].getTitularPoliza().getTelefono());
					resp2 = ps.executeUpdate();
					
				}
				else
				{
					//MODIFICAR
					consulta = "UPDATE titular_poliza SET " +
						"nombres_titular = ?, apellidos_titular = ?, tipoid_titular = ?, " +
						"numeroid_titular = ?, direccion_titular = ?, telefono_titular = ? " +
						"WHERE sub_cuenta = ?";
					
					ps =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					ps.setString(1,cuenta.getConvenios()[i].getTitularPoliza().getNombres());
					ps.setString(2,cuenta.getConvenios()[i].getTitularPoliza().getApellidos());
					ps.setString(3,cuenta.getConvenios()[i].getTitularPoliza().getCodigoTipoIdentificacion());
					ps.setString(4,cuenta.getConvenios()[i].getTitularPoliza().getNumeroIdentificacion());
					ps.setString(5,cuenta.getConvenios()[i].getTitularPoliza().getDireccion());
					ps.setString(6,cuenta.getConvenios()[i].getTitularPoliza().getTelefono());
					ps.setInt(7,Utilidades.convertirAEntero(cuenta.getConvenios()[i].getSubCuenta()));
					
					resp2 = ps.executeUpdate();
				}
				//************************************************************************************************************
				
				if(resp2>0)
				{
					
					for(int j=0;j<cuenta.getConvenios()[i].getTitularPoliza().getSizeInformacionPoliza();j++)
					{
						
						//************************TABLA INFORMACION_POLIZA****************************************
						if(!cuenta.getConvenios()[i].getTitularPoliza().getExisteBdInformacionPoliza(j)&&
							!cuenta.getConvenios()[i].getTitularPoliza().getEliminarInformacionPoliza(j))
						{
							//INSERCION
							consulta = "INSERT INTO informacion_poliza " +
								"(codigo,sub_cuenta,fecha_autorizacion,numero_autorizacion,valor_monto_autorizado,fecha_grabacion,hora_grabacion,usuario) " +
								"VALUES " +
								"("+secuenciaPoliza+",?,CURRENT_DATE,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",?)";
							ps =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
							ps.setInt(1, Utilidades.convertirAEntero(cuenta.getConvenios()[i].getSubCuenta()));
							ps.setString(2,cuenta.getConvenios()[i].getTitularPoliza().getAutorizacionInformacionPoliza(j));
							ps.setDouble(3,Utilidades.convertirADouble(cuenta.getConvenios()[i].getTitularPoliza().getValorInformacionPoliza(j)));
							ps.setString(4,cuenta.getConvenios()[i].getTitularPoliza().getUsuarioInformacionPoliza(j));
							resp2 = ps.executeUpdate();
						}
						else if(cuenta.getConvenios()[i].getTitularPoliza().getExisteBdInformacionPoliza(j))
						{
							//Se verifica si se debe eliminar o modificar
							if(!cuenta.getConvenios()[i].getTitularPoliza().getEliminarInformacionPoliza(j))
							{
								//MODIFICACION
								consulta = "UPDATE informacion_poliza SET " +
									"fecha_autorizacion = ?, numero_autorizacion = ?, valor_monto_autorizado = ?, " +
									"fecha_grabacion = CURRENT_DATE, hora_grabacion = "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+", usuario = ? " +
									"WHERE codigo = ?";
								ps =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
								
								ps.setDate(1, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(cuenta.getConvenios()[i].getTitularPoliza().getFechaInformacionPoliza(j))));
								ps.setString(2,cuenta.getConvenios()[i].getTitularPoliza().getAutorizacionInformacionPoliza(j));
								ps.setDouble(3,Utilidades.convertirADouble(cuenta.getConvenios()[i].getTitularPoliza().getValorInformacionPoliza(j)));
								ps.setString(4,cuenta.getConvenios()[i].getTitularPoliza().getUsuarioInformacionPoliza(j));
								ps.setInt(5,Utilidades.convertirAEntero(cuenta.getConvenios()[i].getTitularPoliza().getCodigoInformacionPoliza(j)));
								resp2 = ps.executeUpdate();
								
							}
							else
							{
								//ELIMINACION
								consulta = "DELETE FROM informacion_poliza WHERE codigo = ?";
								ps =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
								
								ps.setInt(1,Utilidades.convertirAEntero(cuenta.getConvenios()[i].getTitularPoliza().getCodigoInformacionPoliza(j)));
								resp2 = ps.executeUpdate();
							}
						}
						//*************************************************************************************
						
						if(resp2<=0)
							j = cuenta.getConvenios()[i].getTitularPoliza().getSizeInformacionPoliza();
					}
					
					
				}
				
				
			}
			else
				resp2 = 1;
		}
		catch (SQLException e) 
		{
			logger.error("Error en insercionModificacionEliminacionPoliza: "+e);
			resp2 = 0;
		}

		return resp2;
	}

	/**
	 * Mï¿½todo implementado para realizae la insercion/modificacion de la sub_cuenta
	 * @param con
	 * @param cuenta
	 * @param secuenciaSubCuenta
	 * @param i
	 * @return
	 */
	private static int insercionModificacionSubCuenta(Connection con, DtoCuentas cuenta, String secuenciaSubCuenta, int i) 
	{
		String consulta = "";
		int resp1 = 0;
		PreparedStatement pst = null;
		PreparedStatement pst2 = null;
		ResultSet rs2 = null;
		
		try
		{
			//Se verifica si el convenio se debe modificar o ingresar
			if(cuenta.getConvenios()[i].getSubCuenta().equals(""))
			{
				//INSERCION
				consulta = "INSERT INTO sub_cuentas " +
					"(sub_cuenta,convenio,naturaleza_paciente,monto_cobro,nro_poliza," +
					"nro_carnet,contrato,ingreso,tipo_afiliado,clasificacion_socioeconomica," +
					"nro_autorizacion,fecha_afiliacion,semanas_cotizacion,codigo_paciente,valor_utilizado_soat," +
					"nro_prioridad,facturado,fecha_modifica,hora_modifica,usuario_modifica," +
					"numero_solicitud_volante,empresas_institucion,meses_cotizacion,tipo_cobertura,tipo_cobro_paciente," +
					"tipo_monto_cobro,porcentaje_monto_cobro) " +
					"VALUES " +
					"("+secuenciaSubCuenta+",?,?,?,?," +
						"?,?,?,?,?," +
						"?,?,?,?,?," +
						"?,?,CURRENT_DATE,'"+UtilidadFecha.getHoraActual()+"',?," +
						"?,?,?,?,?,?,?)";
				
				pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				pst.setInt(1, cuenta.getConvenios()[i].getConvenio().getCodigo());
				if(cuenta.getConvenios()[i].getNaturalezaPaciente()>0){
					pst.setInt(2, cuenta.getConvenios()[i].getNaturalezaPaciente());
				}
				else{
					pst.setObject(2, null);
				}
				if(cuenta.getConvenios()[i].getMontoCobro()>0){
					pst.setInt(3, cuenta.getConvenios()[i].getMontoCobro());
				}
				else{ 
					pst.setObject(3,null);
				}
				pst.setString(4, cuenta.getConvenios()[i].getNroPoliza());
				pst.setString(5, cuenta.getConvenios()[i].getNroCarnet());
				pst.setInt(6, cuenta.getConvenios()[i].getContrato());
				pst.setInt(7, cuenta.getConvenios()[i].getIngreso());
				if(!UtilidadTexto.isEmpty(cuenta.getConvenios()[i].getTipoAfiliado()))
					pst.setString(8, cuenta.getConvenios()[i].getTipoAfiliado());
				else
					pst.setObject(8, null);
				if(cuenta.getConvenios()[i].getClasificacionSocioEconomica()>0)
					pst.setInt(9, cuenta.getConvenios()[i].getClasificacionSocioEconomica());
				else
					pst.setObject(9,null);
				
				pst.setString(10, cuenta.getConvenios()[i].getNroAutorizacion());
				
				if(!cuenta.getConvenios()[i].getFechaAfiliacion().equals(""))
					pst.setDate(11, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(cuenta.getConvenios()[i].getFechaAfiliacion())));
				else
					pst.setNull(11, Types.DATE);
				
				if(!cuenta.getConvenios()[i].getFechaAfiliacion().equals("")||cuenta.getConvenios()[i].getSemanasCotizacion()>0)
					pst.setInt(12, cuenta.getConvenios()[i].getSemanasCotizacion());
				else
					pst.setNull(12,Types.INTEGER);
				
				pst.setInt(13,cuenta.getConvenios()[i].getCodigoPaciente());
				
				if(!cuenta.getConvenios()[i].getValorUtilizadoSoat().equals(""))
					pst.setDouble(14,Utilidades.convertirADouble(cuenta.getConvenios()[i].getValorUtilizadoSoat()));
				else
					pst.setNull(14, Types.NUMERIC);
				pst.setInt(15,cuenta.getConvenios()[i].getNroPrioridad());
				pst.setString(16,cuenta.getConvenios()[i].getFacturado());
				pst.setString(17,cuenta.getConvenios()[i].getLoginUsuario());
				
				if(!cuenta.getConvenios()[i].getNumeroSolicitudVolante().equals(""))
					pst.setInt(18,Utilidades.convertirAEntero(cuenta.getConvenios()[i].getNumeroSolicitudVolante()));
				else
					pst.setNull(18, Types.NUMERIC);
								
				//Empresas Institucion
				logger.info("valor de empresa institucion: "+cuenta.getConvenios()[i].getEmpresasInstitucion());
				if(!cuenta.getConvenios()[i].getEmpresasInstitucion().equals("") && 
						!cuenta.getConvenios()[i].getEmpresasInstitucion().equals(ConstantesBD.codigoNuncaValido+""))
					pst.setInt(19,Utilidades.convertirAEntero(cuenta.getConvenios()[i].getEmpresasInstitucion()));
				else
					pst.setNull(19, Types.NUMERIC);		
				
				if(cuenta.getConvenios()[i].getMesesCotizacion()>0)
					pst.setInt(20, cuenta.getConvenios()[i].getMesesCotizacion());
				else
					pst.setNull(20,Types.INTEGER);
				if(cuenta.getConvenios()[i].getCodigoTipoCobertura()>0)
					pst.setInt(21,cuenta.getConvenios()[i].getCodigoTipoCobertura());
				else
					pst.setNull(21,Types.INTEGER);
				
				
				if(!cuenta.getConvenios()[i].getTipoCobroPaciente().isEmpty())
					pst.setString(22,cuenta.getConvenios()[i].getTipoCobroPaciente());
				else
					pst.setObject(22,null);
				
				if(!UtilidadTexto.isEmpty(cuenta.getConvenios()[i].getTipoMontoCobro()))
					pst.setString(23,cuenta.getConvenios()[i].getTipoMontoCobro());
				else
					pst.setObject(23,null);
				
				if(cuenta.getConvenios()[i].getPorcentajeMontoCobro()>=0)
					pst.setDouble(24,cuenta.getConvenios()[i].getPorcentajeMontoCobro());
				else
					pst.setNull(24,Types.NUMERIC);
				
  				resp1 = pst.executeUpdate();
				
				if(resp1 > 0)
				{
					//Se consulta el consecutivo de la sub cuenta
					consulta = "SELECT max(sub_cuenta) AS sub_cuenta FROM sub_cuentas WHERE codigo_paciente = ?";
					pst2 =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					pst2.setInt(1,cuenta.getConvenios()[i].getCodigoPaciente());
					rs2 =pst2.executeQuery();
					
					if(rs2.next())
						cuenta.getConvenios()[i].setSubCuenta(rs2.getString("sub_cuenta"));
				}
			}
			else
			{
				///MODIFICACION
				consulta = "UPDATE sub_cuentas SET " +
					"convenio = ?, naturaleza_paciente = ?, monto_cobro = ?, nro_poliza = ?, nro_carnet = ?, " +
					"contrato = ?, tipo_afiliado = ?, clasificacion_socioeconomica = ?, nro_autorizacion = ?, fecha_afiliacion = ?, " +
					"semanas_cotizacion = ?, valor_utilizado_soat = ?, " +
					"fecha_modifica = CURRENT_DATE, hora_modifica = '"+UtilidadFecha.getHoraActual()+"', usuario_modifica = ?, numero_solicitud_volante = ?, " +
					"empresas_institucion = ?,meses_cotizacion = ?, tipo_cobertura = ?,tipo_cobro_paciente=?," +
					"tipo_monto_cobro=?,porcentaje_monto_cobro=? " +
					"WHERE sub_cuenta = ?";
				
				pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				pst.setInt(1, cuenta.getConvenios()[i].getConvenio().getCodigo());
				if(cuenta.getConvenios()[i].getNaturalezaPaciente()>0)
					pst.setInt(2, cuenta.getConvenios()[i].getNaturalezaPaciente());
				else
					pst.setObject(2, null);
				if(cuenta.getConvenios()[i].getMontoCobro()>0)
					pst.setInt(3, cuenta.getConvenios()[i].getMontoCobro());
				else
					pst.setObject(3, null);
				pst.setString(4, cuenta.getConvenios()[i].getNroPoliza());
				pst.setString(5, cuenta.getConvenios()[i].getNroCarnet());
				pst.setInt(6, cuenta.getConvenios()[i].getContrato());
				if(!UtilidadTexto.isEmpty(cuenta.getConvenios()[i].getTipoAfiliado()))
					pst.setString(7, cuenta.getConvenios()[i].getTipoAfiliado());
				else
					pst.setObject(7, null);
				if(cuenta.getConvenios()[i].getClasificacionSocioEconomica()>0)
					pst.setInt(8, cuenta.getConvenios()[i].getClasificacionSocioEconomica());
				else
					pst.setObject(8, null);
					
				pst.setString(9, cuenta.getConvenios()[i].getNroAutorizacion());
				
				if(!cuenta.getConvenios()[i].getFechaAfiliacion().equals(""))
					pst.setDate(10, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(cuenta.getConvenios()[i].getFechaAfiliacion())));
				else
					pst.setNull(10, Types.DATE);
				
				if(!cuenta.getConvenios()[i].getFechaAfiliacion().equals("")||cuenta.getConvenios()[i].getSemanasCotizacion()>0)
					pst.setInt(11, cuenta.getConvenios()[i].getSemanasCotizacion());
				else
					pst.setNull(11,Types.INTEGER);
				
				
				if(!cuenta.getConvenios()[i].getValorUtilizadoSoat().equals(""))
					pst.setDouble(12,Utilidades.convertirADouble(cuenta.getConvenios()[i].getValorUtilizadoSoat()));
				else
					pst.setNull(12,Types.NUMERIC);
				
				pst.setString(13,cuenta.getConvenios()[i].getLoginUsuario());
				
				if(!cuenta.getConvenios()[i].getNumeroSolicitudVolante().equals(""))
					pst.setInt(14,Utilidades.convertirAEntero(cuenta.getConvenios()[i].getNumeroSolicitudVolante()));
				else
					pst.setNull(14, Types.NUMERIC);
				
				//Empresas Institucion
				if(!cuenta.getConvenios()[i].getEmpresasInstitucion().equals("") && 
						!cuenta.getConvenios()[i].getEmpresasInstitucion().equals(ConstantesBD.codigoNuncaValido+""))
					pst.setInt(15,Utilidades.convertirAEntero(cuenta.getConvenios()[i].getEmpresasInstitucion()));
				else
					pst.setNull(15, Types.NUMERIC);
				
				if(cuenta.getConvenios()[i].getMesesCotizacion()>0)
					pst.setInt(16, cuenta.getConvenios()[i].getMesesCotizacion());
				else
					pst.setNull(16,Types.INTEGER);
				
				if(cuenta.getConvenios()[i].getCodigoTipoCobertura()>0)
				{
					pst.setInt(17,cuenta.getConvenios()[i].getCodigoTipoCobertura());
				}
				else
				{
					pst.setNull(17,Types.INTEGER);
				}
				
				if(!cuenta.getConvenios()[i].getTipoCobroPaciente().isEmpty())
					pst.setString(18,cuenta.getConvenios()[i].getTipoCobroPaciente());
				else
					pst.setObject(18,null);
				
				if(!UtilidadTexto.isEmpty(cuenta.getConvenios()[i].getTipoMontoCobro()))
					pst.setString(19,cuenta.getConvenios()[i].getTipoMontoCobro());
				else
					pst.setObject(19,null);
				
				if(cuenta.getConvenios()[i].getPorcentajeMontoCobro()>0)
					pst.setDouble(20,cuenta.getConvenios()[i].getPorcentajeMontoCobro());
				else
					pst.setNull(20,Types.NUMERIC);
				
				pst.setInt(21,Utilidades.convertirAEntero(cuenta.getConvenios()[i].getSubCuenta()));
				
				resp1 = pst.executeUpdate();
				
				
			}
		}
		catch(Exception e){
			logger.error("############## ERROR insercionModificacionSubCuenta", e);
		}
		finally{
			try{
				if(rs2 != null){
					rs2.close();
				}
				if(pst2 != null){
					pst2.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		
		return resp1;
	}

	/**
	 * Mï¿½todo implementado para realizar la insercion/modificacion de la cuenta
	 * @param con
	 * @param cuenta
	 * @param secuenciaCuenta
	 * @return
	 */
	private static int insercionModificacionCuenta(Connection con, DtoCuentas cuenta, String secuenciaCuenta) 
	{
		String consulta = "";
		int resp0 = 0;
		PreparedStatement pst = null;
		PreparedStatement pst2 = null;
		ResultSet rs2 = null;
		try
		{
			if(cuenta.getIdCuenta().equals(""))
			{
				//INSERCION
				consulta = "INSERT INTO cuentas (" +
					"id,codigo_paciente,fecha_apertura,hora_apertura,estado_cuenta," +
					"tipo_paciente,indicativo_acc_transito,id_ingreso,area,tipo_evento," +
					"convenio_arp_afiliado,desplazado,origen_admision,tipo_complejidad,via_ingreso," +
					"codigo_responsable_paciente,fecha_modifica,hora_modifica,usuario_modifica,hospital_dia,observaciones) " +
					"VALUES ("+secuenciaCuenta+",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,CURRENT_DATE,'"+UtilidadFecha.getHoraActual()+"',?,?,?)";
			
				pst = con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet);
				pst.setInt(1,Integer.parseInt(cuenta.getCodigoPaciente()));
				if(cuenta.getFechaApertura().equals(""))
					pst.setDate(2,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
				else
					pst.setDate(2,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(cuenta.getFechaApertura())));
				if(cuenta.getHoraApertura().equals(""))
					pst.setString(3,UtilidadFecha.getHoraActual(con));
				else
					pst.setString(3,cuenta.getHoraApertura());
				pst.setInt(4,cuenta.getCodigoEstado());
				pst.setString(5,cuenta.getCodigoTipoPaciente());
				pst.setBoolean(6, cuenta.getCodigoTipoEvento().equals(ConstantesIntegridadDominio.acronimoAccidenteTransito)?true:false);
				pst.setInt(7,Integer.parseInt(cuenta.getIdIngreso()));
				pst.setInt(8,cuenta.getCodigoArea());
				pst.setString(9,cuenta.getCodigoTipoEvento());
				if(cuenta.getCodigoTipoEvento().equals(ConstantesIntegridadDominio.acronimoAccidenteTrabajo)&&
						cuenta.getCodigoConvenioArpAfiliado()>0)
					pst.setInt(10,cuenta.getCodigoConvenioArpAfiliado());
				else
					pst.setNull(10,Types.INTEGER);
				pst.setString(11, cuenta.isDesplazado()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				pst.setInt(12,cuenta.getCodigoOrigenAdmision());
				if(cuenta.getCodigoTipoComplejidad()>0)
					pst.setInt(13, cuenta.getCodigoTipoComplejidad());
				else
					pst.setNull(13,Types.INTEGER);
				pst.setInt(14,cuenta.getCodigoViaIngreso());
				pst.setNull(15,Types.NUMERIC); //pendiente
				pst.setString(16,cuenta.getLoginUsuario());
				pst.setString(17,cuenta.isHospitalDia()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				if(!cuenta.getObservaciones().trim().equals(""))
				{
					pst.setString(18,cuenta.getObservaciones());
				}
				else
				{
					pst.setNull(18,Types.VARCHAR);
				}
				
				resp0 = pst.executeUpdate();
				if(resp0>0)
				{
					//Se toma el id de la cuenta que se insertï¿½ 
					//Nota* Se realiza modificaciï¿½n con el fin de arreglar la concurrencia de cuentas
					consulta = "select max(id) as codigo from cuentas where codigo_paciente="+cuenta.getCodigoPaciente();
					pst2= con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					rs2=new ResultSetDecorator(pst2.executeQuery());
					if(rs2.next())
						cuenta.setIdCuenta(rs2.getString("codigo"));
				}
			}
			else
			{
				//MODIFICACION
				consulta = "UPDATE cuentas SET " +
					"tipo_paciente = ?, indicativo_acc_transito = ?, area = ?, tipo_evento = ?, convenio_arp_afiliado = ?, " +
					"desplazado = ?, origen_admision = ?, tipo_complejidad = ?, usuario_modifica = ?, fecha_modifica = CURRENT_DATE, " +
					"hora_modifica = '"+UtilidadFecha.getHoraActual()+"', hospital_dia = ?, observaciones = ? WHERE id = ?";
				
				pst = con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet);
				pst.setString(1,cuenta.getCodigoTipoPaciente());
				pst.setBoolean(2, cuenta.getCodigoTipoEvento().equals(ConstantesIntegridadDominio.acronimoAccidenteTransito)?true:false);
				Log4JManager.info("area en sqlbase -->"+cuenta.getCodigoArea());
				pst.setInt(3,cuenta.getCodigoArea());
				pst.setString(4,cuenta.getCodigoTipoEvento());
				
				if(cuenta.getCodigoTipoEvento().equals(ConstantesIntegridadDominio.acronimoAccidenteTrabajo)&&
						cuenta.getCodigoConvenioArpAfiliado()>0)
					pst.setInt(5,cuenta.getCodigoConvenioArpAfiliado());
				else
					pst.setNull(5,Types.INTEGER);
				pst.setString(6, cuenta.isDesplazado()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				
				pst.setInt(7,cuenta.getCodigoOrigenAdmision());
				if(cuenta.getCodigoTipoComplejidad()>0)
					pst.setInt(8, cuenta.getCodigoTipoComplejidad());
				else
					pst.setNull(8,Types.INTEGER);
				pst.setString(9,cuenta.getLoginUsuario());
				pst.setString(10,cuenta.isHospitalDia()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				
				
				if(!cuenta.getObservaciones().equals(""))
				{
					pst.setString(11, cuenta.getObservaciones());
				}
				else
				{
					pst.setNull(11,Types.VARCHAR);
				}
				
				pst.setInt(12, Utilidades.convertirAEntero(cuenta.getIdCuenta()));
				
				resp0 = pst.executeUpdate();
			}
		}
		catch(Exception e){
			logger.error("############## ERROR insercionModificacionCuenta", e);
		}
		finally{
			try{
				if(rs2 != null){
					rs2.close();
				}
				if(pst2 != null){
					pst2.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
		}
		}
		
		return resp0;
	}

	/**
	 * Mï¿½todo que carga la informacion de la cuenta con sus convenios
	 * @param con
	 * @param idCuenta
	 * @return
	 * Nota * Si no se encuentra la cuenta el atributo idCuenta es vacï¿½o
	 */
	public static DtoCuentas cargar(Connection con,String idCuenta)
	{
		DtoCuentas cuenta = new DtoCuentas();
		try
		{
			PreparedStatementDecorator ps = null;
			ResultSetDecorator rs = null, rs0 = null;
			//***************CARGAR LOS DATOS DE LA CUENTA***************************************
			ps =  new PreparedStatementDecorator(con.prepareStatement(cargarCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(idCuenta));
			//logger.info("cargarCuentaStr => "+cargarCuentaStr+"\nidCuenta => "+idCuenta);
			rs =new ResultSetDecorator(ps.executeQuery());
			//logger.info("cargarCuentaStr => "+cargarCuentaStr);
			if(rs.next())
			{
				cuenta.setIdCuenta(rs.getString("id_cuenta"));
				cuenta.setCodigoPaciente(rs.getString("codigo_paciente"));
				cuenta.setDescripcionPaciente(rs.getString("nombre_paciente"));
				cuenta.setFechaApertura(rs.getString("fecha_apertura"));
				cuenta.setHoraApertura(rs.getString("hora_apertura"));
				cuenta.setCodigoEstado(rs.getInt("codigo_estado_cuenta"));
				cuenta.setDescripcionEstado(rs.getString("nombre_estado_cuenta"));
				cuenta.setCodigoTipoPaciente(rs.getString("codigo_tipo_paciente"));
				cuenta.setDescripcionTipoPaciente(rs.getString("nombre_tipo_paciente"));
				cuenta.setIdIngreso(rs.getString("id_ingreso"));
				cuenta.setLoginUsuario(rs.getString("usuario_modifica"));
				cuenta.setCodigoArea(rs.getInt("codigo_area"));
				cuenta.setDescripcionArea(rs.getString("nombre_area"));
				cuenta.setCodigoTipoEvento(rs.getString("codigo_tipo_evento")==null?"":rs.getString("codigo_tipo_evento"));
				cuenta.setDescripcionTipoEvento(ValoresPorDefecto.getIntegridadDominio(cuenta.getCodigoTipoEvento())+"");
				cuenta.setCodigoConvenioArpAfiliado(rs.getInt("codigo_arp_afiliado"));
				cuenta.setDescripcionConvenioArpAfiliado(rs.getString("nombre_arp_afiliado"));
				cuenta.setDesplazado(UtilidadTexto.getBoolean(rs.getString("desplazado")));
				cuenta.setCodigoOrigenAdmision(rs.getInt("codigo_origen_admision"));
				cuenta.setDescripcionOrigenAdmision(rs.getString("nombre_origen_admision"));
				cuenta.setCodigoTipoComplejidad(rs.getInt("codigo_tipo_complejidad"));
				cuenta.setDescripcionTipoComplejidad(rs.getString("nombre_tipo_complejidad"));
				cuenta.setCodigoViaIngreso(rs.getInt("codigo_via_ingreso"));
				cuenta.setDescripcionViaIngreso(rs.getString("nombre_via_ingreso"));
				cuenta.getResponsablePaciente().setCodigo(rs.getString("codigo_responsable_paciente"));
				cuenta.setDescEntidadesSubcontratadas(rs.getString("descripcion_entidadsub"));
				cuenta.setConsecutivoIngreso(rs.getString("consecutivoingreso"));
				///////////////////////////////////////////////////////////////////////////////////////
				// adicionado por anexo  transplante
				cuenta.setTransplante(rs.getString("transplante"));
				//////////////////////////////////////////////////////////////////////////////////////
				if(cuenta.getResponsablePaciente().getCodigo() == null ||cuenta.getResponsablePaciente().getCodigo().equals(""))
					cuenta.setTieneResponsablePaciente(false);
				else
					cuenta.setTieneResponsablePaciente(true);
				cuenta.setHospitalDia(UtilidadTexto.getBoolean(rs.getString("hospital_dia")));
				cuenta.setObservaciones(rs.getString("observaciones"));
				
				rs.close();
				ps.close();
				
				//******************CARGAR RESPONSABLE PACIENTE (SI LO TIENE)********************************
				if(cuenta.isTieneResponsablePaciente())
				{
					ps =  new PreparedStatementDecorator(con.prepareStatement(cargarResponsablePacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1,Utilidades.convertirAEntero(cuenta.getResponsablePaciente().getCodigo()));
					rs =new ResultSetDecorator(ps.executeQuery());
					
					if(rs.next())
					{
						cuenta.getResponsablePaciente().setCodigo(rs.getString("codigo"));
						cuenta.getResponsablePaciente().setNumeroIdentificacion(rs.getString("numero_identificacion"));
						cuenta.getResponsablePaciente().setTipoIdentificacion(rs.getString("tipo_identificacion"));
						cuenta.getResponsablePaciente().setDescripcionTipoIdentificacion(rs.getString("nombre_tipo_identificacion"));
						cuenta.getResponsablePaciente().setDireccion(rs.getString("direccion"));
						cuenta.getResponsablePaciente().setTelefono(rs.getString("telefono"));
						cuenta.getResponsablePaciente().setRelacionPaciente(rs.getString("relacion_paciente"));
						cuenta.getResponsablePaciente().setCodigoPaisExpedicion(rs.getString("codigo_pais_doc"));
						cuenta.getResponsablePaciente().setDescripcionPaisExpedicion(rs.getString("descripcion_pais_doc"));
						cuenta.getResponsablePaciente().setCodigoCiudadExpedicion(rs.getString("codigo_ciudad_doc"));
						cuenta.getResponsablePaciente().setCodigoDeptoExpedicion(rs.getString("codigo_depto_doc"));
						cuenta.getResponsablePaciente().setDescripcionCiudadExpedicion(rs.getString("descripcion_ciudad_doc"));
						cuenta.getResponsablePaciente().setPrimerApellido(rs.getString("primer_apellido"));
						cuenta.getResponsablePaciente().setSegundoApellido(rs.getString("segundo_apellido"));
						cuenta.getResponsablePaciente().setPrimerNombre(rs.getString("primer_nombre"));
						cuenta.getResponsablePaciente().setSegundoNombre(rs.getString("segundo_nombre"));
						cuenta.getResponsablePaciente().setCodigoPais(rs.getString("codigo_pais"));
						cuenta.getResponsablePaciente().setDescripcionPais(rs.getString("descripcion_pais"));
						cuenta.getResponsablePaciente().setCodigoCiudad(rs.getString("codigo_ciudad"));
						cuenta.getResponsablePaciente().setCodigoDepto(rs.getString("codigo_depto"));
						cuenta.getResponsablePaciente().setDescripcionCiudad(rs.getString("descripcion_ciudad"));
						cuenta.getResponsablePaciente().setDescripcionBarrio(rs.getString("descripcion_barrio"));
						cuenta.getResponsablePaciente().setFechaNacimiento(rs.getString("fecha_nacimiento"));
						cuenta.getResponsablePaciente().setLoginUsuario(rs.getString("usuario_modifica"));
					}

					rs.close();
					ps.close();
				}
				//*******************************************************************************************
				
				//********************CARGAR LOS DATOS DE LOS CONVENIOS************************************
				ps =  new PreparedStatementDecorator(con.prepareStatement(cargarSubCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, Utilidades.convertirAEntero(cuenta.getIdIngreso()));
				ps.setInt(2, Utilidades.convertirAEntero(idCuenta));
				
			
				rs =new ResultSetDecorator(ps.executeQuery());	
				ArrayList<DtoSubCuentas> arreglo = new ArrayList<DtoSubCuentas>();
				while(rs.next())
				{
					DtoSubCuentas convenio = new DtoSubCuentas();
					
					convenio.setSubCuenta(rs.getString("sub_cuenta"));
					convenio.setConvenio(new InfoDatosInt(rs.getInt("codigo_convenio"),rs.getString("nombre_convenio")));
					convenio.setNaturalezaPaciente(rs.getInt("codigo_naturaleza_paciente"));
					convenio.setDescripcionNaturalezaPaciente(rs.getString("nombre_naturaleza_paciente"));
					convenio.setMontoCobro(rs.getInt("codigo_monto_cobro"));
					convenio.setCodigoRegistroAccidenteTransito(rs.getLong("codigo_accidente_transito"));
					if(rs.getString("tipocobropaciente").equals(ConstantesIntegridadDominio.acronimoTipoPacienteManejaMontos))
					{
						if(rs.getString("tipodetalle").equals(ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroGEN))
						{
							String consulta=" SELECT tm.nombre||case when tm.codigo=1 then ' '||dmg.porcentaje||'%' else ' '||dmg.valor end as nombremonto from detalle_monto dm inner join detalle_monto_general dmg on (dm.detalle_codigo=dmg.detalle_codigo) inner join tipos_monto tm on(dm.tipo_monto_codigo=tm.codigo) where dm.detalle_codigo="+rs.getInt("codigo_monto_cobro");
							PreparedStatementDecorator psTempo=new PreparedStatementDecorator(con, consulta);
							ResultSetDecorator rsTempo=new ResultSetDecorator(psTempo.executeQuery());
							if(rsTempo.next())
								convenio.setDescripcionMontoCobro(rsTempo.getString("nombremonto"));
							rsTempo.close();
							psTempo.close();
						}
						else
						{
							convenio.setDescripcionMontoCobro(rs.getString("nombre_monto_cobro"));
						}
					}
					else
					{
						convenio.setDescripcionMontoCobro("CONVENIO NO MANEJA MONTOS.");
					}
					convenio.setNroPoliza(rs.getString("numero_poliza"));
					convenio.setNroCarnet(rs.getString("numero_carnet"));
					convenio.setContrato(rs.getInt("codigo_contrato"));
					convenio.setNumeroContrato(rs.getString("numero_contrato"));
					convenio.setIngreso(rs.getInt("id_ingreso"));
					convenio.setTipoAfiliado(rs.getString("codigo_tipo_afiliado"));
					convenio.setDescripcionTipoAfiliado(rs.getString("nombre_tipo_afiliado"));
					convenio.setClasificacionSocioEconomica(rs.getInt("codigo_estrato_social"));
					convenio.setDescripcionClasificacionSocioEconomica(rs.getString("nombre_estrato_social"));
					if(rs.getInt("numero_autorizacion")<0)
					{
						convenio.setNroAutorizacion("");
					}
					else
					{						  
						convenio.setNroAutorizacion(rs.getInt("numero_autorizacion")+"");
					}
					convenio.setFechaAfiliacion(rs.getString("fecha_afiliacion"));
					convenio.setSemanasCotizacion(rs.getInt("semanas_cotizacion"));
					convenio.setMesesCotizacion(rs.getInt("meses_cotizacion"));
					convenio.setCodigoPaciente(rs.getInt("codigo_paciente"));
					convenio.setValorUtilizadoSoat(rs.getString("valor_utilizado_soat"));
					convenio.setNroPrioridad(rs.getInt("numero_prioridad"));
					convenio.setFacturado(rs.getString("facturado"));
					convenio.setNumeroSolicitudVolante(rs.getString("numero_solicitud_volante"));
					convenio.setEmpresasInstitucion(rs.getString("empresas_institucion"));
					convenio.setCodigoTipoCobertura(rs.getInt("codigo_tipo_cobertura"));
					convenio.setNombreTipoCobertura(rs.getString("nombre_tipo_cobertura"));
					
					//	Campo de NIT o numero de identificacion de tercero, que se carga para la insercion del mismo en la tabla ax_pacien en la interfaz. 				
					convenio.setNit(rs.getString("nit"));					
					
					convenio.setSubCuentaPoliza(UtilidadTexto.getBoolean(rs.getString("aseg_at_ec")));
					//********************SE CARGA LA INFORMACION DE LA POLIZA****************************************************
					//Se verifica si el convenio maneja informacion de poliza
					/*
					if(Utilidades.convenioTieneIngresoInfoAdic(con, convenio.getConvenio().getCodigo()))
					{
						convenio.setSubCuentaPoliza(true);
						ps =  new PreparedStatementDecorator(con.prepareStatement(cargarTitularPolizaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						ps.setInt(1,Utilidades.convertirAEntero(convenio.getSubCuenta()));
						
						rs0 =new ResultSetDecorator(ps.executeQuery());
						if(rs0.next())
						{
							convenio.getTitularPoliza().setSubCuenta(rs0.getString("sub_cuenta"));
							convenio.getTitularPoliza().setNombres(rs0.getString("nombres_titular"));
							convenio.getTitularPoliza().setApellidos(rs0.getString("apellidos_titular"));
							convenio.getTitularPoliza().setCodigoTipoIdentificacion(rs0.getString("codigo_tipo_id"));
							convenio.getTitularPoliza().setDescripcionTipoIdentificacion(rs0.getString("nombre_tipo_id"));
							convenio.getTitularPoliza().setNumeroIdentificacion(rs0.getString("numeroid_titular"));
							convenio.getTitularPoliza().setDireccion(rs0.getString("direccion_titular"));
							convenio.getTitularPoliza().setTelefono(rs0.getString("telefono_titular"));
							convenio.getTitularPoliza().setExisteBd(true);
						}
						
						ps =  new PreparedStatementDecorator(con.prepareStatement(cargarInformacionPolizaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						ps.setInt(1,Utilidades.convertirAEntero(convenio.getSubCuenta()));
						
						rs0 =new ResultSetDecorator(ps.executeQuery());
						
						while(rs0.next())
							convenio.getTitularPoliza().setInformacionPoliza(rs0.getString("codigo"), rs0.getString("fecha_autorizacion"), rs0.getString("numero_autorizacion"), rs0.getString("valor_monto_autorizado"), rs0.getString("usuario"),true,false);
						rs0.close();
					}
					*/
					//*****************************************************************************************************************
					
					//******************SE CARGAN LOS REQUISITOS DEL PACIENTE**********************************************************
					ps =  new PreparedStatementDecorator(con.prepareStatement(cargarRequisitosPacienteStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					ps.setInt(1, Utilidades.convertirAEntero(convenio.getSubCuenta()));
					rs0 =new ResultSetDecorator(ps.executeQuery());
					while(rs0.next())
					{
						DtoRequsitosPaciente requisitos = new DtoRequsitosPaciente();
						requisitos.setCodigo(rs0.getInt("codigo"));
						requisitos.setSubCuenta(rs0.getObject("subcuenta").toString());
						requisitos.setCumplido(UtilidadTexto.getBoolean(rs0.getString("cumplido")));
						requisitos.setTipo(rs0.getString("tipo"));
						requisitos.setDescripcion(rs0.getString("descripcion"));
						convenio.getRequisitosPaciente().add(requisitos);
					}
					rs0.close();
					//*****************************************************************************************************************
					
					//****************SE CARGA LA VERIFICACION DE DERECHOS*************************************************************
					ps =  new PreparedStatementDecorator(con.prepareStatement(cargarVerificacionDerechosStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					ps.setInt(1,convenio.getIngreso());
					ps.setInt(2,convenio.getConvenio().getCodigo());
					rs0 =new ResultSetDecorator(ps.executeQuery());
					if(rs0.next())
					{
						convenio.setSubCuentaVerificacionDerechos(true);
						convenio.getVerificacionDerechos().setSubCuenta(rs0.getString("sub_cuenta"));
						convenio.getVerificacionDerechos().setIdIngreso(rs0.getString("ingreso"));
						convenio.getVerificacionDerechos().setConvenio(rs0.getInt("convenio"));
						convenio.getVerificacionDerechos().setCodigoEstado(rs0.getString("estado"));
						convenio.getVerificacionDerechos().setDescripcionEstado(ValoresPorDefecto.getIntegridadDominio(convenio.getVerificacionDerechos().getCodigoEstado()).toString());
						convenio.getVerificacionDerechos().setCodigoTipo(rs0.getString("tipo_verificacion"));
						convenio.getVerificacionDerechos().setDescripcionTipo(ValoresPorDefecto.getIntegridadDominio(convenio.getVerificacionDerechos().getCodigoTipo()).toString());
						convenio.getVerificacionDerechos().setNumero(rs0.getString("numero_verificacion"));
						convenio.getVerificacionDerechos().setPersonaSolicita(rs0.getString("persona_solicita"));
						convenio.getVerificacionDerechos().setFechaSolicitud(rs0.getString("fecha_solicitud"));
						convenio.getVerificacionDerechos().setHoraSolicitud(rs0.getString("hora_solicitud"));
						convenio.getVerificacionDerechos().setPersonaContactada(rs0.getString("persona_contactada"));
						convenio.getVerificacionDerechos().setFechaVerificacion(rs0.getString("fecha_verificacion"));
						convenio.getVerificacionDerechos().setHoraVerificacion(rs0.getString("hora_verificacion"));
						convenio.getVerificacionDerechos().setPorcentajeCobertura(rs0.getString("porcentaje_cobertura"));
						convenio.getVerificacionDerechos().setCuotaVerificacion(rs0.getString("cuota_verificacion"));
						convenio.getVerificacionDerechos().setObservaciones(rs0.getString("observaciones"));
						convenio.getVerificacionDerechos().setLoginUsuario(rs0.getString("usuario_modifica"));
						
					}
					else
						convenio.setSubCuentaVerificacionDerechos(false);
					rs0.close();
					//*****************************************************************************************************************
					
					arreglo.add(convenio);
				}
				
				//Se cargan los convenios en la estructura de cuentas
				cuenta.setConvenios(new DtoSubCuentas[arreglo.size()]);
				for(int i=0;i<arreglo.size();i++)
					cuenta.getConvenios()[i] = (DtoSubCuentas)arreglo.get(i);
				//*******************************************************************************************
			}
			//******************************************************************************************
		}
		catch(SQLException e)
		{
			logger.error("Error en cargar: ", e);
		}
		return cuenta;
	}

	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static int obtenerCodigoViaIngresoCuenta(Connection con, String idCuenta) throws BDException
	{
		PreparedStatement pst= null;
		ResultSet rs=null;
		
		try	{
			String consulta = "SELECT via_ingreso as via FROM  cuentas WHERE id = ?";
			pst =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setString(1,idCuenta);
			rs = pst.executeQuery();
			if(rs.next()){
				return rs.getInt("via");
			}
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return 0;
	}
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static int obtenerTipoComplejidad (Connection con, String idCuenta)
	{
		String consulta="SELECT tipo_complejidad as tipo from cuentas where id=?";
		try
		{
			int tipoComplejidad = 0;
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,idCuenta);
			ResultSetDecorator rs= new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				tipoComplejidad = rs.getInt("tipo");
			rs.close();
			pst.close();
			return tipoComplejidad;
		}	
		catch(SQLException e)
		{
			logger.error("Error en obtenerTipoComplejidad: "+e);
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static int obtenerCentroAtencionCuenta (Connection con, String idCuenta)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		int centroAtencion = 0;
		try
		{
			String consulta="SELECT cc.centro_atencion as centro FROM centros_costo cc, cuentas c where c.area=cc.codigo AND c.id=?";
			pst =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1,Utilidades.convertirAEntero(idCuenta));
			rs = pst.executeQuery();
			if(rs.next()){
				centroAtencion = rs.getInt("centro");
			}
			return centroAtencion;
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerCentroAtencionCuenta",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerCentroAtencionCuenta", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Mï¿½todo que consulta los datos generales de las cuentas de un asocio
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> listarCuentasAsocio(Connection con,String idIngreso)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		try
		{
			String consulta = "SELECT "+ 
				"id, "+
				"to_char(fecha_apertura,'"+ConstantesBD.formatoFechaAp+"') As fecha_apertura, "+
				"substr(hora_apertura,0,6) AS hora_apertura, "+ 
				"getnombreviaingreso(via_ingreso) As nombre_via_ingreso, "+
				"getnomcentrocosto(area) As nombre_area, "+
				"getcentroatencioncc(area) AS nombre_centro_atencion," +
				"getnombretipopaciente(tipo_paciente) AS nombre_tipo_paciente "+ 
				"from cuentas "+ 
				"WHERE id_ingreso = "+idIngreso+" AND estado_cuenta <> "+ConstantesBD.codigoEstadoCuentaCerrada+" ORDER BY id";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("id",rs.getObject("id"));
				elemento.put("fechaApertura",rs.getObject("fecha_apertura"));
				elemento.put("horaApertura",rs.getObject("hora_apertura"));
				elemento.put("nombreViaIngreso",rs.getObject("nombre_via_ingreso"));
				elemento.put("nombreArea",rs.getObject("nombre_area"));
				elemento.put("nombreCentroAtencion",rs.getObject("nombre_centro_atencion"));
				elemento.put("nombreTipoPaciente",rs.getObject("nombre_tipo_paciente"));
				
				resultados.add(elemento);
			}
			rs.close();
			st.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en listarCuentasAsocio: "+e);
		}
		return resultados; 
	}
	
	/**
	 * Mï¿½todo que consulta el tipo de evento de la cuenta
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static String obtenerCodigoTipoEventoCuenta(Connection con,String idCuenta)
	{
		String tipoEvento = "";
		try
		{
			String consulta = "SELECT tipo_evento FROM cuentas WHERE id = "+idCuenta;
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			logger.info("--->"+consulta);
			if(rs.next())
			{
				if(rs.getString("tipo_evento")!=null)
					tipoEvento = rs.getString("tipo_evento");
			}
			logger.info("TIPO DE EVENTO -->"+tipoEvento);
			rs.close();
			st.close();
		}
		catch(SQLException e)
		{
			logger.error("error en obtenerCodigoTipoEventoCuenta: "+e);
		}
		return tipoEvento;
		
	}
	
	/**
	 * Mï¿½todo que actualiza el tipo evento de la cuenta
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int actualizarTipoEventoCuenta(Connection con,HashMap campos)
	{
		try
		{
			String consulta = "UPDATE cuentas SET tipo_evento = ?, indicativo_acc_transito = ? WHERE id = ?";
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,campos.get("tipoEvento").toString());
			if(campos.get("tipoEvento").toString().equals(ConstantesIntegridadDominio.acronimoAccidenteTransito))
				ps.setBoolean(2,true);
			else
				ps.setBoolean(2,false);
			ps.setInt(3,Utilidades.convertirAEntero(campos.get("idCuenta")+""));
			
			int resp = ps.executeUpdate();
			
			ps.close();
			return resp;
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarTipoEventoCuenta: "+e);
			return 0;
		}
	}
	
	/**
	 * Mï¿½todo que verifica si se puede modificar el ï¿½rea de una cuenta
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean puedoModificarAreaCuenta(Connection con,HashMap campos)
	{
		boolean puedoModificar = true;
		try
		{
			//**********SE CONSULTA EL Nï¿½MERO DE SOLICITUDES QUE TIENE LA CUENTA************************
			int numSolicitudes = 0;
			String consulta = "select  count(1) AS tempo from solicitudes where cuenta="+campos.get("idCuenta");
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
			{
				numSolicitudes = rs.getInt("tempo");
				
				if(numSolicitudes>1)
				{
					//si tiene mas de una solicitud ya no se puede modificar el ï¿½rea de la cuenta
					puedoModificar = false;
				}
				//Si solo tiene una solicitud se verifica si son las valoraciones
				else if(numSolicitudes==1)
				{
					int valido = 0;
					
					//****************VALIDACION DE LAS VALORACIONES********************************
					//Si es una cuenta de hosptializacion que hace parte de un asocio se toma como valido modificar el area
					if(Integer.parseInt(campos.get("codigoViaIngreso").toString())==ConstantesBD.codigoViaIngresoHospitalizacion&&
						UtilidadTexto.getBoolean(campos.get("existeAsocio").toString()))
					{
						valido = 1;
					}
					//Si es cuenta de urgencias u hospitalizacion sin asocio se verifica que la valoracion se encuentre en estado solicitada
					else if (Integer.parseInt(campos.get("codigoViaIngreso").toString())==ConstantesBD.codigoViaIngresoHospitalizacion||
							Integer.parseInt(campos.get("codigoViaIngreso").toString())==ConstantesBD.codigoViaIngresoUrgencias)
					{
						//Se verifica como estï¿½ la solicitud de Valoracion Inicial Urgencias
						consulta = "select  count(1) as tempo " +
							"from solicitudes " +
							"where " +
							"cuenta="+campos.get("idCuenta")+" AND " +
							"estado_historia_clinica = "+ConstantesBD.codigoEstadoHCSolicitada;
						st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
						rs =  new ResultSetDecorator(st.executeQuery(consulta));
						if(rs.next())
							valido = rs.getInt("tempo");
					}
					
					if(valido>0)
						puedoModificar = true;
					else
						puedoModificar = false;
					//******************************************************************************
				}
			}
			rs.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en puedoModificarAreaCuenta: "+e);
		}
		
		return puedoModificar;
	}
	
	/**
	 * Mï¿½todo para actualizar el convenio arp afiliado de la cuenta
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int actualizarConvenioArpAfiliadoCuenta(Connection con,HashMap campos)
	{
		try
		{
			String consulta = "UPDATE cuentas SET convenio_arp_afiliado = ? WHERE id = "+campos.get("idCuenta");
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet ));
			
			if(!campos.get("codigoConvenioArpAfiliado").toString().equals(""))
				pst.setInt(1, Utilidades.convertirAEntero(campos.get("codigoConvenioArpAfiliado")+""));
			else
				pst.setNull(1, Types.INTEGER);
			
			int resp = pst.executeUpdate();
			pst.close();
			return resp;
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarConvenioArpAfiliadoCuenta: ", e);
			return 0;
		}
	}

	/**
	 * Actualizar el número de la póliza en el registro de accidentes de tránsito
	 * @param con Conexión con la BD
	 * @param idIngreso código del ingreso
	 * @param convenio código del convenio
	 * @param nroPoliza Número de póliza a actualizar
	 * @return Número de elementos actualizados
	 */
	public static int actualizarNroPolizaConvenioAccidenteTransito(Connection con, int idIngreso, int convenio, String nroPoliza)
	{
		PreparedStatementDecorator pst = null;
		try
		{
			String consulta = "UPDATE manejopaciente.sub_cuentas SET nro_poliza = ? WHERE ingreso = ? AND convenio = ?";
			pst =  new PreparedStatementDecorator(con, consulta);
			
			if(!UtilidadTexto.isEmpty(nroPoliza))
			{
				pst.setString(1, nroPoliza);
			}
			else
			{
				pst.setNull(1, Types.VARCHAR);
			}
			
			pst.setInt(2, idIngreso);
			pst.setInt(3, convenio);
			
			int resp = pst.executeUpdate();

			return resp;
		}
		catch(SQLException e)
		{
			Log4JManager.error("Error actualizando el numero de la póliza para el ingreso: "+idIngreso, e);
			return 0;
		}
		finally
		{
			UtilidadBD.cerrarObjetosPersistencia(pst, null, null);
		}
	}
	/**
	 * Mï¿½todo que actualiza el convenio en los cargos, cuando se realiza modificacion del convenio
	 * en la modificacion de cuentas
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int actualizarConvenioEnCargos(Connection con,HashMap campos)
	{
		try
		{
			String consulta = "UPDATE det_cargos SET convenio = "+campos.get("codigoConvenio")+" WHERE sub_cuenta = "+Utilidades.convertirALong(campos.get("idSubCuenta")+"");
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			int resp = st.executeUpdate(consulta);
			st.close();
			return resp;
			
		}
		catch(SQLException e)
		{
			logger.info("Error en actualizarConvenioEnCargos: "+e);
			return 0;
		}
	}

	/**
	 * 
	 * @param con
	 * @param codCuenta
	 * @return
	 */
	public static boolean esCuentaFinalAsocio(Connection con, int codCuenta)
	{
		try
		{
			boolean valor = false;
			String consulta = "SELECT CASE WHEN cuenta_final="+codCuenta+" THEN true else false end FROM asocios_cuenta where cuenta_final="+codCuenta+" and activo='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' ";
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs=  new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
			{
				valor = rs.getBoolean(1);
			}
			st.close();
			rs.close();
			return valor;
		}
		catch(SQLException e)
		{
			logger.info("Error en actualizarConvenioEnCargos: "+e);
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroIngreso
	 * @param esAsocio
	 * @return
	 */
	public static HashMap<Object, Object> obtenerViasIngresoTipoPacDadoIngreso(Connection con, int numeroIngreso, String esAsocio)
	{
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		String consulta="select id, via_ingreso as viaingreso, tipo_paciente as tipopaciente FROM cuentas WHERE id_ingreso=? and via_ingreso in("+ConstantesBD.codigoViaIngresoHospitalizacion+", "+ConstantesBD.codigoViaIngresoUrgencias+") ";
		if(!UtilidadTexto.isEmpty(esAsocio))
			consulta+=" AND getEsCuentaAsocio(id)='"+esAsocio+"' ";
							
		logger.info("\n obtenerViasIngresoTipoPacDadoIngreso->"+consulta+" \n");
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroIngreso);
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroIngreso
	 * @param viasIngreso
	 * @return
	 */
	public static Vector<String> obtenerCuentasIngreso(Connection con, int numeroIngreso, boolean epicrisis)
	{
		Vector<String> cuentas= new Vector<String>();
		String consulta="SELECT id FROM cuentas WHERE id_ingreso= ? ";
		
		if(epicrisis)
			consulta+="and (" +
								"(" +
									"via_ingreso = "+ConstantesBD.codigoViaIngresoHospitalizacion+"" +
								") " +
								"or " +
								"( " +
									"via_ingreso ="+ConstantesBD.codigoViaIngresoUrgencias+" " +
									"and " +
										"(" +
											"getExisteConductaSeguirUrg(id_ingreso, "+ConstantesBD.codigoConductaSeguirCamaObservacion+")='"+ConstantesBD.acronimoSi+"' " +
											"or getExisteConductaSeguirUrg(id_ingreso, "+ConstantesBD.codigoConductaSeguirSalaCirugiaAmbulatoria+")='"+ConstantesBD.acronimoSi+"' " +
											"or getExisteConductaSeguirUrg(id_ingreso, "+ConstantesBD.codigoConductaSeguirHospitalizarPiso+")='"+ConstantesBD.acronimoSi+"' " +
											"or getExisteConductaSeguirUrg(id_ingreso, "+ConstantesBD.codigoConductaSeguirTrasladoCuidadoEspecial+")='"+ConstantesBD.acronimoSi+"' " +
										")" +
								")" +
							") ";
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroIngreso);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while (rs.next())
			{
				cuentas.add(rs.getInt(1)+"");
			}
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
		return cuentas;
	}

	/**
	 * 
	 * @param idCuenta
	 * @return
	 */
	public static String obtenerFechaVigenciaTopeCuenta(String idCuenta) 
	{
		PreparedStatementDecorator ps=null;
		String retorna="";
		try 
		{
			//modificaron el documento........ y solo debe hacerlo por la fecha de apertura de la cuenta
			String consulta=" SELECT " +
								"to_char(c.fecha_apertura,'dd/mm/yyyy') AS fechavigencia " +
							"FROM " +
								"cuentas c WHERE c.id=?";

			Connection con= UtilidadBD.abrirConexion();
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("\n\n\n obtenerFechaVigenciaTopeCuenta--->"+consulta+"  idCuenta->"+idCuenta+"\n\n\n");
			ps.setInt(1, Utilidades.convertirAEntero(idCuenta));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				retorna= rs.getString(1);
			}
			UtilidadBD.closeConnection(con);
			ps.close();
			rs.close();
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return retorna;
	}





	public static String[] consultarAreaYViaIngreso(Connection con,
			int codigoCuenta) throws SQLException,Exception {
		// TODO Auto-generated method stub
		String[]retornar=null;
		String consulta="SELECT cc.codigo as codigo_centro_costo, " +
					"cc.nombre as nombre_centro_costo, " +
					"vi.codigo as codigo_via_ingreso, " +
					"vi.nombre as nombre_via_ingreso " +
					"from cuentas c " +
				"INNER JOIN vias_ingreso vi " +
				"ON vi.codigo = c.via_ingreso " +
				"INNER JOIN centros_costo cc " +
				"ON cc.codigo = c.area " +
				"where c.id = ?";
		PreparedStatement ps=con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
		ps.setInt(1, codigoCuenta);
		
		ResultSet rs=ps.executeQuery();
		
		if(rs.next()){
			retornar=new String[]{
				rs.getString("codigo_centro_costo"),
				rs.getString("nombre_centro_costo"),
				rs.getString("codigo_via_ingreso"),
				rs.getString("nombre_via_ingreso")
			};
		}
				
		return retornar;
	}
	
	/**
	 * se consulta el numero de factura
	 * @param con
	 * @param numeroFactura
	 * @return String con el numero de la factura
	 * @throws SQLException
	 */
	public static String obtenerIngresoXNumeroFactura(Connection con,String numeroFactura) throws SQLException{
		String ingresoFactura="";

		//consulta
		String consulta = " SELECT i.consecutivo "+
		" FROM Facturas f "+
		" JOIN sub_cuentas sc "+
		" ON (f.sub_cuenta = sc.sub_cuenta) "+
		" JOIN ingresos i "+
		" ON (sc.ingreso =i.id) "+
		" WHERE f.codigo = ?";

		PreparedStatement ps = con.prepareStatement(consulta);

		//se adiciona el numero de la factura 
		if(!UtilidadTexto.isEmpty(numeroFactura)){
			ps.setInt(1, Integer.valueOf(numeroFactura));
		}else{
			ps.setInt(1, 0);
		}

		ResultSet rs = ps.executeQuery();

		if(rs.next()){
			ingresoFactura = String.valueOf(rs.getInt("consecutivo"));
		}
		
		//retorno y cierre de conexion 
		rs.close();
		ps.close();
		return ingresoFactura;
	}
	
	
	/**
	 * Se consultans los datos de la institucion para colocar  en el reporte 
	 * @param con
	 * @param numeroFactura
	 * @return DtoInstitucion con los datos de la institucion
	 * @throws SQLException
	 */
	public  static  DtoInstitucion consultarDatosInstitucionXFactura(Connection con,String numeroFactura) throws SQLException{
		
		//consulta 
		String consulta =" SELECT c.descripcion, i.* "+
			" FROM facturas f "+
			" JOIN instituciones i "+
			" ON (f.institucion=i.codigo) "+
			" join ciudades c "+
			" on(i.ciudad=c.CODIGO_CIUDAD and DEPARTAMENTO= CODIGO_DEPARTAMENTO and pais = CODIGO_PAIS )   "+
			" WHERE f.codigo   = ? ";
		DtoInstitucion dtoInstitucion= new DtoInstitucion();
		PreparedStatement ps = con.prepareStatement(consulta);
		
		//se adiciona el numero de la factura 
		if(!UtilidadTexto.isEmpty(numeroFactura)){
			ps.setInt(1, Integer.valueOf(numeroFactura));
		}else{
			ps.setInt(1, 0);
		}
		
		ResultSet rs = ps.executeQuery();
		
		//se obtienen los datos de la consulta
		if(rs.next()){
			dtoInstitucion.setNombre(rs.getString("RAZON_SOCIAL"));
			dtoInstitucion.setNit(rs.getString("NIT"));
			dtoInstitucion.setDireccion(rs.getString("DIRECCION"));
			dtoInstitucion.setTelefono(rs.getString("TELEFONO"));
			dtoInstitucion.setCiudad(rs.getString("DESCRIPCION"));
		}
		
		//cierre de PreparedStatement y ResultSet
		rs.close();
		ps.close();
		
		//retorno solicitado
		return dtoInstitucion;
		
	}
	
	
	/**
	 * @param con
	 * @param numeroFactura
	 * @return
	 * @throws SQLException
	 */
	public static String obtenerDatosAnulacionFactura(Connection con,String numeroFactura) throws SQLException{
		
		String fechaAnulacion="";
		
		//consulta
		String consulta=" select  af.fecha_grabacion||' '||af.hora_grabacion fecha_anulacion from facturas f join anulaciones_facturas af "+
			" on(f.codigo = af.codigo ) "+
			" where f.estado_facturacion=2 "+
			" and f.codigo = ?";
		PreparedStatement ps = con.prepareStatement(consulta);
		
		//se adiciona el nuemero de la factura
		ps.setInt(1, Integer.valueOf(numeroFactura));
		ResultSet rs = ps.executeQuery();
		
		if(rs.next()){
			fechaAnulacion=rs.getString("fecha_anulacion");
		}
		
		//cierre de PreparedStatement y ResultSet
		rs.close();
		ps.close();
		
		//retorno de la fecha de anulacion
		return fechaAnulacion;
	}
	
	
	/**
	 * @param con
	 * @param numeroFactura
	 * @return
	 * @throws SQLException
	 */
	public static String obtenerCentroAtencionFactura(Connection con,String numeroFactura) throws SQLException{
		
		String centroAtencion="";
		
		//consulta
		String consulta=" select c.descripcion nombre_centro_atencion from facturas f join centro_atencion c "+
			" on(f.CENTRO_ATEN=c.CONSECUTIVO) "+
			" where f.codigo=?";
		PreparedStatement ps = con.prepareStatement(consulta);
		
		//se adiciona el numero de la factura
		ps.setInt(1, Integer.valueOf(numeroFactura));
		ResultSet rs = ps.executeQuery();
		
		if(rs.next()){
			centroAtencion=rs.getString("nombre_centro_atencion");
		}
		
		//cierre de PreparedStatement y ResultSet
		rs.close();
		ps.close();
		
		//se retorna el centro de atencion
		return centroAtencion;
	}
	



	/**
	 * @param con
	 * @param numeroFactura
	 * @return
	 * @throws SQLException
	 */
	public static String obtenerNumeroFacturaAsociada(Connection con,String numeroFactura) throws SQLException{
		
		String centroAtencion="";
		
		//consulta
		String consulta=" select   f.consecutivo_factura  numeroFactura from facturas f "+
			"  where f.codigo=? ";
		PreparedStatement ps = con.prepareStatement(consulta);
		
		//consulta el numero de la factura
		ps.setInt(1, Integer.valueOf(numeroFactura));
		ResultSet rs = ps.executeQuery();
		
		if(rs.next()){
			centroAtencion=rs.getString("numeroFactura");
		}
		
		//cierre de PreparedStatement y ResultSet
		rs.close();
		ps.close();
		
		//centro de atencion
		return centroAtencion;
	}



	/**
	 * @param con
	 * @param numeroFactura
	 * @return
	 * @throws SQLException
	 */
	public static DtoDatosResponsableFacturacion obtenerResponsable(Connection con,String numeroFactura) throws SQLException{
		
		DtoDatosResponsableFacturacion dtoDatosResponsableFacturacion=new DtoDatosResponsableFacturacion();
		
		//consulta
		String consulta=" SELECT  "+
			"  CASE "+
			"    WHEN f.cod_res_particular IS NULL "+
			"    THEN con.nombre "+
			"    ELSE getnomdeudoringreso(c.id_ingreso) "+
			"   END                                  AS responsable, "+
			" CASE "+
			"   WHEN f.cod_res_particular IS NULL "+
			"   THEN em.direccion "+
			"   ELSE getnomdeudoringreso(c.id_ingreso) "+
			"  END                                  AS direccion, "+
			" CASE "+
			"  WHEN f.cod_res_particular IS NULL "+
			"  THEN em.telefono "+
			"  ELSE getnomdeudoringreso(c.id_ingreso) "+
			" END                                  AS telefono, "+
			" CASE "+
			" WHEN f.cod_res_particular IS NULL "+
			" THEN 'CONVENIO' "+
			" ELSE 'PERSONA' "+
			" END                                  AS tipoResponsable, " +
			"  f.valor_convenio                                   AS valorconvenio," +
			" f.valor_bruto_pac                                  AS valorbrutopaciente, " +
			"  f.convenio                                         AS convenio"+
			" FROM facturas f "+
			" INNER JOIN estados_factura_f ef "+
			" ON(f.estado_facturacion=ef.codigo) "+
			" INNER JOIN estados_factura_paciente ep "+
			" ON(f.estado_paciente=ep.codigo) "+
			" LEFT OUTER JOIN vias_ingreso vi "+
			" ON(f.via_ingreso=vi.codigo) "+
			" LEFT OUTER JOIN convenios con "+
			" ON(f.convenio=con.codigo) "+
			" join empresas em on (con.empresa =em.codigo )  "+
			" LEFT OUTER JOIN cuentas c "+
			" ON(f.cuenta=c.id) "+
			" LEFT OUTER JOIN sub_cuentas sbc "+
			" ON (f.sub_cuenta=sbc.sub_cuenta) "+
			" LEFT OUTER JOIN histo_detalle_monto hdm "+
			" ON(f.monto_cobro=hdm.codigo_pk) "+
			" LEFT OUTER JOIN tipos_monto tm "+
			" ON(hdm.tipo_monto_codigo=tm.codigo) "+
			" LEFT OUTER JOIN detalle_monto demo "+
			" ON(demo.detalle_codigo=hdm.detalle_codigo) "+
			" LEFT OUTER JOIN detalle_monto_general dmg "+
			" ON (dmg.detalle_codigo=demo.detalle_codigo) "+
			" WHERE f.codigo        =?";
		
		//se adiciona el numero de la factura 
		PreparedStatement ps = con.prepareStatement(consulta);
		ps.setInt(1, Integer.valueOf(numeroFactura));
		ResultSet rs = ps.executeQuery();
		
		if(rs.next()){
			dtoDatosResponsableFacturacion.setNombreResponsable(rs.getString("responsable"));
			dtoDatosResponsableFacturacion.setDireccion(rs.getString("direccion"));
			dtoDatosResponsableFacturacion.setTelefono(rs.getString("telefono"));
			dtoDatosResponsableFacturacion.setTipoResponsable(rs.getString("tipoResponsable"));
			dtoDatosResponsableFacturacion.setValorConvenio(rs.getString("valorconvenio"));
			dtoDatosResponsableFacturacion.setValorBrutoPaciente(rs.getString("valorbrutopaciente"));
			dtoDatosResponsableFacturacion.setConvenio(rs.getString("convenio"));
			
		}
		//cierre de PreparedStatement y ResultSet
		rs.close();
		ps.close();
		
		//se retorna lso datos del responsable
		return dtoDatosResponsableFacturacion;
	}

	/**
	 * @param con
	 * @param numeroFactura
	 * @return
	 * @throws SQLException
	 */
	public static String obtenerFechaGeneracionFactura(Connection con,String numeroFactura) throws SQLException{
		
		String centroAtencion="";
		
		//consulta 
		String consulta=" select  to_char(f.fecha,'dd/mm/yyyy') fecha from facturas f where f.codigo=?";
		PreparedStatement ps = con.prepareStatement(consulta);
		
		//se adiciona el numero de la factura
		ps.setInt(1, Integer.valueOf(numeroFactura));
		ResultSet rs = ps.executeQuery();
		
		if(rs.next()){
			centroAtencion=rs.getString("fecha");
		}
		
		//cierre de PreparedStatement y ResultSet
		rs.close();
		ps.close();
		
		//se retorna el centro de atencion
		return centroAtencion;
	}
	
	
	/**
	 * Consulta los servicios de la factura
	 * @param con
	 * @param numeroFactura
	 * @return List<DtoFacturaAgrupada> con los articulos de la factura
	 * @throws SQLException
	 */
	public static List<DtoFacturaAgrupada> articulosFacturaAgrupada(Connection con,String numeroFactura)throws SQLException{
		List<DtoFacturaAgrupada> articulos= new LinkedList<DtoFacturaAgrupada>();
		
		//consulta
		String consulta = " SELECT f.codigo AS codigofactura, "+
			" na.nombre  AS nombre, "+
			" SUM(dfs.valor_total) AS valortotal "+
			" FROM facturas f "+
			" INNER JOIN det_factura_solicitud dfs "+
			" ON (f.codigo=dfs.factura) "+
			" INNER JOIN articulo a "+
			" ON(a.codigo=dfs.articulo) "+
			" INNER JOIN naturaleza_articulo na "+
			" ON(na.acronimo    =a.naturaleza "+
			" AND na.institucion=a.institucion) "+
			" WHERE f.codigo    =? "+
			" AND dfs.servicio IS NULL " +
			" AND dfs.tipo_solicitud <> "+ConstantesBD.tipoSolicitudCirugia+
			" GROUP BY f.codigo, "+
			" nombre "+
			" order by na.nombre asc";
		
		PreparedStatement ps = con.prepareStatement(consulta);
		
		//se adiciona el numero de la factura
		ps.setInt(1, Integer.valueOf(numeroFactura));
		ResultSet rs = ps.executeQuery();
		
		
		while(rs.next()){
			DtoFacturaAgrupada dtoFacturaAgrupada = new DtoFacturaAgrupada();
			dtoFacturaAgrupada.setCodigofactura(String.valueOf(rs.getInt("codigofactura")));
			dtoFacturaAgrupada.setDescripcion(rs.getString("nombre"));
			dtoFacturaAgrupada.setValorTotal(String.valueOf(rs.getInt("valortotal")));
			dtoFacturaAgrupada.setDetalleProcedimiento("");
			articulos.add(dtoFacturaAgrupada);
		}
		
		//cierre de PreparedStatement y ResultSet
		rs.close();
		ps.close();
		
		//retorno asociado
		return articulos;
	}
	
	
	
	/**
	 * Consulta los serviciios de la factura
	 * @param con
	 * @param numeroFactura
	 * @return List<DtoFacturaAgrupada> con los servicios de la factura
	 * @throws SQLException
	 */
	public static List<DtoFacturaAgrupada> serviciosFacturaAgrupada(Connection con,String numeroFactura)throws SQLException{
		List<DtoFacturaAgrupada> servicios= new LinkedList<DtoFacturaAgrupada>();
		
		//consulta
		String consulta = " SELECT tabla.codigofactura AS codigofactura, "+
			" tabla.descripcion        AS gruposervicio, "+
			" SUM(tabla.valortotal )   AS valortotal "+
			" FROM ( "+
			" (SELECT f.codigo AS codigofactura, "+
			"  gs.descripcion, "+
			" SUM(dfs.valor_total) AS valortotal "+
			" FROM facturas f "+
			" INNER JOIN det_factura_solicitud dfs "+
			" ON (f.codigo=dfs.factura) "+
			" INNER JOIN servicios serv "+
			" ON (dfs.servicio = serv.codigo) "+
			"  INNER JOIN GRUPOS_SERVICIOS gs "+
			" ON (serv.GRUPO_SERVICIO=gs.codigo) "+
			" INNER JOIN solicitudes s "+
			"  ON (dfs.solicitud     =s.numero_solicitud) "+
			" WHERE dfs.articulo   IS NULL "+
			"  AND s.tipo           <>14 "+
			" AND s.tipo           <>15 "+
			" AND dfs.cantidad_cargo>0 "+
			" GROUP BY f.codigo, "+
			"  gs.descripcion "+
			" ) ) tabla "+
			" WHERE tabla.codigofactura=?  "+
			" GROUP BY codigofactura, "+
			" tabla.descripcion "+
			" ORDER BY gruposervicio ASC";
		
		PreparedStatement ps = con.prepareStatement(consulta);
		
		//se adiciona el numero de la factura
		ps.setInt(1, Integer.valueOf(numeroFactura));
		ResultSet rs = ps.executeQuery();
		
		
		while(rs.next()){
			DtoFacturaAgrupada dtoFacturaAgrupada = new DtoFacturaAgrupada();
			dtoFacturaAgrupada.setCodigofactura(String.valueOf(rs.getInt("codigofactura")));
			dtoFacturaAgrupada.setDescripcion(rs.getString("gruposervicio"));
			dtoFacturaAgrupada.setValorTotal(String.valueOf(rs.getInt("valortotal")));
			dtoFacturaAgrupada.setDetalleProcedimiento("");
			servicios.add(dtoFacturaAgrupada);
		}
		
		//cierre de PreparedStatement y ResultSet
		rs.close();
		ps.close();
		
		//se retornan los servicios de la factura 
		return servicios;
	}
	
	
	  /**
	   * se consultan las cirugias
	 * @param con
	 * @param numeroFactura
	 * @return List<DtoFacturaAgrupada>
	 * @throws SQLException
	 */
	public static List<DtoFacturaAgrupada> cirugiasFacturaAgrupada(Connection con,String numeroFactura,Integer codigoInstitucion)throws SQLException{
		List<DtoFacturaAgrupada> cirugias= new LinkedList<DtoFacturaAgrupada>();
		String nombreAgrupadomaterialesCirugia = agrupadoMaterialesCirugia(con, codigoInstitucion);
		
		
		
		//consulta
		String consulta = " SELECT tabla.codigofactura AS codigofactura, "+
			"  ' '                       AS valortotal, "+
			" tabla.codigopropeitario  AS codigoservicio, "+
			" tabla.procedimiento      AS procedimiento ,tabla.codigodetallefactura     AS codigodetallefactura , solicitud as solciitud "+
			" FROM ( "+
			"  (SELECT f.codigo AS codigofactura, "+
			"  gs.descripcion, "+
			" '' AS valortotal, "+
			" CASE "+
			"  WHEN getcodigopropietario(f.convenio,dfs.servicio)                   IS NULL "+
			" OR getcodigopropietario(f.convenio,dfs.servicio)                      ='' "+
			" OR getnombreservicio(dfs.servicio,getcodimpservconvenio(f.convenio)) IS NULL "+
			" OR getnombreservicio(dfs.servicio,getcodimpservconvenio(f.convenio))  ='' "+
			"  THEN getcodigopropservicio2(dfs.servicio,0) "+
			"  ELSE getcodigopropietario(f.convenio,dfs.servicio) "+
			" END AS codigopropeitario, "+
			" CASE "+
			" WHEN getcodigopropietario(f.convenio,dfs.servicio)                   IS NULL "+
			" OR getcodigopropietario(f.convenio,dfs.servicio)                      ='' "+
			" OR getnombreservicio(dfs.servicio,getcodimpservconvenio(f.convenio)) IS NULL "+
			" OR getnombreservicio(dfs.servicio,getcodimpservconvenio(f.convenio))  ='' "+
			"  THEN getnombreservicio(dfs.servicio,0) "+
			" ELSE getnombreservicio(dfs.servicio,getcodimpservconvenio(f.convenio)) "+
			" END AS procedimiento,dfs.codigo  ||''         AS codigodetallefactura ,  dfs.solicitud as solicitud "+
			" FROM facturas f  "+
			" INNER JOIN det_factura_solicitud dfs "+
			" ON (f.codigo=dfs.factura) "+
			" INNER JOIN servicios serv "+
			" ON (dfs.servicio = serv.codigo) "+
			" INNER JOIN GRUPOS_SERVICIOS gs "+
			" ON (serv.GRUPO_SERVICIO=gs.codigo) "+
			" INNER JOIN solicitudes s "+
			" ON (dfs.solicitud   =s.numero_solicitud) "+
			" WHERE dfs.articulo IS NULL "+
			" AND s.tipo          =14 "+
			" )) tabla "+
			" WHERE tabla.codigofactura=? "+
			" ORDER BY procedimiento ASC";
		
		PreparedStatement ps = con.prepareStatement(consulta);
		
		//se adiciona el numero de la factura
		ps.setInt(1, Integer.valueOf(numeroFactura));
		ResultSet rs = ps.executeQuery();
		
		
		while(rs.next()){
			DtoFacturaAgrupada dtoFacturaAgrupada = new DtoFacturaAgrupada();
			dtoFacturaAgrupada.setCodigofactura(String.valueOf(rs.getInt("codigofactura")));
			dtoFacturaAgrupada.setDescripcion(rs.getString("codigoservicio")+" - "+rs.getString("procedimiento"));
			dtoFacturaAgrupada.setValorTotal(rs.getString("valortotal"));
			dtoFacturaAgrupada.setDetalleProcedimiento("");
			dtoFacturaAgrupada.setCodigoCirugia(rs.getString("codigoservicio"));
			dtoFacturaAgrupada.setCodigoDetallefactura(rs.getString("codigodetallefactura"));
			
			//se consultan los detalles de la factura
			dtoFacturaAgrupada.setDetallesCirugias(consultarDetalleCirugias(con, dtoFacturaAgrupada.getCodigoDetallefactura(),nombreAgrupadomaterialesCirugia,numeroFactura,rs.getInt("solciitud")));
			cirugias.add(dtoFacturaAgrupada);
		}
		
		//cierre de PreparedStatement y ResultSet
		rs.close();
		ps.close();
		
		//se retornan las ciruias
		return cirugias;
	}
	

	/**
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 * @throws SQLException
	 */
	public static String agrupadoMaterialesCirugia(Connection con,Integer codigoInstitucion) throws SQLException{

		String nombreAgrupador="";

		String consulta = " SELECT  "+
		" ta.nombre_asocio  AS nombreasocio  "+
		" FROM tipos_asocio ta  "+
		" INNER JOIN tipos_servicio ts  "+
		" ON (ts.acronimo        = ta.tipos_servicio)  "+
		" WHERE institucion      = ?  "+
		" AND ta.tipos_servicio <> 'P'   "+
		" AND ts.acronimo='M'";
		PreparedStatement ps = con.prepareStatement(consulta);
		ps.setInt(1, codigoInstitucion);
		ResultSet rs = ps.executeQuery();
		if(rs.next()){
			nombreAgrupador = rs.getString("nombreasocio");
		}
		rs.close();
		ps.close();
		return nombreAgrupador;
	}
	
	
	/**
	 * Se conusultan los detalles de la factura
	 * @param con
	 * @param codigoDetalleFactura
	 * @return List<DtoDetalleCirugiasFacturaAgrupada> con los detalles de la factura
	 * @throws SQLException
	 */
	public static  List<DtoDetalleCirugiasFacturaAgrupada> consultarDetalleCirugias(Connection con,String codigoDetalleFactura,String nombreAgrupadomaterialesCirugia,String numeroFactura,Integer numeroSolicitud)throws SQLException{
		List<DtoDetalleCirugiasFacturaAgrupada>  detalleCirugias = new LinkedList<DtoDetalleCirugiasFacturaAgrupada>(); 
		
		//consulta
		String consulta=" SELECT nombreasocio,valortotal "+
			" FROM "+
			" (SELECT ta.codigo_asocio                            AS acronimoasocio, "+
			"  lower(getnombreservicio(adf.servicio_asocio, 0 )) AS nombreasocio, "+
			"  adf.valor_total                                   AS valortotal, "+
			" dfs.solicitud                                     AS numerosolicitud, "+
			" dfs.servicio                                      AS codigoservicio "+
			"  FROM det_factura_solicitud dfs "+
			" INNER JOIN asocios_det_factura adf "+
			" ON(adf.codigo=dfs.codigo) "+
			" INNER JOIN tipos_asocio ta "+
			" ON (ta.codigo      =adf.tipo_asocio) "+
			" WHERE dfs.codigo   =? "+
			" AND ta.codigo     <> 4 "+
			"  AND adf.valor_total>0 "+
			" UNION "+
			" SELECT ta.codigo_asocio                             AS acronimoasocio, "+
			" '"+nombreAgrupadomaterialesCirugia+"' AS nombreasocio, "+
			" SUM(adf.valor_total)                              AS valortotal, "+
			" dfs.solicitud                                     AS numerosolicitud, "+
			" dfs.servicio                                      AS codigoservicio "+
			" FROM det_factura_solicitud dfs "+
			" INNER JOIN asocios_det_factura adf "+
			" ON(adf.codigo=dfs.codigo) "+
			" INNER JOIN tipos_asocio ta "+
			" ON (ta.codigo      =adf.tipo_asocio) "+
			" WHERE dfs.codigo   =? "+
			" AND ta.codigo      = 4 "+
			" AND adf.valor_total>0 "+
			" GROUP BY ta.codigo_asocio, "+
			" lower(getnombreservicio(adf.servicio_asocio, 0 )), "+
			" dfs.solicitud , "+
			" dfs.servicio "+
			" ) detalle_cirugia "+
			" ORDER BY nombreasocio ASC "; 
		
		PreparedStatement ps = con.prepareStatement(consulta);
		
		//como hay un UNION en la consulta se adicionan los numeros de la factura
		ps.setInt(1, Integer.valueOf(codigoDetalleFactura));
		ps.setInt(2, Integer.valueOf(codigoDetalleFactura));
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()){
			DtoDetalleCirugiasFacturaAgrupada dtoDetalleCirugiasFacturaAgrupada = new DtoDetalleCirugiasFacturaAgrupada();
			dtoDetalleCirugiasFacturaAgrupada.setNombreServicioCirugia(rs.getString("nombreasocio"));
			dtoDetalleCirugiasFacturaAgrupada.setValor(String.valueOf(rs.getInt("valortotal")));
			detalleCirugias.add(dtoDetalleCirugiasFacturaAgrupada); 
		}
		detalleCirugias.addAll(articulosFacturaAgrupadaDetalleCirugia(con, numeroFactura,numeroSolicitud));
		//cierre de PreparedStatement y ResultSet
		rs.close();
		ps.close();
		
		//se retornan los detalles de la cirugia
		return detalleCirugias;
	}
	

	/**
	 * @param con
	 * @param numeroFactura
	 * @return
	 * @throws SQLException
	 */
	public static List<DtoDetalleCirugiasFacturaAgrupada> articulosFacturaAgrupadaDetalleCirugia(Connection con,String numeroFactura,Integer numeroSolicitud)throws SQLException{
		List<DtoDetalleCirugiasFacturaAgrupada>  detalleCirugias = new LinkedList<DtoDetalleCirugiasFacturaAgrupada>(); 
		
		//consulta
		String consulta = " SELECT f.codigo AS codigofactura, "+
			" 'Otros Medicamentos y/o Insumos'  AS nombre, "+
			" SUM(dfs.valor_total) AS valortotal "+
			" FROM facturas f "+
			" INNER JOIN det_factura_solicitud dfs "+
			" ON (f.codigo=dfs.factura) "+
			" INNER JOIN articulo a "+
			" ON(a.codigo=dfs.articulo) "+
			" INNER JOIN naturaleza_articulo na "+
			" ON(na.acronimo    =a.naturaleza "+
			" AND na.institucion=a.institucion) "+
			" WHERE f.codigo    =? " +
			" and dfs.solicitud = ? "+
			" AND dfs.servicio IS NULL " +
			" AND dfs.tipo_solicitud = "+ConstantesBD.tipoSolicitudCirugia+
			" GROUP BY f.codigo, "+
			//MT6132 se cambia la agrupación indicando el indice 2 que corresponde a el nombre
			" 2 ";
			//" order by na.nombre asc";
		
		PreparedStatement ps = con.prepareStatement(consulta);
		
		//se adiciona el numero de la factura
		ps.setInt(1, Integer.valueOf(numeroFactura));
		ps.setInt(2, numeroSolicitud);
		ResultSet rs = ps.executeQuery();
		
		
		while(rs.next()){
			DtoDetalleCirugiasFacturaAgrupada dtoDetalleCirugiasFacturaAgrupada = new DtoDetalleCirugiasFacturaAgrupada();
			dtoDetalleCirugiasFacturaAgrupada.setNombreServicioCirugia(rs.getString("nombre"));
			dtoDetalleCirugiasFacturaAgrupada.setValor(String.valueOf(rs.getInt("valortotal")));
			detalleCirugias.add(dtoDetalleCirugiasFacturaAgrupada); 
		}
		
		//cierre de PreparedStatement y ResultSet
		rs.close();
		ps.close();
		
		//retorno asociado
		return detalleCirugias;
	}
	
	
	
	
	
	/**
	 * Se consultan los paquetes de la factura
	 * @param con
	 * @param numeroFactura
	 * @return List<DtoFacturaAgrupada> paquetes de la factura
	 * @throws SQLException
	 */
	public static List<DtoFacturaAgrupada> paquetesFacturaAgrupada(Connection con,String numeroFactura)throws SQLException{
		List<DtoFacturaAgrupada> paquetes= new LinkedList<DtoFacturaAgrupada>();
		
		//consulta
		String consulta = " SELECT tabla.codigofactura AS codigofactura, "+
			" tabla.codigopropeitario  AS codigopropeitario, "+
			" tabla.procedimiento      AS procedimiento,tabla.valortotal         AS valorTotal  "+
			" FROM ( "+
			" (SELECT f.codigo AS codigofactura, "+
			" ''             AS coddetfac, "+
			" dfs.servicio   AS servicio, "+
			" ''             AS solicitud, "+
			"  CASE "+
			" WHEN getcodigopropietario(f.convenio,dfs.servicio)                   IS NULL "+
			"  OR getcodigopropietario(f.convenio,dfs.servicio)                      ='' "+
			" OR getnombreservicio(dfs.servicio,getcodimpservconvenio(f.convenio)) IS NULL "+
			" OR getnombreservicio(dfs.servicio,getcodimpservconvenio(f.convenio))  ='' "+
			" THEN getcodigopropservicio2(dfs.servicio,0) "+
			"  ELSE getcodigopropietario(f.convenio,dfs.servicio) "+
			" END AS codigopropeitario, "+
			" CASE "+
			" WHEN getcodigopropietario(f.convenio,dfs.servicio)                   IS NULL "+
			"  OR getcodigopropietario(f.convenio,dfs.servicio)                      ='' "+
			" OR getnombreservicio(dfs.servicio,getcodimpservconvenio(f.convenio)) IS NULL "+
			" OR getnombreservicio(dfs.servicio,getcodimpservconvenio(f.convenio))  ='' "+
			" THEN getnombreservicio(dfs.servicio,0) "+
			" ELSE getnombreservicio(dfs.servicio,getcodimpservconvenio(f.convenio)) "+
			" END     AS procedimiento, "+
			"  'false' AS escirugia, "+
			" SUM(dfs.cantidad_cargo) "+
			" ||'' AS cantidad, "+
			" (SUM(dfs.valor_total)/SUM(dfs.cantidad_cargo)) "+
			" ||'' AS valorunitario, "+
			" SUM(dfs.valor_total) "+
			"  ||'' AS valortotal, "+
			"  s.tipo "+
			"  FROM facturas f "+
			" INNER JOIN det_factura_solicitud dfs "+
			" ON (f.codigo=dfs.factura) "+
			" INNER JOIN solicitudes s "+
			" ON (dfs.solicitud     =s.numero_solicitud) "+
			" WHERE dfs.articulo   IS NULL "+
			"  AND s.tipo            =15 "+
			" AND dfs.cantidad_cargo>0 "+
			" GROUP BY f.codigo, "+
			" dfs.servicio, "+
			" f.convenio, "+
			" s.tipo "+
			"  ) ) tabla "+
			" WHERE tabla.codigofactura= ? "+
			" ORDER BY procedimiento asc ";
		
		PreparedStatement ps = con.prepareStatement(consulta);
		
		//se adiciona el numero de la factura
		ps.setInt(1, Integer.valueOf(numeroFactura));
		ResultSet rs = ps.executeQuery();
		
		while (rs.next()){
			DtoFacturaAgrupada dtoFacturaAgrupada = new DtoFacturaAgrupada();
			dtoFacturaAgrupada.setCodigofactura(rs.getString("codigofactura")); 
			dtoFacturaAgrupada.setCodigoCirugia(rs.getString("codigopropeitario"));
			dtoFacturaAgrupada.setDescripcion(rs.getString("codigopropeitario")+" - "+rs.getString("procedimiento"));
			dtoFacturaAgrupada.setValorTotal(String.valueOf(rs.getInt("valorTotal")));
			paquetes.add(dtoFacturaAgrupada);
			
		}
		
		//cierre de PreparedStatement y ResultSet
		rs.close();
		ps.close();
		
		//paquetes de la factura
		return paquetes;
	}
	

	/**
	 * Se consulta si se nesecitan los decimales o no 
	 * @param con
	 * @param codigoConvenio
	 * @return boolean tipo de formato de los valores
	 * @throws SQLException
	 */
	public static boolean formatoValoresFormatoFacturaAgrupada(Connection con,String codigoConvenio)throws SQLException{
		
		Boolean usarDecimales=new Boolean(false);
		
		//consulta 
		String consulta = " SELECT "+
			" CASE "+
			" WHEN c.ajuste_servicios = 'AJCE' "+
			" OR c.ajuste_servicios   = 'AJDE' "+
			" OR c.ajuste_servicios   = 'AJUN' "+
			" OR c.ajuste_articulos   = 'AJCE' "+
			" OR c.ajuste_articulos   = 'AJDE' "+
			" OR c.ajuste_articulos   = 'AJUN' "+
			" THEN 'false' "+
			" ELSE 'true' "+
			" END ajustar "+
			" FROM convenios c "+
			" WHERE c.codigo=? ";
		
		PreparedStatement ps = con.prepareStatement(consulta);
		
		//se adiciona el codigo del convenio
		ps.setInt(1, Integer.valueOf(codigoConvenio));
		ResultSet rs = ps.executeQuery();
		
		if(rs.next()){
			if(rs.getString("ajustar").equals("true")){
				usarDecimales=true;
			}
		}
		
		//cierre de PreparedStatement y ResultSet
		rs.close();
		ps.close();
		
		//si se usan o no decimales
		return usarDecimales;
	}
	
	
	/**
	 * @param con
	 * @param convenioId
	 * @param ingresoConsecutivo
	 * @return
	 * @throws SQLException
	 */
	public static Integer consultarNumeroAutorizacion(Connection con,Integer convenioId, Integer ingresoConsecutivo) throws SQLException{
		Integer numeroAutorizacion=new Integer(0);
		Integer subcuenta = new Integer(0);
		Integer codigoAutorizacion = new Integer(0);
		Integer numeroDeAutoriacion = new Integer(0);
		Integer codigoIngresoId = new Integer(0);
		String consultaIdSubcuentaAsociada=" SELECT sc.sub_cuenta FROM ingresos i JOIN sub_cuentas sc ON (i.id=sc.ingreso) "+
		" where i.consecutivo=? "+
		" and sc.convenio = ? "+
		" order by sc.fecha_modifica||' '||sc.hora_modifica desc"; 



		String consultaNumeroDeAutorizacion = " SELECT a.codigo  AS numero_aitorizacion "+
		" FROM autorizaciones a "+
		" INNER JOIN det_autorizaciones d "+
		" ON (d.autorizacion = a.codigo) "+
		" LEFT OUTER JOIN tipos_ser_sol tss "+
		" ON(tss.codigo = a.tipo_servicio_solicitado) "+
		" LEFT OUTER JOIN coberturas_salud cs "+
		" ON(cs.codigo = a.tipo_cobertura) "+
		" LEFT OUTER JOIN causas_externas ce "+
		" ON(ce.codigo     = a.origen_atencion) "+
		" WHERE   a.ingreso = ? "+
		" and a.sub_cuenta = ? "+
		" AND a.tipo       = 'ADMIS' "+
		" AND d.activo     = 'S' ";


		String consultaCodigoAutorizacion= "select auto.codigo from det_autorizaciones auto where auto.autorizacion = ?";

		String consultaNumeroAutorizacion=" SELECT numero_autorizacion "+
		" FROM resp_autorizaciones "+
		" LEFT OUTER JOIN tipos_vigencia t "+
		" ON (t.codigo = tipo_vigencia) "+
		" LEFT OUTER JOIN cargos_usuarios cu "+
		" ON (cu.codigo          = cargo_pers_recibe ) "+
		" WHERE det_autorizacion = ?"; 


		String consultaNumeroverificacion="select v.NUMERO_VERIFICACION from verificaciones_derechos v  where sub_cuenta = ?";


		String consultarIdIngreso = "select id  from ingresos where ingresos.consecutivo=?";





		PreparedStatement ps = con.prepareStatement(consultaIdSubcuentaAsociada);
		ps.setInt(1, ingresoConsecutivo);
		ps.setInt(2, convenioId);

		ResultSet rs = ps.executeQuery();

		if(rs.next()){
			subcuenta= rs.getInt("sub_cuenta");
			if(!UtilidadTexto.isEmpty(subcuenta)){

				PreparedStatement ps6 = con.prepareStatement(consultarIdIngreso);
				ps6.setInt(1,ingresoConsecutivo );
				ResultSet rs6 = ps6.executeQuery();
				if(rs6.next()){
					codigoIngresoId = rs6.getInt("id");
				}
				rs6.close();
				ps6.close();

				PreparedStatement ps2 = con.prepareStatement(consultaNumeroDeAutorizacion);
				ps2.setInt(1,codigoIngresoId);
				ps2.setInt(2,subcuenta);
				ResultSet rs2 = ps2.executeQuery();
				if(rs2.next()){

					codigoAutorizacion = rs2.getInt("numero_aitorizacion");
					if(!UtilidadTexto.isEmpty(codigoAutorizacion)){
						PreparedStatement ps3 = con.prepareStatement(consultaCodigoAutorizacion);
						ps3.setInt(1, codigoAutorizacion);
						ResultSet rs3 = ps3.executeQuery();
						if(rs3.next()){
							numeroDeAutoriacion=rs3.getInt("codigo");
							PreparedStatement ps5  = con.prepareStatement(consultaNumeroAutorizacion);
							ps5.setInt(1, numeroDeAutoriacion);
							ResultSet rs5 = ps5.executeQuery();
							if(rs5.next()){
								numeroAutorizacion = rs5.getInt("numero_autorizacion");
							}
							rs5.close();
							ps5.close();
						}
						rs3.close();
						ps3.close();
					}
				}
				rs2.close();
				ps2.close();
			}
		}
		rs.close();
		ps.close();
		



		if(UtilidadTexto.isEmpty(numeroAutorizacion)){
			PreparedStatement pst4= con.prepareStatement(consultaNumeroverificacion);
			pst4.setInt(1, subcuenta);
			ResultSet rs4 = pst4.executeQuery();

			if(rs4.next()){
				numeroAutorizacion  = rs4.getInt("NUMERO_VERIFICACION");
			}
			rs4.close();
			pst4.close();
		}


		return numeroAutorizacion;
	}




	/**
	 * @author javrammo
	 * @see com.princetonsa.dao.CuentaDao#obtenerDatosCentroCostoXSolcitud(Connection, int)
	 */
	public static InfoDatosInt obtenerDatosCentroCostoXSolcitud(Connection con,int idSolicitud) throws IPSException{
		
		//consulta 
		String consulta = " select cc.codigo as codigo, cc.nombre as nombre from solicitudes sol INNER JOIN centros_costo cc ON(cc.codigo=sol.centro_costo_solicitante) where numero_solicitud = ? ";
		PreparedStatement ps = null;
		ResultSet rs = null;
		InfoDatosInt respuesta =  new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		
		try {
			ps = con.prepareStatement(consulta);
			ps.setInt(1, idSolicitud);
			rs = ps.executeQuery();
			if(rs.next()){
				respuesta.setCodigo(rs.getInt("codigo"));
				respuesta.setDescripcion(rs.getString("nombre"));
			}
			
		}catch (SQLException e){
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}catch (Exception e) {
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}finally{
			if(ps != null){
				try {
					ps.close();
				} catch (SQLException e) {
					throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
				}
			}
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
				}
			}
		}
		
		return respuesta;		
		
	}
	
	
	
}
