package com.servinte.axioma.orm;

// Generated Jan 23, 2011 12:02:57 PM by Hibernate Tools 3.2.4.GA

/**
 * IncluPresProgramaPromo generated by hbm2java
 */
public class IncluPresProgramaPromo implements java.io.Serializable {

	private long codigoPk;
	private DetPromocionesOdo detPromocionesOdo;
	private IncluProgramaConvenio incluProgramaConvenio;

	public IncluPresProgramaPromo() {
	}

	public IncluPresProgramaPromo(long codigoPk,
			DetPromocionesOdo detPromocionesOdo,
			IncluProgramaConvenio incluProgramaConvenio) {
		this.codigoPk = codigoPk;
		this.detPromocionesOdo = detPromocionesOdo;
		this.incluProgramaConvenio = incluProgramaConvenio;
	}

	public long getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public DetPromocionesOdo getDetPromocionesOdo() {
		return this.detPromocionesOdo;
	}

	public void setDetPromocionesOdo(DetPromocionesOdo detPromocionesOdo) {
		this.detPromocionesOdo = detPromocionesOdo;
	}

	public IncluProgramaConvenio getIncluProgramaConvenio() {
		return this.incluProgramaConvenio;
	}

	public void setIncluProgramaConvenio(
			IncluProgramaConvenio incluProgramaConvenio) {
		this.incluProgramaConvenio = incluProgramaConvenio;
	}

}
