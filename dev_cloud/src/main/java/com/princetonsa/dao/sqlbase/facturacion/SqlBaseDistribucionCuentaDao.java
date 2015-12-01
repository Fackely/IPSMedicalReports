/*
 * Jul 5, 2007 
 * Proyect axioma
 * Paquete com.princetonsa.dao.sqlbase.facturacion
 * @author Jorge Armando Osorio Velasquez
 * Compilador Java 1.5.0_07-b03
 */
package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.interfaces.UtilidadBDInterfaz;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoMontoCobro;
import com.princetonsa.dto.interfaz.DtoInterfazPaciente;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.ViasIngreso;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;

/**
 * @author Jorge Armando Osorio Velasquez
 *
 */
public class SqlBaseDistribucionCuentaDao 
{
	/**
	 * Log de la clase.
	 */
	public static Logger logger=Logger.getLogger(SqlBaseDistribucionCuentaDao.class);
	
	/**
	 * 
	 */
	public static String cadenaConsultaSolicitudesSegunConvenioBusquedaAvanzada = "SELECT " +
																	" ss.codigo AS codigo," +
																	" to_char(getFechaSolicitud(ss.solicitud),'dd/mm/yyyy') AS fechasolicitud," +
																	" ss.solicitud AS solicitud," +
																	" ss.sub_cuenta AS subcuenta," +
																	" ss.servicio AS servicio," +
																	" getnombreservicio(ss.servicio,"+ConstantesBD.codigoTarifarioCups+") AS nomservicio," +
																	" gettieneportatilsolicitud(ss.solicitud,dc.servicio) AS codigoportatil,"+
																	" getnombreservicio(gettieneportatilsolicitud(ss.solicitud,dc.servicio),"+ConstantesBD.codigoTarifarioCups+") as nomservicioportatil," +
																	" ss.articulo AS articulo," +
																	" getdescripcionarticulo(ss.articulo) AS nomarticulo," +
																	" ss.cuenta AS cuenta," +
																	" ss.cantidad AS cantidad," +
																	" ss.cubierto AS cubierto," +
																	" ss.monto AS monto," +
																	" ss.tipo_solicitud AS tiposolicitud," +
																	" case when ss.tipo_solicitud="+ConstantesBD.codigoTipoSolicitudCirugia+" then getintegridaddominio(getindicadorqxsolicitud(ss.solicitud)) else getnomtiposolicitud(ss.tipo_solicitud) end  as nomtiposolicitud," +
																	" ss.paquetizada AS paquetizada, " +
																	" ss.sol_subcuenta_padre AS solsubcuentapadre," +
																	" ss.servicio_cx AS serviciocx," +
																	" getnombreservicio(ss.servicio_cx,"+ConstantesBD.codigoTarifarioCups+") AS nomserviciocx," +
																	" ss.tipo_asocio AS tipoasocio," +
																	" getnombretipoasocio(ta.codigo) AS nomtipoasocio," +
																	" ss.porcentaje AS porcentaje," +
																	" getConsecutivoSolicitud(ss.solicitud) AS consecutivosolicitud," +
																	" getCodigoCCSolicita(ss.solicitud) AS codccsolicita," +
																	" getnomcentrocosto(getCodigoCCSolicita(ss.solicitud)) AS nomccsolicita," +
																	" getCodigoCCEjecuta(ss.solicitud) AS codccejecuta," +
																	" getnomcentrocosto(getCodigoCCEjecuta(ss.solicitud)) AS nomccejecuta," +
																	" getNumRespSolServicio(ss.solicitud,ss.servicio) AS numresservicio, " +
																	" getNumRespFacSolServicio(ss.solicitud,ss.servicio) AS numresfactservicio," +
																	" getNumRespSolArticulo(ss.solicitud,ss.articulo) AS numresarticulo," +
																	" getNumRespFacSolArticulo(ss.solicitud,ss.articulo) AS numresfactarticulo," +
																	" ss.tipo_distribucion AS tipodistribucion, " +
																	" dc.cubierto AS cubierto," +
																	//campos de cargos.
																	" dc.codigo_detalle_cargo AS codigodetallecargo, " +
																	" dc.convenio AS convenio," +
																	" dc.esquema_tarifario AS esquematarifario," +
																	" dc.cantidad_cargada AS cantidadcargada," +
																	" dc.valor_unitario_tarifa AS valunitariotarifa," +
																	" dc.valor_unitario_cargado AS valunitariocargado," +
																	" dc.valor_total_cargado AS valtotalcargado," +
																	" dc.porcentaje_cargado AS porcentajecargado," +
																	" dc.porcentaje_recargo AS porcentajerecargo," +
																	" dc.valor_unitario_recargo AS valunitariorecargo," +
																	" dc.porcentaje_dcto AS porcentajedcto, " +
																	" dc.valor_unitario_dcto AS valunitariodcto," +
																	" dc.valor_unitario_iva AS valunitarioiva," +
																	" dc.nro_autorizacion AS nroautorizacion," +
																	" dc.estado AS estado," +
																	" getestadosolfac(dc.estado) AS descestado," +
																	" dc.cargo_padre AS cargopadre," +
																	
																	//Cambio por que en la Anulacion de la Factura no se esta Actualizando este campo
																	//Se presento el error en la Distribuciï¿½n. Tarea 52550, la cual indica que despuï¿½s
																	//de anular una factura se sigue presentando el indicativo "FACTURADO"
																	//" dc.facturado as facturado," +
																	"CASE WHEN fac.estado_facturacion IS NOT NULL THEN " + 
																		"(CASE WHEN fac.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" THEN " +
																			"'"+ConstantesBD.acronimoSi+"'" + 
																		"ELSE " +
																			"'"+ConstantesBD.acronimoNo+"'" + 
																		"END) " +
																	"ELSE " + 
																		"'"+ConstantesBD.acronimoNo+"'" +
																	"END AS facturado, " +
																	
																	" dc.observaciones AS observacionescargo, " +
																	" CASE WHEN dc.tipo_solicitud="+ConstantesBD.codigoTipoSolicitudCirugia+" THEN CASE WHEN dc.servicio_cx IS NOT NULL THEN getnombreservicio(dc.servicio_cx,"+ConstantesBD.codigoTarifarioCups+") ELSE getdescripcionarticulo(dc.articulo) END else case when dc.servicio is null then getdescripcionarticulo(dc.articulo) else getnombreservicio(dc.servicio,"+ConstantesBD.codigoTarifarioCups+") END END AS nomserart, " +
																	" CASE WHEN dc.servicio IS NULL THEN '"+ConstantesBD.acronimoNo+"' ELSE '"+ConstantesBD.acronimoSi+"' END AS esservicio, " +
																	" getcodigoestadohcsol(dc.solicitud) AS codestadohc, " +
																	" getestadosolhis(getcodigoestadohcsol(dc.solicitud)) AS nomestadohc " +
																"FROM " +
																	"solicitudes_subcuenta ss " +
																	"INNER JOIN det_cargos dc ON (dc.cod_sol_subcuenta = ss.codigo) " +
																	"LEFT OUTER JOIN facturas fac ON (dc.codigo_factura = fac.codigo) " +
																	"LEFT OUTER JOIN tipos_asocio ta ON (ta.codigo = ss.tipo_asocio) " +
																"WHERE " +
																	"ss.sub_cuenta = ? " +
																	"AND es_portatil = '"+ConstantesBD.acronimoNo+"' " +
																	"AND ss.paquetizada = '"+ConstantesBD.acronimoNo+"' ";
	
