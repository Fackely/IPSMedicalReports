package com.princetonsa.dao.postgresql.parametrizacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.parametrizacion.IntubacionesHADao;
import com.princetonsa.dao.sqlbase.parametrizacion.SqlBaseIntubacionesHADao;

/**
 * 
 * @author wilson
 *
 */
public class PostgresqlIntubacionesHADao implements IntubacionesHADao 
{
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
    public int insertar(Connection con, HashMap<Object, Object> mapa)
    {
    	return SqlBaseIntubacionesHADao.insertar(con, mapa);
    }
    
    /**
     * 
     * @param con
     * @param codigoIntubacionHojaAnestesia
     * @return
     */
    public boolean eliminar(Connection con, int codigoIntubacionHojaAnestesia)
    {
    	return SqlBaseIntubacionesHADao.eliminar(con, codigoIntubacionHojaAnestesia);
    }
    
    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public boolean insertarDetalle(Connection con, HashMap<Object, Object> mapa)
    {
    	return SqlBaseIntubacionesHADao.insertarDetalle(con, mapa);
    }
    
    /**
     * si tipo_intubacion_multiple es <=0 borra todos las intubaciones  
     * @param con
     * @param viaAerea
     * @param articulo
     * @return
     */
    public boolean eliminarDetalle(Connection con, int codigoIntubacionHojaAnes, int tipoIntubacionMultiple)
    {
    	return SqlBaseIntubacionesHADao.eliminarDetalle(con, codigoIntubacionHojaAnes, tipoIntubacionMultiple);
    }
    
    /**
     * 
     * @param con
     * @param centroCosto
     * @param institucion
     * @return
     */
    public HashMap<Object, Object> cargarTiposIntubacion( Connection con, int centroCosto, int institucion)
    {
    	return SqlBaseIntubacionesHADao.cargarTiposIntubacion(con, centroCosto, institucion);
    }
    
    /**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap<Object, Object> obtenerListadoIntubaciones(Connection con, int numeroSolicitud)
	{
		return SqlBaseIntubacionesHADao.obtenerListadoIntubaciones(con, numeroSolicitud);
	}
	
	/**
     * 
     * @param con
     * @param centroCosto
     * @param institucion
     * @return
     */
    public HashMap<Object, Object> cargarTiposIntubacionMultiple( Connection con, int centroCosto, int institucion)
    {
    	return SqlBaseIntubacionesHADao.cargarTiposIntubacionMultiple(con, centroCosto, institucion);
    }
    
    /**
     * 
     * @param con
     * @param fecha
     * @param hora
     * @param codigoIntubacionHojaAnestesia
     * @return
     */
	public boolean existeIntubacionFechaHora(Connection con, String fecha, String hora, int codigoIntubacionHojaAnestesia)
	{
		return SqlBaseIntubacionesHADao.existeIntubacionFechaHora(con, fecha, hora, codigoIntubacionHojaAnestesia);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoIntubacionHojaAnestesia
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public HashMap<Object, Object> cargarDetalleIntubacionHojaAnestesia(Connection con, int codigoIntubacionHojaAnestesia, int centroCosto, int institucion)
	{
		return SqlBaseIntubacionesHADao.cargarDetalleIntubacionHojaAnestesia(con, codigoIntubacionHojaAnestesia, centroCosto, institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoIntubacionHojaAnestesia
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public HashMap<Object, Object> cargarCormack(Connection con, int centroCosto, int institucion, int tipoIntubacion,  int codigoIntubacionHA)
	{
		return SqlBaseIntubacionesHADao.cargarCormack(con, centroCosto, institucion, tipoIntubacion, codigoIntubacionHA);
	}
}