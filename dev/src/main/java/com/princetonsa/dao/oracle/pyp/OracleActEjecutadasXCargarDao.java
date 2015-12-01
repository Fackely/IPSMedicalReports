/*
 * Nov 14, 2006
 */
package com.princetonsa.dao.oracle.pyp;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.pyp.ActEjecutadasXCargarDao;
import com.princetonsa.dao.sqlbase.pyp.SqlBaseActEjecutadasXCargarDao;
/**
 * @author Sebastián Gómez
 *
 * Clase que maneja los métodos propìos de Oracle para el acceso a la fuente
 * de datos en la funcionalidad Actividades PYP Ejecutadas por Cargar
 */
public class OracleActEjecutadasXCargarDao implements ActEjecutadasXCargarDao 
{
	/**
	 * Método implementado para cargar las ordenes ambulatorias pendientes por cargar de un convenio específico
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarOrdenesAmbXConvenio(Connection con,HashMap campos)
	{
		return SqlBaseActEjecutadasXCargarDao.consultarOrdenesAmbXConvenio(con,campos);
	}
	
	/**
	 * Método implementado para cargar información detallada de una orden ambulatoria
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarDetalleOrdenAmb(Connection con,HashMap campos)
	{
		return SqlBaseActEjecutadasXCargarDao.consultarDetalleOrdenAmb(con,campos);
	}
}
