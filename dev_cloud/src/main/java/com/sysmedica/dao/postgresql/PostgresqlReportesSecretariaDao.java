package com.sysmedica.dao.postgresql;

import com.sysmedica.dao.ReportesSecretariaDao;
import com.sysmedica.dao.sqlbase.SqlBaseReportesSecretariaDao;

import java.sql.Connection;
import java.sql.ResultSet;

public class PostgresqlReportesSecretariaDao implements ReportesSecretariaDao {

	private String secuencia = "SELECT nextval('seq_archivos_reportes')";
	
	public ResultSet consultaMorbilidad(Connection con,int semana,int anyo)
	{
		return SqlBaseReportesSecretariaDao.consultaMorbilidad(con,semana,anyo);
	}
	
	public ResultSet consultaMortalidad(Connection con,int semana,int anyo)
	{
		return SqlBaseReportesSecretariaDao.consultaMortalidad(con,semana,anyo);
	}
	
	public ResultSet consultaBrotes(Connection con,int semana,int anyo)
	{
		return SqlBaseReportesSecretariaDao.consultaBrotes(con,semana,anyo);
	}
	
	public int insertarRegistroArchivo(Connection con,
										String ruta,
										int tipo,
										String autor,
										int semana,
										int anyo)
	{
		return SqlBaseReportesSecretariaDao.insertarRegistroArchivo(con,ruta,tipo,autor,semana,anyo,secuencia);
	}
	
	public int modificarRegistroArchivo(Connection con, int semana, int anyo, String autor)
	{
		return SqlBaseReportesSecretariaDao.modificarRegistroArchivo(con,semana,anyo,autor);
	}
	
	public ResultSet consultaArchivos(Connection con, 
										boolean consultarMorbilidad, 
										boolean consultarMortalidad, 
										boolean consultarBrotes,
										boolean consultarSivim,
										boolean consultarSisvan,
										int semana,
										int anyo)
	{
		return SqlBaseReportesSecretariaDao.consultaArchivos(con,consultarMorbilidad,consultarMortalidad,consultarBrotes,consultarSivim,consultarSisvan,semana,anyo);
	}
	
	
	public boolean consultaExisteArchivo(Connection con,int tipo,int semana, int anyo)
	{
		return SqlBaseReportesSecretariaDao.consultaExisteArchivo(con,tipo,semana,anyo);
	}
}
