/**
 * 
 */
package com.servinte.axioma.dto.manejoPaciente;

import util.ConstantesBD;

/**
 * @author axioma
 *
 */
public class DtoFiltroBusquedaAvanzadaEgresoPorFacturar 
{
	/**
	 * 
	 */
	private String consecutivoAdmision;
	
	/**
	 * 
	 */
	private String fechaAdmisionIncial;
	
	/**
	 * 
	 */
	private String fechaAdmisionFinal;
	
	/**
	 * 
	 */
	private String fechaEgresoInicial;
	
	/**
	 * 
	 */
	private String fechaEgresoFinal;
	
	/**
	 * 
	 */
	private int viaIngreso;
	
	/**
	 * 
	 */
	private String tipoPaciente;
	
	/**
	 * 
	 */
	private int centroCosto;
	
	/**
	 * 
	 */
	private String tipoId;
	
	/**
	 * 
	 */
	private String numeroId;
	
	/**
	 * 
	 */
	private int codigoMedico;

	/**
	 * 
	 */
	public DtoFiltroBusquedaAvanzadaEgresoPorFacturar() 
	{
		this.consecutivoAdmision = "";
		this.fechaAdmisionIncial = "";
		this.fechaAdmisionFinal = "";
		this.fechaEgresoInicial = "";
		this.fechaEgresoFinal = "";
		this.viaIngreso = ConstantesBD.codigoNuncaValido;
		this.tipoPaciente = "";
		this.centroCosto = ConstantesBD.codigoNuncaValido;
		this.tipoId = "";
		this.numeroId = "";
		this.codigoMedico = ConstantesBD.codigoNuncaValido;
	}

	public String getConsecutivoAdmision() {
		return consecutivoAdmision;
	}

	public void setConsecutivoAdmision(String consecutivoAdmision) {
		this.consecutivoAdmision = consecutivoAdmision;
	}

	public String getFechaAdmisionIncial() {
		return fechaAdmisionIncial;
	}

	public void setFechaAdmisionIncial(String fechaAdmisionIncial) {
		this.fechaAdmisionIncial = fechaAdmisionIncial;
	}

	public String getFechaAdmisionFinal() {
		return fechaAdmisionFinal;
	}

	public void setFechaAdmisionFinal(String fechaAdmisionFinal) {
		this.fechaAdmisionFinal = fechaAdmisionFinal;
	}

	public String getFechaEgresoInicial() {
		return fechaEgresoInicial;
	}

	public void setFechaEgresoInicial(String fechaEgresoInicial) {
		this.fechaEgresoInicial = fechaEgresoInicial;
	}

	public String getFechaEgresoFinal() {
		return fechaEgresoFinal;
	}

	public void setFechaEgresoFinal(String fechaEgresoFinal) {
		this.fechaEgresoFinal = fechaEgresoFinal;
	}

	public int getViaIngreso() {
		return viaIngreso;
	}

	public void setViaIngreso(int viaIngreso) {
		this.viaIngreso = viaIngreso;
	}

	public String getTipoPaciente() {
		return tipoPaciente;
	}

	public void setTipoPaciente(String tipoPaciente) {
		this.tipoPaciente = tipoPaciente;
	}

	public int getCentroCosto() {
		return centroCosto;
	}

	public void setCentroCosto(int centroCosto) {
		this.centroCosto = centroCosto;
	}

	public String getTipoId() {
		return tipoId;
	}

	public void setTipoId(String tipoId) {
		this.tipoId = tipoId;
	}

	public String getNumeroId() {
		return numeroId;
	}

	public void setNumeroId(String numeroId) {
		this.numeroId = numeroId;
	}

	public int getCodigoMedico() {
		return codigoMedico;
	}

	public void setCodigoMedico(int codigoMedico) {
		this.codigoMedico = codigoMedico;
	}
	
	
	
	

}
