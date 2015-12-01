package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * CuentaRegimen generated by hbm2java
 */
public class CuentaRegimen implements java.io.Serializable {

	private CuentaRegimenId id;
	private TiposRegimen tiposRegimen;
	private TipoCuenta tipoCuenta;
	private CuentasContables cuentasContables;
	private RubroPresupuestal rubroPresupuestal;
	private Instituciones instituciones;
	private String valor;

	public CuentaRegimen() {
	}

	public CuentaRegimen(CuentaRegimenId id, TiposRegimen tiposRegimen,
			TipoCuenta tipoCuenta, Instituciones instituciones) {
		this.id = id;
		this.tiposRegimen = tiposRegimen;
		this.tipoCuenta = tipoCuenta;
		this.instituciones = instituciones;
	}

	public CuentaRegimen(CuentaRegimenId id, TiposRegimen tiposRegimen,
			TipoCuenta tipoCuenta, CuentasContables cuentasContables,
			RubroPresupuestal rubroPresupuestal, Instituciones instituciones,
			String valor) {
		this.id = id;
		this.tiposRegimen = tiposRegimen;
		this.tipoCuenta = tipoCuenta;
		this.cuentasContables = cuentasContables;
		this.rubroPresupuestal = rubroPresupuestal;
		this.instituciones = instituciones;
		this.valor = valor;
	}

	public CuentaRegimenId getId() {
		return this.id;
	}

	public void setId(CuentaRegimenId id) {
		this.id = id;
	}

	public TiposRegimen getTiposRegimen() {
		return this.tiposRegimen;
	}

	public void setTiposRegimen(TiposRegimen tiposRegimen) {
		this.tiposRegimen = tiposRegimen;
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
