package com.princetonsa.actionform.inventarios;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ResultadoBoolean;

/**
 * @author Mauricio Jaramillo H.
 * Fecha: Agosto de 2008
 */

public class ConsultaCostoArticulosForm extends ValidatorForm
{

	private String estado;
	
	/**
     * Variable para manejar el mensaje
     */
    private ResultadoBoolean mensaje = new ResultadoBoolean(false);
	
    /**
     * HashMap de los Centros de Atencion 
     */
    private HashMap centroAtencion;
    
    /**
     * Codigo del Centro de Atencion seleccionado para realizar el filtro
     */
    private String codigoCentroAtencion;
	
    /**
     * HashMap de los Almacenes
     */
    private HashMap almacenes;
    
    /**
     * Codigo del Almacen seleccionado para realizar el filtrado
     */
    private String almacen;
	
    /**
     * String para almacenar el código de la clase seleccionada
     */
    private String clase;
    
    /**
     * String para almacenar el nombre de la clase seleccionada
     */
    private String descripcionClase;
    
    /**
     * String para almacenar el código del grupo seleccionado
     */
    private String grupo;
    
    /**
     * String para almacenar el nombre del grupo seleccionado
     */
    private String descripcionGrupo;
    
    /**
     * String para almacenar el código del subgrupo seleccionado
     */
    private String subGrupo;
    
    /**
     * String para almacenar el nombre del subgrupo seleccionado
     */
    private String descripcionSubGrupo;
	
    /**
     * String para almacenar el codigo del articulo buscado por la busqueda generica
     */
    private String codArticulo;
    
    /**
     * String para almacenar la descripción del articulo buscado por la busqueda generica
     */
    private String desArticulo;
    
    /**
	 * Tipo de codigo de articulo seleccionado
	 */
	private String tipoCodigoArticulo;
	
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
	 * HashMap para almacenar los datos arrojados por la Consulta de Costo de Artículos
	 */
	private HashMap consultaCostoArticulos;
	
	/**
	 * Metodo reset
	 */
	public void reset()
	{
		this.centroAtencion = new HashMap();
    	this.centroAtencion.put("numRegistros", "0");
    	this.codigoCentroAtencion = "";
    	this.almacenes = new HashMap();
    	this.almacenes.put("numRegistros", "0");
    	this.almacen = "";
    	this.clase = "";
        this.descripcionClase = "";
        this.grupo = "";
        this.descripcionGrupo = "";
        this.subGrupo = "";
        this.descripcionSubGrupo = "";
        this.codArticulo = "";
        this.desArticulo = "";
        this.tipoCodigoArticulo = "";
    	this.tipoSalida = "";
    	this.pathArchivoTxt = "";
	 	this.archivo = false;
	 	this.errores = false;
	 	this.zip = false;
	 	this.consultaCostoArticulos = new HashMap();
    	this.consultaCostoArticulos.put("numRegistros", "0");
	}
	
	/**
	* Control de Errores (Validaciones)
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		if(this.estado.equals("generar"))
		{
			//Validamos los campos requeridos
			if(this.codigoCentroAtencion.trim().equals("") || this.codigoCentroAtencion.trim().equals("null"))
			{
				errores.add("centroAtencion", new ActionMessage("errors.required", "El Centro de Atención "));
				this.errores = true;
			}
			if(this.tipoSalida.trim().equals("") || this.tipoSalida.trim().equals("null"))
			{
				errores.add("tipoSalida", new ActionMessage("errors.required", "El Tipo de Salida "));
				this.errores = true;
			}
			if(this.tipoCodigoArticulo.trim().equals("") || this.tipoCodigoArticulo.trim().equals("null"))
			{
				errores.add("tipoCodigoArticulo", new ActionMessage("errors.required", "El Tipo Código Artículo "));
				this.errores = true;
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
	public Object getCentroAtencion(String key){
		return centroAtencion.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setCentroAtencion(String key, Object value){
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
	 * @return the almacenes
	 */
	public HashMap getAlmacenes() {
		return almacenes;
	}

