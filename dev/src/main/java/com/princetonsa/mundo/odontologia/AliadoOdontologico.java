package com.princetonsa.mundo.odontologia;

import java.util.ArrayList;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.odontologia.DtoAliadoOdontologico;

/**
 * 
 * @author axioma
 *
 */
public class AliadoOdontologico 
{

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static  double guardar(DtoAliadoOdontologico dto) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAliadoOdontologicoDao().guardar(dto);
	}
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static boolean eliminar(DtoAliadoOdontologico dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAliadoOdontologicoDao().eliminar(dtoWhere);
	}
		
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoAliadoOdontologico> cargar(DtoAliadoOdontologico dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAliadoOdontologicoDao().cargar(dtoWhere);
	}

	/**
	 * 
	 * @param dtoNuevo
	 * @param dtoWhere
	 * @return
	 */
	public static boolean modificar(DtoAliadoOdontologico dtoNuevo, DtoAliadoOdontologico dtoWhere)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAliadoOdontologicoDao().modificar(dtoNuevo, dtoWhere);
	}

	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoAliadoOdontologico> cargarDetalle(DtoAliadoOdontologico dtoWhere) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAliadoOdontologicoDao().cargarDetalle(dtoWhere);
	}

}
