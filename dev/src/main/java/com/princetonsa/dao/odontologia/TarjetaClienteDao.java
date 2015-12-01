package com.princetonsa.dao.odontologia;

import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoFiltroReporteIngresosTarjetasCliente;
import com.princetonsa.dto.odontologia.DtoTarjetaCliente;
import com.servinte.axioma.dto.odontologia.ventaTarjeta.DtoResultadoReporteVentaTarjetas;

/**
 * 
 * @author axioma
 *
 */
public  interface TarjetaClienteDao  
{
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public double  guardar( DtoTarjetaCliente dto );
	 
	/**
	 *  
	 * @param dto
	 * @return
	 */
	public ArrayList<DtoTarjetaCliente> cargar(	DtoTarjetaCliente dto);
		
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public boolean modificar(DtoTarjetaCliente dto);
		
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public boolean eliminar(DtoTarjetaCliente dto);

	/**
	 * 
	 * @param contrato
	 * @return
	 */
	public int obtenerEsquemaTarifarioTarjetaCliente(double contrato);
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public ArrayList<DtoTarjetaCliente> cargarConvenio(DtoTarjetaCliente dto, boolean modificar);
	
	/**
	 * 
	 * @param codigoPkTarjetaCliente
	 * @return
	 */
	public  int obtenerContratoTarjetaCliente(double codigoPkTarjetaCliente);

	/**
	 * 
	 * @param dtoFiltro
	 * @return
	 */
	public ArrayList<DtoResultadoReporteVentaTarjetas> consultarDatosReporte(
			DtoFiltroReporteIngresosTarjetasCliente dtoFiltro);
	
}