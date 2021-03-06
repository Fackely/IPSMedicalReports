package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.math.BigDecimal;
import java.util.Date;

/**
 * HisDescuentosOdonAten generated by hbm2java
 */
public class HisDescuentosOdonAten implements java.io.Serializable {

	private long codigo;
	private TiposUsuarios tiposUsuarios;
	private CentroAtencion centroAtencion;
	private Usuarios usuarios;
	private Instituciones instituciones;
	private long consecutivo;
	private BigDecimal porcentajeDcto;
	private Long nivelAutorizacion;
	private int diasVigencia;
	private Date fechaModifica;
	private String horaModifica;
	private char eliminado;
	private Integer diasVigenciaMod;
	private BigDecimal porcentajeDctoMod;

	public HisDescuentosOdonAten() {
	}

	public HisDescuentosOdonAten(long codigo, Usuarios usuarios,
			Instituciones instituciones, long consecutivo,
			BigDecimal porcentajeDcto, int diasVigencia, Date fechaModifica,
			String horaModifica, char eliminado) {
		this.codigo = codigo;
		this.usuarios = usuarios;
		this.instituciones = instituciones;
		this.consecutivo = consecutivo;
		this.porcentajeDcto = porcentajeDcto;
		this.diasVigencia = diasVigencia;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.eliminado = eliminado;
	}

	public HisDescuentosOdonAten(long codigo, TiposUsuarios tiposUsuarios,
			CentroAtencion centroAtencion, Usuarios usuarios,
			Instituciones instituciones, long consecutivo,
			BigDecimal porcentajeDcto, Long nivelAutorizacion,
			int diasVigencia, Date fechaModifica, String horaModifica,
			char eliminado, Integer diasVigenciaMod,
			BigDecimal porcentajeDctoMod) {
		this.codigo = codigo;
		this.tiposUsuarios = tiposUsuarios;
		this.centroAtencion = centroAtencion;
		this.usuarios = usuarios;
		this.instituciones = instituciones;
		this.consecutivo = consecutivo;
		this.porcentajeDcto = porcentajeDcto;
		this.nivelAutorizacion = nivelAutorizacion;
		this.diasVigencia = diasVigencia;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.eliminado = eliminado;
		this.diasVigenciaMod = diasVigenciaMod;
		this.porcentajeDctoMod = porcentajeDctoMod;
	}

	public long getCodigo() {
		return this.codigo;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	public TiposUsuarios getTiposUsuarios() {
		return this.tiposUsuarios;
	}

	public void setTiposUsuarios(TiposUsuarios tiposUsuarios) {
		this.tiposUsuarios = tiposUsuarios;
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

	public Long getNivelAutorizacion() {
		return this.nivelAutorizacion;
	}

	public void setNivelAutorizacion(Long nivelAutorizacion) {
		this.nivelAutorizacion = nivelAutorizacion;
	}

	public int getDiasVigencia() {
		return this.diasVigencia;
	}

	public void setDiasVigencia(int diasVigencia) {
		this.diasVigencia = diasVigencia;
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

	public char getEliminado() {
		return this.eliminado;
	}

	public void setEliminado(char eliminado) {
		this.eliminado = eliminado;
	}

	public Integer getDiasVigenciaMod() {
		return this.diasVigenciaMod;
	}

	public void setDiasVigenciaMod(Integer diasVigenciaMod) {
		this.diasVigenciaMod = diasVigenciaMod;
	}

	public BigDecimal getPorcentajeDctoMod() {
		return this.porcentajeDctoMod;
	}

	public void setPorcentajeDctoMod(BigDecimal porcentajeDctoMod) {
		this.porcentajeDctoMod = porcentajeDctoMod;
	}

}
