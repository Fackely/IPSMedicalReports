/**
 * 
 */
package com.servinte.axioma.dto.salascirugia;

import java.io.Serializable;

/**
 * @author jeilones
 * @created 17/06/2013
 *
 */
public class CampoNotaRecuperacionDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2701789367929156098L;

	private String nombre;
	private String valor;
	private int codigo;
	private int codigoRelacion;
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String descripcion) {
		this.nombre = descripcion;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public int getCodigoRelacion() {
		return codigoRelacion;
	}
	public void setCodigoRelacion(int codigoRelacion) {
		this.codigoRelacion = codigoRelacion;
	}

}
