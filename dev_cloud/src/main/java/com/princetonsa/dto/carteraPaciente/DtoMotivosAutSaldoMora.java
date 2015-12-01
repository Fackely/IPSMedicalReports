package com.princetonsa.dto.carteraPaciente;

import java.io.Serializable;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ValoresPorDefecto;

public class DtoMotivosAutSaldoMora implements Serializable
{
	private double codigoPk;
	private String descripcion;
	private int institucion;
	
	public void reset()
	{
		this.codigoPk=ConstantesBD.codigoNuncaValidoDouble;
		this.descripcion="";
		this.institucion=ConstantesBD.codigoNuncaValido;
	}
	
	
	 
	public DtoMotivosAutSaldoMora()
	{
		this.reset();
	}

	public double getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(double codigoPk) {
		this.codigoPk = codigoPk;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}
	
	
}