package com.princetonsa.dto.carteraPaciente;

import java.io.Serializable;

public class DtoHistoDatosFinanciacion implements Serializable{

	private String codigoPk;
	private String consecutivo;
	private String nroCuotasAnteriores;
	private String nroCuotasAsignadas;
	private String diasPorCuotas;
	private String valorRefinanciar;
	private String valorCuota;
	private String fechaInicio;
	private String observaciones;
	private String fechaModificacion;
	private String horaModificacion;
	private String usuarioModificacion;
	private String codDatosFinanciacion;
	
	
	
	
	public DtoHistoDatosFinanciacion()
	{
		this.reset();
	}
	
	public void reset()
	{
		this.codigoPk=new String("");
		this.consecutivo=new String("");
		this.nroCuotasAnteriores=new String("");
		this.nroCuotasAsignadas=new String("");
		this.diasPorCuotas=new String("");
		this.valorRefinanciar=new String("");
		this.valorCuota=new String("");
		this.fechaInicio=new String("");
		this.observaciones=new String("");
		this.fechaModificacion=new String("");
		this.horaModificacion=new String("");
		this.usuarioModificacion=new String("");
		this.codDatosFinanciacion=new String("");
	}
	
	
	/**
	 * @return the codigoPk
	 */
	public String getCodigoPk() {
		return codigoPk;
	}
	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(String codigoPk) {
		this.codigoPk = codigoPk;
	}
	/**
	 * @return the consecutivo
	 */
	public String getConsecutivo() {
		return consecutivo;
	}
	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}
	/**
	 * @return the nroCuotasAnteriores
	 */
	public String getNroCuotasAnteriores() {
		return nroCuotasAnteriores;
	}
	/**
	 * @param nroCuotasAnteriores the nroCuotasAnteriores to set
	 */
	public void setNroCuotasAnteriores(String nroCuotasAnteriores) {
		this.nroCuotasAnteriores = nroCuotasAnteriores;
	}
	/**
	 * @return the nroCuotasAsignadas
	 */
	public String getNroCuotasAsignadas() {
		return nroCuotasAsignadas;
	}
	/**
	 * @param nroCuotasAsignadas the nroCuotasAsignadas to set
	 */
	public void setNroCuotasAsignadas(String nroCuotasAsignadas) {
		this.nroCuotasAsignadas = nroCuotasAsignadas;
	}
	/**
	 * @return the diasPorCuotas
	 */
	public String getDiasPorCuotas() {
		return diasPorCuotas;
	}
	/**
	 * @param diasPorCuotas the diasPorCuotas to set
	 */
	public void setDiasPorCuotas(String diasPorCuotas) {
		this.diasPorCuotas = diasPorCuotas;
	}
	/**
	 * @return the valorRefinanciar
	 */
	public String getValorRefinanciar() {
		return valorRefinanciar;
	}
	/**
	 * @param valorRefinanciar the valorRefinanciar to set
	 */
	public void setValorRefinanciar(String valorRefinanciar) {
		this.valorRefinanciar = valorRefinanciar;
	}
	/**
	 * @return the valorCuota
	 */
	public String getValorCuota() {
		return valorCuota;
	}
	/**
	 * @param valorCuota the valorCuota to set
	 */
	public void setValorCuota(String valorCuota) {
		this.valorCuota = valorCuota;
	}
	/**
	 * @return the fechaInicio
	 */
	public String getFechaInicio() {
		return fechaInicio;
	}
	/**
	 * @param fechaInicio the fechaInicio to set
	 */
	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}
	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	/**
	 * @return the fechaModificacion
	 */
	public String getFechaModificacion() {
		return fechaModificacion;
	}
	/**
	 * @param fechaModificacion the fechaModificacion to set
	 */
	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	/**
	 * @return the horaModificacion
	 */
	public String getHoraModificacion() {
		return horaModificacion;
	}
	/**
	 * @param horaModificacion the horaModificacion to set
	 */
	public void setHoraModificacion(String horaModificacion) {
		this.horaModificacion = horaModificacion;
	}
	/**
	 * @return the usuarioModificacion
	 */
	public String getUsuarioModificacion() {
		return usuarioModificacion;
	}
	/**
	 * @param usuarioModificacion the usuarioModificacion to set
	 */
	public void setUsuarioModificacion(String usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}
	/**
	 * @return the codDatosFinanciacion
	 */
	public String getCodDatosFinanciacion() {
		return codDatosFinanciacion;
	}
	/**
	 * @param codDatosFinanciacion the codDatosFinanciacion to set
	 */
	public void setCodDatosFinanciacion(String codDatosFinanciacion) {
		this.codDatosFinanciacion = codDatosFinanciacion;
	}
	
	
}
