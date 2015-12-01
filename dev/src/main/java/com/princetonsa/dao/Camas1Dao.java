/*
 * @(#)Camas1Dao.java
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

import com.princetonsa.decorator.ResultSetDecorator;

/**
 *  Interfaz para el acceder a la fuente de datos de camas1
 *
 * @version 1.0, Mayo 31 / 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public interface Camas1Dao 
{
    /**
	 * Método que contiene el Resulset del Listado de las camas 
	 * creadas con antelacion, filtradas por el codigo de la institucion
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param int, codigo de la institucion
	 * @param centroAtencion
	 * @return Resultset con las 
	 * @throws SQLException
	 */
	public ResultSetDecorator listadoCamas1(Connection con, int codigoInstitucion,int centroAtencion) throws SQLException;
	
	/**
	 * Detalle de la cama filtrado por el codigo
	 * @param con
	 * @param codigo
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator detalleCama1(Connection con, int codigo); 
	
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
		String asignableAdmision) ;
	
	/**
	 * Inserta una cama 
	 * @param con
	 * @param codigo
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
													int codigoCentroCosto);
	
	
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
		String asignableAdmision) throws SQLException;
	
	/**
	 * Carga el última cama insertada
	 * @param con
	 * @return
	 */
	public  ResultSetDecorator cargarUltimaCodigoCamaInsertado(Connection con);
	
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
	public boolean existeCamaEnCentroAtencion(Connection con,int habitacion,String numeroCama,int institucion,int centroCosto);
	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public int obtenerCamaDadaCuenta(Connection con, String codigoCuenta);
}
