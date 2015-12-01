package com.princetonsa.dao.glosas;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import com.princetonsa.dto.glosas.DtoGlosa;
import com.princetonsa.dto.glosas.DtoRespuestaFacturaGlosa;

public interface ConsultarImprimirRespuestasDao
{
	/**
	 * 
	 * @param con
	 * @param listadoGlosasMap
	 * @return
	 */
	public ArrayList<DtoGlosa> listarGlosas(Connection con,HashMap<String, Object> listadoGlosasMap);

	/**
	 * Método para consultar las respuestas de las facturas de una glosa
	 * @param con
	 * @param glosa
	 * @return
	 */
	public DtoGlosa consultarRespuestaFacturasGlosa(Connection con, DtoGlosa glosa);

	/**
	 * 
	 * @param con
	 * @param dtoRespuestaFactura
	 * @return
	 */
	public DtoRespuestaFacturaGlosa consultarRespuestaSolicitudesGlosa(Connection con, DtoRespuestaFacturaGlosa dtoRespuestaFactura, int institucion);
}