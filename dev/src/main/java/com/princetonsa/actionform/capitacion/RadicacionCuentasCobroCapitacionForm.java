/*
 * @(#)RadicaionCuentasCobroCapitacionForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 *
 */
package com.princetonsa.actionform.capitacion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;

/**
 * Form que contiene todos los datos específicos para generar 
 * la radicacion de cuentas cobro capitacion
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. Julio 4, 2006
 * @author wrios 
 */
public class RadicacionCuentasCobroCapitacionForm extends ValidatorForm
{
	/**
	 * Colección con los datos del listado, búsqueda avanzada (pager)
	 */
	private Collection col;
	
	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * citerios de busqueda
	 */
	private HashMap criteriosBusquedaMap;
	
	/**
	 * fecha radicacion 
	 */
	private String fechaRadicacionFormatApp;
	
	/**
	 * numero radicacion
	 */
	private String numeroRadicacion;
	
	/**
	 * observaciones
	 */
	private String observaciones;
	
	/**
	 * numero Cuenta cobro
	 */
	private String numeroCuentaCobro;
	
	/**
	 * mensaje de insercion
	 */
	private String mensaje;
	
	/**
	 * Validate the properties InsercionExistosothat have been set from this HTTP request, and
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
		if(estado.equals("busquedaAvanzada"))
		{	
			errores=super.validate(mapping,request);
			
			//primero se evalua que exista por lo menos un criterio de busqueda
			if(!existeUnCriterioBusqueda())
			{
				errores.add("", new ActionMessage("errors.required","Al menos un criterio de búsqueda"));
			}
			else
			{
				if(!this.getCriteriosBusquedaMap("cuentaCobro").trim().equals(""))
				{
					try
					{
						Double.parseDouble(this.getCriteriosBusquedaMap("cuentaCobro"));
					}
					catch(NumberFormatException e)
					{
						errores.add("", new ActionMessage("errors.float", "Cuenta Cobro"));
					}
				}
				if(errores.isEmpty())
				{	
					// primero se valida el formato y que los dos esten parametrizados
					if(!this.getCriteriosBusquedaMap("fechaInicial").trim().equals(""))
					{
						//se valida el formato de la fechaInicial
						if(!UtilidadFecha.validarFecha(this.getCriteriosBusquedaMap("fechaInicial").trim()))
						{
							errores.add("Fecha inicial", new ActionMessage("errors.formatoFechaInvalido", " Inicial"));
						}
						if(this.getCriteriosBusquedaMap("fechaFinal").trim().equals(""))
						{
							errores.add("Campo Fecha Final vacio", new ActionMessage("errors.required","Si parametriza la fecha inicial entonces el campo Fecha Final"));
						}
					}
					if(!this.getCriteriosBusquedaMap("fechaFinal").trim().equals(""))
					{
						//se valida el formato de la fechaInicial
						if(!UtilidadFecha.validarFecha(this.getCriteriosBusquedaMap("fechaFinal").trim()))
						{
							errores.add("Fecha Final", new ActionMessage("errors.formatoFechaInvalido", " Final"));
						}
						if(this.getCriteriosBusquedaMap("fechaInicial").trim().equals(""))
						{
							errores.add("Campo Fecha Inicial vacio", new ActionMessage("errors.required","Si parametriza la fecha final entonces el campo Fecha Inicial"));
						}
						
					}
					//si no existen errores entonces continuar con el resto de validaciones
					if(errores.isEmpty())
					{
						if(!this.getCriteriosBusquedaMap("fechaInicial").trim().equals(""))
						{
							// la fecha inicial debe ser menor a la del sistema
							if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getCriteriosBusquedaMap("fechaInicial").trim(), UtilidadFecha.getFechaActual()))
							{
								errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual","Inicial "+this.getCriteriosBusquedaMap("fechaInicial"), "actual "+UtilidadFecha.getFechaActual()));
							}
							if(!this.getCriteriosBusquedaMap("fechaFinal").trim().equals(""))
							{
								//la fecha final debe ser mayor igual a la inicial
								if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getCriteriosBusquedaMap("fechaInicial").trim(), this.getCriteriosBusquedaMap("fechaFinal").trim()))
								{
									errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual","Inicial "+this.getCriteriosBusquedaMap("fechaInicial"), "final "+this.getCriteriosBusquedaMap("fechaFinal").trim()));
								}
								
								// la fecha final debe ser menor a la del sistema
								if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getCriteriosBusquedaMap("fechaFinal").trim(), UtilidadFecha.getFechaActual()))
								{
									errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual","Final "+this.getCriteriosBusquedaMap("fechaFinal"), "actual "+UtilidadFecha.getFechaActual()));
								}
							}	
						}	
					}
				}	
			}
			if(!errores.isEmpty())
			{
				this.setEstado("continuarMostrarErrores");
			}
		}
		return errores;
	}	
	
	/**
	 * verifica que exista un criterio de busqueda 
	 * ( keys= cuentaCobro, codigoConvenio, fechaInicial, fechaFinal),
	 * @return
	 */
	private boolean existeUnCriterioBusqueda()
	{
		if(this.getCriteriosBusquedaMap("cuentaCobro").trim().equals("")
			&& this.getCriteriosBusquedaMap("codigoConvenio").trim().equals("")
			&& this.getCriteriosBusquedaMap("fechaInicial").trim().equals("")
			&& this.getCriteriosBusquedaMap("fechaFinal").trim().equals(""))
		{
			return false;
		}
		return true;
	}
	
	
	/**
	 * reset
	 *
	 */
	public void reset()
	{
		this.col= new ArrayList();
		this.criteriosBusquedaMap= new HashMap();
		resetInsertar();
	}
	
