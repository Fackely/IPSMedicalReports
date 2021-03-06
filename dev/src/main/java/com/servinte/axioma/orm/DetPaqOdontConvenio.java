package com.servinte.axioma.orm;

// Generated Jul 8, 2010 9:47:11 AM by Hibernate Tools 3.2.4.GA

import java.util.HashSet;
import java.util.Set;

/**
 * DetPaqOdontConvenio generated by hbm2java
 */
public class DetPaqOdontConvenio implements java.io.Serializable {

	private int codigoPk;
	private PaqOdontConvenio paqOdontConvenio;
	private EsquemasTarifarios esquemasTarifarios;
	private PaquetesOdontologicos paquetesOdontologicos;
	private String fechaInicial;
	private String fechaFinal;
	private String activo;
	private Set presupuestoPaqueteses = new HashSet(0);
	private Set detCargoses = new HashSet(0);

	public DetPaqOdontConvenio() {
	}

	public DetPaqOdontConvenio(int codigoPk, PaqOdontConvenio paqOdontConvenio,
			EsquemasTarifarios esquemasTarifarios,
			PaquetesOdontologicos paquetesOdontologicos, String fechaInicial,
			String fechaFinal) {
		this.codigoPk = codigoPk;
		this.paqOdontConvenio = paqOdontConvenio;
		this.esquemasTarifarios = esquemasTarifarios;
		this.paquetesOdontologicos = paquetesOdontologicos;
		this.fechaInicial = fechaInicial;
		this.fechaFinal = fechaFinal;
	}

	public DetPaqOdontConvenio(int codigoPk, PaqOdontConvenio paqOdontConvenio,
			EsquemasTarifarios esquemasTarifarios,
			PaquetesOdontologicos paquetesOdontologicos, String fechaInicial,
			String fechaFinal, String activo, Set presupuestoPaqueteses,
			Set detCargoses) {
		this.codigoPk = codigoPk;
		this.paqOdontConvenio = paqOdontConvenio;
		this.esquemasTarifarios = esquemasTarifarios;
		this.paquetesOdontologicos = paquetesOdontologicos;
		this.fechaInicial = fechaInicial;
		this.fechaFinal = fechaFinal;
		this.activo = activo;
		this.presupuestoPaqueteses = presupuestoPaqueteses;
		this.detCargoses = detCargoses;
	}

	public int getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public PaqOdontConvenio getPaqOdontConvenio() {
		return this.paqOdontConvenio;
	}

	public void setPaqOdontConvenio(PaqOdontConvenio paqOdontConvenio) {
		this.paqOdontConvenio = paqOdontConvenio;
	}

	public EsquemasTarifarios getEsquemasTarifarios() {
		return this.esquemasTarifarios;
	}

	public void setEsquemasTarifarios(EsquemasTarifarios esquemasTarifarios) {
		this.esquemasTarifarios = esquemasTarifarios;
	}

	public PaquetesOdontologicos getPaquetesOdontologicos() {
		return this.paquetesOdontologicos;
	}

	public void setPaquetesOdontologicos(
			PaquetesOdontologicos paquetesOdontologicos) {
		this.paquetesOdontologicos = paquetesOdontologicos;
	}

	public String getFechaInicial() {
		return this.fechaInicial;
	}

	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	public String getFechaFinal() {
		return this.fechaFinal;
	}

	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public String getActivo() {
		return this.activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public Set getPresupuestoPaqueteses() {
		return this.presupuestoPaqueteses;
	}

	public void setPresupuestoPaqueteses(Set presupuestoPaqueteses) {
		this.presupuestoPaqueteses = presupuestoPaqueteses;
	}

	public Set getDetCargoses() {
		return this.detCargoses;
	}

	public void setDetCargoses(Set detCargoses) {
		this.detCargoses = detCargoses;
	}

}
