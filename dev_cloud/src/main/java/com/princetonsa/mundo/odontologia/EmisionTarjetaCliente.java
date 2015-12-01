package com.princetonsa.mundo.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.odontologia.DtoEmisionTarjetaCliente;
import com.princetonsa.dto.odontologia.DtoTarjetaCliente;

public class EmisionTarjetaCliente {

	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static boolean eliminar(DtoEmisionTarjetaCliente dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEmisionTarjetaClienteDao().eliminar(dtoWhere);
	}

	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoEmisionTarjetaCliente> cargar(DtoEmisionTarjetaCliente dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEmisionTarjetaClienteDao().cargar(dtoWhere);
	}
		
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static  double guardar(DtoEmisionTarjetaCliente dto) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEmisionTarjetaClienteDao().guardar(dto);
	}

	/**
	 * 
	 * @param dtoNuevo
	 * @param dtoWhere
	 * @return
	 */
	public static boolean modificar(DtoEmisionTarjetaCliente dtoNuevo,DtoEmisionTarjetaCliente dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEmisionTarjetaClienteDao().modificar(dtoNuevo, dtoWhere);
	}
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param codigo
	 * @param convenio
	 * @param ValorMinimo
	 * @param ValorMaximo
	 * @return
	 */
	public  static boolean existeRangoSeriales(double codigo, String convenio, double ValorMinimo, double ValorMaximo){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEmisionTarjetaClienteDao().existeRangoSeriales(codigo, convenio, ValorMinimo, ValorMaximo);
	}
	
	/**
	 * 
	 * @param codigoPKTipoTarjeta
	 * @param centroAtencion
	 * @param rangoInicial
	 * @param rangoFinal
	 * @return
	 */
	public static boolean existeSerialEnEmisionTarjetaDetalle(double codigoPKTipoTarjeta, int centroAtencion, double rangoInicialSerial, double rangoFinalSerial, int codigoInstitucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEmisionTarjetaClienteDao().existeSerialEnEmisionTarjetaDetalle(codigoPKTipoTarjeta, centroAtencion, rangoInicialSerial, rangoFinalSerial, codigoInstitucion);
	}
	
	
	
	/**
	 * Metodo que recibe un Objeto de Tipo Tarjeta Cliente  y sirve para validar si un tarjeta esta registrada Emision de Seriales
	 * @author Edgar Carvajal Ruiz
	 * @param dto
	 * @return
	 */
	public  static boolean  existeTarjetaRegistrada(DtoTarjetaCliente dto) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEmisionTarjetaClienteDao().existeTarjetaRegistrada(dto);
	
	}
	
	
}