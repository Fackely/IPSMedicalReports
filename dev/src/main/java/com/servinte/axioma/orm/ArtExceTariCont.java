package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * ArtExceTariCont generated by hbm2java
 */
public class ArtExceTariCont implements java.io.Serializable {

	private long codigo;
	private ExcepTarifasContrato excepTarifasContrato;
	private Usuarios usuarios;
	private int codigoArticulo;
	private BigDecimal valorAjuste;
	private String baseExcepcion;
	private BigDecimal nuevaTarifa;
	private Date fechaModifica;
	private String horaModifica;
	private Date fechaVigencia;
	private BigDecimal valorBase;
	private Set porcentajeArtExces = new HashSet(0);

	public ArtExceTariCont() {
	}

	public ArtExceTariCont(long codigo,
			ExcepTarifasContrato excepTarifasContrato, Usuarios usuarios,
			int codigoArticulo, Date fechaModifica, String horaModifica,
			Date fechaVigencia) {
		this.codigo = codigo;
		this.excepTarifasContrato = excepTarifasContrato;
		this.usuarios = usuarios;
		this.codigoArticulo = codigoArticulo;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.fechaVigencia = fechaVigencia;
	}

	public ArtExceTariCont(long codigo,
			ExcepTarifasContrato excepTarifasContrato, Usuarios usuarios,
			int codigoArticulo, BigDecimal valorAjuste, String baseExcepcion,
			BigDecimal nuevaTarifa, Date fechaModifica, String horaModifica,
			Date fechaVigencia, BigDecimal valorBase, Set porcentajeArtExces) {
		this.codigo = codigo;
		this.excepTarifasContrato = excepTarifasContrato;
		this.usuarios = usuarios;
		this.codigoArticulo = codigoArticulo;
		this.valorAjuste = valorAjuste;
		this.baseExcepcion = baseExcepcion;
		this.nuevaTarifa = nuevaTarifa;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.fechaVigencia = fechaVigencia;
		this.valorBase = valorBase;
		this.porcentajeArtExces = porcentajeArtExces;
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

	public int getCodigoArticulo() {
		return this.codigoArticulo;
	}

	public void setCodigoArticulo(int codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}

	public BigDecimal getValorAjuste() {
		return this.valorAjuste;
	}

	public void setValorAjuste(BigDecimal valorAjuste) {
		this.valorAjuste = valorAjuste;
	}

	public String getBaseExcepcion() {
		return this.baseExcepcion;
	}

	public void setBaseExcepcion(String baseExcepcion) {
		this.baseExcepcion = baseExcepcion;
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

	public BigDecimal getValorBase() {
		return this.valorBase;
	}

	public void setValorBase(BigDecimal valorBase) {
		this.valorBase = valorBase;
	}

	public Set getPorcentajeArtExces() {
		return this.porcentajeArtExces;
	}

	public void setPorcentajeArtExces(Set porcentajeArtExces) {
		this.porcentajeArtExces = porcentajeArtExces;
	}

}
