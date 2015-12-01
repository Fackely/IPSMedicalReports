package com.servinte.axioma.orm;

// Generated May 3, 2010 4:30:54 PM by Hibernate Tools 3.2.5.Beta

/**
 * ClasOdoSecServicios generated by hbm2java
 */
public class ClasOdoSecServicios implements java.io.Serializable {

	private int codigoPk;
	private Servicios servicios;
	private ClasificaPacientesOdo clasificaPacientesOdo;

	public ClasOdoSecServicios() {
	}

	public ClasOdoSecServicios(int codigoPk, Servicios servicios,
			ClasificaPacientesOdo clasificaPacientesOdo) {
		this.codigoPk = codigoPk;
		this.servicios = servicios;
		this.clasificaPacientesOdo = clasificaPacientesOdo;
	}

	public int getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public Servicios getServicios() {
		return this.servicios;
	}

	public void setServicios(Servicios servicios) {
		this.servicios = servicios;
	}

	public ClasificaPacientesOdo getClasificaPacientesOdo() {
		return this.clasificaPacientesOdo;
	}

	public void setClasificaPacientesOdo(
			ClasificaPacientesOdo clasificaPacientesOdo) {
		this.clasificaPacientesOdo = clasificaPacientesOdo;
	}

}
