package com.servinte.axioma.mundo.impl.ordenes;

import java.util.ArrayList;

import util.ConstantesIntegridadDominio;

import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoResultadoConsultaProcesosCierre;
import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.ordenes.DtoOrdenesAmbulatorias;
import com.princetonsa.dto.ordenes.DtoSolicitud;
import com.servinte.axioma.dao.fabrica.ordenes.OrdenesFabricaDAO;
import com.servinte.axioma.dao.interfaz.ordenes.IOrdenesAmbulatoriasDAO;
import com.servinte.axioma.dto.capitacion.DtoAutorizacionCapitacionOrdenAmbulatoria;
import com.servinte.axioma.mundo.interfaz.ordenes.IOrdenesAmbulatoriasMundo;
import com.servinte.axioma.orm.OrdenesAmbulatorias;

public class OrdenesAmbulatoriasMundo implements IOrdenesAmbulatoriasMundo{

	
	IOrdenesAmbulatoriasDAO dao;
	

	/**
	 * Constructor
	 */
	public OrdenesAmbulatoriasMundo (){
		dao =OrdenesFabricaDAO.crearOrdenesAmbulatoriasDAO();
	}

	
	/**
	 * Retorna las ordenes ambulatorias de Servicios E Insumos y Medicamentos
	 *  
	 * @param parametros
	 * @return ArrayList<DtoOrdenesAmbulatorias> 
	 * @autor Camilo Gómez
	 */	
	public ArrayList<DtoOrdenesAmbulatorias> obtenerOrdenesAmbulatorias(DtoOrdenesAmbulatorias parametros){
		return dao.obtenerOrdenesAmbulatorias(parametros);
	}
	
	/**
	 * Este método se encarga se consultar las órdenes ambulatorias con estado diferente a anulado 
	 * 
	 * @author Fabián Becerra
	 * @param dtoFiltro parámetros de consulta
	 * @return dtoResultadoConsulta campos de las órdenes ambulatorias y sus servicios o articulos
	 */
	 public ArrayList<DtoResultadoConsultaProcesosCierre> consultarOrdenesAmbulatorias(DtoProcesoPresupuestoCapitado dtoFiltro){
		return dao.consultarOrdenesAmbulatorias(dtoFiltro);
	 }


	@Override
	public ArrayList<DtoSolicitud> obtenerOrdenesAmbulatoriasPorCuentaORango(DtoSolicitud parametros,String estadoSolicitud) {
		return dao.obtenerOrdenesAmbulatoriasPorCuentaORango(parametros,estadoSolicitud);
	}
	
	/**
	 * Método que se encarga de consultar si la Orden Ambulatoria tiene asociada una autorización de 
	 * Capitación Subcontratada.
	 * 
	 * @author Camilo Gómez
	 * @param DtoAutorizacionCapitacionOrdenAmbulatoria dto
	 * @return DtoAutorizacionCapitacionOrdenAmbulatoria
	 */
	public ArrayList<DtoAutorizacionCapitacionOrdenAmbulatoria> existeAutorizacionCapitaOrdenAmbulatoria(DtoAutorizacionCapitacionOrdenAmbulatoria dto){
		return dao.existeAutorizacionCapitaOrdenAmbulatoria(dto);
	}


