/**
 * 
 */
package com.princetonsa.dto.facturacion;

/**
 * @author armando
 *
 */
public class DtoEsquemasTarifarios 
{

	/**
	 * 
	 */
	private int codigo;
	
	/**
	 * 
	 */
	private String nombre;
	
	/**
	 * 
	 */
	private int tarifarioOfocial;
	
	/**
	 * 
	 */
	private String metodoAjuste;
	
	/**
	 * 
	 */
	private boolean esInventario;
	
	/**
	 * 
	 */
	private int institucion;
	
	/**
	 * 
	 */
	private int cantidad;

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getTarifarioOfocial() {
		return tarifarioOfocial;
	}

	public void setTarifarioOfocial(int tarifarioOfocial) {
		this.tarifarioOfocial = tarifarioOfocial;
	}

	public String getMetodoAjuste() {
		return metodoAjuste;
	}

	public void setMetodoAjuste(String metodoAjuste) {
		this.metodoAjuste = metodoAjuste;
	}

	public boolean isEsInventario() {
		return esInventario;
	}

	public void setEsInventario(boolean esInventario) {
		this.esInventario = esInventario;
	}

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

}
