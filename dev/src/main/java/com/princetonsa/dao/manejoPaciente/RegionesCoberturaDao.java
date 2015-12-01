package com.princetonsa.dao.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DtoRegionesCobertura;



public interface RegionesCoberturaDao {

	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public ArrayList<DtoRegionesCobertura> cargar(DtoRegionesCobertura dtoWhere);
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
}
