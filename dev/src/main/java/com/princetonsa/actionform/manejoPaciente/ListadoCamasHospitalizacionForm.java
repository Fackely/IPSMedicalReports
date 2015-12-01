package com.princetonsa.actionform.manejoPaciente;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;

public class ListadoCamasHospitalizacionForm extends ValidatorForm 

{
	
	private String estado="";
	
    /**
     * HashMap de los Centros de Atencion 
     */
    private HashMap centroAtencion;
	
    /**
     * Codigo del Centro de Atencion
     */
	private String codigoCentroAtencion;
	
	private int estadoCama;
	
	private String ocupadas;
	
	private String pendienteTrasladar;
	
	private String salida;
	
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
	 *  Metodo Resetea los atributos del form.
	 *
	 */
	public void reset()
	{
		this.centroAtencion = new HashMap();
    	this.centroAtencion.put("numRegistros", "0");
    	this.codigoCentroAtencion = "";
    	this.estadoCama = ConstantesBD.codigoNuncaValido;
		this.ocupadas = ConstantesBD.acronimoNo;
		this.pendienteTrasladar = ConstantesBD.acronimoNo;
		this.salida = ConstantesBD.acronimoNo;
		this.tipoSalida = "";
		this.pathArchivoTxt = "";
	 	this.archivo = false;
	 	this.errores = false;
	 	this.zip = false;
	}
		
	/**
	* Validacion de Errores 
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		if(this.estado.equals("imprimir") || this.estado.equals("generar_csv"))
		{
			if(this.codigoCentroAtencion.trim().equals("") || this.codigoCentroAtencion.trim().equals("null"))
			{
				errores.add("centro atencion", new ActionMessage("errors.required","El Centro de Atención "));
				this.errores = true;
			}
			if((this.ocupadas.equals(ConstantesBD.acronimoNo)) && this.pendienteTrasladar.equals(ConstantesBD.acronimoNo) && this.salida.equals(ConstantesBD.acronimoNo))
			{
				errores.add("estado cama", new ActionMessage("error.manejoPacientes.estadosCama"));
				this.errores = true;
			}
			if(this.tipoSalida.trim().equals("") || this.tipoSalida.trim().equals("null"))
			{
				errores.add("Tipo Salida", new ActionMessage("errors.required","El Tipo de Salida "));
				this.errores = true;
			}
		}
		if(errores.isEmpty())
		{
			this.errores = false;
		}
		return errores;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getEstadoCama() {
		return estadoCama;
	}

	/**
	 * 
	 * @param estadoCama
	 */
	public void setEstadoCama(int estadoCama) {
		this.estadoCama = estadoCama;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getOcupadas() {
		return ocupadas;
	}

	/**
	 * 
	 * @param ocupadas
	 */
	public void setOcupadas(String ocupadas) {
		this.ocupadas = ocupadas;
	}

	/**
	 * 
	 * @return
	 */
	public String getPendienteTrasladar() {
		return pendienteTrasladar;
	}

	/**
	 * 
	 * @param pendienteTrasladar
	 */
	public void setPendienteTrasladar(String pendienteTrasladar) {
		this.pendienteTrasladar = pendienteTrasladar;
	}

	/**
	 * 
	 * @return
	 */
	public String getSalida() {
		return salida;
	}

	/**
	 * 
	 * @param salida
	 */
	public void setSalida(String salida) {
		this.salida = salida;
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
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	/**
	 * 
	 * @param mensaje
	 */
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
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
	
}