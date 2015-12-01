/*
 * Created on 23/11/2005
 * 
 * @author <a href="mailto:artotor@hotmail.com">Jorge Armando Osorio Velásquez</a>
 * 
 * Copyright Princeton S.A. &copy;&reg; 2005. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 *
 */
package com.princetonsa.dao.postgresql.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.inventarios.MotDevolucionInventariosDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseMotDevolucionInventariosDao;

/**
 * @version 1.0, 23/11/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class PostgresqlMotDevolucionInventariosDao implements
        MotDevolucionInventariosDao
{


    public HashMap consultarMotivosDevolucionInventarios(Connection con,int institucion)
     {
         return SqlBaseMotDevolucionInventariosDao.consultarMotivosDevolucionInventarios(con,institucion);
     }


    
    /**
     * @param con
     * @param i
     * @return
     */
    public boolean eliminarRegistro(Connection con, String codigoRegistro,int institucion)
    {
        return SqlBaseMotDevolucionInventariosDao.eliminarRegistro(con,codigoRegistro,institucion);
    }

    
    
    
    /**
     * @param con
     * @param descripcion
     * @param activo
     * @param codigo
     * @param institucion
     * @return
     */
    public boolean actualizarRegistrio(Connection con, String descripcion, boolean activo, String codigo, int institucion)
    {
        return SqlBaseMotDevolucionInventariosDao.actualizarRegistrio(con, descripcion,activo,codigo, institucion);
    }
    
    /**
     * @param con
     * @param descripcion
     * @param activo
     * @param codigo
     * @param institucion
     * @return boolean
     */
    public boolean insertarRegistro(Connection con, String descripcion, boolean activo, String codigo, int institucion)
    {
        return SqlBaseMotDevolucionInventariosDao.insertarRegistro(con, descripcion,activo,codigo, institucion);
    }
    
    
    /**
     * @param con
     * @param string
     * @param institucion
     * @return
     */
    public HashMap consultarMotivoDevolucionInvLLave(Connection con, String codigo, int institucion)
    {
        return SqlBaseMotDevolucionInventariosDao.consultarMotivoDevolucionInvLLave(con,codigo,institucion);
    }
}
