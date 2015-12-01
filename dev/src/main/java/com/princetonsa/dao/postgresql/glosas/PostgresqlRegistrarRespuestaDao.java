package com.princetonsa.dao.postgresql.glosas;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.glosas.RegistrarRespuestaDao;
import com.princetonsa.dao.sqlbase.glosas.SqlBaseRegistrarRespuestaDao;

public class PostgresqlRegistrarRespuestaDao implements RegistrarRespuestaDao
{
	/**
	 * Metodo que consulta el encabezado de la glosa
	 */
	public HashMap consultaEncabezadoGlosa(Connection con, String glosaSistema, String llamado)
	{
		return SqlBaseRegistrarRespuestaDao.consultaEncabezadoGlosa(con, glosaSistema, llamado);
	}
	
	/**
	 * Metodo que consulta la respuesta de una glosa si la tiene con detalle facturas
	 */
	public HashMap consultaRespuestaDetalleGlosa(Connection con, String glosaSistema)
	{
		return SqlBaseRegistrarRespuestaDao.consultaRespuestaDetalleGlosa(con, glosaSistema);
	}
	
	/**
	 * Metodo que consulta todas las facturas por respuesta glosa
	 */
	public HashMap consultaFacturasRespuestaGlosa(Connection con, String respuesta)
	{
		return SqlBaseRegistrarRespuestaDao.consultaFacturasRespuestaGlosa(con, respuesta);
	}
	
	/**
	 * Metodo que consulta las facturas asociadas a una glosa
	 */
	public HashMap consultaFacturasPorGlosa(Connection con, String codGlosa, String factura, String fecha)
	{
		return SqlBaseRegistrarRespuestaDao.consultaFacturasPorGlosa(con, codGlosa, factura, fecha);
	}
	
	/**
	 * Metodo que inserta un encabezado respuesta
	 */
	public int insertarEncabezadoRespuesta(Connection con, HashMap criterios)
	{
		return SqlBaseRegistrarRespuestaDao.insertarEncabezadoRespuesta(con, criterios);
	}
	
	/**
	 * Metodo que actualiza los datos basicos de un encabezado respuesta
	 */
	public boolean actualizarEncabezadoRespuesta(Connection con, HashMap criterios)
	{
		return SqlBaseRegistrarRespuestaDao.actualizarEncabezadoRespuesta(con, criterios);
	}
	
	/**
	 * Metodo que inserta cada detalle Factura de una respuesta
	 */
	public int insertarDetalleRespuesta(Connection con, HashMap criterios)
	{
		return SqlBaseRegistrarRespuestaDao.insertarDetalleRespuesta(con, criterios);
	}
	
	/**
	 * Metodo que elimina todos los asocios de respuesta glosa
	 * @param con
	 * @param codigoDetalleFactura
	 * @return
	 */
	public boolean eliminarAsociosRespuesta(Connection con, String codigoDetalleFactura)
	{
		return SqlBaseRegistrarRespuestaDao.eliminarAsociosRespuesta(con, codigoDetalleFactura);
	}
	
	/**
	 * Metodo que elimina todos los detalles de factura Respuesta
	 * @param con
	 * @param facturaRespuesta
	 * @return
	 */
	public boolean eliminarDetalleFacturaRespuesta(Connection con, String facturaRespuesta)
	{
		return SqlBaseRegistrarRespuestaDao.eliminarDetalleFacturaRespuesta(con, facturaRespuesta);
	}
	
	/**
	 * Metodo que elimina todos los detalles de facturas Externas Respuesta
	 * @param con
	 * @param facturaRespuesta
	 * @return
	 */
	public boolean eliminarDetalleFacturaExtRespuesta(Connection con, String facturaRespuesta)
	{
		return SqlBaseRegistrarRespuestaDao.eliminarDetalleFacturaExtRespuesta(con, facturaRespuesta);
	}
	
	/**
	 * Metodo que elimina una factura Respuesta
	 * @param con
	 * @param codrespuesta
	 * @param codfactura
	 * @return
	 */
	public boolean eliminarFacturaRespuesta(Connection con, String codfacresp)
	{
		return SqlBaseRegistrarRespuestaDao.eliminarFacturaRespuesta(con, codfacresp);
	}
	
