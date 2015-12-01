package com.princetonsa.dto.enfermeria;

import java.io.Serializable;

import util.ConstantesBD;

public class DtoFrecuenciaCuidadoEnferia implements Serializable{
	
	private String nombrePaciente;
	
	private int codigoPk;
	
	private int codigoCuidadoEnferCcInst;
	
	private int codigoOtroCuidado;
	
	private int frecuencia;
	
	private int tipoFrecuencia;
	
	private String activo;
	
	private int periodo;
	
	private int tipoFrecuenciaPeriodo;	
	
	private int codigoIngreso;
	
	private String nombreCuidadoEnfer;
	
	private int codigoCuidadoEnfermeria;	
	
	private String nombreTipoFrecuencia;
	
	private String nombreTipoFrecuPeriodo;
	
	private boolean vieneRegistroEnfermeria = false;
	
	private boolean esOtroCuidado = false;
	
	private boolean tieneProgramacion;
	
	private String seleccionado;
	
	//Se utilizan al momento de programar ---	
	private String fechaInicioProgramacion;
	
	private String horaInicioProgramacion;
	
	private String observaciones;	
	//---
	
	public void clean()
	{
		this.codigoPk = ConstantesBD.codigoNuncaValido;
		this.activo = ConstantesBD.acronimoNo;		
		this.codigoIngreso = ConstantesBD.codigoNuncaValido;
		this.codigoCuidadoEnferCcInst = ConstantesBD.codigoNuncaValido;
		this.nombreCuidadoEnfer = "";
		this.frecuencia = ConstantesBD.codigoNuncaValido;
		this.tipoFrecuencia = ConstantesBD.codigoNuncaValido;
		this.nombreTipoFrecuencia = "";
		this.vieneRegistroEnfermeria = false;
		this.esOtroCuidado = false;
		this.codigoCuidadoEnfermeria = ConstantesBD.codigoNuncaValido;
		this.periodo = ConstantesBD.codigoNuncaValido;
		this.tipoFrecuenciaPeriodo = ConstantesBD.codigoNuncaValido;
		this.nombreTipoFrecuPeriodo = "";
		this.codigoOtroCuidado = ConstantesBD.codigoNuncaValido;
		this.tieneProgramacion = false;
		this.fechaInicioProgramacion = "";
		this.horaInicioProgramacion = "";
		this.observaciones = "";
		this.seleccionado="";
		this.nombrePaciente = "";
	}	
	
	/**
	 * 
	 * */
	public DtoFrecuenciaCuidadoEnferia()
	{
		clean();		
	}

	/**
	 * @return the codigoCuidadoEnferCcInst
	 */
	public int getCodigoCuidadoEnferCcInst() {
		return codigoCuidadoEnferCcInst;
	}

	/**
	 * @param codigoCuidadoEnferCcInst the codigoCuidadoEnferCcInst to set
	 */
	public void setCodigoCuidadoEnferCcInst(int codigoCuidadoEnferCcInst) {
		this.codigoCuidadoEnferCcInst = codigoCuidadoEnferCcInst;
	}
	
	/**
	 * @return the frecuencia
	 */
	public int getFrecuencia() {
		return frecuencia;
	}

	/**
	 * @param frecuencia the frecuencia to set
	 */
	public void setFrecuencia(int frecuencia) {
		this.frecuencia = frecuencia;
	}

	/**
	 * @return the tipoFrecuencia
	 */
	public int getTipoFrecuencia() {
		return tipoFrecuencia;
	}

	/**
	 * @param tipoFrecuencia the tipoFrecuencia to set
	 */
	public void setTipoFrecuencia(int tipoFrecuencia) {
		this.tipoFrecuencia = tipoFrecuencia;
	}

	/**
	 * @return the vieneRegistroEnfermeria
	 */
	public boolean isVieneRegistroEnfermeria() {
		return vieneRegistroEnfermeria;
	}

	/**
	 * @param vieneRegistroEnfermeria the vieneRegistroEnfermeria to set
	 */
	public void setVieneRegistroEnfermeria(boolean vieneRegistroEnfermeria) {
		this.vieneRegistroEnfermeria = vieneRegistroEnfermeria;
	}

	
	/**
	 * @return the nombreTipoFrecuencia
	 */
	public String getNombreTipoFrecuencia() {
		return nombreTipoFrecuencia;
	}

	/**
	 * @param nombreTipoFrecuencia the nombreTipoFrecuencia to set
	 */
	public void setNombreTipoFrecuencia(String nombreTipoFrecuencia) {
		this.nombreTipoFrecuencia = nombreTipoFrecuencia;
	}
	
