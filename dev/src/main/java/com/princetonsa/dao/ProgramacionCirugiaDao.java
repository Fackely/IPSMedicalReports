/*
 * Creado el 18-nov-2005
 * por Julian Montoya
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author Julian Montoya
 * 
 * Princeton S.A. (ParqueSoft Manizales)
 */
public interface ProgramacionCirugiaDao {
	

	/**
	 * Metodo para retornar la información del encabezado de la peticion de cirugia  
	 * @param con
	 * @param nroPeticion
	 * @param int tipoConsulta 
	 * @return
	 */
	public Collection cargarInformacionPeticion(Connection con, int nroPeticion, int tipoConsulta);

	/**
	 * Metodo para retornar la información de las cirugias asociadas... 
	 * @param con
	 * @param nroPeticion
	 * @return
	 */
	public Collection cargarInformacionCirugias(Connection con, int nroPeticion);
	

	 /**
	 * Metodo para retornar la información de las salas 
	 * @param con
	 * @return
	 */
	public Collection cargarSalas(Connection con);

	/**
	 * Metodo para listar las peticiones por paciente especifico con estado pendiente
	 * @param con
	 * @param codigoPersona
	 * @param centroAtencion 
	 * @param codigoCuenta 
	 * @return
	 */
	public Collection cargarListadoPeticionesPaciente(Connection con, int codigoPersona, int centroAtencion, int codigoCuenta);

	/**
	 * Consultar los servicios asociadas a las peticiones...
	 * @param con
	 * @param codigoPersona
	 * @param centroAtencion 
	 * @param codigoCuenta 
	 * @return
	 */
	public Collection cargarInformacionServiciosPeticion(Connection con, int codigoPersona, int centroAtencion, int codigoCuenta);

	/**
	 *  Metodo para listar las peticiones de acuerdo a unos parametros de busqueda
	 * @param con
	 * @param nroIniServicio
	 * @param nroFinServicio
	 * @param fechaIniPeticion
	 * @param fechaFinPeticion
	 * @param fechaIniCirugia
	 * @param fechaFinCirugia
	 * @param profesionalSolicita
	 * @return
	 */
	public Collection cargarListadoPeticionesBusqueda(Connection con, int nroIniServicio, int nroFinServicio,
													  String fechaIniPeticion, String fechaFinPeticion, String fechaIniCirugia, 
													  String fechaFinCirugia, int profesionalSolicita);

	/**
	 *  Metodo para listar las peticiones de acuerdo a unos parametros de busqueda
	 * @param con
	 * @param nroIniServicio
	 * @param nroFinServicio
	 * @param fechaIniPeticion
	 * @param fechaFinPeticion
	 * @param fechaIniCirugia
	 * @param fechaFinCirugia
	 * @param profesionalSolicita
	 * @param centroAtencion 
	 * @return
	 */
	public HashMap cargarListadoConsultaPeticionesBusqueda(Connection con, int nroIniServicio, int nroFinServicio,
													  String fechaIniPeticion, String fechaFinPeticion, String fechaIniCirugia, 
													  String fechaFinCirugia, int profesionalSolicita, int centroAtencion);


	/**
	 *  Metodo para listar las peticiones de acuerdo a unos parametros de busqueda (solo programadas y reprogramadas)
	 * @param con
	 * @param nroIniServicio
	 * @param nroFinServicio
	 * @param fechaIniPeticion
	 * @param fechaFinPeticion
	 * @param fechaIniCirugia
	 * @param fechaFinCirugia
	 * @param profesionalSolicita
	 * @return
	 */
	public Collection accionResultadoBusquedaAvanzadaProgramadas(Connection con, int nroIniServicio, int nroFinServicio,
																 String fechaIniPeticion, String fechaFinPeticion, String fechaIniCirugia, 
																 String fechaFinCirugia, int profesionalSolicita);


