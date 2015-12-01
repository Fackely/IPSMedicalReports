package com.princetonsa.actionform.manejoPaciente;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.Utilidades;

/**
 * 
 * @author wilson
 *
 */
public class ReingresoHospitalDiaForm extends ValidatorForm 
{
	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * mapa de consultorios
	 */
	private HashMap mapa;
	
	/**
	 * 
	 */
	private String tipoIdentificacion;
	
	/**
	 * 
	 */
	private String numeroIdentificacion;
	
	/**
	 * 
	 */
	private String primerNombre;
	
	/**
	 * 
	 */
	private String primerApellido;
	
	/**
	 * 
	 */
	private String patronOrdenar;
	
	/**
	 * 
	 */
	private String ultimoPatron;
    
	/**
	 * 
	 */
	private HashMap tiposIdentificacionTagMap;
	
	/**
	 * 
	 */
	private String observaciones;
	
	/**
	 * 
	 */
	private int indexSeleccionado;
	
	/**
     * resetea los atributos del form
     *
     */
    public void reset(int codigoInstitucion)
    {
    	this.mapa= new HashMap();
    	this.mapa.put("numRegistros", "0");
    	this.patronOrdenar="";
    	this.ultimoPatron="";
    	this.resetCriteriosBusqueda();
    	this.inicializarTagMap(codigoInstitucion);
    	this.observaciones="";
    	this.indexSeleccionado=ConstantesBD.codigoNuncaValido;
    }

    /**
     * 
     *
     */
    public void resetCriteriosBusqueda()
    {
    	this.tipoIdentificacion="";
    	this.numeroIdentificacion="";
    	this.primerNombre="";
    	this.primerApellido="";
    }
    
    /**
     * 
     * @param codigoInstitucion
     */
    public void inicializarTagMap(int codigoInstitucion)
    {
    	this.tiposIdentificacionTagMap= Utilidades.consultarTiposidentificacion(codigoInstitucion);
    }
    
    
    /**
     * Validate the properties that have been set from this HTTP request, and
     * return an <code>ActionErrors</code> object that encapsulates any
     * validation errors that have been found.  If no errors are found, return
     * <code>null</code> or an <code>ActionErrors</code> object with no recorded
     * error messages.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
    */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
        ActionErrors errores= new ActionErrors();
        errores=super.validate(mapping,request);
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

	/**
	 * @return the mapa
	 */
	public HashMap getMapa() {
		return mapa;
	}

	/**
	 * @param mapa the mapa to set
	 */
	public void setMapa(HashMap mapa) {
		this.mapa = mapa;
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
	 * @return the mapa
	 */
	public Object getMapa(Object key) {
		return mapa.get(key);
	}

	/**
	 * @param mapa the mapa to set
	 */
	public void setMapa(Object key, Object value) {
		this.mapa.put(key, value);
	}

	/**
	 * @return the numeroIdentificacion
	 */
	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	/**
	 * @param numeroIdentificacion the numeroIdentificacion to set
	 */
	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	/**
	 * @return the primerApellido
	 */
	public String getPrimerApellido() {
		return primerApellido;
	}

	/**
	 * @param primerApellido the primerApellido to set
	 */
	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}

	/**
	 * @return the primerNombre
	 */
	public String getPrimerNombre() {
		return primerNombre;
	}

	/**
	 * @param primerNombre the primerNombre to set
	 */
	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}

	/**
	 * @return the tipoIdentificacion
	 */
	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	/**
	 * @param tipoIdentificacion the tipoIdentificacion to set
	 */
	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	/**
	 * @return the tiposIdentificacionTagMap
	 */
	public HashMap getTiposIdentificacionTagMap() {
		return tiposIdentificacionTagMap;
	}

	/**
	 * @param tiposIdentificacionTagMap the tiposIdentificacionTagMap to set
	 */
	public void setTiposIdentificacionTagMap(HashMap tiposIdentificacionTagMap) {
		this.tiposIdentificacionTagMap = tiposIdentificacionTagMap;
	}
    
	/**
	 * @return the tiposIdentificacionTagMap
	 */
	public Object getTiposIdentificacionTagMap(Object key) {
		return tiposIdentificacionTagMap.get(key);
	}

	/**
	 * @param tiposIdentificacionTagMap the tiposIdentificacionTagMap to set
	 */
	public void setTiposIdentificacionTagMap(Object key, Object value) {
		this.tiposIdentificacionTagMap.put(key, value);
	}

	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @return the indexSeleccionado
	 */
	public int getIndexSeleccionado() {
		return indexSeleccionado;
	}

	/**
	 * @param indexSeleccionado the indexSeleccionado to set
	 */
	public void setIndexSeleccionado(int indexSeleccionado) {
		this.indexSeleccionado = indexSeleccionado;
	}

}