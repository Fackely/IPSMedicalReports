package com.princetonsa.dao.capitacion;

import com.princetonsa.mundo.capitacion.EdadCarteraCapitacion;

/**
 * 
 * @author wilson
 *
 */
public interface EdadCarteraCapitacionDao 
{
	/**
     * 
     * @param mundo
     * @param oldQuery
     * @return
     */
    public String armarConsultaReporte(EdadCarteraCapitacion mundo, String oldQuery);
}
