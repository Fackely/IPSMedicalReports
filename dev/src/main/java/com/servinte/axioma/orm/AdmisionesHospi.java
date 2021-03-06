package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * AdmisionesHospi generated by hbm2java
 */
public class AdmisionesHospi implements java.io.Serializable {

	private int codigo;
	private Medicos medicos;
	private Cuentas cuentas;
	private Camas1 camas1;
	private Usuarios usuarios;
	private OriAdmisionHospi oriAdmisionHospi;
	private EstadosAdmision estadosAdmision;
	private Diagnosticos diagnosticos;
	private Date fechaAdmision;
	private String horaAdmision;
	private Date fechaGrabacion;
	private String horaGrabacion;
	private int causaExterna;
	private String numeroAutorizacion;
	private Set hisCamaPacs = new HashSet(0);

	public AdmisionesHospi() {
	}

	public AdmisionesHospi(int codigo, Cuentas cuentas, Usuarios usuarios,
			EstadosAdmision estadosAdmision, Diagnosticos diagnosticos,
			Date fechaAdmision, String horaAdmision, Date fechaGrabacion,
			String horaGrabacion, int causaExterna) {
		this.codigo = codigo;
		this.cuentas = cuentas;
		this.usuarios = usuarios;
		this.estadosAdmision = estadosAdmision;
		this.diagnosticos = diagnosticos;
		this.fechaAdmision = fechaAdmision;
		this.horaAdmision = horaAdmision;
		this.fechaGrabacion = fechaGrabacion;
		this.horaGrabacion = horaGrabacion;
		this.causaExterna = causaExterna;
	}

	public AdmisionesHospi(int codigo, Medicos medicos, Cuentas cuentas,
			Camas1 camas1, Usuarios usuarios,
			OriAdmisionHospi oriAdmisionHospi, EstadosAdmision estadosAdmision,
			Diagnosticos diagnosticos, Date fechaAdmision, String horaAdmision,
			Date fechaGrabacion, String horaGrabacion, int causaExterna,
			String numeroAutorizacion, Set hisCamaPacs) {
		this.codigo = codigo;
		this.medicos = medicos;
		this.cuentas = cuentas;
		this.camas1 = camas1;
		this.usuarios = usuarios;
		this.oriAdmisionHospi = oriAdmisionHospi;
		this.estadosAdmision = estadosAdmision;
		this.diagnosticos = diagnosticos;
		this.fechaAdmision = fechaAdmision;
		this.horaAdmision = horaAdmision;
		this.fechaGrabacion = fechaGrabacion;
		this.horaGrabacion = horaGrabacion;
		this.causaExterna = causaExterna;
		this.numeroAutorizacion = numeroAutorizacion;
		this.hisCamaPacs = hisCamaPacs;
	}

	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public Medicos getMedicos() {
		return this.medicos;
	}

	public void setMedicos(Medicos medicos) {
		this.medicos = medicos;
	}

	public Cuentas getCuentas() {
		return this.cuentas;
	}

	public void setCuentas(Cuentas cuentas) {
		this.cuentas = cuentas;
	}

	public Camas1 getCamas1() {
		return this.camas1;
	}

	public void setCamas1(Camas1 camas1) {
		this.camas1 = camas1;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public OriAdmisionHospi getOriAdmisionHospi() {
		return this.oriAdmisionHospi;
	}

	public void setOriAdmisionHospi(OriAdmisionHospi oriAdmisionHospi) {
		this.oriAdmisionHospi = oriAdmisionHospi;
	}

	public EstadosAdmision getEstadosAdmision() {
		return this.estadosAdmision;
	}

	public void setEstadosAdmision(EstadosAdmision estadosAdmision) {
		this.estadosAdmision = estadosAdmision;
	}

	public Diagnosticos getDiagnosticos() {
		return this.diagnosticos;
	}

	public void setDiagnosticos(Diagnosticos diagnosticos) {
		this.diagnosticos = diagnosticos;
	}

	public Date getFechaAdmision() {
		return this.fechaAdmision;
	}

	public void setFechaAdmision(Date fechaAdmision) {
		this.fechaAdmision = fechaAdmision;
	}

	public String getHoraAdmision() {
		return this.horaAdmision;
	}

	public void setHoraAdmision(String horaAdmision) {
		this.horaAdmision = horaAdmision;
	}

	public Date getFechaGrabacion() {
		return this.fechaGrabacion;
	}

	public void setFechaGrabacion(Date fechaGrabacion) {
		this.fechaGrabacion = fechaGrabacion;
	}

	public String getHoraGrabacion() {
		return this.horaGrabacion;
	}

	public void setHoraGrabacion(String horaGrabacion) {
		this.horaGrabacion = horaGrabacion;
	}

	public int getCausaExterna() {
		return this.causaExterna;
	}

	public void setCausaExterna(int causaExterna) {
		this.causaExterna = causaExterna;
	}

	public String getNumeroAutorizacion() {
		return this.numeroAutorizacion;
	}

	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}

	public Set getHisCamaPacs() {
		return this.hisCamaPacs;
	}

	public void setHisCamaPacs(Set hisCamaPacs) {
		this.hisCamaPacs = hisCamaPacs;
	}

}
