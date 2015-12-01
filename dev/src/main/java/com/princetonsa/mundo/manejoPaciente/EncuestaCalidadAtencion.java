package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.SeccionesDao;
import com.princetonsa.dao.manejoPaciente.EncuestaCalidadAtencionDao;

/**
 * mundo de secciones x almacen
 * @author axioma
 *
 */
public class EncuestaCalidadAtencion 
{
	//--- Atributos
	private static Logger logger = Logger.getLogger(EncuestaCalidadAtencion.class);
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static EncuestaCalidadAtencionDao encuestaCalidadAtencionDao;
	
	private static HashMap ingresosMap; 
	
	private static String paciente;

	
	

	/**
	 * 
	 *
	 */
	public EncuestaCalidadAtencion()
	{
		this.reset();
		this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 * 
	 *
	 */
	public void reset() 
	{
		this.ingresosMap=new HashMap();
		this.ingresosMap.put("numRegistros", 0);
		this.paciente="";
		
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
			encuestaCalidadAtencionDao = myFactory.getEncuestaCalidadAtencionDao();
			wasInited = (encuestaCalidadAtencionDao != null);
		}
		return wasInited;
	}
	
	
	public static HashMap consultar(Connection con, EncuestaCalidadAtencion encuesta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEncuestaCalidadAtencionDao().consultar(con, encuesta);
	}
	
	
	public static HashMap consultarEncuestas(Connection con, int ingreso,String area)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEncuestaCalidadAtencionDao().consultarEncuestas(con, ingreso,area);
	}
	
	public static int guardarEncuestas(Connection con, HashMap encuesta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEncuestaCalidadAtencionDao().guardarEncuestas(con, encuesta);
	}

	public static int modificarEncuestas(Connection con, HashMap encuesta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEncuestaCalidadAtencionDao().modificarEncuestas(con, encuesta);
	}	
	
	/**
	 * @return the paciente
	 */
	public static String getPaciente() {
		return paciente;
	}

	/**
	 * @param paciente the paciente to set
	 */
	public static void setPaciente(String paciente) {
		EncuestaCalidadAtencion.paciente = paciente;
	}

		
}



