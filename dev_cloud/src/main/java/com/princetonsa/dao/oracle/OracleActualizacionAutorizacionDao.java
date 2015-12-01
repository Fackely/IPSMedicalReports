/*
 * @(#)OracleActualizacionAutorizacionDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import com.princetonsa.dao.ActualizacionAutorizacionDao;
import com.princetonsa.dao.sqlbase.SqlBaseActualizacionAutorizacionDao;
import com.princetonsa.decorator.ResultSetDecorator;


/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 24 /May/ 2005
 */
public class OracleActualizacionAutorizacionDao implements ActualizacionAutorizacionDao 
{

	/**
	 * Método que carga los datos de una cuenta del paciente cargado en sesion y muestra
	 * las fechas de Admision o de apertura de la Cuenta segun sea su via de 
	 * Ingreso(urgencias-hospitalizacion o consulta extrena/ambulatorios)
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap cargarDatosPacienteCuenta (Connection con, int codigoPaciente) throws SQLException
	{
		return SqlBaseActualizacionAutorizacionDao.cargarDatosPacienteCuenta(con, codigoPaciente);
	}
	
	
	
	/**
	 * Método para actualizar el numero de autorizacion en una admision de hospitalizacion
	 * @param con
	 * @param numeroAutorizacion
	 * @param cuenta
	 * @return
	 */
	public  int modificarNumeroAutorizacionAdmisionUrgenciasTransaccional(Connection con, String numeroAutorizacion, int cuenta, String estado)
	{
		return SqlBaseActualizacionAutorizacionDao.modificarNumeroAutorizacionAdmisionUrgenciasTransaccional(con, numeroAutorizacion, cuenta, estado);
	}
	
	
	
	/**
	 * Método para actualizar el numero de autorizacion de todas las solicitudes
	 * asociadas a una cuenta
	 * @param con
	 * @param numeroAutorizacion
	 * @param cuenta
	 * @return
	 */
	public  int modificarNumeroAutorizacionTodasSolicitudesXCuentaTransaccional(Connection con, String numeroAutorizacion, int cuenta, String estado) 
	{
		return SqlBaseActualizacionAutorizacionDao.modificarNumeroAutorizacionTodasSolicitudesXCuentaTransaccional(con, numeroAutorizacion, cuenta, estado);
	}
	
	
	/**
	 * Método para actualizar el numero de autorizacion en una admision de hospitalizacion
	 * @param con
	 * @param numeroAutorizacion
	 * @param cuenta
	 * @return
	 */
	public  int modificarNumeroAutorizacionAdmisionHospitalizacionTransaccional(Connection con, String numeroAutorizacion, int cuenta, String estado) 
	{
		return SqlBaseActualizacionAutorizacionDao.modificarNumeroAutorizacionAdmisionHospitalizacionTransaccional(con , numeroAutorizacion, cuenta, estado);
	}
	
	/**
	 *  Consulta todas las ordenes medicas de una cuenta
	 * @param con
	 * @param cuenta
	 * @param contrato
	 * @return
	 */
	public ResultSetDecorator cargarSolicitudesOrden(Connection con, int cuenta,int contrato)
	{
		return SqlBaseActualizacionAutorizacionDao.cargarSolicitudesOrden(con, cuenta,contrato);
	}
	
	/**
	 *  Consulta todas los medicamentos de una cuenta
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap cargarMedicamentos(Connection con, int cuenta)
	{
		return SqlBaseActualizacionAutorizacionDao.cargarMedicamentos(con, cuenta);
	}
	
	/**
	 * Método para actualizar el numero de autorizacion de todas las solicitudes que NO sean de medicamentos
	 * @param con
	 * @param numeroAutorizacion
	 * @param numeroSolicitud
	 * @return
	 */
	public  int modificarNumeroAutorizacionSolicitudes(Connection con, String numeroAutorizacion, int numeroSolicitud) 
	{
		return SqlBaseActualizacionAutorizacionDao.modificarNumeroAutorizacionSolicitudes(con,numeroAutorizacion, numeroSolicitud);
	}
	
	
	/**
	 * Método para actualizar el numero de autorizacion de todas las solicitudes que SOLO sean de medicamentos
	 * @param con
	 * @param numeroAutorizacion
	 * @param numeroSolicitud
	 * @return
	 */
	public int modificarNumeroAutorizacionMedicamentos(Connection con, String numeroAutorizacion, int numeroSolicitud) 
	{
		return SqlBaseActualizacionAutorizacionDao.modificarNumeroAutorizacionMedicamentos(con,numeroAutorizacion, numeroSolicitud);
	}
	
	/**
	 * Metodo para la busqueda Avanzada
	 * @param con
	 * @param cuenta
	 * @param fechaSolicitud
	 * @param consecutivo
	 * @param descripcionServicio
	 * @param numeroAutorizacion
	 * @param contrato
	 * @param esServicio
	 * @return
	 */
	public ResultSetDecorator busquedaAvanzada(Connection con, int cuenta, String fechaSolicitud,String horaSolicitud, String consecutivo, String descripcionServicio, String numeroAutorizacion, int contrato, boolean esServicio)
	{
		return SqlBaseActualizacionAutorizacionDao.busquedaAvanzada(con,cuenta, fechaSolicitud, horaSolicitud, consecutivo,descripcionServicio,numeroAutorizacion,contrato,esServicio);
	}
	
	/**
	 * Método que modifica el numero de autorizacion de una cirugia
	 * @param con
	 * @param numeroAutorizacion
	 * @param codigoCirugia
	 * @return
	 */
	public int modificarNumeroAutorizacionCirugia(Connection con,String numeroAutorizacion,int codigoCirugia)
	{
		return SqlBaseActualizacionAutorizacionDao.modificarNumeroAutorizacionCirugia(con,numeroAutorizacion,codigoCirugia);
	}
}
