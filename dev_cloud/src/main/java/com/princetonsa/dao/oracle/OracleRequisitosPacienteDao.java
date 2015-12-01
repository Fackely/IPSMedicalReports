/*
 * @(#)OracleRequisitosPacienteDao.java
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
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.RequisitosPacienteDao;
import com.princetonsa.dao.sqlbase.SqlBaseRequisitosPacienteDao;

/**
 * Implementación Oracle de las funciones de acceso a la fuente de datos
 * para el requisito de pacientes
 *
 * @version 1.0, Nov 22 / 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class OracleRequisitosPacienteDao implements RequisitosPacienteDao
{//SIN PROBAR FUNC. SECUENCIA
	/**
	 *  Insertar un requisto para el paciente
	 */
	private final static String insertarRequisitoStr = 	"INSERT INTO requisitos_paciente " +
																			"( codigo, descripcion,tipo_requisito, activo, institucion ) " +
																			"VALUES (seq_requisitos_paciente.nextval,?, ?, ?, ? )" ;
	
	/**
	 *  Insertar un requisto para radicacion
	 */
	private final static String insertarRequisitoRadicacionStr = 	"INSERT INTO requisitos_radicacion " +
																							"( codigo, descripcion, tipo_requisito, activo, institucion ) " +
																							"VALUES (seq_requisitos_radicacion.nextval,?, ?, ?, ? )" ;
	
	
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
									int codigoInstitucion)
	{
	    return SqlBaseRequisitosPacienteDao.insertar(con,descripcion,tipoRequisito,activo,codigoInstitucion, insertarRequisitoStr);
	}
	
	/**
	 * Inserta un requisito para el radicacion dependiendo de la institución
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
																	int codigoInstitucion)
	{
	    return SqlBaseRequisitosPacienteDao.insertarRequisitosRadicacion(con,descripcion,tipoRequisito,activo,codigoInstitucion, insertarRequisitoRadicacionStr);
	}
	
	/**
	 * Consulta  la info  de los requisitos de paciente según la institución
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public ResultSetDecorator cargarRequisitos(Connection con, int codigoInstitucion)
	{
	    return SqlBaseRequisitosPacienteDao.cargarRequisitos(con, codigoInstitucion);
	}
	
	/**
	 * Consulta  la info  de los requisitos de radicacion según la institución
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public ResultSetDecorator cargarRequisitosRadicacion(Connection con, int codigoInstitucion)
	{
	    return SqlBaseRequisitosPacienteDao.cargarRequisitosRadicacion(con, codigoInstitucion);
	}
		
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
									int codigo)
	{
	    return SqlBaseRequisitosPacienteDao.modificar(con, descripcion, tipoRequisito, activo, codigo);
	}

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
																	int codigo)
	{
	    return SqlBaseRequisitosPacienteDao.modificarRequisitosRadicacion(con, descripcion,tipoRequisito, activo, codigo);
	}
	
	/**
	 * Implementación del método que permite insertar 
	 * un nuevo requisito exigido por un convenio para
	 * una base de datos Genérica
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoRequisitoPaciente Código del requisito
	 * @param codigoConvenio Código del convenio
	 * @return
	 * @throws SQLException
	 */
	public int insertarRequisitoPacienteConvenio (Connection con, int codigoRequisitoPaciente, int codigoConvenio,int viaIngreso) throws SQLException
	{
	    return SqlBaseRequisitosPacienteDao.insertarRequisitoPacienteConvenio (con, codigoRequisitoPaciente, codigoConvenio,viaIngreso) ;
	}

	/**
	 * Implementación del método que permite insertar 
	 * un nuevo requisito radicacion exigido por un convenio para
	 * una base de datos Genérica
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoRequisitoRadicacion Código del requisito
	 * @param codigoConvenio Código del convenio
	 * @return
	 * @throws SQLException
	 */
	public int insertarRequisitoRadicacionConvenio (Connection con, int codigoRequisitoRadicacion, int codigoConvenio) throws SQLException
	{
	    return SqlBaseRequisitosPacienteDao.insertarRequisitoRadicacionConvenio (con, codigoRequisitoRadicacion, codigoConvenio) ;
	}
	
	/**
	 * Implementación del método que permite eliminar un 
	 * requisito exigido por un convenio para una base de 
	 * datos Genérica
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoRequisitoPaciente Código del requisito
	 * @param codigoConvenio Código del convenio
	 * @return
	 * @throws SQLException
	 */
	public int eliminarRequisitoPacienteConvenio (Connection con, int codigoRequisitoPaciente, int codigoConvenio,int viaIngreso) throws SQLException
	{
	    return SqlBaseRequisitosPacienteDao.eliminarRequisitoPacienteConvenio (con, codigoRequisitoPaciente, codigoConvenio,viaIngreso) ;
	}
	
	/**
	 * Implementación del método que permite eliminar un 
	 * requisito radicacion exigido por un convenio para una base de 
	 * datos Genérica
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoRequisitoRadicacion Código del requisito
	 * @param codigoConvenio Código del convenio
	 * @return
	 * @throws SQLException
	 */
	public int eliminarRequisitoRadicacionConvenio (Connection con, int codigoRequisitoRadicacion, int codigoConvenio) throws SQLException
	{
	    return SqlBaseRequisitosPacienteDao.eliminarRequisitoRadicacionConvenio (con, codigoRequisitoRadicacion, codigoConvenio) ;
	}

	/**
	 * Implementación del método que consulta todos los 
	 * requisitos de paciente para un convenio para una
	 * base de datos Genérica
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoConvenio Código del convenio 
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultarRequisitoPacienteConvenio(Connection con, int codigoConvenio) throws SQLException
	{
	    return SqlBaseRequisitosPacienteDao.consultarRequisitoPacienteConvenio(con, codigoConvenio) ;
	}
	
	/**
	 * Implementación del método que consulta todos los 
	 * requisitos radicacion para un convenio para una
	 * base de datos Genérica
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoConvenio Código del convenio 
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator consultarRequisitoRadicacionConvenio(Connection con, int codigoConvenio) throws SQLException
	{
	    return SqlBaseRequisitosPacienteDao.consultarRequisitoRadicacionConvenio(con, codigoConvenio) ;
	}
	
	/**
	 * Implementación del método que consulta los
	 * requisitos no utilizados por un convenio
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoConvenio Código del convenio 
	 * @param codigoInstitucion Código de la institución
	 * que realiza la búsqueda 
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator obtenerRequisitosNoUtilizadosPorConvenio (Connection con, int codigoConvenio, String codigoInstitucion) throws SQLException
	{
	    return SqlBaseRequisitosPacienteDao.obtenerRequisitosNoUtilizadosPorConvenio (con, codigoConvenio, codigoInstitucion) ;
	}
	
	/**
	 * Implementación del método que consulta los
	 * requisitos radicacion no utilizados por un convenio
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoConvenio Código del convenio 
	 * @param codigoInstitucion Código de la institución
	 * que realiza la búsqueda 
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator obtenerRequisitosRadicacionNoUtilizadosPorConvenio (Connection con, int codigoConvenio, String codigoInstitucion) throws SQLException
	{
	    return SqlBaseRequisitosPacienteDao.obtenerRequisitosRadicacionNoUtilizadosPorConvenio (con, codigoConvenio, codigoInstitucion) ;
	}
	
	

	
	/**
	 * Implementación del método que carga los requisitos previos
	 * llenados por un paciente en una subcuenta.
	 */
	public ResultSetDecorator cargarRequisitosPacienteXSubCuentaModificacion(Connection con, int idCuenta) throws SQLException
	{
		return SqlBaseRequisitosPacienteDao.cargarRequisitosPacienteXSubCuentaModificacion(con, idCuenta);
	}
	
	/**
	 * Adición de Sebastián
	 * Implementación del método que modifica un requisito de una SUBCUENTA previo
	 * llenado por un paciente para una BD Genérica
	 * 
	 * @see com.princetonsa.dao.RequisitosDao#modificarRequisitoPacienteCuenta (Connection , int , int,  boolean ) throws SQLException
	 */
	public int modificarRequisitoPacienteSubCuenta (Connection con, int idSubCuenta, int codigoRequisito, boolean cumplido){
			return SqlBaseRequisitosPacienteDao.modificarRequisitoPacienteSubCuenta(con,idSubCuenta,codigoRequisito,cumplido);
		}
	
	
	
	/**
	 * Método que consulta los requisitos del paciente por convenio segun el tipo
	 * 
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> cargarRequisitosPacienteXConvenio(Connection con,HashMap campos)
	{
		return SqlBaseRequisitosPacienteDao.cargarRequisitosPacienteXConvenio(con, campos);
	}
}
