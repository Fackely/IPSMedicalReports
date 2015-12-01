package com.princetonsa.dao.sqlbase.carteraPaciente;

import java.sql.Connection;
import java.sql.Date;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import org.apache.log4j.Logger;


import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;

import java.util.HashMap;

import javax.swing.text.StyledEditorKit.BoldAction;

import util.UtilidadBD;
import util.historiaClinica.UtilidadesHistoriaClinica;

public class SqlBaseDocumentosGarantiaDao
{

	/**
	 * mensajes de arror
	 * */
	static Logger logger = Logger.getLogger(SqlBaseDocumentosGarantiaDao.class);
	
	
	/*-----------------------------------------------------
	 * ATRIBUTOS DOCUMENTOS GARANTIA 
	 * ---------------------------------------------------*/	
	
	/**
	 * Cadena de Consulta de Documentos Garantia
	 * */
	private static final String strCadenaConsultaDocumentosGarantia = "SELECT " +
			"ingreso AS ingreso," +
			"getconsecutivoingreso(ingreso) AS consecutivoingreso," +		
			"codigo_paciente AS codigopaciente," +
			"institucion AS institucion," +			
			"consecutivo AS consecutivo," +
			"anio_consecutivo AS anioconsecutivo," +
			"tipo_documento AS tipodocumento," +
			"getintegridaddominio(tipo_documento) AS nombretipodocumento,"+
			"to_char(fecha_generacion,'DD/MM/YYYY') AS fechageneracion," +
			"hora_generacion AS horageneracion," +
			"estado AS estado, " +
			"motivo_anulacion AS motivoanulacion," +
			"entidad_financiera AS entidadfinanciera," +
			"numero_cuenta AS numerocuenta," +
			"numero_documento AS numerodocumento," +
			"to_char(fecha_documento,'DD/MM/YYYY') AS fechadocumento," +
			"girador_documento AS giradordocumento," +
			"valor AS valor," +
			"clave_covinoc AS clavecovinoc," +
			"usuario_anula AS usuarioanula," +
			"fecha_anula AS fechaanula," +
			"hora_anula AS horaanula," +
			"getnombrecentatenxing(ingreso) AS centroatencion," +
			"'"+ConstantesBD.acronimoSi+"' AS estabd, " +
			"garantia_ingreso AS garantiaIngreso," +
			"cartera AS cartera " +
			"FROM documentos_garantia " +
			"WHERE ingreso=? AND institucion=? AND" +
			" garantia_ingreso = '"+ConstantesBD.acronimoSi+"'" ;
			
			
	
	/**
	 * Cadena de inserccion de Documentos en Garantia
	 * */
	private static final String strCadenaInsertarDocumentoGarantia = " INSERT INTO carterapaciente.documentos_garantia( " +
			"codigo_pk," +
			"ingreso," +
			"codigo_paciente,"+			
			"institucion," +
			"consecutivo," +
			"anio_consecutivo," +
			"tipo_documento," +
			"fecha_generacion," +
			"hora_generacion," +
			"estado," +
			"entidad_financiera," +
			"numero_cuenta," +
			"numero_documento," +
			"fecha_documento," +
			"girador_documento," +
			"valor," +
			"clave_covinoc," +
			"usuario_modifica," +
			"fecha_modifica," +
			"hora_modifica," +
			"garantia_ingreso," +
			"cartera," +
			"usuario_generacion,saldos_iniciales)" +
			"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
	
	
	/**
	 * Cadena de Actualizacion de Documentos de garantia 
	 * */
	private static final String strCadenaActualizacionDocumentoGarantia = "UPDATE carterapaciente.documentos_garantia " +
			"SET fecha_generacion = ?, " +
			"hora_generacion = ?, " +
			"estado = ?," +
			"motivo_anulacion = ?," +
			"entidad_financiera = ?," +
			"numero_cuenta = ?," +
			"numero_documento = ?," +
			"fecha_documento = ?," +
			"girador_documento = ?," +
			"valor = ?," +
			"clave_covinoc = ?," +
			"usuario_modifica = ?," +
			"fecha_modifica  = ?," +
			"hora_modifica = ?," +
			"usuario_anula = ?," +
			"fecha_anula = ?," +
			"hora_anula = ? " +
			"WHERE ingreso = ? " +
			"AND institucion = ? " +
			"AND consecutivo = ? " +
			"AND anio_consecutivo = ? " +
			"AND tipo_documento = ? ";	
	
	/**
	 * Cadena de Actualizacion del estado de ingreso 
	 * */
	private static final String strCadenaActualizarEstadoIngreso ="UPDATE manejopaciente.ingresos " +
			"SET estado='"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"'," +
			"usuario_modifica=?, fecha_modifica=?, hora_modifica=?  " +
			"WHERE id=? AND estado='"+ConstantesIntegridadDominio.acronimoEstadoIncompletoGarantias+"' ";
	
	/*-----------------------------------------------------
	 * FIN ATRIBUTOS DOCUMENTOS GARANTIA 
	 * ---------------------------------------------------*/	
	
	
	/*-----------------------------------------------------
	 * ATRIBUTOS REGISTRAR DEUDOR CO 
	 * ---------------------------------------------------*/
	
	/**
	 * Cadena de Verificacion de Documentos de Garantia por Ingreso
	 * verifica si el ingreso de paciente  
	 * */	
	private static final String strCadenaInfoDocsDependientes = "SELECT " +
			"ingreso," +
			"getconsecutivoingreso(ingreso) AS consecutivoingreso," +	
			"tipo_documento AS tipo_documento," +
			"estado FROM documentos_garantia " +
			"WHERE " +
			"ingreso = ? " +
			"AND codigo_paciente = ? " +
			"AND institucion = ? " ;	
	
	/**
	 * Cadena de Verificacion de Relacion de Deudor - Codeudor
	 * */
	private static final String strCadenaVerificacionDeudorCodeudor = "SELECT " +
			"ingreso AS ingreso," +
			"getconsecutivoingreso(ingreso) AS consecutivoingreso," +	
			"institucion AS institucion,"+
			"tipo_identificacion AS tipo_identificacion, " +
			"numero_identificacion AS numero_identificacion, " +
			"primer_nombre AS primer_nombre," +
			"segundo_nombre AS segundo_nombre," +
			"primer_apellido AS primer_apellido," +
			"segundo_apellido AS segundo_apellido," +
			"to_char(fecha_nacimiento,'DD/MM/YYYY') AS fecha_nacimiento, " +
			"tipo_deudorco AS tipo_deudor FROM carterapaciente.deudorco " +
			"WHERE ingreso = ? " +			
			"AND institucion = ? " +
			"AND clase_deudorco  = ?  ";
	
	
	/**
	 * Cadena de Inserccion del Deudor - Codeudor 
	 * */	
	private static final String strCadenaInserccionDeudorCo ="INSERT INTO carterapaciente.deudorco (" +
			"codigo_pk," +
			"ingreso," +
			"codigo_paciente," +
			"institucion," +
			"clase_deudorco," +
			"tipo_deudorco," +
			"tipo_identificacion," +
			"numero_identificacion," +
			"codigo_pais," +
			"codigo_departamento," +
			"codigo_ciudad," +
			"primer_nombre," +
			"segundo_nombre," +
			"primer_apellido," +
			"segundo_apellido," +
			"direccion_reside," +
			"codigo_pais_reside," +
			"codigo_departamento_reside," +
			"codigo_ciudad_reside," +
			"codigo_barrio_reside," +
			"telefono_reside," +
			"fecha_nacimiento," +
			"relacion_paciente," +
			"tipo_ocupacion," +
			"ocupacion," +
			"empresa," +
			"cargo," +
			"antiguedad," +
			"direccion_oficina," +
			"telefono_oficina," +
			"nombres_referencia," +
			"direccion_referencia," +
			"telefono_referencia," +
			"observaciones," +
			"fecha_verificacion," +
			"usuario_verificacion," +
			"observaciones_verificacion," +
			"fecha_veri_riesgos," +
			"usuario_veri_riesgos," +
			"observaciones_veri_riesgos," +			
			"usuario_modifica," +
			"fecha_modifica," +
			"hora_modifica) " +
			"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
			
	
	
	/**
	 * Cadena de Actualizacion del Deudor - Codeudor 
	 * */
	private static final String strCadenaActualizacionDeudorCo ="UPDATE carterapaciente.deudorco SET " +		
			"tipo_deudorco= ?, " +
			"tipo_identificacion = ?," +
			"numero_identificacion = ?," +
			"codigo_pais = ?," +
			"codigo_departamento = ?," +
			"codigo_ciudad = ?," +
			"primer_nombre = ?," +
			"segundo_nombre = ?," +
			"primer_apellido = ?," +
			"segundo_apellido = ?," +
			"direccion_reside = ?," +
			"codigo_pais_reside = ?," +
			"codigo_departamento_reside = ?," +
			"codigo_ciudad_reside = ?," +
			"codigo_barrio_reside = ?," +
			"telefono_reside = ?," +
			"fecha_nacimiento = ?," +
			"relacion_paciente = ?," +
			"tipo_ocupacion = ?, " +			
			"ocupacion = ?," +
			"empresa = ?," +
			"cargo = ?," +
			"antiguedad = ?," +
			"direccion_oficina = ?," +
			"telefono_oficina = ?," +
			"nombres_referencia = ?," +
			"direccion_referencia = ?," +
			"telefono_referencia = ?," +
			"observaciones = ?," +			
			"usuario_modifica = ?," +
			"fecha_modifica = ?," +
			"hora_modifica = ? " +
			"WHERE ingreso = ? AND " +			
			"institucion = ? AND " +			
			"clase_deudorco = ? ";
	
