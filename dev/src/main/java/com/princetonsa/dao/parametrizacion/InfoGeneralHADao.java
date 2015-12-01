package com.princetonsa.dao.parametrizacion;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

/**
 * 
 * @author wilson
 *
 */
public interface InfoGeneralHADao 
{
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public HashMap<Object, Object> cargarMonitoreos (Connection con, int numeroSolicitud, int centroCosto, int institucion);
	
	/**
	 * 
	 * @param con
	 * @param mapaSignoVitalAnestesia
	 * @return
	 */
    public boolean insertarMonitoreos(Connection con, HashMap<Object, Object> mapa);
    
    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public boolean modificarMonitoreos(Connection con, HashMap<Object, Object> mapa);
    
    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public boolean eliminarMonitoreo(Connection con, int codigoMonitoreoHojaAnestesia);
    
    /**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public HashMap<Object, Object> cargarProtecciones (Connection con, int numeroSolicitud, int centroCosto, int institucion);
	
	/**
	 * 
	 * @param con
	 * @param mapaSignoVitalAnestesia
	 * @return
	 */
    public boolean insertarProtecciones(Connection con, HashMap<Object, Object> mapa);
    
    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public boolean eliminarProteccion(Connection con, int codigoProteccionHojaAnestesia);
    
    /**
     * 
     */
    public Vector<String> cargarPesoTalla(Connection con, int numeroSolicitud, int institucion);
    
    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public boolean modificarPesoTalla(Connection con, float peso, float talla, int numeroSolicitud);

    /**
     * 
     * @param con
     * @param numeroSolicitud
     * @return
     */
	public String[] obtenerUltimaFechaHoraGraficarRecord(Connection con,int numeroSolicitud);
    
    
}
