package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * ClasOdoSecEstadosPres generated by hbm2java
 */
public class ClasOdoSecEstadosPres implements java.io.Serializable {

	private int codigoPk;
	private ClasificaPacientesOdo clasificaPacientesOdo;
	private String estado;

	public ClasOdoSecEstadosPres() {
	}

	public ClasOdoSecEstadosPres(int codigoPk,
			ClasificaPacientesOdo clasificaPacientesOdo, String estado) {
		this.codigoPk = codigoPk;
		this.clasificaPacientesOdo = clasificaPacientesOdo;
		this.estado = estado;
	}

	public int getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public ClasificaPacientesOdo getClasificaPacientesOdo() {
		return this.clasificaPacientesOdo;
	}

	public void setClasificaPacientesOdo(
			ClasificaPacientesOdo clasificaPacientesOdo) {
		this.clasificaPacientesOdo = clasificaPacientesOdo;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

}
