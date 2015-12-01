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
package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @version 1.0, 23/11/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public interface MotDevolucionInventariosDao
{
    public abstract HashMap consultarMotivosDevolucionInventarios(Connection con, int institucion);

    
    /**
     * @param con
     * @param institucion
     * @param i
     * @return
     */
    public abstract boolean eliminarRegistro(Connection con, String codigo, int institucion);

    
    /**
     * @param con
     * @param string
     * @param boolean1
     * @param i
     * @param institucion
     * @return
     */
    public abstract boolean actualizarRegistrio(Connection con, String descripcion, boolean activo, String codigo, int institucion);


    
    /**
     * @param con
     * @param string
     * @param boolean1
     * @param i
     * @param institucion
     * @return
     */
    public abstract boolean insertarRegistro(Connection con, String descripcion, boolean activo, String codigo, int institucion);


    
    /**
     * @param con
     * @param string
     * @param institucion
     * @return
     */
    public abstract HashMap consultarMotivoDevolucionInvLLave(Connection con, String codigo, int institucion);

}
