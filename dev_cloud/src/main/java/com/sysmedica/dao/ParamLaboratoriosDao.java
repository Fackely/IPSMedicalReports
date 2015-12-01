package com.sysmedica.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;

public interface ParamLaboratoriosDao {

	public int insertarMuestra(Connection con, HashMap codigoMuestra, int codigoEnfNotificable);
	
	public int insertarPrueba(Connection con, HashMap codigoPrueba, int codigoEnfNotificable);
	
	public int insertarAgente(Connection con, HashMap codigoAgente, int codigoEnfNotificable);
	
	public int insertarResultado(Connection con, HashMap codigoResultado, int codigoEnfNotificable);
	
	
	public ResultSet consultarMuestras(Connection con, int codigoEnfNotificable);
	
	public ResultSet consultarPruebas(Connection con, int codigoEnfNotificable);
	
	public ResultSet consultarAgentes(Connection con, int codigoEnfNotificable);
	
	public ResultSet consultarResultados(Connection con, int codigoEnfNotificable);
	
	
	public int actualizarTipoSolicitud(Connection con, int codigoEnfNotificable, int tipoSolicitud);
	
	public int consultarTipoSolicitud(Connection con, int codigoEnfNotificables);
	
	
	public ResultSet consultarServiciosParametrizados(Connection con,int codigoEnfNotificables);
	
	public ResultSet consultaRapidaServicios(Connection con, int codigoCups);
	
	public int insertarServicios(Connection con, HashMap mapaServicios, int codigoEnfNotificable, int codigoInstitucion);
	
	public int eliminarServicio(Connection con,int codigoEnfNotificable,int codigoServicio);
}
