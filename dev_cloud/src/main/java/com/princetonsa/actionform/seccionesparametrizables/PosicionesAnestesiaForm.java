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

/**
 * 
 * @author wilson
 *
 */
public class PosicionesAnestesiaForm extends ValidatorForm 
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
	private boolean esConsulta;
	
	/**
	 * 
	 */
	private HashMap<Object, Object> mapa;
	
	/**
	 * 
	 */
	private HashMap<Object, Object> mapaTag;

	
	/********DATOS A ACTUALIZAR***********/
	
	/**
	 * 
	 */
	private String fechaInicialActualizada;
	
	/**
	 * 
	 */
	private String horaInicialActualizada;
	
	/**
	 * 
	 */
	private int posicionInstCC;
	
	/**
	 * 
	 */
	private boolean estaBD;
	
	/**
	 * indice para saber cuales signos vitales va ha modificar
	 */
	private int index;
	
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
	 * Indicador ocultar menu 
	 * */
	private boolean ocultarMenu = false;
	
	/**
     * resetea los atributos del form
     *
     */
    public void reset()
    {
    	mapa= new HashMap<Object, Object>();
    	mapa.put("numRegistros", "0");
    	
    	mapaTag= new HashMap<Object, Object>();
    	mapaTag.put("numRegistros", "0");
    	
    	this.fechaInicialActualizada="";
    	this.horaInicialActualizada="";
    	this.estaBD=false;
    	this.index=ConstantesBD.codigoNuncaValido;
    	this.posicionInstCC=ConstantesBD.codigoNuncaValido;
    	
    	if(!this.getEstado().equals("resumen"))
    	{	
	    	this.esSinEncabezado = "";
	    	this.ocultarMenu = false;
    	}	
    	mostrarDatosInfo = new InfoDatosString("","","",false);
    }
    
    /**
     * resetea los atributos del form
     *
     */
    public void resetNuevo()
    {
    	this.fechaInicialActualizada=UtilidadFecha.getFechaActual();
    	this.horaInicialActualizada=UtilidadFecha.getHoraActual();
    	this.estaBD=false;
    	this.index=ConstantesBD.codigoNuncaValido;
    	this.posicionInstCC=ConstantesBD.codigoNuncaValido;
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
    	if(this.posicionInstCC<=0)
    	{
    		errores.add("", new ActionMessage("errors.required", "La posicion"));
    	}
    	
    	if(!UtilidadFecha.esFechaValidaSegunAp(this.getFechaInicialActualizada()))
    	{
    		errores.add("", new ActionMessage("errors.formatoFechaInvalido","Inicial"));
    	}
    	if(!UtilidadFecha.validacionHora(this.getHoraInicialActualizada()).puedoSeguir)
    	{
    		errores.add("", new ActionMessage("errors.formatoHoraInvalido","Inicial"));
    	}
    	
    	//si no existen errores entonces se valida que la fecha - hora de inicio sea >= fecha/hora ingreso a la sala y <= fecha sistema
    	if(errores.isEmpty())
    	{
    		if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(), this.getFechaInicialActualizada(), this.getHoraInicialActualizada()).isTrue())
    		{
    			errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial", "Actual"));
    		}
    		
    		if(errores.isEmpty())
    		{
    			SolicitudesCx solicitudCx = new SolicitudesCx();		
    			solicitudCx.cargarEncabezadoSolicitudCx(this.numeroSolicitud+"");
    			
    			
    			if(!UtilidadTexto.isEmpty(solicitudCx.getFechaIngresoSala()))
    			{	
	    			if(!UtilidadFecha.compararFechas( this.getFechaInicialActualizada(), this.getHoraInicialActualizada(), solicitudCx.getFechaIngresoSala(), solicitudCx.getHoraIngresoSala()).isTrue())
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
	 * @return the fechaInicialActualizada
	 */
	public String getFechaInicialActualizada() {
		return fechaInicialActualizada;
	}

	/**
	 * @param fechaInicialActualizada the fechaInicialActualizada to set
	 */
	public void setFechaInicialActualizada(String fechaInicialActualizada) {
		this.fechaInicialActualizada = fechaInicialActualizada;
	}

	/**
	 * @return the horaInicialActualizada
	 */
	public String getHoraInicialActualizada() {
		return horaInicialActualizada;
	}

	/**
	 * @param horaInicialActualizada the horaInicialActualizada to set
	 */
	public void setHoraInicialActualizada(String horaInicialActualizada) {
		this.horaInicialActualizada = horaInicialActualizada;
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
	 * @return the mapa
	 */
	public HashMap<Object, Object> getMapa() {
		return mapa;
	}

	/**
	 * @param mapa the mapa to set
	 */
	public void setMapa(HashMap<Object, Object> mapa) {
		this.mapa = mapa;
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
	 * @return the mapaTag
	 */
	public HashMap<Object, Object> getMapaTag() {
		return mapaTag;
	}

	/**
	 * @param mapaTag the mapaTag to set
	 */
	public void setMapaTag(HashMap<Object, Object> mapaTag) {
		this.mapaTag = mapaTag;
	}
    
	/**
	 * @return the mapaTag
	 */
	public Object getMapaTag(Object key) {
		return mapaTag.get(key);
	}

	/**
	 * @param mapaTag the mapaTag to set
	 */
	public void setMapaTag(Object key, Object value) {
		this.mapaTag.put(key, value);
	}
    
	/**
	 * @return the mapaTag
	 */
	public Object getMapa(Object key) {
		return mapa.get(key);
	}

	/**
	 * @param mapaTag the mapaTag to set
	 */
	public void setMapa(Object key, Object value) {
		this.mapa.put(key, value);
	}

	/**
	 * @return the posicionInstCC
	 */
	public int getPosicionInstCC() {
		return posicionInstCC;
	}

	/**
	 * @param posicionInstCC the posicionInstCC to set
	 */
	public void setPosicionInstCC(int posicionInstCC) {
		this.posicionInstCC = posicionInstCC;
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

	public InfoDatosString getMostrarDatosInfo(){
		return mostrarDatosInfo;
	}	
	public void setMostrarDatosInfo(InfoDatosString mostrarDatosInfo){
		this.mostrarDatosInfo = mostrarDatosInfo;
	}	
	public void setMostrarDatosInfoActivo(boolean valor){
		this.mostrarDatosInfo.setActivo(valor);
	}
	public boolean getMostrarDatosInfoActivo(){
		return this.mostrarDatosInfo.getActivo();
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
}