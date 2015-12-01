package com.servinte.axioma.mundo.impl.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IEntidadesSubcontratadasDAO;
import com.servinte.axioma.mundo.interfaz.facturacion.IEntidadesSubcontratadasMundo;
import com.servinte.axioma.orm.EntidadesSubcontratadas;

/**
 * Implementaci�n de la Interfaz IEntidadesSubcontratadasMundo>
 * @author Cristhian Murillo
 */
public class EntidadesSubcontratadasMundo implements IEntidadesSubcontratadasMundo 
{
	
	private IEntidadesSubcontratadasDAO entidadesSubcontratadasDAO;
	
	
	/**
	 * M�todo constructor de la clase
	 */
	public EntidadesSubcontratadasMundo()
	{
		entidadesSubcontratadasDAO 	= FacturacionFabricaDAO.crearEntidadesSubcontratadasDAO();
	}


	
	@Override
	public ArrayList<DtoEntidadSubcontratada> listarEntidadesSubXCentroCostoActivoContratoVigente(int codCentroCosto) {
		return entidadesSubcontratadasDAO.listarEntidadesSubXCentroCostoActivoContratoVigente(codCentroCosto);
	}


	@Override
	public ArrayList<DtoEntidadSubcontratada> listarEntidadesSubXViaIngreso(DtoEntidadSubcontratada parametros) {
		return entidadesSubcontratadasDAO.listarEntidadesSubXViaIngreso(parametros);
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
		return entidadesSubcontratadasDAO.listarEntidadesSubXCentroCostoActivo(codCentroCosto);
	}
	
	

	/**
	 * Implementaci�n del m�todo findById
	 * @param id
	 * @return EntidadesSubcontratadas
	 */
	public EntidadesSubcontratadas obtenerEntidadesSubcontratadasporId(long id){
		return entidadesSubcontratadasDAO.obtenerEntidadesSubcontratadasporId(id);
	}
	
	/**
	 * Retorna las EntidadesSubcontratadas activas en el sistema
	 * @return ArrayList<DtoEntidadSubcontratada>
	 * 
	 * @author Fabi�n Becerra
	 */
	public ArrayList<DtoEntidadSubcontratada> listarEntidadesSubActivasEnSistema(){
		return entidadesSubcontratadasDAO.listarEntidadesSubActivasEnSistema();
	}


	@Override
	public DtoEntidadSubcontratada listarEntidadesSubXId(Long codigoPk) {
		return entidadesSubcontratadasDAO.listarEntidadesSubXId(codigoPk);
	}
	
	
}
