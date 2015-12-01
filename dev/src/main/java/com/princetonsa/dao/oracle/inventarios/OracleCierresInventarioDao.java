/*
 * Created on 19/12/2005
 * 
 * @author <a href="mailto:artotor@hotmail.com">Jorge Armando Osorio Velásquez</a>
 * 
 * Copyright Princeton S.A. &copy;&reg; 2005. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 *
 */
package com.princetonsa.dao.oracle.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.inventarios.CierresInventarioDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseCierresInventarioDao;

/**
 * @version 1.0, 19/12/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class OracleCierresInventarioDao implements CierresInventarioDao
{

	public  HashMap cargarMovimientosInventarios(Connection con, int institucion, String fechaInicial, String fechaFinal)
	{
		String strConsultaMovimientosInventarios = "SELECT t.almacen as almacen, " + 
    			"t.articulo as articulo, " + 
    			"trim(case when t.lote is null then '' else t.lote end) as lote,  " + 
    			"trim(case when t.fechavencimiento is null then '' else t.fechavencimiento||'' end) as fechavencimiento, " +
    			"sum(convertiranumero(t.valorentrada||'')) as valtotalentradas, " +
    			"sum(convertiranumero(t.valorsalida||'')) as valtotalsalida, " +
    			"sum(convertiranumero(t.cantidadentrada||'')) as cantotalentrada, " +
    			"sum(convertiranumero(t.cantidadsalida||'')) as cantidadsalida " +
    			"FROM ( " + 
    			"SELECT txa.codigo " + 
    		    "||'' AS codigomovimiento, " + 
    		    "tti.codigo " + 
    		    "||'' AS codigotransaccion, " + 
    		    "tti.descripcion " + 
    		    "||'' AS desctransaccion, " + 
    		    "txa.consecutivo " + 
    		    "||'' AS documento, " + 
    		    "TO_CHAR(txa.fecha_elaboracion,'yyyy-mm-dd') " + 
    		    "||'' AS fechaelaboracion, " + 
    		    "TO_CHAR(txa.fecha_cierre,'yyyy-mm-dd') " + 
    		    "||'' AS fechaatencion, " + 
    		    "txa.hora_cierre " + 
    		    "|| '' AS horaatencion, " + 
    		    "txa.almacen " + 
    		    "||'' AS almacen, " + 
    		    "''   AS almacendestino, " + 
    		    "dtxa.articulo " + 
    		    "||''                   AS articulo, " + 
    		    "dtxa.lote              AS lote, " + 
    		    "dtxa.fecha_vencimiento AS fechavencimiento, " + 
    		    "dtxa.val_unitario " + 
    		    "||'' AS costounitario, " + 
    		    "CASE " + 
    		      "WHEN tti.tipos_conceptos_inv=1 " + 
    		      "THEN dtxa.cantidad " + 
    		      "ELSE 0 " + 
    		    "END " + 
    		    "||'' AS cantidadentrada, " + 
    		    "CASE " + 
    		      "WHEN tti.tipos_conceptos_inv=1 " + 
    		      "THEN dtxa.cantidad*dtxa.val_unitario " + 
    		      "ELSE 0 " + 
    		    "END " + 
    		    "||'' AS valorentrada, " + 
    		    "CASE " + 
    		      "WHEN tti.tipos_conceptos_inv=2 " + 
    		      "THEN dtxa.cantidad " + 
    		      "ELSE 0 " + 
    		    "END " + 
    		    "||'' " + 
    		    "||'' AS cantidadsalida, " + 
    		    "CASE " + 
    		      "WHEN tti.tipos_conceptos_inv=2 " + 
    		      "THEN dtxa.cantidad*dtxa.val_unitario " + 
    		      "ELSE 0 " + 
    		    "END " + 
    		    "||'' AS valorsalida, " + 
    		    "txa.usuario " + 
    		    "||'' AS usuariomovimiento, " + 
    		    "txa.entidad " + 
    		    "||''                    AS tercero, " + 
    		    "dtxa.proveedor_compra   AS proveedorcompra, " + 
    		    "dtxa.proveedor_catalogo AS proveedorcatalogo, " + 
    		    "tti.tipos_conceptos_inv " + 
    		    "||'' AS tipoconceptoinv " + 
    		  "FROM inventarios.transacciones_x_almacen txa " + 
    		  "INNER JOIN inventarios.det_trans_x_almacen dtxa " + 
    		  "ON (dtxa.transaccion=txa.codigo) " + 
    		  "INNER JOIN inventarios.tipos_trans_inventarios tti " + 
    		  "ON (txa.transaccion=tti.consecutivo) " + 
    		  "WHERE txa.estado   =2 " + 
  			"AND to_date(txa.fecha_cierre) between to_date('"+fechaInicial+"','DD/MM/YY') AND to_date('"+fechaFinal+"','DD/MM/YY')  " +
		"UNION  " + 
		"SELECT sta.numero_traslado " +
	    "||'' AS codigomovimiento, " +
	    "(SELECT codigo " +
	    "FROM inventarios.tipos_trans_inventarios " +
	    "WHERE consecutivo= " +
	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " +
	      "FROM administracion.valores_por_defecto " +
	      "WHERE parametro ='codigo_trans_traslado_almacen' " +
	      ") " +
	    ") " +
	    "||'' AS codigotransaccion, " +
	    "(SELECT descripcion " +
	    "FROM inventarios.tipos_trans_inventarios " +
	    "WHERE consecutivo= " +
	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " +
	      "FROM administracion.valores_por_defecto " +
	      "WHERE parametro ='codigo_trans_traslado_almacen' " +
	      ") " +
	    ") " +
	    "||'' AS desctransaccion, " +
	    "sta.numero_traslado " +
	    "||'' AS documento, " +
	    "TO_CHAR(sta.fecha_elaboracion,'yyyy-mm-dd') " +
	    "||'' AS fechaelaboracion, " +
	    "TO_CHAR(dta.fecha_despacho,'yyyy-mm-dd') " + 
	    "||'' AS fechaatencion, " +
	    "dta.hora_despacho " +
	    "|| '' AS horaatencion, " +
	    "sta.almacen_solicita " +
	    "||'' AS almacen, " +
	    "sta.almacen_solicitado " +
	    "||'' AS almacendestino, " +
	    "ddta.articulo " +
	    "||''                   AS articulo, " +
	    "ddta.lote              AS lote, " +
	    "ddta.fecha_vencimiento AS fechavencimiento, " +
	    "ddta.valor_unitario " +
	    "||'' AS costounitario, " +
	    "ddta.cantidad " +
	    "||'' AS cantidadentrada, " +
	    "(ddta.cantidad*ddta.valor_unitario) " +
	    "||'' AS valorentrada, " +
	    "0 " +
	    "||'' AS cantidadsalida, " +
	    "0 " +
	    "||'' AS valorsalida, " +
	    "sta.usuario_elabora " +
	    "||''           AS usuariomovimiento, " +
	    "''             AS tercero, " +
	    "NULL           AS proveedorcompra, " +
	    "ddta.proveedor AS proveedorcatalogo, " +
	    "(SELECT tipos_conceptos_inv " +
	    "FROM inventarios.tipos_trans_inventarios " +
	    "WHERE consecutivo= " +
	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " +
	      "FROM administracion.valores_por_defecto " +
	      "WHERE parametro ='codigo_trans_traslado_almacen' " +
	      ") " +
	    ") " +
	    "||'' AS tipoconceptoinv " +
	  "FROM inventarios.solicitud_traslado_almacen sta " +
	  "INNER JOIN inventarios.despacho_traslado_almacen dta " +
	  "ON(sta.numero_traslado=dta.numero_traslado) " +
	  "INNER JOIN inventarios.det_des_traslado_almacen ddta " +
	  "ON(ddta.numero_traslado=dta.numero_traslado) " +
	  "WHERE sta.estado=4 " +
		"AND to_date(dta.fecha_despacho) between to_date('"+fechaInicial+"','DD/MM/YY') AND to_date('"+fechaFinal+"','DD/MM/YY')  " +
		"UNION " + 
		"SELECT sta.numero_traslado " +
	    "||'' AS codigomovimiento, " +
	    "(SELECT codigo " +
	    "FROM inventarios.tipos_trans_inventarios " +
	    "WHERE consecutivo= " +
	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " +
	      "FROM administracion.valores_por_defecto " +
	      "WHERE parametro ='codigo_trans_traslado_almacen' " +
	      ") " +
	    ") " +
	    "||'' AS codigotransaccion, " +
	    "(SELECT descripcion " +
	    "FROM inventarios.tipos_trans_inventarios " +
	    "WHERE consecutivo= " +
	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " +
	      "FROM administracion.valores_por_defecto " +
	      "WHERE parametro ='codigo_trans_traslado_almacen' " +
	      ") " +
	    ") " +
	    "||'' AS desctransaccion, " +
	    "sta.numero_traslado " +
	    "||'' AS documento, " +
	    "TO_CHAR(sta.fecha_elaboracion,'yyyy-mm-dd') " +
	    "||'' AS fechaelaboracion, " +
	    "TO_CHAR(dta.fecha_despacho,'yyyy-mm-dd') " +
	    "||'' AS fechaatencion, " +
	    "dta.hora_despacho " +
	    "|| '' AS horaatencion, " +
	    "sta.almacen_solicitado " +
	    "||'' AS almacen, " +
	    "sta.almacen_solicita " +
	    "||'' AS almacendestino, " +
	    "ddta.articulo " +
	    "||''                   AS articulo, " +
	    "ddta.lote              AS lote, " +
	    "ddta.fecha_vencimiento AS fechavencimiento, " +
	    "ddta.valor_unitario " +
	    "||'' AS costounitario, " +
	    "0 " +
	    "||'' AS cantidadsalida, " +
	    "0 " +
	    "||'' AS valorsalida, " +
	    "ddta.cantidad " +
	    "||'' AS cantidadsalida, " +
	    "COALESCE((ddta.cantidad*ddta.valor_unitario),0) " +
	    "||'' AS valorsalida, " +
	    "dta.usuario_despacho " +
	    "||''           AS usuariomovimiento, " +
	    "''             AS tercero , " +
	    "NULL           AS proveedorcompra, " +
	    "ddta.proveedor AS proveedorcatalogo, " +
	    "(SELECT tipos_conceptos_inv " +
	    "FROM inventarios.tipos_trans_inventarios " +
	    "WHERE consecutivo= " +
	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " +
	      "FROM administracion.valores_por_defecto " +
	      "WHERE parametro ='codigo_trans_traslado_almacen' " +
	      ") " +
	    ") " +
	    "||'' AS tipoconceptoinv " +
	  "FROM inventarios.solicitud_traslado_almacen sta " +
	  "INNER JOIN inventarios.despacho_traslado_almacen dta " +
	  "ON(sta.numero_traslado=dta.numero_traslado) " +
	  "INNER JOIN inventarios.det_des_traslado_almacen ddta " +
	  "ON(ddta.numero_traslado=dta.numero_traslado) " +
	  "WHERE sta.estado=4 " +
		"AND to_date(dta.fecha_despacho) between to_date('"+fechaInicial+"','DD/MM/YY') AND to_date('"+fechaFinal+"','DD/MM/YY')  " +
		"UNION " + 
		"SELECT s.numero_solicitud " +
	    "||'' AS codigomovimiento, " +
	    "(SELECT codigo " +
	    "FROM inventarios.tipos_trans_inventarios " +
	    "WHERE consecutivo= " +
	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " +
	      "FROM administracion.valores_por_defecto " +
	      "WHERE parametro ='codigo_trans_soli_pacientes' " +
	      ") " +
	    ") " +
	    "||'' AS codigotransaccion, " +
	    "(SELECT descripcion " +
	    "FROM inventarios.tipos_trans_inventarios " +
	    "WHERE consecutivo= " +
	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " +
	      "FROM administracion.valores_por_defecto " +
	      "WHERE parametro ='codigo_trans_soli_pacientes' " +
	      ") " +
	    ") " +
	    "||'' AS desctransaccion, " +
	    "s.consecutivo_ordenes_medicas " +
	    "||'-' " +
	    "||d.orden " +
	    "||'' AS documento, " +
	    "TO_CHAR(s.fecha_solicitud,'yyyy-mm-dd') " +
	    "||'' AS fechaelaboracion, " +
	    "TO_CHAR(d.fecha,'yyyy-mm-dd') " +
	    "||'' AS fechaatencion, " +
	    "d.hora " +
	    "|| '' AS horaatencion, " +
	    "s.centro_costo_solicitado " +
	    "||'' AS almacen, " +
	    "s.centro_costo_solicitante " +
	    "||'' AS almacendestino, " +
	    "dd.articulo " +
	    "||''                 AS articulo, " +
	    "dd.lote              AS lote, " +
	    "dd.fecha_vencimiento AS fechavencimiento, " +
	    "COALESCE(dd.costo_unitario,0) " +
	    "||'' AS costounitario, " +
	    "0 " +
	    "||'' AS cantidadentrada, " +
	    "0 " +
	    "||'' AS valorentrada, " +
	    "dd.cantidad " +
	    "||'' AS cantidadsalida, " +
	    "COALESCE((dd.cantidad*dd.costo_unitario),0) " +
	    "||'' AS valorsalida, " +
	    "d.usuario " +
	    "||''                  AS usuariomovimiento, " +
	    "''                    AS tercero, " +
	    "dd.proveedor_compra   AS proveedorcompra, " +
	    "dd.proveedor_catalogo AS proveedorcatalogo, " +
	    "(SELECT tipos_conceptos_inv " +
	    "FROM inventarios.tipos_trans_inventarios " +
	    "WHERE consecutivo= " +
	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " +
	      "FROM administracion.valores_por_defecto " +
	      "WHERE parametro ='codigo_trans_soli_pacientes' " +
	      ") " +
	    ") " +
	    "||'' AS tipoconceptoinv " +
	  "FROM ordenes.solicitudes s " +
	  "INNER JOIN facturacion.cargos_directos cardir " +
	  "ON(cardir.numero_solicitud=s.numero_solicitud) " +
	  "INNER JOIN inventarios.despacho d " +
	  "ON(s.numero_solicitud=d.numero_solicitud) " +
	  "INNER JOIN inventarios.detalle_despachos dd " +
	  "ON(d.orden=dd.despacho) " +
	  "WHERE s.estado_historia_clinica=7 " +
	  "AND cardir.afecta_inventarios  ='S' " +
	  "AND s.centro_costo_solicitado <>11 " +
		"AND to_date(d.fecha) between to_date('"+fechaInicial+"','DD/MM/YY') AND to_date('"+fechaFinal+"','DD/MM/YY')  " +
	  "UNION " + 
		"SELECT s.numero_solicitud " +
	    "||'' AS codigomovimiento, " +
	    "(SELECT codigo " +
	    "FROM inventarios.tipos_trans_inventarios " +
	    "WHERE consecutivo= " +
	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " +
	      "FROM administracion.valores_por_defecto " +
	      "WHERE parametro ='codigo_trans_soli_pacientes' " +
	      ") " +
	    ") " +
	    "||'' AS codigotransaccion, " +
	    "(SELECT descripcion " +
	    "FROM inventarios.tipos_trans_inventarios " +
	    "WHERE consecutivo= " +
	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " +
	      "FROM administracion.valores_por_defecto " +
	      "WHERE parametro ='codigo_trans_soli_pacientes' " +
	      ") " +
	    ") " +
	    "||'' AS desctransaccion, " +
	    "s.consecutivo_ordenes_medicas " +
	    "||'-' " +
	    "||d.orden " +
	    "||'' AS documento, " +
	    "TO_CHAR(s.fecha_solicitud,'yyyy-mm-dd') " +
	    "||'' AS fechaelaboracion, " +
	    "TO_CHAR(d.fecha,'yyyy-mm-dd') " +
	    "||'' AS fechaatencion, " +
	    "d.hora " +
	    "|| '' AS horaatencion, " +
	    "s.centro_costo_solicitado " +
	    "||'' AS almacen, " +
	    "s.centro_costo_solicitante " +
	    "||'' AS almacendestino, " +
	    "dd.articulo " +
	    "||''                 AS articulo, " +
	    "dd.lote              AS lote, " +
	    "dd.fecha_vencimiento AS fechavencimiento, " +
	    "dd.costo_unitario " +
	    "||'' AS costounitario, " +
	    "0 " +
	    "||'' AS cantidadentrada, " +
	    "0 " +
	    "||'' AS valorentrada, " +
	    "dd.cantidad " +
	    "||'' AS cantidadsalida, " +
	    "COALESCE((dd.cantidad*dd.costo_unitario),0) " +
	    "||'' AS valorsalida, " +
	    "d.usuario " +
	    "||''                  AS usuariomovimiento, " +
	    "''                    AS tercero, " +
	    "dd.proveedor_compra   AS proveedorcompra, " +
	    "dd.proveedor_catalogo AS proveedorcatalogo, " +
	    "(SELECT tipos_conceptos_inv " +
	    "FROM inventarios.tipos_trans_inventarios " +
	    "WHERE consecutivo= " +
	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " +
	      "FROM administracion.valores_por_defecto " +
	      "WHERE parametro ='codigo_trans_soli_pacientes' " +
	      ") " +
	    ") " +
	    "||'' AS tipoconceptoinv " +
	  "FROM ordenes.solicitudes s " +
	  "INNER JOIN inventarios.despacho d " +
	  "ON(s.numero_solicitud=d.numero_solicitud) " +
	  "INNER JOIN inventarios.detalle_despachos dd " +
	  "ON(d.orden=dd.despacho) " +
	  "WHERE s.estado_historia_clinica IN (1,5,6) " +
	  "AND s.centro_costo_solicitado   <>11 " +
		"AND to_date(d.fecha) between to_date('"+fechaInicial+"','DD/MM/YY') AND to_date('"+fechaFinal+"','DD/MM/YY')  " +
		"UNION " + 
		"SELECT dm.codigo " +
	    "||'' AS codigomovimiento, " +
	    "(SELECT codigo " +
	    "FROM inventarios.tipos_trans_inventarios " +
	    "WHERE consecutivo= " +
	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " +
	      "FROM administracion.valores_por_defecto " +
	      "WHERE parametro ='codigo_trans_devolucion_paciente' " +
	      ") " +
	    ") " +
	    "||'' AS codigotransaccion, " +
	    "(SELECT descripcion " +
	    "FROM inventarios.tipos_trans_inventarios " +
	    "WHERE consecutivo= " +
	    "  (SELECT to_number(COALESCE(trim(valor),'-1')) " +
	      "FROM administracion.valores_por_defecto " +
	      "WHERE parametro ='codigo_trans_devolucion_paciente' " +
	      ") " +
	    ") " +
	    "||'' AS desctransaccion, " +
	    "dm.codigo " +
	    "||'' AS documento, " +
	    "TO_CHAR(dm.fecha,'yyyy-mm-dd') " +
	    "||'' AS fechaelaboracion, " +
	    "TO_CHAR(rm.fecha,'yyyy-mm-dd') " +
	    "||'' AS fechaatencion, " +
	    "rm.hora " +
	    "|| '' AS horaatencion, " +
	    "dm.farmacia " +
	    "||'' AS almacen, " +
	    "''   AS almacendestino, " +
	    "drm.articulo " +
	    "||''                  AS articulo, " +
	    "drm.lote              AS lote, " +
	    "drm.fecha_vencimiento AS fechavencimiento, " +
	    "drm.costo_unitario " +
	    "||'' AS costounitario, " +
	    "drm.cantidadrecibida " +
	    "||'' AS cantidadentrada, " +
	    "(drm.cantidadrecibida*drm.costo_unitario) " +
	    "||'' AS valorentrada, " +
	    "0 " +
	    "||'' AS cantidadsalida, " +
	    "0 " +
	    "||'' AS valorsalida, " +
	    "dm.usuario " +
	    "||''                   AS usuariomovimiento, " +
	    "''                     AS tercero, " +
	    "drm.proveedor_compra   AS proveedorcompra, " +
	    "drm.proveedor_catalogo AS proveedorcatalogo, " +
	    "(SELECT tipos_conceptos_inv " +
	    "FROM inventarios.tipos_trans_inventarios " +
	    "WHERE consecutivo= " +
	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " +
	      "FROM administracion.valores_por_defecto " +
	      "WHERE parametro ='codigo_trans_devolucion_paciente' " +
	      ") " +
	    ") " +
	    "||'' AS tipoconceptoinv " +
	  "FROM inventarios.devolucion_med dm " +
	  "INNER JOIN inventarios.recepciones_medicamentos rm " +
	  "ON (dm.codigo=rm.devolucion) " +
	  "INNER JOIN inventarios.detalle_recep_medicamentos drm " +
	  "ON(rm.devolucion=drm.numerodevolucion) " +
	  "WHERE estado=2 " +
		"AND to_date(rm.fecha) between to_date('"+fechaInicial+"','DD/MM/YY') AND to_date('"+fechaFinal+"','DD/MM/YY')  " +
	"UNION " + 
		"SELECT p.codigo " +
	    "||'' AS codigomovimiento, " +
	    "(SELECT codigo " +
	    "FROM inventarios.tipos_trans_inventarios " +
	    "WHERE consecutivo= " +
	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " +
	      "FROM administracion.valores_por_defecto " +
	      "WHERE parametro ='codigo_transaccion_pedidos' " +
	      ") " +
	    ") " +
	    "||'' AS codigotransaccion, " +
	    "(SELECT descripcion " +
	    "FROM inventarios.tipos_trans_inventarios " +
	    "WHERE consecutivo= " +
	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " +
	      "FROM administracion.valores_por_defecto " +
	      "WHERE parametro ='codigo_transaccion_pedidos' " +
	      ") " +
	    ") " +
	    "||'' AS desctransaccion, " +
	    "p.codigo " +
	    "||'' AS documento, " +
	    "TO_CHAR(p.fecha,'yyyy-mm-dd') " +
	    "||'' AS fechaelaboracion, " +
	    "TO_CHAR(dp.fecha,'yyyy-mm-dd') " +
	    "||'' AS fechaatencion, " +
	    "dp.hora " +
	    "|| '' AS horaatencion, " +
	    "p.centro_costo_solicitado " +
	    "||'' AS almacen, " +
	    "p.centro_costo_solicitante " +
	    "||'' AS almacendestino, " +
	    "ddp.articulo " +
	    "||''                  AS articulo, " +
	    "ddp.lote              AS lote, " +
	    "ddp.fecha_vencimiento AS fechavencimiento, " +
	    "ddp.costo_unitario " +
	    "||'' AS costounitario, " +
	    "0 " +
	    "||'' AS cantidadentrada, " +
	    "0 " +
	    "||'' AS valorentrada, " +
	    "ddp.cantidad " +
	    "||'' AS cantidadsalida, " +
	    "COALESCE((ddp.cantidad*ddp.costo_unitario),0) " +
	    "||'' AS valorsalida, " +
	    "p.usuario " +
	    "||''                   AS usuariomovimiento, " +
	    "''                     AS tercero, " +
	    "ddp.proveedor_compra   AS proveedorcompra, " +
	    "ddp.proveedor_catalogo AS proveedorcatalogo, " +
	    "(SELECT tipos_conceptos_inv " +
	    "FROM inventarios.tipos_trans_inventarios " +
	    "WHERE consecutivo= " +
	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " +
	      "FROM administracion.valores_por_defecto " +
	      "WHERE parametro ='codigo_transaccion_pedidos' " +
	      ") " +
	    ") " +
	    "||'' AS tipoconceptoinv " +
	  "FROM inventarios.pedido p " +
	  "INNER JOIN inventarios.despacho_pedido dp " +
	  "ON(p.codigo=dp.pedido) " +
	  "INNER JOIN inventarios.detalle_despacho_pedido ddp " +
	  "ON(dp.pedido=ddp.pedido) " +
	  "WHERE p.estado=2 " +
		"AND to_date(dp.fecha) between to_date('"+fechaInicial+"','DD/MM/YY') AND to_date('"+fechaFinal+"','DD/MM/YY')  " +
		"UNION " + 
		"SELECT dp.codigo " + 
	    "||'' AS codigomovimiento, " + 
	    "(SELECT codigo " + 
	    "FROM inventarios.tipos_trans_inventarios " + 
	    "WHERE consecutivo= " + 
	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " + 
	      "FROM administracion.valores_por_defecto " + 
	      "WHERE parametro ='codigo_trans_devolucion_pedidos' " + 
	      ") " + 
	    ") " + 
	    "||'' AS codigotransaccion, " + 
	    "(SELECT descripcion " + 
	    "FROM inventarios.tipos_trans_inventarios " + 
	    "WHERE consecutivo= " + 
	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " + 
	      "FROM administracion.valores_por_defecto " + 
	      "WHERE parametro ='codigo_trans_devolucion_pedidos' " + 
	      ") " + 
	    ") " + 
	    "||'' AS desctransaccion, " + 
	    "dp.codigo " + 
	    "||'-' " + 
	    "||ddp.pedido " + 
	    "||'' AS documento, " + 
	    "TO_CHAR(dp.fecha,'yyyy-mm-dd') " + 
	    "||'' AS fechaelaboracion, " + 
	    "TO_CHAR(rp.fecha,'yyyy-mm-dd') " + 
	    "||'' AS fechaatencion, " + 
	    "rp.hora " + 
	    "|| '' AS horaatencion, " + 
	    "p.centro_costo_solicitado " + 
	    "||'' AS almacen, " + 
	    "p.centro_costo_solicitante " + 
	    "||'' AS almacendestino, " + 
	    "ddp.articulo " + 
	    "||''                  AS articulo, " + 
	    "drp.lote              AS lote, " + 
	    "drp.fecha_vencimiento AS fechavencimiento, " + 
	    "drp.costo_unitario " + 
	    "||'' AS costounitario, " + 
	    "drp.cantidadrecibida " + 
	    "||'' AS cantidadentrada, " + 
	    "(drp.cantidadrecibida*drp.costo_unitario) " + 
	    "||'' AS valorentrada, " + 
	    "0 " + 
	    "||'' AS cantidadsalida, " + 
	    "0 " + 
	    "||'' AS valorsalida, " + 
	    "dp.usuario " + 
	    "||''                   AS usuariomovimiento, " + 
	    "''                     AS tercero, " + 
	    "drp.proveedor_compra   AS proveedorcompra, " + 
	    "drp.proveedor_catalogo AS proveedorcatalogo, " + 
	    "(SELECT tipos_conceptos_inv " + 
	    "FROM inventarios.tipos_trans_inventarios " + 
	    "WHERE consecutivo= " + 
	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " + 
	      "FROM administracion.valores_por_defecto " + 
	      "WHERE parametro ='codigo_trans_devolucion_pedidos' " + 
	      ") " + 
	    ") " + 
	    "||'' AS tipoconceptoinv " + 
	  "FROM inventarios.devolucion_pedidos dp " + 
	  "INNER JOIN inventarios.recepciones_pedidos rp " + 
	  "ON(dp.codigo=rp.devolucion) " + 
	  "INNER JOIN inventarios.detalle_devol_pedido ddp " + 
	  "ON(dp.codigo=ddp.devolucion) " + 
	  "INNER JOIN inventarios.pedido p " + 
	  "ON (ddp.pedido=p.codigo) " + 
	  "INNER JOIN inventarios.detalle_recep_pedidos drp " + 
	  "ON(drp.codigo=ddp.codigo) " + 
	  "WHERE dp.estado=2 " + 
	  "AND to_date(rp.fecha) between to_date('"+fechaInicial+"','DD/MM/YY') AND to_date('"+fechaFinal+"','DD/MM/YY')  " +
	"UNION     " + 
		"SELECT s.numero_solicitud " + 
	    "||''                       AS codigomovimiento, " + 
	    "''                         AS codigotransaccion, " + 
	    "'ANULACION CARGO ARTICULO' AS desctransaccion, " + 
	    "acf.numero_solicitud " + 
	    "||'' AS documento, " + 
	    "TO_CHAR(acf.fecha_modifica,'yyyy-mm-dd') " + 
	    "||'' AS fechaelaboracion, " + 
	    "TO_CHAR(acf.fecha_modifica,'yyyy-mm-dd') " + 
	    "||'' AS fechaatencion, " + 
	    "acf.hora_modificar " + 
	    "||'' AS horaatencion, " + 
	    "s.centro_costo_solicitado " + 
	    "||'' AS almacen, " + 
	    "s.centro_costo_solicitante " + 
	    "||'' AS almacendestino, " + 
	    "dd.articulo " + 
	    "||''                 AS articulo, " + 
	    "dd.lote              AS lote, " + 
	    "dd.fecha_vencimiento AS fechavencimiento, " + 
	    "dd.costo_unitario " + 
	    "||'' AS costounitario, " + 
	    "dd.cantidad " + 
	    "||'' AS cantidadentrada, " + 
	    "(dd.cantidad*dd.costo_unitario) " + 
	    "||''AS valorentrada, " + 
	    "0 " + 
	    "||'' AS cantidadsalida, " + 
	    "0 " + 
	    "||'' AS valorsalida, " + 
	    "d.usuario " + 
	    "||''                  AS usuariomovimiento, " + 
	    "''                    AS tercero, " + 
	    "dd.proveedor_compra   AS proveedorcompra, " + 
	    "dd.proveedor_catalogo AS proveedorcatalogo, " + 
	    "(SELECT tipos_conceptos_inv " + 
	    "FROM inventarios.tipos_trans_inventarios " + 
	    "WHERE consecutivo= " + 
	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " + 
	      "FROM administracion.valores_por_defecto " + 
	      "WHERE parametro ='codigo_trans_soli_pacientes' " + 
	      ") " + 
	    ") " + 
	    "||'' AS tipoconceptoinv " + 
	  "FROM ordenes.solicitudes s " + 
	  "INNER JOIN facturacion.anulacion_cargos_farmacia acf " + 
	  "ON(s.numero_solicitud=acf.numero_solicitud) " + 
	  "INNER JOIN inventarios.despacho d " + 
	  "ON(s.numero_solicitud=d.numero_solicitud) " + 
	  "INNER JOIN inventarios.detalle_despachos dd " + 
	  "ON(d.orden=dd.despacho) " + 
	  "WHERE to_date(acf.fecha_modifica) between to_date('"+fechaInicial+"','DD/MM/YY') AND to_date('"+fechaFinal+"','DD/MM/YY')  " +
        		") t   " + 
				"INNER JOIN centros_costo cc on (t.almacen = cc.codigo) " + 
        		"WHERE cc.institucion = " +institucion+ " " +
				"GROUP BY t.almacen,t.articulo,t.lote,t.fechavencimiento,cc.codigo,cc.institucion "; 
    			
		return SqlBaseCierresInventarioDao.cargarMovimientosInventarios(con,strConsultaMovimientosInventarios);
	}
	
	/**
	 * Metodo que genera el cierre dado un mapa con el encabezado del cierre
	 * y un mapa con el detalle de los movimientos.
	 * @param con
	 * @param encabezadoCirre
	 * @param movimientos
	 * @return
	 */
	public boolean generarCierre(Connection con, HashMap encabezadoCirre, HashMap movimientos)
	{
		String codigo="select seq_cierre_inventarios.nextval  as codigo from dual";
		return SqlBaseCierresInventarioDao.generarCierre(con,encabezadoCirre,movimientos,codigo);
	}
	

	/**
	 * 
	 * @param con
	 * @param codigoCierre
	 * @return
	 */
	public int eliminarCierreInventarios(Connection con, String codigoCierre)
	{
		return SqlBaseCierresInventarioDao.eliminarCierreInventarios(con,codigoCierre);
	}
}
