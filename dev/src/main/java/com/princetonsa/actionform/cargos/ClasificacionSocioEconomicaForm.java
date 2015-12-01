/*
 * @(#)ClasificacionSocioEconomicaForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.actionform.cargos;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.Utilidades;

/**
 * Form que contiene todos los datos específicos para generar 
 * la clasificación socio económica
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. Julio 01, 2004
 * @author wrios 
 */
public class ClasificacionSocioEconomicaForm extends ValidatorForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6095659453162802888L;

	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ClasificacionSocioEconomicaForm.class);

	/**
	 * Código del estrato
	 */
	private int codigo;

	/** 
	 * Descripcion de la clasificación socioEconómica 
	 */
	private String descripcion;

	/**
	 * Campo para capturar el acronimo  del 
	 *  tipo de régimen previamente creado en el sistema
	 */
	private String acronimoTipoRegimen;
	
	/**
	 * Campo para capturar el nombre del 
	 *  tipo de régimen previamente creado en el sistema
	 */
	private String nombreTipoRegimen;
	
	/** 
	 * estado de la clasificación
	 */
	private boolean activa;

	/**
	 * Auxiliar del campo boolean activa, para poder mandar
	 * un nuevo valor diferente de true o false en la búsqueda
	 * avanzada
	 */
	private int activaAux;	
	
	/**
	 * Campo donde se restringe por qué criterios se va a buscar
	 */
	private String criteriosBusqueda[];

	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;

	/**
	 * Colección con los datos del listado, ya sea para consulta,
	 * como también para búsqueda avanzada (pager)
	 */
	private Collection col=null;
	
	/**
	 * Offset para el pager 
	 */
	private int offset=0;

	/**
	 * Contiene el log con info original para
	 * almacenar esta info en el log tipo Archivo
	 */
	private String logInfoOriginal;


	public void reset()
	{
		this.codigo=0;
		this.descripcion="";
		this.acronimoTipoRegimen="";
		this.nombreTipoRegimen="";
		this.activa=true;
		
		this.activaAux=0;
	}	

	/**
	 * resetea en vector de strings que
	 * contiene los criterios de búsqueda 
	 *
	 */
	public void resetCriteriosBusqueda()
	{
		try
		{
			for(int k=0 ; k<criteriosBusqueda.length ; k++)
				criteriosBusqueda[k]="";
		}catch(Exception e)
		{
			logger.warn(" error en el reset de busqueda "+e);
		}
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
		if(estado.equals("salir")||estado.equals("guardarModificacion"))
		{
		    if(descripcion.equals("") || !Utilidades.validarEspacios(descripcion))
			{
				errores.add("Campo Descripción vacio", new ActionMessage("errors.required","El campo Descripción"));				
			}
			if(acronimoTipoRegimen.equals(""))
			{
				errores.add("Campo Tipo Régimen vacio", new ActionMessage("errors.required","El campo Tipo Régimen"));
			}
			if(!errores.isEmpty())
			{
				if(estado.equals("salir"))
					this.setEstado("empezar");
									
				if(estado.equals("guardarModificacion"))
					this.setEstado("modificar");
			}		
		}
		else if(estado.equals("inserta"))
		{
				mapping.findForward("principal");
		}
		else if(estado.equals("modificar"))
		{
				errores=super.validate(mapping,request);
		}
		else if(estado.equals("listar") || estado.equals("listarModificar"))
		{
				errores=super.validate(mapping,request);
		}	
		else if(estado.equals("busquedaAvanzada"))
		{
				errores=super.validate(mapping,request);
		}	
		else if(estado.equals("resultadoBusquedaAvanzada"))
		{
				errores=super.validate(mapping,request);
		}		
		return errores;
	}
	
	/**
	 * Retorna el estado en que se encuentra el registro del tercero
	 * @return
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * Asigna el estado en que se encuentra el registro del tercero
	 * @param string
	 */
	public void setEstado(String string) {
		estado = string;
	}

	/**
	 * Returns the activo.
	 * @return boolean
	 */
	public boolean getActiva() {
		return activa;
	}

	/**
	 * Returns the codigo.
	 * @return int
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * Returns the descripcion.
	 * @return String
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Sets the activo.
	 * @param activo The activo to set
	 */
	public void setActiva(boolean activa) {
		this.activa = activa;
	}

	/**
	 * Sets the codigo.
	 * @param codigo The codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * Sets the descripcion.
	 * @param descripcion The descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * Retorna los criterios de búsqueda como strings
	 * @return
	 */
	public String[] getCriteriosBusqueda() {
		return criteriosBusqueda;
	}

	/**
	 * Asigna los criterios de búsqueda como strings
	 * @param strings
	 */
	public void setCriteriosBusqueda(String[] strings) {
		criteriosBusqueda = strings;
	}

	/**
	 * Retorna el Auxiliar del campo boolean activa, para poder mandar
	 * un nuevo valor diferente de true o false en la búsqueda
	 * avanzada
	 */
	public int getActivaAux() {
		return activaAux;
	}

	/**
	 * Asigna el Auxiliar del campo boolean activa, para poder mandar
	 * un nuevo valor diferente de true o false en la búsqueda
	 * avanzada
	 */
	public void setActivaAux(int i) {
		activaAux = i;
	}

	/**
	 * retorna el log con info original para
	 * almacenar esta info en el log tipo Archivo
	 * @return
	 */
	public String getLogInfoOriginal() {
		return logInfoOriginal;
	}

	/**
	 * Asigna el log con info original para
	 * almacenar esta info en el log tipo Archivo
	 * @param string
	 */
	public void setLogInfoOriginal(String string) {
		logInfoOriginal = string;
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
	
	public int getColSize()
	{
		if(col!=null)
			return col.size();
		else
			return 0;
	}
	
	/**
	 * Retorna Offset del pager
	 * @return
	 */
	public int getOffset()
	{
		return offset;
	}

	/**
	 * Asigna Offset del pager
	 * @param i
	 */
	public void setOffset(int i) 
	{
		offset = i;
	}

	/**
	 * Retorna el acrónimo tipo Régimen
	 * @return
	 */
	public String getAcronimoTipoRegimen() {
		return acronimoTipoRegimen;
	}

	/**
	 * Asigna el acrónimo tipo Régimen
	 * @param string
	 */
	public void setAcronimoTipoRegimen(String string) {
		acronimoTipoRegimen = string;
	}


	/**
	 * Retorna el nombre del tipo de régimen
	 * @return
	 */
	public String getNombreTipoRegimen() {
		return nombreTipoRegimen;
	}

	/**
	 * Asigna el  nombre del tipo de régimen
	 * @param string
	 */
	public void setNombreTipoRegimen(String string) {
		nombreTipoRegimen = string;
	}

}
