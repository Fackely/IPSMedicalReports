/*
 * Created on 11/04/2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sies.dao;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author Juan David Ramírez
 *
 */
public interface VacacionesDao
{
    public int insertarVacaciones(Connection con, int codigo, String fecha_inicio, String fecha_fin);
    public Collection<HashMap<String, Object>> consultarVacaciones(Connection con, Integer codigoPersona, String fechaInicio, String fechaFin);
    public Collection consultarModificar(Connection con, int codigo, String fecha_inicio);
    public void modificar(Connection con, int codigo, String fecha_inicio, String fecha_fin, String fecha_ant);
    public int eliminarVacaciones(Connection con, int codigo, String fecha);
}
