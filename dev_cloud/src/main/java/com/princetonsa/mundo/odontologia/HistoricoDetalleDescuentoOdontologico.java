package com.princetonsa.mundo.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.DaoFactory;

import com.princetonsa.dto.odontologia.DtoHistoricoDetalleDescuentoOdontologico;

public class HistoricoDetalleDescuentoOdontologico {

	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoHistoricoDetalleDescuentoOdontologico> cargar(DtoHistoricoDetalleDescuentoOdontologico dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHistoricoDetalleDescuentoOdontologicoDao().cargar(dtoWhere);
	}
		
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static  double guardar(DtoHistoricoDetalleDescuentoOdontologico dto) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHistoricoDetalleDescuentoOdontologicoDao().guardar(dto);
	}

	/**
	 * 
	 * @param dtoNuevo
	 * @param dtoWhere
	 * @return
	 */
	public static boolean modificar(DtoHistoricoDetalleDescuentoOdontologico dtoNuevo,DtoHistoricoDetalleDescuentoOdontologico dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHistoricoDetalleDescuentoOdontologicoDao().modificar(dtoNuevo, dtoWhere);
	}
	
	
}
