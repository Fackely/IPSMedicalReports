package com.princetonsa.dao.glosas;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dto.glosas.DtoDetalleFacturaGlosa;
import com.princetonsa.dto.glosas.DtoGlosa;

/**
 * @author Gio
 */
public interface UtilidadesGlosasDao {

	/**
	 * Obtener las glosas de una factura
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public ArrayList<DtoGlosa> obtenerGlosasFactura(Connection con,String codigoFactura);

	/**
	 * @param con
	 * @param codigoGlosa
	 * @return
	 */
	public ArrayList<DtoDetalleFacturaGlosa> obtenerDetalleGlosaFactura(Connection con, String codigoGlosa);

	/**
	 * 
	 * @param con
	 * @param codigoInstitucion 
	 * @param informacionRespuesta 
	 * @return
	 */
	public double obtenerTotalSoportadoRespuesta(Connection con, int informacionRespuesta, int codigoInstitucion);

	/**
	 * 
	 * @param con
	 * @param codigoInstitucion 
	 * @param informacionRespuesta 
	 * @return
	 */
	public double obtenerTotalAceptadoRespuesta(Connection con, int informacionRespuesta, int codigoInstitucion);
	
}

	

