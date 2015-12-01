package com.princetonsa.dto.interfaz;

import java.io.Serializable;

import util.ConstantesBD;

public class DtoMovimientoTipoDocumento implements Serializable
{
	private String tipoConenio;
	
	private double valorFacturacion;
	
	private double valorAnulacion;
	
	private String centroAtencion;
	
	private String clasificacion;
	
	private String grupoServicio;
	
	private double valorIngreso;
	
	private String concepto;
	
	private int codigoConcepto;
	
	private double valorAjusteDebito;
	
	private double valorAjusteCredito;
	
	private String formaPago;
	
	private double valorRecibo;
	
	
	public DtoMovimientoTipoDocumento()
	{
		reset();
	}
	
	public void reset()
	{
		this.tipoConenio = "";
		this.valorFacturacion = ConstantesBD.codigoNuncaValidoDouble;
		this.valorAnulacion = ConstantesBD.codigoNuncaValidoDouble;
		this.centroAtencion = "";
		this.clasificacion = "";
		this.grupoServicio = "";
		this.valorIngreso = ConstantesBD.codigoNuncaValidoDouble;
		this.concepto = "";
		this.codigoConcepto = ConstantesBD.codigoNuncaValido;
		this.valorAjusteDebito = ConstantesBD.codigoNuncaValidoDouble;
		this.valorAjusteCredito = ConstantesBD.codigoNuncaValidoDouble;
		this.formaPago = "";
		this.valorRecibo = ConstantesBD.codigoNuncaValidoDouble;
	}

	/**
	 * @return the tipoConenio
	 */
	public String getTipoConenio() {
		return tipoConenio;
	}

	/**
	 * @param tipoConenio the tipoConenio to set
	 */
	public void setTipoConenio(String tipoConenio) {
		this.tipoConenio = tipoConenio;
	}

	/**
	 * @return the valorFacturacion
	 */
	public double getValorFacturacion() {
		return valorFacturacion;
	}

	/**
	 * @param valorFacturacion the valorFacturacion to set
	 */
	public void setValorFacturacion(double valorFacturacion) {
		this.valorFacturacion = valorFacturacion;
	}

	/**
	 * @return the valorAnulacion
	 */
	public double getValorAnulacion() {
		return valorAnulacion;
	}

	/**
	 * @param valorAnulacion the valorAnulacion to set
	 */
	public void setValorAnulacion(double valorAnulacion) {
		this.valorAnulacion = valorAnulacion;
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
	 * @return the clasificacion
	 */
	public String getClasificacion() {
		return clasificacion;
	}

	/**
	 * @param clasificacion the clasificacion to set
	 */
	public void setClasificacion(String clasificacion) {
		this.clasificacion = clasificacion;
	}

	/**
	 * @return the grupoServicio
	 */
	public String getGrupoServicio() {
		return grupoServicio;
	}

	/**
	 * @param grupoServicio the grupoServicio to set
	 */
	public void setGrupoServicio(String grupoServicio) {
		this.grupoServicio = grupoServicio;
	}

	/**
	 * @return the valorIngreso
	 */
	public double getValorIngreso() {
		return valorIngreso;
	}

	/**
	 * @param valorIngreso the valorIngreso to set
	 */
	public void setValorIngreso(double valorIngreso) {
		this.valorIngreso = valorIngreso;
	}

	/**
	 * @return the concepto
	 */
	public String getConcepto() {
		return concepto;
	}

	/**
	 * @param concepto the concepto to set
	 */
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	/**
	 * @return the codigoConcepto
	 */
	public int getCodigoConcepto() {
		return codigoConcepto;
	}

	/**
	 * @param codigoConcepto the codigoConcepto to set
	 */
	public void setCodigoConcepto(int codigoConcepto) {
		this.codigoConcepto = codigoConcepto;
	}

	/**
	 * @return the valorAjusteDebito
	 */
	public double getValorAjusteDebito() {
		return valorAjusteDebito;
	}

	/**
	 * @param valorAjusteDebito the valorAjusteDebito to set
	 */
	public void setValorAjusteDebito(double valorAjusteDebito) {
		this.valorAjusteDebito = valorAjusteDebito;
	}

	/**
	 * @return the valorAjusteCredito
	 */
	public double getValorAjusteCredito() {
		return valorAjusteCredito;
	}

	/**
	 * @param valorAjusteCredito the valorAjusteCredito to set
	 */
	public void setValorAjusteCredito(double valorAjusteCredito) {
		this.valorAjusteCredito = valorAjusteCredito;
	}

	/**
	 * @return the formaPago
	 */
	public String getFormaPago() {
		return formaPago;
	}

	/**
	 * @param formaPago the formaPago to set
	 */
	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}

	/**
	 * @return the valorRecibo
	 */
	public double getValorRecibo() {
		return valorRecibo;
	}

	/**
	 * @param valorRecibo the valorRecibo to set
	 */
	public void setValorRecibo(double valorRecibo) {
		this.valorRecibo = valorRecibo;
	}

	
}