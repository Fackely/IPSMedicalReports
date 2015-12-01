package com.princetonsa.dao.parametrizacion;

import java.sql.Connection;
import java.util.HashMap;

/**
 * 
 * @author wilson
 *
 */
public interface PosicionesAnestesiaDao 
{
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public HashMap<Object, Object> cargarPosiciones (Connection con, int numeroSolicitud);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public  HashMap<Object, Object> cargarTagMapPosiciones(Connection con, int numeroSolicitud, int centroCosto, int institucion);
	
	/**
	 * 
	 * @param con
	 * @param mapaSignoVitalAnestesia
	 * @return
	 */
    public  boolean insertar(Connection con, HashMap<Object, Object> mapa);
    
    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public  boolean modificarPosicion(Connection con, HashMap<Object, Object> mapa);
    
    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public  boolean eliminar(Connection con, int codigoPosicionHojaAnestesia);
    
    /**
     * 
     * @param con
     * @param codigoPosicionInstCC
     * @return
     */
    public int obtenerPosicionDadaPosicionInstCC(Connection con, int codigoPosicionInstCC);
}