	@Override
	public ArrayList<OrdenesAmbulatorias> buscarPorParametros(DtoOrdenesAmbulatorias dtoOrdenesAmbulatorias) {
		return dao.buscarPorParametros(dtoOrdenesAmbulatorias);
	}
	
	
	/**
	 * @see com.servinte.axioma.mundo.interfaz.ordenes.IOrdenesAmbulatoriasMundo#obtenerSolicitudesPorAutorizar(com.princetonsa.dto.ordenes.DtoSolicitud)
	 */
	public  ArrayList<DtoSolicitud> obtenerSolicitudesPorAutorizar(DtoSolicitud parametros){
		
		//se obtienen las ordenen ambulatorias autorizadas 
		ArrayList<DtoSolicitud> ambAutorizados=	dao.obtenerOrdenesAmbulatoriasPorCuentaORango(parametros,ConstantesIntegridadDominio.acronimoAutorizado);
		ArrayList<DtoSolicitud> porAutorizar = new ArrayList<DtoSolicitud>();
		
		//se recorre la lista de ordenes 
		for (DtoSolicitud dtoSolicitud : ambAutorizados) {
			//se obtienen la lista de articulos por autorizar 
				dtoSolicitud=listaAutorizados(dtoSolicitud, obtenerSolciitudesAutorizadasAmbulatorias(dtoSolicitud, listaArtAmbAutorizados(dtoSolicitud)));
				if(dtoSolicitud.getListaArticulos().size()>0){
					porAutorizar.add(dtoSolicitud);
				}
		}
		
		//lista de DtoSolicitud por autorizar 
		return porAutorizar;
	}
	
	/**
	 * metodo que obtiene la lista de articulos solicitados
	 * @param dtoSolicitud
	 * @param soloAutorizados
	 * @return DtoSolicitud con lista de autorizados 
	 */
	public DtoSolicitud listaAutorizados(DtoSolicitud dtoSolicitud , ArrayList<DtoSolicitud> soloAutorizados){
		Boolean flagEsta=false;
		Boolean cambio = false;
		ArrayList<DtoArticulosAutorizaciones> articulosAutorizados= new ArrayList<DtoArticulosAutorizaciones>();
		
		// se recorre la lista de los que ya estan autorizados 
		for (int i = 0; i < soloAutorizados.size(); i++) {
			flagEsta=false;
			
			//se recorre la lista de los que ya estan 
			for (int j = 0; j < dtoSolicitud.getListaArticulos().size() && !flagEsta ; j++) {
				
				//si cumple la condicion de igualdad de codigo de articulo entonces se adiciona a la lista 
				if(dtoSolicitud.getListaArticulos().get(j).getCodigoArticulo().equals(soloAutorizados.get(i).getNumeroSolicitud())){
					flagEsta=true;
					 cambio = true;
					dtoSolicitud.getListaArticulos().remove(j);
					//articulosAutorizados.add(dtoSolicitud.getListaArticulos().get(j));
				}
			}
		}
		
		//si la cantidad de articulos es igual no se adicona por que indica que ya fueron autorizados tods 
		if(!cambio){
			dtoSolicitud.setListaArticulos(new ArrayList<DtoArticulosAutorizaciones>());
		}
		
		//dtoSolicitud con lista de articulos por autorizar 
		return dtoSolicitud;
	}
	
	
	/**
	 * @param dtoSolicitud
	 * @return Integer[] con codigos de lso ariticulos pro autorizar 
	 */
	public Integer[] listaArtAmbAutorizados(DtoSolicitud dtoSolicitud){
		
		Integer[] codigosArticulos= new Integer[dtoSolicitud.getListaArticulos().size()];
		
		//se adicionan los codigos a al arreglo  
		for (int i = 0; i < dtoSolicitud.getListaArticulos().size(); i++) {
			codigosArticulos[i]=dtoSolicitud.getListaArticulos().get(i).getCodigoArticulo();
		}
		
		//arreglo de integer solicitado
		return codigosArticulos;
	}
	
	/**
	 * @see com.servinte.axioma.mundo.interfaz.ordenes.IOrdenesAmbulatoriasMundo#obtenerSolciitudesAutorizadasAmbulatorias(com.princetonsa.dto.ordenes.DtoSolicitud, java.lang.Integer[])
	 */
	public  ArrayList<DtoSolicitud>  obtenerSolciitudesAutorizadasAmbulatorias(DtoSolicitud solicitud,Integer[] parametros){
		return dao.obtenerSolciitudesAutorizadasAmbulatorias(solicitud, parametros);
	}
}
