package com.princetonsa.dao.glosas;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Diego Bedoya
 * Registro Auditoria (GLOSAS)
 */
public interface RegistrarRespuestaDao
{
	/**
	 * Metodo que consulta el encabezado de la glosa
	 * @param con
	 * @param glosaSistema
	 * @return
	 */
	public HashMap consultaEncabezadoGlosa(Connection con, String glosaSistema, String llamado);
	
	/**
	 * Metodo que consulta la respuesta de la glosa si la hay con detalle facturas
	 * @param con
	 * @param glosaSistema
	 * @return
	 */
	public HashMap consultaRespuestaDetalleGlosa(Connection con, String glosaSistema);
	
	/**
	 * Metodo que consulta todas las facturas asociadas a una respuesta glosa
	 * @param con
	 * @param respuesta
	 * @return
	 */
	public HashMap consultaFacturasRespuestaGlosa(Connection con, String respuesta);
	
	/**
	 * Metodo que consulta las facturas asociadas a una glosa
	 * @param con
	 * @param codGlosa
	 * @return
	 */
	public HashMap consultaFacturasPorGlosa(Connection con, String codGlosa, String factura, String fecha);
	
	/**
	 * Metodo que inserta un encabezado respuesta
	 * @param con
	 * @param criterios
	 * @return
	 */
	public int insertarEncabezadoRespuesta(Connection con, HashMap criterios);
	
	/**
	 * Metodo que actualiza los datos basicos de un encabezado respuesta
	 * @param con
	 * @param criterios
	 * @return
	 */
	public boolean actualizarEncabezadoRespuesta(Connection con, HashMap criterios);
	
	/**
	 * Metodo que inserta cada detalle Factura de una respuesta
	 * @param con
	 * @param criterios
	 * @return
	 */
	public int insertarDetalleRespuesta(Connection con, HashMap criterios);
	
	/**
	 * Metodo que elimina todos los asocios de respuesta glosa
	 * @param con
	 * @param codigoDetalleFactura
	 * @return
	 */
	public boolean eliminarAsociosRespuesta(Connection con, String codigoDetalleFactura);
	
	/**
	 * Metodo que elimina todos los detalles de factura Respuesta
	 * @param con
	 * @param facturaRespuesta
	 * @return
	 */
	public boolean eliminarDetalleFacturaRespuesta(Connection con, String facturaRespuesta);
	
	/**
	 * Metodo que elimina todos los detalles de facturas Externas Respuesta
	 * @param con
	 * @param facturaRespuesta
	 * @return
	 */
	public boolean eliminarDetalleFacturaExtRespuesta(Connection con, String facturaRespuesta);
	
	/**
	 * Metodo que elimina una factura Respuesta
	 * @param con
	 * @param codrespuesta
	 * @param codfactura
	 * @return
	 */
	public boolean eliminarFacturaRespuesta(Connection con, String codfacresp);
	
	/**
	 * Metodo que consulta el detalle factura respuesta por codigo factura respuesta
	 * @param con
	 * @param codfacresp
	 * @return
	 */
	public HashMap consultaDetalleFacturaRespuesta(Connection con, String codfacresp);
	
	/**
	 * Metodo que consulta todas las solcitudes servicio/articulo asociadas a una factura glosa
	 * @param con
	 * @param codAudi
	 * @return
	 */
	public HashMap consultaSolicitudesDetalleFactura(Connection con, String codAudi);
	
	/**
	 * Metodo que consulta los conceptos respuesta por tipo concepto
	 * @param co
	 * @param tipoConcepto
	 * @return
	 */
	public HashMap consultaConceptosRespuesta(Connection con, String tipoConcepto);
	
	/**
	 * Metodo que consulta el detalle de una factura externa
	 * @param con
	 * @param codAudi
	 * @return
	 */
	public HashMap consultaDetalleFacturasExternas(Connection con, String codAudi);
	
	/**
	 * Metodo que consulta todos los asocios de todas las solicitudes de una factura glosa
	 * @param con
	 * @param codAudi
	 * @return
	 */
	public HashMap consultaAsociosDetalleFactura(Connection con, String codAudi);
	
	/**
	 * Metodo que inserta un detalle Factura Respuesta
	 * @param con
	 * @param criterios
	 * @return
	 */
	public int insertarDetalleFacturaRespuesta(Connection con, HashMap criterios);
	
	/**
	 * Metodo que inserta un Asocio detalle Factura Respuesta
	 * @param con
	 * @param criterios
	 * @return
	 */
	public int insertarAsocioDetalleFacturaRespuesta(Connection con, HashMap criterios);
	
	/**
	 * Metodo que actualiza un detalle factura respuesta por concepto
	 * @param con
	 * @param criterios
	 * @return
	 */
	public boolean actualizarDetalleFacturaRespuesta(Connection con, HashMap criterios);
	
	/**
	 * Metodo que actualiza un asocio detalle factura respuesta por concepto
	 * @param con
	 * @param criterios
	 * @return
	 */
	public boolean actualizarAsocioDetalleFacturaRespuesta(Connection con, HashMap criterios);
	
	/**
	 * Metodo que inserta un detalle factura externa
	 * @param con
	 * @param criterios
	 * @return
	 */
	public int insertarDetalleFacturaExternaRespuesta(Connection con, HashMap criterios);
	
	/**
	 * Metodo que actualiza un detalle factura externa respuesta
	 * @param con
	 * @param criterios
	 * @return
	 */
	public boolean actualizarDetalleFacturaExternaRespuesta(Connection con, HashMap criterios);

	public boolean actualizarValoresFacturas(Connection con,int numFactura, double valorAjuste, int institucion,int tipoAjuste);
}