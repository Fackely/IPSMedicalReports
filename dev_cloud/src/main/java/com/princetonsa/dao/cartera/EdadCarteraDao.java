/*
 * @(#)EdadCarteraDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK jdk1.5.0_07
 *
 */
package com.princetonsa.dao.cartera;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.mundo.cartera.EdadCartera;

/**
 * 
 * @author wilson
 *
 */
public interface EdadCarteraDao 
{
	/**
     * 
     * @param mundo
     * @param oldQuery
     * @return
     */
    public String armarConsultaReporte(EdadCartera mundo, String oldQuery);

    /**
     * 
     * @param query
     * @return
     */
	public HashMap ejecutarConsultaReporte(Connection con, String query);
}