	/**
	 * Cadena de Actualizacion de la Verificacion
	 * */
	private static final String strCadenaActualizacionVerificacion ="UPDATE carterapaciente.deudorco SET " +
			"fecha_verificacion = ?,"+
			"usuario_verificacion = ?,"+
			"observaciones_verificacion = ?,"+			
			"fecha_veri_riesgos = ?,"+
			"usuario_veri_riesgos = ?,"+
			"observaciones_veri_riesgos = ?,"+		
			"usuario_modifica_verificacion = ?,"+
			"fecha_modifica_verificacion = ?,"+
			"hora_modifica_verificacion = ? "+			
			"WHERE ingreso = ? AND " +			
			"institucion = ? AND " +			
			"clase_deudorco = ? ";
		
	
	/**
	 * Cadena de Actualizacion de la Cuenta 
	 * */
	private static final String strCadenaActualizacionCuenta ="UPDATE manejopaciente.cuentas SET " +
			"codigo_responsable_paciente = (SELECT rp.codigo FROM manejopaciente.responsables_pacientes rp WHERE rp.tipo_identificacion = ?" +
			"AND rp.numero_identificacion = ? ) " +
			"WHERE id= ? ";					
	
	
	/**
	 * Cadena de Consulta del Deudor - Codeudor
	 * */
	private static final String strCadenaConsultaDeudorCo  ="SELECT " +			
			"d.ingreso AS ingreso," +
			"getconsecutivoingreso(d.ingreso) AS consecutivoingreso," +	
			"d.codigo_paciente AS codigo_paciente," +
			"d.institucion AS institucion," +			
			"d.clase_deudorco AS clase_deudor_co," +
			"d.tipo_deudorco AS tipo_deudor_co,"  +
			"d.tipo_identificacion AS tipo_identificacion," +
			"d.numero_identificacion AS numero_identificacion," +
			"d.codigo_pais AS codigo_pais," +
			"getdescripcionpais(d.codigo_pais) AS descripcion_pais," +
			"d.codigo_departamento AS codigo_departamento," +
			"getnombredepto(d.codigo_pais,d.codigo_departamento) AS descripcion_departamento," +			
			"d.codigo_departamento || '"+ConstantesBD.separadorSplit+"' || d.codigo_ciudad AS codigo_ciudad," +
			"getnombreciudad(d.codigo_pais,d.codigo_departamento,d.codigo_ciudad) AS descripcion_ciudad," +
			"d.primer_nombre AS primer_nombre," +
			"d.segundo_nombre AS segundo_nombre," +
			"d.primer_apellido AS primer_apellido," +
			"d.segundo_apellido AS segundo_apellido," +
			"d.direccion_reside AS direccion_reside," +
			"d.codigo_pais_reside AS codigo_pais_reside," +
			"getdescripcionpais(d.codigo_pais_reside) AS descripcion_pais_reside," +
			"d.codigo_departamento_reside AS codigo_departamento_reside," +
			"getnombredepto(d.codigo_pais_reside,d.codigo_departamento_reside) AS descripcion_departamento_resid," +
			"d.codigo_departamento_reside || '"+ConstantesBD.separadorSplit+"' || d.codigo_ciudad_reside AS codigo_ciudad_reside," +
			"getnombreciudad(d.codigo_pais_reside,d.codigo_departamento_reside,d.codigo_ciudad_reside) AS descripcion_ciudad_reside," +
			"d.codigo_barrio_reside AS codigo_barrio_reside," +
			"getdescripcionbarrio(d.codigo_barrio_reside) AS descripcion_barrio_reside," +
			"d.telefono_reside AS telefono_reside," +
			"to_char(d.fecha_nacimiento,'DD/MM/YYYY') AS fecha_nacimiento," +
			"d.relacion_paciente AS relacion_paciente," +
			"d.tipo_ocupacion AS tipo_ocupacion," +
			"d.ocupacion AS ocupacion," +
			"d.empresa AS empresa," +
			"d.cargo AS cargo," +
			"d.antiguedad AS antiguedad," +
			"d.direccion_oficina AS direccion_oficina," +
			"d.telefono_oficina AS telefono_oficina," +
			"d.nombres_referencia AS nombres_referencia," +
			"d.direccion_referencia AS direccion_referencia," +
			"d.telefono_referencia AS telefono_referencia," +
			"d.observaciones AS observaciones," +
			"to_char(d.fecha_verificacion,'DD/MM/YYYY') AS fecha_verificacion," +
			"d.usuario_verificacion AS usuario_verificacion," +
			"d.observaciones_verificacion AS observaciones_verificacion," +			
			"to_char(d.fecha_veri_riesgos,'DD/MM/YYYY') AS fecha_veri_riesgos," +
			"d.usuario_veri_riesgos AS usuario_veri_riesgos," +
			"d.observaciones_veri_riesgos AS observaciones_veri_riesgos," +
			"d.usuario_modifica AS usuario_modifica," +
			"d.fecha_modifica AS fecha_modifica," +
			"d.hora_modifica AS hora_modifica," +
			"'"+ConstantesBD.acronimoSi+"' AS estabd, " +
			"d.codigo_pk as codigo_pk " +
			"FROM carterapaciente.deudorco d " ;
			
	
	
	/**
	 * Cadena de Eliminacion de informacion del DeudorCo 
	 * */
	private static final String strCadenaEliminacionDeudorCo = "DELETE FROM carterapaciente.deudorco " +
			"WHERE ingreso = ? " +
			"AND codigo_paciente = ? " +
			"AND institucion = ? " +
			"AND clase_deudorco  = ?  ";
	
	
	
	/**
	 * Cadena de consulta de la informacion del paciente como DeudorCo
	 * */
	private static final String strCadenaConsultaPaciente = "SELECT " +
			"pe.codigo AS codigo_paciente, " +
			"pe.tipo_identificacion AS tipo_identificacion," +
			"pe.numero_identificacion AS numero_identificacion," +
			"pe.codigo_pais_nacimiento AS codigo_pais," +
			"pe.codigo_departamento_nacimiento  AS codigo_departamento," +
			"pe.codigo_ciudad_nacimiento AS codigo_ciudadold," +
			"pe.codigo_departamento_nacimiento || '"+ConstantesBD.separadorSplit+"' || pe.codigo_ciudad_nacimiento AS codigo_ciudad," +
			"pe.primer_nombre AS primer_nombre," +
			"pe.segundo_nombre AS segundo_nombre," +
			"pe.primer_apellido AS primer_apellido," +
			"pe.segundo_apellido AS segundo_apellido," +
			"pe.direccion AS direccion_reside," +
			"pe.codigo_pais_vivienda AS codigo_pais_reside," +
			"pe.codigo_departamento_vivienda AS codigo_departamento_reside," +
			"codigo_ciudad_vivienda AS codigo_ciudad_resideold," +
			"pe.codigo_departamento_vivienda || '"+ConstantesBD.separadorSplit+"' || codigo_ciudad_vivienda AS codigo_ciudad_reside," +
			"pe.codigo_barrio_vivienda AS codigo_barrio_reside," +
			"getdescripcionbarrio(pe.codigo_barrio_vivienda) AS descripcion_barrio_reside," +			
			"pe.telefono AS telefono_reside, " +
			"to_char(pe.fecha_nacimiento,'DD/MM/YYYY') AS fecha_nacimiento," +
			"to_char(pe.fecha_nacimiento,'"+ConstantesBD.formatoFechaBD+"') AS fecha_nacimientoold, " +
			"'' AS relacion_paciente " +
			"FROM personas pe  "; 
			
	
	/**
	 * Cadena de consulta de la informacion del Responsable de Paciente
	 * */
	private static final String strCadenaConsultaResponsablePaciente = "SELECT " +
			"codigo AS codigo_responsable_paciente, " +
			"tipo_identificacion AS tipo_identificacion," +
			"numero_identificacion AS numero_identificacion," +
			"codigo_pais_doc AS codigo_pais," +
			"codigo_depto_doc AS codigo_departamento," +
			"codigo_ciudad_doc AS codigo_ciudadold, " +
			"codigo_depto_doc || '"+ConstantesBD.separadorSplit+"' || codigo_ciudad_doc AS codigo_ciudad, " +
			"primer_nombre AS primer_nombre, " +
			"segundo_nombre AS segundo_nombre, " +
			"primer_apellido AS primer_apellido, " +
			"segundo_apellido AS segundo_apellido, " +
			"direccion AS direccion_reside," +
			"codigo_pais AS codigo_pais_reside, " +
			"codigo_depto AS codigo_departamento_reside," +
			"codigo_depto || '"+ConstantesBD.separadorSplit+"' ||codigo_ciudad AS codigo_ciudad_reside," +
			"codigo_ciudad AS codigo_ciudad_resideold," +
			"codigo_barrio AS codigo_barrio_reside," +
			"getdescripcionbarrio(codigo_barrio) AS descripcion_barrio_reside," +
			"telefono AS telefono_reside," +
			"to_char(fecha_nacimiento,'DD/MM/YYYY') AS fecha_nacimiento," +
			"to_char(fecha_nacimiento,'"+ConstantesBD.formatoFechaBD+"') AS fecha_nacimientoold, " +
			"relacion_paciente AS relacion_paciente " +
			"FROM manejopaciente.responsables_pacientes ";
	
	/*
	 * Cadena de actualizacion de la informacion del responsable paciente
	 * */
	private static final String strCadenaActualizacionResponsablePaciente ="UPDATE manejopaciente.responsables_pacientes " +
			"SET codigo_pais_doc = ?," +
			"codigo_depto_doc = ?," +
			"codigo_ciudad_doc = ?," +
			"codigo_pais = ?," +
			"codigo_depto = ?," +
			"codigo_ciudad= ? " +
			"WHERE " +
			"numero_identificacion = ? AND " +
			"tipo_identificacion = ?  AND " +			
			"(codigo_pais_doc IS NULL OR codigo_pais IS NULL) ";
			 
		
	/*-----------------------------------------------------
	 * FIN ATRIBUTOS REGISTRAR DEUDOR CO 
	 * ---------------------------------------------------*/	
	
	/*-----------------------------------------------------
	 * ATRIBUTOS CONSULTAR INGRESOS 
	 * ---------------------------------------------------*/
	
	/**
	 * Cadena de Consulta Ingresos
	 * Ingreso = abierto,cerrado o incompleto por garantias
	 * cuenta asociada = activa, asociada cuenta final null, facturada parcial
	 * */
	private static final String strCadenaConsultaIngresos = "SELECT " +
			"i.id AS ingreso," +
			"i.preingreso AS indpre," +
			"CASE WHEN i.reingreso IS NOT NULL THEN i.reingreso ELSE 0 END AS indrei," +
			"getconsecutivoingreso(i.id) AS consecutivoingreso," +	
			"i.codigo_paciente AS codigo_paciente," +
			"i.institucion AS institucion," +
			"i.estado AS estado," +
			"to_char(i.fecha_ingreso,'DD/MM/YYYY') || ' ' || i.hora_ingreso AS fecha_ingreso," +
			"to_char(i.fecha_egreso,'DD/MM/YYYY')  || ' ' || i.hora_egreso  AS fecha_egreso," +
			"c.id AS numero_cuenta," +
			"c.estado_cuenta AS estado_cuenta," +
			"ec.nombre AS descripcion_cuenta, " +
			"c.via_ingreso AS via_ingreso, " +
			"vi.nombre AS descripcion_via_ingreso, " +
			"CASE WHEN c.codigo_responsable_paciente IS NULL THEN "+ConstantesBD.codigoNuncaValido+" " +
			"ELSE c.codigo_responsable_paciente END AS codigo_responsable_paciente," +
			"CASE WHEN c.codigo_responsable_paciente IS NOT NULL THEN re.tipo_identificacion || '"+ConstantesBD.separadorSplit+"' || re.numero_identificacion " +
			"ELSE '"+ConstantesBD.codigoNuncaValido+"' END AS datos_responsable_paciente," +			
			"'' AS clase_deudor_co " +		
			"FROM ingresos i INNER JOIN cuentas c ON (c.id_ingreso=i.id) " +
			"				 INNER JOIN vias_ingreso vi ON (vi.codigo=c.via_ingreso) " +
			"				 INNER JOIN estados_cuenta ec ON (ec.codigo=c.estado_cuenta) " +
			"				 LEFT JOIN manejopaciente.responsables_pacientes re ON (re.codigo=c.codigo_responsable_paciente) " +
			"WHERE " +
			"i.codigo_paciente=? AND i.estado IN ('"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"','"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"','"+ConstantesIntegridadDominio.acronimoEstadoIncompletoGarantias+"') ";
	
	
	/**
	 * Cadena de Validacion de Cuenta Valida 
	 * */
	private static final String strCadenaValidacionCuentaValida = "SELECT " +
			"COUNT(1) " +
			"FROM cuentas c " +
			"INNER JOIN centros_costo cc ON (cc.codigo = c.area AND cc.centro_atencion = ?) " +
			"WHERE c.id = ? AND (c.estado_cuenta IN ("+ConstantesBD.codigoEstadoCuentaActiva+","+ConstantesBD.codigoEstadoCuentaFacturadaParcial+") " +
			"OR (c.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaAsociada+" AND c.id " +
					"IN (select ac.cuenta_inicial from asocios_cuenta ac WHERE ac.cuenta_inicial=c.id AND ac.cuenta_final is null)))";	
	
	
	
	
	
	
//***********CONSULTAS SOBRE LOS DOCUEMENTOS DE GARANTIA Y CONSULTA E INSERCION DEL HISTORIAL************* 	
	
