package com.princetonsa.dao.odontologia;

import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoServicioHonorarios;

/**
 * 
 * @author axioma
 *
 */
public interface ServicioHonorariosDao {

	/**
	 * 
	 * @param objetoServicioHonorarios
	 * @return
	 */
	public boolean modificar(DtoServicioHonorarios dtoNuevo, DtoServicioHonorarios dtoWhere);
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public ArrayList<DtoServicioHonorarios> cargar(	DtoServicioHonorarios dtoWhere);
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public boolean eliminar(DtoServicioHonorarios dtoWhere);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public double guardar(DtoServicioHonorarios dto) ;
}