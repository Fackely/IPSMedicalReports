/*
 * Ene 06, 2008
 */
package com.princetonsa.dto.administracion;

import java.io.Serializable;

import com.princetonsa.dto.interfaz.DtoCuentaContable;

import util.ConstantesBD;

/**
 * Data Transfer Object: 
 * Usado para el manejo del concepto de retencion
 * 
 * @author Angela María Angel.
 *
 */
public class DtoConceptosRetencionTercero implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String consecutivo;
	private String tercero;
	private String tipoAplicacion;
	private String indAgenteRetenedor;;
	private String fechaModificacion;
	private String horaModificacion;
	private String usuarioModificacion;
	private String activo;
	private String fechaInactivacion;
	private String horaInactivacion;
	private String usuarioInactivacion;
	
	//Datos adiconales para manejo en objetos
	private DtoConceptosRetencion conceptosRet;
	
	private boolean enBD=false;
	private boolean modificado=false;
	
	/**
	 * Constructor
	 */
	public DtoConceptosRetencionTercero()
	{
		this.clean();
	}
	
	/**
	 * Método que limpia los datos del DTo
	 */
	public void clean()
	{
		this.consecutivo="";
		this.tercero="";
		this.tipoAplicacion="";
		this.indAgenteRetenedor="";
		this.activo="";
		this.conceptosRet= new DtoConceptosRetencion();
		this.fechaInactivacion="";
		this.horaInactivacion="";
		this.usuarioInactivacion="";
		this.fechaModificacion="";
		this.horaModificacion="";
		this.usuarioModificacion="";
		this.enBD=false;
	}

	
	
	public String getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public String getHoraModificacion() {
		return horaModificacion;
	}

	public void setHoraModificacion(String horaModificacion) {
		this.horaModificacion = horaModificacion;
	}

	public String getUsuarioModificacion() {
		return usuarioModificacion;
	}

	public void setUsuarioModificacion(String usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}

	public String getFechaInactivacion() {
		return fechaInactivacion;
	}

	public void setFechaInactivacion(String fechaInactivacion) {
		this.fechaInactivacion = fechaInactivacion;
	}

	public String getHoraInactivacion() {
		return horaInactivacion;
	}

	public void setHoraInactivacion(String horaInactivacion) {
		this.horaInactivacion = horaInactivacion;
	}

	public String getUsuarioInactivacion() {
		return usuarioInactivacion;
	}

	public void setUsuarioInactivacion(String usuarioInactivacion) {
		this.usuarioInactivacion = usuarioInactivacion;
	}

	public String getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getTercero() {
		return tercero;
	}

	public void setTercero(String tercero) {
		this.tercero = tercero;
	}

	public String getTipoAplicacion() {
		return tipoAplicacion;
	}

	public void setTipoAplicacion(String tipoAplicacion) {
		this.tipoAplicacion = tipoAplicacion;
	}

	public String getIndAgenteRetenedor() {
		return indAgenteRetenedor;
	}

	public void setIndAgenteRetenedor(String indAgenteRetenedor) {
		this.indAgenteRetenedor = indAgenteRetenedor;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public DtoConceptosRetencion getConceptosRet() {
		return conceptosRet;
	}

	public void setConceptosRet(DtoConceptosRetencion conceptosRet) {
		this.conceptosRet = conceptosRet;
	}

	public boolean isEnBD() {
		return enBD;
	}

	public void setEnBD(boolean enBD) {
		this.enBD = enBD;
	}

	public boolean isModificado() {
		return modificado;
	}

	public void setModificado(boolean modificado) {
		this.modificado = modificado;
	}
	
}
