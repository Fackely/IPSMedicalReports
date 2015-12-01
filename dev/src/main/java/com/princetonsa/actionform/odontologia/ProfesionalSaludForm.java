/**
 * @autor Jorge Armando Agudelo Quintero
 */
package com.princetonsa.actionform.odontologia;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.UtilidadTexto;

/**
 * @author axioma
 *
 */
public class ProfesionalSaludForm extends ActionForm {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Atributo con el estado utilizado para controlar
	 * el flujo
	 */
	private String estado;
	
	/**
	 * M&eacute;todo donde se centralizan las validaciones
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping,	HttpServletRequest request) {
		/*
		 * Almacenamiento de los errores encontrados
		 */
		ActionErrors errores = new ActionErrors();

		if (UtilidadTexto.isEmpty(getEstado())) {
			errores.add("estado invalido", new ActionMessage(
					"errors.estadoInvalido"));
			return errores;

		}
	
		return errores;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

}
