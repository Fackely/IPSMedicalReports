package com.servinte.axioma.dao.interfaz.ordenes;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoResultadoConsultaProcesosCierre;
import com.princetonsa.dto.ordenes.DtoOrdenesAmbulatorias;
import com.princetonsa.dto.ordenes.DtoSolicitud;
import com.servinte.axioma.dto.capitacion.DtoAutorizacionCapitacionOrdenAmbulatoria;
import com.servinte.axioma.orm.OrdenesAmbulatorias;


public interface IOrdenesAmbulatoriasDAO {

	
	/**
	 * Retorna las ordenes ambulatorias de Servicios E Insumos y Medicamentos
	 *  
	 * @param parametros
	 * @return ArrayList<DtoOrdenesAmbulatorias> 
	 * @autor Camilo G�mez
	 */	
	public ArrayList<DtoOrdenesAmbulatorias> obtenerOrdenesAmbulatorias(DtoOrdenesAmbulatorias parametros);
	
	/**
	 * Este m�todo se encarga se consultar las �rdenes ambulatorias con estado diferente a anulado 
	 * 
	 * @author Fabi�n Becerra
	 * @param dtoFiltro par�metros de consulta
	 * @return dtoResultadoConsulta campos de las �rdenes ambulatorias y sus servicios o articulos
	 */
	public ArrayList<DtoResultadoConsultaProcesosCierre> consultarOrdenesAmbulatorias(DtoProcesoPresupuestoCapitado dtoFiltro);
	
	
	
	/**
	 * Retorna las ordenes ambulatorias por cuenta o rango.
	 * Hace un filtro por los parametros recibidos de DtoSolicitud.
	 * Si no se le envia cuenta carga todas las ordenes ambulatorias por rango de las fechas
	 * 
	 * @param parametros
	 * @return ArrayList<DtoSolicitud>
	 * @autor Cristhian Murillo.
	 */
	public ArrayList<DtoSolicitud> obtenerOrdenesAmbulatoriasPorCuentaORango(DtoSolicitud parametros,String estadoConsulta);
	
	
	/**
	 * M�todo que se encarga de consultar si la Orden Ambulatoria tiene asociada una autorizaci�n de 
	 * Capitaci�n Subcontratada.
	 * 
	 * @author Camilo G�mez
	 * @param DtoAutorizacionCapitacionOrdenAmbulatoria dto
	 * @return DtoAutorizacionCapitacionOrdenAmbulatoria
	 */
	public ArrayList<DtoAutorizacionCapitacionOrdenAmbulatoria> existeAutorizacionCapitaOrdenAmbulatoria(DtoAutorizacionCapitacionOrdenAmbulatoria dto);
	
	
	/**
	 * M�todo que se encarga de consultar si la Orden Ambulatoria seg�n los parametros enviados
	 * 
	 * @author Cristhian Murillo
	 * @param DtoOrdenesAmbulatorias
	 */
	public ArrayList<OrdenesAmbulatorias> buscarPorParametros(DtoOrdenesAmbulatorias dtoOrdenesAmbulatorias);
	
	
	/**
	 * @param solicitud
	 * @param parametros
	 * @return autorizaciones ambulatorias autorizadas
	 */
	public  ArrayList<DtoSolicitud>  obtenerSolciitudesAutorizadasAmbulatorias(DtoSolicitud solicitud,Integer[] parametros); 
}
