/*
 * @(#)TerceroDaoDao.java
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
import java.util.ArrayList;

import util.ResultadoBoolean;
import util.Cargos.InfoDeudorTerceroDto;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.administracion.DtoConceptosRetencionTercero;

/**
 *  Interfaz para el acceder a la fuente de datos de un tercero
 *
 * @version 1.0, Junio 11 / 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson R�os</a>
 */
public interface TerceroDao 
{
	/**
	 * inserta un  tercero (nit)
	 * @param con, Connection, conexi�n abierta con una fuente de datos 
	 * @param numeroIdentificacion, String,  n�mero de id
	 * @param descripcion, String, descripci�n
	 * @param activa, boolean, estado activo/ inactivo del tercero
	 * @param institucion, instituci�n seg�n dado el usuarioBasico
	 * @return  1 si encuentra, 0 de lo contrario
	 */
	public  int  insertar (	Connection con,
										String numeroIdentificacion,
										String descripcion,
										boolean activa,
										int institucion,
										int codigoTipoTercero,
										String digitpVerificacion, 
										String direccion, String telefono);
	
	/**
	 * inserta un  tercero (nit) dentro de una transacci�n dado su estado.
	 * @param con, Connection, conexi�n abierta con una fuente de datos 
	 * @param numeroIdentificacion, String,  n�mero de id
	 * @param descripcion, String, descripci�n
	 * @param activa, boolean, estado activo/ inactivo del tercero
	 * @param institucion, instituci�n seg�n dado el usuarioBasico
	 * @return ResultadoBoolean, true si la inserci�n fue exitosa, false y con la descripci�n 
	 * de lo contrario
	 */
	public  ResultadoBoolean insertarTransaccional(	Connection con,
																						String numeroIdentificacion,
																						String descripcion,
																						boolean activa,
																						int institucion,
																						String estado) throws SQLException;

	/**
	 * M�todo que  carga  los datos de un tercero seg�n los datos
	 * que lleguen del codigo de tercero para mostrarlos en el resumen
	 * en una BD PostgresSQL o Hsqldb 
	 */
	public ResultSetDecorator cargarResumen(Connection con, int codigo); 
																																	
	/**Carga el �ltimo tercero insertado**/
	public ResultSetDecorator cargarUltimoCodigo(Connection con);
	
	/**
	 * modifica un  tercero (nit)
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param codigo, int, codigo asignado por el sistema al tercero
	 * @param numeroIdentificacion, String,  n�mero de id
	 * @param descripcion, String, descripci�n
	 * @param activa, boolean, estado activo/ inactivo del tercero
	 * @param institucion, instituci�n seg�n dado el usuarioBasico
	 * @return  1 si encuentra, 0 de lo contrario
	 */
	public  int modificar(		Connection con,
											int codigo,
											String numeroIdentificacion,
											String descripcion,
											int codigoTipoTercero,
											String digitoVerificacion, 
											boolean activa,
											String direccion, String telefono);

	/**
	 * modifica un  tercero (nit) dado su c�digo con los param�tros dados  dentro de una transacci�n dado su estado.
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param codigo, int, codigo asignado por el sistema al tercero
	 * @param numeroIdentificacion, String,  n�mero de id
	 * @param descripcion, String, descripci�n
	 * @param activa, boolean, estado activo/ inactivo del tercero
	 * @param institucion, instituci�n seg�n dado el usuarioBasico
	 * @param estado. String, estado dentro de la transacci�n 
	 * @return ResultadoBoolean, true si la modificaci�n fue exitosa, false y con la descripci�n 
	 * de lo contrario
	 */
	public  ResultadoBoolean modificarTransaccional(		Connection con,
																							int codigo,
																							String numeroIdentificacion,
																							String descripcion,
																							int codigoTipoTercero,
																							String digitoVerificacion, 
																							boolean activa,
																							String estado)  throws SQLException;

	/**
	 * M�todo que contiene el Resulset de todos los terceros
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param codigoInstitucion
	 * @return Resultset con todos los datos de la tabla terceros
	 * @throws SQLException
	 */
	public ResultSetDecorator listado(Connection con, int codigoInstitucion) throws SQLException;											
		
	/**
	 * M�todo que contiene el Resulset de todas los terceros buscados
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param codigoInstitucion
	 * @param numeroIdentificacion,
	 * @param descripcion
	 * @param activaAux
	 * @return Resultset con todos los datos de la tabla terceros
	 * @throws SQLException
	 */
	public ResultSetDecorator busqueda(		Connection con,
	        										int codigoInstitucion,
													String numeroIdentificacion,
													String descripcion,
													int codigoTipoTercero,
													String digitoVerificacion, 
													int activaAux) throws SQLException;
	
	/**
	 * Busca un tercero dado el tipo y numero de identificacion
	 * @param con
	 * @param numeroIdentificacion
	 * @return
	 * @throws SQLException
	 */
	public int busquedaExistenciaTercero(Connection con, String numeroIdentificacion) throws SQLException;
	
	public boolean insertarConceptoRetencion(DtoConceptosRetencionTercero dto, int tercero);
	
	public ArrayList<DtoConceptosRetencionTercero> consultarConceptosRetTercero(int tercero);

	public boolean modificarConceptoRetencion(DtoConceptosRetencionTercero dto);
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public ArrayList<InfoDeudorTerceroDto> cargarTerceroArray(InfoDeudorTerceroDto dto);

}
