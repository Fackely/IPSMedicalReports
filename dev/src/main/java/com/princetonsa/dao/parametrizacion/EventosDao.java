package com.princetonsa.dao.parametrizacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dto.salas.DtoEventos;

/**
 * 
 * @author wilson
 *
 */
public interface EventosDao 
{
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public HashMap<Object, Object> cargarSubseccionesEventos (Connection con, int numeroSolicitud, int centroCosto, int institucion);
	
	/**
	 * 
	 * @param con
	 * @param codigoEvento
	 * @return
	 */
	public DtoEventos cargarEvento(Connection con, int codigoEvento);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoEventoInstCC
	 * @param codigoEvento
	 * @return
	 */
	public HashMap<Object, Object> cargarEventoHojaAnestesia (Connection con, int numeroSolicitud, int codigoEventoInstCC, int codigoEvento);
	
	/**
	 * 
	 * @param con
	 * @param mapaSignoVitalAnestesia
	 * @return
	 */
    public boolean insertar(Connection con, HashMap<Object, Object> mapa);
    
    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public boolean modificar(Connection con, HashMap<Object, Object> mapa);


    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public boolean eliminar(Connection con, int codigoHojaAnestesia);
    
    /**
     * 
     * @param con
     * @param codigoHojaAnestesia
     * @return
     */
    public boolean actualizarConsecutivoOtrosEventos(Connection con, int codigoHojaAnestesia);

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
    public boolean existeCruceFechasEventos(Connection con, String fechaInicial, String horaInicial, String fechaFinal, String horaFinal, int codigoEventoInstCC, int codigoEventoHojaAnestesia, int numeroSolicitud);
    
    /**
     * 
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public HashMap<Object, Object> consultaEventosMinutos(Connection con, int numeroSolicitud);
    
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
    public boolean actualizarInicioFinHojaAnestesia(Connection con, String fechaInicio, String horaInicio, String fechaFin, String horaFin, int numeroSolicitud);
    
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
    public boolean actualizarInicioFinCx(Connection con, String fechaInicio, String horaInicio, String fechaFin, String horaFin, int numeroSolicitud);

    /**
     * Consulta las fechas de los eventos los cuales sean obligatorios 
     * y posea el indicador lleva_fecha_fin como lo indica el parametro
     * @param Connection con
     * @param HashMap parametros
     * */
    public HashMap consultarEventosHojaAnesLlevaFin(Connection con, HashMap parametros);
    
    /**
     * 
     * @param con
     * @param numeroSolicitud
     * @param graficar
     * @return
     */
	public HashMap<Object, Object> cargarEventosHojaAnestesia (Connection con, int numeroSolicitud, String graficar);
}