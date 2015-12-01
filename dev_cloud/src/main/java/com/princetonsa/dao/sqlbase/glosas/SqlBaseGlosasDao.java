package com.princetonsa.dao.sqlbase.glosas;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.LogsAxioma;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import com.princetonsa.dto.glosas.DtoConceptoGlosa;
import com.princetonsa.dto.glosas.DtoDetalleAsociosGlosa;
import com.princetonsa.dto.glosas.DtoDetalleFacturaGlosa;
import com.princetonsa.dto.glosas.DtoFacturaGlosa;
import com.princetonsa.dto.glosas.DtoGlosa;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.glosas.Glosas;

/**
 * Anexo 686 Registro Auditoría
 * Creado el 6 Enero de 2009
 * @author Felipe Pérez Granda - Sebastián Gómez
 * @mail lfperez@princetonsa.com - sgomez@princetonsa.com
 */

public class SqlBaseGlosasDao
{
	/* *******************
	 * Atributos de Logger
	 * *******************/
	public static Logger logger = Logger.getLogger(Glosas.class);
	
	/* ***********************
	 * Fin Atributos de Logger
	 * ***********************/
	
	/**
	 * Cadena de consulta facturas
	 */
	private static final String ConsultaFacturas = 
		"SELECT "+
			"f.consecutivo_factura as consecutivo_factura, "+
			"f.codigo as codigo, "+
			"f.valor_total as valor_total, "+
			"to_char(f.fecha,'"+ConstantesBD.formatoFechaAp+"') as fecha, "+
			"f.fecha as fecha_bd, "+
			"getnombreviaingreso(f.via_ingreso) as nombre_via_ingreso, "+
			"f.via_ingreso as codigo_via_ingreso, " +
			"getnombretipopacsegcuenta(f.cuenta) as tipo_paciente, "+
			"getnombrepersona(f.cod_paciente) as nombre_paciente, "+
			"getnombreconvenio(f.convenio) as nombre_convenio, "+
			"f.convenio as codigo_convenio, " +
			"sc.contrato as codigo_contrato, "+
			"coalesce(gl.observaciones,'') as observaciones, "+ 
			"gl.codigo as codigo_auditoria, "+
			"CASE WHEN agl.codigo_factura IS NULL THEN '"+ConstantesBD.acronimoNo+"' ELSE '"+ConstantesBD.acronimoSi+"' END as seleccionado, "+
			"CASE WHEN agl.codigo_factura IS NULL THEN '"+ConstantesBD.acronimoNo+"' ELSE '"+ConstantesBD.acronimoSi+"' END as auditada, " +
			"CASE WHEN f.tipo_factura_sistema = "+ValoresPorDefecto.getValorFalseParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS externa "+ 
		"FROM facturas f " +
		"INNER JOIN sub_cuentas sc ON(sc.sub_cuenta = f.sub_cuenta) "+ 
		"LEFT OUTER JOIN (" +
			"auditorias_glosas agl " +
			"INNER JOIN registro_glosas gl ON(gl.codigo = agl.glosa and gl.estado = '"+ConstantesIntegridadDominio.acronimoEstadoAuditado+"')" +
		") ON(agl.codigo_factura = f.codigo) ";
	
	
	/**
	 * Cadena para consultar el encabezado de la glosa
	 */
	private static final String consultarEncabezadoGlosaStr = "SELECT " +
			"rg.codigo as codigo, coalesce(rg.valor_glosa||'','') as valor_glosa, " +
			"coalesce(rg.glosa_sistema,'')  as glosa_sistema, " +
			"coalesce(to_char(rg.fecha_registro_glosa,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha_registro_glosa, " +
			"rg.convenio as codigo_convenio, " +
			"getnombreconvenio(rg.convenio) as nombre_convenio, " +
			"coalesce(rg.glosa_entidad,'') as glosa_entidad, " +
			"coalesce(to_char(rg.fecha_notificacion,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha_notificacion, " +
			"coalesce(rg.observaciones,'') as observaciones, " +
			"coalesce(rg.usuario_glosa,'') as usuario_glosa, " +
			"coalesce(rg.usuario_auditor,'') as usuario_auditor, " +
			"coalesce(to_char(rg.fecha_auditoria,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha_auditoria, " +
			"coalesce(rg.hora_auditoria,'') as hora_auditoria, " +
			"rg.estado AS estado, " +
			"to_char(rg.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') as fecha_modifica, " +
			"rg.hora_modifica as hora_modifica, " +
			"rg.usuario_modifica as usuario_modifica, " +
			"rg.institucion as codigo_institucion " +
		"FROM registro_glosas rg " +
		"WHERE rg.codigo = ? ";
	
	/**
	 * Cadena encargada de consultar la tabla auditorias_glosa
	 */
	private static final String consultarAuditoriasGlosa = 
		"SELECT " +
			"ag.codigo as codigo, " +
			"to_char (ag.fecha_modificacion, '"+ConstantesBD.formatoFechaAp+"') as fecha_modificacion, " +
			"ag.hora_modificacion as hora_modificacion, " +
			"ag.usuario_modificacion as usuario_modificacion, " +
			"ag.contrato as contrato, " +
			"coalesce (ag.valor_glosa_factura||'','') as valor_glosa_factura, " +
			"to_char (ag.fecha_elaboracion_fact, '"+ConstantesBD.formatoFechaAp+"') as fecha_elaboracion_fact, " +
			"coalesce (ag.numero_cuenta_cobro||'','') as numero_cuenta_cobro, " +
			"coalesce (ag.institucion,"+ConstantesBD.codigoNuncaValido+") as institucion, " +
			"coalesce (to_char(ag.fecha_radicacion_cxc, '"+ConstantesBD.formatoFechaAp+"'),'') as fecha_radicacion_cxc, " +
			"f.codigo as codigo_factura, " +
			"f.consecutivo_factura as consecutivo_factura, " +
			"f.valor_cartera - f.ajustes_credito + f.ajustes_debito as saldo_factura, " +
			"f.valor_total as total_factura, " +
			"f.valor_convenio as total_responsable, " +
			"f.valor_bruto_pac as total_bruto_paciente, " +
			"f.val_desc_pac AS total_descuentos, " +
			"f.valor_abonos as total_abonos, " +
			"f.valor_neto_paciente as total_neto_paciente " +
		"FROM auditorias_glosas ag " +
		"INNER JOIN facturas f ON (f.codigo = ag.codigo_factura) " +
		"INNER JOIN registro_glosas rg ON (rg.codigo = ag.glosa) ";
	
	/**
	 * Cadena encargada de consultar la tabla conceptos_audi_glosas
	 */
	private static final String consultarConceptosAudiGlosasStr = 
		"SELECT " +
			"cag.codigo as codigo, " +
			"cag.auditoria_glosa as auditoria_glosa, " +
			"cag.concepto_glosa as concepto_glosa, " +
			"cag.institucion as institucion, " +
			"cg.descripcion as descripcion," +
			"cg.tipo_concepto as tipo  " +
		"FROM conceptos_audi_glosas cag " +
		"INNER JOIN concepto_glosas cg ON (cag.concepto_glosa = cg.codigo AND cag.institucion = cg.institucion) " +
		"WHERE cag.auditoria_glosa = ? ";
	
	/**
	 * Cadena encargada de consultar la informacion de la tabla det_auditorias_glosas
	 */
	private static final String consultarDetalleAuditoriasGlosasStr =
		"SELECT " +
			"dag.codigo as codigo, " +
			"dag.auditoria_glosa as auditoria_glosa, " +
			"dag.det_factura_solicitud as det_factura_solicitud, " +
			"dag.solicitud as solicitud, " +
			"getconsecutivosolicitud(dag.solicitud) as consecutivo_solicitud, "+
			"CASE " +
				"WHEN dag.servicio IS NULL " +
			"THEN " +
				"getdescarticulo(dag.articulo) " +
			"ELSE " +
				"'(' || getcodigopropservicio2(dag.servicio,?) || ') ' || getnombreservicio(dag.servicio, "+ConstantesBD.codigoTarifarioCups+") " +
			"END as descripcion_articulo_servicio, " +
			"dag.cantidad_glosa as cantidad_glosa, " +
			"dag.valor_glosa as valor_glosa, " +
			"coalesce (dag.cantidad||'','') as cantidad, " +
			"coalesce (dag.valor||'','') as valor, " +
			"CASE " +
				"WHEN dag.servicio IS NULL " +
			"THEN " +
				"dag.articulo " +
			"ELSE " +
				"dag.servicio " +
			"END as codigo_articulo_servicio, " +
			"CASE " +
				"WHEN dag.servicio IS NULL " +
			"THEN " +
				"'"+ConstantesBD.acronimoSi+"' " +
			"ELSE " +
			"'"+ConstantesBD.acronimoNo+"'" +
			"END as es_servicio, " +
			"dfs.tipo_solicitud as codigo_tipo_solicitud, " +
			"getnomtiposolicitud(dfs.tipo_solicitud) as nombre_tipo_solicitud, " +
			"CASE WHEN dfs.articulo IS NULL THEN 'SERV' ELSE 'ARTI' END AS esarticulo " +
		"FROM det_auditorias_glosas dag " +
		"INNER JOIN det_factura_solicitud dfs on (dfs.codigo = dag.det_factura_solicitud) " +
		"WHERE dag.auditoria_glosa = ?";
	
	/**
	 * Cadena encargada de consultar los datos de la tabla conceptos_det_audi_glosas
	 */
	private static final String consultarDetalleConceptosAuditoriasGlosasStr =
		"SELECT " +
			"cdag.codigo as codigo, " +
			"cdag.det_audi_fact as det_audi_fact, " +
			"cdag.concepto_glosa as concepto_glosa, " +
			"cdag.institucion as institucion, " +
			"cg.descripcion as descripcion, " +
			"cg.tipo_concepto as tipo " +
		"FROM conceptos_det_audi_glosas cdag " +
		"INNER JOIN concepto_glosas cg ON (cdag.concepto_glosa = cg.codigo AND cdag.institucion = cg.institucion) " +
		"WHERE cdag.det_audi_fact = ?";

	/**
	 * Cadena encargada de consultar si el detalle auditoria glosa ya tiene un concepto asignado
	 */
	private static final String consultarConceptoDetalleAuditoriasGlosasStr =
		"SELECT " +
			"cdag.codigo as codigo, " +
			"cdag.det_audi_fact as det_audi_fact " +
		"FROM conceptos_det_audi_glosas cdag " +
		"WHERE cdag.det_audi_fact = ?";

	/**
	 * Cadena encargada de consultar los datos de la tabla asocios_auditorias_glosas
	 */
	private static final String consultarAsociosAuditoriasGlosasStr =
		"SELECT " +
			"aag.codigo as codigo, " +
			"aag.asocio_det_factura as asocio_det_factura, " +
			"aag.det_auditoria_glosa as det_auditoria_glosa, " +
			"aag.tipo_asocio as tipo_asocio, " +
			"ta.nombre_asocio as nombre_asocio, " +
			"coalesce (aag.codigo_medico||'','') as codigo_medico, " +
			"getnombrepersona(aag.codigo_medico) as nombre_medico, " +
			"coalesce(aag.cantidad_glosa||'','') as cantidad_glosa, " +
			"coalesce(aag.valor_glosa||'','') as valor_glosa, " +
			"aag.servicio_asocio as servicio_asocio, " +
			"getcodigopropservicio2(aag.servicio_asocio,?) as codigo_prop_servicio_asocio, " +
			"coalesce(aag.cantidad||'','') as cantidad, " +
			"coalesce(aag.valor||'','') as valor " +
		"FROM asocios_auditorias_glosas aag " +
		"INNER JOIN tipos_asocio ta ON (aag.tipo_asocio = ta.codigo)" +
		"WHERE aag.det_auditoria_glosa = ?";
	
	private static final String consultarConceptosAsociosAudiGlosasStr =
		"SELECT " +
			"caag.codigo as codigo, " +
			"caag.asocio_auditoria_glosa as asocio_auditoria_glosa, " +
			"caag.concepto_glosa as concepto_glosa, " +
			"caag.institucion as institucion, " +
			"cg.descripcion as descripcion, " +
			"cg.tipo_concepto as tipo " +
			"FROM conceptos_aso_audi_glosas caag " +
		"INNER JOIN concepto_glosas cg ON (caag.concepto_glosa = cg.codigo AND caag.institucion = cg.institucion) " +
		"WHERE caag.asocio_auditoria_glosa = ?";
	
	/**
	 * Cadena para la inserción del encabezado de la glosa
	 */
	private static final String insertarEncabezadoGlosaStr = "INSERT INTO registro_glosas " +
		"(codigo," + //1
		"valor_glosa," + //2
		"glosa_sistema," + //3
		"fecha_registro_glosa," + //4
		"convenio," + //5
		"glosa_entidad," + //6
		"fecha_notificacion," + //7
		"observaciones," + //8
		"usuario_glosa," + //9
		"usuario_auditor," + //10
		"fecha_auditoria," + //11
		"hora_auditoria," + //12
		"estado," + //13
		"fecha_modifica," + //14
		"hora_modifica," + //15
		"usuario_modifica," + //16
		"institucion," +
		"contrato) " + //17
		"values " +
		"(?,?,?,?,?,?,?,?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?,?)";
	
	/**
	 * Cadena para la modificacion del encabezado de la glosa
	 */
	private static final String modificarEncabezadoGlosaStr = "UPDATE registro_glosas SET " +
		"valor_glosa = ?, " + // 1
		"glosa_sistema = ?, " + //2
		"fecha_registro_glosa = ?, " + //3
		"convenio = ?, " + //4
		"glosa_entidad = ?, " + //5
		"fecha_notificacion = ?, " + //6
		"observaciones = ?, " + //7
		"usuario_glosa = ?, " + //8
		"usuario_auditor = ?, " + //9
		"fecha_auditoria = ?, " + //10
		"hora_auditoria = ?, " + //11
		"estado = ?, " + //12
		"fecha_modifica = CURRENT_DATE, " +
		"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
		"usuario_modifica =?, " + //13
		"institucion = ? " + //14
		"WHERE codigo = ?"; //15
	
	private static final String insertarDetalleGlosaStr = "INSERT INTO auditorias_glosas " +
			"(codigo, " + //1
			"codigo_factura, " +//2
			"fecha_modificacion, " +
			"hora_modificacion, " +
			"usuario_modificacion, " +//3
			"contrato, " +//4
			"valor_glosa_factura, " +//5
			"saldo_factura, " +//6
			"fecha_elaboracion_fact, " +//7
			"numero_cuenta_cobro, " +//8
			"institucion, " +//9
			"fecha_radicacion_cxc, " +//10
			"glosa, " +
			"fue_auditada) " +//11
			"VALUES (?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?,?,?,?,?,?,?,?,'PG')";
	
