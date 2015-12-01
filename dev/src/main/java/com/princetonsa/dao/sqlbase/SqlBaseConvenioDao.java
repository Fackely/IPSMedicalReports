






/*
 * @(#)SqlBaseConvenioDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosStr;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadLog;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.dto.facturacion.DtoMediosAutorizacion;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Contrato;
import com.princetonsa.mundo.cargos.Convenio;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;

/**
 * Implementaci�n sql gen�rico de todas las funciones de acceso a la fuente de datos
 * para un  convenio
 *
 * @version 1.0, Abril 30 / 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson R�os</a>
 */

public class SqlBaseConvenioDao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseConvenioDao.class);
	
	/**
	 * Carga los datos para mostrarlos en el resumen
	 */
	@SuppressWarnings("unused")
	private static String consultaInsertarDocumentoAdjunto="INSERT INTO facturacion.archivos_convenio (codigo,convenio, nombre_archivo, nombre_original) VALUES ( ? , ? , ? , ?)";
	
	private static String insertarMedioAutorizacion = " INSERT INTO facturacion.medios_aut_conv_odo( " +
	"codigo , " +//1
	"convenio , " +//2
	"fecha_modifica , " +//3
	"hora_modifica , " +//4
	"tipo  , " +//5
	"usuario_modifica ) " +//6
	"values ( " +
	"? , " +//1
	"? , " +//2
	"? , " +//3
	"? , " +//4
	"? , " +//5
	"?  ) ";//6


	private static String cargarDatosConvenio ="SELECT c.codigo AS codigo, " +
																					"c.empresa AS empresa, " +
																					"c.tipo_regimen AS tipoRegimen," +
																					"tr.nombre AS nombretiporegimen, " +
																					"c.nombre AS nombre," +
																					"c.observaciones AS observaciones, " +
																					"c.plan_beneficios AS planBeneficios, " +
																					"coalesce(c.codigo_min_salud,'') AS codigoMinSalud, " +
																					"c.formato_factura AS formatoFactura, " +
																					"c.activo AS activa," +
																					"getnumeroidentificaciontercero(e.tercero) AS numeroIdentificacion, "+
																					"c.tipo_contrato AS tipoContrato, " +
																					"t.nombre AS nombreTipoContrato, " +
																					"c.info_adic_ingreso_convenios AS infoAdicIngresoConvenio, " +
																					"c.pyp AS pyp, " +
																					"c.unificar_pyp AS unificar_pyp," +
																					"coalesce(c.num_dias_vencimiento||'','') as numdiasvencimiento," +
																					"c.requiere_justificacion_serv as requierejustificacionservicios ," +
																					"c.requiere_justificacion_art as requierejustificacionarticulos ," +
																					"c.req_just_art_nopos_dif_med as requierejustartnoposdifmed, " +
																					"c.maneja_complejidad as manejacomplejidad ," +
																					"coalesce(c.semanas_min_cotizacion||'','') as semanasmincotizacion ," +
																					"c.requiere_ingreso_num_carnet as requierenumcarnet ," +
																					"coalesce(c.tipo_convenio||'','-1')  as tipoconvenio," +
																					"c.tipo_codigo as tipocodigo," +
																					"CASE WHEN c.tipo_codigo_articulos IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE c.tipo_codigo_articulos END as tipocodigoart, " + 
																					"coalesce(c.ajuste_servicios||'','') as ajusteservicios," +
																					"coalesce(c.ajuste_articulos||'','') as ajustearticulos," +
																					"c.interfaz as interfaz," +
																					"coalesce(tc.clasificacion,0) as clasificaciontipoconvenio," +
																					"tr.requiere_deudor AS requiereDeudor," +
																					"tr.req_verific_derechos AS requiereVerificacionDerechos," +
																					"CASE WHEN c.cantidad_max_cirugia IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE c.cantidad_max_cirugia END AS cantidadmaxcirugia," +
																					"CASE WHEN c.cantidad_max_ayudpag IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE c.cantidad_max_ayudpag END AS cantidadmaxayudpag," +
																					"CASE WHEN c.tipo_liquidacion_scx IS NULL THEN '' ELSE c.tipo_liquidacion_scx END  AS tipoliquidacionscx," +
																					"CASE WHEN c.tipo_liquidacion_dyt IS NULL THEN '' ELSE c.tipo_liquidacion_dyt END  AS tipoliquidaciondyt," +
																					"CASE WHEN c.tipo_liquidacion_gcx IS NULL THEN '' ELSE c.tipo_liquidacion_gcx END AS tipoliquidaciongcx," +
																					"CASE WHEN c.tipo_liquidacion_gdyt IS NULL THEN '' ELSE c.tipo_liquidacion_gdyt END AS tipoliquidaciongdyt," +
																					"CASE WHEN c.tipo_tarifa_liqmatecx IS NULL THEN '' ELSE c.tipo_tarifa_liqmatecx END AS tipotarifaliqmatecx," +
																					"CASE WHEN c.tipo_tarifa_liqmatedyt IS NULL THEN '' ELSE c.tipo_tarifa_liqmatedyt END AS tipotarifaliqmatedyt," +
																					"CASE WHEN c.tipo_fecha_liqtiempcx IS NULL THEN '' ELSE c.tipo_fecha_liqtiempcx END AS tipofechaliqtiempcx," +
																					"CASE WHEN c.tipo_fecha_liqtiempdyt IS NULL THEN '' ELSE c.tipo_fecha_liqtiempdyt END AS tipofechaliqtiempdyt," +
																					"CASE WHEN c.liquidacion_tmp_frac_add IS NULL THEN '' ELSE c.liquidacion_tmp_frac_add END AS liquidaciontmpfracadd, " +
																					"coalesce(c.encabezado_factura, '') as encabezadofactura, " +
																					"coalesce(c.pie_factura,'') as piefactura, " +
																					"CASE WHEN c.empresa_institucion IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE c.empresa_institucion END AS empresainstitucion, " +
																					"c.centro_costo_plan_especial as planEspecial, " +
																					"c.institucion as institucion, " +
																					"c.excento_deudor as excentoDeudor ," +
																					"c.excento_doc_garantia as excentoDocumentoGarantia, " +
																					"c.vip as vip," +
																					"c.dias_control_post as diascpo," +
																					"c.citas_max_control as cantcpo," +
																					"c.radicar_cuentas_negativas AS radicarcuentasnegativas, " +
																					"c.asignarfac_valorpac_valorabo AS asignarfactvalorpacvalorabono, "+
																					"c.aseguradora AS aseguradora, "+
																					"coalesce(c.cod_aseguradora,'') AS codaseguradora, " +
																					"CASE WHEN c.valor_letras_factura IS NULL THEN '' ELSE c.valor_letras_factura END AS valorletras, "+
																				    "c.reporte_incon_bd AS report_incons_bd, "+
																				    "c.reporte_atencion_ini_urg AS report_aten_ini, "+
																				    "c.gen_auto_val_ate_ini_urg AS gener_auto_aten_ini_urg, " +
																				    "c.req_auto_serv_add AS req_autorizacion_servic, " +
																				    "c.formato_autorizacion AS formato_autorizacion, "+
																				    "c.man_mul_inc_citas AS man_mul_inc_citas, " +
																				    "coalesce (c.val_mul_inc_citas||'','') AS val_mul_inc_citas, "+
																				    "c.centro_costo_contable AS ccContable, "+
																				    "c.maneja_bonos AS maneja_bonos, "+
																				    "c.req_bono_ing_pac AS req_bono_ing_pac, "+
																				    "c.maneja_promociones AS maneja_promociones, "+
																				    "c.maneja_des_odo as maneja_des_odo , "+
																				    "c.es_conv_tar_cli AS es_conv_tar_cli, "+
																				    "c.ing_pac_val_bd AS ing_pac_val_bd, "+
																				    "c.ing_pac_req_aut AS ing_pac_req_aut, "+
																				    "c.req_ing_val_auto AS req_ing_val_auto, "+
																				    "c.tipo_atencion AS tipo_atencion ," +
																				    "c.tipo_liquidacion_pool as tipo_liquidacion_pool," +
																				    "c.maneja_montos as maneja_montos, " +
																				    "c.capitacion_subcontratada as capitacion_subcontratada, " +
																				    "c.maneja_presup_capitacion as maneja_presup_capitacion, " +
																				    "tc.aseg_at_ec AS aseguradora_accidente_transito "+
																				    
																				    " FROM convenios c " +
																				  	" INNER JOIN empresas e ON (c.empresa = e.codigo) " +
																					" INNER JOIN tipos_regimen tr ON(tr.acronimo=c.tipo_regimen) " +
																					" INNER JOIN tipos_contrato t ON(c.tipo_contrato=t.codigo) " +
																					" LEFT OUTER JOIN tipos_convenio tc ON(tc.codigo=c.tipo_convenio AND tc.institucion=c.institucion) "+
																					" LEFT OUTER JOIN tarifarios_oficiales tf ON(tf.codigo=c.tipo_codigo) "+
																					" WHERE c.codigo=  ?" ;

	
	
	private final static String cargarCodigoUltimaInsercion = "SELECT MAX(codigo) AS codigo from convenios";
																					
	/**
	 * Hace la modificaci�n de los datos del convenio
	 */
	private final static String modificarConvenio=	"UPDATE " +
																					"convenios SET " +
																					"empresa= ?, " + //1
																					"tipo_regimen= ?, " +//2
																					"nombre= ?, " + //3
																					"observaciones= ?, " + //4
																					"plan_beneficios= ?, " + //5
																					"codigo_min_salud= ?, " + //6
																					"formato_factura= ?, " + //7
																					"activo= ?, " + //8
																					"tipo_contrato= ?, " + //9
																					"pyp= ?," + //10
																					"unificar_pyp = ?," + //11
																					"num_dias_vencimiento=?," + //12
																					"requiere_justificacion_serv=?," + //13
																					"requiere_justificacion_art=?," + //14
																					"maneja_complejidad=?," + //15
																					"semanas_min_cotizacion=?," + //16
																					"requiere_ingreso_num_carnet=?," + //17
																					"tipo_convenio=?," + //18
																					"tipo_codigo=?," + //19
																					"ajuste_servicios=?," + //20
																					"ajuste_articulos=?," + //21
																					"interfaz=? ," + //22
																					"cantidad_max_cirugia = ?," + //23
																					"cantidad_max_ayudpag = ?," + //24
																					"tipo_liquidacion_scx = ?," + //25
																					"tipo_liquidacion_dyt = ?," + //26
																					"tipo_liquidacion_gcx = ?," + //27
																					"tipo_liquidacion_gdyt = ?," + //28
																					"tipo_tarifa_liqmatecx = ?," + //29
																					"tipo_tarifa_liqmatedyt = ?," + //30
																					"tipo_fecha_liqtiempcx = ?," + //31
																					"tipo_fecha_liqtiempdyt = ?," + //32
																					"liquidacion_tmp_frac_add = ?, " + //33
																					"encabezado_factura=?, " + //34
																					"pie_factura=?," + //35
																					"empresa_institucion=?, " + //36
																					"req_just_art_nopos_dif_med=?," + //37
																					"centro_costo_plan_especial=?," + //38
																					"excento_deudor=?," + //39
																					"excento_doc_garantia=?, " + //40
																					"vip=?," + //41
																					"dias_control_post=?," + //42
																					"citas_max_control=?," + //43
																					"radicar_cuentas_negativas=?," + //44
																					"asignarfac_valorpac_valorabo=?, " + //45
																					"aseguradora = ?, " + //46
																					"cod_aseguradora = ?, " + //47
																					"valor_letras_factura = ?, " + //48
																					"reporte_incon_bd = ?, "+ //49
																					"reporte_atencion_ini_urg = ?, "+ //50
																					"gen_auto_val_ate_ini_urg = ?, " + //51
																					"req_auto_serv_add = ?, " + //52
																					"formato_autorizacion = ?, " + //53
//																					****** anexo 791 *******
																					"man_mul_inc_citas = ?, " + //54
																					"val_mul_inc_citas = ?, " + //55
//																					************************
//																					******* anexo 809 *******
														        					"centro_costo_contable = ?," + //56
														        					"maneja_bonos = ?, " +  //57
																					"req_bono_ing_pac = ?, " + //58
																					"maneja_promociones = ?, " + //59
																					"es_conv_tar_cli = ?, " + //60
																					"ing_pac_val_bd = ?, " + //61
																					"ing_pac_req_aut = ?, " + //62
																					"req_ing_val_auto = ?, " + //63
																					"tipo_atencion = ?,  " +	//64
																					"tipo_liquidacion_pool=?, " + //65
																					"maneja_montos=?, "+ //66
																					"capitacion_subcontratada=?, "+ //67
																					"maneja_presup_capitacion=?, "+ //68
																					"tipo_codigo_articulos=? " + //70 
																					
//														        					*************************
																					" WHERE " +
																					" codigo = ?"; //69
	
	/**
	 * Seleccionar todos los datos de la tabla  convenios para mostrarlos en el listado
	 */
	private final static String consultarConvenios= 	"SELECT c.codigo, c.empresa, e.razon_social, " +
																			"t.nombre AS tipo_regimen1, c.nombre,  c.observaciones,  " +
																			"c.plan_beneficios, c.codigo_min_salud, " +
																			"CASE WHEN c.formato_factura IS NULL THEN 'Estándar' ELSE f.nombre_formato END AS formato_factura1, c.activo, " +
																			"c.tipo_contrato AS tipoContrato, " +
																			"ti.nombre AS nombreTipoContrato, " +
																			"c.pyp, " +
																			"c.unificar_pyp," +
																			"coalesce(c.num_dias_vencimiento||'','') as numdiasvencimiento," +
																			"c.requiere_justificacion_serv as requierejustificacionservicios ," +
																			"c.requiere_justificacion_art as requierejustificacionarticulos ," +
																			"c.req_just_art_nopos_dif_med as requierejustartnoposdifmed ," +
																			"c.maneja_complejidad as manejacomplejidad ," +
																			"coalesce(c.semanas_min_cotizacion||'','') as semanasmincotizacion ," +
																			"c.requiere_ingreso_num_carnet as requierenumcarnet ," +
																			"coalesce(c.tipo_convenio||'','-1')  as tipoconvenio ," +
																			"c.tipo_codigo as tipocodigo ," +
																			"c.ajuste_servicios as ajusteservicios," +
																			"c.ajuste_articulos as ajustearticulos," +
																			"c.interfaz as interfaz, " +
																			"CASE WHEN c.cantidad_max_cirugia IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE c.cantidad_max_cirugia END AS cantidadmaxcirugia," +
																			"CASE WHEN c.cantidad_max_ayudpag IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE c.cantidad_max_ayudpag END AS cantidadmaxayudpag," +																			
																			"CASE WHEN c.tipo_liquidacion_scx IS NULL THEN '' ELSE c.tipo_liquidacion_scx END  AS tipoliquidacionscx," +
																			"CASE WHEN c.tipo_liquidacion_dyt IS NULL THEN '' ELSE c.tipo_liquidacion_dyt END  AS tipoliquidaciondyt," +
																			"CASE WHEN c.tipo_liquidacion_gcx IS NULL THEN '' ELSE c.tipo_liquidacion_gcx END AS tipoliquidaciongcx," +
																			"CASE WHEN c.tipo_liquidacion_gdyt IS NULL THEN '' ELSE c.tipo_liquidacion_gdyt END AS tipoliquidaciongdyt," +
																			"CASE WHEN c.tipo_tarifa_liqmatecx IS NULL THEN '' ELSE c.tipo_tarifa_liqmatecx END AS tipotarifaliqmatecx," +
																			"CASE WHEN c.tipo_tarifa_liqmatedyt IS NULL THEN '' ELSE c.tipo_tarifa_liqmatedyt END AS tipotarifaliqmatedyt," +
																			"CASE WHEN c.tipo_fecha_liqtiempcx IS NULL THEN '' ELSE c.tipo_fecha_liqtiempcx END AS tipofechaliqtiempcx," +
																			"CASE WHEN c.tipo_fecha_liqtiempdyt IS NULL THEN '' ELSE c.tipo_fecha_liqtiempdyt END AS tipofechaliqtiempdyt," +
																			"CASE WHEN c.liquidacion_tmp_frac_add IS NULL THEN '' ELSE c.liquidacion_tmp_frac_add END AS liquidaciontmpfracadd," +
																			// la linea estaba de esta manera pero presentaba error se cambio el plural del campo empresas por empresa (singular)
																			// "CASE WHEN c.empresas_institucion IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE c.empresa_institucion END AS empresas_institucion "+
																			"CASE WHEN c.empresa_institucion IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE c.empresa_institucion END AS empresas_institucion," +
																			"c.radicar_cuentas_negativas AS radicar_cuentas_negativas, " +
																			"c.asignarfac_valorpac_valorabo AS asignar_fact_valor_pac_valor, "+
																			"CASE WHEN c.valor_letras_factura IS NULL THEN '' ELSE c.valor_letras_factura END AS valorletras, "+
																			"c.reporte_incon_bd AS report_incons_bd, "+
																		    "c.reporte_atencion_ini_urg AS report_aten_ini, "+
																		    "c.gen_auto_val_ate_ini_urg AS gener_auto_aten_ini_urg, "+
																		    "c.req_auto_serv_add AS req_autorizacion_servic, " +
																		    "c.formato_autorizacion AS formato_autorizacion, "+
//																		    *********** anexo 791 ***********
																		    "c.man_mul_inc_citas AS man_mul_inc_citas, " +
																		    "coalesce(c.val_mul_inc_citas||'','') AS val_mul_inc_citas, "+
//																		    *********************************
//																		    *********** cambios anexo 809 ***********
																		    "c.centro_costo_contable AS ccContable, "+
																		    "c.maneja_bonos AS maneja_bonos, "+
																		    "c.req_bono_ing_pac AS req_bono_ing_pac, "+
																		    "c.maneja_promociones AS maneja_promociones, "+
																		    "c.es_conv_tar_cli AS es_conv_tar_cli, "+
																		    "c.ing_pac_val_bd AS ing_pac_val_bd, "+
																		    "c.ing_pac_req_aut AS ing_pac_req_aut, "+
																		    "c.req_ing_val_auto AS req_ing_val_auto, "+
																		    "c.tipo_atencion AS tipo_atencion  ," +
																		    "c.tipo_liquidacion_pool as tipo_liquidacion_pool," +
																		    "c.maneja_montos as manejamontos "+
