/*
 * @(#)OracleEmbarazoDao.java
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
import java.util.Vector;

import com.princetonsa.dao.EmbarazoDao;
import com.princetonsa.dao.sqlbase.SqlBaseEmbarazoDao;

/**
 * Clase que implementa toda la funcionalidad de embarazo (en antecedentes
 * gineco-obstetricos) con respecto a la Base de Datos Oracle
 *
 * @version 1.0, Apr 7, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho
 */
public class OracleEmbarazoDao implements EmbarazoDao
{
	/*
	/**
	 * Implementación de Insertar un embarazo (de antecedentes gineco-
	 * obstetricos) en una BD Oracle
	 * 
	 * @see com.princetonsa.dao.EmbarazoDao#insertar (Connection , int , float ,
	 * String , int , String , int , String )
	 *
	public int insertar (Connection con, int codigoPaciente, float mesesGestacion, String fechaTerminacion, int codigoComplicacion, String otraComplicacion, int codigoTrabajoParto, String otroTrabajoParto) throws SQLException
	{
		return SqlBaseEmbarazoDao.insertar (con, codigoPaciente, mesesGestacion, fechaTerminacion, codigoComplicacion, otraComplicacion, codigoTrabajoParto, otroTrabajoParto) ;
	}*/

	/**
	 * Implementación de Insertar un embarazo (de antecedentes gineco-
	 * obstetricos) en una BD Oracle, soportando Transaccionalidad.
	 * 
	 * @see com.princetonsa.dao.EmbarazoDao#insertarTransaccional(Connection,
	 * int, int, String, int, String, String)
	 */
	public int insertarTransaccional (Connection con, int codigoPaciente, float mesesGestacion, String fechaTerminacion, int[] complicaciones, Vector otraComplicacion, int codigoTrabajoParto, String otroTrabajoParto, String duracion, String tiempoRupturaMembranas, String legrado, String estado) throws SQLException
	{
		String secuencia="seq_complicaciones_gineco.nextval";
		String secuenciaOtras="seq_compli_gineco_otra.nextval";
		return SqlBaseEmbarazoDao.insertarTransaccional (con, codigoPaciente, mesesGestacion, fechaTerminacion, complicaciones, otraComplicacion, codigoTrabajoParto, otroTrabajoParto, estado, secuencia, secuenciaOtras, duracion, tiempoRupturaMembranas, legrado) ;
	}
	
	/*
	/**
	 * Implementación de Modificar un embarazo (de antecedentes gineco-
	 * obstetricos) en una BD Oracle.
	 * 
	 * @see com.princetonsa.dao.EmbarazoDao#modificar(Connection, int, int, int,
	 * String, int, String)
	 *
	public int modificar (Connection con, int codigoPaciente, int codigoEmbarazo, float mesesGestacion, String fechaTerminacion, int codigoComplicacion, String otraComplicacion, int codigoTrabajoParto, String otroTrabajoParto) throws SQLException
	{
		return SqlBaseEmbarazoDao.modificar (con, codigoPaciente, codigoEmbarazo, mesesGestacion, fechaTerminacion, codigoComplicacion, otraComplicacion, codigoTrabajoParto, otroTrabajoParto) ;
	}*/
	
	/**
	 * Implementación de Modificar un embarazo (de antecedentes gineco-
	 * obstetricos) en una BD Oracle, soportando Transaccionalidad.
	 * 
	 * @see com.princetonsa.dao.EmbarazoDao#modificarTransaccional(Connection,
	 * int, int, int, String, int, String, String)
	 */
	public int modificarTransaccional (Connection con, int codigoPaciente, int codigoEmbarazo, float mesesGestacion, String fechaTerminacion, int[] complicacion, Vector otraComplicacion, int codigoTrabajoParto, String otroTrabajoParto, String duracion, String tiempoRupturaMembranas, String legrado, String estado) throws SQLException
	{
		String secuencia="seq_complicaciones_gineco.nextval";
		String secuenciaOtras="seq_compli_gineco_otra.nextval";
		return SqlBaseEmbarazoDao.modificarTransaccional (con, codigoPaciente, codigoEmbarazo, mesesGestacion, fechaTerminacion, complicacion, otraComplicacion, codigoTrabajoParto, otroTrabajoParto, estado, secuencia, secuenciaOtras, duracion, tiempoRupturaMembranas, legrado) ;
	}

	/**
	 * Implementación de la cargada de hijos en una Base de Datos Oracle
	 * 
	 * @see com.princetonsa.dao.EmbarazoDao#cargarHijos(Connection, String, String, int)
	 */

	public ResultSetDecorator cargarHijos (Connection con, int codigoPaciente, int numeroEmbarazo) throws SQLException
	{
		return SqlBaseEmbarazoDao.cargarHijos (con, codigoPaciente, numeroEmbarazo) ;
	}


	/**
	 * Implementación de la búsqueda de un embarazo en una base de datos
	 * Oracle
	 * 
	 * @see com.princetonsa.dao.EmbarazoDao#existeEmbarazo(Connection, int, int)
	 */
	public boolean existeEmbarazo(Connection con, int codigoPaciente, int numeroEmbarazo) throws SQLException
	{
		return SqlBaseEmbarazoDao.existeEmbarazo(con, codigoPaciente, numeroEmbarazo) ;
	}
	
	/**
	 * Método para ingresar complicaciones
	 * @param con
	 * @param secuencia
	 * @param codigoEmbarazo
	 * @param codigoPaciente
	 * @param complicaciones
	 * @param complicacionesOtras
	 * @return numero de inserciones hechas en la BD
	 */
	public int ingresarComplicaciones(Connection con, int codigoEmbarazo, int codigoPaciente, int[] complicaciones, Vector complicacionesOtras)
	{
		String secuencia="seq_complicaciones_gineco.nextvalue";
		String secuenciaOtras="seq_compli_gineco_otra.nextvalue";
		return SqlBaseEmbarazoDao.ingresarComplicaciones(con, secuencia, secuenciaOtras, codigoEmbarazo, codigoPaciente, complicaciones, complicacionesOtras);
	}


}
