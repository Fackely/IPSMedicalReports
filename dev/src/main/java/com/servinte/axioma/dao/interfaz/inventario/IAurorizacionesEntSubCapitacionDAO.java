package com.servinte.axioma.dao.interfaz.inventario;

import java.util.ArrayList;

import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;
import com.servinte.axioma.orm.AutorizacionesEntSubArticu;
import com.servinte.axioma.orm.AutorizacionesEntSubServi;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;


/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de IAurorizacionesEntSubCapitacionDAO
 * 
 * @author Cristhian Murillo
 */
public interface IAurorizacionesEntSubCapitacionDAO 
{
	
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
	 * Metodo de la super clase finById
	 * @param id
	 * @return AutorizacionesEntSubArticu
	 */
	public AutorizacionesEntSubArticu obtenerAutorizacionesEntSubArticuPorId(long id);
	
	
	/**
	 * Implementacion del método findById
	 * @param id
	 * @return AutorizacionesEntidadesSub
	 */
	public AutorizacionesEntidadesSub obtenerAutorizacionesEntidadesSubPorId(long id);
	
	

	/**
	 * Metodo de la super clase attachDirty
	 * @param instance
	 */
	public void attachDirtyAutorizacionesEntSubArticu(AutorizacionesEntSubArticu instance);
	
	
	
	/**
	 * Retorna las Autorizaciones de la EntidadesSubcontratada según en número de solicitud enviada.
	 * @param DtoAutorizacionEntSubcontratadasCapitacion
	 * @return ArrayList<DtoEntregaMedicamentosInsumosEntSubcontratadas>
	 * 
	 * @author Cristhian Murillo
	 */
	public ArrayList<DtoAutorizacionEntSubcontratadasCapitacion> obtenerAutorizacionesPorEntSubPorNumeroSolicitud(
			DtoAutorizacionEntSubcontratadasCapitacion dtoEntregaMedicamentosInsumosEntSubcontratadas);
}
