package com.servinte.axioma.orm;

// Generated Feb 15, 2011 5:27:21 PM by Hibernate Tools 3.2.4.GA

/**
 * CertAtenMedicaFurips generated by hbm2java
 */
public class CertAtenMedicaFurips implements java.io.Serializable {

	private long codigoReclamacion;
	private Diagnosticos diagnosticosByPkCeratemedfuripsDxing;
	private TiposIdentificacion tiposIdentificacion;
	private Medicos medicos;
	private ReclamacionesAccEveFact reclamacionesAccEveFact;
	private Diagnosticos diagnosticosByPkCeratemedfuripsRelegr2;
	private Diagnosticos diagnosticosByPkCeratemedfuripsReling2;
	private Diagnosticos diagnosticosByPkCeratemedfuripsRelegr1;
	private Diagnosticos diagnosticosByPkCeratemedfuripsDxegr;
	private Diagnosticos diagnosticosByPkCeratemedfuripsReling1;
	private Integer tipoCieDxRel2Ingreso;
	private Integer tipoCieDxRel2Egreso;
	private String primerApellidoMedico;
	private String segundoApellidoMedico;
	private String primerNombreMedico;
	private String segundoNombreMedico;
	private String nroDocumentoMedico;
	private String nroRegistroMedico;

	public CertAtenMedicaFurips() {
	}

	public CertAtenMedicaFurips(ReclamacionesAccEveFact reclamacionesAccEveFact) {
		this.reclamacionesAccEveFact = reclamacionesAccEveFact;
	}

	public CertAtenMedicaFurips(
			Diagnosticos diagnosticosByPkCeratemedfuripsDxing,
			TiposIdentificacion tiposIdentificacion, Medicos medicos,
			ReclamacionesAccEveFact reclamacionesAccEveFact,
			Diagnosticos diagnosticosByPkCeratemedfuripsRelegr2,
			Diagnosticos diagnosticosByPkCeratemedfuripsReling2,
			Diagnosticos diagnosticosByPkCeratemedfuripsRelegr1,
			Diagnosticos diagnosticosByPkCeratemedfuripsDxegr,
			Diagnosticos diagnosticosByPkCeratemedfuripsReling1,
			Integer tipoCieDxRel2Ingreso, Integer tipoCieDxRel2Egreso,
			String primerApellidoMedico, String segundoApellidoMedico,
			String primerNombreMedico, String segundoNombreMedico,
			String nroDocumentoMedico, String nroRegistroMedico) {
		this.diagnosticosByPkCeratemedfuripsDxing = diagnosticosByPkCeratemedfuripsDxing;
		this.tiposIdentificacion = tiposIdentificacion;
		this.medicos = medicos;
		this.reclamacionesAccEveFact = reclamacionesAccEveFact;
		this.diagnosticosByPkCeratemedfuripsRelegr2 = diagnosticosByPkCeratemedfuripsRelegr2;
		this.diagnosticosByPkCeratemedfuripsReling2 = diagnosticosByPkCeratemedfuripsReling2;
		this.diagnosticosByPkCeratemedfuripsRelegr1 = diagnosticosByPkCeratemedfuripsRelegr1;
		this.diagnosticosByPkCeratemedfuripsDxegr = diagnosticosByPkCeratemedfuripsDxegr;
		this.diagnosticosByPkCeratemedfuripsReling1 = diagnosticosByPkCeratemedfuripsReling1;
		this.tipoCieDxRel2Ingreso = tipoCieDxRel2Ingreso;
		this.tipoCieDxRel2Egreso = tipoCieDxRel2Egreso;
		this.primerApellidoMedico = primerApellidoMedico;
		this.segundoApellidoMedico = segundoApellidoMedico;
		this.primerNombreMedico = primerNombreMedico;
		this.segundoNombreMedico = segundoNombreMedico;
		this.nroDocumentoMedico = nroDocumentoMedico;
		this.nroRegistroMedico = nroRegistroMedico;
	}

	public long getCodigoReclamacion() {
		return this.codigoReclamacion;
	}

	public void setCodigoReclamacion(long codigoReclamacion) {
		this.codigoReclamacion = codigoReclamacion;
	}

	public Diagnosticos getDiagnosticosByPkCeratemedfuripsDxing() {
		return this.diagnosticosByPkCeratemedfuripsDxing;
	}

	public void setDiagnosticosByPkCeratemedfuripsDxing(
			Diagnosticos diagnosticosByPkCeratemedfuripsDxing) {
		this.diagnosticosByPkCeratemedfuripsDxing = diagnosticosByPkCeratemedfuripsDxing;
	}

	public TiposIdentificacion getTiposIdentificacion() {
		return this.tiposIdentificacion;
	}

	public void setTiposIdentificacion(TiposIdentificacion tiposIdentificacion) {
		this.tiposIdentificacion = tiposIdentificacion;
	}

