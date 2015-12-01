package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;

import util.ConstantesBD;

public class DtoRegistroEnvioInformInconsisenBD implements Serializable{

	/**
	 * 
	 * */
	private int codigoPk;
	
	/**
	 * 
	 * */
	private int codigoInformInconsisenBD;
	
	/**
	 * 
	 * */
	private String fechaEnvio;
	
	/**
	 * 
	 * */
	private String horaEnvio;
	
	/**
	 * 
	 * */
	private int codigoEntidadEnvio;
	
	/**
	 * 
	 * */
	private String medioEnvio;
	
	/**
	 * 
	 * */
	private int codigoConvenioEnvio;
	
	/**
	 * 
	 * */
	private String nombreConvenioEnvio;
	
	/**
	 * 
	 * */
	private String nombreEntidadEnvio;
	
	/**
	 * 
	 */
	private String usuarioEnvio;
	
	/**
	 * 
	 */
	private String urlArchivoIncoXmlDes;

	
	public DtoRegistroEnvioInformInconsisenBD()
	{
	    this.reset();	
	}
	
	public void reset()
	{
		this.codigoPk=ConstantesBD.codigoNuncaValido;
		this.codigoInformInconsisenBD=ConstantesBD.codigoNuncaValido;
		this.codigoConvenioEnvio=ConstantesBD.codigoNuncaValido;
		this.codigoEntidadEnvio=ConstantesBD.codigoNuncaValido;
		this.fechaEnvio=new String("");
		this.medioEnvio=new String("");
		this.horaEnvio=new String("");
		this.nombreConvenioEnvio=new String("");
		this.nombreEntidadEnvio=new String("");
		this.usuarioEnvio=new String("");
		this.urlArchivoIncoXmlDes = "";
	}
	
	
	public int getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public int getCodigoInformInconsisenBD() {
		return codigoInformInconsisenBD;
	}

	public void setCodigoInformInconsisenBD(int codigoInformInconsisenBD) {
		this.codigoInformInconsisenBD = codigoInformInconsisenBD;
	}

	public String getFechaEnvio() {
		return fechaEnvio;
	}

	public void setFechaEnvio(String fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}

	public String getHoraEnvio() {
		return horaEnvio;
	}

	public void setHoraEnvio(String horaEnvio) {
		this.horaEnvio = horaEnvio;
	}

	public int getCodigoEntidadEnvio() {
		return codigoEntidadEnvio;
	}

	public void setCodigoEntidadEnvio(int codigoEntidadEnvio) {
		this.codigoEntidadEnvio = codigoEntidadEnvio;
	}

	public String getMedioEnvio() {
		return medioEnvio;
	}

	public void setMedioEnvio(String medioEnvio) {
		this.medioEnvio = medioEnvio;
	}

	public int getCodigoConvenioEnvio() {
		return codigoConvenioEnvio;
	}

	public void setCodigoConvenioEnvio(int codigoConvenioEnvio) {
		this.codigoConvenioEnvio = codigoConvenioEnvio;
	}

	public String getNombreConvenioEnvio() {
		return nombreConvenioEnvio;
	}

	public void setNombreConvenioEnvio(String nombreConvenioEnvio) {
		this.nombreConvenioEnvio = nombreConvenioEnvio;
	}

	public String getNombreEntidadEnvio() {
		return nombreEntidadEnvio;
	}

	public void setNombreEntidadEnvio(String nombreEntidadEnvio) {
		this.nombreEntidadEnvio = nombreEntidadEnvio;
	}

	public String getUsuarioEnvio() {
		return usuarioEnvio;
	}

	public void setUsuarioEnvio(String usuarioEnvio) {
		this.usuarioEnvio = usuarioEnvio;
	}

	/**
	 * @return the urlArchivoIncoXmlDes
	 */
	public String getUrlArchivoIncoXmlDes() {
		return urlArchivoIncoXmlDes;
	}

	/**
	 * @param urlArchivoIncoXmlDes the urlArchivoIncoXmlDes to set
	 */
	public void setUrlArchivoIncoXmlDes(String urlArchivoIncoXmlDes) {
		this.urlArchivoIncoXmlDes = urlArchivoIncoXmlDes;
	}
	
}
