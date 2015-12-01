package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;
import util.UtilidadFecha;

public class DtoValDiagnosticosOdo implements Serializable 
{
	private double valoracionOdo;
	
	private String acronimoDiagnostico;
	
	private int tipoCleDiagnostico;
	
	private String principal;
	
	private String fecha;
	
	private String hora;
	
	private String usuario;
	
	private String nombreDiagnostico;
	
	public void clean()
	{	
		this.valoracionOdo=ConstantesBD.codigoNuncaValidoDouble;
		this.acronimoDiagnostico="";
		this.tipoCleDiagnostico=ConstantesBD.codigoNuncaValido;
		this.principal="";
		this.fecha="";
		this.hora="";
		this.usuario="";
		this.nombreDiagnostico="";
	}
	
	public void DtoValDiagnosticosOdo()
	{
		clean();
	}

	public double getValoracionOdo() {
		return valoracionOdo;
	}

	public void setValoracionOdo(double valoracionOdo) {
		this.valoracionOdo = valoracionOdo;
	}

	public String getAcronimoDiagnostico() {
		return acronimoDiagnostico;
	}

	public void setAcronimoDiagnostico(String acronimoDiagnostico) {
		this.acronimoDiagnostico = acronimoDiagnostico;
	}

	public int getTipoCleDiagnostico() {
		return tipoCleDiagnostico;
	}

	public void setTipoCleDiagnostico(int tipoCleDiagnostico) {
		this.tipoCleDiagnostico = tipoCleDiagnostico;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getNombreDiagnostico() {
		return nombreDiagnostico;
	}

	public void setNombreDiagnostico(String nombreDiagnostico) {
		this.nombreDiagnostico = nombreDiagnostico;
	}
}