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
 * @author Mauricio Jllo
 * Fecha Junio de 2008
 */

public class ConsolidadoFacturacionForm extends ValidatorForm
{

	private String estado;

	/**
	 * String para la fecha inicial para la busqueda de las facturas
	 */
	private String fechaInicial;
	
	/**
	 * String para la fecha final para la busqueda de las facturas
	 */
	private String fechaFinal;
	
	/**
	 * Numero de la Factura Inicial
	 */
	private String facturaInicial;
	
	/**
	 * Numero de la Factura Final
	 */
	private String facturaFinal;
	
    /**
     * Carga los datos del select de Convenios
     */
    private ArrayList<HashMap<String, Object>> convenios;
    
    /**
     * Variable que maneja el convenio seleccionado
     */
    private String convenioSeleccionado; 
	
    /**
     * Carga los datos del select de contratos
     */
    private ArrayList<HashMap<String, Object>> contratos;
    
    /**
     * Variable que maneja el contrato seleccionado
     */
    private String contratoSeleccionado;
    
    /**
     * Carga las vias de ingreso
     */
    private ArrayList<HashMap<String, Object>> viasIngresos;
    
    /**
     * Codigo de la Via de Ingreso para realizar la busqueda
     */
    private String viaIngreso;
    
    /**
     * Tipo Salida Reporte (PDF, Archivo Plano)
     */
    private String tipoSalida;
    
