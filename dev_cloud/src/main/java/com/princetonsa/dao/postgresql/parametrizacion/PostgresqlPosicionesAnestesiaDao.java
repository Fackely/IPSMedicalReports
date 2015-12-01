package com.princetonsa.dao.postgresql.parametrizacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.parametrizacion.PosicionesAnestesiaDao;
import com.princetonsa.dao.sqlbase.parametrizacion.SqlBasePosicionesAnestesiaDao;

/**
 * 
 * @author wilson
 *
 */
public class PostgresqlPosicionesAnestesiaDao implements PosicionesAnestesiaDao 
{
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public HashMap<Object, Object> cargarPosiciones (Connection con, int numeroSolicitud)
	{
		return SqlBasePosicionesAnestesiaDao.cargarPosiciones(con, numeroSolicitud);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public  HashMap<Object, Object> cargarTagMapPosiciones(Connection con, int numeroSolicitud, int centroCosto, int institucion)
	{
		return SqlBasePosicionesAnestesiaDao.cargarTagMapPosiciones(con, numeroSolicitud, centroCosto, institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param mapaSignoVitalAnestesia
	 * @return
	 */
    public  boolean insertar(Connection con, HashMap<Object, Object> mapa)
    {
    	return SqlBasePosicionesAnestesiaDao.insertar(con, mapa);
    }
    
    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public  boolean modificarPosicion(Connection con, HashMap<Object, Object> mapa)
    {
    	return SqlBasePosicionesAnestesiaDao.modificarPosicion(con, mapa);
    }
    
    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public  boolean eliminar(Connection con, int codigoPosicionHojaAnestesia)
    {
    	return SqlBasePosicionesAnestesiaDao.eliminar(con, codigoPosicionHojaAnestesia);
    }

    /**
     * 
     */
    public int obtenerPosicionDadaPosicionInstCC(Connection con, int codigoPosicionInstCC)
    {
    	return SqlBasePosicionesAnestesiaDao.obtenerPosicionDadaPosicionInstCC(con, codigoPosicionInstCC);
    }
}
