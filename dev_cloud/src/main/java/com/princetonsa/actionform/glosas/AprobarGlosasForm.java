package com.princetonsa.actionform.glosas;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ResultadoBoolean;

public class AprobarGlosasForm extends ValidatorForm
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(AprobarGlosasForm.class);
	
	/**
	 * Variable para manejo de Estado
	 */
	private String estado;
	
	/**
	 * Atributo Mensaje
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);	
	
	/**
	 * Variable para almacenar los Convenios
	 */
	private HashMap conveniosMap;
	
	/**
	 * Variable para almacenar el convenio seleccionado
	 */
	private String convenio;
	
	/**
	 * Variable para almacenar el codigo del contrato
	 */
	private String contratoP;
	
	/**
	 * Mapa para los valores de la consulta de la GLosa
	 */
	private HashMap informacionGlosa;
	
	/**
	 * Variable para almacenar el codigo del convenio
	 */
	private String indicativoglosa;
	
	/**
	 * Variable para el manejo del campo de chequeo
	 * @param codigoInstitucion
	 */
	private String check;
	
	private String guardo;
	private String numcontrato;
	
	public void reset(int codigoInstitucion)
	{
		this.estado="";
		this.convenio="";
		this.conveniosMap = new HashMap();
		this.conveniosMap.put("numRegistros", "0");
		this.informacionGlosa=new HashMap();
		this.informacionGlosa.put("numRegistros", "0");	
		this.indicativoglosa="";
		this.check="";
		this.contratoP="";
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



	public String getContratoP() {
		return contratoP;
	}


	public void setContratoP(String contratoP) {
		this.contratoP = contratoP;
	}


	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	public String getIndicativoglosa() {
		return indicativoglosa;
	}



	public void setIndicativoglosa(String indicativoglosa) {
		this.indicativoglosa = indicativoglosa;
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
	 * @return the conveniosMap
	 */
	public HashMap getConveniosMap() {
		return conveniosMap;
	}

	/**
	 * @param conveniosMap the conveniosMap to set
	 */
	public void setConveniosMap(HashMap conveniosMap) {
		this.conveniosMap = conveniosMap;
	}

	public Object getConveniosMap(String key) {
		return conveniosMap.get(key);
	}

	public void setConveniosMap(String key, Object value) {
		this.conveniosMap.put(key, value);
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
	 * @return the informacionGlosa
	 */
	public HashMap getInformacionGlosa() {
		return informacionGlosa;
	}

	/**
	 * @param informacionGlosa the informacionGlosa to set
	 */
	public void setInformacionGlosa(HashMap informacionGlosa) {
		this.informacionGlosa = informacionGlosa;
	}

	public Object getInformacionGlosa(String key) {
		return informacionGlosa.get(key);
	}

	public void setInformacionGlosa(String key, Object value) {
		this.informacionGlosa.put(key, value);
	}




	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
		ActionErrors errores= new ActionErrors();
        errores=super.validate(mapping,request);
        
        if(this.estado.equals("guardar"))
        {
        	logger.info("\n\nglosa::: "+this.informacionGlosa.get("codglosa"));
        	if(this.informacionGlosa.get("codglosa").equals(""))
        	{
        		errores.add("descripcion",new ActionMessage("errors.required","Cargar la informacion de la Glosa a Aprobar "));
        	}
        }
        
        return errores;
    }	
}