package com.princetonsa.dao.oracle.capitacion;

import com.princetonsa.dao.capitacion.EdadCarteraCapitacionDao;
import com.princetonsa.dao.sqlbase.capitacion.SqlBaseEdadCarteraCapitacionDao;
import com.princetonsa.mundo.capitacion.EdadCarteraCapitacion;

public class OracleEdadCarteraCapitacionDao implements EdadCarteraCapitacionDao
{
	/**
     * 
     * @param mundo
     * @param oldQuery
     * @return
     */
    public String armarConsultaReporte(EdadCarteraCapitacion mundo, String oldQuery)
    {
    	return SqlBaseEdadCarteraCapitacionDao.armarConsultaReporte(mundo, oldQuery);
    }

}