	/**
	 * @param almacenes the almacenes to set
	 */
	public void setAlmacenes(HashMap almacenes) {
		this.almacenes = almacenes;
	}

	/**
	 * @param key
	 * @return
	 */	
	public Object getAlmacenes(String key){
		return almacenes.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setAlmacenes(String key, Object value){
		this.almacenes.put(key, value);
	}
	
	/**
	 * @return the almacen
	 */
	public String getAlmacen() {
		return almacen;
	}

	/**
	 * @param almacen the almacen to set
	 */
	public void setAlmacen(String almacen) {
		this.almacen = almacen;
	}

	/**
	 * @return the clase
	 */
	public String getClase() {
		return clase;
	}

	/**
	 * @param clase the clase to set
	 */
	public void setClase(String clase) {
		this.clase = clase;
	}

	/**
	 * @return the descripcionClase
	 */
	public String getDescripcionClase() {
		return descripcionClase;
	}

	/**
	 * @param descripcionClase the descripcionClase to set
	 */
	public void setDescripcionClase(String descripcionClase) {
		this.descripcionClase = descripcionClase;
	}

	/**
	 * @return the grupo
	 */
	public String getGrupo() {
		return grupo;
	}

	/**
	 * @param grupo the grupo to set
	 */
	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	/**
	 * @return the descripcionGrupo
	 */
	public String getDescripcionGrupo() {
		return descripcionGrupo;
	}

	/**
	 * @param descripcionGrupo the descripcionGrupo to set
	 */
	public void setDescripcionGrupo(String descripcionGrupo) {
		this.descripcionGrupo = descripcionGrupo;
	}

	/**
	 * @return the subGrupo
	 */
	public String getSubGrupo() {
		return subGrupo;
	}

	/**
	 * @param subGrupo the subGrupo to set
	 */
	public void setSubGrupo(String subGrupo) {
		this.subGrupo = subGrupo;
	}

	/**
	 * @return the descripcionSubGrupo
	 */
	public String getDescripcionSubGrupo() {
		return descripcionSubGrupo;
	}

	/**
	 * @param descripcionSubGrupo the descripcionSubGrupo to set
	 */
	public void setDescripcionSubGrupo(String descripcionSubGrupo) {
		this.descripcionSubGrupo = descripcionSubGrupo;
	}

	/**
	 * @return the codArticulo
	 */
	public String getCodArticulo() {
		return codArticulo;
	}

	/**
	 * @param codArticulo the codArticulo to set
	 */
	public void setCodArticulo(String codArticulo) {
		this.codArticulo = codArticulo;
	}

	/**
	 * @return the tipoCodigoArticulo
	 */
	public String getTipoCodigoArticulo() {
		return tipoCodigoArticulo;
	}

	/**
	 * @param tipoCodigoArticulo the tipoCodigoArticulo to set
	 */
	public void setTipoCodigoArticulo(String tipoCodigoArticulo) {
		this.tipoCodigoArticulo = tipoCodigoArticulo;
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

	/**
	 * @return the consultaCostoArticulos
	 */
	public HashMap getConsultaCostoArticulos() {
		return consultaCostoArticulos;
	}

	/**
	 * @param consultaCostoArticulos the consultaCostoArticulos to set
	 */
	public void setConsultaCostoArticulos(HashMap consultaCostoArticulos) {
		this.consultaCostoArticulos = consultaCostoArticulos;
	}
    
	/**
	 * @param key
	 * @return
	 */	
	public Object getConsultaCostoArticulos(String key){
		return consultaCostoArticulos.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setConsultaCostoArticulos(String key, Object value){
		this.consultaCostoArticulos.put(key, value);
	}

	/**
	 * @return the desArticulo
	 */
	public String getDesArticulo() {
		return desArticulo;
	}

	/**
	 * @param desArticulo the desArticulo to set
	 */
	public void setDesArticulo(String desArticulo) {
		this.desArticulo = desArticulo;
	}
	
}