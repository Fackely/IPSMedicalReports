/*
 * @(#)ActivPyPXFacturarForm
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.actionform.pyp;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

/**
 * Form que contiene todos los datos específicos para generar 
 * la parametrizacion curva de aterta 
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @author wrios 
 */
public class ActivPyPXFacturarForm extends ValidatorForm 
{
	/**
	 * codigo convenio
	 */
	private int codigoConvenio;
	
	/**
	 * Mapa 
	 */
	private HashMap mapa= new HashMap();
	
	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * Patron de ordenamiento por columnas
	 */
	private String patronOrdenar;
	
	/**
	 * String ultimo patron de ordenamiento
	 */
	private String ultimoPatron;
	
	/**
	 * codifo del paciente para cargarlo en sesion
	 */
	private String codigoPaciente;
	
	/**
	 * Resetea  los valores de  la forma
	 *
	 */
	public void reset()
	{
		this.codigoConvenio=-1;
		this.mapa=new HashMap();
		this.mapa.put("numRegistros", "0");
		this.patronOrdenar="";
	    this.ultimoPatron="";
	    this.codigoPaciente="";
	}
	
	/**
	 * Validate the properties that have been set from this HTTP request, and
	 * return an <code>ActionErrors</code> object that encapsulates any
	 * validation errors that have been found.  If no errors are found, return
	 * <code>null</code> or an <code>ActionErrors</code> object with no recorded
	 * error messages.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
	    ActionErrors errores= new ActionErrors();
	    return errores;
	}
	
	/** Set del mapa 
	 * @param key
	 * @param value
	 */
	public void setMapa(String key, Object value){
		mapa.put(key, value);
	}
	/**
	 * Get del mapa 
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getMapa(String key){
		return mapa.get(key);
	}

	/**
	 * @return Returns the codigoConvenio.
	 */
	public int getCodigoConvenio()
	{
		return codigoConvenio;
	}

	/**
	 * @param codigoConvenio The codigoConvenio to set.
	 */
	public void setCodigoConvenio(int codigoConvenio)
	{
		this.codigoConvenio = codigoConvenio;
	}

	/**
	 * @return Returns the estado.
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	/**
	 * @return Returns the mapa.
	 */
	public HashMap getMapa()
	{
		return mapa;
	}

	/**
	 * @param mapa The mapa to set.
	 */
	public void setMapa(HashMap mapa)
	{
		this.mapa = mapa;
	}

	/**
	 * @return Returns the patronOrdenar.
	 */
	public String getPatronOrdenar()
	{
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar The patronOrdenar to set.
	 */
	public void setPatronOrdenar(String patronOrdenar)
	{
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @return Returns the ultimoPatron.
	 */
	public String getUltimoPatron()
	{
		return ultimoPatron;
	}

	/**
	 * @param ultimoPatron The ultimoPatron to set.
	 */
	public void setUltimoPatron(String ultimoPatron)
	{
		this.ultimoPatron = ultimoPatron;
	}

	/**
	 * @return Returns the codigoPaciente.
	 */
	public String getCodigoPaciente()
	{
		return codigoPaciente;
	}

	/**
	 * @return Returns the codigoPaciente.
	 */
	public int getCodigoPacienteInt()
	{
		int toReturn=-1;
		try
		{
			toReturn=Integer.parseInt(codigoPaciente);
		}
		catch(Exception e)
		{
			toReturn=-1;
		}
		return toReturn;
	}
	
	/**
	 * @param codigoPaciente The codigoPaciente to set.
	 */
	public void setCodigoPaciente(String codigoPaciente)
	{
		this.codigoPaciente = codigoPaciente;
	}
	
}