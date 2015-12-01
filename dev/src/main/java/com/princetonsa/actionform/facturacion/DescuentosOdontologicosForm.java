package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.InfoDatosString;
import util.UtilidadFecha;
import util.UtilidadTexto;

/**
 * 
 * @author Juan Diego Garcia Luna
 *
 */

public class DescuentosOdontologicosForm  extends ValidatorForm{
	
	/**
	 * 
	 */
	private String estado;

	public void reset() {
		
		
	}
	
	/**
	 * 
	 * @return
	 */
	public String getEstado()
	{
		return estado;
	}
	
	/**
	 * 
	 * @param estado
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}

}
