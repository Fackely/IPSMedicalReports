package com.princetonsa.mundo.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.odontologia.DtoDetConvPromocionesOdo;

public class DetConvPromocionesOdo {
	public static double guardar(DtoDetConvPromocionesOdo dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetConvPromocionesOdoDao().guardar(dto);
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoDetConvPromocionesOdo> cargar(DtoDetConvPromocionesOdo dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetConvPromocionesOdoDao().cargar(dto);
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
    
	public static boolean  modificar(DtoDetConvPromocionesOdo dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetConvPromocionesOdoDao().modificar(dto);
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean  eliminar(DtoDetConvPromocionesOdo dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetConvPromocionesOdoDao().eliminar(dto);
	}



}
