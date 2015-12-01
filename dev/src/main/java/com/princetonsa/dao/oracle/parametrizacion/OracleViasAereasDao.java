package com.princetonsa.dao.oracle.parametrizacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.parametrizacion.ViasAereasDao;
import com.princetonsa.dao.sqlbase.parametrizacion.SqlBaseViasAereasDao;

/**
 * 
 * @author wilson
 *
 */
public class OracleViasAereasDao implements ViasAereasDao 
{

	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
    public int insertar(Connection con, HashMap<Object, Object> mapa)
    {
    	return SqlBaseViasAereasDao.insertar(con, mapa);
    }
    
    /**
     * 
     * @param con
     * @param viaAerea
     * @param articulo
     * @return
     */
    public boolean eliminar(Connection con, int viaAerea)
    {
    	return SqlBaseViasAereasDao.eliminar(con, viaAerea);
    }
    
    /**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
    public boolean insertarDetalle(Connection con, HashMap<Object, Object> mapa)
    {
    	return SqlBaseViasAereasDao.insertarDetalle(con, mapa);
    }
    
    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public boolean modificar(Connection con, HashMap<Object, Object> mapa)
    {
    	return SqlBaseViasAereasDao.modificar(con, mapa);
    }
    
    /**
     * si articulo es <=0 borra todos los articulos pertenecientes a la via aerea
     * @param con
     * @param viaAerea
     * @param articulo
     * @return
     */
    public boolean eliminarDetalle(Connection con, int viaAerea, int articulo)
    {
    	return SqlBaseViasAereasDao.eliminarDetalle(con, viaAerea, articulo);
    }
    
    /**
     * 
     * @param con
     * @param centroCosto
     * @param institucion
     * @return
     */
    public HashMap<Object, Object> cargarTiposDispositivosCCInst( Connection con, int centroCosto, int institucion)
    {
    	return SqlBaseViasAereasDao.cargarTiposDispositivosCCInst(con, centroCosto, institucion);
    }

    /**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap<Object, Object> obtenerListadoViasAereas(Connection con, int numeroSolicitud)
	{
		return SqlBaseViasAereasDao.obtenerListadoViasAereas(con, numeroSolicitud);
	}
	
	/**
	 * 
	 * @param con
	 * @param articulo
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
    public HashMap<Object, Object> cargarViasInsercionArticulo( Connection con, int articulo, int centroCosto, int institucion)
    {
    	return SqlBaseViasAereasDao.cargarViasInsercionArticulo(con, articulo, centroCosto, institucion);
    }
    
    /**
     * 
     * @param con
     * @param fecha
     * @param hora
     * @param codigoViaAereaHojaAnestesia
     * @return
     */
	public boolean existeViaAereaFechaHora(Connection con, String fecha, String hora, int codigoViaAereaHojaAnestesia)
	{
		return SqlBaseViasAereasDao.existeViaAereaFechaHora(con, fecha, hora, codigoViaAereaHojaAnestesia);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoViaAereaHojaAnestesia
	 * @return
	 */	
	public HashMap<Object, Object> cargarViaAereaHojaAnestesia(Connection con, int codigoViaAereaHojaAnestesia)
	{
		return SqlBaseViasAereasDao.cargarViaAereaHojaAnestesia(con, codigoViaAereaHojaAnestesia);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */	
	public HashMap<Object, Object> cargarViaAereaCompleta(Connection con, int numeroSolicitud)
	{
		return SqlBaseViasAereasDao.cargarViaAereaCompleta(con, numeroSolicitud);
	}
 }
