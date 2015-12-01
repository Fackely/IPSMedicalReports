package com.sysmedica.dao.oracle;

import java.sql.Connection;
import com.sysmedica.dao.sqlbase.SqlBaseBusquedaNotificacionesDao;
import java.util.Collection;
import com.sysmedica.dao.BusquedaNotificacionesDao;

public class OracleBusquedaNotificacionesDao implements BusquedaNotificacionesDao {
	
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
