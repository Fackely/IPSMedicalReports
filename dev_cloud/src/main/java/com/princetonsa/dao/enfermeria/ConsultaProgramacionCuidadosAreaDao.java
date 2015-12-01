package com.princetonsa.dao.enfermeria;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dto.enfermeria.DtoCuidadosEnfermeria;
import com.princetonsa.dto.enfermeria.DtoFrecuenciaCuidadoEnferia;

public interface ConsultaProgramacionCuidadosAreaDao {

	
	/**
	 * Consulta las Areas por Via de Ingreso segun Centro de Atencion
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public HashMap<String, Object> listaAreas (Connection con, int centroAtencion);
	
	/**
	 * Consulta los Pisos segun Centro de Atencion
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public HashMap<String, Object> listaPisos (Connection con, int centroAtencion);
	
	/**
	 * Consulta las Habitaciones segun Piso
	 * @param con
	 * @param Piso
	 * @return
	 */
	public HashMap<String, Object> listaHabitaciones (Connection con, int piso);

	
	/**
	 * 
	 * @param con
	 * @param areaFiltro
	 * @param pisoFiltro
	 * @param habitacionFiltro
	 * @return
	 */
	public HashMap consultarListadoPacientes(Connection con, String areaFiltro,	String pisoFiltro, String habitacionFiltro, String fechaProg, String horaProg);

	
	/**
	 * 
	 * @param con
	 * @param parametros
	 * @return
	 */
	public ArrayList<DtoFrecuenciaCuidadoEnferia> consultarFrecuenciaCuidado(Connection con, HashMap parametros);

	
	/**
	 * 
	 * @param con
	 * @param parametros
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> consultarTipoFrecuenciaInst(Connection con, HashMap parametros);

	/**
	 * Retorna la consulta(String) sirve para listar los cuidados de enfermeria
	 * @param con
	 * @param parametros
	 * @return
	 */
	public String cadenaConsultaCuidados(Connection con, HashMap parametros);


	
	
}
