package com.sysmedica.dao;

import java.sql.Connection;
import java.util.Collection;

public interface BusquedaNotificacionesDao {

	public Collection consultaNotificaciones(Connection con,
												String fechaInicial,
												String fechaFinal,
												String diagnostico,
												String loginUsuario,
												String loginUsuarioBusqueda,
												int tipoNotificacion);
	
}
