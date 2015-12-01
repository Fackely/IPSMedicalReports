package com.princetonsa.actionform.manejoPaciente;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;



/**
 * Clase para el manejo de secciones y subsecciones x almacen
 * Date: 2008-05-09
 * @author lgchavez@princetonsa.com
 */
public class EncuestaCalidadAtencionForm extends ValidatorForm 
{
	
	/************************************************/
	//atributos para el uso del pager
	
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
	
	/**
	 * estado del formulario
	 */
	private String estado;
	

	/**
	 * Mapa Ingresos. mapa donde se almacena la consulta inicial de la funcionalidad
	 */
	private HashMap ingresosMap;
	
	/**
	 * Centro atencion
	 */
	private String centroAtencion;
	
	/**
	 * Mapa de las areas o centros costo
	 */
	private HashMap centrosCosto;
	
	/**
	 * Mapa donde se almacenan los datos de la encuesta
	 */
	private HashMap encuesta;
	
	/**
	 * Mapa de motivos de la calificacion
	 */
	private HashMap motivos;
	
	private int ingreso;
	
	
	private int capa;
	
	private int mostrart;
	
	private String calificacion;
	
	
	/**
	 * 
	 */
	public void reset()
	{
		this.ingresosMap=new HashMap();
		this.ingresosMap.put("numRegistros", 0);
		this.centrosCosto=new HashMap();
		this.centrosCosto.put("numRegistros", 0);
		this.centroAtencion="";
		this.encuesta=new HashMap();
		this.motivos=new HashMap();
		this.motivos.put("numRegistros", 0);
		this.capa=0;
		this.ingreso=ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 *reset encuesta 
	 */
	public void reset1()
	{
		this.centrosCosto=new HashMap();
		this.centrosCosto.put("numRegistros", 0);
		this.encuesta=new HashMap();
		this.encuesta.put("calificacion_0", ConstantesIntegridadDominio.acronimoSatisfaccion);
		this.motivos=new HashMap();
		this.motivos.put("numRegistros", 0);
		this.capa=0;
		this.mostrart=0;
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
        ActionErrors errors= new ActionErrors();
        errors=super.validate(mapping,request);
        
        if (this.estado.equals("guardar"))
	        {
	        
	        	if(!encuesta.containsKey("calificacion_0") || (encuesta.containsKey("calificacion_0") && encuesta.get("calificacion_0").toString().equals("")))
	        	{
	        		errors.add("Motivo", new ActionMessage("errors.required", "Calificación "));	
	        	}
	        	if(!encuesta.containsKey("motivo_0") || (encuesta.containsKey("motivo_0") && encuesta.get("motivo_0").toString().equals("-1")))
	        	{
	        		errors.add("Motivo", new ActionMessage("errors.required", "Motivo de la calificación"));	
	        	}
	        	if(encuesta.get("area").toString().equals(ConstantesBD.separadorSplit+""))
	        	{
	        		errors.add("Motivo", new ActionMessage("errors.required", "Área de la calificación"));	
	        	}

	        	
	        }
        if(this.estado.equals("modificar"))
	        {
	        	if(!encuesta.containsKey("observaciones") || (encuesta.containsKey("observaciones") && encuesta.get("observaciones").toString().equals("")))
	    		{
	        		errors.add("Motivo", new ActionMessage("errors.required", "Observaciones"));
	    		}
	        	if(encuesta.get("area").toString().equals(ConstantesBD.separadorSplit+""))
	        	{
	        		errors.add("Motivo", new ActionMessage("errors.required", "Área de la calificación"));	
	        	}
	        }
        
        
        
        
    	if(!errors.isEmpty())
    		this.estado="mostrarCapa";
        
        return errors;        
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
	 * @return the ingresosMap
	 */
	public HashMap getIngresosMap() {
		return ingresosMap;
	}

	/**
	 * @param ingresosMap the ingresosMap to set
	 */
	public void setIngresosMap(HashMap ingresosMap) {
		this.ingresosMap = ingresosMap;
	}

	/**
	 * @return the centroAtencion
	 */
	public String getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the centrosCosto
	 */
	public HashMap getCentrosCosto() {
		return centrosCosto;
	}

	/**
	 * @param centrosCosto the centrosCosto to set
	 */
	public void setCentrosCosto(HashMap centrosCosto) {
		this.centrosCosto = centrosCosto;
	}

	/**
	 * @return the encuesta
	 */
	public HashMap getEncuesta() {
		return encuesta;
	}

	/**
	 * @param encuesta the encuesta to set
	 */
	public void setEncuesta(HashMap encuesta) {
		this.encuesta = encuesta;
	}

	/**
	 * @param encuesta the encuesta to set
	 */
	public void setEncuesta(String key, Object o) {
		this.encuesta.put(key, o);
	}
	
	/**
	 * @return the motivos
	 */
	public HashMap getMotivos() {
		return motivos;
	}

	/**
	 * @param motivos the motivos to set
	 */
	public void setMotivos(HashMap motivos) {
		this.motivos = motivos;
	}

	/**
	 * @return the capa
	 */
	public int getCapa() {
		return capa;
	}

	/**
	 * @param capa the capa to set
	 */
	public void setCapa(int capa) {
		this.capa = capa;
	}

	/**
	 * @return the ingreso
	 */
	public int getIngreso() {
		return ingreso;
	}

	/**
	 * @param ingreso the ingreso to set
	 */
	public void setIngreso(int ingreso) {
		this.ingreso = ingreso;
	}

	/**
	 * @return the mostrart
	 */
	public int getMostrart() {
		return mostrart;
	}

	/**
	 * @param mostrart the mostrart to set
	 */
	public void setMostrart(int mostrart) {
		this.mostrart = mostrart;
	}

	public String getCalificacion() {
		return calificacion;
	}

	public void setCalificacion(String calificacion) {
		this.calificacion = calificacion;
	}


    
    
}