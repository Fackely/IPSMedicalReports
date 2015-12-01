package com.princetonsa.mundo.odontologia;

import java.sql.Connection;
import java.util.ArrayList;

import util.InfoDatosStr;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseVentaTarjetaCliente;
import com.princetonsa.dto.odontologia.DtoVentaTarjetasCliente;
/**
 * 
 * @author axioma
 *
 */
public class VentasTarjetasCliente {
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static double guardar(Connection con, DtoVentaTarjetasCliente dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getVentasTarjetasCliente().guardar(con, dto);
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoVentaTarjetasCliente>  cargar(DtoVentaTarjetasCliente dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getVentasTarjetasCliente().cargar(dto);
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean  modificar(DtoVentaTarjetasCliente dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getVentasTarjetasCliente().modificar(dto);
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean  eliminar(DtoVentaTarjetasCliente dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getVentasTarjetasCliente().eliminar(dto);
	}
	/**
	 * 
	 * 
	 * 
	 */
	public static  boolean existeRangoEnVenta(double rangoInicial , double rangoFinal , int institucion, String aliado ){
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getVentasTarjetasCliente().existeRangoEnVenta(rangoInicial, rangoFinal, institucion, aliado);
	}
	
	
	/***
	 * 
	 * 
	 * @param serial
	 * @param institucion
	 * @return
	 */
	public static InfoDatosStr retornarVenta(String serial, int institucion, double codigoTipoTarjeta, double codigoBeneficiario ) {
		
		return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getVentasTarjetasCliente().retornarVenta(serial, institucion, codigoTipoTarjeta, codigoBeneficiario);
	}
}
