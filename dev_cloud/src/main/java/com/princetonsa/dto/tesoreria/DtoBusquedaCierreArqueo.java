package com.princetonsa.dto.tesoreria;

import java.io.Serializable;
import java.util.Date;

import util.ConstantesBD;

/**
 * 
 * 
 * @author Jorge Armando Agudelo Quintero - Cristhian Murillo
 * 
 */
public class DtoBusquedaCierreArqueo implements
		Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	/**
	 * Fecha inicial para realizar la consulta del Arqueo / Cierre
	 */
	private Date fechaInicial;
	

	/**
	 * Fecha final para realizar la consulta del Arqueo / Cierre
	 */
	private Date fechaFinal;
	
	/**
	 * Login del cajero asociado a la búsqueda del Arqueo / Cierre
	 */
	private String cajero;
	
	/**
	 * Consecutivo de la caja asociada a la búsqueda del Arqueo / Cierre
	 */
	private int consecutivoCaja;

	/**
	 * Código del tipo de arqueo asociado a la búsqueda del Arqueo / Cierre
	 */
	private int codigoTipoMovimiento;
	

	/**
	 * Constuctor de la clase
	 */
	public DtoBusquedaCierreArqueo() {

		setFechaInicial(null);
		setFechaFinal(null);
		setCajero("");
		setConsecutivoCaja(ConstantesBD.codigoNuncaValido);
		setCodigoTipoMovimiento(ConstantesBD.codigoNuncaValido);
	}


	/**
	 * @return the fechaInicial
	 */
	public Date getFechaInicial() {
		return fechaInicial;
	}


	/**
	 * @param fechaInicial the fechaInicial to set
	 */
	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}


	/**
	 * @return the fechaFinal
	 */
	public Date getFechaFinal() {
		return fechaFinal;
	}


	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}


	/**
	 * @return the cajero
	 */
	public String getCajero() {
		return cajero;
	}


	/**
	 * @param cajero the cajero to set
	 */
	public void setCajero(String cajero) {
		this.cajero = cajero;
	}


	/**
	 * @return the consecutivoCaja
	 */
	public int getConsecutivoCaja() {
		return consecutivoCaja;
	}


	/**
	 * @param consecutivoCaja the consecutivoCaja to set
	 */
	public void setConsecutivoCaja(int consecutivoCaja) {
		this.consecutivoCaja = consecutivoCaja;
	}


	/**
	 * @return the codigoTipoMovimiento
	 */
	public int getCodigoTipoMovimiento() {
		return codigoTipoMovimiento;
	}


	/**
	 * @param codigoTipoMovimiento the codigoTipoMovimiento to set
	 */
	public void setCodigoTipoMovimiento(int codigoTipoMovimiento) {
		this.codigoTipoMovimiento = codigoTipoMovimiento;
	}

}
