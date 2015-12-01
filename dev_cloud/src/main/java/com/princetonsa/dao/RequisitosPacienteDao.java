/*
 * @(#)RequisitosPacienteDao.java
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
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 *  Interfaz para el acceder a la fuente de datos de los requisitos paciente
 *
 * @version 1.0, Nov 22 / 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson R�os</a>
 */
public interface RequisitosPacienteDao 
{
	/**
	 * Inserta un requisito para el paciente dependiendo de la instituci�n
	 * @param con
	 * @param descripcion
	 * @param activo
	 * @param institucion
	 * @return
	 */
	public int  insertar(	Connection con,
									String descripcion,
									String tipoRequisito,
									boolean activo,
									int codigoInstitucion);
	
	/**
	 * Inserta un requisito para radicacion dependiendo de la instituci�n
	 * @param con
	 * @param descripcion
	 * @param activo
	 * @param institucion
	 * @return
	 */
	public int  insertarRequisitosRadicacion(		Connection con,
																	String descripcion,
																	String tipoRequisito,
																	boolean activo,
																	int codigoInstitucion);
	
	/**
	 *  Consulta  la info  de los requisitos de paciente seg�n la instituci�n
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public ResultSetDecorator cargarRequisitos(Connection con, int codigoInstitucion);
	
	/**
	 *  Consulta  la info  de los requisitos de radicacion seg�n la instituci�n
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public ResultSetDecorator cargarRequisitosRadicacion(Connection con, int codigoInstitucion);
	
	/**
	 * Modifica un requisito de paciente dado su codigo
	 * @param con
	 * @param descripcion
	 * @param activo
	 * @param codigo
	 * @return
	 */
	public int modificar(	Connection con, 
									String descripcion,
									String tipoRequisito,
									boolean activo, 
									int codigo);
	
	/**
	 * Modifica un requisito de radicacion dado su codigo
	 * @param con
	 * @param descripcion
	 * @param activo
	 * @param codigo
	 * @return
	 */
	public int modificarRequisitosRadicacion(	Connection con, 
																	String descripcion,
																	String tipoRequisito,
																	boolean activo, 
																	int codigo);
	
	
	/**
	 * M�todo que permite insertar un nuevo requisito
	 * exigido por un convenio
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param codigoRequisitoPaciente C�digo del requisito
	 * @param codigoConvenio C�digo del convenio
	 * @return
	 * @throws SQLException
	 */
	public int insertarRequisitoPacienteConvenio (Connection con, int codigoRequisitoPaciente, int codigoConvenio, int viaIngreso) throws SQLException;
	
	/**
	 * M�todo que permite insertar un nuevo requisito radicacion
	 * exigido por un convenio
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param codigoRequisitoRadicacion C�digo del requisito
	 * @param codigoConvenio C�digo del convenio
	 * @return
	 * @throws SQLException
	 */
	public int insertarRequisitoRadicacionConvenio (Connection con, int codigoRequisitoRadicacion, int codigoConvenio) throws SQLException;
	
	/**
	 * M�todo que permite eliminar un requisito exigido
	 * por un convenio
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param codigoRequisitoPaciente C�digo del requisito
	 * @param codigoConvenio C�digo del convenio
	 * @return
	 * @throws SQLException
	 */
	public int eliminarRequisitoPacienteConvenio (Connection con, int codigoRequisitoPaciente, int codigoConvenio, int viaIngreso) throws SQLException;
	
	/**
	 * M�todo que permite eliminar un requisito radicacion exigido
	 * por un convenio
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param codigoRequisitoRadicacion C�digo del requisito
	 * @param codigoConvenio C�digo del convenio
	 * @return
	 * @throws SQLException
	 */
	public int eliminarRequisitoRadicacionConvenio (Connection con, int codigoRequisitoRadicacion, int codigoConvenio) throws SQLException;
	
	/**
	 * M�todo que consulta todos los requisitos de paciente
	 * para un convenio
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param codigoConvenio C�digo del convenio 
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultarRequisitoPacienteConvenio(Connection con, int codigoConvenio) throws SQLException;

	/**
	 * M�todo que consulta todos los requisitos radicacion
	 * para un convenio
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param codigoConvenio C�digo del convenio 
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultarRequisitoRadicacionConvenio(Connection con, int codigoConvenio) throws SQLException;
	
	/**
	 * M�todo que consulta los requisitos no utilizados 
	 * por un convenio
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param codigoConvenio C�digo del convenio
	 * @param codigoInstitucion C�digo de la instituci�n
	 * que realiza la b�squeda 
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator obtenerRequisitosNoUtilizadosPorConvenio (Connection con, int codigoConvenio, String codigoInstitucion) throws SQLException;
	
	/**
	 * M�todo que consulta los requisitos radicacion no utilizados 
	 * por un convenio
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param codigoConvenio C�digo del convenio
	 * @param codigoInstitucion C�digo de la instituci�n
	 * que realiza la b�squeda 
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator obtenerRequisitosRadicacionNoUtilizadosPorConvenio (Connection con, int codigoConvenio, String codigoInstitucion) throws SQLException;

	

	/**
	 * Implementaci�n del m�todo que carga los requisitos previos
	 * llenados por un paciente en una subcuenta.
	 */
	public ResultSetDecorator cargarRequisitosPacienteXSubCuentaModificacion(Connection con, int idCuenta) throws SQLException;

	
	/**
	 * Adici�n de Sebasti�n
	 * Implementaci�n del m�todo que modifica un requisito de una SUBCUENTA previo
	 * llenado por un paciente para una BD Gen�rica
	 * 
	 * @see com.princetonsa.dao.RequisitosDao#modificarRequisitoPacienteCuenta (Connection , int , int,  boolean ) throws SQLException
	 */
	public int modificarRequisitoPacienteSubCuenta (Connection con, int idSubCuenta, int codigoRequisito, boolean cumplido);
	
	
	/**
	 * M�todo que consulta los requisitos del paciente por convenio segun el tipo
	 * 
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> cargarRequisitosPacienteXConvenio(Connection con,HashMap campos);
}
