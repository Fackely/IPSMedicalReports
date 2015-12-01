/**
 * 
 */
package util.manejoPaciente;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * @author Wilson Rios 
 *
 * May 25, 2010 - 12:10:04 PM
 */
public class InfoIngresoCuenta implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4168054173487272315L;
	
	/**
	 * ingreso
	 */
	private BigDecimal ingreso;
	
	/**
	 * 
	 */
	private BigDecimal cuenta;

	/**
	 * 
	 * Constructor de la clase
	 * @param ingreso
	 * @param cuenta
	 */
	public InfoIngresoCuenta(BigDecimal ingreso, BigDecimal cuenta) {
		super();
		this.ingreso = ingreso;
		this.cuenta = cuenta;
	}

	/**
	 * @return the ingreso
	 */
	public BigDecimal getIngreso() {
		return ingreso;
	}

	/**
	 * @param ingreso the ingreso to set
	 */
	public void setIngreso(BigDecimal ingreso) {
		this.ingreso = ingreso;
	}

	/**
	 * @return the cuenta
	 */
	public BigDecimal getCuenta() {
		return cuenta;
	}

	/**
	 * @param cuenta the cuenta to set
	 */
	public void setCuenta(BigDecimal cuenta) {
		this.cuenta = cuenta;
	}
	
	
}
