package com.servinte.axioma.dto.capitacion;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Dto que contiene los valores totales por cada Convenio
 * ademas de los contratos por cada Convenio
 * 
 * 
 * @version 1.0, May 02, 2011
 * @author <a href="mailto:ricardo.ruiz@servinte.com.co">Ricardo Ruiz</a>
 * 
 */
public class DtoConvenioReporte implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1411193876390743383L;
	
	/**
	 *Atributo que representa el nombre del Convenio
	 */
	private String nombre;
	
	/**
	 *Atributo que representa el tipo del Convenio
	 */
	private String tipoConvenio;
	
	/**
	 * Atributo que representa el total presupuestado para el Convenio
	 */
	private double totalPresupuestado;
	
	/**
	 * Atributo que representa el total ordenado para el Convenio
	 */
	private double totalOrdenado;
	
	/**
	 * Atributo que representa el total autorizado para el Convenio
	 */
	private double totalAutorizado;
	
	/**
	 * Atributo que representa el total de los cargos a la cuenta para el Convenio
	 */
	private double totalCargosCuenta;
	
	/**
	 * Atributo que representa el total facturado para el Convenio
	 */
	private double totalFacturado;
	
	/**
	 * Atributo que representa la lista de grupos de servicios o clases de inventarios
	 */
	private ArrayList<DtoContratoReporte> contratos = new ArrayList<DtoContratoReporte>();
	
	
	/**
	 * Constructor de la clase
	 */
	public DtoConvenioReporte(){
		
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
	 * @return the contratos
	 */
	public ArrayList<DtoContratoReporte> getContratos() {
		return contratos;
	}

	/**
	 * @param contratos the contratos to set
	 */
	public void setContratos(ArrayList<DtoContratoReporte> contratos) {
		this.contratos = contratos;
	}

	/**
	 * @return the tipoConvenio
	 */
	public String getTipoConvenio() {
		return tipoConvenio;
	}

	/**
	 * @param tipoConvenio the tipoConvenio to set
	 */
	public void setTipoConvenio(String tipoConvenio) {
		this.tipoConvenio = tipoConvenio;
	}

	
	

}
