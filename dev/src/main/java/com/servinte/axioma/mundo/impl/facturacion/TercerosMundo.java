package com.servinte.axioma.mundo.impl.facturacion;

import com.princetonsa.dto.cargos.DtoTercero;
import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.facturacion.ITercerosDAO;
import com.servinte.axioma.mundo.interfaz.facturacion.ITercerosMundo;
import com.servinte.axioma.orm.Terceros;

public class TercerosMundo implements ITercerosMundo {
	
	private ITercerosDAO tercerosDAO;
	
	public TercerosMundo(){
		setTercerosDAO(FacturacionFabricaDAO.crearTercerosDAO());
		
	}
	

	public void setTercerosDAO(ITercerosDAO tercerosDAO) {
		this.tercerosDAO = tercerosDAO;
	}

	public ITercerosDAO getTercerosDAO() {
		return tercerosDAO;
	}

	@Override
	public Terceros findById(int id) {
		return tercerosDAO.findById(id);
	}
	
	/**
	 * Obtener el tercero por el codigo pk de una entidad subcontratada 
	 * 
	 * @param codEntidadSubcontratada
	 * @return DtoTercero
	 * 
	 * @author Fabián Becerra
	 */
	public DtoTercero obtenerTerceroXEntidadSub (long codEntidadSubcontratada){
		return tercerosDAO.obtenerTerceroXEntidadSub(codEntidadSubcontratada);
	}

}
