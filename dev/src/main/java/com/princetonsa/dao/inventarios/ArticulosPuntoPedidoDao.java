/*
 * Created on 2/12/2005
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
 * @version 1.0, 2/12/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public interface ArticulosPuntoPedidoDao
{

    
    /**
     * @param con
     * @param institucion
     * @param porcAleta
     */
    public abstract HashMap consultarArticulosGeneral(Connection con, String porcAlerta, int institucion);

    
    /**
     * @param con
     * @param porcAlerta
     * @param codBusqueda
     * @param desBusqueda
     * @return
     */
    public abstract HashMap consultarArticulosAvanzada(Connection con, String porcAlerta, String codBusqueda, String desBusqueda, int institucion);
    
}
