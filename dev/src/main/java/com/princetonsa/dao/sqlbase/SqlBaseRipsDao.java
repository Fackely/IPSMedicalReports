/*
 * Created on Jun 10, 2005
 *
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;

/**
 * @author sebacho 
 *
 */
public class SqlBaseRipsDao {
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseRipsDao.class);
	
	//************************************************************************************************************************
	//*******************QUERY'S RIPS FACTURACION******************************************************************************
	//************************************************************************************************************************
	/**
	 * Sección WHERE común para la consulta RIPS por Factura
	 */
	private static final String consultaFactura_WHERE_Str = " " +
		"f.convenio=? AND "+ 
		"(to_char(f.fecha,'"+ConstantesBD.formatoFechaBD+"') BETWEEN ? AND ?) AND "+ 
		"f.institucion=? AND "+
		"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" " +
		"ORDER BY f.consecutivo_factura ";
	
	
	
	
	/**
	 * Sección WHERE común para la consulta RIPS por cuenta Cobro
	 */
	private static final String consultaCuentaCobro_WHERE_Str = " "+
		"f.numero_cuenta_cobro=? AND "+ 
		"f.institucion=? AND "+
		"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" "+
		"ORDER BY f.consecutivo_factura ";
	
	/**
	 * Cadena para la consulta del archivo AF por Factura
	 */
	private static final String consultaFacturaAFStr="SELECT "+ 
		"f.consecutivo_factura, coalesce(ins.pref_factura, '')||f.consecutivo_factura AS consecutivo, "+
		"f.fecha AS fecha, "+
		"f.valor_bruto_pac AS valor_bruto, "+
		"f.valor_convenio AS valor_convenio, "+
		"f.cuenta AS cuenta, "+
		"p.numero_identificacion AS numero_identificacion, "+
		"p.tipo_identificacion AS tipo_identificacion, "+
		"c.indicativo_acc_transito AS indicativo_transito, "+
		"CASE WHEN c.indicativo_acc_transito = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN getNroPolizaConvenio(f.sub_cuenta) ELSE '' END AS numero_poliza, "+
		"con.numero_contrato AS contrato, "+
		"administracion.getnombremedico(f.cod_paciente) AS paciente, "+
		"getnombreviaingreso(f.via_ingreso) AS via_ingreso "+ 
		"FROM facturas f "+ 
		"INNER JOIN cuentas c ON(c.id=f.cuenta) "+ 
		"INNER JOIN contratos con ON(con.convenio=f.convenio) "+ 
		"INNER JOIN personas p ON(f.cod_paciente=p.codigo) "+
		"INNER JOIN instituciones ins ON (ins.codigo=f.institucion) "+
		"WHERE "+ 
		"(to_char(f.fecha,'"+ConstantesBD.formatoFechaBD+"') BETWEEN to_char(con.fecha_inicial,'"+ConstantesBD.formatoFechaBD+"') AND to_char(con.fecha_final,'"+ConstantesBD.formatoFechaBD+"')) AND  " +
		consultaFactura_WHERE_Str;
	
	
	
	/**
	 * Cadena para la consulta del archivo AF por Factura para la interfaz ax_rips
	 */
	private static final String consultaFacturaAFAxRipsStr="SELECT "+ 
		"f.consecutivo_factura, coalesce(ins.pref_factura, '')||f.consecutivo_factura AS consecutivo, "+
		"f.fecha AS fecha, "+
		"f.valor_bruto_pac AS valor_bruto, "+
		"f.valor_convenio AS valor_convenio, "+
		"f.cuenta AS cuenta, "+
		"p.numero_identificacion AS numero_identificacion, "+
		"p.tipo_identificacion AS tipo_identificacion, "+
		"c.indicativo_acc_transito AS indicativo_transito, "+
		"CASE WHEN c.indicativo_acc_transito = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN getNroPolizaConvenio(f.sub_cuenta) ELSE '' END AS numero_poliza, "+
		"con.numero_contrato AS contrato, "+
		"administracion.getnombremedico(f.cod_paciente) AS paciente, "+
		"getnombreviaingreso(f.via_ingreso) AS via_ingreso, " +
		"cv.codigo_min_salud as codigo_min_salud, " +
		"cv.tipo_regimen as tipo_regimen, " +
		"cv.nombre as nombre_convenio, " +
		"cv.plan_beneficios as plan_beneficios "+ 
		"FROM facturas f "+ 
		"INNER JOIN cuentas c ON(c.id=f.cuenta) "+ 
		"INNER JOIN contratos con ON(con.convenio=f.convenio) "+ 
		"INNER JOIN personas p ON(f.cod_paciente=p.codigo) " +
		"INNER JOIN convenios cv ON(cv.codigo=f.convenio) "+
		"INNER JOIN instituciones ins ON (ins.codigo=f.institucion) ";
	
	/**
	 * Cadena para la consulta del archivo AF por cuenta de cobro
	 */
	private static final String consultaCuentaCobroAFStr="SELECT "+ 
		"f.consecutivo_factura, coalesce(ins.pref_factura, '')||f.consecutivo_factura AS consecutivo, "+
		"f.fecha AS fecha, "+
		"f.valor_bruto_pac AS valor_bruto, "+
		"f.valor_convenio AS valor_convenio, "+
		"f.cuenta AS cuenta, "+
		"c.indicativo_acc_transito AS indicativo_transito, "+
		"CASE WHEN c.indicativo_acc_transito = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN getNroPolizaConvenio(f.sub_cuenta) ELSE '' END AS numero_poliza, "+
		"cc.fecha_inicial AS fecha_inicial, "+
		"cc.fecha_final AS fecha_final, "+
		"con.numero_contrato AS contrato, "+
		"p.numero_identificacion AS numero_identificacion, "+
		"p.tipo_identificacion As tipo_identificacion, "+
		"administracion.getnombremedico(f.cod_paciente) AS paciente, "+
		"getnombreviaingreso(f.via_ingreso) AS via_ingreso "+ 
		"FROM facturas f "+ 
		"INNER JOIN cuentas c ON(c.id=f.cuenta) "+ 
		"INNER JOIN cuentas_cobro cc ON(f.numero_cuenta_cobro=cc.numero_cuenta_cobro) "+ 
		"INNER JOIN contratos con ON(con.convenio=cc.convenio) "+ 
		"INNER JOIN personas p ON(f.cod_paciente=p.codigo) "+
		"INNER JOIN instituciones ins ON (ins.codigo=f.institucion) "+
		"WHERE "+ 
		"(to_char(f.fecha,'"+ConstantesBD.formatoFechaBD+"') BETWEEN to_char(con.fecha_inicial,'"+ConstantesBD.formatoFechaBD+"') AND to_char(con.fecha_final,'"+ConstantesBD.formatoFechaBD+"')) AND  " +
		consultaCuentaCobro_WHERE_Str;
	
	
	/**
	 * Seccion select de la consulta del archivo AD
	 */
	private static final String consultaAD_SELECT_Str = "SELECT "+ 
		"f.consecutivo_factura, coalesce(ins.pref_factura, '')||f.consecutivo_factura AS consecutivo, "+ 
		"f.cuenta AS cuenta, "+ 
		"p.numero_identificacion AS numero_identificacion, "+ 
		"p.tipo_identificacion As tipo_identificacion, "+ 
		"administracion.getnombremedico(f.cod_paciente) AS paciente, "+ 
		"getnombreviaingreso(f.via_ingreso) AS via_ingreso, "+ 
		"CASE WHEN dfs.tipo_cargo = "+ConstantesBD.codigoTipoCargoServicios+" THEN " +
			"s.naturaleza_servicio " +
		"ELSE " +
			"coalesce(na.codigo_rips,'') " +
		"END AS naturaleza_servicio, "+
		"dfs.cantidad_cargo AS cantidad_cargo, "+
		"dfs.valor_total AS valor_total, " +
		"CASE WHEN dfs.tipo_cargo = "+ConstantesBD.codigoTipoCargoServicios+" THEN " +
			"dfs.servicio " +
		"ELSE " +
			"dfs.articulo " +
		"END AS serv_art, " +
		"dfs.tipo_cargo AS tipo_cargo, "+
		"sol.consecutivo_ordenes_medicas AS orden," +
		"coalesce(s.tipo_servicio,'') AS tipo_servicio, " +
		"coalesce(na.es_medicamento,'') AS es_medicamento, " +
		"coalesce(na.es_pos,'') As es_pos " +
		
		/**
		 
		"CASE WHEN dfs.tipo_cargo = "+ConstantesBD.codigoTipoCargoServicios+" THEN " +
			"getNumServNaturaleza(f.consecutivo_factura,s.naturaleza_servicio,f.institucion) " +
		"ELSE " +
			"getNumServNaturaleza(f.consecutivo_factura,a.naturaleza,f.institucion) " +
		"END AS cantidad, "+ 
		"CASE WHEN dfs.tipo_cargo = "+ConstantesBD.codigoTipoCargoServicios+" THEN " +
			"getTotalServNaturaleza(f.consecutivo_factura,s.naturaleza_servicio,f.institucion) " +
		"ELSE " +
			"getTotalServNaturaleza(f.consecutivo_factura,a.naturaleza,f.institucion) " +
		"END AS total "+**/ 
		"FROM facturas f ";
		
	/**
	 * Seccion inner 01 de la consulta del archivo AD
	 */
	private static final String consultaAD_INNER_01_Str = ""+
		"INNER JOIN personas p ON(f.cod_paciente=p.codigo) "+
		"INNER JOIN det_factura_solicitud dfs ON(dfs.factura=f.codigo) "+ 
		"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dfs.solicitud) " +
		"INNER JOIN instituciones ins ON (ins.codigo=f.institucion) "+
		"LEFT OUTER JOIN servicios s ON(dfs.servicio=s.codigo) "+ 
		"LEFT OUTER JOIN articulo a ON(dfs.articulo=a.codigo) "+
		"LEFT OUTER JOIN naturaleza_articulo na ON(na.acronimo=a.naturaleza AND na.institucion = a.institucion) ";
	
	/**
	 * Seccion inner 02 de la consulta del archivo AD
	 */
	private static final String consultaAD_INNER_02_str = " "+
		"INNER JOIN cuentas_cobro cc ON(cc.numero_cuenta_cobro=f.numero_cuenta_cobro) ";
	
	/**
	 * Seccion WHERE de la consulta del archivo AD por factura
	 */
	private static final String consultaAD_WHERE_Factura_Str = " "+
		"WHERE "+
		"f.convenio=? AND "+ 
		"(to_char(f.fecha,'"+ConstantesBD.formatoFechaBD+"') BETWEEN ? AND ?) AND "+
		"f.institucion=? AND "+
		"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" "+
		/**"GROUP BY "+ 
		"f.consecutivo_factura,f.via_ingreso,f.institucion,f.cuenta,f.cod_paciente,p.numero_identificacion,p.tipo_identificacion,dfs.tipo_cargo,s.naturaleza_servicio,a.naturaleza "+**/
		"ORDER BY consecutivo,naturaleza_servicio";
	
	
	
	/**
	 * Seccion WHERE de la consulta del archivo AD por cuenta de cobro
	 */
	private static final String consultaAD_WHERE_CuentaCobro_Str = " "+
		"WHERE "+
		"f.numero_cuenta_cobro=? AND "+
		"f.institucion=? AND "+
		"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" "+
		/**"GROUP BY " +
		"f.consecutivo_factura,f.via_ingreso,f.institucion,f.cuenta,f.cod_paciente,cc.fecha_inicial,cc.fecha_final,p.numero_identificacion,p.tipo_identificacion,dfs.tipo_cargo,s.naturaleza_servicio,a.naturaleza "+**/
		"ORDER BY consecutivo_factura,naturaleza_servicio";
	
		
	/**
	 * cadena para la consulta del archivo US por factura
	 */
	private static final String consultaFacturaUS_SELECT_Str="SELECT "+ 
		"f.consecutivo_factura, coalesce(ins.pref_factura, '')||f.consecutivo_factura AS consecutivo, "+
		"f.cuenta AS cuenta, "+
		"c.fecha_apertura AS fecha_apertura, "+
		"p.tipo_identificacion AS tipo_identificacion, "+
		"p.numero_identificacion AS numero_identificacion, "+
		"p.primer_apellido AS primer_apellido, "+
		"p.segundo_apellido AS segundo_apellido, "+
		"p.primer_nombre AS primer_nombre, "+
		"p.segundo_nombre AS segundo_nombre, "+
		"p.fecha_nacimiento AS fecha_nacimiento, "+
		"p.sexo AS sexo, "+
		"p.codigo_departamento_vivienda AS depto_vivienda, "+
		"p.codigo_ciudad_vivienda AS ciudad_vivienda, "+
		"pac.zona_domicilio AS zona_domicilio, "+
		"administracion.getnombremedico(f.cod_paciente) AS paciente, "+
		"getnombreviaingreso(f.via_ingreso) AS via_ingreso, "+
		"c.desplazado AS desplazado, "+
		"cv.codigo_min_salud as codigo_min_salud, " +
		"cv.tipo_regimen as tipo_regimen, " +
		"cv.nombre as nombre_convenio, " +
		"cv.plan_beneficios as plan_beneficios "+
		"FROM facturas f "+ 
		"INNER JOIN cuentas c ON(c.id=f.cuenta) "+ 
		"INNER JOIN personas p ON(p.codigo=f.cod_paciente) "+ 
		"INNER JOIN pacientes pac ON(p.codigo=pac.codigo_paciente) " +
		"INNER JOIN convenios cv ON(cv.codigo=f.convenio) "+
		"INNER JOIN instituciones ins ON (ins.codigo=f.institucion) ";
	
	/**
	 * Sección WHERE para la consulta del archivo US por factura
	 */
	private static final String consultaFacturaUS_WHERE_Str = "" +
		"WHERE "+
		"f.convenio=? AND "+
		"(to_char(f.fecha,'"+ConstantesBD.formatoFechaBD+"') BETWEEN ? AND ?) AND "+ 
		"f.institucion=? AND "+ 
		"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" "+ 
		"ORDER BY p.tipo_identificacion,p.numero_identificacion";

	
	/**
	 * cadena para la consulta del archivo US por cuenta de cobro
	 */
	private static final String consultaCuentaCobroUSStr="SELECT "+ 
		"f.consecutivo_factura, coalesce(ins.pref_factura, '')||f.consecutivo_factura AS consecutivo, "+
		"f.cuenta AS cuenta, "+
		"c.fecha_apertura AS fecha_apertura, "+
		"p.tipo_identificacion AS tipo_identificacion, "+
		"p.numero_identificacion AS numero_identificacion, "+
		"p.primer_apellido AS primer_apellido, "+
		"p.segundo_apellido AS segundo_apellido, "+
		"p.primer_nombre AS primer_nombre, "+
		"p.segundo_nombre AS segundo_nombre, "+
		"p.fecha_nacimiento AS fecha_nacimiento,  "+
		"p.sexo AS sexo, "+
		"p.codigo_departamento_vivienda AS depto_vivienda, "+
		"p.codigo_ciudad_vivienda AS ciudad_vivienda, "+
		"pac.zona_domicilio AS zona_domicilio, "+
		"administracion.getnombremedico(f.cod_paciente) AS paciente, "+
		"getnombreviaingreso(f.via_ingreso) AS via_ingreso, "+
		"c.desplazado AS desplazado "+
		"FROM facturas f "+ 
		"INNER JOIN cuentas_cobro cc ON(cc.numero_cuenta_cobro=f.numero_cuenta_cobro) "+ 
		"INNER JOIN cuentas c ON(f.cuenta=c.id) "+ 
		"INNER JOIN personas p ON(f.cod_paciente=p.codigo) "+ 
		"INNER JOIN pacientes pac ON(p.codigo=pac.codigo_paciente) "+
		"INNER JOIN instituciones ins ON (ins.codigo=f.institucion) "+
		"WHERE "+ 
		"f.numero_cuenta_cobro=? AND "+ 
		"f.institucion=? AND "+ 
		"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" "+ 
		"ORDER BY p.tipo_identificacion,p.numero_identificacion";
		 
	
	/**
	 * Cadena para la consultar del archivo AC por factura
	 */
	private static final String consultaFacturaACStr="SELECT "+ 
		"f.consecutivo_factura, coalesce(ins.pref_factura, '')||f.consecutivo_factura AS consecutivo, "+
		"f.cuenta AS cuenta, "+
		"dfs.valor_total AS valor, " +
		"dfs.servicio AS serv_art, "+
		"p.tipo_identificacion AS tipo_identificacion, "+
		"p.numero_identificacion AS numero_identificacion, "+
		"MANEJOPACIENTE.GETAUTORXSUBCUENTASOLSERV(f.sub_cuenta, '"+ConstantesIntegridadDominio.acronimoSolicitud+"', s.numero_solicitud, dfs.servicio) AS numero_autorizacion, "+
		"s.consecutivo_ordenes_medicas AS orden, "+ 
		"serv.naturaleza_servicio AS naturaleza_servicio, "+
		"administracion.getnombremedico(f.cod_paciente) AS paciente, "+
		"getnombreviaingreso(f.via_ingreso) AS via_ingreso, "+
		"coalesce(getCodigoPropServicio2(dfs.servicio,?),'') AS codigo_consulta, "+
		"getFinalidadConsulta(dfs.solicitud,s.tipo) AS finalidad_consulta, "+
		"getCausaExterna(dfs.solicitud,s.tipo) AS causa_externa, "+
		"getTipoDiagPrincipal(dfs.solicitud,s.tipo) AS tipo_diag_ppal, "+
		"getAcronimoConsulta(dfs.solicitud,s.tipo,"+ValoresPorDefecto.getValorTrueParaConsultas()+",0) AS cod_diag_ppal, "+
		"getAcronimoConsulta(dfs.solicitud,s.tipo,"+ValoresPorDefecto.getValorFalseParaConsultas()+",1) AS cod_diag_rel1, "+
		"getAcronimoConsulta(dfs.solicitud,s.tipo,"+ValoresPorDefecto.getValorFalseParaConsultas()+",2) AS cod_diag_rel2, "+
		"getAcronimoConsulta(dfs.solicitud,s.tipo,"+ValoresPorDefecto.getValorFalseParaConsultas()+",3) AS cod_diag_rel3, "+
		"getFechaConsulta(dfs.solicitud,s.tipo) AS fecha_consulta "+   
		"FROM facturas f "+ 
		"INNER JOIN det_factura_solicitud dfs ON(dfs.factura=f.codigo) "+ 
		"INNER JOIN personas p ON(f.cod_paciente=p.codigo) "+ 
		"INNER JOIN solicitudes s ON(dfs.solicitud=s.numero_solicitud) " +
		"INNER JOIN servicios serv ON(serv.codigo=dfs.servicio and serv.tipo_servicio='"+ConstantesBD.codigoServicioInterconsulta+"') "+
		"INNER JOIN instituciones ins ON (ins.codigo=f.institucion) "+
		"WHERE ";
		//+		consultaFactura_WHERE_Str;
	
	/**
	 * cadena para la consulta del archivo AC por cuenta de cobro
	 */
	private static final String consultaCuentaCobroACStr="SELECT "+ 
		"f.consecutivo_factura, coalesce(ins.pref_factura, '')||f.consecutivo_factura AS consecutivo, "+
		"f.cuenta AS cuenta, "+
		"dfs.valor_total AS valor, " +
		"dfs.servicio AS serv_art, "+
		"p.tipo_identificacion AS tipo_identificacion, "+
		"p.numero_identificacion AS numero_identificacion, "+
		"cc.fecha_inicial AS fecha_inicial, "+
		"cc.fecha_final AS fecha_final, "+
		"MANEJOPACIENTE.GETAUTORXSUBCUENTASOLSERV(f.sub_cuenta, '"+ConstantesIntegridadDominio.acronimoSolicitud+"', s.numero_solicitud, dfs.servicio) AS numero_autorizacion, "+
		"s.consecutivo_ordenes_medicas AS orden, "+ 
		"serv.naturaleza_servicio AS naturaleza_servicio, "+
		"getFechaConsulta(dfs.solicitud,s.tipo) AS fecha_consulta, "+
		"administracion.getnombremedico(f.cod_paciente) AS paciente, "+
		"getnombreviaingreso(f.via_ingreso) AS via_ingreso, "+ 
		"CASE WHEN getCodigoPropServicio2(dfs.servicio,?) IS NULL THEN '' ELSE getCodigoPropServicio2(dfs.servicio,?) END AS codigo_consulta, "+
		"getFinalidadConsulta(dfs.solicitud,s.tipo) AS finalidad_consulta, "+
		"getCausaExterna(dfs.solicitud,s.tipo) AS causa_externa, "+
		"getAcronimoConsulta(dfs.solicitud,s.tipo,"+ValoresPorDefecto.getValorTrueParaConsultas()+",0) AS cod_diag_ppal, "+
		"getAcronimoConsulta(dfs.solicitud,s.tipo,"+ValoresPorDefecto.getValorFalseParaConsultas()+",1) AS cod_diag_rel1, "+
		"getAcronimoConsulta(dfs.solicitud,s.tipo,"+ValoresPorDefecto.getValorFalseParaConsultas()+",2) AS cod_diag_rel2, "+
		"getAcronimoConsulta(dfs.solicitud,s.tipo,"+ValoresPorDefecto.getValorFalseParaConsultas()+",3) AS cod_diag_rel3, "+
		"getTipoDiagPrincipal(dfs.solicitud,s.tipo) AS tipo_diag_ppal "+ 
		"FROM facturas f "+
		"INNER JOIN det_factura_solicitud dfs ON(dfs.factura=f.codigo) "+ 
		"INNER JOIN personas p ON(p.codigo=f.cod_paciente) "+ 
		"INNER JOIN cuentas_cobro cc ON(cc.numero_cuenta_cobro=f.numero_cuenta_cobro) "+ 
		"INNER JOIN solicitudes s ON(s.numero_solicitud=dfs.solicitud) " +
		"INNER JOIN servicios serv ON(serv.codigo=dfs.servicio and serv.tipo_servicio='"+ConstantesBD.codigoServicioInterconsulta+"') "+ 
		"INNER JOIN instituciones ins ON (ins.codigo=f.institucion) "+
		"WHERE " +
		consultaCuentaCobro_WHERE_Str;
	
	
	
	/**
	 * Sección SELECT para la consulta RIPS del archivo AH
	 */
	private static final String consultaAH_SELECT_Str = "SELECT DISTINCT "+
		"f.consecutivo_factura, coalesce(ins.pref_factura, '')||f.consecutivo_factura AS consecutivo, "+ 
		"f.cuenta AS cuenta, "+ 
		"p.numero_identificacion As numero_identificacion, "+ 
		"p.tipo_identificacion AS tipo_identificacion, "+ 
		"ah.origen_admision_hospitalaria AS via, "+ 
		"to_char(ah.fecha_admision,'DD/MM/YYYY') AS fecha_ingreso, "+ 
		"ah.hora_admision AS hora_ingreso, "+ 
		"vd.numero_verificacion AS numero_autorizacion, "+ 
		"CASE WHEN f.pac_entidad_subcontratada IS NULL THEN v.causa_externa ELSE ah.causa_externa END AS causa_externa, "+ //varía por subcontratación 
		"CASE WHEN f.pac_entidad_subcontratada IS NULL THEN vh.diagnostico_ingreso ELSE ah.diagnostico_admision END AS diag_ingreso, "+ //varía por subcontratación 
		//diagnostico de egreso principal
		"CASE WHEN e.codigo_medico IS NOT NULL AND e.usuario_responsable IS NOT NULL THEN " +
			"e.diagnostico_principal " +
		"ELSE " +
			"coalesce(historiaclinica.getultimodiagvaloracion(v.numero_solicitud,"+ValoresPorDefecto.getValorTrueParaConsultas()+",0),'') " +
		"END As diag_egreso, "+
		//diagnostico de egreso relacionado N° 1
		"CASE WHEN e.codigo_medico IS NOT NULL AND e.usuario_responsable IS NOT NULL THEN " +
			"CASE WHEN e.diagnostico_relacionado1 = '1' THEN '' ELSE e.diagnostico_relacionado1 END " +
		"ELSE " +
			"coalesce(historiaclinica.getultimodiagvaloracion(v.numero_solicitud,"+ValoresPorDefecto.getValorFalseParaConsultas()+",1),'') " +
		"END AS diag_egreso_rel1, "+ 
		//diagnostico de egreso relacionado N° 2
		"CASE WHEN e.codigo_medico IS NOT NULL AND e.usuario_responsable IS NOT NULL THEN " +
			"CASE WHEN e.diagnostico_relacionado2 = '1' THEN '' ELSE e.diagnostico_relacionado2 END " +
		"ELSE " +
			"coalesce(historiaclinica.getultimodiagvaloracion(v.numero_solicitud,"+ValoresPorDefecto.getValorFalseParaConsultas()+",2),'') " +
		"END AS diag_egreso_rel2, "+ 
		//diagnostico de egreso relacionado N° 3
		"CASE WHEN e.codigo_medico IS NOT NULL AND e.usuario_responsable IS NOT NULL THEN " +
			"CASE WHEN e.diagnostico_relacionado3 = '1' THEN '' ELSE e.diagnostico_relacionado3 END " +
		"ELSE " +
			"coalesce(historiaclinica.getultimodiagvaloracion(v.numero_solicitud,"+ValoresPorDefecto.getValorFalseParaConsultas()+",3),'') " +
		"END AS diag_egreso_rel3, "+ 
		///-----validaciones previas para el estado de salida-----------------
		"CASE WHEN e.estado_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN " +
			"'2' " +
		"ELSE " +
			"CASE WHEN e.estado_salida = "+ValoresPorDefecto.getValorFalseParaConsultas()+" OR (e.codigo_medico IS NULL AND e.usuario_responsable IS NULL ) THEN " +
				"'1' " +
			"ELSE " +
				"'' " +
			"END " +
		"END AS estado_salida, "+
		//---- validaciones previas para el diagnostico de muerte-----------------------------
		"CASE WHEN e.estado_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN " +
			"CASE WHEN e.diagnostico_muerte = '1' THEN " +
				"'' " +
			"ELSE " +
				"e.diagnostico_muerte " +
			"END " +
		"ELSE " +
			"'' " +
		"END AS diag_muerte, "+ 
		"CASE WHEN e.codigo_medico IS NOT NULL AND e.usuario_responsable IS NOT NULL THEN to_char(e.fecha_egreso,'DD/MM/YYYY') ELSE to_char(f.fecha,'DD/MM/YYYY') END AS fecha_egreso, "+ 
		"CASE WHEN e.codigo_medico IS NOT NULL AND e.usuario_responsable IS NOT NULL THEN e.hora_egreso ||'' ELSE f.hora||'' END AS hora_egreso, "+
		"CASE WHEN f.pac_entidad_subcontratada IS NULL THEN "+ //varía por subcontratacion
			"CASE WHEN ev.diagnostico_complicacion = '1' OR ev.diagnostico_complicacion IS NULL THEN '' ELSE ev.diagnostico_complicacion END " +
		"ELSE "+
			"coalesce(e.diagnostico_complicacion,'') "+
		"END AS diag_complicacion, "+ 
		"administracion.getnombremedico(f.cod_paciente) AS paciente, "+ 
		"getnombreviaingreso(f.via_ingreso) AS via_ingreso "+ 
		"FROM facturas f " +
		"INNER JOIN cuentas c ON(c.id = f.cuenta) " +
		"INNER JOIN instituciones ins ON (ins.codigo=f.institucion) ";
	

	
	/**
	 * Seccion LEFT OUTER para la consulta del archivo AH
	 */
	private static final String consultaAH_LEFTOUTER_Str = "" +
		"LEFT OUTER JOIN personas p ON(p.codigo=f.cod_paciente) "+ 
		"LEFT OUTER JOIN admisiones_hospi ah ON(ah.cuenta=f.cuenta) "+ 
		"LEFT OUTER JOIN solicitudes s ON(f.cuenta=s.cuenta AND s.tipo IN ("+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+")) "+
		"LEFT OUTER JOIN valoraciones v ON(v.numero_solicitud=s.numero_solicitud) "+ 
		"LEFT OUTER JOIN val_hospitalizacion vh ON(vh.numero_solicitud=v.numero_solicitud) "+ 
		"LEFT OUTER JOIN egresos e ON(f.cuenta=e.cuenta) "+
		"LEFT OUTER JOIN evoluciones ev ON(ev.valoracion=v.numero_solicitud AND ev.orden_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+") "+
		"left outer join sub_cuentas sc ON (sc.sub_cuenta= f.sub_cuenta) " +
		"left outer join verificaciones_derechos vd ON (vd.sub_cuenta= sc.sub_cuenta) ";;
	

	/**
	 * Sección WHERE para la consulta RIPS del archivo AH 
	 */
	private static final String consultaAH_WHERE_Str = " WHERE "+ 
		"f.via_ingreso = "+ConstantesBD.codigoViaIngresoHospitalizacion+" AND c.tipo_paciente = '"+ConstantesBD.tipoPacienteHospitalizado+"' AND ";
	
	
	/**
	 * Sección SELECT para la consulta RIPS del archivo AU
	 */
	private static final String consultaAU_SELECT_Str = "SELECT "+ 
		"f.consecutivo_factura, coalesce(ins.pref_factura, '')||f.consecutivo_factura AS consecutivo, "+ 
		"f.cuenta AS cuenta, "+ 
		"v.causa_externa AS causa_externa, "+ 
		"p.tipo_identificacion AS tipo_identificacion, "+ 
		"p.numero_identificacion As numero_identificacion, "+ 
		"to_char(au.fecha_ingreso_observacion,'DD/MM/YYYY') AS fecha_ingreso, "+ 
		"au.hora_ingreso_observacion AS hora_ingreso, "+ 
		"vd.numero_verificacion AS numero_autorizacion, "+ 
		"to_char(au.fecha_egreso_observacion,'DD/MM/YYYY') AS fecha_egreso, "+ 
		"au.hora_egreso_observacion AS hora_egreso, "+ 
		"e.diagnostico_principal As diag_egreso, "+ 
		"CASE WHEN e.diagnostico_relacionado1 = '1' THEN '' ELSE e.diagnostico_relacionado1 END AS diag_egreso_rel1, "+ 
		"CASE WHEN e.diagnostico_relacionado2 = '1' THEN '' ELSE e.diagnostico_relacionado2 END AS diag_egreso_rel2, "+ 
		"CASE WHEN e.diagnostico_relacionado3 = '1' THEN '' ELSE e.diagnostico_relacionado3 END AS diag_egreso_rel3, "+
		//cadena de CASE para la definición del destino de salida según regla RIPS
		"CASE WHEN " +
			"e.destino_salida = "+ConstantesBD.codigoDestinoSalidaDadoDeAlta+" OR " +
			"e.destino_salida = "+ConstantesBD.codigoDestinoSalidaOtro+" OR " +
			"e.destino_salida = "+ConstantesBD.codigoDestinoSalidaVoluntaria+" THEN " +
				"1 " + //Alta de Urgencias
		"ELSE " +
			"CASE WHEN e.destino_salida = "+ConstantesBD.codigoDestinoSalidaRemitidoOtroNivelComplejidad+" THEN " +
				"2 " + // Remisión a otor nivel de complejidad
			"ELSE " +
				"CASE WHEN e.destino_salida = "+ConstantesBD.codigoDestinoSalidaHospitalizacion+" THEN " +
					"3 " + //Hospitalización
				"ELSE " +
					"-1 " + // Código Inválido
				"END " +
			"END " +
		"END AS destino_salida, "+
		//----------validaciones previas para el estado de salida---------------------------------------------------------------
		"CASE WHEN e.estado_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN " +
			"'2' " +
		"ELSE " +
			"CASE WHEN e.estado_salida = "+ValoresPorDefecto.getValorFalseParaConsultas()+" THEN " +
				"'1' " +
			"ELSE " +
				"'' " +
			"END " +
		"END AS estado_salida, "+
		//---------validaciones previas para el diagnostico de muerte-----------------------------------------------------------
		"CASE WHEN e.estado_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN " +
			"CASE WHEN e.diagnostico_muerte = '1' THEN " +
				"'' " +
			"ELSE " +
				"e.diagnostico_muerte " +
			"END " +
		"ELSE " +
			"'' " +
		"END AS diag_muerte, "+ 
		"administracion.getnombremedico(f.cod_paciente) AS paciente, "+ 
		"getnombreviaingreso(f.via_ingreso) AS via_ingreso "+ 
		"FROM facturas f "+ 
		"INNER JOIN det_factura_solicitud dfs ON(dfs.factura=f.codigo) "+ 
		"INNER JOIN solicitudes s ON(s.numero_solicitud=dfs.solicitud) " + 
		"INNER JOIN valoraciones v ON(v.numero_solicitud=s.numero_solicitud) "+ 
		"INNER JOIN valoraciones_urgencias vu ON(vu.numero_solicitud=v.numero_solicitud AND vu.codigo_conducta_valoracion = "+ConstantesBD.codigoConductaSeguirCamaObservacion+" ) "+ 
		"INNER JOIN instituciones ins ON (ins.codigo=f.institucion) "+
		"LEFT OUTER JOIN personas p ON(p.codigo=f.cod_paciente) "+ 
		"LEFT OUTER JOIN admisiones_urgencias au ON(au.cuenta=f.cuenta) "+ 
		"LEFT OUTER JOIN egresos e ON(e.cuenta=f.cuenta)  " +
		"left outer join sub_cuentas sc ON (sc.sub_cuenta= f.sub_cuenta) " +
		"left outer join verificaciones_derechos vd ON (vd.sub_cuenta= sc.sub_cuenta) ";
	
	/**
	 * Seccion SELECT para la consulta de admisiones urgencias por entidades subcontratadas
	 */
	private static final String consultaAU_Subcontratadas_SELECT_Str = "SELECT "+ 
		"f.consecutivo_factura, coalesce(ins.pref_factura, '')||f.consecutivo_factura AS consecutivo, "+ 
		"f.cuenta AS cuenta, "+ 
		"au.causa_externa AS causa_externa, "+ 
		"p.tipo_identificacion AS tipo_identificacion, "+ 
		"p.numero_identificacion As numero_identificacion, "+ 
		"to_char(au.fecha_ingreso_observacion,'DD/MM/YYYY') AS fecha_ingreso, "+ 
		"au.hora_ingreso_observacion AS hora_ingreso, "+ 
		"vd.numero_verificacion AS numero_autorizacion, "+ 
		"to_char(au.fecha_egreso_observacion,'DD/MM/YYYY') AS fecha_egreso, "+ 
		"au.hora_egreso_observacion AS hora_egreso, "+ 
		"e.diagnostico_principal As diag_egreso, "+ 
		"CASE WHEN e.diagnostico_relacionado1 = '1' THEN '' ELSE e.diagnostico_relacionado1 END AS diag_egreso_rel1, "+ 
		"CASE WHEN e.diagnostico_relacionado2 = '1' THEN '' ELSE e.diagnostico_relacionado2 END AS diag_egreso_rel2, "+ 
		"CASE WHEN e.diagnostico_relacionado3 = '1' THEN '' ELSE e.diagnostico_relacionado3 END AS diag_egreso_rel3, "+
		//cadena de CASE para la definición del destino de salida según regla RIPS
		"CASE WHEN " +
			"e.destino_salida = "+ConstantesBD.codigoDestinoSalidaDadoDeAlta+" OR " +
			"e.destino_salida = "+ConstantesBD.codigoDestinoSalidaOtro+" OR " +
			"e.destino_salida = "+ConstantesBD.codigoDestinoSalidaVoluntaria+" THEN " +
				"1 " + //Alta de Urgencias
		"ELSE " +
			"CASE WHEN e.destino_salida = "+ConstantesBD.codigoDestinoSalidaRemitidoOtroNivelComplejidad+" THEN " +
				"2 " + // Remisión a otor nivel de complejidad
			"ELSE " +
				"CASE WHEN e.destino_salida = "+ConstantesBD.codigoDestinoSalidaHospitalizacion+" THEN " +
					"3 " + //Hospitalización
				"ELSE " +
					"-1 " + // Código Inválido
				"END " +
			"END " +
		"END AS destino_salida, "+
		//----------validaciones previas para el estado de salida---------------------------------------------------------------
		"CASE WHEN e.estado_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN " +
			"'2' " +
		"ELSE " +
			"CASE WHEN e.estado_salida = "+ValoresPorDefecto.getValorFalseParaConsultas()+" THEN " +
				"'1' " +
			"ELSE " +
				"'' " +
			"END " +
		"END AS estado_salida, "+
		//---------validaciones previas para el diagnostico de muerte-----------------------------------------------------------
		"CASE WHEN e.estado_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN " +
			"CASE WHEN e.diagnostico_muerte = '1' THEN " +
				"'' " +
			"ELSE " +
				"e.diagnostico_muerte " +
			"END " +
		"ELSE " +
			"'' " +
		"END AS diag_muerte, "+ 
		"administracion.getnombremedico(f.cod_paciente) AS paciente, "+ 
		"getnombreviaingreso(f.via_ingreso) AS via_ingreso "+ 
		"FROM facturas f "+ 
		"INNER JOIN det_factura_solicitud dfs ON(dfs.factura=f.codigo) "+
		"INNER JOIN instituciones ins ON (ins.codigo=f.institucion) " +
		"LEFT OUTER JOIN personas p ON(p.codigo=f.cod_paciente) "+ 
		"LEFT OUTER JOIN admisiones_urgencias au ON(au.cuenta=f.cuenta) "+ 
		"LEFT OUTER JOIN egresos e ON(e.cuenta=f.cuenta)  " +
		"left outer join sub_cuentas sc ON (sc.sub_cuenta= f.sub_cuenta) " +
		"left outer join verificaciones_derechos vd ON (vd.sub_cuenta= sc.sub_cuenta) " ;;
	
	
	/**
	 * Seccion WHERE FACTURA para la consulta RIPS por Factura del archivo AU
	 */
	private static final String consultaAU_WHERE_FACTURA_Str = " WHERE " +
		"f.convenio=? AND "+ 
		"(to_char(f.fecha,'"+ConstantesBD.formatoFechaBD+"') BETWEEN ? AND ?) AND "+ 
		"f.institucion=? AND "+
		"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" ";
	
	
	/**
	 * Sección WHERE CUENTA COBRO para la consulta RIPS por cuenta de cobro del archivo AU
	 */
	private static final String consultaAU_WHERE_CUENTA_COBRO_Str = " WHERE " +
		"f.numero_cuenta_cobro=? AND "+ 
		"f.institucion=? AND "+
		"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" ";
	
	/**
	 * Sección WHERE para la consulta RIPS del archivo AU
	 */
	private static final String consultaAU_WHERE_Str = " AND f.via_ingreso = "+ConstantesBD.codigoViaIngresoUrgencias+" " +
		" ";
	
	
	
	/**
	 * Sección SELECT para la consulta RIPS del archivo AM
	 */
	private static final String consultaAM_SELECT_Str = "SELECT "+ 
		"f.consecutivo_factura, coalesce(ins.pref_factura, '')||f.consecutivo_factura AS consecutivo, " + 
		"f.cuenta AS cuenta, "+ 
		"dfs.cantidad_cargo AS numero_unidades, "+ 
		"dfs.valor_cargo AS valor_unitario, "+ 
		"dfs.valor_total AS valor_total," +
		"dfs.articulo AS serv_art,  "+ 
		"coalesce(na.codigo_rips,'') AS naturaleza, "+ 
		"coalesce(na.es_pos,'') AS es_pos, "+
		//"CASE WHEN na.es_pos = '"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' THEN a.minsalud ELSE '' END AS codigo_medicamento, "+
		" case " +
			"when a.numero_expediente is not null and a.cons_present_comercial is not null " +
			"then a.numero_expediente ||'-'|| a.cons_present_comercial " +
			"else '' " +
		"end as codigo_medicamento, " +
		"CASE WHEN na.es_pos = '"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' THEN '1' ELSE '2' END AS tipo, " +
		"coalesce(a.descripcion_alterna, a.descripcion) AS nombre, "+ 
		"CASE WHEN na.es_pos = '"+ValoresPorDefecto.getValorFalseCortoParaConsultas()+"' OR na.es_pos IS NULL THEN a.concentracion ELSE '' END AS concentracion, "+ 
		"CASE WHEN na.es_pos = '"+ValoresPorDefecto.getValorFalseCortoParaConsultas()+"' OR na.es_pos IS NULL THEN getformafarmaceuticaarticulo(a.codigo) ELSE '' END AS forma_farmaceutica, "+ 
		"CASE WHEN na.es_pos = '"+ValoresPorDefecto.getValorFalseCortoParaConsultas()+"' OR na.es_pos IS NULL THEN getunidadmedidaarticulo(a.codigo) ELSE '' END AS unidad_medida, "+ 
		"CASE WHEN p.primer_nombre IS NULL THEN '' ELSE p.primer_nombre END || "+
		"CASE WHEN p.segundo_nombre IS NULL THEN '' ELSE p.segundo_nombre END || "+ 
		"CASE WHEN p.primer_apellido IS NULL THEN '' ELSE p.primer_apellido END || "+ 
		"CASE WHEN p.segundo_apellido IS NULL THEN '' ELSE p.segundo_apellido END "+
		"AS paciente, "+ 
		"p.tipo_identificacion AS tipo_identificacion, "+ 
		"p.numero_identificacion AS numero_identificacion, "+ 
		"s.consecutivo_ordenes_medicas AS orden, "+  
		"getnombreviaingreso(f.via_ingreso) AS via_ingreso, "+
		//"getAutorizacionRips(dfs.codigo,dfs.tipo_cargo) AS numero_autorizacion "+ 
		"MANEJOPACIENTE.GETAUTORXSUBCUENTASOLART(f.sub_cuenta, '"+ConstantesIntegridadDominio.acronimoSolicitud+"', s.numero_solicitud, dfs.articulo) AS numero_autorizacion "+
		"FROM facturas f ";
	
	private static final String consultaAM_LEFTOUTER_Str = "" +
		"INNER JOIN det_factura_solicitud dfs ON(dfs.factura=f.codigo and dfs.tipo_cargo="+ConstantesBD.codigoTipoCargoArticulos+") "+ 
		"INNER JOIN articulo a ON(a.codigo=dfs.articulo) " +
		"INNER JOIN naturaleza_articulo na ON(na.acronimo=a.naturaleza AND na.institucion=a.institucion AND na.es_medicamento = '"+ConstantesBD.acronimoSi+"') "+ 
		"LEFT OUTER JOIN personas p ON(f.cod_paciente=p.codigo) "+ 
		"LEFT OUTER JOIN solicitudes s ON(s.numero_solicitud=dfs.solicitud) "; 

	/**
	 * Sección WHERE para la consulta RIPS del archivo AM
	 *
	private static final String consultaAM_WHERE_Str = " WHERE "+
		"dfs.tipo_cargo="+ConstantesBD.codigoTipoCargoArticulos+" AND "+
		"a.naturaleza IN('"+ConstantesBD.codigoNaturalezaArticuloMedicamentosNoPos+"','"+ConstantesBD.codigoNaturalezaArticuloMedicamentosPos+"') AND ";
	**/
	
	/**
	 * Sección SELECT para la consulta RIPS ARTICULOS del archivo AT
	 */
	private static final String consultaAT_SELECT_ARTICULOS_Str = "SELECT "+ 
		"f.consecutivo_factura, coalesce(ins.pref_factura, '')||f.consecutivo_factura AS consecutivo, "+ 
		"f.cuenta AS cuenta, "+ 
		"dfs.cantidad_cargo AS cantidad, "+ 
		"dfs.valor_cargo AS valor_unitario, "+ 
		"dfs.valor_total AS valor_total, "+ 
		"dfs.tipo_cargo AS tipo_cargo," +
		"dfs.articulo AS serv_art, "+ 
		"p.tipo_identificacion AS tipo_identificacion, "+ 
		"p.numero_identificacion AS numero_identificacion, "+ 
		//"getAutorizacionRips(dfs.codigo,dfs.tipo_cargo) AS numero_autorizacion, "+ 
		"MANEJOPACIENTE.GETAUTORXSUBCUENTASOLART(f.sub_cuenta, '"+ConstantesIntegridadDominio.acronimoSolicitud+"', sol.numero_solicitud, dfs.articulo) AS numero_autorizacion, "+
		"'1' AS tipo_servicio, "+ //Para materiales e insumos es 1
		"'' AS codigo_servicio, "+ 
		"a.descripcion AS nombre_servicio, "+ 
		"administracion.getnombremedico(f.cod_paciente) AS paciente, "+ 
		"getnombreviaingreso(f.via_ingreso) AS via_ingreso, "+ 
		"coalesce(na.codigo_rips,'') AS naturaleza, "+ 
		"sol.consecutivo_ordenes_medicas AS orden "+ 
		"FROM facturas f "+ 
		"INNER JOIN det_factura_solicitud dfs ON(dfs.factura=f.codigo) "+ 
		"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dfs.solicitud) "+  
		"INNER JOIN articulo a ON(a.codigo=dfs.articulo) " +
		"INNER JOIN naturaleza_articulo na ON(na.acronimo = a.naturaleza AND na.institucion = a.institucion AND na.es_medicamento = '"+ConstantesBD.acronimoNo+"') " + 
		"INNER JOIN instituciones ins ON (ins.codigo=f.institucion) "+
		"LEFT OUTER JOIN personas p ON(p.codigo=f.cod_paciente) ";
	
	private static final String consultaAT_SELECT_SERVICIOS_Str = "SELECT "+ 
		"f.consecutivo_factura, coalesce(ins.pref_factura, '')||f.consecutivo_factura AS consecutivo, "+ 
		"f.cuenta AS cuenta, "+ 
		"dfs.cantidad_cargo AS cantidad, "+ 
		"dfs.valor_cargo AS valor_unitario, "+ 
		"dfs.valor_total AS valor_total, "+ 
		"dfs.tipo_cargo AS tipo_cargo, " +
		"dfs.servicio AS serv_art,"+ 
		"p.tipo_identificacion AS tipo_identificacion, "+ 
		"p.numero_identificacion AS numero_identificacion, "+ 
		//"getAutorizacionRips(dfs.codigo,dfs.tipo_cargo) AS numero_autorizacion, "+ 
		"MANEJOPACIENTE.GETAUTORXSUBCUENTASOLSERV(f.sub_cuenta, '"+ConstantesIntegridadDominio.acronimoSolicitud+"', sol.numero_solicitud, dfs.servicio) AS numero_autorizacion, "+
		"getTipoServRips(dfs.codigo,dfs.tipo_cargo) AS tipo_servicio, "+ 
		"CASE WHEN getCodigoPropServicio2(dfs.servicio,?) IS NULL OR " +
			"getCodigoPropServicio2(dfs.servicio,?) = '' OR " +
			"getnombreservicio(dfs.servicio,?) IS NULL OR  " +
			"getnombreservicio(dfs.servicio,?) = '' THEN "+ 
				"getCodigoPropServicio2(dfs.servicio,"+ConstantesBD.codigoTarifarioCups+") "+  
		"ELSE "+ 
				"getCodigoPropServicio2(dfs.servicio,?) ||'' "+  
		"END AS codigo_servicio, "+ 
		"CASE WHEN getCodigoPropServicio2(dfs.servicio,?) IS NULL OR " +
			"getCodigoPropServicio2(dfs.servicio,?) = '' OR  " +
			"getnombreservicio(dfs.servicio,?) IS NULL OR  " +
			"getnombreservicio(dfs.servicio,?) = '' THEN "+ 
				"getnombreservicio(dfs.servicio,"+ConstantesBD.codigoTarifarioCups+") "+  
		"ELSE "+  
			"getnombreservicio(dfs.servicio,?) "+  
		"END AS nombre_servicio, "+ 
		"administracion.getnombremedico(f.cod_paciente) AS paciente, "+ 
		"getnombreviaingreso(f.via_ingreso) AS via_ingreso, "+ 
		"s.naturaleza_servicio AS naturaleza, "+ 
		"sol.consecutivo_ordenes_medicas AS orden "+ 
		"FROM facturas f "+ 
		"INNER JOIN det_factura_solicitud dfs ON(dfs.factura=f.codigo) "+ 
		"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dfs.solicitud) " +
		"INNER JOIN instituciones ins ON (ins.codigo=f.institucion) "+
		"INNER JOIN servicios s ON(s.codigo=dfs.servicio AND " +
			"s.naturaleza_servicio IN(" +
			"'"+ConstantesBD.codigoNaturalezaServicioEstancias+"'," +
			"'"+ConstantesBD.codigoNaturalezaServicioMaterialesInsumos+"'," +
			"'"+ConstantesBD.codigoNaturalezaServicioHonorarios+"'," +
			"'"+ConstantesBD.codigoNaturalezaServicioTrasladoPaciente+"')) "+ 
		"LEFT OUTER JOIN personas p ON(p.codigo=f.cod_paciente) ";
	
	/**
	 * Seccion WHERe para la consulta RIPS por factura del archivo AT
	 */
	private static final String consultaAT_WHERE_FACTURA_Str = " WHERE "+ 
		"f.convenio=? AND "+ 
		"(to_char(f.fecha,'"+ConstantesBD.formatoFechaBD+"') BETWEEN ? AND ?) AND "+ 
		"f.institucion=? AND "+ 
		"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada;
	
	
	/**
	 * Seccion WHERE para la consulta RIPS por cuenta de cobro del archivo AT
	 */
	private static final String consultaAT_WHERE_CUENTA_COBRO_Str = " WHERE " +
		"f.numero_cuenta_cobro=? AND "+ 
		"f.institucion=? AND "+
		"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" ";
	
	/**
	 * Sección SELECT para la consulta RIPS del archivo AP
	 */
	private static final String consultaAP_SELECT_Str = "SELECT "+ 
		"f.consecutivo_factura, coalesce(ins.pref_factura, '')||f.consecutivo_factura AS consecutivo, "+ 
		"f.cuenta AS cuenta, "+ 
		"dfs.valor_total As valor, " +
		"dfs.servicio AS serv_art, "+ 
		"p.tipo_identificacion AS tipo_identificacion, "+ 
		"p.numero_identificacion AS numero_identificacion, "+ 
		"s.naturaleza_servicio AS naturaleza_servicio, "+ 
		"s.tipo_servicio AS tipo_servicio, "+ 
		"sol.consecutivo_ordenes_medicas AS orden, "+
		"getFechaProcedimiento(dfs.solicitud,s.tipo_servicio,dfs.tipo_solicitud) AS fecha_procedimiento, "+ 
		
		//"getAutorizacionRips2(dfs.solicitud,s.tipo_servicio,dfs.servicio,dfs.tipo_solicitud) AS numero_autorizacion, "+ 
		"MANEJOPACIENTE.GETAUTORXSUBCUENTASOLSERV(f.sub_cuenta, '"+ConstantesIntegridadDominio.acronimoSolicitud+"', sol.numero_solicitud, dfs.servicio) AS numero_autorizacion, "+
		
		"getCodigoPropServicio2(dfs.servicio,?)  AS codigo_procedimiento, "+ 
		"getFinalidadServicio(s.tipo_servicio,dfs.servicio,dfs.solicitud,dfs.tipo_solicitud) AS finalidad_procedimiento, "+ 
		//campo de personal atiende (Sólo para partos y cesáreas)
		"CASE WHEN s.tipo_servicio = '"+ConstantesBD.codigoServicioPartosCesarea+"' THEN getPersonalAtiende(dfs.solicitud,dfs.servicio,f.institucion,dfs.tipo_solicitud) ELSE 0 END AS personal_atiende, "+
		"getAcronimoProcedimiento(dfs.solicitud,dfs.servicio,"+ValoresPorDefecto.getValorTrueParaConsultas()+","+ValoresPorDefecto.getValorFalseParaConsultas()+",s.tipo_servicio,dfs.tipo_solicitud) AS cod_diag_ppal, "+
		"getAcronimoProcedimiento(dfs.solicitud,dfs.servicio,"+ValoresPorDefecto.getValorFalseParaConsultas()+","+ValoresPorDefecto.getValorFalseParaConsultas()+",s.tipo_servicio,dfs.tipo_solicitud) AS cod_diag_rel, "+
		"getAcronimoProcedimiento(dfs.solicitud,dfs.servicio,"+ValoresPorDefecto.getValorFalseParaConsultas()+","+ValoresPorDefecto.getValorTrueParaConsultas()+",s.tipo_servicio,dfs.tipo_solicitud) AS cod_diag_com, "+ 
		//campo de la forma de realizacion (Sólo para partos y cesareas y quirúgicos y no cruentos)
		"CASE WHEN s.tipo_servicio IN ('"+ConstantesBD.codigoServicioPartosCesarea+"','"+ConstantesBD.codigoServicioQuirurgico+"','"+ConstantesBD.codigoServicioNoCruentos+"') THEN getFormaRealizacion(dfs.solicitud,dfs.servicio,dfs.tipo_solicitud) ELSE 0 END AS forma_realizacion, "+ 
		"getAmbitoRealizacion(f.via_ingreso,getTipoPacSegCuenta(f.cuenta)) AS ambito_realizacion, "+
		"administracion.getnombremedico(f.cod_paciente) AS paciente, "+ 
		"getnombreviaingreso(f.via_ingreso) AS via_ingreso "+
		"FROM facturas f "+ 
		"INNER JOIN det_factura_solicitud dfs ON(dfs.factura=f.codigo) "+
		"INNER JOIN instituciones ins ON (ins.codigo=f.institucion) "+
		"INNER JOIN servicios s ON(s.codigo=dfs.servicio AND "+ 
		"s.tipo_servicio IN ('"+ConstantesBD.codigoServicioProcedimiento+"'," +
		"'"+ConstantesBD.codigoServicioPartosCesarea+"',"+
		"'"+ConstantesBD.codigoServicioQuirurgico+"'," +
		"'"+ConstantesBD.codigoServicioNoCruentos+"',"+
		"'"+ConstantesBD.codigoServicioPaquetes+"')  ) "+
		"LEFT OUTER JOIN personas p ON (p.codigo=f.cod_paciente) "+ 
		"LEFT OUTER JOIN solicitudes sol ON(sol.numero_solicitud=dfs.solicitud) " +
		"WHERE ";
		
	
	
	
	
	/**
	 * Sección SELECT para la consulta RIPS del archivo AN 
	 */
	private static final String consultaAN_SELECT_Str = "SELECT DISTINCT "+ 
		"f.consecutivo_factura, coalesce(ins.pref_factura, '')||f.consecutivo_factura AS consecutivo, "+ 
		"f.cuenta AS cuenta, " +
		"dfs.servicio AS serv_art, "+ 
		"s.consecutivo_ordenes_medicas AS orden, "+ 
		"p.tipo_identificacion AS tipo_identificacion, "+ 
		"p.numero_identificacion AS numero_identificacion, "+ 
		"p1.tipo_identificacion AS tipo_id_madre, "+ 
		"p1.numero_identificacion AS numero_id_madre, "+ 
		"ip.semana_gestacional AS edad_gestacional, "+
		"CASE WHEN ip.control_prenatal = '"+ConstantesBD.acronimoSi+"' THEN 1 ELSE 2 END AS control_prenatal, "+ 
		"to_char(ih.fecha_nacimiento,'DD/MM/YYYY') AS fecha_nacimiento, "+ 
		"ih.hora_nacimiento AS hora_nacimiento, "+ 
		"CASE WHEN ih.sexo = 1 THEN 'M' ELSE 'F' END AS sexo, "+ 
		"ih.peso_egreso As peso, "+ 
		"ih.diagnostico_rn AS diagnostico_rn, "+ 
		"CASE WHEN ih.diagnostico_muerte is NULL THEN '' ELSE ih.diagnostico_muerte END AS causa_muerte, "+ 
		"CASE WHEN ih.fecha_muerte IS NULL THEN '' ELSE to_char(ih.fecha_muerte,'DD/MM/YYYY') END AS fecha_muerte, "+ 
		"CASE WHEN ih.hora_muerte IS NULL THEN '' ELSE ih.hora_muerte || '' END AS hora_muerte, "+
		//Campos adicionales de validacion
		"CASE WHEN ih.consecutivo IS NULL THEN 'false' ELSE 'true' END AS tuvo_informacion, "+ 
		"administracion.getnombremedico(f.cod_paciente) AS paciente, "+ 
		"getnombreviaingreso(f.via_ingreso) AS via_ingreso "+ 
		"FROM facturas f ";
	
	/**
	 * Sección SELECT para la consulta RIPS del archivo AN Subcontratadas
	 */
	private static final String consultaAN_Subcontratadas_SELECT_Str = "SELECT DISTINCT "+ 
		"f.consecutivo_factura, coalesce(ins.pref_factura, '')||f.consecutivo_factura AS consecutivo, "+ 
		"f.cuenta AS cuenta, " +
		ConstantesBD.codigoNuncaValido+" AS serv_art, "+ 
		ConstantesBD.codigoNuncaValido+" AS orden, "+ 
		"p.tipo_identificacion AS tipo_identificacion, "+ 
		"p.numero_identificacion AS numero_identificacion, "+ 
		"p1.tipo_identificacion AS tipo_id_madre, "+ 
		"p1.numero_identificacion AS numero_id_madre, "+ 
		"ip.semana_gestacional AS edad_gestacional, "+
		"CASE WHEN ip.control_prenatal = '"+ConstantesBD.acronimoSi+"' THEN 1 ELSE 2 END AS control_prenatal, "+ 
		"to_char(ih.fecha_nacimiento,'DD/MM/YYYY') AS fecha_nacimiento, "+ 
		"ih.hora_nacimiento AS hora_nacimiento, "+ 
		"CASE WHEN ih.sexo = 1 THEN 'M' ELSE 'F' END AS sexo, "+ 
		"ih.peso_egreso As peso, "+ 
		"ih.diagnostico_rn AS diagnostico_rn, "+ 
		"CASE WHEN ih.diagnostico_muerte is NULL THEN '' ELSE ih.diagnostico_muerte END AS causa_muerte, "+ 
		"CASE WHEN ih.fecha_muerte IS NULL THEN '' ELSE to_char(ih.fecha_muerte,'DD/MM/YYYY') END AS fecha_muerte, "+ 
		"CASE WHEN ih.hora_muerte IS NULL THEN '' ELSE ih.hora_muerte || '' END AS hora_muerte, "+
		//Campos adicionales de validacion
		"CASE WHEN ih.consecutivo IS NULL THEN 'false' ELSE 'true' END AS tuvo_informacion, "+ 
		"administracion.getnombremedico(f.cod_paciente) AS paciente, "+ 
		"getnombreviaingreso(f.via_ingreso) AS via_ingreso "+ 
		"FROM facturas f ";
	
	
	/**
	 * Seccion LEFT OUTER para la consulta del archivo AN
	 */
	private static final String consultaAN_LEFT_OUTER_Str = " "+
		"INNER JOIN det_factura_solicitud dfs ON(dfs.factura=f.codigo) "+ 
		"INNER JOIN solicitudes s ON(dfs.solicitud=s.numero_solicitud) "+ 
		"INNER JOIN sol_cirugia_por_servicio sc ON(sc.numero_solicitud=dfs.solicitud) "+ 
		"INNER JOIN servicios ss ON(ss.codigo = sc.servicio) "+
		"INNER JOIN instituciones ins ON (ins.codigo=f.institucion) "+
		"LEFT OUTER JOIN personas p ON(p.codigo=f.cod_paciente) "+ 
		"LEFT OUTER JOIN informacion_parto ip ON(ip.cirugia=sc.codigo OR ip.numero_solicitud = sc.numero_solicitud) "+ 
		"LEFT OUTER JOIN personas p1 ON(p1.codigo=ip.codigo_paciente) "+  
		"LEFT OUTER JOIN info_parto_hijos ih ON(ih.cirugia=sc.codigo OR ih.numero_solicitud = sc.numero_solicitud) " ;  
	
	/**
	 * Sección INNER JOIN para la consulta del archivo AN subcontratadas
	 */
	private static final String consultaAN_Subcontratadas_INNER_Str = ""+
		"INNER JOIN personas p ON(p.codigo=f.cod_paciente) "+ 
		"INNER JOIN cuentas c ON(c.id=f.cuenta) "+
		"INNER JOIN informacion_parto ip ON(ip.ingreso=c.id_ingreso) "+ 
		"INNER JOIN personas p1 ON(p1.codigo=ip.codigo_paciente) "+ 
		"INNER JOIN instituciones ins ON (ins.codigo=f.institucion) "+
		"INNER JOIN info_parto_hijos ih ON(ih.ingreso=c.id_ingreso) " ; 
		
	
	
	/**
	 * Sección WHERE para la consulta RIPS del archivo AN (Para factura)
	 */
	private static final String consultaAN_WHERE01_Str = " WHERE " +
		"f.via_ingreso IN("+ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoUrgencias+") AND " +
		"f.convenio=? AND "+ 
		"(to_char(f.fecha,'"+ConstantesBD.formatoFechaBD+"') BETWEEN ? AND ?) AND "+ 
		"f.institucion=? AND "+
		"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" AND "+
		"ss.tipo_servicio = '"+ConstantesBD.codigoServicioPartosCesarea+"' AND "+
		"p.sexo = "+ConstantesBD.codigoSexoFemenino+"  "+
		" ";
	
	
	/**
	 * Sección WHERE 02 para la consulta RIPS del archivo AN
	 */
	private static final String consultaAN_WHERE02_Str = " WHERE " +
		"f.via_ingreso IN("+ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoUrgencias+") AND " +
		"f.numero_cuenta_cobro=? AND "+ 
		"f.institucion=? AND "+
		"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" AND "+
		"ss.tipo_servicio = '"+ConstantesBD.codigoServicioPartosCesarea+"' AND "+
		"p.sexo = "+ConstantesBD.codigoSexoFemenino+"  "+
		" ";
	
	
	/**
	 * Sección WHERE para la consulta RIPS del archivo AN Subcontratada (Para factura)
	 */
	private static final String consultaAN_Subcontratada_WHERE01_Str = " WHERE " +
		"f.via_ingreso IN("+ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoUrgencias+") AND " +
		"f.convenio=? AND "+ 
		"(to_char(f.fecha,'"+ConstantesBD.formatoFechaBD+"') BETWEEN ? AND ?) AND "+ 
		"f.institucion=? AND "+
		"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" AND "+
		"f.pac_entidad_subcontratada IS NOT NULL AND "+
		"p.sexo = "+ConstantesBD.codigoSexoFemenino+"  "+
		" ";
	
	/**
	 * Sección WHERE 02 para la consulta RIPS del archivo AN Subcontratada (Para cuenta de cobro)
	 */
	private static final String consultaAN_Subcontratada_WHERE02_Str = " WHERE " +
		"f.via_ingreso IN("+ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoUrgencias+") AND " +
		"f.numero_cuenta_cobro=? AND "+ 
		"f.institucion=? AND "+
		"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" AND "+
		"f.pac_entidad_subcontratada IS NOT NULL AND "+
		"p.sexo = "+ConstantesBD.codigoSexoFemenino+"  "+
		" ";
		 
	
	//***************************************************************************************************************
	//*************QUERY'S RIPS CONSULTORIOS***********************************************************************
	//************************************************************************************************************************
	/**
	 * Cadena usada para cargar los registros iniciales del rips por Rangos
	 */
	private static final String consultaRegistrosPorRangosStr="(SELECT "+
		"CASE WHEN s.tipo="+ConstantesBD.codigoTipoSolicitudCita+"  " +
				"AND s.estado_historia_clinica="+ConstantesBD.codigoEstadoHCInterpretada+" " +
		"THEN v.fecha_valoracion ELSE c.fecha_apertura END AS fecha,"+
		"p.primer_apellido || ' ' || p.segundo_apellido || ' ' || p.primer_nombre || ' ' || p.segundo_nombre AS paciente,"+
		"p.tipo_identificacion || ' ' || p.numero_identificacion AS tipo_num_id,"+
		"getcodigopropservicio2(serv.codigo,"+ConstantesBD.codigoTarifarioCups+") AS servicio,"+
		"s.tipo AS tipo,"+
		"s.numero_solicitud AS solicitud,"+
		"c.id AS cuenta,"+
		"c.codigo_paciente AS codigo_paciente,"+
		"serv.tipo_servicio || '"+ConstantesBD.separadorSplit+"' || serv.naturaleza_servicio AS datos_servicio "+
		"FROM cuentas c "+ 
		"INNER JOIN personas p ON(p.codigo=c.codigo_paciente) "+ 
		"LEFT OUTER JOIN solicitudes s ON(s.cuenta=c.id) "+ 
		"LEFT OUTER JOIN servicios serv ON(serv.codigo=getserviciosolicitud(s.numero_solicitud,s.tipo)) "+
		"LEFT OUTER JOIN valoraciones v ON(s.numero_solicitud=v.numero_solicitud) "+
		"WHERE "+ 
		"c.convenio_por_defecto=? AND "+ 
		"(((c.fecha_apertura BETWEEN ? AND ?) AND c.id NOT IN (SELECT cuenta FROM solicitudes) AND c.id NOT IN (SELECT cuenta FROM rips_consultorios) ) "+ 
		"OR ((c.fecha_apertura BETWEEN ? AND ?) AND s.tipo!="+ConstantesBD.codigoTipoSolicitudCita+" ) "+
		"OR (s.tipo="+ConstantesBD.codigoTipoSolicitudCita+" AND " +
		"s.estado_historia_clinica="+ConstantesBD.codigoEstadoHCInterpretada+" AND " +
		"v.fecha_valoracion BETWEEN ? AND ?)) AND "+
		"(s.numero_solicitud NOT IN(SELECT numero_solicitud FROM rips_consultorios WHERE rips=? AND numero_solicitud IS NOT NULL) " +
		"OR s.numero_solicitud IS NULL) AND "+ 
		"c.id NOT IN(SELECT cuenta FROM rips_consultorios WHERE rips=? AND numero_solicitud IS NULL)) " +
		"UNION " +
		"(SELECT "+
		"rc.fecha_atencion AS fecha,"+
		"p.primer_apellido || ' ' || p.segundo_apellido || ' ' || p.primer_nombre || ' ' || p.segundo_nombre AS paciente,"+
		"p.tipo_identificacion || ' ' || p.numero_identificacion AS tipo_num_id,"+
		"getcodigopropservicio2(rc.servicio,"+ConstantesBD.codigoTarifarioCups+") AS servicio,"+
		"rc.codigo AS tipo,"+
		"'-1' AS solicitud,"+
		"rc.cuenta AS cuenta,"+
		"rc.paciente As codigo_paciente,"+
		"s.tipo_servicio || '"+ConstantesBD.separadorSplit+"' || s.naturaleza_servicio AS datos_servicio "+ 
		"FROM "+ 
		"rips_consultorios rc, personas p, servicios s "+
		"WHERE "+ 
		"p.codigo=rc.paciente AND s.codigo=rc.servicio AND rc.convenio=? AND " +
		"(rc.fecha_atencion BETWEEN ? AND ?) AND rc.numero_solicitud IS NULL AND " +
		"rc.codigo NOT IN (select codigo from rips_consultorios where rips=?))";
	
	/**
	 * Cadena usada para cargar los registros iniciales del rips Rangos Paciente
	 */
	private static final String consultaRegistrosRangosPacienteStr="(SELECT "+
		"CASE WHEN s.tipo="+ConstantesBD.codigoTipoSolicitudCita+" " +
				"AND s.estado_historia_clinica="+ConstantesBD.codigoEstadoHCInterpretada+" " +
		"THEN v.fecha_valoracion ELSE c.fecha_apertura END AS fecha,"+
		"p.primer_apellido || ' ' || p.segundo_apellido || ' ' || p.primer_nombre || ' ' || p.segundo_nombre AS paciente,"+
		"p.tipo_identificacion || ' ' || p.numero_identificacion AS tipo_num_id,"+
		"getcodigopropservicio2(serv.codigo,"+ConstantesBD.codigoTarifarioCups+") AS servicio,"+
		"s.tipo AS tipo,"+
		"s.numero_solicitud AS solicitud,"+
		"c.id AS cuenta,"+
		"c.codigo_paciente AS codigo_paciente,"+
		"serv.tipo_servicio || '"+ConstantesBD.separadorSplit+"' || serv.naturaleza_servicio AS datos_servicio "+
		"FROM cuentas c "+ 
		"INNER JOIN personas p ON(p.codigo=c.codigo_paciente) "+ 
		"LEFT OUTER JOIN solicitudes s ON(s.cuenta=c.id) "+ 
		"LEFT OUTER JOIN servicios serv ON(serv.codigo=getserviciosolicitud(s.numero_solicitud,s.tipo)) "+
		"LEFT OUTER JOIN valoraciones v ON(s.numero_solicitud=v.numero_solicitud) "+
		"WHERE "+ 
		"c.id=? AND "+ 
		"((c.id NOT IN (SELECT cuenta FROM solicitudes) AND c.id NOT IN (SELECT cuenta FROM rips_consultorios)) "+ 
		"OR s.tipo!="+ConstantesBD.codigoTipoSolicitudCita+"  "+
		"OR (s.tipo="+ConstantesBD.codigoTipoSolicitudCita+"  AND " +
		"s.estado_historia_clinica="+ConstantesBD.codigoEstadoHCInterpretada+")) AND "+
		"(s.numero_solicitud NOT IN(SELECT numero_solicitud FROM rips_consultorios WHERE rips=? AND numero_solicitud IS NOT NULL) " +
		"OR s.numero_solicitud IS NULL) AND "+ 
		"c.id NOT IN(SELECT cuenta FROM rips_consultorios WHERE rips=? AND numero_solicitud IS NULL)) " +
		"UNION " +
		"(SELECT "+
		"rc.fecha_atencion AS fecha,"+
		"p.primer_apellido || ' ' || p.segundo_apellido || ' ' || p.primer_nombre || ' ' || p.segundo_nombre AS paciente,"+
		"p.tipo_identificacion || ' ' || p.numero_identificacion AS tipo_num_id,"+
		"getcodigopropservicio2(rc.servicio,"+ConstantesBD.codigoTarifarioCups+") AS servicio,"+
		"rc.codigo AS tipo,"+
		"'-1' AS solicitud,"+
		"rc.cuenta AS cuenta,"+
		"rc.paciente As codigo_paciente,"+
		"s.tipo_servicio || '"+ConstantesBD.separadorSplit+"' || s.naturaleza_servicio AS datos_servicio "+ 
		"FROM "+ 
		"rips_consultorios rc, personas p, servicios s "+
		"WHERE "+ 
		"p.codigo=rc.paciente AND s.codigo=rc.servicio AND rc.cuenta=? AND " +
		"rc.numero_solicitud IS NULL AND " +
		"rc.codigo NOT IN (select codigo from rips_consultorios where rips=?))";
	
	/**
	 * Cadena usad apara consultar los datos RIPS de un registro de cita con servicio asociado
	 */
	private static final String cargarDatosRipsCitaStr="SELECT "+ 
		"getcodigopropservicio2(serv.codigo,"+ConstantesBD.codigoTarifarioCups+") || '"+ConstantesBD.separadorSplit+"' || " +
		"getnombreservicio(serv.codigo,"+ConstantesBD.codigoTarifarioCups+") || '"+ConstantesBD.separadorSplit+"' || " +
		"serv.codigo AS servicio,"+
		
		"getAcronimoConsulta(s.numero_solicitud,s.tipo,"+ValoresPorDefecto.getValorTrueParaConsultas()+",0) || '" +	ConstantesBD.separadorSplit+"' || " +
		"getTipoCieConsulta(s.numero_solicitud,s.tipo,"+ValoresPorDefecto.getValorTrueParaConsultas()+","+ValoresPorDefecto.getValorTrueParaConsultas()+",0) || '"+ConstantesBD.separadorSplit+"' || "+
		"getnombrediagnostico(getAcronimoConsulta(s.numero_solicitud,s.tipo,"+ValoresPorDefecto.getValorTrueParaConsultas()+",0),getTipoCieConsulta(s.numero_solicitud,s.tipo,"+ValoresPorDefecto.getValorTrueParaConsultas()+","+ValoresPorDefecto.getValorTrueParaConsultas()+",0)) AS diag_ppal,"+
		
		"getAcronimoConsulta(s.numero_solicitud,s.tipo,"+ValoresPorDefecto.getValorFalseParaConsultas()+",1) || '" +ConstantesBD.separadorSplit+"' || "+
		"getTipoCieConsulta(s.numero_solicitud,s.tipo,"+ValoresPorDefecto.getValorFalseParaConsultas()+","+ValoresPorDefecto.getValorTrueParaConsultas()+",1) || '"+ConstantesBD.separadorSplit+"' || "+
		"getnombrediagnostico(getAcronimoConsulta(s.numero_solicitud,s.tipo,"+ValoresPorDefecto.getValorFalseParaConsultas()+",1),getTipoCieConsulta(s.numero_solicitud,s.tipo,"+ValoresPorDefecto.getValorFalseParaConsultas()+","+ValoresPorDefecto.getValorTrueParaConsultas()+",1)) AS diag_rel1,"+
		
		"getAcronimoConsulta(s.numero_solicitud,s.tipo,"+ValoresPorDefecto.getValorFalseParaConsultas()+",2) || '" +ConstantesBD.separadorSplit+"' || " +
		"getTipoCieConsulta(s.numero_solicitud,s.tipo,"+ValoresPorDefecto.getValorFalseParaConsultas()+","+ValoresPorDefecto.getValorTrueParaConsultas()+",2) || '"+ConstantesBD.separadorSplit+"' || "+
		"getnombrediagnostico(getAcronimoConsulta(s.numero_solicitud,s.tipo,"+ValoresPorDefecto.getValorFalseParaConsultas()+",2),getTipoCieConsulta(s.numero_solicitud,s.tipo,"+ValoresPorDefecto.getValorFalseParaConsultas()+","+ValoresPorDefecto.getValorTrueParaConsultas()+",2)) AS diag_rel2,"+
		
		"getAcronimoConsulta(s.numero_solicitud,s.tipo,"+ValoresPorDefecto.getValorFalseParaConsultas()+",3) || '" +ConstantesBD.separadorSplit+"' || "+
		"getTipoCieConsulta(s.numero_solicitud,s.tipo,"+ValoresPorDefecto.getValorFalseParaConsultas()+","+ValoresPorDefecto.getValorTrueParaConsultas()+",3) || '"+ConstantesBD.separadorSplit+"' || "+
		"getnombrediagnostico(getAcronimoConsulta(s.numero_solicitud,s.tipo,"+ValoresPorDefecto.getValorFalseParaConsultas()+",3,getTipoCieConsulta(s.numero_solicitud,s.tipo,"+ValoresPorDefecto.getValorFalseParaConsultas()+","+ValoresPorDefecto.getValorTrueParaConsultas()+",3)) AS diag_rel3,"+
		
		"getTipoDiagPrincipal(s.numero_solicitud,s.tipo) || '" +
		ConstantesBD.separadorSplit+"' || getnombretipodiag(getTipoDiagPrincipal(s.numero_solicitud,s.tipo)) AS tipo_diag,"+
		
		"getCausaExterna(s.numero_solicitud,s.tipo) || '" +
		ConstantesBD.separadorSplit+"' || getnombrecausaexterna(getCausaExterna(s.numero_solicitud,s.tipo)) AS causa_externa,"+
		
		"getFinalidadConsulta(s.numero_solicitud,s.tipo) || '" +
		ConstantesBD.separadorSplit+"' || getnomfinalidadconsulta(getFinalidadConsulta(s.numero_solicitud,s.tipo)) AS finalidad,"+
		
		"s.numero_autorizacion AS autorizacion,"+
		//"getvalortotalcargo(s.numero_solicitud,serv.codigo) AS valor_total,"+
		"getvalorcopago(s.numero_solicitud,getvalortotalcargo(s.numero_solicitud,serv.codigo)) AS valor_copago," +
		"s.numero_solicitud AS solicitud," +
		"s.cuenta AS cuenta," +
		"c.codigo_paciente AS paciente "+ 
		"FROM solicitudes s, servicios serv, cuentas c "+ 
		"WHERE "+ 
		"serv.codigo=getserviciosolicitud(s.numero_solicitud,s.tipo) AND "+ 
		"c.id=s.cuenta AND "+
		"s.numero_solicitud=?";
	
	/**
	 * Cadena usada para consultar datos rips de registros que no son citas pero tiene un servicio asociado
	 */
	private static final String cargarDatosRipsNoCitaStr="SELECT "+ 
		"getcodigopropservicio2(serv.codigo,"+ConstantesBD.codigoTarifarioCups+") || '"+ConstantesBD.separadorSplit+"' || " +
		"getnombreservicio(serv.codigo,"+ConstantesBD.codigoTarifarioCups+") || '"+ConstantesBD.separadorSplit+"' || " +
		"serv.codigo AS servicio,"+
		//"getvalortotalcargo(s.numero_solicitud,serv.codigo) AS valor_total,"+
		"s.numero_autorizacion AS autorizacion,"+
		"s.numero_solicitud AS solicitud,"+
		"s.cuenta AS cuenta,"+
		"c.codigo_paciente AS paciente, "+
		"serv.tipo_servicio AS tipo_servicio,"+
		"serv.naturaleza_servicio AS naturaleza "+
		"FROM solicitudes s " +
		"INNER JOIN cuentas c ON(c.id=s.cuenta) " +
		"INNER JOIN servicios serv ON(serv.codigo=getserviciosolicitud(s.numero_solicitud,s.tipo))"+  
		"WHERE "+ 
		"s.numero_solicitud=?";
	
	/**
	 * cadena para consultar servicios por el codigo CUPS
	 */
	private static final String consultarServiciosStr="SELECT "+ 
		"getcodigopropservicio2(codigo,"+ConstantesBD.codigoTarifarioCups+") || '"+ConstantesBD.separadorSplit+"' || " +
		"getnombreservicio(codigo,"+ConstantesBD.codigoTarifarioCups+") || '"+ConstantesBD.separadorSplit+"' || " +
		"codigo || '"+ConstantesBD.separadorSplit+"' || " +
		"tipo_servicio  || '"+ConstantesBD.separadorSplit+"' || " +
		"naturaleza_servicio AS servicio "+ 
		"FROM servicios "+ 
		"WHERE "+ 
		"((tipo_servicio='"+ConstantesBD.codigoServicioInterconsulta+"' AND (naturaleza_servicio='01' OR naturaleza_servicio='05')) OR " +
		" (tipo_servicio='"+ConstantesBD.codigoServicioProcedimiento+"' AND (naturaleza_servicio='02' OR naturaleza_servicio='03')) OR " +
		" (tipo_servicio='"+ConstantesBD.codigoServicioNoCruentos+"' AND (naturaleza_servicio='02' OR naturaleza_servicio='03' OR naturaleza_servicio='04')) OR " +
		" (tipo_servicio='"+ConstantesBD.codigoServicioQuirurgico+"' AND naturaleza_servicio='04') OR " +
		" (tipo_servicio='"+ConstantesBD.codigoServicioPartosCesarea+"' AND (naturaleza_servicio='03' OR naturaleza_servicio='04')) OR " +
		" (tipo_servicio='"+ConstantesBD.codigoServicioPaquetes+"' AND (naturaleza_servicio='03' OR naturaleza_servicio='04'))) " +
		" AND "+ 
		"getcodigopropservicio2(codigo,"+ConstantesBD.codigoTarifarioCups+")=?";
	
	/**
	 * Cadena para consultar la tarifa del servicio y calcular el valor del copago
	 */
	private static final String consultarValoresServicioStr="SELECT "+
		//no se encuantra en el documento, verificado el 2009-03-10, entonces no me puedo inventar el valor,
		//por otro lado la función que calculaba el valor total tenia 3 errores.
		"? AS valor_total, "+
		"CASE WHEN mc.tipo_monto="+ConstantesBD.codigoTipoMontoCuotaModeradora+" THEN mc.valor ELSE ? * (mc.porcentaje/100) END AS valor_copago "+ 
		"FROM cuentas c, montos_cobro mc WHERE c.monto_cobro=mc.codigo AND c.id=?";
	
	/**
	 * Cadena para consultar un registro RIPS por su consecutivo
	 */
	private static final String consultarRegistroRipsStr="SELECT "+
		"convenio AS convenio,fecha_inicial AS fecha_inicial,"+
		"fecha_final AS fecha_final,servicio AS servicio,"+
		"tipo_diagnostico AS tipo_diag,diagnostico_principal AS diag_ppal,"+
		"diagnostico_rel1 AS diag_rel1,diagnostico_rel2 AS diag_rel2,"+
		"diagnostico_rel3 AS diag_rel3,causa_externa AS causa_externa,"+
		"finalidad_consulta AS finalidad,valor_total AS valor_total,"+
		"valor_copago AS valor_copago,valor_empresa AS valor_empresa,"+
		"autorizacion AS autorizacion,ambito_realizacion AS ambito,"+
		"personal_atiende AS personal,forma_realizacion AS forma,"+
		"paciente AS paciente,cuenta AS cuenta,numero_solicitud AS solicitud "+ 
		"FROM rips_consultorios WHERE codigo=?";
	
	/**
	 * Cadena para consultar los registros AF de RIPS cosnultorios
	 */
	private static final String consultaConsultoriosAFStr="SELECT DISTINCT "+
		"rc.numero_factura AS consecutivo,"+
		"rc.fecha_factura AS fecha,"+
		"rc.fecha_inicial AS fecha_inicial,"+
		"rc.fecha_final AS fecha_final,"+
		"CASE WHEN " +
			"rc.fecha_factura BETWEEN con.fecha_inicial AND con.fecha_final " +
		"THEN " +
			"con.numero_contrato ELSE '' END AS contrato,"+
		"'false' AS indicativo_transito,"+
		"'          ' AS numero_poliza,"+
		"getsumacopagosrips(rc.numero_factura) AS valor_bruto,"+
		"getsumatotalesrips(rc.numero_factura,'"+ConstantesBD.codigoServicioInterconsulta+"') AS valor_convenio "+ 
		"FROM rips_consultorios rc "+ 
		"LEFT OUTER JOIN contratos con ON(con.convenio=rc.convenio AND " +
			"(rc.fecha_factura BETWEEN con.fecha_inicial AND con.fecha_final)) "+ 
		"INNER JOIN cuentas c ON(c.id=rc.cuenta) "+ 
		"WHERE "+ 
		"rc.numero_factura=? AND rc.numero_remision=? "+ValoresPorDefecto.getValorLimit1()+" 1"; 
		
	

	/**
	 * Cadena para consultar los registros AD de RIPS cosnultorios
	 */
	private static final String consultaConsultoriosADStr="SELECT "+
		"rc.numero_factura AS consecutivo,"+
		"ser.naturaleza_servicio AS naturaleza_servicio,"+
		"getNumServNaturalezaRips(rc.numero_factura,ser.naturaleza_servicio) AS cantidad,"+
		"getValorServicioRips(rc.numero_factura,ser.naturaleza_servicio) AS total "+ 
		"FROM rips_consultorios rc " +
		"INNER JOIN servicios ser ON(ser.codigo=rc.servicio) "+
		"WHERE rc.numero_factura=? AND rc.numero_remision=? "+ 
		"GROUP BY ser.naturaleza_servicio,rc.numero_factura";
	
	/**
	 * Cadena para consultar los registros US de RIPS cosnultorios
	 */
	private static final String consultaConsultoriosUSStr="SELECT DISTINCT "+
		"p.tipo_identificacion AS tipo_identificacion,"+
		"p.numero_identificacion AS numero_identificacion,"+
		"p.primer_apellido AS primer_apellido,"+
		"p.segundo_apellido AS segundo_apellido,"+
		"p.primer_nombre AS primer_nombre,"+
		"p.segundo_nombre AS segundo_nombre,"+
		"c.fecha_apertura AS fecha_apertura,"+
		"p.fecha_nacimiento AS fecha_nacimiento,"+
		"p.sexo AS sexo,"+
		"p.codigo_departamento_vivienda AS depto_vivienda,"+
		"p.codigo_ciudad_vivienda AS ciudad_vivienda,"+
		"pac.zona_domicilio AS zona_domicilio,"+
		"rc.numero_factura AS consecutivo,"+
		"rc.cuenta AS cuenta,"+
		"administracion.getnombremedico(rc.paciente) AS paciente,"+
		"getnombreviaingreso(mc.via_ingreso) AS via_ingreso,"+
		"rc.numero_solicitud AS orden, "+
		"c.desplazado AS desplazado "+
		"FROM rips_consultorios rc "+ 
		"INNER JOIN cuentas c ON(rc.cuenta=c.id) "+ 
		"INNER JOIN personas p ON(p.codigo=rc.paciente) "+ 
		"INNER JOIN pacientes pac ON(pac.codigo_paciente=p.codigo) "+ 
		"INNER JOIN montos_cobro mc ON(mc.codigo=c.monto_cobro) "+ 
		"WHERE "+ 
		"rc.numero_factura=? AND rc.numero_remision=? "+ 
		"ORDER BY p.tipo_identificacion,p.numero_identificacion";
	
	/**
	 * Cadena para consultar los registros AC de RIPS cosnultorios
	 */
	private static final String consultaConsultoriosACStr="SELECT "+
		"rc.numero_factura AS consecutivo,"+
		"p.tipo_identificacion AS tipo_identificacion,"+
		"p.numero_identificacion AS numero_identificacion,"+
		"rc.fecha_atencion AS fecha_consulta,"+
		"rc.autorizacion AS numero_autorizacion,"+
		"getcodigopropservicio2(rc.servicio,"+ConstantesBD.codigoTarifarioCups+") AS codigo_consulta,"+
		"rc.finalidad_consulta AS finalidad_consulta,"+
		"rc.causa_externa AS causa_externa,"+
		"rc.diagnostico_principal AS cod_diag_ppal,"+
		"rc.diagnostico_rel1 AS cod_diag_rel1,"+
		"rc.diagnostico_rel2 AS cod_diag_rel2,"+
		"rc.diagnostico_rel3 AS cod_diag_rel3,"+
		"rc.tipo_diagnostico AS tipo_diag_ppal,"+
		"rc.valor_total As valor,"+
		"rc.valor_copago AS valor_copago,"+
		"rc.valor_empresa AS valor_empresa,"+
		"serv.naturaleza_servicio AS naturaleza_servicio,"+
		"rc.cuenta AS cuenta,"+
		"administracion.getnombremedico(rc.paciente) AS paciente,"+
		"getnombreviaingreso(mc.via_ingreso) AS via_ingreso,"+
		"s.consecutivo_ordenes_medicas AS orden "+  
		"FROM rips_consultorios rc "+ 
		"INNER JOIN personas p ON(rc.paciente=p.codigo) "+ 
		"INNER JOIN servicios serv ON(serv.codigo=rc.servicio) "+ 
		"INNER JOIN cuentas c ON(c.id=rc.cuenta) "+  
		"INNER JOIN montos_cobro mc ON(mc.codigo=c.monto_cobro) "+ 
		"LEFT OUTER JOIN solicitudes s ON(s.numero_solicitud=rc.numero_solicitud) "+
		"WHERE "+ 
		"serv.tipo_servicio='"+ConstantesBD.codigoServicioInterconsulta+"' AND "+ 
		"rc.numero_factura=? AND rc.numero_remision=?";
	
	/**
	 * Cadena para consultar los registros AP de RIPS cosnultorios
	 */
	private static final String consultaConsultoriosAPStr="SELECT "+
		"rc.numero_factura AS consecutivo,"+
		"p.tipo_identificacion AS tipo_identificacion,"+
		"p.numero_identificacion AS numero_identificacion,"+
		"rc.fecha_atencion AS fecha_procedimiento,"+
		"rc.autorizacion AS numero_autorizacion,"+
		"getcodigopropservicio2(rc.servicio,"+ConstantesBD.codigoTarifarioCups+") AS codigo_procedimiento,"+
		"getCodigoParametroRips(rc.ambito_realizacion) AS ambito_realizacion,"+
		"serv.finalidad_servicio AS finalidad_procedimiento,"+
		"getCodigoParametroRips(rc.personal_atiende) AS personal_atiende,"+
		"rc.diagnostico_principal AS cod_diag_ppal,"+
		"rc.diagnostico_rel1 AS cod_diag_rel,"+
		"rc.diagnostico_rel2 AS cod_diag_com,"+
		"getCodigoParametroRips(rc.forma_realizacion) AS forma_realizacion,"+
		"rc.valor_total AS valor,"+
		"rc.cuenta AS cuenta,"+ 
		"administracion.getnombremedico(rc.paciente) AS paciente,"+
		"getnombreviaingreso(mc.via_ingreso) AS via_ingreso,"+ 
		"s.consecutivo_ordenes_medicas As orden,"+
		"serv.tipo_servicio AS tipo_servicio,"+
		"serv.naturaleza_servicio AS naturaleza_servicio "+ 
		"FROM rips_consultorios rc "+ 
		"INNER JOIN servicios serv ON(serv.codigo=rc.servicio) "+ 
		"INNER JOIN personas p ON(p.codigo=rc.paciente) "+ 
		"INNER JOIN cuentas c ON(c.id=rc.cuenta) "+ 
		"INNER JOIN montos_cobro mc ON(mc.codigo=c.monto_cobro) "+ 
		"LEFT OUTER JOIN solicitudes s ON(s.numero_solicitud=rc.numero_solicitud) "+
		"WHERE "+
		"(serv.tipo_servicio='"+ConstantesBD.codigoServicioProcedimiento+"'  OR "+
		"serv.tipo_servicio='"+ConstantesBD.codigoServicioNoCruentos+"' OR "+
		"serv.tipo_servicio='"+ConstantesBD.codigoServicioQuirurgico+"' OR "+
		"serv.tipo_servicio='"+ConstantesBD.codigoServicioPartosCesarea+"' OR "+
		"serv.tipo_servicio='"+ConstantesBD.codigoServicioPaquetes+"' ) "+ 
		"AND rc.numero_factura=? AND rc.numero_remision=?";
	
	/**
	 * Cadena para actualizar datos RIPS de AC y AP
	 */
	private final static String actualizarDatosRipsStr="UPDATE rips_consultorios SET valor_total=?, valor_copago=?, valor_empresa=?, autorizacion=? WHERE codigo=?";
	
	/**
	 * Cadena para cosnultar el consecutivo de un registro RIPS por numero de solicitud
	 */
	private final static String verificarExistenciaRegistroRipsStr="SELECT codigo As consecutivo FROM rips_consultorios WHERE numero_solicitud=?";
	
	/**
	 * Cadena usada para consultar los datos del servicio
	 */
	private final static String consultarServicioStr="SELECT "+
		"getcodigopropservicio2(codigo,"+ConstantesBD.codigoTarifarioCups+") AS servicio," +
		"tipo_servicio || '"+ConstantesBD.separadorSplit+"' || naturaleza_servicio AS datos_servicio "+
		"FROM servicios WHERE codigo=?";
	
	/**
	 * Cadena para insertar una excepcion de rips consultorios
	 */
	private final static String insertarExcepcionRipsConsultoriosStr="INSERT INTO " +
			"excepciones_rips_con (numero_solicitud,institucion,rips) VALUES (?,?,?)";
	
	/**
	 * Cadena para consultar el estado de la excepcion de rips consultorios
	 */
	private final static String consultarExcepcionRipsConsultoriosStr="SELECT " +
			"rips As rips FROM excepciones_rips_con WHERE numero_solicitud=? AND institucion=?";
	
	
	//************************************************************************************************************************
	//*****************************CONSULTAS RIPS CAPITACION***********************************************************************
	//************************************************************************************************************************
	/**
	 * Cadena que consulta los datos de la cuenta de cobro capitacion
	 */
	private static final String consultarDatosCxCCapitacionStr = "SELECT "+ 
		"convenio AS codigo_convenio, "+
		"getnombreconvenio(convenio) AS nombre_convenio, "+
		"to_char(fecha_elaboracion,'DD/MM/YYYY') as fecha_elaboracion, "+
		"CASE WHEN fecha_inicial IS NULL THEN '' ELSE to_char(fecha_inicial,'DD/MM/YYYY') END AS fecha_inicial, "+
		"CASE WHEN fecha_final IS NULL THEN '' ELSE to_char(fecha_final,'DD/MM/YYYY') END AS fecha_final," +
		"valor_inicial_cuenta AS valor_cuenta "+ 
		"FROM cuentas_cobro_capitacion "+ 
		"WHERE numero_cuenta_cobro = ? AND institucion = ? AND estado != "+ConstantesBD.codigoEstadoCarteraAnulado;
	
	/**
	 * Cadena que consulta los contratos de la cuenta de cobro
	 */
	private static final String consultarContratosCxCCapitacionStr = "SELECT " +
		"contrato AS codigo," +
		"getnumerocontrato(contrato) As numero " +
		"FROM contrato_cargue " +
		"WHERE cuenta_cobro = ? AND institucion = ?";
	
	/**
	 * Sección WHERE para la consulta de capitación
	 */
	private static final String consultaCapitacion_WHERE_Str = "WHERE "+
		"f.convenio=? AND " +
		"(to_char(f.fecha,'"+ConstantesBD.formatoFechaBD+"') BETWEEN ? AND ?) AND " +
		"f.institucion=? AND " +
		"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" AND " +
		"sc.contrato in(REPLACE_CONTRATOS) ";
	
	/**
	 * Sección ORDER para la consulta del archivo AC Rips Capitacion
	 */
	private static final String consultaCapitacion_ORDER_Str = "ORDER BY f.consecutivo_factura";
	
	/**
	 * Sección select para la consulta del archivo AD Rips
	 */
	private static final String consultaAD_Capitacion_SELECT_Str = "SELECT "+ 
		"f.consecutivo_factura, coalesce(ins.pref_factura, '')||f.consecutivo_factura AS consecutivo_factura, "+ 
		"f.cuenta AS cuenta, "+ 
		"p.numero_identificacion AS numero_identificacion, "+ 
		"p.tipo_identificacion As tipo_identificacion, "+ 
		"administracion.getnombremedico(f.cod_paciente) AS paciente, "+ 
		"getnombreviaingreso(f.via_ingreso) AS via_ingreso, "+ 
		"CASE WHEN dfs.tipo_cargo = "+ConstantesBD.codigoTipoCargoServicios+" THEN " +
			"s.naturaleza_servicio " +
		"ELSE " +
			"coalesce(na.codigo_rips,'') " +
		"END AS naturaleza_servicio, "+
		"dfs.cantidad_cargo AS cantidad_cargo, "+
		"dfs.valor_total AS valor_total, " +
		"CASE WHEN dfs.tipo_cargo = "+ConstantesBD.codigoTipoCargoServicios+" THEN " +
			"dfs.servicio " +
		"ELSE " +
			"dfs.articulo " +
		"END AS serv_art, " +
		"dfs.tipo_cargo AS tipo_cargo, "+
		"sol.consecutivo_ordenes_medicas AS orden, " +
		"coalesce(s.tipo_servicio,'') AS tipo_servicio, " +
		"coalesce(na.es_medicamento,'') AS es_medicamento, " +
		"coalesce(na.es_pos,'') As es_pos, " ;
		 
		
		
		 
	
	/**
	 * Sección FROM para la consulta del archivo AD Rips
	 */
	private static final String consultaAD_Capitacion_FROM_Str = "FROM facturas f " +
		"INNER JOIN sub_cuentas sc ON(sc.sub_cuenta=f.sub_cuenta) "+ 
		"INNER JOIN cuentas c ON(c.id=f.cuenta) "+
		"INNER JOIN personas p ON(f.cod_paciente=p.codigo) "+ 
		"INNER JOIN det_factura_solicitud dfs ON(dfs.factura=f.codigo) "+ 
		"INNER JOIN solicitudes  sol ON(sol.numero_solicitud=dfs.solicitud) "+
		"INNER JOIN instituciones ins ON (ins.codigo=f.institucion) "+
		"LEFT OUTER JOIN servicios s ON(dfs.servicio=s.codigo) "+ 
		"LEFT OUTER JOIN articulo a ON(dfs.articulo=a.codigo)  "+
		"LEFT OUTER JOIN naturaleza_articulo na ON(na.acronimo=a.naturaleza AND na.institucion = a.institucion) ";
	
	
	
	/**
	 * Sección ORDER BY para la consulta del archivo AD Rips
	 */
	private static final String consultaAD_Capitacion_ORDER_Str = "ORDER BY naturaleza_servicio";
	
	/**
	 * Seccion SELECT para la consulta del archivo US rips capitacion
	 */
	private static final String consultaUS_Capitacion_SELECT_Str = "SELECT " +
		"f.consecutivo_factura, coalesce(ins.pref_factura, '')||f.consecutivo_factura AS consecutivo_factura, "+
		"f.cuenta AS cuenta, "+
		"c.fecha_apertura AS fecha_apertura, "+
		"p.tipo_identificacion AS tipo_identificacion, "+
		"p.numero_identificacion AS numero_identificacion, "+
		"p.primer_apellido AS primer_apellido, "+
		"p.segundo_apellido AS segundo_apellido, "+
		"p.primer_nombre AS primer_nombre, "+
		"p.segundo_nombre AS segundo_nombre, "+
		"p.fecha_nacimiento AS fecha_nacimiento, "+
		"p.sexo AS sexo, "+
		"p.codigo_departamento_vivienda AS depto_vivienda, "+
		"p.codigo_ciudad_vivienda AS ciudad_vivienda, "+
		"pac.zona_domicilio AS zona_domicilio, "+
		"administracion.getnombremedico(f.cod_paciente) AS paciente, "+
		"getnombreviaingreso(f.via_ingreso) AS via_ingreso, "+
		"c.desplazado AS desplazado, ";
	
	/**
	 * Sección FROM para la consulta del archivo US rips capitacion
	 */
	private static final String consultaUS_Capitacion_FROM_Str = ""+
		"FROM facturas f "+
		"INNER JOIN sub_cuentas sc ON(sc.sub_cuenta=f.sub_cuenta) "+
		"INNER JOIN cuentas c ON(c.id=f.cuenta) "+ 
		"INNER JOIN personas p ON(p.codigo=f.cod_paciente) "+ 
		"INNER JOIN instituciones ins ON (ins.codigo=f.institucion) "+
		"INNER JOIN pacientes pac ON(p.codigo=pac.codigo_paciente) ";
	
	/**
	 * Sección ORDER BY para la consulta del archivo US Rips Capitacion
	 */
	private static final String consultaUS_Capitacion_ORDER_Str = " ORDER BY p.tipo_identificacion,p.numero_identificacion";
	
	/**
	 * Sección SELECT para la consulta del archivo AC Rips Capitacion
	 */
	private static final String consultaAC_Capitacion_SELECT_Str = "SELECT "+
		"f.consecutivo_factura, coalesce(ins.pref_factura, '')||f.consecutivo_factura AS consecutivo_factura, "+
		"f.cuenta AS cuenta, " +
		"dfs.servicio AS serv_art, "+ 
		"p.tipo_identificacion AS tipo_identificacion, "+ 
		"p.numero_identificacion AS numero_identificacion, "+ 
		"MANEJOPACIENTE.GETAUTORXSUBCUENTASOLSERV(f.sub_cuenta, '"+ConstantesIntegridadDominio.acronimoSolicitud+"', s.numero_solicitud, dfs.servicio) AS numero_autorizacion, "+
		"s.consecutivo_ordenes_medicas AS orden, "+ 
		"serv.naturaleza_servicio AS naturaleza_servicio, "+ 
		"administracion.getnombremedico(f.cod_paciente) AS paciente, "+ 
		"getnombreviaingreso(f.via_ingreso) AS via_ingreso, "+
		"coalesce(getCodigoPropServicio2(dfs.servicio,?),'') AS codigo_consulta, " +
		"getFinalidadConsulta(dfs.solicitud,s.tipo) AS finalidad_consulta, "+ 
		"getCausaExterna(dfs.solicitud,s.tipo) AS causa_externa, "+ 
		"getTipoDiagPrincipal(dfs.solicitud,s.tipo) AS tipo_diag_ppal, "+ 
		"getAcronimoConsulta(dfs.solicitud,s.tipo,"+ValoresPorDefecto.getValorTrueParaConsultas()+",0) AS cod_diag_ppal, "+ 
		"getAcronimoConsulta(dfs.solicitud,s.tipo,"+ValoresPorDefecto.getValorFalseParaConsultas()+",1) AS cod_diag_rel1, "+ 
		"getAcronimoConsulta(dfs.solicitud,s.tipo,"+ValoresPorDefecto.getValorFalseParaConsultas()+",2) AS cod_diag_rel2, "+ 
		"getAcronimoConsulta(dfs.solicitud,s.tipo,"+ValoresPorDefecto.getValorFalseParaConsultas()+",3) AS cod_diag_rel3, "+ 
		"getFechaConsulta(dfs.solicitud,s.tipo) AS fecha_consulta, "+ 
		"'0' AS valor, ";
	
	/**
	 * Sección FROM para la consulta del archivo AC Rips Capitacion 
	 */
	private static final String consultaAC_Capitacion_FROM_Str = "FROM facturas f " +
		"INNER JOIN sub_cuentas sc ON(sc.sub_cuenta=f.sub_cuenta) "+ 
		"INNER JOIN cuentas c ON(c.id=f.cuenta) "+ 
		"INNER JOIN det_factura_solicitud dfs ON(dfs.factura=f.codigo) "+ 
		"INNER JOIN personas p ON(f.cod_paciente=p.codigo) "+ 
		"INNER JOIN solicitudes s ON(dfs.solicitud=s.numero_solicitud) "+ 
		"INNER JOIN instituciones ins ON (ins.codigo=f.institucion) "+
		"INNER JOIN servicios serv ON(serv.codigo=dfs.servicio and serv.tipo_servicio='"+ConstantesBD.codigoServicioInterconsulta+"') ";
	
	
	
	/**
	 * Seccion SELECT para la consulta del archivo AP Rips Capitacion
	 */
	private static final String consultaAP_Capitacion_SELECT_Str = "SELECT "+
		"f.consecutivo_factura, coalesce(ins.pref_factura, '')||f.consecutivo_factura AS consecutivo_factura, "+
		"f.cuenta AS cuenta, " +
		"dfs.servicio AS serv_art, "+ 
		"p.tipo_identificacion AS tipo_identificacion, "+ 
		"p.numero_identificacion AS numero_identificacion, "+ 
		"s.naturaleza_servicio AS naturaleza_servicio, "+ 
		"s.tipo_servicio AS tipo_servicio, "+ 
		"sol.consecutivo_ordenes_medicas AS orden, "+ 
		"getFechaProcedimiento(dfs.solicitud,s.tipo_servicio,dfs.tipo_solicitud) AS fecha_procedimiento, "+ 
		
		//"getAutorizacionRips2(dfs.solicitud,s.tipo_servicio,dfs.servicio,dfs.tipo_solicitud) AS numero_autorizacion, "+ 
		"MANEJOPACIENTE.GETAUTORXSUBCUENTASOLSERV(f.sub_cuenta, '"+ConstantesIntegridadDominio.acronimoSolicitud+"', sol.numero_solicitud, dfs.servicio) AS numero_autorizacion, "+
		
		"getCodigoPropServicio2(dfs.servicio,?)  AS codigo_procedimiento, "+ 
		"getFinalidadServicio(s.tipo_servicio,dfs.servicio,dfs.solicitud,dfs.tipo_solicitud) AS finalidad_procedimiento, "+ 
		//campo de personal atiende (Sólo para partos y cesáreas)
		"CASE WHEN s.tipo_servicio = '"+ConstantesBD.codigoServicioPartosCesarea+"' THEN  getPersonalAtiende(dfs.solicitud,dfs.servicio,f.institucion,dfs.tipo_solicitud) ELSE 0 END AS personal_atiende, "+ 
		"getAcronimoProcedimiento(dfs.solicitud,dfs.servicio,"+ValoresPorDefecto.getValorTrueParaConsultas()+","+ValoresPorDefecto.getValorFalseParaConsultas()+",s.tipo_servicio,dfs.tipo_solicitud) AS cod_diag_ppal, "+ 
		"getAcronimoProcedimiento(dfs.solicitud,dfs.servicio,"+ValoresPorDefecto.getValorFalseParaConsultas()+","+ValoresPorDefecto.getValorFalseParaConsultas()+",s.tipo_servicio,dfs.tipo_solicitud) AS cod_diag_rel, "+ 
		"getAcronimoProcedimiento(dfs.solicitud,dfs.servicio,"+ValoresPorDefecto.getValorFalseParaConsultas()+","+ValoresPorDefecto.getValorTrueParaConsultas()+",s.tipo_servicio,dfs.tipo_solicitud) AS cod_diag_com, "+ 
		//campo de la forma de realizacion (Sólo para partos y cesareas y quirúgicos)
		"CASE WHEN s.tipo_servicio IN ('"+ConstantesBD.codigoServicioPartosCesarea+"','"+ConstantesBD.codigoServicioQuirurgico+"','"+ConstantesBD.codigoServicioNoCruentos+"') THEN getFormaRealizacion(dfs.solicitud,dfs.servicio,dfs.tipo_solicitud) ELSE 0 END AS forma_realizacion, "+ 
		"getAmbitoRealizacion(f.via_ingreso, getTipoPacSegCuenta(f.cuenta)) AS ambito_realizacion, "+ 
		"administracion.getnombremedico(f.cod_paciente) AS paciente, "+ 
		"getnombreviaingreso(f.via_ingreso) AS via_ingreso, "+
		"'0' As valor, ";
	
	/**
	 * Seccion FROM para la consulta del archivo AP Rips Capitacion
	 */
	private static final String consultaAP_Capitacion_FROM_Str = "FROM facturas f "+
		"INNER JOIN sub_cuentas sc ON(sc.sub_cuenta=f.sub_cuenta) "+
		"INNER JOIN cuentas c ON(c.id=f.cuenta) "+ 
		"INNER JOIN det_factura_solicitud dfs ON(dfs.factura=f.codigo) "+
		"INNER JOIN instituciones ins ON (ins.codigo=f.institucion) "+
		"INNER JOIN servicios s ON(s.codigo=dfs.servicio AND s.tipo_servicio IN ('"+ConstantesBD.codigoServicioProcedimiento+"','"+ConstantesBD.codigoServicioPartosCesarea+"','"+ConstantesBD.codigoServicioQuirurgico+"','"+ConstantesBD.codigoServicioNoCruentos+"','"+ConstantesBD.codigoServicioPaquetes+"')  ) "+ 
		"LEFT OUTER JOIN personas p ON (p.codigo=f.cod_paciente) "+ 
		"LEFT OUTER JOIN solicitudes sol ON(sol.numero_solicitud=dfs.solicitud) ";
	
	/**
	 * Sección SELECT para la consulta del archivo AM Rips Capitacion
	 */
	private static final String consultaAM_Capitacion_SELECT_Str = "SELECT "+
		"f.consecutivo_factura, coalesce(ins.pref_factura, '')||f.consecutivo_factura AS consecutivo_factura, "+
		"f.cuenta AS cuenta, "+ 
		"dfs.cantidad_cargo AS numero_unidades, " +
		"dfs.articulo AS serv_art, "+ 
		"coalesce(na.codigo_rips,'') AS naturaleza, "+ 
		"coalesce(na.es_pos,'') AS es_pos, "+
		//"CASE WHEN na.es_pos = '"+ConstantesBD.acronimoTrueCorto+"' THEN a.minsalud ELSE '' END AS codigo_medicamento, "+
		" case " +
			"when a.numero_expediente is not null and a.cons_present_comercial is not null " +
			"then a.numero_expediente ||'-'|| a.cons_present_comercial " +
			"else '' " +
		"end as codigo_medicamento, " +
		"CASE WHEN na.es_pos = '"+ConstantesBD.acronimoTrueCorto+"' THEN '1' ELSE '2' END AS tipo, " +
		"coalesce(a.descripcion_alterna, a.descripcion) AS nombre, "+ 
		"CASE WHEN na.es_pos = '"+ConstantesBD.acronimoFalseCorto+"' OR na.es_pos IS NULL THEN a.concentracion ELSE '' END AS concentracion, "+ 
		"CASE WHEN na.es_pos = '"+ConstantesBD.acronimoFalseCorto+"' OR na.es_pos IS NULL THEN getformafarmaceuticaarticulo(a.codigo) ELSE '' END AS forma_farmaceutica, "+ 
		"CASE WHEN na.es_pos = '"+ConstantesBD.acronimoFalseCorto+"' OR na.es_pos IS NULL THEN getunidadmedidaarticulo(a.codigo) ELSE '' END AS unidad_medida, "+ 
		"CASE WHEN p.primer_nombre IS NULL THEN '' ELSE p.primer_nombre END || "+ 
			"CASE WHEN p.segundo_nombre IS NULL THEN '' ELSE p.segundo_nombre END || "+ 
			"CASE WHEN p.primer_apellido IS NULL THEN '' ELSE p.primer_apellido END || "+ 
			"CASE WHEN p.segundo_apellido IS NULL THEN '' ELSE p.segundo_apellido END AS paciente, "+ 
		"p.tipo_identificacion AS tipo_identificacion, "+ 
		"p.numero_identificacion AS numero_identificacion, "+ 
		"s.consecutivo_ordenes_medicas AS orden, "+ 
		"getnombreviaingreso(f.via_ingreso) AS via_ingreso, "+ 
		//"getAutorizacionRips(dfs.codigo,dfs.tipo_cargo) AS numero_autorizacion, "+
		"MANEJOPACIENTE.GETAUTORXSUBCUENTASOLART(f.sub_cuenta, '"+ConstantesIntegridadDominio.acronimoSolicitud+"', s.numero_solicitud, dfs.articulo) AS numero_autorizacion, "+
		"'0' AS valor_unitario, "+ 
		"'0' AS valor_total, "; 
	
	/**
	 * Seccion FROM para la consulta del archivo AM Rips Capitacion
	 */
	private static final String consultaAM_Capitacion_FROM_Str = "FROM facturas f "+
		"INNER JOIN sub_cuentas sc ON(sc.sub_cuenta=f.sub_cuenta) "+
		"INNER JOIN cuentas c ON(c.id=f.cuenta) "+
		"INNER JOIN instituciones ins ON (ins.codigo=f.institucion) ";
	
	/**
	 * Sección SELECT para la consulta del archivo AT Articulo Rips Capitacion
	 */
	private static final String consultaAT_Articulo_Capitacion_SELECT_Str = "SELECT "+
		"coalesce(ins.pref_factura, '')||f.consecutivo_factura AS consecutivo_factura, "+
		"f.cuenta AS cuenta, "+ 
		"dfs.cantidad_cargo AS cantidad, "+ 
		"dfs.tipo_cargo AS tipo_cargo, " +
		"dfs.articulo AS serv_art, "+ 
		"p.tipo_identificacion AS tipo_identificacion, "+ 
		"p.numero_identificacion AS numero_identificacion, "+ 
		//"getAutorizacionRips(dfs.codigo,dfs.tipo_cargo) AS numero_autorizacion, "+ 
		"MANEJOPACIENTE.GETAUTORXSUBCUENTASOLART(f.sub_cuenta, '"+ConstantesIntegridadDominio.acronimoSolicitud+"', sol.numero_solicitud, dfs.articulo) AS numero_autorizacion, "+
		"'1' AS tipo_servicio, "+ // Para materuales e insumos
		"'' AS codigo_servicio, "+ 
		"a.descripcion AS nombre_servicio, "+ 
		"administracion.getnombremedico(f.cod_paciente) AS paciente, "+ 
		"getnombreviaingreso(f.via_ingreso) AS via_ingreso, "+ 
		"coalesce(na.codigo_rips,'') AS naturaleza, "+ 
		"sol.consecutivo_ordenes_medicas AS orden, "+ 
		"'0' AS valor_unitario, "+ 
		"'0' AS valor_total, "; 
	
	/**
	 * Seccion FROM para la consulta del archivo AT Articulo Rips Capitacion
	 */
	private static final String consultaAT_Articulo_Capitacion_FROM_Str = "FROM facturas f "+
		"INNER JOIN sub_cuentas sc ON(sc.sub_cuenta=f.sub_cuenta) "+
		"INNER JOIN cuentas c ON(c.id=f.cuenta) "+ 
		"INNER JOIN det_factura_solicitud dfs ON(dfs.factura=f.codigo) "+ 
		"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dfs.solicitud) "+ 
		"INNER JOIN articulo a ON(a.codigo=dfs.articulo) " +
		"INNER JOIN instituciones ins ON (ins.codigo=f.institucion) "+
		"INNER JOIN naturaleza_articulo na ON(na.acronimo = a.naturaleza AND na.institucion = a.institucion AND na.es_medicamento = '"+ConstantesBD.acronimoNo+"') "+ 
		"LEFT OUTER JOIN personas p ON(p.codigo=f.cod_paciente) ";
	
	/**
	 * Sección SELECT para la consulta del archivo AT Servicio Rips Capitacion
	 */
	private static final String consultaAT_Servicio_Capitacion_SELECT_Str = "SELECT "+ 
		"coalesce(ins.pref_factura, '')||f.consecutivo_factura AS consecutivo_factura, "+
		"f.cuenta AS cuenta, "+ 
		"dfs.cantidad_cargo AS cantidad, "+ 
		"dfs.tipo_cargo AS tipo_cargo, " +
		"dfs.servicio AS serv_art, "+ 
		"p.tipo_identificacion AS tipo_identificacion, "+ 
		"p.numero_identificacion AS numero_identificacion, "+ 
		//"getAutorizacionRips(dfs.codigo,dfs.tipo_cargo) AS numero_autorizacion, "+ 
		"MANEJOPACIENTE.GETAUTORXSUBCUENTASOLSERV(f.sub_cuenta, '"+ConstantesIntegridadDominio.acronimoSolicitud+"', sol.numero_solicitud, dfs.servicio) AS numero_autorizacion, "+ 
		"getTipoServRips(dfs.codigo,dfs.tipo_cargo) AS tipo_servicio, "+ 
		"CASE WHEN getCodigoPropServicio2(dfs.servicio,?) IS NULL OR getCodigoPropServicio2(dfs.servicio,?) = '' OR getnombreservicio(dfs.servicio,?) IS NULL OR  getnombreservicio(dfs.servicio,?) = '' THEN "+ 
			"getCodigoPropServicio2(dfs.servicio,"+ConstantesBD.codigoTarifarioCups+") "+ 
		"ELSE "+ 
			"getCodigoPropServicio2(dfs.servicio,?) ||'' "+ 
		"END AS codigo_servicio, "+ 
		"CASE WHEN getCodigoPropServicio2(dfs.servicio,?) IS NULL OR "+ 
			"getCodigoPropServicio2(dfs.servicio,?) = '' OR "+  
			"getnombreservicio(dfs.servicio,?) IS NULL OR "+  
			"getnombreservicio(dfs.servicio,?) = '' THEN "+ 
				"getnombreservicio(dfs.servicio,"+ConstantesBD.codigoTarifarioCups+") "+ 
		"ELSE "+ 
			"getnombreservicio(dfs.servicio,?) "+ 
		"END AS nombre_servicio, "+ 
		"administracion.getnombremedico(f.cod_paciente) AS paciente, "+ 
		"getnombreviaingreso(f.via_ingreso) AS via_ingreso, "+ 
		"s.naturaleza_servicio AS naturaleza, "+ 
		"sol.consecutivo_ordenes_medicas AS orden, "+ 
		"'0' AS valor_unitario, "+ 
		"'0' AS valor_total, ";
	
	/**
	 * Seccion FROM para la consulta del archivo AT Servicio Rips Capitacion
	 */
	private static final String consultaAT_Servicio_Capitacion_FROM_Str = "FROM facturas f "+
		"INNER JOIN sub_cuentas sc ON(sc.sub_cuenta=f.sub_cuenta) "+
		"INNER JOIN cuentas c ON(c.id=f.cuenta) "+ 
		"INNER JOIN det_factura_solicitud dfs ON(dfs.factura=f.codigo) "+ 
		"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dfs.solicitud) "+ 
		"INNER JOIN instituciones ins ON (ins.codigo=f.institucion) "+
		"INNER JOIN servicios s ON(s.codigo=dfs.servicio AND s.naturaleza_servicio IN('"+ConstantesBD.codigoNaturalezaServicioEstancias+"','"+ConstantesBD.codigoNaturalezaServicioMaterialesInsumos+"','"+ConstantesBD.codigoNaturalezaServicioHonorarios+"','"+ConstantesBD.codigoNaturalezaServicioTrasladoPaciente+"')) "+ 
		"LEFT OUTER JOIN personas p ON(p.codigo=f.cod_paciente) ";
	
	/**
	 * Seccion SELECT para la consulta del archivo AU Rips Capitacion
	 */
	private static final String consultaAU_Capitacion_SELECT_Str = "SELECT "+
		"f.consecutivo_factura AS consecFactura, coalesce(ins.pref_factura, '')||f.consecutivo_factura AS consecutivo_factura, "+
		"f.cuenta AS cuenta, "+ 
		"v.causa_externa AS causa_externa, "+ 
		"p.tipo_identificacion AS tipo_identificacion, "+ 
		"p.numero_identificacion As numero_identificacion, "+ 
		"to_char(au.fecha_ingreso_observacion,'DD/MM/YYYY') AS fecha_ingreso, "+ 
		"au.hora_ingreso_observacion AS hora_ingreso, "+ 
		"vd.numero_verificacion AS numero_autorizacion, "+ 
		"to_char(au.fecha_egreso_observacion,'DD/MM/YYYY') AS fecha_egreso, "+ 
		"au.hora_egreso_observacion AS hora_egreso, "+ 
		"e.diagnostico_principal As diag_egreso, "+ 
		"CASE WHEN e.diagnostico_relacionado1 = '1' THEN '' ELSE e.diagnostico_relacionado1 END AS diag_egreso_rel1, "+ 
		"CASE WHEN e.diagnostico_relacionado2 = '1' THEN '' ELSE e.diagnostico_relacionado2 END AS diag_egreso_rel2, "+ 
		"CASE WHEN e.diagnostico_relacionado3 = '1' THEN '' ELSE e.diagnostico_relacionado3 END AS diag_egreso_rel3, "+ 
		//cadena de CASE para la definición del destino de salida según regla RIPS
		"CASE WHEN e.destino_salida = "+ConstantesBD.codigoDestinoSalidaDadoDeAlta+" OR e.destino_salida = "+ConstantesBD.codigoDestinoSalidaOtro+" OR e.destino_salida = "+ConstantesBD.codigoDestinoSalidaVoluntaria+" THEN "+ 
			"1 "+//Alta de Urgencias
		"ELSE "+ 
			"CASE WHEN e.destino_salida = "+ConstantesBD.codigoDestinoSalidaRemitidoOtroNivelComplejidad+" THEN "+ 
				"2  "+ // Remisión a otor nivel de complejidad
			"ELSE "+ 
				"CASE WHEN e.destino_salida = "+ConstantesBD.codigoDestinoSalidaHospitalizacion+" THEN "+ 
					"3  "+ //Hospitalización
				"ELSE "+ 	
					"-1 "+ // Código Inválido
				"END "+ 
			"END "+ 
		"END AS destino_salida, "+ 
		//----------validaciones previas para el estado de salida---------------------------------------------------------------
		"CASE WHEN e.estado_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN "+ 
			"'2' "+ 
		"ELSE "+ 
			"CASE WHEN e.estado_salida = "+ValoresPorDefecto.getValorFalseParaConsultas()+" THEN "+ 
				"'1' "+ 
			"ELSE "+ 	
				"'' "+ 
			"END "+ 
		"END AS estado_salida, "+ 
		//---------validaciones previas para el diagnostico de muerte-----------------------------------------------------------
		"CASE WHEN e.estado_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN "+ 
			"CASE WHEN e.diagnostico_muerte = '1' THEN "+ 
				"'' "+ 
			"ELSE "+ 
				"e.diagnostico_muerte "+ 
			"END "+ 
		"ELSE "+ 
			"'' "+ 
		"END AS diag_muerte, "+ 
		"administracion.getnombremedico(f.cod_paciente) AS paciente, "+ 
		"getnombreviaingreso(f.via_ingreso) AS via_ingreso,";
	
	/**
	 * Seccion SELECT para la consulta de las admisiones de urgencias subcontratadas en capitacion
	 */
	private static final String consultaAU_SubContratadas_Capitacion_SELECT_Str = "SELECT "+
		"f.consecutivo_factura, coalesce(ins.pref_factura, '')||f.consecutivo_factura AS consecutivo_factura, "+
		"f.cuenta AS cuenta, "+ 
		"au.causa_externa AS causa_externa, "+ 
		"p.tipo_identificacion AS tipo_identificacion, "+ 
		"p.numero_identificacion As numero_identificacion, "+ 
		"to_char(au.fecha_ingreso_observacion,'DD/MM/YYYY') AS fecha_ingreso, "+ 
		"au.hora_ingreso_observacion AS hora_ingreso, "+ 
		"vd.numero_verificacion AS numero_autorizacion, "+ 
		"to_char(au.fecha_egreso_observacion,'DD/MM/YYYY') AS fecha_egreso, "+ 
		"au.hora_egreso_observacion AS hora_egreso, "+ 
		"e.diagnostico_principal As diag_egreso, "+ 
		"CASE WHEN e.diagnostico_relacionado1 = '1' THEN '' ELSE e.diagnostico_relacionado1 END AS diag_egreso_rel1, "+ 
		"CASE WHEN e.diagnostico_relacionado2 = '1' THEN '' ELSE e.diagnostico_relacionado2 END AS diag_egreso_rel2, "+ 
		"CASE WHEN e.diagnostico_relacionado3 = '1' THEN '' ELSE e.diagnostico_relacionado3 END AS diag_egreso_rel3, "+
		//cadena de CASE para la definición del destino de salida según regla RIPS
		"CASE WHEN " +
			"e.destino_salida = "+ConstantesBD.codigoDestinoSalidaDadoDeAlta+" OR " +
			"e.destino_salida = "+ConstantesBD.codigoDestinoSalidaOtro+" OR " +
			"e.destino_salida = "+ConstantesBD.codigoDestinoSalidaVoluntaria+" THEN " +
				"1 " + //Alta de Urgencias
		"ELSE " +
			"CASE WHEN e.destino_salida = "+ConstantesBD.codigoDestinoSalidaRemitidoOtroNivelComplejidad+" THEN " +
				"2 " + // Remisión a otor nivel de complejidad
			"ELSE " +
				"CASE WHEN e.destino_salida = "+ConstantesBD.codigoDestinoSalidaHospitalizacion+" THEN " +
					"3 " + //Hospitalización
				"ELSE " +
					"-1 " + // Código Inválido
				"END " +
			"END " +
		"END AS destino_salida, "+
		//----------validaciones previas para el estado de salida---------------------------------------------------------------
		"CASE WHEN e.estado_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN " +
			"'2' " +
		"ELSE " +
			"CASE WHEN e.estado_salida = "+ValoresPorDefecto.getValorFalseParaConsultas()+" THEN " +
				"'1' " +
			"ELSE " +
				"'' " +
			"END " +
		"END AS estado_salida, "+
		//---------validaciones previas para el diagnostico de muerte-----------------------------------------------------------
		"CASE WHEN e.estado_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN " +
			"CASE WHEN e.diagnostico_muerte = '1' THEN " +
				"'' " +
			"ELSE " +
				"e.diagnostico_muerte " +
			"END " +
		"ELSE " +
			"'' " +
		"END AS diag_muerte, "+ 
		"administracion.getnombremedico(f.cod_paciente) AS paciente, "+ 
		"getnombreviaingreso(f.via_ingreso) AS via_ingreso, ";
	
	/**
	 * Sección FROM para la consulta del archivo AU Rips Capitacion
	 */
	private static final String consultaAU_Capitacion_FROM_Str = "FROM facturas f "+
		"INNER JOIN sub_cuentas sc ON(sc.sub_cuenta=f.sub_cuenta) "+
		"INNER JOIN cuentas c ON(c.id=f.cuenta) "+ 
		"INNER JOIN det_factura_solicitud dfs ON(dfs.factura=f.codigo) "+ 
		"INNER JOIN solicitudes s ON(s.numero_solicitud=dfs.solicitud) "+  
		"INNER JOIN valoraciones v ON(v.numero_solicitud=s.numero_solicitud) "+ 
		"INNER JOIN instituciones ins ON (ins.codigo=f.institucion) "+
		"INNER JOIN valoraciones_urgencias vu ON(vu.numero_solicitud=v.numero_solicitud AND vu.codigo_conducta_valoracion = "+ConstantesBD.codigoConductaSeguirCamaObservacion+" ) "+ 
		"LEFT OUTER JOIN personas p ON(p.codigo=f.cod_paciente) "+ 
		"LEFT OUTER JOIN admisiones_urgencias au ON(au.cuenta=f.cuenta) "+ 
		"LEFT OUTER JOIN egresos e ON(e.cuenta=f.cuenta) " +
		"left outer join verificaciones_derechos vd ON (vd.sub_cuenta= sc.sub_cuenta) ";
	
	/**
	 * Seccion FROM para la consulta del archivo AU Subcotnratadas Rips Capitacion
	 */
	private static final String consultaAU_Subcontratadas_Capitacion_FROM_Str = ""+
		"FROM facturas f "+ 
		"INNER JOIN sub_cuentas sc ON(sc.sub_cuenta=f.sub_cuenta) "+ 
		"INNER JOIN instituciones ins ON (ins.codigo=f.institucion) "+
		"LEFT OUTER JOIN personas p ON(p.codigo=f.cod_paciente) "+ 
		"LEFT OUTER JOIN admisiones_urgencias au ON(au.cuenta=f.cuenta) "+ 
		"LEFT OUTER JOIN egresos e ON(e.cuenta=f.cuenta)  " +
		"left outer join verificaciones_derechos vd ON (vd.sub_cuenta= sc.sub_cuenta) " ;
	
	/**
	 * Seccion WHERE para la consulta del archivo AU Rips Capitacion
	 */
	private static final String consultaAU_Capitacion_WHERE_Str = "WHERE "+ 
		"f.via_ingreso = "+ConstantesBD.codigoViaIngresoUrgencias+" AND "+ 
		"f.convenio=? AND "+ 
		"(to_char(f.fecha,'"+ConstantesBD.formatoFechaBD+"') BETWEEN ? AND ?) AND "+ 
		"f.institucion=? AND "+ 
		"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+ 
		" AND sc.contrato IN(REPLACE_CONTRATOS)  ";
	
	/**
	 * Sección SELEC para la consulta AH Rips Capitacion
	 */
	private static final String consultaAH_Capitacion_SELECT_Str = "SELECT DISTINCT "+
		"f.consecutivo_factura, coalesce(ins.pref_factura, '')||f.consecutivo_factura AS consecutivo_factura, "+
		"f.cuenta AS cuenta, "+ 
		"p.numero_identificacion As numero_identificacion, "+ 
		"p.tipo_identificacion AS tipo_identificacion, "+ 
		"ah.origen_admision_hospitalaria AS via, "+ 
		"to_char(ah.fecha_admision,'DD/MM/YYYY') AS fecha_ingreso, "+ 
		"ah.hora_admision AS hora_ingreso, "+ 
		"vd.numero_verificacion AS numero_autorizacion, "+ 
		"CASE WHEN f.pac_entidad_subcontratada IS NULL THEN v.causa_externa ELSE ah.causa_externa END AS causa_externa, "+ //varía por subcontratación 
		"CASE WHEN f.pac_entidad_subcontratada IS NULL THEN vh.diagnostico_ingreso ELSE ah.diagnostico_admision END AS diag_ingreso, "+ //varía por subcontratación 
		///diagnostico de egreso principal
		"CASE WHEN e.codigo_medico IS NOT NULL AND e.usuario_responsable IS NOT NULL THEN " +
			"e.diagnostico_principal " +
		"ELSE " +
			"coalesce(historiaclinica.getultimodiagvaloracion(v.numero_solicitud,"+ValoresPorDefecto.getValorTrueParaConsultas()+",0),'') " +
		"END As diag_egreso, "+
		//diagnostico de egreso relacionado N° 1
		"CASE WHEN e.codigo_medico IS NOT NULL AND e.usuario_responsable IS NOT NULL THEN " +
			"CASE WHEN e.diagnostico_relacionado1 = '1' THEN '' ELSE e.diagnostico_relacionado1 END " +
		"ELSE " +
			"coalesce(historiaclinica.getultimodiagvaloracion(v.numero_solicitud,"+ValoresPorDefecto.getValorFalseParaConsultas()+",1),'') " +
		"END AS diag_egreso_rel1, "+ 
		//diagnostico de egreso relacionado N° 2
		"CASE WHEN e.codigo_medico IS NOT NULL AND e.usuario_responsable IS NOT NULL THEN " +
			"CASE WHEN e.diagnostico_relacionado2 = '1' THEN '' ELSE e.diagnostico_relacionado2 END " +
		"ELSE " +
			"coalesce(historiaclinica.getultimodiagvaloracion(v.numero_solicitud,"+ValoresPorDefecto.getValorFalseParaConsultas()+",2),'') " +
		"END AS diag_egreso_rel2, "+ 
		//diagnostico de egreso relacionado N° 3
		"CASE WHEN e.codigo_medico IS NOT NULL AND e.usuario_responsable IS NOT NULL THEN " +
			"CASE WHEN e.diagnostico_relacionado3 = '1' THEN '' ELSE e.diagnostico_relacionado3 END " +
		"ELSE " +
			"coalesce(historiaclinica.getultimodiagvaloracion(v.numero_solicitud,"+ValoresPorDefecto.getValorFalseParaConsultas()+",3),'') " +
		"END AS diag_egreso_rel3, "+ 
		///-----validaciones previas para el estado de salida-----------------
		"CASE WHEN e.estado_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN "+ 
			"'2' "+ 
		"ELSE "+ 
			"CASE WHEN e.estado_salida = "+ValoresPorDefecto.getValorFalseParaConsultas()+" OR (e.codigo_medico IS NULL AND e.usuario_responsable IS NULL ) THEN "+ 
				"'1' "+ 
			"ELSE "+ 
				"'' "+ 
			"END "+ 
		"END AS estado_salida, "+ 
		//---- validaciones previas para el diagnostico de muerte-----------------------------
		"CASE WHEN e.estado_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN "+ 
			"CASE WHEN e.diagnostico_muerte = '1' THEN "+ 
				"'' "+ 
			"ELSE "+ 
				"e.diagnostico_muerte "+ 
			"END "+ 
		"ELSE "+ 
			"'' "+ 
		"END AS diag_muerte, "+ 
		"CASE WHEN e.codigo_medico IS NOT NULL AND e.usuario_responsable IS NOT NULL THEN to_char(e.fecha_egreso,'DD/MM/YYYY') ELSE to_char(f.fecha,'DD/MM/YYYY') END AS fecha_egreso, "+ 
		"CASE WHEN e.codigo_medico IS NOT NULL AND e.usuario_responsable IS NOT NULL THEN e.hora_egreso||'' ELSE f.hora||'' END AS hora_egreso, "+
		"CASE WHEN f.pac_entidad_subcontratada IS NULL THEN "+ //varía por subcontratacion
			"CASE WHEN ev.diagnostico_complicacion = '1' OR ev.diagnostico_complicacion IS NULL THEN '' ELSE ev.diagnostico_complicacion END " +
		"ELSE "+
			"coalesce(e.diagnostico_complicacion,'') "+
		"END AS diag_complicacion, "+  
		"administracion.getnombremedico(f.cod_paciente) AS paciente, "+ 
		"getnombreviaingreso(f.via_ingreso) AS via_ingreso,";
	
	/**
	 * Sección FROM para la consulta AH rips Capitacion
	 */
	private static final String consultaAH_Capitacion_FROM_Str = "FROM facturas f "+
		"INNER JOIN sub_cuentas sc ON(sc.sub_cuenta=f.sub_cuenta) "+
		"INNER JOIN cuentas c ON(c.id=f.cuenta) "+
		"INNER JOIN instituciones ins ON (ins.codigo=f.institucion) "+
		"LEFT OUTER JOIN personas p ON(p.codigo=f.cod_paciente) "+ 
		"LEFT OUTER JOIN admisiones_hospi ah ON(ah.cuenta=f.cuenta) "+ 
		"LEFT OUTER JOIN solicitudes s ON(f.cuenta=s.cuenta AND s.tipo IN ("+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+")) "+ 
		"LEFT OUTER JOIN valoraciones v ON(v.numero_solicitud=s.numero_solicitud) "+ 
		"LEFT OUTER JOIN val_hospitalizacion vh ON(vh.numero_solicitud=v.numero_solicitud) "+ 
		"LEFT OUTER JOIN egresos e ON(f.cuenta=e.cuenta) "+ 
		"LEFT OUTER JOIN evoluciones ev ON(ev.valoracion=v.numero_solicitud AND ev.orden_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+") "+
		"left outer join verificaciones_derechos vd ON (vd.sub_cuenta= sc.sub_cuenta) ";
	
	/**
	 * Sección WHERE para la consulta AH rips Capitacion
	 */
	private static final String consultaAH_Capitacion_WHERE_Str = "WHERE "+ 
		"f.via_ingreso = "+ConstantesBD.codigoViaIngresoHospitalizacion+" AND "+ 
		"f.convenio=? AND "+ 
		"(to_char(f.fecha,'"+ConstantesBD.formatoFechaBD+"') BETWEEN ? AND ?) AND "+ 
		"f.institucion=? AND "+ 
		"f.estado_facturacion = " + ConstantesBD.codigoEstadoFacturacionFacturada+" AND " +
		"c.tipo_paciente = '"+ConstantesBD.tipoPacienteHospitalizado+"' "+ 
		" AND sc.contrato IN(REPLACE_CONTRATOS)  ";
	
	/**
	 * Seccion SELECT para la consulta AN rips capitacion
	 */
	private static final String consultaAN_Capitacion_SELECT_Str = "SELECT DISTINCT "+
		"coalesce(ins.pref_factura, '')||f.consecutivo_factura AS consecutivo_factura, "+
		"f.cuenta AS cuenta, " +
		"dfs.servicio AS serv_art, "+ 
		"s.consecutivo_ordenes_medicas AS orden, "+ 
		"p.tipo_identificacion AS tipo_identificacion, "+ 
		"p.numero_identificacion AS numero_identificacion, "+ 
		"p1.tipo_identificacion AS tipo_id_madre, "+ 
		"p1.numero_identificacion AS numero_id_madre, "+ 
		"ip.semana_gestacional AS edad_gestacional, "+ 
		"CASE WHEN ip.control_prenatal = '"+ConstantesBD.acronimoSi+"' THEN 1 ELSE 2 END AS control_prenatal, "+ 
		"to_char(ih.fecha_nacimiento,'DD/MM/YYYY') AS fecha_nacimiento, "+ 
		"ih.hora_nacimiento AS hora_nacimiento, "+ 
		"CASE WHEN ih.sexo = 1 THEN 'M' ELSE 'F' END AS sexo, "+ 
		"ih.peso_egreso As peso, "+ 
		"ih.diagnostico_rn AS diagnostico_rn, "+ 
		"CASE WHEN ih.diagnostico_muerte is NULL THEN '' ELSE ih.diagnostico_muerte END AS causa_muerte, "+ 
		"CASE WHEN ih.fecha_muerte IS NULL THEN '' ELSE to_char(ih.fecha_muerte,'DD/MM/YYYY') END AS fecha_muerte, "+ 
		"CASE WHEN ih.hora_muerte IS NULL THEN '' ELSE ih.hora_muerte || '' END AS hora_muerte, "+ 
		//Campos adicionales de validacion
		"CASE WHEN ih.consecutivo IS NULL THEN 'false' ELSE 'true' END AS tuvo_informacion, "+
		"administracion.getnombremedico(f.cod_paciente) AS paciente, "+ 
		"getnombreviaingreso(f.via_ingreso) AS via_ingreso,";
	
	/**
	 * Seccion SELECT para la consulta AN rips capitacion subcontratadas
	 */
	private static final String consultaAN_Capitacion_Subcontratadas_SELECT_Str = "SELECT DISTINCT "+
		"coalesce(ins.pref_factura, '')||f.consecutivo_factura AS consecutivo_factura, "+
		"f.cuenta AS cuenta, " +
		ConstantesBD.codigoNuncaValido+" AS serv_art, "+ 
		ConstantesBD.codigoNuncaValido+" AS orden, "+ 
		"p.tipo_identificacion AS tipo_identificacion, "+ 
		"p.numero_identificacion AS numero_identificacion, "+ 
		"p1.tipo_identificacion AS tipo_id_madre, "+ 
		"p1.numero_identificacion AS numero_id_madre, "+ 
		"ip.semana_gestacional AS edad_gestacional, "+ 
		"CASE WHEN ip.control_prenatal = '"+ConstantesBD.acronimoSi+"' THEN 1 ELSE 2 END AS control_prenatal, "+ 
		"to_char(ih.fecha_nacimiento,'DD/MM/YYYY') AS fecha_nacimiento, "+ 
		"ih.hora_nacimiento AS hora_nacimiento, "+ 
		"CASE WHEN ih.sexo = 1 THEN 'M' ELSE 'F' END AS sexo, "+ 
		"ih.peso_egreso As peso, "+ 
		"ih.diagnostico_rn AS diagnostico_rn, "+ 
		"CASE WHEN ih.diagnostico_muerte is NULL THEN '' ELSE ih.diagnostico_muerte END AS causa_muerte, "+ 
		"CASE WHEN ih.fecha_muerte IS NULL THEN '' ELSE to_char(ih.fecha_muerte,'DD/MM/YYYY') END AS fecha_muerte, "+ 
		"CASE WHEN ih.hora_muerte IS NULL THEN '' ELSE ih.hora_muerte || '' END AS hora_muerte, "+ 
		//Campos adicionales de validacion
		"CASE WHEN ih.consecutivo IS NULL THEN 'false' ELSE 'true' END AS tuvo_informacion, "+
		"administracion.getnombremedico(f.cod_paciente) AS paciente, "+ 
		"getnombreviaingreso(f.via_ingreso) AS via_ingreso,";
	
	/**
	 * Sección FROM para la consulta AN rips capitacion
	 */
	private static final String consultaAN_Capitacion_FROM_Str = "FROM facturas f "+
		"INNER JOIN sub_cuentas scs ON(scs.sub_cuenta=f.sub_cuenta) "+
		"INNER JOIN cuentas c ON(c.id=f.cuenta) "+ 
		"INNER JOIN det_factura_solicitud dfs ON(dfs.factura=f.codigo) "+ 
		"INNER JOIN solicitudes s ON(dfs.solicitud=s.numero_solicitud) "+ 
		"INNER JOIN sol_cirugia_por_servicio sc ON(sc.numero_solicitud=dfs.solicitud) "+
		"INNER JOIN instituciones ins ON (ins.codigo=f.institucion) "+
		"INNER JOIN servicios ss ON(ss.codigo = sc.servicio) "+ 
		"LEFT OUTER JOIN personas p ON(p.codigo=f.cod_paciente) "+ 
		"LEFT OUTER JOIN informacion_parto ip ON(ip.cirugia=sc.codigo OR ip.numero_solicitud = sc.numero_solicitud) "+ 
		"LEFT OUTER JOIN personas p1 ON(p1.codigo=ip.codigo_paciente) "+ 
		"LEFT OUTER JOIN info_parto_hijos ih ON(ih.cirugia=sc.codigo OR ih.numero_solicitud = sc.numero_solicitud) ";
	
	/**
	 * Seccion FROM para la consulta AN Subcontratación rips capitacion
	 */
	private static final String consultaAN_Capitacion_Subcontratadas_FROM_Str = "FROM facturas f "+
		"INNER JOIN sub_cuentas scs ON(scs.sub_cuenta=f.sub_cuenta) "+
		"INNER JOIN personas p ON(p.codigo=f.cod_paciente) "+ 
		"INNER JOIN cuentas c ON(c.id=f.cuenta) "+
		"INNER JOIN informacion_parto ip ON(ip.ingreso=c.id_ingreso) "+ 
		"INNER JOIN personas p1 ON(p1.codigo=ip.codigo_paciente) "+ 
		"INNER JOIN instituciones ins ON (ins.codigo=f.institucion) "+
		"INNER JOIN info_parto_hijos ih ON(ih.ingreso=c.id_ingreso) " ; 
	
	/**
	 * Sección WHERE para la consulta AN rips capitacion
	 */
	private static final String consultaAN_Capitacion_WHERE_Str = "WHERE "+ 
		"f.via_ingreso IN("+ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoUrgencias+") AND "+ 
		"f.convenio=? AND "+ 
		"(to_char(f.fecha,'"+ConstantesBD.formatoFechaBD+"') BETWEEN ? AND ?) AND "+  
		"f.institucion=? AND "+ 
		"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" AND "+
		"scs.contrato IN(REPLACE_CONTRATOS)  AND "+
		"ss.tipo_servicio = '"+ConstantesBD.codigoServicioPartosCesarea+"' AND "+ 
		"p.sexo = "+ConstantesBD.codigoSexoFemenino+" ";
	
	/**
	 * Sección WHERE para la consulta AN rips capitacion
	 */
	private static final String consultaAN_Capitacion_Subcontratadas_WHERE_Str = "WHERE "+ 
		"f.via_ingreso IN("+ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoUrgencias+") AND "+ 
		"f.convenio=? AND "+ 
		"(to_char(f.fecha,'"+ConstantesBD.formatoFechaBD+"') BETWEEN ? AND ?) AND "+  
		"f.institucion=? AND "+ 
		"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" AND "+
		"scs.contrato IN(REPLACE_CONTRATOS)  AND "+
		"f.pac_entidad_subcontratada IS NOT NULL AND "+ 
		"p.sexo = "+ConstantesBD.codigoSexoFemenino+" ";
	
	private static final String strConsultaANPaquete =  "INNER JOIN det_factura_solicitud dfs ON(dfs.factura=f.codigo) " +
													    "INNER JOIN solicitudes s ON(dfs.solicitud=s.numero_solicitud) " +
														"INNER JOIN paquetizacion pq ON(pq.numero_solicitud_paquete=s.numero_solicitud) " +	
														"INNER JOIN detalle_paquetizacion dp ON(dp.codigo_paquetizacion=pq.codigo) " +
														"INNER JOIN sol_cirugia_por_servicio sc ON(sc.numero_solicitud=dp.numero_solicitud) " +
														"INNER JOIN servicios ss ON(ss.codigo = sc.servicio) " +
														"INNER JOIN instituciones ins ON (ins.codigo=f.institucion) " +
														"LEFT OUTER JOIN personas p ON(p.codigo=f.cod_paciente) " +
														"LEFT OUTER JOIN informacion_parto ip ON(ip.cirugia          =sc.codigo OR ip.numero_solicitud = sc.numero_solicitud) " +
														"LEFT OUTER JOIN personas p1 ON(p1.codigo=ip.codigo_paciente) " +
														"LEFT OUTER JOIN info_parto_hijos ih ON(ih.cirugia          =sc.codigo OR ih.numero_solicitud = sc.numero_solicitud) "; 
	
	
	//************************************************************************************************************************
	//************************************************************************************************************************
	//*****************************MÉTODOS RIPS CARTERA***********************************************************************
	//************************************************************************************************************************
	/**
	 * Método para consultar los datos del archivo AF por Factura
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param convenio
	 * @return
	 */
	// Modificado para la generacion de archivos rips para la interfaz ax_rips
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap consultaFacturaAF(Connection con,String fechaInicial,String fechaFinal,int convenio,int codigoInstitucion, boolean esAxRips, String numeroFactura)
	{
		HashMap mapaRetorno=new HashMap();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consulta = "";
			if (!esAxRips){
				consulta = consultaFacturaAFStr;
			}
			else{
				 String consultaAT_WHERE_FACTURA_Str_Ax_Rips = " WHERE f.consecutivo_factura IN ("+numeroFactura+") AND f.institucion=? ";
				 consulta = consultaFacturaAFAxRipsStr+consultaAT_WHERE_FACTURA_Str_Ax_Rips;
			}
			pst= con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			if (!esAxRips){
				pst.setInt(1,convenio);
				pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
				pst.setString(3,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
				pst.setInt(4,codigoInstitucion);				
			}
			else{		
				pst.setInt(1,codigoInstitucion);	
			}
			
			rs = pst.executeQuery();
			int cont=0;
			mapaRetorno.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					String alias=rsm.getColumnLabel(i).toLowerCase();
					mapaRetorno.put(alias+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapaRetorno.put("numRegistros", cont+"");
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultaFacturaAF",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultaFacturaAF", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return mapaRetorno;
	}
	
	/**
	 * Método para consultar los datos del archivo AF por cuenta de cobro
	 * @param con
	 * @param numeroCuentaCobro
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap consultaCuentaCobroAF(Connection con,double numeroCuentaCobro,int codigoInstitucion)
	{
		HashMap mapaRetorno=new HashMap();
		ResultSet rs=null;
		PreparedStatement pst=null;
		try
		{
			logger.info("############## Inicio consultaCuentaCobroAF");
			logger.info("############## Consulta: "+consultaCuentaCobroAFStr);
			pst= new PreparedStatementDecorator(con.prepareStatement(consultaCuentaCobroAFStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,numeroCuentaCobro);
			pst.setInt(2,codigoInstitucion);
			rs=pst.executeQuery();
			int cont=0;
			mapaRetorno.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					String alias=rsm.getColumnLabel(i).toLowerCase();
					mapaRetorno.put(alias+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapaRetorno.put("numRegistros", cont+"");
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultaCuentaCobroAF",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultaCuentaCobroAF", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		logger.info("############## Fin consultaCuentaCobroAF");
		return mapaRetorno;
	}
	
	/**
	 * Método para consultar los registros del archivo AD por factura
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param convenio
	 * @return
	 */
	//	 Modificado para la generacion de archivos rips para la interfaz ax_rips
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap consultaFacturaAD(Connection con,String fechaInicial,String fechaFinal,int convenio,int codigoInstitucion,boolean esAxRips, String numeroFactura)
	{
		HashMap mapaRetorno=new HashMap();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consulta = "";
			if (!esAxRips)
			{
				consulta = consultaAD_SELECT_Str + consultaAD_INNER_01_Str + consultaAD_WHERE_Factura_Str;
			}
			else				
			{
				String consultaAD_WHERE_Factura_Ax_Rips_Str = " "+
					"WHERE "+
					"f.consecutivo_factura in("+numeroFactura+") AND " +
					"f.institucion=? AND "+
					"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" "+
					/**"GROUP BY "+ 
					"f.consecutivo_factura,f.via_ingreso,f.institucion,f.cuenta,f.cod_paciente,p.numero_identificacion,p.tipo_identificacion,dfs.tipo_cargo,s.naturaleza_servicio,a.naturaleza "+**/
					"ORDER BY consecutivo,naturaleza_servicio";
				consulta = consultaAD_SELECT_Str + consultaAD_INNER_01_Str + consultaAD_WHERE_Factura_Ax_Rips_Str;				
			}			
			
			pst= con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			
			if (!esAxRips)
			{
				pst.setInt(1,convenio);
				pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
				pst.setString(3,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
				pst.setInt(4,codigoInstitucion);				
			}
			else
			{
				pst.setInt(1,codigoInstitucion);	
			}
			
			rs = pst.executeQuery();
			int cont=0;
			mapaRetorno.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					String alias=rsm.getColumnLabel(i).toLowerCase();
					mapaRetorno.put(alias+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapaRetorno.put("numRegistros", cont+"");
			
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultaFacturaAD",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultaFacturaAD", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return mapaRetorno;
	}
	
	/**
	 * Método para consultar los registros del archivo AD por cuenta de cobro
	 * @param con
	 * @param numeroCuentaCobro
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap consultaCuentaCobroAD(Connection con,double numeroCuentaCobro,int codigoInstitucion)
	{
		HashMap mapaRetorno=new HashMap();
		ResultSet rs=null;
		PreparedStatement pst=null;
		try
		{
			logger.info("############## Inicio consultaCuentaCobroAD");
			String consulta = consultaAD_SELECT_Str + consultaAD_INNER_02_str + consultaAD_INNER_01_Str + consultaAD_WHERE_CuentaCobro_Str;
			pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,numeroCuentaCobro);
			pst.setInt(2,codigoInstitucion);
			rs=pst.executeQuery();
			int cont=0;
			mapaRetorno.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					String alias=rsm.getColumnLabel(i).toLowerCase();
					mapaRetorno.put(alias+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapaRetorno.put("numRegistros", cont+"");
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultaCuentaCobroAD",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultaCuentaCobroAD", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		logger.info("############## Fin consultaCuentaCobroAD");
		return mapaRetorno;
	}
	
	/**
	 * Método para consultar los registros del archivo US por factura
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param convenio
	 * @return
	 */
	//	 Modificado para la generacion de archivos rips para la interfaz ax_rips
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap consultaFacturaUS(Connection con,String fechaInicial,String fechaFinal,int convenio,int codigoInstitucion,boolean esAxRips, String numeroFactura)
	{
		HashMap mapaRetorno=new HashMap();
		ResultSet rs=null;
		PreparedStatement pst=null;
		try
		{
			String consulta = "";
			if (!esAxRips){
				consulta = consultaFacturaUS_SELECT_Str+consultaFacturaUS_WHERE_Str;
			}
			else{	
				String consultaFacturaUSAxRips_WHERE_Str = "WHERE "+
					"f.consecutivo_factura IN ("+numeroFactura+") AND " +
					"f.institucion=? AND "+ 
					"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" "+ 
					"ORDER BY p.tipo_identificacion,p.numero_identificacion";
				
				consulta = consultaFacturaUS_SELECT_Str+ consultaFacturaUSAxRips_WHERE_Str;
			}
			
			pst= con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			
			if (!esAxRips){
				pst.setInt(1,convenio);
				pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
				pst.setString(3,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
				pst.setInt(4,codigoInstitucion);
			}
			else{
				pst.setInt(1,codigoInstitucion);
			}
			
			rs = pst.executeQuery();
			int cont=0;
			mapaRetorno.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					String alias=rsm.getColumnLabel(i).toLowerCase();
					mapaRetorno.put(alias+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapaRetorno.put("numRegistros", cont+"");
			
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultaFacturaUS",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultaFacturaUS", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return mapaRetorno;
	}
	
	/**
	 * Método para consultar los datos del archivo US por cuenta de cobro
	 * @param con
	 * @param numeroCuentaCobro
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap consultaCuentaCobroUS(Connection con,double numeroCuentaCobro,int codigoInstitucion)
	{
		HashMap mapaRetorno=new HashMap();
		ResultSet rs=null;
		PreparedStatement pst=null;
		try
		{
			logger.info("############## Inicio consultaCuentaCobroUS");
			logger.info("############## Consulta: "+consultaCuentaCobroUSStr);
			pst= new PreparedStatementDecorator(con.prepareStatement(consultaCuentaCobroUSStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,numeroCuentaCobro);
			pst.setInt(2,codigoInstitucion);
			rs=pst.executeQuery();
			int cont=0;
			mapaRetorno.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					String alias=rsm.getColumnLabel(i).toLowerCase();
					mapaRetorno.put(alias+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapaRetorno.put("numRegistros", cont+"");
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultaCuentaCobroUS",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultaCuentaCobroUS", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		logger.info("############## Fin consultaCuentaCobroUS");
		return mapaRetorno;
	}
	
	/**
	 * Método para consultar los datos del archivo AC por factura
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param convenio
	 * @param institucion
	 * @param tarifarioOficial
	 * @return
	 */
	//	 funcion modificada para poder generar archivos rips para la interface ax_rips
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap consultaFacturaAC(Connection con,String fechaInicial,String fechaFinal,int convenio,
			int codigoInstitucion,int tarifarioOficial, boolean esAxRips, String numeroFactura)
	{
		HashMap mapaRetorno=new HashMap();
		ResultSet rs=null;
		PreparedStatement pst=null;
		
		try
		{
			String consultaFacturaAxRips_WHERE_Str = " " +
											"f.consecutivo_factura in("+numeroFactura+") AND "+ 		
											"f.institucion=? AND "+
											"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" "+
											"ORDER BY f.consecutivo_factura ";
			
			String consulta = "";
			// verificar si es una consulta de la interfaz ax_rips
			if (!esAxRips){
				consulta = consultaFacturaACStr  +consultaFactura_WHERE_Str;
			}
			else{
				consulta = consultaFacturaACStr + consultaFacturaAxRips_WHERE_Str;
			}

			pst= con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			if (!esAxRips){
				pst.setInt(1,tarifarioOficial);
				pst.setInt(2,convenio);
				pst.setString(3,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
				pst.setString(4,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
				pst.setInt(5,codigoInstitucion);
			}
			else{
				pst.setInt(1,tarifarioOficial);
				pst.setInt(2,codigoInstitucion);
			}
			rs = pst.executeQuery();
			int cont=0;
			mapaRetorno.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					String alias=rsm.getColumnLabel(i).toLowerCase();
					mapaRetorno.put(alias+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapaRetorno.put("numRegistros", cont+"");
			
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultaFacturaAC",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultaFacturaAC", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return mapaRetorno;
	}
	
	/**
	 * Método para consultar los datos del archivo AC por cuenta de cobro
	 * @param con
	 * @param numeroCuentaCobro
	 * @param institucion
	 * @param tarifario Oficial
	 * @return
	 */

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap consultaCuentaCobroAC(Connection con,double numeroCuentaCobro,int codigoInstitucion,int tarifarioOficial)
	{
		HashMap mapaRetorno=new HashMap();
		ResultSet rs=null;
		PreparedStatement pst=null;
		try
		{
			logger.info("############## Inicio consultaCuentaCobroAC");
			logger.info("############## Consulta: "+consultaCuentaCobroACStr);
			pst= new PreparedStatementDecorator(con.prepareStatement(consultaCuentaCobroACStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,tarifarioOficial);
			pst.setInt(2,tarifarioOficial);
			pst.setDouble(3,numeroCuentaCobro);
			pst.setInt(4,codigoInstitucion);
			rs=pst.executeQuery();
			int cont=0;
			mapaRetorno.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					String alias=rsm.getColumnLabel(i).toLowerCase();
					mapaRetorno.put(alias+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapaRetorno.put("numRegistros", cont+"");
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultaCuentaCobroAC",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultaCuentaCobroAC", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return mapaRetorno;
	}
	
	/**
	 * Método para consultar los datos del archivo AH 
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param cuenta cobro
	 * @param convenio
	 * @param esFactura
	 * @param institucion
	 * @return
	 */
	//	 funcion modificada para poder generar archivos rips para la interface ax_rips
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap consultaAH(Connection con,String fechaInicial,String fechaFinal,
			double cuentaCobro,int convenio,int codigoInstitucion,boolean esFactura, boolean esAxRips, String numeroFactura)
	{
				
		HashMap mapa= new HashMap();
		PreparedStatement pst= null;
		ResultSet rs=null;
		try
		{
			logger.info("############## Inicio consultaAH");
			/**
			 * Sección WHERE común para la consulta RIPS por Factura para consulta de interface de ax_rips
			 */
			String consultaFacturaAxRips_WHERE_Str = " " +
				"f.consecutivo_factura in("+numeroFactura+") AND "+ 		
				"f.institucion=? AND "+
				"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" "+
				"ORDER BY f.consecutivo_factura ";
			String consulta = "";
			
			//			 verificar si es una consulta de la interfaz ax_rips
			if (!esAxRips)
			{
				//se verifica si es por factura o por cuenta de cobro
				if(esFactura)
					consulta = consultaAH_SELECT_Str + consultaAH_LEFTOUTER_Str + consultaAH_WHERE_Str + consultaFactura_WHERE_Str ;
				else
					consulta = consultaAH_SELECT_Str + consultaAH_LEFTOUTER_Str + consultaAH_WHERE_Str + consultaCuentaCobro_WHERE_Str;
			}
			else
			{
				consulta = consultaAH_SELECT_Str + consultaAH_LEFTOUTER_Str + consultaAH_WHERE_Str + consultaFacturaAxRips_WHERE_Str ;
			}
				
			logger.info("############## Consulta: "+consulta);
			pst= con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			
			//			 verificar si es una consulta de la interfaz ax_rips
			if (!esAxRips)
			{
				if(esFactura)
				{
					pst.setInt(1,convenio);
					pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
					pst.setString(3,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
					pst.setInt(4,codigoInstitucion);
				}
				else
				{
					pst.setDouble(1,cuentaCobro);
					pst.setInt(2,codigoInstitucion);
				}
			}
			else
			{
				pst.setInt(1,codigoInstitucion);
			}
			rs=pst.executeQuery();
			int cont=0;
			mapa.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					mapa.put((rsm.getColumnLabel(i)).toLowerCase()+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null||rs.getObject(rsm.getColumnLabel(i)).toString().equals(" ")?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapa.put("numRegistros", cont+"");
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultaAH",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultaAH", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		logger.info("############## Fin consultaAH");
		return mapa;
	}
	
	/**
	 * Método para consultar los datos del archivo AU
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param cuentaCobro
	 * @param convenio
	 * @param codigoInstitucion
	 * @param esFactura
	 * @return
	 */
	//	 funcion modificada para poder generar archivos rips para la interface ax_rips
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap consultaAU(Connection con,String fechaInicial, String fechaFinal,
			double cuentaCobro,int convenio,int codigoInstitucion,boolean esFactura, boolean esAxRips, String numeroFactura)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		HashMap mapa = new HashMap();
		try
		{
			logger.info("############## Inicio consultaAU");
			String consulta = "";
//			 verificar si es una consulta de la interfaz ax_rips
			if (!esAxRips)
			{
				//se verifica si es por factura o por cuenta de cobro
				if(esFactura)
					consulta = "SELECT * FROM (" +
						"("+consultaAU_SELECT_Str + consultaAU_WHERE_FACTURA_Str + consultaAU_WHERE_Str +") " +
						"UNION " +
						"(" + consultaAU_Subcontratadas_SELECT_Str + consultaAU_WHERE_FACTURA_Str + " AND f.pac_entidad_subcontratada IS NOT NULL "+consultaAU_WHERE_Str +") " +
						") t ORDER BY t.consecutivo ";
				else
					consulta = "SELECT * FROM (" +
						"(" +consultaAU_SELECT_Str + consultaAU_WHERE_CUENTA_COBRO_Str +consultaAU_WHERE_Str + ") " +
						"UNION " +
						"(" + consultaAU_Subcontratadas_SELECT_Str + consultaAU_WHERE_CUENTA_COBRO_Str + " AND f.pac_entidad_subcontratada IS NOT NULL " + consultaAU_WHERE_Str +") " +
						") t ORDER BY t.consecutivo ";
				
				
			}
			else
			{
				
				/**
				 * Seccion WHERE FACTURA para la consulta RIPS por Factura del archivo AU para la interface de ax_rips
				 */
				String consultaAUAxRips_WHERE_FACTURA_Str = " WHERE " +
					"f.consecutivo_factura in ("+numeroFactura+") AND "+ 		
					"f.institucion=? AND "+
					"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" ";
				
				consulta = "SELECT * FROM (" +
				"("+consultaAU_SELECT_Str + consultaAUAxRips_WHERE_FACTURA_Str + consultaAU_WHERE_Str +") " +
				"UNION " +
				"(" + consultaAU_Subcontratadas_SELECT_Str + consultaAUAxRips_WHERE_FACTURA_Str + " AND f.pac_entidad_subcontratada IS NOT NULL "+consultaAU_WHERE_Str+") " +
				") t ORDER BY t.consecutivo ";
			
			}
			logger.info("############## Consulta: "+consulta);
			pst= con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
//			 verificar si es una consulta de la interfaz ax_rips
			if (!esAxRips)
			{
				if(esFactura)
				{
					pst.setInt(1,convenio);
					pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
					pst.setString(3,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
					pst.setInt(4,codigoInstitucion);
					pst.setInt(5,convenio);
					pst.setString(6,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
					pst.setString(7,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
					pst.setInt(8,codigoInstitucion);
				}
				else
				{
					pst.setDouble(1,cuentaCobro);
					pst.setInt(2,codigoInstitucion);
					pst.setDouble(3,cuentaCobro);
					pst.setInt(4,codigoInstitucion);
				}
			}
			else
			{
				pst.setInt(1,codigoInstitucion);
				pst.setInt(2,codigoInstitucion);
			}
			
			rs=pst.executeQuery();
			int cont=0;
			mapa.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					mapa.put((rsm.getColumnLabel(i)).toLowerCase()+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null||rs.getObject(rsm.getColumnLabel(i)).toString().equals(" ")?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapa.put("numRegistros", cont+"");
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultaAU",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultaAU", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		logger.info("############## Fin consultaAU");
		return mapa;
	}
	
	/**
	 * Método para consultar los datos del archivo AM
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param cuentaCobro
	 * @param convenio
	 * @param codigoInstitucion
	 * @param esFactura
	 * @return
	 */
	//	 funcion modificada para poder generar archivos rips para la interface ax_rips
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap consultaAM(Connection con,String fechaInicial, String fechaFinal,
			double cuentaCobro,int convenio,int codigoInstitucion,boolean esFactura, boolean esAxRips, String numeroFactura)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		HashMap mapa = new HashMap();
		try
		{
			logger.info("############## Inicio consultaAM");
			String consulta = "";
//			 verificar si es una consulta de la interfaz ax_rips
			if (!esAxRips)
			{
				//se verifica si es por factura o por cuenta de cobro
				if(esFactura)
					consulta = consultaAM_SELECT_Str + " INNER JOIN instituciones ins ON (ins.codigo=f.institucion) " + consultaAM_LEFTOUTER_Str + " WHERE " +
					"f.convenio=? AND "+ 
					"(f.fecha BETWEEN ? AND ?) AND "+ 
					"f.institucion=? AND "+
					"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" "+
					"ORDER BY f.consecutivo_factura ";
				else
					consulta = consultaAM_SELECT_Str + " INNER JOIN instituciones ins ON (ins.codigo=f.institucion) "  + consultaAM_LEFTOUTER_Str + " WHERE " + 
					"f.numero_cuenta_cobro=? AND "+ 
					"f.institucion=? AND "+
					"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" "+
					"ORDER BY f.consecutivo_factura ";	
			}
			else
			{
				consulta = consultaAM_SELECT_Str + " INNER JOIN instituciones ins ON (ins.codigo=f.institucion) " +consultaAM_LEFTOUTER_Str + " WHERE " +
				"f.consecutivo_factura IN ("+numeroFactura+") AND "+				 
				"f.institucion=? AND "+
				"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" "+
				"ORDER BY f.consecutivo_factura ";				
			}
			
			logger.info("############## Consulta: "+consulta);
			pst= con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			
			if (!esAxRips)
			{
				if(esFactura)
				{
					pst.setInt(1,convenio);
					pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
					pst.setString(3,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
					pst.setInt(4,codigoInstitucion);
				}
				else
				{
					pst.setDouble(1,cuentaCobro);
					pst.setInt(2,codigoInstitucion);
				}
			}
			else
			{
				pst.setInt(1,codigoInstitucion);
			}
			
			rs=pst.executeQuery();
			int cont=0;
			mapa.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					mapa.put((rsm.getColumnLabel(i)).toLowerCase()+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null||rs.getObject(rsm.getColumnLabel(i)).toString().equals(" ")?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapa.put("numRegistros", cont+"");
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultaAM",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultaAM", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		logger.info("############## Fin consultaAM");
		return mapa;
	}
	
	/**
	 * Método para consultar los datos del archivo AT
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param cuentaCobro
	 * @param convenio
	 * @param codigoInstitucion
	 * @param tarifarioOficial
	 * @param esFactura
	 * @return
	 */
	//funcion modificada para poder generar archivos rips para la interface ax_rips
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap consultaAT(Connection con,String fechaInicial, String fechaFinal,
			double cuentaCobro,int convenio,int codigoInstitucion,int tarifarioOficial,boolean esFactura, boolean esAxRips, String numeroFactura)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		HashMap mapa = new HashMap();
		try
		{
			logger.info("############## Inicio consultaAT");
			String consulta = "";
			int convenioConsultado = 0;
			// verificar si es una consulta de la interfaz ax_rips
			if(!esAxRips)
			{
				//se verifica si es por factura o por cuenta de cobro
				if(esFactura)
					consulta = "SELECT * FROM (" +
						"("+consultaAT_SELECT_ARTICULOS_Str+consultaAT_WHERE_FACTURA_Str+
						") " +
						"UNION " +
						"("+consultaAT_SELECT_SERVICIOS_Str+consultaAT_WHERE_FACTURA_Str+")" +
						") g ORDER BY g.consecutivo";
				else
					consulta = "SELECT * FROM (" +
					"("+consultaAT_SELECT_ARTICULOS_Str+consultaAT_WHERE_CUENTA_COBRO_Str+
					") " +
					"UNION " +
					"("+consultaAT_SELECT_SERVICIOS_Str+consultaAT_WHERE_CUENTA_COBRO_Str+")" +
					") g ORDER BY g.consecutivo";
			}
			else
			{
				
				/**
				 * Seccion WHERe para la consulta RIPS en la interfaz ax_rips por factura del archivo AT
				 */
				String consultaAT_WHERE_FACTURA_Str_Ax_Rips = " WHERE f.consecutivo_factura IN ("+numeroFactura+") AND f.institucion=? ";
				
				consulta = "SELECT * FROM (" +
				"("+consultaAT_SELECT_ARTICULOS_Str+consultaAT_WHERE_FACTURA_Str_Ax_Rips+
				") " +
				"UNION " +
				"("+consultaAT_SELECT_SERVICIOS_Str+consultaAT_WHERE_FACTURA_Str_Ax_Rips+")" +
				") g ORDER BY g.consecutivo";
				
			}
				
			logger.info("############## Consulta: "+consulta);
			pst= con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			
			// verificar si es una consulta de la interfaz ax_rips
			if(!esAxRips)
			{	
				if(esFactura)
				{
					pst.setInt(1,convenio);
					pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
					pst.setString(3,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
					pst.setInt(4,codigoInstitucion);
					
					pst.setInt(5,tarifarioOficial);
					pst.setInt(6,tarifarioOficial);
					pst.setInt(7,tarifarioOficial);
					pst.setInt(8,tarifarioOficial);
					pst.setInt(9,tarifarioOficial);
					pst.setInt(10,tarifarioOficial);
					pst.setInt(11,tarifarioOficial);
					pst.setInt(12,tarifarioOficial);
					pst.setInt(13,tarifarioOficial);
					pst.setInt(14,tarifarioOficial);
					pst.setInt(15,convenio);
					pst.setString(16,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
					pst.setString(17,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
					pst.setInt(18,codigoInstitucion);
				}
				else
				{
					pst.setDouble(1,cuentaCobro);
					pst.setInt(2,codigoInstitucion);
					pst.setInt(3,tarifarioOficial);
					pst.setInt(4,tarifarioOficial);
					pst.setInt(5,tarifarioOficial);
					pst.setInt(6,tarifarioOficial);
					pst.setInt(7,tarifarioOficial);
					pst.setInt(8,tarifarioOficial);
					pst.setInt(9,tarifarioOficial);
					pst.setInt(10,tarifarioOficial);
					pst.setInt(11,tarifarioOficial);
					pst.setInt(12,tarifarioOficial);
					pst.setDouble(13,cuentaCobro);
					pst.setInt(14,codigoInstitucion);
				}
			}
			else
			{
				convenioConsultado = tarifarioOficial;//consulartConvenioXNroFactura(con,numeroFactura);
				
				pst.setInt(1,codigoInstitucion);
				
				pst.setInt(2,convenioConsultado);
				pst.setInt(3,convenioConsultado);
				pst.setInt(4,convenioConsultado);
				pst.setInt(5,convenioConsultado);
				pst.setInt(6,convenioConsultado);
				pst.setInt(7,convenioConsultado);
				pst.setInt(8,convenioConsultado);
				pst.setInt(9,convenioConsultado);
				pst.setInt(10,convenioConsultado);
				pst.setInt(11,convenioConsultado);
				pst.setInt(12,codigoInstitucion);
				
			}
			
			
			rs=pst.executeQuery();
			int cont=0;
			mapa.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					mapa.put((rsm.getColumnLabel(i)).toLowerCase()+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null||rs.getObject(rsm.getColumnLabel(i)).toString().equals(" ")?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapa.put("numRegistros", cont+"");
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultaAT",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultaAT", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		logger.info("############## Fin consultaAT");
		return mapa;
	}
	

	
	/**
	 * Método para consultar los datos del archivo AP
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param cuentaCobro
	 * @param convenio
	 * @param codigoInstitucion
	 * @param tarifarioOficial
	 * @param esFactura
	 * @return
	 */
	// funcion modificada para poder generar archivos rips para la interface ax_rips
	//public static HashMap consultaAP(Connection con,String fechaInicial, String fechaFinal,
	//		double cuentaCobro,int convenio,int codigoInstitucion,int tarifarioOficial,boolean esFactura)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap consultaAP(Connection con,String fechaInicial, String fechaFinal,
			double cuentaCobro,int convenio,int codigoInstitucion,int tarifarioOficial,boolean esFactura, boolean esAxRips, String numeroFactura)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		HashMap mapa=new HashMap();
		try
		{
			logger.info("############## Inicio consultaAP");
			String consulta = "";
			// verificar si es una consulta de la interfaz ax_rips
			if(!esAxRips)
			{
				//se verifica si es por factura o por cuenta de cobro
				if(esFactura)
					consulta = consultaAP_SELECT_Str + consultaFactura_WHERE_Str;
				else
					consulta = consultaAP_SELECT_Str + consultaCuentaCobro_WHERE_Str ;
			}
			else
			{
				/**
				 * Seccion WHERE para la consulta de facturas asociadas a la tabla de interfaz ax_rips
				 */
				String consulta_WHERE_AX_RIPS = " f.consecutivo_factura IN ("+numeroFactura+")";
				
				consulta = consultaAP_SELECT_Str + consulta_WHERE_AX_RIPS ;
			}
			
			logger.info("############## Consulta: "+consulta);
			pst= con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			
			
			if(!esAxRips)
			{
				pst.setInt(1,tarifarioOficial);
				if(esFactura)
				{
					pst.setInt(2,convenio);
					pst.setString(3,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
					pst.setString(4,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
					pst.setInt(5,codigoInstitucion);
				}
				else
				{
					pst.setDouble(2,cuentaCobro);
					pst.setInt(3,codigoInstitucion);
				}
			}
			else
			{
				// consultar el numero de factura referente al numero de envio dado
				/*UtilidadBDInterfaz objInterfaz = new UtilidadBDInterfaz();
				String nroFactura = objInterfaz.consultarFacturasEnAxRips(numeroEnvio, codigoInstitucion);*/
				// cargar el convenio
				pst.setInt(1,tarifarioOficial);//consulartConvenioXNroFactura(con,numeroFactura));				
				logger.info("\n\n\n\n Valor convenio consultado : " + tarifarioOficial );
			}
				
			
			rs=pst.executeQuery();
			int cont=0;
			mapa.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					mapa.put((rsm.getColumnLabel(i)).toLowerCase()+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null||rs.getObject(rsm.getColumnLabel(i)).toString().equals(" ")?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapa.put("numRegistros", cont+"");
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultaAP",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultaAP", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		logger.info("############## Fin consultaAP");
		return mapa;
	}
	
	
	
	
	
	/**
	 * Método para consultar los datos del archivo AN
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param cuentaCobro
	 * @param convenio
	 * @param codigoInstitucion
	 * @param esFactura
	 * @return
	 */
	// funcion modificada para poder generar archivos rips para la interface ax_rips
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap consultaAN(Connection con,String fechaInicial, String fechaFinal,
			double cuentaCobro,int convenio,int codigoInstitucion,boolean esFactura, boolean esAxRips, String numeroFactura)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		HashMap mapa= new HashMap();
		try
		{
			logger.info("############## Inicio consultaAN");
			String consulta = "";
			
			// verificar si es una consulta de la interfaz ax_rips
			if (!esAxRips){
				//se verifica si es por factura o por cuenta de cobro
				if(esFactura)
					consulta = "SELECT * FROM (" +
						"(" +consultaAN_SELECT_Str + consultaAN_LEFT_OUTER_Str + consultaAN_WHERE01_Str + ") " +
						" UNION " +
						"("+consultaAN_Subcontratadas_SELECT_Str + consultaAN_Subcontratadas_INNER_Str + consultaAN_Subcontratada_WHERE01_Str + ") " +
						" UNION " +
						"("+consultaAN_SELECT_Str+strConsultaANPaquete+consultaAN_WHERE01_Str+" AND dp.servicio_cx IS NOT NULL)" +
						") t ORDER BY t.consecutivo ";
				else
					consulta = "SELECT * FROM (" +
						"(" + consultaAN_SELECT_Str  + consultaAN_LEFT_OUTER_Str + consultaAN_WHERE02_Str + ") " +
						" UNION " +
						"("+consultaAN_Subcontratadas_SELECT_Str + consultaAN_Subcontratadas_INNER_Str + consultaAN_Subcontratada_WHERE02_Str + ") " +
						" UNION " +
						"("+consultaAN_SELECT_Str+strConsultaANPaquete+consultaAN_WHERE02_Str+" AND dp.servicio_cx IS NOT NULL)" +
						") t ORDER BY t.consecutivo ";
				
			}
			else
			{
				/**
				 * Sección WHERE para la consulta RIPS del archivo AN (Para factura) para la interface de ax_rips
				 */
				String consultaANAxRips_WHERE01_Str = " WHERE " +
					"f.via_ingreso IN("+ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoUrgencias+") AND " +
					"f.consecutivo_factura IN ("+numeroFactura+") AND "+ 
					"f.institucion=? AND "+
					"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" AND "+
					"ss.tipo_servicio = '"+ConstantesBD.codigoServicioPartosCesarea+"' AND "+
					"p.sexo = "+ConstantesBD.codigoSexoFemenino+"  "+
					" ";
				
				
				/**
				 * Sección WHERE para la consulta RIPS del archivo AN Subcontratada (Para factura) para la interface de ax_rips
				 */
				 String consultaANAxRips_Subcontratada_WHERE01_Str = " WHERE " +
					"f.via_ingreso IN("+ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoUrgencias+") AND " +
					"f.consecutivo_factura IN("+numeroFactura+") AND "+ 	
					"f.institucion=? AND "+
					"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" AND "+
					"f.pac_entidad_subcontratada IS NOT NULL AND "+
					"p.sexo = "+ConstantesBD.codigoSexoFemenino+"  "+
					" ";
				
				
				consulta = "SELECT * FROM (" +
				"(" +consultaAN_SELECT_Str + consultaAN_LEFT_OUTER_Str + consultaANAxRips_WHERE01_Str + ") " +
				" UNION " +
				"("+consultaAN_Subcontratadas_SELECT_Str + consultaAN_Subcontratadas_INNER_Str + consultaANAxRips_Subcontratada_WHERE01_Str + ")" +
				") t ORDER BY t.consecutivo ";
			}
			logger.info("############## Consulta: "+consulta);
			pst= con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);

			//			 verificar si es una consulta de la interfaz ax_rips
			if (!esAxRips){
				if(esFactura)
				{
					pst.setInt(1,convenio);
					pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
					pst.setString(3,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
					pst.setInt(4,codigoInstitucion);
					pst.setInt(5,convenio);
					pst.setString(6,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
					pst.setString(7,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
					pst.setInt(8,codigoInstitucion);
					pst.setInt(9,convenio);
					pst.setString(10,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
					pst.setString(11,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
					pst.setInt(12,codigoInstitucion);
				}
				else
				{
					pst.setDouble(1,cuentaCobro);
					pst.setInt(2,codigoInstitucion);
					pst.setDouble(3,cuentaCobro);
					pst.setInt(4,codigoInstitucion);
					pst.setDouble(5,cuentaCobro);
					pst.setInt(6,codigoInstitucion);
				}
			}
			else
			{
				pst.setInt(1,codigoInstitucion);
				pst.setDouble(2,cuentaCobro);
			
			}
			
			rs=pst.executeQuery();
			int cont=0;
			mapa.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					mapa.put((rsm.getColumnLabel(i)).toLowerCase()+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null||rs.getObject(rsm.getColumnLabel(i)).toString().equals(" ")?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapa.put("numRegistros", cont+"");
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultaAN",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultaAN", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		logger.info("############## Fin consultaAN");
		return mapa;
	}
	
	//************************************************************************************************************************
	//***************************MÉTODOS RIPS CONSULTORIOS********************************************************************
	//************************************************************************************************************************
	/**
	 * Método para consultar los registros del RIPS por Rangos
	 * @param con
	 * @param codigoConvenio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap consultaRegistrosPorRangos(Connection con,int codigoConvenio,String fechaInicial,String fechaFinal,String rips)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		HashMap mapaRetorno = new HashMap();
		try
		{
			//columnas del listado
			String[] columnas={
					"fecha",
					"paciente",
					"tipo_num_id",
					"servicio",
					"tipo",
					"solicitud",
					"cuenta",
					"codigo_paciente",
					"datos_servicio"
				};
			
			pst= con.prepareStatement(consultaRegistrosPorRangosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			
			pst.setInt(1,codigoConvenio);
			pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
			pst.setString(3,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
			pst.setString(4,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
			pst.setString(5,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
			pst.setString(6,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
			pst.setString(7,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
			pst.setString(8,rips);
			pst.setString(9,rips);
			pst.setInt(10,codigoConvenio);
			pst.setString(11,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
			pst.setString(12,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
			pst.setString(13,rips);
			
			rs=pst.executeQuery();
			
			String keyValue = "";
		 	String regValue = "";
		    int i;
		    int k=0;
		    mapaRetorno.put("numRegistros", (new Integer(0)));
	    	do{
	    		for( i=0; i<columnas.length; i++){
	    		   keyValue = columnas[i]  +"_" +k ;
	    		   if(rs.getString(columnas[i])==null){
	    			   regValue = "";
	    		   }
		    	   else{
		    		   regValue = rs.getString(columnas[i]); 
		    	   }	          
	    		   if(regValue == null){
	    			   regValue = "";
	    		   }
	    		   mapaRetorno.put(keyValue, regValue); 
		        }
			    k  +=  1;
	    	}while(rs.next());
		    mapaRetorno.put("numRegistros", (new Integer(k)));
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultaRegistrosPorRangos",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultaRegistrosPorRangos", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return mapaRetorno;
	}
	
	/**
	 * Método usado para cargar la información rips de un cita.
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap cargarDatosRipsCita(Connection con,int numeroSolicitud)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		HashMap mapaRetorno = new HashMap();
		try
		{
			//columnas del listado
			String[] columnas={
						"servicio",
						"diag_ppal",
						"diag_rel1",
						"diag_rel2",
						"diag_rel3",
						"tipo_diag",
						"causa_externa",
						"finalidad",
						"autorizacion",
						"valor_total",
						"valor_copago",
						"solicitud",
						"cuenta",
						"paciente"
					};
			pst= con.prepareStatement(cargarDatosRipsCitaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1,numeroSolicitud);
			rs= pst.executeQuery();
			
			String keyValue = "";
		 	String regValue = "";
		    int i;
		    int k=0;
		    mapaRetorno.put("numRegistros", (new Integer(0)));
	    	do{
	    		for( i=0; i<columnas.length; i++){
	    		   keyValue = columnas[i]  +"_" +k ;
	    		   if(rs.getString(columnas[i])==null){
	    			   regValue = "";
	    		   }
		    	   else{
		    		   regValue = rs.getString(columnas[i]); 
		    	   }	          
	    		   if(regValue == null){
	    			   regValue = "";
	    		   }
	    		   mapaRetorno.put(keyValue, regValue); 
		        }
			    k  +=  1;
	    	}while(rs.next());
		    mapaRetorno.put("numRegistros", (new Integer(k)));
			
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR cargarDatosRipsCita",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR cargarDatosRipsCita", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return mapaRetorno;
	}
	
	/**
	 * Método usado para cargar parte de los datos rips de un solicitud diferente a cita que tiene cargada un servicio
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap cargarDatosRipsNoCita(Connection con,int numeroSolicitud)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		HashMap mapaRetorno = new HashMap();
		try
		{
			// columnas del listado
			String[] columnas={
						"servicio",
						"valor_total",
						"autorizacion",
						"solicitud",
						"cuenta",
						"paciente",
						"tipo_servicio",
						"naturaleza"
					};
			pst= con.prepareStatement(cargarDatosRipsNoCitaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1,numeroSolicitud);
			rs=pst.executeQuery();
			
			String keyValue = "";
		 	String regValue = "";
		    int i;
		    int k=0;
		    mapaRetorno.put("numRegistros", (new Integer(0)));
	    	do{
	    		for( i=0; i<columnas.length; i++){
	    		   keyValue = columnas[i]  +"_" +k ;
	    		   if(rs.getString(columnas[i])==null){
	    			   regValue = "";
	    		   }
		    	   else{
		    		   regValue = rs.getString(columnas[i]); 
		    	   }	          
	    		   if(regValue == null){
	    			   regValue = "";
	    		   }
	    		   mapaRetorno.put(keyValue, regValue); 
		        }
			    k  +=  1;
	    	}while(rs.next());
		    mapaRetorno.put("numRegistros", (new Integer(k)));
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR cargarDatosRipsNoCita",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR cargarDatosRipsNoCita", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return mapaRetorno;
	}
	
	/**
	 * Método usado para consultar servicios por codigo CUPS
	 * @param con
	 * @param codigoCups
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap consultarServicios(Connection con,String codigoCups)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		HashMap mapaRetorno = new HashMap();
		try
		{
			//columnas del listado
			String[] columnas={
						"servicio"
					};
			pst= con.prepareStatement(consultarServiciosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setString(1,codigoCups);
			rs=pst.executeQuery();
			String keyValue = "";
		 	String regValue = "";
		    int i;
		    int k=0;
		    mapaRetorno.put("numRegistros", (new Integer(0)));
	    	do{
	    		for( i=0; i<columnas.length; i++){
	    		   keyValue = columnas[i]  +"_" +k ;
	    		   if(rs.getString(columnas[i])==null){
	    			   regValue = "";
	    		   }
		    	   else{
		    		   regValue = rs.getString(columnas[i]); 
		    	   }	          
	    		   if(regValue == null){
	    			   regValue = "";
	    		   }
	    		   mapaRetorno.put(keyValue, regValue); 
		        }
			    k  +=  1;
	    	}while(rs.next());
		    mapaRetorno.put("numRegistros", (new Integer(k)));
			
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultarServicios",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultarServicios", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return mapaRetorno;
	}
	
	/**
	 * Método que consulta la tarifa del servicio y calcula el valor del copago según la cuenta
	 * @param con
	 * @param codigoServicio
	 * @param idCuenta
	 * @return valor total (separadorSplit) valor copago
	 */
	public static String consultarValoresServicio(Connection con,int codigoServicio,int idCuenta)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			pst= con.prepareStatement(consultarValoresServicioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			double valorTotal= ConstantesBD.codigoNuncaValidoDouble;
			pst.setDouble(1,valorTotal);
			pst.setDouble(2,valorTotal);
			pst.setInt(3,idCuenta);
			
			rs=pst.executeQuery();
			if(rs.next())
			{
				String valTotal=(rs.getString("valor_total")!=null)?rs.getString("valor_total"):"";
				String valCopago=(rs.getString("valor_copago")!=null)?rs.getString("valor_copago"):"";
				String resultado=valTotal+ConstantesBD.separadorSplit+valCopago;
				return resultado;
			}			
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultarValoresServicio",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultarValoresServicio", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return null;
	}
	
	/**
	 * Método para consultar los servicio por descripcion
	 * @param con
	 * @param criterio
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap consultarServicioXNombre(Connection con,String criterio)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		HashMap mapaRetorno = new HashMap();
		try
		{
			String consulta="SELECT  " +
				"getcodigopropservicio2(s.codigo,"+ConstantesBD.codigoTarifarioCups+") || '"+ConstantesBD.separadorSplit+"' ||  " +
				"getnombreservicio(s.codigo,"+ConstantesBD.codigoTarifarioCups+") || '"+ConstantesBD.separadorSplit+"' ||  " +
				"s.codigo || '"+ConstantesBD.separadorSplit+"' || " +
				"s.tipo_servicio  || '"+ConstantesBD.separadorSplit+"' || " +
				"s.naturaleza_servicio AS servicio "+ 
				"FROM servicios s,referencias_servicio rs  " +
				"WHERE  rs.servicio=s.codigo AND " +
				"((tipo_servicio='"+ConstantesBD.codigoServicioInterconsulta+"' AND (naturaleza_servicio='01' OR naturaleza_servicio='05')) OR " +
				" (tipo_servicio='"+ConstantesBD.codigoServicioProcedimiento+"' AND (naturaleza_servicio='02' OR naturaleza_servicio='03')) OR " +
				" (tipo_servicio='"+ConstantesBD.codigoServicioNoCruentos+"' AND (naturaleza_servicio='02' OR naturaleza_servicio='03' OR naturaleza_servicio='04')) OR " +
				" (tipo_servicio='"+ConstantesBD.codigoServicioQuirurgico+"' AND naturaleza_servicio='04') OR " +
				" (tipo_servicio='"+ConstantesBD.codigoServicioPartosCesarea+"' AND (naturaleza_servicio='03' OR naturaleza_servicio='04')) OR " +
				" (tipo_servicio='"+ConstantesBD.codigoServicioPaquetes+"' AND (naturaleza_servicio='03' OR naturaleza_servicio='04'))) AND " +
				"UPPER(rs.descripcion) like UPPER('%"+criterio+"%') AND rs.tipo_tarifario="+ConstantesBD.codigoTarifarioCups;
			//columnas del listado
			String[] columnas={
						"servicio"
					};
			pst=con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			rs=pst.executeQuery();
			
			String keyValue = "";
		 	String regValue = "";
		    int i;
		    int k=0;
		    mapaRetorno.put("numRegistros", (new Integer(0)));
	    	do{
	    		for( i=0; i<columnas.length; i++){
	    		   keyValue = columnas[i]  +"_" +k ;
	    		   if(rs.getString(columnas[i])==null){
	    			   regValue = "";
	    		   }
		    	   else{
		    		   regValue = rs.getString(columnas[i]); 
		    	   }	          
	    		   if(regValue == null){
	    			   regValue = "";
	    		   }
	    		   mapaRetorno.put(keyValue, regValue); 
		        }
			    k  +=  1;
	    	}while(rs.next());
		    mapaRetorno.put("numRegistros", (new Integer(k)));
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultarServicioXNombre",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultarServicioXNombre", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return mapaRetorno;
	}
	
	/**
	 * Método para cargar el resumen de un registro RIPS
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap consultarRegistroRips(Connection con,int consecutivo)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		HashMap mapaRetorno = new HashMap();
		try
		{
			//columnas del listado
			String[] columnas={
					"convenio",
					"fecha_inicial",
					"fecha_final",
					"servicio",
					"tipo_diag",
					"diag_ppal",
					"diag_rel1",
					"diag_rel2",
					"diag_rel3",
					"causa_externa",
					"finalidad",
					"valor_total",
					"valor_copago",
					"valor_empresa",
					"autorizacion",
					"ambito",
					"personal",
					"forma",
					"paciente",
					"cuenta",
					"solicitud"
			};
			pst= con.prepareStatement(consultarRegistroRipsStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1,consecutivo);
			rs=pst.executeQuery();
			
			String keyValue = "";
		 	String regValue = "";
		    int i;
		    int k=0;
		    mapaRetorno.put("numRegistros", (new Integer(0)));
	    	do{
	    		for( i=0; i<columnas.length; i++){
	    		   keyValue = columnas[i]  +"_" +k ;
	    		   if(rs.getString(columnas[i])==null){
	    			   regValue = "";
	    		   }
		    	   else{
		    		   regValue = rs.getString(columnas[i]); 
		    	   }	          
	    		   if(regValue == null){
	    			   regValue = "";
	    		   }
	    		   mapaRetorno.put(keyValue, regValue); 
		        }
			    k  +=  1;
	    	}while(rs.next());
		    mapaRetorno.put("numRegistros", (new Integer(k)));			
			
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultarRegistroRips",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultarRegistroRips", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return mapaRetorno;
	}
	
	/**
	 * Método para consultar los registros del archivo AF
	 * del RIPS consultorios
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap consultaConsultoriosAF(Connection con,String numeroFactura,String numeroRemision)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		HashMap mapaRetorno = new HashMap();
		try
		{
			//columnas del listado
			String columnas[]={
					"consecutivo",
					"fecha",
					"fecha_inicial",
					"fecha_final",
					"contrato",
					"indicativo_transito",
					"numero_poliza",
					"valor_bruto",
					"valor_convenio"
					};
			
			pst= con.prepareStatement(consultaConsultoriosAFStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setString(1,numeroFactura);
			pst.setInt(2,Integer.parseInt(numeroRemision));
			rs=pst.executeQuery();
			
			String keyValue = "";
		 	String regValue = "";
		    int i;
		    int k=0;
		    mapaRetorno.put("numRegistros", (new Integer(0)));
	    	do{
	    		for( i=0; i<columnas.length; i++){
	    		   keyValue = columnas[i]  +"_" +k ;
	    		   if(rs.getString(columnas[i])==null){
	    			   regValue = "";
	    		   }
		    	   else{
		    		   regValue = rs.getString(columnas[i]); 
		    	   }	          
	    		   if(regValue == null){
	    			   regValue = "";
	    		   }
	    		   mapaRetorno.put(keyValue, regValue); 
		        }
			    k  +=  1;
	    	}while(rs.next());
		    mapaRetorno.put("numRegistros", (new Integer(k)));			
			
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultaConsultoriosAF",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultaConsultoriosAF", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return mapaRetorno;
	}
	
	/**
	 * Método para consultar los registros del archivo AD
	 * del RIPS consultorios
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap consultaConsultoriosAD(Connection con,String numeroFactura,String numeroRemision)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		HashMap mapaRetorno = new HashMap();
		try
		{
			//columnas del listado
			String columnas[]={
					"consecutivo",
					"naturaleza_servicio",
					"cantidad",
					"total"
					};
			pst= con.prepareStatement(consultaConsultoriosADStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setString(1,numeroFactura);
			pst.setInt(2,Integer.parseInt(numeroRemision));
			rs=pst.executeQuery();
			
			String keyValue = "";
		 	String regValue = "";
		    int i;
		    int k=0;
		    mapaRetorno.put("numRegistros", (new Integer(0)));
	    	do{
	    		for( i=0; i<columnas.length; i++){
	    		   keyValue = columnas[i]  +"_" +k ;
	    		   if(rs.getString(columnas[i])==null){
	    			   regValue = "";
	    		   }
		    	   else{
		    		   regValue = rs.getString(columnas[i]); 
		    	   }	          
	    		   if(regValue == null){
	    			   regValue = "";
	    		   }
	    		   mapaRetorno.put(keyValue, regValue); 
		        }
			    k  +=  1;
	    	}while(rs.next());
		    mapaRetorno.put("numRegistros", (new Integer(k)));			
			
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultaConsultoriosAD",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultaConsultoriosAD", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return mapaRetorno;
	}
	
	/**
	 * Método para consultar los registros del archivo US
	 * del RIPS consultorios
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap consultaConsultoriosUS(Connection con,String numeroFactura,String numeroRemision)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		HashMap mapaRetorno = new HashMap();
		try
		{
			//columnas del listado
			String columnas[]={
					"tipo_identificacion","numero_identificacion","primer_apellido",
					"segundo_apellido","primer_nombre","segundo_nombre","fecha_apertura",
					"fecha_nacimiento","sexo","depto_vivienda","ciudad_vivienda",
					"zona_domicilio","consecutivo","cuenta","paciente","via_ingreso","orden","desplazado"
					};
			
			pst= con.prepareStatement(consultaConsultoriosUSStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setString(1,numeroFactura);
			pst.setInt(2,Integer.parseInt(numeroRemision));
			rs=pst.executeQuery();
			
			String keyValue = "";
		 	String regValue = "";
		    int i;
		    int k=0;
		    mapaRetorno.put("numRegistros", (new Integer(0)));
	    	do{
	    		for( i=0; i<columnas.length; i++){
	    		   keyValue = columnas[i]  +"_" +k ;
	    		   if(rs.getString(columnas[i])==null){
	    			   regValue = "";
	    		   }
		    	   else{
		    		   regValue = rs.getString(columnas[i]); 
		    	   }	          
	    		   if(regValue == null){
	    			   regValue = "";
	    		   }
	    		   mapaRetorno.put(keyValue, regValue); 
		        }
			    k  +=  1;
	    	}while(rs.next());
		    mapaRetorno.put("numRegistros", (new Integer(k)));			
			
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultaConsultoriosUS",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultaConsultoriosUS", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return mapaRetorno;
			
	}
	
	/**
	 * Método para consultar los registros del archivo AC
	 * del RIPS consultorios
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap consultaConsultoriosAC(Connection con,String numeroFactura,String numeroRemision)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		HashMap mapaRetorno = new HashMap();
		try
		{
			//columnas del listado
			String columnas[]={
					"consecutivo","tipo_identificacion","numero_identificacion",
					"fecha_consulta","numero_autorizacion","codigo_consulta",
					"finalidad_consulta","causa_externa","cod_diag_ppal","cod_diag_rel1",
					"cod_diag_rel2","cod_diag_rel3","tipo_diag_ppal","valor",
					"valor_copago","valor_empresa","naturaleza_servicio","cuenta",
					"paciente","via_ingreso","orden"
					};
			
			pst= con.prepareStatement(consultaConsultoriosACStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setString(1,numeroFactura);
			pst.setInt(2,Integer.parseInt(numeroRemision));			
			rs=pst.executeQuery();
			
			String keyValue = "";
		 	String regValue = "";
		    int i;
		    int k=0;
		    mapaRetorno.put("numRegistros", (new Integer(0)));
	    	do{
	    		for( i=0; i<columnas.length; i++){
	    		   keyValue = columnas[i]  +"_" +k ;
	    		   if(rs.getString(columnas[i])==null){
	    			   regValue = "";
	    		   }
		    	   else{
		    		   regValue = rs.getString(columnas[i]); 
		    	   }	          
	    		   if(regValue == null){
	    			   regValue = "";
	    		   }
	    		   mapaRetorno.put(keyValue, regValue); 
		        }
			    k  +=  1;
	    	}while(rs.next());
		    mapaRetorno.put("numRegistros", (new Integer(k)));			
			
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultaConsultoriosAC",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultaConsultoriosAC", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return mapaRetorno;
	}
	
	/**
	 * Método para consultar los registros del archivo AP
	 * del RIPS consultorios
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap consultaConsultoriosAP(Connection con,String numeroFactura,String numeroRemision)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		HashMap mapaRetorno = new HashMap();
		try
		{
			//columnas del listado
			String columnas[]={
					"consecutivo","tipo_identificacion","numero_identificacion",
					"fecha_procedimiento","numero_autorizacion","codigo_procedimiento",
					"ambito_realizacion","finalidad_procedimiento","personal_atiende",
					"cod_diag_ppal","cod_diag_rel","cod_diag_com","forma_realizacion",
					"valor","cuenta","paciente","via_ingreso","orden","tipo_servicio",
					"naturaleza_servicio"
					};
			
			pst= con.prepareStatement(consultaConsultoriosAPStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setString(1,numeroFactura);
			pst.setInt(2,Integer.parseInt(numeroRemision));
			rs=pst.executeQuery();
			
			String keyValue = "";
		 	String regValue = "";
		    int i;
		    int k=0;
		    mapaRetorno.put("numRegistros", (new Integer(0)));
	    	do{
	    		for( i=0; i<columnas.length; i++){
	    		   keyValue = columnas[i]  +"_" +k ;
	    		   if(rs.getString(columnas[i])==null){
	    			   regValue = "";
	    		   }
		    	   else{
		    		   regValue = rs.getString(columnas[i]); 
		    	   }	          
	    		   if(regValue == null){
	    			   regValue = "";
	    		   }
	    		   mapaRetorno.put(keyValue, regValue); 
		        }
			    k  +=  1;
	    	}while(rs.next());
		    mapaRetorno.put("numRegistros", (new Integer(k)));			
			
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultaConsultoriosAP",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultaConsultoriosAP", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return mapaRetorno;
			
	}
	
	/**
	 * Método usado para consultar la generación de RIPS consultorios
	 * @param con
	 * @param codigoConvenio
	 * @param fechaFactura
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap consultaGeneracionRips(Connection con,int codigoConvenio,String fechaFactura)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		HashMap mapaRetorno = new HashMap();		
		try
		{
			//columnas del listado
			String columnas[]={
						"fecha_factura",
						"numero_factura",
						"convenio",
						"fecha_generacion",
						"numero_remision",
						"fecha_remision",
						"fecha_inicial",
						"fecha_final",
						"usuario"
					};
			String consulta="SELECT DISTINCT "+ 
				"fecha_factura AS fecha_factura,numero_factura AS numero_factura,"+
				"getnombreconvenio(convenio) AS convenio," +
				"fecha_generacion || ' ' || hora_generacion AS fecha_generacion,"+
				"numero_remision AS numero_remision,"+
				"fecha_remision AS fecha_remision,fecha_inicial AS fecha_inicial,"+
				"fecha_final AS fecha_final,usuario AS usuario "+ 
				"FROM rips_consultorios WHERE rips=true ";
			
			if(codigoConvenio!=0)
				consulta+="AND convenio="+codigoConvenio+" ";
			
			if(!fechaFactura.equals(""))
				consulta+="AND fecha_factura='"+UtilidadFecha.conversionFormatoFechaABD(fechaFactura)+"' ";
			
			consulta+="ORDER BY fecha_factura DESC";
			
			pst=con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			rs=pst.executeQuery();
			
			String keyValue = "";
		 	String regValue = "";
		    int i;
		    int k=0;
		    mapaRetorno.put("numRegistros", (new Integer(0)));
	    	do{
	    		for( i=0; i<columnas.length; i++){
	    		   keyValue = columnas[i]  +"_" +k ;
	    		   if(rs.getString(columnas[i])==null){
	    			   regValue = "";
	    		   }
		    	   else{
		    		   regValue = rs.getString(columnas[i]); 
		    	   }	          
	    		   if(regValue == null){
	    			   regValue = "";
	    		   }
	    		   mapaRetorno.put(keyValue, regValue); 
		        }
			    k  +=  1;
	    	}while(rs.next());
		    mapaRetorno.put("numRegistros", (new Integer(k)));			
			
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultaConsultoriosAP",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultaConsultoriosAP", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return mapaRetorno;
			
	}
	
	/**
	 * Método para actualizar los datos de un registro RIPS ya insertado
	 * @param con
	 * @param consecutivo
	 * @param valorTotal
	 * @param valorCopago
	 * @param valorEmpresa
	 * @param autorizacion
	 * @return
	 */
	public static int actualizarDatosRips(Connection con,int consecutivo,double valorTotal,
			double valorCopago,double valorEmpresa,String autorizacion)
	{
		PreparedStatement pst=null;
		try
		{
			pst= con.prepareStatement(actualizarDatosRipsStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setDouble(1,valorTotal);
			
			if(valorCopago==-1){
				pst.setNull(2,Types.DOUBLE);
			}
			else{
				pst.setDouble(2,valorCopago);
			}
			if(valorEmpresa==-1){
				pst.setNull(3,Types.DOUBLE);
			}
			else{
				pst.setDouble(3,valorEmpresa);
			}
			pst.setString(4,autorizacion);
			pst.setInt(5,consecutivo);
			
			return pst.executeUpdate();
			
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR actualizarDatosRips",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR actualizarDatosRips", e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Método para verificar si una solicitud ya está registrada en RIPS
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static int verificarExistenciaRegistroRips(Connection con,int numeroSolicitud)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			pst= con.prepareStatement(verificarExistenciaRegistroRipsStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1,numeroSolicitud);
			
			rs=pst.executeQuery();
			if(rs.next())
			{
				return rs.getInt("consecutivo");
			}	
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR verificarExistenciaRegistroRips",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR verificarExistenciaRegistroRips", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Método para consultar los datos de un servicio
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap consultarServicio(Connection con,int codigoServicio)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		HashMap mapaRetorno = new HashMap();
		try
		{
			//columnas del listado
			String columnas[]={
						"servicio",
						"datos_servicio",
					};
			
			pst= con.prepareStatement(consultarServicioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1,codigoServicio);
			rs=pst.executeQuery();
			
			String keyValue = "";
		 	String regValue = "";
		    int i;
		    int k=0;
		    mapaRetorno.put("numRegistros", (new Integer(0)));
	    	do{
	    		for( i=0; i<columnas.length; i++){
	    		   keyValue = columnas[i]  +"_" +k ;
	    		   if(rs.getString(columnas[i])==null){
	    			   regValue = "";
	    		   }
		    	   else{
		    		   regValue = rs.getString(columnas[i]); 
		    	   }	          
	    		   if(regValue == null){
	    			   regValue = "";
	    		   }
	    		   mapaRetorno.put(keyValue, regValue); 
		        }
			    k  +=  1;
	    	}while(rs.next());
		    mapaRetorno.put("numRegistros", (new Integer(k)));			
			
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultarServicio",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultarServicio", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return mapaRetorno;
	}
	
	/**
	 * Método usado para insertar una excepcion de rips consultorios
	 * desde la valoración
	 * @param con
	 * @param numeroSolicitud
	 * @param institucion
	 * @param rips
	 * @return
	 */
	public static int insertarExcepcionRipsConsultorios(Connection con,int numeroSolicitud,int institucion,boolean rips)
	{
		PreparedStatement pst=null;
		try
		{
			pst= con.prepareStatement(insertarExcepcionRipsConsultoriosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1,numeroSolicitud);
			pst.setInt(2,institucion);
			pst.setBoolean(3,rips);
			return pst.executeUpdate();
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR insertarExcepcionRipsConsultorios",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR insertarExcepcionRipsConsultorios", e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Método para consultar la excepcion rips de un registro
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean consultarExcepcionRipsConsultorios(Connection con,int numeroSolicitud,int institucion)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			pst= con.prepareStatement(consultarExcepcionRipsConsultoriosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1,numeroSolicitud);
			pst.setInt(2,institucion);
			
			rs=pst.executeQuery();
			if(rs.next()){
				String valor=(rs.getString("rips")!=null)?rs.getString("rips"):"";
				if(valor.equals("true")||valor.equals("t")||valor.equals("1")){
					return true;
				}
			}
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultarExcepcionRipsConsultorios",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultarExcepcionRipsConsultorios", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return false;
	}
	
	/**
	 * Método para consultar los registros del RIPS por Rangos Paciente
	 * @param con
	 * @param idCuenta
	 *
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap consultaRegistrosRangosPaciente(Connection con,int idCuenta,String rips)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		HashMap mapaRetorno = new HashMap();
		try
		{
			//columnas del listado
			String[] columnas={
					"fecha",
					"paciente",
					"tipo_num_id",
					"servicio",
					"tipo",
					"solicitud",
					"cuenta",
					"codigo_paciente",
					"datos_servicio"
				};
			pst= con.prepareStatement(consultaRegistrosRangosPacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			
			pst.setInt(1,idCuenta);
			pst.setString(2,rips);
			pst.setString(3,rips);
			pst.setInt(4,idCuenta);
			pst.setString(5,rips);
			rs=pst.executeQuery();
			
			String keyValue = "";
		 	String regValue = "";
		    int i;
		    int k=0;
		    mapaRetorno.put("numRegistros", (new Integer(0)));
	    	do{
	    		for( i=0; i<columnas.length; i++){
	    		   keyValue = columnas[i]  +"_" +k ;
	    		   if(rs.getString(columnas[i])==null){
	    			   regValue = "";
	    		   }
		    	   else{
		    		   regValue = rs.getString(columnas[i]); 
		    	   }	          
	    		   if(regValue == null){
	    			   regValue = "";
	    		   }
	    		   mapaRetorno.put(keyValue, regValue); 
		        }
			    k  +=  1;
	    	}while(rs.next());
		    mapaRetorno.put("numRegistros", (new Integer(k)));			
			
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultaRegistrosRangosPaciente",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultaRegistrosRangosPaciente", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return mapaRetorno;
			
	}
	
	//************************************************************************************************************************
	//******************************MÉTODOS RIPS CAPITACIÓN*******************************************************************
	//************************************************************************************************************************
	
	/**
	 * Método que consulta de datos de la cuenta de cobro de capitacion
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap consultarDatosCxCCapitacion(Connection con,HashMap campos)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		PreparedStatement pst2=null;
		ResultSet rs2=null;
		HashMap mapaRetorno = new HashMap();
		HashMap mapaContratos = new HashMap();
		try
		{
			pst =  con.prepareStatement(consultarDatosCxCCapitacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, Integer.parseInt(campos.get("cuentaCobro").toString()));
			pst.setInt(2, Integer.parseInt(campos.get("institucion").toString()));
			
			rs=pst.executeQuery();
			int cont=0;
			mapaRetorno.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					String alias=rsm.getColumnLabel(i).toLowerCase();
					int index=alias.indexOf("_");
					while(index>0)
					{
						index=alias.indexOf("_");
						try{
							String caracter=alias.charAt(index+1)+"";
							{
								alias=alias.replaceFirst("_"+caracter, caracter.toUpperCase());
							}
						}
						catch(IndexOutOfBoundsException e)
						{
							break;
						}
					}
					mapaRetorno.put(alias, rs.getObject(rsm.getColumnLabel(i))==null?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapaRetorno.put("numRegistros", cont+"");
			
			pst2 =  con.prepareStatement(consultarContratosCxCCapitacionStr);
			pst2.setInt(1, Integer.parseInt(campos.get("cuentaCobro").toString()));
			pst2.setInt(2, Integer.parseInt(campos.get("institucion").toString()));
			rs2=pst2.executeQuery();
			
			int cont2=0;
			mapaContratos.put("numRegistros","0");
			ResultSetMetaData rsm2=rs2.getMetaData();
			while(rs2.next())
			{
				for(int i=1;i<=rsm2.getColumnCount();i++)
				{
					String alias=rsm2.getColumnLabel(i).toLowerCase();
					int index=alias.indexOf("_");
					while(index>0)
					{
						index=alias.indexOf("_");
						try{
							String caracter=alias.charAt(index+1)+"";
							{
								alias=alias.replaceFirst("_"+caracter, caracter.toUpperCase());
							}
						}
						catch(IndexOutOfBoundsException e)
						{
							break;
						}
					}
					mapaContratos.put(alias+"_"+cont2, rs2.getObject(rsm2.getColumnLabel(i))==null?"":rs2.getObject(rsm2.getColumnLabel(i)));
				}
				cont2++;
			}
			mapaContratos.put("numRegistros", cont2+"");
			mapaRetorno.put("contratos",mapaContratos);
			
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultarDatosCxCCapitacion",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultarDatosCxCCapitacion", e);
		}
		finally{
			try{
				if(rs2 != null){
					rs2.close();
				}
				if(rs != null){
					rs.close();
				}
				if(pst2 != null){
					pst2.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return mapaRetorno;
	}
	
	/**
	 * Método para consultar los RIPS del archivo AD en Capitacion
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap consultaCapitacionAD(Connection con,HashMap campos)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		HashMap mapaRetorno = new HashMap();
		try
		{
			String consulta = consultaAD_Capitacion_SELECT_Str + "'"+campos.get("numeroCuentaCobro")+"' AS consecutivo " +
				consultaAD_Capitacion_FROM_Str + consultaCapitacion_WHERE_Str + consultaAD_Capitacion_ORDER_Str;
			consulta= consulta.replace("REPLACE_CONTRATOS", UtilidadTexto.convertirVectorACodigosSeparadosXComas((Vector<Object>)campos.get("codigosContrato"), false));
			pst =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1,Integer.parseInt(campos.get("codigoConvenio").toString()));
			pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString()));
			pst.setString(3,UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString()));
			pst.setInt(4,Integer.parseInt(campos.get("institucion").toString()));
			rs=pst.executeQuery();
			
			int cont=0;
			mapaRetorno.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					String alias=rsm.getColumnLabel(i).toLowerCase();
					mapaRetorno.put(alias+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapaRetorno.put("numRegistros", cont+"");
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultaCapitacionAD",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultaCapitacionAD", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return mapaRetorno;
	}
	
	/**
	 * Método que consulta los RIPS del archivo US Capitacion
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap consultaCapitacionUS(Connection con,HashMap campos)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		HashMap mapaRetorno = new HashMap();
		try
		{
			String consulta = consultaUS_Capitacion_SELECT_Str + "'"+campos.get("numeroCuentaCobro")+"' AS consecutivo " + 
				consultaUS_Capitacion_FROM_Str + consultaCapitacion_WHERE_Str + consultaUS_Capitacion_ORDER_Str;
			consulta= consulta.replace("REPLACE_CONTRATOS", UtilidadTexto.convertirVectorACodigosSeparadosXComas((Vector<Object>)campos.get("codigosContrato"), false));

			pst =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1,Integer.parseInt(campos.get("codigoConvenio").toString()));
			pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString()));
			pst.setString(3,UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString()));
			pst.setInt(4,Integer.parseInt(campos.get("institucion").toString()));
			rs=pst.executeQuery();
			
			int cont=0;
			mapaRetorno.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					String alias=rsm.getColumnLabel(i).toLowerCase();
					mapaRetorno.put(alias+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapaRetorno.put("numRegistros", cont+"");
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultaCapitacionUS",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultaCapitacionUS", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return mapaRetorno;
	}
	
	/**
	 * Método que consulta los RIPS del archivo AC Capitacion
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap consultaCapitacionAC(Connection con,HashMap campos)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		HashMap mapaRetorno = new HashMap();
		try
		{
			String consulta = consultaAC_Capitacion_SELECT_Str + "'"+campos.get("numeroCuentaCobro")+"' AS consecutivo " +
				consultaAC_Capitacion_FROM_Str + consultaCapitacion_WHERE_Str + consultaCapitacion_ORDER_Str;
			consulta= consulta.replace("REPLACE_CONTRATOS", UtilidadTexto.convertirVectorACodigosSeparadosXComas((Vector<Object>)campos.get("codigosContrato"), false));
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			
			pst.setInt(1,Integer.parseInt(campos.get("tarifarioOficial").toString()));
			pst.setInt(2,Integer.parseInt(campos.get("codigoConvenio").toString()));
			pst.setString(3,UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString()));
			pst.setString(4,UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString()));
			pst.setInt(5,Integer.parseInt(campos.get("institucion").toString()));
			rs=pst.executeQuery();
			
			int cont=0;
			mapaRetorno.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					String alias=rsm.getColumnLabel(i).toLowerCase();
					mapaRetorno.put(alias+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapaRetorno.put("numRegistros", cont+"");
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultaCapitacionAC",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultaCapitacionAC", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return mapaRetorno;
	}
	
	/**
	 * Método que consulta los rips AP Capitacion
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap consultaCapitacionAP(Connection con,HashMap campos)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		HashMap mapaRetorno = new HashMap();
		try
		{
			String consulta = consultaAP_Capitacion_SELECT_Str + "'"+campos.get("numeroCuentaCobro")+"' AS consecutivo " +
				consultaAP_Capitacion_FROM_Str + consultaCapitacion_WHERE_Str + consultaCapitacion_ORDER_Str;
			consulta= consulta.replace("REPLACE_CONTRATOS", UtilidadTexto.convertirVectorACodigosSeparadosXComas((Vector<Object>)campos.get("codigosContrato"), false));
			
			pst =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1,Integer.parseInt(campos.get("tarifarioOficial").toString()));
			pst.setInt(2,Integer.parseInt(campos.get("codigoConvenio").toString()));
			pst.setString(3,UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString()));
			pst.setString(4,UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString()));
			pst.setInt(5,Integer.parseInt(campos.get("institucion").toString()));
			
			rs=pst.executeQuery();
			
			int cont=0;
			mapaRetorno.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					String alias=rsm.getColumnLabel(i).toLowerCase();
					mapaRetorno.put(alias+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapaRetorno.put("numRegistros", cont+"");
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultaCapitacionAP",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultaCapitacionAP", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return mapaRetorno;
	}
	
	/**
	 * Método que consulta los rips del archivo AM Capitacion
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap consultaCapitacionAM(Connection con,HashMap campos)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		HashMap mapaRetorno = new HashMap();
		try
		{
			String consulta = consultaAM_Capitacion_SELECT_Str + "'"+campos.get("numeroCuentaCobro")+"' AS consecutivo " +
				consultaAM_Capitacion_FROM_Str + 
				consultaAM_LEFTOUTER_Str + 
				consultaCapitacion_WHERE_Str +
				consultaCapitacion_ORDER_Str;
			
			consulta= consulta.replace("REPLACE_CONTRATOS", UtilidadTexto.convertirVectorACodigosSeparadosXComas((Vector<Object>)campos.get("codigosContrato"), false));
			
			pst =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1,Integer.parseInt(campos.get("codigoConvenio").toString()));
			pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString()));
			pst.setString(3,UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString()));
			pst.setInt(4,Integer.parseInt(campos.get("institucion").toString()));
			rs=pst.executeQuery();
			
			int cont=0;
			mapaRetorno.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					String alias=rsm.getColumnLabel(i).toLowerCase();
					mapaRetorno.put(alias+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapaRetorno.put("numRegistros", cont+"");
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultaCapitacionAM",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultaCapitacionAM", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return mapaRetorno;
	}
	
	/**
	 * Método que consulta los rips AT Capitacion
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap consultaCapitacionAT(Connection con,HashMap campos)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		HashMap mapaRetorno = new HashMap();
		try
		{
			String consulta = "SELECT * FROM (("+
				consultaAT_Articulo_Capitacion_SELECT_Str + "'"+campos.get("numeroCuentaCobro")+"' AS consecutivo " +
				consultaAT_Articulo_Capitacion_FROM_Str + consultaCapitacion_WHERE_Str +
				") UNION (" +
				consultaAT_Servicio_Capitacion_SELECT_Str + "'"+campos.get("numeroCuentaCobro")+"' AS consecutivo " +
				consultaAT_Servicio_Capitacion_FROM_Str + consultaCapitacion_WHERE_Str +
				")) g ORDER BY g.consecutivo_factura";
			
			consulta= consulta.replaceAll("REPLACE_CONTRATOS", UtilidadTexto.convertirVectorACodigosSeparadosXComas((Vector<Object>)campos.get("codigosContrato"), false));
			
			pst =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1,Integer.parseInt(campos.get("codigoConvenio").toString()));
			pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString()));
			pst.setString(3,UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString()));
			pst.setInt(4,Integer.parseInt(campos.get("institucion").toString()));
			pst.setInt(5,Integer.parseInt(campos.get("tarifarioOficial").toString()));
			pst.setInt(6,Integer.parseInt(campos.get("tarifarioOficial").toString()));
			pst.setInt(7,Integer.parseInt(campos.get("tarifarioOficial").toString()));
			pst.setInt(8,Integer.parseInt(campos.get("tarifarioOficial").toString()));
			pst.setInt(9,Integer.parseInt(campos.get("tarifarioOficial").toString()));
			pst.setInt(10,Integer.parseInt(campos.get("tarifarioOficial").toString()));
			pst.setInt(11,Integer.parseInt(campos.get("tarifarioOficial").toString()));
			pst.setInt(12,Integer.parseInt(campos.get("tarifarioOficial").toString()));
			pst.setInt(13,Integer.parseInt(campos.get("tarifarioOficial").toString()));
			pst.setInt(14,Integer.parseInt(campos.get("tarifarioOficial").toString()));
			pst.setInt(15,Integer.parseInt(campos.get("codigoConvenio").toString()));
			pst.setString(16,UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString()));
			pst.setString(17,UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString()));
			pst.setInt(18,Integer.parseInt(campos.get("institucion").toString()));
			rs=pst.executeQuery();
			
			int cont=0;
			mapaRetorno.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					String alias=rsm.getColumnLabel(i).toLowerCase();
					mapaRetorno.put(alias+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapaRetorno.put("numRegistros", cont+"");
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultaCapitacionAT",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultaCapitacionAT", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return mapaRetorno;
			
	}
	
	/**
	 * Método que consulta los rips de AU Capitacion
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap consultaCapitacionAU(Connection con,HashMap campos)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		HashMap mapaRetorno = new HashMap();
		try
		{
			String consulta = "SELECT * FROM "+ 
				"(( " +
					//Admisiones de Axioma Capitacion
					consultaAU_Capitacion_SELECT_Str +  
					"'"+campos.get("numeroCuentaCobro")+"' AS consecutivo " +
					consultaAU_Capitacion_FROM_Str + 
					consultaAU_Capitacion_WHERE_Str + 
					/*consultaCapitacion_ORDER_Str+*/
				") " +
				"UNION " +
				"("+
					//Admisiones de Entidades Subcontratadas Capitacion
					consultaAU_SubContratadas_Capitacion_SELECT_Str +
					"'"+campos.get("numeroCuentaCobro")+"' AS consecutivo " +
					consultaAU_Subcontratadas_Capitacion_FROM_Str +
					consultaAU_Capitacion_WHERE_Str +
					" AND f.pac_entidad_subcontratada IS NOT NULL "+
					/*consultaCapitacion_ORDER_Str*/
				")) t ORDER BY t.consecFactura";
			
			consulta= consulta.replaceAll("REPLACE_CONTRATOS", UtilidadTexto.convertirVectorACodigosSeparadosXComas((Vector<Object>)campos.get("codigosContrato"), false));
			
			pst =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1,Integer.parseInt(campos.get("codigoConvenio").toString()));
			pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString()));
			pst.setString(3,UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString()));
			pst.setInt(4,Integer.parseInt(campos.get("institucion").toString()));
			pst.setInt(5,Integer.parseInt(campos.get("codigoConvenio").toString()));
			pst.setString(6,UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString()));
			pst.setString(7,UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString()));
			pst.setInt(8,Integer.parseInt(campos.get("institucion").toString()));
			rs=pst.executeQuery();
			
			int cont=0;
			mapaRetorno.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					String alias=rsm.getColumnLabel(i).toLowerCase();
					mapaRetorno.put(alias+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapaRetorno.put("numRegistros", cont+"");
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultaCapitacionAT",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultaCapitacionAT", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return mapaRetorno;
			
	}
	
	/**
	 * Método implementado para consultar los rips AH Capitacion
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap consultaCapitacionAH(Connection con,HashMap campos)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		HashMap mapaRetorno = new HashMap();
		try
		{
			String consulta = consultaAH_Capitacion_SELECT_Str +  "'"+campos.get("numeroCuentaCobro")+"' AS consecutivo " +
				consultaAH_Capitacion_FROM_Str + consultaAH_Capitacion_WHERE_Str + consultaCapitacion_ORDER_Str;
			
			consulta= consulta.replaceAll("REPLACE_CONTRATOS", UtilidadTexto.convertirVectorACodigosSeparadosXComas((Vector<Object>)campos.get("codigosContrato"), false));
			
			pst =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1,Integer.parseInt(campos.get("codigoConvenio").toString()));
			pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString()));
			pst.setString(3,UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString()));
			pst.setInt(4,Integer.parseInt(campos.get("institucion").toString()));
			rs=pst.executeQuery();
			
			int cont=0;
			mapaRetorno.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					String alias=rsm.getColumnLabel(i).toLowerCase();
					mapaRetorno.put(alias+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapaRetorno.put("numRegistros", cont+"");
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultaCapitacionAH",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultaCapitacionAH", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return mapaRetorno;
			
	}
	
	/**
	 * Método que consulta los rips del archivo AN
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap consultaCapitacionAN(Connection con,HashMap campos)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		HashMap mapaRetorno = new HashMap();
		try
		{
			String consulta = "SELECT * FROM " +
				"((" + 
					//Registros normales de Axioma
					consultaAN_Capitacion_SELECT_Str +  "'"+campos.get("numeroCuentaCobro")+"' AS consecutivo " +
					consultaAN_Capitacion_FROM_Str + consultaAN_Capitacion_WHERE_Str + /*consultaCapitacion_ORDER_Str +*/
				") " +
				" UNION " +
				"(" +
					//Registros provenientes de entidades subcontratadas
					consultaAN_Capitacion_Subcontratadas_SELECT_Str +  "'"+campos.get("numeroCuentaCobro")+"' AS consecutivo " +
					consultaAN_Capitacion_Subcontratadas_FROM_Str + consultaAN_Capitacion_Subcontratadas_WHERE_Str + /*consultaCapitacion_ORDER_Str +*/
				")) t ORDER BY t.consecutivo_factura ";
			
			consulta= consulta.replaceAll("REPLACE_CONTRATOS", UtilidadTexto.convertirVectorACodigosSeparadosXComas((Vector<Object>)campos.get("codigosContrato"), false));
			
			pst =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1,Integer.parseInt(campos.get("codigoConvenio").toString()));
			pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString()));
			pst.setString(3,UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString()));
			pst.setInt(4,Integer.parseInt(campos.get("institucion").toString()));
			pst.setInt(5,Integer.parseInt(campos.get("codigoConvenio").toString()));
			pst.setString(6,UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString()));
			pst.setString(7,UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString()));
			pst.setInt(8,Integer.parseInt(campos.get("institucion").toString()));
			rs=pst.executeQuery();
			
			int cont=0;
			mapaRetorno.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					String alias=rsm.getColumnLabel(i).toLowerCase();
					mapaRetorno.put(alias+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapaRetorno.put("numRegistros", cont+"");
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultaCapitacionAN",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultaCapitacionAN", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return mapaRetorno;
			
	}
	
	/**
	 * Metodo de consulta del tipo de convenio de una factura consultada en la interfaz de ax_rips
	 * @param con
	 * @param nroFactura
	 * @return
	 */
	public static int consulartConvenioXNroFactura(Connection con,int nroFactura)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consulta = "SELECT " +
					" coalesce(c.tipo_codigo,"+ConstantesBD.codigoTarifarioCups+") as convenio" +
				" FROM" +
					" facturas f" +
				" INNER JOIN " +
					" convenios  c ON (c.codigo = f.convenio)" +
				" WHERE " +
					" f.consecutivo_factura = ? " ;

			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, nroFactura);
			
			rs = pst.executeQuery();
			if (rs.next())
			{
				return rs.getInt(1);
			}
			
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consulartConvenioXNroFactura",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consulartConvenioXNroFactura", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		//Si no encuentra nada por defecto es CUPS
		return ConstantesBD.codigoTarifarioCups;
	}
	
	/**
	 * Metodo de consulta del convenio de una factura
	 * @param con
	 * @param nroFactura
	 * @return
	 */
	public static int consultarConvenioFactura(Connection con,int nroFactura)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consulta = "SELECT " +
						" convenio " +
					" FROM" +
						" facturas " + 
					" WHERE " +
						" consecutivo_factura = ? ";
			pst =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, nroFactura);
			
			rs = pst.executeQuery();
			if (rs.next()){
				return rs.getInt(1);
			}
			
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultarConvenioFactura",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultarConvenioFactura", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * Consultar NOmbre del Tercero
	 * @param con
	 * @param nitTercero
	 * @return
	 */
	public static String consultarNombreTercero(Connection con, String nitTercero) 
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String cadenaStr2 = "SELECT em.razon_social as descripcion from empresas em INNER JOIN terceros ter ON(ter.codigo=em.tercero) where ter.numero_identificacion =? ";
			pst = con.prepareStatement(cadenaStr2, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setString(1, nitTercero);
			rs = pst.executeQuery();
			if(rs.next()){
				return (rs.getString("descripcion")!=null)?rs.getString("descripcion"):"";
			}
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultarNombreTercero",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultarNombreTercero", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return "";
	}

	/**
	 * Nit del Convenio 
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static String consultarNitConvenio(Connection con, int codigoConvenio) 
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String cadenaStr = "SELECT ter.numero_identificacion as nit from convenios con INNER JOIN empresas em ON(em.codigo=con.empresa) INNER JOIN terceros ter ON(ter.codigo=em.tercero) where con.codigo=?";
			pst = con.prepareStatement(cadenaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoConvenio);
			rs = pst.executeQuery();
			if(rs.next()){
				return (rs.getString("nit")!=null)?rs.getString("nit"):"";
			}
		}
		catch(SQLException sqe)
		{
			logger.error("############## SQLException ERROR consultarNitConvenio",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultarNitConvenio", e);
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
				logger.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		return "";
	}
	
	
	
}
