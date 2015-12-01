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
	 * Este m�todo consulta las peticiones dependiendo de los par�metros enviados
	 * @param dtoFiltro
	 * @return ArrayList<DtoSolicitud>
	 *
	 * @autor Cristhian Murillo
	*/
	public ArrayList<DtoSolicitud> obtenerOrdenesPeticionesPorCuentaORango(DtoSolicitud parametros){
		return delegate.obtenerOrdenesPeticionesPorCuentaORango(parametros);
	}

	/**
	 * M�todo que se encarga de consultar si la Peticion tiene asociada una autorizaci�n de 
	 * Capitaci�n Subcontratada.
	 * RQF 02-0025 Autorizaciones Capitaci�n
	 * 
	 * @author Camilo G�mez
	 * @param DtoAutorizacionCapitacionPeticion dto
	 * @return ArrayList<DtoAutorizacionCapitacionPeticion> dtoPeticiones
	 */
	public ArrayList<DtoAutorizacionCapitacionPeticion> existeAutorizacionCapitaPeticion(DtoAutorizacionCapitacionPeticion dto){
		return delegate.existeAutorizacionCapitaPeticion(dto);
	}
	
	/**
	 * Este m�todo consulta las peticiones de servicios dependiendo de los par�metros enviados
	 * @param dtoFiltro
	 * @return ArrayList<DtoResultadoConsultaProcesosCierre>
	 *
	 * @autor Fabi�n Becerra
	*/
	public ArrayList<DtoResultadoConsultaProcesosCierre> consultarPeticionesServicios(DtoProcesoPresupuestoCapitado dtoFiltro){
		return delegate.consultarPeticionesServicios(dtoFiltro);
	}
	
	/**
	 * Este m�todo consulta las peticiones de Articulos dependiendo de los par�metros enviados
	 * @param dtoFiltro
	 * @return ArrayList<DtoResultadoConsultaProcesosCierre>
	 *
	 * @autor Fabi�n Becerra
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
