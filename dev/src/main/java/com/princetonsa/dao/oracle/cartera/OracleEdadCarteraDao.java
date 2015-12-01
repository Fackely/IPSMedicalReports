package com.princetonsa.dao.oracle.cartera;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.cartera.EdadCarteraDao;
import com.princetonsa.dao.sqlbase.cartera.SqlBaseEdadCarteraDao;
import com.princetonsa.mundo.cartera.EdadCartera;

/**
 * @author wilson
 *
 */
public class OracleEdadCarteraDao implements EdadCarteraDao 
{
	/**
     * 
     * @param mundo
     * @param oldQuery
     * @return
     */
    public String armarConsultaReporte(EdadCartera mundo, String oldQuery)
    {
    	return SqlBaseEdadCarteraDao.armarConsultaReporte(mundo, oldQuery);
    }
    
    /**
     * @param query
     */
    public HashMap ejecutarConsultaReporte(Connection con, String query)
    {
    	return SqlBaseEdadCarteraDao.ejecutarConsultaReporte(con, query);
    }
}
