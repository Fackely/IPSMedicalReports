package com.princetonsa.mundo.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.manejoPaciente.DtoRegionesCobertura;


public class RegionesCobertura {


	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoRegionesCobertura> cargar(DtoRegionesCobertura dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegionesCoberturaDao().cargar(dtoWhere);
	}
	

	
}
