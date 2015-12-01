package com.princetonsa.actionform.facturasVarias;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;

/**
 * Fecha: Abril de 2008
 * @author Mauricio Jaramillo
 */

public class ConsultaImpresionPagosFacturasVariasForm extends ValidatorForm 
{

	private String estado;
	
	/**
	 * Tipo de Documento (RC) para la Consulta Inicial
	 */
	private int tipo;
	
	/**
	 * Numero de Documento para la Consulta Inicial
	 */
	private String numeroDocumento;
	
	/**
	 * Consecutivo de Pago para la Consulta Inicial
	 */
	private String consecutivoPago;
	
	/**
	 * Estado del Pago para la Consulta Inicial
	 */
	private int estadoPago;
	
	/**
	 * Fecha Inicial para la Consulta Inicial
	 */
	private String fechaInicial;
	
	/**
	 * Fecha Final para la Consulta Inicial
	 */
	private String fechaFinal;
	
	 /**
     * Para controlar la página actual del pager.
     */
    private int offset;
    
    /**
     * El numero de registros por pager
     */
    private int maxPageItems;
    
    /**
     * Para controlar el link siguiente del pager 
     */
    private String linkSiguiente;
    
    /**
     * Mensaje que informa sobre la generacion de la aplicacion de pagos facturas varias
     */
    private ResultadoBoolean mensaje = new ResultadoBoolean(false);
	
    /**
     * HashMap que almacena el listado de Pagos de Facturas Varias
     */
    private HashMap listadoPagosFacturasVarias = new HashMap();
    
    /**
     * String que maneja el patron por el que se debe ordenar 
     */
    private String patronOrdenar;
    
    /**
     * Ultimo patron por el que se ordeno
     */
    private String ultimoPatron;
    
    /**
     * Posicion aplicacion de pagos facturas varias seleccionada
     */
    private int posAplicacionPagosFacturasVarias;
    
    /**
     * Mapa Seccion Conceptos
     */
    private HashMap secConceptos;
    
    /**
     * Mapa Seccion Facturas
     */
    private HashMap secFacturas;
    
	/**
     * Metodo que inicializa todas las variables.
     */
    public void reset()
    {
    	this.tipo = ConstantesBD.codigoNuncaValido;
    	this.numeroDocumento = "";
    	this.consecutivoPago = "";
    	this.estadoPago = ConstantesBD.codigoNuncaValido;
    	this.fechaInicial = "";
    	this.fechaFinal = "";
    	this.maxPageItems = ConstantesBD.codigoNuncaValido;
    	this.linkSiguiente = "";
    	this.listadoPagosFacturasVarias = new HashMap<String, Object>();
        this.listadoPagosFacturasVarias.put("numRegistros", "0");
        this.patronOrdenar = "";
        this.ultimoPatron = "";
        this.posAplicacionPagosFacturasVarias = ConstantesBD.codigoNuncaValido;
        this.secConceptos = new HashMap<String, Object>();
        this.secConceptos.put("numRegistros", "0");
        this.secFacturas = new HashMap<String, Object>();
        this.secFacturas.put("numRegistros", "0");
    }

    /**
     * Funcion Validate
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		if(this.estado.equals("buscar"))
		{
			if(this.tipo==ConstantesBD.codigoNuncaValido && this.numeroDocumento.trim().equals("") && this.consecutivoPago.trim().equals("") && this.estadoPago==ConstantesBD.codigoNuncaValido)
			{
				if(UtilidadTexto.isEmpty(this.fechaInicial) && UtilidadTexto.isEmpty(this.fechaFinal))
					errores.add("", new ActionMessage("error.facturasVarias.consultaPagosFacturasVarias", "criterio de búsqueda"));
				if(!UtilidadTexto.isEmpty(this.fechaInicial) && UtilidadTexto.isEmpty(this.fechaFinal))
					errores.add("", new ActionMessage("errors.required", "La fecha Final "));
				if(!UtilidadTexto.isEmpty(this.fechaFinal) && UtilidadTexto.isEmpty(this.fechaInicial))
					errores.add("", new ActionMessage("errors.required", "La fecha Inicial "));
			}
			if(!UtilidadTexto.isEmpty(this.fechaInicial) && !UtilidadTexto.isEmpty(this.fechaFinal))
			{
				boolean centinelaErrorFechas = false;
				if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaInicial))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Inicial "+this.fechaInicial));
					centinelaErrorFechas=true;
				}
				if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaFinal))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final "+this.fechaFinal));
					centinelaErrorFechas=true;
				}
				if(!centinelaErrorFechas)
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial.toString(), this.fechaFinal.toString()))
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+this.fechaInicial, "Final "+this.fechaFinal));
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaFinal.toString(), UtilidadFecha.getFechaActual().toString()))
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final "+this.fechaFinal, "Actual "+UtilidadFecha.getFechaActual()));
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial.toString(), UtilidadFecha.getFechaActual().toString()))
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+this.fechaInicial, "Actual "+UtilidadFecha.getFechaActual()));
					if(UtilidadFecha.numeroDiasEntreFechas(this.getFechaInicial(), this.getFechaFinal()) > 180)
						errores.add("", new ActionMessage("error.facturasVarias.consultaFacturasVarias", "para consultar pagos de facturas varias"));
				}
			}
		}
		return errores;
	}

	/**
	 * @return the consecutivoPago
	 */
	public String getConsecutivoPago() {
		return consecutivoPago;
	}

