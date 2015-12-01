/**
 * 
 */
package util;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * @author Wilson Rios 
 *
 * Jun 6, 2010 - 1:23:30 PM
 */
public class InfoDatosBigDecimal implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4227352432130188915L;

	/**
	 * codigo
	 */
	private BigDecimal codigo;
	
	/**
	 * 
	 */
	private String nombre;
	
	/**
	 * Constructor de la clase
	 * @param codigo
	 * @param nombre
	 * @param cantidad
	 */
	public InfoDatosBigDecimal() {
		this.codigo = BigDecimal.ZERO;
		this.nombre = "";
	}

	/**
	 * @return the codigo
	 */
	public BigDecimal getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(BigDecimal codigo) {
		this.codigo = codigo;
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
