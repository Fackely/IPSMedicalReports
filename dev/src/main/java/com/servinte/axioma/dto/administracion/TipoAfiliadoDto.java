/**
 * 
 */
package com.servinte.axioma.dto.administracion;

/**
 * @author jeilones
 * @created 8/10/2012
 *
 */
public class TipoAfiliadoDto {

	private char acronimo;
	private String nombre;
	
	/**
	 * 
	 * @author jeilones
	 * @created 8/10/2012
	 */
	public TipoAfiliadoDto() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param acronimo
	 * @param nombre
	 * @author jeilones
	 * @created 8/10/2012
	 */
	public TipoAfiliadoDto(char acronimo, String nombre) {
		super();
		this.acronimo = acronimo;
		this.nombre = nombre;
	}

	/**
	 * @return the acronimo
	 */
	public char getAcronimo() {
		return acronimo;
	}

	/**
	 * @param acronimo the acronimo to set
	 */
	public void setAcronimo(char acronimo) {
		this.acronimo = acronimo;
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

}
