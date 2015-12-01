/**
 * 
 */
package com.princetonsa.dto.carteraPaciente;

import java.io.Serializable;
import java.math.BigDecimal;

import util.ConstantesBD;
import util.InfoDatosString;

/**
 * @author armando
 *
 */
public class DtoSaldosInicialesCarteraPaciente implements Serializable
{
	private InfoDatosString tipoDocumento;
	private String consecutivoDocumento;
	private String fechaElaboracion;
	private BigDecimal valorDocumento;
	private int codigoDeudor;
	private String nombreDeudor;
	private int codigoPaciente;
	private String nombresPaciente;
	private int codigoFactura;
	private int consecutivoFactura;
	private InfoDatosString convenio;
	
	public DtoSaldosInicialesCarteraPaciente()
	{
		this.tipoDocumento=new InfoDatosString();
		this.consecutivoDocumento="";
		this.fechaElaboracion="";
		this.valorDocumento=new BigDecimal(0.0);
		this.codigoDeudor=ConstantesBD.codigoNuncaValido;
		this.nombreDeudor="";
		this.codigoPaciente=ConstantesBD.codigoNuncaValido;
		this.nombresPaciente="";
		this.codigoFactura=ConstantesBD.codigoNuncaValido;
		this.consecutivoFactura=ConstantesBD.codigoNuncaValido;
		this.convenio=new InfoDatosString();
	}
	
	
	
	
	
	
	/**
	 * @return the tipoDocumento
	 */
	public InfoDatosString getTipoDocumento() {
		return tipoDocumento;
	}
	/**
	 * @param tipoDocumento the tipoDocumento to set
	 */
	public void setTipoDocumento(InfoDatosString tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	/**
	 * @return the consecutivoDocumento
	 */
	public String getConsecutivoDocumento() {
		return consecutivoDocumento;
	}
	/**
	 * @param consecutivoDocumento the consecutivoDocumento to set
	 */
	public void setConsecutivoDocumento(String consecutivoDocumento) {
		this.consecutivoDocumento = consecutivoDocumento;
	}
	/**
	 * @return the fechaElaboracion
	 */
	public String getFechaElaboracion() {
		return fechaElaboracion;
	}
	/**
	 * @param fechaElaboracion the fechaElaboracion to set
	 */
	public void setFechaElaboracion(String fechaElaboracion) {
		this.fechaElaboracion = fechaElaboracion;
	}
	/**
	 * @return the valorDocumento
	 */
	public BigDecimal getValorDocumento() {
		return valorDocumento;
	}
	/**
	 * @param valorDocumento the valorDocumento to set
	 */
	public void setValorDocumento(BigDecimal valorDocumento) {
		this.valorDocumento = valorDocumento;
	}
	/**
	 * @return the nombreDeudor
	 */
	public String getNombreDeudor() {
		return nombreDeudor;
	}
	/**
	 * @param nombreDeudor the nombreDeudor to set
	 */
	public void setNombreDeudor(String nombreDeudor) {
		this.nombreDeudor = nombreDeudor;
	}
	/**
	 * @return the codigoPaciente
	 */
	public int getCodigoPaciente() {
		return codigoPaciente;
	}
	/**
	 * @param codigoPaciente the codigoPaciente to set
	 */
	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}
	/**
	 * @return the nombresPaciente
	 */
	public String getNombresPaciente() {
		return nombresPaciente;
	}
	/**
	 * @param nombresPaciente the nombresPaciente to set
	 */
	public void setNombresPaciente(String nombresPaciente) {
		this.nombresPaciente = nombresPaciente;
	}
	/**
	 * @return the codigoFactura
	 */
	public int getCodigoFactura() {
		return codigoFactura;
	}
	/**
	 * @param codigoFactura the codigoFactura to set
	 */
	public void setCodigoFactura(int codigoFactura) {
		this.codigoFactura = codigoFactura;
	}
	/**
	 * @return the consecutivoFactura
	 */
	public int getConsecutivoFactura() {
		return consecutivoFactura;
	}
	/**
	 * @param consecutivoFactura the consecutivoFactura to set
	 */
	public void setConsecutivoFactura(int consecutivoFactura) {
		this.consecutivoFactura = consecutivoFactura;
	}
	/**
	 * @return the convenio
	 */
	public InfoDatosString getConvenio() {
		return convenio;
	}
	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(InfoDatosString convenio) {
		this.convenio = convenio;
	}






	/**
	 * @return the codigoDeudor
	 */
	public int getCodigoDeudor() {
		return codigoDeudor;
	}






	/**
	 * @param codigoDeudor the codigoDeudor to set
	 */
	public void setCodigoDeudor(int codigoDeudor) {
		this.codigoDeudor = codigoDeudor;
	}
	

}
