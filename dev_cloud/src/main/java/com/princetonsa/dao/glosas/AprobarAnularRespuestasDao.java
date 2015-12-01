package com.princetonsa.dao.glosas;

import java.sql.Connection;
import java.util.HashMap;

public interface AprobarAnularRespuestasDao
{
	/**
	 * Metodo que actualiza la Glosa y la Respuesta segun estado
	 * @param con
	 * @param criterios
	 * @return
	 */
	public boolean guardar(Connection con, HashMap criterios);

	/**
	 * Metodo que consulta todas las facturas de la Glosa
	 * @param con
	 * @param codglosa
	 * @return
	 */
	public HashMap consultaNumFacturasGlosa(Connection con, String codglosa);
	
	/**
	 * Metodo que valida si las faturas de la Glosa corresponden tambien a la Respuesta 
	 * @param con
	 * @param codrespuesta
	 * @param codfactura
	 * @return
	 */
	public HashMap validarRespuesta(Connection con, String codrespuesta);
	
	/**
	 * Metodo que consulta el campo conciliacion de la Respuesta
	 * @param con
	 * @param codrespuesta
	 * @return
	 */
	public String consultarConciliar(Connection con, String codrespuesta);
	
	/**
	 * Metodo que actualiza el estado de la Glosa
	 * @param con
	 * @param codglosa
	 * @param conciliar
	 * @param estadoResp
	 * @return
	 */
	public boolean guardarEstadoGlosa(Connection con, String codglosa, String conciliar, String estadoResp);
	
	/**
	 * Metodo que consulta las Facturas asociadas a una Glosa
	 * @param con
	 * @param codGlosa
	 * @return
	 */
	public HashMap consultaFacturasGlosa(Connection con, String codGlosa);
	
	/**
	 * Metodo que consulta todos los det_audi_glosas por el codigo de la auditoria
	 * @param con
	 * @param codGlosa
	 * @return
	 */
	public HashMap consultaDetalleFacturaGlosa(Connection con, String codAudi);
	
	/**
	 * Metodo que consulta los conceptos por detalle factura glosa
	 * @param con
	 * @param detAudiFact
	 * @return
	 */
	public int consultaConceptosDetalleFacturaGlosa(Connection con, String detAudiFact);
	
	/**
	 * Cadena que consulta los asocios por solicitud detalle factura glosa
	 * @param con
	 * @param detAudiFact
	 * @return
	 */
	public HashMap consultaAsociosSolicitudDetalleFacturaGlosa(Connection con, String detAudiFact);
	
	/**
	 * Cadena que consulta los conceptos por asocios solicitud detalle factura glosa
	 * @param con
	 * @param detAudiFact
	 * @return
	 */
	public int consultaConceptosAsociosSolicitudDetalleFacturaGlosa(Connection con, String codAsocio);
	
	/**
	 * Metodo para consultar los conceptos del detalle de las facturas de la respuesta de glosa
	 * @param con
	 * @param detAudiFact
	 * @return
	 */
	public int consultarConceptosDetFacRespGlosa(Connection con, String detAudiFact);
	
	/**
	 * Metodopara consultar los conceptos de asocios de la respuesta de glosa
	 * @param con
	 * @param codasocio
	 * @return
	 */
	public int consultarConceptosAsociosRespGlosa(Connection con, String codasocio);
	
	/**
	 * Metodo para consultar el estado de los ajustes de facturas externas
	 * @param con
	 * @param codFactRespGlosa
	 * @return
	 */
	public HashMap consultarEstadoAjustesFacExterna(Connection con, String codFactRespGlosa);
	
	/**
	 * Metodo para consultar el estado de los ajustes de solicitudes de facturas internas
	 * @param con
	 * @param codFactRespGlosa
	 * @return
	 */
	public HashMap consultarEstadoAjustesDetFacInterna(Connection con, String codFactRespGlosa);
	
	/**
	 * Metodo para consultar el estado de los ajustes de asocios de facturas internas
	 * @param con
	 * @param codDetFactRespGlosa
	 * @return
	 */
	public HashMap consultarEstadoAjustesAsoFacInterna(Connection con, String codDetFactRespGlosa);
}