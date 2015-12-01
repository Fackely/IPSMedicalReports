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
package com.princetonsa.dao.postgresql.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import util.UtilidadFecha;

import com.princetonsa.dao.inventarios.CierresInventarioDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseCierresInventarioDao;

/**
 * @version 1.0, 19/12/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class PostgresqlCierresInventarioDao implements CierresInventarioDao
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
    			"SELECT txa.codigo::text || ''::text AS codigomovimiento, tti.codigo::text || ''::text AS codigotransaccion, tti.descripcion::text || ''::text AS desctransaccion, txa.consecutivo::text || ''::text AS documento, txa.fecha_elaboracion || ''::text AS fechaelaboracion, txa.fecha_cierre || ''::text AS fechaatencion, txa.hora_cierre::text || ''::text AS horaatencion, txa.almacen || ''::text AS almacen, ''::text AS almacendestino, dtxa.articulo || ''::text AS articulo, dtxa.lote, dtxa.fecha_vencimiento AS fechavencimiento, dtxa.val_unitario || ''::text AS costounitario,  " + 
    			"CASE " + 
    			"WHEN tti.tipos_conceptos_inv = 1 THEN dtxa.cantidad " + 
    			"ELSE 0 " + 
    			"END || ''::text AS cantidadentrada, " + 
    			"CASE " + 
    			"WHEN tti.tipos_conceptos_inv = 1 THEN dtxa.cantidad::numeric * dtxa.val_unitario " + 
    			"ELSE 0::numeric " + 
    			"END || ''::text AS valorentrada, ( " + 
    			"CASE " + 
    			"WHEN tti.tipos_conceptos_inv = 2 THEN dtxa.cantidad " + 
    			"ELSE 0 " + 
    			"END || ''::text) || ''::text AS cantidadsalida,  " + 
    			"CASE " + 
    			"WHEN tti.tipos_conceptos_inv = 2 THEN dtxa.cantidad::numeric * dtxa.val_unitario " + 
    			"ELSE 0::numeric " + 
    			"END || ''::text AS valorsalida, txa.usuario::text || ''::text AS usuariomovimiento, txa.entidad || ''::text AS tercero, dtxa.proveedor_compra AS proveedorcompra, dtxa.proveedor_catalogo AS proveedorcatalogo, tti.tipos_conceptos_inv || ''::text AS tipoconceptoinv " + 
    			"FROM transacciones_x_almacen txa " + 
    			"JOIN det_trans_x_almacen dtxa ON dtxa.transaccion::text = txa.codigo::text " + 
    			"JOIN tipos_trans_inventarios tti ON txa.transaccion = tti.consecutivo " + 
    			"WHERE txa.estado = 2 " + 
    			"AND txa.fecha_cierre between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"'   " +
				"UNION  " + 
					"SELECT sta.numero_traslado || ''::text AS codigomovimiento, ((( SELECT tipos_trans_inventarios.codigo " + 
					"FROM tipos_trans_inventarios " + 
					"WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
					"CASE " + 
					"WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
					"ELSE valores_por_defecto.valor::integer " + 
					"END AS valor " + 
					"FROM valores_por_defecto " + 
					"WHERE valores_por_defecto.parametro::text = 'codigo_trans_traslado_almacen'::text ))))::text) || ''::text AS codigotransaccion, ((( SELECT tipos_trans_inventarios.descripcion " + 
					"FROM tipos_trans_inventarios " + 
					"WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
					"CASE " + 
					"WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
					"ELSE valores_por_defecto.valor::integer " + 
					"END AS valor " + 
					"FROM valores_por_defecto " + 
					"WHERE valores_por_defecto.parametro::text = 'codigo_trans_traslado_almacen'::text ))))::text) || ''::text AS desctransaccion, sta.numero_traslado || ''::text AS documento, sta.fecha_elaboracion || ''::text AS fechaelaboracion, dta.fecha_despacho || ''::text AS fechaatencion, dta.hora_despacho::text || ''::text AS horaatencion, sta.almacen_solicita || ''::text AS almacen, sta.almacen_solicitado || ''::text AS almacendestino, ddta.articulo || ''::text AS articulo, ddta.lote, ddta.fecha_vencimiento AS fechavencimiento, ddta.valor_unitario || ''::text AS costounitario, ddta.cantidad || ''::text AS cantidadentrada, (ddta.cantidad::numeric * ddta.valor_unitario) || ''::text AS valorentrada, 0 || ''::text AS cantidadsalida, 0 || ''::text AS valorsalida, sta.usuario_elabora::text || ''::text AS usuariomovimiento, ''::text AS tercero, NULL::character varying AS proveedorcompra, ddta.proveedor AS proveedorcatalogo, (( SELECT tipos_trans_inventarios.tipos_conceptos_inv " + 
					"FROM tipos_trans_inventarios " + 
					"WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
					"CASE " + 
					"WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
					"ELSE valores_por_defecto.valor::integer " + 
					"END AS valor " + 
					"FROM valores_por_defecto " + 
					"WHERE valores_por_defecto.parametro::text = 'codigo_trans_traslado_almacen'::text )))) || ''::text AS tipoconceptoinv " + 
					"FROM solicitud_traslado_almacen sta " + 
					"JOIN despacho_traslado_almacen dta ON sta.numero_traslado = dta.numero_traslado " + 
					"JOIN det_des_traslado_almacen ddta ON ddta.numero_traslado = dta.numero_traslado " + 
                "WHERE sta.estado = 4 " + 
                "AND dta.fecha_despacho between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"'   " +
				"UNION " + 
				"SELECT sta.numero_traslado || ''::text AS codigomovimiento, ((( SELECT tipos_trans_inventarios.codigo " + 
				"FROM tipos_trans_inventarios " + 
				"WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
				"CASE " + 
				"WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
				"ELSE valores_por_defecto.valor::integer " + 
				"END AS valor " + 
				"FROM valores_por_defecto " + 
				"WHERE valores_por_defecto.parametro::text = 'codigo_trans_traslado_almacen'::text ))))::text) || ''::text AS codigotransaccion, ((( SELECT tipos_trans_inventarios.descripcion " +
				"FROM tipos_trans_inventarios " + 
				"WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
				"CASE " + 
				"WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
				"ELSE valores_por_defecto.valor::integer " + 
				"END AS valor " + 
				"FROM valores_por_defecto " + 
				"WHERE valores_por_defecto.parametro::text = 'codigo_trans_traslado_almacen'::text ))))::text) || ''::text AS desctransaccion, sta.numero_traslado || ''::text AS documento, sta.fecha_elaboracion || ''::text AS fechaelaboracion, dta.fecha_despacho || ''::text AS fechaatencion, dta.hora_despacho::text || ''::text AS horaatencion, sta.almacen_solicitado || ''::text AS almacen, sta.almacen_solicita || ''::text AS almacendestino, ddta.articulo || ''::text AS articulo, ddta.lote, ddta.fecha_vencimiento AS fechavencimiento, ddta.valor_unitario || ''::text AS costounitario, 0 || ''::text AS cantidadentrada, 0 || ''::text AS valorentrada, ddta.cantidad || ''::text AS cantidadsalida, (ddta.cantidad::numeric * ddta.valor_unitario) || ''::text AS valorsalida, dta.usuario_despacho::text || ''::text AS usuariomovimiento, ''::text AS tercero, NULL::character varying AS proveedorcompra, ddta.proveedor AS proveedorcatalogo, (( SELECT tipos_trans_inventarios.tipos_conceptos_inv " + 
				"FROM tipos_trans_inventarios " + 
				"WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
				"CASE " + 
				"WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
				"ELSE valores_por_defecto.valor::integer " + 
				"END AS valor " + 
				"FROM valores_por_defecto " + 
				"WHERE valores_por_defecto.parametro::text = 'codigo_trans_traslado_almacen'::text )))) || ''::text AS tipoconceptoinv " + 
				"FROM solicitud_traslado_almacen sta " + 
				"JOIN despacho_traslado_almacen dta ON sta.numero_traslado = dta.numero_traslado " + 
				"JOIN det_des_traslado_almacen ddta ON ddta.numero_traslado = dta.numero_traslado " + 
                "WHERE sta.estado = 4 " + 
                "AND dta.fecha_despacho between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"'   " +
				"UNION " + 
				"SELECT s.numero_solicitud || ''::text AS codigomovimiento, ((( SELECT tipos_trans_inventarios.codigo " + 
				"FROM tipos_trans_inventarios " + 
				"WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
				"CASE " +
				"WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1)" +
				"ELSE valores_por_defecto.valor::integer " + 
				"END AS valor " + 
				"FROM valores_por_defecto " + 
				"WHERE valores_por_defecto.parametro::text = 'codigo_trans_soli_pacientes'::text))))::text) || ''::text AS codigotransaccion, ((( SELECT tipos_trans_inventarios.descripcion " + 
				"FROM tipos_trans_inventarios " + 
				"WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
				"CASE " + 
				"WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
				"ELSE valores_por_defecto.valor::integer " + 
				"END AS valor " + 
				"FROM valores_por_defecto " + 
				"WHERE valores_por_defecto.parametro::text = 'codigo_trans_soli_pacientes'::text))))::text) || ''::text AS desctransaccion, ((s.consecutivo_ordenes_medicas || '-'::text) || d.orden) || ''::text AS documento, s.fecha_solicitud || ''::text AS fechaelaboracion, d.fecha || ''::text AS fechaatencion, d.hora || ''::text AS horaatencion, s.centro_costo_solicitado || ''::text AS almacen, s.centro_costo_solicitante || ''::text AS almacendestino, dd.articulo || ''::text AS articulo, dd.lote, dd.fecha_vencimiento AS fechavencimiento, dd.costo_unitario || ''::text AS costounitario, 0 || ''::text AS cantidadentrada, 0 || ''::text AS valorentrada, dd.cantidad || ''::text AS cantidadsalida, (dd.cantidad::numeric * dd.costo_unitario) || ''::text AS valorsalida, d.usuario::text || ''::text AS usuariomovimiento, ''::text AS tercero, dd.proveedor_compra AS proveedorcompra, dd.proveedor_catalogo AS proveedorcatalogo, (( SELECT tipos_trans_inventarios.tipos_conceptos_inv " + 
				"FROM tipos_trans_inventarios " + 
				"WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
				"CASE " + 
				"WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
				"ELSE valores_por_defecto.valor::integer " + 
				"END AS valor " + 
				"FROM valores_por_defecto " + 
				"WHERE valores_por_defecto.parametro::text = 'codigo_trans_soli_pacientes'::text )))) || ''::text AS tipoconceptoinv " + 
				"FROM solicitudes s " + 
				"JOIN cargos_directos cardir ON cardir.numero_solicitud = s.numero_solicitud " + 
				"JOIN despacho d ON s.numero_solicitud = d.numero_solicitud " + 
				"JOIN detalle_despachos dd ON d.orden = dd.despacho " + 
				"WHERE s.estado_historia_clinica = 7 AND cardir.afecta_inventarios::text = 'S'::text AND s.centro_costo_solicitado <> 11 " + 
				"AND d.fecha between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"'   "+ 
        		"UNION " + 
				"SELECT s.numero_solicitud || ''::text AS codigomovimiento, ((( SELECT tipos_trans_inventarios.codigo " + 
				"FROM tipos_trans_inventarios " + 
				"WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
				"CASE " + 
				"WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
				"ELSE valores_por_defecto.valor::integer " + 
				"END AS valor             " +
				"FROM valores_por_defecto " + 
				"WHERE valores_por_defecto.parametro::text = 'codigo_trans_soli_pacientes'::text ))))::text) || ''::text AS codigotransaccion, ((( SELECT tipos_trans_inventarios.descripcion " + 
				"FROM tipos_trans_inventarios " + 
				"WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
				"CASE " + 
				"WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
				"ELSE valores_por_defecto.valor::integer " +
				"END AS valor " + 
				"FROM valores_por_defecto " + 
				"WHERE valores_por_defecto.parametro::text = 'codigo_trans_soli_pacientes'::text ))))::text) || ''::text AS desctransaccion, ((s.consecutivo_ordenes_medicas || '-'::text) || d.orden) || ''::text AS documento, s.fecha_solicitud || ''::text AS fechaelaboracion, d.fecha || ''::text AS fechaatencion, d.hora || ''::text AS horaatencion, s.centro_costo_solicitado || ''::text AS almacen, s.centro_costo_solicitante || ''::text AS almacendestino, dd.articulo || ''::text AS articulo, dd.lote, dd.fecha_vencimiento AS fechavencimiento, dd.costo_unitario || ''::text AS costounitario, 0 || ''::text AS cantidadentrada, 0 || ''::text AS valorentrada, dd.cantidad || ''::text AS cantidadsalida, (dd.cantidad::numeric * dd.costo_unitario) || ''::text AS valorsalida, d.usuario::text || ''::text AS usuariomovimiento, ''::text AS tercero, dd.proveedor_compra AS proveedorcompra, dd.proveedor_catalogo AS proveedorcatalogo, (( SELECT tipos_trans_inventarios.tipos_conceptos_inv " + 
				"FROM tipos_trans_inventarios " + 
				"WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
				"CASE " + 
				"WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
				"ELSE valores_por_defecto.valor::integer " + 
				"END AS valor " + 
				"FROM valores_por_defecto " + 
				"WHERE valores_por_defecto.parametro::text = 'codigo_trans_soli_pacientes'::text )))) || ''::text AS tipoconceptoinv " + 
				"FROM solicitudes s " + 
				"JOIN despacho d ON s.numero_solicitud = d.numero_solicitud " + 
				"JOIN detalle_despachos dd ON d.orden = dd.despacho " + 
				"WHERE (s.estado_historia_clinica = ANY (ARRAY[1, 5, 6])) AND s.centro_costo_solicitado <> 11 " + 
				"AND d.fecha between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"'   " +
				"UNION " + 
				"SELECT dm.codigo || ''::text AS codigomovimiento, ((( SELECT tipos_trans_inventarios.codigo " + 
				"FROM tipos_trans_inventarios " + 
				"WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
				"CASE " + 
				"WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
				"ELSE valores_por_defecto.valor::integer " +
				"END AS valor " + 
				"FROM valores_por_defecto " + 
				"WHERE valores_por_defecto.parametro::text = 'codigo_trans_devolucion_paciente'::text ))))::text) || ''::text AS codigotransaccion, ((( SELECT tipos_trans_inventarios.descripcion " + 
				"FROM tipos_trans_inventarios " + 
				"WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
				"CASE " + 
				"WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
				"ELSE valores_por_defecto.valor::integer " + 
				"END AS valor " + 
				"FROM valores_por_defecto " + 
				"WHERE valores_por_defecto.parametro::text = 'codigo_trans_devolucion_paciente'::text ))))::text) || ''::text AS desctransaccion, dm.codigo || ''::text AS documento, dm.fecha || ''::text AS fechaelaboracion, rm.fecha || ''::text AS fechaatencion, rm.hora || ''::text AS horaatencion, dm.farmacia || ''::text AS almacen, ''::text AS almacendestino, drm.articulo || ''::text AS articulo, drm.lote, drm.fecha_vencimiento AS fechavencimiento, drm.costo_unitario || ''::text AS costounitario, drm.cantidadrecibida || ''::text AS cantidadentrada, (drm.cantidadrecibida::numeric * drm.costo_unitario) || ''::text AS valorentrada, 0 || ''::text AS cantidadsalida, 0 || ''::text AS valorsalida, dm.usuario::text || ''::text AS usuariomovimiento, ''::text AS tercero, drm.proveedor_compra AS proveedorcompra, drm.proveedor_catalogo AS proveedorcatalogo, (( SELECT tipos_trans_inventarios.tipos_conceptos_inv " + 
				"FROM tipos_trans_inventarios " + 
				"WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
				"CASE " + 
				"WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
				"ELSE valores_por_defecto.valor::integer " + 
				"END AS valor " + 
				"FROM valores_por_defecto " + 
				"WHERE valores_por_defecto.parametro::text = 'codigo_trans_devolucion_paciente'::text )))) || ''::text AS tipoconceptoinv " + 
				"FROM devolucion_med dm " + 
				"JOIN recepciones_medicamentos rm ON dm.codigo = rm.devolucion " + 
				"JOIN detalle_recep_medicamentos drm ON rm.devolucion = drm.numerodevolucion " + 
				"WHERE dm.estado = 2 " + 
				"AND rm.fecha between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"'   " +
				"UNION " + 
				"SELECT p.codigo || ''::text AS codigomovimiento, ((( SELECT tipos_trans_inventarios.codigo " + 
				"FROM tipos_trans_inventarios " + 
				"WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
				"CASE " + 
				"WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
				"ELSE valores_por_defecto.valor::integer " + 
				"END AS valor " + 
				"FROM valores_por_defecto " + 
				"WHERE valores_por_defecto.parametro::text = 'codigo_transaccion_pedidos'::text ))))::text) || ''::text AS codigotransaccion, ((( SELECT tipos_trans_inventarios.descripcion " + 
				"FROM tipos_trans_inventarios " + 
				"WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
				"CASE " + 
				"WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
				"ELSE valores_por_defecto.valor::integer " + 
				"END AS valor " + 
				"FROM valores_por_defecto " + 
				"WHERE valores_por_defecto.parametro::text = 'codigo_transaccion_pedidos'::text ))))::text) || ''::text AS desctransaccion, p.codigo || ''::text AS documento, p.fecha || ''::text AS fechaelaboracion, dp.fecha || ''::text AS fechaatencion, dp.hora || ''::text AS horaatencion, p.centro_costo_solicitado || ''::text AS almacen, p.centro_costo_solicitante || ''::text AS almacendestino, ddp.articulo || ''::text AS articulo, ddp.lote, ddp.fecha_vencimiento AS fechavencimiento, ddp.costo_unitario || ''::text AS costounitario, 0 || ''::text AS cantidadentrada, 0 || ''::text AS valorentrada, ddp.cantidad || ''::text AS cantidadsalida, (ddp.cantidad::double precision * ddp.costo_unitario) || ''::text AS valorsalida, p.usuario::text || ''::text AS usuariomovimiento, ''::text AS tercero, ddp.proveedor_compra AS proveedorcompra, ddp.proveedor_catalogo AS proveedorcatalogo, (( SELECT tipos_trans_inventarios.tipos_conceptos_inv " + 
				"FROM tipos_trans_inventarios " + 
				"WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
				"CASE " + 
				"WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
				"ELSE valores_por_defecto.valor::integer " + 
				"END AS valor " + 
				"FROM valores_por_defecto " + 
				"WHERE valores_por_defecto.parametro::text = 'codigo_transaccion_pedidos'::text )))) || ''::text AS tipoconceptoinv " + 
				"FROM pedido p " + 
				"JOIN despacho_pedido dp ON p.codigo = dp.pedido " + 
				"JOIN detalle_despacho_pedido ddp ON dp.pedido = ddp.pedido " + 
				"WHERE p.estado = 2 " + 
				"AND dp.fecha between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"'   " +
				"UNION " + 
				"SELECT dp.codigo || ''::text AS codigomovimiento, ((( SELECT tipos_trans_inventarios.codigo " + 
				"FROM tipos_trans_inventarios " + 
				"WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
				"CASE " + 
				"WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
				"ELSE valores_por_defecto.valor::integer " + 
				"END AS valor " + 
				"FROM valores_por_defecto " + 
				"WHERE valores_por_defecto.parametro::text = 'codigo_trans_devolucion_pedidos'::text ))))::text) || ''::text AS codigotransaccion, ((( SELECT tipos_trans_inventarios.descripcion " + 
				"FROM tipos_trans_inventarios " + 
				"WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
				"CASE " + 
				"WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
				"ELSE valores_por_defecto.valor::integer " + 
				"END AS valor " + 
				"FROM valores_por_defecto " + 
				"WHERE valores_por_defecto.parametro::text = 'codigo_trans_devolucion_pedidos'::text ))))::text) || ''::text AS desctransaccion, ((dp.codigo || '-'::text) || ddp.pedido) || ''::text AS documento, dp.fecha || ''::text AS fechaelaboracion, rp.fecha || ''::text AS fechaatencion, rp.hora || ''::text AS horaatencion, p.centro_costo_solicitado || ''::text AS almacen, p.centro_costo_solicitante || ''::text AS almacendestino, ddp.articulo || ''::text AS articulo, drp.lote, drp.fecha_vencimiento AS fechavencimiento, drp.costo_unitario || ''::text AS costounitario, drp.cantidadrecibida || ''::text AS cantidadentrada, (drp.cantidadrecibida::numeric * drp.costo_unitario) || ''::text AS valorentrada, 0 || ''::text AS cantidadsalida, 0 || ''::text AS valorsalida, dp.usuario::text || ''::text AS usuariomovimiento, ''::text AS tercero, drp.proveedor_compra AS proveedorcompra, drp.proveedor_catalogo AS proveedorcatalogo, (( SELECT tipos_trans_inventarios.tipos_conceptos_inv " + 
				"FROM tipos_trans_inventarios " + 
				"WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
				"CASE " + 
				"WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
				"ELSE valores_por_defecto.valor::integer " + 
				"END AS valor " + 
				"FROM valores_por_defecto " + 
				"WHERE valores_por_defecto.parametro::text = 'codigo_trans_devolucion_pedidos'::text )))) || ''::text AS tipoconceptoinv " + 
				"FROM devolucion_pedidos dp " + 
				"JOIN recepciones_pedidos rp ON dp.codigo = rp.devolucion " + 
				"JOIN detalle_devol_pedido ddp ON dp.codigo = ddp.devolucion " + 
				"JOIN pedido p ON ddp.pedido = p.codigo " + 
				"JOIN detalle_recep_pedidos drp ON drp.codigo = ddp.codigo " + 
				"WHERE dp.estado = 2 " + 
				"AND rp.fecha between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"'   " +
        		"UNION     " + 
				"SELECT s.numero_solicitud || ''::text AS codigomovimiento, ''::text AS codigotransaccion, 'ANULACION CARGO ARTICULO'::text AS desctransaccion, acf.numero_solicitud || ''::text AS documento, acf.fecha_modifica || ''::text AS fechaelaboracion, acf.fecha_modifica || ''::text AS fechaatencion, acf.hora_modificar::text || ''::text AS horaatencion, s.centro_costo_solicitado || ''::text AS almacen, s.centro_costo_solicitante || ''::text AS almacendestino, dd.articulo || ''::text AS articulo, dd.lote, dd.fecha_vencimiento AS fechavencimiento, dd.costo_unitario || ''::text AS costounitario, dd.cantidad || ''::text AS cantidadentrada, (dd.cantidad::numeric * dd.costo_unitario) || ''::text AS valorentrada, 0 || ''::text AS cantidadsalida, 0 || ''::text AS valorsalida, d.usuario::text || ''::text AS usuariomovimiento, ''::text AS tercero, dd.proveedor_compra AS proveedorcompra, dd.proveedor_catalogo AS proveedorcatalogo, (( SELECT tipos_trans_inventarios.tipos_conceptos_inv " + 
				"FROM tipos_trans_inventarios " + 
				"WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
				"CASE " + 
				"WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
				"ELSE valores_por_defecto.valor::integer " + 
				"END AS valor " + 
				"FROM valores_por_defecto " + 
				"WHERE valores_por_defecto.parametro::text = 'codigo_trans_soli_pacientes'::text )))) || ''::text AS tipoconceptoinv " + 
				"FROM solicitudes s " + 
				"JOIN anulacion_cargos_farmacia acf ON s.numero_solicitud = acf.numero_solicitud " + 
				"JOIN despacho d ON s.numero_solicitud = d.numero_solicitud " + 
				"JOIN detalle_despachos dd ON d.orden = dd.despacho " + 
				"WHERE acf.fecha_modifica between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"'   " +
        		") t   " + 
				"INNER JOIN centros_costo cc on (t.almacen::NUMERIC = cc.codigo) " + 
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
		String codigo="select nextval('seq_cierre_inventarios') as codigo";
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
