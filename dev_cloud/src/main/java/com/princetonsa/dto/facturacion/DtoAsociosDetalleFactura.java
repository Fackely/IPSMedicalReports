package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;

/**
 * 
 * @author wilson
 *
 */
public class DtoAsociosDetalleFactura implements Serializable
{
	/**
	 * 
	 */
	private int codigo;
	
	/**
	 * 
	 */
	private int consecutivo;
	
	/**
	 * 
	 */
	private int codigoServicioAsocio;
	
	/**
	 * 
	 */
	private double valorAsocio;
	
	/**
	 * 
	 */
	private int tipoAsocio;
	
	/**
	 * 
	 */
	private String tipoServicio;
	
	/**
	 * 
	 */
	private String nombreTipoAsocio;
	
	/**
	 * 
	 */
	private double ajusteCreditoMedico;
	
	/**
	 * 
	 */
	private double ajusteDebitoMedico;
	
	/**
	 * 
	 */
	private int porcentajePool;
	
	/**
	 * Variable que almacena el porcentaje en 'double' por incidencia Mantis 849 por calculo de valor honorarios Médico 
	 */
	private double porcentajePoolDouble;
	
	/**
	 * 
	 */
	private double porcentajeMedico;
	
	/**
	 * 
	 */
	private double valorMedico;
	
	/**
	 * 
	 */
	private double valorCargo;
	
	/**
	 * 
	 */
	private double valorIva;
	
	/**
	 * 
	 */
	private double valorRecargo;
	
	/**
	 * 
	 */
	private double valorTotal;
	
	/**
	 * 
	 */
	private double ajustesCredito;
	
	/**
	 * 
	 */
	private double ajustesDebito;
	
	/**
	 * 
	 */
	private int codigoEsquemaTarifario;
	
	/**
	 * 
	 */
	private double valorPool;
	
	/**
	 * 
	 */
	private int pool;
	
	/**
	 * 
	 */
	private int codigoMedico;

	/**
	 * 
	 */
	private double codigoDetalleCargo;
	
	/**
	 * 
	 */
	private int codigoMedicoAsocio;
	
	/**
	 * 
	 */
	private int codigoEspecialidadMedicoAsocio;
	
	/**
	 * 
	 */
	private String codigoPropietarioAsocio;
	
	/**
	 * 
	 */
	private ArrayList<DtoDetAsociosDetFactura> detalleAsociosArray= new ArrayList<DtoDetAsociosDetFactura>();
	
	/**
	 * 
	 */
	public DtoAsociosDetalleFactura() 
	{
		super();
		this.codigo = ConstantesBD.codigoNuncaValido;
		this.codigoServicioAsocio = ConstantesBD.codigoNuncaValido;
		this.valorAsocio = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.tipoAsocio = ConstantesBD.codigoNuncaValido;
		this.nombreTipoAsocio="";
		this.ajusteCreditoMedico = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.ajusteDebitoMedico = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.porcentajePool = ConstantesBD.codigoNuncaValido;
		this.porcentajePoolDouble=ConstantesBD.codigoNuncaValidoDouble;
		this.porcentajeMedico = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.valorMedico = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.valorCargo = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.valorIva = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.valorRecargo = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.valorTotal = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.ajustesCredito = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.ajustesDebito = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.codigoEsquemaTarifario = ConstantesBD.codigoNuncaValido;
		this.valorPool = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.pool = ConstantesBD.codigoNuncaValido;
		this.codigoMedico = ConstantesBD.codigoNuncaValido;
		this.codigoDetalleCargo = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.codigoMedicoAsocio=ConstantesBD.codigoNuncaValido;
		this.codigoEspecialidadMedicoAsocio= ConstantesBD.codigoNuncaValido;
		this.detalleAsociosArray= new ArrayList<DtoDetAsociosDetFactura>();
		this.tipoServicio="";
		this.consecutivo=0;
		this.codigoPropietarioAsocio="";
	}

	
	
