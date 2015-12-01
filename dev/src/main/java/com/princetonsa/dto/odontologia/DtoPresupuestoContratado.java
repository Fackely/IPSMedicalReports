/**
 * 
 */
package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * @author Wilson Rios 
 *
 * Jun 5, 2010 - 5:12:12 PM
 */
public class DtoPresupuestoContratado implements Serializable 
{
	private static final long serialVersionUID = 4188623981848647381L;

	/**
	 * Atributo que almacena el c&oacute;digo del presupuesto
	 * contratado.
	 */
	private BigDecimal codigoPkPresupuesto;
	
	/**
	 * Atributo que almacena el pie de p&aacute;gina del presupuesto
	 * contratado.
	 */
	private String piePaginaPresupuesto;
	
	/**
	 * Atributo que almacena el cosecutivo del presupuesto contratado.
	 */
	private BigDecimal consecutivo;
	
	/**
	 * Atributo que almacena el c&oacute;digo del presupuesto
	 * contratado.c
	 */
	private Long codigoPresupuesto;
	
	/**
	 * Atributo que almacena el consecutivo
	 * del presupuesto contratado
	 */
	private Long consecutivoContrato;
	
	/**
	 * Constructor de la clase
	 * @param codigoPkPresupuesto
	 * @param piePaginaPresupuesto
	 * @param consecutivo
	 */
	public DtoPresupuestoContratado() 
	{
		this.codigoPkPresupuesto = BigDecimal.ZERO;
		this.piePaginaPresupuesto = "";
		this.consecutivo = BigDecimal.ZERO;
	}

	/**
	 * @return the codigoPkPresupuesto
	 */
	public BigDecimal getCodigoPkPresupuesto() {
		return codigoPkPresupuesto;
	}

	/**
	 * @param codigoPkPresupuesto the codigoPkPresupuesto to set
	 */
	public void setCodigoPkPresupuesto(BigDecimal codigoPkPresupuesto) {
		this.codigoPkPresupuesto = codigoPkPresupuesto;
	}

	/**
	 * @return the piePaginaPresupuesto
	 */
	public String getPiePaginaPresupuesto() {
		return piePaginaPresupuesto;
	}

	/**
	 * @param piePaginaPresupuesto the piePaginaPresupuesto to set
	 */
	public void setPiePaginaPresupuesto(String piePaginaPresupuesto) {
		this.piePaginaPresupuesto = piePaginaPresupuesto;
	}

	/**
	 * @return the consecutivo
	 */
	public BigDecimal getConsecutivo() {
		return consecutivo;
	}

	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(BigDecimal consecutivo) {
		this.consecutivo = consecutivo;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo codigoPresupuesto
	 * 
	 * @return  Retorna la variable codigoPresupuesto
	 */
	public Long getCodigoPresupuesto() {
		return codigoPresupuesto;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo codigoPresupuesto
	 * 
	 * @param  valor para el atributo codigoPresupuesto 
	 */
	public void setCodigoPresupuesto(Long codigoPresupuesto) {
		this.codigoPresupuesto = codigoPresupuesto;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo consecutivoContrato
	 * 
	 * @return  Retorna la variable consecutivoContrato
	 */
	public Long getConsecutivoContrato() {
		return consecutivoContrato;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo consecutivoContrato
	 * 
	 * @param  valor para el atributo consecutivoContrato 
	 */
	public void setConsecutivoContrato(Long consecutivoContrato) {
		this.consecutivoContrato = consecutivoContrato;
	}
	
}
