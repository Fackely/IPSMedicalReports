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
public class TipoHeridaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7259426617221668318L;

	private String acronimo;
	private String nombre;
	
	public TipoHeridaDto() 
	{
		
	}
	
	public TipoHeridaDto(String acronimo, String nombre) 
	{
		this.acronimo = acronimo;
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getAcronimo() {
		return acronimo;
	}
	public void setAcronimo(String acronimo) {
		this.acronimo = acronimo;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((acronimo == null) ? 0 : acronimo.hashCode());
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
		if (!(obj instanceof TipoHeridaDto)) {
			return false;
		}
		TipoHeridaDto other = (TipoHeridaDto) obj;
		if (acronimo == null) {
			if (other.acronimo != null) {
				return false;
			}
		} else if (!acronimo.equals(other.acronimo)) {
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
