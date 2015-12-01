package com.servinte.axioma.mundo.impl.manejoPaciente;

import java.util.ArrayList;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;

import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;
import com.princetonsa.dto.ordenes.DtoSolicitud;
import com.servinte.axioma.dao.fabrica.ManejoPacienteDAOFabrica;
import com.servinte.axioma.dao.fabrica.ordenes.OrdenesFabricaDAO;
import com.servinte.axioma.dao.interfaz.inventario.IAurorizacionesEntSubCapitacionDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IAutorizacionesEntidadesSubDAO;
import com.servinte.axioma.dao.interfaz.ordenes.ISolicitudesDAO;
import com.servinte.axioma.dao.interfaz.ordenes.ISolicitudesPosponerDAO;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IAurorizacionesEntSubCapitacionMundo;
import com.servinte.axioma.orm.AutorizacionesEntSubServi;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;
import com.servinte.axioma.orm.SolicitudesPosponer;

/**
 * @author Cristhian Murillo
 */
public class AurorizacionesEntSubCapitacionMundo implements IAurorizacionesEntSubCapitacionMundo {
	
	
	private IAutorizacionesEntidadesSubDAO autorizacionesEntidadesSubDAO;
	private IAurorizacionesEntSubCapitacionDAO aurorizacionesEntSubCapitacionHibernateDAO;
	private ISolicitudesDAO solicitudesDAO;
	private ISolicitudesPosponerDAO solicitudesPosponerDAO;
	
	
	public AurorizacionesEntSubCapitacionMundo() {
		inicializar();
	}
	
	/**
	 * Inicializador
	 */
	private void inicializar() {
		autorizacionesEntidadesSubDAO 				= ManejoPacienteDAOFabrica.crearAutorizacionesEntidadesSubDAO();
		aurorizacionesEntSubCapitacionHibernateDAO	= ManejoPacienteDAOFabrica.crearAurorizacionesEntSubCapitacion();
		solicitudesDAO								= OrdenesFabricaDAO.crearSolicitudesDAO();
		solicitudesPosponerDAO						= OrdenesFabricaDAO.crearSolicitudesPosponerDAO();
	}

	

	@Override
	public ArrayList<DtoAutorizacionEntSubcontratadasCapitacion> obtenerAutorizacionesPorEntSub(
			DtoAutorizacionEntSubcontratadasCapitacion dtoEntregaMedicamentosInsumosEntSubcontratadas) {
		return autorizacionesEntidadesSubDAO.obtenerAutorizacionesPorEntSub(dtoEntregaMedicamentosInsumosEntSubcontratadas);
	}


	@Override
	public ArrayList<AutorizacionesEntSubServi> listarAutorizacionesEntSubServiPorAutoEntSub(
			DtoAutorizacionEntSubcontratadasCapitacion dtoParametros) {
		return aurorizacionesEntSubCapitacionHibernateDAO.listarAutorizacionesEntSubServiPorAutoEntSub(dtoParametros);
	}


	@Override
	public ArrayList<DtoArticulosAutorizaciones> listarautorizacionesEntSubArticuPorAutoEntSub(
			DtoAutorizacionEntSubcontratadasCapitacion dtoParametros) {
		return aurorizacionesEntSubCapitacionHibernateDAO.listarautorizacionesEntSubArticuPorAutoEntSub(dtoParametros);
	}

	
	@Override
	public void validarListaArticulos(DtoAutorizacionEntSubcontratadasCapitacion dtoAutorizacionEntSubcontratadasCapitacion) 
	{
		/*
		 * Se itera dos veces por tipo de articulo para no iterar todos los articulos en caso que se encuentren coincidencias  
		*/
		boolean existenArticulosMedicamentos 	= false;
		boolean existenArticulosInsumos			= false;
		
		
		// Se itera para verificar si existen articulos
		for (DtoArticulosAutorizaciones dtoArticulosAutorizaciones : dtoAutorizacionEntSubcontratadasCapitacion.getListaArticulos()) {
			if(dtoArticulosAutorizaciones.getEsMedicamento() == ConstantesBD.acronimoSiChar){
				existenArticulosMedicamentos 	= true;
				break;
			}
		} dtoAutorizacionEntSubcontratadasCapitacion.setExistenArticulosMedicamentos(existenArticulosMedicamentos);
		
		
		// Se itera para verificar si existen Insumos
		for (DtoArticulosAutorizaciones dtoArticulosAutorizaciones : dtoAutorizacionEntSubcontratadasCapitacion.getListaArticulos()) {
			if(dtoArticulosAutorizaciones.getEsMedicamento() == ConstantesBD.acronimoNoChar){
				existenArticulosInsumos			= true;
				break;
			}
		} dtoAutorizacionEntSubcontratadasCapitacion.setExistenArticulosInsumos(existenArticulosInsumos);
		
	}

