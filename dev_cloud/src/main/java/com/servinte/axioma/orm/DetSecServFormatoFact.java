package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * DetSecServFormatoFact generated by hbm2java
 */
public class DetSecServFormatoFact implements java.io.Serializable {

	private DetSecServFormatoFactId id;
	private TipoRompimientoServ tipoRompimientoServ;
	private SecServFormatoImpFact secServFormatoImpFact;
	private NivelRompimientoServ nivelRompimientoServ;
	private Boolean imprimirDetalle;
	private Boolean imprimirSubtotal;

	public DetSecServFormatoFact() {
	}

	public DetSecServFormatoFact(DetSecServFormatoFactId id,
			SecServFormatoImpFact secServFormatoImpFact,
			NivelRompimientoServ nivelRompimientoServ) {
		this.id = id;
		this.secServFormatoImpFact = secServFormatoImpFact;
		this.nivelRompimientoServ = nivelRompimientoServ;
	}

	public DetSecServFormatoFact(DetSecServFormatoFactId id,
			TipoRompimientoServ tipoRompimientoServ,
			SecServFormatoImpFact secServFormatoImpFact,
			NivelRompimientoServ nivelRompimientoServ, Boolean imprimirDetalle,
			Boolean imprimirSubtotal) {
		this.id = id;
		this.tipoRompimientoServ = tipoRompimientoServ;
		this.secServFormatoImpFact = secServFormatoImpFact;
		this.nivelRompimientoServ = nivelRompimientoServ;
		this.imprimirDetalle = imprimirDetalle;
		this.imprimirSubtotal = imprimirSubtotal;
	}

	public DetSecServFormatoFactId getId() {
		return this.id;
	}

	public void setId(DetSecServFormatoFactId id) {
		this.id = id;
	}

	public TipoRompimientoServ getTipoRompimientoServ() {
		return this.tipoRompimientoServ;
	}

	public void setTipoRompimientoServ(TipoRompimientoServ tipoRompimientoServ) {
		this.tipoRompimientoServ = tipoRompimientoServ;
	}

	public SecServFormatoImpFact getSecServFormatoImpFact() {
		return this.secServFormatoImpFact;
	}

	public void setSecServFormatoImpFact(
			SecServFormatoImpFact secServFormatoImpFact) {
		this.secServFormatoImpFact = secServFormatoImpFact;
	}

	public NivelRompimientoServ getNivelRompimientoServ() {
		return this.nivelRompimientoServ;
	}

	public void setNivelRompimientoServ(
			NivelRompimientoServ nivelRompimientoServ) {
		this.nivelRompimientoServ = nivelRompimientoServ;
	}

	public Boolean getImprimirDetalle() {
		return this.imprimirDetalle;
	}

	public void setImprimirDetalle(Boolean imprimirDetalle) {
		this.imprimirDetalle = imprimirDetalle;
	}

	public Boolean getImprimirSubtotal() {
		return this.imprimirSubtotal;
	}

	public void setImprimirSubtotal(Boolean imprimirSubtotal) {
		this.imprimirSubtotal = imprimirSubtotal;
	}

}
