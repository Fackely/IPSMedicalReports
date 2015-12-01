/*
 * @(#)SqlBaseHistoriaClinicaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */

package com.princetonsa.dao.sqlbase;

import java.sql.CallableStatement;
import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadFecha;

/**
 * Esta clase implementa la funcionalidad com�n a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL est�ndar. M�todos particulares a HistoriaClinica
 *
 *	@version 1.0, Mar 29, 2004
 */
public class SqlBaseHistoriaClinicaDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseHistoriaClinicaDao.class);
	

	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar una historia cl�nica de la base de datos
	 */
	private static final String cargarHistoriaClinica="select fecha_apertura as fechaApertura, historia_clinica_anterior as historiaClinicaAnterior from historias_clinicas where codigo_paciente=?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para modificar una historia cl�nica en la base de datos
	 */
	private static final String modificarHistoriaClinica="update historias_clinicas set fecha_apertura=? where codigo_paciente=?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar los
	 * nombres y c�digos de las instituciones en las que este paciente tiene
	 * historia cl�nica
	 */
	private static final String cargarInstitucionesConHistoriaStr ="SELECT pi.codigo_institucion as codigoInstitucion, emp.nombre as nombreInstitucion from pacientes_instituciones pi, empresas emp where pi.codigo_institucion=emp.codigo and pi.codigo_paciente=? ";

	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar una historia cl�nica en la base de datos
	 */
	private static final String insertarHistoriaClinicaStr="insert into historias_clinicas (historia_clinica_anterior, codigo_paciente, fecha_apertura) VALUES (?,?, CURRENT_DATE)";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para modificar la historia previa
	 */
	private static final String modificarHistoriaPreviaStr="update historias_clinicas set historia_clinica_anterior=? where codigo_paciente=?";


	/**
	 * Carga los datos de una historia cl�nica desde la base de datos PostgresSQL
	 * @param con una conexion abierta con una base de datos PostgresSQL
	 * @param tipoId tipo de identificacion del paciente al cual pertenece la historia cl�nica
	 * @param numeroId n�mero de identificacion del paciente al cual pertenece la historia cl�nica
	 * @return un objeto <code>Answer</code> con los datos pedidos y una conexi�n abierta.
	 */
	public static ResultSetDecorator cargarHistoriaClinica(Connection con, int codigoPaciente) throws SQLException
	{
		ResultSetDecorator rs=null;
		PreparedStatementDecorator cargarHistoriaClinicaStatement= new PreparedStatementDecorator(con.prepareStatement(cargarHistoriaClinica,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cargarHistoriaClinicaStatement.setInt(1,codigoPaciente);
		
		rs=new ResultSetDecorator(cargarHistoriaClinicaStatement.executeQuery());
		return rs;
	}

	/**
	 * Modifica una historia cl�nica ya existente en una base de datos PostgresSQL, reutilizando 
	 * una conexion existente, con los datos pasados como par�metro.
	 * @param con una conexion abierta con una base de datos PostgresSQL
	 * @param tipoId tipo de identificacion del paciente al cual pertenece la historia cl�nica
	 * @param anioApertura a�o de apertura de la historia cl�nica
	 * @param mesApertura mes de apertura de la historia cl�nica
	 * @param diaApertura d�a de apertura de la historia cl�nica
	 * @param numeroId n�mero de identificacion del paciente al cual pertenece la historia cl�nica
	 * @return n�mero de historias modificadas (debe ser 1)
	 */
	public static int modificarHistoriaClinica(Connection con, int codigoPaciente, String fechaApertura) throws SQLException
	{
		int resp=0;

		CallableStatement modificarHistoriaClinicaStatement=con.prepareCall(modificarHistoriaClinica,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
		modificarHistoriaClinicaStatement.setString(1, UtilidadFecha.conversionFormatoFechaABD(fechaApertura));
		modificarHistoriaClinicaStatement.setInt(2, codigoPaciente);
		resp=modificarHistoriaClinicaStatement.executeUpdate();
		return resp;
	}

	/**
	 * Carga los datos de todas las instituciones en las cuales este paciente
	 * tiene historia cl�nica de una historia cl�nica desde la base de datos
	 * PostgresSQL
	 * 
	 * @param con una conexion abierta con una base de datos PostgresSQL
	 * @param tipoId tipo de identificacion del paciente al cual pertenece la historia cl�nica
	 * @param numeroId n�mero de identificacion del paciente al cual pertenece la historia cl�nica
	 * @return un objeto <code>Answer</code> con los datos pedidos y una conexi�n abierta.
	 */
	public static ResultSetDecorator cargarInstitucionesConHistoria(Connection con, int codigoPaciente) throws SQLException
	{
		ResultSetDecorator rs=null;
		
		PreparedStatementDecorator cargarInstitucionesConHistoriaStatement= new PreparedStatementDecorator(con.prepareStatement(cargarInstitucionesConHistoriaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cargarInstitucionesConHistoriaStatement.setInt(1,codigoPaciente);
		
		rs=new ResultSetDecorator(cargarInstitucionesConHistoriaStatement.executeQuery());
		return rs;
	}

	/**
	 * Inserta una historia clinica en una base de datos PostgresSQL, reutilizando 
	 * una conexion existente, con los datos presentes en los atributos 
	 * de este objeto.
	 * @param con una conexion abierta con una base de datos PostgresSQL
	 * @param tipoId tipo de identificacion del paciente al cual pertenece la historia cl�nica
	 * @param codigoPaciente C�digo que identifica al paciente al cual pertenece la historia cl�nica
	 * @return n�mero de historias insertadas (debe ser 1)
	 */
	public static int insertarHistoriaClinica(Connection con, String historiaClinicaAnterior, int codigoPaciente) throws SQLException
	{
		PreparedStatementDecorator insertarHistoriaClinicaStatement=  new PreparedStatementDecorator(con.prepareStatement(insertarHistoriaClinicaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		insertarHistoriaClinicaStatement.setString(1, historiaClinicaAnterior);
		insertarHistoriaClinicaStatement.setInt(2, codigoPaciente);
		return insertarHistoriaClinicaStatement.executeUpdate();
	}
	
	/**
	 * M�todo implementado para modificar la descripcion de la historia clinica previa del paciente
	 * @param con
	 * @param codigoPaciente
	 * @param historiaAnterior
	 * @return
	 */
	public static int modificarHistoriaPrevia(Connection con,int codigoPaciente,String historiaAnterior)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(modificarHistoriaPreviaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,historiaAnterior);
			pst.setInt(2,codigoPaciente);
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en modificarHistoriaClinica de SqlBaseHistoriaClinicaDao: "+e);
			return -1;
		}
	}


}
