package com.princetonsa.actionform.inventarios;

import java.util.ArrayList;

import org.apache.struts.action.ActionForm;

import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;


/**
 * @author Cristhian Murillo
 *
 * Clase que almacena y carga la informaci&oacute;n a la vista utilizada para la funcionalidad
 */
public class EntregaMedicamentosInsumosEntSubcontratadasForm extends ActionForm 
{
	
	private static final long serialVersionUID = 1L;
	
	/*--------------------------*/
	/* ATRIBUTOS				*/
	/*--------------------------*/
	
	/** * Variable para manejar la direcci&oacute;n del workflow  */
	private String estado;
	
	/** * Lista de entidades subcontratadas  */
	private ArrayList<DtoEntidadSubcontratada> listaEntidadesSubcontratadas;
	
	/** * Entidad seleccionada de la lista */
	private String entidadSubcontratadaSelect;
	
	/** * Lista de entidades subcontratadas  */
	private ArrayList<DtoAutorizacionEntSubcontratadasCapitacion> listaAutorizacionesEntSub;
	
	/** * Posicion de las autorizaciones */
	private int posArray;
	
	/** * Parametros para ordenar */
	private String patronOrdenar;
	
	/** * Parametros para ordenar */
	private String esDescendente;
	
	/** * Indica si se debe mostrar el detalle de la Autorización */
	private boolean mostrarDetalle;
	
	/** * Autorización seleccionada  */
	private DtoAutorizacionEntSubcontratadasCapitacion autorizacionSeleccionada;
	
	
	

	/*-------------------------------------------------------*/
	/* RESET's
	/*-------------------------------------------------------*/
	
	/**
	 * Reset de la forma
	 */
	public void reset()
	{
		this.listaEntidadesSubcontratadas 	= new ArrayList<DtoEntidadSubcontratada>();
		this.entidadSubcontratadaSelect 	= "";
		this.listaAutorizacionesEntSub		= new ArrayList<DtoAutorizacionEntSubcontratadasCapitacion>();
		this.patronOrdenar 					= "";
		this.esDescendente					= "";
		this.mostrarDetalle					= false;
		this.autorizacionSeleccionada		= new DtoAutorizacionEntSubcontratadasCapitacion();
	}
	
	
	/*-------------------------------------------------------*/
	/* METODOS SETs Y GETs
	/*-------------------------------------------------------*/
	
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public ArrayList<DtoEntidadSubcontratada> getListaEntidadesSubcontratadas() {
		return listaEntidadesSubcontratadas;
	}

	public void setListaEntidadesSubcontratadas(
			ArrayList<DtoEntidadSubcontratada> listaEntidadesSubcontratadas) {
		this.listaEntidadesSubcontratadas = listaEntidadesSubcontratadas;
	}

	public String getEntidadSubcontratadaSelect() {
		return entidadSubcontratadaSelect;
	}

	public void setEntidadSubcontratadaSelect(String entidadSubcontratadaSelect) {
		this.entidadSubcontratadaSelect = entidadSubcontratadaSelect;
	}

	public ArrayList<DtoAutorizacionEntSubcontratadasCapitacion> getListaAutorizacionesEntSub() {
		return listaAutorizacionesEntSub;
	}

	public void setListaAutorizacionesEntSub(
			ArrayList<DtoAutorizacionEntSubcontratadasCapitacion> listaAutorizacionesEntSub) {
		this.listaAutorizacionesEntSub = listaAutorizacionesEntSub;
	}
	
	public int getPosArray() {
		return posArray;
	}

	public void setPosArray(int posArray) {
		this.posArray = posArray;
	}

	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	public String getEsDescendente() {
		return esDescendente;
	}

	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}

	public boolean isMostrarDetalle() {
		return mostrarDetalle;
	}

	public void setMostrarDetalle(boolean mostrarDetalle) {
		this.mostrarDetalle = mostrarDetalle;
	}

	public DtoAutorizacionEntSubcontratadasCapitacion getAutorizacionSeleccionada() {
		return autorizacionSeleccionada;
	}

	public void setAutorizacionSeleccionada(
			DtoAutorizacionEntSubcontratadasCapitacion autorizacionSeleccionada) {
		this.autorizacionSeleccionada = autorizacionSeleccionada;
	}
	
}
