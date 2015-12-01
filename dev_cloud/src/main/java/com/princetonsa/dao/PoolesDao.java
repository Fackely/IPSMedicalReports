/*
 * PoolesDao.java 
 * Autor		:  jarloc
 * Creado el	:  01-dic-2004
 * 
 * Lenguaje		: Java
 * Compilador	: J2SDK 1.4.1_01
 * 
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 * */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.ArrayList;

import util.InfoDatosStr;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * descripcion de esta clase
 *
 * @version 1.0, 01-dic-2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan Lopez </a>
 */
public interface PoolesDao 
{

	
	/**
	 * Metodo que realiza la consulta de uno ó varios pooles.
	 * @param con, Connection con la fuente de datos.
	 * @param institucion, Codigo de la institución.	 
	 * @return ResultSet, con el resultado.
	 * @see com.princetonsa.dao.SqlBasePoolesDao#consultaPooles(java.sql.Connection,int)
	 */
	public ResultSetDecorator consultaPooles (Connection con, int institucion);
	
	/**
	 * Metodo que realiza la consulta de terceros
	 * @param con, Connection con la fuente de datos.
	 * @return ResultSet, con el resultado.
	 * @see com.princetonsa.dao.SqlBasePoolesDao#consultaTerceros(java.sql.Connection)
	 */
	public ResultSetDecorator consultaTerceros (Connection con);
	
	/**
	 * Metodo que realiza la consulta para verificar si el pool
	 * se encuentra relacionado con la funcionalidad de MedicosXPool 
	 * @param con, Connection con la fuente de datos.
	 * @param codigoPool, int codigo del Pool
	 * @return ResultSet, con el resultado.
	 * @see com.princetonsa.dao.SqlBasePoolesDao#existePoolToMedicosXPool(java.sql.Connection)
	 */
	public ResultSetDecorator existePoolToMedicosXPool (Connection con, int codigoPool);
	
	/**
	 * Metodo para insertar un Pool
	 * @param diasVencFactura 
	 * @param cuentaXpagar 
	 * @param con, Connection con la fuente de datos
	 * @param descripcion, String descripción del pool
	 * @param responsable, int código del responsable (tabla terceros)
	 * @param activo, int 1 activo, 0 de lo contrario
	 * @return boolean, true efectivo, false de lo contrario
	 * @see com.princetonsa.dao.SqlBasePoolesDao#insertarPool(java.sql.Connection,String,int,int)
	 */
	public boolean insertarPool (Connection con,String descripcion,int responsable,int activo, int diasVencFactura, int cuentaXpagar);
	
	/**
	 * Metodo que realiza la consulta de uno ó varios pooles.
	 * @param con, Connection con la fuente de datos.
	 * @param codigos,int[] con los códigos a consultar	 
	 * @return ResultSet, con el resultado.
	 * @see com.princetonsa.dao.SqlBasePoolesDao#consultaResumen(java.sql.Connection,int)
	 */
	public ResultSetDecorator consultaResumen (Connection con, int[] codigos);
	
	/**
	 * Eliminar pooles
	 * @param con, Connection con la fuente de datos
	 * @param codigoPool, código del pool a eliminar	 
	 * @return boolean, true si es efectivo, false de lo contrario
	 * @see com.princetonsa.dao.SqlBasePoolesDao#eliminar(java.sql.Connection,int)
	 */
	public boolean eliminar(Connection con, int codigoPool); 
	
	/**
	 * Modifica uno ó varios registros de pooles
	 * @param diasVencFactura 
	 * @param cuentaXpagar 
	 * @param descripcion, String nombre del pool
	 * @param activo, int estado del registro 1 activo, 0 de lo contrario
	 * @param responsable, int codigo del tercero
	 * @param codigoPool, int codigo del pool
	 * @see com.princetonsa.dao.SqlBasePoolesDao#modificar(java.sql.Connection,String,int,int)
	 * @return boolean, true si modifico, false de lo contrario
	 */
	public boolean modificar (Connection con,String descripcion,int activo,int tercero,int codigoPool, int diasVencFactura, int cuentaXpagar);
	
	/**
	 * Consulta Avanzada, segun los parametros recibidos
	 * @param con, Connection con la fuente de datos
	 * @param descripcion, String con el nombre del pool
	 * @param nombreResponsable, String con el nombre del responsable
	 * @param nit, String con el número de identificación
	 * @param activo, int 1 si es activo, 0 de lo contrario
	 * @return ResultSet, con el resultado de la consulta
	 * @see com.princetonsa.dao.SqlBasePoolesDao#consultaPoolesAvanzada(java.sql.Connection,String,String,int,int,double)
	 */
	public ResultSetDecorator consultaPoolesAvanzada (Connection con, 
			        												String descripcion,
															        String nombreResponsable,
															        String nit, 
															        int activo );
	
	
	/**
	 * 
	 * @param institucion
	 * @param activo
	 * @return
	 */
	public  ArrayList<InfoDatosStr> cargarPooles(final int institucion, boolean activo);
	
		
	
}
