package com.princetonsa.mundo.parametrizacion;

import java.sql.Connection;
import java.util.HashMap;

import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.parametrizacion.SignosVitalesDao;

/**
 * 
 * @author wilson
 *
 */
public class SignosVitales 
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static SignosVitalesDao signosVitalesDao;
	
	
	/**
	 * constructor de la clase
	 *
	 */
	public SignosVitales() 
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
			signosVitalesDao = myFactory.getSignosVitalesDao();
			wasInited = (signosVitalesDao != null);
		}
		return wasInited;
	}
	
	/**
	 * Obtiene los signos vitales, dependiendo si existe informacion histotica en la bd, de los contrario postula el tiempo cero con la parametrizacion
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public static HashMap<Object, Object> obtenerSignosVitalesHojaAnestesia (Connection con, int numeroSolicitud, int centroCosto, int institucion, String graficar)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSignosVitalesDao().obtenerSignosVitalesHojaAnestesia(con, numeroSolicitud, centroCosto, institucion, graficar);
	}
	
	/**
	 * 
	 * @param con
	 * @param mapaSignoVitalAnestesia
	 * @return
	 */
    public static boolean insertarTiempoSignoVitalAnestesia(Connection con, HashMap<Object, Object> mapaSignoVitalAnestesia)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSignosVitalesDao().insertarTiempoSignoVitalAnestesia(con, mapaSignoVitalAnestesia);
    }
    
	/**
	 * 
	 * @param con
	 * @param mapaSignoVitalAnestesia
	 * @return
	 */
    public static boolean modificarTiempoSignoVitalAnestesia(Connection con, HashMap<Object, Object> mapaSignoVitalAnestesia)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSignosVitalesDao().modificarTiempoSignoVitalAnestesia(con, mapaSignoVitalAnestesia);
    }
 
    /**
     * 
     * @param fecha
     * @param hora
     * @param numeroSolicitud
     * @return
     */
	public static boolean existeTiempoSignoVitalAnestesia(String fecha, String hora, int numeroSolicitud, int codigoTiempo)
	{
		Connection con= UtilidadBD.abrirConexion();
		boolean existe= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSignosVitalesDao().existeTiempoSignoVitalAnestesia(con, fecha, hora, numeroSolicitud, codigoTiempo);
		UtilidadBD.closeConnection(con);
		return existe;
	}
	
	/**
	 * 
	 * @param con
	 * @param codTiempoSignoVital
	 * @param codSignoVitalAnestInst
	 * @return
	 */
	public static boolean eliminarTiempoSignoVitalAnestesia(Connection con, int codTiempoSignoVital)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSignosVitalesDao().eliminarTiempoSignoVitalAnestesia(con, codTiempoSignoVital);
	}
}