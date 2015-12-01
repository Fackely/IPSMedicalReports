package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * ClasOdoMotivosNoConfir generated by hbm2java
 */
public class ClasOdoMotivosNoConfir implements java.io.Serializable {

	private int codigoPk;
	private MotivosCita motivosCita;
	private ClasOdoSecIndConf clasOdoSecIndConf;

	public ClasOdoMotivosNoConfir() {
	}

	public ClasOdoMotivosNoConfir(int codigoPk,
			ClasOdoSecIndConf clasOdoSecIndConf) {
		this.codigoPk = codigoPk;
		this.clasOdoSecIndConf = clasOdoSecIndConf;
	}

	public ClasOdoMotivosNoConfir(int codigoPk, MotivosCita motivosCita,
			ClasOdoSecIndConf clasOdoSecIndConf) {
		this.codigoPk = codigoPk;
		this.motivosCita = motivosCita;
		this.clasOdoSecIndConf = clasOdoSecIndConf;
	}

	public int getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public MotivosCita getMotivosCita() {
		return this.motivosCita;
	}

	public void setMotivosCita(MotivosCita motivosCita) {
		this.motivosCita = motivosCita;
	}

	public ClasOdoSecIndConf getClasOdoSecIndConf() {
		return this.clasOdoSecIndConf;
	}

	public void setClasOdoSecIndConf(ClasOdoSecIndConf clasOdoSecIndConf) {
		this.clasOdoSecIndConf = clasOdoSecIndConf;
	}

}
