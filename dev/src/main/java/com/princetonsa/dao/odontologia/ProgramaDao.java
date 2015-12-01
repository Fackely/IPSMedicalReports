 package com.princetonsa.dao.odontologia;

import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoPrograma;


public  interface ProgramaDao {

	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public double  guardar(DtoPrograma dto);
	
	 /**
	  * 
	  * @param dto
	  * @return
	  */
	public ArrayList<DtoPrograma> cargar(DtoPrograma dto);
			
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public boolean modificar(DtoPrograma dto);
			
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public boolean eliminar(DtoPrograma dto);
	
		
	/**
	 * 
	 * @param dto
	 * @param incluirInactivos 
	 * @return
	 */
	public  ArrayList<DtoPrograma> cargarConsultaAvanzada( DtoPrograma dto, boolean incluirInactivos);
	
			
	/**
	 * 
	 * @param programa
	 * @return
	 */
	public String obtenerEspeciliadadPrograma(double programa);
}