	/**
	 * @author Jhony Alexander Duque A.
	 * Cadena que consulta todos los documentos de garantia de un paciente
	 * en todos sus ingresos
	 * */
	private static final String strCadenaConsultaIngresosydocgarantia = "SELECT " +
			"dg.ingreso AS ingreso," +
			"i.preingreso AS indpre," +
			"CASE WHEN i.reingreso IS NOT NULL THEN i.reingreso ELSE 0 END AS indrei," +
			"getconsecutivoingreso(dg.ingreso) AS consecutivoingreso," +
			"dg.institucion AS institucion," +
			"getnombrecentatenxing(dg.ingreso) AS centroatencion,"+
			"dg.consecutivo AS consecutivo,"+
			"dg.anio_consecutivo AS anioconsecutivo,"+
			"dg.consecutivo ||' '||dg.anio_consecutivo AS numeroducumento,"+
			"getintegridaddominio(dg.tipo_documento) AS nombretipodocumento,"+
			"dg.tipo_documento AS tipodocumento,"+
			"dg.estado AS acronimoestado,"+
			"getintegridaddominio(dg.estado) AS estado,"+
			"to_char(dg.fecha_generacion,'DD/MM/YYYY') AS fechageneracion, " +
			"c.id as numero_cuenta, " +
			"dg.garantia_ingreso AS garantiaIngreso," +
			"dg.cartera AS cartera " +
			"FROM documentos_garantia dg " +
				"INNER JOIN ingresos i ON (dg.ingreso=i.id) " +
				"INNER JOIN cuentas c ON(c.id_ingreso = i.id) " +
			"WHERE " +
			"dg.codigo_paciente = ? and manejopaciente.getcuentafinalasocioint(dg.ingreso,c.id) is null AND" +
			" dg.garantia_ingreso = '"+ConstantesBD.acronimoSi+"'";
	
	
	
	/**
	 * Consulta por Rangos Documentos de Garantia
	 * @author Jhony Alexander Duque A.
	 */
	private static final String strCadenaConsultaRangosDocGarantia = "SELECT "+
				"dg.ingreso AS ingreso," +
				"i.preingreso AS indpre," +
				"CASE WHEN i.reingreso IS NOT NULL THEN i.reingreso ELSE 0 END AS indrei," +
				"getconsecutivoingreso(dg.ingreso) AS consecutivoingreso," +
				"dg.institucion AS institucion," +
				"getnomcentroatencion(i.centro_atencion) AS centroatencion,"+
				"dg.consecutivo AS consecutivo,"+
				"dg.anio_consecutivo AS anioconsecutivo,"+
				"dg.consecutivo ||' '||dg.anio_consecutivo AS numeroducumento,"+
				"getintegridaddominio(dg.tipo_documento) AS nombretipodocumento,"+
				"dg.tipo_documento AS tipodocumento,"+
				"dg.estado AS acronimoestado,"+
				"getintegridaddominio(dg.estado) AS estado,"+
				"to_char(dg.fecha_generacion,'DD/MM/YYYY') AS fechageneracion," +
				"p.tipo_identificacion AS acronimoidentpac,"+
				"p.numero_identificacion AS identificacionpaciente,"+
				"administracion.getnombremedico (p.codigo) AS nombrepaciente,"+
				"p.codigo AS codigopaciente, "+
				"dg.garantia_ingreso AS garantiaIngreso," +
				"dg.cartera AS cartera " +
				"FROM documentos_garantia dg INNER JOIN personas p 	ON (p.codigo = dg.codigo_paciente) "+
				"							 INNER JOIN ingresos i 	ON (i.id = dg.ingreso) ";
				
	
	
	
	
	
	/**
	 * @author adicionado por Jhony Alexander Duque A.
	 * Cadena que se encarga de consultar los datos que van a ser ingresados 
	 * al historial
	 * */		
	private static final String strCadenaConsultaDatosHistorial ="SELECT "+
			"sc.ingreso AS ingreso,"+
			"getconsecutivoingreso(sc.ingreso) AS consecutivoingreso," +
			"c.institucion As institucion,"+
			"sc.convenio AS convenio,"+
			"tc.clasificacion AS clasificacionconvenio,"+
			"c.tipo_regimen AS tiporegimen,"+
			"sc.nro_carnet AS numerocarnet,"+
			"sc.nro_poliza AS numeropoliza,"+
			"sc.clasificacion_socioeconomica AS clasificacionsec,"+
			"c.plan_beneficios AS planbeneficios "+
			"FROM sub_cuentas sc "+
			"INNER JOIN convenios c ON (c.codigo = sc.convenio) " +
			"LEFT OUTER JOIN tipos_convenio tc ON (tc.codigo = c.tipo_convenio) "+
			"WHERE "+
			"sc.ingreso = ? " +
			"and sc.nro_prioridad=1";//MT 2827 Cargaba mas de un registro y la tabla HISTORIAL_CONVENIOSXINGRESO no permite repetir el ingreso (codigo_pk)
	
	/**
	 * @author adicionado por Jhony Alexander Duque A.
	 * Cadena que se encarga de consultar los datos que van a ser ingresados 
	 * al historial
	 * */
	private static final String strCadenaInsercionHistorial ="INSERT INTO historial_conveniosxingreso (" +
			"ingreso," +
			"institucion," +
			"convenio,"+
			"tipo_documento,"+
			"clasificacion_convenio," +
			"tipo_regimen," +
			"nro_carnet," +
			"nro_poliza," +
			"clasificacion_socioeconomica," +
			"plan_beneficios) " +
			"VALUES (?,?,?,?,?,?,?,?,?,?)";
	
	
	/**
	 * Cadena consulta si existe historial para el ingreso y tipo de documento
	 * */
	private static final String strCadenaElimincacionHistorial= "DELETE FROM historial_conveniosxingreso " +
			"WHERE ingreso = ? AND tipo_documento = ? ";
	
	
//*********************** FIN CINSULTAS DOCUMENTOS GRANTIA E HISTORIAL****************************	
	
	
	




 
			
	/*-----------------------------------------------------
	 * FIN ATRIBUTOS CONSULTAR INGRESOS 
	 * ---------------------------------------------------*/

	
	/*-----------------------------------------------------
	 * DEFINICION DE INDICES 
	 * ---------------------------------------------------*/
	
	/**
	 * Vector String indices Mapa DeudorCo 
	 * */
	private static String[] indicesDeudorCo = {"ingreso","consecutivoingreso","codigoPaciente","institucion","claseDeudorCo","tipoDeudorCo","tipoIdentificacion","numeroIdentificacion",
										"codigoPais","descripcionPais","codigoDepartamento","descripcionDepartamento","codigoCiudad","descripcionCiudad","primerNombre","segundoNombre","primerApellido",
										"segundoApellido","direccionReside","codigoPaisReside","descripcionPaisReside","codigoDepartamentoReside","descripcionDepartamentoReside",
										"codigoCiudadReside","descripcionCiudadReside","codigoBarrioReside","descripcionBarrioReside","telefonoReside",
										"fechaNacimiento","relacionPaciente","tipoOcupacion","ocupacion","empresa","cargo","antiguedad","direccionOficina",
										"telefonoOficina","nombresReferencia","direccionReferencia","telefonoReferencia","observaciones","fechaVerificacion",
										"usuarioVerificacion","observacionesVerificacion","fechaVeriRiesgos","usuarioVeriRiesgos","observacionesVeriRiesgos",
										"usuarioModifica","fechaModifica","horaModifica","estabd"};
	
	
	/**
	 * Vector String indices Mapa Ingresos.
	 * */
	private static String[] indicesIngresos = {"ingreso_","indpre_","indrei_","consecutivoingreso_","codigoPaciente_","institucion_","estado_","fechaIngreso_","fechaEgreso_","numeroCuenta_",
										"estadoCuenta_","descripcionCuenta_","viaIngreso_","descripcionViaIngreso_","codigoResponsablePaciente_","datosResponsablePaciente_","claseDeudorCo_"};	
	
	/**
	 * Vector de String indices Mapa Documentos Garantia .
	 * */
	private static String[] indicesListarDocumentos ={"consecutivo_","anioconsecutivo_","tipodocumento_","nombretipodocumento_","fechageneracion_","horageneracion_","estado_","estabd_"};
	
	/**
	 * @author Jhony Alexander Duque A.
	 * Vector de String indices Mapa del listado de documentos de garantia X ingreso.
	 */
	private static String[] indicesListarDocGarantiaXIngreso = {"ingreso_","indpre_","consecutivoingreso_","institucion_","centroatencion_","consecutivo_","anioconsecutivo_","numeroducumento_","tipodocumento_","acronimoestado_","nombretipodocumento_","estado_","fechageneracion_"};
	
	/**
	 * @author Jhony Alexander Duque A.
	 * Vector de String indices Mapa del historial.
	 */
	private static String [] indicesListarHistorial = {"ingreso_","consecutivoingreso_","institucion_","convenio_","clasificacionconvenio_","tiporegimen_","numerocarnet_","numeropoliza_","clasificacionsec_","planbeneficios_"};

	/**
	 * @author Jhony Alexander Duque A.
	 * Vector de String indices Mapa del lsitado de la busqueda por rangos.
	 */
	private static String [] indicesListarBusquedaXRangos = {"ingreso_","indpre_","indrei_","consecutivoingreso_","institucion_","centroatencion_","consecutivo_","anioconsecutivo_","numeroducumento_","nombretipodocumento_","tipodocumento_","acronimoestado_","estado_","fechageneracion_","acronimoidentpac_","identificacionpaciente_","nombrepaciente_"};

	/*-----------------------------------------------------
	 * METODOS DOCUMENTOS GARANTIA 
	 * ---------------------------------------------------*/
	
