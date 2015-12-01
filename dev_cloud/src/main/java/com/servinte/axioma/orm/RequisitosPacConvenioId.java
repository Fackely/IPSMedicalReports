package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * RequisitosPacConvenioId generated by hbm2java
 */
public class RequisitosPacConvenioId implements java.io.Serializable {

	private int requisitoPaciente;
	private int convenio;
	private int viaIngreso;

	public RequisitosPacConvenioId() {
	}

	public RequisitosPacConvenioId(int requisitoPaciente, int convenio,
			int viaIngreso) {
		this.requisitoPaciente = requisitoPaciente;
		this.convenio = convenio;
		this.viaIngreso = viaIngreso;
	}

	public int getRequisitoPaciente() {
		return this.requisitoPaciente;
	}

	public void setRequisitoPaciente(int requisitoPaciente) {
		this.requisitoPaciente = requisitoPaciente;
	}

	public int getConvenio() {
		return this.convenio;
	}

	public void setConvenio(int convenio) {
		this.convenio = convenio;
	}

	public int getViaIngreso() {
		return this.viaIngreso;
	}

	public void setViaIngreso(int viaIngreso) {
		this.viaIngreso = viaIngreso;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof RequisitosPacConvenioId))
			return false;
		RequisitosPacConvenioId castOther = (RequisitosPacConvenioId) other;

		return (this.getRequisitoPaciente() == castOther.getRequisitoPaciente())
				&& (this.getConvenio() == castOther.getConvenio())
				&& (this.getViaIngreso() == castOther.getViaIngreso());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getRequisitoPaciente();
		result = 37 * result + this.getConvenio();
		result = 37 * result + this.getViaIngreso();
		return result;
	}

}
