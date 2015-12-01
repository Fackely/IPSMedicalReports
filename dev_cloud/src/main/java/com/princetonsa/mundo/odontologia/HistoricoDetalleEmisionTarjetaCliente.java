package com.princetonsa.mundo.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.odontologia.DtoHistoricoDetalleEmisionTarjetaCliente;


public class HistoricoDetalleEmisionTarjetaCliente {

	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static boolean eliminar(DtoHistoricoDetalleEmisionTarjetaCliente dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHistoricoDetalleEmisionTarjetaClienteDao().eliminar(dtoWhere);
	}

	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoHistoricoDetalleEmisionTarjetaCliente> cargar(DtoHistoricoDetalleEmisionTarjetaCliente dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHistoricoDetalleEmisionTarjetaClienteDao().cargar(dtoWhere);
	}
		
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static  double guardar(DtoHistoricoDetalleEmisionTarjetaCliente dto) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHistoricoDetalleEmisionTarjetaClienteDao().guardar(dto);
	}

	/**
	 * 
	 * @param dtoNuevo
	 * @param dtoWhere
	 * @return
	 */
	public static boolean modificar(DtoHistoricoDetalleEmisionTarjetaCliente dtoNuevo,DtoHistoricoDetalleEmisionTarjetaCliente dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHistoricoDetalleEmisionTarjetaClienteDao().modificar(dtoNuevo, dtoWhere);
	}
	
	
}