	/**
	 * @param consecutivoPago the consecutivoPago to set
	 */
	public void setConsecutivoPago(String consecutivoPago) {
		this.consecutivoPago = consecutivoPago;
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
	 * @return the estadoPago
	 */
	public int getEstadoPago() {
		return estadoPago;
	}

	/**
	 * @param estadoPago the estadoPago to set
	 */
	public void setEstadoPago(int estadoPago) {
		this.estadoPago = estadoPago;
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
	 * @return the numeroDocumento
	 */
	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	/**
	 * @param numeroDocumento the numeroDocumento to set
	 */
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	/**
	 * @return the tipo
	 */
	public int getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the linkSiguiente
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	/**
	 * @param linkSiguiente the linkSiguiente to set
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * @return the maxPageItems
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}

	/**
	 * @param maxPageItems the maxPageItems to set
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
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
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * @return the listadoPagosFacturasVarias
	 */
	public HashMap getListadoPagosFacturasVarias() {
		return listadoPagosFacturasVarias;
	}

	/**
	 * @param listadoPagosFacturasVarias the listadoPagosFacturasVarias to set
	 */
	public void setListadoPagosFacturasVarias(HashMap listadoPagosFacturasVarias) {
		this.listadoPagosFacturasVarias = listadoPagosFacturasVarias;
	}
    
	/**
	 * @param key
	 * @return
	 */
	public Object getListadoPagosFacturasVarias(String key)
    {
        return listadoPagosFacturasVarias.get(key);
    }
    
	/**
	 * @param key
	 * @param value
	 */
	public void setListadoPagosFacturasVarias(String key,Object value)
    {
        this.listadoPagosFacturasVarias.put(key, value);
    }

	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @return the ultimoPatron
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}

	/**
	 * @param ultimoPatron the ultimoPatron to set
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	/**
	 * @return the posAplicacionPagosFacturasVarias
	 */
	public int getPosAplicacionPagosFacturasVarias() {
		return posAplicacionPagosFacturasVarias;
	}

	/**
	 * @param posAplicacionPagosFacturasVarias the posAplicacionPagosFacturasVarias to set
	 */
	public void setPosAplicacionPagosFacturasVarias(
			int posAplicacionPagosFacturasVarias) {
		this.posAplicacionPagosFacturasVarias = posAplicacionPagosFacturasVarias;
	}

	/**
	 * @return the secConceptos
	 */
	public HashMap getSecConceptos() {
		return secConceptos;
	}

	/**
	 * @param secConceptos the secConceptos to set
	 */
	public void setSecConceptos(HashMap secConceptos) {
		this.secConceptos = secConceptos;
	}

	/**
	 * @param key
	 * @return
	 */
	public Object getSecConceptos(String key)
    {
        return secConceptos.get(key);
    }
    
	/**
	 * @param key
	 * @param value
	 */
	public void setSecConceptos(String key,Object value)
    {
        this.secConceptos.put(key, value);
    }
	
	/**
	 * @return the secFacturas
	 */
	public HashMap getSecFacturas() {
		return secFacturas;
	}

	/**
	 * @param secFacturas the secFacturas to set
	 */
	public void setSecFacturas(HashMap secFacturas) {
		this.secFacturas = secFacturas;
	}
	
	/**
	 * @param key
	 * @return
	 */
	public Object getSecFacturas(String key)
    {
        return secFacturas.get(key);
    }
    
	/**
	 * @param key
	 * @param value
	 */
	public void setSecFacturas(String key,Object value)
    {
        this.secFacturas.put(key, value);
    }
	
}