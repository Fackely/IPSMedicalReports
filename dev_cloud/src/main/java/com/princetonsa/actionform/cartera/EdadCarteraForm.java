package com.princetonsa.actionform.cartera;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.mundo.facturacion.ParametrizacionInstitucion;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.Administracion.UtilConversionMonedas;

public class EdadCarteraForm extends ValidatorForm 
{
	
	 /**
     * Variable para manejar el estado de la funcionalidad 
     */
	private String estado="";

   
    
//*****************Inicio Declaracion variables manejadas en la busqueda de estados de cartera
   
    /**
     * Para manejar la fecha inicial del rango de Reporte del estado de la Cartera.
     */
    private String fechaCorte;
    
    
    private String tipoReporte;
    
    /**
     * Tipo Contrato para el Campo Convenio
     */
    private int convenioNormal;
    
    /**
     * 
     */
    private int tipoConvenio;
    
    /**
     * 
     */
    private int index;
    
    /**
     * 
     */
    private boolean manejaConversionMoneda;
    
    /**
     * 
     */
    private HashMap tiposMonedaTagMap;
    
    //*****************Fin Declaracion variables manejadas en la busqueda de estado de cartera
    
    /**
     * 
     */
    private String esFactura="";
   
    /**
     * 
     */
    private double empresaInstitucion;
    
    /**
     * 
     */
    private HashMap empresasInstitucionMap= new HashMap();
    
    /**
     * Tipo Salida Reporte (PDF, Archivo Plano)
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
     * reset de la forma
     *
     */
    public void reset(int codigoInstitucion)
    {
        this.tipoReporte="";
        this.fechaCorte="";
        this.convenioNormal=ConstantesBD.codigoNuncaValido;
        this.tipoConvenio=ConstantesBD.codigoNuncaValido;
        this.index=ConstantesBD.codigoNuncaValido;
        this.manejaConversionMoneda=UtilidadTexto.getBoolean(ValoresPorDefecto.getManejaConversionMonedaExtranjera(codigoInstitucion));
        this.empresaInstitucion=ConstantesBD.codigoNuncaValidoDoubleNegativo;
        this.empresasInstitucionMap= new HashMap();
        this.empresasInstitucionMap.put("numRegistros", "0");
        this.inicializarTagMap(codigoInstitucion);
        this.tipoSalida = "";
        this.pathArchivoTxt = "";
        this.archivo = false;
        this.errores = false;
    }

    /**
     * 
     * @param codigoInstitucion
     */
    public void inicializarTagMap (int codigoInstitucion)
    {
    	tiposMonedaTagMap= UtilConversionMonedas.obtenerTiposMonedaTagMap(codigoInstitucion, /*mostrarMonedaManejaInstitucion*/false);
    	if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(codigoInstitucion)))
    	{
    		empresasInstitucionMap= ParametrizacionInstitucion.obtenerEmpresasInstitucion(codigoInstitucion);
    	}
    }
    
    
    /**
     * 
     */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		if(this.estado.equals("imprimir"))
		{
			if(this.tipoReporte==null || this.tipoReporte.equals(""))
			{
				errores.add("codigo", new ActionMessage("errors.required","El Tipo Reporte "));
				this.errores = true;
			}
			if(UtilidadTexto.isEmpty(this.getFechaCorte()))
			{
				errores.add("codigo", new ActionMessage("errors.required","La Fecha Corte "));
				this.errores = true;
			}
			if(this.tipoSalida==null || this.tipoSalida.equals(""))
			{
				errores.add("tipoSalida", new ActionMessage("errors.required","El Tipo de Salida "));
				this.errores = true;
			}
			if(!UtilidadTexto.isEmpty(this.getFechaCorte()) || !UtilidadTexto.isEmpty(this.getFechaCorte()))
			{
				boolean centinelaErrorFechas=false;
				if(!UtilidadFecha.esFechaValidaSegunAp(this.getFechaCorte()))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Corte "+this.getFechaCorte()));
					this.errores = true;
					centinelaErrorFechas=true;
				}
				if(!centinelaErrorFechas)
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getFechaCorte(), UtilidadFecha.getFechaActual()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Corte "+this.getFechaCorte(), "Actual "+UtilidadFecha.getFechaActual()));
						this.errores = true;
					}
				}
			}
		}
		return errores;
	}

	/**
	 * @return the convenio
	 */
	public int getConvenioNormal() {
		return convenioNormal;
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenioNormal(int convenio) {
		this.convenioNormal = convenio;
	}

	/**
	 * @return the esFactura
	 */
	public String getEsFactura() {
		return esFactura;
	}

	/**
	 * @param esFactura the esFactura to set
	 */
	public void setEsFactura(String esFactura) {
		this.esFactura = esFactura;
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
	 * @return the fechaCorte
	 */
	public String getFechaCorte() {
		return fechaCorte;
	}

	/**
	 * @param fechaCorte the fechaCorte to set
	 */
	public void setFechaCorte(String fechaCorte) {
		this.fechaCorte = fechaCorte;
	}

	/**
	 * @return the tipoConvenio
	 */
	public int getTipoConvenio() {
		return tipoConvenio;
	}

	/**
	 * @param tipoConvenio the tipoConvenio to set
	 */
	public void setTipoConvenio(int tipoConvenio) {
		this.tipoConvenio = tipoConvenio;
	}

	/**
	 * @return the tipoReporte
	 */
	public String getTipoReporte() {
		return tipoReporte;
	}

	/**
	 * @param tipoReporte the tipoReporte to set
	 */
	public void setTipoReporte(String tipoReporte) {
		this.tipoReporte = tipoReporte;
	}

	/**
	 * @return the manejaConversionMoneda
	 */
	public boolean getManejaConversionMoneda() {
		return manejaConversionMoneda;
	}

	/**
	 * @param manejaConversionMoneda the manejaConversionMoneda to set
	 */
	public void setManejaConversionMoneda(boolean manejaConversionMoneda) {
		this.manejaConversionMoneda = manejaConversionMoneda;
	}

	/**
	 * @return the tiposMonedaTagMap
	 */
	public HashMap getTiposMonedaTagMap() {
		return tiposMonedaTagMap;
	}

	/**
	 * @param tiposMonedaTagMap the tiposMonedaTagMap to set
	 */
	public void setTiposMonedaTagMap(HashMap tiposMonedaTagMap) {
		this.tiposMonedaTagMap = tiposMonedaTagMap;
	}

	/**
	 * @return the tiposMonedaTagMap
	 */
	public Object getTiposMonedaTagMap(Object key) {
		return tiposMonedaTagMap.get(key);
	}

	/**
	 * @param tiposMonedaTagMap the tiposMonedaTagMap to set
	 */
	public void setTiposMonedaTagMap(Object key, Object value) {
		this.tiposMonedaTagMap.put(key, value);
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
	 * @return the empresasInstitucionMap
	 */
	public HashMap getEmpresasInstitucionMap() {
		return empresasInstitucionMap;
	}

	/**
	 * @param empresasInstitucionMap the empresasInstitucionMap to set
	 */
	public void setEmpresasInstitucionMap(HashMap empresasInstitucionMap) {
		this.empresasInstitucionMap = empresasInstitucionMap;
	}

	/**
	 * @return the empresaInstitucion
	 */
	public double getEmpresaInstitucion() {
		return empresaInstitucion;
	}

	/**
	 * @param empresaInstitucion the empresaInstitucion to set
	 */
	public void setEmpresaInstitucion(double empresaInstitucion) {
		this.empresaInstitucion = empresaInstitucion;
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

	
}