	/**
	 * consulta lista de Garantia Generados
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static HashMap consultarListaDocumentosGarantia(Connection con,
											HashMap parametros)
	{
		HashMap mapa = new HashMap();
		
		try
		{
			String consulta = strCadenaConsultaDocumentosGarantia;
			//logger.info("mapa parametros=> "+parametros);
			
			if (parametros.containsKey("soloVigente"))
			if(UtilidadTexto.getBoolean(parametros.get("soloVigente")+""))
				consulta += " AND estado = '"+ConstantesIntegridadDominio.acronimoPolizaVigente+"' ";
			if (parametros.containsKey("tipodocumento"))
				consulta += "AND tipo_documento = '"+parametros.get("tipodocumento")+"' ";
			
			if (parametros.containsKey("consecutivo"))
				consulta += "AND consecutivo = '"+parametros.get("consecutivo")+"' ";
			if (parametros.containsKey("anioconsecutivo"))
				consulta += "AND anio_consecutivo = '"+parametros.get("anioconsecutivo")+"' ";
			
			
			consulta += " ORDER BY tipo_documento ASC, consecutivo DESC ";
			
			logger.info("CONSULTA DE LOS DOCUMENTOS=> "+consulta);
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("ingreso").toString()));
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			
			if (parametros.containsKey("tipodocumento"))
				mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),false,true);
			else
				mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
				
		mapa.put("INDICES_MAPA", indicesListarDocumentos);
		return mapa;
	}
	
	
	/**
	 * Consulta por Rangos los documentos de garantia existentes
	 * @author Jhony Alexander Duque A.
	 * @param Connection con
	 * @param HashMap parametros
	 * en el hashmap parametros deben venir los criterios de la busqueda; estos datos pueden o no venir.
	 * institucion --> Requerido
	 * centroatencion --> Opcional
	 * fechainicial --> Opcional
	 * fechafinal --> Opcional
	 * onsecutivo --> Opcional
	 * tipodocumento --> Opcional 
	 * estadodoc --> Opcional
	 */
	public static HashMap consultaXRangosDocumentosGarantia (Connection connection, HashMap parametros)
	{
		
		
		HashMap mapa = new HashMap();
		String consulta = strCadenaConsultaRangosDocGarantia+" WHERE dg.garantia_ingreso = '"+ConstantesBD.acronimoSi+"'";
		//logger.info(" consultaXRangosDocumentosGarantia mapa parametros=> "+parametros);
		try {
			if(parametros.containsKey("institucion"))
				consulta+= " AND dg.institucion = "+parametros.get("institucion").toString();
			
			if(parametros.containsKey("centroatencion"))
				if (!parametros.get("centroatencion").equals(""))
					consulta+= " AND i.centro_atencion = "+parametros.get("centroatencion").toString();
			
			if(parametros.containsKey("fechainicial"))
				if (!parametros.get("fechainicial").equals(""))
					consulta+= " AND dg.fecha_generacion >='"+Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechainicial").toString()))+"' ";
			
			if(parametros.containsKey("fechafinal"))
				if (!parametros.get("fechafinal").equals(""))
					consulta+= " AND dg.fecha_generacion <='"+Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechafinal").toString()))+"' ";
			
			if(parametros.containsKey("consecutivo"))
				if (!parametros.get("consecutivo").equals(""))
					consulta+= " AND dg.consecutivo ='"+parametros.get("consecutivo").toString()+"' ";
			
			if(parametros.containsKey("tipodocumento"))
				if (!parametros.get("tipodocumento").equals(""))
					consulta+= " AND dg.tipo_documento ='"+parametros.get("tipodocumento").toString()+"' ";
			
			if(parametros.containsKey("estadodoc"))
				if (!parametros.get("estadodoc").equals(""))
					consulta+= " AND dg.estado ='"+parametros.get("estadodoc").toString()+"' ";
			
			consulta += " ORDER BY dg.ingreso DESC";
		//	logger.info("CONSULTA DE LOS DOCUMENTOS X RANGOS=> "+consulta);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			
			
			}
		catch (SQLException e) 
		{
			e.printStackTrace();			
		}
		mapa.put("INDICES_MAPA", indicesListarBusquedaXRangos);
		logger.info("\n\n \n **** el valor del hashmap"+mapa+"\n\n\n");
		return mapa;
	}
	
	
	
	
	/**
	 * @autor Adicionado por Jhony Alexander Duque A. 
	 * Metodo encargado de consultar los ingresos que tienen documentos de garantia
	 * @param Connection con
	 * @param HashMap parametros 
	 */
	public static HashMap consultarListadoingresosDocumentosGarantia (Connection connection, HashMap parametros)
	{
		HashMap mapa = new HashMap();
		try
		{
			String consulta = strCadenaConsultaIngresosydocgarantia;
		//	logger.info("mapa parametros=> "+parametros);
		
			consulta += " ORDER BY dg.ingreso DESC";
		//	logger.info("CONSULTA DE LOS DOCUMENTOS=> "+consulta);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get("codigopaciente").toString()));
			
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		mapa.put("INDICES_MAPA", indicesListarDocGarantiaXIngreso);
		return mapa;
	}
	
	/**
	 * @autor Adicionado por Jhony Alexander Duque A. 
	 * Metodo encargado de Consultar los datos del historial
	 * @param Connection con
	 * @param HashMap parametros 
	 */
	public static HashMap consultarDatosHistorial (Connection connection, HashMap parametros)
	{		
		HashMap datosHistorial = new HashMap();
		try {
			 	PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(strCadenaConsultaDatosHistorial,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			 	ps.setInt(1, Utilidades.convertirAEntero(parametros.get("ingreso").toString()));
			 	datosHistorial = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));	
			 	
			 	
			} 
		catch (SQLException e) 
			{
				e.printStackTrace(); 
			}
		
		
		datosHistorial.put("INDICES_MAPA", indicesListarHistorial);
		return datosHistorial;
	}
	
	
	
