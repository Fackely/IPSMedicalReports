/*
 * @(#)SolicitudEvolucionDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */

package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar 
 * la clase que presta el servicio de acceso a datos para el objeto 
 * <code>SolicitudEvolucionDao</code>.
 *
 *	@version 1.0, May 11, 2004
 */
public interface SolicitudEvolucionDao 
{

	/**
	 * M�todo que inserta una solicitud de Evoluci�n
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param codigoSolicitud N�mero de la solicitud
	 * @param codigoEvolucion C�digo de la evoluci�n
	 * @return
	 * @throws SQLException
	 */
	public int insertarSolicitudEvolucion (Connection con, int codigoSolicitud, int codigoEvolucion) throws SQLException;
	
	/**
	 * M�todo que actualiza el estado a interpretado de todas
	 * las solicitudes de evoluci�n dado el c�digo de la valoraci�n
	 * a la que est�n asociadas
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param codigoValoracionAsociada C�digo de la valoraci�n
	 * @return
	 * @throws SQLException
	 */
	public int interpretarSolicitudesEvolucionDadaValoracion(Connection con, int codigoValoracionAsociada) throws SQLException;
	
	/**
	 * Implementaci�n del m�todo para interpretar todas las solicitudes 
	 * de evoluci�n asociadas a una valoraci�n particular y un estado de HC en una BD 
	 * Gen�rica
	 *
	 * @see com.princetonsa.dao.SolicitudEvolucionDao#interpretarSolicitudesEvolucionDadaValoracionYEstadoHC(Connection , int ) 
	 */
	public  int interpretarSolicitudesEvolucionDadaValoracionYEstadoHC(Connection con, int codigoValoracionAsociada); 
	
	/**
	 * M�todo que actualiza el estado a interpretado la solicitud de 
	 * evoluci�n dado el c�digo de la evoluci�n
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param codigoEvolucion C�digo de la evoluci�n
	 * @return
	 * @throws SQLException
	 */
	public int interpretarSolicitudesEvolucionDadaEvolucion(Connection con, int codigoEvolucion) throws SQLException;
}
