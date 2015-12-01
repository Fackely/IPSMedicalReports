package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * EsqTarInvtSubCuentas generated by hbm2java
 */
public class EsqTarInvtSubCuentas implements java.io.Serializable {

	private long codigo;
	private SubCuentas subCuentas;
	private EsquemasTarifarios esquemasTarifarios;
	private Integer claseInventario;

	public EsqTarInvtSubCuentas() {
	}

	public EsqTarInvtSubCuentas(long codigo, SubCuentas subCuentas,
			EsquemasTarifarios esquemasTarifarios) {
		this.codigo = codigo;
		this.subCuentas = subCuentas;
		this.esquemasTarifarios = esquemasTarifarios;
	}

	public EsqTarInvtSubCuentas(long codigo, SubCuentas subCuentas,
			EsquemasTarifarios esquemasTarifarios, Integer claseInventario) {
		this.codigo = codigo;
		this.subCuentas = subCuentas;
		this.esquemasTarifarios = esquemasTarifarios;
		this.claseInventario = claseInventario;
	}

	public long getCodigo() {
		return this.codigo;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	public SubCuentas getSubCuentas() {
		return this.subCuentas;
	}

	public void setSubCuentas(SubCuentas subCuentas) {
		this.subCuentas = subCuentas;
	}

	public EsquemasTarifarios getEsquemasTarifarios() {
		return this.esquemasTarifarios;
	}

	public void setEsquemasTarifarios(EsquemasTarifarios esquemasTarifarios) {
		this.esquemasTarifarios = esquemasTarifarios;
	}

	public Integer getClaseInventario() {
		return this.claseInventario;
	}

	public void setClaseInventario(Integer claseInventario) {
		this.claseInventario = claseInventario;
	}

}
