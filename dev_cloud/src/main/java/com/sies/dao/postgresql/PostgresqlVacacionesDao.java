/*
 * Created on 11/04/2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sies.dao.postgresql;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

import com.sies.dao.VacacionesDao;
import com.sies.dao.sqlbase.SqlBaseVacacionesDao;

/**
 * @author karenth
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PostgresqlVacacionesDao implements VacacionesDao{

    
    
    public int insertarVacaciones(Connection con, int codigo, String fecha_inicio, String fecha_fin)
	{
		return SqlBaseVacacionesDao.insertarVacaciones(con, codigo, fecha_inicio, fecha_fin);
	}
    
    public Collection<HashMap<String, Object>> consultarVacaciones(Connection con, Integer codigoPersona, String fechaInicio, String fechaFin)
    {
    	return SqlBaseVacacionesDao.consultarVacaciones(con, codigoPersona, fechaInicio, fechaFin);
    }

    public Collection consultarModificar(Connection con, int codigo, String fecha_inicio) 
    {
        return SqlBaseVacacionesDao.consultarModificar(con, codigo, fecha_inicio);
    }
    
    
    public void modificar(Connection con, int codigo, String fecha_inicio, String fecha_fin, String fecha_ant)
	{
		SqlBaseVacacionesDao.modificar(con,codigo,fecha_inicio, fecha_fin, fecha_ant);
	}
    
    
    public int eliminarVacaciones(Connection con, int codigo, String fecha_inicio)
    {
        return SqlBaseVacacionesDao.eliminarVacaciones(con, codigo, fecha_inicio);
    }
    
}
