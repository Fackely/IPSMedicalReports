package com.princetonsa.dto.manejoPaciente;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;

public class DtoEnvioInfoAtenIniUrg
{
	/**
	 * 
	 * */
	private int codigoPk;
	
	/**
	 * 
	 * */
	private int codigoInfoAtencioIniUrg;
	
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
	 * */
	private String nombreUsuarioEnvio;
	/**
	 * 
	 */
	private String urlArchivoIncoXmlDes; 
	
	public DtoEnvioInfoAtenIniUrg()
	{
		this.reset();
	}	
	
	public void reset()
	{
		this.codigoPk = ConstantesBD.codigoNuncaValido ;
		this.codigoInfoAtencioIniUrg = ConstantesBD.codigoNuncaValido;
		this.fechaEnvio = "";
		this.horaEnvio = "";
		this.codigoEntidadEnvio = ConstantesBD.codigoNuncaValido;
		this.medioEnvio = "";
		this.codigoConvenioEnvio = ConstantesBD.codigoNuncaValido;
		this.nombreConvenioEnvio = "";
		this.nombreEntidadEnvio = "";
		this.nombreUsuarioEnvio = "";
		this.urlArchivoIncoXmlDes = "";
	}
	

	public int getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public int getCodigoInfoAtencioIniUrg() {
		return codigoInfoAtencioIniUrg;
	}

	public void setCodigoInfoAtencioIniUrg(int codigoInfoAtencioIniUrg) {
		this.codigoInfoAtencioIniUrg = codigoInfoAtencioIniUrg;
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
	
	public String getDescripcioMedioEnvio()
	{	
		if(this.medioEnvio.equals(ConstantesIntegridadDominio.acronimoEmail))
			return "Correo Electronico";
		else if(this.medioEnvio.equals(ConstantesIntegridadDominio.acronimoFax))
			return "FAX";
		else if(this.medioEnvio.equals(ConstantesIntegridadDominio.acronimoIntercambioElectDatos))
			return "Intercambio Electrónico de Datos (EDI)";
		else
			return "";
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

	public String getNombreUsuarioEnvio() {
		return nombreUsuarioEnvio;
	}

	public void setNombreUsuarioEnvio(String nombreUsuarioEnvio) {
		this.nombreUsuarioEnvio = nombreUsuarioEnvio;
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