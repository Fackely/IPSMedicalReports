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
public class TipoProfesionalDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 678308246369476175L;

	private int codigo;
	private String nombreAsocio;

	public TipoProfesionalDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	public TipoProfesionalDto(int codigo, String nombreAsocio) {
		this.codigo = codigo;
		this.nombreAsocio = nombreAsocio;
	}



	/**
	 * @return the nombreAsocio
	 */
	public String getNombreAsocio() {
		return nombreAsocio;
	}



	/**
	 * @param nombreAsocio the nombreAsocio to set
	 */
	public void setNombreAsocio(String nombreAsocio) {
		this.nombreAsocio = nombreAsocio;
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



	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + codigo;
		result = prime * result
				+ ((nombreAsocio == null) ? 0 : nombreAsocio.hashCode());
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
		if (!(obj instanceof TipoProfesionalDto)) {
			return false;
		}
		TipoProfesionalDto other = (TipoProfesionalDto) obj;
		if (codigo != other.codigo) {
			return false;
		}
		if (nombreAsocio == null) {
			if (other.nombreAsocio != null) {
				return false;
			}
		} else if (!nombreAsocio.equals(other.nombreAsocio)) {
			return false;
		}
		return true;
	}



	
	
}
