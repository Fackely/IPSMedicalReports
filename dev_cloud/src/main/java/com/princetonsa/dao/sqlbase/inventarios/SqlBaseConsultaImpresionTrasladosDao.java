
/*
 * Creado   25/01/2006
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_06
 * author Joan Lopez
 */
package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
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

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * 
 * Clase para manejar
 *
 * @version 1.0, 25/01/2006
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class SqlBaseConsultaImpresionTrasladosDao 
{
    /**
     * manejador de los logs de la clase
     */
    private static Logger logger=Logger.getLogger(SqlBaseConsultaImpresionTrasladosDao.class);
    /**
     * query para realizar la busqueda avanzada de traslados almacen
     */
    private static final String consultaListadoTrasladoAlmacenStr0="SELECT " +
    																		"numero_traslado," +
    																		"cod_almacen_despacha," +
    																		"nom_almacen_despacha," +
    																		"cod_almacen_solicita," +
    																		"nom_almacen_solicita," +
    																		"cod_estado," +
    																		"nom_estado," +
    																		"prioritario," +
    																		"fecha_solicitud," +
    																		"hora_elaboracion," +
    																		"usuario_solicita," +
    																		"fecha_despacho," +
    																		"hora_despacho," +
    																		"usuario_despacho," +
    																		"observaciones," +
    																		"centro_atencion_solicita," +
    																		"centro_atencion_solicitado " +
    																" FROM (";
    
    private static final String consultaListadoTrasladosAlmacenStr1=" SELECT " +
    																		" st.numero_traslado as numero_traslado," +
    																		"st.almacen_solicitado as cod_almacen_despacha," +
    																		"getnomcentrocosto(st.almacen_solicitado) as nom_almacen_despacha," +
    																		"st.almacen_solicita as cod_almacen_solicita," +
    																		"getnomcentrocosto(st.almacen_solicita) as nom_almacen_solicita," +
    																		"st.estado as cod_estado," +
    																		"et.descripcion as nom_estado," +
    																		"st.prioritario as prioritario," +
    																		"to_char(st.fecha_elaboracion, 'YYYY-MM-DD') as fecha_solicitud," +
    																		"st.hora_elaboracion as hora_elaboracion," +
    																		"st.usuario_elabora as usuario_solicita," +
    																		"to_char(dta.fecha_despacho,'yyyy-mm-dd') as fecha_despacho," +
    																		"dta.hora_despacho as hora_despacho," +
    																		"CASE WHEN dta.usuario_despacho IS NULL THEN '' ELSE dta.usuario_despacho END as usuario_despacho," +
    																		"CASE WHEN st.observaciones IS NULL THEN '' ELSE st.observaciones END as observaciones," +
    																		"getnomcentroatencion(cc.centro_atencion) as centro_atencion_solicita," +
    																		"getnomcentroatencion(cc1.centro_atencion) as centro_atencion_solicitado " +
    																" FROM solicitud_traslado_almacen st" +
    																" inner join estados_traslado_almacen et on (et.codigo=st.estado)" +
    																" inner join centros_costo cc on (cc.codigo=st.almacen_solicita)" +
    																" inner join centros_costo cc1 on (cc1.codigo=st.almacen_solicitado)" +
    																" inner join det_sol_traslado_almacen  dst on (dst.numero_traslado=st.numero_traslado)" +
    																" left join despacho_traslado_almacen dta on (dta.numero_traslado=st.numero_traslado)" +
    																" WHERE cc.institucion=?  and st.estado<>"+ConstantesBD.codigoEstadoTrasladoDespachada;
                                                                    
    private static final String consultaListadoTrasladosAlmacenStr2="  SELECT " +
    																			" st.numero_traslado as numero_traslado," +
    																			" st.almacen_solicitado as cod_almacen_despacha," +
    																			" getnomcentrocosto(st.almacen_solicitado) as nom_almacen_despacha," +
    																			" st.almacen_solicita as cod_almacen_solicita," +
    																			" getnomcentrocosto(st.almacen_solicita) as nom_almacen_solicita," +
    																			" st.estado as cod_estado," +
    																			" et.descripcion as nom_estado," +
    																			" st.prioritario as prioritario," +
    																			" to_char(st.fecha_elaboracion, 'YYYY-MM-DD') as fecha_solicitud," +
    																			" st.hora_elaboracion as hora_elaboracion," +
    																			" st.usuario_elabora as usuario_solicita," +
    																			" to_char(dta.fecha_despacho,'yyyy-mm-dd') as fecha_despacho," +
    																			" dta.hora_despacho as hora_despacho," +
    																			" CASE WHEN dta.usuario_despacho IS NULL THEN '' ELSE dta.usuario_despacho END as usuario_despacho," +
    																			" CASE WHEN st.observaciones IS NULL THEN '' ELSE st.observaciones END as observaciones," +
    																			" getnomcentroatencion(cc.centro_atencion) as centro_atencion_solicita," +
    																			" getnomcentroatencion(cc1.centro_atencion) as centro_atencion_solicitado " +
    																	" FROM solicitud_traslado_almacen st" +
    																	" inner join estados_traslado_almacen et on (et.codigo=st.estado)" +
    																	" inner join centros_costo cc on (cc.codigo=st.almacen_solicita)" +
    																	" inner join centros_costo cc1 on (cc1.codigo=st.almacen_solicitado)" +
    																	" left outer join det_des_traslado_almacen ddta on (ddta.numero_traslado=st.numero_traslado)" +
    																	" left join despacho_traslado_almacen dta on (dta.numero_traslado=st.numero_traslado)" +
    																	" WHERE cc.institucion=?  and  st.estado="+ConstantesBD.codigoEstadoTrasladoDespachada;
    	
    private static final String consultaListadoTrasladosAlmacenStr3=" ) s order by numero_traslado asc ";  
    
    /**
     * query para consultar el detalle de los articulos de la solicitud
     */
    private static final String consultaDetalleSolicitudStr="SELECT " +                                                         
                                                            "dts.articulo as articulo," +
                                                            " coalesce(va.codigo_interfaz,'') as codigointerfaz,"+
                                                            "dts.cantidad as cantidadsolicitada," +
                                                            "getdescarticulosincodigo(va.codigo) as descripcion," +
                                                            "getNomUnidadMedida(va.unidad_medida) AS unidadmedida," +
                                                            "CASE WHEN ddta.valor_unitario IS NULL THEN 0 ELSE ddta.valor_unitario end as valor_unitario," +                                                            
                                                            "CASE WHEN ddta.cantidad IS NULL THEN 0 ELSE ddta.cantidad END as cantidaddespachada," +                                                            
                                                            "((CASE WHEN ddta.cantidad IS NULL THEN 0 ELSE ddta.cantidad end) * (CASE WHEN ddta.valor_unitario IS NULL THEN 0 ELSE ddta.valor_unitario end)) as valor_total," +
												    		"CASE WHEN ddta.lote is null then '' else ddta.lote end as lote," +
												    		"CASE WHEN ddta.fecha_vencimiento is null then '' else to_char(ddta.fecha_vencimiento,'dd/mm/yyyy') end as fechavencimiento, " +
												    		"getUbicacionArticulo(va.codigo,st.almacen_solicitado) As ubicacion_alm_des, " +
												    		"getUbicacionArticulo(va.codigo,st.almacen_solicita) As ubicacion_alm_sol " + 
                                                         "from det_sol_traslado_almacen dts " +
                                                         "inner join solicitud_traslado_almacen st on (st.numero_traslado=dts.numero_traslado)" +
                                                         "inner join articulo va on(dts.articulo=va.codigo) " +
                                                         "left join det_des_traslado_almacen ddta on(ddta.articulo=dts.articulo and ddta.numero_traslado=?) " +
                                                         "where dts.numero_traslado=? order by va.descripcion";
    
    /***********************************************************************************************************
     * Modificacion por anexo 632 
     ***********************************************************************************************************/
    
    private static final String consultaDetalladaXAlmacen0="SELECT " +
    		                                                       " \"fecha_solicitud\", " +
    		                                                       " \"nom_almacen_despacha\", " +												
    															   " \"codigo_articulo\", " +
    															   " \"descripcion\", " +
    															   " \"fecha_despacho\", " +
    															   " \"unidad_medida\", " +
    															   " \"cantidadsolicitada\"," +
    															   " \"cantidaddespachada\", " +
    															   " \"diferencia\" " +
    														"FROM ( ";                                           
    
    private static final String consultaDetalladaXAlmacen1=" SELECT " +
    		                                                   		" to_char(st.fecha_elaboracion,'dd/mm/yyyy') as \"fecha_solicitud\"," +
    															    " getnomcentrocosto(st.almacen_solicitado) as \"nom_almacen_despacha\"," +
    																" getCodArticuloAxiomaInterfaz(dst.articulo, @@ ) As \"codigo_articulo\"," +
    																" coalesce(getdescripcionarticulo(dst.articulo),'') ||' CONC:'|| coalesce(getconcentracionarticulo(dst.articulo),'') ||' F.F:'|| coalesce(getformafarmaceuticaarticulo(dst.articulo),'')  || ' NAT:' || coalesce(getnaturalezaarticulo(dst.articulo),'') || CASE WHEN getespos(dst.articulo)='1'  THEN ' - POS' WHEN getespos(dst.articulo)='0' THEN ' - NOPOS' ELSE '' END AS \"descripcion\"," +
    																" to_char(dta.fecha_despacho,'dd/mm/yyyy')  as \"fecha_despacho\"," +
    																" getunidadmedidaarticulo(dst.articulo) As \"unidad_medida\"," +
    																" dst.cantidad as \"cantidadsolicitada\"," +
    																" 0 as \"cantidaddespachada\"," +
    																" dst.cantidad As \"diferencia\" " +
    														" FROM solicitud_traslado_almacen st" +
    														" INNER JOIN det_sol_traslado_almacen  dst on (dst.numero_traslado=st.numero_traslado)" +
    														" LEFT OUTER JOIN despacho_traslado_almacen dta on (dta.numero_traslado=st.numero_traslado)" +
    														" INNER JOIN centros_costo cc on (cc.codigo=st.almacen_solicita)" +
    														/*INICIO MODIFICACION mt4086 Alberto Ovalle*/
    														/*" WHERE cc.institucion=?  AND st.prioritario="+ValoresPorDefecto.getValorFalseParaConsultas()+*/
    														" WHERE cc.institucion=?  AND st.prioritario in(0,1)" +
    														/*FIN MODIFIACION Alberto Ovalle*/
    														" AND st.estado<>"+ConstantesBD.codigoEstadoTrasladoDespachada;
   
    private static final String consultaDetalladaXAlmacen2=" SELECT " +
    																" to_char(st.fecha_elaboracion,'dd/mm/yyyy') as \"fecha_solicitud\"," +
    																" getnomcentrocosto(st.almacen_solicitado) as \"nom_almacen_despacha\"," +
    																" getCodArticuloAxiomaInterfaz(ddta.articulo, @@  ) As \"codigo_articulo\"," +
    																" coalesce(getdescripcionarticulo(ddta.articulo),'') ||' CONC:'|| coalesce(getconcentracionarticulo(ddta.articulo),'') ||' F.F:'|| coalesce(getformafarmaceuticaarticulo(ddta.articulo),'')  || ' NAT:' || coalesce(getnaturalezaarticulo(ddta.articulo),'') || CASE WHEN getespos(ddta.articulo)='1'  THEN ' - POS' WHEN getespos(ddta.articulo)='0' THEN ' - NOPOS' ELSE '' END AS \"descripcion\"," +
    																" to_char(dta.fecha_despacho,'dd/mm/yyyy')  as \"fecha_despacho\"," +
    																" getunidadmedidaarticulo(ddta.articulo) As \"unidad_medida\"," +
    																" (select dst.cantidad from det_sol_traslado_almacen dst where dst.numero_traslado=ddta.numero_traslado and dst.articulo=ddta.articulo ) as \"cantidadsolicitada\"," +
    																" CASE WHEN ddta.cantidad IS NULL THEN 0 ELSE ddta.cantidad END as \"cantidaddespachada\"," +
    																" (select dst.cantidad from det_sol_traslado_almacen dst where dst.numero_traslado=ddta.numero_traslado and dst.articulo=ddta.articulo )- (CASE WHEN ddta.cantidad IS NULL THEN 0 ELSE ddta.cantidad END) as \"diferencia\" " +
    														" FROM solicitud_traslado_almacen st " +
    														" INNER JOIN det_des_traslado_almacen ddta on (ddta.numero_traslado=st.numero_traslado) " +
    														" INNER JOIN despacho_traslado_almacen dta on (dta.numero_traslado=st.numero_traslado) " +
    														" INNER JOIN centros_costo cc on (cc.codigo=st.almacen_solicita) " +
    														/*INICIO MODIFICACION mt4086 Alberto Ovalle*/
    														/*" WHERE cc.institucion=?  AND st.prioritario="+ValoresPorDefecto.getValorFalseParaConsultas() +*/
    														" WHERE cc.institucion=?  AND st.prioritario in(0,1)" +
    														/*FIN MODIFIACION Alberto Ovalle*/
    														" AND st.estado="+ConstantesBD.codigoEstadoTrasladoDespachada;
    
    private static final String consultaDetalladaXAlmacen3=" ) s order by \"nom_almacen_despacha\" asc " ;
    
    
    
    private static final String consultaDetalladaXTransaccion0="SELECT " +
    																   " \"codigo_almacen_despacha\"," +
    																   " \"nom_almacen_despacha\"," +
    																   " \"numero_traslado\"," +
    																   " \"codigo_almacen_solicita\"," +
    																   " \"nom_almacen_solicita\"," +
    																   " \"fecha_solicitud\", " +
    																   " \"hora_elaboracion\", " +
    																   " \"cantidadsolicitada\", " +
    																   " \"fecha_despacho\", " +
    																   " \"unidad_medida\", " +
    																   " \"cantidaddespachada\", " +
    																   " \"hora_despacho\" " +
    															" FROM ( " ;
    
    
    private static final String consultaDetalladaXTransaccion1="SELECT " +
    																   " st.almacen_solicitado As \"codigo_almacen_despacha\"," +
    																   " getnomcentrocosto(st.almacen_solicitado) as \"nom_almacen_despacha\"," +
    																   " st.numero_traslado as \"numero_traslado\"," +
    																   " st.almacen_solicita As \"codigo_almacen_solicita\"," +
    																   " getnomcentrocosto(st.almacen_solicita) as \"nom_almacen_solicita\"," +
    																   " to_char(st.fecha_elaboracion,'dd/mm/yyyy') as \"fecha_solicitud\"," +
    																   " st.hora_elaboracion As \"hora_elaboracion\"," +
    																   " dst.cantidad as \"cantidadsolicitada\"," +
    																   " to_char(dta.fecha_despacho,'dd/mm/yyyy')  as \"fecha_despacho\"," +
    																   " dta.hora_despacho As \"hora_despacho\"," +
    																   " getunidadmedidaarticulo(dst.articulo) As \"unidad_medida\"," +
    																   " 0 as \"cantidaddespachada\"," +
    																   " dst.articulo As articulo " +
    															" FROM solicitud_traslado_almacen st" +
    															" INNER JOIN det_sol_traslado_almacen  dst on (dst.numero_traslado=st.numero_traslado)" +
    															" LEFT OUTER JOIN  despacho_traslado_almacen dta on (dta.numero_traslado=st.numero_traslado)" +
    															" INNER JOIN centros_costo cc on (cc.codigo=st.almacen_solicita)" +
    															" INNER JOIN centros_costo cc1 on (cc1.codigo=st.almacen_solicitado)" +
    															" WHERE cc.institucion=?  " +
    															/*INICIO MODIFICACION mt4086 Alberto Ovalle*/
    															/*" and st.prioritario="+ValoresPorDefecto.getValorFalseParaConsultas()+*/
    															" and st.prioritario in(0,1) " +
    															/*FIN MODIFIACION Alberto Ovalle*/
    															" and st.estado<>"+ConstantesBD.codigoEstadoTrasladoDespachada;
    
    private static final String consultaDetalladaXTransaccion2="SELECT " +
    																   " st.almacen_solicitado As \"codigo_almacen_despacha\"," +
    																   " getnomcentrocosto(st.almacen_solicitado) as \"nom_almacen_despacha\"," +
    																   " st.numero_traslado as \"numero_traslado\"," +
    																   " st.almacen_solicita As \"codigo_almacen_solicita\"," +
    																   " getnomcentrocosto(st.almacen_solicita) as \"nom_almacen_solicita\"," +
    																   " to_char(st.fecha_elaboracion,'dd/mm/yyyy') as \"fecha_solicitud\"," +
    																   " st.hora_elaboracion As \"hora_solicitud\"," +
    																   " (select dst.cantidad from det_sol_traslado_almacen dst where dst.numero_traslado=ddta.numero_traslado and dst.articulo=ddta.articulo ) as \"cantidadsolicitada\"," +
    																   " to_char(dta.fecha_despacho,'dd/mm/yyyy')  as \"fecha_despacho\"," +
    																   " dta.hora_despacho As \"hora_despacho\"," +
    																   " getunidadmedidaarticulo(ddta.articulo) As \"unidad_medida\"," +
    																   " CASE WHEN ddta.cantidad IS NULL THEN 0 ELSE ddta.cantidad END as \"cantidaddespachada\"," +
    																   " ddta.articulo As articulo " +
    															" FROM solicitud_traslado_almacen st" +
    															" INNER JOIN det_des_traslado_almacen ddta on (ddta.numero_traslado=st.numero_traslado)" +
    															" inner JOIN despacho_traslado_almacen dta on (dta.numero_traslado=st.numero_traslado)" +
    															" INNER JOIN centros_costo cc on (cc.codigo=st.almacen_solicita) " +
    															" WHERE cc.institucion=? " +
    															/*INICIO MODIFICACION mt4086 Alberto Ovalle*/
    															/*" and st.prioritario="+ValoresPorDefecto.getValorFalseParaConsultas()+*/
    															" and st.prioritario in(0,1)" +
    															/*FIN MODIFIACION Alberto Ovalle*/
    															" AND st.estado="+ConstantesBD.codigoEstadoTrasladoDespachada;
    
    private static final String consultaDetalladaXTransaccion3=" ) s order by \"numero_traslado\", \"nom_almacen_despacha\" asc ";
    
    
    private static final String consultaDetalladaXClaseInventario0=" SELECT " +
    																		" \"codigo_almacen_despacha\", \"nom_almacen_despacha\"," +
    																		" \"numero_traslado\",\"codigo_almacen_solicita\"," +
    																		" \"nom_almacen_solicita\", \"codigo_clase\", \"nombre_clase\"," +
    																		" \"unidad_medida\",\"cantidaddespachada\", \"valor_unitario\"," +
    																		" \"valor_total\" " +
    																" FROM (";
    
    private static final String consultaDetalladaXClaseInventario1=" SELECT " +
    																		" st.almacen_solicitado As \"codigo_almacen_despacha\"," +
    																		" getnomcentrocosto(st.almacen_solicitado) as \"nom_almacen_despacha\"," +
    																		" st.numero_traslado as \"numero_traslado\"," +
    																		" st.almacen_solicita As \"codigo_almacen_solicita\"," +
    																		" getnomcentrocosto(st.almacen_solicita) as \"nom_almacen_solicita\"," +
    																		" getclasearticulo(dst.articulo) As \"codigo_clase\"," +
    																		" getnombreclaseinventario(getclasearticulo(dst.articulo)) As \"nombre_clase\"," +
    																		" getunidadmedidaarticulo(dst.articulo) As \"unidad_medida\"," +
    																		" 0 as \"cantidaddespachada\"," +
    																		" CASE WHEN ddta.valor_unitario IS NULL THEN 0 ELSE ddta.valor_unitario end as \"valor_unitario\"," +
    																		" ((CASE WHEN ddta.cantidad IS NULL THEN 0 ELSE ddta.cantidad end) * (CASE WHEN ddta.valor_unitario IS NULL THEN 0 ELSE ddta.valor_unitario end)) as \"valor_total\", " +
    																		" dst.articulo As articulo " +
    																" FROM solicitud_traslado_almacen st" +
    																" INNER JOIN det_des_traslado_almacen ddta on (ddta.numero_traslado=st.numero_traslado)" +
    																" INNER JOIN det_sol_traslado_almacen  dst on (dst.numero_traslado=st.numero_traslado)" +
    																" LEFT OUTER JOIN despacho_traslado_almacen dta on (dta.numero_traslado=st.numero_traslado)" +
    																" INNER JOIN centros_costo cc on (cc.codigo=st.almacen_solicita) " +
    																" INNER JOIN centros_costo cc1 on (cc1.codigo=st.almacen_solicitado)" +
    																" WHERE cc.institucion=?  " +
    																/*INICIO MODIFICACION mt4086 Alberto Ovalle*/
        															/*" and st.prioritario="+ValoresPorDefecto.getValorFalseParaConsultas()+*/
    																" and st.prioritario in(0,1)" +
    																/*FIN MODIFIACION Alberto Ovalle*/
    																" and st.estado<>"+ConstantesBD.codigoEstadoTrasladoDespachada;

    
    
    private static final String consultaDetalladaXClaseInventario2=" SELECT " +
    																		" st.almacen_solicitado As \"codigo_almacen_despacha\"," +
    																		" getnomcentrocosto(st.almacen_solicitado) as \"nom_almacen_despacha\"," +
    																		" st.numero_traslado as \"numero_traslado\"," +
    																		" st.almacen_solicita As \"codigo_almacen_solicita\"," +
    																		" getnomcentrocosto(st.almacen_solicita) as \"nom_almacen_solicita\"," +
    																		" getclasearticulo(ddta.articulo) As \"codigo_clase\"," +
    																		" getnombreclaseinventario(getclasearticulo(ddta.articulo)) As \"nombre_clase\"," +
    																		" getunidadmedidaarticulo(ddta.articulo) As \"unidad_medida\"," +
    																		" CASE WHEN ddta.cantidad IS NULL THEN 0 ELSE ddta.cantidad END as \"cantidaddespachada\"," +
    																		" CASE WHEN ddta.valor_unitario IS NULL THEN 0 ELSE ddta.valor_unitario end as \"valor_unitario\"," +
    																		" ((CASE WHEN ddta.cantidad IS NULL THEN 0 ELSE ddta.cantidad end) * (CASE WHEN ddta.valor_unitario IS NULL THEN 0 ELSE ddta.valor_unitario end)) as \"valor_total\", " +
    																		" ddta.articulo As articulo " +
    																" FROM solicitud_traslado_almacen st" +
    																" INNER JOIN det_des_traslado_almacen ddta on (ddta.numero_traslado=st.numero_traslado)" +
    																" INNER JOIN despacho_traslado_almacen dta on (dta.numero_traslado=st.numero_traslado)" +
    																" INNER JOIN centros_costo cc on (cc.codigo=st.almacen_solicita)" +
    																" INNER JOIN centros_costo cc1 on (cc1.codigo=st.almacen_solicitado)" +
    																" WHERE cc.institucion=? " +
    																/*INICIO MODIFICACION mt4086 Alberto Ovalle*/
        															/*" and st.prioritario="+ValoresPorDefecto.getValorFalseParaConsultas()+*/
    																" and st.prioritario in(0,1)" +
    																/*FIN MODIFIACION Alberto Ovalle*/
    																" AND st.estado="+ConstantesBD.codigoEstadoTrasladoDespachada;
    
    
    
    private static final String consultaDetalladaXClaseInventario3=" ) s order by \"nombre_clase\" asc " ;
    
    /***********************************************************************************************************
     * Modificacion por Alberto Ovalle mt4086 Solucion permita resultados independientes
     ***********************************************************************************************************/
    private static final String consultaDetalladaXAlmacenp0="SELECT " +
            " \"fecha_solicitud\", " +
            " \"nom_almacen_despacha\", " +												
			   " \"codigo_articulo\", " +
			   " \"descripcion\", " +
			   " \"fecha_despacho\", " +
			   " \"unidad_medida\", " +
			   " \"cantidadsolicitada\"," +
			   " \"cantidaddespachada\", " +
			   " \"diferencia\" " +
		"FROM ( ";                                           

     private static final String consultaDetalladaXAlmacenp1=" SELECT " +
        		" to_char(st.fecha_elaboracion,'dd/mm/yyyy') as \"fecha_solicitud\"," +
			    " getnomcentrocosto(st.almacen_solicitado) as \"nom_almacen_despacha\"," +
				" getCodArticuloAxiomaInterfaz(dst.articulo, @@ ) As \"codigo_articulo\"," +
				" coalesce(getdescripcionarticulo(dst.articulo),'') ||' CONC:'|| coalesce(getconcentracionarticulo(dst.articulo),'') ||' F.F:'|| coalesce(getformafarmaceuticaarticulo(dst.articulo),'')  || ' NAT:' || coalesce(getnaturalezaarticulo(dst.articulo),'') || CASE WHEN getespos(dst.articulo)='1'  THEN ' - POS' WHEN getespos(dst.articulo)='0' THEN ' - NOPOS' ELSE '' END AS \"descripcion\"," +
				" to_char(dta.fecha_despacho,'dd/mm/yyyy')  as \"fecha_despacho\"," +
				" getunidadmedidaarticulo(dst.articulo) As \"unidad_medida\"," +
				" dst.cantidad as \"cantidadsolicitada\"," +
				" 0 as \"cantidaddespachada\"," +
				" dst.cantidad As \"diferencia\" " +
		" FROM solicitud_traslado_almacen st" +
		" INNER JOIN det_sol_traslado_almacen  dst on (dst.numero_traslado=st.numero_traslado)" +
		" LEFT OUTER JOIN despacho_traslado_almacen dta on (dta.numero_traslado=st.numero_traslado)" +
		" INNER JOIN centros_costo cc on (cc.codigo=st.almacen_solicita)" +
		/*INICIO MODIFICACION mt4086 Alberto Ovalle*/
		/*" WHERE cc.institucion=?  AND st.prioritario="+ValoresPorDefecto.getValorFalseParaConsultas()+*/
		" WHERE cc.institucion=?  AND st.prioritario in(true,false)" +
		/*FIN MODIFIACION Alberto Ovalle*/
		" AND st.estado<>"+ConstantesBD.codigoEstadoTrasladoDespachada;

     private static final String consultaDetalladaXAlmacenp2=" SELECT " +
				" to_char(st.fecha_elaboracion,'dd/mm/yyyy') as \"fecha_solicitud\"," +
				" getnomcentrocosto(st.almacen_solicitado) as \"nom_almacen_despacha\"," +
				" getCodArticuloAxiomaInterfaz(ddta.articulo, @@  ) As \"codigo_articulo\"," +
				" coalesce(getdescripcionarticulo(ddta.articulo),'') ||' CONC:'|| coalesce(getconcentracionarticulo(ddta.articulo),'') ||' F.F:'|| coalesce(getformafarmaceuticaarticulo(ddta.articulo),'')  || ' NAT:' || coalesce(getnaturalezaarticulo(ddta.articulo),'') || CASE WHEN getespos(ddta.articulo)='1'  THEN ' - POS' WHEN getespos(ddta.articulo)='0' THEN ' - NOPOS' ELSE '' END AS \"descripcion\"," +
				" to_char(dta.fecha_despacho,'dd/mm/yyyy')  as \"fecha_despacho\"," +
				" getunidadmedidaarticulo(ddta.articulo) As \"unidad_medida\"," +
				" (select dst.cantidad from det_sol_traslado_almacen dst where dst.numero_traslado=ddta.numero_traslado and dst.articulo=ddta.articulo ) as \"cantidadsolicitada\"," +
				" CASE WHEN ddta.cantidad IS NULL THEN 0 ELSE ddta.cantidad END as \"cantidaddespachada\"," +
				" (select dst.cantidad from det_sol_traslado_almacen dst where dst.numero_traslado=ddta.numero_traslado and dst.articulo=ddta.articulo )- (CASE WHEN ddta.cantidad IS NULL THEN 0 ELSE ddta.cantidad END) as \"diferencia\" " +
		" FROM solicitud_traslado_almacen st " +
		" INNER JOIN det_des_traslado_almacen ddta on (ddta.numero_traslado=st.numero_traslado) " +
		" INNER JOIN despacho_traslado_almacen dta on (dta.numero_traslado=st.numero_traslado) " +
		" INNER JOIN centros_costo cc on (cc.codigo=st.almacen_solicita) " +
		/*INICIO MODIFICACION mt4086 Alberto Ovalle*/
		/*" WHERE cc.institucion=?  AND st.prioritario="+ValoresPorDefecto.getValorFalseParaConsultas() +*/
		" WHERE cc.institucion=?  AND st.prioritario in(true,false)" +
		/*FIN MODIFIACION Alberto Ovalle*/
		" AND st.estado="+ConstantesBD.codigoEstadoTrasladoDespachada;

      private static final String consultaDetalladaXAlmacenp3=" ) s order by \"nom_almacen_despacha\" asc " ;



      private static final String consultaDetalladaXTransaccionp0="SELECT " +
				   " \"codigo_almacen_despacha\"," +
				   " \"nom_almacen_despacha\"," +
				   " \"numero_traslado\"," +
				   " \"codigo_almacen_solicita\"," +
				   " \"nom_almacen_solicita\"," +
				   " \"fecha_solicitud\", " +
				   " \"hora_elaboracion\", " +
				   " \"cantidadsolicitada\", " +
				   " \"fecha_despacho\", " +
				   " \"unidad_medida\", " +
				   " \"cantidaddespachada\", " +
				   " \"hora_despacho\" " +
			" FROM ( " ;


       private static final String consultaDetalladaXTransaccionp1="SELECT " +
				   " st.almacen_solicitado As \"codigo_almacen_despacha\"," +
				   " getnomcentrocosto(st.almacen_solicitado) as \"nom_almacen_despacha\"," +
				   " st.numero_traslado as \"numero_traslado\"," +
				   " st.almacen_solicita As \"codigo_almacen_solicita\"," +
				   " getnomcentrocosto(st.almacen_solicita) as \"nom_almacen_solicita\"," +
				   " to_char(st.fecha_elaboracion,'dd/mm/yyyy') as \"fecha_solicitud\"," +
				   " st.hora_elaboracion As \"hora_elaboracion\"," +
				   " dst.cantidad as \"cantidadsolicitada\"," +
				   " to_char(dta.fecha_despacho,'dd/mm/yyyy')  as \"fecha_despacho\"," +
				   " dta.hora_despacho As \"hora_despacho\"," +
				   " getunidadmedidaarticulo(dst.articulo) As \"unidad_medida\"," +
				   " 0 as \"cantidaddespachada\"," +
				   " dst.articulo As articulo " +
			" FROM solicitud_traslado_almacen st" +
			" INNER JOIN det_sol_traslado_almacen  dst on (dst.numero_traslado=st.numero_traslado)" +
			" LEFT OUTER JOIN  despacho_traslado_almacen dta on (dta.numero_traslado=st.numero_traslado)" +
			" INNER JOIN centros_costo cc on (cc.codigo=st.almacen_solicita)" +
			" INNER JOIN centros_costo cc1 on (cc1.codigo=st.almacen_solicitado)" +
			" WHERE cc.institucion=?  " +
			/*INICIO MODIFICACION mt4086 Alberto Ovalle*/
			/*" and st.prioritario="+ValoresPorDefecto.getValorFalseParaConsultas()+*/
			" and st.prioritario in(true,false) " +
			/*FIN MODIFIACION Alberto Ovalle*/
			" and st.estado<>"+ConstantesBD.codigoEstadoTrasladoDespachada;

       private static final String consultaDetalladaXTransaccionp2="SELECT " +
				   " st.almacen_solicitado As \"codigo_almacen_despacha\"," +
				   " getnomcentrocosto(st.almacen_solicitado) as \"nom_almacen_despacha\"," +
				   " st.numero_traslado as \"numero_traslado\"," +
				   " st.almacen_solicita As \"codigo_almacen_solicita\"," +
				   " getnomcentrocosto(st.almacen_solicita) as \"nom_almacen_solicita\"," +
				   " to_char(st.fecha_elaboracion,'dd/mm/yyyy') as \"fecha_solicitud\"," +
				   " st.hora_elaboracion As \"hora_solicitud\"," +
				   " (select dst.cantidad from det_sol_traslado_almacen dst where dst.numero_traslado=ddta.numero_traslado and dst.articulo=ddta.articulo ) as \"cantidadsolicitada\"," +
				   " to_char(dta.fecha_despacho,'dd/mm/yyyy')  as \"fecha_despacho\"," +
				   " dta.hora_despacho As \"hora_despacho\"," +
				   " getunidadmedidaarticulo(ddta.articulo) As \"unidad_medida\"," +
				   " CASE WHEN ddta.cantidad IS NULL THEN 0 ELSE ddta.cantidad END as \"cantidaddespachada\"," +
				   " ddta.articulo As articulo " +
			" FROM solicitud_traslado_almacen st" +
			" INNER JOIN det_des_traslado_almacen ddta on (ddta.numero_traslado=st.numero_traslado)" +
			" inner JOIN despacho_traslado_almacen dta on (dta.numero_traslado=st.numero_traslado)" +
			" INNER JOIN centros_costo cc on (cc.codigo=st.almacen_solicita) " +
			" WHERE cc.institucion=? " +
			/*INICIO MODIFICACION mt4086 Alberto Ovalle*/
			/*" and st.prioritario="+ValoresPorDefecto.getValorFalseParaConsultas()+*/
			" and st.prioritario in(true,false)" +
			/*FIN MODIFIACION Alberto Ovalle*/
			" AND st.estado="+ConstantesBD.codigoEstadoTrasladoDespachada;

        private static final String consultaDetalladaXTransaccionp3=" ) s order by \"numero_traslado\", \"nom_almacen_despacha\" asc ";


        private static final String consultaDetalladaXClaseInventariop0=" SELECT " +
						" \"codigo_almacen_despacha\", \"nom_almacen_despacha\"," +
						" \"numero_traslado\",\"codigo_almacen_solicita\"," +
						" \"nom_almacen_solicita\", \"codigo_clase\", \"nombre_clase\"," +
						" \"unidad_medida\",\"cantidaddespachada\", \"valor_unitario\"," +
						" \"valor_total\" " +
				" FROM (";

        private static final String consultaDetalladaXClaseInventariop1=" SELECT " +
						" st.almacen_solicitado As \"codigo_almacen_despacha\"," +
						" getnomcentrocosto(st.almacen_solicitado) as \"nom_almacen_despacha\"," +
						" st.numero_traslado as \"numero_traslado\"," +
						" st.almacen_solicita As \"codigo_almacen_solicita\"," +
						" getnomcentrocosto(st.almacen_solicita) as \"nom_almacen_solicita\"," +
						" getclasearticulo(dst.articulo) As \"codigo_clase\"," +
						" getnombreclaseinventario(getclasearticulo(dst.articulo)) As \"nombre_clase\"," +
						" getunidadmedidaarticulo(dst.articulo) As \"unidad_medida\"," +
						" 0 as \"cantidaddespachada\"," +
						" CASE WHEN ddta.valor_unitario IS NULL THEN 0 ELSE ddta.valor_unitario end as \"valor_unitario\"," +
						" ((CASE WHEN ddta.cantidad IS NULL THEN 0 ELSE ddta.cantidad end) * (CASE WHEN ddta.valor_unitario IS NULL THEN 0 ELSE ddta.valor_unitario end)) as \"valor_total\", " +
						" dst.articulo As articulo " +
				" FROM solicitud_traslado_almacen st" +
				" INNER JOIN det_des_traslado_almacen ddta on (ddta.numero_traslado=st.numero_traslado)" +
				" INNER JOIN det_sol_traslado_almacen  dst on (dst.numero_traslado=st.numero_traslado)" +
				" LEFT OUTER JOIN despacho_traslado_almacen dta on (dta.numero_traslado=st.numero_traslado)" +
				" INNER JOIN centros_costo cc on (cc.codigo=st.almacen_solicita) " +
				" INNER JOIN centros_costo cc1 on (cc1.codigo=st.almacen_solicitado)" +
				" WHERE cc.institucion=?  " +
				/*INICIO MODIFICACION mt4086 Alberto Ovalle*/
				/*" and st.prioritario="+ValoresPorDefecto.getValorFalseParaConsultas()+*/
				" and st.prioritario in(true,false)" +
				/*FIN MODIFIACION Alberto Ovalle*/
				" and st.estado<>"+ConstantesBD.codigoEstadoTrasladoDespachada;



        private static final String consultaDetalladaXClaseInventariop2=" SELECT " +
						" st.almacen_solicitado As \"codigo_almacen_despacha\"," +
						" getnomcentrocosto(st.almacen_solicitado) as \"nom_almacen_despacha\"," +
						" st.numero_traslado as \"numero_traslado\"," +
						" st.almacen_solicita As \"codigo_almacen_solicita\"," +
						" getnomcentrocosto(st.almacen_solicita) as \"nom_almacen_solicita\"," +
						" getclasearticulo(ddta.articulo) As \"codigo_clase\"," +
						" getnombreclaseinventario(getclasearticulo(ddta.articulo)) As \"nombre_clase\"," +
						" getunidadmedidaarticulo(ddta.articulo) As \"unidad_medida\"," +
						" CASE WHEN ddta.cantidad IS NULL THEN 0 ELSE ddta.cantidad END as \"cantidaddespachada\"," +
						" CASE WHEN ddta.valor_unitario IS NULL THEN 0 ELSE ddta.valor_unitario end as \"valor_unitario\"," +
						" ((CASE WHEN ddta.cantidad IS NULL THEN 0 ELSE ddta.cantidad end) * (CASE WHEN ddta.valor_unitario IS NULL THEN 0 ELSE ddta.valor_unitario end)) as \"valor_total\", " +
						" ddta.articulo As articulo " +
				" FROM solicitud_traslado_almacen st" +
				" INNER JOIN det_des_traslado_almacen ddta on (ddta.numero_traslado=st.numero_traslado)" +
				" INNER JOIN despacho_traslado_almacen dta on (dta.numero_traslado=st.numero_traslado)" +
				" INNER JOIN centros_costo cc on (cc.codigo=st.almacen_solicita)" +
				" INNER JOIN centros_costo cc1 on (cc1.codigo=st.almacen_solicitado)" +
				" WHERE cc.institucion=? " +
				/*INICIO MODIFICACION mt4086 Alberto Ovalle*/
				/*" and st.prioritario="+ValoresPorDefecto.getValorFalseParaConsultas()+*/
				" and st.prioritario in(true,false)" +
				/*FIN MODIFIACION Alberto Ovalle*/
				" AND st.estado="+ConstantesBD.codigoEstadoTrasladoDespachada;



        private static final String consultaDetalladaXClaseInventariop3=" ) s order by \"nombre_clase\" asc " ;

    
    /**
     * Metodo encargado armar toda la consulta Detallada por almacen
     * @param vo
     * -----------------------------------
     * KEY'S DEL MAPA VO
     * -----------------------------------
     * -- codAlmacenDespacha
     * -- codAlmacenSolicita
     * -- noTrasladoInicial
     * -- noTrasladoFinal
     * -- fechaInicialSolicitud
     * -- fechaInicialDespacho
     * -- usuarioSolicita
     * -- usuarioDespacha
     * -- codEstado
     * -- prioridad
     * -- claseInventario
     * -- articulo
     * -- tipoCodigoArticulo
     * @return String con la consulta
     */
    public static String consultasReportes (HashMap vo)
    {
    	  String cadena0="";
          String cadena1="";
          String cadena2="";
          String cadena3="";
     
          if ((vo.get("tipoReporte")+"").equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoXAlmacen))
  		{
        	  cadena0=consultaDetalladaXAlmacen0;
              cadena1=consultaDetalladaXAlmacen1.replace("?", vo.get("institucion")+"");
              cadena2=consultaDetalladaXAlmacen2.replace("?", vo.get("institucion")+"");
              cadena3=consultaDetalladaXAlmacen3;
  		
  		}
  		else
  			if ((vo.get("tipoReporte")+"").equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoXClaseInventario))
  			{
  				cadena0=consultaDetalladaXClaseInventario0;
  				cadena1=consultaDetalladaXClaseInventario1.replace("?", vo.get("institucion")+"");
  				cadena2=consultaDetalladaXClaseInventario2.replace("?", vo.get("institucion")+"");
  				cadena3=consultaDetalladaXClaseInventario3;
  			}
  			else
  				if ((vo.get("tipoReporte")+"").equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoXTransaccion))
  				{
  					cadena0=consultaDetalladaXTransaccion0;
  					cadena1=consultaDetalladaXTransaccion1.replace("?", vo.get("institucion")+"");;
  					cadena2=consultaDetalladaXTransaccion2.replace("?", vo.get("institucion")+"");
  					cadena3=consultaDetalladaXTransaccion3;
  				}
          
          
          
          
          String cadena="";
          
          if(!vo.get("codAlmacenDespacha").equals(ConstantesBD.codigoNuncaValido+""))
              cadena=cadena+" and st.almacen_solicitado="+vo.get("codAlmacenDespacha");
          if(!vo.get("codAlmacenSolicita").equals(ConstantesBD.codigoNuncaValido+""))
              cadena=cadena+" and st.almacen_solicita="+vo.get("codAlmacenSolicita");
          
          if(!vo.get("noTrasladoInicial").equals("") && !vo.get("noTrasladoFinal").equals(""))
             cadena=cadena+" and st.numero_traslado between "+vo.get("noTrasladoInicial")+" and "+vo.get("noTrasladoFinal");
        
          if(!vo.get("noTrasladoInicial").equals("") && vo.get("noTrasladoFinal").equals(""))
             cadena=cadena+" and st.numero_traslado="+vo.get("noTrasladoInicial");
         
          if(!vo.get("noTrasladoFinal").equals("") && vo.get("noTrasladoInicial").equals(""))
             cadena=cadena+" and st.numero_traslado="+vo.get("noTrasladoFinal");
         
         if(!vo.get("fechaInicialSolicitud").equals("") && !vo.get("fechaFinalSolicitud").equals(""))
             cadena=cadena+" and to_char(st.fecha_elaboracion, 'YYYY-MM-DD') between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicialSolicitud")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinalSolicitud")+"")+"'";
         
         if(!vo.get("fechaInicialSolicitud").equals("") && vo.get("fechaFinalSolicitud").equals(""))
             cadena=cadena+" and to_char(st.fecha_elaboracion, 'YYYY-MM-DD')='"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicialSolicitud")+"")+"'";
       
         if(!vo.get("fechaFinalSolicitud").equals("") && vo.get("fechaInicialSolicitud").equals(""))
             cadena=cadena+" and to_char(st.fecha_elaboracion, 'YYYY-MM-DD')='"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinalSolicitud")+"")+"'";
         
         if(!vo.get("fechaInicialDespacho").equals("") && !vo.get("fechaFinalDespacho").equals(""))
             cadena=cadena+" and to_char(dta.fecha_despacho,'yyyy-mm-dd') between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicialDespacho")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinalDespacho")+"")+"'";
        
         if(!vo.get("fechaInicialDespacho").equals("") && vo.get("fechaFinalDespacho").equals(""))
             cadena=cadena+" and to_char(dta.fecha_despacho,'yyyy-mm-dd')='"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicialDespacho")+"")+"'";
         
         if(!vo.get("fechaFinalDespacho").equals("") && vo.get("fechaInicialDespacho").equals(""))
             cadena=cadena+" and to_char(dta.fecha_despacho,'yyyy-mm-dd')='"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinalDespacho")+"")+"'"; 
         
         if(!vo.get("usuarioSolicita").equals("Seleccione"))
             cadena=cadena+" and st.usuario_elabora='"+vo.get("usuarioSolicita")+"'";
        
         if(!vo.get("usuarioDespacha").equals("Seleccione"))
             cadena=cadena+" and dta.usuario_despacho='"+vo.get("usuarioDespacha")+"'";
         
         if(!vo.get("codEstado").equals(ConstantesBD.codigoNuncaValido+""))
             cadena=cadena+" and st.estado="+vo.get("codEstado");
         
         logger.info("------PRIORIDAD----->"+vo.get("prioridad")+"<--------");
         if(!UtilidadTexto.isEmpty(vo.get("prioridad")+""))
         {
        	 
        	 if(UtilidadTexto.getBoolean(vo.get("prioridad")+""))
        		 cadena=cadena+" and st.prioritario='"+ValoresPorDefecto.getValorTrueParaConsultas()+"'";
        	 else
        		 cadena=cadena+" and st.prioritario="+ValoresPorDefecto.getValorFalseParaConsultas()+" ";
         }
         
         
         //hasta aqui las dos clausulas del where son iguales pero de aqui en 
         //adelante cambian
         String where1=cadena;
         String where2=cadena;
         //clase de inventario
         if (UtilidadCadena.noEsVacio(vo.get("claseInventario")+"") && !(vo.get("claseInventario")+"").equals(ConstantesBD.codigoNuncaValido+""))
         {
      	   where1=" AND getclasearticulo(dst.articulo)="+vo.get("claseInventario");
      	   where2=" AND getclasearticulo(ddta.articulo)="+vo.get("claseInventario");
         }
      	   
      	   
         //articulo
         if (UtilidadCadena.noEsVacio(vo.get("articulo")+"") && !(vo.get("articulo")+"").equals(ConstantesBD.codigoNuncaValido+""))
         {
      	   //este es el where para la consulta 1
      	   where1+=" AND dst.articulo="+vo.get("articulo");
      	   where2+=" AND ddta.articulo="+vo.get("articulo");
         }
         
    logger.info("\n el tipo de codigo -->"+vo.get("tipoCodigoArticulo"));
    
  
        	if ((vo.get("tipoCodigoArticulo")+"").equals(ConstantesIntegridadDominio.acronimoInterfaz))
        	{
        		cadena1=cadena1.replace("@@", "2");
        		cadena2=cadena2.replace("@@", "2");
        	}
        	else
        		if ((vo.get("tipoCodigoArticulo")+"").equals(ConstantesIntegridadDominio.acronimoAmbos))
        		{
        			cadena1=cadena1.replace("@@", "3");
            		cadena2=cadena2.replace("@@", "3");
        		}
        		else
        		{
        			logger.info("\n entre a reemplazar por 1");
        			cadena1=cadena1.replace("@@", "1");
        			cadena2=cadena2.replace("@@", "1");
        		}
       
         String sql=cadena0+cadena1+where1+" UNION "+cadena2+where2+cadena3;
          
          
    	
    	return sql;
    }
    
    /**
     * Metodo encargado de ejecutar la  consulta enviada.
     * @param connection
     * @param consulta
     * @return
     */
    public static HashMap ejecutarConsulta (Connection connection,String consulta)
    {
    	logger.info("\n entre a ejecutarConsulta \n consunlta -->  "+consulta);
    	try
    	{
    		PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
    		HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
    		ps.close();
    		return mapaRetorno;
		} catch (SQLException e) {
			logger.info("\n problema consultandao el reporte "+e);
		}
    	
    	return null;
    	
    }
    
    
    /***********************************************************************************************************
     * fin Modificacion por anexo 632 
     ***********************************************************************************************************/
    
    
    /**
     * metodo para realizar la busqueda avanzada de
     * listado traslado almacen
     * @param con Connection
     * @param vo HashMap
     * @return HashMap
     */
    public static HashMap ejecutarBusquedaAvanzadaTraslados(Connection con,HashMap vo)
    {
    	logger.info("\n entre a  ejecutarBusquedaAvanzadaTraslados criterios -->"+vo);
    	
        HashMap mapa=new HashMap();        
        String cadena="";
        
        String cadena0=consultaListadoTrasladoAlmacenStr0;
        String cadena1=consultaListadoTrasladosAlmacenStr1;
        String cadena2=consultaListadoTrasladosAlmacenStr2;
        String cadena3=consultaListadoTrasladosAlmacenStr3;
        
        
        if(!vo.get("codAlmacenDespacha").equals(ConstantesBD.codigoNuncaValido+""))
            cadena=cadena+" and st.almacen_solicitado="+vo.get("codAlmacenDespacha");
        if(!vo.get("codAlmacenSolicita").equals(ConstantesBD.codigoNuncaValido+""))
            cadena=cadena+" and st.almacen_solicita="+vo.get("codAlmacenSolicita");
        
        if(!vo.get("noTrasladoInicial").equals("") && !vo.get("noTrasladoFinal").equals(""))
           cadena=cadena+" and st.numero_traslado between "+vo.get("noTrasladoInicial")+" and "+vo.get("noTrasladoFinal");
       if(!vo.get("noTrasladoInicial").equals("") && vo.get("noTrasladoFinal").equals(""))
           cadena=cadena+" and st.numero_traslado="+vo.get("noTrasladoInicial");
       if(!vo.get("noTrasladoFinal").equals("") && vo.get("noTrasladoInicial").equals(""))
           cadena=cadena+" and st.numero_traslado="+vo.get("noTrasladoFinal");
       
       if(!vo.get("fechaInicialSolicitud").equals("") && !vo.get("fechaFinalSolicitud").equals(""))
           cadena=cadena+" and to_char(st.fecha_elaboracion, 'YYYY-MM-DD') between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicialSolicitud")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinalSolicitud")+"")+"'";
       if(!vo.get("fechaInicialSolicitud").equals("") && vo.get("fechaFinalSolicitud").equals(""))
           cadena=cadena+" and to_char(st.fecha_elaboracion, 'YYYY-MM-DD')='"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicialSolicitud")+"")+"'";
       if(!vo.get("fechaFinalSolicitud").equals("") && vo.get("fechaInicialSolicitud").equals(""))
           cadena=cadena+" and to_char(st.fecha_elaboracion, 'YYYY-MM-DD')='"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinalSolicitud")+"")+"'";
       
       if(!vo.get("fechaInicialDespacho").equals("") && !vo.get("fechaFinalDespacho").equals(""))
           cadena=cadena+" and to_char(dta.fecha_despacho,'yyyy-mm-dd') between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicialDespacho")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinalDespacho")+"")+"'";
       if(!vo.get("fechaInicialDespacho").equals("") && vo.get("fechaFinalDespacho").equals(""))
           cadena=cadena+" and to_char(dta.fecha_despacho,'yyyy-mm-dd')='"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicialDespacho")+"")+"'";
       if(!vo.get("fechaFinalDespacho").equals("") && vo.get("fechaInicialDespacho").equals(""))
           cadena=cadena+" and to_char(dta.fecha_despacho,'yyyy-mm-dd')='"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinalDespacho")+"")+"'"; 
       
       if(!vo.get("usuarioSolicita").equals("Seleccione"))
           cadena=cadena+" and st.usuario_elabora='"+vo.get("usuarioSolicita")+"'";
       if(!vo.get("usuarioDespacha").equals("Seleccione"))
           cadena=cadena+" and dta.usuario_despacho='"+vo.get("usuarioDespacha")+"'";
       
       if(!vo.get("codEstado").equals(ConstantesBD.codigoNuncaValido+""))
           cadena=cadena+" and st.estado="+vo.get("codEstado");
       
       logger.info("------PRIORIDAD----->"+vo.get("prioridad")+"<--------");
       if(!UtilidadTexto.isEmpty(vo.get("prioridad")+""))
       {
    	   if(UtilidadTexto.getBoolean(vo.get("prioridad")+""))
    		   cadena += " and st.prioritario="+ValoresPorDefecto.getValorTrueParaConsultas()+" ";
    	   else
    		   cadena += " and st.prioritario="+ValoresPorDefecto.getValorFalseParaConsultas()+" ";
       }    
       
       /********************************************************
        * Modificacion por anexo 632
        ********************************************************/
       //hasta aqui las dos clausulas del where son iguales pero de aqui en 
       //adelante cambian
       String where1=cadena;
       String where2=cadena;
       //clase de inventario
       if (UtilidadCadena.noEsVacio(vo.get("claseInventario")+"") && !(vo.get("claseInventario")+"").equals(ConstantesBD.codigoNuncaValido+""))
       {
    	   where1=" AND getclasearticulo(dst.articulo)="+vo.get("claseInventario");
    	   where2=" AND getclasearticulo(ddta.articulo)="+vo.get("claseInventario");
       }
    	   
    	   
       //articulo
       if (UtilidadCadena.noEsVacio(vo.get("articulo")+"") && !(vo.get("articulo")+"").equals(ConstantesBD.codigoNuncaValido+""))
       {
    	   //este es el where para la consulta 1
    	   where1+=" AND dst.articulo="+vo.get("articulo");
    	   where2+=" AND ddta.articulo="+vo.get("articulo");
       }
       
  
       /********************************************************
        * fin de la modificacion
        ********************************************************/
       
       String sql=cadena0+cadena1+where1+" UNION "+cadena2+where2+cadena3;
       
       logger.info("\n cadena --> "+sql);
       try
       {
           PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(sql,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
           ps.setInt(1,Utilidades.convertirAEntero(vo.get("institucion")+""));
           ps.setInt(2,Utilidades.convertirAEntero(vo.get("institucion")+""));
           mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
       }
       catch (SQLException e)
       {
           logger.error("Error generando la consulta del listado de traslados"+e);
           e.printStackTrace();
           mapa=new HashMap();
           mapa.put("numRegistros", "0");
       }
       catch (Exception e) 
       {
    	   logger.error("Error generando la consulta del listado de traslados"+e);
           e.printStackTrace();
           mapa=new HashMap();
           mapa.put("numRegistros", "0");
       }
        return (HashMap) mapa.clone();
    }
    /**
     * metodo para consultar el detalle de la
     * solicitud
     * @param con Connection
     * @param numeroSolicitud int
     * @return HashMap
     */
    public static HashMap consultaDetalleSolicitud(Connection con,int numeroSolicitud)
    {      
    	logger.info("\n entre a consultaDetalleSolicitud numeroSolicitud -->"+numeroSolicitud);
    	
    	logger.info("\n cadena --> "+consultaDetalleSolicitudStr); 
        try
        {
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaDetalleSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,numeroSolicitud);
            ps.setInt(2,numeroSolicitud);
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery()); 
            HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
	        rs.close();
	        ps.close();
	        return mapaRetorno;
        }
        catch (SQLException e)
        {
            logger.error("Error en consultaDetalleSolicitud "+e);
            e.printStackTrace();
            return null;
        }      
    }
    /**
     * mt 4086
     * Alberto Ovalle Solucion permita resultados independientes
     * @param vo
     * @return
     */
    public static String consultasReportespostgresql(HashMap vo)
    {
    	  String cadena0="";
          String cadena1="";
          String cadena2="";
          String cadena3="";
     
          if ((vo.get("tipoReporte")+"").equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoXAlmacen))
  		{
        	  cadena0=consultaDetalladaXAlmacenp0;
              cadena1=consultaDetalladaXAlmacenp1.replace("?", vo.get("institucion")+"");
              cadena2=consultaDetalladaXAlmacenp2.replace("?", vo.get("institucion")+"");
              cadena3=consultaDetalladaXAlmacenp3;
  		
  		}
  		else
  			if ((vo.get("tipoReporte")+"").equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoXClaseInventario))
  			{
  				cadena0=consultaDetalladaXClaseInventariop0;
  				cadena1=consultaDetalladaXClaseInventariop1.replace("?", vo.get("institucion")+"");
  				cadena2=consultaDetalladaXClaseInventariop2.replace("?", vo.get("institucion")+"");
  				cadena3=consultaDetalladaXClaseInventariop3;
  			String sql = cadena0+cadena1+cadena2+cadena3;
  			System.out.println("Impresionsql"+sql);
  			}
  			else
  				if ((vo.get("tipoReporte")+"").equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoXTransaccion))
  				{
  					cadena0=consultaDetalladaXTransaccionp0;
  					cadena1=consultaDetalladaXTransaccionp1.replace("?", vo.get("institucion")+"");;
  					cadena2=consultaDetalladaXTransaccionp2.replace("?", vo.get("institucion")+"");
  					cadena3=consultaDetalladaXTransaccionp3;
  				}
          
          
          
          
          String cadena="";
          
          if(!vo.get("codAlmacenDespacha").equals(ConstantesBD.codigoNuncaValido+""))
              cadena=cadena+" and st.almacen_solicitado="+vo.get("codAlmacenDespacha");
          if(!vo.get("codAlmacenSolicita").equals(ConstantesBD.codigoNuncaValido+""))
              cadena=cadena+" and st.almacen_solicita="+vo.get("codAlmacenSolicita");
          
          if(!vo.get("noTrasladoInicial").equals("") && !vo.get("noTrasladoFinal").equals(""))
             cadena=cadena+" and st.numero_traslado between "+vo.get("noTrasladoInicial")+" and "+vo.get("noTrasladoFinal");
        
          if(!vo.get("noTrasladoInicial").equals("") && vo.get("noTrasladoFinal").equals(""))
             cadena=cadena+" and st.numero_traslado="+vo.get("noTrasladoInicial");
         
          if(!vo.get("noTrasladoFinal").equals("") && vo.get("noTrasladoInicial").equals(""))
             cadena=cadena+" and st.numero_traslado="+vo.get("noTrasladoFinal");
         
         if(!vo.get("fechaInicialSolicitud").equals("") && !vo.get("fechaFinalSolicitud").equals(""))
             cadena=cadena+" and to_char(st.fecha_elaboracion, 'YYYY-MM-DD') between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicialSolicitud")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinalSolicitud")+"")+"'";
         
         if(!vo.get("fechaInicialSolicitud").equals("") && vo.get("fechaFinalSolicitud").equals(""))
             cadena=cadena+" and to_char(st.fecha_elaboracion, 'YYYY-MM-DD')='"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicialSolicitud")+"")+"'";
       
         if(!vo.get("fechaFinalSolicitud").equals("") && vo.get("fechaInicialSolicitud").equals(""))
             cadena=cadena+" and to_char(st.fecha_elaboracion, 'YYYY-MM-DD')='"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinalSolicitud")+"")+"'";
         
         if(!vo.get("fechaInicialDespacho").equals("") && !vo.get("fechaFinalDespacho").equals(""))
             cadena=cadena+" and to_char(dta.fecha_despacho,'yyyy-mm-dd') between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicialDespacho")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinalDespacho")+"")+"'";
        
         if(!vo.get("fechaInicialDespacho").equals("") && vo.get("fechaFinalDespacho").equals(""))
             cadena=cadena+" and to_char(dta.fecha_despacho,'yyyy-mm-dd')='"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicialDespacho")+"")+"'";
         
         if(!vo.get("fechaFinalDespacho").equals("") && vo.get("fechaInicialDespacho").equals(""))
             cadena=cadena+" and to_char(dta.fecha_despacho,'yyyy-mm-dd')='"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinalDespacho")+"")+"'"; 
         
         if(!vo.get("usuarioSolicita").equals("Seleccione"))
             cadena=cadena+" and st.usuario_elabora='"+vo.get("usuarioSolicita")+"'";
        
         if(!vo.get("usuarioDespacha").equals("Seleccione"))
             cadena=cadena+" and dta.usuario_despacho='"+vo.get("usuarioDespacha")+"'";
         
         if(!vo.get("codEstado").equals(ConstantesBD.codigoNuncaValido+""))
             cadena=cadena+" and st.estado="+vo.get("codEstado");
         
         logger.info("------PRIORIDAD----->"+vo.get("prioridad")+"<--------");
         if(!UtilidadTexto.isEmpty(vo.get("prioridad")+""))
         {
        	 
        	 if(UtilidadTexto.getBoolean(vo.get("prioridad")+""))
        		 cadena=cadena+" and st.prioritario='"+ValoresPorDefecto.getValorTrueParaConsultas()+"'";
        	 else
        		 cadena=cadena+" and st.prioritario="+ValoresPorDefecto.getValorFalseParaConsultas()+" ";
         }
         
         
         //hasta aqui las dos clausulas del where son iguales pero de aqui en 
         //adelante cambian
         String where1=cadena;
         String where2=cadena;
         //clase de inventario
         if (UtilidadCadena.noEsVacio(vo.get("claseInventario")+"") && !(vo.get("claseInventario")+"").equals(ConstantesBD.codigoNuncaValido+""))
         {
      	   where1=" AND getclasearticulo(dst.articulo)="+vo.get("claseInventario");
      	   where2=" AND getclasearticulo(ddta.articulo)="+vo.get("claseInventario");
         }
      	   
      	   
         //articulo
         if (UtilidadCadena.noEsVacio(vo.get("articulo")+"") && !(vo.get("articulo")+"").equals(ConstantesBD.codigoNuncaValido+""))
         {
      	   //este es el where para la consulta 1
      	   where1+=" AND dst.articulo="+vo.get("articulo");
      	   where2+=" AND ddta.articulo="+vo.get("articulo");
         }
         
    logger.info("\n el tipo de codigo -->"+vo.get("tipoCodigoArticulo"));
    
  
        	if ((vo.get("tipoCodigoArticulo")+"").equals(ConstantesIntegridadDominio.acronimoInterfaz))
        	{
        		cadena1=cadena1.replace("@@", "2");
        		cadena2=cadena2.replace("@@", "2");
        	}
        	else
        		if ((vo.get("tipoCodigoArticulo")+"").equals(ConstantesIntegridadDominio.acronimoAmbos))
        		{
        			cadena1=cadena1.replace("@@", "3");
            		cadena2=cadena2.replace("@@", "3");
        		}
        		else
        		{
        			logger.info("\n entre a reemplazar por 1");
        			cadena1=cadena1.replace("@@", "1");
        			cadena2=cadena2.replace("@@", "1");
        		}
       
         String sql=cadena0+cadena1+where1+" UNION "+cadena2+where2+cadena3;
          
          
    	
    	return sql;
    }
    
    
}
