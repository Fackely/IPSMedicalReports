package com.princetonsa.mundo.facturacion;


import java.util.ArrayList;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.facturacion.DtoContactoEmpresa;


public class ContactosEmpresa {

	
	
	/**
	 * 
	 * @param objetoServicioHonorarios
	 * @return
	 */
	public static boolean modificar(DtoContactoEmpresa dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getContactosEmpresaDao().modificar(dto);

	}
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoContactoEmpresa> cargar(	DtoContactoEmpresa dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getContactosEmpresaDao().cargar(dto);
	}
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static boolean eliminar(DtoContactoEmpresa dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getContactosEmpresaDao().eliminar(dto) ;
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static double guardar(DtoContactoEmpresa dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getContactosEmpresaDao().guardar(dto);
	}
	
	
}
