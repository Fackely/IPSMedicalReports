package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * CoberturasXContratoId generated by hbm2java
 */
public class CoberturasXContratoId implements java.io.Serializable {

	private int codigoContrato;
	private String codigoCobertura;
	private int institucion;

	public CoberturasXContratoId() {
	}

	public CoberturasXContratoId(int codigoContrato, String codigoCobertura,
			int institucion) {
		this.codigoContrato = codigoContrato;
		this.codigoCobertura = codigoCobertura;
		this.institucion = institucion;
	}

	public int getCodigoContrato() {
		return this.codigoContrato;
	}

	public void setCodigoContrato(int codigoContrato) {
		this.codigoContrato = codigoContrato;
	}

	public String getCodigoCobertura() {
		return this.codigoCobertura;
	}

	public void setCodigoCobertura(String codigoCobertura) {
		this.codigoCobertura = codigoCobertura;
	}

	public int getInstitucion() {
		return this.institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CoberturasXContratoId))
			return false;
		CoberturasXContratoId castOther = (CoberturasXContratoId) other;

		return (this.getCodigoContrato() == castOther.getCodigoContrato())
				&& ((this.getCodigoCobertura() == castOther
						.getCodigoCobertura()) || (this.getCodigoCobertura() != null
						&& castOther.getCodigoCobertura() != null && this
						.getCodigoCobertura().equals(
								castOther.getCodigoCobertura())))
				&& (this.getInstitucion() == castOther.getInstitucion());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getCodigoContrato();
		result = 37
				* result
				+ (getCodigoCobertura() == null ? 0 : this.getCodigoCobertura()
						.hashCode());
		result = 37 * result + this.getInstitucion();
		return result;
	}

}
