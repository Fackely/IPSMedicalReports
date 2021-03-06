package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;


public interface PacientesEntidadesSubConDao
{
	/**
	 * Consulta la informacion de la tabla Pacientes Entidades Subcontratadas
	 * @param Connection con
	 * @param HashMap parametros
	 * @return HashMap datos
	 * */
	public HashMap consultarPacientesEntidadesSubcontratadas(Connection con, HashMap parametros);
	
	/**
	 * Actualiza la informacion de la tabla Pacientes Entidades Subcontratadas
	 * @param Connection con
	 * @param HashMap parametros
	 * @return boolean 
	 * */
	public boolean actualizarPacientesEntidadesSubcontratadas(Connection con, HashMap parametros);
	
	/**
	 * Actualiza el estado del registro paciente entidades subcontratadas
	 * @param Connection con
	 * @param HashMap parametros
	 * @return boolean 
	 * */
	public boolean actualizarEstadoPacientesEntidadesSubcontratadas(Connection con, HashMap parametros);
	
	
	/**
	 * Inserta informacion en la tabla Pacientes Entidades Subcontratadas 
	 * @param Connection con
	 * @param HashMap parametros
	 * @param String cadena
	 * @return boolean
	 * */
	public boolean insertarPacientesEntidadesSubcontratadas(Connection con, HashMap parametros);
	
	/**
	 * Consulta la informacion de la tabla Detalle Servicios Autorizados
	 * @param Connection con
	 * @param HashMap parametros
	 * @return HashMap datos
	 * */
	public HashMap consultarServiciosAutorizados(Connection con, HashMap parametros);
	
	/**
	 * Actuliza los datos de la tabla Detalle Servicios Autorizados
	 * @param Connection con
	 * @param HashMap parametros
	 * @return boolean 
	 * */
	public boolean actualizarServiciosAutorizados(Connection con, HashMap parametros);
	
	/**
	 * Inserta datos en la tabla Detalle Servicios Autorizados
	 * @param Conenction con
	 * @param HashMap parametros
	 * */
	public boolean insertarServiciosAutorizados(Connection con, HashMap parametros);
	
	/**
	 * Elimina Servcios Autorizados 
	 * @param Connection con
	 * @param HashMap parametros
	 * @return boolean 
	 * */
	public boolean eliminarServiciosAutorizados(Connection con, HashMap parametros);
	
	/**
	 * M?todo implementado para reversar el registro de entidades subcontratadas
	 * @param con
	 * @param campos
	 * @return
	 */
	public int reversarPacienteEntidadSubcontratada(Connection con,HashMap campos);
}