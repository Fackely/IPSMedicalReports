package com.princetonsa.dao.postgresql.parametrizacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.parametrizacion.TiemposHojaAnestesiaDao;
import com.princetonsa.dao.sqlbase.parametrizacion.SqlBaseTiemposHojaAnestesiaDao;
import com.princetonsa.dto.salas.DtoTiempos;

/**
 * 
 * @author wilson
 *
 */
public class PostgresqlTiemposHojaAnestesiaDao implements TiemposHojaAnestesiaDao
{
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public HashMap<Object, Object> cargarTiempos (Connection con, int numeroSolicitud, int centroCosto, int institucion)
	{
		return SqlBaseTiemposHojaAnestesiaDao.cargarTiempos(con, numeroSolicitud, centroCosto, institucion);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoEvento
	 * @return
	 */
	public DtoTiempos cargarTiempo(Connection con, int codigoTiempo)
	{
		return SqlBaseTiemposHojaAnestesiaDao.cargarTiempo(con, codigoTiempo);
	}
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
    public boolean insertar(Connection con, HashMap<Object, Object> mapa)
    {
    	return SqlBaseTiemposHojaAnestesiaDao.insertar(con, mapa);
    }
    
    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public boolean modificar(Connection con, HashMap<Object, Object> mapa)
    {
    	return SqlBaseTiemposHojaAnestesiaDao.modificar(con, mapa);
    }
}
