package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * DiagnosticosId generated by hbm2java
 */
public class DiagnosticosId implements java.io.Serializable {

	private String acronimo;
	private int tipoCie;

	public DiagnosticosId() {
	}

	public DiagnosticosId(String acronimo, int tipoCie) {
		this.acronimo = acronimo;
		this.tipoCie = tipoCie;
	}

	public String getAcronimo() {
		return this.acronimo;
	}

	public void setAcronimo(String acronimo) {
		this.acronimo = acronimo;
	}

	public int getTipoCie() {
		return this.tipoCie;
	}

	public void setTipoCie(int tipoCie) {
		this.tipoCie = tipoCie;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof DiagnosticosId))
			return false;
		DiagnosticosId castOther = (DiagnosticosId) other;

		return ((this.getAcronimo() == castOther.getAcronimo()) || (this
				.getAcronimo() != null
				&& castOther.getAcronimo() != null && this.getAcronimo()
				.equals(castOther.getAcronimo())))
				&& (this.getTipoCie() == castOther.getTipoCie());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getAcronimo() == null ? 0 : this.getAcronimo().hashCode());
		result = 37 * result + this.getTipoCie();
		return result;
	}

}
