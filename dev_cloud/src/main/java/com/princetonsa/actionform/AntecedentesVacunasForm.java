package com.princetonsa.actionform;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * AntecedentesVacunasForm
 */
@SuppressWarnings("rawtypes")
public class AntecedentesVacunasForm extends ActionForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(AntecedentesVacunasForm.class);
	

	/**
	 * Estado para el manejo del flujo de la funcionalidad
	 */
	private String estado;

	/**
	 * Mapa para el manejo de los tipos de inmunizaciones vacunas
	 */
	private HashMap mapaTiposInmunizacion;
	
	/**
	 * Mapa para postular y mostrar la información de las dosis de las vacunas
	 */
	private HashMap mapaDatosVacunas;
	
	/**
	 * Mapa para guardar la información ingresada en antecedentes vacunas
	 */
	private HashMap mapaAntVacunas;
	
	/**
	 * Campo para las observaciones generales
	 */
	private String observacionesGrales;
	
	/**
	 * Campo para las nuevas observaciones generales
	 */
	private String observacionesGralesNueva;
	
	/**
	 * Variable para indicar si se deben ocultar los cabezotes
	 */
	private String ocultarCabezotes;

	
	/**
	 * Función para resetear los valores de la funcionalidad.
	 *
	 */
	public void reset()
	{
		this.mapaTiposInmunizacion = new HashMap();
		this.mapaDatosVacunas = new HashMap();
		this.mapaAntVacunas = new HashMap();
		this.observacionesGrales ="";
		this.observacionesGralesNueva = "";
	}
	
	
	/**
	 * Metodo para validar la informacion digitada por el usuario.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		
		/*if(estado.equals("guardar"))
		{
			
		}*/
		
		return errores;
	}

//------------------------------------- SETS Y GETS ---------------------------------------------------//
	
	
	/**
	 * @return Retorna mapaTiposInmunizacion.
	 */
	public HashMap getMapaTiposInmunizacion() {
		return mapaTiposInmunizacion;
	}

	/**
	 * @param Asigna mapaTiposInmunizacion.
	 */
	public void setMapaTiposInmunizacion(HashMap mapaTiposInmunizacion) {
		this.mapaTiposInmunizacion = mapaTiposInmunizacion;
	}

	/**
	 * @return Retorna mapaTiposInmunizacion.
	 */
	public Object getMapaTiposInmunizacion(String key)
	{
		return mapaTiposInmunizacion.get(key);
	}

	/**
	 * @param Asigna mapaTiposInmunizacion.
	 */
	@SuppressWarnings("unchecked")
	public void setMapaTiposInmunizacion(String key, Object obj) 
	{
		this.mapaTiposInmunizacion.put(key, obj);
	}
	
	/**
	 * @return Retorna observacionesGrales
	 */
	public String getObservacionesGrales() {
		return observacionesGrales;
	}

	/**
	 * @param Asigna observacionesGrales
	 */
	public void setObservacionesGrales(String observacionesGrales) {
		this.observacionesGrales = observacionesGrales;
	}
	
	/**
	 * @return Retorna observacionesGralesNueva
	 */
	public String getObservacionesGralesNueva() {
		return observacionesGralesNueva;
	}

	/**
	 * @param Asigna observacionesGralesNueva
	 */
	public void setObservacionesGralesNueva(String observacionesGralesNueva) {
		this.observacionesGralesNueva = observacionesGralesNueva;
	}


	/**
	 * @return Retorna mapaDatosVacunas
	 */
	public HashMap getMapaDatosVacunas() {
		return mapaDatosVacunas;
	}

	/**
	 * @param Asigna mapaDatosVacunas 
	 */
	public void setMapaDatosVacunas(HashMap mapaDatosVacunas) {
		this.mapaDatosVacunas = mapaDatosVacunas;
	}
	
	/**
	 * @return Retorna mapaDatosVacunas.
	 */
	public Object getMapaDatosVacunas(String key)
	{
		return mapaDatosVacunas.get(key);
	}

	/**
	 * @param Asigna mapaDatosVacunas.
	 */
	@SuppressWarnings("unchecked")
	public void setMapaDatosVacunas(String key, Object obj) 
	{
		this.mapaDatosVacunas.put(key, obj);
	}

	/**
	 * @return Retorna estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param Asigna estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return Retorna mapaAntVacunas
	 */
	public HashMap getMapaAntVacunas() {
		return mapaAntVacunas;
	}

	/**
	 * @param Asigna mapaAntVacunas 
	 */
	public void setMapaAntVacunas(HashMap mapaAntVacunas) {
		this.mapaAntVacunas = mapaAntVacunas;
	}
	
	/**
	 * @return Retorna mapaAntVacunas.
	 */
	public Object getMapaAntVacunas(String key)
	{
		return mapaAntVacunas.get(key);
	}

	/**
	 * @param Asigna mapaAntVacunas.
	 */
	@SuppressWarnings("unchecked")
	public void setMapaAntVacunas(String key, Object obj) 
	{
		this.mapaAntVacunas.put(key, obj);
	}
	
	/**
	 * @return Returns the ocultarCabezotes.
	 */
	public String getOcultarCabezotes() {
		return ocultarCabezotes;
	}
	/**
	 * @param ocultarCabezotes The ocultarCabezotes to set.
	 */
	public void setOcultarCabezotes(String ocultarCabezotes) {
		this.ocultarCabezotes = ocultarCabezotes;
	}
}
