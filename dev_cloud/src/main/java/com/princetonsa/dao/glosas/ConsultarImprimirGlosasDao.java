package com.princetonsa.dao.glosas;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.struts.action.ActionForward;

public interface ConsultarImprimirGlosasDao
{
	/**
	 * Metodo para consultar la información de una glosa
	 * @param con
	 * @param glosa
	 * @return
	 */
	public HashMap consultarGlosa(Connection con, String glosa);

	/**
	 * 
	 * @param con
	 * @param listadoGlosasMap
	 * @return
	 */
	public HashMap listarGlosas(Connection con,HashMap<String, Object> listadoGlosasMap);
	
	
	/**
	 * Metodo para consultar el detalle de las facturas de una glosa
	 * @param con
	 * @param glosa
	 * @return
	 */
	public HashMap consultarDetalleFacturasGlosa(Connection con, String glosa, int institucion);
	
	/**
	 * Metodo para consultar el detalle de las facturas de una glosa
	 * @param con
	 * @param glosa
	 * @return
	 */
	public HashMap consultarDetalleSolicitudesGlosa(Connection con, String codAuditoriaGlosa, int institucion);
	
}