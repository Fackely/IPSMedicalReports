
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
	 * Método para actualizar la solicitud de Interconsulta
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud
	 * @param motivoSolicitud
	 * @return
	 * @throws SQLException
	 */
	public int cambiarSolicitudInterconsulta(Connection con, int numeroSolicitud,  String motivoSolicitud, String resumenHistoriaClinica, String comentario/*, String numeroAutorizacion*/)throws SQLException;
	/**
	 * Método para insertar la solicitud
	 * de interconsulta
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud Número de la solicitud a 
		 * cargar
	 * @param codigoServicioSolicitado procedimiento solicitado
	 * @param nombreOtros procedimiento no parametrizado
	 * @param motivoSolicitud motivo
	 * @param resumenHistoriaClinica 
	 * @param comentario
	 * @param codigoManejointerconsulta
	 * @param flagManejoConjuntoFinalizado Flag para decirle
	 * a la aplicación si debe o no mostrar la interconsulta en el
	 * listado de interpretación
	 * @return
	 */
	public int insertarSolicitudInterconsulta(Connection con, int numeroSolicitud, int codigoServicioSolicitado, String nombreOtros, String motivoSolicitud, String resumenHistoriaClinica,String comentario, int codigoManejointerconsulta, boolean flagManejoConjuntoFinalizado);

	/**
	* Insertar documentos adjuntos a la solicitud
	* @param con Conexión con la fuente de datos
	* @param numeroSolicitud Número de la solicitud a 
	 * cargar
	* @param nombreArchivo
	* @param es_solicitud
	* @return boolean true si los datos fueron insertados correctamente
	*/
	public ResultadoBoolean insertarDocumentoAdjunto(Connection con, int numeroSolicitud, String nombreArchivo, String nombreOriginal, boolean es_solicitud);
		
	/**
	 * Método para cargar los documentos adjuntos al resumen
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud Número de la solicitud a 
	 * cargar
	 * @return
	 */
	public ResultadoCollectionDB cargarDocumentosAdjuntos(Connection con, int numeroSolicitud);
	
	/**
	 * Método que permite cargar esta solicitud
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud Número de la solicitud a 
	 * cargar
	 * @return ResultSetDecorator resultado de la consulta
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarSolicitudInterconsulta (Connection con, int numeroSolicitud) throws SQLException;
	
	/**
	 * Método que permite cargar el código del servicio solicitado
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud Número de la solicitud a cargar
	 * @return ResultSetDecorator resultado de la consulta
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarCodigoServicioSolicitado (Connection con, int numeroSolicitud) throws SQLException;
	
	/**
	 * Método para cargar el acronimo días urgente o normal del grupo del servicio solicitado
	 * dependiendo del campo urgente que se selecciono
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoServicio servicio solicitado
	 * @param urgente 
	 * @return diasTramite 
	 */
	public String cargarDiasTramiteServicioSolicitado(Connection con, int codigoServicio, boolean urgente);
	
	/**
	 * Método que permite cargar el motivo de la anulación cuando una solicitud está anulada
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud Número de la solicitud a cargar
	 * @return ResultSetDecorator resultado de la consulta
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarMotivoAnulacionSolicitudInterconsulta (Connection con, int numeroSolicitud) throws SQLException;
	
	/**
	 * Método que permite actualizar el flag de mostrar interconsulta
	 * en interpretación
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud Número de la solicitud de interconsulta
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
