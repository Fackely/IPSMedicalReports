/*
 * @(#)OracleAntecedentesAlergiasDao.java
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
import com.princetonsa.dto.comun.DtoDatosGenericos;

import java.sql.SQLException;
import java.util.ArrayList;

import com.princetonsa.dao.AntecedentesAlergiasDao;
import com.princetonsa.dao.sqlbase.SqlBaseAntecedentesAlergiasDao;


/*
 * @version 1.0, Ago 12, 2003
 * @author 	<a href="mailto:Sandra@PrincetonSA.com">Sandra Moya</a> 
 */
public class OracleAntecedentesAlergiasDao implements AntecedentesAlergiasDao
{
	
	/**
	 * Implementación de la inserción de antecedentes alergias para
	 * una BD Oracle (Transaccionalidad se debe manejar en capa más arriba) 
	 * 
	 * @see com.princetonsa.dao.AntecedentesAlergiasDao#insertarAntecedentesAlergias(con, int, String, String, String, String, String, String, String, String, int, int, int, int, String, String)
	 */
	public int insertarAntecedentesAlergias(Connection con, int codigoPaciente, String observaciones) throws SQLException
	{
		return SqlBaseAntecedentesAlergiasDao.insertarAntecedentesAlergias(con,codigoPaciente,observaciones);
	}
		
	/**
	 * Implementación de la inserción de alergias predefinidas para
	 * una BD Oracle (Transaccionalidad se maneja en capa más arriba) 
	 * 
	 * @see com.princetonsa.dao.AntecedentesAlergiasDao#insertarAlergiasPredef(con, String, String, int, String)
	 */	
	public int insertarAlergiasPredef(Connection con, int codigoPaciente, int codigo, String observaciones ) throws SQLException
	{
			return SqlBaseAntecedentesAlergiasDao.insertarAlergiasPredef(con,codigoPaciente,codigo,observaciones);
	}
	
	/**
	 * Implementación de la inserción de alergias adicionales para
	 * una BD Oracle (Transaccionalidad se maneja en capa más arriba) 
	 * 
	 * @see com.princetonsa.dao.AntecedentesAlergiasDao#insertarAlergiasAdic(con, String, String, int, int, String)
	 */	
	public int insertarAlergiasAdic(Connection con, int codigoPaciente, int categoria, int codigo, String nombre, String observaciones ) throws SQLException
	{
			return SqlBaseAntecedentesAlergiasDao.insertarAlergiasAdic(con,codigoPaciente,categoria,codigo,nombre,observaciones);
	}
	
	/**
	 * Implementación de la carga de antecedentes alergias en una BD Oracle
	 * 
	 * @see com.princetonsa.dao.AntecedentesAlergiasDao#cargarAntecedentesAlergias(con, String, String)
	 */
	public ResultSetDecorator cargarAntecedentesAlergias (Connection con, int codigoPaciente) throws SQLException
	{		
			return SqlBaseAntecedentesAlergiasDao.cargarAntecedentesAlergias(con,codigoPaciente);
	}
	
	/**
	 * Implementación de la carga de alergias predefinidas en una BD Oracle
	 * 
	 * @see com.princetonsa.dao.AntecedentesAlergiasDao#cargarAlergiasPredef(con, String, String)
	 */	
	public ResultSetDecorator cargarAlergiasPredef (Connection con, int codigoPaciente) throws SQLException
	{
		return SqlBaseAntecedentesAlergiasDao.cargarAlergiasPredef(con,codigoPaciente);
	}

	/**
	 * Implementación de la carga de alergias adicionales en una BD Oracle
	 * 
	 * @see com.princetonsa.dao.AntecedentesAlergiasDao#cargarAlergiasAdic(con, String, String)
	 */	
	public ResultSetDecorator cargarAlergiasAdic (Connection con, int codigoPaciente) throws SQLException
	{
		return SqlBaseAntecedentesAlergiasDao.cargarAlergiasAdic(con,codigoPaciente);			
	}
	
	/**
	 * Implementación de la actualizacion de los campos modificables de antecedentes alergias para
	 * una BD Oracle
	 * 
	 * @see com.princetonsa.dao.AntecedentesAlergiasDao#modificarAntecedentesAlergias(con, String, String, String) 
	 */
	public int modificarAntecedentesAlergias(Connection con, int codigoPaciente, String observaciones) throws SQLException
	{
		return SqlBaseAntecedentesAlergiasDao.modificarAntecedentesAlergias(con,codigoPaciente,observaciones);
	}
	
	/**
	 * Implementación de la actualizacion de los campos modificables de alergias predefinidas para
	 * una BD Oracle
	 * 
	 * @see com.princetonsa.dao.AntecedentesAlergiasDao#modificarAlergiasPredef(con, String, String, int, String) 
	 */	
	public int modificarAlergiasPredef(Connection con, int codigoPaciente, int codigo, String observaciones ) throws SQLException
	{
		return SqlBaseAntecedentesAlergiasDao.modificarAlergiasPredef(con,codigoPaciente,codigo,observaciones);
	}
	
	/**
	 * Implementación de la actualizacion de los campos modificables de alergias adicionales para
	 * una BD Oracle
	 * 
	 * @see com.princetonsa.dao.AntecedentesAlergiasDao#modificarAlergiasAdic(con, String, String, int, int, String) 
	 */	
	public int modificarAlergiasAdic(Connection con, int codigoPaciente, int categoria, int codigo, String nombre, String observaciones ) throws SQLException
	{
		return SqlBaseAntecedentesAlergiasDao.modificarAlergiasAdic(con,codigoPaciente,categoria,codigo,nombre,observaciones);
	}

	
	
	@Override
	public ArrayList<DtoDatosGenericos> cargarAlergiasAdicNoRs(Connection con,int codigoPaciente) throws SQLException {
		return SqlBaseAntecedentesAlergiasDao.cargarAlergiasAdicNoRs(con,codigoPaciente);		
	}
}