	public int getConsecutivo() {
		return consecutivo;
	}



	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
	}



	public String getTipoServicio() {
		return tipoServicio;
	}



	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}



	/**
	 * @return the ajusteCreditoMedico
	 */
	public double getAjusteCreditoMedico() {
		return ajusteCreditoMedico;
	}

	/**
	 * @param ajusteCreditoMedico the ajusteCreditoMedico to set
	 */
	public void setAjusteCreditoMedico(double ajusteCreditoMedico) {
		this.ajusteCreditoMedico = ajusteCreditoMedico;
	}

	/**
	 * @return the ajusteDebitoMedico
	 */
	public double getAjusteDebitoMedico() {
		return ajusteDebitoMedico;
	}

	/**
	 * @param ajusteDebitoMedico the ajusteDebitoMedico to set
	 */
	public void setAjusteDebitoMedico(double ajusteDebitoMedico) {
		this.ajusteDebitoMedico = ajusteDebitoMedico;
	}

	/**
	 * @return the ajustesCredito
	 */
	public double getAjustesCredito() {
		return ajustesCredito;
	}

	/**
	 * @param ajustesCredito the ajustesCredito to set
	 */
	public void setAjustesCredito(double ajustesCredito) {
		this.ajustesCredito = ajustesCredito;
	}

	/**
	 * @return the ajustesDebito
	 */
	public double getAjustesDebito() {
		return ajustesDebito;
	}

	/**
	 * @param ajustesDebito the ajustesDebito to set
	 */
	public void setAjustesDebito(double ajustesDebito) {
		this.ajustesDebito = ajustesDebito;
	}

	/**
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the codigoEsquemaTarifario
	 */
	public int getCodigoEsquemaTarifario() {
		return codigoEsquemaTarifario;
	}

	/**
	 * @param codigoEsquemaTarifario the codigoEsquemaTarifario to set
	 */
	public void setCodigoEsquemaTarifario(int codigoEsquemaTarifario) {
		this.codigoEsquemaTarifario = codigoEsquemaTarifario;
	}

	/**
	 * @return the codigoMedico
	 */
	public int getCodigoMedico() {
		return codigoMedico;
	}

	/**
	 * @param codigoMedico the codigoMedico to set
	 */
	public void setCodigoMedico(int codigoMedico) {
		this.codigoMedico = codigoMedico;
	}

	/**
	 * @return the codigoServicioAsocio
	 */
	public int getCodigoServicioAsocio() {
		return codigoServicioAsocio;
	}

	/**
	 * @param codigoServicioAsocio the codigoServicioAsocio to set
	 */
	public void setCodigoServicioAsocio(int codigoServicioAsocio) {
		this.codigoServicioAsocio = codigoServicioAsocio;
	}

	/**
	 * @return the pool
	 */
	public int getPool() {
		return pool;
	}

	/**
	 * @param pool the pool to set
	 */
	public void setPool(int pool) {
		this.pool = pool;
	}

	/**
	 * @return the porcentajeMedico
	 */
	public double getPorcentajeMedico() {
		return porcentajeMedico;
	}

	/**
	 * @param porcentajeMedico the porcentajeMedico to set
	 */
	public void setPorcentajeMedico(double porcentajeMedico) {
		this.porcentajeMedico = porcentajeMedico;
	}

	/**
	 * @return the porcentajePool
	 */
	public int getPorcentajePool() {
		return porcentajePool;
	}

	/**
	 * @param porcentajePool the porcentajePool to set
	 */
	public void setPorcentajePool(int porcentajePool) {
		this.porcentajePool = porcentajePool;
	}

	/**
	 * @return the tipoAsocio
	 */
	public int getTipoAsocio() {
		return tipoAsocio;
	}

	/**
	 * @param tipoAsocio the tipoAsocio to set
	 */
	public void setTipoAsocio(int tipoAsocio) {
		this.tipoAsocio = tipoAsocio;
	}

	/**
	 * @return the valorAsocio
	 */
	public double getValorAsocio() {
		return valorAsocio;
	}

	/**
	 * @param valorAsocio the valorAsocio to set
	 */
	public void setValorAsocio(double valorAsocio) {
		this.valorAsocio = valorAsocio;
	}

	/**
	 * @return the valorCargo
	 */
	public double getValorCargo() {
		return valorCargo;
	}

	/**
	 * @param valorCargo the valorCargo to set
	 */
	public void setValorCargo(double valorCargo) {
		this.valorCargo = valorCargo;
	}

	/**
	 * @return the valorIva
	 */
	public double getValorIva() {
		return valorIva;
	}

	/**
	 * @param valorIva the valorIva to set
	 */
	public void setValorIva(double valorIva) {
		this.valorIva = valorIva;
	}

	/**
	 * @return the valorMedico
	 */
	public double getValorMedico() {
		return valorMedico;
	}

	/**
	 * @param valorMedico the valorMedico to set
	 */
	public void setValorMedico(double valorMedico) {
		this.valorMedico = valorMedico;
	}

	/**
	 * @return the valorPool
	 */
	public double getValorPool() {
		return valorPool;
	}

	/**
	 * @param valorPool the valorPool to set
	 */
	public void setValorPool(double valorPool) {
		this.valorPool = valorPool;
	}

	/**
	 * @return the valorRecargo
	 */
	public double getValorRecargo() {
		return valorRecargo;
	}

	/**
	 * @param valorRecargo the valorRecargo to set
	 */
	public void setValorRecargo(double valorRecargo) {
		this.valorRecargo = valorRecargo;
	}

	/**
	 * @return the valorTotal
	 */
	public double getValorTotal() {
		return valorTotal;
	}

	/**
	 * @param valorTotal the valorTotal to set
	 */
	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}

	/**
	 * @return the nombreTipoAsocio
	 */
	public String getNombreTipoAsocio() {
		return nombreTipoAsocio;
	}

	/**
	 * @param nombreTipoAsocio the nombreTipoAsocio to set
	 */
	public void setNombreTipoAsocio(String nombreTipoAsocio) {
		this.nombreTipoAsocio = nombreTipoAsocio;
	}

	/**
	 * @return the codigoDetalleCargo
	 */
	public double getCodigoDetalleCargo() {
		return codigoDetalleCargo;
	}

	/**
	 * @param codigoDetalleCargo the codigoDetalleCargo to set
	 */
	public void setCodigoDetalleCargo(double codigoDetalleCargo) {
		this.codigoDetalleCargo = codigoDetalleCargo;
	}

	/**
	 * @return the codigoMedicoAsocio
	 */
	public int getCodigoMedicoAsocio() {
		return codigoMedicoAsocio;
	}

	/**
	 * @param codigoMedicoAsocio the codigoMedicoAsocio to set
	 */
	public void setCodigoMedicoAsocio(int codigoMedicoAsocio) {
		this.codigoMedicoAsocio = codigoMedicoAsocio;
	}

	/**
	 * @return the codigoEspecialidadMedicoAsocio
	 */
	public int getCodigoEspecialidadMedicoAsocio() {
		return codigoEspecialidadMedicoAsocio;
	}

	/**
	 * @param codigoEspecialidadMedicoAsocio the codigoEspecialidadMedicoAsocio to set
	 */
	public void setCodigoEspecialidadMedicoAsocio(int codigoEspecialidadMedicoAsocio) {
		this.codigoEspecialidadMedicoAsocio = codigoEspecialidadMedicoAsocio;
	}

	/**
	 * @return the detalleAsociosArray
	 */
	public ArrayList<DtoDetAsociosDetFactura> getDetalleAsociosArray() {
		return detalleAsociosArray;
	}

	/**
	 * @param detalleAsociosArray the detalleAsociosArray to set
	 */
	public void setDetalleAsociosArray(
			ArrayList<DtoDetAsociosDetFactura> detalleAsociosArray) {
		this.detalleAsociosArray = detalleAsociosArray;
	}



	public String getCodigoPropietarioAsocio() {
		return codigoPropietarioAsocio;
	}



	public void setCodigoPropietarioAsocio(String codigoPropietarioAsocio) {
		this.codigoPropietarioAsocio = codigoPropietarioAsocio;
	}


	/**
	 * Metodo que setea el porcentaje del pool double
	 * @param porcentajePoolDouble
	 */
	public void setPorcentajePoolDouble(double porcentajePoolDouble) {
		this.porcentajePoolDouble = porcentajePoolDouble;
	}


	/**
	 * Metodo que retorna el porcentaje del pool Double
	 * @return
	 */
	public double getPorcentajePoolDouble() {
		return porcentajePoolDouble;
	}
	
}