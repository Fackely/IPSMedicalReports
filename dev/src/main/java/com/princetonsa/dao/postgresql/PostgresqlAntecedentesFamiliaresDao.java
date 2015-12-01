package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import com.princetonsa.decorator.ResultSetDecorator;

import com.princetonsa.dao.AntecedentesFamiliaresDao;
import com.princetonsa.dao.sqlbase.SqlBaseAntecedentesFamiliaresDao;

public class PostgresqlAntecedentesFamiliaresDao implements AntecedentesFamiliaresDao
{
	public int insertarFamiliaresTipos(Connection con, int codigoPaciente, int tipoEnfFamiliar, String observaciones, String parentesco) throws SQLException
	{
		return SqlBaseAntecedentesFamiliaresDao.insertarFamiliaresTipos(con, codigoPaciente, tipoEnfFamiliar, observaciones, parentesco);
	}
	
	public int insertarFamiliaresOtros(Connection con, int codigoPaciente, int codigo, String nombre, String observaciones, String parentesco) throws SQLException
	{
		return SqlBaseAntecedentesFamiliaresDao.insertarFamiliaresOtros(con, codigoPaciente, codigo, nombre, observaciones, parentesco);
	}
	
	/**
	* Implementación de la inserción de antecedentes alergias para una BD
	* Postgresql (Transaccionalidad se debe manejar en capa más arriba)
	* @see com.princetonsa.dao.
	* AntecedentesAlergiasDao#insertarAntecedentesAlergias(con, int, String,
	* String, String, String, String, String, String, String, int, int, int,
	* int, String, String)
	*/
	public int insertarFamiliares(Connection con, int codigoPaciente, String observacionesGenerales) throws SQLException
	{
		return SqlBaseAntecedentesFamiliaresDao.insertarFamiliares(con, codigoPaciente, observacionesGenerales);
	}
		
	public ResultSetDecorator cargarFamiliares(Connection con, int codigoPaciente)throws SQLException
	{
		return SqlBaseAntecedentesFamiliaresDao.cargarFamiliares(con, codigoPaciente);
	}
			
	public ResultSetDecorator cargarFamiliaresTipos(Connection con, int codigoPaciente)throws SQLException
	{
		return SqlBaseAntecedentesFamiliaresDao.cargarFamiliaresTipos(con, codigoPaciente);
	}
				
	public ResultSetDecorator cargarFamiliaresOtros(Connection con, int codigoPaciente)throws SQLException
	{
		return SqlBaseAntecedentesFamiliaresDao.cargarFamiliaresOtros(con, codigoPaciente);
	}
				
	public int modificarFamiliaresTipos(Connection con, int codigoPaciente, int tipoEnfFamiliar, String observaciones) throws SQLException
	{
		return SqlBaseAntecedentesFamiliaresDao.modificarFamiliaresTipos(con, codigoPaciente, tipoEnfFamiliar, observaciones);
	}
		
	public int modificarFamiliaresOtros(Connection con, int codigoPaciente, int codigo, String observaciones) throws SQLException
	{
		return SqlBaseAntecedentesFamiliaresDao.modificarFamiliaresOtros(con, codigoPaciente, codigo, observaciones);
	}
		
	public int modificarFamiliares(Connection con, int codigoPaciente, String observacionesGenerales) throws SQLException
	{
		return SqlBaseAntecedentesFamiliaresDao.modificarFamiliares(con,codigoPaciente, observacionesGenerales);
	}
}
