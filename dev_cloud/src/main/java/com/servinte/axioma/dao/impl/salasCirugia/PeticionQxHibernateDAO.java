package com.servinte.axioma.dao.impl.salasCirugia;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoResultadoConsultaProcesosCierre;
import com.princetonsa.dto.ordenes.DtoSolicitud;
import com.servinte.axioma.dao.interfaz.salasCirugia.IPeticionQxDAO;
import com.servinte.axioma.dto.capitacion.DtoAutorizacionCapitacionPeticion;
import com.servinte.axioma.orm.delegate.salascirujia.PeticionQxDelegate;

public class PeticionQxHibernateDAO implements IPeticionQxDAO{
	
	private PeticionQxDelegate delegate;
	
	/**
	 * Constructor
	 */
	public PeticionQxHibernateDAO(){
		delegate	= new PeticionQxDelegate();
	}
	
	/**
	 * Este método consulta las peticiones dependiendo de los parámetros enviados
	 * @param dtoFiltro
	 * @return ArrayList<DtoSolicitud>
	 *
	 * @autor Cristhian Murillo
	*/
	public ArrayList<DtoSolicitud> obtenerOrdenesPeticionesPorCuentaORango(DtoSolicitud parametros){
		return delegate.obtenerOrdenesPeticionesPorCuentaORango(parametros);
	}

	/**
	 * Método que se encarga de consultar si la Peticion tiene asociada una autorización de 
	 * Capitación Subcontratada.
	 * RQF 02-0025 Autorizaciones Capitación
	 * 
	 * @author Camilo Gómez
	 * @param DtoAutorizacionCapitacionPeticion dto
	 * @return ArrayList<DtoAutorizacionCapitacionPeticion> dtoPeticiones
	 */
	public ArrayList<DtoAutorizacionCapitacionPeticion> existeAutorizacionCapitaPeticion(DtoAutorizacionCapitacionPeticion dto){
		return delegate.existeAutorizacionCapitaPeticion(dto);
	}
	
	/**
	 * Este método consulta las peticiones de servicios dependiendo de los parámetros enviados
	 * @param dtoFiltro
	 * @return ArrayList<DtoResultadoConsultaProcesosCierre>
	 *
	 * @autor Fabián Becerra
	*/
	public ArrayList<DtoResultadoConsultaProcesosCierre> consultarPeticionesServicios(DtoProcesoPresupuestoCapitado dtoFiltro){
		return delegate.consultarPeticionesServicios(dtoFiltro);
	}
	
	/**
	 * Este método consulta las peticiones de Articulos dependiendo de los parámetros enviados
	 * @param dtoFiltro
	 * @return ArrayList<DtoResultadoConsultaProcesosCierre>
	 *
	 * @autor Fabián Becerra
	*/
	public ArrayList<DtoResultadoConsultaProcesosCierre> consultarPeticionesArticulos(DtoProcesoPresupuestoCapitado dtoFiltro){
		return delegate.consultarPeticionesArticulos(dtoFiltro);
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.salasCirugia.IPeticionQxDAO#consultarPeticionesArticulosAnulados(com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado)
	 */
	@Override
	public ArrayList<DtoResultadoConsultaProcesosCierre> consultarPeticionesArticulosAnulados(DtoProcesoPresupuestoCapitado dtoFiltro) {
		return delegate.consultarPeticionesArticulosAnulados(dtoFiltro);
	}
}