	/**
	 * reset los atributos pa insertar
	 *
	 */
	public void resetInsertar()
	{
		this.numeroRadicacion="";
		this.fechaRadicacionFormatApp="";
		this.observaciones="";
		this.numeroCuentaCobro="";
		this.mensaje="";
	}
	
	/**
	 * Retorna Colección para mostrar datos en el pager
	 * @return
	 */
	public Collection getCol() {
		return col;
	}
	
	/**
	 * Asigna Colección para mostrar datos en el pager
	 * @param collection
	 */
	public void setCol(Collection collection) {
		col = collection;
	}
	
	/**
	 * size de la col
	 * @return
	 */
	public int getColSize()
	{
		if(col!=null)
			return col.size();
		else
			return 0;
	}

	/**
	 * @return Returns the criteriosBusquedaMap.
	 */
	public HashMap getCriteriosBusquedaMap() {
		return criteriosBusquedaMap;
	}

	/**
	 * @param criteriosBusquedaMap The criteriosBusquedaMap to set.
	 */
	public void setCriteriosBusquedaMap(HashMap criteriosBusquedaMap) {
		this.criteriosBusquedaMap = criteriosBusquedaMap;
	}

	/**
	 * @return Returns the criteriosBusquedaMap.
	 */
	public String getCriteriosBusquedaMap(String key) {
		return criteriosBusquedaMap.get(key).toString();
	}

	/**
	 * @return Returns the estado.
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return Returns the fechaRadicacionFormatApp.
	 */
	public String getFechaRadicacionFormatApp() {
		return fechaRadicacionFormatApp;
	}

	/**
	 * @param fechaRadicacionFormatApp The fechaRadicacionFormatApp to set.
	 */
	public void setFechaRadicacionFormatApp(String fechaRadicacionFormatApp) {
		this.fechaRadicacionFormatApp = fechaRadicacionFormatApp;
	}

	/**
	 * @return Returns the numeroCuentaCobro.
	 */
	public String getNumeroCuentaCobro() {
		return numeroCuentaCobro;
	}

	/**
	 * @param numeroCuentaCobro The numeroCuentaCobro to set.
	 */
	public void setNumeroCuentaCobro(String numeroCuentaCobro) {
		this.numeroCuentaCobro = numeroCuentaCobro;
	}

	/**
	 * @return Returns the numeroRadicacion.
	 */
	public String getNumeroRadicacion() {
		return numeroRadicacion;
	}

	/**
	 * @param numeroRadicacion The numeroRadicacion to set.
	 */
	public void setNumeroRadicacion(String numeroRadicacion) {
		this.numeroRadicacion = numeroRadicacion;
	}

	/**
	 * @return Returns the observaciones.
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @param observaciones The observaciones to set.
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @return Returns the mensaje.
	 */
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje The mensaje to set.
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	
	
}
