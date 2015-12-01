package com.princetonsa.actionform.manejoPaciente;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * Fecha: Febrero - 2008
 * @author Mauricio Jaramillo
 *
 */

public class TotalOcupacionCamasForm extends ValidatorForm

{

	private String estado="";
	
	/**
     * HashMap de los Centros de Atencion 
     */
    private HashMap centroAtencion;
	
	/**
	 * Centro de Atencion
	 */
	private String codigoCentroAtencion;
	
	/**
	 * Incluir Camas de Urgencias
	 */
	private String incluirCamas;
	
	/**
	 * Mapa para listar el resultado de la Consulta
	 */
	private HashMap<String, Object> estadosCama;

	/**
	 * Mapa para listar los estadosCama de la tabla estados_cama
	 */
	private HashMap<String, Object> inicialMap;
	
	/** 
	 * Numero de Check Box Seleccionados
	 */
	private int numCheck;
	
    /**
     * Maneja el tipo de salida que se desea ejecutar (Imprimir y Archivo Plano)
     */
    private String tipoSalida;
	
	/**
	 * Mensaje generacion del archivo CSV
	 */
	private ResultadoBoolean mensaje = new ResultadoBoolean(false);
	
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
	 * Atributo que le indica a la vista si se
	 * genero el archivo plano
	 */
	private boolean operacionTrue;
	
	/**
	 * Atributo que indica donde se almaceno el archivo,
	 * este es para mostrar la ruta excata donde se genero
	 * el archivo dentro del sistema de directorios del
	 * servidor 
	 */
	private String ruta;
	
	/**
	 * atributo que indica la direccion para poder
	 * descargar el archivo
	 */
	private String urlArchivo;
	
	/**
	 * Atributo que almacena si el archivo
	 * .ZIP si se genero
	 */
	private boolean existeArchivo=false; 
	
	
	public boolean isOperacionTrue() {
		return operacionTrue;
	}

	public void setOperacionTrue(boolean operacionTrue) {
		this.operacionTrue = operacionTrue;
	}

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public String getUrlArchivo() {
		return urlArchivo;
	}

	public void setUrlArchivo(String urlArchivo) {
		this.urlArchivo = urlArchivo;
	}

	public boolean isExisteArchivo() {
		return existeArchivo;
	}

	public void setExisteArchivo(boolean existeArchivo) {
		this.existeArchivo = existeArchivo;
	}

	private HashMap datosConsulta = new HashMap ();
	
	private boolean noDatos = false;
	
	public boolean isNoDatos() {
		return noDatos;
	}

	public void setNoDatos(boolean noDatos) {
		this.noDatos = noDatos;
	}

	/**
	 *  Metodo Resetea los atributos del form.
	 *
	 */
	public void reset()
	{
		this.centroAtencion = new HashMap();
    	this.centroAtencion.put("numRegistros", "0");
    	this.codigoCentroAtencion = "";
    	this.incluirCamas = ConstantesBD.acronimoNo;
		this.estadosCama = new HashMap<String, Object>();
		this.numCheck = ConstantesBD.codigoNuncaValido;
		this.tipoSalida = "";
		this.pathArchivoTxt = "";
	 	this.archivo = false;
	 	this.errores = false;
	 	this.zip = false;
	 	this.datosConsulta = new HashMap ();
	 	this.setDatosConsulta("numRegistros", 0);
	 	this.noDatos=false;
	 	this.operacionTrue=false;
		this.ruta="";
		this.urlArchivo="";
	}

	/**
	 * Control de Errores (Validaciones)
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
			
		if(this.estado.equals("generar"))
		{
			boolean estadoRequerido=true;
			if(this.centroAtencion==null || this.centroAtencion.equals(""))
				errores.add("centroAtencion", new ActionMessage("errors.required", "El Centro de Atención "));
			
			if(this.tipoSalida.trim().equals("") || this.tipoSalida.trim().equals("null"))
				errores.add("tipoSalida", new ActionMessage("errors.required", "El Tipo de Salida "));
			
			int numReg = Utilidades.convertirAEntero(this.inicialMap.get("numRegistros")+"");
			
			for (int i=0;i<numReg;i++)
				if (UtilidadTexto.getBoolean(this.inicialMap.get("checkcodigo_"+i)+""))
					estadoRequerido=false;
			
			if (estadoRequerido)
				errores.add("tipoSalida", new ActionMessage("errors.required", "Seleccionar al menos un estado de la cama "));
				
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
	 * 
	 * @return
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * 
	 * @param estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getEstadosCama() {
		return estadosCama;
	}

	/**
	 * 
	 * @param estadosCama
	 */
	public void setEstadosCama(HashMap<String, Object> estadosCama) {
		this.estadosCama = estadosCama;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getEstadosCama(String key) {
		return estadosCama.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setEstadosCama(String key,Object value) {
		this.estadosCama.put(key, value);
	}

	/**
	 * 
	 * @return
	 */
	public String getIncluirCamas() {
		return incluirCamas;
	}

	/**
	 * 
	 * @param incluirCamas
	 */
	public void setIncluirCamas(String incluirCamas) {
		this.incluirCamas = incluirCamas;
	}

	/**
	 * 
	 * @return
	 */
	public int getNumCheck() {
		return numCheck;
	}

	/**
	 * 
	 * @param numCheck
	 */
	public void setNumCheck(int numCheck) {
		this.numCheck = numCheck;
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getInicialMap() {
		return inicialMap;
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getInicialMap(String key) {
		return inicialMap.get(key);
	}
	
	/**
	 * 
	 * @param inicialMap
	 */
	public void setInicialMap(HashMap<String, Object> inicialMap) {
		this.inicialMap = inicialMap;
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setInicialMap(String key,Object value) {
		this.inicialMap.put(key, value);
	}

	/**
	 * @return
	 */
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje
	 */
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * @return
	 */
	public boolean isArchivo() {
		return archivo;
	}

	/**
	 * @param archivo
	 */
	public void setArchivo(boolean archivo) {
		this.archivo = archivo;
	}

	/**
	 * @return
	 */
	public boolean isErrores() {
		return errores;
	}

	/**
	 * @param errores
	 */
	public void setErrores(boolean errores) {
		this.errores = errores;
	}

	/**
	 * @return
	 */
	public String getPathArchivoTxt() {
		return pathArchivoTxt;
	}

	/**
	 * @param pathArchivoTxt
	 */
	public void setPathArchivoTxt(String pathArchivoTxt) {
		this.pathArchivoTxt = pathArchivoTxt;
	}

	/**
	 * @return
	 */
	public String getTipoSalida() {
		return tipoSalida;
	}

	/**
	 * @param tipoSalida
	 */
	public void setTipoSalida(String tipoSalida) {
		this.tipoSalida = tipoSalida;
	}

	/**
	 * @return
	 */
	public boolean isZip() {
		return zip;
	}

	/**
	 * @param zip
	 */
	public void setZip(boolean zip) {
		this.zip = zip;
	}

	public HashMap getDatosConsulta() {
		return datosConsulta;
	}

	public void setDatosConsulta(HashMap datosConsulta) {
		this.datosConsulta = datosConsulta;
	}
	
	public Object  getDatosConsulta(String key) {
		return datosConsulta.get(key);
	}

	public void setDatosConsulta(String key,Object value) {
		this.datosConsulta.put(key, value);
	}
	
}