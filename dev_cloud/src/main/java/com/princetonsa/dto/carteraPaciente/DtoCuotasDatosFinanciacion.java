package com.princetonsa.dto.carteraPaciente;

import java.io.Serializable;
import java.math.BigDecimal;

import util.ConstantesBD;

/**
 * 
 *
 */
public class DtoCuotasDatosFinanciacion implements Serializable
{
	private int codigoPK;
	private int datosFinanciacion;
	private String numeroDocumento;
	private BigDecimal valorCuota;
	private String usuarioModifica;
	private String activo;
	
	//Adicionales para aplicacion de pagos
	private boolean aplicar;
	private BigDecimal valorAplicar;
	private BigDecimal valorDebe;
	
	private int nroCuota;
	private BigDecimal totalAplicado;
	
	private Boolean enMora;
	private int diasMora;
	private String fechaVigencia;
	
	public DtoCuotasDatosFinanciacion()
	{
		this.reset();
	}
	
	public void reset()
	{
		this.codigoPK = ConstantesBD.codigoNuncaValido;
		this.datosFinanciacion = ConstantesBD.codigoNuncaValido;
		this.numeroDocumento = "";
		this.valorCuota = new BigDecimal(0.0);
		this.usuarioModifica = "";
		this.activo = ConstantesBD.acronimoNo;
		this.aplicar=false;
		this.valorDebe= new BigDecimal(0.0);
		this.nroCuota=ConstantesBD.codigoNuncaValido;
		this.totalAplicado=new BigDecimal(0.0);
		this.enMora=false;
		this.diasMora=ConstantesBD.codigoNuncaValido;
		this.fechaVigencia="";
	}

	/**
	 * @return the codigoPK
	 */
	public int getCodigoPK() {
		return codigoPK;
	}

	/**
	 * @param codigoPK the codigoPK to set
	 */
	public void setCodigoPK(int codigoPK) {
		this.codigoPK = codigoPK;
	}

	/**
	 * @return the datosFinanciacion
	 */
	public int getDatosFinanciacion() {
		return datosFinanciacion;
	}

	/**
	 * @param datosFinanciacion the datosFinanciacion to set
	 */
	public void setDatosFinanciacion(int datosFinanciacion) {
		this.datosFinanciacion = datosFinanciacion;
	}

	/**
	 * @return the numeroDocumento
	 */
	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	/**
	 * @param numeroDocumento the numeroDocumento to set
	 */
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	/**
	 * @return the valorCuota
	 */
	public BigDecimal getValorCuota() {
		return valorCuota;
	}

	/**
	 * @param valorCuota the valorCuota to set
	 */
	public void setValorCuota(BigDecimal valorCuota) {
		this.valorCuota = valorCuota;
	}

	/**
	 * @return the usuarioModifica
	 */
	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	/**
	 * @return the activo
	 */
	public String getActivo() {
		return activo;
	}

	/**
	 * @param activo the activo to set
	 */
	public void setActivo(String activo) {
		this.activo = activo;
	}

	public boolean isAplicar() {
		return aplicar;
	}

	public void setAplicar(boolean aplicar) {
		this.aplicar = aplicar;
	}

	public BigDecimal getValorAplicar() {
		return valorAplicar;
	}

	public void setValorAplicar(BigDecimal valorAplicar) {
		this.valorAplicar = valorAplicar;
	}

	public BigDecimal getValorDebe() {
		return valorDebe;
	}

	public void setValorDebe(BigDecimal valorDebe) {
		this.valorDebe = valorDebe;
	}

	public int getNroCuota() {
		return nroCuota;
	}

	public void setNroCuota(int nroCuota) {
		this.nroCuota = nroCuota;
	}

	public BigDecimal getTotalAplicado() {
		return totalAplicado;
	}

	public void setTotalAplicado(BigDecimal totalAplicado) {
		this.totalAplicado = totalAplicado;
	}

	public Boolean getEnMora() {
		return enMora;
	}

	public void setEnMora(Boolean enMora) {
		this.enMora = enMora;
	}

	public int getDiasMora() {
		return diasMora;
	}

	public void setDiasMora(int diasMora) {
		this.diasMora = diasMora;
	}

	public String getFechaVigencia() {
		return fechaVigencia;
	}

	public void setFechaVigencia(String fechaVigencia) {
		this.fechaVigencia = fechaVigencia;
	}
	
	
}
