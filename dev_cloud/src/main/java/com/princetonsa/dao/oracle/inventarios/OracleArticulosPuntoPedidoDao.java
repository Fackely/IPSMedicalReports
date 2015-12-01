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
package com.princetonsa.dao.oracle.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.inventarios.ArticulosPuntoPedidoDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseArticulosPuntoPedidoDao;

/**
 * @version 1.0, 2/12/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class OracleArticulosPuntoPedidoDao implements ArticulosPuntoPedidoDao
{
    /**
     * @param con
     * @param porcAleta
     */
    public HashMap consultarArticulosGeneral(Connection con, String porcAlerta,int institucion)
    {
        return SqlBaseArticulosPuntoPedidoDao.consultarArticulosGeneral(con,porcAlerta,institucion);
    }
    
    /**
     * @param con
     * @param porcAlerta
     * @param codBusqueda
     * @param desBusqueda
     * @return
     */
    public HashMap consultarArticulosAvanzada(Connection con, String porcAlerta, String codBusqueda, String desBusqueda, int institucion)
    {
        return SqlBaseArticulosPuntoPedidoDao.consultarArticulosAvanzada(con,porcAlerta,codBusqueda,desBusqueda,institucion);
    }
}
