/*
 * @(#)OracleTerceroDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.administracion.DtoConceptosRetencionTercero;

import java.sql.SQLException;
import java.util.ArrayList;

import util.ResultadoBoolean;
import util.Cargos.InfoDeudorTerceroDto;

import com.princetonsa.dao.TerceroDao;
import com.princetonsa.dao.sqlbase.SqlBaseTerceroDao;

/**
 * Implementación Oracle de las funciones de acceso a la fuente de datos
 * para un tercero
 *
 * @version 1.0, Junio 11 / 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class OracleTerceroDao implements TerceroDao 
{
	/**
	 * Inserta un tercero
	 */
	private final static String insertarTerceroStr= 	"INSERT INTO terceros " +
																	"(" +
																		" codigo, " +
																		" numero_identificacion, " +
																		" descripcion, " +
																		" institucion," +
																		" activo, " +
																		" tipo_tercero, " +
																		" digito_verificacion, " +
																		" direccion, " +
																		" telefono " +
																	" ) " +
																	"VALUES (" +
																		" ?, " +
																		" ?," +
																		" ?," +
																		" ?," +
																		" ?," +
																		" ?," +
																		" ?, " +
																		" ?," +
																		" ? )"; 

	/**
	 * inserta un  tercero (nit)
	 * @param con, Connection, conexión abierta con una fuente de datos 
	 * @param numeroIdentificacion, String,  número de id
	 * @param descripcion, String, descripción
	 * @param activa, boolean, estado activo/ inactivo del tercero
	 * @param institucion, institución según dado el usuarioBasico
	 * @return  1 si encuentra, 0 de lo contrario
	 */
	public  int  insertar (	Connection con,
							String numeroIdentificacion,
							String descripcion,
							boolean activa,
							int institucion,
							int codigoTipoTercero,
							String digitoVerificacion,
							String direccion, String telefono)
	{
		return SqlBaseTerceroDao.insertar(con,numeroIdentificacion,descripcion,activa,institucion, insertarTerceroStr, codigoTipoTercero, digitoVerificacion, direccion, telefono);											
	}
	
	/**
	 * inserta un  tercero (nit) dentro de una transacción dado su estado.
	 * @param con, Connection, conexión abierta con una fuente de datos 
	 * @param numeroIdentificacion, String,  número de id
	 * @param descripcion, String, descripción
	 * @param activa, boolean, estado activo/ inactivo del tercero
	 * @param institucion, institución según dado el usuarioBasico
	 * @return ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
	 * de lo contrario
	 */
	public  ResultadoBoolean insertarTransaccional(	Connection con,
																						String numeroIdentificacion,
																						String descripcion,
																						boolean activa,
																						int institucion,
																						String estado) throws SQLException
	{
		return SqlBaseTerceroDao.insertarTransaccional (	con,numeroIdentificacion,
																								descripcion,activa,institucion,
																								estado, insertarTerceroStr);
	}	

	/**
	 * Método que  carga  los datos de un tercero según los datos
	 * que lleguen del codigo de tercero para mostrarlos en el resumen
	 * en una BD PostgresSQL o Hsqldb 
	 */
	public ResultSetDecorator cargarResumen(Connection con, int codigo) 
	{
		return SqlBaseTerceroDao.cargarResumen(con,codigo);
	}	

	/**Carga el último tercero insertado**/
	public ResultSetDecorator cargarUltimoCodigo(Connection con)
	{
		return SqlBaseTerceroDao.cargarUltimoCodigo(con);
	}
	
	/**
	 * modifica un  tercero (nit)
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param codigo, int, codigo asignado por el sistema al tercero
	 * @param numeroIdentificacion, String,  número de id
	 * @param descripcion, String, descripción
	 * @param activa, boolean, estado activo/ inactivo del tercero
	 * @param institucion, institución según dado el usuarioBasico
	 * @return  1 si encuentra, 0 de lo contrario
	 */
	public  int modificar(		Connection con,
											int codigo,
											String numeroIdentificacion,
											String descripcion,
											int codigoTipoTercero,
											String digitoVerificacion,
											boolean activa,
											String direccion, String telefono)
	{
		return SqlBaseTerceroDao.modificar(	con,codigo,numeroIdentificacion,descripcion,codigoTipoTercero,digitoVerificacion,activa, direccion, telefono);											
	}
													
	/**
	 * modifica un  tercero (nit) dado su código con los paramétros dados  dentro de una transacción dado su estado.
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param codigo, int, codigo asignado por el sistema al tercero
	 * @param numeroIdentificacion, String,  número de id
	 * @param descripcion, String, descripción
	 * @param activa, boolean, estado activo/ inactivo del tercero
	 * @param institucion, institución según dado el usuarioBasico
	 * @param estado. String, estado dentro de la transacción 
	 * @return ResultadoBoolean, true si la modificación fue exitosa, false y con la descripción 
	 * de lo contrario
	 */
	public  ResultadoBoolean modificarTransaccional(		Connection con,
																							int codigo,
																							String numeroIdentificacion,
																							String descripcion,
																							int codigoTipoTercero,
																							String digitoVerificacion, 
																							boolean activa,
																							String estado) throws SQLException
	{
		return SqlBaseTerceroDao.modificarTransaccional (	con,codigo,numeroIdentificacion,descripcion,codigoTipoTercero,digitoVerificacion,activa, estado);																							
	}

	/**
	 * Método que contiene el Resulset de todos los terceros
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param codigoInstitucion
	 * @return Resultset con todos los datos de la tabla terceros
	 * @throws SQLException
	 */
	public ResultSetDecorator listado(Connection con, int codigoInstitucion) throws SQLException
	{
	    return SqlBaseTerceroDao.listado(con, codigoInstitucion);
	}
		
	/**
	 * Método que contiene el Resulset de todas los terceros buscados
	 * @param con, Connection, conexión abierta con una fuente de datos
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
													int activaAux) throws SQLException
	{
	    return SqlBaseTerceroDao.busqueda(con, codigoInstitucion, numeroIdentificacion, descripcion, codigoTipoTercero, digitoVerificacion, activaAux);
	}
	
	/**
	 * Busca un tercero dado el tipo y numero de identificacion
	 * @param con
	 * @param numeroIdentificacion
	 * @return
	 * @throws SQLException
	 */
	public int busquedaExistenciaTercero(Connection con, String numeroIdentificacion) throws SQLException
	{
	    return SqlBaseTerceroDao.busquedaExistenciaTercero(con, numeroIdentificacion);
	}
	
	public boolean insertarConceptoRetencion(DtoConceptosRetencionTercero dto, int tercero)
	{
		return SqlBaseTerceroDao.insertarConceptoRetencion(dto, tercero);
	}
	
	public ArrayList<DtoConceptosRetencionTercero> consultarConceptosRetTercero(int tercero) 
	{
		return SqlBaseTerceroDao.consultarConceptosRetTercero(tercero);
	}

	@Override
	public boolean modificarConceptoRetencion(DtoConceptosRetencionTercero dto) {
		return SqlBaseTerceroDao.modificarConceptoRetencion(dto);
	}

	@Override
	public ArrayList<InfoDeudorTerceroDto> cargarTerceroArray(InfoDeudorTerceroDto dto) {
		
		return SqlBaseTerceroDao.cargarTerceroArray(dto);
	}
	
	
}
