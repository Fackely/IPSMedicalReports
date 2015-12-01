package com.servinte.axioma.dao.interfaz.salasCirugia;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoResultadoConsultaProcesosCierre;
import com.princetonsa.dto.ordenes.DtoSolicitud;
import com.servinte.axioma.dto.capitacion.DtoAutorizacionCapitacionPeticion;

public interface IPeticionQxDAO {
	
	/**
	 * Este método consulta las peticiones dependiendo de los parámetros enviados
	 * @param dtoFiltro
	 * @return ArrayList<DtoSolicitud>
	 *
	 * @autor Cristhian Murillo
	*/
	public ArrayList<DtoSolicitud> obtenerOrdenesPeticionesPorCuentaORango(DtoSolicitud parametros);

	/**
	 * Método que se encarga de consultar si la Peticion tiene asociada una autorización de 
	 * Capitación Subcontratada.
	 * RQF 02-0025 Autorizaciones Capitación
	 * 
	 * @author Camilo Gómez
	 * @param DtoAutorizacionCapitacionPeticion dto
	 * @return ArrayList<DtoAutorizacionCapitacionPeticion> dtoPeticiones
	 */
	public ArrayList<DtoAutorizacionCapitacionPeticion> existeAutorizacionCapitaPeticion(DtoAutorizacionCapitacionPeticion dto);
	
	/**
	 * Este método consulta las peticiones de servicios dependiendo de los parámetros enviados
	 * @param dtoFiltro
	 * @return ArrayList<DtoResultadoConsultaProcesosCierre>
	 *
	 * @autor Fabián Becerra
	*/
	public ArrayList<DtoResultadoConsultaProcesosCierre> consultarPeticionesServicios(DtoProcesoPresupuestoCapitado dtoFiltro);
	
	/**
	 * Este método consulta las peticiones de Articulos dependiendo de los parámetros enviados
	 * @param dtoFiltro
	 * @return ArrayList<DtoResultadoConsultaProcesosCierre>
	 *
	 * @autor Fabián Becerra
	*/
	public ArrayList<DtoResultadoConsultaProcesosCierre> consultarPeticionesArticulos(DtoProcesoPresupuestoCapitado dtoFiltro);
	
	/**
	 * Metodo que devuelve las peticiones de articulos anuladas desde la orden medica (SolicitudCx)
	 * @param dtoFiltro
	 * @return ArrayList<DtoResultadoConsultaProcesosCierre>
	 * @author hermorhu
	 */
	public ArrayList<DtoResultadoConsultaProcesosCierre> consultarPeticionesArticulosAnulados (DtoProcesoPresupuestoCapitado dtoFiltro);
}
