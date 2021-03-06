package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * DepartamentosId generated by hbm2java
 */
public class DepartamentosId implements java.io.Serializable {

	private String codigoDepartamento;
	private String codigoPais;

	public DepartamentosId() {
	}

	public DepartamentosId(String codigoDepartamento, String codigoPais) {
		this.codigoDepartamento = codigoDepartamento;
		this.codigoPais = codigoPais;
	}

	public String getCodigoDepartamento() {
		return this.codigoDepartamento;
	}

	public void setCodigoDepartamento(String codigoDepartamento) {
		this.codigoDepartamento = codigoDepartamento;
	}

	public String getCodigoPais() {
		return this.codigoPais;
	}

	public void setCodigoPais(String codigoPais) {
		this.codigoPais = codigoPais;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof DepartamentosId))
			return false;
		DepartamentosId castOther = (DepartamentosId) other;

		return ((this.getCodigoDepartamento() == castOther
				.getCodigoDepartamento()) || (this.getCodigoDepartamento() != null
				&& castOther.getCodigoDepartamento() != null && this
				.getCodigoDepartamento().equals(
						castOther.getCodigoDepartamento())))
				&& ((this.getCodigoPais() == castOther.getCodigoPais()) || (this
						.getCodigoPais() != null
						&& castOther.getCodigoPais() != null && this
						.getCodigoPais().equals(castOther.getCodigoPais())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getCodigoDepartamento() == null ? 0 : this
						.getCodigoDepartamento().hashCode());
		result = 37
				* result
				+ (getCodigoPais() == null ? 0 : this.getCodigoPais()
						.hashCode());
		return result;
	}

}
