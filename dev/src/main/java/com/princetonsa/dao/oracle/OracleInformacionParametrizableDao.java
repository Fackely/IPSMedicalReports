/*
 * @(#)OracleInformacionParametrizableDao.java
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

import com.princetonsa.dao.InformacionParametrizableDao;
import com.princetonsa.dao.sqlbase.SqlBaseInformacionParametrizableDao;

/**
 * Clase que implementa toda la funcionalidad relacionada con
 * datos parametrizables por el usuario en la Base de Datos 
 * Oracle
 *
 * @version 1.0, Oct 29, 2003
 */
public class OracleInformacionParametrizableDao implements InformacionParametrizableDao 
{//SIN PROBAR FUNC. SECUENCIA
    /**
     * Cadena constante con el <i>statement</i> necesario 
     * para insertar un campo parametrizado en una BD
     * Oracle 
     */
	private final static String insertarCampoParametrizadoStr="INSERT INTO param_asociadas (codigo, orden, seccion, tipo, nombre, codigo_medico, centro_costo, institucion, alcance_campo, activo) values (seq_param_asociadas.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	/**
	 * Implementación de la búsqueda de los campos que puede llenar un médico en
	 * particular para una funcionalidad particular (datos parametrizados) en una BD 
	 * Oracle
	 * 
	 * @see com.princetonsa.dao.InformacionParametrizableDao#buscarSeccionesYCamposDadaFuncionalidad (Connection , int , String , String, int, String ) throws SQLException
	 */
	public ResultSetDecorator buscarSeccionesYCamposDadaFuncionalidad (Connection con, int idFuncionalidad, int codigoMedico, int codigoCentroCosto, String codigoInstitucion) throws SQLException
	{
		return SqlBaseInformacionParametrizableDao.buscarSeccionesYCamposDadaFuncionalidad(con,idFuncionalidad,codigoMedico, codigoCentroCosto, codigoInstitucion);
	}

	/**
	 * Método que busca los campos parametrizados 
	 * previamente por un médico para una BD Oracle
	 * 
	 * @see com.princetonsa.dao.InformacionParametrizableDao#buscarCamposParametrizadosPreviamente (Connection , int ) 
	 */
	public ResultSetDecorator buscarCamposParametrizadosPreviamente (Connection con, int codigoMedico, int institucion) throws SQLException
	{
		return SqlBaseInformacionParametrizableDao.buscarCamposParametrizadosPreviamente(con,codigoMedico, institucion);
	}

	/**
	 * Implementación de la inserción de un campo parametrizado en 
	 * una BD Oracle, dado que es solo una inserción no tiene
	 * transacción ya que si falla la instrucción falla toda la operación
	 * (def. transacción)
	 * 
	 * @see com.princetonsa.dao.InformacionParametrizableDao#public int insertarLlenadoCampo (Connection , int , int , String ) throws SQLException
	 */
	public int insertarLlenadoCampo (Connection con, int codigoTabla, int parametrizacionAsociada, String valor) throws SQLException
	{
		return SqlBaseInformacionParametrizableDao.insertarLlenadoCampo(con,codigoTabla,parametrizacionAsociada,valor);
	}
	
	/**
	 * Implementación de la inserción de un campo parametrizado en 
	 * una BD Oracle, soportando definir el estado de la
	 * transacción.
	 * 
	 * @see com.princetonsa.dao.InformacionParametrizableDao#insertarLlenadoCampoTransaccional (Connection , int , int , String , String ) throws SQLException
	 */
	public int insertarLlenadoCampoTransaccional (Connection con, int codigoTabla, int parametrizacionAsociada, String valor, String estado) throws SQLException
	{
		return SqlBaseInformacionParametrizableDao.insertarLlenadoCampoTransaccional(con,codigoTabla,parametrizacionAsociada,valor, estado);			
	}
	
	/**
	 * Método que inserta un nuevo campo parametrizado para 
	 * una BD Oracle
	 * 
	 * @see com.princetonsa.dao.InformacionParametrizableDao#insertarCampoParametrizado (Connection , int , int , int , String , int , boolean , int , String , int , String ) throws SQLException 
	 */
	public int insertarCampoParametrizado (Connection con, int orden, int codigoSeccion, int codigoTipo, String nombre, int codigoMedico,boolean activo, int codigoCentroCosto, String codigoInstitucion, int codigoAlcance) throws SQLException
	{
		return SqlBaseInformacionParametrizableDao.insertarCampoParametrizado(con,orden,codigoSeccion, codigoTipo, nombre, codigoMedico,activo, codigoCentroCosto, codigoInstitucion, codigoAlcance,insertarCampoParametrizadoStr);	
	}
	
	/**
	 * Implementación de la inserción de un campo parametrizado en 
	 * una BD Oracle.
	 * 
	 * @see com.princetonsa.dao.InformacionParametrizableDao#insertarCampoParametrizadoTransaccional (Connection , int , int , int , String , int , boolean , String , int , String , int , String ) throws SQLException
	 */
	public int insertarCampoParametrizadoTransaccional (Connection con, int orden, int codigoSeccion, int codigoTipo, String nombre, int codigoMedico, boolean activo, int codigoCentroCosto, String codigoInstitucion, int codigoAlcance, String estado) throws SQLException
	{
		return SqlBaseInformacionParametrizableDao.insertarCampoParametrizadoTransaccional( con,  orden,  codigoSeccion,  codigoTipo,  nombre,  codigoMedico,  activo,  estado, codigoCentroCosto, codigoInstitucion, codigoAlcance, insertarCampoParametrizadoStr);
	}
		
	/**
	 * Método que actualiza un nuevo campo parametrizado 
	 * para una BD Oracle
	 * 
	 * @see com.princetonsa.dao.InformacionParametrizableDao#actualizarCampoParametrizado (Connection , int , boolean , int , int ) throws SQLException 
	 */
	public int actualizarCampoParametrizado (Connection con, boolean activo, int codigo, int codigoAlcance, int centroCosto) throws SQLException
	{
		return SqlBaseInformacionParametrizableDao.actualizarCampoParametrizado(con,activo,codigo, codigoAlcance,centroCosto);
	}

	/**
	 * Implementación de la actualización de un campo 
	 * parametrizado en una BD Oracle, soportando 
	 * definir el estado de la transacción.
	 * 
	 * @see com.princetonsa.dao.InformacionParametrizableDao#actualizarCampoParametrizadoTransaccional (Connection , int , boolean , int , int , String ) throws SQLException
	 */
	public int actualizarCampoParametrizadoTransaccional (Connection con, boolean activo, int codigo, int codigoAlcance,  int centroCosto, String estado) throws SQLException
	{
		return SqlBaseInformacionParametrizableDao.actualizarCampoParametrizadoTransaccional( con,  activo,  codigo, codigoAlcance, centroCosto,  estado);
	}
	
}
