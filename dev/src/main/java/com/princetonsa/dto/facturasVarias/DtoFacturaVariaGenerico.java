package com.princetonsa.dto.facturasVarias;

import java.io.Serializable;
import java.util.HashMap;

public class DtoFacturaVariaGenerico implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public DtoFacturaVariaGenerico() {
		
	}
	
	private int codigoInstitucion;
	private String estadosFactura;
	private String centroAtencionUsuarioActual;
	private HashMap detalleFactura;
	private String codigoFactura;
	private String loginUsuarioActual;

	/**
	 * @return the loginUsuarioActual
	 */
	public String getLoginUsuarioActual() {
		return loginUsuarioActual;
	}

	/**
	 * @param loginUsuarioActual the loginUsuarioActual to set
	 */
	public void setLoginUsuarioActual(String loginUsuarioActual) {
		this.loginUsuarioActual = loginUsuarioActual;
	}

	/**
	 * @return the codigoFactura
	 */
	public String getCodigoFactura() {
		return codigoFactura;
	}

	/**
	 * @param codigoFactura the codigoFactura to set
	 */
	public void setCodigoFactura(String codigoFactura) {
		this.codigoFactura = codigoFactura;
	}

	/**
	 * @return the detalleFactura
	 */
	public HashMap getDetalleFactura() {
		return detalleFactura;
	}

	/**
	 * @param detalleFactura the detalleFactura to set
	 */
	public void setDetalleFactura(HashMap detalleFactura) {
		this.detalleFactura = detalleFactura;
	}

	/**
	 * @return the centroAtencionUsuarioActual
	 */
	public String getCentroAtencionUsuarioActual() {
		return centroAtencionUsuarioActual;
	}

	/**
	 * @param centroAtencionUsuarioActual the centroAtencionUsuarioActual to set
	 */
	public void setCentroAtencionUsuarioActual(String centroAtencionUsuarioActual) {
		this.centroAtencionUsuarioActual = centroAtencionUsuarioActual;
	}

	/**
	 * @return the estadosFactura
	 */
	public String getEstadosFactura() {
		return estadosFactura;
	}

	/**
	 * @param estadosFactura the estadosFactura to set
	 */
	public void setEstadosFactura(String estadosFactura) {
		this.estadosFactura = estadosFactura;
	}

	/**
	 * @return the codigoInstitucion
	 */
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * @param codigoInstitucion the codigoInstitucion to set
	 */
	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}
	

}
