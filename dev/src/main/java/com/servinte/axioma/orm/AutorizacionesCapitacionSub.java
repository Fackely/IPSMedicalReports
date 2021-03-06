package com.servinte.axioma.orm;

// Generated Mar 30, 2011 8:31:34 AM by Hibernate Tools 3.2.4.GA

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * AutorizacionesCapitacionSub generated by hbm2java
 */
public class AutorizacionesCapitacionSub implements java.io.Serializable {

	private static final long serialVersionUID = -1279934698442944224L;
	private long codigoPk;
	private Convenios convenios;
	private Usuarios usuarios;
	private TiposAfiliado tiposAfiliado;
	private AutorizacionesEntidadesSub autorizacionesEntidadesSub;
	private EstratosSociales estratosSociales;
	private long consecutivo;
	private String tipoAutorizacion;
	private String otroConvenioRecobro;
	private char indicativoTemporal;
	private String descripcionEntidad;
	private Integer indicadorPrioridad;
	private String direccionEntidad;
	private String telefonoEntidad;
	private Date fechaAutoriza;
	private String horaAutoriza;
	private String tipoEntQueSeAuto;
	private String observaciones;
	private Set histoAutorizacionCapitaSubs = new HashSet(0);
	private Set autorizacionesEstanciaCapitas = new HashSet(0);

	public AutorizacionesCapitacionSub() {
	}

	public AutorizacionesCapitacionSub(long codigoPk,
			AutorizacionesEntidadesSub autorizacionesEntidadesSub,
			long consecutivo, String tipoAutorizacion, char indicativoTemporal) {
		this.codigoPk = codigoPk;
		this.autorizacionesEntidadesSub = autorizacionesEntidadesSub;
		this.consecutivo = consecutivo;
		this.tipoAutorizacion = tipoAutorizacion;
		this.indicativoTemporal = indicativoTemporal;
	}

	public AutorizacionesCapitacionSub(long codigoPk, Convenios convenios,
			Usuarios usuarios, TiposAfiliado tiposAfiliado,
			AutorizacionesEntidadesSub autorizacionesEntidadesSub,
			EstratosSociales estratosSociales, long consecutivo,
			String tipoAutorizacion, String otroConvenioRecobro,
			char indicativoTemporal, String descripcionEntidad,
			Integer indicadorPrioridad, String direccionEntidad,
			String telefonoEntidad, Date fechaAutoriza, String horaAutoriza,
			String tipoEntQueSeAuto, String observaciones,
			Set histoAutorizacionCapitaSubs, Set autorizacionesEstanciaCapitas) {
		this.codigoPk = codigoPk;
		this.convenios = convenios;
		this.usuarios = usuarios;
		this.tiposAfiliado = tiposAfiliado;
		this.autorizacionesEntidadesSub = autorizacionesEntidadesSub;
		this.estratosSociales = estratosSociales;
		this.consecutivo = consecutivo;
		this.tipoAutorizacion = tipoAutorizacion;
		this.otroConvenioRecobro = otroConvenioRecobro;
		this.indicativoTemporal = indicativoTemporal;
		this.descripcionEntidad = descripcionEntidad;
		this.indicadorPrioridad = indicadorPrioridad;
		this.direccionEntidad = direccionEntidad;
		this.telefonoEntidad = telefonoEntidad;
		this.fechaAutoriza = fechaAutoriza;
		this.horaAutoriza = horaAutoriza;
		this.tipoEntQueSeAuto = tipoEntQueSeAuto;
		this.observaciones = observaciones;
		this.histoAutorizacionCapitaSubs = histoAutorizacionCapitaSubs;
		this.autorizacionesEstanciaCapitas = autorizacionesEstanciaCapitas;
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

	public TiposAfiliado getTiposAfiliado() {
		return this.tiposAfiliado;
	}

	public void setTiposAfiliado(TiposAfiliado tiposAfiliado) {
		this.tiposAfiliado = tiposAfiliado;
	}

	public AutorizacionesEntidadesSub getAutorizacionesEntidadesSub() {
		return this.autorizacionesEntidadesSub;
	}

	public void setAutorizacionesEntidadesSub(
			AutorizacionesEntidadesSub autorizacionesEntidadesSub) {
		this.autorizacionesEntidadesSub = autorizacionesEntidadesSub;
	}

	public EstratosSociales getEstratosSociales() {
		return this.estratosSociales;
	}

	public void setEstratosSociales(EstratosSociales estratosSociales) {
		this.estratosSociales = estratosSociales;
	}

	public long getConsecutivo() {
		return this.consecutivo;
	}

	public void setConsecutivo(long consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getTipoAutorizacion() {
		return this.tipoAutorizacion;
	}

	public void setTipoAutorizacion(String tipoAutorizacion) {
		this.tipoAutorizacion = tipoAutorizacion;
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

	public String getDescripcionEntidad() {
		return this.descripcionEntidad;
	}

	public void setDescripcionEntidad(String descripcionEntidad) {
		this.descripcionEntidad = descripcionEntidad;
	}

	public Integer getIndicadorPrioridad() {
		return this.indicadorPrioridad;
	}

	public void setIndicadorPrioridad(Integer indicadorPrioridad) {
		this.indicadorPrioridad = indicadorPrioridad;
	}

	public String getDireccionEntidad() {
		return this.direccionEntidad;
	}

	public void setDireccionEntidad(String direccionEntidad) {
		this.direccionEntidad = direccionEntidad;
	}

	public String getTelefonoEntidad() {
		return this.telefonoEntidad;
	}

	public void setTelefonoEntidad(String telefonoEntidad) {
		this.telefonoEntidad = telefonoEntidad;
	}

	public Date getFechaAutoriza() {
		return this.fechaAutoriza;
	}

	public void setFechaAutoriza(Date fechaAutoriza) {
		this.fechaAutoriza = fechaAutoriza;
	}

	public String getHoraAutoriza() {
		return this.horaAutoriza;
	}

	public void setHoraAutoriza(String horaAutoriza) {
		this.horaAutoriza = horaAutoriza;
	}

	public String getTipoEntQueSeAuto() {
		return this.tipoEntQueSeAuto;
	}

	public void setTipoEntQueSeAuto(String tipoEntQueSeAuto) {
		this.tipoEntQueSeAuto = tipoEntQueSeAuto;
	}

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Set getHistoAutorizacionCapitaSubs() {
		return this.histoAutorizacionCapitaSubs;
	}

	public void setHistoAutorizacionCapitaSubs(Set histoAutorizacionCapitaSubs) {
		this.histoAutorizacionCapitaSubs = histoAutorizacionCapitaSubs;
	}

	public Set getAutorizacionesEstanciaCapitas() {
		return this.autorizacionesEstanciaCapitas;
	}

	public void setAutorizacionesEstanciaCapitas(
			Set autorizacionesEstanciaCapitas) {
		this.autorizacionesEstanciaCapitas = autorizacionesEstanciaCapitas;
	}

}
