/*
 * @(#)PostgresqlCamas1Dao.java
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

import com.princetonsa.dao.Camas1Dao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.sqlbase.SqlBaseCamas1Dao;

/**
 * Implementación postgresql de las funciones de acceso a la fuente de datos
 * para un camas1
 *
 * @version 1.0, Mayo 31 / 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class PostgresqlCamas1Dao implements Camas1Dao
{
    
    /**
	 * Inserta una cama
	 */
	private final static String ingresarCama1Str=	"INSERT INTO camas1 " +
																		"(codigo, " +
																		"habitacion, " +
																		"numero_cama, " +
																		"descripcion, " +
																		"estado, " +
																		"es_uci, " +
																		"institucion, " +
																		"tipo_usuario_cama, " +
																		"centro_costo ) " +
																		"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    /**
	 * Método que contiene el Resulset del Listado de las camas 
	 * creadas con antelacion, filtradas por el codigo de la institucion
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param int, codigo de la institucion
	 * @param centroAtencion
	 * @return Resultset con las 
	 * @throws SQLException
	 */
	public ResultSetDecorator listadoCamas1(Connection con, int codigoInstitucion,int centroAtencion) throws SQLException
	{
	    return SqlBaseCamas1Dao.listadoCamas1(con, codigoInstitucion,centroAtencion,DaoFactory.POSTGRESQL);
	}
	
	/**
	 * Detalle de la cama filtrado por el codigo
	 * @param con
	 * @param codigo
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator detalleCama1(Connection con, int codigo)
	{
	    return SqlBaseCamas1Dao.detalleCama1(con, codigo);
	}
	
	/**
	 * Metodo que modifica una cama dado su codigo
	 * @param con
	 * @param habitacion
	 * @param descripcion
	 * @param estado
	 * @param tipoUsuarioCama
	 * @param centroCosto
	 * @param esUci
	 * @param codigo
	 * @return true si fue modificado
	 */
	public boolean modificarCama1(	Connection con,
		String habitacion,
		String descripcion, 
		int estado,
		int tipoUsuarioCama,
		int centroCosto, 
		boolean esUci, 
		int codigo,
		String asignableAdmision) 
	{
	    return SqlBaseCamas1Dao.modificarCama1(con, habitacion, descripcion, estado, tipoUsuarioCama, centroCosto, esUci, codigo,asignableAdmision);
	}
	
	/**
	 * Inserta una cama 
	 * @param con
	 * @param habitacion
	 * @param numeroCama
	 * @param descripcion
	 * @param estado
	 * @param esUci
	 * @param codigoInstitucion
	 * @param tipoUsuarioCama
	 * @param codigoCentroCosto
	 * @return true - false
	 */
	public boolean  insertarCama1(	Connection con,
			                                        int codigo,  
													int habitacion,
													String numeroCama,
													String descripcion,
													int estado,
													boolean esUci,
													int codigoInstitucion,
													int tipoUsuarioCama,
													int codigoCentroCosto)
	{
	    return SqlBaseCamas1Dao.insertarCama1(con, codigo, habitacion, numeroCama, descripcion, estado, esUci, codigoInstitucion, tipoUsuarioCama, codigoCentroCosto, ingresarCama1Str);
	}
	
	/**
	 * Busqueda Avanzada de las camas
	 * @param con
	 * @param habitacion
	 * @param piso
	 * @param tipoHabitacion
	 * @param numeroCama
	 * @param descripcionCama
	 * @param codigoEstado
	 * @param codigoTipoUsuario
	 * @param esUciAux
	 * @param codigoServicio
	 * @param nombreServicio
	 * @param codigoInstitucion
	 * @param codigoCentroCosto
	 * @param codigoCentroAtencion
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator busquedaAvanzadaCama1(	Connection con,
			int habitacion,
			String piso,
			String tipoHabitacion,
			String numeroCama,
			String descripcionCama,
			int codigoEstado,
			int codigoTipoUsuario,
			int esUciAux,
			int codigoServicio,
			String nombreServicio,
			int codigoInstitucion,
			int codigoCentroCosto,
			int codigoCentroAtencion,
			String asignableAdmision) throws SQLException
	{
	    return SqlBaseCamas1Dao.busquedaAvanzadaCama1(con, habitacion, piso, tipoHabitacion, numeroCama, descripcionCama, codigoEstado, codigoTipoUsuario, esUciAux, codigoServicio, nombreServicio, codigoInstitucion, codigoCentroCosto,codigoCentroAtencion,asignableAdmision);
	}

	/**
	 * Carga el última cama insertada
	 * @param con
	 * @return
	 */
	public  ResultSetDecorator cargarUltimaCodigoCamaInsertado(Connection con)
	{
	    return SqlBaseCamas1Dao.cargarUltimaCodigoCamaInsertado(con);
	}
	
	/**
	 * Método implementado para verificar si ya existe una cama
	 * con la misma habitacion, numero de cama, institucion en el mismo
	 * de centro de atención.
	 * @param con
	 * @param habitacion
	 * @param numeroCama
	 * @param institucion
	 * @param centroCosto
	 * @return
	 */
	public boolean existeCamaEnCentroAtencion(Connection con,int habitacion,String numeroCama,int institucion,int centroCosto)
	{
		return SqlBaseCamas1Dao.existeCamaEnCentroAtencion(con,habitacion,numeroCama,institucion,centroCosto);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public int obtenerCamaDadaCuenta(Connection con, String codigoCuenta)
	{
		return SqlBaseCamas1Dao.obtenerCamaDadaCuenta(con, codigoCuenta);
	}
}
