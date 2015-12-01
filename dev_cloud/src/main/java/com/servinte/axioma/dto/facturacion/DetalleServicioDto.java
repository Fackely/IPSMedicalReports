package com.servinte.axioma.dto.facturacion;

import java.io.Serializable;

/**
 * Dto para guardar los datos de la parametrización del propietario del servicio
 * 
 * @author ricruico
 * @version 1.0
 * @created 04-jul-2012 02:24:01 p.m.
 */
public class DetalleServicioDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2153884546155947348L;

	/**
	 * Atributo que representa el código parametrizado para el servicio de acuerdo
	 * al tarifario Oficial
	 */
	private String codigoPropietario;
	
	/**
	 * Atributo que representa la descripción parametrizada para el servicio de acuerdo
	 * al tarifario Oficial
	 */
	private String descripcionPropietario; 
	
	
	public DetalleServicioDto() {
	}
	
	/**
	 * Constructor para mapear la consulta del detalle del servicio
	 * según el tarifario oficial
	 */
	public DetalleServicioDto(String codigoPropietario,
			String descripcionPropietario) {
		this.codigoPropietario = codigoPropietario;
		this.descripcionPropietario = descripcionPropietario;
	}


	/**
	 * @return the codigoPropietario
	 */
	public String getCodigoPropietario() {
		return codigoPropietario;
	}


	/**
	 * @param codigoPropietario the codigoPropietario to set
	 */
	public void setCodigoPropietario(String codigoPropietario) {
		this.codigoPropietario = codigoPropietario;
	}


	/**
	 * @return the descripcionPropietario
	 */
	public String getDescripcionPropietario() {
		return descripcionPropietario;
	}


	/**
	 * @param descripcionPropietario the descripcionPropietario to set
	 */
	public void setDescripcionPropietario(String descripcionPropietario) {
		this.descripcionPropietario = descripcionPropietario;
	}
}
