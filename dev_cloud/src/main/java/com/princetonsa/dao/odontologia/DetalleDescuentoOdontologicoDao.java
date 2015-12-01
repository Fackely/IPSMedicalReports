package com.princetonsa.dao.odontologia;

import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoDetalleDescuentoOdontologico;

public interface DetalleDescuentoOdontologicoDao {

	/**
	 * 
	 * @param 
	 * @return
	 */
	public boolean modificar(DtoDetalleDescuentoOdontologico dtoNuevo, DtoDetalleDescuentoOdontologico dtoWhere);
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public ArrayList<DtoDetalleDescuentoOdontologico> cargar(DtoDetalleDescuentoOdontologico dtoWhere);
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public boolean eliminar(DtoDetalleDescuentoOdontologico dtoWhere);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public double guardar(DtoDetalleDescuentoOdontologico dto) ;
	
	/**
	 * 
	 * 
	 */
	
	public boolean existeRangoPresupuesto(double consecutivo, double centroAtencion, double ValorMinimo, double ValorMaximo, double codigo);
	
	/**
	 * 
	 */
}
