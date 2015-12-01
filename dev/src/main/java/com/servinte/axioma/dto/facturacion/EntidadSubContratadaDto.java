package com.servinte.axioma.dto.facturacion;

import java.io.Serializable;

/**
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:24:00 p.m.
 */
public class EntidadSubContratadaDto implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6712424354653037411L;

	/**Atributo para almacenar el codigo de la entidad subcontratada*/
	private long codEntidadSubcontratada;
	
	/**Atributo para almacenar el codigo del contrato asociado a la entidad subcontratada */
	private long codContratoEntidadSub;	
	
	/**Atributo para almacenar el tipo de tarifa de la entidad subcontratada */
	private String tipoTarifa;
	
	/**Atributo que almacena la descripción de la entidad subcontratada a autorizar */
	private String razonSocial;
	
	/**Atributo que almacvena la direccion de la entidad subcontratada a autorizar */
	private String direccionEntidad;
	
	/**Atributo que almacena el telefono de la entidad subcontratada a autorizar */
	private String telefonoEntidad;
	
	/**
	 * Atributo que representa el número de proridad parametrizado para la entidad
	 */
	private Integer numeroPrioridad;
	/**
	 * Constructor de la clase 
	 */
	public EntidadSubContratadaDto(){
	
	}
	
	
	/**
	 * Constructor de la clase para mapear la consulta de entidades subcontratadas por centro de costo
	 * por prioridad
	 */
	public EntidadSubContratadaDto(Long codigoPk, String razonSocial, String direccion, String telefono, 
										Integer numeroPrioridad, Long codigoContrato, String tipoTarifa){
		this.codEntidadSubcontratada=codigoPk;
		this.razonSocial=razonSocial;
		this.direccionEntidad=direccion;
		this.telefonoEntidad=telefono;
		this.numeroPrioridad=numeroPrioridad;
		this.codContratoEntidadSub=codigoContrato;
		this.tipoTarifa=tipoTarifa;
	}
	
	/**
	 * @return codEntidadSubcontratada
	 */
	public long getCodEntidadSubcontratada() {
		return codEntidadSubcontratada;
	}
	
	/**
	 * @param codEntidadSubcontratada
	 */
	public void setCodEntidadSubcontratada(long codEntidadSubcontratada) {
		this.codEntidadSubcontratada = codEntidadSubcontratada;
	}
	
	/**
	 * @return codContratoEntidadSub
	 */
	public long getCodContratoEntidadSub() {
		return codContratoEntidadSub;
	}
	
	/**
	 * @param codContratoEntidadSub
	 */
	public void setCodContratoEntidadSub(long codContratoEntidadSub) {
		this.codContratoEntidadSub = codContratoEntidadSub;
	}
	
	/**
	 * @return tipoTarifa
	 */
	public String getTipoTarifa() {
		return tipoTarifa;
	}
	
	/**
	 * @param tipoTarifa
	 */
	public void setTipoTarifa(String tipoTarifa) {
		this.tipoTarifa = tipoTarifa;
	}
	
	/**
	 * @return razonSocial
	 */
	public String getRazonSocial() {
		return razonSocial;
	}
	
	/**
	 * @param razonSocial
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	
	/**
	 * @return direccionEntidadSubcontratada
	 */
	public String getDireccionEntidad() {
		return direccionEntidad;
	}
	
	/**
	 * @param direccionEntidad
	 */
	public void setDireccionEntidad(String direccionEntidad) {
		this.direccionEntidad = direccionEntidad;
	}
	
	/**
	 * @return telefonoEntidad
	 */
	public String getTelefonoEntidad() {
		return telefonoEntidad;
	}
	
	/**
	 * @param telefonoEntidad
	 */
	public void setTelefonoEntidad(String telefonoEntidad) {
		this.telefonoEntidad = telefonoEntidad;
	}


	/**
	 * @return the numeroPrioridad
	 */
	public Integer getNumeroPrioridad() {
		return numeroPrioridad;
	}


	/**
	 * @param numeroPrioridad the numeroPrioridad to set
	 */
	public void setNumeroPrioridad(Integer numeroPrioridad) {
		this.numeroPrioridad = numeroPrioridad;
	}


}