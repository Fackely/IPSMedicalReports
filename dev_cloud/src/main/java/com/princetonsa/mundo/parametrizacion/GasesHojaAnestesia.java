package com.princetonsa.mundo.parametrizacion;

import java.sql.Connection;
import java.util.HashMap;

import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.parametrizacion.GasesHojaAnestesiaDao;
import com.princetonsa.dao.sqlbase.parametrizacion.SqlBaseGasesHojaAnestesiaDao;
import com.princetonsa.dto.salas.DtoGases;

/**
 * 
 * @author wilson
 *
 */
public class GasesHojaAnestesia 
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static GasesHojaAnestesiaDao gasesDao;
	
	
	/**
	 * constructor de la clase
	 *
	 */
	public GasesHojaAnestesia() 
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
			gasesDao = myFactory.getGasesHojaAnestesiaDao();
			wasInited = (gasesDao != null);
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
	public static HashMap<Object, Object> cargarSubseccionesGases (int numeroSolicitud, int centroCosto, int institucion)
	{
		Connection con= UtilidadBD.abrirConexion();
		HashMap<Object, Object> mapa= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getGasesHojaAnestesiaDao().cargarSubseccionesGases(con, numeroSolicitud, centroCosto, institucion);
		UtilidadBD.closeConnection(con);
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoEvento
	 * @return
	 */
	public static DtoGases cargarGas(Connection con, int codigoGas)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getGasesHojaAnestesiaDao().cargarGas(con, codigoGas);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoEventoInstCC
	 * @param codigoEvento
	 * @return
	 */
	public static HashMap<Object, Object> cargarGasHojaAnestesia (Connection con, int numeroSolicitud, int codigoGasInstCC, int codigoGas)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getGasesHojaAnestesiaDao().cargarGasHojaAnestesia(con, numeroSolicitud, codigoGasInstCC, codigoGas);
	}
	
	/**
	 * 
	 * @param con
	 * @param mapaSignoVitalAnestesia
	 * @return
	 */
    public static boolean insertar(Connection con, HashMap<Object, Object> mapa)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getGasesHojaAnestesiaDao().insertar(con, mapa);
    }
	
	/**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public static boolean modificar(Connection con, HashMap<Object, Object> mapa)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getGasesHojaAnestesiaDao().modificar(con, mapa);
    }
    
    /**
     * 
     * @param con
     * @return
     */
    public static HashMap<Object, Object> obtenerTiposGasesAnestesicos()
    {
    	Connection con= UtilidadBD.abrirConexion();
    	HashMap<Object, Object> mapa=  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getGasesHojaAnestesiaDao().obtenerTiposGasesAnestesicos(con);
    	UtilidadBD.closeConnection(con);
    	return mapa;
    }
    
    /**
     * 
     * @param con
     * @param numeroSolicitud
     * @param graficar
     * @return
     */
	public static HashMap<Object, Object> cargarGasesHojaAnestesia (Connection con, int numeroSolicitud, String graficar)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getGasesHojaAnestesiaDao().cargarGasesHojaAnestesia(con, numeroSolicitud, graficar);
	}
}
