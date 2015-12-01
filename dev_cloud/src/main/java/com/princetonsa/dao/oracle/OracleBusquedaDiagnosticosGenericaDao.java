/*
 * Ene 17, 2007
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.BusquedaDiagnosticosGenericaDao;
import com.princetonsa.dao.sqlbase.SqlBaseBusquedaDiagnosticosGenericaDao;

/**
 * 
 * @author Sebasti�n G�mez R.
 * Implementaci�n oracle de las funciones de acceso a la fuente de datos
 * para busqueda de diagn�sticos generica
 *
 */
public class OracleBusquedaDiagnosticosGenericaDao implements
		BusquedaDiagnosticosGenericaDao 
{
	/**
	 * M�todo implementado para consultar la fecha de una cita 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String consultaFechaCita (Connection con, String numeroSolicitud)
	{
		return SqlBaseBusquedaDiagnosticosGenericaDao.consultaFechaCita(con, numeroSolicitud);
	}
	
	/**
	 * M�todo que consulta el CIE vigente seg�n fecha
	 * @param con
	 * @param fecha
	 * @return
	 */
	public int consultaCieVigente (Connection con, String fecha)
	{
		return SqlBaseBusquedaDiagnosticosGenericaDao.consultaCieVigente(con, fecha);
	}
	
	/**
	 * M�todo que realiza la busqueda de diagnosticos
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultaDiagnosticos(Connection con, HashMap campos)
	{
		return SqlBaseBusquedaDiagnosticosGenericaDao.consultaDiagnosticos(con, campos);
	}
}