//																		    *****************************************
																			"FROM convenios c " +
																			"INNER JOIN empresas e ON (c.empresa = e.codigo) " +
																			"INNER JOIN tipos_regimen t ON (c.tipo_regimen= t.acronimo) " +
																			"LEFT OUTER JOIN formato_impresion_factura f ON (c.formato_factura= f.codigo) " +
																			"INNER JOIN terceros ter ON (e.tercero=ter.codigo) " +
																			"INNER JOIN tipos_contrato ti ON(c.tipo_contrato=ti.codigo) "+
																			"WHERE ter.institucion = ? "; 	                                                                        
	                                                                         
	
	private final static String actualizarInfoAdicIngresoConveniosStr=" UPDATE convenios " +
																	   " SET info_adic_ingreso_convenios=? ";
	/**
	 *  Insertar un convenio
	 */
	private final static String insertarConvenioStr = "INSERT INTO convenios " +
																				"( " +
																				"codigo, " + //1
																				"empresa, " + //2
																				"tipo_regimen, " + //3
																				"nombre, " + //4
																				"observaciones, " + //5
																				"plan_beneficios, " + //6
																				"codigo_min_salud, " + //7
																				"formato_factura, " + //8
																				"activo, " + //9
																				"tipo_contrato, " + //10
																				"info_adic_ingreso_convenios, " + //11
																				"pyp," + //12
																				"unificar_pyp," + //13
																				"num_dias_vencimiento," + //14
																				"requiere_justificacion_serv," + //15
																				"requiere_justificacion_art," + //16
																				"maneja_complejidad," + //17
																				"semanas_min_cotizacion," + //18
																				"requiere_ingreso_num_carnet," + //19
																				"tipo_convenio, " + //20
																				"tipo_codigo, " + //21
																				"ajuste_servicios," + //22
																				"ajuste_articulos," + //23
																				"interfaz," + //24
																				"institucion," + //25
																				"cantidad_max_cirugia," + //26
																				"cantidad_max_ayudpag," + //27
																				"tipo_liquidacion_scx," + //28
																				"tipo_liquidacion_dyt," + //29
																				"tipo_liquidacion_gcx," + //30
																				"tipo_liquidacion_gdyt," + //31
																				"tipo_tarifa_liqmatecx," + //32
																				"tipo_tarifa_liqmatedyt," + //33
																				"tipo_fecha_liqtiempcx," + //34
																				"tipo_fecha_liqtiempdyt," + //35
																				"liquidacion_tmp_frac_add, " + //36
																				"encabezado_factura, " + //37
																				"pie_factura," +//38
																				"empresa_institucion, " + //39
																				"req_just_art_nopos_dif_med, " + //40
																				"centro_costo_plan_especial, " + //41
																				"excento_deudor, " + //42
																				"excento_doc_garantia, " + //43
																				"vip," + //44
																				"dias_control_post," + //45
																				"citas_max_control," + //46
																				"radicar_cuentas_negativas," + //47
																				"asignarfac_valorpac_valorabo, " + //48
																				"aseguradora, " + //49
																				"cod_aseguradora, " + //50
																				"valor_letras_factura, " + //51
																			//********* Nuevos Campos Anexo 753 Decreto 4747 **************	
																				"reporte_incon_bd, "+ //52
																				"reporte_atencion_ini_urg, "+ //53
																				"gen_auto_val_ate_ini_urg, " + //54
																				"req_auto_serv_add, " + //55
																				"formato_autorizacion, " + //56
																			//*************************************************************
																			//********* Nuevos Campos Anexo 791 ***************************	
																				"man_mul_inc_citas, "+ //57
																				"val_mul_inc_citas, " + //58
																			//*************************************************************
//																			*********** nuevos campos anexo 809 ***************************
																				
																				
																				"centro_costo_contable, " + //59
																				"maneja_bonos, " + //60
																				"req_bono_ing_pac, " + //61
																				"maneja_promociones, " + //62
																				"es_conv_tar_cli, " + //63
																				"ing_pac_val_bd, " + //64
																				"ing_pac_req_aut, " + //65
																				"req_ing_val_auto, " + //66
																				"tipo_atencion," + //67
																				"tipo_liquidacion_pool," +//68
																				"maneja_montos, " +	//69
																				"capitacion_subcontratada, " +	//70
																				"maneja_presup_capitacion, " +	//71
																				"tipo_codigo_articulos " + //72
																				
																				
																			    
																				
																				") " +
//																			***************************************************************
																				"VALUES " +
																				"(?, " + //1
																				"?, " + //2
																				"?, " + //3
																				"?, " + //4
																				"?, " + //5
																				"?, " + //6
																				"?, " + //7
																				"?, " + //8
																				"?," + //9
																				" ?, " +//10
																				"?, " + //11
																				"?, " + //12
																				"?, " + //13
																				"?, " + //14
																				"?, " + //15
																				"?, " + //16
																				"?, " + //17
																				"?, " + //18
																				"?, " + //19
																				"?, " + //20
																				"?, " + //21
																				"?, " + //22
																				"?, " + //23
																				"?," + //24
																				" ?," + //25
																				" ?, " + //26
																				"?, " + //27
																				"?, " + //28
																				"?, " + //29
																				"?, " + //30
																				"?, " + //31
																				"?, " + //32
																				"?, " + //33
																				"?, " +//34
																				"?, " +//35
																				"?, " +//36
																				"?, " +//37
																				"?, " +//38
																				"?, " +//39
																				"?, " +//40
																				"?, " +//41
																				"?, " +//42
																				"?, " +//43
																				"?, " +//44
																				"? ," +//45
																				"?, " +//46
																				"?, " +//47
																				"?, " +//48
																				"?, " +//49
																				"?, " +//50
																				"?, " +//51
																				"?, " +//52
																				"?, " +//53
																				"?, " +//54
																				"?, " +//55
																				"?, " +//56
																				"?, " +//57
																				"?, " +//58
																				"? , " +//59
																				"? , " +//60
																				"? , " +//61
																				"? , " +//62
																				"? , " +//63
																				"? , " +//64
																				"? , " +//65
																				"? , " +//66
																				"? , " + //67
																				"? , " +//68
																				"? , " +//69
																				"? , " + //70
																				"? ," + //71
																				"? "+ //72
																				") ";

	
//***********************************Cambios Anexo 753 Decreto 4747 ******************************************************************************************
	/**
	 * Cadena para insertar los Medios de Envio al Convenio
	 */
	
	
	
	/**
	 * 
	 */
	 
	 
	 
	private final static String insertarMediosEnvioaConvenioStr1="INSERT INTO medios_envio_convenio "+
		                                                             "(codigo_pk, "+
		                                                             "convenio, "+
		                                                             "medio_envio, "+
		                                                             "fecha_modifica, "+
		                                                             "hora_modifica, "+
		                                                             "usuario_modifica) "+
		                                                             "VALUES (?, ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?)";
	
	/**
	 * Cadena para eliminar un medio de Envio
	 */
	private final static String eliminarMediosEnvioConvenioStr=" DELETE FROM medios_envio_convenio " +
			                                                     "WHERE convenio= ? ";	
	
	
	/**
	 * Cadena para cargar medios de Envio de los reportes de un convenio
	 */
	private final static String cargarMediosEnvioStr ="SELECT medio_envio "+
	                                                  "FROM facturacion.medios_envio_convenio "+
	                                                  "WHERE convenio= ? ";
	                                               
	/**
	 * Cadena para consultar los medios de envio asociados al convenio
	 */
	private final static String consultarMedioEnvioStr="SELECT * FROM medios_envio_convenio "+
														"WHERE convenio= ? AND medio_envio= ?";
	
	
	private final static String eliminarAdjuntos="DELETE FROM facturacion.archivos_convenio where convenio = ? ";
	
	
	/**
	 * 
	 * 
	 * 
	 */
	private final static String consultarAdjuntos=" Select ad.codigo  as codigo , ad.convenio as convenio , ad.nombre_original as generado , ad.nombre_archivo as original , '"+ConstantesBD.acronimoSi+"' as checkbox   from  facturacion.archivos_convenio  ad   where ad.convenio=? ";
	/**
	 * Cadena para eliminar un Correo Electronico asociado a un convenio
	 */
	private final static String eliminarCorreoElectronicoStr="DELETE FROM correos_convenio "+
															 "WHERE convenio= ? AND email= ? ";	
															
	
	/**
	 * Cadena para realizar la consulta de los correos electronicos asociados al convenio
	 */
	private final static String cargarCorreosElectronicosStr="SELECT codigo_pk as codigo, "+
	                                                           "email as mail, "+
	                                                           "convenio as convenio, "+
	                                                           "fecha_modifica as fecha, "+
	                                                           "hora_modifica as hora, "+
	                                                           "usuario_modifica as usuario, "+
	                                                           "'"+ConstantesBD.acronimoNo+"' as eliminar "+	                                                 
	                                                           "FROM facturacion.correos_convenio "+
															   "WHERE convenio= ?";
	
	private final static String consultarCorreoElectronico ="SELECT email as mail FROM correos_convenio " +
															 "WHERE convenio= ? and email= ? ";
															
	
			                                                     
