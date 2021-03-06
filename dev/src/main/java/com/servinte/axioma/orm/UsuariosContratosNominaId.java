package com.servinte.axioma.orm;

// Generated Jan 4, 2011 9:13:38 AM by Hibernate Tools 3.2.4.GA

/**
 * UsuariosContratosNominaId generated by hbm2java
 */
public class UsuariosContratosNominaId implements java.io.Serializable {

	private String login;
	private long contratoNomina;

	public UsuariosContratosNominaId() {
	}

	public UsuariosContratosNominaId(String login, long contratoNomina) {
		this.login = login;
		this.contratoNomina = contratoNomina;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public long getContratoNomina() {
		return this.contratoNomina;
	}

	public void setContratoNomina(long contratoNomina) {
		this.contratoNomina = contratoNomina;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof UsuariosContratosNominaId))
			return false;
		UsuariosContratosNominaId castOther = (UsuariosContratosNominaId) other;

		return ((this.getLogin() == castOther.getLogin()) || (this.getLogin() != null
				&& castOther.getLogin() != null && this.getLogin().equals(
				castOther.getLogin())))
				&& (this.getContratoNomina() == castOther.getContratoNomina());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getLogin() == null ? 0 : this.getLogin().hashCode());
		result = 37 * result + (int) this.getContratoNomina();
		return result;
	}

}
