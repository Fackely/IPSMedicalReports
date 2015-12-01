package com.princetonsa.actionform.facturasVarias;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFecha;

/**
 * Fecha: Abril 2008
 * @author Mauricio Jaramillo
 */

public class PagosFacturasVariasForm extends ValidatorForm 
{

	/**
     * Variable para manejar el estado de la aplicacion.
     */
    private String estado;
    
    /**
     * Mapa que contiene todos los documentos de pagos general empresa 
     */
    private HashMap documentosPagos;
    
    /**
     * Almacena el indice por el cual se va ordenar el HashMap
     */
    private String patronOrdenar;
    
    /**
     * Almacena el ultimo indice por el cual se ordeno el HashMap
     */
    private String ultimoPatron;
    
    /**
     * Codigo del pago que se modificara.
     */
    private int indexPagoFacturasVarias;
    
    /**
     * Tipo del documento por el que se hace la busqueda(Recibos Caja - RC )
     */
    private int tipoDocBusquedaFacturasVarias;
    
    /**
     * Numero del documento por el que se hace la busqueda.
     */
    private String documentoBusquedaFacturasVarias;
    
    /**
     * Fecha del documento para hacer la busqueda.
     */
    private String fechaDocBusquedaFacturasVarias;
    
    /**
     * Deudor para busqueda.
     */
    private int deudorBusquedaFacturasVarias;
    
    /**
     * Institucion
     */
    private int institucionFacturasVarias;
    
    /**
     * Mapa para manejar los conceptos de las aplicaciones de los pagos.
     */
    private HashMap conceptosAplicacionPagosFacturasVarias;
    
    /**
     * Variable para manejar las observaciones de los conceptos
     */
    private String observacionesFacturasVarias;
    
    /**
     * Fecha de la aplicacion del pago
     */
    private String fechaAplicacionFacturasVarias;
    
    /**
     * Mapa que contiene las facturas para la busqueda de las facturas en detalle de pago
     */
    private HashMap mapaBusFacturas;
    
    /**
     * Posicion del concepto que se eliminar;
     */
    private int conEliminarFacturasVarias;
    
    /**
     * Motivo por el que se anula una aplicacion
     */
    private String motivoAnulacionFacturasVarias;
    
    /**
     * Consecutivo de la factura para la busqueda avanzada
     */
    private String facturaBusquedaAvanzadaFacturasVarias;
    
    /**
     * Mapa para manejar los pagos a nivel de facturas.
     */
    private HashMap pagosFacturaFacturasVarias; 
    
    /**
     * Boolean que me indica si la busqueda de nivel de facturas se está realizando
     * en el nivel 3(conceptos->cxc->facturas) o en facturas directamente(conceptos->facturas) 
     */
    private boolean busquedaNivelFacturas3;
    
    private boolean guardadoEncabezado;
    
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
     * ArrayList para consultar todos los conceptos de pagos
     */
    private ArrayList conceptosPagos;
    
    /**
     * String para guardar el log
     */
    private String log;
    
    /**
     * Metodo que inicializa todas las variables.
     */
    public void reset()
    {
    	this.documentosPagos = new HashMap<String, Object>();
        this.documentosPagos.put("numRegistros", "0");
        this.patronOrdenar = "";
        this.ultimoPatron = "";
        this.indexPagoFacturasVarias = ConstantesBD.codigoNuncaValido;
        this.tipoDocBusquedaFacturasVarias = ConstantesBD.codigoNuncaValido;
        this.documentoBusquedaFacturasVarias = "";
        this.fechaDocBusquedaFacturasVarias = "";
        this.deudorBusquedaFacturasVarias = ConstantesBD.codigoNuncaValido;
        this.institucionFacturasVarias = ConstantesBD.codigoNuncaValido;
        this.conceptosAplicacionPagosFacturasVarias = new HashMap<String, Object>();
        this.conceptosAplicacionPagosFacturasVarias.put("numRegistros", "0");
        this.observacionesFacturasVarias = "";
        this.mapaBusFacturas = new HashMap<String, Object>();
        this.mapaBusFacturas.put("numRegistros", "0");
        this.fechaAplicacionFacturasVarias = UtilidadFecha.getFechaActual();
        this.conEliminarFacturasVarias = ConstantesBD.codigoNuncaValido;
        this.motivoAnulacionFacturasVarias = "";
        this.facturaBusquedaAvanzadaFacturasVarias = "";
        this.pagosFacturaFacturasVarias = new HashMap<String, Object>();
        this.pagosFacturaFacturasVarias.put("numRegistros", "0");
        this.busquedaNivelFacturas3 = false;
        this.guardadoEncabezado = false;
        this.maxPageItems = 20;
        this.linkSiguiente = "";
        this.conceptosPagos = new ArrayList();
        this.log = "";
    }
    
