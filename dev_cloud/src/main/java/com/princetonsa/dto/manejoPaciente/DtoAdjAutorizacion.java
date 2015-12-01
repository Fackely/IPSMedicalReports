/*
 * Abril 21, 2009
 */
package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;

import com.princetonsa.mundo.UsuarioBasico;

/**
 * DTO que modela los adjuntos de la autorizacion (adj_autorizaciones)
 * @author Sebastián Gómez
 *
 */
public class DtoAdjAutorizacion implements Serializable{
	
	private String codigoPK;
	private String autorizacion;
	private String nombreArchivo;
	private String nombreOriginal;
	private String fechaModifica;
	private String horaModifica;
	private UsuarioBasico usuarioModifica;
	private boolean eliminado;

	
	/**
	 * Cosntructor
	 */
	public DtoAdjAutorizacion()
	{
		this.clean();
	}
	
	public void clean()
	{
		this.codigoPK = "";
		this.autorizacion = "";
		this.nombreArchivo = "";
		this.nombreOriginal = "";
		this.fechaModifica = "";
		this.horaModifica = "";
		this.usuarioModifica = new UsuarioBasico();
		this.eliminado = false;
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
	 * @return the autorizacion
	 */
	public String getAutorizacion() {
		return autorizacion;
	}

	/**
	 * @param autorizacion the autorizacion to set
	 */
	public void setAutorizacion(String autorizacion) {
		this.autorizacion = autorizacion;
	}

	/**
	 * @return the nombreArchivo
	 */
	public String getNombreArchivo() {
		return nombreArchivo;
	}

	/**
	 * @param nombreArchivo the nombreArchivo to set
	 */
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	/**
	 * @return the nombreOriginal
	 */
	public String getNombreOriginal() {
		return nombreOriginal;
	}

	/**
	 * @param nombreOriginal the nombreOriginal to set
	 */
	public void setNombreOriginal(String nombreOriginal) {
		this.nombreOriginal = nombreOriginal;
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

	public boolean isEliminado() {
		return eliminado;
	}

	public void setEliminado(boolean eliminado) {
		this.eliminado = eliminado;
	}
}

