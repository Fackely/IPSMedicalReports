package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.math.BigDecimal;
import java.util.Date;

/**
 * PoolesEsquemaTarifario generated by hbm2java
 */
public class PoolesEsquemaTarifario implements java.io.Serializable {

	private PoolesEsquemaTarifarioId id;
	private CuentasContables cuentasContablesByCuentaContablePool;
	private EsquemasTarifarios esquemasTarifarios;
	private CuentasContables cuentasContablesByCueContInstVigAnterior;
	private Pooles pooles;
	private CuentasContables cuentasContablesByCuentaContableIns;
	private Double porcentajeParticipacion;
	private int institucion;
	private String usuarioModifica;
	private Date fechaModifica;
	private String horaModifica;
	private BigDecimal valorParticipacion;

	public PoolesEsquemaTarifario() {
	}

	public PoolesEsquemaTarifario(PoolesEsquemaTarifarioId id,
			EsquemasTarifarios esquemasTarifarios, Pooles pooles,
			int institucion) {
		this.id = id;
		this.esquemasTarifarios = esquemasTarifarios;
		this.pooles = pooles;
		this.institucion = institucion;
	}

	public PoolesEsquemaTarifario(PoolesEsquemaTarifarioId id,
			CuentasContables cuentasContablesByCuentaContablePool,
			EsquemasTarifarios esquemasTarifarios,
			CuentasContables cuentasContablesByCueContInstVigAnterior,
			Pooles pooles,
			CuentasContables cuentasContablesByCuentaContableIns,
			Double porcentajeParticipacion, int institucion,
			String usuarioModifica, Date fechaModifica, String horaModifica,
			BigDecimal valorParticipacion) {
		this.id = id;
		this.cuentasContablesByCuentaContablePool = cuentasContablesByCuentaContablePool;
		this.esquemasTarifarios = esquemasTarifarios;
		this.cuentasContablesByCueContInstVigAnterior = cuentasContablesByCueContInstVigAnterior;
		this.pooles = pooles;
		this.cuentasContablesByCuentaContableIns = cuentasContablesByCuentaContableIns;
		this.porcentajeParticipacion = porcentajeParticipacion;
		this.institucion = institucion;
		this.usuarioModifica = usuarioModifica;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.valorParticipacion = valorParticipacion;
	}

	public PoolesEsquemaTarifarioId getId() {
		return this.id;
	}

	public void setId(PoolesEsquemaTarifarioId id) {
		this.id = id;
	}

	public CuentasContables getCuentasContablesByCuentaContablePool() {
		return this.cuentasContablesByCuentaContablePool;
	}

	public void setCuentasContablesByCuentaContablePool(
			CuentasContables cuentasContablesByCuentaContablePool) {
		this.cuentasContablesByCuentaContablePool = cuentasContablesByCuentaContablePool;
	}

	public EsquemasTarifarios getEsquemasTarifarios() {
		return this.esquemasTarifarios;
	}

	public void setEsquemasTarifarios(EsquemasTarifarios esquemasTarifarios) {
		this.esquemasTarifarios = esquemasTarifarios;
	}

	public CuentasContables getCuentasContablesByCueContInstVigAnterior() {
		return this.cuentasContablesByCueContInstVigAnterior;
	}

	public void setCuentasContablesByCueContInstVigAnterior(
			CuentasContables cuentasContablesByCueContInstVigAnterior) {
		this.cuentasContablesByCueContInstVigAnterior = cuentasContablesByCueContInstVigAnterior;
	}

	public Pooles getPooles() {
		return this.pooles;
	}

	public void setPooles(Pooles pooles) {
		this.pooles = pooles;
	}

	public CuentasContables getCuentasContablesByCuentaContableIns() {
		return this.cuentasContablesByCuentaContableIns;
	}

	public void setCuentasContablesByCuentaContableIns(
			CuentasContables cuentasContablesByCuentaContableIns) {
		this.cuentasContablesByCuentaContableIns = cuentasContablesByCuentaContableIns;
	}

	public Double getPorcentajeParticipacion() {
		return this.porcentajeParticipacion;
	}

	public void setPorcentajeParticipacion(Double porcentajeParticipacion) {
		this.porcentajeParticipacion = porcentajeParticipacion;
	}

	public int getInstitucion() {
		return this.institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public String getUsuarioModifica() {
		return this.usuarioModifica;
	}

	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
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

	public BigDecimal getValorParticipacion() {
		return this.valorParticipacion;
	}

	public void setValorParticipacion(BigDecimal valorParticipacion) {
		this.valorParticipacion = valorParticipacion;
	}

}
