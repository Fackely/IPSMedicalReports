package com.sysmedica.dao;

import java.sql.ResultSet;
import java.sql.Connection;

public interface ReportesSecretariaDao {

	public ResultSet consultaMorbilidad(Connection con,int semana,int anyo);
	
	public ResultSet consultaMortalidad(Connection con,int semana,int anyo);
	
	public ResultSet consultaBrotes(Connection con,int semana,int anyo);
	
	
	public int insertarRegistroArchivo(Connection con,
										String ruta,
										int tipo,
										String autor,
										int semana,
										int anyo);
	
	public int modificarRegistroArchivo(Connection con, int semana, int anyo, String autor);
	
	public ResultSet consultaArchivos(Connection con, 
										boolean consultarMorbilidad, 
										boolean consultarMortalidad, 
										boolean consultarBrotes,
										boolean consultarSivim,
										boolean consultarSisvan,
										int semana,
										int anyo);
	
	
	public boolean consultaExisteArchivo(Connection con,int tipo,int semana, int anyo);
}
