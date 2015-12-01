package com.princetonsa.mundo.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.odontologia.DtoHistoricoEmisionTarjetaCliente;

public class HistoricoEmisionTarjetaCliente {


	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static boolean eliminar(DtoHistoricoEmisionTarjetaCliente dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHistoricoEmisionTarjetaClienteDao().eliminar(dtoWhere);
	}

	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoHistoricoEmisionTarjetaCliente> cargar(DtoHistoricoEmisionTarjetaCliente dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHistoricoEmisionTarjetaClienteDao().cargar(dtoWhere);
	}
		
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static  double guardar(DtoHistoricoEmisionTarjetaCliente dto) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHistoricoEmisionTarjetaClienteDao().guardar(dto);
	}

	/**
	 * 
	 * @param dtoNuevo
	 * @param dtoWhere
	 * @return
	 */
	public static boolean modificar(DtoHistoricoEmisionTarjetaCliente dtoNuevo,DtoHistoricoEmisionTarjetaCliente dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHistoricoEmisionTarjetaClienteDao().modificar(dtoNuevo, dtoWhere);
	}
	
	
}