    /**
     * @return
     */
	public boolean isBusquedaNivelFacturas3() {
		return busquedaNivelFacturas3;
	}

	/**
	 * @param busquedaNivelFacturas3
	 */
	public void setBusquedaNivelFacturas3(boolean busquedaNivelFacturas3) {
		this.busquedaNivelFacturas3 = busquedaNivelFacturas3;
	}

	/**
	 * @return
	 */
	public HashMap getConceptosAplicacionPagosFacturasVarias() {
		return conceptosAplicacionPagosFacturasVarias;
	}

	/**
	 * @param conceptosAplicacionPagosFacturasVarias
	 */
	public void setConceptosAplicacionPagosFacturasVarias(
			HashMap conceptosAplicacionPagosFacturasVarias) {
		this.conceptosAplicacionPagosFacturasVarias = conceptosAplicacionPagosFacturasVarias;
	}

	/**
     * @return Returns the conceptosAplicacionPagos.
     */
    public Object getConceptosAplicacionPagosFacturasVarias(String key)
    {
        return conceptosAplicacionPagosFacturasVarias.get(key);
    }
    
    /**
     * @param conceptosAplicacionPagos The conceptosAplicacionPagos to set.
     */
    public void setConceptosAplicacionPagosFacturasVarias(String key,Object value)
    {
        this.conceptosAplicacionPagosFacturasVarias.put(key, value);
    }
	
	/**
	 * @return
	 */
	public int getConEliminarFacturasVarias() {
		return conEliminarFacturasVarias;
	}

	/**
	 * @param conEliminarFacturasVarias
	 */
	public void setConEliminarFacturasVarias(int conEliminarFacturasVarias) {
		this.conEliminarFacturasVarias = conEliminarFacturasVarias;
	}

	/**
	 * @return
	 */
	public int getDeudorBusquedaFacturasVarias() {
		return deudorBusquedaFacturasVarias;
	}

	/**
	 * @param convenioBusquedaFacturasVarias
	 */
	public void setDeudorBusquedaFacturasVarias(int convenioBusquedaFacturasVarias) {
		this.deudorBusquedaFacturasVarias = convenioBusquedaFacturasVarias;
	}

	/**
	 * @return
	 */
	public String getDocumentoBusquedaFacturasVarias() {
		return documentoBusquedaFacturasVarias;
	}

	/**
	 * @param documentoBusquedaFacturasVarias
	 */
	public void setDocumentoBusquedaFacturasVarias(
			String documentoBusquedaFacturasVarias) {
		this.documentoBusquedaFacturasVarias = documentoBusquedaFacturasVarias;
	}

	/**
	 * @return
	 */
	public HashMap getDocumentosPagos() {
		return documentosPagos;
	}

	/**
	 * @param documentosPagos
	 */
	public void setDocumentosPagos(HashMap documentosPagos) {
		this.documentosPagos = documentosPagos;
	}

	/**
     * @return Returns the documentosPagos.
     */
    public Object getDocumentosPagos(String key)
    {
        return documentosPagos.get(key);
    }
    
