package com.princetonsa.actionform.interfaz;

/*
 * Abril 20, 2010
 */

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.CuentasXEspOdonto;


/**
 * @author Cristhian Murillo
 *
 * Clase que almacena y carga la informaci&oacute;n a la vista utilizada para la funcionalidad
 */
@SuppressWarnings("serial")
public class CuentasXEspOdontoForm extends ValidatorForm 
{
	
	/* ATRIBUTOS*/
	
	/**
	 * Variable para manejar la direcci&oacute;n del workflow 
	 */
	private String estado;
	
	
	/**
	 * Lista DTO 
	 */
	private ArrayList<CuentasXEspOdonto> listaDto;

	/**
	 * Paginador 
	 */
	private int posArray;
	
	/**
	 * Lista CentroAtencion
	 */
	private ArrayList<CentroAtencion> listaCentroAtencion;
	
	/**
	 * Variable para hacer el cast de la llave primaria de centro de Atencion
	 */
	private String strCentroAtencion;
	
	
	
	
	
	
	
	
	/* SETS Y GETS */
	
	/**
	 * @return the listaDto
	 */
	public ArrayList<CuentasXEspOdonto> getListaDto() {
		return listaDto;
	}

	/**
	 * @param listaDto the listaDto to set
	 */
	public void setListaDto(ArrayList<CuentasXEspOdonto> listaDto) {
		this.listaDto = listaDto;
	}

	/**
	 * @return the strCentroAtencion
	 */
	public String getStrCentroAtencion() {
		return strCentroAtencion;
	}
	
	/**
	 * @param strCentroAtencion the strCentroAtencion to set
	 */
	public void setStrCentroAtencion(String strCentroAtencion) {
		this.strCentroAtencion = strCentroAtencion;
	}
	
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
	 * @return the centroAtencion
	 */
	public ArrayList<CentroAtencion> getListaCentroAtencion() {
		return listaCentroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setListaCentroAtencion(ArrayList<CentroAtencion> listaCentroAtencion) {
		this.listaCentroAtencion = listaCentroAtencion;
	}

	
	

	/* METODOS */
	
	/**
	 * Reset de la forma
	 */
	public void reset()
	{
		this.listaDto 					= new ArrayList<CuentasXEspOdonto>();
		this.listaCentroAtencion		= new ArrayList<CentroAtencion>();
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
		return new ActionErrors();
	}
	
}