	/**
	 * Metodo que consulta el detalle factura respuesta
	 */
	public HashMap consultaDetalleFacturaRespuesta(Connection con, String codfacresp)
	{
		return SqlBaseRegistrarRespuestaDao.consultaDetalleFacturaRespuesta(con, codfacresp);
	}
	
	/**
	 * Metodo que consulta todas las solicitudes servicio/articulo asociadas a una factura glosa
	 */
	public HashMap consultaSolicitudesDetalleFactura(Connection con, String codAudi)
	{
		return SqlBaseRegistrarRespuestaDao.consultaSolicitudesDetalleFactura(con, codAudi);
	}
	
	/**
	 * Metodo que consulta los conceptos respuesta por tipo concepto
	 */
	public HashMap consultaConceptosRespuesta(Connection con, String tipoConcepto)
	{
		return SqlBaseRegistrarRespuestaDao.consultaConceptosRespuesta(con, tipoConcepto);
	}
	
	/**
	 * Metodo que consulta el detalle de una factura externa
	 */
	public HashMap consultaDetalleFacturasExternas(Connection con, String codAudi)
	{
		return SqlBaseRegistrarRespuestaDao.consultaDetalleFacturasExternas(con, codAudi);
	}
	
	/**
	 * Metodo que consulta todos los asocios de todas las solicitudes de una factura glosa
	 */
	public HashMap consultaAsociosDetalleFactura(Connection con, String codAudi)
	{
		return SqlBaseRegistrarRespuestaDao.consultaAsociosDetalleFactura(con, codAudi);
	}
	
	/**
	 * Metodo que inserta un detalle Factura Respuesta
	 * @param con
	 * @param criterios
	 * @return
	 */
	public int insertarDetalleFacturaRespuesta(Connection con, HashMap criterios)
	{
		return SqlBaseRegistrarRespuestaDao.insertarDetalleFacturaRespuesta(con, criterios);
	}
	
	/**
	 * Metodo que inserta un Asocio detalle Factura Respuesta
	 * @param con
	 * @param criterios
	 * @return
	 */
	public int insertarAsocioDetalleFacturaRespuesta(Connection con, HashMap criterios)
	{
		return SqlBaseRegistrarRespuestaDao.insertarAsocioDetalleFacturaRespuesta(con, criterios);
	}
	
	/**
	 * Metodo que actualiza un detalle factura respuesta por concepto
	 * @param con
	 * @param criterios
	 * @return
	 */
	public boolean actualizarDetalleFacturaRespuesta(Connection con, HashMap criterios)
	{
		return SqlBaseRegistrarRespuestaDao.actualizarDetalleFacturaRespuesta(con, criterios);
	}
	
	/**
	 * Metodo que actualiza un asocio detalle factura respuesta por concepto
	 * @param con
	 * @param criterios
	 * @return
	 */
	public boolean actualizarAsocioDetalleFacturaRespuesta(Connection con, HashMap criterios)
	{
		return SqlBaseRegistrarRespuestaDao.actualizarAsocioDetalleFacturaRespuesta(con, criterios);
	}
	
	/**
	 * Metodo que inserta un detalle de factura externa respuesta
	 */
	public int insertarDetalleFacturaExternaRespuesta(Connection con, HashMap criterios)
	{
		return SqlBaseRegistrarRespuestaDao.insertarDetalleFacturaExternaRespuesta(con, criterios);
	}
	
	/**
	 * Metodo que actualiza un detalle factura externa respuesta
	 */
	public boolean actualizarDetalleFacturaExternaRespuesta(Connection con, HashMap criterios)
	{
		return SqlBaseRegistrarRespuestaDao.actualizarDetalleFacturaExternaRespuesta(con, criterios);
	}

	@Override
	public boolean actualizarValoresFacturas(Connection con, int numFactura,double valorAjuste, int institucion, int tipoAjuste) {
		return SqlBaseRegistrarRespuestaDao.actualizarValoresFacturas(con,numFactura,valorAjuste,institucion,tipoAjuste);
	}
}