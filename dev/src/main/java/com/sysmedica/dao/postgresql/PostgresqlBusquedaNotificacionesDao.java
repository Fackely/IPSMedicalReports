package com.sysmedica.dao.postgresql;

import java.sql.Connection;
import java.util.Collection;
import com.sysmedica.dao.BusquedaNotificacionesDao;

import com.sysmedica.dao.sqlbase.SqlBaseBusquedaNotificacionesDao;

public class PostgresqlBusquedaNotificacionesDao implements BusquedaNotificacionesDao {
		
	public Collection consultaNotificaciones(Connection con,
												String fechaInicial,
												String fechaFinal,
												String diagnostico,
												String loginUsuario,
												String loginUsuarioBusqueda,
												int tipoNotificacion) {
	
		return SqlBaseBusquedaNotificacionesDao.consultaNotificaciones(con,fechaInicial,fechaFinal,diagnostico,loginUsuario,loginUsuarioBusqueda,tipoNotificacion);
	}
	
	
}
