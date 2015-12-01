/*
 * @(#)TrasladoCamasDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 07 /Jul/ 2005
 */
public interface TrasladoCamasDao 
{

	/**
	 * Método que carga la cama actual del paciente dado el Codigo del paciente
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public  ResultSetDecorator cargarCamaActualPaciente(Connection con, int codigoPaciente)throws SQLException;
	
	/**
	 * Método para la insercion de los datos de la nueva cama asiganda al paciente
	 * @param con
	 * @param fecha
	 * @param hora
	 * @param nuevaCama
	 * @param nuevaHabitacion
	 * @param camaAntigua
	 * @param habitacionAntigua
	 * @param institucion
	 * @param fechaGrabacion
	 * @param horaGrabacion
	 * @param usuario
	 * @return
	 * @throws SQLException
	 */
	public int insertarTrasladoCamaPaciente(Connection con, String fecha_asignacion, String hora_asignacion, int codigoNuevaCama,  int codigoCamaAntigua, int institucion, String usuario, int codigoPaciente, int cuenta, int convenio, String observacion) throws SQLException; 
	
	/**
	 *  Método para la insercion en la base de datos de un traslado de cama
	 * @param con
	 * @param fechaAsignacion
	 * @param horaAsignacion
	 * @param codigoInstitucion
	 * @param loginUsuario
	 * @param codigoPaciente
	 * @param codigoConvenio
	 * @param fechaFinalizacion
	 * @param horaFinalizacion
	 * @param codigoCuenta
	 * @param codigoNuevaCama
	 * @param codigoCamaAntigua
	 * @return
	 * @throws SQLException
	 */
	public int insertarTrasladoCama(	Connection con, 
	        											String fechaAsignacion, 
	        											String horaAsignacion, 
	        											int codigoInstitucion, 
	        											String loginUsuario, 
	        											int codigoPaciente, 
	        											int codigoConvenio, 
	        											String fechaFinalizacion, 
	        											String horaFinalizacion, 
	        											int codigoCuenta,
	        											int codigoNuevaCama,
	        											int codigoCamaAntigua) throws SQLException;
	
	/**
	 * Mètodo para buscar los traslados de cama por centro de costo
	 * @param con
	 * @param codigoCentroCosto
	 * @return
	 */
	public ResultSetDecorator cargarTrasladosArea (Connection con, int codigoCentroCosto) throws SQLException;
	
	
	
	/**
	 * Método que carga los traslados de cama de un paciente cargado en session
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public ResultSetDecorator cargarTrasladosPaciente(Connection con, int codigoPaciente)  throws SQLException;
	
		
	/**
	 * Método apra cargar el detalle de un traslado de un paciente
	 * @param con
	 * @param codigoTraslado
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarDetalleTrasladoPaciente(Connection con, int codigoTraslado)  throws SQLException;
	
	
	/**
	 * Método para cargar las ingresos anteriores del paciente
	 * @param con
	 * @param codigoPaciente
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarIngresosAnteriores(Connection con, int codigoPaciente)  throws SQLException;
	
	
	/**
	 * Método para buscar los traslados por Fecha
	 * @param con
	 * @param fechaTraslado
	 * @param centroAtencion
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarTrasladosFecha (Connection con,String fechaTraslado,int centroAtencion) throws SQLException;
	
	

	/**
	 * Método para actualizar el estado de una cama 
	 * @param con
	 * @param estadoCama
	 * @param codigoCama
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public int modificarEstadoCama (Connection con,int estadoCama,int  codigoCama, int institucion) throws SQLException;
	
	
	/**
	 * Método apra cargar el detalle de un traslado anterior de un paciente dada su cuenta
	 * @param con
	 * @param cuenta
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarDetalleTrasladoAnteriorPaciente(Connection con, int cuenta)  throws SQLException;
	
	/**
	 * Metodo que actualiza la fecha - hora de finalización de la estancia de un paciente en una cama
	 * @param con
	 * @param codigoCuenta
	 * @param fechaFin
	 * @param horaFin
	 * @return
	 */
	public  boolean  actualizarFechaHoraFinalizacion(	Connection con, 
																						int codigoCuenta, 
																						String fechaFin,  
																						String horaFin,
																						String observaciones
																					); 
	

	
	/**
	 * Metodo que actualiza la fecha y hora de finalizacion de ocupacion de una cama
	 * @param con
	 * @param codigoCuenta
	 * @param fechaFinalizacion
	 * @param horaFinalizacion
	 * @return
	 * @throws SQLException
	 */
	public boolean actualizarFechaHoraFinalizacionNoTransaccional (Connection con, int codigoCuenta, String fechaFinalizacion, String horaFinalizacion, String observaciones) throws SQLException;
	
	/**
	 *Metodo encargdo de cambiar el estado de la cama,
	 *este devuelve true para indicar operacion exitosa
	 *de lo contrario devuelve false.
	 *El HashMap aprametros contiene las siguientes key's
	 *--------------------------------------------------
	 *--nuevoEstadoCama --> Requerido
	 *--institucion --> Requerido
	 *--codigoCama --> Requerido
	 *--codigoPaciente --> Requerido
	 *--estadoCama --> Requerido
	 *-------------------------------------------------- 
	 * @param connection
	 * @param parametros
	 * @return
	 */
	public  boolean cambiarEstaReserva (Connection connection,HashMap parametros);
	
	
	
	
}