package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;

import org.apache.struts.action.ActionForm;

import util.ConstantesBD;

import com.princetonsa.dto.odontologia.DtoConvencionesOdontologicas;

/**
 * Form relacionado a las convenciones odontológicas
 * @author Jorge Andrés Ortiz
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ConvencionesOdontologicasForm extends ActionForm {

	private String estado;
	private String patronOrdenar;
	private String esDescendente;
	private String colorFondo;
	private String colorBorde;
	private String linkSiguiente;
	private int posConvencion;
	private ArrayList<DtoConvencionesOdontologicas> convencionesOdontologicas;
	private DtoConvencionesOdontologicas nuevaConvencion; 
	
	/**
	 * Constructor vacío, asigna todos los valores por defecto
	 */
	public ConvencionesOdontologicasForm()
	{
		this.reset();
	}
	
	/**
	 * Limpia todos los atributos de la forma
	 */
	public void reset()
	{
		this.estado=new String("");
		this.patronOrdenar=new String("");
		this.esDescendente=new String("");
		this.linkSiguiente= new String("");
		this.posConvencion=ConstantesBD.codigoNuncaValido;
		this.convencionesOdontologicas=new ArrayList<DtoConvencionesOdontologicas>();
		this.nuevaConvencion= new DtoConvencionesOdontologicas();
		this.colorFondo=new String("#FFFFFF");
		this.colorBorde=new String("#FFFFFF");
	}
	
	/**
	 * Limpia los campos para ingresar una nueva convención 
	 */
	public void resetNuevaConvencion()
	{
		this.nuevaConvencion= new DtoConvencionesOdontologicas();
		this.colorFondo=new String("#FFFFFF");
		this.colorBorde=new String("#FFFFFF");
	}

	/**
	 * Obtiene el valor del atributo estado
	 *
	 * @return  Retorna atributo estado
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * Establece el valor del atributo estado
	 *
	 * @param valor para el atributo estado
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	/**
	 * Obtiene el valor del atributo patronOrdenar
	 *
	 * @return  Retorna atributo patronOrdenar
	 */
	public String getPatronOrdenar()
	{
		return patronOrdenar;
	}

	/**
	 * Establece el valor del atributo patronOrdenar
	 *
	 * @param valor para el atributo patronOrdenar
	 */
	public void setPatronOrdenar(String patronOrdenar)
	{
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * Obtiene el valor del atributo esDescendente
	 *
	 * @return  Retorna atributo esDescendente
	 */
	public String getEsDescendente()
	{
		return esDescendente;
	}

	/**
	 * Establece el valor del atributo esDescendente
	 *
	 * @param valor para el atributo esDescendente
	 */
	public void setEsDescendente(String esDescendente)
	{
		this.esDescendente = esDescendente;
	}

	/**
	 * Obtiene el valor del atributo colorFondo
	 *
	 * @return  Retorna atributo colorFondo
	 */
	public String getColorFondo()
	{
		return colorFondo;
	}

	/**
	 * Establece el valor del atributo colorFondo
	 *
	 * @param valor para el atributo colorFondo
	 */
	public void setColorFondo(String colorFondo)
	{
		this.colorFondo = colorFondo;
	}
	
	/**
	 * Obtiene el valor del atributo colorBorde
	 *
	 * @return  Retorna atributo colorBorde
	 */
	public String getColorBorde()
	{
		return colorBorde;
	}

	/**
	 * Establece el valor del atributo colorBorde
	 *
	 * @param valor para el atributo colorBorde
	 */
	public void setColorBorde(String colorBorde)
	{
		this.colorBorde = colorBorde;
	}

	/**
	 * Obtiene el valor del atributo linkSiguiente
	 *
	 * @return  Retorna atributo linkSiguiente
	 */
	public String getLinkSiguiente()
	{
		return linkSiguiente;
	}

	/**
	 * Establece el valor del atributo linkSiguiente
	 *
	 * @param valor para el atributo linkSiguiente
	 */
	public void setLinkSiguiente(String linkSiguiente)
	{
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * Obtiene el valor del atributo posConvencion
	 *
	 * @return  Retorna atributo posConvencion
	 */
	public int getPosConvencion()
	{
		return posConvencion;
	}

	/**
	 * Establece el valor del atributo posConvencion
	 *
	 * @param valor para el atributo posConvencion
	 */
	public void setPosConvencion(int posConvencion)
	{
		this.posConvencion = posConvencion;
	}

	/**
	 * Obtiene el valor del atributo convencionesOdontologicas
	 *
	 * @return  Retorna atributo convencionesOdontologicas
	 */
	public ArrayList<DtoConvencionesOdontologicas> getConvencionesOdontologicas()
	{
		return convencionesOdontologicas;
	}

	/**
	 * Establece el valor del atributo convencionesOdontologicas
	 *
	 * @param valor para el atributo convencionesOdontologicas
	 */
	public void setConvencionesOdontologicas(
			ArrayList<DtoConvencionesOdontologicas> convencionesOdontologicas)
	{
		this.convencionesOdontologicas = convencionesOdontologicas;
	}

	/**
	 * Obtiene el valor del atributo nuevaConvencion
	 *
	 * @return  Retorna atributo nuevaConvencion
	 */
	public DtoConvencionesOdontologicas getNuevaConvencion()
	{
		return nuevaConvencion;
	}

	/**
	 * Establece el valor del atributo nuevaConvencion
	 *
	 * @param valor para el atributo nuevaConvencion
	 */
	public void setNuevaConvencion(DtoConvencionesOdontologicas nuevaConvencion)
	{
		this.nuevaConvencion = nuevaConvencion;
	}

	
	
}
