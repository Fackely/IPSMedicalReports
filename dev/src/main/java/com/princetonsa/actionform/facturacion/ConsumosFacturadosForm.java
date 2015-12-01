package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * @author Mauricio Jllo.
 * Fecha: Mayo de 2008
 */

public class ConsumosFacturadosForm extends ValidatorForm
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
     * Codigo del Centro de Atencion seleccionado para realizar el filtro
     */
    private String codigoCentroAtencion;
	
    /**
     * Fecha Inicial de las facturas
     */
    private String fechaInicial;
    
    /**
     * Fecha Final de las facturas
     */
    private String fechaFinal;
    
    /**
     * Carga los datos del select de Convenios
     */
    private ArrayList<HashMap<String, Object>> convenios;
    
    /**
     * Variable que maneja el codigo del convenio seleccionado
     */
    private String convenioSeleccionado;
    
    /**
     * Maneja el tipo de salida que se desea ejecutar (Imprimir y Archivo Plano)
     */
    private String tipoSalida;
    
    /**
     * Float que tiene el valor base inicial
     */
    private String montoBaseInicial;
    
    /**
     * Float que tiene el valor base final
     */
    private String montoBaseFinal;
    
    /**
     * Valor que maneja el tope por que se desea realizar el filtrado
     */
    private String tope;
    
    /**
     * HashMap para almacenar los datos que arroja la consulta de Consumos Facturados
     */
    private HashMap consumosFacturados;
    
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
    	this.fechaInicial = "";
    	this.fechaFinal = "";
    	this.convenios = new ArrayList<HashMap<String,Object>>();
    	this.convenioSeleccionado = "";
    	this.tipoSalida = "";
    	this.montoBaseInicial = "";
    	this.montoBaseFinal = "";
    	this.tope = "";
    	this.consumosFacturados = new HashMap();
    	this.consumosFacturados.put("numRegistros", "0");
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
			if(this.tipoSalida.trim().equals("") || this.tipoSalida.trim().equals("null"))
			{
				errores.add("Tipo Salida", new ActionMessage("errors.required","El Tipo de Salida "));
				this.errores = true;
			}
			if(this.fechaInicial.trim().equals("") || this.fechaInicial.trim().equals("null"))
			{
				errores.add("codigo", new ActionMessage("errors.required","La Fecha Inicial Facturas "));
				this.errores = true;
			}
			if(this.fechaFinal.trim().equals("") || this.fechaFinal.trim().equals("null"))
			{
				errores.add("codigo", new ActionMessage("errors.required","La Fecha Final Facturas "));
				this.errores = true;
			}
			if(UtilidadTexto.isEmpty((this.montoBaseInicial)+"") && !UtilidadTexto.isEmpty((this.montoBaseFinal)+""))
			{
				errores.add("codigo", new ActionMessage("errors.required","El Monto Base Inicial "));
				this.errores = true;
			}
			if(!UtilidadTexto.isEmpty(this.montoBaseInicial) && UtilidadTexto.isEmpty(this.montoBaseFinal))
			{
				errores.add("codigo", new ActionMessage("errors.required","El Monto Base Final "));
				this.errores = true;
			}
			if(!UtilidadTexto.isEmpty((this.montoBaseInicial)+"") && !UtilidadTexto.isEmpty((this.montoBaseFinal)+""))
			{
				if(Utilidades.convertirADouble((this.montoBaseFinal)+"") < Utilidades.convertirADouble((this.montoBaseInicial)+""))
				{
					errores.add("", new ActionMessage("error.facturacion.montoInicialMenorIgual", "Inicial "+this.montoBaseInicial, "Final "+this.montoBaseFinal));
					this.errores = true;
				}
			}
			if(!UtilidadTexto.isEmpty(this.fechaInicial) && !UtilidadTexto.isEmpty(this.fechaFinal))
			{
				boolean centinelaErrorFechas = false;
				if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaInicial))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Inicial Facturas "+this.fechaInicial));
					centinelaErrorFechas=true;
					this.errores = true;
				}
				if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaFinal))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final Facturas "+this.fechaFinal));
					centinelaErrorFechas=true;
					this.errores = true;
				}
				if(!centinelaErrorFechas)
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial, this.fechaFinal))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial Facturas "+this.fechaInicial, "Final Facturas "+this.fechaFinal));
						this.errores = true;
					}
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaFinal, UtilidadFecha.getFechaActual().toString()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final Facturas "+this.fechaFinal, "Actual "+UtilidadFecha.getFechaActual()));
						this.errores = true;
					}
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial, UtilidadFecha.getFechaActual().toString()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial Facturas "+this.fechaInicial, "Actual "+UtilidadFecha.getFechaActual()));
						this.errores = true;
					}
					if(UtilidadFecha.numeroMesesEntreFechasExacta(this.getFechaInicial(), this.getFechaFinal()) >= 3)
					{
						errores.add("", new ActionMessage("error.facturacion.maximoRangoFechas", "para consultar Consumos Facturados", "3", "90"));
						this.errores = true;
					}
				}
			}
			if(errores.isEmpty())
			{
				this.errores = false;
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
	 * @return the fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * @return the fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * @param fechaInicial the fechaInicial to set
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
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
	 * @return the montoBaseFinal
	 */
	public String getMontoBaseFinal() {
		return montoBaseFinal;
	}

	/**
	 * @param montoBaseFinal the montoBaseFinal to set
	 */
	public void setMontoBaseFinal(String montoBaseFinal) {
		this.montoBaseFinal = montoBaseFinal;
	}

	/**
	 * @return the montoBaseInicial
	 */
	public String getMontoBaseInicial() {
		return montoBaseInicial;
	}

	/**
	 * @param montoBaseInicial the montoBaseInicial to set
	 */
	public void setMontoBaseInicial(String montoBaseInicial) {
		this.montoBaseInicial = montoBaseInicial;
	}

	/**
	 * @return the tope
	 */
	public String getTope() {
		return tope;
	}

	/**
	 * @param tope the tope to set
	 */
	public void setTope(String tope) {
		this.tope = tope;
	}
	
	/**
	 * @return the consumosFacturados
	 */
	public HashMap getConsumosFacturados() {
		return consumosFacturados;
	}

	/**
	 * @param consumosFacturados the consumosFacturados to set
	 */
	public void setConsumosFacturados(HashMap consumosFacturados) {
		this.consumosFacturados = consumosFacturados;
	}

	/**
	 * @param key
	 * @return
	 */	
	public Object getConsumosFacturados(String key) 
	{
		return consumosFacturados.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setConsumosFacturados(String key, Object value) 
	{
		this.consumosFacturados.put(key, value);
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
	
}