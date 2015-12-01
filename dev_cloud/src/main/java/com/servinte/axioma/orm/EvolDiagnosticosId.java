package com.servinte.axioma.orm;

// Generated 21/07/2011 03:06:42 PM by Hibernate Tools 3.4.0.CR1

/**
 * EvolDiagnosticosId generated by hbm2java
 */
public class EvolDiagnosticosId implements java.io.Serializable {

	private int evolucion;
	private String acronimoDiagnostico;
	private int tipoCieDiagnostico;
	private Boolean principal;
	private Boolean definitivo;

	public EvolDiagnosticosId() {
	}

	public EvolDiagnosticosId(int evolucion, String acronimoDiagnostico,
			int tipoCieDiagnostico, Boolean principal, Boolean definitivo) {
		this.evolucion = evolucion;
		this.acronimoDiagnostico = acronimoDiagnostico;
		this.tipoCieDiagnostico = tipoCieDiagnostico;
		this.principal = principal;
		this.definitivo = definitivo;
	}

	public int getEvolucion() {
		return this.evolucion;
	}

	public void setEvolucion(int evolucion) {
		this.evolucion = evolucion;
	}

	public String getAcronimoDiagnostico() {
		return this.acronimoDiagnostico;
	}

	public void setAcronimoDiagnostico(String acronimoDiagnostico) {
		this.acronimoDiagnostico = acronimoDiagnostico;
	}

	public int getTipoCieDiagnostico() {
		return this.tipoCieDiagnostico;
	}

	public void setTipoCieDiagnostico(int tipoCieDiagnostico) {
		this.tipoCieDiagnostico = tipoCieDiagnostico;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof EvolDiagnosticosId))
			return false;
		EvolDiagnosticosId castOther = (EvolDiagnosticosId) other;

		return (this.getEvolucion() == castOther.getEvolucion())
				&& ((this.getAcronimoDiagnostico() == castOther
						.getAcronimoDiagnostico()) || (this
						.getAcronimoDiagnostico() != null
						&& castOther.getAcronimoDiagnostico() != null && this
						.getAcronimoDiagnostico().equals(
								castOther.getAcronimoDiagnostico())))
				&& (this.getTipoCieDiagnostico() == castOther
						.getTipoCieDiagnostico())
				&& (this.getPrincipal() == castOther.getPrincipal())
				&& (this.getDefinitivo() == castOther.getDefinitivo());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getEvolucion();
		result = 37
				* result
				+ (getAcronimoDiagnostico() == null ? 0 : this
						.getAcronimoDiagnostico().hashCode());
		result = 37 * result + this.getTipoCieDiagnostico();
		result = 37 * result + (this.getPrincipal() == null ? 0 : this.getPrincipal().hashCode());
		result = 37 * result + (this.getDefinitivo() == null ? 0 : this.getDefinitivo().hashCode());
		return result;
	}

	/**
	 * @return valor de principal
	 */
	public Boolean getPrincipal() {
		return principal;
	}

	/**
	 * @param principal el principal para asignar
	 */
	public void setPrincipal(Boolean principal) {
		this.principal = principal;
	}

	/**
	 * @return valor de definitivo
	 */
	public Boolean getDefinitivo() {
		return definitivo;
	}

	/**
	 * @param definitivo el definitivo para asignar
	 */
	public void setDefinitivo(Boolean definitivo) {
		this.definitivo = definitivo;
	}

}
