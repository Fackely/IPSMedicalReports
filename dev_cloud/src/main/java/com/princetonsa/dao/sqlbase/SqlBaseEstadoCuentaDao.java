/*
 * @(#)SqlBaseEstadoCuentaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.Types;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.sqlbase.facturacion.SqlBaseUtilidadesFacturacionDao;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a Estado Cuenta
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 5/08/2004
 */
public class SqlBaseEstadoCuentaDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseEstadoCuentaDao.class);
	
	
	/**
	 * Cadena constante con el <i>statement</i> necesario 
	 * para cargar el número de la cuenta, la vía de
	 * ingreso, la fecha de apertura de la cuenta, el 
	 * estado de la cuenta de todas las cuentas de un
	 * paciente
	 */
	private static final String cargarTodasCuentaPacienteStr= "SELECT "+ 
		"i.id AS id_ingreso, "+
		"CASE WHEN i.preingreso IS NOT NULL THEN i.preingreso ELSE '' END AS indpre," +
		"CASE WHEN i.reingreso IS NOT NULL THEN i.reingreso ELSE 0 END AS indrei, " +
		"c.id AS codigo_cuenta, "+ 
		"getcentroatencioncc(c.area) AS centro_atencion, "+
		"(to_char(c.fecha_apertura, '"+ConstantesBD.formatoFechaAp+"')||' - '||c.hora_apertura) as fecha_apertura, "+
		"getnombreestadocuenta(c.estado_cuenta) as estado_cuenta, "+
		"getnombreviaingresotipopac(c.id) as via_ingreso, "+
		"getintegridaddominio(i.estado) AS estado_ingreso, " +
		"CASE WHEN getestadoasocio(c.id,c.tipo_paciente) > 9 AND getestadoasocio(c.id,c.tipo_paciente) < 13 THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS existe_asocio," +
		"i.consecutivo AS consecutivo_ingreso, " +
		"to_char(getfechaingreso(c.id,c.via_ingreso),'"+ConstantesBD.formatoFechaAp+"') AS fecha_ingreso," +
		"c.hospital_dia," +
		"CASE WHEN pes.ingreso IS NULL THEN coalesce(getDescEntitadSubContratada(pes.entidad_subcontratada),'') ELSE coalesce(getDescripEntidadSubXingreso(c.id_ingreso),'') END AS descripcionentidadsub "+ 
		"FROM  cuentas c "+ 
		"INNER JOIN ingresos i ON(i.id=c.id_ingreso) "+
		"LEFT OUTER JOIN pac_entidades_subcontratadas pes ON(pes.consecutivo=i.pac_entidades_subcontratadas)	 "+
		"WHERE "+ 
		"c.codigo_paciente = ? AND "+ 
		"getcuentafinal(c.id) IS NULL AND " +
		"i.estado IN ('"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"','"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"') and " +
		//Filtro para que no se muestren las cuentas que se han cerrado de un asocio
		"esCuentaCerradaAsocio(c.id) = '"+ConstantesBD.acronimoNo+"' "+
		"ORDER BY c.fecha_apertura DESC,c.hora_apertura DESC ";
	
	
	
	/**
	 * Cadena que consulta las solicitudes de cirugia sin cargo generado
	 */
	private static final String cargarSolicitudesCxSinCargoSubCuentaStr  = "SELECT "+ 
	  	"sol.numero_solicitud as numero_solicitud, "+
	  	"sc.sub_cuenta as sub_cuenta, "+
	  	"gettieneportatilsolicitud(sol.numero_solicitud) as codigo_portatil, "+
	  	"sol.consecutivo_ordenes_medicas as orden, "+ 
	  	"sol.tipo as codigo_tipo_solicitud, "+ 
	  	"getintegridaddominio(sc.ind_qx) as nombre_tipo_solicitud, "+ 
	  	"to_char(sol.fecha_grabacion,'"+ConstantesBD.formatoFechaAp+"') as fecha_grabacion, "+
	  	"to_char(sol.fecha_solicitud,'"+ConstantesBD.formatoFechaAp+"') as fecha_solicitud, "+
	  	"getestadosolhis(sol.estado_historia_clinica) as estado_medico, "+
	  	"getnomcentrocosto(sol.centro_costo_solicitante) as centro_costo_solicitante, "+  
	  	"getnomcentrocosto(sol.centro_costo_solicitado) as centro_costo_solicitado, "+  
	  	"CASE WHEN sol.pyp = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'Si' ELSE 'No' END as es_pyp, " +
	  	"getnombreviaingreso(getviaingresocuenta(sol.cuenta)) AS nombre_via_ingreso," +
	  	"0 as valor, " +
	  	"0 as recargo, " +
	  	"0 as iva, " +
	  	"CASE WHEN getSolicitudTieneIncluidos(sol.numero_solicitud) > 0 THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS tieneincluidos "+
	  	"FROM cuentas c " +
	  	"INNER JOIN solicitudes sol ON(sol.cuenta=c.id) " +
	  	"INNER JOIN solicitudes_cirugia sc ON(sc.numero_solicitud=sol.numero_solicitud) " +
	  	"WHERE " +
	  	"c.id_ingreso = ? AND sc.sub_cuenta = ? AND tieneCargosSolicitud(sol.numero_solicitud) = '"+ConstantesBD.acronimoNo+"' ";

	/**
	 * Cadena que consulta las solicitudes de cirugia sin cargo generado
	 */
	private static final String cargarSolicitudesCxSinCargoSubCuentaOracleStr  = "SELECT "+ 
	  	"sol.numero_solicitud as numero_solicitud, "+
	  	"sc.sub_cuenta as sub_cuenta, "+
	  	"gettieneportatilsolicitud1(sol.numero_solicitud) as codigo_portatil, "+
	  	"sol.consecutivo_ordenes_medicas as orden, "+ 
	  	"sol.tipo as codigo_tipo_solicitud, "+ 
	  	"getintegridaddominio(sc.ind_qx) as nombre_tipo_solicitud, "+ 
	  	"to_char(sol.fecha_grabacion,'"+ConstantesBD.formatoFechaAp+"') as fecha_grabacion, "+
	  	"to_char(sol.fecha_solicitud,'"+ConstantesBD.formatoFechaAp+"') as fecha_solicitud, "+
	  	"getestadosolhis(sol.estado_historia_clinica) as estado_medico, "+
	  	"getnomcentrocosto(sol.centro_costo_solicitante) as centro_costo_solicitante, "+  
	  	"getnomcentrocosto(sol.centro_costo_solicitado) as centro_costo_solicitado, "+  
	  	"CASE WHEN sol.pyp = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'Si' ELSE 'No' END as es_pyp, " +
	  	"getnombreviaingreso(getviaingresocuenta(sol.cuenta)) AS nombre_via_ingreso," +
	  	"0 as valor, " +
	  	"0 as recargo, " +
	  	"0 as iva, " +
	  	"CASE WHEN getSolicitudTieneIncluidos(sol.numero_solicitud) > 0 THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS tieneincluidos "+
	  	"FROM cuentas c " +
	  	"INNER JOIN solicitudes sol ON(sol.cuenta=c.id) " +
	  	"INNER JOIN solicitudes_cirugia sc ON(sc.numero_solicitud=sol.numero_solicitud) " +
	  	"WHERE " +
	  	"c.id_ingreso = ? AND sc.sub_cuenta = ? AND tieneCargosSolicitud(sol.numero_solicitud) = '"+ConstantesBD.acronimoNo+"' ";
	
	
	/**
	 * Seccion SELECT que consulta todas las solicitudes de una sub_cuenta
	 */
	  private static final String cargarTodasSolicitudesSubCuentaStr_SELECT= "SELECT "+ 
	  	"sol.numero_solicitud as numero_solicitud, "+ 
	  	"dc.sub_cuenta as sub_cuenta, "+ 
	  	"gettieneportatilsolicitud(sol.numero_solicitud) as codigo_portatil, "+
	  	"sol.consecutivo_ordenes_medicas as orden, "+ 
	  	"sol.tipo as codigo_tipo_solicitud, "+ 
	  	"CASE WHEN sol.tipo = "+ConstantesBD.codigoTipoSolicitudCirugia+" THEN " +
	  		"getintegridaddominio(getIndCargoSolicitudCx(sol.numero_solicitud)) " +
	  	"ELSE  " +
	  		"getnomtiposolicitud(sol.tipo) " +
	  	"END as nombre_tipo_solicitud, "+ 
	  	"to_char(sol.fecha_grabacion,'"+ConstantesBD.formatoFechaAp+"') as fecha_grabacion, "+
	  	"to_char(sol.fecha_solicitud,'"+ConstantesBD.formatoFechaAp+"') as fecha_solicitud, "+
	  	"getestadosolhis(sol.estado_historia_clinica) as estado_medico, "+
	  	"getnomcentrocosto(sol.centro_costo_solicitante) as centro_costo_solicitante, "+  
	  	"getnomcentrocosto(sol.centro_costo_solicitado) as centro_costo_solicitado, "+  
	  	"CASE WHEN sol.pyp = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'Si' ELSE 'No' END as es_pyp, " +
	  	"getnombreviaingreso(getviaingresocuenta(sol.cuenta)) AS nombre_via_ingreso,"+
	  	"case when (sum(coalesce(dc.valor_unitario_cargado * dc.cantidad_cargada,0)) + sum(coalesce(dc.valor_unitario_recargo * dc.cantidad_cargada,0)) - sum(coalesce(dc.valor_unitario_dcto * dc.cantidad_cargada,0)))<0 then 0 else (sum(coalesce(dc.valor_unitario_cargado * dc.cantidad_cargada,0)) + sum(coalesce(dc.valor_unitario_recargo * dc.cantidad_cargada,0)) - sum(coalesce(dc.valor_unitario_dcto * dc.cantidad_cargada,0))) end  AS valor, "+
	  	"sum(coalesce(dc.valor_unitario_recargo * dc.cantidad_cargada,0)) AS recargo, "+
	  	"sum(coalesce(dc.valor_unitario_iva * dc.cantidad_cargada,0)) AS iva ," +
	  	"CASE WHEN getSolicitudTieneIncluidos(sol.numero_solicitud) > 0 THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END  AS tieneincluidos "+ 
	  	"FROM det_cargos dc "+ 
	  	"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+ 
	  	"WHERE "+ 
	  	"dc.sub_cuenta = ? AND dc.paquetizado = '"+ConstantesBD.acronimoNo+"' AND dc.eliminado='"+ConstantesBD.acronimoNo+"'  " ;
	  	//Este filtro se usa para ignorar cargos que fueron paqutizados en su totalidad y qued'el cargo en 0
	  	//"(dc.estado <> "+ConstantesBD.codigoEstadoFCargada+" OR (dc.estado="+ConstantesBD.codigoEstadoFCargada+" AND (dc.valor_total_cargado > 0 OR dc.valor_total_cargado IS NULL)))";
	  

	  	/**
		 * Seccion SELECT que consulta todas las solicitudes de una sub_cuenta
		 */
		  private static final String cargarTodasSolicitudesSubCuentaOracleStr_SELECT= "SELECT "+ 
		  	"sol.numero_solicitud as numero_solicitud, "+ 
		  	"dc.sub_cuenta as sub_cuenta, "+ 
		  	"gettieneportatilsolicitud1(sol.numero_solicitud) as codigo_portatil, "+
		  	"sol.consecutivo_ordenes_medicas as orden, "+ 
		  	"sol.tipo as codigo_tipo_solicitud, "+ 
		  	"CASE WHEN sol.tipo = "+ConstantesBD.codigoTipoSolicitudCirugia+" THEN " +
		  		"getintegridaddominio(getIndCargoSolicitudCx(sol.numero_solicitud)) " +
		  	"ELSE  " +
		  		"getnomtiposolicitud(sol.tipo) " +
		  	"END as nombre_tipo_solicitud, "+ 
		  	"to_char(sol.fecha_grabacion,'"+ConstantesBD.formatoFechaAp+"') as fecha_grabacion, "+
		  	"to_char(sol.fecha_solicitud,'"+ConstantesBD.formatoFechaAp+"') as fecha_solicitud, "+
		  	"getestadosolhis(sol.estado_historia_clinica) as estado_medico, "+
		  	"getnomcentrocosto(sol.centro_costo_solicitante) as centro_costo_solicitante, "+  
		  	"getnomcentrocosto(sol.centro_costo_solicitado) as centro_costo_solicitado, "+  
		  	"CASE WHEN sol.pyp = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'Si' ELSE 'No' END as es_pyp, " +
		  	"getnombreviaingreso(getviaingresocuenta(sol.cuenta)) AS nombre_via_ingreso,"+
		  	"case when (sum(coalesce(dc.valor_unitario_cargado * dc.cantidad_cargada,0)) + sum(coalesce(dc.valor_unitario_recargo * dc.cantidad_cargada,0)) - sum(coalesce(dc.valor_unitario_dcto * dc.cantidad_cargada,0)))<0 then 0 else (sum(coalesce(dc.valor_unitario_cargado * dc.cantidad_cargada,0)) + sum(coalesce(dc.valor_unitario_recargo * dc.cantidad_cargada,0)) - sum(coalesce(dc.valor_unitario_dcto * dc.cantidad_cargada,0))) end  AS valor, "+
		  	"sum(coalesce(dc.valor_unitario_recargo * dc.cantidad_cargada,0)) AS recargo, "+
		  	"sum(coalesce(dc.valor_unitario_iva * dc.cantidad_cargada,0)) AS iva ," +
		  	"CASE WHEN getSolicitudTieneIncluidos(sol.numero_solicitud) > 0 THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END  AS tieneincluidos "+ 
		  	"FROM det_cargos dc "+ 
		  	"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+ 
		  	"WHERE "+ 
		  	"dc.sub_cuenta = ? AND dc.paquetizado = '"+ConstantesBD.acronimoNo+"' AND dc.eliminado='"+ConstantesBD.acronimoNo+"'  " ;
		  	//Este filtro se usa para ignorar cargos que fueron paqutizados en su totalidad y qued'el cargo en 0
		  	//"(dc.estado <> "+ConstantesBD.codigoEstadoFCargada+" OR (dc.estado="+ConstantesBD.codigoEstadoFCargada+" AND (dc.valor_total_cargado > 0 OR dc.valor_total_cargado IS NULL)))";
		  

		  /**
	   * Seccion GROUP/ORDER que consulta todas las solicitudes de una sub_cuenta
	   */
	  private static final String cargarTodasSolicitudesSubCuentaStr_GROUP_ORDER =  " " +
	  	"GROUP BY dc.sub_cuenta,sol.numero_solicitud, sol.consecutivo_ordenes_medicas, sol.tipo,sol.fecha_grabacion, sol.fecha_solicitud, sol.estado_historia_clinica, sol.centro_costo_solicitante, sol.centro_costo_solicitado, sol.pyp, sol.cuenta "; 
	  	//"ORDER BY sol.numero_solicitud ASC";
	
	  /**
		 * Hallar el total del valor de los cargos
		 * este dato se saca de todas las solicitudes del paciente
		 * en estado cargada
		 */
		private static String totalValorCargosStr= "SELECT " +
			"sum(coalesce(valor_total_cargado,0)) + sum(coalesce(valor_unitario_recargo * cantidad_cargada,0)) - sum(coalesce(valor_unitario_dcto * cantidad_cargada,0)) AS total_cargo " +
			"FROM det_cargos " +
			"WHERE sub_cuenta = ? AND eliminado='"+ConstantesBD.acronimoNo+"'  AND estado = "+ConstantesBD.codigoEstadoFCargada+" AND paquetizado = '"+ConstantesBD.acronimoNo+"' ";
		
		/**
		 * Se hala el total de valor de los cargos
		 * de una sub cuenta del paciente sin tomar en cuenta las solicitudes de pyp
		 */
		private static String totalValorCargosSinPypStr = "SELECT "+ 
			"sum(coalesce(dc.valor_total_cargado,0)) + sum(coalesce(dc.valor_unitario_recargo * dc.cantidad_cargada,0)) - sum(coalesce(valor_unitario_dcto * dc.cantidad_cargada,0)) AS total_cargo "+
			"FROM det_cargos dc "+ 
			"INNER JOIN solicitudes s ON(s.numero_solicitud=dc.solicitud) "+ 
			"WHERE " +
			"dc.sub_cuenta = ? AND " +
			"dc.estado = "+ConstantesBD.codigoEstadoFCargada+" AND dc.eliminado='"+ConstantesBD.acronimoNo+"' AND " +
			"dc.paquetizado = '"+ConstantesBD.acronimoNo+"' AND " +
			"(s.pyp IS NULL OR s.pyp = "+ValoresPorDefecto.getValorFalseParaConsultas()+")"; 
		
		/**
		 * Cadena para cargar el monto de cobro de la subCuenta
		 */
		private static String cargarMontoCobroSubCuentaStr = "SELECT " +
			"coalesce(mc.valor||'','') AS valor, " +
			"coalesce(mc.porcentaje||'','') AS porcentaje " +
			"FROM sub_cuentas sc " +
			"INNER JOIN montos_cobro mc ON(mc.codigo=sc.monto_cobro) " +
			"WHERE sc.sub_cuenta = ?";
		
		/**
		 * Cadena para cargar el detalle del cargo servicio/articulo
		 */
		private static final String cargarDetalleCargoServicioArticuloStr = "SELECT "+ 
			"getnombreesquematarifario(dc.esquema_tarifario) AS esquema_tarifario, "+
			"CASE WHEN dc.servicio IS NOT NULL THEN getcodigoespecialidad(dc.servicio) || '-' || getcodigoservicio(dc.servicio, "+ConstantesBD.codigoTarifarioCups+") ELSE dc.articulo || '' END AS codigo, "+
			"CASE WHEN dc.servicio IS NOT NULL THEN getnombreservicio(dc.servicio,"+ConstantesBD.codigoTarifarioCups+") ELSE getdescarticulo(dc.articulo) END AS descripcion, "+
			"coalesce(dc.cantidad_cargada,0) AS cantidad, "+
			"coalesce(dc.valor_total_cargado,0) AS total_cargo, "+
			"coalesce(dc.valor_unitario_recargo * dc.cantidad_cargada,0) AS total_recargo, "+
			"coalesce(dc.valor_unitario_dcto * dc.cantidad_cargada,0) AS total_descuento, "+
			"CASE WHEN dc.estado = "+ConstantesBD.codigoEstadoFPendiente+" THEN '' ELSE to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') || ' - ' || dc.hora_modifica END AS fecha_cargo, "+
			"getestadosolfac(dc.estado) AS estado, "+
			"CASE WHEN dc.facturado = '"+ConstantesBD.acronimoSi+"' THEN (select coalesce(f.pref_factura||'-','') || f.consecutivo_factura from facturas f where f.codigo=dc.codigo_factura) ELSE '' END AS numero_factura "+ 
			"FROM det_cargos dc "+ 
			"WHERE "+ 
			"dc.solicitud = ? AND dc.sub_cuenta = ? AND dc.eliminado='"+ConstantesBD.acronimoNo+"'  AND dc.paquetizado = '"+ConstantesBD.acronimoNo+"'  ";
			//Este filtro se usa para ignorar cargos que fueron paqutizados en su totalidad y qued'el cargo en 0
			//"(dc.estado <> "+ConstantesBD.codigoEstadoFCargada+" OR (dc.estado="+ConstantesBD.codigoEstadoFCargada+" AND (dc.valor_total_cargado > 0 OR dc.valor_total_cargado IS NULL))) ";
			
		/**
		 * Cadena que carga el detalle del art&iacute;culo con despacho de equivalentes
		 * @author Diana Ruiz
		 */
		
		private static final String cargarDetalleCargoArticuloConEquivalente =  "SELECT DISTINCT dd.articulo, " + 
		"getnombreesquematarifario(dc.esquema_tarifario) AS esquema_tarifario, dc.articulo AS codigo, getdescarticulo(dc.articulo) AS descripcion, " +		
		"coalesce(dc.cantidad_cargada,0) AS cantidad, " +
		"coalesce(dc.valor_total_cargado,0) AS total_cargo, " +
		"coalesce(dc.valor_unitario_recargo * dc.cantidad_cargada,0) AS total_recargo, " +
		"coalesce(dc.valor_unitario_dcto * dc.cantidad_cargada,0) AS total_descuento, " + 		
		"CASE WHEN dc.estado = "+ConstantesBD.codigoEstadoFPendiente+" THEN '' ELSE to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') || ' - ' || dc.hora_modifica END AS fecha_cargo, " +
		"getestadosolfac(dc.estado) AS estado, " +		
		"CASE WHEN dc.facturado = '"+ConstantesBD.acronimoSi+"' THEN (select coalesce(f.pref_factura||'-','') || f.consecutivo_factura " +
		"FROM facturas f where f.codigo=dc.codigo_factura) ELSE '' END AS numero_factura " + 
		"FROM det_cargos dc inner join despacho d on (dc.solicitud = d.numero_solicitud) " +
		"inner join detalle_despachos dd on (dd.despacho = d.orden and dd.articulo = dc.articulo)  " + 
		"WHERE dc.solicitud = ? " +
		"AND dc.sub_cuenta = ? " +
		"AND dc.eliminado='"+ConstantesBD.acronimoNo+"'  " +
		"AND dc.paquetizado = '"+ConstantesBD.acronimoNo+"'" +
		"AND dd.cantidad > 0";	
		
		
		/**
		 * Cadena que carga el detalle del cargo de una cirugía
		 */
		private static final String cargarDetalleCargoCirugiaStr = "SELECT "+ 
			"getnombreesquematarifario(dc.esquema_tarifario) AS esquema_tarifario, "+
			"dc.servicio_cx AS codigo_cirugia, "+
			"coalesce(dc.cantidad_cargada,0) AS cantidad, "+
			"coalesce(dc.valor_total_cargado,0) AS total_cargo, "+
			"coalesce(dc.valor_unitario_recargo * dc.cantidad_cargada,0) AS total_recargo, "+
			"CASE WHEN dc.estado = "+ConstantesBD.codigoEstadoFPendiente+" THEN '' ELSE to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') || ' - ' || dc.hora_modifica END AS fecha_cargo, "+
			"dc.estado AS codigo_estado, "+
			"getestadosolfac(dc.estado) AS estado, " +
			"ta.codigo_asocio AS codigo_asocio, "+
			"ta.nombre_asocio || ' (' || getnombretiposervicio(ta.tipos_servicio) || ')' AS nombre_asocio," +
			"CASE WHEN ta.tipos_servicio = '"+ConstantesBD.codigoServicioHonorariosCirugia+"' THEN getNomProfesionalAsocio(dc.det_cx_honorarios) ELSE '' END AS nombre_profesional, " +
			"CASE WHEN dc.det_cx_honorarios IS NULL THEN '' ELSE getintegridaddominio(getcobrableAsocio(dc.det_cx_honorarios)) END AS cobrable, " +
			"'(' || getcodigoespecialidad(dc.servicio) || '-' || dc.servicio || ') ' || getnombreservicio(dc.servicio,"+ConstantesBD.codigoTarifarioCups+") as nombre_servicio_asocio," +
			"dc.paquetizado AS paquetizado "+ 
			"FROM det_cargos dc "+ 
			"INNER JOIN tipos_asocio ta ON(ta.codigo=dc.tipo_asocio) "+ 
			"WHERE "+ 
			"dc.solicitud = ? AND " +
			"dc.sub_cuenta = ? AND dc.eliminado='"+ConstantesBD.acronimoNo+"'  AND " +
			"dc.servicio_cx = ? " ;
			
		
		/**
		 * Cadena que consulta los carnet de un paciente cargado
		 */
		private static String cargarCarnet="SELECT getobtenercarnetpaciente(sb.convenio,sb.ingreso) AS carnet " +
											"FROM sub_cuentas sb WHERE sb.convenio=? AND sb.ingreso=? ";
		
		/**
		 * Cadena que verifica el estado de la cuenta y trae la fecha del egreso
		 */
		private static String fechaEgreso="SELECT coalesce(to_char(i.fecha_egreso, 'DD/MM/YYYY'), '') AS fechae, c.estado_cuenta AS estado " +
											"FROM ingresos i INNER JOIN cuentas c ON (i.id=c.id_ingreso) " +
											"WHERE i.id=? ";
		
		
		
		/**
		 * Consulta TipoMonto y Valores Paciente de las Facturas de la Subcuenta
		 */
		private static String consultaTipoMontoValoresPaciente="SELECT tabla.tipom AS tipomp, " +
																		"sum(tabla.valori) AS valorip, " +
																		"sum(tabla.valori2) AS valori2p " +
																"FROM " +
																	"(SELECT tp.nombre AS tipom, " +
																			"sum(coalesce((f.valor_bruto_pac-f.val_desc_pac),0)) AS valori, " +
																			"CASE WHEN f.estado_paciente="+ConstantesBD.codigoEstadoFacturacionPacienteCancelada+" THEN sum(coalesce((f.valor_bruto_pac-f.val_desc_pac),0)) ELSE sum(f.valor_abonos) END AS valori2 " +
																	"FROM det_cargos dc " +
																			"LEFT OUTER JOIN facturas f ON (dc.codigo_factura=f.codigo) " +
																			"LEFT OUTER JOIN tipos_monto tp ON (f.tipo_monto=tp.codigo) " +
																	"WHERE dc.sub_cuenta=? AND dc.codigo_factura IS NOT NULL " +
																	"GROUP BY tp.nombre,f.estado_paciente,f.codigo) tabla ";
	
	
	/**
	 * Implementación del método que carga todas las
	 * cuentas del paciente en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.EstadoCuentaDao#cargarTodasCuentaPaciente (Connection , int ) throws SQLException 
	 */
	public static ArrayList<HashMap<String, Object>> cargarTodasCuentaPaciente (Connection con, int codigoPaciente) 
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarTodasCuentaPacienteStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoPaciente);
			
			logger.info("consulta=> "+cargarTodasCuentaPacienteStr+"\n\ncodigoPaciente: "+codigoPaciente);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("idIngreso",rs.getObject("id_ingreso"));
				elemento.put("indpre",rs.getObject("indpre")==null?"":rs.getObject("indpre")+"");
				elemento.put("indrei",rs.getObject("indrei")==null?"":rs.getObject("indrei")+"");
				elemento.put("idCuenta",rs.getObject("codigo_cuenta"));
				elemento.put("centroAtencion",rs.getObject("centro_atencion"));
				elemento.put("fechaApertura",rs.getObject("fecha_apertura"));
				elemento.put("estadoCuenta",rs.getObject("estado_cuenta"));
				elemento.put("viaIngreso",rs.getObject("via_ingreso"));
				elemento.put("estadoIngreso",rs.getObject("estado_ingreso"));
				elemento.put("existeAsocio",rs.getObject("existe_asocio")==null?"":rs.getObject("existe_asocio")+"");
				elemento.put("consecutivoIngreso",rs.getObject("consecutivo_ingreso"));
				elemento.put("fechaIngreso",rs.getObject("fecha_ingreso"));
				elemento.put("hospitalDia",rs.getObject("hospital_dia"));
				elemento.put("descripcionentidadsub",rs.getObject("descripcionentidadsub")==null?"":rs.getObject("descripcionentidadsub")+"");
				
				resultados.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarTodasCuentaPaciente: "+e);
			e.printStackTrace();
		}
		return resultados;
		
		
	}
	
	/**
	 * Método que consulta todos los convenios de un ingreso
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> cargarTodosConvenioIngreso (Connection con, String idIngreso,String cargarTodosConveniosIngresoStr) 
	{
		logger.info("CODIGO DEL INGRESSSSSSOOOOOO>>>>>>>>>>>>>>"+idIngreso);
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarTodosConveniosIngresoStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(idIngreso));
			logger.info("CONSULTAAAAAA JOOOOO>>>>>>>>>>"+cargarTodosConveniosIngresoStr+" -->"+idIngreso);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("idSubCuenta",rs.getObject("sub_cuenta"));
				elemento.put("codigoConvenio",rs.getObject("codigo_convenio")==null?"":rs.getObject("codigo_convenio")+"");
				elemento.put("codigoTipoRegimen",rs.getObject("tipo_regimen")==null?"":rs.getObject("tipo_regimen")+"");
				elemento.put("nombreConvenio",rs.getObject("nombre_convenio")==null?"":rs.getObject("nombre_convenio")+"");
				elemento.put("nombreEstratoSocial",rs.getObject("estrato_social")==null?"":rs.getObject("estrato_social"));
				elemento.put("nombreMontoCobro",rs.getObject("nombre_monto_cobro")==null?"":rs.getObject("nombre_monto_cobro")+"");
				elemento.put("nombreTipoMonto",rs.getObject("tipo_monto")==null?"":rs.getObject("tipo_monto"));
				elemento.put("codigoNaturaleza",rs.getObject("codigo_naturaleza")==null?"":rs.getObject("codigo_naturaleza")+"");
				elemento.put("nombreNaturaleza",rs.getObject("nombre_naturaleza")==null?"":rs.getObject("nombre_naturaleza")+"");
				elemento.put("esConvenioPoliza",rs.getObject("es_convenio_poliza")==null?"":rs.getObject("es_convenio_poliza")+"");
				elemento.put("facturas",rs.getObject("numerofactura")==null?"":rs.getObject("numerofactura")+"");
				resultados.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarTodosConvenioIngreso: "+e);
			e.printStackTrace();
		}
		return resultados;
	}
	
	 /**
	  * Método que consulta las solicitudes de un convenio
	  * @param con
	  * @param campos
	  * @return
	  */
	public static HashMap<String, Object> cargarTodasSolicitudesSubCuenta (Connection con, HashMap campos) 
	{
		HashMap<String, Object> resultado = new HashMap<String, Object>();
		String consultatempo="";
		if((System.getProperty("TIPOBD")+"").equals("ORACLE"))
		{
			consultatempo=cargarSolicitudesCxSinCargoSubCuentaOracleStr;
		}
		else
		{
			consultatempo=cargarSolicitudesCxSinCargoSubCuentaStr;
		}
		String consultatempo1="";
		if((System.getProperty("TIPOBD")+"").equals("ORACLE"))
		{
			consultatempo1=cargarTodasSolicitudesSubCuentaOracleStr_SELECT;
		}
		else
		{
			consultatempo1=cargarTodasSolicitudesSubCuentaStr_SELECT;
		}
		try
		{
			String consulta = "SELECT * FROM (" +
					"( " + consultatempo + ") " +
					"UNION " +
					"( " + consultatempo1 + cargarTodasSolicitudesSubCuentaStr_GROUP_ORDER + ") " +
					") t " +
					"ORDER BY t.numero_solicitud ASC ";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setObject(1,campos.get("idIngreso"));
			pst.setLong(2, Utilidades.convertirALong(campos.get("idSubCuenta")+""));
			pst.setObject(3,campos.get("idSubCuenta"));
			logger.info("CONSULTA SOLICITUDES SUBCUENTA=> "+consulta+"\n\n\ncampos:"+campos);
			resultado = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			
			
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarTodasSolicitudesSubCuenta: "+e);
		}
		return resultado;
	}
	
	/**
	 * Método que realiza la busqueda avanzada de las solicitudes de una sub cuenta
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap<String, Object> buscarSolicitudesSubCuenta(Connection con,HashMap campos)
	{
		HashMap<String, Object> resultado = new HashMap<String, Object>();
		String consultatempo="";
		if((System.getProperty("TIPOBD")+"").equals("ORACLE"))
		{
			consultatempo=cargarSolicitudesCxSinCargoSubCuentaOracleStr;
		}
		else
		{
			consultatempo=cargarSolicitudesCxSinCargoSubCuentaStr;
		}
		try
		{
			
			//String consulta = cargarTodasSolicitudesSubCuentaStr_SELECT;
			String consulta = "SELECT * FROM (" +
				"( " + consultatempo;
			
			//***********SE ANALIZA LOS CAMPOS DE LA BUSQUEDA AVANZADA************
			
			//Tipo Solicitud
			if(!campos.get("tipoSolicitud").toString().equals(""))
				consulta += " AND sol.tipo = "+campos.get("tipoSolicitud"); 
			
			//Orden
			if(!campos.get("orden").toString().equals(""))
				consulta += " AND sol.consecutivo_ordenes_medicas = "+campos.get("orden");
			
			//Fecha Orden
			if(!campos.get("fechaOrden").toString().equals(""))
				consulta += " AND sol.fecha_solicitud = '"+UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaOrden").toString())+"' ";
			
			//Fecha Grabación
			if(!campos.get("fechaGrabacion").toString().equals(""))
				consulta += " AND sol.fecha_grabacion = '"+UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaGrabacion").toString())+"' ";
			
			//Estado Medico
			if(!campos.get("estadoMedico").toString().equals(""))
				consulta += " AND sol.estado_historia_clinica = "+campos.get("estadoMedico");
			
			//Centro Costo Solicitante
			if(!campos.get("centroCostoSolicitante").toString().equals(""))
				consulta += " AND sol.centro_costo_solicitante = "+campos.get("centroCostoSolicitante");
			
			//Centro Costo Solicitado
			if(!campos.get("centroCostoSolicitado").toString().equals(""))
				consulta += " AND sol.centro_costo_solicitado = "+campos.get("centroCostoSolicitado");
			
			String consultatempo1="";
			if((System.getProperty("TIPOBD")+"").equals("ORACLE"))
			{
				consultatempo1=cargarTodasSolicitudesSubCuentaOracleStr_SELECT;
			}
			else
			{
				consultatempo1=cargarTodasSolicitudesSubCuentaStr_SELECT;
			}
			
			consulta += ") UNION ( "+ consultatempo1; 
			
			//*********************************************************************
			//***********SE ANALIZA LOS CAMPOS DE LA BUSQUEDA AVANZADA************
			
			//Tipo Solicitud
			if(!campos.get("tipoSolicitud").toString().equals(""))
				consulta += " AND dc.tipo_solicitud = "+campos.get("tipoSolicitud"); 
			
			//Orden
			if(!campos.get("orden").toString().equals(""))
				consulta += " AND sol.consecutivo_ordenes_medicas = "+campos.get("orden");
			
			//Fecha Orden
			if(!campos.get("fechaOrden").toString().equals(""))
				consulta += " AND sol.fecha_solicitud = '"+UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaOrden").toString())+"' ";
			
			//Fecha Grabación
			if(!campos.get("fechaGrabacion").toString().equals(""))
				consulta += " AND sol.fecha_grabacion = '"+UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaGrabacion").toString())+"' ";
			
			//Estado Medico
			if(!campos.get("estadoMedico").toString().equals(""))
				consulta += " AND sol.estado_historia_clinica = "+campos.get("estadoMedico");
			
			//Centro Costo Solicitante
			if(!campos.get("centroCostoSolicitante").toString().equals(""))
				consulta += " AND sol.centro_costo_solicitante = "+campos.get("centroCostoSolicitante");
			
			//Centro Costo Solicitado
			if(!campos.get("centroCostoSolicitado").toString().equals(""))
				consulta += " AND sol.centro_costo_solicitado = "+campos.get("centroCostoSolicitado");
			
			consulta += cargarTodasSolicitudesSubCuentaStr_GROUP_ORDER + ") " +
			") t ";
			//Estado Facturacion
			if(!campos.get("estadoFacturacion").toString().equals(""))
				consulta += " WHERE TIENECARGOSENESTADO(t.sub_cuenta,t.numero_solicitud,"+campos.get("estadoFacturacion")+")= '"+ConstantesBD.acronimoSi+"'";;

			consulta +=" ORDER BY t.numero_solicitud ASC ";
			
			//*******************************************************************************************
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setObject(1,campos.get("idIngreso"));
			pst.setObject(2,campos.get("idSubCuenta"));
			pst.setObject(3,campos.get("idSubCuenta"));
			
			resultado = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			
			
		}
		catch(SQLException e)
		{
			logger.error("Error en buscarSolicitudesSubCuenta: "+e);
		}
		return resultado;
	}
	
	/**
	 * Valor total de los cargos (Independiente convenio o paciente)
	 * @param con
	 * @param cuenta
	 * @param incluyePyp (para saber sis e incluyen las solicitudes de pyp en el calculo del cargo totsl)
	 * @return
	 */
	public static double valorTotalCargos(Connection con, String idSubCuenta,boolean incluyePyp)
	{
		try
		{
			double vTotal = 0;
			PreparedStatementDecorator total= new PreparedStatementDecorator(con.prepareStatement(incluyePyp?totalValorCargosStr:totalValorCargosSinPypStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			total.setLong(1, Utilidades.convertirALong(idSubCuenta));
			ResultSetDecorator rs = new ResultSetDecorator(total.executeQuery());
			if(rs.next())
				vTotal = rs.getDouble("total_cargo");
			
			
			return vTotal;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el total de pagos del paciente en SqlBaseEstadoCuentaDao "+e);
			return 0;
		}
	}
	
	/**
	 * Método que carga la informacion del monto de cobro para un convenio específico
	 * @param con
	 * @param idSubCuenta
	 * @return
	 */
	public static HashMap cargarMontoCobroSubCuenta(Connection con,String idSubCuenta)
	{
		HashMap resultado = new HashMap();
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarMontoCobroSubCuentaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setLong(1, Utilidades.convertirALong(idSubCuenta));
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), false, true);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarMontoCobroSubCuenta: "+e);
		}
		return resultado;
	}
	
	/**
	 * Método para obtener los abonos del paciente segun cada posible estado de la cuenta del paciente
	 * @param con
	 * @param cuenta
	 * @param codigoPaciente
	 * @param ingreso Id ingreso del paciente
	 * @return
	 */
	public static double consultarAbonos(Connection con,int idCuenta, int codigoPaciente, Integer ingreso)
	{
		String consultaAbonosStr=
			"SELECT " +
				"CASE WHEN " +
					"cue.estado_cuenta<>"+ConstantesBD.codigoEstadoCuentaFacturada+"  "+
						"AND " +
					"cue.estado_cuenta<>"+ConstantesBD.codigoEstadoCuentaCerrada+" AND cue.estado_cuenta<>"+ConstantesBD.codigoEstadoCuentaExcenta+" " +
				"THEN " +
					" manejopaciente.getabonodisponible(?, ?) " +
				/*
					"(" +
						"SELECT " +
							"CASE WHEN " +
								"sum(ma.valor) IS NULL " +
							"THEN " +
								"0 " +
							"ELSE " +
								"sum(ma.valor) " +
							"END " +
						"FROM " +
							"movimientos_abonos ma " +
						"INNER JOIN " +
							"tipos_mov_abonos tmv " +
								"ON(ma.tipo=tmv.codigo) " +
						"WHERE " +
							"tmv.operacion='suma' " +
						"AND " +
							"ma.paciente=?" +
					")" +
						"-" +
					"(" +
						"SELECT " +
							"CASE WHEN " +
								"sum(ma.valor) IS NULL " +
							"THEN " +
								"0 " +
							"ELSE " +
								"sum(ma.valor) " +
							"END " +
						"FROM " +
							"movimientos_abonos ma " +
						"INNER JOIN " +
							"tipos_mov_abonos tmv " +
								"ON(ma.tipo=tmv.codigo) " +
						"WHERE " +
							"tmv.operacion='resta' " +
						"AND ma.paciente=?" +
					") " +
					*/
				"ELSE " +
					"CASE WHEN cue.estado_cuenta="+ConstantesBD.codigoEstadoCuentaFacturada+"  THEN  " +
				"(SELECT sum(fac.valor_abonos) FROM facturas fac WHERE fac.cuenta=? AND fac.estado_facturacion<>"+ConstantesBD.codigoEstadoFacturacionAnulada+") " +
					"ELSE 0 END END as abonos " +
				"FROM " +
					"cuentas cue " +
				"WHERE " +
					"cue.id=? " +
				"AND " +
					"cue.codigo_paciente=?";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con, consultaAbonosStr);
			ps.setInt(1, codigoPaciente);
			if(ingreso==null)
			{
				ps.setNull(2, Types.INTEGER);
			}
			else
			{
				ps.setInt(2, ingreso);
			}
			ps.setInt(3, idCuenta);
			ps.setInt(4, idCuenta);
			ps.setInt(5, codigoPaciente);
			logger.info("consulta "+ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				if(rs.getDouble("abonos")>0)
					return rs.getDouble("abonos");
				else
					return 0;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error obteniendo el valor abonos del paciente en SqlBaseEstadoCuentaDao ", e);
			return 0;
		}
		return 0;
	}
	
	/**
	 * Método que consulta el detalle del cargo de un solicitud de servicio/articulo
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap<String, Object> cargarDetalleCargoServicioArticulo(Connection con,HashMap campos)
	{
		try
		{
			
			 /**Control de cambio Anexo de la cuenta (3463)
			  * desarrollador: leoquico
			  * fecha: 23-Abril-2013
			 * */
			StringBuilder consulta = new StringBuilder();
			consulta.append("SELECT ");
			consulta.append("getnombreesquematarifario(dc.esquema_tarifario) AS esquema_tarifario, ");
			consulta.append("CASE WHEN dc.servicio IS NOT NULL THEN getcodigoespecialidad(dc.servicio) || '-' || getcodigoservicio(dc.servicio, ");
			consulta.append(ConstantesBD.codigoTarifarioCups);
			consulta.append(") ELSE ");
			if (campos.get("tipoArticulo") == null) {

				 consulta.append("dc.articulo ");
			}
			else {
				if (campos.get("tipoArticulo").equals("") || campos.get("tipoArticulo").equals("AXM")) {

					  consulta.append("dc.articulo ");
				}
				if (campos.get("tipoArticulo").equals("CUM")) {

					  consulta.append("FACTURACION.getcodigocumarticulo(dc.articulo) ");
				}
				if (campos.get("tipoArticulo").equals("INZ")) {

					  consulta.append("FACTURACION.getcodigointerfaz(dc.articulo) ");
				}
			}
			consulta.append(" || '' END AS codigo, ");
			consulta.append("CASE WHEN dc.servicio IS NOT NULL THEN getnombreservicio(dc.servicio,");
			consulta.append(ConstantesBD.codigoTarifarioCups);
			consulta.append(") ELSE getdescarticulo(dc.articulo) END AS descripcion, ");
			consulta.append("coalesce(dc.cantidad_cargada,0) AS cantidad, ");
			consulta.append("coalesce(dc.valor_total_cargado,0) AS total_cargo, ");
			consulta.append("coalesce(dc.valor_unitario_recargo * dc.cantidad_cargada,0) AS total_recargo, ");
			consulta.append("coalesce(dc.valor_unitario_dcto * dc.cantidad_cargada,0) AS total_descuento, ");
			consulta.append("CASE WHEN dc.estado = ");
			consulta.append(ConstantesBD.codigoEstadoFPendiente);
			consulta.append(" THEN '' ELSE to_char(dc.fecha_modifica,'");
			consulta.append(ConstantesBD.formatoFechaAp);
			consulta.append("') || ' - ' || dc.hora_modifica END AS fecha_cargo, ");
			consulta.append("getestadosolfac(dc.estado) AS estado, ");
			consulta.append("CASE WHEN dc.facturado ='");
			consulta.append(ConstantesBD.acronimoSi);
			consulta.append("' THEN (select coalesce(f.pref_factura||'-','') || f.consecutivo_factura ");
			consulta.append("from facturas f where f.codigo=dc.codigo_factura) ELSE '' END AS numero_factura ");
			consulta.append("FROM det_cargos dc "); 
			consulta.append("WHERE ");
			consulta.append("dc.solicitud = ? AND dc.sub_cuenta = ? AND dc.eliminado='");
			consulta.append(ConstantesBD.acronimoNo);
			consulta.append("'  AND dc.paquetizado = '");
			consulta.append(ConstantesBD.acronimoNo);
			consulta.append("'");
			
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("numeroSolicitud")+""));
			pst.setInt(2,Utilidades.convertirAEntero(campos.get("idSubCuenta")+""));
			
			logger.info("===>Consulta: "+cargarDetalleCargoServicioArticuloStr);
			logger.info("===>Cuenta: "+campos.get("idCuenta"));
			logger.info("===>SubCuenta: "+campos.get("idSubCuenta"));
			logger.info("===>Numero Solicitud: "+campos.get("numeroSolicitud"));
						
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
	        pst.close();
			return mapaRetorno;
			

		}
		catch(SQLException e)
		{
			logger.error("Error en cargarDetalleCargoServicioArticulo: "+e,e);
			return null;
		}
		
	}
	/**
	 * 
	 * M&eacute;todo para mostrar el detalle de la solicitud con despacho de equivalentes 
	 * @author Diana Ruiz
	 * @param con
	 * @param campos
	 * @return
	 * 
	 */
	public static HashMap<String, Object> cargarDetalleCargoArticuloConEquivalente(
			Connection con, HashMap campos) {
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarDetalleCargoArticuloConEquivalente,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("numeroSolicitud")+""));
			pst.setInt(2,Utilidades.convertirAEntero(campos.get("idSubCuenta")+""));
			
			logger.info("===>Consulta Equivalente: "+cargarDetalleCargoArticuloConEquivalente);
			logger.info("===>Cuenta Equivalente: "+campos.get("idCuenta"));
			logger.info("===>SubCuenta Equivalente: "+campos.get("idSubCuenta"));
			logger.info("===>Numero Solicitud Equivalente: "+campos.get("numeroSolicitud"));
						
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
	        pst.close();
			return mapaRetorno;	

		}
		catch(SQLException e)
		{
			logger.error("Error en cargarDetalleCargoArticuloConEquivalente: "+e,e);
			return null;
		}
		
	}
	
	/**
	 * Método que carga el detalle del cargo de una cirugía
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap<String, Object> cargarDetalleCargoCirugia(Connection con, HashMap campos)
	{
		try
		{
			String consulta = cargarDetalleCargoCirugiaStr;
			boolean mostrarPaquetizacion = UtilidadTexto.getBoolean(campos.get("mostrarPaquetizacion").toString());
			
			if(mostrarPaquetizacion)
			{
				consulta += " AND dc.cantidad_cargada >0 ";
			}
			else
				consulta += " AND dc.paquetizado ='"+ConstantesBD.acronimoNo+"' " ; 
		
			consulta += " ORDER BY nombre_asocio";
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setObject(1,campos.get("numeroSolicitud"));
			pst.setLong(2, Utilidades.convertirALong(campos.get("idSubCuenta")+""));
			pst.setObject(3,campos.get("codigoServicio"));
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
	        pst.close();
			return mapaRetorno;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarDetalleCargoCirugia: "+e);
			return null;
		}
	}
	
	/**
	 * Metodo que obtiene los numeros de carnet de un paciente
	 * @param con
	 * @param convenio
	 * @param ingreso
	 * @return
	 */
	public static String obtenerCarnet(Connection con, int convenio, int ingreso)
	{
		try
		{
			String carnet = "";
			PreparedStatementDecorator total= new PreparedStatementDecorator(con.prepareStatement(cargarCarnet,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			total.setInt(1,convenio);
			total.setInt(2,ingreso);
			ResultSetDecorator rs = new ResultSetDecorator(total.executeQuery());
			if(rs.next())
				carnet = rs.getString("carnet");
			
			return carnet;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando los numeros de Carnet de un paciente "+e);
		}
		return "";
	}
	
	/**
	 * Metodo que consulta la fecha de Egreso segun Estado Cuenta
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static HashMap<String, Object> fechaEgreso(Connection con, int ingreso)
	{
		HashMap<String, Object> resultado = new HashMap<String, Object>();
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(fechaEgreso, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,ingreso);
			
			resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), false, true);
			
			logger.info("\n\nMAPA FECHA EGRESO DESDE SQL>>>>>>>>>>>>"+resultado+"\n\n");
		}
		catch(SQLException e)
		{
			logger.error("Error en Fecha Egreso segun Estado Cuenta: "+e);
		}
		return resultado;
	}
	
	/**
	 * Metodo para consultar los codigos de Facturas asociados a un cargo
	 * @param con
	 * @param subCuenta
	 * @param solicitud
	 * @param consultaCodigosFacturas 
	 * @return
	 */
	public static String codigosFacturas(Connection con, int subCuenta, int solicitud, String consultaCodigosFacturas)
	{
		try
		{
			String facturas = "";
			PreparedStatementDecorator total= new PreparedStatementDecorator(con.prepareStatement(consultaCodigosFacturas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			total.setInt(1,subCuenta);
			if(solicitud!=0)
				total.setInt(2,solicitud);
			else
				total.setInt(2,0);
			ResultSetDecorator rs = new ResultSetDecorator(total.executeQuery());
			if(rs.next())
				facturas = rs.getString("codf");
			
			return facturas;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando los Codigos de Facturas de un Cargo "+e);
		}
		return "";
	}

	/**
	 * 
	 * @param con
	 * @param idSubCuenta
	 * @return
	 */
	public static String obtenerFechaValidacionEsquemas(Connection con,String idSubCuenta) 
	{
		String cadena="SELECT i.id from sub_cuentas sc inner join ingresos i on(sc.ingreso=i.id) where i.estado='"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' and sc.sub_cuenta="+Utilidades.convertirALong(idSubCuenta);
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				
				return UtilidadFecha.getFechaActual();
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return SqlBaseUtilidadesFacturacionDao.obtenerFechaFacturaResponsable(con,idSubCuenta);
	}
	
	
	/**
	 * Metodo para verificar si una consulta arroja resultados
	 * @param con
	 * @param consulta
	 * @return
	 */
	public static boolean resultadosConsulta(Connection con, String consulta)
	{
		HashMap<String, Object> resultado = new HashMap<String, Object>();
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), false, false);
			
			if(Utilidades.convertirAEntero(resultado.get("numRegistros")+"")>0)
				return true;
		}
		catch(SQLException e)
		{
			logger.error("Error en RESULTADOS CONSULTA: "+e);
		}
		return false;
	}
	
	/**
	 * Metodo que consulta Tipos Monto y Valores Paciente
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public static HashMap tipoMontoValoresPaciente(Connection con, int subCuenta)
	{
		HashMap<String, Object> resultado = new HashMap<String, Object>();
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaTipoMontoValoresPaciente, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,subCuenta);
			
			resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			
			logger.info("\n\nMAPA TIPO MONTO VALORES PACIENTE DESDE SQL>>>>>>>>>>>>"+resultado+"\n\n");
		}
		catch(SQLException e)
		{
			logger.error("Error en Tipo Monto Valores Paciente segun Estado Cuenta: "+e);
		}
		return resultado;
	}
	
	/**
	 * Método que carga el detalle de una solicitud
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap<String, Object> cargarDetalleServicioArticuloSolicitud(Connection con,HashMap campos)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		try
		{
			//*************SE TOMAN LOS PARÁMETROS****************************************
			int numeroSolicitud = Integer.parseInt(campos.get("numeroSolicitud").toString());
			int codigoTipoSolicitud = Integer.parseInt(campos.get("codigoTipoSolicitud").toString());
			int codigoSubCuenta = Integer.parseInt(campos.get("codigoSubCuenta").toString());
			int codigoInstitucion = Integer.parseInt(campos.get("codigoInstitucion").toString());
			int codigoManualServicio = Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(codigoInstitucion),true);
			String codigoManualArticulo = ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(codigoInstitucion);
			// Control Cambio Anexo cuenta 3463
			int tipoConvenio = UtilidadesFacturacion.consultarTipoConvenioArticulo(con, campos.get("convenio").toString());
			// *****************************************************************************
			String consulta = "";
			PreparedStatementDecorator pst = null;

			switch (codigoTipoSolicitud) {
			case ConstantesBD.codigoTipoSolicitudCirugia:
				consulta = "SELECT distinct "
						+ "'(' || getcodigopropservicio2(dc.servicio_cx,"
						+ codigoManualServicio
						+ ") || ') ' || getnombreservicio(dc.servicio_cx,"
						+ codigoManualServicio
						+ ") as nombre  "
						+ "from det_cargos dc "
						+ "WHERE dc.solicitud = ? and dc.articulo is null and dc.sub_cuenta = ? and dc.eliminado = '"
						+ ConstantesBD.acronimoNo + "' and dc.paquetizado = '"
						+ ConstantesBD.acronimoNo + "'";
				pst = new PreparedStatementDecorator(con.prepareStatement(
						consulta, ConstantesBD.typeResultSet,
						ConstantesBD.concurrencyResultSet));
				pst.setInt(1, numeroSolicitud);
				pst.setInt(2, codigoSubCuenta);
				resultados = UtilidadBD.cargarValueObject(
						new ResultSetDecorator(pst.executeQuery()), true, true);

				// Si no se encuentra resultados se consulta de la tabla
				// sol_cirugia_por_servicio
				if (Integer.parseInt(resultados.get("numRegistros").toString()) <= 0) {
					consulta = "SELECT "
							+ "'(' || getcodigopropservicio2(sc.servicio,"
							+ codigoManualServicio
							+ ") || ') ' || getnombreservicio(sc.servicio,"
							+ codigoManualServicio
							+ ") as nombre  "
							+ "from sol_cirugia_por_servicio sc "
							+ "WHERE sc.numero_solicitud = ? order by sc.consecutivo";
					pst = new PreparedStatementDecorator(con.prepareStatement(
							consulta, ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
					pst.setInt(1, numeroSolicitud);
					resultados = UtilidadBD.cargarValueObject(
							new ResultSetDecorator(pst.executeQuery()), true,
							true);
				}
				break;
			case ConstantesBD.codigoTipoSolicitudMedicamentos:
			case ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos:
				consulta = "SELECT distinct  ";
				/**
				 * Control de cambio Anexo de la cuenta (3463) 
				 * desarrollador: leoquico
				 * fecha: 23-Abril-2013
				 * */
				if (tipoConvenio >= 0) {
					if (tipoConvenio == 0) {
						consulta += "FACTURACION.getcodigocumarticulo(a.codigo) ";
					}
					if (tipoConvenio == 1) {
						consulta += "a.codigo ";
					}
				} else if (codigoManualArticulo.equals(ConstantesIntegridadDominio.acronimoInterfaz)) {
					consulta += "FACTURACION.getcodigointerfaz(a.codigo) ";
				} else {
					consulta += "a.codigo ";
				}
				consulta += "|| ' - ' || a.descripcion as nombre ";
				// (codigoManualArticulo.equals(ConstantesIntegridadDominio.acronimoInterfaz)?"a.codigo_interfaz":"a.codigo")+" || ' - ' || a.descripcion as nombre "
				// +
				consulta += "from det_cargos dc "
						+ "inner join articulo a on(a.codigo = dc.articulo) "
						+ "WHERE dc.solicitud = ? and dc.sub_cuenta = ? and dc.eliminado = '"
						+ ConstantesBD.acronimoNo + "' and dc.paquetizado = '"
						+ ConstantesBD.acronimoNo + "'";
				pst = new PreparedStatementDecorator(con.prepareStatement(
						consulta, ConstantesBD.typeResultSet,
						ConstantesBD.concurrencyResultSet));
				pst.setInt(1, numeroSolicitud);
				pst.setInt(2, codigoSubCuenta);
				resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
				break;
			default:
				consulta = "SELECT distinct " +
				"'(' || getcodigopropservicio2(dc.servicio,"+codigoManualServicio+") || ') ' || getnombreservicio(dc.servicio,"+codigoManualServicio+") as nombre  " +
				"from det_cargos dc " +
				"WHERE dc.solicitud = ? and dc.sub_cuenta = ? and dc.eliminado = '"+ConstantesBD.acronimoNo+"' and dc.paquetizado = '"+ConstantesBD.acronimoNo+"'";
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setInt(1,numeroSolicitud);
				pst.setInt(2,codigoSubCuenta);
				resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			break;
				
			}
			
			
	}
		catch(SQLException e)
		{
			logger.error("Error en cargarDetalleServicioArticuloSolicitud: "+e);
		}
		return resultados;
	}
	
	/**
	 * M&eacute;todo que consulta la informaciòn del detalle de la solicitud que se muestra en el PopUp
	 * @author Diana Ruiz
	 * @param con
	 * @param campos
	 * @return
	 */

	public static HashMap<String, Object> cargarDetalleServicioArticuloSolicitudPopUp(
			Connection con, HashMap campos) {
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		try {
			// *************SE TOMAN LOS
			// PARÁMETROS****************************************
			int numeroSolicitud = Integer.parseInt(campos
					.get("numeroSolicitud").toString());
			int codigoTipoSolicitud = Integer.parseInt(campos.get(
					"codigoTipoSolicitud").toString());
			int codigoSubCuenta = Integer.parseInt(campos
					.get("codigoSubCuenta").toString());
			int codigoInstitucion = Integer.parseInt(campos.get(
					"codigoInstitucion").toString());
			int codigoManualServicio = Utilidades
					.convertirAEntero(
							ValoresPorDefecto
									.getCodigoManualEstandarBusquedaServicios(codigoInstitucion),
							true);
			String codigoManualArticulo = ValoresPorDefecto
					.getCodigoManualEstandarBusquedaArticulos(codigoInstitucion);
			// Control Cambio Anexo cuenta 3463
			int tipoConvenio = UtilidadesFacturacion
					.consultarTipoConvenioArticulo(con, campos.get("convenio")
							.toString());
			// *****************************************************************************
			String consulta = "";
			PreparedStatementDecorator pst = null;

			switch (codigoTipoSolicitud) {
			case ConstantesBD.codigoTipoSolicitudCirugia:
				consulta = "SELECT distinct "
						+ "'(' || getcodigopropservicio2(dc.servicio_cx,"
						+ codigoManualServicio
						+ ") || ') ' || getnombreservicio(dc.servicio_cx,"
						+ codigoManualServicio
						+ ") as nombre  "
						+ "from det_cargos dc "
						+ "WHERE dc.solicitud = ? and dc.articulo is null and dc.sub_cuenta = ? and dc.eliminado = '"
						+ ConstantesBD.acronimoNo + "' and dc.paquetizado = '"
						+ ConstantesBD.acronimoNo + "'";
				pst = new PreparedStatementDecorator(con.prepareStatement(
						consulta, ConstantesBD.typeResultSet,
						ConstantesBD.concurrencyResultSet));
				pst.setInt(1, numeroSolicitud);
				pst.setInt(2, codigoSubCuenta);
				resultados = UtilidadBD.cargarValueObject(
						new ResultSetDecorator(pst.executeQuery()), true, true);

				// Si no se encuentra resultados se consulta de la tabla
				// sol_cirugia_por_servicio
				if (Integer.parseInt(resultados.get("numRegistros").toString()) <= 0) {
					consulta = "SELECT "
							+ "'(' || getcodigopropservicio2(sc.servicio,"
							+ codigoManualServicio
							+ ") || ') ' || getnombreservicio(sc.servicio,"
							+ codigoManualServicio
							+ ") as nombre  "
							+ "from sol_cirugia_por_servicio sc "
							+ "WHERE sc.numero_solicitud = ? order by sc.consecutivo";
					pst = new PreparedStatementDecorator(con.prepareStatement(
							consulta, ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
					pst.setInt(1, numeroSolicitud);
					resultados = UtilidadBD.cargarValueObject(
							new ResultSetDecorator(pst.executeQuery()), true,
							true);
				}
				break;
			case ConstantesBD.codigoTipoSolicitudMedicamentos:
			case ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos:
				/**
				 * Control de cambio Anexo de la cuenta (3463) desarrollador:
				 * leoquico fecha: 23-Abril-2013
				 * */
				if (tipoConvenio > 0) {
					if (tipoConvenio == 0) {
						consulta += "FACTURACION.getcodigocumarticulo(a.codigo) ";
					}
					if (tipoConvenio == 1) {
						consulta += "a.codigo ";
					}
				} else if (codigoManualArticulo.equals(ConstantesIntegridadDominio.acronimoInterfaz)) {
					consulta += "FACTURACION.getcodigointerfaz(a.codigo) ";
				} else {
					consulta += "a.codigo ";
				}

				consulta = "SELECT distinct a.descripcion as nombre, dd.articulo, dd.cantidad, dd.art_principal, dd.cantidad "
						+ "from det_cargos dc inner join despacho d on (dc.solicitud = d.numero_solicitud) "
						+ "inner join detalle_despachos dd on (dd.despacho = d.orden) "
						+ "inner join articulo a on (a.codigo = dd.articulo) "
						+ "WHERE dc.solicitud = ? and dc.sub_cuenta = ? and dc.eliminado = '"
						+ ConstantesBD.acronimoNo
						+ "' and dc.paquetizado = '"
						+ ConstantesBD.acronimoNo
						+ "' "
						+ "and dd.cantidad > 0";
				pst = new PreparedStatementDecorator(con.prepareStatement(
						consulta, ConstantesBD.typeResultSet,
						ConstantesBD.concurrencyResultSet));
				pst.setInt(1, numeroSolicitud);
				pst.setInt(2, codigoSubCuenta);
				resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
				break;
			default:
				consulta = "SELECT distinct a.descripcion as nombre, dd.articulo, dd.cantidad, dd.art_principal, dd.cantidad "
						+ "from det_cargos dc inner join despacho d on (dc.solicitud = d.numero_solicitud) "
						+ "inner join detalle_despachos dd on (dd.despacho = d.orden) "
						+ "inner join articulo a on (a.codigo = dd.articulo) "
						+ "WHERE dc.solicitud = ? and dc.sub_cuenta = ? and dc.eliminado = '"
						+ ConstantesBD.acronimoNo
						+ "' and dc.paquetizado = '"
						+ ConstantesBD.acronimoNo
						+ "' "
						+ "and dd.cantidad > 0";
				pst = new PreparedStatementDecorator(con.prepareStatement(
						consulta, ConstantesBD.typeResultSet,
						ConstantesBD.concurrencyResultSet));
				pst.setInt(1, numeroSolicitud);
				pst.setInt(2, codigoSubCuenta);
				resultados = UtilidadBD.cargarValueObject(
						new ResultSetDecorator(pst.executeQuery()), true, true);
				break;
			}

			// logger.info("********AQUIIIIIIIIIIIII********/////////////////*******************------------------"+consulta+"******---------nro solicitud--...................."+numeroSolicitud+"**************-------------codigo subcuenta...................."+codigoSubCuenta);

		} catch (SQLException e) {
			logger.error("Error en cargarDetalleServicioArticuloSolicitudPopUP: "
					+ e);
		}
		return resultados;
	}

	/**
	 * 
	 * M&eacute;todo que consulta la informaciòn del detalle de la solicitud
	 * @param con
	 * @param campos
	 * @return
	 * @author Diana Ruiz
	 * 
	 */

	public static boolean despachoEquivalentes(Connection con, HashMap campos)
    {
        HashMap<String, Object> equivalente = new HashMap<String, Object>();
        try
        {
            //*************SE TOMAN LOS PARÁMETROS****************************************
            int numeroSolicitud = Integer.parseInt(campos.get("numeroSolicitud").toString());
            int codigoTipoSolicitud = Integer.parseInt(campos.get("codigoTipoSolicitud").toString());
            int codigoSubCuenta = Integer.parseInt(campos.get("codigoSubCuenta").toString());
           // int codigoInstitucion = Integer.parseInt(campos.get("codigoInstitucion").toString());    
            //*****************************************************************************
            String consultaEquivalente = "";
            PreparedStatementDecorator pst = null;
           
            switch(codigoTipoSolicitud)
            {           
                case ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos:
                    consultaEquivalente = "SELECT \'S\' as Equivalente " +
                        "from detalle_despachos dd inner join despacho d on (dd.despacho = d.orden) " +
                        "WHERE d.numero_solicitud = ? and dd.cantidad > 0 and dd.articulo <> dd.art_principal";               
                    pst =  new PreparedStatementDecorator(con.prepareStatement(consultaEquivalente, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
                    pst.setInt(1,numeroSolicitud);
                    //pst.setInt(2,codigoSubCuenta);
                    equivalente = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
                break;
                default:
                    consultaEquivalente = "SELECT \'S\' as Equivalente " +
                    "from detalle_despachos dd inner join despacho d on (dd.despacho = d.orden) " +
                    "WHERE d.numero_solicitud = ? and dd.cantidad > 0 and dd.articulo <> dd.art_principal";
                    pst =  new PreparedStatementDecorator(con.prepareStatement(consultaEquivalente, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
                    pst.setInt(1,numeroSolicitud);                   
                    equivalente = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
                break;
            }           
            logger.info("********AQUIIIIIIIIIIIII********/////////////////*******************------------------"+equivalente);
        }
       
        catch(SQLException e)
        {
            logger.error("Error en cargarDetalleServicioArticuloSolicitudPopUP: "+e);
        }
       
        if(equivalente.size() > 0){
            if(equivalente.get("equivalente_0") != null){
                if(equivalente.get("equivalente_0").equals("S")){
                    return true;
                }else{
                    return false;
                }
            }
        }
        return false;
    }
	
}