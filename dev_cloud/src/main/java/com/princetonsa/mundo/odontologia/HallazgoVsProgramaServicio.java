package com.princetonsa.mundo.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.DaoFactory;

import com.princetonsa.dto.odontologia.DtoHallazgoVsProgramaServicio;

public class HallazgoVsProgramaServicio {

	
	public static boolean eliminar(DtoHallazgoVsProgramaServicio dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHallazgoVsProgramaServicioDao().eliminar(dtoWhere);
	}

	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoHallazgoVsProgramaServicio> cargar(DtoHallazgoVsProgramaServicio dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHallazgoVsProgramaServicioDao().cargar(dtoWhere);
	}
		
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static  double guardar(DtoHallazgoVsProgramaServicio dto) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHallazgoVsProgramaServicioDao().guardar(dto);
	}

	/**
	 * 
	 * @param dtoNuevo
	 * @param dtoWhere
	 * @return
	 */
	public static boolean modificar(DtoHallazgoVsProgramaServicio dtoNuevo,DtoHallazgoVsProgramaServicio dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHallazgoVsProgramaServicioDao().modificar(dtoNuevo, dtoWhere);
	}
	
	
	
	
}
