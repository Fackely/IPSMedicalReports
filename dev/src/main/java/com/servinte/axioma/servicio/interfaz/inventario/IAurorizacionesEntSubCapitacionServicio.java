package com.servinte.axioma.servicio.interfaz.inventario;

import java.util.ArrayList;

import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;
import com.princetonsa.dto.ordenes.DtoSolicitud;
import com.servinte.axioma.orm.AutorizacionesEntSubServi;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;
import com.servinte.axioma.orm.SolicitudesPosponer;

/**
 * @author Cristhian Murillo
 */
public interface IAurorizacionesEntSubCapitacionServicio 
{
	
	/**
	 * Retorna las Autorizaciones de la EntidadesSubcontratada enviada.
	 * Filtrando por su estado
	 * @param DtoAutorizacionEntSubcontratadasCapitacion
	 * @return ArrayList<DtoEntregaMedicamentosInsumosEntSubcontratadas>
	 * 
	 * @author Cristhian Murillo
	 */
	public ArrayList<DtoAutorizacionEntSubcontratadasCapitacion> obtenerAutorizacionesPorEntSub(DtoAutorizacionEntSubcontratadasCapitacion dtoEntregaMedicamentosInsumosEntSubcontratadas);
	
	
	/**
	 * Lista por Autorizacionentidad subcontratada.
	 * @author Cristhian Murillo
	 * @param dtoParametros
	 * @return ArrayList<AutorizacionesEntSubServi>
	 */
	public ArrayList<AutorizacionesEntSubServi> listarAutorizacionesEntSubServiPorAutoEntSub(DtoAutorizacionEntSubcontratadasCapitacion dtoParametros);

	
	/**
	 * Lista por Autorizacionentidad subcontratada.
	 * @author Cristhian Murillo
	 * @param dtoParametros
	 * @return ArrayList<DtoArticulosAutorizaciones>
	 */
	public ArrayList<DtoArticulosAutorizaciones> listarautorizacionesEntSubArticuPorAutoEntSub(DtoAutorizacionEntSubcontratadasCapitacion dtoParametros);

	
	/**
	 * Realiza un recorrido en la lista de articulos y determina si existen Articulos tipo Medicamentos y articulos tipo Insumos
	 * @author Cristhian Murillo
	 * @param dtoAutorizacionEntSubcontratadasCapitacion
	 */
	public void validarListaArticulos(DtoAutorizacionEntSubcontratadasCapitacion dtoAutorizacionEntSubcontratadasCapitacion);
	
	
	/**
	 * Implementacion del método findById
	 * @param id
	 * @return AutorizacionesEntidadesSub
	 */
	public AutorizacionesEntidadesSub obtenerAutorizacionesEntidadesSubPorId(long id);
	
	
	/**
	 * Retorna las solicitudes por cuenta.
	 * Hace un filtro por los parametros recibidos de DtoSolicitud.
	 * Si la Solicitud No tenga asociadas Autorizaciones Capitación Subcontrada o si tienen  se encuentre en estado Anulada.
	 * Si no se le envia cuenta carga todas als solicitudes por rango de las fechas
	 * 
	 * @param parametros
	 * @return ArrayList<DtoSolicitud>
	 * @autor Cristhian Murillo.
	 */
	public ArrayList<DtoSolicitud> obtenerSolicitudesPorCuentaORango(DtoSolicitud parametros,String codigoEstado);
	
	
	/**
	 * Guarta la instancia de la entidad en la base de datos
	 * @param transientInstance
	 */
	public void guardarSolicitudesPosponer(SolicitudesPosponer transientInstance);
	
	
	
	/**
	 * Retorna las solicitudes por el número de esta.
	 * 
	 * @param parametros
	 * @return ArrayList<DtoSolicitud>
	 * @autor Cristhian Murillo.
	 */
	public ArrayList<DtoSolicitud> obtenerSolicitudesSubcuenta(DtoSolicitud parametros);
	
	
	
	/**
	 * Retorna las Autorizaciones de la EntidadesSubcontratada según en número de solicitud enviada.
	 * @param DtoAutorizacionEntSubcontratadasCapitacion
	 * @return ArrayList<DtoEntregaMedicamentosInsumosEntSubcontratadas>
	 * 
	 * @author Cristhian Murillo
	 */
	public ArrayList<DtoAutorizacionEntSubcontratadasCapitacion> obtenerAutorizacionesPorEntSubPorNumeroSolicitud(
			DtoAutorizacionEntSubcontratadasCapitacion dtoEntregaMedicamentosInsumosEntSubcontratadas);
	
	/**
	 * Obtiene articulos por autorizar 
	 * @param parametros
	 * @param codigoEstado
	 */
	public ArrayList<DtoSolicitud>  obtenerArticulosSinAutorizar(DtoSolicitud parametros);
}
