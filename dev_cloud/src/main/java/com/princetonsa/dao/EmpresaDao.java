/*
 * @(#)EmpresaDao.java
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
import java.util.HashMap;

import util.ResultadoBoolean;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Interfaz para el acceder a la fuente de datos de una empresa
 *
 * @version 1.0, Abril 29 / 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public interface EmpresaDao
{
	/**
	 * Inserta una empresa
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param tercero, int,  nit de la empresa
	 * @param razonSocial. String, razón social de la empresa
	 * @param nombreContacto. String, nombre del contacto de la empresa
	 * @param telefono. String, teléfono de la empresa
	 * @param direccion. String, dirección de la empresa
	 * @param correo. String, correo electrónico de la empresa
	 * @param activa. boolean, si la empresa está activa en el sistema o no
	 * @return int 1 si inserta, 0 de lo contrario
	 */
	public int  insertar	(	Connection con,
										int tercero,		
										String razonSocial,
										String telefono,
										String direccion,
										String correo,
										boolean activa,
										String direccionCuentas,
										String direccionSucursal,
										String telefonoSucursal,
										String representante,
										String observaciones,
										String paisPrincipal,
										String ciudadPrincipal,
										String paisCuentas,
										String ciudadCuentas,
										String deptoPrincipal,
										String deptoCuentas,
										String faxSedePrincipal,
										String faxSucursalLocal,
										String direccionTerritorial,
										String numeroAfiliados,
										double nivelIngreso,
										int formaPago);
										
															
	/**
	 * Inserta una empresa dentro de una transacción dado su estado.
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param tercero, int,  nit de la empresa
	 * @param razonSocial. String, razón social de la empresa
	 * @param nombreContacto. String, nombre del contacto de la empresa
	 * @param telefono. String, teléfono de la empresa
	 * @param direccion. String, dirección de la empresa
	 * @param correo. String, correo electrónico de la empresa
	 * @param activa. boolean, si la empresa está activa en el sistema o no
	 * @param estado. String, estado dentro de la transacción
	 * @return ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
	 * de lo contrario
	 */
	public ResultadoBoolean insertarTransaccional(	Connection con,
																					int tercero,	
																					String razonSocial,
																					String telefono,
																					String direccion,
																					String correo,
																					boolean activa,
																					String direccionCuentas,
																					String direccionSucursal,
																					String telefonoSucursal,
																					String representante,
																					String obsevaciones,
																					String paisPrincipal,
																					String ciudadPrincipal,
																					String paisCuentas,
																					String ciudadCuentas,
																					String estado,
																					String deptoPrincipal,
																					String deptoCuentas,
																					String faxSedePrincipal,
																					String faxSucursalLocal,
																					String direccionTerritorial,
																					String numeroAfiliados,
																					double nivelIngreso,
																					int formaPago)throws SQLException;

	/**
	 * Modifica una empresa dado su código con los paramétros dados.
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param codigo. int, código de la empresa
	 * @param tercero, int,  nit de la empresa
	 * @param razonSocial. String, razón social de la empresa
	 * @param nombreContacto. String, nombre del contacto de la empresa
	 * @param telefono. String, teléfono de la empresa
	 * @param direccion. String, dirección de la empresa
	 * @param correo. String, correo electrónico de la empresa
	 * @param activa. boolean, si la empresa está activa en el sistema o no
	 * @return 1 si inserta, 0 de lo contrario
	 */																					
	public int modificar(	Connection con,
							int codigo,
							int tercero,
							String razonSocial,
							String telefono,
							String direccion,
							String correo,
							String direccionCuentas,
							String direccionSucursal,
							String telefonoSucursal,
							String representante,
							String observaciones,
							String paisPrincipal,
							String ciudadPrincipal,
							String paisCuentas,
							String ciudadCuentas,
							String deptoPrincipal,
							String deptoCuentas,
							boolean activa,
							String codigoCiudadPrincipal,
							String codigoCiudadCuentas,
							String codigoPaisCuentas,
							String codigoPaisPrincipal,
							String faxSedePrincipal,
							String faxSucursalLocal,
							String direccionTerritorial,
							String numeroAfiliados,
							double nivelIngreso,
							int formaPago);
																																				
	/**
	 * Modifica una empresa dado su código con los paramétros dados  dentro de una transacción dado su estado.
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param codigo. int, código de la empresa
	 * @param tercero, int,  nit de la empresa
	 * @param razonSocial. String, razón social de la empresa
	 * @param nombreContacto. String, nombre del contacto de la empresa
	 * @param telefono. String, teléfono de la empresa
	 * @param direccion. String, dirección de la empresa
	 * @param correo. String, correo electrónico de la empresa
	 * @param activa. boolean, si la empresa está activa en el sistema o no
	 * @param estado. String, estado dentro de la transacción 
	 * @return ResultadoBoolean, true si la modificación fue exitosa, false y con la descripción 
	 * de lo contrario
	 */																					
	public ResultadoBoolean modificarTransaccional(	Connection con,
													int codigo,
													int tercero,
													String razonSocial,
													String telefono,
													String direccion,
													String correo,
													boolean activa,
													String direccionCuentas,
													String direccionSucursal,
													String telefonoSucursal,
													String representante,
													String obsevaciones,
													String paisPrincipal,
													String ciudadPrincipal,
													String paisCuentas,
													String ciudadCuentas,
													String estado,
													String deptoPrincipal,
													String deptoCuentas,
													String faxSedePrincipal,
													String faxSucursalLocal,
													String direccionTerritorial,
													String numeroAfiliados,
													double nivelIngreso,
													int formaPago) throws SQLException;

	/**
	 * Método que  carga  los datos de una empresa según los datos
	 * que lleguen del nit o tercero para mostrarlos en el resumen
	 * en una BD PostgresSQL o Hsqldb 
	 */
	public ResultSetDecorator cargarResumen(Connection con, int tercero) throws SQLException;
	
	/**
	 * Método que devuelve los resultados de la búsqueda avanzada
	 * en una BD PostgresSQL o Hsqldb 
	 */
	public ResultSetDecorator busqueda(	Connection con,
													String nit,
													String descripcionTercero,		
													int tercero,
													String razonSocial,
													String nombreContacto,
													String telefono,
													String direccion,
													String correo,
													int activaAux, 
													int codigoInstitucion,
													String ciudadPrincipal,
													String numeroAfiliados,
													double nivelIngreso,
													int formaPago) throws SQLException;
	
	/**
	 * Método que contiene el Resulset que contiene toda la información de la
	 * tabla empresas
	 * @param con, Conexión con la fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public  ResultSetDecorator listado(Connection con, int codigoInstitucion) throws SQLException;
	
	/**
	 * Método que contiene el Resulset validando que todos los terceros se relacionen con las empresas
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @return Resultset con todos los datos de la tabla empresas
	 * @throws SQLException
	 */
	public  Boolean consultarTercerosRelacionados(Connection con, int codigoInstitucion) throws SQLException;
	
	/**
	 * Método implementado para cargar los datos de una empresa dado su codigo Axioma
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap cargar(Connection con,int codigo);
	
	/**
	 * 
	 */
	public boolean terceroExisteComoEmpresa(Connection con, int tercero);
	
	/**
	 * 
	 */
	public boolean actualizarADeudorEmpresaDeudorTercero(Connection con, int emrpesa, int tercero);
}
