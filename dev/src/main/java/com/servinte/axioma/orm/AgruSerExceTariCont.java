package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * AgruSerExceTariCont generated by hbm2java
 */
public class AgruSerExceTariCont implements java.io.Serializable {

	private long codigo;
	private ExcepTarifasContrato excepTarifasContrato;
	private Usuarios usuarios;
	private TiposServicio tiposServicio;
	private Especialidades especialidades;
	private GruposServicios gruposServicios;
	private Character pos;
	private BigDecimal valorAjuste;
	private BigDecimal nuevaTarifa;
	private Date fechaModifica;
	private String horaModifica;
	private Date fechaVigencia;
	private Set porcentajeAgruSerExces = new HashSet(0);

	public AgruSerExceTariCont() {
	}

	public AgruSerExceTariCont(long codigo,
			ExcepTarifasContrato excepTarifasContrato, Usuarios usuarios,
			Date fechaModifica, String horaModifica, Date fechaVigencia) {
		this.codigo = codigo;
		this.excepTarifasContrato = excepTarifasContrato;
		this.usuarios = usuarios;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.fechaVigencia = fechaVigencia;
	}

	public AgruSerExceTariCont(long codigo,
			ExcepTarifasContrato excepTarifasContrato, Usuarios usuarios,
			TiposServicio tiposServicio, Especialidades especialidades,
			GruposServicios gruposServicios, Character pos,
			BigDecimal valorAjuste, BigDecimal nuevaTarifa, Date fechaModifica,
			String horaModifica, Date fechaVigencia, Set porcentajeAgruSerExces) {
		this.codigo = codigo;
		this.excepTarifasContrato = excepTarifasContrato;
		this.usuarios = usuarios;
		this.tiposServicio = tiposServicio;
		this.especialidades = especialidades;
		this.gruposServicios = gruposServicios;
		this.pos = pos;
		this.valorAjuste = valorAjuste;
		this.nuevaTarifa = nuevaTarifa;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.fechaVigencia = fechaVigencia;
		this.porcentajeAgruSerExces = porcentajeAgruSerExces;
	}

	public long getCodigo() {
		return this.codigo;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	public ExcepTarifasContrato getExcepTarifasContrato() {
		return this.excepTarifasContrato;
	}

	public void setExcepTarifasContrato(
			ExcepTarifasContrato excepTarifasContrato) {
		this.excepTarifasContrato = excepTarifasContrato;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public TiposServicio getTiposServicio() {
		return this.tiposServicio;
	}

	public void setTiposServicio(TiposServicio tiposServicio) {
		this.tiposServicio = tiposServicio;
	}

	public Especialidades getEspecialidades() {
		return this.especialidades;
	}

	public void setEspecialidades(Especialidades especialidades) {
		this.especialidades = especialidades;
	}

	public GruposServicios getGruposServicios() {
		return this.gruposServicios;
	}

	public void setGruposServicios(GruposServicios gruposServicios) {
		this.gruposServicios = gruposServicios;
	}

	public Character getPos() {
		return this.pos;
	}

	public void setPos(Character pos) {
		this.pos = pos;
	}

	public BigDecimal getValorAjuste() {
		return this.valorAjuste;
	}

	public void setValorAjuste(BigDecimal valorAjuste) {
		this.valorAjuste = valorAjuste;
	}

	public BigDecimal getNuevaTarifa() {
		return this.nuevaTarifa;
	}

	public void setNuevaTarifa(BigDecimal nuevaTarifa) {
		this.nuevaTarifa = nuevaTarifa;
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

	public Date getFechaVigencia() {
		return this.fechaVigencia;
	}

	public void setFechaVigencia(Date fechaVigencia) {
		this.fechaVigencia = fechaVigencia;
	}

	public Set getPorcentajeAgruSerExces() {
		return this.porcentajeAgruSerExces;
	}

	public void setPorcentajeAgruSerExces(Set porcentajeAgruSerExces) {
		this.porcentajeAgruSerExces = porcentajeAgruSerExces;
	}

}
