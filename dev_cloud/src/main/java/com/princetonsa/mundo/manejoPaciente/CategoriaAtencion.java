package com.princetonsa.mundo.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.manejoPaciente.DtoCategoriaAtencion;


public class CategoriaAtencion {
	
	

	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoCategoriaAtencion> cargar(DtoCategoriaAtencion dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCategoriaAtencionDao().cargar(dtoWhere);
	}
	

}
