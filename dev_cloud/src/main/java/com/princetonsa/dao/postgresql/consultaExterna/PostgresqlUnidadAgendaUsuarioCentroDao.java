package com.princetonsa.dao.postgresql.consultaExterna;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.consultaExterna.UnidadAgendaUsuarioCentroDao;
import com.princetonsa.dao.sqlbase.consultaExterna.SqlBaseUnidadAgendaUsuarioCentroDao;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.consultaExterna.UnidadAgendaUsuarioCentro;

public class PostgresqlUnidadAgendaUsuarioCentroDao implements UnidadAgendaUsuarioCentroDao
{
	/**
	 * Metodo para Constultar Unidad de Agenda Principal
	 * @param con
	 * @return
	 */
	public HashMap<String, Object> consultaUA (Connection con, int centroAtencion,String loginUsuario)
	{
		return SqlBaseUnidadAgendaUsuarioCentroDao.consultaUA(con, centroAtencion,loginUsuario);
	}
	
	public HashMap<String, Object> consultaUA1 (Connection con, int centroAtencion,String loginUsuario)
	{
		return SqlBaseUnidadAgendaUsuarioCentroDao.consultaUA1(con, centroAtencion,loginUsuario);
	}
	
	public HashMap<String, Object> consultaUAP (Connection con, int centroAtencion, int unidadAgenda, String usuarioAutorizado)
	{
		return SqlBaseUnidadAgendaUsuarioCentroDao.consultaUnidadPrincipal(con, centroAtencion, unidadAgenda, usuarioAutorizado);
	}
	
	public HashMap<String, Object> consultaUnidades (Connection con, int centroAtencion)
	{
		return SqlBaseUnidadAgendaUsuarioCentroDao.consultaUnidades(con, centroAtencion);
	}
	
	public HashMap<String, Object> consultaUsuarios (Connection con)
	{
		return SqlBaseUnidadAgendaUsuarioCentroDao.consultaUsuarios(con);
	}
	
	public HashMap<String, Object> consultaActividades (Connection con,UsuarioBasico user)
	{
		return SqlBaseUnidadAgendaUsuarioCentroDao.consultaActividades(con,user);
	}
	
	public boolean insertarUnidad(Connection con, UnidadAgendaUsuarioCentro mundo)
	{
		return SqlBaseUnidadAgendaUsuarioCentroDao.insertarUnidad(con, mundo,DaoFactory.POSTGRESQL);
	}
	
	public boolean insertarActividadxUnidad(Connection con, UnidadAgendaUsuarioCentro mundo)
	{
		return SqlBaseUnidadAgendaUsuarioCentroDao.insertarActividadxUnidad(con, mundo);
	}
	
	public boolean eliminar(Connection con, int codigo)
	{
		return SqlBaseUnidadAgendaUsuarioCentroDao.eliminar(con, codigo);
	}
	
	public boolean modificar(Connection con, UnidadAgendaUsuarioCentro mundo)
	{
		return SqlBaseUnidadAgendaUsuarioCentroDao.modificar(con, mundo,DaoFactory.POSTGRESQL);
	}
	
	public boolean insertarActModificar(Connection con, UnidadAgendaUsuarioCentro mundo, int codigoAct)
	{
		return SqlBaseUnidadAgendaUsuarioCentroDao.insertarActModificar(con, mundo, codigoAct);
	}
	
	public boolean eliminarActModificar(Connection con, UnidadAgendaUsuarioCentro mundo, int codigoAct)
	{
		return SqlBaseUnidadAgendaUsuarioCentroDao.eliminarActModificar(con, mundo, codigoAct);
	}
}