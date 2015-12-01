package com.princetonsa.mundo.parametrizacion;

import java.sql.Connection;
import java.util.HashMap;

import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.parametrizacion.EventosDao;
import com.princetonsa.dao.sqlbase.parametrizacion.SqlBaseEventosDao;
import com.princetonsa.dto.salas.DtoEventos;

/**
 * 
 * @author wilson
 *
 */
public class Eventos 
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static EventosDao eventosDao;
	
	
	/**
	 * constructor de la clase
	 *
	 */
	public Eventos() 
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
			eventosDao = myFactory.getEventosDao();
			wasInited = (eventosDao != null);
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
	public static HashMap<Object, Object> cargarSubseccionesEventos (int numeroSolicitud, int centroCosto, int institucion)
	{
		Connection con=UtilidadBD.abrirConexion();
		HashMap<Object, Object> mapa= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEventosDao().cargarSubseccionesEventos(con, numeroSolicitud, centroCosto, institucion);
		UtilidadBD.closeConnection(con);
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoEvento
	 * @return
	 */
	public static DtoEventos cargarEvento(Connection con, int codigoEvento)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEventosDao().cargarEvento(con, codigoEvento);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoEventoInstCC
	 * @param codigoEvento
	 * @return
	 */
	public static HashMap<Object, Object> cargarEventoHojaAnestesia (Connection con, int numeroSolicitud, int codigoEventoInstCC, int codigoEvento)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEventosDao().cargarEventoHojaAnestesia(con, numeroSolicitud, codigoEventoInstCC, codigoEvento);
	}
	
	/**
	 * 
	 * @param con
	 * @param mapaSignoVitalAnestesia
	 * @return
	 */
    public static boolean insertar(Connection con, HashMap<Object, Object> mapa)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEventosDao().insertar(con, mapa);
    }

    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public static boolean modificar(Connection con, HashMap<Object, Object> mapa)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEventosDao().modificar(con, mapa);
    }
    
    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public static boolean eliminar(Connection con, int codigoHojaAnestesia)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEventosDao().eliminar(con, codigoHojaAnestesia);
    }

    /**
     * 
     * @param con
     * @param codigoHojaAnestesia
     * @return
     */
    public static boolean actualizarConsecutivoOtrosEventos(Connection con, int codigoHojaAnestesia)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEventosDao().actualizarConsecutivoOtrosEventos(con, codigoHojaAnestesia);
    }
    
    /**
     * 
     * @param con
     * @param fechaInicial
     * @param horaInicial
     * @param fechaFinal
     * @param horaFinal
     * @param codigoEventoInstCC
     * @param codigoEventoHojaAnestesia
     * @param numeroSolicitud
     * @return
     */
    public static boolean existeCruceFechasEventos(String fechaInicial, String horaInicial, String fechaFinal, String horaFinal, int codigoEventoInstCC, int codigoEventoHojaAnestesia, int numeroSolicitud)
    {
    	Connection con=UtilidadBD.abrirConexion();
    	boolean existeCruce= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEventosDao().existeCruceFechasEventos(con, fechaInicial, horaInicial, fechaFinal, horaFinal, codigoEventoInstCC, codigoEventoHojaAnestesia, numeroSolicitud);
    	UtilidadBD.closeConnection(con);
    	return existeCruce;
    }
    
    /**
     * 
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public static HashMap<Object, Object> consultaEventosMinutos(Connection con, int numeroSolicitud)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEventosDao().consultaEventosMinutos(con, numeroSolicitud);
    }
    
    /**
     * 
     * @param con
     * @param fechaInicio
     * @param horaInicio
     * @param fechaFin
     * @param horaFin
     * @param numeroSolicitud
     * @return
     */
    public static boolean actualizarInicioFinHojaAnestesia(Connection con, String fechaInicio, String horaInicio, String fechaFin, String horaFin, int numeroSolicitud)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEventosDao().actualizarInicioFinHojaAnestesia(con, fechaInicio, horaInicio, fechaFin, horaFin, numeroSolicitud);
    }
    
    /**
     * 
     * @param con
     * @param fechaInicio
     * @param horaInicio
     * @param fechaFin
     * @param horaFin
     * @param numeroSolicitud
     * @return
     */
    public static boolean actualizarInicioFinCx(Connection con, String fechaInicio, String horaInicio, String fechaFin, String horaFin, int numeroSolicitud)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEventosDao().actualizarInicioFinCx(con, fechaInicio, horaInicio, fechaFin, horaFin, numeroSolicitud);
    }
    
    /**
     * Consulta las fechas de los eventos los cuales sean obligatorios 
     * y posea el indicador lleva_fecha_fin como lo indica el parametro
     * @param Connection con
     * @param int numeroSolicitud
     * */
    public static HashMap consultarEventosHojaAnesLlevaFin(Connection con, int numeroSolicitud, String llevaFechaFin)
    {
    	HashMap parametros = new HashMap();
    	parametros.put("numeroSolicitud",numeroSolicitud);
    	parametros.put("llevaFechaFin",llevaFechaFin);
    	return	DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEventosDao().consultarEventosHojaAnesLlevaFin(con, parametros);
    }
    
    /**
     * 
     * @param con
     * @param numeroSolicitud
     * @param graficar
     * @return
     */
	public static HashMap<Object, Object> cargarEventosHojaAnestesia (Connection con, int numeroSolicitud, String graficar)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEventosDao().cargarEventosHojaAnestesia(con, numeroSolicitud, graficar);
	}
}
