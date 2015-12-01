package com.princetonsa.mundo.glosas;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.ImpresionSoportesFacturasDao;
import com.princetonsa.dao.glosas.AprobarGlosasDao;
import com.princetonsa.dao.glosas.ConsultarImprimirGlosasDao;

public class ConsultarImprimirGlosas
{
	
	//---------------------ATRIBUTOS
	/**
	 * Para manejo de Logs
	 */
	private static Logger logger = Logger.getLogger(ConsultarImprimirGlosas.class);
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static ConsultarImprimirGlosasDao consultarImprimirGlosasDao;
	
	/**
	 * Codigo de la institucion
	 */
	private int institucion;
	
	
	// ----------------	METODOS
	
	/**
	 * Reset
	 */
	private void reset() 
	{
		this.institucion = ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		if (myFactory != null)
		{
			consultarImprimirGlosasDao = myFactory.getConsultarImprimirGlosasDao();
			wasInited = (consultarImprimirGlosasDao != null);
		}
		return wasInited;
	}
	
	/**
	 * 
	 * @return
	 */
	private static ConsultarImprimirGlosasDao getConsultarImprimirGlosasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultarImprimirGlosasDao();
	}
	
	/**
	 * Metodo para consultar la información de una glosa
	 * @param con
	 * @return
	 */
	public static HashMap consultarGlosa(Connection con, String glosa)
	{
		return getConsultarImprimirGlosasDao().consultarGlosa(con, glosa);
	}

	/**
	 * 
	 * @param con
	 * @param listadoGlosasMap
	 * @return
	 */
	public static HashMap listarGlosas(Connection con,HashMap<String, Object> listadoGlosasMap) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultarImprimirGlosasDao().listarGlosas(con, listadoGlosasMap);
	}
	
	/**
	 * Metodo para consultar el detalle de las facturas de una glosa
	 * @param con
	 * @return
	 */
	public static HashMap consultarDetalleFacturasGlosa(Connection con, String glosa, int institucion)
	{
		return getConsultarImprimirGlosasDao().consultarDetalleFacturasGlosa(con, glosa, institucion);
	}
	
	/**
	 * Metodo para consultar el detalle de las facturas de una glosa
	 * @param con
	 * @return
	 */
	public static HashMap consultarDetalleSolicitudesGlosa(Connection con, String codAuditoriaGlosa, int institucion)
	{
		return getConsultarImprimirGlosasDao().consultarDetalleSolicitudesGlosa(con, codAuditoriaGlosa, institucion);
	}
		
}