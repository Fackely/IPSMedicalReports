/*
 * @(#)ConsultaGruposEtareosForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.actionform.capitacion;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;


/**
 * Forma para manejo presentación de la funcionalidad 
 * Consultar Grupos Etareos
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 25 /May/ 2006
 */
public class ConsultaGruposEtareosForm extends ValidatorForm
{

	/**
	 * Estado en el que se encuentra el proceso.
	 */
	private  String estado = "";
	
	/**
	 * almacena los datos de la consulta
	 */
	private HashMap mapaGruposEtareos;
	
	/**
 	 * Offset para el pager 
 	 */
 	private int offset=0;
 	
    /**
 	 * Este campo contiene el pageUrl para controlar el pager,
 	 *  y conservar los valores del hashMap mediante un submit de
 	 * JavaScript. (Integra pager -Valor Captura)
 	 */
     private String linkSiguiente;
     
 	/**
	 * Patron de ordenamiento por columnas
	 */
	private String patronOrdenar;
	
	/**
	 * String ultimo patron de ordenamiento
	 */
	private String ultimoPatron;
	
	/**
	 * Int para el codigo del convenio
	 */
	private int codigoConvenio;
	
	/**
	 * String de la Fecha inicial
	 */
	private String fechaInicial;
	
	/**
	 * String con la fecha final
	 */
	private String fechaFinal;
	
	
	/**
 	 * Reset generla de la Forma
 	 */
	public void reset ()
	{
		this.mapaGruposEtareos = new HashMap ();
		this.estado = "";
		this.linkSiguiente = "";
	 	this.patronOrdenar = "";
	 	this.ultimoPatron = "";
	 	this.codigoConvenio = -1;
	 	this.fechaInicial = "";
	 	this.fechaFinal = "";
	}
	
	/**
	 * Reset único para los mapas
	 */
	public void resetMapa()
	{
		this.mapaGruposEtareos = new HashMap ();
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
		this.codigoConvenio=codigoConvenio;
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
		this.estado=estado;
	}

	/**
	 * @return Returns the fechaFinal.
	 */
	public String getFechaFinal()
	{
		return fechaFinal;
	}

	/**
	 * @param fechaFinal The fechaFinal to set.
	 */
	public void setFechaFinal(String fechaFinal)
	{
		this.fechaFinal=fechaFinal;
	}

	/**
	 * @return Returns the fechaInicial.
	 */
	public String getFechaInicial()
	{
		return fechaInicial;
	}

	/**
	 * @param fechaInicial The fechaInicial to set.
	 */
	public void setFechaInicial(String fechaInicial)
	{
		this.fechaInicial=fechaInicial;
	}

	/**
	 * @return Returns the linkSiguiente.
	 */
	public String getLinkSiguiente()
	{
		return linkSiguiente;
	}

	/**
	 * @param linkSiguiente The linkSiguiente to set.
	 */
	public void setLinkSiguiente(String linkSiguiente)
	{
		this.linkSiguiente=linkSiguiente;
	}

	/**
	 * @return Returns the offset.
	 */
	public int getOffset()
	{
		return offset;
	}

	/**
	 * @param offset The offset to set.
	 */
	public void setOffset(int offset)
	{
		this.offset=offset;
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
		this.patronOrdenar=patronOrdenar;
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
		this.ultimoPatron=ultimoPatron;
	}
	
	/**
	 * @return Returns the mapaGruposEtareos
	 */
	public HashMap getMapaGruposEtareos()
	{
		return mapaGruposEtareos;
	}
	
	/**
	 * @param mapaGruposEtareos The mapaGruposEtareos to set.
	 */
	public void setMapaGruposEtareos(HashMap mapaGruposEtareos)
	{
		this.mapaGruposEtareos = mapaGruposEtareos;
	}
	
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaGruposEtareos(String key) 
	{
		return mapaGruposEtareos.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaGruposEtareos(String key, Object value) 
	{
		mapaGruposEtareos.put(key, value);
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
		
		if(estado.equals("buscar"))
		{
			if(this.getCodigoConvenio()== -1 )
			{
				errores.add("errors.required", new ActionMessage("errors.required", "El Convenio"));
			}
			if(this.getFechaInicial().trim().equals(""))
			{
				errores.add("errors.required", new ActionMessage("errors.required", "La Fecha Inicial"));
			}
			if(this.getFechaFinal().trim().equals(""))
			{
				errores.add("errors.required", new ActionMessage("errors.required", "La Fecha Final"));
			}
			if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaFinal())).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.getFechaInicial()))<0)
			{
				errores.add("Fecha Final menor a Fecha Incial", new ActionMessage("errors.fechaAnteriorIgualActual","Final", " Inicial"));				
			}
			if(!UtilidadFecha.validarFecha(this.getFechaInicial()))
			{
				errores.add("fechaInicial", new ActionMessage("errors.formatoFechaInvalido",this.getFechaInicial()));
			}
			if(!UtilidadFecha.validarFecha(this.getFechaFinal()))
			{
				errores.add("fechaFinal", new ActionMessage("errors.formatoFechaInvalido",this.getFechaFinal()));
			}
		}
		return errores;
	}
	
}
	
	
	