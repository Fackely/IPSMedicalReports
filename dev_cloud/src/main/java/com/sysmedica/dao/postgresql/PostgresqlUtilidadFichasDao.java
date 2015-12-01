package com.sysmedica.dao.postgresql;

import java.sql.Connection;
import java.sql.ResultSet;

import com.sysmedica.dao.UtilidadFichasDao;
import com.sysmedica.dao.sqlbase.SqlBaseUtilidadFichasDao;

public class PostgresqlUtilidadFichasDao implements UtilidadFichasDao {

	private String secuenciaStr = "SELECT nextval('epidemiologia.seq_log_fichas_rep')";
	
	public int eliminarFichasInactivas(Connection con, String loginUsuario,int codigoPaciente)
	{
		return SqlBaseUtilidadFichasDao.eliminarFichasInactivas(con,loginUsuario,codigoPaciente);
	}
	
	public int activarFichaPorCodigo(Connection con, int codigoFicha, int codigoEnfermedadesNotificables)
	{
		return SqlBaseUtilidadFichasDao.activarFichaPorCodigo(con,codigoFicha,codigoEnfermedadesNotificables);
	}
	
	public int insertarLogFichasReportadas(Connection con, String loginUsuario,int codigoPaciente,String acronimo,int tipoCie,int numeroSolicitud)
	{
		return SqlBaseUtilidadFichasDao.insertarLogFichasReportadas(con,loginUsuario,codigoPaciente,acronimo,tipoCie,numeroSolicitud,secuenciaStr);
	}
	
	public int eliminarFichaPorCodigo(Connection con, int codigoFicha, int codigoEnfermedadesNotificables)
	{
		return SqlBaseUtilidadFichasDao.eliminarFichaPorCodigo(con,codigoFicha,codigoEnfermedadesNotificables);
	}
	
	public ResultSet consultarParametrosFichas(Connection con, int codigo)
	{
		return SqlBaseUtilidadFichasDao.consultarParametrosFichas(con,codigo);
	}
	
	
	public ResultSet consultarParametrosFichasExternas(Connection con, int codigo)
	{
		return SqlBaseUtilidadFichasDao.consultarParametrosFichasExternas(con,codigo);
	}
	
	
	public int consultarFichaInactiva(Connection con, int codigoEnfNotificable, int codigoPaciente, String loginUsuario)
	{
		return SqlBaseUtilidadFichasDao.consultarFichaInactiva(con,codigoEnfNotificable,codigoPaciente,loginUsuario);
	}
}
