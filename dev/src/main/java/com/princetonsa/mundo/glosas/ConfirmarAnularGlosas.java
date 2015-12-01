package com.princetonsa.mundo.glosas;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesIntegridadDominio;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.glosas.ConfirmarAnularGlosasDao;

public class ConfirmarAnularGlosas
{
	/**
	 * Para manejo de Logs
	 */
	private static Logger logger = Logger.getLogger(ConfirmarAnularGlosas.class);
	
	private static ConfirmarAnularGlosasDao getConfirmarAnularGlosasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConfirmarAnularGlosasDao();
	}
	
	/**
	 * Metodo para consultar los Convenios
	 * @param con
	 * @return
	 */
	public static HashMap consultarConvenios(Connection con)
	{
		return getConfirmarAnularGlosasDao().consultarConvenios(con);
	}
	
	/**
	 * Metodo que valida el detalle y la suma del valor de la Glosa
	 * @param con
	 * @param codGlosa
	 * @param valor
	 * @return
	 */
	public static boolean validarAnuConfGlosa(Connection con, String codGlosa, String valor)
	{
		return getConfirmarAnularGlosasDao().validarAnuConfGlosa(con, codGlosa, valor);
	}
	
	/**
	 * Metodo que actualiza la glosa segun estado
	 * @param con
	 * @param codGlosa
	 * @param checkCA
	 * @param motivo
	 * @return
	 */
	public static boolean guardar(Connection con, String codGlosa, String checkCA, String motivo, String usuario, String indicativo)
	{
		ActionErrors errors = new ActionErrors();
		HashMap<String, Object> criterios = new HashMap<String, Object>();
		HashMap<String, Object> detalleGlosa = new HashMap<String, Object>();
		int detalleFacturas=0;
		
		criterios.put("codGlosa", codGlosa);
		criterios.put("usuario", usuario);
		if(checkCA.equals("CON"))
		{							
			detalleGlosa=getConfirmarAnularGlosasDao().consultarFacturasGlosa(con, codGlosa);
			for(int i=0;i<Utilidades.convertirAEntero(detalleGlosa.get("numRegistros")+"");i++)
			{
				detalleFacturas=getConfirmarAnularGlosasDao().consultaDetalleFacturaGlosa(con, detalleGlosa.get("codaudi_"+i)+"");					
				if(detalleFacturas > 0)
					return false;
			}				
			criterios.put("anularConfirmar", "CONF");
			criterios.put("motivo", "");
		}
		if(checkCA.equals("ANU"))
		{
			criterios.put("anularConfirmar", "ANUL");
			criterios.put("motivo", motivo);
		}
		
		return getConfirmarAnularGlosasDao().guardar(con, criterios);
	}
}