//************************************************************************************************************************************
	
	/**
	 * cadena para consultar si el convenio de una cuenta dada es capitado
	 */
	private final static String esConvenioCapitadoStr="select "+ 
		"count(1) AS cuenta "+ 
		"from cuentas cu "+ 
		"inner join sub_cuentas sc ON(sc.ingreso=cu.id_ingreso) "+ 
		"inner join convenios c on (c.codigo=sc.convenio) "+  
		"WHERE "+ 
		"cu.id=? AND "+ 
		"c.tipo_contrato= "+ConstantesBD.codigoTipoContratoCapitado;
	
	/**
	 * M�todo que consulta los contratos vigentes del convenio
	 */
	private final static String consultarContratosVigentesConvenioStr = "SELECT codigo, numero_contrato " +
	 "FROM contratos " +
	 "WHERE convenio = ? AND " +
	 "(to_char(CURRENT_DATE,'"+ConstantesBD.formatoFechaBD+"') BETWEEN to_char(fecha_inicial,'"+ConstantesBD.formatoFechaBD+"') AND to_char(fecha_final,'"+ConstantesBD.formatoFechaBD+"')) and (numero_contrato is not null )";
		
	
	/**
	 * 
	 */
	private final static String consultarCentrosCostoPlanEspecialStr = "SELECT cc.codigo as codigo," +
																			"cc.nombre as centro_costo, " +
																			"ca.descripcion as centro_atencion " +
																			"from administracion.centros_costo cc INNER JOIN administracion.centro_atencion ca " +
																			"ON(ca.consecutivo=cc.centro_atencion) " +
																			"where cc.es_activo=? " +
																			"and cc.institucion=? " +
																			"and cc.tipo_area=? " +
																			"and cc.codigo<>? " +
																			"order by cc.nombre";
	
	/**
	 * 
	 */
	private final static String consultarCentrosCostoContableStr = "SELECT cc.codigo as codigo," +
																			"cc.nombre as centro_costo, " +
																			"ca.descripcion as centro_atencion " +
																			"from administracion.centros_costo cc INNER JOIN administracion.centro_atencion ca " +
																			"ON(ca.consecutivo=cc.centro_atencion) " +
																			"where cc.es_activo=? " +
																			"and cc.institucion=? " +
																			"and cc.tipo_area IN ("+ConstantesBD.codigoTipoAreaDirecto+","+ConstantesBD.codigoTipoAreaIndirecto+")  " +
																			"and cc.codigo > 0 " +
																			"order by cc.nombre";
	
	/**
	 * Cadena consulta de usuarios_glosas_conv
	 */
	private static final String strCadenaConsulta = "SELECT " +
														"ugc.codigo AS codigo, " +
														"ugc.usuario AS usuario, " +
														"getnombreusuario(ugc.usuario) AS nombre_usuario, " +
														"ugc.tipo_usuario AS tipo_usuario, " +
														"ugc.activo AS activo " +
													"FROM " +
														"usuarios_glosas_conv ugc";
	
	/**
	 * Cadena de inserci�n de usuarios_glosas_conv
	 */
	private static final String strCadenaInsertar = "INSERT INTO " +
														"usuarios_glosas_conv " +
													"VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	
	/**
	 * Cadena de modificaci�n de usuarios_glosas_conv
	 */
	private static final String strCadenaActualizar = "UPDATE " +
														"usuarios_glosas_conv " +
													"SET " +
														"usuario = ?, " + //1
														"tipo_usuario = ?, " + //2
														"activo = ?, " + //3
														"convenio = ?, " + //4
														"fecha_modifica = ?, " +//5
														"hora_modifica = ?, " +//6
														"usuario_modifica = ? " +//7
													"WHERE " +
														"codigo = ? ";//8
	
	/**
	 * Cadena que carga los indicadores del convenio 
	 * */
	private static final String strCadenaConsultarIndicadores = "SELECT " +
																"coalesce(reporte_atencion_ini_urg,'"+ConstantesBD.acronimoNo+"') AS reporte_atencion_ini_urg," +
																"coalesce(gen_auto_val_ate_ini_urg,'"+ConstantesBD.acronimoNo+"') AS gen_auto_val_ate_ini_urg," +
																"coalesce(reporte_incon_bd,'"+ConstantesBD.acronimoNo+"') AS reporte_incon_bd, " +
																"coalesce(req_auto_serv_add,'"+ConstantesBD.acronimoNo+"') AS req_auto_servicio " +
																"FROM " +
																"convenios " +
																"WHERE " +
																"codigo = ? ";
	
	
	
	
	private static String InsertarDocumentoAdjuntoConv="INSERT INTO facturacion.archivos_convenio(codigo, convenio , nombre_original, nombre_archivo) VALUES ( ? , ? , ? , ?)";
	
	
	
	/**
	 * @see com.princetonsa.dao.DocumentoAdjuntoDao#insertarDocumentoAdjunto(java.sql.Connection, int, java.lang.String, java.lang.String, boolean)
	 */
	public static boolean insertarDocumentoAdjuntoConvenio(Connection con, int convenio, String nombreArchivo, String nombreOriginal)
	{
		try
		{
			
			logger.info("ENTRO A INSERTAR LOS ARCHIVOS");
			
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(InsertarDocumentoAdjuntoConv,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
			
			int codigo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturacion.seq_archivos_convenio ");
			logger.info("CONVVVVVVVVVVV"+ convenio + "CODIGOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO"+codigo);
			pst.setInt(1,codigo);
			pst.setInt(2,convenio);
			pst.setString(3,nombreOriginal);
			pst.setString(4,nombreArchivo);
			
			
			
			if(pst.executeUpdate()>0)
				return true;
			else
				return false;
		}
		catch(SQLException e)
		{
			logger.error("Problemas insertando el archivo adjunto",e);
			return false;
		}
	}
	
	/**
	 * Inserta un convenio
	 * @param convenioManejaMontoCobro 
	 * @param unificarPyp 
	 * @param tipoConvenio 
	 * @param requiereCarnet 
	 * @param semanasMinimasCotizacion 
	 * @param manejaComplejidad 
	 * @param requiereJustificacion 
	 * @param numerDiasVencimiento 
	 * @param institucion 
	 * @param empresaInstitucion 
	 * @param codigoAseguradora 
	 * @param aseguradora 
	 * @param ccContable 
	 * @param cenAtencContable 
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param empresa, int, c�digo de la empresa (tabla empresas - estado activo)
	 * @param tipoRegimen, String, r�gimen de acuerdo a los previam/t
	 * 				ingresados en el sistema
	 * @param nombre, String, nombre del convenio
	 * @param observaciones, String, observaciones del convenio 
	 * @param planBeneficios, String, descripci�n del plan de beneficios
	 * @param codigoMinSalud, String, codigo Minsalud
	 * @param formatoFactura, int, selecciona el tipo de formato de factura 
	 * 				que utiliza el convenio  
	 * @param activo, boolean, si el convenio est� activo en el sistema o no
	 * 
	 * @param Cantidad max cirugia adicionales
	 * @param Cantidad Max ayudantes que paga
	 * @param Tipo de Liquidacion Salas Cirugia
	 * @param Tipo de Liquidacion Salas No Cruentos
	 * @param Tipo de Liquidacion General Cirugias
	 * @param Tipo de Liquidacion General No Cruetos
	 * @param Tipo de Tarifa para liquidacion materiales Cirugia
	 * @param Tipo de Tarifa para liquidacion materiales No Cruentos
	 * @param Tipo de Fecha liquidacion Tiempos Cirugia
	 * @param Tipo de Fecha para Liquidacion Tiempos No Cruentos
	 * @param Liquidacion de Tiempos x Fraccion Adicional Cumplida
	 * 
	 * @return int, 0 si  no inserta, 1 si inserta
	 */
	public static int  insertar(	Connection con,
									UsuarioBasico usuario,
									int empresa,
									String tipoRegimen,
									String nombre,
									String observaciones,
									String planBeneficios,
									String codigoMinSalud,
									int formatoFactura,
									boolean activa,
									boolean convenioManejaMontoCobro, int tipoContrato, 
									String checkInfoAdicCuenta,
									int codigoConvenioAInsertar,
									String pyp,
									String unificarPyp,
									String numerDiasVencimiento,
									String requiereJustificacionServicios,
									String requiereJustificacionArticulos,
									String requiereJustArtNoposDifMed,
									String manejaComplejidad,
									String semanasMinimasCotizacion,
									String requiereCarnet,
									String tipoConvenio,
									int tipoCodigo,
									int tipoCodigoArt,
									String ajusteServicios,
									String ajusteArticulos,
									String interfaz,
									int institucion,
									
									int cantidadMaxCirugia,
									int cantidadMaxAyudpag,
									String tipoLiquidacionScx,
									String tipoLiquidacionDyt,
									String tipoLiquidacionGcx,
									String tipoLiquidacionGdyt,
									String tipoTarifaLiqMateCx,
									String tipoTarifaLiqMateDyt,
									String tipoFechaLiqTiemPcx,
									String tipoFechaLiqTiempDyt,
									String liquidacionTmpFracAdd,
									int planEspecial,
									String excentoDeudor,
									String excentoDocumentoGarantia,
									String vip,
									String radicarCuentasNegativas,
									String asignarFactValorPacValorAbono,
									
									String encabezadoFactura,
									String pieFactura, 
									String empresaInstitucion,
									int cantCPO,
									int diasCPO, 
									String aseguradora, 
									String codigoAseguradora,
									String valorLetrasFactura,
									//*************Cambios Anexo 753 Decreto 4747*****************************
									String repInconsistBD,
		    						String repAtencIniUrg,
		    						String generAutoValAteniniUrg,
		        					String requiereAutorizacionServicio,
		        					String formatoAutorizacion,
		        					//*****************Anexo 791 Decreto*******************************
		        					String manejaMultasPorIncumplimiento,
		        					String valorMultaPorIncumplimientoCitas,
		                            //*****************************************************************
//		        					****** anexo 809 ***********
		        					int ccContable,
		        					 //		        					****************************
		        					int cenAtencContable, // anexo 50
		        					String manejaBonos,
									String requiereBono,
									String manejaPromociones,
									String esTargetaCliene,
									String ingresoBdValido,
									String ingresoPacienteReqAutorizacion,
									String reqIngresoValido,
									String tipoAtencion,
									String tipoLiquidacionPool,
									String manejaPresupCapitacion,
									String capitacionSubcontratada){
		try
		{
			logger.info("ENTRO A INSERTAR + convenio::"+codigoConvenioAInsertar+"\n\nconsulta--_>>>>"+insertarConvenioStr+"\n\nTipo Atenci�n");
			
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarConvenioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1,codigoConvenioAInsertar);
			ps.setInt(2,empresa);
			ps.setString(3, tipoRegimen);
			ps.setString(4,nombre);
			ps.setString(5,observaciones);
			ps.setString(6,planBeneficios);
			
			
			//Por tarea 157024 - Se hace NULL este campo, por ende se valida cuando no llegue requerido
			if (!codigoMinSalud.trim().equals(""))
				ps.setString(7,codigoMinSalud);
			else
				ps.setNull(7, Types.VARCHAR);
				
			if(formatoFactura==0)
			{
				ps.setObject(8,null);
			}
			else
			{
				ps.setInt(8,formatoFactura);
			}
			ps.setBoolean(9,activa);
			ps.setInt(10, tipoContrato);
			if(checkInfoAdicCuenta.equals("true"))
				ps.setBoolean(11, true);
			else if(checkInfoAdicCuenta.equals("false"))
				ps.setBoolean(11, false);
			if(pyp.equals("true"))
				ps.setBoolean(12, true);
			else if(pyp.equals("false"))
				ps.setBoolean(12, false);

			if(unificarPyp.equals("true"))
				ps.setBoolean(13, true);
			else 
				ps.setBoolean(13, false);
			
			if(UtilidadTexto.isEmpty(numerDiasVencimiento))
				ps.setObject(14, null);
			else
				ps.setInt(14, Integer.parseInt(numerDiasVencimiento));
			if(UtilidadTexto.isEmpty(requiereJustificacionServicios))
				ps.setString(15, ConstantesBD.acronimoNo);
			else
				ps.setString(15, requiereJustificacionServicios);
			if(UtilidadTexto.isEmpty(requiereJustificacionArticulos))
				ps.setString(16, ConstantesBD.acronimoNo);
			else
				ps.setString(16, requiereJustificacionArticulos);
			if(UtilidadTexto.isEmpty(manejaComplejidad))
				ps.setString(17, ConstantesBD.acronimoNo);
			else
				ps.setString(17, manejaComplejidad);
			if(UtilidadTexto.isEmpty(semanasMinimasCotizacion))
				ps.setObject(18, null);
			else
				ps.setInt(18, Integer.parseInt(semanasMinimasCotizacion));
			if(UtilidadTexto.isEmpty(requiereCarnet))
				ps.setString(19, ConstantesBD.acronimoNo);
			else
				ps.setString(19, requiereCarnet);
			if(Utilidades.convertirAEntero(tipoConvenio)>0)
				ps.setString(20, tipoConvenio);
			else
				ps.setObject(20, null);
			if(tipoCodigo>0)
				ps.setInt(21, tipoCodigo);
			else
				ps.setObject(21, null);
			if(tipoCodigoArt>=0)
				ps.setInt(72, tipoCodigoArt);
			else
				ps.setObject(72, null);
			
			if(ajusteServicios==null || ajusteServicios.trim().equals(""))
				ps.setObject(22, null);
	        else
	        	ps.setString(22,ajusteServicios);
			
			if(ajusteArticulos==null || ajusteArticulos.trim().equals(""))
				ps.setObject(23, null);
	        else
	        	ps.setString(23,ajusteArticulos);					
			
			if(interfaz==null || interfaz.trim().equals(""))
				ps.setObject(24, null);
	        else
	        	ps.setString(24,interfaz);
			
			ps.setInt(25, institucion);
			
			//----
			if(cantidadMaxCirugia != ConstantesBD.codigoNuncaValido)
				ps.setObject(26,cantidadMaxCirugia);
			else
				ps.setNull(26,Types.NULL);
			
			if(cantidadMaxAyudpag != ConstantesBD.codigoNuncaValido)
				ps.setObject(27,cantidadMaxAyudpag);
			else
				ps.setNull(27,Types.NULL);
								
			if(!tipoLiquidacionScx.equals(""))
				ps.setObject(28,tipoLiquidacionScx);
			else
				ps.setNull(28,Types.NULL);
			
			if(!tipoLiquidacionDyt.equals(""))
				ps.setObject(29,tipoLiquidacionDyt);
			else
				ps.setNull(29,Types.NULL);
			
			if(!tipoLiquidacionGcx.equals(""))
				ps.setObject(30,tipoLiquidacionGcx);
			else
				ps.setNull(30,Types.NULL);
			
			if(!tipoLiquidacionGdyt.equals(""))
				ps.setObject(31,tipoLiquidacionGdyt);
			else
				ps.setNull(31,Types.NULL);
			
			if(!tipoTarifaLiqMateCx.equals(""))
				ps.setObject(32,tipoTarifaLiqMateCx);
			else
				ps.setNull(32,Types.NULL);
			
			if(!tipoTarifaLiqMateDyt.equals(""))
				ps.setObject(33,tipoTarifaLiqMateDyt);
			else
				ps.setNull(33,Types.NULL);
			
			if(!tipoFechaLiqTiemPcx.equals(""))
				ps.setObject(34,tipoFechaLiqTiemPcx);
			else
				ps.setNull(34,Types.NULL);
			
			if(!tipoFechaLiqTiempDyt.equals(""))
				ps.setObject(35,tipoFechaLiqTiempDyt);
			else
				ps.setNull(35,Types.NULL);
			
			if(!liquidacionTmpFracAdd.equals(""))
				ps.setObject(36,liquidacionTmpFracAdd);
			else
				ps.setNull(36,Types.NULL);
			//--
			
			if(UtilidadTexto.isEmpty(encabezadoFactura))
				ps.setString(37, null);
			else
				ps.setString(37, encabezadoFactura);
			
			if(UtilidadTexto.isEmpty(pieFactura))
				ps.setString(38, null);
			else
				ps.setString(38, pieFactura);
			
			/*if(UtilidadTexto.isEmpty(empresaInstitucion))
				ps.setObject(39, null);
			else
				ps.setObject(39, empresaInstitucion);*/
			
			if(ValoresPorDefecto.getInstitucionMultiempresa(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi))
				ps.setObject(39, empresaInstitucion);
			else
				ps.setObject(39, null);
			if(UtilidadTexto.isEmpty(requiereJustArtNoposDifMed))
				ps.setString(40, ConstantesBD.acronimoNo);
			else
				ps.setString(40, requiereJustArtNoposDifMed);
			
			if(planEspecial==ConstantesBD.codigoNuncaValido)
				ps.setNull(41, Types.NUMERIC);
			else
				ps.setInt(41, planEspecial);
			
			ps.setString(42, excentoDeudor);
			ps.setString(43, excentoDocumentoGarantia);
			ps.setString(44, vip);
			ps.setInt(46, diasCPO);
			ps.setInt(45, cantCPO);
			ps.setString(47,radicarCuentasNegativas);
			ps.setString(48,asignarFactValorPacValorAbono);
			
			//Inluido por el Anexo 722
			ps.setString(49, aseguradora);
			if(UtilidadCadena.noEsVacio(codigoAseguradora))
				ps.setString(50, codigoAseguradora);
			else
				ps.setNull(50, Types.VARCHAR);
			
			if(!valorLetrasFactura.equals(""))
				ps.setString(51, valorLetrasFactura);
			else
				ps.setNull(51, Types.VARCHAR);
			
		//*************Cambios Anexo 753 Decreto 4747*****************************	
			if(!repInconsistBD.equals(""))
				ps.setString(52, repInconsistBD);
			else
				ps.setString(52, ConstantesBD.acronimoNo);
			
			if(!repAtencIniUrg.equals(""))
				ps.setString(53,repAtencIniUrg);
			else
				ps.setString(53, ConstantesBD.acronimoNo);
			
			if(!generAutoValAteniniUrg.equals(""))
				ps.setString(54,generAutoValAteniniUrg);
			else
				ps.setString(54, ConstantesBD.acronimoNo);
			
			if(!requiereAutorizacionServicio.equals(""))
				ps.setString(55,requiereAutorizacionServicio);
			else
				ps.setString(55, ConstantesBD.acronimoNo);
			
			
			if(!formatoAutorizacion.equals(""))
				ps.setString(56,formatoAutorizacion);
			else
				ps.setNull(56, Types.VARCHAR);
			
	    //***************************************************************************
			
		//************* Cambios Anexo 791 *******************************************
			if(!manejaMultasPorIncumplimiento.equals(""))
				ps.setString(57, manejaMultasPorIncumplimiento);
			else
				ps.setNull(57, Types.VARCHAR);
			if(!valorMultaPorIncumplimientoCitas.equals(""))
				ps.setInt(58, Utilidades.convertirAEntero(valorMultaPorIncumplimientoCitas));
			else
				ps.setNull(58, Types.INTEGER);
			
		//***************************************************************************
			
//					****************** Cambios Anexo 809 *********************************
			if(ccContable > 0)
				ps.setInt(59, ccContable);
			else
				ps.setNull(59, Types.INTEGER);
			
//					**********************************************************************
			if(!UtilidadTexto.isEmpty(manejaBonos))
				ps.setString(60, manejaBonos);
			else
				ps.setNull(60, Types.VARCHAR);
			
			if(!UtilidadTexto.isEmpty(requiereBono))
				ps.setString(61,requiereBono);
			else
				ps.setNull(61, Types.VARCHAR);
			
			if(!UtilidadTexto.isEmpty(manejaPromociones))
				ps.setString(62,manejaPromociones);
			else
				ps.setNull(62, Types.VARCHAR);
			
			if(!UtilidadTexto.isEmpty(esTargetaCliene))
				ps.setString(63, esTargetaCliene);
			else
				ps.setNull(63, Types.VARCHAR);
			
			if(!UtilidadTexto.isEmpty(ingresoBdValido))
				ps.setString(64, ingresoBdValido);
			else
				ps.setNull(64, Types.VARCHAR);
			
			if(!UtilidadTexto.isEmpty(ingresoPacienteReqAutorizacion))
				ps.setString(65, ingresoPacienteReqAutorizacion);
			else
				ps.setNull(65, Types.VARCHAR);
			
			if(!UtilidadTexto.isEmpty(reqIngresoValido))
				ps.setString(66, reqIngresoValido);
			else
				ps.setNull(66, Types.VARCHAR);
			
			
				ps.setString(67, tipoAtencion);
			
			if(!UtilidadTexto.isEmpty(tipoLiquidacionPool))
			{
				ps.setString(68, tipoLiquidacionPool);
			}
			else
			{
				ps.setNull(68, Types.VARCHAR);
			}
			ps.setString(69, convenioManejaMontoCobro?"S":"N");
		
			if(!UtilidadTexto.isEmpty(manejaPresupCapitacion)&& manejaPresupCapitacion.equals(ConstantesBD.acronimoSi)){
				ps.setString(71, manejaPresupCapitacion);
			}
			else{
				ps.setNull(71, Types.VARCHAR);
			}
			
			if(!UtilidadTexto.isEmpty(capitacionSubcontratada)){
				ps.setString(70, capitacionSubcontratada);
			}
			else{
				ps.setNull(70, Types.VARCHAR);
			}
			
			
			int res;	
			if(ps.executeUpdate()>0 )
				res=codigoConvenioAInsertar;
			else
				res=0;
			
			ps.close();
			if(res>0 && cenAtencContable>0)
			{
				insertatCentroAtencionContable(con, codigoConvenioAInsertar, cenAtencContable);
			}
			return res;
					
		}
		catch(SQLException e)
		{
				logger.warn(e+" Error en la inserci�n de datos: SqlBaseConvenioDao "+e.toString()+"Consulta "+insertarConvenioStr );
		}
		return 0;
	}

	private static int modificarCentroAtencionContable(Connection con, int codigoConvenioAInsertar, int cenAtencContable) {
		try{
			String sentencia="UPDATE facturacion.cen_aten_contable_conv SET centro_atencion=? WHERE convenio=?";
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con.prepareStatement(sentencia));
			psd.setInt(1, cenAtencContable);
			psd.setInt(2, codigoConvenioAInsertar);
			int res2=psd.executeUpdate();
			psd.close();
			if(res2<=0)
			{
				return 0;
			}
		}catch (SQLException e) {
			logger.error("Error modificando el centro de atenci�n contable", e);
		}
		return -1;
	}

	private static int eliminarCentroAtencionContable(Connection con, int codigoConvenioAInsertar) {
		try{
			String sentencia="DELETE FROM facturacion.cen_aten_contable_conv WHERE convenio=?";
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con.prepareStatement(sentencia));
			psd.setInt(1, codigoConvenioAInsertar);
			int res2=psd.executeUpdate();
			psd.close();
			if(res2<=0)
			{
				return 0;
			}
		}catch (SQLException e) {
			logger.error("Error modificando el centro de atenci�n contable", e);
		}
		return -1;
	}

	private static int insertatCentroAtencionContable(Connection con, int codigoConvenioAInsertar, int cenAtencContable) {
		logger.info("***++++++++++++++++++");
		logger.info("Codigo Convenio: " + codigoConvenioAInsertar);
		logger.info("Centro Atencion Contable: " + cenAtencContable);
		
		try {
			String sentencia="INSERT INTO facturacion.cen_aten_contable_conv(convenio, centro_atencion) VALUES(?, ?)";
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con.prepareStatement(sentencia));
			psd.setInt(1, codigoConvenioAInsertar);
			psd.setInt(2, cenAtencContable);
			int res2=psd.executeUpdate();
			psd.close();
			if(res2<=0)
			{
				return 0;
			}
		}catch (SQLException e) {
			logger.error("Error ingresando el centro de atenci�n contable", e);
		}
		return -1;
	}

	//************************** Nuevos M�todos - Anexo 753 Decreto 4747 ******************************************************************	
	/**
	 * Metodo que Inserta los medios de Envio a un Convenio
	 * @param con
	 * @param usuario
	 * @param codConvenio
	 * @param medioEnvio
	 * @param fechaModificacion
	 * @param horaModificacion
	 * @return int, 0 si  no inserta, 1 si inserta
	 */
	
	
	
	
	
	public static ArrayList<DtoMediosAutorizacion> cargarMediosAutorizacion(Connection con, int convenio, String fromTabla) throws BDException
	{
		ArrayList<DtoMediosAutorizacion> arrayDto = new ArrayList<DtoMediosAutorizacion>();
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try {
			Log4JManager.info("############## Inicio cargarMediosAutorizacion");
			String consultaStr = 	"SELECT " ;
			for(int w=0; w<Convenio.mediosAutorizacionVector.length; w++)
			{
				consultaStr+=" (select codigo from facturacion.medios_aut_conv_odo where convenio="+convenio+" and tipo='"+Convenio.mediosAutorizacionVector[w]+"') as codigo_"+Convenio.mediosAutorizacionVector[w]+", ";
				consultaStr+=" (select fecha_modifica||'' from facturacion.medios_aut_conv_odo where convenio="+convenio+" and tipo='"+Convenio.mediosAutorizacionVector[w]+"') as fecha_"+Convenio.mediosAutorizacionVector[w]+", ";
				consultaStr+=" (select hora_modifica from facturacion.medios_aut_conv_odo where convenio="+convenio+" and tipo='"+Convenio.mediosAutorizacionVector[w]+"') as hora_"+Convenio.mediosAutorizacionVector[w]+", ";
				consultaStr+=" (select usuario_modifica from facturacion.medios_aut_conv_odo where convenio="+convenio+" and tipo='"+Convenio.mediosAutorizacionVector[w]+"') as usuario_"+Convenio.mediosAutorizacionVector[w]+", ";
				consultaStr+=" (select getintegridaddominio('"+Convenio.mediosAutorizacionVector[w]+"') "+fromTabla+") as nombre_tipo_"+Convenio.mediosAutorizacionVector[w]+", ";
				consultaStr+=" (select 'true' from facturacion.medios_aut_conv_odo where convenio="+convenio+" and tipo='"+Convenio.mediosAutorizacionVector[w]+"') as activo_"+Convenio.mediosAutorizacionVector[w]+" ";
				if(w!=(Convenio.mediosAutorizacionVector.length-1))
					consultaStr+=", ";
			}
			consultaStr+=fromTabla;
			pst = con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs = pst.executeQuery();
			if(rs.next()) 
			{
				for(int w=0; w<Convenio.mediosAutorizacionVector.length; w++)
				{
					DtoMediosAutorizacion dto = new DtoMediosAutorizacion();
					dto.setCodigo(rs.getInt("codigo_"+Convenio.mediosAutorizacionVector[w]));
					dto.setConvenio(convenio);
					dto.setFechaModifica(rs.getString("fecha_"+Convenio.mediosAutorizacionVector[w]));
					dto.setHoraModifica(rs.getString("hora_"+Convenio.mediosAutorizacionVector[w]));
					dto.setTipo(new InfoDatosStr(Convenio.mediosAutorizacionVector[w], rs.getString("nombre_tipo_"+Convenio.mediosAutorizacionVector[w])));
					dto.setUsuarioModifica(rs.getString("usuario_"+Convenio.mediosAutorizacionVector[w]));
					dto.setActivo(UtilidadTexto.getBoolean(rs.getString("activo_"+Convenio.mediosAutorizacionVector[w])));
					arrayDto.add(dto);
				}	
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
		Log4JManager.info("############## Fin cargarMediosAutorizacion");
		return arrayDto;
	}
	
	
	
	
	
	public static boolean eliminarMediosAutorizacion(int convenio, Connection con)
	{
		String consultaStr = "DELETE FROM facturacion.medios_aut_conv_odo where convenio="+convenio;
		
		try 
		{
		
		
		PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
		ps.executeUpdate();
		ps.close();
		return true;
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN eliminar  aut"+ e);
		
		}
		return false;
		
	}
	
	/**
	 * 
	 * @param dto
	 * @param con
	 * @return
	 */
	public static double insertarMedioAutorizacion(DtoMediosAutorizacion dto , Connection con) {
		
		
		logger.info("*************** Entro agregar Medio Auto" + dto.getActivo());   
		logger.info("*************** *Elemento en  es************" + dto.getActivo());  
		
		int secuencia = ConstantesBD.codigoNuncaValido;
		try 
		{
			secuencia = UtilidadBD.obtenerSiguienteValorSecuencia(con,"facturacion.seq_medios_aut");
			dto.setCodigo(secuencia);
			
			logger.info("*************** *secuencia************" + secuencia);  
			logger.info(UtilidadLog.obtenerString(dto, true));
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(insertarMedioAutorizacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, secuencia);
			ps.setInt(2, dto.getConvenio());
				
			
			ps.setDate(3, Date.valueOf(dto.getFechaModificaFromatoBD()));
			ps.setString(4, dto.getHoraModifica());
			ps.setString(5, dto.getTipo().getCodigo());
			ps.setString(6, dto.getUsuarioModifica());

			if (ps.executeUpdate() > 0) 
			{
				ps.close();
				return secuencia;
			}
			ps.close();
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR en insert " + e);
			e.printStackTrace();
			secuencia = ConstantesBD.codigoNuncaValido;
		}
		return secuencia;
	}
	
	/**
	 * 
	 */
	public static int insertarMediosEnvio(Connection con,UsuarioBasico usuario,	int codConvenio,String[] mediosEnvio)
	{
		boolean band=true;
		try
		{
			logger.info("ENTRO A INSERTAR MEDIOS ENVIO");	
			for(int i=0; i<mediosEnvio.length;i++)
			{
				if(!mediosEnvio[i].equals("") && mediosEnvio[i]!=null )
				{ 
					PreparedStatementDecorator psEnvio=  new PreparedStatementDecorator(con.prepareStatement(insertarMediosEnvioaConvenioStr1,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	

					psEnvio.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_medios_envio_conv"));
					psEnvio.setInt(2,codConvenio);
					psEnvio.setString(3, mediosEnvio[i]);
					psEnvio.setString(4,usuario.getLoginUsuario());

					if(psEnvio.executeUpdate()<=0)
					{
						band=false;
						i=mediosEnvio.length;
					}  
				}  
			} 

			if (band)
				return 1;
			else
				return 0; 
		}

		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserci�n de datos: SqlBaseConvenioDao "+e.toString()+"Consulta: >>  "+insertarMediosEnvioaConvenioStr1 );
		}
		return 0;
	}
	
	
	/**
	 * Metodo para modificar los medios de envio del Convenio, elimina en caso de venir vacio el medio de envio
	 * y adiciona en caso de venir lleno
	 * @param con
	 * @param codConvenio
	 * @param mediosEnvio
	 * 
	 * @return int, 0 si no modifico , 1 si modifico correctamente
	 */
	public static int modificarMediosEnvio(Connection con,
			UsuarioBasico usuario,
			int codConvenio,
			String[] mediosEnvio
			)
	{
		int resp=0;
		try
		{
			
			if (con == null || con.isClosed()) 
			{
					DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
					con = myFactory.getConnection();
			}			
		  logger.info("ENTRO A MODIFICAR MEDIOS DE ENVIO");	
		  
		  PreparedStatementDecorator psEnvio=  new PreparedStatementDecorator(con.prepareStatement(eliminarMediosEnvioConvenioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet)); 
		  psEnvio.setInt(1,codConvenio); 
		  
		  if(psEnvio.executeUpdate()<=0)
	      {
		    logger.info(" Error en la eliminacion de Envios: SqlBaseConvenioDao Consulta: >>  "+eliminarMediosEnvioConvenioStr);
		      
	      } 
		  
		  for(int i=0; i<mediosEnvio.length;i++)
		  {
			  if(!mediosEnvio[i].equals(""))
			  {

				  PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarMediosEnvioaConvenioStr1,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

				  ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_medios_envio_conv"));
				  ps.setInt(2,codConvenio);
				  ps.setString(3, mediosEnvio[i]);
				  ps.setString(4,usuario.getLoginUsuario());

				  if(ps.executeUpdate()<=0)
				  {
					  i=mediosEnvio.length;
					  logger.info(" Error en la modificacion de datos: SqlBaseConvenioDao Consulta: >>  "+insertarMediosEnvioaConvenioStr1);
					  return resp;

				  }  
			  }
		  }  


		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la modificacion de datos: SqlBaseConvenioDao "+e.toString()+"Consulta: >>  "+eliminarMediosEnvioConvenioStr);
			return resp;
		}
		return resp;
	}
	
	
	
	/**
	 * Metodo que carga los Medios de Envio de los reportes de un Convenio
	 * @param con
	 * @param codigo
	 * @return
	 * @throws SQLException
	 */
	public static  HashMap<String, String> cargarMediosEnvio(Connection con, int codigo)throws SQLException, BDException
	{
		Log4JManager.info("cargarMediosEnvioStr=> "+cargarMediosEnvioStr+"\n Codigo=> "+codigo);
		HashMap<String, String> mapa = null;
		
		try {
			PreparedStatementDecorator cargarMediosEnvioStatement= new PreparedStatementDecorator(con.prepareStatement(cargarMediosEnvioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarMediosEnvioStatement.setInt(1, codigo);
			ResultSetDecorator rs=new ResultSetDecorator(cargarMediosEnvioStatement.executeQuery()); 
			mapa=UtilidadBD.cargarValueObject(rs, true, true);
			cargarMediosEnvioStatement.close();
			rs.close();
		}
		catch (SQLException sqe) {
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return mapa;
	}
	
	
	/**
	 * Metodo de consulta los correos Electronicos asociados a un convenio
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static HashMap consultarCorreosElectronicos(Connection con,
			int codigoConvenio) {
		
		HashMap mapa = new HashMap();
		mapa.put("0", "numRegistros");
		//logger.info("ENTRO A CARGAR LOS CORREOS");
	try{	
		PreparedStatementDecorator cargarCorreosElectronicosStatement= new PreparedStatementDecorator(con.prepareStatement(cargarCorreosElectronicosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cargarCorreosElectronicosStatement.setInt(1, codigoConvenio);
	    mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(cargarCorreosElectronicosStatement.executeQuery()), true, true);
          //logger.info("CADENA CONSULTA CORREOS >>" +cargarCorreosElectronicosStr);
          //logger.info("Convenio: "+codigoConvenio);
          
	    }
	   catch(SQLException e)
	   {
		logger.warn("Error en el listado de Correos " +e.toString());
		
	    }
		return mapa;
	 }
	
	/**
	 * Metodo que inserta los Correos electronicos del Convenio
	 * @param con
	 * @param usuario
	 * @param codConvenio
	 * @param correos
	 * @param insertarCorreoElectronicoStr 
	 * @return
	 */
	public static int insertarCorreosElectronicos(Connection con, UsuarioBasico usuario,int codConvenio, HashMap correos, String insertarCorreoElectronicoStr)
	{
		boolean band=true;
		try
		{
		  logger.info("ENTRO A INSERTAR CORREOS ELECTRONICOS");	
		  	for(int i=0; i<Integer.parseInt(correos.get("numRegistros")+"");i++)
		  	{
		  		PreparedStatementDecorator psCorreo=  new PreparedStatementDecorator(con.prepareStatement(insertarCorreoElectronicoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	
		          
		  		psCorreo.setString(1,correos.get("mail_"+i).toString());      
		  		psCorreo.setInt(2,codConvenio);
		  		psCorreo.setString(3,usuario.getLoginUsuario());
	                	 			    
		        	  if(psCorreo.executeUpdate()<=0)
		        	      {
						    band=false;
		        	        i=Integer.parseInt(correos.get("numRegistros")+"");
		        	      }  
		        }  
		         
		  		if (band)
		  			return 1;
		  		else
		  			return 0; 
		  }
		  
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserci�n de datos: SqlBaseConvenioDao "+e.toString()+"Consulta: >>  "+insertarMediosEnvioaConvenioStr1 );
		}
		return 0;
		
	
	}
	
	/**
	 * Metodo que modifica los correos electronicos asociados a un Convenio
	 * @param con
	 * @param usuario
	 * @param codConvenio
	 * @param correos
	 * @param insertarCorreoElectronicoStr 
	 * @return
	 */
	public static int modificarCorreosElectronicos(Connection con,
			UsuarioBasico usuario,
			int codConvenio,
			HashMap correos, String insertarCorreoElectronicoStr
			)
	{
		boolean band=true;
		try
		{
			
			if (con == null || con.isClosed()) 
			{
					DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
					con = myFactory.getConnection();
			}			
		  logger.info("ENTRO A MODIFICAR CORREOS ELECTRONICOS");	
		  
		  for(int i=0; i<Integer.parseInt(correos.get("numRegistros")+"");i++)
		     {
			  
			  if(correos.get("eliminar_"+i).toString().equals(ConstantesBD.acronimoSi)){
				 
				  PreparedStatementDecorator pscorreoelim =  new PreparedStatementDecorator(con.prepareStatement(eliminarCorreoElectronicoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    		  pscorreoelim.setInt(1, codConvenio);
				  pscorreoelim.setString(2, correos.get("mail_"+i).toString());
				  logger.info("consulta eliminar correo: "+eliminarCorreoElectronicoStr+" mail: "+correos.get("mail_"+i).toString() +" convenio: "+codConvenio );	
				  
				  pscorreoelim.executeUpdate();
			  }
			  else
			  {
				  PreparedStatementDecorator psconsulta=  new PreparedStatementDecorator(con.prepareStatement(consultarCorreoElectronico,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    		  psconsulta.setInt(1, codConvenio);
				  psconsulta.setString(2, correos.get("mail_"+i).toString());
				  logger.info("consulta consultar correo: "+consultarCorreoElectronico +" mail: "+correos.get("mail_"+i).toString() +" convenio: "+codConvenio);
				  ResultSetDecorator rs = new ResultSetDecorator(psconsulta.executeQuery());
					  if(!rs.next())
					   {
						  PreparedStatementDecorator psCorreo=  new PreparedStatementDecorator(con.prepareStatement(insertarCorreoElectronicoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	
						  
					        psCorreo.setString(1,correos.get("mail_"+i).toString());      
					        psCorreo.setInt(2,codConvenio);
					        logger.info("consulta insertar correo: "+insertarCorreoElectronicoStr +" mail:"+correos.get("mail_"+i).toString()+" convenio: "+codConvenio);
					        psCorreo.setString(3,usuario.getLoginUsuario());
			                	 			    
				        	  if(psCorreo.executeUpdate()<=0)
				        	      {
				        	        i=Integer.parseInt(correos.get("numRegistros")+"");
				        	        band=false;
				        	      } 
					     }
			  }
		     }
		  if (band)
	  			return 1;
	  		else
	  			return 0; 
		
		}
		  catch(SQLException e)
			{
				logger.warn(e+" Error en la modificacion de Correos: SqlBaseConvenioDao "+e.toString()+"Consulta: >>  ");
			    return 0;
			}
			
			
	}
	
	
	
//************************************************************************************************************************************************
	
	/**
	 * M�todo que  carga  los datos de un convenio seg�n los datos
	 * que lleguen del  c�digo del convenio para mostrarlos en el resumen
	 * en una BD PostgresSQL o Hsqldb 
	 */
	public static boolean cargarResumen(Connection con, int codigo, Convenio convenio) throws SQLException, BDException
	{
		boolean band1=false;
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try {
			Log4JManager.info("############## Inicio cargarResumen");
			pst=con.prepareStatement(cargarDatosConvenio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigo);
			rs=pst.executeQuery();
			if (rs.next())
			{
				convenio.setCodigo(rs.getInt("codigo"));
				convenio.setEmpresa(rs.getInt("empresa"));
				convenio.setTipoRegimen(rs.getString("tipoRegimen")==null?"":rs.getString("tipoRegimen"));
				convenio.setDescripcionTipoRegimen(rs.getString("nombretiporegimen")==null?"":rs.getString("nombretiporegimen"));
				convenio.setNombre(rs.getString("nombre")==null?"":rs.getString("nombre"));
				convenio.setObservaciones(rs.getString("observaciones")==null?"":rs.getString("observaciones"));
				convenio.setPlanBeneficios(rs.getString("planBeneficios"));
				convenio.setCodigoMinSalud(rs.getString("codigoMinSalud")==null?"":rs.getString("codigoMinSalud"));
				convenio.setFormatoFactura(rs.getObject("formatoFactura")==null?0:rs.getInt("formatoFactura"));
				convenio.setActiva(rs.getBoolean("activa"));
				convenio.setTipoContrato( rs.getInt("tipoContrato"));
				convenio.setCheckInfoAdicCuenta(rs.getBoolean("infoAdicIngresoConvenio")+"");
				convenio.setPyp(UtilidadTexto.getBooleanSegunTipoBD(rs.getBoolean("pyp"))+"");
				convenio.setUnificarPyp(rs.getBoolean("unificar_pyp")+"");
				convenio.setNumeroDiasVencimiento(rs.getString("numdiasvencimiento")==null?"":rs.getString("numdiasvencimiento"));
				convenio.setRequiereJustificacionServicios(rs.getString("requierejustificacionservicios"));
				convenio.setRequiereJustificacionArticulos(rs.getString("requierejustificacionarticulos"));
				convenio.setRequiereJustArtNoposDifMed(rs.getString("requierejustartnoposdifmed"));
				convenio.setManejaComplejidad(rs.getString("manejacomplejidad"));
				convenio.setSemanasMinimasCotizacion(rs.getString("semanasmincotizacion")==null?"":rs.getString("semanasmincotizacion"));
				convenio.setRequiereNumeroCarnet(rs.getString("requierenumcarnet"));
				convenio.setTipoConvenio(rs.getString("tipoconvenio"));
				convenio.setTipoCodigo(rs.getInt("tipocodigo"));
				convenio.setTipoCodigoArt(rs.getInt("tipocodigoart"));
				convenio.setAjusteServicios(rs.getString("ajusteservicios")==null?"":rs.getString("ajusteservicios"));
				convenio.setAjusteArticulos(rs.getString("ajustearticulos")==null?"":rs.getString("ajustearticulos"));
				convenio.setInterfaz(rs.getString("interfaz")==null?"":rs.getString("interfaz"));
				convenio.setClasificacionTipoConvenio(rs.getInt("clasificaciontipoconvenio"));
				convenio.setRequiereDeudor ( UtilidadTexto.getBoolean(rs.getString("requiereDeudor")));
				convenio.setRequiereVerificacionDerechos ( UtilidadTexto.getBoolean(rs	.getString("requiereVerificacionDerechos")));
				
				convenio.setCantidadMaxCirugia ( rs.getInt("cantidadmaxcirugia")>0?rs.getInt("cantidadmaxcirugia")+"":"");				
				convenio.setCantidadMaxAyudpag ( rs.getInt("cantidadmaxayudpag")>0?rs.getInt("cantidadmaxayudpag")+"":"");				
				convenio.setTipoLiquidacionScx ( rs.getString("tipoliquidacionscx"));				
				convenio.setTipoLiquidacionDyt ( rs.getString("tipoliquidaciondyt"));				
				convenio.setTipoLiquidacionGcx ( rs.getString("tipoliquidaciongcx"));				
				convenio.setTipoLiquidacionGdyt ( rs.getString("tipoliquidaciongdyt"));				
				convenio.setTipoTarifaLiqMateCx ( rs.getString("tipotarifaliqmatecx"));				
				convenio.setTipoTarifaLiqMateDyt ( rs.getString("tipotarifaliqmatedyt"));				
				convenio.setTipoFechaLiqTiemPcx ( rs.getString("tipofechaliqtiempcx"));				
				convenio.setTipoFechaLiqTiempDyt ( rs.getString("tipofechaliqtiempdyt"));				
				convenio.setLiquidacionTmpFracAdd ( rs.getString("liquidaciontmpfracadd"));
				
				convenio.setEncabezadoFactura(rs.getString("encabezadofactura")==null?"":rs.getString("encabezadofactura"));
				convenio.setPieFactura(rs.getString("piefactura")==null?"":rs.getString("piefactura"));
				convenio.setEmpresaInstitucion ( rs.getString("empresainstitucion"));
				convenio.setNumeroIdentificacion ( rs.getString("numeroIdentificacion"));
				convenio.setPlanEspecial ( Utilidades.convertirAEntero(rs.getObject("planEspecial")+""));
				convenio.setPlanEspecialList ( convenio.cargarPlanEspecial(con, rs.getString("institucion")));
				convenio.setExcentoDeudor ( rs.getString("excentoDeudor"));
				convenio.setExcentoDocumentoGarantia ( rs.getString("excentoDocumentoGarantia"));
				convenio.setVip ( rs.getString("vip"));
				convenio.setRadicarCuentasNegativas( rs.getString("radicarcuentasnegativas"));
				convenio.setAsignarFactValorPacValorAbono ( rs.getString("asignarfactvalorpacvalorabono"));
				convenio.setDiasCPO(rs.getInt("diascpo")+"");
				convenio.setCantCPO(rs.getInt("cantcpo")+"");
				convenio.setAseguradora (rs.getString("aseguradora")==null?"":rs.getString("aseguradora"));
				convenio.setCodigoAseguradora (rs.getString("codaseguradora")==null?"":rs.getString("codaseguradora"));
				convenio.setValorLetrasFactura (rs.getString("valorletras")==null?"":rs.getString("valorletras"));
				convenio.setReporte_incons_bd( rs.getString("report_incons_bd"));
				convenio.setReporte_atencion_ini_urg(rs.getString("report_aten_ini"));
				convenio.setGeneracion_autom_val_urg(rs.getString("gener_auto_aten_ini_urg"));
				convenio.setRequiere_autorizacion_servicio(rs.getString("req_autorizacion_servic"));
				convenio.setFormato_autorizacion(rs.getString("formato_autorizacion"));
			    convenio.setCorreosElectronicos(convenio.consultarCorreosElectronicos(con));
			    
//			    ********* anexo 791 ********
			    convenio.setManejaMultasPorIncumplimiento(rs.getString("man_mul_inc_citas")==null?"":rs.getString("man_mul_inc_citas"));
			    convenio.setValorMultaPorIncumplimientoCitas(rs.getObject("val_mul_inc_citas")+"");
//			    ****************************
			    
//			    ******** Cambios Anexo 809 ***********
			    convenio.setCcContableList( convenio.consultarCcContable(con, rs.getObject("institucion")+""));
			    convenio.setCcContable( Utilidades.convertirAEntero(rs.getObject("ccContable")+""));
//			    **************************************
			    convenio.setManejaBonos(rs.getString("maneja_bonos")==null?"":rs.getString("maneja_bonos"));
			    convenio.setRequiereBono(rs.getString("req_bono_ing_pac")==null?"":rs.getString("req_bono_ing_pac"));
			    convenio.setManejaPromociones(rs.getString("maneja_promociones")==null?"":rs.getString("maneja_promociones"));
			    convenio.setEsTargetaCliene(rs.getString("es_conv_tar_cli")==null?"":rs.getString("es_conv_tar_cli"));
			    convenio.setIngresoBdValido(rs.getString("ing_pac_val_bd")==null?"":rs.getString("ing_pac_val_bd"));
			    convenio.setIngresoPacienteReqAutorizacion(rs.getString("ing_pac_req_aut")==null?"":rs.getString("ing_pac_req_aut"));
			    convenio.setReqIngresoValido(rs.getString("req_ing_val_auto")==null?"":rs.getString("req_ing_val_auto"));
			    convenio.setTipoAtencion( rs.getString("tipo_atencion"));
			    convenio.setManejaDescuentoOdontologico(rs.getString("maneja_des_odo")==null?"":rs.getString("maneja_des_odo"));
			    convenio.setTipoLiquidacionPool(rs.getString("tipo_liquidacion_pool"));//tarea 19584
			    convenio.setConvenioManejaMontoCobro(UtilidadTexto.getBoolean(rs.getString("maneja_montos")));
			    convenio.setCapitacionSubcontratada(rs.getString("capitacion_subcontratada"));
			    convenio.setManejaPresupCapitacion(rs.getString("maneja_presup_capitacion"));
			    convenio.setCheckInfoAdicCuenta(rs.getString("aseguradora_accidente_transito"));
			    
				band1 = true;
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
		Log4JManager.info("############## Fin cargarResumen");
		return band1;
	}
	
	/**Carga el �ltimo convenio insertado**/
	public static ResultSetDecorator cargarUltimoCodigo(Connection con)
	{
		try
		{
			PreparedStatementDecorator cargarUltimoStatement= new PreparedStatementDecorator(con.prepareStatement(cargarCodigoUltimaInsercion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return new ResultSetDecorator(cargarUltimoStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta del �ltimo c�digo del convenio: SqlBaseConvenioDao "+e.toString());
			return null;
		}
	}
	
	/**
	 * Modifica un convenio dado su c�digo con los param�tros dados.
	 * @param convenioManejaMontoCobro 
	 * @param unificarPyp 
	 * @param tipoConvenio 
	 * @param requiereCarnet 
	 * @param semanasMinimasCotizacion 
	 * @param manejaComplejidad 
	 * @param requiereJustificacion 
	 * @param numerDiasVencimiento 
	 * @param empresaInstitucion 
	 * @param ccContable 
	 * @param cenAtencContable 
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param codigo, int, codigo del convenio
	 * @param empresa, int, c�digo de la empresa (tabla empresas - estado activo)
	 *  @param tipoRegimen, String, r�gimen de acuerdo a los previam/t
	 * 				ingresados en el sistema
	 * @param nombre, String, nombre del convenio
	 * @param observaciones, String, observaciones del convenio 
	 * @param planBeneficios, String, descripci�n del plan de beneficios
	 * @param codigoMinSalud, String, codigo Minsalud
	 * @param formatoFactura, int, selecciona el tipo de formato de factura 
	 * 				que utiliza el convenio
	 * @param activo, boolean, si el convenio est� activo en el sistema o no
	 * 
	 * @param Cantidad max cirugia adicionales
	 * @param Cantidad Max ayudantes que paga
	 * @param Tipo de Liquidacion Salas Cirugia
	 * @param Tipo de Liquidacion Salas No Cruentos
	 * @param Tipo de Liquidacion General Cirugias
	 * @param Tipo de Liquidacion General No Cruetos
	 * @param Tipo de Tarifa para liquidacion materiales Cirugia
	 * @param Tipo de Tarifa para liquidacion materiales No Cruentos
	 * @param Tipo de Fecha liquidacion Tiempos Cirugia
	 * @param Tipo de Fecha para Liquidacion Tiempos No Cruentos
	 * @param Liquidacion de Tiempos x Fraccion Adicional Cumplida
	 * @return int, 0 si  no inserta, 1 si inserta
	 */
	public static int modificar(	Connection con,UsuarioBasico usuario, 
									int codigo, 
									int empresa,
									String tipoRegimen,
									String nombre, 
									String  observaciones, 
									String planBeneficios,  
									String codigoMinSalud,	
									int formatoFactura,
									boolean activa,
									boolean convenioManejaMontoCobro, int tipoContrato,
									String pyp,
									String unificarPyp,
									String numerDiasVencimiento,
									String requiereJustificacionServicios,
									String requiereJustificacionArticulos,
									String requiereJustArtNoposDifMed,
									String manejaComplejidad,
									String semanasMinimasCotizacion,
									String requiereCarnet,
									String tipoConvenio,
									int tipoCodigo,
									int tipoCodigoArt,
									String ajusteServicios,
									String ajusteArticulos,
									String interfaz,
									
									int cantidadMaxCirugia,
									int cantidadMaxAyudpag,
									String tipoLiquidacionScx,
									String tipoLiquidacionDyt,
									String tipoLiquidacionGcx,
									String tipoLiquidacionGdyt,
									String tipoTarifaLiqMateCx,
									String tipoTarifaLiqMateDyt,
									String tipoFechaLiqTiemPcx,
									String tipoFechaLiqTiempDyt,
									String liquidacionTmpFracAdd,
									
									String encabezadoFactura,
									String pieFactura, 
									String empresaInstitucion,
									int planEspecial, 
									String excentoDeudor, 
									String excentoDocumentoGarantia, 
									String vip,
									String radicarCuentasNegativas,
									String asignarFactValorPacValorAbono,
									int cantCPO,
									int diasCPO,
									String aseguradora,
		        					String codigoAseguradora,
		        					String valorLetras,
		        				//*************Cambios Anexo 753 Decreto 4747*****************************
									String repInconsistBD,
		    						String repAtencIniUrg,
		    						String generAutoValAteniniUrg,
		        					String[] mediosEnvio,
		        					HashMap correos,
		        					String requiereAutorizacionServicio,
		        					String formatoAutorizacion,
		        				//**************************************************************************
		        					String insertarCorreoElectronicoStr,
//		        					************ Anexo 791 ************
		        					String manejaMultasPorIncumplimiento,
		        					String valorMultaPorIncumplimientoCitas,
//		        					***********************************
//		        					******* anexo 809 *******
		        					int ccContable ,
		        					int cenAtencContable, 
		        					String manejaBonos,
									String requiereBono,
									String manejaPromociones,
									String esTargetaCliene,
									String ingresoBdValido,
									String ingresoPacienteReqAutorizacion,
									String reqIngresoValido,
									String tipoAtencion,
									String manejaDescuentoOdontologico,
									String capitacionSubcontratada,
									String manejaPresupCapitacion
									) throws BDException
	{
		int resp=0;	
		try
		{
			if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexi�n cerrada");
			}

			logger.info("ENTRO A MODIFICAR");
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarConvenio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			ps.setInt(1,empresa);
			ps.setString(2, tipoRegimen);
			ps.setString(3,nombre);
			ps.setString(4,observaciones);
			ps.setString(5,planBeneficios);
			ps.setString(6,codigoMinSalud);
			if(formatoFactura==0)
			{
				ps.setObject(7,null);
			}
			else
			{
				ps.setInt(7,formatoFactura);
			}
			ps.setBoolean(8,activa);
			ps.setInt(9, tipoContrato);

			if(UtilidadTexto.getBoolean(pyp))
				ps.setBoolean(10, true);
			else 
				ps.setBoolean(10, false);

			if(unificarPyp.equals("true"))
				ps.setBoolean(11, true);
			else 
				ps.setBoolean(11, false);

			if(UtilidadTexto.isEmpty(numerDiasVencimiento))
				ps.setObject(12, null);
			else
				ps.setInt(12, Integer.parseInt(numerDiasVencimiento));
			if(UtilidadTexto.isEmpty(requiereJustificacionServicios))
				ps.setString(13, ConstantesBD.acronimoNo);
			else
				ps.setString(13, requiereJustificacionServicios);
			if(UtilidadTexto.isEmpty(requiereJustificacionArticulos))
				ps.setString(14, ConstantesBD.acronimoNo);
			else
				ps.setString(14, requiereJustificacionArticulos);
			if(UtilidadTexto.isEmpty(manejaComplejidad))
				ps.setString(15, ConstantesBD.acronimoNo);
			else
				ps.setString(15, manejaComplejidad);
			if(UtilidadTexto.isEmpty(semanasMinimasCotizacion))
				ps.setObject(16, null);
			else
				ps.setInt(16, Integer.parseInt(semanasMinimasCotizacion));
			if(UtilidadTexto.isEmpty(requiereCarnet))
				ps.setString(17, ConstantesBD.acronimoNo);
			else
				ps.setString(17, requiereCarnet);
			if(Utilidades.convertirAEntero(tipoConvenio)>0)
				ps.setString(18, tipoConvenio);
			else
				ps.setObject(18, null);
			if(tipoCodigo>0)
				ps.setInt(19, tipoCodigo);
			else
				ps.setObject(19, null);
			if(tipoCodigoArt>=0)               
				ps.setInt(69, tipoCodigoArt); 
            else                           
				ps.setObject(69, null);    
			
			if(ajusteServicios==null || ajusteServicios.trim().equals(""))
				ps.setObject(20, null);
			else
				ps.setString(20,ajusteServicios);

			if(ajusteArticulos==null || ajusteArticulos.trim().equals(""))
				ps.setObject(21, null);
			else
				ps.setString(21,ajusteArticulos);

			/*if(UtilidadTexto.isEmpty(ajusteServicios))
					ps.setString(20, ConstantesBD.acronimoNo);
				else
					ps.setString(20,ajusteServicios);
				if(UtilidadTexto.isEmpty(ajusteArticulos))
					ps.setString(21, ConstantesBD.acronimoNo);
				else
					ps.setString(21,ajusteArticulos);*/
			if(interfaz==null || interfaz.trim().equals(""))
				ps.setObject(22, null);
			else
				ps.setString(22,interfaz);


			//----
			if(cantidadMaxCirugia >= 0)
				ps.setObject(23,cantidadMaxCirugia);
			else
				ps.setNull(23,Types.NULL);

			if(cantidadMaxAyudpag >= 0)
				ps.setObject(24,cantidadMaxAyudpag);
			else
				ps.setNull(24,Types.NULL);

			if(!tipoLiquidacionScx.equals(""))
				ps.setObject(25,tipoLiquidacionScx);
			else
				ps.setNull(25,Types.NULL);

			if(!tipoLiquidacionDyt.equals(""))
				ps.setObject(26,tipoLiquidacionDyt);
			else
				ps.setNull(26,Types.NULL);

			if(!tipoLiquidacionGcx.equals(""))
				ps.setObject(27,tipoLiquidacionGcx);
			else
				ps.setNull(27,Types.NULL);

			if(!tipoLiquidacionGdyt.equals(""))
				ps.setObject(28,tipoLiquidacionGdyt);
			else
				ps.setNull(28,Types.NULL);

			if(!tipoTarifaLiqMateCx.equals(""))
				ps.setObject(29,tipoTarifaLiqMateCx);
			else
				ps.setNull(29,Types.NULL);

			if(!tipoTarifaLiqMateDyt.equals(""))
				ps.setObject(30,tipoTarifaLiqMateDyt);
			else
				ps.setNull(30,Types.NULL);

			if(!tipoFechaLiqTiemPcx.equals(""))
				ps.setObject(31,tipoFechaLiqTiemPcx);
			else
				ps.setNull(31,Types.NULL);

			if(!tipoFechaLiqTiempDyt.equals(""))
				ps.setObject(32,tipoFechaLiqTiempDyt);
			else
				ps.setNull(32,Types.NULL);

			if(!liquidacionTmpFracAdd.equals(""))
				ps.setObject(33,liquidacionTmpFracAdd);
			else
				ps.setNull(33,Types.NULL);
			//---
			
			if(UtilidadTexto.isEmpty(encabezadoFactura))
				ps.setString(34, null);
			else
				ps.setString(34, encabezadoFactura);
			
			if(UtilidadTexto.isEmpty(pieFactura))
				ps.setString(35, null);
			else
				ps.setString(35, pieFactura);
			
			if(Utilidades.convertirAEntero(empresaInstitucion,true)<=0)
				ps.setObject(36, null);
			else
				ps.setObject(36, empresaInstitucion);
			
			if(UtilidadTexto.isEmpty(requiereJustArtNoposDifMed))
				ps.setString(37, ConstantesBD.acronimoNo);
			else
				ps.setString(37, requiereJustArtNoposDifMed);

			logger.info("valor sql >> "+planEspecial);
			if(planEspecial==ConstantesBD.codigoNuncaValido)
			{
				ps.setNull(38, Types.NUMERIC);
				logger.info("entro a null");
			}
				
			else
			{
				logger.info("entro planespecial >>> "+planEspecial);
				ps.setInt(38, planEspecial);
			}
			
			
			ps.setString(39, excentoDeudor);
			ps.setString(40, excentoDocumentoGarantia);
			ps.setString(41, vip);
			ps.setInt(43, diasCPO);
			ps.setInt(42, cantCPO);
			
			ps.setString(44,radicarCuentasNegativas);
			ps.setString(45,asignarFactValorPacValorAbono);
			
			//Inluido por el Anexo 722
			ps.setString(46, aseguradora);
			if(UtilidadCadena.noEsVacio(codigoAseguradora))
				ps.setString(47, codigoAseguradora);
			else
				ps.setNull(47, Types.VARCHAR);
			
			if(!valorLetras.equals(""))
				ps.setString(48, valorLetras);
			else
				ps.setNull(48, Types.VARCHAR);
			
			//*************Cambios Anexo 753 Decreto 4747*****************************	
			if(!repInconsistBD.equals(""))
				ps.setString(49, repInconsistBD);
			else
				ps.setString(49, ConstantesBD.acronimoNo);
			
			if(!repAtencIniUrg.equals(""))
				ps.setString(50,repAtencIniUrg);
			else
				ps.setString(50, ConstantesBD.acronimoNo);
			
			if(!generAutoValAteniniUrg.equals(""))
				ps.setString(51,generAutoValAteniniUrg);
			else
				ps.setString(51, ConstantesBD.acronimoNo);
			
			if(!requiereAutorizacionServicio.equals(""))
				ps.setString(52,requiereAutorizacionServicio);
			else
				ps.setString(52, ConstantesBD.acronimoNo);
			
			if(!formatoAutorizacion.equals(""))
				ps.setString(53,formatoAutorizacion);
			else
				ps.setNull(53, Types.VARCHAR);
			
			
	    //***************************************************************************
			
			
			
//			******** anexo 791 *********
			if(!manejaMultasPorIncumplimiento.equals("")){
				logger.info("entro manejaMultasPorIncumplimiento::::::::::::"+manejaMultasPorIncumplimiento);
				ps.setString(54, manejaMultasPorIncumplimiento);
			}
			else{
				logger.info("No entro manejaMultasPorIncumplimiento::::::::::::null");
				ps.setNull(54, Types.VARCHAR);
			}
			
			
			if(!valorMultaPorIncumplimientoCitas.equals("")){
				logger.info("entro valorMultaPorIncumplimientoCitas::::::::::::"+valorMultaPorIncumplimientoCitas);
				ps.setInt(55, Utilidades.convertirAEntero(valorMultaPorIncumplimientoCitas));
			}
			else{
				logger.info("No entro valorMultaPorIncumplimientoCitas::::::::::::null");
				ps.setNull(55, Types.NUMERIC);
			}
//			****************************
//			******* anexo 809 *******
			if(ccContable > 0){
				logger.info("entro ccContable::::::::::::"+ccContable);
				ps.setInt(56, ccContable);
			}
			else{
				logger.info("No entro ccContable::::::::::::null");
				ps.setNull(56, Types.NUMERIC);
			}
//			*************************
			if(!UtilidadTexto.isEmpty(manejaBonos))
				ps.setString(57, manejaBonos);
			else
				ps.setNull(57, Types.VARCHAR);
			
			if(!UtilidadTexto.isEmpty(requiereBono))
				ps.setString(58,requiereBono);
			else
				ps.setNull(58, Types.VARCHAR);
			
			if(!UtilidadTexto.isEmpty(manejaPromociones))
				ps.setString(59,manejaPromociones);
			else
				ps.setNull(59, Types.VARCHAR);
			
			if(!UtilidadTexto.isEmpty(esTargetaCliene))
				ps.setString(60, esTargetaCliene);
			else
				ps.setNull(60, Types.VARCHAR);
			
			if(!UtilidadTexto.isEmpty(ingresoBdValido))
				ps.setString(61, ingresoBdValido);
			else
				ps.setNull(61, Types.VARCHAR);
			
			if(!UtilidadTexto.isEmpty(ingresoPacienteReqAutorizacion))
				ps.setString(62, ingresoPacienteReqAutorizacion);
			else
				ps.setNull(62, Types.VARCHAR);
			
			if(!UtilidadTexto.isEmpty(reqIngresoValido))
				ps.setString(63, reqIngresoValido);
			else
				ps.setNull(63, Types.VARCHAR);
			
			
				ps.setString(64, tipoAtencion);
			
			
				if(!UtilidadTexto.isEmpty(manejaDescuentoOdontologico))
					ps.setString(65, manejaDescuentoOdontologico);
				else
					ps.setNull(65, Types.VARCHAR);
				

			ps.setString(66, convenioManejaMontoCobro?"S":"N");
				
			if(!UtilidadTexto.isEmpty(capitacionSubcontratada))
				ps.setString(67, capitacionSubcontratada);
			else
				ps.setNull(67, Types.VARCHAR);
			
			if(!UtilidadTexto.isEmpty(manejaPresupCapitacion))
				ps.setString(68, manejaPresupCapitacion);
			else
				ps.setNull(68, Types.VARCHAR);
		
			
			ps.setInt(70,codigo);
			

			ps.executeUpdate();
			modificarMediosEnvio(con,usuario,codigo,mediosEnvio);
			modificarCorreosElectronicos(con, usuario, codigo, correos, insertarCorreoElectronicoStr);

			
			logger.info("requiereJustArtNoposDifMed ---> "+requiereJustArtNoposDifMed);

			int cenAtContableBD=cargarCentroAtencionContable(con, codigo);
			
			if(cenAtencContable>0)
			{
				if(cenAtContableBD<1)
				{
					insertatCentroAtencionContable(con, codigo, cenAtencContable);
				}
				else
				{
					modificarCentroAtencionContable(con, codigo, cenAtencContable);
				}
			}
			else
			{
				if(cenAtContableBD>0)
				{
					eliminarCentroAtencionContable(con, codigo);
				}
			}
				
		}
		catch(SQLException sqe)
		{
			Log4JManager.error(sqe+" Error en la inserci�n de datos: SqlBaseConvenioDao "+sqe.toString()+" CONSULTA >"+modificarConvenio);
			resp=0;			
		}	
		return resp;	
	}
	

	
	/**
	 * M�todo que contiene el Resulset de los datos de la tabla convenios
	 * para posteriorm/t ser mostrados en el pager
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @return Resultset con todos los datos de la tabla convenios
	 * @throws SQLException
	 */
	public static  ResultSetDecorator listado(Connection con, int codigoInstitucion,String tipoAtencion, boolean ambos) throws SQLException
	{
		ResultSetDecorator respuesta=null;
		String consultaStr=consultarConvenios;
		if(con==null || con.isClosed())
		{
			try
			{
				DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
			catch(SQLException e)
			{
				logger.warn("No se pudo realizar la conexi�n "+e.toString());
				respuesta= null;
			}
		}
		try
		{
			
			
			if(!ambos){
				consultaStr+=" and c.tipo_atencion= '"+tipoAtencion+"' "; 
				
			}
			
			consultaStr+="ORDER BY  c.nombre ";
			
			logger.info(consultarConvenios);
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoInstitucion);
			respuesta=new ResultSetDecorator(ps.executeQuery());				
		}
		catch(SQLException e)
		{
			logger.warn("Error en el listado convenios " +e.toString());
			respuesta=null;
		}
		return respuesta;
	}
	
	/**
	 * M�todo que contiene el Resulset de todas los convenios buscados
	 * @param unificarPyp 
	 * @param empresaInstitucion 
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @return Resultset con todos los datos de la tabla empresas
	 * @throws SQLException
	 */
	public static  ResultSetDecorator busqueda(		Connection con,
											int codigoConvenio,
											String  razonSocial,
											String nombreTipoRegimen,
											String nombre,
											String observaciones,
											String planBeneficios,
											String codigoMinSalud,
											String nombreFormatoFactura,
											int activaAux,
											String nombreTipoContrato,
											int codigoInstitucion,
											int pypAux,
											String unificarPyp,
											String interfaz,
											
											int cantidadMaxCirugia,
											int cantidadMaxAyudpag,
											String tipoLiquidacionScx,
											String tipoLiquidacionDyt,
											String tipoLiquidacionGcx,
											String tipoLiquidacionGdyt,
											String tipoTarifaLiqMateCx,
											String tipoTarifaLiqMateDyt,
											String tipoFechaLiqTiemPcx,
											String tipoFechaLiqTiempDyt,
											String liquidacionTmpFracAdd, 
											String empresaInstitucion, 
											int planEspecial,
											String radicarCuentasNegativas,
											String asignarFactValorPacValorAbono,
											String manejaBonos,
											String requiereBono,
											String manejaPromociones,
											String esTargetaCliene,
											String ingresoBdValido,
											String ingresoPacienteReqAutorizacion,
											String reqIngresoValido,
											String tipoAtencion,
											String tipoLiquidacionPool) throws SQLException
	{
		ResultSetDecorator respuesta=null;
		String consultaArmada="";
		if(con==null || con.isClosed())
		{
			try
			{
				DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
			catch(SQLException e)
			{
				logger.warn("No se pudo realizar la conexi�n "+e.toString());
				respuesta= null;
			}
		}
		try
		{
			consultaArmada=armarConsulta(		codigoConvenio, 
												razonSocial,
												nombreTipoRegimen,
												nombre,
												observaciones,
    											planBeneficios,
    											codigoMinSalud,
    											nombreFormatoFactura,
    											activaAux,
    											nombreTipoContrato,
    											codigoInstitucion, 
    											pypAux, 
    											unificarPyp, 
    											interfaz,
    											cantidadMaxCirugia,
    											cantidadMaxAyudpag,
    											tipoLiquidacionScx,
    											tipoLiquidacionDyt,
    											tipoLiquidacionGcx,
    											tipoLiquidacionGdyt,
    											tipoTarifaLiqMateCx,
    											tipoTarifaLiqMateDyt,
    											tipoFechaLiqTiemPcx,
    											tipoFechaLiqTiempDyt,
    											liquidacionTmpFracAdd,
    											empresaInstitucion, 
    											planEspecial,
    											radicarCuentasNegativas,
    											asignarFactValorPacValorAbono,
    											manejaBonos,
    											requiereBono,
    											manejaPromociones,
    											esTargetaCliene,
    											ingresoBdValido,
    											ingresoPacienteReqAutorizacion,
    											reqIngresoValido,
    											tipoAtencion, 
    											tipoLiquidacionPool);
			logger.info(consultaArmada);
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consultaArmada,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			respuesta=new ResultSetDecorator(ps.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn("Error en la b�squeda avanzada del convenio " +e.toString());
			respuesta=null;
		}
		return respuesta;
	}

	/**
	 * M�todo que arma la consulta seg�n los datos dados por el usuarios en 
	 * la b�squeda avanzada. 
	 * @param unificarPyp 
	 * @param empresaInstitucion 
	 */
	private static String armarConsulta  (		int codigoConvenio,
												String  razonSocial,
												String nombreTipoRegimen,
												String nombre,
												String observaciones,
												String planBeneficios,
												String codigoMinSalud,
												String nombreFormatoFactura,
												int activaAux,
												String nombreTipoContrato,
												int codigoInstitucion,
												int pypAux, String unificarPyp,
												String interfaz,
												int cantidadMaxCirugia,
												int cantidadMaxAyudpag,
												String tipoLiquidacionScx,
												String tipoLiquidacionDyt,
												String tipoLiquidacionGcx,
												String tipoLiquidacionGdyt,
												String tipoTarifaLiqMateCx,
												String tipoTarifaLiqMateDyt,
												String tipoFechaLiqTiemPcx,
												String tipoFechaLiqTiempDyt,
												String liquidacionTmpFracAdd, 
												String empresaInstitucion, 
												int planEspecial,
												String radicarCuentasNegativas,
												String asignarFactValorPacValorAbono,
												String manejaBonos,
												String requiereBono,
												String manejaPromociones,
												String esTargetaCliene,
												String ingresoBdValido,
												String ingresoPacienteReqAutorizacion,
												String reqIngresoValido,
												String tipoAtencion,
												String tipoLiquidacionPool)
	{
		String consulta= "SELECT c.codigo, e.razon_social, t.nombre AS tipo_regimen1,  " +
									"c.nombre, c.observaciones, c.plan_beneficios,c.codigo_min_salud, " +
									"CASE WHEN c.formato_factura IS NULL THEN 'Est�ndar' ELSE f.nombre_formato END AS formato_factura1, c.activo, c.tipo_contrato AS codigoTipoContrato," +
									"ti.nombre AS nombreTipoContrato, c.pyp,  c.unificar_pyp," +
									"coalesce(c.num_dias_vencimiento||'','') as numdiasvencimiento," +
									"c.requiere_justificacion_serv as requierejustificacionservicios ," +
									"c.interfaz as interfaz ," +
									"c.requiere_justificacion_art as requierejustificacionarticulos ," +
									"c.req_just_art_nopos_dif_med as requierejustartnoposdifmed ," +
									"c.maneja_complejidad as manejacomplejidad ," +
									"coalesce(c.semanas_min_cotizacion||'','') as semanasmincotizacion ," +
									"c.requiere_ingreso_num_carnet as requierenumcarnet ," +
									"coalesce(c.tipo_convenio||'','-1')  as tipoconvenio," +
									// la linea estaba de esta manera pero presentaba error se cambio el plural del campo empresas por empresa (singular)
									// "CASE WHEN c.empresas_institucion IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE c.empresa_institucion END AS empresas_institucion, "+
									"CASE WHEN c.empresa_institucion IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE c.empresa_institucion END AS empresas_institucion, "+
									"CASE WHEN c.cantidad_max_cirugia IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE c.cantidad_max_cirugia END AS cantidadmaxcirugia," +
									"CASE WHEN c.cantidad_max_ayudpag IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE c.cantidad_max_ayudpag END AS cantidadmaxayudpag," +
									"CASE WHEN c.tipo_liquidacion_scx IS NULL THEN '' ELSE c.tipo_liquidacion_scx END  AS tipoliquidacionscx," +
									"CASE WHEN c.tipo_liquidacion_dyt IS NULL THEN '' ELSE c.tipo_liquidacion_dyt END  AS tipoliquidaciondyt," +
									"CASE WHEN c.tipo_liquidacion_gcx IS NULL THEN '' ELSE c.tipo_liquidacion_gcx END AS tipoliquidaciongcx," +
									"CASE WHEN c.tipo_liquidacion_gdyt IS NULL THEN '' ELSE c.tipo_liquidacion_gdyt END AS tipoliquidaciongdyt," +
									"CASE WHEN c.tipo_tarifa_liqmatecx IS NULL THEN '' ELSE c.tipo_tarifa_liqmatecx END AS tipotarifaliqmatecx," +
									"CASE WHEN c.tipo_tarifa_liqmatedyt IS NULL THEN '' ELSE c.tipo_tarifa_liqmatedyt END AS tipotarifaliqmatedyt," +
									"CASE WHEN c.tipo_fecha_liqtiempcx IS NULL THEN '' ELSE c.tipo_fecha_liqtiempcx END AS tipofechaliqtiempcx," +
									"CASE WHEN c.tipo_fecha_liqtiempdyt IS NULL THEN '' ELSE c.tipo_fecha_liqtiempdyt END AS tipofechaliqtiempdyt," +
									"CASE WHEN c.empresa_institucion IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE c.empresa_institucion END AS empresainstitucion, "+																					
									"CASE WHEN c.liquidacion_tmp_frac_add IS NULL THEN '' ELSE c.liquidacion_tmp_frac_add END AS liquidaciontmpfracadd, " +
									"c.centro_costo_plan_especial as planEspecial, c.institucion as institucion, " +
									"c.aseguradora AS aseguradora, " +
									"coalesce(c.cod_aseguradora,'') AS cod_aseguradora, " +
//									********* anexo 791 *********
									"coalesce(c.man_mul_inc_citas||'','')  as man_mul_inc_citas," +
									"coalesce(c.val_mul_inc_citas||'','')  as val_mul_inc_citas," +
//									*****************************
									
								    "c.maneja_bonos AS maneja_bonos, "+
								    "c.req_bono_ing_pac AS req_bono_ing_pac, "+
								    "c.maneja_promociones AS maneja_promociones, "+
								    "c.es_conv_tar_cli AS es_conv_tar_cli, "+
								    "c.ing_pac_val_bd AS ing_pac_val_bd, "+
								    "c.ing_pac_req_aut AS ing_pac_req_aut, "+
								    "c.req_ing_val_auto AS req_ing_val_auto, "+
								    "c.tipo_atencion AS tipo_atencion  "+
									
									"FROM convenios c " +
									"INNER JOIN empresas e ON (c.empresa=e.codigo ";
									
		if(razonSocial != null && !razonSocial.equals(""))
		{
			consulta = consulta + "AND UPPER(e.razon_social) LIKE  UPPER('%"+razonSocial+"%')  ";
		}
		
		if(nombre != null && !nombre.equals(""))
		{
			consulta = consulta + " AND UPPER(c.nombre) LIKE  UPPER('%"+nombre+"%') ";
		}
		
		if(observaciones != null && !observaciones.equals(""))
		{
			consulta = consulta + " AND UPPER(c.observaciones) LIKE  UPPER('%"+observaciones+"%') ";
		}
		
		if(planBeneficios != null && !planBeneficios.equals(""))
		{
			consulta = consulta + " AND UPPER(c.plan_beneficios) LIKE  UPPER('%"+planBeneficios+"%') ";
		}
		
		if(codigoMinSalud != null && !codigoMinSalud.equals(""))
		{
			consulta = consulta + " AND UPPER(c.codigo_min_salud) LIKE UPPER('%"+codigoMinSalud+"%') ";
		}
		
		if(activaAux==1)
		{
			consulta = consulta + "AND c.activo= '"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' "	;	
		}
		
		if(activaAux==2)
		{
			consulta = consulta + "AND c.activo= '"+ValoresPorDefecto.getValorFalseCortoParaConsultas()+"' "	;	
		}
		
		if(pypAux==1)
		{
			consulta = consulta + "AND c.pyp= '"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' "	;	
		}
		
		if(Utilidades.convertirAEntero(empresaInstitucion,true)>0)
		{
			consulta = consulta + "AND c.empresa_institucion= "+empresaInstitucion;
		}
		
		if(pypAux==2)
		{
			consulta = consulta + "AND c.pyp= '"+ValoresPorDefecto.getValorFalseCortoParaConsultas()+"' "	;	
		}
		
		if(unificarPyp.equals("1"))
		{
			consulta = consulta + "AND c.unificar_pyp = '"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' "	;	
		}
		
		if(interfaz != null && !interfaz.equals(""))
		{
			consulta = consulta + " AND UPPER(c.interfaz) LIKE  UPPER('%"+interfaz+"%') ";
		}
		
		if(unificarPyp.equals("2"))
		{
			consulta = consulta + "AND c.unificar_pyp = '"+ValoresPorDefecto.getValorFalseCortoParaConsultas()+"' "	;	
		}
		
		
		consulta = consulta + ") INNER JOIN tipos_regimen t ON ( c.tipo_regimen=t.acronimo ";
		
		if(nombreTipoRegimen != null && !nombreTipoRegimen.equals(""))
		{
			consulta= consulta + "AND UPPER(t.nombre) LIKE UPPER('%" +nombreTipoRegimen+ "%')";
		}
		
		consulta= consulta + ") LEFT OUTER JOIN formato_impresion_factura f ON (c.formato_factura=f.codigo ";
		 
		if(nombreFormatoFactura != null && !nombreFormatoFactura.equals(""))
		{
			if("ESTANDAR".contains(nombreFormatoFactura.toUpperCase()))
			{
				consulta= consulta + "AND  c.formato_factura is NULL";
			}
			else
			{
				consulta= consulta + "AND UPPER(f.nombre_formato) LIKE UPPER('%" +nombreFormatoFactura+ "%')";
			}
		} 
		
		consulta= consulta + ") INNER JOIN tipos_contrato ti ON(c.tipo_contrato=ti.codigo ";
		
		if(nombreTipoContrato!=null && !nombreTipoContrato.equals(""))
		{
			consulta= consulta + "AND UPPER(ti.nombre) LIKE UPPER('%"+nombreTipoContrato+"%') ";
		}
		
		consulta= consulta + ") INNER JOIN terceros ter ON (ter.codigo=e.tercero) WHERE ter.institucion = "+codigoInstitucion+" " ;
		
		
		if(cantidadMaxCirugia!=ConstantesBD.codigoNuncaValido)
			consulta = consulta + "AND cantidad_max_cirugia = "+cantidadMaxCirugia+" ";
		
		if(cantidadMaxAyudpag!=ConstantesBD.codigoNuncaValido)
			consulta = consulta + "AND cantidad_max_ayudpag = "+cantidadMaxAyudpag+" ";
		
		if(tipoLiquidacionScx!=null && !tipoLiquidacionScx.equals(""))
			consulta = consulta + "AND tipo_liquidacion_scx = '"+tipoLiquidacionScx+"' ";
		
		if(tipoLiquidacionDyt!=null && !tipoLiquidacionDyt.equals(""))
			consulta = consulta + "AND tipo_liquidacion_dyt = '"+tipoLiquidacionDyt+"' ";
		
		if(tipoLiquidacionGcx!=null && !tipoLiquidacionGcx.equals(""))
			consulta = consulta + "AND tipo_liquidacion_gcx = '"+tipoLiquidacionGcx+"' ";
		
		if(tipoLiquidacionGdyt!=null && !tipoLiquidacionGdyt.equals(""))
			consulta = consulta + "AND tipo_liquidacion_gdyt = '"+tipoLiquidacionGdyt+"' ";
		
		if(tipoTarifaLiqMateCx!=null && !tipoTarifaLiqMateCx.equals(""))
			consulta = consulta + "AND tipo_tarifa_liqmatecx = '"+tipoTarifaLiqMateCx+"' ";
		
		if(tipoTarifaLiqMateDyt!=null && !tipoTarifaLiqMateDyt.equals(""))
			consulta = consulta + "AND tipo_tarifa_liqmatedyt = '"+tipoTarifaLiqMateDyt+"' ";
		
		if(tipoFechaLiqTiemPcx!=null && !tipoFechaLiqTiemPcx.equals(""))
			consulta = consulta + "AND tipo_fecha_liqtiempcx = '"+tipoFechaLiqTiemPcx+"' ";
		
		if(tipoFechaLiqTiempDyt!=null && !tipoFechaLiqTiempDyt.equals(""))
			consulta = consulta + "AND tipo_fecha_liqtiempdyt = '"+tipoFechaLiqTiempDyt+"' ";
		
		
		if(radicarCuentasNegativas!=null && !radicarCuentasNegativas.equals(""))
		consulta = consulta + "AND radicar_cuentas_negativas = '"+radicarCuentasNegativas+"' ";
		
		if(asignarFactValorPacValorAbono!=null && !asignarFactValorPacValorAbono.equals(""))
		consulta = consulta + "AND asignarfac_valorpac_valorabo = '"+asignarFactValorPacValorAbono+"' ";		
		
		if(liquidacionTmpFracAdd!=null && !liquidacionTmpFracAdd.equals(""))
			consulta = consulta + "AND liquidacion_tmp_frac_add = '"+liquidacionTmpFracAdd+"' ";		
		
		if(planEspecial!=ConstantesBD.codigoNuncaValido)
			consulta = consulta + "AND centro_costo_plan_especial like '%"+planEspecial+"%') ";
		
		int codigoSinExcepcionBD=-8;
		
		
		consulta+= !UtilidadTexto.isEmpty(manejaBonos)?" and c.maneja_bonos= '"+manejaBonos+"' ":" ";
		consulta+= !UtilidadTexto.isEmpty(requiereBono)?" and c.req_bono_ing_pac= '"+requiereBono+"' ":" ";
		consulta+= !UtilidadTexto.isEmpty(manejaPromociones)?" and c.maneja_promociones= '"+manejaPromociones+"' ":" ";
		consulta+= !UtilidadTexto.isEmpty(esTargetaCliene)?" and c.es_conv_tar_cli= '"+esTargetaCliene+"' ":" ";
		consulta+= !UtilidadTexto.isEmpty(ingresoBdValido)?" and c.ing_pac_val_bd= '"+ingresoBdValido+"' ":" ";
		consulta+= !UtilidadTexto.isEmpty(ingresoPacienteReqAutorizacion)?" and c.ing_pac_req_aut= '"+ingresoPacienteReqAutorizacion+"' ":" ";
		consulta+= !UtilidadTexto.isEmpty(reqIngresoValido)?" and c.req_ing_val_auto= '"+reqIngresoValido+"' ":" ";
		consulta+= !UtilidadTexto.isEmpty(tipoAtencion)?" and c.tipo_atencion= '"+tipoAtencion+"' ":" ";
		consulta+=!UtilidadTexto.isEmpty(tipoLiquidacionPool)? " and c.tipo_liquidacion_pool='"+tipoLiquidacionPool+"'": " ";
		
		
		
		if(codigoConvenio>0 || codigoConvenio==codigoSinExcepcionBD)
		    consulta+="AND c.codigo="+codigoConvenio+" ";
		
		
		
		consulta+="ORDER BY c.nombre";
				
		 	   
		   
		  
		   
		    
		    
		   
		   
			
		
			
			
		
		return consulta;
	}
	
	/**
	 * M�todo para actualizar segun lo ingresado en parametros Generales
	 * @param con
	 * @param infoAdicIngresoConvenios
	 * @return
	 */
	public static int modificarInfoAdicConvenio(Connection con, boolean infoAdicIngresoConvenios)
	{
		int resp=0;	
		try
		{
	
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(actualizarInfoAdicIngresoConveniosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setBoolean(1, infoAdicIngresoConvenios);
			ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la actualizaci�n seg�n parametro general : SqlBaseConvenioDao "+e.toString());
			resp=0;			
		}	
		return resp;	
	}


	/**
	 * Obtiene el codigo PK del convenio a insertar
	 * @param con
	 * @param consulta
	 * @return
	 */
	public static int getPKConvenioAinsertar(Connection con, String consulta)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getInt("codigo");
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el PK de convenio: "+e);
		}
		return -1;
	}
	
	/**
	 * metodo que indica si un convenio en el tipo de contrato es capitado o no
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static boolean esConvenioCapitado(Connection con, String idCuenta)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(esConvenioCapitadoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, idCuenta);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{	
				 if(rs.getInt("cuenta")>0)
					 return true;
			}	 
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el esConvenioCapitado de convenio: "+e);
		}
		return false;
	}
	
	/**
	 * M�todo que consulta los contratos vigentes del convenio
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static HashMap consultarContratosVigentesConvenio(Connection con,int codigoConvenio)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarContratosVigentesConvenioStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoConvenio);
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarContratosVigentesConvenio: "+e);
			return null;
		}
	}
	
	/**
	 * metodo que indica si el convenio maneja o no complejidad
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static boolean convenioManejaComplejidad(Connection con, int codigoConvenio) throws BDException
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		boolean resultado=false;
		
		try	{
			Log4JManager.info("############## Inicio convenioManejaComplejidad");
			String consultaComplejidadStr=" SELECT maneja_complejidad as manejac from convenios where codigo=? ";
			String resul="";
			
			pst = con.prepareStatement(consultaComplejidadStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1,codigoConvenio);
			rs= pst.executeQuery();
			if(rs.next()){
				resul=rs.getString("manejac");
				if(resul.equals(ConstantesBD.acronimoSi)){
					resultado=true;
				}
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
		Log4JManager.info("############## Fin convenioManejaComplejidad");
		return resultado;
	}
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param codigoConvenio
	 * @return
	 */
	public static boolean existeVerificacionDerechosConvenio( Connection con, int codigoConvenio)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try 
	    {
			String consulta="SELECT tr.req_verific_derechos as verificacion FROM convenios c INNER JOIN tipos_regimen tr ON (tr.acronimo=c.tipo_regimen)  where codigo=? ";
			pst= con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoConvenio);
		    rs=pst.executeQuery();
		    if(rs.next())
		    {	
		        if(rs.getString("verificacion") != null && rs.getString("verificacion").equals(ConstantesBD.acronimoSi))
		        	return true;
		    }    	
	    }
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR existeVerificacionDerechosConvenio",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR existeVerificacionDerechosConvenio", e);
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
	    return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @param codigoConvenio
	 * @param estratoSocial
	 * @param acronimoTipoAfiliado
	 * @param fechaAfiliacion
	 * @return
	 */
	public static boolean actualizarConveniosPacientes(	Connection con, int codigoPaciente, int codigoConvenio, int estratoSocial, String acronimoTipoAfiliado, String fechaAfiliacion)
	{
		String consulta="";
		if(!existeConvenioPaciente(con, codigoPaciente, codigoConvenio))
			consulta=" INSERT INTO convenios_pacientes (estrato_social, fecha_afiliacion, codigo_paciente, convenio) values(?, ?, ?, ?)";
		else
			consulta=" UPDATE convenios_pacientes SET estrato_social=?, fecha_afiliacion=? WHERE codigo_paciente=? and convenio=? ";
		
		try 
	    {
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			if(estratoSocial<=0)
				ps.setNull(1, Types.INTEGER);
			else
				ps.setInt(1, estratoSocial);
			if(!UtilidadTexto.isEmpty(fechaAfiliacion))
				ps.setString(2, fechaAfiliacion);
			else
				ps.setObject(2, null);
			ps.setInt(3, codigoPaciente);
			ps.setInt(4, codigoConvenio);
		    if(ps.executeUpdate()>0)
		    {
		        return true;
		    }    	
		} 
	    catch (SQLException e) 
	    {
			logger.error("error actualizarConveniosPacientes");
			e.printStackTrace();
		}
	    return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @param codigoConvenio
	 * @return
	 */
	private static boolean existeConvenioPaciente(Connection con, int codigoPaciente, int codigoConvenio)
	{
		String consulta="SELECT codigo_paciente from convenios_pacientes where codigo_paciente=? and convenio=? ";
		try 
	    {
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoPaciente);
			ps.setInt(2, codigoConvenio);
		    ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
		    if(rs.next())
		    {	
		        return true;
		    }    	
		} 
	    catch (SQLException e) 
	    {
			logger.error("error existeConvenioPaciente");
			e.printStackTrace();
		}
	    return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @param codigoConvenio
	 * @return
	 */
	public static int obtenerNumeroDiasVencimiento(Connection con, int codigoConvenio)
	{
		String consulta="SELECT coalesce(num_dias_vencimiento, 0) as dias from convenios where codigo=?";
		try 
	    {
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoConvenio);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
		    if(rs.next())
		    {	
		        return rs.getInt("dias");
		    }    	
		} 
	    catch (SQLException e) 
	    {
			logger.error("error obtenerNumeroDiasVencimiento");
			e.printStackTrace();
		}
	    //el numero de dias debe ser cero si llega aca
	    return 0;
	}
	
	/**
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static int obtenerRadicarCuentasNegativas(Connection con, int codigoConvenio)
	{
		String consulta="SELECT radicar_cuentas_negativas as radicar from convenios where codigo=?";
		try 
	    {
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoConvenio);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
		    if(rs.next())
		    {	
		    	if(rs.getString("radicar").equals(ConstantesBD.acronimoSi))
		    		return 1;
		    	else
		    		return 0;		        
		    }    	
		} 
	    catch (SQLException e) 
	    {
			logger.error("error radicar cuentas negativas");
			e.printStackTrace();
		}
	  
	    return 0;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @param codigoConvenio
	 * @return
	 */
	public static String obtenerCuentaContableConvenio(Connection con, int codigoConvenio)
	{
		String consulta="SELECT coalesce(tc.cuenta_contable||'', '') as cuen from convenios c INNER JOIN tipos_convenio tc ON (c.tipo_convenio=tc.codigo and c.institucion=tc.institucion) where c.codigo=?";
		try 
	    {
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoConvenio);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
		    if(rs.next())
		    {	
		        return rs.getString("cuen");
		    }    	
		} 
	    catch (SQLException e) 
	    {
			logger.error("error obtenerCuentaContableConvenio");
			e.printStackTrace();
		}
	    return "";
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @param codigoConvenio
	 * @return
	 */
	public static String obtenerNroIdentificacionConvenio(Connection con, int codigoConvenio)
	{
		String consulta="SELECT t.numero_identificacion as numid FROM convenios c INNER JOIN empresas e ON(e.codigo=c.empresa) INNER JOIN terceros t ON (t.codigo=e.tercero) where c.codigo=?";
		try 
	    {
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoConvenio);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
		    if(rs.next())
		    {	
		        return rs.getString("numid");
		    }    	
		} 
	    catch (SQLException e) 
	    {
			logger.error("error obtenerNroIdentificacionConvenio");
			e.printStackTrace();
		}
	    return "";
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static double obtenerEmpresaInstitucionConvenio( Connection con, int codigoConvenio)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		double retorna=ConstantesBD.codigoNuncaValido;
		try
		{
			String consulta="SELECT COALESCE(empresa_institucion, "+ConstantesBD.codigoNuncaValido+") as empresainstitucion FROM convenios WHERE codigo=? ";
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);			
			pst.setInt(1,codigoConvenio);
			rs=pst.executeQuery();
			if(rs.next())
			{
				retorna= rs.getDouble("empresainstitucion");
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerEmpresaInstitucionConvenio",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerEmpresaInstitucionConvenio", e);
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
		return retorna;
	}

	public static ArrayList cargarPlanEspecial(Connection con, String codigoInstitucion) 
	{
		ArrayList respuesta =new ArrayList();
		
		try 
		{
			ResultSetDecorator rs;
			HashMap mapa;
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarCentrosCostoPlanEspecialStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setBoolean(1, UtilidadTexto.getBoolean(ConstantesBD.acronimoTrueCorto));
			ps.setInt(2, Utilidades.convertirAEntero(codigoInstitucion));
			ps.setInt(3, ConstantesBD.codigoTipoAreaDirecto);
			ps.setInt(4, ConstantesBD.codigoNuncaValido);
			rs =new ResultSetDecorator(ps.executeQuery());
			
			while (rs.next()) 
			{
				mapa = new HashMap();
				mapa.put("codigo", rs.getObject(1));
				mapa.put("centro_costo", rs.getObject(2));
				mapa.put("centro_atencion", rs.getObject(3));
				
				respuesta.add(mapa);
			}	
			rs.close();
			ps.close();
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return respuesta;
	}
	
	public static ArrayList consultarCcContable(Connection con, String codigoInstitucion) 
	{
		ArrayList respuesta =new ArrayList();
		
		try 
		{
			ResultSetDecorator rs;
			HashMap mapa;
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarCentrosCostoContableStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, ValoresPorDefecto.getValorTrueParaConsultas());
			ps.setInt(2, Utilidades.convertirAEntero(codigoInstitucion));
			rs =new ResultSetDecorator(ps.executeQuery());
			
			while (rs.next()) 
			{
				mapa = new HashMap();
				mapa.put("codigo", rs.getObject(1));
				mapa.put("centro_costo", rs.getObject(2));
				mapa.put("centro_atencion", rs.getObject(3));
				
				respuesta.add(mapa);
			}
			rs.close();
			ps.close();
			
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return respuesta;
	}

	public static String cargarNombreCentroCostoPlanEspecial(Connection con, String codigoPlanEspecial) 
	{
		String consulta="SELECT cc.nombre || '-' || ca.descripcion || '-' || cc.codigo  as nombre " +
							"from centros_costo cc INNER JOIN centro_atencion ca ON(ca.consecutivo=cc.centro_atencion) " +
							"where cc.codigo=?";
		try 
	    {
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Integer.parseInt(codigoPlanEspecial));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
		    if(rs.next())
		    {	
		        return rs.getString("nombre");
		    }    	
		} 
	    catch (SQLException e) 
	    {
			logger.error("error Obtener Nombre Centro Costo Plan Especial");
			e.printStackTrace();
		}
	    return "";
	}
	
	public static HashMap consultaUsuariosGlosasConvenio (Connection con)
	{
		HashMap mapa = new HashMap();
		mapa.put("0", "numRegistros");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(strCadenaConsulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			logger.info("====>Consulta Usuarios Glosas Convenio: "+strCadenaConsulta);
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			return mapa; 
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR CONSULTANDO LOS USUARIOS GLOSAS CONVENIO"+e);
		}
		return mapa;
	}
	
	public static boolean insertarUsuariosGlosasConvenio (Connection connection, HashMap datos)
	{
		boolean enTransaccion = false;
		logger.info("===> SqlBase insertarUsuariosGlosasConvenio");
		
		try
		{
			logger.info("===> El mapa datos es: "+datos);
			logger.info("===> SqlBase Entr� al Try");
			logger.info("===> SqlBase Preparo la conexi�n");
	  		PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(strCadenaInsertar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	  		ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(connection, "seq_usuarios_glosas_conv"));
	  		logger.info("===> SqlBase Insert� codigo "+UtilidadBD.obtenerSiguienteValorSecuencia(connection, "seq_usuarios_glosas_conv"));
	  		ps.setString(2, datos.get("usuario")+"");
	  		logger.info("===> SqlBase Insert� usuario "+datos.get("usuario")+"");
	  		ps.setString(3, datos.get("tipousuario")+"");
	  		logger.info("===> SqlBase Insert� tipousuario "+ datos.get("tipousuario")+"");
	  		ps.setString(4, datos.get("activo")+"");
	  		logger.info("===> SqlBase Insert� activo "+datos.get("activo")+"");
	  		ps.setInt(5, Integer.parseInt(datos.get("convenio")+""));
	  		logger.info("===> SqlBase Insert� convenio "+Integer.parseInt(datos.get("convenio")+""));
	  		ps.setDate(6, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(connection))));
	  		logger.info("===> SqlBase Insert� fechaModifica "+Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(connection))));
	  		ps.setString(7, UtilidadFecha.getHoraActual(connection));
	  		logger.info("===> SqlBase Insert� HoraModifica "+UtilidadFecha.getHoraActual(connection));
	  		ps.setString(8, datos.get("usuariomodifica")+"");
	  		logger.info("===> SqlBase Insert� usuarioModifica "+datos.get("usuariomodifica")+"");
	  		enTransaccion = (ps.executeUpdate() > 0);
	  		
	    	if(enTransaccion)
	    	{
	    		logger.info("SE ADICION� CORRECTAMENTE EL USUARIO");
	    		return true;
	    	}
	    	
	    	else
	    	{
	    		logger.info("HUBO PROBLEMAS INSERTANDO EL USUARIO");
	    		return false;
	    	}
	    		
		}
		
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static boolean existeConvenioOdontologico(String tipoAtencion, int institucion){

		
		try 
		{
			
		Connection con = UtilidadBD.abrirConexion();
		
		String consultaExisteConvenio = "SELECT c.codigo  FROM facturacion.convenios c where  c.tipo_atencion =? and c.institucion =? ";
		
		PreparedStatementDecorator cargarExistencia = new PreparedStatementDecorator(con.prepareStatement(consultaExisteConvenio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
		cargarExistencia.setString(1, tipoAtencion);
		cargarExistencia.setInt(2, institucion);
		
		
		ResultSetDecorator rs=new ResultSetDecorator(cargarExistencia.executeQuery());
		
		logger.info("consulta >>>>>>>>>>>>>>>>>> " + "   Existencia   " +"    tipoAtencion   ->  "+tipoAtencion);
	    if(rs.next())
	    {	
	       
	        	return true;
	    }   
		
		logger.info("consulta >>>>>>>>>>>>>>>>>> " + "   Existencia   " +"    tipoAtencion   ->  "+tipoAtencion);
		
		}catch(SQLException e)
		{
			logger.warn(e+" tipoAtencion "+e.toString());
			return false;
		}
		
		
		return false;
	}

	public static boolean modificarUsuariosGlosasConvenio (Connection connection, HashMap datos)
	{
		boolean enTransaccion = false;
		logger.info("===> SqlBase modificarUsuariosGlosasConvenio");
		
		try
		{
			logger.info("===> El mapa datos es: "+datos);
			logger.info("===> SqlBase Entr� al Try");
			logger.info("===> SqlBase Preparo la conexi�n");
	  		PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(strCadenaActualizar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	  		
	  		ps.setString(1, datos.get("usuario")+"");
	  		logger.info("===> SqlBase Insert� usuario "+datos.get("usuario")+"");
	  		ps.setString(2, datos.get("tipousuario")+"");
	  		logger.info("===> SqlBase Insert� tipousuario "+ datos.get("tipousuario")+"");
	  		ps.setString(3, datos.get("activo")+"");
	  		logger.info("===> SqlBase Insert� activo "+datos.get("activo")+"");
	  		ps.setInt(4, Integer.parseInt(datos.get("convenio")+""));
	  		logger.info("===> SqlBase Insert� convenio "+Integer.parseInt(datos.get("convenio")+""));
	  		ps.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(connection))));
	  		logger.info("===> SqlBase Insert� fechaModifica "+Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(connection))));
	  		ps.setString(6, UtilidadFecha.getHoraActual(connection));
	  		logger.info("===> SqlBase Insert� HoraModifica "+UtilidadFecha.getHoraActual(connection));
	  		ps.setString(7, datos.get("usuariomodifica")+"");
	  		logger.info("===> SqlBase Insert� usuarioModifica "+datos.get("usuariomodifica")+"");
	  		
	  		ps.setString(8, datos.get("codigo")+"");
	  		logger.info("===> SqlBase Insert� codigo "+datos.get("codigo")+"");
	  		
	  		enTransaccion = (ps.executeUpdate() > 0);
	  		
	    	if(enTransaccion)
	    	{
	    		logger.info("SE MODIFIC� CORRECTAMENTE EL USUARIO");
	    		return true;
	    	}
	    	
	    	else
	    	{
	    		logger.info("HUBO PROBLEMAS MODIFICANDO EL USUARIO");
	    		return false;
	    	}
	    		
		}
		
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Cadena consulta de usuarios_glosas_conv
	 */
	private static final String consultaUsuariosGlosasPorConvenio = "SELECT " +
														"ugc.codigo AS codigo, " +
														"ugc.usuario AS usuario, " +
														"getnombrepersona(u.codigo_persona) AS nombre_usuario, " +
														"ugc.tipo_usuario AS tipo_usuario, " +
														"ugc.activo AS activo " +
													"FROM " +
														"usuarios_glosas_conv ugc " +
														"INNER JOIN usuarios u ON (u.login=ugc.usuario) " +
													"WHERE " +
														"convenio=? ORDER BY nombre_usuario ";
	
	/**
	 * Metodo encargado de consultar los usuarios de Glosa por Convenio
	 * @param con
	 * @param convenio
	 * @return
	 */
	public static HashMap consultaUsuariosGlosasPorConvenio(Connection con, String convenio)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator ps;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaUsuariosGlosasPorConvenio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, convenio);
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);				
			
			logger.info("\n\nCONSULTANDO USUARIOS POR CONVENIO ----->>>>>>>>>>>"+consultaUsuariosGlosasPorConvenio.replace("?", convenio));
	
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO USUARIOS POR CONVENIO------>>>>>>"+e);
			e.printStackTrace();
		}
		logger.info("\n\nRESULTADOS USUARIO--------->"+resultados);
		
		return resultados;
	}
	
	//*************************************************************************************
	
	/**
	 * consultar indicadores del convenio
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	
	
	
	
	public static HashMap cargarAdjuntos(Connection con , int convenio){
		HashMap respuesta = new HashMap();
		
		logger.info("EL CONVENIO QUE ENTRA A CARGAR ADJUNTOS ES ++++++++++++++=========="+convenio);
		
		try
		{
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarAdjuntos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,convenio);
			logger.info(consultarAdjuntos);
			
			respuesta = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, false);
			Utilidades.imprimirMapa(respuesta);
			ps.close();
			
		}catch (SQLException e) {
			logger.info("error en consultarAjuntos "+ convenio);
			e.printStackTrace();
		}
	
		return respuesta;
	}
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static boolean eliminarAjuntos(Connection con , int convenio) 
	{
		
		
		try 
		{
			
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(eliminarAdjuntos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, convenio);
			ps.executeUpdate();
			
			return true;
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN eliminar  adjuntos "+ e);
		
		}
		return false;
	}
	
	public static HashMap consultarIndicadoresConvenio(Connection con, HashMap parametros)
	{
		HashMap respuesta = new HashMap();
		
		respuesta.put("reporteAtencionIniUrg",ConstantesBD.acronimoNo);
		respuesta.put("genAutoValAteIniUrg",ConstantesBD.acronimoNo);
		respuesta.put("reporteInconBd",ConstantesBD.acronimoNo);
		respuesta.put("requiereAutorizacion", ConstantesBD.acronimoNo);
				
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strCadenaConsultarIndicadores,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("codigoConvenio").toString()));
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				respuesta.put("reporteAtencionIniUrg",rs.getString("reporte_atencion_ini_urg"));
				respuesta.put("genAutoValAteIniUrg",rs.getString("gen_auto_val_ate_ini_urg"));
				respuesta.put("reporteInconBd",rs.getString("reporte_incon_bd"));
				respuesta.put("requiereAutorizacion",rs.getString("req_auto_servicio"));
			}
			
		}catch (Exception e) {
			logger.info("error en consultarIndicadoresConvenio "+parametros);
		}
		
		return respuesta;
	}
	
	/**
	 * 
	 * @param convenio
	 * @return
	 */
	public static boolean manejaPromociones(int convenio)
	{
		String consultaStr="select maneja_promociones from facturacion.convenios where codigo=?";
		boolean maneja= false;
		
		try
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,convenio);
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				maneja= UtilidadTexto.getBoolean(rs.getString(1));
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			
		}
		catch (SQLException e) {
			logger.error("error en consultarIndicadoresConvenio "+e);
		}
		return maneja;
	}
	
	
	
	/**
	 * 
	 * @param convenio
	 * @return
	 */
	public static String obtenerTipoAtencion(int convenio) throws BDException
	{
		String consultaStr="SELECT tipo_atencion FROM facturacion.convenios where codigo=?";
		String retorna="";
		
		try
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,convenio);
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				retorna= rs.getString(1);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return retorna;
	}

	public static ArrayList<Convenio> consultarConveniosPacienteUltimoIngreso(Connection con, int codigoPersona, boolean filtrarOdontologicos)
	{
		String sentencia=
			"SELECT " +
				"con.codigo AS codigo, " +
				"con.empresa AS empresa, " +
				"con.tipo_regimen AS tipo_regimen, " +
				"con.nombre AS nombre, " +
				"con.observaciones AS observaciones, " +
				"con.plan_beneficios AS plan_beneficios, " +
				"con.codigo_min_salud AS codigo_min_salud, " +
				"con.formato_factura AS formato_factura, " +
				"con.activo AS activo, " +
				"con.tipo_contrato AS tipo_contrato, " +
				"con.info_adic_ingreso_convenios AS info_adic_ingreso_convenios, " +
				"con.pyp AS pyp, " +
				"con.unificar_pyp AS unificar_pyp, " +
				"con.encabezado_factura AS encabezado_factura, " +
				"con.pie_factura AS pie_factura, " +
				"con.tipo_convenio AS tipo_convenio, " +
				"con.institucion AS institucion, " +
				"con.num_dias_vencimiento AS num_dias_vencimiento, " +
				"con.requiere_justificacion_serv AS requiere_justificacion_serv, " +
				"con.maneja_complejidad AS maneja_complejidad, " +
				"con.semanas_min_cotizacion AS semanas_min_cotizacion, " +
				"con.requiere_ingreso_num_carnet AS requiere_ingreso_num_carnet, " +
				"con.requiere_justificacion_art AS requiere_justificacion_art, " +
				"con.tipo_codigo AS tipo_codigo, " +
				"con.ajuste_servicios AS ajuste_servicios, " +
				"con.ajuste_articulos AS ajuste_articulos, " +
				"con.interfaz AS interfaz, " +
				"con.empresa_institucion AS empresa_institucion, " +
				"con.cantidad_max_cirugia AS cantidad_max_cirugia, " +
				"con.cantidad_max_ayudpag AS cantidad_max_ayudpag, " +
				"con.tipo_liquidacion_scx AS tipo_liquidacion_scx, " +
				"con.tipo_liquidacion_dyt AS tipo_liquidacion_dyt, " +
				"con.tipo_liquidacion_gcx AS tipo_liquidacion_gcx, " +
				"con.tipo_liquidacion_gdyt AS tipo_liquidacion_gdyt, " +
				"con.tipo_tarifa_liqmatecx AS tipo_tarifa_liqmatecx, " +
				"con.tipo_tarifa_liqmatedyt AS tipo_tarifa_liqmatedyt, " +
				"con.liquidacion_tmp_frac_add AS liquidacion_tmp_frac_add, " +
				"con.tipo_fecha_liqtiempcx AS tipo_fecha_liqtiempcx, " +
				"con.tipo_fecha_liqtiempdyt AS tipo_fecha_liqtiempdyt, " +
				"con.req_just_art_nopos_dif_med AS req_just_art_nopos_dif_med, " +
				"con.centro_costo_plan_especial AS centro_costo_plan_especial, " +
				"con.excento_deudor AS excento_deudor, " +
				"con.excento_doc_garantia AS excento_doc_garantia, " +
				"con.vip AS vip, " +
				"con.dias_control_post AS dias_control_post, " +
				"con.citas_max_control AS citas_max_control, " +
				"con.radicar_cuentas_negativas AS radicar_cuentas_negativas, " +
				"con.asignarfac_valorpac_valorabo AS asignarfac_valorpac_valorabo, " +
				"con.aseguradora AS aseguradora, " +
				"con.cod_aseguradora As cod_aseguradora, " +
				"con.valor_letras_factura AS valor_letras_factura, " +
				"con.reporte_incon_bd AS reporte_incon_bd, " +
				"con.reporte_atencion_ini_urg AS reporte_atencion_ini_urg, " +
				"con.gen_auto_val_ate_ini_urg AS gen_auto_val_ate_ini_urg," +
				"con.req_auto_serv_add AS req_auto_serv_add, " +
				"con.formato_autorizacion AS formato_autorizacion, " +
				"con.man_mul_inc_citas AS man_mul_inc_citas, " +
				"con.val_mul_inc_citas AS val_mul_inc_citas, " +
				"con.centro_costo_contable AS centro_costo_contable, " +
				"con.maneja_bonos AS maneja_bonos, " +
				"con.req_bono_ing_pac AS req_bono_ing_pac, " +
				"con.maneja_promociones AS maneja_promociones, " +
				"con.es_conv_tar_cli AS es_conv_tar_cli, " +
				"con.ing_pac_val_bd AS ing_pac_val_bd, " +
				"con.ing_pac_req_aut AS ing_pac_req_aut, " +
				"con.req_ing_val_auto AS req_ing_val_auto, " +
				"con.tipo_atencion AS tipo_atencion, " +
				
				"sc.contrato AS contrato " +
			"FROM convenios con INNER JOIN sub_cuentas sc ON(con.codigo=sc.convenio) INNER JOIN ingresos ing ON(ing.id=sc.ingreso) WHERE ing.codigo_paciente=? AND ing.fecha_ingreso=(SELECT max(ing_interno.fecha_ingreso) FROM ingresos ing_interno WHERE ing.codigo_paciente=ing_interno.codigo_paciente) ";
		try{
			if(filtrarOdontologicos)
			{
				sentencia+="AND tipo_atencion='"+ConstantesIntegridadDominio.acronimoTipoAtencionConvenioOdontologico+"' ";
			}
			sentencia+="order by sc.nro_prioridad";
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
			psd.setInt(1, codigoPersona);
			//logger.info(psd);
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			ArrayList<Convenio> lista=new ArrayList<Convenio>();
			while(rsd.next())
			{
				Convenio conv=new Convenio();
				llenarConvenioAPartirDeresultSet(conv, rsd);
				lista.add(conv);
			}
			rsd.close();
			psd.close();
			return lista;
		}
		catch (SQLException e) {
			logger.error("Error consultando los convenios del paciente del ultimo ingreso", e);
		}
		return null;
	}

	public static void llenarConvenioAPartirDeresultSet(Convenio convenio,
			ResultSetDecorator rsd) {
		try {
			convenio.setCodigo(rsd.getInt("codigo"));
			convenio.setEmpresa(rsd.getInt("empresa"));
			convenio.setTipoRegimen(rsd.getString("tipo_regimen"));
			convenio.setNombre(rsd.getString("nombre"));
			convenio.setObservaciones(rsd.getString("observaciones"));
			convenio.setPlanBeneficios(rsd.getString("plan_beneficios"));
			convenio.setCodigoMinSalud(rsd.getString("codigo_min_salud"));
			convenio.setFormatoFactura(rsd.getInt("formato_factura"));
			convenio.setActiva(rsd.getBoolean("activo"));
			convenio.setTipoContrato(rsd.getInt("tipo_contrato"));
			convenio.setPyp(rsd.getString("pyp"));
			convenio.setUnificarPyp(rsd.getString("unificar_pyp"));
			convenio.setEncabezadoFactura(rsd.getString("encabezado_factura"));
			convenio.setPieFactura(rsd.getString("pie_factura"));
			convenio.setTipoConvenio(rsd.getString("tipo_convenio"));
			convenio.setInstitucion(rsd.getInt("institucion"));
			convenio.setNumeroDiasVencimiento(rsd.getString("num_dias_vencimiento"));
			convenio.setRequiere_autorizacion_servicio(rsd.getString("requiere_justificacion_serv"));
			convenio.setManejaComplejidad(rsd.getString("maneja_complejidad"));
			convenio.setSemanasMinimasCotizacion(rsd.getString("semanas_min_cotizacion"));
			convenio.setRequiereNumeroCarnet(rsd.getString("requiere_ingreso_num_carnet"));
			convenio.setRequiereJustificacionArticulos(rsd.getString("requiere_justificacion_art"));
			convenio.setTipoCodigo(rsd.getInt("tipo_codigo"));
			convenio.setAjusteServicios(rsd.getString("ajuste_servicios"));
			convenio.setAjusteArticulos(rsd.getString("ajuste_articulos"));
			convenio.setInterfaz(rsd.getString("interfaz"));
			convenio.setEmpresaInstitucion(rsd.getString("empresa_institucion"));
			convenio.setCantidadMaxCirugia(rsd.getString("cantidad_max_cirugia"));
			convenio.setCantidadMaxAyudpag(rsd.getString("cantidad_max_ayudpag"));
			convenio.setTipoLiquidacionScx(rsd.getString("tipo_liquidacion_scx"));
			convenio.setTipoLiquidacionDyt(rsd.getString("tipo_liquidacion_dyt"));
			convenio.setTipoLiquidacionGcx(rsd.getString("tipo_liquidacion_gcx"));
			convenio.setTipoLiquidacionGdyt(rsd.getString("tipo_liquidacion_gdyt"));
			convenio.setTipoTarifaLiqMateCx(rsd.getString("tipo_tarifa_liqmatecx"));
			convenio.setTipoTarifaLiqMateDyt(rsd.getString("tipo_tarifa_liqmatedyt"));
			convenio.setLiquidacionTmpFracAdd(rsd.getString("liquidacion_tmp_frac_add"));
			convenio.setTipoFechaLiqTiemPcx(rsd.getString("tipo_fecha_liqtiempcx"));
			convenio.setTipoFechaLiqTiempDyt(rsd.getString("tipo_fecha_liqtiempdyt"));
			convenio.setRequiereJustArtNoposDifMed(rsd.getString("req_just_art_nopos_dif_med"));
			//"centro_costo_plan_especial"
			convenio.setExcentoDeudor(rsd.getString("excento_deudor"));
			convenio.setExcentoDocumentoGarantia(rsd.getString("excento_doc_garantia"));
			convenio.setVip(rsd.getString("vip"));
			/*
			"dias_control_post"
			"citas_max_control"
			"radicar_cuentas_negativas"
			"asignarfac_valorpac_valorabo"
			"aseguradora"
			"cod_aseguradora"
			"valor_letras_factura"
			"reporte_incon_bd"
			"reporte_atencion_ini_urg"
			"gen_auto_val_ate_ini_urg ," +
			"req_auto_serv_add"
			"formato_autorizacion"
			"man_mul_inc_citas"
			"val_mul_inc_citas"
			"centro_costo_contable"*/
			convenio.setManejaBonos(rsd.getString("maneja_bonos"));
			convenio.setRequiereBono(rsd.getString("req_bono_ing_pac"));
			convenio.setManejaPromociones(rsd.getString("maneja_promociones"));
			convenio.setEsTargetaCliene(rsd.getString("es_conv_tar_cli"));
			convenio.setIngresoBdValido(rsd.getString("ing_pac_val_bd"));
			convenio.setIngresoPacienteReqAutorizacion(rsd.getString("ing_pac_req_aut"));
			convenio.setRequiereIngresoValorAuto(rsd.getString("req_ing_val_auto"));
			convenio.setTipoAtencion(rsd.getString("tipo_atencion"));
			
			
			//Datos de contrato
			Contrato contrato=new Contrato();
			contrato.setCodigo(rsd.getInt("contrato"));
			convenio.setContrato(contrato);
		} catch (SQLException e) {
			logger.error("Error cargando el convenio desde resultset ",e);
		}
	}
	
	//*************************************************************************************	
	
	public static int cargarCentroAtencionContable(Connection con, int codigo) throws BDException{
		PreparedStatement pst=null;
		ResultSet rs=null;
		int centroAtencionContable=0;
		try{
			Log4JManager.info("############## Inicio cargarCentroAtencionContable");
			String sentencia="SELECT centro_atencion FROM facturacion.cen_aten_contable_conv WHERE convenio=?";
			pst=con.prepareStatement(sentencia);
			pst.setInt(1, codigo);
			rs=pst.executeQuery();
			if(rs.next())
			{
				centroAtencionContable=rs.getInt("centro_atencion");
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
		Log4JManager.info("############## Fin cargarCentroAtencionContable");
		return centroAtencionContable;
	}	
	
	
	
	
	
	/**
	 * 
	 * @param convenio
	 * @return
	 */
	public static String obtenerTipoLiquidacionPool(int convenio)
	{
		String consultaStr="select tipo_liquidacion_pool from convenios where codigo=?";
		String retorna="";
		
		try
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,convenio);
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				retorna= rs.getString(1);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			
		}
		catch (SQLException e) {
			logger.error("error en consultarIndicadoresConvenio "+e);
		}
		return retorna;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * CARGAR CONVENIOS ODONTOLOGICOS
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoConvenio> cargarConveniosArrayList(DtoConvenio dto)
	{
		
		
		ArrayList<DtoConvenio> newList = new ArrayList<DtoConvenio>();
		
		String consultaStr= "select codigo as codigoConvenio , tipo_atencion as tipoAtencion,maneja_montos as manejamontos from facturacion.convenios  where 1=1";
		
				consultaStr+=dto.getCodigo()>0?" AND codigo="+dto.getCodigo(): " ";
				consultaStr+=!UtilidadTexto.isEmpty(dto.getTipoAtencion()) ?"AND tipo_atencion='"+dto.getTipoAtencion()+"'": "";          //  'ATEN_ODON_ESP";
		
		logger.info("\n\n\n\n\n\n CONSULTA CONVENIO \n\n\n");
		logger.info("Sql -->"+consultaStr);
		logger.info("\n\n\n\n\n");
		
		try
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultaStr);
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoConvenio newDtoConvenio = new DtoConvenio();
				newDtoConvenio.setCodigo(rs.getInt("codigoConvenio"));
				newDtoConvenio.setTipoAtencion(rs.getString("tipoAtencion"));
				newDtoConvenio.setConvenioManejaMontos(rs.getString("manejamontos"));
				newList.add(newDtoConvenio);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			
		}
		catch (SQLException e) {
			logger.error("Error consultar CONVENIO "+e);
		}
		
		
		return newList;
	}

	
	/**
	 * 
	 * @param codigoConvenio
	 * @return
	 */
	public static boolean esConvenioTipoConventioEventoCatTrans(int codigoConvenio) 
	{
		Connection con=UtilidadBD.abrirConexion();
		boolean resultado=false;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("select tc.aseg_at_ec from convenios conv inner join tipos_convenio tc on (tc.codigo=conv.tipo_convenio) where conv.codigo=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoConvenio);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{	
				 resultado=UtilidadTexto.getBoolean(rs.getString(1));
			}	 
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el esConvenioCapitado de convenio: "+e);
		}
		finally
		{
			UtilidadBD.closeConnection(con);
		}
		return resultado;
	}

	
	
	
}