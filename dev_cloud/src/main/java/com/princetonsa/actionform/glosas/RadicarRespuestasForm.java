package com.princetonsa.actionform.glosas;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.mundo.glosas.RegistrarModificarGlosas;

public class RadicarRespuestasForm extends ValidatorForm
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(AprobarAnularRespuestasForm.class);
	
	/**
	 * Variable para manejo de Estado
	 */
	private String estado;
	
	/**
	 * Atributo Mensaje
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	/**
	 * Variable para almacenar el codigo del convenio
	 */
	private String convenio;
	
	/**
	 * Variable que almacena el numero de mapas en el arraylist
	 */
	private int numMapas;
	
	/**
	 * Arreglo para almacenar los Convenios
	 */
	private ArrayList<HashMap<String, Object>> arregloConvenios = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * Mapa para los valores de la consulta de la Respuesta
	 */
	private HashMap informacionRespuesta;
	
	/**
	 * Variable para almacenar el numero de radicacion
	 */
	private String numRad;
	
	/**
	 * Variable para almacenar las observaciones de Radicacion de Respuesta
	 */
	private String observ;
	
	/**
	 * Variable para almacenar la fecha de Radicacion de la Respuesta
	 */
	private String fecharadicacion;
	
	private HashMap<String, Object> encabezadoGlosaMap= new HashMap<String, Object>();
	
	private String guardo;
	
	private String numcontrato;
	
	
	public void reset(int codigoInstitucion)
	{		
		this.estado="";
		this.convenio="";
		this.numMapas=0;
		this.arregloConvenios = new ArrayList<HashMap<String,Object>>();
		this.informacionRespuesta= new HashMap();
		this.informacionRespuesta.put("numResgistros", "0");
		this.numRad="";
		this.observ="";
		this.fecharadicacion="";
		this.encabezadoGlosaMap= new HashMap<String, Object>();
		this.encabezadoGlosaMap.put("numRegistros", "0");
		this.guardo="N";
		this.numcontrato="";
	}



	public String getNumcontrato() {
		return numcontrato;
	}



	public void setNumcontrato(String numcontrato) {
		this.numcontrato = numcontrato;
	}



	public String getGuardo() {
		return guardo;
	}



	public void setGuardo(String guardo) {
		this.guardo = guardo;
	}



	public HashMap<String, Object> getEncabezadoGlosaMap() {
		return encabezadoGlosaMap;
	}



	public void setEncabezadoGlosaMap(HashMap<String, Object> encabezadoGlosaMap) {
		this.encabezadoGlosaMap = encabezadoGlosaMap;
	}

	
	public Object getEncabezadoGlosaMap(String key) {
		return encabezadoGlosaMap.get(key);
	}

	
	public void setEncabezadoGlosaMap(String key, Object value){
		this.encabezadoGlosaMap.put(key, value);
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
	 * @return the mensaje
	 */
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}	
	
	/**
	 * @return the convenio
	 */
	public String getConvenio() {
		return convenio;
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}

	/**
	 * @return the numMapas
	 */
	public int getNumMapas() {
		return numMapas;
	}

	/**
	 * @param numMapas the numMapas to set
	 */
	public void setNumMapas(int numMapas) {
		this.numMapas = numMapas;
	}

	/**
	 * @return the arregloConvenios
	 */
	public ArrayList<HashMap<String, Object>> getArregloConvenios() {
		return arregloConvenios;
	}

	/**
	 * @param arregloConvenios the arregloConvenios to set
	 */
	public void setArregloConvenios(
			ArrayList<HashMap<String, Object>> arregloConvenios) {
		this.arregloConvenios = arregloConvenios;
	}

	/**
	 * @return the informacionRespuesta
	 */
	public HashMap getInformacionRespuesta() {
		return informacionRespuesta;
	}

	/**
	 * @param informacionRespuesta the informacionRespuesta to set
	 */
	public void setInformacionRespuesta(HashMap informacionRespuesta) {
		this.informacionRespuesta = informacionRespuesta;
	}
	
	public Object getInformacionRespuesta(String key){
		return this.informacionRespuesta.get(key);
	}

	
	public void setInformacionRespuesta(String key, Object value){
		this.informacionRespuesta.put(key, value);
	}

	/**
	 * @return the numRad
	 */
	public String getNumRad() {
		return numRad;
	}

	/**
	 * @param numRad the numRad to set
	 */
	public void setNumRad(String numRad) {
		this.numRad = numRad;
	}

	/**
	 * @return the observ
	 */
	public String getObserv() {
		return observ;
	}

	/**
	 * @param observ the observ to set
	 */
	public void setObserv(String observ) {
		this.observ = observ;
	}


	/**
	 * @return the fecharadicacion
	 */
	public String getFecharadicacion() {
		return fecharadicacion;
	}

	/**
	 * @param fecharadicacion the fecharadicacion to set
	 */
	public void setFecharadicacion(String fecharadicacion) {
		this.fecharadicacion = fecharadicacion;
	}

	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
		ActionErrors errores= new ActionErrors();
        errores=super.validate(mapping,request); 
        
        if(this.estado.equals("radicar"))
        {
        	if(this.informacionRespuesta.get("codrespuesta").equals(""))
        		errores.add("descripcion",new ActionMessage("prompt.generico","No hay informacion de Respuesta de la Glosa."));        	
        	if(!this.informacionRespuesta.get("codrespuesta").equals("") && this.numRad.equals(""))
        		errores.add("descripcion",new ActionMessage("errors.required","El numero de radicacion de la Respuesta"));        	
        	if((this.informacionRespuesta.get("observresp")+"").length()>1950)
        		errores.add("", new ActionMessage("errors.maxlength", "Las Observaciones ", "2000"));
        	
        	if(!UtilidadFecha.esFechaValidaSegunAp(this.getFecharadicacion()))
        	{
        		errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido","de radicacion de la Respuesta"));
        	}
        	else
        	{
        		if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getFecharadicacion(), UtilidadFecha.getFechaActual()))
        		{
        			errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Radicación "+this.getFecharadicacion(), "Actual "+UtilidadFecha.getFechaActual()));
        		}
        		else
        		{	
		        	String fechaAprobacion= RegistrarModificarGlosas.obtenerFechaAprobacionGlosa(Utilidades.convertirAEntero(this.informacionRespuesta.get("codglosa")+"")+"");
		        	if(UtilidadFecha.esFechaValidaSegunAp(this.getFecharadicacion()) && UtilidadFecha.esFechaValidaSegunAp(fechaAprobacion))
		        	{	
		        		if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.getFecharadicacion(), fechaAprobacion))
		        		{
		        			errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "Radicación "+this.getFecharadicacion(), "Aprobación "+fechaAprobacion));
		        		}
		        	}
        		}	
        	}	
        	
        	
        }     
        if(this.getEstado().equals("buscarGlosa"))
        {        	
        	if((this.getInformacionRespuesta("glosasis").equals("")))
        		errores.add("descripcion",new ActionMessage("errors.required","El consecutivo de Glosa "));
        }
        return errores;
    }
}