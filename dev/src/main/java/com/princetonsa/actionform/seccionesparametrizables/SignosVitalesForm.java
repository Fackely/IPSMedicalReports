package com.princetonsa.actionform.seccionesparametrizables;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.mundo.parametrizacion.SignosVitales;

import util.ConstantesBD;
import util.InfoDatosString;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * 
 * @author wilson
 *
 */
public class SignosVitalesForm extends ValidatorForm
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
	 * mapa con los signos vitales
	 */
	private HashMap<Object, Object> mapaSignosVitales;
	
	/**
	 * mapa con la informacion del nuevo o modificado signo vital
	 */
	private HashMap<Object, Object> mapaSignoVitalActualizado; 
	
	/**
	 * atributo que indica si se debe o no insertar
	 */
	private boolean esConsulta;
	
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
	 * 
	 */
	private boolean existeFechaHoraIngresoSala ;
	
	/**
     * resetea los atributos del form
     *
     */
    public void reset()
    {
    	mapaSignosVitales= new HashMap<Object, Object>();
    	mapaSignosVitales.put("numRegistros", "0");
    	
    	mapaSignoVitalActualizado= new HashMap<Object, Object>();
    	//no se resetea el numero solicitud centro costo, mirar get
    	this.index=ConstantesBD.codigoNuncaValido;
    	if(!this.estado.equals("resumen"))
    	{	
	    	this.esSinEncabezado = "";
	    	this.ocultarMenu = false;
    	}	
    	this.mostrarDatosInfo = new InfoDatosString("","","",false);    
    	this.existeFechaHoraIngresoSala=true;
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
        
        if(estado.equals("guardarNuevoSignoVitalAnestesia") || estado.equals("guardarModificarSignoVitalAnestesia"))
        {
        	if(!UtilidadFecha.esFechaValidaSegunAp(mapaSignoVitalActualizado.get("fecha")+""))
        	{
        		errores.add("", new ActionMessage("errors.formatoFechaInvalido",""));
        	}
        	if(!UtilidadFecha.validacionHora(mapaSignoVitalActualizado.get("hora")+"").puedoSeguir)
        	{
        		errores.add("", new ActionMessage("errors.formatoHoraInvalido",""));
        	}
        	
        	//si estan bien la hora y la fecha y esta insertando uno nuevo entonces debemos evaluar que no exista ya ese tiempo en bd
        	if(errores.isEmpty())
        	{
        		int codigoTiempo=ConstantesBD.codigoNuncaValido;
        		if(estado.equals("guardarModificarSignoVitalAnestesia"))
        			codigoTiempo= Integer.parseInt(mapaSignoVitalActualizado.get("codigo_tiempo").toString());
        		
        		if(SignosVitales.existeTiempoSignoVitalAnestesia(mapaSignoVitalActualizado.get("fecha").toString(), mapaSignoVitalActualizado.get("hora").toString(), this.numeroSolicitud, codigoTiempo))
        			errores.add("", new ActionMessage("errors.yaExiste", "La fecha "+mapaSignoVitalActualizado.get("fecha")+" y hora "+mapaSignoVitalActualizado.get("hora")));
        	}
        	
        	for(int w=0; w<Integer.parseInt(this.mapaSignoVitalActualizado.get("numSignosVitales")+""); w++)
        	{
        		if(UtilidadTexto.isEmpty(this.mapaSignoVitalActualizado.get("formula_"+w).toString()))
        		{
	        		//primero verificamos que sea o no requerido
	        		if(UtilidadTexto.getBoolean(this.mapaSignoVitalActualizado.get("es_requerido_"+w)+""))
	        		{
	        			if(UtilidadTexto.isEmpty(this.mapaSignoVitalActualizado.get("valor_"+w)+""))
	        			{
	        				errores.add("", new ActionMessage("errors.required", this.mapaSignoVitalActualizado.get("nombre_"+w)+""));
	        			}
	        		}
	        		
	        		if(!UtilidadTexto.isEmpty(this.mapaSignoVitalActualizado.get("valor_"+w)+""))
	        		{
	    				//debe ser numerico
	    				if(!UtilidadTexto.isNumber(this.mapaSignoVitalActualizado.get("valor_"+w)+""))
	    				{
	    					errores.add("", new ActionMessage("errors.integer", this.mapaSignoVitalActualizado.get("nombre_"+w)+""));
	    				}
	    				else
	    				{
	    					//debe estar en el rango
	    					if(!UtilidadTexto.isEmpty(this.mapaSignoVitalActualizado.get("valor_maximo_"+w)+"")
	    		        		&& !UtilidadTexto.isEmpty(this.mapaSignoVitalActualizado.get("valor_minimo_"+w)+"")
	    		        		&& UtilidadTexto.isNumber(this.mapaSignoVitalActualizado.get("valor_maximo_"+w)+"")
	    		        		&& UtilidadTexto.isNumber(this.mapaSignoVitalActualizado.get("valor_minimo_"+w)+""))
			        		{
			        			int valorMaximo= Integer.parseInt(this.mapaSignoVitalActualizado.get("valor_maximo_"+w)+"");
			        			int valorMinimo= Integer.parseInt(this.mapaSignoVitalActualizado.get("valor_minimo_"+w)+"");
			        			int valor= Utilidades.convertirAEntero(this.mapaSignoVitalActualizado.get("valor_"+w)+"");
			        			
			        			if(valor<valorMinimo || valor>valorMaximo)
			        			{
			        				errores.add("", new ActionMessage("errors.range", this.mapaSignoVitalActualizado.get("nombre_"+w)+"", valorMinimo, valorMaximo));
			        			}
			        		}
	    				}
	        		}
        		}	
        	}
        	
        	if(!errores.isEmpty())
        	{
        		if(estado.equals("guardarNuevoSignoVitalAnestesia"))	
        			this.estado="nuevoSignoVitalAnestesia";
        		else if(estado.equals("guardarModificarSignoVitalAnestesia"))
        			this.estado="modificarSignoVitalAnestesia";
        	}	
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

	/**
	 * @return the mapaSignosVitales
	 */
	public HashMap<Object, Object> getMapaSignosVitales() {
		return mapaSignosVitales;
	}

	/**
	 * @return the mapaSignosVitales
	 */
	public HashMap<Object, Object> getMapaTitulosSignosVitales() {
		return  (HashMap<Object, Object>)((HashMap<Object, Object>)mapaSignosVitales.get("TITULOS")).clone();
	}
	
	/**
	 * @return the mapaSignosVitales
	 */
	public Object getMapaTitulosSignosVitales(Object key) {
		return  this.getMapaTitulosSignosVitales().get(key);
	}
	
	/**
	 * @return the mapaSignosVitales
	 */
	public HashMap<Object, Object> getMapaCamposSignosVitales() {
		return  (HashMap<Object, Object>)((HashMap<Object, Object>)mapaSignosVitales.get("CAMPOS")).clone();
	}
	
	/**
	 * @return the mapaSignosVitales
	 */
	public Object getMapaCamposSignosVitales(Object key) {
		return  this.getMapaCamposSignosVitales().get(key);
	}
	
	/**
	 * @param mapaSignosVitales the mapaSignosVitales to set
	 */
	public void setMapaSignosVitales(HashMap<Object, Object> mapaSignosVitales) {
		this.mapaSignosVitales = mapaSignosVitales;
	}

	/**
	 * @return the centroCosto
	 */
	public int getCentroCosto() 
	{
		if(UtilidadTexto.isEmpty(centroCosto+""))
			return ConstantesBD.codigoNuncaValido;
		return centroCosto;
	}

	/**
	 * @param centroCosto the centroCosto to set
	 */
	public void setCentroCosto(int centroCosto) {
		this.centroCosto = centroCosto;
	}

	/**
	 * @return the numeroSolicitud
	 */
	public int getNumeroSolicitud() 
	{
		if(UtilidadTexto.isEmpty(numeroSolicitud+""))
			return ConstantesBD.codigoNuncaValido;
		return numeroSolicitud;
	}

	/**
	 * @param numeroSolicitud the numeroSolicitud to set
	 */
	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}
    
	/**
	 * @return the mapaSignosVitales
	 */
	public Object getMapaSignosVitales(Object key) {
		return mapaSignosVitales.get(key);
	}

	/**
	 * @param mapaSignosVitales the mapaSignosVitales to set
	 */
	public void setMapaSignosVitales(Object key, Object value) {
		this.mapaSignosVitales.put(key, value);
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
	 * @return the mapaSignoVitalActualizado
	 */
	public HashMap<Object, Object> getMapaSignoVitalActualizado() {
		return mapaSignoVitalActualizado;
	}

	/**
	 * @param mapaSignoVitalActualizado the mapaSignoVitalActualizado to set
	 */
	public void setMapaSignoVitalActualizado(
			HashMap<Object, Object> mapaSignoVitalActualizado) {
		this.mapaSignoVitalActualizado = mapaSignoVitalActualizado;
	}
	
	/**
	 * @return the mapaSignoVitalActualizado
	 */
	public Object getMapaSignoVitalActualizado(Object key) {
		return mapaSignoVitalActualizado.get(key);
	}

	/**
	 * @param mapaSignoVitalActualizado the mapaSignoVitalActualizado to set
	 */
	public void setMapaSignoVitalActualizado(Object key, Object value) {
		this.mapaSignoVitalActualizado.put(key, value);
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

	/**
	 * @return the existeFechaHoraIngresoSala
	 */
	public boolean isExisteFechaHoraIngresoSala() {
		return existeFechaHoraIngresoSala;
	}

	/**
	 * @param existeFechaHoraIngresoSala the existeFechaHoraIngresoSala to set
	 */
	public void setExisteFechaHoraIngresoSala(boolean existeFechaHoraIngresoSala) {
		this.existeFechaHoraIngresoSala = existeFechaHoraIngresoSala;
	}
}