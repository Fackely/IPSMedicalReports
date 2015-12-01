package com.princetonsa.mundo.parametrizacion;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.parametrizacion.InfoGeneralHADao;

/**
 * 
 * @author wilson
 *
 */
public class InfoGeneralHA 
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static InfoGeneralHADao infoDao;
	
	
	/**
	 * constructor de la clase
	 *
	 */
	public InfoGeneralHA() 
	{
		this.init (System.getProperty("TIPOBD"));
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
			infoDao = myFactory.getInfoGeneralHADao();
			wasInited = (infoDao != null);
		}
		return wasInited;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public static HashMap<Object, Object> cargarMonitoreos (Connection con, int numeroSolicitud, int centroCosto, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getInfoGeneralHADao().cargarMonitoreos(con, numeroSolicitud, centroCosto, institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param mapaSignoVitalAnestesia
	 * @return
	 */
    public static boolean insertarMonitoreos(Connection con, HashMap<Object, Object> mapa)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getInfoGeneralHADao().insertarMonitoreos(con, mapa);
    }
    
    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public static boolean modificarMonitoreos(Connection con, HashMap<Object, Object> mapa)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getInfoGeneralHADao().modificarMonitoreos(con, mapa);
    }
    
    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public static boolean eliminarMonitoreo(Connection con, int codigoMonitoreoHojaAnestesia)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getInfoGeneralHADao().eliminarMonitoreo(con, codigoMonitoreoHojaAnestesia);
    }
    
    /**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public static HashMap<Object, Object> cargarProtecciones (Connection con, int numeroSolicitud, int centroCosto, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getInfoGeneralHADao().cargarProtecciones(con, numeroSolicitud, centroCosto, institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param mapaSignoVitalAnestesia
	 * @return
	 */
    public static boolean insertarProtecciones(Connection con, HashMap<Object, Object> mapa)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getInfoGeneralHADao().insertarProtecciones(con, mapa);
    }
    
    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public static boolean eliminarProteccion(Connection con, int codigoProteccionHojaAnestesia)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getInfoGeneralHADao().eliminarProteccion(con, codigoProteccionHojaAnestesia);
    }
 
    /**
     * 
     */
    public static Vector<String> cargarPesoTalla(Connection con, int numeroSolicitud, int institucion)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getInfoGeneralHADao().cargarPesoTalla(con, numeroSolicitud, institucion);
    }
    
    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public static boolean modificarPesoTalla(Connection con, float peso, float talla, int numeroSolicitud)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getInfoGeneralHADao().modificarPesoTalla(con, peso, talla, numeroSolicitud);
    }
    
    /**
     * Metodo que obtiene la ultima fecha hora de las secciones graficables del record de anestesia
     */
    public static String[] obtenerUltimaFechaHoraGraficarRecord(Connection con, int numeroSolicitud)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getInfoGeneralHADao().obtenerUltimaFechaHoraGraficarRecord(con, numeroSolicitud);
    }
    
}
