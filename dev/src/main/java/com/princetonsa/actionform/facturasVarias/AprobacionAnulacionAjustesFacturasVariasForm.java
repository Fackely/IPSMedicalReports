package com.princetonsa.actionform.facturasVarias;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.mundo.facturasVarias.GeneracionModificacionAjustesFacturasVarias;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;

/**
 * @author Mauricio Jaramillo H.
 * Fecha: Agosto de 2008
 */

public class AprobacionAnulacionAjustesFacturasVariasForm extends ValidatorForm
{

	private String estado;
	
	/**
     * Mensaje que informa sobre la generacion de la aplicacion de pagos facturas varias
     */
    private ResultadoBoolean mensaje = new ResultadoBoolean(false);
	
    /**
     * Mapa que contiene todas los ajustes de facturas varias en estado pendiente  
     */
    private HashMap listadoAjustesFacturasVarias;
    
    /**
     * Mapa que contiene el ajuste seleccionado
     */
    private HashMap ajustesSeleccionado = new HashMap();
    
    /**
     * Número de la posición que se seleccionada para aprobar o anular el ajuste
     */
    private int posicion;
    
    /**
     * String que almacena el motivo de aprobación/anulación del ajuste de facturas varias
     */
    private String motivoAprobAnul = "";
    
    /**
     * String que almacena la fecha de aprobación/anulación del ajuste de facturas varias
     */
    private String fechaAprobAnul = "";
    
    /**
     * String que almacena el estado de la aporbación/anulación del ajuste de facturas varias
     */
    private String estadoAprobAnul = "";
    
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
     * String para ordenar por un nuevo patron
     */
    private String patronOrdenar;
	
	/**
	 * String que almacena el ordenamiento del ultimo patron ordenado 
	 */
    private String ultimoPatron;
    
    /**
     * Boolean que permite evaluar si se carga un ajuste desde la búsqueda avanzada
     * o desde el listado principal de la funcionalidad
     */
    private boolean cargaListadoPrincipal;
    
    //*********************INICIO COPIA PARA MANEJAR LA BUSQUEDA AVANZADA DE LA GENERACIÓN*************************
    /*--------------------------------------------------------------------
	 * INDICES PARA EL MANEJO DE LOS KEY'S DE LOS MAPAS.
	 ---------------------------------------------------------------------*/
	private String [] indicesAjustes = GeneracionModificacionAjustesFacturasVarias.indicesAjustesFacturasVarias;
	
	private String [] indicesFacturasVarias = GeneracionModificacionAjustesFacturasVarias.indicesFacturasVarias;
	
	/**
	 * Mapa que almacena los diferentes filtros que puede
	 * contener la busqueda de ajustes a facturas.
	 * este mapa no tiene y no necita un key numRegistros.
	 */
	private HashMap filtrosBusqueda;
	
	/**
	 * Mapa que almacena la informacion del ajuste.
	 * este mapa no necita un key numRegistros.
	 */
	private HashMap mapaAjustes;
	
	/**
	 * Mapa que almacena la informacion de la factura
	 */
	private HashMap mapaInfoFac;
	
	/**
	 * Listado con la informacion de la consulta
	 */
	private HashMap listado;
	
	/**
	 * String encargado de manejar la posicion dentro del
	 * mapa de la busqueda.
	 */
	private String index;
	
	private String codigoDeudor;
	private String descripDeudor;
	private String tipoDeudor;
	
	//-----------------------------------------------------------------------
	//********************FIN COPIA PARA MANEJAR LA BUSQUEDA AVANZADA DE LA GENERACIÓN*****************************
    
    /**
	 * Metodo reset
	 */
	public void reset()
	{
		this.listadoAjustesFacturasVarias = new HashMap<String, Object>();
		this.ajustesSeleccionado = new HashMap();
        this.listadoAjustesFacturasVarias.put("numRegistros", "0");
        this.filtrosBusqueda = new HashMap<String, Object>();
        this.filtrosBusqueda.put("numRegistros", "0");
        this.mapaAjustes = new HashMap<String, Object>();
        this.mapaAjustes.put("numRegistros", "0");
        this.mapaInfoFac = new HashMap<String, Object>();
        this.mapaInfoFac.put("numRegistros", "0");
        this.listado = new HashMap<String, Object>();
        this.listado.put("numRegistros", "0");
        this.maxPageItems = 20;
        this.linkSiguiente = "";
        this.posicion = ConstantesBD.codigoNuncaValido;
        this.motivoAprobAnul = "";
        this.fechaAprobAnul = UtilidadFecha.getFechaActual();
        this.estadoAprobAnul = "";
        this.patronOrdenar = "";
        this.ultimoPatron = "";
        this.index = ConstantesBD.codigoNuncaValido+"";
        this.cargaListadoPrincipal = false;
        this.codigoDeudor="";
        this.descripDeudor="";
        this.tipoDeudor="";
	}
	
