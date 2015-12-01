/*
 * @(#)ResponderCirugiasDao.java
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

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 02 /Nov/ 2005
 */
public interface ResponderCirugiasDao 
{
	/**
	 * Método que carga las peticiones de un paciente cargado en session
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public ResultSetDecorator cargarPeticionesPaciente(Connection con, int codigoPaciente, int institucionPaciente, int idIngreso, int consecutivoOrdenesMedicas) throws SQLException;
	
	/**
	 * Método para consultar las peticiones asociadas al usuario que ingreso en el sistema
	 * @param con
	 * @param usuario
	 * @param institucionUsuario
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarPeticionesMedico(Connection con, String usuario, int institucionUsuario, int centroCosto, int centroAtencion) throws SQLException;
	
	/**
	 * Método para cargar los servicios de la peticion
	 * @param con
	 * @param codigoPeticion
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarServiciosPeticion(Connection con, int codigoPeticion)  throws SQLException;
	
	/**
	 * Método que carga las peticiones de un paciente cargado en session
	 * que posean peticion
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public ResultSetDecorator cargarPeticionesPacienteConOrden(Connection con, int codigoPaciente, int institucionPaciente, int idCuenta, int consecutivoOrdenesMedicas)  throws SQLException;
	
}