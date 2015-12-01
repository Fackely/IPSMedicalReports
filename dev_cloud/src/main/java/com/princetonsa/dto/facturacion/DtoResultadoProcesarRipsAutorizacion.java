package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.util.ArrayList;

import util.UtilidadTexto;

public class DtoResultadoProcesarRipsAutorizacion implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String autorizacion;
	private String fechaAutorizacion;
	private String tipoNroIDUsuario;
	private String tipoIDUsuario;
	private String numeroIDUsuario;
	private String nombreCompletoPaciente;
	private String primerNombrePaciente;
	private String segundoNombrePaciente;
	private String primerApellidoPaciente;
	private String segundoApellidoPaciente;
	private String tipoOrden;
	private String valor;
	private boolean mostrarAutorizacion;
	
	private String codigoServArt;	
	private String descripcionServArt;
	private String cantidadServArt;
	private String valorServArt;
	
	private ArrayList<DtoInconsistenciasRipsEntidadesSub> listaInconsistencias = new ArrayList<DtoInconsistenciasRipsEntidadesSub>();
	
	public String getFechaAutorizacion() {
		return fechaAutorizacion;
	}
	public void setFechaAutorizacion(String fechaAutorizacion) {
		this.fechaAutorizacion = fechaAutorizacion;
	}
	public String getTipoNroIDUsuario() {
		this.tipoNroIDUsuario=this.tipoIDUsuario+" "+this.numeroIDUsuario;
		return tipoNroIDUsuario;
	}
	public void setTipoNroIDUsuario(String tipoNroIDUsuario) {
		this.tipoNroIDUsuario = tipoNroIDUsuario;
	}
	public String getNombreCompletoPaciente() {
		this.nombreCompletoPaciente=this.primerApellidoPaciente+" "+(UtilidadTexto.isEmpty(this.segundoApellidoPaciente)?"":this.segundoApellidoPaciente)
									+this.primerNombrePaciente+" "+(UtilidadTexto.isEmpty(this.segundoNombrePaciente)?"":this.segundoNombrePaciente);
		return nombreCompletoPaciente;
	}
	public void setNombreCompletoPaciente(String nombreCompletoPaciente) {
		this.nombreCompletoPaciente = nombreCompletoPaciente;
	}
	public String getTipoOrden() {
		return tipoOrden;
	}
	public void setTipoOrden(String tipoOrden) {
		this.tipoOrden = tipoOrden;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getTipoIDUsuario() {
		return tipoIDUsuario;
	}
	public void setTipoIDUsuario(String tipoIDUsuario) {
		this.tipoIDUsuario = tipoIDUsuario;
	}
	public String getNumeroIDUsuario() {
		return numeroIDUsuario;
	}
	public void setNumeroIDUsuario(String numeroIDUsuario) {
		this.numeroIDUsuario = numeroIDUsuario;
	}
	public String getPrimerNombrePaciente() {
		return primerNombrePaciente;
	}
	public void setPrimerNombrePaciente(String primerNombrePaciente) {
		this.primerNombrePaciente = primerNombrePaciente;
	}
	public String getSegundoNombrePaciente() {
		return segundoNombrePaciente;
	}
	public void setSegundoNombrePaciente(String segundoNombrePaciente) {
		this.segundoNombrePaciente = segundoNombrePaciente;
	}
	public String getPrimerApellidoPaciente() {
		return primerApellidoPaciente;
	}
	public void setPrimerApellidoPaciente(String primerApellidoPaciente) {
		this.primerApellidoPaciente = primerApellidoPaciente;
	}
	public String getSegundoApellidoPaciente() {
		return segundoApellidoPaciente;
	}
	public void setSegundoApellidoPaciente(String segundoApellidoPaciente) {
		this.segundoApellidoPaciente = segundoApellidoPaciente;
	}
	public String getAutorizacion() {
		return autorizacion;
	}
	public void setAutorizacion(String autorizacion) {
		this.autorizacion = autorizacion;
	}
	public void setMostrarAutorizacion(boolean mostrarAutorizacion) {
		this.mostrarAutorizacion = mostrarAutorizacion;
	}
	public boolean isMostrarAutorizacion() {
		return mostrarAutorizacion;
	}
	public void setListaInconsistencias(ArrayList<DtoInconsistenciasRipsEntidadesSub> listaInconsistencias) {
		this.listaInconsistencias = listaInconsistencias;
	}
	public ArrayList<DtoInconsistenciasRipsEntidadesSub> getListaInconsistencias() {
		return listaInconsistencias;
	}
	public void setCodigoServArt(String codigoServArt) {
		this.codigoServArt = codigoServArt;
	}
	public String getCodigoServArt() {
		return codigoServArt;
	}
	public void setDescripcionServArt(String descripcionServArt) {
		this.descripcionServArt = descripcionServArt;
	}
	public String getDescripcionServArt() {
		return descripcionServArt;
	}
	public void setCantidadServArt(String cantidadServArt) {
		this.cantidadServArt = cantidadServArt;
	}
	public String getCantidadServArt() {
		return cantidadServArt;
	}
	public void setValorServArt(String valorServArt) {
		this.valorServArt = valorServArt;
	}
	public String getValorServArt() {
		return valorServArt;
	}

	
}
