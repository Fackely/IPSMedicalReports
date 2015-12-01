package com.princetonsa.dto.facturasVarias;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * 
 * DTO Recibos de Caja 
 * @author Víctor Gómez L.
 *
 */
@SuppressWarnings("serial")
public class DtoRecibosCaja implements Serializable{
	
	private String consecutivo;
	private String nroFactura;
	private String fecha;
	private String hora;
	private String saldoFactura;
	private String codDeudor;
	private String deudor;
	private String identificacionDeudor;
	private String conceptoFactura;
	private String multasCitas;
	
	//Campos Adicionales para consulta de Anexo 764
	private String tipoDocumento;
	private String nombreDeudor;
	private String tipoDeudor;
	private double valorDocumento;
	private double saldo;
	private double totalAplicacion;
	
	private String contabilizado;
	
	
	
	private String tipoIdentificacion;
	
	
	/**
	 * ATRIBUTO  QUE SETTEA 'S' SI Y SOLO SI, SE PUEDE CREAR UN NUEVO RECIBO DE CAJA; EN OTRO CASO SETTEA 'N'.
	 *  
	 */
	private String aplicaReciboCaja;
	
	/**
	 * Almacenar la persona de quien se recibe el recibo de caja, a manera informativa
	 */
	private String recibidoDe;
	
	/**
	 * Centro de atención donde se genera el recibo de caja
	 */
	private int centroAtencion;
	
	/**
	 * Constructor
	 */
	public DtoRecibosCaja()
	{
		this.reset();
	}
	
	/**
	 * Reset
	 */
	public void reset()
	{
		this.consecutivo = "" ;
		this.nroFactura = "" ;
		this.fecha = "" ;
		this.saldoFactura = "" ;
		this.codDeudor = "" ;
		this.deudor = "" ;
		this.tipoDeudor = "" ;
		this.identificacionDeudor = "" ;
		this.conceptoFactura = "" ;
		this.multasCitas = "";
		this.tipoDocumento="";
		this.nombreDeudor="";
		this.valorDocumento=0;
		this.saldo=0;
		this.totalAplicacion=0;
		this.contabilizado="";
		this.setAplicaReciboCaja(ConstantesBD.acronimoNo);
		this.tipoIdentificacion="";
		this.recibidoDe="";
		this.centroAtencion=ConstantesBD.codigoNuncaValido;
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
	 * @return the nroFactura
	 */
	public String getNroFactura() {
		return nroFactura;
	}

	/**
	 * @param nroFactura the nroFactura to set
	 */
	public void setNroFactura(String nroFactura) {
		this.nroFactura = nroFactura;
	}

	/**
	 * @return the fecha
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return the saldoFactura
	 */
	public String getSaldoFactura() {
		return saldoFactura;
	}

	/**
	 * @param saldoFactura the saldoFactura to set
	 */
	public void setSaldoFactura(String saldoFactura) {
		this.saldoFactura = saldoFactura;
	}

	/**
	 * @return the deudor
	 */
	public String getDeudor() {
		return deudor;
	}

	/**
	 * @param deudor the deudor to set
	 */
	public void setDeudor(String deudor) {
		this.deudor = deudor;
	}

	/**
	 * @return the identificacionDeudor
	 */
	public String getIdentificacionDeudor() {
		return identificacionDeudor;
	}

	/**
	 * @param identificacionDeudor the identificacionDeudor to set
	 */
	public void setIdentificacionDeudor(String identificacionDeudor) {
		this.identificacionDeudor = identificacionDeudor;
	}

	/**
	 * @return the conceptoFactura
	 */
	public String getConceptoFactura() {
		return conceptoFactura;
	}

	/**
	 * @param conceptoFactura the conceptoFactura to set
	 */
	public void setConceptoFactura(String conceptoFactura) {
		this.conceptoFactura = conceptoFactura;
	}

	/**
	 * @return the multasCitas
	 */
	public String getMultasCitas() {
		return multasCitas;
	}

	/**
	 * @param multasCitas the multasCitas to set
	 */
	public void setMultasCitas(String multasCitas) {
		this.multasCitas = multasCitas;
	}

	/**
	 * @return the codDeudor
	 */
	public String getCodDeudor() {
		return codDeudor;
	}

	/**
	 * @param codDeudor the codDeudor to set
	 */
	public void setCodDeudor(String codDeudor) {
		this.codDeudor = codDeudor;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getNombreDeudor() {
		return nombreDeudor;
	}

	public void setNombreDeudor(String nombreDeudor) {
		this.nombreDeudor = nombreDeudor;
	}

	public double getValorDocumento() {
		return valorDocumento;
	}

	public void setValorDocumento(double valorDocumento) {
		this.valorDocumento = valorDocumento;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	public double getTotalAplicacion() {
		return totalAplicacion;
	}

	public void setTotalAplicacion(double totalAplicacion) {
		this.totalAplicacion = totalAplicacion;
	}

	public String getContabilizado() {
		return contabilizado;
	}

	public void setContabilizado(String contabilizado) {
		this.contabilizado = contabilizado;
	}

	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	public void setAplicaReciboCaja(String aplicaReciboCaja) {
		this.aplicaReciboCaja = aplicaReciboCaja;
	}

	public String getAplicaReciboCaja() {
		return aplicaReciboCaja;
	}

	public String getRecibidoDe() {
		return recibidoDe;
	}

	public void setRecibidoDe(String recibidoDe) {
		this.recibidoDe = recibidoDe;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public int getCentroAtencion() {
		return centroAtencion;
	}

	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the tipoDeudor
	 */
	public String getTipoDeudor() {
		return tipoDeudor;
	}

	/**
	 * @param tipoDeudor the tipoDeudor to set
	 */
	public void setTipoDeudor(String tipoDeudor) {
		this.tipoDeudor = tipoDeudor;
	}
	
	
}
