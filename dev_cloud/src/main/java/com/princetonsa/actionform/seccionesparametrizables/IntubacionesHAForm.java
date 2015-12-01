package com.princetonsa.actionform.seccionesparametrizables;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.InfoDatosString;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.mundo.ordenesmedicas.cirugias.SolicitudesCx;
import com.princetonsa.mundo.parametrizacion.IntubacionesHA;

/**
 * 
 * @author wilson
 *
 */
public class IntubacionesHAForm extends ValidatorForm  
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * 
	 */
	private int numeroSolicitud;
	
	/**
	 * 
	 * */
	private String codigoPeticion;
	
	/**
	 * 
	 */
	private int centroCosto;
	
	/**
	 * 
	 */
	private HashMap<Object, Object> mapaTiposIntubacionMultiple;
	
	/**
	 * 
	 */
	private HashMap<Object,Object> mapaCormack;
	
	/**
	 * 
	 */
	private HashMap<Object, Object> mapaListado;
	
	/**
	 * 
	 */
	private boolean esConsulta;
	
	/**
	 * 
	 */
	private boolean estaBD;
	
	/**
	 * 
	 */
	private String fecha;
	
	/**
	 * 
	 */
	private String hora;
	
	/**
	 * facil - dificil
	 */
	private int tipoIntubacion;
	
	/**
	 * grado I - IV
	 */
	private int clasificacionCormack;
	
	/**
	 * 
	 */
	private int index;
	
	/**
	 * 
	 */
	private int codigoIntubacionHA;
		
	//Campos Funcionalidad Dummy
	
	/**
	 * String esSinEncabezado
	 * */
	private String esSinEncabezado;
	
	/**
	 * Indicador para mostrar informacion solo cuando posea datos
	 * */
	private InfoDatosString mostrarDatosInfo = new InfoDatosString("","","",false);
	
	/**
	 * Indicador de ocultar menu
	 * */
	private boolean ocultarMenu = false;
	
	
	/**
     * resetea los atributos del form
     *
     */
    public void reset()
    {
    	this.mapaTiposIntubacionMultiple= new HashMap<Object, Object>();
    	this.mapaTiposIntubacionMultiple.put("numRegistros", "0");
    	this.mapaListado= new HashMap<Object, Object>();
    	this.mapaListado.put("numRegistros", "0");
    	this.mapaCormack= new HashMap<Object, Object>();
    	this.mapaCormack.put("numRegistros", "0");
    	
    	this.estaBD=false;
    	this.fecha=UtilidadFecha.getFechaActual();
    	this.hora=UtilidadFecha.getHoraActual();
    	this.index=ConstantesBD.codigoNuncaValido;
    	this.tipoIntubacion=ConstantesBD.codigoNuncaValido;
    	this.codigoIntubacionHA=ConstantesBD.codigoNuncaValido;
    	this.clasificacionCormack=ConstantesBD.codigoNuncaValido;
    	
    	mostrarDatosInfo = new InfoDatosString("","","",false);
    	
    	if(!this.estado.equals("resumen"))
    	{
    		this.esSinEncabezado="";
    		this.ocultarMenu=false;
    	}	
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
        
        
        if(estado.equals("guardar"))
        {
        	errores=validacion(errores);
        }	
        
        if(!errores.isEmpty())
    	{
    		if(estado.equals("guardar") && !this.getEstaBD())	
    			this.estado="nuevo";
    		else if(estado.equals("guardar") && this.getEstaBD())	
    			this.estado="modificar";
    	}
        return errores;
    }
    
    /**
     * 
     * @param errores
     * @return
     */
    public ActionErrors validacion(ActionErrors errores)
    {
    	if(!UtilidadFecha.esFechaValidaSegunAp(this.getFecha()))
    	{
    		errores.add("", new ActionMessage("errors.formatoFechaInvalido","Inicial"));
    	}
    	if(!UtilidadFecha.validacionHora(this.getHora()).puedoSeguir)
    	{
    		errores.add("", new ActionMessage("errors.formatoHoraInvalido","Inicial"));
    	}
    	
    	//si no existen errores entonces se valida que la fecha - hora de inicio sea >= fecha/hora ingreso a la sala y <= fecha sistema
    	if(errores.isEmpty())
    	{
    		if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(), this.getFecha(), this.getHora()).isTrue())
    		{
    			errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial", "Actual"));
    		}
    		if(errores.isEmpty())
    		{
    			SolicitudesCx solicitudCx = new SolicitudesCx();		
    			solicitudCx.cargarEncabezadoSolicitudCx(this.numeroSolicitud+"");
    			
    			
    			if(!UtilidadTexto.isEmpty(solicitudCx.getFechaIngresoSala()))
    			{	
	    			if(!UtilidadFecha.compararFechas( this.getFecha(), this.getHora(), solicitudCx.getFechaIngresoSala(), solicitudCx.getHoraIngresoSala()).isTrue())
	        		{
	    				errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "Inicial", "Ingreso Sala "+solicitudCx.getFechaIngresoSala()+" "+solicitudCx.getHoraIngresoSala()));
	        		}
    			}
    			else
    			{
    				errores.add("", new ActionMessage("errors.required", "La fecha/hora ingreso sala"));
    			}
    		}
    	}
    	
    	if(errores.isEmpty())
    	{
    		if(IntubacionesHA.existeIntubacionFechaHora(this.fecha, this.hora, this.codigoIntubacionHA)) 
    		{
    			errores.add("", new ActionMessage("errors.yaExiste", "La intubacion con fecha y hora "+this.getFecha()+" "+this.getHora()));
    		}
    	}
    	
    	//tipo dispositivo es requerido
    	if(this.tipoIntubacion<=0)
    	{
    		errores.add("", new ActionMessage("errors.required", "El tipo intubacion"));
    	}
    	
    	if(this.clasificacionCormack<=0)
    	{
    		errores.add("", new ActionMessage("errors.required", "La clasificacion Cormack"));
    	}
    	return errores;
    }

	/**
	 * @return the centroCosto
	 */
	public int getCentroCosto() {
		return centroCosto;
	}

	/**
	 * @param centroCosto the centroCosto to set
	 */
	public void setCentroCosto(int centroCosto) {
		this.centroCosto = centroCosto;
	}

	/**
	 * @return the estaBD
	 */
	public boolean getEstaBD() {
		return estaBD;
	}

	/**
	 * @param estaBD the estaBD to set
	 */
	public void setEstaBD(boolean estaBD) {
		this.estaBD = estaBD;
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
	 * @return the fecha
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return the hora
	 */
	public String getHora() {
		return hora;
	}

	/**
	 * @param hora the hora to set
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}

	/**
	 * @return the mapa
	 */
	public HashMap<Object, Object> getMapaTiposIntubacionMultiple() {
		return mapaTiposIntubacionMultiple;
	}

	/**
	 * @param mapa the mapa to set
	 */
	public void setMapaTiposIntubacionMultiple(HashMap<Object, Object> mapa) {
		this.mapaTiposIntubacionMultiple = mapa;
	}

	/**
	 * @return the mapa
	 */
	public Object getMapaTiposIntubacionMultiple(Object key) 
	{	
		return mapaTiposIntubacionMultiple.get(key);
	}

	/**
	 * @param mapa the mapa to set
	 */
	public void setMapaTiposIntubacionMultiple(Object key, Object value) {
		this.mapaTiposIntubacionMultiple.put(key, value);
	}

	
	
	/**
	 * @return the numeroSolicitud
	 */
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * @param numeroSolicitud the numeroSolicitud to set
	 */
	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	/**
	 * @return the esConsulta
	 */
	public boolean getEsConsulta() {
		return esConsulta;
	}

	/**
	 * @param esConsulta the esConsulta to set
	 */
	public void setEsConsulta(boolean esConsulta) {
		this.esConsulta = esConsulta;
	}

	/**
	 * @return the mapaListado
	 */
	public HashMap<Object, Object> getMapaListado() {
		return mapaListado;
	}

	/**
	 * @param mapaListado the mapaListado to set
	 */
	public void setMapaListado(HashMap<Object, Object> mapaListado) {
		this.mapaListado = mapaListado;
	}
    
	/**
	 * @return the mapaListado
	 */
	public Object getMapaListado(Object key) {
		return mapaListado.get(key);
	}

	/**
	 * @param mapaListado the mapaListado to set
	 */
	public void setMapaListado(Object key, Object value) {
		this.mapaListado.put(key, value);
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the clasificacionCormack
	 */
	public int getClasificacionCormack() {
		return clasificacionCormack;
	}

	/**
	 * @param clasificacionCormack the clasificacionCormack to set
	 */
	public void setClasificacionCormack(int clasificacionCormack) {
		this.clasificacionCormack = clasificacionCormack;
	}

	/**
	 * @return the codigoIntubacionHA
	 */
	public int getCodigoIntubacionHA() {
		return codigoIntubacionHA;
	}

	/**
	 * @param codigoIntubacionHA the codigoIntubacionHA to set
	 */
	public void setCodigoIntubacionHA(int codigoIntubacionHA) {
		this.codigoIntubacionHA = codigoIntubacionHA;
	}

	/**
	 * @return the tipoIntubacion
	 */
	public int getTipoIntubacion() {
		return tipoIntubacion;
	}

	/**
	 * @param tipoIntubacion the tipoIntubacion to set
	 */
	public void setTipoIntubacion(int tipoIntubacion) {
		this.tipoIntubacion = tipoIntubacion;
	}

	/**
	 * @return the mapaCormack
	 */
	public HashMap<Object, Object> getMapaCormack() {
		return mapaCormack;
	}

	/**
	 * @param mapaCormack the mapaCormack to set
	 */
	public void setMapaCormack(HashMap<Object, Object> mapaCormack) {
		this.mapaCormack = mapaCormack;
	}

	/**
	 * @return the mapaCormack
	 */
	public Object getMapaCormack(Object key) {
		return mapaCormack.get(key);
	}

	/**
	 * @param mapaCormack the mapaCormack to set
	 */
	public void setMapaCormack(Object key, Object value) {
		this.mapaCormack.put(key, value);
	}

	/**
	 * @return the codigoPeticion
	 */
	public String getCodigoPeticion() {
		return codigoPeticion;
	}

	/**
	 * @param codigoPeticion the codigoPeticion to set
	 */
	public void setCodigoPeticion(String codigoPeticion) {
		this.codigoPeticion = codigoPeticion;
	}

	/**
	 * @return the esSinEncabezado
	 */
	public String getEsSinEncabezado() {
		return esSinEncabezado;
	}

	/**
	 * @param esSinEncabezado the esSinEncabezado to set
	 */
	public void setEsSinEncabezado(String esSinEncabezado) {
		this.esSinEncabezado = esSinEncabezado;
	}

	/**
	 * @return the mostrarDatosInfo
	 */
	public InfoDatosString getMostrarDatosInfo() {
		return mostrarDatosInfo;
	}

	/**
	 * @param mostrarDatosInfo the mostrarDatosInfo to set
	 */
	public void setMostrarDatosInfo(InfoDatosString mostrarDatosInfo) {
		this.mostrarDatosInfo = mostrarDatosInfo;
	}

	/**
	 * @return the ocultarMenu
	 */
	public boolean isOcultarMenu() {
		return ocultarMenu;
	}

	/**
	 * @param ocultarMenu the ocultarMenu to set
	 */
	public void setOcultarMenu(boolean ocultarMenu) {
		this.ocultarMenu = ocultarMenu;
	}	
		
	public void setMostrarDatosInfoActivo(boolean valor){
		this.mostrarDatosInfo.setActivo(valor);
	}
	public boolean getMostrarDatosInfoActivo(){
		return this.mostrarDatosInfo.getActivo();
	}
}