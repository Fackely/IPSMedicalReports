package com.princetonsa.mundo.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseDetPromocionesOdoDao;
import com.princetonsa.dto.odontologia.DtoDetPromocionOdo;

public class DetPromocionesOdontolgicas {
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static int guardar(DtoDetPromocionOdo dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetPromocionesOdoDao().guardar(dto);
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static int guardarLog(DtoDetPromocionOdo dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetPromocionesOdoDao().guardarLog(dto);
	}
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoDetPromocionOdo> cargar(DtoDetPromocionOdo dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetPromocionesOdoDao().cargar(dto);
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
    
	public static boolean  modificar(DtoDetPromocionOdo dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetPromocionesOdoDao().modificar(dto);
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean  eliminar(DtoDetPromocionOdo dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetPromocionesOdoDao().eliminar(dto);
	}
	
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  static  DtoDetPromocionOdo cargarObjeto( DtoDetPromocionOdo dto){
		   return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetPromocionesOdoDao().cargarObjeto(dto);
	   } 

	

}
