/*
 * @(#)TopesFacturacionDao
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
import java.util.Date;

import util.ResultadoBoolean;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 *  Interfaz para el acceder a la fuente de datos de un tope de facturación
 *
 * @version 1.0, Julio 12 / 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public interface TopesFacturacionDao 
{
	/**
	 * Inserta un tope de facturación
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param tipoRegimen, tipo de régimen
	 * @param estrato, clasificación socioEconómica
	 * @param tipoMonto, tipo de monto
	 * @param topeEvento, tope por evento
	 * @param topeAnioCalendario, tope por anio calendario
	 * @param institucion, institucion
	 * @return 0 o 1 si inserta
	 */
	public  int  insertar(		Connection con,
										String tipoRegimen,
										int estrato, 
										int tipoMonto,
										double topeEvento,
										double topeAnioCalendario,
										int institucion,
										Date fechaVigenciaInicial)throws SQLException;
	
	/**
	 *  Inserta un tope de facturación  de una transacción dado su estado.
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param tipoRegimen, tipo de régimen
	 * @param estrato, clasificación socioEconómica
	 * @param tipoMonto, tipo de monto
	 * @param topeEvento, tope por evento
	 * @param topeAnioCalendario, tope por anio calendario
	 * @param institucion, institucion
	 * @param estado
	 * @return  ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
	 * 				de lo contrario
	 */
	public  ResultadoBoolean insertarTransaccional(	Connection con,
																			String tipoRegimen,
																			int estrato, 
																			int tipoMonto,
																			float topeEvento,
																			float topeAnioCalendario,
																			int institucion,
																			String estado)throws SQLException;
	
	/**
	 * Método que  carga  los datos de un tope de facturación 
	 * según los datos que lleguen del  código de la tabla topes_facturacion 
	 * para mostrarlos en el resumen
	 * en una BD PostgresSQL o Hsqldb 
	 */
	public  ResultSetDecorator cargarResumen(Connection con, int codigo) throws SQLException;
	
	/**Carga el tope de facturación insertado**/
	public ResultSetDecorator cargarUltimoCodigo(Connection con);

	/**
	 * Modificar un tope de facturación
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param fecha vigencia
	 * @param topeEvento,
	 * @param topeAnioCalendario
 	 * @return int, 0 si  no inserta, 1 si inserta
	 */
	public  int modificar(	Connection con, 
									int codigo,
									double topeEvento,
									double topeAnioCalendario,
									String fechaVigenciaInicial) ;
	
	/**
	 * Modificar un tope de facturación en una transacción
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param topeEvento,
	 * @param topeAnioCalendario
 	 * @param estado. String, estado dentro de la transacción 
	 * @return  ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
	 * 				de lo contrario
	 */
	public ResultadoBoolean modificarTransaccional(	Connection con, 
																				int codigo, 
																				float topeEvento,
																				float topeAnioCalendario,
																				String estado)throws SQLException;
	
	/**
	 * Método que contiene el Resulset de los datos de la tabla topes_facturacion
	 * para posteriorm/t ser mostrados en el pager
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @return Resultset con todos los datos de la tabla estratos_sociales
	 * @throws SQLException
	 */
	public  ResultSetDecorator listado(Connection con) throws SQLException;

	/**
	 * Método que elimina un tope de facturación  
	 */
	public int eliminar(Connection con, int codigo);
	
	/**
	 * Método que contiene el Resulset de los datos de la tabla topes_facturacion
	 * para posteriorm/t ser mostrados en el pager
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @return Resultset con todos los datos de la tabla estratos_sociales
	 * @throws SQLException
	 */
	public  ResultSetDecorator listadoPosteriores(Connection con) throws SQLException;
	
	/**
	 * Carga codigo
	 * @param con
	 * @param tipoRegimen
	 * @param estratoSocial
	 * @param tipoMonto
	 * @return 
	 */
	public int cargarTopeParaFacturacion(Connection con, String tipoRegimen, int estratoSocial, int tipoMonto, String fechaVigencia);

}
