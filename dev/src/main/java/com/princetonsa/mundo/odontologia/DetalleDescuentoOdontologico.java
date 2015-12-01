package com.princetonsa.mundo.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.odontologia.DtoDetalleDescuentoOdontologico;

public class DetalleDescuentoOdontologico {


	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static boolean eliminar(DtoDetalleDescuentoOdontologico dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetalleDescuentoOdontologicoDao().eliminar(dtoWhere);
	}

	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoDetalleDescuentoOdontologico> cargar(DtoDetalleDescuentoOdontologico dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetalleDescuentoOdontologicoDao().cargar(dtoWhere);
	}
		
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static  double guardar(DtoDetalleDescuentoOdontologico dto) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetalleDescuentoOdontologicoDao().guardar(dto);
	}

	/**
	 * 
	 * @param dtoNuevo
	 * @param dtoWhere
	 * @return
	 */
	public static boolean modificar(DtoDetalleDescuentoOdontologico dtoNuevo,DtoDetalleDescuentoOdontologico dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetalleDescuentoOdontologicoDao().modificar(dtoNuevo, dtoWhere);
	}
	
	/**
	 * 
	 * 
	 * 
	 */
	
	
	public static boolean existeRangoPresupuesto(double consecutivo, double centroAtencion, double ValorMinimo, double ValorMaximo ,double codigo){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetalleDescuentoOdontologicoDao().existeRangoPresupuesto(consecutivo, centroAtencion, ValorMinimo, ValorMaximo,codigo);
	}
		

	
}