	/**
	 *  Metodo para listar los servicios de las peticiones que se consulten de acuerdo a unos parametros de busqueda
	 * @param con
	 * @param nroIniServicio
	 * @param nroFinServicio
	 * @param fechaIniPeticion
	 * @param fechaFinPeticion
	 * @param fechaIniCirugia
	 * @param fechaFinCirugia
	 * @param profesionalSolicita
	 * @return
	 */
	public Collection cargarInformacionServiciosBusqueda(Connection con, int nroIniServicio, int nroFinServicio,
													  	 String fechaIniPeticion, String fechaFinPeticion, String fechaIniCirugia, 
														 String fechaFinCirugia, int profesionalSolicita);

	
	/**
 	 * Metodo para listar los servicios de las peticiones. Para la busqueda avanzada de la funcionalidad de consulta de cirugias programadas
	 * @param con
	 * @return
	 */
	public HashMap cargarListadoConsultaServiciosBusqueda(Connection con, int nroIniServicio, int nroFinServicio,
													  	 String fechaIniPeticion, String fechaFinPeticion, String fechaIniCirugia, 
														 String fechaFinCirugia, int profesionalSolicita);

	
	/**
	 *  Metodo para listar los servicios de las peticiones que se consulten de acuerdo a unos parametros de busqueda 
	 * @param con
	 * @param nroIniServicio
	 * @param nroFinServicio
	 * @param fechaIniPeticion
	 * @param fechaFinPeticion
	 * @param fechaIniCirugia
	 * @param fechaFinCirugia
	 * @param profesionalSolicita
	 * @return
	 */
	public Collection cargarInformacionServiciosBusquedaProgramadas(Connection con, int nroIniServicio, int nroFinServicio,
																	String fechaIniPeticion, String fechaFinPeticion, String fechaIniCirugia, 
																	String fechaFinCirugia, int profesionalSolicita);
	
	
	/**
	 * Metodo para insertar la programacion de una sala a una hora determinada
	 * @param con
	 * @param numeroPeticion
	 * @param fechaProgramacion
	 * @param nroSala
	 * @param horaInicioProgramacion
	 * @param horaFinProgramacion
	 * @param loginUsuario
	 * @param operacion
	 * @return
	 */
	
	public int insertarProgamacion(Connection con, int numeroPeticion, String fechaProgramacion, int nroSala, String horaInicioProgramacion, String horaFinProgramacion, String loginUsuario, int operacion);

	/**
	 *  Metodo para cargar la programacion regsitrada de cada sala
	 * @param con
	 * @param fechaProgramacion
	 * @return
	 */
	public Collection cargarProgramacionSalas(Connection con, String fechaProgramacion);

	/**
	 * Metodo para cargar listado de las peticiones de programadas y reprogramadas
	 * @param con
	 * @param nroPeticion
	 * @return
	 */
	public Collection cargarListadoPeticionesProgramadas(Connection con, int centroAtencion);

	/**
	 * Metodo para listar las peticiones de acuerdo a unos parametros de busqueda en la Reprogramacion de Cirugias   
	 * @param con
	 * @param nroIniServicio
	 * @param nroFinServicio
	 * @param estadoPeticion
	 * @param profesional
	 * @param fechaIniCirugia
	 * @param fechaFinCirugia
	 * @return
	 */
	public Collection cargarListadoPeticionesBusquedaReprogramacion(Connection con, int nroIniServicio, int nroFinServicio, String estadoPeticion,
																					int profesional, String fechaIniCirugia, String fechaFinCirugia);

	/**
	 * Metodo para listar los servicios de las peticiones de acuerdo a unos parametros de busqueda en la Reprogramacion de Cirugias
	 * @param con
	 * @param nroIniServicio
	 * @param nroFinServicio
	 * @param estadoPeticion
	 * @param profesional
	 * @param fechaIniCirugia
	 * @param fechaFinCirugia
	 * @return
	 */
	public Collection cargarInformacionServiciosPeticionReprogramacion(Connection con, int nroIniServicio, int nroFinServicio, String estadoPeticion,
																	   				   int profesional, String fechaIniCirugia, String fechaFinCirugia);

	/**
	 * Metodo para dejar disponible la peticion de cirugia de nuevo y eliminar la programacion de la misma
	 * @param con
	 * @param numeroPeticion
	 * @return
	 */
	public int eliminarProgramacion(Connection con, int numeroPeticion);

	/**
	 * Metodo para consultar la peticion
	 * @param con
	 * @param numeroPeticion
	 * @return
	 */
	public HashMap consultaProgramacionSalasQx(Connection con, int numeroPeticion);
	
	/**
	 * 
	 * @param con
	 * @param rep
	 * @return
	 */
	public int insertarCancelacionProgramacionSalasQx(Connection con, HashMap rep);
	
	/**
	 * Metodo para cargar el codigo de la persona.
	 * @param con
	 * @param nroPeticion
	 * @return
	 */
	public Collection cargarCodigoPersona(Connection con, int nroPeticion);

	/**
	 * Metodo para cargar listado de las peticiones para la funcionalidad Consulta de programacaion de Cirugias.
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public HashMap cargarListadoPeticiones(Connection con, int codigoPersona);

	/**
	 * Metodo para cargar listado de servicios para la funcionalidad Consulta de programacaion de Cirugias.
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public HashMap cargarServiciosPeticion(Connection con, int codigoPersona);
}
