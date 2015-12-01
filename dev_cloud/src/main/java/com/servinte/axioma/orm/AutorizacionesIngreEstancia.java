package com.servinte.axioma.orm;

// Generated 5/07/2011 11:51:29 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * AutorizacionesIngreEstancia generated by hbm2java
 */
public class AutorizacionesIngreEstancia implements java.io.Serializable {

	private long codigoPk;
	private Convenios convenios;
	private Usuarios usuarios;
	private IngresosEstancia ingresosEstancia;
	private CentrosCosto centrosCosto;
	private Date fechaInicioAutorizacion;
	private long consecutivoAdmision;
	private int diasEstanciaAutorizados;
	private String usuarioContacta;
	private String observaciones;
	private String otroConvenioRecobro;
	private char indicativoTemporal;
	private String estado;
	private String cargoUsuContacta;
	private Date fechaAutorizacion;
	private String horaAutorizacion;
	private Set autorizacionesEstanciaCapitas = new HashSet(0);
	private Set histoAutorizacionIngEstans = new HashSet(0);

	public AutorizacionesIngreEstancia() {
	}

	public AutorizacionesIngreEstancia(long codigoPk,
			IngresosEstancia ingresosEstancia, Date fechaInicioAutorizacion,
			long consecutivoAdmision, int diasEstanciaAutorizados,
			char indicativoTemporal, String estado) {
		this.codigoPk = codigoPk;
		this.ingresosEstancia = ingresosEstancia;
		this.fechaInicioAutorizacion = fechaInicioAutorizacion;
		this.consecutivoAdmision = consecutivoAdmision;
		this.diasEstanciaAutorizados = diasEstanciaAutorizados;
		this.indicativoTemporal = indicativoTemporal;
		this.estado = estado;
	}

	public AutorizacionesIngreEstancia(long codigoPk, Convenios convenios,
			Usuarios usuarios, IngresosEstancia ingresosEstancia,
			CentrosCosto centrosCosto, Date fechaInicioAutorizacion,
			long consecutivoAdmision, int diasEstanciaAutorizados,
			String usuarioContacta, String observaciones,
			String otroConvenioRecobro, char indicativoTemporal, String estado,
			String cargoUsuContacta, Date fechaAutorizacion,
			String horaAutorizacion, Set autorizacionesEstanciaCapitas,
			Set histoAutorizacionIngEstans) {
		this.codigoPk = codigoPk;
		this.convenios = convenios;
		this.usuarios = usuarios;
		this.ingresosEstancia = ingresosEstancia;
		this.centrosCosto = centrosCosto;
		this.fechaInicioAutorizacion = fechaInicioAutorizacion;
		this.consecutivoAdmision = consecutivoAdmision;
		this.diasEstanciaAutorizados = diasEstanciaAutorizados;
		this.usuarioContacta = usuarioContacta;
		this.observaciones = observaciones;
		this.otroConvenioRecobro = otroConvenioRecobro;
		this.indicativoTemporal = indicativoTemporal;
		this.estado = estado;
		this.cargoUsuContacta = cargoUsuContacta;
		this.fechaAutorizacion = fechaAutorizacion;
		this.horaAutorizacion = horaAutorizacion;
		this.autorizacionesEstanciaCapitas = autorizacionesEstanciaCapitas;
		this.histoAutorizacionIngEstans = histoAutorizacionIngEstans;
	}

	public long getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public Convenios getConvenios() {
		return this.convenios;
	}

	public void setConvenios(Convenios convenios) {
		this.convenios = convenios;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public IngresosEstancia getIngresosEstancia() {
		return this.ingresosEstancia;
	}

	public void setIngresosEstancia(IngresosEstancia ingresosEstancia) {
		this.ingresosEstancia = ingresosEstancia;
	}

	public CentrosCosto getCentrosCosto() {
		return this.centrosCosto;
	}

	public void setCentrosCosto(CentrosCosto centrosCosto) {
		this.centrosCosto = centrosCosto;
	}

	public Date getFechaInicioAutorizacion() {
		return this.fechaInicioAutorizacion;
	}

	public void setFechaInicioAutorizacion(Date fechaInicioAutorizacion) {
		this.fechaInicioAutorizacion = fechaInicioAutorizacion;
	}

	public long getConsecutivoAdmision() {
		return this.consecutivoAdmision;
	}

	public void setConsecutivoAdmision(long consecutivoAdmision) {
		this.consecutivoAdmision = consecutivoAdmision;
	}

	public int getDiasEstanciaAutorizados() {
		return this.diasEstanciaAutorizados;
	}

	public void setDiasEstanciaAutorizados(int diasEstanciaAutorizados) {
		this.diasEstanciaAutorizados = diasEstanciaAutorizados;
	}

	public String getUsuarioContacta() {
		return this.usuarioContacta;
	}

	public void setUsuarioContacta(String usuarioContacta) {
		this.usuarioContacta = usuarioContacta;
	}

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getOtroConvenioRecobro() {
		return this.otroConvenioRecobro;
	}

	public void setOtroConvenioRecobro(String otroConvenioRecobro) {
		this.otroConvenioRecobro = otroConvenioRecobro;
	}

	public char getIndicativoTemporal() {
		return this.indicativoTemporal;
	}

	public void setIndicativoTemporal(char indicativoTemporal) {
		this.indicativoTemporal = indicativoTemporal;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCargoUsuContacta() {
		return this.cargoUsuContacta;
	}

	public void setCargoUsuContacta(String cargoUsuContacta) {
		this.cargoUsuContacta = cargoUsuContacta;
	}

	public Date getFechaAutorizacion() {
		return this.fechaAutorizacion;
	}

	public void setFechaAutorizacion(Date fechaAutorizacion) {
		this.fechaAutorizacion = fechaAutorizacion;
	}

	public String getHoraAutorizacion() {
		return this.horaAutorizacion;
	}

	public void setHoraAutorizacion(String horaAutorizacion) {
		this.horaAutorizacion = horaAutorizacion;
	}

	public Set getAutorizacionesEstanciaCapitas() {
		return this.autorizacionesEstanciaCapitas;
	}

	public void setAutorizacionesEstanciaCapitas(
			Set autorizacionesEstanciaCapitas) {
		this.autorizacionesEstanciaCapitas = autorizacionesEstanciaCapitas;
	}

	public Set getHistoAutorizacionIngEstans() {
		return this.histoAutorizacionIngEstans;
	}

	public void setHistoAutorizacionIngEstans(Set histoAutorizacionIngEstans) {
		this.histoAutorizacionIngEstans = histoAutorizacionIngEstans;
	}

}
