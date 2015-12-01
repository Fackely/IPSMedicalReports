package com.princetonsa.actionform.historiaClinica;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import util.ConstantesBD;
import util.historiaClinica.UtilidadesHistoriaClinica;


/**
 * Clase para el manejo de la parametrizacion 
 * de los eventos adversos
 * Date: 2008-05-12
 * @author garias@princetonsa.com
 */
public class EventosAdversosForm extends ValidatorForm 
{
	/**
	 * Estado de la funcionalidad
	 */
	private String estado;
	
	/**
	 * Indicador de posición mapa
	 */
	private int posMap; 
	
	/**
	 * Mapa con la información de los motivox
	 */
	private HashMap eventosMap;
	
	/**
	 * Mapa con la calsificación de los eventos
	 */
	private HashMap clasificacionEventosMap;
	
	/**
	 * String Patron Ordenar 
	 * **/
	private String patronOrdenar;
	
	/**
	 * String Ultimo Patron de Ordenamiento
	 * */
	private String ultimoPatron;
	
	/**
	 * Estado de auxiliar
	 */
	private String estadoAux;
	
	/**
	 *
	 */
	public void reset()
	{
		this.posMap=ConstantesBD.codigoNuncaValido;
		this.eventosMap=new HashMap();
		this.eventosMap.put("numRegistros", "0");
		this.clasificacionEventosMap=new HashMap();
		this.clasificacionEventosMap.put("numRegistros", "0");
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
        ActionErrors errores = new ActionErrors();
        errores = super.validate(mapping,request);
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
	 * @return the eventosMap
	 */
	public HashMap getEventosMap() {
		return eventosMap;
	}

	/**
	 * @param eventosMap the eventosMap to set
	 */
	public void setEventosMap(HashMap eventosMap) {
		this.eventosMap = eventosMap;
	}

	/**
	 * @return the eventosMap
	 */
	public Object getEventosMap(String llave) {
		return eventosMap.get(llave);
	}

	/**
	 * @param eventosMap the eventosMap to set
	 */
	public void setEventosMap(String llave, Object obj) {
		this.eventosMap.put(llave, obj);
	}
	
	/**
	 * @return the posMap
	 */
	public int getPosMap() {
		return posMap;
	}

	/**
	 * @param posMap the posMap to set
	 */
	public void setPosMap(int posMap) {
		this.posMap = posMap;
	}

	/**
	 * @return the clasificacionEventosMap
	 */
	public HashMap getClasificacionEventosMap() {
		return clasificacionEventosMap;
	}

	/**
	 * @param clasificacionEventosMap the clasificacionEventosMap to set
	 */
	public void setClasificacionEventosMap(HashMap clasificacionEventosMap) {
		this.clasificacionEventosMap = clasificacionEventosMap;
	}
	
	/**
	 * @return the clasificacionEventosMap
	 */
	public Object getClasificacionEventosMap(String llave) {
		return clasificacionEventosMap.get(llave);
	}

	/**
	 * @param clasificacionEventosMap the clasificacionEventosMap to set
	 */
	public void setClasificacionEventosMap(String llave, Object obj) {
		this.clasificacionEventosMap.put(llave, obj);
	}

	/**
	 * @return the estadoAux
	 */
	public String getEstadoAux() {
		return estadoAux;
	}

	/**
	 * @param estadoAux the estadoAux to set
	 */
	public void setEstadoAux(String estadoAux) {
		this.estadoAux = estadoAux;
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
    
    
}	