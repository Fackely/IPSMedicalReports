package com.servinte.axioma.dto.capitacion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Dto que contiene los valores totales por cada Contrato
 * ademas de los Niveles de atención por cada contrato
 * 
 * 
 * @version 1.0, May 02, 2011
 * @author <a href="mailto:ricardo.ruiz@servinte.com.co">Ricardo Ruiz</a>
 * 
 */
public class DtoContratoReporte implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3904195025677105348L;

		
	/**
	 *Atributo que representa el número del Contrato
	 */
	private String numeroContrato;
	
	
	/**
	 *Atributo que representa el nombre del convenio asociado al contrato
	 */
	private String nombreConvenio;
	
	/**
	 * Atributo que representa el valor mensual del contrato
	 */
	private BigDecimal valorMensual;
	
	/**
	 * Atributo que representa el porcentaje de gasto mensual del contrato
	 */
	private BigDecimal porcentajeGastoMensual;
	
	
	/**
	 *Atributo que representa el valor de gasto mensual del contrato 
	 */
	private BigDecimal valorGastoMensual;	
	
	
	/**
	 * Atributo que representa el total presupuestado para el Contrato
	 */
	private double totalPresupuestado;
	
	/**
	 * Atributo que representa el total ordenado para el Contrato
	 */
	private double totalOrdenado;
	
	/**
	 * Atributo que representa el total autorizado para el Contrato
	 */
	private double totalAutorizado;
	
	/**
	 * Atributo que representa el total de los cargos a la cuenta para el Contrato
	 */
	private double totalCargosCuenta;
	
	/**
	 * Atributo que representa el total facturado para el Contrato
	 */
	private double totalFacturado;
	
	/**
	 * Atributo que representa la lista de Niveles de atención del contrato
	 */
	private ArrayList<DtoNivelReporte> nivelesAtencion = new ArrayList<DtoNivelReporte>();
	
	
	
	/**
	 * Constructor de la clase
	 */
	public DtoContratoReporte(){
		
	}

	

	/**
	 * @return the totalPresupuestado
	 */
	public double getTotalPresupuestado() {
		return totalPresupuestado;
	}

	/**
	 * @param totalPresupuestado the totalPresupuestado to set
	 */
	public void setTotalPresupuestado(double totalPresupuestado) {
		this.totalPresupuestado = totalPresupuestado;
	}

	/**
	 * @return the totalOrdenado
	 */
	public double getTotalOrdenado() {
		return totalOrdenado;
	}

	/**
	 * @param totalOrdenado the totalOrdenado to set
	 */
	public void setTotalOrdenado(double totalOrdenado) {
		this.totalOrdenado = totalOrdenado;
	}

	/**
	 * @return the totalAutorizado
	 */
	public double getTotalAutorizado() {
		return totalAutorizado;
	}

	/**
	 * @param totalAutorizado the totalAutorizado to set
	 */
	public void setTotalAutorizado(double totalAutorizado) {
		this.totalAutorizado = totalAutorizado;
	}

	/**
	 * @return the totalCargosCuenta
	 */
	public double getTotalCargosCuenta() {
		return totalCargosCuenta;
	}

	/**
	 * @param totalCargosCuenta the totalCargosCuenta to set
	 */
	public void setTotalCargosCuenta(double totalCargosCuenta) {
		this.totalCargosCuenta = totalCargosCuenta;
	}

	/**
	 * @return the totalFacturado
	 */
	public double getTotalFacturado() {
		return totalFacturado;
	}

	/**
	 * @param totalFacturado the totalFacturado to set
	 */
	public void setTotalFacturado(double totalFacturado) {
		this.totalFacturado = totalFacturado;
	}



	/**
	 * @return the numeroContrato
	 */
	public String getNumeroContrato() {
		return numeroContrato;
	}



	/**
	 * @param numeroContrato the numeroContrato to set
	 */
	public void setNumeroContrato(String numeroContrato) {
		this.numeroContrato = numeroContrato;
	}






	/**
	 * @return the nivelesAtencion
	 */
	public ArrayList<DtoNivelReporte> getNivelesAtencion() {
		return nivelesAtencion;
	}



	/**
	 * @param nivelesAtencion the nivelesAtencion to set
	 */
	public void setNivelesAtencion(ArrayList<DtoNivelReporte> nivelesAtencion) {
		this.nivelesAtencion = nivelesAtencion;
	}



	/**
	 * @return the nombreConvenio
	 */
	public String getNombreConvenio() {
		return nombreConvenio;
	}



	/**
	 * @param nombreConvenio the nombreConvenio to set
	 */
	public void setNombreConvenio(String nombreConvenio) {
		this.nombreConvenio = nombreConvenio;
	}



	/**
	 * @return the valorMensual
	 */
	public BigDecimal getValorMensual() {
		return valorMensual;
	}



	/**
	 * @param valorMensual the valorMensual to set
	 */
	public void setValorMensual(BigDecimal valorMensual) {
		this.valorMensual = valorMensual;
	}



	/**
	 * @return the porcentajeGastoMensual
	 */
	public BigDecimal getPorcentajeGastoMensual() {
		return porcentajeGastoMensual;
	}



	/**
	 * @param porcentajeGastoMensual the porcentajeGastoMensual to set
	 */
	public void setPorcentajeGastoMensual(BigDecimal porcentajeGastoMensual) {
		this.porcentajeGastoMensual = porcentajeGastoMensual;
	}



	/**
	 * @return the valorGastoMensual
	 */
	public BigDecimal getValorGastoMensual() {
		return valorGastoMensual;
	}



	/**
	 * @param valorGastoMensual the valorGastoMensual to set
	 */
	public void setValorGastoMensual(BigDecimal valorGastoMensual) {
		this.valorGastoMensual = valorGastoMensual;
	}
	
}
