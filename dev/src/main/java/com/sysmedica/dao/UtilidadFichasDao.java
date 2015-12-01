package com.sysmedica.dao;

import java.sql.Connection;
import java.sql.ResultSet;

import com.sysmedica.dao.sqlbase.SqlBaseUtilidadFichasDao;

public interface UtilidadFichasDao {

	public int eliminarFichasInactivas(Connection con, String loginUsuario,int codigoPaciente);
	
	public int activarFichaPorCodigo(Connection con, int codigoFicha, int codigoEnfermedadesNotificables);
	
	public int insertarLogFichasReportadas(Connection con, String loginUsuario,int codigoPaciente,String acronimo,int tipoCie,int numeroSolicitud);
	
	public int eliminarFichaPorCodigo(Connection con, int codigoFicha, int codigoEnfermedadesNotificables);
	
	public ResultSet consultarParametrosFichas(Connection con,int codigo);
	
	public ResultSet consultarParametrosFichasExternas(Connection con, int codigo);
	
	public int consultarFichaInactiva(Connection con, int codigoEnfNotificable, int codigoPaciente, String loginUsuario);
}