	/**
	 * Cadena que modifica el detalle de una glosa
	 */
	private static final String modificarDetalleGlosaStr = "UPDATE auditorias_glosas SET " +
		"fecha_modificacion = CURRENT_DATE," +
		"hora_modificacion = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
		"usuario_modificacion = ?, " + //1
		"contrato = ?, " + //2
		"valor_glosa_factura = ?, " + //3
		"saldo_factura = ?, " + //4
		"fecha_elaboracion_fact = ?, " + //5
		"numero_cuenta_cobro = ?,  " + //6
		"institucion = ?, " + //7
		"fecha_radicacion_cxc = ?, " +
		"fue_auditada =  'PG'" + //8
		"WHERE codigo = ?"; //9
	
	
	/**
	 * Cadena para insertar el concepto de una glosa por factura
	 */
	private static final String insertarConceptoFacturaStr = "INSERT INTO conceptos_audi_glosas (" +
		"codigo," + //1
		"auditoria_glosa," + //2
		"concepto_glosa," + //3
		"institucion) " + //4
		"values (?,?,?,?)";	
	
	/**
	 * Cadena para insertar el concepto de una glosa por factura
	 */
	private static final String modificarConceptoFacturaStr = "UPDATE conceptos_audi_glosas " +
		"SET concepto_glosa=? " + //3
		"WHERE auditoria_glosa=?";
	
	/**
	 * Cadena para eliminar el concepto de una glosa por factura
	 */
	private static final String eliminarConceptoFacturaStr = "DELETE FROM conceptos_audi_glosas WHERE codigo = ?";
	
	//*****************************************LIMITE CADENAS STRING***************************************************************
	
	/**
	 * Cadena para insertar el detalle de la factura de una glosa
	 */
	private static final String insertarDetalleFacturaGlosaStr = "INSERT INTO det_auditorias_glosas (" +
		"codigo," + //1
		"auditoria_glosa," + //2
		"det_factura_solicitud," +  //3
		"solicitud," + //4
		"servicio," + //5
		"articulo," + //6
		"cantidad_glosa," + //7
		"valor_glosa," + //8
		"cantidad,valor" + //9
		") " +
		"VALUES " +
		"(?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Cadena para modificar el detalle dela factura de una glosa
	 */
	private static final String modificarDetalleFacturaGlosaStr = "UPDATE det_auditorias_glosas SET cantidad_glosa = ? ,valor_glosa = ?, cantidad = ?, valor = ? WHERE codigo = ?";
	
	/**
	 * Cadena para eliminar el detalle de la factura de una glosa
	 */
	private static final String eliminarDetalleFacturaGlosaStr = "DELETE FROM det_auditorias_glosas  WHERE codigo = ? ";
	
	/**
	 * Cadena para eliminar los conceptos del detalle de la factura de una glosa
	 */
	private static final String eliminarConceptosDetalleFacturaGlosaStr = "DELETE FROM conceptos_det_audi_glosas WHERE ";
	
	
	/**
	 * 
	 * Cadena para eliminar un registro de un asocio que está relacionado al detalle de una factura
	 */
	private static final String eliminarAsocioDetalleFacturaGlosaStr = "DELETE FROM asocios_auditorias_glosas WHERE codigo = ?";
	
	/**
	 * Cadena para eliminar un registro de un concepto relacinado a un asocio de un detalle de una factura de una glosa
	 */
	private static final String eliminarConceptosAsocioDetalleFacturaGlosaStr = "DELETE FROM conceptos_aso_audi_glosas WHERE ";
	
	
	/**
	 * Cadena para insertar el concepto del detalle de la factura servicio articulo
	 */
	private static final String insertarConceptoDetalleFacturaServicioArticuloStr = "INSERT INTO conceptos_det_audi_glosas (codigo,det_audi_fact,concepto_glosa,institucion) VALUES (?,?,?,?)";
	
	/**
	 * Cadena para insertar el concepto del detalle de la factura servicio articulo
	 */
	private static final String modificarConceptoDetalleFacturaServicioArticuloStr = "UPDATE conceptos_det_audi_glosas SET concepto_glosa=?,institucion=? WHERE det_audi_fact=? ";
	
	/**
	 * Cadena que elimina un concepto del detalle de la factura servicio articulo
	 */
	private static final String eliminarConceptoDetalleFacturaServicioArticuloStr = "DELETE FROM conceptos_det_audi_glosas WHERE codigo = ?";
	
	/**
	 * Cadena para insertar un detalle de asocio de la factura glosa
	 */
	private static final String insertarAsociosDetalleFacturaGlosaStr = "INSERT INTO asocios_auditorias_glosas (" +
		"codigo," + //1
		"asocio_det_factura," + //2
		"det_auditoria_glosa," + //3
		"tipo_asocio," + //4
		"codigo_medico," + //5
		"cantidad_glosa," + //6
		"valor_glosa," + //7
		"servicio_asocio," + //8
		"cantidad," + //9
		"valor" + //10
		") VALUES (?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Cadena para modificar un detalle de asocio de la factura glosa
	 */
	private static final String modificarAsociosDetalleFacturaGlosaStr = "UPDATE asocios_auditorias_glosas SET cantidad_glosa = ?, valor_glosa = ?, cantidad = ?, valor = ? WHERE codigo = ?";
	
	/**
	 * Cadena para insertar un concepto del detalle asocio factura de la glosa
	 */
	private static final String insertarConceptoDetalleAsocioFacturaGlosaStr = "INSERT INTO conceptos_aso_audi_glosas (codigo,asocio_auditoria_glosa,concepto_glosa,institucion) VALUES (?,?,?,?)";
	
	/**
	 * Cadena para eliminar un concepto del detalle asocio factura de la glosa
	 */
	private static final String eliminarConceptoDetalleAsocioFacturaGlosaStr = "DELETE FROM conceptos_aso_audi_glosas WHERE codigo = ?";
	
	
	/**
	 * Cadena que carga todo el detalle de la factura teniendo en cuenta que si hay cargos que ya están asociados a la
	 * glsoa de la factura se toma los valores respectivos de la glosa
	 */
	private static final String cargarDetalleFacturaGlosaDevolucionStr = "SELECT "+ 
		"coalesce(dag.codigo||'','') as codigo, "+
		"coalesce(dag.auditoria_glosa||'','') as auditoria_glosa, "+
		"dfs.codigo as det_factura_solicitud, "+
		"dfs.solicitud as solicitud, "+
		"getconsecutivosolicitud(dfs.solicitud) as consecutivo_solicitud, "+
		"CASE WHEN dfs.servicio IS NULL THEN getdescarticulo(dfs.articulo) ELSE '(' || getcodigopropservicio2(dfs.servicio,?) || ') ' || getnombreservicio(dfs.servicio, "+ConstantesBD.codigoTarifarioCups+") END as descripcion_articulo_servicio, "+ 
		"coalesce(dag.cantidad_glosa,0) as cantidad_glosa, "+ 
		"coalesce(dag.valor_glosa||'','') as valor_glosa, "+ 
		"dfs.cantidad_cargo as cantidad, "+ 
		"dfs.valor_total + dfs.ajustes_debito - dfs.ajustes_credito as valor, "+ 
		"CASE WHEN dfs.servicio IS NULL THEN dfs.articulo ELSE dfs.servicio END as codigo_articulo_servicio, "+ 
		"CASE WHEN dfs.servicio IS NULL THEN '"+ConstantesBD.acronimoNo+"' ELSE '"+ConstantesBD.acronimoSi+"' END as es_servicio, " +
		"dfs.tipo_solicitud as codigo_tipo_solicitud," +
		"getnomtiposolicitud(dfs.tipo_solicitud) as nombre_tipo_solicitud "+ 
		"FROM det_factura_solicitud dfs "+ 
		"LEFT OUTER JOIN det_auditorias_glosas dag ON(dag.det_factura_solicitud = dfs.codigo and dag.auditoria_glosa = ?) "+ 
		"WHERE "+ 
		"dfs.factura = ?";
	
	/**
	 * Cadena que carga los asocios de un servicio del detalle de una factura
	 */
	private static final String cargarAsociosDetalleFacturaStr = "SELECT "+ 
		"adf.consecutivo AS asocio_det_factura, "+ 
		"adf.tipo_asocio as tipo_asocio, "+ 
		"ta.nombre_asocio as nombre_asocio, "+ 
		//mientras se actualizan los historicos del medico del asocio de las facturas entonces dejamos el coalesce
		//segun determinacion gerencia esto se va ha actualizar luego, el medico_asocio debería ser not null......
		"coalesce (adf.medico_asocio,adf.codigo_medico) as codigo_medico, "+ 
		"coalesce(getnombrepersona(adf.medico_asocio),getnombrepersona(adf.codigo_medico)) as nombre_medico, "+ 
		"adf.servicio_asocio as servicio_asocio," +
		"getcodigopropservicio2(adf.servicio_asocio,?) AS codigo_prop_servicio_asocio, "+ 
		"1 as cantidad, "+ 
		"adf.valor_total + adf.ajustes_debito - adf.ajustes_credito as valor "+ 
		"FROM asocios_det_factura adf "+ 
		"INNER JOIN tipos_asocio ta ON (adf.tipo_asocio = ta.codigo) "+ 
		"WHERE adf.codigo = ?";
	
	
	/**
	 * Cadena para cargar las solicitudes de la factura
	 */
	private static final String cargarSolicitudesFacturaStr = "SELECT "+ 
		"dfs.codigo as det_factura_solicitud, "+
		"dfs.solicitud as solicitud, "+
		"getconsecutivosolicitud(dfs.solicitud) as consecutivo_solicitud, "+
		"to_char(getfechasolicitud(dfs.solicitud),'"+ConstantesBD.formatoFechaAp+"') as fecha_solicitud, " +
		"dfs.tipo_solicitud as codigo_tipo_solicitud, " +
		"getnomtiposolicitud(dfs.tipo_solicitud) as nombre_tipo_solicitud, " +
		"CASE WHEN dfs.servicio IS NULL THEN '(' || inventarios.getCodArticuloAxiomaInterfaz(dfs.articulo,?) || ')' ELSE '(' || getcodigopropservicio2(dfs.servicio,?) || ') ' END as codigo_articulo_servicio,  "+
		"CASE WHEN dfs.servicio IS NULL THEN getdescripcionarticulo(dfs.articulo) ELSE getnombreservicio(dfs.servicio, "+ConstantesBD.codigoTarifarioCups+") END as descripcion_articulo_servicio, " +
		"CASE WHEN dfs.servicio IS NULL THEN dfs.articulo || '' ELSE getcodigopropservicio2(dfs.servicio,?) END as codigo_articulo_servicio, "+
		"CASE WHEN dfs.servicio IS NULL THEN dfs.articulo ELSE dfs.servicio END as consecutivo_articulo_servicio, "+
		"dfs.cantidad_cargo as cantidad, "+ 
		"dfs.valor_total + coalesce(dfs.ajustes_debito,0) - coalesce(dfs.ajustes_credito, 0) as valor," +
		"'"+ConstantesBD.acronimoNo+"' as seleccionado, " +
		"dfs.servicio AS serv, " +
		"dfs.articulo AS arti " +
		"FROM det_factura_solicitud dfs " +
		"LEFT OUTER JOIN articulo a ON(a.codigo=dfs.articulo) "+ 
		"WHERE "+ 
		"dfs.factura = ? " +
		"and (dfs.valor_total + coalesce(dfs.ajustes_debito,0) - coalesce(dfs.ajustes_credito, 0))>0 ";
	
	//**************************************************************************************************************************
	//*****************************CONSULTA FACTURAS AUDITADAS*********************************************************************
	//*****************************************************************************************************************************
	
	/**
	 * Cadena para consulta de facturas auditadas
	 */
	private static final String consultaFacturasAuditadasStr = "SELECT "+ 
		"rg.codigo as codigo_glosa, "+
		"rg.fecha_auditoria as fecha_auditoria_bd, "+
		"to_char(rg.fecha_auditoria,'"+ConstantesBD.formatoFechaAp+"') as fecha_auditoria, "+
		"'Convenio: '|| c.nombre as nombre_convenio, " +
		"c.codigo as codigo_convenio, "+
		"coalesce(rg.glosa_sistema,'') as pre_glosa, "+
		"coalesce(rg.valor_glosa||'','') as valor_pre_glosa, "+
		"ag.fecha_elaboracion_fact as fecha_elaboracion_bd, "+
		"to_char(ag.fecha_elaboracion_fact,'"+ConstantesBD.formatoFechaAp+"') as fecha_elaboracion, "+
		"f.consecutivo_factura as consecutivo_factura, "+
		"getnombrepersona(f.cod_paciente) as nombre_paciente, "+
		"f.valor_total as valor_total, " +
		"f.estado_facturacion as estadofactura "+
		"FROM registro_glosas rg "+ 
		"INNER JOIN auditorias_glosas ag ON(ag.glosa=rg.codigo) "+ 
		"INNER JOIN facturas f ON(f.codigo = ag.codigo_factura) " +
		"INNER JOIN convenios c ON(c.codigo=rg.convenio) "+ 
		"WHERE ";
	
	
	/**
	 * Método encargado de consultar las facturas
	 * @author Felipe Pérez Granda - Sebastián Gómez
	 * @param connection
	 * @param criterios
	 * @return HashMap
	 */
	public static HashMap<String, Object> consultarFacturas (Connection connection, HashMap<String, Object> criterios)
	{
		String consulta = ConsultaFacturas;
		consulta += obtenerWhere(criterios);
		
		consulta += " AND f.numero_cuenta_cobro IS NULL AND f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada;
		consulta += " ORDER BY f.consecutivo_factura";
		logger.info("===> La cadena a consultar es: "+consulta);
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(connection.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
	        ps.close();
			return mapaRetorno;	
		}
		catch (SQLException e)
		{
			logger.error("===> Problema consultando datos en Glosas "+e);
		}
		return null;
	}
	
