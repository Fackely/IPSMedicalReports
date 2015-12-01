package com.princetonsa.mundo.glosas;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.glosas.AprobarGlosasDao;

public class AprobarGlosas
{
	/**
	 * Para manejo de Logs
	 */
	private static Logger logger = Logger.getLogger(AprobarGlosas.class);
	
	private static AprobarGlosasDao getAprobarGlosasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAprobarGlosasDao();
	}
	
	/**
	 * Metodo para consultar los Convenios
	 * @param con
	 * @return
	 */
	public static HashMap consultarConvenios(Connection con)
	{
		return getAprobarGlosasDao().consultarConvenios(con);
	}
		
	/**
	 * Valida el detalle de las facturas y la suma del valor de la Glosa
	 * @param con
	 * @param codGlosa
	 * @param valor
	 * @return
	 */
	public boolean validarAprobarGlosa(Connection con, String codGlosa, String valor) 
	{
		return getAprobarGlosasDao().validarAprobarGlosa(con, codGlosa, valor);
	}

	/**
	 * Guardar Glosa
	 * @param con
	 * @param string
	 * @param loginUsuario
	 * @return
	 */
	public boolean guardar(Connection con, String codGlosa, String usuario) {
		HashMap<String, Object> criterios = new HashMap<String, Object>();
		
		criterios.put("codGlosa", codGlosa);
		criterios.put("usuario", usuario);
		
		return getAprobarGlosasDao().guardar(con, criterios);
	}
}