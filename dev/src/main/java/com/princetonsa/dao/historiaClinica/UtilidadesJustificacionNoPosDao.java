package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;

import com.princetonsa.dto.historiaClinica.DtoJustificacionNoPos;
import com.princetonsa.dto.historiaClinica.DtoParamJusNoPos;

/**
 * 
 * @author axioma
 *
 */
public interface UtilidadesJustificacionNoPosDao 
{
	/**
	 * M�todo para cargar la parametrizaci�n del formato de Justificaci�n No Pos
	 * @param con
	 * @param institucion
	 * @param tipoJustificacion
	 * @return
	 */
	public DtoParamJusNoPos cargarParametrizacion(Connection con, int institucion, String tipoJustificacion);

	/**
	 * M�todo para guardar la justificacion No Pos en la BD 
	 * @param con
	 * @param dtoJus
	 * @param tipoJustificacion
	 * @param usuario
	 * @param esModificacion
	 * @return
	 */
	public boolean guardarJustificacion(Connection con,DtoJustificacionNoPos dtoJus, String tipoJustificacion);

	/**
	 * Metodo que registra la asociacion justificaci�n  y solicitud de medicamentos Nopos
	 * 
	 * @param con
	 * @param cantidadSolicitada 
	 * @param institucion
	 * @param tipoJustificacion
	 * @return
	 */
	public void insertarAsocioSolicitudJustificacion(Connection con, int nuemorSolicitud, int codigoAsocioJustificacion, int cantidadSolicitada);
	
	/**
	 * Metodo que registra la asociacion justificaci�n  y Ordenes de medicamentos Nopos
	 * 
	 * @param con
	 * @param institucion
	 * @param tipoJustificacion
	 * @param cantidadSolicitada
	 * @return
	 */
	public void insertarAsocioOrdenJustificacion(Connection con, int nuemorOrden, int codigoAsocioJustificacion,int cantidadSolicitada);
	
	
	/**
	 * M�todo que consulta si existe o no una justificaci�n No Pos para un articulo/solicitud general
	 * @param con
	 * @param institucion
	 * @param codigoArticulo
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean existeJustificacion(Connection con, int institucion, String codigo, String solicitud);
	
	
	/**
	 * M�todo que consulta si existe o no una justificaci�n No Pos para un articulo/solicitud
	 * @param con
	 * @param institucion
	 * @param tipoJustificacion
	 * @return
	 */
	public boolean existeJustificacion(Connection con, int institucion, String tipoJustificacion, String codigo, String solicitud);

	/**
	 * Metodo que consulta el codigo de una justificacion no pos si existe o no para un articulo/orden-solicitud
	 * @param con
	 * @param institucion
	 * @param tipoJustificacion
	 * @return
	 */
	public int consultarCodigoJustificacion(Connection con, int institucion, String tipoJustificacion, String codigoArt,String codigoOrden, String numeroSolicitud);
	/**
	 * M�todo que consulta una justificaci�n No Pos
	 * @param con
	 * @param tipoJustificacion
	 * @param dtoParam
	 * @return
	 */
	public DtoJustificacionNoPos consultarJustificacion(Connection con, String tipoJustificacion, DtoParamJusNoPos dtoParam);

	/**
	 * M�todo para actualizar una justificaci�n No Pos
	 * @param con
	 * @param dtoJus
	 * @param tipoJustificacion
	 * @return
	 */
	public boolean actualizarJustificacion(Connection con, DtoJustificacionNoPos dtoJus, String tipoJustificacion);
	
	/**
	 * M�todo que consulta si existe o no una justificaci�n No Pos para un Insumo/ orden ambulatoria
	 * @param con
	 * @param institucion
	 * @param tipoJustificacion
	 * @param codigoArticulo
	 * @param codigoOrdenAmbulatoria
	 * @return
	 */
	public boolean existeJustificacionOrdenAmbulatoria(Connection con, int institucion, String tipoJustificacion, String codigoArticulo, String codigoOrdenAmbulatoria);

}