/*
 * @(#)PostgresqlTopesFacturacionDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Date;

import util.ResultadoBoolean;
import util.UtilidadFecha;

import com.princetonsa.dao.TopesFacturacionDao;
import com.princetonsa.dao.sqlbase.SqlBaseTopesFacturacionDao;

/**
 * Implementaci�n postgresql de las funciones de acceso a la fuente de datos
 * para un tope de facturacion
 *
 * @version 1.0, Julio 12 / 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson R�os</a>
 */

public class PostgresqlTopesFacturacionDao implements TopesFacturacionDao
{
	/**
	 *  Insertar un tope de facturaci�n
	 */
	private final static String insertarTopeStr = 	"INSERT INTO topes_facturacion " +
																	"(" +
																	"codigo, " +
																	"tipo_regimen," +
																	"estrato_social, " +
																	"tipo_monto," +
																	"tope_evento, " +
																	"tope_anio_calendario," +
																	"institucion, " +
																	"vigencia_inicial " +
																	") " +
																	"VALUES " +
																	"(" +
																	"nextval('seq_topes_facturacion'), ?, ?, ?, ?, ?, ?, ? " +
																	")";

	/**
	 * Inserta un tope de facturaci�n
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param tipoRegimen, tipo de r�gimen
	 * @param estrato, clasificaci�n socioEcon�mica
	 * @param tipoMonto, tipo de monto
	 * @param topeEvento, tope por evento
	 * @param topeAnioCalendario, tope por anio calendario
	 * @param institucion, institucion
	 * @return 0 o 1 si inserta
	 * @throws SQLException
	 */
	public  int  insertar(		Connection con,
										String tipoRegimen,
										int estrato, 
										int tipoMonto,
										double topeEvento,
										double topeAnioCalendario,
										int institucion,
										Date fechaVigenciaInicial) throws SQLException
	{
		return SqlBaseTopesFacturacionDao.insertar(con,tipoRegimen,estrato,tipoMonto,topeEvento,topeAnioCalendario,institucion, fechaVigenciaInicial, insertarTopeStr);
	}
	
	/**
	 *  Inserta un tope de facturaci�n  de una transacci�n dado su estado.
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param tipoRegimen, tipo de r�gimen
	 * @param estrato, clasificaci�n socioEcon�mica
	 * @param tipoMonto, tipo de monto
	 * @param topeEvento, tope por evento
	 * @param topeAnioCalendario, tope por anio calendario
	 * @param institucion, institucion
	 * @param estado
	 * @return  ResultadoBoolean, true si la inserci�n fue exitosa, false y con la descripci�n 
	 * 				de lo contrario
	 */
	public  ResultadoBoolean insertarTransaccional(	Connection con,
																			String tipoRegimen,
																			int estrato, 
																			int tipoMonto,
																			float topeEvento,
																			float topeAnioCalendario,
																			int institucion,
																			String estado)throws SQLException
	{
		return SqlBaseTopesFacturacionDao.insertarTransaccional(con,tipoRegimen,estrato,tipoMonto,topeEvento,topeAnioCalendario,institucion,estado,insertarTopeStr);
	}
	
	/**
	 * M�todo que  carga  los datos de un tope de facturaci�n 
	 * seg�n los datos que lleguen del  c�digo de la tabla topes_facturacion 
	 * para mostrarlos en el resumen
	 * en una BD PostgresSQL o Hsqldb 
	 */
	public  ResultSetDecorator cargarResumen(Connection con, int codigo) throws SQLException
	{
		return SqlBaseTopesFacturacionDao.cargarResumen(con,codigo);
	}
	
	/**Carga el tope de facturaci�n insertado**/
	public ResultSetDecorator cargarUltimoCodigo(Connection con)
	{
		return SqlBaseTopesFacturacionDao.cargarUltimoCodigo(con);
	}

	/**
	 * Modificar un tope de facturaci�n
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param fecha vigencia
	 * @param topeEvento,
	 * @param topeAnioCalendario
 	 * @return int, 0 si  no inserta, 1 si inserta
	 */
	public  int modificar(	Connection con, 
									int codigo,
									double topeEvento,
									double topeAnioCalendario,
									String fechaVigenciaInicial)
	{
		return SqlBaseTopesFacturacionDao.modificar(con,codigo,topeEvento,topeAnioCalendario,fechaVigenciaInicial);
	}
	
	/**
	 * Modificar un tope de facturaci�n en una transacci�n
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param fecha vigencia
	 * @param topeEvento,
	 * @param topeAnioCalendario
 	 * @param estado. String, estado dentro de la transacci�n 
	 * @return  ResultadoBoolean, true si la inserci�n fue exitosa, false y con la descripci�n 
	 * 				de lo contrario
	 */
	public ResultadoBoolean modificarTransaccional(	Connection con, 
																				int codigo, 
																				float topeEvento,
																				float topeAnioCalendario,
																				String estado)throws SQLException
	{
		return SqlBaseTopesFacturacionDao.modificarTransaccional(con,codigo, topeEvento,topeAnioCalendario,estado);
	}
	
	/**
	 * M�todo que elimina un tope de facturaci�n  
	 */
	public int eliminar(Connection con, int codigo)
	{
		return SqlBaseTopesFacturacionDao.eliminar(con,codigo);
	}
	
	/**
	 * M�todo que contiene el Resulset de los datos de la tabla topes_facturacion
	 * para posteriorm/t ser mostrados en el pager
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @return Resultset con todos los datos de la tabla estratos_sociales
	 * @throws SQLException
	 */
	public  ResultSetDecorator listado(Connection con) throws SQLException
	{
		return SqlBaseTopesFacturacionDao.listado(con);
	}
	
	/**
	 * 
	 * Carga el codigo
	 * @param con
	 * @param tipoRegimen
	 * @param estratoSocial
	 * @param tipoMonto
	 * @return 
	 */
	public int cargarTopeParaFacturacion(Connection con, String tipoRegimen, int estratoSocial, int tipoMonto, String fechaVigencia)
	{
		return SqlBaseTopesFacturacionDao.cargarTopeParaFacturacion(con, tipoRegimen, estratoSocial, tipoMonto, fechaVigencia);
	}
	
	/**
	 * M�todo que contiene el Resulset de los datos de la tabla topes_facturacion
	 * para posteriorm/t ser mostrados en el pager
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @return Resultset con todos los datos de la tabla estratos_sociales
	 * @throws SQLException
	 */
	public  ResultSetDecorator listadoPosteriores(Connection con) throws SQLException
	{
		return SqlBaseTopesFacturacionDao.listadoPosteriores(con);
	}
}
