/*
 * @(#)CamaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta el servicio
 * de acceso a datos para el objeto <code>Camas</code>.
 *
 * @version 1.0, Oct 03, 2003
 * @author 	<a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */

public interface CamasDao 
{
	/**
	 * Lista todas las camas con los atributos establecidos
	 * @param con
	 * @param centroAtencion
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSetDecorator listarCamas(Connection con,int centroAtencion) throws SQLException;
	
	/**
	 * Lista las camas del centro de costo dado
	 * @param con
	 * @param centroCosto
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSetDecorator listarCamasCentroCosto(Connection con, int centroCosto) throws SQLException;
	
	
	

	/**
	 * Consultar los traslados de las camas
	 * @param con
	 * @param numeroCama
	 * @param fechaTraslado
	 * @param horaTraslado
	 * @param usuario
	 * @return
	 */
	public Collection consultaTrasladoCamas(		Connection con, 
																				String numeroCama, 
																				String fechaInicialTraslado, 
																				String fechaFinalTraslado,
																				String horaInicialTraslado, 
																				String horaFinalTraslado,
																				String usuario);

	/**
	 * Método que carga los links pertenecientes a la consulta de traslado 
	 * con búsqueda por  paciente
	 * @param con, conexión con la fuente de datos
	 * @param codigoPaciente, código del paciente cargado en la sesión
	 * @return
	 * @throws SQLException
	 */
	public Collection linksConsultaTrasladoCamasPorPaciente(Connection con, int codigoPaciente) throws SQLException;
	
	/**
	 * Método que obtiene los datos pertinentes desde el link 
	 * see=linksConsultaTrasladoCamasPorPaciente para cargar 
	 * los datos de la consulta de traslado de camas
	 * @param con, conexión con la fuente de datos
	 * @param cuenta, cuenta del paciente
	 * @return
	 */
	public Collection busquedaConsultaTrasladoCamasPorPaciente (	Connection con,	int cuenta ) throws SQLException; 

																			
}