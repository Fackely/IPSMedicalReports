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
 * @author axioma
 * Mauricio Jaramillo H.
 */

public class AprobacionAnulacionPagosFacturasVariasForm extends ValidatorForm
{

	private String estado;
	
	/**
     * Mapa que contiene todas las aplicaciones de pago de facturas varias  
     */
    private HashMap listadoAplicaciones;

    /**
     * Mensaje que informa sobre la generacion de la aplicacion de pagos facturas varias
     */
    private ResultadoBoolean mensaje = new ResultadoBoolean(false);
    
    /**
     * Busqueda Avanzada por fecha documento
     */
    private String fechaDocumento;
    
    /**
     * Busqueda Avanzada por tipo de documento
     */
    private int tipoDocumento;
    
    /**
     * Busqueda Avanzada por documento
     */
    private String numeroDocumento;
    
    /**
     * Busqueda Avanzada por deudor de documento
     */
    private int deudorDocumento;
    
    /**
     * Mapa que contiene las facturas asociadas al deudor y a la aplicacion de pago seleccionada 
     */
    private HashMap listadoFacturasAplicacion;
    
    /**
     * Posicion del deudor seleccionado para consultar las facturas aplicadas asociadas a el
     */
    private int posicion;
    
    /**
     * Estado seleccionado para aprobar o anular una aplicacion de pago de facturas varias
     */
    private int estadoAprobAnul;
    
    /**
     * Motivo de anulacion de una aplicacion de pago anulada
     */
    private String motivoAprobAnul;
    
    /**
     * Fecha de aprobacion o anulacion de una aplicacion de pago de facturas varias
     */
    private String fechaAprobAnul; 
    
    /**
     * Codigo de la Aplicacion de Pago seleccionada para anular o aprobar
     */
    private int codAplicacionPago;
    
    /**
     * Fecha generacion del documento para realizar la validacion
     */
    private String fechaGenDoc;
    
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
     * Metodo que inicializa todas las variables.
     */
    public void reset()
    {
    	this.listadoAplicaciones = new HashMap<String, Object>();
        this.listadoAplicaciones.put("numRegistros", "0");
        this.fechaDocumento = "";
        this.tipoDocumento = ConstantesBD.codigoNuncaValido;
        this.numeroDocumento = "";
        this.deudorDocumento = ConstantesBD.codigoNuncaValido;
        this.listadoFacturasAplicacion = new HashMap<String, Object>();
        this.listadoFacturasAplicacion.put("numRegistros", "0");
        this.posicion = ConstantesBD.codigoNuncaValido;
        this.estadoAprobAnul = ConstantesBD.codigoNuncaValido;
        this.motivoAprobAnul = "";
        this.fechaAprobAnul = UtilidadFecha.getFechaActual();
        this.fechaGenDoc = "";
        this.codAplicacionPago = ConstantesBD.codigoNuncaValido;
        this.maxPageItems = 20;
        this.linkSiguiente = "";
    }
    
    /**
     * Funcion Validate
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		if(this.estado.equals("guardar"))
		{
			if(this.estadoAprobAnul == ConstantesBD.codigoNuncaValido)
				errores.add("codigo", new ActionMessage("errors.required","El Estado de Aprobación/Anulación "));
			if(this.estadoAprobAnul == ConstantesBD.codigoEstadoAplicacionPagosAnulado && this.motivoAprobAnul.trim().equals(""))
				errores.add("codigo", new ActionMessage("errors.required","El Motivo de Anulación "));
			if(this.fechaAprobAnul.trim().equals(""))
				errores.add("codigo", new ActionMessage("errors.required","La Fecha de Aprobación/Anulación "));
			if(!UtilidadTexto.isEmpty(this.fechaAprobAnul))
			{
				boolean centinelaErrorFechas = false;
				if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaAprobAnul))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Aprobación/Anulación "+this.fechaAprobAnul));
					centinelaErrorFechas=true;
				}
				if(!centinelaErrorFechas)
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaGenDoc, UtilidadFecha.conversionFormatoFechaAAp(this.fechaAprobAnul)))
						errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "Aprobación/Anulación "+this.fechaAprobAnul, "Generación Documento "+this.fechaGenDoc ));
				}
				if(!centinelaErrorFechas)
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(this.fechaAprobAnul), UtilidadFecha.getFechaActual()))
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Aprobación/Anulación "+this.fechaAprobAnul, "Actual "+UtilidadFecha.getFechaActual()));
				}
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
	 * @return
	 */
	public HashMap getListadoAplicaciones() {
		return listadoAplicaciones;
	}

