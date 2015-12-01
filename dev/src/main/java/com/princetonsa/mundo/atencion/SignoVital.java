/*
 * @(#)SignoVital.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo.atencion;

import java.util.ArrayList;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.InfoDatos;
import util.Utilidades;

/**
 * Esta clase encapsula los atributos de un signo vital.
 *
 * @version 1.0, May 15, 2003
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
 */

public class SignoVital extends InfoDatos {

	/**
	 * Unidad de medida en la que se toma este signo vital en particular.
	 */
	private String unidadMedida;

	/**
	 * Valor correspondiente a este signo vital.
	 */
	private String valorSignoVital;
	
	private Boolean estadoNormal;
	
	/**
	 * Campo para saber si el signo vital se debe mostrar
	 */
	private boolean visible;
	
	/**
	 * Campo para saber si es requerido
	 */
	private boolean requerido;

	/**
	 * Crea un nuevo objeto <code>SignoVital</code>.
	 */
	public SignoVital() {
		this.unidadMedida = "";
		this.valorSignoVital = "";
		this.estadoNormal = null;
		this.visible = true;
		this.requerido = false;
	}

	/**
	 * @return the estadoNormal
	 */
	public Boolean getEstadoNormal() {
		return estadoNormal;
	}

	/**
	 * @param estadoNormal the estadoNormal to set
	 */
	public void setEstadoNormal(Boolean estadoNormal) {
		this.estadoNormal = estadoNormal;
	}

	/**
	 * Retorna la unidad de medida de este signo vital.
	 * @return la unidad de medida de este signo vital
	 */
	public String getUnidadMedida() {
		return unidadMedida;
	}

	/**
	 * Retorna el valor de este signo vital.
	 * @return el valor de este signo vital
	 */
	public String getValorSignoVital() {
		return valorSignoVital;
	}

	/**
	 * Establece la unidad de medida de este signo vital.
	 * @param unidadMedida la unidad de medida a establecer
	 */
	public void setUnidadMedida(String unidadMedida) {
	    if (unidadMedida!=null)
	    {
			this.unidadMedida = unidadMedida;
	    }
	}

	/**
	 * Establece el valor de este signo vital.
	 * @param valorSignoVital el valor de este signo vital a establecer
	 */
	public void setValorSignoVital(String valorSignoVital) {
	    if (valorSignoVital!=null)
	    {
			this.valorSignoVital = valorSignoVital;
	    }
	}

	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @param visible the visible to set
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * @return the requerido
	 */
	public boolean isRequerido() {
		return requerido;
	}

	/**
	 * @param requerido the requerido to set
	 */
	public void setRequerido(boolean requerido) {
		this.requerido = requerido;
	}
	
	
	/**
	 * Método para realizar la validación de los signos vitales
	 * @param errores
	 * @param signosVitales
	 * @return
	 */
	public static ActionErrors validate(ActionErrors errores,ArrayList<SignoVital> signosVitales)
	{
		for(SignoVital signoVital:signosVitales)
		{
			if(!signoVital.getValorSignoVital().equals("")&&Utilidades.convertirADouble(signoVital.getValorSignoVital())==ConstantesBD.codigoNuncaValido)
				errores.add("", new ActionMessage("errors.float","El signo vital "+signoVital.getNombre()));
			
			if(signoVital.getValorSignoVital().equals("")&&signoVital.isRequerido())
				errores.add("", new ActionMessage("errors.required","El signo vital "+signoVital.getNombre()));
		}
			
		
		return errores;
	}
	
	/**
	 * Método para verificar si se ingresó información de los signos vitales
	 * @param signosVitales
	 * @return
	 */
	public static boolean ingresoInformacion(ArrayList<SignoVital> signosVitales)
	{
		boolean ingreso = false;
		
		for(SignoVital signo:signosVitales)
			if(!signo.getValorSignoVital().equals(""))
				ingreso = true;
		
		return ingreso;
	}
}