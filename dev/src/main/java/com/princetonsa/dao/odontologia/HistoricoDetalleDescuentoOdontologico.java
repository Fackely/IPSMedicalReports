package com.princetonsa.dao.odontologia;

import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoHistoricoDescuentoOdontologico;
import com.princetonsa.dto.odontologia.DtoHistoricoDetalleDescuentoOdontologico;

public interface HistoricoDetalleDescuentoOdontologico {

	/**
	 * 
	 * @param 
	 * @return
	 */
	public boolean modificar(DtoHistoricoDetalleDescuentoOdontologico dtoNuevo, DtoHistoricoDetalleDescuentoOdontologico dtoWhere);
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public ArrayList<DtoHistoricoDetalleDescuentoOdontologico> cargar(DtoHistoricoDetalleDescuentoOdontologico dtoWhere);
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public double guardar(DtoHistoricoDetalleDescuentoOdontologico dto) ;
	
	
	
}