    /**
     * HashMap con la informacion de la consulta de consolidados de facturacion
     */
    private HashMap consolidadoFacturacion;
    
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
		this.fechaInicial = "";
		this.fechaFinal = "";
		this.facturaInicial = "";
		this.facturaFinal = "";
		this.convenios = new ArrayList<HashMap<String,Object>>();
		this.convenioSeleccionado = "";
		this.contratos = new ArrayList<HashMap<String,Object>>();
		this.contratoSeleccionado = "";
		this.viasIngresos = new ArrayList<HashMap<String,Object>>();
		this.viaIngreso = "";
		this.consolidadoFacturacion = new HashMap();
		this.consolidadoFacturacion.put("numRegistros", "0");
		this.tipoSalida = "";
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
			//Validamos el Campo Tipo de Salida que es Requerido
			if(this.tipoSalida.trim().equals("null") || this.tipoSalida.trim().equals(""))
			{
				errores.add("tipoSalida", new ActionMessage("errors.required","El Tipo de Salida "));
				this.errores = true;
			}
			//Validamos que la factura final es requerida siempre y cuando la inicial no este vacia
			if(!UtilidadTexto.isEmpty(this.facturaInicial) && UtilidadTexto.isEmpty(this.facturaFinal))
			{
				errores.add("facturaFinal", new ActionMessage("errors.required","La Factura Final "));
				this.errores = true;
			}
			//Validamos que la factura inicial es requerida siempre y cuando la final no este vacia
			if(!UtilidadTexto.isEmpty(this.facturaFinal) && UtilidadTexto.isEmpty(this.facturaInicial))
			{
				errores.add("facturaInicial", new ActionMessage("errors.required","La Factura Inicial "));
				this.errores = true;
			}
			//Hacemos la validacion que los campos factura inicial y final sean mayores a cero
			if(!UtilidadTexto.isEmpty(this.facturaInicial) && !UtilidadTexto.isEmpty(this.facturaFinal))
			{
				if(Utilidades.convertirAEntero(this.getFacturaInicial()) < 0)
				{
					errores.add("errors.integer", new ActionMessage("errors.integer","El Rango Inicial de Facturas"));
					this.errores = true;
				}
				if(Utilidades.convertirAEntero(this.getFacturaFinal()) < 0)
				{
					errores.add("errors.integer", new ActionMessage("errors.integer","El Rango Final de Facturas"));
					this.errores = true;
				}
			}
			//Validamos que los campos factura vengas vacios con la intencion de verificar que las fechas sean requeridas
			if(UtilidadTexto.isEmpty(this.facturaInicial) && UtilidadTexto.isEmpty(this.facturaFinal))
			{
				if(this.fechaInicial.trim().equals("null") || this.fechaInicial.trim().equals(""))
				{
					errores.add("fechaInicial", new ActionMessage("errors.required","La Fecha Inicial "));
					this.errores = true;
				}
				if(this.fechaFinal.trim().equals("null") || this.fechaFinal.trim().equals(""))
				{
					errores.add("fechaFinal", new ActionMessage("errors.required","La Fecha Final "));
					this.errores = true;
				}
			}
			//Hacemos las validaciones de los campos fecha
			if(!UtilidadTexto.isEmpty(this.fechaInicial) || !UtilidadTexto.isEmpty(this.fechaFinal))
			{
				boolean centinelaErrorFechas=false;
				if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaInicial.toString()))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Inicial "+this.fechaInicial));
					centinelaErrorFechas=true;
					this.errores = true;
				}
				if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaFinal.toString()))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final "+this.fechaFinal));
					centinelaErrorFechas=true;
					this.errores = true;
				}
				if(!centinelaErrorFechas)
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial.toString(), this.fechaFinal.toString()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+this.fechaInicial, "Final "+this.fechaFinal));
						this.errores = true;
					}
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaFinal.toString(), UtilidadFecha.getFechaActual().toString()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final "+this.fechaFinal, "Actual "+UtilidadFecha.getFechaActual()));
						this.errores = true;
					}
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial.toString(), UtilidadFecha.getFechaActual().toString()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+this.fechaInicial, "Actual "+UtilidadFecha.getFechaActual()));
						this.errores = true;
					}
					if(UtilidadFecha.numeroMesesEntreFechasExacta(this.getFechaInicial(), this.getFechaFinal()) >= 3)
					{
						errores.add("", new ActionMessage("error.facturacion.maximoRangoFechas", "para consultar Consolidado de Facturación", "3", "90"));
						this.errores = true;
					}
				}
			}
		}
		if(errores.isEmpty())
		{
			this.errores = false;
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
	 * @return the facturaFinal
	 */
	public String getFacturaFinal() {
		return facturaFinal;
	}

	/**
	 * @param facturaFinal the facturaFinal to set
	 */
	public void setFacturaFinal(String facturaFinal) {
		this.facturaFinal = facturaFinal;
	}

	/**
	 * @return the facturaInicial
	 */
	public String getFacturaInicial() {
		return facturaInicial;
	}

	/**
	 * @param facturaInicial the facturaInicial to set
	 */
	public void setFacturaInicial(String facturaInicial) {
		this.facturaInicial = facturaInicial;
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
	 * @return the contratos
	 */
	public ArrayList<HashMap<String, Object>> getContratos() {
		return contratos;
	}

	/**
	 * @param contratos the contratos to set
	 */
	public void setContratos(ArrayList<HashMap<String, Object>> contratos) {
		this.contratos = contratos;
	}

	/**
	 * @return the contratoSeleccionado
	 */
	public String getContratoSeleccionado() {
		return contratoSeleccionado;
	}

	/**
	 * @param contratoSeleccionado the contratoSeleccionado to set
	 */
	public void setContratoSeleccionado(String contratoSeleccionado) {
		this.contratoSeleccionado = contratoSeleccionado;
	}

	/**
	 * @return the consolidadoFacturacion
	 */
	public HashMap getConsolidadoFacturacion() {
		return consolidadoFacturacion;
	}

	/**
	 * @param consolidadoFacturacion the consolidadoFacturacion to set
	 */
	public void setConsolidadoFacturacion(HashMap consolidadoFacturacion) {
		this.consolidadoFacturacion = consolidadoFacturacion;
	} 
	
	/**
	 * @param key
	 * @return
	 */	
	public Object getConsolidadoFacturacion(String key) 
	{
		return consolidadoFacturacion.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setConsolidadoFacturacion(String key, Object value) 
	{
		this.consolidadoFacturacion.put(key, value);
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