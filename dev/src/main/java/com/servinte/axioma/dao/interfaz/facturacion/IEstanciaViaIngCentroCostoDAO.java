package com.servinte.axioma.dao.interfaz.facturacion;

import java.util.List;

import com.servinte.axioma.dao.interfaz.IBaseDAO;
import com.servinte.axioma.orm.EstanciaViaIngCentroCosto;

public interface IEstanciaViaIngCentroCostoDAO  extends IBaseDAO<EstanciaViaIngCentroCosto>{
	
	/**
	 * 
	 * @param idEntidadSubContratada
	 * @return
	 */
	public List<EstanciaViaIngCentroCosto> listarEstanciasxEntidadesSubContratadas(Long idEntidadSubContratada);
	
	
	public boolean guardarEstancia(EstanciaViaIngCentroCosto estancia);

}
