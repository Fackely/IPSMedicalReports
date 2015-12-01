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
public class EspecialidadDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8658784376893200932L;

	private int codigo;
	private String nombre;
	
	private boolean esConfirmado;
	
	public EspecialidadDto() {
	}
	public EspecialidadDto(int codigo, String nombre) {
		this.codigo = codigo;
		this.nombre = nombre;
	}
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
	
	/**
	 * @return the esConfirmado
	 */
	public boolean isEsConfirmado() {
		return esConfirmado;
	}
	/**
	 * @param esConfirmado the esConfirmado to set
	 */
	public void setEsConfirmado(boolean esConfirmado) {
		this.esConfirmado = esConfirmado;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + codigo;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof EspecialidadDto)) {
			return false;
		}
		EspecialidadDto other = (EspecialidadDto) obj;
		if (codigo != other.codigo) {
			return false;
		}
		if (nombre == null) {
			if (other.nombre != null) {
				return false;
			}
		} else if (!nombre.equals(other.nombre)) {
			return false;
		}
		return true;
	}

	
}
