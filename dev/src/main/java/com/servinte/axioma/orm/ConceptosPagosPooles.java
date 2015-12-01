package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * ConceptosPagosPooles generated by hbm2java
 */
public class ConceptosPagosPooles implements java.io.Serializable {

	private String codigo;
	private Usuarios usuarios;
	private Instituciones instituciones;
	private String descripcion;
	private String tipoConcepto;
	private Date fechaModifica;
	private String horaModifica;
	private Set conceptPagPoolXConvs = new HashSet(0);

	public ConceptosPagosPooles() {
	}

	public ConceptosPagosPooles(String codigo, Usuarios usuarios,
			Instituciones instituciones, String descripcion,
			String tipoConcepto, Date fechaModifica, String horaModifica) {
		this.codigo = codigo;
		this.usuarios = usuarios;
		this.instituciones = instituciones;
		this.descripcion = descripcion;
		this.tipoConcepto = tipoConcepto;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
	}

	public ConceptosPagosPooles(String codigo, Usuarios usuarios,
			Instituciones instituciones, String descripcion,
			String tipoConcepto, Date fechaModifica, String horaModifica,
			Set conceptPagPoolXConvs) {
		this.codigo = codigo;
		this.usuarios = usuarios;
		this.instituciones = instituciones;
		this.descripcion = descripcion;
		this.tipoConcepto = tipoConcepto;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.conceptPagPoolXConvs = conceptPagPoolXConvs;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public Instituciones getInstituciones() {
		return this.instituciones;
	}

	public void setInstituciones(Instituciones instituciones) {
		this.instituciones = instituciones;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getTipoConcepto() {
		return this.tipoConcepto;
	}

	public void setTipoConcepto(String tipoConcepto) {
		this.tipoConcepto = tipoConcepto;
	}

	public Date getFechaModifica() {
		return this.fechaModifica;
	}

	public void setFechaModifica(Date fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	public String getHoraModifica() {
		return this.horaModifica;
	}

	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	public Set getConceptPagPoolXConvs() {
		return this.conceptPagPoolXConvs;
	}

	public void setConceptPagPoolXConvs(Set conceptPagPoolXConvs) {
		this.conceptPagPoolXConvs = conceptPagPoolXConvs;
	}

}
