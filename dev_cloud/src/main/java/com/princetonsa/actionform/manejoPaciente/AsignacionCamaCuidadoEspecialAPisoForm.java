package com.princetonsa.actionform.manejoPaciente;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.mundo.manejoPaciente.AsignacionCamaCuidadoEspecialAPiso;

import util.ConstantesBD;
import util.UtilidadFecha;





/**
 *@author Jhony Alexander Duque A.
 *jduque@princetonsa.com 
 */
public class AsignacionCamaCuidadoEspecialAPisoForm extends ValidatorForm
{
	
	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	/**
	 * Para manjar los logger de la clase AsignacionCamaCuidadoEspecialForm
	 */
	Logger logger = Logger.getLogger(AsignacionCamaCuidadoEspecialAPisoForm.class);
	
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	
	//--------indices ---------------------
	String indicesDetalle [] = AsignacionCamaCuidadoEspecialAPiso.indicesDetalle;
	
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

	private String estado="";
	
	
	private String index="";
	
	/**
	 * Almacena el listado de paciente 
	 */
	private HashMap listadoPacientes = new HashMap ();
	
	/**
	 * almacena la informacion del detalle
	 */
	private HashMap detalle = new HashMap ();
	
	/**
	 * se almacenan los centros de costo
	 */
	private ArrayList<HashMap<String, Object>> CCosto = new ArrayList<HashMap<String,Object>>();
	
	private boolean operacionTrue=false;
	
	private HashMap mapaCamaNuevaPaciente = new HashMap();



	public void reset ()
	{
		this.index="";
		this.listadoPacientes = new HashMap ();
		this.setListadoPacientes("numRegistros", 0);
		this.detalle = new HashMap ();
		this.setDetalle("numRegistros", 0);
		this.CCosto = new ArrayList<HashMap<String,Object>>();
		this.operacionTrue=false;
		this.mapaCamaNuevaPaciente= new HashMap ();
		
	}
	
	
	
	
	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
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

//---listado de pacientes --------------------
	public HashMap getListadoPacientes() {
		return listadoPacientes;
	}
	public void setListadoPacientes(HashMap listadoPacientes) {
		this.listadoPacientes = listadoPacientes;
	}	
	public Object getListadoPacientes(String key) {
		return listadoPacientes.get(key);
	}
	public void setListadoPacientes(String key, Object value) {
		this.listadoPacientes.put(key, value);
	}
//-----------------------------------------------


//----detalle ----------------------------------
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
//-----------------------------------------------
	
//----------centros de costo
	public ArrayList<HashMap<String, Object>> getCCosto() {
		return CCosto;
	}
	public void setCCosto(ArrayList<HashMap<String, Object>> costo) {
		CCosto = costo;
	}
//--------------------------------------------------------------------
	
	public boolean isOperacionTrue() {
		return operacionTrue;
	}

	public void setOperacionTrue(boolean operacionTrue) {
		this.operacionTrue = operacionTrue;
	}

///---------mapa de la cama nueva -------------------
	public HashMap getMapaCamaNuevaPaciente() {
		return mapaCamaNuevaPaciente;
	}
	public void setMapaCamaNuevaPaciente(HashMap mapaCamaNuevaPaciente) {
		this.mapaCamaNuevaPaciente = mapaCamaNuevaPaciente;
	}
	
	
	public Object getMapaCamaNuevaPaciente(String key) {
		return mapaCamaNuevaPaciente.get(key);
	}
	public void setMapaCamaNuevaPaciente(String key, Object value) {
		this.mapaCamaNuevaPaciente.put(key, value);
	}

//------------------------------------------------------------------------
	
	
	
	/**************************************************
	 * 				METODO VALIDATE
	 **************************************************/


	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = super.validate(mapping, request); 
		
		if (this.estado.equals("guardar"))
		{
			//se toma la fecha y hora del sistema 
			String hora=UtilidadFecha.getHoraActual();
			String fecha=UtilidadFecha.getFechaActual();
			
			//se toma la fecha y hora del traslado del paciente
			
			String [] fechaHoraTraslado=(this.detalle.get(indicesDetalle[22])+"").split(" ");
			
			String fechaTraslado=fechaHoraTraslado[0];
			String horaTraslado=fechaHoraTraslado[1];
			
			//se compara que la fecha y hora de la asigancion de la cama sea anterior o igual a la fecha y hora del sistema
			if (!UtilidadFecha.compararFechas(fecha,hora,this.detalle.get(indicesDetalle[0])+"",this.detalle.get(indicesDetalle[1])+"").isTrue())
			{
				errores.add("descripcion",new ActionMessage("errors.fechaHoraPosteriorIgualActual","de la asignación de la cama: "+ this.detalle.get(indicesDetalle[0])+" - "+this.detalle.get(indicesDetalle[1]) ,"del sistema: "+fecha+" - "+hora));
				
			}
			
			
			//la fecha y hora de asignacion de la cama debe ser posterior a la fecha de la orden del traslado
			if (!UtilidadFecha.compararFechas(this.detalle.get(indicesDetalle[0])+"",this.detalle.get(indicesDetalle[1])+"",fechaTraslado,horaTraslado).isTrue())
			{
				errores.add("descripcion",new ActionMessage("errors.fechaHoraAnteriorOtraReferencia","de la asignación de la cama: "+ this.detalle.get(indicesDetalle[0])+" - "+this.detalle.get(indicesDetalle[1]) ,"de la orden "+fechaTraslado+" - "+horaTraslado));
				
			}
			
			if ((this.detalle.get(indicesDetalle[2])+"").equals(ConstantesBD.codigoNuncaValido+""))
				errores.add("descripcion",new ActionMessage("errors.required","El Centro de costo"));	
			
			
		}
		
		return errores;
	}




}