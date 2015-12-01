package com.princetonsa.mundo.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.odontologia.DtoPrograma;

public class Programa {
	public static double guardar(DtoPrograma dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProgramaDao().guardar(dto);
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoPrograma> cargar(DtoPrograma dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProgramaDao().cargar(dto);
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
    
	public static boolean  modificar(DtoPrograma dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProgramaDao().modificar(dto);
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean  eliminar(DtoPrograma dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProgramaDao().eliminar(dto);
	}
	
	/**
	 * 
	 * @param dto
	 * @param b 
	 * @return
	 */
	public static ArrayList<DtoPrograma> cargarAvanzado(DtoPrograma dto, boolean incluirInactivos)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProgramaDao().cargarConsultaAvanzada(dto, incluirInactivos);
	}
	
	/**
	 * 
	 * @param programa
	 * @return
	 */
	public static String obtenerEspeciliadadPrograma(double programa) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProgramaDao().obtenerEspeciliadadPrograma(programa);
	}
	

}
