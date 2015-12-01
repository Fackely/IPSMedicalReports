package com.princetonsa.dto.carteraPaciente;

import java.io.Serializable;
import java.math.BigDecimal;
import util.ConstantesBD;
import util.UtilidadFecha;

public class DtoDetApliPagosCarteraPac implements Serializable
{
	private double codigoPk;
	private double apliPagoCarteraPac;
	private double cuotaDatoFinanciacion;
	private String fechaPago;
	private String usuario;
	private String consecutivo;
	private BigDecimal valor;
	
	public void reset()
	{
		this.codigoPk=ConstantesBD.codigoNuncaValido;
		this.apliPagoCarteraPac=ConstantesBD.codigoNuncaValido;
		this.cuotaDatoFinanciacion=ConstantesBD.codigoNuncaValido;
		this.fechaPago=UtilidadFecha.getFechaActual();
		this.usuario= new String("");
		this.consecutivo=ConstantesBD.codigoNuncaValido+"";
		this.valor=new BigDecimal(0);
	}
	
	public DtoDetApliPagosCarteraPac()
	{
		this.reset();
	}
	
	public double getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(double codigoPk) {
		this.codigoPk = codigoPk;
	}

	public double getApliPagoCarteraPac() {
		return apliPagoCarteraPac;
	}

	public void setApliPagoCarteraPac(double apliPagoCarteraPac) {
		this.apliPagoCarteraPac = apliPagoCarteraPac;
	}

	public double getCuotaDatoFinanciacion() {
		return cuotaDatoFinanciacion;
	}

	public void setCuotaDatoFinanciacion(double cuotaDatoFinanciacion) {
		this.cuotaDatoFinanciacion = cuotaDatoFinanciacion;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	/**
	 * @return the fechaPago
	 */
	public String getFechaPago() {
		return fechaPago;
	}

	/**
	 * @param fechaPago the fechaPago to set
	 */
	public void setFechaPago(String fechaPago) {
		this.fechaPago = fechaPago;
	}

	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the consecutivo
	 */
	public String getConsecutivo() {
		return consecutivo;
	}

	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}
	
}