package com.princetonsa.actionform.manejoPaciente;





import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.mundo.manejoPaciente.ConsultaPreingresos;

import util.ConstantesBD;
import util.UtilidadCadena;
import util.UtilidadFecha;


/**
 *@author Jhony Alexander Duque A.
 *jduque@princetonsa.com 
 */
public class ConsultaPreingresosForm extends ValidatorForm
{
	
	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	/**
	 * Para manjar los logger de la clase AperturaIngresosForm
	 */
	Logger logger = Logger.getLogger(AperturaIngresosForm.class);
	
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	
	//-------------------------------------------------
	//manejo de indices
	
	String [] indicesCriterios = ConsultaPreingresos.indicesCriterios;
	
	
	/*------------------------------------------------
	 * 			ATRIBUTOS DE CONSULTA PREINGRESOS  
	 -------------------------------------------------*/
	
	/**
	 * String Link Siguiente
	 * */
	private String linkSiguiente;	
	
	
	/**
	 * maneja los estados en el action
	 */
	private String estado ="";
	
	private String index = "";
	
	/**
	 * Almacena los criterios de busqueda 
	 * de la busqueda por rangos
	 */
	private HashMap criterios = new HashMap ();
	
	
	/**
	 * almacena los centros de atencion a listar
	 * en el select
	 */
	private ArrayList<HashMap<String, Object>> centrosAtent = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * Almacena los usuarios listados en la funcionalidad por 
	 * rangos
	 */
	private ArrayList<HashMap<String, Object>> usuarios= new ArrayList<HashMap<String,Object>>();
	/**
	 * mapa que almacena los resultados de la busqueda
	 */
	private HashMap listado = new HashMap ();
	
	
	/**
	 * almacena los datos del detalle del preingreso 
	 */
	private HashMap detalle = new HashMap ();
	
	
	/*------------------------------------------------
	 * 			ATRIBUTOS DE CONSULTA PREINGRESOS
	 -------------------------------------------------*/

	
	
	/*---------------------------------------------------------
	 * 				METODOS SETTER AND GETTERS
	 -----------------------------------------------------------*/
	
	//--------ESTADO------------------------------------------- 
//-------- DETALLE ------------------------
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
//-----------------------------------------------------------	
	
//-------centros de atencion a listar -------------------------	
public ArrayList<HashMap<String, Object>> getCentrosAtent() {
		return centrosAtent;
	}

	public void setCentrosAtent(ArrayList<HashMap<String, Object>> centrosAtent) {
		this.centrosAtent = centrosAtent;
	}
//--------------------------------------------------------------------------------
	
	//----- criterios de busqueda -------------------------------------
	public HashMap getCriterios() {
		return criterios;
	}

	public void setCriterios(HashMap criterios) {
		this.criterios = criterios;
	}
	
	public Object getCriterios(String key) {
		return criterios.get(key);
	}

	public void setCriterios(String key,Object value) {
		this.criterios.put(key, value);
	}
//-------------------------------------------------------------------	
	

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	} 
	//----------------------------------------------------------
	
	
	public HashMap getListado() {
		return listado;
	}

	public void setListado(HashMap listado) {
		this.listado = listado;
	}
	
	public Object getListado(String key) {
		return listado.get(key);
	}

	public void setListado(String key,Object value) {
		this.listado.put(key, value);
	}

	public ArrayList<HashMap<String, Object>> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(ArrayList<HashMap<String, Object>> usuarios) {
		this.usuarios = usuarios;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}
	
	/*---------------------------------------------------------
	 * 				FIN METODOS SETTER AND GETTERS
	 -----------------------------------------------------------*/
	
	
	public void reset ()
	{
		this.listado= new HashMap ();
		this.setListado("numRegistros", 0);
		this.usuarios = new ArrayList<HashMap<String,Object>>();
		this.centrosAtent = new ArrayList<HashMap<String,Object>>();
		this.criterios= new HashMap ();
		this.index="";
		this.detalle = new HashMap ();
		this.linkSiguiente="";
	}

	
	/***********************************************
	 * METODO VALIDATE
	 ***************************************************/
	
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
	
		ActionErrors errores = super.validate(mapping, request); 
		boolean ban =true;
		
		if (estado.equals("buscar"))
		{
			if (!UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[0])+"") || (criterios.get(indicesCriterios[0])+"").equals(ConstantesBD.codigoNuncaValido+""))
				errores.add("mensaje", new ActionMessage("errors.required","El Centro de Atencion "));
		
			if (!UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[1])+"") && !UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[2])+""))
				errores.add("fechas", new ActionMessage("errors.required","Las Fechas Inicial y Final "));
			else
			{
				if (!UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[1])+""))
				{
					errores.add("fechas", new ActionMessage("errors.required","La Fecha Inicial "));
					ban=false;
				}
				else
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(),criterios.get(indicesCriterios[1])+""))
						errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual","Inicial "+criterios.get(indicesCriterios[1])+"","Actual "+UtilidadFecha.getFechaActual()));
					
				
				if (!UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[2])+""))
				{
					errores.add("fechas", new ActionMessage("errors.required","La Fecha Final "));
					ban=false;
				}
				else
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(),criterios.get(indicesCriterios[2])+""))
						errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual","Final "+criterios.get(indicesCriterios[2])+"","Actual "+UtilidadFecha.getFechaActual()));
					
				if (ban)
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(criterios.get(indicesCriterios[2])+"",criterios.get(indicesCriterios[1])+""))
					{
						errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual","Inicial "+criterios.get(indicesCriterios[1])+"","Final "+criterios.get(indicesCriterios[2])+""));
						ban=false;
					}
				if (ban)	
					if(UtilidadFecha.numeroMesesEntreFechasExacta(criterios.get(indicesCriterios[1])+"", criterios.get(indicesCriterios[2])+"") >= 3)
						errores.add("", new ActionMessage("error.facturacion.maximoRangoFechas", "para consultar Preingresos", "3", "90"));
					
				
			}	
			
			
			
						
			
		}

		if (!errores.isEmpty())
			this.setEstado("periodo");
		
		
		return errores;
	}

	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}
	
	
	
}