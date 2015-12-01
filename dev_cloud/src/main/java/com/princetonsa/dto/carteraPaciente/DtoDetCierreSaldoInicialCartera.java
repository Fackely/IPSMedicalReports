package com.princetonsa.dto.carteraPaciente;

import java.io.Serializable;
import java.math.BigDecimal;

import util.ConstantesBD;

public class DtoDetCierreSaldoInicialCartera implements Serializable
{
	
	public DtoDetCierreSaldoInicialCartera() 
	{
		this.codigoCierre=ConstantesBD.codigoNuncaValido;
		this.codigo=ConstantesBD.codigoNuncaValido;
		this.codigoDocumento=ConstantesBD.codigoNuncaValido;
		this.tipoDocumento="";
		this.valorDocumentos=new BigDecimal(0);
		this.valorPagos=new BigDecimal(0);
	}
	
	/**
	 * 
	 */
	private int codigoCierre;
	
	/**
	 * @return the codigoCierre
	 */
	public int getCodigoCierre() {
		return codigoCierre;
	}
	/**
	 * @param codigoCierre the codigoCierre to set
	 */
	public void setCodigoCierre(int codigoCierre) {
		this.codigoCierre = codigoCierre;
	}

	/**
	 * codigo del detalle.
	 */
	private int codigo;
	
	/**
	 * Codigo del documento de garantia asociado.
	 */
	private int codigoDocumento;
	
	/**
	 * tipo de documento de garantia, se carga solo en consultas.
	 */
	private String tipoDocumento;
	
	/**
	 * valor del documento.
	 */
	private BigDecimal valorDocumentos;
	
	/**
	 * valor de pagos.
	 */
	private BigDecimal valorPagos;
	
	/**
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	/**
	 * @return the codigoDocumento
	 */
	public int getCodigoDocumento() {
		return codigoDocumento;
	}
	/**
	 * @param codigoDocumento the codigoDocumento to set
	 */
	public void setCodigoDocumento(int codigoDocumento) {
		this.codigoDocumento = codigoDocumento;
	}
	/**
	 * @return the tipoDocumento
	 */
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	/**
	 * @param tipoDocumento the tipoDocumento to set
	 */
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	/**
	 * @return the valorDocumentos
	 */
	public BigDecimal getValorDocumentos() {
		return valorDocumentos;
	}
	/**
	 * @param valorDocumentos the valorDocumentos to set
	 */
	public void setValorDocumentos(BigDecimal valorDocumentos) {
		this.valorDocumentos = valorDocumentos;
	}
	/**
	 * @return the valorPagos
	 */
	public BigDecimal getValorPagos() {
		return valorPagos;
	}
	/**
	 * @param valorPagos the valorPagos to set
	 */
	public void setValorPagos(BigDecimal valorPagos) {
		this.valorPagos = valorPagos;
	}
	
	
	
}
