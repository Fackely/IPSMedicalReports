package com.princetonsa.dao.odontologia;

import java.util.ArrayList;
import com.princetonsa.dto.odontologia.DtoAliadoOdontologico;


public interface AliadoOdontologicoDao
{
	/**
	 * @param dtoWhere
	 * @return
	 */
	public ArrayList<DtoAliadoOdontologico> cargar (DtoAliadoOdontologico dtoWhere);
	
	/**
	 * @param dtoWhere
	 * @return
	 */
	public boolean eliminar (DtoAliadoOdontologico dtoWhere);
	
	/**
	 * @param dto
	 * @return
	 */
	public double guardar(DtoAliadoOdontologico dto);
	
	/**
	 * 
	 * @param dtoNuevo
	 * @param dtoWhere
	 * @return
	 */
	public boolean modificar(DtoAliadoOdontologico dtoNuevo, DtoAliadoOdontologico dtoWhere);

	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public ArrayList<DtoAliadoOdontologico> cargarDetalle(DtoAliadoOdontologico dtoWhere);
	
}