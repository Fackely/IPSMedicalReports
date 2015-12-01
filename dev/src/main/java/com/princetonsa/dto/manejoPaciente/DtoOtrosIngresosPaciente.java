package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;

import util.ValoresPorDefecto;

/**
 * @author Jose Eduardo Arias Doncel
 * jeaddoncel@princetonsa.com
 * */
public class DtoOtrosIngresosPaciente implements Serializable
{

	/**
	 * Versión serial
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Codigo del Centro de Atencion
	 * */	
	private String centroAtencion;
	
	/**
	 * Descripcion del centro de Atencion 
	 * */
	private String descripcionCentroAtencion;
	
	/**
	 * No Ingreso
	 * */
	private String ingreso;
	
	/**
	 * Consecutivo del Ingreso
	 * */
	private String ingresoConsecutivo;
	
	/**
	 * Presingreso del Ingreso
	 */
	private String preingreso;
	
	/**
	 * Reingreso del Ingreso
	 */
	private int reingreso;
		
	/**
	 * Institucion asociada al Ingreso
	 * */
	private String institucion;
	
	/**
	 * Año del Consecutivo 
	 * */
	private String anioConsecutivo;
	
	/**
	 * Estado del Ingreso
	 * */
	private String estadoIngreso;
	/**
	 * NombreINgres
	 */
	@SuppressWarnings("unused")
	private String nombreEstadoIngresoAyudante;
	
	/**
	 * Nombre Estado del Ingreso
	 * */
	private String nombreEstadoingreso;
	
	/**
	 * Codigo del Paciente
	 * */
	private String codigoPaciente;
	
	/**
	 * Fecha apertura Ingreso
	 * */
	private String fechaAperturaIngreso;
	
	/**
	 * Fecha cierre Ingreso
	 * */
	private String fechaCierreIngreso;
	
	/**
	 * NumeroCuenta  
	 * */
	private String numeroCuenta;
	
	/**
	 * Estado de la Cuenta
	 * */
	
	private String fechaIngreso;
	
	/**
	 * 
	 */
	private String horaIngreso;
	/**
	 * 
	 * 
	 * 
	 */
	
	private String estadoCuenta;
	
	/**
	 * Nombre Estado de la Cuenta
	 * */
	private String nombreEstadoCuenta;
	
	/**
	 * Via de Ingreso de la Cuenta
	 * */
	private String viaIngresoCuenta;
	
	/**
	 * Nombre Via del Ingreso
	 * */
	private String nombreViaIngreso;
	
	
	/**
	 * Inicialiaza los valores de la Forma
	 * */
	public void reset()
	{
		
		this.centroAtencion 		= "";
		this.descripcionCentroAtencion = "";
		this.ingreso      			= "";
		this.ingresoConsecutivo		= "";
		this.preingreso				= "";
		this.reingreso				= 0;
		this.anioConsecutivo		= "";
		this.estadoIngreso 			= "";
		this.nombreEstadoingreso	= "";
		this.fechaAperturaIngreso 	= "";
		this.fechaCierreIngreso 	= "";
		this.numeroCuenta 			= "";
		this.estadoCuenta			= "";
		this.nombreEstadoCuenta		= "";
		this.viaIngresoCuenta 		= "";
		this.codigoPaciente 		= "";
		this.nombreViaIngreso 		= "";
		this.fechaIngreso ="";
		this.horaIngreso = "";
		this.nombreEstadoIngresoAyudante="";
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
	 * @return the fechaAperturaIngreso
	 */
	public String getFechaAperturaIngreso() {
		return fechaAperturaIngreso;
	}


	/**
	 * @param fechaAperturaIngreso the fechaAperturaIngreso to set
	 */
	public void setFechaAperturaIngreso(String fechaAperturaIngreso) {
		this.fechaAperturaIngreso = fechaAperturaIngreso;
	}


	/**
	 * @return the fechaCierreIngreso
	 */
	public String getFechaCierreIngreso() {
		return fechaCierreIngreso;
	}


	/**
	 * @param fechaCierreIngreso the fechaCierreIngreso to set
	 */
	public void setFechaCierreIngreso(String fechaCierreIngreso) {
		this.fechaCierreIngreso = fechaCierreIngreso;
	}


	/**
	 * @return the noIngreso
	 */
	public String getIngreso() {
		return ingreso;
	}


	/**
	 * @param noIngreso the noIngreso to set
	 */
	public void setIngreso(String ingreso) {
		this.ingreso = ingreso;
	}


	/**
	 * @return the numeroCuenta
	 */
	public String getNumeroCuenta() {
		return numeroCuenta;
	}


	/**
	 * @param numeroCuenta the numeroCuenta to set
	 */
	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}


