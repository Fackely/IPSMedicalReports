package com.princetonsa.dao.tramiteReferencia;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Jose Eduardo Arias Doncel
 * jearias@princetonsa.com
 * */
public interface TramiteReferenciaDao
{
	/**
	 * Consultar Listado de Referencia
	 * @param Connection 
	 * @param HashMap parametrosBusqueda (institucion,referencia,estado1,estado2)
	 * */
	public abstract HashMap consultarListadoReferencia(Connection con, HashMap parametrosBusqueda);

	/**
	 * Consultar Tramite Referencia
	 * @param Connection 
	 * @param int numero de referencia tramite  
	 * */
	public abstract HashMap consultarTramiteReferencia(Connection con, int numeroReferenciaTramite);
	
	/**
	 * Actualiza Tramite Referencia
	 * @param Connection 
	 * @param HashMap tramiteReferencia  
	 * */
	public abstract boolean actualizarTramiteReferencia(Connection con, HashMap tramiteReferencia);
	
	/**
	 * Inserta un registro en la tabla tramite referencia 
	 * Connection con
	 * HashMap tramiteReferencia 
	 * */
	public abstract boolean insertarTramiteReferencia(Connection con, HashMap tramiteReferencia);
	
	/**
	 * Consulta instituciones tramite referencia
	 * @param Connection con
	 * @param HashMap parametros	 
	 * */
	public abstract HashMap consultarInstitucionesTramiteReferencia(Connection con, HashMap parametros);	
	
	
	/**
	 * Actualiza un registro en instituciones tramite referencia 
	 * @param Connection con
	 * @param HashMap Parametros 
	 * */
	public abstract boolean actualizarInstitucionesTramiteReferencia(Connection con, HashMap parametros);
	
	/**
	 * Inserta un registro en instituciones tramite referencia
	 * @param Connection 
	 * @param HashMap parametros
	 * */
	public abstract boolean insertarInstitucionesTramiteReferencia(Connection con, HashMap parametros);
	
	/**
	  * Consultar Servicios Instituciones Referencia 
	  * @param Connection con
	  * @param HashMap parametros
	  * */
	public abstract HashMap consultarServiciosInstitucionesReferencia(Connection con, HashMap parametros);
	
	/**
	 * Actualizar Servicios Instituciones Referencia
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public abstract boolean actualizarServiciosInstitucionesReferencia(Connection con, HashMap parametros);
	
	/**
	 * Insertar Servicios Instituciones Referencia
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public abstract boolean insertarServiciosInstitucionesReferencia(Connection con, HashMap parametros);
	
	/**
	  * Consultar Historial Servicios Instituciones Referencia 
	  * @param Connection con
	  * @param HashMap parametros
	  * */
	public abstract HashMap consultarHistorialServiciosInstitucionesReferencia(Connection con, HashMap parametros);
	
	/**
	  * Consultar Traslado Paciente 
	  * @param Connection con
	  * @param HashMap parametros
	  * */
	public abstract HashMap consultarTrasladoPaciente(Connection con, HashMap parametros);
	
	/**
	 * Actualizar Traslado Paciente 
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public abstract boolean actualizarTrasladoPaciente(Connection con, HashMap parametros);
	
	/**
	 * Insertar Traslado Paciente
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public abstract boolean insertarTrasladoPaciente(Connection con, HashMap parametros);
	
	/**
	  * Consultar Historial Traslado Paciente 
	  * @param Connection con
	  *	@param int numeroreferenciatramite
	  * @param int institucionsirc
	  * @param int institucion
	  * */
	public abstract HashMap consultarHistorialTrasladoPaciente(Connection con,int numeroreferenciatramite, String institucionsirc, int institucion );
}