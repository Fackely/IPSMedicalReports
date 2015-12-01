package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;

/**
 * TarifasInvConEntSub generated by hbm2java
 */
public class TarifasInvConEntSub implements java.io.Serializable {

	private long consecutivo;
	private Usuarios usuariosByUsuarioModifica;
	private EsquemasTarifarios esquemasTarifarios;
	private ContratosEntidadesSub contratosEntidadesSub;
	private Usuarios usuariosByUsuarioInactivacion;
	private Integer claseInventario;
	private Date fechaVigencia;
	private Date fechaModifica;
	private String horaModifica;
	private String activo;
	private Date fechaInactivacion;
	private String horaInactivacion;

	public TarifasInvConEntSub() {
	}

	public TarifasInvConEntSub(long consecutivo,
			EsquemasTarifarios esquemasTarifarios,
			ContratosEntidadesSub contratosEntidadesSub, Date fechaVigencia,
			String activo) {
		this.consecutivo = consecutivo;
		this.esquemasTarifarios = esquemasTarifarios;
		this.contratosEntidadesSub = contratosEntidadesSub;
		this.fechaVigencia = fechaVigencia;
		this.activo = activo;
	}

	public TarifasInvConEntSub(long consecutivo,
			Usuarios usuariosByUsuarioModifica,
			EsquemasTarifarios esquemasTarifarios,
			ContratosEntidadesSub contratosEntidadesSub,
			Usuarios usuariosByUsuarioInactivacion, Integer claseInventario,
			Date fechaVigencia, Date fechaModifica, String horaModifica,
			String activo, Date fechaInactivacion, String horaInactivacion) {
		this.consecutivo = consecutivo;
		this.usuariosByUsuarioModifica = usuariosByUsuarioModifica;
		this.esquemasTarifarios = esquemasTarifarios;
		this.contratosEntidadesSub = contratosEntidadesSub;
		this.usuariosByUsuarioInactivacion = usuariosByUsuarioInactivacion;
		this.claseInventario = claseInventario;
		this.fechaVigencia = fechaVigencia;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.activo = activo;
		this.fechaInactivacion = fechaInactivacion;
		this.horaInactivacion = horaInactivacion;
	}

	public long getConsecutivo() {
		return this.consecutivo;
	}

	public void setConsecutivo(long consecutivo) {
		this.consecutivo = consecutivo;
	}

	public Usuarios getUsuariosByUsuarioModifica() {
		return this.usuariosByUsuarioModifica;
	}

	public void setUsuariosByUsuarioModifica(Usuarios usuariosByUsuarioModifica) {
		this.usuariosByUsuarioModifica = usuariosByUsuarioModifica;
	}

	public EsquemasTarifarios getEsquemasTarifarios() {
		return this.esquemasTarifarios;
	}

	public void setEsquemasTarifarios(EsquemasTarifarios esquemasTarifarios) {
		this.esquemasTarifarios = esquemasTarifarios;
	}

	public ContratosEntidadesSub getContratosEntidadesSub() {
		return this.contratosEntidadesSub;
	}

	public void setContratosEntidadesSub(
			ContratosEntidadesSub contratosEntidadesSub) {
		this.contratosEntidadesSub = contratosEntidadesSub;
	}

	public Usuarios getUsuariosByUsuarioInactivacion() {
		return this.usuariosByUsuarioInactivacion;
	}

	public void setUsuariosByUsuarioInactivacion(
			Usuarios usuariosByUsuarioInactivacion) {
		this.usuariosByUsuarioInactivacion = usuariosByUsuarioInactivacion;
	}

	public Integer getClaseInventario() {
		return this.claseInventario;
	}

	public void setClaseInventario(Integer claseInventario) {
		this.claseInventario = claseInventario;
	}

	public Date getFechaVigencia() {
		return this.fechaVigencia;
	}

	public void setFechaVigencia(Date fechaVigencia) {
		this.fechaVigencia = fechaVigencia;
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

	public String getActivo() {
		return this.activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public Date getFechaInactivacion() {
		return this.fechaInactivacion;
	}

	public void setFechaInactivacion(Date fechaInactivacion) {
		this.fechaInactivacion = fechaInactivacion;
	}

	public String getHoraInactivacion() {
		return this.horaInactivacion;
	}

	public void setHoraInactivacion(String horaInactivacion) {
		this.horaInactivacion = horaInactivacion;
	}

}
