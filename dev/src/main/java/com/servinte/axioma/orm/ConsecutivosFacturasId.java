package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;

/**
 * ConsecutivosFacturasId generated by hbm2java
 */
public class ConsecutivosFacturasId implements java.io.Serializable {

	private int codigo;
	private String nombreHilo;
	private Integer cons;
	private Date hora;

	public ConsecutivosFacturasId() {
	}

	public ConsecutivosFacturasId(int codigo) {
		this.codigo = codigo;
	}

	public ConsecutivosFacturasId(int codigo, String nombreHilo, Integer cons,
			Date hora) {
		this.codigo = codigo;
		this.nombreHilo = nombreHilo;
		this.cons = cons;
		this.hora = hora;
	}

	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getNombreHilo() {
		return this.nombreHilo;
	}

	public void setNombreHilo(String nombreHilo) {
		this.nombreHilo = nombreHilo;
	}

	public Integer getCons() {
		return this.cons;
	}

	public void setCons(Integer cons) {
		this.cons = cons;
	}

	public Date getHora() {
		return this.hora;
	}

	public void setHora(Date hora) {
		this.hora = hora;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ConsecutivosFacturasId))
			return false;
		ConsecutivosFacturasId castOther = (ConsecutivosFacturasId) other;

		return (this.getCodigo() == castOther.getCodigo())
				&& ((this.getNombreHilo() == castOther.getNombreHilo()) || (this
						.getNombreHilo() != null
						&& castOther.getNombreHilo() != null && this
						.getNombreHilo().equals(castOther.getNombreHilo())))
				&& ((this.getCons() == castOther.getCons()) || (this.getCons() != null
						&& castOther.getCons() != null && this.getCons()
						.equals(castOther.getCons())))
				&& ((this.getHora() == castOther.getHora()) || (this.getHora() != null
						&& castOther.getHora() != null && this.getHora()
						.equals(castOther.getHora())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getCodigo();
		result = 37
				* result
				+ (getNombreHilo() == null ? 0 : this.getNombreHilo()
						.hashCode());
		result = 37 * result
				+ (getCons() == null ? 0 : this.getCons().hashCode());
		result = 37 * result
				+ (getHora() == null ? 0 : this.getHora().hashCode());
		return result;
	}

}
