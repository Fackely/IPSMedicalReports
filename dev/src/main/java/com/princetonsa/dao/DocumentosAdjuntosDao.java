package com.princetonsa.dao;

import java.sql.Connection;

import util.ResultadoCollectionDB;

/**
 * Interfaz para acceder a la fuente de datos del módulo de documentos adjuntos
 *
 * @version 1.0, Febrero 26 / 2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:juandavid@PrincetonSA.com">Juan David Ramirez</a>
 */

public interface DocumentosAdjuntosDao
{
	/**
	 * Cargar documentos adjuntos a la valoración de interconsulta
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public ResultadoCollectionDB cargarDocumentosAdjuntos(Connection con, int numeroSolicitud, boolean esSolicitud, String codigoRespuesta);
}