	/**
	 * @return the viaIngresoCuenta
	 */
	public String getViaIngresoCuenta() {
		return viaIngresoCuenta;
	}


	/**
	 * @param viaIngresoCuenta the viaIngresoCuenta to set
	 */
	public void setViaIngresoCuenta(String viaIngresoCuenta) {
		this.viaIngresoCuenta = viaIngresoCuenta;
	}


	/**
	 * @return the descripcionCentroAtencion
	 */
	public String getDescripcionCentroAtencion() {
		return descripcionCentroAtencion;
	}


	/**
	 * @param descripcionCentroAtencion the descripcionCentroAtencion to set
	 */
	public void setDescripcionCentroAtencion(String descripcionCentroAtencion) {
		this.descripcionCentroAtencion = descripcionCentroAtencion;
	}


	/**
	 * @return the codigoPaciente
	 */
	public String getCodigoPaciente() {
		return codigoPaciente;
	}


	/**
	 * @param codigoPaciente the codigoPaciente to set
	 */
	public void setCodigoPaciente(String codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}


	/**
	 * @return the nombreEstadoCuenta
	 */
	public String getNombreEstadoCuenta() {
		return nombreEstadoCuenta;
	}


	/**
	 * @param nombreEstadoCuenta the nombreEstadoCuenta to set
	 */
	public void setNombreEstadoCuenta(String nombreEstadoCuenta) {
		this.nombreEstadoCuenta = nombreEstadoCuenta;
	}


	/**
	 * @return the nombreEstadoingreso
	 */
	public String getNombreEstadoingreso() {
		return nombreEstadoingreso;
	}


	/**
	 * @param nombreEstadoingreso the nombreEstadoingreso to set
	 */
	public void setNombreEstadoingreso(String nombreEstadoingreso) {
		this.nombreEstadoingreso = nombreEstadoingreso;
	}


	/**
	 * @return the nombreViaIngreso
	 */
	public String getNombreViaIngreso() {
		return nombreViaIngreso;
	}


	/**
	 * @param nombreViaIngreso the nombreViaIngreso to set
	 */
	public void setNombreViaIngreso(String nombreViaIngreso) {
		this.nombreViaIngreso = nombreViaIngreso;
	}


	/**
	 * @return the anioConsecutivo
	 */
	public String getAnioConsecutivo() {
		return anioConsecutivo;
	}


	/**
	 * @param anioConsecutivo the anioConsecutivo to set
	 */
	public void setAnioConsecutivo(String anioConsecutivo) {
		this.anioConsecutivo = anioConsecutivo;
	}


	/**
	 * @return the ingresoConsecutivo
	 */
	public String getIngresoConsecutivo() {
		return ingresoConsecutivo;
	}


	/**
	 * @param ingresoConsecutivo the ingresoConsecutivo to set
	 */
	public void setIngresoConsecutivo(String ingresoConsecutivo) {
		this.ingresoConsecutivo = ingresoConsecutivo;
	}


	/**
	 * @return the institucion
	 */
	public String getInstitucion() {
		return institucion;
	}


	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}


	public String getPreingreso() {
		return preingreso;
	}


	public void setPreingreso(String preingreso) {
		this.preingreso = preingreso;
	}


	public int getReingreso() {
		return reingreso;
	}


	public void setReingreso(int reingreso) {
		this.reingreso = reingreso;
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
	 * @return the horaIngreso
	 */
	public String getHoraIngreso() {
		return horaIngreso;
	}


	/**
	 * @param horaIngreso the horaIngreso to set
	 */
	public void setHoraIngreso(String horaIngreso) {
		this.horaIngreso = horaIngreso;
	}


	public void setNombreEstadoIngresoAyudante(
			String nombreEstadoIngresoAyudante) {
		this.nombreEstadoIngresoAyudante = nombreEstadoIngresoAyudante;
	}


	public String getNombreEstadoIngresoAyudante() {
		return ValoresPorDefecto.getIntegridadDominio(this.estadoIngreso)+"" ;
	}
	
}