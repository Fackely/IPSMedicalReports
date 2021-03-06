package com.servinte.axioma.orm;

// Generated Feb 23, 2011 5:31:34 PM by Hibernate Tools 3.2.4.GA

/**
 * LogRipsEntSubRegValor generated by hbm2java
 */
public class LogRipsEntSubRegValor implements java.io.Serializable {

	private long codigoPk;
	private LogRipsEntidadesSubRegistr logRipsEntidadesSubRegistr;
	private String campoObligatorio;
	private String valor;

	public LogRipsEntSubRegValor() {
	}

	public LogRipsEntSubRegValor(long codigoPk,
			LogRipsEntidadesSubRegistr logRipsEntidadesSubRegistr,
			String campoObligatorio) {
		this.codigoPk = codigoPk;
		this.logRipsEntidadesSubRegistr = logRipsEntidadesSubRegistr;
		this.campoObligatorio = campoObligatorio;
	}

	public LogRipsEntSubRegValor(long codigoPk,
			LogRipsEntidadesSubRegistr logRipsEntidadesSubRegistr,
			String campoObligatorio, String valor) {
		this.codigoPk = codigoPk;
		this.logRipsEntidadesSubRegistr = logRipsEntidadesSubRegistr;
		this.campoObligatorio = campoObligatorio;
		this.valor = valor;
	}

	public long getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public LogRipsEntidadesSubRegistr getLogRipsEntidadesSubRegistr() {
		return this.logRipsEntidadesSubRegistr;
	}

	public void setLogRipsEntidadesSubRegistr(
			LogRipsEntidadesSubRegistr logRipsEntidadesSubRegistr) {
		this.logRipsEntidadesSubRegistr = logRipsEntidadesSubRegistr;
	}

	public String getCampoObligatorio() {
		return this.campoObligatorio;
	}

	public void setCampoObligatorio(String campoObligatorio) {
		this.campoObligatorio = campoObligatorio;
	}

	public String getValor() {
		return this.valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}
