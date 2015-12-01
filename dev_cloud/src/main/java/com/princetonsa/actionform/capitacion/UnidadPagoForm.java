package com.princetonsa.actionform.capitacion;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;



public class UnidadPagoForm extends ActionForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(UnidadPagoForm.class);
	

	/**
	 * Estado para el manejo del flujo de la funcionalidad
	 */
	private String estado;


	/**
	 * Mapa para el manejo de las consultas
	 */
	private HashMap mapa;


	private boolean esConsulta;  
	
	/**
	 * Función para resetear los valores de la funcionalidad.
	 *
	 */
	public void reset()
	{
		this.mapa = new HashMap(); 
		this.enviarUltimaPagina = false;
	}
	
	
	/**
	 * variables para la Navegacion. 
	 */
	private String linkSiguiente;
	private boolean enviarUltimaPagina;
	
	
	/**
	 * Metodo para validar la informacion digitada por el usuario.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		return errores;
	}

	
	
	/**
	 * @return Retorna mapa.
	 */
	public HashMap getMapa() {
		return mapa;
	}

	/**
	 * @param Asigna mapa.
	 */
	public void setMapa(HashMap mapa) {
		this.mapa = mapa;
	}

	/**
	 * @return Retorna mapa.
	 */
	public Object getMapa(String key)
	{
		return mapa.get(key);
	}

	/**
	 * @param Asigna mapa.
	 */
	public void setMapa(String key, Object obj) 
	{
		this.mapa.put(key, obj);
	}



	public String getEstado() {
		return estado;
	}



	public void setEstado(String estado) {
		this.estado = estado;
	}



	public boolean getEnviarUltimaPagina() {
		return enviarUltimaPagina;
	}



	public void setEnviarUltimaPagina(boolean enviarUltimaPagina) {
		this.enviarUltimaPagina = enviarUltimaPagina;
	}



	public String getLinkSiguiente() {
		return linkSiguiente;
	}



	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}



	public boolean isEsConsulta() {
		return esConsulta;
	}



	public void setEsConsulta(boolean esConsulta) {
		this.esConsulta = esConsulta;
	}

}
