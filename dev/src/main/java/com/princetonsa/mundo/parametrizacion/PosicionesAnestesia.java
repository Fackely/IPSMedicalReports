package com.princetonsa.mundo.parametrizacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.parametrizacion.PosicionesAnestesiaDao;


/**
 * 
 * @author wilson
 *
 */
public class PosicionesAnestesia 
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static PosicionesAnestesiaDao posicionesDao;
	
	
	/**
	 * constructor de la clase
	 *
	 */
	public PosicionesAnestesia() 
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
			posicionesDao = myFactory.getPosicionesAnestesiaDao();
			wasInited = (posicionesDao != null);
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
	public static HashMap<Object, Object> cargarPosiciones (Connection con, int numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPosicionesAnestesiaDao().cargarPosiciones(con, numeroSolicitud);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public static HashMap<Object, Object> cargarTagMapPosiciones(Connection con, int numeroSolicitud, int centroCosto, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPosicionesAnestesiaDao().cargarTagMapPosiciones(con, numeroSolicitud, centroCosto, institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param mapaSignoVitalAnestesia
	 * @return
	 */
    public static boolean insertar(Connection con, HashMap<Object, Object> mapa)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPosicionesAnestesiaDao().insertar(con, mapa);
    }
    
    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public static boolean modificarPosicion(Connection con, HashMap<Object, Object> mapa)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPosicionesAnestesiaDao().modificarPosicion(con, mapa);
    }
    
    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public static boolean eliminar(Connection con, int codigoPosicionHojaAnestesia)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPosicionesAnestesiaDao().eliminar(con, codigoPosicionHojaAnestesia);
    }

    /**
     * 
     * @param con
     * @param codigoPosicionInstCC
     * @return
     */
    public static int obtenerPosicionDadaPosicionInstCC(Connection con, int codigoPosicionInstCC)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPosicionesAnestesiaDao().obtenerPosicionDadaPosicionInstCC(con, codigoPosicionInstCC);
    }
}
