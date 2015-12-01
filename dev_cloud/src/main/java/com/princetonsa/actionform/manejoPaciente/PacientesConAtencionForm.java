package com.princetonsa.actionform.manejoPaciente;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ResultadoBoolean;

/**
 * @author Mauricio Jllo
 * Fecha: Junio de 2008
 */

public class PacientesConAtencionForm extends ValidatorForm
{

	private String estado;
	
	/**
     * Mensaje que informa sobre la generacion de la aplicacion de pagos facturas varias
     */
    private ResultadoBoolean mensaje = new ResultadoBoolean(false);
	
    /**
     * HashMap de los Centros de Atencion 
     */
    private HashMap centroAtencion;
    
    /**
     * Codigo del Centro de Atencion seleccionado para realizar el filtrado
     */
    private String codigoCentroAtencion;
	
    /**
     * Carga los datos del select de Convenios
     */
    private ArrayList<HashMap<String, Object>> convenios;
    
    /**
     * Variable que maneja el convenio seleccionado
     */
    private String convenioSeleccionado;
    
    /**
     * Carga las vias de ingreso
     */
    private ArrayList<HashMap<String, Object>> viasIngresos;
    
    /**
     * Codigo de la Via de Ingreso para realizar la busqueda
     */
    private String viaIngreso;
    
    /**
     * HashMap con los datos arrojados por la busqueda
     */
    private HashMap busquedaPacientesConAtencion;
   
    /**
     * Carga las vias de ingreso
     */
    private ArrayList<HashMap<String, Object>> tiposPaciente;
    
    /**
     * Tipo de Paciente para realizar la busqueda
     */
    private String tipoPaciente;
    
    /**
     * HashMap con el tipo de solicitudes
     */
    private HashMap tiposSolicitudes;
    
    /**
	 * String donde se almacena el valor del criterio
	 * de busqueda Estado de la Cama.
	 */
	private String [] tipoSolicitud;
    
    /**
     * Tipo de Salida permite generar Archivo Plano o Impresion
     */
    private String tipoSalida;
    
    /**
	 * Path completo del archivo generado
	 */
	private String pathArchivoTxt;
	
	/**
	 * Controla si se genera el archivo o no?
	 */
	private boolean archivo;
 	
	/**
	 * Sin errores en el validate
	 */
	private boolean errores;
	
	/**
	 * Valida si se genero el archivo .zip para informar al usuario
	 */
	private boolean zip;
    
    /**
     * Metodo que inicializa todas las variables.
     */
    public void reset()
    {
    	this.centroAtencion = new HashMap();
    	this.centroAtencion.put("numRegistros", "0");
    	this.codigoCentroAtencion = "";
    	this.convenios = new ArrayList<HashMap<String,Object>>();
    	this.convenioSeleccionado = "";
    	this.busquedaPacientesConAtencion = new HashMap();
    	this.busquedaPacientesConAtencion.put("numRegistros", "0");
    	this.viasIngresos = new ArrayList<HashMap<String,Object>>();
    	this.viaIngreso = "";
    	this.tiposPaciente = new ArrayList<HashMap<String,Object>>();
    	this.tipoPaciente = "";
    	this.tipoSalida = "";
    	this.tiposSolicitudes = new HashMap();
    	this.tiposSolicitudes.put("numRegistros", "0");
    	this.tipoSolicitud = new String []{""};
    	this.pathArchivoTxt = "";
	 	this.archivo = false;
	 	this.errores = false;
	 	this.zip = false;
    }
    
