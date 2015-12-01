package com.princetonsa.dao.odontologia;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoDetalleEmisionesTarjetaCliente;


public interface DetalleEmisionTarjetaClienteDao {

	
	
	public boolean modificar(DtoDetalleEmisionesTarjetaCliente dtoNuevo, DtoDetalleEmisionesTarjetaCliente dtoWhere);
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public ArrayList<DtoDetalleEmisionesTarjetaCliente> cargar(DtoDetalleEmisionesTarjetaCliente dtoWhere);
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public boolean eliminar(DtoDetalleEmisionesTarjetaCliente dtoWhere);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public double guardar(DtoDetalleEmisionesTarjetaCliente dto) ;
	
	/**
	 * 
	 * @param codigo
	 * @param institucion
	 * @param ValorMinimo
	 * @param ValorMaximo
	 * @return
	 */
	public boolean existeRangoSeriales(double codigo, int institucion, double ValorMinimo, double ValorMaximo);
	
	

	/**
	 * 
	 * @param institucion
	 * @param encabezado
	 * @param ValorMinimo
	 * @param ValorMaximo
	 * @return
	 */
	public  boolean fueraRango(int  institucion, double encabezado, double ValorMinimo, double ValorMaximo);
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  BigDecimal cargarSerialMayor(DtoDetalleEmisionesTarjetaCliente dto);
	
	
	
}


