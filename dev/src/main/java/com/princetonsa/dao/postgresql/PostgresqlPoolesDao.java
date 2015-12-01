/*
 * PostgresqlPoolesDao.java 
 * Autor		:  jarloc
 * Creado el	:  01-dic-2004
 * 
 * Lenguaje		: Java
 * Compilador	: J2SDK 1.4.1_01
 * 
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 * */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.util.ArrayList;

import util.InfoDatosStr;

import com.princetonsa.decorator.ResultSetDecorator;

import com.princetonsa.dao.PoolesDao;
import com.princetonsa.dao.sqlbase.SqlBasePoolesDao;

/**
 * descripcion de esta clase
 *
 * @version 1.0, 01-dic-2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L�pez</a>
 */
public class PostgresqlPoolesDao implements PoolesDao
{
	/**
	 * almacena el Query de insertar pool
	 */
	public static final String insertQuery = 	"INSERT INTO pooles (codigo, descripcion, tercero_responsable, activo, dias_vencimiento_fact, cuenta_x_pagar) " +
												"VALUES (NEXTVAL('seq_pooles'),?,?,?,?,?) ";
												
	
		
	/**
	 * Metodo que realiza la consulta de uno � varios pooles.
	 * @param con, Connection con la fuente de datos.
	 * @param institucion, Codigo de la instituci�n.	 
	 * @return ResultSet, con el resultado.
	 * @see com.princetonsa.dao.SqlBasePoolesDao#consultaPooles(java.sql.Connection,int)
	 */
	public ResultSetDecorator consultaPooles (Connection con, int institucion)
	{
	    return SqlBasePoolesDao.consultaPooles(con,institucion);
	}
	
	
	/**
	 * Metodo que realiza la consulta de terceros
	 * @param con, Connection con la fuente de datos.
	 * @return ResultSet, con el resultado.
	 * @see com.princetonsa.dao.SqlBasePoolesDao#consultaTerceros(java.sql.Connection)
	 */
	public ResultSetDecorator consultaTerceros (Connection con)
	{
	    return SqlBasePoolesDao.consultaTerceros(con);
	}
	
	/**
	 * Metodo que realiza la consulta para verificar si el pool
	 * se encuentra relacionado con la funcionalidad de MedicosXPool 
	 * @param con, Connection con la fuente de datos.
	 * @param codigoPool, int codigo del Pool
	 * @return ResultSet, con el resultado.
	 * @see com.princetonsa.dao.SqlBasePoolesDao#existePoolToMedicosXPool(java.sql.Connection)
	 */
	public ResultSetDecorator existePoolToMedicosXPool (Connection con, int codigoPool)
	{
	    return SqlBasePoolesDao.existePoolToMedicosXPool(con, codigoPool);
	}
	
	/**
	 * Metodo para insertar un Pool
	 * @param con, Connection con la fuente de datos
	 * @param descripcion, String descripci�n del pool
	 * @param responsable, int c�digo del responsable (tabla terceros)
	 * @param activo, int 1 activo, 0 de lo contrario
	 * @return boolean, true efectivo, false de lo contrario
	 * @see com.princetonsa.dao.SqlBasePoolesDao#insertarPool(java.sql.Connection,String,int,int)
	 */
	public boolean insertarPool (Connection con,String descripcion,int responsable,int activo, int diasVencFactura, int cuentaXpagar)
	{
	    return SqlBasePoolesDao.insertarPool(con,descripcion,responsable,activo,diasVencFactura,cuentaXpagar,insertQuery);
	}
	
	/**
	 * Metodo que realiza la consulta de uno � varios pooles.
	 * @param con, Connection con la fuente de datos.
	 * @param codigos,int[] con los c�digos a consultar	 
	 * @return ResultSet, con el resultado.
	 * @see com.princetonsa.dao.SqlBasePoolesDao#consultaResumen(java.sql.Connection,int)
	 */
	public ResultSetDecorator consultaResumen (Connection con, int[] codigos)
	{
	    return SqlBasePoolesDao.consultaResumen(con,codigos);
	}

	
	/**
	 * Eliminar pooles
	 * @param con, Connection con la fuente de datos
	 * @param codigoPool, c�digo del pool a eliminar	 
	 * @return boolean, true si es efectivo, false de lo contrario
	 * @see com.princetonsa.dao.SqlBasePoolesDao#eliminar(java.sql.Connection,int)
	 */
	public boolean eliminar(Connection con, int codigoPool)
	{
	    return SqlBasePoolesDao.eliminar(con,codigoPool);
	}
	
	/**
	 * Modifica uno � varios registros de pooles
	 * @param descripcion, String nombre del pool
	 * @param activo, int estado del registro 1 activo, 0 de lo contrario
	 * @param responsable, int codigo del tercero
	 * @param codigoPool, int codigo del pool
	 * @see com.princetonsa.dao.SqlBasePoolesDao#modificar(java.sql.Connection,String,int,int)
	 * @return boolean, true si modifico, false de lo contrario
	 */
	public boolean modificar (Connection con,String descripcion,int activo,int tercero,int codigoPool, int diasVencFactura, int cuentaXpagar)
	{
	    return SqlBasePoolesDao.modificar(con,descripcion,activo,tercero,codigoPool, diasVencFactura, cuentaXpagar);
	}
	
	/**
	 * Consulta Avanzada, segun los parametros recibidos
	 * @param con, Connection con la fuente de datos
	 * @param descripcion, String con el nombre del pool
	 * @param nombreResponsable, String con el nombre del responsable
	 * @param nit, String con el n�mero de identificaci�n
	 * @param activo, int 1 si es activo, 0 de lo contrario
	 * @return ResultSet, con el resultado de la consulta
	 * @see com.princetonsa.dao.SqlBasePoolesDao#consultaPoolesAvanzada(java.sql.Connection,String,String,int,int,double)
	 */
	public ResultSetDecorator consultaPoolesAvanzada (Connection con, 
			        												String descripcion,
															        String nombreResponsable,
															        String nit, 
															        int activo )
	{
	    return SqlBasePoolesDao.consultaPoolesAvanzada(con,descripcion,nombreResponsable,nit,activo);
	}


	@Override
	public ArrayList<InfoDatosStr> cargarPooles(int institucion, boolean activo) {
		return SqlBasePoolesDao.cargarPooles(institucion, activo);
	}

}