    /**
     * Validate
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
    	ActionErrors errores = new ActionErrors();
		if(this.estado.equals("buscar"))
		{
			if(this.codigoCentroAtencion.trim().equals("") || this.codigoCentroAtencion.trim().equals("null"))
			{
				errores.add("Centro de Atención", new ActionMessage("errors.required","El Centro de Atención "));
				this.errores = true;
			}
			if(this.convenioSeleccionado.trim().equals("") || this.convenioSeleccionado.trim().equals("null"))
			{
				errores.add("Convenio", new ActionMessage("errors.required","El Convenio "));
				this.errores = true;
			}
			if(this.viaIngreso.trim().equals("") || this.viaIngreso.trim().equals("null"))
			{
				errores.add("Indicativo Egreso", new ActionMessage("errors.required","La Vía de Ingreso "));
				this.errores = true;
			}
			if(this.tipoPaciente.trim().equals("") || this.tipoPaciente.trim().equals("null"))
			{
				errores.add("codigo", new ActionMessage("errors.required","El Tipo de Paciente "));
				this.errores = true;
			}
			if(this.tipoSalida.trim().equals("") || this.tipoSalida.trim().equals("null"))
			{
				errores.add("codigo", new ActionMessage("errors.required","El Tipo de Salida "));
				this.errores = true;
			}
			if(errores.isEmpty())
				this.errores = false;
		}
    	return errores;
	}
    
	/**
	 * @return the centroAtencion
	 */
	public HashMap getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(HashMap centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @param key
	 * @return
	 */	
	public Object getCentroAtencion(String key) 
	{
		return centroAtencion.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setCentroAtencion(String key, Object value) 
	{
		this.centroAtencion.put(key, value);
	}
	
	/**
	 * @return the codigoCentroAtencion
	 */
	public String getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}

	/**
	 * @param codigoCentroAtencion the codigoCentroAtencion to set
	 */
	public void setCodigoCentroAtencion(String codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}

	/**
	 * @return the convenios
	 */
	public ArrayList<HashMap<String, Object>> getConvenios() {
		return convenios;
	}

	/**
	 * @param convenios the convenios to set
	 */
	public void setConvenios(ArrayList<HashMap<String, Object>> convenios) {
		this.convenios = convenios;
	}

	/**
	 * @return the convenioSeleccionado
	 */
	public String getConvenioSeleccionado() {
		return convenioSeleccionado;
	}

	/**
	 * @param convenioSeleccionado the convenioSeleccionado to set
	 */
	public void setConvenioSeleccionado(String convenioSeleccionado) {
		this.convenioSeleccionado = convenioSeleccionado;
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
	 * @return the busquedaPacientesConAtencion
	 */
	public HashMap getBusquedaPacientesConAtencion() {
		return busquedaPacientesConAtencion;
	}

	/**
	 * @param busquedaPacientesConAtencion the busquedaPacientesConAtencion to set
	 */
	public void setBusquedaPacientesConAtencion(HashMap busquedaPacientesConAtencion) {
		this.busquedaPacientesConAtencion = busquedaPacientesConAtencion;
	}

	/**
	 * @param key
	 * @return
	 */	
	public Object getBusquedaPacientesConAtencion(String key) 
	{
		return busquedaPacientesConAtencion.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setBusquedaPacientesConAtencion(String key, Object value) 
	{
		this.busquedaPacientesConAtencion.put(key, value);
	}
	
	/**
	 * @return the viaIngreso
	 */
	public String getViaIngreso() {
		return viaIngreso;
	}

	/**
	 * @param viaIngreso the viaIngreso to set
	 */
	public void setViaIngreso(String viaIngreso) {
		this.viaIngreso = viaIngreso;
	}

	/**
	 * @return the tipoPaciente
	 */
	public String getTipoPaciente() {
		return tipoPaciente;
	}

	/**
	 * @param tipoPaciente the tipoPaciente to set
	 */
	public void setTipoPaciente(String tipoPaciente) {
		this.tipoPaciente = tipoPaciente;
	}

	/**
	 * @return the tipoSalida
	 */
	public String getTipoSalida() {
		return tipoSalida;
	}

	/**
	 * @param tipoSalida the tipoSalida to set
	 */
	public void setTipoSalida(String tipoSalida) {
		this.tipoSalida = tipoSalida;
	}

	/**
	 * @return
	 */
	public String[] getTipoSolicitud() {
		return tipoSolicitud;
	}

	/**
	 * @param tipoSolicitud
	 */
	public void setTipoSolicitud(String[] tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}

	/**
	 * @return the viasIngresos
	 */
	public ArrayList<HashMap<String, Object>> getViasIngresos() {
		return viasIngresos;
	}

	/**
	 * @param viasIngresos the viasIngresos to set
	 */
	public void setViasIngresos(ArrayList<HashMap<String, Object>> viasIngresos) {
		this.viasIngresos = viasIngresos;
	}

	/**
	 * @return the tiposPaciente
	 */
	public ArrayList<HashMap<String, Object>> getTiposPaciente() {
		return tiposPaciente;
	}

	/**
	 * @param tiposPaciente the tiposPaciente to set
	 */
	public void setTiposPaciente(ArrayList<HashMap<String, Object>> tiposPaciente) {
		this.tiposPaciente = tiposPaciente;
	}

	/**
	 * @return the tiposSolicitudes
	 */
	public HashMap getTiposSolicitudes() {
		return tiposSolicitudes;
	}

	/**
	 * @param tiposSolicitudes the tiposSolicitudes to set
	 */
	public void setTiposSolicitudes(HashMap tiposSolicitudes) {
		this.tiposSolicitudes = tiposSolicitudes;
	}
	
	/**
	 * @param key
	 * @return
	 */	
	public Object getTiposSolicitudes(String key) 
	{
		return tiposSolicitudes.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setTiposSolicitudes(String key, Object value) 
	{
		this.tiposSolicitudes.put(key, value);
	}

	/**
	 * @return the archivo
	 */
	public boolean isArchivo() {
		return archivo;
	}

	/**
	 * @param archivo the archivo to set
	 */
	public void setArchivo(boolean archivo) {
		this.archivo = archivo;
	}

	/**
	 * @return the errores
	 */
	public boolean isErrores() {
		return errores;
	}

	/**
	 * @param errores the errores to set
	 */
	public void setErrores(boolean errores) {
		this.errores = errores;
	}

	/**
	 * @return the pathArchivoTxt
	 */
	public String getPathArchivoTxt() {
		return pathArchivoTxt;
	}

	/**
	 * @param pathArchivoTxt the pathArchivoTxt to set
	 */
	public void setPathArchivoTxt(String pathArchivoTxt) {
		this.pathArchivoTxt = pathArchivoTxt;
	}

	/**
	 * @return the zip
	 */
	public boolean isZip() {
		return zip;
	}

	/**
	 * @param zip the zip to set
	 */
	public void setZip(boolean zip) {
		this.zip = zip;
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
	
}