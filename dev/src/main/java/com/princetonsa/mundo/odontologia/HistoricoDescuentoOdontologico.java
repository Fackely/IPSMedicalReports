package com.princetonsa.mundo.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseHistoricoDescuentoOdontologicoDao;
import com.princetonsa.dto.odontologia.DtoDescuentosOdontologicos;
import com.princetonsa.dto.odontologia.DtoHistoricoDescuentoOdontologicoAtencion;

import com.princetonsa.dto.odontologia.DtoHistoricoDescuentoOdontologico;


public class HistoricoDescuentoOdontologico {

	
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoHistoricoDescuentoOdontologico> cargar(DtoHistoricoDescuentoOdontologico dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHistoricoDescuentoOdontologicoDao().cargar(dtoWhere);
	}
		
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static  double guardar(DtoHistoricoDescuentoOdontologico dto) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHistoricoDescuentoOdontologicoDao().guardar(dto);
	}

	/**
	 * 
	 * @param dtoNuevo
	 * @param dtoWhere
	 * @return
	 */
	public static boolean modificar(DtoHistoricoDescuentoOdontologico dtoNuevo,DtoHistoricoDescuentoOdontologico dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHistoricoDescuentoOdontologicoDao().modificar(dtoNuevo, dtoWhere);
	}
	
	/**
	 * 
	 * 
	 */

	public static  ArrayList<DtoHistoricoDescuentoOdontologicoAtencion> cargarAtencion(
			DtoHistoricoDescuentoOdontologicoAtencion dtoWhere) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHistoricoDescuentoOdontologicoDao().cargarAtencion(dtoWhere);
	}
	
	/**
	 * 
	 * 
	 * @param dto
	 * @return
	 */


	public static  double guardarAtencion(DtoHistoricoDescuentoOdontologicoAtencion dto) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHistoricoDescuentoOdontologicoDao().guardarAtencion(dto);
	}
	
	/**
	 * 
	 * 
	 * @param dtoNuevo
	 * @param dtoWhere
	 * @return
	 */

	
	public static  boolean modificarAtencion(
			DtoHistoricoDescuentoOdontologicoAtencion dtoNuevo,
			DtoHistoricoDescuentoOdontologicoAtencion dtoWhere) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHistoricoDescuentoOdontologicoDao().modificarAtencion(dtoNuevo, dtoWhere);
	}

	
	
}
