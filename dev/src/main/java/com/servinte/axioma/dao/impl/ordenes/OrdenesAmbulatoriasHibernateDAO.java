package com.servinte.axioma.dao.impl.ordenes;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoResultadoConsultaProcesosCierre;
import com.princetonsa.dto.ordenes.DtoOrdenesAmbulatorias;
import com.princetonsa.dto.ordenes.DtoSolicitud;
import com.servinte.axioma.dao.interfaz.ordenes.IOrdenesAmbulatoriasDAO;
import com.servinte.axioma.dto.capitacion.DtoAutorizacionCapitacionOrdenAmbulatoria;
import com.servinte.axioma.orm.OrdenesAmbulatorias;
import com.servinte.axioma.orm.delegate.ordenes.OrdenesAmbulatoriasDelegate;

public class OrdenesAmbulatoriasHibernateDAO implements IOrdenesAmbulatoriasDAO
{

	private OrdenesAmbulatoriasDelegate ordenesAmbulatoriasDelegate;
	

	/**
	 * Constructor
	 */
	public OrdenesAmbulatoriasHibernateDAO (){
		ordenesAmbulatoriasDelegate = new OrdenesAmbulatoriasDelegate();
	}

	
	
	/**
	 * Retorna las ordenes ambulatorias de Servicios E Insumos y Medicamentos
	 *  
	 * @param parametros
	 * @return ArrayList<DtoOrdenesAmbulatorias> 
	 * @autor Camilo G�mez
	 */	
	public ArrayList<DtoOrdenesAmbulatorias> obtenerOrdenesAmbulatorias(DtoOrdenesAmbulatorias parametros){
		return ordenesAmbulatoriasDelegate.obtenerOrdenesAmbulatorias(parametros); 
	}
	
	/**
	 * Este m�todo se encarga se consultar las �rdenes ambulatorias con estado diferente a anulado 
	 * 
	 * @author Fabi�n Becerra
	 * @param dtoFiltro par�metros de consulta
	 * @return dtoResultadoConsulta campos de las �rdenes ambulatorias y sus servicios o articulos
	 */
	public ArrayList<DtoResultadoConsultaProcesosCierre> consultarOrdenesAmbulatorias(DtoProcesoPresupuestoCapitado dtoFiltro){
		return ordenesAmbulatoriasDelegate.consultarOrdenesAmbulatorias(dtoFiltro);
	}


	@Override
	public ArrayList<DtoSolicitud> obtenerOrdenesAmbulatoriasPorCuentaORango(DtoSolicitud parametros,String estadoConsulta) {
		return ordenesAmbulatoriasDelegate.obtenerOrdenesAmbulatoriasPorCuentaORango(parametros,estadoConsulta);
	}
	
	/**
	 * M�todo que se encarga de consultar si la Orden Ambulatoria tiene asociada una autorizaci�n de 
	 * Capitaci�n Subcontratada.
	 * 
	 * @author Camilo G�mez
	 * @param DtoAutorizacionCapitacionOrdenAmbulatoria dto
	 * @return DtoAutorizacionCapitacionOrdenAmbulatoria
	 */
	public ArrayList<DtoAutorizacionCapitacionOrdenAmbulatoria> existeAutorizacionCapitaOrdenAmbulatoria(DtoAutorizacionCapitacionOrdenAmbulatoria dto){
		return ordenesAmbulatoriasDelegate.existeAutorizacionCapitaOrdenAmbulatoria(dto);
	}


	/**
	 * M�todo que se encarga de consultar si la Orden Ambulatoria seg�n los parametros enviados
	 * 
	 * @author Cristhian Murillo
	 * @param DtoOrdenesAmbulatorias
	 */
	public ArrayList<OrdenesAmbulatorias> buscarPorParametros(DtoOrdenesAmbulatorias dtoOrdenesAmbulatorias) {
		return ordenesAmbulatoriasDelegate.buscarPorParametros(dtoOrdenesAmbulatorias);
	}
	
	/**
	 * @see com.servinte.axioma.dao.interfaz.ordenes.IOrdenesAmbulatoriasDAO#obtenerSolciitudesAutorizadasAmbulatorias(com.princetonsa.dto.ordenes.DtoSolicitud, java.lang.Integer[])
	 */
	public  ArrayList<DtoSolicitud>  obtenerSolciitudesAutorizadasAmbulatorias(DtoSolicitud solicitud,Integer[] parametros){
		return ordenesAmbulatoriasDelegate.obtenerSolciitudesAutorizadasAmbulatorias(solicitud, parametros);
	}
	
}
