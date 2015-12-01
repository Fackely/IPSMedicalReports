/*
 * Abr 29, 2008
 */
package com.princetonsa.dto.historiaClinica;

import java.io.Serializable;

import com.princetonsa.mundo.UsuarioBasico;

/**
 * Data Transfer Object: Observaciones de la valoracion
 * @author Sebastián Gómez R.
 *
 */
public class DtoValoracionObservaciones implements Serializable
{
	/**
	 * Consecutivo BD del registro observacion
	 */
	private String consecutivo;
	/**
	 * Campo que identifica el tipo de observacion que puede Ser:
	 * Se usa ConstantesIntegridadDominio
	 	acronimoMotivoConsulta = "MOTCO";
		acronimoPlanDiagnosticoTerapeutico = "PLDIA";
		acronimoComentariosGenerales = "COMGE";
		acronimoPronostico = "PRONO";
		acronimoEnfermedadActual = "ENFAC";
		acronimoComentariosAdicioinalesHC = "COMAD";
		acronimoConceptoConsulta = "CONCO"
	 * 
	 */
	private String tipo;
	private String label;
	private String valor;
	private String fecha;
	private String hora;
	private UsuarioBasico profesional;
	
	

	 /**
	 *  Alberto Ovalle
	 * mt 5749
	 */
	
	 /**
	 * atributo de planDiagnostico
	 */
	private String planDiagnostico;
	/**
	* atributo de comentariosGenerales
	*/
	private String comentariosGenerales;
	/**
	* atributo de expliqueDosDeberesDerechos
	*/
	private String expliqueDosDeberesDerechos;
		
	
	public void clean()
	{
		this.consecutivo = "";
		this.tipo = "";
		this.label = "";
		this.valor = "";
		this.fecha = "";
		this.hora = "";
		this.profesional = new UsuarioBasico();
	}
	
	/**
	 * Constructor
	 *
	 */
	public DtoValoracionObservaciones()
	{
		this.clean();
	}

	/**
	 * @return the consecutivo
	 */
	public String getConsecutivo() {
		return consecutivo;
	}

	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	/**
	 * @return the fecha
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return the hora
	 */
	public String getHora() {
		return hora;
	}

	/**
	 * @param hora the hora to set
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}

	/**
	 * @return the profesional
	 */
	public UsuarioBasico getProfesional() {
		return profesional;
	}

	/**
	 * @param profesional the profesional to set
	 */
	public void setProfesional(UsuarioBasico profesional) {
		this.profesional = profesional;
	}

	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the valor
	 */
	public String getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	
	/**
	 * 
	 * @return planDiagnostico
	 */
	
	public String getPlanDiagnostico() {
		return planDiagnostico;
	}

	/**
	 * 
	 * @param planDiagnostico
	 */
	public void setPlanDiagnostico(String planDiagnostico) {
		this.planDiagnostico = planDiagnostico;
	}

	/**
	 * 
	 * @return comentariosGenerales
	 */
	public String getComentariosGenerales() {
		return comentariosGenerales;
	}

	/**
	 * 
	 * @param comentariosGenerales
	 */
	public void setComentariosGenerales(String comentariosGenerales) {
		this.comentariosGenerales = comentariosGenerales;
	}

	/**
	 * 
	 * @return expliqueDosDeberesDerechos
	 */
	public String getExpliqueDosDeberesDerechos() {
		return expliqueDosDeberesDerechos;
	}

	/**
	 * 
	 * @param expliqueDosDeberesDerechos
	 */
	public void setExpliqueDosDeberesDerechos(String expliqueDosDeberesDerechos) {
		this.expliqueDosDeberesDerechos = expliqueDosDeberesDerechos;
	}
	
	
	
}
