package com.princetonsa.dao.parametrizacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dto.salas.DtoGases;

/**
 * 
 * @author wilson
 *
 */
public interface GasesHojaAnestesiaDao 
{
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public HashMap<Object, Object> cargarSubseccionesGases (Connection con, int numeroSolicitud, int centroCosto, int institucion);
	
	/**
	 * 
	 * @param con
	 * @param codigoEvento
	 * @return
	 */
	public DtoGases cargarGas(Connection con, int codigoGas);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoEventoInstCC
	 * @param codigoEvento
	 * @return
	 */
	public HashMap<Object, Object> cargarGasHojaAnestesia (Connection con, int numeroSolicitud, int codigoGasInstCC, int codigoGas);
	
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
     * @return
     */
    public HashMap<Object, Object> obtenerTiposGasesAnestesicos(Connection con);
    
    /**
     * 
     * @param con
     * @param numeroSolicitud
     * @param graficar
     * @return
     */
	public HashMap<Object, Object> cargarGasesHojaAnestesia (Connection con, int numeroSolicitud, String graficar);
}
