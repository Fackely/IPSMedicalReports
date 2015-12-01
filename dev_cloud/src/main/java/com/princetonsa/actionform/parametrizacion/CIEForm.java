/*
 * @(#)CIEForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.actionform.parametrizacion;

import java.text.SimpleDateFormat;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;

/**
 * Form que contiene todos los datos específicos para generar 
 * la vigencia de diagnósticos
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * 
 * @version 1,0. Agosto 17, 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class CIEForm extends ValidatorForm
{
	/**
	 * Código CIE asignado por el sistema
	 */
	private int codigo;
		
	/**
	 * Código Real  del CIE para el user (Tipo CIE) 
	 */
	private String codigoReal;
	
	/**
	 * Fecha desde la cual estará vigente el CIE
	 */
	private String vigencia;
	
	/**
	 * Fecha temp
	 */
	private String vigenciaAntigua;
	
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
			errores=super.validate(mapping,request);
			if(this.codigoReal==null || this.codigoReal.equals(""))
			{	
				errores.add("Campo Tipo CIE no seleccionado", new ActionMessage("errors.required","El campo Tipo CIE"));
			}
			if(this.vigencia.equals("") || this.vigencia==null)
			{	
				errores.add("Campo Vigencia vacio", new ActionMessage("errors.required","El campo Vigencia"));
			}
			else
			{
				if(validacionFecha(this.vigencia))
				{
					errores.add("Vigencia", new ActionMessage("errors.formatoFechaInvalido", " Vigencia"));							
				}
				else if( validacionFechaSistema(this.vigencia, UtilidadFecha.getFechaActual()))
				{		
					errores.add("Vigencia", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia"," de Vigencia", " actual"));
				}
			}			
			if(!errores.isEmpty())
			{
				if(estado.equals("salir"))
					this.setEstado("empezar");
						
				if(estado.equals("guardarModificacion"))
					this.setEstado("modificar");
			}		
		}
		else if(estado.equals("modificar"))
		{
			errores=super.validate(mapping,request);
		}
		return errores;
	}

//	si retorna true es que es menor o igual que la fecha del sistema
	public boolean validacionFechaSistema(String fecha, String fecha1)
	{
		String[] codigoNombre = fecha1.split("/", 3);
		String[] codigoNombre1 = fecha.split("/", 3);
		
		if(Integer.parseInt(codigoNombre1[2]) < Integer.parseInt(codigoNombre[2]))
			return true;
		else if(Integer.parseInt(codigoNombre1[2]) == Integer.parseInt(codigoNombre[2]))
		{
			if(Integer.parseInt(codigoNombre1[1]) < Integer.parseInt(codigoNombre[1]))
			return true;
			else if(Integer.parseInt(codigoNombre1[1]) == Integer.parseInt(codigoNombre[1]))
			{		
				if(Integer.parseInt(codigoNombre1[0]) < Integer.parseInt(codigoNombre[0]))
					return true;
				else if(Integer.parseInt(codigoNombre1[0]) == Integer.parseInt(codigoNombre[0]))	
					return true;
			}
		}	
		return false;	 
	}

	private boolean validacionFecha(String fecha)
	{
		boolean tieneErroresFecha= false;
		final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
	
		try
		{
			dateFormatter.parse(fecha);
		}
		catch (java.text.ParseException e)
		{
			tieneErroresFecha=true;
		}
		catch(Exception e)
		{
			tieneErroresFecha=true;
		}
		fecha="";
		return tieneErroresFecha;
	}

	
	public void reset()
	{
		this.codigo = -1;
		this.codigoReal="";
		this.vigencia="";
		this.vigenciaAntigua="";
		this.logInfoOriginal="";
	}	
	
	/**
	 * @return Returns the codigo.
	 */
	public int getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo The codigo to set.
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	/**
	 * @return Returns the codigoReal.
	 */
	public String getCodigoReal() {
		return codigoReal;
	}
	/**
	 * @param codigoReal The codigoReal to set.
	 */
	public void setCodigoReal(String codigoReal) {
		this.codigoReal = codigoReal;
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
	 * @return Returns the logInfoOriginal.
	 */
	public String getLogInfoOriginal() {
		return logInfoOriginal;
	}
	/**
	 * @param logInfoOriginal The logInfoOriginal to set.
	 */
	public void setLogInfoOriginal(String logInfoOriginal) {
		this.logInfoOriginal = logInfoOriginal;
	}
	/**
	 * @return Returns the vigencia.
	 */
	public String getVigencia() {
		return vigencia;
	}
	/**
	 * @param vigencia The vigencia to set.
	 */
	public void setVigencia(String vigencia) {
		this.vigencia = vigencia;
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
	 * Retorna Colección para mostrar datos en el pager
	 * @return collection
	 */

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
	 * @return Returns the vigenciaAntigua.
	 */
	public String getVigenciaAntigua() {
		return vigenciaAntigua;
	}
	/**
	 * @param vigenciaAntigua The vigenciaAntigua to set.
	 */
	public void setVigenciaAntigua(String vigenciaAntigua) {
		this.vigenciaAntigua = vigenciaAntigua;
	}
}
