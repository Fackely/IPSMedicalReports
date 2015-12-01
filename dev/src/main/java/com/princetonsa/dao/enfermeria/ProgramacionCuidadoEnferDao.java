package com.princetonsa.dao.enfermeria;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.sqlbase.enfermeria.SqlBaseProgramacionCuidadoEnferDao;
import com.princetonsa.dto.enfermeria.DtoCuidadosEnfermeria;
import com.princetonsa.dto.enfermeria.DtoFrecuenciaCuidadoEnferia;

public interface ProgramacionCuidadoEnferDao 
{
	/**
	 * Consulta el listado de los tipo frecuencia por institución
	 * @param Connection con
	 * @param HashMap parametros 
	 * */	
	public ArrayList<HashMap<String,Object>> consultarTipoFrecuenciaInst(Connection con, HashMap parametros);	
				
	/**
	 * Inserta registros de frecuencias de cuidados en Registro de Enfermeria
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public int insertarFrecuenciasCuidados(Connection con, HashMap parametros);
	
	/**
	 * actualiza la informacion de frecuencias de cuidados de registro de enfermeria
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public boolean actualizarFrecuenciasCuidadosRegEnfer(Connection con, HashMap parametros);
		
	/**
	 * 
	 */
	public HashMap consultarListadoPacientes(Connection con, String areaFiltro, String pisoFiltro, String habitacionFiltro, String programados);	
	
	/**
	 * Actualiza el estado de activo de las frecuencias de cuidados de registro de enfermeria
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarEstadoFrecuenciasCuidadosRegEnfer(Connection con,HashMap parametros);
	
	/**
	 * Consulta la informacion de las Frecuencias de los cuidados de enfermeria
	 * @param Connection con 
	 * @param HashMap parametros
	 * */
	public ArrayList<DtoFrecuenciaCuidadoEnferia> consultarFrecuenciaCuidado(Connection con,HashMap parametros);
	
	/**
	 * Consulta la informacion de los programas de cuidados de enfermeria
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public ArrayList<DtoCuidadosEnfermeria> consultarProgCuidadosEnfer(Connection con, HashMap parametros);
	
	/**
	 * Inserta el detalle de programacion de cuidados de enfermeria
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public int insertarDetalleProgCuidadosEnfer(Connection con, HashMap parametros);
	
	/**
	 * Inserta el encabezado de una programacion de cuidados de enfermeria
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public int insertarProgCuidadosEnfer(Connection con, HashMap parametros);
	
	/**
	 * Retorna la cadena sql de consulta
	 *  @param HashMap parametros
	 * */
	public HashMap getStringSqlConsulProgCuidEnfer(HashMap parametros);	
	
}