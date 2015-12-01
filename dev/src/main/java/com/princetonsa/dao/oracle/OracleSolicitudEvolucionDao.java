/*
 * @(#)OracleSolicitudEvolucionDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */

package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.sql.SQLException;

import com.princetonsa.dao.SolicitudEvolucionDao;
import com.princetonsa.dao.sqlbase.SqlBaseSolicitudEvolucionDao;

/**
 * Esta clase implementa el contrato estipulado en <code>SolicitudEvolucionDao</code>, 
 * proporcionando los servicios de acceso a una base de datos Hsqldb 
 * requeridos por <code>SolicitudEvolucion</code>
 *
 *	@version 1.0, May 11, 2004
 */
public class OracleSolicitudEvolucionDao implements SolicitudEvolucionDao
{
	/**
	 * Implementaci�n del m�todo para insertar una solicitud de 
	 * evoluci�n en una BD Oracle
	 *
	 * @see com.princetonsa.dao.SolicitudEvolucionDao#insertarSolicitudEvolucion (Connection , int , int ) throws SQLException
	 */
	public int insertarSolicitudEvolucion (Connection con, int codigoSolicitud, int codigoEvolucion) throws SQLException
	{
		return SqlBaseSolicitudEvolucionDao.insertarSolicitudEvolucion (con, codigoSolicitud, codigoEvolucion) ;
	}

	/**
	 * Implementaci�n del m�todo para interpretar todas las solicitudes 
	 * de evoluci�n asociadas a una valoraci�n particular en una BD 
	 * Oracle
	 *
	 * @see com.princetonsa.dao.SolicitudEvolucionDao#interpretarSolicitudesEvolucionDadaValoracion(Connection , int ) throws SQLException
	 */
	public int interpretarSolicitudesEvolucionDadaValoracion(Connection con, int codigoValoracionAsociada) throws SQLException
	{
		return SqlBaseSolicitudEvolucionDao.interpretarSolicitudesEvolucionDadaValoracion(con, codigoValoracionAsociada) ;
	}

	/**
	 * Implementaci�n del m�todo para interpretar todas las solicitudes 
	 * de evoluci�n asociadas a una valoraci�n particular y un estado de HC en una BD 
	 * Gen�rica
	 *
	 * @see com.princetonsa.dao.SolicitudEvolucionDao#interpretarSolicitudesEvolucionDadaValoracionYEstadoHC(Connection , int ) 
	 */
	public  int interpretarSolicitudesEvolucionDadaValoracionYEstadoHC(Connection con, int codigoValoracionAsociada)
	{
	    return SqlBaseSolicitudEvolucionDao.interpretarSolicitudesEvolucionDadaValoracionYEstadoHC(con, codigoValoracionAsociada);
	}
	
	
	/**
	 * Implementaci�n del m�todo para interpretar todas las solicitudes 
	 * de evoluci�n asociadas a una evoluci�n particular en una BD 
	 * Oracle
	 *
	 * @see com.princetonsa.dao.SolicitudEvolucionDao#interpretarSolicitudesEvolucionDadaEvolucion(Connection , int ) throws SQLException
	 */
	public int interpretarSolicitudesEvolucionDadaEvolucion(Connection con, int codigoEvolucion) throws SQLException
	{
		return SqlBaseSolicitudEvolucionDao.interpretarSolicitudesEvolucionDadaEvolucion(con, codigoEvolucion) ;
	}
}