	/**
	 * Método reset campos de la aprobación y/o anulación ajustes facturas varias
	 */
	public void resetAprobacionAnulacion()
	{
		this.motivoAprobAnul = "";
        this.fechaAprobAnul = UtilidadFecha.getFechaActual();
        this.estadoAprobAnul = "";
	}
	
	/**
	 * Método que hace reset al mapa listado
	 * de la búsqueda avanzada
	 */
	public void resetBusqueda()
	{
		this.listado = new HashMap<String, Object>();
        this.listado.put("numRegistros", "0");
	}
    
	/**
	* Control de Errores (Validaciones)
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		if(this.estado.equals("guardar"))
		{
			if(this.estadoAprobAnul.trim().equals("") || this.estadoAprobAnul.trim().equals("null"))
				errores.add("estadoAprobAnul", new ActionMessage("errors.required","El Estado de Aprobación/Anulación "));
			if(this.estadoAprobAnul.trim().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado) && this.motivoAprobAnul.trim().equals(""))
				errores.add("codigo", new ActionMessage("errors.required","El Motivo de Anulación "));
			if(this.fechaAprobAnul.trim().equals(""))
				errores.add("codigo", new ActionMessage("errors.required","La Fecha de Aprobación/Anulación "));
			if(!UtilidadTexto.isEmpty(this.fechaAprobAnul) || this.fechaAprobAnul.trim().equals("null"))
			{
				boolean centinelaErrorFechas = false;
				if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaAprobAnul))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Aprobación/Anulación "+this.fechaAprobAnul));
					centinelaErrorFechas=true;
				}
				if(!centinelaErrorFechas)
				{
					//Si la carga es desde el listado principal se valida con la fecha del mapa listadoAjustesFacturasVarias
					if(this.isCargaListadoPrincipal())
					{
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getListadoAjustesFacturasVarias("fechaajuste_"+this.getPosicion())+"", this.fechaAprobAnul))
							errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "Aprobación/Anulación "+this.fechaAprobAnul, "del Ajuste "+this.getListadoAjustesFacturasVarias("fechaajuste_"+this.getPosicion())+""));
					}
					//Si la carga es desde la búsqueda avanzada implementada desde la funcionalidad Generación/Modificación Ajustes Facturas Varias se valida con la fecha del mapa listado
					else
					{
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getListado("fechaAjuste3_"+this.getIndex())+"", this.fechaAprobAnul))
							errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "Aprobación/Anulación "+this.fechaAprobAnul, "del Ajuste "+this.getListado("fechaAjuste3_"+this.getIndex())+""));
					}
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

	
	
	
	public String getCodigoDeudor() {
		return codigoDeudor;
	}

	public void setCodigoDeudor(String codigoDeudor) {
		this.codigoDeudor = codigoDeudor;
	}

	public String getDescripDeudor() {
		return descripDeudor;
	}

	public void setDescripDeudor(String descripDeudor) {
		this.descripDeudor = descripDeudor;
	}

	public String getTipoDeudor() {
		return tipoDeudor;
	}

	public void setTipoDeudor(String tipoDeudor) {
		this.tipoDeudor = tipoDeudor;
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
	 * @return
	 */
	public HashMap getListadoAjustesFacturasVarias() {
		return listadoAjustesFacturasVarias;
	}

	/**
	 * @param listadoAjustesFacturasVarias
	 */
	public void setListadoAjustesFacturasVarias(HashMap listadoAjustesFacturasVarias) {
		this.listadoAjustesFacturasVarias = listadoAjustesFacturasVarias;
	}

	/**
	 * @param key
	 * @return
	 */
	public Object getListadoAjustesFacturasVarias(String key) {
        return listadoAjustesFacturasVarias.get(key);
    }
    
	/**
	 * @param key
	 * @param value
	 */
	public void setListadoAjustesFacturasVarias(String key,Object value) {
        this.listadoAjustesFacturasVarias.put(key, value);
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
	 * @return the posicion
	 */
	public int getPosicion() {
		return posicion;
	}

	/**
	 * @param posicion the posicion to set
	 */
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}

	/**
	 * @return the motivoAprobAnul
	 */
	public String getMotivoAprobAnul() {
		return motivoAprobAnul;
	}

