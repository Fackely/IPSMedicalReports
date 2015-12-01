package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;

import util.ResultadoBoolean;

/**
 * Interfaz para el acceder a la fuente de datos de un documento adjunto
 *
 * @version 1.0, Febrero 26 / 2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:juandavid@PrincetonSA.com">Juan David Ramirez</a>
 */
public interface DocumentoAdjuntoDao
{
	/**
	 * Insertar documentos adjuntos a la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @param nombreArchivo
	 * @param es_solicitud
	 * @return boolean true si los datos fueron insertados correctamente
	 */
	public ResultadoBoolean insertarDocumentoAdjunto(Connection con, int numeroSolicitud, String nombreArchivo, String nombreOriginal, boolean es_solicitud, int codigoMedico, String codigoRespuestaSolicitud);
	
	/**
	 * Inserta un documneto adjunto a la solicitud dentro de una transacción
	 * @param con
	 * @param numeroSolicitud
	 * @param nombreArchivo
	 * @param nombreOriginal
	 * @param es_solicitud
	 * @param estado
	 * @return ResultadoBoolean
	 */
	public ResultadoBoolean insertarDocumentoAdjuntoTransaccional(Connection con, int numeroSolicitud, String nombreArchivo, String nombreOriginal, boolean es_solicitud, int codigoMedico,String estado, String codigoRespuestaSolicitud) throws SQLException;
	
	/**
	 * Inserta un documneto adjunto a la solicitud dentro de una transacción
	 * @param con
	 * @param nombreArchivo
	 * @param nombreOriginal
	 * @param estado
	 * @return ResultadoBoolean
	 */
	public ResultadoBoolean insertarDocumentoAdjuntoTransaccionalIM(Connection con, int numeroSolicitud, String nombreArchivo, String nombreOriginal, String estado) throws SQLException;
	
	/**
	 * Dice si existe o no el documento adjunto especificado
	 * @param con
	 * @param codigoArchivo
	 * @return ResultadoBoolean
	 */
	public ResultadoBoolean existeDocumentoadjunto(Connection con, int codigoArchivo);
	
	/**
	 * Elimina el documento adjunto dado el código.
	 * @param con
	 * @param codigoArchivo
	 * @return ResultadoBoolean
	 */
	public ResultadoBoolean eliminarDocumentoAdjunto(Connection con, int codigoArchivo);
	
	/**
	 * Elimina el documento adjunto dentro de una transacción
	 * @param con
	 * @param codigoArchivo
	 * @param estado
	 * @return ResultadoBoolean
	 */
	public ResultadoBoolean eliminarDocumentoAdjuntoTransaccional(Connection con, int codigoArchivo, String estado);
}
