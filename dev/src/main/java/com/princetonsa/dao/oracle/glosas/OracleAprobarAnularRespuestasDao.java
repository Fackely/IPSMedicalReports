package com.princetonsa.dao.oracle.glosas;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.glosas.AprobarAnularRespuestasDao;
import com.princetonsa.dao.sqlbase.glosas.SqlBaseAprobarAnularRespuestasDao;

public class OracleAprobarAnularRespuestasDao implements AprobarAnularRespuestasDao
{
	/**
	 * Metodo que actualiza la Glosa y la Respuesta segun estado
	 * @param con
	 * @param criterios
	 * @return
	 */
	public boolean guardar(Connection con, HashMap criterios)
	{
		
		return SqlBaseAprobarAnularRespuestasDao.guardar(con, criterios);
	}
	
	/**
	 * Metodo que consulta todas las facturas de la Glosa
	 * @param con
	 * @param codglosa
	 * @return
	 */
	public HashMap consultaNumFacturasGlosa(Connection con, String codglosa)
	{
		return SqlBaseAprobarAnularRespuestasDao.consultaNumFacturasGlosa(con, codglosa);
	}
	
	/**
	 * Metodo que valida si las faturas de la Glosa corresponden tambien a la Respuesta
	 * @param con
	 * @param codrespuesta
	 * @param codfactura
	 */
	public HashMap validarRespuesta(Connection con, String codrespuesta)
	{
		return SqlBaseAprobarAnularRespuestasDao.validarRespuesta(con, codrespuesta);
	}
	
	/**
	 * Metodo que consulta el campo conciliacion de la Respuesta
	 * @param con
	 * @param codrespuesta
	 * @return
	 */
	public String consultarConciliar(Connection con, String codrespuesta)
	{
		return SqlBaseAprobarAnularRespuestasDao.consultarConciliar(con, codrespuesta);
	}
	
	/**
	 * Metodo que actualiza el estado de la Glosa
	 */
	public boolean guardarEstadoGlosa(Connection con, String codglosa, String conciliar, String estadoResp)
	{
		return SqlBaseAprobarAnularRespuestasDao.guardarEstadoGlosa(con, codglosa, conciliar, estadoResp);
	}
	
	/**
	 * Metodo que consulta las Facturas asociadas a una Glosa
	 * @param con
	 * @param codGlosa
	 * @return
	 */
	public HashMap consultaFacturasGlosa(Connection con, String codGlosa)
	{
		return SqlBaseAprobarAnularRespuestasDao.consultaFacturasGlosa(con, codGlosa);
	}
	
	/**
	 * Metodo que consulta todos los det_audi_glosas por el codigo de la auditoria
	 * @param con
	 * @param codGlosa
	 * @return
	 */
	public HashMap consultaDetalleFacturaGlosa(Connection con, String codAudi)
	{
		return SqlBaseAprobarAnularRespuestasDao.consultaDetalleFacturaGlosa(con, codAudi);
	}
	
	/**
	 * Metodo que consulta los conceptos por detalle factura glosa
	 * @param con
	 * @param detAudiFact
	 * @return
	 */
	public int consultaConceptosDetalleFacturaGlosa(Connection con, String detAudiFact)
	{
		return SqlBaseAprobarAnularRespuestasDao.consultaConceptosDetalleFacturaGlosa(con, detAudiFact);
	}
	
	/**
	 * Cadena que consulta los asocios por solicitud detalle factura glosa
	 * @param con
	 * @param detAudiFact
	 * @return
	 */
	public HashMap consultaAsociosSolicitudDetalleFacturaGlosa(Connection con, String detAudiFact)
	{
		return SqlBaseAprobarAnularRespuestasDao.consultaAsociosSolicitudDetalleFacturaGlosa(con, detAudiFact);
	}
	
	/**
	 * Cadena que consulta los conceptos por asocios solicitud detalle factura glosa
	 * @param con
	 * @param detAudiFact
	 * @return
	 */
	public int consultaConceptosAsociosSolicitudDetalleFacturaGlosa(Connection con, String codAsocio)
	{
		return SqlBaseAprobarAnularRespuestasDao.consultaConceptosAsociosSolicitudDetalleFacturaGlosa(con, codAsocio);
	}
	
	/**
	 * Metodo para consultar los conceptos del detalle de las facturas de la respuesta de glosa
	 * @param con
	 * @param detAudiFact
	 * @return
	 */
	public int consultarConceptosDetFacRespGlosa(Connection con, String detAudiFact)
	{
		return SqlBaseAprobarAnularRespuestasDao.consultarConceptosDetFacRespGlosa(con, detAudiFact);
	}
	
	/**
	 * Metodopara consultar los conceptos de asocios de la respuesta de glosa
	 * @param con
	 * @param codasocio
	 * @return
	 */
	public int consultarConceptosAsociosRespGlosa(Connection con, String codasocio)
	{
		return SqlBaseAprobarAnularRespuestasDao.consultarConceptosAsociosRespGlosa(con, codasocio);
	}
	
	/**
	 * Metodo para consultar el estado de los ajustes de facturas externas
	 * @param con
	 * @param codFactRespGlosa
	 * @return
	 */
	public HashMap consultarEstadoAjustesFacExterna(Connection con, String codFactRespGlosa)
	{
		return SqlBaseAprobarAnularRespuestasDao.consultarEstadoAjustesFacExterna(con, codFactRespGlosa);
	}
	
	/**
	 * Metodo para consultar el estado de los ajustes de solicitudes de facturas internas
	 * @param con
	 * @param codFactRespGlosa
	 * @return
	 */
	public HashMap consultarEstadoAjustesDetFacInterna(Connection con, String codFactRespGlosa)
	{
		return SqlBaseAprobarAnularRespuestasDao.consultarEstadoAjustesDetFacInterna(con, codFactRespGlosa);
	}
	
	/**
	 * Metodo para consultar el estado de los ajustes de asocios de facturas internas
	 * @param con
	 * @param codDetFactRespGlosa
	 * @return
	 */
	public HashMap consultarEstadoAjustesAsoFacInterna(Connection con, String codDetFactRespGlosa)	
	{
		return SqlBaseAprobarAnularRespuestasDao.consultarEstadoAjustesAsoFacInterna(con, codDetFactRespGlosa);
	}
}