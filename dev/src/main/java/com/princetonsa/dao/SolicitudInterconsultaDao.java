
package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;

import util.ResultadoBoolean;
import util.ResultadoCollectionDB;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoServicios;

/**
 * * Clase para el manejo de una solicitud de interconsulta
 * @version 1.0, Feb 10, 2004
 */

public interface SolicitudInterconsultaDao 
{
	/**
	 * M�todo para actualizar la solicitud de Interconsulta
	 * @param con Conexi�n con la fuente de datos
	 * @param numeroSolicitud
	 * @param motivoSolicitud
	 * @return
	 * @throws SQLException
	 */
	public int cambiarSolicitudInterconsulta(Connection con, int numeroSolicitud,  String motivoSolicitud, String resumenHistoriaClinica, String comentario/*, String numeroAutorizacion*/)throws SQLException;
	/**
	 * M�todo para insertar la solicitud
	 * de interconsulta
	 * @param con Conexi�n con la fuente de datos
	 * @param numeroSolicitud N�mero de la solicitud a 
		 * cargar
	 * @param codigoServicioSolicitado procedimiento solicitado
	 * @param nombreOtros procedimiento no parametrizado
	 * @param motivoSolicitud motivo
	 * @param resumenHistoriaClinica 
	 * @param comentario
	 * @param codigoManejointerconsulta
	 * @param flagManejoConjuntoFinalizado Flag para decirle
	 * a la aplicaci�n si debe o no mostrar la interconsulta en el
	 * listado de interpretaci�n
	 * @return
	 */
	public int insertarSolicitudInterconsulta(Connection con, int numeroSolicitud, int codigoServicioSolicitado, String nombreOtros, String motivoSolicitud, String resumenHistoriaClinica,String comentario, int codigoManejointerconsulta, boolean flagManejoConjuntoFinalizado);

	/**
	* Insertar documentos adjuntos a la solicitud
	* @param con Conexi�n con la fuente de datos
	* @param numeroSolicitud N�mero de la solicitud a 
	 * cargar
	* @param nombreArchivo
	* @param es_solicitud
	* @return boolean true si los datos fueron insertados correctamente
	*/
	public ResultadoBoolean insertarDocumentoAdjunto(Connection con, int numeroSolicitud, String nombreArchivo, String nombreOriginal, boolean es_solicitud);
		
	/**
	 * M�todo para cargar los documentos adjuntos al resumen
	 * @param con Conexi�n con la fuente de datos
	 * @param numeroSolicitud N�mero de la solicitud a 
	 * cargar
	 * @return
	 */
	public ResultadoCollectionDB cargarDocumentosAdjuntos(Connection con, int numeroSolicitud);
	
	/**
	 * M�todo que permite cargar esta solicitud
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param numeroSolicitud N�mero de la solicitud a 
	 * cargar
	 * @return ResultSetDecorator resultado de la consulta
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarSolicitudInterconsulta (Connection con, int numeroSolicitud) throws SQLException;
	
	/**
	 * M�todo que permite cargar el c�digo del servicio solicitado
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param numeroSolicitud N�mero de la solicitud a cargar
	 * @return ResultSetDecorator resultado de la consulta
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarCodigoServicioSolicitado (Connection con, int numeroSolicitud) throws SQLException;
	
	/**
	 * M�todo para cargar el acronimo d�as urgente o normal del grupo del servicio solicitado
	 * dependiendo del campo urgente que se selecciono
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param codigoServicio servicio solicitado
	 * @param urgente 
	 * @return diasTramite 
	 */
	public String cargarDiasTramiteServicioSolicitado(Connection con, int codigoServicio, boolean urgente);
	
	/**
	 * M�todo que permite cargar el motivo de la anulaci�n cuando una solicitud est� anulada
	 * @param con Conexi�n con la fuente de datos
	 * @param numeroSolicitud N�mero de la solicitud a cargar
	 * @return ResultSetDecorator resultado de la consulta
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarMotivoAnulacionSolicitudInterconsulta (Connection con, int numeroSolicitud) throws SQLException;
	
	/**
	 * M�todo que permite actualizar el flag de mostrar interconsulta
	 * en interpretaci�n
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param numeroSolicitud N�mero de la solicitud de interconsulta
	 * a actualizar
	 * @param nuevoEstadoFlag El nuevo valor del flag 
	 * @return
	 * @throws SQLException
	 */
	public int actualizarFlagMostrarInterconsulta (Connection con, int numeroSolicitud, boolean nuevoEstadoFlag) throws SQLException;
	
	/**
	 * M&eacute;todo encargado de consultar el c&oacute;digo del servicio
	 * asociado a una solicitud de interconsulta 
	 * @param Connection con, 
    		String numeroSolicitud, int codigoTarifario
	 * @return DtoServicios
	 * @author Diana Carolina G
	 */
	public DtoServicios buscarServiciosSolicitudInterconsulta(Connection con, 
    		int numeroSolicitud, int codigoTarifario);
}
