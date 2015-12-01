package com.princetonsa.dto.interfaz;

import java.io.Serializable;

import util.InfoDatosString;

/**
 * 
 * DTO para el manejo de los posibles numeros de documentro
 * que puede tener un registro de movimiento contable
 * @author Sebastián Gómez R.
 *
 */
public class DtoInterfazDatosDocumentoS1E implements Serializable
{
	private InfoDatosString documento;
	private String codigoTipoConsecutivo ;
	private String fecha;
	private String valor;
	
	
	public DtoInterfazDatosDocumentoS1E()
	{
		reset();
	}
	
	public void reset()
	{
		this.documento = new InfoDatosString("","");
		this.codigoTipoConsecutivo = "";
		this.fecha = "";
		this.valor = "";
	}
	
	
	/**
	 * @return the documento
	 */
	public InfoDatosString getDocumento() {
		return documento;
	}

	/**
	 * @param documento the documento to set
	 */
	public void setDocumento(InfoDatosString documento) {
		this.documento = documento;
	}
	
	/**
	 * @return the documento
	 */
	public String getNumeroDocumento() {
		return documento.getCodigo();
	}

	/**
	 * @param documento the documento to set
	 */
	public void setNumeroDocumento(String documento) {
		this.documento.setCodigo(documento);
	}
	
	/**
	 * @return the consecutivo
	 */
	public String getConsecutivoDocumento() {
		return documento.getConsecutivo();
	}

	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivoDocumento(String consecutivo) {
		this.documento.setConsecutivo(consecutivo);
	}
	
	/**
	 * @return the documento
	 */
	public String getDescripcionDocumento() {
		return documento.getDescripcion();
	}

	/**
	 * @param documento the documento to set
	 */
	public void setDescripcionDocumento(String documento) {
		this.documento.setDescripcion(documento);
	}
	
	/**
	 * @return the documento
	 */
	public String getTipoDocumento() {
		return documento.getValue();
	}

	/**
	 * @param documento the documento to set
	 */
	public void setTipoDocumento(String documento) {
		this.documento.setValue(documento);
	}


	/**
	 * @return the codigoTipoConsecutivo
	 */
	public String getCodigoTipoConsecutivo() {
		return codigoTipoConsecutivo;
	}


	/**
	 * @param codigoTipoConsecutivo the codigoTipoConsecutivo to set
	 */
	public void setCodigoTipoConsecutivo(String codigoTipoConsecutivo) {
		this.codigoTipoConsecutivo = codigoTipoConsecutivo;
	}

	/**
	 * @return the fecha
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return the valor
	 */
	public String getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}
	
}
