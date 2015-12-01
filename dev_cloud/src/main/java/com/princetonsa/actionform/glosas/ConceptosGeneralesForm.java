package com.princetonsa.actionform.glosas;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;

/**
 * @author Mauricio Jaramillo
 * Fecha: Septiembre de 2008
 */

public class ConceptosGeneralesForm extends ValidatorForm
{

	private String estado;
	
	/**
	 * HashMap para almacenar los resultados arrojados por la consulta de Conceptos Generales Glosas
	 */
	private HashMap conceptosGenerales;
	
	/**
     * Mensaje que informa sobre la generacion de la aplicacion de Conceptos Generales Glosas
     */
    private ResultadoBoolean mensaje = new ResultadoBoolean(false);
	
	/**
     * Para controlar la página actual del pager.
     */
    private int offset;
    
    /**
     * El numero de registros por pager
     */
    private int maxPageItems;
    
    /**
     * Para controlar el link siguiente del pager 
     */
    private String linkSiguiente;
    
    /**
     * String para ordenar por un nuevo patron
     */
    private String patronOrdenar;
	
	/**
	 * String que almacena el ordenamiento del ultimo patron ordenado 
	 */
    private String ultimoPatron;
	
    /**
     * String código digito por el usuario
     */
    private String codigoConcepto;
    
    /**
     * String para manejar la descripcion del concepto general a ingresar
     */
    private String descripcionConcepto;
    
    /**
     * String para manejar el tipo de concepto general a ingresar
     */
    private String tipoConcepto;
    
    /**
     * String para manejar si el concepto general de glosas es activo o no?
     */
    private String activoConcepto;
    
    /**
     * Posición del concepto general seleccionado en el listado principal
     */
    private int posicion;
    
    /**
     * Boolean que indica si el registro a modificar esta siendo utilizado;
     * con el fin de validarlo en la vista y solo habilitar los campos correpondientes  
     */
    private boolean esUtilizado;
    
    /**
     * Variable String para almacenar la información original del LOG
     */
    private String log;
    
	/**
	 * Método reset de la forma. Inicializa las variables
	 */
	public void reset()
	{
		this.conceptosGenerales = new HashMap<String, Object>();
		this.conceptosGenerales.put("numRegistros", "0");
		this.offset = ConstantesBD.codigoNuncaValido;
		this.maxPageItems = ConstantesBD.codigoNuncaValido;
		this.linkSiguiente = "";
		this.patronOrdenar = "";
		this.ultimoPatron = "";
		this.codigoConcepto = "";
		this.descripcionConcepto = "";
		this.tipoConcepto = "";
		this.activoConcepto = ConstantesBD.acronimoSi;
		this.posicion = ConstantesBD.codigoNuncaValido;
		this.esUtilizado = true;
		this.log = "";
	}
	
   /**
	* Control de Errores (Validaciones)
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		//Validaciones al momento de guardar de un registro nuevo
		if(this.estado.equals("guardar"))
		{
			if(this.codigoConcepto.trim().equals("") || this.codigoConcepto.trim().equals("null"))
				errores.add("codigoConcepto", new ActionMessage("errors.required", "El Código del Concepto General "));
			if(this.descripcionConcepto.trim().equals("") || this.descripcionConcepto.trim().equals("null"))
				errores.add("codigoConcepto", new ActionMessage("errors.required", "La Descripción del Concepto General "));
			if(this.tipoConcepto.equals("") || this.tipoConcepto.equals("null"))
				errores.add("codigoConcepto", new ActionMessage("errors.required", "El Tipo de Concepto General "));
		}
		//Validaciones al momento de guardar un registro modificado
		else if(this.estado.equals("guardarModificar"))
		{
			if((this.conceptosGenerales.get("consecutivo_"+this.posicion)+"").trim().equals("") || (this.conceptosGenerales.get("consecutivo_"+this.posicion)+"").trim().equals("null"))
				errores.add("codigoConcepto", new ActionMessage("errors.required", "El Código del Concepto General "));
			if((this.conceptosGenerales.get("descripcion_"+this.posicion)+"").trim().equals("") || (this.conceptosGenerales.get("descripcion_"+this.posicion)+"").trim().equals("null"))
				errores.add("codigoConcepto", new ActionMessage("errors.required", "La Descripción del Concepto General "));
			if((this.conceptosGenerales.get("tipoglosa_"+this.posicion)+"").trim().equals("") || (this.conceptosGenerales.get("tipoglosa_"+this.posicion)+"").trim().equals("null"))
				errores.add("codigoConcepto", new ActionMessage("errors.required", "El Tipo de Concepto General "));
		}
		
		return errores;
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

	//---------------Inicio Conceptos Generales-------------
	/**
	 * @return the conceptosGenerales
	 */
	public HashMap getConceptosGenerales() {
		return conceptosGenerales;
	}

	/**
	 * @param conceptosGenerales the conceptosGenerales to set
	 */
	public void setConceptosGenerales(HashMap conceptosGenerales) {
		this.conceptosGenerales = conceptosGenerales;
	}
	
	/**
	 * @param key
	 * @return
	 */
	public Object getConceptosGenerales(String key) {
        return conceptosGenerales.get(key);
    }
    
	/**
	 * @param key
	 * @param value
	 */
	public void setConceptosGenerales(String key,Object value) {
        this.conceptosGenerales.put(key, value);
    }
	//---------------Fin Conceptos Generales-------------

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * @return the maxPageItems
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}

	/**
	 * @param maxPageItems the maxPageItems to set
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}

	/**
	 * @return the linkSiguiente
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	/**
	 * @param linkSiguiente the linkSiguiente to set
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @return the ultimoPatron
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}

	/**
	 * @param ultimoPatron the ultimoPatron to set
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	/**
	 * @return the codigoConcepto
	 */
	public String getCodigoConcepto() {
		return codigoConcepto;
	}

	/**
	 * @param codigoConcepto the codigoConcepto to set
	 */
	public void setCodigoConcepto(String codigoConcepto) {
		this.codigoConcepto = codigoConcepto;
	}

	/**
	 * @return the descripcionConcepto
	 */
	public String getDescripcionConcepto() {
		return descripcionConcepto;
	}

	/**
	 * @param descripcionConcepto the descripcionConcepto to set
	 */
	public void setDescripcionConcepto(String descripcionConcepto) {
		this.descripcionConcepto = descripcionConcepto;
	}
	
	/**
	 * @return the tipoConcepto
	 */
	public String getTipoConcepto() {
		return tipoConcepto;
	}

	/**
	 * @param tipoConcepto the tipoConcepto to set
	 */
	public void setTipoConcepto(String tipoConcepto) {
		this.tipoConcepto = tipoConcepto;
	}

	/**
	 * @return the activoConcepto
	 */
	public String getActivoConcepto() {
		return activoConcepto;
	}

	/**
	 * @param activoConcepto the activoConcepto to set
	 */
	public void setActivoConcepto(String activoConcepto) {
		this.activoConcepto = activoConcepto;
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
	 * @return the posicion
	 */
	public int getPosicion() {
		return posicion;
	}

	/**
	 * @param posicion the posicion to set
	 */
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}

	/**
	 * @return the esUtilizado
	 */
	public boolean isEsUtilizado() {
		return esUtilizado;
	}

	/**
	 * @param esUtilizado the esUtilizado to set
	 */
	public void setEsUtilizado(boolean esUtilizado) {
		this.esUtilizado = esUtilizado;
	}

	/**
	 * @return the log
	 */
	public String getLog() {
		return log;
	}

	/**
	 * @param log the log to set
	 */
	public void setLog(String log) {
		this.log = log;
	}
	
}