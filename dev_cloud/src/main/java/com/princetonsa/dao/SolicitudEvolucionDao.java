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
	 * Método que inserta una solicitud de Evolución
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoSolicitud Número de la solicitud
	 * @param codigoEvolucion Código de la evolución
	 * @return
	 * @throws SQLException
	 */
	public int insertarSolicitudEvolucion (Connection con, int codigoSolicitud, int codigoEvolucion) throws SQLException;
	
	/**
	 * Método que actualiza el estado a interpretado de todas
	 * las solicitudes de evolución dado el código de la valoración
	 * a la que están asociadas
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoValoracionAsociada Código de la valoración
	 * @return
	 * @throws SQLException
	 */
	public int interpretarSolicitudesEvolucionDadaValoracion(Connection con, int codigoValoracionAsociada) throws SQLException;
	
	/**
	 * Implementación del método para interpretar todas las solicitudes 
	 * de evolución asociadas a una valoración particular y un estado de HC en una BD 
	 * Genérica
	 *
	 * @see com.princetonsa.dao.SolicitudEvolucionDao#interpretarSolicitudesEvolucionDadaValoracionYEstadoHC(Connection , int ) 
	 */
	public  int interpretarSolicitudesEvolucionDadaValoracionYEstadoHC(Connection con, int codigoValoracionAsociada); 
	
	/**
	 * Método que actualiza el estado a interpretado la solicitud de 
	 * evolución dado el código de la evolución
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoEvolucion Código de la evolución
	 * @return
	 * @throws SQLException
	 */
	public int interpretarSolicitudesEvolucionDadaEvolucion(Connection con, int codigoEvolucion) throws SQLException;
}
