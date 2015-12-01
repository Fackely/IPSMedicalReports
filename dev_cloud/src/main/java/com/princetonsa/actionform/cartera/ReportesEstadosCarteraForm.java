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

public class ReportesEstadosCarteraForm extends ValidatorForm
{
	/**
    * Variable para manejar el estado de la funcionalidad 
    */
	private String estado="";
    
//*****************Inicio Declaracion variables manejadas en la busqueda de estados de cartera
   
    /**
     * Para manejar la fecha inicial del rango de Reporte del estado de la Cartera.
     */
    private String fechaInicial;
    /**
     * Para manejar la fecha final del rango de Reporte del estado de la Cartera.
     */
    private String fechaFinal;
    
    /**
     * Tipo de Reporte del estado de la Cartera
     */
    private String tipoReporte;
    
    /**
     * Tipo Contrato para el Campo Convenio: Normal
     */
    private int convenio;
    
    /**
     * Maneja todos los Estados de las cuentas de Cobro.
     */
    private int estadoCuenta;
    
    /**
     * Maneja la Estructura de Empresas.
     */
    private int empresa;
    
    /**
     * es capitado?
     */
    private String esCapitado="";
    
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
    
    
    //*****************Fin Declaracion variables manejadas en la busqueda de estado de cartera
   
    
    /**
     * reset
     */
    public void reset(int codigoInstitucion)
    {
        this.tipoReporte="";
        this.fechaInicial="";
        this.fechaFinal="";
        this.convenio=ConstantesBD.codigoNuncaValido;
        this.empresa=ConstantesBD.codigoNuncaValido;
        this.estadoCuenta=ConstantesBD.codigoNuncaValido;
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
		
		if(this.estado.equals("imprimir"))
		{
			if(UtilidadTexto.isEmpty(this.getTipoReporte()))
			{
				errores.add("codigo", new ActionMessage("errors.required","El Tipo de Reporte "));
				this.errores = true;
			}
			if(UtilidadTexto.isEmpty(this.getFechaInicial()))
			{
				errores.add("codigo", new ActionMessage("errors.required","La Fecha Inicial "));
				this.errores = true;
			}
			if(UtilidadTexto.isEmpty(this.getFechaFinal()))
			{
				errores.add("codigo", new ActionMessage("errors.required","La Fecha Final "));
				this.errores = true;
			}
			if(this.tipoSalida==null || this.tipoSalida.equals(""))
			{
				errores.add("tipoSalida", new ActionMessage("errors.required","El Tipo de Salida "));
				this.errores = true;
			}
			if(!UtilidadTexto.isEmpty(this.getFechaInicial()) || !UtilidadTexto.isEmpty(this.getFechaFinal()))
			{
				boolean centinelaErrorFechas=false;
				if(!UtilidadFecha.esFechaValidaSegunAp(this.getFechaInicial()))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Inicial "+this.getFechaInicial()));
					this.errores = true;
					centinelaErrorFechas=true;
				}
				if(!UtilidadFecha.esFechaValidaSegunAp(this.getFechaFinal()))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final "+this.getFechaFinal()));
					this.errores = true;
					centinelaErrorFechas=true;
				}
				if(!centinelaErrorFechas)
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getFechaInicial(), this.getFechaFinal()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "inicial "+this.getFechaInicial(), "final "+this.getFechaFinal()));
						this.errores = true;
					}
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getFechaInicial(), UtilidadFecha.getFechaActual()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "inicial "+this.getFechaInicial(), "Actual "+UtilidadFecha.getFechaActual()));
						this.errores = true;
					}
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getFechaFinal(), UtilidadFecha.getFechaActual()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final "+this.getFechaFinal(), "Actual "+UtilidadFecha.getFechaActual()));
						this.errores = true;
					}
					if(UtilidadFecha.numeroMesesEntreFechasExacta(this.getFechaInicial(), this.getFechaFinal())>=3)
					{
						errores.add("", new ActionMessage("errors.rangoMayorTresMeses", "PARA CONSULTAR ESTADOS CARTERA"));
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
	public int getConvenio() {
		return convenio;
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(int convenio) {
		this.convenio = convenio;
	}

	/**
	 * @return the empresa
	 */
	public int getEmpresa() {
		return empresa;
	}

	/**
	 * @param empresa the empresa to set
	 */
	public void setEmpresa(int empresa) {
		this.empresa = empresa;
	}

	/**
	 * @return the esCapitado
	 */
	public String getEsCapitado() {
		return esCapitado;
	}

	/**
	 * @param esCapitado the esCapitado to set
	 */
	public void setEsCapitado(String esCapitado) {
		this.esCapitado = esCapitado;
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
	 * @return the estadoCuenta
	 */
	public int getEstadoCuenta() {
		return estadoCuenta;
	}

	/**
	 * @param estadoCuenta the estadoCuenta to set
	 */
	public void setEstadoCuenta(int estadoCuenta) {
		this.estadoCuenta = estadoCuenta;
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