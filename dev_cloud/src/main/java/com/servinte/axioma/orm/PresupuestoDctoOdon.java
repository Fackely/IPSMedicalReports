package com.servinte.axioma.orm;

// Generated Jan 17, 2011 3:52:28 PM by Hibernate Tools 3.2.4.GA

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * PresupuestoDctoOdon generated by hbm2java
 */
public class PresupuestoDctoOdon implements java.io.Serializable {

	private long codigoPk;
	private DetDescuentosOdon detDescuentosOdon;
	private AutorizacionPresuDctoOdon autorizacionPresuDctoOdon;
	private PresupuestoOdontologico presupuestoOdontologico;
	private Usuarios usuariosByUsuarioModifica;
	private DescuentosOdonAten descuentosOdonAten;
	private Usuarios usuariosByUsuarioSolicitad;
	private MotivosDescuentosOdon motivosDescuentosOdon;
	private Date fechaSolicitad;
	private Date fechaModifica;
	private String horaModifica;
	private String horaSolicita;
	private BigDecimal valorDescuento;
	private String estado;
	private long consecutivo;
	private BigDecimal porcentajeDcto;
	private String observaciones;
	private char inclusion;
	private Set incluDctoOdontologicos = new HashSet(0);
	private Set autorizacionPresuDctoOdons = new HashSet(0);
	private Set logPresupuestoDctoOdons = new HashSet(0);

	public PresupuestoDctoOdon() {
	}

	public PresupuestoDctoOdon(long codigoPk,
			PresupuestoOdontologico presupuestoOdontologico,
			Usuarios usuariosByUsuarioModifica,
			Usuarios usuariosByUsuarioSolicitad, Date fechaSolicitad,
			Date fechaModifica, String horaModifica, String horaSolicita,
			String estado, long consecutivo, char inclusion) {
		this.codigoPk = codigoPk;
		this.presupuestoOdontologico = presupuestoOdontologico;
		this.usuariosByUsuarioModifica = usuariosByUsuarioModifica;
		this.usuariosByUsuarioSolicitad = usuariosByUsuarioSolicitad;
		this.fechaSolicitad = fechaSolicitad;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.horaSolicita = horaSolicita;
		this.estado = estado;
		this.consecutivo = consecutivo;
		this.inclusion = inclusion;
	}

	public PresupuestoDctoOdon(long codigoPk,
			DetDescuentosOdon detDescuentosOdon,
			AutorizacionPresuDctoOdon autorizacionPresuDctoOdon,
			PresupuestoOdontologico presupuestoOdontologico,
			Usuarios usuariosByUsuarioModifica,
			DescuentosOdonAten descuentosOdonAten,
			Usuarios usuariosByUsuarioSolicitad,
			MotivosDescuentosOdon motivosDescuentosOdon, Date fechaSolicitad,
			Date fechaModifica, String horaModifica, String horaSolicita,
			BigDecimal valorDescuento, String estado, long consecutivo,
			BigDecimal porcentajeDcto, String observaciones, char inclusion,
			Set incluDctoOdontologicos, Set autorizacionPresuDctoOdons,
			Set logPresupuestoDctoOdons) {
		this.codigoPk = codigoPk;
		this.detDescuentosOdon = detDescuentosOdon;
		this.autorizacionPresuDctoOdon = autorizacionPresuDctoOdon;
		this.presupuestoOdontologico = presupuestoOdontologico;
		this.usuariosByUsuarioModifica = usuariosByUsuarioModifica;
		this.descuentosOdonAten = descuentosOdonAten;
		this.usuariosByUsuarioSolicitad = usuariosByUsuarioSolicitad;
		this.motivosDescuentosOdon = motivosDescuentosOdon;
		this.fechaSolicitad = fechaSolicitad;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.horaSolicita = horaSolicita;
		this.valorDescuento = valorDescuento;
		this.estado = estado;
		this.consecutivo = consecutivo;
		this.porcentajeDcto = porcentajeDcto;
		this.observaciones = observaciones;
		this.inclusion = inclusion;
		this.incluDctoOdontologicos = incluDctoOdontologicos;
		this.autorizacionPresuDctoOdons = autorizacionPresuDctoOdons;
		this.logPresupuestoDctoOdons = logPresupuestoDctoOdons;
	}

