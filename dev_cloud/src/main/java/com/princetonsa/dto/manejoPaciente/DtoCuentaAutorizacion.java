package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.UtilidadFecha;

import com.princetonsa.mundo.UsuarioBasico;

public class DtoCuentaAutorizacion implements Serializable
{
	private String idCuenta ;
	private String idIngreso;
	private String codigoViaIngreso;	
	private String codigoTipoPaciente;
	private String indViaIngCuenta;
	private String codigoSubCuenta;
	private String codigoConvenio;
	private String nombreConvenio;
	private String estadoCuenta;	
	private String viaIngCuentaAsocio;	
	private String codigoAutorizacion;
	private String codigoDetAutorizacion;
	private String tipoAutorizacion;
	private String estadoAutorizacion;
		
	private boolean esAsocioPendiente;
	private boolean fueAsociada;
	private boolean esBD;
	
	private String colorEstado;
	
	public DtoCuentaAutorizacion()
	{
		reset();
	}
	
	public void reset()
	{
		idCuenta = "";
		idIngreso = "";
		codigoViaIngreso = "";
		codigoTipoPaciente = "";
		indViaIngCuenta = "";
		codigoConvenio = "";
		nombreConvenio = "";		
		codigoAutorizacion = "";
		tipoAutorizacion = "";
		estadoAutorizacion = "";
		codigoSubCuenta = "";
		this.esAsocioPendiente = false;
		this.fueAsociada = false;
		this.viaIngCuentaAsocio = "";
		this.codigoDetAutorizacion = "";
		this.estadoCuenta = "";
		this.esBD = false;
		this.colorEstado = "";		
	}
	
	
	public String getIdCuenta() {
		return idCuenta;
	}
	public void setIdCuenta(String idCuenta) {
		this.idCuenta = idCuenta;
	}
	public String getIdIngreso() {
		return idIngreso;
	}
	public void setIdIngreso(String idIngreso) {
		this.idIngreso = idIngreso;
	}
	public String getCodigoViaIngreso() {
		return codigoViaIngreso;
	}
	public void setCodigoViaIngreso(String codigoViaIngreso) {
		this.codigoViaIngreso = codigoViaIngreso;
	}
	public String getCodigoTipoPaciente() {
		return codigoTipoPaciente;
	}
	public void setCodigoTipoPaciente(String codigoTipoPaciente) {
		this.codigoTipoPaciente = codigoTipoPaciente;
	}
	public String getIndViaIngCuenta() {
		return indViaIngCuenta;
	}
	public void setIndViaIngCuenta(String indViaIngCuenta) {
		this.indViaIngCuenta = indViaIngCuenta;
	}
	public String getCodigoConvenio() {
		return codigoConvenio;
	}
	public void setCodigoConvenio(String codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}
	public String getNombreConvenio() {
		return nombreConvenio;
	}
	public void setNombreConvenio(String nombreConvenio) {
		this.nombreConvenio = nombreConvenio;
	}
	public String getCodigoAutorizacion() {
		return codigoAutorizacion;
	}
	public void setCodigoAutorizacion(String codigoAutorizacion) {
		this.codigoAutorizacion = codigoAutorizacion;
	}
	public String getTipoAutorizacion() {
		return tipoAutorizacion;
	}
	public void setTipoAutorizacion(String tipoAutorizacion) {
		this.tipoAutorizacion = tipoAutorizacion;
	}
	public String getEstadoAutorizacion() {
		return estadoAutorizacion;
	}
	public void setEstadoAutorizacion(String estadoAutorizacion) {
		this.estadoAutorizacion = estadoAutorizacion;
	}

	public String getCodigoSubCuenta() {
		return codigoSubCuenta;
	}

	public void setCodigoSubCuenta(String codigoSubCuenta) {
		this.codigoSubCuenta = codigoSubCuenta;
	}

	public String getViaIngCuentaAsocio() {
		return viaIngCuentaAsocio;
	}

	public void setViaIngCuentaAsocio(String viaIngCuentaAsocio) {
		this.viaIngCuentaAsocio = viaIngCuentaAsocio;
	}

	public String getCodigoDetAutorizacion() {
		return codigoDetAutorizacion;
	}

	public void setCodigoDetAutorizacion(String codigoDetAutorizacion) {
		this.codigoDetAutorizacion = codigoDetAutorizacion;
	}

	public String getEstadoCuenta() {
		return estadoCuenta;
	}

	public void setEstadoCuenta(String estadoCuenta) {
		this.estadoCuenta = estadoCuenta;
	}

	public boolean isEsAsocioPendiente() {
		return esAsocioPendiente;
	}

	public void setEsAsocioPendiente(boolean esAsocioPendiente) {
		this.esAsocioPendiente = esAsocioPendiente;
	}

	public boolean isFueAsociada() {
		return fueAsociada;
	}

	public void setFueAsociada(boolean fueAsociada) {
		this.fueAsociada = fueAsociada;
	}

	public boolean isEsBD() {
		return esBD;
	}

	public void setEsBD(boolean esBD) {
		this.esBD = esBD;
	}

	public String getColorEstado() {
		return colorEstado;
	}

	public void setColorEstado(String colorEstado) {
		this.colorEstado = colorEstado;
	}
}