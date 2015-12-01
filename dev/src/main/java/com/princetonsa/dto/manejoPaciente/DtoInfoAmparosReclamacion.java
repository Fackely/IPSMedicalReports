/**
 * 
 */
package com.princetonsa.dto.manejoPaciente;

import java.util.ArrayList;

/**
 * @author axioma
 *
 */
public class DtoInfoAmparosReclamacion 
{

	/**
	 * 
	 */
	private DtoDiagnostico diagnosticoIngresoPrincipal;
	
	/**
	 * 
	 */
	private ArrayList<DtoDiagnostico> diagnosticosIngresoRelacionados;
	
	/**
	 * 
	 */
	private DtoDiagnostico diagnosticoEgresoPrincipal;
	
	/**
	 * 
	 */
	private ArrayList<DtoDiagnostico> diagnosticosEgresoRelacionados;
	
	/**
	 * 
	 */
	private String primerNombreMedicoTratante;
	
	/**
	 * 
	 */
	private String primerApellidoMedicoTratante;
	
	/**
	 * 
	 */
	private String segundoNombreMedicoTratante;
	
	/**
	 * 
	 */
	private String segundoApellidoMedicoTratante;
	
	/**
	 * 
	 */
	private String tipoIdMedicoTratante;
	
	/**
	 * 
	 */
	private String numeroIdMedicoTratante;
	
	
	/**
	 * 
	 */
	private String numeroRegistroMedico;
	
	/**
	 * 
	 */
	public DtoInfoAmparosReclamacion()
	{
		this.diagnosticoIngresoPrincipal=new DtoDiagnostico();
		this.diagnosticoEgresoPrincipal=new DtoDiagnostico();
		this.diagnosticosIngresoRelacionados=new ArrayList<DtoDiagnostico>();
		this.diagnosticosEgresoRelacionados=new ArrayList<DtoDiagnostico>();		
		this.primerNombreMedicoTratante="";
		this.segundoNombreMedicoTratante="";
		this.primerApellidoMedicoTratante="";
		this.segundoApellidoMedicoTratante="";
		this.tipoIdMedicoTratante="";
		this.numeroIdMedicoTratante="";
		this.numeroRegistroMedico="";
	}

	public DtoDiagnostico getDiagnosticoIngresoPrincipal() {
		return diagnosticoIngresoPrincipal;
	}

	public void setDiagnosticoIngresoPrincipal(
			DtoDiagnostico diagnosticoIngresoPrincipal) {
		this.diagnosticoIngresoPrincipal = diagnosticoIngresoPrincipal;
	}

	public ArrayList<DtoDiagnostico> getDiagnosticosIngresoRelacionados() {
		return diagnosticosIngresoRelacionados;
	}

	public void setDiagnosticosIngresoRelacionados(
			ArrayList<DtoDiagnostico> diagnosticosIngresoRelacionados) {
		this.diagnosticosIngresoRelacionados = diagnosticosIngresoRelacionados;
	}

	public DtoDiagnostico getDiagnosticoEgresoPrincipal() {
		return diagnosticoEgresoPrincipal;
	}

	public void setDiagnosticoEgresoPrincipal(
			DtoDiagnostico diagnosticoEgresoPrincipal) {
		this.diagnosticoEgresoPrincipal = diagnosticoEgresoPrincipal;
	}

	public ArrayList<DtoDiagnostico> getDiagnosticosEgresoRelacionados() {
		return diagnosticosEgresoRelacionados;
	}

	public void setDiagnosticosEgresoRelacionados(
			ArrayList<DtoDiagnostico> diagnosticosEgresoRelacionados) {
		this.diagnosticosEgresoRelacionados = diagnosticosEgresoRelacionados;
	}

	public String getPrimerNombreMedicoTratante() {
		return primerNombreMedicoTratante;
	}

	public void setPrimerNombreMedicoTratante(String primerNombreMedicoTratante) {
		this.primerNombreMedicoTratante = primerNombreMedicoTratante;
	}

	public String getPrimerApellidoMedicoTratante() {
		return primerApellidoMedicoTratante;
	}

	public void setPrimerApellidoMedicoTratante(String primerApellidoMedicoTratante) {
		this.primerApellidoMedicoTratante = primerApellidoMedicoTratante;
	}

	public String getSegundoNombreMedicoTratante() {
		return segundoNombreMedicoTratante;
	}

	public void setSegundoNombreMedicoTratante(String segundoNombreMedicoTratante) {
		this.segundoNombreMedicoTratante = segundoNombreMedicoTratante;
	}

	public String getSegundoApellidoMedicoTratante() {
		return segundoApellidoMedicoTratante;
	}

	public void setSegundoApellidoMedicoTratante(
			String segundoApellidoMedicoTratante) {
		this.segundoApellidoMedicoTratante = segundoApellidoMedicoTratante;
	}

	public String getTipoIdMedicoTratante() {
		return tipoIdMedicoTratante;
	}

	public void setTipoIdMedicoTratante(String tipoIdMedicoTratante) {
		this.tipoIdMedicoTratante = tipoIdMedicoTratante;
	}

	public String getNumeroIdMedicoTratante() {
		return numeroIdMedicoTratante;
	}

	public void setNumeroIdMedicoTratante(String numeroIdMedicoTratante) {
		this.numeroIdMedicoTratante = numeroIdMedicoTratante;
	}

	public String getNumeroRegistroMedico() {
		return numeroRegistroMedico;
	}

	public void setNumeroRegistroMedico(String numeroRegistroMedico) {
		this.numeroRegistroMedico = numeroRegistroMedico;
	}
}
