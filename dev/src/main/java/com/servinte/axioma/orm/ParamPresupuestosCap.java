package com.servinte.axioma.orm;

// Generated 20/05/2011 04:42:54 PM by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * ParamPresupuestosCap generated by hbm2java
 */
public class ParamPresupuestosCap implements java.io.Serializable {

	private long codigo;
	private Contratos contratos;
	private CentroAtencion centroAtencion;
	private Usuarios usuarios;
	private Instituciones instituciones;
	private String anioVigencia;
	private BigDecimal valorContrato;
	private BigDecimal porcentajeGastoGeneral;
	private BigDecimal valorGastoGeneral;
	private char activo;
	private Date fechaParam;
	private String horaParam;
	private Set logParamPresupuestoCaps = new HashSet(0);
	private Set detalleValorizacionArts = new HashSet(0);
	private Set valorizacionPresCapGens = new HashSet(0);
	private Set detalleValorizacionServs = new HashSet(0);

	public ParamPresupuestosCap() {
	}

	public ParamPresupuestosCap(long codigo, Contratos contratos,
			CentroAtencion centroAtencion, Usuarios usuarios,
			Instituciones instituciones, String anioVigencia,
			BigDecimal valorContrato, BigDecimal porcentajeGastoGeneral,
			BigDecimal valorGastoGeneral, char activo, Date fechaParam,
			String horaParam) {
		this.codigo = codigo;
		this.contratos = contratos;
		this.centroAtencion = centroAtencion;
		this.usuarios = usuarios;
		this.instituciones = instituciones;
		this.anioVigencia = anioVigencia;
		this.valorContrato = valorContrato;
		this.porcentajeGastoGeneral = porcentajeGastoGeneral;
		this.valorGastoGeneral = valorGastoGeneral;
		this.activo = activo;
		this.fechaParam = fechaParam;
		this.horaParam = horaParam;
	}

	public ParamPresupuestosCap(long codigo, Contratos contratos,
			CentroAtencion centroAtencion, Usuarios usuarios,
			Instituciones instituciones, String anioVigencia,
			BigDecimal valorContrato, BigDecimal porcentajeGastoGeneral,
			BigDecimal valorGastoGeneral, char activo, Date fechaParam,
			String horaParam, Set logParamPresupuestoCaps,
			Set detalleValorizacionArts, Set valorizacionPresCapGens,
			Set detalleValorizacionServs) {
		this.codigo = codigo;
		this.contratos = contratos;
		this.centroAtencion = centroAtencion;
		this.usuarios = usuarios;
		this.instituciones = instituciones;
		this.anioVigencia = anioVigencia;
		this.valorContrato = valorContrato;
		this.porcentajeGastoGeneral = porcentajeGastoGeneral;
		this.valorGastoGeneral = valorGastoGeneral;
		this.activo = activo;
		this.fechaParam = fechaParam;
		this.horaParam = horaParam;
		this.logParamPresupuestoCaps = logParamPresupuestoCaps;
		this.detalleValorizacionArts = detalleValorizacionArts;
		this.valorizacionPresCapGens = valorizacionPresCapGens;
		this.detalleValorizacionServs = detalleValorizacionServs;
	}

	public long getCodigo() {
		return this.codigo;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	public Contratos getContratos() {
		return this.contratos;
	}

	public void setContratos(Contratos contratos) {
		this.contratos = contratos;
	}

	public CentroAtencion getCentroAtencion() {
		return this.centroAtencion;
	}

	public void setCentroAtencion(CentroAtencion centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public Instituciones getInstituciones() {
		return this.instituciones;
	}

	public void setInstituciones(Instituciones instituciones) {
		this.instituciones = instituciones;
	}

	public String getAnioVigencia() {
		return this.anioVigencia;
	}

	public void setAnioVigencia(String anioVigencia) {
		this.anioVigencia = anioVigencia;
	}

	public BigDecimal getValorContrato() {
		return this.valorContrato;
	}

	public void setValorContrato(BigDecimal valorContrato) {
		this.valorContrato = valorContrato;
	}

	public BigDecimal getPorcentajeGastoGeneral() {
		return this.porcentajeGastoGeneral;
	}

	public void setPorcentajeGastoGeneral(BigDecimal porcentajeGastoGeneral) {
		this.porcentajeGastoGeneral = porcentajeGastoGeneral;
	}

	public BigDecimal getValorGastoGeneral() {
		return this.valorGastoGeneral;
	}

	public void setValorGastoGeneral(BigDecimal valorGastoGeneral) {
		this.valorGastoGeneral = valorGastoGeneral;
	}

	public char getActivo() {
		return this.activo;
	}

	public void setActivo(char activo) {
		this.activo = activo;
	}

	public Date getFechaParam() {
		return this.fechaParam;
	}

	public void setFechaParam(Date fechaParam) {
		this.fechaParam = fechaParam;
	}

	public String getHoraParam() {
		return this.horaParam;
	}

	public void setHoraParam(String horaParam) {
		this.horaParam = horaParam;
	}

	public Set getLogParamPresupuestoCaps() {
		return this.logParamPresupuestoCaps;
	}

	public void setLogParamPresupuestoCaps(Set logParamPresupuestoCaps) {
		this.logParamPresupuestoCaps = logParamPresupuestoCaps;
	}

	public Set getDetalleValorizacionArts() {
		return this.detalleValorizacionArts;
	}

	public void setDetalleValorizacionArts(Set detalleValorizacionArts) {
		this.detalleValorizacionArts = detalleValorizacionArts;
	}

	public Set getValorizacionPresCapGens() {
		return this.valorizacionPresCapGens;
	}

	public void setValorizacionPresCapGens(Set valorizacionPresCapGens) {
		this.valorizacionPresCapGens = valorizacionPresCapGens;
	}

	public Set getDetalleValorizacionServs() {
		return this.detalleValorizacionServs;
	}

	public void setDetalleValorizacionServs(Set detalleValorizacionServs) {
		this.detalleValorizacionServs = detalleValorizacionServs;
	}

}
