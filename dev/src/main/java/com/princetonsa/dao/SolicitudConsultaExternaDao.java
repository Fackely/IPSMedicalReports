/*
* @(#)SolicitudConsultaExternaDao.java
*
* Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
*
* Lenguaje		: Java
* Compilador	: J2SDK 1.4.1_01
*/
package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

/**
* Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta
* el servicio de acceso a datos para el objeto <code>SolicitudConsultaExternaDao</code>
*
* @version 1.0, Abr 03, 2004
* @author <a href="mailto:edgar@PrincetonSA.com">Edgar Prieto</a>
*/
public interface SolicitudConsultaExternaDao
{
	/**
	* Carga los datos de una solicitud de consulta externa desde una fuente de datos
	* @param ac_con				Conexión a la fuente de datos
	* @param ai_numeroSolicitud	Número de la solicitud de consulta externa a cargar
	*/
	public HashMap cargar(Connection ac_con, int ai_numeroSolicitud)throws SQLException;

	/**
	* Inserta una solicitud de consulta externa en una fuente de datos
	* @param ac_con				Conexión a la fuente de datos
	* @param as_estado			Estado de la transacción
	* @param ai_numeroSolicitud	Número de la solicitud de consulta externa a asignar. Este número
	*							debe existir en la tabla de solicitudes
	* @param ai_codigoServicio	Código del servicio de la solicitud de consulta externa
	* @return Número de solicitudes insertadas correctamente
	* @throws java.sql.SQLException si se presentó un error de base de datos
	*/
	public int insertarTransaccional(
		Connection	ac_con,
		String		as_estado,
		int			ai_numeroSolicitud,
		int			ai_codigoServicio
	)throws SQLException;
}