package com.princetonsa.dao.parametrizacion;

import java.sql.Connection;
import java.util.HashMap;

/**
 * 
 * @author wilson
 *
 */
public interface IntubacionesHADao 
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
     * @param codigoIntubacionHojaAnestesia
     * @return
     */
    public boolean eliminar(Connection con, int codigoIntubacionHojaAnestesia);
    
    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public boolean insertarDetalle(Connection con, HashMap<Object, Object> mapa);
    
    /**
     * si tipo_intubacion_multiple es <=0 borra todos las intubaciones  
     * @param con
     * @param viaAerea
     * @param articulo
     * @return
     */
    public boolean eliminarDetalle(Connection con, int codigoIntubacionHojaAnes, int tipoIntubacionMultiple);
    
    /**
     * 
     * @param con
     * @param centroCosto
     * @param institucion
     * @return
     */
    public HashMap<Object, Object> cargarTiposIntubacion( Connection con, int centroCosto, int institucion);
    
    /**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap<Object, Object> obtenerListadoIntubaciones(Connection con, int numeroSolicitud);
	
	/**
     * 
     * @param con
     * @param centroCosto
     * @param institucion
     * @return
     */
    public HashMap<Object, Object> cargarTiposIntubacionMultiple( Connection con, int centroCosto, int institucion);
    
    /**
     * 
     * @param con
     * @param fecha
     * @param hora
     * @param codigoIntubacionHojaAnestesia
     * @return
     */
	public boolean existeIntubacionFechaHora(Connection con, String fecha, String hora, int codigoIntubacionHojaAnestesia);
	
	/**
	 * 
	 * @param con
	 * @param codigoIntubacionHojaAnestesia
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public HashMap<Object, Object> cargarDetalleIntubacionHojaAnestesia(Connection con, int codigoIntubacionHojaAnestesia, int centroCosto, int institucion);
	
	/**
	 * 
	 * @param con
	 * @param codigoIntubacionHojaAnestesia
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public HashMap<Object, Object> cargarCormack(Connection con, int centroCosto, int institucion, int tipoIntubacion, int codigoIntubacionHA);
	
}
