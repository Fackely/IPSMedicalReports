package com.princetonsa.mundo.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseDescuentoOdontologicoDao;
import com.princetonsa.dto.odontologia.DtoDescuentoOdontologicoAtencion;
import com.princetonsa.dto.odontologia.DtoDescuentosOdontologicos;


public class DescuentoOdontologico {

	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static boolean eliminar(DtoDescuentosOdontologicos dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDescuentoOdontologicoDao().eliminar(dtoWhere);
	}

	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoDescuentosOdontologicos> cargar(DtoDescuentosOdontologicos dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDescuentoOdontologicoDao().cargar(dtoWhere);
	}
		
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static  double guardar(DtoDescuentosOdontologicos dto) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDescuentoOdontologicoDao().guardar(dto);
	}

	/**
	 * 
	 * @param dtoNuevo
	 * @param dtoWhere
	 * @return
	 */
	public static boolean modificar(DtoDescuentosOdontologicos dtoNuevo,DtoDescuentosOdontologicos dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDescuentoOdontologicoDao().modificar(dtoNuevo, dtoWhere);
	}
	/***
	 * 
	 * 
	 * 
	 * 
	 */

	public static boolean existeCruceFechas(DtoDescuentosOdontologicos dto,
			double codigoPkNotIn, int centroAtencion) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDescuentoOdontologicoDao().existeCruceFechas(dto, codigoPkNotIn, centroAtencion);
	}
	
	
	public static  ArrayList<DtoDescuentoOdontologicoAtencion> cargarAtencion(
			DtoDescuentoOdontologicoAtencion dtoWhere) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDescuentoOdontologicoDao().cargarAtencion(dtoWhere);
	}

	
	public static double guardarAtencion(DtoDescuentoOdontologicoAtencion dto) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDescuentoOdontologicoDao().guardarAtencion(dto);
	}

	
	public static  boolean modificarAtencion(DtoDescuentoOdontologicoAtencion dtoNuevo,
			DtoDescuentoOdontologicoAtencion dtoWhere) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDescuentoOdontologicoDao().modificarAtencion(dtoNuevo, dtoWhere);
	}
	
	/**
	 * 
	 * 
	 */
	
	public static  boolean eliminarAtencion(DtoDescuentoOdontologicoAtencion dtoWhere) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDescuentoOdontologicoDao().eliminarAtencion(dtoWhere);
	}
	
	
}