	/**
	 * @param motivoAprobAnul the motivoAprobAnul to set
	 */
	public void setMotivoAprobAnul(String motivoAprobAnul) {
		this.motivoAprobAnul = motivoAprobAnul;
	}

	/**
	 * @return the fechaAprobAnul
	 */
	public String getFechaAprobAnul() {
		return fechaAprobAnul;
	}

	/**
	 * @param fechaAprobAnul the fechaAprobAnul to set
	 */
	public void setFechaAprobAnul(String fechaAprobAnul) {
		this.fechaAprobAnul = fechaAprobAnul;
	}

	/**
	 * @return the estadoAprobAnul
	 */
	public String getEstadoAprobAnul() {
		return estadoAprobAnul;
	}

	/**
	 * @param estadoAprobAnul the estadoAprobAnul to set
	 */
	public void setEstadoAprobAnul(String estadoAprobAnul) {
		this.estadoAprobAnul = estadoAprobAnul;
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
	
	//-----listado-----------------------------------------------------
	public HashMap getListado() {
		return listado;
	}

	public void setListado(HashMap listado) {
		this.listado = listado;
	}
	
	public Object getListado(String key) {
		return listado.get(key);
	}

	public void setListado(String key,Object value) {
		this.listado.put(key, value);
	}
	//------------------------------------------------------------------
	
	// ---filtroBusqueda--------------------------------------------------
	public HashMap getFiltrosBusqueda() {
		return filtrosBusqueda;
	}

	public void setFiltrosBusqueda(HashMap filtrosBusqueda) {
		this.filtrosBusqueda = filtrosBusqueda;
	}

	public Object getFiltrosBusqueda(String key) {
		return filtrosBusqueda.get(key);
	}

	public void setFiltrosBusqueda(String key,Object value) {
		this.filtrosBusqueda.put(key, value);
	}
	//----------------------------------------------------------------------
	
	// ---mapaAjustes--------------------------------------------------------
	public HashMap getMapaAjustes() {
		return mapaAjustes;
	}

	public void setMapaAjustes(HashMap mapaAjustes) {
		this.mapaAjustes = mapaAjustes;
	}
	
	public Object getMapaAjustes(String key) {
		return mapaAjustes.get(key);
	}

	public void setMapaAjustes(String key,Object value) {
		this.mapaAjustes.put(key, value);
	}
	//---------------------------------------------------------------------
	
	// ---mapaInfoFac--------------------------------------------------------

	public HashMap getMapaInfoFac() {
		return mapaInfoFac;
	}

	public void setMapaInfoFac(HashMap mapaInfoFac) {
		this.mapaInfoFac = mapaInfoFac;
	}

	public Object getMapaInfoFac(String key	) {
		return mapaInfoFac.get(key);
	}

	public void setMapaInfoFac(String key,Object value) {
		this.mapaInfoFac.put(key, value);
	}
	//----------------------------------------------------------------------

	/**
	 * @return the index
	 */
	public String getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(String index) {
		this.index = index;
	}

	/**
	 * @return the cargaListadoPrincipal
	 */
	public boolean isCargaListadoPrincipal() {
		return cargaListadoPrincipal;
	}

	/**
	 * @param cargaListadoPrincipal the cargaListadoPrincipal to set
	 */
	public void setCargaListadoPrincipal(boolean cargaListadoPrincipal) {
		this.cargaListadoPrincipal = cargaListadoPrincipal;
	}

	/**
	 * @return the ajustesSeleccionado
	 */
	public HashMap getAjustesSeleccionado() {
		return ajustesSeleccionado;
	}

	/**
	 * @param ajustesSeleccionado the ajustesSeleccionado to set
	 */
	public void setAjustesSeleccionado(HashMap ajustesSeleccionado) {
		this.ajustesSeleccionado = ajustesSeleccionado;
	}
	
	/**
	 * @return the ajustesSeleccionado
	 */
	public Object getAjustesSeleccionado(String key) {
		return ajustesSeleccionado.get(key);
	}

	/**
	 * @param ajustesSeleccionado the ajustesSeleccionado to set
	 */
	public void setAjustesSeleccionado(String key,Object obj) {
		this.ajustesSeleccionado.put(key, obj);
	}

	/**
	 * @return the indicesAjustes
	 */
	public String[] getIndicesAjustes() {
		return indicesAjustes;
	}

	/**
	 * @param indicesAjustes the indicesAjustes to set
	 */
	public void setIndicesAjustes(String[] indicesAjustes) {
		this.indicesAjustes = indicesAjustes;
	}
	
}