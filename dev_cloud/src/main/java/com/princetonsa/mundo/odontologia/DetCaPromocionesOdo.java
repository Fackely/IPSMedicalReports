package com.princetonsa.mundo.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.odontologia.DtoDetCaPromocionesOdo;

public class DetCaPromocionesOdo {
	public static double guardar(DtoDetCaPromocionesOdo dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetCaPromocionesOdoDao().guardar(dto);
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoDetCaPromocionesOdo> cargar(DtoDetCaPromocionesOdo dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetCaPromocionesOdoDao().cargar(dto);
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
    
	public static boolean  modificar(DtoDetCaPromocionesOdo dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetCaPromocionesOdoDao().modificar(dto);
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean  eliminar(DtoDetCaPromocionesOdo dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetCaPromocionesOdoDao().eliminar(dto);
	}
	
	
	/**
	 * CARGAR CENTRO DE ATENCION QUE ES NULL
	 * SIGNIFICA QUE LA PROMOCION APLICA PARA TODOS LOS CENTROS DE ATENCION
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoDetCaPromocionesOdo> cargarCentroAtencionNUll(DtoDetCaPromocionesOdo dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetCaPromocionesOdoDao().cargarCentroAtencionNUll(dto);
	}
	


}
