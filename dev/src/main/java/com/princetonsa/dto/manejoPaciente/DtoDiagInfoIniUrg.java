package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;

import util.ConstantesBD;

import com.princetonsa.mundo.atencion.Diagnostico;

public class DtoDiagInfoIniUrg implements Serializable{
	private String codigoPK;
	private String info_atencion_ini_urg;
	private Diagnostico diagnostico;
	
	/**
	 * Constructor
	 */
	public DtoDiagInfoIniUrg()
	{
		this.clean();
	}
	
	public void clean()
	{
		this.codigoPK = "";
		this.info_atencion_ini_urg = "";
		this.diagnostico = new Diagnostico("",ConstantesBD.codigoNuncaValido,false);
	}

	/**
	 * @return the codigoPK
	 */
	public String getCodigoPK() {
		return codigoPK;
	}

	/**
	 * @param codigoPK the codigoPK to set
	 */
	public void setCodigoPK(String codigoPK) {
		this.codigoPK = codigoPK;
	}

	/**
	 * @return the autorizacion
	 */
	public String getInfIniUrg() {
		return info_atencion_ini_urg;
	}

	/**
	 * @param autorizacion the autorizacion to set
	 */
	public void setInfIniUrg(String info_atencion_ini_urg) {
		this.info_atencion_ini_urg = info_atencion_ini_urg;
	}

	/**
	 * @return the diagnostico
	 */
	public Diagnostico getDiagnostico() {
		return diagnostico;
	}

	/**
	 * @param diagnostico the diagnostico to set
	 */
	public void setDiagnostico(Diagnostico diagnostico) {
		this.diagnostico = diagnostico;
	}
	
}

	
