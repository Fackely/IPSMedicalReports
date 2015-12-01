package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.sql.SQLException;

import util.ResultadoBoolean;
import util.ResultadoCollectionDB;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.SolicitudInterconsultaDao;
import com.princetonsa.dao.sqlbase.SqlBaseSolicitudInterconsultaDao;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoServicios;

/**
 * Implementaci�n de los m�todos para acceder a la fuente de datos, la parte de
 * Solicitud Interconsulta para Postgres.
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson R�os</a>,
 * @version 1.0, Feb 10, 2004
 */

public class OracleSolicitudInterconsultaDao implements SolicitudInterconsultaDao 
{
	
	/**
	 * M�todo que  carga  una solicitud 
	 * en una BD Oracle de Interconsulta
	 */
	public ResultSetDecorator cargarSolicitudInterconsulta (Connection con, int numeroSolicitud) throws SQLException
	{
		return SqlBaseSolicitudInterconsultaDao.cargarSolicitudInterconsulta(con,numeroSolicitud);
	}
	
	/**
	 * M�todo que carga el motivo de anulaci�n de una solicitud
	 * en una BD Oracle de Interconsulta
	 */
	public ResultSetDecorator cargarMotivoAnulacionSolicitudInterconsulta (Connection con, int numeroSolicitud) throws SQLException
	{
		return SqlBaseSolicitudInterconsultaDao.cargarMotivoAnulacionSolicitudInterconsulta(con,numeroSolicitud);
	}
	
	
	/**
	 * M�todo que  carga  el c�digo del servicio solicitado 
	 * en una BD Oracle de Interconsulta
	 *
	*/
	public ResultSetDecorator cargarCodigoServicioSolicitado (Connection con, int numeroSolicitud) throws SQLException
	{
		return SqlBaseSolicitudInterconsultaDao.cargarCodigoServicioSolicitado(con,numeroSolicitud);
	}
	
	/**
	 * M�todo para cargar el acronimo d�as urgente o normal del grupo del servicio solicitado
	 * dependiendo del campo urgente que se selecciono
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param codigoServicio servicio solicitado
	 * @param urgente
	 * @return diasTramite
	 * 
	 */
	public String cargarDiasTramiteServicioSolicitado(Connection con, int codigoServicio, boolean urgente)
	{
		return SqlBaseSolicitudInterconsultaDao.cargarDiasTramiteServicioSolicitado(con,codigoServicio,urgente);
	}
	
	/**
	 * Constructor
	 */
	public OracleSolicitudInterconsultaDao(){}
	
	/**
	 * M�todo para Actualizar  la solicitud  de interconsulta 
	*/
	public int cambiarSolicitudInterconsulta(Connection con, int numeroSolicitud,  String motivoSolicitud, String resumenHistoriaClinica, String comentario/*, String numeroAutorizacion*/)throws SQLException
	{
		return SqlBaseSolicitudInterconsultaDao.cambiarSolicitudInterconsulta(con,
		        numeroSolicitud,
		        motivoSolicitud,
		        resumenHistoriaClinica,
		        comentario,
		        /*numeroAutorizacion,*/
		        DaoFactory.getDaoFactory(System.getProperty("TIPOBD"))		        
				);
	}
	
	/**
	 * M�todo para insertar una nueva solicitud de interconsulta
	 */
	public int insertarSolicitudInterconsulta(Connection con, int numeroSolicitud, int codigoServicioSolicitado, String nombreOtros, String motivoSolicitud, String resumenHistoriaClinica,String comentario, int codigoManejointerconsulta, boolean flagManejoConjuntoFinalizado)
	{
		return SqlBaseSolicitudInterconsultaDao.insertarSolicitudInterconsulta(
		        con,
		        numeroSolicitud,
		        codigoServicioSolicitado,
		        nombreOtros,
		        motivoSolicitud,
		        resumenHistoriaClinica,
		        comentario,
		        codigoManejointerconsulta, 
		        flagManejoConjuntoFinalizado, 
		        DaoFactory.getDaoFactory(System.getProperty("TIPOBD"))
		        );
	}
	
	/**
	* Insertar documentos adjuntos a la solicitud
	* @param con Conexi�n
	* @param numeroSolicitud numero de la solicitud
	* @param nombreArchivo nombre del archivo
	* @param es_solicitud
	* @return boolean true si los datos fueron insertados correctamente
	*/
	public ResultadoBoolean insertarDocumentoAdjunto(Connection con, int numeroSolicitud, String nombreArchivo, String nombreOriginal, boolean es_solicitud)
	{
		return SqlBaseSolicitudInterconsultaDao.insertarDocumentoAdjunto( con,  numeroSolicitud,  nombreArchivo,  nombreOriginal,  es_solicitud);
	}

	/**
	 * M�todo encargado de cargar los docs que 
	 * se soliciten en la interconsulta
	 * @param con Conexi�n con la fuente de datos
	 * @param numeroSolicitud numero de la solicitud
	 * @return  ResultadoCollectionDB
	 */
	public ResultadoCollectionDB cargarDocumentosAdjuntos(Connection con, int numeroSolicitud)
	{
		return SqlBaseSolicitudInterconsultaDao.cargarDocumentosAdjuntos(con,numeroSolicitud);
	}

	/**
	 * Implementaci�n del m�todo que actualiza el Flag para manejo de
	 * Interconsulta en una BD Oracle
	 *
	 * @see com.princetonsa.dao.SolicitudInterconsultaDao#actualizarFlagMostrarInterconsulta (Connection , int , boolean ) throws SQLException
	 */
	public int actualizarFlagMostrarInterconsulta (Connection con, int numeroSolicitud, boolean nuevoEstadoFlag) throws SQLException
	{
		return SqlBaseSolicitudInterconsultaDao.actualizarFlagMostrarInterconsulta (con, numeroSolicitud, nuevoEstadoFlag) ;
	}
	
	/**
	 * M&eacute;todo encargado de consultar el c&oacute;digo del servicio
	 * asociado a una solicitud de interconsulta 
	 * @param Connection con, 
    		String numeroSolicitud, int codigoTarifario
	 * @return DtoServicios
	 * @author Diana Carolina G
	 */
	public DtoServicios buscarServiciosSolicitudInterconsulta(Connection con, 
    		int numeroSolicitud, int codigoTarifario){
		return SqlBaseSolicitudInterconsultaDao.buscarServiciosSolicitudInterconsulta(con, numeroSolicitud, codigoTarifario);
	}

}