package com.princetonsa.mundo.parametrizacion;

import java.sql.Connection;
import java.util.HashMap;

import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.parametrizacion.ViasAereasDao;

/**
 * 
 * @author wilson
 *
 */
public class ViasAereas 
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static ViasAereasDao viasDao;
	
	
	/**
	 * constructor de la clase
	 *
	 */
	public ViasAereas() 
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
			viasDao = myFactory.getViasAereasDao();
			wasInited = (viasDao != null);
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
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getViasAereasDao().insertar(con, mapa);
    }
    
    /**
     * 
     * @param con
     * @param viaAerea
     * @param articulo
     * @return
     */
    public static boolean eliminar(Connection con, int viaAerea)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getViasAereasDao().eliminar(con, viaAerea);
    }
    
    /**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
    public static boolean insertarDetalle(Connection con, HashMap<Object, Object> mapa)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getViasAereasDao().insertarDetalle(con, mapa);
    }
    
    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public static boolean modificar(Connection con, HashMap<Object, Object> mapa)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getViasAereasDao().modificar(con, mapa);
    }
    
    /**
     * si articulo es <=0 borra todos los articulos pertenecientes a la via aerea
     * @param con
     * @param viaAerea
     * @param articulo
     * @return
     */
    public static boolean eliminarDetalle(Connection con, int viaAerea, int articulo)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getViasAereasDao().eliminarDetalle(con, viaAerea, articulo);
    }
    
    /**
     * 
     * @param con
     * @param centroCosto
     * @param institucion
     * @return
     */
    public static HashMap<Object, Object> cargarTiposDispositivosCCInst( Connection con, int centroCosto, int institucion)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getViasAereasDao().cargarTiposDispositivosCCInst(con, centroCosto, institucion);
    }
 
    /**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static HashMap<Object, Object> obtenerListadoViasAereas(Connection con, int numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getViasAereasDao().obtenerListadoViasAereas(con, numeroSolicitud);
	}
	
	/**
	 * 
	 * @param con
	 * @param articulo
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
    public static HashMap<Object, Object> cargarViasInsercionArticulo( int articulo, int centroCosto, int institucion)
    {
    	Connection con=UtilidadBD.abrirConexion();
    	HashMap<Object, Object> mapa= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getViasAereasDao().cargarViasInsercionArticulo(con, articulo, centroCosto, institucion);
    	UtilidadBD.closeConnection(con);
    	return mapa;
    }
    
    /**
     * 
     * @param con
     * @param fecha
     * @param hora
     * @param codigoViaAereaHojaAnestesia
     * @return
     */
	public static boolean existeViaAereaFechaHora(String fecha, String hora, int codigoViaAereaHojaAnestesia)
	{
		Connection con=UtilidadBD.abrirConexion();
    	boolean existe= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getViasAereasDao().existeViaAereaFechaHora(con, fecha, hora, codigoViaAereaHojaAnestesia);
    	UtilidadBD.closeConnection(con);
    	return existe;
    }

	/**
	 * 
	 * @param con
	 * @param codigoViaAereaHojaAnestesia
	 * @return
	 */	
	public static HashMap<Object, Object> cargarViaAereaHojaAnestesia(Connection con, int codigoViaAereaHojaAnestesia)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getViasAereasDao().cargarViaAereaHojaAnestesia(con, codigoViaAereaHojaAnestesia);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */	
	public static HashMap<Object, Object> cargarViaAereaCompleta(Connection con, int numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getViasAereasDao().cargarViaAereaCompleta(con, numeroSolicitud);
	}
}