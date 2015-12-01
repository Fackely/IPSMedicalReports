/*
* @(#)PostgresqlDocumentosAdjuntosDao.java
*
* Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
*
* Lenguaje		: Java
* Compilador	: J2SDK 1.4.1_01
*/
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import util.ResultadoCollectionDB;
import com.princetonsa.dao.DocumentosAdjuntosDao;
import com.princetonsa.dao.sqlbase.SqlBaseDocumentosAdjuntosDao;

/**
 * @author lcaballero
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class PostgresqlDocumentosAdjuntosDao implements DocumentosAdjuntosDao
{
	/**
	 * @see com.princetonsa.dao.DocumentosAdjuntosDao#cargarDocumentosAdjuntos(java.sql.Connection, int)
	 */
	public ResultadoCollectionDB cargarDocumentosAdjuntos(	Connection con, int numeroSolicitud, boolean esSolicitud, String codigoRespuesta )
	{
		return SqlBaseDocumentosAdjuntosDao.cargarDocumentosAdjuntos(con, numeroSolicitud, esSolicitud, codigoRespuesta );
	}
}
