package com.mercury.dto.odontologia;

import java.io.Serializable;

import util.ConstantesBD;

public class DtoDiagCartaDental implements Serializable{
	
	private int codigoPk;
	
	private int codigoPkCartaDental;
	
	private int codigoPkEstSecDienteInst;
	
	private String nombreEstSecDiente;
	
	private int codigoPkSuperficieDental;
	
	private int codigoPkSuperficieDentalAnterior;
	
	private String nombreSuperficieDental;
	
	private String activo;
	
	public DtoDiagCartaDental(){
		
		this.clean();
	}
	
	public void clean()
	{
		this.codigoPk = ConstantesBD.codigoNuncaValido;
		this.codigoPkCartaDental = ConstantesBD.codigoNuncaValido;		
		this.codigoPkEstSecDienteInst = ConstantesBD.codigoNuncaValido;
		this.nombreEstSecDiente = "";
		this.codigoPkSuperficieDental = ConstantesBD.codigoNuncaValido;
		this.codigoPkSuperficieDentalAnterior = ConstantesBD.codigoNuncaValido;
		this.nombreSuperficieDental = "";
		this.activo = ConstantesBD.acronimoSi;
	}
    
	public int getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public int getCodigoPkCartaDental() {
		return codigoPkCartaDental;
	}

	public void setCodigoPkCartaDental(int codigoPkCartaDental) {
		this.codigoPkCartaDental = codigoPkCartaDental;
	}

	public int getCodigoPkEstSecDienteInst() {
		return codigoPkEstSecDienteInst;
	}

	public void setCodigoPkEstSecDienteInst(int codigoPkEstSecDienteInst) {
		this.codigoPkEstSecDienteInst = codigoPkEstSecDienteInst;
	}

	public String getNombreEstSecDiente() {
		return nombreEstSecDiente;
	}

	public void setNombreEstSecDiente(String nombreEstSecDiente) {
		this.nombreEstSecDiente = nombreEstSecDiente;
	}

	public int getCodigoPkSuperficieDental() {
		return codigoPkSuperficieDental;
	}

	public void setCodigoPkSuperficieDental(int codigoPkSuperficieDental) {
		this.codigoPkSuperficieDental = codigoPkSuperficieDental;
	}

	public String getNombreSuperficieDental() {
		return nombreSuperficieDental;
	}

	public void setNombreSuperficieDental(String nombreSuperficieDental) {
		this.nombreSuperficieDental = nombreSuperficieDental;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public int getCodigoPkSuperficieDentalAnterior() {
		return codigoPkSuperficieDentalAnterior;
	}

	public void setCodigoPkSuperficieDentalAnterior(
			int codigoPkSuperficieDentalAnterior) {
		this.codigoPkSuperficieDentalAnterior = codigoPkSuperficieDentalAnterior;
	}
}
