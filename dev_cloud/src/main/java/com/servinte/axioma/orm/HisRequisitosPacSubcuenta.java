package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * HisRequisitosPacSubcuenta generated by hbm2java
 */
public class HisRequisitosPacSubcuenta implements java.io.Serializable {

	private HisRequisitosPacSubcuentaId id;
	private Ingresos ingresos;
	private Convenios convenios;
	private Boolean cumplido;

	public HisRequisitosPacSubcuenta() {
	}

	public HisRequisitosPacSubcuenta(HisRequisitosPacSubcuentaId id,
			Ingresos ingresos, Convenios convenios) {
		this.id = id;
		this.ingresos = ingresos;
		this.convenios = convenios;
	}

	public HisRequisitosPacSubcuenta(HisRequisitosPacSubcuentaId id,
			Ingresos ingresos, Convenios convenios, Boolean cumplido) {
		this.id = id;
		this.ingresos = ingresos;
		this.convenios = convenios;
		this.cumplido = cumplido;
	}

	public HisRequisitosPacSubcuentaId getId() {
		return this.id;
	}

	public void setId(HisRequisitosPacSubcuentaId id) {
		this.id = id;
	}

	public Ingresos getIngresos() {
		return this.ingresos;
	}

	public void setIngresos(Ingresos ingresos) {
		this.ingresos = ingresos;
	}

	public Convenios getConvenios() {
		return this.convenios;
	}

	public void setConvenios(Convenios convenios) {
		this.convenios = convenios;
	}

	public Boolean getCumplido() {
		return this.cumplido;
	}

	public void setCumplido(Boolean cumplido) {
		this.cumplido = cumplido;
	}

}
