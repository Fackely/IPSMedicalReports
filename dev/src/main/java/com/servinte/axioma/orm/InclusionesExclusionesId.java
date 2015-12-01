package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * InclusionesExclusionesId generated by hbm2java
 */
public class InclusionesExclusionesId implements java.io.Serializable {

	private String codigo;
	private int institucion;

	public InclusionesExclusionesId() {
	}

	public InclusionesExclusionesId(String codigo, int institucion) {
		this.codigo = codigo;
		this.institucion = institucion;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
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
		if (!(other instanceof InclusionesExclusionesId))
			return false;
		InclusionesExclusionesId castOther = (InclusionesExclusionesId) other;

		return ((this.getCodigo() == castOther.getCodigo()) || (this
				.getCodigo() != null
				&& castOther.getCodigo() != null && this.getCodigo().equals(
				castOther.getCodigo())))
				&& (this.getInstitucion() == castOther.getInstitucion());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getCodigo() == null ? 0 : this.getCodigo().hashCode());
		result = 37 * result + this.getInstitucion();
		return result;
	}

}
