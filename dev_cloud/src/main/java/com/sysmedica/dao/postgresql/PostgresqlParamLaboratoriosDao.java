package com.sysmedica.dao.postgresql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;

import com.sysmedica.dao.ParamLaboratoriosDao;
import com.sysmedica.dao.sqlbase.SqlBaseParamLaboratorios;

public class PostgresqlParamLaboratoriosDao implements ParamLaboratoriosDao {

	public int insertarMuestra(Connection con, HashMap codigoMuestra, int codigoEnfNotificable)
	{
		return SqlBaseParamLaboratorios.insertarMuestra(con,codigoMuestra,codigoEnfNotificable);
	}
	
	public int insertarPrueba(Connection con, HashMap codigoPrueba, int codigoEnfNotificable)
	{
		return SqlBaseParamLaboratorios.insertarPrueba(con,codigoPrueba,codigoEnfNotificable);
	}
	
	public int insertarAgente(Connection con, HashMap codigoAgente, int codigoEnfNotificable)
	{
		return SqlBaseParamLaboratorios.insertarAgente(con,codigoAgente,codigoEnfNotificable);
	}
	
	public int insertarResultado(Connection con, HashMap codigoResultado, int codigoEnfNotificable)
	{
		return SqlBaseParamLaboratorios.insertarResultado(con,codigoResultado,codigoEnfNotificable);
	}
	
	
	public ResultSet consultarMuestras(Connection con, int codigoEnfNotificable)
	{
		return SqlBaseParamLaboratorios.consultarMuestras(con,codigoEnfNotificable);
	}
	
	public ResultSet consultarPruebas(Connection con, int codigoEnfNotificable)
	{
		return SqlBaseParamLaboratorios.consultarPruebas(con,codigoEnfNotificable);
	}
	
	public ResultSet consultarAgentes(Connection con, int codigoEnfNotificable)
	{
		return SqlBaseParamLaboratorios.consultarAgentes(con,codigoEnfNotificable);
	}
	
	public ResultSet consultarResultados(Connection con, int codigoEnfNotificable)
	{
		return SqlBaseParamLaboratorios.consultarResultados(con,codigoEnfNotificable);
	}
	
	
	public int actualizarTipoSolicitud(Connection con, int codigoEnfNotificable, int tipoSolicitud)
	{
		return SqlBaseParamLaboratorios.actualizarTipoSolicitud(con,codigoEnfNotificable,tipoSolicitud);
	}
	
	public int consultarTipoSolicitud(Connection con, int codigoEnfNotificables)
	{
		return SqlBaseParamLaboratorios.consultarTipoSolicitud(con,codigoEnfNotificables);
	}
	
	
	public ResultSet consultarServiciosParametrizados(Connection con, int codigoEnfNotificables)
	{
		return SqlBaseParamLaboratorios.consultarServiciosParametrizados(con,codigoEnfNotificables);
	}
	
	public ResultSet consultaRapidaServicios(Connection con, int codigoCups)
	{
		return SqlBaseParamLaboratorios.consultaRapidaServicios(con,codigoCups);
	}
	
	
	public int insertarServicios(Connection con, HashMap mapaServicios, int codigoEnfNotificable, int codigoInstitucion)
	{
		return SqlBaseParamLaboratorios.insertarServicios(con,mapaServicios,codigoEnfNotificable,codigoInstitucion);
	}
	
	
	public int eliminarServicio(Connection con,int codigoEnfNotificable,int codigoServicio)
	{
		return SqlBaseParamLaboratorios.eliminarServicio(con,codigoEnfNotificable,codigoServicio);
	}
}