	public long getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public DetDescuentosOdon getDetDescuentosOdon() {
		return this.detDescuentosOdon;
	}

	public void setDetDescuentosOdon(DetDescuentosOdon detDescuentosOdon) {
		this.detDescuentosOdon = detDescuentosOdon;
	}

	public AutorizacionPresuDctoOdon getAutorizacionPresuDctoOdon() {
		return this.autorizacionPresuDctoOdon;
	}

	public void setAutorizacionPresuDctoOdon(
			AutorizacionPresuDctoOdon autorizacionPresuDctoOdon) {
		this.autorizacionPresuDctoOdon = autorizacionPresuDctoOdon;
	}

	public PresupuestoOdontologico getPresupuestoOdontologico() {
		return this.presupuestoOdontologico;
	}

	public void setPresupuestoOdontologico(
			PresupuestoOdontologico presupuestoOdontologico) {
		this.presupuestoOdontologico = presupuestoOdontologico;
	}

	public Usuarios getUsuariosByUsuarioModifica() {
		return this.usuariosByUsuarioModifica;
	}

	public void setUsuariosByUsuarioModifica(Usuarios usuariosByUsuarioModifica) {
		this.usuariosByUsuarioModifica = usuariosByUsuarioModifica;
	}

	public DescuentosOdonAten getDescuentosOdonAten() {
		return this.descuentosOdonAten;
	}

	public void setDescuentosOdonAten(DescuentosOdonAten descuentosOdonAten) {
		this.descuentosOdonAten = descuentosOdonAten;
	}

	public Usuarios getUsuariosByUsuarioSolicitad() {
		return this.usuariosByUsuarioSolicitad;
	}

	public void setUsuariosByUsuarioSolicitad(
			Usuarios usuariosByUsuarioSolicitad) {
		this.usuariosByUsuarioSolicitad = usuariosByUsuarioSolicitad;
	}

	public MotivosDescuentosOdon getMotivosDescuentosOdon() {
		return this.motivosDescuentosOdon;
	}

	public void setMotivosDescuentosOdon(
			MotivosDescuentosOdon motivosDescuentosOdon) {
		this.motivosDescuentosOdon = motivosDescuentosOdon;
	}

	public Date getFechaSolicitad() {
		return this.fechaSolicitad;
	}

	public void setFechaSolicitad(Date fechaSolicitad) {
		this.fechaSolicitad = fechaSolicitad;
	}

	public Date getFechaModifica() {
		return this.fechaModifica;
	}

	public void setFechaModifica(Date fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	public String getHoraModifica() {
		return this.horaModifica;
	}

	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	public String getHoraSolicita() {
		return this.horaSolicita;
	}

	public void setHoraSolicita(String horaSolicita) {
		this.horaSolicita = horaSolicita;
	}

	public BigDecimal getValorDescuento() {
		return this.valorDescuento;
	}

	public void setValorDescuento(BigDecimal valorDescuento) {
		this.valorDescuento = valorDescuento;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public long getConsecutivo() {
		return this.consecutivo;
	}

	public void setConsecutivo(long consecutivo) {
		this.consecutivo = consecutivo;
	}

	public BigDecimal getPorcentajeDcto() {
		return this.porcentajeDcto;
	}

	public void setPorcentajeDcto(BigDecimal porcentajeDcto) {
		this.porcentajeDcto = porcentajeDcto;
	}

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public char getInclusion() {
		return this.inclusion;
	}

	public void setInclusion(char inclusion) {
		this.inclusion = inclusion;
	}

	public Set getIncluDctoOdontologicos() {
		return this.incluDctoOdontologicos;
	}

	public void setIncluDctoOdontologicos(Set incluDctoOdontologicos) {
		this.incluDctoOdontologicos = incluDctoOdontologicos;
	}

	public Set getAutorizacionPresuDctoOdons() {
		return this.autorizacionPresuDctoOdons;
	}

	public void setAutorizacionPresuDctoOdons(Set autorizacionPresuDctoOdons) {
		this.autorizacionPresuDctoOdons = autorizacionPresuDctoOdons;
	}

	public Set getLogPresupuestoDctoOdons() {
		return this.logPresupuestoDctoOdons;
	}

	public void setLogPresupuestoDctoOdons(Set logPresupuestoDctoOdons) {
		this.logPresupuestoDctoOdons = logPresupuestoDctoOdons;
	}

}
