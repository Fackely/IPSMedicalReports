/*
 * Abril 21, 2009
 */
package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;

import com.princetonsa.mundo.UsuarioBasico;

import util.ConstantesBD;
import util.InfoDatos;
import util.InfoDatosInt;

/**
 * DTO: que modela el envio de autorizaciones (envio_autorizaciones)
 * @author Sebastián Gómez
 *
 */
public class DtoEnvioAutorizacion implements Serializable
{
	private String codigoPK;
	private String detAutorizacion;
	private InfoDatos entidadEnvio;
	private boolean esEmpresa;	
	private String medioEnvio;
	private String fechaModifica;
	private String horaModifica;
	private UsuarioBasico usuarioModifica;
	private boolean confirmarEnvio; 
	/**
	 * 
	 */
	private String urlArchivoIncoXmlDes;
	
	/**
	 * Cosntructor
	 */
	public DtoEnvioAutorizacion()
	{
		this.clean();
	}
	
	public void clean()
	{
		this.codigoPK = "";
		this.detAutorizacion = "";
		this.entidadEnvio = new InfoDatos(ConstantesBD.codigoNuncaValido,"");		
		this.esEmpresa = false;
		this.medioEnvio = "";
		this.fechaModifica = "";
		this.horaModifica = "";
		this.usuarioModifica = new UsuarioBasico();
		
		this.confirmarEnvio = false;
		this.urlArchivoIncoXmlDes= "";
	}

	/**
	 * @return the confirmarEnvio
	 */
	public boolean isConfirmarEnvio() {
		return confirmarEnvio;
	}

	/**
	 * @param confirmarEnvio the confirmarEnvio to set
	 */
	public void setConfirmarEnvio(boolean confirmarEnvio) {
		this.confirmarEnvio = confirmarEnvio;
	}

	/**
	 * @return the codigoPK
	 */
	public String getCodigoPK() {
		return codigoPK;
	}

	/**
	 * @param codigoPK the codigoPK to set
	 */
	public void setCodigoPK(String codigoPK) {
		this.codigoPK = codigoPK;
	}
	
	/**
	 * @return the entidadEnvio
	 */
	public InfoDatos getEntidadEnvio() {
		return entidadEnvio;
	}

	/**
	 * @param entidadEnvio the entidadEnvio to set
	 */
	public void setEntidadEnvio(InfoDatos entidadEnvio) {
		this.entidadEnvio = entidadEnvio;
	}



	/**
	 * @return the medioEnvio
	 */
	public String getMedioEnvio() {
		return medioEnvio;
	}

	/**
	 * @param medioEnvio the medioEnvio to set
	 */
	public void setMedioEnvio(String medioEnvio) {
		this.medioEnvio = medioEnvio;
	}

	/**
	 * @return the fechaModifica
	 */
	public String getFechaModifica() {
		return fechaModifica;
	}

	/**
	 * @param fechaModifica the fechaModifica to set
	 */
	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	/**
	 * @return the horaModifica
	 */
	public String getHoraModifica() {
		return horaModifica;
	}

	/**
	 * @param horaModifica the horaModifica to set
	 */
	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	/**
	 * @return the usuarioModifica
	 */
	public UsuarioBasico getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(UsuarioBasico usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	/**
	 * @return the esEmpresa
	 */
	public boolean isEsEmpresa() {
		return esEmpresa;
	}

	/**
	 * @param esEmpresa the esEmpresa to set
	 */
	public void setEsEmpresa(boolean esEmpresa) {
		this.esEmpresa = esEmpresa;
	}

	public String getDetAutorizacion() {
		return detAutorizacion;
	}

	public void setDetAutorizacion(String detAutorizacion) {
		this.detAutorizacion = detAutorizacion;
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