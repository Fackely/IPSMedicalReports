package com.servinte.axioma.mundo.impl.salasCirugia;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoResultadoConsultaProcesosCierre;
import com.princetonsa.dto.ordenes.DtoSolicitud;
import com.servinte.axioma.dao.fabrica.salasCirugia.SalasCirugiaFabricaDAO;
import com.servinte.axioma.dao.interfaz.salasCirugia.IPeticionQxDAO;
import com.servinte.axioma.dto.capitacion.DtoAutorizacionCapitacionPeticion;
import com.servinte.axioma.mundo.interfaz.salasCirugia.IPeticionQxMundo;

public class PeticionQxMundo implements IPeticionQxMundo{
	
	IPeticionQxDAO dao;
	
	/**
	  * 
	  * M�todo constructor de la clase
	  * @author, Fabi�n Becerra
	 */
	public PeticionQxMundo() {
		dao = SalasCirugiaFabricaDAO.crearPeticionQxDAO();
	}
	
	/**
	 * Este m�todo consulta las peticiones de servicios dependiendo de los par�metros enviados
	 * @param dtoFiltro
	 * @return ArrayList<DtoResultadoConsultaProcesosCierre>
	 *
	 * @autor Fabi�n Becerra
	*/
	public ArrayList<DtoResultadoConsultaProcesosCierre> consultarPeticionesServicios(DtoProcesoPresupuestoCapitado dtoFiltro){
		return dao.consultarPeticionesServicios(dtoFiltro);
	}
	
	/**
	 * Este m�todo consulta las peticiones de Articulos dependiendo de los par�metros enviados
	 * @param dtoFiltro
	 * @return ArrayList<DtoResultadoConsultaProcesosCierre>
	 *
	 * @autor Fabi�n Becerra
	*/
	public ArrayList<DtoResultadoConsultaProcesosCierre> consultarPeticionesArticulos(DtoProcesoPresupuestoCapitado dtoFiltro){
		return dao.consultarPeticionesArticulos(dtoFiltro);
	}
	
	/**
	 * Este m�todo consulta las peticiones dependiendo de los par�metros enviados
	 * @param dtoFiltro
	 * @return ArrayList<DtoSolicitud>
	 *
	 * @autor Cristhian Murillo
	*/
	public ArrayList<DtoSolicitud> obtenerOrdenesPeticionesPorCuentaORango(DtoSolicitud parametros){
		return dao.obtenerOrdenesPeticionesPorCuentaORango(parametros);
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
		return dao.existeAutorizacionCapitaPeticion(dto);
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.salasCirugia.IPeticionQxMundo#consultarPeticionesArticulosAnulados(com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado)
	 */
	@Override
	public ArrayList<DtoResultadoConsultaProcesosCierre> consultarPeticionesArticulosAnulados(DtoProcesoPresupuestoCapitado dtoFiltro) {
		return dao.consultarPeticionesArticulosAnulados(dtoFiltro);
	}

}
