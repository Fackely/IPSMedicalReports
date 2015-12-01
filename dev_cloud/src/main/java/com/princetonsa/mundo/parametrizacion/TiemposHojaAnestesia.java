package com.princetonsa.mundo.parametrizacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.parametrizacion.TiemposHojaAnestesiaDao;
import com.princetonsa.dto.salas.DtoTiempos;

/**
 * 
 * @author wilson
 *
 */
public class TiemposHojaAnestesia 
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static TiemposHojaAnestesiaDao tiemposDao;
	
	
	/**
	 * constructor de la clase
	 *
	 */
	public TiemposHojaAnestesia() 
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
			tiemposDao = myFactory.getTiemposHojaAnestesiaDao();
			wasInited = (tiemposDao != null);
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
	public static HashMap<Object, Object> cargarTiempos (Connection con, int numeroSolicitud, int centroCosto, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTiemposHojaAnestesiaDao().cargarTiempos(con, numeroSolicitud, centroCosto, institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoEvento
	 * @return
	 */
	public static DtoTiempos cargarTiempo(Connection con, int codigoTiempo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTiemposHojaAnestesiaDao().cargarTiempo(con, codigoTiempo);
	}
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
    public static boolean insertar(Connection con, HashMap<Object, Object> mapa)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTiemposHojaAnestesiaDao().insertar(con, mapa);
    }
    
    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public static boolean modificar(Connection con, HashMap<Object, Object> mapa)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTiemposHojaAnestesiaDao().modificar(con, mapa);
    }
	
}
