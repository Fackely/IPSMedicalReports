package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.mundo.facturacion.ParamArchivoPlanoIndCalidad;

public interface ParamArchivoPlanoIndCalidadDao
{
	/**
	 * 
	 * @param con
	 * @return
	 */
	public HashMap<String, Object> consultaES (Connection con);
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public HashMap<String, Object> consultaCC (Connection con);
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public HashMap<String, Object> consultaDX (Connection con);
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public HashMap<String, Object> consultaG (Connection con);
	
	/**
	 * 
	 * @param con
	 * @param tipo
	 * @return
	 */
	public HashMap<String, Object> consultaCOD (Connection con, String tipo);
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public HashMap<String, Object> consultaEspe (Connection con);

	/**
	 * 
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public HashMap<String, Object> consultaCentros (Connection con, int centroAtencion);
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public HashMap<String, Object> consultaSignos (Connection con);
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public boolean insertarEspecialidad(Connection con, ParamArchivoPlanoIndCalidad mundo);
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public boolean insertarCentro(Connection con, ParamArchivoPlanoIndCalidad mundo);
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public boolean insertarDiagnostico(Connection con, ParamArchivoPlanoIndCalidad mundo);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean eliminarEspecialidad(Connection con, String codigo);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean eliminarCentro(Connection con, String codigo);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean eliminarDiagnostico(Connection con, String codigo);
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @param codigoM
	 * @return
	 */
	public boolean modificarES(Connection con, ParamArchivoPlanoIndCalidad mundo, String codigoM);
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @param codigoM
	 * @return
	 */
	public boolean modificarCC(Connection con, ParamArchivoPlanoIndCalidad mundo, String codigoM);
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @param codigoM
	 * @return
	 */
	public boolean modificarDX(Connection con, ParamArchivoPlanoIndCalidad mundo, String codigoM);
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @param codigoM
	 * @return
	 */
	public boolean modificarS(Connection con, ParamArchivoPlanoIndCalidad mundo, String codigoM, String esNuevo);
}