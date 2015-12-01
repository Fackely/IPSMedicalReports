package com.princetonsa.dao.postgresql.parametrizacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.parametrizacion.GasesHojaAnestesiaDao;
import com.princetonsa.dao.sqlbase.parametrizacion.SqlBaseGasesHojaAnestesiaDao;
import com.princetonsa.dto.salas.DtoGases;

/**
 * 
 * @author wilson
 *
 */
public class PostgresqlGasesHojaAnestesiaDao implements GasesHojaAnestesiaDao 
{
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public HashMap<Object, Object> cargarSubseccionesGases (Connection con, int numeroSolicitud, int centroCosto, int institucion)
	{
		return SqlBaseGasesHojaAnestesiaDao.cargarSubseccionesGases(con, numeroSolicitud, centroCosto, institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoEvento
	 * @return
	 */
	public DtoGases cargarGas(Connection con, int codigoGas)
	{
		return SqlBaseGasesHojaAnestesiaDao.cargarGas(con, codigoGas);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoEventoInstCC
	 * @param codigoEvento
	 * @return
	 */
	public HashMap<Object, Object> cargarGasHojaAnestesia (Connection con, int numeroSolicitud, int codigoGasInstCC, int codigoGas)
	{
		return SqlBaseGasesHojaAnestesiaDao.cargarGasHojaAnestesia(con, numeroSolicitud, codigoGasInstCC, codigoGas);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param mapaSignoVitalAnestesia
	 * @return
	 */
    public boolean insertar(Connection con, HashMap<Object, Object> mapa)
    {
    	return SqlBaseGasesHojaAnestesiaDao.insertar(con, mapa);
    }
	
	/**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public boolean modificar(Connection con, HashMap<Object, Object> mapa)
    {
    	return SqlBaseGasesHojaAnestesiaDao.modificar(con, mapa);
    }

    /**
     * 
     * @param con
     * @return
     */
    public HashMap<Object, Object> obtenerTiposGasesAnestesicos(Connection con)
    {
    	return SqlBaseGasesHojaAnestesiaDao.obtenerTiposGasesAnestesicos(con);
    }
    
    /**
     * 
     * @param con
     * @param numeroSolicitud
     * @param graficar
     * @return
     */
	public HashMap<Object, Object> cargarGasesHojaAnestesia (Connection con, int numeroSolicitud, String graficar)
	{
		return SqlBaseGasesHojaAnestesiaDao.cargarGasesHojaAnestesia(con, numeroSolicitud, graficar);
	}
}
