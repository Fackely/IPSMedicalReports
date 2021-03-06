package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * IncluexcluContrato generated by hbm2java
 */
public class IncluexcluContrato implements java.io.Serializable {

	private IncluexcluContratoId id;
	private Contratos contratos;
	private Usuarios usuarios;
	private IncluExcluCc incluExcluCc;
	private Instituciones instituciones;

	public IncluexcluContrato() {
	}

	public IncluexcluContrato(IncluexcluContratoId id, Contratos contratos,
			Usuarios usuarios, Instituciones instituciones) {
		this.id = id;
		this.contratos = contratos;
		this.usuarios = usuarios;
		this.instituciones = instituciones;
	}

	public IncluexcluContrato(IncluexcluContratoId id, Contratos contratos,
			Usuarios usuarios, IncluExcluCc incluExcluCc,
			Instituciones instituciones) {
		this.id = id;
		this.contratos = contratos;
		this.usuarios = usuarios;
		this.incluExcluCc = incluExcluCc;
		this.instituciones = instituciones;
	}

	public IncluexcluContratoId getId() {
		return this.id;
	}

	public void setId(IncluexcluContratoId id) {
		this.id = id;
	}

	public Contratos getContratos() {
		return this.contratos;
	}

	public void setContratos(Contratos contratos) {
		this.contratos = contratos;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public IncluExcluCc getIncluExcluCc() {
		return this.incluExcluCc;
	}

	public void setIncluExcluCc(IncluExcluCc incluExcluCc) {
		this.incluExcluCc = incluExcluCc;
	}

	public Instituciones getInstituciones() {
		return this.instituciones;
	}

	public void setInstituciones(Instituciones instituciones) {
		this.instituciones = instituciones;
	}

}
