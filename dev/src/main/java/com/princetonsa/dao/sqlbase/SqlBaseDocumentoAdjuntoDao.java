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

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadTexto;

/**
 * @author juanda
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SqlBaseDocumentoAdjuntoDao
{
	
	/**
	 * Manejador de logs
	 */
	private static Logger logger = Logger.getLogger(SqlBaseDocumentoAdjuntoDao.class);

	private static String consultaExistenciaDocumentoAdjunto="SELECT" +
																						 " nombre_original AS nombreArchivo" +
																						 " FROM" +
																						 " doc_adj_solicitud" +
																						 " WHERE" +
																						 " codigo=?";

	private static String eliminarDocumentoAdjuntoStr = 	"DELETE FROM doc_adj_solicitud " +
																						"WHERE codigo = ? ";																						 

	/**
	 * Sentencia SQL para la insrción de un documento adjunto
	 */
	private static String consultaInsertarDocumentoAdjunto="INSERT INTO doc_adj_solicitud (codigo,numero_solicitud, nombre_archivo, nombre_original, es_solicitud, codigo_medico, es_codigo_resp_sol) VALUES (?, ? , ? , ? , ?, ?, ?)";
	
	/**
	 * Sentencia SQL para la insrción de un documento adjunto
	 */
	private static String consultaInsertarDocumentoAdjuntoIM="INSERT INTO doc_adj_articulo (codigo, cod_articulo, nombre_archivo, nombre_original) VALUES ( ? , ? , ? , ?)";
	
	/**
	 * @see com.princetonsa.dao.DocumentoAdjuntoDao#existeDocumentoadjunto(java.sql.Connection, int)
	 */	
	public static ResultadoBoolean existeDocumentoadjunto(Connection con, int codigoArchivo)
	{
		try
		{
			PreparedStatementDecorator consulta= new PreparedStatementDecorator(con.prepareStatement(consultaExistenciaDocumentoAdjunto,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consulta.setInt(1, codigoArchivo);
			
			ResultSetDecorator resultadoConsulta=new ResultSetDecorator(consulta.executeQuery());
			if(resultadoConsulta.next())
				return new ResultadoBoolean(true);
			else
				return new ResultadoBoolean(false);
		}
		catch(SQLException e)
		{
			return new ResultadoBoolean(false,"Error verificando existencia de documento adjunto "+e);
		}
	}
	
	/**
	 * @see com.princetonsa.dao.DocumentoAdjuntoDao#eliminarDocumentoAdjunto(java.sql.Connection, int)
	 */
	public static ResultadoBoolean eliminarDocumentoAdjunto(	Connection con,
																							int codigoArchivo)
	{
		try
		{
			PreparedStatementDecorator eliminarDocumentoAdjuntoStatement =  new PreparedStatementDecorator(con.prepareStatement(eliminarDocumentoAdjuntoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			eliminarDocumentoAdjuntoStatement.setInt(1,codigoArchivo);
			
			int resultado = eliminarDocumentoAdjuntoStatement.executeUpdate();
			
			if( resultado < 1 )
				return new ResultadoBoolean(false);
			else
				return new ResultadoBoolean(true);
		}
		catch( SQLException sqe )
		{
			logger.error("No se pudo eliminar el documento adjunto "+sqe.getMessage());
			return new ResultadoBoolean(false, "No se pudo eliminar el documento adjunto : "+codigoArchivo+"  "+sqe.getMessage());
		}
	}

	/**
	 * @see com.princetonsa.dao.DocumentoAdjuntoDao#eliminarDocumentoAdjuntoTransaccional(java.sql.Connection, int, java.lang.String)
	 */
	public static ResultadoBoolean eliminarDocumentoAdjuntoTransaccional(	Connection con,
																													int codigoArchivo,
																													String estado)
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		ResultadoBoolean resp=new ResultadoBoolean(false);
		if( estado != null && estado.equals(ConstantesBD.inicioTransaccion) )
		{
			try
			{
				resp= new ResultadoBoolean(myFactory.beginTransaction(con));
			}
			catch (SQLException e) {
				logger.error("Error iniciando la transacción: "+e);
				return new ResultadoBoolean(false,"Error iniciando la transacción: "+e);
			}		
			if(!resp.isTrue())
			{
				resp.setDescripcion("Error eliminarDocumetnoAdjunto Genérica empezando la transacción");
				return resp;
			}
		}
		try
		{
			PreparedStatementDecorator eliminarDocumentoAdjuntoStatement =  new PreparedStatementDecorator(con.prepareStatement(eliminarDocumentoAdjuntoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			eliminarDocumentoAdjuntoStatement.setInt(1,codigoArchivo);
			
			int resultado = eliminarDocumentoAdjuntoStatement.executeUpdate();
			
			if( resultado < 1 )
				return new ResultadoBoolean(false);
		}
		catch( SQLException sqe )
		{
			logger.error("No se pudo eliminar el documento adjunto "+sqe.getMessage());
			return new ResultadoBoolean(false, "No se pudo eliminar el documento adjunto : "+codigoArchivo+"  "+sqe.getMessage());
		}
		
		if( estado != null && estado.equals(ConstantesBD.finTransaccion) )
		{
			try {
				myFactory.endTransaction(con);
			} catch (SQLException e) {
				logger.error("Error terminando la transacción: "+e);
				return new ResultadoBoolean(false,"Error terminando la transacción: "+e);
			}
		}
		return new ResultadoBoolean(true);
	}
	/**
	 * @see com.princetonsa.dao.DocumentoAdjuntoDao#insertarDocumentoAdjunto(java.sql.Connection, int, java.lang.String, java.lang.String, boolean)
	 */
	public static ResultadoBoolean insertarDocumentoAdjunto(Connection con, int numeroSolicitud, String nombreArchivo, String nombreOriginal, boolean es_solicitud, int codigoMedico, String codigoRespuestaSolicitud)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultaInsertarDocumentoAdjunto,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
			logger.info("-->"+consultaInsertarDocumentoAdjunto);
			int codigo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_doc_adj_solicitud");
			pst.setInt(1,codigo);
			pst.setInt(2,numeroSolicitud);
			pst.setString(3,nombreArchivo);
			pst.setString(4,nombreOriginal);
			pst.setBoolean(5,es_solicitud);
			
			if(codigoMedico<1)
				pst.setObject(6,null);
			else	
				pst.setInt(6,codigoMedico);
			
			if(codigoRespuestaSolicitud.equals(""))
				pst.setObject(7, null);
			else
				pst.setString(7, codigoRespuestaSolicitud);
			
			if(pst.executeUpdate()>0)
				return new ResultadoBoolean(true);
			else
				return new ResultadoBoolean(false,"No se insertó documento Porstgresql ");
		}
		catch(SQLException e)
		{
			logger.error("Problemas insertando el archivo adjunto",e);
			return new ResultadoBoolean(false,"No se insertó documento Porstgresql ");
		}
	}

	/**
	 * @see com.princetonsa.dao.DocumentoAdjuntoDao#insertarDocumentoAdjunto(java.sql.Connection, int, java.lang.String, java.lang.String, boolean, java.lang.String)
	 */
	public static ResultadoBoolean insertarDocumentoAdjuntoTransaccional(	Connection con,
																							int numeroSolicitud,
																							String nombreArchivo,
																							String nombreOriginal,
																							boolean es_solicitud,
																							int codigoMedico,
																							String estado,
																							String codigoRespuestaSolicitud) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		if( estado != null && estado.equals(ConstantesBD.inicioTransaccion) )
		{
		    if (!myFactory.beginTransaction(con))
		    {
		        return new ResultadoBoolean (false, "No se pudo empezar la transacción");
		    }
		}
		try
		{
			if (con == null || con.isClosed()) 
			{
				//Como es transaccional NO se va a tratar de abrir una nueva conexión, sino que mandaré una excepction
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			else
			{			
				PreparedStatementDecorator insertarStatement= new PreparedStatementDecorator(con.prepareStatement(consultaInsertarDocumentoAdjunto,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				int codigo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_doc_adj_solicitud");
				insertarStatement.setInt(1,codigo);
				insertarStatement.setInt(2,numeroSolicitud);
				insertarStatement.setString(3,nombreArchivo);
				insertarStatement.setString(4,nombreOriginal);
				insertarStatement.setBoolean(5,es_solicitud);
				if(codigoMedico<1)
					insertarStatement.setObject(6,null);
				else	
					insertarStatement.setInt(6,codigoMedico);
				
				if(codigoRespuestaSolicitud.equals(""))
					insertarStatement.setObject(7, null);
				else
					insertarStatement.setString(7, codigoRespuestaSolicitud);
				
				int insert=insertarStatement.executeUpdate();
				if( insert == 0 )
				{
				    myFactory.abortTransaction(con);
				    return new ResultadoBoolean (false, "Transacción abortada");
				}
			}
		}
		catch (SQLException e)
		{
			logger.error("Error en la insercion del documento adjunto Genérica: "+e);
		    myFactory.abortTransaction(con);
			return new ResultadoBoolean(false,"Error en la insercion dedocumento adjunto Genérica: "+e);
		}
		
		if( estado != null && estado.equals(ConstantesBD.finTransaccion) )
		{
		    myFactory.endTransaction(con);
		}
		return new ResultadoBoolean(true);
	}
	
	/**
	 * @see com.princetonsa.dao.DocumentoAdjuntoDao#insertarDocumentoAdjunto(java.sql.Connection, int, java.lang.String, java.lang.String, boolean, java.lang.String)
	 */
	public static ResultadoBoolean insertarDocumentoAdjuntoTransaccionalIM(	Connection con, int numeroSolicitud, String nombreArchivo, String nombreOriginal, String estado) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		if( estado != null && estado.equals(ConstantesBD.inicioTransaccion) )
		{
		    if (!myFactory.beginTransaction(con))
		    {
		        return new ResultadoBoolean (false, "No se pudo empezar la transacción");
		    }
		}
		try
		{
			if (con == null || con.isClosed()) 
			{
				//Como es transaccional NO se va a tratar de abrir una nueva conexión, sino que mandaré una excepction
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			else
			{			
				PreparedStatementDecorator insertarStatement= new PreparedStatementDecorator(con.prepareStatement(consultaInsertarDocumentoAdjuntoIM,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				int codigo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_doc_adj_articulo");
				insertarStatement.setInt(1, codigo);
				insertarStatement.setString(2,String.valueOf(numeroSolicitud));
				insertarStatement.setString(3,nombreArchivo);
				insertarStatement.setString(4,nombreOriginal);
				
				int insert=insertarStatement.executeUpdate();
				if( insert == 0 )
				{
				    myFactory.abortTransaction(con);
				    return new ResultadoBoolean (false, "Transacción abortada");
				}
			}
		}
		catch (SQLException e)
		{
			logger.error("Error en la insercion del documento adjunto Genérica: "+e);
		    myFactory.abortTransaction(con);
			return new ResultadoBoolean(false,"Error en la insercion dedocumento adjunto Genérica: "+e);
		}
		
		if( estado != null && estado.equals(ConstantesBD.finTransaccion) )
		{
		    myFactory.endTransaction(con);
		}
		return new ResultadoBoolean(true);
	}
	
}