	@Override
	public AutorizacionesEntidadesSub obtenerAutorizacionesEntidadesSubPorId(long id) {
		return aurorizacionesEntSubCapitacionHibernateDAO.obtenerAutorizacionesEntidadesSubPorId(id);
	}

	
	@Override
	public ArrayList<DtoSolicitud> obtenerSolicitudesPorCuentaORango(DtoSolicitud parametros,String codigoEstado){
		return solicitudesDAO.obtenerSolicitudesPorCuentaORango(parametros,codigoEstado);
	}

	
	@Override
	public void guardarSolicitudesPosponer(SolicitudesPosponer transientInstance) {
		solicitudesPosponerDAO.guardarSolicitudesPosponer(transientInstance);
	}


	@Override
	public ArrayList<DtoSolicitud> obtenerSolicitudesSubcuenta(DtoSolicitud parametros) {
		return solicitudesDAO.obtenerSolicitudesSubcuenta(parametros);
	}


	
	@Override
	public ArrayList<DtoAutorizacionEntSubcontratadasCapitacion> obtenerAutorizacionesPorEntSubPorNumeroSolicitud(
			DtoAutorizacionEntSubcontratadasCapitacion dtoEntregaMedicamentosInsumosEntSubcontratadas) {
		return aurorizacionesEntSubCapitacionHibernateDAO.obtenerAutorizacionesPorEntSubPorNumeroSolicitud(dtoEntregaMedicamentosInsumosEntSubcontratadas);
	}

	/**
	 * @see com.servinte.axioma.mundo.interfaz.manejoPaciente.IAurorizacionesEntSubCapitacionMundo#obtenerArticulosSinAutorizar(com.princetonsa.dto.ordenes.DtoSolicitud)
	 */
	@Override
	public ArrayList<DtoSolicitud> obtenerArticulosSinAutorizar(DtoSolicitud parametros) {
		
		//se obtienen todas las solictudes hechas en estado autorizado con todos sus articulos asociados 
		ArrayList<DtoSolicitud> tmpAutorizadas = solicitudesDAO.obtenerSolicitudesPorCuentaORango(parametros,ConstantesIntegridadDominio.acronimoAutorizado);
		ArrayList<DtoSolicitud> solicitudesPendientesPorAutorizar = new ArrayList<DtoSolicitud>();
		
		//se recorren la ssolicitudes autorizadoas para obtener las que aunno estan autorizadas 
		for (DtoSolicitud dtoSolicitud : tmpAutorizadas) {
			
			//se obtienen las solcitudes ya autorizadas 
				solicitudesDAO.obtenerDetalleArticulosSolicitudesPorCuentaAutorizados(dtoSolicitud);
				ArrayList<DtoSolicitud>  soliciTmp = solicitudesDAO.obtenerSolciitudesAutorizadas(dtoSolicitud, obtenerListaCodigosArticulos(dtoSolicitud));
				
				//se obtiernen las solcitudes que aun no estan autorizadas 
				dtoSolicitud= articulosAutorizados(soliciTmp, dtoSolicitud);
				if(dtoSolicitud.getListaArticulos().size()>0){
					
					//se adiciona a la lista las solicitudes que aun tienen articulos por autorizar 
					solicitudesPendientesPorAutorizar.add(dtoSolicitud);
				}
		}
		
		//lista con las solicitudes por autorizar 
		return solicitudesPendientesPorAutorizar;
	}
	
	
	/**
	 * Obtiene los articulos que aun no están autorizados 
	 * @param soliciTmp
	 * @param dtoSolicitud
	 * @return DtoSolicitud
	 */
	public DtoSolicitud articulosAutorizados(ArrayList<DtoSolicitud>  soliciTmp,DtoSolicitud dtoSolicitud ){
		Boolean flagEsta=false;
		Boolean flagCambio = false;
		ArrayList<DtoArticulosAutorizaciones> listaArticulos= new ArrayList<DtoArticulosAutorizaciones>();
		
		//se recorren la lista con los codigos de las solcitudes por autorizar 
		for (int i = 0; i <soliciTmp.size(); i++) {
			flagEsta=false;
			
			//se recorre la lista de de articulos para buscar los que cumplan la condicion de igualdad de codigo de articulo
			for (int j = 0; j < dtoSolicitud.getListaArticulos().size() && !flagEsta; j++) {
				
				//si se culple la condicion de adionan a la lista de articulos por autorizar 
				if(dtoSolicitud.getListaArticulos().get(j).getCodigoArticulo().equals(soliciTmp.get(i).getNumeroSolicitud())){
					flagEsta=true;
					flagCambio = true;
					dtoSolicitud.getListaArticulos().remove(j);
				}
			}
		}
		
		//si la cantidad de articulos es la misma que la que hay que buscar entonces no se adiciona por que indica que todos los articulos 
		//fueron autorizardos
		if(!flagCambio){
			dtoSolicitud.setListaArticulos(new ArrayList<DtoArticulosAutorizaciones>());
		}
		
		//dtoSolicitud con la lista de artuculos por autorizar 
		return dtoSolicitud;
	}
	
	
	/**
	 * Obtiene en un arreglo los codigos de los articulos por buscar 
	 * @param dtoSolicitud
	 * @return Integer[] con códigos de articulos por autorizar 
	 */
	public Integer[]  obtenerListaCodigosArticulos(DtoSolicitud dtoSolicitud){
		ArrayList<Integer> params = new ArrayList<Integer>();
		Integer[] parametros;
		
		//se recorre la lista de articulos y se adicionan a una lista para obtener el tamaño definitivo del arreglo
		for (DtoArticulosAutorizaciones art : dtoSolicitud.getListaArticulos()) {
			params.add(art.getCodigoArticulo());
		}
		
		//arreglo de Integer igualal tamaño de la lista 
		parametros=new Integer[params.size()];
		
		//se adicionan al arreglo de Integer 
		for (int i = 0; i < params.size(); i++) {
			parametros[i]=params.get(i);
		}
		
		return parametros;
	}
	
