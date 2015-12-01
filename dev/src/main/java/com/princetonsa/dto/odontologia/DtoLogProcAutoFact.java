package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

import util.ConstantesBD;

public class DtoLogProcAutoFact implements Serializable{

	
	
	private BigDecimal codigoPk;
	private BigDecimal logProcAutoServCita ;
 	private BigDecimal detCargo; 
	private int estadoInicialFact;


	/**
	 * 
	 */
	public	DtoLogProcAutoFact(){
		this.reset();
	}
	
	/**
	 * 
	 */
	public void reset(){
		this.codigoPk= new BigDecimal(0);
		this.logProcAutoServCita = new BigDecimal(0);
	 	this.detCargo = new BigDecimal(0); 
		this.estadoInicialFact=ConstantesBD.codigoNuncaValido;
	}

	/**
	 * @return the codigoPk
	 */
	public BigDecimal getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(BigDecimal codigoPk) {
		this.codigoPk = codigoPk;
	}

	/**
	 * @return the logProcAutoServCita
	 */
	public BigDecimal getLogProcAutoServCita() {
		return logProcAutoServCita;
	}

	/**
	 * @param logProcAutoServCita the logProcAutoServCita to set
	 */
	public void setLogProcAutoServCita(BigDecimal logProcAutoServCita) {
		this.logProcAutoServCita = logProcAutoServCita;
	}

	/**
	 * @return the detCargo
	 */
	public BigDecimal getDetCargo() {
		return detCargo;
	}

	/**
	 * @param detCargo the detCargo to set
	 */
	public void setDetCargo(BigDecimal detCargo) {
		this.detCargo = detCargo;
	}

	/**
	 * @return the estadoInicialFact
	 */
	public int getEstadoInicialFact() {
		return estadoInicialFact;
	}

	/**
	 * @param estadoInicialFact the estadoInicialFact to set
	 */
	public void setEstadoInicialFact(int estadoInicialFact) {
		this.estadoInicialFact = estadoInicialFact;
	}
	
	
	
	
									
	

}
