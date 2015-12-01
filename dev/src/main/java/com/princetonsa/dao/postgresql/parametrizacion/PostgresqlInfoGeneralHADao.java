package com.princetonsa.dao.postgresql.parametrizacion;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.parametrizacion.InfoGeneralHADao;
import com.princetonsa.dao.sqlbase.parametrizacion.SqlBaseInfoGeneralHADao;

/**
 * 
 * @author wilson
 *
 */
public class PostgresqlInfoGeneralHADao implements InfoGeneralHADao
{

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public HashMap<Object, Object> cargarMonitoreos (Connection con, int numeroSolicitud, int centroCosto, int institucion)
	{
		return SqlBaseInfoGeneralHADao.cargarMonitoreos(con, numeroSolicitud, centroCosto, institucion);
	}

	/**
	 * 
	 * @param con
	 * @param mapaSignoVitalAnestesia
	 * @return
	 */
    public boolean insertarMonitoreos(Connection con, HashMap<Object, Object> mapa)
    {
    	return SqlBaseInfoGeneralHADao.insertarMonitoreos(con, mapa);
    }
    
    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public boolean modificarMonitoreos(Connection con, HashMap<Object, Object> mapa)
    {
    	return SqlBaseInfoGeneralHADao.modificarMonitoreos(con, mapa);
    }
    
    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public boolean eliminarMonitoreo(Connection con, int codigoMonitoreoHojaAnestesia)
    {
    	return SqlBaseInfoGeneralHADao.eliminarMonitoreo(con, codigoMonitoreoHojaAnestesia);
    }
    
    /**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public HashMap<Object, Object> cargarProtecciones (Connection con, int numeroSolicitud, int centroCosto, int institucion)
	{
		return SqlBaseInfoGeneralHADao.cargarProtecciones(con, numeroSolicitud, centroCosto, institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param mapaSignoVitalAnestesia
	 * @return
	 */
    public boolean insertarProtecciones(Connection con, HashMap<Object, Object> mapa)
    {
    	return SqlBaseInfoGeneralHADao.insertarProtecciones(con, mapa);
    }
    
    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public boolean eliminarProteccion(Connection con, int codigoProteccionHojaAnestesia)
    {
    	return SqlBaseInfoGeneralHADao.eliminarProteccion(con, codigoProteccionHojaAnestesia);
    }
    
    /**
     * 
     */
    public Vector<String> cargarPesoTalla(Connection con, int numeroSolicitud, int institucion)
    {
    	return SqlBaseInfoGeneralHADao.cargarPesoTalla(con, numeroSolicitud, institucion);
    }
    
    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public boolean modificarPesoTalla(Connection con, float peso, float talla, int numeroSolicitud)
    {
    	return SqlBaseInfoGeneralHADao.modificarPesoTalla(con, peso, talla, numeroSolicitud);
    }
    
    /**
     * 
     * @param con
     * @param numeroSolicitud
     * @return
     */
	public String[] obtenerUltimaFechaHoraGraficarRecord(Connection con,int numeroSolicitud)
	{
		return SqlBaseInfoGeneralHADao.obtenerUltimaFechaHoraGraficarRecord(con, numeroSolicitud, DaoFactory.POSTGRESQL);
	}
}
