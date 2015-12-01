/*
 * @(#)ClasificacionSocioEconomicaDao
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
 *  Interfaz para el acceder a la fuente de datos de una  clasificaci�n sociecon�mica
 *
 * @version 1.0, Junio 30 / 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson R�os</a>
 */
public interface ClasificacionSocioEconomicaDao 
{
	/**
	 * Inserta una clasificaci�n social
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param descripcion, String, descripcion de la calasificaci�n social
	 * @param tipoRegimen, String, r�gimen de acuerdo a los previam/t
	 * 				ingresados en el sistema
	 * 				que utiliza la clasificacion social
	 * @param activa, boolean
	 * @return int, 0 si  no inserta, 1 si inserta
	 */
	public int  insertar(	Connection con,
										String descripcion,
										String tipoRegimen,
										boolean activa,
										int institucion);
										
	/**
	 *  Inserta una clasificaci�n socioecon�mica dentro de una transacci�n dado su estado.
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param descripcion, String, descripcion de la calasificaci�n social
	 * @param tipoRegimen, String, r�gimen de acuerdo a los previam/t
	 * 				ingresados en el sistema
	 * @param activa, boolean	 
	 * @param estado. String, estado dentro de la transacci�n 
	 * @return  ResultadoBoolean, true si la inserci�n fue exitosa, false y con la descripci�n 
	 * 				de lo contrario
	 */																					
	public  ResultadoBoolean insertarTransaccional(	Connection con,
																						String descripcion,
																						String tipoRegimen,
																						boolean activa,
																						int institucion,
																						String estado) throws SQLException;
	
	/**
	 * M�todo que  carga  los datos de una clasifcaci�n socioecon�mica 
	 * seg�n los datos que lleguen del  c�digo de la tabla estratos_sociales 
	 * para mostrarlos en el resumen
	 * en una BD PostgresSQL o Hsqldb 
	 */
	public ResultSetDecorator cargarResumen(Connection con, int codigo) throws SQLException;
									
	/**Carga el �ltima clasificaci�n socioecon�mica insertada**/
	public ResultSetDecorator cargarUltimoCodigo(Connection con);

	/**
	 * Modificar una clasificaci�n socioecon�mica
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param descripcion, String, descripcion de la calasificaci�n social
	 * @param activa, boolean
	 * @return int, 0 si  no inserta, 1 si inserta
	 */
	public int modificar(	Connection con, 
										int codigo, 
										String descripcion,
										String tipoRegimen,
										boolean activa); 

	/**
	 * Modifica una clasificaci�n socioecon�mica dado su c�digo con los 
	 * param�tros dados  dentro de una transacci�n dado su estado. 
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param descripcion, String, descripcion de la calasificaci�n social
	 * @param activa, boolean	 
	 * @param estado. String, estado dentro de la transacci�n 
	 * @return  ResultadoBoolean, true si la inserci�n fue exitosa, false y con la descripci�n 
	 * 				de lo contrario
	 */
	public ResultadoBoolean modificarTransaccional(	Connection con, 
																						int codigo, 
																						String descripcion,
																						String tipoRegimen,
																						boolean activa,
																						String estado) throws SQLException; 
	
	/**
	 * M�todo que contiene el Resulset de los datos de la tabla estratos_sociales
	 * para posteriorm/t ser mostrados en el pager
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param , int, institucion, Codigo de la institucion a la que pertenece el usuario para filtrar la consulta
	 * @return Resultset con todos los datos de la tabla estratos_sociales
	 * @throws SQLException
	 */
	public  ResultSetDecorator listado(Connection con, int institucion) throws SQLException;
	
	/**
	 * B�squeda Avanzada de un estrato 
	 * @param con
	 * @param descripcion
	 * @param nombreTipoRegimen
	 * @param activaAux
	 * @param  int, institucion, Codigo de la institucion a la que pertenece el usuario para filtrar la consulta
	 * @return ResulSet con el resultado de la b�squeda
	 * @throws SQLException
	 */
	public ResultSetDecorator busqueda(	Connection con,
													int codigo,
													String descripcion,
													String nombreTipoRegimen,
													int activaAux,
													int institucion) throws SQLException;
}
