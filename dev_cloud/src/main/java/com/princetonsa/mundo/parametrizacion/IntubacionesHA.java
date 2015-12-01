package com.princetonsa.mundo.parametrizacion;

import java.sql.Connection;
import java.util.HashMap;

import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.parametrizacion.IntubacionesHADao;

/**
 * 
 * @author wilson
 *
 */
public class IntubacionesHA 
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static IntubacionesHADao iDao;
	
	
	/**
	 * constructor de la clase
	 *
	 */
	public IntubacionesHA() 
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
			iDao = myFactory.getInfusionesHADao();
			wasInited = (iDao != null);
		}
		return wasInited;
	}
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
    public static int insertar(Connection con, HashMap<Object, Object> mapa)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getInfusionesHADao().insertar(con, mapa);
    }
    
    /**
     * 
     * @param con
     * @param codigoIntubacionHojaAnestesia
     * @return
     */
    public static boolean eliminar(Connection con, int codigoIntubacionHojaAnestesia)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getInfusionesHADao().eliminar(con, codigoIntubacionHojaAnestesia);
    }
    
    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public static boolean insertarDetalle(Connection con, HashMap<Object, Object> mapa)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getInfusionesHADao().insertarDetalle(con, mapa);
    }
    
    /**
     * si tipo_intubacion_multiple es <=0 borra todos las intubaciones  
     * @param con
     * @param viaAerea
     * @param articulo
     * @return
     */
    public static boolean eliminarDetalle(Connection con, int codigoIntubacionHojaAnes, int tipoIntubacionMultiple)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getInfusionesHADao().eliminarDetalle(con, codigoIntubacionHojaAnes, tipoIntubacionMultiple);
    }
    
    /**
     * 
     * @param con
     * @param centroCosto
     * @param institucion
     * @return
     */
    public static HashMap<Object, Object> cargarTiposIntubacion( int centroCosto, int institucion)
    {
    	Connection con=UtilidadBD.abrirConexion();
    	HashMap<Object, Object> mapa= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getInfusionesHADao().cargarTiposIntubacion(con, centroCosto, institucion);
    	UtilidadBD.closeConnection(con);
		return mapa;
    }
    
    /**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static HashMap<Object, Object> obtenerListadoIntubaciones(Connection con, int numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getInfusionesHADao().obtenerListadoIntubaciones(con, numeroSolicitud);
	}
	
	/**
     * 
     * @param con
     * @param centroCosto
     * @param institucion
     * @return
     */
    public static HashMap<Object, Object> cargarTiposIntubacionMultiple( Connection con, int centroCosto, int institucion)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getInfusionesHADao().cargarTiposIntubacionMultiple(con, centroCosto, institucion);
    }
    
    /**
     * 
     * @param con
     * @param fecha
     * @param hora
     * @param codigoIntubacionHojaAnestesia
     * @return
     */
	public static boolean existeIntubacionFechaHora(String fecha, String hora, int codigoIntubacionHojaAnestesia)
	{
		Connection con=UtilidadBD.abrirConexion();
		boolean existe= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getInfusionesHADao().existeIntubacionFechaHora(con, fecha, hora, codigoIntubacionHojaAnestesia);
		UtilidadBD.closeConnection(con);
		return existe;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoIntubacionHojaAnestesia
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public static HashMap<Object, Object> cargarDetalleIntubacionHojaAnestesia(Connection con, int codigoIntubacionHojaAnestesia, int centroCosto, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getInfusionesHADao().cargarDetalleIntubacionHojaAnestesia(con, codigoIntubacionHojaAnestesia, centroCosto, institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoIntubacionHojaAnestesia
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public static HashMap<Object, Object> cargarCormack(Connection con, int centroCosto, int institucion, int tipoIntubacion, int codigoIntubacionHA)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getInfusionesHADao().cargarCormack(con, centroCosto,institucion, tipoIntubacion, codigoIntubacionHA);
	}
}
