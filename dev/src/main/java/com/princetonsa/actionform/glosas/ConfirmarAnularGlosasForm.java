package com.princetonsa.actionform.glosas;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ResultadoBoolean;

public class ConfirmarAnularGlosasForm extends ValidatorForm
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ConfirmarAnularGlosasForm.class);
	
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
	 * Variable para almacenar el codigo del convenio
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
	 * Variable para el motivo de la Confirmacion Anulacion
	 */
	private String motivo;
	
	/**
	 * Variable para el check confirmar / anular glosa
	 */
	private String checkCA;
	
	/**
	 * Variable para almacenar el codigo del convenio
	 */
	private String indicativoglosa;
	
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
		this.conveniosMap = new HashMap();
		this.conveniosMap.put("numRegistros", "0");
		this.informacionGlosa=new HashMap();
		this.informacionGlosa.put("numRegistros", "0");
		this.motivo="";
		this.checkCA="";
		this.contratoP="";
		this.indicativoglosa="";
		this.guardo="N";
		this.numcontrato="";
	}
	
	

	public String getNumcontrato() {
		return numcontrato;
	}



	public void setNumcontrato(String numcontrato) {
		this.numcontrato = numcontrato;
	}



	/**
	 * @return the guardo
	 */
	public String getGuardo() {
		return guardo;
	}



	/**
	 * @param guardo the guardo to set
	 */
	public void setGuardo(String guardo) {
		this.guardo = guardo;
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
	 * @return the checkCA
	 */
	public String getCheckCA() {
		return checkCA;
	}

	/**
	 * @param checkCA the checkCA to set
	 */
	public void setCheckCA(String checkCA) {
		this.checkCA = checkCA;
	}

	public String getContratoP() {
		return contratoP;
	}



	public void setContratoP(String contratoP) {
		this.contratoP = contratoP;
	}



	/**
	 * Metodo encargado de validar..
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
		ActionErrors errores= new ActionErrors();
        errores=super.validate(mapping,request);
        
        if(this.estado.equals("guardar"))
        {
        	logger.info("\n\nestado:: "+estado);
        	if(this.checkCA.equals(""))
        		errores.add("descripcion",new ActionMessage("prompt.generico","El campo de chequeo debe estar activo."));
        }
        
        return errores;
    }	
}