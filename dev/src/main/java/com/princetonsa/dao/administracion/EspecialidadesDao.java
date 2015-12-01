package com.princetonsa.dao.administracion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dto.administracion.DtoEspecialidades;

/**
 * @author Víctor Hugo Gómez L.
 */

public interface EspecialidadesDao
{

	/**
	 * Insertar una Autorizacion
	 * @param Connection con
	 * @param DtoEspecialidades dtoEspecialidades
	 */
	public int insertarEspecialidad(Connection con, DtoEspecialidades dtoEspecialidades);
	
	/**
	 * Modificacion Especialidad
	 * @param Connection con
	 * @param DtoEspecialidades dtoEpecialidades
	 * 
	 */
	public int updateEspecialidad(Connection con, DtoEspecialidades dtoEspecialidades);
	
	/**
	 * Consulta listado de Especialidades 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public ArrayList<DtoEspecialidades> cargarListadoEspecialidades(Connection con, HashMap parametros);
	
	/**
	 * Metodo que Elimina  una Especialida 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public int deleteEspecialidades(Connection con, HashMap parametros);
	
	/**
	 * Consulta listado de Especialidades 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public ArrayList<DtoEspecialidades> busquedaAvanzadaEspecialidades(Connection con, HashMap parametros);
	
	/**
	 * Consulta listado de Especialidades y Verifica que especialidad esta Siendo Usada 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public ArrayList<DtoEspecialidades> verificarEspecialidadesUsadas(Connection con, ArrayList<DtoEspecialidades> list);
}
