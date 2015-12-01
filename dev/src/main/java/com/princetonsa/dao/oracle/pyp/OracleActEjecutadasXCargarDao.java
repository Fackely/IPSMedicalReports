/*
 * Nov 14, 2006
 */
package com.princetonsa.dao.oracle.pyp;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.pyp.ActEjecutadasXCargarDao;
import com.princetonsa.dao.sqlbase.pyp.SqlBaseActEjecutadasXCargarDao;
/**
 * @author Sebasti�n G�mez
 *
 * Clase que maneja los m�todos prop�os de Oracle para el acceso a la fuente
 * de datos en la funcionalidad Actividades PYP Ejecutadas por Cargar
 */
public class OracleActEjecutadasXCargarDao implements ActEjecutadasXCargarDao 
{
	/**
	 * M�todo implementado para cargar las ordenes ambulatorias pendientes por cargar de un convenio espec�fico
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarOrdenesAmbXConvenio(Connection con,HashMap campos)
	{
		return SqlBaseActEjecutadasXCargarDao.consultarOrdenesAmbXConvenio(con,campos);
	}
	
	/**
	 * M�todo implementado para cargar informaci�n detallada de una orden ambulatoria
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarDetalleOrdenAmb(Connection con,HashMap campos)
	{
		return SqlBaseActEjecutadasXCargarDao.consultarDetalleOrdenAmb(con,campos);
	}
}
