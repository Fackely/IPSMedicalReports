package com.princetonsa.dao.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DtoCategoriaAtencion;



public interface CategoriaAtencionDao {
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public ArrayList<DtoCategoriaAtencion> cargar(DtoCategoriaAtencion dtoWhere);
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */

}
