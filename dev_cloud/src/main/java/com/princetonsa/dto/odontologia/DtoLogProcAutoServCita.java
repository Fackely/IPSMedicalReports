package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import com.princetonsa.dto.cargos.DtoDetalleCargo;

import util.ConstantesBD;

public class DtoLogProcAutoServCita implements Serializable{

	
	
	/**
	 * 
	 */
	private BigDecimal	codigoPk;
	private BigDecimal logProcAutoCita;
	private BigDecimal	servicioCitaOdo;
	private int  estadoInicialHc ;
	private int estadoInicialFact;
	private int numeroSolicitud;
	private ArrayList<DtoDetalleCargo> listaCargos = new ArrayList<DtoDetalleCargo>();
	
	
	/**
	 * 
	 */
	void reset(){
		this.codigoPk = new BigDecimal(ConstantesBD.codigoNuncaValidoDouble);
		this.logProcAutoCita = new BigDecimal(ConstantesBD.codigoNuncaValidoDouble);
		this.servicioCitaOdo = new BigDecimal(ConstantesBD.codigoNuncaValidoDouble);
		this.estadoInicialHc =ConstantesBD.codigoNuncaValido;
		this.estadoInicialFact=ConstantesBD.codigoNuncaValido;
		this.numeroSolicitud = ConstantesBD.codigoNuncaValido;
		this.listaCargos = new ArrayList<DtoDetalleCargo>();
	}
	
	
	/**
	 * @return the numeroSolicitud
	 */
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}


	/**
	 * @param numeroSolicitud the numeroSolicitud to set
	 */
	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}


	/**
	 * 
	 */
	public 		DtoLogProcAutoServCita(){
		this.reset();
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
	 * @return the logProcAutoCita
	 */
	public BigDecimal getLogProcAutoCita() {
		return logProcAutoCita;
	}

	/**
	 * @param logProcAutoCita the logProcAutoCita to set
	 */
	public void setLogProcAutoCita(BigDecimal logProcAutoCita) {
		this.logProcAutoCita = logProcAutoCita;
	}

	/**
	 * @return the servicioCitaOdo
	 */
	public BigDecimal getServicioCitaOdo() {
		return servicioCitaOdo;
	}

	/**
	 * @param servicioCitaOdo the servicioCitaOdo to set
	 */
	public void setServicioCitaOdo(BigDecimal servicioCitaOdo) {
		this.servicioCitaOdo = servicioCitaOdo;
	}

	/**
	 * @return the estadoInicialHc
	 */
	public int getEstadoInicialHc() {
		return estadoInicialHc;
	}

	/**
	 * @param estadoInicialHc the estadoInicialHc to set
	 */
	public void setEstadoInicialHc(int estadoInicialHc) {
		this.estadoInicialHc = estadoInicialHc;
	}


	public void setEstadoInicialFact(int estadoInicialFact) {
		this.estadoInicialFact = estadoInicialFact;
	}


	public int getEstadoInicialFact() {
		return estadoInicialFact;
	}


	/**
	 * @return the listaCargos
	 */
	public ArrayList<DtoDetalleCargo> getListaCargos() {
		return listaCargos;
	}


	/**
	 * @param listaCargos the listaCargos to set
	 */
	public void setListaCargos(ArrayList<DtoDetalleCargo> listaCargos) {
		this.listaCargos = listaCargos;
	}


	
}

