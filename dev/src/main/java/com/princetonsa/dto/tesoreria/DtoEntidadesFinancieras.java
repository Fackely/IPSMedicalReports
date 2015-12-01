package com.princetonsa.dto.tesoreria;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;

/**
 * @author Cristhian Murillo
 * 
 */
public class DtoEntidadesFinancieras implements Serializable {

	private static final long serialVersionUID = 1L;

	private int consecutivo;
	private String codigo;
	private int codigoTercero;
	private String numeroIdentificacionTercero;
	private int digitoVerificacionTercero;
	private String descripcionTercero;
	private int codigoTipoEntidad;
	private String descripcionTipoEntidad;
	private boolean activo;

	private String formaPago;
	private int consecutivoFormaPago;
	
	private double totalValorRecibido;
	private double totalValorSistema;
	private double totalValorDiferencia;
	
	private ArrayList<DtoDetalleDocSopor> listadoDtoDetDocSoporte;

	public DtoEntidadesFinancieras() {

		this.consecutivo = ConstantesBD.codigoNuncaValido;
		this.codigo = "";
		this.codigoTercero = ConstantesBD.codigoNuncaValido;
		this.numeroIdentificacionTercero = "";
		this.digitoVerificacionTercero = ConstantesBD.codigoNuncaValido;
		this.descripcionTercero = "";
		this.codigoTipoEntidad = ConstantesBD.codigoNuncaValido;
		this.descripcionTipoEntidad = "";
		this.activo = false;
		this.formaPago = "";
		this.consecutivoFormaPago = ConstantesBD.codigoNuncaValido;
		this.setTotalValorSistema(ConstantesBD.codigoNuncaValidoDouble);
		this.totalValorDiferencia = ConstantesBD.codigoNuncaValidoDouble;
		this.setListadoDtoDetDocSoporte(new ArrayList<DtoDetalleDocSopor>());
		this.totalValorRecibido = ConstantesBD.codigoNuncaValidoDouble;
	}

	public int getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public int getCodigoTercero() {
		return codigoTercero;
	}

	public void setCodigoTercero(int codigoTercero) {
		this.codigoTercero = codigoTercero;
	}

	public String getNumeroIdentificacionTercero() {
		return numeroIdentificacionTercero;
	}

	public void setNumeroIdentificacionTercero(
			String numeroIdentificacionTercero) {
		this.numeroIdentificacionTercero = numeroIdentificacionTercero;
	}

	public int getDigitoVerificacionTercero() {
		return digitoVerificacionTercero;
	}

	public void setDigitoVerificacionTercero(int digitoVerificacionTercero) {
		this.digitoVerificacionTercero = digitoVerificacionTercero;
	}

	public String getDescripcionTercero() {
		return descripcionTercero;
	}

	public void setDescripcionTercero(String descripcionTercero) {
		this.descripcionTercero = descripcionTercero;
	}

	public int getCodigoTipoEntidad() {
		return codigoTipoEntidad;
	}

	public void setCodigoTipoEntidad(int codigoTipoEntidad) {
		this.codigoTipoEntidad = codigoTipoEntidad;
	}

	public String getDescripcionTipoEntidad() {
		return descripcionTipoEntidad;
	}

	public void setDescripcionTipoEntidad(String descripcionTipoEntidad) {
		this.descripcionTipoEntidad = descripcionTipoEntidad;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public String getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}
	
	public double getTotalValorDiferencia() {
		return totalValorDiferencia;
	}

	public void setTotalValorDiferencia(double totalValorDiferencia) {
		this.totalValorDiferencia = totalValorDiferencia;
	}

	public void setListadoDtoDetDocSoporte(
			ArrayList<DtoDetalleDocSopor> listadoDtoDetDocSoporte) {
		this.listadoDtoDetDocSoporte = listadoDtoDetDocSoporte;
	}

	public ArrayList<DtoDetalleDocSopor> getListadoDtoDetDocSoporte() {
		return listadoDtoDetDocSoporte;
	}

	public void setTotalValorSistema(double totalValorSistema) {
		this.totalValorSistema = totalValorSistema;
	}

	public double getTotalValorSistema() {
		return totalValorSistema;
	}

	public void setConsecutivoFormaPago(int consecutivoFormaPago) {
		this.consecutivoFormaPago = consecutivoFormaPago;
	}

	public int getConsecutivoFormaPago() {
		return consecutivoFormaPago;
	}
	
	public double getTotalValorRecibido() {
		return totalValorRecibido;
	}

	public void setTotalValorRecibido(double totalValorRecibido) {
		this.totalValorRecibido = totalValorRecibido;
	}

}
