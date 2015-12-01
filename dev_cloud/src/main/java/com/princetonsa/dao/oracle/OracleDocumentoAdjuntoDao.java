/*
* @(#)OracleDocumentoAdjuntoDao.java
*
* Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
*
* Lenguaje		: Java
* Compilador	: J2SDK 1.4.1_01
*/
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.sql.SQLException;

import util.ResultadoBoolean;

import com.princetonsa.dao.DocumentoAdjuntoDao;
import com.princetonsa.dao.sqlbase.*;

/**
 * Implementación de la interfaz para el acceder a la fuente de datos de un
 * documento adjunto
 *
 * @version 1.0, Febrero 26 / 2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:juandavid@PrincetonSA.com">Juan David Ramirez</a>
 */

public class OracleDocumentoAdjuntoDao implements DocumentoAdjuntoDao
{
	/**
	 * @see com.princetonsa.dao.DocumentoAdjuntoDao#insertarDocumentoAdjunto(java.sql.Connection, int, java.lang.String, java.lang.String, boolean)
	 */
	public ResultadoBoolean insertarDocumentoAdjunto(Connection con, int numeroSolicitud, String nombreArchivo, String nombreOriginal, boolean es_solicitud, int codigoMedico, String codigoRespuestaSolicitud)
	{
	    return SqlBaseDocumentoAdjuntoDao.insertarDocumentoAdjunto(con, numeroSolicitud, nombreArchivo, nombreOriginal, es_solicitud, codigoMedico, codigoRespuestaSolicitud);
	}
	
	/**
	 * @see com.princetonsa.dao.DocumentoAdjuntoDao#existeDocumentoadjunto(java.sql.Connection, int)
	 */	
	public ResultadoBoolean existeDocumentoadjunto(Connection con, int codigoArchivo)
	{
		return SqlBaseDocumentoAdjuntoDao.existeDocumentoadjunto(con, codigoArchivo);
	}
	
	/**
	 * @see com.princetonsa.dao.DocumentoAdjuntoDao#insertarDocumentoAdjunto(java.sql.Connection, int, java.lang.String, java.lang.String, boolean, java.lang.String)
	 */
	public ResultadoBoolean insertarDocumentoAdjuntoTransaccional(	Connection con,
																							int numeroSolicitud,
																							String nombreArchivo,
																							String nombreOriginal,
																							boolean es_solicitud,
																							int codigoMedico,
																							String estado,
																							String codigoRespuestaSolicitud) throws SQLException
	{
	    return SqlBaseDocumentoAdjuntoDao.insertarDocumentoAdjuntoTransaccional(con,
				numeroSolicitud,
				nombreArchivo,
				nombreOriginal,
				es_solicitud,
				codigoMedico,
				estado,
				codigoRespuestaSolicitud); 
	}
	
	/**
	 * @see com.princetonsa.dao.DocumentoAdjuntoDao#insertarDocumentoAdjunto(java.sql.Connection, int, java.lang.String, java.lang.String, boolean, java.lang.String)
	 */
	public ResultadoBoolean insertarDocumentoAdjuntoTransaccionalIM(	Connection con, int numeroSolicitud, String nombreArchivo, String nombreOriginal, String estado) throws SQLException
	{
	    return SqlBaseDocumentoAdjuntoDao.insertarDocumentoAdjuntoTransaccionalIM(con, numeroSolicitud, nombreArchivo, nombreOriginal, estado); 
	}

	/**
	 * @see com.princetonsa.dao.DocumentoAdjuntoDao#eliminarDocumentoAdjunto(java.sql.Connection, int)
	 */
	public ResultadoBoolean eliminarDocumentoAdjunto(	Connection con,
																							int codigoArchivo)
	{
		return SqlBaseDocumentoAdjuntoDao.eliminarDocumentoAdjunto(con, codigoArchivo);
	}
	/**
	 * @see com.princetonsa.dao.DocumentoAdjuntoDao#eliminarDocumentoAdjuntoTransaccional(java.sql.Connection, int, java.lang.String)
	 */
	public ResultadoBoolean eliminarDocumentoAdjuntoTransaccional(	Connection con,
																													int codigoArchivo,
																													String estado)
	{
		return SqlBaseDocumentoAdjuntoDao.eliminarDocumentoAdjuntoTransaccional(con, codigoArchivo, estado);
	}
}
