/*
 * Marzo 17, 2010
 */
package com.princetonsa.actionform.historiaClinica;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import util.ConstantesBD;
import util.UtilidadTexto;
import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.servinte.axioma.orm.OtrosDiagnosticos;


/**
 * @author Cristhian Murillo
 *
 * Clase que almacena y carga la informaci&oacute;n   la vista utilizada para la funcionalidad
 */
@SuppressWarnings("serial")
public class OtrosDiagnosticosForm extends ValidatorForm 
{
	
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	@SuppressWarnings("unused")
	transient private Logger logger = Logger.getLogger(OtrosDiagnosticosForm.class);
	
	/**
	 * Variable para manejar la direcci&oacute;n del workflow 
	 */
	private String estado;
	
	/**
	 * DTO 
	 */
	private OtrosDiagnosticos dto = new OtrosDiagnosticos();
	
	/**
	 * Lista DTO 
	 */
	private ArrayList<OtrosDiagnosticos> listaDto;

	/**
	 * Lista de los tipos de diagnosticos asignables al DTO 
	 */
	private ArrayList<DtoIntegridadDominio> listaTiposDiagnostico;
	
	/**
	 *  Lista que se debe utilizar para cargar los DTOs que pueden ser eliminables 
	 */
	private ArrayList<OtrosDiagnosticos> listaDtoEliminables;

	/**
	 * Indica si se debe mostrar el formulario nuevo/modificar 
	 */
	private boolean mostrarFormularioIngreso;

	/**
	 * Parametros para ordenar 
	 */
	private String patronOrdenar;
	
	/**
	 * Parametros para ordenar 
	 */
	private String esDescendente;
	
	/**
	 * Paginador 
	 */
	private int posArray;
	
		
	
	
	
	/**
	 * @return the posArray
	 */
	public int getPosArray() {
		return posArray;
	}




	/**
	 * @param posArray the posArray to set
	 */
	public void setPosArray(int posArray) {
		this.posArray = posArray;
	}




	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}




	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}




	/**
	 * @return the dto
	 */
	public OtrosDiagnosticos getDto() {
		return dto;
	}




	/**
	 * @param dto the dto to set
	 */
	public void setDto(OtrosDiagnosticos dto) {
		this.dto = dto;
	}




	/**
	 * @return the listaDto
	 */
	public ArrayList<OtrosDiagnosticos> getListaDto() {
		return listaDto;
	}




	/**
	 * @param listaDto the listaDto to set
	 */
	public void setListaDto(ArrayList<OtrosDiagnosticos> listaDto) {
		this.listaDto = listaDto;
	}




	/**
	 * @return the listaTiposDiagnostico
	 */
	public ArrayList<DtoIntegridadDominio> getListaTiposDiagnostico() {
		return listaTiposDiagnostico;
	}




	/**
	 * @param listaTiposDiagnostico the listaTiposDiagnostico to set
	 */
	public void setListaTiposDiagnostico(ArrayList<DtoIntegridadDominio> listaTiposDiagnostico) {
		this.listaTiposDiagnostico = listaTiposDiagnostico;
	}




	/**
	 * @return the listaDtoEliminables
	 */
	public ArrayList<OtrosDiagnosticos> getListaDtoEliminables() {
		return listaDtoEliminables;
	}




	/**
	 * @param listaDtoEliminables the listaDtoEliminables to set
	 */
	public void setListaDtoEliminables(
			ArrayList<OtrosDiagnosticos> listaDtoEliminables) {
		this.listaDtoEliminables = listaDtoEliminables;
	}




	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}




	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}




	/**
	 * @return the esDescendente
	 */
	public String getEsDescendente() {
		return esDescendente;
	}




	/**
	 * @param esDescendente the esDescendente to set
	 */
	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}


	
	
	
	
	


	/**
	 * Reset de la forma
	 */
	public void reset()
	{
		this.dto 					= new OtrosDiagnosticos();
		this.dto.setActivo(ConstantesBD.acronimoSi.charAt(0));
		this.listaTiposDiagnostico 	= new ArrayList<DtoIntegridadDominio>();
		this.listaDto 				= new ArrayList<OtrosDiagnosticos>();
		this.listaDtoEliminables 	= new ArrayList<OtrosDiagnosticos>();
		this.patronOrdenar = "";
		this.esDescendente = "";
		this.mostrarFormularioIngreso	= false;
	
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
		
		if(  (estado.equals("guardar")) || (estado.equals("guardarmodificar"))  )
		{
			if(UtilidadTexto.isEmpty(dto.getCodigo()))
			{
				errores.add("error codigo", new ActionMessage("errors.required", "El c&oacute;digo"));
			}
			
			if(UtilidadTexto.isEmpty(dto.getDescripcion()))
			{
				errores.add("error descripcion", new ActionMessage("errors.required", "La descripci&oacute;n"));
			}
			
			if(UtilidadTexto.isEmpty(dto.getTipo())){
				errores.add("error tipo", new ActionMessage("errors.required", "El tipo de diagn&oacute;stico"));
			}
		}
		
		return errores;
	}
	
	
	

	/**
	 * @return the mostrarFormularioIngreso
	 */
	public boolean isMostrarFormularioIngreso() {
		return mostrarFormularioIngreso;
	}

	/**
	 * @param mostrarFormularioIngreso the mostrarFormularioIngreso to set
	 */
	public void setMostrarFormularioIngreso(boolean mostrarFormularioIngreso) {
		this.mostrarFormularioIngreso = mostrarFormularioIngreso;
	}

	
}



