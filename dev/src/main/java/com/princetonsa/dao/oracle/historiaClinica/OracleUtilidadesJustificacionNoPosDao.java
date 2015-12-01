package com.princetonsa.dao.oracle.historiaClinica;

import java.sql.Connection;

import com.princetonsa.dao.historiaClinica.UtilidadesJustificacionNoPosDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseUtilidadesJustificacionNoPosDao;
import com.princetonsa.dto.historiaClinica.DtoJustificacionNoPos;
import com.princetonsa.dto.historiaClinica.DtoParamJusNoPos;

/**
 * 
 * @author axioma
 *
 */
public class OracleUtilidadesJustificacionNoPosDao implements UtilidadesJustificacionNoPosDao 
{
	/**
	 * Método para cargar la parametrización del formato de Justificación No Pos
	 * @param con
	 * @param institucion
	 * @param tipoJustificacion
	 * @return
	 */
	public DtoParamJusNoPos cargarParametrizacion(Connection con, int institucion, String tipoJustificacion)
	{
		return SqlBaseUtilidadesJustificacionNoPosDao.cargarParametrizacion(con, institucion, tipoJustificacion);
	}
	
	/**
	 * Método para guardar la justificacion No Pos en la BD 
	 * @param con
	 * @param dtoJus
	 * @param tipoJustificacion
	 * @param usuario
	 * @param esModificacion
	 * @return
	 */
	public boolean guardarJustificacion(Connection con, DtoJustificacionNoPos dtoJus, String tipoJustificacion)
	{
		return SqlBaseUtilidadesJustificacionNoPosDao.guardarJustificacion(con, dtoJus, tipoJustificacion);
	}
	
	/**
	 * Metodo que inserta el asocio de la justificacion solicitudes
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoAsocioJustificacion
	 * @param cantidadSolicitada 
	 
	 */
	
	public void insertarAsocioSolicitudJustificacion(Connection con, int numeroSolicitud, int codigoAsocioJustificacion, int cantidadSolicitada)
	{
		
		SqlBaseUtilidadesJustificacionNoPosDao.insertarAsocioSolicitudJustificacion(con, numeroSolicitud, codigoAsocioJustificacion, cantidadSolicitada);
	}

	/**
	 * Metodo que inserta el asocio de la justificacion ordenes
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoAsocioJustificacion
	 * @param cantidadSolicitada 
	 
	 */
	
	public void insertarAsocioOrdenJustificacion(Connection con, int numeroOrden, int codigoAsocioJustificacion, int cantidadSolicitada)
	{
		
		SqlBaseUtilidadesJustificacionNoPosDao.insertarAsocioOrdenJustificacion(con, numeroOrden, codigoAsocioJustificacion, cantidadSolicitada);
	}
	
	
	/**
	 * Método que consulta si existe o no una justificación No Pos para un articulo/solicitud
	 * @param con
	 * @param institucion
	 * @param tipoJustificacion
	 * @return
	 */
	public boolean existeJustificacion(Connection con, int institucion, String tipoJustificacion, String codigo, String solicitud)
	{
		return SqlBaseUtilidadesJustificacionNoPosDao.existeJustificacion(con, institucion, tipoJustificacion, codigo, solicitud);
	}
	
	/**
	 * Método que consulta si existe o no una justificación No Pos para un articulo/solicitud general
	 * @param con
	 * @param institucion
	 * @param codigoArticulo
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean existeJustificacion(Connection con, int institucion, String codigo, String solicitud)
	{
		return SqlBaseUtilidadesJustificacionNoPosDao.existeJustificacion(con, institucion, codigo, solicitud);
	}
	
	/**
	 * Metodo que consulta el codigo de una justificacion no pos si existe o no para un articulo/orden-solicitud
	 * @param con
	 * @param institucion
	 * @param tipoJustificacion
	 * @return
	 */
	public int consultarCodigoJustificacion(Connection con, int institucion, String tipoJustificacion, String codigoArt,String codigoOrden, String numeroSolicitud){
		return SqlBaseUtilidadesJustificacionNoPosDao.consultarCodigoJustificacion(con, institucion, tipoJustificacion, codigoArt, codigoOrden, numeroSolicitud);
	}
	
	/**
	 * Método que consulta una justificación No Pos
	 * @param con
	 * @param tipoJustificacion
	 * @param dtoParam
	 * @return
	 */
	public DtoJustificacionNoPos consultarJustificacion(Connection con, String tipoJustificacion, DtoParamJusNoPos dtoParam)
	{
		return SqlBaseUtilidadesJustificacionNoPosDao.consultarJustificacion(con, tipoJustificacion, dtoParam);
	}
	
	/**
	 * Método para actualizar una justificación No Pos
	 * @param con
	 * @param dtoJus
	 * @param tipoJustificacion
	 * @return
	 */
	public boolean actualizarJustificacion(Connection con, DtoJustificacionNoPos dtoJus, String tipoJustificacion)
	{
		return SqlBaseUtilidadesJustificacionNoPosDao.actualizarJustificacion(con, dtoJus, tipoJustificacion);
	}

	/**
	 * Método que consulta si existe o no una justificación No Pos para un Insumo/Orden Ambulatoria
	 * @param con
	 * @param institucion
	 * @param tipoJustificacion
	 * @param codigoArticulo
	 * @param codigoOrdenAmbulatoria
	 * @return
	 */
	@Override
	public boolean existeJustificacionOrdenAmbulatoria(Connection con,
			int institucion, String tipoJustificacion, String codigoArticulo,
			String codigoOrdenAmbulatoria) {
		return SqlBaseUtilidadesJustificacionNoPosDao.existeJustificacionOrdenAmbulatoria(con, institucion, tipoJustificacion, codigoArticulo, codigoOrdenAmbulatoria);

	}
}