	/**
	 * @autor Adicionado por Jhony Alexander Duque A. 
	 * Metodo encargado de Insertar los datos del historial
	 * @param Connection con
	 * @param HashMap parametros 
	 */
	public static boolean insertarHistorial (Connection connection, HashMap parametros )
	{
		
		
		try {				
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(strCadenaElimincacionHistorial,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setObject(1,parametros.get("ingreso"));
				ps.setObject(2,parametros.get("tipodocumento"));				
				ps.executeUpdate();								
				
				ps =  new PreparedStatementDecorator(connection.prepareStatement(strCadenaInsercionHistorial,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				for (int i=0;Utilidades.convertirAEntero(parametros.get("numRegistros").toString())>i;i++)
				{
					ps.setInt(1, Utilidades.convertirAEntero(parametros.get("ingreso_"+i).toString()));
					ps.setInt(2, Utilidades.convertirAEntero(parametros.get("institucion_"+i).toString()));
					ps.setInt(3, Utilidades.convertirAEntero(parametros.get("convenio_"+i).toString()));
					ps.setString(4,parametros.get("tipodocumento").toString());
					//se valida para evitar que el campo que venga vacio sea pasado a entero
					if (parametros.get("clasificacionconvenio_"+i).toString().equals(""))
						ps.setNull(5,Types.NULL);
					else
						ps.setInt(5, Utilidades.convertirAEntero(parametros.get("clasificacionconvenio_"+i).toString()));
					
					ps.setString(6, parametros.get("tiporegimen_"+i).toString());
					ps.setString(7, parametros.get("numerocarnet_"+i).toString());
					ps.setString(8, parametros.get("numeropoliza_"+i).toString());
					
					//se valida para evitar que el campo que venga vacio sea pasado a entero
					if (parametros.get("clasificacionsec_"+i).toString().equals(""))
						ps.setNull(9,Types.NULL);
					else
					ps.setInt(9, Utilidades.convertirAEntero(parametros.get("clasificacionsec_"+i).toString()));
					ps.setString(10, parametros.get("planbeneficios_"+i).toString());
					
					if (ps.executeUpdate()<=0)
						return false;
				}
					return true;
			} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		
		
		return false;
	}
	
	/**
	 * Inserta un nuevo registro de tipo Documentos en Garantia
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static int insertarDocumentoGarantia(Connection con, 
													HashMap parametros)
	{
		Utilidades.imprimirMapa(parametros);
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strCadenaInsertarDocumentoGarantia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			int codigoPK=UtilidadBD.obtenerSiguienteValorSecuencia(con, "carterapaciente.seq_docgarantia");
			ps.setInt(1, codigoPK);
			if(parametros.containsKey("ingreso"))
				ps.setInt(2,Utilidades.convertirAEntero(parametros.get("ingreso").toString()));
			else
				ps.setObject(2, null);
			if(parametros.containsKey("codigopaciente"))
				ps.setInt(3,Utilidades.convertirAEntero(parametros.get("codigopaciente").toString()));
			else
				ps.setObject(3, null);
			ps.setInt(4,Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			ps.setString(5,parametros.get("consecutivo").toString().trim());
			
			if(!parametros.get("anioconsecutivo").toString().trim().equals(""))
				ps.setString(6,parametros.get("anioconsecutivo").toString());
			else
				ps.setString(6," ");
			
			ps.setString(7,parametros.get("tipodocumento").toString());
			ps.setDate(8,Date.valueOf(parametros.get("fechageneracion").toString()));
			ps.setString(9,parametros.get("horageneracion").toString());
			ps.setString(10,parametros.get("estado").toString());
			
			if(parametros.containsKey("entidadfinanciera"))
				ps.setString(11,parametros.get("entidadfinanciera").toString());
			else
				ps.setNull(11,Types.NULL);
			
			
			if(parametros.containsKey("numerocuenta"))
				ps.setString(12,parametros.get("numerocuenta").toString());
			else
				ps.setNull(12,Types.NULL);
			
			if(parametros.containsKey("numerodocumento"))			
				ps.setString(13,parametros.get("numerodocumento").toString());
			else
				ps.setNull(13,Types.NULL);
			
			if(parametros.containsKey("fechadocumento"))
				ps.setDate(14,Date.valueOf(parametros.get("fechadocumento").toString()));
			else
				ps.setNull(14,Types.NULL);
			
			if(parametros.containsKey("giradordocumento"))
				ps.setString(15,parametros.get("giradordocumento").toString());
			else
				ps.setNull(15,Types.NULL);
			
			if(parametros.containsKey("valor")&&Utilidades.convertirADouble(parametros.get("valor").toString())>=0)
				ps.setDouble(16,Utilidades.convertirADouble(parametros.get("valor").toString()));
			else
				ps.setNull(16,Types.NULL);
			
			if(parametros.containsKey("clavecovinoc"))
				ps.setString(17,parametros.get("clavecovinoc").toString());
			else
				ps.setNull(17,Types.NULL);
			
			
			ps.setString(18,parametros.get("usuariomodifica").toString());
			ps.setDate(19,Date.valueOf(parametros.get("fechamodifica").toString()));
			ps.setString(20,parametros.get("horamodifica").toString());
			
			ps.setString(21,parametros.get("garantiaIngreso").toString());
			ps.setString(22,parametros.get("cartera").toString());
			ps.setString(23, parametros.get("usuariomodifica").toString());
			if(parametros.containsKey("saldoInicialCartera"))
			{
				
				ps.setString(24, parametros.get("saldoInicialCartera")+"");
			}
			else
			{
				ps.setString(24, ConstantesBD.acronimoNo);
			}
			
			if(ps.executeUpdate()>0)
			{
				if(parametros.containsKey("ingreso"))
				{
					if(parametros.get("estado").toString().equals(ConstantesIntegridadDominio.acronimoEstadoPendiente) ||
							parametros.get("estado").toString().equals(ConstantesIntegridadDominio.acronimoPolizaVigente))
					{	
						
						if(UtilidadesHistoriaClinica.obtenerEstadoIngreso(con,Utilidades.convertirAEntero(parametros.get("ingreso").toString())).getAcronimo().equals(ConstantesIntegridadDominio.acronimoEstadoIncompletoGarantias))
						{
							
							ps =  new PreparedStatementDecorator(con.prepareStatement(strCadenaActualizarEstadoIngreso,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							
							ps.setString(1,parametros.get("usuariomodifica").toString());
							ps.setDate(2,Date.valueOf(parametros.get("fechamodifica").toString()));
							ps.setString(3,parametros.get("horamodifica").toString());
							ps.setInt(4,Utilidades.convertirAEntero(parametros.get("ingreso").toString()));					
						
							if(ps.executeUpdate()<=0)//si no es exitoso
								codigoPK=ConstantesBD.codigoNuncaValido;					
							
						}				
						
						//se pregunta que si el estado va a cambiar a vigente, de ser asi se insertan los datos al historial
						if(	parametros.get("estado").toString().equals(ConstantesIntegridadDominio.acronimoPolizaVigente))
						{						
							HashMap datos = new HashMap ();
							datos =consultarDatosHistorial(con, parametros);
							datos.put("ingreso",parametros.get("ingreso").toString());
							datos.put("tipodocumento",parametros.get("tipodocumento").toString());							
							if(!insertarHistorial(con, datos))//si no es exitoso
								codigoPK=ConstantesBD.codigoNuncaValido;
						}
						
					}
				}
			}
			ps.close();
			return codigoPK;							
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}		
		
		return ConstantesBD.codigoNuncaValido;
	}
	
	
	
	
	/**
	 * Actuliza un nuevo registro de tipo Documentos en Garantia
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static boolean actualizarDocumentoGarantia(Connection con, 
													HashMap parametros)
	{	
		//logger.info("\n\nentro al sql actualizarDocumentoGarantia "+parametros);
		try
		{
						
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strCadenaActualizacionDocumentoGarantia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setDate(1,Date.valueOf(parametros.get("fechageneracion").toString()));
			ps.setString(2,parametros.get("horageneracion").toString());			
			ps.setString(3,parametros.get("estado").toString());
			
			if(parametros.containsKey("motivoanulacion"))
				ps.setString(4,parametros.get("motivoanulacion").toString());
			else
				ps.setNull(4,Types.NULL);
			
			if(parametros.containsKey("entidadfinanciera"))
				ps.setString(5,parametros.get("entidadfinanciera").toString());
			else
				ps.setNull(5,Types.NULL);
			
			//se valida si viene el numero de cuenta, de ser asi se valida que no venga vacio.
			if(parametros.containsKey("numerocuenta"))
			{
				if (parametros.get("numerocuenta").toString().equals(""))
					ps.setNull(6,Types.NULL);
				else
					ps.setString(6,parametros.get("numerocuenta").toString());
			}
			else
				ps.setNull(6,Types.NULL);
			
			//se valida si viene el numero de documento, de ser asi se valida que no venga vacio.
			if(parametros.containsKey("numerodocumento"))
			{	
				if (parametros.get("numerodocumento").toString().equals(""))
					ps.setNull(7,Types.NULL);
				else
				ps.setString(7,parametros.get("numerodocumento").toString());
			}
			else
				ps.setNull(7,Types.NULL);
			
			//se valida que la fecha del documento venga, de ser asi se valida que no venga vacio.
			if(parametros.containsKey("fechadocumento"))
			{	
				if (parametros.get("fechadocumento").toString().equals(""))
					ps.setNull(8,Types.NULL);
				else
					ps.setDate(8,Date.valueOf(parametros.get("fechadocumento").toString()));
			}
			else
				ps.setNull(8,Types.NULL);
			
			if(parametros.containsKey("giradordocumento"))
				ps.setString(9,parametros.get("giradordocumento").toString());
			else
				ps.setNull(9,Types.NULL);
						
			//se valida si el valor viene, de ser asi se valida que no venga vacio.
			if(parametros.containsKey("valor"))
			{	
				if (parametros.get("valor").toString().equals(""))
					ps.setNull(10,Types.NULL);
				else
					ps.setDouble(10,Utilidades.convertirADouble(parametros.get("valor").toString()));
			}		
			else
				ps.setNull(10,Types.NULL);
			
			if(parametros.containsKey("clavecovinoc"))
				ps.setString(11,parametros.get("clavecovinoc").toString());
			else
				ps.setNull(11,Types.NULL);
			
			ps.setString(12,parametros.get("usuariomodifica").toString());
			ps.setDate(13,Date.valueOf(parametros.get("fechamodifica").toString()));
			ps.setString(14,parametros.get("horamodifica").toString());
			
			if(parametros.containsKey("usuarioanula"))
				ps.setString(15,parametros.get("usuarioanula").toString());
			else
				ps.setNull(15,Types.NULL);
			
			if(parametros.containsKey("fechaanula"))
				ps.setDate(16,Date.valueOf(parametros.get("fechaanula").toString()));
			else
				ps.setNull(16,Types.NULL);
			
			if(parametros.containsKey("horaanula"))
				ps.setString(17,parametros.get("horaanula").toString());
			else
				ps.setNull(17,Types.NULL);
		
			ps.setInt(18,Utilidades.convertirAEntero(parametros.get("ingreso").toString()));			
			ps.setInt(19,Utilidades.convertirAEntero(parametros.get("institucion").toString()));			
			ps.setString(20,parametros.get("consecutivo").toString());			
			ps.setString(21,parametros.get("anioconsecutivo").toString());			
			ps.setString(22,parametros.get("tipodocumento").toString());		
		
			
			if(ps.executeUpdate()>0)
				//se pregunta que si el estado va a cambiar a vigente, de ser asi se insertan los datos al historial
				if(	parametros.get("estado").toString().equals(ConstantesIntegridadDominio.acronimoPolizaVigente))
				{
					HashMap datos = new HashMap ();
					datos =consultarDatosHistorial(con, parametros);
					datos.put("ingreso",parametros.get("ingreso").toString());
					datos.put("tipodocumento",parametros.get("tipodocumento").toString());
					return insertarHistorial(con, datos);
				}
				return true;
							
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}		
		
		return false;
	}
	
	/*-----------------------------------------------------
	 * FIN METODOS DOCUMENTOS GARANTIA 
	 * ---------------------------------------------------*/
	
	
	/*-----------------------------------------------------
	 * METODOS INGRESO PACIENTE 
	 * ---------------------------------------------------*/	
	
	/**
	 * consulta ingresos de pacientes
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static HashMap consultarIngresos(Connection con,
											HashMap parametros)
	{
		HashMap mapa = new HashMap();
		HashMap mapaResultado = new HashMap();
		
		try
		{
			logger.info("CONSULTA: "+strCadenaConsultaIngresos.replace("?", parametros.get("codigoPaciente").toString()));
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strCadenaConsultaIngresos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("codigoPaciente").toString()));
			
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}	
		
		mapaResultado.put("error","");
		
		//validaciones para mostrar el listado de Ingresos / Cuenta 
		if(mapa.get("numRegistros").toString().equals("0"))
		{
			mapa.put("error","errorIngresos");
		}
		else
		{
			try
			{				
				int cont=0;
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strCadenaValidacionCuentaValida,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ResultSetDecorator rs;
				
				for(int i=0; i<Utilidades.convertirAEntero(mapa.get("numRegistros").toString()); i++)
				{
					ps.setInt(1,Utilidades.convertirAEntero(parametros.get("centroAtencion").toString()));
					ps.setInt(2,Utilidades.convertirAEntero(mapa.get("numeroCuenta_"+i).toString()));			
					
					rs =new ResultSetDecorator(ps.executeQuery());					
					
					if(rs.next())
					{						
						if(rs.getInt(1)>0)
						{
							mapaResultado.put("ingreso_"+cont,mapa.get("ingreso_"+i));
							mapaResultado.put("indpre_"+cont,mapa.get("indpre_"+i));
							mapaResultado.put("indrei_"+cont,mapa.get("indrei_"+i));
							mapaResultado.put("consecutivoingreso_"+cont,mapa.get("consecutivoingreso_"+i));
							mapaResultado.put("codigoPaciente_"+cont,mapa.get("codigoPaciente_"+i));
							mapaResultado.put("institucion_"+cont,mapa.get("institucion_"+i));
							mapaResultado.put("estado_"+cont,mapa.get("estado_"+i));
							mapaResultado.put("fechaIngreso_"+cont,mapa.get("fechaIngreso_"+i));
							mapaResultado.put("fechaEgreso_"+cont,mapa.get("fechaEgreso_"+i));
							mapaResultado.put("numeroCuenta_"+cont,mapa.get("numeroCuenta_"+i));							
							mapaResultado.put("estadoCuenta_"+cont,mapa.get("estadoCuenta_"+i));
							mapaResultado.put("descripcionCuenta_"+cont,mapa.get("descripcionCuenta_"+i));
							mapaResultado.put("viaIngreso_"+cont,mapa.get("viaIngreso_"+i));							
							mapaResultado.put("descripcionViaIngreso_"+cont,mapa.get("descripcionViaIngreso_"+i));
							mapaResultado.put("codigoResponsablePaciente_"+cont,mapa.get("codigoResponsablePaciente_"+i));							
							mapaResultado.put("datosResponsablePaciente_"+cont,mapa.get("datosResponsablePaciente_"+i));							
							mapaResultado.put("claseDeudorCo_"+cont,mapa.get("claseDeudorCo_"+i));
							
							cont++;						
						}
					}
				}
				
				mapaResultado.put("INDICES_MAPA", indicesIngresos);
				mapaResultado.put("numRegistros",cont);
				
				if(cont > 0)	
				{
					mapaResultado.put("error","");
					return mapaResultado;
				}
				else
				{
					mapaResultado.put("error","ErrorCuentaValida");					
					return mapaResultado;										
				}			
			}
			catch (Exception e) {
				e.printStackTrace();
			}		
		}	
		
		mapa.put("INDICES_MAPA", indicesIngresos);
		
		return mapa;
	}
	

	/*-----------------------------------------------------
	 * METODOS REGISTRAR DEUDOR CO 
	 * ---------------------------------------------------*/
	
	/**
	 * Devuelve el listado de los documentos cargados al paciente y su estado
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static HashMap infoDocsDependientes(Connection con,
											 HashMap parametros) 
	{	
		HashMap mapa = new HashMap();
		
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strCadenaInfoDocsDependientes,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("ingreso").toString()));
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("codigoPaciente").toString()));
			ps.setInt(3,Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);
						
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return mapa;
	}
	
	/**
	 * inserta un registro de tipo deudorco
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static int insertarDeudorCo(Connection con,
										   HashMap parametros) 
	{		
			
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strCadenaInserccionDeudorCo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int codigoPK=UtilidadBD.obtenerSiguienteValorSecuencia(con, "carterapaciente.seq_deudorco");
			ps.setInt(1, codigoPK);
			
			if(parametros.containsKey("ingreso")) 
				ps.setInt(2,Utilidades.convertirAEntero(parametros.get("ingreso").toString()));
			else
				ps.setObject(2, null);
			if(parametros.containsKey("codigoPaciente")) 
				ps.setInt(3,Utilidades.convertirAEntero(parametros.get("codigoPaciente").toString()));
			else
				ps.setObject(3, null);
			if(parametros.containsKey("institucion"))
				ps.setInt(4,Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			if(parametros.containsKey("claseDeudorCo"))
				ps.setString(5,parametros.get("claseDeudorCo").toString());
			if(parametros.containsKey("tipoDeudorCo"))
				ps.setString(6,parametros.get("tipoDeudorCo").toString());
			if(parametros.containsKey("tipoIdentificacion"))
				ps.setString(7,parametros.get("tipoIdentificacion").toString());
			if(parametros.containsKey("numeroIdentificacion"))
				ps.setString(8,parametros.get("numeroIdentificacion").toString());			
			
			if(parametros.containsKey("codigoPais"))
			{
				logger.info("...."+parametros.get("codigoPais"));
				if(!parametros.get("codigoPais").toString().equals(""))
					ps.setString(9,parametros.get("codigoPais").toString());
				else
					ps.setNull(9, Types.VARCHAR);
			}else
				ps.setNull(9, Types.VARCHAR);
			
			if(parametros.containsKey("codigoDepartamento"))
			{
				if(!parametros.get("codigoDepartamento").toString().equals(""))
					ps.setString(10,parametros.get("codigoDepartamento").toString());
				else
					ps.setNull(10, Types.VARCHAR);
			}else
				ps.setNull(10, Types.VARCHAR);
			
			if(parametros.containsKey("codigoCiudad"))
			{
				if(!parametros.get("codigoCiudad").toString().equals(""))
					ps.setString(11,parametros.get("codigoCiudad").toString());
				else
					ps.setNull(11, Types.VARCHAR);
			}else
				ps.setNull(11, Types.VARCHAR);
			
			if(parametros.containsKey("primerNombre"))
				ps.setString(12,parametros.get("primerNombre").toString());
			
			if(parametros.containsKey("segundoNombre"))
				ps.setString(13,parametros.get("segundoNombre").toString());
			if(parametros.containsKey("primerApellido"))
				ps.setString(14,parametros.get("primerApellido").toString());
			if(parametros.containsKey("segundoApellido"))
				ps.setString(15,parametros.get("segundoApellido").toString());
			if(parametros.containsKey("direccionReside"))
				ps.setString(16,parametros.get("direccionReside").toString());
			
			if(parametros.containsKey("codigoPaisReside"))
			{
				if(!parametros.get("codigoPaisReside").toString().equals(""))
					ps.setString(17,parametros.get("codigoPaisReside").toString());
				else
					ps.setNull(17, Types.VARCHAR);
			}else
				ps.setNull(17, Types.VARCHAR);	
			
			if(parametros.containsKey("codigoDepartamentoReside"))
			{
				if(!parametros.get("codigoDepartamentoReside").toString().equals(""))
					ps.setString(18,parametros.get("codigoDepartamentoReside").toString());
				else
					ps.setNull(18, Types.VARCHAR);
			}else
				ps.setNull(18, Types.VARCHAR);
			
			if(parametros.containsKey("codigoCiudadReside"))
			{
				if(!parametros.get("codigoCiudadReside").toString().equals(""))
					ps.setString(19,parametros.get("codigoCiudadReside").toString());
				else
					ps.setNull(19, Types.VARCHAR);
			}else
				ps.setNull(19, Types.VARCHAR);
			
			if(parametros.containsKey("codigoBarrioReside"))
			{				
				if(!parametros.get("codigoBarrioReside").toString().equals(""))
					ps.setInt(20,Utilidades.convertirAEntero(parametros.get("codigoBarrioReside").toString()));
				else
					ps.setNull(20,Types.INTEGER);
			}else
				ps.setNull(20,Types.INTEGER);
			
			if(parametros.containsKey("telefonoReside"))
				ps.setString(21,parametros.get("telefonoReside").toString());
			if(parametros.containsKey("fechaNacimiento"))
			{
				if(!parametros.get("fechaNacimiento").toString().equals(""))
					ps.setDate(22,Date.valueOf(parametros.get("fechaNacimiento").toString()));
				else
					ps.setNull(22, Types.VARCHAR);
			}else
				ps.setNull(22, Types.VARCHAR);
						
			if(parametros.containsKey("relacionPaciente"))
			{
				if(parametros.get("relacionPaciente").equals(""))
					ps.setNull(23,Types.VARCHAR);
				else
					ps.setString(23,parametros.get("relacionPaciente").toString());
			}else
				ps.setNull(23,Types.VARCHAR);
				
				
			if(parametros.containsKey("tipoOcupacion"))
				ps.setString(24,parametros.get("tipoOcupacion").toString());
			if(parametros.containsKey("ocupacion"))
				ps.setString(25,parametros.get("ocupacion").toString());
			
			
			if(parametros.containsKey("empresa"))
			{
				if(parametros.get("empresa").equals(""))
					ps.setNull(26,Types.VARCHAR);
				else					
					ps.setString(26,parametros.get("empresa").toString());
			}else
				ps.setNull(26,Types.VARCHAR);
				
			
			
			if(parametros.containsKey("cargo"))
			{
				if(parametros.get("cargo").equals(""))
					ps.setNull(27,Types.VARCHAR);
				else
					ps.setString(27,parametros.get("cargo").toString());					
			}else
				ps.setNull(27,Types.VARCHAR);
			
			
			if(parametros.containsKey("antiguedad"))
			{	
				if(parametros.get("antiguedad").equals(""))
					ps.setNull(28,Types.VARCHAR);
				else
					ps.setString(28,parametros.get("antiguedad").toString());				
			}else	
				ps.setNull(28,Types.VARCHAR);
			
			
			if(parametros.containsKey("direccionOficina"))
			{	
				if(parametros.get("direccionOficina").equals(""))
					ps.setNull(29,Types.VARCHAR);
				else
					ps.setString(29,parametros.get("direccionOficina").toString());			
			}else
				ps.setNull(29,Types.VARCHAR);
				
			
			if(parametros.containsKey("telefonoOficina"))
			{	
				if(parametros.get("telefonoOficina").equals(""))
					ps.setNull(30,Types.VARCHAR);
				else
					ps.setString(30,parametros.get("telefonoOficina").toString());				
			}else
				ps.setNull(30,Types.VARCHAR);
				
			
			if(parametros.containsKey("nombresReferencia"))
			{
				if(!parametros.get("nombresReferencia").toString().equals(""))
					ps.setString(31,parametros.get("nombresReferencia").toString());
				else
					ps.setNull(31, Types.VARCHAR);
			}else
				ps.setNull(31, Types.VARCHAR);
			
			if(parametros.containsKey("direccionReferencia"))
			{
				if(!parametros.get("direccionReferencia").toString().equals(""))
					ps.setString(32,parametros.get("direccionReferencia").toString());
				else
					ps.setNull(32, Types.VARCHAR);
			}else
				ps.setNull(32, Types.VARCHAR);
			
			if(parametros.containsKey("telefonoReferencia"))
			{
				if(!parametros.get("telefonoReferencia").toString().equals(""))
					ps.setString(33,parametros.get("telefonoReferencia").toString());
				else
					ps.setNull(33, Types.VARCHAR);
			}else
				ps.setNull(33, Types.VARCHAR);
			
			
			if(parametros.containsKey("observaciones"))				
				ps.setString(34,parametros.get("observaciones").toString());
			else
				ps.setNull(34, Types.VARCHAR);
			
			if(parametros.containsKey("fechaVerificacion"))
			{	
				if(parametros.get("fechaVerificacion").equals(""))
					ps.setNull(35,Types.DATE);	
				else					
					ps.setDate(35,Date.valueOf(parametros.get("fechaVerificacion").toString()));
			}else
				ps.setNull(35,Types.DATE);
			
			if(parametros.containsKey("usuarioVerificacion"))
			{
				if(parametros.get("usuarioVerificacion").equals(""))
					ps.setNull(36,Types.VARCHAR);
				else
					ps.setString(36,parametros.get("usuarioVerificacion").toString());										
			}else
				ps.setNull(36,Types.VARCHAR);
				
			
			if(parametros.containsKey("observacionesVerificacion"))
			{
				if(parametros.get("observacionesVerificacion").equals(""))
					ps.setNull(37,Types.VARCHAR);
				else
					ps.setString(37,parametros.get("observacionesVerificacion").toString());									
			}else
				ps.setNull(37,Types.VARCHAR);
				
				
			
			if(parametros.containsKey("fechaVeriRiesgos"))
			{
				if(parametros.get("fechaVeriRiesgos").equals(""))
					ps.setNull(38,Types.DATE);
				else					
					ps.setDate(38,Date.valueOf(parametros.get("fechaVeriRiesgos").toString()));				
			}else
				ps.setNull(38,Types.DATE);
			
				
			if(parametros.containsKey("usuarioVeriRiesgos"))
			{
				if(parametros.containsKey("usuarioVeriRiesgos"))
					ps.setNull(39,Types.VARCHAR);
				else
					ps.setString(39,parametros.get("usuarioVeriRiesgos").toString());
			}else
				ps.setNull(39,Types.VARCHAR);
				
							
			if(parametros.containsKey("observacionesVeriRiesgos"))
			{
				if(parametros.get("observacionesVeriRiesgos").equals(""))
					ps.setNull(40,Types.VARCHAR);
				else
					ps.setString(40,parametros.get("observacionesVeriRiesgos").toString());
			}else
				ps.setNull(40,Types.VARCHAR);
				
			
			if(parametros.containsKey("usuarioModifica"))
				ps.setString(41,parametros.get("usuarioModifica").toString());
			if(parametros.containsKey("fechaModifica"))
				ps.setDate(42,Date.valueOf(parametros.get("fechaModifica").toString()));
			if(parametros.containsKey("horaModifica"))
				ps.setString(43,parametros.get("horaModifica").toString());
			//----
						
			if(ps.executeUpdate()<=0)
				codigoPK=ConstantesBD.codigoNuncaValido;
			ps.close();
			return codigoPK;
		}
		
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return ConstantesBD.codigoNuncaValido;
	}	
	
	
	/**
	 * Actualiza un registro de tipo deudorco
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static boolean actualizarDeudorCo(Connection con,
										   HashMap parametros) 
	{			
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strCadenaActualizacionDeudorCo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
			if(parametros.containsKey("tipoDeudorCo"))
				ps.setString(1,parametros.get("tipoDeudorCo").toString());
			if(parametros.containsKey("tipoIdentificacion"))
				ps.setString(2,parametros.get("tipoIdentificacion").toString());
			if(parametros.containsKey("numeroIdentificacion"))
				ps.setString(3,parametros.get("numeroIdentificacion").toString());
			if(parametros.containsKey("codigoPais")) 
				ps.setString(4,parametros.get("codigoPais").toString());
			if(parametros.containsKey("codigoDepartamento"))
				ps.setString(5,parametros.get("codigoDepartamento").toString());
			if(parametros.containsKey("codigoCiudad")) 
				ps.setString(6,parametros.get("codigoCiudad").toString());
			if(parametros.containsKey("primerNombre"))
				ps.setString(7,parametros.get("primerNombre").toString());
			if(parametros.containsKey("segundoNombre"))
				ps.setString(8,parametros.get("segundoNombre").toString());
			if(parametros.containsKey("primerApellido"))
				ps.setString(9,parametros.get("primerApellido").toString());
			if(parametros.containsKey("segundoApellido"))
				ps.setString(10,parametros.get("segundoApellido").toString());
			if(parametros.containsKey("direccionReside"))
				ps.setString(11,parametros.get("direccionReside").toString());
			if(parametros.containsKey("codigoPaisReside"))
				ps.setString(12,parametros.get("codigoPaisReside").toString());
			if(parametros.containsKey("codigoDepartamentoReside"))
				ps.setString(13,parametros.get("codigoDepartamentoReside").toString());
			if(parametros.containsKey("codigoCiudadReside"))
				ps.setString(14,parametros.get("codigoCiudadReside").toString());
			if(parametros.containsKey("codigoBarrioReside"))
				{	
					if(parametros.get("codigoBarrioReside").equals(""))
						ps.setNull(15,Types.NULL);
					else
						ps.setString(15,parametros.get("codigoBarrioReside").toString());
				}	
				
					
			if(parametros.containsKey("telefonoReside"))
				ps.setString(16,parametros.get("telefonoReside").toString());
			if(parametros.containsKey("fechaNacimiento"))
				ps.setDate(17,Date.valueOf(parametros.get("fechaNacimiento").toString()));
			
			//valores modificables cuando el Codeudor diferente de Otros
			if(parametros.containsKey("relacionPaciente"))
			{	
				if(parametros.get("relacionPaciente").equals(""))
					ps.setNull(18,Types.VARCHAR);
				else
					ps.setString(18,parametros.get("relacionPaciente").toString());
			}	
			
			
			if(parametros.containsKey("tipoOcupacion"))
				ps.setString(19,parametros.get("tipoOcupacion").toString());		
			if(parametros.containsKey("ocupacion"))
				ps.setString(20,parametros.get("ocupacion").toString());
			
			if(parametros.containsKey("empresa"))
			{	
				if(parametros.get("empresa").equals(""))
					ps.setNull(21,Types.VARCHAR);
				else
					ps.setString(21,parametros.get("empresa").toString());
			}	
			
			
			if(parametros.containsKey("cargo"))
			{	
				if(parametros.get("cargo").equals(""))
					ps.setNull(22,Types.VARCHAR);
				else
					ps.setString(22,parametros.get("cargo").toString());
			}	
			
			if(parametros.containsKey("antiguedad"))
			{	
				if(parametros.get("antiguedad").equals(""))
					ps.setNull(23,Types.VARCHAR);
				else
					ps.setString(23,parametros.get("antiguedad").toString());
			}	
			
			if(parametros.containsKey("direccionOficina"))
			{	
				if(parametros.get("direccionOficina").equals(""))
					ps.setNull(24,Types.VARCHAR);
				else
					ps.setString(24,parametros.get("direccionOficina").toString());
			}	
	
			if(parametros.containsKey("telefonoOficina"))
			{	
				if(parametros.get("telefonoOficina").equals(""))
					ps.setNull(25,Types.VARCHAR);
				else
					ps.setString(25,parametros.get("telefonoOficina").toString());
			}	
			
			if(parametros.containsKey("nombresReferencia"))
				ps.setString(26,parametros.get("nombresReferencia").toString());
			if(parametros.containsKey("direccionReferencia"))
				ps.setString(27,parametros.get("direccionReferencia").toString());
			if(parametros.containsKey("telefonoReferencia"))
				ps.setString(28,parametros.get("telefonoReferencia").toString());
			if(parametros.containsKey("observaciones"))
				ps.setString(29,parametros.get("observaciones").toString());			
			
			if(parametros.containsKey("usuarioModifica"))
				ps.setString(30,parametros.get("usuarioModifica").toString());
			if(parametros.containsKey("fechaModifica"))
				ps.setDate(31,Date.valueOf(parametros.get("fechaModifica").toString()));
			if(parametros.containsKey("horaModifica"))
				ps.setString(32,parametros.get("horaModifica").toString());
			
			
			if(parametros.containsKey("ingreso")) 
				ps.setInt(33,Utilidades.convertirAEntero(parametros.get("ingreso").toString()));			
			if(parametros.containsKey("institucion"))
				ps.setInt(34,Utilidades.convertirAEntero(parametros.get("institucion").toString()));			
			if(parametros.containsKey("claseDeudorCo"))
				ps.setString(35,parametros.get("claseDeudorCo").toString());			 
			//logger.info("\n\n**************************llega hasta aqui4*****************************");	
			if(ps.executeUpdate()>0)
				return true;
		}
		
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}	
	
	
	
	
	
	/**
	 * Actualiza los registros de la verificacion
	 * Connection con
	 * HashMap parametros
	 * */
	public static boolean actualizarverficacion(Connection con, HashMap parametros)
	{		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strCadenaActualizacionVerificacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
			if(parametros.containsKey("fechaVerificacion"))
				ps.setDate(1,Date.valueOf(parametros.get("fechaVerificacion").toString()));
			if(parametros.containsKey("usuarioVerificacion"))
				ps.setString(2,parametros.get("usuarioVerificacion").toString());
			if(parametros.containsKey("observacionesVerificacion"))
				ps.setString(3,parametros.get("observacionesVerificacion").toString());
			
			if(parametros.containsKey("fechaVeriRiesgos"))
				ps.setDate(4,Date.valueOf(parametros.get("fechaVeriRiesgos").toString()));
			if(parametros.containsKey("usuarioVeriRiesgos"))
				ps.setString(5,parametros.get("usuarioVeriRiesgos").toString());			
			if(parametros.containsKey("observacionesVeriRiesgos"))
				ps.setString(6,parametros.get("observacionesVeriRiesgos").toString());
			
			if(parametros.containsKey("usuarioModificaVerifica"))
				ps.setString(7,parametros.get("usuarioModificaVerifica").toString());
			if(parametros.containsKey("fechaModificaVerifica"))
				ps.setDate(8,Date.valueOf(parametros.get("fechaModificaVerifica").toString()));
			if(parametros.containsKey("horaModificaVerifica"))
				ps.setString(9,parametros.get("horaModificaVerifica").toString());
			
			if(parametros.containsKey("ingreso")) 
				ps.setInt(10,Utilidades.convertirAEntero(parametros.get("ingreso").toString()));			
			if(parametros.containsKey("institucion"))
				ps.setInt(11,Utilidades.convertirAEntero(parametros.get("institucion").toString()));			
			if(parametros.containsKey("claseDeudorCo"))
				ps.setString(12,parametros.get("claseDeudorCo").toString());
			
						
			if(ps.executeUpdate()>0)
				return true;
		}
		
		catch(SQLException e)
		{
			e.printStackTrace();
		}		
		return false;
	}

	
	
	
	/**
	 * Consulta Informacion DeudorCo
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static HashMap consultarDeudorCo(Connection con, HashMap parametros)
	{
		HashMap mapa = new HashMap();
		String cadena =  strCadenaConsultaDeudorCo+" WHERE ";
		
		try
		{
			if(parametros.containsKey("institucion"))
				cadena+= " institucion = "+parametros.get("institucion").toString();
			
			if(parametros.containsKey("ingreso"))
				cadena+= " AND ingreso = "+parametros.get("ingreso").toString();
			
			if(parametros.containsKey("codigoPaciente"))
				cadena+= " AND codigo_paciente = "+parametros.get("codigoPaciente").toString();
			
			if(parametros.containsKey("claseDeudorCo"))
				cadena+= " AND clase_deudorco='"+parametros.get("claseDeudorCo").toString()+"' ";
			
			if(parametros.containsKey("tipoIdentificacion"))
				cadena+= " AND tipo_identificacion='"+parametros.get("tipoIdentificacion").toString()+"' ";
			
			if(parametros.containsKey("numeroIdentificacion"))
				cadena+= " AND numero_identificacion='"+parametros.get("numeroIdentificacion")+"'";			
						
			logger.info("cadena ---------->>>>>>>>>> "+cadena);			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),false,true);
						
		}
		catch(SQLException e)
		{			
			e.printStackTrace();
		}
		
		mapa.put("INDICES_MAPA", indicesDeudorCo);				
		return mapa;
	}
	
	/**
	 * Eliminacion de Informacion DeudorCo
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static boolean eliminarDeudorCo(Connection con, HashMap parametros)
	{		
				
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement( strCadenaEliminacionDeudorCo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("ingreso").toString()));
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("codigoPaciente").toString()));
			ps.setInt(3,Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			ps.setString(4,parametros.get("claseDeudorCo").toString());
						
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	
		return false;
	}
	
	
	/**
	 * Consulta la dependencia que existe entre el Deudor y el Codeudor
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static HashMap consultarDependenciaDeudorCodeudor(Connection con, HashMap parametros)
	{
		HashMap mapa = new HashMap();
				
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strCadenaVerificacionDeudorCodeudor,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("ingreso").toString()));			
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			ps.setString(3,parametros.get("claseDeudorCo").toString());
			
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),false,true);
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}		
		
		return mapa;			
	}
	
	
	/**
	 * Consulta los datos de la persona
	 * @param Connection con
	 * @param HashMap parametros
	 * @param String OpcionConsulta
	 * */
	public static HashMap consultarDatosPaciente(Connection con, HashMap parametros)
	{
		HashMap mapa = new HashMap();
		String cadena = strCadenaConsultaPaciente+" WHERE ";
		
		try
		{				
			if(parametros.containsKey("codigoPaciente")) 	
				cadena+= " pe.codigo="+parametros.get("codigoPaciente").toString();			
			else if(parametros.containsKey("numeroIdentificacion"))							
				cadena+= " pe.tipo_identificacion='"+parametros.get("tipoIdentificacion").toString()+"' AND pe.numero_identificacion='"+parametros.get("numeroIdentificacion").toString()+"' ";
				
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),false,true);
				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}		
		
		return mapa;
	}
	