	/**
	 * Método encargado de obtener el where de la consulta
	 * @author Felipe Pérez Granda - Sebastián Gómez
	 * @param criterios
	 * @return String
	 */
	public static String obtenerWhere (HashMap<String, Object> criterios)
	{
		String where = "";
		
		if(!criterios.get("facturaInicial").toString().equals("") &&
			!criterios.get("facturaFinal").toString().equals(""))
		{
			where +=  "(f.consecutivo_factura between "+criterios.get("facturaInicial")+
				" AND "+criterios.get("facturaFinal")+") ";
		}
		
		if(!criterios.get("fechaElaboracionInicial").toString().equals("") &&
			!criterios.get("fechaElaboracionFinal").toString().equals(""))
		{
			where += (where.equals("")?"":" AND ") +
				"f.fecha between '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaElaboracionInicial").toString())+
				"' and '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaElaboracionFinal").toString())+"'  ";
		}
		
		/*Modificacion tarea 87586 - Si el usuario es auditor, y se seleccionan TODOS en Convenios,
		 * es necesario realizar la búsqueda de las facturas con respecto a todos los convenios que 
		 * tiene asociados el usuario. Si el usuario no es auditor, la búsqueda se realiza normalmente 
		*/	
		if(criterios.containsKey("numConvenios"))
		{
			where += (where.equals("")?"":" AND ");
			where += ("(");
			for(int i=0;i<Utilidades.convertirAEntero(criterios.get("numConvenios")+"");i++)
			{
				where += " f.convenio="+criterios.get("convenio_"+i)+" ";
				if ((i+1)<Utilidades.convertirAEntero(criterios.get("numConvenios")+""))
					where += " OR ";
			}
			where += ") ";
			
			Utilidades.imprimirMapa(criterios);
		}
		//Fin modificacion tarea 87586
		
		/*El siguiente if fue modificado, tiene el ELSE por si no contiene la llave de numConvenios
		 * Es decir, si el usuario no es Auditor
		 */
		else 
		{
			if (Utilidades.convertirAEntero(criterios.get("convenio")+"")==-1)
				where += (where.equals("")?"":" AND ") +"f.convenio != "+criterios.get("convenio");
			else
				where += (where.equals("")?"":" AND ") +"f.convenio = "+criterios.get("convenio");
		}
		
		if(criterios.get("viaIngreso").equals("-1"))
		{
			criterios.put("viaIngreso", "");
		}
		if(!criterios.get("viaIngreso").toString().equals(""))
		{
			where += (where.equals("")?"":" AND ") +" f.via_ingreso || '"+ConstantesBD.separadorSplit+"' || gettipopacsegcuenta(f.cuenta) = '"+criterios.get("viaIngreso")+"' ";
		}
		
		if(Utilidades.convertirAEntero(criterios.get("estadoPaciente").toString())>0)
		{
			where += (where.equals("")?"":" AND ") +"f.estado_paciente = "+criterios.get("estadoPaciente");
		}
		
		if(UtilidadTexto.getBoolean(criterios.get("estadoAuditoria").toString()))
			where += (where.equals("")?"":" AND ") + " f.codigo IN (SELECT ag.codigo_factura from registro_glosas g inner join auditorias_glosas ag ON(ag.glosa = g.codigo) WHERE ag.codigo_factura = f.codigo AND g.estado = '"+ConstantesIntegridadDominio.acronimoEstadoAuditado+"')  ";
		else
			where += (where.equals("")?"":" AND ") + " f.codigo NOT IN (SELECT ag.codigo_factura from registro_glosas g inner join auditorias_glosas ag ON(ag.glosa = g.codigo) WHERE ag.codigo_factura = f.codigo)  ";
			
		where = " WHERE " + where;
		return where;
	}
	
	/**
	 * Método que realiza la inserción de la glosa
	 * @param con
	 * @param glosa
	 * @return
	 */
	public static ResultadoBoolean guardarGlosa(Connection con,DtoGlosa glosa, String remite)
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true,"");
		
		//****************GUARDAR ENCABEZADO DE LA GLOSA*******************************************
		if(!remite.equals("RegistrarModificarGlosa"))
			resultado = guardarEncabezadoGlosa(con,glosa);
		//******************************************************************************************
		
		
		//Preguntamos si todo hasta ahora va bien
		if(resultado.isTrue()&&glosa.getFacturas().size()>0)
			//************SE GUARDA EL DETALLE DE LA GLOSA*********************************************
			resultado = guardarDetalleGlosa(con,glosa, remite);
			//*************************************************************************************
			
		
		return resultado;
	}

	/**
	 * Método usado para guardar el detalle de una glosa
	 * @param con
	 * @param glosa
	 * @return
	 */
	private static ResultadoBoolean guardarDetalleGlosa(Connection con,
			DtoGlosa glosa, String remite)
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true,"");
		int consecutivo=ConstantesBD.codigoNuncaValido;
		try
		{
			for(int i=0;i<glosa.getFacturas().size();i++)
			{
				DtoFacturaGlosa factura = glosa.getFacturas().get(i);
				
				if(resultado.isTrue())
				{
					
					if(factura.getCodigo().equals(""))
					{
						//************INSERCION DETALLE GLOSA**************************************
						PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(insertarDetalleGlosaStr, ConstantesBD.typeResultSet, 
							ConstantesBD.concurrencyResultSet ));
						
						consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_auditorias_glosas");
						pst.setInt(1, consecutivo);
						pst.setInt(2, Utilidades.convertirAEntero(factura.getCodigoFactura()));
						pst.setString(3, factura.getUsuarioModificacion().getLoginUsuario());
						pst.setInt(4, factura.getCodigoContrato());
						
						if(!factura.getValorGlosaFacturaStr().equals(""))
							pst.setDouble(5, Double.parseDouble(factura.getValorGlosaFacturaStr()));
						else
							pst.setNull(5, Types.NUMERIC);
						
						if(!factura.getSaldoFacturaStr().equals(""))
							pst.setDouble(6, Double.parseDouble(factura.getSaldoFacturaStr()));
						else
							pst.setNull(6, Types.NUMERIC);
						
						if(!factura.getFechaElaboracionFactura().equals(""))
							 pst.setDate(7, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(factura.getFechaElaboracionFactura())));
						else
							pst.setNull(7, Types.DATE);
						
						if(!factura.getNumeroCuentaCobro().equals(""))
							pst.setInt(8, Utilidades.convertirAEntero(factura.getNumeroCuentaCobro()));
						else
							pst.setNull(8, Types.INTEGER);
						
						if(factura.getInstitucion()>0)
							pst.setInt(9, factura.getInstitucion());
						else
							pst.setNull(9, Types.INTEGER);
						
						if(!factura.getFechaRadicacion().equals(""))
							 pst.setDate(10, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(factura.getFechaRadicacion())));
						else
							pst.setNull(10, Types.DATE);
						
						pst.setInt(11, Utilidades.convertirAEntero(glosa.getCodigo()));
						
						if(pst.executeUpdate()<=0)
						{
							resultado.setResultado(false);
							resultado.setDescripcion("Error ingresando el detalle de la glosa.");
							logger.info("===> Error ingresando el detalle de la glosa.");
						}
						else
						{
							resultado.setDescripcion(consecutivo+"");
							factura.setCodigo(consecutivo+"");
						}
						
						logger.info("===> ");
						logger.info("===> Vamos a actualizar la tabla de facturas");
						
											
						//**************************************************************************
					}
					else if(!remite.equals("RegistrarModificarGlosa"))
					{
						//***********MODIFICACION DETALLE GLOSA*************************************
						PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(modificarDetalleGlosaStr, ConstantesBD.typeResultSet, 
							ConstantesBD.concurrencyResultSet ));
						pst.setString(1,factura.getUsuarioModificacion().getLoginUsuario());
						pst.setInt(2,factura.getCodigoContrato());
						if(!factura.getValorGlosaFacturaStr().equals(""))
							pst.setDouble(3,Double.parseDouble(factura.getValorGlosaFacturaStr()));
						else
							pst.setNull(3,Types.NUMERIC);
						
						if(!factura.getSaldoFacturaStr().equals(""))
							pst.setDouble(4, Double.parseDouble(factura.getSaldoFacturaStr()));
						else
							pst.setNull(4, Types.NUMERIC);
						
						if(!factura.getFechaElaboracionFactura().equals(""))
							 pst.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(factura.getFechaElaboracionFactura())));
						else
							pst.setNull(5, Types.DATE);
						
						if(!factura.getNumeroCuentaCobro().equals(""))
							pst.setInt(6, Utilidades.convertirAEntero(factura.getNumeroCuentaCobro()));
						else
							pst.setNull(6, Types.INTEGER);
						
						if(factura.getInstitucion()>0)
							pst.setInt(7, factura.getInstitucion());
						else
							pst.setNull(7, Types.INTEGER);
						
						if(!factura.getFechaRadicacion().equals(""))
							 pst.setDate(8, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(factura.getFechaRadicacion())));
						else
							pst.setNull(8, Types.DATE);
						
						pst.setInt(9,Utilidades.convertirAEntero(factura.getCodigo()));
						
						if(pst.executeUpdate()<=0)
						{
							resultado.setResultado(false);
							resultado.setDescripcion("Error modificando el detalle de la glosa.");
						}
						else
						{
							resultado.setDescripcion(factura.getCodigo());
						}
						
						//**************************************************************************
					}
					
					//Se verifica si todo va bien
					if(resultado.isTrue())
					{
						//***************GUARDAR LOS CONCEPTOS DE LA FACTURA*********************************
						resultado = guardarConceptosFactura(con,factura,remite);
						//***********************************************************************************
					}
					
					
					//Se verifica si todo va bien
					if(resultado.isTrue())
					{
						//**********GUARDAR DETALLE FACTURA*************************************
						resultado = guardarDetalleFacturaGlosa(con,factura);
						//**********************************************************************
					}
				}
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en guardarDetalleGlosa: "+e);
			resultado.setResultado(false);
			resultado.setDescripcion("Ocurrió error tratando de guardar el detalle de la glosa: "+e);
		}
		return resultado;
	}

	

	/**
	 * Método para guardar los conceptops de la factura
	 * @param con
	 * @param factura
	 * @return
	 */
	private static ResultadoBoolean guardarConceptosFactura(Connection con,DtoFacturaGlosa factura,String remite) 
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true,"");
		try
		{
			logger.info("*************CONCEPTOS DE LA FACTURA*******************");
			for(DtoConceptoGlosa concepto:factura.getConceptos())
			{
				logger.info("consecutivo concepto: "+concepto.getCodigo());
				logger.info("codigo concepto: "+concepto.getCodigoConcepto());
				logger.info("resultado: "+resultado.isTrue());
				logger.info("¿es eliminado?: "+concepto.isEliminado());
				if(resultado.isTrue())
				{
					//Se verifica si ya existe el concepto
					if(concepto.getCodigo().equals(""))
					{
						if(!concepto.getCodigoConcepto().equals(""))
						{
							//*********************INGRESAR CONCEPTO DE GLOSA DE LA FACTURA***********************************
							PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(insertarConceptoFacturaStr, ConstantesBD.typeResultSet, 
								ConstantesBD.concurrencyResultSet ));
							int consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_conceptos_audi_glosas");
							pst.setInt(1,consecutivo);
							pst.setInt(2,Utilidades.convertirAEntero(factura.getCodigo()));
							pst.setString(3,concepto.getCodigoConcepto());
							pst.setInt(4,concepto.getCodigoInstitucion());
							
							if(pst.executeUpdate()<=0)
							{
								resultado.setResultado(false);
								resultado.setDescripcion("Error ingresando el concepto glosa de la factura "+factura.getConsecutivoFactura()+".");
							}
							else
							{
								resultado.setDescripcion(consecutivo+"");
								concepto.setCodigo(consecutivo+"");
								concepto.setCodigoFacturaGlosa(factura.getCodigo());
							}				
						//*************************************************************************
						}
						
					}
					else if(concepto.isEliminado())
					{
						//******************ELIMINAR CONCEPTO DE GLOSA DE LA FACTURA***********************************************
						PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarConceptoFacturaStr, ConstantesBD.typeResultSet, 
							ConstantesBD.concurrencyResultSet ));
						pst.setInt(1,Utilidades.convertirAEntero(concepto.getCodigo()));
						
						
						if(pst.executeUpdate()<=0)
						{
							resultado.setResultado(false);
							resultado.setDescripcion("Error eliminando el concepto de glosa de la factura "+factura.getConsecutivoFactura()+".");
						}
						else
							resultado.setDescripcion(concepto.getCodigo()+"");
						//**********************************************************************************************************
					}
					else
					{
						if(remite.equals("RegistrarModificarGlosa"))
						{
							//*********************MODIFICAR CONCEPTO DE GLOSA DE LA FACTURA***********************************
							PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(modificarConceptoFacturaStr, ConstantesBD.typeResultSet, 
								ConstantesBD.concurrencyResultSet ));
							pst.setString(1,concepto.getCodigoConcepto());
							pst.setInt(2,Utilidades.convertirAEntero(factura.getCodigo()));
							
							
							if(pst.executeUpdate()<=0)
							{
								resultado.setResultado(false);
								resultado.setDescripcion("Error actualizando el concepto glosa de la factura "+factura.getConsecutivoFactura()+".");
							}
							else
							{
								resultado.setResultado(true);
							}
						}
					}
					
				} //Fin IF resultado
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en guardarConceptosFactura: "+e);
			resultado.setResultado(false);
			resultado.setDescripcion("Ocurrió error al insertar un concepto glosa de la factura "+factura.getConsecutivoFactura()+" : "+e);
		}
		return resultado;
	}

	/**
	 * Método usado para guardar el encabezado de la glosa 
	 * @param con
	 * @param glosa
	 * @return
	 */
	private static ResultadoBoolean guardarEncabezadoGlosa(Connection con,DtoGlosa glosa) 
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true,"");
		int consecutivo = ConstantesBD.codigoNuncaValido;
		
		try
		{
			double totalGlosa = 0;
			for(DtoFacturaGlosa factura:glosa.getFacturas())
				totalGlosa += Utilidades.convertirADouble(factura.getValorGlosaFacturaStr(), true);
			glosa.setValorGlosa(totalGlosa);
			glosa.setValorGlosaStr(UtilidadTexto.formatearValores(totalGlosa, "0.00"));
			
			//Se verifica si el encabezado de la glosa ya fue registrado
			if(glosa.getCodigo().equals(""))
			{
				//*****************INSERCION DEL ENCABEZADO DE LA GLOSA ************************************************************
				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(insertarEncabezadoGlosaStr, ConstantesBD.typeResultSet, 
					ConstantesBD.concurrencyResultSet ));
				consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_registro_glosas");
				pst.setInt(1,consecutivo);
				if(!glosa.getValorGlosaStr().equals(""))
					pst.setDouble(2, Double.parseDouble(glosa.getValorGlosaStr()));
				else
					pst.setNull(2,Types.NUMERIC);
				if(!glosa.getGlosaSistema().equals(""))
					pst.setString(3,glosa.getGlosaSistema());
				else
					pst.setNull(3,Types.VARCHAR);
				if(!glosa.getFechaRegistroGlosa().equals(""))
					pst.setDate(4,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(glosa.getFechaRegistroGlosa())));
				else
					pst.setNull(4,Types.DATE);
				
				if(glosa.getCodigoConvenio()>0)
					pst.setInt(5,glosa.getCodigoConvenio());
				else
					pst.setNull(5,Types.INTEGER);
				
				if(!glosa.getGlosaEntidad().equals(""))
					pst.setString(6,glosa.getGlosaEntidad());
				else
					pst.setNull(6,Types.VARCHAR);
				
				if(!glosa.getFechaNotificacion().equals(""))
					pst.setDate(7,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(glosa.getFechaNotificacion())));
				else
					pst.setNull(7,Types.DATE);
				
				if(!glosa.getObservaciones().equals(""))
					pst.setString(8,glosa.getObservaciones());
				else
					pst.setNull(8,Types.VARCHAR);
				
				if(!glosa.getUsuarioGlosa().getLoginUsuario().equals(""))
					pst.setString(9, glosa.getUsuarioGlosa().getLoginUsuario());
				else
					pst.setNull(9,Types.VARCHAR);
				
				if(!glosa.getUsuarioAuditor().getLoginUsuario().equals(""))
					pst.setString(10, glosa.getUsuarioAuditor().getLoginUsuario());
				else
					pst.setNull(10,Types.VARCHAR);
				
				if(!glosa.getFechaAuditoria().equals(""))
					pst.setDate(11,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(glosa.getFechaAuditoria())));
				else
					pst.setNull(11,Types.DATE);
				
				if(!glosa.getHoraAuditoria().equals(""))
					pst.setString(12, glosa.getHoraAuditoria());
				else
					pst.setNull(12,Types.VARCHAR);
				/*
				 * El estado es NOT NULL, por tanto se inserta sin hacer la validación
				 */
				pst.setString(13, glosa.getEstado());
				pst.setString(14, glosa.getUsuarioModificacion().getLoginUsuario());
				pst.setInt(15, glosa.getCodigoInstitucion());
				pst.setInt(16, Utilidades.convertirAEntero(glosa.getConsecutivoContrato()+""));
				
				if(pst.executeUpdate()<=0)
				{
					resultado.setResultado(false);
					resultado.setDescripcion("\n\nError ingresando el encabezado de la glosa.");
				}
				else
				{
					resultado.setDescripcion(consecutivo+"");
					glosa.setCodigo(consecutivo+"");
				}
				//*******************************************************************************
			}
			else
			{
				//****************MODIFICACION DEL ENCABEZADO DE LA GLOSA*************************
				
				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(modificarEncabezadoGlosaStr, ConstantesBD.typeResultSet, 
						ConstantesBD.concurrencyResultSet ));
			
				
				if(!glosa.getValorGlosaStr().equals(""))
					pst.setDouble(1, Double.parseDouble(glosa.getValorGlosaStr()));
				else
					pst.setNull(1,Types.NUMERIC);
				
				if(!glosa.getGlosaSistema().equals(""))
					pst.setString(2,glosa.getGlosaSistema());
				else
					pst.setNull(2,Types.VARCHAR);
				
				if(!glosa.getFechaRegistroGlosa().equals(""))
					pst.setDate(3,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(glosa.getFechaRegistroGlosa())));
				else
					pst.setNull(3,Types.DATE);
				
				if(glosa.getCodigoConvenio()>0)
					pst.setInt(4,glosa.getCodigoConvenio());
				else
					pst.setNull(4,Types.INTEGER);
				
				if(!glosa.getGlosaEntidad().equals(""))
					pst.setString(5,glosa.getGlosaEntidad());
				else
					pst.setNull(5,Types.VARCHAR);
				
				if(!glosa.getFechaNotificacion().equals(""))
					pst.setDate(6,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(glosa.getFechaNotificacion())));
				else
					pst.setNull(6,Types.DATE);
				
				if(!glosa.getObservaciones().equals(""))
					pst.setString(7,glosa.getObservaciones());
				else
					pst.setNull(7,Types.VARCHAR);
				
				if(!glosa.getUsuarioGlosa().getLoginUsuario().equals(""))
					pst.setString(8, glosa.getUsuarioGlosa().getLoginUsuario());
				else
					pst.setNull(8,Types.VARCHAR);
				
				if(!glosa.getUsuarioAuditor().getLoginUsuario().equals(""))
					pst.setString(9, glosa.getUsuarioAuditor().getLoginUsuario());
				else
					pst.setNull(9,Types.VARCHAR);
				
				if(!glosa.getFechaAuditoria().equals(""))
					pst.setDate(10,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(glosa.getFechaAuditoria())));
				else
					pst.setNull(10,Types.DATE);
				
				if(!glosa.getHoraAuditoria().equals(""))
					pst.setString(11, glosa.getHoraAuditoria());
				else
					pst.setNull(11,Types.VARCHAR);
				/*
				 * El estado es NOT NULL, por tanto se inserta sin hacer la validación
				 */
				pst.setString(12, glosa.getEstado());
				pst.setString(13, glosa.getUsuarioModificacion().getLoginUsuario());
				pst.setInt(14, glosa.getCodigoInstitucion());
				pst.setInt(15,Utilidades.convertirAEntero(glosa.getCodigo()));
				
				if(pst.executeUpdate()<=0)
				{
					resultado.setResultado(false);
					resultado.setDescripcion("Error modificando el encabezado de la glosa.");
				}
				else
					resultado.setDescripcion(glosa.getCodigo()+"");
				//*********************************************************************************
			}
		}
		catch(SQLException e)
		{
			logger.error("\n\nError en guardarEncabezadoGlosa: "+e);
			resultado.setResultado(false);
			resultado.setDescripcion("\n\nOcurrió error tratando de guardar el encabezado de la glosa: "+e);
		}
		return resultado;
	}
	
	/**
	 * Método para guardar el detalle de una factura
	 * @param con
	 * @param factura
	 * @return
	 */
	private static ResultadoBoolean guardarDetalleFacturaGlosa(Connection con,DtoFacturaGlosa factura) 
	{
		ResultadoBoolean resultado= new ResultadoBoolean(true,"");
		int consecutivo = ConstantesBD.codigoNuncaValido;
		try
		{
			//Se iteran los detalles de la factura
			for(DtoDetalleFacturaGlosa detalle:factura.getDetalle())
			{
				if(resultado.isTrue())
				{
					if(detalle.getCodigo().equals("")&&!detalle.isEliminado())
					{
						//********************INSERTAR EL DETALLE DE LA FACTURA ******************************************************************
						PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(insertarDetalleFacturaGlosaStr, ConstantesBD.typeResultSet, 
							ConstantesBD.concurrencyResultSet ));
						consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_det_auditorias_glosas");
						pst.setInt(1,consecutivo);
						pst.setInt(2, Utilidades.convertirAEntero(factura.getCodigo()));
						pst.setInt(3,Utilidades.convertirAEntero(detalle.getCodigoDetalleFacturaSolicitud()));
						pst.setInt(4,Utilidades.convertirAEntero(detalle.getNumeroSolicitud()));
						//Dependiendo si es servicio o artículo
						if(detalle.isEsServicio())
						{
							pst.setInt(5,Utilidades.convertirAEntero(detalle.getCodigoServicioArticulo()));
							pst.setNull(6,Types.INTEGER);
						}
						else
						{
							pst.setNull(5,Types.INTEGER);
							pst.setInt(6,Utilidades.convertirAEntero(detalle.getCodigoServicioArticulo()));
						}
						pst.setInt(7,detalle.getCantidadGlosa());
						pst.setDouble(8,Double.parseDouble(detalle.getValorGlosaStr()));
						pst.setInt(9, detalle.getCantidad());
						pst.setDouble(10, Double.parseDouble(detalle.getValorStr()));
						
						if(pst.executeUpdate()<=0)
						{
							resultado.setResultado(false);
							resultado.setDescripcion("Error ingresando el detalle (servicio/asocio) de la factura de la glosa.");
						}
						else
						{
							resultado.setDescripcion(consecutivo+"");
							detalle.setCodigo(consecutivo+"");
						}
						//**************************************************************************************************
					}
					else if(!detalle.isEliminado()&&!detalle.getCodigo().equals(""))
					{
						//************MODIFICAR EL DETALLE DE LA FACTURA*************************************************
						PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(modificarDetalleFacturaGlosaStr, ConstantesBD.typeResultSet, 
							ConstantesBD.concurrencyResultSet ));
						pst.setInt(1,detalle.getCantidadGlosa());
						pst.setDouble(2,Double.parseDouble(detalle.getValorGlosaStr()));
						pst.setInt(3,detalle.getCantidad());
						pst.setDouble(4,Double.parseDouble(detalle.getValorStr()));
						pst.setInt(5,Utilidades.convertirAEntero(detalle.getCodigo()));
						
						logger.info("modificarDetalleFacturaGlosa: "+modificarDetalleFacturaGlosaStr+", cantidadGlosa: "+detalle.getCantidadGlosa()+", valorGlosa: "+detalle.getValorGlosaStr()+", cantidad: "+detalle.getCantidad()+", valor: "+detalle.getValorStr()+", codigo: "+detalle.getCodigo());
						
						if(pst.executeUpdate()<=0)
						{
							resultado.setResultado(false);
							resultado.setDescripcion("Error modificando el detalle (servicio/asocio) de la factura de la glosa.");
						}
						else
							resultado.setDescripcion(detalle.getCodigo());
						//************************************************************************************************
					}
					else if(detalle.isEliminado()&&!detalle.getCodigo().equals(""))
					{
						//**********ELIMINANDO EL DETALLE DE LA FACTURA*********************************************************
						//Si el detalle tiene conceptos en la base de datos, se eliminan primero
						if(detalle.tieneConceptosEnBD())
						{
							String consulta = eliminarConceptosDetalleFacturaGlosaStr + " det_audi_fact = "+detalle.getCodigo();
							Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
							if(st.executeUpdate(consulta)<=0)
							{
								resultado.setResultado(false);
								resultado.setDescripcion("Error tratando de eliminar los conceptos de la orden "+detalle.getConsecutivoOrden()+".");
							}
							else
							{
								//SE eliminan todos los conceptos del detalle
								detalle.getConceptos().clear();
							}
						}
						
						//Si el detalle tiene asocios en la base de datos , se deben eliminar primero
						if(detalle.tieneAsociosEnBD()&&resultado.isTrue())
						{
							for(DtoDetalleAsociosGlosa asocio:detalle.getAsocios())
								//Solo se toman los asocios que existan en la base de datos
								if(!asocio.getCodigo().equals("")&&resultado.isTrue())
								{
									if(asocio.tieneConceptosEnBD())
									{
										String consulta = eliminarConceptosAsocioDetalleFacturaGlosaStr + " asocio_auditoria_glosa =  "+asocio.getCodigo();
										Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
										
										if(st.executeUpdate(consulta)<=0)
										{
											resultado.setResultado(false);
											resultado.setDescripcion("Error tratando de eliminar los conceptos del asocio "+asocio.getNombreAsocio()+".");
										}
									}
									
									//Se verifica que todo vaya con éxito
									if(resultado.isTrue())
									{
										PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarAsocioDetalleFacturaGlosaStr,ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet ));
										pst.setInt(1,Utilidades.convertirAEntero(asocio.getCodigo()));
										
										if(pst.executeUpdate()<=0)
										{
											resultado.setResultado(false);
											resultado.setDescripcion("Error tratando de eliminar un asocio de la orden "+detalle.getConsecutivoOrden()+".");
										}
									}
								}
							
							if(resultado.isTrue())
								detalle.getAsocios().clear();
						}
						
						if(resultado.isTrue())
						{
						
							PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarDetalleFacturaGlosaStr, ConstantesBD.typeResultSet, 
								ConstantesBD.concurrencyResultSet ));
								pst.setInt(1,Utilidades.convertirAEntero(detalle.getCodigo()) );
							if(pst.executeUpdate()<=0)
							{
								resultado.setResultado(false);
								resultado.setDescripcion("Error Eliminando el registro (servicio/asocio) de la factura de la glosa.");
							}
							else
								resultado.setDescripcion(detalle.getCodigo());
						}
						//********************************************************************************************************
					}
					
					//Se verifica que todo vaya bien
					if(resultado.isTrue())
					{
						//**********GUARDAR CONCEPTOS DEL DETALLE DE FACTURA****************
						resultado = guardarConceptosDetalleFacturaServicioArticuloGlosa(con,detalle);
						//********************************************************************
					}
					
					//Se verifica que todo vaya bien
					if(resultado.isTrue())
					{
						//**********GUARDAR DETALLE DE ASOCIO DE LA FACTURA***************************
						resultado = guardarDetalleAsociosFacturaGlosa(con,detalle);
						//****************************************************************************
					}
					
				} //Fin IF resultados validos
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en guardarDetalleFacturaGlosa: "+e);
			resultado.setResultado(false);
			resultado.setDescripcion("Ocurrió error tratando de guardar el detalle de una factura: "+e);
		}
		
		return resultado;
	}

	/**
	 * Método para guardar el detalle de los asocios de la factura de la glosa
	 * @param con
	 * @param detalle
	 * @return
	 */
	private static ResultadoBoolean guardarDetalleAsociosFacturaGlosa(Connection con, DtoDetalleFacturaGlosa detalle) 
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true,"");
		try
		{
			for(DtoDetalleAsociosGlosa detalleAsocio:detalle.getAsocios())
			{
				if(resultado.isTrue())
				{
					if(detalleAsocio.getCodigo().equals(""))
					{
						//**************INGRESAR ASOCIOS DEL DETALLE DE LA FACTURA***************************************
						PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(insertarAsociosDetalleFacturaGlosaStr, ConstantesBD.typeResultSet, 
							ConstantesBD.concurrencyResultSet ));
						int consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_asocios_auditorias_glosas");
						pst.setInt(1,consecutivo);
						pst.setInt(2,Utilidades.convertirAEntero(detalleAsocio.getCodigoAsocioDetalleFactura()));
						pst.setInt(3,Utilidades.convertirAEntero(detalle.getCodigo()));
						pst.setInt(4,detalleAsocio.getCodigoAsocio());
						if(detalleAsocio.getCodigoProfesional()>0)
							pst.setInt(5,detalleAsocio.getCodigoProfesional());
						else
							pst.setNull(5,Types.INTEGER);
						if(detalleAsocio.getCantidadGlosa()!=null&&detalleAsocio.getCantidadGlosa().intValue()>0)
							pst.setInt(6,detalleAsocio.getCantidadGlosa());
						else
							pst.setNull(6,Types.INTEGER);
						if(Utilidades.convertirADouble(detalleAsocio.getValorGlosaStr())>0)
							pst.setDouble(7,Double.parseDouble(detalleAsocio.getValorGlosaStr()));
						else
							pst.setNull(7,Types.NUMERIC);
						pst.setInt(8,detalleAsocio.getCodigoServicioAsocio());
						pst.setInt(9,detalleAsocio.getCantidad());
						pst.setDouble(10,Double.parseDouble(detalleAsocio.getValorStr()));
						
						if(pst.executeUpdate()<=0)
						{
							resultado.setResultado(false);
							resultado.setDescripcion("Error ingresando el asocio "+detalleAsocio.getNombreAsocio()+" de la orden "+
								detalle.getConsecutivoOrden()+" y el servicio "+detalle.getDescripcionServicioArticulo()+
								" de la factura de la glosa.");
						}
						else
						{
							resultado.setDescripcion(consecutivo+"");
							detalleAsocio.setCodigo(consecutivo+"");
							detalleAsocio.setCodigoDetalleGlosa(detalle.getCodigo());
						}
						
						//***********************************************************************************************
					}
					else
					{
						//*************MODIFICAR ASOCIOS DEL DETALLE DE LA FACTURA**************************************
						PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(modificarAsociosDetalleFacturaGlosaStr, ConstantesBD.typeResultSet, 
							ConstantesBD.concurrencyResultSet ));
						
						if(detalleAsocio.getCantidadGlosa()!=null&&detalleAsocio.getCantidadGlosa().intValue()>0)
							pst.setInt(1,detalleAsocio.getCantidadGlosa());
						else
							pst.setNull(1,Types.INTEGER);
						if(Utilidades.convertirADouble(detalleAsocio.getValorGlosaStr())>0)
							pst.setDouble(2,Double.parseDouble(detalleAsocio.getValorGlosaStr()));
						else
							pst.setNull(2,Types.NUMERIC);
						
						pst.setInt(3,detalleAsocio.getCantidad());
						pst.setDouble(4,Double.parseDouble(detalleAsocio.getValorStr()));
						pst.setInt(5,Utilidades.convertirAEntero(detalleAsocio.getCodigo()));
						
						if(pst.executeUpdate()<=0)
						{
							resultado.setResultado(false);
							resultado.setDescripcion("Error modificando el asocio "+detalleAsocio.getNombreAsocio()+
								" de la orden "+detalle.getConsecutivoOrden()+" y el servicio "+detalle.getDescripcionServicioArticulo()+
								" de la factura de la glosa.");
						}
						else
							resultado.setDescripcion(detalleAsocio.getCodigo());
						//***********************************************************************************************
					}
					
					//Se verifica que todo vaya bien
					if(resultado.isTrue())
					{
						resultado = guardarConceptosAsocioDetalleFacturaGlosa(con,detalleAsocio);
					}
					
				} //Fin IF validacion resultado
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en guardarDetalleAsociosFacturaGlosa: "+e);
			resultado.setResultado(false);
			resultado.setDescripcion("Ocurrió error tratando de guardar los asocios de la orden "+detalle.getConsecutivoOrden()+
				" y servicio: "+detalle.getDescripcionServicioArticulo()+": "+e);
		}
		
		return resultado;
	}

	/**
	 * Método para guardar los conceptos del asocios del detalle de la factura de la glosa
	 * @param con
	 * @param detalleAsocio
	 * @return
	 */
	private static ResultadoBoolean guardarConceptosAsocioDetalleFacturaGlosa(Connection con, DtoDetalleAsociosGlosa detalleAsocio) 
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true,"");
		try
		{
			for(DtoConceptoGlosa concepto:detalleAsocio.getConceptos())
			{
				if(resultado.isTrue())
				{
					if(concepto.getCodigo().equals(""))
					{
						//*************INSERCION DEL CONCEPTO GLOSA DEL ASOCIO DEL DETALLE DE LA FACTURA****************************
						PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(insertarConceptoDetalleAsocioFacturaGlosaStr, ConstantesBD.typeResultSet, 
							ConstantesBD.concurrencyResultSet ));
						int consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_conceptos_aso_audi_glosas");
						pst.setInt(1,consecutivo);
						pst.setInt(2,Utilidades.convertirAEntero(detalleAsocio.getCodigo()));
						pst.setString(3,concepto.getCodigoConcepto());
						pst.setInt(4,concepto.getCodigoInstitucion());
						
						if(pst.executeUpdate()<=0)
						{
							resultado.setResultado(false);
							resultado.setDescripcion("Error ingresando el concepto del asocio del detalle de la factura de la glosa.");
						}
						else
						{
							resultado.setDescripcion(consecutivo+"");
							concepto.setCodigo(consecutivo+"");
							concepto.setCodigoDetalleAsocioGlosa(detalleAsocio.getCodigo());
						}
						
						//**********************************************************************************************************
					}
					else if(concepto.isEliminado())
					{
						//*************ELIMINAR DEL CONCEPTO GLOSA DEL ASOCIO DEL DETALLE DE LA FACTURA*********************************
						PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarConceptoDetalleAsocioFacturaGlosaStr, ConstantesBD.typeResultSet, 
							ConstantesBD.concurrencyResultSet ));
						pst.setInt(1, Utilidades.convertirAEntero(concepto.getCodigo()));
						if(pst.executeUpdate()<=0)
						{
							resultado.setResultado(false);
							resultado.setDescripcion("Error eliminando el concepto del asocio del detalle de la factura de la glosa.");
						}
						else
							resultado.setDescripcion(concepto.getCodigo()+"");
						//*****************************************************************************************************************
					}
				} //Fin IF de resultado
			}
		}
		catch(SQLException e)
		{
			logger.error("Error guardarConceptosAsocioDetalleFacturaGlosa: "+e);
			resultado.setResultado(false);
			resultado.setDescripcion("Ocurrió error tratando de guardar los conceptos del asocio "+detalleAsocio.getNombreAsocio()+": "+e);
		}
		
		return resultado;
	}

	/**
	 * Método para guardar los conceptos detalle de los servicios/asocios del detalle de la factura de la glosa
	 * @param con
	 * @param detalle
	 * @return
	 */
	private static ResultadoBoolean guardarConceptosDetalleFacturaServicioArticuloGlosa(Connection con, DtoDetalleFacturaGlosa detalle) 
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true,"");
		int consecutivo = ConstantesBD.codigoNuncaValido;
		try
		{
			//Se iteran los conceptos del detalle del servicio/articulo de la factura
			for(DtoConceptoGlosa concepto:detalle.getConceptos())
			{
				if(resultado.isTrue())
				{
					/*PreparedStatementDecorator pstDetalleConceptosAuditoriasGlosas =  new PreparedStatementDecorator(con.prepareStatement(consultarConceptoDetalleAuditoriasGlosasStr, 
					ConstantesBD.typeResultSet,	ConstantesBD.concurrencyResultSet ));
					pstDetalleConceptosAuditoriasGlosas.setInt(1, Utilidades.convertirAEntero(detalle.getCodigo()));
					ResultSetDecorator rsDetalleConceptoAudiGlosa = new ResultSetDecorator(pstDetalleConceptosAuditoriasGlosas.executeQuery());
					
					String codigo="";
					
					if(rsDetalleConceptoAudiGlosa.next())
						codigo=rsDetalleConceptoAudiGlosa.getString("codigo");*/
					if(concepto.getCodigo().equals(""))
					{
						//******************INSERTAR UN CONCEPTO DEL DETALLE SERVICIO/ARTICULO DE LA FACTURA************************************
						PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(insertarConceptoDetalleFacturaServicioArticuloStr, 
							ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet ));
						consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_conceptos_det_audi_glosas");
						pst.setInt(1,consecutivo);
						pst.setInt(2, Utilidades.convertirAEntero(detalle.getCodigo()));
						pst.setString(3, concepto.getCodigoConcepto());
						pst.setInt(4,concepto.getCodigoInstitucion());
						
						
						if(pst.executeUpdate()<=0)
						{
							resultado.setResultado(false);
							resultado.setDescripcion("Error ingresando el concepto del detalle (servicio/asocio) de la factura de la glosa.");
						}
						else
						{
							resultado.setDescripcion(consecutivo+"");
							concepto.setCodigo(consecutivo+"");
							concepto.setCodigoDetalleFacturaGlosa(detalle.getCodigo());
						}
						//***************************************************************************************************************************
					}
					/*else if(!concepto.getCodigo().equals(""))
					{
						//******************INSERTAR UN CONCEPTO DEL DETALLE SERVICIO/ARTICULO DE LA FACTURA************************************
						PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(modificarConceptoDetalleFacturaServicioArticuloStr, 
							ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet ));
						pst.setString(1, concepto.getCodigoConcepto());
						pst.setInt(2,concepto.getCodigoInstitucion());
						pst.setInt(3, Utilidades.convertirAEntero(detalle.getCodigo()));
						
						if(pst.executeUpdate()<=0)
						{
							resultado.setResultado(false);
							resultado.setDescripcion("Error ingresando el concepto del detalle (servicio/asocio) de la factura de la glosa.");
						}
						else
						{
							resultado.setDescripcion(consecutivo+"");
							concepto.setCodigo(consecutivo+"");
							concepto.setCodigoDetalleFacturaGlosa(detalle.getCodigo());
						}
						//***************************************************************************************************************************
					}*/
					//Se verifica si el concepto se debe eliminar
					else if(concepto.isEliminado())
					{
						//**************ELIMINAR UN CONCEPTO DEL DETALLE SERVICIO/ARTICULO DE LA FACTURA ***********************************************
						PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarConceptoDetalleFacturaServicioArticuloStr, 
							ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet ));
						pst.setInt(1,Utilidades.convertirAEntero(concepto.getCodigo()));
						if(pst.executeUpdate()<=0)
						{
							resultado.setResultado(false);
							resultado.setDescripcion("Error eliminando el concepto del detalle (servicio/asocio) de la factura de la glosa.");
						}
						else
							resultado.setDescripcion(concepto.getCodigo()+"");
						//*******************************************************************************************************************************
					}
						
				}
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en guardarDetalleFacturaServicioArticuloGlosa: "+e);
			resultado.setResultado(false);
			resultado.setDescripcion("Ocurrió error tratando de guardar los conceptos de un detalle servicio/asocio: "+e);
		}
		return resultado;
	}
	
	/**
	 * Método implementado para cargar los datos de una glosa
	 * @author Sebastián Gómez R. - Felipe Pérez Granda
	 * @param con Connection
	 * @param criterios HashMap <String, Object> 
	 * @return DtoGlosa
	 */
	public static DtoGlosa cargarGlosa(Connection con, HashMap <String, Object>  criterios, UsuarioBasico usuario)
	{
		logger.info("===> Entré al SqlBase");
		//Variables para la creacion del Log Tipo Archivo
		String logPrevio="";
		String logDespues="";
		String logPrevioInterno="";
		String logDespuesInterno="";
	
		DtoGlosa glosa = new DtoGlosa();
		try
		{
			//*****************se toman los parametros*********************************************************************
			int codigoTarifarioOficial = Utilidades.convertirAEntero(criterios.get("codigoTarifarioOficial")+"", true);
			int codigo = Utilidades.convertirAEntero(criterios.get("codigo")+"");
			//***********************************************************************************************************
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarEncabezadoGlosaStr, ConstantesBD.typeResultSet, 
				ConstantesBD.concurrencyResultSet ));
			pst.setInt(1, codigo);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				//String antes de la modificacion para Creación del Log - Fase de Glosa
				logPrevio=	"DATOS DE GLOSA  \n" +
						"Codigo: "+glosa.getCodigo()+"\n"+
						"Valor: " +glosa.getValorGlosa()+"\n"+
						"Glosa Sistema: " +glosa.getGlosaSistema()+"\n"+
						"Codigo Convenio: " +glosa.getCodigoConvenio()+"\n"+
						"Nombre Convenio: " +glosa.getNombreConvenio()+"\n"+
						"Glosa Entidad: "+glosa.getGlosaEntidad()+"\n";
				
				glosa.setCodigo(rs.getString("codigo"));
				glosa.setValorGlosa(Utilidades.convertirADouble(rs.getString("valor_glosa"), true));
				glosa.setValorGlosaStr(rs.getString("valor_glosa"));
				glosa.setGlosaSistema(rs.getString("glosa_sistema"));
				glosa.setFechaRegistroGlosa(rs.getString("fecha_registro_glosa"));
				glosa.setCodigoConvenio(rs.getInt("codigo_convenio"));
				glosa.setNombreConvenio(rs.getString("nombre_convenio"));
				glosa.setGlosaEntidad(rs.getString("glosa_entidad"));
				glosa.setFechaNotificacion(rs.getString("fecha_notificacion"));
				glosa.setObservaciones(rs.getString("observaciones"));
				if(!rs.getString("usuario_glosa").toString().equals(""))
					glosa.getUsuarioGlosa().cargarUsuarioBasico(con, rs.getString("usuario_glosa"));
				if(!rs.getString("usuario_auditor").toString().equals(""))
					glosa.getUsuarioAuditor().cargarUsuarioBasico(con, rs.getString("usuario_auditor"));
				glosa.setFechaAuditoria(rs.getString("fecha_auditoria"));
				glosa.setHoraAuditoria(rs.getString("hora_auditoria"));
				glosa.setEstado(rs.getString("estado"));
				glosa.setFechaModificacion(rs.getString("fecha_modifica"));
				glosa.setHoraModificacion(rs.getString("hora_modifica"));
				glosa.getUsuarioModificacion().cargarUsuarioBasico(con, rs.getString("usuario_modifica"));
				glosa.setCodigoInstitucion(rs.getInt("codigo_institucion"));
				
				//String después de la modificacion para Creación del Log - Fase de Glosa
				logDespues=	"DATOS DE GLOSA \n" +
						"Codigo: "+glosa.getCodigo()+"\n"+
						"Valor: " +glosa.getValorGlosa()+"\n"+
						"Glosa Sistema: " +glosa.getGlosaSistema()+"\n"+
						"Codigo Convenio: " +glosa.getCodigoConvenio()+"\n"+
						"Nombre Convenio: " +glosa.getNombreConvenio()+"\n"+
						"Glosa Entidad: "+glosa.getGlosaEntidad()+"\n";
			}
			
			//logger.info("===> Entré al SqlBase Parte 1");
			if(!glosa.getCodigo().equals(""))
			{								
				String cadena= consultarAuditoriasGlosa;
				
				if(!criterios.get("auditoria").equals(""))
				{
					cadena += " WHERE ag.codigo=? ";
					logger.info("===> consultarAuditoriasGlosa = "+cadena.replace("?", criterios.get("auditoria")+""));
				}
				else
				{
					cadena += " WHERE ag.glosa=? ";
					logger.info("===> consultarAuditoriasGlosa = "+cadena.replace("?", criterios.get("codigo")+""));
				}
							
				logger.info("===> ag.codigo = "+glosa.getCodigo());
				logger.info("===> auditoria = "+criterios.get("auditoria"));
				
				pst =  new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				
				if(!criterios.get("auditoria").equals(""))
					pst.setString(1, (criterios.get("auditoria")+""));
				else
					pst.setInt(1, Utilidades.convertirAEntero(glosa.getCodigo()));
				rs = new ResultSetDecorator(pst.executeQuery());
				
				//Se inician los títulos de la información pre y post modificación de la Auditoría de Glosa
				logPrevio+="\nINFORMACIÓN FACTURA \n";
				logDespues+="\nINFORMACIÓN FACTURA \n";
				
				while(rs.next())
				{
					logger.info("===> Entré al SqlBase Parte 2");
					DtoFacturaGlosa factura = new DtoFacturaGlosa();
					
					//Asignación del String para el Log con al información antes de modificar
					logPrevio+=	"FACTURA\n"+
								"Codigo Factura: "+factura.getCodigoFactura()+"\n" +
								"Consecutivo Factura: "+factura.getConsecutivoFactura()+"\n"+
								"Saldo Factura: "+factura.getSaldoFacturaStr()+"\n";
					
					factura.setCodigo(rs.getString("codigo"));
					factura.setCodigoFactura(rs.getString("codigo_factura"));
					factura.setConsecutivoFactura(rs.getString("consecutivo_factura"));
					factura.setFechaModificacion(rs.getString("fecha_modificacion"));
					factura.setHoraModificacion(rs.getString("hora_modificacion"));
					factura.getUsuarioModificacion().cargarUsuarioBasico(con, rs.getString("usuario_modificacion"));
					factura.setCodigoContrato(rs.getInt("contrato"));
					factura.setValorGlosaFactura(Utilidades.convertirADouble(rs.getString("valor_glosa_factura"),  true));
					factura.setValorGlosaFacturaStr(rs.getString("valor_glosa_factura"));
					factura.setFechaElaboracionFactura(rs.getString("fecha_elaboracion_fact"));
					factura.setNumeroCuentaCobro(rs.getString("numero_cuenta_cobro"));
					factura.setInstitucion(rs.getInt("institucion"));
					factura.setFechaRadicacion(rs.getString("fecha_radicacion_cxc"));
					factura.setConsecutivoFactura(rs.getString("consecutivo_factura"));
					factura.setSaldoFactura(Utilidades.convertirADouble(rs.getString("saldo_factura"), false));
					factura.setSaldoFacturaStr(rs.getString("saldo_factura"));
					factura.setCodigoGlosa(glosa.getCodigo());
					
					/*
					 * Saldos y Totales de la factura
					 */
					factura.setTotalFacturaStr(rs.getString("total_factura"));
					factura.setTotalFactura(Utilidades.convertirADouble(rs.getString("total_factura"), false));
					factura.setTotalResponsableStr(rs.getString("total_responsable"));
					factura.setTotalResponsable(Utilidades.convertirADouble(rs.getString("total_responsable"), false));
					factura.setTotalBrutoPacienteStr(rs.getString("total_bruto_paciente"));
					factura.setTotalBrutoPaciente(Utilidades.convertirADouble(rs.getString("total_bruto_paciente"), false));
					factura.setTotalDescuentosStr(rs.getString("total_descuentos"));
					factura.setTotalDescuentos(Utilidades.convertirADouble(rs.getString("total_descuentos"), false));
					factura.setTotalAbonosStr(rs.getString("total_abonos"));
					factura.setTotalAbonos(Utilidades.convertirADouble(rs.getString("total_abonos"), false));
					factura.setTotalNetoPacienteStr(rs.getString("total_neto_paciente"));
					factura.setTotalNetoPaciente(Utilidades.convertirADouble(rs.getString("total_neto_paciente"), false));
					
					//Asignación del String para el Log con al información después de modificar
					logDespues+=	"FACTURA\n"+
								"Codigo: "+factura.getCodigo()+"\n"+
								"Codigo Factura: "+factura.getCodigoFactura()+"\n"+
								"Saldo Factura: "+factura.getSaldoFacturaStr()+"\n";
										
					logger.info("===> Entré al SqlBase Parte 3");
					logger.info("===> consultarConceptosAudiGlosasStr = "+consultarConceptosAudiGlosasStr);
					logger.info("===> codigo = "+glosa.getCodigo());
					/*
					 * Preparación de la consulta para los conceptos_audi_glosas (conceptos de la factura)
					 */
					PreparedStatementDecorator pstConceptosAudiGlosas =  new PreparedStatementDecorator(con.prepareStatement(consultarConceptosAudiGlosasStr, ConstantesBD.typeResultSet, 
						ConstantesBD.concurrencyResultSet ));
					pstConceptosAudiGlosas.setInt(1, Utilidades.convertirAEntero(factura.getCodigo()));
					ResultSetDecorator rsConceptosAudiGlosas = new ResultSetDecorator(pstConceptosAudiGlosas.executeQuery());
					
					while(rsConceptosAudiGlosas.next())
					{
						
						//Se inician los títulos de la información pre y post modificación de la Auditoría de Glosa
						logPrevioInterno="\nCONCEPTO \n";
						logDespuesInterno="\nCONCEPTO \n";
						
						DtoConceptoGlosa conceptofactura = new DtoConceptoGlosa();

						//Asignación del String para el Log con al información antes de modificar
						logPrevioInterno+= 	"Codigo: "+conceptofactura.getCodigo()+"\n"+
											"Auditoria Glosa: "+conceptofactura.getCodigoFacturaGlosa()+"\n"+
											"Concepto: "+conceptofactura.getCodigoConcepto()+"\n"+
											"Descipcion: "+conceptofactura.getDescripcion()+"\n";

						conceptofactura.setCodigo(rsConceptosAudiGlosas.getString("codigo"));
						conceptofactura.setCodigoFacturaGlosa(rsConceptosAudiGlosas.getString("auditoria_glosa"));
						conceptofactura.setCodigoConcepto(rsConceptosAudiGlosas.getString("concepto_glosa"));
						conceptofactura.setCodigoInstitucion(rsConceptosAudiGlosas.getInt("institucion"));
						conceptofactura.setDescripcion(rsConceptosAudiGlosas.getString("descripcion"));
						conceptofactura.setTipo(rsConceptosAudiGlosas.getString("tipo"));
						
						//Asignación del String para el Log con al información después de modificar
						logDespuesInterno+= "Codigo: "+conceptofactura.getCodigo()+"\n"+
											"Auditoria Glosa: "+conceptofactura.getCodigoFacturaGlosa()+"\n"+
											"Concepto: "+conceptofactura.getCodigoConcepto()+"\n"+
											"Descipcion: "+conceptofactura.getDescripcion()+"\n";
						
						factura.getConceptos().add(conceptofactura);	
					}
					
					logPrevio+=logPrevioInterno;
					logDespues+=logDespuesInterno;
					
					logger.info("===> Entré al SqlBase Parte 4");
					logger.info("===> consultarDetalleAuditoriasGlosasStr = "+consultarDetalleAuditoriasGlosasStr);
					logger.info("===> codigo = "+factura.getCodigo());
					/*
					 * Preparación de la consulta para el detalle de auditorías glosas (Detalle de la factura)
					 */
					PreparedStatementDecorator pstDetalleAuditoriasGlosas =  new PreparedStatementDecorator(con.prepareStatement(consultarDetalleAuditoriasGlosasStr, 
						ConstantesBD.typeResultSet,	ConstantesBD.concurrencyResultSet ));
					pstDetalleAuditoriasGlosas.setInt(1, codigoTarifarioOficial);
					pstDetalleAuditoriasGlosas.setInt(2, Utilidades.convertirAEntero(factura.getCodigo()));
					ResultSetDecorator rsDetalleAudiGlosa = new ResultSetDecorator(pstDetalleAuditoriasGlosas.executeQuery());
					
					//Se inician los títulos de la información pre y post modificación de la Auditoría de Glosa
					logDespuesInterno+="\nDETALLE AUDITORIAS GLOSAS \n";
					logPrevioInterno+="\nDETALLE AUDITORIAS GLOSAS \n";
					
					while(rsDetalleAudiGlosa.next())
					{
						DtoDetalleFacturaGlosa detalleAuditoriasGlosas = new DtoDetalleFacturaGlosa();
						
						//String para Creación del Log - Fase de Detalle Auditorias
						logPrevioInterno+=	"Codigo Glosa: "+detalleAuditoriasGlosas.getCodigo()+"\n"+
											"Codigo: "+detalleAuditoriasGlosas.getCodigo()+"\n"+
											"Detalle Factura Solicitud: "+detalleAuditoriasGlosas.getCodigoDetalleFacturaSolicitud()+"\n"+
											"Consecutivo: "+detalleAuditoriasGlosas.getConsecutivoOrden()+"\n"+
											"Decripcion Articulo/Servicio: "+detalleAuditoriasGlosas.getDescripcionServicioArticulo()+"\n"+
											"Cantidad Glosa: "+detalleAuditoriasGlosas.getCantidadGlosa()+"\n"+
											"Valor Glosa: "+detalleAuditoriasGlosas.getValorGlosaStr()+"\n"+
											"Codigo Articulo Servicio: "+detalleAuditoriasGlosas.getCodigoServicioArticulo()+"\n";
						
						detalleAuditoriasGlosas.setCodigoGlosa(factura.getCodigo());
						detalleAuditoriasGlosas.setCodigo(rsDetalleAudiGlosa.getString("codigo"));
						detalleAuditoriasGlosas.setCodigoDetalleFacturaSolicitud(rsDetalleAudiGlosa.getString("det_factura_solicitud"));
						detalleAuditoriasGlosas.setNumeroSolicitud(rsDetalleAudiGlosa.getString("solicitud"));
						detalleAuditoriasGlosas.setConsecutivoOrden(rsDetalleAudiGlosa.getString("consecutivo_solicitud"));
						detalleAuditoriasGlosas.setDescripcionServicioArticulo(rsDetalleAudiGlosa.getString("descripcion_articulo_servicio"));
						detalleAuditoriasGlosas.setCantidadGlosa(rsDetalleAudiGlosa.getInt("cantidad_glosa"));
						detalleAuditoriasGlosas.setValorGlosa(Utilidades.convertirADouble(
							rsDetalleAudiGlosa.getString("valor_glosa"), false));
						detalleAuditoriasGlosas.setValorGlosaStr(rsDetalleAudiGlosa.getString("valor_glosa"));
						detalleAuditoriasGlosas.setCantidad(rsDetalleAudiGlosa.getInt("cantidad"));
						detalleAuditoriasGlosas.setValor(Utilidades.convertirADouble(
							rsDetalleAudiGlosa.getString("valor"), true));
						detalleAuditoriasGlosas.setValorStr(rsDetalleAudiGlosa.getString("valor"));
						detalleAuditoriasGlosas.setCodigoServicioArticulo(rsDetalleAudiGlosa.getString("codigo_articulo_servicio"));
						detalleAuditoriasGlosas.setEsServicio(UtilidadTexto.getBoolean(rsDetalleAudiGlosa.getString("es_servicio")));
						detalleAuditoriasGlosas.setCodigoTipoSolicitud(rsDetalleAudiGlosa.getInt("codigo_tipo_solicitud"));
						detalleAuditoriasGlosas.setNombreTipoSolicitud(rsDetalleAudiGlosa.getString("nombre_tipo_solicitud"));
						
						//String para Creación del Log - Fase de Detalle Auditorias
						logDespuesInterno+=	"Codigo Glosa: "+detalleAuditoriasGlosas.getCodigo()+"\n"+
											"Codigo: "+detalleAuditoriasGlosas.getCodigo()+"\n"+
											"Detalle Factura Solicitud: "+detalleAuditoriasGlosas.getCodigoDetalleFacturaSolicitud()+"\n"+
											"Consecutivo: "+detalleAuditoriasGlosas.getConsecutivoOrden()+"\n"+
											"Decripcion Articulo/Servicio: "+detalleAuditoriasGlosas.getDescripcionServicioArticulo()+"\n"+
											"Cantidad Glosa: "+detalleAuditoriasGlosas.getCantidadGlosa()+"\n"+
											"Valor Glosa: "+detalleAuditoriasGlosas.getValorGlosaStr()+"\n"+
											"Codigo Articulo Servicio: "+detalleAuditoriasGlosas.getCodigoServicioArticulo()+"\n";
						
						if(rsDetalleAudiGlosa.getString("esarticulo").equals("SERV"))
							detalleAuditoriasGlosas.setEsArticulo(false);
						else
							detalleAuditoriasGlosas.setEsArticulo(true);
						
						logger.info("===> Entré al SqlBase Parte 5");
						logger.info("===> consultarDetalleConceptosAuditoriasGlosasStr = "+consultarDetalleConceptosAuditoriasGlosasStr);
						logger.info("===> codigo = "+detalleAuditoriasGlosas.getCodigo());
						/*
						 * Preparación de la consulta para el detalle del concepto auditorías glosas (Detalle del concepto de la factura)
						 */
						PreparedStatementDecorator pstDetalleConceptosAuditoriasGlosas =  new PreparedStatementDecorator(con.prepareStatement(consultarDetalleConceptosAuditoriasGlosasStr, 
							ConstantesBD.typeResultSet,	ConstantesBD.concurrencyResultSet ));
						pstDetalleConceptosAuditoriasGlosas.setInt(1, Utilidades.convertirAEntero(detalleAuditoriasGlosas.getCodigo()));
						ResultSetDecorator rsDetalleConceptoAudiGlosa = new ResultSetDecorator(pstDetalleConceptosAuditoriasGlosas.executeQuery());
						
						//Se inician los títulos de la información pre y post modificación de la Auditoría de Glosa
						logDespuesInterno+="\nCONCEPTOS DETALLE AUDITORIAS GLOSAS \n";
						logPrevioInterno+="\nCONCEPTOS DETALLE AUDITORIAS GLOSAS \n";
						
						while(rsDetalleConceptoAudiGlosa.next())
						{
							DtoConceptoGlosa conceptoDetalleAuditoriasGlosas = new DtoConceptoGlosa();
							
							//String para Creación del Log - Fase de Conceptos Detalle Auditorias Glosas
							logPrevioInterno+=	"Codigo: "+conceptoDetalleAuditoriasGlosas.getCodigo()+"\n\n" +
												"Detalle Factura Auditada: "+conceptoDetalleAuditoriasGlosas.getCodigoDetalleFacturaGlosa()+"\n"+
												"Concepto Glosa: "+conceptoDetalleAuditoriasGlosas.getCodigoConcepto()+"\n" +
												"Descripción: "+conceptoDetalleAuditoriasGlosas.getDescripcion()+"\n";
							
							conceptoDetalleAuditoriasGlosas.setCodigo(rsDetalleConceptoAudiGlosa.getString("codigo"));
							conceptoDetalleAuditoriasGlosas.setCodigoDetalleFacturaGlosa(rsDetalleConceptoAudiGlosa.getString("det_audi_fact"));
							conceptoDetalleAuditoriasGlosas.setCodigoConcepto(rsDetalleConceptoAudiGlosa.getString("concepto_glosa"));
							conceptoDetalleAuditoriasGlosas.setCodigoInstitucion(rsDetalleConceptoAudiGlosa.getInt("institucion"));
							conceptoDetalleAuditoriasGlosas.setDescripcion(rsDetalleConceptoAudiGlosa.getString("descripcion"));
							conceptoDetalleAuditoriasGlosas.setTipo(rsDetalleConceptoAudiGlosa.getString("tipo"));
							
							detalleAuditoriasGlosas.getConceptos().add(conceptoDetalleAuditoriasGlosas);
							
							//String para Creación del Log - Fase de Conceptos Detalle Auditorias Glosas
							logDespuesInterno+=	"Codigo: "+conceptoDetalleAuditoriasGlosas.getCodigo()+"\n" +
												"Detalle Factura Auditada: "+conceptoDetalleAuditoriasGlosas.getCodigoDetalleFacturaGlosa()+"\n"+
												"Concepto Glosa: "+conceptoDetalleAuditoriasGlosas.getCodigoConcepto()+"\n" +
												"Descripción: "+conceptoDetalleAuditoriasGlosas.getDescripcion()+"\n\n";
						}
						
						logger.info("===> Entré al SqlBase Parte 6");
						logger.info("===> consultarAsociosAuditoriasGlosasStr = "+consultarAsociosAuditoriasGlosasStr);
						logger.info("===> codigo = "+detalleAuditoriasGlosas.getCodigo());
						/*
						 * Preparación de la consulta para los asocios de la auditorias de glosas (Asocio de una auditoria de glosa)
						 */
						PreparedStatementDecorator pstAsocioAuditoriasGlosas =  new PreparedStatementDecorator(con.prepareStatement(consultarAsociosAuditoriasGlosasStr, 
							ConstantesBD.typeResultSet,	ConstantesBD.concurrencyResultSet ));
						pstAsocioAuditoriasGlosas.setInt(1, codigoTarifarioOficial);
						pstAsocioAuditoriasGlosas.setInt(2, Utilidades.convertirAEntero(detalleAuditoriasGlosas.getCodigo()));
						ResultSetDecorator rsAsocioAudiGlosa = new ResultSetDecorator(pstAsocioAuditoriasGlosas.executeQuery());
											
						while(rsAsocioAudiGlosa.next())
						{
							//Se inician los títulos de la información pre y post modificación de la Auditoría de Glosa
							logDespuesInterno+="\nAsocio Auditorias Glosas\n";
							logPrevioInterno+="\nAsocio Auditorias Glosas\n";
							
							DtoDetalleAsociosGlosa asociosAuditoriasGlosas = new DtoDetalleAsociosGlosa();
							
							//String para Creación del Log - Fase de Asocios Auditorias Glosas
							logPrevioInterno+=	"Codigo: "+asociosAuditoriasGlosas.getCodigo()+"\n" +
												"Asocio Detalle Factura"+asociosAuditoriasGlosas.getCodigoAsocioDetalleFactura()+"\n" +
												"Codigo Detalle Glosa: "+asociosAuditoriasGlosas.getCodigoDetalleGlosa()+"\n"+
												"Nombre Asocio: "+asociosAuditoriasGlosas.getNombreAsocio()+"\n"+
												"Codigo Medico: "+asociosAuditoriasGlosas.getCodigoProfesional()+"\n"+
												"Nombre Medico: "+asociosAuditoriasGlosas.getNombreProfesional()+"\n"+
												"Valor Glosa: "+asociosAuditoriasGlosas.getValorGlosaStr()+"\n"+
												"Codigo Servicio Asocio: "+asociosAuditoriasGlosas.getCodigoPropietarioServicioAsocio()+"\n" +
												"Cantidad: "+asociosAuditoriasGlosas.getCantidad()+"\n" +
												"Valor: "+asociosAuditoriasGlosas.getValorStr()+"\n" +
												"Codigo Propietario Servicio Asocio: "+asociosAuditoriasGlosas.getCodigoPropietarioServicioAsocio()+"\n\n";
							
							asociosAuditoriasGlosas.setCodigo(rsAsocioAudiGlosa.getString("codigo"));
							asociosAuditoriasGlosas.setCodigoAsocioDetalleFactura(rsAsocioAudiGlosa.getString("asocio_det_factura"));
							asociosAuditoriasGlosas.setCodigoDetalleGlosa(rsAsocioAudiGlosa.getString("det_auditoria_glosa"));
							asociosAuditoriasGlosas.setCodigoAsocio(rsAsocioAudiGlosa.getInt("tipo_asocio"));
							asociosAuditoriasGlosas.setNombreAsocio(rsAsocioAudiGlosa.getString("nombre_asocio"));
							asociosAuditoriasGlosas.setCodigoProfesional(Utilidades.convertirAEntero(rsAsocioAudiGlosa.getString("codigo_medico")));
							asociosAuditoriasGlosas.setNombreProfesional(rsAsocioAudiGlosa.getString("nombre_medico"));
							if(!rsAsocioAudiGlosa.getString("cantidad_glosa").equals(""))
								asociosAuditoriasGlosas.setCantidadGlosa(Utilidades.convertirAEntero(rsAsocioAudiGlosa.getString("cantidad_glosa")));
							asociosAuditoriasGlosas.setValorGlosa(Utilidades.convertirADouble(rsAsocioAudiGlosa.getString("valor_glosa"), true));
							asociosAuditoriasGlosas.setValorGlosaStr(rsAsocioAudiGlosa.getString("valor_glosa"));
							asociosAuditoriasGlosas.setCodigoServicioAsocio(rsAsocioAudiGlosa.getInt("servicio_asocio"));
							asociosAuditoriasGlosas.setCantidad(rsAsocioAudiGlosa.getInt("cantidad"));
							asociosAuditoriasGlosas.setValor(Utilidades.convertirADouble(rsAsocioAudiGlosa.getString("valor"), true));
							asociosAuditoriasGlosas.setValorStr(rsAsocioAudiGlosa.getString("valor"));
							asociosAuditoriasGlosas.setCodigoPropietarioServicioAsocio(rsAsocioAudiGlosa.getString("codigo_prop_servicio_asocio"));
							
							//String para Creación del Log - Fase de Asocios Auditorias Glosas
							logDespuesInterno+=	"Codigo: "+asociosAuditoriasGlosas.getCodigo()+"\n" +
												"Asocio Detalle Factura"+asociosAuditoriasGlosas.getCodigoAsocioDetalleFactura()+"\n" +
												"Codigo Detalle Glosa: "+asociosAuditoriasGlosas.getCodigoDetalleGlosa()+"\n"+
												"Nombre Asocio: "+asociosAuditoriasGlosas.getNombreAsocio()+"\n"+
												"Codigo Medico: "+asociosAuditoriasGlosas.getCodigoProfesional()+"\n"+
												"Nombre Medico: "+asociosAuditoriasGlosas.getNombreProfesional()+"\n"+
												"Valor Glosa: "+asociosAuditoriasGlosas.getValorGlosaStr()+"\n"+
												"Codigo Servicio Asocio: "+asociosAuditoriasGlosas.getCodigoPropietarioServicioAsocio()+"\n" +
												"Cantidad: "+asociosAuditoriasGlosas.getCantidad()+"\n" +
												"Valor: "+asociosAuditoriasGlosas.getValorStr()+"\n" +
												"Codigo Propietario Servicio Asocio: "+asociosAuditoriasGlosas.getCodigoPropietarioServicioAsocio()+"\n\n";
							
							logger.info("===> Entré al SqlBase Parte 7");
							logger.info("===> consultarConceptosAsociosAudiGlosasStr = "+consultarConceptosAsociosAudiGlosasStr);
							logger.info("===> codigo = "+asociosAuditoriasGlosas.getCodigo());
							/*
							 * Preparación de la consulta para los conceptos de asocios para las auditorías de glosas
							 * (Concepto asocio de una auditoria de glosa)
							 */
							PreparedStatementDecorator pstConceptoAsocioAuditoriasGlosas =  new PreparedStatementDecorator(con.prepareStatement(consultarConceptosAsociosAudiGlosasStr, 
								ConstantesBD.typeResultSet,	ConstantesBD.concurrencyResultSet ));
							pstConceptoAsocioAuditoriasGlosas.setInt(1, Utilidades.convertirAEntero(asociosAuditoriasGlosas.getCodigo()));
							ResultSetDecorator rsConceptoAsocioAudiGlosa = new ResultSetDecorator(pstConceptoAsocioAuditoriasGlosas.executeQuery());
							
							
							logger.info("===> Entré al SqlBase Parte 6");
							//Se inician los títulos de la información pre y post modificación de los Asocios Auditorias Glosas
							logDespuesInterno+="\n Asocios Auditorias Glosas \n";
							logPrevioInterno+="\n Asocios Auditorias Glosas \n";
							
							while(rsConceptoAsocioAudiGlosa.next())
							{
								DtoConceptoGlosa conceptoAsociosAuditoriasGlosas = new DtoConceptoGlosa();
								
								logPrevioInterno+=	"Codigo: "+conceptoAsociosAuditoriasGlosas.getCodigo()+"\n" +
													"Codigo Detalle Asocio Auditoria Glosas: "+conceptoAsociosAuditoriasGlosas.getCodigoDetalleAsocioGlosa()+"\n" +
													"Concepto Glosa: "+conceptoAsociosAuditoriasGlosas.getCodigoConcepto()+"\n" +
													"Descripción: "+conceptoAsociosAuditoriasGlosas.getDescripcion()+"\n" +
													"Tipo: "+conceptoAsociosAuditoriasGlosas.getTipo()+"\n\n";
								
								conceptoAsociosAuditoriasGlosas.setCodigo(rsConceptoAsocioAudiGlosa.getString("codigo"));
								conceptoAsociosAuditoriasGlosas.setCodigoDetalleAsocioGlosa(
									rsConceptoAsocioAudiGlosa.getString("asocio_auditoria_glosa"));
								conceptoAsociosAuditoriasGlosas.setCodigoConcepto(rsConceptoAsocioAudiGlosa.getString("concepto_glosa"));
								conceptoAsociosAuditoriasGlosas.setCodigoInstitucion(rsConceptoAsocioAudiGlosa.getInt("institucion"));
								conceptoAsociosAuditoriasGlosas.setDescripcion(rsConceptoAsocioAudiGlosa.getString("descripcion"));
								conceptoAsociosAuditoriasGlosas.setTipo(rsConceptoAsocioAudiGlosa.getString("tipo"));
								asociosAuditoriasGlosas.getConceptos().add(conceptoAsociosAuditoriasGlosas);
								
								logPrevioInterno+=	"Codigo: "+conceptoAsociosAuditoriasGlosas.getCodigo()+"\n" +
													"Codigo Detalle Asocio Auditoria Glosas: "+conceptoAsociosAuditoriasGlosas.getCodigoDetalleAsocioGlosa()+"\n" +
													"Concepto Glosa: "+conceptoAsociosAuditoriasGlosas.getCodigoConcepto()+"\n" +
													"Descripción: "+conceptoAsociosAuditoriasGlosas.getDescripcion()+"\n" +
													"Tipo: "+conceptoAsociosAuditoriasGlosas.getTipo()+"\n\n";
							}
							
							
							detalleAuditoriasGlosas.getAsocios().add(asociosAuditoriasGlosas);
							
							
						}
						logPrevio+=logPrevioInterno;
						logDespues+=logDespuesInterno;
											
						factura.getDetalle().add(detalleAuditoriasGlosas);
					}
					
					
					glosa.getFacturas().add(factura);
				}
			}
			//*******************GENERACION DEL LOG
		    String log = "";
			int tipoLog=ConstantesBD.tipoRegistroLogModificacion;
			String separador = System.getProperty("file.separator");
			log = "\n  ============LOG TIPO ARCHIVO=========== \n" +
					"\nFecha: "+UtilidadFecha.getFechaActual()+
					"\nHora: "+UtilidadFecha.getHoraActual()+
					"\nRuta del archivo plano:"+ConstantesBD.logFolderModuloGlosas
					+ separador + ConstantesBD.logFolderModificacionInsercionGlosa
					+ separador + ConstantesBD.logModificacionPreglosaNombre+
					"\n\n************INFORMACION PREVIA A LA MODIFICACIÓN DE LA GLOSA-PREGLOSA*************" +
					"\n"+logPrevio+
					"\n\n\n************INFORMACION DESPUÉS DE LA MODIFICACIÓN LA GLOSA-PREGLOSA*************" +
					"\n"+logDespues;
		    
			logger.info("\n\n\nSE GENERA EL LOG TIPO ARCHIVO EN: "+ConstantesBD.logFolderModuloGlosas
					+ separador + ConstantesBD.logFolderModificacionInsercionGlosa
					+ separador + ConstantesBD.logModificacionPreglosaNombre+"\n\n\n");
			
			
			LogsAxioma.enviarLog(ConstantesBD.logModificacionPreglosaCodigo,log,tipoLog,usuario.getLoginUsuario());
		    
			
		    //*******************FIN GENERACION DEL LOG
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarGlosa: "+e);
		}
		logger.info("===> Voy a retornar a glosa !!!");
		return glosa;
	}
	
	/**
	 * Método para precargar la glosa cuando se piensa hacer una devolucion
	 * @param con
	 * @param glosa
	 * @return
	 */
	public static DtoGlosa cargarGlosaDevolucion(Connection con,DtoGlosa glosa, String forma)
	{
		try
		{
			int codigoTarifarioOficial = Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(glosa.getCodigoInstitucion()),true);
			int posFactura = glosa.getPosicionFactura();
			
			//Inicialmente se elimina todo el detalle de la factura
			for(DtoDetalleFacturaGlosa detalle:glosa.getFacturas().get(posFactura).getDetalle())
				detalle.setEliminado(true);
			
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarDetalleFacturaGlosaDevolucionStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(glosa.getCodigoInstitucion()), true));
			pst.setInt(2,Utilidades.convertirAEntero(glosa.getFacturas().get(posFactura).getCodigo()));
			pst.setInt(3,Utilidades.convertirAEntero(glosa.getFacturas().get(posFactura).getCodigoFactura()));
			
			logger.info("cargar detalle factura devolucion: "+cargarDetalleFacturaGlosaDevolucionStr+", codigo tarifario: "+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(glosa.getCodigoInstitucion())+", auditoria_glosa: "+glosa.getFacturas().get(posFactura).getCodigo()+", factura: "+glosa.getFacturas().get(posFactura).getCodigoFactura());
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{
				int posicion = ConstantesBD.codigoNuncaValido;
				String codigoDetalleFacturaSolicitud = rs.getString("det_factura_solicitud");
				
				//Se verifica si el detalle de la factura ya está en el arreglo del DTO
				for(int i=0;i<glosa.getFacturas().get(posFactura).getDetalle().size();i++)
					if(glosa.getFacturas().get(posFactura).getDetalle().get(i).getCodigoDetalleFacturaSolicitud().equals(codigoDetalleFacturaSolicitud))
					{
						posicion = i;
						glosa.getFacturas().get(posFactura).getDetalle().get(i).setEliminado(false);
						//La cantidad y el valor de la glosa deben ser los mismos de la cantidad y valor de la factura
						glosa.getFacturas().get(posFactura).getDetalle().get(i).setCantidad(rs.getInt("cantidad"));
						glosa.getFacturas().get(posFactura).getDetalle().get(i).setValorStr(rs.getString("valor"));
						glosa.getFacturas().get(posFactura).getDetalle().get(i).setCantidadGlosa(rs.getInt("cantidad"));
						glosa.getFacturas().get(posFactura).getDetalle().get(i).setValorGlosaStr(rs.getString("valor"));
						glosa.getFacturas().get(posFactura).getDetalle().get(i).setCodigoTipoSolicitud(rs.getInt("codigo_tipo_solicitud"));
						glosa.getFacturas().get(posFactura).getDetalle().get(i).setNombreTipoSolicitud(rs.getString("nombre_tipo_solicitud"));
						
						//Se limpian los conceptos del detalle
						glosa.getFacturas().get(posFactura).getDetalle().get(i).getConceptos().clear();
						for(DtoConceptoGlosa concepto:glosa.getFacturas().get(posFactura).getDetalle().get(i).getConceptos())
							concepto.setEliminado(true);
						
						//Se pasan los conceptos de la factura al detalle
						for(DtoConceptoGlosa concepto:glosa.getFacturas().get(posFactura).getConceptos())
						{
							DtoConceptoGlosa nuevoConcepto = new DtoConceptoGlosa();
							nuevoConcepto.setCodigoConcepto(concepto.getCodigoConcepto());
							nuevoConcepto.setDescripcion(concepto.getDescripcion());
							nuevoConcepto.setCodigoInstitucion(concepto.getCodigoInstitucion());
							nuevoConcepto.setTipo(concepto.getTipo());
							glosa.getFacturas().get(posFactura).getDetalle().get(i).getConceptos().add(nuevoConcepto);
						}
						
						
						//Se verifica si el detalle tiene asocios
						for(int j=0; j<glosa.getFacturas().get(posFactura).getDetalle().get(i).getAsocios().size();j++)
						{
							glosa.getFacturas().get(posFactura).getDetalle().get(i).getAsocios().get(j).setCantidadGlosa(glosa.getFacturas().get(posFactura).getDetalle().get(i).getAsocios().get(j).getCantidad());
							glosa.getFacturas().get(posFactura).getDetalle().get(i).getAsocios().get(j).setValorGlosaStr(glosa.getFacturas().get(posFactura).getDetalle().get(i).getAsocios().get(j).getValorStr());
							
							for(DtoConceptoGlosa concepto:glosa.getFacturas().get(posFactura).getDetalle().get(i).getAsocios().get(j).getConceptos())
								concepto.setEliminado(true);
							
							for(DtoConceptoGlosa concepto:glosa.getFacturas().get(posFactura).getConceptos())
							{
								DtoConceptoGlosa nuevoConcepto = new DtoConceptoGlosa();
								nuevoConcepto.setCodigoConcepto(concepto.getCodigoConcepto());
								nuevoConcepto.setDescripcion(concepto.getDescripcion());
								nuevoConcepto.setCodigoInstitucion(concepto.getCodigoInstitucion());
								nuevoConcepto.setTipo(concepto.getTipo());
								glosa.getFacturas().get(posFactura).getDetalle().get(i).getAsocios().get(j).getConceptos().add(nuevoConcepto);
							}
						}
					}
				
				//Si no se existía ese detalle se crea un nuevo DTO
				if(posicion==ConstantesBD.codigoNuncaValido)
				{
					DtoDetalleFacturaGlosa detalle = new DtoDetalleFacturaGlosa();
					detalle.setCodigo(rs.getString("codigo"));
					detalle.setCodigoGlosa(rs.getString("auditoria_glosa"));
					detalle.setCodigoDetalleFacturaSolicitud(rs.getString("det_factura_solicitud"));
					detalle.setNumeroSolicitud(rs.getString("solicitud"));
					detalle.setConsecutivoOrden(rs.getString("consecutivo_solicitud"));
					detalle.setDescripcionServicioArticulo(rs.getString("descripcion_articulo_servicio"));
					detalle.setCantidad(rs.getInt("cantidad"));
					detalle.setValorStr(rs.getString("valor"));
					detalle.setValor(Utilidades.convertirADouble(rs.getString("valor"),true));
					//La cantidad y el valor de la glosa deben ser los mismos de la cantidad y valor de la factura
					detalle.setCantidadGlosa(detalle.getCantidad());
					detalle.setValorGlosaStr(detalle.getValorStr());
					detalle.setValorGlosa(detalle.getValor());
					
					detalle.setCodigoServicioArticulo(rs.getString("codigo_articulo_servicio"));
					detalle.setEsServicio(UtilidadTexto.getBoolean(rs.getString("es_servicio")));
					detalle.setCodigoTipoSolicitud(rs.getInt("codigo_tipo_solicitud"));
					detalle.setNombreTipoSolicitud(rs.getString("nombre_tipo_solicitud"));
					
					//Se pasan los conceptos de la factura al detalle
					for(DtoConceptoGlosa concepto:glosa.getFacturas().get(posFactura).getConceptos())
					{
						DtoConceptoGlosa nuevoConcepto = new DtoConceptoGlosa();
						nuevoConcepto.setCodigoConcepto(concepto.getCodigoConcepto());
						nuevoConcepto.setDescripcion(concepto.getDescripcion());
						nuevoConcepto.setCodigoInstitucion(concepto.getCodigoInstitucion());
						nuevoConcepto.setTipo(concepto.getTipo());
						detalle.getConceptos().add(nuevoConcepto);
					}
					
					
					//Se carga el detalle de los asocios del detalle (SOLO APLICA PARA CIRUGIAS)
					if(detalle.getCodigoTipoSolicitud()==ConstantesBD.codigoTipoSolicitudCirugia)
					{
						if(!forma.equals("RegistrarModificarGlosas"))
						{
							PreparedStatementDecorator pst0 =  new PreparedStatementDecorator(con.prepareStatement(cargarAsociosDetalleFacturaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
							pst0.setInt(1,codigoTarifarioOficial);
							pst0.setInt(2,Utilidades.convertirAEntero(detalle.getCodigoDetalleFacturaSolicitud()));
							
							ResultSetDecorator rs0 = new ResultSetDecorator(pst.executeQuery());
							
							while(rs0.next())
							{
								DtoDetalleAsociosGlosa asocio = new DtoDetalleAsociosGlosa();
								asocio.setCodigoAsocioDetalleFactura(rs0.getString("asocio_det_factura"));
								asocio.setCodigoAsocio(rs0.getInt("tipo_asocio"));
								asocio.setNombreAsocio(rs0.getString("nombre_asocio"));
								asocio.setCodigoProfesional(rs0.getInt("codigo_medico"));
								asocio.setNombreProfesional(rs0.getString("nombre_medico"));
								asocio.setCodigoServicioAsocio(rs0.getInt("servicio_asocio"));
								//La cantidad y el valor de la glosa deben ser los mismos de la cantidad y valor de la factura
								asocio.setCantidad(rs0.getInt("cantidad"));
								asocio.setValor(rs0.getDouble("valor"));
								asocio.setValorStr(rs0.getString("valor"));
								asocio.setCantidadGlosa(asocio.getCantidad());
								asocio.setValorGlosa(asocio.getValor());
								asocio.setValorGlosaStr(asocio.getValorStr());
								asocio.setCodigoPropietarioServicioAsocio(rs0.getString("codigo_prop_servicio_asocio"));
								
								//Se pasan los conceptos de la factura al detalle del asocio
								for(DtoConceptoGlosa concepto:glosa.getFacturas().get(posFactura).getConceptos())
								{
									DtoConceptoGlosa nuevoConcepto = new DtoConceptoGlosa();
									nuevoConcepto.setCodigoConcepto(concepto.getCodigoConcepto());
									nuevoConcepto.setDescripcion(concepto.getDescripcion());
									nuevoConcepto.setCodigoInstitucion(concepto.getCodigoInstitucion());
									nuevoConcepto.setTipo(concepto.getTipo());
									asocio.getConceptos().add(nuevoConcepto);
								}
								detalle.getAsocios().add(asocio);
							}
						}
						else
						{
							PreparedStatementDecorator pst0 =  new PreparedStatementDecorator(con.prepareStatement(cargarAsociosDetalleFacturaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
							pst0.setInt(1,codigoTarifarioOficial);
							pst0.setInt(2,Utilidades.convertirAEntero(detalle.getCodigoDetalleFacturaSolicitud()));
							
							ResultSetDecorator rs0;
							HashMap<String,Object> resultado= new HashMap<String,Object> ();
							resultado= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst0.executeQuery()), true, true);
							
							logger.info("\n\nRESULTADO----------->"+resultado);
						}
						
					}
					
					
					glosa.getFacturas().get(posFactura).getDetalle().add(detalle);
				}
						
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarGlosaDevolucion: "+e);
		}
		
		return glosa;
	}
	
	/**
	 * Método para cargar las solicitudes de la factura
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static HashMap<String, Object> cargarSolicitudesFactura(Connection con,HashMap<String, Object> criterios)
	{
		logger.info("\n entre a cargarSolicitudesFactura criterios--> "+criterios);
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		try
		{
			//*************SE TOMAN LOS PARÀMETROS***************************************
			int codigoFactura = Utilidades.convertirAEntero(criterios.get("codigoFactura").toString());
			String codigosDetalleFactura = criterios.get("codigosDetalleFactura").toString();
			int codigoTarifarioOficial = Utilidades.convertirAEntero(criterios.get("codigoTarifarioOficial").toString(), true);
			int numeroSolicitud = Utilidades.convertirAEntero(criterios.get("numeroSolicitud").toString());
			String codigoServicio = criterios.get("codigoServicio").toString();
			String descripcionServicio = criterios.get("descripcionServicio").toString();
			int codigoArticulo = Utilidades.convertirAEntero(criterios.get("codigoArticulo").toString());
			String descripcionArticulo = criterios.get("descripcionArticulo").toString();
			String codigoEstandarBusquedaArticulo = criterios.get("codigoEstandarBusquedaArticulo")+"";
			//***************************************************************************
			
			String consulta = cargarSolicitudesFacturaStr ;
			
			if(!codigosDetalleFactura.equals(""))
				consulta += " and dfs.codigo not in ("+codigosDetalleFactura+") ";
			
			if(numeroSolicitud>0)
				consulta += " and getconsecutivosolicitud(dfs.solicitud) =  '"+numeroSolicitud+"' ";
			
			if(!codigoServicio.equals(""))
				consulta += " and upper(getcodigopropservicio2(dfs.servicio,"+codigoTarifarioOficial+")) like upper('%"+codigoServicio+"%') ";
			
			if(!descripcionServicio.equals(""))
				consulta += " and upper(getnombreservicio(dfs.servicio, "+ConstantesBD.codigoTarifarioCups+")) like upper('%"+descripcionServicio+"%') ";
							
			
			if(!descripcionArticulo.equals(""))
				consulta += " and upper(getdescarticulo(dfs.articulo)) like upper('%"+descripcionArticulo+"%') ";
			
			if(codigoArticulo>0)
			{			
				if(codigoEstandarBusquedaArticulo.equals(ConstantesIntegridadDominio.acronimoAxioma))
					consulta += " and a.codigo = "+codigoArticulo;
				else if(codigoEstandarBusquedaArticulo.equals(ConstantesIntegridadDominio.acronimoInterfaz))
					consulta += " and upper(a.codigo_interfaz) like upper('%"+codigoArticulo+"%')" ;
			}			
			
			// Modificado por tarea [id=83959]
			// consulta += " ORDER BY consecutivo_solicitud";
			// 
			consulta += " ORDER BY descripcion_articulo_servicio ";
			
			
			logger.info("\n\n CONSULTA-->"+consulta+"  codigoEstandarBusquedaArticulo->"+codigoEstandarBusquedaArticulo+" codigoTarifarioOficial->"+codigoTarifarioOficial+" codigoFactura->"+codigoFactura+"   \n\n");
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setString(1,codigoEstandarBusquedaArticulo);
			pst.setInt(2,codigoTarifarioOficial);
			pst.setInt(3,codigoTarifarioOficial);
			pst.setInt(4,codigoFactura);
			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			
			
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarSolicitudesFactura: "+e);
			e.printStackTrace();
		}
		return resultados;
	}
	
	/**
	 * Método para cargar los asocios del detalle de la factura
	 * @param con
	 * @param codigoDetalleFactura
	 * @param codigoTarifarioOficial
	 * @return
	 */
	public static ArrayList<DtoDetalleAsociosGlosa> cargarAsociosDetalleFactura(Connection con,String codigoDetalleFactura,int codigoTarifarioOficial)
	{
		ArrayList<DtoDetalleAsociosGlosa> resultados = new ArrayList<DtoDetalleAsociosGlosa>();
		try
		{
			logger.info("CONSULTA------------------->"+cargarAsociosDetalleFacturaStr+"\n\nPARAMETRO_------->"+codigoDetalleFactura);
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarAsociosDetalleFacturaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoTarifarioOficial);
			pst.setInt(2,Utilidades.convertirAEntero(codigoDetalleFactura));
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{
				DtoDetalleAsociosGlosa asocio = new DtoDetalleAsociosGlosa();
				asocio.setCodigoAsocioDetalleFactura(rs.getString("asocio_det_factura"));
				asocio.setCodigoAsocio(rs.getInt("tipo_asocio"));
				asocio.setNombreAsocio(rs.getString("nombre_asocio"));
				asocio.setCodigoProfesional(rs.getInt("codigo_medico"));
				asocio.setNombreProfesional(rs.getString("nombre_medico"));
				asocio.setCodigoServicioAsocio(rs.getInt("servicio_asocio"));
				asocio.setCodigoPropietarioServicioAsocio(rs.getString("codigo_prop_servicio_asocio"));
				asocio.setCantidad(rs.getInt("cantidad"));
				//asocio.setCantidadGlosa(asocio.getCantidad());
				asocio.setValorStr(rs.getString("valor"));
				asocio.setValor(Double.parseDouble(asocio.getValorStr()));
				//asocio.setValorGlosa(asocio.getValor());
				//asocio.setValorGlosaStr(asocio.getValorStr());
				resultados.add(asocio);
				
			}
			//Utilidades.imprimirArrayList(resultados);
			
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarAsociosDetalleFactura: "+e);
		}
		return resultados;
	}
	
	//*************************************************************************************************************************
	//****************************MÉTODOS PARA GENERAR GLOSAS DE AUDITORIA*****************************************************
	//***************************************************************************************************************************
	
	/**
	 * Método para consultar las facturas auditadas
	 */
	public static HashMap<String, Object> consultarFacturasAuditadas(Connection con,HashMap<String, Object> criterios, ArrayList<Integer> listaConvenios)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		try
		{
			//*********************SE TOMAN PARÁMETROS************************************
			String fechaAuditoriaInicial = UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaAuditoriaInicial").toString());
			String fechaAuditoriaFinal = UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaAuditoriaFinal").toString());
			String facturaInicial = criterios.get("facturaInicial").toString();
			String facturaFinal = criterios.get("facturaFinal").toString();
			int codigoConvenio = Utilidades.convertirAEntero(criterios.get("codigoConvenio").toString());
			int codigoContrato = Utilidades.convertirAEntero(criterios.get("codigoContrato").toString());
			int numeroPreGlosa = Utilidades.convertirAEntero(criterios.get("numeroPreGlosa").toString());
			int codigoInstitucion = Utilidades.convertirAEntero(criterios.get("codigoInstitucion").toString());
			//*****************************************************************************
			
			String consulta = consultaFacturasAuditadasStr;
			String seccionWHERE = "";
			
			
			if(!fechaAuditoriaInicial.equals("")&&!fechaAuditoriaFinal.equals(""))
				seccionWHERE = (seccionWHERE.equals("")?"":" AND ") + " rg.fecha_auditoria between '"+fechaAuditoriaInicial+"' and '"+fechaAuditoriaFinal+"' ";
			if(numeroPreGlosa>0)
				seccionWHERE += (seccionWHERE.equals("")?"":" AND ") + " rg.glosa_sistema = '"+numeroPreGlosa+"' ";
			if(codigoConvenio>0)
				seccionWHERE += (seccionWHERE.equals("")?"":" AND ") + " rg.convenio = "+codigoConvenio+" ";
			seccionWHERE += (seccionWHERE.equals("")?"":" AND ") + " rg.estado = '"+ConstantesIntegridadDominio.acronimoEstadoAuditado+"' and rg.institucion = "+codigoInstitucion+"  ";
				
			if(codigoContrato>0)
				seccionWHERE += (seccionWHERE.equals("")?"":" AND ") + " ag.contrato = "+codigoContrato+" ";
			if(!facturaInicial.equals("")&&!facturaFinal.equals(""))
				seccionWHERE += (seccionWHERE.equals("")?"":" AND ") + " f.consecutivo_factura between "+facturaInicial+" and "+facturaFinal+" ";
			
			if(listaConvenios.size()>0 && codigoConvenio<=0)
				seccionWHERE+=" AND rg.convenio IN( "+UtilidadTexto.convertirArrayIntegerACodigosSeparadosXComas(listaConvenios)+")";
			
			if((criterios.get("action")+"").equals("ConsultarImpFactAudi"))
			{
				consulta += seccionWHERE + " ORDER BY c.nombre, f.consecutivo_factura ";
			}
			else if((criterios.get("action")+"").equals("GenerarGlosaAuditoria"))
			{
				seccionWHERE += " AND f.numero_cuenta_cobro IS NULL ";
				consulta += seccionWHERE + " AND f.estado_facturacion= "+ConstantesBD.codigoEstadoFacturacionFacturada+" ORDER BY f.consecutivo_factura ";
			}			
			
			logger.info("consultarFacturasAuditadas | "+consulta);
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)), true, true);
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarFacturasAuditadas: "+e);
			resultados.put("numRegistros", "0");
		}
		return resultados;
		
	}

	/**
	 * @return the consultaFacturasAuditadasStr
	 */
	public static String getConsultaFacturasAuditadasStr() {
		return consultaFacturasAuditadasStr;
	}

	/**
	 * 
	 * @param codigoAuditoria
	 * @param observaciones
	 * @return
	 */
	public static boolean actualizarObservacionAuditoria(String codigoAuditoria, String observaciones) 
	{
		String cadena="UPDATE registro_glosas SET observaciones='"+observaciones+"' where codigo="+codigoAuditoria;
		Connection con = UtilidadBD.abrirConexion();
		boolean resultado=false;
		try
		{
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con,cadena);
			ps.executeUpdate();
			resultado=true;
		}
		catch(Exception e)
		{
			Log4JManager.error("error: ",e);
		}
		finally
		{
			UtilidadBD.closeConnection(con);
		}
		return resultado;
	}	
}