package com.mercury.dto.odontologia;

import java.io.Serializable;

import util.ConstantesBD;

public class DtoTratamientoCartaDental implements Serializable
{
	private int codigoPk;
	
	private int codigoPkCartaDental;
		
	private int codigoPkTipoTratOdoInst;
	
	private String nombreTratamiento;
	
	private boolean activo;
	
	private String tieneDetalle;
	
	public void clean()
	{
		this.codigoPk = ConstantesBD.codigoNuncaValido;
		this.activo = true;
		this.codigoPkCartaDental = ConstantesBD.codigoNuncaValido;		
		this.codigoPkTipoTratOdoInst = ConstantesBD.codigoNuncaValido;		
		this.nombreTratamiento = "";
		this.tieneDetalle = ConstantesBD.acronimoNo;
	}
	
	public void DtoTratamientoCartaDental()
	{
		this.clean();
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

	public int getCodigoPkTipoTratOdoInst() {
		return codigoPkTipoTratOdoInst;
	}

	public void setCodigoPkTipoTratOdoInst(int codigoPkTipoTratOdoInst) {
		this.codigoPkTipoTratOdoInst = codigoPkTipoTratOdoInst;
	}

	public String getNombreTratamiento() {
		return nombreTratamiento;
	}

	public void setNombreTratamiento(String nombreTratamiento) {
		this.nombreTratamiento = nombreTratamiento;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public String getTieneDetalle() {
		return tieneDetalle;
	}

	public void setTieneDetalle(String tieneDetalle) {
		this.tieneDetalle = tieneDetalle;
	}
}