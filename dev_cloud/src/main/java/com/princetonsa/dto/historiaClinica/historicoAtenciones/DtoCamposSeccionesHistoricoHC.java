/**
 * 
 */
package com.princetonsa.dto.historiaClinica.historicoAtenciones;

import java.io.Serializable;


/**
 * @author axioma
 *
 */
public class DtoCamposSeccionesHistoricoHC implements Serializable
{

	/**
	 * 
	 */
	private int codigo;
	
	/**
	 * 
	 */
	private int codigoSeccion;
	
	/**
	 * 
	 */
	private String nombre;
	
	/**
	 * 
	 */
	private String activo;
	
	/**
	 * 
	 */
	private String fechaAtencion;
	
	/**
	 * 
	 */
	private String valor;
	
	/**
	 * 
	 */
	private int colspan;
	
	/**
	 * @return the colspan
	 */
	public int getColspan() {
		return colspan;
	}
	/**
	 * @param colspan the colspan to set
	 */
	public void setColspan(int colspan) {
		this.colspan = colspan;
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
	 * @return the codigoSeccion
	 */
	public int getCodigoSeccion() {
		return codigoSeccion;
	}
	/**
	 * @param codigoSeccion the codigoSeccion to set
	 */
	public void setCodigoSeccion(int codigoSeccion) {
		this.codigoSeccion = codigoSeccion;
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
	 * @return the activo
	 */
	public String getActivo() {
		return activo;
	}
	/**
	 * @param activo the activo to set
	 */
	public void setActivo(String activo) {
		this.activo = activo;
	}
	/**
	 * @return the fechaAtencion
	 */
	public String getFechaAtencion() {
		return fechaAtencion;
	}
	/**
	 * @param fechaAtencion the fechaAtencion to set
	 */
	public void setFechaAtencion(String fechaAtencion) {
		this.fechaAtencion = fechaAtencion;
	}
	/**
	 * @return the valor
	 */
	public String getValor() {
		return valor;
	}
	/**
	 * @param valor the valor to set
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	
	
}
