/*
* @(#)OracleSolicitudConsultaExternaDao.java
*
* Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
*
* Lenguaje		: Java
* Compilador	: J2SDK 1.4.1_01
*/
package com.princetonsa.dao.oracle;


import java.sql.Connection;
import java.sql.SQLException;

import java.util.HashMap;

import com.princetonsa.dao.SolicitudConsultaExternaDao;
import com.princetonsa.dao.sqlbase.SqlBaseSolicitudConsultaExternaDao;

/**
* Esta clase implementa el contrato estipulado en <code>SolicitudConsultaExternaDao</code>, y presta
* los servicios de acceso a una base de datos Oracle requeridos por la clase
* <code>SolicitudConsultaExterna</code>
*
* @version 1.0, Abr 03, 2004
* @author <a href="mailto:edgar@PrincetonSA.com">Edgar Prieto</a>
*/
public class OracleSolicitudConsultaExternaDao implements SolicitudConsultaExternaDao
{
	/**
	* Carga los datos de una solicitud de consulta externa desde una 
	* BD Oracle
	*  
	* @param ac_con				Conexión a la fuente de datos
	* @param ai_numeroSolicitud	Número de la solicitud de consulta externa a cargar
	*/
	public HashMap cargar(Connection ac_con, int ai_numeroSolicitud)throws SQLException
	{
		return SqlBaseSolicitudConsultaExternaDao.cargar(ac_con, ai_numeroSolicitud);
	}

	/**
	* Inserta una solicitud de consulta externa en una BD Oracle
	* 
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
	)throws SQLException
	{
		return SqlBaseSolicitudConsultaExternaDao.insertarTransaccional(ac_con, as_estado, ai_numeroSolicitud, ai_codigoServicio);
	}
}