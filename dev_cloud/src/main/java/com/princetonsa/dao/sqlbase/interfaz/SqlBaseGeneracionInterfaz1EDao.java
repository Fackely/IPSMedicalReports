package com.princetonsa.dao.sqlbase.interfaz;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.interfaz.UtilidadesInterfaz;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.historiaClinica.DtoEspecialidad;
import com.princetonsa.dto.interfaz.DtoCuentaContable;
import com.princetonsa.dto.interfaz.DtoEventosParam1E;
import com.princetonsa.dto.interfaz.DtoInterfazCampoS1E;
import com.princetonsa.dto.interfaz.DtoInterfazDatosDocumentoS1E;
import com.princetonsa.dto.interfaz.DtoInterfazLineaS1E;
import com.princetonsa.dto.interfaz.DtoInterfazS1EInfo;
import com.princetonsa.dto.interfaz.DtoRetencion;
import com.princetonsa.dto.interfaz.DtoTiposInterfazDocumentosParam1E;
import com.princetonsa.enu.interfaz.CampoDocumentoContable;
import com.princetonsa.enu.interfaz.CampoLineaEvento;
import com.princetonsa.enu.interfaz.CampoLineaInicioFin;
import com.princetonsa.enu.interfaz.CampoMoviCuentasXCobrar;
import com.princetonsa.enu.interfaz.CampoMoviCuentasXPagar;
import com.princetonsa.enu.interfaz.CampoMoviDocumentoContable;
import com.princetonsa.enu.interfaz.TipoInconsistencia;
import com.princetonsa.mundo.interfaz.GeneracionInterfaz1E;

public class SqlBaseGeneracionInterfaz1EDao
{	
	private static Logger logger = Logger.getLogger(SqlBaseGeneracionInterfaz1EDao.class);
	private static boolean huboInconsistencia = false;
	private static boolean huboLineaDetMov = false;
	private static boolean huboLineaCxC = false;
	private static boolean huboLineaCxP = false;
	private static String mensajeEInconFaltaDefinirInfo = "Falta definir informacion";
	private static String mensajeEInconNoInfo = "No se encontro informacion";
	private static String mensajeEInconRestaRetencion = "Parametrizacion erronea en los porcentajes de retencion";
	
	//Marcar como usados.
	/**
	 * Cadena Sql que actualiza la tabla recibos_caja
	 */
	private static String marcarRecibosdeCaja="UPDATE recibos_caja SET contabilizado = '"+ConstantesBD.acronimoSi+"' WHERE numero_recibo_caja = ? ";

	
	/**
	 * Cadena Sql que actualiza la tabla anulacion_recibos_caja
	 */
	private static String marcarAnulacionRecibosdeCaja= "UPDATE anulacion_recibos_caja SET contabilizado = '"+ConstantesBD.acronimoSi+"' WHERE numero_anulacion_rc = ? ";
	
	/**
	 * Cadena Sql que actualiza la tabla recibos_caja
	 */
	private static String marcarDevolucionRecibosdeCaja="UPDATE devol_recibos_caja SET contabilizado = '"+ConstantesBD.acronimoSi+"' WHERE consecutivo = ?  ";
	
	/**
     * Cadena Sql que actualiza la tabla Autorizaciones entidades Subcontratadas
     */
	private static String marcarRegAutorSerEntidadesSub= "UPDATE autorizaciones_entidades_sub SET contabilizado = '"+ConstantesBD.acronimoSi+"' WHERE consecutivo_autorizacion = ? ";
	
	/**
     * Cadena Sql que actualiza la tabla Autorizaciones entidades Subcontratadas
     */
	private static String marcarRegAnulacionAutorSerEntidadesSub= "UPDATE autorizaciones_entidades_sub SET contabilizado_anulacion = '"+ConstantesBD.acronimoSi+"' WHERE consecutivo_autorizacion = ? ";
	
	/**
	 * Cadena Sql que actualiza la tabla despacho
	 */
	private static String marcarDespachoMedicamentos=" UPDATE despacho SET contabilizado = '"+ConstantesBD.acronimoSi+"' WHERE orden = ? " ;
	
	
	/**
	 * Cadena Sql que actualiza la tabla devolucion medicamentos
	 */
	private static String marcarDevolucionMedicamentos= "UPDATE devolucion_med SET contabilizado = '"+ConstantesBD.acronimoSi+"' WHERE codigo = ? ";
	
	/**
	 * Cadena Sql que actualiza la tabla despacho pedidos
	 */
	private static String marcarDespachoPedidos= "UPDATE despacho_pedido SET contabilizado = '"+ConstantesBD.acronimoSi+"' WHERE consecutivo = ? " ;
	
	/**
	 * Cadena Sql que acutualiza la tabla devolucion pedidos
	 */
	private static String marcarDevolucionPedidos ="UPDATE devolucion_pedidos SET contabilizado = '"+ConstantesBD.acronimoSi+"' WHERE  codigo = ? ";
	
	
	/**
	 * Cadena Sql que actualiza la tabla despacho
	 */
	private static String marcarCargosDirectosArticulos = "UPDATE despacho SET contabilizado = '"+ConstantesBD.acronimoSi+"' WHERE  orden = ? ";

	
	/**
	 * Cadena Sql que actualiza la tabla anulacion cargos farmacia
	 */
	private static String marcarAnulacionCargosArticulos= "UPDATE anulacion_cargos_farmacia SET contabilizado = '"+ConstantesBD.acronimoSi+"' WHERE codigo = ? ";
	
	
	/**
	 * Cadena Sql que actualiza la tabla facturas 
	 */
	private static String marcarFacturasPacientes= "UPDATE facturas SET contabilizado = '"+ConstantesBD.acronimoSi+"'  WHERE consecutivo_factura = ? ";

    /**
     * Cadena Sql que actualiza la tabla facturas
     */
	private static String marcarAnulacionFacturasPacientes="UPDATE anulaciones_facturas  SET contabilizado = '"+ConstantesBD.acronimoSi+"' WHERE  consecutivo_anulacion = ? ";
	
	/**
	 * Cadena Sql que actualiza la tabla facuras_varias
	 */
	private static String marcarFacturasVarias="UPDATE facturas_varias  SET contabilizado = '"+ConstantesBD.acronimoSi+"'  WHERE consecutivo = ?  " ;
	
	/**
	 * Cadena Sql que actualiza la tabla facuras_varias
	 */
	private static String marcarAnulaFacturasVarias="UPDATE facturas_varias  SET contabilizado = '"+ConstantesBD.acronimoSi+"'  WHERE consecutivo = ?  " ;
                           
	/**
	 * Cadena Sql que actuliza la tabla ajus_facturas_varias 
	 */
	private static String marcarAjustesFacturasVarias ="UPDATE ajus_facturas_varias SET contabilizado = '"+ConstantesBD.acronimoSi+"' WHERE consecutivo = ? "; 
	
	/**
	 * Cadena Sql que actualiza la tabla cuentas_cobro_capitacion
	 */
	private static String marcarCuentasCobroCapitacion ="UPDATE cuentas_cobro_capitacion SET contabilizado = '"+ConstantesBD.acronimoSi+"' WHERE numero_cuenta_cobro = ? "; 
	
	/**
	 * Cadena Sql que actualiza la tabla ajustes_cxc
	 */
	private static String marcarAjustesCuentasCobroCapitacion= "UPDATE ajustes_cxc SET contabilizado = '"+ConstantesBD.acronimoSi+"' WHERE consecutivo = ?  ";

	/**
	 * Cadena Sql que actualiza la tabla ajustes_empresa
	 * */
	private static String marcarAjustesFacturasPaciente = "UPDATE cartera.ajustes_empresa SET contabilizado = '"+ConstantesBD.acronimoSi+"' WHERE consecutivo_ajuste = ?  ";
	//*****************************************************************************************************************
	
	/**
	 * cadena consulta tipos documentos por tipo movimiento 1e
	 * */
	private static final String strConsultaTipoDocxTipoMov1e = "" +
			"SELECT " +
			"tipo_documento," +
			"tipo_movimiento " +
			"FROM " +
			"tipos_doc_x_tipo_mov_1e " +
			"WHERE " +
			"tipo_movimiento IN ("+ConstantesBD.separadorSplit+"1) " +
			"ORDER BY tipo_movimiento,tipo_documento ";
	
	
	/**
	 * Cadena para consultar la información de los documentos de recaudos
	 */
	private static final String consultaDocumentosContablesRecaudosStr = "SELECT * from "+ 
		"(SELECT "+  
		ConstantesBD.codigoTipoDocInteReciboCaja+" as tipo_documento01, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteReciboCaja+") as descripcion_documento01, "+
		"rc.numero_recibo_caja as numero_documento01, "+
		ConstantesBD.tipoConsecutivoInteNumeroReciboCaja+" as tipo_consecutivo01, " +
		"to_char(rc.fecha,'"+ConstantesBD.formatoFechaAp+"') as fecha01, "+
		ConstantesBD.codigoNuncaValido+" as tipo_documento02, "+
		"'' as descripcion_documento02, "+
		"'' as numero_documento02, "+
		ConstantesBD.codigoNuncaValido+" as tipo_consecutivo02, " +
		"'' as fecha02," +
		"coalesce(ca.codigo,'') as codigo_centro_atencion, "+ 
		"rc.consecutivo_recibo AS consecutivo_documento01, " +
	    "'' AS consecutivo_documento02 " + 
		"FROM recibos_caja rc " +
		"LEFT OUTER JOIN centro_atencion ca ON(ca.consecutivo = rc.centro_atencion) "+ 
		"WHERE to_char(rc.fecha,'YYYY-MM-DD') = ? and (rc.contabilizado IS NULL OR rc.contabilizado = '"+ConstantesBD.acronimoNo+"') "+
		"UNION "+
		"SELECT "+ 
		ConstantesBD.codigoTipoDocInteAnulaRecCaja+" as tipo_documento01, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteAnulaRecCaja+") as descripcion_documento01, "+
		"arc.numero_anulacion_rc as numero_documento01, "+
		ConstantesBD.tipoConsecutivoInteNumeroAnulacion+" as tipo_consecutivo01, "+
		"coalesce(to_char(arc.fecha,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha01, "+
		ConstantesBD.codigoTipoDocInteReciboCaja+" as tipo_documento02, " +		
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteReciboCaja+") as descripcion_documento02, "+
		"rc.numero_recibo_caja as numero_documento02, "+
		ConstantesBD.tipoConsecutivoInteNumeroReciboCaja+" as tipo_consecutivo02, "+
		"to_char(rc.fecha,'"+ConstantesBD.formatoFechaAp+"') as fecha02, "+
		"coalesce(ca.codigo,'') as codigo_centro_atencion, "+
		"arc.consecutivo_anulacion AS consecutivo_documento01, " +
	    "rc.consecutivo_recibo AS consecutivo_documento02 " +
		"FROM anulacion_recibos_caja arc "+ 
		"INNER JOIN recibos_caja rc ON(rc.numero_recibo_caja = arc.numero_recibo_caja and rc.institucion = arc.institucion) "+
		"LEFT OUTER JOIN centro_atencion ca ON(ca.consecutivo = rc.centro_atencion) "+
		"WHERE to_char(arc.fecha,'YYYY-MM-DD') = ? and (arc.contabilizado IS NULL OR arc.contabilizado = '"+ConstantesBD.acronimoNo+"') " +
		"UNION " +
		"SELECT " +
		ConstantesBD.codigoTipoDocInteDevolRecibosCaja+" as tipo_documento01, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteDevolRecibosCaja+") as descripcion_documento01, "+
		"drc.consecutivo||'' as numero_documento01, "+
		ConstantesBD.tipoConsecutivoInteNumeroDevolucion +" as tipo_consecutivo01, " +
		"to_char(drc.fecha_aprobacion,'"+ConstantesBD.formatoFechaAp+"') as fecha01, "+
		ConstantesBD.codigoTipoDocInteReciboCaja+" as tipo_documento02, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteReciboCaja+") as descripcion_documento02, "+
		"rc.numero_recibo_caja as numero_documento02, "+
		ConstantesBD.tipoConsecutivoInteNumeroReciboCaja+" as tipo_consecutivo02, " +
		"to_char(rc.fecha,'"+ConstantesBD.formatoFechaAp+"') as fecha02, "+
		"coalesce(ca.codigo,'') as codigo_centro_atencion, "+
		"drc.consecutivo||'' AS consecutivo_documento01, " +
	    "rc.consecutivo_recibo AS consecutivo_documento02 " +
		"FROM tesoreria.devol_recibos_caja drc "+
		"INNER JOIN recibos_caja rc ON(rc.numero_recibo_caja = drc.numero_rc and rc.institucion = drc.institucion) "+
		"LEFT OUTER JOIN centro_atencion ca ON(ca.consecutivo = rc.centro_atencion) "+
		"WHERE to_char(drc.fecha_aprobacion,'YYYY-MM-DD') = ? and drc.estado = '"+ConstantesIntegridadDominio.acronimoEstadoAprobado+"' and (drc.contabilizado IS NULL OR drc.contabilizado = '"+ConstantesBD.acronimoNo+"') " +
		") t";
	
	/**
	 * Método para consultar los datos del concepto de un recibo de caja
	 */
	private static final String consultaDatosConceptoReciboCaja = "SELECT " +
		"dcr.numero_id_beneficiario AS nit_tercero  ,"+
		"cit.codigo_tipo_ingreso          AS tipo_concepto, "+
		"cit.valor AS filtro," +
		"coalesce(getnumeroidentificaciontercero(cit.nit_homologacion),'') as nit " +
		"FROM detalle_conceptos_rc dcr "+
		"INNER JOIN conceptos_ing_tesoreria cit ON(cit.codigo= dcr.concepto AND cit.institucion= dcr.institucion) " +
		"WHERE dcr.numero_recibo_caja = ?";
	
	/**
	 * Método para obtener el tipo de regimen del convenio de un recibo de caja
	 */
	private static final String obtenerTipoRegimenConvenioReciboCajaStr = "SELECT " +
		"c.tipo_regimen as codigo_tipo_regimen," +
		"c.codigo as codigo_convenio, " +
		"c.tipo_contrato as codigo_tipo_contrato," +
		"c.tipo_convenio as tipo_convenio," +
		"c.institucion as institucion " +
		"FROM pagos_general_empresa pge " +
		"INNER JOIN convenios c ON(c.codigo = pge.convenio) " +
		"WHERE " +
		"pge.documento=? and " +
		"pge.tipo_doc = "+ConstantesBD.codigoTipoDocumentoPagosReciboCaja;
	
	/**
	 * Cadena para obtener el tipo de regiemen del convenio de unr ecibo de caja de concepto de paciente
	 */
	private static final String obtenerTipoRegimenPacienteReciboCajaStr = "SELECT " +
		"c.tipo_regimen as codigo_tipo_regimen," +
		"c.codigo as codigo_convenio, " +
		"c.tipo_contrato as codigo_tipo_contrato," +
		"c.tipo_convenio as tipo_convenio," +
		"c.institucion as institucion " +
		"FROM pagos_facturas_paciente pfp " +
		"INNER JOIN facturas f ON(f.codigo = pfp.factura) " +
		"INNER JOIN convenios c ON(c.codigo = f.convenio) " +
		"WHERE " +
		"pfp.documento=? and " +
		"pfp.tipo_doc = "+ConstantesBD.codigoTipoDocumentoPagosReciboCaja;
	
	
	//*************************************CONSULTAS JOSE******************************************************************************
	/**
	 *  consulta el detalle de pagos y conceptos de recibos de caja
	 * */
	//Se reemplaza por MT-1453
	/*private static final String consultaDetMovContableRecaudosStr = 
			"SELECT * FROM (  " +
			"SELECT " +
			"dprc.consecutivo," +
			"dprc.forma_pago," +
			"dprc.valor," +
			"coalesce(cc.cuenta_contable,'') AS cuenta_contable," +
			ConstantesBD.codigoNuncaValido+" AS codigo_tipo_ingreso," +
			"coalesce(cc.manejo_terceros||'','false') AS  manejo_terceros, " +
			"'' AS codigo_centro_costo, " +
			"coalesce(cc.manejo_centros_costo||'','false') AS manejo_centro_costo, " +
			"coalesce(dcrc.tipo_id_beneficiario,'') AS tipo_id_beneficiario, " +
			"coalesce(dcrc.numero_id_beneficiario,'') AS numero_id_beneficiario, " +
			"coalesce(dcrc.nombre_beneficiario,'') AS nombre_beneficiario " +
			"FROM detalle_pagos_rc dprc " +
			"INNER JOIN formas_pago fp ON (fp.consecutivo = dprc.forma_pago) " +
			"INNER JOIN detalle_conceptos_rc dcrc ON(dcrc.numero_recibo_caja = dprc.numero_recibo_caja) " +
			"LEFT OUTER JOIN cuentas_contables cc ON (cc.codigo = fp.cuenta_contable ) " +
			"WHERE dprc.numero_recibo_caja = ? " +
			" " +
			"UNION " +
			" " +
			"SELECT " +
			"dcrc.consecutivo," +
			ConstantesBD.codigoNuncaValido+" AS forma_pago," +
			"dcrc.valor, " +
			"coalesce(cc.cuenta_contable,'') as cuenta_contable," +
			"cit.codigo_tipo_ingreso," +
			"coalesce(cc.manejo_terceros||'','false') AS  manejo_terceros," +
			"coalesce(cco.codigo_interfaz,'') AS codigo_centro_costo, " +
			"coalesce(cc.manejo_centros_costo||'','false') AS manejo_centro_costo," +
			"coalesce(dcrc.tipo_id_beneficiario,'') AS tipo_id_beneficiario, " +
			"coalesce(dcrc.numero_id_beneficiario,'') AS numero_id_beneficiario, " +
			"coalesce(dcrc.nombre_beneficiario,'') AS nombre_beneficiario " +
			"FROM detalle_conceptos_rc dcrc " +
			"INNER JOIN conceptos_ing_tesoreria cit ON (cit.codigo = dcrc.concepto AND cit.institucion = dcrc.institucion) " +
			"LEFT OUTER JOIN cuentas_contables cc ON (cc.codigo = cit.cuenta) " +
			"LEFT OUTER JOIN centros_costo cco ON (cco.codigo = cit.codigo_centro_costo ) " +
			"WHERE dcrc.numero_recibo_caja = ? AND cit.codigo_tipo_ingreso = "+ConstantesBD.codigoTipoIngresoTesoreriaNinguno+" " +
			" ) t ORDER BY forma_pago ASC ";*/
	
	private static final String consultaDetMovContableRecaudosStr =  
		"SELECT * FROM (  " +
		  "SELECT dprc.consecutivo, " +
		    "dprc.forma_pago, " +
		    "dprc.valor, " +
		    "COALESCE(cc.cuenta_contable,'') AS cuenta_contable, " +
		    ConstantesBD.codigoNuncaValido + " AS codigo_tipo_ingreso, " +
		    "COALESCE(cc.manejo_terceros ||'','false') AS manejo_terceros, " +
		    "'' AS codigo_centro_costo, " +
		    "COALESCE(cc.manejo_centros_costo ||'','false') AS manejo_centro_costo, " +
		    "COALESCE(dcrc.tipo_id_beneficiario,'')   AS tipo_id_beneficiario, " +
		    "COALESCE(dcrc.numero_id_beneficiario,'') AS numero_id_beneficiario, " +
		    "COALESCE(dcrc.nombre_beneficiario,'')    AS nombre_beneficiario, " +
		    "cccomision.manejo_terceros AS manejo_terceros_comision, " +
		    "cccomision.manejo_centros_costo AS manejo_centro_costo_comision, " +
		    "COALESCE(cccomision.cuenta_contable,'') AS cuenta_cont_comision, " +
		    "ccretfte.manejo_terceros AS manejo_terceros_retefuente, " +
		    "COALESCE(ccretfte.cuenta_contable, '') AS cuenta_cont_retefuente, " +
		    "tercero.numero_identificacion AS tercero_tarjeta, " +
		    "CASE WHEN cxca.centro_atencion IS NOT NULL THEN cxca.comision ELSE tfc.comision END as comision, " +
		    "tf.retefte, " +
		    "COALESCE(ca.codigo,'') as centro_atencion_recibo, " +
		    "mt.numero_autorizacion " +
		  "FROM detalle_pagos_rc dprc " +
		  "INNER JOIN formas_pago fp " +
		  "ON (fp.consecutivo = dprc.forma_pago) " +
		  "INNER JOIN detalle_conceptos_rc dcrc " +
		  "ON(dcrc.numero_recibo_caja = dprc.numero_recibo_caja) " +
		  "LEFT OUTER JOIN recibos_caja rc " +
		  "ON(rc.numero_recibo_caja = dprc.numero_recibo_caja) " +
		  "LEFT OUTER JOIN centro_atencion ca " +
		  "ON(ca.consecutivo = rc.centro_atencion) " +
		  "LEFT OUTER JOIN cuentas_contables cc " +
		  "ON (cc.codigo = fp.cuenta_contable ) " +
		  "LEFT OUTER JOIN movimientos_tarjetas mt " +
		  "ON (mt.det_pago_rc = dprc.consecutivo ) " +
		  "LEFT OUTER JOIN entidades_financieras ef " +
		  "ON (ef.consecutivo = mt.entidad_financiera ) " +
		  "LEFT OUTER JOIN tarjetas_financieras tf " +
		  "ON (tf.consecutivo = mt.codigo_tarjeta ) " +
		  "LEFT OUTER JOIN cuentas_contables cccomision " +
		  "ON (cccomision.codigo = tf.cuenta_contable_comision ) " +
		  "LEFT OUTER JOIN cuentas_contables ccretfte " +
		  "ON (ccretfte.codigo = tf.cuenta_contable_retefte ) " + 
		  "LEFT OUTER JOIN tarjeta_financiera_comision tfc " +
		  "ON (tfc.tarjeta_financiera = tf.consecutivo AND tfc.entidad_financiera = mt.entidad_financiera) " +
		  "LEFT OUTER JOIN terceros tercero " +
		  "ON (tercero.codigo = ef.tercero ) " +
		  "LEFT OUTER JOIN comision_x_centro_atencion cxca " +
		  "ON (cxca.tarjeta_financiera_comision = tfc.codigo_pk ) " +
		  "WHERE dprc.numero_recibo_caja = ?" + 
		  " " +
		  "UNION " +
		  " " +
		  "SELECT " +
		  "dcrc.consecutivo," +
		  ConstantesBD.codigoNuncaValido+" AS forma_pago," +
		  "dcrc.valor, " +
		  "coalesce(cc.cuenta_contable,'') as cuenta_contable," +
		  "cit.codigo_tipo_ingreso," +
		  "coalesce(cc.manejo_terceros||'','false') AS  manejo_terceros," +
		  "coalesce(cco.codigo_interfaz,'') AS codigo_centro_costo, " +
		  "coalesce(cc.manejo_centros_costo||'','false') AS manejo_centro_costo," +
		  "coalesce(dcrc.tipo_id_beneficiario,'') AS tipo_id_beneficiario, " +
		  "coalesce(dcrc.numero_id_beneficiario,'') AS numero_id_beneficiario, " +
		  "coalesce(dcrc.nombre_beneficiario,'') AS nombre_beneficiario, " +
		  "0 AS manejo_terceros_comision, " +
		  "0 AS manejo_centro_costo_comision, " +
		  "'' AS cuenta_cont_comision, " +
		  "0 AS manejo_terceros_retefuente, " +
		  "'' AS cuenta_cont_retefuente, " +
		  "'' AS tercero_tarjeta, " +
		  "0 AS comision, " +
		  "0 AS retefte, " +
		  "'' AS centro_atencion_recibo, " +
		  "'' AS numero_autorizacion " +
		  "FROM detalle_conceptos_rc dcrc " +
		  "INNER JOIN conceptos_ing_tesoreria cit ON (cit.codigo = dcrc.concepto AND cit.institucion = dcrc.institucion) " +
		  "LEFT OUTER JOIN cuentas_contables cc ON (cc.codigo = cit.cuenta) " +
		  "LEFT OUTER JOIN centros_costo cco ON (cco.codigo = cit.codigo_centro_costo ) " +
		  "WHERE dcrc.numero_recibo_caja = ? AND cit.codigo_tipo_ingreso = "+ConstantesBD.codigoTipoIngresoTesoreriaNinguno+" " +
		  " ) t ORDER BY forma_pago ASC ";

	/**
	 *  consulta el detalle de pagos y conceptos de devolucion de recibos de caja
	 * */
	private static final String consultaDetMovContableRecaudosDevRcStr =
			"SELECT * FROM ( " +
			"SELECT " +
			"drc.consecutivo," +
			ConstantesBD.codigoNuncaValido+" AS forma_pago," +
			"drc.valor_devolucion AS valor," +
			"coalesce(cc.cuenta_contable,'') as cuenta_contable," +
			"cit.codigo_tipo_ingreso," +
			"coalesce(cc.manejo_terceros||'','false') AS  manejo_terceros," +
			"coalesce(cco.codigo_interfaz,'') AS codigo_centro_costo, " +
			"coalesce(cc.manejo_centros_costo||'','false') AS manejo_centro_costo," +
			"coalesce(dcrc.tipo_id_beneficiario,'') AS tipo_id_beneficiario, " +
			"coalesce(dcrc.numero_id_beneficiario,'') AS numero_id_beneficiario, " +
			"coalesce(dcrc.nombre_beneficiario,'') AS nombre_beneficiario " +
			"FROM devol_recibos_caja drc " +
			"INNER JOIN conceptos_ing_tesoreria cit ON (cit.codigo = drc.concepto AND cit.institucion = drc.institucion) " +
			"INNER JOIN detalle_conceptos_rc dcrc ON (dcrc.numero_recibo_caja = drc.numero_rc AND dcrc.concepto = drc.concepto) " +
			"LEFT OUTER JOIN cuentas_contables cc ON (cc.codigo = cit.cuenta) " +
			"LEFT OUTER JOIN centros_costo cco ON (cco.codigo = cit.codigo_centro_costo ) " +
			"WHERE drc.consecutivo = ? AND cit.codigo_tipo_ingreso = "+ConstantesBD.codigoTipoIngresoTesoreriaNinguno+" AND " +
			"(drc.contabilizado IS NULL OR drc.contabilizado = '"+ConstantesBD.acronimoNo+"') " +
			" " +
			"UNION " +
			" " +
			"SELECT " +
			"drc.consecutivo," +
			"drc.forma_pago," +
			"drc.valor_devolucion AS valor," +
			"coalesce(cc.cuenta_contable,'') AS cuenta_contable," +
			ConstantesBD.codigoNuncaValido+" AS codigo_tipo_ingreso," +
			"coalesce(cc.manejo_terceros||'','false') AS  manejo_terceros, " +
			"'' AS codigo_centro_costo, " +
			"coalesce(cc.manejo_centros_costo||'','false') AS manejo_centro_costo, " +
			"'' AS tipo_id_beneficiario, " +
			"'' AS numero_id_beneficiario, " +
			"'' AS nombre_beneficiario " +
			"FROM devol_recibos_caja drc " +
			"INNER JOIN formas_pago fp on (fp.consecutivo = drc.forma_pago) " +
			"LEFT OUTER JOIN cuentas_contables cc ON (cc.codigo = fp.cuenta_contable ) " +
			"WHERE drc.consecutivo = ? and (drc.contabilizado IS NULL OR drc.contabilizado = '"+ConstantesBD.acronimoNo+"') " +
			//segun lo que me informa margarita, no se debe validar la forma de pago al generar la interfaz. 02/06/2011
			//"AND ((drc.forma_pago = '"+ConstantesIntegridadDominio.acronimoFormaPagoEfectivo+"' and fp.tipo_detalle = "+ConstantesBD.codigoTipoDetalleFormasPagoNinguno+" ) or (drc.forma_pago = '"+ConstantesIntegridadDominio.acronimoFormaPagoCheque+"' and fp.tipo_detalle = "+ConstantesBD.codigoTipoDetalleFormasPagoCheque+" )) " +
			" ) t ORDER BY forma_pago ASC ";
	/**
	 * consulta Auxiliar de Centro de Costo
	 * */
	private static final String consultarAuxCentroCosto = 
			"SELECT " +
			"coalesce(cco.codigo_interfaz,'') as codigo_centro_costo " +
			"FROM detalle_conceptos_rc dcrc " +
			"INNER JOIN conceptos_ing_tesoreria cit ON (cit.codigo = dcrc.concepto AND cit.institucion = dcrc.institucion) " +
			"INNER JOIN centros_costo cco ON (cco.codigo = cit.codigo_centro_costo ) " +
			"WHERE dcrc.numero_recibo_caja = ? ";
	
	//*******
	
	/**
	 * consulta la entidades subcontratadas
	 * */
	private static final String consultarAutoEntidadesSub = " " +
			"SELECT " +
			ConstantesBD.codigoTipoDocInteAutoServicioEntSub+" as tipo_documento01, "+
			"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteAutoServicioEntSub+") as descripcion_documento01, "+
			"aes.consecutivo_autorizacion as numero_documento01, "+
			ConstantesBD.tipoConsecutivoInteNumeroDocAutor+" as tipo_consecutivo01, " +
			"coalesce(to_char(aes.fecha_autorizacion,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha01, "+
			"'' as numero_documento02, "+
			"ca.codigo AS centro_oper_doc," +
			"coalesce(getnumeroidentificaciontercero(es.tercero),'') AS tercero, " +
			"tes.valor_unitario as valor " +
			
			"FROM manejopaciente.autorizaciones_entidades_sub aes " +
			"INNER JOIN facturacion.tarifas_entidad_sub tes on(tes.autorizacion=aes.consecutivo) " +
			"INNER JOIN entidades_subcontratadas es ON (es.codigo_pk = aes.entidad_autorizada_sub) " +
			
			// PermitirAutorizarDiferenteDeSolicitudes
			"INNER JOIN ordenes.auto_entsub_solicitudes aess ON (aess.autorizacion_ent_sub = aes.consecutivo ) " + 
			"INNER JOIN solicitudes sol ON (sol.numero_solicitud = aess.numero_solicitud) " +
			
			"INNER JOIN centros_costo cc ON (cc.codigo = sol.centro_costo_solicitante) " +
			"INNER JOIN centro_atencion ca ON (ca.consecutivo = cc.centro_atencion) " +
			"WHERE aes.tipo = '"+ConstantesIntegridadDominio.acronimoExterna+"' " +
			"AND aes.estado IN ('"+ConstantesIntegridadDominio.acronimoAutorizado+"') " +
			"AND to_char(aes.fecha_autorizacion,'YYYY-MM-DD') = ? AND (aes.contabilizado IS NULL OR aes.contabilizado = '"+ConstantesBD.acronimoNo+"') " +
			" " +
			"UNION " +
			" " +
			"SELECT " +
			ConstantesBD.codigoTipoDocInteAnulaAutorServicioEntSub+" as tipo_documento01, "+
			"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteAnulaAutorServicioEntSub+") as descripcion_documento01, "+
			"aes.consecutivo_autorizacion as numero_documento01, "+
			ConstantesBD.tipoConsecutivoInteNumeroDocAutor+" as tipo_consecutivo01, " +
			"coalesce(to_char(aes.fecha_autorizacion,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha01, "+
			"'' as numero_documento02, "+
			"ca.codigo AS centro_oper_doc," +
			"coalesce(getnumeroidentificaciontercero(es.tercero),'') AS tercero, " +
			"tes.valor_unitario as valor " +
			"FROM manejopaciente.autorizaciones_entidades_sub aes " +
			"INNER JOIN facturacion.tarifas_entidad_sub tes on(tes.autorizacion=aes.consecutivo) " +
			"INNER JOIN entidades_subcontratadas es ON (es.codigo_pk = aes.entidad_autorizada_sub) " +
			
			// PermitirAutorizarDiferenteDeSolicitudes
			"INNER JOIN ordenes.auto_entsub_solicitudes aess ON (aess.autorizacion_ent_sub = aes.consecutivo) " +
			"INNER JOIN solicitudes sol ON (sol.numero_solicitud = aess.numero_solicitud) " +
			
			"INNER JOIN centros_costo cc ON (cc.codigo = sol.centro_costo_solicitante) " +
			"INNER JOIN centro_atencion ca ON (ca.consecutivo = cc.centro_atencion) " +
			"WHERE aes.tipo = '"+ConstantesIntegridadDominio.acronimoExterna+"' " +
			"AND aes.estado IN ('"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"') " +
			"AND to_char(aes.fecha_autorizacion,'YYYY-MM-DD') = ? AND (aes.contabilizado_anulacion IS NULL OR aes.contabilizado_anulacion = '"+ConstantesBD.acronimoNo+"') ";	
			
	
	/**
	 * consulta los despachos y devoluciones de medicamentos
	 * */
	private static final String consultarMedicamentos = " " +
			"SELECT " +
			ConstantesBD.codigoTipoDocInteDespachoMed+" as tipo_documento01, "+
			"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteDespachoMed+") as descripcion_documento01, "+
			"d.orden as numero_documento01, "+
			ConstantesBD.tipoConsecutivoInteNumeroDespacho+" as tipo_consecutivo01, " +
			"coalesce(to_char(d.fecha,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha01, "+
			
			ConstantesBD.codigoTipoDocInteDespachoMed+" as tipo_documento02, "+
			"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteDespachoMed+") as descripcion_documento02, "+
			"sol.consecutivo_ordenes_medicas as numero_documento02, "+
			ConstantesBD.tipoConsecutivoInteNumeroSolicitud+" as tipo_consecutivo02, " +
			"coalesce(to_char(sol.fecha_solicitud,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha02, "+
			"ca.codigo AS centro_oper_doc," +
			"coalesce(getnumeroidentificaciontercero(es.tercero),'') AS tercero, " +
			"facturacion.getTotalTarifaEntidadesSub(d.orden,"+ConstantesBD.codigoNuncaValido+","+ConstantesBD.codigoNuncaValido+","+ConstantesBD.codigoNuncaValido+","+ConstantesBD.codigoNuncaValido+") as valor " +
			"FROM solicitudes sol  " +
			"INNER JOIN despacho d ON (d.numero_solicitud = sol.numero_solicitud ) " +
			"INNER JOIN solicitudes_medicamentos solm ON (solm.numero_solicitud = sol.numero_solicitud) " +
			"INNER JOIN entidades_subcontratadas es ON (es.codigo_pk =  " +
			"( " +
			"SELECT COALESCE(tes.entidad_subcontratada,-1) "+ 
			"FROM tarifas_entidad_sub tes WHERE solicitud = sol.numero_solicitud "+ 
			"GROUP BY tes.entidad_subcontratada" +
			")) " +
			"INNER JOIN centros_costo cc ON (cc.codigo = sol.centro_costo_solicitante) " +
			"INNER JOIN centro_atencion ca ON (ca.consecutivo = cc.centro_atencion) " +
			"WHERE to_char(d.fecha,'YYYY-MM-DD') = ? AND sol.tipo = "+ConstantesBD.codigoTipoSolicitudMedicamentos+" AND (d.contabilizado IS NULL OR d.contabilizado = '"+ConstantesBD.acronimoNo+"')  "+
			" " +
			"UNION " +
			" " +
			"SELECT " +
			ConstantesBD.codigoTipoDocInteDevolucionMedi+" as tipo_documento01, "+
			"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteDevolucionMedi+") as descripcion_documento01, "+
			"dm.codigo as numero_documento01, "+
			ConstantesBD.tipoConsecutivoInteNumeroDevolucion+" as tipo_consecutivo01, " +
			"coalesce(to_char(dm.fecha,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha01, "+
			
			ConstantesBD.codigoNuncaValido+" as tipo_documento02, "+
			"'' as descripcion_documento02, "+
			ConstantesBD.codigoNuncaValido+" as numero_documento02, "+
			ConstantesBD.codigoNuncaValido+" as tipo_consecutivo02, " +
			"'' as fecha02, "+
			
			"ca.codigo AS centro_oper_doc," +
			"coalesce(getnumeroidentificaciontercero(es.tercero),'') AS tercero, " +
			"facturacion.getTotalTarifaEntidadesSub("+ConstantesBD.codigoNuncaValido+",dm.codigo,"+ConstantesBD.codigoNuncaValido+","+ConstantesBD.codigoNuncaValido+","+ConstantesBD.codigoNuncaValido+") as valor " +
			"FROM devolucion_med dm " +
			"INNER JOIN detalle_devol_med ddm ON (ddm.devolucion = dm.codigo) " +
			"INNER JOIN solicitudes sol ON (sol.numero_solicitud = ddm.numero_solicitud) " +
			"INNER JOIN solicitudes_medicamentos solm ON (solm.numero_solicitud = ddm.numero_solicitud) " +
			"INNER JOIN entidades_subcontratadas es ON (es.codigo_pk = " +
			"( " +
			"SELECT COALESCE(tes.entidad_subcontratada,-1) "+ 
			"FROM tarifas_entidad_sub tes WHERE solicitud = sol.numero_solicitud "+ 
			"GROUP BY tes.entidad_subcontratada" +
			")) " +
			"INNER JOIN centros_costo cc ON (cc.codigo = dm.centro_costo_devuelve) " +
			"INNER JOIN centro_atencion ca ON (ca.consecutivo = cc.centro_atencion) " +
			"WHERE " +
			"to_char(dm.fecha,'YYYY-MM-DD') = ? AND dm.estado = "+ConstantesBD.codigoEstadoDevolucionRecibida+" AND sol.tipo = "+ConstantesBD.codigoTipoSolicitudMedicamentos+" AND (dm.contabilizado IS NULL OR dm.contabilizado = '"+ConstantesBD.acronimoNo+"')  ";
	
	/**
	 * Consulta el Despacho/Devoluciones de Pedidos de Insumos de entidades subcontratadas 
	 * */
	private static final String consultarPedidos = 
		"SELECT " +
		ConstantesBD.codigoTipoDocInteDespaPedidoInsumo+" as tipo_documento01, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteDespaPedidoInsumo+") as descripcion_documento01, "+
		"dp.consecutivo as numero_documento01, "+
		ConstantesBD.tipoConsecutivoInteNumeroDespacho+" as tipo_consecutivo01, " +
		"coalesce(to_char(p.fecha,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha01, "+
		
		"'' as numero_documento02, "+
		
		"ca.codigo AS centro_oper_doc," +
		"coalesce(getnumeroidentificaciontercero(es.tercero),'') AS tercero, " +
		"facturacion.getTotalTarifaEntidadesSub("+ConstantesBD.codigoNuncaValido+","+ConstantesBD.codigoNuncaValido+",dp.pedido,"+ConstantesBD.codigoNuncaValido+","+ConstantesBD.codigoNuncaValido+") as valor " +
		"FROM despacho_pedido dp " +
		"INNER JOIN pedido p ON (dp.pedido = p.codigo AND p.entidad_subcontratada IS NOT NULL AND p.es_qx = '"+ConstantesBD.acronimoNo+"') " +
		"INNER JOIN entidades_subcontratadas es ON (es.codigo_pk = p.entidad_subcontratada) " +
		"INNER JOIN centros_costo cc ON (cc.codigo = p.centro_costo_solicitante) " +
		"INNER JOIN centro_atencion ca ON (ca.consecutivo = cc.centro_atencion) " +
		"WHERE " +
		"to_char(p.fecha,'YYYY-MM-DD') = ? AND (dp.contabilizado IS NULL OR dp.contabilizado = '"+ConstantesBD.acronimoNo+"')  " +
		" " +
		"UNION " +
		" " +
		"SELECT " +
		ConstantesBD.codigoTipoDocInteDevolPedidoInsumo+" as tipo_documento01, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteDevolPedidoInsumo+") as descripcion_documento01, "+
		"dp.codigo as numero_documento01, "+
		ConstantesBD.tipoConsecutivoInteNumeroDevolucion+" as tipo_consecutivo01, " +
		"coalesce(to_char(dp.fecha,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha01, "+
		
		"'' as numero_documento02, " +
		"'' AS centro_oper_doc," +
		"'' AS tercero, " +
		"facturacion.getTotalTarifaEntidadesSub("+ConstantesBD.codigoNuncaValido+","+ConstantesBD.codigoNuncaValido+","+ConstantesBD.codigoNuncaValido+",dp.codigo,"+ConstantesBD.codigoNuncaValido+") as valor " +
		"FROM devolucion_pedidos dp " +
		"WHERE " +
		"to_char(dp.fecha,'YYYY-MM-DD') = ? AND dp.es_qx = '"+ConstantesBD.acronimoNo+"' AND (dp.contabilizado IS NULL OR dp.contabilizado = '"+ConstantesBD.acronimoNo+"') ";
	
	/**
	 * Consulta el Despacho/Devoluciones de Pedidos Quirurgicos de entidades subcontratadas 
	 * */
	private static final String consultarPedidosQx = 
		"SELECT " +
		ConstantesBD.codigoTipoDocInteDespachoPedidoQx+" as tipo_documento01, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteDespachoPedidoQx+") as descripcion_documento01, "+
		"dp.consecutivo||'' as numero_documento01, "+
		ConstantesBD.tipoConsecutivoInteNumeroDespacho+" as tipo_consecutivo01, " +
		"coalesce(to_char(p.fecha,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha01, "+
		
		"'' as numero_documento02, "+
		
		"ca.codigo AS centro_oper_doc," +
		"coalesce(getnumeroidentificaciontercero(es.tercero),'') AS tercero, " +
		"facturacion.getTotalTarifaEntidadesSub("+ConstantesBD.codigoNuncaValido+","+ConstantesBD.codigoNuncaValido+",dp.pedido,"+ConstantesBD.codigoNuncaValido+","+ConstantesBD.codigoNuncaValido+") as valor " +
		"FROM despacho_pedido dp " +
		"INNER JOIN pedido p ON (dp.pedido = p.codigo AND p.entidad_subcontratada IS NOT NULL AND p.es_qx = '"+ConstantesBD.acronimoSi+"') " +
		"INNER JOIN entidades_subcontratadas es ON (es.codigo_pk = p.entidad_subcontratada) " +
		"INNER JOIN centros_costo cc ON (cc.codigo = p.centro_costo_solicitante) " +
		"INNER JOIN centro_atencion ca ON (ca.consecutivo = cc.centro_atencion) " +
		"WHERE " +
		"to_char(p.fecha,'YYYY-MM-DD') = ? AND (dp.contabilizado IS NULL OR dp.contabilizado = '"+ConstantesBD.acronimoNo+"')  " +
		" " +
		"UNION " +
		" " +
		"SELECT " +
		ConstantesBD.codigoTipoDocInteDevolucionPedidoQx+" as tipo_documento01, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteDevolucionPedidoQx+") as descripcion_documento01, "+
		"dp.codigo||'' as numero_documento01, "+
		ConstantesBD.tipoConsecutivoInteNumeroDevolucion+" as tipo_consecutivo01, " +
		"coalesce(to_char(dp.fecha,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha01, "+
		
		"'' as numero_documento02, " +
		"'' AS centro_oper_doc," +
		"'' AS tercero, " +
		"facturacion.getTotalTarifaEntidadesSub("+ConstantesBD.codigoNuncaValido+","+ConstantesBD.codigoNuncaValido+","+ConstantesBD.codigoNuncaValido+",dp.codigo,"+ConstantesBD.codigoNuncaValido+") as valor " +
		"FROM devolucion_pedidos dp " +
		"WHERE " +
		"to_char(dp.fecha,'YYYY-MM-DD') = ? AND dp.es_qx = '"+ConstantesBD.acronimoSi+"' AND (dp.contabilizado IS NULL OR dp.contabilizado = '"+ConstantesBD.acronimoNo+"') ";
	
	/**
	 * consulta los despachos y devoluciones de medicamentos
	 * */
	private static final String consultarCargosDirectos = " " +
		"SELECT " +
		
		ConstantesBD.codigoTipoDocInteCargosDirectosArt+" as tipo_documento01, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteCargosDirectosArt+") as descripcion_documento01, "+
		"sol.consecutivo_ordenes_medicas||'' as numero_documento01, "+
		ConstantesBD.tipoConsecutivoInteNumeroSolicitud+" as tipo_consecutivo01, " +
		"coalesce(to_char(sol.fecha_solicitud,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha01, "+
		
		ConstantesBD.codigoTipoDocInteCargosDirectosArt+" as tipo_documento02, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteCargosDirectosArt+") as descripcion_documento02, "+
		"d.orden as numero_documento02, "+
		ConstantesBD.tipoConsecutivoInteNumeroDespacho+" as tipo_consecutivo02, " +
		"coalesce(to_char(d.fecha,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha02, "+
		
		"ca.codigo AS centro_oper_doc," +
		"coalesce(getnumeroidentificaciontercero(es.tercero),'') AS tercero, " +
		"facturacion.getTotalTarifaEntidadesSub(d.orden,"+ConstantesBD.codigoNuncaValido+","+ConstantesBD.codigoNuncaValido+","+ConstantesBD.codigoNuncaValido+","+ConstantesBD.codigoNuncaValido+") as valor " +
		"FROM solicitudes sol  " +
		"INNER JOIN cargos_directos cdi ON (cdi.numero_solicitud = sol.numero_solicitud AND cdi.afecta_inventarios = '"+ConstantesBD.acronimoSi+"') " +
		"INNER JOIN despacho d ON (d.numero_solicitud = sol.numero_solicitud ) " +
		"INNER JOIN solicitudes_medicamentos solm ON (solm.numero_solicitud = sol.numero_solicitud) " +
		"INNER JOIN entidades_subcontratadas es ON (es.codigo_pk = " +
		"( " +
		"SELECT COALESCE(tes.entidad_subcontratada,-1) "+ 
		"FROM tarifas_entidad_sub tes WHERE solicitud = sol.numero_solicitud "+ 
		"GROUP BY tes.entidad_subcontratada" +
		")) " +
		"INNER JOIN centros_costo cc ON (cc.codigo = sol.centro_costo_solicitante) " +
		"INNER JOIN centro_atencion ca ON (ca.consecutivo = cc.centro_atencion) " +
		"WHERE to_char(d.fecha,'YYYY-MM-DD') = ? AND sol.tipo = "+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+" AND (d.contabilizado IS NULL OR d.contabilizado = '"+ConstantesBD.acronimoNo+"')  "+
		" " +
		"UNION " +
		" " +
		"SELECT " +
		ConstantesBD.codigoTipoDocInteAnulaCargosDirectosArticulo+" as tipo_documento01, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteAnulaCargosDirectosArticulo+") as descripcion_documento01, "+
		"getconsecutivosolicitud(solm.numero_solicitud)||'' AS numero_documento01, "+
		ConstantesBD.tipoConsecutivoInteNumeroSolicitud+" AS tipo_consecutivo01, " +
		"coalesce(to_char(sol.fecha_solicitud,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha01, "+
		
		ConstantesBD.codigoTipoDocInteAnulaCargosDirectosArticulo+" as tipo_documento02, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteAnulaCargosDirectosArticulo+") as descripcion_documento02, "+
		"acf.codigo AS numero_documento02, "+
		ConstantesBD.tipoConsecutivoInteNumeroAnulacion+" AS tipo_consecutivo02, " +
		"coalesce(to_char(acf.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha02, "+
		
		"ca.codigo AS centro_oper_doc," +
		"coalesce(getnumeroidentificaciontercero(es.tercero),'') AS tercero, " +
		"facturacion.getTotalTarifaEntidadesSub("+ConstantesBD.codigoNuncaValido+","+ConstantesBD.codigoNuncaValido+","+ConstantesBD.codigoNuncaValido+","+ConstantesBD.codigoNuncaValido+",acf.codigo) as valor " +
		"FROM anulacion_cargos_farmacia acf " +
		"INNER JOIN cargos_directos cdi ON (cdi.numero_solicitud = acf.numero_solicitud AND cdi.afecta_inventarios = '"+ConstantesBD.acronimoSi+"') " +
		"INNER JOIN solicitudes_medicamentos solm ON (solm.numero_solicitud = acf.numero_solicitud) " +
		"INNER JOIN entidades_subcontratadas es ON (es.codigo_pk = "+
		"( " +
		"SELECT COALESCE(tes.entidad_subcontratada,-1) "+ 
		"FROM tarifas_entidad_sub tes WHERE solicitud = acf.numero_solicitud "+ 
		"GROUP BY tes.entidad_subcontratada" +
		")) " +
		"INNER JOIN solicitudes sol ON (sol.numero_solicitud = acf.numero_solicitud) " +
		"INNER JOIN centros_costo cc ON (cc.codigo = sol.centro_costo_solicitante) " +
		"INNER JOIN centro_atencion ca ON (ca.consecutivo = cc.centro_atencion) " +
		"WHERE " +
		"to_char(acf.fecha_modifica,'YYYY-MM-DD') = ? AND (acf.contabilizado IS NULL OR acf.contabilizado = '"+ConstantesBD.acronimoNo+"') AND sol.tipo = "+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos;
	
	/**
	 * Consulta la informacion varia del la devolucion del pedido
	 * */
	private static final String pedidoTerceroCenOpera = 
		"SELECT " +
		"ca.codigo AS centro_oper_doc," +
		"coalesce(getnumeroidentificaciontercero(es.tercero),'') AS tercero " +
		"FROM " +
		"detalle_devol_pedido ddp " +
		"INNER JOIN pedido p ON (ddp.pedido = p.codigo AND p.entidad_subcontratada IS NOT NULL AND p.es_qx = ? ) " +
		"INNER JOIN entidades_subcontratadas es ON (es.codigo_pk = p.entidad_subcontratada) " +
		"INNER JOIN centros_costo cc ON (cc.codigo = p.centro_costo_solicitante) " +
		"INNER JOIN centro_atencion ca ON (ca.consecutivo = cc.centro_atencion) " +
		"WHERE ddp.devolucion = ? " ;
	
	/**
	 * consulta el detalle de ajustes de facturas 
	 * */
	private static final String consultaDetAjusteFactStr = " " +
		"SELECT " +
		"coalesce(dfs.servicio,dfs.articulo) as servicio_articulo, " +
		"dfs.solicitud as numero_solicitud, "+
		"dfs.tipo_solicitud as tipo_solicitud, "+
		"adfe.valor_ajuste AS valor," +
		"coalesce(dfs.valor_consumo_paquete,0) as valor_consumo_paquete, "+
		"cc1.codigo as centro_costo_solicitante, "+
		"cc1.codigo_interfaz as cod_centro_costo_solicitante,  " +
		"cc2.unidad_funcional as uni_func_solicitado, "+
		"cc2.institucion as cod_inst_solicitado, "+
		"CASE WHEN f.cod_paciente IS NOT NULL THEN getnombrepersona(f.cod_paciente) ELSE '' END as paciente, "+		
		"f.tipo_factura_sistema," +
		"ae.tipo_ajuste,  "+
		"cac.cuenta_contable,  " +
		"adfe.valor_ajuste_pool as valor_pool, " +
		"adfe.pool as pool, " +
		"coalesce(s.especialidad_solicitada,"+ConstantesBD.codigoNuncaValido+") as especialidad_solicitada " +
		"FROM ajustes_empresa ae " +
		"INNER JOIN ajus_fact_empresa afe ON (afe.codigo = ae.codigo) " +
		"INNER JOIN facturas f ON (f.codigo = afe.factura) " +
		"INNER JOIN ajus_det_fact_empresa adfe ON (adfe.codigo = afe.codigo AND adfe.factura = afe.factura) " +
		"INNER JOIN det_factura_solicitud dfs on (dfs.codigo = adfe.det_fact_solicitud) "+
		"INNER JOIN concepto_ajustes_cartera cac ON (afe.concepto_ajuste = cac.codigo) " +
		"INNER JOIN solicitudes s ON(s.numero_solicitud = dfs.solicitud) "+ 
		"INNER JOIN centros_costo cc1 ON(cc1.codigo = s.centro_costo_solicitante) "+
		"INNER JOIN centros_costo cc2 ON(cc2.codigo = s.centro_costo_solicitado) "+		
		"WHERE ae.codigo = ? and f.consecutivo_factura = ? AND adfe.valor_ajuste > 0 AND dfs.tipo_solicitud <> "+ConstantesBD.codigoTipoSolicitudCirugia+" AND ae.estado = "+ConstantesBD.codigoEstadoCarteraAprobado+" " +
		"UNION " +
		"SELECT " +
		"adf.servicio_asocio as servicio_articulo, "+
		"dfs.solicitud as numero_solicitud, "+
		"dfs.tipo_solicitud as tipo_solicitud, "+
		"aafe.valor_ajuste as valor, "+
		"0 as valor_consumo_paquete, "+
		"cc1.codigo as centro_costo_solicitante, "+
		"cc1.codigo_interfaz as cod_centro_costo_solicitante,  "+
		"cc2.unidad_funcional as uni_func_solicitado, "+
		"cc2.institucion as cod_inst_solicitado,  "+
		"CASE WHEN f.cod_paciente IS NOT NULL THEN getnombrepersona(f.cod_paciente) ELSE ''END as paciente, "+
		"f.tipo_factura_sistema," +
		"ae.tipo_ajuste,  "+
		"cac.cuenta_contable,  " +
		"aafe.valor_ajuste_pool as valor_pool, " +
		"aafe.pool as pool, " +
		"coalesce(adf.especialidad,"+ConstantesBD.codigoNuncaValido+") as especialidad_solicitada "+
		"FROM ajustes_empresa ae " +		
		"INNER JOIN ajus_fact_empresa afe ON (afe.codigo = ae.codigo) " +		
		"INNER JOIN facturas f ON (f.codigo = afe.factura) " +
		"INNER JOIN ajus_det_fact_empresa adfe ON (adfe.codigo = afe.codigo AND adfe.factura = afe.factura) " +		
		"INNER JOIN det_factura_solicitud dfs on (dfs.codigo = adfe.det_fact_solicitud) " +
		"INNER JOIN cartera.ajus_asocios_fact_empresa aafe ON (aafe.codigo_ajuste = adfe.codigo AND aafe.factura = adfe.factura AND aafe.det_aso_fac_solicitud = adfe.det_fact_solicitud) "+
		"INNER JOIN asocios_det_factura adf ON(adf.consecutivo = aafe.consecutivo_aso_det_fac) " +
		"INNER JOIN tipos_asocio ta ON (ta.codigo = adf.tipo_asocio) "+
		"INNER JOIN concepto_ajustes_cartera cac ON (afe.concepto_ajuste = cac.codigo) " +
		"INNER JOIN solicitudes s ON(s.numero_solicitud = dfs.solicitud) "+
		"INNER JOIN centros_costo cc1 ON(cc1.codigo = s.centro_costo_solicitante) "+
		"INNER JOIN centros_costo cc2 ON(cc2.codigo = s.centro_costo_solicitado) "+
		"WHERE ae.codigo = ? and f.consecutivo_factura = ? AND ae.estado = "+ConstantesBD.codigoEstadoCarteraAprobado+" AND " +
		"dfs.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" AND aafe.valor_ajuste > 0 AND " +
		"(ta.tipos_servicio <> '"+ConstantesBD.codigoServicioMaterialesCirugia+"' OR " +
		"(ta.tipos_servicio = '"+ConstantesBD.codigoServicioMaterialesCirugia+"')) " +
		"UNION "+
		"SELECT " +
		ConstantesBD.codigoNuncaValido+" AS servicio_articulo, " +
		ConstantesBD.codigoNuncaValido+" AS numero_solicitud, "+
		ConstantesBD.codigoNuncaValido+" AS tipo_solicitud, "+
		"afe.valor_ajuste AS valor," +
		"0 as valor_consumo_paquete, "+
		ConstantesBD.codigoNuncaValido+" AS centro_costo_solicitante,"+
		"'"+ConstantesBD.codigoNuncaValido+"' AS cod_centro_costo_solicitante, " +
		"'"+ConstantesBD.codigoNuncaValido+"' AS uni_func_solicitado, "+
		ConstantesBD.codigoNuncaValido+" AS cod_inst_solicitado, "+
		"CASE WHEN f.cod_paciente IS NOT NULL THEN getnombrepersona(f.cod_paciente) ELSE ''END as paciente, "+
		"f.tipo_factura_sistema," +
		"ae.tipo_ajuste,  "+
		"cac.cuenta_contable,  " +
		"0 as valor_pool, " +
		"null as pool, " +
		""+ConstantesBD.codigoNuncaValido+" as especialidad_solicitada "+
		"FROM ajustes_empresa ae "+
		"INNER JOIN ajus_fact_empresa afe ON (afe.codigo = ae.codigo) " +
		"INNER JOIN facturas f ON (f.codigo = afe.factura) " +
		"INNER JOIN concepto_ajustes_cartera cac ON (afe.concepto_ajuste = cac.codigo) " +
		"WHERE " +
		"ae.codigo = ? and f.consecutivo_factura = ? " +
		"AND afe.valor_ajuste > 0 AND ae.estado = "+ConstantesBD.codigoEstadoCarteraAprobado+" AND f.tipo_factura_sistema = "+ValoresPorDefecto.getValorFalseParaConsultas();
	
	/**
	 * consulta el detalle de ajustes de facturas
	 * valor ajuste pool 
	 * */
	
	 private static final String consultaDetAjusteFact02Str = " " +
		"SELECT " +
		"dfs.servicio as servicio, "+
		"dfs.tipo_solicitud as tipo_solicitud, "+
		"adfe.valor_ajuste_pool as valor, "+
		"cc1.codigo as centro_costo_solicitante, "+
		"cc1.unidad_funcional as uni_func_solicitante, "+
		"cc1.institucion as cod_inst_solicitante, "+
		"cc1.codigo_interfaz as cod_centro_costo_solicitante, " +
		"coalesce(s.especialidad_solicitada,"+ConstantesBD.codigoNuncaValido+") as especialidad_solicitada, "+
		"cc2.unidad_funcional as uni_func_solicitado, "+
		"cc2.institucion as cod_inst_solicitado, "+
		ConstantesBD.codigoNuncaValido+" as tipo_asocio, "+ 
		"'' as tipo_serv_asocio, "+
		"coalesce(adfe.pool,"+ConstantesBD.codigoNuncaValido+") as pool, "+
		"CASE WHEN f.cod_paciente IS NOT NULL THEN getnombrepersona(f.cod_paciente) ELSE ''END as paciente, "+
		"getnumeroidentificaciontercero(p.tercero_responsable) as id_tercero, "+
		"f.tipo_factura_sistema, " +
		"ae.tipo_ajuste,  "+
		"cac.cuenta_contable " +
		"FROM ajustes_empresa ae " +
		"INNER JOIN ajus_fact_empresa afe ON (afe.codigo = ae.codigo) " +
		"INNER JOIN facturas f ON (f.codigo = afe.factura) " +
		"INNER JOIN ajus_det_fact_empresa adfe ON (adfe.codigo = afe.codigo AND adfe.factura = afe.factura) " +
		"INNER JOIN det_factura_solicitud dfs on (dfs.codigo = adfe.det_fact_solicitud) "+
		"INNER JOIN concepto_ajustes_cartera cac ON (afe.concepto_ajuste = cac.codigo) " +
		"INNER JOIN solicitudes s ON(s.numero_solicitud = dfs.solicitud) "+
		"INNER JOIN centros_costo cc1 ON(cc1.codigo = s.centro_costo_solicitante) "+
		"INNER JOIN centros_costo cc2 ON(cc2.codigo = s.centro_costo_solicitado) "+
		"INNER JOIN pooles p ON(p.codigo = dfs.pool) "+
		"WHERE ae.codigo = ? AND f.consecutivo_factura = ? AND dfs.tipo_solicitud <> "+ConstantesBD.codigoTipoSolicitudCirugia+" " +
		"AND dfs.tipo_solicitud <> "+ConstantesBD.codigoTipoSolicitudPaquetes+" AND ae.estado = "+ConstantesBD.codigoEstadoCarteraAprobado+" "+
		"UNION " +
		"SELECT " +
		"adf.servicio_asocio as servicio, "+
		"dfs.tipo_solicitud as tipo_solicitud, "+
		"adf.valor_total as valor, "+
		"cc1.codigo as centro_costo_solicitante, "+
		"cc1.unidad_funcional as uni_func_solicitante, "+
		"cc1.institucion as cod_inst_solicitante, "+
		"cc1.codigo_interfaz as cod_centro_costo_solicitante,  "+
		"case when adf.codigo_medico is not null then "+ 
		"coalesce((" +
			"select " +
			"dch.especialidad_medico " +
			"from sol_cirugia_por_servicio scp " +
			"inner join det_cx_honorarios dch on(dch.cod_sol_cx_servicio = scp.codigo) " +
			"where " +
			"scp.numero_solicitud = dfs.solicitud and " +
			"scp.servicio = dfs.servicio and " +
			"dch.servicio = adf.servicio_asocio and " +
			"dch.tipo_asocio = adf.tipo_asocio and " +
			"dch.medico = adf.medico_asocio " +
			"),"+ConstantesBD.codigoNuncaValido+") " +
		"else " +
			"coalesce(s.especialidad_solicitada,"+ConstantesBD.codigoNuncaValido+") "+ 
		"end as especialidad_solicitada, "+		
		"cc2.unidad_funcional as uni_func_solicitado, "+
		"cc2.institucion as cod_inst_solicitado, "+
		"adf.tipo_asocio as tipo_asocio, "+
		"ta.tipos_servicio as tipo_serv_asocio, "+
		"coalesce(adf.pool,"+ConstantesBD.codigoNuncaValido+") as pool, "+
		"CASE WHEN f.cod_paciente IS NOT NULL THEN getnombrepersona(f.cod_paciente) ELSE '' END as paciente, "+
		"getnumeroidentificaciontercero(p.tercero_responsable) as id_tercero, "+
		"f.tipo_factura_sistema, " +
		"ae.tipo_ajuste,  "+
		"cac.cuenta_contable " +
		"FROM ajustes_empresa ae " +
		"INNER JOIN ajus_fact_empresa afe ON (afe.codigo = ae.codigo) " +
		"INNER JOIN facturas f ON (f.codigo = afe.factura) " +
		"INNER JOIN ajus_det_fact_empresa adfe ON (adfe.codigo = afe.codigo AND adfe.factura = afe.factura) " +
		"INNER JOIN det_factura_solicitud dfs on (dfs.factura = adfe.det_fact_solicitud) "+
		"INNER JOIN solicitudes_cirugia sc ON(sc.numero_solicitud = dfs.solicitud) "+
		"INNER JOIN cartera.ajus_asocios_fact_empresa aafe ON (aafe.codigo_ajuste = adfe.codigo AND aafe.factura = adfe.factura AND aafe.det_aso_fac_solicitud = adfe.det_fact_solicitud) "+
		"INNER JOIN asocios_det_factura adf ON(adf.codigo = aafe.consecutivo_aso_det_fac) "+
		"INNER JOIN concepto_ajustes_cartera cac ON (afe.concepto_ajuste = cac.codigo) " +
		"INNER JOIN solicitudes s ON(s.numero_solicitud = dfs.solicitud) "+
		"INNER JOIN centros_costo cc1 ON(cc1.codigo = s.centro_costo_solicitante) "+
		"INNER JOIN centros_costo cc2 ON(cc2.codigo = s.centro_costo_solicitado) "+		
		"INNER JOIN pooles p ON(p.codigo = adf.pool) "+
		"LEFT OUTER JOIN tipos_asocio ta ON(ta.codigo = adf.tipo_asocio) "+
		"WHERE ae.codigo = ? AND f.consecutivo_factura = ? AND " +
		"dfs.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" AND " +
		"(ta.tipos_servicio <> '"+ConstantesBD.codigoServicioMaterialesCirugia+"' OR " +
		"(ta.tipos_servicio = '"+ConstantesBD.codigoServicioMaterialesCirugia+"' AND sc.indi_tarifa_consumo_materiales <> '"+ConstantesBD.acronimoSi+"')) " ;
	 
	 /**
		 * consulta el detalle de ajustes de facturas
		 * para los convenios de captiacion
		 * */
		
	 private static final String consultaDetAjusteFact03Str = "SELECT "+ 
	 	"coalesce(f.valor_convenio,0) as valor_cobrar_convenio, "+ 
	 	"coalesce(f.valor_favor_convenio,0) as valor_favor_convenio, "+ 
	 	"c.codigo as codigo_convenio, "+ 
	 	"c.nombre as nombre_convenio, "+
	 	"c.tipo_regimen as codigo_tipo_regimen, "+ 
	 	"c.tipo_convenio as codigo_tipo_convenio, "+ 
	 	"c.institucion as codigo_institucion, "+  
	 	"c.tipo_contrato as codigo_tipo_contrato, "+  
	 	"getnumeroidentificaciontercero(e.tercero) as id_tercero, "+ 
	 	"coalesce(ca.codigo,'') as codigo_centro_at_cont, "+  
	 	"coalesce(facturacion.getcodintcentrocosto(c.centro_costo_contable),'') as cod_int_cc_contable, "+ 
	 	"ae.tipo_ajuste as tipo_ajuste "+ 
	 	"FROM ajustes_empresa ae "+  
	 	"INNER JOIN ajus_fact_empresa afe ON (afe.codigo = ae.codigo) "+  
	 	"INNER JOIN facturas f ON (f.codigo = afe.factura) "+  
	 	"INNER JOIN convenios c ON(c.codigo = f.convenio) "+ 
	 	"INNER JOIN empresas e on(e.codigo = c.empresa) "+ 
	 	"LEFT OUTER JOIN facturacion.cen_aten_contable_conv cacc ON(cacc.convenio = c.codigo) "+  
	 	"LEFT OUTER JOIN administracion.centro_atencion ca ON(ca.consecutivo = cacc.centro_atencion) "+ 
	 	"WHERE " +
	 	"ae.codigo = ? and f.consecutivo_factura = ? AND afe.valor_ajuste > 0 AND ae.estado = "+ConstantesBD.codigoEstadoCarteraAprobado+"  and c.tipo_contrato =  "+ConstantesBD.codigoTipoContratoCapitado; 
	//************************************CONSULTAS ZSEBAS******************************************************************************
	
	
	/**
	 * Método para obtener el tercero del concepto de una factura varia de un recibo de caja
	 */
	private static final String obtenerTerceroFacturaVariaReciboCajaStr = "SELECT "+ 
		"coalesce(getnumeroidentificaciontercero(cfv.tercero),'') as id_tercero "+ 
		"FROM pagos_general_empresa pge "+ 
		"INNER JOIN aplicacion_pagos_fvarias apf ON (apf.pagos_general_fvarias = pge.codigo) "+ 
		"INNER JOIN aplicac_pagos_factura_fvarias apff ON(apff.aplicacion_pagos = apf.codigo) "+ 
		"INNER JOIN facturas_varias fv ON(fv.codigo_fac_var = apff.factura) "+ 
		"INNER JOIN conceptos_facturas_varias cfv ON(cfv.consecutivo = fv.concepto) "+ 
		"WHERE "+ 
		"pge.documento=? and "+ 
		"pge.tipo_doc = "+ConstantesBD.codigoTipoDocumentoPagosReciboCaja+" and "+ 
		"cfv.tercero is not null";
	
	/**
	 * Cadena para consultar la cuenta debito de una factura varia del recibo de caja
	 */
	private static final String obtenerCuentaDebitoFacturaVariaReciboCajaStr = "SELECT "+ 
		"cc.codigo as codigo, "+
		"cc.cuenta_contable as cuenta_contable, "+
		"cc.descripcion as descripcion, "+
		"cc.activo as activo, "+
		"cc.manejo_terceros as manejo_terceros, "+
		"cc.manejo_centros_costo as manejo_centros_costo, "+
		"cc.manejo_base_gravable as manejo_base_gravable, "+
		"cc.naturaleza_cuenta as naturaleza_cuenta, "+
		"coalesce(cc.institucion,"+ConstantesBD.codigoNuncaValido+") as institucion, "+
		"cc.anio_vigencia as anio_vigencia "+  
		"FROM pagos_general_empresa pge "+ 
		"INNER JOIN aplicacion_pagos_fvarias apf ON (apf.pagos_general_fvarias = pge.codigo) "+ 
		"INNER JOIN aplicac_pagos_factura_fvarias apff ON(apff.aplicacion_pagos = apf.codigo) "+ 
		"INNER JOIN facturas_varias fv ON(fv.codigo_fac_var = apff.factura) "+ 
		"INNER JOIN conceptos_facturas_varias cfv ON(cfv.consecutivo = fv.concepto) " +
		"INNER JOIN cuentas_contables cc ON(cc.codigo = cfv.cuenta_contable_debito) "+ 
		"WHERE "+ 
		"pge.documento=? and "+ 
		"pge.tipo_doc = "+ConstantesBD.codigoTipoDocumentoPagosReciboCaja+"  "; 
		
	
	
	/**
	 * Cadena para consultar el detalle de conceptos del recibo de caja para las cuentas x cobrar
	 */
	private static final String consultaDetMovCuentasXCobrarRecaudoStr = "SELECT "+ 
		"dcrc.consecutivo, " +
		"coalesce(dcrc.nombre_beneficiario,'') as beneficiario,"+
		"coalesce(dcrc.valor,0) as valor, "+
		"coalesce(dcrc.doc_soporte,'') as doc_soporte, "+
		"cit.codigo_tipo_ingreso, "+
		"cc.codigo as codigo, "+
		"cc.cuenta_contable as cuenta_contable, "+
		"cc.descripcion as descripcion, "+
		"cc.activo as activo, "+
		"cc.manejo_terceros as manejo_terceros, "+
		"cc.manejo_centros_costo as manejo_centros_costo, "+
		"cc.manejo_base_gravable as manejo_base_gravable, "+
		"cc.naturaleza_cuenta as naturaleza_cuenta, "+
		"coalesce(cc.institucion,"+ConstantesBD.codigoNuncaValido+") as institucion, "+
		"cc.anio_vigencia as anio_vigencia," +
		//"CASE WHEN f.consecutivo_factura||'' IS NULL THEN '' ELSE f.consecutivo_factura||'' END AS factura_cruce," +
		//Factura cruce incluyendo factura_varia
		"CASE WHEN tit.codigo = " + ConstantesBD.codigoTipoIngresoTesoreriaPacientes + " THEN f.consecutivo_factura ||'' WHEN tit.codigo = " + ConstantesBD.codigoTipoIngresoTesoreriaOtrasCxC + " THEN fv.consecutivo ||'' ELSE '' END  AS factura_cruce," +
		"coalesce(cit.valor,'') AS  valor_concep_ing_tes, " +
		"ing.consecutivo AS ingreso "+
		"FROM detalle_conceptos_rc dcrc "+ 
		"LEFT OUTER JOIN tesoreria.movimientos_abonos ma " +
		"ON(ma.codigo_documento=dcrc.numero_recibo_caja AND ma.tipo IN("+ConstantesBD.tipoMovimientoAbonoIngresoReciboCaja+")  AND ma.ingreso is not null) "+ 
		"INNER JOIN conceptos_ing_tesoreria cit ON (cit.codigo = dcrc.concepto AND cit.institucion = dcrc.institucion) "+ 
		//Esta tabla define si el recibo es de abonos, facturas o facturas varias:
		"INNER JOIN tipo_ing_tesoreria tit ON tit.codigo = cit.codigo_tipo_ingreso " +
		"LEFT OUTER JOIN cuentas_contables cc ON (cc.codigo = cit.cuenta) " +
		"LEFT OUTER JOIN cartera.pagos_facturas_paciente pfp ON (pfp.documento = dcrc.numero_recibo_caja AND pfp.tipo_doc = "+ConstantesBD.codigoTipoDocumentoPagosReciboCaja+" ) "+ 
		"LEFT OUTER JOIN facturacion.facturas f ON (f.codigo = pfp.factura) "+
		// Para adicionar las facturas varias:
		"LEFT OUTER JOIN cartera.pagos_general_empresa pge ON (pge.documento = dcrc.numero_recibo_caja AND pge.tipo_doc  =" + ConstantesBD.codigoTipoDocumentoPagosReciboCaja+" ) "+ 
		"LEFT OUTER JOIN aplicacion_pagos_fvarias apf ON (apf.pagos_general_fvarias = pge.codigo) " +
		"LEFT OUTER JOIN aplicac_pagos_factura_fvarias apff ON(apff.aplicacion_pagos = apf.codigo) " +
		"LEFT OUTER JOIN facturas_varias fv ON(fv.codigo_fac_var = apff.factura) " + 
		// Hasta aquí
		"LEFT OUTER JOIN manejopaciente.ingresos ing " +
			"ON(ing.id=ma.ingreso) " +
		"WHERE "+ 
		"dcrc.numero_recibo_caja = ? AND "+ 
		"cit.codigo_tipo_ingreso IN ("+ConstantesBD.codigoTipoIngresoTesoreriaConvenios+","+
		ConstantesBD.codigoTipoIngresoTesoreriaPacientes+","+
		ConstantesBD.codigoTipoIngresoTesoreriaCarteraParticular+","+
		ConstantesBD.codigoTipoIngresoTesoreriaOtrasCxC+"," +
		ConstantesBD.codigoTipoIngresoTesoreriaAbonos+") " ;
	
	/**
	 * Cadena para consultar el detalle de conceptos del recibo de caja para las cuentas x cobrar
	 */
	private static final String consultaDetMovCuentasXCobrarRecaudoStr_anulados = "SELECT "+ 
		"dcrc.consecutivo, " +
		"coalesce(dcrc.nombre_beneficiario,'') as beneficiario,"+
		"coalesce(dcrc.valor,0) as valor, "+
		"coalesce(dcrc.doc_soporte,'') as doc_soporte, "+
		"cit.codigo_tipo_ingreso, "+
		"cc.codigo as codigo, "+
		"cc.cuenta_contable as cuenta_contable, "+
		"cc.descripcion as descripcion, "+
		"cc.activo as activo, "+
		"cc.manejo_terceros as manejo_terceros, "+
		"cc.manejo_centros_costo as manejo_centros_costo, "+
		"cc.manejo_base_gravable as manejo_base_gravable, "+
		"cc.naturaleza_cuenta as naturaleza_cuenta, "+
		"coalesce(cc.institucion,"+ConstantesBD.codigoNuncaValido+") as institucion, "+
		"cc.anio_vigencia as anio_vigencia," +
		//"CASE WHEN f.consecutivo_factura||'' IS NULL THEN '' ELSE f.consecutivo_factura||'' END AS factura_cruce," +
		//Factura cruce incluyendo factura_varia
		"CASE WHEN tit.codigo = " + ConstantesBD.codigoTipoIngresoTesoreriaPacientes + " THEN f.consecutivo_factura ||'' WHEN tit.codigo = " + ConstantesBD.codigoTipoIngresoTesoreriaOtrasCxC + " THEN fv.consecutivo ||'' ELSE '' END  AS factura_cruce," +
		"coalesce(cit.valor,'') AS  valor_concep_ing_tes, " +
		"ing.consecutivo AS ingreso "+
		"FROM detalle_conceptos_rc dcrc "+ 
		"LEFT OUTER JOIN tesoreria.movimientos_abonos ma " +
		"ON(ma.codigo_documento=dcrc.numero_recibo_caja AND ma.tipo IN("+ConstantesBD.tipoMovimientoAbonoAnulacionFactura+")  AND ma.ingreso is not null) "+ 
		"INNER JOIN conceptos_ing_tesoreria cit ON (cit.codigo = dcrc.concepto AND cit.institucion = dcrc.institucion) "+ 
		//Esta tabla define si el recibo es de abonos, facturas o facturas varias:
		"INNER JOIN tipo_ing_tesoreria tit ON tit.codigo = cit.codigo_tipo_ingreso " +
		"LEFT OUTER JOIN cuentas_contables cc ON (cc.codigo = cit.cuenta) " +
		"LEFT OUTER JOIN cartera.pagos_facturas_paciente pfp ON (pfp.documento = dcrc.numero_recibo_caja AND pfp.tipo_doc = "+ConstantesBD.codigoTipoDocumentoPagosReciboCaja+" ) "+ 
		"LEFT OUTER JOIN facturacion.facturas f ON (f.codigo = pfp.factura) "+
		// Para adicionar las facturas varias:
		"LEFT OUTER JOIN cartera.pagos_general_empresa pge ON (pge.documento = dcrc.numero_recibo_caja AND pge.tipo_doc  =" + ConstantesBD.codigoTipoDocumentoPagosReciboCaja+" ) "+ 
		"LEFT OUTER JOIN aplicacion_pagos_fvarias apf ON (apf.pagos_general_fvarias = pge.codigo) " +
		"LEFT OUTER JOIN aplicac_pagos_factura_fvarias apff ON(apff.aplicacion_pagos = apf.codigo) " +
		"LEFT OUTER JOIN facturas_varias fv ON(fv.codigo_fac_var = apff.factura) " + 
		// Hasta aquí
		"LEFT OUTER JOIN manejopaciente.ingresos ing " +
			"ON(ing.id=ma.ingreso) " +
		"WHERE "+ 
		"dcrc.numero_recibo_caja = ? AND "+ 
		"cit.codigo_tipo_ingreso IN ("+ConstantesBD.codigoTipoIngresoTesoreriaConvenios+","+
		ConstantesBD.codigoTipoIngresoTesoreriaPacientes+","+
		ConstantesBD.codigoTipoIngresoTesoreriaCarteraParticular+","+
		ConstantesBD.codigoTipoIngresoTesoreriaOtrasCxC+"," +
		ConstantesBD.codigoTipoIngresoTesoreriaAbonos+") " ;
	
	/**
	 * Cadena para consultar el detalle de conceptos del recibo de caja para las cuentas x cobrar
	 */
	private static final String consultaDetMovCuentasXCobrarDevRecaudoStr =
	"SELECT "+ 
	"drc.consecutivo, " +
	"coalesce(dcrc.nombre_beneficiario,'') AS beneficiario, " +		
	"coalesce(drc.valor_devolucion,0) as valor, "+
	"coalesce(dcrc.doc_soporte,'') as doc_soporte, "+
	"cit.codigo_tipo_ingreso, "+		
	"cc.codigo as codigo, "+
	"cc.cuenta_contable as cuenta_contable, "+
	"cc.descripcion as descripcion, "+
	"cc.activo as activo, "+
	"cc.manejo_terceros as manejo_terceros, "+
	"cc.manejo_centros_costo as manejo_centros_costo, "+
	"cc.manejo_base_gravable as manejo_base_gravable, "+
	"cc.naturaleza_cuenta as naturaleza_cuenta, "+
	"coalesce(cc.institucion,"+ConstantesBD.codigoNuncaValido+") as institucion, "+
	"cc.anio_vigencia as anio_vigencia, "+
	//"CASE WHEN f.consecutivo_factura||'' IS NULL THEN '' ELSE f.consecutivo_factura||'' END AS factura_cruce, " +
	//Factura cruce incluyendo factura_varia
	"CASE WHEN tit.codigo = " + ConstantesBD.codigoTipoIngresoTesoreriaPacientes + " THEN f.consecutivo_factura ||'' WHEN tit.codigo = " + ConstantesBD.codigoTipoIngresoTesoreriaOtrasCxC + " THEN fv.consecutivo ||'' ELSE '' END  AS factura_cruce," +
	"coalesce(cit.valor,'') AS  valor_concep_ing_tes, " +
	"ing.consecutivo AS ingreso "+
	"FROM devol_recibos_caja drc "+ 
	"INNER JOIN conceptos_ing_tesoreria cit ON (cit.codigo = drc.concepto AND cit.institucion = drc.institucion) " +
	//Esta tabla define si el recibo es de abonos, facturas o facturas varias:
	"INNER JOIN tipo_ing_tesoreria tit ON tit.codigo = cit.codigo_tipo_ingreso " +
	"INNER JOIN detalle_conceptos_rc dcrc ON (dcrc.numero_recibo_caja = drc.numero_rc AND dcrc.concepto = drc.concepto) " +
	//Se elimina en el IN el movimiento 4 (ConstantesBD.tipoMovimientoAbonoAnulacionReciboCaja) por incidencia 2975
	"LEFT OUTER JOIN tesoreria.movimientos_abonos ma ON(ma.codigo_documento=drc.consecutivo AND ma.tipo IN("+ConstantesBD.tipoMovimientoAbonoDevolucionReciboCaja+")  AND ma.ingreso is not null) "+ 
	"LEFT OUTER JOIN manejopaciente.ingresos ing  ON(ing.id=ma.ingreso) " +
	"LEFT OUTER JOIN cuentas_contables cc ON (cc.codigo = cit.cuenta) "+
	"LEFT OUTER JOIN cartera.pagos_facturas_paciente pfp ON (pfp.documento = drc.numero_rc AND pfp.tipo_doc = "+ConstantesBD.codigoTipoDocumentoPagosReciboCaja+" ) " +
	"LEFT OUTER JOIN facturacion.facturas f ON (f.codigo = pfp.factura) "+
	// Para adicionar las facturas varias:
	"LEFT OUTER JOIN cartera.pagos_general_empresa pge ON (pge.documento = dcrc.numero_recibo_caja AND pge.tipo_doc  =" + ConstantesBD.codigoTipoDocumentoPagosReciboCaja+" ) "+ 
	"LEFT OUTER JOIN aplicacion_pagos_fvarias apf ON (apf.pagos_general_fvarias = pge.codigo) " +
	"LEFT OUTER JOIN aplicac_pagos_factura_fvarias apff ON(apff.aplicacion_pagos = apf.codigo) " +
	"LEFT OUTER JOIN facturas_varias fv ON(fv.codigo_fac_var = apff.factura) " + 
	// Hasta aquí
	"WHERE "+ 
	"drc.consecutivo = ? AND " +
	"(drc.contabilizado IS NULL OR drc.contabilizado = '"+ConstantesBD.acronimoNo+"') AND "+ 
	"cit.codigo_tipo_ingreso NOT IN ("+ConstantesBD.codigoTipoIngresoTesoreriaNinguno+") " ;
	
	
	//*********CONSULTAS PARA LA PARTE DE LÍNEA MOVIMIENTO DE ENTIDADES EXTERNAS*****************************************
	/**
	 * Método para consultar el detalle de una autorizacion de entidades subcontratadas
	 */
	private static final String consultarDetalleAutorizacionesEntidadesSubStr = "SELECT "+ 
		"te.articulo as servicio_articulo, " +
		"getnombrepersona(ae.codigo_paciente) as observaciones, "+ 
		"cc.codigo as consecutivo_centro_costo, "+
		"coalesce(cc2.codigo_interfaz,'')  as codigo_centro_costo, "+
		"cc.unidad_funcional as unidad_funcional, "+
		"coalesce(te.valor_unitario,0) as valor_tarifa, " +
		"getnombrepersona(ae.codigo_paciente) as observaciones "+ 
		
		"FROM autorizaciones_entidades_sub ae "+ 
		
		// PermitirAutorizarDiferenteDeSolicitudes
		"INNER JOIN ordenes.auto_entsub_solicitudes aes ON(aes.autorizacion_ent_sub = ae.consecutivo) "+  
		"INNER JOIN solicitudes s ON(s.numero_solicitud = aes.numero_solicitud) "+  
		
		"INNER JOIN centros_costo cc ON(cc.codigo = s.centro_costo_solicitado) "+
		"INNER JOIN centros_costo cc2 ON(cc2.codigo = s.centro_costo_solicitante) "+
		"INNER JOIN tarifas_entidad_sub te ON(te.autorizacion = ae.consecutivo) "+
		"WHERE ae.consecutivo_autorizacion = ?";
	
	/**
	 * Cadena para consultar el detalle del despacho de medicamentos
	 */
	private static final String consultarDetalleDespachoMedicamentosStr = "SELECT "+ 
		"dd.articulo as servicio_articulo, "+
		"cc.codigo as consecutivo_centro_costo, "+
		"coalesce(cc2.codigo_interfaz,'')  as codigo_centro_costo, "+
		"cc.unidad_funcional as unidad_funcional, "+ 
		"coalesce(te.valor_unitario*dd.cantidad,0) as valor_tarifa, " +
		"getnombrepersona(c.codigo_paciente) as observaciones "+ 
		"FROM despacho d "+ 
		"INNER JOIN solicitudes s ON(s.numero_solicitud = d.numero_solicitud) " +
		"INNER JOIN cuentas c ON(c.id = s.cuenta) "+ 
		"INNER JOIN centros_costo cc ON(cc.codigo = s.centro_costo_solicitado) "+
		"INNER JOIN centros_costo cc2 ON(cc2.codigo = s.centro_costo_solicitante) "+
		"INNER JOIN detalle_despachos dd ON(dd.despacho = d.orden) "+ 
		"INNER JOIN tarifas_entidad_sub te ON(te.solicitud = d.numero_solicitud and te.articulo = dd.articulo) "+
		"WHERE "+ 
		"d.orden = ?";
	
	/**
	 * Cadena para consultar el detalle de la devolucion de medicamentos
	 */
	private static final String consultarDetalleDevolucionMedicamentosStr = "SELECT "+ 
		"ddd.articulo as servicio_articulo, "+
		"cc.codigo as consecutivo_centro_costo, "+
		"coalesce(cc2.codigo_interfaz,'')  as codigo_centro_costo, "+
		"cc.unidad_funcional as unidad_funcional, "+ 
		"coalesce(te.valor_unitario*ddd.cantidad,0) as valor_tarifa," +
		"getnombrepersona(c.codigo_paciente) as observaciones "+ 
		"FROM devolucion_med dd "+ 
		"INNER JOIN centros_costo cc ON(cc.codigo = dd.farmacia) "+ 
		"INNER JOIN centros_costo cc2 ON(cc2.codigo = dd.centro_costo_devuelve) "+
		"INNER JOIN detalle_devol_med ddd on (ddd.devolucion = dd.codigo) " +
		"INNER JOIN solicitudes s ON(s.numero_solicitud = ddd.numero_solicitud) " +
		"INNER JOIN cuentas c ON(c.id = s.cuenta) "+ 
		"INNER JOIN tarifas_entidad_sub te ON(te.solicitud = ddd.numero_solicitud and te.articulo = ddd.articulo) "+
		"WHERE "+ 
		"dd.codigo = ? ";
	
	/**
	 * Cadena para consultar el detalle del despacho de los pedidos
	 */
	private static final String consultarDetalleDespachoPedidosStr = "SELECT "+ 
		"ddp.articulo as servicio_articulo, "+
		"cc.codigo as consecutivo_centro_costo, "+
		"coalesce(cc.codigo_interfaz,'')  as codigo_centro_costo, "+
		"cc.unidad_funcional as unidad_funcional, "+ 
		"coalesce(te.valor_unitario*ddp.cantidad,0) as valor_tarifa, "+ 
		"getnomcentrocosto(p.centro_costo_solicitante) as observaciones "+ 
		"from despacho_pedido dp "+ 
		"INNER JOIN pedido p ON(p.codigo = dp.pedido and p.es_qx = '"+ConstantesBD.acronimoNo+"') "+ 
		"INNER JOIN centros_costo cc ON(cc.codigo = p.centro_costo_solicitado) "+
		"INNER JOIN detalle_despacho_pedido ddp ON(ddp.pedido = dp.pedido) "+ 
		"INNER JOIN tarifas_entidad_sub te ON(te.pedido = ddp.pedido and te.articulo = ddp.articulo) "+
		"WHERE "+ 
		"dp.consecutivo = ?";
	
	/**
	 * Cadena para consultar el detalle de la devolución de pedido
	 */
	private static final String consultarDetalleDevolucionPedidosStr = "SELECT "+ 
		"ddp.articulo as servicio_articulo, "+ 
		"cc.codigo as consecutivo_centro_costo, "+
		"coalesce(cc.codigo_interfaz,'')  as codigo_centro_costo, "+
		"cc.unidad_funcional as unidad_funcional, "+ 
		"coalesce(te.valor_unitario*ddp.cantidad,0) as valor_tarifa, "+ 
		"getnomcentrocosto(p.centro_costo_solicitante) as observaciones "+ 
		"FROM devolucion_pedidos dp "+ 
		"INNER JOIN detalle_devol_pedido ddp ON(ddp.devolucion = dp.codigo) "+ 
		"INNER JOIN pedido p ON(p.codigo = ddp.pedido and p.es_qx = '"+ConstantesBD.acronimoNo+"') "+ 
		"INNER JOIN centros_costo cc ON(cc.codigo = p.centro_costo_solicitado) "+ 
		"INNER JOIN tarifas_entidad_sub te ON(te.pedido = ddp.pedido and te.articulo = ddp.articulo) "+ 
		"WHERE "+ 
		"dp.codigo = ? ";
	
	/**
	 * Cadena para consultar el detalle del despacho pedido qx.
	 */
	private static final String consultarDetalleDespachoPedidosQxStr = "SELECT "+ 
		"ddp.articulo as servicio_articulo, "+
		"cc.codigo as consecutivo_centro_costo, "+
		"coalesce(cc.codigo_interfaz,'')  as codigo_centro_costo, "+
		"cc.unidad_funcional as unidad_funcional, "+ 
		"coalesce(te.valor_unitario*ddp.cantidad,0) as valor_tarifa, "+ 
		"getnombrepersona(pq.paciente) as observaciones "+ 
		"from despacho_pedido dp "+ 
		"INNER JOIN pedido p ON(p.codigo = dp.pedido) "+ 
		"INNER JOIN pedidos_peticiones_qx ppq ON(ppq.pedido = p.codigo) "+ 
		"INNER JOIN peticion_qx pq ON(pq.codigo = ppq.peticion) "+
		"INNER JOIN centros_costo cc ON(cc.codigo = p.centro_costo_solicitado) "+
		"INNER JOIN detalle_despacho_pedido ddp ON(ddp.pedido = dp.pedido) "+ 
		"INNER JOIN tarifas_entidad_sub te ON(te.pedido = ddp.pedido and te.articulo = ddp.articulo) "+
		"WHERE "+ 
		"dp.consecutivo = ? ";
	
	/**
	 * Cadena que consulta el detalle de las devoluciones del pedido qx.
	 */
	private static final String consultarDetalleDevolucionPedidosQxStr = "SELECT "+ 
		"ddp.articulo as servicio_articulo, "+ 
		"cc.codigo as consecutivo_centro_costo, "+
		"coalesce(cc.codigo_interfaz,'')  as codigo_centro_costo, "+
		"cc.unidad_funcional as unidad_funcional, "+ 
		"coalesce(te.valor_unitario*ddp.cantidad,0) as valor_tarifa, "+ 
		"getnombrepersona(pq.paciente) as observaciones "+ 
		"FROM devolucion_pedidos dp "+ 
		"INNER JOIN detalle_devol_pedido ddp ON(ddp.devolucion = dp.codigo) "+ 
		"INNER JOIN pedido p ON(p.codigo = ddp.pedido and p.es_qx = '"+ConstantesBD.acronimoSi+"') "+ 
		"INNER JOIN pedidos_peticiones_qx ppq ON(ppq.pedido = p.codigo) "+ 
		"INNER JOIN peticion_qx pq ON(pq.codigo = ppq.peticion) "+
		"INNER JOIN centros_costo cc ON(cc.codigo = p.centro_costo_solicitado) "+ 
		"INNER JOIN tarifas_entidad_sub te ON(te.pedido = ddp.pedido and te.articulo = ddp.articulo) "+ 
		"WHERE "+ 
		"dp.codigo = ? ";
	
	/**
	 * Cadena que consulta las anulaciones de farmacia
	 */
	private static final String consultarDetalleAnulacionesFarmaciaStr = "SELECT "+ 
		"da.articulo as servicio_articulo, "+
		"cc.codigo as consecutivo_centro_costo, "+
		"coalesce(cc2.codigo_interfaz,'')  as codigo_centro_costo, "+
		"cc.unidad_funcional as unidad_funcional, "+ 
		"coalesce(te.valor_unitario*da.cantidad_anulada,0) as valor_tarifa, "+ 
		"getnombrepersona(c.codigo_paciente) as observaciones "+ 
		"FROM anulacion_cargos_farmacia ac  "+ 
		"INNER JOIN solicitudes s ON(s.numero_solicitud = ac.numero_solicitud) "+
		"INNER JOIN cuentas c ON(c.id = s.cuenta) "+ 
		"INNER JOIN centros_costo cc ON(cc.codigo = s.centro_costo_solicitado) "+ 
		"INNER JOIN centros_costo cc2 ON(cc2.codigo = s.centro_costo_solicitante) "+
		"INNER JOIN det_anul_cargos_farmacia da ON(da.codigo_anulacion = ac.codigo) "+ 
		"INNER JOIN tarifas_entidad_sub te ON(te.solicitud = da.numero_solicitud and te.articulo = da.articulo) "+ 
		"WHERE "+ 
		"ac.codigo = ?";
	//*****************CONSULTAS PARA LINEA MOVIMIENTO DE CUENTA X PAGAR ENTIDADES EXTERNAS****************************************+
	/**
	 * Cadena que consulta informacion de autorizacion para cuentas x pagar
	 */
	private static final String consultarDetalleAutorizacionesEntidadesSub02Str = "SELECT "+ 
		"cc.codigo as codigo, "+ 
		"cc.cuenta_contable as cuenta_contable, "+ 
		"cc.descripcion as descripcion, "+ 
		"cc.activo as activo, "+ 
		"cc.manejo_terceros as manejo_terceros, "+ 
		"cc.manejo_centros_costo as manejo_centros_costo, "+ 
		"cc.manejo_base_gravable as manejo_base_gravable, "+ 
		"cc.naturaleza_cuenta as naturaleza_cuenta, "+ 
		"coalesce(cc.institucion,"+ConstantesBD.codigoNuncaValido+") as institucion, "+ 
		"cc.anio_vigencia as anio_vigencia, "+
		"getnumeroidentificaciontercero(es.tercero) as id_tercero, "+
		"getnombrepersona(ae.codigo_paciente) as observacion, "+
		"coalesce(es.dias_vencimiento_factura,0) as dias, "+
		"to_char(ae.fecha_autorizacion,'"+ConstantesBD.formatoFechaAp+"') as fecha, "+ 
		"coalesce(te.valor_unitario,0)  as valor "+ 
		"FROM autorizaciones_entidades_sub ae "+ 
		"INNER JOIN entidades_subcontratadas es ON(es.codigo_pk = ae.entidad_autorizada_sub) "+ 
		"INNER JOIN tarifas_entidad_sub te ON(te.autorizacion = ae.consecutivo) "+
		"LEFT OUTER JOIN cuentas_contables cc ON(cc.codigo = es.cuenta_cxp) "+
		"WHERE "+ 
		"ae.consecutivo_autorizacion = ?";
	
	/**
	 * CAdena que consulta informacionde despachos medicamentos para cuentas x pagar
	 */
	private static final String consultarDetalleDespachoMedicamentos02Str = "SELECT "+ 
		"cc.codigo as codigo, "+ 
		"cc.cuenta_contable as cuenta_contable, "+ 
		"cc.descripcion as descripcion, "+ 
		"cc.activo as activo, "+ 
		"cc.manejo_terceros as manejo_terceros, "+ 
		"cc.manejo_centros_costo as manejo_centros_costo, "+ 
		"cc.manejo_base_gravable as manejo_base_gravable, "+ 
		"cc.naturaleza_cuenta as naturaleza_cuenta, "+ 
		"coalesce(cc.institucion,"+ConstantesBD.codigoNuncaValido+") as institucion, "+ 
		"cc.anio_vigencia as anio_vigencia, "+
		"getnumeroidentificaciontercero(es.tercero) as id_tercero, "+
		"getnombrepersona(c.codigo_paciente) as observacion, "+
		"coalesce(es.dias_vencimiento_factura,0) as dias, "+
		"to_char(d.fecha,'"+ConstantesBD.formatoFechaAp+"') as fecha, "+ 
		"sum(coalesce(te.valor_unitario*dd.cantidad,0))  as valor "+ 
		"FROM despacho d "+ 
		"INNER JOIN solicitudes s ON(s.numero_solicitud = d.numero_solicitud) "+ 
		"INNER JOIN cuentas c ON(c.id = s.cuenta) "+
		"INNER JOIN detalle_despachos dd ON(dd.despacho = d.orden) "+ 
		"INNER JOIN tarifas_entidad_sub te ON(te.solicitud = d.numero_solicitud and te.articulo = dd.articulo) "+ 
		"INNER JOIN entidades_subcontratadas es ON(es.codigo_pk = te.entidad_subcontratada) "+ 
		"LEFT OUTER JOIN cuentas_contables cc ON(cc.codigo = es.cuenta_cxp) "+ 
		"WHERE d.orden = ? "+ 
		"group by cc.codigo,cc.cuenta_contable,cc.descripcion, "+
		"cc.activo,cc.manejo_terceros,cc.manejo_centros_costo, "+
		"cc.manejo_base_gravable,cc.naturaleza_cuenta,cc.institucion,cc.anio_vigencia, "+
		"es.tercero,c.codigo_paciente,es.dias_vencimiento_factura,d.fecha";
	
	/**
	 * Consulta de la informacion de cuentas x pagar para el detalle de los despachos de pedidos
	 */
	private static final String consultarDetalleDespachoPedidos02Str = "SELECT "+ 
		"cc.codigo as codigo, "+ 
		"cc.cuenta_contable as cuenta_contable, "+ 
		"cc.descripcion as descripcion, "+ 
		"cc.activo as activo, "+ 
		"cc.manejo_terceros as manejo_terceros, "+ 
		"cc.manejo_centros_costo as manejo_centros_costo, "+ 
		"cc.manejo_base_gravable as manejo_base_gravable, "+ 
		"cc.naturaleza_cuenta as naturaleza_cuenta, "+ 
		"coalesce(cc.institucion,"+ConstantesBD.codigoNuncaValido+") as institucion, "+ 
		"cc.anio_vigencia as anio_vigencia, "+
		"getnumeroidentificaciontercero(es.tercero) as id_tercero, "+
		"getnomcentrocosto(p.centro_costo_solicitante) as observacion, "+
		"coalesce(es.dias_vencimiento_factura,0) as dias, "+
		"to_char(dp.fecha,'"+ConstantesBD.formatoFechaAp+"') as fecha, "+ 
		"sum(coalesce(te.valor_unitario*ddp.cantidad,0))  as valor "+ 
		"FROM despacho_pedido dp "+ 
		"INNER JOIN pedido p ON(p.codigo = dp.pedido) "+ 
		"INNER JOIN detalle_despacho_pedido ddp on(ddp.pedido = dp.pedido) "+ 
		"INNER JOIN tarifas_entidad_sub te ON(te.pedido = ddp.pedido and te.articulo = ddp.articulo) "+ 
		"INNER JOIN entidades_subcontratadas es ON(es.codigo_pk = te.entidad_subcontratada) "+ 
		"LEFT OUTER JOIN cuentas_contables cc ON(cc.codigo = es.cuenta_cxp) "+ 
		"WHERE dp.consecutivo = ? "+ 
		"group by cc.codigo,cc.cuenta_contable,cc.descripcion, "+
		"cc.activo,cc.manejo_terceros,cc.manejo_centros_costo, "+
		"cc.manejo_base_gravable,cc.naturaleza_cuenta,cc.institucion,cc.anio_vigencia, "+ 
		"es.tercero,p.centro_costo_solicitante,es.dias_vencimiento_factura,dp.fecha";
	
	/**
	 * Cadena que consulta las anulaciones de farmacia
	 */
	private static final String consultarDetalleAnulacionesFarmacia02Str = "SELECT "+ 
		"cc.codigo as codigo, "+ 
		"cc.cuenta_contable as cuenta_contable, "+ 
		"cc.descripcion as descripcion, "+ 
		"cc.activo as activo, "+ 
		"cc.manejo_terceros as manejo_terceros, "+ 
		"cc.manejo_centros_costo as manejo_centros_costo, "+ 
		"c.manejo_base_gravable as manejo_base_gravable, "+ 
		"cc.naturaleza_cuenta as naturaleza_cuenta, "+ 
		"coalesce(cc.institucion,"+ConstantesBD.codigoNuncaValido+") as institucion, "+ 
		"cc.anio_vigencia as anio_vigencia, "+
		"getnumeroidentificaciontercero(es.tercero) as id_tercero, "+
		"getnombrepersona(c.codigo_paciente) as observacion, "+
		"coalesce(es.dias_vencimiento_factura,0) as dias, "+
		"to_char(ac.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') as fecha, "+ 
		"sum(coalesce(te.valor_unitario*dac.cantidad_anulada,0))  as valor "+ 
		"FROM anulacion_cargos_farmacia ac "+ 
		"INNER JOIN solicitudes s on(s.numero_solicitud = ac.numero_solicitud) "+
		"INNER JOIN cuentas c ON(c.id = s.cuenta) "+
		"INNER JOIN det_anul_cargos_farmacia dac ON(dac.codigo_anulacion = ac.codigo) "+ 
		"INNER JOIN tarifas_entidad_sub te ON(te.solicitud = ac.numero_solicitud and te.articulo = dac.articulo) "+ 
		"INNER JOIN entidades_subcontratadas es ON(es.codigo_pk = te.entidad_subcontratada) "+ 
		"LEFT OUTER JOIN cuentas_contables cc ON(cc.codigo = es.cuenta_cxp) "+ 
		"WHERE ac.codigo = =? "+ 
		"group by cc.codigo,cc.cuenta_contable,cc.descripcion,cc.activo,cc.manejo_terceros,cc.manejo_centros_costo, "+
		"cc.manejo_base_gravable,cc.naturaleza_cuenta,cc.institucion,cc.anio_vigencia,es.tercero,c.codigo_paciente,es.dias_vencimiento_factura,ac.fecha_modifica ";
	
	/**
	 * Cadena que consulta la informacion cueta x pagar para las devoluciones de medicamentos
	 */
	private static final String consultarDetalleDevolucionMedicamentos02Str = "SELECT "+ 
		"cc.codigo as codigo, "+ 
		"cc.cuenta_contable as cuenta_contable, "+ 
		"cc.descripcion as descripcion, "+ 
		"cc.activo as activo, "+ 
		"cc.manejo_terceros as manejo_terceros, "+ 
		"cc.manejo_centros_costo as manejo_centros_costo, "+ 
		"cc.manejo_base_gravable as manejo_base_gravable, "+ 
		"cc.naturaleza_cuenta as naturaleza_cuenta, "+ 
		"coalesce(cc.institucion,"+ConstantesBD.codigoNuncaValido+") as institucion, "+ 
		"cc.anio_vigencia as anio_vigencia, "+
		"getnumeroidentificaciontercero(es.tercero) as id_tercero, "+
		"getnombrepersona(c.codigo_paciente) as observacion, "+
		"coalesce(es.dias_vencimiento_factura,0) as dias, "+
		"to_char(d.fecha,'"+ConstantesBD.formatoFechaAp+"') as fecha, "+ 
		"sum(coalesce(te.valor_unitario*dd.cantidad,0))  as valor "+ 
		"FROM devolucion_med d "+ 
		"INNER JOIN detalle_devol_med dd ON(dd.devolucion = d.codigo) "+ 
		"INNER JOIN solicitudes s ON(s.numero_solicitud = dd.numero_solicitud) "+ 
		"INNER JOIN cuentas c ON(c.id = s.cuenta) "+ 
		"INNER JOIN tarifas_entidad_sub te ON(te.solicitud = dd.numero_solicitud and te.articulo = dd.articulo) "+ 
		"INNER JOIN entidades_subcontratadas es ON(es.codigo_pk = te.entidad_subcontratada) "+ 
		"LEFT OUTER JOIN cuentas_contables cc ON(cc.codigo = es.cuenta_cxp) "+ 
		"WHERE d.codigo = ? "+ 
		"GROUP BY cc.codigo, cc.cuenta_contable, cc.descripcion, cc.activo, cc.manejo_terceros, cc.manejo_centros_costo, "+
		"cc.manejo_base_gravable,cc.naturaleza_cuenta, cc.institucion, cc.anio_vigencia, es.tercero, c.codigo_paciente, es.dias_vencimiento_factura, d.fecha";
	
	/**
	 * Cadena que consulta informacion de cuengtas x pagar para el detalle de devolucion pedidos
	 */
	private static final String consultarDetalleDevolucionPedido02Str = "SELECT "+ 
		"cc.codigo as codigo, "+ 
		"cc.cuenta_contable as cuenta_contable, "+ 
		"cc.descripcion as descripcion, "+ 
		"cc.activo as activo, "+ 
		"cc.manejo_terceros as manejo_terceros, "+ 
		"cc.manejo_centros_costo as manejo_centros_costo, "+ 
		"cc.manejo_base_gravable as manejo_base_gravable, "+ 
		"cc.naturaleza_cuenta as naturaleza_cuenta, "+ 
		"coalesce(cc.institucion,"+ConstantesBD.codigoNuncaValido+") as institucion, "+ 
		"cc.anio_vigencia as anio_vigencia, "+
		"getnumeroidentificaciontercero(es.tercero) as id_tercero, "+
		"getnomcentrocosto(p.centro_costo_solicitante) as observacion, "+
		"coalesce(es.dias_vencimiento_factura,0) as dias, "+
		"to_char(dp.fecha,'"+ConstantesBD.formatoFechaAp+"') as fecha, "+ 
		"sum(coalesce(te.valor_unitario*ddp.cantidad,0))  as valor "+ 
		"FROM devolucion_pedidos dp "+ 
		"INNER JOIN detalle_devol_pedido ddp ON(ddp.devolucion = dp.codigo) "+ 
		"INNER JOIN pedido p ON(p.codigo = ddp.pedido) "+ 
		"INNER JOIN tarifas_entidad_sub te ON(te.pedido = p.codigo and te.articulo = ddp.articulo) "+ 
		"INNER JOIN entidades_subcontratadas es ON(es.codigo_pk = te.entidad_subcontratada) "+ 
		"LEFT OUTER JOIN cuentas_contables cc ON(cc.codigo = es.cuenta_cxp) "+ 
		"WHERE dp.codigo = ? "+ 
		"GROUP BY cc.codigo, cc.cuenta_contable, cc.descripcion, cc.activo, cc.manejo_terceros, cc.manejo_centros_costo, "+
		"cc.manejo_base_gravable,cc.naturaleza_cuenta, cc.institucion, cc.anio_vigencia, es.tercero, p.centro_costo_solicitante, es.dias_vencimiento_factura, dp.fecha";
	
	/**
	 * Cadena que consulta la informacion de cyuentas x pagar de la devolucion de pedido qx
	 */
	private static final String consultarDetalleDevolucionPedidosQx02Str = "SELECT "+ 
		"cc.codigo as codigo, "+ 
		"cc.cuenta_contable as cuenta_contable, "+ 
		"cc.descripcion as descripcion, "+ 
		"cc.activo as activo, "+ 
		"cc.manejo_terceros as manejo_terceros, "+ 
		"cc.manejo_centros_costo as manejo_centros_costo, "+ 
		"cc.manejo_base_gravable as manejo_base_gravable, "+ 
		"cc.naturaleza_cuenta as naturaleza_cuenta, "+ 
		"coalesce(cc.institucion,"+ConstantesBD.codigoNuncaValido+") as institucion, "+ 
		"cc.anio_vigencia as anio_vigencia, "+
		"getnumeroidentificaciontercero(es.tercero) as id_tercero, "+
		"getnombrepersona(pq.paciente) as observacion, "+
		"coalesce(es.dias_vencimiento_factura,0) as dias, "+
		"to_char(dp.fecha,'"+ConstantesBD.formatoFechaAp+"') as fecha, "+ 
		"sum(coalesce(te.valor_unitario*ddp.cantidad,0))  as valor "+ 
		"FROM devolucion_pedidos dp "+ 
		"INNER JOIN detalle_devol_pedido ddp ON(ddp.devolucion = dp.codigo) "+ 
		"INNER JOIN pedidos_peticiones_qx ppq ON(ppq.pedido = ddp.pedido) "+ 
		"INNER JOIN peticion_qx pq ON(pq.codigo = ppq.peticion) "+ 
		"INNER JOIN tarifas_entidad_sub te ON(te.pedido = ppq.pedido and te.articulo = ddp.articulo) "+ 
		"INNER JOIN entidades_subcontratadas es ON(es.codigo_pk = te.entidad_subcontratada) "+ 
		"LEFT OUTER JOIN cuentas_contables cc ON(cc.codigo = es.cuenta_cxp) "+ 
		"WHERE dp.codigo = ? "+ 
		"GROUP BY cc.codigo, cc.cuenta_contable, cc.descripcion, cc.activo, cc.manejo_terceros, cc.manejo_centros_costo, "+
		"cc.manejo_base_gravable,cc.naturaleza_cuenta, cc.institucion, cc.anio_vigencia, es.tercero, pq.paciente, es.dias_vencimiento_factura, dp.fecha";
	
	//********************CONSULTAS FLUJO VENTAS Y HONORARIOS**********************************************
	/**
	 * Cadena que realiza consulta de los documentos contables de ventas y honorarios
	 */
	private static final String consultarDocumentosContablesVentasStr = "SELECT "+
		//FACTURAS PACIENTES
		ConstantesBD.codigoTipoDocInteFacturaPaciente+" as tipo_documento01, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteFacturaPaciente+") as descripcion_documento01, "+
	     " f.consecutivo_factura||'' AS numero_documento01, " +		
		ConstantesBD.tipoConsecutivoInteNumeroFactura+" as tipo_consecutivo01, "+
		"to_char(f.fecha,'"+ConstantesBD.formatoFechaAp+"') as fecha01, "+
		"coalesce(ca.codigo,'') as codigo_centro_atencion, "+
		ConstantesBD.codigoNuncaValido+" as tipo_documento02, "+
		"'' as descripcion_documento02, "+
		"'' as numero_documento02, "+
		ConstantesBD.codigoNuncaValido+" as tipo_consecutivo02, "+ 
		"'' as fecha02, " +
		"f.valor_total as valor "+  
		"FROM facturacion.facturas f " +
		" LEFT OUTER JOIN administracion.centro_atencion ca ON(ca.consecutivo = f.centro_aten) "+ 
		"WHERE "+ 
		"to_char(f.fecha,'YYYY-MM-DD') = ? and f.estado_facturacion in ("+ConstantesBD.codigoEstadoFacturacionFacturada+","+ConstantesBD.codigoEstadoFacturacionAnulada+") and (f.contabilizado IS NULL OR f.contabilizado = '"+ConstantesBD.acronimoNo+"') and f.tipo_factura_sistema = "+ValoresPorDefecto.getValorTrueParaConsultas()+" "+
		"UNION "+
		//ANULACION FACTURAS PACIENTE	
		"SELECT "+ 
		ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente+" as tipo_documento01, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente+") as descripcion_documento01, "+
		" af.consecutivo_anulacion  AS numero_documento01, " +
		ConstantesBD.tipoConsecutivoInteNumeroAnulacion+" as tipo_consecutivo01, "+
		"to_char(af.fecha_grabacion,'"+ConstantesBD.formatoFechaAp+"') as fecha01, "+
		"coalesce(ca.codigo,'') as codigo_centro_atencion, "+
		ConstantesBD.codigoTipoDocInteFacturaPaciente+" as tipo_documento02, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteFacturaPaciente+") as descripcion_documento02, "+
	     "f.consecutivo_factura||''  AS numero_documento02, " +
		ConstantesBD.tipoConsecutivoInteNumeroFactura+" as tipo_consecutivo02, "+
		"to_char(f.fecha,'"+ConstantesBD.formatoFechaAp+"') as fecha02, "+
		"f.valor_total as valor "+
		"FROM facturacion.anulaciones_facturas af "+ 
		"INNER JOIN facturacion.facturas f ON(f.codigo = af.codigo) "+ 
		"LEFT OUTER JOIN administracion.centro_atencion ca ON(ca.consecutivo = f.centro_aten) "+
		"WHERE "+ 
		"to_char(af.fecha_grabacion,'YYYY-MM-DD') = ? and (af.contabilizado IS NULL OR af.contabilizado = '"+ConstantesBD.acronimoNo+"')  and f.tipo_factura_sistema = "+ValoresPorDefecto.getValorTrueParaConsultas()+" "+ 
		"UNION "+ 
		//FACTURAS VARIAS
		"SELECT "+ 
		ConstantesBD.codigoTipoDocInteFacturasVarias+" as tipo_documento01, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteFacturasVarias+") as descripcion_documento01, "+
		"fv.consecutivo||'' as numero_documento01, "+
		ConstantesBD.tipoConsecutivoInteNumeroFacturaVaria+" as tipo_consecutivo01, "+
		"to_char(fv.fecha,'"+ConstantesBD.formatoFechaAp+"') as fecha01, "+
		"coalesce(ca.codigo,'') as codigo_centro_atencion, "+
		ConstantesBD.codigoNuncaValido+" as tipo_documento02, "+
		"'' as descripcion_documento02, "+
		"'' as numero_documento02, "+
		ConstantesBD.codigoNuncaValido+" as tipo_consecutivo02, "+ 
		"'' as fecha02, "+
		"fv.valor_factura as valor "+
		"FROM facturasvarias.facturas_varias fv " +
		"LEFT OUTER JOIN administracion.centro_atencion ca ON(ca.consecutivo = fv.centro_atencion) "+ 
		"WHERE " + 
		"to_char(fv.fecha_aprobacion,'YYYY-MM-DD') = ? and (fv.estado_factura = '"+ConstantesIntegridadDominio.acronimoEstadoAprobado+"' or (fv.estado_factura = '"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"' and fv.anula_fac_apro = '"+ConstantesBD.acronimoSi+"')) and (fv.contabilizado IS NULL OR fv.contabilizado = '"+ConstantesBD.acronimoNo+"') "+
		//ANULACION FACTURAS VARIAS
		"UNION "+ 
		"SELECT "+ 
		ConstantesBD.codigoTipoDocInteAnulaFacturasVarias+" as tipo_documento01, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteAnulaFacturasVarias+") as descripcion_documento01, "+
		"fv.consecutivo||'' as numero_documento01, "+
		ConstantesBD.tipoConsecutivoInteNumeroAnulacion+" as tipo_consecutivo01, "+
		"to_char(fv.fecha_anulacion,'"+ConstantesBD.formatoFechaAp+"') as fecha01, "+
		"coalesce(ca.codigo,'') as codigo_centro_atencion, "+
		ConstantesBD.codigoTipoDocInteFacturasVarias+" as tipo_documento02, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteFacturasVarias+") as descripcion_documento02, "+
		"fv.consecutivo||'' as numero_documento02, "+
		ConstantesBD.tipoConsecutivoInteNumeroFacturaVaria+" as tipo_consecutivo02, "+ 
		"to_char(fv.fecha,'"+ConstantesBD.formatoFechaAp+"') as fecha02, "+
		"fv.valor_factura as valor "+
		"FROM facturasvarias.facturas_varias fv "+ 
		"LEFT OUTER JOIN administracion.centro_atencion ca ON(ca.consecutivo = fv.centro_atencion) "+
		"WHERE "+ 
		"to_char(fv.fecha_anulacion,'YYYY-MM-DD') = ? and fv.estado_factura = '"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"' and fv.anula_fac_apro = '"+ConstantesBD.acronimoSi+"' and (fv.contabilizado_anulacion IS NULL OR fv.contabilizado_anulacion = '"+ConstantesBD.acronimoNo+"') "+ 
		"UNION "+ 
		//AJUSTES FACTURAS VARIAS
		"SELECT "+ 
		ConstantesBD.codigoTipoDocInteAjusFacturasVarias+" as tipo_documento01, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteAjusFacturasVarias+") as descripcion_documento01, "+
		"af.consecutivo||'' as numero_documento01, "+
		ConstantesBD.tipoConsecutivoInteNumeroAjuste+" as tipo_consecutivo01, "+
		"to_char(af.fecha_ajuste,'"+ConstantesBD.formatoFechaAp+"') as fecha01, "+
		"coalesce(ca.codigo,'') as codigo_centro_atencion, "+
		ConstantesBD.codigoTipoDocInteFacturasVarias+" as tipo_documento02, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteFacturasVarias+") as descripcion_documento02, "+
		"fv.consecutivo||'' as numero_documento02, "+
		ConstantesBD.tipoConsecutivoInteNumeroFacturaVaria+" as tipo_consecutivo02, "+ 
		"to_char(fv.fecha,'"+ConstantesBD.formatoFechaAp+"') as fecha02, " +
		"af.valor_ajuste as valor "+ 
		"FROM ajus_facturas_varias af "+ 
		"INNER JOIN facturasvarias.facturas_varias fv ON(fv.codigo_fac_var = af.factura) "+
		"LEFT OUTER JOIN administracion.centro_atencion ca ON(ca.consecutivo = fv.centro_atencion) "+
		"WHERE "+  
		"to_char(af.fecha_aprob_anul,'YYYY-MM-DD') = ? and af.estado = '"+ConstantesIntegridadDominio.acronimoEstadoAprobado+"' and (af.contabilizado IS NULL OR af.contabilizado = '"+ConstantesBD.acronimoNo+"')  "+ 
		"UNION "+ 
		//CUENTAS DE COBRO CAPITACION
		"SELECT "+ 
		ConstantesBD.codigoTipoDocInteCCCapitacion+" as tipo_documento01, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteCCCapitacion+") as descripcion_documento01, "+
		"cc.numero_cuenta_cobro ||'' as numero_documento01, "+
		ConstantesBD.tipoConsecutivoInteNumeroCC+" as tipo_consecutivo01, "+
		"to_char(cc.fecha_elaboracion,'"+ConstantesBD.formatoFechaAp+"') as fecha01, "+
		"coalesce(ca.codigo,'') as codigo_centro_atencion, "+
		ConstantesBD.codigoNuncaValido+" as tipo_documento02, "+
		"'' as descripcion_documento02, "+
		"'' as numero_documento02, "+
		ConstantesBD.codigoNuncaValido+" as tipo_consecutivo02, "+ 
		"'' as fecha02, " +
		"cc.valor_inicial_cuenta as valor "+  
		"FROM capitacion.cuentas_cobro_capitacion cc "+ 
		"INNER JOIN administracion.centro_atencion ca ON(ca.consecutivo = cc.centro_atencion) "+
		"WHERE "+ 
		"to_char(cc.fecha_radicacion,'YYYY-MM-DD') = ? and cc.estado = "+ConstantesBD.codigoEstadoCarteraRadicada+" and (cc.contabilizado IS NULL OR cc.contabilizado = '"+ConstantesBD.acronimoNo+"') "+ 
		"UNION "+ 
		//AJUSTES CUENTAS COBRO CAPITACION
		"SELECT "+ 
		ConstantesBD.codigoTipoDocInteAjustesCCCapitacion+" as tipo_documento01, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteAjustesCCCapitacion+") as descripcion_documento01, "+
		"ac.consecutivo ||'' as numero_documento01, "+
		ConstantesBD.tipoConsecutivoInteNumeroAjuste+" as tipo_consecutivo01, "+
		"to_char(ac.fecha,'"+ConstantesBD.formatoFechaAp+"') as fecha01, "+
		"coalesce(ca.codigo,'') as codigo_centro_atencion, "+
		ConstantesBD.codigoTipoDocInteCCCapitacion+" as tipo_documento02, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteCCCapitacion+") as descripcion_documento02, "+
		"cc.numero_cuenta_cobro ||'' as numero_documento02, "+
		ConstantesBD.tipoConsecutivoInteNumeroCC+" as tipo_consecutivo02, "+
		"to_char(cc.fecha_elaboracion,'"+ConstantesBD.formatoFechaAp+"') as fecha02, " +
		"ac.valor as valor "+  
		"FROM capitacion.aprobacion_ajustes_cxc aac "+ 
		"INNER JOIN  capitacion.ajustes_cxc ac on(ac.codigo = aac.codigo_ajuste ) "+ 
		"INNER JOIN capitacion.cuentas_cobro_capitacion cc ON(cc.numero_cuenta_cobro = ac.cuenta_cobro and cc.institucion = ac.institucion) "+
		"INNER JOIN administracion.centro_atencion ca ON(ca.consecutivo = cc.centro_atencion) "+
		"WHERE to_char(aac.fecha_aprobacion,'YYYY-MM-DD') = ? and ac.estado = "+ConstantesBD.codigoEstadoAjusteCxCAprobado+" and (ac.contabilizado IS NULL OR ac.contabilizado = '"+ConstantesBD.acronimoNo+"') ";

	/**
	 * Cadena que realiza consulta de los documentos contables de ventas y honorarios
	 */
	private static final String consultarDocumentosContablesHonorariosStr = "SELECT "+
		//FACTURAS PACIENTES
		ConstantesBD.codigoTipoDocInteFacturaPaciente+" as tipo_documento01, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteFacturaPaciente+") as descripcion_documento01, "+	
		"f.consecutivo_factura||'' AS numero_documento01, "+
		ConstantesBD.tipoConsecutivoInteNumeroFactura+" as tipo_consecutivo01, "+
		"to_char(f.fecha,'"+ConstantesBD.formatoFechaAp+"') as fecha01, "+
		"coalesce(ca.codigo,'') as codigo_centro_atencion, "+
		ConstantesBD.codigoNuncaValido+" as tipo_documento02, "+
		"'' as descripcion_documento02, "+
		"'' as numero_documento02, "+
		ConstantesBD.codigoNuncaValido+" as tipo_consecutivo02, "+ 
		"'' as fecha02 "+  
		"FROM facturas f " +
		"LEFT OUTER JOIN centro_atencion ca ON(ca.consecutivo = f.centro_aten) "+	
		"WHERE "+ 
		"to_char(f.fecha,'YYYY-MM-DD') = ? and f.estado_facturacion in ("+ConstantesBD.codigoEstadoFacturacionFacturada+","+ConstantesBD.codigoEstadoFacturacionAnulada+") and (f.contabilizado IS NULL OR f.contabilizado = '"+ConstantesBD.acronimoNo+"')  and f.tipo_factura_sistema = "+ValoresPorDefecto.getValorTrueParaConsultas()+" "+
		"UNION "+
		//ANULACION FACTURAS PACIENTE	
		"SELECT "+ 
		ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente+" as tipo_documento01, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente+") as descripcion_documento01, "+
		"af.consecutivo_anulacion as numero_documento01, "+
		ConstantesBD.tipoConsecutivoInteNumeroAnulacion+" as tipo_consecutivo01, "+
		"to_char(af.fecha_grabacion,'"+ConstantesBD.formatoFechaAp+"') as fecha01, "+
		"coalesce(ca.codigo,'') as codigo_centro_atencion, "+
		ConstantesBD.codigoTipoDocInteFacturaPaciente+" as tipo_documento02, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteFacturaPaciente+") as descripcion_documento02, "+
		"  f.consecutivo_factura||'' AS numero_documento02," +
		ConstantesBD.tipoConsecutivoInteNumeroFactura+" as tipo_consecutivo02, "+
		"to_char(f.fecha,'"+ConstantesBD.formatoFechaAp+"') as fecha02 "+    
		"FROM anulaciones_facturas af "+ 
		"INNER JOIN facturas f ON(f.codigo = af.codigo) "+ 
		"LEFT OUTER JOIN centro_atencion ca ON(ca.consecutivo = f.centro_aten) "+
		"WHERE "+ 
		"to_char(af.fecha_grabacion,'YYYY-MM-DD') = ? and (af.contabilizado IS NULL OR af.contabilizado = '"+ConstantesBD.acronimoNo+"') and f.tipo_factura_sistema = "+ValoresPorDefecto.getValorTrueParaConsultas()+" ";
	
	/**
	 * Cadena que consulta los datos de convenio de la factura
	 */
	private static final String consultarDatosConvenioFacturaStr = "SELECT "+ 
		"c.tipo_regimen as codigo_tipo_regimen, "+
		"getnumeroidentificaciontercero(e.tercero) as id_tercero "+ 
		"FROM facturas f "+ 
		"INNER JOIN convenios c ON(c.codigo = f.convenio) "+  
		"INNER JOIN empresas e on(e.codigo = c.empresa) "+
		"WHERE f.consecutivo_factura = ?"; 
	
	/**
	 * Cadena para consultar los datos del tercero de la factura 
	 */
	private static final String consultarDatosTerceroFacturaVaria = "SELECT "+ 
		"coalesce(getnumeroidentificaciontercero(cfv.tercero),'') as id_tercero_concepto, "+
		"CASE "+ 
			"WHEN d.codigo_paciente is not null then coalesce(p.numero_identificacion,'') "+ 
			"when d.codigo_empresa is not null then coalesce(getnumeroidentificaciontercero(e.tercero),'') "+ 
		"ELSE "+
			"coalesce(getnumeroidentificaciontercero(d.codigo_tercero),'') "+ 
		"END as id_tercero_deudor "+ 
		"FROM facturas_varias fv "+ 
		"INNER JOIN conceptos_facturas_varias cfv ON(cfv.consecutivo = fv.concepto) "+ 
		"INNER JOIN deudores d ON(d.codigo = fv.deudor) "+ 
		"LEFT OUTER JOIN empresas e ON(e.codigo = d.codigo_empresa) "+ 
		"LEFT OUTER JOIN personas p ON(p.codigo = d.codigo_paciente) "+ 
		"WHERE "+ 
		"fv.consecutivo = ?";
	
	/**
	 * Cadena para consultar el nit del tercero de la cuenta de cobro
	 */
	private static final String consultarNitTerceroCuentaCobroCapitacionStr = "SELECT "+
		"getnitconvenio(cc.convenio) as id_tercero "+
		"from cuentas_cobro_capitacion cc "+
		"where cc.numero_cuenta_cobro = ?";
	
	/**
	 * Cadena para consultar el detalle de las facturas paciente
	 */
	private static final String consultarDetalleFacturasPacienteStr = "" +
		//Consulta de los servicos sin asocio y articulos y la solicitud de paquete
		"SELECT " +  
		"coalesce(dfs.servicio,dfs.articulo) as servicio_articulo, " +
		"dfs.solicitud as numero_solicitud, "+
		"dfs.tipo_solicitud as tipo_solicitud, "+
		"dfs.valor_total as valor, "+
		"coalesce(dfs.valor_consumo_paquete,0) as valor_consumo_paquete, " +
		"cc1.codigo as centro_costo_solicitante, "+
		"cc1.codigo_interfaz as cod_centro_costo_solicitante, "+
		"cc2.unidad_funcional as uni_func_solicitado, "+
		"cc2.institucion as cod_inst_solicitado, "+ 
		"getnombrepersona(f.cod_paciente)  as observaciones, "+
		"dfs.valor_pool as valor_pool, "+
		"coalesce(dfs.pool,"+ConstantesBD.codigoNuncaValido+") as pool, "+ 
		"coalesce(s.especialidad_solicitada,"+ConstantesBD.codigoNuncaValido+") as especialidad_solicitada , cc1.nombre as nombre_centro_costo, "+ 
		"-1 AS cod_asocio_det_fac, " +
		"s.consecutivo_ordenes_medicas                             AS consecutivo_solicitud, " +
		"''                         AS tipo_asocio " +
		"FROM facturas f "+ 
		"INNER JOIN det_factura_solicitud dfs on (dfs.factura = f.codigo) "+ 
		"INNER JOIN solicitudes s ON(s.numero_solicitud = dfs.solicitud) "+ 
		"inner join centros_costo cc1 ON(cc1.codigo = s.centro_costo_solicitante) "+
		"inner join centros_costo cc2 ON(cc2.codigo = s.centro_costo_solicitado) "+
		"WHERE "+ 
		"f.consecutivo_factura = ? and dfs.tipo_solicitud <> "+ConstantesBD.codigoTipoSolicitudCirugia+"  " +
		"UNION "+ 
		//Consulta medicamentos especiales separados de los asocios
		"SELECT " +  
		"coalesce(dfs.servicio,dfs.articulo) as servicio_articulo, " +
		"dfs.solicitud as numero_solicitud, "+
		"dfs.tipo_solicitud as tipo_solicitud, "+
		"dfs.valor_total as valor, "+
		"coalesce(dfs.valor_consumo_paquete,0) as valor_consumo_paquete, " +
		"cc1.codigo as centro_costo_solicitante, "+
		"cc1.codigo_interfaz as cod_centro_costo_solicitante, "+
		"cc2.unidad_funcional as uni_func_solicitado, "+
		"cc2.institucion as cod_inst_solicitado, "+ 
		"getnombrepersona(f.cod_paciente)  as observaciones, "+
		"dfs.valor_pool as valor_pool, "+
		"coalesce(dfs.pool,"+ConstantesBD.codigoNuncaValido+") as pool, "+ 
		"coalesce(s.especialidad_solicitada,"+ConstantesBD.codigoNuncaValido+") as especialidad_solicitada , cc1.nombre as nombre_centro_costo, "+ 
		"-1 AS cod_asocio_det_fac, " +
		"s.consecutivo_ordenes_medicas                             AS consecutivo_solicitud, " +
		"''                         AS tipo_asocio " +
		"FROM facturas f "+ 
		"INNER JOIN det_factura_solicitud dfs on (dfs.factura = f.codigo) "+ 
		"INNER JOIN solicitudes s ON(s.numero_solicitud = dfs.solicitud) "+ 
		"inner join centros_costo cc1 ON(cc1.codigo = s.centro_costo_solicitante) "+
		"inner join centros_costo cc2 ON(cc2.codigo = s.centro_costo_solicitado) "+
		"WHERE "+ 
		"f.consecutivo_factura = ? and dfs.articulo IS NOT NULL " +
		"UNION "+ 
		
		//Consulta de los asocios de las cirugias
		"SELECT "+ 
		"adf.servicio_asocio as servicio_articulo, "+
		"dfs.solicitud as numero_solicitud, "+
		"dfs.tipo_solicitud as tipo_solicitud, "+
		"adf.valor_total as valor, "+
		"0 as valor_consumo_paquete, "+
		"cc1.codigo as centro_costo_solicitante, "+
		"cc1.codigo_interfaz as cod_centro_costo_solicitante, "+
		"cc2.unidad_funcional as uni_func_solicitado, "+
		"cc2.institucion as cod_inst_solicitado, "+ 
		"getnombrepersona(f.cod_paciente)  as observaciones, "+
		"adf.valor_pool as valor_pool, "+
		"coalesce(adf.pool,"+ConstantesBD.codigoNuncaValido+") as pool, "+ 
		"coalesce(adf.especialidad,"+ConstantesBD.codigoNuncaValido+") as especialidad_solicitada , cc1.nombre as nombre_centro_costo, "+		
		"adf.consecutivo AS cod_asocio_det_fac, " +
		"s.consecutivo_ordenes_medicas                             AS consecutivo_solicitud, " +
		"ta.nombre_asocio                         AS tipo_asocio " +
		"FROM facturas f "+ 
		"INNER JOIN det_factura_solicitud dfs on (dfs.factura = f.codigo) " +
		"INNER JOIN solicitudes_cirugia sc ON(sc.numero_solicitud = dfs.solicitud) "+ 
		"INNER JOIN asocios_det_factura adf ON(adf.codigo = dfs.codigo) "+
		"INNER JOIN solicitudes s ON(s.numero_solicitud = dfs.solicitud) "+ 
		"inner join centros_costo cc1 ON(cc1.codigo = s.centro_costo_solicitante) "+
		"inner join centros_costo cc2 ON(cc2.codigo = s.centro_costo_solicitado) "+  
		"left outer join tipos_asocio ta ON(ta.codigo = adf.tipo_asocio) "+ 
		"WHERE "+ 
		"f.consecutivo_factura = ? and " +
		"dfs.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" and " +
		"(ta.tipos_servicio <> '"+ConstantesBD.codigoServicioMaterialesCirugia+"' or (ta.tipos_servicio = '"+ConstantesBD.codigoServicioMaterialesCirugia+"' and sc.indi_tarifa_consumo_materiales <> '"+ConstantesBD.acronimoSi+"')) "+ 
		"UNION "+ 
		//Consulta del detalle de los paquetes
		"SELECT "+ 
		"coalesce(pdf.servicio,pdf.articulo) as servicio_articulo, "+
		"pdf.solicitud as numero_solicitud, "+
		"pdf.tipo_solicitud as tipo_solicitud, "+
		"pdf.valor_total as valor, "+
		"pdf.valor_dif_consumo_valor as valor_consumo_paquete, "+
		"cc1.codigo as centro_costo_solicitante, "+
		"cc1.codigo_interfaz as cod_centro_costo_solicitante, "+
		"cc2.unidad_funcional as uni_func_solicitado, "+
		"cc2.institucion as cod_inst_solicitado, "+ 
		"getnombrepersona(f.cod_paciente)  as observaciones, "+
		"pdf.valor_pool as valor_pool, "+
		"coalesce(pdf.pool,"+ConstantesBD.codigoNuncaValido+") as pool, "+ 
		"CASE WHEN pdf.tipo_asocio IS NULL THEN coalesce(s.especialidad_solicitada,"+ConstantesBD.codigoNuncaValido+") ELSE coalesce(pdf.especialidad_asocio,"+ConstantesBD.codigoNuncaValido+") END as especialidad_solicitada , cc1.nombre as nombre_centro_costo, "+
		"-1 AS cod_asocio_det_fac, " +
		"s.consecutivo_ordenes_medicas                             AS consecutivo_solicitud, " +
		"''                         AS tipo_asocio " +
		"FROM facturas f "+ 
		"INNER JOIN det_factura_solicitud dfs on (dfs.factura = f.codigo) "+ 
		"INNER JOIN paquetizacion_det_factura pdf ON(pdf.codigo_det_fact = dfs.codigo) "+
		"INNER JOIN solicitudes s ON(s.numero_solicitud = pdf.solicitud) "+ 
		"inner join centros_costo cc1 ON(cc1.codigo = s.centro_costo_solicitante) "+
		"inner join centros_costo cc2 ON(cc2.codigo = s.centro_costo_solicitado) "+  
		"left outer join tipos_asocio ta ON(ta.codigo = pdf.tipo_asocio) "+
		"WHERE "+ 
		"f.consecutivo_factura = ? and dfs.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudPaquetes+" "+
		"UNION "+
		//Consulta del detalle de articulos del consumo de materiales
		"SELECT "+
		"dadf.articulo as servicio_articulo, "+ 
		"dfs.solicitud as numero_solicitud, "+ 
		ConstantesBD.codigoTipoSolicitudMedicamentos+" as tipo_solicitud, "+ //Se pone este tipo de solicitud como comodin para obtener la cuenta contable 
		"dadf.valor_total as valor, "+ 
		"0 as valor_consumo_paquete, "+ 
		"cc1.codigo as centro_costo_solicitante, "+ 
		"cc1.codigo_interfaz as cod_centro_costo_solicitante, "+ 
		"cc2.unidad_funcional as uni_func_solicitado, "+ 
		"cc2.institucion as cod_inst_solicitado, "+  
		"getnombrepersona(f.cod_paciente)  as observaciones," +
		"0 as valor_pool, " +
		"null AS pool, " +
		ConstantesBD.codigoNuncaValido+" as especialidad_solicitada , cc1.nombre as nombre_centro_costo, "+
		"-1 AS cod_asocio_det_fac, " +
		"s.consecutivo_ordenes_medicas                             AS consecutivo_solicitud, " +
		"''                         AS tipo_asocio " +
		"FROM facturacion.facturas f "+  
		"INNER JOIN facturacion.det_factura_solicitud dfs on (dfs.factura = f.codigo) "+  
		"INNER JOIN salascirugia.solicitudes_cirugia sc ON(sc.numero_solicitud = dfs.solicitud) "+  
		"INNER JOIN facturacion.asocios_det_factura adf ON(adf.codigo = dfs.codigo) "+
		"INNER JOIN solicitudes s ON(s.numero_solicitud = dfs.solicitud) " +
		"INNER JOIN salascirugia.materiales_qx mq ON(mq.numero_solicitud = dfs.solicitud) "+  
		"inner join administracion.centros_costo cc1 ON(cc1.codigo = mq.centro_costo) "+ 
		"inner join administracion.centros_costo cc2 ON(cc2.codigo = mq.farmacia) "+   
		"inner join facturacion.det_asocios_det_factura dadf ON(dadf.asocio_det_factura = adf.consecutivo) "+  
		"WHERE "+  
		"f.consecutivo_factura = ? and dfs.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia; 
	
	/**
	 * Cadena que consulta el detalle para la linea movimiento de honorarios
	 */
	private static final String consultarDetalleFacturasPaciente04Str = "SELECT "+
		"dfs.servicio as servicio, "+
		"dfs.tipo_solicitud as tipo_solicitud, "+
		"dfs.valor_pool as valor, "+
		"cc1.codigo as centro_costo_solicitante, "+
		"cc1.unidad_funcional as uni_func_solicitante, "+
		"cc1.institucion as cod_inst_solicitante, "+
		"cc1.codigo_interfaz as cod_centro_costo_solicitante, "+
		"coalesce(s.especialidad_solicitada,"+ConstantesBD.codigoNuncaValido+") as especialidad_solicitada, "+
		"cc2.unidad_funcional as uni_func_solicitado, "+
		"cc2.institucion as cod_inst_solicitado, "+ 
		ConstantesBD.codigoNuncaValido+" as tipo_asocio, "+ 
		"'' as tipo_serv_asocio, "+
		"coalesce(dfs.pool,"+ConstantesBD.codigoNuncaValido+") as pool, "+ 
		"getnombrepersona(f.cod_paciente)  as observacion, " +
		"getnumeroidentificaciontercero(p.tercero_responsable) as id_tercero " + 
		"FROM facturas f "+ 
		"INNER JOIN det_factura_solicitud dfs on (dfs.factura = f.codigo) " +
		"INNER JOIN pooles p ON(p.codigo = dfs.pool) "+ 
		"INNER JOIN solicitudes s ON(s.numero_solicitud = dfs.solicitud) "+ 
		"inner join centros_costo cc1 ON(cc1.codigo = s.centro_costo_solicitante) "+
		"inner join centros_costo cc2 ON(cc2.codigo = s.centro_costo_solicitado) "+
		"WHERE "+ 
		"f.consecutivo_factura = ? and dfs.tipo_solicitud NOT IN ("+ConstantesBD.codigoTipoSolicitudCirugia+","+ConstantesBD.codigoTipoSolicitudMedicamentos+","+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+","+ConstantesBD.codigoTipoSolicitudPaquetes+","+ConstantesBD.codigoTipoSolicitudEstancia+") " +
		"AND dfs.valor_pool > 0 "+  
		"UNION ALL "+ 
		//tener en cuenta los excentos
		"SELECT "+ 
		"dfs.servicio as servicio, "+
		"dfs.tipo_solicitud as tipo_solicitud, "+
		"dfs.valor_pool as valor, "+
		"cc1.codigo as centro_costo_solicitante, "+
		"cc1.unidad_funcional as uni_func_solicitante, "+
		"cc1.institucion as cod_inst_solicitante, "+
		"cc1.codigo_interfaz as cod_centro_costo_solicitante, "+
		"coalesce(s.especialidad_solicitada,"+ConstantesBD.codigoNuncaValido+") as especialidad_solicitada, "+
		"cc2.unidad_funcional as uni_func_solicitado, "+
		"cc2.institucion as cod_inst_solicitado, "+ 
		ConstantesBD.codigoNuncaValido+" as tipo_asocio, "+ 
		"'' as tipo_serv_asocio, "+
		"coalesce(dfs.pool,"+ConstantesBD.codigoNuncaValido+") as pool, "+ 
		"getnombrepersona(f.cod_paciente)  as observacion, " +
		"getnumeroidentificaciontercero(p.tercero_responsable) as id_tercero " + 
		"FROM facturas f "+ 
		"INNER JOIN facturacion.det_factura_solicitud_excenta dfs on (dfs.factura = f.codigo) " +
		"INNER JOIN pooles p ON(p.codigo = dfs.pool) "+ 
		"INNER JOIN solicitudes s ON(s.numero_solicitud = dfs.solicitud) "+ 
		"inner join centros_costo cc1 ON(cc1.codigo = s.centro_costo_solicitante) "+
		"inner join centros_costo cc2 ON(cc2.codigo = s.centro_costo_solicitado) "+
		"WHERE "+ 
		"f.consecutivo_factura = ? and dfs.tipo_solicitud NOT IN ("+ConstantesBD.codigoTipoSolicitudCirugia+","+ConstantesBD.codigoTipoSolicitudMedicamentos+","+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+","+ConstantesBD.codigoTipoSolicitudPaquetes+","+ConstantesBD.codigoTipoSolicitudEstancia+") " +
		"AND dfs.valor_pool > 0 "+  
		"UNION ALL "+ 
		//cirugias
		"SELECT "+ 
		"adf.servicio_asocio as servicio, "+
		"dfs.tipo_solicitud as tipo_solicitud, "+
		"adf.valor_pool as valor, "+
		"cc1.codigo as centro_costo_solicitante, "+
		"cc1.unidad_funcional as uni_func_solicitante, "+
		"cc1.institucion as cod_inst_solicitante, "+
		"cc1.codigo_interfaz as cod_centro_costo_solicitante, "+
		"coalesce(adf.especialidad,"+ConstantesBD.codigoNuncaValido+")  as especialidad_solicitada, "+
		"cc2.unidad_funcional as uni_func_solicitado, "+
		"cc2.institucion as cod_inst_solicitado, "+ 
		"adf.tipo_asocio as tipo_asocio, "+ 
		"ta.tipos_servicio as tipo_serv_asocio, "+
		"coalesce(adf.pool,"+ConstantesBD.codigoNuncaValido+") as pool, "+ 
		"getnombrepersona(f.cod_paciente)  as observacion, " +
		"getnumeroidentificaciontercero(p.tercero_responsable) as id_tercero "+ 
		"FROM facturas f "+ 
		"INNER JOIN det_factura_solicitud dfs on (dfs.factura = f.codigo) "+ 
		"INNER JOIN solicitudes_cirugia sc ON(sc.numero_solicitud = dfs.solicitud) "+
		"INNER JOIN asocios_det_factura adf ON(adf.codigo = dfs.codigo) "+
		"INNER JOIN pooles p ON(p.codigo = adf.pool) "+
		"INNER JOIN solicitudes s ON(s.numero_solicitud = dfs.solicitud) "+ 
		"inner join centros_costo cc1 ON(cc1.codigo = s.centro_costo_solicitante) "+
		"inner join centros_costo cc2 ON(cc2.codigo = s.centro_costo_solicitado) "+  
		"left outer join tipos_asocio ta ON(ta.codigo = adf.tipo_asocio) "+ 
		"WHERE "+ 
		" f.consecutivo_factura = ? and " +
		" dfs.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" and " +
		//Se buscan todos los asocios de ese servicio que fueron liquidados con valor de honorarios
		//" ta.tipos_servicio = '"+ConstantesBD.codigoServicioHonorariosCirugia+"' AND " +
		" adf.valor_pool > 0 "+ 
		"UNION ALL "+ 
		//cirugias con excentos
		"SELECT "+ 
		"adf.servicio_asocio as servicio, "+
		"dfs.tipo_solicitud as tipo_solicitud, "+
		"adf.valor_pool as valor, "+
		"cc1.codigo as centro_costo_solicitante, "+
		"cc1.unidad_funcional as uni_func_solicitante, "+
		"cc1.institucion as cod_inst_solicitante, "+
		"cc1.codigo_interfaz as cod_centro_costo_solicitante, "+
		"coalesce(adf.especialidad,"+ConstantesBD.codigoNuncaValido+")  as especialidad_solicitada, "+
		"cc2.unidad_funcional as uni_func_solicitado, "+
		"cc2.institucion as cod_inst_solicitado, "+ 
		"adf.tipo_asocio as tipo_asocio, "+ 
		"ta.tipos_servicio as tipo_serv_asocio, "+
		"coalesce(adf.pool,"+ConstantesBD.codigoNuncaValido+") as pool, "+ 
		"getnombrepersona(f.cod_paciente)  as observacion, " +
		"getnumeroidentificaciontercero(p.tercero_responsable) as id_tercero "+ 
		"FROM facturas f "+ 
		"INNER JOIN facturacion.det_factura_solicitud_excenta dfs on (dfs.factura = f.codigo) "+ 
		"INNER JOIN solicitudes_cirugia sc ON(sc.numero_solicitud = dfs.solicitud) "+
		"INNER JOIN facturacion.asocios_det_factura_excenta adf ON(adf.codigo = dfs.codigo) "+
		"INNER JOIN pooles p ON(p.codigo = adf.pool) "+
		"INNER JOIN solicitudes s ON(s.numero_solicitud = dfs.solicitud) "+ 
		"inner join centros_costo cc1 ON(cc1.codigo = s.centro_costo_solicitante) "+
		"inner join centros_costo cc2 ON(cc2.codigo = s.centro_costo_solicitado) "+  
		"left outer join tipos_asocio ta ON(ta.codigo = adf.tipo_asocio) "+ 
		"WHERE "+ 
		" f.consecutivo_factura = ? and " +
		" dfs.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" and " +
		//Se buscan todos los asocios de ese servicio que fueron liquidados con valor de honorarios
		//" ta.tipos_servicio = '"+ConstantesBD.codigoServicioHonorariosCirugia+"' AND " +
		" adf.valor_pool > 0 "+ 
		"UNION ALL "+ 
		"SELECT "+ 
		"pdf.servicio as servicio, "+
		"pdf.tipo_solicitud as tipo_solicitud, "+
		"coalesce(pdf.valor_pool,0) as valor, "+
		"cc1.codigo as centro_costo_solicitante, "+
		"cc1.unidad_funcional as uni_func_solicitante, "+
		"cc1.institucion as cod_inst_solicitante, "+
		"cc1.codigo_interfaz as cod_centro_costo_solicitante, "+
		"case when pdf.tipo_asocio is not null then "+ 
			"coalesce(pdf.especialidad_asocio,"+ConstantesBD.codigoNuncaValido+") "+ 
		"else "+
			"coalesce(s.especialidad_solicitada,"+ConstantesBD.codigoNuncaValido+")"+
		"end as especialidad_solicitada, "+
		"cc2.unidad_funcional as uni_func_solicitado, "+
		"cc2.institucion as cod_inst_solicitado, "+ 
		"pdf.tipo_asocio as tipo_asocio, "+ 
		"ta.tipos_servicio as tipo_serv_asocio, "+
		"coalesce(pdf.pool,"+ConstantesBD.codigoNuncaValido+") as pool, "+ 
		"getnombrepersona(f.cod_paciente)  as observacion, " +
		"getnumeroidentificaciontercero(p.tercero_responsable) as id_tercero "+
		"FROM facturas f "+ 
		"INNER JOIN det_factura_solicitud dfs on (dfs.factura = f.codigo) "+ 
		"INNER JOIN paquetizacion_det_factura pdf ON(pdf.codigo_det_fact = dfs.codigo) "+
		"INNER JOIN pooles p ON(p.codigo = pdf.pool) "+
		"INNER JOIN solicitudes s ON(s.numero_solicitud = pdf.solicitud) "+ 
		"inner join centros_costo cc1 ON(cc1.codigo = s.centro_costo_solicitante) "+
		"inner join centros_costo cc2 ON(cc2.codigo = s.centro_costo_solicitado) "+  
		"left outer join tipos_asocio ta ON(ta.codigo = pdf.tipo_asocio) "+ 
		"WHERE "+ 
		"f.consecutivo_factura = ? and dfs.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudPaquetes+" and pdf.tipo_solicitud not in ("+ConstantesBD.codigoTipoSolicitudMedicamentos+","+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+") " +
		"AND pdf.valor_pool > 0 ";
	/**
	 * Cadena para consultar el detalle de las facturas varias
	 */
	private static final String consultarDetalleFacturasVariasStr = "SELECT "+ 
		"coalesce(cfv.cuenta_contable_credito,"+ConstantesBD.codigoNuncaValido+") as cuenta_contable, "+
		"coalesce(cc.codigo_interfaz,'')as codigo_centro_costo, "+
		"coalesce(cc.unidad_funcional,'') as uni_func_solicitado, "+
		"fv.valor_factura as valor, "+ 
		"cfv.descripcion as observaciones , cc.nombre as nombre_centro_costo "+ 
		"FROM facturas_varias fv "+ 
		"INNER JOIN conceptos_facturas_varias cfv ON(cfv.consecutivo = fv.concepto) "+ 
		"LEFT OUTER JOIN centros_costo cc on(cc.codigo = fv.centro_costo) "+ 
		"WHERE "+ 
		"fv.consecutivo = ?";
	
	/**
	 * Cadena para consultar el detalle de los ajsutes de facturas varias
	 */
	private static final String consultarDetalleAjustesFacturasVariasStr = "SELECT "+ 
		"coalesce(cfv.cuenta_contable_credito,"+ConstantesBD.codigoNuncaValido+") as cuenta_contable, "+
		"coalesce(cc.codigo_interfaz,'')as codigo_centro_costo, "+ 
		"afv.valor_ajuste as valor, "+ 
		"afv.tipo_ajuste as tipo_ajuste, "+
		"cfv.descripcion as observaciones, cc.nombre as nombre_centro_costo "+ 
		"FROM ajus_facturas_varias afv "+ 
		"inner join facturas_varias fv ON(fv.codigo_fac_var = afv.factura) "+ 
		"INNER JOIN conceptos_facturas_varias cfv ON(cfv.consecutivo = fv.concepto) "+ 
		"LEFT OUTER JOIN centros_costo cc on(cc.codigo = fv.centro_costo) "+ 
		"WHERE "+ 
		"afv.consecutivo = ?";
	
	/**
	 * Cadena para consultar los tipos de ajuste facturas varias
	 */
	private static final String consultarTipoAjusteFacturasVariasStr = "SELECT afv.tipo_ajuste as tipo_ajuste from ajus_facturas_varias afv WHERE afv.consecutivo = ? ";
	
	/**
	 * Cadena para consultar el detalle de la cuenta de cobro
	 */
	private static final String consultarDetalleCuentasCobroCapitacionStr = "SELECT "+ 
		"cc.valor_inicial_cuenta as valor, "+
		"c.codigo as codigo_convenio, "+ 
		"c.tipo_regimen as codigo_tipo_regimen, "+
		"c.tipo_convenio as codigo_tipo_convenio, "+
		"c.institucion as codigo_institucion, "+
		"c.nombre as observaciones, "+
		"coalesce(cc1.codigo_interfaz,'') as codigo_centro_costo, " +
		"ca.codigo as centro_atencion_contable , cc1.nombre as nombre_centro_costo "+ 
		"FROM cuentas_cobro_capitacion cc "+ 
		"INNER JOIN convenios c ON(c.codigo = cc.convenio) "+ 
		"LEFT OUTER JOIN centros_costo cc1 ON(cc1.codigo = c.centro_costo_contable) " +
		"LEFT OUTER JOIN cen_aten_contable_conv cacc ON (cacc.convenio=c.codigo) " +
		"LEFT OUTER JOIN centro_atencion ca ON (ca.consecutivo=cacc.centro_atencion) "+ 
		"WHERE "+ 
		"cc.numero_cuenta_cobro = ?";
	
	/**
	 * Cadena para consultar el detalle del ajuste dela cuenta de cobro
	 */
	private static final String consultarDetalleAjustesCuentaCobroCapitacionStr = "SELECT "+ 
		"ac.valor as valor, "+
		"ac.tipo_ajuste AS tipo_ajuste, "+
		"c.codigo as codigo_convenio, "+ 
		"c.tipo_regimen as codigo_tipo_regimen, "+
		"c.tipo_convenio as codigo_tipo_convenio, "+
		"c.institucion as codigo_institucion, "+
		"c.nombre as observaciones, "+
		"coalesce(cc1.codigo_interfaz,'') as codigo_centro_costo, cc1.nombre as nombre_centro_costo "+ 
		"FROM ajustes_cxc ac "+ 
		"INNER JOIN cuentas_cobro_capitacion cc ON(cc.numero_cuenta_cobro = ac.cuenta_cobro and cc.institucion = ac.institucion) "+ 
		"INNER JOIN convenios c ON(c.codigo = cc.convenio) "+ 
		"LEFT OUTER JOIN centros_costo cc1 ON(cc1.codigo = c.centro_costo_contable) "+ 
		"WHERE "+ 
		"ac.consecutivo = ?";
	
	/**
	 * Cadena para consutar el tipo de ajuste de la cuenta de cobro de capitacion
	 */
	private static final String consultarTipoAjusteCuentaCobroCapitacionStr = "SELECT ac.tipo_ajuste as tipo_ajuste FROM ajustes_cxc ac WHERE ac.consecutivo = ?";
	
	/**
	 * Cadena para consultar el detalle de las facturas del paciente para cuentas x cobrar
	 */
	private static final String consultarDetalleFacturasPaciente02Str = "SELECT "+ 
		"c.codigo as codigo_convenio, "+
		"c.tipo_regimen as codigo_tipo_regimen, "+
		"c.tipo_convenio as codigo_tipo_convenio, "+
		"c.institucion as codigo_institucion, " +
		"c.tipo_contrato as codigo_tipo_contrato, " +
		"c.nombre as nombre_convenio, "+ 
		"coalesce(c.num_dias_vencimiento,0) as dias, "+ 
		"coalesce(f.valor_convenio,0) as valor_cobrar_convenio, "+
		"coalesce(f.valor_favor_convenio,0) as valor_favor_convenio, "+
		"coalesce(f.valor_neto_paciente,0) as valor_paciente, "+ 
		"coalesce(f.valor_abonos,0) as valor_abonos, "+ 
		"coalesce(f.val_desc_pac,0) as valor_descuento, "+ 
		"to_char(f.fecha,'"+ConstantesBD.formatoFechaAp+"') as fecha, "+  
		"getnombrepersona(f.cod_paciente) as observacion, "+ 
		"coalesce(cc.codigo_interfaz,'') as codigo_area, "+ 
		"getnumeroidentificaciontercero(e.tercero) as id_tercero," +
		"getidentificacionpaciente(f.cod_paciente) AS id_paciente, " +
		"coalesce(ca.codigo,'') as codigo_centro_at_cont, " +
		"coalesce(facturacion.getcodintcentrocosto(c.centro_costo_contable),'') as cod_int_cc_contable," +
		"c.centro_costo_contable as centro_costo_contable, "+
		"coalesce(ing.consecutivo, ' ') AS consecutivo_ingreso," +
		"COALESCE(ma.tipo, "+ConstantesBD.codigoNuncaValido+") AS codigo_tipo_ingreso "+ 
		"FROM facturas f "+ 
		"INNER JOIN convenios c ON(c.codigo = f.convenio) "+
		"INNER JOIN cuentas cue on (cue.id = f.cuenta) " +
		"INNER JOIN centros_costo cc ON(cc.codigo = cue.area) "+
		"INNER JOIN empresas e on(e.codigo = c.empresa) " +
		"LEFT OUTER JOIN facturacion.cen_aten_contable_conv cacc ON(cacc.convenio = c.codigo) " +
		"LEFT OUTER JOIN administracion.centro_atencion ca ON(ca.consecutivo = cacc.centro_atencion) "+
		//***********INICIO ABONO APLICADO PACIENTE**************** 
		" LEFT OUTER JOIN tesoreria.movimientos_abonos ma on (f.institucion=ma.institucion AND ma.codigo_documento=f.codigo AND ma.tipo IN ("+ConstantesBD.tipoMovimientoAbonoFacturacion+")  AND ingreso is not null) " +
		" LEFT OUTER JOIN manejopaciente.ingresos ing on(ing.id=ma.ingreso) " +
		//*********	FIN ABONO APLICADO PACIENTE ***************	 
		"WHERE "+ 
		"f.consecutivo_factura = ? "; //and f.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionFacturada;
	
	
	/**
	 * Cadena para consultar el detalle de las facturas del paciente para cuentas x cobrar
	 */
	private static final String consultarDetalleFacturasPacienteAnuladas02Str = "SELECT "+ 
		"c.codigo as codigo_convenio, "+
		"c.tipo_regimen as codigo_tipo_regimen, "+
		"c.tipo_convenio as codigo_tipo_convenio, "+
		"c.institucion as codigo_institucion, " +
		"c.tipo_contrato as codigo_tipo_contrato, " +
		"c.nombre as nombre_convenio, "+ 
		"coalesce(c.num_dias_vencimiento,0) as dias, "+ 
		"coalesce(f.valor_convenio,0) as valor_cobrar_convenio, "+
		"coalesce(f.valor_favor_convenio,0) as valor_favor_convenio, "+
		"coalesce(f.valor_neto_paciente,0) as valor_paciente, "+ 
		"coalesce(f.valor_abonos,0) as valor_abonos, "+ 
		"coalesce(f.val_desc_pac,0) as valor_descuento, "+ 
		"to_char(f.fecha,'"+ConstantesBD.formatoFechaAp+"') as fecha, "+  
		"getnombrepersona(f.cod_paciente) as observacion, "+ 
		"coalesce(cc.codigo_interfaz,'') as codigo_area, "+ 
		"getnumeroidentificaciontercero(e.tercero) as id_tercero," +
		"getidentificacionpaciente(f.cod_paciente) AS id_paciente, " +
		"coalesce(ca.codigo,'') as codigo_centro_at_cont, " +
		"coalesce(facturacion.getcodintcentrocosto(c.centro_costo_contable),'') as cod_int_cc_contable," +
		"c.centro_costo_contable as centro_costo_contable, "+
		"coalesce(ing.consecutivo, ' ') AS consecutivo_ingreso," +
		"COALESCE(ma.tipo, "+ConstantesBD.codigoNuncaValido+") AS codigo_tipo_ingreso," +
		"to_char(anulfac.fecha_grabacion,'"+ConstantesBD.formatoFechaAp+"') as fechaanulacion "+ 
		" "+ 
		"FROM facturas f "+ 
		"INNER JOIN convenios c ON(c.codigo = f.convenio) "+
		"INNER JOIN cuentas cue on (cue.id = f.cuenta) " +
		"INNER JOIN centros_costo cc ON(cc.codigo = cue.area) "+
		"INNER JOIN empresas e on(e.codigo = c.empresa) " +
		"inner join anulaciones_facturas anulfac on (f.codigo=anulfac.codigo)" +
		"LEFT OUTER JOIN facturacion.cen_aten_contable_conv cacc ON(cacc.convenio = c.codigo) " +
		"LEFT OUTER JOIN administracion.centro_atencion ca ON(ca.consecutivo = cacc.centro_atencion) "+
		//***********INICIO ABONO APLICADO PACIENTE**************** 
		" LEFT OUTER JOIN tesoreria.movimientos_abonos ma on (f.institucion=ma.institucion AND ma.codigo_documento=f.codigo AND ma.tipo IN ("+ConstantesBD.tipoMovimientoAbonoAnulacionFactura+")  AND ingreso is not null) " +
		" LEFT OUTER JOIN manejopaciente.ingresos ing on(ing.id=ma.ingreso) " +
		//*********	FIN ABONO APLICADO PACIENTE ***************	 
		"WHERE "+ 
		"f.consecutivo_factura = ? and f.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionAnulada;
	
	
	/**
	 * Cadena para consultar el detalle de las facturas varias para cuentas x cobrar
	 */
	private static final String consultarDetalleFacturasVarias02Str = "SELECT "+ 
		"coalesce(cfv.cuenta_contable_debito,"+ConstantesBD.codigoNuncaValido+") as cuenta_contable, "+ 
		"cfv.descripcion as observacion, "+
		"fv.valor_factura as valor, "+
		"to_char(fv.fecha,'"+ConstantesBD.formatoFechaAp+"') as fecha, "+
		"to_char(fv.fecha_anulacion,'"+ConstantesBD.formatoFechaAp+"') as fechaanulacion, "+
		"coalesce(d.dias_vencimiento_fac,0) as dias "+ 
		"FROM facturas_varias fv "+ 
		"INNER JOIN conceptos_facturas_varias cfv ON(cfv.consecutivo = fv.concepto) "+ 
		"INNER JOIN deudores d ON(d.codigo = fv.deudor) "+ 
		"WHERE "+ 
		"fv.consecutivo = ?";
	
	/**
	 * Cadena para consultar el detalle del ajuste de facturas varias para cuentas x cobrar
	 */
	private static final String consultarDetalleAjustesFacturasVarias02Str = "SELECT "+ 
		"coalesce(cfv.cuenta_contable_debito,"+ConstantesBD.codigoNuncaValido+") as cuenta_contable, "+ 
		"cfv.descripcion as observacion, "+
		"fv.valor_factura as valor, "+
		"afv.tipo_ajuste as tipo_ajuste, "+ 
		"to_char(fv.fecha,'"+ConstantesBD.formatoFechaAp+"') as fecha, "+
		"coalesce(d.dias_vencimiento_fac,0) as dias "+ 
		"FROM ajus_facturas_varias afv "+ 
		"INNER JOIN facturas_varias fv ON(fv.codigo_fac_var = afv.factura) "+ 
		"INNER JOIN conceptos_facturas_varias cfv ON(cfv.consecutivo = fv.concepto) "+ 
		"INNER JOIN deudores d ON(d.codigo = fv.deudor) "+ 
		"WHERE "+ 
		"afv.consecutivo = ?";
	
	/**
	 * Cadena para consultar el detalle de la cuenta de cobro para cuentas x cobrar
	 */
	private static final String consultarDetalleCuentasCobroCapitacion02Str = "SELECT "+ 
		"cc.valor_inicial_cuenta as valor, "+
		"c.codigo as codigo_convenio, "+ 
		"c.tipo_regimen as codigo_tipo_regimen, "+
		"c.tipo_convenio as codigo_tipo_convenio, "+
		"c.institucion as codigo_institucion, "+
		"c.nombre as observacion, "+
		"coalesce(c.num_dias_vencimiento,0) as dias, "+ 
		"coalesce(to_char(cc.fecha_radicacion,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha "+ 
		"FROM cuentas_cobro_capitacion cc "+ 
		"INNER JOIN convenios c ON(c.codigo = cc.convenio) "+ 
		"WHERE "+ 
		"cc.numero_cuenta_cobro = ?";
	
	/**
	 * Cadena para consultar el detalle del ajuste de la cuenta de cobro para cuenta x cobrar
	 */
	private static final String consultarDetalleAjustesCuentaCobroCapitacion02Str = "SELECT " + 
		"ac.valor as valor, "+
		"ac.tipo_ajuste AS tipo_ajuste, "+
		"c.codigo as codigo_convenio, "+ 
		"c.tipo_regimen as codigo_tipo_regimen, "+
		"c.tipo_convenio as codigo_tipo_convenio, "+
		"c.institucion as codigo_institucion, "+
		"c.nombre as observacion, "+
		"coalesce(c.num_dias_vencimiento,0) as dias, "+ 
		"coalesce(to_char(cc.fecha_radicacion,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha "+ 
		"FROM ajustes_cxc ac "+ 
		"INNER JOIN cuentas_cobro_capitacion cc ON(cc.numero_cuenta_cobro = ac.cuenta_cobro and cc.institucion = ac.institucion) "+ 
		"INNER JOIN convenios c ON(c.codigo = cc.convenio) "+ 
		"WHERE "+ 
		"ac.consecutivo = ?";
	//*********************CONSULTAS FLUJO VENTAS Y HONORARIOS PARA CUENTAS POR PAGAR*************************************************
	/**
	 * Cadena para consultar el detalle de las facturas del paciente para cuentas x pagar
	 */
	
	private static final String consultarDetalleFacturasPaciente03Str = "SELECT "+ 
		"f.convenio as codigo_convenio, "+
		"to_char(f.fecha,'"+ConstantesBD.formatoFechaAp+"') as fecha, "+ 
		"dfs.pool as codigo_pool, "+ 
		"dfs.valor_pool as valor, "+
		"getconsecutivosolicitud(dfs.solicitud) as consecutivo_solicitud, "+
		"coalesce(dfs.esquema_tarifario,"+ConstantesBD.codigoNuncaValido+") as codigo_esquema_tarifario, "+
		"coalesce(p.cuenta_x_pagar,"+ConstantesBD.codigoNuncaValido+") as cuenta_contable, "+ 
		"coalesce(getnumeroidentificaciontercero(p.tercero_responsable),'') as id_tercero, "+
		"to_char(anulfac.fecha_grabacion,'"+ConstantesBD.formatoFechaAp+"') as fechaanulacion, "+
		"coalesce(p.dias_vencimiento_fact,0) as dias," +
		"CASE WHEN f.cod_paciente IS NOT NULL THEN getnombrepersona(f.cod_paciente) ELSE ''END  as observacion "+ 
		"FROM facturas f "+     
		"INNER JOIN det_factura_solicitud dfs on(dfs.factura = f.codigo) "+  
		"INNER JOIN pooles p ON(p.codigo = dfs.pool) "+
		"left outer join anulaciones_facturas anulfac on (f.codigo=anulfac.codigo)" +
		"WHERE f.consecutivo_factura = ? and dfs.tipo_solicitud not in ("+ConstantesBD.codigoTipoSolicitudCirugia+","+ConstantesBD.codigoTipoSolicitudPaquetes+") and dfs.valor_pool > 0 "+ 
		"UNION ALL "+
		//mirar los excentos
		"SELECT "+ 
		"f.convenio as codigo_convenio, "+
		"to_char(f.fecha,'"+ConstantesBD.formatoFechaAp+"') as fecha, "+ 
		"dfs.pool as codigo_pool, "+ 
		"dfs.valor_pool as valor, "+
		"getconsecutivosolicitud(dfs.solicitud) as consecutivo_solicitud, "+
		"coalesce(dfs.esquema_tarifario,"+ConstantesBD.codigoNuncaValido+") as codigo_esquema_tarifario, "+
		"coalesce(p.cuenta_x_pagar,"+ConstantesBD.codigoNuncaValido+") as cuenta_contable, "+ 
		"coalesce(getnumeroidentificaciontercero(p.tercero_responsable),'') as id_tercero, "+
		"to_char(anulfac.fecha_grabacion,'"+ConstantesBD.formatoFechaAp+"') as fechaanulacion, "+
		"coalesce(p.dias_vencimiento_fact,0) as dias," +
		"CASE WHEN f.cod_paciente IS NOT NULL THEN getnombrepersona(f.cod_paciente) ELSE ''END  as observacion "+ 
		"FROM facturas f "+     
		"INNER JOIN facturacion.det_factura_solicitud_excenta dfs on(dfs.factura = f.codigo) "+  
		"INNER JOIN pooles p ON(p.codigo = dfs.pool) "+
		"left outer join anulaciones_facturas anulfac on (f.codigo=anulfac.codigo)" +
		"WHERE f.consecutivo_factura = ? and dfs.tipo_solicitud not in ("+ConstantesBD.codigoTipoSolicitudCirugia+","+ConstantesBD.codigoTipoSolicitudPaquetes+") and dfs.valor_pool > 0 "+ 
		"UNION ALL "+
		//cirugias
		"SELECT "+ 
		"f.convenio as codigo_convenio, "+
		"to_char(f.fecha,'"+ConstantesBD.formatoFechaAp+"') as fecha, "+ 
		"adf.pool as codigo_pool, "+ 
		"adf.valor_pool as valor, "+
		"getconsecutivosolicitud(dfs.solicitud) as consecutivo_solicitud, "+
		"coalesce(dfs.esquema_tarifario,"+ConstantesBD.codigoNuncaValido+") as codigo_esquema_tarifario, "+
		"coalesce(p.cuenta_x_pagar,"+ConstantesBD.codigoNuncaValido+") as cuenta_contable, "+ 
		"coalesce(getnumeroidentificaciontercero(p.tercero_responsable),'') as id_tercero, "+ 
		"to_char(anulfac.fecha_grabacion,'"+ConstantesBD.formatoFechaAp+"') as fechaanulacion, "+ 
		"coalesce(p.dias_vencimiento_fact,0) as dias, "+
		"CASE WHEN f.cod_paciente IS NOT NULL THEN getnombrepersona(f.cod_paciente) ELSE '' END as observacion "+
		"FROM facturas f "+ 
		"INNER JOIN det_factura_solicitud dfs on(dfs.factura = f.codigo) "+  
		"INNER JOIN asocios_det_factura adf ON(adf.codigo = dfs.codigo) "+  
		"INNER JOIN pooles p ON(p.codigo = adf.pool) "+
		"left outer join anulaciones_facturas anulfac on (f.codigo=anulfac.codigo)" +
		"WHERE f.consecutivo_factura = ? and dfs.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" and adf.valor_pool > 0 "+ 
		"UNION ALL "+ 
		//cirugias excentas
		"SELECT "+ 
		"f.convenio as codigo_convenio, "+
		"to_char(f.fecha,'"+ConstantesBD.formatoFechaAp+"') as fecha, "+ 
		"adf.pool as codigo_pool, "+ 
		"adf.valor_pool as valor, "+
		"getconsecutivosolicitud(dfs.solicitud) as consecutivo_solicitud, "+
		"coalesce(dfs.esquema_tarifario,"+ConstantesBD.codigoNuncaValido+") as codigo_esquema_tarifario, "+
		"coalesce(p.cuenta_x_pagar,"+ConstantesBD.codigoNuncaValido+") as cuenta_contable, "+ 
		"coalesce(getnumeroidentificaciontercero(p.tercero_responsable),'') as id_tercero, "+ 
		"to_char(anulfac.fecha_grabacion,'"+ConstantesBD.formatoFechaAp+"') as fechaanulacion, "+ 
		"coalesce(p.dias_vencimiento_fact,0) as dias, "+
		"CASE WHEN f.cod_paciente IS NOT NULL THEN getnombrepersona(f.cod_paciente) ELSE '' END as observacion "+
		"FROM facturas f "+ 
		"INNER JOIN facturacion.det_factura_solicitud_excenta dfs on(dfs.factura = f.codigo) "+  
		"INNER JOIN facturacion.asocios_det_factura_excenta adf ON(adf.codigo = dfs.codigo) "+  
		"INNER JOIN pooles p ON(p.codigo = adf.pool) "+
		"left outer join anulaciones_facturas anulfac on (f.codigo=anulfac.codigo)" +
		"WHERE f.consecutivo_factura = ? and dfs.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" and adf.valor_pool > 0 "+ 
		"UNION ALL "+ 
		"SELECT "+ 
		"f.convenio as codigo_convenio, "+
		"to_char(f.fecha,'"+ConstantesBD.formatoFechaAp+"') as fecha, "+ 
		"pdf.pool as codigo_pool, "+ 
		"pdf.valor_pool as valor, "+
		"getconsecutivosolicitud(dfs.solicitud) as consecutivo_solicitud, "+
		"coalesce(dfs.esquema_tarifario,"+ConstantesBD.codigoNuncaValido+") as codigo_esquema_tarifario, "+
		"coalesce(p.cuenta_x_pagar,"+ConstantesBD.codigoNuncaValido+") as cuenta_contable, "+ 
		"coalesce(getnumeroidentificaciontercero(p.tercero_responsable),'') as id_tercero, "+ 
		"to_char(anulfac.fecha_grabacion,'"+ConstantesBD.formatoFechaAp+"') as fechaanulacion, "+ 
		"coalesce(p.dias_vencimiento_fact,0) as dias, "+
		"CASE WHEN f.cod_paciente IS NOT NULL THEN getnombrepersona(f.cod_paciente) ELSE '' END as observacion "+
		"FROM facturas f "+ 
		"INNER JOIN det_factura_solicitud dfs on(dfs.factura = f.codigo) "+  
		"INNER JOIN paquetizacion_det_factura pdf ON(pdf.codigo_det_fact = dfs.codigo) "+  
		"INNER JOIN pooles p ON(p.codigo = pdf.pool) "+
		"left outer join anulaciones_facturas anulfac on (f.codigo=anulfac.codigo)" +
		"WHERE f.consecutivo_factura = ? and dfs.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudPaquetes+" and pdf.valor_pool > 0 ";
	
	//********************CONSULTAS FLUJO AJUSTES Y RECLASIFIACIONES***********************************************************************
	
	/**
	 * Cadena que consulta los documentos de los ajustes
	 */
	private static final String consultarDocumentosContablesAjustesStr = 
		"SELECT "+ 
		ConstantesBD.codigoTipoDocInteAjustesFactPaciente+" as tipo_documento01, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteAjustesFactPaciente+") as descripcion_documento01, "+
		"ae.consecutivo_ajuste as numero_documento01, "+
		ConstantesBD.tipoConsecutivoInteNumeroAjuste+" as tipo_consecutivo01, "+ 
		"to_char(ae.fecha_ajuste,'"+ConstantesBD.formatoFechaAp+"') as fecha01, "+
		ConstantesBD.codigoTipoDocInteFacturaPaciente+" as tipo_documento02, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteFacturaPaciente+") as descripcion_documento02, "+
		"f.consecutivo_factura||'' as numero_documento02, "+
		ConstantesBD.tipoConsecutivoInteNumeroFactura+" as tipo_consecutivo02, "+
		"to_char(f.fecha,'"+ConstantesBD.formatoFechaAp+"') as fecha02, "+
		"coalesce(ca.codigo||'','') as codigo_centro_atencion, " +
		"ae.codigo||'' as numero_documento03, " +		
		"afe.valor_ajuste as valor, "+
		"'' as id_tercero "+
		"FROM ajustes_empresa ae "+ 
		"INNER JOIN ajus_fact_empresa afe ON(afe.codigo = ae.codigo) "+ 
		"INNER JOIN facturas f ON(f.codigo = afe.factura) "+
		"LEFT OUTER JOIN centro_atencion ca on(ca.consecutivo = f.centro_aten) "+
		"WHERE "+ 
		"to_char(ae.fecha_ajuste,'YYYY-MM-DD') = ? " +
		"and ae.estado = "+ConstantesBD.codigoEstadoCarteraAprobado+" " +
		"and (ae.contabilizado IS NULL OR ae.contabilizado = '"+ConstantesBD.acronimoNo+"') " +
		" " +
		"UNION " +
		" "+
		"SELECT "+
		ConstantesBD.codigoTipoDocInteRegistroGlosas+" as tipo_documento01, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteRegistroGlosas+") as descripcion_documento01, "+
		"coalesce(rg.glosa_sistema,'') as numero_documento01, "+
		ConstantesBD.tipoConsecutivoInteNumeroGlosaInterno+" as tipo_consecutivo01, "+ 
		"to_char(rg.fecha_aprobacion,'"+ConstantesBD.formatoFechaAp+"') as fecha01, "+
		
		ConstantesBD.codigoTipoDocInteRegistroGlosas+" as tipo_documento02, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteRegistroGlosas+") as descripcion_documento02, "+
		"coalesce(rg.glosa_entidad,'') as numero_documento02, "+
		ConstantesBD.tipoConsecutivoInteNumeroGlosaEntidad+" as tipo_consecutivo02, "+ 
		"to_char(rg.fecha_aprobacion,'"+ConstantesBD.formatoFechaAp+"') as fecha02, "+
		
		
		"(SELECT coalesce(ca.codigo,'') FROM facturacion.facturas f LEFT OUTER JOIN administracion.centro_atencion ca on(ca.consecutivo = f.centro_aten)  WHERE f.codigo = (SELECT MAX(ag.codigo_factura) FROM glosas.auditorias_glosas ag WHERE ag.glosa = rg.codigo) ) AS codigo_centro_atencion, " +
		"rg.codigo||'' AS numero_documento03, "+
		"coalesce(rg.valor_glosa,0) as valor," +
		"getnumeroidentificaciontercero(e.tercero) as id_tercero "+
		"FROM glosas.registro_glosas rg " +
		"INNER JOIN convenios c ON(c.codigo = rg.convenio) "+
		"INNER JOIN empresas e on(e.codigo = c.empresa) "+
		"WHERE " +
		"rg.estado IN ('"+ConstantesIntegridadDominio.acronimoEstadoGlosaAprobada+"','"+ConstantesIntegridadDominio.acronimoEstadoGlosaRespondida+"','"+ConstantesIntegridadDominio.acronimoEstadoGlosaConciliada+"') " +
		"AND to_char(rg.fecha_aprobacion,'YYYY-MM-DD') = ? AND (rg.contabilizado IS NULL OR rg.contabilizado = '"+ConstantesBD.acronimoNo+"') ";
		
	/**
	 * Cadena que consulta el detalle de la información del ajuste de la factura
	 */
	private static final String consultarDetalleAjustesFacturas01Str = "SELECT "+ 
		"ae.tipo_ajuste as tipo_ajuste, "+ 
		"afe.valor_ajuste as valor, "+
		"to_char(f.fecha,'"+ConstantesBD.formatoFechaAp+"') as fecha, "+  
		"getnombrepersona(f.cod_paciente) as observacion, "+ 
		"c.codigo as codigo_convenio, "+
		"c.tipo_regimen as codigo_tipo_regimen, "+
		"c.tipo_convenio as codigo_tipo_convenio, "+
		"c.institucion as codigo_institucion, "+ 
		"c.tipo_contrato as codigo_tipo_contrato," +
		"coalesce(c.num_dias_vencimiento,0) as dias "+ 
		"FROM ajustes_empresa ae "+ 
		"INNER JOIN ajus_fact_empresa afe ON(afe.codigo = ae.codigo) "+ 
		"INNER JOIN facturas f ON(f.codigo = afe.factura) "+ 
		"INNER JOIN convenios c on (c.codigo = f.convenio) "+ 
		"WHERE " +
		"ae.codigo = ? AND " +
		"f.consecutivo_factura = ?  AND " +
		"(" +
			"(ae.tipo_ajuste not in ("+ConstantesBD.codigoAjusteDebitoCuentaCobro+","+ConstantesBD.codigoAjusteCreditoCuentaCobro+") and c.tipo_contrato <> "+ConstantesBD.codigoTipoContratoCapitado+")" +
			" OR " +
			"ae.tipo_ajuste in ("+ConstantesBD.codigoAjusteDebitoCuentaCobro+","+ConstantesBD.codigoAjusteCreditoCuentaCobro+")" +
		") ";
	
	/**
	 * Cadena que consulta el detalle de la información del ajuste de la factura
	 */
	private static final String consultarDetalleGlosasStr = "SELECT " +
		ConstantesBD.codigoNuncaValido+" AS tipo_ajuste, "+
		"ag.valor_glosa_factura AS valor," +
		"to_char(rg.fecha_aprobacion,'"+ConstantesBD.formatoFechaAp+"') as fecha, " +
		"getnombreconvenio(rg.convenio) AS observacion,"+
		"c.codigo as codigo_convenio, "+
		"c.tipo_regimen as codigo_tipo_regimen, "+
		"c.tipo_convenio as codigo_tipo_convenio, "+
		"c.institucion as codigo_institucion," +
		"c.tipo_contrato as codigo_tipo_contrato," +
		"f.consecutivo_factura AS consecutivo "+
		"FROM glosas.registro_glosas rg  " +
		"INNER JOIN glosas.auditorias_glosas ag ON (ag.glosa = rg.codigo) " +
		"INNER JOIN facturacion.convenios c ON (c.codigo = rg.convenio) " +
		"INNER JOIN facturacion.facturas f ON (f.codigo = ag.codigo_factura) " +
		"WHERE rg.codigo = ?  AND f.consecutivo_factura = ? ";
	
	/**
	 * Cadena que consulta el detalle de la información del ajuste de la factura
	 */
	private static final String consultarDetalleAjustesFacturas02Str = "SELECT  "+
		"ae.tipo_ajuste as tipo_ajuste, "+ 
		"f.convenio as codigo_convenio, "+ 
		"to_char(f.fecha,'"+ConstantesBD.formatoFechaAp+"') as fecha, "+
		"getnombrepersona(f.cod_paciente) as observacion, "+
		"adf.pool as codigo_pool, "+  
		"adf.valor_ajuste_pool as valor, "+ 
		"getconsecutivosolicitud(dfs.solicitud) as consecutivo_solicitud, "+ 
		"coalesce(dfs.esquema_tarifario,"+ConstantesBD.codigoNuncaValido+") as codigo_esquema_tarifario, "+ 
		"coalesce(p.cuenta_x_pagar,"+ConstantesBD.codigoNuncaValido+") as cuenta_contable, "+  
		"coalesce(getnumeroidentificaciontercero(p.tercero_responsable),'') as id_tercero, "+  
		"coalesce(p.dias_vencimiento_fact,0) as dias "+  
		"FROM ajustes_empresa ae "+ 
		"INNER JOIN ajus_fact_empresa afe ON(afe.codigo = ae.codigo) "+ 
		"INNER JOIN facturas f ON(f.codigo = afe.factura) "+ 
		"INNER JOIN ajus_det_fact_empresa adf on(adf.factura = afe.factura and adf.codigo = afe.codigo) "+  
		"INNER JOIN det_factura_solicitud dfs ON(dfs.codigo = adf.det_fact_solicitud) "+  
		"INNER JOIN pooles p ON(p.codigo = adf.pool) "+ 
		"WHERE ae.codigo = ? and adf.valor_ajuste_pool > 0  and dfs.tipo_solicitud not in ("+ConstantesBD.codigoTipoSolicitudCirugia+","+ConstantesBD.codigoTipoSolicitudPaquetes+") "+  
		"UNION "+ 
		"SELECT "+  
		"ae.tipo_ajuste as tipo_ajuste, "+ 
		"f.convenio as codigo_convenio, "+ 
		"to_char(f.fecha,'"+ConstantesBD.formatoFechaAp+"') as fecha, " +
		"getnombrepersona(f.cod_paciente) as observacion, "+  
		"aaf.pool as codigo_pool, "+  
		"aaf.valor_ajuste_pool as valor, "+ 
		"getconsecutivosolicitud(dfs.solicitud) as consecutivo_solicitud, "+ 
		"coalesce(dfs.esquema_tarifario,"+ConstantesBD.codigoNuncaValido+") as codigo_esquema_tarifario, "+ 
		"coalesce(p.cuenta_x_pagar,"+ConstantesBD.codigoNuncaValido+") as cuenta_contable, "+  
		"coalesce(getnumeroidentificaciontercero(p.tercero_responsable),'') as id_tercero, "+  
		"coalesce(p.dias_vencimiento_fact,0) as dias "+  
		"FROM ajustes_empresa ae "+ 
		"INNER JOIN ajus_fact_empresa afe ON(afe.codigo = ae.codigo) "+ 
		"INNER JOIN facturas f ON(f.codigo = afe.factura) "+ 
		"INNER JOIN ajus_det_fact_empresa adfe on(adfe.factura = afe.factura and adfe.codigo = afe.codigo) "+  
		"INNER JOIN det_factura_solicitud dfs ON(dfs.codigo = adfe.det_fact_solicitud) "+ 
		"INNER JOIN ajus_asocios_fact_empresa aaf ON(aaf.codigo_ajuste = adfe.codigo and aaf.factura = adfe.factura and aaf.det_aso_fac_solicitud = adfe.det_fact_solicitud) "+
		"INNER JOIN asocios_det_factura adf ON(adf.codigo = aaf.consecutivo_aso_det_fac) "+   
		"INNER JOIN pooles p ON(p.codigo = aaf.pool) "+ 
		"WHERE ae.codigo = ? and aaf.valor_ajuste_pool > 0   and dfs.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" ";
	//*********************************************************************************************************************************
	//**********************CONSULTA FLUJOS CARTERA***************************************************************************************
	
	/**
	 * Cadena para consultar los eventos de facturas de pacientes
	 */
	private static final String consultarEventoFacturasPacientesStr = "SELECT " +
		""+ConstantesBD.eventoFacturasPacientes+" as evento,"+ //--
		"f.consecutivo_factura as numero_documento, "+
		ConstantesBD.codigoTipoDocInteFacturaPaciente+" as tipo_documento, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteFacturaPaciente+") as nombre_tipo_documento, "+
		"f.hora as hora, "+
		"to_char(f.fecha,'"+ConstantesBD.formatoFechaBD+"') as fecha, "+
		"'' as observaciones, "+
		"'' as fecha_radicacion, "+ 
		"c.codigo as codigo_convenio, "+ 
		"c.tipo_regimen as codigo_tipo_regimen, "+ 
		"c.tipo_convenio as codigo_tipo_convenio, "+
		"c.institucion as codigo_institucion, "+
		"coalesce(ca.codigo,'') as codigo_centro_atencion," +
		"FECHASEGUIMIENTO as fecha_seguimiento "+
		"FROM facturacion.facturas f "+ 
		"INNER JOIN facturacion.convenios c on(c.codigo = f.convenio) "+
		"LEFT OUTER JOIN administracion.centro_atencion ca ON(ca.consecutivo = f.centro_aten) "+
		"WHERE " +
		"to_char(f.fecha,'"+ConstantesBD.formatoFechaBD+"') = ? and " +
		"f.estado_facturacion in ("+ConstantesBD.codigoEstadoFacturacionFacturada+","+ConstantesBD.codigoEstadoFacturacionAnulada+") and " +
		"f.valor_convenio > 0 and " +
		"f.tipo_factura_sistema = "+ValoresPorDefecto.getValorTrueParaConsultas()+" and " +
		"c.tipo_regimen <> '"+ConstantesBD.codigoTipoRegimenParticular+"' and " +
		"c.tipo_contrato <> "+ConstantesBD.codigoTipoContratoCapitado;
	
	/**
	 * Cadena para consultar los eventos de cuentas cobro generadas
	 */
	private static final String consultarEventoCuentasCobroGeneradasStr = "SELECT "+ConstantesBD.eventoCuentasCobroCartera+" as evento,"+ 
		"f.consecutivo_factura as numero_documento, "+
		ConstantesBD.codigoTipoDocInteFacturaPaciente+" as tipo_documento, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteFacturaPaciente+") as nombre_tipo_documento, "+
		"cc.HORA_APROBACION as hora, "+
		"to_char(f.fecha,'"+ConstantesBD.formatoFechaBD+"') as fecha, "+
		"coalesce(cc.obs_generacion,'') as observaciones, "+
		"'' as fecha_radicacion, "+ 
		" c.codigo as codigo_convenio, "+ 
		"c.tipo_regimen as codigo_tipo_regimen, "+ 
		"c.tipo_convenio as codigo_tipo_convenio, "+
		"c.institucion as codigo_institucion, "+
		"coalesce(ca.codigo,'') as codigo_centro_atencion," +
		"FECHASEGUIMIENTO as fecha_seguimiento "+
		"FROM cartera.cuentas_cobro cc "+ 
		"INNER JOIN facturacion.facturas f ON(f.numero_cuenta_cobro = cc.numero_cuenta_cobro and f.institucion = cc.institucion) "+ 
		"INNER JOIN facturacion.convenios c on(c.codigo = f.convenio) "+
		"LEFT OUTER JOIN administracion.centro_atencion ca ON(ca.consecutivo = f.centro_aten) "+
		"WHERE "+ 
		"to_char(cc.fecha_aprobacion,'"+ConstantesBD.formatoFechaBD+"') = ? and cc.estado in ("+ConstantesBD.codigoEstadoCarteraAprobado+","+ConstantesBD.codigoEstadoCarteraRadicada+")";
	
	/**
	 * Cadena para consultar los eventos de cuentas cobro generadas
	 */
	private static final String consultarEventoCuentasCobroGeneradasFacInactivasStr = "SELECT "+ConstantesBD.eventoCuentasCobroCartera+" as evento,"+ 
		"f.consecutivo_factura as numero_documento, "+
		ConstantesBD.codigoTipoDocInteFacturaPaciente+" as tipo_documento, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteFacturaPaciente+") as nombre_tipo_documento, "+
		"cc.HORA_APROBACION as hora, "+
		"to_char(f.fecha,'"+ConstantesBD.formatoFechaBD+"') as fecha, "+
		"coalesce(cc.obs_generacion,'') as observaciones, "+
		"'' as fecha_radicacion, "+ 
		" c.codigo as codigo_convenio, "+ 
		"c.tipo_regimen as codigo_tipo_regimen, "+ 
		"c.tipo_convenio as codigo_tipo_convenio, "+
		"c.institucion as codigo_institucion, "+
		"coalesce(ca.codigo,'') as codigo_centro_atencion," +
		"FECHASEGUIMIENTO as fecha_seguimiento "+
		"FROM cartera.cuentas_cobro cc "+ 
		"inner join cartera.inactivacion_factura ifac on (ifac.numero_cuenta_cobro=cc.numero_cuenta_cobro and cc.institucion = ifac.institucion ) " +
		"INNER JOIN facturacion.facturas f ON(ifac.codigo_factura = f.codigo) "+ 
		"INNER JOIN facturacion.convenios c on(c.codigo = f.convenio) "+
		"LEFT OUTER JOIN administracion.centro_atencion ca ON(ca.consecutivo = f.centro_aten) "+
		"WHERE "+ 
		"to_char(cc.fecha_aprobacion,'"+ConstantesBD.formatoFechaBD+"') = ? and cc.estado in ("+ConstantesBD.codigoEstadoCarteraAprobado+","+ConstantesBD.codigoEstadoCarteraRadicada+")";
	
	/**
	 * Cadena para consultar los eventos de cuentas cobro generadas
	 */
	private static final String consultarEventoCuentasCobroGeneradasFacDevueltasStr = "SELECT "+ConstantesBD.eventoCuentasCobroCartera+" as evento,"+ 
		"f.consecutivo_factura as numero_documento, "+
		ConstantesBD.codigoTipoDocInteFacturaPaciente+" as tipo_documento, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteFacturaPaciente+") as nombre_tipo_documento, "+
		"cc.HORA_APROBACION as hora, "+
		"to_char(f.fecha,'"+ConstantesBD.formatoFechaBD+"') as fecha, "+
		"coalesce(cc.obs_generacion,'') as observaciones, "+
		"'' as fecha_radicacion, "+ 
		" c.codigo as codigo_convenio, "+ 
		"c.tipo_regimen as codigo_tipo_regimen, "+ 
		"c.tipo_convenio as codigo_tipo_convenio, "+
		"c.institucion as codigo_institucion, "+
		"coalesce(ca.codigo,'') as codigo_centro_atencion," +
		"FECHASEGUIMIENTO as fecha_seguimiento "+
		"FROM cartera.cuentas_cobro cc "+ 
		"inner join cartera.movimientos_cxc mc on (mc.numero_cuenta_cobro=cc.numero_cuenta_cobro and mc.institucion = cc.institucion ) " +
		"INNER JOIN cartera.det_movimientos_cxc dmc ON(dmc.numero_cuenta_cobro = mc.numero_cuenta_cobro and dmc.institucion = mc.institucion  and mc.tipo_movimiento = "+ConstantesBD.codigoTipoMovimientoDevolucion+") "+ 
		"INNER JOIN facturacion.facturas f ON(dmc.factura = f.codigo) "+ 
		"INNER JOIN facturacion.convenios c on(c.codigo = f.convenio) "+
		"LEFT OUTER JOIN administracion.centro_atencion ca ON(ca.consecutivo = f.centro_aten) "+
		"WHERE "+ 
		"to_char(cc.fecha_aprobacion,'"+ConstantesBD.formatoFechaBD+"') = ? and cc.estado in ("+ConstantesBD.codigoEstadoCarteraAprobado+","+ConstantesBD.codigoEstadoCarteraRadicada+")";

	
	/**
	 * Cadena para consultar los eventos de cuentas cobro radicadas
	 */
	private static final String consultarEventoCuentrasCobroRadicadasStr = "SELECT "+ConstantesBD.eventoRadicacionCuentasCobroCartera+" as evento,"+ 
		"f.consecutivo_factura as numero_documento, "+
		ConstantesBD.codigoTipoDocInteFacturaPaciente+" as tipo_documento, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteFacturaPaciente+") as nombre_tipo_documento, "+
		"cc.hora_radica as hora, "+
		"to_char(f.fecha,'"+ConstantesBD.formatoFechaBD+"') as fecha, "+
		"coalesce(cc.obs_radicacion,'') as observaciones, "+
		"to_char(cc.fecha_radicacion,'"+ConstantesBD.formatoFechaAp+"') as fecha_radicacion, "+ 
		"c.codigo as codigo_convenio, "+ 
		"c.tipo_regimen as codigo_tipo_regimen, "+ 
		"c.tipo_convenio as codigo_tipo_convenio, "+
		"c.institucion as codigo_institucion, "+
		"coalesce(ca.codigo,'') as codigo_centro_atencion," +
		"to_char(cc.fecha_radica,'"+ConstantesBD.formatoFechaAp+"') as fecha_seguimiento "+
		"FROM cartera.cuentas_cobro cc "+ 
		"INNER JOIN facturacion.facturas f ON(f.numero_cuenta_cobro = cc.numero_cuenta_cobro and f.institucion = cc.institucion) "+ 
		"INNER JOIN facturacion.convenios c on(c.codigo = f.convenio) "+
		"LEFT OUTER JOIN administracion.centro_atencion ca ON(ca.consecutivo = f.centro_aten) "+
		"WHERE "+ 
		"to_char(cc.fecha_radica,'"+ConstantesBD.formatoFechaBD+"') = ? and cc.estado  =  "+ConstantesBD.codigoEstadoCarteraRadicada;
	
	/**
	 * Cadena para consultar los eventos de cuentas cobro radicadas
	 */
	private static final String consultarEventoCuentrasCobroRadicadasFacInactivasStr = "SELECT "+ConstantesBD.eventoRadicacionCuentasCobroCartera+" as evento,"+ 
		"f.consecutivo_factura as numero_documento, "+
		ConstantesBD.codigoTipoDocInteFacturaPaciente+" as tipo_documento, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteFacturaPaciente+") as nombre_tipo_documento, "+
		"cc.hora_radica as hora, "+
		"to_char(f.fecha,'"+ConstantesBD.formatoFechaBD+"') as fecha, "+
		"coalesce(cc.obs_radicacion,'') as observaciones, "+
		"to_char(cc.fecha_radicacion,'"+ConstantesBD.formatoFechaAp+"') as fecha_radicacion, "+ 
		"c.codigo as codigo_convenio, "+ 
		"c.tipo_regimen as codigo_tipo_regimen, "+ 
		"c.tipo_convenio as codigo_tipo_convenio, "+
		"c.institucion as codigo_institucion, "+
		"coalesce(ca.codigo,'') as codigo_centro_atencion," +
		"to_char(cc.fecha_radica,'"+ConstantesBD.formatoFechaAp+"') as fecha_seguimiento "+
		"FROM cartera.cuentas_cobro cc "+ 
		"inner join cartera.inactivacion_factura ifac on (ifac.numero_cuenta_cobro=cc.numero_cuenta_cobro and cc.institucion = ifac.institucion ) " +
		"INNER JOIN facturacion.facturas f ON(ifac.codigo_factura = f.codigo) "+ 
		"INNER JOIN facturacion.convenios c on(c.codigo = f.convenio) "+
		"LEFT OUTER JOIN administracion.centro_atencion ca ON(ca.consecutivo = f.centro_aten) "+
		"WHERE "+ 
		"to_char(cc.fecha_radica,'"+ConstantesBD.formatoFechaBD+"') = ? and cc.estado  =  "+ConstantesBD.codigoEstadoCarteraRadicada;
	
	/**
	 * Cadena para consultar los eventos de cuentas cobro radicadas
	 */
	private static final String consultarEventoCuentrasCobroRadicadasFacDevueltasStr = "SELECT "+ConstantesBD.eventoRadicacionCuentasCobroCartera+" as evento,"+ 
		"f.consecutivo_factura as numero_documento, "+
		ConstantesBD.codigoTipoDocInteFacturaPaciente+" as tipo_documento, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteFacturaPaciente+") as nombre_tipo_documento, "+
		"cc.hora_radica as hora, "+
		"to_char(f.fecha,'"+ConstantesBD.formatoFechaBD+"') as fecha, "+
		"coalesce(cc.obs_radicacion,'') as observaciones, "+
		"to_char(cc.fecha_radicacion,'"+ConstantesBD.formatoFechaAp+"') as fecha_radicacion, "+ 
		"c.codigo as codigo_convenio, "+ 
		"c.tipo_regimen as codigo_tipo_regimen, "+ 
		"c.tipo_convenio as codigo_tipo_convenio, "+
		"c.institucion as codigo_institucion, "+
		"coalesce(ca.codigo,'') as codigo_centro_atencion," +
		"to_char(cc.fecha_radica,'"+ConstantesBD.formatoFechaAp+"') as fecha_seguimiento "+
		"FROM cartera.cuentas_cobro cc "+ 
		"inner join cartera.movimientos_cxc mc on (mc.numero_cuenta_cobro=cc.numero_cuenta_cobro and mc.institucion = cc.institucion ) " +
		"INNER JOIN cartera.det_movimientos_cxc dmc ON(dmc.numero_cuenta_cobro = mc.numero_cuenta_cobro and dmc.institucion = mc.institucion  and mc.tipo_movimiento = "+ConstantesBD.codigoTipoMovimientoDevolucion+") "+ 
		"INNER JOIN facturacion.facturas f ON(dmc.factura = f.codigo) "+ 
		"INNER JOIN facturacion.convenios c on(c.codigo = f.convenio) "+
		"LEFT OUTER JOIN administracion.centro_atencion ca ON(ca.consecutivo = f.centro_aten) "+
		"WHERE "+ 
		"to_char(cc.fecha_radica,'"+ConstantesBD.formatoFechaBD+"') = ? and cc.estado  =  "+ConstantesBD.codigoEstadoCarteraRadicada;
	
	
	/**
	 * Cadena para consultar los eventos de facturas inactivas de cuenas de cobro
	 */
	private static final String consultarEventoFacturasInactivasCuentaCobroStr = "SELECT "+ConstantesBD.eventoInactivacionFacturasCuentasCobro+" as evento,"+ 
		"f.consecutivo_factura as numero_documento, "+
		ConstantesBD.codigoTipoDocInteFacturaPaciente+" as tipo_documento, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteFacturaPaciente+") as nombre_tipo_documento, "+
		"fi.hora_inactivacion as hora, "+
		"to_char(f.fecha,'"+ConstantesBD.formatoFechaBD+"') as fecha, "+
		"coalesce(fi.obs_inactivacion,'') as observaciones, "+
		"'' as fecha_radicacion, "+ 
		"c.codigo as codigo_convenio, "+ 
		"c.tipo_regimen as codigo_tipo_regimen, "+ 
		"c.tipo_convenio as codigo_tipo_convenio, "+
		"c.institucion as codigo_institucion, "+ 
		"coalesce(ca.codigo,'') as codigo_centro_atencion," +
		"to_char(to_date(fi.fecha_inactivacion,'yyyy-mm-dd'),'"+ConstantesBD.formatoFechaAp+"') as fecha_seguimiento "+
		"FROM cartera.inactivacion_factura fi "+ 
		"INNER JOIN facturacion.facturas f on(f.codigo = fi.codigo_factura) "+  
		"INNER JOIN facturacion.convenios c on(c.codigo = f.convenio) "+
		"LEFT OUTER JOIN administracion.centro_atencion ca ON(ca.consecutivo = f.centro_aten) "+
		"WHERE " +
		"fi.fecha_inactivacion = ?";
	
	/**
	 * Cadena para cpnsultar los eventos de facturas devueltas de una cuenta de cobro
	 */
	private static final String consultarEventoFacturasDEvueltasCuentaCobroStr = "SELECT "+ConstantesBD.eventoDevolucionCuentasCobro+" as evento,"+ 
		"f.consecutivo_factura as numero_documento, "+
		ConstantesBD.codigoTipoDocInteFacturaPaciente+" as tipo_documento, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteFacturaPaciente+") as nombre_tipo_documento, "+
		"mc.hora_movimiento as hora, "+
		"to_char(f.fecha,'"+ConstantesBD.formatoFechaBD+"') as fecha, "+
		"coalesce(mc.observacion,'') as observaciones, "+
		"'' as fecha_radicacion, "+ 
		"c.codigo as codigo_convenio, "+ 
		"c.tipo_regimen as codigo_tipo_regimen, "+ 
		"c.tipo_convenio as codigo_tipo_convenio, "+
		"c.institucion as codigo_institucion, "+
		"coalesce(ca.codigo,'') as codigo_centro_atencion, "+
		"to_char(mc.fecha_movimiento,'"+ConstantesBD.formatoFechaAp+"') as fecha_seguimiento "+
		"FROM cartera.movimientos_cxc mc "+ 
		"INNER JOIN cartera.det_movimientos_cxc dmc ON(dmc.numero_cuenta_cobro = mc.numero_cuenta_cobro and dmc.institucion = mc.institucion) "+ 
		"INNER JOIN facturacion.facturas f on(f.codigo = dmc.factura) "+  
		"INNER JOIN facturacion.convenios c on(c.codigo = f.convenio) "+
		"LEFT OUTER JOIN administracion.centro_atencion ca ON(ca.consecutivo = f.centro_aten) "+
		"WHERE "+ 
		"to_char(mc.fecha_movimiento,'"+ConstantesBD.formatoFechaBD+"') = ? and mc.tipo_movimiento = "+ConstantesBD.codigoTipoMovimientoDevolucion;
	
	/**
	 * Cadena pra consultar los eventos de facturas registradas en glsoas tipo devolucion
	 */
	private static final String consultarEventoFacturasGlosasDevolucionStr = "SELECT DISTINCT "+ConstantesBD.eventoRegistroGlosasDevolucion+" as evento,"+ 
		"f.consecutivo_factura as numero_documento, "+
		ConstantesBD.codigoTipoDocInteFacturaPaciente+" as tipo_documento, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteFacturaPaciente+") as nombre_tipo_documento, "+
		"f.hora as hora, "+
		"to_char(f.fecha,'"+ConstantesBD.formatoFechaBD+"') as fecha, "+
		"coalesce(rg.observaciones,'') as observaciones, "+
		"'' as fecha_radicacion, "+ 
		"c.codigo as codigo_convenio, "+ 
		"c.tipo_regimen as codigo_tipo_regimen, "+ 
		"c.tipo_convenio as codigo_tipo_convenio, "+
		"c.institucion as codigo_institucion, "+
		"coalesce(ca.codigo,'') as codigo_centro_atencion," +
		"FECHASEGUIMIENTO as fecha_seguimiento "+
		"FROM glosas.registro_glosas rg "+ 
		"INNER JOIN glosas.auditorias_glosas ag ON(ag.glosa = rg.codigo) "+ 
		"INNER JOIN facturacion.facturas f on(f.codigo = ag.codigo_factura) "+  
		"INNER JOIN facturacion.convenios c on(c.codigo = f.convenio) "+
		"INNER JOIN glosas.conceptos_audi_glosas cag ON(cag.auditoria_glosa = ag.codigo) "+ 
		"inner JOIN glosas.concepto_glosas cg1 ON(cg1.codigo = cag.concepto_glosa and cg1.institucion = cag.institucion) "+
		"LEFT OUTER JOIN administracion.centro_atencion ca ON(ca.consecutivo = f.centro_aten) "+
		"WHERE " +
		"to_char(rg.fecha_aprobacion,'"+ConstantesBD.formatoFechaBD+"') = ? and " +
		"rg.estado in ('"+ConstantesIntegridadDominio.acronimoEstadoGlosaAprobada+"','"+ConstantesIntegridadDominio.acronimoEstadoGlosaConciliada+"','"+ConstantesIntegridadDominio.acronimoEstadoGlosaRespondida+"') and " +
		"cg1.tipo_concepto = '"+ConstantesIntegridadDominio.acronimoTipoGlosaDevolucion+"' ";
	
	/**
	 * Cadena para consultar los eventos de facturas auditadas
	 */
	private static final String consultarEventoFacturasAuditadasStr = "SELECT "+ConstantesBD.eventoAuditoriaFacturas+" as evento," +
		"f.consecutivo_factura as numero_documento, "+
		ConstantesBD.codigoTipoDocInteFacturaPaciente+" as tipo_documento, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteFacturaPaciente+") as nombre_tipo_documento, "+
		"f.hora as hora, "+
		"to_char(f.fecha,'"+ConstantesBD.formatoFechaBD+"') as fecha, "+
		"coalesce(rg.observaciones,'') as observaciones, "+
		"'' as fecha_radicacion, "+ 
		"c.codigo as codigo_convenio, "+ 
		"c.tipo_regimen as codigo_tipo_regimen, "+ 
		"c.tipo_convenio as codigo_tipo_convenio, "+
		"c.institucion as codigo_institucion, "+
		"coalesce(ca.codigo,'') as codigo_centro_atencion," +
		"FECHASEGUIMIENTO as fecha_seguimiento "+
		"FROM glosas.registro_glosas rg "+ 
		"INNER JOIN glosas.auditorias_glosas ag ON(ag.glosa = rg.codigo) "+ 
		"INNER JOIN facturacion.facturas f on(f.codigo = ag.codigo_factura) "+  
		"INNER JOIN facturacion.convenios c on(c.codigo = f.convenio) "+
		"LEFT OUTER JOIN administracion.centro_atencion ca ON(ca.consecutivo = f.centro_aten) "+
		"WHERE to_char(rg.fecha_auditoria,'"+ConstantesBD.formatoFechaBD+"') = ?";
	
	/**
	 * Cadena para consultar los eventros de facturas registradas en glosas
	 */
	private static final String consultarEventoFacturasRegistradasEnGlosasStr = "SELECT "+ConstantesBD.eventoRegistroGlosas+" as evento,"+ 
		"f.consecutivo_factura as numero_documento, "+
		ConstantesBD.codigoTipoDocInteFacturaPaciente+" as tipo_documento, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteFacturaPaciente+") as nombre_tipo_documento, "+
		"f.hora as hora, "+
		"to_char(f.fecha,'"+ConstantesBD.formatoFechaBD+"') as fecha, "+
		"coalesce(rg.observaciones,'') as observaciones, "+
		"'' as fecha_radicacion, "+ 
		"c.codigo as codigo_convenio, "+ 
		"c.tipo_regimen as codigo_tipo_regimen, "+ 
		"c.tipo_convenio as codigo_tipo_convenio, "+
		"c.institucion as codigo_institucion, "+
		"coalesce(ca.codigo,'') as codigo_centro_atencion," +
		"FECHASEGUIMIENTO as fecha_seguimiento "+
		"FROM glosas.registro_glosas rg "+ 
		"INNER JOIN glosas.auditorias_glosas ag ON(ag.glosa = rg.codigo) "+ 
		"INNER JOIN facturacion.facturas f on(f.codigo = ag.codigo_factura) "+  
		"INNER JOIN facturacion.convenios c on(c.codigo = f.convenio) "+
		"LEFT OUTER JOIN administracion.centro_atencion ca ON(ca.consecutivo = f.centro_aten) "+
		"WHERE " +
		"to_char(rg.fecha_aprobacion,'"+ConstantesBD.formatoFechaBD+"') = ? and " +
		"rg.estado in ('"+ConstantesIntegridadDominio.acronimoEstadoGlosaAprobada+"','"+ConstantesIntegridadDominio.acronimoEstadoGlosaConciliada+"','"+ConstantesIntegridadDominio.acronimoEstadoGlosaRespondida+"')  " ;
	
	/**
	 * Cadena para consultar los eventos de facturas registradas en glosas con respuesta
	 */
	private static final String consultarEventoFacturasRegistradasEnGlosasRespuestaStr = "SELECT "+ConstantesBD.eventoRegistroRespuestaGlosas+" as evento,"+ 
		"f.consecutivo_factura as numero_documento, "+
		ConstantesBD.codigoTipoDocInteFacturaPaciente+" as tipo_documento, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteFacturaPaciente+") as nombre_tipo_documento, "+
		"f.hora as hora, "+
		"to_char(f.fecha,'"+ConstantesBD.formatoFechaBD+"') as fecha, "+
		"coalesce(rpg.observaciones,'') as observaciones, "+
		"'' as fecha_radicacion, "+ 
		"c.codigo as codigo_convenio, "+ 
		"c.tipo_regimen as codigo_tipo_regimen, "+ 
		"c.tipo_convenio as codigo_tipo_convenio, "+
		"c.institucion as codigo_institucion, "+
		"coalesce(ca.codigo,'') as codigo_centro_atencion," +
		"FECHASEGUIMIENTO as fecha_seguimiento "+
		"FROM glosas.respuesta_glosa rpg "+ 
		"INNER JOIN glosas.registro_glosas rg ON(rg.codigo = rpg.glosa) "+
		"INNER JOIN glosas.auditorias_glosas ag ON(ag.glosa = rg.codigo) "+ 
		"INNER JOIN facturacion.facturas f on(f.codigo = ag.codigo_factura) "+  
		"INNER JOIN facturacion.convenios c on(c.codigo = f.convenio) "+
		"LEFT OUTER JOIN administracion.centro_atencion ca ON(ca.consecutivo = f.centro_aten) "+
		"WHERE " +
		"to_char(rpg.fecha_aprob_anul,'"+ConstantesBD.formatoFechaBD+"') = ? and " +
		"rpg.estado <> '"+ConstantesIntegridadDominio.acronimoEstadoRespuestaGlosaAnulada+"'  and " +
		"rg.estado in ('"+ConstantesIntegridadDominio.acronimoEstadoGlosaConciliada+"','"+ConstantesIntegridadDominio.acronimoEstadoGlosaRespondida+"')";
	
	/**
	 * Cadena para consultar los eventos de facturas registradas en glosas con respuesta conciliada
	 */
	private static final String consultarEventoFacturasRegistradasEnGlosasRespuestaConciliadaStr = "SELECT "+ConstantesBD.eventorespuestaGlosasConciliadas+" as evento,"+ 
		"f.consecutivo_factura as numero_documento, "+
		ConstantesBD.codigoTipoDocInteFacturaPaciente+" as tipo_documento, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteFacturaPaciente+") as nombre_tipo_documento, "+
		"f.hora as hora, "+
		"to_char(f.fecha,'"+ConstantesBD.formatoFechaBD+"') as fecha, "+
		"coalesce(rpg.observaciones,'') as observaciones, "+
		"'' as fecha_radicacion, "+ 
		"c.codigo as codigo_convenio, "+ 
		"c.tipo_regimen as codigo_tipo_regimen, "+ 
		"c.tipo_convenio as codigo_tipo_convenio, "+
		"c.institucion as codigo_institucion, "+
		"coalesce(ca.codigo,'') as codigo_centro_atencion," +
		"FECHASEGUIMIENTO as fecha_seguimiento "+
		"FROM glosas.respuesta_glosa rpg "+ 
		"INNER JOIN glosas.registro_glosas rg ON(rg.codigo = rpg.glosa) "+
		"INNER JOIN glosas.auditorias_glosas ag ON(ag.glosa = rg.codigo) "+ 
		"INNER JOIN facturacion.facturas f on(f.codigo = ag.codigo_factura) "+  
		"INNER JOIN facturacion.convenios c on(c.codigo = f.convenio) "+
		"LEFT OUTER JOIN administracion.centro_atencion ca ON(ca.consecutivo = f.centro_aten) "+
		"WHERE " +
		"to_char(rpg.fecha_aprob_anul,'"+ConstantesBD.formatoFechaBD+"') = ? and " +
		"rpg.estado <> '"+ConstantesIntegridadDominio.acronimoEstadoRespuestaGlosaAnulada+"'  and " +
		"rg.estado in ('"+ConstantesIntegridadDominio.acronimoEstadoGlosaConciliada+"')";
	
	/**
	 * Cadena para consultar las respuestas de glosas radicadas
	 */
	private static final String consultarEventoRadicacionRespuestaGlosasStr = "SELECT "+ConstantesBD.eventoRadicacionrespuestaGlosas+" as evento,"+ 
		"f.consecutivo_factura as numero_documento, "+
		ConstantesBD.codigoTipoDocInteFacturaPaciente+" as tipo_documento, "+
		"getdesctipodocumento("+ConstantesBD.codigoTipoDocInteFacturaPaciente+") as nombre_tipo_documento, "+
		"f.hora as hora, "+
		"to_char(f.fecha,'"+ConstantesBD.formatoFechaBD+"') as fecha, "+
		"coalesce(rpg.observaciones,'') as observaciones, "+
		"'' as fecha_radicacion, "+ 
		"c.codigo as codigo_convenio, "+ 
		"c.tipo_regimen as codigo_tipo_regimen, "+ 
		"c.tipo_convenio as codigo_tipo_convenio, "+
		"c.institucion as codigo_institucion, "+
		"coalesce(ca.codigo,'') as codigo_centro_atencion," +
		"FECHASEGUIMIENTO as fecha_seguimiento "+
		"FROM glosas.respuesta_glosa rpg "+ 
		"INNER JOIN glosas.registro_glosas rg ON(rg.codigo = rpg.glosa) "+
		"INNER JOIN glosas.auditorias_glosas ag ON(ag.glosa = rg.codigo) "+ 
		"INNER JOIN facturacion.facturas f on(f.codigo = ag.codigo_factura) "+  
		"INNER JOIN facturacion.convenios c on(c.codigo = f.convenio) "+
		"LEFT OUTER JOIN administracion.centro_atencion ca ON(ca.consecutivo = f.centro_aten) "+
		"WHERE " +
		"to_char(rpg.fecha_radicacion,'"+ConstantesBD.formatoFechaBD+"') = ? and " +
		"rpg.estado = '"+ConstantesIntegridadDominio.acronimoEstadoRespuestaGlosaRadicada+"'  and " +
		"rg.estado in ('"+ConstantesIntegridadDominio.acronimoEstadoGlosaConciliada+"','"+ConstantesIntegridadDominio.acronimoEstadoGlosaRespondida+"') ";
	//**************************************************************************************************************************************
	
	
	/**
	 * consulta informacion de tipo doc por tipo movimiento 1e
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ArrayList<HashMap> consultarTipoDocXTipoMov1E(Connection con,HashMap parametros)
	{
		ArrayList<HashMap> array = new ArrayList<HashMap>();
		String cadena = strConsultaTipoDocxTipoMov1e;
		
		cadena = cadena.replace(ConstantesBD.separadorSplit+"1",parametros.get("codigoTipoMov").toString());
				
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				HashMap mapa = new HashMap();
				mapa.put("tipo_documento",rs.getInt("tipo_documento"));
				mapa.put("tipo_movimiento",rs.getString("tipo_movimiento"));
				array.add(mapa);
			}
			rs.close();
			ps.close();
		}
		catch (Exception e) {
			logger.info("error en consultarTipoDocXTipoMov1E >> "+e+" "+cadena);
		}
		
		return array;
	}
	
	
	/**
	 * Método para consultar los documentos contables de un tipo de movimiento específico
	 * @param con
	 * @param parametrizacion
	 * @return
	 */
	public static ArrayList<DtoInterfazLineaS1E> consultarDocumentosContables(Connection con,DtoInterfazS1EInfo parametrizacion)
	{
		huboInconsistencia = false;
		huboLineaCxC = false;
		huboLineaCxP = false;
		huboLineaDetMov = false;
		ArrayList<DtoInterfazLineaS1E> lineas = new ArrayList<DtoInterfazLineaS1E>();
		parametrizacion.setPosicion(1);
		
		//*******************SE AÑADE LA LÍNEA DE INICIO*****************************************************
		logger.info("\n");
		logger.info("..:Movimiento a generar >> "+parametrizacion.getTipoMovimientoSeleccionado().getDescripcion());
		lineas.add(generarLineaInicioFin(parametrizacion,GeneracionInterfaz1E.indicadorLineaInicio));
		parametrizacion.setPosicion(parametrizacion.getPosicion()+1);
		//*********************************************************************************************************
		//**************DEPENDIENDO DEL TIPO DE MOVIMIENTO ********************************************************
		if(parametrizacion.getTipoMovimientoSeleccionado().getCodigo().equals(ConstantesIntegridadDominio.acronimoTipoMovRecaudos))
		{
			lineas.addAll(consultarDocumentosContablesRecaudos(con,parametrizacion));
		}
		else if(parametrizacion.getTipoMovimientoSeleccionado().getCodigo().equals(ConstantesIntegridadDominio.acronimoTipoMovServiciEntidaExter))
		{
			//Consulta/Anulacion las Autorizaciones de Servicios Entidades Subcontratadas
			lineas.addAll(consultarDocumentosContableServicioEntidadesEx(
					con,
					consultarAutoEntidadesSub,
					ConstantesBD.codigoTipoDocInteAutoServicioEntSub,
					parametrizacion));
			
			//Despacho/Devolucion de Medicamentos Entidades Subcontratadas
			lineas.addAll(consultarDocumentosContableServicioEntidadesEx(
					con,
					consultarMedicamentos,
					ConstantesBD.codigoTipoDocInteDespachoMed,
					parametrizacion));
			
			//Despacho/Devolucion de Pedidos Insumos Entidades Subcontratadas
			lineas.addAll(consultarDocumentosContableServicioEntidadesEx(
					con,
					consultarPedidos,
					ConstantesBD.codigoTipoDocInteDespaPedidoInsumo,
					parametrizacion));
			
			//Despacho/Devolucion de Pedidos Quirurgucos Entidades Subcontratadas
			lineas.addAll(consultarDocumentosContableServicioEntidadesEx(
					con,
					consultarPedidosQx,
					ConstantesBD.codigoTipoDocInteDespachoPedidoQx,
					parametrizacion));
			
			//Cargos/Anulacion Directos de Articulos
			lineas.addAll(consultarDocumentosContableServicioEntidadesEx(
					con,
					consultarCargosDirectos,
					ConstantesBD.codigoTipoDocInteCargosDirectosArt,
					parametrizacion));
		}
		else if(parametrizacion.getTipoMovimientoSeleccionado().getCodigo().equals(GeneracionInterfaz1E.acronimoTipoMovVentasInte))
		{
			lineas.addAll(consultarDocumentosContablesVentas(con, parametrizacion));//--
		}
		else if(parametrizacion.getTipoMovimientoSeleccionado().getCodigo().equals(GeneracionInterfaz1E.acronimoTipoMovHonoraInte))
		{
			lineas.addAll(consultarDocumentosContablesHonorarios(con, parametrizacion));
		}
		else if(parametrizacion.getTipoMovimientoSeleccionado().getCodigo().equals(ConstantesIntegridadDominio.acronimoTipoMovAjusteReclasi))
		{
			lineas.addAll(consultarDocumentosContablesAjustes(con, parametrizacion));
		}
		else if(parametrizacion.getTipoMovimientoSeleccionado().getCodigo().equals(ConstantesIntegridadDominio.acronimoTipoMovCartera))
		{
			lineas.addAll(consultarEventosCartera(con, parametrizacion));
		}
		
		//********************************************************************************************************
		//************SE AÑADE LA LÍNEA DE FIN********************************************************************
		lineas.add(generarLineaInicioFin(parametrizacion,GeneracionInterfaz1E.indicadorLineaFinal));
		//*****************************************************************************************************
		
		logger.info("..:FIN Movimiento a generar >> "+parametrizacion.getTipoMovimientoSeleccionado().getDescripcion());
		
		if(huboInconsistencia)
			lineas.get(0).setExisteInconsistencia(true);
		
		//evalua si existieron resultados
		if(lineas.size() == 2)
			return new ArrayList<DtoInterfazLineaS1E>();
		
		return lineas;
	}

	/**
	 * Consultar Documentos Contable Servicio Entidades Externas
	 * @param Connection con
	 * @param DtoInterfazS1EInfo parametrizacion
	 * */
	private static ArrayList<DtoInterfazLineaS1E> consultarDocumentosContableServicioEntidadesEx(
			Connection con,
			String consulta,
			int tipoDocumento,
			DtoInterfazS1EInfo parametrizacion) 
	{
		ArrayList<DtoInterfazLineaS1E> lineas = new ArrayList<DtoInterfazLineaS1E>();
		ArrayList<DtoInterfazLineaS1E> array = new ArrayList<DtoInterfazLineaS1E>();
		String codigoTerceroPed = "",codigoCentroOperacion = "";
		boolean adicionLinea = false;
		
		boolean infDocInteAutoServicioEntSub = false, 
		indDocInteDespachoMed = false, 
		indDocInteDevolucionMedi = false ,
		indDocInteDespaPedidoInsumo = false, 
		indDocInteDevolPedidoInsumo = false, 
		indDocInteDespachoPedidoQx = false,
		indDocInteDevolucionPedidoQx = false,
		indDocInteCargosDirectosArt = false,
		indDocInteAnulaCargosDirectosArticulo = false;
		 
		//logger.info("..:Tipo Documento "+tipoDocumento+" >> "+consulta.replace("?",UtilidadFecha.conversionFormatoFechaABD(parametrizacion.getFechaProceso()))+"\n\n");
		
		try
		{
			//Consulta la informacion de Autorizacion de Entidades Subcontratadas
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			if(tipoDocumento == ConstantesBD.codigoTipoDocInteAutoServicioEntSub)
			{
				pst.setString(1,UtilidadFecha.conversionFormatoFechaABD(parametrizacion.getFechaProceso()));
				pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(parametrizacion.getFechaProceso()));
			}
			else if(tipoDocumento == ConstantesBD.codigoTipoDocInteDespachoMed)
			{
				pst.setString(1,UtilidadFecha.conversionFormatoFechaABD(parametrizacion.getFechaProceso()));
				pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(parametrizacion.getFechaProceso()));
			}
			else if(tipoDocumento == ConstantesBD.codigoTipoDocInteDespaPedidoInsumo)
			{
				pst.setString(1,UtilidadFecha.conversionFormatoFechaABD(parametrizacion.getFechaProceso()));
				pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(parametrizacion.getFechaProceso()));
			}
			else if(tipoDocumento == ConstantesBD.codigoTipoDocInteDespachoPedidoQx)
			{
				pst.setString(1,UtilidadFecha.conversionFormatoFechaABD(parametrizacion.getFechaProceso()));
				pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(parametrizacion.getFechaProceso()));
			}
			else if(tipoDocumento == ConstantesBD.codigoTipoDocInteCargosDirectosArt)
			{
				pst.setString(1,UtilidadFecha.conversionFormatoFechaABD(parametrizacion.getFechaProceso()));
				pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(parametrizacion.getFechaProceso()));
			}

			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{	
				DtoInterfazLineaS1E linea = new DtoInterfazLineaS1E(GeneracionInterfaz1E.indicadorLineaDocumentoContable);
				
				//***********Se asignan los posibles documentos que puede tener el movimiento****************************
				DtoInterfazDatosDocumentoS1E documento02 = new DtoInterfazDatosDocumentoS1E();
				DtoInterfazDatosDocumentoS1E documento01 = new DtoInterfazDatosDocumentoS1E();
				documento01.setNumeroDocumento(rs.getString("numero_documento01"));
				documento01.setDescripcionDocumento(rs.getString("descripcion_documento01"));
				documento01.setTipoDocumento(rs.getString("tipo_documento01"));
				documento01.setCodigoTipoConsecutivo(rs.getString("tipo_consecutivo01"));
				documento01.setFecha(rs.getString("fecha01"));
				documento01.setValor(rs.getString("valor"));
				linea.getArrayDocumentos().add(documento01);
				
				if(!UtilidadTexto.isEmpty(rs.getString("numero_documento02")))
				{
					documento02 = new DtoInterfazDatosDocumentoS1E();
					documento02.setNumeroDocumento(rs.getString("numero_documento02"));
					documento02.setDescripcionDocumento(rs.getString("descripcion_documento02"));
					documento02.setTipoDocumento(rs.getString("tipo_documento02"));
					documento02.setCodigoTipoConsecutivo(rs.getString("tipo_consecutivo02"));
					documento02.setFecha(rs.getString("fecha02"));
					documento02.setValor(rs.getString("valor"));
					linea.getArrayDocumentos().add(documento02);
				}
				
				//*******************************************************************************************************
				
				codigoCentroOperacion = "";
				codigoTerceroPed = "";
				
				if((tipoDocumento == ConstantesBD.codigoTipoDocInteDespachoPedidoQx || 
						tipoDocumento == ConstantesBD.codigoTipoDocInteDespaPedidoInsumo) 
							&&  (documento01.getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteDevolPedidoInsumo+"") || 
									documento01.getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteDevolucionPedidoQx+"")))
				{
					//busca la informacion del tercero y centro de operación
					pst = new PreparedStatementDecorator(con.prepareStatement(pedidoTerceroCenOpera, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					
					if(tipoDocumento == ConstantesBD.codigoTipoDocInteDespachoPedidoQx)
						pst.setString(1,ConstantesBD.acronimoSi);
					else
						pst.setString(1,ConstantesBD.acronimoNo);
					
					pst.setInt(2,Utilidades.convertirAEntero(documento01.getNumeroDocumento()));
						
					ResultSetDecorator rs1 = new ResultSetDecorator(pst.executeQuery());
					
					if(rs1.next())
					{
						codigoCentroOperacion = rs.getString("centro_oper_doc");
						codigoTerceroPed = rs.getString("tercero");
					}
					
					rs1.close();
				}
				 
				/*******************CAMPOS DE LA LÍNEA DOCUMENTO CONTABLE***************************************************/
				//**************VALIDACIÓN CAMPO NUMERO REGISTRO*******************************
				linea.getArrayCampos().get(CampoDocumentoContable.NUMERO_REGISTRO.getPosicion()).setValor(parametrizacion.getPosicion()+"");
				//*******************************************************************************
				//***************VALIDACION CAMPO COMPAÑÍA****************************************
				asignarCodigoInterfazInstitucion(linea, parametrizacion, CampoDocumentoContable.COMPANIA.getPosicion());
				//**********************************************************************************
				//***************VALIDACION CAMPO CENTRO OPERACION****************************************
				if(documento01.getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteDevolPedidoInsumo+"") || 
					documento01.getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteDevolucionPedidoQx+""))
						linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).setValor(codigoCentroOperacion);
				else
						linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).setValor(rs.getString("centro_oper_doc"));
				
				if(linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getValor().equals(""))
				{
					//Con reportar la línea de inicio es suficiente
					linea.setExisteInconsistencia(true);
					linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).setInconsistencia(true);
					linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
					linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
					linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
					linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getDescripcion()+" ");
				}
				
				else if(linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getValor().length()>3)
				{
					//Si el campo supera el tamaño se trunca
					linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getValor().substring(0, 3));
				}
				//**********************************************************************************
				//**************VALIDACION CAMPO TIPO DE DOCUMENTO************************************
				asignarIndTipoDocumento(linea, parametrizacion, CampoDocumentoContable.TIPO_DOCUMENTO.getPosicion(), linea.getArrayDocumentos().get(0).getTipoDocumento(),"");
				//*************************************************************************************
				//*************VALIDACION CAMPO NÚMERO DE DOCUMENTO**************************************
				asignarNumeroDocumento(linea, parametrizacion, CampoDocumentoContable.NRO_DOCUMENTO.getPosicion(), linea.getArrayDocumentos().get(0).getTipoDocumento(),"","");
				//****************************************************************************************
				//***************VALIDACION CAMPO FECHA DOCUMNENTO*****************************************
				linea.getArrayCampos().get(CampoDocumentoContable.FECHA_DOCUMENTO.getPosicion()).setValor(parametrizacion.getFechaProceso());
				//*******************************************************************************************
				///***************VALIDACION CAMPO TERCERO DOCUMENTO*****************************************
				if(documento01.getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteDevolPedidoInsumo+"") || 
						documento01.getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteDevolucionPedidoQx+""))
					linea.getArrayCampos().get(CampoDocumentoContable.TERCERO_DOCUMENTO.getPosicion()).setValor(codigoTerceroPed);
				else
					linea.getArrayCampos().get(CampoDocumentoContable.TERCERO_DOCUMENTO.getPosicion()).setValor(rs.getString("tercero"));
				//*******************************************************************************************
				//******************VALIDACION CAMPO OBSERVACIONES ENCABEZADO************************************
				asignarObservacionesDocumento(linea, parametrizacion, CampoDocumentoContable.OBSERVACIONES_DOCUMENTO.getPosicion(), linea.getArrayDocumentos().get(0).getTipoDocumento());
				//************************************************************************************************
				
				parametrizacion.setPosicion(parametrizacion.getPosicion()+1);
				
				
				/**
				 * MT382 - MT2816
				 * Si la autorizacion no tiene tarifa no se debe generar interfaz, se debe generar inconsistencia
				 * Diana Ruiz
				 * 
				 */
				
				
				if (!linea.getArrayDocumentos().get(0).getValor().isEmpty()){
					/*********************CALCULO DE LA RETENCION*******************************************/
					DtoRetencion retencion = new DtoRetencion();
					UtilidadesInterfaz.calcularRetencionInterfaz(con, linea.getArrayDocumentos(), parametrizacion, linea.getArrayCampos().get(CampoDocumentoContable.TERCERO_DOCUMENTO.getPosicion()).getValor(), true,Double.parseDouble(linea.getArrayDocumentos().get(0).getValor()));					
										
					for(String mensajeIncon:retencion.getInconsistenciasRetencion())
					{
						linea.setExisteInconsistencia(true);
						linea.getArrayCampos().get(CampoDocumentoContable.NUMERO_REGISTRO.getPosicion()).setInconsistencia(true);
						linea.getArrayCampos().get(CampoDocumentoContable.NUMERO_REGISTRO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						linea.getArrayCampos().get(CampoDocumentoContable.NUMERO_REGISTRO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						linea.getArrayCampos().get(CampoDocumentoContable.NUMERO_REGISTRO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						linea.getArrayCampos().get(CampoDocumentoContable.NUMERO_REGISTRO.getPosicion()).setDescripcionIncon(mensajeIncon);
					}
					
					
					//*******************************************************************************************
					//Lineas de movimiento
					adicionLinea = false;
					array = new ArrayList<DtoInterfazLineaS1E>();
					
					array = generarLineaMovimientoEntidadesExternas(con, linea, parametrizacion,retencion);
					
					if(array.size() > 0)
					{
						if(!adicionLinea)
						{
							lineas.add(linea);
							adicionLinea = true;
						}
						
						lineas.addAll(array);
					}
					
					//Lineas de cuentas por pagar
					array = new ArrayList<DtoInterfazLineaS1E>();
					array =  generarLineaMovimientoEntidadesExternasCuentasXPagar(con, linea, parametrizacion, retencion);
					
					if(array.size() > 0)
					{
						if(!adicionLinea)
						{
							lineas.add(linea);
							adicionLinea = true;
						}
						
						lineas.addAll(array);
					}
					
				} else {
					
					linea.setExisteInconsistencia(true);
					linea.getArrayCampos().get(CampoDocumentoContable.NUMERO_REGISTRO.getPosicion()).setInconsistencia(true);
					linea.getArrayCampos().get(CampoDocumentoContable.NUMERO_REGISTRO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
					linea.getArrayCampos().get(CampoDocumentoContable.NUMERO_REGISTRO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
					linea.getArrayCampos().get(CampoDocumentoContable.NUMERO_REGISTRO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
					linea.getArrayCampos().get(CampoDocumentoContable.NUMERO_REGISTRO.getPosicion()).setDescripcionIncon("Problemas verificando la tarifa de la autorización");
					
					lineas.add(linea);
					adicionLinea = true;
				}
				
				
				
				if(adicionLinea)
				{
					if(linea.isExisteInconsistencia())
						huboInconsistencia = true;
					
					if(!huboInconsistencia)
					{
					
						if(tipoDocumento == ConstantesBD.codigoTipoDocInteCargosDirectosArt)					
							actualizarRegistroMarcado(con,documento02.getTipoDocumento(),documento02.getNumeroDocumento());
						else
							actualizarRegistroMarcado(con,documento01.getTipoDocumento(),documento01.getNumeroDocumento());
					}
				}
			}
			rs.close();
			pst.close();
			
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarDocumentosContableServicioEntidadesEx: "+e+" >> "+consulta);
		}
		
		return lineas;
	}

	/**
	 * consulta informacion de tipo doc por tipo movimiento 1e
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	private static ArrayList<DtoInterfazLineaS1E> consultarDocumentosContablesRecaudos(Connection con,DtoInterfazS1EInfo parametrizacion) 
	{
		ArrayList<DtoInterfazLineaS1E> lineas = new ArrayList<DtoInterfazLineaS1E>();
		boolean indDocInteReciboCaja = false, indDocInteAnulaRecCaja = false ;
		int codigoTipoDocumento = ConstantesBD.codigoNuncaValido;

		
		try
		{
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consultaDocumentosContablesRecaudosStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			pst.setString(1,UtilidadFecha.conversionFormatoFechaABD(parametrizacion.getFechaProceso()));
			pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(parametrizacion.getFechaProceso()));
			pst.setString(3,UtilidadFecha.conversionFormatoFechaABD(parametrizacion.getFechaProceso()));
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{
				DtoInterfazLineaS1E linea = new DtoInterfazLineaS1E(GeneracionInterfaz1E.indicadorLineaDocumentoContable);
				
				//***********Se asignan los posibles documentos que puede tener el movimiento****************************
				DtoInterfazDatosDocumentoS1E documento01 = new DtoInterfazDatosDocumentoS1E();
				documento01.setNumeroDocumento(rs.getString("numero_documento01"));
				documento01.setConsecutivoDocumento(rs.getString("consecutivo_documento01"));
				documento01.setDescripcionDocumento(rs.getString("descripcion_documento01"));
				documento01.setTipoDocumento(rs.getString("tipo_documento01"));
				documento01.setCodigoTipoConsecutivo(rs.getString("tipo_consecutivo01"));
				documento01.setFecha(rs.getString("fecha01"));
				codigoTipoDocumento = Integer.parseInt(documento01.getTipoDocumento());
				linea.getArrayDocumentos().add(documento01);
				
				if(!UtilidadTexto.isEmpty(rs.getString("numero_documento02")))
				{
					DtoInterfazDatosDocumentoS1E documento02 = new DtoInterfazDatosDocumentoS1E();
					documento02.setNumeroDocumento(rs.getString("numero_documento02"));
					documento02.setConsecutivoDocumento(rs.getString("consecutivo_documento02"));
					documento02.setDescripcionDocumento(rs.getString("descripcion_documento02"));
					documento02.setTipoDocumento(rs.getString("tipo_documento02"));
					documento02.setCodigoTipoConsecutivo(rs.getString("tipo_consecutivo02"));
					documento02.setFecha(rs.getString("fecha02"));
					linea.getArrayDocumentos().add(documento02);
				}
				
				//*******************************************************************************************************
				
				/*
				if(!indDocInteReciboCaja && documento01.getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteReciboCaja+""))
				{
					indDocInteReciboCaja = true;
					logger.info("..:Analizar Tipo Documento "+documento01.getDescripcionDocumento()+" lineas ["+
							DtoInterfazCampoS1E.getDescripcionLinea(GeneracionInterfaz1E.indicadorLineaDetMovDocumentoContable)+" "+
							DtoInterfazCampoS1E.getDescripcionLinea(GeneracionInterfaz1E.indicadorLineaDetCxC)+"]");
				}
					
				if(!indDocInteAnulaRecCaja && documento01.getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteAnulaRecCaja+""))
				{
					indDocInteAnulaRecCaja = true;
					logger.info("..:Analizar Tipo Documento "+documento01.getDescripcionDocumento()+" lineas ["+
							DtoInterfazCampoS1E.getDescripcionLinea(GeneracionInterfaz1E.indicadorLineaDetMovDocumentoContable)+" "+
							DtoInterfazCampoS1E.getDescripcionLinea(GeneracionInterfaz1E.indicadorLineaDetCxC)+"]");
				}**/
				 
				/*******************CAMPOS DE LA LÍNEA DOCUMENTO CONTABLE***************************************************/
				
				//**************VALIDACIÓN CAMPO NUMERO REGISTRO*******************************
				linea.getArrayCampos().get(CampoDocumentoContable.NUMERO_REGISTRO.getPosicion()).setValor(parametrizacion.getPosicion()+"");
				//*******************************************************************************
				//***************VALIDACION CAMPO COMPAÑÍA****************************************
				asignarCodigoInterfazInstitucion(linea, parametrizacion, CampoDocumentoContable.COMPANIA.getPosicion());
				//**********************************************************************************
				//***************VALIDACION CAMPO CENTRO OPERACION****************************************
				linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).setValor(rs.getString("codigo_centro_atencion"));
				if(linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getValor().equals(""))
				{
					linea.setExisteInconsistencia(true);
					linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).setInconsistencia(true);
					linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
					linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).setConsecutivoDocumento(linea.getArrayDocumentos().get(0).getConsecutivoDocumento());
					linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
					linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
					linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getDescripcion()+". Funcionalidad Parametrizacion Interfaz Sistema 1E.");
				}
				
				//**********************************************************************************
				//**************VALIDACION CAMPO TIPO DE DOCUMENTO************************************
				asignarIndTipoDocumento(linea, parametrizacion, CampoDocumentoContable.TIPO_DOCUMENTO.getPosicion(), linea.getArrayDocumentos().get(0).getTipoDocumento(),"");
				//*************************************************************************************
				//*************VALIDACION CAMPO NÚMERO DE DOCUMENTO**************************************
				asignarNumeroDocumento(linea, parametrizacion, CampoDocumentoContable.NRO_DOCUMENTO.getPosicion(), linea.getArrayDocumentos().get(0).getTipoDocumento(),"",ConstantesIntegridadDominio.acronimoTipoMovRecaudos);
				//****************************************************************************************
				//***************VALIDACION CAMPO FECHA DOCUMNENTO*****************************************
				linea.getArrayCampos().get(CampoDocumentoContable.FECHA_DOCUMENTO.getPosicion()).setValor(parametrizacion.getFechaProceso());
				//*******************************************************************************************
				///***************VALIDACION CAMPO TERCERO DOCUMENTO*****************************************
				asignarTerceroDocumento(con, linea, parametrizacion, CampoDocumentoContable.TERCERO_DOCUMENTO.getPosicion());
				//*******************************************************************************************
				//******************VALIDACION CAMPO OBSERVACIONES ENCABEZADO************************************
				asignarObservacionesDocumento(linea, parametrizacion, CampoDocumentoContable.OBSERVACIONES_DOCUMENTO.getPosicion(), linea.getArrayDocumentos().get(0).getTipoDocumento());
				//************************************************************************************************
				
				parametrizacion.setPosicion(parametrizacion.getPosicion()+1);
				
				/*******************CAMPOS DE LA LÍNEA MOVIMIENTO DOCUMENTO CONTABLE***************************************************/
				linea.getArrayDetalle().addAll(generarLineaMovimientoRecaudo(con,linea,parametrizacion));
				
				/*******************CAMPOS DE LA LÍNEA MOVIMIENTO CUENTAS X COBRAR*******************************************************/
				linea.getArrayDetalle().addAll(generarLineaMovimientoRecaudoCuentasXCobrar(con,linea,parametrizacion));
				
				//***********VALIDACION DE LAS SUMAS IGUALES****************************************************
				//sumas iguales en los credito y debito
				
				if((Math.round(linea.getSumaDebito()*Math.pow(10,8))/Math.pow(10,8))-(Math.round(linea.getSumaCredito()*Math.pow(10,8))/Math.pow(10,8)) != 0)
				{
					linea.setExisteInconsistencia(true);
					linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).setInconsistencia(true);
					linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
					linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).setConsecutivoDocumento(linea.getArrayDocumentos().get(0).getConsecutivoDocumento());
					linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
					linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.SUMAS_IGUALES);
					linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).setDescripcionIncon("Los totales son diferentes D: "+UtilidadTexto.formatearValores(linea.getSumaDebito())+" C: "+UtilidadTexto.formatearValores(linea.getSumaCredito()));
				}
				
				if(linea.isExisteInconsistencia())
					huboInconsistencia = true;
				
				if(!huboInconsistencia)
					actualizarRegistroMarcado(con,documento01.getTipoDocumento(),documento01.getNumeroDocumento());
				
				
				lineas.add(linea);
			}
			rs.close();
			pst.close();
			
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarDocumentosContablesRecaudos: "+e);
		}
		
		return lineas;
	}


	/**
	 * Método implementado para generar la línea de inicio
	 * @param parametrizacion
	 * @param contador 
	 * @return
	 */
	private static DtoInterfazLineaS1E generarLineaInicioFin(DtoInterfazS1EInfo parametrizacion,String indicador) 
	{
		DtoInterfazLineaS1E lineaInicio = new DtoInterfazLineaS1E(indicador);
		
		//*********************VALIDACIONES CAMPO NUMERO REGISTRO********************************************************
		int posicion = CampoLineaInicioFin.NUMERO_REGISTRO.getPosicion();
		lineaInicio.getArrayCampos().get(posicion).setValor(parametrizacion.getPosicion()+"");
		
		//*********************VALIDACIONES CAMPO COMPAÑÍA***************************************************************
		asignarCodigoInterfazInstitucion(lineaInicio, parametrizacion, CampoLineaInicioFin.COMPANIA.getPosicion());
		//***************************************************************************************************************
		
		return lineaInicio;
	}
	
	/**
	 * Método para asignar el codigo interfaz de la institucion
	 * @param linea
	 * @param parametrizacion
	 * @param posicion
	 */
	private static void asignarCodigoInterfazInstitucion(DtoInterfazLineaS1E linea,DtoInterfazS1EInfo parametrizacion, int posicion)
	{ 
		linea.getArrayCampos().get(posicion).setValor(parametrizacion.getInstitucionBasica().getCodigoInterfaz());
		if(UtilidadTexto.isEmpty(linea.getArrayCampos().get(posicion).getValor()))
		{
			//Con reportar la línea de inicio es suficiente
			if(linea.getTipoLinea().equals(GeneracionInterfaz1E.indicadorLineaInicio))
			{
				if(!parametrizacion.isHuboInconsistenciaCompania())
				{
					logger.info("inconsistencia =================================================");
					parametrizacion.setHuboInconsistenciaCompania(true);
					linea.setExisteInconsistencia(true);
					linea.getArrayCampos().get(posicion).setInconsistencia(true);
					linea.getArrayCampos().get(posicion).setTipoInconsistencia(TipoInconsistencia.DATOS);
					linea.getArrayCampos().get(posicion).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+linea.getArrayCampos().get(posicion).getDescripcion()+". Funcionalidad parametrizacion institucion.");
				}
			}
		}
		else if(linea.getArrayCampos().get(posicion).getValor().length()>3)
		{
			///Con reportar la línea de inicio es suficiente
			if(linea.getTipoLinea().equals(GeneracionInterfaz1E.indicadorLineaInicio))
			{
				logger.info("inconsistencia =================================================");
				linea.setExisteInconsistencia(true);
				linea.getArrayCampos().get(posicion).setInconsistencia(true);
				linea.getArrayCampos().get(posicion).setTipoInconsistencia(TipoInconsistencia.DATOS);
				linea.getArrayCampos().get(posicion).setDescripcionIncon("El campo "+linea.getArrayCampos().get(posicion).getDescripcion()+" (parametrizacion institucion) sobre pasa el limite (3 caracteres)");
			}
		}
	}
	
	/**
	 * Método para buscar el codigo del centro de atencion contable de la parametrización
	 * @param linea
	 * @param parametrizacion
	 * @param posicion
	 */
	private static void asignarCodigoCentroAtencionContable(DtoInterfazLineaS1E linea,DtoInterfazS1EInfo parametrizacion,int posicion, String centroAtencionContable)
	{
		if(!centroAtencionContable.equals("")) {
			linea.getArrayCampos().get(posicion).setValor(centroAtencionContable);
		} else {	
			linea.getArrayCampos().get(posicion).setValor(parametrizacion.getCodigoCentroAtencionContable());
			if(UtilidadTexto.isEmpty(linea.getArrayCampos().get(posicion).getValor()))
			{
				//Con reportar la línea de inicio es suficiente
				if(!parametrizacion.isHuboInconsistenciaCentroAtencionContable())
				{
					parametrizacion.setHuboInconsistenciaCentroAtencionContable(true);
					linea.setExisteInconsistencia(true);
					linea.getArrayCampos().get(posicion).setInconsistencia(true);
					linea.getArrayCampos().get(posicion).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
					linea.getArrayCampos().get(posicion).setConsecutivoDocumento(linea.getArrayDocumentos().get(0).getConsecutivoDocumento());
					linea.getArrayCampos().get(posicion).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
					linea.getArrayCampos().get(posicion).setTipoInconsistencia(TipoInconsistencia.DATOS);
					linea.getArrayCampos().get(posicion).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+linea.getArrayCampos().get(posicion).getDescripcion()+". Funcionalidad parametrizacion interfaz (Centro Atencion Contable Administrativo).");
				}
			}
			else if(linea.getArrayCampos().get(posicion).getValor().length()>3)
			{
				//Si el campo supera el tamaño se trunca
				linea.getArrayCampos().get(posicion).setValor(linea.getArrayCampos().get(posicion).getValor().substring(0, 3));
			}
		}
	}
	
	/**
	 * Método para obtener el indicador de tipo de documento
	 * @param linea
	 * @param parametrizacion
	 * @param posicion
	 * @param codigoTipoDocumento
	 */
	private static void asignarIndTipoDocumento(DtoInterfazLineaS1E linea,DtoInterfazS1EInfo parametrizacion,int posicion,String codigoTipoDocumento,String acronimoTipoMovimiento)
	{
		String indTipoDocumento = "";
		//Se busca el indicativo del tipo de documento que aplica para el tipo de documento
		if(linea.getArrayDocumentos().get(0).getNumeroDocumento().equals("10"))
		{
			logger.info("codigoTipoDocumento inicial: "+codigoTipoDocumento);
			logger.info("acronimoTipoMovimiento inicial: "+acronimoTipoMovimiento);
		}
		for(DtoTiposInterfazDocumentosParam1E tipo:parametrizacion.getArrayListDtoTiposDocumentos())
		{
			if(linea.getArrayDocumentos().get(0).getNumeroDocumento().equals("10"))
			{
				logger.info("codigoTipoDocumento: "+tipo.getTipoDocumento());
				logger.info("acronimoTipoMovimiento: "+tipo.getTipoMovimiento());
			}
			
			if(tipo.getTipoDocumento().equals(codigoTipoDocumento)&&(acronimoTipoMovimiento.equals("")||acronimoTipoMovimiento.equals(tipo.getTipoMovimiento())))
			{
				if(linea.getArrayDocumentos().get(0).getNumeroDocumento().equals("10"))
				{
					logger.info("indicador de tipo documento===> "+tipo.getIndTipoDocumento());
				}
				indTipoDocumento = tipo.getIndTipoDocumento();
			}
		}
		linea.getArrayCampos().get(posicion).setValor(indTipoDocumento);
		if(UtilidadTexto.isEmpty(linea.getArrayCampos().get(posicion).getValor()))
		{
			//Con reportar la línea de inicio es suficiente
			if(!parametrizacion.isHuboInconsistenciaIndTipoDocumento(codigoTipoDocumento,acronimoTipoMovimiento))
			{
				parametrizacion.setHuboInconsistenciaIndTipoDocumento(codigoTipoDocumento,acronimoTipoMovimiento);
				logger.info("inconsistencia ================================================="+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+":  "+linea.getArrayDocumentos().get(0).getNumeroDocumento()+":  "+linea.getArrayDocumentos().get(0).getConsecutivoDocumento());
				linea.setExisteInconsistencia(true);
				linea.getArrayCampos().get(posicion).setInconsistencia(true);
				linea.getArrayCampos().get(posicion).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
				linea.getArrayCampos().get(posicion).setConsecutivoDocumento(linea.getArrayDocumentos().get(0).getConsecutivoDocumento());
				linea.getArrayCampos().get(posicion).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
				linea.getArrayCampos().get(posicion).setTipoInconsistencia(TipoInconsistencia.DATOS);
				linea.getArrayCampos().get(posicion).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+linea.getArrayCampos().get(posicion).getDescripcion()+". Funcionalidad  parametrizacion interfaz.");
			}
		}
		else if(linea.getArrayCampos().get(posicion).getValor().length()>3)
		{
			//Si el campo supera el tamaño se trunca
			linea.getArrayCampos().get(posicion).setValor(linea.getArrayCampos().get(posicion).getValor().substring(0, 3));
		}
	}
	
	/**
	 * Método para asignar el número de documento según parametrización del tipo de consecutivo
	 * @param linea
	 * @param parametrizacion
	 * @param posicion
	 * @param codigoTipoDocumento
	 */
	private static void asignarNumeroDocumento(DtoInterfazLineaS1E linea,DtoInterfazS1EInfo parametrizacion,int posicion,String codigoTipoDocumento,String acronimoTipoMovimiento, String tipoMovimiento)
	{
		String codigoTipoConsecutivo = "";
		///Se busca el indicativo del tipo de documento que aplica para el tipo de documento
		for(DtoTiposInterfazDocumentosParam1E tipo:parametrizacion.getArrayListDtoTiposDocumentos())
		{
			if(tipo.getTipoDocumento().equals(codigoTipoDocumento)&&(acronimoTipoMovimiento.equals("")||acronimoTipoMovimiento.equals(tipo.getTipoMovimiento())))
			{
				codigoTipoConsecutivo = tipo.getTipoConsecutivo();
			}
		}
		
		
		
		if(UtilidadTexto.isEmpty(codigoTipoConsecutivo))
		{
			//Con reportar la línea de inicio es suficiente
			if(!parametrizacion.isHuboInconsistenciaTipoConsecutivo(codigoTipoDocumento,acronimoTipoMovimiento))
			{
				parametrizacion.setHuboInconsistenciaTipoConsecutivo(codigoTipoDocumento,acronimoTipoMovimiento);
				logger.info("inconsistencia ==================================00");
				linea.setExisteInconsistencia(true);
				linea.getArrayCampos().get(posicion).setInconsistencia(true);
				linea.getArrayCampos().get(posicion).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
				linea.getArrayCampos().get(posicion).setConsecutivoDocumento(linea.getArrayDocumentos().get(0).getConsecutivoDocumento());
				linea.getArrayCampos().get(posicion).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
				linea.getArrayCampos().get(posicion).setTipoInconsistencia(TipoInconsistencia.DATOS);
				linea.getArrayCampos().get(posicion).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+linea.getArrayCampos().get(posicion).getDescripcion()+". Funcionalidad parametrizacion interfaz.");
			}
		}
		else 
		{
			boolean exito = false;
			for(DtoInterfazDatosDocumentoS1E datosDocumento:linea.getArrayDocumentos())
			{
				if(datosDocumento.getCodigoTipoConsecutivo().equals(codigoTipoConsecutivo))
				{
					if (ConstantesIntegridadDominio.acronimoTipoMovRecaudos.equals(tipoMovimiento)) {
						linea.getArrayCampos().get(posicion).setValor(datosDocumento.getConsecutivoDocumento());
					} else {
						linea.getArrayCampos().get(posicion).setValor(datosDocumento.getNumeroDocumento());
					}
					
					exito = true;
				}
			}
			
			if(!exito)
			{
				linea.setExisteInconsistencia(true);
				logger.info("inconsistencia ==================================00");
				linea.getArrayCampos().get(posicion).setInconsistencia(true);
				linea.getArrayCampos().get(posicion).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
				linea.getArrayCampos().get(posicion).setConsecutivoDocumento(linea.getArrayDocumentos().get(0).getConsecutivoDocumento());
				linea.getArrayCampos().get(posicion).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
				linea.getArrayCampos().get(posicion).setTipoInconsistencia(TipoInconsistencia.DATOS);
				linea.getArrayCampos().get(posicion).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+linea.getArrayCampos().get(posicion).getDescripcion()+".");
			}
		}
	}
	
	/**
	 * Método para asignar el Auxiliar de centre de costo
	 * @param linea
	 * @param parametrizacion
	 * @param posicion
	 * @param codigoTipoDocumento
	 */
	private static String asignarAuxiliarCentroCosto(
			Connection con,
			String reciboCaja)
	{
		String resultado = "";
		try
		{
			PreparedStatementDecorator pstMov = new PreparedStatementDecorator(con.prepareStatement(consultarAuxCentroCosto, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pstMov.setString(1,reciboCaja);
			
			ResultSetDecorator rsMov = new ResultSetDecorator(pstMov.executeQuery());
			
			if(rsMov.next())
			{
				resultado = rsMov.getString("codigo_centro_costo").toString();
			}
			rsMov.close();
			pstMov.close();
		}
		catch (Exception e) {
			logger.info("error asignarAuxiliarCentroCosto "+e);
		}
		
		return resultado;
	}
	
	/**
	 * @param Connection con
	 * @param DtoInterfazLineaS1E linea
	 * @param DtoInterfazS1EInfo parametrizacion
	 * */
	private static ArrayList<DtoInterfazLineaS1E> generarLineaMovimientoRecaudo(
			Connection con,
			DtoInterfazLineaS1E linea,
			DtoInterfazS1EInfo parametrizacion)
	{
		ArrayList<DtoInterfazLineaS1E> array = new ArrayList<DtoInterfazLineaS1E>();
		ArrayList<DtoInterfazLineaS1E> arrayComision = new ArrayList<DtoInterfazLineaS1E>();
		ArrayList<DtoInterfazLineaS1E> arrayRetencion = new ArrayList<DtoInterfazLineaS1E>();
		ArrayList<DtoInterfazLineaS1E> lineasMovAgrupada = new ArrayList<DtoInterfazLineaS1E>();
		ResultSetDecorator rsMov = null;
		PreparedStatementDecorator pstMov = null;
		String reciboCaja = "",auxCentroCostos = "",beneficiario = "", cadenalog = "";
		boolean esAnulacion = false;
		
		if(linea.getArrayDocumentos().size() > 1)
		{
			reciboCaja = linea.getArrayDocumentos().get(1).getNumeroDocumento();
			esAnulacion = true;
		}
		else
		{
			reciboCaja = linea.getArrayDocumentos().get(0).getNumeroDocumento();
			esAnulacion = false;
		}
		
		try
		{
			if(linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteReciboCaja+"") || 
					linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteAnulaRecCaja+""))
			{
				cadenalog =  consultaDetMovContableRecaudosStr;
				pstMov = new PreparedStatementDecorator(con.prepareStatement(consultaDetMovContableRecaudosStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pstMov.setString(1,reciboCaja);
				pstMov.setString(2,reciboCaja);
				
				rsMov = new ResultSetDecorator(pstMov.executeQuery());
			}
			else
			{
				cadenalog = consultaDetMovContableRecaudosDevRcStr;
				pstMov = new PreparedStatementDecorator(con.prepareStatement(consultaDetMovContableRecaudosDevRcStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pstMov.setString(1,linea.getArrayDocumentos().get(0).getNumeroDocumento());
				//pstMov.setInt(2,Utilidades.convertirAEntero(ValoresPorDefecto.getFormaPagoEfectivo(Utilidades.convertirAEntero(parametrizacion.getInstitucion()))));
				pstMov.setString(2,linea.getArrayDocumentos().get(0).getNumeroDocumento());
				
				rsMov = new ResultSetDecorator(pstMov.executeQuery());
				
				logger.info("consulta: "+consultaDetMovContableRecaudosDevRcStr.replace("?","'"+ linea.getArrayDocumentos().get(0).getNumeroDocumento()+"'"));
			}
			
			while(rsMov.next())
			{
				DtoInterfazLineaS1E lineaMov = new DtoInterfazLineaS1E(GeneracionInterfaz1E.indicadorLineaDetMovDocumentoContable);
				//Se agregan dos nuevas líneas de movimiento para comisión y retención para formas de pago = TARJETA - MT 1453
				DtoInterfazLineaS1E lineaMovComision = new DtoInterfazLineaS1E(GeneracionInterfaz1E.indicadorLineaDetMovDocumentoContable);
				DtoInterfazLineaS1E lineaMovRetencion = new DtoInterfazLineaS1E(GeneracionInterfaz1E.indicadorLineaDetMovDocumentoContable);
				
				boolean generaLineaComision = true;
				boolean generaLineaRetencion = true;
				//**************VALIDACIÓN CAMPO NUMERO REGISTRO*******************************
				//lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_REGISTRO.getPosicion()).setValor(parametrizacion.getPosicion()+"");
				
				//***************VALIDACION CAMPO COMPAÑÍA****************************************
				lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.COMPANIA.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.COMPANIA.getPosicion()).getValor());
				lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.COMPANIA.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.COMPANIA.getPosicion()).getValor());
				lineaMovRetencion.getArrayCampos().get(CampoMoviDocumentoContable.COMPANIA.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.COMPANIA.getPosicion()).getValor());
				
				//***************VALIDACION CAMPO CENTRO OPERACION****************************************
				lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPERACION.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getValor());
				lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPERACION.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getValor());
				lineaMovRetencion.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPERACION.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getValor());
				
				//**************VALIDACION CAMPO TIPO DE DOCUMENTO****************************************
				lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TIPO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.TIPO_DOCUMENTO.getPosicion()).getValor());
				lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.TIPO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.TIPO_DOCUMENTO.getPosicion()).getValor());
				lineaMovRetencion.getArrayCampos().get(CampoMoviDocumentoContable.TIPO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.TIPO_DOCUMENTO.getPosicion()).getValor());
				
				//*************VALIDACION CAMPO NÚMERO DE DOCUMENTO***************************************
				lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NRO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).getValor());
				lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.NRO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).getValor());
				lineaMovRetencion.getArrayCampos().get(CampoMoviDocumentoContable.NRO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).getValor());
				
				//*************VALIDACION CAMPO AUXILIAR CUENTA CONTABLE**********************************
				DtoCuentaContable cuentaContable = new DtoCuentaContable();
				DtoCuentaContable cuentaContableComision = new DtoCuentaContable();
				DtoCuentaContable cuentaContableReteFuente = new DtoCuentaContable();
				
				boolean esFormaPagoTarjeta = (rsMov.getInt("forma_pago") == ConstantesBD.codigoTipoDetalleFormasPagoTarjeta) ? true : false;
				
				if(rsMov.getInt("forma_pago")>0)
				{
						
					cuentaContable.setCuentaContable(rsMov.getString("cuenta_contable"));
					cuentaContable.setManejaTerceros(UtilidadTexto.getBoolean(rsMov.getString("manejo_terceros")));
					cuentaContable.setManejoCentrosCosto(UtilidadTexto.getBoolean(rsMov.getString("manejo_centro_costo")));
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setValor(cuentaContable.getCuentaContable());
					
					if(cuentaContable.getCuentaContable().equals(""))
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setConsecutivoDocumento(linea.getArrayDocumentos().get(0).getConsecutivoDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).getDescripcion()+" Inconsistencia de Datos Número de Documento:"+linea.getArrayDocumentos().get(0).getNumeroDocumento()+",Descripción Documento:"+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+".");
					}
					
					if(esFormaPagoTarjeta) {
						
						cuentaContableComision.setCuentaContable(rsMov.getString("cuenta_cont_comision"));
						cuentaContableComision.setManejaTerceros(UtilidadTexto.getBoolean(rsMov.getString("manejo_terceros_comision")));
						cuentaContableComision.setManejoCentrosCosto(UtilidadTexto.getBoolean(rsMov.getString("manejo_centro_costo_comision")));
						lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setValor(cuentaContableComision.getCuentaContable());
						
						cuentaContableReteFuente.setCuentaContable(rsMov.getString("cuenta_cont_retefuente"));
						cuentaContableReteFuente.setManejaTerceros(UtilidadTexto.getBoolean(rsMov.getString("manejo_terceros_retefuente")));
						cuentaContableReteFuente.setManejoCentrosCosto(false);
						lineaMovRetencion.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setValor(cuentaContableReteFuente.getCuentaContable());
						
						if(UtilidadTexto.isEmpty(cuentaContableComision.getCuentaContable())) {
							lineaMovComision.setExisteInconsistencia(true);
							lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setInconsistencia(true);
							lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
							lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setConsecutivoDocumento(linea.getArrayDocumentos().get(0).getConsecutivoDocumento());
							lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
							lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
							lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).getDescripcion()+" Comisión." +" Inconsistencia de Datos Número de Documento:"+linea.getArrayDocumentos().get(0).getNumeroDocumento()+",Descripción Documento:"+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+".");
						}
						
						if(UtilidadTexto.isEmpty(cuentaContableReteFuente.getCuentaContable())) {
							lineaMovRetencion.setExisteInconsistencia(true);
							lineaMovRetencion.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setInconsistencia(true);
							lineaMovRetencion.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
							lineaMovRetencion.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setConsecutivoDocumento(linea.getArrayDocumentos().get(0).getConsecutivoDocumento());
							lineaMovRetencion.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
							lineaMovRetencion.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
							lineaMovRetencion.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMovRetencion.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).getDescripcion()+" Retención."+" Inconsistencia de Datos Número de Documento:"+linea.getArrayDocumentos().get(0).getNumeroDocumento()+",Descripción Documento:"+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+".");
						}
					}
				}
				else
				{	
					if(rsMov.getInt("codigo_tipo_ingreso") == ConstantesBD.codigoTipoIngresoTesoreriaNinguno)
					{
						cuentaContable.setCuentaContable(rsMov.getString("cuenta_contable"));
						cuentaContable.setManejaTerceros(UtilidadTexto.getBoolean(rsMov.getString("manejo_terceros")));
						cuentaContable.setManejoCentrosCosto(UtilidadTexto.getBoolean(rsMov.getString("manejo_centro_costo")));
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setValor(cuentaContable.getCuentaContable());
					}
					
					
					if(rsMov.getInt("codigo_tipo_ingreso") == ConstantesBD.codigoTipoIngresoTesoreriaNinguno )
					{
						if((cuentaContable.getCuentaContable().equals("")))
						{
							lineaMov.setExisteInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setConsecutivoDocumento(linea.getArrayDocumentos().get(0).getConsecutivoDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).getDescripcion()+" Inconsistencia de Datos Número de Documento:"+linea.getArrayDocumentos().get(0).getNumeroDocumento()+",Descripción Documento:"+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+"."); 
						}
					}
				}
				
				//*************VALIDACION CAMPO TERCERO**********************************
				
				if(!esFormaPagoTarjeta) {
					
					if(cuentaContable.isManejaTerceros()) {
						
						if(!linea.getArrayCampos().get(CampoDocumentoContable.TERCERO_DOCUMENTO.getPosicion()).getValor().equals(""))
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.TERCERO_DOCUMENTO.getPosicion()).getValor());
						else
						{
							lineaMov.setExisteInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setConsecutivoDocumento(linea.getArrayDocumentos().get(0).getConsecutivoDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).getDescripcion()+" Inconsistencia de Datos Número de Documento:"+linea.getArrayDocumentos().get(0).getNumeroDocumento()+",Descripción Documento:"+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+".");
						}

					} else	{
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setValor("");
					}
				} else {
					/**
					 *  El tercero se toma de la entidad financiera que se registra en la información de la tarjeta, 
					 *  en el detalle de la forma de pago. Se toma el tercero asociado a esta entidad financiera en la 
					 *  parametrización de 'Entidades Financieras' 
					 */
					
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setValor("");
					
					if(cuentaContableComision.isManejaTerceros()) {
						
						if(!UtilidadTexto.isEmpty(rsMov.getString("tercero_tarjeta"))) {
							lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setValor(rsMov.getString("tercero_tarjeta"));
						} else {
							lineaMovComision.setExisteInconsistencia(true);
							lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setInconsistencia(true);
							lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
							lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setConsecutivoDocumento(linea.getArrayDocumentos().get(0).getConsecutivoDocumento());
							lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
							lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
							lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).getDescripcion()+" Inconsistencia de Datos Número de Documento:"+linea.getArrayDocumentos().get(0).getNumeroDocumento()+",Descripción Documento:"+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+".");
						}
					} else	{
						lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setValor("");
					}
					
					if(cuentaContableReteFuente.isManejaTerceros()) {
						
						if(!UtilidadTexto.isEmpty(rsMov.getString("tercero_tarjeta"))) {
							lineaMovRetencion.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setValor(rsMov.getString("tercero_tarjeta"));
						} else {
							lineaMovRetencion.setExisteInconsistencia(true);
							lineaMovRetencion.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setInconsistencia(true);
							lineaMovRetencion.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
							lineaMovRetencion.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setConsecutivoDocumento(linea.getArrayDocumentos().get(0).getConsecutivoDocumento());
							lineaMovRetencion.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
							lineaMovRetencion.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
							lineaMovRetencion.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMovRetencion.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).getDescripcion()+" Inconsistencia de Datos Número de Documento:"+linea.getArrayDocumentos().get(0).getNumeroDocumento()+",Descripción Documento:"+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+".");
						}
					}  else	{
						lineaMovRetencion.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setValor("");
					}

				}
			
				//*************VALIDACION CAMPO CENTRO OPERACION MOVIMIENTO**********************************
				/**Nota: tanto para formas de pago como conceptos de tipo Ninguno o Abono se toma el centro de operacion de la parametrizacion**/
				asignarCodigoCentroAtencionContable(lineaMov, parametrizacion, CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion(), "");
				
				if(esFormaPagoTarjeta) {

					asignarCodigoCentroAtencionContable(lineaMovRetencion, parametrizacion, CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion(), "");

					//Para la comisión se toma el centro de atención asociado al recibo de caja. (Centro de atención en el cual se genera)
					if(!UtilidadTexto.isEmpty(rsMov.getString("centro_atencion_recibo"))) {
						lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).setValor(rsMov.getString("centro_atencion_recibo"));
						if(lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).getValor().length() > 3) {
							//Si el campo supera el tamaño se trunca
							lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).setValor(lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).getValor().substring(0, 3));
						}
					} else {
						if(!parametrizacion.isHuboInconsistenciaCentroAtencionContable()) {
							parametrizacion.setHuboInconsistenciaCentroAtencionContable(true);
							lineaMovComision.setExisteInconsistencia(true);
							lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).setInconsistencia(true);
							lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
							lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).setConsecutivoDocumento(linea.getArrayDocumentos().get(0).getConsecutivoDocumento());
							lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
							lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
							lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).getDescripcion()+". Funcionalidad parametrizacion interfaz (Centro Atencion Contable Administrativo).");
						}
					}
				}
				//*************VALIDACION CAMPO UNIDAD DE NEGOCIO**********************************
				//TODO Validar esto
				asignarUnidadFuncionalEstandar(lineaMov,parametrizacion,CampoMoviDocumentoContable.UNIDAD_NEGOCIO.getPosicion(),linea,"");
				asignarUnidadFuncionalEstandarParamGeneral(lineaMovComision,parametrizacion,CampoMoviDocumentoContable.UNIDAD_NEGOCIO.getPosicion(),linea,"");
				asignarUnidadFuncionalEstandarParamGeneral(lineaMovRetencion,parametrizacion,CampoMoviDocumentoContable.UNIDAD_NEGOCIO.getPosicion(),linea,"");
				//*************VALIDACION CAMPO AUXILIAR DE CENTRO DE COSTOS**********************************
				
				if(!esFormaPagoTarjeta) {

					if(auxCentroCostos.equals(""))
						auxCentroCostos = asignarAuxiliarCentroCosto(con, reciboCaja);

					if(cuentaContable.isManejoCentrosCosto() && 
							!lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).getValor().equals(""))
					{
						if(rsMov.getInt("forma_pago") > 0)
						{
							if(!auxCentroCostos.equals(""))
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setValor(auxCentroCostos);
							else
							{
								lineaMov.setExisteInconsistencia(true);
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setInconsistencia(true);
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setConsecutivoDocumento(linea.getArrayDocumentos().get(0).getConsecutivoDocumento());
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).getDescripcion()+" Inconsistencia de Datos Número de Documento:"+linea.getArrayDocumentos().get(0).getNumeroDocumento()+",Descripción Documento:"+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+".");
							}
						}
						else
						{
							if(!rsMov.getString("codigo_centro_costo").equals(""))
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setValor(rsMov.getString("codigo_centro_costo"));
							else
							{
								lineaMov.setExisteInconsistencia(true);
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setInconsistencia(true);
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setConsecutivoDocumento(linea.getArrayDocumentos().get(0).getConsecutivoDocumento());
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).getDescripcion()+" Inconsistencia de Datos Número de Documento:"+linea.getArrayDocumentos().get(0).getNumeroDocumento()+",Descripción Documento:"+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+".");
							}
						}
					}
					else
					{
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setValor("");
					}

				} else {
					
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setValor("");
					lineaMovRetencion.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setValor("");
					
					if(cuentaContableComision.isManejoCentrosCosto()) {
						asignarAuxCentroCostosParametrizacion(lineaMovComision, parametrizacion, CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion());
					}
				}

				//*************VALIDACION CAMPO AUXILIAR DE CONCEPTO DE FLUJO DE EFECTIVO**********************************
				if(!parametrizacion.getCodConFlEfeMovCon().equals(""))
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CONCEP_FLUJO_EFE.getPosicion()).setValor(parametrizacion.getCodConFlEfeMovCon());
				else
				{
					if(!parametrizacion.isHuboInconsistenciaCodConFlEfeMovCon())
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CONCEP_FLUJO_EFE.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CONCEP_FLUJO_EFE.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CONCEP_FLUJO_EFE.getPosicion()).setConsecutivoDocumento(linea.getArrayDocumentos().get(0).getConsecutivoDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CONCEP_FLUJO_EFE.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CONCEP_FLUJO_EFE.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CONCEP_FLUJO_EFE.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CONCEP_FLUJO_EFE.getPosicion()).getDescripcion()+". Funcionalidad parametrizacion interfaz. ");
						parametrizacion.setHuboInconsistenciaCodConFlEfeMovCon(true);
					}
				}

				lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CONCEP_FLUJO_EFE.getPosicion()).setValor("");
				lineaMovRetencion.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CONCEP_FLUJO_EFE.getPosicion()).setValor("");
				//*************VALIDACION CAMPO VALOR DEBITO**********************************
				if(linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteReciboCaja+"") || 
						linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteAnulaRecCaja+""))
				{
					if(!esAnulacion)
					{
						
						if(!esFormaPagoTarjeta) {

							if(rsMov.getInt("forma_pago") > 0) {
								
								linea.setSumaDebito(linea.getSumaDebito() + rsMov.getDouble("valor"));
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rsMov.getDouble("valor"),"0.0000"));
								
							}
							else {
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor("0");
							}
						} else {
							
							double valorComision  = 0D;
							double valorRetencion = 0D;
							double valorDebito    = 0D;
							
							if (rsMov.getDouble("comision") > 0 ) {
								valorComision = Math.round((rsMov.getDouble("valor") * rsMov.getDouble("comision")) / 100D);
							} else {
								generaLineaComision = false;
							}
							if (rsMov.getDouble("retefte") > 0 ) {
								valorRetencion = Math.round((rsMov.getDouble("valor") * rsMov.getDouble("retefte")) / 100D);
							} else {
								generaLineaRetencion = false;
							}
							
							valorDebito = rsMov.getDouble("valor") - valorComision - valorRetencion;
							linea.setSumaDebito(linea.getSumaDebito() + rsMov.getDouble("valor"));
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(valorDebito,"0.0000"));
							lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(valorComision,"0.0000"));
							lineaMovRetencion.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(valorRetencion,"0.0000"));
						}
					}
					else
					{
						if(rsMov.getInt("forma_pago") < 0)
						{
							linea.setSumaDebito(linea.getSumaDebito()+rsMov.getDouble("valor"));
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rsMov.getDouble("valor"),"0.0000"));
						}
						else
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor("0");
					}
				}else if(linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteDevolRecibosCaja+""))
				{
					if(rsMov.getInt("forma_pago") > 0)
					{
						//se agrega este campo por que cuando es devolucion de recibo de caja no se esta almacenando el valor debito, probar bien.
						//NO DEBE IR POR QUE LO TOMARIA DOBLE
						//linea.setSumaDebito(linea.getSumaDebito()+rsMov.getDouble("valor"));
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor("0");
					}
					else
					{
						linea.setSumaDebito(linea.getSumaDebito()+rsMov.getDouble("valor"));
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rsMov.getDouble("valor"),"0.0000"));
					}	
				}

				//*************VALIDACION CAMPO VALOR CREDITO**********************************
				if(linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteReciboCaja+"") || 
						linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteAnulaRecCaja+""))
				{
					if(!esAnulacion)
					{
						if(rsMov.getInt("forma_pago") > 0)
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor("0");
						else
						{	
							linea.setSumaCredito(linea.getSumaCredito()+rsMov.getDouble("valor"));
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rsMov.getDouble("valor"),"0.0000"));
						}
					}
					else
					{
						if(rsMov.getInt("forma_pago") < 0)
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor("0");
						else
						{
							if (esFormaPagoTarjeta) {
								
								double valorComision  = 0D;
								double valorRetencion = 0D;
								double valorCredito   = 0D;
								
								if (rsMov.getDouble("comision") > 0 ) {
									valorComision = Math.round((rsMov.getDouble("valor") * rsMov.getDouble("comision")) / 100D);
								} else {
									generaLineaComision = false;
								}
								if (rsMov.getDouble("retefte") > 0 ) {
									valorRetencion = Math.round((rsMov.getDouble("valor") * rsMov.getDouble("retefte")) / 100D);
								} else {
									generaLineaRetencion = false;
								}
								
								valorCredito = rsMov.getDouble("valor") - valorComision - valorRetencion;
								linea.setSumaCredito(linea.getSumaCredito()+rsMov.getDouble("valor"));
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(valorCredito,"0.0000"));
								lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(valorComision,"0.0000"));
								lineaMovRetencion.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(valorRetencion,"0.0000"));
							} else {
								
								linea.setSumaCredito(linea.getSumaCredito()+rsMov.getDouble("valor"));
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rsMov.getDouble("valor"),"0.0000"));
								
							}
							
						}
					}
				}
				else if(linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteDevolRecibosCaja+""))
				{
					if(rsMov.getInt("forma_pago") > 0)
					{
						linea.setSumaCredito(linea.getSumaCredito()+rsMov.getDouble("valor"));
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rsMov.getDouble("valor"),"0.0000"));
					}
					else
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor("0");
				}

				//*************VALIDACION CAMPO VALOR BASE GRAVABLE*****************************
				lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_BASE_GRAVABLE.getPosicion()).setValor("0");
				lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_BASE_GRAVABLE.getPosicion()).setValor("0");
				lineaMovRetencion.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_BASE_GRAVABLE.getPosicion()).setValor("0");
				
				
				if (esFormaPagoTarjeta) {
					
					//*************VALIDACION CAMPO TIPO DE DOCUMENTO DE BANCO*****************************
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TIPO_DOCUMENTO_BANCO.getPosicion()).setValor("NC");
					lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.TIPO_DOCUMENTO_BANCO.getPosicion()).setValor("  ");
					lineaMovRetencion.getArrayCampos().get(CampoMoviDocumentoContable.TIPO_DOCUMENTO_BANCO.getPosicion()).setValor("  ");
					
					//*************VALIDACION CAMPO TIPO DE DOCUMENTO DE BANCO*****************************
					if (!UtilidadTexto.isEmpty(rsMov.getString("numero_autorizacion"))) {
						
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_DOCUMENTO_BANCO.getPosicion()).setValor(rsMov.getString("numero_autorizacion"));
						if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_DOCUMENTO_BANCO.getPosicion()).getValor().length() > 8)	{
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_DOCUMENTO_BANCO.getPosicion()).setValor(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_DOCUMENTO_BANCO.getPosicion()).getValor().substring(0, 8));
						}
						
					} else	{
						
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_DOCUMENTO_BANCO.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_DOCUMENTO_BANCO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_DOCUMENTO_BANCO.getPosicion()).setConsecutivoDocumento(linea.getArrayDocumentos().get(0).getConsecutivoDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_DOCUMENTO_BANCO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_DOCUMENTO_BANCO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_DOCUMENTO_BANCO.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+"Tipo Validación de Banco: campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_DOCUMENTO_BANCO.getPosicion()).getDescripcion());
						
					}
					lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_DOCUMENTO_BANCO.getPosicion()).setValor("0");
					lineaMovRetencion.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_DOCUMENTO_BANCO.getPosicion()).setValor("0");
					
				}
				
				//*************VALIDACION CAMPO OBSERVACIONES MOVIMIENTOS*********************
				beneficiario = rsMov.getString("tipo_id_beneficiario")+" "+rsMov.getString("numero_id_beneficiario")+" "+rsMov.getString("nombre_beneficiario");
				beneficiario +=" "+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+" "+linea.getArrayDocumentos().get(0).getConsecutivoDocumento(); 
				
				if(!beneficiario.equals(""))
				{
					if(beneficiario.length() > 255 )
						beneficiario = beneficiario.substring(0,255);
					
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.OBSERVACIONES_MOVIMI.getPosicion()).setValor(beneficiario);
					lineaMovComision.getArrayCampos().get(CampoMoviDocumentoContable.OBSERVACIONES_MOVIMI.getPosicion()).setValor(beneficiario);
					lineaMovRetencion.getArrayCampos().get(CampoMoviDocumentoContable.OBSERVACIONES_MOVIMI.getPosicion()).setValor(beneficiario);
				}
				
				array.add(lineaMov);
				if (esFormaPagoTarjeta && generaLineaComision) {
					arrayComision.add(lineaMovComision);
				}
				if (esFormaPagoTarjeta && generaLineaRetencion) {
					arrayRetencion.add(lineaMovRetencion);
				}
				
				if(lineaMov.isExisteInconsistencia() || (esFormaPagoTarjeta && generaLineaComision && lineaMovComision.isExisteInconsistencia()) || (esFormaPagoTarjeta && generaLineaRetencion && lineaMovRetencion.isExisteInconsistencia()))
					linea.setExisteInconsistencia(true);
			}
			
			
			
			//logger.info("existe error en linea >> "+linea.isExisteInconsistencia());
			if(!huboLineaDetMov && array.size() > 0)
			{	
				huboLineaDetMov = true;
				logger.info("..:Genero "+array.size()+" de linea "+DtoInterfazCampoS1E.getDescripcionLinea(GeneracionInterfaz1E.indicadorLineaDetMovDocumentoContable));
			}
			
			//************AGRUPAR EL DETALLE**************************************************
			lineasMovAgrupada = agruparLineasMovimientoContable(array,parametrizacion);
			//**********************************************************************************
			
			for (DtoInterfazLineaS1E dtoInterfazLineaS1E : arrayComision) {
				lineasMovAgrupada.add(dtoInterfazLineaS1E);
				dtoInterfazLineaS1E.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_REGISTRO.getPosicion()).setValor(parametrizacion.getPosicion()+"");
				parametrizacion.setPosicion(parametrizacion.getPosicion()+1);
			}
			
			for (DtoInterfazLineaS1E dtoInterfazLineaS1E : arrayRetencion) {
				lineasMovAgrupada.add(dtoInterfazLineaS1E);
				dtoInterfazLineaS1E.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_REGISTRO.getPosicion()).setValor(parametrizacion.getPosicion()+"");
				parametrizacion.setPosicion(parametrizacion.getPosicion()+1);
			}
			
		}
		catch(SQLException e) {
			logger.error("Error en generarLineaMovimientoRecaudoErr: "+e+" >> "+cadenalog);
		} finally {
			UtilidadBD.cerrarObjetosPersistencia(pstMov, rsMov, null);
		}
		
		
		return lineasMovAgrupada;
		
	}
	
	/**
	 * Actualiza el registro como tomado para la generacion de la interfaz
	 * */
	private static void actualizarRegistroMarcado(Connection con,String tipoDocumento, String consecutivo)
	{
		String cadena = "";
		
		//Recaudos
		if(tipoDocumento.equals(ConstantesBD.codigoTipoDocInteReciboCaja+""))
			cadena = marcarRecibosdeCaja;
		if(tipoDocumento.equals(ConstantesBD.codigoTipoDocInteAnulaRecCaja+""))
			cadena = marcarAnulacionRecibosdeCaja;
		if(tipoDocumento.equals(ConstantesBD.codigoTipoDocInteDevolRecibosCaja+""))
			cadena = marcarDevolucionRecibosdeCaja;		
		//Entidades externas
		if(tipoDocumento.equals(ConstantesBD.codigoTipoDocInteAutoServicioEntSub+""))
			cadena = marcarRegAutorSerEntidadesSub;		
		if(tipoDocumento.equals(ConstantesBD.codigoTipoDocInteAnulaAutorServicioEntSub+""))			
			cadena = marcarRegAnulacionAutorSerEntidadesSub;
		if(tipoDocumento.equals(ConstantesBD.codigoTipoDocInteDespachoMed+""))
			cadena = marcarDespachoMedicamentos;
		if(tipoDocumento.equals(ConstantesBD.codigoTipoDocInteDevolucionMedi+""))
			cadena = marcarDevolucionMedicamentos;
		if(tipoDocumento.equals(ConstantesBD.codigoTipoDocInteDespaPedidoInsumo+""))
			cadena = marcarDespachoPedidos;
		if(tipoDocumento.equals(ConstantesBD.codigoTipoDocInteDevolPedidoInsumo+""))
			cadena = marcarDevolucionPedidos;
		if(tipoDocumento.equals(ConstantesBD.codigoTipoDocInteDespachoPedidoQx+""))
			cadena = marcarDespachoPedidos;
		if(tipoDocumento.equals(ConstantesBD.codigoTipoDocInteDevolucionPedidoQx+""))
			cadena = marcarDevolucionPedidos;
		if(tipoDocumento.equals(ConstantesBD.codigoTipoDocInteCargosDirectosArt+""))
			cadena = marcarCargosDirectosArticulos;
		if(tipoDocumento.equals(ConstantesBD.codigoTipoDocInteAnulaCargosDirectosArticulo+""))
			cadena = marcarAnulacionCargosArticulos;
		
		//Valores y Honorarios
		if(tipoDocumento.equals(ConstantesBD.codigoTipoDocInteFacturaPaciente+""))
			cadena = marcarFacturasPacientes;
		if(tipoDocumento.equals(ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente+""))
			cadena = marcarAnulacionFacturasPacientes;
		if(tipoDocumento.equals(ConstantesBD.codigoTipoDocInteFacturasVarias+""))
			cadena = marcarFacturasVarias;
		if(tipoDocumento.equals(ConstantesBD.codigoTipoDocInteAnulaFacturasVarias+""))
			cadena = marcarAnulaFacturasVarias;
		if(tipoDocumento.equals(ConstantesBD.codigoTipoDocInteAjusFacturasVarias+""))
			cadena = marcarAjustesFacturasVarias;
		if(tipoDocumento.equals(ConstantesBD.codigoTipoDocInteCCCapitacion+""))
			cadena = marcarCuentasCobroCapitacion;
		if(tipoDocumento.equals(ConstantesBD.codigoTipoDocInteAjustesCCCapitacion+""))
			cadena = marcarAjustesCuentasCobroCapitacion;
		
		//Ajustes
		if(tipoDocumento.equals(ConstantesBD.codigoTipoDocInteAjustesFactPaciente+""))
			cadena = marcarAjustesFacturasPaciente;
		
		
		if(!cadena.equals(""))
		{
			try
			{
				PreparedStatementDecorator pstMov = new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				if(tipoDocumento.equals(ConstantesBD.codigoTipoDocInteReciboCaja+"") || 
						tipoDocumento.equals(ConstantesBD.codigoTipoDocInteAnulaRecCaja+"") ||
							tipoDocumento.equals(ConstantesBD.codigoTipoDocInteAutoServicioEntSub+"") ||
								tipoDocumento.equals(ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente+"")||
									tipoDocumento.equals(ConstantesBD.codigoTipoDocInteAjustesFactPaciente+""))
					pstMov.setString(1,consecutivo);
				else
					pstMov.setInt(1,Utilidades.convertirAEntero(consecutivo));
				
				if(pstMov.executeUpdate()<0)
					logger.info("error no se actualizo el documento >> "+tipoDocumento+" valor >>  "+consecutivo+" sql >> "+cadena);
				
				pstMov.close();
			}
			catch (Exception e) {
				logger.info("error en marcar documento >> "+tipoDocumento+" valor >>  "+consecutivo+" sql >> "+cadena);
			}
		}
	}
	
	
	
	/**
	 * @param Connection con
	 * @param DtoInterfazLineaS1E linea
	 * @param DtoInterfazS1EInfo parametrizacion
	 * */
	private static ArrayList<DtoInterfazLineaS1E> generarLineaMovimientoAjusteCIngCapitacion(
			Connection con,
			DtoInterfazLineaS1E linea,
			DtoInterfazS1EInfo parametrizacion)
	{	
		ArrayList<DtoInterfazLineaS1E> array = new ArrayList<DtoInterfazLineaS1E>();
		ArrayList<DtoInterfazLineaS1E> lineasMovAgrupada = new ArrayList<DtoInterfazLineaS1E>();
		String tipoAjuste = "",observaciones = "";
		
		try
		{
			PreparedStatementDecorator pstMov = new PreparedStatementDecorator(con,consultaDetAjusteFact03Str);
			pstMov.setString(1,linea.getArrayDocumentos().get(2).getNumeroDocumento());
			pstMov.setLong(2,Long.parseLong(linea.getArrayDocumentos().get(1).getNumeroDocumento()));
			
			
			ResultSetDecorator rsMov = new ResultSetDecorator(pstMov.executeQuery());
			
			logger.info(pstMov);
			
			while(rsMov.next())
			{
				int numeroRegistros = 0;
				
				//Si hay valor x cobrar convenio se suma
				if(rsMov.getDouble("valor_cobrar_convenio")>0)
				{
					numeroRegistros++;
				}
				//Si hay valor a favor convenio se suma
				if(rsMov.getDouble("valor_favor_convenio")>0)
				{
					numeroRegistros++;
				}
				for(int i=0;i<numeroRegistros;i++)
				{
				
						DtoInterfazLineaS1E lineaMov = new DtoInterfazLineaS1E(GeneracionInterfaz1E.indicadorLineaDetMovDocumentoContable);
						//**************VALIDACIÓN CAMPO NUMERO REGISTRO*******************************
						//lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_REGISTRO.getPosicion()).setValor(parametrizacion.getPosicion()+"");
						//***************VALIDACION CAMPO COMPAÑÍA****************************************
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.COMPANIA.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.COMPANIA.getPosicion()).getValor());
						//***************VALIDACION CAMPO CENTRO OPERACION****************************************
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPERACION.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getValor());
						//**************VALIDACION CAMPO TIPO DE DOCUMENTO****************************************
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TIPO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.TIPO_DOCUMENTO.getPosicion()).getValor());
						//*************VALIDACION CAMPO NÚMERO DE DOCUMENTO***************************************
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NRO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).getValor());
						//*************VALIDACION CAMPO AUXILIAR CUENTA CONTABLE**********************************
						DtoCuentaContable cuentaContable = new DtoCuentaContable();
						
						int codigoConvenio = rsMov.getInt("codigo_convenio");
						String codigoTipoRegimen = rsMov.getString("codigo_tipo_regimen");
						String codigoTipoConvenio = rsMov.getString("codigo_tipo_convenio");
						int codigoInstitucion = rsMov.getInt("codigo_institucion");
							
						//Cuenta x cobrar convenio
						if(i==0)
						{
							cuentaContable = UtilidadesInterfaz.consultarCuentaContableInterfazConvenio(con, codigoConvenio, codigoTipoRegimen, ConstantesBD.tipoCuentaConvenio, true, false);								
							if(cuentaContable.getCuentaContable().equals(""))
							{
								cuentaContable = UtilidadesFacturacion.consultarCuentaContableTipoConvenio(con, codigoTipoConvenio, codigoInstitucion, true, false, false, false, false);
							}
							
							cuentaContable.setMensaje("Cuenta por Cobrar Convenio");
						}
						//Cuenta valor a favor convenio
						else if(i==1)
						{
							cuentaContable = UtilidadesInterfaz.consultarCuentaContableInterfazConvenio(con, codigoConvenio, codigoTipoRegimen, ConstantesBD.tipoCuentaFavorConvenio, true, false);
							if(cuentaContable.getCuentaContable().equals(""))
							{
								cuentaContable = UtilidadesFacturacion.consultarCuentaContableTipoConvenio(con, codigoTipoConvenio, codigoInstitucion, false, true, false, false, false);
							}
							cuentaContable.setMensaje("Cuenta valor a favor Convenio");
						}
						
						
						
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setValor(cuentaContable.getCuentaContable());
						if(cuentaContable.getCuentaContable().equals(""))
						{
							lineaMov.setExisteInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).getDescripcion()+". [ "+cuentaContable.getMensaje()+" ] ");
						}
						
						//*************VALIDACION CAMPO TERCERO**********************************
						if(cuentaContable.isManejaTerceros())
						{
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setValor(rsMov.getString("id_tercero"));	
							if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).getValor().equals(""))
							{
								lineaMov.setExisteInconsistencia(true);
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setInconsistencia(true);
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).getDescripcion());
							}
						}
						
						//*************VALIDACION CAMPO CENTRO OPERACION MOVIMIENTO**********************************
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).setValor(rsMov.getString("codigo_centro_at_cont"));
						 if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).getValor().length()>3)
						{
							//Si el campo supera el tamaño se trunca
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).setValor(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).getValor().substring(0, 3));
						}
						
						//*************VALIDACION CAMPO UNIDAD DE NEGOCIO**********************************
						asignarUnidadFuncionalEstandar(lineaMov,parametrizacion,CampoMoviDocumentoContable.UNIDAD_NEGOCIO.getPosicion(),linea,"");
						
						
						//*************VALIDACION CAMPO AUXILIAR DE CENTRO DE COSTOS**********************************
						if(cuentaContable.isManejoCentrosCosto())
						{
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setValor(rsMov.getString("cod_int_cc_contable"));
							if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).getValor().equals(""))
							{
								lineaMov.setExisteInconsistencia(true);
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setInconsistencia(true);
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).getDescripcion());
							}
							else if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).getValor().length()>15)
							{
								//Si el campo supera el tamaño se trunca
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setValor(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).getValor().substring(0, 15));
							}
						}
						
						//*************VALIDACION CAMPO VALOR DEBITO**********************************
						tipoAjuste = rsMov.getString("tipo_ajuste");
						if(rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteDebitoFactura+"") || 
								rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteDebitoCuentaCobro+"") || 
									rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoConceptosCarteraDebito+""))
						{
							//Valor cuenta x cobrar convenio
							if(i==0)
							{
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rsMov.getString("valor_cobrar_convenio"), "0.0000"));
								linea.setSumaDebito(linea.getSumaDebito()+rsMov.getDouble("valor_cobrar_convenio"));
								
							}
							//Valor cuenta a favor convenio
							else if(i==1)
							{
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor("0");
								
							}
						}
						else if(rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteCreditoFactura+"") || 
								rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteCreditoCuentaCobro+"") || 
									rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoConceptosCarteraCredito+""))
						{
							//Valor cuenta x cobrar convenio
							if(i==0)
							{
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor("0");
							}
							//Valor cuenta a favor convenio
							else if(i==1)
							{
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rsMov.getString("valor_favor_convenio"), "0.0000"));
								linea.setSumaDebito(linea.getSumaDebito()+rsMov.getDouble("valor_favor_convenio"));
							}
						}
						
						
						if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).getValor().equals(""))
						{
							lineaMov.setExisteInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setDescripcionIncon("Falta definir informacion para el campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).getDescripcion());
						}
						
						
						//*************VALIDACION CAMPO VALOR CREDITO**********************************
						
						
						if(rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteDebitoFactura+"") || 
								rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteDebitoCuentaCobro+"") || 
									rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoConceptosCarteraDebito+""))
						{
						
							//Valor cuenta x cobrar convenio
							if(i==0)
							{
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor("0");
							}
							//Valor cuenta a favor convenio
							else if(i==1)
							{
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rsMov.getString("valor_favor_convenio"), "0.0000"));
								linea.setSumaCredito(linea.getSumaCredito()+rsMov.getDouble("valor_favor_convenio"));
							}
						}
						else if(rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteCreditoFactura+"") || 
								rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteCreditoCuentaCobro+"") || 
									rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoConceptosCarteraCredito+""))
						{
							//Valor cuenta x cobrar convenio
							if(i==0)
							{
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rsMov.getString("valor_cobrar_convenio"), "0.0000"));
								linea.setSumaCredito(linea.getSumaCredito()+rsMov.getDouble("valor_cobrar_convenio"));
							}
							//Valor cuenta a favor convenio
							else if(i==1)
							{
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor("0");
								
							}
						}	
							
						
						
						if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).getValor().equals(""))
						{
							lineaMov.setExisteInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setDescripcionIncon("Falta definir informacion para el campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).getDescripcion());
						}
						
						
						//*************VALIDACION CAMPO OBSERVACIONES MOVIMIENTOS*********************
						observaciones = rsMov.getString("nombre_convenio")+" "+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+" "+linea.getArrayDocumentos().get(0).getNumeroDocumento(); 
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.OBSERVACIONES_MOVIMI.getPosicion()).setValor(observaciones);
						//**************************************************************************
							
						//parametrizacion.setPosicion(parametrizacion.getPosicion()+1);
						array.add(lineaMov);
						
						if(lineaMov.isExisteInconsistencia())
						{	
							linea.setExisteInconsistencia(true);
						}
					
				} //For numeroREgistros
			}//Fin while rs
			

			if(!huboLineaDetMov && array.size() > 0)
			{	
				huboLineaDetMov = true;
				logger.info("..:Genero "+array.size()+" de linea "+DtoInterfazCampoS1E.getDescripcionLinea(GeneracionInterfaz1E.indicadorLineaDetMovDocumentoContable));
			}
			rsMov.close();
			pstMov.close();
			
			//************AGRUPAR EL DETALLE**************************************************
			lineasMovAgrupada = agruparLineasMovimientoContable(array,parametrizacion);
			//**********************************************************************************
		}
		catch(SQLException e)
		{
			logger.error("Error en generarLineaMovimientoAjusteIngCapitacion: "+e+" >> ");
		}
		
		return lineasMovAgrupada;
	}
	
	/**
	 * @param Connection con
	 * @param DtoInterfazLineaS1E linea
	 * @param DtoInterfazS1EInfo parametrizacion
	 * */
	private static ArrayList<DtoInterfazLineaS1E> generarLineaMovimientoAjusteCAutoIng(
			Connection con,
			DtoInterfazLineaS1E linea,
			DtoInterfazS1EInfo parametrizacion,
			DtoRetencion retencion)
	{	
		ArrayList<DtoInterfazLineaS1E> array = new ArrayList<DtoInterfazLineaS1E>();
		ArrayList<DtoInterfazLineaS1E> lineasMovAgrupada = new ArrayList<DtoInterfazLineaS1E>();
		String tipoAjuste = "",observaciones = "";
		
		try
		{
			PreparedStatementDecorator pstMov = new PreparedStatementDecorator(con,consultaDetAjusteFactStr);
			pstMov.setString(1,linea.getArrayDocumentos().get(2).getNumeroDocumento());
			pstMov.setLong(2,Long.parseLong(linea.getArrayDocumentos().get(1).getNumeroDocumento()));
			pstMov.setString(3,linea.getArrayDocumentos().get(2).getNumeroDocumento());
			pstMov.setLong(4,Long.parseLong(linea.getArrayDocumentos().get(1).getNumeroDocumento()));
			pstMov.setString(5,linea.getArrayDocumentos().get(2).getNumeroDocumento());
			pstMov.setLong(6,Long.parseLong(linea.getArrayDocumentos().get(1).getNumeroDocumento()));
			
			ResultSetDecorator rsMov = new ResultSetDecorator(pstMov.executeQuery());
			
			logger.info(pstMov);
			
			while(rsMov.next())
			{
				
						DtoInterfazLineaS1E lineaMov = new DtoInterfazLineaS1E(GeneracionInterfaz1E.indicadorLineaDetMovDocumentoContable);
						//**************VALIDACIÓN CAMPO NUMERO REGISTRO*******************************
						//lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_REGISTRO.getPosicion()).setValor(parametrizacion.getPosicion()+"");
						//***************VALIDACION CAMPO COMPAÑÍA****************************************
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.COMPANIA.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.COMPANIA.getPosicion()).getValor());
						//***************VALIDACION CAMPO CENTRO OPERACION****************************************
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPERACION.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getValor());
						//**************VALIDACION CAMPO TIPO DE DOCUMENTO****************************************
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TIPO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.TIPO_DOCUMENTO.getPosicion()).getValor());
						//*************VALIDACION CAMPO NÚMERO DE DOCUMENTO***************************************
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NRO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).getValor());
						//*************VALIDACION CAMPO AUXILIAR CUENTA CONTABLE**********************************
						DtoCuentaContable cuentaContable = new DtoCuentaContable();
						cuentaContable.setCodigo(rsMov.getString("cuenta_contable"));
						cuentaContable = UtilidadesInterfaz.consultarDatosCuentaContable(con, cuentaContable);
		
						//Interna/Externa
						if(cuentaContable.getCuentaContable().equals(""))
						{
							//Según el tipo de solicitud
							switch(rsMov.getInt("tipo_solicitud"))
							{
								case ConstantesBD.codigoTipoSolicitudMedicamentos:
								case ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos:
									
									cuentaContable = UtilidadesInterfaz.consultarCuentaContableInterfazUnidadFuncional(con,rsMov.getInt("centro_costo_solicitante"), false, true, false, false, false);
									if(cuentaContable.getCuentaContable().equals(""))
										cuentaContable = UtilidadesInterfaz.consultarCuentaContableInterfazInvIngreso(con,rsMov.getInt("centro_costo_solicitante"), rsMov.getInt("servicio_articulo"), true, false);
									
									cuentaContable.setMensaje("Cuenta Ingreso");
									
								break;
								case ConstantesBD.codigoTipoSolicitudPaquetes:
									
									double utilidad = rsMov.getDouble("valor") - rsMov.getDouble("valor_consumo_paquete");
									cuentaContable = UtilidadesFacturacion.consultarCuentaContablePaquetexSolicitud(con, rsMov.getString("numero_solicitud"), utilidad>0?true:false, utilidad<0?true:false);
									
									cuentaContable.setMensaje("Cuenta Ingreso");
									
								break;
								default:
									
									cuentaContable = UtilidadesInterfaz.consultarCuentaContableInterfazUnidadFuncional(con, rsMov.getInt("centro_costo_solicitante"), true, false, false, false, false);
									if(cuentaContable.getCuentaContable().equals(""))
									{
										cuentaContable = UtilidadesInterfaz.consultarCuentaContableInterfazServicio(con, rsMov.getInt("servicio_articulo"), rsMov.getInt("centro_costo_solicitante"), true, false, false);
									}
									
									cuentaContable.setMensaje("Cuenta Ingreso");
									
								break;
							}
						}
						
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setValor(cuentaContable.getCuentaContable());
						if(cuentaContable.getCuentaContable().equals(""))
						{
							lineaMov.setExisteInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).getDescripcion()+". [ "+cuentaContable.getMensaje()+" ] ");
						}
						
						//*************VALIDACION CAMPO TERCERO**********************************
						if(cuentaContable.isManejaTerceros())
						{
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.TERCERO_DOCUMENTO.getPosicion()).getValor());	
							if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).getValor().equals(""))
							{
								lineaMov.setExisteInconsistencia(true);
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setInconsistencia(true);
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).getDescripcion());
							}
						}
						
						//*************VALIDACION CAMPO CENTRO OPERACION MOVIMIENTO**********************************
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getValor());
						
						//*************VALIDACION CAMPO UNIDAD DE NEGOCIO**********************************
						//Interna
						if(UtilidadTexto.getBoolean(rsMov.getString("tipo_factura_sistema")))
						{
							
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.UNIDAD_NEGOCIO.getPosicion()).setValor(rsMov.getString("uni_func_solicitado"));
							
						}
						else
						{
							asignarUnidadFuncionalEstandar(lineaMov,parametrizacion,CampoMoviDocumentoContable.UNIDAD_NEGOCIO.getPosicion(),linea,"");
						}
						
						//*************VALIDACION CAMPO AUXILIAR DE CENTRO DE COSTOS**********************************
						//Interna
						if(UtilidadTexto.getBoolean(rsMov.getString("tipo_factura_sistema")))
						{
							if(cuentaContable.isManejoCentrosCosto())
							{
								//Según el tipo de solicitud
								switch(rsMov.getInt("tipo_solicitud"))
								{
									case ConstantesBD.codigoTipoSolicitudMedicamentos:
									case ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos:
										lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setValor(rsMov.getString("cod_centro_costo_solicitante"));
									break;
									default:
										if(rsMov.getDouble("pool")>0 && rsMov.getDouble("valor_pool")>=0)
										{
											DtoEspecialidad especialidad = new DtoEspecialidad();
											
											especialidad.setCodigo(rsMov.getInt("especialidad_solicitada"));
											if(especialidad.getCodigo()>0)
											{
												UtilidadesHistoriaClinica.consultarEspecialidad(con, especialidad);
											}
											lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setValor(especialidad.getCentroCostoHonorarios().getCodigoInterfaz());
										}
										else
										{
											lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setValor(rsMov.getString("cod_centro_costo_solicitante"));
										}
									break;
								}
								
								if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).getValor().equals(""))
								{
									lineaMov.setExisteInconsistencia(true);
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setInconsistencia(true);
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).getDescripcion());
								}
								else if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).getValor().length()>15)
								{
									//Si el campo supera el tamaño se trunca
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setValor(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.UNIDAD_NEGOCIO.getPosicion()).getValor().substring(0, 15));
								}
							}
						}
						//*************VALIDACION CAMPO VALOR DEBITO**********************************
						//Interna
						if(UtilidadTexto.getBoolean(rsMov.getString("tipo_factura_sistema")))
						{
							tipoAjuste = rsMov.getString("tipo_ajuste"); 
							if(rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteDebitoFactura+"") || 
									rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteDebitoCuentaCobro+"") || 
										rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoConceptosCarteraDebito+""))
							{
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor("0");
							}
							else if(rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteCreditoFactura+"") || 
										rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteCreditoCuentaCobro+"") || 
											rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoConceptosCarteraCredito+""))
							{
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rsMov.getDouble("valor"),"0.0000"));
								linea.setSumaDebito(linea.getSumaDebito()+rsMov.getDouble("valor"));
							}
						}
						else
						{
							tipoAjuste = rsMov.getString("tipo_ajuste");
							if(rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteDebitoFactura+"") || 
									rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteDebitoCuentaCobro+"") || 
										rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoConceptosCarteraDebito+""))
							{
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor("0");
							}
							else if(rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteCreditoFactura+"") || 
									rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteCreditoCuentaCobro+"") || 
										rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoConceptosCarteraCredito+""))
							{
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rsMov.getDouble("valor"),"0.0000"));
								linea.setSumaDebito(linea.getSumaDebito()+rsMov.getDouble("valor"));
							}
						}
						
						if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).getValor().equals(""))
						{
							lineaMov.setExisteInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setDescripcionIncon("Falta definir informacion para el campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).getDescripcion());
						}
						
						
						//*************VALIDACION CAMPO VALOR CREDITO**********************************
						//Interna
						if(UtilidadTexto.getBoolean(rsMov.getString("tipo_factura_sistema")))
						{
							tipoAjuste = rsMov.getString("tipo_ajuste");
							if(rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteDebitoFactura+"") || 
									rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteDebitoCuentaCobro+"") || 
										rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoConceptosCarteraDebito+""))
							{
								
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rsMov.getDouble("valor"),"0.0000"));
								linea.setSumaCredito(linea.getSumaCredito()+rsMov.getDouble("valor"));
								
									
								
							}
							else if(rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteCreditoFactura+"") || 
									rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteCreditoCuentaCobro+"") || 
										rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoConceptosCarteraCredito+""))
							{
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor("0");
							}
						}
						else
						{
							tipoAjuste = rsMov.getString("tipo_ajuste");
							if(rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteDebitoFactura+"") || 
									rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteDebitoCuentaCobro+"") || 
										rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoConceptosCarteraDebito+""))
							{
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rsMov.getDouble("valor"),"0.0000"));
								linea.setSumaCredito(linea.getSumaCredito()+rsMov.getDouble("valor"));
							}
							else if(rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteCreditoFactura+"") || 
									rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteCreditoCuentaCobro+"") || 
										rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoConceptosCarteraCredito+""))
							{
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor("0");
							}
						}
						
						if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).getValor().equals(""))
						{
							lineaMov.setExisteInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setDescripcionIncon("Falta definir informacion para el campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).getDescripcion());
						}
						
						
						//*************VALIDACION CAMPO OBSERVACIONES MOVIMIENTOS*********************
						observaciones = rsMov.getString("paciente")+" "+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+" "+linea.getArrayDocumentos().get(0).getNumeroDocumento(); 
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.OBSERVACIONES_MOVIMI.getPosicion()).setValor(observaciones);
						//**************************************************************************
							
						//parametrizacion.setPosicion(parametrizacion.getPosicion()+1);
						array.add(lineaMov);
						
						if(lineaMov.isExisteInconsistencia())
						{	
							linea.setExisteInconsistencia(true);
						}
					
				
			}//Fin while rs
			
			//***************************CONCEPTOS DE AUTORRETENCION**********************************************
			for(int i=0;i<retencion.getConceptos().size();i++)
			{
				for(int j=0;j<2;j++)
				{
					DtoInterfazLineaS1E lineaMov = new DtoInterfazLineaS1E(GeneracionInterfaz1E.indicadorLineaDetMovDocumentoContable);
					//**************VALIDACIÓN CAMPO NUMERO REGISTRO*******************************
					//lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_REGISTRO.getPosicion()).setValor(parametrizacion.getPosicion()+"");
					//***************VALIDACION CAMPO COMPAÑÍA****************************************
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.COMPANIA.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.COMPANIA.getPosicion()).getValor());
					//***************VALIDACION CAMPO CENTRO OPERACION****************************************
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPERACION.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getValor());
					//**************VALIDACION CAMPO TIPO DE DOCUMENTO****************************************
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TIPO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.TIPO_DOCUMENTO.getPosicion()).getValor());
					//*************VALIDACION CAMPO NÚMERO DE DOCUMENTO***************************************
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NRO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).getValor());
					//*************VALIDACION CAMPO AUXILIAR CUENTA CONTABLE**********************************
					DtoCuentaContable cuentaContable = new DtoCuentaContable();
					
					//Debito
					if(j==0)
					{
						cuentaContable.setCodigo(retencion.getConceptos().get(i).getCuentaDBAutoretencion().getCodigo());
						cuentaContable.setMensaje("Cuenta Autoretencion Debito");
					}
					//Credito
					else
					{
						cuentaContable.setCodigo(retencion.getConceptos().get(i).getCuentaCRAutoretencion().getCodigo());
						cuentaContable.setMensaje("Cuenta Autoretencion Credito");
					}
					cuentaContable = UtilidadesInterfaz.consultarDatosCuentaContable(con, cuentaContable);
					
					
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setValor(cuentaContable.getCuentaContable());
					if(cuentaContable.getCuentaContable().equals(""))
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).getDescripcion()+". [ "+cuentaContable.getMensaje()+" ] ");
					}
					
					//*************VALIDACION CAMPO TERCERO**********************************
					if(cuentaContable.isManejaTerceros())
					{
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.TERCERO_DOCUMENTO.getPosicion()).getValor());	
						if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).getValor().equals(""))
						{
							lineaMov.setExisteInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).getDescripcion());
						}
					}
					//***************VALIDACION CAMPO CENTRO DE OPRACION DE MOVIMIENTO**************************+
					asignarCodigoCentroAtencionContable(lineaMov, parametrizacion, CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion(), "");
					
					///*************VALIDACION CAMPO UNIDAD DE NEGOCIO**********************************
					asignarUnidadFuncionalEstandar(lineaMov,parametrizacion,CampoMoviDocumentoContable.UNIDAD_NEGOCIO.getPosicion(),linea,"");
						
						
					
					//*************VALIDACION CAMPO VALOR DEBITO**********************************
					if(tipoAjuste.equals(ConstantesBD.codigoAjusteDebitoFactura+"") || 
							tipoAjuste.equals(ConstantesBD.codigoAjusteDebitoCuentaCobro+"") || 
								tipoAjuste.equals(ConstantesBD.codigoConceptosCarteraDebito+""))
					{
						
						//Debito
						if(j==0)
						{
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(retencion.getConceptos().get(i).getValorRetencion(),"0.0000"));
							linea.setSumaDebito(linea.getSumaDebito()+Utilidades.convertirADouble(retencion.getConceptos().get(i).getValorRetencion()));
						}
						//Credito
						else
						{
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor("0");
						}
						
					}
					else if(tipoAjuste.equals(ConstantesBD.codigoAjusteCreditoFactura+"") || 
								tipoAjuste.equals(ConstantesBD.codigoAjusteCreditoCuentaCobro+"") || 
									tipoAjuste.equals(ConstantesBD.codigoConceptosCarteraCredito+""))
					{
						//Debito
						if(j==0)
						{
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor("0");
							
						}
						//Credito
						else
						{
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(retencion.getConceptos().get(i).getValorRetencion(),"0.0000"));
							linea.setSumaDebito(linea.getSumaDebito()+Utilidades.convertirADouble(retencion.getConceptos().get(i).getValorRetencion()));
						}
					}
					
						
						
					if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).getValor().equals(""))
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setDescripcionIncon("Falta definir informacion para el campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).getDescripcion());
					}
						
						
					//*************VALIDACION CAMPO VALOR CREDITO**********************************
					if(tipoAjuste.equals(ConstantesBD.codigoAjusteDebitoFactura+"") || 
							tipoAjuste.equals(ConstantesBD.codigoAjusteDebitoCuentaCobro+"") || 
								tipoAjuste.equals(ConstantesBD.codigoConceptosCarteraDebito+""))
					{
						//Debito
						if(j==0)
						{
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor("0");
						}
						//Credito
						else
						{
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(retencion.getConceptos().get(i).getValorRetencion(),"0.0000"));
							linea.setSumaCredito(linea.getSumaCredito()+Utilidades.convertirADouble(retencion.getConceptos().get(i).getValorRetencion()));
						}
						
							
						
					}
					else if(tipoAjuste.equals(ConstantesBD.codigoAjusteCreditoFactura+"") || 
							tipoAjuste.equals(ConstantesBD.codigoAjusteCreditoCuentaCobro+"") || 
								tipoAjuste.equals(ConstantesBD.codigoConceptosCarteraCredito+""))
					{
						//Debito
						if(j==0)
						{
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(retencion.getConceptos().get(i).getValorRetencion(),"0.0000"));
							linea.setSumaCredito(linea.getSumaCredito()+Utilidades.convertirADouble(retencion.getConceptos().get(i).getValorRetencion()));
							
						}
						//Credito
						else
						{
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor("0");
						}
					}
						
						
					if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).getValor().equals(""))
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setDescripcionIncon("Falta definir informacion para el campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).getDescripcion());
					}
					
					//*************VALIDACION CAMPO VALOR BASE GRAVABLE*****************************
					
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_BASE_GRAVABLE.getPosicion()).setValor(retencion.getConceptos().get(i).getValorBaseGravable());
					
					
					//*************VALIDACION CAMPO OBSERVACIONES MOVIMIENTOS*********************
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.OBSERVACIONES_MOVIMI.getPosicion()).setValor(observaciones);
					//**************************************************************************
						
					//parametrizacion.setPosicion(parametrizacion.getPosicion()+1);
					array.add(lineaMov);
					
					if(lineaMov.isExisteInconsistencia())
					{	
						linea.setExisteInconsistencia(true);
					}
					
					
					
				} //Fin for Debito/credito
			} //fin for conceptos autorretencion
			//******************************************************************************************************

			if(!huboLineaDetMov && array.size() > 0)
			{	
				huboLineaDetMov = true;
				logger.info("..:Genero "+array.size()+" de linea "+DtoInterfazCampoS1E.getDescripcionLinea(GeneracionInterfaz1E.indicadorLineaDetMovDocumentoContable));
			}
			rsMov.close();
			pstMov.close();
			
			//************AGRUPAR EL DETALLE**************************************************
			lineasMovAgrupada = agruparLineasMovimientoContable(array,parametrizacion);
			//**********************************************************************************
		}
		catch(SQLException e)
		{
			logger.error("Error en generarLineaMovimientoAjusteCAutoIng: "+e+" >> ");
		}
		
		return lineasMovAgrupada;
	}
	

	/**
	 * @param retencion 
	 * @param Connection con
	 * @param DtoInterfazLineaS1E linea
	 * @param DtoInterfazS1EInfo parametrizacion
	 * */
	private static ArrayList<DtoInterfazLineaS1E> generarLineaMovimientoAjusteCHonoReten(
			Connection con,
			DtoInterfazLineaS1E linea,
			DtoInterfazS1EInfo parametrizacion)
	{
		ArrayList<DtoInterfazLineaS1E> array = new ArrayList<DtoInterfazLineaS1E>();
		ArrayList<DtoInterfazLineaS1E> lineasMovAgrupada = new ArrayList<DtoInterfazLineaS1E>();
		
		try
		{	
			PreparedStatementDecorator pstMov = new PreparedStatementDecorator(con,consultaDetAjusteFact02Str);
			pstMov.setString(1,linea.getArrayDocumentos().get(2).getNumeroDocumento());
			pstMov.setLong(2,Long.parseLong(linea.getArrayDocumentos().get(1).getNumeroDocumento()));
			pstMov.setString(3,linea.getArrayDocumentos().get(2).getNumeroDocumento());
			pstMov.setLong(4,Long.parseLong(linea.getArrayDocumentos().get(1).getNumeroDocumento()));
			
			logger.info(pstMov);
			
			ResultSetDecorator rsMov = new ResultSetDecorator(pstMov.executeQuery());
			
			while(rsMov.next())
			{
				
				
				{
					
					DtoInterfazLineaS1E lineaMov = new DtoInterfazLineaS1E(GeneracionInterfaz1E.indicadorLineaDetMovDocumentoContable);
					//**************VALIDACIÓN CAMPO NUMERO REGISTRO*******************************
					//lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_REGISTRO.getPosicion()).setValor(parametrizacion.getPosicion()+"");
					//***************VALIDACION CAMPO COMPAÑÍA****************************************
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.COMPANIA.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.COMPANIA.getPosicion()).getValor());
					//***************VALIDACION CAMPO CENTRO OPERACION****************************************
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPERACION.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getValor());
					//**************VALIDACION CAMPO TIPO DE DOCUMENTO****************************************
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TIPO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.TIPO_DOCUMENTO.getPosicion()).getValor());
					//*************VALIDACION CAMPO NÚMERO DE DOCUMENTO***************************************
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NRO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).getValor());
					//*************VALIDACION CAMPO AUXILIAR CUENTA CONTABLE**********************************
					DtoCuentaContable cuentaContable = new DtoCuentaContable();
					cuentaContable.setCodigo(rsMov.getString("cuenta_contable"));
					cuentaContable = UtilidadesInterfaz.consultarDatosCuentaContable(con, cuentaContable);
	
					//Interna/Externa
					if(cuentaContable.getCuentaContable().equals(""))
					{
						
							
						cuentaContable = UtilidadesInterfaz.consultarCuentaContableInterfazUnidadFuncional(con, rsMov.getInt("centro_costo_solicitante"), false, false, false, false, true);
						if(cuentaContable.getCuentaContable().equals(""))
						{
							cuentaContable = UtilidadesInterfaz.consultarCuentaContableInterfazServicio(con, rsMov.getInt("servicio"), rsMov.getInt("centro_costo_solicitante"), false, false, true);
						}
						
						cuentaContable.setMensaje("Cuenta Costo Honorarios");
						
					}
					
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setValor(cuentaContable.getCuentaContable());
					if(cuentaContable.getCuentaContable().equals(""))
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).getDescripcion()+". [ "+cuentaContable.getMensaje()+" ] ");
					}
					
					//*************VALIDACION CAMPO TERCERO**********************************
					if(cuentaContable.isManejaTerceros())
					{
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setValor(rsMov.getString("id_tercero"));	
						if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).getValor().equals(""))
						{
							lineaMov.setExisteInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).getDescripcion());
						}
					}
					
					//*************VALIDACION CAMPO CENTRO OPERACION MOVIMIENTO**********************************
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getValor());
					
					//*************VALIDACION CAMPO UNIDAD DE NEGOCIO**********************************
					//Interna
					DtoEspecialidad especialidad = new DtoEspecialidad();
					
					especialidad.setCodigo(rsMov.getInt("especialidad_solicitada"));
					if(especialidad.getCodigo()>0)
					{
						UtilidadesHistoriaClinica.consultarEspecialidad(con, especialidad);
					}
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.UNIDAD_NEGOCIO.getPosicion()).setValor(especialidad.getCentroCostoHonorarios().getAcronimoUnidadFuncional());
					
					
					//*************VALIDACION CAMPO AUXILIAR DE CENTRO DE COSTOS**********************************
					//Interna
					if(UtilidadTexto.getBoolean(rsMov.getString("tipo_factura_sistema")))
					{
						if(cuentaContable.isManejoCentrosCosto())
						{
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setValor(rsMov.getString("cod_centro_costo_solicitante"));
							if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).getValor().equals(""))
							{
								lineaMov.setExisteInconsistencia(true);
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setInconsistencia(true);
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).getDescripcion());
							}
							else if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).getValor().length()>15)
							{
								//Si el campo supera el tamaño se trunca
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setValor(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).getValor().substring(0, 15));
							}
						}
					}
					//*************VALIDACION CAMPO VALOR DEBITO**********************************
					//Interna
					if(UtilidadTexto.getBoolean(rsMov.getString("tipo_factura_sistema")))
					{
						if(rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteDebitoFactura+"") || 
								rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteDebitoCuentaCobro+"") || 
									rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoConceptosCarteraDebito+""))
						{
							
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rsMov.getDouble("valor"),"0.0000"));
							linea.setSumaDebito(linea.getSumaDebito()+rsMov.getDouble("valor"));	
							
						}
						else if(rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteCreditoFactura+"") || 
									rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteCreditoCuentaCobro+"") || 
										rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoConceptosCarteraCredito+""))
						{
							
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor("0");	
							
						}
					}
					else
					{
						if(rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteDebitoFactura+"") || 
								rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteDebitoCuentaCobro+"") || 
									rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoConceptosCarteraDebito+""))
						{
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor("0");
						}
						else if(rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteCreditoFactura+"") || 
								rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteCreditoCuentaCobro+"") || 
									rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoConceptosCarteraCredito+""))
						{
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rsMov.getDouble("valor_ajuste"),"0.0000"));
							linea.setSumaDebito(linea.getSumaDebito()+rsMov.getDouble("valor_ajuste"));
						}
					}
					
					if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).getValor().equals(""))
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setDescripcionIncon("Falta definir informacion para el campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).getDescripcion());
					}
					
					
					//*************VALIDACION CAMPO VALOR CREDITO**********************************
					//Interna
					if(UtilidadTexto.getBoolean(rsMov.getString("tipo_factura_sistema")))
					{
						if(rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteDebitoFactura+"") || 
								rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteDebitoCuentaCobro+"") || 
									rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoConceptosCarteraDebito+""))
						{
							
							//lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rsMov.getDouble("valor"),"0.0000"));
							//linea.setSumaCredito(linea.getSumaCredito()+rsMov.getDouble("valor"));
							
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor("0");
						}
						else if(rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteCreditoFactura+"") || 
								rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteCreditoCuentaCobro+"") || 
									rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoConceptosCarteraCredito+""))
						{
							//lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor("0");
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rsMov.getDouble("valor"),"0.0000"));
							linea.setSumaCredito(linea.getSumaCredito()+rsMov.getDouble("valor"));
						}
					}
					else
					{
						if(rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteDebitoFactura+"") || 
								rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteDebitoCuentaCobro+"") || 
									rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoConceptosCarteraDebito+""))
						{
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rsMov.getDouble("valor_ajuste"),"0.0000"));
							linea.setSumaCredito(linea.getSumaCredito()+rsMov.getDouble("valor_ajuste"));
						}
						else if(rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteCreditoFactura+"") || 
								rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteCreditoCuentaCobro+"") || 
									rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoConceptosCarteraCredito+""))
						{
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor("0");
						}
					}
					
					if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).getValor().equals(""))
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setDescripcionIncon("Falta definir informacion para el campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).getDescripcion());
					}
					
					
					
					//*************VALIDACION CAMPO OBSERVACIONES MOVIMIENTOS*********************
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.OBSERVACIONES_MOVIMI.getPosicion()).setValor(rsMov.getString("paciente")+" "+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+" "+linea.getArrayDocumentos().get(0).getNumeroDocumento());
					//**************************************************************************
						
					//parametrizacion.setPosicion(parametrizacion.getPosicion()+1);
					array.add(lineaMov);
					
					if(lineaMov.isExisteInconsistencia())
						linea.setExisteInconsistencia(true);
				} 
				
				/************CALCULO DE LA RETENCION*********************************************************************************/
				DtoRetencion retencion = UtilidadesInterfaz.calcularRetencionInterfaz(con, linea.getArrayDocumentos(), parametrizacion, linea.getArrayCampos().get(CampoDocumentoContable.TERCERO_DOCUMENTO.getPosicion()).getValor(), true,Utilidades.convertirADouble(rsMov.getString("valor"))); // retencion 
				
				for(String mensajeIncon:retencion.getInconsistenciasRetencion())
				{
					linea.setExisteInconsistencia(true);
					linea.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_REGISTRO.getPosicion()).setInconsistencia(true);
					linea.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_REGISTRO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
					linea.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_REGISTRO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
					linea.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_REGISTRO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
					linea.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_REGISTRO.getPosicion()).setDescripcionIncon(mensajeIncon);
				}
				
				for(int i=0;i<retencion.getConceptos().size();i++)
				{
					DtoInterfazLineaS1E lineaMov = new DtoInterfazLineaS1E(GeneracionInterfaz1E.indicadorLineaDetMovDocumentoContable);
					//**************VALIDACIÓN CAMPO NUMERO REGISTRO*******************************
					//lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_REGISTRO.getPosicion()).setValor(parametrizacion.getPosicion()+"");
					//***************VALIDACION CAMPO COMPAÑÍA****************************************
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.COMPANIA.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.COMPANIA.getPosicion()).getValor());
					//***************VALIDACION CAMPO CENTRO OPERACION****************************************
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPERACION.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getValor());
					//**************VALIDACION CAMPO TIPO DE DOCUMENTO****************************************
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TIPO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.TIPO_DOCUMENTO.getPosicion()).getValor());
					//*************VALIDACION CAMPO NÚMERO DE DOCUMENTO***************************************
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NRO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).getValor());
					//*************VALIDACION CAMPO AUXILIAR CUENTA CONTABLE**********************************
					DtoCuentaContable cuentaContable = new DtoCuentaContable();
					cuentaContable.setCodigo(retencion.getConceptos().get(i).getCuentaRetencion().getCodigo());
					if(!cuentaContable.getCodigo().equals(""))
					{
						cuentaContable = UtilidadesInterfaz.consultarDatosCuentaContable(con, cuentaContable);
					}
					
					cuentaContable.setMensaje("Cuenta Retencion");
					
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setValor(cuentaContable.getCuentaContable());
					if(cuentaContable.getCuentaContable().equals(""))
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).getDescripcion()+". [ "+cuentaContable.getMensaje()+" ] ");
					}
					
					//*************VALIDACION CAMPO TERCERO**********************************
					if(cuentaContable.isManejaTerceros())
					{
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setValor(rsMov.getString("id_tercero"));	
						if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).getValor().equals(""))
						{
							lineaMov.setExisteInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).getDescripcion());
						}
					}
					
					//*************VALIDACION CAMPO CENTRO OPERACION MOVIMIENTO**********************************
					asignarCodigoCentroAtencionContable(lineaMov, parametrizacion, CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion(), "");
					
					
					//*************VALIDACION CAMPO UNIDAD DE NEGOCIO**********************************
					asignarUnidadFuncionalEstandar(lineaMov,parametrizacion,CampoMoviDocumentoContable.UNIDAD_NEGOCIO.getPosicion(),linea,"");
					
					
					//*************VALIDACION CAMPO VALOR DEBITO**********************************
					if(rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteDebitoFactura+"") || 
							rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteDebitoCuentaCobro+"") || 
								rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoConceptosCarteraDebito+""))
					{
						
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor("0");
						
					}
					else if(rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteCreditoFactura+"") || 
								rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteCreditoCuentaCobro+"") || 
									rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoConceptosCarteraCredito+""))
					{
						
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(retencion.getConceptos().get(i).getValorRetencion(),"0.0000"));
						linea.setSumaDebito(linea.getSumaDebito()+Utilidades.convertirADouble(retencion.getConceptos().get(i).getValorRetencion()));
						
					}
					
					
					if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).getValor().equals(""))
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setDescripcionIncon("Falta definir informacion para el campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).getDescripcion());
					}
					
					
					//*************VALIDACION CAMPO VALOR CREDITO**********************************
					if(rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteDebitoFactura+"") || 
							rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteDebitoCuentaCobro+"") || 
								rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoConceptosCarteraDebito+""))
					{
						
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(retencion.getConceptos().get(i).getValorRetencion(),"0.0000"));
						linea.setSumaCredito(linea.getSumaCredito()+Utilidades.convertirADouble(retencion.getConceptos().get(i).getValorRetencion()));
						
					}
					else if(rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteCreditoFactura+"") || 
							rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteCreditoCuentaCobro+"") || 
								rsMov.getString("tipo_ajuste").equals(ConstantesBD.codigoConceptosCarteraCredito+""))
					{
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor("0");
					}
					
					
					if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).getValor().equals(""))
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setDescripcionIncon("Falta definir informacion para el campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).getDescripcion());
					}
					
					//*************VALIDACION CAMPO VALOR BASE GRAVABLE*****************************
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_BASE_GRAVABLE.getPosicion()).setValor(retencion.getConceptos().get(i).getValorBaseGravable());
					
					
					//*************VALIDACION CAMPO OBSERVACIONES MOVIMIENTOS*********************
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.OBSERVACIONES_MOVIMI.getPosicion()).setValor(rsMov.getString("paciente")+" "+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+" "+linea.getArrayDocumentos().get(0).getNumeroDocumento());
					//**************************************************************************
						
					//parametrizacion.setPosicion(parametrizacion.getPosicion()+1);
					array.add(lineaMov);
					
					if(lineaMov.isExisteInconsistencia())
						linea.setExisteInconsistencia(true);
				}//Fin For de conceptos
				
			} //Fin While

			if(!huboLineaDetMov && array.size() > 0)
			{	
				huboLineaDetMov = true;
				logger.info("..:Genero "+array.size()+" de linea "+DtoInterfazCampoS1E.getDescripcionLinea(GeneracionInterfaz1E.indicadorLineaDetMovDocumentoContable));
			}
			rsMov.close();
			pstMov.close();
			
			//************AGRUPAR EL DETALLE**************************************************
			lineasMovAgrupada = agruparLineasMovimientoContable(array,parametrizacion);
			//**********************************************************************************
		}
		catch(SQLException e)
		{
			logger.error("Error en generarLineaMovimientoAjuste: "+e+" "+consultaDetAjusteFact02Str);
		}
		
		return lineasMovAgrupada;

	}
		
	//*******************************************************METODOS SEBAS*******************************************************************************************************************************
	
	/**
	 * Método para obtener el tercero de un documneto dependiendo de us tipo
	 */
	private static void asignarTerceroDocumento(Connection con,DtoInterfazLineaS1E lineaMov,DtoInterfazS1EInfo parametrizacion,int posicion)
	{
		try
		{
			//Según tipo de documento
			int codigoTipoDocumento = Integer.parseInt(lineaMov.getArrayDocumentos().get(0).getTipoDocumento());
		
			//*************OBTENER TERCERO PARA RECIBOS DE CAJA Y ANULACIÓN DE RECIBOS DE CAJA**************************************
			switch(codigoTipoDocumento)
			{
			case ConstantesBD.codigoTipoDocInteDevolRecibosCaja:
			case ConstantesBD.codigoTipoDocInteReciboCaja:
			case ConstantesBD.codigoTipoDocInteAnulaRecCaja:				
				String numeroReciboCaja = lineaMov.getArrayDocumentos().size()>1?lineaMov.getArrayDocumentos().get(1).getNumeroDocumento():lineaMov.getArrayDocumentos().get(0).getNumeroDocumento();
				String terceroDocumento = "";
				String codigoTipoConceptoRC = "";
				String filtroConceptoRC = "";
				String nitConcepto = "";
				///***************Se toma la información del concepto de recibo de caja**************************************
				PreparedStatementDecorator pst = new  PreparedStatementDecorator(con.prepareStatement(consultaDatosConceptoReciboCaja, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setString(1,numeroReciboCaja);
				ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
				if(rs.next())
				{
					terceroDocumento = rs.getString("nit_tercero");
					codigoTipoConceptoRC = rs.getString("tipo_concepto");
					filtroConceptoRC = rs.getString("filtro");
					nitConcepto = rs.getString("nit");
					
				}
				pst.close();
				rs.close();
				//***********************************************************************************************************
				if(codigoTipoConceptoRC.equals(ConstantesBD.codigoTipoIngresoTesoreriaPacientes+""))
				{
				
					//Se toma el tipo de regimen del recibo de caja para saber si el convenio es particular o no
					String tipoRegimen = "";
					pst = new PreparedStatementDecorator(con.prepareStatement(obtenerTipoRegimenPacienteReciboCajaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst.setString(1,numeroReciboCaja);
					
					rs = new ResultSetDecorator(pst.executeQuery());
					if(rs.next())
					{
						tipoRegimen = rs.getString("codigo_tipo_regimen");
					}
					if(tipoRegimen.equals(ConstantesBD.codigoTipoRegimenParticular+""))
					{
						//Convenio particular:
						//Se busca si hay parametrización en el campo Terceros Generico Facturas Particulares
						if(!UtilidadTexto.isEmpty(parametrizacion.getNitTerceroGenFacPar()))
						{
							lineaMov.getArrayCampos().get(posicion).setValor(parametrizacion.getNitTerceroGenFacPar());
						}
						//de lo contrario se toma el tercero del recibo de caja
						else
						{
							lineaMov.getArrayCampos().get(posicion).setValor(terceroDocumento);
						}
					}
					else
					{
						//Convenio NO Particular
						//Se busca si hay parametrización en el campo Tercero Genérico Monto de pago pacientes
						if(!UtilidadTexto.isEmpty(parametrizacion.getNitTerceroGenPagPac()))
						{							
							lineaMov.getArrayCampos().get(posicion).setValor(parametrizacion.getNitTerceroGenPagPac());
						}
						//De lo contrario se toma el tercero del recibo de caja
						else
						{						
							lineaMov.getArrayCampos().get(posicion).setValor(terceroDocumento);
						}
					}
					
					pst.close();
					rs.close();

				}
				else if(codigoTipoConceptoRC.equals(ConstantesBD.codigoTipoIngresoTesoreriaOtrasCxC+""))
				{
					if(filtroConceptoRC.equals(ConstantesIntegridadDominio.acronimoDeudor))
					{
						lineaMov.getArrayCampos().get(posicion).setValor(terceroDocumento);
					}
					else if(filtroConceptoRC.equals(ConstantesIntegridadDominio.acronimoFactura))
					{
						pst = new PreparedStatementDecorator(con.prepareStatement(obtenerTerceroFacturaVariaReciboCajaStr,ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						pst.setString(1,numeroReciboCaja);
						rs = new ResultSetDecorator(pst.executeQuery());
						if(rs.next())
						{
							lineaMov.getArrayCampos().get(posicion).setValor(rs.getString("id_tercero"));
						}
						else
						{
							lineaMov.getArrayCampos().get(posicion).setValor(terceroDocumento);
						}
						pst.close();
						rs.close();
					}
				}
				else if(codigoTipoConceptoRC.equals(ConstantesBD.codigoTipoIngresoTesoreriaConvenios+""))
				{
					lineaMov.getArrayCampos().get(posicion).setValor(terceroDocumento);
				}
				else if(codigoTipoConceptoRC.equals(ConstantesBD.codigoTipoIngresoTesoreriaAbonos+""))
				{
					//Se verifica si hay parametrización en el campo Terceros Generico Facturas Particulares
					if(!UtilidadTexto.isEmpty(parametrizacion.getNitTerceroGenFacPar()))
					{							
						lineaMov.getArrayCampos().get(posicion).setValor(parametrizacion.getNitTerceroGenFacPar());
					}
				}
				else if(codigoTipoConceptoRC.equals(ConstantesBD.codigoTipoIngresoTesoreriaNinguno+""))
				{
					if(!nitConcepto.equals(""))
					{
						lineaMov.getArrayCampos().get(posicion).setValor(nitConcepto);
					}
					else
					{
						lineaMov.getArrayCampos().get(posicion).setValor(terceroDocumento);
					}
				}
				
			break;
			case ConstantesBD.codigoTipoDocInteFacturaPaciente:
			case ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente:
			case ConstantesBD.codigoTipoDocInteAjustesFactPaciente:
				String numeroFactura = lineaMov.getArrayDocumentos().size()>1?lineaMov.getArrayDocumentos().get(1).getNumeroDocumento():lineaMov.getArrayDocumentos().get(0).getNumeroDocumento();
				String codigoTipoRegimen = "";
				String nitTerceroConvenio = "";
				pst = new PreparedStatementDecorator(con.prepareStatement(consultarDatosConvenioFacturaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setLong(1,Long.parseLong(numeroFactura));
				rs = new ResultSetDecorator(pst.executeQuery());
				
				if(rs.next())
				{
					codigoTipoRegimen = rs.getString("codigo_tipo_regimen");
					nitTerceroConvenio = rs.getString("id_tercero");
					
					//CONVENIO PARTICULAR
					if(codigoTipoRegimen.equals(ConstantesBD.codigoTipoRegimenParticular+""))
					{
						///Se busca si hay parametrización en el campo Tercero Genérico Monto de pago pacientes
						if(!UtilidadTexto.isEmpty(parametrizacion.getNitTerceroGenPagPac()))
						{
							lineaMov.getArrayCampos().get(posicion).setValor(parametrizacion.getNitTerceroGenPagPac());
						}
						//De lo contrario se toma el tercero del recibo de caja
						else
						{
							lineaMov.getArrayCampos().get(posicion).setValor(nitTerceroConvenio);
						}
					}
					//CONVENIO NO PARTICULAR
					else
					{
						lineaMov.getArrayCampos().get(posicion).setValor(nitTerceroConvenio);
					}
				}
				pst.close();
				rs.close();
			break;
			case ConstantesBD.codigoTipoDocInteFacturasVarias:
			case ConstantesBD.codigoTipoDocInteAnulaFacturasVarias:
			case ConstantesBD.codigoTipoDocInteAjusFacturasVarias:
				String numeroFacturaVaria = lineaMov.getArrayDocumentos().size()>1?lineaMov.getArrayDocumentos().get(1).getNumeroDocumento():lineaMov.getArrayDocumentos().get(0).getNumeroDocumento();
				pst = new PreparedStatementDecorator(con.prepareStatement(consultarDatosTerceroFacturaVaria, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setLong(1,Long.parseLong(numeroFacturaVaria));
				rs = new ResultSetDecorator(pst.executeQuery());
				if(rs.next())
				{
					lineaMov.getArrayCampos().get(posicion).setValor(rs.getString("id_tercero_concepto"));
					if(UtilidadTexto.isEmpty(lineaMov.getArrayCampos().get(posicion).getValor()))
					{
						lineaMov.getArrayCampos().get(posicion).setValor(rs.getString("id_tercero_deudor"));
					}
				}
				rs.close();
				pst.close();
			break;
			case ConstantesBD.codigoTipoDocInteCCCapitacion:
			case ConstantesBD.codigoTipoDocInteAjustesCCCapitacion:
				String numeroCuentaCobro = lineaMov.getArrayDocumentos().size()>1?lineaMov.getArrayDocumentos().get(1).getNumeroDocumento():lineaMov.getArrayDocumentos().get(0).getNumeroDocumento();
				pst = new PreparedStatementDecorator(con.prepareStatement(consultarNitTerceroCuentaCobroCapitacionStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setLong(1,Long.parseLong(numeroCuentaCobro));
				rs = new ResultSetDecorator(pst.executeQuery());
				if(rs.next())
				{
					lineaMov.getArrayCampos().get(posicion).setValor(rs.getString("id_tercero"));
				}
				rs.close();
				pst.close();
			break;
			}
			//********************************************************************************************************
		}
		catch(SQLException e)
		{
			logger.error("error en asignarTerceroDocumento: "+e);
		}
	}
	
	/**
	 * Método para asignar las observaciones de la parametrizacion dee interfaz
	 * @param linea
	 * @param parametrizacion
	 * @param posicion
	 */
	private static void asignarObservacionesDocumento(DtoInterfazLineaS1E linea,DtoInterfazS1EInfo parametrizacion,int posicion,String codigoTipoDocumento)
	{
		String observaciones = "";
		
		///Se busca el indicativo del tipo de documento que aplica para el tipo de documento
		for(DtoTiposInterfazDocumentosParam1E tipo:parametrizacion.getArrayListDtoTiposDocumentos())
		{	
			if(tipo.getTipoDocumento().equals(codigoTipoDocumento))
			{
				observaciones = tipo.getObservacionesEncabezado();
			}
		}
		
		if(!UtilidadTexto.isEmpty(observaciones))
		{
			linea.getArrayCampos().get(posicion).setValor(observaciones);
		}
	}
	
	/**
	 * @param Connection con
	 * @param DtoInterfazLineaS1E linea
	 * @param DtoInterfazS1EInfo parametrizacion
	 * */
	private static ArrayList<DtoInterfazLineaS1E> generarLineaMovimientoRecaudoCuentasXCobrar(
			Connection con,
			DtoInterfazLineaS1E linea,
			DtoInterfazS1EInfo parametrizacion)
	{
		ArrayList<DtoInterfazLineaS1E> array = new ArrayList<DtoInterfazLineaS1E>();
		String documentoContable = "";
		documentoContable = linea.getArrayDocumentos().size()>1?linea.getArrayDocumentos().get(1).getNumeroDocumento():linea.getArrayDocumentos().get(0).getNumeroDocumento();
		String cadenalog = "";
		try
		{
			PreparedStatementDecorator pst = null;
			PreparedStatementDecorator pstMov = null;
			ResultSetDecorator rs = null;
			
			if(linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteReciboCaja+"") || 
					linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteAnulaRecCaja+""))
			{
				if(linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteReciboCaja+""))
				{
					cadenalog = consultaDetMovCuentasXCobrarRecaudoStr;
					pstMov = new PreparedStatementDecorator(con.prepareStatement(consultaDetMovCuentasXCobrarRecaudoStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pstMov.setString(1,documentoContable);
				}
				else
				{
					cadenalog = consultaDetMovCuentasXCobrarRecaudoStr_anulados;
					pstMov = new PreparedStatementDecorator(con.prepareStatement(consultaDetMovCuentasXCobrarRecaudoStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pstMov.setString(1,documentoContable);
				}
			}
			else
			{
				cadenalog = consultaDetMovCuentasXCobrarDevRecaudoStr;
				pstMov = new PreparedStatementDecorator(con,consultaDetMovCuentasXCobrarDevRecaudoStr);
				pstMov.setString(1,linea.getArrayDocumentos().get(0).getNumeroDocumento());
			}
			
			
			ResultSetDecorator rsMov = new ResultSetDecorator(pstMov.executeQuery());
			
			while(rsMov.next())
			{
				DtoInterfazLineaS1E lineaMov = new DtoInterfazLineaS1E(GeneracionInterfaz1E.indicadorLineaDetCxC);
				
				//**************VALIDACIÓN CAMPO NUMERO REGISTRO*******************************
				lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.NUMERO_REGISTRO.getPosicion()).setValor(parametrizacion.getPosicion()+"");
				//***************VALIDACION CAMPO COMPAÑÍA****************************************
				lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.COMPANIA.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.COMPANIA.getPosicion()).getValor());
				//***************VALIDACION CAMPO CENTRO OPERACION****************************************
				lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.CENTRO_OPERACION.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getValor());
				
				
				//**************VALIDACION CAMPO TIPO DE DOCUMENTO****************************************
				lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TIPO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.TIPO_DOCUMENTO.getPosicion()).getValor());
				//*************VALIDACION CAMPO NÚMERO DE DOCUMENTO***************************************
				lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.NRO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).getValor());
				//*************VALIDACION CAMPO AUXILIAR CUENTA CONTABLE**********************************
				DtoCuentaContable cuentaContable = new DtoCuentaContable();
				
				int tipoIngreso=rsMov.getInt("codigo_tipo_ingreso");
				switch(tipoIngreso)
				{
					case ConstantesBD.codigoTipoIngresoTesoreriaConvenios:
					case ConstantesBD.codigoTipoIngresoTesoreriaPacientes:
						//Se obtiene el tipo de regimen y convenio dle recibo de caja
						int codigoConvenio = ConstantesBD.codigoNuncaValido;
						String tipoRegimen = "";
						int codigoTipoContrato = ConstantesBD.codigoNuncaValido;
						String codigoTipoConvenio = "";
						int codigoInstitucion = ConstantesBD.codigoNuncaValido;
						if(rsMov.getInt("codigo_tipo_ingreso")==ConstantesBD.codigoTipoIngresoTesoreriaConvenios)
						{
							pst = new PreparedStatementDecorator(con.prepareStatement(obtenerTipoRegimenConvenioReciboCajaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						}
						else
						{
							pst = new PreparedStatementDecorator(con.prepareStatement(obtenerTipoRegimenPacienteReciboCajaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						}
						pst.setString(1,documentoContable);
						rs = new ResultSetDecorator(pst.executeQuery());
						if(rs.next())
						{
							tipoRegimen = rs.getString("codigo_tipo_regimen");
							codigoConvenio = rs.getInt("codigo_convenio");
							codigoTipoContrato = rs.getInt("codigo_tipo_contrato");
							codigoTipoConvenio = rs.getString("tipo_convenio");
							codigoInstitucion = rs.getInt("institucion");
						}
						pst.close();
						rs.close();
						
						//Segun tipo de concepto se hace una búsqueda distinta
						if(rsMov.getInt("codigo_tipo_ingreso")== ConstantesBD.codigoTipoIngresoTesoreriaConvenios)
						{
						
							if(codigoTipoContrato==ConstantesBD.codigoTipoContratoCapitado)
							{
								cuentaContable = UtilidadesInterfaz.consultarCuentaContableInterfazConvenio(con, codigoConvenio, tipoRegimen, ConstantesBD.tipoCuentaConvenio,false,true);
								if(cuentaContable.getCuentaContable().equals(""))
								{
									cuentaContable = UtilidadesFacturacion.consultarCuentaContableTipoConvenio(con, codigoTipoConvenio, codigoInstitucion, false, false, false, false, true);
								}
							}
							else
							{
								cuentaContable = UtilidadesInterfaz.consultarCuentaContableInterfazConvenio(con, codigoConvenio, tipoRegimen, ConstantesBD.tipoCuentaConvenio,true,false);
								if(cuentaContable.getCuentaContable().equals(""))
								{
									cuentaContable = UtilidadesFacturacion.consultarCuentaContableTipoConvenio(con, codigoTipoConvenio, codigoInstitucion, true, false, false, false, false);
								}
							}
						}
						else
						{
							cuentaContable = UtilidadesInterfaz.consultarCuentaContableInterfazConvenio(con, codigoConvenio, tipoRegimen, ConstantesBD.tipoCuentaPaciente,true,false);
							if(cuentaContable.getCuentaContable().equals(""))
							{
								cuentaContable = UtilidadesFacturacion.consultarCuentaContableTipoConvenio(con, codigoTipoConvenio, codigoInstitucion, false, false, true, false, false);
							}
						}
					break;
					case ConstantesBD.codigoTipoIngresoTesoreriaCarteraParticular:
						cuentaContable.setCodigo(rsMov.getString("cuenta_contable"));
						cuentaContable = UtilidadesInterfaz.consultarDatosCuentaContable(con, cuentaContable);
					break;
					/*
					case ConstantesBD.codigoTipoIngresoTesoreriaNinguno:
						cuentaContable.setCodigo(rsMov.getString("codigo"));
						cuentaContable = UtilidadesInterfaz.consultarDatosCuentaContable(con, cuentaContable);
					break;*/
					case ConstantesBD.codigoTipoIngresoTesoreriaOtrasCxC:
						//Se verifica el filtro del concepto
						
						if(rsMov.getString("valor_concep_ing_tes").equals(ConstantesIntegridadDominio.acronimoFactura))
						{
							pst = new PreparedStatementDecorator(con.prepareStatement(obtenerCuentaDebitoFacturaVariaReciboCajaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
							pst.setString(1,documentoContable);
							rs = new ResultSetDecorator(pst.executeQuery());
							if(rs.next())
							{								
								cuentaContable.setCodigo(rs.getString("codigo"));
								cuentaContable.setCuentaContable(rs.getString("cuenta_contable"));
								cuentaContable.setDescripcion(rs.getString("descripcion"));
								cuentaContable.setActivo(UtilidadTexto.getBoolean(rs.getString("activo")));
								cuentaContable.setManejaTerceros(UtilidadTexto.getBoolean(rs.getString("manejo_terceros")));
								cuentaContable.setManejoCentrosCosto(UtilidadTexto.getBoolean(rs.getString("manejo_centros_costo")));
								cuentaContable.setManejoBaseGravable(UtilidadTexto.getBoolean(rs.getString("manejo_base_gravable")));
								cuentaContable.setNaturalezaCuenta(rs.getString("naturaleza_cuenta"));
								cuentaContable.setCodigoInstitucion(rs.getInt("institucion"));
								cuentaContable.setAnioVigencia(rs.getString("anio_vigencia"));
								
							}
							
						}
						else if(rsMov.getString("valor_concep_ing_tes").equals(ConstantesIntegridadDominio.acronimoDeudor))
						{
							cuentaContable.setCodigo(rsMov.getString("codigo"));
							cuentaContable.setCuentaContable(rsMov.getString("cuenta_contable"));
							cuentaContable.setDescripcion(rsMov.getString("descripcion"));
							cuentaContable.setActivo(UtilidadTexto.getBoolean(rsMov.getString("activo")));
							cuentaContable.setManejaTerceros(UtilidadTexto.getBoolean(rsMov.getString("manejo_terceros")));
							cuentaContable.setManejoCentrosCosto(UtilidadTexto.getBoolean(rsMov.getString("manejo_centros_costo")));
							cuentaContable.setManejoBaseGravable(UtilidadTexto.getBoolean(rsMov.getString("manejo_base_gravable")));
							cuentaContable.setNaturalezaCuenta(rsMov.getString("naturaleza_cuenta"));
							cuentaContable.setCodigoInstitucion(rsMov.getInt("institucion"));
							cuentaContable.setAnioVigencia(rsMov.getString("anio_vigencia"));
						}
					break;
					case ConstantesBD.codigoTipoIngresoTesoreriaAbonos:
						cuentaContable.setCodigo(parametrizacion.getCuentaAbonoPac());
						cuentaContable = UtilidadesInterfaz.consultarDatosCuentaContable(con, cuentaContable);
					break;
				}
				//Si no hubo cuenta contable se genera inconsistencia
				if(cuentaContable.getCuentaContable().toString().trim().equals(""))
				{
					lineaMov.setExisteInconsistencia(true);
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.AUX_CUENT_CONTABLE.getPosicion()).setInconsistencia(true);
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.AUX_CUENT_CONTABLE.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.AUX_CUENT_CONTABLE.getPosicion()).setConsecutivoDocumento(linea.getArrayDocumentos().get(0).getConsecutivoDocumento());
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.AUX_CUENT_CONTABLE.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.AUX_CUENT_CONTABLE.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.AUX_CUENT_CONTABLE.getPosicion()).setDescripcionIncon(mensajeEInconNoInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.AUX_CUENT_CONTABLE.getPosicion()).getDescripcion()+" Inconsistencia de Datos Tipo Documento:"+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+" Número de Documento:"+linea.getArrayDocumentos().get(0).getNumeroDocumento());
				}
				else
				{
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.AUX_CUENT_CONTABLE.getPosicion()).setValor(cuentaContable.getCuentaContable());
				}
				//******************************************************************************************
				
				//*************VALIDACION CAMPO TERCERO**********************************
				if(cuentaContable.isManejaTerceros())
				{
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.TERCERO_DOCUMENTO.getPosicion()).getValor());
					if(lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).getValor().equals(""))
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).setConsecutivoDocumento(linea.getArrayDocumentos().get(0).getConsecutivoDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).setDescripcionIncon(mensajeEInconNoInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).getDescripcion()+" Inconsistencia de Datos Tipo Documento:"+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+" Número de Documento:"+linea.getArrayDocumentos().get(0).getNumeroDocumento());
					}
				}
				else
				{
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).setValor("");
				}
				
				//*************VALIDACION CAMPO CENTRO OPERACION MOVIMIENTO**********************************
				asignarCodigoCentroAtencionContable(lineaMov, parametrizacion,CampoMoviCuentasXCobrar.CENTRO_OPER_MOVI.getPosicion(), "");
				
				//*************VALIDACION CAMPO UNIDAD DE NEGOCIO**********************************
				asignarUnidadFuncionalEstandar(lineaMov,parametrizacion,CampoMoviCuentasXCobrar.UNIDAD_NEGOCIO.getPosicion(),linea,"");//--1111
				//*************************************************************************************
				
				//***********VALIDACION CAMPO AUXILIAR CENTRO DE COSTO**************************************
				lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.AUX_CENTRO_COSTO.getPosicion()).setValor(asignarAuxiliarCentroCosto(con, documentoContable));
				
				//*******************************************************************************************
				
				//**************VALIDACION CAMPO VALOR DEBITO********************************************
				if(linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteReciboCaja+""))
				{
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).setValor("0");
				}
				else if(linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteAnulaRecCaja+"") || 
						linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteDevolRecibosCaja+""))
				{
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rsMov.getDouble("valor"),"0.0000"));
					linea.setSumaDebito(linea.getSumaDebito()+Utilidades.convertirADouble(lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).getValor()));
					
					if(lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).getValor().equals(""))
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).setConsecutivoDocumento(linea.getArrayDocumentos().get(0).getConsecutivoDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).setDescripcionIncon(mensajeEInconNoInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).getDescripcion()+" Inconsistencia de Datos Tipo Documento:"+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+" Número de Documento:"+linea.getArrayDocumentos().get(0).getNumeroDocumento());
					}
				}
				//*******************************************************************************************
				//******************VALIDACION CAMPO VALOR CREDITO**********************************************
				if(linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteAnulaRecCaja+"") ||
						linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteDevolRecibosCaja+""))
				{
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).setValor("0");
				}
				else if(linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteReciboCaja+"")
						/*Esto lo agregué debido a que creo que no se deben contabilizar los de tipo ninguno Feb 10 2010 JDR*
						&& tipoIngreso!=ConstantesBD.codigoTipoIngresoTesoreriaNinguno
						/* Hasta aqui */
				)
				{
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rsMov.getDouble("valor"),"0.0000"));
					linea.setSumaCredito(linea.getSumaCredito()+Utilidades.convertirADouble(lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).getValor()));
					
					if(lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).getValor().equals(""))
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).setDescripcionIncon(mensajeEInconNoInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).getDescripcion()+" Inconsistencia de Datos Tipo Documento:"+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+" Número de Documento:"+linea.getArrayDocumentos().get(0).getNumeroDocumento());
					}
				}
				//************************************************************************************************
				//************************VALIDACION CAMPO OBSERVACIONES MOVIMIENTO********************************
				String nombreBeneficiario = rsMov.getString("beneficiario");
				if(!nombreBeneficiario.equals(""))
				{
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.OBSERVACIONES_MOVIMIENTO.getPosicion()).setValor(nombreBeneficiario+" "+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+" "+linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).getValor());					
				}
				//*************************************************************************************************
				//**************************VALIDACION CAMPO TIPO DOCUMENTO CRUCE************************************
				asignarTipoDocCruzeParametrizacion(lineaMov, parametrizacion, CampoMoviCuentasXCobrar.TIPO_DOC_CRUCE.getPosicion(), linea,true, linea.getArrayCampos().get(CampoDocumentoContable.TIPO_DOCUMENTO.getPosicion()).getValor());
				//*************************************************************************************************
				//***************************VALIDACION CAMPO NÚMERO DOCUMENTO CRUCE*********************************
				//Dependiendo del tipo de documento
				if(linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteAnulaRecCaja+"")|| 
						linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteDevolRecibosCaja+"")
						)
				{
					switch(rsMov.getInt("codigo_tipo_ingreso"))
					{
						case ConstantesBD.codigoTipoIngresoTesoreriaConvenios:
						case ConstantesBD.codigoTipoIngresoTesoreriaPacientes:
						case ConstantesBD.codigoTipoIngresoTesoreriaCarteraParticular:
						case ConstantesBD.codigoTipoIngresoTesoreriaOtrasCxC:
							if(rsMov.getString("factura_cruce").trim().equals(""))
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.NRO_DOC_CRUCE.getPosicion()).setValor(rsMov.getString("doc_soporte").trim());
							else
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.NRO_DOC_CRUCE.getPosicion()).setValor(rsMov.getString("factura_cruce"));
						break;
						case ConstantesBD.codigoTipoIngresoTesoreriaAbonos:
							String ingreso=rsMov.getString("ingreso");
							lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.NRO_DOC_CRUCE.getPosicion()).setValor(ingreso);
							//lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.NRO_DOC_CRUCE.getPosicion()).setValor(documentoContable);
						break;
					}
				}
				else if(linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteReciboCaja+""))
				{
					switch(rsMov.getInt("codigo_tipo_ingreso"))
					{
						case ConstantesBD.codigoTipoIngresoTesoreriaAbonos:
							String ingreso=rsMov.getString("ingreso");
							lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.NRO_DOC_CRUCE.getPosicion()).setValor(ingreso);
						break;
						default:
							//sgun lo conversado con diana correa, siempre debe enviar el numero documento en la tabla de detalle_conceptos_rc
							if(rsMov.getString("doc_soporte").trim().equals(""))
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.NRO_DOC_CRUCE.getPosicion()).setValor(rsMov.getString("doc_soporte").trim());
							else
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.NRO_DOC_CRUCE.getPosicion()).setValor(rsMov.getString("factura_cruce"));
							//lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.NRO_DOC_CRUCE.getPosicion()).setValor(documentoContable);

					}
				}
				else
				{
					if(rsMov.getString("factura_cruce").trim().equals(""))
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.NRO_DOC_CRUCE.getPosicion()).setValor(rsMov.getString("doc_soporte").trim());
					else
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.NRO_DOC_CRUCE.getPosicion()).setValor(rsMov.getString("factura_cruce"));
				}
				//****************************************************************************************************
				//***************************VALIDACION CAMPO FECHA VENCIMIENTO DOCUMENTO******************************
				lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.FECHA_VEN_DOC.getPosicion()).setValor(linea.getArrayDocumentos().get(0).getFecha());
				if(lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.FECHA_VEN_DOC.getPosicion()).getValor().equals(""))
				{
					lineaMov.setExisteInconsistencia(true);
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.FECHA_VEN_DOC.getPosicion()).setInconsistencia(true);
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.FECHA_VEN_DOC.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.FECHA_VEN_DOC.getPosicion()).setConsecutivoDocumento(linea.getArrayDocumentos().get(0).getConsecutivoDocumento());
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.FECHA_VEN_DOC.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.FECHA_VEN_DOC.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.FECHA_VEN_DOC.getPosicion()).setDescripcionIncon(mensajeEInconNoInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.FECHA_VEN_DOC.getPosicion()).getDescripcion()+" Inconsistencia de Datos Tipo Documento:"+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+" Número de Documento:"+linea.getArrayDocumentos().get(0).getNumeroDocumento());
				}
				//******************************************************************************************************
				//************************VALIDACION CAMPO FECHA PRONTO PAGO DOCUMENTO*************************************
				lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.FECHA_PAG_DOC.getPosicion()).setValor(parametrizacion.getFechaProceso());
				//***********************************************************************************************************
				//*************************VALIDACION CAMPO TERCERO VENDEDOR****************************************************
				asignarIdentificacionInstitucion(lineaMov, parametrizacion, CampoMoviCuentasXCobrar.TERCERO_VENDEDOR.getPosicion());
				//*****************************************************************************************************************
				//**************************VALIDACION CAMPO OBSERVACIONES MOVIMIENTO SALDO ABIERTO************************************
				if(!nombreBeneficiario.equals(""))
				{
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.OBS_SALDO_ABI.getPosicion()).setValor(nombreBeneficiario+" "+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+" "+linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).getValor());
				}
				else
				{
					lineaMov.setExisteInconsistencia(true);
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.OBS_SALDO_ABI.getPosicion()).setInconsistencia(true);
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.OBS_SALDO_ABI.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.OBS_SALDO_ABI.getPosicion()).setConsecutivoDocumento(linea.getArrayDocumentos().get(0).getConsecutivoDocumento());
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.OBS_SALDO_ABI.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.OBS_SALDO_ABI.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.OBS_SALDO_ABI.getPosicion()).setDescripcionIncon(mensajeEInconNoInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.OBS_SALDO_ABI.getPosicion()).getDescripcion()+" Inconsistencia de Datos Tipo Documento:"+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+" Número de Documento:"+linea.getArrayDocumentos().get(0).getNumeroDocumento());
				}
				//***********************************************************************************************************************
				
				
				parametrizacion.setPosicion(parametrizacion.getPosicion()+1);
				array.add(lineaMov); 
				
				if(lineaMov.isExisteInconsistencia())
					linea.setExisteInconsistencia(true);
			}
			
			//logger.info("existe error en linea >> "+linea.isExisteInconsistencia());
			if(!huboLineaCxC && array.size() > 0)
			{	
				huboLineaCxC = true;
				logger.info("..:Genero "+array.size()+" de linea "+DtoInterfazCampoS1E.getDescripcionLinea(GeneracionInterfaz1E.indicadorLineaDetCxC));
			}
			
			pstMov.close();
			rsMov.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en generarLineaMovimientoRecaudoErr: "+e+" "+cadenalog);
			e.printStackTrace();
		}
		
		
		return array;
		
	}
	
	/**
	 * Método usado paera asignar la unidad funcional estandar de la paramtrizacion de la interfaz 1E
	 * @param linea
	 * @param parametrizacion
	 * @param posicion
	 * @param acronimoTipoMovimiento 
	 * @param codigoTipoDocumento
	 */
	private static void asignarUnidadFuncionalEstandar(
			DtoInterfazLineaS1E lineaDet,
			DtoInterfazS1EInfo parametrizacion,
			int posicion,
			DtoInterfazLineaS1E linea, String acronimoTipoMovimiento)
	{
		///Se busca el indicativo de Unidad Funcional Estandar que aplica para el tipo de documento
		String unidadFuncEstandar = "";
		
		
		
		for(DtoTiposInterfazDocumentosParam1E tipo:parametrizacion.getArrayListDtoTiposDocumentos())
		{
			
			
			
			if(tipo.getTipoDocumento().equals(linea.getArrayDocumentos().get(0).getTipoDocumento())&&(acronimoTipoMovimiento.equals("")||acronimoTipoMovimiento.equals(tipo.getTipoMovimiento())))
			{
				unidadFuncEstandar = tipo.getUnidadFuncionalEstandar();
				
			}
		}
		
		if(unidadFuncEstandar.equals(ConstantesBD.codigoNuncaValido+""))
		{
			unidadFuncEstandar = "";
		}
		
		lineaDet.getArrayCampos().get(posicion).setValor(unidadFuncEstandar);
		//Se revisó documento y no es necesario generar inconsistencia es un campo no requerido
		/*if(lineaDet.getArrayCampos().get(posicion).getValor().equals(""))
		{
			if(!parametrizacion.isHuboInconsistenciaUnidadFuncionalEstandar(linea.getArrayDocumentos().get(0).getTipoDocumento()))
			{
				parametrizacion.setHuboInconsistenciaUnidadFuncionalEstandar(linea.getArrayDocumentos().get(0).getTipoDocumento());
				lineaDet.setExisteInconsistencia(true);
				lineaDet.getArrayCampos().get(posicion).setInconsistencia(true);
				lineaDet.getArrayCampos().get(posicion).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
				lineaDet.getArrayCampos().get(posicion).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
				lineaDet.getArrayCampos().get(posicion).setTipoInconsistencia(TipoInconsistencia.DATOS);
				lineaDet.getArrayCampos().get(posicion).setDescripcionIncon("Falta definir informacion campo Unidad Funcional Estandar para el Tipo de Documento "+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+" en parametrizacion interfaz.");
			}
		}*/
		if(lineaDet.getArrayCampos().get(posicion).getValor().length()>2)
		{
			//Si el campo supera el tamaño se trunca
			lineaDet.getArrayCampos().get(posicion).setValor(lineaDet.getArrayCampos().get(posicion).getValor().substring(0, 2));
		}
	}
	
	
	private static void asignarUnidadFuncionalEstandarParamGeneral(
			DtoInterfazLineaS1E lineaDet,
			DtoInterfazS1EInfo parametrizacion,
			int posicion,
			DtoInterfazLineaS1E linea, String acronimoTipoMovimiento)
	{
		///Se busca el indicativo de Unidad Funcional Estandar que aplica para el tipo de documento
		String unidadFuncEstandar = "";
		
		
		
		for(DtoTiposInterfazDocumentosParam1E tipo:parametrizacion.getArrayListDtoTiposDocumentos())
		{
			
			
			
			if(tipo.getTipoDocumento().equals(linea.getArrayDocumentos().get(0).getTipoDocumento())&&(acronimoTipoMovimiento.equals("")||acronimoTipoMovimiento.equals(tipo.getTipoMovimiento())))
			{
				unidadFuncEstandar = tipo.getUnidadFuncionalEstandarParamGen();
				
			}
		}
		
		if(unidadFuncEstandar.equals(ConstantesBD.codigoNuncaValido+""))
		{
			unidadFuncEstandar = "";
		}
		
		lineaDet.getArrayCampos().get(posicion).setValor(unidadFuncEstandar);
		
		if(lineaDet.getArrayCampos().get(posicion).getValor().length()>2)
		{
			//Si el campo supera el tamaño se trunca
			lineaDet.getArrayCampos().get(posicion).setValor(lineaDet.getArrayCampos().get(posicion).getValor().substring(0, 2));
		}
	}
	
	/**
	 * Método usado paera asignar la unidad funcional estandar de la paramtrizacion de la interfaz 1E
	 * @param linea
	 * @param parametrizacion
	 * @param posicion
	 * @param codigoTipoDocumento
	 */
	private static void asignarConceptoFlujoEfectivoCXP(
			DtoInterfazLineaS1E lineaDet,
			DtoInterfazS1EInfo parametrizacion,
			int posicion,
			DtoInterfazLineaS1E linea)
	{
		
		lineaDet.getArrayCampos().get(posicion).setValor(parametrizacion.getCodConFlEfeMovCxp());
		//Se revisó documento y no es necesario generar inconsistencia es un campo no requerido
		if(lineaDet.getArrayCampos().get(posicion).getValor().equals(""))
		{
			if(!parametrizacion.isHuboInconsistenciaCodConFlEfeMovCxp())
			{
				parametrizacion.setHuboInconsistenciaCodConFlEfeMovCxp(true);
				lineaDet.setExisteInconsistencia(true);
				lineaDet.getArrayCampos().get(posicion).setInconsistencia(true);
				lineaDet.getArrayCampos().get(posicion).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
				lineaDet.getArrayCampos().get(posicion).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
				lineaDet.getArrayCampos().get(posicion).setTipoInconsistencia(TipoInconsistencia.DATOS);
				lineaDet.getArrayCampos().get(posicion).setDescripcionIncon("Falta definir informacion campo Concepto flujo efectivo CXP en parametrizacion interfaz.");
			}
		}
		if(lineaDet.getArrayCampos().get(posicion).getValor().length()>10)
		{
			//Si el campo supera el tamaño se trunca
			lineaDet.getArrayCampos().get(posicion).setValor(lineaDet.getArrayCampos().get(posicion).getValor().substring(0, 2));
		}
	}
	
	/**
	 * Método usado paera asignar la unidad funcional estandar de la paramtrizacion de la interfaz 1E
	 * @param linea
	 * @param parametrizacion
	 * @param posicion
	 * @param requerido 
	 * @param indTipoDocumento 
	 * @param codigoTipoDocumento
	 */
	private static void asignarTipoDocCruzeParametrizacion(DtoInterfazLineaS1E lineaMov,DtoInterfazS1EInfo parametrizacion,int posicion,DtoInterfazLineaS1E linea, boolean requerido, String indTipoDocumento)
	{
		///Se busca el tipo de dic cruce que aplica para el tipo de documento
		String tipoDocCruce = "";
		for(DtoTiposInterfazDocumentosParam1E tipo:parametrizacion.getArrayListDtoTiposDocumentos())
		{
			if(tipo.getTipoDocumento().equals(linea.getArrayDocumentos().get(0).getTipoDocumento())
					&& tipo.getIndTipoDocumento().equals(indTipoDocumento))
			{
				tipoDocCruce = tipo.getTipoDocumentoCruce();
			}
		}
		
		lineaMov.getArrayCampos().get(posicion).setValor(tipoDocCruce);
		if(lineaMov.getArrayCampos().get(posicion).getValor().equals("")&&requerido)
		{
			if(!parametrizacion.isHuboInconsistenciaTipoDocCruce(linea.getArrayDocumentos().get(0).getTipoDocumento()))
			{
				logger.info("inconsistencia ==================================00");
				parametrizacion.setHuboInconsistenciaUnidadFuncionalEstandar(linea.getArrayDocumentos().get(0).getTipoDocumento());
				lineaMov.setExisteInconsistencia(true);
				lineaMov.getArrayCampos().get(posicion).setInconsistencia(true);
				lineaMov.getArrayCampos().get(posicion).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
				lineaMov.getArrayCampos().get(posicion).setConsecutivoDocumento(linea.getArrayDocumentos().get(0).getConsecutivoDocumento());
				lineaMov.getArrayCampos().get(posicion).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
				lineaMov.getArrayCampos().get(posicion).setTipoInconsistencia(TipoInconsistencia.DATOS);
				lineaMov.getArrayCampos().get(posicion).setDescripcionIncon("Falta definir informacion campo Tipo Documento Cruce para el Tipo de Documento "+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+" en parametrizacion interfaz.");
			}
		}
	}
	
	/**
	 * Método para asignar el codigo interfaz de la institucion
	 * @param linea
	 * @param parametrizacion
	 * @param posicion
	 */
	private static void asignarIdentificacionInstitucion(DtoInterfazLineaS1E linea,DtoInterfazS1EInfo parametrizacion, int posicion)
	{ 
		linea.getArrayCampos().get(posicion).setValor(parametrizacion.getInstitucionBasica().getNit());
		if(UtilidadTexto.isEmpty(linea.getArrayCampos().get(posicion).getValor()))
		{
		
			if(!parametrizacion.isHuboInconsistenciaIdentificacionInstitucion())
			{
				parametrizacion.setHuboInconsistenciaIdentificacionInstitucion(true);
				linea.setExisteInconsistencia(true);
				linea.getArrayCampos().get(posicion).setInconsistencia(true);
				linea.getArrayCampos().get(posicion).setTipoInconsistencia(TipoInconsistencia.DATOS);
				linea.getArrayCampos().get(posicion).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo Identificacion en parametrizacion institucion.");
			}
			
		}
		else if(linea.getArrayCampos().get(posicion).getValor().length()>15)
		{
			linea.getArrayCampos().get(posicion).setValor(linea.getArrayCampos().get(posicion).getValor().substring(0, 15));
		}
	}
	
	/**
	 * Método para asigna el centro de costo parametrizado en la funcionalidad 'Parametrización Interfaz Sistema1E', 
	 * campo 'Centro de costo de tesoreria' 
	 * @param linea
	 * @param parametrizacion
	 * @param posicion
	 */
	private static void asignarAuxCentroCostosParametrizacion(DtoInterfazLineaS1E linea,DtoInterfazS1EInfo parametrizacion, int posicion)
	{ 
		linea.getArrayCampos().get(posicion).setValor(parametrizacion.getCodigoInterfazCentroCostoTesoreria());
		if(UtilidadTexto.isEmpty(linea.getArrayCampos().get(posicion).getValor())) {
		
			if(!parametrizacion.isHuboInconsistenciaCentroCostoTesoreria())
			{
				parametrizacion.setHuboInconsistenciaCentroCostoTesoreria(true);
				linea.setExisteInconsistencia(true);
				linea.getArrayCampos().get(posicion).setInconsistencia(true);
				linea.getArrayCampos().get(posicion).setTipoInconsistencia(TipoInconsistencia.DATOS);
				linea.getArrayCampos().get(posicion).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo Auxiliar centros de costo para el documento: "+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+" en parametrizacion interfaz.");
			}
			
		}
		else if(linea.getArrayCampos().get(posicion).getValor().length()>15)
		{
			linea.getArrayCampos().get(posicion).setValor(linea.getArrayCampos().get(posicion).getValor().substring(0, 15));
		}
	}
	
	/**
	 * @param retencion 
	 * @param Connection con
	 * @param DtoInterfazLineaS1E linea
	 * @param DtoInterfazS1EInfo parametrizacion
	 * */
	private static ArrayList<DtoInterfazLineaS1E> generarLineaMovimientoEntidadesExternas(
			Connection con,
			DtoInterfazLineaS1E linea,
			DtoInterfazS1EInfo parametrizacion, 
			DtoRetencion retencion)
	{
		ArrayList<DtoInterfazLineaS1E> array = new ArrayList<DtoInterfazLineaS1E>();
		ArrayList<DtoInterfazLineaS1E> lineasMovAgrupada = new ArrayList<DtoInterfazLineaS1E>();
		int codigoTipoDocumento = Integer.parseInt(linea.getArrayDocumentos().get(0).getTipoDocumento());
		PreparedStatementDecorator pst = null;
		ResultSetDecorator  rs = null;
		String consulta = "";
		String observaciones = "";
		//Se calcula el número de registros a procesar según retencion
		retencion.getConceptos().size();
		
		/**
		 * ANOTACION: En este método se deben generar 2 líneas por cada documento
		 * una es  por cuenta costo y la otra por retencion
		 * 
		 */
		
		
		try
		{	
			//Según el tipo de documento se hace la consulta correspondiente
			switch(codigoTipoDocumento)
			{
			case ConstantesBD.codigoTipoDocInteAutoServicioEntSub:
			case ConstantesBD.codigoTipoDocInteAnulaAutorServicioEntSub:
				consulta = consultarDetalleAutorizacionesEntidadesSubStr;
				pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setString(1,linea.getArrayDocumentos().get(0).getNumeroDocumento());
				rs = new ResultSetDecorator(pst.executeQuery());
			break;
			case ConstantesBD.codigoTipoDocInteDespachoMed:
			case ConstantesBD.codigoTipoDocInteCargosDirectosArt:
				consulta = consultarDetalleDespachoMedicamentosStr;
				pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setInt(1,Integer.parseInt(linea.getArrayDocumentos().get(0).getNumeroDocumento()));
				rs = new ResultSetDecorator(pst.executeQuery());
			break;
			case ConstantesBD.codigoTipoDocInteDevolucionMedi:
				consulta = consultarDetalleDevolucionMedicamentosStr;
				pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setInt(1,Integer.parseInt(linea.getArrayDocumentos().get(0).getNumeroDocumento()));
				rs = new ResultSetDecorator(pst.executeQuery());
			break;
			case ConstantesBD.codigoTipoDocInteDespaPedidoInsumo:
				consulta = consultarDetalleDespachoPedidosStr;
				pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setLong(1,Long.parseLong(linea.getArrayDocumentos().get(0).getNumeroDocumento()));
				rs = new ResultSetDecorator(pst.executeQuery());
			break;
			case ConstantesBD.codigoTipoDocInteDevolPedidoInsumo:
				consulta = consultarDetalleDevolucionPedidosStr;
				pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setLong(1,Long.parseLong(linea.getArrayDocumentos().get(0).getNumeroDocumento()));
				rs = new ResultSetDecorator(pst.executeQuery());
			break;
			case ConstantesBD.codigoTipoDocInteDespachoPedidoQx:
				consulta = consultarDetalleDespachoPedidosQxStr;
				pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setInt(1,Integer.parseInt(linea.getArrayDocumentos().get(0).getNumeroDocumento()));
				rs = new ResultSetDecorator(pst.executeQuery());
			break;
			case ConstantesBD.codigoTipoDocInteDevolucionPedidoQx:
				consulta = consultarDetalleDevolucionPedidosQxStr;
				pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setLong(1,Long.parseLong(linea.getArrayDocumentos().get(0).getNumeroDocumento()));
				rs = new ResultSetDecorator(pst.executeQuery());
			break;
			case ConstantesBD.codigoTipoDocInteAnulaCargosDirectosArticulo:
				consulta = consultarDetalleAnulacionesFarmaciaStr;
				pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setLong(1,Long.parseLong(linea.getArrayDocumentos().get(1).getNumeroDocumento()));
				rs = new ResultSetDecorator(pst.executeQuery());
			break;
			}
			
			while(rs.next())
			{
				
				DtoInterfazLineaS1E lineaMov = new DtoInterfazLineaS1E(GeneracionInterfaz1E.indicadorLineaDetMovDocumentoContable);
				//**************VALIDACIÓN CAMPO NUMERO REGISTRO*******************************
				//lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_REGISTRO.getPosicion()).setValor(parametrizacion.getPosicion()+"");
				//***************VALIDACION CAMPO COMPAÑÍA****************************************
				lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.COMPANIA.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.COMPANIA.getPosicion()).getValor());
				//***************VALIDACION CAMPO CENTRO OPERACION****************************************
				lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPERACION.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getValor());
				//**************VALIDACION CAMPO TIPO DE DOCUMENTO****************************************
				lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TIPO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.TIPO_DOCUMENTO.getPosicion()).getValor());
				//*************VALIDACION CAMPO NÚMERO DE DOCUMENTO***************************************
				lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NRO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).getValor());
				//*************VALIDACION CAMPO AUXILIAR CUENTA CONTABLE**********************************
				DtoCuentaContable cuentaContable = new DtoCuentaContable();
					
				//Para autorizaciones / anulacion autorizaciones entidades subcontratadas
				if(linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteAutoServicioEntSub+"")||
						linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteAnulaAutorServicioEntSub+""))
				{
					cuentaContable = UtilidadesInterfaz.consultarCuentaContableInterfazUnidadFuncional(con, rs.getInt("consecutivo_centro_costo"), false, false, false, false, true);
					if(cuentaContable.getCodigo().equals(""))
					{
						cuentaContable = UtilidadesInterfaz.consultarCuentaContableInterfazServicio(con,rs.getInt("servicio_articulo"), rs.getInt("consecutivo_centro_costo"), false, false, true);
					}
				}
				//Para despacho medicamentes, pedidos, devoluciones, anulaciones cargos farmacia
				else
				{
					cuentaContable = UtilidadesInterfaz.consultarCuentaContableInterfazInventarios(con, rs.getInt("servicio_articulo"), rs.getInt("consecutivo_centro_costo"));
					if(cuentaContable.getCodigo().equals(""))
					{
						cuentaContable = UtilidadesInterfaz.consultarCuentaContableSubgrupoGrupoClase(con, rs.getInt("servicio_articulo"), true, false);
					}
				}
				
				lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setValor(cuentaContable.getCuentaContable());
				if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).getValor().equals(""))
				{
					lineaMov.setExisteInconsistencia(true);
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setInconsistencia(true);
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).getDescripcion());
				}
					
					
				//*************VALIDACION CAMPO TERCERO**********************************
				if(cuentaContable.isManejaTerceros())
				{
					if(!linea.getArrayCampos().get(CampoDocumentoContable.TERCERO_DOCUMENTO.getPosicion()).getValor().equals(""))
					{
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.TERCERO_DOCUMENTO.getPosicion()).getValor());
					}
					else
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).getDescripcion());
					}
					
				}
					
					
				//*************VALIDACION CAMPO CENTRO OPERACION MOVIMIENTO**********************************
				lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getValor());
					
				//*************VALIDACION CAMPO UNIDAD DE NEGOCIO**********************************
				
				lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.UNIDAD_NEGOCIO.getPosicion()).setValor(rs.getString("unidad_funcional"));
				if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.UNIDAD_NEGOCIO.getPosicion()).getValor().length()>2)
				{
					//Si el campo supera el tamaño se trunca
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.UNIDAD_NEGOCIO.getPosicion()).setValor(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.UNIDAD_NEGOCIO.getPosicion()).getValor().substring(0, 2));
				}
					
					
				//*************VALIDACION CAMPO AUXILIAR DE CENTRO DE COSTOS**********************************
				if(cuentaContable.isManejoCentrosCosto())
				{
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setValor(rs.getString("codigo_centro_costo"));
					if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).getValor().equals(""))
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+ " campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).getDescripcion());
					}
					else if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).getValor().length()>15)
					{
						//Si el campo supera el tamaño se trunca
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setValor(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.UNIDAD_NEGOCIO.getPosicion()).getValor().substring(0, 15));
					}
				}	
					
					//*************VALIDACION CAMPO VALOR DEBITO**********************************
					switch(codigoTipoDocumento)
					{
						case ConstantesBD.codigoTipoDocInteAutoServicioEntSub:
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getDouble("valor_tarifa"),"0.0000"));
							linea.setSumaDebito(linea.getSumaDebito()+rs.getDouble("valor_tarifa"));
						break;
						case ConstantesBD.codigoTipoDocInteAnulaAutorServicioEntSub:
						case ConstantesBD.codigoTipoDocInteDevolucionMedi:
						case ConstantesBD.codigoTipoDocInteDevolPedidoInsumo:
						case ConstantesBD.codigoTipoDocInteDevolucionPedidoQx:
						case ConstantesBD.codigoTipoDocInteAnulaCargosDirectosArticulo:
							
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor("0");
							
						break;
						case ConstantesBD.codigoTipoDocInteDespachoMed:
						case ConstantesBD.codigoTipoDocInteDespaPedidoInsumo:
						case ConstantesBD.codigoTipoDocInteDespachoPedidoQx:
						case ConstantesBD.codigoTipoDocInteCargosDirectosArt:
							
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getDouble("valor_tarifa"),"0.0000"));
							linea.setSumaDebito(linea.getSumaDebito()+rs.getDouble("valor_tarifa"));
							
						break;
					}
					
					
					//*************VALIDACION CAMPO VALOR CREDITO**********************************
					switch(codigoTipoDocumento)
					{
						case ConstantesBD.codigoTipoDocInteAutoServicioEntSub:
						case ConstantesBD.codigoTipoDocInteDespachoMed:
						case ConstantesBD.codigoTipoDocInteDespaPedidoInsumo:
						case ConstantesBD.codigoTipoDocInteDespachoPedidoQx:
						case ConstantesBD.codigoTipoDocInteCargosDirectosArt:
						
							
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor("0");
							
						break;
						case ConstantesBD.codigoTipoDocInteAnulaAutorServicioEntSub:
						case ConstantesBD.codigoTipoDocInteDevolucionMedi:
						case ConstantesBD.codigoTipoDocInteDevolPedidoInsumo:
						case ConstantesBD.codigoTipoDocInteDevolucionPedidoQx:
						case ConstantesBD.codigoTipoDocInteAnulaCargosDirectosArticulo:
							
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getDouble("valor_tarifa"),"0.0000"));
								linea.setSumaCredito(linea.getSumaCredito()+rs.getDouble("valor_tarifa"));
							
						break;
						
						
					}
					
					//*************VALIDACION CAMPO VALOR BASE GRAVABLE*****************************
					
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_BASE_GRAVABLE.getPosicion()).setValor("0");
					
					//*************VALIDACION CAMPO OBSERVACIONES MOVIMIENTOS*********************
					observaciones = rs.getString("observaciones")+" "+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+" "+linea.getArrayDocumentos().get(0).getNumeroDocumento();
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.OBSERVACIONES_MOVIMI.getPosicion()).setValor(observaciones);
					//**************************************************************************
					
					
					//parametrizacion.setPosicion(parametrizacion.getPosicion()+1);
					array.add(lineaMov);
					
					if(lineaMov.isExisteInconsistencia())
					{
						linea.setExisteInconsistencia(true);
					}
				
			} //Fin while resultset
			
			//*******************ASIGNACION DE LÍNEA ADICIONALES POR RETENCION ***************************************************+
			for(int i=0;i<retencion.getConceptos().size();i++)
			{
				DtoInterfazLineaS1E lineaMov = new DtoInterfazLineaS1E(GeneracionInterfaz1E.indicadorLineaDetMovDocumentoContable);
				//**************VALIDACIÓN CAMPO NUMERO REGISTRO*******************************
				//lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_REGISTRO.getPosicion()).setValor(parametrizacion.getPosicion()+"");
				//***************VALIDACION CAMPO COMPAÑÍA****************************************
				lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.COMPANIA.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.COMPANIA.getPosicion()).getValor());
				//***************VALIDACION CAMPO CENTRO OPERACION****************************************
				asignarCodigoCentroAtencionContable(lineaMov, parametrizacion, CampoMoviDocumentoContable.CENTRO_OPERACION.getPosicion(), "");
				//**************VALIDACION CAMPO TIPO DE DOCUMENTO****************************************
				lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TIPO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.TIPO_DOCUMENTO.getPosicion()).getValor());
				//*************VALIDACION CAMPO NÚMERO DE DOCUMENTO***************************************
				lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NRO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).getValor());
				//*************VALIDACION CAMPO AUXILIAR CUENTA CONTABLE**********************************
				DtoCuentaContable cuentaContable = new DtoCuentaContable();
				
				cuentaContable = retencion.getConceptos().get(i).getCuentaRetencion();
				if(!cuentaContable.getCodigo().equals(""))
				{
					cuentaContable = UtilidadesInterfaz.consultarDatosCuentaContable(con, cuentaContable);
				}
				
				lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setValor(cuentaContable.getCuentaContable());
				if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).getValor().equals(""))
				{
					lineaMov.setExisteInconsistencia(true);
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setInconsistencia(true);
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).getDescripcion());
				}
				
				//*************VALIDACION CAMPO TERCERO**********************************
				if(cuentaContable.isManejaTerceros())
				{
					if(!linea.getArrayCampos().get(CampoDocumentoContable.TERCERO_DOCUMENTO.getPosicion()).getValor().equals(""))
					{
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.TERCERO_DOCUMENTO.getPosicion()).getValor());
					}
					else
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).getDescripcion());
					}
					
				}
				
				//*************VALIDACION CAMPO CENTRO OPERACION MOVIMIENTO**********************************
				lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getValor());
				
				///*************VALIDACION CAMPO UNIDAD DE NEGOCIO**********************************
				asignarUnidadFuncionalEstandar(lineaMov,parametrizacion,CampoMoviDocumentoContable.UNIDAD_NEGOCIO.getPosicion(),linea,"");
				
				//*************VALIDACION CAMPO VALOR DEBITO**********************************
				switch(codigoTipoDocumento)
				{
					
					case ConstantesBD.codigoTipoDocInteAnulaAutorServicioEntSub:
					case ConstantesBD.codigoTipoDocInteDevolucionMedi:
					case ConstantesBD.codigoTipoDocInteDevolPedidoInsumo:
					case ConstantesBD.codigoTipoDocInteDevolucionPedidoQx:
					case ConstantesBD.codigoTipoDocInteAnulaCargosDirectosArticulo:
						
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(retencion.getConceptos().get(i).getValorRetencion(),"0.0000"));
						linea.setSumaDebito(linea.getSumaDebito()+Utilidades.convertirADouble(retencion.getConceptos().get(i).getValorRetencion()));
						
					break;
					case ConstantesBD.codigoTipoDocInteAutoServicioEntSub:
					case ConstantesBD.codigoTipoDocInteDespachoMed:
					case ConstantesBD.codigoTipoDocInteDespaPedidoInsumo:
					case ConstantesBD.codigoTipoDocInteDespachoPedidoQx:
					case ConstantesBD.codigoTipoDocInteCargosDirectosArt:
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor("0");
						
					break;
				}
				
				
				//*************VALIDACION CAMPO VALOR CREDITO**********************************
				switch(codigoTipoDocumento)
				{
					case ConstantesBD.codigoTipoDocInteAutoServicioEntSub:
					case ConstantesBD.codigoTipoDocInteDespachoMed:
					case ConstantesBD.codigoTipoDocInteDespaPedidoInsumo:
					case ConstantesBD.codigoTipoDocInteDespachoPedidoQx:
					case ConstantesBD.codigoTipoDocInteCargosDirectosArt:
					
						
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(retencion.getConceptos().get(i).getValorRetencion(),"0.0000"));
						linea.setSumaCredito(linea.getSumaCredito()+Utilidades.convertirADouble(retencion.getConceptos().get(i).getValorRetencion()));
						
					break;
					case ConstantesBD.codigoTipoDocInteAnulaAutorServicioEntSub:
					case ConstantesBD.codigoTipoDocInteDevolucionMedi:
					case ConstantesBD.codigoTipoDocInteDevolPedidoInsumo:
					case ConstantesBD.codigoTipoDocInteDevolucionPedidoQx:
					case ConstantesBD.codigoTipoDocInteAnulaCargosDirectosArticulo:
						
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor("0");
						
					break;
					
					
				}
				
				//*************VALIDACION CAMPO VALOR BASE GRAVABLE*****************************
				lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_BASE_GRAVABLE.getPosicion()).setValor(retencion.getConceptos().get(i).getValorBaseGravable());
				
				
				//*************VALIDACION CAMPO OBSERVACIONES MOVIMIENTOS*********************
				lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.OBSERVACIONES_MOVIMI.getPosicion()).setValor(observaciones);
				//**************************************************************************
				
				
				//parametrizacion.setPosicion(parametrizacion.getPosicion()+1);
				array.add(lineaMov);
				
				if(lineaMov.isExisteInconsistencia())
				{
					linea.setExisteInconsistencia(true);
				}	
				
			}
			//***********************************************************************************************************************
			
			
			if(!huboLineaDetMov && array.size() > 0)
			{	
				huboLineaDetMov = true;
				logger.info("..:Genero "+array.size()+" de linea "+DtoInterfazCampoS1E.getDescripcionLinea(GeneracionInterfaz1E.indicadorLineaDetMovDocumentoContable));
			}
			
			pst.close();
			rs.close();
			
			//************AGRUPAR EL DETALLE**************************************************
			lineasMovAgrupada = agruparLineasMovimientoContable(array,parametrizacion);
			//**********************************************************************************
			
			if(lineasMovAgrupada.size() <= 0 && array.size() > 0)
			{
				linea.setExisteInconsistencia(true);
				linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).setInconsistencia(true);
				linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
				linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
				linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
				linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).setDescripcionIncon("Valores en cero para los campos Credito y/o Debito ");
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en generarLineaMovimientoRecaudoErr: "+e+" >> "+consulta);
		}
		
		return lineasMovAgrupada;
		
	}
	
	/**
	 * Método implementado para agrupar las líneas del movimiento contable
	 * @param array
	 * @return
	 */
	private static ArrayList<DtoInterfazLineaS1E> agruparLineasMovimientoContable(ArrayList<DtoInterfazLineaS1E> array, DtoInterfazS1EInfo parametrizacion)
	{
		ArrayList<DtoInterfazLineaS1E> lineasMovAgrupada = new ArrayList<DtoInterfazLineaS1E>();
		for(int i=0;i<array.size();i++)
		{
			
			double totalD = 0;
			double totalC = 0;
			double totalGravable = 0;
			
			totalD += Utilidades.convertirADouble(array.get(i).getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).getValor(), true);
			totalC += Utilidades.convertirADouble(array.get(i).getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).getValor(), true);
			totalGravable += Utilidades.convertirADouble(array.get(i).getArrayCampos().get(CampoMoviDocumentoContable.VALOR_BASE_GRAVABLE.getPosicion()).getValor(), true);
			
			if(!array.get(i).isRepetido())
			{
				for(int j=(array.size()-1);j>i;j--)
				{	
					if
					(
						//Igual cuenta contable...
						array.get(i).getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).getValor().equals(array.get(j).getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).getValor())
						&&
						//Igual  tercero
						array.get(i).getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).getValor().equals(array.get(j).getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).getValor())
						&&
						//Igual Unidad de negocio
						array.get(i).getArrayCampos().get(CampoMoviDocumentoContable.UNIDAD_NEGOCIO.getPosicion()).getValor().equals(array.get(j).getArrayCampos().get(CampoMoviDocumentoContable.UNIDAD_NEGOCIO.getPosicion()).getValor())
						&&
						//Igual  auxiliar de centro de costo
						array.get(i).getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).getValor().equals(array.get(j).getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).getValor())
						&&
						//Igual observaciones de movimiento
						array.get(i).getArrayCampos().get(CampoMoviDocumentoContable.OBSERVACIONES_MOVIMI.getPosicion()).getValor().equals(array.get(j).getArrayCampos().get(CampoMoviDocumentoContable.OBSERVACIONES_MOVIMI.getPosicion()).getValor())
						&&
						//Igual tipo de documento banco
						array.get(i).getArrayCampos().get(CampoMoviDocumentoContable.TIPO_DOCUMENTO_BANCO.getPosicion()).getValor().equals(array.get(j).getArrayCampos().get(CampoMoviDocumentoContable.TIPO_DOCUMENTO_BANCO.getPosicion()).getValor())
						&&
						//Igual número documento de banco
						array.get(i).getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_DOCUMENTO_BANCO.getPosicion()).getValor().equals(array.get(j).getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_DOCUMENTO_BANCO.getPosicion()).getValor())
						//Se valida que los valores de las líneas a agrupar sean de valores débitos o créditos (ambos débito o ambos crédito) mayores a cero
						&&
						((Utilidades.convertirADouble(array.get(i).getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).getValor()) > 0 
								&& Utilidades.convertirADouble(array.get(j).getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).getValor()) > 0) ||
						(Utilidades.convertirADouble(array.get(i).getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).getValor()) > 0 
								&& Utilidades.convertirADouble(array.get(j).getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).getValor()) > 0))
					)
					{	
						array.get(j).setRepetido(true);
						totalD += Utilidades.convertirADouble(array.get(j).getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).getValor(), true);
						totalC += Utilidades.convertirADouble(array.get(j).getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).getValor(), true);
						totalGravable += Utilidades.convertirADouble(array.get(j).getArrayCampos().get(CampoMoviDocumentoContable.VALOR_BASE_GRAVABLE.getPosicion()).getValor(), true);
						
						
					}
				}
				
				if(totalD>0)
				{
					DtoInterfazLineaS1E nuevaLinea = new DtoInterfazLineaS1E(GeneracionInterfaz1E.indicadorLineaDetMovDocumentoContable);
					nuevaLinea = array.get(i);
					nuevaLinea.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(totalD,"0.0000"));
					nuevaLinea.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor("0");
					nuevaLinea.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_BASE_GRAVABLE.getPosicion()).setValor(UtilidadTexto.formatearValores(totalGravable,"0.0000"));
					nuevaLinea.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_REGISTRO.getPosicion()).setValor(parametrizacion.getPosicion()+"");					
					parametrizacion.setPosicion(parametrizacion.getPosicion()+1);
			
					
					lineasMovAgrupada.add(nuevaLinea);
				}
				if(totalC>0)
				{
					DtoInterfazLineaS1E nuevaLinea = new DtoInterfazLineaS1E(GeneracionInterfaz1E.indicadorLineaDetMovDocumentoContable);
					nuevaLinea = array.get(i);
					nuevaLinea.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor("0");
					nuevaLinea.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(totalC,"0.0000"));
					nuevaLinea.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_BASE_GRAVABLE.getPosicion()).setValor(UtilidadTexto.formatearValores(totalGravable,"0.0000"));
					nuevaLinea.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_REGISTRO.getPosicion()).setValor(parametrizacion.getPosicion()+"");
					parametrizacion.setPosicion(parametrizacion.getPosicion()+1);
					
					lineasMovAgrupada.add(nuevaLinea);
				}
			}
			
			array.get(i).setRepetido(true);
		}
		
		return lineasMovAgrupada;
	}
	
	/**
	 * @param retencion 
	 * @param Connection con
	 * @param DtoInterfazLineaS1E linea
	 * @param DtoInterfazS1EInfo parametrizacion
	 * */
	private static ArrayList<DtoInterfazLineaS1E> generarLineaMovimientoEntidadesExternasCuentasXPagar(
			Connection con,
			DtoInterfazLineaS1E linea,
			DtoInterfazS1EInfo parametrizacion, 
			DtoRetencion retencion)
	{
		ArrayList<DtoInterfazLineaS1E> array = new ArrayList<DtoInterfazLineaS1E>();
		DtoCuentaContable cuentaContable = new DtoCuentaContable();
		PreparedStatementDecorator pst = null;
		ResultSetDecorator rs = null;
		int codigoTipoDocumento = Integer.parseInt(linea.getArrayDocumentos().get(0).getTipoDocumento());
		
		try
		{
			//DEPENDIENDO DEL TIPO DE DOCUMENTO SE REALIZA LA CONSULTA ESPECÍFICA
			switch(codigoTipoDocumento)
			{
			case ConstantesBD.codigoTipoDocInteAutoServicioEntSub:
			case ConstantesBD.codigoTipoDocInteAnulaAutorServicioEntSub:
				pst = new PreparedStatementDecorator(con.prepareStatement(consultarDetalleAutorizacionesEntidadesSub02Str,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setString(1,linea.getArrayDocumentos().get(0).getNumeroDocumento());
				rs = new ResultSetDecorator(pst.executeQuery());
			break;
			case ConstantesBD.codigoTipoDocInteDespachoMed:
			
				pst = new PreparedStatementDecorator(con.prepareStatement(consultarDetalleDespachoMedicamentos02Str,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setString(1,linea.getArrayDocumentos().get(0).getNumeroDocumento());
				rs = new ResultSetDecorator(pst.executeQuery());
			break;
			case ConstantesBD.codigoTipoDocInteCargosDirectosArt:
				pst = new PreparedStatementDecorator(con.prepareStatement(consultarDetalleDespachoMedicamentos02Str,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setString(1,linea.getArrayDocumentos().get(1).getNumeroDocumento());
				rs = new ResultSetDecorator(pst.executeQuery());
			break;
			case ConstantesBD.codigoTipoDocInteDespaPedidoInsumo:
			case ConstantesBD.codigoTipoDocInteDespachoPedidoQx:
				pst = new PreparedStatementDecorator(con.prepareStatement(consultarDetalleDespachoPedidos02Str,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setString(1,linea.getArrayDocumentos().get(0).getNumeroDocumento());
				rs = new ResultSetDecorator(pst.executeQuery());
			break;
			case ConstantesBD.codigoTipoDocInteDevolucionMedi:
				pst = new PreparedStatementDecorator(con.prepareStatement(consultarDetalleDevolucionMedicamentos02Str,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setString(1,linea.getArrayDocumentos().get(0).getNumeroDocumento());
				rs = new ResultSetDecorator(pst.executeQuery());
			break;
			case ConstantesBD.codigoTipoDocInteDevolPedidoInsumo:
				pst = new PreparedStatementDecorator(con.prepareStatement(consultarDetalleDevolucionPedido02Str,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setString(1,linea.getArrayDocumentos().get(0).getNumeroDocumento());
				rs = new ResultSetDecorator(pst.executeQuery());
			break;
			case ConstantesBD.codigoTipoDocInteDevolucionPedidoQx:
				pst = new PreparedStatementDecorator(con.prepareStatement(consultarDetalleDevolucionPedidosQx02Str,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setString(1,linea.getArrayDocumentos().get(0).getNumeroDocumento());
				rs = new ResultSetDecorator(pst.executeQuery());
			break;
			case ConstantesBD.codigoTipoDocInteAnulaCargosDirectosArticulo:
				pst = new PreparedStatementDecorator(con.prepareStatement(consultarDetalleAnulacionesFarmacia02Str,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setString(1,linea.getArrayDocumentos().get(1).getNumeroDocumento());
				rs = new ResultSetDecorator(pst.executeQuery());
			break;
				
			}
			
			
			
			while(rs.next())
			{
				DtoInterfazLineaS1E lineaMov = new DtoInterfazLineaS1E(GeneracionInterfaz1E.indicadorLineaDetCxP);
				
				//**************VALIDACIÓN CAMPO NUMERO REGISTRO*******************************
				lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.NUMERO_REGISTRO.getPosicion()).setValor(parametrizacion.getPosicion()+"");
				//***************VALIDACION CAMPO COMPAÑÍA****************************************
				lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.COMPANIA.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.COMPANIA.getPosicion()).getValor());
				//***************VALIDACION CAMPO CENTRO OPERACION****************************************
				lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.CENTRO_OPERACION.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getValor());
				//**************VALIDACION CAMPO TIPO DE DOCUMENTO****************************************
				lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.TIPO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.TIPO_DOCUMENTO.getPosicion()).getValor());
				//*************VALIDACION CAMPO NÚMERO DE DOCUMENTO***************************************
				lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.NRO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).getValor());
				//*************VALIDACION CAMPO AUXILIAR CUENTA CONTABLE**********************************
				cuentaContable.setCodigo(rs.getString("codigo"));
				cuentaContable.setCuentaContable(rs.getString("cuenta_contable"));
				cuentaContable.setDescripcion(rs.getString("descripcion"));
				cuentaContable.setActivo(UtilidadTexto.getBoolean(rs.getString("activo")));
				cuentaContable.setManejaTerceros(UtilidadTexto.getBoolean(rs.getString("manejo_terceros")));
				cuentaContable.setManejoCentrosCosto(UtilidadTexto.getBoolean(rs.getString("manejo_centros_costo")));
				cuentaContable.setManejoBaseGravable(UtilidadTexto.getBoolean(rs.getString("manejo_base_gravable")));
				cuentaContable.setNaturalezaCuenta(rs.getString("naturaleza_cuenta"));
				cuentaContable.setCodigoInstitucion(rs.getInt("institucion"));
				cuentaContable.setAnioVigencia(rs.getString("anio_vigencia"));
				lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.AUX_CUENT_CONTABLE.getPosicion()).setValor(cuentaContable.getCuentaContable());
				if(lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.AUX_CUENT_CONTABLE.getPosicion()).getValor().equals(""))
				{
					lineaMov.setExisteInconsistencia(true);
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.AUX_CUENT_CONTABLE.getPosicion()).setInconsistencia(true);
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.AUX_CUENT_CONTABLE.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.AUX_CUENT_CONTABLE.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.AUX_CUENT_CONTABLE.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.AUX_CUENT_CONTABLE.getPosicion()).setDescripcionIncon(mensajeEInconNoInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.AUX_CUENT_CONTABLE.getPosicion()).getDescripcion());
				}
				//******************************************************************************************
				
				//*************VALIDACION CAMPO TERCERO**********************************
				if(cuentaContable.isManejaTerceros())
				{
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.TERCERO.getPosicion()).setValor(rs.getString("id_tercero"));
					if(lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.TERCERO.getPosicion()).getValor().equals(""))
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.TERCERO.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.TERCERO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.TERCERO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.TERCERO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.TERCERO.getPosicion()).setDescripcionIncon(mensajeEInconNoInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.TERCERO.getPosicion()).getDescripcion());
					}
				}
				else
				{
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.TERCERO.getPosicion()).setValor("");
				}
				
				//*************VALIDACION CAMPO CENTRO OPERACION MOVIMIENTO**********************************
				asignarCodigoCentroAtencionContable(lineaMov, parametrizacion, CampoMoviCuentasXPagar.CENTRO_OPER_MOVI.getPosicion(), "");
				
				//*************VALIDACION CAMPO UNIDAD DE NEGOCIO**********************************
				asignarUnidadFuncionalEstandar(lineaMov,parametrizacion,CampoMoviCuentasXPagar.UNIDAD_NEGOCIO.getPosicion(),linea,"");
				//*************************************************************************************
				//**************VALIDACION CAMPO VALOR DEBITO********************************************
				//Según tipo de documento
				switch(codigoTipoDocumento)
				{
					case ConstantesBD.codigoTipoDocInteAutoServicioEntSub:
					case ConstantesBD.codigoTipoDocInteDespachoMed:
					case ConstantesBD.codigoTipoDocInteDespaPedidoInsumo:
					case ConstantesBD.codigoTipoDocInteDespachoPedidoQx:
					case ConstantesBD.codigoTipoDocInteCargosDirectosArt:
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setValor("0");
					break;
					case ConstantesBD.codigoTipoDocInteAnulaAutorServicioEntSub:
					case ConstantesBD.codigoTipoDocInteDevolucionMedi:
					case ConstantesBD.codigoTipoDocInteDevolPedidoInsumo:
					case ConstantesBD.codigoTipoDocInteDevolucionPedidoQx:
					case ConstantesBD.codigoTipoDocInteAnulaCargosDirectosArticulo:
						//Se resta el valor de la retencion
						double valorDebito = rs.getDouble("valor") - retencion.consultarTotalValorRetencion();
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(valorDebito,"0.0000"));
						if(valorDebito>0)
						{
							linea.setSumaDebito(linea.getSumaDebito()+valorDebito);
						}
						
						if(Utilidades.convertirADouble(lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).getValor())<=0)
						{
							lineaMov.setExisteInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
							lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
							lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
							if(valorDebito<0)
							{
								lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setDescripcionIncon(mensajeEInconRestaRetencion+ " campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).getDescripcion());
							}
							else
							{
								lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setDescripcionIncon(mensajeEInconNoInfo+ " campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).getDescripcion());
							}
						}
					break;
				}
				
				
				//*******************************************************************************************
				//******************VALIDACION CAMPO VALOR CREDITO**********************************************
				//Según tipo documento
				switch(codigoTipoDocumento)
				{
					case ConstantesBD.codigoTipoDocInteAnulaAutorServicioEntSub:
					case ConstantesBD.codigoTipoDocInteDevolucionMedi:
					case ConstantesBD.codigoTipoDocInteDevolPedidoInsumo:
					case ConstantesBD.codigoTipoDocInteDevolucionPedidoQx:
					case ConstantesBD.codigoTipoDocInteAnulaCargosDirectosArticulo:
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).setValor("0");
					break;
					case ConstantesBD.codigoTipoDocInteAutoServicioEntSub:
					case ConstantesBD.codigoTipoDocInteDespachoMed:
					case ConstantesBD.codigoTipoDocInteDespaPedidoInsumo:
					case ConstantesBD.codigoTipoDocInteDespachoPedidoQx:
					case ConstantesBD.codigoTipoDocInteCargosDirectosArt:
						//se resta valor de la retencion
						double valorCredito = rs.getDouble("valor") - retencion.consultarTotalValorRetencion();
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(valorCredito,"0.0000"));
						if(valorCredito>0)
						{
							linea.setSumaCredito(linea.getSumaCredito()+valorCredito);
						}
						if(Utilidades.convertirADouble(lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).getValor())<=0)
						{
							lineaMov.setExisteInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).setInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
							lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
							lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
							if(valorCredito<0)
							{
								lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).setDescripcionIncon(mensajeEInconRestaRetencion+" campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).getDescripcion());
							}
							else
							{
								lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).setDescripcionIncon(mensajeEInconNoInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).getDescripcion());
							}
						}
					break;
				}
				
				
				//************************************************************************************************
				//************************VALIDACION CAMPO OBSERVACIONES MOVIMIENTO********************************
				lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.OBSERVACIONES_MOVIMIENTO.getPosicion()).setValor(rs.getString("observacion")+" "+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+" "+linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).getValor());
				
				//*************************************************************************************************
				//**************************VALIDACION CAMPO TIPO DOCUMENTO CRUCE************************************
				asignarTipoDocCruzeParametrizacion(lineaMov, parametrizacion, CampoMoviCuentasXPagar.TIPO_DOC_CRUCE.getPosicion(), linea,false, linea.getArrayCampos().get(CampoDocumentoContable.TIPO_DOCUMENTO.getPosicion()).getValor());
				//*************************************************************************************************
				//***************************VALIDACION CAMPO NÚMERO DOCUMENTO CRUCE*********************************
				
				lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.NRO_DOC_CRUCE.getPosicion()).setValor(linea.getArrayDocumentos().get(0).getNumeroDocumento());
				
				if(lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.NRO_DOC_CRUCE.getPosicion()).getValor().equals(""))
				{
					lineaMov.setExisteInconsistencia(true);
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.NRO_DOC_CRUCE.getPosicion()).setInconsistencia(true);
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.NRO_DOC_CRUCE.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.NRO_DOC_CRUCE.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.NRO_DOC_CRUCE.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.NRO_DOC_CRUCE.getPosicion()).setDescripcionIncon(mensajeEInconNoInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.NRO_DOC_CRUCE.getPosicion()).getDescripcion());
				}
				
				
				//****************************************************************************************************
				//***************************VALIDACION CAMPO AUXILIAR CONCEPTO FLUJO EFECTIVO***************************
				asignarConceptoFlujoEfectivoCXP(lineaMov, parametrizacion, CampoMoviCuentasXPagar.AUX_CONCEP_FLUJO.getPosicion(), linea);
				//************************************************************************************************************
				//***************************VALIDACION CAMPO FECHA VENCIMIENTO DOCUMENTO******************************
				lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.FECHA_VEN_DOC.getPosicion()).setValor(UtilidadFecha.incrementarDiasAFecha(rs.getString("fecha"), rs.getInt("dias"), false));
				if(lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.FECHA_VEN_DOC.getPosicion()).getValor().equals(""))
				{
					lineaMov.setExisteInconsistencia(true);
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.FECHA_VEN_DOC.getPosicion()).setInconsistencia(true);
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.FECHA_VEN_DOC.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.FECHA_VEN_DOC.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.FECHA_VEN_DOC.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.FECHA_VEN_DOC.getPosicion()).setDescripcionIncon(mensajeEInconNoInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.FECHA_VEN_DOC.getPosicion()).getDescripcion());
				}
				//******************************************************************************************************
				//************************VALIDACION CAMPO FECHA PRONTO PAGO DOCUMENTO*************************************
				lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.FECHA_PAG_DOC.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.FECHA_DOCUMENTO.getPosicion()).getValor());
				//***********************************************************************************************************
				
				//*****************************************************************************************************************
				
				//**************************VALIDACION CAMPO OBSERVACIONES MOVIMIENTO SALDO ABIERTO************************************
				lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.OBS_SALDO_ABI.getPosicion()).setValor(lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.OBSERVACIONES_MOVIMIENTO.getPosicion()).getValor());
				if(lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.OBS_SALDO_ABI.getPosicion()).getValor().equals(""))
				{
					lineaMov.setExisteInconsistencia(true);
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.OBS_SALDO_ABI.getPosicion()).setInconsistencia(true);
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.OBS_SALDO_ABI.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.OBS_SALDO_ABI.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.OBS_SALDO_ABI.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.OBS_SALDO_ABI.getPosicion()).setDescripcionIncon(mensajeEInconNoInfo+" del beneficiario");
				}
				//***********************************************************************************************************************
				
				
				parametrizacion.setPosicion(parametrizacion.getPosicion()+1);
				array.add(lineaMov); 
			}
			
			pst.close();
			rs.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en generarLineaMovimientoEntidadesExternasCuentasXPagar: "+e);
		}
		
		
		return array;
		
	}
	
	
	
	/**
	 * Consultar Documentos Contable Recaudos
	 * @param Connection con
	 * @param DtoInterfazS1EInfo parametrizacion
	 * */
	private static ArrayList<DtoInterfazLineaS1E> consultarDocumentosContablesVentas(Connection con,DtoInterfazS1EInfo parametrizacion) //--
	{
		ArrayList<DtoInterfazLineaS1E> lineas = new ArrayList<DtoInterfazLineaS1E>();
		logger.info("\n\n\n\n\n ENTRAAAA consultarDocumentosContablesVentas>>> ");
		try
		{
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consultarDocumentosContablesVentasStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//PRIMEROS 5 REGISTROS.
			//PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement("SELECT * FROM ("+consultarDocumentosContablesVentasStr+") WHERE numero_documento01='431183' and rownum<=5", ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setString(1,UtilidadFecha.conversionFormatoFechaABD(parametrizacion.getFechaProceso()));
			pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(parametrizacion.getFechaProceso()));
			pst.setString(3,UtilidadFecha.conversionFormatoFechaABD(parametrizacion.getFechaProceso()));
			pst.setString(4,UtilidadFecha.conversionFormatoFechaABD(parametrizacion.getFechaProceso()));
			pst.setString(5,UtilidadFecha.conversionFormatoFechaABD(parametrizacion.getFechaProceso()));
			pst.setString(6,UtilidadFecha.conversionFormatoFechaABD(parametrizacion.getFechaProceso()));
			pst.setString(7,UtilidadFecha.conversionFormatoFechaABD(parametrizacion.getFechaProceso()));
			logger.info("\n\n\n\n************** COnsultaDocContables >> "+consultarDocumentosContablesVentasStr+" Fecha >>"+parametrizacion.getFechaProceso()+" **************************************");
			
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{
				DtoInterfazLineaS1E linea = new DtoInterfazLineaS1E(GeneracionInterfaz1E.indicadorLineaDocumentoContable);
				
				//***********Se asignan los posibles documentos que puede tener el movimiento****************************
				DtoInterfazDatosDocumentoS1E documento01 = new DtoInterfazDatosDocumentoS1E();
				documento01.setNumeroDocumento(rs.getString("numero_documento01"));
				documento01.setDescripcionDocumento(rs.getString("descripcion_documento01"));
				documento01.setTipoDocumento(rs.getString("tipo_documento01"));
				documento01.setCodigoTipoConsecutivo(rs.getString("tipo_consecutivo01"));
				documento01.setFecha(rs.getString("fecha01"));
				documento01.setValor(UtilidadTexto.formatearValores(rs.getString("valor")));
				linea.getArrayDocumentos().add(documento01);
				
				if(!UtilidadTexto.isEmpty(rs.getString("numero_documento02")))
				{
					DtoInterfazDatosDocumentoS1E documento02 = new DtoInterfazDatosDocumentoS1E();
					documento02.setNumeroDocumento(rs.getString("numero_documento02"));
					documento02.setDescripcionDocumento(rs.getString("descripcion_documento02"));
					documento02.setTipoDocumento(rs.getString("tipo_documento02"));
					documento02.setCodigoTipoConsecutivo(rs.getString("tipo_consecutivo02"));
					documento02.setFecha(rs.getString("fecha02"));
					documento02.setValor(UtilidadTexto.formatearValores(rs.getString("valor")));
					linea.getArrayDocumentos().add(documento02);
				}
				//*******************************************************************************************************
				//if(linea.getArrayDocumentos().get(0).getNumeroDocumento().equals("38382"))
					logger.info("NUMERO DOCUMENTO: "+linea.getArrayDocumentos().get(0).getNumeroDocumento());
				/*******************CAMPOS DE LA LÍNEA DOCUMENTO CONTABLE***************************************************/
				
				//**************VALIDACIÓN CAMPO NUMERO REGISTRO*******************************
				linea.getArrayCampos().get(CampoDocumentoContable.NUMERO_REGISTRO.getPosicion()).setValor(parametrizacion.getPosicion()+"");
				//*******************************************************************************
				//***************VALIDACION CAMPO COMPAÑÍA****************************************
				asignarCodigoInterfazInstitucion(linea, parametrizacion, CampoDocumentoContable.COMPANIA.getPosicion());
				//**********************************************************************************
				//***************VALIDACION CAMPO CENTRO OPERACION****************************************
				linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).setValor(rs.getString("codigo_centro_atencion"));
				if(linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getValor().equals(""))
				{
					linea.setExisteInconsistencia(true);
					linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).setInconsistencia(true);
					linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
					linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
					linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
					linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).setDescripcionIncon(mensajeEInconNoInfo+" campo "+linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getDescripcion());
				}
				//**********************************************************************************
				//**************VALIDACION CAMPO TIPO DE DOCUMENTO************************************
				if(linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteFacturaPaciente+"")||
						linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente+""))
				{
					asignarIndTipoDocumento(linea, parametrizacion, CampoDocumentoContable.TIPO_DOCUMENTO.getPosicion(), linea.getArrayDocumentos().get(0).getTipoDocumento(),ConstantesIntegridadDominio.acronimoVentas);
				}
				else
				{
					asignarIndTipoDocumento(linea, parametrizacion, CampoDocumentoContable.TIPO_DOCUMENTO.getPosicion(), linea.getArrayDocumentos().get(0).getTipoDocumento(),"");
				}
				//*************************************************************************************
				//*************VALIDACION CAMPO NÚMERO DE DOCUMENTO**************************************
				if(linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteFacturaPaciente+"")||
						linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente+""))
				{
					asignarNumeroDocumento(linea, parametrizacion, CampoDocumentoContable.NRO_DOCUMENTO.getPosicion(), linea.getArrayDocumentos().get(0).getTipoDocumento(),ConstantesIntegridadDominio.acronimoVentas,"");
				}
				else
				{
					asignarNumeroDocumento(linea, parametrizacion, CampoDocumentoContable.NRO_DOCUMENTO.getPosicion(), linea.getArrayDocumentos().get(0).getTipoDocumento(),"","");
				}
				//****************************************************************************************
				//***************VALIDACION CAMPO FECHA DOCUMNENTO*****************************************
				linea.getArrayCampos().get(CampoDocumentoContable.FECHA_DOCUMENTO.getPosicion()).setValor(parametrizacion.getFechaProceso());
				//*******************************************************************************************
				///***************VALIDACION CAMPO TERCERO DOCUMENTO*****************************************
				asignarTerceroDocumento(con, linea, parametrizacion, CampoDocumentoContable.TERCERO_DOCUMENTO.getPosicion());
				//*******************************************************************************************
				//******************VALIDACION CAMPO OBSERVACIONES ENCABEZADO************************************
				asignarObservacionesDocumento(linea, parametrizacion, CampoDocumentoContable.OBSERVACIONES_DOCUMENTO.getPosicion(), linea.getArrayDocumentos().get(0).getTipoDocumento());
				//************************************************************************************************
				
				parametrizacion.setPosicion(parametrizacion.getPosicion()+1);
				
				
				
				/************CALCULO DE LA RETENCION*********************************************************************************/
				DtoRetencion retencion = UtilidadesInterfaz.calcularRetencionInterfaz(con, linea.getArrayDocumentos(), parametrizacion, linea.getArrayCampos().get(CampoDocumentoContable.TERCERO_DOCUMENTO.getPosicion()).getValor(), false,0); //autoretencion
				
				for(String mensajeIncon:retencion.getInconsistenciasRetencion())
				{
					linea.setExisteInconsistencia(true);
					linea.getArrayCampos().get(CampoDocumentoContable.NUMERO_REGISTRO.getPosicion()).setInconsistencia(true);
					linea.getArrayCampos().get(CampoDocumentoContable.NUMERO_REGISTRO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
					linea.getArrayCampos().get(CampoDocumentoContable.NUMERO_REGISTRO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
					linea.getArrayCampos().get(CampoDocumentoContable.NUMERO_REGISTRO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
					linea.getArrayCampos().get(CampoDocumentoContable.NUMERO_REGISTRO.getPosicion()).setDescripcionIncon(mensajeIncon);
				}
	
				/*******************CAMPOS DE LA LÍNEA MOVIMIENTO DOCUMENTO CONTABLE***************************************************/
				
				ArrayList<DtoInterfazLineaS1E> array = new ArrayList<DtoInterfazLineaS1E>();
				array = generarLineaMovimientoVentas(con,linea,parametrizacion,retencion);//--lineaMvtoVentas
				
				linea.getArrayDetalle().addAll(array);
				
				if(array.size()<=0)
					logger.info("valor de posicion no creo detalle LMDC>> "+parametrizacion.getPosicion());
				
				/*******************CAMPOS DE LA LÍNEA MOVIMIENTO CUENTAS X COBRAR*******************************************************/
				array = new ArrayList<DtoInterfazLineaS1E>();
				array = generarLineaMovimientoVentasCuentasXCobrar(con,linea,parametrizacion);
				
				linea.getArrayDetalle().addAll(array);
				
				/*if(array.size()<=0)
					logger.info("valor de posicion no creo detalle LMVCXC>> "+parametrizacion.getPosicion());*/
				
				
				//***********VALIDACION DE LAS SUMAS IGUALES****************************************************
				//sumas iguales en los credito y debito
				if((Math.round(linea.getSumaDebito()*Math.pow(10,8))/Math.pow(10,8))-(Math.round(linea.getSumaCredito()*Math.pow(10,8))/Math.pow(10,8)) != 0)
				{	
					
					linea.setExisteInconsistencia(true);
					
					linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).setInconsistencia(true);
					linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
					linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
					linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.SUMAS_IGUALES);
					linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).setDescripcionIncon("Los totales son diferentes D: "+UtilidadTexto.formatearValores(linea.getSumaDebito())+" C: "+UtilidadTexto.formatearValores(linea.getSumaCredito()));
				}
				
				if(linea.isExisteInconsistencia())
					huboInconsistencia = true;
				
				if(!huboInconsistencia)
				{
					//No se actualizan estos registros porque los numeros de documento son los mismos utilizados para la generacion de la linea de honorarios 
					if(!documento01.getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteFacturaPaciente+"") && 
							!documento01.getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente+""))
					actualizarRegistroMarcado(con,documento01.getTipoDocumento(),documento01.getNumeroDocumento());
				}
				
				lineas.add(linea);
			}
			
			rs.close();
			pst.close();
			
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarDocumentosContablesVentasHonorarios: "+e+" >> "+consultarDocumentosContablesVentasStr+" "+UtilidadFecha.conversionFormatoFechaABD(parametrizacion.getFechaProceso()) );
		}
		
		return lineas;
	}
	
	/**
	 * Consultar Documentos Contable Recaudos
	 * @param Connection con
	 * @param DtoInterfazS1EInfo parametrizacion
	 * */
	private static ArrayList<DtoInterfazLineaS1E> consultarDocumentosContablesHonorarios(Connection con,DtoInterfazS1EInfo parametrizacion) 
	{
		ArrayList<DtoInterfazLineaS1E> lineas = new ArrayList<DtoInterfazLineaS1E>();
		boolean existenDat = false;
		logger.info("\n\n\n\n\n ENTRAAAAAAAA consultarDocumentosContablesHonorarios >>> ");
		try
		{
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consultarDocumentosContablesHonorariosStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//para los primeros 5 registros.
			//PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement("select * from ("+consultarDocumentosContablesHonorariosStr+") where numero_documento01='431183' AND rownum<=5", ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setString(1,UtilidadFecha.conversionFormatoFechaABD(parametrizacion.getFechaProceso()));
			pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(parametrizacion.getFechaProceso()));
			logger.info("\n\n\n CONSULTA SQL consultarDocumentosContablesHonorariosStr >>"+consultarDocumentosContablesHonorariosStr+"    \n >>fehca "+UtilidadFecha.conversionFormatoFechaABD(parametrizacion.getFechaProceso()));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			//logger.info("CONSULTA DE DOCUMENTOS HONORARIOS: "+consultarDocumentosContablesHonorariosStr.replace("?","'"+UtilidadFecha.conversionFormatoFechaABD(parametrizacion.getFechaProceso())+"'"));
			while(rs.next())
			{
				//logger.info("Encontró numero de documento: "+rs.getString("numero_documento01"));
				existenDat = false;
				DtoInterfazLineaS1E linea = new DtoInterfazLineaS1E(GeneracionInterfaz1E.indicadorLineaDocumentoContable);
				
				//***********Se asignan los posibles documentos que puede tener el movimiento****************************
				DtoInterfazDatosDocumentoS1E documento01 = new DtoInterfazDatosDocumentoS1E();
				documento01.setNumeroDocumento(rs.getString("numero_documento01"));
				documento01.setDescripcionDocumento(rs.getString("descripcion_documento01"));
				documento01.setTipoDocumento(rs.getString("tipo_documento01"));
				documento01.setCodigoTipoConsecutivo(rs.getString("tipo_consecutivo01"));
				documento01.setFecha(rs.getString("fecha01"));
				linea.getArrayDocumentos().add(documento01);
				
				if(!UtilidadTexto.isEmpty(rs.getString("numero_documento02")))
				{
					DtoInterfazDatosDocumentoS1E documento02 = new DtoInterfazDatosDocumentoS1E();
					documento02.setNumeroDocumento(rs.getString("numero_documento02"));
					documento02.setDescripcionDocumento(rs.getString("descripcion_documento02"));
					documento02.setTipoDocumento(rs.getString("tipo_documento02"));
					documento02.setCodigoTipoConsecutivo(rs.getString("tipo_consecutivo02"));
					documento02.setFecha(rs.getString("fecha02"));
					linea.getArrayDocumentos().add(documento02);
				}
				//*******************************************************************************************************
				
				 
				//if(linea.getArrayDocumentos().get(0).getNumeroDocumento().equals("18535")||linea.getArrayDocumentos().get(0).getNumeroDocumento().equals("105"))
					logger.info("NUMERO DOCUMENTO: "+linea.getArrayDocumentos().get(0).getNumeroDocumento());
				
				/*******************CAMPOS DE LA LÍNEA DOCUMENTO CONTABLE***************************************************/
				
				//**************VALIDACIÓN CAMPO NUMERO REGISTRO*******************************				
				linea.getArrayCampos().get(CampoDocumentoContable.NUMERO_REGISTRO.getPosicion()).setValor(parametrizacion.getPosicion()+"");
				//*******************************************************************************
				//***************VALIDACION CAMPO COMPAÑÍA****************************************
				asignarCodigoInterfazInstitucion(linea, parametrizacion, CampoDocumentoContable.COMPANIA.getPosicion());
				//**********************************************************************************
				//***************VALIDACION CAMPO CENTRO OPERACION****************************************
				linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).setValor(rs.getString("codigo_centro_atencion"));
				if(linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getValor().equals(""))
				{
					logger.info("inconsistencia ==============================================");
					linea.setExisteInconsistencia(true);
					linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).setInconsistencia(true);
					linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
					linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
					linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
					linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).setDescripcionIncon(mensajeEInconNoInfo+" campo "+linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getDescripcion());
				}
				//**********************************************************************************
				//**************VALIDACION CAMPO TIPO DE DOCUMENTO************************************
				asignarIndTipoDocumento(linea, parametrizacion, CampoDocumentoContable.TIPO_DOCUMENTO.getPosicion(), linea.getArrayDocumentos().get(0).getTipoDocumento(),ConstantesIntegridadDominio.acronimoHonorarios);
				//logger.info("¿INCONSISTENCIA DE TIPO DOCUMENTO? "+linea.getArrayCampos().get(CampoDocumentoContable.TIPO_DOCUMENTO.getPosicion()).isInconsistencia());
				//*************************************************************************************
				//*************VALIDACION CAMPO NÚMERO DE DOCUMENTO**************************************
				asignarNumeroDocumento(linea, parametrizacion, CampoDocumentoContable.NRO_DOCUMENTO.getPosicion(), linea.getArrayDocumentos().get(0).getTipoDocumento(),ConstantesIntegridadDominio.acronimoHonorarios,"");
				//logger.info("¿INCONSISTENCIA DE NUMERO DOCUMENTO? "+linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).isInconsistencia());
				//****************************************************************************************
				//***************VALIDACION CAMPO FECHA DOCUMNENTO*****************************************
				linea.getArrayCampos().get(CampoDocumentoContable.FECHA_DOCUMENTO.getPosicion()).setValor(parametrizacion.getFechaProceso());
				//*******************************************************************************************
				///***************VALIDACION CAMPO TERCERO DOCUMENTO*****************************************
				asignarTerceroDocumento(con, linea, parametrizacion, CampoDocumentoContable.TERCERO_DOCUMENTO.getPosicion());
				//*******************************************************************************************
				//******************VALIDACION CAMPO OBSERVACIONES ENCABEZADO************************************
				asignarObservacionesDocumento(linea, parametrizacion, CampoDocumentoContable.OBSERVACIONES_DOCUMENTO.getPosicion(), linea.getArrayDocumentos().get(0).getTipoDocumento());
				//************************************************************************************************
				
				parametrizacion.setPosicion(parametrizacion.getPosicion()+1);
				
				
				/*******************CAMPOS DE LA LÍNEA MOVIMIENTO DOCUMENTO CONTABLE***************************************************/
				ArrayList<DtoInterfazLineaS1E> array = new ArrayList<DtoInterfazLineaS1E>();
				
				array = generarLineaMovimientoHonorarios(con, linea, parametrizacion);
				
				
				if(array.size()>0)
					existenDat = true;
				
				linea.getArrayDetalle().addAll(array);
				if(linea.getArrayDocumentos().get(0).getNumeroDocumento().equals("12581")||linea.getArrayDocumentos().get(0).getNumeroDocumento().equals("10"))
				{
					logger.info("número de líneas encontradas movimiento honorarios: "+array.size());
				}
				
				/*******************CAMPOS DE LA LÍNEA MOVIMIENTO CUENTAS X PAGAR*******************************************************/
				array = new ArrayList<DtoInterfazLineaS1E>();
				array = generarLineaMovimientoHonorariosCuentasXPagar(con,linea,parametrizacion);
				
				if(array.size()>0)
					existenDat = true;
				
				linea.getArrayDetalle().addAll(array);
				
				if(linea.getArrayDocumentos().get(0).getNumeroDocumento().equals("12581")||linea.getArrayDocumentos().get(0).getNumeroDocumento().equals("10"))
				{
					logger.info("número de líneas encontradas movimiento cuentas x pagar: "+array.size());
				}
				//logger.info("\n\nvalor sumas despues de linea movimiento CXP >> "+linea.getSumaDebito()+" >> "+UtilidadTexto.formatearValores(linea.getSumaCredito()));
				
				//***********VALIDACION DE LAS SUMAS IGUALES****************************************************

				//sumas iguales en los credito y debito
				if((Math.round(linea.getSumaDebito()*Math.pow(10,8))/Math.pow(10,8))-(Math.round(linea.getSumaCredito()*Math.pow(10,8))/Math.pow(10,8)) != 0)
				{	
					logger.info("inconsistencia ==============================================");
					linea.setExisteInconsistencia(true);
					
					linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).setInconsistencia(true);
					linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
					linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
					linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.SUMAS_IGUALES);
					linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).setDescripcionIncon("Los totales son diferentes D: "+UtilidadTexto.formatearValores(linea.getSumaDebito())+" C: "+UtilidadTexto.formatearValores(linea.getSumaCredito()));
				}
				
								
				if(existenDat)
				{
					if(linea.isExisteInconsistencia())
						huboInconsistencia = true;
					
					if(!huboInconsistencia)
						actualizarRegistroMarcado(con,documento01.getTipoDocumento(),documento01.getNumeroDocumento());
					
					lineas.add(linea);
				}
				else
				{
					
					//Se actualizan estos registros porque los numeros de documento son los mismos utilizados para la generacion de la linea de Ventas 
					if(documento01.getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteFacturaPaciente+"") || 
							documento01.getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente+""))
						actualizarRegistroMarcado(con,documento01.getTipoDocumento(),documento01.getNumeroDocumento());
					
					parametrizacion.setPosicion(parametrizacion.getPosicion()-1);
					
					//Se reversan las inconsistencias de honorarios de tipo consecutivo y tipo indicador consecutivo
					if(linea.getArrayCampos().get(CampoDocumentoContable.TIPO_DOCUMENTO.getPosicion()).isInconsistencia())
					{
						parametrizacion.eliminarHuboInconsistenciaIndTipoDocumento(linea.getArrayDocumentos().get(0).getTipoDocumento(),ConstantesIntegridadDominio.acronimoHonorarios);
					}
					if(linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).isInconsistencia())
					{
						parametrizacion.eliminarHuboInconsistenciaTipoConsecutivo(linea.getArrayDocumentos().get(0).getTipoDocumento(),ConstantesIntegridadDominio.acronimoHonorarios);
					}
				}
				
				
			}
			
			rs.close();
			pst.close();
			
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarDocumentosContablesVentasHonorarios: "+e);
		}
		
		return lineas;
	}
	
	
	/**
	 * @param retencion 
	 * @param Connection con
	 * @param DtoInterfazLineaS1E linea
	 * @param DtoInterfazS1EInfo parametrizacion
	 * */
	private static ArrayList<DtoInterfazLineaS1E> generarLineaMovimientoVentas(
			Connection con,
			DtoInterfazLineaS1E linea,
			DtoInterfazS1EInfo parametrizacion, DtoRetencion retencion)
	{
		ArrayList<DtoInterfazLineaS1E> array = new ArrayList<DtoInterfazLineaS1E>();
		ArrayList<DtoInterfazLineaS1E> lineasMovAgrupada = new ArrayList<DtoInterfazLineaS1E>();
		int codigoTipoDocumento = Integer.parseInt(linea.getArrayDocumentos().get(0).getTipoDocumento());
		int codigoTipoSolicitud = ConstantesBD.codigoNuncaValido;
		PreparedStatementDecorator pst = null;
		ResultSetDecorator  rs = null;
		String observaciones = "";
		logger.info("\n\n\nENTRAAA generarLineaMovimientoVentas >>");
		
		/*
		 * ANOTACION: En este método se deben generar N líneas de cuenta dependiendo de cada item encontrado 
		 * del resultset
		 * 
		 */
		
		
		try
		{
			String consulta = "";
			//Según el tipo de documento se hace la consulta correspondiente
			switch(codigoTipoDocumento)
			{
				case ConstantesBD.codigoTipoDocInteFacturaPaciente:
				case ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente:
					consulta = consultarDetalleFacturasPacienteStr;
					pst = new PreparedStatementDecorator(con, consulta);
					pst.setLong(1,Long.parseLong(linea.getArrayDocumentos().size()>1?linea.getArrayDocumentos().get(1).getNumeroDocumento():linea.getArrayDocumentos().get(0).getNumeroDocumento()));
					pst.setLong(2,Long.parseLong(linea.getArrayDocumentos().size()>1?linea.getArrayDocumentos().get(1).getNumeroDocumento():linea.getArrayDocumentos().get(0).getNumeroDocumento()));
					pst.setLong(3,Long.parseLong(linea.getArrayDocumentos().size()>1?linea.getArrayDocumentos().get(1).getNumeroDocumento():linea.getArrayDocumentos().get(0).getNumeroDocumento()));
					pst.setLong(4,Long.parseLong(linea.getArrayDocumentos().size()>1?linea.getArrayDocumentos().get(1).getNumeroDocumento():linea.getArrayDocumentos().get(0).getNumeroDocumento()));
					pst.setLong(5,Long.parseLong(linea.getArrayDocumentos().size()>1?linea.getArrayDocumentos().get(1).getNumeroDocumento():linea.getArrayDocumentos().get(0).getNumeroDocumento()));
					Log4JManager.info("<<<<<<<<<>>>>>>>>><< " + Long.parseLong(linea.getArrayDocumentos().size()>1?linea.getArrayDocumentos().get(1).getNumeroDocumento():linea.getArrayDocumentos().get(0).getNumeroDocumento()) +"     " + consulta);
					rs = new ResultSetDecorator(pst.executeQuery());
				break;
				
				case ConstantesBD.codigoTipoDocInteFacturasVarias:
				case ConstantesBD.codigoTipoDocInteAnulaFacturasVarias:
					consulta = consultarDetalleFacturasVariasStr;
					pst = new PreparedStatementDecorator(con, consulta);
					pst.setLong(1,Long.parseLong(linea.getArrayDocumentos().size()>1?linea.getArrayDocumentos().get(1).getNumeroDocumento():linea.getArrayDocumentos().get(0).getNumeroDocumento()));
					rs = new ResultSetDecorator(pst.executeQuery());
				break;
				case ConstantesBD.codigoTipoDocInteAjusFacturasVarias:
					consulta = consultarDetalleAjustesFacturasVariasStr;
					pst = new PreparedStatementDecorator(con, consulta);
					pst.setInt(1,Integer.parseInt(linea.getArrayDocumentos().get(0).getNumeroDocumento()));
					rs = new ResultSetDecorator(pst.executeQuery());
				break;
				case ConstantesBD.codigoTipoDocInteCCCapitacion:
					consulta = consultarDetalleCuentasCobroCapitacionStr;
					pst = new PreparedStatementDecorator(con, consulta);
					pst.setLong(1,Long.parseLong(linea.getArrayDocumentos().get(0).getNumeroDocumento()));
					rs = new ResultSetDecorator(pst.executeQuery());
				break;
				case ConstantesBD.codigoTipoDocInteAjustesCCCapitacion:
					consulta = consultarDetalleAjustesCuentaCobroCapitacionStr;
					pst = new PreparedStatementDecorator(con, consulta);
					pst.setLong(1,Long.parseLong(linea.getArrayDocumentos().get(0).getNumeroDocumento()));
					rs = new ResultSetDecorator(pst.executeQuery());
				break;
			}
			
			int contador=0;
			while(rs.next())
			{
				contador++;
				logger.info("i "+contador+" "+linea.getSumaDebito()+" "+linea.getSumaCredito());
				
				// Si el tipo de documento es codigoTipoDocInteCCCapitacion el campo Centro de operación del movimiento 
				// se debe tomar a partir del convenio y no de la parametrización
				String centroOperacion="";
				if (codigoTipoDocumento==ConstantesBD.codigoTipoDocInteCCCapitacion){
					centroOperacion = rs.getString("centro_atencion_contable");
				}
				
				//Identificar el número de cuentas según el tipo de documento
				int numeroRegistros = 1  ;
				
				//VALIDACION DE LOS PAQUETES************************************************************************
				if(codigoTipoDocumento == ConstantesBD.codigoTipoDocInteFacturaPaciente || codigoTipoDocumento == ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente)
				{
					codigoTipoSolicitud = rs.getInt("tipo_solicitud");
					if(codigoTipoSolicitud == ConstantesBD.codigoTipoSolicitudPaquetes)
					{
						//Validacion de la utilidad o pérdida del paquete, solo se registra cuenta si genera utilidad o perdida
						
						if(rs.getDouble("valor")==rs.getDouble("valor_consumo_paquete"))
						{
							numeroRegistros = 0;
						}
					}
				}
				//*************************************************************************************
				
				if(numeroRegistros==1) 
				{
						DtoInterfazLineaS1E lineaMov = new DtoInterfazLineaS1E(GeneracionInterfaz1E.indicadorLineaDetMovDocumentoContable);
						//**************VALIDACIÓN CAMPO NUMERO REGISTRO*******************************
						//lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_REGISTRO.getPosicion()).setValor(parametrizacion.getPosicion()+"");
						//***************VALIDACION CAMPO COMPAÑÍA****************************************
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.COMPANIA.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.COMPANIA.getPosicion()).getValor());
						//***************VALIDACION CAMPO CENTRO OPERACION****************************************
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPERACION.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getValor());
						//**************VALIDACION CAMPO TIPO DE DOCUMENTO****************************************
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TIPO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.TIPO_DOCUMENTO.getPosicion()).getValor());
						//*************VALIDACION CAMPO NÚMERO DE DOCUMENTO***************************************
						
						// IF para cambiar solamente los de facturación y anulacion facturas
						
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NRO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).getValor());
										 
						logger.info("\n\n\n\n\n Nro Documento >> "+ lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NRO_DOCUMENTO.getPosicion()).getValor());
						//*************VALIDACION CAMPO AUXILIAR CUENTA CONTABLE**********************************
						DtoCuentaContable cuentaContable = new DtoCuentaContable();
						//Según tipo de documento se consulta la cuenta correspondiente
						
						String nombreCentroCosto="";
						
						String consecutivoSolicitud="";
						String tipoAsocio="";
						switch(codigoTipoDocumento)
						{
							case ConstantesBD.codigoTipoDocInteFacturaPaciente:
							case ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente:
							
							consecutivoSolicitud=rs.getString("consecutivo_solicitud");
							tipoAsocio=rs.getString("tipo_asocio");
							//Según el tipo de solicitud
							switch(codigoTipoSolicitud)
							{
								case ConstantesBD.codigoTipoSolicitudMedicamentos:
								case ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos:	
									cuentaContable = UtilidadesInterfaz.consultarCuentaContableInterfazUnidadFuncional(con, rs.getInt("centro_costo_solicitante"), false, true, false, false, false);
									
									if(cuentaContable.getCuentaContable().equals(""))                                  
									{
										
										cuentaContable = UtilidadesInterfaz.consultarCuentaContableInterfazInvIngreso(con, rs.getInt("centro_costo_solicitante"), rs.getInt("servicio_articulo"), true, false);
										
									}
									
									 nombreCentroCosto="";
									if(rs.getString("nombre_centro_costo")!=null){
										nombreCentroCosto=" Centro costo solicitante: " +rs.getString("nombre_centro_costo");
									}
									cuentaContable.setMensaje("Consecutivo Solicitud: "+consecutivoSolicitud+" Cuenta Ingreso - ARTÍCULOS - código Artículo: " + rs.getString("servicio_articulo") + nombreCentroCosto);
									
									
								break;
								case ConstantesBD.codigoTipoSolicitudPaquetes:
									
									double utilidad = rs.getDouble("valor") - rs.getDouble("valor_consumo_paquete");
									cuentaContable = UtilidadesFacturacion.consultarCuentaContablePaquetexSolicitud(con, rs.getString("numero_solicitud"), utilidad>0?true:false, utilidad<0?true:false);
									
									 nombreCentroCosto="";
									if(rs.getString("nombre_centro_costo")!=null){
										nombreCentroCosto=" Centro costo solicitante: " +rs.getString("nombre_centro_costo");
									}
									cuentaContable.setMensaje("Consecutivo Solicitud: "+consecutivoSolicitud+" Cuenta Ingreso - SOLICITUD PAQUETES - Número Solicictud: " + rs.getInt("numero_solicitud") + nombreCentroCosto);
									
								break;
								default:
									
									cuentaContable = UtilidadesInterfaz.consultarCuentaContableInterfazUnidadFuncional(con, rs.getInt("centro_costo_solicitante"), true, false, false, false, false);
									
									if(cuentaContable.getCuentaContable().equals(""))
									{
										cuentaContable = UtilidadesInterfaz.consultarCuentaContableInterfazServicio(con, rs.getInt("servicio_articulo"), rs.getInt("centro_costo_solicitante"), true, false, false);
										
									}
									
									
									//Solución temporal para obtener cuentas contables de medicamentos especiales registrados en solicitudes de cirugia
									if(cuentaContable.getCuentaContable().equals(""))                                  
									{
										
										cuentaContable = UtilidadesInterfaz.consultarCuentaContableInterfazInvIngreso(con, rs.getInt("centro_costo_solicitante"), rs.getInt("servicio_articulo"), true, false);
										
									}
									//fin solución temporal
									 nombreCentroCosto="";
										if(rs.getString("nombre_centro_costo")!=null){
											nombreCentroCosto=  " Centro costo solicitante: "+rs.getString("nombre_centro_costo");
										}
									cuentaContable.setMensaje("Consecutivo Solicitud: "+consecutivoSolicitud+" Cuenta Ingreso - SERVICIO - PAQUETE - ARTICULO - código Servicio: " + rs.getInt("servicio_articulo") +nombreCentroCosto);
									
								break;
								
							}
						break;
							case ConstantesBD.codigoTipoDocInteFacturasVarias:
							case ConstantesBD.codigoTipoDocInteAnulaFacturasVarias:
							case ConstantesBD.codigoTipoDocInteAjusFacturasVarias:
								//Se consulta la información de la cuenta contable
								cuentaContable.setCodigo(rs.getString("cuenta_contable"));
								if(Utilidades.convertirAEntero(cuentaContable.getCodigo())>0)
								{
									cuentaContable = UtilidadesInterfaz.consultarDatosCuentaContable(con, cuentaContable);
								}
								
								 nombreCentroCosto="";
									if(rs.getString("nombre_centro_costo")!=null){
										nombreCentroCosto=" Centro costo solicitante:  "+rs.getString("nombre_centro_costo");
									}
								cuentaContable.setMensaje("Cuenta Ingreso AJUSTE FACTURAS VARIAS Cuenta Contable: "+cuentaContable + nombreCentroCosto);
							
						break;
							case ConstantesBD.codigoTipoDocInteCCCapitacion:
							case ConstantesBD.codigoTipoDocInteAjustesCCCapitacion:
								int codigoConvenio = rs.getInt("codigo_convenio");
								String codigoTipoRegimen = rs.getString("codigo_tipo_regimen");
								String codigoTipoConvenio = rs.getString("codigo_tipo_convenio");
								int codigoInstitucion = rs.getInt("codigo_institucion");
								Boolean datosMensaje=false;
								cuentaContable = UtilidadesInterfaz.consultarCuentaContableInterfazConvenio(con, codigoConvenio, codigoTipoRegimen, ConstantesBD.tipoCuentaConvenio, true, false);
								
								
								if(UtilidadTexto.isEmpty(cuentaContable.getCuentaContable()))
								{
									datosMensaje=true;
									cuentaContable = UtilidadesFacturacion.consultarCuentaContableTipoConvenio(con, codigoTipoConvenio, codigoInstitucion, true, false, false, false, false);
								}
								
								 nombreCentroCosto="";
									if(rs.getString("nombre_centro_costo")!=null){
										nombreCentroCosto=" Centro costo solicitante: "+rs.getString("nombre_centro_costo");
									}
									
									if(datosMensaje){	
										cuentaContable.setMensaje(" Cuenta Ingreso AJUSTES CAPITACION Código del tipo convenio: "+codigoTipoConvenio+ " Código de la Institucion  " +codigoInstitucion + nombreCentroCosto );
									}else{
										cuentaContable.setMensaje(" Cuenta Ingreso AJUSTES CAPITACION Código del convenio: "+codigoConvenio+ " Código Tipo Regimen   " +codigoTipoRegimen + nombreCentroCosto );
									}
							
							
							break;
						}
						
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setValor(cuentaContable.getCuentaContable());
						if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).getValor().equals(""))
						{
							//logger.info("*************************************************");
							//logger.info("codigoTipoSolicitud: "+codigoTipoSolicitud);
							//logger.info("numeroSolicitud: "+rs.getString("numero_solicitud"));
							//logger.info("centroCostoSolicitante: "+rs.getInt("centro_costo_solicitante"));
							//logger.info("servicioArticulo: "+rs.getInt("servicio_articulo"));
							lineaMov.setExisteInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
							String mensaje=mensajeEInconFaltaDefinirInfo;
							mensaje+=" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).getDescripcion()+". ["+cuentaContable.getMensaje()+"]";
							
							if(!consecutivoSolicitud.isEmpty()){
								mensaje+=" Consecutivo Solicitud: "+consecutivoSolicitud;
							}
							
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setDescripcionIncon(mensaje);
						}
						
						
						//*************VALIDACION CAMPO TERCERO**********************************
						if(cuentaContable.isManejaTerceros())
						{
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.TERCERO_DOCUMENTO.getPosicion()).getValor());	
							if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).getValor().equals(""))
							{
								lineaMov.setExisteInconsistencia(true);
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setInconsistencia(true);
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
								
								String mensaje=mensajeEInconFaltaDefinirInfo;
								mensaje+=" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).getDescripcion();
								
								if(!consecutivoSolicitud.isEmpty()){
									mensaje+=" Consecutivo Solicitud: "+consecutivoSolicitud;
								}
								
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setDescripcionIncon(mensaje);
							}
						}
						
						
						//*************VALIDACION CAMPO CENTRO OPERACION MOVIMIENTO**********************************
						if(codigoTipoDocumento==ConstantesBD.codigoTipoDocInteCCCapitacion||codigoTipoDocumento==ConstantesBD.codigoTipoDocInteAjustesCCCapitacion)
						{
							asignarCodigoCentroAtencionContable(lineaMov, parametrizacion, CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion(), centroOperacion);
							
							if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).getValor().equals(""))
							{
								lineaMov.setExisteInconsistencia(true);
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).setInconsistencia(true);
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
								
								String mensaje="No se encontro informacion del campo";
								mensaje+=" "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).getDescripcion();
								
								if(!consecutivoSolicitud.isEmpty()){
									mensaje+=" Consecutivo Solicitud: "+consecutivoSolicitud;
								}
								
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).setDescripcionIncon(mensaje);
							}
							else
							{
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).setValor(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).getValor().substring(0,3));
							}
						}
						else
						{
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getValor());
						}
						
						//*************VALIDACION CAMPO UNIDAD DE NEGOCIO**********************************
						//Según el tipo de documento
						switch(codigoTipoDocumento)
						{
							case ConstantesBD.codigoTipoDocInteFacturaPaciente:
							case ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente:
							case ConstantesBD.codigoTipoDocInteFacturasVarias:
							case ConstantesBD.codigoTipoDocInteAnulaFacturasVarias:
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.UNIDAD_NEGOCIO.getPosicion()).setValor(rs.getString("uni_func_solicitado"));
								
								break;
							
							case ConstantesBD.codigoTipoDocInteAjusFacturasVarias:
							case ConstantesBD.codigoTipoDocInteCCCapitacion:
							case ConstantesBD.codigoTipoDocInteAjustesCCCapitacion:	
								asignarUnidadFuncionalEstandar(lineaMov,parametrizacion,CampoMoviDocumentoContable.UNIDAD_NEGOCIO.getPosicion(),linea,"");
							    logger.info("\n\n\n\n\n ****   UNIDAD FUNCIONAL **** \n tipo Doc "+codigoTipoDocumento+" \n Valor"+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.UNIDAD_NEGOCIO.getPosicion()).getValor());
							    break;	
					    /*	
						case ConstantesBD.codigoTipoDocInteCCCapitacion:
						case ConstantesBD.codigoTipoDocInteAjustesCCCapitacion: // aqui1111 Cambio Tarea 152926 Agosto 20 - 2010
							asignarUnidadFuncionalEstandarParamGeneral(lineaMov,parametrizacion,CampoMoviDocumentoContable.UNIDAD_NEGOCIO.getPosicion(),linea,"");
						break;*/
						}
						
						
						//*************VALIDACION CAMPO AUXILIAR DE CENTRO DE COSTOS**********************************
						/*
						if(linea.getArrayDocumentos().get(0).getNumeroDocumento().equals("38382"))
						{
							logger.info("¿Cuenta contable maneja centro de costo? "+cuentaContable.isManejoCentrosCosto());
							logger.info("cxodigo centro de costo: "+rs.getString("cod_centro_costo_solicitante"));
							logger.info("valor pool: "+rs.getDouble("valor_pool"));
							logger.info("especialidad solicitada: "+rs.getInt("especialidad_solicitada"));
							logger.info("centro costo contable: "+rs.getInt("centro_costo_solicitante"));
						}*/
						if(cuentaContable.isManejoCentrosCosto())
						{
							String mensajeCentroCosto = "";
							//Según tipo de documento
							switch(codigoTipoDocumento)
							{
							case ConstantesBD.codigoTipoDocInteFacturaPaciente:
							case ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente:
								
								//Segun el tipode solicitud se toma el auxiliar de centros de costo de manera distinta
								switch(codigoTipoSolicitud)
								{
									case ConstantesBD.codigoTipoSolicitudMedicamentos:
									case ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos:
										mensajeCentroCosto += "Tipo Sol: Medicamentos - Cargos directos articulos - COD. CENTRO COSTO SOLICITANTE: " + rs.getString("cod_centro_costo_solicitante");
										lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setValor(rs.getString("cod_centro_costo_solicitante"));
									break;
									default:
										if(rs.getInt("pool")>0 && rs.getDouble("valor_pool")>=0)
										{
											DtoEspecialidad especialidad = new DtoEspecialidad();
											
											especialidad.setCodigo(rs.getInt("especialidad_solicitada"));
											if(especialidad.getCodigo()>0)
											{
												UtilidadesHistoriaClinica.consultarEspecialidad(con, especialidad);
											}
											lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setValor(especialidad.getCentroCostoHonorarios().getCodigoInterfaz());
											
											mensajeCentroCosto += " - POR ESPECIALIDAD";
											if(especialidad.getCodigo()>0){
												 mensajeCentroCosto+=" - Codigo especialidad: " + rs.getInt("especialidad_solicitada") + " Nombre especialidad : " + especialidad.getNombre() + " Codigo Interfaz: " + especialidad.getCentroCostoHonorarios().getCodigoInterfaz();
											}
										}
										else
										{
											lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setValor(rs.getString("cod_centro_costo_solicitante"));
											mensajeCentroCosto += " - Tipo Sol diferente a medicamentos o cargos directos articulos - COD. CENTRO COSTO SOLICITANTE:" + rs.getString("cod_centro_costo_solicitante");
										}
									break;
								}
								
								
								if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).getValor().equals(""))
								{
									lineaMov.setExisteInconsistencia(true);
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setInconsistencia(true);
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
									
									String mensaje=mensajeEInconFaltaDefinirInfo;
									
									mensaje+=" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).getDescripcion() +mensajeCentroCosto ;
									
									if(!consecutivoSolicitud.isEmpty()){
										mensaje+=" Consecutivo Solicitud: "+consecutivoSolicitud;
										if(tipoAsocio!=null&&!tipoAsocio.trim().isEmpty()){
											mensaje+=" Tipo Asocio: "+tipoAsocio;
										}
									}
									
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setDescripcionIncon(mensaje);
								}
								else if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).getValor().length()>15)
								{
									//Si el campo supera el tamaño se trunca
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setValor(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.UNIDAD_NEGOCIO.getPosicion()).getValor().substring(0, 15));
								}
								
								break;
							case ConstantesBD.codigoTipoDocInteFacturasVarias:
							case ConstantesBD.codigoTipoDocInteAnulaFacturasVarias:
							case ConstantesBD.codigoTipoDocInteAjusFacturasVarias:
							case ConstantesBD.codigoTipoDocInteCCCapitacion:
							case ConstantesBD.codigoTipoDocInteAjustesCCCapitacion:
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setValor(rs.getString("codigo_centro_costo"));
								if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).getValor().equals(""))
								{
									lineaMov.setExisteInconsistencia(true);
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setInconsistencia(true);
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
									
									String mensaje=mensajeEInconFaltaDefinirInfo;
									mensaje+=" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).getDescripcion();
									
									if(!consecutivoSolicitud.isEmpty()){
										mensaje+=" Consecutivo Solicitud: "+consecutivoSolicitud;
									}
									
									
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setDescripcionIncon(mensaje);
								}
								else if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).getValor().length()>15)
								{
									//Si el campo supera el tamaño se trunca
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setValor(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.UNIDAD_NEGOCIO.getPosicion()).getValor().substring(0, 15));
								}
								break;
							}
							
							
						}	
						
						//*************VALIDACION CAMPO VALOR DEBITO**********************************
						//Dependiendo del tipo de documento
						switch(codigoTipoDocumento)
						{
							case ConstantesBD.codigoTipoDocInteFacturaPaciente:
								if(codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudPaquetes)
								{
									double utilidad = rs.getDouble("valor") - rs.getDouble("valor_consumo_paquete");
									if(utilidad<0)
									{
										lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(utilidad*-1,"0.0000"));
										linea.setSumaDebito(linea.getSumaDebito()+(utilidad*-1));
									}
									else
									{
										lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor("0");
									}
								}
								else
								{
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor("0");
								}
							break;
							case ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente:
								if(codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudPaquetes)
								{
									double utilidad = rs.getDouble("valor") - rs.getDouble("valor_consumo_paquete");
									if(utilidad>0)
									{
										lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(utilidad,"0.0000"));
										linea.setSumaDebito(linea.getSumaDebito()+(utilidad));
									}
									else
									{
										lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor("0");
									}
								}
								else
								{
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getDouble("valor"),"0.0000"));
									linea.setSumaDebito(linea.getSumaDebito()+rs.getDouble("valor"));
									
								}
							break;
							case ConstantesBD.codigoTipoDocInteFacturasVarias:
							case ConstantesBD.codigoTipoDocInteCCCapitacion:
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor("0");
							break;
							case ConstantesBD.codigoTipoDocInteAnulaFacturasVarias:
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getDouble("valor"),"0.0000"));
								linea.setSumaDebito(linea.getSumaDebito()+rs.getDouble("valor"));
							break;
							case ConstantesBD.codigoTipoDocInteAjusFacturasVarias:
								if(rs.getString("tipo_ajuste").equals(ConstantesIntegridadDominio.acronimoCredito))
								{
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getDouble("valor"),"0.0000"));
									linea.setSumaDebito(linea.getSumaDebito()+rs.getDouble("valor"));
								}
								else
								{
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor("0");
									
								}
							break;
							case ConstantesBD.codigoTipoDocInteAjustesCCCapitacion:
								int codTipoAjuste = rs.getInt("tipo_ajuste");
								if(codTipoAjuste==ConstantesBD.codigoAjusteCreditoCuentaCobro||codTipoAjuste==ConstantesBD.codigoAjusteCreditoFactura||codTipoAjuste==ConstantesBD.codigoConceptosCarteraCredito)
								{
									
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getDouble("valor"),"0.0000"));
									linea.setSumaDebito(linea.getSumaDebito()+rs.getDouble("valor"));
									
								}
								else
								{
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor("0");
									
								}
							break;
						}
						if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).getValor().equals(""))
						{
							lineaMov.setExisteInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
							
							String mensaje="Falta definir informacion para el campo";
							mensaje+=" "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).getDescripcion();
							
							if(!consecutivoSolicitud.isEmpty()){
								mensaje+=" Consecutivo Solicitud: "+consecutivoSolicitud;
							}
							
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setDescripcionIncon(mensaje);
						}
											
						
						//*************VALIDACION CAMPO VALOR CREDITO**********************************
						///Dependiendo del tipo de documento
						switch(codigoTipoDocumento)
						{
							case ConstantesBD.codigoTipoDocInteFacturaPaciente:
							
								if(codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudPaquetes)
								{
									double utilidad = rs.getDouble("valor") - rs.getDouble("valor_consumo_paquete");
									if(utilidad>0)
									{
										lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(utilidad,"0.0000"));
										linea.setSumaCredito(linea.getSumaCredito()+utilidad);
									}
									else
									{
										lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor("0");
										//linea.setSumaCredito(linea.getSumaCredito()+Utilidades.convertirADouble(retencion.getConceptos().get(i).getValorRetencion()));
									}
								}
								else
								{
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getDouble("valor"),"0.0000"));
									linea.setSumaCredito(linea.getSumaCredito()+rs.getDouble("valor"));
									
								}
							break;
							case ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente:
								if(codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudPaquetes)
								{
									double utilidad = rs.getDouble("valor") - rs.getDouble("valor_consumo_paquete");
									if(utilidad<0)
									{
										lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores((utilidad*-1),"0.0000"));
										linea.setSumaCredito(linea.getSumaCredito()+(utilidad*-1));
									}
									else
									{
										lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor("0");
									}
								}
								else
								{
									
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor("0");
									
								}
							break;	
							case ConstantesBD.codigoTipoDocInteFacturasVarias:
							case ConstantesBD.codigoTipoDocInteCCCapitacion:
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getString("valor"),"0.0000"));
								linea.setSumaCredito(linea.getSumaCredito()+rs.getDouble("valor"));
								
							break;
							case ConstantesBD.codigoTipoDocInteAnulaFacturasVarias:
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor("0");
								
							break;
							case ConstantesBD.codigoTipoDocInteAjusFacturasVarias:
								if(rs.getString("tipo_ajuste").equals(ConstantesIntegridadDominio.acronimoDebito))
								{
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getDouble("valor"),"0.0000"));
									linea.setSumaCredito(linea.getSumaCredito()+rs.getDouble("valor"));
									
								}
								else
								{
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor("0");
									
								}
							break;
							case ConstantesBD.codigoTipoDocInteAjustesCCCapitacion:
								int codTipoAjuste = rs.getInt("tipo_ajuste");
								if(codTipoAjuste==ConstantesBD.codigoAjusteCreditoCuentaCobro||codTipoAjuste==ConstantesBD.codigoAjusteCreditoFactura||codTipoAjuste==ConstantesBD.codigoConceptosCarteraCredito)
								{	
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor("0");
																		
								}
								else
								{
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getDouble("valor"),"0.0000"));
									linea.setSumaCredito(linea.getSumaCredito()+rs.getDouble("valor"));								
									
								}
							break;
						}
						
						if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).getValor().equals(""))
						{
							lineaMov.setExisteInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
							
							String mensaje="Falta definir informacion para el campo";
							mensaje+=" "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).getDescripcion();
							
							if(!consecutivoSolicitud.isEmpty()){
								mensaje+=" Consecutivo Solicitud: "+consecutivoSolicitud;
							}
							
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setDescripcionIncon(mensaje);
						}
						
							
						
						
						//*************VALIDACION CAMPO OBSERVACIONES MOVIMIENTOS*********************
						
						observaciones = rs.getString("observaciones")+" "+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+" "+linea.getArrayDocumentos().get(0).getNumeroDocumento();
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.OBSERVACIONES_MOVIMI.getPosicion()).setValor(observaciones);
						//**************************************************************************
						
						if(Utilidades.convertirADouble(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).getValor()) > 0 || 
								Utilidades.convertirADouble(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).getValor()) > 0)
						{	
							array.add(lineaMov);							
							if(lineaMov.isExisteInconsistencia())
							{	
								linea.setExisteInconsistencia(true);
							}
						}						
					
				} //Fin if de numero Registros
			} //Fin while resultset
			pst.close();
			rs.close();
			
			//Se generan las líneas de la autorretencion
			for(int i=0;i<retencion.getConceptos().size();i++)
			{
				//Se generan 2 lineas por cada concepto
				for(int j=0;j<2;j++)
				{
					DtoInterfazLineaS1E lineaMov = new DtoInterfazLineaS1E(GeneracionInterfaz1E.indicadorLineaDetMovDocumentoContable);
					//**************VALIDACIÓN CAMPO NUMERO REGISTRO*******************************
					//lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_REGISTRO.getPosicion()).setValor(parametrizacion.getPosicion()+"");
					//***************VALIDACION CAMPO COMPAÑÍA****************************************
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.COMPANIA.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.COMPANIA.getPosicion()).getValor());
					//***************VALIDACION CAMPO CENTRO OPERACION****************************************
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPERACION.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getValor());
					
					//**************VALIDACION CAMPO TIPO DE DOCUMENTO****************************************
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TIPO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.TIPO_DOCUMENTO.getPosicion()).getValor());
					//*************VALIDACION CAMPO NÚMERO DE DOCUMENTO***************************************
						
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NRO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).getValor());
								
					logger.info("\n\n\n\n\n Nro Documento >> "+ lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NRO_DOCUMENTO.getPosicion()).getValor());
					
					//*************VALIDACION CAMPO AUXILIAR CUENTA CONTABLE**********************************
					DtoCuentaContable cuentaContable = new DtoCuentaContable();
					//Debito
					if(j==0)
					{
						cuentaContable.setCodigo(retencion.getConceptos().get(i).getCuentaDBAutoretencion().getCodigo());
						cuentaContable.setMensaje("Cuenta Autoretencion Debito : "+retencion.getConceptos().get(i).getCuentaDBAutoretencion().getCodigo() );
					}
					//Credito
					else
					{
						cuentaContable.setCodigo(retencion.getConceptos().get(i).getCuentaCRAutoretencion().getCodigo());
						cuentaContable.setMensaje("Cuenta Autoretencion Credito : "+retencion.getConceptos().get(i).getCuentaCRAutoretencion().getCodigo() );
					}
					cuentaContable = UtilidadesInterfaz.consultarDatosCuentaContable(con, cuentaContable);
					
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setValor(cuentaContable.getCuentaContable());
					if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).getValor().equals(""))
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).getDescripcion()+". ["+cuentaContable.getMensaje()+"]");
					}
					
					
					//*************VALIDACION CAMPO TERCERO**********************************
					if(cuentaContable.isManejaTerceros())
					{
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.TERCERO_DOCUMENTO.getPosicion()).getValor());	
						if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).getValor().equals(""))
						{
							lineaMov.setExisteInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).getDescripcion());
						}
					}
					
					
					//*************VALIDACION CAMPO CENTRO OPERACION MOVIMIENTO**********************************
					asignarCodigoCentroAtencionContable(lineaMov, parametrizacion, CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion(), "");
					if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).getValor().equals(""))
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).setDescripcionIncon("No se encontro informacion del campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).getDescripcion());
					}
					else
					{
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).setValor(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).getValor().substring(0,3));
					}
					
					
					//*************VALIDACION CAMPO UNIDAD DE NEGOCIO**********************************
					///Dependiendo del tipo de documento
					if(codigoTipoDocumento==ConstantesBD.codigoTipoDocInteFacturaPaciente||codigoTipoDocumento==ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente)
					{
						asignarUnidadFuncionalEstandar(lineaMov,parametrizacion,CampoMoviDocumentoContable.UNIDAD_NEGOCIO.getPosicion(),linea,ConstantesIntegridadDominio.acronimoVentas);
						
					}
					else
					{// aqui1111 Cambio Tarea 152926 Agosto 20 - 2010
						if(codigoTipoDocumento== ConstantesBD.codigoTipoDocInteCCCapitacion || codigoTipoDocumento==ConstantesBD.codigoTipoDocInteAjustesCCCapitacion)            
						{
						  asignarUnidadFuncionalEstandarParamGeneral(lineaMov,parametrizacion,CampoMoviDocumentoContable.UNIDAD_NEGOCIO.getPosicion(),linea,"");
						}
					}
					
					///*************VALIDACION CAMPO VALOR DEBITO**********************************
					//Para los ajustes de cuentas de cobro capitacion se consulta el tipo de ajuste
					int codTipoAjuste = ConstantesBD.codigoNuncaValido;
					if(codigoTipoDocumento==ConstantesBD.codigoTipoDocInteAjustesCCCapitacion)
					{
						pst = new PreparedStatementDecorator(con.prepareStatement(consultarTipoAjusteCuentaCobroCapitacionStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						pst.setLong(1,Long.parseLong(linea.getArrayDocumentos().get(0).getNumeroDocumento()));
						rs = new ResultSetDecorator(pst.executeQuery());	
						if(rs.next())
						{
							rs.getInt("tipo_ajuste");
						}
						rs.close();
						pst.close();
					}
					//Par alos ajustes de facturas varias se consulta el tipo de ajuste
					String tipoAjusteFV = "";
					if(codigoTipoDocumento == ConstantesBD.codigoTipoDocInteAjusFacturasVarias)
					{
						pst = new PreparedStatementDecorator(con.prepareStatement(consultarTipoAjusteFacturasVariasStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						pst.setInt(1,Integer.parseInt(linea.getArrayDocumentos().get(0).getNumeroDocumento()));
						rs = new ResultSetDecorator(pst.executeQuery());
						
						if(rs.next())
						{
							tipoAjusteFV = rs.getString("tipo_ajuste");
						}
						rs.close();
						pst.close();
					}
					
					//Dependiendo del tipo de documento
					switch(codigoTipoDocumento)
					{
						case ConstantesBD.codigoTipoDocInteFacturaPaciente:
								
							//Debito
							if(j==0)
							{
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(retencion.getConceptos().get(i).getValorRetencion(),"0.0000"));
								linea.setSumaDebito(linea.getSumaDebito()+Utilidades.convertirADouble(retencion.getConceptos().get(i).getValorRetencion(), true));
							}
							//Crédito
							else
							{
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor("0");
								     
							}		
						break;
						case ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente:
							
							//Debito
							if(j==0)
							{
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor("0");
							}
							//Crédito
							else
							{
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(retencion.getConceptos().get(i).getValorRetencion(),"0.0000"));
								linea.setSumaDebito(linea.getSumaDebito()+Utilidades.convertirADouble(retencion.getConceptos().get(i).getValorRetencion(), true));     
							}
						break;
						case ConstantesBD.codigoTipoDocInteFacturasVarias:
						case ConstantesBD.codigoTipoDocInteCCCapitacion:
							
							//Debito
							if(j==0)
							{
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(retencion.getConceptos().get(i).getValorRetencion(),"0.0000"));
								linea.setSumaDebito(linea.getSumaDebito()+Utilidades.convertirADouble(retencion.getConceptos().get(i).getValorRetencion(), true));
							}
							//Crédito
							else
							{
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor("0");
								     
							}
						break;
						case ConstantesBD.codigoTipoDocInteAnulaFacturasVarias:
							//Debito
							if(j==0)
							{
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor("0");
							}
							//Credito
							else
							{
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(retencion.getConceptos().get(i).getValorRetencion(),"0.0000"));
								linea.setSumaDebito(linea.getSumaDebito()+Utilidades.convertirADouble(retencion.getConceptos().get(i).getValorRetencion(), true));
							}
									
							
						break;
						case ConstantesBD.codigoTipoDocInteAjusFacturasVarias:
							
							if(tipoAjusteFV.equals(ConstantesIntegridadDominio.acronimoCredito))
							{
								
								//Debito
								if(j==0)
								{
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor("0");
									
								}
								//Credito
								else
								{
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(retencion.getConceptos().get(i).getValorRetencion(),"0.0000"));
									linea.setSumaDebito(linea.getSumaDebito()+Utilidades.convertirADouble(retencion.getConceptos().get(i).getValorRetencion(), true));
									
								}
							}
							else
							{
								
								//Debito
								if(j==0)
								{
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(retencion.getConceptos().get(i).getValorRetencion(),"0.0000"));
									linea.setSumaDebito(linea.getSumaDebito()+Utilidades.convertirADouble(retencion.getConceptos().get(i).getValorRetencion(), true));
								}
								//Credito
								else
								{
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor("0");
									
								}
							}
						break;
						case ConstantesBD.codigoTipoDocInteAjustesCCCapitacion:
							
							if(codTipoAjuste==ConstantesBD.codigoAjusteCreditoCuentaCobro||codTipoAjuste==ConstantesBD.codigoAjusteCreditoFactura||codTipoAjuste==ConstantesBD.codigoConceptosCarteraCredito)
							{
								
								//Debito
								if(j==0)
								{
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor("0");
									
								}
								//Credito
								else
								{
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(retencion.getConceptos().get(i).getValorRetencion(),"0.0000"));
									linea.setSumaDebito(linea.getSumaDebito()+Utilidades.convertirADouble(retencion.getConceptos().get(i).getValorRetencion(), true));
									
								}
								
							}
							else
							{
								
								//Debito
								if(j==0)
								{
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(retencion.getConceptos().get(i).getValorRetencion(),"0.0000"));
									linea.setSumaDebito(linea.getSumaDebito()+Utilidades.convertirADouble(retencion.getConceptos().get(i).getValorRetencion(), true));
								}
								//Credito
								else
								{
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor("0");
									
								}
							}
						break;
					}
					if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).getValor().equals(""))
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setDescripcionIncon("Falta definir informacion para el campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).getDescripcion());
					}
											
						
					//*************VALIDACION CAMPO VALOR CREDITO**********************************
					///Dependiendo del tipo de documento
					switch(codigoTipoDocumento)
					{
						case ConstantesBD.codigoTipoDocInteFacturaPaciente:
							//Debito
							if(j==0)
							{
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor("0");
							}
							//Credito
							else
							{
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(retencion.getConceptos().get(i).getValorRetencion(),"0.0000"));
								linea.setSumaCredito(linea.getSumaCredito()+Utilidades.convertirADouble(retencion.getConceptos().get(i).getValorRetencion()));
							}
						break;
						case ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente:
							
							//Debito
							if(j==0)
							{
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(retencion.getConceptos().get(i).getValorRetencion(),"0.0000"));
								linea.setSumaCredito(linea.getSumaCredito()+Utilidades.convertirADouble(retencion.getConceptos().get(i).getValorRetencion()));
								
							}
							//Credito
							else
							{
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor("0");
							}
						break;	
						case ConstantesBD.codigoTipoDocInteFacturasVarias:
						case ConstantesBD.codigoTipoDocInteCCCapitacion:
							
							//Debito
							if(j==0)
							{
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor("0");
							}
							//Credito
							else
							{
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(retencion.getConceptos().get(i).getValorRetencion(),"0.0000"));
								linea.setSumaCredito(linea.getSumaCredito()+Utilidades.convertirADouble(retencion.getConceptos().get(i).getValorRetencion()));	
							}
								
							
						break;
						case ConstantesBD.codigoTipoDocInteAnulaFacturasVarias:
							
							//Debito
							if(j==0)
							{
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(retencion.getConceptos().get(i).getValorRetencion(),"0.0000"));
								linea.setSumaCredito(linea.getSumaCredito()+Utilidades.convertirADouble(retencion.getConceptos().get(i).getValorRetencion()));
								
							}
							//Credito
							else
							{
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor("0");
							}
						break;
						case ConstantesBD.codigoTipoDocInteAjusFacturasVarias:
							if(tipoAjusteFV.equals(ConstantesIntegridadDominio.acronimoDebito))
							{
								
								//Debito
								if(j==0)
								{
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor("0");
								}
								//Credito
								else
								{
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(retencion.getConceptos().get(i).getValorRetencion(),"0.0000"));
									linea.setSumaCredito(linea.getSumaCredito()+Utilidades.convertirADouble(retencion.getConceptos().get(i).getValorRetencion()));	
								}
								
							}
							else
							{
								
								//Debito
								if(j==0)
								{
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(retencion.getConceptos().get(i).getValorRetencion(),"0.0000"));
									linea.setSumaCredito(linea.getSumaCredito()+Utilidades.convertirADouble(retencion.getConceptos().get(i).getValorRetencion()));
								}
								//Credito
								else
								{
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor("0");
										
								}
								
							}
						break;
						case ConstantesBD.codigoTipoDocInteAjustesCCCapitacion:
							if(codTipoAjuste==ConstantesBD.codigoAjusteCreditoCuentaCobro||codTipoAjuste==ConstantesBD.codigoAjusteCreditoFactura||codTipoAjuste==ConstantesBD.codigoConceptosCarteraCredito)
							{	
								
								//Debito
								if(j==0)
								{
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(retencion.getConceptos().get(i).getValorRetencion(),"0.0000"));
									linea.setSumaCredito(linea.getSumaCredito()+Utilidades.convertirADouble(retencion.getConceptos().get(i).getValorRetencion()));
								}
								//Credito
								else
								{
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor("0");
										
								}	
							}
							else
							{
								//Debito
								if(j==0)
								{
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor("0");
								}
								//Credito
								else
								{
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(retencion.getConceptos().get(i).getValorRetencion(),"0.0000"));
									linea.setSumaCredito(linea.getSumaCredito()+Utilidades.convertirADouble(retencion.getConceptos().get(i).getValorRetencion()));	
								}
							}
						break;
					}
					
					if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).getValor().equals(""))
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setDescripcionIncon("Falta definir informacion para el campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).getDescripcion());
					}
					
					
					//*************VALIDACION CAMPO VALOR BASE GRAVABLE*****************************
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_BASE_GRAVABLE.getPosicion()).setValor(retencion.getConceptos().get(i).getValorBaseGravable());
					
					//*************VALIDACION CAMPO OBSERVACIONES MOVIMIENTOS*********************
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.OBSERVACIONES_MOVIMI.getPosicion()).setValor(observaciones);
					
					
					
					//**************************************************************************
					
					if(Utilidades.convertirADouble(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).getValor()) > 0 || 
							Utilidades.convertirADouble(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).getValor()) > 0)
					{	
						array.add(lineaMov);							
						if(lineaMov.isExisteInconsistencia())
						{	
							linea.setExisteInconsistencia(true);
						}
					}		
					
				}//Fin for J (para hacer las 2 lineas de la autorretencion)
			}//Finfor i (conceptos de retencion)
			
			
			//*********************GENERACION LÍNEA DE CUENTA TEMPORAL INGRESO****************************************************+
			if(codigoTipoDocumento==ConstantesBD.codigoTipoDocInteFacturaPaciente || codigoTipoDocumento==ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente)
			{
				//Solo aplica para capitados
				//ARMANDO. ESTA TOMANDO DOBLE EL MOVIEMINTO DE ABONO.
				if(codigoTipoDocumento==ConstantesBD.codigoTipoDocInteFacturaPaciente)
					consulta = consultarDetalleFacturasPaciente02Str + " and c.tipo_contrato = "+ConstantesBD.codigoTipoContratoCapitado;
				else if(codigoTipoDocumento==ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente)
					consulta = consultarDetalleFacturasPacienteAnuladas02Str + " and c.tipo_contrato = "+ConstantesBD.codigoTipoContratoCapitado;
				pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setLong(1,Long.parseLong(linea.getArrayDocumentos().size()>1?linea.getArrayDocumentos().get(1).getNumeroDocumento():linea.getArrayDocumentos().get(0).getNumeroDocumento()));
				
				rs = new ResultSetDecorator(pst.executeQuery());
				if(rs.next())
				{
					//Identificar el número de cuentas según el tipo de documento
					int numeroRegistros = 0  ;
					
					//Se verifica si hubo valor cobrar convenio
					if(rs.getDouble("valor_cobrar_convenio")>0)
					{
						numeroRegistros++;
					}
					//Se verifica si hay valor a favor convenio
					if(rs.getDouble("valor_favor_convenio")>0)
					{
						numeroRegistros++;
					}
					
					for(int i=0;i<numeroRegistros;i++)
					{
						DtoInterfazLineaS1E lineaMov = new DtoInterfazLineaS1E(GeneracionInterfaz1E.indicadorLineaDetMovDocumentoContable);
						//**************VALIDACIÓN CAMPO NUMERO REGISTRO*******************************
						//lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_REGISTRO.getPosicion()).setValor(parametrizacion.getPosicion()+"");
						//***************VALIDACION CAMPO COMPAÑÍA****************************************
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.COMPANIA.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.COMPANIA.getPosicion()).getValor());
						//***************VALIDACION CAMPO CENTRO OPERACION****************************************
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPERACION.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getValor());
						//**************VALIDACION CAMPO TIPO DE DOCUMENTO****************************************
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TIPO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.TIPO_DOCUMENTO.getPosicion()).getValor());
						//*************VALIDACION CAMPO NÚMERO DE DOCUMENTO***************************************
						
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NRO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).getValor());
						logger.info("\n\n\n\n\n Nro Documento >> "+ lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NRO_DOCUMENTO.getPosicion()).getValor());
						//*************VALIDACION CAMPO AUXILIAR CUENTA CONTABLE**********************************
						DtoCuentaContable cuentaContable = new DtoCuentaContable();
						
						int codigoConvenio = rs.getInt("codigo_convenio");
						String codigoTipoRegimen = rs.getString("codigo_tipo_regimen");
						String codigoTipoConvenio = rs.getString("codigo_tipo_convenio");
						int codigoInstitucion = rs.getInt("codigo_institucion");
						Boolean mensajesCuenta=false;
						//Cuenta x cobrar convenio
						if(i==0)
						{
							cuentaContable = UtilidadesInterfaz.consultarCuentaContableInterfazConvenio(con, codigoConvenio, codigoTipoRegimen, ConstantesBD.tipoCuentaConvenio, true, false);								
							if(cuentaContable.getCuentaContable().equals(""))
							{
								mensajesCuenta=true;
								cuentaContable = UtilidadesFacturacion.consultarCuentaContableTipoConvenio(con, codigoTipoConvenio, codigoInstitucion, true, false, false, false, false);
							}
							
							if(!mensajesCuenta){
								cuentaContable.setMensaje("Cuenta por Cobrar Convenio Código del Convenio:"+codigoConvenio+" Código Tipo Régimen: "+codigoTipoRegimen );
							}else{
								cuentaContable.setMensaje("Cuenta por Cobrar Convenio Código Tipo Convenio: "+codigoTipoConvenio+" Código Institución:"+codigoInstitucion );
							}
						}
						//Cuenta valor a favor convenio
						else if(i==1)
						{
							mensajesCuenta=false;
							cuentaContable = UtilidadesInterfaz.consultarCuentaContableInterfazConvenio(con, codigoConvenio, codigoTipoRegimen, ConstantesBD.tipoCuentaFavorConvenio, true, false);
							if(cuentaContable.getCuentaContable().equals(""))
							{
								mensajesCuenta=true;
								cuentaContable = UtilidadesFacturacion.consultarCuentaContableTipoConvenio(con, codigoTipoConvenio, codigoInstitucion, false, true, false, false, false);
							}
							if(mensajesCuenta){
								cuentaContable.setMensaje("Cuenta valor a favor Convenio  Código Tipo Convenio:"+codigoTipoConvenio+" Codigo Institución:"+codigoInstitucion );
							}else{
								cuentaContable.setMensaje("Cuenta valor a favor Convenio  Código Convenio:"+codigoConvenio+" Codigo Tipo Régimen:"+codigoTipoRegimen );
							}
						
							
						}
						
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setValor(cuentaContable.getCuentaContable());
						if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).getValor().equals(""))
						{
							//logger.info("*************************************************");
							//logger.info("codigoTipoSolicitud: "+codigoTipoSolicitud);
							//logger.info("numeroSolicitud: "+rs.getString("numero_solicitud"));
							//logger.info("centroCostoSolicitante: "+rs.getInt("centro_costo_solicitante"));
							//logger.info("servicioArticulo: "+rs.getInt("servicio_articulo"));
							lineaMov.setExisteInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).getDescripcion()+". ["+cuentaContable.getMensaje()+"]");
						}
						
						
						//*************VALIDACION CAMPO TERCERO**********************************
						if(cuentaContable.isManejaTerceros())
						{
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setValor(rs.getString("id_tercero"));	
							if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).getValor().equals(""))
							{
								lineaMov.setExisteInconsistencia(true);
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setInconsistencia(true);
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).getDescripcion());
							}
						}
						
						
						//*************VALIDACION CAMPO CENTRO OPERACION MOVIMIENTO**********************************
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).setValor(rs.getString("codigo_centro_at_cont"));
						if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).getValor().equals(""))
						{
							lineaMov.setExisteInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).setInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).setDescripcionIncon("No se encontro informacion del campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).getDescripcion());
						}
						else
						{
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).setValor(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).getValor().substring(0,3));
						}
						
						//*************VALIDACION CAMPO UNIDAD DE NEGOCIO**********************************
						////Se busca el indicativo de Unidad Funcional Estandar que aplica para el tipo de documento
						String unidadFuncEstandar = "";
						for(DtoTiposInterfazDocumentosParam1E tipo:parametrizacion.getArrayListDtoTiposDocumentos())
						{
							if(tipo.getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteCCCapitacion+""))//--PRUEBA
							{
								unidadFuncEstandar = tipo.getUnidadFuncionalEstandar();
								
							}
						}
						if(unidadFuncEstandar.equals(ConstantesBD.codigoNuncaValido+""))
						{
							unidadFuncEstandar = "";
						}
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.UNIDAD_NEGOCIO.getPosicion()).setValor(unidadFuncEstandar);
						if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.UNIDAD_NEGOCIO.getPosicion()).getValor().length()>2)
						{
							//Si el campo supera el tamaño se trunca
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.UNIDAD_NEGOCIO.getPosicion()).setValor(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.UNIDAD_NEGOCIO.getPosicion()).getValor().substring(0, 2));
						}
						
						
						//*************VALIDACION CAMPO AUXILIAR DE CENTRO DE COSTOS**********************************
						if(linea.getArrayDocumentos().get(0).getNumeroDocumento().equals("38382"))
						{
							logger.info("parate de capitacion !!");
							logger.info("codigo interfaz centro costo: "+rs.getString("cod_int_cc_contable"));
							logger.info("centro costo contable: "+rs.getInt("centro_costo_contable"));
						}
						
						if(cuentaContable.isManejoCentrosCosto())
						{
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setValor(rs.getString("cod_int_cc_contable"));
							if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).getValor().equals(""))
							{
								if(linea.getArrayDocumentos().get(0).getNumeroDocumento().equals("38382"))
								{
									logger.info("genera inconsistencia");
								}
								
								lineaMov.setExisteInconsistencia(true);
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setInconsistencia(true);
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).getDescripcion());
							}
							else if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).getValor().length()>15)
							{
								//Si el campo supera el tamaño se trunca
								lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setValor(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).getValor().substring(0, 15));
							}
						}	
						
						//*************VALIDACION CAMPO VALOR DEBITO**********************************
						//Dependiendo del tipo de documento
						switch(codigoTipoDocumento)
						{
							
							case ConstantesBD.codigoTipoDocInteFacturaPaciente:
								//Valor cuenta x cobrar convenio
								if(i==0)
								{
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getString("valor_cobrar_convenio"), "0.0000"));
									linea.setSumaDebito(linea.getSumaDebito()+rs.getDouble("valor_cobrar_convenio"));
									
								}
								//Valor cuenta a favor convenio
								else if(i==1)
								{
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor("0");
									
								}
							break;
							case ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente:
								//Valor cuenta x cobrar convenio
								if(i==0)
								{
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor("0");
								}
								//Valor cuenta a favor convenio
								else if(i==1)
								{
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getString("valor_favor_convenio"), "0.0000"));
									linea.setSumaDebito(linea.getSumaDebito()+rs.getDouble("valor_favor_convenio"));
								}
							break;
							
						}
						if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).getValor().equals(""))
						{
							lineaMov.setExisteInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setDescripcionIncon("Falta definir informacion para el campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).getDescripcion());
						}
											
						
						//*************VALIDACION CAMPO VALOR CREDITO**********************************
						///Dependiendo del tipo de documento
						switch(codigoTipoDocumento)
						{
							case ConstantesBD.codigoTipoDocInteFacturaPaciente:
							
								//Valor cuenta x cobrar convenio
								if(i==0)
								{
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor("0");
								}
								//Valor cuenta a favor convenio
								else if(i==1)
								{
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getDouble("valor_favor_convenio"), "0.0000"));
									linea.setSumaCredito(linea.getSumaCredito()+rs.getDouble("valor_favor_convenio"));
								}
							break;
							case ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente:
								//Valor cuenta x cobrar convenio
								if(i==0)
								{
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getDouble("valor_cobrar_convenio"), "0.0000"));
									linea.setSumaCredito(linea.getSumaCredito()+rs.getDouble("valor_cobrar_convenio"));
								}
								//Valor cuenta a favor convenio
								else if(i==1)
								{
									lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor("0");
									
								}
							break;	
							
						}
						
						if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).getValor().equals(""))
						{
							lineaMov.setExisteInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setDescripcionIncon("Falta definir informacion para el campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).getDescripcion());
						}
						
							
						
						
						//*************VALIDACION CAMPO OBSERVACIONES MOVIMIENTOS*********************
						
						
						observaciones = rs.getString("nombre_convenio")+" "+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+" "+linea.getArrayDocumentos().get(0).getNumeroDocumento();
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.OBSERVACIONES_MOVIMI.getPosicion()).setValor(observaciones);
						//**************************************************************************
						
						if(Utilidades.convertirADouble(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).getValor()) > 0 || 
								Utilidades.convertirADouble(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).getValor()) > 0)
						{	
							array.add(lineaMov);							
							if(lineaMov.isExisteInconsistencia())
							{	
								linea.setExisteInconsistencia(true);
							}
						}
					} // Fin for numero de registros
				} //fion if der esultSet
				rs.close();
				pst.close();
			} //Fin if veriricacion tipo documento factura y anulacion factura opaciente
			//***********************************************************************************************************************
			
			if(!huboLineaDetMov && array.size() > 0)
			{	
				huboLineaDetMov = true;
				logger.info("..:Genero "+array.size()+" de linea "+DtoInterfazCampoS1E.getDescripcionLinea(GeneracionInterfaz1E.indicadorLineaDetMovDocumentoContable));
			}
			
			
			
			
			//************AGRUPAR EL DETALLE**************************************************
			lineasMovAgrupada = agruparLineasMovimientoContable(array,parametrizacion);
			//**********************************************************************************
			
		}
		catch(SQLException e)
		{
			logger.error("Error en generarLineaMovimientoVentasHonorarios: ",e);
		}
		
		return lineasMovAgrupada;
		
	}
	
	/**
	 * @param retencion 
	 * @param Connection con
	 * @param DtoInterfazLineaS1E linea
	 * @param DtoInterfazS1EInfo parametrizacion
	 * */
	private static ArrayList<DtoInterfazLineaS1E> generarLineaMovimientoHonorarios(
			Connection con,
			DtoInterfazLineaS1E linea,
			DtoInterfazS1EInfo parametrizacion)
	{
		ArrayList<DtoInterfazLineaS1E> array = new ArrayList<DtoInterfazLineaS1E>();
		ArrayList<DtoInterfazLineaS1E> lineasMovAgrupada = new ArrayList<DtoInterfazLineaS1E>();
		int codigoTipoDocumento = Integer.parseInt(linea.getArrayDocumentos().get(0).getTipoDocumento());
		logger.info("\n\n\nENTRAAA generarLineaMovimientoHonorarios >>");
		
		PreparedStatementDecorator pst = null;
		ResultSetDecorator  rs = null;
		
		/*
		 * ANOTACION: En este método se deben generar N líneas de cuenta dependiendo de cada item encontrado 
		 * del resultset
		 * 
		 */
		
		
		try
		{
		
				
			Long documento=Long.parseLong(linea.getArrayDocumentos().size()>1?linea.getArrayDocumentos().get(1).getNumeroDocumento():linea.getArrayDocumentos().get(0).getNumeroDocumento());
			pst = new PreparedStatementDecorator(con, consultarDetalleFacturasPaciente04Str);
			pst.setLong(1,documento);
			pst.setLong(2,documento);
			pst.setLong(3,documento);
			pst.setLong(4,documento);
			pst.setLong(5,documento);
			

			logger.info("consulta --> "+ pst);
			
			
			rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{				
				
				{
				
					DtoInterfazLineaS1E lineaMov = new DtoInterfazLineaS1E(GeneracionInterfaz1E.indicadorLineaDetMovDocumentoContable);
					//**************VALIDACIÓN CAMPO NUMERO REGISTRO*******************************
					//lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_REGISTRO.getPosicion()).setValor(parametrizacion.getPosicion()+"");
					//***************VALIDACION CAMPO COMPAÑÍA****************************************
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.COMPANIA.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.COMPANIA.getPosicion()).getValor());
					//***************VALIDACION CAMPO CENTRO OPERACION****************************************
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPERACION.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getValor());
					//**************VALIDACION CAMPO TIPO DE DOCUMENTO****************************************
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TIPO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.TIPO_DOCUMENTO.getPosicion()).getValor());
					//*************VALIDACION CAMPO NÚMERO DE DOCUMENTO***************************************
					
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NRO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).getValor());
					
					//*************VALIDACION CAMPO AUXILIAR CUENTA CONTABLE**********************************
					DtoCuentaContable cuentaContable = new DtoCuentaContable();
					
					cuentaContable = UtilidadesInterfaz.consultarCuentaContableInterfazUnidadFuncional(con, rs.getInt("centro_costo_solicitante"), false, false, false, false, true);
					if(cuentaContable.getCuentaContable().equals(""))
					{
						//logger.error("--------------------------- AQUI ES DONDE SE LLAMA ----------------------------");
						cuentaContable = UtilidadesInterfaz.consultarCuentaContableInterfazServicio(con, rs.getInt("servicio"), rs.getInt("centro_costo_solicitante"), false, false, true);
					}
					
					cuentaContable.setMensaje("Cuenta Costo Honorarios");
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setValor(cuentaContable.getCuentaContable());
					if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).getValor().equals(""))
					{
						
						lineaMov.setExisteInconsistencia(true);
						logger.info("inconsistencia ==================================00");
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).getDescripcion()+". ["+cuentaContable.getMensaje()+"] ");
					}
					
					//*************VALIDACION CAMPO TERCERO**********************************
					if(cuentaContable.isManejaTerceros())
					{
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setValor(rs.getString("id_tercero"));	
						if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).getValor().equals(""))
						{
							logger.info("inconsistencia ==============================================");
							lineaMov.setExisteInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).getDescripcion());
						}
					}
					
					
					//*************VALIDACION CAMPO CENTRO OPERACION MOVIMIENTO**********************************
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getValor());
					
					//*************VALIDACION CAMPO UNIDAD DE NEGOCIO**********************************
					DtoEspecialidad especialidad = new DtoEspecialidad();
					
					especialidad.setCodigo(rs.getInt("especialidad_solicitada"));
					if(especialidad.getCodigo()>0)
					{
						UtilidadesHistoriaClinica.consultarEspecialidad(con, especialidad);
					}
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.UNIDAD_NEGOCIO.getPosicion()).setValor(especialidad.getCentroCostoHonorarios().getAcronimoUnidadFuncional());
					
					//*************VALIDACION CAMPO AUXILIAR DE CENTRO DE COSTOS**********************************
					//Solo aplica para honorarios
					if(cuentaContable.isManejoCentrosCosto())
					{
						//lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setValor(especialidad.getCentroCostoHonorarios().getCodigoInterfaz());
						// MT 1558 
						// DCU 781 -- Versión 1.8
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setValor(rs.getString("cod_centro_costo_solicitante"));
						
						if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).getValor().equals(""))
						{
							logger.info("inconsistencia ==============================================");
							lineaMov.setExisteInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).getDescripcion()+" por especialidad "+especialidad.getNombre().toLowerCase()+" ");
						}
						else if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).getValor().length()>15)
						{
							//Si el campo supera el tamaño se trunca
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).setValor(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.UNIDAD_NEGOCIO.getPosicion()).getValor().substring(0, 15));
						}
						
					}	
					
					//*************VALIDACION CAMPO VALOR DEBITO**********************************
					switch(codigoTipoDocumento)
					{
						case ConstantesBD.codigoTipoDocInteFacturaPaciente:
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getDouble("valor"),"0.0000"));
							linea.setSumaDebito(linea.getSumaDebito()+rs.getDouble("valor"));	
							linea.setSumaDebito(Math.round(linea.getSumaDebito()*Math.pow(10,8))/Math.pow(10,8));//redondeando a 8 decimales.						
						break;
						case ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente:
							
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor("0");	
							
						break;
					}
					
						
					if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).getValor().equals(""))
					{
						logger.info("inconsistencia ==============================================");
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setDescripcionIncon("Falta definir informacion campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).getDescripcion());
					}
					
					//*************VALIDACION CAMPO VALOR CREDITO**********************************
					switch(codigoTipoDocumento)
					{
						case ConstantesBD.codigoTipoDocInteFacturaPaciente:
							
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor("0");
							
						break;
						case ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente:
							
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getDouble("valor"),"0.0000"));
							linea.setSumaCredito(linea.getSumaCredito()+rs.getDouble("valor"));
							
						break;
					}
						
					if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).getValor().equals(""))
					{
						logger.info("inconsistencia ==============================================");
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setDescripcionIncon("Falta definir informacion campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).getDescripcion());
					}
					
					if(linea.getArrayDocumentos().get(0).getNumeroDocumento().equals("18535")||linea.getArrayDocumentos().get(0).getNumeroDocumento().equals("105"))
					{
						logger.info("valor credito: "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).getValor());
					}
					
					//logger.info("valor de la suma credito >> "+linea.getSumaCredito());
					
					
					
					//*************VALIDACION CAMPO OBSERVACIONES MOVIMIENTOS*********************
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.OBSERVACIONES_MOVIMI.getPosicion()).setValor(rs.getString("observacion")+" "+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+" "+linea.getArrayDocumentos().get(0).getNumeroDocumento());
									
					//**************************************************************************
					
					
					//parametrizacion.setPosicion(parametrizacion.getPosicion()+1);
					array.add(lineaMov);
					
					if(lineaMov.isExisteInconsistencia())
					{
						linea.setExisteInconsistencia(true);
					}
					
				}
				//*************CALCULO DE LA RETENCION***************************************************+
			
				DtoRetencion retencion = UtilidadesInterfaz.calcularRetencionInterfaz(con, linea.getArrayDocumentos(), parametrizacion, rs.getString("id_tercero"), true,Utilidades.convertirADouble(rs.getString("valor"))); 
				
				for(String mensajeIncon:retencion.getInconsistenciasRetencion())
				{
					logger.info("inconsistencia ==============================================");
					linea.setExisteInconsistencia(true);
					linea.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_REGISTRO.getPosicion()).setInconsistencia(true);
					linea.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_REGISTRO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
					linea.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_REGISTRO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
					linea.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_REGISTRO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
					linea.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_REGISTRO.getPosicion()).setDescripcionIncon(mensajeIncon);
				}
				
				for(int i=0;i<retencion.getConceptos().size();i++)
				{
					DtoInterfazLineaS1E lineaMov = new DtoInterfazLineaS1E(GeneracionInterfaz1E.indicadorLineaDetMovDocumentoContable);
					//**************VALIDACIÓN CAMPO NUMERO REGISTRO*******************************
					//lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NUMERO_REGISTRO.getPosicion()).setValor(parametrizacion.getPosicion()+"");
					//***************VALIDACION CAMPO COMPAÑÍA****************************************
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.COMPANIA.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.COMPANIA.getPosicion()).getValor());
					//***************VALIDACION CAMPO CENTRO OPERACION****************************************
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.CENTRO_OPERACION.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getValor());
					
					//**************VALIDACION CAMPO TIPO DE DOCUMENTO****************************************
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TIPO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.TIPO_DOCUMENTO.getPosicion()).getValor());
					//*************VALIDACION CAMPO NÚMERO DE DOCUMENTO***************************************
					
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NRO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).getValor());
					
					
					logger.info("\n\n\n NRO DOCUMENTO DEFINITIVO >> "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.NRO_DOCUMENTO.getPosicion()).getValor());
					//*************VALIDACION CAMPO AUXILIAR CUENTA CONTABLE**********************************
					DtoCuentaContable cuentaContable = new DtoCuentaContable();
					
					cuentaContable.setCodigo(retencion.getConceptos().get(i).getCuentaRetencion().getCodigo());
					if(!cuentaContable.getCodigo().equals(""))
					{
						cuentaContable = UtilidadesInterfaz.consultarDatosCuentaContable(con, cuentaContable);
					}
					
					cuentaContable.setMensaje("Cuenta Retencion");
					
					
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setValor(cuentaContable.getCuentaContable());
					if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).getValor().equals(""))
					{
						logger.info("inconsistencia ==============================================");
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).getDescripcion()+". ["+cuentaContable.getMensaje()+"] ");
					}
					
					//*************VALIDACION CAMPO TERCERO**********************************
					if(cuentaContable.isManejaTerceros())
					{
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setValor(rs.getString("id_tercero"));	
						if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).getValor().equals(""))
						{
							logger.info("inconsistencia ==============================================");
							lineaMov.setExisteInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).getDescripcion());
						}
					}
					
					
					//*************VALIDACION CAMPO CENTRO OPERACION MOVIMIENTO**********************************
					asignarCodigoCentroAtencionContable(lineaMov, parametrizacion, CampoMoviDocumentoContable.CENTRO_OPER_MOVI.getPosicion(), "");
					
					
					//*************VALIDACION CAMPO UNIDAD DE NEGOCIO**********************************
					if(codigoTipoDocumento==ConstantesBD.codigoTipoDocInteFacturaPaciente||codigoTipoDocumento==ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente)
					{
						asignarUnidadFuncionalEstandar(lineaMov,parametrizacion,CampoMoviDocumentoContable.UNIDAD_NEGOCIO.getPosicion(),linea,ConstantesIntegridadDominio.acronimoHonorarios);
					}
					else
					{
						asignarUnidadFuncionalEstandar(lineaMov,parametrizacion,CampoMoviDocumentoContable.UNIDAD_NEGOCIO.getPosicion(),linea,"");
					}
					
					//*************VALIDACION CAMPO VALOR DEBITO**********************************
					switch(codigoTipoDocumento)
					{
						case ConstantesBD.codigoTipoDocInteFacturaPaciente:
							
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor("0");
							
						break;
						case ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente:
							
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(retencion.getConceptos().get(i).getValorRetencion(),"0.0000"));
							linea.setSumaDebito(linea.getSumaDebito()+Utilidades.convertirADouble(retencion.getConceptos().get(i).getValorRetencion()));
								
							
						break;
					}
					
						
					if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).getValor().equals(""))
					{
						logger.info("inconsistencia ==============================================");
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).setDescripcionIncon("Falta definir informacion campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_DEBITO.getPosicion()).getDescripcion());
					}
				
					//*************VALIDACION CAMPO VALOR CREDITO**********************************
					switch(codigoTipoDocumento)
					{
						case ConstantesBD.codigoTipoDocInteFacturaPaciente:
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(retencion.getConceptos().get(i).getValorRetencion(),"0.0000"));
							linea.setSumaCredito(linea.getSumaCredito()+Utilidades.convertirADouble(retencion.getConceptos().get(i).getValorRetencion()));
						break;
						case ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente:
							
							lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setValor("0");
							
						break;
					}
						
					if(lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).getValor().equals(""))
					{
						logger.info("inconsistencia ==============================================");
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).setDescripcionIncon("Falta definir informacion campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_CREDITO.getPosicion()).getDescripcion());
					}
					
					//*************VALIDACION CAMPO VALOR BASE GRAVABLE*****************************
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.VALOR_BASE_GRAVABLE.getPosicion()).setValor(retencion.getConceptos().get(i).getValorBaseGravable());
					
					//*************VALIDACION CAMPO OBSERVACIONES MOVIMIENTOS*********************
					lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.OBSERVACIONES_MOVIMI.getPosicion()).setValor(rs.getString("observacion")+" "+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+" "+linea.getArrayDocumentos().get(0).getNumeroDocumento());
							
					//**************************************************************************
					
					//parametrizacion.setPosicion(parametrizacion.getPosicion()+1);
					array.add(lineaMov);
					
					if(lineaMov.isExisteInconsistencia())
					{
						linea.setExisteInconsistencia(true);
					}
					
				}
				//****************************************************************************************
				
			
			} //Fin while resultset
			
			
			
			if(!huboLineaDetMov && array.size() > 0)
			{	
				huboLineaDetMov = true;
				logger.info("..:Genero "+array.size()+" de linea "+DtoInterfazCampoS1E.getDescripcionLinea(GeneracionInterfaz1E.indicadorLineaDetMovDocumentoContable));
			}
			
			pst.close();
			rs.close();
						
			//************AGRUPAR EL DETALLE**************************************************
			lineasMovAgrupada = agruparLineasMovimientoContable(array,parametrizacion);
			//**********************************************************************************			
			
		}
		catch(SQLException e)
		{
			logger.error("Error en generarLineaMovimientoVentasHonorarios: "+e);
		}
		
		return lineasMovAgrupada;
		
	}
	
	/**
	 * @param Connection con
	 * @param DtoInterfazLineaS1E linea
	 * @param DtoInterfazS1EInfo parametrizacion
	 * */
	private static ArrayList<DtoInterfazLineaS1E> generarLineaMovimientoVentasCuentasXCobrar(//--11111
			Connection con,
			DtoInterfazLineaS1E linea,
			DtoInterfazS1EInfo parametrizacion)
	{
		ArrayList<DtoInterfazLineaS1E> array = new ArrayList<DtoInterfazLineaS1E>();
		int codigoTipoDocumento = Integer.parseInt(linea.getArrayDocumentos().get(0).getTipoDocumento());
		PreparedStatementDecorator pst = null;
		ResultSetDecorator  rs = null;
		String consulta = "";
		
		/**
		 * ANOTACION: En este método se deben generar N líneas de cuenta dependiendo de cada item encontrado 
		 * del resultset
		 * 
		 */
				
		try
		{	
			//Según el tipo de documento se hace la consulta correspondiente
			switch(codigoTipoDocumento)
			{
			case ConstantesBD.codigoTipoDocInteFacturaPaciente:
			case ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente:
				//ARMANDO. ESTA TOMANDO DOBLE EL MOVIEMINTO DE ABONO.
				if(codigoTipoDocumento==ConstantesBD.codigoTipoDocInteFacturaPaciente)
					consulta = consultarDetalleFacturasPaciente02Str;
				else if(codigoTipoDocumento==ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente)
					consulta = consultarDetalleFacturasPacienteAnuladas02Str;
				
				pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setLong(1,Long.parseLong(linea.getArrayDocumentos().size()>1?linea.getArrayDocumentos().get(1).getNumeroDocumento():linea.getArrayDocumentos().get(0).getNumeroDocumento()));
				logger.info("Consulta consultarDetalleFacturasPaciente02Str "+consultarDetalleFacturasPaciente02Str+"  >> "+Long.parseLong(linea.getArrayDocumentos().size()>1?linea.getArrayDocumentos().get(1).getNumeroDocumento():linea.getArrayDocumentos().get(0).getNumeroDocumento()));
				rs = new ResultSetDecorator(pst.executeQuery());
			break;
			
			case ConstantesBD.codigoTipoDocInteFacturasVarias:
			case ConstantesBD.codigoTipoDocInteAnulaFacturasVarias:
				consulta = consultarDetalleFacturasVarias02Str;
				pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setLong(1,Long.parseLong(linea.getArrayDocumentos().size()>1?linea.getArrayDocumentos().get(1).getNumeroDocumento():linea.getArrayDocumentos().get(0).getNumeroDocumento()));
				rs = new ResultSetDecorator(pst.executeQuery());
				
			break;
			case ConstantesBD.codigoTipoDocInteAjusFacturasVarias:
				consulta = consultarDetalleAjustesFacturasVarias02Str;
				pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setInt(1,Integer.parseInt(linea.getArrayDocumentos().get(0).getNumeroDocumento()));
				rs = new ResultSetDecorator(pst.executeQuery());
			break;
			case ConstantesBD.codigoTipoDocInteCCCapitacion:
				consulta = consultarDetalleCuentasCobroCapitacion02Str;
				pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setLong(1,Long.parseLong(linea.getArrayDocumentos().get(0).getNumeroDocumento()));
				rs = new ResultSetDecorator(pst.executeQuery());
			break;
			case ConstantesBD.codigoTipoDocInteAjustesCCCapitacion:
				consulta = consultarDetalleAjustesCuentaCobroCapitacion02Str;
				pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setLong(1,Long.parseLong(linea.getArrayDocumentos().get(0).getNumeroDocumento()));
				rs = new ResultSetDecorator(pst.executeQuery());
				
			break;
			}
			
			
			while(rs.next())
			{
				//Identificar el número de cuentas según el tipo de documento
				int numeroCuentas = 0;
				int i=0;
				
				switch(codigoTipoDocumento)
				{
				case ConstantesBD.codigoTipoDocInteFacturaPaciente:
				case ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente:
					numeroCuentas = 5;
					
					//Si el convenio es capitado no se contabiliza no la cuenta x cobrar convenio ni la cuenta a favor convenio
					if(rs.getInt("codigo_tipo_contrato")==ConstantesBD.codigoTipoContratoCapitado)
					{
						i=2;
					}
				break;
				case ConstantesBD.codigoTipoDocInteFacturasVarias:
				case ConstantesBD.codigoTipoDocInteAnulaFacturasVarias:
				case ConstantesBD.codigoTipoDocInteAjusFacturasVarias:
				case ConstantesBD.codigoTipoDocInteCCCapitacion:
				case ConstantesBD.codigoTipoDocInteAjustesCCCapitacion:
					numeroCuentas = 1;
				break;
					
				}
				
				
				while(i<numeroCuentas)
				{
					DtoInterfazLineaS1E lineaMov = new DtoInterfazLineaS1E(GeneracionInterfaz1E.indicadorLineaDetCxC);
					
					//**************VALIDACIÓN CAMPO NUMERO REGISTRO*******************************
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.NUMERO_REGISTRO.getPosicion()).setValor(parametrizacion.getPosicion()+"");
					//***************VALIDACION CAMPO COMPAÑÍA****************************************
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.COMPANIA.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.COMPANIA.getPosicion()).getValor());
					//***************VALIDACION CAMPO CENTRO OPERACION****************************************
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.CENTRO_OPERACION.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getValor());
					
					
					
					//**************VALIDACION CAMPO TIPO DE DOCUMENTO****************************************
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TIPO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.TIPO_DOCUMENTO.getPosicion()).getValor());
					//*************VALIDACION CAMPO NÚMERO DE DOCUMENTO***************************************
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.NRO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).getValor());
					
					//*************VALIDACION CAMPO AUXILIAR CUENTA CONTABLE**********************************
					DtoCuentaContable cuentaContable = new DtoCuentaContable();
					int codigoConvenio  = ConstantesBD.codigoNuncaValido;
					String codigoTipoRegimen =  "";
					String codigoTipoConvenio = "";
					int codigoInstitucion = ConstantesBD.codigoNuncaValido;
					//Dependiendo del tipo de documento
					switch(codigoTipoDocumento)
					{
						case ConstantesBD.codigoTipoDocInteFacturaPaciente:
						case ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente:
							codigoConvenio = rs.getInt("codigo_convenio");
							codigoTipoRegimen = rs.getString("codigo_tipo_regimen");
							codigoTipoConvenio = rs.getString("codigo_tipo_convenio");
							codigoInstitucion = rs.getInt("codigo_institucion");
							Boolean mensaje=false;
							//Cuenta x cobrar convenio
							if(i==0)
							{
								cuentaContable = UtilidadesInterfaz.consultarCuentaContableInterfazConvenio(con, codigoConvenio, codigoTipoRegimen, ConstantesBD.tipoCuentaConvenio, true, false);								
								if(cuentaContable.getCuentaContable().equals(""))
								{
									mensaje=true;
									cuentaContable = UtilidadesFacturacion.consultarCuentaContableTipoConvenio(con, codigoTipoConvenio, codigoInstitucion, true, false, false, false, false);
								}
								
								if(mensaje){
									cuentaContable.setMensaje("Cuenta por Cobrar Convenio Código Tipo Convenio:"+codigoTipoConvenio+" Código Institucion ");
								}else{
									cuentaContable.setMensaje("Cuenta por Cobrar Convenio Código Convenio:"+codigoConvenio+" Código Tipo Régimen: "+codigoTipoRegimen);
								}
								
							}
							//Cuenta valor a favor convenio
							else if(i==1)
							{
								mensaje=false;
								cuentaContable = UtilidadesInterfaz.consultarCuentaContableInterfazConvenio(con, codigoConvenio, codigoTipoRegimen, ConstantesBD.tipoCuentaFavorConvenio, true, false);
								if(cuentaContable.getCuentaContable().equals(""))
								{
									mensaje=true;
									cuentaContable = UtilidadesFacturacion.consultarCuentaContableTipoConvenio(con, codigoTipoConvenio, codigoInstitucion, false, true, false, false, false);
								}
								
								if(mensaje){
									cuentaContable.setMensaje("Cuenta valor a favor Convenio Código Tipo Convenio:"+codigoTipoConvenio+" Codigo Institución:"+codigoInstitucion);
								}else{
									cuentaContable.setMensaje("Cuenta valor a favor Convenio Código Convenio:"+codigoConvenio+" Código Tipo Regimen:"+codigoTipoRegimen);
								}
								
								
							}
							//Cuenta por cobrar paciente
							else if(i==2)
							{
								mensaje=false;
								cuentaContable = UtilidadesInterfaz.consultarCuentaContableInterfazConvenio(con, codigoConvenio, codigoTipoRegimen, ConstantesBD.tipoCuentaPaciente, true, false);
								if(cuentaContable.getCuentaContable().equals(""))
								{
									mensaje=true;
									cuentaContable = UtilidadesFacturacion.consultarCuentaContableTipoConvenio(con, codigoTipoConvenio, codigoInstitucion, false, false, true, false, false);
								}
								if(mensaje){
									cuentaContable.setMensaje("Cuenta por cobrar Paciente Código Tipo Convenio:"+codigoTipoConvenio+" Código Institución:"+codigoInstitucion);
								}else{
									cuentaContable.setMensaje("Cuenta por cobrar Paciente Código Convenio:"+codigoConvenio+" Código Tipo Regimen:"+codigoTipoRegimen);
								}
								
							}
							//Cuenta abono aplicado paciente
							else if(i==3)
							{
								if(!parametrizacion.getCuentaAbonoPac().equals(""))
								{
									cuentaContable.setCodigo(parametrizacion.getCuentaAbonoPac());
									cuentaContable = UtilidadesInterfaz.consultarDatosCuentaContable(con, cuentaContable);
									
								}
								else
								{
									if(!parametrizacion.isHuboInconsistenciaCuentaAbonoPac())
									{
										lineaMov.setExisteInconsistencia(true);
										lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.AUX_CUENT_CONTABLE.getPosicion()).setInconsistencia(true);
										lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.AUX_CUENT_CONTABLE.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
										lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.AUX_CUENT_CONTABLE.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
										lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.AUX_CUENT_CONTABLE.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
										lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.AUX_CUENT_CONTABLE.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).getDescripcion()+". Funcionalidad parametrizacion interfaz, campo Cuenta Abonos Paciente. ");
										parametrizacion.setHuboInconsistenciaCuentaAbonoPac(true);
									}
								}
								
								cuentaContable.setMensaje("Cuenta abono aplicado Paciente");
							}
							//Cuenta descuento paciente
							else if(i==4)
							{
								
								mensaje = false;
								cuentaContable = UtilidadesInterfaz.consultarCuentaContableInterfazConvenio(con, codigoConvenio, codigoTipoRegimen, ConstantesBD.tipoCuentaDescuentosPaciente, true, false);
								if(cuentaContable.getCuentaContable().equals(""))
								{
									mensaje = true;
									cuentaContable = UtilidadesFacturacion.consultarCuentaContableTipoConvenio(con, codigoTipoConvenio, codigoInstitucion, false, false, false, true, false);
								}
								
								if(mensaje){
									cuentaContable.setMensaje("Cuenta descuento Paciente Código Tipo Convenio:"+codigoTipoConvenio+" Código Institución:"+codigoInstitucion);
								}else{
									cuentaContable.setMensaje("Cuenta descuento Pacient Código Convenio:"+codigoConvenio+" Código Tipo Régimen:"+codigoTipoRegimen);
								}
								
							}
						break;
						case ConstantesBD.codigoTipoDocInteFacturasVarias:
						case ConstantesBD.codigoTipoDocInteAnulaFacturasVarias:
						case ConstantesBD.codigoTipoDocInteAjusFacturasVarias:
							cuentaContable.setCodigo(rs.getString("cuenta_contable"));
							if(!cuentaContable.getCodigo().equals(""))
							{
								cuentaContable = UtilidadesInterfaz.consultarDatosCuentaContable(con, cuentaContable);
							}
						break;
						case ConstantesBD.codigoTipoDocInteCCCapitacion:
						case ConstantesBD.codigoTipoDocInteAjustesCCCapitacion:
							codigoConvenio = rs.getInt("codigo_convenio");
							codigoTipoRegimen = rs.getString("codigo_tipo_regimen");
							codigoTipoConvenio = rs.getString("codigo_tipo_convenio");
							codigoInstitucion = rs.getInt("codigo_institucion");
							cuentaContable = UtilidadesInterfaz.consultarCuentaContableInterfazConvenio(con, codigoConvenio, codigoTipoRegimen, ConstantesBD.tipoCuentaConvenio, false, true);
							if(cuentaContable.getCuentaContable().equals(""))
							{
								cuentaContable = UtilidadesFacturacion.consultarCuentaContableTipoConvenio(con, codigoTipoConvenio, codigoInstitucion, false, false, false, false, true);
							}
						break;
					} //FIN SWITCH
					///Si no hubo cuenta contable se genera inconsistencia
					if(cuentaContable.getCuentaContable().equals(""))
					{
						//logger.info("codigoConvenio: "+codigoConvenio);
						//logger.info("codigoTipoRegimen: "+codigoTipoRegimen);
						//logger.info("codigoTipoConvenio: "+codigoTipoConvenio);
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.AUX_CUENT_CONTABLE.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.AUX_CUENT_CONTABLE.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.AUX_CUENT_CONTABLE.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.AUX_CUENT_CONTABLE.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.AUX_CUENT_CONTABLE.getPosicion()).setDescripcionIncon("No se encontro informacion campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.AUX_CUENT_CONTABLE.getPosicion()).getDescripcion()+". ["+cuentaContable.getMensaje()+"] ");
					}
					else
					{
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.AUX_CUENT_CONTABLE.getPosicion()).setValor(cuentaContable.getCuentaContable());
					}
					
					
					//******************************************************************************************
					
					//*************VALIDACION CAMPO TERCERO**********************************
					if(cuentaContable.isManejaTerceros())
					{
						if(codigoTipoDocumento == ConstantesBD.codigoTipoDocInteFacturaPaciente || 
								codigoTipoDocumento == ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente)
						{
							if(rs.getString("codigo_tipo_regimen").equals(ConstantesBD.codigoTipoRegimenParticular))
							{
								//Valor por cobrar convenio , Valor a favor convenio
								if(i==0 || i==1)
								{
									lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).setValor("");
								}
								//Valor paciente
								else if (i==2 || i==4)
								{
									if(!parametrizacion.getNitTerceroGenFacPar().toString().equals("") 
											&& !parametrizacion.getTerceroGenFacPar().toString().equals(ConstantesBD.codigoNuncaValido+""))
										lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).setValor(parametrizacion.getNitTerceroGenFacPar().toString());
									else
										lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).setValor(rs.getString("id_paciente"));
								}
								//Valor abono aplicado paciente
								else if(i==3)
								{
									//Se asigna el tercero genérico de facturas particulares
									if(!parametrizacion.getNitTerceroGenFacPar().toString().equals("") 
											&& !parametrizacion.getTerceroGenFacPar().toString().equals(ConstantesBD.codigoNuncaValido+""))
									{
										lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).setValor(parametrizacion.getNitTerceroGenFacPar().toString());
									}
									
								}
							}
							else
							{
								//Valor por cobrar convenio - Valor a favor convenio
								if(i==0 || i==1)
								{
									lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).setValor(rs.getString("id_tercero"));
								}
								//Valor paciente
								else if (i==2 || i==4)
								{									
									if(!parametrizacion.getNitTerceroGenPagPac().toString().equals("") && 
											!parametrizacion.getTerceroGenPagPac().toString().equals(ConstantesBD.codigoNuncaValido+""))
										lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).setValor(parametrizacion.getNitTerceroGenPagPac().toString());
									else
										lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).setValor(rs.getString("id_paciente"));
								}
								//Valor abono aplicado paciente
								else if(i==3)
								{
									//Se asigna el tercero genérico de facturas particulares
									if(!parametrizacion.getNitTerceroGenFacPar().toString().equals("") 
											&& !parametrizacion.getTerceroGenFacPar().toString().equals(ConstantesBD.codigoNuncaValido+""))
									{
										lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).setValor(parametrizacion.getNitTerceroGenFacPar().toString());
									}
								}
							}
							
							/*if(!(rs.getString("codigo_tipo_regimen").toString().equals(ConstantesBD.codigoTipoRegimenParticular) && (i==0  || i == 1)))
							{*/
								//Valor por cobrar convenio , Valor a favor convenio
								if(lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).getValor().equals(""))
								{
									lineaMov.setExisteInconsistencia(true);
									lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).setInconsistencia(true);
									lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
									lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
									lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
									lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).getDescripcion());
								}
							//}
						}
						else
						{	
							lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.TERCERO_DOCUMENTO.getPosicion()).getValor());
							if(lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).getValor().equals(""))
							{
								lineaMov.setExisteInconsistencia(true);
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).setInconsistencia(true);
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).getDescripcion());
							}
						}
					}
					else
					{
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).setValor("");
					}
					
					//*************VALIDACION CAMPO CENTRO OPERACION MOVIMIENTO**********************************
					switch(codigoTipoDocumento)
					{
						case ConstantesBD.codigoTipoDocInteFacturaPaciente:
						case ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente:
							//Cuenta x cobrar convenio, Cuenta a favor convenio
							if(i==0 || i==1)
							{
								//Si el convenio es capitado se toma el codigo de centro de operaicon de la factura de lo contrario se toma 
								//el cdoigo centro atencion contable administrativo de la aprametrizacion de interfaz
								if(rs.getInt("codigo_tipo_contrato")==ConstantesBD.codigoTipoContratoCapitado)
								{
									lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.CENTRO_OPER_MOVI.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getValor());
								}
								else
								{
									asignarCodigoCentroAtencionContable(lineaMov, parametrizacion, CampoMoviCuentasXCobrar.CENTRO_OPER_MOVI.getPosicion(), "");
								}
							}
							//Cuenta por cobrar paciente , cuenta abono paciente, cuenta descuento paciente
							else if(i==2 || i==3 || i==4)
							{
								// se asigna el centro de atencion de la parametrizacion de uinterfaz S1E
								asignarCodigoCentroAtencionContable(lineaMov, parametrizacion, CampoMoviCuentasXCobrar.CENTRO_OPER_MOVI.getPosicion(), "");
							}
							
						break;
						case ConstantesBD.codigoTipoDocInteFacturasVarias:
						case ConstantesBD.codigoTipoDocInteAnulaFacturasVarias:
						case ConstantesBD.codigoTipoDocInteAjusFacturasVarias:
						case ConstantesBD.codigoTipoDocInteCCCapitacion:
						case ConstantesBD.codigoTipoDocInteAjustesCCCapitacion:
							// se asigna el centro de atencion de la parametrizacion de uinterfaz S1E
							asignarCodigoCentroAtencionContable(lineaMov, parametrizacion, CampoMoviCuentasXCobrar.CENTRO_OPER_MOVI.getPosicion(), "");
						break;
					
					}
					if(lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.CENTRO_OPER_MOVI.getPosicion()).getValor().equals(""))
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.CENTRO_OPER_MOVI.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.CENTRO_OPER_MOVI.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.CENTRO_OPER_MOVI.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.CENTRO_OPER_MOVI.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.CENTRO_OPER_MOVI.getPosicion()).setDescripcionIncon("No se encontro informacion del campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.CENTRO_OPER_MOVI.getPosicion()).getDescripcion());
					}
					else
					{
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.CENTRO_OPER_MOVI.getPosicion()).setValor(lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.CENTRO_OPER_MOVI.getPosicion()).getValor().substring(0,3));
					}
					
					//*************VALIDACION CAMPO UNIDAD DE NEGOCIO**********************************
					// GIO
					if(codigoTipoDocumento == ConstantesBD.codigoTipoDocInteFacturaPaciente || codigoTipoDocumento == ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente )
					{
						asignarUnidadFuncionalEstandar(lineaMov,parametrizacion,CampoMoviCuentasXCobrar.UNIDAD_NEGOCIO.getPosicion(),linea,ConstantesIntegridadDominio.acronimoVentas);
					}
					else
					{
						if (codigoTipoDocumento == ConstantesBD.codigoTipoDocInteCCCapitacion  )
						   {
							
							//asignarUnidadFuncionalEstandarParamGeneral(lineaMov,parametrizacion,CampoMoviCuentasXCobrar.UNIDAD_NEGOCIO.getPosicion(),linea,""); // CAMBIO por TAREA 152926 Agosto 20 - 2010
							 lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.UNIDAD_NEGOCIO.getPosicion()).setValor(parametrizacion.getUnidMovCxcCap());
						     logger.info("\n\n\n\n ************* ENTRA 1************** \n >> codigo tipo Doc: "+codigoTipoDocumento+" \n");
						   }
						   else
						    {
							  logger.info("\n\n\n\n ************* ENTRA 2************** \n >> codigo tipo Doc: "+codigoTipoDocumento+" \n"); 
							 asignarUnidadFuncionalEstandar(lineaMov,parametrizacion,CampoMoviCuentasXCobrar.UNIDAD_NEGOCIO.getPosicion(),linea,"");
						    }
					
					}
					//*************************************************************************************
					//*************VALIDACION CAMPO AUXILIAR DE CENTROS DE COSTO*******************************
					//Este campo solo aplica para DEscuentos de paciente en los documentos Factura Paciente y Anulacion Factura Paciente
					if(i==4 && (codigoTipoDocumento == ConstantesBD.codigoTipoDocInteFacturaPaciente || codigoTipoDocumento == ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente))
					{
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.AUX_CENTRO_COSTO.getPosicion()).setValor(rs.getString("codigo_area"));
						
					}
					//******************************************************************************************
					//**************VALIDACION CAMPO VALOR DEBITO********************************************
					//Segun tipo de documento
					switch(codigoTipoDocumento)
					{
						case ConstantesBD.codigoTipoDocInteFacturaPaciente:
							//Valor por cobrar convenio
							if(i==0)
							{
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getDouble("valor_cobrar_convenio"),"0.0000"));
							}
							//Valor a favor convenio
							else if(i==1)
							{
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).setValor("0");
							}
							//Valor paciente
							else if (i==2)
							{
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getDouble("valor_paciente"),"0.0000"));
							}
							//Valor abono aplicado paciente
							else if(i==3)
							{
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getDouble("valor_abonos"),"0.0000"));
							}
							//Valor descuento paciente
							else if(i==4)
							{
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getDouble("valor_descuento"),"0.0000"));
							}
						break;
						case ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente:
							//Valor por cobrar convenio, valor paciente, valor abono aplicado paciente y valor descuento paciente
							if(i==0 || i==2 || i==3 || i==4)
							{
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).setValor("0");
							}
							//Valor a favor convenio
							else if(i==1)
							{
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getDouble("valor_favor_convenio"),"0.0000"));
							}
						break;
						case ConstantesBD.codigoTipoDocInteFacturasVarias:
						case ConstantesBD.codigoTipoDocInteCCCapitacion:
							lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getDouble("valor"),"0.0000"));
						break;
						case ConstantesBD.codigoTipoDocInteAnulaFacturasVarias:
							lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).setValor("0");
						break;
						case ConstantesBD.codigoTipoDocInteAjusFacturasVarias:
							if(rs.getString("tipo_ajuste").equals(ConstantesIntegridadDominio.acronimoDebito))
							{
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getDouble("valor"),"0.0000"));
							}
							else
							{
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).setValor("0");
							}
						break;
						case ConstantesBD.codigoTipoDocInteAjustesCCCapitacion:
							int codTipoAjuste = rs.getInt("tipo_ajuste");
							if(codTipoAjuste == ConstantesBD.codigoAjusteDebitoCuentaCobro || codTipoAjuste == ConstantesBD.codigoAjusteDebitoFactura || codTipoAjuste == ConstantesBD.codigoConceptosCarteraDebito)
							{
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getDouble("valor"),"0.0000"));
							}
							else
							{
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).setValor("0");
							}
						break;
						
					}
					
					if(lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).getValor().equals(""))
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).setDescripcionIncon("No se encontro informacion del campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).getDescripcion());
					}
					else
					{
						linea.setSumaDebito(linea.getSumaDebito()+Utilidades.convertirADouble(lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).getValor()));
					}
					
					
					//*******************************************************************************************
					//******************VALIDACION CAMPO VALOR CREDITO**********************************************
					//Segun tipo de documento
					switch(codigoTipoDocumento)
					{
						case ConstantesBD.codigoTipoDocInteFacturaPaciente:
							//Valor por cobrar convenio, valor paciente, valor abono aplicado paciente y valor descuento paciente
							if(i==0 || i==2 || i==3 || i==4)
							{
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).setValor("0");
							}
							//Valor a favor convenio
							else if(i==1)
							{
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getDouble("valor_favor_convenio"),"0.0000"));
							}
							
						break;
						case ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente:
							//Valor por cobrar convenio
							if(i==0)
							{
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getDouble("valor_cobrar_convenio"),"0.0000"));
							}
							//Valor a favor convenio
							else if(i==1)
							{
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).setValor("0");
							}
							//Valor paciente
							else if (i==2)
							{
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getDouble("valor_paciente"),"0.0000"));
							}
							//Valor abono aplicado paciente
							else if(i==3)
							{
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getDouble("valor_abonos"),"0.0000"));
							}
							//Valor descuento paciente
							else if(i==4)
							{
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getDouble("valor_descuento"),"0.0000"));
							}
						break;
						case ConstantesBD.codigoTipoDocInteFacturasVarias:
						case ConstantesBD.codigoTipoDocInteCCCapitacion:
							lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).setValor("0");
							
						break;
						case ConstantesBD.codigoTipoDocInteAnulaFacturasVarias:
							lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getDouble("valor"),"0.0000"));
						break;
						case ConstantesBD.codigoTipoDocInteAjusFacturasVarias:
							if(rs.getString("tipo_ajuste").equals(ConstantesIntegridadDominio.acronimoDebito))
							{
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).setValor("0");
							}
							else
							{
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getDouble("valor"),"0.0000"));
								
							}
						break;
						case ConstantesBD.codigoTipoDocInteAjustesCCCapitacion:
							int codTipoAjuste = rs.getInt("tipo_ajuste");
							if(codTipoAjuste == ConstantesBD.codigoAjusteDebitoCuentaCobro || codTipoAjuste == ConstantesBD.codigoAjusteDebitoFactura || codTipoAjuste == ConstantesBD.codigoConceptosCarteraDebito)
							{
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).setValor("0");
							}
							else
							{
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getDouble("valor"),"0.0000"));
								
							}
						break;
						
					}
					if(lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).getValor().equals(""))
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).setDescripcionIncon("No se encontro informacion del campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).getDescripcion());
					}
					else
					{
						linea.setSumaCredito(linea.getSumaCredito()+Utilidades.convertirADouble(lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).getValor()));
					}
					
					
					
					//************************************************************************************************
					//************************VALIDACION CAMPO OBSERVACIONES MOVIMIENTO********************************
					String observacion = rs.getString("observacion");
					if(!observacion.equals(""))
					{
						
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.OBSERVACIONES_MOVIMIENTO.getPosicion()).setValor(observacion+" "+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+" "+linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).getValor());					
					}
					//*************************************************************************************************
					//**************************VALIDACION CAMPO TIPO DOCUMENTO CRUCE************************************
					asignarTipoDocCruzeParametrizacion(lineaMov, parametrizacion, CampoMoviCuentasXCobrar.TIPO_DOC_CRUCE.getPosicion(), linea,true, linea.getArrayCampos().get(CampoDocumentoContable.TIPO_DOCUMENTO.getPosicion()).getValor());
					
					
					/*********if(codigoTipoDocumento == ConstantesBD.codigoTipoDocInteFacturaPaciente || codigoTipoDocumento == ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente )
					{
					//*************************************************************************************************
					//***************************VALIDACION CAMPO NÚMERO DOCUMENTO CRUCE*********************************
					/*
					 * Anotacion: Si la línea tiene mas de 1 documento asociado, se pone el segundo, pero si solo tiene 1 se pone ese.
					 */
					 lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.NRO_DOC_CRUCE.getPosicion()).setValor(linea.getArrayDocumentos().size()>1?linea.getArrayDocumentos().get(1).getNumeroDocumento():linea.getArrayDocumentos().get(0).getNumeroDocumento());
					
					switch(codigoTipoDocumento)
					{
						case ConstantesBD.codigoTipoDocInteFacturaPaciente:
						case ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente:
					
					//Valor por cobrar convenio, Valor a favor convenio
						if(i==0 || i==1)
						{
							//Si el convenio es  igual a capitado se toma el consecutivo del Ingreso en caso de tener Abonos 
							//(si el convenio es diferente de capitado  se deja le numero de la factura) 
							 
							 if(rs.getInt("codigo_tipo_contrato")==ConstantesBD.codigoTipoContratoCapitado)
							 {
								// Tiene Abonos				
								logger.info("Codigo TIPO ingreso >>> "+rs.getInt("codigo_tipo_ingreso"));
								   if( (rs.getInt("codigo_tipo_ingreso")>ConstantesBD.codigoNuncaValido)	|| ( Utilidades.convertirAEntero(rs.getString("codigo_tipo_ingreso"))==ConstantesBD.tipoMovimientoAbonoFacturacion || Utilidades.convertirAEntero(rs.getString("codigo_tipo_ingreso"))==ConstantesBD.tipoMovimientoAbonoAnulacionFactura))
								    {
										lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.NRO_DOC_CRUCE.getPosicion()).setValor(rs.getString("consecutivo_ingreso"));
									}
							 }
						}
						//Valor abono aplicado paciente
						else if(i==3)
						{   // Tiene Abonos				
							logger.info("Codigo TIPO ingreso >>> "+rs.getInt("codigo_tipo_ingreso"));
							if( (rs.getInt("codigo_tipo_ingreso")>ConstantesBD.codigoNuncaValido)	|| ( Utilidades.convertirAEntero(rs.getString("codigo_tipo_ingreso"))==ConstantesBD.tipoMovimientoAbonoFacturacion || Utilidades.convertirAEntero(rs.getString("codigo_tipo_ingreso"))==ConstantesBD.tipoMovimientoAbonoAnulacionFactura))
						    {
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.NRO_DOC_CRUCE.getPosicion()).setValor(rs.getString("consecutivo_ingreso"));
							}
						}
					   
					
					 break;
					}
					logger.info("\n\n\n NRO DOCUMENTO CRUCE DEFINITIVO >> "+lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.NRO_DOC_CRUCE.getPosicion()).getValor());
					//****************************************************************************************************
					//***************************VALIDACION CAMPO FECHA VENCIMIENTO DOCUMENTO******************************

					if(codigoTipoDocumento==ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente||codigoTipoDocumento==ConstantesBD.codigoTipoDocInteAnulaFacturasVarias)
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.FECHA_VEN_DOC.getPosicion()).setValor(rs.getString("fechaanulacion"));
					else
					{
						if(!rs.getString("fecha").equals(""))
							lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.FECHA_VEN_DOC.getPosicion()).setValor(UtilidadFecha.incrementarDiasAFecha(rs.getString("fecha"), rs.getInt("dias"), false));
					}
					
					if(lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.FECHA_VEN_DOC.getPosicion()).getValor().equals(""))
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.FECHA_VEN_DOC.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.FECHA_VEN_DOC.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.FECHA_VEN_DOC.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.FECHA_VEN_DOC.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.FECHA_VEN_DOC.getPosicion()).setDescripcionIncon("No se encontro informacion del campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.FECHA_VEN_DOC.getPosicion()).getDescripcion());
					}
					//******************************************************************************************************
					//************************VALIDACION CAMPO FECHA PRONTO PAGO DOCUMENTO*************************************
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.FECHA_PAG_DOC.getPosicion()).setValor(parametrizacion.getFechaProceso());
					//***********************************************************************************************************
					//*************************VALIDACION CAMPO TERCERO VENDEDOR****************************************************
					asignarIdentificacionInstitucion(lineaMov, parametrizacion, CampoMoviCuentasXCobrar.TERCERO_VENDEDOR.getPosicion());
					//*****************************************************************************************************************
					//**************************VALIDACION CAMPO OBSERVACIONES MOVIMIENTO SALDO ABIERTO************************************
					if(!lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.OBSERVACIONES_MOVIMIENTO.getPosicion()).getValor().equals(""))
					{
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.OBS_SALDO_ABI.getPosicion()).setValor(lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.OBSERVACIONES_MOVIMIENTO.getPosicion()).getValor());
					}
					else
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.OBS_SALDO_ABI.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.OBS_SALDO_ABI.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.OBS_SALDO_ABI.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.OBS_SALDO_ABI.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.OBS_SALDO_ABI.getPosicion()).setDescripcionIncon("No se encontro informacion del beneficiario en el campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.OBS_SALDO_ABI.getPosicion()).getDescripcion());
					}
					//***********************************************************************************************************************

					if(Utilidades.convertirADouble(lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).getValor()) > 0 || 
							Utilidades.convertirADouble(lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).getValor()) > 0)
					{
						parametrizacion.setPosicion(parametrizacion.getPosicion()+1);
						array.add(lineaMov);
						
						if(lineaMov.isExisteInconsistencia())
						{
							linea.setExisteInconsistencia(true);
						}
					}
					i++;
				} //Fin while numeros cuentas
			} //Fin while resultSet
			
			if(!huboLineaCxC && array.size() > 0)
			{	
				huboLineaCxC = true;
				logger.info("..:Genero "+array.size()+" de linea "+DtoInterfazCampoS1E.getDescripcionLinea(GeneracionInterfaz1E.indicadorLineaDetCxC));
			}
			
			pst.close();
			rs.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en generarLineaMovimientoVentasHonroariosCuentasXCobrar: "+e+" >> "+consulta);
		}
		
		
		return array;
		
	}
	
	
	/**
	 * @param retencion 
	 * @param Connection con
	 * @param DtoInterfazLineaS1E linea
	 * @param DtoInterfazS1EInfo parametrizacion
	 * */
	private static ArrayList<DtoInterfazLineaS1E> generarLineaMovimientoHonorariosCuentasXPagar(
			Connection con,
			DtoInterfazLineaS1E linea,
			DtoInterfazS1EInfo parametrizacion)
	{
		ArrayList<DtoInterfazLineaS1E> array = new ArrayList<DtoInterfazLineaS1E>();
		ArrayList<DtoInterfazLineaS1E> lineasMovAgrupada= new ArrayList<DtoInterfazLineaS1E>();
		
		DtoCuentaContable cuentaContable = new DtoCuentaContable();
		PreparedStatementDecorator pst = null;
		ResultSetDecorator rs = null;
		int codigoTipoDocumento = Integer.parseInt(linea.getArrayDocumentos().get(0).getTipoDocumento());
		
		try
		{
			//PARA VENTAS Y HONORARIOS, CUENTAS X PAGAR SOLO APLICA PARA FACTURAS Y ANULACIONES 
			if(codigoTipoDocumento == ConstantesBD.codigoTipoDocInteFacturaPaciente || codigoTipoDocumento == ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente)
			{
			
				Long documento=Long.parseLong(linea.getArrayDocumentos().size()>1?linea.getArrayDocumentos().get(1).getNumeroDocumento():linea.getArrayDocumentos().get(0).getNumeroDocumento());
				pst = new PreparedStatementDecorator(con.prepareStatement(consultarDetalleFacturasPaciente03Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				
				pst.setLong(1,documento);
				pst.setLong(2,documento);
				pst.setLong(3,documento);
				pst.setLong(4,documento);
				pst.setLong(5,documento);
				
				logger.info("consulta --> "+ pst);

				rs = new ResultSetDecorator(pst.executeQuery());
				
			
				while(rs.next())
				{
					DtoInterfazLineaS1E lineaMov = new DtoInterfazLineaS1E(GeneracionInterfaz1E.indicadorLineaDetCxP);
					
					//**************VALIDACIÓN CAMPO NUMERO REGISTRO*******************************
					//lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.NUMERO_REGISTRO.getPosicion()).setValor(parametrizacion.getPosicion()+"");
					//***************VALIDACION CAMPO COMPAÑÍA****************************************
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.COMPANIA.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.COMPANIA.getPosicion()).getValor());
					//***************VALIDACION CAMPO CENTRO OPERACION****************************************
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.CENTRO_OPERACION.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getValor());
					//**************VALIDACION CAMPO TIPO DE DOCUMENTO****************************************
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.TIPO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.TIPO_DOCUMENTO.getPosicion()).getValor());
					//*************VALIDACION CAMPO NÚMERO DE DOCUMENTO***************************************
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.NRO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).getValor());
					//*************VALIDACION CAMPO AUXILIAR CUENTA CONTABLE**********************************
					cuentaContable = UtilidadesFacturacion.consultarCuentaContableParticipacionPoolTarifasConvenio(con, rs.getInt("codigo_pool"), rs.getInt("codigo_convenio"), rs.getInt("codigo_esquema_tarifario"), Integer.parseInt(parametrizacion.getInstitucionBasica().getCodigo()) , true, false, false);
					if(cuentaContable.getCuentaContable().equals(""))
					{
						cuentaContable.setCodigo(rs.getString("cuenta_contable"));
						if(!cuentaContable.getCodigo().equals(""))
						{
							cuentaContable = UtilidadesInterfaz.consultarDatosCuentaContable(con, cuentaContable);
						}
					}
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.AUX_CUENT_CONTABLE.getPosicion()).setValor(cuentaContable.getCuentaContable());
					if(lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.AUX_CUENT_CONTABLE.getPosicion()).getValor().equals(""))
					{
						logger.info("inconsistencia ==============================================");
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.AUX_CUENT_CONTABLE.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.AUX_CUENT_CONTABLE.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.AUX_CUENT_CONTABLE.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.AUX_CUENT_CONTABLE.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.AUX_CUENT_CONTABLE.getPosicion()).setDescripcionIncon(mensajeEInconNoInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.AUX_CUENT_CONTABLE.getPosicion()).getDescripcion());
					}
					if(linea.getArrayDocumentos().get(0).getNumeroDocumento().equals("18535")||linea.getArrayDocumentos().get(0).getNumeroDocumento().equals("105"))
					{
						logger.info("cuenta x pagar: "+cuentaContable.getCuentaContable());
					}
					//******************************************************************************************
					
					//*************VALIDACION CAMPO TERCERO**********************************
					if(cuentaContable.isManejaTerceros())
					{
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.TERCERO.getPosicion()).setValor(rs.getString("id_tercero"));
						if(lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.TERCERO.getPosicion()).getValor().equals(""))
						{
							logger.info("inconsistencia ==============================================");
							lineaMov.setExisteInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.TERCERO.getPosicion()).setInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.TERCERO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
							lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.TERCERO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
							lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.TERCERO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
							lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.TERCERO.getPosicion()).setDescripcionIncon(mensajeEInconNoInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.TERCERO.getPosicion()).getDescripcion());
						}
					}
					else
					{
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.TERCERO.getPosicion()).setValor("");
					}
					
					//*************VALIDACION CAMPO CENTRO OPERACION MOVIMIENTO**********************************
					asignarCodigoCentroAtencionContable(lineaMov, parametrizacion, CampoMoviCuentasXPagar.CENTRO_OPER_MOVI.getPosicion(), "");
					if(lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.CENTRO_OPER_MOVI.getPosicion()).getValor().equals(""))
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.CENTRO_OPER_MOVI.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.CENTRO_OPER_MOVI.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.CENTRO_OPER_MOVI.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.CENTRO_OPER_MOVI.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.CENTRO_OPER_MOVI.getPosicion()).setDescripcionIncon("No se encontro informacion del campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.CENTRO_OPER_MOVI.getPosicion()).getDescripcion());
					}
					else
					{
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.CENTRO_OPER_MOVI.getPosicion()).setValor(lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.CENTRO_OPER_MOVI.getPosicion()).getValor().substring(0,3));
					}
					
					//*************VALIDACION CAMPO UNIDAD DE NEGOCIO**********************************
					if(codigoTipoDocumento == ConstantesBD.codigoTipoDocInteFacturaPaciente || codigoTipoDocumento == ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente)
					{
						asignarUnidadFuncionalEstandar(lineaMov,parametrizacion,CampoMoviCuentasXPagar.UNIDAD_NEGOCIO.getPosicion(),linea,ConstantesIntegridadDominio.acronimoHonorarios);
					}
					else
					{
						asignarUnidadFuncionalEstandar(lineaMov,parametrizacion,CampoMoviCuentasXPagar.UNIDAD_NEGOCIO.getPosicion(),linea,"");
					}
					
					//*************************************************************************************
					//**************VALIDACION CAMPO VALOR DEBITO********************************************
					//Según tipo de documento
					switch(codigoTipoDocumento)
					{
						case ConstantesBD.codigoTipoDocInteFacturaPaciente:
							lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setValor("0");
						break;
						case ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente:
							DtoRetencion retencion = UtilidadesInterfaz.calcularRetencionInterfaz(con, linea.getArrayDocumentos(), parametrizacion, rs.getString("id_tercero"), true,rs.getDouble("valor")); 
							
							for(String mensajeIncon:retencion.getInconsistenciasRetencion())
							{
								logger.info("inconsistencia ==============================================");
								linea.setExisteInconsistencia(true);
								linea.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setInconsistencia(true);
								linea.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
								linea.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
								linea.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
								linea.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setDescripcionIncon(mensajeIncon);
							}
							double valorDebito = rs.getDouble("valor") - retencion.consultarTotalValorRetencion();
							if(valorDebito>0)
							{
								lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(valorDebito,"0.0000"));
								linea.setSumaDebito(linea.getSumaDebito()+valorDebito);
							}
							if(Utilidades.convertirADouble(lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).getValor())<=0)
							{
								logger.info("inconsistencia ==============================================");
								lineaMov.setExisteInconsistencia(true);
								lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setInconsistencia(true);
								lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
								lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
								lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
								if(valorDebito<0)
								{
									lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setDescripcionIncon(mensajeEInconRestaRetencion+ " campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).getDescripcion());
								}
								else
								{
									lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setDescripcionIncon(mensajeEInconNoInfo+ " campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).getDescripcion());
								}
							}
						break;
					}
					if(linea.getArrayDocumentos().get(0).getNumeroDocumento().equals("18535")||linea.getArrayDocumentos().get(0).getNumeroDocumento().equals("105"))
					{
						logger.info("valor debito: "+lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).getValor());
					}
					
					
					//*******************************************************************************************
					//******************VALIDACION CAMPO VALOR CREDITO**********************************************
					//Según tipo documento
					switch(codigoTipoDocumento)
					{
						case ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente:
							lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).setValor("0");
						break;
						case ConstantesBD.codigoTipoDocInteFacturaPaciente:
							DtoRetencion retencion = UtilidadesInterfaz.calcularRetencionInterfaz(con, linea.getArrayDocumentos(), parametrizacion, rs.getString("id_tercero"), true,rs.getDouble("valor")); 
							
							for(String mensajeIncon:retencion.getInconsistenciasRetencion())
							{
								logger.info("inconsistencia ==============================================");
								linea.setExisteInconsistencia(true);
								linea.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).setInconsistencia(true);
								linea.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
								linea.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
								linea.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
								linea.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).setDescripcionIncon(mensajeIncon);
							}
							
							double valorCredito = rs.getDouble("valor") - retencion.consultarTotalValorRetencion();
							if(valorCredito>0)
							{
								lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(valorCredito,"0.0000"));
								linea.setSumaCredito(linea.getSumaCredito()+valorCredito);
							}
							if(Utilidades.convertirADouble(lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).getValor())<=0)
							{
								logger.info("inconsistencia ==============================================");
								lineaMov.setExisteInconsistencia(true);
								lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).setInconsistencia(true);
								lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
								lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
								lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
								if(valorCredito<0)
								{
									lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).setDescripcionIncon(mensajeEInconRestaRetencion+" campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).getDescripcion());	
								}
								else
								{
									lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).setDescripcionIncon(mensajeEInconNoInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).getDescripcion());
								}
								
							}
						break;
					}
					if(linea.getArrayDocumentos().get(0).getNumeroDocumento().equals("18535")||linea.getArrayDocumentos().get(0).getNumeroDocumento().equals("105"))
					{
						logger.info("valor credito: "+lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).getValor());
					}
					
					//************************************************************************************************
					//************************VALIDACION CAMPO OBSERVACIONES MOVIMIENTO********************************
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.OBSERVACIONES_MOVIMIENTO.getPosicion()).setValor(rs.getString("observacion")+" "+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+" "+linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).getValor());
					
					//*************************************************************************************************
					//**************************VALIDACION CAMPO TIPO DOCUMENTO CRUCE************************************
					/* 	
						Se comenta x ke en en el anexo 781 dice: 
						Asignar el tipo de documento de cruce parametrizado en la funcionalidad 'parametrización interfaz sistema 1E', campo 'Documento cruce para honorarios internos'
						asignarTipoDocCruzeParametrizacion(lineaMov, parametrizacion, CampoMoviCuentasXPagar.TIPO_DOC_CRUCE.getPosicion(), linea,false, linea.getArrayCampos().get(CampoDocumentoContable.TIPO_DOCUMENTO.getPosicion()).getValor());
					*/
					
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.TIPO_DOC_CRUCE.getPosicion()).setValor(parametrizacion.getDocumentoCruceHi());
					//*************************************************************************************************
					//***************************VALIDACION CAMPO NÚMERO DOCUMENTO CRUCE*********************************
					
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.NRO_DOC_CRUCE.getPosicion()).setValor(rs.getString("consecutivo_solicitud"));
					
					
					if(lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.NRO_DOC_CRUCE.getPosicion()).getValor().equals(""))
					{
						logger.info("inconsistencia ==============================================");
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.NRO_DOC_CRUCE.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.NRO_DOC_CRUCE.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.NRO_DOC_CRUCE.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.NRO_DOC_CRUCE.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.NRO_DOC_CRUCE.getPosicion()).setDescripcionIncon(mensajeEInconNoInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.NRO_DOC_CRUCE.getPosicion()).getDescripcion());
					}
					
					
					//****************************************************************************************************
					//***************************VALIDACION CAMPO AUXILIAR CONCEPTO FLUJO EFECTIVO***************************
					asignarConceptoFlujoEfectivoCXP(lineaMov, parametrizacion, CampoMoviCuentasXPagar.AUX_CONCEP_FLUJO.getPosicion(), linea);
					//************************************************************************************************************
					//***************************VALIDACION CAMPO FECHA VENCIMIENTO DOCUMENTO******************************
					
					
					
					
					
					if(codigoTipoDocumento==ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente)
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.FECHA_VEN_DOC.getPosicion()).setValor(rs.getString("fechaanulacion"));
					else
					{
						if(!rs.getString("fecha").equals(""))
							lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.FECHA_VEN_DOC.getPosicion()).setValor(UtilidadFecha.incrementarDiasAFecha(rs.getString("fecha"), rs.getInt("dias"), false));
					}

					if(lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.FECHA_VEN_DOC.getPosicion()).getValor().equals(""))
					{
						logger.info("inconsistencia ==============================================");
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.FECHA_VEN_DOC.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.FECHA_VEN_DOC.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.FECHA_VEN_DOC.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.FECHA_VEN_DOC.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.FECHA_VEN_DOC.getPosicion()).setDescripcionIncon(mensajeEInconNoInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.FECHA_VEN_DOC.getPosicion()).getDescripcion());
					}
					//******************************************************************************************************
					//************************VALIDACION CAMPO FECHA PRONTO PAGO DOCUMENTO*************************************
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.FECHA_PAG_DOC.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.FECHA_DOCUMENTO.getPosicion()).getValor());
					//***********************************************************************************************************
					
					//*****************************************************************************************************************
					
					//**************************VALIDACION CAMPO OBSERVACIONES MOVIMIENTO SALDO ABIERTO************************************
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.OBS_SALDO_ABI.getPosicion()).setValor(lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.OBSERVACIONES_MOVIMIENTO.getPosicion()).getValor());
					if(lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.OBS_SALDO_ABI.getPosicion()).getValor().equals(""))
					{
						logger.info("inconsistencia ==============================================");
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.OBS_SALDO_ABI.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.OBS_SALDO_ABI.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.OBS_SALDO_ABI.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.OBS_SALDO_ABI.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.OBS_SALDO_ABI.getPosicion()).setDescripcionIncon(mensajeEInconNoInfo+" del beneficiario");
					}
					//***********************************************************************************************************************
					
					if(Utilidades.convertirADouble(lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).getValor()) > 0 || 
							Utilidades.convertirADouble(lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).getValor())>0 )
					{	
						array.add(lineaMov);
						
						if(lineaMov.isExisteInconsistencia())
						{
							linea.setExisteInconsistencia(true);
						}
					}
				} //Fin while
			
				pst.close();
				rs.close();
				
				//*****************SE REALIZA LA AGRUPACIÓN DE LINEAS*************************************************
				lineasMovAgrupada = agruparLineasMovimientoCuentasXPagar(array,parametrizacion);
				//****************************************************************************************************
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en generarLineaMovimientoHonorariosCuentasXPagar: "+e+" >> "+consultarDetalleFacturasPaciente03Str);
		}
		
		
		return lineasMovAgrupada;
		
	}
	
	/**
	 * Método implementado para agrupar las líneas del movimiento contable
	 * @param array
	 * @return
	 */
	private static ArrayList<DtoInterfazLineaS1E> agruparLineasMovimientoCuentasXPagar(ArrayList<DtoInterfazLineaS1E> array,DtoInterfazS1EInfo parametrizacion)
	{
		ArrayList<DtoInterfazLineaS1E> lineasMovAgrupada = new ArrayList<DtoInterfazLineaS1E>();
		for(int i=0;i<array.size();i++)
		{
			
			double totalD = 0;
			double totalC = 0;
			
			totalD += Utilidades.convertirADouble(array.get(i).getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).getValor(), true);
			totalC += Utilidades.convertirADouble(array.get(i).getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).getValor(), true);
			
			if(!array.get(i).isRepetido())
			{
				for(int j=(array.size()-1);j>i;j--)
				{
					if
					(
						//Igual cuenta contable...
						array.get(i).getArrayCampos().get(CampoMoviCuentasXPagar.AUX_CUENT_CONTABLE.getPosicion()).getValor().equals(array.get(j).getArrayCampos().get(CampoMoviDocumentoContable.AUX_CUENT_CONTABLE.getPosicion()).getValor())
						&&
						//Igual  tercero
						array.get(i).getArrayCampos().get(CampoMoviCuentasXPagar.TERCERO.getPosicion()).getValor().equals(array.get(j).getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).getValor())
						&&
						//Igual  auxiliar de numero documento cruce
						array.get(i).getArrayCampos().get(CampoMoviCuentasXPagar.NRO_DOC_CRUCE.getPosicion()).getValor().equals(array.get(j).getArrayCampos().get(CampoMoviDocumentoContable.AUX_CENTRO_COSTO.getPosicion()).getValor())
						&&
						//Igual fecha vencimiento
						array.get(i).getArrayCampos().get(CampoMoviCuentasXPagar.FECHA_VEN_DOC.getPosicion()).getValor().equals(array.get(j).getArrayCampos().get(CampoMoviDocumentoContable.OBSERVACIONES_MOVIMI.getPosicion()).getValor())
						
					)
					{						
						array.get(j).setRepetido(true);
						totalD += Utilidades.convertirADouble(array.get(j).getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).getValor(), true);
						totalC += Utilidades.convertirADouble(array.get(j).getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).getValor(), true);						
					}
				}
				
				if(totalD>0)
				{
					DtoInterfazLineaS1E nuevaLinea = new DtoInterfazLineaS1E(GeneracionInterfaz1E.indicadorLineaDetCxP);
					nuevaLinea = array.get(i);
					nuevaLinea.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(totalD,"0.0000"));
					nuevaLinea.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).setValor("0");
					nuevaLinea.getArrayCampos().get(CampoMoviCuentasXPagar.NUMERO_REGISTRO.getPosicion()).setValor(parametrizacion.getPosicion()+"");
					parametrizacion.setPosicion(parametrizacion.getPosicion()+1);
					lineasMovAgrupada.add(nuevaLinea);
				}
				if(totalC>0)
				{
					DtoInterfazLineaS1E nuevaLinea = new DtoInterfazLineaS1E(GeneracionInterfaz1E.indicadorLineaDetCxP);
					nuevaLinea = array.get(i);
					nuevaLinea.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setValor("0");
					nuevaLinea.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(totalC,"0.0000"));
					nuevaLinea.getArrayCampos().get(CampoMoviCuentasXPagar.NUMERO_REGISTRO.getPosicion()).setValor(parametrizacion.getPosicion()+"");
					parametrizacion.setPosicion(parametrizacion.getPosicion()+1);
					lineasMovAgrupada.add(nuevaLinea);
				}
			}
			
			array.get(i).setRepetido(true);
		}
		
		return lineasMovAgrupada;
	}
	
	/**
	 * Consultar Documentos Contable Recaudos
	 * @param Connection con
	 * @param DtoInterfazS1EInfo parametrizacion
	 * */
	private static ArrayList<DtoInterfazLineaS1E> consultarDocumentosContablesAjustes(Connection con,DtoInterfazS1EInfo parametrizacion) 
	{
		ArrayList<DtoInterfazLineaS1E> lineas = new ArrayList<DtoInterfazLineaS1E>();
		
		try
		{
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consultarDocumentosContablesAjustesStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setString(1,UtilidadFecha.conversionFormatoFechaABD(parametrizacion.getFechaProceso()));
			pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(parametrizacion.getFechaProceso()));
			
			//logger.info("cadena >> "+consultarDocumentosContablesAjustesStr.replace("?", UtilidadFecha.conversionFormatoFechaABD(parametrizacion.getFechaProceso())));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{
				DtoInterfazLineaS1E linea = new DtoInterfazLineaS1E(GeneracionInterfaz1E.indicadorLineaDocumentoContable);
				
				//***********Se asignan los posibles documentos que puede tener el movimiento****************************
				DtoInterfazDatosDocumentoS1E documento01 = new DtoInterfazDatosDocumentoS1E();
				documento01.setNumeroDocumento(rs.getString("numero_documento01"));
				documento01.setDescripcionDocumento(rs.getString("descripcion_documento01"));
				documento01.setTipoDocumento(rs.getString("tipo_documento01"));
				documento01.setCodigoTipoConsecutivo(rs.getString("tipo_consecutivo01"));
				documento01.setFecha(rs.getString("fecha01"));
				documento01.setValor(rs.getString("valor"));
				linea.getArrayDocumentos().add(documento01);
				
				if(!UtilidadTexto.isEmpty(rs.getString("numero_documento02")))
				{
					DtoInterfazDatosDocumentoS1E documento02 = new DtoInterfazDatosDocumentoS1E();
					documento02.setNumeroDocumento(rs.getString("numero_documento02"));
					documento02.setDescripcionDocumento(rs.getString("descripcion_documento02"));
					documento02.setTipoDocumento(rs.getString("tipo_documento02"));
					documento02.setCodigoTipoConsecutivo(rs.getString("tipo_consecutivo02"));
					documento02.setFecha(rs.getString("fecha02"));
					documento02.setValor(rs.getString("valor"));
					linea.getArrayDocumentos().add(documento02);
					
				}
				//Usado para almacenar el consecutivo del ajuste (PK)
				if(!UtilidadTexto.isEmpty(rs.getString("numero_documento03")))
				{
					
					/*
					 * Nota *  Arreglo por tarea  [id=133531]
	
					if(linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteRegistroGlosas+""))
					{
						DtoInterfazDatosDocumentoS1E documento03 = new DtoInterfazDatosDocumentoS1E();
						documento03.setNumeroDocumento(rs.getString("numero_documento03"));
						documento03.setDescripcionDocumento(rs.getString("descripcion_documento03"));
						documento03.setTipoDocumento(rs.getString("tipo_documento03"));
						documento03.setCodigoTipoConsecutivo(rs.getString("tipo_consecutivo03"));
						documento03.setFecha(rs.getString("fecha03"));
						documento03.setValor(rs.getString("valor"));
						linea.getArrayDocumentos().add(documento03);
					
					}
					else
					{*/
						DtoInterfazDatosDocumentoS1E documento03 = new DtoInterfazDatosDocumentoS1E();
						documento03.setNumeroDocumento(rs.getString("numero_documento03"));
						documento03.setValor(rs.getString("valor"));
						linea.getArrayDocumentos().add(documento03);
						
					/*}*/
				}
				
				
				//*******************************************************************************************************
				
				 
				/*******************CAMPOS DE LA LÍNEA DOCUMENTO CONTABLE***************************************************/
				
				//**************VALIDACIÓN CAMPO NUMERO REGISTRO*******************************
				linea.getArrayCampos().get(CampoDocumentoContable.NUMERO_REGISTRO.getPosicion()).setValor(parametrizacion.getPosicion()+"");
				//*******************************************************************************
				//***************VALIDACION CAMPO COMPAÑÍA****************************************
				asignarCodigoInterfazInstitucion(linea, parametrizacion, CampoDocumentoContable.COMPANIA.getPosicion());
				//**********************************************************************************
				//***************VALIDACION CAMPO CENTRO OPERACION****************************************
				
				linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).setValor(rs.getString("codigo_centro_atencion"));
				if(linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getValor().equals(""))
				{
					linea.setExisteInconsistencia(true);
					linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).setInconsistencia(true);
					linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
					linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
					linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
					linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).setDescripcionIncon(mensajeEInconNoInfo+" campo "+linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getDescripcion());
				}
				//**********************************************************************************
				//**************VALIDACION CAMPO TIPO DE DOCUMENTO************************************
				asignarIndTipoDocumento(linea, parametrizacion, CampoDocumentoContable.TIPO_DOCUMENTO.getPosicion(), linea.getArrayDocumentos().get(0).getTipoDocumento(),"");
				//*************************************************************************************
				//*************VALIDACION CAMPO NÚMERO DE DOCUMENTO**************************************
				asignarNumeroDocumento(linea, parametrizacion, CampoDocumentoContable.NRO_DOCUMENTO.getPosicion(), linea.getArrayDocumentos().get(0).getTipoDocumento(),"","");
				//****************************************************************************************
				//***************VALIDACION CAMPO FECHA DOCUMNENTO*****************************************
				linea.getArrayCampos().get(CampoDocumentoContable.FECHA_DOCUMENTO.getPosicion()).setValor(parametrizacion.getFechaProceso());
				//*******************************************************************************************
				///***************VALIDACION CAMPO TERCERO DOCUMENTO*****************************************
				if(linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteRegistroGlosas+""))
					linea.getArrayCampos().get(CampoDocumentoContable.TERCERO_DOCUMENTO.getPosicion()).setValor(rs.getString("id_tercero"));
				else
					asignarTerceroDocumento(con, linea, parametrizacion, CampoDocumentoContable.TERCERO_DOCUMENTO.getPosicion());
				//*******************************************************************************************
				//******************VALIDACION CAMPO OBSERVACIONES ENCABEZADO************************************
				asignarObservacionesDocumento(linea, parametrizacion, CampoDocumentoContable.OBSERVACIONES_DOCUMENTO.getPosicion(), linea.getArrayDocumentos().get(0).getTipoDocumento());
				//************************************************************************************************
				
				parametrizacion.setPosicion(parametrizacion.getPosicion()+1);
				
				/************CALCULO DE LA RETENCION*********************************************************************************/
				DtoRetencion retencion = UtilidadesInterfaz.calcularRetencionInterfaz(con, linea.getArrayDocumentos(), parametrizacion, linea.getArrayCampos().get(CampoDocumentoContable.TERCERO_DOCUMENTO.getPosicion()).getValor(), false,0); // autoretencion 
				
				for(String mensajeIncon:retencion.getInconsistenciasRetencion())
				{
					linea.setExisteInconsistencia(true);
					linea.getArrayCampos().get(CampoDocumentoContable.NUMERO_REGISTRO.getPosicion()).setInconsistencia(true);
					linea.getArrayCampos().get(CampoDocumentoContable.NUMERO_REGISTRO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
					linea.getArrayCampos().get(CampoDocumentoContable.NUMERO_REGISTRO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
					linea.getArrayCampos().get(CampoDocumentoContable.NUMERO_REGISTRO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
					linea.getArrayCampos().get(CampoDocumentoContable.NUMERO_REGISTRO.getPosicion()).setDescripcionIncon(mensajeIncon);
				}
				
				
				/*******************CAMPOS DE LA LÍNEA MOVIMIENTO DOCUMENTO CONTABLE***************************************************/
				if(linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteAjustesFactPaciente+""))
				{
					linea.getArrayDetalle().addAll(generarLineaMovimientoAjusteCAutoIng(con,linea,parametrizacion,retencion));
					
					linea.getArrayDetalle().addAll(generarLineaMovimientoAjusteCHonoReten(con, linea, parametrizacion));
					
					linea.getArrayDetalle().addAll(generarLineaMovimientoAjusteCIngCapitacion(con, linea, parametrizacion));
				}
				
				/*******************CAMPOS DE LA LÍNEA MOVIMIENTO CUENTAS X COBRAR*******************************************************/
				
				linea.getArrayDetalle().addAll(generarLineaMovimientoAjustesCuentasXCobrar(con,linea,parametrizacion));
				
				
				/*******************CAMPOS DE LA LÍNEA MOVIMIENTO CUENTAS X PAGAR*******************************************************/
				if(linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteAjustesFactPaciente+""))
				{
					linea.getArrayDetalle().addAll(generarLineaMovimientoAjustesCuentasXPagar(con,linea,parametrizacion));
				}
				  
				
				//***********VALIDACION DE LAS SUMAS IGUALES****************************************************
				//sumas iguales en los credito y debito
				if((Math.round(linea.getSumaDebito()*Math.pow(10,8))/Math.pow(10,8))-(Math.round(linea.getSumaCredito()*Math.pow(10,8))/Math.pow(10,8)) != 0)
				{	
					linea.setExisteInconsistencia(true);
					
					linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).setInconsistencia(true);
					linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
					linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
					linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.SUMAS_IGUALES);
					linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).setDescripcionIncon("Los totales son diferentes D: "+UtilidadTexto.formatearValores(linea.getSumaDebito())+" C: "+UtilidadTexto.formatearValores(linea.getSumaCredito()));
				}
				
				if(linea.isExisteInconsistencia())
				{
					
					huboInconsistencia = true;
					
				}
				
				if(!huboInconsistencia)
				{
					actualizarRegistroMarcado(con,documento01.getTipoDocumento(),documento01.getNumeroDocumento());
				}
				
				lineas.add(linea);
			}
			
			rs.close();
			pst.close();
			
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarDocumentosContablesAjustes: "+e+" >> "+consultarDocumentosContablesAjustesStr);
		}
		
		return lineas;
	}
	
	/**
	 * @param Connection con
	 * @param DtoInterfazLineaS1E linea
	 * @param DtoInterfazS1EInfo parametrizacion
	 * */
	private static ArrayList<DtoInterfazLineaS1E> generarLineaMovimientoAjustesCuentasXCobrar(
			Connection con,
			DtoInterfazLineaS1E linea,
			DtoInterfazS1EInfo parametrizacion)
	{
		ArrayList<DtoInterfazLineaS1E> array = new ArrayList<DtoInterfazLineaS1E>();
		
		try
		{
			String consulta = "";
			String numeroDocumento = "";
			
			if(linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteRegistroGlosas+""))
				consulta = consultarDetalleGlosasStr;
			else
				consulta = consultarDetalleAjustesFacturas01Str;
				
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			if(linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteRegistroGlosas+""))
			{
				pst.setInt(1,Utilidades.convertirAEntero(linea.getArrayDocumentos().get(2).getNumeroDocumento()));
				pst.setLong(2,Long.parseLong(linea.getArrayDocumentos().get(1).getNumeroDocumento()));
			}
			else
			{
				pst.setLong(1,Long.parseLong(linea.getArrayDocumentos().get(2).getNumeroDocumento()));
				pst.setLong(2,Long.parseLong(linea.getArrayDocumentos().get(1).getNumeroDocumento()));
			}
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next()) 
			{
				
				//Para ajustes solo se contabiliza cuenta x cobrar convenio
				int numeroCuentas = 1;
				
				if(linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteRegistroGlosas+""))
				{
					numeroDocumento = rs.getString("consecutivo");
					numeroCuentas  = 2;
					
				}else{
					if(linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteAjustesFactPaciente+"")){
						/*
						 * MT 6168
						 * jeilones
						 * piden que cuando el tipo de documento sea ajustes a las facturas de pacientes
						 * el nro de documento de cruce siempre sea el numero de la factura
						 * 
						 * */
						numeroDocumento=linea.getArrayDocumentos().get(1).getNumeroDocumento();
					}else
					{
						numeroDocumento = linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).getValor();
					}
				}
				
					
				
				for(int i=0;i<numeroCuentas;i++)
				{
					DtoInterfazLineaS1E lineaMov = new DtoInterfazLineaS1E(GeneracionInterfaz1E.indicadorLineaDetCxC);
					
					//**************VALIDACIÓN CAMPO NUMERO REGISTRO*******************************
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.NUMERO_REGISTRO.getPosicion()).setValor(parametrizacion.getPosicion()+"");
					//***************VALIDACION CAMPO COMPAÑÍA****************************************
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.COMPANIA.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.COMPANIA.getPosicion()).getValor());
					//***************VALIDACION CAMPO CENTRO OPERACION****************************************
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.CENTRO_OPERACION.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getValor());
					//**************VALIDACION CAMPO TIPO DE DOCUMENTO****************************************
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TIPO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.TIPO_DOCUMENTO.getPosicion()).getValor());
					//*************VALIDACION CAMPO NÚMERO DE DOCUMENTO***************************************
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.NRO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).getValor());
					//*************VALIDACION CAMPO AUXILIAR CUENTA CONTABLE**********************************
					DtoCuentaContable cuentaContable = new DtoCuentaContable();
					int codigoConvenio = rs.getInt("codigo_convenio");
					String codigoTipoRegimen = rs.getString("codigo_tipo_regimen");
					String codigoTipoConvenio = rs.getString("codigo_tipo_convenio");
					int codigoInstitucion = rs.getInt("codigo_institucion");
					int codTipoAjuste = rs.getInt("tipo_ajuste");
				
					//Cuenta x cobrar convenio o Cuenta por Cobrar Glosa
					if(i==0)
					{
						if(linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteRegistroGlosas+""))
						{
							cuentaContable = UtilidadesInterfaz.consultarCuentaContableInterfazConvenio(con, codigoConvenio, codigoTipoRegimen, ConstantesBD.tipoCuentaCxCGlosas, true, false);
							if(cuentaContable.getCuentaContable().equals(""))
							{
								cuentaContable = UtilidadesFacturacion.consultarCuentaContableTipoConvenio(con, codigoTipoConvenio, codigoInstitucion, false, true, false, false, false);
							}
						}
						else
						{
							if(codTipoAjuste == ConstantesBD.codigoAjusteDebitoCuentaCobro || codTipoAjuste == ConstantesBD.codigoAjusteCreditoCuentaCobro)
							{
								cuentaContable = UtilidadesInterfaz.consultarCuentaContableInterfazConvenio(con, codigoConvenio, codigoTipoRegimen, ConstantesBD.tipoCuentaCxCGlosas, true, false);								
								if(cuentaContable.getCuentaContable().equals(""))
								{
									cuentaContable = UtilidadesFacturacion.consultarCuentaContableTipoConvenio(con, codigoTipoConvenio, codigoInstitucion, false, true, false, false, false);
								}
							}
							else
							{
								cuentaContable = UtilidadesInterfaz.consultarCuentaContableInterfazConvenio(con, codigoConvenio, codigoTipoRegimen, ConstantesBD.tipoCuentaConvenio, true, false);								
								if(cuentaContable.getCuentaContable().equals(""))
								{
									cuentaContable = UtilidadesFacturacion.consultarCuentaContableTipoConvenio(con, codigoTipoConvenio, codigoInstitucion, true, false, false, false, false);
								}
							}
							cuentaContable.setMensaje("Cuenta por Cobrar Convenio");
						}
					} else if(i==1)
					{
						if(linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteRegistroGlosas+""))
						{
							cuentaContable = UtilidadesInterfaz.consultarCuentaContableInterfazConvenio(con, codigoConvenio, codigoTipoRegimen, ConstantesBD.tipoCuentaConvenio, true, false);
							if(cuentaContable.getCuentaContable().equals(""))
							{
								cuentaContable = UtilidadesFacturacion.consultarCuentaContableTipoConvenio(con, codigoTipoConvenio, codigoInstitucion, false, true, false, false, false);
							}
						}
					}		
					
					///Si no hubo cuenta contable se genera inconsistencia
					if(cuentaContable.getCuentaContable().equals(""))
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.AUX_CUENT_CONTABLE.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.AUX_CUENT_CONTABLE.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.AUX_CUENT_CONTABLE.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.AUX_CUENT_CONTABLE.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.AUX_CUENT_CONTABLE.getPosicion()).setDescripcionIncon("No se encontro informacion campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.AUX_CUENT_CONTABLE.getPosicion()).getDescripcion()+". ["+cuentaContable.getMensaje()+"] ");
					}
					else
					{
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.AUX_CUENT_CONTABLE.getPosicion()).setValor(cuentaContable.getCuentaContable());
					}
					
					
					//******************************************************************************************
					
					//*************VALIDACION CAMPO TERCERO**********************************
					if(cuentaContable.isManejaTerceros())
					{
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.TERCERO_DOCUMENTO.getPosicion()).getValor());
						if(lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).getValor().equals(""))
						{
							lineaMov.setExisteInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).setInconsistencia(true);
							lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
							lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
							lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
							lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviDocumentoContable.TERCERO.getPosicion()).getDescripcion());
						}
					}
					else
					{
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.TERCERO.getPosicion()).setValor("");
					}
					
					//*************VALIDACION CAMPO CENTRO OPERACION MOVIMIENTO**********************************
					//Según tipo de documento
					if(linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteRegistroGlosas+""))
					{
						asignarCodigoCentroAtencionContable(lineaMov, parametrizacion, CampoMoviCuentasXCobrar.CENTRO_OPER_MOVI.getPosicion(), "");
					}
					else
					{
						//Par ajustes se verifica si el convenio es capitado o no
						if(rs.getInt("codigo_tipo_contrato")==ConstantesBD.codigoTipoContratoCapitado)
						{
							lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.CENTRO_OPER_MOVI.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getValor());
						}
						else
						{
							asignarCodigoCentroAtencionContable(lineaMov, parametrizacion, CampoMoviCuentasXCobrar.CENTRO_OPER_MOVI.getPosicion(), "");
						}
					}
					
					//*************VALIDACION CAMPO UNIDAD DE NEGOCIO**********************************
					// GIO
					//asignarUnidadFuncionalEstandar(lineaMov,parametrizacion,CampoMoviCuentasXCobrar.UNIDAD_NEGOCIO.getPosicion(),linea,"");
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.UNIDAD_NEGOCIO.getPosicion()).setValor(parametrizacion.getUnidMovCxcCap());
					
					
					//*************************************************************************************
					//**************VALIDACION CAMPO VALOR DEBITO********************************************
					//Valor por cobrar convenio
					if(i==0)
					{
						if(linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteRegistroGlosas+""))
						{
							lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).setValor("0");
						}
						else
						{
							if(codTipoAjuste == ConstantesBD.codigoAjusteDebitoCuentaCobro || 
									codTipoAjuste == ConstantesBD.codigoAjusteDebitoFactura || 
										codTipoAjuste == ConstantesBD.codigoConceptosCarteraDebito)
							{
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getDouble("valor"),"0.0000"));
							}
							else
							{
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).setValor("0");
							}
						}
					}
					else if(i==1)
					{
						if(linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteRegistroGlosas+""))
						{
							lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getDouble("valor"),"0.0000"));
						}
					}
							
					if(lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).getValor().equals(""))
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).setDescripcionIncon("No se encontro informacion del campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).getDescripcion());
					}
					else
					{
						linea.setSumaDebito(linea.getSumaDebito()+Utilidades.convertirADouble(lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_DEBITO.getPosicion()).getValor()));
					}
					
					
					//*******************************************************************************************
					//******************VALIDACION CAMPO VALOR CREDITO**********************************************
					
					//Valor por cobrar convenio
					if(i==0)
					{
						if(linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteRegistroGlosas+""))
						{	
							lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).setValor("0");
						}
						else
						{
							if(codTipoAjuste == ConstantesBD.codigoAjusteDebitoCuentaCobro || 
									codTipoAjuste == ConstantesBD.codigoAjusteDebitoFactura || 
										codTipoAjuste == ConstantesBD.codigoConceptosCarteraDebito)
							{
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).setValor("0");
							}
							else
							{
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getDouble("valor"),"0.0000"));
							}
						}
					}
					else if(i==1)
					{
						if(linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteRegistroGlosas+""))
						{
							lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(rs.getDouble("valor"),"0.0000"));
						}
					}
							
					if(lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).getValor().equals(""))
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).setDescripcionIncon("No se encontro informacion del campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).getDescripcion());
					}
					else
					{
						linea.setSumaCredito(linea.getSumaCredito()+Utilidades.convertirADouble(lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.VALOR_CREDITO.getPosicion()).getValor()));
					}
					
					//************************************************************************************************
					//************************VALIDACION CAMPO OBSERVACIONES MOVIMIENTO********************************
					String observacion = rs.getString("observacion");
					if(!observacion.equals(""))
					{
						
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.OBSERVACIONES_MOVIMIENTO.getPosicion()).setValor(observacion+" "+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+" "+linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).getValor());					
					}
					//*************************************************************************************************
					//**************************VALIDACION CAMPO TIPO DOCUMENTO CRUCE************************************
					asignarTipoDocCruzeParametrizacion(lineaMov, parametrizacion, CampoMoviCuentasXCobrar.TIPO_DOC_CRUCE.getPosicion(), linea,true, linea.getArrayCampos().get(CampoDocumentoContable.TIPO_DOCUMENTO.getPosicion()).getValor());
					//*************************************************************************************************
					//***************************VALIDACION CAMPO NÚMERO DOCUMENTO CRUCE*********************************
					/*
					 * Anotacion: Si la línea tiene mas de 1 documento asociado, se pone el segundo, pero si solo tiene 1 se pone ese.
					 */
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.NRO_DOC_CRUCE.getPosicion()).setValor(numeroDocumento);
					//****************************************************************************************************
					//***************************VALIDACION CAMPO FECHA VENCIMIENTO DOCUMENTO******************************
					if(linea.getArrayDocumentos().get(0).getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteRegistroGlosas+""))
					{
						if(Utilidades.convertirAEntero(ValoresPorDefecto.getNumeroDiasResponderGlosas(Utilidades.convertirAEntero(parametrizacion.getInstitucion()))) > 0)
							lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.FECHA_VEN_DOC.getPosicion()).setValor(UtilidadFecha.incrementarDiasAFecha(rs.getString("fecha"),Utilidades.convertirAEntero(ValoresPorDefecto.getNumeroDiasResponderGlosas(Utilidades.convertirAEntero(parametrizacion.getInstitucion()))), false));
						else
								lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.FECHA_VEN_DOC.getPosicion()).setValor(rs.getString("fecha"));
					}
					else
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.FECHA_VEN_DOC.getPosicion()).setValor(UtilidadFecha.incrementarDiasAFecha(rs.getString("fecha"), rs.getInt("dias"), false));
						
					if(lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.FECHA_VEN_DOC.getPosicion()).getValor().equals(""))
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.FECHA_VEN_DOC.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.FECHA_VEN_DOC.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.FECHA_VEN_DOC.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.FECHA_VEN_DOC.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.FECHA_VEN_DOC.getPosicion()).setDescripcionIncon("No se encontro informacion del campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.FECHA_VEN_DOC.getPosicion()).getDescripcion());
					}
					//******************************************************************************************************
					//************************VALIDACION CAMPO FECHA PRONTO PAGO DOCUMENTO*************************************
					lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.FECHA_PAG_DOC.getPosicion()).setValor(parametrizacion.getFechaProceso());
					//***********************************************************************************************************
					//*************************VALIDACION CAMPO TERCERO VENDEDOR****************************************************
					asignarIdentificacionInstitucion(lineaMov, parametrizacion, CampoMoviCuentasXCobrar.TERCERO_VENDEDOR.getPosicion());
					//*****************************************************************************************************************
					//**************************VALIDACION CAMPO OBSERVACIONES MOVIMIENTO SALDO ABIERTO************************************
					if(!lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.OBSERVACIONES_MOVIMIENTO.getPosicion()).getValor().equals(""))
					{
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.OBS_SALDO_ABI.getPosicion()).setValor(lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.OBSERVACIONES_MOVIMIENTO.getPosicion()).getValor());
					}
					else
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.OBS_SALDO_ABI.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.OBS_SALDO_ABI.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.OBS_SALDO_ABI.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.OBS_SALDO_ABI.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.OBS_SALDO_ABI.getPosicion()).setDescripcionIncon("No se encontro informacion del beneficiario en el campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXCobrar.OBS_SALDO_ABI.getPosicion()).getDescripcion());
					}
					//***********************************************************************************************************************
					
					
					parametrizacion.setPosicion(parametrizacion.getPosicion()+1);
					array.add(lineaMov); 
				} //Fin For numeros cuentas
			} //Fin while resultSet
			
			if(!huboLineaCxC && array.size() > 0)
			{	
				huboLineaCxC = true;
				logger.info("..:Genero "+array.size()+" de linea "+DtoInterfazCampoS1E.getDescripcionLinea(GeneracionInterfaz1E.indicadorLineaDetCxC));
			}
			
			rs.close();
			pst.close();
			
		}
		catch(SQLException e)
		{
			logger.error("Error en generarLineaMovimientoAjustesCuentasXCobrar: "+e);
		}
		
		
		return array;
		
	}
	
	/**
	 * @param retencion 
	 * @param Connection con
	 * @param DtoInterfazLineaS1E linea
	 * @param DtoInterfazS1EInfo parametrizacion
	 * */
	private static ArrayList<DtoInterfazLineaS1E> generarLineaMovimientoAjustesCuentasXPagar(
			Connection con,
			DtoInterfazLineaS1E linea,
			DtoInterfazS1EInfo parametrizacion)
	{
		ArrayList<DtoInterfazLineaS1E> array = new ArrayList<DtoInterfazLineaS1E>();
		ArrayList<DtoInterfazLineaS1E> lineasMovAgrupada= new ArrayList<DtoInterfazLineaS1E>();
		
		DtoCuentaContable cuentaContable = new DtoCuentaContable();
		
		try
		{
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consultarDetalleAjustesFacturas02Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setLong(1,Long.parseLong(linea.getArrayDocumentos().get(2).getNumeroDocumento()));
			pst.setLong(2,Long.parseLong(linea.getArrayDocumentos().get(2).getNumeroDocumento()));
			
			//logger.info("\n\n >> "+consultaDetAjusteFact02Str+" >> "+linea.getArrayDocumentos().get(2).getNumeroDocumento());
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				DtoInterfazLineaS1E lineaMov = new DtoInterfazLineaS1E(GeneracionInterfaz1E.indicadorLineaDetCxP);
				
				//**************VALIDACIÓN CAMPO NUMERO REGISTRO*******************************
				//lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.NUMERO_REGISTRO.getPosicion()).setValor(parametrizacion.getPosicion()+"");
				//***************VALIDACION CAMPO COMPAÑÍA****************************************
				lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.COMPANIA.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.COMPANIA.getPosicion()).getValor());
				//***************VALIDACION CAMPO CENTRO OPERACION****************************************
				lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.CENTRO_OPERACION.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.CENTRO_OPERACION.getPosicion()).getValor());
				//**************VALIDACION CAMPO TIPO DE DOCUMENTO****************************************
				lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.TIPO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.TIPO_DOCUMENTO.getPosicion()).getValor());
				//*************VALIDACION CAMPO NÚMERO DE DOCUMENTO***************************************
				lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.NRO_DOCUMENTO.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).getValor());
				//*************VALIDACION CAMPO AUXILIAR CUENTA CONTABLE**********************************
				cuentaContable = UtilidadesFacturacion.consultarCuentaContableParticipacionPoolTarifasConvenio(con, rs.getInt("codigo_pool"), rs.getInt("codigo_convenio"), rs.getInt("codigo_esquema_tarifario"), Integer.parseInt(parametrizacion.getInstitucionBasica().getCodigo()) , true, false, false);
				if(cuentaContable.getCuentaContable().equals(""))
				{
					cuentaContable.setCodigo(rs.getString("cuenta_contable"));
					if(!cuentaContable.getCodigo().equals(""))
					{
						cuentaContable = UtilidadesInterfaz.consultarDatosCuentaContable(con, cuentaContable);
					}
				}
				lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.AUX_CUENT_CONTABLE.getPosicion()).setValor(cuentaContable.getCuentaContable());
				if(lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.AUX_CUENT_CONTABLE.getPosicion()).getValor().equals(""))
				{
					lineaMov.setExisteInconsistencia(true);
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.AUX_CUENT_CONTABLE.getPosicion()).setInconsistencia(true);
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.AUX_CUENT_CONTABLE.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.AUX_CUENT_CONTABLE.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.AUX_CUENT_CONTABLE.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.AUX_CUENT_CONTABLE.getPosicion()).setDescripcionIncon(mensajeEInconNoInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.AUX_CUENT_CONTABLE.getPosicion()).getDescripcion());
				}
				//******************************************************************************************
				
				//*************VALIDACION CAMPO TERCERO**********************************
				if(cuentaContable.isManejaTerceros())
				{
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.TERCERO.getPosicion()).setValor(rs.getString("id_tercero"));
					if(lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.TERCERO.getPosicion()).getValor().equals(""))
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.TERCERO.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.TERCERO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.TERCERO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.TERCERO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.TERCERO.getPosicion()).setDescripcionIncon(mensajeEInconNoInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.TERCERO.getPosicion()).getDescripcion());
					}
				}
				else
				{
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.TERCERO.getPosicion()).setValor("");
				}
				
				//*************VALIDACION CAMPO CENTRO OPERACION MOVIMIENTO**********************************
				asignarCodigoCentroAtencionContable(lineaMov, parametrizacion, CampoMoviCuentasXPagar.CENTRO_OPER_MOVI.getPosicion(), "");
				
				//*************VALIDACION CAMPO UNIDAD DE NEGOCIO**********************************
				asignarUnidadFuncionalEstandar(lineaMov,parametrizacion,CampoMoviCuentasXPagar.UNIDAD_NEGOCIO.getPosicion(),linea,"");
				//*************************************************************************************
				//**************VALIDACION CAMPO VALOR DEBITO********************************************
				
				if(rs.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteDebitoFactura+"") || 
						rs.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteDebitoCuentaCobro+"") || 
							rs.getString("tipo_ajuste").equals(ConstantesBD.codigoConceptosCarteraDebito+""))
				{
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setValor("0");
				}
				else
				{
					/************CALCULO DE LA RETENCION*********************************************************************************/
					DtoRetencion retencion = UtilidadesInterfaz.calcularRetencionInterfaz(con, linea.getArrayDocumentos(), parametrizacion, linea.getArrayCampos().get(CampoDocumentoContable.TERCERO_DOCUMENTO.getPosicion()).getValor(), true,rs.getDouble("valor")); // retencion 
					
					for(String mensajeIncon:retencion.getInconsistenciasRetencion())
					{
						linea.setExisteInconsistencia(true);
						linea.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setInconsistencia(true);
						linea.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						linea.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						linea.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						linea.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setDescripcionIncon(mensajeIncon);
					}
					double valorDebito = rs.getDouble("valor") - retencion.consultarTotalValorRetencion();
					if(valorDebito>0)
					{
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setValor(UtilidadTexto.formatearValores(valorDebito,"0.0000"));
						linea.setSumaDebito(linea.getSumaDebito()+valorDebito);
					}
					
					if(Utilidades.convertirADouble(lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).getValor())<=0)
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						if(valorDebito<0)
						{
							lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setDescripcionIncon(mensajeEInconRestaRetencion+ " campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).getDescripcion());
						}
						else
						{
							lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setDescripcionIncon(mensajeEInconNoInfo+ " campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).getDescripcion());
						}
						
					}
				}
				//
				
				
				//*******************************************************************************************
				//******************VALIDACION CAMPO VALOR CREDITO**********************************************
				if(rs.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteDebitoFactura+"") || 
						rs.getString("tipo_ajuste").equals(ConstantesBD.codigoAjusteDebitoCuentaCobro+"") || 
							rs.getString("tipo_ajuste").equals(ConstantesBD.codigoConceptosCarteraDebito+""))
				{
					/************CALCULO DE LA RETENCION*********************************************************************************/
					DtoRetencion retencion = UtilidadesInterfaz.calcularRetencionInterfaz(con, linea.getArrayDocumentos(), parametrizacion, linea.getArrayCampos().get(CampoDocumentoContable.TERCERO_DOCUMENTO.getPosicion()).getValor(), true,rs.getDouble("valor")); // retencion 
					
					for(String mensajeIncon:retencion.getInconsistenciasRetencion())
					{
						linea.setExisteInconsistencia(true);
						linea.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setInconsistencia(true);
						linea.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						linea.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						linea.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						linea.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_DEBITO.getPosicion()).setDescripcionIncon(mensajeIncon);
					}
					
					double valorCredito = rs.getDouble("valor") - retencion.consultarTotalValorRetencion();
					if(valorCredito>0)
					{
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).setValor(UtilidadTexto.formatearValores(valorCredito,"0.0000"));
						linea.setSumaCredito(linea.getSumaCredito()+valorCredito);
					}
					
					if(Utilidades.convertirADouble(lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).getValor())<=0)
					{
						lineaMov.setExisteInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).setInconsistencia(true);
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						if(valorCredito<0)
						{
							lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).setDescripcionIncon(mensajeEInconRestaRetencion+" campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).getDescripcion());
						}
						else
						{
							lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).setDescripcionIncon(mensajeEInconNoInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).getDescripcion());
						}
						
					}
				}
				else
				{
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.VALOR_CREDITO.getPosicion()).setValor("0");
				}
				
				
				
				//************************************************************************************************
				//************************VALIDACION CAMPO OBSERVACIONES MOVIMIENTO********************************
				lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.OBSERVACIONES_MOVIMIENTO.getPosicion()).setValor(rs.getString("observacion")+" "+linea.getArrayDocumentos().get(0).getDescripcionDocumento()+" "+linea.getArrayCampos().get(CampoDocumentoContable.NRO_DOCUMENTO.getPosicion()).getValor());
				
				//*************************************************************************************************
				//**************************VALIDACION CAMPO TIPO DOCUMENTO CRUCE************************************
				asignarTipoDocCruzeParametrizacion(lineaMov, parametrizacion, CampoMoviCuentasXPagar.TIPO_DOC_CRUCE.getPosicion(), linea,false, linea.getArrayCampos().get(CampoDocumentoContable.TIPO_DOCUMENTO.getPosicion()).getValor());
				//*************************************************************************************************
				//***************************VALIDACION CAMPO NÚMERO DOCUMENTO CRUCE*********************************
				
				lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.NRO_DOC_CRUCE.getPosicion()).setValor(rs.getString("consecutivo_solicitud"));
				
				if(lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.NRO_DOC_CRUCE.getPosicion()).getValor().equals(""))
				{
					lineaMov.setExisteInconsistencia(true);
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.NRO_DOC_CRUCE.getPosicion()).setInconsistencia(true);
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.NRO_DOC_CRUCE.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.NRO_DOC_CRUCE.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.NRO_DOC_CRUCE.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.NRO_DOC_CRUCE.getPosicion()).setDescripcionIncon(mensajeEInconNoInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.NRO_DOC_CRUCE.getPosicion()).getDescripcion());
				}
				
				
				//****************************************************************************************************
				//***************************VALIDACION CAMPO AUXILIAR CONCEPTO FLUJO EFECTIVO***************************
				asignarConceptoFlujoEfectivoCXP(lineaMov, parametrizacion, CampoMoviCuentasXPagar.AUX_CONCEP_FLUJO.getPosicion(), linea);
				//************************************************************************************************************
				//***************************VALIDACION CAMPO FECHA VENCIMIENTO DOCUMENTO******************************
				lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.FECHA_VEN_DOC.getPosicion()).setValor(UtilidadFecha.incrementarDiasAFecha(rs.getString("fecha"), rs.getInt("dias"), false));
				if(lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.FECHA_VEN_DOC.getPosicion()).getValor().equals(""))
				{
					lineaMov.setExisteInconsistencia(true);
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.FECHA_VEN_DOC.getPosicion()).setInconsistencia(true);
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.FECHA_VEN_DOC.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.FECHA_VEN_DOC.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.FECHA_VEN_DOC.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.FECHA_VEN_DOC.getPosicion()).setDescripcionIncon(mensajeEInconNoInfo+" campo "+lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.FECHA_VEN_DOC.getPosicion()).getDescripcion());
				}
				//******************************************************************************************************
				//************************VALIDACION CAMPO FECHA PRONTO PAGO DOCUMENTO*************************************
				lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.FECHA_PAG_DOC.getPosicion()).setValor(linea.getArrayCampos().get(CampoDocumentoContable.FECHA_DOCUMENTO.getPosicion()).getValor());
				//***********************************************************************************************************
				
				//*****************************************************************************************************************
				
				//**************************VALIDACION CAMPO OBSERVACIONES MOVIMIENTO SALDO ABIERTO************************************
				lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.OBS_SALDO_ABI.getPosicion()).setValor(lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.OBSERVACIONES_MOVIMIENTO.getPosicion()).getValor());
				if(lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.OBS_SALDO_ABI.getPosicion()).getValor().equals(""))
				{
					lineaMov.setExisteInconsistencia(true);
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.OBS_SALDO_ABI.getPosicion()).setInconsistencia(true);
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.OBS_SALDO_ABI.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.OBS_SALDO_ABI.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.OBS_SALDO_ABI.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
					lineaMov.getArrayCampos().get(CampoMoviCuentasXPagar.OBS_SALDO_ABI.getPosicion()).setDescripcionIncon(mensajeEInconNoInfo+" del beneficiario");
				}
				//***********************************************************************************************************************
				if(lineaMov.isExisteInconsistencia())					
					linea.setExisteInconsistencia(true);
								
				array.add(lineaMov);
				
			} //Fin while
		
			rs.close();
			pst.close();
						 
			//*****************SE REALIZA LA AGRUPACIÓN DE LINEAS*************************************************
			lineasMovAgrupada = agruparLineasMovimientoCuentasXPagar(array,parametrizacion);
			//****************************************************************************************************
			
		}
		catch(SQLException e)
		{
			logger.error("Error en generarLineaMovimientoAjustesCuentasXPagar: "+e);
		}
		
		
		return lineasMovAgrupada;
		
	}
	
	
	
	/**
	 * Consultar EVENTOS CARTERA
	 * @param Connection con
	 * @param DtoInterfazS1EInfo parametrizacion
	 * */
	private static ArrayList<DtoInterfazLineaS1E> consultarEventosCartera(Connection con,DtoInterfazS1EInfo parametrizacion) 
	{
		ArrayList<DtoInterfazLineaS1E> lineas = new ArrayList<DtoInterfazLineaS1E>();
		int codigoEventoCartera = ConstantesBD.codigoNuncaValido;
		
		try
		{
			
			
			/*
			//Son 11 tipos de eventos que se reportan
			for(int i=1;i<=11;i++)
			{
				PreparedStatementDecorator pst = null;
				codigoEventoCartera = i;
				
				switch(codigoEventoCartera)
				{
					case ConstantesBD.eventoFacturasPacientes:
						//logger.info(" cadena  "+consultarEventoFacturasPacientesStr);
						pst = new PreparedStatementDecorator(con.prepareStatement(consultarEventoFacturasPacientesStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					break;
					case ConstantesBD.eventoCuentasCobroCartera:
						//logger.info(" cadena  "+consultarEventoCuentasCobroGeneradasStr);
						pst = new PreparedStatementDecorator(con.prepareStatement(consultarEventoCuentasCobroGeneradasStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					break;
					case ConstantesBD.eventoRadicacionCuentasCobroCartera:
						//logger.info(" cadena  "+consultarEventoCuentrasCobroRadicadasStr);
						pst = new PreparedStatementDecorator(con.prepareStatement(consultarEventoCuentrasCobroRadicadasStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					break;
					case ConstantesBD.eventoInactivacionFacturasCuentasCobro:
						//logger.info(" cadena  "+consultarEventoFacturasInactivasCuentaCobroStr);
						pst = new PreparedStatementDecorator(con.prepareStatement(consultarEventoFacturasInactivasCuentaCobroStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					break;
					case ConstantesBD.eventoDevolucionCuentasCobro:
						//logger.info(" cadena  "+consultarEventoFacturasDEvueltasCuentaCobroStr);
						pst = new PreparedStatementDecorator(con.prepareStatement(consultarEventoFacturasDEvueltasCuentaCobroStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					break;
					case ConstantesBD.eventoRegistroGlosasDevolucion:
						//logger.info(" cadena  "+consultarEventoFacturasGlosasDevolucionStr);
						pst = new PreparedStatementDecorator(con.prepareStatement(consultarEventoFacturasGlosasDevolucionStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					break;
					case ConstantesBD.eventoAuditoriaFacturas:
						//logger.info(" cadena  "+consultarEventoFacturasAuditadasStr);
						pst = new PreparedStatementDecorator(con.prepareStatement(consultarEventoFacturasAuditadasStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					break;
					case ConstantesBD.eventoRegistroGlosas:
						//logger.info(" cadena  "+consultarEventoFacturasRegistradasEnGlosasStr);
						pst = new PreparedStatementDecorator(con.prepareStatement(consultarEventoFacturasRegistradasEnGlosasStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					break;
					case ConstantesBD.eventoRegistroRespuestaGlosas:
						//logger.info(" cadena  "+consultarEventoFacturasRegistradasEnGlosasRespuestaStr);
						pst = new PreparedStatementDecorator(con.prepareStatement(consultarEventoFacturasRegistradasEnGlosasRespuestaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					break;
					case ConstantesBD.eventorespuestaGlosasConciliadas:
						//logger.info(" cadena  "+consultarEventoFacturasRegistradasEnGlosasRespuestaConciliadaStr);
						pst = new PreparedStatementDecorator(con.prepareStatement(consultarEventoFacturasRegistradasEnGlosasRespuestaConciliadaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					break;
					case ConstantesBD.eventoRadicacionrespuestaGlosas:
						//logger.info(" cadena  "+consultarEventoRadicacionRespuestaGlosasStr);
						pst = new PreparedStatementDecorator(con.prepareStatement(consultarEventoRadicacionRespuestaGlosasStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					break;
				}
					
			*/	
		PreparedStatementDecorator pst = null;
		String cadena="select * from ( "+	
				consultarEventoFacturasPacientesStr+
				" UNION "+
				consultarEventoCuentasCobroGeneradasStr+
				" UNION "+
				consultarEventoCuentasCobroGeneradasFacInactivasStr+
				" UNION "+
				consultarEventoCuentasCobroGeneradasFacDevueltasStr+
				" UNION "+
				consultarEventoCuentrasCobroRadicadasStr+
				" UNION "+
				consultarEventoCuentrasCobroRadicadasFacInactivasStr+
				" UNION "+
				consultarEventoCuentrasCobroRadicadasFacDevueltasStr+
				" UNION "+
				consultarEventoFacturasInactivasCuentaCobroStr+
				" UNION "+
				consultarEventoFacturasDEvueltasCuentaCobroStr+
				" UNION "+
				consultarEventoFacturasGlosasDevolucionStr+
				" UNION "+
				consultarEventoFacturasAuditadasStr+
				" UNION "+
				consultarEventoFacturasRegistradasEnGlosasStr+
				" UNION "+
				consultarEventoFacturasRegistradasEnGlosasRespuestaStr+
				" UNION "+
				consultarEventoFacturasRegistradasEnGlosasRespuestaConciliadaStr+
				" UNION "+
				consultarEventoRadicacionRespuestaGlosasStr+
			" ) tabla order by numero_documento,fecha_seguimiento,hora,evento";
			cadena=cadena.replaceAll("FECHASEGUIMIENTO", "'"+parametrizacion.getFechaProceso()+"'");
			
			
			pst = new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
				String fechaProceso=UtilidadFecha.conversionFormatoFechaABD(parametrizacion.getFechaProceso());
				pst.setString(1,fechaProceso);
				pst.setString(2,fechaProceso);
				pst.setString(3,fechaProceso);
				pst.setString(4,fechaProceso);
				pst.setString(5,fechaProceso);
				pst.setString(6,fechaProceso);
				pst.setString(7,fechaProceso);
				pst.setString(8,fechaProceso);
				pst.setString(9,fechaProceso);
				pst.setString(10,fechaProceso);
				pst.setString(11,fechaProceso);
				pst.setString(12,fechaProceso);
				pst.setString(13,fechaProceso);
				pst.setString(14,fechaProceso);
				pst.setString(15,fechaProceso);
				
				logger.info("-->"+cadena);
				
				ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
				
				
				
				while(rs.next())
				{
					
					codigoEventoCartera=rs.getInt("evento");
					
					DtoInterfazLineaS1E linea = new DtoInterfazLineaS1E(GeneracionInterfaz1E.indicadorLineaEventoCartera);
					
					
					//***********Se asignan los posibles documentos que puede tener el movimiento****************************
					DtoInterfazDatosDocumentoS1E documento = new DtoInterfazDatosDocumentoS1E();
					documento.setNumeroDocumento(rs.getString("numero_documento"));
					documento.setDescripcionDocumento(rs.getString("nombre_tipo_documento"));
					documento.setTipoDocumento(rs.getString("tipo_documento"));
					documento.setCodigoTipoConsecutivo(ConstantesBD.tipoConsecutivoInteNumeroFactura+"");
					documento.setFecha(rs.getString("fecha"));
					linea.getArrayDocumentos().add(documento);
					//*******************************************************************************************************
					
					 
					/*******************CAMPOS DE LA LÍNEA EVENTO***************************************************/
					
					//**************VALIDACIÓN CAMPO NUMERO REGISTRO*******************************
					linea.getArrayCampos().get(CampoLineaEvento.NUMERO_REGISTRO.getPosicion()).setValor(parametrizacion.getPosicion()+"");
					//*******************************************************************************
					//***************VALIDACION CAMPO COMPAÑÍA****************************************
					asignarCodigoInterfazInstitucion(linea, parametrizacion, CampoLineaEvento.COMPANIA.getPosicion());
					//**********************************************************************************
					//***************VALIDACION CAMPO CODIGO SEGUIMIENTO EVENTO****************************************
					asignarCodigoEvento(linea, parametrizacion, CampoLineaEvento.CODIGO_SEGUIMIENTO_EVENTO.getPosicion(),codigoEventoCartera);
					//**********************************************************************************
					//***************VALIDACION FECHA DEL SEGUIMIENTO************************************fecha_seguimiento
					linea.getArrayCampos().get(CampoLineaEvento.FECHA_SEGUIMIENTO.getPosicion()).setValor(rs.getString("fecha_seguimiento"));
					//*************************************************************************************
					//*************VALIDACION HORA DEL SEGUIMIENTO****************************************
					String horaSeguimiento = rs.getString("hora");
					if(!UtilidadTexto.isEmpty(horaSeguimiento))
					{
						linea.getArrayCampos().get(CampoLineaEvento.HORA_SEGUIMIENTO.getPosicion()).setValor(UtilidadFecha.convertirHoraACincoCaracteres(horaSeguimiento)+":00");
					}
					else
					{
						linea.setExisteInconsistencia(true);
						linea.getArrayCampos().get(CampoLineaEvento.HORA_SEGUIMIENTO.getPosicion()).setInconsistencia(true);
						linea.getArrayCampos().get(CampoLineaEvento.HORA_SEGUIMIENTO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						linea.getArrayCampos().get(CampoLineaEvento.HORA_SEGUIMIENTO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						linea.getArrayCampos().get(CampoLineaEvento.HORA_SEGUIMIENTO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						linea.getArrayCampos().get(CampoLineaEvento.HORA_SEGUIMIENTO.getPosicion()).setDescripcionIncon(mensajeEInconNoInfo+" campo "+linea.getArrayCampos().get(CampoLineaEvento.HORA_SEGUIMIENTO.getPosicion()).getDescripcion());
					}
					//*************************************************************************************
					//*******************VALIDACION CAMPO NOTAS SEGUIMIENTO**************************************
					asignarNotaEvento(linea, parametrizacion, CampoLineaEvento.NOTAS_SEGUIMIENTO_EVENTO.getPosicion(), codigoEventoCartera, rs.getString("observaciones"));
					//*********************************************************************************************
					//********************VALIDACION CAMPO FECHA***************************************************
					if(codigoEventoCartera==ConstantesBD.eventoRadicacionCuentasCobroCartera)
					{
						linea.getArrayCampos().get(CampoLineaEvento.FECHA.getPosicion()).setValor(rs.getString("fecha_radicacion"));
					}
					//**********************************************************************************************
					//******************VALIDACION AUXILIAR DE CUENTA CONTABLE*******************************************
					DtoCuentaContable cuentaContable = new DtoCuentaContable();
					int codigoConvenio = rs.getInt("codigo_convenio");
					String codigoTipoRegimen = rs.getString("codigo_tipo_regimen");
					String codigoTipoConvenio = rs.getString("codigo_tipo_convenio");
					int codigoInstitucion = rs.getInt("codigo_institucion");
				
					cuentaContable = UtilidadesInterfaz.consultarCuentaContableInterfazConvenio(con, codigoConvenio, codigoTipoRegimen, ConstantesBD.tipoCuentaConvenio, true, false);								
					if(cuentaContable.getCuentaContable().equals(""))
					{
						cuentaContable = UtilidadesFacturacion.consultarCuentaContableTipoConvenio(con, codigoTipoConvenio, codigoInstitucion, true, false, false, false, false);
					}
					
					cuentaContable.setMensaje("Cuenta por Cobrar Convenio");
					
						
					
					///Si no hubo cuenta contable se genera inconsistencia
					if(cuentaContable.getCuentaContable().equals(""))
					{
						linea.setExisteInconsistencia(true);
						linea.getArrayCampos().get(CampoLineaEvento.AUXILIAR_CUENTA_CONTABLE.getPosicion()).setInconsistencia(true);
						linea.getArrayCampos().get(CampoLineaEvento.AUXILIAR_CUENTA_CONTABLE.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						linea.getArrayCampos().get(CampoLineaEvento.AUXILIAR_CUENTA_CONTABLE.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						linea.getArrayCampos().get(CampoLineaEvento.AUXILIAR_CUENTA_CONTABLE.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						linea.getArrayCampos().get(CampoLineaEvento.AUXILIAR_CUENTA_CONTABLE.getPosicion()).setDescripcionIncon("No se encontro informacion campo "+linea.getArrayCampos().get(CampoLineaEvento.AUXILIAR_CUENTA_CONTABLE.getPosicion()).getDescripcion()+". ["+cuentaContable.getMensaje()+"] ");
					}
					else
					{
						linea.getArrayCampos().get(CampoLineaEvento.AUXILIAR_CUENTA_CONTABLE.getPosicion()).setValor(cuentaContable.getCuentaContable());
					}
					
					//****************************************************************************************************
					//*********************VALIDACION CAMPO TERCERO**********************************************************
					if(cuentaContable.isManejaTerceros())
					{
						asignarTerceroDocumento(con, linea, parametrizacion, CampoLineaEvento.TERCERO.getPosicion());
						
						if(linea.getArrayCampos().get(CampoLineaEvento.TERCERO.getPosicion()).getValor().equals(""))
						{
							linea.setExisteInconsistencia(true);
							linea.getArrayCampos().get(CampoLineaEvento.TERCERO.getPosicion()).setInconsistencia(true);
							linea.getArrayCampos().get(CampoLineaEvento.TERCERO.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
							linea.getArrayCampos().get(CampoLineaEvento.TERCERO.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
							linea.getArrayCampos().get(CampoLineaEvento.TERCERO.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
							linea.getArrayCampos().get(CampoLineaEvento.TERCERO.getPosicion()).setDescripcionIncon(mensajeEInconFaltaDefinirInfo+" campo "+linea.getArrayCampos().get(CampoLineaEvento.TERCERO.getPosicion()).getDescripcion());
						}
					}
					else
					{
						linea.getArrayCampos().get(CampoLineaEvento.TERCERO.getPosicion()).setValor("");
					}
					
					//*******************************************************************************************************
					//********************VALIDACION CAMPO CENTRO DE OPERACION DEL MOVIMIENTO*********************************
					asignarCodigoCentroAtencionContable(linea, parametrizacion, CampoLineaEvento.CENTRO_OPERACION_MOVIMIENTO.getPosicion(), "");
					
					
					//***********************************************************************************************************
					//*************VALIDACION CAMPO UNIDAD DE NEGOCIO**********************************
					if(documento.getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteFacturaPaciente+"")||documento.getTipoDocumento().equals(ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente+""))
					{
						asignarUnidadFuncionalEstandar(linea,parametrizacion,CampoLineaEvento.UNIDAD_NEGOCIO.getPosicion(),linea,ConstantesIntegridadDominio.acronimoVentas);
					}
					else
					{
						asignarUnidadFuncionalEstandar(linea,parametrizacion,CampoLineaEvento.UNIDAD_NEGOCIO.getPosicion(),linea,"");
					}
					//*************************************************************************************
					///**************************VALIDACION CAMPO TIPO DOCUMENTO CRUCE************************************
					// DUDA
					asignarTipoDocCruzeParametrizacion(linea, parametrizacion, CampoLineaEvento.TIPO_DOC_CRUCE.getPosicion(), linea,true, "CV" );//eviar por defeto el tipo de movimiento ventas segun el anexo 832
					//*************************************************************************************************
					//***************************VALIDACION NUMERO DOCUMENTO CRUCE***************************************
					linea.getArrayCampos().get(CampoLineaEvento.NUMERO_DOCUMENTO_CRUCE.getPosicion()).setValor(rs.getString("numero_documento"));
					if(linea.getArrayCampos().get(CampoLineaEvento.NUMERO_DOCUMENTO_CRUCE.getPosicion()).getValor().equals(""))
					{
						linea.setExisteInconsistencia(true);
						linea.getArrayCampos().get(CampoLineaEvento.NUMERO_DOCUMENTO_CRUCE.getPosicion()).setInconsistencia(true);
						linea.getArrayCampos().get(CampoLineaEvento.NUMERO_DOCUMENTO_CRUCE.getPosicion()).setNumeroDocumento(linea.getArrayDocumentos().get(0).getNumeroDocumento());
						linea.getArrayCampos().get(CampoLineaEvento.NUMERO_DOCUMENTO_CRUCE.getPosicion()).setTipoDocumento(linea.getArrayDocumentos().get(0).getDescripcionDocumento());
						linea.getArrayCampos().get(CampoLineaEvento.NUMERO_DOCUMENTO_CRUCE.getPosicion()).setTipoInconsistencia(TipoInconsistencia.DATOS);
						linea.getArrayCampos().get(CampoLineaEvento.NUMERO_DOCUMENTO_CRUCE.getPosicion()).setDescripcionIncon("No se encontro informacion campo "+linea.getArrayCampos().get(CampoLineaEvento.NUMERO_DOCUMENTO_CRUCE.getPosicion()).getDescripcion()+".");
					}
					//Si supera el tamaño se trunca
					else if(linea.getArrayCampos().get(CampoLineaEvento.NUMERO_DOCUMENTO_CRUCE.getPosicion()).getValor().length()>8)
					{
						linea.getArrayCampos().get(CampoLineaEvento.NUMERO_DOCUMENTO_CRUCE.getPosicion()).setValor(linea.getArrayCampos().get(CampoLineaEvento.NUMERO_DOCUMENTO_CRUCE.getPosicion()).getValor().substring(0, 8));
					}
					//*****************************************************************************************************
					
					
					parametrizacion.setPosicion(parametrizacion.getPosicion()+1);
					
					
					
					if(linea.isExisteInconsistencia())
						huboInconsistencia = true;
					
					
					
					lineas.add(linea);
					
				} //Fin while rs.next
				
				pst.close();
				rs.close();
				
			//} //Fin FOR tipos de eventos a reportar
			
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarEventosCartera: ",e);
		}
		
		return lineas;
	}
	
	
	/**
	 * Método usado paera asignar el código evento
	 * @param linea
	 * @param parametrizacion
	 * @param posicion
	 * @param codigoTipoDocumento
	 */
	private static void asignarCodigoEvento(
			DtoInterfazLineaS1E lineaDet,
			DtoInterfazS1EInfo parametrizacion,
			int posicion,
			int codigoTipoEvento)
	{
		///Se busca el indicativo de Unidad Funcional Estandar que aplica para el tipo de documento
		String codigoEvento = "";
		for(DtoEventosParam1E evento:parametrizacion.getEventos())
		{
			if(evento.getEvento().equals(codigoTipoEvento+""))
			{
				codigoEvento = evento.getCodigoEvento();
			}
		}
		
		lineaDet.getArrayCampos().get(posicion).setValor(codigoEvento);
		//Se revisó documento y no es necesario generar inconsistencia es un campo no requerido
		if(lineaDet.getArrayCampos().get(posicion).getValor().equals(""))
		{
			
			switch(codigoTipoEvento)
			{
				case ConstantesBD.eventoFacturasPacientes:
					if(!parametrizacion.isHuboInconsistenciaEventoFacturasPaciente())
					{
						parametrizacion.setHuboInconsistenciaEventoFacturasPaciente(true);
						lineaDet.setExisteInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setNumeroDocumento(lineaDet.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoDocumento(lineaDet.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoInconsistencia(TipoInconsistencia.DATOS);
						
						lineaDet.getArrayCampos().get(posicion).setDescripcionIncon("Falta parametrizar codigo evento para el evento Facturas Paciente en parametrizacion interfaz.");
						
					}
				break;
				case ConstantesBD.eventoAuditoriaFacturas:
					if(!parametrizacion.isHuboInconsistenciaEventoAuditoriaFacturas())
					{
						parametrizacion.setHuboInconsistenciaEventoAuditoriaFacturas(true);
						lineaDet.setExisteInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setNumeroDocumento(lineaDet.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoDocumento(lineaDet.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoInconsistencia(TipoInconsistencia.DATOS);
						
						lineaDet.getArrayCampos().get(posicion).setDescripcionIncon("Falta parametrizar codigo evento para el evento Auditoria Facturas en parametrizacion interfaz.");
						
					}
				break;
				case ConstantesBD.eventoCuentasCobroCartera:
					if(!parametrizacion.isHuboInconsistenciaEventoCuentasCobroCartera())
					{
						parametrizacion.setHuboInconsistenciaEventoCuentasCobroCartera(true);
						lineaDet.setExisteInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setNumeroDocumento(lineaDet.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoDocumento(lineaDet.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoInconsistencia(TipoInconsistencia.DATOS);
						
						lineaDet.getArrayCampos().get(posicion).setDescripcionIncon("Falta parametrizar codigo evento para el evento Cuentas Cobro Cartera en parametrizacion interfaz.");
						
					}
				break;
				case ConstantesBD.eventoRadicacionCuentasCobroCartera:
					if(!parametrizacion.isHuboInconsistenciaEventoRadicacionCuentasCobro())
					{
						parametrizacion.setHuboInconsistenciaEventoRadicacionCuentasCobro(true);
						lineaDet.setExisteInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setNumeroDocumento(lineaDet.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoDocumento(lineaDet.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoInconsistencia(TipoInconsistencia.DATOS);
						
						lineaDet.getArrayCampos().get(posicion).setDescripcionIncon("Falta parametrizar codigo evento para el evento Radicacion Cuentas Cobro Cartera en parametrizacion interfaz.");
						
					}
				break;
				case ConstantesBD.eventoInactivacionFacturasCuentasCobro:
					if(!parametrizacion.isHuboInconsistenciaEventoInactivacionFacturasCuentaCobro())
					{
						parametrizacion.setHuboInconsistenciaEventoInactivacionFacturasCuentaCobro(true);
						lineaDet.setExisteInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setNumeroDocumento(lineaDet.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoDocumento(lineaDet.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoInconsistencia(TipoInconsistencia.DATOS);
						
						lineaDet.getArrayCampos().get(posicion).setDescripcionIncon("Falta parametrizar codigo evento para el evento Inactivacion Facturas Cuentas Cobro en parametrizacion interfaz.");
						
					}
				break;
				case ConstantesBD.eventoDevolucionCuentasCobro:
					if(!parametrizacion.isHuboInconsistenciaEventoDevolucionCuentasCobro())
					{
						parametrizacion.setHuboInconsistenciaEventoDevolucionCuentasCobro(true);
						lineaDet.setExisteInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setNumeroDocumento(lineaDet.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoDocumento(lineaDet.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoInconsistencia(TipoInconsistencia.DATOS);
						
						lineaDet.getArrayCampos().get(posicion).setDescripcionIncon("Falta parametrizar codigo evento para el evento Devolución Cuentas Cobro en parametrizacion interfaz.");
						
					}
				break;
				case ConstantesBD.eventoRegistroGlosas:
					if(!parametrizacion.isHuboInconsistenciaEventoRegistroGlosas())
					{
						parametrizacion.setHuboInconsistenciaEventoRegistroGlosas(true);
						lineaDet.setExisteInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setNumeroDocumento(lineaDet.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoDocumento(lineaDet.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoInconsistencia(TipoInconsistencia.DATOS);
						
						lineaDet.getArrayCampos().get(posicion).setDescripcionIncon("Falta parametrizar codigo evento para el evento Registro Glosas en parametrizacion interfaz.");
						
					}
				break;
				case ConstantesBD.eventoRegistroGlosasDevolucion:
					if(!parametrizacion.isHuboInconsistenciaEventoRegistroGlosasDevolucion())
					{
						parametrizacion.setHuboInconsistenciaEventoRegistroGlosasDevolucion(true);
						lineaDet.setExisteInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setNumeroDocumento(lineaDet.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoDocumento(lineaDet.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoInconsistencia(TipoInconsistencia.DATOS);
						
						lineaDet.getArrayCampos().get(posicion).setDescripcionIncon("Falta parametrizar codigo evento para el evento Registro Glosas devolucion en parametrizacion interfaz.");
						
					}
				break;
				case ConstantesBD.eventoRegistroRespuestaGlosas:
					if(!parametrizacion.isHuboInconsistenciaEventoRegistroRespuestaGlosas())
					{
						parametrizacion.setHuboInconsistenciaEventoRegistroRespuestaGlosas(true);
						lineaDet.setExisteInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setNumeroDocumento(lineaDet.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoDocumento(lineaDet.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoInconsistencia(TipoInconsistencia.DATOS);
						
						lineaDet.getArrayCampos().get(posicion).setDescripcionIncon("Falta parametrizar codigo evento para el evento Registro respuesta glosas en parametrizacion interfaz.");
						
					}
				break;
				case ConstantesBD.eventorespuestaGlosasConciliadas:
					if(!parametrizacion.isHuboInconsistenciaEventoRegistroRespuestaGlosasConciliadas())
					{
						parametrizacion.setHuboInconsistenciaEventoRegistroRespuestaGlosasConciliadas(true);
						lineaDet.setExisteInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setNumeroDocumento(lineaDet.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoDocumento(lineaDet.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoInconsistencia(TipoInconsistencia.DATOS);
						
						lineaDet.getArrayCampos().get(posicion).setDescripcionIncon("Falta parametrizar codigo evento para el evento Registro respuesta glosas conciliadas en parametrizacion interfaz.");
						
					}
				break;
				case ConstantesBD.eventoRadicacionrespuestaGlosas:
					if(!parametrizacion.isHuboInconsistenciaEventoRadicacionRespuestaGlosas())
					{
						parametrizacion.setHuboInconsistenciaEventoRadicacionRespuestaGlosas(true);
						lineaDet.setExisteInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setNumeroDocumento(lineaDet.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoDocumento(lineaDet.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoInconsistencia(TipoInconsistencia.DATOS);
						
						lineaDet.getArrayCampos().get(posicion).setDescripcionIncon("Falta parametrizar codigo evento para el evento radicacion respuesta glosas en parametrizacion interfaz.");
						
					}
				break;
				
			}
			
		}
		if(lineaDet.getArrayCampos().get(posicion).getValor().length()>20)
		{
			//Si el campo supera el tamaño se trunca
			lineaDet.getArrayCampos().get(posicion).setValor(lineaDet.getArrayCampos().get(posicion).getValor().substring(0, 20));
		}
	}
	
	
	/**
	 * Método usado paera asignar la nota del evento
	 * @param linea
	 * @param parametrizacion
	 * @param posicion
	 * @param codigoTipoDocumento
	 */
	private static void asignarNotaEvento(
			DtoInterfazLineaS1E lineaDet,
			DtoInterfazS1EInfo parametrizacion,
			int posicion,
			int codigoTipoEvento,
			String observacion)
	{
		///Se busca el indicativo de Unidad Funcional Estandar que aplica para el tipo de documento
		String notaEvento = "";
		for(DtoEventosParam1E evento:parametrizacion.getEventos())
		{
			if(evento.getEvento().equals(codigoTipoEvento+""))
			{
				notaEvento = evento.getNotasEvento();
			}
		}
		
		lineaDet.getArrayCampos().get(posicion).setValor(notaEvento);
		//Se revisó documento y no es necesario generar inconsistencia es un campo no requerido
		if(lineaDet.getArrayCampos().get(posicion).getValor().equals(""))
		{
			
			switch(codigoTipoEvento)
			{
				case ConstantesBD.eventoFacturasPacientes:
					if(!parametrizacion.isHuboInconsistenciaNEventoFacturasPaciente())
					{
						parametrizacion.setHuboInconsistenciaNEventoFacturasPaciente(true);
						lineaDet.setExisteInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setNumeroDocumento(lineaDet.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoDocumento(lineaDet.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoInconsistencia(TipoInconsistencia.DATOS);
						
						lineaDet.getArrayCampos().get(posicion).setDescripcionIncon("Falta parametrizar notas evento para el evento Facturas Paciente en parametrizacion interfaz.");
						
					}
				break;
				case ConstantesBD.eventoAuditoriaFacturas:
					if(!parametrizacion.isHuboInconsistenciaNEventoAuditoriaFacturas())
					{
						parametrizacion.setHuboInconsistenciaNEventoAuditoriaFacturas(true);
						lineaDet.setExisteInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setNumeroDocumento(lineaDet.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoDocumento(lineaDet.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoInconsistencia(TipoInconsistencia.DATOS);
						
						lineaDet.getArrayCampos().get(posicion).setDescripcionIncon("Falta parametrizar notas evento para el evento Auditoria Facturas en parametrizacion interfaz.");
						
					}
				break;
				case ConstantesBD.eventoCuentasCobroCartera:
					if(!parametrizacion.isHuboInconsistenciaNEventoCuentasCobroCartera())
					{
						parametrizacion.setHuboInconsistenciaNEventoCuentasCobroCartera(true);
						lineaDet.setExisteInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setNumeroDocumento(lineaDet.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoDocumento(lineaDet.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoInconsistencia(TipoInconsistencia.DATOS);
						
						lineaDet.getArrayCampos().get(posicion).setDescripcionIncon("Falta parametrizar notas evento para el evento Cuentas Cobro Cartera en parametrizacion interfaz.");
						
					}
				break;
				case ConstantesBD.eventoRadicacionCuentasCobroCartera:
					if(!parametrizacion.isHuboInconsistenciaNEventoRadicacionCuentasCobro())
					{
						parametrizacion.setHuboInconsistenciaNEventoRadicacionCuentasCobro(true);
						lineaDet.setExisteInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setNumeroDocumento(lineaDet.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoDocumento(lineaDet.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoInconsistencia(TipoInconsistencia.DATOS);
						
						lineaDet.getArrayCampos().get(posicion).setDescripcionIncon("Falta parametrizar notas evento para el evento Radicacion Cuentas Cobro Cartera en parametrizacion interfaz.");
						
					}
				break;
				case ConstantesBD.eventoInactivacionFacturasCuentasCobro:
					if(!parametrizacion.isHuboInconsistenciaNEventoInactivacionFacturasCuentaCobro())
					{
						parametrizacion.setHuboInconsistenciaNEventoInactivacionFacturasCuentaCobro(true);
						lineaDet.setExisteInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setNumeroDocumento(lineaDet.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoDocumento(lineaDet.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoInconsistencia(TipoInconsistencia.DATOS);
						
						lineaDet.getArrayCampos().get(posicion).setDescripcionIncon("Falta parametrizar notas evento para el evento Inactivacion Facturas Cuentas Cobro en parametrizacion interfaz.");
						
					}
				break;
				case ConstantesBD.eventoDevolucionCuentasCobro:
					if(!parametrizacion.isHuboInconsistenciaNEventoDevolucionCuentasCobro())
					{
						parametrizacion.setHuboInconsistenciaNEventoDevolucionCuentasCobro(true);
						lineaDet.setExisteInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setNumeroDocumento(lineaDet.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoDocumento(lineaDet.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoInconsistencia(TipoInconsistencia.DATOS);
						
						lineaDet.getArrayCampos().get(posicion).setDescripcionIncon("Falta parametrizar notas evento para el evento Devolución Cuentas Cobro en parametrizacion interfaz.");
						
					}
				break;
				case ConstantesBD.eventoRegistroGlosas:
					if(!parametrizacion.isHuboInconsistenciaNEventoRegistroGlosas())
					{
						parametrizacion.setHuboInconsistenciaNEventoRegistroGlosas(true);
						lineaDet.setExisteInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setNumeroDocumento(lineaDet.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoDocumento(lineaDet.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoInconsistencia(TipoInconsistencia.DATOS);
						
						lineaDet.getArrayCampos().get(posicion).setDescripcionIncon("Falta parametrizar notas evento para el evento Registro Glosas en parametrizacion interfaz.");
						
					}
				break;
				case ConstantesBD.eventoRegistroGlosasDevolucion:
					if(!parametrizacion.isHuboInconsistenciaNEventoRegistroGlosasDevolucion())
					{
						parametrizacion.setHuboInconsistenciaNEventoRegistroGlosasDevolucion(true);
						lineaDet.setExisteInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setNumeroDocumento(lineaDet.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoDocumento(lineaDet.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoInconsistencia(TipoInconsistencia.DATOS);
						
						lineaDet.getArrayCampos().get(posicion).setDescripcionIncon("Falta parametrizar notas evento para el evento Registro Glosas devolucion en parametrizacion interfaz.");
						
					}
				break;
				case ConstantesBD.eventoRegistroRespuestaGlosas:
					if(!parametrizacion.isHuboInconsistenciaNEventoRegistroRespuestaGlosas())
					{
						parametrizacion.setHuboInconsistenciaNEventoRegistroRespuestaGlosas(true);
						lineaDet.setExisteInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setNumeroDocumento(lineaDet.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoDocumento(lineaDet.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoInconsistencia(TipoInconsistencia.DATOS);
						
						lineaDet.getArrayCampos().get(posicion).setDescripcionIncon("Falta parametrizar notas evento para el evento Registro respuesta glosas en parametrizacion interfaz.");
						
					}
				break;
				case ConstantesBD.eventorespuestaGlosasConciliadas:
					if(!parametrizacion.isHuboInconsistenciaNEventoRegistroRespuestaGlosasConciliadas())
					{
						parametrizacion.setHuboInconsistenciaNEventoRegistroRespuestaGlosasConciliadas(true);
						lineaDet.setExisteInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setNumeroDocumento(lineaDet.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoDocumento(lineaDet.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoInconsistencia(TipoInconsistencia.DATOS);
						
						lineaDet.getArrayCampos().get(posicion).setDescripcionIncon("Falta parametrizar notas evento para el evento Registro respuesta glosas conciliadas en parametrizacion interfaz.");
						
					}
				break;
				case ConstantesBD.eventoRadicacionrespuestaGlosas:
					if(!parametrizacion.isHuboInconsistenciaNEventoRadicacionRespuestaGlosas())
					{
						parametrizacion.setHuboInconsistenciaNEventoRadicacionRespuestaGlosas(true);
						lineaDet.setExisteInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setInconsistencia(true);
						lineaDet.getArrayCampos().get(posicion).setNumeroDocumento(lineaDet.getArrayDocumentos().get(0).getNumeroDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoDocumento(lineaDet.getArrayDocumentos().get(0).getDescripcionDocumento());
						lineaDet.getArrayCampos().get(posicion).setTipoInconsistencia(TipoInconsistencia.DATOS);
						
						lineaDet.getArrayCampos().get(posicion).setDescripcionIncon("Falta parametrizar notas evento para el evento radicacion respuesta glosas en parametrizacion interfaz.");
						
					}
				break;
				
			}
			
		}
		else
		{
			lineaDet.getArrayCampos().get(posicion).setValor(lineaDet.getArrayCampos().get(posicion).getValor()+(observacion.length()==0?"":" "+observacion));
		}
		if(lineaDet.getArrayCampos().get(posicion).getValor().length()>2000)
		{
			//Si el campo supera el tamaño se trunca
			lineaDet.getArrayCampos().get(posicion).setValor(lineaDet.getArrayCampos().get(posicion).getValor().substring(0, 2000));
		}
	}
	
}