	/**
	 * Obtienen la lista dearticulos por ser autorizados
	 * @param articulosAsociados
	 * @param articulosAutorizados
	 * @return ArrayList<DtoArticulosAutorizaciones>
	 */
	public ArrayList<DtoArticulosAutorizaciones> articulosSinAutorizar(ArrayList<DtoArticulosAutorizaciones> articulosAsociados,
			ArrayList<DtoArticulosAutorizaciones> articulosAutorizados){
		Boolean flagEncontro=false;
		
		//se recorre a lista de articulos autorizados
		for (DtoArticulosAutorizaciones tmpArticulo : articulosAutorizados) {
			flagEncontro=false;
			
			//se busca el articulo que se igual 
			for (int i = 0; i < articulosAsociados.size() && !flagEncontro; i++) {
				flagEncontro=validarIgualdadArticulo(tmpArticulo, articulosAsociados.get(i));
				
				if(flagEncontro){
					articulosAsociados.remove(i);
				}
			}
		}
		
		//lsta con los articulos a autorizar 
		return articulosAsociados;
	}
	
	/**
	 * metodo que valida si dos articulos son iguales 
	 * @param tmpArticuloAutorizado
	 * @param asociado
	 * @return Boolean si son iguales los articulos o no 
	 */
	public Boolean validarIgualdadArticulo(DtoArticulosAutorizaciones tmpArticuloAutorizado,DtoArticulosAutorizaciones asociado){
		
		//se valida por las caracteristicas del articulo 
		if( ( tmpArticuloAutorizado.getCodigoArticulo().equals(asociado.getCodigoArticulo()))
				&& (tmpArticuloAutorizado.getDosisFormulacion().equals( asociado.getDosisFormulacion()))
				&& (tmpArticuloAutorizado.getFrecuenciaFormulacion().equals(asociado.getFrecuenciaFormulacion()))
				&& (tmpArticuloAutorizado.getDiasTratamientoFormulacion().equals(asociado.getDiasTratamientoFormulacion())) 
				&& (tmpArticuloAutorizado.getViaFormulacion().equals(asociado.getViaFormulacion()))
				&& (tmpArticuloAutorizado.getTotalUnidadesFormulacion().equals(asociado.getTotalUnidadesFormulacion()))
				&& (tmpArticuloAutorizado.getTipoFrecuenciaFormulacion().equals(asociado.getTipoFrecuenciaFormulacion()))
				){
			//si son iguales 
			return true;
		}
		
		//si no son iguales 
		return false;
	}
	
}
