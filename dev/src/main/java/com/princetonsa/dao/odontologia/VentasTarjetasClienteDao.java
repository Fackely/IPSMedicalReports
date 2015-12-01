package com.princetonsa.dao.odontologia;

import java.sql.Connection;
import java.util.ArrayList;

import util.InfoDatosStr;


import com.princetonsa.dto.odontologia.DtoVentaTarjetasCliente;

public interface VentasTarjetasClienteDao {

	/**
	 * @param con 
	 * 
	 */
	public  double  guardar( Connection con, DtoVentaTarjetasCliente dto );
	 
	/**
	 *  
	 * @param dto
	 * @return
	 */
	public ArrayList<DtoVentaTarjetasCliente> cargar(	DtoVentaTarjetasCliente dto);
		
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public boolean modificar(DtoVentaTarjetasCliente dto);
		
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public boolean eliminar(DtoVentaTarjetasCliente dto);

	
	/**
	 * 
	 * @param rangoInicial
	 * @param rangoFinal
	 * @param institucion
	 * @param aliado
	 * @return
	 */
	public boolean  existeRangoEnVenta(double rangoInicial , double rangoFinal , int institucion, String aliado);
	
	/**
	 * 
	 * @param serial
	 * @param institucion
	 * @param codigoTipoTarjeta
	 * @return
	 */
	public  InfoDatosStr retornarVenta(String serial, int institucion,double  codigoTipoTarjeta,  double codigoBeneficiario);
	
	
}