	public Medicos getMedicos() {
		return this.medicos;
	}

	public void setMedicos(Medicos medicos) {
		this.medicos = medicos;
	}

	public ReclamacionesAccEveFact getReclamacionesAccEveFact() {
		return this.reclamacionesAccEveFact;
	}

	public void setReclamacionesAccEveFact(
			ReclamacionesAccEveFact reclamacionesAccEveFact) {
		this.reclamacionesAccEveFact = reclamacionesAccEveFact;
	}

	public Diagnosticos getDiagnosticosByPkCeratemedfuripsRelegr2() {
		return this.diagnosticosByPkCeratemedfuripsRelegr2;
	}

	public void setDiagnosticosByPkCeratemedfuripsRelegr2(
			Diagnosticos diagnosticosByPkCeratemedfuripsRelegr2) {
		this.diagnosticosByPkCeratemedfuripsRelegr2 = diagnosticosByPkCeratemedfuripsRelegr2;
	}

	public Diagnosticos getDiagnosticosByPkCeratemedfuripsReling2() {
		return this.diagnosticosByPkCeratemedfuripsReling2;
	}

	public void setDiagnosticosByPkCeratemedfuripsReling2(
			Diagnosticos diagnosticosByPkCeratemedfuripsReling2) {
		this.diagnosticosByPkCeratemedfuripsReling2 = diagnosticosByPkCeratemedfuripsReling2;
	}

	public Diagnosticos getDiagnosticosByPkCeratemedfuripsRelegr1() {
		return this.diagnosticosByPkCeratemedfuripsRelegr1;
	}

	public void setDiagnosticosByPkCeratemedfuripsRelegr1(
			Diagnosticos diagnosticosByPkCeratemedfuripsRelegr1) {
		this.diagnosticosByPkCeratemedfuripsRelegr1 = diagnosticosByPkCeratemedfuripsRelegr1;
	}

	public Diagnosticos getDiagnosticosByPkCeratemedfuripsDxegr() {
		return this.diagnosticosByPkCeratemedfuripsDxegr;
	}

	public void setDiagnosticosByPkCeratemedfuripsDxegr(
			Diagnosticos diagnosticosByPkCeratemedfuripsDxegr) {
		this.diagnosticosByPkCeratemedfuripsDxegr = diagnosticosByPkCeratemedfuripsDxegr;
	}

	public Diagnosticos getDiagnosticosByPkCeratemedfuripsReling1() {
		return this.diagnosticosByPkCeratemedfuripsReling1;
	}

	public void setDiagnosticosByPkCeratemedfuripsReling1(
			Diagnosticos diagnosticosByPkCeratemedfuripsReling1) {
		this.diagnosticosByPkCeratemedfuripsReling1 = diagnosticosByPkCeratemedfuripsReling1;
	}

	public Integer getTipoCieDxRel2Ingreso() {
		return this.tipoCieDxRel2Ingreso;
	}

	public void setTipoCieDxRel2Ingreso(Integer tipoCieDxRel2Ingreso) {
		this.tipoCieDxRel2Ingreso = tipoCieDxRel2Ingreso;
	}

	public Integer getTipoCieDxRel2Egreso() {
		return this.tipoCieDxRel2Egreso;
	}

	public void setTipoCieDxRel2Egreso(Integer tipoCieDxRel2Egreso) {
		this.tipoCieDxRel2Egreso = tipoCieDxRel2Egreso;
	}

	public String getPrimerApellidoMedico() {
		return this.primerApellidoMedico;
	}

	public void setPrimerApellidoMedico(String primerApellidoMedico) {
		this.primerApellidoMedico = primerApellidoMedico;
	}

	public String getSegundoApellidoMedico() {
		return this.segundoApellidoMedico;
	}

	public void setSegundoApellidoMedico(String segundoApellidoMedico) {
		this.segundoApellidoMedico = segundoApellidoMedico;
	}

	public String getPrimerNombreMedico() {
		return this.primerNombreMedico;
	}

	public void setPrimerNombreMedico(String primerNombreMedico) {
		this.primerNombreMedico = primerNombreMedico;
	}

	public String getSegundoNombreMedico() {
		return this.segundoNombreMedico;
	}

	public void setSegundoNombreMedico(String segundoNombreMedico) {
		this.segundoNombreMedico = segundoNombreMedico;
	}

	public String getNroDocumentoMedico() {
		return this.nroDocumentoMedico;
	}

	public void setNroDocumentoMedico(String nroDocumentoMedico) {
		this.nroDocumentoMedico = nroDocumentoMedico;
	}

	public String getNroRegistroMedico() {
		return this.nroRegistroMedico;
	}

	public void setNroRegistroMedico(String nroRegistroMedico) {
		this.nroRegistroMedico = nroRegistroMedico;
	}

}
