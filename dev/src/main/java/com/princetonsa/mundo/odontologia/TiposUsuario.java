package com.princetonsa.mundo.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.DaoFactory;

import com.princetonsa.dto.odontologia.DtoTiposDeUsuario;

public class TiposUsuario {

	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoTiposDeUsuario> cargar(DtoTiposDeUsuario dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTiposDeUsuarioDao().cargar(dtoWhere);
	}
	
}
