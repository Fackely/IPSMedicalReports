/*
 * @(#)OracleClasificacionSocioEconomicaDao.java
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
import java.sql.SQLException;

import util.ResultadoBoolean;

import com.princetonsa.dao.ClasificacionSocioEconomicaDao;
import com.princetonsa.dao.sqlbase.SqlBaseClasificacionSocioEconomicaDao;

/**
 * Implementación Oracle de las funciones de acceso a la fuente de datos
 * para una clasificación socioEconómica
 *
 * @version 1.0, Junio 30 / 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class OracleClasificacionSocioEconomicaDao implements ClasificacionSocioEconomicaDao
{//SIN PROBAR FUNC. SECUENCIA

	/**
	 *  Insertar una clasificación socieconómica
	 */
	private final static String insertarClasificacionStr = 	"INSERT INTO estratos_sociales " +
																						"(" +
																						"codigo, " +
																						"descripcion, " +
																						"tipo_regimen," +
																						"activo," +
																						"institucion " +
																						") " +
																						"VALUES " +
																						"(" +
																						"seq_estratos_sociales.nextval, ?, ?, ?, ?" +
																						")";

	/**
	 * Inserta una clasificación social
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param descripcion, String, descripcion de la calasificación social
	 * @param tipoRegimen, String, régimen de acuerdo a los previam/t
	 * 				ingresados en el sistema
	 * @param activa, boolean
	 * @param institucion, int, Codigo de la institucion a la que pertenece el usuario
	 * @return int, 0 si  no inserta, 1 si inserta
	 */
	public int  insertar(	Connection con,
										String descripcion,
										String tipoRegimen,
										boolean activa,
										int institucion)
	{
		return SqlBaseClasificacionSocioEconomicaDao.insertar(con,descripcion,tipoRegimen,activa, institucion,insertarClasificacionStr);									
	}
										
	/**
	 *  Inserta una clasificación socioeconómica dentro de una transacción dado su estado.
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param descripcion, String, descripcion de la calasificación social
	 * @param tipoRegimen, String, régimen de acuerdo a los previam/t
	 * 				ingresados en el sistema
	 * @param activa, boolean	 
	 * @param institucion, int, Codigo de la institucion a la que pertenece el usuario
	 * @param estado. String, estado dentro de la transacción 
	 * @return  ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
	 * 				de lo contrario
	 */																					
	public  ResultadoBoolean insertarTransaccional(	Connection con,
																						String descripcion,
																						String tipoRegimen,
																						boolean activa,
																						int institucion,
																						String estado) throws SQLException
	{
		return SqlBaseClasificacionSocioEconomicaDao.insertarTransaccional(con,descripcion,tipoRegimen,activa,institucion,estado, insertarClasificacionStr);																					
	}
	
	/**
	 * Método que  carga  los datos de una clasifcación socioeconómica 
	 * según los datos que lleguen del  código de la tabla estratos_sociales 
	 * para mostrarlos en el resumen
	 * en una BD PostgresSQL o Hsqldb 
	 */
	public ResultSetDecorator cargarResumen(Connection con, int codigo) throws SQLException
	{
		return SqlBaseClasificacionSocioEconomicaDao.cargarResumen(con,codigo);
	}
									
	/**Carga el última clasificación socioeconómica insertada**/
	public ResultSetDecorator cargarUltimoCodigo(Connection con)
	 {
		return SqlBaseClasificacionSocioEconomicaDao.cargarUltimoCodigo(con);
	 }

	/**
	 * Modificar una clasificación socioeconómica
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param descripcion, String, descripcion de la calasificación social
	 * @param activa, boolean
	 * @return int, 0 si  no inserta, 1 si inserta
	 */
	public int modificar(	Connection con, 
										int codigo, 
										String descripcion,
										String tipoRegimen,
										boolean activa)
	{
		return SqlBaseClasificacionSocioEconomicaDao.modificar(con,codigo,descripcion,tipoRegimen,activa);									 
	}

	/**
	 * Modifica una clasificación socioeconómica dado su código con los 
	 * paramétros dados  dentro de una transacción dado su estado. 
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param descripcion, String, descripcion de la calasificación social
	 * @param activa, boolean	 
	 * @param estado. String, estado dentro de la transacción 
	 * @return  ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
	 * 				de lo contrario
	 */
	public ResultadoBoolean modificarTransaccional(	Connection con, 
																						int codigo, 
																						String descripcion,
																						String tipoRegimen,
																						boolean activa,
																						String estado)throws SQLException
	{
		return SqlBaseClasificacionSocioEconomicaDao.modificarTransaccional(con,codigo,descripcion,tipoRegimen,activa,estado);																					 
	}
	
	/**
	 * Método que contiene el Resulset de los datos de la tabla estratos_sociales
	 * para posteriorm/t ser mostrados en el pager
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param , int, institucion, Codigo de la institucion a la que pertenece el usuario para filtrar la consulta
	 * @return Resultset con todos los datos de la tabla estratos_sociales
	 * @throws SQLException
	 */
	public  ResultSetDecorator listado(Connection con, int institucion) throws SQLException
	{
		return SqlBaseClasificacionSocioEconomicaDao.listado(con,institucion);
	}
	
	/**
	 * Búsqueda Avanzada de un estrato 
	 * @param con
	 * @param descripcion
	 * @param nombreTipoRegimen
	 * @param activaAux
	 * @param  int, institucion, Codigo de la institucion a la que pertenece el usuario para filtrar la consulta
	 * @return ResulSet con el resultado de la búsqueda
	 * @throws SQLException
	 */
	public ResultSetDecorator busqueda(	Connection con,
													int codigo,
													String descripcion,
													String nombreTipoRegimen,
													int activaAux,
													int institucion) throws SQLException
	{
		return SqlBaseClasificacionSocioEconomicaDao.busqueda(con,codigo,descripcion,nombreTipoRegimen,activaAux, institucion);												
	}
}
