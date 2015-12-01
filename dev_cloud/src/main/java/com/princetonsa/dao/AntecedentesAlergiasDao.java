/*
 * Created on May 16, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.comun.DtoDatosGenericos;

/**
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface AntecedentesAlergiasDao 
{
	/**
	 * M�todo que inserta los datos propios de una valoraci�n de urgencias.
	 * 
	 * @param con Una conexion abierta con una fuente de datos
	 * @return 0 si no se pudo insertar correctamente, 1 de lo contrario
	 * @throws SQLException
	 */
	public int insertarAntecedentesAlergias(Connection con, int codigoPaciente, String observaciones) throws SQLException;
	public int insertarAlergiasPredef(Connection con, int codigoPaciente, int codigo, String observaciones ) throws SQLException;
	public int insertarAlergiasAdic(Connection con, int codigoPaciente, int categoria, int codigo, String nombre, String observaciones ) throws SQLException;	
	
	/**
	 * M�todo que carga los datos propios de una valoraci�n de urgencias.
	 * 
	 * @param con Una conexion abierta con una fuente de datos
	 * @return ResultSetDecorator con los datos de la valoraci�n de urgencias
	 * o null si no hay una valoraci�n de urgencias asociada al n�mero de la solicitud dado por par�metro.
	 * @throws SQLException
	 */	
	public ResultSetDecorator cargarAntecedentesAlergias (Connection con, int codigoPaciente) throws SQLException;
	public ResultSetDecorator cargarAlergiasPredef (Connection con, int codigoPaciente) throws SQLException;
	public ResultSetDecorator cargarAlergiasAdic (Connection con, int codigoPaciente) throws SQLException;	
	public ArrayList<DtoDatosGenericos> cargarAlergiasAdicNoRs(Connection con, int codigoPaciente) throws SQLException;	
	
	
	/**
	 * M�todo que modifica unicamente los campos de valoracion de urgencias que son modificables.
	 *  
	 * @param con Una conexion abierta con una fuente de datos 
	 * @param numeroSolicitud N�mero de la solicitud con la que se pidi� 
	 * (reserv�) esta valoraci�n de Urgencias.
	 * @return 0 si no se pudo modificar correctamente, 1 de lo contrario
	 * @throws SQLException
	 */
	public int modificarAntecedentesAlergias(Connection con, int codigoPaciente, String observaciones) throws SQLException;
	public int modificarAlergiasPredef(Connection con, int codigoPaciente, int codigo, String observaciones ) throws SQLException;
	public int modificarAlergiasAdic(Connection con,int codigoPaciente, int categoria, int codigo, String nombre, String observaciones ) throws SQLException;	
}