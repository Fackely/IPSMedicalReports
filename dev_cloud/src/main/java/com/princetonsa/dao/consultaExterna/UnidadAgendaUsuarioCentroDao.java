package com.princetonsa.dao.consultaExterna;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.consultaExterna.UnidadAgendaUsuarioCentro;

public interface UnidadAgendaUsuarioCentroDao
{
	/**
	 * Metodo para Consultar Unidad Agenda Principal
	 * @param con
	 * @return
	 */
	public HashMap<String, Object> consultaUA (Connection con, int centroAtencion,String usuario);
	
	public HashMap<String, Object> consultaUA1 (Connection con, int centroAtencion,String usuario);
	
	public HashMap<String, Object> consultaUAP (Connection con, int centroAtencion, int unidadAgenda, String usuarioAutorizado);
	
	public HashMap<String, Object> consultaUnidades (Connection con, int centroAtencion);
	
	public HashMap<String, Object> consultaUsuarios (Connection con);
	
	public HashMap<String, Object> consultaActividades (Connection con,UsuarioBasico user);
	
	public boolean insertarUnidad(Connection con, UnidadAgendaUsuarioCentro mundo);
	
	public boolean insertarActividadxUnidad(Connection con, UnidadAgendaUsuarioCentro mundo);
	
	public boolean eliminar(Connection con, int codigo);
	
	public boolean modificar(Connection con, UnidadAgendaUsuarioCentro mundo);
	
	public boolean insertarActModificar(Connection con, UnidadAgendaUsuarioCentro mundo, int codigoAct);
	
	public boolean eliminarActModificar(Connection con, UnidadAgendaUsuarioCentro mundo, int codigoAct);
}