    /**
     * @param documentosPagos The documentosPagos to set.
     */
    public void setDocumentosPagos(String key,Object value)
    {
        this.documentosPagos.put(key, value);
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
	public String getFacturaBusquedaAvanzadaFacturasVarias() {
		return facturaBusquedaAvanzadaFacturasVarias;
	}

	/**
	 * @param facturaBusquedaAvanzadaFacturasVarias
	 */
	public void setFacturaBusquedaAvanzadaFacturasVarias(
			String facturaBusquedaAvanzadaFacturasVarias) {
		this.facturaBusquedaAvanzadaFacturasVarias = facturaBusquedaAvanzadaFacturasVarias;
	}

	/**
	 * @return
	 */
	public String getFechaAplicacionFacturasVarias() {
		return fechaAplicacionFacturasVarias;
	}

	/**
	 * @param fechaApliacionFacturasVarias
	 */
	public void setFechaAplicacionFacturasVarias(String fechaApliacionFacturasVarias) {
		this.fechaAplicacionFacturasVarias = fechaApliacionFacturasVarias;
	}

	/**
	 * @return
	 */
	public String getFechaDocBusquedaFacturasVarias() {
		return fechaDocBusquedaFacturasVarias;
	}

	/**
	 * @param fechaDocBusquedaFacturasVarias
	 */
	public void setFechaDocBusquedaFacturasVarias(
			String fechaDocBusquedaFacturasVarias) {
		this.fechaDocBusquedaFacturasVarias = fechaDocBusquedaFacturasVarias;
	}

	/**
	 * @return
	 */
	public boolean isGuardadoEncabezado() {
		return guardadoEncabezado;
	}

	/**
	 * @param guardadoEncabezado
	 */
	public void setGuardadoEncabezado(boolean guardadoEncabezado) {
		this.guardadoEncabezado = guardadoEncabezado;
	}

	/**
	 * @return
	 */
	public int getIndexPagoFacturasVarias() {
		return indexPagoFacturasVarias;
	}

	/**
	 * @param indexPagoFacturasVarias
	 */
	public void setIndexPagoFacturasVarias(int indexPagoFacturasVarias) {
		this.indexPagoFacturasVarias = indexPagoFacturasVarias;
	}

	/**
	 * @return
	 */
	public int getInstitucionFacturasVarias() {
		return institucionFacturasVarias;
	}

	/**
	 * @param institucionFacturasVarias
	 */
	public void setInstitucionFacturasVarias(int institucionFacturasVarias) {
		this.institucionFacturasVarias = institucionFacturasVarias;
	}

	/**
	 * @return
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	/**
	 * @param linkSiguiente
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * @return
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}

	/**
	 * @param maxPageItems
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}

	/**
	 * @return
	 */
	public String getMotivoAnulacionFacturasVarias() {
		return motivoAnulacionFacturasVarias;
	}

	/**
	 * @param motivoAnulacionFacturasVarias
	 */
	public void setMotivoAnulacionFacturasVarias(
			String motivoAnulacionFacturasVarias) {
		this.motivoAnulacionFacturasVarias = motivoAnulacionFacturasVarias;
	}

	/**
	 * @return
	 */
	public String getObservacionesFacturasVarias() {
		return observacionesFacturasVarias;
	}

	/**
	 * @param observacionesFacturasVarias
	 */
	public void setObservacionesFacturasVarias(String observacionesFacturasVarias) {
		this.observacionesFacturasVarias = observacionesFacturasVarias;
	}

	/**
	 * @return
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * @return
	 */
	public HashMap getPagosFacturaFacturasVarias() {
		return pagosFacturaFacturasVarias;
	}

	/**
	 * @param pagosFacturaFacturasVarias
	 */
	public void setPagosFacturaFacturasVarias(HashMap pagosFacturaFacturasVarias) {
		this.pagosFacturaFacturasVarias = pagosFacturaFacturasVarias;
	}

	/**
     * @return Returns the pagosFactura.
     */
    public Object getPagosFacturaFacturasVarias(String key)
    {
        return pagosFacturaFacturasVarias.get(key);
    }
    
    /**
     * @param pagosFactura The pagosFactura to set.
     */
    public void setPagosFacturaFacturasVarias(String key, Object value)
    {
        this.pagosFacturaFacturasVarias.put(key, value);
    }
	
	/**
	 * @return
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @return
	 */
	public int getTipoDocBusquedaFacturasVarias() {
		return tipoDocBusquedaFacturasVarias;
	}

	/**
	 * @param tipoDocBusquedaFacturasVarias
	 */
	public void setTipoDocBusquedaFacturasVarias(int tipoDocBusquedaFacturasVarias) {
		this.tipoDocBusquedaFacturasVarias = tipoDocBusquedaFacturasVarias;
	}

	/**
	 * @return
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}

	/**
	 * @param ultimoPatron
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	/**
	 * @return
	 */
	public HashMap getMapaBusFacturas() {
		return mapaBusFacturas;
	}

	/**
	 * @param mapaBusFacturas
	 */
	public void setMapaBusFacturas(HashMap mapaBusFacturas) {
		this.mapaBusFacturas = mapaBusFacturas;
	}

	/**
	 * @param key
	 * @return
	 */
	public Object getMapaBusFacturas(String key)
    {
        return mapaBusFacturas.get(key);
    }
	
    /**
     * @param key
     * @param value
     */
	public void setMapaBusFacturas(String key,Object value)
    {
        this.mapaBusFacturas.put(key, value);
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
	 * @return the conceptosPagos
	 */
	public ArrayList getConceptosPagos() {
		return conceptosPagos;
	}

	/**
	 * @param conceptosPagos the conceptosPagos to set
	 */
	public void setConceptosPagos(ArrayList conceptosPagos) {
		this.conceptosPagos = conceptosPagos;
	}

	/**
	 * @return the log
	 */
	public String getLog() {
		return log;
	}

	/**
	 * @param log the log to set
	 */
	public void setLog(String log) {
		this.log = log;
	}
	
}