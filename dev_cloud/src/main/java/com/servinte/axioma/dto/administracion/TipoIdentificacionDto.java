/**
 * 
 */
package com.servinte.axioma.dto.administracion;

/**
 * @author jeilones
 * @created 8/10/2012
 *
 */
public class TipoIdentificacionDto {

	private String acronimo;
	private String nombre;
	private String codInterfaz;
	private String tipo;
	private Character soloNumeros;
	/**
	 * 
	 * @author jeilones
	 * @created 8/10/2012
	 */
	public TipoIdentificacionDto() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param acronimo
	 * @param nombre
	 * @param codInterfaz
	 * @param tipo
	 * @param soloNumeros
	 * @author jeilones
	 * @created 8/10/2012
	 */
	public TipoIdentificacionDto(String acronimo, String nombre,
			String codInterfaz, String tipo, Character soloNumeros) {
		super();
		this.acronimo = acronimo;
		this.nombre = nombre;
		this.codInterfaz = codInterfaz;
		this.tipo = tipo;
		this.soloNumeros = soloNumeros;
	}

	/**
	 * @return the acronimo
	 */
	public String getAcronimo() {
		return acronimo;
	}
	/**
	 * @param acronimo the acronimo to set
	 */
	public void setAcronimo(String acronimo) {
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
	/**
	 * @return the codInterfaz
	 */
	public String getCodInterfaz() {
		return codInterfaz;
	}
	/**
	 * @param codInterfaz the codInterfaz to set
	 */
	public void setCodInterfaz(String codInterfaz) {
		this.codInterfaz = codInterfaz;
	}
	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}
	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	/**
	 * @return the soloNumeros
	 */
	public Character getSoloNumeros() {
		return soloNumeros;
	}
	/**
	 * @param soloNumeros the soloNumeros to set
	 */
	public void setSoloNumeros(Character soloNumeros) {
		this.soloNumeros = soloNumeros;
	}

}
