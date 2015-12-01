package com.princetonsa.dao.oracle.glosas;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.struts.action.ActionForward;

import com.princetonsa.dao.glosas.AprobarGlosasDao;
import com.princetonsa.dao.glosas.ConsultarImprimirGlosasDao;
import com.princetonsa.dao.sqlbase.glosas.SqlBaseAprobarGlosasDao;
import com.princetonsa.dao.sqlbase.glosas.SqlBaseConsultarImprimirGlosasDao;

public class OracleConsultarImprimirGlosasDao implements ConsultarImprimirGlosasDao
{
	/**
	 * Metodo para consultar la información de una glosa
	 * @param con
	 * @param glosa
	 * @return
	 */
	public HashMap consultarGlosa(Connection con, String glosa)
	{
		return SqlBaseConsultarImprimirGlosasDao.consultarGlosa(con, glosa);
	}
	
	/**
	 * Metodo para consultar la información de las glosas
	 * 
	 */
	public HashMap listarGlosas(Connection con,HashMap<String, Object> listadoGlosasMap){
		return SqlBaseConsultarImprimirGlosasDao.listarGlosas(con, listadoGlosasMap);
	}
	
	/**
	 * Metodo para consultar el detalle de las facturas de una glosa
	 * @param con
	 * @param glosa
	 * @return
	 */
	public HashMap consultarDetalleFacturasGlosa(Connection con, String glosa, int institucion)
	{
		return SqlBaseConsultarImprimirGlosasDao.consultarDetalleFacturasGlosa(con, glosa, institucion);
	}
	
	/**
	 * Metodo para consultar el detalle de las facturas de una glosa
	 * @param con
	 * @param glosa
	 * @return
	 */
	public HashMap consultarDetalleSolicitudesGlosa(Connection con, String codAuditoriaGlosa, int institucion)
	{
		return SqlBaseConsultarImprimirGlosasDao.consultarDetalleSolicitudesGlosa(con, codAuditoriaGlosa, institucion);
	}
}