	/**
	 * @param listadoAplicaciones
	 */
	public void setListadoAplicaciones(HashMap listadoAplicaciones) {
		this.listadoAplicaciones = listadoAplicaciones;
	}

	/**
	 * @param key
	 * @return
	 */
	public Object getListadoAplicaciones(String key)
    {
        return listadoAplicaciones.get(key);
    }
    
	/**
	 * @param key
	 * @param value
	 */
	public void setListadoAplicaciones(String key,Object value)
    {
        this.listadoAplicaciones.put(key, value);
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
	public int getDeudorDocumento() {
		return deudorDocumento;
	}

	/**
	 * @param deudorDocumento
	 */
	public void setDeudorDocumento(int deudorDocumento) {
		this.deudorDocumento = deudorDocumento;
	}

	/**
	 * @return
	 */
	public String getFechaDocumento() {
		return fechaDocumento;
	}

	/**
	 * @param fechaDocumento
	 */
	public void setFechaDocumento(String fechaDocumento) {
		this.fechaDocumento = fechaDocumento;
	}

	/**
	 * @return
	 */
	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	/**
	 * @param numeroDocumento
	 */
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	/**
	 * @return
	 */
	public int getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @param tipoDocumento
	 */
	public void setTipoDocumento(int tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	/**
	 * @return
	 */
	public HashMap getListadoFacturasAplicacion() {
		return listadoFacturasAplicacion;
	}

	/**
	 * @param listadoFacturasAplicacion
	 */
	public void setListadoFacturasAplicacion(HashMap listadoFacturasAplicacion) {
		this.listadoFacturasAplicacion = listadoFacturasAplicacion;
	}
	
	/**
	 * @param key
	 * @return
	 */
	public Object getListadoFacturasAplicacion(String key)
    {
        return listadoFacturasAplicacion.get(key);
    }
    
	/**
	 * @param key
	 * @param value
	 */
	public void setlistadoFacturasAplicacion(String key,Object value)
    {
        this.listadoFacturasAplicacion.put(key, value);
    }

	/**
	 * @return
	 */
	public int getPosicion() {
		return posicion;
	}

	/**
	 * @param posicion
	 */
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}

	/**
	 * @return
	 */
	public int getEstadoAprobAnul() {
		return estadoAprobAnul;
	}

	/**
	 * @param estadoAprobAnul
	 */
	public void setEstadoAprobAnul(int estadoAprobAnul) {
		this.estadoAprobAnul = estadoAprobAnul;
	}

	/**
	 * @return
	 */
	public String getFechaAprobAnul() {
		return fechaAprobAnul;
	}

	/**
	 * @param fechaAprobAnul
	 */
	public void setFechaAprobAnul(String fechaAprobAnul) {
		this.fechaAprobAnul = fechaAprobAnul;
	}

	/**
	 * @return
	 */
	public String getMotivoAprobAnul() {
		return motivoAprobAnul;
	}

	/**
	 * @param motivoAprobAnul
	 */
	public void setMotivoAprobAnul(String motivoAprobAnul) {
		this.motivoAprobAnul = motivoAprobAnul;
	}

	/**
	 * @return
	 */
	public String getFechaGenDoc() {
		return fechaGenDoc;
	}

	/**
	 * @param fechaGenDoc
	 */
	public void setFechaGenDoc(String fechaGenDoc) {
		this.fechaGenDoc = fechaGenDoc;
	}

	/**
	 * @return
	 */
	public int getCodAplicacionPago() {
		return codAplicacionPago;
	}

	/**
	 * @param codAplicacionPago
	 */
	public void setCodAplicacionPago(int codAplicacionPago) {
		this.codAplicacionPago = codAplicacionPago;
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
	
}