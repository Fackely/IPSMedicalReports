package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * CuentaConvenio generated by hbm2java
 */
public class CuentaConvenio implements java.io.Serializable {

	private CuentaConvenioId id;
	private Convenios convenios;
	private TipoCuenta tipoCuenta;
	private CuentasContables cuentasContables;
	private RubroPresupuestal rubroPresupuestal;
	private Instituciones instituciones;
	private String valor;

	public CuentaConvenio() {
	}

	public CuentaConvenio(CuentaConvenioId id, Convenios convenios,
			TipoCuenta tipoCuenta, Instituciones instituciones) {
		this.id = id;
		this.convenios = convenios;
		this.tipoCuenta = tipoCuenta;
		this.instituciones = instituciones;
	}

	public CuentaConvenio(CuentaConvenioId id, Convenios convenios,
			TipoCuenta tipoCuenta, CuentasContables cuentasContables,
			RubroPresupuestal rubroPresupuestal, Instituciones instituciones,
			String valor) {
		this.id = id;
		this.convenios = convenios;
		this.tipoCuenta = tipoCuenta;
		this.cuentasContables = cuentasContables;
		this.rubroPresupuestal = rubroPresupuestal;
		this.instituciones = instituciones;
		this.valor = valor;
	}

	public CuentaConvenioId getId() {
		return this.id;
	}

	public void setId(CuentaConvenioId id) {
		this.id = id;
	}

	public Convenios getConvenios() {
		return this.convenios;
	}

	public void setConvenios(Convenios convenios) {
		this.convenios = convenios;
	}

	public TipoCuenta getTipoCuenta() {
		return this.tipoCuenta;
	}

	public void setTipoCuenta(TipoCuenta tipoCuenta) {
		this.tipoCuenta = tipoCuenta;
	}

	public CuentasContables getCuentasContables() {
		return this.cuentasContables;
	}

	public void setCuentasContables(CuentasContables cuentasContables) {
		this.cuentasContables = cuentasContables;
	}

	public RubroPresupuestal getRubroPresupuestal() {
		return this.rubroPresupuestal;
	}

	public void setRubroPresupuestal(RubroPresupuestal rubroPresupuestal) {
		this.rubroPresupuestal = rubroPresupuestal;
	}

	public Instituciones getInstituciones() {
		return this.instituciones;
	}

	public void setInstituciones(Instituciones instituciones) {
		this.instituciones = instituciones;
	}

	public String getValor() {
		return this.valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}
