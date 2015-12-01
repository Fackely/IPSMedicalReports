package com.princetonsa.dao.parametrizacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.InfoDatosInt;

import com.princetonsa.dto.salas.DtoMonitoreoHemoDinamica;

/**
 * 
 * @author wilson
 *
 */
public interface MonitoreoHemodinamicaDao 
{
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap<Object, Object> obtenerListadoMonitoreos(Connection con, int numeroSolicitud);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public DtoMonitoreoHemoDinamica cargarMonitoreoHemoDto(Connection con, int codigo);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public ArrayList<InfoDatosInt> cargarOpciones(Connection con, int codigo);
	
	/**
	 * 
	 * @param con
	 * @param codigoMonitoreoHemoHojaAnestesia
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public HashMap<Object, Object> cargarMonitoreo(Connection con, int codigoMonitoreoHemoHojaAnestesia, int centroCosto, int institucion);
	
	/**
	 * 
	 * @param con
	 * @param codigoMonitoreo
	 * @param codigoCampo
	 * @param valor
	 * @return
	 */
	public boolean insertarDetMonHemoHojaAnestesia(Connection con, int codigoMonitoreo, int codigoCampo, String valor);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param fecha
	 * @param hora
	 * @param loginUsuario
	 * @return
	 */
	public int insertarMonHemoHojaAnestesia(Connection con, int numeroSolicitud, String fecha, String hora, String loginUsuario);
	
	/**
	 * 
	 * @param con
	 * @param codigoMonitoreo
	 * @param codigoCampo
	 * @param valor
	 * @return
	 */
	public boolean modificarDetMonHemoHojaAnestesia(Connection con, int codigoMonitoreo, int codigoCampo, String valor);
	
	/**
	 * 
	 * @param con
	 * @param fecha
	 * @param hora
	 * @param loginUsuario
	 * @param codigoMonHemoHojaAnes
	 * @return
	 */
	public boolean modificarMonHemoHojaAnestesia(Connection con, String fecha, String hora, String loginUsuario, int codigoMonHemoHojaAnes);
	
	/**
	 * 
	 * @param con
	 * @param codigoMonitoreo
	 * @param codigoCampo
	 * @param valor
	 * @return
	 */
	public boolean eliminarDetMonHemoHojaAnestesia(Connection con, int codigoMonitoreo, int codigoCampo);
	
	/**
	 * 
	 * @param con
	 * @param codigoMonitoreo
	 * @param codigoCampo
	 * @param valor
	 * @return
	 */
	public boolean eliminarMonHemoHojaAnestesia(Connection con, int codigo);
	
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param fecha
	 * @param hora
	 * @param codigoMonitoreoHojaAnestsia
	 * @return
	 */
	public boolean existeMonitoreoHojaAnestesia(Connection con, int numeroSolicitud, String fecha, String hora, int codigoMonitoreoHojaAnestsia);

}
