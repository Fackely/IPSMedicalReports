/**
 * 
 */
package com.servinte.axioma.servicio.impl.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IEntidadesSubcontratadasMundo;
import com.servinte.axioma.orm.EntidadesSubcontratadas;
import com.servinte.axioma.servicio.interfaz.facturacion.IEntidadesSubcontratadasServicio;

/**
 * @author Cristhian Murillo
 *
 */
public class EntidadesSubcontratadasServicio implements IEntidadesSubcontratadasServicio
{
	
	IEntidadesSubcontratadasMundo entidadesSubcontratadasMundo;

	
	/**
	 * Constructor
	 */
	public EntidadesSubcontratadasServicio() 
	{
		entidadesSubcontratadasMundo = FacturacionFabricaMundo.crearEntidadesSubcontratadasMundo();
	}

	
	@Override
	public ArrayList<DtoEntidadSubcontratada> listarEntidadesSubXCentroCostoActivoContratoVigente(int codCentroCosto) {
		return entidadesSubcontratadasMundo.listarEntidadesSubXCentroCostoActivoContratoVigente(codCentroCosto);
	}
	
	
	/**
	 * Retorna las EntidadesSubcontratadas activas para el centro de costo
	 * de la funcionalidad CentroCosto por Unidades Subcontratadas.
	 * 
	 * @param codCentroCosto
	 * @return ArrayList<DtoEntidadSubcontratada>
	 * 
	 * 
	 */
	@Override
	public ArrayList<DtoEntidadSubcontratada> listarEntidadesSubXCentroCostoActivo (int codCentroCosto){
		return entidadesSubcontratadasMundo.listarEntidadesSubXCentroCostoActivo(codCentroCosto);
	}


	@Override
	public ArrayList<DtoEntidadSubcontratada> listarEntidadesSubXViaIngreso(DtoEntidadSubcontratada parametros) {
		return entidadesSubcontratadasMundo.listarEntidadesSubXViaIngreso(parametros);
	}
	
	/**
	 * Implementación del método findById
	 * @param id
	 * @return EntidadesSubcontratadas
	 */
	public EntidadesSubcontratadas obtenerEntidadesSubcontratadasporId(long id){
		return entidadesSubcontratadasMundo.obtenerEntidadesSubcontratadasporId(id);
	}
	
	/**
	 * Retorna las EntidadesSubcontratadas activas en el sistema
	 * @return ArrayList<DtoEntidadSubcontratada>
	 * 
	 * @author Fabián Becerra
	 */
	public ArrayList<DtoEntidadSubcontratada> listarEntidadesSubActivasEnSistema(){
		return entidadesSubcontratadasMundo.listarEntidadesSubActivasEnSistema();
	}


	@Override
	public DtoEntidadSubcontratada listarEntidadesSubXId(Long codigoPk) {
		return entidadesSubcontratadasMundo.listarEntidadesSubXId(codigoPk);
	}

}


