package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * LogProcAutoFact generated by hbm2java
 */
public class LogProcAutoFact implements java.io.Serializable {

	private long codigoPk;
	private LogProcAutoServCita logProcAutoServCita;
	private long detCargo;
	private int estadoInicialFact;

	public LogProcAutoFact() {
	}

	public LogProcAutoFact(long codigoPk,
			LogProcAutoServCita logProcAutoServCita, long detCargo,
			int estadoInicialFact) {
		this.codigoPk = codigoPk;
		this.logProcAutoServCita = logProcAutoServCita;
		this.detCargo = detCargo;
		this.estadoInicialFact = estadoInicialFact;
	}

	public long getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public LogProcAutoServCita getLogProcAutoServCita() {
		return this.logProcAutoServCita;
	}

	public void setLogProcAutoServCita(LogProcAutoServCita logProcAutoServCita) {
		this.logProcAutoServCita = logProcAutoServCita;
	}

	public long getDetCargo() {
		return this.detCargo;
	}

	public void setDetCargo(long detCargo) {
		this.detCargo = detCargo;
	}

	public int getEstadoInicialFact() {
		return this.estadoInicialFact;
	}

	public void setEstadoInicialFact(int estadoInicialFact) {
		this.estadoInicialFact = estadoInicialFact;
	}

}
