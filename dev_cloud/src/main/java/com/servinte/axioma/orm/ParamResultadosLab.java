package com.servinte.axioma.orm;

// Generated Nov 30, 2010 11:44:21 AM by Hibernate Tools 3.2.4.GA

import java.util.HashSet;
import java.util.Set;

/**
 * ParamResultadosLab generated by hbm2java
 */
public class ParamResultadosLab implements java.io.Serializable {

	private int codigo;
	private CentrosCosto centrosCosto;
	private String etiquetaCampo;
	private int orden;
	private Set resultadoLaboratorioOrdens = new HashSet(0);
	private Set resultadoLaboratorioRegenfs = new HashSet(0);

	public ParamResultadosLab() {
	}

	public ParamResultadosLab(int codigo, CentrosCosto centrosCosto,
			String etiquetaCampo, int orden) {
		this.codigo = codigo;
		this.centrosCosto = centrosCosto;
		this.etiquetaCampo = etiquetaCampo;
		this.orden = orden;
	}

	public ParamResultadosLab(int codigo, CentrosCosto centrosCosto,
			String etiquetaCampo, int orden, Set resultadoLaboratorioOrdens,
			Set resultadoLaboratorioRegenfs) {
		this.codigo = codigo;
		this.centrosCosto = centrosCosto;
		this.etiquetaCampo = etiquetaCampo;
		this.orden = orden;
		this.resultadoLaboratorioOrdens = resultadoLaboratorioOrdens;
		this.resultadoLaboratorioRegenfs = resultadoLaboratorioRegenfs;
	}

	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public CentrosCosto getCentrosCosto() {
		return this.centrosCosto;
	}

	public void setCentrosCosto(CentrosCosto centrosCosto) {
		this.centrosCosto = centrosCosto;
	}

	public String getEtiquetaCampo() {
		return this.etiquetaCampo;
	}

	public void setEtiquetaCampo(String etiquetaCampo) {
		this.etiquetaCampo = etiquetaCampo;
	}

	public int getOrden() {
		return this.orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	public Set getResultadoLaboratorioOrdens() {
		return this.resultadoLaboratorioOrdens;
	}

	public void setResultadoLaboratorioOrdens(Set resultadoLaboratorioOrdens) {
		this.resultadoLaboratorioOrdens = resultadoLaboratorioOrdens;
	}

	public Set getResultadoLaboratorioRegenfs() {
		return this.resultadoLaboratorioRegenfs;
	}

	public void setResultadoLaboratorioRegenfs(Set resultadoLaboratorioRegenfs) {
		this.resultadoLaboratorioRegenfs = resultadoLaboratorioRegenfs;
	}

}
