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
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public interface RequisitosPacienteDao 
{
	/**
	 * Inserta un requisito para el paciente dependiendo de la institución
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
	 * Inserta un requisito para radicacion dependiendo de la institución
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
	 *  Consulta  la info  de los requisitos de paciente según la institución
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public ResultSetDecorator cargarRequisitos(Connection con, int codigoInstitucion);
	
	/**
	 *  Consulta  la info  de los requisitos de radicacion según la institución
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
	 * Mètodo que permite insertar un nuevo requisito
	 * exigido por un convenio
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoRequisitoPaciente Código del requisito
	 * @param codigoConvenio Código del convenio
	 * @return
	 * @throws SQLException
	 */
	public int insertarRequisitoPacienteConvenio (Connection con, int codigoRequisitoPaciente, int codigoConvenio, int viaIngreso) throws SQLException;
	
	/**
	 * Mètodo que permite insertar un nuevo requisito radicacion
	 * exigido por un convenio
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoRequisitoRadicacion Código del requisito
	 * @param codigoConvenio Código del convenio
	 * @return
	 * @throws SQLException
	 */
	public int insertarRequisitoRadicacionConvenio (Connection con, int codigoRequisitoRadicacion, int codigoConvenio) throws SQLException;
	
	/**
	 * Método que permite eliminar un requisito exigido
	 * por un convenio
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoRequisitoPaciente Código del requisito
	 * @param codigoConvenio Código del convenio
	 * @return
	 * @throws SQLException
	 */
	public int eliminarRequisitoPacienteConvenio (Connection con, int codigoRequisitoPaciente, int codigoConvenio, int viaIngreso) throws SQLException;
	
	/**
	 * Método que permite eliminar un requisito radicacion exigido
	 * por un convenio
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoRequisitoRadicacion Código del requisito
	 * @param codigoConvenio Código del convenio
	 * @return
	 * @throws SQLException
	 */
	public int eliminarRequisitoRadicacionConvenio (Connection con, int codigoRequisitoRadicacion, int codigoConvenio) throws SQLException;
	
	/**
	 * Método que consulta todos los requisitos de paciente
	 * para un convenio
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoConvenio Código del convenio 
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultarRequisitoPacienteConvenio(Connection con, int codigoConvenio) throws SQLException;

	/**
	 * Método que consulta todos los requisitos radicacion
	 * para un convenio
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoConvenio Código del convenio 
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultarRequisitoRadicacionConvenio(Connection con, int codigoConvenio) throws SQLException;
	
	/**
	 * Método que consulta los requisitos no utilizados 
	 * por un convenio
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoConvenio Código del convenio
	 * @param codigoInstitucion Código de la institución
	 * que realiza la búsqueda 
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator obtenerRequisitosNoUtilizadosPorConvenio (Connection con, int codigoConvenio, String codigoInstitucion) throws SQLException;
	
	/**
	 * Método que consulta los requisitos radicacion no utilizados 
	 * por un convenio
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoConvenio Código del convenio
	 * @param codigoInstitucion Código de la institución
	 * que realiza la búsqueda 
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator obtenerRequisitosRadicacionNoUtilizadosPorConvenio (Connection con, int codigoConvenio, String codigoInstitucion) throws SQLException;

	

	/**
	 * Implementación del método que carga los requisitos previos
	 * llenados por un paciente en una subcuenta.
	 */
	public ResultSetDecorator cargarRequisitosPacienteXSubCuentaModificacion(Connection con, int idCuenta) throws SQLException;

	
	/**
	 * Adición de Sebastián
	 * Implementación del método que modifica un requisito de una SUBCUENTA previo
	 * llenado por un paciente para una BD Genérica
	 * 
	 * @see com.princetonsa.dao.RequisitosDao#modificarRequisitoPacienteCuenta (Connection , int , int,  boolean ) throws SQLException
	 */
	public int modificarRequisitoPacienteSubCuenta (Connection con, int idSubCuenta, int codigoRequisito, boolean cumplido);
	
	
	/**
	 * Método que consulta los requisitos del paciente por convenio segun el tipo
	 * 
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> cargarRequisitosPacienteXConvenio(Connection con,HashMap campos);
}
