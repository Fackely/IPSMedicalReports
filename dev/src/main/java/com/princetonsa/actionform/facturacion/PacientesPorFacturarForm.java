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

/**
 * @author Mauricio Jaramillo
 * Fecha: Agosto de 2008
 */

public class PacientesPorFacturarForm extends ValidatorForm
{

	private String estado;
	
    /**
     * HashMap de los Centros de Atencion 
     */
    private HashMap centroAtencion;
    
    /**
     * Codigo del Centro de Atencion seleccionado para realizar el filtro
     */
    private String codigoCentroAtencion;
	
    /**
     * Carga las vias de ingreso
     */
    private ArrayList<HashMap<String, Object>> viasIngresos;
    
    /**
     * Codigo de la Via de Ingreso para realizar la busqueda
     */
    private String viaIngreso;
    
	/**
	 * String para la fecha inicial para la busqueda de las facturas
	 */
	private String fechaInicial;
	
	/**
	 * String para la fecha final para la busqueda de las facturas
	 */
	private String fechaFinal;
	
	/**
     * Carga los tipos convenio
     */
    private ArrayList<HashMap<String, Object>> tiposConvenio;
    
    /**
     * Codigo del tipo convenio seleccionado
     */
    private String tipoConvenioSeleccionado;
	
    /**
     * Carga los datos del select de Convenios
     */
    private ArrayList<HashMap<String, Object>> convenios;
    
    /**
     * Variable que maneja el convenio seleccionado
     */
    private String convenioSeleccionado;
    
    /**
     * String con el indicativo de egreso seleccionado para realizar el filtrado
     */
    private String tipoEgreso;
    
    /**
     * HashMap con la información de pacientes con consumos por facturar
     */
    private HashMap consumosPorFacturar;
    
	 /**
     * Maneja el tipo de salida que se desea ejecutar (Imprimir y Archivo Plano)
     */
    private String tipoSalida;
	
    /**
	 * Mensaje generacion del archivo
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
	 * Controla si se generaron errores en validate para no mostrar el mensaje
	 */
	private boolean errores;
	
	/**
	 * Valida si se genero el archivo .zip para informar al usuario
	 */
	private boolean zip;
	
