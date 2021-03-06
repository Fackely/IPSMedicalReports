package com.servinte.axioma.orm;

// Generated Sep 24, 2010 5:55:45 PM by Hibernate Tools 3.2.4.GA

/**
 * PrioridadUsuEsp generated by hbm2java
 */
public class PrioridadUsuEsp implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int codigoPk;
	private NivelAutorUsuEspec nivelAutorUsuEspec;
	private int nroPrioridad;

	public PrioridadUsuEsp() {
	}

	public PrioridadUsuEsp(int codigoPk, NivelAutorUsuEspec nivelAutorUsuEspec, int nroPrioridad) {
		this.codigoPk = codigoPk;
		this.nivelAutorUsuEspec = nivelAutorUsuEspec;
		this.nroPrioridad=nroPrioridad;
	}

	public int getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public NivelAutorUsuEspec getNivelAutorUsuEspec() {
		return this.nivelAutorUsuEspec;
	}

	public void setNivelAutorUsuEspec(NivelAutorUsuEspec nivelAutorUsuEspec) {
		this.nivelAutorUsuEspec = nivelAutorUsuEspec;
	}

	/**
	 * @return the nroPrioridad
	 */
	public int getNroPrioridad() {
		return nroPrioridad;
	}

	/**
	 * @param nroPrioridad the nroPrioridad to set
	 */
	public void setNroPrioridad(int nroPrioridad) {
		this.nroPrioridad = nroPrioridad;
	}

}
