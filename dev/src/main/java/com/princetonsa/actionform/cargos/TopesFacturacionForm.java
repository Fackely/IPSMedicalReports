/*
 * @(#)TopesFacturacionForm.java
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

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;
import util.UtilidadValidacion;



/**
 * Form que contiene todos los datos específicos para generar 
 * los topes de facturación
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. Julio 12 de 2004
 * @author wrios 
 */

public class TopesFacturacionForm extends ValidatorForm
{
	/**
	 * Código del tope
	 */
	private int codigo;
	
	/**
	 * Acrónimo del tipo de régimen
	 */
	private String acronimoTipoRegimen;
	
	/**
	 * nombre del tipo de régimen
	 */
	private String nombreTipoRegimen;
	
	/**
	 * código de la clasificación socioeconómica
	 */
	private int codigoEstrato;
	
	/**
	 * Descripción de la clasificación socioeconómica
	 */
	private String descripcionEstrato;
	
	/**
	 * código del tipo de monto
	 */
	private int codigoTipoMonto;
	
	/**
	 * nombre del tipo de monto
	 */
	private String nombreTipoMonto;
	
	/**
	 * tope por evento
	 */
	private String topeEvento;
	
	/**
	 * tope anio de calendario
	 */
	private String  topeAnioCalendario;
	
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
	 *Variable para guardar la fecha de Vigencia Inicial 
	 */
	private String fechaVigenciaInicial;
	
	
	/**
	 *Resetea los valores
	 */
	public void reset()
	{
		this.codigo=0;
		this.acronimoTipoRegimen="";
		this.nombreTipoRegimen="";
		this.codigoEstrato=0;
		this.descripcionEstrato="";
		this.codigoTipoMonto=0;
		this.topeEvento="";
		this.topeAnioCalendario="";
		this.fechaVigenciaInicial="";
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
		    boolean topeEventoVacio=false;
		    boolean topeAnioCalendarioVacio=false;
		    
			if(this.topeEvento.equals(""))
			{
				topeEventoVacio=true;
			}
			if(this.topeAnioCalendario.equals(""))
			{
				topeAnioCalendarioVacio=true;
			}
			if(this.acronimoTipoRegimen.equals(""))
			{
				errores.add("Campo Tipo Régimen vacio", new ActionMessage("errors.required","El campo Tipo Régimen"));
			}
			if(this.codigoEstrato<=0)
			{
				errores.add("Campo Clasificación socioeconómica vacio", new ActionMessage("errors.required","El campo clasificación socioEconómica"));
			}
			if(this.codigoTipoMonto<=0)
			{
				errores.add("Campo tipo monto vacio", new ActionMessage("errors.required","El campo Tipo Monto"));
			}
			if(this.fechaVigenciaInicial.equals(""))
			{
				errores.add("Fecha de Vigencia Inicial vacía ", new ActionMessage("errors.required","La Fecha de Vigencia Inicial "));
			}
			else if(!UtilidadFecha.validarFecha(this.fechaVigenciaInicial))
			{
				errores.add("Fecha de Vigencia Inicial vacía ", new ActionMessage("prompt.generico","La Fecha de Vigencia Inicial debe tener el formato dd/mm/yyyy para guardar el registro."));
			}
			try
			{
			    if(!topeAnioCalendarioVacio)
			    {    
					if(Double.parseDouble(topeAnioCalendario)>=0)
					{
						if(UtilidadValidacion.esMayorNdecimales(this.topeAnioCalendario,2))
						{
							errores.add("NUMERO DECIMALES", new ActionMessage("errors.numDecimales","El campo Tope Año calendario","2"));
						}
						if(UtilidadValidacion.esMayorNdigitos(topeAnioCalendario,9))
						{
							errores.add("NUMERO DIGITOS", new ActionMessage("errors.numDigitos","El campo Tope Año calendario","9"));
						}
					}
					else
						errores.add("NEGATIVO", new ActionMessage("errors.MayorIgualQue","El campo Tope Año calendario","0"));
			    }	
			}
			catch (NumberFormatException e) 
			{
				errores.add("no es un numero",new ActionMessage("errors.float","El campo Tope Año calendario"));
			}
			try
			{
			    if(!topeEventoVacio)
			    {    
					if(Double.parseDouble(this.topeEvento)>=0)
					{
						if(UtilidadValidacion.esMayorNdecimales(this.topeEvento,2))
						{
							errores.add("NUMERO DECIMALES", new ActionMessage("errors.numDecimales","El campo Tope Evento","2"));
						}
						if(UtilidadValidacion.esMayorNdigitos(topeEvento,9))
						{
							errores.add("NUMERO DIGITOS", new ActionMessage("errors.numDigitos","El campo Tope Evento","9"));
						}
					}
					else
						errores.add("NEGATIVO", new ActionMessage("errors.MayorIgualQue","El campo Tope Evento","0"));
			    }	
			}
			catch (NumberFormatException e) 
			{
				errores.add("no es un numero",new ActionMessage("errors.float","El campo Tope Evento"));
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
		return errores;
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
	 * @return Returns the acronimoTipoRegimen.
	 */
	public String getAcronimoTipoRegimen() {
		return acronimoTipoRegimen;
	}
	/**
	 * @param acronimoTipoRegimen The acronimoTipoRegimen to set.
	 */
	public void setAcronimoTipoRegimen(String acronimoTipoRegimen) {
		this.acronimoTipoRegimen = acronimoTipoRegimen;
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
	 * @return Returns the codigoEstrato.
	 */
	public int getCodigoEstrato() {
		return codigoEstrato;
	}
	/**
	 * @param codigoEstrato The codigoEstrato to set.
	 */
	public void setCodigoEstrato(int codigoEstrato) {
		this.codigoEstrato = codigoEstrato;
	}
	
	/**
	 * @return Returns the codigoTipoMonto.
	 */
	public int getCodigoTipoMonto() {
		return codigoTipoMonto;
	}
	/**
	 * @param codigoTipoMonto The codigoTipoMonto to set.
	 */
	public void setCodigoTipoMonto(int codigoTipoMonto) {
		this.codigoTipoMonto = codigoTipoMonto;
	}
	/**
	 * @return Returns the descripcionEstrato.
	 */
	public String getDescripcionEstrato() {
		return descripcionEstrato;
	}
	/**
	 * @param descripcionEstrato The descripcionEstrato to set.
	 */
	public void setDescripcionEstrato(String descripcionEstrato) {
		this.descripcionEstrato = descripcionEstrato;
	}
	/**
	 * @return Returns the estado.topeAnioCalendario
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
	 * @return Returns the nombreTipoMonto.
	 */
	public String getNombreTipoMonto() {
		return nombreTipoMonto;
	}
	/**
	 * @param nombreTipoMonto The nombreTipoMonto to set.
	 */
	public void setNombreTipoMonto(String nombreTipoMonto) {
		this.nombreTipoMonto = nombreTipoMonto;
	}
	/**
	 * @return Returns the nombreTipoRegimen.
	 */
	public String getNombreTipoRegimen() {
		return nombreTipoRegimen;
	}
	/**
	 * @param nombreTipoRegimen The nombreTipoRegimen to set.
	 */
	public void setNombreTipoRegimen(String nombreTipoRegimen) {
		this.nombreTipoRegimen = nombreTipoRegimen;
	}
	/**
	 * @return Returns the topeAnioCalendario.
	 */
	public String getTopeAnioCalendario() {
		return topeAnioCalendario;
	}
	/**
	 * @param topeAnioCalendario The topeAnioCalendario to set.
	 */
	public void setTopeAnioCalendario(String topeAnioCalendario) {
		this.topeAnioCalendario = topeAnioCalendario;
	}
	/**
	 * @return Returns the topeEvento.
	 */
	public String getTopeEvento() {
		return topeEvento;
	}
	/**
	 * @param topeEvento The topeEvento to set.
	 */
	public void setTopeEvento(String topeEvento) {
		this.topeEvento = topeEvento;
	}

	public String getFechaVigenciaInicial() {
		return fechaVigenciaInicial;
	}

	public void setFechaVigenciaInicial(String fechaVigenciaInicial) {
		this.fechaVigenciaInicial = fechaVigenciaInicial;
	}
}
