package com.princetonsa.dao.odontologia;

import java.util.ArrayList;


import com.princetonsa.dto.odontologia.DtoHallazgoVsProgramaServicio;

public interface HallazgoVsProgramaServicioDao {
	
	/**
	 * 
	 * @param 
	 * @return
	 */
	public boolean modificar(DtoHallazgoVsProgramaServicio dtoNuevo, DtoHallazgoVsProgramaServicio dtoWhere);
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public ArrayList<DtoHallazgoVsProgramaServicio> cargar(DtoHallazgoVsProgramaServicio dtoWhere);
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public boolean eliminar(DtoHallazgoVsProgramaServicio dtoWhere);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public double guardar(DtoHallazgoVsProgramaServicio dto) ;
	/**
	 * 
	 * 
	 */

}
