package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * EspeciServCueIngId generated by hbm2java
 */
public class EspeciServCueIngId implements java.io.Serializable {

	private int grupoServicio;
	private String tipoServicio;
	private int especialidadServicio;
	private int centroCosto;

	public EspeciServCueIngId() {
	}

	public EspeciServCueIngId(int grupoServicio, String tipoServicio,
			int especialidadServicio, int centroCosto) {
		this.grupoServicio = grupoServicio;
		this.tipoServicio = tipoServicio;
		this.especialidadServicio = especialidadServicio;
		this.centroCosto = centroCosto;
	}

	public int getGrupoServicio() {
		return this.grupoServicio;
	}

	public void setGrupoServicio(int grupoServicio) {
		this.grupoServicio = grupoServicio;
	}

	public String getTipoServicio() {
		return this.tipoServicio;
	}

	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}

	public int getEspecialidadServicio() {
		return this.especialidadServicio;
	}

	public void setEspecialidadServicio(int especialidadServicio) {
		this.especialidadServicio = especialidadServicio;
	}

	public int getCentroCosto() {
		return this.centroCosto;
	}

	public void setCentroCosto(int centroCosto) {
		this.centroCosto = centroCosto;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof EspeciServCueIngId))
			return false;
		EspeciServCueIngId castOther = (EspeciServCueIngId) other;

		return (this.getGrupoServicio() == castOther.getGrupoServicio())
				&& ((this.getTipoServicio() == castOther.getTipoServicio()) || (this
						.getTipoServicio() != null
						&& castOther.getTipoServicio() != null && this
						.getTipoServicio().equals(castOther.getTipoServicio())))
				&& (this.getEspecialidadServicio() == castOther
						.getEspecialidadServicio())
				&& (this.getCentroCosto() == castOther.getCentroCosto());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getGrupoServicio();
		result = 37
				* result
				+ (getTipoServicio() == null ? 0 : this.getTipoServicio()
						.hashCode());
		result = 37 * result + this.getEspecialidadServicio();
		result = 37 * result + this.getCentroCosto();
		return result;
	}

}
