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


public class AprobarAnularRespuestasForm extends ValidatorForm
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
	 * Variable para el motivo de la Confirmacion Anulacion
	 */
	private String motivo;
	
	/**
	 * Variable para el check confirmar / anular glosa
	 */
	private String checkApAn;
	
	/**
	 * Mapa para los valores de la consulta de la Respuesta
	 */
	private HashMap informacionRespuesta;

	private HashMap<String, Object> encabezadoGlosaMap= new HashMap<String, Object>(); 
	
	private String guardo;
	
	private String numcontrato;
	
	/**
	 * Resetea los valores de la forma cuando
	 * se ingresa por la funcionalidad por Area
	 * @param codigoInstitucion
	 */
	public void reset(int codigoInstitucion)
	{		
		this.estado="";
		this.convenio="";
		this.numMapas=0;
		this.arregloConvenios = new ArrayList<HashMap<String,Object>>();
		this.motivo="";
		this.checkApAn="";
		this.informacionRespuesta= new HashMap();
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
	
	
	public Object getEncabezadoGlosaMap(String key){
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
	 * @return the motivo
	 */
	public String getMotivo() {
		return motivo;
	}

	/**
	 * @param motivo the motivo to set
	 */
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	/**
	 * @return the checkApAn
	 */
	public String getCheckApAn() {
		return checkApAn;
	}

	/**
	 * @param checkApAn the checkApAn to set
	 */
	public void setCheckApAn(String checkApAn) {
		this.checkApAn = checkApAn;
	}		

	/**
	 * @return the informacionResp
	 */
	public HashMap getInformacionRespuesta() {
		return informacionRespuesta;
	}

	/**
	 * @param informacionResp the informacionResp to set
	 */
	public void setInformacionRespuesta(HashMap informacionResp) {
		this.informacionRespuesta = informacionResp;
	}
		
	public Object getInformacionRespuesta(String key){
		return this.informacionRespuesta.get(key);
	}
	
	public void setInformacionRespuesta(String key, Object value){
		this.informacionRespuesta.put(key,value);
	}	



	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
		ActionErrors errores= new ActionErrors();
        errores=super.validate(mapping,request);  
        
        if(this.estado.equals("guardar"))
        {   
        	if(this.informacionRespuesta.get("codrespuesta").equals(""))
        		errores.add("descripcion",new ActionMessage("prompt.generico","No hay informacion de Repsuesta."));        	
        	if(!this.informacionRespuesta.get("codrespuesta").equals("") && this.checkApAn.equals(""))
        		errores.add("descripcion",new ActionMessage("prompt.generico","Es necesario seleccionar Aprobar o Anular para actualizar informacion."));
        	if(this.checkApAn.equals("ANU") && this.motivo.equals(""))
        	{
        		this.checkApAn="";
        		errores.add("descripcion",new ActionMessage("errors.required","El motivo de anulacion"));
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