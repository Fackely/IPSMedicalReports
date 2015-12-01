/*
 * Creado en 15-nov-2005
 * 
 * Autor Santiago Salamanca
 * SysMédica
 */
package com.sysmedica.dao.postgresql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

import com.sysmedica.dao.BusquedaFichasDao;
import com.sysmedica.dao.sqlbase.SqlBaseBusquedaFichasDao;
import com.sysmedica.dao.sqlbase.SqlBaseBusquedaNotificacionesDao;

import java.util.Collection;

/**
 * @author santiago
 *
 */
public class PostgresqlBusquedaFichasDao implements BusquedaFichasDao {

    private String secuenciaStr = "SELECT nextval('seq_notificaciones')";
    
    public Collection consultaFichas(Connection con, 
										String fechaInicial, 
										String fechaFinal, 
										String diagnostico, 
										String estado, 
										boolean verPendientes,
										String loginUsuario,
										String loginUsuarioBusqueda)
	{
        return SqlBaseBusquedaFichasDao.consultaFichas(con,fechaInicial,fechaFinal,diagnostico,estado,verPendientes,loginUsuario,loginUsuarioBusqueda);
	}
    
    
    public int notificarFicha(Connection con,
								HashMap mapaFichas,
								String codigoUsuario,
								int tipo)
    {
        return SqlBaseBusquedaFichasDao.notificarFicha(con,mapaFichas,codigoUsuario,tipo,secuenciaStr);
    }
    
    
    public Collection consultaFichasPendientes(Connection con,String loginUsuario) {
        
        return SqlBaseBusquedaFichasDao.consultaFichasPendientes(con,loginUsuario);
    }
    
    
    public Collection consultaBrotesPendientes(Connection con,String loginUsuario) {
    	
    	return SqlBaseBusquedaFichasDao.consultaBrotesPendientes(con,loginUsuario);
    }
    
    public Collection consultaBrote(Connection con, 
    								int evento, 
    								String loginUsuarioBusqueda, 
    								String loginUsuario, 
    								String fechaInicial, 
    								String fechaFinal, 
    								int estado) 
    {
		return SqlBaseBusquedaFichasDao.consultaBrote(con,evento,loginUsuarioBusqueda,loginUsuario,fechaInicial,fechaFinal,estado);
	}
    
    
    
    public ResultSet consultaFichasPorPaciente(Connection con, 
									    		int codigoPaciente, 
									    		String diagnostico,
									    		String codigoDx)
    {
    	return SqlBaseBusquedaFichasDao.consultaFichasPorPaciente(con,codigoPaciente,diagnostico,codigoDx);
    }
}