	/**
	 * Cadena para consultar los ingresos/cuentas validos para la distribucion del paciente.
	 */
	public static String cadenaConsultaIngresoValidosDistribucion="SELECT " +
																		" i.id as idingreso," +
																		" i.estado as estadoingreso," +
																		" i.fecha_ingreso as fechaingreso," +
																		" i.fecha_egreso as fechaegreso," +
																		" i.consecutivo as consecutivo," +
																		" c.id as cuenta," +
																		" c.estado_cuenta as codestadocuenta," +
																		" getnombreestadocuenta(c.estado_cuenta) as nomestadocuenta, " +
																		" c.via_ingreso as codviaingreso, " +
																		" getNombreViaIngresoTipoPac(c.id) as nomviaingreso " +
																" from cuentas c " +
																" inner join ingresos i on(c.id_ingreso=i.id) " +
																" inner join centros_costo cc on(cc.codigo=c.area) " +
																" where " +
																		" c.codigo_paciente= ? and " +
																		" cc.centro_atencion=?  and " +
																		" i.estado in('"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"','"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"') and " +
																		" (c.estado_cuenta in ("+ConstantesBD.codigoEstadoCuentaActiva+","+ConstantesBD.codigoEstadoCuentaFacturadaParcial+") or (c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaAsociada+" and getEsCuentaAsocioincompleto(c.id)='S') ) ";
	
	/**
	 * 
	 */
	public static String cadenaConsultarEncabezadoDistribucion="SELECT " +
																		" tipo_distribucion as tipodistribucion," +
																		" '('||usuario_modifica||') '||getnombreusuario(usuario_modifica) as usuarioliquidacion," +
																		" fecha_modifica as fechaliquidacion," +
																		" hora_modifica as horaliquidacion " +
																" from distribucion_ingreso " +
																" where ingreso=?";

	/**
	 * 
	 */
	public static String cadenaInsertarEncabezadoDistribucion="insert into distribucion_ingreso (ingreso,tipo_distribucion,usuario_modifica,fecha_modifica,hora_modifica) values(?,?,?,?,?) ";
	
	/**
	 * 
	 */
	public static String cadenaUpdateEncabezadoDistribucion="UPDATE distribucion_ingreso set tipo_distribucion=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=? where ingreso=? ";
	
	/**
	 * 
	 */
	public static String cadenaUpdatePrioridad="UPDATE sub_cuentas set nro_prioridad=? where sub_cuenta=?";
	
	/**
	 * 
	 */
	public static String cadenaUpdateComplejidadCuenta="UPDATE cuentas set tipo_complejidad=? where id=?";
	
	/**
	 * 
	 */
	public static String cadenaConsultaFiltroDistribucion=" SELECT " +
																	" sub_cuenta as subcuenta," +
																	" via_ingreso as viaingreso," +
																	" centro_costo_solicita as ccsol," +
																	" centro_costo_ejecuta as cceje," +
																	" tipo_paciente as tipopaciente," +
																	" to_char(fecha_inicial_solicitud,'dd/mm/yyyy') as fechainicial, " +
																	" to_char(fecha_final_solicitud,'dd/mm/yyyy') as fechafinal " +
															" from filtro_distribucion " +
															" where sub_cuenta=?";
	
	/**
	 * 
	 */
	public static String cadenaUpdateResponsableGeneral="UPDATE sub_cuentas set " +
																" naturaleza_paciente=?, " +
																" monto_cobro=?, " +
																" nro_poliza=?, " +
																" nro_carnet=?, " +
																" contrato=?, " +
																" tipo_afiliado=?," +
																" clasificacion_socioeconomica=?," +
																" nro_autorizacion=?," +
																" fecha_afiliacion=?," +
																" semanas_cotizacion=?," +
																" valor_utilizado_soat=?," +
																" fecha_modifica=?, " +
																" hora_modifica=?," +
																" usuario_modifica=?," +
																" numero_solicitud_volante = ?," +
																" meses_cotizacion = ?, " +
																" tipo_cobertura = ?," +
																" tipo_cobro_paciente=?," +
																" tipo_monto_cobro=?," +
																" porcentaje_monto_cobro=? " +
															" where sub_cuenta=?";

	/**
	 * 
	 */
	public static String cadenaActualizacionPrioridadUltimoRes="UPDATE sub_cuentas set nro_prioridad=(SELECT (max(nro_prioridad)+1) from sub_cuentas where ingreso = ?) where ingreso = ? and nro_prioridad=(SELECT max(nro_prioridad) from sub_cuentas where ingreso = ?)";
	
	/**
	 * 
	 */
	public static String cadenaInsertarSubcuentaConvDefecto =" INSERT INTO sub_cuentas (" +
											" sub_cuenta," +   //1
											" convenio," + //2
											" naturaleza_paciente," + //3
											" monto_cobro," + //4
											" nro_poliza," + //5
											" nro_carnet," + //6
											" contrato," + //7
											" ingreso," + //8
											" tipo_afiliado," + //9
											" clasificacion_socioeconomica," + //10
											" nro_autorizacion," + //11
											" fecha_afiliacion," + //12
											" semanas_cotizacion," + //13
											" codigo_paciente," + //14
											" valor_utilizado_soat," + //15
											" nro_prioridad," + //
											" facturado," + //
											" porcentaje_autorizado," + //17
											" monto_autorizado," + //18
											" fecha_modifica," + //19
											" hora_modifica," + //20
											" usuario_modifica," + //21
											" numero_solicitud_volante,"+ //22
											" meses_cotizacion," + //23
											" obs_parametros_distribucion, "+//24 
											" tipo_cobertura," + //25
											" tipo_cobro_paciente," + //26 
											" tipo_monto_cobro," + //27
											" porcentaje_monto_cobro "+ //28
											
										" ) " +
										" values " +
										" (" +
										" ?,?,?,?,?," +
										" ?,?,?,?,?," +
										" ?,?,?,?,?," +
										" (SELECT (max(nro_prioridad)-1) from sub_cuentas where ingreso = ?),'"+ConstantesBD.acronimoNo+"',?,?,?, " +
										" ?,?,?,?,'', ?,?,?,?" +
										" )";

	/**
	 * 
	 */
	public static String cadenaInsertarSubcuentaSinConvDefecto =" INSERT INTO sub_cuentas (" +
											" sub_cuenta," +   //1
											" convenio," + //2
											" naturaleza_paciente," + //3
											" monto_cobro," + //4
											" nro_poliza," + //5
											" nro_carnet," + //6
											" contrato," + //7
											" ingreso," + //8
											" tipo_afiliado," + //9
											" clasificacion_socioeconomica," + //10
											" nro_autorizacion," + //11
											" fecha_afiliacion," + //12
											" semanas_cotizacion," + //13
											" codigo_paciente," + //14
											" valor_utilizado_soat," + //15
											" nro_prioridad," + //
											" facturado," + //
											" porcentaje_autorizado," + //17
											" monto_autorizado," + //17
											" fecha_modifica," + //18
											" hora_modifica," + //19
											" usuario_modifica, " + //20
											" numero_solicitud_volante,"+ //21
											" meses_cotizacion," + //22
											" obs_parametros_distribucion," + 
											" tipo_cobertura," + //23
											" tipo_cobro_paciente," + //24 
											" tipo_monto_cobro," + //25
											" porcentaje_monto_cobro "+ //26
										" ) " +
										" values " +
										" (" +
										" ?,?,?,?,?," +
										" ?,?,?,?,?," +
										" ?,?,?,?,?," +
										" coalesce((SELECT (max(nro_prioridad)+1) from sub_cuentas where ingreso = ?),1),'"+ConstantesBD.acronimoNo+"',?,?,?, " +
										" ?,?,?,?,'',?,?,?,?" +
										" )";

	
	/**
	 * 
	 */
	public static String cadenaUpdateTitularPoliza="UPDATE titular_poliza set " +
															" nombres_titular=?," +
															" apellidos_titular=?," +
															" tipoid_titular=?," +
															" numeroid_titular=?," +
															" direccion_titular=?," +
															" telefono_titular=? " +
														" where sub_cuenta=?";
	
	/**
	 * 
	 */
	public static String cadenaUpdateInfoPoliza="UPDATE informacion_poliza set " +
													" fecha_autorizacion=?," +
													" numero_autorizacion=?," +
													" valor_monto_autorizado=?," +
													" fecha_grabacion=?," +
													" hora_grabacion=?," +
													" usuario=? " +
												" where sub_cuenta=?";

	/**
	 * 
	 */
	public static String cadenaDeleteRequisitos="DELETE FROM requisitos_pac_subcuenta where  subcuenta =?";

	/**
	 * 
	 */
	public static String cadenaInsertRequisitos="INSERT INTO requisitos_pac_subcuenta(subcuenta,requisito_paciente,cumplido) values (?,?,?)";

	
	/**
	 * 
	 */
	public static String cadenaInsertTitularPoliza="INSERT INTO titular_poliza (" +
												" nombres_titular," +
												" apellidos_titular," +
												" tipoid_titular," +
												" numeroid_titular," +
												" direccion_titular," +
												" telefono_titular," +
												" sub_cuenta) " +
											" values (?,?,?,?,?,?,?)";
	
	/**
	 * 
	 */
	public static String cadenaInsertInfoPoliza="INSERT INTO informacion_poliza (" +
										" codigo," +
										" fecha_autorizacion," +
										" numero_autorizacion," +
										" valor_monto_autorizado," +
										" fecha_grabacion," +
										" hora_grabacion," +
										" usuario," +
										"sub_cuenta" +
									" ) values(?,?,?,?,?,?,?,?)";
	

	/**
	 * 
	 */
	public static String cadenaConsultaSolSubcuentaDetCargos="SELECT DISTINCT " +
																" to_char(getFechaSolicitud(dc.solicitud),'dd/mm/yyyy') AS fechasolicitud," +
																" dc.solicitud AS solicitud," +
																" getviaingresosolicitud(dc.solicitud) AS viaingreso, " +
																" gettipopacientesolicitud(dc.solicitud) AS tipopaciente, " +
																" dc.servicio AS servicio," +
																" dc.articulo AS articulo," +
																" case when gettieneportatilsolicitud(ss.solicitud,dc.servicio)=-1 THEN '' ELSE gettieneportatilsolicitud(ss.solicitud,dc.servicio)||'' end AS codigoportatil,"+
																" getnombreservicio(gettieneportatilsolicitud(ss.solicitud,dc.servicio),"+ConstantesBD.codigoTarifarioCups+") AS nomservicioportatil," +
																" dc.servicio_cx AS serviciocx," +
																" coalesce(dc.det_cx_honorarios,-1) AS detcxhonorarios," +
																" coalesce(dc.det_asocio_cx_salas_mat,-1) AS detascxsalmat, " +
																" CASE WHEN dc.tipo_solicitud="+ConstantesBD.codigoTipoSolicitudCirugia+" THEN CASE WHEN dc.servicio_cx IS NOT NULL THEN getnombreservicio(dc.servicio_cx,"+ConstantesBD.codigoTarifarioCups+") ELSE getdescripcionarticulo(dc.articulo) END else case when dc.servicio is null then getdescripcionarticulo(dc.articulo) else getnombreservicio(dc.servicio,"+ConstantesBD.codigoTarifarioCups+") END END AS nomserart, " +
																" CASE WHEN dc.servicio IS NULL THEN '"+ConstantesBD.acronimoNo+"' ELSE '"+ConstantesBD.acronimoSi+"' END AS esservicio, " +
																" dc.tipo_solicitud AS tiposolicitud," +
																" CASE WHEN dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" THEN getintegridaddominio(getIndicadorQxSolicitud(dc.solicitud)) ELSE getnomtiposolicitud(dc.tipo_solicitud) END AS nomtiposolicitud, "+																
																" dc.tipo_asocio AS tipoasocio," +
																" dc.tipo_distribucion AS tipodistribucion, " +
																" dc.tipo_distribucion AS tipodistribucionoriginal, " +
																" getnombretipoasocio(ta.codigo) AS nomtipoasocio," +
																" getConsecutivoSolicitud(dc.solicitud) AS consecutivosolicitud," +
																" getCodigoCCSolicita(dc.solicitud) AS codccsolicita," +
																" getnomcentrocosto(getCodigoCCSolicita(dc.solicitud)) AS nomccsolicita," +
																" getCodigoCCEjecuta(dc.solicitud) AS codccejecuta," +
																" getnomcentrocosto(getCodigoCCEjecuta(dc.solicitud)) AS nomccejecuta," +
																" CASE WHEN dc.servicio IS NULL THEN getCantTotalServArt(dc.solicitud,dc.articulo,'"+ConstantesBD.acronimoNo+"',dc.servicio_cx,dc.tipo_asocio,dc.det_cx_honorarios,dc.det_asocio_cx_salas_mat,dc.facturado) else getCantTotalServArt(dc.solicitud,dc.servicio,'"+ConstantesBD.acronimoNo+"',dc.servicio_cx,dc.tipo_asocio,dc.det_cx_honorarios,dc.det_asocio_cx_salas_mat,dc.facturado) END AS cantidad," +
																" getcodigoestadohcsol(dc.solicitud) AS codestadohc, " +
																" getestadosolhis(getcodigoestadohcsol(dc.solicitud)) AS nomestadohc ," +
																" getNumRespSolServicio(dc.solicitud,dc.servicio) AS numresservicio, " +
																" getNumRespFacSolServicio(dc.solicitud,dc.servicio) AS numresfactservicio," +
																" getNumRespSolArticulo(dc.solicitud,dc.articulo) AS numresarticulo," +
																" getNumRespFacSolArticulo(dc.solicitud,dc.articulo) AS numresfactarticulo," +
																" dc.paquetizado AS paquetizada, " +
																" ss.sol_subcuenta_padre AS solsubcuentapadre, " +
																" dc.estado as estadocargo," +
																" getestadosolfac(dc.estado) as nomestadocargo," +
																" coalesce(dc.nro_autorizacion,'') as nro_autorizacion " +
																
															" FROM det_cargos dc " +
															" INNER JOIN solicitudes_subcuenta ss ON (ss.codigo = dc.cod_sol_subcuenta) "+
															" LEFT OUTER JOIN tipos_asocio ta ON (ta.codigo = dc.tipo_asocio)  " +
															" WHERE ss.eliminado='"+ConstantesBD.acronimoNo+"' AND dc.paquetizado='"+ConstantesBD.acronimoNo+"' and ((dc.servicio is not null and getCantTotalServArt(dc.solicitud,dc.servicio,'"+ConstantesBD.acronimoNo+"',dc.servicio_cx,dc.tipo_asocio,dc.det_cx_honorarios,dc.det_asocio_cx_salas_mat,dc.facturado) > 0) or (dc.articulo is not null and getCantTotalServArt(dc.solicitud,dc.articulo,'"+ConstantesBD.acronimoNo+"',dc.servicio_cx,dc.tipo_asocio,dc.det_cx_honorarios,dc.det_asocio_cx_salas_mat,dc.facturado)>0)) ";
															//" where  ss.eliminado='"+ConstantesBD.acronimoNo+"' and dc.paquetizado='"+ConstantesBD.acronimoNo+"' and dc.cantidad_cargada>0  and (dc.servicio is not null or dc.articulo is not null) ";
	
	/**
	 * Igual, a a la consulta anterior petro trae tambine el estado del cargo, es decir la consulta es mas detallada, y en caso de repetir el cargo
	 * en diferente estado no lo agrupara( se presenta este caso cuando el cargo se anula parcialmente.)
	 */
	public static String cadenaConsultaSolSubcuentaDetCargos2="SELECT DISTINCT " +
																" to_char(getFechaSolicitud(dc.solicitud),'dd/mm/yyyy') as fechasolicitud," +
																" dc.solicitud as solicitud," +
																" getviaingresosolicitud(dc.solicitud) as viaingreso, " +
																" gettipopacientesolicitud(dc.solicitud) as tipopaciente, " +
																" dc.servicio as servicio," +
																" dc.articulo as articulo," +
																" case when gettieneportatilsolicitud(ss.solicitud,dc.servicio)=-1 then '' else gettieneportatilsolicitud(ss.solicitud,dc.servicio)||'' end AS codigoportatil,"+
																" getnombreservicio(gettieneportatilsolicitud(ss.solicitud,dc.servicio),"+ConstantesBD.codigoTarifarioCups+") as nomservicioportatil," +
																" dc.servicio_cx as serviciocx," +
																" coalesce(dc.det_cx_honorarios,-1) as detcxhonorarios," +
																" coalesce(dc.det_asocio_cx_salas_mat,-1) as detascxsalmat, " +
																" case when dc.tipo_solicitud="+ConstantesBD.codigoTipoSolicitudCirugia+" then  CASE WHEN dc.servicio_cx IS NOT NULL THEN getnombreservicio(dc.servicio_cx,"+ConstantesBD.codigoTarifarioCups+") else getdescripcionarticulo(dc.articulo) END  else case when dc.servicio is null then getdescripcionarticulo(dc.articulo) else getnombreservicio(dc.servicio,"+ConstantesBD.codigoTarifarioCups+") end end as nomserart, " +
																" case when dc.servicio is null then '"+ConstantesBD.acronimoNo+"' else '"+ConstantesBD.acronimoSi+"' end as esservicio, " +
																" dc.tipo_solicitud as tiposolicitud," +
																" CASE WHEN dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" THEN getintegridaddominio(getIndicadorQxSolicitud(dc.solicitud)) ELSE getnomtiposolicitud(dc.tipo_solicitud) END AS nomtiposolicitud, "+															
																" dc.tipo_asocio as tipoasocio," +
																" dc.tipo_distribucion as tipodistribucion, " +
																" dc.tipo_distribucion as tipodistribucionoriginal, " +
																" getnombretipoasocio(ta.codigo) as nomtipoasocio," +
																" getConsecutivoSolicitud(dc.solicitud) as consecutivosolicitud," +
																" getCodigoCCSolicita(dc.solicitud) as codccsolicita," +
																" getnomcentrocosto(getCodigoCCSolicita(dc.solicitud)) as nomccsolicita," +
																" getCodigoCCEjecuta(dc.solicitud) as codccejecuta," +
																" getnomcentrocosto(getCodigoCCEjecuta(dc.solicitud)) as nomccejecuta," +
																" case " +
																	" when dc.estado="+ConstantesBD.codigoEstadoFAnulada+" or dc.estado= "+ConstantesBD.codigoEstadoFInactiva+" then cantidad_cargada" +
																	" when dc.servicio is null then getCantTotalServArt(dc.solicitud,dc.articulo,'"+ConstantesBD.acronimoNo+"',dc.servicio_cx,dc.tipo_asocio,dc.det_cx_honorarios,dc.det_asocio_cx_salas_mat,dc.facturado) " +
																	" else getCantTotalServArt(dc.solicitud,dc.servicio,'"+ConstantesBD.acronimoNo+"',dc.servicio_cx,dc.tipo_asocio,dc.det_cx_honorarios,dc.det_asocio_cx_salas_mat,dc.facturado) " +
																" end as cantidad," +
																" getcodigoestadohcsol(dc.solicitud) as codestadohc, " +
																" getestadosolhis(getcodigoestadohcsol(dc.solicitud)) as nomestadohc ," +
																" getNumRespSolServicio(dc.solicitud,dc.servicio) as numresservicio, " +
																" getNumRespFacSolServicio(dc.solicitud,dc.servicio) as numresfactservicio," +
																" getNumRespSolArticulo(dc.solicitud,dc.articulo) as numresarticulo," +
																" getNumRespFacSolArticulo(dc.solicitud,dc.articulo) as numresfactarticulo," +
																" dc.paquetizado as paquetizada, " +
																" ss.sol_subcuenta_padre as solsubcuentapadre," +
																" dc.estado as estadocargo," +
																" getestadosolfac(dc.estado) as nomestadocargo, " +
																" coalesce(dc.nro_autorizacion,'') as nro_autorizacion " +
															" from det_cargos dc " +
															" inner join solicitudes_subcuenta ss on(ss.codigo=dc.cod_sol_subcuenta) "+
															" left outer join tipos_asocio ta on (ta.codigo=dc.tipo_asocio)  " +
															" where cantidad_cargada is not null and ss.eliminado='"+ConstantesBD.acronimoNo+"' and dc.paquetizado='"+ConstantesBD.acronimoNo+"' and dc.facturado='"+ConstantesBD.acronimoNo+"'";
															//" where cantidad_cargada is not null and ss.eliminado='"+ConstantesBD.acronimoNo+"' and dc.paquetizado='"+ConstantesBD.acronimoNo+"' and dc.facturado='"+ConstantesBD.acronimoNo+"'";

	
	private  final static String consultarListadoIngresos = "SELECT " +
							" i.id As id_ingreso0, " +
							" i.consecutivo As consecutivo_ingreso1, " +
							" i.centro_atencion As centro_atencion2," +
							" getnomcentroatencion(i.centro_atencion) As nombre_centro_atencion3, " +
							" i.estado As estado_ingreso4, " +
							" to_char(i.fecha_ingreso,'DD/MM/YYYY') || ' ' || i.hora_ingreso As fecha_apertura_ingreso5, " +
							" c.via_ingreso As via_ingreso6, " +
							" getnombreviaingreso(c.via_ingreso) As nombre_via_ingreso7, " +
							" coalesce(to_char(i.fecha_egreso,'DD/MM/YYYY'),'')||' '||coalesce(substr(i.hora_egreso,0,6), '') As fecha_cierre_ingreso8, " +
							" c.id As numero_cuenta9, " +
							" c.estado_cuenta AS estado_cuenta10, " +
							" getnombreestadocuenta(c.estado_cuenta) As nombre_estado_cuenta11 " +
						" FROM ingresos i " +
						" INNER JOIN cuentas c ON (c.id_ingreso=i.id) " +
						" WHERE i.codigo_paciente=?" +
						" AND i.institucion=? " +
						" AND i.estado IN ('"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"','"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"') " +
						" ORDER BY consecutivo_ingreso1 DESC ";
	
	
	/**
	 * Insertar el estado de la cuenta en el momento de iniciar el proceso de
	 * distribucion
	 */
	private static final String procesoDistribucionStr = " INSERT INTO cuentas_proceso_dist(cuenta, estado, usuario, fecha, hora) "+
														 " VALUES(?,(SELECT estado_cuenta from cuentas where id=? " +
														 " AND estado_cuenta!=" + ConstantesBD.codigoEstadoCuentaProcesoDistribucion+ "), ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+")";
	
	/**
	 * Insertar el estado de la cuenta en el momento de iniciar el proceso de
	 * distribucion
	 */
	private static final String ingresoProcesoDistribucionStr = " INSERT INTO ingresos_procesos_distribucion(id_ingreso, usuario, fecha, hora) "+
														 " VALUES(?, ?, CURRENT_DATE, ?)";
	

	
	/**
	 * String que actualiza el estado de la cuenta al que tenï¿½a en el momento de
	 * iniicar el proceso de distribucion
	 */
	private static final String cancelarProcesoDistribucionStr = "UPDATE cuentas SET estado_cuenta=(SELECT estado FROM cuentas_proceso_dist WHERE cuenta=?) WHERE id=?";
	
	/**
	 * Cambiar el estado de una cuenta a "Cuenta en proceso de Distribucion" dado si ID
	 */
	private static final String cuentaEnProcesoDistribucionStr="UPDATE cuentas SET estado_cuenta="+ConstantesBD.codigoEstadoCuentaProcesoDistribucion+" WHERE id=?";

	/**
	 * Sentencia que busca todas las cuentas que estan en proceso de distribucion
	 */
	private static final String cuentasEnProcesoStr = "SELECT cuenta AS cuentas FROM facturacion.cuentas_proceso_dist";
	
	/**
	 * Verificar si existe asocio y solo se facturï¿½ la cuenta de urgencias
	 */
	private static final String verificarCuentasAsocioStr = "SELECT ac.cuenta_inicial as cuenta FROM cuentas c INNER JOIN asocios_cuenta ac ON(ac.cuenta_final=c.id) WHERE c.estado_cuenta="
																					+ ConstantesBD.codigoEstadoCuentaProcesoDistribucion
																					+ " AND ac.cuenta_inicial=?";
	
	/**
	 * String que elimina de la tabla de cuentas_proceso_dist de acuerdo al
	 * nï¿½mero de la cuenta
	 */
	private static final String terminarProcesoDistribucionStr = "DELETE from cuentas_proceso_dist WHERE cuenta=?";

	/**
	 * 
	 */
	private static final String cadenaConsultaCantFacturadasSub="SELECT case when dc.servicio is null then dc.sub_cuenta||'_'||dc.articulo else dc.sub_cuenta||'_'||dc.servicio end as subserart,sum(dc.cantidad_cargada) as cantidad from det_cargos dc inner join sub_cuentas sc on(dc.sub_cuenta=sc.sub_cuenta) where sc.ingreso=? and dc.facturado='S' and eliminado='N' group by dc.servicio,dc.articulo,dc.sub_cuenta";
	
	/**
	 * 
	 */
	private final static String consultaServiciosArticulos=" SELECT " +
							" dc.solicitud As solicitud0, " +
							" s.consecutivo_ordenes_medicas As consecutivo_ordenes1, " +
							" s.tipo As tipo_solicitud2, " +
							" getnomtiposolicitud(s.tipo) As nombre_tipo_solicitud3, " +
							" s.estado_historia_clinica As estado_historia_clinica4, " +
							" getestadosolhis(s.estado_historia_clinica) As nomestadohiscli5, " +
							" s.centro_costo_solicitante As centro_costo_solicita6, " +
							" getnomcentrocosto(s.centro_costo_solicitante) As nombre_centro_costo_solicita7, " +
							" s.centro_costo_solicitado As centro_costo_ejecuta8, " +
							" getnomcentrocosto(s.centro_costo_solicitado) As nombre_centro_costo_ejecuta9, " +
							" sc.porcentaje_autorizado As porcentaje_autorizado10, " +
							" sc.monto_autorizado As monto_autorizado11, " +
							" coalesce(dc.valor_unitario_cargado,0) As valor_unitario12, " +
							" coalesce(dc.valor_total_cargado,0) As valor_total13, " +
							" dc.estado As estado_cargo14, " +
							" getestadosolfac(dc.estado) As nombre_estado_cargo15, " +
							" coalesce(to_char(s.fecha_solicitud,'DD/MM/YYYY'),'')||' '||coalesce(substr(s.hora_solicitud,0,6),'') As fecha_solicitud16, " +
							" case when dc.servicio is null then getdescripcionarticulo(dc.articulo) else getobtenercodigocupsserv (dc.servicio,"+ConstantesBD.codigoTarifarioCups+") ||' - '||getnombreservicio(dc.servicio,"+ConstantesBD.codigoTarifarioCups+") end As serv_articulo17, " +
							" sum(dc.cantidad_cargada) as cantidad18, " +
							" coalesce(dc.requiere_autorizacion,'') As requiere_autorizacion19, " +
							//" CASE WHEN dc.nro_autorizacion IS NULL AND dc.servicio IS NOT NULL THEN getAutorizacionSolicitud(dc.solicitud,gettiposervicio(dc.servicio),dc.servicio,s.tipo) ELSE coalesce(dc.nro_autorizacion,'') END As numero_autorizacion20, " +
							" dc.codigo_detalle_cargo As codigo_detalle_cargo21, " +
							" getnombretipoasocio(dc.tipo_asocio) as nombre_tipo_asocio24, " +
							" getnombreservicio(dc.servicio_cx,0) As servicio_cx25, " +
							" CASE WHEN s.tipo="+ConstantesBD.codigoTipoSolicitudCirugia+" AND dc.articulo is not null THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END As es_material_especial26, " +
							" coalesce(a.codigo,"+ConstantesBD.codigoNuncaValido+") as cod_autorizacion27, " +
							" coalesce(da.codigo,"+ConstantesBD.codigoNuncaValido+") as cod_det_autorizacion28, " +
							" coalesce(da.estado,'') as estado_autorizacion29, " +
							" coalesce(a.tipo_tramite,'') as tipo_tramite30," +
							" s.urgente as urgente31, " +
							" coalesce(ra.vigencia,0) as vigencia, " +
							" coalesce(to_char(ra.fecha_autorizacion,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha_autorizacion36, "+
							" coalesce(ra.hora_autorizacion,'') as hora_autorizacion37, "+
							" coalesce(tv.tipo,'') as tipo_vigencia," +
							" sc.sub_cuenta as id_sub_cuenta34, " +
							" dc.convenio as codigo_convenio35," +
							" coalesce(s.codigo_medico,"+ConstantesBD.codigoNuncaValido+") AS medico_sol38," +
							" CASE WHEN s.codigo_medico IS NULL THEN '' ELSE getnombrepersona(s.codigo_medico) END AS nombre_medico_sol39," +
							" CASE WHEN ra.fecha_fin_autorizada IS NULL THEN '' ELSE to_char(ra.fecha_fin_autorizada,'dd/mm/yyyy') END AS fecha_fin_autoriza " +
						" FROM det_cargos dc " +
						" INNER JOIN solicitudes s ON (s.numero_solicitud=dc.solicitud)" +
						" INNER JOIN sub_cuentas sc ON (sc.sub_cuenta=dc.sub_cuenta) " +
						" LEFT OUTER JOIN det_autorizaciones da ON(da.det_cargo = dc.codigo_detalle_cargo and da.activo = '"+ConstantesBD.acronimoSi+"') " +
						" LEFT OUTER JOIN autorizaciones a ON(a.codigo = da.autorizacion ) " +
						" LEFT OUTER JOIN resp_autorizaciones ra ON(ra.det_autorizacion = da.codigo) " +
						" LEFT OUTER JOIN tipos_vigencia tv ON(tv.codigo = ra.tipo_vigencia) " ;
	
	
	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public static HashMap<String, Object> consultarIngresosValidosDistribucion(Connection con, int codigoPersona,int codigoCentroAtencion) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaIngresoValidosDistribucion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoPersona);
			ps.setInt(2, codigoCentroAtencion);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			ps.close();
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR AL EJECUTAR LA CONSULTA DE INGRESOS VALIDOS PARA LA DISTRIBUCION "+e);
			e.printStackTrace();
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap<String, Object> consultarEncabezadoUltimaDistribucion(Connection con, int codigoIngreso) throws BDException
	{
		HashMap mapa=new HashMap();
		PreparedStatement pst=null;
		ResultSet rs= null;
		
		try {
			pst= con.prepareStatement(cadenaConsultarEncabezadoDistribucion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoIngreso);
			rs=pst.executeQuery();
			int cont=0;
			mapa.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					String alias=rsm.getColumnLabel(i).toLowerCase();
					mapa.put(alias, rs.getObject(rsm.getColumnLabel(i))==null?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapa.put("numRegistros", cont+"");
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
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean actualizarEncabezadoDistribucion(Connection con, HashMap vo) throws BDException
	{
		Log4JManager.info("############## Inicio actualizarEncabezadoDistribucion");
		boolean resultado=false;
		PreparedStatement pst=null;
		
		try {
			pst= con.prepareStatement(cadenaUpdateEncabezadoDistribucion);
			pst.setString(1, vo.get("tipoDistribucion")+"");
			pst.setString(2, vo.get("usuarioModifica")+"");
			pst.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaModifica")+"")));
			pst.setString(4, vo.get("horaModifica")+"");
			pst.setInt(5, Utilidades.convertirAEntero(vo.get("ingreso")+"",false));
			resultado=pst.executeUpdate()>0;
		} 
		catch(SQLException sqe)	{
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin actualizarEncabezadoDistribucion");
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean insertarEncabezadoDistribucion(Connection con, HashMap vo) throws BDException
	{
		Log4JManager.info("############## Inicio insertarEncabezadoDistribucion");
		boolean resultado=false;
		PreparedStatement pst=null;
		
		try {
			pst= con.prepareStatement(cadenaInsertarEncabezadoDistribucion);
			pst.setInt(1, Utilidades.convertirAEntero(vo.get("ingreso")+"",false));
			pst.setString(2, vo.get("tipoDistribucion")+"");
			pst.setString(3, vo.get("usuarioModifica")+"");
			pst.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaModifica")+"")));
			pst.setString(5, vo.get("horaModifica")+"");
			resultado=pst.executeUpdate()>0;
		} 
		catch(SQLException sqe)	{
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin insertarEncabezadoDistribucion");
		return resultado;
	}
	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @param nroPrioridad
	 * @return
	 */
	public static boolean actualizarPrioridadResponsable(Connection con, String subCuenta, int nroPrioridad) throws BDException
	{
		boolean resultado=false;
		try {
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaUpdatePrioridad));
			ps.setInt(1, nroPrioridad);
			ps.setLong(2, Utilidades.convertirALong(subCuenta));
			resultado=ps.executeUpdate()>0;
			ps.close();
		} 
		catch (SQLException sqe) {
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public static boolean actualizarTipoComplejidadCuenta(Connection con, int tipoComplejidad, int cuenta) throws BDException
	{
		boolean resultado=false;
		try {
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaUpdateComplejidadCuenta));
			if(tipoComplejidad>0)
				ps.setInt(1, tipoComplejidad);
			else
				ps.setObject(1, null);
			ps.setInt(2, cuenta);
			resultado=ps.executeUpdate()>0;
			ps.close();
		} 
		catch (SQLException sqe) {
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap consultarFiltroDistribucionResponsable(Connection con, String subCuenta) throws BDException
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try {
			Log4JManager.info("############## Inicio consultarFiltroDistribucionResponsable");
			pst= con.prepareStatement(cadenaConsultaFiltroDistribucion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setLong(1, Utilidades.convertirALong(subCuenta));
			rs=pst.executeQuery();
			ResultSetMetaData rsm=rs.getMetaData();
			int cont=0;
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++){
					String alias=rsm.getColumnLabel(i).toLowerCase();
					mapa.put(alias, rs.getObject(rsm.getColumnLabel(i))==null?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapa.put("numRegistros", cont+"");
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
				Log4JManager.error("###########  Error close PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin consultarFiltroDistribucionResponsable");
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param voInfoIngreso
	 * @param voRequisitosPaciente
	 * @param usuario 
	 * @return
	 */
	public  static boolean modificarSubCuenta(Connection con, HashMap voInfoIngreso, HashMap voRequisitosPaciente, String usuario, HashMap voVerificacionDerechos) 
	{
		boolean exitoso=false;
		try 
		{
			Date fechaActual=Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con)));
			String horaActual=UtilidadFecha.getHoraActual(con);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaUpdateResponsableGeneral,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE sub_cuentas set " +
																" naturaleza_paciente=?, " +
																" monto_cobro=?, " +
																" nro_poliza=?, " +
																" nro_carnet=?, " +
																" contrato=?, " +
																" tipo_afiliado=?," +
																" clasificacion_socioeconomica=?," +
																" nro_autorizacion=?," +
																" fecha_afiliacion=?," +
																" semanas_cotizacion=?," +
																" valor_utilizado_soat=?," +
																" fecha_modifica=?, " +
																" hora_modifica=?," +
																" usuario_modifica=?," +
																" numero_solicitud_volante = ? " +
															" where sub_cuenta=?";
			 */
			
			if(Utilidades.convertirAEntero(voInfoIngreso.get("codigoNaturaleza")+"")>0)
				ps.setInt(1, Utilidades.convertirAEntero(voInfoIngreso.get("codigoNaturaleza")+""));
			else
				ps.setObject(1, null);

			if(Utilidades.convertirAEntero(voInfoIngreso.get("codigoMontoCobro")+"")>0)
				ps.setInt(2, Utilidades.convertirAEntero(voInfoIngreso.get("codigoMontoCobro")+""));
			else 
				ps.setObject(2,null);

			
			if(UtilidadTexto.isEmpty(voInfoIngreso.get("numeroPoliza")+""))
				ps.setObject(3, null);
			else
				ps.setString(3, voInfoIngreso.get("numeroPoliza")+"");
			
			if(UtilidadTexto.isEmpty(voInfoIngreso.get("numeroCarnet")+""))
				ps.setObject(4, null);
			else
				ps.setString(4, voInfoIngreso.get("numeroCarnet")+"");
			
			ps.setInt(5, Utilidades.convertirAEntero(voInfoIngreso.get("codigoContrato")+""));
			
			if(!UtilidadTexto.isEmpty(voInfoIngreso.get("codigoTipoAfiliado")+""))
				ps.setString(6, voInfoIngreso.get("codigoTipoAfiliado")+"");
			else
				ps.setObject(6, null);
			if(Utilidades.convertirAEntero(voInfoIngreso.get("codigoEstratoSocial")+"")>0)
				ps.setInt(7, Utilidades.convertirAEntero(voInfoIngreso.get("codigoEstratoSocial")+""));
			else
				ps.setObject(7,null);
			
			if(UtilidadTexto.isEmpty(voInfoIngreso.get("autorizacionIngreso")+""))
				ps.setObject(8, null);
			else
				ps.setString(8, voInfoIngreso.get("autorizacionIngreso")+"");
			
			if(UtilidadTexto.isEmpty(voInfoIngreso.get("fechaAfiliacion")+""))
				ps.setObject(9, null);
			else
				ps.setDate(9, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(voInfoIngreso.get("fechaAfiliacion")+"")) );
			
			if(UtilidadTexto.isEmpty(voInfoIngreso.get("semanasCotizacion")+""))
				ps.setObject(10, null);
			else
				ps.setInt(10, Utilidades.convertirAEntero(voInfoIngreso.get("semanasCotizacion")+""));
			
			if(UtilidadTexto.isEmpty(voInfoIngreso.get("valorUtilizadoSoat")+""))
				ps.setObject(11, null);
			else
				ps.setDouble(11, Utilidades.convertirADouble(voInfoIngreso.get("valorUtilizadoSoat")+""));
			
			ps.setDate(12, fechaActual);
			
			ps.setString(13,horaActual);
			
			ps.setString(14, usuario);
			
			if(!UtilidadTexto.isEmpty(voInfoIngreso.get("numeroSolicitudVolante")+""))
				ps.setLong(15,Utilidades.convertirALong(voInfoIngreso.get("numeroSolicitudVolante")+""));
			else
				ps.setNull(15,Types.NUMERIC);
			
			if(UtilidadTexto.isEmpty(voInfoIngreso.get("mesesCotizacion")+""))
				ps.setObject(16, null);
			else
				ps.setInt(16, Utilidades.convertirAEntero(voInfoIngreso.get("mesesCotizacion")+""));
			
			if(Utilidades.convertirAEntero(voInfoIngreso.get("codigoTipoCobertura")+"")>0)
			{
				ps.setInt(17,Utilidades.convertirAEntero(voInfoIngreso.get("codigoTipoCobertura")+""));
			}
			else
			{
				ps.setNull(17,Types.INTEGER);
			}
			

			
			if(!UtilidadTexto.isEmpty(voInfoIngreso.get("tipoCobroPaciente")+""))
				ps.setString(18,voInfoIngreso.get("tipoCobroPaciente")+"");
			else
				ps.setObject(18,null);
			
			if(!UtilidadTexto.isEmpty(voInfoIngreso.get("tipoMontoCobro")+""))
				ps.setString(19,voInfoIngreso.get("tipoMontoCobro")+"");
			else
				ps.setObject(19,null);
			
			if(Utilidades.convertirAEntero(voInfoIngreso.get("porcentajeMontoCobro")+"")>0)
				ps.setDouble(20,Utilidades.convertirAEntero(voInfoIngreso.get("porcentajeMontoCobro")+""));
			else
				ps.setNull(20,Types.NUMERIC);
			
			
			ps.setLong(21, Utilidades.convertirALong(voInfoIngreso.get("responsable")+""));
			
			exitoso=ps.executeUpdate()>0;
			if(exitoso&&UtilidadTexto.getBoolean(voInfoIngreso.get("esConvenioPoliza")+""))
			{
				ps= new PreparedStatementDecorator(con.prepareStatement(cadenaUpdateTitularPoliza,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				/**
				 * UPDATE titular_poliza set " +
											" nombres_titular=?," +
											" apellidos_titular=?," +
											" tipoid_titular=?," +
											" numeroid_titular=?," +
											" direccion_titular=?," +
											" telefono_titular=? " +
											" where sub_cuenta=?
				 */
				
				ps.setString(1, voInfoIngreso.get("nombresPoliza")+"");
				ps.setString(2, voInfoIngreso.get("apellidosPoliza")+"");
				ps.setString(3, voInfoIngreso.get("tipoIdPoliza")+"");
				ps.setString(4, voInfoIngreso.get("numeroIdPoliza")+"");
				ps.setString(5, voInfoIngreso.get("direccionPoliza")+"");
				ps.setString(6, voInfoIngreso.get("telefonoPoliza")+"");
				ps.setLong(7, Utilidades.convertirALong(voInfoIngreso.get("responsable")+""));
				exitoso=ps.executeUpdate()>0;
				if(exitoso)
				{
					ps= new PreparedStatementDecorator(con.prepareStatement(cadenaUpdateInfoPoliza,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
					/**
					 * UPDATE informacion_poliza set " +
													" fecha_autorizacion=?," +
													" numero_autorizacion=?," +
													" valor_monto_autorizado=?," +
													" fecha_grabacion=?," +
													" hora_grabacion=?," +
													" usuario=? " +
												" where sub_cuenta=?
					 */
					
					ps.setDate(1, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(voInfoIngreso.get("fechaAutorizacionPoliza")+"")));
					ps.setString(2, voInfoIngreso.get("autorizacionPoliza")+"");
					ps.setDouble(3, Utilidades.convertirADouble(voInfoIngreso.get("valorPoliza")+""));
					ps.setDate(4, fechaActual);
					ps.setString(5,horaActual);
					ps.setString(6, usuario);
					ps.setLong(7, Utilidades.convertirALong(voInfoIngreso.get("responsable")+""));
					exitoso=ps.executeUpdate()>0;
				}
				
				ps.close();
			}
			if(exitoso){
				HashMap<String, Object> mapa=consultarVerificacionDerechos(con, Utilidades.convertirAEntero(voInfoIngreso.get("responsable")+""));
				if(mapa.get("numRegistros") == null || Integer.valueOf(mapa.get("numRegistros").toString()).intValue()==0){
					exitoso=insertarVerificacionDerechos(con, Utilidades.convertirAEntero(voInfoIngreso.get("responsable")+""), 
															Utilidades.convertirAEntero(voInfoIngreso.get("ingreso")+""),
															Utilidades.convertirAEntero(voInfoIngreso.get("codigoConvenio")+""),
															voVerificacionDerechos, fechaActual, horaActual, usuario);
				}
				else{
					exitoso=actualizarVerificacionDerechos(con, Utilidades.convertirAEntero(voInfoIngreso.get("responsable")+""), voVerificacionDerechos, fechaActual, horaActual, usuario);
				}
			}
			if(exitoso){
				exitoso=insertarModificarRequisitos(con,voInfoIngreso.get("responsable")+"",voRequisitosPaciente);
			}
			
			ps.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return exitoso;
	}

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @param voRequisitosPaciente
	 * @return
	 */
	private static boolean insertarModificarRequisitos(Connection con, String subCuenta, HashMap voRequisitosPaciente) 
	{
		PreparedStatementDecorator ps1 = null;
		PreparedStatementDecorator ps2 = null;
		
		try 
		{
			ps1 = new PreparedStatementDecorator(con.prepareStatement(cadenaDeleteRequisitos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps1.setInt(1, Utilidades.convertirAEntero(subCuenta));
			ps1.executeUpdate();
			
			for(int a=0;a<Utilidades.convertirAEntero(voRequisitosPaciente.get("numRegistros")+"", false);a++)
			{
				/**
				 * INSERT INTO requisitos_pac_subcuenta(subcuenta,requisito_paciente,cumplido) values (?,?,?)
				 */
				ps2 = new PreparedStatementDecorator(con.prepareStatement(cadenaInsertRequisitos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps2.setInt(1, Utilidades.convertirAEntero(subCuenta));
				ps2.setInt(2, Utilidades.convertirAEntero(voRequisitosPaciente.get("codigo_"+a)+""));
				ps2.setBoolean(3, UtilidadTexto.getBoolean(voRequisitosPaciente.get("cumplido_"+a)+""));
				if(ps2.executeUpdate() <= 0 ) {
					ps2.close();
					return false;
				}
				ps2.close();
				ps2 = null;
			}
		} 
		catch (SQLException e) {
			Log4JManager.error("ERROR en insertarModificarRequisitos : " + e);
		} finally {
			try {
				if(ps1 != null) {
					ps1.close();
				}
			} catch (SQLException e) {
				Log4JManager.error("ERROR cerrando objeto persistente : " + e);
			}
		}
		return true;
	}

	/**
	 * 
	 * @param con
	 * @param codigoSubcuenta 
	 * @param voInfoIngreso
	 * @param voRequisitosPaciente
	 * @param usuario
	 * @return
	 */
	public static boolean insertarSubCuenta(Connection con, int codigoSubcuenta, HashMap voInfoIngreso, HashMap voRequisitosPaciente, String usuario,int viaIngreso, HashMap voVerificacionDerechos) 
	{
		boolean exitoso=true;
		try
		{
			boolean tieneConDef=tieneConvenioDefectoViaIngreso(con,viaIngreso,voInfoIngreso.get("ingreso")+"");
			
			if(tieneConDef)
			{
				PreparedStatementDecorator psTemp= new PreparedStatementDecorator(con.prepareStatement(cadenaActualizacionPrioridadUltimoRes,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				/**
				 * UPDATE sub_cuentas set 
				 * nro_prioridad=(SELECT (max(nro_prioridad)+1) 
				 * from sub_cuentas 
				 * where ingreso = ?) 
				 * where ingreso = ? 
				 * and nro_prioridad=(SELECT max(nro_prioridad) from sub_cuentas where ingreso = ?)
				 */
				
				psTemp.setInt(1,Utilidades.convertirAEntero(voInfoIngreso.get("ingreso")+""));
				psTemp.setInt(2,Utilidades.convertirAEntero(voInfoIngreso.get("ingreso")+""));
				psTemp.setInt(3,Utilidades.convertirAEntero(voInfoIngreso.get("ingreso")+""));
			
				exitoso=psTemp.executeUpdate()>0;
				
				psTemp.close();
			}
			if(exitoso)
			{
				PreparedStatementDecorator ps=null;
				if(tieneConDef)
					ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarSubcuentaConvDefecto,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				else
					ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarSubcuentaSinConvDefecto,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				
				/**
				 * INSERT INTO sub_cuentas (" +
											" sub_cuenta," +   //1
											" convenio," + //2
											" naturaleza_paciente," + //3
											" monto_cobro," + //4
											" nro_poliza," + //5
											" nro_carnet," + //6
											" contrato," + //7
											" ingreso," + //8
											" tipo_afiliado," + //9
											" clasificacion_socioeconomica," + //10
											" nro_autorizacion," + //11
											" fecha_afiliacion," + //12
											" semanas_cotizacion," + //13
											" codigo_paciente," + //14
											" valor_utilizado_soat," + //15
											" nro_prioridad," + //
											" facturado," + //
											" porcentaje_autorizado," + //16
											" monto_autorizado," + //17
											" fecha_modifica," + //18
											" hora_modifica," + //19
											" usuario_modifica, " + //20
											" numero_solicitud_volante"+ //21
										" ) " +
										" values " +
										" (" +
										" ?,?,?,?,?," +
										" ?,?,?,?,?," +
										" ?,?,?,?,?," +
										" (SELECT (max(nro_prioridad)+1) from sub_cuentas where ingreso = ?),'"+ConstantesBD.acronimoNo+"',?,?,?, " +
										" ?,?,?" +
										" )
			forma.setInfoIngreso("tipoCobroPaciente",validacion.getTipoCobroPaciente()+"");
			forma.setInfoIngreso("tipoMontoCobro","");
			forma.setInfoIngreso("porcentajeMontoCobro","");
				forma.setInfoIngreso("codigoEstratoSocial",ConstantesBD.codigoNuncaValido+"");
				forma.setInfoIngreso("codigoTipoAfiliado","");
				forma.setInfoIngreso("codigoNaturaleza",ConstantesBD.codigoNuncaValido+"");

				forma.setInfoIngreso("codigoMontoCobro",ConstantesBD.codigoNuncaValido+"");
				forma.setInfoIngreso("porcentajeMontoCobro",validacion.getPorcentajeMontoCobro()+"");
			
			
				 */
				
				
				
				ps.setInt(1,codigoSubcuenta);
				ps.setInt(2, Utilidades.convertirAEntero(voInfoIngreso.get("codigoConvenio")+""));
				if(Utilidades.convertirAEntero(voInfoIngreso.get("codigoNaturaleza")+"")>0)
					ps.setInt(3, Utilidades.convertirAEntero(voInfoIngreso.get("codigoNaturaleza")+""));
				else
					ps.setObject(3, null);
				if(Utilidades.convertirAEntero(voInfoIngreso.get("codigoMontoCobro")+"")>0)
					ps.setInt(4, Utilidades.convertirAEntero(voInfoIngreso.get("codigoMontoCobro")+""));
				else 
					ps.setObject(4,null);
				if(UtilidadTexto.isEmpty(voInfoIngreso.get("numeroPoliza")+""))
					ps.setObject(5, null);
				else
					ps.setString(5, voInfoIngreso.get("numeroPoliza")+"");
				
				if(UtilidadTexto.isEmpty(voInfoIngreso.get("numeroCarnet")+""))
					ps.setObject(6, null);
				else
					ps.setString(6, voInfoIngreso.get("numeroCarnet")+"");
				
				ps.setInt(7, Utilidades.convertirAEntero(voInfoIngreso.get("codigoContrato")+""));
				ps.setInt(8, Utilidades.convertirAEntero(voInfoIngreso.get("ingreso")+""));
				
				if(!UtilidadTexto.isEmpty(voInfoIngreso.get("codigoTipoAfiliado")+""))
					ps.setString(9, voInfoIngreso.get("codigoTipoAfiliado")+"");
				else
					ps.setObject(9, null);
				if(Utilidades.convertirAEntero(voInfoIngreso.get("codigoEstratoSocial")+"")>0)
					ps.setInt(10, Utilidades.convertirAEntero(voInfoIngreso.get("codigoEstratoSocial")+""));
				else
					ps.setObject(10,null);
				
				
				if(UtilidadTexto.isEmpty(voInfoIngreso.get("autorizacionIngreso")+""))
					ps.setObject(11, null);
				else
					ps.setString(11, voInfoIngreso.get("autorizacionIngreso")+"");
				
				if(UtilidadTexto.isEmpty(voInfoIngreso.get("fechaAfiliacion")+""))
					ps.setObject(12, null);
				else
					ps.setDate(12, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(voInfoIngreso.get("fechaAfiliacion")+"")) );
				
				if(UtilidadTexto.isEmpty(voInfoIngreso.get("semanasCotizacion")+""))
					ps.setObject(13, null);
				else
					ps.setInt(13, Utilidades.convertirAEntero(voInfoIngreso.get("semanasCotizacion")+""));
				
				ps.setInt(14, Utilidades.convertirAEntero(voInfoIngreso.get("paciente")+""));
				
				if(UtilidadTexto.isEmpty(voInfoIngreso.get("valorUtilizadoSoat")+""))
					ps.setObject(15, null);
				else
					ps.setDouble(15, Utilidades.convertirADouble(voInfoIngreso.get("valorUtilizadoSoat")+"",true));
				
				ps.setInt(16, Utilidades.convertirAEntero(voInfoIngreso.get("ingreso")+""));
				
				if(Utilidades.convertirADouble(voInfoIngreso.get("porcentajeAutorizado")+"")>0)
					ps.setDouble(17,Utilidades.convertirADouble(voInfoIngreso.get("porcentajeAutorizado")+""));
				else
					ps.setObject(17, null);
				if(Utilidades.convertirADouble(voInfoIngreso.get("montoAutorizado")+"")>0)
					ps.setDouble(18,Utilidades.convertirADouble(voInfoIngreso.get("montoAutorizado")+""));
				else
					ps.setObject(18, null);
				ps.setDate(19, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
				ps.setString(20,UtilidadFecha.getHoraActual(con));
				ps.setString(21, usuario);
				if(!UtilidadTexto.isEmpty(voInfoIngreso.get("numeroSolicitudVolante")+""))
					ps.setLong(22, Utilidades.convertirALong(voInfoIngreso.get("numeroSolicitudVolante")+""));
				else
					ps.setNull(22,Types.NUMERIC);
				
				if(UtilidadTexto.isEmpty(voInfoIngreso.get("mesesCotizacion")+""))
					ps.setObject(23, null);
				else
					ps.setInt(23, Utilidades.convertirAEntero(voInfoIngreso.get("mesesCotizacion")+""));
				
				if(Utilidades.convertirAEntero(voInfoIngreso.get("codigoTipoCobertura")+"")>0)
				{
					ps.setInt(24,Utilidades.convertirAEntero(voInfoIngreso.get("codigoTipoCobertura")+""));
				}
				else
				{
					ps.setNull(24,Types.INTEGER);
				}
				
				
				if(!UtilidadTexto.isEmpty(voInfoIngreso.get("tipoCobroPaciente")+""))
					ps.setString(25,voInfoIngreso.get("tipoCobroPaciente")+"");
				else
					ps.setObject(25,null);
				
				if(!UtilidadTexto.isEmpty(voInfoIngreso.get("tipoMontoCobro")+""))
					ps.setString(26,voInfoIngreso.get("tipoMontoCobro")+"");
				else
					ps.setObject(26,null);
				
				if(Utilidades.convertirAEntero(voInfoIngreso.get("porcentajeMontoCobro")+"")>=0)
					ps.setDouble(27,Utilidades.convertirAEntero(voInfoIngreso.get("porcentajeMontoCobro")+""));
				else
					ps.setNull(27,Types.NUMERIC);
				
				
				
				
				exitoso=ps.executeUpdate()>0;
				Date fechaActual=Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con)));
				String horaActual=UtilidadFecha.getHoraActual(con);
				if(exitoso&&UtilidadTexto.getBoolean(voInfoIngreso.get("esConvenioPoliza")+""))
				{
					ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertTitularPoliza,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
					/**
					 * INSERT INTO titular_poliza (" +
												" nombres_titular," +
												" apellidos_titular," +
												" tipoid_titular," +
												" numeroid_titular," +
												" direccion_titular," +
												" telefono_titular," +
												" sub_cuenta) " +
											" values (?,?,?,?,?,?,?)
					 */
					
					ps.setString(1, voInfoIngreso.get("nombresPoliza")+"");
					ps.setString(2, voInfoIngreso.get("apellidosPoliza")+"");
					ps.setString(3, voInfoIngreso.get("tipoIdPoliza")+"");
					ps.setString(4, voInfoIngreso.get("numeroIdPoliza")+"");
					ps.setString(5, voInfoIngreso.get("direccionPoliza")+"");
					ps.setString(6, voInfoIngreso.get("telefonoPoliza")+"");
					ps.setLong(7, Utilidades.convertirALong(codigoSubcuenta+""));
					
					exitoso=ps.executeUpdate()>0;
					if(exitoso)
					{
						ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertInfoPoliza,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						int codigoInfoPoliza=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_informacion_poliza");
						
						/**
						 * INSERT INTO informacion_poliza (" +
										" codigo," +
										" fecha_autorizacion," +
										" numero_autorizacion," +
										" valor_monto_autorizado," +
										" fecha_grabacion," +
										" hora_grabacion," +
										" usuario," +
										"sub_cuenta" +
									" ) values(?,?,?,?,?,?,?,?)
						 */
						
						ps.setInt(1, codigoInfoPoliza);
						ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(voInfoIngreso.get("fechaAutorizacionPoliza")+"")));
						ps.setString(3, voInfoIngreso.get("autorizacionPoliza")+"");
						ps.setDouble(4, Utilidades.convertirADouble(voInfoIngreso.get("valorPoliza")+""));
						ps.setDate(5, fechaActual);
						ps.setString(6,horaActual);
						ps.setString(7, usuario);
						ps.setInt(8, codigoSubcuenta);
						exitoso=ps.executeUpdate()>0;
					}
				}
				if(exitoso){
					exitoso=insertarModificarRequisitos(con,codigoSubcuenta+"",voRequisitosPaciente);
				}
				if(exitoso){
					exitoso=insertarVerificacionDerechos(con, codigoSubcuenta, Utilidades.convertirAEntero(voInfoIngreso.get("ingreso")+""),
								Utilidades.convertirAEntero(voInfoIngreso.get("codigoConvenio")+""), voVerificacionDerechos, fechaActual, horaActual, usuario);
				}
				if(exitoso)
					exitoso=eliminarHistoricos(con,codigoSubcuenta,voInfoIngreso.get("codigoConvenio")+"",voInfoIngreso.get("ingreso")+"",true);
				
				ps.close();
			}
			
			
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return exitoso;
		
	}

	/**
	 * 
	 * @param con
	 * @param viaIngreso
	 * @param ingreso
	 * @return
	 */
	private static boolean tieneConvenioDefectoViaIngreso(Connection con, int viaIngreso, String ingreso) 
	{
		int convenio=ViasIngreso.obtenerConvenioDefecto(con,viaIngreso);
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("select count(1) from sub_cuentas where ingreso="+ingreso+" and convenio=?"));
			ps.setInt(1, convenio);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getInt(1)>0;
		}
		catch (Exception e) 
		{
			logger.info("Error tomando el convenio por defecto");
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param codigoSubcuenta
	 * @param codigoConvenio
	 * @param ingreso
	 * @return
	 */
	private static boolean eliminarHistoricos(Connection con, int codigoSubcuenta, String codigoConvenio, String ingreso,boolean actualizarVerficacionDerechos) 
	{
		boolean exitoso=true;
		PreparedStatementDecorator ps=null;
		
		String cadenaEliminarHistoricoReqPacSub="DELETE FROM his_requisitos_pac_subcuenta where ingreso = ? and convenio=?";
		String cadenaEliminarHistoricoDistribucion="DELETE FROM historico_distribucion where ingreso = ? and convenio=?";
		String cadenaUpdateVerDer="UPDATE verificaciones_derechos set sub_cuenta = ? where ingreso = ? and convenio=?";
		try
		{
			if(exitoso)
			{
				ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminarHistoricoReqPacSub,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, Utilidades.convertirAEntero(ingreso));
				ps.setInt(2, Utilidades.convertirAEntero(codigoConvenio));
				ps.executeUpdate();
			}
			if(exitoso)
			{
				ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminarHistoricoDistribucion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, Utilidades.convertirAEntero(ingreso));
				ps.setInt(2, Utilidades.convertirAEntero(codigoConvenio));
				ps.executeUpdate();
			}
			if(exitoso && actualizarVerficacionDerechos)
			{
				//asigna de nuevo la verficacion de derechos al nuevo convenio, esto solo se hace en el proceso de insercion.
				ps= new PreparedStatementDecorator(con.prepareStatement(cadenaUpdateVerDer,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codigoSubcuenta);
				ps.setInt(2, Utilidades.convertirAEntero(ingreso));
				ps.setInt(3, Utilidades.convertirAEntero(codigoConvenio));
			}
			
			ps.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return exitoso;
	}
	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @param conSolicitudes 
	 * @param convenio 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean eliminarSubCuenta(Connection con, DtoSubCuentas subCuenta, double codigoUltimoResponsable, int convenio, boolean conSolicitudes) throws BDException
	{
		/* 
		 * 0- mover los cargos que aun depende de la sub_cuenta.
		 * 1- actualiza los registros que aun estan en solicitudes-subcuenta y det_cargos al ultimo responsable.
		 * 2- elimina los registros en titular-poliza
		 * 3- elimina los registros en informacion - poliza
		 * 4.1 - guardar el historico de requisitors_pac_subcuenta
		 * 4.2 - elimina los registros en requisitors_pac_subcuenta
		 * 5- elimina los registros en responsables_subcuenta
		 * 6 - historico de verificacion de derechos.
		 * 7- crea el historico de la info en subcuenta(de no existir el historico).
		 * 8- elimina la subcuenta.
		 */
		boolean exitoso=false;
		
		PreparedStatement pst10=null;
		PreparedStatement pst11=null;
		PreparedStatement pst12=null;
		PreparedStatement pst13=null;
		PreparedStatement pst14=null;
		PreparedStatement pst15=null;
		ResultSet rs10 = null;
		ResultSet rs11 = null;
		
		PreparedStatement pst20=null;
		PreparedStatement pst21=null;
		PreparedStatement pst22=null;
		PreparedStatement pst23=null;
		PreparedStatement pst24=null;
		PreparedStatement pst25=null;
		ResultSet rs20 = null;
		ResultSet rs21 = null;
		
		PreparedStatement pst30=null;
		PreparedStatement pst31=null;
		PreparedStatement pst32=null;
		PreparedStatement pst33=null;
		PreparedStatement pst34=null;
		PreparedStatement pst35=null;
		ResultSet rs30 = null;
		ResultSet rs31 = null;
		
		PreparedStatement pst001=null;
		PreparedStatement pst002=null;
		PreparedStatement pst003=null;
		PreparedStatement pst004=null;
		PreparedStatement pst005=null;
		PreparedStatement pst006=null;
		PreparedStatement pst007=null;
		PreparedStatement pst008=null;
		PreparedStatement pst009=null;
		PreparedStatement pst010=null;
		PreparedStatement pst011=null;
		PreparedStatement pst012=null;
		PreparedStatement pst013=null;
		PreparedStatement pst014=null;
		PreparedStatement pst015=null;
		PreparedStatement pst016=null;
		PreparedStatement pst017=null;
		PreparedStatement pst018=null;
		PreparedStatement pst020=null;
		
		try	{
			Log4JManager.info("############## Inicio eliminarSubCuenta");
			String cadenaUpdateSolicitudesSubcuenta="UPDATE solicitudes_subcuenta set sub_cuenta=? where sub_cuenta = ? and paquetizada='"+ConstantesBD.acronimoNo+"'";
			String cadenaUpdateSolicitudesCirugiaSubcuenta="UPDATE solicitudes_cirugia set sub_cuenta=? where sub_cuenta = ?";
			String cadenaUpdateDetCargos="UPDATE det_cargos set sub_cuenta=? where sub_cuenta = ? and paquetizado='"+ConstantesBD.acronimoNo+"'";
			String cadenaEliminarTitulaPoliza="DELETE FROM titular_poliza where sub_cuenta =  ?";
			String cadenaEliminarInfoPoliza="DELETE FROM informacion_poliza where sub_cuenta = ?";
			String cadenaEliminarRequisitosAteriores="DELETE FROM his_requisitos_pac_subcuenta where ingreso||'-'||convenio=(SELECT sc.ingreso||'-'||sc.convenio from sub_cuentas sc where sc.sub_cuenta=?)";
			String cadenaInsertarHistoricoReqPacSub="INSERT INTO his_requisitos_pac_subcuenta (ingreso,convenio,requisito_paciente,cumplido) (SELECT sc.ingreso,sc.convenio,rps.requisito_paciente,rps.cumplido from requisitos_pac_subcuenta rps inner join sub_cuentas sc on(rps.subcuenta=sc.sub_cuenta) where sc.sub_cuenta=?)";
			String cadenaEliminarReqPacSubcuenta="DELETE FROM requisitos_pac_subcuenta where subcuenta =?";
			String cadenaEliminarFiltroDistribucion=" DELETE FROM filtro_distribucion where sub_cuenta = ?";
			String cadenaHistoricoVerDere="UPDATE verificaciones_derechos set sub_cuenta = null where sub_cuenta=?";
			String cadenaHistoricoSubCuenta="INSERT INTO historico_distribucion(ingreso,convenio,porcentaje_autorizado,monto_autorizado,nro_autorizacion) (select ingreso,convenio,porcentaje_autorizado,monto_autorizado,nro_autorizacion from sub_cuentas where sub_cuenta=?)";
			String cadenaEliminarETServiciosSubcuenta="DELETE FROM esq_tar_proc_sub_cuentas where sub_cuenta=?";
			String cadenaEliminarETInventarioSubcuenta="DELETE FROM esq_tar_invt_sub_cuentas where sub_cuenta=?";
			String cadenaEliminarJustificacionesSevSubCuenta="DELETE FROM justificacion_serv_resp where sub_cuenta=?";
			String cadenaEliminarJustificacionesArtSubCuenta="DELETE FROM justificacion_art_resp where subcuenta=?";
			String cadenaEliminarSubCuenta="DELETE FROM sub_cuentas where sub_cuenta = ?";
			String cadenaMoverAutorizacion="UPDATE manejopaciente.autorizaciones SET sub_cuenta=? WHERE sub_cuenta = ?";
			if(conSolicitudes)
			{
				HashMap mapaTempo10=new HashMap();
				HashMap mapaTempo20=new HashMap();
				HashMap mapaTempo30=new HashMap();
				String consulta10="SELECT codigo_detalle_cargo as codigo, cod_sol_subcuenta as codsolsub, solicitud, servicio, estado, tipo_distribucion as tipodistribucion, cantidad_cargada as cantidadcargada," +
										" porcentaje_cargado as porcentajecargado, valor_unitario_cargado as valorunitariocargado, valor_total_cargado as valortotalcargado" +
									" from det_cargos where sub_cuenta=? and servicio is not null and servicio_cx is null and valor_total_cargado is not null and getNumRespSolServicio(solicitud,servicio) > 1";
				String consulta11="SELECT codigo_detalle_cargo,cod_sol_subcuenta " +
									"FROM det_cargos " +
									"WHERE sub_cuenta=? " +
									"and solicitud=? " +
									"and servicio=? " +
									"and estado=?  " +
									"and servicio_cx is null " +
									"and articulo is null " +
									"and facturado='"+ConstantesBD.acronimoNo+"' and paquetizado='"+ConstantesBD.acronimoNo+"'";
				pst10= con.prepareStatement(consulta10,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				pst10.setLong(1, Utilidades.convertirALong(subCuenta.getSubCuenta()));
				rs10=pst10.executeQuery();
				int cont10=0;
				mapaTempo10.put("numRegistros","0");
				ResultSetMetaData rsm10=rs10.getMetaData();
				while(rs10.next())
				{
					for(int i=1;i<=rsm10.getColumnCount();i++){
						mapaTempo10.put((rsm10.getColumnLabel(i)).toLowerCase()+"_"+cont10, rs10.getObject(rsm10.getColumnLabel(i))==null||rs10.getObject(rsm10.getColumnLabel(i)).toString().equals(" ")?"":rs10.getObject(rsm10.getColumnLabel(i)));
					}
					cont10++;
				}
				mapaTempo10.put("numRegistros", cont10+"");
				
				for(int i=0;i<Utilidades.convertirAEntero(mapaTempo10.get("numRegistros")+"");i++)
				{
					pst11= con.prepareStatement(consulta11,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					pst11.setLong(1,(long)codigoUltimoResponsable );
					pst11.setInt(2, Utilidades.convertirAEntero(mapaTempo10.get("solicitud_"+i)+""));
					pst11.setInt(3, Utilidades.convertirAEntero(mapaTempo10.get("servicio_"+i)+""));
					pst11.setInt(4, Utilidades.convertirAEntero(mapaTempo10.get("estado_"+i)+""));
					rs11= pst11.executeQuery();
					//si existe actualizar simplemente.
					if(rs11.next())
					{
						if((mapaTempo10.get("tipodistribucion_"+i)+"").trim().equals(ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad))
						{
							String actualizarCargos="UPDATE det_cargos set cantidad_cargada=cantidad_cargada+"+mapaTempo10.get("cantidadcargada_"+i)+",valor_total_cargado=valor_total_cargado+"+mapaTempo10.get("valortotalcargado_"+i)+" where codigo_detalle_cargo= "+rs11.getObject(1);
							pst12= con.prepareStatement(actualizarCargos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
							pst12.executeUpdate();
							
						}
						else if((mapaTempo10.get("tipodistribucion_"+i)+"").trim().equals(ConstantesIntegridadDominio.acronimoTipoDistribucionPorcentual))
						{
							String actualizarCargos="UPDATE det_cargos set porcentaje_cargado=porcentaje_cargado+"+mapaTempo10.get("porcentajecargado_"+i)+",valor_total_cargado=valor_total_cargado+"+mapaTempo10.get("valortotalcargado_"+i)+" where codigo_detalle_cargo= "+rs11.getObject(1);
							pst12= con.prepareStatement(actualizarCargos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
							pst12.executeUpdate();
							
							String actualizarCargos2="UPDATE solicitudes_subcuenta set porcentaje=porcentaje+"+mapaTempo10.get("porcentajecargado_"+i)+" where codigo= "+rs11.getObject(2);
							pst13= new PreparedStatementDecorator(con.prepareStatement(actualizarCargos2,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							pst13.executeUpdate();
						} 
						else if((mapaTempo10.get("tipodistribucion_"+i)+"").trim().equals(ConstantesIntegridadDominio.acronimoTipodistribucionMonto))
						{
							String actualizarCargos="UPDATE det_cargos set valor_total_cargado=valor_total_cargado+"+mapaTempo10.get("valortotalcargado_"+i)+" where codigo_detalle_cargo= "+rs11.getObject(1);
							pst12= con.prepareStatement(actualizarCargos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
							pst12.executeUpdate();
							
							String actualizarCargos2="UPDATE solicitudes_subcuenta set monto=monto+"+mapaTempo10.get("valortotalcargado_"+i)+" where codigo= "+rs11.getObject(2);
							pst13 = con.prepareStatement(actualizarCargos2,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
							pst13.executeUpdate();
						} 
					}
					//sino crear el registro.
					else
					{
						String cubierto="'"+ConstantesBD.acronimoSi+"'";
						int codSolSub=UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_sol_sub_cuentas");
						String consulta12="INSERT INTO solicitudes_subcuenta (codigo, solicitud, sub_cuenta, servicio, articulo, porcentaje, cantidad," +
													" monto, cubierto, facturado, cuenta, tipo_solicitud, paquetizada, sol_subcuenta_padre," +
													" servicio_cx, tipo_asocio, tipo_distribucion, fecha_modifica, hora_modifica, usuario_modifica) " +
										" (select " +codSolSub+", solicitud," +(long)codigoUltimoResponsable+ ", servicio, articulo, porcentaje," +
												" cantidad, monto," +cubierto+" , facturado, cuenta, tipo_solicitud, paquetizada, sol_subcuenta_padre," +
												" servicio_cx, tipo_asocio, tipo_distribucion, fecha_modifica, hora_modifica, usuario_modifica" +
											" from solicitudes_subcuenta " +
											" where codigo="+mapaTempo10.get("codsolsub_"+i)+
										")";
						String consulta13=" INSERT INTO det_cargos (codigo_detalle_cargo,sub_cuenta,convenio,esquema_tarifario,cantidad_cargada," +
												" valor_unitario_tarifa,valor_unitario_cargado,valor_total_cargado,porcentaje_cargado,porcentaje_recargo," +
												" valor_unitario_recargo,porcentaje_dcto,valor_unitario_dcto,valor_unitario_iva,nro_autorizacion,estado," +
												" cubierto,tipo_distribucion,solicitud,servicio,articulo,servicio_cx,tipo_asocio,facturado,tipo_solicitud," +
												" paquetizado,cargo_padre,usuario_modifica,fecha_modifica,hora_modifica,cod_sol_subcuenta,observaciones," + 
												" requiere_autorizacion,contrato) " +
											" (select " +UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_det_cargos")+"," +(long)codigoUltimoResponsable+ "," +
											convenio+" , esquema_tarifario, cantidad_cargada, valor_unitario_tarifa, valor_unitario_cargado, valor_total_cargado," +
											" porcentaje_cargado, porcentaje_recargo, valor_unitario_recargo, porcentaje_dcto, valor_unitario_dcto,  valor_unitario_iva," +
											" nro_autorizacion, estado," +cubierto+" , tipo_distribucion, solicitud, servicio, articulo, servicio_cx," +
											" tipo_asocio, facturado, tipo_solicitud, paquetizado, cargo_padre, usuario_modifica, current_date," +
											" "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +codSolSub+" , observaciones, requiere_autorizacion," +
											" contrato " +
										" from det_cargos " +
										" where codigo_detalle_cargo="+mapaTempo10.get("codigo_"+i)+" )";

						pst12= con.prepareStatement(consulta12,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
						pst12.executeUpdate();
						
						
						pst13= con.prepareStatement(consulta13,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
						pst13.executeUpdate();
					}
					
					pst14= con.prepareStatement("update solicitudes_subcuenta set eliminado='"+ConstantesBD.acronimoSi+"',sub_cuenta=null,sol_subcuenta_padre=null WHERE codigo= "+mapaTempo10.get("codsolsub_"+i),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					pst14.executeUpdate();
					
					pst15 = con.prepareStatement("UPDATE det_cargos set eliminado='"+ConstantesBD.acronimoSi+"',sub_cuenta=null,cargo_padre=null  where codigo_detalle_cargo= "+mapaTempo10.get("codigo_"+i),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					pst15.executeUpdate();
				}
				
				////acomodocacion de ServiciosCX.
				String consulta20="SELECT codigo_detalle_cargo as codigo, cod_sol_subcuenta as codsolsub, solicitud, servicio, estado, servicio_cx as serviciocx, det_cx_honorarios as detcxhonorarios," +
														" det_asocio_cx_salas_mat as detascxsalmat, tipo_distribucion as tipodistribucion, cantidad_cargada as cantidadcargada, porcentaje_cargado as porcentajecargado," +
														" valor_unitario_cargado as valorunitariocargado, valor_total_cargado as valortotalcargado" +
													" from det_cargos where sub_cuenta=? and servicio is not null and servicio_cx is not null and valor_total_cargado is not null and getNumRespSolServicio(solicitud,servicio) > 1";
			
				String consulta21="SELECT codigo_detalle_cargo,cod_sol_subcuenta " +
								"FROM det_cargos " +
							"WHERE sub_cuenta=? " +
								"and solicitud=? " +
								"and servicio=? " +
								"and servicio_cx=? " +
								"and det_cx_honorarios=? " +
								"and det_asocio_cx_salas_mat=? " +
								"and estado=? " +
								"and articulo is null " +
								"and facturado='"+ConstantesBD.acronimoNo+"' and paquetizado='"+ConstantesBD.acronimoNo+"'";
				pst20= con.prepareStatement(consulta20,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				pst20.setLong(1, Utilidades.convertirALong(subCuenta.getSubCuenta()));
				rs20=pst20.executeQuery();
				int cont20=0;
				mapaTempo20.put("numRegistros","0");
				ResultSetMetaData rsm20=rs20.getMetaData();
				while(rs20.next())
				{
					for(int i=1;i<=rsm10.getColumnCount();i++){
						mapaTempo20.put((rsm20.getColumnLabel(i)).toLowerCase()+"_"+cont20, rs20.getObject(rsm20.getColumnLabel(i))==null||rs20.getObject(rsm20.getColumnLabel(i)).toString().equals(" ")?"":rs20.getObject(rsm20.getColumnLabel(i)));
					}
					cont20++;
				}
				mapaTempo20.put("numRegistros", cont20+"");
				for(int i=0;i<Utilidades.convertirAEntero(mapaTempo20.get("numRegistros")+"");i++)
				{
					
					pst21= con.prepareStatement(consulta21,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					pst21.setLong(1,(long)codigoUltimoResponsable );
					pst21.setInt(2, Utilidades.convertirAEntero(mapaTempo20.get("solicitud_"+i)+""));
					pst21.setInt(3, Utilidades.convertirAEntero(mapaTempo20.get("servicio_"+i)+""));
					pst21.setInt(4, Utilidades.convertirAEntero(mapaTempo20.get("serviciocx_"+i)+""));
					pst21.setInt(5, Utilidades.convertirAEntero(mapaTempo20.get("detcxhonorarios_"+i)+""));
					pst21.setInt(6, Utilidades.convertirAEntero(mapaTempo20.get("detascxsalmat_"+i)+""));
					pst21.setInt(7, Utilidades.convertirAEntero(mapaTempo20.get("estado_"+i)+""));
					rs21=  pst21.executeQuery();
					//si existe actualizar simplemente.
					if(rs21.next())
					{
						if((mapaTempo20.get("tipodistribucion_"+i)+"").trim().equals(ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad))
						{
							String actualizarCargos="UPDATE det_cargos set cantidad_cargada=cantidad_cargada+"+mapaTempo20.get("cantidadcargada_"+i)+",valor_total_cargado=valor_total_cargado+"+mapaTempo20.get("valortotalcargado_"+i)+" where codigo_detalle_cargo= "+rs21.getObject(1);
							pst22= con.prepareStatement(actualizarCargos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
							pst22.executeUpdate();
							
						}
						else if((mapaTempo20.get("tipodistribucion_"+i)+"").trim().equals(ConstantesIntegridadDominio.acronimoTipoDistribucionPorcentual))
						{
							String actualizarCargos="UPDATE det_cargos set porcentaje_cargado=porcentaje_cargado+"+mapaTempo20.get("porcentajecargado_"+i)+",valor_total_cargado=valor_total_cargado+"+mapaTempo20.get("valortotalcargado_"+i)+" where codigo_detalle_cargo= "+rs21.getObject(1);
							pst22= con.prepareStatement(actualizarCargos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
							pst22.executeUpdate();
							
							String actualizarCargos2="UPDATE solicitudes_subcuenta set porcentaje=porcentaje+"+mapaTempo20.get("porcentajecargado_"+i)+" where codigo= "+rs21.getObject(2);
							pst23= con.prepareStatement(actualizarCargos2,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
							pst23.executeUpdate();
						} 
						else if((mapaTempo20.get("tipodistribucion_"+i)+"").trim().equals(ConstantesIntegridadDominio.acronimoTipodistribucionMonto))
						{
							String actualizarCargos="UPDATE det_cargos set valor_total_cargado=valor_total_cargado+"+mapaTempo20.get("valortotalcargado_"+i)+" where codigo_detalle_cargo= "+rs21.getObject(1);
							pst22= con.prepareStatement(actualizarCargos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
							pst22.executeUpdate();
							
							String actualizarCargos2="UPDATE solicitudes_subcuenta set monto=monto+"+mapaTempo20.get("valortotalcargado_"+i)+" where codigo= "+rs21.getObject(2);
							pst23= con.prepareStatement(actualizarCargos2,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
							pst23.executeUpdate();
						} 
					}
					//sino crear el registro.
					else
					{
						String cubierto="'"+ConstantesBD.acronimoSi+"'";
						int codSolSub=UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_sol_sub_cuentas");
						String consulta22="INSERT INTO solicitudes_subcuenta (codigo, solicitud, sub_cuenta, servicio, articulo, porcentaje, cantidad," +
													" monto, cubierto, facturado, cuenta, tipo_solicitud, paquetizada, sol_subcuenta_padre," +
													" servicio_cx, tipo_asocio, tipo_distribucion, fecha_modifica, hora_modifica, usuario_modifica) " +
										" (select " +codSolSub+", solicitud," +(long)codigoUltimoResponsable+ ", servicio, articulo, porcentaje," +
												" cantidad, monto," +cubierto+" , facturado, cuenta, tipo_solicitud, paquetizada, sol_subcuenta_padre," +
												" servicio_cx, tipo_asocio, tipo_distribucion, fecha_modifica, hora_modifica, usuario_modifica" +
											" from solicitudes_subcuenta " +
											" where codigo="+mapaTempo20.get("codsolsub_"+i)+
										")";
						String consulta23=" INSERT INTO det_cargos (codigo_detalle_cargo,sub_cuenta,convenio,esquema_tarifario,cantidad_cargada," +
												" valor_unitario_tarifa,valor_unitario_cargado,valor_total_cargado,porcentaje_cargado,porcentaje_recargo," +
												" valor_unitario_recargo,porcentaje_dcto,valor_unitario_dcto,valor_unitario_iva,nro_autorizacion,estado," +
												" cubierto,tipo_distribucion,solicitud,servicio,articulo,servicio_cx,tipo_asocio,facturado,tipo_solicitud," +
												" paquetizado,cargo_padre,usuario_modifica,fecha_modifica,hora_modifica,cod_sol_subcuenta,observaciones," + 
												" requiere_autorizacion,contrato) " +
											" (select " +UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_det_cargos")+"," +(long)codigoUltimoResponsable+ "," +
											convenio+" , esquema_tarifario, cantidad_cargada, valor_unitario_tarifa, valor_unitario_cargado, valor_total_cargado," +
											" porcentaje_cargado, porcentaje_recargo, valor_unitario_recargo, porcentaje_dcto, valor_unitario_dcto,  valor_unitario_iva," +
											" nro_autorizacion, estado," +cubierto+" , tipo_distribucion, solicitud, servicio, articulo, servicio_cx," +
											" tipo_asocio, facturado, tipo_solicitud, paquetizado, cargo_padre, usuario_modifica, current_date," +
											" "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +codSolSub+" , observaciones, requiere_autorizacion," +
											" contrato " +
										" from det_cargos " +
										" where codigo_detalle_cargo="+mapaTempo20.get("codigo_"+i)+" )";

						pst22= con.prepareStatement(consulta22,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
						pst22.executeUpdate();
						
						
						pst23= con.prepareStatement(consulta23,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
						pst23.executeUpdate();
						
					}
					//eliminar existente
					pst24= new PreparedStatementDecorator(con.prepareStatement("update solicitudes_subcuenta set eliminado='"+ConstantesBD.acronimoSi+"',sub_cuenta=null,sol_subcuenta_padre=null WHERE codigo= "+mapaTempo20.get("codsolsub_"+i),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst24.executeUpdate();
					
					pst25 =  new PreparedStatementDecorator(con.prepareStatement("UPDATE det_cargos set eliminado='"+ConstantesBD.acronimoSi+"',sub_cuenta=null,cargo_padre=null  where codigo_detalle_cargo= "+mapaTempo20.get("codigo_"+i),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst25.executeUpdate();
				}
				/////acomodación de articulos.
				String consulta30="SELECT codigo_detalle_cargo as codigo, cod_sol_subcuenta as codsolsub, solicitud, articulo, estado, tipo_distribucion as tipodistribucion," +
														" cantidad_cargada as cantidadcargada, porcentaje_cargado as porcentajecargado, valor_unitario_cargado as valorunitariocargado, valor_total_cargado as valortotalcargado" +
													" from det_cargos where sub_cuenta=? and articulo is not null and servicio_cx is null and valor_total_cargado is not null and getNumRespSolArticulo(solicitud,articulo) > 1";
				String consulta31="SELECT codigo_detalle_cargo,cod_sol_subcuenta " +
									"FROM det_cargos " +
								"WHERE sub_cuenta=? " +
									"and solicitud=? " +
									"and articulo=? " +
									"and estado=? " +
									"and servicio_cx is null " +
									"and servicio is null " +
									"and facturado='"+ConstantesBD.acronimoNo+"' and paquetizado='"+ConstantesBD.acronimoNo+"'";
				
				pst30= con.prepareStatement(consulta30,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				pst30.setLong(1, Utilidades.convertirALong(subCuenta.getSubCuenta()));
				rs30=pst30.executeQuery();
				int cont30=0;
				mapaTempo30.put("numRegistros","0");
				ResultSetMetaData rsm30=rs30.getMetaData();
				while(rs30.next())
				{
					for(int i=1;i<=rsm10.getColumnCount();i++){
						mapaTempo30.put((rsm30.getColumnLabel(i)).toLowerCase()+"_"+cont30, rs30.getObject(rsm30.getColumnLabel(i))==null||rs30.getObject(rsm30.getColumnLabel(i)).toString().equals(" ")?"":rs30.getObject(rsm30.getColumnLabel(i)));
					}
					cont30++;
				}
				mapaTempo30.put("numRegistros", cont30+"");
				for(int i=0;i<Utilidades.convertirAEntero(mapaTempo30.get("numRegistros")+"");i++)
				{
					pst31= con.prepareStatement(consulta31,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					pst31.setLong(1,(long)codigoUltimoResponsable );
					pst31.setInt(2, Utilidades.convertirAEntero(mapaTempo30.get("solicitud_"+i)+""));
					pst31.setInt(3, Utilidades.convertirAEntero(mapaTempo30.get("articulo_"+i)+""));
					pst31.setInt(4, Utilidades.convertirAEntero(mapaTempo30.get("estado_"+i)+""));
					rs31=  pst31.executeQuery();
					//si existe actualizar simplemente.
					if(rs31.next())
					{
						if((mapaTempo30.get("tipodistribucion_"+i)+"").trim().equals(ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad))
						{
							String actualizarCargos="UPDATE det_cargos set cantidad_cargada=cantidad_cargada+"+mapaTempo30.get("cantidadcargada_"+i)+",valor_total_cargado=valor_total_cargado+"+mapaTempo30.get("valortotalcargado_"+i)+" where codigo_detalle_cargo= "+rs31.getObject(1);
							pst32= con.prepareStatement(actualizarCargos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
							pst32.executeUpdate();
							
						}
						else if((mapaTempo30.get("tipodistribucion_"+i)+"").trim().equals(ConstantesIntegridadDominio.acronimoTipoDistribucionPorcentual))
						{
							String actualizarCargos="UPDATE det_cargos set porcentaje_cargado=porcentaje_cargado+"+mapaTempo30.get("porcentajecargado_"+i)+",valor_total_cargado=valor_total_cargado+"+mapaTempo30.get("valortotalcargado_"+i)+" where codigo_detalle_cargo= "+rs31.getObject(1);
							pst32= con.prepareStatement(actualizarCargos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
							pst32.executeUpdate();
							
							String actualizarCargos2="UPDATE solicitudes_subcuenta set porcentaje=porcentaje+"+mapaTempo30.get("porcentajecargado_"+i)+" where codigo= "+rs31.getObject(2);
							pst33= con.prepareStatement(actualizarCargos2,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
							pst33.executeUpdate();
						} 
						else if((mapaTempo30.get("tipodistribucion_"+i)+"").trim().equals(ConstantesIntegridadDominio.acronimoTipodistribucionMonto))
						{
							String actualizarCargos="UPDATE det_cargos set valor_total_cargado=valor_total_cargado+"+mapaTempo30.get("valortotalcargado_"+i)+" where codigo_detalle_cargo= "+rs31.getObject(1);
							pst32= con.prepareStatement(actualizarCargos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
							pst32.executeUpdate();
							
							String actualizarCargos2="UPDATE solicitudes_subcuenta set monto=monto+"+mapaTempo30.get("valortotalcargado_"+i)+" where codigo= "+rs31.getObject(2);
							pst33= con.prepareStatement(actualizarCargos2,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
							pst33.executeUpdate();
						} 
					}
					//sino crear el registro.
					else
					{
						String cubierto="'"+ConstantesBD.acronimoSi+"'";
						int codSolSub=UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_sol_sub_cuentas");
						String consulta32="INSERT INTO solicitudes_subcuenta (codigo, solicitud, sub_cuenta, servicio, articulo, porcentaje, cantidad," +
													" monto, cubierto, facturado, cuenta, tipo_solicitud, paquetizada, sol_subcuenta_padre," +
													" servicio_cx, tipo_asocio, tipo_distribucion, fecha_modifica, hora_modifica, usuario_modifica) " +
										" (select " +codSolSub+", solicitud," +(long)codigoUltimoResponsable+ ", servicio, articulo, porcentaje," +
												" cantidad, monto," +cubierto+" , facturado, cuenta, tipo_solicitud, paquetizada, sol_subcuenta_padre," +
												" servicio_cx, tipo_asocio, tipo_distribucion, fecha_modifica, hora_modifica, usuario_modifica" +
											" from solicitudes_subcuenta " +
											" where codigo="+mapaTempo30.get("codsolsub_"+i)+
										")";
						String consulta33=" INSERT INTO det_cargos (codigo_detalle_cargo,sub_cuenta,convenio,esquema_tarifario,cantidad_cargada," +
												" valor_unitario_tarifa,valor_unitario_cargado,valor_total_cargado,porcentaje_cargado,porcentaje_recargo," +
												" valor_unitario_recargo,porcentaje_dcto,valor_unitario_dcto,valor_unitario_iva,nro_autorizacion,estado," +
												" cubierto,tipo_distribucion,solicitud,servicio,articulo,servicio_cx,tipo_asocio,facturado,tipo_solicitud," +
												" paquetizado,cargo_padre,usuario_modifica,fecha_modifica,hora_modifica,cod_sol_subcuenta,observaciones," + 
												" requiere_autorizacion,contrato) " +
											" (select " +UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_det_cargos")+"," +(long)codigoUltimoResponsable+ "," +
											convenio+" , esquema_tarifario, cantidad_cargada, valor_unitario_tarifa, valor_unitario_cargado, valor_total_cargado," +
											" porcentaje_cargado, porcentaje_recargo, valor_unitario_recargo, porcentaje_dcto, valor_unitario_dcto,  valor_unitario_iva," +
											" nro_autorizacion, estado," +cubierto+" , tipo_distribucion, solicitud, servicio, articulo, servicio_cx," +
											" tipo_asocio, facturado, tipo_solicitud, paquetizado, cargo_padre, usuario_modifica, current_date," +
											" "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +codSolSub+" , observaciones, requiere_autorizacion," +
											" contrato " +
										" from det_cargos " +
										" where codigo_detalle_cargo="+mapaTempo30.get("codigo_"+i)+" )";

						pst32= con.prepareStatement(consulta32,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
						pst32.executeUpdate();
						
						
						pst33= con.prepareStatement(consulta33,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
						pst33.executeUpdate();
						
					}
					///////eliminar existente
					pst34= con.prepareStatement("update solicitudes_subcuenta set eliminado='"+ConstantesBD.acronimoSi+"',sub_cuenta=null,sol_subcuenta_padre=null WHERE codigo= "+mapaTempo30.get("codsolsub_"+i),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					pst34.executeUpdate();
					
					pst35 =  con.prepareStatement("UPDATE det_cargos set eliminado='"+ConstantesBD.acronimoSi+"',sub_cuenta=null,cargo_padre=null  where codigo_detalle_cargo= "+mapaTempo30.get("codigo_"+i),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					pst35.executeUpdate();
				}
				
				pst001= con.prepareStatement(cadenaUpdateSolicitudesSubcuenta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				pst001.setLong(1, (long)codigoUltimoResponsable);
				pst001.setLong(2, Utilidades.convertirALong(subCuenta.getSubCuenta()));
				pst001.executeUpdate();
				
				pst002= con.prepareStatement(cadenaUpdateDetCargos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				pst002.setLong(1, (long)codigoUltimoResponsable);
				pst002.setLong(2, Utilidades.convertirALong(subCuenta.getSubCuenta()));
				pst002.executeUpdate();
				
				pst003= con.prepareStatement(cadenaUpdateSolicitudesCirugiaSubcuenta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				pst003.setLong(1, (long)codigoUltimoResponsable);
				pst003.setLong(2, Utilidades.convertirALong(subCuenta.getSubCuenta()));
				pst003.executeUpdate();
			}
			pst004= con.prepareStatement(cadenaEliminarTitulaPoliza,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst004.setLong(1, Utilidades.convertirALong(subCuenta.getSubCuenta()));
			pst004.executeUpdate();
			
			pst005= con.prepareStatement(cadenaEliminarInfoPoliza,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst005.setLong(1, Utilidades.convertirALong(subCuenta.getSubCuenta()));
			pst005.executeUpdate();
			
			pst006= con.prepareStatement(cadenaEliminarRequisitosAteriores,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst006.setLong(1, Utilidades.convertirALong(subCuenta.getSubCuenta()));
			pst006.executeUpdate();

			pst007= con.prepareStatement(cadenaInsertarHistoricoReqPacSub,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst007.setLong(1, Utilidades.convertirALong(subCuenta.getSubCuenta()));
			pst007.executeUpdate();
			
			pst008= con.prepareStatement(cadenaEliminarReqPacSubcuenta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst008.setLong(1, Utilidades.convertirALong(subCuenta.getSubCuenta()));
			pst008.executeUpdate();

			pst009= con.prepareStatement(cadenaHistoricoVerDere,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst009.setLong(1, Utilidades.convertirALong(subCuenta.getSubCuenta()));
			pst009.executeUpdate();
		
			//eliminar primero el historico en caso de que exista.
			String consulta010="DELETE FROM historico_distribucion WHERE ingreso=(select ingreso from sub_cuentas where sub_cuenta=?) and convenio=(select convenio from sub_cuentas where sub_cuenta=?)";
			pst010= con.prepareStatement(consulta010);
			pst010.setLong(1, Utilidades.convertirALong(subCuenta.getSubCuenta()));
			pst010.setLong(2, Utilidades.convertirALong(subCuenta.getSubCuenta()));
			pst010.executeUpdate();
			
			//Se eliminan las Verificaciones de Derechos
			String consulta020="DELETE FROM verificaciones_derechos WHERE sub_cuenta=?";
			pst020= con.prepareStatement(consulta020);
			pst020.setLong(1, Long.valueOf(subCuenta.getSubCuenta()));
			pst020.executeUpdate();
			
			
			pst011= con.prepareStatement(cadenaHistoricoSubCuenta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst011.setLong(1, Utilidades.convertirALong(subCuenta.getSubCuenta()));
			pst011.executeUpdate();
			
			pst012= con.prepareStatement(cadenaEliminarFiltroDistribucion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst012.setLong(1, Utilidades.convertirALong(subCuenta.getSubCuenta()));
			pst012.executeUpdate();
			
			pst013= con.prepareStatement(cadenaEliminarETInventarioSubcuenta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst013.setLong(1, Utilidades.convertirALong(subCuenta.getSubCuenta()));
			pst013.executeUpdate();
		
			pst014= con.prepareStatement(cadenaEliminarETServiciosSubcuenta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst014.setLong(1, Utilidades.convertirALong(subCuenta.getSubCuenta()));
			pst014.executeUpdate();
		
			pst015= con.prepareStatement(cadenaEliminarJustificacionesSevSubCuenta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst015.setLong(1, Utilidades.convertirALong(subCuenta.getSubCuenta()));
			pst015.executeUpdate();
		
			pst016= con.prepareStatement(cadenaEliminarJustificacionesArtSubCuenta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst016.setLong(1, Utilidades.convertirAEntero(subCuenta.getSubCuenta()));
			pst016.executeUpdate();
		
			pst017= con.prepareStatement(cadenaMoverAutorizacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst017.setLong(1, (long)codigoUltimoResponsable);
			pst017.setLong(2, Utilidades.convertirALong(subCuenta.getSubCuenta()));
			pst017.executeUpdate();
		
			pst018= con.prepareStatement(cadenaEliminarSubCuenta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst018.setLong(1, Utilidades.convertirALong(subCuenta.getSubCuenta()));
			pst018.executeUpdate();
			exitoso=true;
		}
		catch(SQLException sqe){
			exitoso=false;
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			exitoso=false;
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs11 != null){
					rs11.close();
				}
				if(pst11 != null){
					pst11.close();
				}
				if(rs10 != null){
					rs10.close();
				}
				if(pst10 != null){
					pst10.close();
				}
				if(pst12 != null){
					pst12.close();
				}
				if(pst13 != null){
					pst13.close();
				}
				if(pst14 != null){
					pst14.close();
				}
				if(pst15 != null){
					pst15.close();
				}
				if(rs21 != null){
					rs21.close();
				}
				if(pst21 != null){
					pst21.close();
				}
				if(rs20 != null){
					rs20.close();
				}
				if(pst20 != null){
					pst20.close();
				}
				if(pst22 != null){
					pst22.close();
				}
				if(pst23 != null){
					pst23.close();
				}
				if(pst24 != null){
					pst24.close();
				}
				if(pst25 != null){
					pst25.close();
				}
				if(rs31 != null){
					rs31.close();
				}
				if(pst31 != null){
					pst31.close();
				}
				if(rs30 != null){
					rs30.close();
				}
				if(pst30 != null){
					pst30.close();
				}
				if(pst32 != null){
					pst32.close();
				}
				if(pst33 != null){
					pst33.close();
				}
				if(pst34 != null){
					pst34.close();
				}
				if(pst35 != null){
					pst35.close();
				}				
				if(pst001 != null){
					pst001.close();
				}
				if(pst002 != null){
					pst002.close();
				}
				if(pst003 != null){
					pst003.close();
				}
				if(pst004 != null){
					pst004.close();
				}
				if(pst005 != null){
					pst005.close();
				}
				if(pst006 != null){
					pst006.close();
				}
				if(pst007 != null){
					pst007.close();
				}
				if(pst008 != null){
					pst008.close();
				}
				if(pst009 != null){
					pst009.close();
				}
				if(pst010 != null){
					pst010.close();
				}
				if(pst011 != null){
					pst011.close();
				}
				if(pst012 != null){
					pst012.close();
				}
				if(pst013 != null){
					pst013.close();
				}
				if(pst014 != null){
					pst014.close();
				}
				if(pst015 != null){
					pst015.close();
				}
				if(pst016 != null){
					pst016.close();
				}
				if(pst017 != null){
					pst017.close();
				}
				if(pst018 != null){
					pst018.close();
				}
				if(pst020 != null){
					pst020.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
				exitoso=false;
			}
		}
		Log4JManager.info("############## Fin eliminarSubCuenta");
		return exitoso;
	}




	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @param codigoIngreso
	 * @return
	 */
	public static HashMap obtenerHirtoricoResponsable(Connection con, int codigoConvenio, int codigoIngreso) 
	{
		HashMap mapa=new HashMap();
		
		mapa.put("porcentaje", "");
		mapa.put("monto", "");
		mapa.put("autorizacion", "");
		
		try
		{
			String cadenaConsultaRequisitos="SELECT requisito_paciente,cumplido " +
														"FROM his_requisitos_pac_subcuenta " +
														"WHERE convenio=? and ingreso=?";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaRequisitos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoConvenio);
			ps.setInt(2, codigoIngreso);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while (rs.next())
			{
				mapa.put(rs.getString(1), UtilidadTexto.getBoolean(rs.getString(2)));
			}
			
			String cadenaConsultaHD="SELECT porcentaje_autorizado,monto_autorizado,nro_autorizacion " +
														"FROM historico_distribucion " +
														"WHERE convenio=? and ingreso=?";
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaHD,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoConvenio);
			ps.setInt(2, codigoIngreso);
			rs=new ResultSetDecorator(ps.executeQuery());
			if (rs.next())
			{
				mapa.put("porcentaje", rs.getString(1));
				mapa.put("monto", rs.getString(2));
				mapa.put("autorizacion", rs.getString(3));
			}

			rs.close();
			ps.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public static HashMap consultarInformacionPoliza(Connection con, String subCuenta) 
	{
		HashMap mapa=new HashMap();
		try
		{
			String cadenaConsulaInfoPoliza="SELECT apellidos_titular as apellidospoliza,nombres_titular as nombrespoliza,tipoid_titular as tipoidpoliza," +
					" numeroid_titular as numeroidpoliza,direccion_titular as direccionpoliza,telefono_titular as telefonopoliza," +
					" numero_autorizacion as autorizacionpoliza,fecha_autorizacion as fechaautorizacionpoliza,valor_monto_autorizado as valor from titular_poliza tp inner join informacion_poliza infop on(infop.sub_cuenta=tp.sub_cuenta) where tp.sub_cuenta=?";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsulaInfoPoliza,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setLong(1, Utilidades.convertirALong(subCuenta));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if (rs.next())
			{
				mapa.put("apellidosPoliza", rs.getString("apellidospoliza"));
				mapa.put("nombresPoliza", rs.getString("nombrespoliza"));
				mapa.put("tipoIdPoliza", rs.getString("tipoidpoliza"));
				mapa.put("numeroIdPoliza", rs.getString("numeroidpoliza"));
				mapa.put("direccionPoliza", rs.getString("direccionpoliza"));
				mapa.put("telefonoPoliza", rs.getString("telefonopoliza"));
				mapa.put("autorizacionPoliza", rs.getString("autorizacionpoliza"));
				mapa.put("valorPoliza", rs.getString("valor"));
				mapa.put("fechaAutorizacionPoliza", UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechaAutorizacionPoliza")));
			}
			rs.close();
			ps.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean guardarFiltroDistribucion(Connection con, HashMap vo) 
	{
		boolean resultado=false;
		try
		{
			logger.info("SE IMPRIME EL VALUE OBJECT:"+vo);
			if((vo.get("tipoRegistro")+"").trim().equals("MEM"))
			{
				String cadenaInsercionFiltro="INSERT INTO filtro_distribucion " +
															"(sub_cuenta," +
															"via_ingreso," +
															"centro_costo_solicita," +
															"centro_costo_ejecuta," +
															"fecha_inicial_solicitud," +
															"fecha_final_solicitud," +
															"tipo_paciente) " +
															"values(?,?,?,?,?,?,?)";
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsercionFiltro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				ps.setLong(1, Utilidades.convertirALong(vo.get("subCuenta")+""));
				
				if(Utilidades.convertirAEntero(obtenerValor(vo.get("viaIngreso")+"")+"")!=ConstantesBD.codigoNuncaValido)
					ps.setInt(2, Utilidades.convertirAEntero(obtenerValor(vo.get("viaIngreso")+"")+""));
				else
					ps.setNull(2,Types.INTEGER);
				if(Utilidades.convertirAEntero(obtenerValor(vo.get("ccSol")+"")+"")!=ConstantesBD.codigoNuncaValido)
					ps.setInt(3, Utilidades.convertirAEntero(obtenerValor(vo.get("ccSol")+"")+""));
				else
					ps.setNull(3,Types.INTEGER);
				if(Utilidades.convertirAEntero(obtenerValor(vo.get("ccEje")+"")+"")!=ConstantesBD.codigoNuncaValido)
					ps.setInt(4, Utilidades.convertirAEntero(obtenerValor(vo.get("ccEje")+"")+""));
				else
					ps.setNull(4,Types.INTEGER);
				if(!UtilidadTexto.isEmpty(vo.get("fechaInicial")+""))
					ps.setDate(5, Date.valueOf(obtenerValor(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+""))+""));
				else
					ps.setNull(5,Types.DATE);
				if(!UtilidadTexto.isEmpty(vo.get("fechaFinal")+""))
					ps.setDate(6, Date.valueOf(obtenerValor(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+""))+""));
				else
					ps.setNull(6,Types.DATE);
				if(obtenerValor(vo.get("tipoPaciente")+"")!=null)
					ps.setString(7, obtenerValor(vo.get("tipoPaciente")+"")+"");
				else
					ps.setNull(7, Types.CHAR);
				resultado=(ps.executeUpdate()>0);
				ps.close();
			}
			else if((vo.get("tipoRegistro")+"").trim().equals("BD"))
			{
				String cadenaInsercionFiltro="UPDATE filtro_distribucion SET " +
													"via_ingreso=?," +
													"centro_costo_solicita=?," +
													"centro_costo_ejecuta=?," +
													"fecha_inicial_solicitud=?," +
													"fecha_final_solicitud=?," +
													"tipo_paciente=? " +
													"WHERE sub_cuenta=?";
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsercionFiltro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				if(Utilidades.convertirAEntero(obtenerValor(vo.get("viaIngreso")+"")+"")!=ConstantesBD.codigoNuncaValido)
					ps.setInt(1, Utilidades.convertirAEntero(obtenerValor(vo.get("viaIngreso")+"")+""));
				else
					ps.setNull(1,Types.INTEGER);
				if(Utilidades.convertirAEntero(obtenerValor(vo.get("ccSol")+"")+"")!=ConstantesBD.codigoNuncaValido)
					ps.setInt(2, Utilidades.convertirAEntero(obtenerValor(vo.get("ccSol")+"")+""));
				else
					ps.setNull(2,Types.INTEGER);
				if(Utilidades.convertirAEntero(obtenerValor(vo.get("ccEje")+"")+"")!=ConstantesBD.codigoNuncaValido)
					ps.setInt(3, Utilidades.convertirAEntero(obtenerValor(vo.get("ccEje")+"")+""));
				else
					ps.setNull(3,Types.INTEGER);
				if(!UtilidadTexto.isEmpty(vo.get("fechaInicial")+""))
					ps.setDate(4, Date.valueOf(obtenerValor(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+""))+""));
				else
					ps.setNull(4,Types.DATE);
				if(!UtilidadTexto.isEmpty(vo.get("fechaFinal")+""))
					ps.setDate(5, Date.valueOf(obtenerValor(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+""))+""));
				else
					ps.setNull(5,Types.DATE);
				if(!UtilidadTexto.isEmpty(obtenerValor(vo.get("tipoPaciente")+"")+""))
					ps.setString(6, obtenerValor(vo.get("tipoPaciente")+"")+"");
				else
					ps.setNull(6,Types.CHAR);
				ps.setLong(7, Utilidades.convertirALong(vo.get("subCuenta")+""));
				resultado=(ps.executeUpdate()>0);
				ps.close();
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return resultado;
	}

	/**
	 * 
	 * @param string
	 * @return
	 */
	private static Object obtenerValor(String valor) 
	{
		if(UtilidadTexto.isEmpty(valor))
			return null;
		return valor;
	}
	/**
	 * 
	 * @param string
	 * @return
	 */
	private static String obtenerValor(Object valor) 
	{
		if(UtilidadTexto.isEmpty(valor+""))
			return "";
		return valor+"";
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @param loginUsuario
	 * @return
	 */
	public static boolean modificarParametrosDistribucion(Connection con, HashMap vo, String loginUsuario) 
	{
		boolean resultado=false;
		try
		{
			String cadenaInsercionFiltro="UPDATE sub_cuentas SET " +
													"porcentaje_autorizado=?, " +
													"monto_autorizado=?, " +
													"obs_parametros_distribucion=? " +
													"WHERE sub_cuenta=?";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsercionFiltro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			if(Utilidades.convertirADouble(vo.get("porcentajeAutorizado")+"")>0)
				ps.setDouble(1,Utilidades.convertirADouble(vo.get("porcentajeAutorizado")+""));
			else
				ps.setObject(1, null);
			if(Utilidades.convertirADouble(vo.get("montoAutorizado")+"")>0)
				ps.setDouble(2,Utilidades.convertirADouble(vo.get("montoAutorizado")+""));
			else
				ps.setObject(2, null);
			if(UtilidadTexto.isEmpty(vo.get("observaciones")+""))
				ps.setString(3,"");
			else
				ps.setString(3, obtenerValor(vo.get("observaciones")+"")+"");
			ps.setLong(4, Utilidades.convertirALong(vo.get("subCuenta")+""));
			resultado=(ps.executeUpdate()>0);
			ps.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean actualizarMontoAutorizado(Connection con, HashMap vo) 
	{
		try
		{
			
			boolean exitoso=true;
			for(int a=0;a<Utilidades.convertirAEntero(vo.get("numRegistros")+"");a++)
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("UPDATE sub_cuentas SET monto_autorizado = ? WHERE sub_cuenta=?"));
				if(exitoso)
				{
					if(UtilidadCadena.noEsVacio(vo.get("montoAutorizado_"+a)+""))
						ps.setDouble(1, Utilidades.convertirADouble(obtenerValor(vo.get("montoAutorizado_"+a)+"")+""));
					else
						ps.setNull(1, Types.DOUBLE);
					
					ps.setLong(2, Utilidades.convertirALong(vo.get("subCuenta_"+a)+""));
					exitoso=ps.executeUpdate()>0;
				}
				else
				{
					exitoso=false;
				}
				ps.close();
			}
			
			
			return exitoso;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @param responsablesEliminados 
	 * @param subCuentasResponsables 
	 * @param liquidacionAutomatica 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap consultarDetSolicitudesPaciente(Connection con,int[] estados,boolean incluirPaquetes, String[] responsablesEliminados, String[] subCuentasResponsables, boolean liquidacionAutomatica) throws BDException 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try	{
			Log4JManager.info("############## Inicio consultarDetSolicitudesPaciente");
			String cadena="";
			if(liquidacionAutomatica)
				cadena=cadenaConsultaSolSubcuentaDetCargos2;
			else
				cadena=cadenaConsultaSolSubcuentaDetCargos;
			if(subCuentasResponsables.length>0)
			{
				cadena=cadena+" and dc.sub_cuenta in(";
				for(int a=0;a<subCuentasResponsables.length;a++)
				{
					if(a>0)
						cadena=cadena+",";
					cadena=cadena+Utilidades.convertirALong(subCuentasResponsables[a]);
				}
				for(int a=0;a<responsablesEliminados.length;a++)
				{
					cadena=cadena+" , "+Utilidades.convertirALong(responsablesEliminados[a]);
				}

				cadena=cadena+") ";
				String estadosStr="";
				if(!incluirPaquetes)
					cadena=cadena+" and dc.tipo_solicitud<>"+ConstantesBD.codigoTipoSolicitudPaquetes+" ";
				
				if(estados.length>0)
				{
					estadosStr=" dc.estado in (";
					for(int a=0;a<estados.length;a++)
					{
						if(a==0)
							estadosStr=estadosStr+" "+estados[a];
						else
							estadosStr=estadosStr+" , "+estados[a];
					}
					estadosStr=estadosStr +") ";
				}
				
				if(responsablesEliminados.length>0)
				{
					estadosStr=" ( " +estadosStr+" OR ( dc.estado = "+ConstantesBD.codigoEstadoFacturacionAnulada +" and dc.sub_cuenta in (";
					for(int a=0;a<responsablesEliminados.length;a++)
					{
						if(a==0)
							estadosStr=estadosStr+" "+Utilidades.convertirALong(responsablesEliminados[a]);
						else
							estadosStr=estadosStr+" , "+Utilidades.convertirALong(responsablesEliminados[a]);
					}
					estadosStr=estadosStr +") ) ) ";
				}
				if(!estadosStr.trim().equals(""))
					cadena=cadena+" AND "+estadosStr+" order by nomserart";
				else
					cadena=cadena+" order by nomserart,solicitud";
				
				pst= con.prepareStatement(cadena);
				rs=pst.executeQuery();
				int cont=0;
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
		Log4JManager.info("############## Fin consultarDetSolicitudesPaciente");
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap consultarDetSolicitudesPaciente(Connection con, HashMap vo,boolean incluirPaquetes, String[] subCuentasResponsables) 
	{
		String cadena=cadenaConsultaSolSubcuentaDetCargos;
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		if(subCuentasResponsables.length>0)
		{
			cadena=cadena+" and dc.sub_cuenta in(";
			for(int a=0;a<subCuentasResponsables.length;a++)
			{
				if(a>0)
					cadena=cadena+",";
				cadena=cadena+subCuentasResponsables[a];
			}
			cadena=cadena+") ";

			//Se agrego filtro en la Bï¿½squeda Avanzada por el Responsable. Tarea 52463
			if(!UtilidadTexto.isEmpty(vo.get("codigoConvenio")+""))
				cadena += " and dc.convenio = "+vo.get("codigoConvenio")+" ";
			
			if(!incluirPaquetes)
				cadena = cadena+" dc.tipo_solicitud<>"+ConstantesBD.codigoTipoSolicitudPaquetes+" ";
			
			String consulta="select * from ("+cadena+" order by nomserart) tabla where 1=1 ";
			
			if(!UtilidadTexto.isEmpty(vo.get("ccSolicita")+""))
			{
				consulta+=" and tabla.codccsolicita="+vo.get("ccSolicita");
			}
			if(!UtilidadTexto.isEmpty(vo.get("ccEjecuta")+""))
			{
				consulta+=" and tabla.codccejecuta="+vo.get("ccEjecuta");
			}
			if(!UtilidadTexto.isEmpty(vo.get("descServicio")+""))
			{
				consulta+=" and upper(tabla.nomserart) like upper('%"+vo.get("descServicio")+"%') ";
			}
			if(!UtilidadTexto.isEmpty(vo.get("estadoHC")+""))
			{
				consulta+=" and tabla.codestadohc="+vo.get("estadoHC");
			}
			if(!UtilidadTexto.isEmpty(vo.get("tipoSolicitud")+""))
			{
				consulta+=" and tabla.tiposolicitud ="+vo.get("tipoSolicitud");
			}
			if(!UtilidadTexto.isEmpty(vo.get("consecutivoSol")+""))
			{
				consulta+=" and tabla.consecutivosolicitud = '"+vo.get("consecutivoSol")+"' ";
			}
			
			//********************INICIO TAREA 52463********************************
			//Si se escribo la fechaInicial y la fechaFinal se hace el BETWEEN
			if(!UtilidadTexto.isEmpty(vo.get("fechaInicialSolicitud")+"") && !UtilidadTexto.isEmpty(vo.get("fechaFinalSolicitud")+""))
				consulta+=" and tabla.fechasolicitud BETWEEN '"+vo.get("fechaInicialSolicitud")+"' AND '"+vo.get("fechaFinalSolicitud")+"' ";
			//Si se escribo la fechaInicial no mï¿½s se hace el filtro por esta
			else if(!UtilidadTexto.isEmpty(vo.get("fechaInicialSolicitud")+"") && UtilidadTexto.isEmpty(vo.get("fechaFinalSolicitud")+""))
				consulta+=" and tabla.fechasolicitud = '"+vo.get("fechaInicialSolicitud")+"' ";
			//Si se escribo la fechaFinal no mï¿½s se hace el filtro por esta
			else if(UtilidadTexto.isEmpty(vo.get("fechaInicialSolicitud")+"") && !UtilidadTexto.isEmpty(vo.get("fechaFinalSolicitud")+""))
				consulta+=" and tabla.fechasolicitud = '"+vo.get("fechaFinalSolicitud")+"' ";
			//*******************FIN TAREA 52463************************************
			
			try
			{
				logger.info("===>Consulta Busqueda Avanzada: "+consulta);
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
				mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
				ps.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param solicitud
	 * @param servicio
	 * @param articulo
	 * @param serviciocx 
	 * @param tipodistribucion 
	 * @return
	 */
	public static HashMap consultarDistribucionSolicitud(Connection con, String solicitud, String servicio, String articulo, String serviciocx,String detCxHonorarios,String detCxAsoSalMat, String tipodistribucion) 
	{
		String cadenaConsultaDistribucionSol="SELECT " +
														" dc.codigo_detalle_cargo as codigodetcargo," +
														" dc.cod_sol_subcuenta as codsolsubcuenta," +
														" dc.solicitud as solicitud," +
														" dc.sub_cuenta as subcuenta," +
														" dc.nro_autorizacion as autorizacion," +
														" coalesce(dc.requiere_autorizacion,'"+ConstantesBD.acronimoNo+"') as requiere_autorizacion, " +
														" dc.tipo_solicitud as tiposolicitud," +
														" dc.cantidad_cargada as cantidad," +
														" dc.porcentaje_cargado as porcentaje," +
														" dc.esquema_tarifario as esquematarifario, " +
														" et.nombre as nomesquematarifario, " +
														" dc.valor_unitario_cargado as valorunitariocargado, " +
														" dc.valor_total_cargado as valortotalcargado," +
														" dc.porcentaje_dcto as porcentajedcto," +
														" dc.valor_unitario_dcto as valorunitariodcto," +
														//" case when tipo_distribucion='CANT' then dc.cantidad_cargada*dc.valor_unitario_dcto else dc.valor_unitario_dcto end as valortotaldcto," +
														" dc.valor_unitario_dcto*dc.cantidad_cargada as valortotaldcto," +
														" dc.estado  as codestado," +
														
														//Cambio por que en la Anulacion de la Factura no se esta Actualizando este campo
														//Se presento el error en la Distribuciï¿½n. Tarea 52550, la cual indica que despuï¿½s
														//de anular una factura se sigue presentando el indicativo "FACTURADO"
														//" dc.facturado as facturado," +
														"CASE WHEN fac.estado_facturacion IS NOT NULL THEN " + 
															"(CASE WHEN fac.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" THEN " +
																"'"+ConstantesBD.acronimoSi+"'" + 
															"ELSE " +
																"'"+ConstantesBD.acronimoNo+"'" + 
															"END) " +
														"ELSE " + 
															"'"+ConstantesBD.acronimoNo+"'" +
														"END AS facturado, " +
														
														" dc.cubierto as cubierto," +
														" getestadosolfac(dc.estado) as nomestado," +
														" codigo_factura as codigofactura," +
														" 'BD' as tiporegistro  " +
												" from det_cargos dc " +
												" left outer join facturas fac on (dc.codigo_factura = fac.codigo) " +
												" inner join esquemas_tarifarios et on (dc.esquema_tarifario=et.codigo)" +
												" where dc.solicitud= ? AND dc.eliminado='"+ConstantesBD.acronimoNo+"' and dc.paquetizado='"+ConstantesBD.acronimoNo+"' and dc.estado<>"+ConstantesBD.codigoEstadoFAnulada+"  and dc.estado<>"+ConstantesBD.codigoEstadoFInactiva;
		HashMap mapa=new HashMap();
		int cont=0;

		if(!UtilidadTexto.isEmpty(servicio))
		{
			cadenaConsultaDistribucionSol+=" and servicio="+servicio;
			if(!UtilidadTexto.isEmpty(serviciocx)&&Utilidades.convertirAEntero(serviciocx)>0)
			{
				cadenaConsultaDistribucionSol=cadenaConsultaDistribucionSol+" AND dc.servicio_cx="+serviciocx;
				if(Utilidades.convertirAEntero(detCxHonorarios)>0)
					cadenaConsultaDistribucionSol=cadenaConsultaDistribucionSol+" AND dc.det_cx_honorarios="+detCxHonorarios;
				else
					cadenaConsultaDistribucionSol=cadenaConsultaDistribucionSol+" AND dc.det_cx_honorarios is null";
				if(Utilidades.convertirAEntero(detCxAsoSalMat)>0)
					cadenaConsultaDistribucionSol=cadenaConsultaDistribucionSol+" AND dc.det_asocio_cx_salas_mat="+detCxAsoSalMat;
				else
					cadenaConsultaDistribucionSol=cadenaConsultaDistribucionSol+" AND dc.det_asocio_cx_salas_mat is null";
				
			}
		}
		else 
			cadenaConsultaDistribucionSol+=" and articulo="+articulo;

		try
		{
			//sacar cargos no facturados.
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaDistribucionSol +" and facturado='"+ConstantesBD.acronimoNo+"'"));
			ps.setInt(1, Utilidades.convertirAEntero(solicitud));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				mapa.put("codigodetcargo_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado")),obtenerValor(rs.getObject("codigodetcargo")));
				mapa.put("codsolsubcuenta_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado")),obtenerValor(rs.getObject("codsolsubcuenta")));
				mapa.put("solicitud_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado")),obtenerValor(rs.getObject("solicitud")));
				mapa.put("tiposolicitud_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado")),obtenerValor(rs.getObject("tiposolicitud")));
				mapa.put("autorizacion_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado")),obtenerValor(rs.getObject("autorizacion")));
				mapa.put("requiere_autorizacion_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado")),obtenerValor(rs.getObject("requiere_autorizacion")));
				mapa.put("cantidad_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado")),obtenerValor(rs.getObject("cantidad")));
				mapa.put("porcentaje_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado")),obtenerValor(rs.getObject("porcentaje")));
				mapa.put("esquematarifario_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado")),obtenerValor(rs.getObject("esquematarifario")));
				mapa.put("nomesquematarifario_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado")),obtenerValor(rs.getObject("nomesquematarifario")));
				mapa.put("valortotalcargado_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado")),obtenerValor(rs.getObject("valortotalcargado")));
				mapa.put("porcentajedcto_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado")),obtenerValor(rs.getObject("porcentajedcto")));
				mapa.put("valorunitariodcto_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado")),obtenerValor(rs.getObject("valorunitariodcto")));
				mapa.put("valortotaldcto_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado")),obtenerValor(rs.getObject("valortotaldcto")));
				mapa.put("codestado_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado")),obtenerValor(rs.getObject("codestado")));
				mapa.put("nomestado_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado")),obtenerValor(rs.getObject("nomestado")));
				mapa.put("tiporegistro_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado")),obtenerValor(rs.getObject("tiporegistro")));
				mapa.put("valorunitariocargado_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado")),obtenerValor(rs.getObject("valorunitariocargado")));
				mapa.put("facturado_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado")),obtenerValor(rs.getObject("facturado")));
				mapa.put("cubierto_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado")),obtenerValor(rs.getObject("cubierto")));
				mapa.put("consecutivofactura_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado")),"");
				cont++;
			}
			
			///sacar los cargos facturados.
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaDistribucionSol +" and facturado='"+ConstantesBD.acronimoSi+"' order by subcuenta"));
			ps.setInt(1, Utilidades.convertirAEntero(solicitud));
			rs=new ResultSetDecorator(ps.executeQuery());
			String anteriorSubCuenta="";
			int contFacturas=0;
			while(rs.next())
			{
				if(!anteriorSubCuenta.equals(obtenerValor(rs.getObject("subcuenta"))))
				{
					contFacturas=0;
					cont++;
				}
				mapa.put("codigodetcargo_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado"))+"_"+contFacturas,obtenerValor(rs.getObject("codigodetcargo")));
				mapa.put("codsolsubcuenta_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado"))+"_"+contFacturas,obtenerValor(rs.getObject("codsolsubcuenta")));
				mapa.put("solicitud_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado"))+"_"+contFacturas,obtenerValor(rs.getObject("solicitud")));
				mapa.put("tiposolicitud_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado"))+"_"+contFacturas,obtenerValor(rs.getObject("tiposolicitud")));
				mapa.put("autorizacion_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado"))+"_"+contFacturas,obtenerValor(rs.getObject("autorizacion")));
				mapa.put("requiere_autorizacion_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado"))+"_"+contFacturas,obtenerValor(rs.getObject("requiere_autorizacion")));
				mapa.put("cantidad_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado"))+"_"+contFacturas,obtenerValor(rs.getObject("cantidad")));
				mapa.put("porcentaje_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado"))+"_"+contFacturas,obtenerValor(rs.getObject("porcentaje")));
				mapa.put("esquematarifario_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado"))+"_"+contFacturas,obtenerValor(rs.getObject("esquematarifario")));
				mapa.put("nomesquematarifario_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado"))+"_"+contFacturas,obtenerValor(rs.getObject("nomesquematarifario")));
				mapa.put("valortotalcargado_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado"))+"_"+contFacturas,obtenerValor(rs.getObject("valortotalcargado")));
				mapa.put("porcentajedcto_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado"))+"_"+contFacturas,obtenerValor(rs.getObject("porcentajedcto")));
				mapa.put("valorunitariodcto_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado"))+"_"+contFacturas,obtenerValor(rs.getObject("valorunitariodcto")));
				mapa.put("valortotaldcto_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado"))+"_"+contFacturas,obtenerValor(rs.getObject("valortotaldcto")));
				mapa.put("codestado_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado"))+"_"+contFacturas,obtenerValor(rs.getObject("codestado")));
				mapa.put("nomestado_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado"))+"_"+contFacturas,obtenerValor(rs.getObject("nomestado")));
				mapa.put("tiporegistro_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado"))+"_"+contFacturas,obtenerValor(rs.getObject("tiporegistro")));
				mapa.put("valorunitariocargado_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado"))+"_"+contFacturas,obtenerValor(rs.getObject("valorunitariocargado")));
				mapa.put("facturado_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado"))+"_"+contFacturas,obtenerValor(rs.getObject("facturado")));
				mapa.put("cubierto_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado"))+"_"+contFacturas,obtenerValor(rs.getObject("cubierto")));
				mapa.put("consecutivofactura_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado"))+"_"+contFacturas,Utilidades.obtenerConsecutivoFactura(Integer.parseInt(rs.getObject("codigofactura")+"")));
				contFacturas++;
				mapa.put("numfacturas_"+obtenerValor(rs.getObject("subcuenta"))+"_"+obtenerValor(rs.getObject("facturado")),contFacturas+"");
				anteriorSubCuenta=obtenerValor(rs.getObject("subcuenta"));

			}
			rs.close();
			ps.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		mapa.put("numRegistros", cont+"");
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param codigoSolSubcuenta
	 * @return
	 */
	public static boolean eliminarSolicitudSubCuenta(Connection con, String codigoSolSubcuenta) throws BDException
	{
		boolean resultado=false;
		PreparedStatement pst =null;
		try	{
			Log4JManager.info("############## Inicio eliminarSolicitudSubCuenta");
			String eliminarDetallSolSubCuenta="DELETE FROM solicitudes_subcuenta where codigo=?";
			pst= con.prepareStatement(eliminarDetallSolSubCuenta);
			pst.setLong(1,Utilidades.convertirALong(codigoSolSubcuenta));
			resultado=pst.executeUpdate()>0;
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
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("############## Error close ResulSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin eliminarSolicitudSubCuenta");
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean modificarSolicitudSubcuenta(Connection con, HashMap vo) throws BDException
	{
		boolean resultado=false;
		PreparedStatement pst=null;
		
		try	{
			Log4JManager.info("############## Inicio modificarSolicitudSubcuenta");
			String cadena="UPDATE solicitudes_subcuenta set " +
				"porcentaje = ?," +
				"cantidad=?," +
				"monto=?," +
				"tipo_distribucion=?, " +
				"usuario_modifica=?, " +
				"fecha_modifica= current_date, " +
				"hora_modifica=? " +
			"WHERE codigo=? AND eliminado='"+ConstantesBD.acronimoNo+"' ";
			pst= con.prepareStatement(cadena);
			
			if(UtilidadTexto.isEmpty(vo.get("porcentaje")+"")){
				pst.setNull(1, Types.NUMERIC);
			}
			else{
				pst.setDouble(1, Utilidades.convertirADouble(vo.get("porcentaje")+""));
			}
			
			if(UtilidadTexto.isEmpty(vo.get("cantidad")+"")){
				pst.setNull(2, Types.NUMERIC);
			}
			else{
				pst.setDouble(2, Utilidades.convertirADouble(vo.get("cantidad")+""));
			}
			if(UtilidadTexto.isEmpty(vo.get("monto")+"")){
				pst.setNull(3, Types.DOUBLE);
			}
			else{
				pst.setDouble(3, Utilidades.convertirADouble(vo.get("monto")+""));
			}
			
			pst.setString(4, vo.get("tipodistribucion")+"");
			pst.setString(5, vo.get("usuariomodifica")+"");
			pst.setString(6, UtilidadFecha.getHoraActual());
			pst.setLong(7, Utilidades.convertirALong(vo.get("codsolsubcuenta")+""));
			resultado=pst.executeUpdate()>0;
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
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin modificarSolicitudSubcuenta");
		return resultado;
	}

	
	/**
	 * 
	 * @param con
	 * @param numSolicitud
	 * @param codServArt
	 * @param servicioCX 
	 * @param esServicio
	 * @param estadoCargo 
	 * @return
	 */
	public static boolean eliminarSolicitudSubCuentaDetCargoXSolServArt(Connection con, int numSolicitud, String codServArt, String servicioCX,String detCxHonorarios,String detCxAsSalMat, boolean esServicio, int estadoCargo) throws BDException 
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		PreparedStatement ps2=null;
		PreparedStatement ps3=null;
		PreparedStatement ps4=null;
		PreparedStatement ps5=null;
		
		boolean exito=false;
		
		try	{
			Log4JManager.info("############## Inicio eliminarSolicitudSubCuentaDetCargoXSolServArt");
			String consulta="select distinct codigo_detalle_cargo as codigodetallecargo,cod_sol_subcuenta as codigosolsubcuenta from det_cargos ";
			String whereStr=" WHERE solicitud="+numSolicitud;
			if(esServicio)
			{
				whereStr=whereStr+" AND servicio="+Utilidades.convertirAEntero(codServArt);
				if(!UtilidadTexto.isEmpty(servicioCX)&&Utilidades.convertirAEntero(servicioCX)>0)
				{
					whereStr=whereStr+" AND servicio_cx="+Utilidades.convertirAEntero(servicioCX);
					if(Utilidades.convertirAEntero(detCxHonorarios)>0)
						whereStr=whereStr+" AND det_cx_honorarios="+Utilidades.convertirAEntero(detCxHonorarios);
					else
						whereStr=whereStr+" AND det_cx_honorarios is null";
					if(Utilidades.convertirAEntero(detCxAsSalMat)>0)
						whereStr=whereStr+" AND det_asocio_cx_salas_mat="+Utilidades.convertirAEntero(detCxAsSalMat);
					else
						whereStr=whereStr+" AND det_asocio_cx_salas_mat is null";
				}
			}
			else
				whereStr=whereStr+" AND articulo="+Utilidades.convertirALong(codServArt);
			
			whereStr=whereStr+" and paquetizado='"+ConstantesBD.acronimoNo+"' AND facturado='"+ConstantesBD.acronimoNo+"' AND estado="+estadoCargo;// AND estado<>"+ConstantesBD.codigoEstadoFInactiva;// AND eliminado='"+ConstantesBD.acronimoNo+"'";
			
			String eliminarMatDetCargos="DELETE FROM facturacion.det_cargos_art_consumo where det_cargo in (select codigo_detalle_cargo FROM det_cargos where cod_sol_subcuenta=?)";
			String eliminarErroresCargosSolicitudesSubcuentas="DELETE FROM errores_det_cargos where codigo_detalle_cargo in (select codigo_detalle_cargo FROM det_cargos where cod_sol_subcuenta=?)";
			String eliminarDetCargosSolicitudesSubcuentas="DELETE FROM det_cargos where cod_sol_subcuenta=?";
			String eliminarSolicitudesSubcuentas="DELETE FROM solicitudes_subcuenta WHERE codigo=? ";
			ps= con.prepareStatement(consulta+whereStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs=ps.executeQuery();
			while(rs.next())
			{
				ps2= con.prepareStatement(eliminarMatDetCargos);
				ps2.setLong(1, rs.getLong("codigosolsubcuenta"));
				ps2.executeUpdate();
				
				ps3= con.prepareStatement(eliminarErroresCargosSolicitudesSubcuentas);
				ps3.setLong(1, rs.getLong("codigosolsubcuenta"));
				ps3.executeUpdate();
				
				ps4= con.prepareStatement(eliminarDetCargosSolicitudesSubcuentas);
				ps4.setLong(1, rs.getLong("codigosolsubcuenta"));
				ps4.executeUpdate();
				
				ps5= con.prepareStatement(eliminarSolicitudesSubcuentas);
				ps5.setLong(1, rs.getLong("codigosolsubcuenta"));
				ps5.executeUpdate();
			}
			exito=true;
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
	    }
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally {
			try {
				if (ps2 != null){ 
					ps2.close(); 
				}
				if (ps3 != null){ 
					ps3.close(); 
				}
				if (ps4 != null){ 
					ps4.close(); 
				}
				if (ps5 != null){ 
					ps5.close(); 
				}
				if (rs != null){ 
					rs.close(); 
				}
				if (ps != null){ 
					ps.close(); 
				}
			} catch(SQLException excep) {
				Log4JManager.error("ERROR CERRANDO PreparedStatements BORRANDO DET_CARGOS y ERRORES_DET_CARGOS",excep);
	        }
		}
		Log4JManager.info("############## Fin eliminarSolicitudSubCuentaDetCargoXSolServArt");
		return exito;
	}
		
	/**
	 * METODOS QUE YA NO SON USADOS, LOS DEJO EN CASO DE ALGUNDIA NECESITAR USARLO, LA IDEA ES SI, DESPUES DE ENTREGAR LA VERSION DEL 31 DE JULIO, NO SE HAN USADO, ELIMINARLOS
	 * @param con
	 * @param numSolicitud
	 * @param codServArt
	 * @param servicioCX 
	 * @param esServicio
	 * @return
	 */
	public static boolean eliminarSolicitudSubCuentaXSolServArt(Connection con, int numSolicitud, String codServArt, String servicioCX,String detCxHonorario,String detCxAsSalMat, boolean esServicio) 
	{
		
		String consulta="DELETE FROM solicitudes_subcuenta WHERE solicitud=? ";
		//String consulta="update solicitudes_subcuenta set eliminado='"+ConstantesBD.acronimoSi+"',sub_cuenta=null,sol_subcuenta_padre=null WHERE solicitud=? ";
		
		if(esServicio)
		{
			consulta=consulta+" AND servicio="+codServArt;
			if(!UtilidadTexto.isEmpty(servicioCX)&&Utilidades.convertirAEntero(servicioCX)>0)
			{
				consulta=consulta+" AND servicio_cx="+Utilidades.convertirAEntero(servicioCX)+" AND det_cx_honorarios="+Utilidades.convertirAEntero(detCxHonorario)+" AND det_asocio_cx_salas_mat="+Utilidades.convertirAEntero(detCxAsSalMat);
			}
		}
		else
			consulta=consulta+" AND articulo="+codServArt;
		
		//consulta=consulta+" and paquetizada='"+ConstantesBD.acronimoNo+"'";
		consulta=consulta+" and paquetizada='"+ConstantesBD.acronimoNo+"' AND facturado='"+ConstantesBD.acronimoNo+"'";

		
		PreparedStatementDecorator ps;
		try 
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numSolicitud);
			ps.executeUpdate();
			ps.close();
			return true;
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN eliminarSolicitudSubCuentaXSolServArt ");
			e.printStackTrace();
		}

		return false;

	}
	

	/**
	 * 
	 * @param con
	 * @param evento
	 * @param numeroSolicitud
	 * @param codServArt
	 * @param codigoDetPadre
	 * @param esServicio
	 * @return
	 */
	public static boolean actualizarCantidadDetCargo(Connection con, int cantidad,String subCuenta, String numeroSolicitud, String codServArt, boolean esServicio) 
	{
		boolean resultado=false;
		try 
		{
			String acutalizarCargos="UPDATE det_cargos set " +
													"cantidad_cargada="+cantidad+"," +
													"valor_total_cargado=valor_unitario_cargado*"+cantidad+" " +
												"WHERE sub_cuenta= "+Utilidades.convertirALong(subCuenta)+" " +
														"and solicitud="+numeroSolicitud+" " +
														"and paquetizado = '"+ConstantesBD.acronimoNo+"'  " +
														"AND facturado='"+ConstantesBD.acronimoNo+"'";
			if(esServicio)
				acutalizarCargos=acutalizarCargos+" AND servicio="+codServArt;
			else 
				acutalizarCargos=acutalizarCargos+" AND articulo="+codServArt;
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(acutalizarCargos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			resultado=ps.executeUpdate()>0;
			ps.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param cargoPadre
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap consultarDetlleCargoDetPaqueteOrginal(Connection con, String cargoPadre) throws BDException
	{
		HashMap mapa=new HashMap();
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try {
			Log4JManager.info("############## Inicio consultarDetlleCargoDetPaqueteOrginal");
			String cadena="SELECT DISTINCT convenio," +
										"esquema_tarifario as esquematarifario," +
										"cantidad_cargada as cantitad," +
										"valor_unitario_tarifa as valorunitariotarifa," +
										"valor_unitario_cargado as valorunitariocargado," +
										"valor_total_cargado as valortotalcargado," +
										"porcentaje_recargo as porcentajerecargo," +
										"valor_unitario_recargo as valorunitariorecargo," +
										"porcentaje_dcto as porcentajedcto," +
										"valor_unitario_dcto as valorunitariodcto," +
										"valor_unitario_iva as valorunitarioiva," +
										"nro_autorizacion as nroautorizacion," +
										"estado as estado,cubierto as cubierto," +
										"tipo_distribucion as tipodistribucion," +
										"solicitud as solicitud," +
										"servicio as servicio," +
										"articulo as articulo," +
										"servicio_cx as serviciocx," +
										" coalesce(det_cx_honorarios,-1) as detcxhonorarios," +
										" coalesce(det_asocio_cx_salas_mat,-1) as detascxsalmat, " +
										"tipo_asocio as tipoasocio," +
										"facturado as facturado," +
										"tipo_solicitud as tiposolicitud," +
										"paquetizado as paquetizado," +
										"observaciones as observaciones," +
										"requiere_autorizacion as requiereautorizacion," +
										"contrato as contrato " +
										"FROM det_cargos where cargo_padre=? ";
			pst= con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setLong(1, Utilidades.convertirALong(cargoPadre));
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
				Log4JManager.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin consultarDetlleCargoDetPaqueteOrginal");
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param solSubcuentaPadre
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap consultarSolSubCuentaDetPaqueteOrginal(Connection con, String solSubcuentaPadre) throws BDException
	{
		HashMap mapa=new HashMap();
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try {
			Log4JManager.info("############## Inicio consultarSolSubCuentaDetPaqueteOrginal");
			String cadena="SELECT DISTINCT solicitud," +
						"servicio," +
						"articulo," +
						"cantidad," +
						"monto," +
						"cubierto," +
						"cuenta," +
						"tipo_solicitud as tiposolicitud," +
						"paquetizada,servicio_cx as serviciocx," +
						" coalesce(det_cx_honorarios,-1) as detcxhonorarios," +
						" coalesce(det_asocio_cx_salas_mat,-1) as detascxsalmat, " +
						"tipo_asocio as tipoasocio," +
						"tipo_distribucion as tipodistribucion " +
						"FROM solicitudes_subcuenta where sol_subcuenta_padre=?";

			pst= con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, Utilidades.convertirAEntero(solSubcuentaPadre));
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
				Log4JManager.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin consultarSolSubCuentaDetPaqueteOrginal");
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param cargoPadre
	 * @return
	 */
	public static boolean eliminarDetalleCargoDetallePaquete(Connection con, int cargoPadre) throws BDException{
		PreparedStatement pst=null;
		PreparedStatement pst2=null;
		boolean exito=false;
		
		try {
			Log4JManager.info("############## Inicio eliminarDetalleCargoDetallePaquete");
			String consulta="DELETE FROM det_cargos WHERE cargo_padre=?";
			String deleteErrorCargos="DELETE FROM errores_det_cargos where codigo_detalle_cargo in ( SELECT codigo_detalle_cargo from det_cargos WHERE cargo_padre=? )";
			
			pst = con.prepareStatement(deleteErrorCargos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setLong(1, cargoPadre);
			pst.executeUpdate();	
			
			pst2 =  con.prepareStatement(consulta+" AND facturado='"+ConstantesBD.acronimoNo+"'",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst2.setLong(1, cargoPadre);
			pst2.executeUpdate();
			exito=true;
		} 
		catch (SQLException sqe) 
		{
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
	    } 
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
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
				Log4JManager.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin eliminarDetalleCargoDetallePaquete");
		return exito;

	}

	/**
	 * 
	 * @param con
	 * @param solSubcuentaPadre
	 * @return
	 */
	public static boolean eliminarSolicitudSubCuentaDetallePaquete(Connection con, String solSubcuentaPadre) throws BDException
	{
		PreparedStatement pst=null;
		boolean resultado=false;
		
		try {
			Log4JManager.info("############## Inicio eliminarSolicitudSubCuentaDetallePaquete");
			String consulta="DELETE FROM solicitudes_subcuenta where sol_subcuenta_padre="+solSubcuentaPadre;
			
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.executeUpdate();
			resultado= true;
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
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin eliminarSolicitudSubCuentaDetallePaquete");
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @param porcentaje
	 * @param tipoDistribucion 
	 * @param usuario 
	 * @return
	 */
	public static boolean updateProceDetalleCargoSolSubDetPaquete(Connection con, double codigoDetalleCargo, double porcentaje, String usuario, String tipoDistribucion) 
	{
		String updateDetCargo="UPDATE det_cargos SET porcentaje_cargado ="+porcentaje+" ,valor_total_cargado=(cantidad_cargada*valor_unitario_tarifa)*"+porcentaje+"/100,valor_unitario_cargado=valor_unitario_tarifa*"+porcentaje+"/100,tipo_distribucion='"+tipoDistribucion+"',usuario_modifica='"+usuario+"',fecha_modifica=current_date,hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+" where cargo_padre="+(long)codigoDetalleCargo;
		String updateSolSub="UPDATE solicitudes_subcuenta SET porcentaje = "+porcentaje+",tipo_distribucion='"+tipoDistribucion+"',usuario_modifica='"+usuario+"',fecha_modifica=current_date,hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+" where sol_subcuenta_padre=(select cod_sol_subcuenta from det_cargos where codigo_detalle_cargo="+(long)codigoDetalleCargo+" AND eliminado='"+ConstantesBD.acronimoNo+"' )";
		PreparedStatementDecorator ps;
		try 
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(updateDetCargo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			if(ps.executeUpdate()>0)
			{
				ps =  new PreparedStatementDecorator(con.prepareStatement(updateSolSub,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.executeUpdate();
				ps.close();
				return true;
			}
			return true;
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN updateProceDetalleCargoSolSubDetPaquete ");
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param detCargoOrginial
	 * @param solSubcuentaOriginal
	 * @param codigoDetCargo
	 * @param codigoDetCargo2
	 * @param porcentaje
	 * @param convenio 
	 * @param tipoDistribucion 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean insertarDetalleCargoSolSubDetPaquete(Connection con, HashMap detCargoOrginial, HashMap solSubcuentaOriginal, double codigoDetCargo, double solicitudSubCuenta, double porcentaje,String subCuenta,int convenio, String usuario, String tipoDistribucion) throws BDException 
	{

		boolean exitoso=true;
		PreparedStatement pst=null;
		PreparedStatement pst2=null;
		try{
			Log4JManager.info("############## Inicio insertarDetalleCargoSolSubDetPaquete");
			for(int a=0;a<Utilidades.convertirAEntero(detCargoOrginial.get("numRegistros")+"")&&a<Utilidades.convertirAEntero(solSubcuentaOriginal.get("numRegistros")+"");a++)
			{
				int solSubcuenta=UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_sol_sub_cuentas");
				String cadena="INSERT INTO solicitudes_subcuenta  ( codigo, solicitud, sub_cuenta, servicio," +
											" articulo, porcentaje, cantidad, monto, cubierto, cuenta, tipo_solicitud," +
											" paquetizada, sol_subcuenta_padre, servicio_cx, det_cx_honorarios, det_asocio_cx_salas_mat," +
											" tipo_asocio, tipo_distribucion, fecha_modifica, hora_modifica, usuario_modifica) " +
											" VALUES (" +
											" " +
											solSubcuenta+", " +
											solSubcuentaOriginal.get("solicitud_"+a)+"," +
											Utilidades.convertirALong(subCuenta)+"," +
											(UtilidadTexto.isEmpty(solSubcuentaOriginal.get("servicio_"+a)+"")?"null,":(solSubcuentaOriginal.get("servicio_"+a)+",")) +
											(UtilidadTexto.isEmpty(solSubcuentaOriginal.get("articulo_"+a)+"")?"null,":(solSubcuentaOriginal.get("articulo_"+a)+",")) +
											porcentaje+", " +
											solSubcuentaOriginal.get("cantidad_"+a)+"," +
											(UtilidadTexto.isEmpty(solSubcuentaOriginal.get("monto_"+a)+"")?"null,":(solSubcuentaOriginal.get("monto_"+a)+",")) +
											"'"+solSubcuentaOriginal.get("cubierto_"+a)+"'," +
											solSubcuentaOriginal.get("cuenta_"+a)+"," +
											solSubcuentaOriginal.get("tiposolicitud_"+a)+"," +
											"'"+solSubcuentaOriginal.get("paquetizada_"+a)+"'," +
											(long)solicitudSubCuenta+"," +
											(UtilidadTexto.isEmpty(solSubcuentaOriginal.get("serviciocx_"+a)+"")?"null,":(solSubcuentaOriginal.get("serviciocx_"+a)+",")) +
											(Utilidades.convertirAEntero(solSubcuentaOriginal.get("detcxhonorarios_"+a)+"")>0?(solSubcuentaOriginal.get("detcxhonorarios_"+a)+","):"null,") +
											(Utilidades.convertirAEntero(solSubcuentaOriginal.get("detascxsalmat_"+a)+"")>0?(solSubcuentaOriginal.get("detascxsalmat_"+a)+","):"null,") +
											(UtilidadTexto.isEmpty(solSubcuentaOriginal.get("tipoasocio_"+a)+"")?"null,":(solSubcuentaOriginal.get("tipoasocio_"+a)+",")) +
											"'"+tipoDistribucion+"'," +
											"current_date," +
											""+ValoresPorDefecto.getSentenciaHoraActualBD()+"," +
											"'"+usuario+"'"+
											")";
				
				String cadena1=" INSERT INTO det_cargos  ( codigo_detalle_cargo, sub_cuenta, convenio, esquema_tarifario, cantidad_cargada," +
											" valor_unitario_tarifa, valor_unitario_cargado, valor_total_cargado, porcentaje_cargado, porcentaje_recargo," +
											" valor_unitario_recargo, porcentaje_dcto, valor_unitario_dcto, valor_unitario_iva, nro_autorizacion," +
											" estado, cubierto, tipo_distribucion, solicitud, servicio, articulo, servicio_cx, det_cx_honorarios," +
											" det_asocio_cx_salas_mat, tipo_asocio, facturado, tipo_solicitud, paquetizado, cargo_padre, cod_sol_subcuenta," +
											" observaciones, requiere_autorizacion, contrato, usuario_modifica, fecha_modifica, hora_modifica) " +
											" VALUES ( " +
											UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_det_cargos")+", " +
											Utilidades.convertirALong(subCuenta)+"," +
											convenio+"," +
											detCargoOrginial.get("esquematarifario_"+a)+"," +
											detCargoOrginial.get("cantitad_"+a)+"," +
											detCargoOrginial.get("valorunitariotarifa_"+a)+"," +
											Utilidades.convertirADouble(detCargoOrginial.get("valorunitariotarifa_"+a)+"")*porcentaje/100 +", "+
											Utilidades.convertirAEntero(detCargoOrginial.get("cantitad_"+a)+"", true)*(Utilidades.convertirADouble(detCargoOrginial.get("valorunitariotarifa_"+a)+"",true)*porcentaje/100) +", "+
											porcentaje +","+
											(UtilidadTexto.isEmpty(detCargoOrginial.get("porcentajerecargo_"+a)+"")?"null,":(detCargoOrginial.get("porcentajerecargo_"+a)+",")) +
											(UtilidadTexto.isEmpty(detCargoOrginial.get("valorunitariorecargo_"+a)+"")?"null,":(detCargoOrginial.get("valorunitariorecargo_"+a)+",")) +
											(UtilidadTexto.isEmpty(detCargoOrginial.get("porcentajedcto_"+a)+"")?"null,":(detCargoOrginial.get("porcentajedcto_"+a)+",")) +
											(UtilidadTexto.isEmpty(detCargoOrginial.get("valorunitariodcto_"+a)+"")?"null,":(detCargoOrginial.get("valorunitariodcto_"+a)+",")) +
											(UtilidadTexto.isEmpty(detCargoOrginial.get("valorunitarioiva_"+a)+"")?"null,":(detCargoOrginial.get("valorunitarioiva_"+a)+",")) +
											"'"+detCargoOrginial.get("nroautorizacion_"+a)+"'," +
											detCargoOrginial.get("estado_"+a)+"," +
											"'"+detCargoOrginial.get("cubierto_"+a)+"'," +
											"'"+tipoDistribucion+"'," +
											detCargoOrginial.get("solicitud_"+a)+"," +
											(UtilidadTexto.isEmpty(detCargoOrginial.get("servicio_"+a)+"")?"null,":(detCargoOrginial.get("servicio_"+a)+",")) +
											(UtilidadTexto.isEmpty(detCargoOrginial.get("articulo_"+a)+"")?"null,":(detCargoOrginial.get("articulo_"+a)+",")) +
											(UtilidadTexto.isEmpty(detCargoOrginial.get("serviciocx_"+a)+"")?"null,":(detCargoOrginial.get("serviciocx_"+a)+",")) +
											(Utilidades.convertirAEntero(solSubcuentaOriginal.get("detcxhonorarios_"+a)+"")>0?(solSubcuentaOriginal.get("detcxhonorarios_"+a)+","):"null,") +
											(Utilidades.convertirAEntero(solSubcuentaOriginal.get("detascxsalmat_"+a)+"")>0?(solSubcuentaOriginal.get("detascxsalmat_"+a)+","):"null,") +
											(UtilidadTexto.isEmpty(detCargoOrginial.get("tipoasocio_"+a)+"")?"null,":(detCargoOrginial.get("tipoasocio_"+a)+",")) +
											"'"+detCargoOrginial.get("facturado_"+a)+"'," +
											detCargoOrginial.get("tiposolicitud_"+a)+"," +
											"'"+detCargoOrginial.get("paquetizado_"+a)+"'," +
											(long)codigoDetCargo+"," +
											solSubcuenta+"," +
											"'"+detCargoOrginial.get("observaciones_"+a)+"'," +
											"'"+detCargoOrginial.get("requiereautorizacion_"+a)+"'," +
											detCargoOrginial.get("contrato_"+a)+"," +
											"'"+usuario+"',"+
											"current_date," +
											""+ValoresPorDefecto.getSentenciaHoraActualBD()+"" +
											" )";
									
				pst =  con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				if(pst.executeUpdate()>0)
				{
					pst2 = con.prepareStatement(cadena1,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					pst2.executeUpdate();
				}
			}
			exitoso=true;
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
				if(pst2 != null){
					pst2.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin insertarDetalleCargoSolSubDetPaquete");
		return exitoso;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public static HashMap<String, Object> consultarIngresos(Connection con, int codigoPersona) 
	{
		
		String cadena="SELECT " +
								" i.id as idingreso," +
								" i.estado as estadoingreso," +
								" i.fecha_ingreso as fechaingreso," +
								" i.fecha_egreso as fechaegreso," +
								" c.id as cuenta," +
								" c.estado_cuenta as codestadocuenta," +
								" i.consecutivo as consecutivo," +
								" getnombreestadocuenta(c.estado_cuenta) as nomestadocuenta, " +
								" c.via_ingreso as codviaingreso, " +
								" getnomcentroatencion(cc.centro_atencion) as centroatencion, " +
								" getNombreViaIngresoTipoPac(c.id) as nomviaingreso," +
								" coalesce(getnombreusuario(di.usuario_modifica),'') as usuario " +
						" from cuentas c " +
						" inner join ingresos i on(c.id_ingreso=i.id) " +
						" inner join centros_costo cc on(cc.codigo=c.area) " +
						" left outer join distribucion_ingreso di on(di.ingreso=i.id) " +
						" where " +
								" c.codigo_paciente= ? and " +
								" i.estado in('"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"','"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"') and " +
								" (c.estado_cuenta in ("+ConstantesBD.codigoEstadoCuentaActiva+","+ConstantesBD.codigoEstadoCuentaFacturadaParcial+","+ConstantesBD.codigoEstadoCuentaCerrada+","+ConstantesBD.codigoEstadoCuentaExcenta+","+ConstantesBD.codigoEstadoCuentaFacturada+") or (c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaAsociada+" and getEsCuentaAsocioincompleto(c.id)='S') ) ";
						
						HashMap mapa=new HashMap();
						mapa.put("numRegistros", "0");
						try 
						{
							PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							ps.setInt(1, codigoPersona);
							mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
							ps.close();
						} 
						catch (SQLException e) 
						{
						logger.error("ERROR AL EJECUTAR LA CONSULTA "+e);
						e.printStackTrace();
						}
						return mapa;
		
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @param vo
	 * @param incluirPaquetes
	 * @return
	 */
	public static HashMap consultarDetSolicitudesResponsableAvanzada(Connection con, String subCuenta, HashMap vo) 
	{
		
		String cadena="SELECT " +
								" to_char(getFechaSolicitud(dc.solicitud),'dd/mm/yyyy') as fechasolicitud," +
								" CASE WHEN dc.servicio IS NOT NULL THEN getnombreservicio(dc.servicio,"+ConstantesBD.codigoTarifarioCups+") else getdescripcionarticulo(dc.articulo) END as nomserart," +
								" getcodigocups(dc.servicio,0) as codigocups," +
								" gettieneportatilsolicitud(dc.solicitud,dc.servicio) AS codigoportatil,"+
								" dc.cantidad_cargada as cantidad," +
								" dc.tipo_solicitud AS tiposolicitud, " +
								" CASE WHEN dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" THEN getintegridaddominio(getIndicadorQxSolicitud(dc.solicitud)) ELSE getnomtiposolicitud(dc.tipo_solicitud) END AS nomtiposolicitud, "+
								" getnombretipoasocio(ta.codigo) as nomtipoasocio," +
								" getConsecutivoSolicitud(dc.solicitud) as consecutivosolicitud," +
								" getCodigoCCSolicita(dc.solicitud) as codccsolicita, " +
								" dc.servicio_cx as serviciocx," +
								" getnombreservicio(dc.servicio_cx,"+ConstantesBD.codigoTarifarioCups+") as nomserviciocx," +
								" getcodigocups(dc.servicio_cx,0) as codigocupscx," +
								" getnomcentrocosto(getCodigoCCSolicita(dc.solicitud)) as nomccsolicita," +
								" getCodigoCCEjecuta(dc.solicitud) as codccejecuta, " +
								" getnomcentrocosto(getCodigoCCEjecuta(dc.solicitud)) as nomccejecuta," +
								" dc.tipo_distribucion as tipodistribucion, " +
								" getcodigoestadohcsol(dc.solicitud) as codestadohc, " +
								" getestadosolhis(getcodigoestadohcsol(dc.solicitud)) as nomestadohc ," +
								" dc.estado as estadofac,"+
								" getestadosolfac(dc.estado) as nomestadofac,"+
								" dc.valor_total_cargado as valtotalcargado," +
								"CASE WHEN fac.estado_facturacion IS NOT NULL THEN " + 
								"(CASE WHEN fac.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" THEN " +
									"'"+ConstantesBD.acronimoSi+"'" + 
									"ELSE " +
										"'"+ConstantesBD.acronimoNo+"'" + 
									"END) " +
								"ELSE " + 
									"'"+ConstantesBD.acronimoNo+"'" +
								"END AS facturado, " +
								" dc.porcentaje_cargado as porcentajecargado" +
								
							" from det_cargos dc  " +
							" left outer join tipos_asocio ta on (ta.codigo=dc.tipo_asocio) " +
							" left outer join facturas fac on (dc.codigo_factura = fac.codigo) " +
							" where dc.sub_cuenta=? AND dc.paquetizado='"+ConstantesBD.acronimoNo+"'";
		
		
		
		String consulta="select * from ("+cadena+" order by nomserart) tabla where 1=1 ";
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		
		//********************INICIO TAREA 52463********************************
		if(!UtilidadTexto.isEmpty(vo.get("ccSolicita")+""))
			consulta+=" and tabla.codccsolicita="+vo.get("ccSolicita");
		if(!UtilidadTexto.isEmpty(vo.get("ccEjecuta")+""))
			consulta+=" and tabla.codccejecuta="+vo.get("ccEjecuta");
		if(!UtilidadTexto.isEmpty(vo.get("descServicio")+""))
			consulta+=" and upper(tabla.nomserart) like upper('%"+vo.get("descServicio")+"%') ";
		if(!UtilidadTexto.isEmpty(vo.get("estadoHC")+""))
			consulta+=" and tabla.codestadohc="+vo.get("estadoHC");
		if(!UtilidadTexto.isEmpty(vo.get("tipoSolicitud")+""))
			consulta+=" and tabla.tiposolicitud ="+vo.get("tipoSolicitud");
		if(!UtilidadTexto.isEmpty(vo.get("consecutivoSol")+""))
			consulta+=" and tabla.consecutivosolicitud = '"+vo.get("consecutivoSol")+"' ";
		
		//Si se escribo la fechaInicial y la fechaFinal se hace el BETWEEN
		if(!UtilidadTexto.isEmpty(vo.get("fechaInicialSolicitud")+"") && !UtilidadTexto.isEmpty(vo.get("fechaFinalSolicitud")+""))
			consulta+=" and tabla.fechasolicitud BETWEEN '"+vo.get("fechaInicialSolicitud")+"' AND '"+vo.get("fechaFinalSolicitud")+"' ";
		//Si se escribo la fechaInicial no mï¿½s se hace el filtro por esta
		else if(!UtilidadTexto.isEmpty(vo.get("fechaInicialSolicitud")+"") && UtilidadTexto.isEmpty(vo.get("fechaFinalSolicitud")+""))
			consulta+=" and tabla.fechasolicitud = '"+vo.get("fechaInicialSolicitud")+"' ";
		//Si se escribo la fechaFinal no mï¿½s se hace el filtro por esta
		else if(UtilidadTexto.isEmpty(vo.get("fechaInicialSolicitud")+"") && !UtilidadTexto.isEmpty(vo.get("fechaFinalSolicitud")+""))
			consulta+=" and tabla.fechasolicitud = '"+vo.get("fechaFinalSolicitud")+"' ";
		//*******************FIN TAREA 52463************************************
		
		//Se modifico segï¿½n la Tarea 52463
		/*if(!UtilidadTexto.isEmpty(vo.get("descServicio")+""))
		{
			consulta+=" and upper(tabla.nomserart) like upper('%"+vo.get("descServicio")+"%') ";
		}
		if(!UtilidadTexto.isEmpty(vo.get("consecutivoSol")+""))
		{
			consulta+=" and tabla.consecutivosolicitud ='"+vo.get("consecutivoSol")+"'";
		}*/
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setLong(1, Utilidades.convertirALong(subCuenta));
			
			logger.info("===>Consulta: "+consulta);
			logger.info("===>Subcuenta: "+subCuenta);
			
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			
			ps.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @param estados
	 * @return
	 */
	public static HashMap consultarDetSolicitudesResponsable(Connection con, String subCuenta, int[] estados)
	{
		
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		
		
		try
		{
		
			String cadena="SELECT " +
									" to_char(getFechaSolicitud(dc.solicitud),'dd/mm/yyyy') as fechasolicitud," +
									" CASE WHEN dc.servicio IS NOT NULL THEN getnombreservicio(dc.servicio,"+ConstantesBD.codigoTarifarioCups+") else getdescripcionarticulo(dc.articulo) END as nomserart," +
									" getcodigocups(dc.servicio,0) as codigocups," +
									" gettieneportatilsolicitud(dc.solicitud,dc.servicio) AS codigoportatil,"+
									" dc.cantidad_cargada as cantidad," +
									" ss.tipo_solicitud as tiposolicitud," +
									" CASE WHEN dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" THEN getintegridaddominio(getIndicadorQxSolicitud(dc.solicitud)) ELSE getnomtiposolicitud(dc.tipo_solicitud) END AS nomtiposolicitud, "+									
									" getnombretipoasocio(ta.codigo) as nomtipoasocio," +
									" getConsecutivoSolicitud(dc.solicitud) as consecutivosolicitud," +
									" ss.servicio_cx as serviciocx," +
									" getnombreservicio(ss.servicio_cx,"+ConstantesBD.codigoTarifarioCups+") as nomserviciocx," +
									" getcodigocups(ss.servicio_cx,0) as codigocupscx," +
									" getnomcentrocosto(getCodigoCCSolicita(dc.solicitud)) as nomccsolicita," +
									" getnomcentrocosto(getCodigoCCEjecuta(dc.solicitud)) as nomccejecuta," +
									" dc.tipo_distribucion as tipodistribucion, " +
									" getestadosolhis(getcodigoestadohcsol(dc.solicitud)) as nomestadohc ," +
									" dc.estado as estadofac,"+
									" getestadosolfac(dc.estado) as nomestadofac,"+
									" dc.valor_total_cargado as valtotalcargado," +
									"CASE WHEN fac.estado_facturacion IS NOT NULL THEN " + 
									"(CASE WHEN fac.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" THEN " +
											"'"+ConstantesBD.acronimoSi+"'" + 
										"ELSE " +
											"'"+ConstantesBD.acronimoNo+"'" + 
										"END) " +
									"ELSE " + 
										"'"+ConstantesBD.acronimoNo+"'" +
									"END AS facturado, " +
									" dc.porcentaje_cargado as porcentajecargado" +
									
								" from solicitudes_subcuenta ss " +
								" inner join det_cargos dc on (dc.cod_sol_subcuenta=ss.codigo) " +
								" left outer join tipos_asocio ta on (ta.codigo=dc.tipo_asocio) " +
								" left outer join facturas fac on (dc.codigo_factura = fac.codigo) " +
								" where dc.sub_cuenta=? AND dc.paquetizado='"+ConstantesBD.acronimoNo+"'";
			
			
			String filtroEstados="";
			if(estados.length>0)
			{
				filtroEstados=estados[0]+"";
				for(int i=1;i<estados.length;i++)
				{
					filtroEstados="','"+estados[i];
				}
				
				filtroEstados="";
			}
			
			String consulta="select * from ("+cadena+" order by nomserart) tabla where 1=1 ";
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			
			logger.info("\n\n\nConsulta: "+consulta);
			logger.info("\n\n\nSubcuenta: "+subCuenta);
			
			ps.setLong(1, Utilidades.convertirALong(subCuenta));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			ps.close();
		}
		catch(SQLException e)
		{
		e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * Metodo que cambia el estado de una cuenta a "Cuenta en Proceso de Distribucion"
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static int cuentaProcesoDistribucion(Connection con,int idCuenta)
	{
		int resultado=0;
		try
		{
			PreparedStatementDecorator actualizar= new PreparedStatementDecorator(con.prepareStatement(cuentaEnProcesoDistribucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			actualizar.setInt(1,idCuenta);
			resultado=actualizar.executeUpdate();
			actualizar.close();
		}
		catch(SQLException e)
		{
			//abortarTransaccion(con);
			logger.error("Error actualizando el estado de la cuenta "+e);
		}
		return resultado;
	}
	
	/**
	 * Mï¿½todo que inicializa el proceso de distribucion Inserta en la tabla
	 * 
	 * @param con
	 * @param idCuenta
	 * @param loginUsuario
	 * @return nï¿½mero mayor que 0 si se iniciï¿½ el proceso correctamente
	 */
	public static int empezarProcesoDistribucion(Connection con, int idCuenta, String loginUsuario) 
	{
		int resultado=0;
		try 
		{
			PreparedStatementDecorator procesoFact =  new PreparedStatementDecorator(con.prepareStatement(procesoDistribucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			procesoFact.setInt(1, idCuenta);
			procesoFact.setInt(2, idCuenta);
			procesoFact.setString(3, loginUsuario);
			resultado=procesoFact.executeUpdate();
		}
		catch (SQLException e) 
		{
			logger.error("Error insertando el estado de la cuenta (Poceso de Distribucion) en la tabla de proceso de facturaciï¿½n"+ e);
		}
		return resultado;
	}
	
	/**
	 * Mï¿½todo que cancela todos los procesos de distribucion en proceso
	 * @param con
	 * @return
	 */
	public static int cancelarTodosLosProcesosDeDistribucion(Connection con) 
	{
		try 
		{
			DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).beginTransaction(con);
			PreparedStatementDecorator cuentasEnProceso =  new PreparedStatementDecorator(con.prepareStatement(cuentasEnProcesoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator cuentas = new ResultSetDecorator(cuentasEnProceso.executeQuery());
			int cancelados = 0;
			while (cuentas.next()) 
			{
				int idCuenta = cuentas.getInt("cuentas");
				cancelados += cancelarProcesoDistribucionTransaccional(con,idCuenta, ConstantesBD.continuarTransaccion);
				finalizarProcesoDistribuciontransaccional(con, idCuenta,ConstantesBD.continuarTransaccion);
			}
			cuentas.close();
			cuentasEnProceso.close();
			
			PreparedStatementDecorator statement =  new PreparedStatementDecorator(con.prepareStatement("DELETE FROM facturacion.ingresos_procesos_distribucion",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cancelados += statement.executeUpdate();
			
			DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).endTransaction(con);
			
			statement.close();
			
			return cancelados;
		}
		catch (SQLException e) 
		{
			try 
			{
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
			} 
			catch (SQLException e1) 
			{
				logger.error("Error abortando la transacciï¿½n " + e1);
				return 0;
			}
			logger.error("Error consultando cuentas en procesos de distribucion "	+ e);
			return 0;
		}
	}
	
	/**
	 * Mï¿½todo para cancelar el proceso de distribucion (transaccional)
	 * @param con
	 * @param idCuenta
	 * @param estado
	 * @return
	 */
	public static int cancelarProcesoDistribucionTransaccional(	Connection con, int idCuenta, String estado) 
	{
		try 
		{
			if (estado.equals(ConstantesBD.inicioTransaccion)) 
			{
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).beginTransaction(con);
			}
			PreparedStatementDecorator statement =  new PreparedStatementDecorator(con.prepareStatement(cancelarProcesoDistribucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statement.setInt(1, idCuenta);
			statement.setInt(2, idCuenta);
			int resultado = statement.executeUpdate();
			if (estado.equals(ConstantesBD.finTransaccion)) 
			{
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).endTransaction(con);
			}
			
			statement.close();
			
			return resultado;
		}
		catch (SQLException e) 
		{
			logger.error("Error cancelando el proceso de distribucion de la cuenta " + e);
			return 0;
		}
	}
	
	/**
	 * Mï¿½todo para termina el proceso de distribucion
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	private static int finalizarProcesoDistribucion(Connection con, int idCuenta) 
	{
		int res=0;
		try 
		{
			PreparedStatementDecorator statement =  new PreparedStatementDecorator(con.prepareStatement(verificarCuentasAsocioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statement.setInt(1, idCuenta);
			ResultSetDecorator resultado = new ResultSetDecorator(statement.executeQuery());
			if (resultado.next()) 
			{
				int cuentaHosp = resultado.getInt("cuenta");
				cancelarProcesoDistribucionTransaccional(con, cuentaHosp,ConstantesBD.continuarTransaccion);
				statement =  new PreparedStatementDecorator(con.prepareStatement(terminarProcesoDistribucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				statement.setInt(1, cuentaHosp);
				res=statement.executeUpdate();
				statement.close();
			} 
			else 
			{
				cancelarProcesoDistribucionTransaccional(con, idCuenta,ConstantesBD.continuarTransaccion);
				statement =  new PreparedStatementDecorator(con.prepareStatement(terminarProcesoDistribucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				statement.setInt(1, idCuenta);
				res=statement.executeUpdate();
				statement.close();
			}
		} 
		catch (SQLException e) 
		{
			logger.error("Error terminando el proceso de facturacion " + e);
		}
		return res;
	}
	
	
	/**
	 * Mï¿½todo para terminar el proceso de distribucion Si el estado es null, se ejecuta no transaccional
	 * @param con
	 * @param idCuenta
	 * @param estado
	 * @return
	 */
	public static int finalizarProcesoDistribuciontransaccional(Connection con,int idCuenta, String estado) 
	{
		if (estado == null) 
		{
			return finalizarProcesoDistribucion(con, idCuenta);
		}
		try 
		{
			if (estado.equals(ConstantesBD.inicioTransaccion)) 
			{
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).beginTransaction(con);
			}
			int resultado = finalizarProcesoDistribucion(con, idCuenta);
			if (estado.equals(ConstantesBD.finTransaccion)) 
			{
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).endTransaction(con);
			}
			return resultado;
		} 
		catch (SQLException e) 
		{
			logger.error("Error terminando el proceso de Distribucion " + e);
			return 0;
		}
	}

	/**
	 * Mï¿½todo que termina el proceso de distribuciï¿½n de la cuenta!
	 * NO CAMBIA ESTADOS
	 * @param con
	 * @param codigoCuentaPaciente
	 */
	public static boolean terminarProcesoDistribucion(Connection con, int codigoCuentaPaciente) 
	{
		boolean res=false;
		try 
		{
			PreparedStatementDecorator statement =  new PreparedStatementDecorator(con.prepareStatement(verificarCuentasAsocioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statement.setInt(1, codigoCuentaPaciente);
			ResultSetDecorator resultado = new ResultSetDecorator(statement.executeQuery());
			if (resultado.next()) 
			{
				int cuentaUrg = resultado.getInt("cuenta");
				statement =  new PreparedStatementDecorator(con.prepareStatement(terminarProcesoDistribucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				statement.setInt(1, cuentaUrg);
				res=statement.executeUpdate()>0;
				statement.close();
			} 
			else 
			{
				statement =  new PreparedStatementDecorator(con.prepareStatement(terminarProcesoDistribucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				statement.setInt(1, codigoCuentaPaciente);
				res=statement.executeUpdate()>0;
				statement.close();
			}
		} 
		catch (SQLException e) 
		{
			logger.error("Error terminando el proceso de facturacion " + e);
		}
		return res;
	}

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public static ResultadoBoolean reacomodarPrioridades(Connection con, int codigoIngreso, PersonaBasica persona, UsuarioBasico usuario ) throws BDException
	{
		PreparedStatement pst=null;
		PreparedStatement pst2=null;
		PreparedStatement pst3=null;
		ResultSet rs=null;
		ResultSet rs2=null;
		ResultadoBoolean res = new ResultadoBoolean(true,"");
		
		try	{
			Log4JManager.info("########   Inicio reacomodarPrioridades");
			String cadena="SELECT sub_cuenta from sub_cuentas where ingreso="+codigoIngreso+" order by nro_prioridad";
			pst= con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs=pst.executeQuery();
			int prioridad=1;
			
			while(rs.next())
			{
				if(prioridad==1)
				{
					if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInterfazPaciente(usuario.getCodigoInstitucionInt())))
					{
						String subCuenta=rs.getString(1);
						//hacer cambio interfaz
						
						//***************************************************************************************************
						//******************************** INTERFAZ AX_PACIEN PRESTAMO DE HISTORIAS *************************
						
						UtilidadBDInterfaz utilidadBD = new UtilidadBDInterfaz();
						// se vuelve a cargar el objeto mundo de la clase cuentas para poder obtener los ultimos datos ingresados
						//mundoCuenta.cargar(con, dtoCuenta.getIdCuenta());
						
						//Se verifica si existe registro para el paciente
						DtoInterfazPaciente dto = utilidadBD.cargarPaciente(persona.getCodigoPersona()+"", usuario.getCodigoInstitucionInt());
						
						if (dto.isError()){
							res= new ResultadoBoolean(false,dto.getMensaje());
							break;
						}
						
						dto.setInstitucion(usuario.getCodigoInstitucionInt());
						
						
						/**************************************************************************
						 * Realizar la consulta sobre la tabla sub cuentas para obtener el convenio, 
						 * el nombre del convenio y el numero_identificacion de tercero
						 */
						String query="select " +
											" substr(c.codigo||'',1,6) as codigo, " +
											" c.nombre as nombre," +
											" coalesce((select t.numero_identificacion from terceros t where t.codigo = e.tercero ),'') as NIT " +
										" from sub_cuentas sb " +
											" inner join convenios c on (c.codigo = sb.convenio) " +
											" left outer join empresas e on (e.codigo = c.codigo) " +
										" where " +
											" sub_cuenta = "+ subCuenta + " ";
						
						pst2= con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
						rs2=pst2.executeQuery();
						
						if(rs2.next())
						{
							//cargar los nuevos valores de la cuenta y los convenios modificados por el usuario
							// cargar el nuevo codigo del convenio
							dto.setCodconv(rs2.getString(1));
							// cargar el nuevo nombre del convenio
							dto.setNomconv(rs2.getString(2));					
							// cargar el nit del tercero asociado al convenio 
							dto.setTercero(rs2.getString(3));
							//Se modifica el registro de la interfaz de tesoreria
							res = utilidadBD.modificarPaciente(dto);
							
							if (!res.isTrue()){
								rs2.close();
								pst2.close();
								break;
							}	
						}
						rs2.close();
						pst2.close();
					}
					//****************************************************************************************************
					//****************************************************************************************************
					
				}
				pst3= con.prepareStatement("UPDATE sub_cuentas SET nro_prioridad="+prioridad+" where sub_cuenta="+rs.getString(1),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				pst3.executeUpdate();
				pst3.close();
				prioridad++;
			}
		}
		catch(SQLException sqe)	{
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
				Log4JManager.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin reacomodarPrioridades");
		return res;
	}

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public static HashMap consultarCantidadesSubCuentaFacturadas(Connection con, int codigoIngreso) throws BDException
	{
		HashMap mapa=new HashMap();
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try	{
			Log4JManager.info("############## Inicio consultarCantidadesSubCuentaFacturadas");
			pst= con.prepareStatement(cadenaConsultaCantFacturadasSub,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoIngreso);
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
		Log4JManager.info("############## Fin consultarCantidadesSubCuentaFacturadas");
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @param loginUsuario
	 * @return
	 */
	public static boolean empezarProcesoDistribucionIngreso(Connection con, int codigoIngreso, String loginUsuario) 
	{
		boolean resultado=false;
		try 
		{
			PreparedStatementDecorator procesoFact =  new PreparedStatementDecorator(con.prepareStatement(ingresoProcesoDistribucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			procesoFact.setInt(1, codigoIngreso);
			procesoFact.setString(2, loginUsuario);
			procesoFact.setString(3, UtilidadFecha.getHoraActual());
			resultado=procesoFact.executeUpdate()>0;
			procesoFact.close();
		}
		catch (SQLException e) 
		{
			logger.error("Error insertando el estado de la cuenta (Poceso de Distribucion) en la tabla de proceso de facturaciï¿½n"+ e);
		}
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public static boolean cancelarIngresoProcesoDistribucion(Connection con, int codigoIngreso) 
	{
		boolean resultado=false;
		try 
		{
			PreparedStatementDecorator procesoFact =  new PreparedStatementDecorator(con.prepareStatement("DELETE FROM ingresos_procesos_distribucion where id_ingreso ="+codigoIngreso,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			resultado=procesoFact.executeUpdate()>0;
			procesoFact.close();
		}
		catch (SQLException e) 
		{
			logger.error("error "+ e);
		}
		return resultado;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @param codigoConvenio 
	 * @return
	 */
	public static double obtenerValorFacturadoSoat(Connection con, int codigoIngreso, int codigoConvenio) throws BDException 
	{
		double resultado=0;
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try	{
			Log4JManager.info("############## Inicio obtenerValorFacturadoSoat");
			String consulta="SELECT case when sum(valor_convenio) is null then 0 else sum(valor_convenio) end " +
				"from facturas " +
				"where convenio=? and cuenta in(select id from cuentas where id_ingreso in " +
				"(SELECT ingreso from ingresos_registro_accidentes where codigo_registro=" +
				"(select codigo_registro from ingresos_registro_accidentes where ingreso=?))) and estado_facturacion="+ConstantesBD.codigoEstadoFacturacionFacturada;
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoConvenio);
			pst.setInt(2, Utilidades.convertirAEntero(codigoIngreso+""));
			
			rs=pst.executeQuery();
			if(rs.next())
			{
				resultado=rs.getDouble(1);
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
		Log4JManager.info("############## Fin obtenerValorFacturadoSoat");
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public static HashMap consultarEsquemasTarifariosInventario(Connection con, String subCuenta) 
	{
		String cadena="SELECT codigo,sub_cuenta as subcuenta," +
								"coalesce(clase_inventario||'','') as claseinventario," +
								"case when clase_inventario is null then 'Todos' else getnomclaseinventario(clase_inventario) end as nombreclaseinventario," +
								"esquema_tarifario as esquematarifario,getnombreesquematarifario(esquema_tarifario) as nombreesquematarifario,'BD' " +
								"as tipoRegistro from esq_tar_invt_sub_cuentas where sub_cuenta=?";
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setLong(1, Utilidades.convertirALong(subCuenta));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			ps.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return mapa;

	}

	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public static HashMap consultarEsquemasTarifariosProcedimientos(Connection con, String subCuenta) 
	{
		String cadena="SELECT codigo,sub_cuenta as subcuenta,coalesce(grupo_servicio||'','') as gruposervicio," +
								"case when grupo_servicio is null then 'Todos' else getnombregruposervicio(grupo_servicio) end as nombregruposervicio," +
								"esquema_tarifario as esquematarifario,getnombreesquematarifario(esquema_tarifario) as nombreesquematarifario,'BD' as tipoRegistro " +
								"from esq_tar_proc_sub_cuentas where sub_cuenta=?";
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setLong(1, Utilidades.convertirALong(subCuenta));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			ps.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return mapa;
	}
	//--------------------PARTE DEL ESQUEM TARIFARIO---------------------------//

	/**
	 * 
	 */
	public static HashMap consultarEsquemaInventarioLLave(Connection con, String codigoEsquema) 
	{
		String cadena="SELECT codigo,sub_cuenta as subcuenta,coalesce(clase_inventario||'','') as claseinventario," +
							"case when clase_inventario is null then 'Todos' else getnomclaseinventario(clase_inventario) end as nombreclaseinventario," +
							"esquema_tarifario as esquematarifario,getnombreesquematarifario(esquema_tarifario) as nombreesquematarifario,'BD' as tipoRegistro " +
							"from esq_tar_invt_sub_cuentas where codigo=?";
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setLong(1, Utilidades.convertirALong(codigoEsquema));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			ps.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param codigoEsquema
	 * @return
	 */
	public static HashMap consultarEsquemaProcedimientoLLave(Connection con, String codigoEsquema) 
	{
		String cadena="SELECT codigo,sub_cuenta as subcuenta,coalesce(grupo_servicio||'','') as gruposervicio,case when grupo_servicio is null then 'Todos' else getnombregruposervicio(grupo_servicio) end as nombregruposervicio,esquema_tarifario as esquematarifario,getnombreesquematarifario(esquema_tarifario) as nombreesquematarifario,'BD' as tipoRegistro from esq_tar_proc_sub_cuentas where codigo=?";
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setLong(1, Utilidades.convertirALong(codigoEsquema));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			ps.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param codigoEsquema
	 * @param esInventario
	 * @return
	 */
	public static boolean eliminarEsquema(Connection con, String codigoEsquema, boolean esInventario) 
	{
		String cadena="";
		if(esInventario)
			cadena="DELETE FROM esq_tar_invt_sub_cuentas where codigo=?";
		else 
			cadena="DELETE FROM esq_tar_proc_sub_cuentas where codigo=?";
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setLong(1, Utilidades.convertirALong(codigoEsquema));
			ps.executeUpdate();
			ps.close();
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
	 * @param vo
	 * @return
	 */
	public static boolean insertarEsquemasInventario(Connection con, HashMap vo) 
	{
		String cadena="INSERT INTO esq_tar_invt_sub_cuentas(codigo,sub_cuenta,clase_inventario,esquema_tarifario) values (?,?,?,?)";
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setLong(1, Utilidades.convertirALong(UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_esqtarinvcont")+""));
			ps.setLong(2, Utilidades.convertirALong(vo.get("subcuenta")+""));
			if(UtilidadTexto.isEmpty(vo.get("clase")+""))
				ps.setObject(3, null);
			else
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("clase")+""));
			ps.setInt(4, Utilidades.convertirAEntero(vo.get("esquematarifario")+""));
			ps.executeUpdate();
			ps.close();
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
	 * @param vo
	 * @return
	 */
	public static boolean insertarEsquemasProcedimientos(Connection con, HashMap vo) 
	{
		String cadena="INSERT INTO esq_tar_proc_sub_cuentas(codigo,sub_cuenta,grupo_servicio,esquema_tarifario) values (?,?,?,?)";
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setLong(1, Utilidades.convertirALong(UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_esqtarprocont")+""));
			ps.setLong(2, Utilidades.convertirALong(vo.get("subcuenta")+""));
			if(UtilidadTexto.isEmpty(vo.get("grupo")+""))
				ps.setObject(3, null);
			else
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("grupo")+""));
			ps.setInt(4, Utilidades.convertirAEntero(vo.get("esquematarifario")+""));
			ps.executeUpdate();
			ps.close();
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
	 * @param vo
	 * @return
	 */
	public static boolean modificarEsquemasInventario(Connection con, HashMap vo) 
	{
		boolean resultado=false;
		String cadena="UPDATE esq_tar_invt_sub_cuentas SET clase_inventario=?,esquema_tarifario=? WHERE codigo=?";
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			if(UtilidadTexto.isEmpty(vo.get("clase")+""))
				ps.setObject(1, null);
			else
				ps.setInt(1, Utilidades.convertirAEntero(vo.get("clase")+""));
			ps.setInt(2, Utilidades.convertirAEntero(vo.get("esquematarifario")+""));
			ps.setLong(3,Utilidades.convertirALong(vo.get("codigo")+""));
			resultado=ps.executeUpdate()>0;
			ps.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return resultado;

	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarEsquemasProcedimientos(Connection con, HashMap vo) 
	{
		String cadena="UPDATE esq_tar_proc_sub_cuentas SET grupo_servicio=?,esquema_tarifario=? WHERE codigo=?";
		boolean resultado=false;
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			if(UtilidadTexto.isEmpty(vo.get("grupo")+""))
				ps.setObject(1, null);
			else
				ps.setInt(1, Utilidades.convertirAEntero(vo.get("grupo")+""));
			ps.setInt(2, Utilidades.convertirAEntero(vo.get("esquematarifario")+""));
			ps.setLong(3,Utilidades.convertirALong(vo.get("codigo")+""));
			resultado=ps.executeUpdate()>0;
			ps.close();

		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return resultado;
	}

	/**
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public static boolean limpiarParametrosDistribucion(Connection con,int codigoIngreso) throws BDException
	{
		String cadena="UPDATE sub_cuentas set obs_parametros_distribucion='',porcentaje_autorizado=0,monto_autorizado=0 where ingreso=?";
		PreparedStatementDecorator ps = null;
		boolean resultado = false;
		
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setInt(1, codigoIngreso);
			ps.executeUpdate();
			ps.close();
			resultado = true;
		} 
		catch (SQLException e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
		catch(Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(ps != null){
					ps.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			} 
		}
		return resultado;
	}

	/**
	 * Mï¿½todo que consulta los registros de distribucion por convenio 
	 * segï¿½n los parametros de busqueda avanzada seleccionada. Este mï¿½todo
	 * se creo ya que en la funcionalidad de registrar utilizan una consulta
	 * diferente a la de la funcionalidad de consultar la distribucion
	 * Este cambio se adiciono por Tarea 52462
	 * @param con
	 * @param subCuenta
	 * @param estados
	 * @param incluirPaquetizadas
	 * @param agruparPortatiles
	 * @param parametrosBusquedaAvanzadaResponsable
	 * @return
	 */
	public static ArrayList<DtoSolicitudesSubCuenta> obtenerSolicitudesSubCuentaBusquedaAvanzada(Connection con, String subCuenta, boolean incluirPaquetizadas, boolean agruparPortatiles, HashMap parametrosBusquedaAvanzadaResponsable)
	{
		ArrayList resultado = new ArrayList();
		
		String consulta = "SELECT * FROM ("+cadenaConsultaSolicitudesSegunConvenioBusquedaAvanzada+" ORDER BY fechasolicitud) tabla WHERE 1=1 ";
		
		//********************INICIO TAREA 52462********************************
		if(!UtilidadTexto.isEmpty(parametrosBusquedaAvanzadaResponsable.get("ccSolicita")+""))
			consulta +=" AND tabla.codccsolicita = "+parametrosBusquedaAvanzadaResponsable.get("ccSolicita");
		if(!UtilidadTexto.isEmpty(parametrosBusquedaAvanzadaResponsable.get("ccEjecuta")+""))
			consulta +=" AND tabla.codccejecuta = "+parametrosBusquedaAvanzadaResponsable.get("ccEjecuta");
		if(!UtilidadTexto.isEmpty(parametrosBusquedaAvanzadaResponsable.get("descServicio")+""))
			consulta +=" AND UPPER(tabla.nomserart) LIKE UPPER('%"+parametrosBusquedaAvanzadaResponsable.get("descServicio")+"%') ";
		if(!UtilidadTexto.isEmpty(parametrosBusquedaAvanzadaResponsable.get("estadoHC")+""))
			consulta +=" AND tabla.codestadohc = "+parametrosBusquedaAvanzadaResponsable.get("estadoHC");
		if(!UtilidadTexto.isEmpty(parametrosBusquedaAvanzadaResponsable.get("tipoSolicitud")+""))
			consulta +=" AND tabla.tiposolicitud = "+parametrosBusquedaAvanzadaResponsable.get("tipoSolicitud");
		if(!UtilidadTexto.isEmpty(parametrosBusquedaAvanzadaResponsable.get("consecutivoSol")+""))
			consulta +=" AND tabla.consecutivosolicitud = '"+parametrosBusquedaAvanzadaResponsable.get("consecutivoSol")+"' ";
		//Si se escribo la fechaInicial y la fechaFinal se hace el BETWEEN
		if(!UtilidadTexto.isEmpty(parametrosBusquedaAvanzadaResponsable.get("fechaInicialSolicitud")+"") && !UtilidadTexto.isEmpty(parametrosBusquedaAvanzadaResponsable.get("fechaFinalSolicitud")+""))
			consulta +=" AND tabla.fechasolicitud BETWEEN '"+parametrosBusquedaAvanzadaResponsable.get("fechaInicialSolicitud")+"' AND '"+parametrosBusquedaAvanzadaResponsable.get("fechaFinalSolicitud")+"' ";
		//Si se escribo la fechaInicial no mï¿½s se hace el filtro por esta
		else if(!UtilidadTexto.isEmpty(parametrosBusquedaAvanzadaResponsable.get("fechaInicialSolicitud")+"") && UtilidadTexto.isEmpty(parametrosBusquedaAvanzadaResponsable.get("fechaFinalSolicitud")+""))
			consulta +=" AND tabla.fechasolicitud = '"+parametrosBusquedaAvanzadaResponsable.get("fechaInicialSolicitud")+"' ";
		//Si se escribo la fechaFinal no mï¿½s se hace el filtro por esta
		else if(UtilidadTexto.isEmpty(parametrosBusquedaAvanzadaResponsable.get("fechaInicialSolicitud")+"") && !UtilidadTexto.isEmpty(parametrosBusquedaAvanzadaResponsable.get("fechaFinalSolicitud")+""))
			consulta +=" AND tabla.fechasolicitud = '"+parametrosBusquedaAvanzadaResponsable.get("fechaFinalSolicitud")+"' ";
		//*******************FIN TAREA 52462************************************
		
		try
		{
			logger.info("===>Consulta: "+consulta);
			logger.info("===>SubCuenta: "+subCuenta);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setLong(1, Utilidades.convertirALong(subCuenta));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoSolicitudesSubCuenta dto=new DtoSolicitudesSubCuenta();
				dto.setCodigo(rs.getString("codigo")==null?"":rs.getString("codigo"));
				dto.setNumeroSolicitud(rs.getString("solicitud")==null?"":rs.getString("solicitud"));
				dto.setSubCuenta(rs.getString("subcuenta")==null?"":rs.getString("subcuenta"));
				dto.setServicio(new InfoDatosString(rs.getString("servicio")==null?"":rs.getString("servicio").trim(),rs.getString("nomservicio")==null?"":rs.getString("nomservicio").trim()));
				dto.setPortatil(new InfoDatosString((rs.getString("codigoportatil")==null&&Utilidades.convertirAEntero(rs.getString("codigoportatil")+"")<=0)?"":(rs.getObject("codigoportatil")+"").trim(),rs.getString("nomservicioportatil")==null?"":rs.getString("nomservicioportatil").trim()));
				dto.setArticulo(new InfoDatosString(rs.getString("articulo")==null?"":rs.getString("articulo").trim(),rs.getString("nomarticulo")==null?"":rs.getString("nomarticulo").trim()));
				dto.setCuenta(rs.getString("cuenta")==null?"":rs.getString("cuenta"));
				dto.setCantidad(rs.getString("cantidad")==null?"0":rs.getString("cantidad"));
				dto.setCubierto(rs.getString("cubierto")==null?"":rs.getString("cubierto"));
				dto.setMonto(rs.getString("monto")==null?"0":rs.getString("monto"));
				dto.setTipoSolicitud(new InfoDatosInt(rs.getInt("tiposolicitud"),rs.getString("nomtiposolicitud")==null?"":rs.getString("nomtiposolicitud")));
				dto.setPaquetizada(rs.getString("paquetizada")==null?"":rs.getString("paquetizada"));
				dto.setSolicitudPadre(rs.getString("solsubcuentapadre")==null?"":rs.getString("solsubcuentapadre"));
				dto.setServicioCX(new InfoDatosString(rs.getString("serviciocx")==null?"":rs.getString("serviciocx").trim(),rs.getString("nomserviciocx")==null?"":rs.getString("nomserviciocx").trim()));
				dto.setTipoAsocio(new InfoDatosInt(rs.getInt("tipoasocio"),rs.getString("nomtipoasocio")==null?"":rs.getString("nomtipoasocio")));
				dto.setProcentaje(rs.getString("porcentaje")==null?"0":rs.getString("porcentaje"));
				dto.setEstadoCargo(new InfoDatosInt(rs.getInt("estado"),rs.getString("descestado")==null?"":rs.getString("descestado")));
				dto.setTipoDistribucion(new InfoDatosString(rs.getString("tipodistribucion"),ValoresPorDefecto.getIntegridadDominio(rs.getString("tipodistribucion"))+""));
				dto.setFechaSolicitud(rs.getString("fechasolicitud"));
				dto.setConsecutivoSolicitud(rs.getString("consecutivosolicitud"));
				dto.setCentroCostoSolicita(new InfoDatosInt(rs.getInt("codccsolicita"),rs.getString("nomccsolicita")==null?"":rs.getString("nomccsolicita").trim()));
				dto.setCentroCostoEjecuta(new InfoDatosInt(rs.getInt("codccejecuta"),rs.getString("nomccejecuta")==null?"":rs.getString("nomccejecuta").trim()));
				dto.setNumResponsablesMismoServicio(rs.getInt("numresservicio"));
				dto.setNumResponsablesMismoArticulo(rs.getInt("numresarticulo"));
				dto.setNumResponsablesFacturadosMismoServicio(rs.getInt("numresfactservicio"));
				dto.setNumResponsablesFacturadosMismoArticulo(rs.getInt("numresfactarticulo"));
				dto.setCodigoDetalleCargo(UtilidadTexto.isEmpty(rs.getString("codigodetallecargo"))?"":rs.getString("codigodetallecargo"));
				dto.setConvenio(UtilidadTexto.isEmpty(rs.getString("convenio"))?"":rs.getString("convenio"));
				dto.setEsquemaTarifario(UtilidadTexto.isEmpty(rs.getString("esquematarifario"))?"":rs.getString("esquematarifario"));
				dto.setCantidadCargada(UtilidadTexto.isEmpty(rs.getString("cantidadcargada"))?"":rs.getString("cantidadcargada"));
				dto.setValorUnitarioTarifa(UtilidadTexto.isEmpty(rs.getString("valunitariotarifa"))?"":rs.getString("valunitariotarifa"));
				dto.setValorUnitarioCargado(UtilidadTexto.isEmpty(rs.getString("valunitariocargado"))?"":rs.getString("valunitariocargado"));
				dto.setValorTotalCargado(UtilidadTexto.isEmpty(rs.getString("valtotalcargado"))?"":rs.getString("valtotalcargado"));
				dto.setPorcentajeCargado(UtilidadTexto.isEmpty(rs.getString("porcentajecargado"))?"":rs.getString("porcentajecargado"));
				dto.setPorcentajeRecargo(UtilidadTexto.isEmpty(rs.getString("porcentajerecargo"))?"":rs.getString("porcentajerecargo"));
				dto.setValorUnitarioRecargo(UtilidadTexto.isEmpty(rs.getString("valunitariorecargo"))?"":rs.getString("valunitariorecargo"));
				dto.setPorcentajeDcto(UtilidadTexto.isEmpty(rs.getString("porcentajedcto"))?"":rs.getString("porcentajedcto"));
				dto.setValorUnitarioDcto(UtilidadTexto.isEmpty(rs.getString("valunitariodcto"))?"":rs.getString("valunitariodcto"));
				dto.setValorUnitarioIva(UtilidadTexto.isEmpty(rs.getString("valunitarioiva"))?"":rs.getString("valunitarioiva"));
				dto.setNroAutorizacion(UtilidadTexto.isEmpty(rs.getString("nroautorizacion"))?"":rs.getString("nroautorizacion"));
				dto.setCargoPadre(UtilidadTexto.isEmpty(rs.getString("cargopadre"))?"":rs.getString("cargopadre"));
				dto.setObservacionesCargo(UtilidadTexto.isEmpty(rs.getString("observacionescargo"))?"":rs.getString("observacionescargo"));
				dto.setFacturado(rs.getString("facturado")==null?"":rs.getString("facturado"));
				resultado.add(dto);
			}
			rs.close();
			ps.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return resultado;
	}

	/**
	 * Mï¿½todo implementado para consultar si un Articulo ï¿½ Servicio
	 * tiene justificaciï¿½n y si la respuesta es afirmativa se debe de
	 * eliminar los registros de los reponsables de esa justificaciï¿½n
	 * antes de realizar el proceso de Distribuciï¿½n de la Cuenta
	 * @param con
	 * @param numeroSolicitud
	 * @param servArt
	 * @param esArticulo
	 */
	public static int eliminarResponsablesJustificacionSolicitud(Connection con, int numeroSolicitud, String servArt, boolean esArticulo) throws BDException
	{
		int codigoJustificacion = ConstantesBD.codigoNuncaValido;
		PreparedStatement pst=null;
		PreparedStatement pst2=null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		
		try {
			Log4JManager.info("############## Inicio eliminarResponsablesJustificacionSolicitud");
			String cadena = "";
			String cadena2 = "";
			//Evaluamos si se va a realizar la consulta de la justifiaciï¿½n para servicios o articulos
			if(esArticulo){
				cadena = "SELECT codigo AS codigo FROM justificacion_art_sol WHERE numero_solicitud = "+numeroSolicitud+" AND articulo = "+servArt+" ";
			}
			else{
				cadena = "SELECT codigo AS codigo FROM justificacion_serv_sol WHERE solicitud = "+numeroSolicitud+" AND servicio = "+servArt+" ";
			}
			pst = con.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet);
			rs =pst.executeQuery();
			
			//Evaluamos si dicho articulo ï¿½ servicio tiene justificaciï¿½n si es asï¿½ guardamos el cï¿½digo
			if(rs.next()){
				codigoJustificacion = rs.getInt("codigo");
			}
			if(codigoJustificacion != ConstantesBD.codigoNuncaValido)
			{
				//Evaluamos si se va a realizar la eliminaciï¿½n de los responsables desde la justifiaciï¿½n de servicios ï¿½ articulos
				if(esArticulo){
					cadena2 = "DELETE FROM justificacion_art_resp WHERE justificacion_art_sol = "+codigoJustificacion+" ";
				}
				else{
					cadena2 = "DELETE FROM justificacion_serv_resp WHERE justificacion_serv_sol = "+codigoJustificacion+" ";
				}
				pst2 = con.prepareStatement(cadena2, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet);
				pst2.executeUpdate();
						
			}
			/*Se debe tener en cuenta en este lugar, para cuando Giovanny tenga tiempo para hacer
			el caso expuesto por Wilson sobre la distribuciï¿½n de una justificaciï¿½n cuando se encuentra
			en estado pendiente*/
		} 
		catch(SQLException sqe) {
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs2 != null){
					rs2.close();
				}
				if(pst2 != null){
					pst2.close();
				}
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin eliminarResponsablesJustificacionSolicitud");
		return codigoJustificacion;
	}
	
	
	/**
	 * Cargar Servicio Articulos 
	 * @param con
	 * @param criterios
	 * @return HashMap
	 */
	public static HashMap cargarServiciosArticulos (Connection con, HashMap criterios)
	{
		HashMap resultados = new HashMap();
		// carga el servicio articulo
		resultados = cargarServiciosArticulosSolicitudes(con, criterios);
		//resultados = verificarVigencia(con,ordenesAmbulatorias,resultados);
		return resultados;
	}
	
	/**
	 * Mï¿½todo implementado para cargar los servicios artï¿½culos de las solicitudes de un detalle de cargo
	 * @param con
	 * @param criterios
	 * @return HashMap
	 */
	private static HashMap cargarServiciosArticulosSolicitudes(Connection con,HashMap criterios)
	{
		HashMap resultados = new HashMap();
		resultados.put("numRegistros","0");
		logger.info("\n entre a cargarServiciosArticulos criterios-->"+criterios);
		String cadena = consultaServiciosArticulos;
		
		//filtro codigo_detalle_cargo de la tabla ddet_cargos
		String where=" WHERE dc.codigo_detalle_cargo = ? ";
								
		String group=" GROUP BY dc.solicitud,s.consecutivo_ordenes_medicas,s.tipo, s.estado_historia_clinica," +
								" s.centro_costo_solicitante, s.centro_costo_solicitado,sc.porcentaje_autorizado," +
								" sc.monto_autorizado, dc.valor_unitario_cargado, dc.valor_total_cargado, " +
								" dc.estado, s.fecha_solicitud,s.hora_solicitud ,dc.servicio, dc.articulo, " +
								" dc.requiere_autorizacion,dc.nro_autorizacion,s.numero_autorizacion,dc.codigo_detalle_cargo,dc.tipo_asocio," +
								" dc.servicio_cx,s.tipo,dc.articulo, a.codigo, da.codigo, da.estado,a.tipo_tramite,s.urgente, ra.vigencia, " +
								" ra.fecha_autorizacion,ra.hora_autorizacion,tv.tipo,sc.sub_cuenta,dc.convenio,s.codigo_medico,ra.fecha_fin_autorizada  ";
		
		String order=" ORDER BY s.consecutivo_ordenes_medicas,servicio_cx25,serv_articulo17,nombre_tipo_asocio24 DESC";
		
		cadena+=where+group+order;
		
		logger.info("\n cadena -->"+cadena);
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//filtro codigo_detalle_cargo
			ps.setLong(1, Utilidades.convertirALong(criterios.get("cod_det_cargo")+""));
			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			ps.close();
		}
		catch (SQLException e)
		{
			logger.error(" Error en consulta de servicios articulos det_cargos "+e);
		}
		return resultados;
	}
	
	/**
	 * Metodo encargado de consultar el listado de ingresos (cerrados, abiertos)
	 * y cuentas (no importa el estado) de un paciente. 
	 * @author Jhony Alexander Duque A.
	 * @param con
	 * @param criterios
	 * ---------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ---------------------------------
	 * -- paciente --> Requerido
	 * -- institucion --> Requerido
	 * @return Mapa
	 * -----------------------------
	 * KEY'S DEL MAPA QUE RETORNA
	 * -----------------------------
	 * idIngreso0_,consecutivoIngreso1_,centroAtencion2_,
	 * nombreCentroAtencion3_,estadoIngreso4_,
	 * fechaAperturaIngreso5_,viaIngreso6_,nombreViaIngreso7_,
	 * fechaCierreIngreso8_,numeroCuenta9_,estadoCuenta10_,
	 * nombreEstadoCuenta11_
	 */
	public static HashMap cargarListadoIngresos (Connection con, HashMap criterios)
	{
		logger.info("\n entre a cargarListadoIngresos criterios-->"+criterios);
		String cadena = consultarListadoIngresos;
		
			
		logger.info("\n cadena -->"+cadena);
		try{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//codigo del paciente
			ps.setInt(1, Utilidades.convertirAEntero(criterios.get("paciente")+""));
			// codigo institucion 
			ps.setInt(2, Utilidades.convertirAEntero(criterios.get("institucion")+""));
			// Cargar los resultados de la consulta en un hashMap
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
	        ps.close();
			return mapaRetorno;
			
			
		}
		catch (SQLException e)
		{
			logger.error(" Error en consulta de ingresos del paciente "+e);
		}
				
		return null;
	}
	
	
	
	public static DtoMontoCobro consultarMontoCobro(Connection con,Integer codigoMontoCobro){
		
		String consulta = "select dm.detalle_codigo codigo, tm.nombre, dmg.valor, dmg.porcentaje from " +
				" detalle_monto dm inner join tipos_monto tm " +
				" on (dm.tipo_monto_codigo = tm.codigo)" +
				" inner join detalle_monto_general dmg " +
				" on (dm.detalle_codigo = dmg.detalle_codigo)" +
				" where dm.detalle_codigo = ?";
		
		DtoMontoCobro dtoMontoCobro = new DtoMontoCobro();
		try{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoMontoCobro);
			ResultSet res = ps.executeQuery();
			
			while(res.next()){
				dtoMontoCobro.setCodigoMonto(res.getString("codigo"));
				String descripcion="";
				if(res.getString("valor") == null){
					descripcion += res.getString("nombre") +" "+res.getString("porcentaje")+" %"; 
					dtoMontoCobro.setDescripcion(descripcion);
				}
				else{
					descripcion += res.getString("valor"); 
					dtoMontoCobro.setDescripcion(descripcion);
				}
			}
			
		}
		catch (SQLException e)
		{
			logger.error(" Error en consulta de montos cobro "+e);
		}
		
		
		
		
		return dtoMontoCobro;
		
	}
	
	
	
	/**
	 * @param con
	 * @param subCuenta
	 * @param ingreso
	 * @param convenio
	 * @param voVerificacionDerechos
	 * @param fechaActual
	 * @param horaActual
	 * @param usuario
	 * @return
	 */
	private static boolean insertarVerificacionDerechos(Connection con, int subCuenta, int ingreso, int convenio, HashMap voVerificacionDerechos, Date fechaActual, String horaActual, String usuario) 
	{
		
		PreparedStatement pst=null;
		ResultSet rs=null;
		try 
		{
			if(voVerificacionDerechos.get("codigoEstado") == null || voVerificacionDerechos.get("codigoEstado").toString().equals("")){
				return true;
			}
			String sqlInsert="INSERT INTO verificaciones_derechos(sub_cuenta,estado,tipo_verificacion, numero_verificacion,persona_solicita,fecha_solicitud,"+
												"hora_solicitud,persona_contactada,fecha_verificacion,hora_verificacion,porcentaje_cobertura,cuota_verificacion,"+
												"observaciones,usuario_modifica,fecha_modifica,hora_modifica,"+
												"ingreso,convenio) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			
			pst= con.prepareStatement(sqlInsert,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, subCuenta);
			pst.setString(2, String.valueOf(voVerificacionDerechos.get("codigoEstado")));
			pst.setString(3, String.valueOf(voVerificacionDerechos.get("codigoTipo")));
			pst.setString(4, String.valueOf(voVerificacionDerechos.get("numero")));
			pst.setString(5, String.valueOf(voVerificacionDerechos.get("personaSolicita")));
			pst.setDate(6, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(String.valueOf(voVerificacionDerechos.get("fechaSolicitud")))));
			pst.setString(7, String.valueOf(voVerificacionDerechos.get("horaSolicitud")));
			pst.setString(8, String.valueOf(voVerificacionDerechos.get("personaContactada")));
			pst.setDate(9, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(String.valueOf(voVerificacionDerechos.get("fechaVerificacion")))));
			pst.setString(10, String.valueOf(voVerificacionDerechos.get("horaVerificacion")));
			if(voVerificacionDerechos.get("porcentajeCobertura") != null && !voVerificacionDerechos.get("porcentajeCobertura").equals("")){
				pst.setInt(11, Integer.valueOf(String.valueOf(voVerificacionDerechos.get("porcentajeCobertura"))));
			}
			else{
				pst.setNull(11, Types.INTEGER);
			}
			if(voVerificacionDerechos.get("cuotaVerificacion") != null && !voVerificacionDerechos.get("cuotaVerificacion").equals("")){
				pst.setInt(12, Integer.valueOf(String.valueOf(voVerificacionDerechos.get("cuotaVerificacion"))));
			}
			else{
				pst.setNull(12, Types.INTEGER);
			}
			pst.setString(13, String.valueOf(voVerificacionDerechos.get("observaciones")));
			pst.setString(14, usuario);
			pst.setDate(15, fechaActual);
			pst.setString(16, horaActual);
			pst.setInt(17, ingreso);
			pst.setInt(18, convenio);
			
			if(pst.executeUpdate()<=0)
			{
				return false;
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return false;
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
			catch(SQLException sql){
				logger.error("Errors cerrando recursos", sql);
			}
		}
		return true;
	}
	
	
	/**
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public static HashMap<String, Object> consultarVerificacionDerechos(Connection con, int subCuenta) 
	{
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try 
		{
			String sqlSelect="SELECT "+ 
									"vd.sub_cuenta AS sub_cuenta, " +
									"vd.ingreso AS ingreso, " +
									"vd.convenio AS convenio, "+
									"vd.estado AS codigo_estado, "+
									"vd.tipo_verificacion AS codigo_tipo, "+
									"coalesce(vd.numero_verificacion,'') AS numero, "+
									"vd.persona_solicita AS persona_solicita, "+
									"to_char(vd.fecha_solicitud,'"+ConstantesBD.formatoFechaAp+"') AS fecha_solicitud, "+
									"vd.hora_solicitud AS hora_solicitud, "+
									"coalesce(vd.persona_contactada,'') AS persona_contactada, "+
									"to_char(vd.fecha_verificacion,'"+ConstantesBD.formatoFechaAp+"') As fecha_verificacion, "+
									"vd.hora_verificacion AS hora_verificacion, "+
									"coalesce(vd.porcentaje_cobertura||'','') AS porcentaje_cobertura, "+
									"coalesce(vd.cuota_verificacion||'','') As cuota_verificacion, "+
									"coalesce(vd.observaciones,'') AS observaciones "+ 
									"FROM verificaciones_derechos vd "+ 
									"WHERE vd.sub_cuenta = ?";
									
			
			pst= con.prepareStatement(sqlSelect,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, subCuenta);
			rs=pst.executeQuery();
			mapa.put("numRegistros","0");
			int j=0;
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				j++;
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
					mapa.put(alias, rs.getObject(rsm.getColumnLabel(i))==null?"":rs.getObject(rsm.getColumnLabel(i)));
				}
			}
			mapa.put("numRegistros",String.valueOf(j));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
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
			catch(SQLException sql){
				logger.error("Errors cerrando recursos", sql);
			}
		}
		return mapa;
	}
	
	/**
	 * @param con
	 * @param subCuenta
	 * @param voVerificacionDerechos
	 * @param fechaActual
	 * @param horaActual
	 * @param usuario
	 * @return
	 */
	public static boolean actualizarVerificacionDerechos(Connection con, int subCuenta, HashMap voVerificacionDerechos, Date fechaActual, String horaActual, String usuario) 
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try 
		{
			if(voVerificacionDerechos.get("codigoEstado") == null || voVerificacionDerechos.get("codigoEstado").toString().equals("")){
				return true;
			}
			String sqlUpdate="UPDATE verificaciones_derechos SET estado=?,tipo_verificacion=?, numero_verificacion=?,persona_solicita=?,fecha_solicitud=?,"+
												"hora_solicitud=?,persona_contactada=?,fecha_verificacion=?,hora_verificacion=?,porcentaje_cobertura=?,cuota_verificacion=?,"+
												"observaciones=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=? "+
												"WHERE sub_cuenta=?";
			
			
			pst= con.prepareStatement(sqlUpdate,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setString(1, String.valueOf(voVerificacionDerechos.get("codigoEstado")));
			pst.setString(2, String.valueOf(voVerificacionDerechos.get("codigoTipo")));
			pst.setString(3, String.valueOf(voVerificacionDerechos.get("numero")));
			pst.setString(4, String.valueOf(voVerificacionDerechos.get("personaSolicita")));
			pst.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(String.valueOf(voVerificacionDerechos.get("fechaSolicitud")))));
			pst.setString(6, String.valueOf(voVerificacionDerechos.get("horaSolicitud")));
			pst.setString(7, String.valueOf(voVerificacionDerechos.get("personaContactada")));
			pst.setDate(8, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(String.valueOf(voVerificacionDerechos.get("fechaVerificacion")))));
			pst.setString(9, String.valueOf(voVerificacionDerechos.get("horaVerificacion")));
			if(voVerificacionDerechos.get("porcentajeCobertura") != null && !voVerificacionDerechos.get("porcentajeCobertura").equals("")){
				pst.setInt(10, Integer.valueOf(String.valueOf(voVerificacionDerechos.get("porcentajeCobertura"))));
			}
			else{
				pst.setNull(10, Types.INTEGER);
			}
			if(voVerificacionDerechos.get("cuotaVerificacion") != null && !voVerificacionDerechos.get("cuotaVerificacion").equals("")){
				pst.setInt(11, Integer.valueOf(String.valueOf(voVerificacionDerechos.get("cuotaVerificacion"))));
			}
			else{
				pst.setNull(11, Types.INTEGER);
			}
			pst.setString(12, String.valueOf(voVerificacionDerechos.get("observaciones")));
			pst.setString(13, usuario);
			pst.setDate(14, fechaActual);
			pst.setString(15, horaActual);
			pst.setInt(16, subCuenta);
			
			if(pst.executeUpdate()<=0)
			{
				return false;
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return false;
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
			catch(SQLException sql){
				logger.error("Errors cerrando recursos", sql);
			}
		}
		return true;
	}
	
}