package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * InfoInconVarIncorrecta generated by hbm2java
 */
public class InfoInconVarIncorrecta implements java.io.Serializable {

	private long codigoPk;
	private VariablesIncorrectas variablesIncorrectas;
	private InformeInconsistencias informeInconsistencias;
	private String valor;

	public InfoInconVarIncorrecta() {
	}

	public InfoInconVarIncorrecta(long codigoPk,
			VariablesIncorrectas variablesIncorrectas,
			InformeInconsistencias informeInconsistencias) {
		this.codigoPk = codigoPk;
		this.variablesIncorrectas = variablesIncorrectas;
		this.informeInconsistencias = informeInconsistencias;
	}

	public InfoInconVarIncorrecta(long codigoPk,
			VariablesIncorrectas variablesIncorrectas,
			InformeInconsistencias informeInconsistencias, String valor) {
		this.codigoPk = codigoPk;
		this.variablesIncorrectas = variablesIncorrectas;
		this.informeInconsistencias = informeInconsistencias;
		this.valor = valor;
	}

	public long getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public VariablesIncorrectas getVariablesIncorrectas() {
		return this.variablesIncorrectas;
	}

	public void setVariablesIncorrectas(
			VariablesIncorrectas variablesIncorrectas) {
		this.variablesIncorrectas = variablesIncorrectas;
	}

	public InformeInconsistencias getInformeInconsistencias() {
		return this.informeInconsistencias;
	}

	public void setInformeInconsistencias(
			InformeInconsistencias informeInconsistencias) {
		this.informeInconsistencias = informeInconsistencias;
	}

	public String getValor() {
		return this.valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}
