package com.princetonsa.mundo.odontologia;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.odontologia.DtoDetalleEmisionesTarjetaCliente;


public class DetalleEmisionTarjetaCliente {

	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static boolean eliminar(DtoDetalleEmisionesTarjetaCliente dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetalleEmisionTarjetaClienteDao().eliminar(dtoWhere);
	}

	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoDetalleEmisionesTarjetaCliente> cargar(DtoDetalleEmisionesTarjetaCliente dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetalleEmisionTarjetaClienteDao().cargar(dtoWhere);
	}
		
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static  double guardar(DtoDetalleEmisionesTarjetaCliente dto) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetalleEmisionTarjetaClienteDao().guardar(dto);
	}

	/**
	 * 
	 * @param dtoNuevo
	 * @param dtoWhere
	 * @return
	 */
	public static boolean modificar(DtoDetalleEmisionesTarjetaCliente dtoNuevo,DtoDetalleEmisionesTarjetaCliente dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetalleEmisionTarjetaClienteDao().modificar(dtoNuevo, dtoWhere);
	}
	
	/**
	 * 
	 * @param codigo
	 * @param institucion
	 * @param ValorMinimo
	 * @param ValorMaximo
	 * @return
	 */
	public static boolean existeRangoSeriales(double codigo, int institucion, double ValorMinimo, double ValorMaximo){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetalleEmisionTarjetaClienteDao().existeRangoSeriales(codigo, institucion, ValorMinimo, ValorMaximo);
	}
	
	
	/**
	 * 
	 * @param institucion
	 * @param encabezado
	 * @param ValorMinimo
	 * @param ValorMaximo
	 * @return
	 */
	public static boolean fueraRango(int institucion, double encabezado,
			double ValorMinimo, double ValorMaximo){
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetalleEmisionTarjetaClienteDao().fueraRango(institucion, encabezado, ValorMinimo, ValorMaximo);
	}
	
	
	/**
	 * CARGAR EL SERIAL MAXIMO  
	 * @param dto
	 * @return
	 */
	public static BigDecimal cargarSerialMayor(DtoDetalleEmisionesTarjetaCliente dto) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetalleEmisionTarjetaClienteDao().cargarSerialMayor(dto);
	}
	
	
}
