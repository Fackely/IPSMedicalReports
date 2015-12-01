package com.princetonsa.actionform.capitacion;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.Administracion.UtilConversionMonedas;


public class EdadCarteraCapitacionForm extends ValidatorForm 
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
    
    /**
     * 
     */
    private String tipoReporte;
    
    /**
     * Tipo Contrato para el Campo Convenio: Normal
     */
    private int convenioCapitado;
    
    /**
     * 
     */  
    private int contrato;
    
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
     * reset
     */
    public void reset(int codigoInstitucion)
    {
        this.tipoReporte="";
        this.fechaCorte="";
        this.convenioCapitado=ConstantesBD.codigoNuncaValido;
        this.tipoConvenio=ConstantesBD.codigoNuncaValido;
        this.contrato=ConstantesBD.codigoNuncaValido;
        this.index=ConstantesBD.codigoNuncaValido;
        this.manejaConversionMoneda=UtilidadTexto.getBoolean(ValoresPorDefecto.getManejaConversionMonedaExtranjera(codigoInstitucion));
        this.inicializarTagMap(codigoInstitucion);
    }
    
    /**
     * 
     * @param codigoInstitucion
     */
    public void inicializarTagMap (int codigoInstitucion)
    {
    	tiposMonedaTagMap= UtilConversionMonedas.obtenerTiposMonedaTagMap(codigoInstitucion, /*mostrarMonedaManejaInstitucion*/false);
    }
    
    /**
     *
     */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		if(this.estado.equals("imprimir"))
		{
			if(UtilidadTexto.isEmpty(this.getTipoReporte()))
			{
				errores.add("codigo", new ActionMessage("errors.required","El Tipo de Reporte "));
			}
			if(UtilidadTexto.isEmpty(this.getFechaCorte()))
			{
				errores.add("codigo", new ActionMessage("errors.required","La Fecha Corte "));
			}
			if(!UtilidadTexto.isEmpty(this.getFechaCorte()) || !UtilidadTexto.isEmpty(this.getFechaCorte()))
			{
				boolean centinelaErrorFechas=false;
				if(!UtilidadFecha.esFechaValidaSegunAp(this.getFechaCorte()))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Inicial "+this.getFechaCorte()));
					centinelaErrorFechas=true;
				}
				if(!centinelaErrorFechas)
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getFechaCorte(), UtilidadFecha.getFechaActual()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Corte "+this.getFechaCorte(), "Actual "+UtilidadFecha.getFechaActual()));
					}
				}
			}
						
		}
		return errores;
	}

	/**
	 * @return the contrato
	 */
	public int getContrato() {
		return contrato;
	}

	/**
	 * @param contrato the contrato to set
	 */
	public void setContrato(int contrato) {
		this.contrato = contrato;
	}

	/**
	 * @return the convenioCapitado
	 */
	public int getConvenioCapitado() {
		return convenioCapitado;
	}

	/**
	 * @param convenioCapitado the convenioCapitado to set
	 */
	public void setConvenioCapitado(int convenioCapitado) {
		this.convenioCapitado = convenioCapitado;
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
}