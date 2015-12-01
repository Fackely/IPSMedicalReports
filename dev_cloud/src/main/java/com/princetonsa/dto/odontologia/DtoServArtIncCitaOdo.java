package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import util.ConstantesBD;
import util.InfoDatosInt;


public class DtoServArtIncCitaOdo implements Serializable {

	/**
	 * 
	 */
	
	
	private BigDecimal codigo_pK ;
	private BigDecimal servicioCitaOdo;
	private InfoDatosInt servicio;
	private int cantidad;
	private InfoDatosInt articulo;
	private InfoDatosInt solicitud;
	private DtoInfoFechaUsuario usuarioModifica;
	
	
	
	
	public DtoServArtIncCitaOdo(){
			reset();
	}
	
	/**
	 * 
	 */
	void reset(){
		 this.codigo_pK = BigDecimal.ZERO;
		  this.servicioCitaOdo=BigDecimal.ZERO;
		  this.servicio = new InfoDatosInt();
		  this.cantidad = ConstantesBD.codigoNuncaValido;
		  this.articulo = new InfoDatosInt();
		  this.solicitud = new InfoDatosInt();
		  this.usuarioModifica = new DtoInfoFechaUsuario();
	}
	/**
	 * @return the codigo_pK
	 */
	public BigDecimal getCodigo_pK() {
		return codigo_pK;
	}
	/**
	 * @param codigo_pK the codigo_pK to set
	 */
	public void setCodigo_pK(BigDecimal codigo_pK) {
		this.codigo_pK = codigo_pK;
	}
	/**
	 * @return the programaServPlant
	 */
	public BigDecimal getServicioCitaOdo() {
		return servicioCitaOdo;
	}
	/**
	 * @param programaServPlant the programaServPlant to set
	 */
	public void setServicioCitaOdo(BigDecimal servicioCitaOdo) {
		this.servicioCitaOdo = servicioCitaOdo;
	}
	/**
	 * @return the servicio
	 */
	public InfoDatosInt getServicio() {
		return servicio;
	}
	/**
	 * @param servicio the servicio to set
	 */
	public void setServicio(InfoDatosInt servicio) {
		this.servicio = servicio;
	}
	/**
	 * @return the cantidad
	 */
	public int getCantidad() {
		return cantidad;
	}
	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	/**
	 * @return the articulo
	 */
	public InfoDatosInt getArticulo() {
		return articulo;
	}
	/**
	 * @param articulo the articulo to set
	 */
	public void setArticulo(InfoDatosInt articulo) {
		this.articulo = articulo;
	}
	/**
	 * @return the solicitud
	 */
	public InfoDatosInt getSolicitud() {
		return solicitud;
	}
	/**
	 * @param solicitud the solicitud to set
	 */
	public void setSolicitud(InfoDatosInt solicitud) {
		this.solicitud = solicitud;
	}
	/**
	 * @return the serialVersionUID
	 */
	
	public void setUsuarioModifica(DtoInfoFechaUsuario usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	public DtoInfoFechaUsuario getUsuarioModifica() {
		return usuarioModifica;
	}
	
	
	
	
	
	

	
	
}
