/*
 * Julio 26, 2010
 */
package com.princetonsa.actionform.tesoreria;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.princetonsa.dto.tesoreria.DtoBusTrasladoAbono;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.Ciudades;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.Paises;
import com.servinte.axioma.orm.RegionesCobertura;


/**
 * @author Cristhian Murillo
 *
 * Clase que almacena y carga la informaci&oacute;n a la vista utilizada para la funcionalidad
 */

public class ConsultaTrasladoAbonoPacienteForm extends ActionForm 
{
	
	private static final long serialVersionUID = 1L;
	
	/** * Variable para manejar la direcci&oacute;n del workflow  */
	private String estado;
	
	
	/** * Clase que contiene los parametros de busqueda para para traslado de abonos   */
	private DtoBusTrasladoAbono dtoBusTrasladoAbono;
	
	
	/** * Lista de paises usados como parametros de busqueda*/
	private ArrayList<Paises> listaPaises;
	
	
	/** * Lista de Ciudades usados como parametros de busqueda*/
	private ArrayList<Ciudades> listaCiudades;
	
	
	/** * Lista de Regiones usados como parametros de busqueda*/
	private ArrayList<RegionesCobertura> listaRegiones;
	
	
	/** * Lista de Instituciones usados como parametros de busqueda*/
	private ArrayList<Instituciones> listaInstituciones;
	
	
	/** * Lista de Centros de Atencion usados como parametros de busqueda*/
	private ArrayList<CentroAtencion> listaCentroAtencion;
	
	
	
	
	
	
	/*-------------------------------------------------------*/
	/* RESET's
	/*-------------------------------------------------------*/

	/**
	 * Reset de la forma
	 */
	public void reset()
	{
		this.dtoBusTrasladoAbono 	= new DtoBusTrasladoAbono();
		this.listaPaises			= new ArrayList<Paises>();
		this.listaCiudades			= new ArrayList<Ciudades>();
		this.listaRegiones			= new ArrayList<RegionesCobertura>();
		this.listaInstituciones		= new ArrayList<Instituciones>();
		this.listaCentroAtencion	= new ArrayList<CentroAtencion>();
	}
	
	
	/*-------------------------------------------------------*/
	/* VALIDACION
	/*-------------------------------------------------------*/
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
		if(estado.equals("???"))
		{
			/*
			if(UtilidadTexto.isEmpty(this.getAcronimoTipoIdentificacion()))
			{
				errores.add("error_traslado_abono_pac", new ActionMessage("errors.required", " El Tipo de Identificación"));
			}
			
			if(UtilidadTexto.isEmpty(this.getIdentificacionBuscar()))
			{
				errores.add("error_traslado_abono_pac", new ActionMessage("errors.required", " El Número de identificación "));
			}
			*/
		}
		
		return errores;
	}






	public String getEstado() {
		return estado;
	}






	public DtoBusTrasladoAbono getDtoBusTrasladoAbono() {
		return dtoBusTrasladoAbono;
	}






	public ArrayList<Paises> getListaPaises() {
		return listaPaises;
	}






	public ArrayList<Ciudades> getListaCiudades() {
		return listaCiudades;
	}






	public ArrayList<RegionesCobertura> getListaRegiones() {
		return listaRegiones;
	}






	public ArrayList<Instituciones> getListaInstituciones() {
		return listaInstituciones;
	}






	public void setEstado(String estado) {
		this.estado = estado;
	}






	public void setDtoBusTrasladoAbono(DtoBusTrasladoAbono dtoBusTrasladoAbono) {
		this.dtoBusTrasladoAbono = dtoBusTrasladoAbono;
	}






	public void setListaPaises(ArrayList<Paises> listaPaises) {
		this.listaPaises = listaPaises;
	}






	public void setListaCiudades(ArrayList<Ciudades> listaCiudades) {
		this.listaCiudades = listaCiudades;
	}






	public void setListaRegiones(ArrayList<RegionesCobertura> listaRegiones) {
		this.listaRegiones = listaRegiones;
	}






	public void setListaInstituciones(ArrayList<Instituciones> listaInstituciones) {
		this.listaInstituciones = listaInstituciones;
	}




	public ArrayList<CentroAtencion> getListaCentroAtencion() {
		return listaCentroAtencion;
	}




	public void setListaCentroAtencion(ArrayList<CentroAtencion> listaCentroAtencion) {
		this.listaCentroAtencion = listaCentroAtencion;
	}






}
