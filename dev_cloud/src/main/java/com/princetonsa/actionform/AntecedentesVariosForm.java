/*
 * Created on Apr 25, 2003
 *
 */
package com.princetonsa.actionform;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;


/**
 * Form que maneja el formulario de Antecedentes varios
 * @author &lt;a href="mailto:dramirez@princetonsa.com"&gt;dramirez&lt;/a&gt;
 */
public class AntecedentesVariosForm extends ValidatorForm {

	/** *  */
	private static final long serialVersionUID = 1L;
	
	/**
	 * C&oacute;digo del Tipo de antecedente
	 */
	private String tipoAntecedente = null;
	/**
	 * Descripci&oacute;n del tipo de antecedente
	 */
	private String descripcionAntecedente = null;
	/**
	 * Estado en el que se encuentra el formulario
	 */
	private String estado = null;
	
	private String ocultarCabezotes;
	
	
	/**
	 *atributo que permite re direccionar 
	 */
	private Boolean redireccionar;
	
	/**
	 * Constructor vacio 
	 */
	public AntecedentesVariosForm() {
		this.redireccionar=false;
	}

	/**
	 * Permite asignar el c&oacute;digo del tipo de antecedente
	 * @param tipoAntecedente
	 */
	public void setTipoAntecedente(String tipoAntecedente) {
		this.tipoAntecedente = tipoAntecedente;
	}

	/**
	 * Permite obtener el c&oacute;digo del tipo de antecedente
	 * @return El c&oacute;digo del tipo de antecedente
	 */
	public String getTipoAntecedente() {
		return tipoAntecedente;
	}

	/**
	 * Permite asignar la descripci&oacute;n del tipo de antecedente
	 * @param descripcionAntencedente Descripci&oacute;n del tipo de antecedente
	 */
	public void setDescripcionAntecedente(String descripcionAntencedente) {
		this.descripcionAntecedente = descripcionAntencedente;
	}

	/**
	 * Permite obtener la descripci&oacute;n del antecedente
	 * @return Descripci&oacute;n del antecedente
	 */
	public String getDescripcionAntecedente() {
		return descripcionAntecedente;
	}

	/**
	 * Permite modificar el estado del formulario
	 * @param estado Nuevo estado del formulario
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * Permite obtener el estado actual de la forma
	 * @return Estado actual de la forma
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * Inicializa los elementos de la forma
	 */
	public void reset(){
		this.descripcionAntecedente = "";
		this.tipoAntecedente ="";
	}
	
	/**
	 * @return Returns the ocultarCabezotes.
	 */
	public String getOcultarCabezotes() {
		return ocultarCabezotes;
	}
	/**
	 * @param ocultarCabezotes The ocultarCabezotes to set.
	 */
	public void setOcultarCabezotes(String ocultarCabezotes) {
		this.ocultarCabezotes = ocultarCabezotes;
	}
	
	
	
	
	
	
	/**
	 * @return the redireccionar
	 */
	public Boolean getRedireccionar() {
		return redireccionar;
	}

	/**
	 * @param redireccionar the redireccionar to set
	 */
	public void setRedireccionar(Boolean redireccionar) {
		this.redireccionar = redireccionar;
	}

	/**
	 * Metodo de validaci&oacute;n de la forma 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		// Perform validator framework validations
		ActionErrors errors = super.validate(mapping, request);
		
		if((this.getEstado().equals("inicio") || this.getEstado().equals("resumen")|| this.getEstado().equals("regresarResumenAntecedentes")
				|| this.getEstado().equals("voverAntecedentesPrincipal")
				|| this.getEstado().equals("registro")) && request.getSession().getAttribute("pacienteActivo")!=null){
			errors = null;
		}
		return errors;
	}
}