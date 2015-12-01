package com.princetonsa.dao.oracle.administracion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.administracion.EspecialidadesDao;
import com.princetonsa.dao.sqlbase.administracion.SqlBaseEspecialidadesDao;
import com.princetonsa.dto.administracion.DtoEspecialidades;

public class OracleEspecialidadesDao implements EspecialidadesDao 
{
	/**
	 * Insertar una Autorizacion
	 * @param Connection con
	 * @param DtoEspecialidades dtoEspecialidades
	 */
	public int insertarEspecialidad(Connection con, DtoEspecialidades dtoEspecialidades){
		return SqlBaseEspecialidadesDao.insertarEspecialidad(con, dtoEspecialidades);
	}
	
	/**
	 * Modificacion Especialidad
	 * @param Connection con
	 * @param DtoEspecialidades dtoEpecialidades
	 * 
	 */
	public int updateEspecialidad(Connection con, DtoEspecialidades dtoEspecialidades){
		return SqlBaseEspecialidadesDao.updateEspecialidad(con, dtoEspecialidades);
	}
	
	/**
	 * Consulta listado de Especialidades 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public ArrayList<DtoEspecialidades> cargarListadoEspecialidades(Connection con, HashMap parametros){
		return SqlBaseEspecialidadesDao.cargarListadoEspecialidades(con, parametros);
	}
	
	/**
	 * Metodo que Elimina  una Especialida 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public int deleteEspecialidades(Connection con, HashMap parametros)
	{
		return SqlBaseEspecialidadesDao.deleteEspecialidades(con, parametros);
	}
	
	/**
	 * Consulta listado de Especialidades 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public ArrayList<DtoEspecialidades> busquedaAvanzadaEspecialidades(Connection con, HashMap parametros)
	{
		return SqlBaseEspecialidadesDao.busquedaAvanzadaEspecialidades(con, parametros);
	}
	
	/**
	 * Consulta listado de Especialidades y Verifica que especialidad esta Siendo Usada 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public ArrayList<DtoEspecialidades> verificarEspecialidadesUsadas(Connection con, ArrayList<DtoEspecialidades> list)
	{
		return SqlBaseEspecialidadesDao.verificarEspecialidadesUsadas(con, list);
	}
}
