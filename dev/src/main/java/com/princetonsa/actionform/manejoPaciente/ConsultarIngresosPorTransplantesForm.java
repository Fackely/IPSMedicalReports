package com.princetonsa.actionform.manejoPaciente;



import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadCadena;
import util.UtilidadFecha;

import com.princetonsa.mundo.manejoPaciente.ConsultarIngresosPorTransplantes;








/**
 *@author Jhony Alexander Duque A.
 *jduque@princetonsa.com 
 */
public class ConsultarIngresosPorTransplantesForm extends ValidatorForm
{
	
	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	/**
	 * Para manjar los logger de la clase ConsultarIngresosPorTransplantesForm
	 */
	Logger logger = Logger.getLogger(ConsultarIngresosPorTransplantesForm.class);
	
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	//indices
	String [] indicesCriterios = ConsultarIngresosPorTransplantes.indicesCriterios;
	
	
	/*-----------------------------------------------
	 * 				ATRIBUTOS DEL PAGER
	 ------------------------------------------------*/
	/**
	 * String Patron Ordenar 
	 * **/
	private String patronOrdenar;
	
	/**
	 * String Ultimo Patron de Ordenamiento
	 * */
	private String ultimoPatron;
	
	/**
	 * String Link Siguiente
	 * */
	private String linkSiguiente;


	
	/*-----------------------------------------------
	 * 				FIN ATRIBUTOS DEL PAGER
	 ------------------------------------------------*/
	
	/*-----------------------------------------------------
	 * ATRIBUTOS DE LA CONSULTA DE INGRESOS POR TRANSPLANTES
	 ------------------------------------------------------*/
	
	private HashMap criterios = new HashMap ();
	
	private String estado;
	
	private ArrayList<HashMap<String, Object>> centrosAtent = new ArrayList<HashMap<String,Object>>();

	private HashMap listadoPaciente = new HashMap(); 
	
	private String index = "";
	
	private HashMap detalle = new HashMap ();
	
	private HashMap encabezado = new HashMap ();
	/*-----------------------------------------------------------
	 * FIN ATRIBUTOS DE LA CONSULTA DE INGRESOS POR TRANSPLANTES
	 -----------------------------------------------------------*/
	




	/*-------------------------------------------------------
	 * 						METODOS
	 --------------------------------------------------------*/
	public void reset ()
	{
		this.centrosAtent = new ArrayList<HashMap<String,Object>>();
		this.criterios = new HashMap ();
		this.listadoPaciente = new HashMap ();
		this.setListadoPaciente("numRegistros", 0);
		this.index="";
		this.detalle= new HashMap ();
		this.setDetalle("numRegistros", 0);
		this.encabezado = new  HashMap ();
		
	}
	
	/*-------------------------------------------------------
	 * 						FIN METODOS
	 --------------------------------------------------------*/
	

	/*-------------------------------------------------------
	 * 					METODO VALIDATE
	 --------------------------------------------------------*/
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		
		ActionErrors errores = super.validate(mapping, request);
		
		if (estado.equals("buscar") || estado.equals("imprimir"))
		{
			if (!UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[4])+"") && !UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[5])+""))
				errores.add("fechas", new ActionMessage("errors.required","Las Fechas Inicial y Final "));
			else
				if (!UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[4])+""))
					errores.add("fechas", new ActionMessage("errors.required","La Fecha Inicial "));
				else
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(),criterios.get(indicesCriterios[4])+""))
						errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual","Inicial "+criterios.get(indicesCriterios[4])+"","Actual "+UtilidadFecha.getFechaActual()));
					else
						if (!UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[5])+""))
							errores.add("fechas", new ActionMessage("errors.required","La Fecha Final "));
						else
							if(UtilidadFecha.esFechaMenorQueOtraReferencia(criterios.get(indicesCriterios[5])+"",criterios.get(indicesCriterios[4])+""))
								errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual","Final "+criterios.get(indicesCriterios[5])+"","Inicial "+criterios.get(indicesCriterios[4])));
							else
								if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(),criterios.get(indicesCriterios[5])+""))
									errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual","Final "+criterios.get(indicesCriterios[5])+"","Actual "+UtilidadFecha.getFechaActual()));
			
			if (!errores.isEmpty())
				this.setEstado("empezar");
		}
		
		return errores;
	}
	

	/*-------------------------------------------------------
	 * 					FIN METODO VALIDATE
	 --------------------------------------------------------*/
	

	/*---------------------------------------------------------
	 * 				METODOS GETTERS AND SETTERS
	 ----------------------------------------------------------*/
	




	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}

	
	public HashMap getEncabezado() {
		return encabezado;
	}
	public void setEncabezado(HashMap encabezado) {
		this.encabezado = encabezado;
	}
	public Object getEncabezado(String key) {
		return encabezado.get(key);
	}
	public void setEncabezado(String key,Object value) {
		this.encabezado.put(key, value);
	}

	
	

	public String getLinkSiguiente() {
		return linkSiguiente;
	}
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}



	public String getPatronOrdenar() {
		return patronOrdenar;
	}
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}
	public String getUltimoPatron() {
		return ultimoPatron;
	}
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}



	public HashMap getCriterios() {
		return criterios;
	}
	public void setCriterios(HashMap criterios) {
		this.criterios = criterios;
	}
	public Object getCriterios(String key) {
		return criterios.get(key);
	}
	public void setCriterios(String key, Object value) {
		this.criterios.put(key, value);
	}
	

	public ArrayList<HashMap<String, Object>> getCentrosAtent() {
		return centrosAtent;
	}



	public void setCentrosAtent(ArrayList<HashMap<String, Object>> centrosAtent) {
		this.centrosAtent = centrosAtent;
	}



	public HashMap getListadoPaciente() {
		return listadoPaciente;
	}
	public void setListadoPaciente(HashMap listadoPaciente) {
		this.listadoPaciente = listadoPaciente;
	}
	public Object getListadoPaciente(String key) {
		return listadoPaciente.get(key);
	}
	public void setListadoPaciente(String key,Object value) {
		this.listadoPaciente.put(key, value);
	}



	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}



	public HashMap getDetalle() {
		return detalle;
	}
	public void setDetalle(HashMap detalle) {
		this.detalle = detalle;
	}
	public Object getDetalle(String key) {
		return detalle.get(key);
	}
	public void setDetalle(String key, Object value) {
		this.detalle.put(key, value);
	}



	
	/*---------------------------------------------------------
	 * 				FIN METODOS GETTERS AND SETTERS
	 ----------------------------------------------------------*/



}