package com.princetonsa.dto.historiaClinica;

import java.io.Serializable;
/*
* Tipo Modificacion: Segun incidencia 7055
* usuario: jesrioro
* Fecha: 30/05/2013
* Descripcion: Se  implementa  serializable  para  poder generar  el  reporte  de  HC  en  el  contexto de  reportes            
*/
public class DtoIngresoHistoriaClinica implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 6336494356766084635L;
	private Boolean ingreso;
	private String fechaHoraIngreso;
	private String fechaHoraEgreso;
	private String centroAtencion;
	private String viaIng;
	private String especialidad;
	private String ing;
	private String ing2;
	private String estadoIngreso;
	private String estadoCuenta;
	private String cuenta;
	private String codigoViaIngreso;
	private String fechaIngreso;
	private String viaIngreso;
	private String dx;
	private String asocio;
	private String reIngreso;
	private String preIngreso;
	private String codigoPkEntidadSubcontratada;
	private String razonSocialEntidadSubcontratada;
	private String codigoDx;
	private String codigoCie;
	private String descripcionDx; 
	private String tipoPaciente;
	private boolean ordenamiento;
	
	

	/**
	 * 
	 */
	public DtoIngresoHistoriaClinica() {
		this.ingreso=false;
		this.fechaHoraIngreso="";
		this.fechaHoraEgreso="";
		this.centroAtencion="";
		this.viaIng="";
		this.especialidad="";
		this.ing="";
		this.estadoIngreso="";
		this.estadoCuenta="";
		this.cuenta="";
		this.codigoViaIngreso="";
		this.fechaIngreso="";
		this.viaIngreso="";
		this.dx="";
		this.reIngreso="";
		this.preIngreso="";
		this.codigoPkEntidadSubcontratada="";
		this.razonSocialEntidadSubcontratada="";
		this.codigoDx="";
		this.codigoCie="";
		this.descripcionDx=""; 
		this.tipoPaciente="";
		this.ordenamiento=false;
	}





	/**
	 * @return the fechaHoraIngreso
	 */
	public String getFechaHoraIngreso() {
		return fechaHoraIngreso;
	}


	/**
	 * @param fechaHoraIngreso the fechaHoraIngreso to set
	 */
	public void setFechaHoraIngreso(String fechaHoraIngreso) {
		this.fechaHoraIngreso = fechaHoraIngreso;
	}


	/**
	 * @return the fechaHoraEgreso
	 */
	public String getFechaHoraEgreso() {
		return fechaHoraEgreso;
	}


	/**
	 * @param fechaHoraEgreso the fechaHoraEgreso to set
	 */
	public void setFechaHoraEgreso(String fechaHoraEgreso) {
		this.fechaHoraEgreso = fechaHoraEgreso;
	}


	/**
	 * @return the centroAtencion
	 */
	public String getCentroAtencion() {
		return centroAtencion;
	}


	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}


	/**
	 * @return the viaIng
	 */
	public String getViaIng() {
		return viaIng;
	}


	/**
	 * @param viaIng the viaIng to set
	 */
	public void setViaIng(String viaIng) {
		this.viaIng = viaIng;
	}


	/**
	 * @return the especialidad
	 */
	public String getEspecialidad() {
		return especialidad;
	}


	/**
	 * @param especialidad the especialidad to set
	 */
	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}


	/**
	 * @return the ing
	 */
	public String getIng() {
		return ing;
	}


	/**
	 * @param ing the ing to set
	 */
	public void setIng(String ing) {
		this.ing = ing;
	}


	/**
	 * @return the estadoIngreso
	 */
	public String getEstadoIngreso() {
		return estadoIngreso;
	}


	/**
	 * @param estadoIngreso the estadoIngreso to set
	 */
	public void setEstadoIngreso(String estadoIngreso) {
		this.estadoIngreso = estadoIngreso;
	}


	/**
	 * @return the estadoCuenta
	 */
	public String getEstadoCuenta() {
		return estadoCuenta;
	}


	/**
	 * @param estadoCuenta the estadoCuenta to set
	 */
	public void setEstadoCuenta(String estadoCuenta) {
		this.estadoCuenta = estadoCuenta;
	}





	/**
	 * @return the ingreso
	 */
	public Boolean getIngreso() {
		return ingreso;
	}





	/**
	 * @param ingreso the ingreso to set
	 */
	public void setIngreso(Boolean ingreso) {
		this.ingreso = ingreso;
	}





	/**
	 * @return the cuenta
	 */
	public String getCuenta() {
		return cuenta;
	}





	/**
	 * @param cuenta the cuenta to set
	 */
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}





	/**
	 * @return the codigoViaIngreso
	 */
	public String getCodigoViaIngreso() {
		return codigoViaIngreso;
	}





	/**
	 * @param codigoViaIngreso the codigoViaIngreso to set
	 */
	public void setCodigoViaIngreso(String codigoViaIngreso) {
		this.codigoViaIngreso = codigoViaIngreso;
	}





	/**
	 * @return the fechaIngreso
	 */
	public String getFechaIngreso() {
		return fechaIngreso;
	}





	/**
	 * @param fechaIngreso the fechaIngreso to set
	 */
	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}





	/**
	 * @return the viaIngreso
	 */
	public String getViaIngreso() {
		return viaIngreso;
	}





	/**
	 * @param viaIngreso the viaIngreso to set
	 */
	public void setViaIngreso(String viaIngreso) {
		this.viaIngreso = viaIngreso;
	}





	/**
	 * @return the dx
	 */
	public String getDx() {
		return dx;
	}





	/**
	 * @param dx the dx to set
	 */
	public void setDx(String dx) {
		this.dx = dx;
	}





	/**
	 * @return the ing2
	 */
	public String getIng2() {
		return ing2;
	}





	/**
	 * @param ing2 the ing2 to set
	 */
	public void setIng2(String ing2) {
		this.ing2 = ing2;
	}





	/**
	 * @return the asocio
	 */
	public String getAsocio() {
		return asocio;
	}





	/**
	 * @param asocio the asocio to set
	 */
	public void setAsocio(String asocio) {
		this.asocio = asocio;
	}





	/**
	 * @return the reIngreso
	 */
	public String getReIngreso() {
		return reIngreso;
	}





	/**
	 * @param reIngreso the reIngreso to set
	 */
	public void setReIngreso(String reIngreso) {
		this.reIngreso = reIngreso;
	}





	/**
	 * @return the preIngreso
	 */
	public String getPreIngreso() {
		return preIngreso;
	}





	/**
	 * @param preIngreso the preIngreso to set
	 */
	public void setPreIngreso(String preIngreso) {
		this.preIngreso = preIngreso;
	}





	/**
	 * @return the codigoPkEntidadSubcontratada
	 */
	public String getCodigoPkEntidadSubcontratada() {
		return codigoPkEntidadSubcontratada;
	}





	/**
	 * @param codigoPkEntidadSubcontratada the codigoPkEntidadSubcontratada to set
	 */
	public void setCodigoPkEntidadSubcontratada(String codigoPkEntidadSubcontratada) {
		this.codigoPkEntidadSubcontratada = codigoPkEntidadSubcontratada;
	}





	/**
	 * @return the razonSocialEntidadSubcontratada
	 */
	public String getRazonSocialEntidadSubcontratada() {
		return razonSocialEntidadSubcontratada;
	}





	/**
	 * @param razonSocialEntidadSubcontratada the razonSocialEntidadSubcontratada to set
	 */
	public void setRazonSocialEntidadSubcontratada(
			String razonSocialEntidadSubcontratada) {
		this.razonSocialEntidadSubcontratada = razonSocialEntidadSubcontratada;
	}





	/**
	 * @return the codigoDx
	 */
	public String getCodigoDx() {
		return codigoDx;
	}





	/**
	 * @param codigoDx the codigoDx to set
	 */
	public void setCodigoDx(String codigoDx) {
		this.codigoDx = codigoDx;
	}





	/**
	 * @return the codigoCie
	 */
	public String getCodigoCie() {
		return codigoCie;
	}





	/**
	 * @param codigoCie the codigoCie to set
	 */
	public void setCodigoCie(String codigoCie) {
		this.codigoCie = codigoCie;
	}





	/**
	 * @return the descripcionDx
	 */
	public String getDescripcionDx() {
		return descripcionDx;
	}





	/**
	 * @param descripcionDx the descripcionDx to set
	 */
	public void setDescripcionDx(String descripcionDx) {
		this.descripcionDx = descripcionDx;
	}





	/**
	 * @return the tipoPaciente
	 */
	public String getTipoPaciente() {
		return tipoPaciente;
	}





	/**
	 * @param tipoPaciente the tipoPaciente to set
	 */
	public void setTipoPaciente(String tipoPaciente) {
		this.tipoPaciente = tipoPaciente;
	}





	/**
	 * @return the ordenamiento
	 */
	public boolean isOrdenamiento() {
		return ordenamiento;
	}





	/**
	 * @param ordenamiento the ordenamiento to set
	 */
	public void setOrdenamiento(boolean ordenamiento) {
		this.ordenamiento = ordenamiento;
	}

	

}
