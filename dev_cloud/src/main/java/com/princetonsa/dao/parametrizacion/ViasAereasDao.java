package com.princetonsa.dao.parametrizacion;

import java.sql.Connection;
import java.util.HashMap;

/**
 * 
 * @author wilson
 *
 */
public interface ViasAereasDao 
{
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
    public int insertar(Connection con, HashMap<Object, Object> mapa);
    
    /**
     * 
     * @param con
     * @param viaAerea
     * @param articulo
     * @return
     */
    public boolean eliminar(Connection con, int viaAerea);
    
    /**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
    public boolean insertarDetalle(Connection con, HashMap<Object, Object> mapa);
    
    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public boolean modificar(Connection con, HashMap<Object, Object> mapa);
    
    /**
     * si articulo es <=0 borra todos los articulos pertenecientes a la via aerea
     * @param con
     * @param viaAerea
     * @param articulo
     * @return
     */
    public boolean eliminarDetalle(Connection con, int viaAerea, int articulo);
    
    /**
     * 
     * @param con
     * @param centroCosto
     * @param institucion
     * @return
     */
    public HashMap<Object, Object> cargarTiposDispositivosCCInst( Connection con, int centroCosto, int institucion);
    
    /**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap<Object, Object> obtenerListadoViasAereas(Connection con, int numeroSolicitud);
	
	/**
	 * 
	 * @param con
	 * @param articulo
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
    public HashMap<Object, Object> cargarViasInsercionArticulo( Connection con, int articulo, int centroCosto, int institucion);
    
    /**
     * 
     * @param con
     * @param fecha
     * @param hora
     * @param codigoViaAereaHojaAnestesia
     * @return
     */
	public boolean existeViaAereaFechaHora(Connection con, String fecha, String hora, int codigoViaAereaHojaAnestesia);
	
	/**
	 * 
	 * @param con
	 * @param codigoViaAereaHojaAnestesia
	 * @return
	 */	
	public HashMap<Object, Object> cargarViaAereaHojaAnestesia(Connection con, int codigoViaAereaHojaAnestesia);
	
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */	
	public HashMap<Object, Object> cargarViaAereaCompleta(Connection con, int numeroSolicitud);
}
