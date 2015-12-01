/*
 * @(#)ExcepcionesServiciosDao
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;

import util.ResultadoBoolean;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 *  Interfaz para el acceder a la fuente de datos de una excepción de servicios 
 *
 * @version 1.0, Julio 21 / 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public interface ExcepcionesServiciosDao
{
	/**
	 * Inserción de una excepción de servicio
	 * @param con, connection con la fuente de datos
	 * @param servicio, código del servicio
	 * @param contrato, coigo del contrato
	 * @return 0 -1 (insert)
	 */
	public int  insertar(		Connection con, int servicio, int contrato);
	
	/**
	 * Insertar Transaccional
	 * @param con
	 * @param servicio
	 * @param contrato
	 * @param estado
	 * @return
	 */
	public  ResultadoBoolean insertarTransaccional(	Connection con,
																			int servicio, 
																			int contrato,
																			String estado)throws SQLException;
		
	/**
	 * Método que  carga  los datos de un resumen de excepcion de servicio
	 * en una BD PostgresSQL o Hsqldb 
	 */
	public ResultSetDecorator cargarResumen(Connection con, int servicio, int contrato) throws SQLException;
	
	public ResultSetDecorator hayRepetidas (Connection con, int servicio, int contrato ); 
	
	/**
	 * Método que elimina una excepcion de servicio  
	 * si el contrato tiene  fechas >= al sistema 
	 */
	public int eliminar(Connection con, int servicio, int contrato);
	
	/**
	 * Modifica el serviio de la excepción de un servicio
	 * @param con
	 * @param servicioNuevo
	 * @param contrato
	 * @param servicioAntiguo
	 * @return
	 */
	public int modificar(	Connection con, 
									int servicioNuevo, 
									int contrato, 
									int servicioAntiguo);	
	
	/**
	 * Modificación transaccional
	 * @param con
	 * @param servicioNuevo
	 * @param contrato
	 * @param servicioAntiguo
	 * @param estado
	 * @return ResultadoBoolean
	 */	
	public ResultadoBoolean modificarTransaccional(	Connection con, 
																				int servicioNuevo, 
																				int contrato, 
																				int servicioAntiguo, 
																				String estado)throws SQLException; 
	
	/**
	 * Método que contiene el Resulset de los datos de la tabla excepciones_servicios
	 * para posteriorm/t ser mostrados en el pager
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @return Resultset con todos los datos de la tabla excepciones_servicios
	 * @throws SQLException
	 */
	public ResultSetDecorator listado(Connection con) throws SQLException;
	
	/**
	 * Método que contiene el Resulset de todas las excepciones de servicios buscados
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @return Resultset con todos los datos de la tabla empresas
	 * @throws SQLException
	 */
	public ResultSetDecorator busqueda(	Connection con, 
												String nombreConvenio, 
												String descripcionServicio,
												String numeroContrato,
												int esposAux
											) throws SQLException;	
	
}
