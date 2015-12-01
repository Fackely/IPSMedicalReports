package com.princetonsa.dao.parametrizacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dto.salas.DtoTiempos;

/**
 * 
 * @author wilson
 *
 */
public interface TiemposHojaAnestesiaDao  
{
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public HashMap<Object, Object> cargarTiempos (Connection con, int numeroSolicitud, int centroCosto, int institucion);
	
	/**
	 * 
	 * @param con
	 * @param codigoEvento
	 * @return
	 */
	public DtoTiempos cargarTiempo(Connection con, int codigoTiempo);
	
	/**
	 * 
	 * @param con
	 * @param mapa
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
	
}
