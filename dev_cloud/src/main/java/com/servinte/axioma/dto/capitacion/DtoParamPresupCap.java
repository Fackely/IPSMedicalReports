package com.servinte.axioma.dto.capitacion;

import java.io.Serializable;
import java.sql.Date;

import com.servinte.axioma.orm.Contratos;

public class DtoParamPresupCap implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long codigoParametrizacionPresupuesto;
	
	private Contratos contrato;
	
	private String numeroContrato;
	
	private Date fechaParam;
	
	private String anioVigencia;
	
	
	public DtoParamPresupCap() {}

	public DtoParamPresupCap(long codigoParametrizacionPresupuesto,
			Contratos contrato, String numeroContrato, Date fechaParam,
			String anioVigencia) {
		super();
		this.codigoParametrizacionPresupuesto = codigoParametrizacionPresupuesto;
		this.contrato = contrato;
		this.numeroContrato = numeroContrato;
		this.fechaParam = fechaParam;
		this.anioVigencia = anioVigencia;
	}

	
	public long getCodigoParametrizacionPresupuesto() {
		return codigoParametrizacionPresupuesto;
	}

	public void setCodigoParametrizacionPresupuesto(
			long codigoParametrizacionPresupuesto) {
		this.codigoParametrizacionPresupuesto = codigoParametrizacionPresupuesto;
	}

	public String getNumeroContrato() {
		if (contrato != null) {
			numeroContrato = this.contrato.getNumeroContrato();
		} else {
			numeroContrato = "";
		}
		return numeroContrato;
	}

	public void setNumeroContrato(String numeroContrato) {
		this.numeroContrato = numeroContrato;
	}

	public Contratos getContrato() {
		return contrato;
	}

	public void setContrato(Contratos contrato) {
		this.contrato = contrato;
	}

	public Date getFechaParam() {
		return fechaParam;
	}

	public void setFechaParam(Date fechaParam) {
		this.fechaParam = fechaParam;
	}

	public String getAnioVigencia() {
		return anioVigencia;
	}

	public void setAnioVigencia(String anioVigencia) {
		this.anioVigencia = anioVigencia;
	}

	
}
