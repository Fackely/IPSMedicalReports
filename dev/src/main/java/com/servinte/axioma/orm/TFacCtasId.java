package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * TFacCtasId generated by hbm2java
 */
public class TFacCtasId implements java.io.Serializable {

	private Integer consecutivoFactura;
	private Integer cuenta;
	private Double valorNetoPaciente;
	private Double valorBrutoPac;

	public TFacCtasId() {
	}

	public TFacCtasId(Integer consecutivoFactura, Integer cuenta,
			Double valorNetoPaciente, Double valorBrutoPac) {
		this.consecutivoFactura = consecutivoFactura;
		this.cuenta = cuenta;
		this.valorNetoPaciente = valorNetoPaciente;
		this.valorBrutoPac = valorBrutoPac;
	}

	public Integer getConsecutivoFactura() {
		return this.consecutivoFactura;
	}

	public void setConsecutivoFactura(Integer consecutivoFactura) {
		this.consecutivoFactura = consecutivoFactura;
	}

	public Integer getCuenta() {
		return this.cuenta;
	}

	public void setCuenta(Integer cuenta) {
		this.cuenta = cuenta;
	}

	public Double getValorNetoPaciente() {
		return this.valorNetoPaciente;
	}

	public void setValorNetoPaciente(Double valorNetoPaciente) {
		this.valorNetoPaciente = valorNetoPaciente;
	}

	public Double getValorBrutoPac() {
		return this.valorBrutoPac;
	}

	public void setValorBrutoPac(Double valorBrutoPac) {
		this.valorBrutoPac = valorBrutoPac;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TFacCtasId))
			return false;
		TFacCtasId castOther = (TFacCtasId) other;

		return ((this.getConsecutivoFactura() == castOther
				.getConsecutivoFactura()) || (this.getConsecutivoFactura() != null
				&& castOther.getConsecutivoFactura() != null && this
				.getConsecutivoFactura().equals(
						castOther.getConsecutivoFactura())))
				&& ((this.getCuenta() == castOther.getCuenta()) || (this
						.getCuenta() != null
						&& castOther.getCuenta() != null && this.getCuenta()
						.equals(castOther.getCuenta())))
				&& ((this.getValorNetoPaciente() == castOther
						.getValorNetoPaciente()) || (this
						.getValorNetoPaciente() != null
						&& castOther.getValorNetoPaciente() != null && this
						.getValorNetoPaciente().equals(
								castOther.getValorNetoPaciente())))
				&& ((this.getValorBrutoPac() == castOther.getValorBrutoPac()) || (this
						.getValorBrutoPac() != null
						&& castOther.getValorBrutoPac() != null && this
						.getValorBrutoPac()
						.equals(castOther.getValorBrutoPac())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getConsecutivoFactura() == null ? 0 : this
						.getConsecutivoFactura().hashCode());
		result = 37 * result
				+ (getCuenta() == null ? 0 : this.getCuenta().hashCode());
		result = 37
				* result
				+ (getValorNetoPaciente() == null ? 0 : this
						.getValorNetoPaciente().hashCode());
		result = 37
				* result
				+ (getValorBrutoPac() == null ? 0 : this.getValorBrutoPac()
						.hashCode());
		return result;
	}

}
