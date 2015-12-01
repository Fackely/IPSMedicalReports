package com.princetonsa.dto.enfermeria;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;
import util.InfoDatos;

public class DtoCuidadosEnfermeria implements Serializable
{
	private int codigoPkProgramacion;
	
	private int codigoPkFrecCuidadoEnfer;
	
	private String fechaInicio;
	
	private String horaInicio;
	
	private String observaciones;
	
	private String usuarioProgramacion;	
	
	private int codigoCuidadoEnferCcInst;
	
	private String nombreCuidado;
	
	private int codigoTipoFrecuenciaPer;
	
	private String nombreTipoFrecuenciaPer;
	
	private int codigoFrecuencia;
	
	private int codigoTipoFrecuencia;
	
	private ArrayList<DtoDetalleCuidadosEnfermeria> detalleCuidados;
	
	private DtoFrecuenciaCuidadoEnferia frecuenciaCuidadoEnfer;
	
	private boolean activo;
	
	private InfoDatos infoDatosCuidados;
	
	/**
	 * 
	 */
	public void clean()
	{
		this.codigoPkProgramacion = ConstantesBD.codigoNuncaValido;
		this.codigoPkFrecCuidadoEnfer = ConstantesBD.codigoNuncaValido;
		this.fechaInicio = "";
		this.horaInicio = "";
		this.observaciones = "";
		this.usuarioProgramacion = "";
		this.activo = true;		
		
		this.codigoCuidadoEnferCcInst = ConstantesBD.codigoNuncaValido;		
		this.fechaInicio="";
		this.horaInicio="";		
		this.codigoTipoFrecuencia=ConstantesBD.codigoNuncaValido;
		this.nombreTipoFrecuenciaPer="";
		this.observaciones="";
		this.codigoFrecuencia = ConstantesBD.codigoNuncaValido;
		this.codigoTipoFrecuencia = ConstantesBD.codigoNuncaValido;		
		this.detalleCuidados= new ArrayList<DtoDetalleCuidadosEnfermeria>();
		this.frecuenciaCuidadoEnfer = new DtoFrecuenciaCuidadoEnferia();
		this.nombreCuidado = "";
	}
	
	
	/**
	 * 
	 */
	public DtoCuidadosEnfermeria()
	{
		this.clean();
	}


	/**
	 * @return the codigoPkProgramacion
	 */
	public int getCodigoPkProgramacion() {
		return codigoPkProgramacion;
	}


	/**
	 * @param codigoPkProgramacion the codigoPkProgramacion to set
	 */
	public void setCodigoPkProgramacion(int codigoPkProgramacion) {
		this.codigoPkProgramacion = codigoPkProgramacion;
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
	 * @return the horaInicio
	 */
	public String getHoraInicio() {
		return horaInicio;
	}


	/**
	 * @param horaInicio the horaInicio to set
	 */
	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}

	/**
	 * @return the codigoTipoFrecuenciaPer
	 */
	public int getCodigoTipoFrecuenciaPer() {
		return codigoTipoFrecuenciaPer;
	}


	/**
	 * @param codigoTipoFrecuenciaPer the codigoTipoFrecuenciaPer to set
	 */
	public void setCodigoTipoFrecuenciaPer(int codigoTipoFrecuenciaPer) {
		this.codigoTipoFrecuenciaPer = codigoTipoFrecuenciaPer;
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
	 * @return the codigoFrecuencia
	 */
	public int getCodigoFrecuencia() {
		return codigoFrecuencia;
	}


	/**
	 * @param codigoFrecuencia the codigoFrecuencia to set
	 */
	public void setCodigoFrecuencia(int codigoFrecuencia) {
		this.codigoFrecuencia = codigoFrecuencia;
	}


	/**
	 * @return the codigoTipoFrecuencia
	 */
	public int getCodigoTipoFrecuencia() {
		return codigoTipoFrecuencia;
	}


	/**
	 * @param codigoTipoFrecuencia the codigoTipoFrecuencia to set
	 */
	public void setCodigoTipoFrecuencia(int codigoTipoFrecuencia) {
		this.codigoTipoFrecuencia = codigoTipoFrecuencia;
	}


	/**
	 * @return the detalleCuidados
	 */
	public ArrayList<DtoDetalleCuidadosEnfermeria> getDetalleCuidados() {
		return detalleCuidados;
	}


	/**
	 * @param detalleCuidados the detalleCuidados to set
	 */
	public void setDetalleCuidados(
			ArrayList<DtoDetalleCuidadosEnfermeria> detalleCuidados) {
		this.detalleCuidados = detalleCuidados;
	}

	/**
	 * @return the nombreTipoFrecuenciaPer
	 */
	public String getNombreTipoFrecuenciaPer() {
		return nombreTipoFrecuenciaPer;
	}


	/**
	 * @param nombreTipoFrecuenciaPer the nombreTipoFrecuenciaPer to set
	 */
	public void setNombreTipoFrecuenciaPer(String nombreTipoFrecuenciaPer) {
		this.nombreTipoFrecuenciaPer = nombreTipoFrecuenciaPer;
	}


	/**
	 * @return the codigoPkFrecCuidadoEnfer
	 */
	public int getCodigoPkFrecCuidadoEnfer() {
		return codigoPkFrecCuidadoEnfer;
	}


	/**
	 * @param codigoPkFrecCuidadoEnfer the codigoPkFrecCuidadoEnfer to set
	 */
	public void setCodigoPkFrecCuidadoEnfer(int codigoPkFrecCuidadoEnfer) {
		this.codigoPkFrecCuidadoEnfer = codigoPkFrecCuidadoEnfer;
	}


	/**
	 * @return the usuarioProgramacion
	 */
	public String getUsuarioProgramacion() {
		return usuarioProgramacion;
	}


	/**
	 * @param usuarioProgramacion the usuarioProgramacion to set
	 */
	public void setUsuarioProgramacion(String usuarioProgramacion) {
		this.usuarioProgramacion = usuarioProgramacion;
	}


	/**
	 * @return the activo
	 */
	public boolean isActivo() {
		return activo;
	}


	/**
	 * @param activo the activo to set
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
	}


	/**
	 * @return the nombreCuidado
	 */
	public String getNombreCuidado() {
		return nombreCuidado;
	}


	/**
	 * @param nombreCuidado the nombreCuidado to set
	 */
	public void setNombreCuidado(String nombreCuidado) {
		this.nombreCuidado = nombreCuidado;
	}


	/**
	 * @return the frecuenciaCuidadoEnfer
	 */
	public DtoFrecuenciaCuidadoEnferia getFrecuenciaCuidadoEnfer() {
		return frecuenciaCuidadoEnfer;
	}


	/**
	 * @param frecuenciaCuidadoEnfer the frecuenciaCuidadoEnfer to set
	 */
	public void setFrecuenciaCuidadoEnfer(
			DtoFrecuenciaCuidadoEnferia frecuenciaCuidadoEnfer) {
		this.frecuenciaCuidadoEnfer = frecuenciaCuidadoEnfer;
	}


	/**
	 * @return the infoDatosCuidados
	 */
	public InfoDatos getInfoDatosCuidados() {
		return infoDatosCuidados;
	}


	/**
	 * @param infoDatosCuidados the infoDatosCuidados to set
	 */
	public void setInfoDatosCuidados(InfoDatos infoDatosCuidados) {
		this.infoDatosCuidados = infoDatosCuidados;
	}

}