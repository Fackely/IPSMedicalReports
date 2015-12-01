package com.princetonsa.dao.oracle.glosas;

import java.sql.Connection;
import java.util.ArrayList;
import com.princetonsa.dao.glosas.UtilidadesGlosasDao;
import com.princetonsa.dao.sqlbase.glosas.SqlBaseUtilidadesGlosasDao;
import com.princetonsa.dto.glosas.DtoDetalleFacturaGlosa;
import com.princetonsa.dto.glosas.DtoGlosa;

/**
 * @author Gio
 */
public class OracleUtilidadesGlosasDao implements UtilidadesGlosasDao{
	
	/**
	 * Obtener las glosas de una factura
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public ArrayList<DtoGlosa> obtenerGlosasFactura(Connection con,String codigoFactura)
	{
		return SqlBaseUtilidadesGlosasDao.obtenerGlosasFactura(con, codigoFactura);
	}
	
	/**
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public ArrayList<DtoDetalleFacturaGlosa> obtenerDetalleGlosaFactura(Connection con, String codigoGlosa)
	{
		return SqlBaseUtilidadesGlosasDao.obtenerDetalleGlosaFactura(con, codigoGlosa);
	}
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public double obtenerTotalSoportadoRespuesta(Connection con, int informacionRespuesta, int codigoInstitucion)
	{
		return SqlBaseUtilidadesGlosasDao.obtenerTotalSoportadoRespuesta(con, informacionRespuesta, codigoInstitucion);
	}

	/**
	 * 
	 * @param con
	 * @return
	 */
	public double obtenerTotalAceptadoRespuesta(Connection con, int informacionRespuesta, int codigoInstitucion)
	{
		return SqlBaseUtilidadesGlosasDao.obtenerTotalAceptadoRespuesta(con, informacionRespuesta, codigoInstitucion);
	}
}