	/**
	 * Consulta los datos de la persona
	 * @param Connection con
	 * @param HashMap parametros
	 * @param String OpcionConsulta
	 * */
	public static HashMap consultarResponsablePaciente(Connection con, HashMap parametros)
	{
		HashMap mapa = new HashMap();
		
		String cadena = strCadenaConsultaResponsablePaciente;
		
		try
		{			
			
			if(parametros.containsKey("numeroIdentificacion"))
				cadena+=" WHERE numero_identificacion ='"+parametros.get("numeroIdentificacion").toString()+"' AND " +
						"tipo_identificacion ='"+parametros.get("tipoIdentificacion")+"' ";
			else
				cadena+=" WHERE codigo="+parametros.get("codigoResponsablePaciente").toString();		
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),false,true);			
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}		
		
		mapa.put("INDICES_MAP",indicesDeudorCo);		
		return mapa;
	}
	
	
	
	/**
	 * Actualiza la informacion del responsable del paciente mientras este exista y 
	 * los campos de ciudad y pais de residencia y expedicion esten vacios
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static boolean actualizarResponsablePaciente(Connection con, HashMap parametros)
	{		
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strCadenaActualizacionResponsablePaciente,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			//Informacion por actualizar del responsable paciente
			ps.setString(1,parametros.get("codigoPais").toString());
			ps.setString(2,parametros.get("codigoDepartamento").toString());
			ps.setString(3,parametros.get("codigoCiudad").toString());
			ps.setString(4,parametros.get("codigoPaisReside").toString());
			ps.setString(5,parametros.get("codigoDepartamentoReside").toString());
			ps.setString(6,parametros.get("codigoCiudadReside").toString());
			ps.setString(7,parametros.get("numeroIdentificacion").toString());
			ps.setString(8,parametros.get("tipoIdentificacion").toString());
			
			ps.executeUpdate();
					
		}
		catch(SQLException e )
		{
			e.printStackTrace();
		}
		
		return true;
	}
	
	/**
	 * inserta un nuevo responsable de paciente
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static boolean insertarResponsablePaciente(Connection con, HashMap parametros, String strCadenaInserccionNuevoResponsable)
	{
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strCadenaInserccionNuevoResponsable,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			//informacion del responsable de paciente
			ps.setString(1,parametros.get("numeroIdentificacion").toString());
			ps.setString(2,parametros.get("tipoIdentificacion").toString());
			ps.setString(3,parametros.get("direccionReside").toString());
			ps.setString(4,parametros.get("telefonoReside").toString());
			ps.setString(5,parametros.get("relacionPaciente").toString());
			ps.setString(6,parametros.get("codigoPais").toString());
			ps.setString(7,parametros.get("codigoCiudad").toString());
			ps.setString(8,parametros.get("codigoDepartamento").toString());
			ps.setString(9,parametros.get("primerApellido").toString());
			ps.setString(10,parametros.get("segundoApellido").toString());
			ps.setString(11,parametros.get("primerNombre").toString());
			ps.setString(12,parametros.get("segundoNombre").toString());
			ps.setString(13,parametros.get("codigoPaisReside").toString());
			ps.setString(14,parametros.get("codigoCiudadReside").toString());
			ps.setString(15,parametros.get("codigoDepartamentoReside").toString());
			
			if(parametros.get("codigoBarrioReside").equals(""))
				ps.setNull(16,Types.NULL);
			else
				ps.setInt(16,Utilidades.convertirAEntero(parametros.get("codigoBarrioReside").toString()));
			
			logger.info("fecha de nacimiento: "+parametros.get("fechaNacimiento"));
			ps.setDate(17,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaNacimiento").toString())));
			
			//fecha modifica
			ps.setDate(18, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			
			//hora modifica
			ps.setString(19, UtilidadFecha.getHoraActual());
			
			ps.setString(20, parametros.get("usuarioModifica")+"");
			
			if(ps.executeUpdate()>0)
				return true;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;				
	}	
	
	 
	
	/**
	 * Actualiza el codigo del Responsable en la Tabla Cuentas
	 * Connection con
	 * HashMap parametros
	 * */
	public static boolean actualizarCuenta(Connection con, HashMap parametros)
	{		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strCadenaActualizacionCuenta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("parametros actualiza cuenta: "+parametros);
			ps.setString(1,parametros.get("tipoIdentificacion").toString());			
			ps.setString(2,parametros.get("numeroIdentificacion").toString());			
			ps.setInt(3,Utilidades.convertirAEntero(parametros.get("numeroCuenta").toString()));
			
			if(ps.executeUpdate()>0)
				return true;
		}
		
		catch(SQLException e)
		{
			e.printStackTrace();
		}		
		return false;
	}
	
	
	
	
	/**
	 * Actualiza la información del DeudorCo de Documentos de Garantia cuando se ha 
	 * modificado la información del paciente o del Responsable desde las funcionalidades
	 * Ingresar Paciente,Modificar Paciente,Modificar Cuenta.   
	 * @param Connection con
	 * @param HashMap campos  
	 * */
	public static boolean actualizarDatosPersonaDocGarantia(Connection con, HashMap campos)
	{		
		boolean tmp;
		HashMap datosActuales;
		String strConsulta = "",strIngresos="";
		String tipoDeudorCo = campos.get("tipoDeudorCo").toString();
		
		logger.info("ENTRO MODIFICAR PERSONA DOC GARANTIA >>> ");
		
		try
		{
			ResultSetDecorator rs;
			PreparedStatementDecorator ps;						
			
			if(tipoDeudorCo.equals(ConstantesIntegridadDominio.acronimoPaciente))
			{		
				
				//****************************************************************************
				//Captura la informacion de los ingresos asociados al paciente en documentos de garantia
				
				strConsulta = "SELECT ingreso || ',' " +
						"FROM documentos_garantia " +
						"WHERE " +
						"codigo_paciente = " +campos.get("codigoPaciente")+" "+
						"AND institucion = "+campos.get("institucion")+" "+						
						"AND estado IN('"+ConstantesIntegridadDominio.acronimoEstadoEntregado+"','"+ConstantesIntegridadDominio.acronimoPolizaVigente+"') " +
						"GROUP BY ingreso ";				
				
				//Verifica que no existan documentos de Garantia en estado Vigente o Entregado asociados a la persona
				ps =  new PreparedStatementDecorator(con.prepareStatement(strConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				tmp = false;				
				rs =new ResultSetDecorator(ps.executeQuery());
				
				while(rs.next())				
					strIngresos += rs.getString(1);
									
				if(strIngresos.length() > 0)
					strIngresos = strIngresos.substring(0,(strIngresos.length()-1));
				else
					strIngresos = "";
							
				//****************************************************************************			
				
				//****************************************************************************
				//Consulta la informacion del Paciente Actual
				datosActuales = consultarDatosPaciente(con, campos);	
				
				
				if(Utilidades.convertirAEntero(datosActuales.get("numRegistros").toString()) > 0)
				{
										
					strConsulta ="UPDATE " +
						"deudorco SET " +		
						"tipo_deudorco= ?, " +
						"tipo_identificacion = ?," +
						"numero_identificacion = ?," +
						"codigo_pais = ?," +
						"codigo_departamento = ?," +
						"codigo_ciudad = ?," +
						"primer_nombre = ?," +
						"segundo_nombre = ?," +
						"primer_apellido = ?," +
						"segundo_apellido = ?," +
						"direccion_reside = ?," +
						"codigo_pais_reside = ?," +
						"codigo_departamento_reside = ?," +
						"codigo_ciudad_reside = ?," +
						"codigo_barrio_reside = ?,";						
						
					
					if(!datosActuales.get("telefonoReside").equals(""))
						strConsulta +=" telefono_reside=?,";
					
					strConsulta +=" fecha_nacimiento = '"+datosActuales.get("fechaNacimientoold").toString()+"' "+			
								  " WHERE institucion = "+campos.get("institucion").toString()+
								  " AND codigo_paciente = "+datosActuales.get("codigoPaciente").toString()+
					  	          " AND tipo_deudorco =  '"+ConstantesIntegridadDominio.acronimoPaciente+"' ";
					
					if(!strIngresos.equals(""))					
						strConsulta +=" AND ingreso NOT IN("+strIngresos+") ";				
					 
					
					ps =  new PreparedStatementDecorator(con.prepareStatement(strConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setString(1,ConstantesIntegridadDominio.acronimoPaciente);
					ps.setString(2,datosActuales.get("tipoIdentificacion").toString());
					ps.setString(3,datosActuales.get("numeroIdentificacion").toString());
					ps.setString(4,datosActuales.get("codigoPais").toString());
					ps.setString(5,datosActuales.get("codigoDepartamento").toString());
					ps.setString(6,datosActuales.get("codigoCiudadold").toString());
					ps.setString(7,datosActuales.get("primerNombre").toString());
					ps.setString(8,datosActuales.get("segundoNombre").toString());
					ps.setString(9,datosActuales.get("primerApellido").toString());
					ps.setString(10,datosActuales.get("segundoApellido").toString());
					ps.setString(11,datosActuales.get("direccionReside").toString());
					ps.setString(12,datosActuales.get("codigoPaisReside").toString());
					ps.setString(13,datosActuales.get("codigoDepartamentoReside").toString());
					ps.setString(14,datosActuales.get("codigoCiudadResideold").toString());
					
						
					if(datosActuales.get("codigoBarrioReside").equals(""))
						ps.setNull(15,Types.NULL);
					else
						ps.setInt(15,Utilidades.convertirAEntero(datosActuales.get("codigoBarrioReside").toString()));						
					
					if(!datosActuales.get("telefonoReside").equals(""))					
						ps.setString(16,datosActuales.get("telefonoReside").toString());					
										
					ps.executeUpdate();
					
					
					logger.info("Modifico Persona ");
					return true;
					
				}	
				else
					return false;
			}
			else if(tipoDeudorCo.equals(ConstantesIntegridadDominio.acronimoResponsablePaci))			
			{
				
				//****************************************************************************
				//Captura la informacion de los ingresos asociados al paciente en documentos de garantia
				
				strConsulta = "SELECT ingreso || ',' " +
						"FROM documentos_garantia " +
						"WHERE " +
						"codigo_paciente = " +campos.get("codigoPaciente")+" "+
						"AND institucion = "+campos.get("institucion")+" "+						
						"AND estado IN('"+ConstantesIntegridadDominio.acronimoEstadoEntregado+"','"+ConstantesIntegridadDominio.acronimoPolizaVigente+"') " +
						"GROUP BY ingreso ";
				
				//Verifica que no existan documentos de Garantia en estado Vigente o Entregado asociados a la persona
				ps =  new PreparedStatementDecorator(con.prepareStatement(strConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				tmp = false;				
				rs =new ResultSetDecorator(ps.executeQuery());
				
				while(rs.next())				
					strIngresos += rs.getString(1);
								
				
				if(strIngresos.length() > 0)
					strIngresos = strIngresos.substring(0,(strIngresos.length()-1));
				else
					strIngresos = "";				
							
				//****************************************************************************				
				
				//****************************************************************************
				//Consulta la informacion del Responsable de Paciente Actual
				
				datosActuales = consultarResponsablePaciente(con,campos);								
				
				if(Utilidades.convertirAEntero(datosActuales.get("numRegistros").toString()) > 0 )
				{
					strConsulta ="UPDATE " +
						"deudorco SET " +		
						"tipo_deudorco= ?, " +
						"tipo_identificacion = ?," +
						"numero_identificacion = ?," +
						"codigo_pais = ?," +
						"codigo_departamento = ?," +
						"codigo_ciudad = ?," +
						"primer_nombre = ?," +
						"segundo_nombre = ?," +
						"primer_apellido = ?," +
						"segundo_apellido = ?," +
						"direccion_reside = ?," +
						"codigo_pais_reside = ?," +
						"codigo_departamento_reside = ?," +
						"codigo_ciudad_reside = ?," +
						"codigo_barrio_reside = ?,";						
					
				
					if(!datosActuales.get("telefonoReside").equals(""))
						strConsulta +=" telefono_reside=?,";
					
					strConsulta +=" fecha_nacimiento = '"+datosActuales.get("fechaNacimientoold")+"' "+			
								  " WHERE institucion = "+campos.get("institucion")+
								  " AND tipo_identificacion = '"+campos.get("tipoIdentificacion")+"' "+
								  "	AND numero_identificacion = '"+campos.get("numeroIdentificacion")+"' "+
								  " AND tipo_deudorco = '"+ConstantesIntegridadDominio.acronimoResponsablePaci+"' ";								  
					
					
					if(!strIngresos.equals(""))					
						strConsulta +=" AND ingreso NOT IN("+strIngresos+") ";									
											
						
					
					ps =  new PreparedStatementDecorator(con.prepareStatement(strConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setString(1,ConstantesIntegridadDominio.acronimoResponsablePaci);
					ps.setString(2,datosActuales.get("tipoIdentificacion").toString());
					ps.setString(3,datosActuales.get("numeroIdentificacion").toString());
					ps.setString(4,datosActuales.get("codigoPais").toString());
					ps.setString(5,datosActuales.get("codigoDepartamento").toString());
					ps.setString(6,datosActuales.get("codigoCiudadold").toString());
					ps.setString(7,datosActuales.get("primerNombre").toString());
					ps.setString(8,datosActuales.get("segundoNombre").toString());
					ps.setString(9,datosActuales.get("primerApellido").toString());
					ps.setString(10,datosActuales.get("segundoApellido").toString());
					ps.setString(11,datosActuales.get("direccionReside").toString());
					ps.setString(12,datosActuales.get("codigoPaisReside").toString());
					ps.setString(13,datosActuales.get("codigoDepartamentoReside").toString());
					ps.setString(14,datosActuales.get("codigoCiudadResideold").toString());
					
						
					if(datosActuales.get("codigoBarrioReside").equals(""))
						ps.setNull(15,Types.NULL);
					else
						ps.setInt(15,Utilidades.convertirAEntero(datosActuales.get("codigoBarrioReside").toString()));						
					
					if(!datosActuales.get("telefonoReside").toString().equals(""))					
						ps.setString(16,datosActuales.get("telefonoReside").toString());					
					
					logger.info("Modifico Responsable ? "+strConsulta);
					ps.executeUpdate();					
					logger.info("Modifico Responsable");
					
					return true;					
				}
				else
					return false;
			}							
			else if(tipoDeudorCo.equals(ConstantesIntegridadDominio.acronimoOtro))
			{
				
				//****************************************************************************
				//Captura la informacion de los ingresos asociados al paciente en documentos de garantia
				
				strConsulta = "SELECT count(ingreso) " +
						"FROM documentos_garantia " +
						"WHERE " +
						"ingreso = " +campos.get("ingreso")+" "+
						"AND codigo_paciente = " +campos.get("codigoPaciente")+" "+
						"AND institucion = "+campos.get("institucion")+" "+						
						"AND estado IN('"+ConstantesIntegridadDominio.acronimoEstadoEntregado+"','"+ConstantesIntegridadDominio.acronimoPolizaVigente+"') ";
						
				
				//Verifica que no existan documentos de Garantia en estado Vigente o Entregado asociados a la persona
				ps =  new PreparedStatementDecorator(con.prepareStatement(strConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));								
				rs =new ResultSetDecorator(ps.executeQuery());
				
				//retorna si encuentra documentos en garantia en estado Vigente o Entregado
				if(rs.next())				
				{
					if(rs.getInt(1)>0)
						return true;
				}							
				//****************************************************************************
				
				ps.clearParameters();			
				
				strConsulta ="UPDATE " +
					"deudorco SET " +		
					"tipo_deudorco= ? " +
					"WHERE institucion = ? " +
					"AND codigo_paciente = ? " +
					"AND ingreso = ? " +
					"AND tipo_deudorco = '"+ConstantesIntegridadDominio.acronimoResponsablePaci+"' ";			
				
				ps =  new PreparedStatementDecorator(con.prepareStatement(strConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setString(1,ConstantesIntegridadDominio.acronimoOtro);
				ps.setInt(2,Utilidades.convertirAEntero(campos.get("institucion").toString()));
				ps.setInt(3,Utilidades.convertirAEntero(campos.get("codigoPaciente").toString()));
				ps.setInt(4,Utilidades.convertirAEntero(campos.get("ingreso").toString()));				
				
				ps.executeUpdate();
				logger.info("Modifico Responsable Otro");
				
				return true;				
			}					
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}	
		
		return false;
	}	
	
	/*-----------------------------------------------------
	 * FIN METODOS REGISTRAR DEUDOR CO 
	 * ---------------------------------------------------*/	
	
	/*
	 * ------------------------------------------------
	 * METODOS UTILITARIOS
	 * -------------------------------------------------
	 */
	/**
	 * Método que verifica  si existen documentos de garabtía x ingreso
	 */
	public static boolean existenDocumentosGarantiaXIngreso(Connection con,HashMap campos)
	{
		boolean existe = false;
		try
		{	
			String consulta = strCadenaInfoDocsDependientes + " AND estado <> '"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"'";
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1,Utilidades.convertirAEntero(campos.get("idIngreso").toString()));
			ps.setInt(2,Utilidades.convertirAEntero(campos.get("codigoPaciente").toString()));
			ps.setInt(3,Utilidades.convertirAEntero(campos.get("codigoInstitucion").toString()));
			
			
			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
				existe = true;
			
						
		}
		catch(SQLException e)
		{
			logger.error("Error existenDocumentosGarantiaXIngreso: "+e);
		}
		return existe;
	}
	/*
	 * ------------------------------------------------
	 * FIN METODOS UTILITARIOS
	 * -------------------------------------------------
	 */
}	