	/**
	 * @return the codigoPk
	 */
	public int getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}
	
	/**
	 * @return the activo
	 */
	public String getActivo() {
		return activo;
	}

	/**
	 * @param activo the activo to set
	 */
	public void setActivo(String activo) {
		this.activo = activo;
	}

	/**
	 * @return the codigoCuidadoEnfermeria
	 */
	public int getCodigoCuidadoEnfermeria() {
		return codigoCuidadoEnfermeria;
	}

	/**
	 * @param codigoCuidadoEnfermeria the codigoCuidadoEnfermeria to set
	 */
	public void setCodigoCuidadoEnfermeria(int codigoCuidadoEnfermeria) {
		this.codigoCuidadoEnfermeria = codigoCuidadoEnfermeria;
	}

	/**
	 * @return the periodo
	 */
	public int getPeriodo() {
		return periodo;
	}

	/**
	 * @param periodo the periodo to set
	 */
	public void setPeriodo(int periodo) {
		this.periodo = periodo;
	}
	
	/**
	 * @return the codigoOtroCuidado
	 */
	public int getCodigoOtroCuidado() {
		return codigoOtroCuidado;
	}

	/**
	 * @param codigoOtroCuidado the codigoOtroCuidado to set
	 */
	public void setCodigoOtroCuidado(int codigoOtroCuidado) {
		this.codigoOtroCuidado = codigoOtroCuidado;
	}

	/**
	 * @return the tipoFrecuenciaPeriodo
	 */
	public int getTipoFrecuenciaPeriodo() {
		return tipoFrecuenciaPeriodo;
	}

	/**
	 * @param tipoFrecuenciaPeriodo the tipoFrecuenciaPeriodo to set
	 */
	public void setTipoFrecuenciaPeriodo(int tipoFrecuenciaPeriodo) {
		this.tipoFrecuenciaPeriodo = tipoFrecuenciaPeriodo;
	}

	/**
	 * @return the nombreCuidadoEnfer
	 */
	public String getNombreCuidadoEnfer() {
		return nombreCuidadoEnfer;
	}

	/**
	 * @param nombreCuidadoEnfer the nombreCuidadoEnfer to set
	 */
	public void setNombreCuidadoEnfer(String nombreCuidadoEnfer) {
		this.nombreCuidadoEnfer = nombreCuidadoEnfer;
	}

	/**
	 * @return the esOtroCuidado
	 */
	public boolean isEsOtroCuidado() {
		return esOtroCuidado;
	}

	/**
	 * @param esOtroCuidado the esOtroCuidado to set
	 */
	public void setEsOtroCuidado(boolean esOtroCuidado) {
		this.esOtroCuidado = esOtroCuidado;
	}

	/**
	 * @return the codigoIngreso
	 */
	public int getCodigoIngreso() {
		return codigoIngreso;
	}

	/**
	 * @param codigoIngreso the codigoIngreso to set
	 */
	public void setCodigoIngreso(int codigoIngreso) {
		this.codigoIngreso = codigoIngreso;
	}

	/**
	 * @return the tieneProgramacion
	 */
	public boolean isTieneProgramacion() {
		return tieneProgramacion;
	}

	/**
	 * @param tieneProgramacion the tieneProgramacion to set
	 */
	public void setTieneProgramacion(boolean tieneProgramacion) {
		this.tieneProgramacion = tieneProgramacion;
	}

	/**
	 * @return the fechaInicioProgramacion
	 */
	public String getFechaInicioProgramacion() {
		return fechaInicioProgramacion;
	}

	/**
	 * @param fechaInicioProgramacion the fechaInicioProgramacion to set
	 */
	public void setFechaInicioProgramacion(String fechaInicioProgramacion) {
		this.fechaInicioProgramacion = fechaInicioProgramacion;
	}

	/**
	 * @return the horaInicioProgramacion
	 */
	public String getHoraInicioProgramacion() {
		return horaInicioProgramacion;
	}

	/**
	 * @param horaInicioProgramacion the horaInicioProgramacion to set
	 */
	public void setHoraInicioProgramacion(String horaInicioProgramacion) {
		this.horaInicioProgramacion = horaInicioProgramacion;
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
	 * @return the nombreTipoFrecuPeriodo
	 */
	public String getNombreTipoFrecuPeriodo() {
		return nombreTipoFrecuPeriodo;
	}

	/**
	 * @param nombreTipoFrecuPeriodo the nombreTipoFrecuPeriodo to set
	 */
	public void setNombreTipoFrecuPeriodo(String nombreTipoFrecuPeriodo) {
		this.nombreTipoFrecuPeriodo = nombreTipoFrecuPeriodo;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSeleccionado() {
		return seleccionado;
	}

	/**
	 * 
	 * @param seleccionado
	 */
	public void setSeleccionado(String seleccionado) {
		this.seleccionado = seleccionado;
	}

	public String getNombrePaciente() {
		return nombrePaciente;
	}

	public void setNombrePaciente(String nombrePaciente) {
		this.nombrePaciente = nombrePaciente;
	}
	
}