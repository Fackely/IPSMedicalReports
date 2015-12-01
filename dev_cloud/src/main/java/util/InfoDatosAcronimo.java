package util;

import java.io.Serializable;

/**
 * 
 * @author axioma
 *
 */
public class InfoDatosAcronimo implements Serializable
{
	/**
	 * 
	 */
	private String acronimo;
	
	/**
	 * 
	 */
	private String decripcion;

	/**
	 * constructor
	 * @param acronimo
	 * @param decripcion
	 */
	public InfoDatosAcronimo(String acronimo, String decripcion) {
		super();
		this.acronimo = acronimo;
		this.decripcion = decripcion;
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
	 * @return the decripcion
	 */
	public String getDecripcion() {
		return decripcion;
	}

	/**
	 * @param decripcion the decripcion to set
	 */
	public void setDecripcion(String decripcion) {
		this.decripcion = decripcion;
	}
	
	
}
