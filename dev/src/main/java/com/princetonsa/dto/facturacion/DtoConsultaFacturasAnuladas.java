package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.math.BigDecimal;

import util.ConstantesBD;

/**
 * 
 * @author axioma
 *
 */
public class DtoConsultaFacturasAnuladas implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1755269383323372340L;
	
	/**
	 * 
	 */
	private BigDecimal codigoFactura;
	
	/**
	 * 
	 */
	private int institucion;
	
	/**
	 * 
	 */
	private BigDecimal consecutivoFactura;
	
	/**
	 * 
	 */
	private BigDecimal consecutivoAnulacion;
	
	/**
	 * 
	 */
	private String fechaInicialAnulacion;
	
	/**
	 * 
	 */
	private String fechaFinalAnulacion;
	
	/**
	 * 
	 */
	private String loginUsuario;
	
	/**
	 * 
	 */
	private int centroAtencion;
	
	/**
	 * 
	 */
	private int empresaInstitucion;
	
	/**
	 * 
	 */
	public DtoConsultaFacturasAnuladas() 
	{
		super();
		this.codigoFactura = BigDecimal.ZERO;
		this.institucion = ConstantesBD.codigoNuncaValido;
		this.consecutivoFactura = BigDecimal.ZERO;
		this.consecutivoAnulacion = BigDecimal.ZERO;
		this.fechaFinalAnulacion = "";
		this.loginUsuario = "";
		this.centroAtencion = ConstantesBD.codigoNuncaValido;
		this.empresaInstitucion = ConstantesBD.codigoNuncaValido;
		this.fechaInicialAnulacion="";
	}
	/**
	 * @return the codigoFactura
	 */
	public BigDecimal getCodigoFactura() {
		return codigoFactura;
	}
	/**
	 * @param codigoFactura the codigoFactura to set
	 */
	public void setCodigoFactura(BigDecimal codigoFactura) {
		this.codigoFactura = codigoFactura;
	}
	/**
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}
	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}
	/**
	 * @return the consecutivoFactura
	 */
	public BigDecimal getConsecutivoFactura() {
		return consecutivoFactura;
	}
	/**
	 * @param consecutivoFactura the consecutivoFactura to set
	 */
	public void setConsecutivoFactura(BigDecimal consecutivoFactura) {
		this.consecutivoFactura = consecutivoFactura;
	}
	/**
	 * @return the consecutivoAnulacion
	 */
	public BigDecimal getConsecutivoAnulacion() {
		return consecutivoAnulacion;
	}
	/**
	 * @param consecutivoAnulacion the consecutivoAnulacion to set
	 */
	public void setConsecutivoAnulacion(BigDecimal consecutivoAnulacion) {
		this.consecutivoAnulacion = consecutivoAnulacion;
	}
	/**
	 * @return the fechaFinalAnulacion
	 */
	public String getFechaFinalAnulacion() {
		return fechaFinalAnulacion;
	}
	/**
	 * @param fechaFinalAnulacion the fechaFinalAnulacion to set
	 */
	public void setFechaFinalAnulacion(String fechaFinalAnulacion) {
		this.fechaFinalAnulacion = fechaFinalAnulacion;
	}
	/**
	 * @return the loginUsuario
	 */
	public String getLoginUsuario() {
		return loginUsuario;
	}
	/**
	 * @param loginUsuario the loginUsuario to set
	 */
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}
	/**
	 * @return the centroAtencion
	 */
	public int getCentroAtencion() {
		return centroAtencion;
	}
	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}
	/**
	 * @return the empresaInstitucion
	 */
	public int getEmpresaInstitucion() {
		return empresaInstitucion;
	}
	/**
	 * @param empresaInstitucion the empresaInstitucion to set
	 */
	public void setEmpresaInstitucion(int empresaInstitucion) {
		this.empresaInstitucion = empresaInstitucion;
	}
	/**
	 * @return the fechaInicialAnulacion
	 */
	public String getFechaInicialAnulacion() {
		return fechaInicialAnulacion;
	}
	/**
	 * @param fechaInicialAnulacion the fechaInicialAnulacion to set
	 */
	public void setFechaInicialAnulacion(String fechaInicialAnulacion) {
		this.fechaInicialAnulacion = fechaInicialAnulacion;
	}

	
}
