package com.servinte.axioma.dto.facturacion;

import java.io.Serializable;

/**
 * Dto para mapear datos de los convenios
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:24:00 p.m.
 */
public class ConvenioDto implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7739984841238089556L;
	
	/**
	 * Atributo que representa el códigoPk del convenio
	 */
	private int codigo;
	
	/**
	 * Atributo que representa el nombre del convenio
	 */
	private String nombre;
	
	/**
	 * Atributo que representa el codigoPk del tipo de contrato del convenio
	 */
	private int codigoTipoContrato;
	
	/**
	 * Atributo que representa el nombre del tipo de contrato del convenio
	 */
	private String nombreTipoContrato;
	
	/**
	 * Atributo que representa si el convenio maneja o no capitación subcontratada
	 */
	private boolean convenioManejaCapiSub;
	
	/**
	 * Atributo que indica si el convenio maneja presupuesto 
	 */
	private boolean convenioManejaPresupuesto;

	public ConvenioDto(){

	}
	
	/**
	 * Constructor para mapear los datos de la consulta de convenios de ConvenioContratoDelegate
	 * 
	 * @param codigo
	 * @param nombre
	 */
	public ConvenioDto(int codigo, String nombre){
		this.codigo=codigo;
		this.nombre=nombre;
	}

	/**
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the codigoTipoContrato
	 */
	public int getCodigoTipoContrato() {
		return codigoTipoContrato;
	}

	/**
	 * @param codigoTipoContrato the codigoTipoContrato to set
	 */
	public void setCodigoTipoContrato(int codigoTipoContrato) {
		this.codigoTipoContrato = codigoTipoContrato;
	}

	/**
	 * @return the nombreTipoContrato
	 */
	public String getNombreTipoContrato() {
		return nombreTipoContrato;
	}

	/**
	 * @param nombreTipoContrato the nombreTipoContrato to set
	 */
	public void setNombreTipoContrato(String nombreTipoContrato) {
		this.nombreTipoContrato = nombreTipoContrato;
	}

	/**
	 * @return the convenioManejaCapiSub
	 */
	public boolean isConvenioManejaCapiSub() {
		return convenioManejaCapiSub;
	}

	/**
	 * @param convenioManejaCapiSub the convenioManejaCapiSub to set
	 */
	public void setConvenioManejaCapiSub(boolean convenioManejaCapiSub) {
		this.convenioManejaCapiSub = convenioManejaCapiSub;
	}

	public boolean isConvenioManejaPresupuesto() {
		return convenioManejaPresupuesto;
	}

	public void setConvenioManejaPresupuesto(boolean convenioManejaPresupuesto) {
		this.convenioManejaPresupuesto = convenioManejaPresupuesto;
	}

}