	/**
	 * Metodo que inicializa las variables
	 */
	public void reset()
    {
		this.centroAtencion = new HashMap();
    	this.centroAtencion.put("numRegistros", "0");
    	this.codigoCentroAtencion = "";
    	this.viasIngresos = new ArrayList<HashMap<String,Object>>();
		this.viaIngreso = "";
		this.tiposConvenio = new ArrayList<HashMap<String,Object>>();
		this.tipoConvenioSeleccionado = "";
		this.convenios = new ArrayList<HashMap<String,Object>>();
		this.convenioSeleccionado = "";
		this.fechaInicial = "";
    	this.fechaFinal = "";
    	this.tipoEgreso = "";
    	this.tipoSalida = "";
    	this.consumosPorFacturar = new HashMap();
    	this.consumosPorFacturar.put("numRegistros", "0");
    	this.pathArchivoTxt = "";
	 	this.archivo = false;
	 	this.errores = false;
	 	this.zip = false;
    }
	
	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		if(this.estado.equals("generar"))
		{
			if(this.codigoCentroAtencion.trim().equals("") || this.codigoCentroAtencion.trim().equals("null"))
			{
				errores.add("Centro de Atención", new ActionMessage("errors.required","El Centro de Atención "));
				this.errores = true;
			}
			if(this.fechaInicial.trim().equals("") || this.fechaInicial.trim().equals("null"))
			{
				errores.add("codigo", new ActionMessage("errors.required","La Fecha Inicial de Ingreso "));
				this.errores = true;
			}
			if(this.fechaFinal.trim().equals("") || this.fechaFinal.trim().equals("null"))
			{
				errores.add("codigo", new ActionMessage("errors.required","La Fecha Final de Ingreso "));
				this.errores = true;
			}			
			if(this.tipoEgreso.trim().equals("") || this.tipoEgreso.trim().equals("null"))
			{
				errores.add("tipoEgreso", new ActionMessage("errors.required","El Tipo Egreso "));
				this.errores = true;
			}
			if(this.tipoSalida.trim().equals("") || this.tipoSalida.trim().equals("null"))
			{
				errores.add("Tipo Salida", new ActionMessage("errors.required","El Tipo de Salida "));
				this.errores = true;
			}
			if(!UtilidadTexto.isEmpty(this.fechaInicial) && !UtilidadTexto.isEmpty(this.fechaFinal))
			{
				boolean centinelaErrorFechas = false;
				if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaInicial))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Inicial de Ingreso "+this.fechaInicial));
					centinelaErrorFechas = true;
					this.errores = true;
				}
				if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaFinal))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final de Ingreso "+this.fechaFinal));
					centinelaErrorFechas = true;
					this.errores = true;
				}
				if(!centinelaErrorFechas)
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial, this.fechaFinal))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial de Ingreso "+this.fechaInicial, "Final de Ingreso "+this.fechaFinal));
						this.errores = true;
					}
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaFinal, UtilidadFecha.getFechaActual().toString()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final de Ingreso "+this.fechaFinal, "Actual "+UtilidadFecha.getFechaActual()));
						this.errores = true;
					}
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial, UtilidadFecha.getFechaActual().toString()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial de Ingreso "+this.fechaInicial, "Actual "+UtilidadFecha.getFechaActual()));
						this.errores = true;
					}
					if(UtilidadFecha.numeroMesesEntreFechasExacta(this.getFechaInicial(), this.getFechaFinal()) >= 3)
					{
						errores.add("", new ActionMessage("error.facturacion.maximoRangoFechas", "para consultar Pacientes Por Facturar", "3", "90"));
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
	 * @return
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
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
	public Object getCentroAtencion(String key) {
		return centroAtencion.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setCentroAtencion(String key, Object value) {
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
	 * @return the tipoConvenioSeleccionado
	 */
	public String getTipoConvenioSeleccionado() {
		return tipoConvenioSeleccionado;
	}

	/**
	 * @param tipoConvenioSeleccionado the tipoConvenioSeleccionado to set
	 */
	public void setTipoConvenioSeleccionado(String tipoConvenioSeleccionado) {
		this.tipoConvenioSeleccionado = tipoConvenioSeleccionado;
	}

	/**
	 * @return the tiposConvenio
	 */
	public ArrayList<HashMap<String, Object>> getTiposConvenio() {
		return tiposConvenio;
	}

	/**
	 * @param tiposConvenio the tiposConvenio to set
	 */
	public void setTiposConvenio(ArrayList<HashMap<String, Object>> tiposConvenio) {
		this.tiposConvenio = tiposConvenio;
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
	 * @return the tipoEgreso
	 */
	public String getTipoEgreso() {
		return tipoEgreso;
	}

	/**
	 * @param tipoEgreso the tipoEgreso to set
	 */
	public void setTipoEgreso(String tipoEgreso) {
		this.tipoEgreso = tipoEgreso;
	}

	/**
	 * @return the consumosPorFacturar
	 */
	public HashMap getConsumosPorFacturar() {
		return consumosPorFacturar;
	}

	/**
	 * @param consumosPorFacturar the consumosPorFacturar to set
	 */
	public void setConsumosPorFacturar(HashMap consumosPorFacturar) {
		this.consumosPorFacturar = consumosPorFacturar;
	}
	
	/**
	 * @param key
	 * @return
	 */	
	public Object getConsumosPorFacturar(String key) {
		return consumosPorFacturar.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setConsumosPorFacturar(String key, Object value) {
		this.consumosPorFacturar.put(key, value);
	}
	
}