/*
 * Created on 05-abr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Collection;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.ResultadoCollectionDB;
import util.UtilidadBD;

/**
 * @author juanda
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SqlBaseDocumentosAdjuntosDao
{	
	/**
	 * Manejador de logs
	 */
	private static Logger logger = Logger.getLogger(SqlBaseDocumentosAdjuntosDao.class);
	
	/**
	 * Consulta para cargar documentos adjuntos
	 */
	private static final String consultaCargarDocumentoAdjunto="SELECT" +
																					" nombre_archivo AS nombreGenerado," +
																					" nombre_original AS nombreOriginal," +
																					" codigo AS codigoArchivo," +
																					" codigo_medico AS codigoMedico" +
																					" FROM" +
																					" doc_adj_solicitud" +
																					" WHERE" +
																					" numero_solicitud=? AND" +
																					" es_solicitud = ? ";

	public static ResultadoCollectionDB cargarDocumentosAdjuntos(Connection con, int numeroSolicitud, boolean esSolicitud, String codigoRespuesta )
	{
		ResultadoBoolean resp=new ResultadoBoolean(false);
		String consulta= consultaCargarDocumentoAdjunto;
		ResultSetDecorator rs=null;
		try
		{
			if (con == null || con.isClosed()) 
			{
				//Como es transaccional NO voy a tratar de abrir una nueva conexión, sino que mandaré una excepction
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			
			logger.info("codigoRespuesta--->"+codigoRespuesta);
			
			if(!codigoRespuesta.equals(""))
				consulta+=" AND es_codigo_resp_sol =  "+codigoRespuesta;
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
			ps.setBoolean(2, esSolicitud);
			
			logger.info("\n\n\n\n consultaCargarDocumentoAdjunto--->"+consulta+" numeroSolicitud-->"+numeroSolicitud+" esSolictud-->"+esSolicitud+" \n\n\n\n");
			
			rs=new ResultSetDecorator(ps.executeQuery());
			
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de datos: PostrgresValoracionInterconsultasDao");			
			return new ResultadoCollectionDB(false, " Error en la inserción de datos: PostrgresValoracionInterconsultasDao: "+e);
		}
		try
		{
			Collection col=UtilidadBD.resultSet2Collection(rs);
			if( col != null && !col.isEmpty() )
			{
				return new ResultadoCollectionDB(true, "", col);
			}
			else
			{
				return new ResultadoCollectionDB(false);
			}
		}
		catch(Exception e)
		{
			logger.warn(e+" Error en la creacion de la colección: PostrgresValoracionInterconsultasDao");
			resp.setDescripcion(e+" Error en la creacion de la colección: PostrgresValoracionInterconsultasDao");
			return new ResultadoCollectionDB(false, "Error en la creando coleccion Genérica");
		}
	}
}
