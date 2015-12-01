package com.princetonsa.dao.oracle.parametrizacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.InfoDatosInt;

import com.princetonsa.dao.parametrizacion.MonitoreoHemodinamicaDao;
import com.princetonsa.dao.sqlbase.parametrizacion.SqlBaseMonitoreoHemodinamicaDao;
import com.princetonsa.dto.salas.DtoMonitoreoHemoDinamica;

/**
 * 
 * @author wilson
 *
 */
public class OracleMonitoreoHemodinamicaDao implements MonitoreoHemodinamicaDao
{
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap<Object, Object> obtenerListadoMonitoreos(Connection con, int numeroSolicitud)
	{
		return SqlBaseMonitoreoHemodinamicaDao.obtenerListadoMonitoreos(con, numeroSolicitud);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public DtoMonitoreoHemoDinamica cargarMonitoreoHemoDto(Connection con, int codigo)
	{
		return SqlBaseMonitoreoHemodinamicaDao.cargarMonitoreoHemoDto(con, codigo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public ArrayList<InfoDatosInt> cargarOpciones(Connection con, int codigo)
	{
		return SqlBaseMonitoreoHemodinamicaDao.cargarOpciones(con, codigo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoMonitoreoHemoHojaAnestesia
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public HashMap<Object, Object> cargarMonitoreo(Connection con, int codigoMonitoreoHemoHojaAnestesia, int centroCosto, int institucion)
	{
		return SqlBaseMonitoreoHemodinamicaDao.cargarMonitoreo(con, codigoMonitoreoHemoHojaAnestesia, centroCosto, institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoMonitoreo
	 * @param codigoCampo
	 * @param valor
	 * @return
	 */
	public boolean insertarDetMonHemoHojaAnestesia(Connection con, int codigoMonitoreo, int codigoCampo, String valor)
	{
		return SqlBaseMonitoreoHemodinamicaDao.insertarDetMonHemoHojaAnestesia(con, codigoMonitoreo, codigoCampo, valor);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param fecha
	 * @param hora
	 * @param loginUsuario
	 * @return
	 */
	public int insertarMonHemoHojaAnestesia(Connection con, int numeroSolicitud, String fecha, String hora, String loginUsuario)
	{
		return SqlBaseMonitoreoHemodinamicaDao.insertarMonHemoHojaAnestesia(con, numeroSolicitud, fecha, hora, loginUsuario);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoMonitoreo
	 * @param codigoCampo
	 * @param valor
	 * @return
	 */
	public boolean modificarDetMonHemoHojaAnestesia(Connection con, int codigoMonitoreo, int codigoCampo, String valor)
	{
		return SqlBaseMonitoreoHemodinamicaDao.modificarDetMonHemoHojaAnestesia(con, codigoMonitoreo, codigoCampo, valor);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param fecha
	 * @param hora
	 * @param loginUsuario
	 * @param codigoMonHemoHojaAnes
	 * @return
	 */
	public boolean modificarMonHemoHojaAnestesia(Connection con, String fecha, String hora, String loginUsuario, int codigoMonHemoHojaAnes)
	{
		return SqlBaseMonitoreoHemodinamicaDao.modificarMonHemoHojaAnestesia(con, fecha, hora, loginUsuario, codigoMonHemoHojaAnes);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoMonitoreo
	 * @param codigoCampo
	 * @param valor
	 * @return
	 */
	public boolean eliminarDetMonHemoHojaAnestesia(Connection con, int codigoMonitoreo, int codigoCampo)
	{
		return SqlBaseMonitoreoHemodinamicaDao.eliminarDetMonHemoHojaAnestesia(con, codigoMonitoreo, codigoCampo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoMonitoreo
	 * @param codigoCampo
	 * @param valor
	 * @return
	 */
	public boolean eliminarMonHemoHojaAnestesia(Connection con, int codigo)
	{
		return SqlBaseMonitoreoHemodinamicaDao.eliminarMonHemoHojaAnestesia(con, codigo);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param fecha
	 * @param hora
	 * @param codigoMonitoreoHojaAnestsia
	 * @return
	 */
	public boolean existeMonitoreoHojaAnestesia(Connection con, int numeroSolicitud, String fecha, String hora, int codigoMonitoreoHojaAnestsia)
	{
		return SqlBaseMonitoreoHemodinamicaDao.existeMonitoreoHojaAnestesia(con, numeroSolicitud, fecha, hora, codigoMonitoreoHojaAnestsia);
	}	
}