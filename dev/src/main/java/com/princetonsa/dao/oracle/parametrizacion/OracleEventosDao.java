package com.princetonsa.dao.oracle.parametrizacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.parametrizacion.EventosDao;
import com.princetonsa.dao.sqlbase.parametrizacion.SqlBaseEventosDao;
import com.princetonsa.dto.salas.DtoEventos;

/**
 * 
 * @author wilson
 *
 */
public class OracleEventosDao implements EventosDao 
{
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public HashMap<Object, Object> cargarSubseccionesEventos (Connection con, int numeroSolicitud, int centroCosto, int institucion)
	{
		return SqlBaseEventosDao.cargarSubseccionesEventos(con, numeroSolicitud, centroCosto, institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoEvento
	 * @return
	 */
	public DtoEventos cargarEvento(Connection con, int codigoEvento)
	{
		return SqlBaseEventosDao.cargarEvento(con, codigoEvento);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoEventoInstCC
	 * @param codigoEvento
	 * @return
	 */
	public HashMap<Object, Object> cargarEventoHojaAnestesia (Connection con, int numeroSolicitud, int codigoEventoInstCC, int codigoEvento)
	{
		return SqlBaseEventosDao.cargarEventoHojaAnestesia(con, numeroSolicitud, codigoEventoInstCC, codigoEvento);
	}
	
	/**
	 * 
	 * @param con
	 * @param mapaSignoVitalAnestesia
	 * @return
	 */
    public boolean insertar(Connection con, HashMap<Object, Object> mapa)
    {
    	return SqlBaseEventosDao.insertar(con, mapa);
    }
    
    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public boolean modificar(Connection con, HashMap<Object, Object> mapa)
    {
    	return SqlBaseEventosDao.modificar(con, mapa);
    }

    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public boolean eliminar(Connection con, int codigoHojaAnestesia)
    {
    	return SqlBaseEventosDao.eliminar(con, codigoHojaAnestesia);
    }
    
    /**
     * 
     * @param con
     * @param codigoHojaAnestesia
     * @return
     */
    public boolean actualizarConsecutivoOtrosEventos(Connection con, int codigoHojaAnestesia)
    {
    	return SqlBaseEventosDao.actualizarConsecutivoOtrosEventos(con, codigoHojaAnestesia);
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
    public boolean existeCruceFechasEventos(Connection con, String fechaInicial, String horaInicial, String fechaFinal, String horaFinal, int codigoEventoInstCC, int codigoEventoHojaAnestesia, int numeroSolicitud)
    {
    	return SqlBaseEventosDao.existeCruceFechasEventos(con, fechaInicial, horaInicial, fechaFinal, horaFinal, codigoEventoInstCC, codigoEventoHojaAnestesia, numeroSolicitud);
    }

    /**
     * 
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public HashMap<Object, Object> consultaEventosMinutos(Connection con, int numeroSolicitud)
    {
    	return SqlBaseEventosDao.consultaEventosMinutos(con, numeroSolicitud);
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
    public boolean actualizarInicioFinHojaAnestesia(Connection con, String fechaInicio, String horaInicio, String fechaFin, String horaFin, int numeroSolicitud)
    {
    	return SqlBaseEventosDao.actualizarInicioFinHojaAnestesia(con, fechaInicio, horaInicio, fechaFin, horaFin, numeroSolicitud);
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
    public boolean actualizarInicioFinCx(Connection con, String fechaInicio, String horaInicio, String fechaFin, String horaFin, int numeroSolicitud)
    {
    	return SqlBaseEventosDao.actualizarInicioFinCx(con, fechaInicio, horaInicio, fechaFin, horaFin, numeroSolicitud);
    }
    
    /**
     * Consulta las fechas de los eventos los cuales sean obligatorios 
     * y posea el indicador lleva_fecha_fin como lo indica el parametro
     * @param Connection con
     * @param HashMap parametros
     * */
    public  HashMap consultarEventosHojaAnesLlevaFin(Connection con, HashMap parametros)
    {
    	return SqlBaseEventosDao.consultarEventosHojaAnesLlevaFin(con, parametros);
    }
    
    /**
     * 
     * @param con
     * @param numeroSolicitud
     * @param graficar
     * @return
     */
	public HashMap<Object, Object> cargarEventosHojaAnestesia (Connection con, int numeroSolicitud, String graficar)
	{
		return SqlBaseEventosDao.cargarEventosHojaAnestesia(con, numeroSolicitud, graficar);
	}
}
