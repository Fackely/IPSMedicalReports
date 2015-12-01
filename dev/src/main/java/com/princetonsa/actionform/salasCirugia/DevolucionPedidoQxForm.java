package com.princetonsa.actionform.salasCirugia;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;

/**
 * @author Mauricio Jllo
 * Fecha: Junio de 2008
 */

public class DevolucionPedidoQxForm extends ValidatorForm
{

	private String estado;

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
     * Patron para realizar el ordenamiento
     */
    private String patronOrdenar;
	
    /**
     * Ultimo patron por el que se realizo el ordenamiento
     */
    private String ultimoPatron;
    
    /**
     * Mapa que guarda el listado de las solicitudes por paciente
     */
    private HashMap listadoSolicitudes;
    
    /**
     * Mapa que guarda el listado de los pedidos por paciente
     */
    private HashMap<String,Object> listadoPeticiones;
    
    /**
     * Mapa que guarda el listado de los pedidos por paciente
     */
    private HashMap<String,String> listadoPedidos;
    
    /**
     * Posicion seleccionada en el listado de solicitudes
     */
    private int posicion;
    
    /**
     * HashMap que guarda el detalle de las cirugias de la solicitud seleccionada
     */
    private HashMap detalleSolicitudCirugia;
    
    /**
     * HashMap que guarda el detalle de los servicios de la peticion seleccionada
     */
    private HashMap<String,Object> detallePeticionServicios;
    
    /**
     * HashMap que guarda el detalle de los articulos de cada una de las cirugias de la peticion
     */
    private HashMap<String,Object> detallePeticionArticulos;
    
    /**
     * HashMap de los Centros de Costo 
     */
    private HashMap centroCosto;
    
    /**
     * Codigo del Centro de Costo que Ejecuta seleccionado para realizar el filtrado
     */
    private String codigoCentroCostoEjecuta;
    
    /**
     * Codigo del Centro de Costo que Devuelve seleccionado para realizar el filtrado
     */
    private String codigoCentroCostoDevuelve;
    
    /**
     * Fecha Inicial cuando la devolucion es por rango
     */
    private String fechaInicial;
    
    /**
     * Fecha Final cuando la devolucion es por rango
     */
    private String fechaFinal;
    
    /**
     * Boolean que indica si la funcionalidad es por paciente o por rangos
     * para validar el cargado del paciente en sesion
     */
    private boolean esPorPaciente;
    
    /**
     * ArrayList para los motivos de devolucion de inventarios
     */
    private ArrayList motivoDevolucion;
    
    /**
     * Codigo del motivo de devolucion seleccionado
     */
    private String motivoDevolucionSeleccionado;
    
    /**
     * Nombre del motivo de devolucion seleccionado
     */
    private String nombreMotivoDevolucionSeleccionado;
    
    /**
     * Obsevaciones de la devolucion de pedidos seleccionado
     */
    private String observaciones;
    
    /**
     * HashMap para el manejo de la informacion de los almacenes 
     */
    private HashMap almacenes;
    
    /**
     * Posicion del articulo al cual se le pretende abrir el pop-up de almacenes
     */
    private int posicionArticulo;
    
    /**
     * Cantidad ha devolver en el articulo para poder validar la sumatoria de las cantidades de los almacenes 
     */
    private String cantidad;
    
    /**
     * Maneja si el check Devolver Todas fue seleccionado o no?
     */
    //private String devolverTodas;
    
    /**
     * Mensaje que informa sobre la generacion de la aplicacion de pagos facturas varias
     */
    private ResultadoBoolean mensaje = new ResultadoBoolean(false);
    
    /**
     * String que guarda los codigos de la devolucion guardados
     */
    private String codigosDevolucion;
    
    private boolean pacienteDummy = false;
    
    private boolean pedidosVarios = false;
    
    /**
     * Metodo que inicializa todas las variables.
     */
    public void reset()
    {
    	this.maxPageItems = 20;
        this.linkSiguiente = "";
        this.patronOrdenar = "";
    	this.ultimoPatron = "";
    	this.listadoSolicitudes = new HashMap();
    	this.listadoSolicitudes.put("numRegistros", "0");
    	this.listadoPeticiones = new HashMap<String, Object>();
    	this.listadoPeticiones.put("numRegistros","0");
    	this.listadoPedidos = new HashMap<String,String>();
    	this.listadoPedidos.put("numRegistros", "0");
    	this.posicion = ConstantesBD.codigoNuncaValido;
    	this.detalleSolicitudCirugia = new HashMap();
    	this.detalleSolicitudCirugia.put("numRegistros", "0");
    	this.detallePeticionServicios = new HashMap<String, Object>();
    	this.detallePeticionServicios.put("numRegistros", "0");
    	this.detallePeticionArticulos = new HashMap<String,Object>();
    	this.detallePeticionArticulos.put("numRegistros", "0");
    	this.centroCosto = new HashMap();
    	this.centroCosto.put("numRegistros", "0");
    	this.codigoCentroCostoEjecuta = "";
    	this.codigoCentroCostoDevuelve = "";
    	this.fechaInicial = "";
    	this.fechaFinal = "";
    	this.esPorPaciente = false;
    	this.motivoDevolucion = new ArrayList();
    	this.motivoDevolucionSeleccionado = "";
    	this.observaciones = "";
    	this.almacenes = new HashMap();
    	this.almacenes.put("numRegistros", "0");
    	this.posicionArticulo = ConstantesBD.codigoNuncaValido;
    	this.nombreMotivoDevolucionSeleccionado = "";
    	this.cantidad = "";
    	this.codigosDevolucion = "";
    	//this.devolverTodas = "";
    }
	
    /**
     * Validate
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
    	ActionErrors errores = new ActionErrors();
    	if(this.estado.equals("buscarRangos"))
    	{
    		if(this.codigoCentroCostoDevuelve.trim().equals("") || this.codigoCentroCostoDevuelve.trim().equals("null"))
				errores.add("Centro de Costo", new ActionMessage("errors.required","El Centro de Costo que Devuelve "));
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
	 * @return the listadoSolicitudes
	 */
	public HashMap getListadoSolicitudes() {
		return listadoSolicitudes;
	}

	/**
	 * @param listadoSolicitudes the listadoSolicitudes to set
	 */
	public void setListadoSolicitudes(HashMap listadoSolicitudes) {
		this.listadoSolicitudes = listadoSolicitudes;
	}

	/**
	 * @param key
	 * @return
	 */	
	public Object getListadoSolicitudes(String key) 
	{
		return listadoSolicitudes.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setListadoSolicitudes(String key, Object value) 
	{
		this.listadoSolicitudes.put(key, value);
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
	 * @return the detalleSolicitudCirugia
	 */
	public HashMap getDetalleSolicitudCirugia() {
		return detalleSolicitudCirugia;
	}

	/**
	 * @param detalleSolicitudCirugia the detalleSolicitudCirugia to set
	 */
	public void setDetalleSolicitudCirugia(HashMap detalleSolicitudCirugia) {
		this.detalleSolicitudCirugia = detalleSolicitudCirugia;
	}

	/**
	 * @param key
	 * @return
	 */	
	public Object getDetalleSolicitudCirugia(String key) 
	{
		return detalleSolicitudCirugia.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setDetalleSolicitudCirugia(String key, Object value) 
	{
		this.detalleSolicitudCirugia.put(key, value);
	}
	
	/**
	 * @return the detallePeticionServicios
	 */
	public HashMap<String,Object> getDetallePeticionServicios() {
		return detallePeticionServicios;
	}

	/**
	 * @param detallePeticionServicios the detallePeticionServicios to set
	 */
	public void setDetallePeticionServicios(HashMap<String,Object> detallePeticionServicios) {
		this.detallePeticionServicios = detallePeticionServicios;
	}

	/**
	 * @param key
	 * @return
	 */	
	public Object getDetallePeticionServicios(String key) 
	{
		return detallePeticionServicios.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setDetallePeticionServicios(String key, Object value) 
	{
		this.detallePeticionServicios.put(key, value);
	}
	/**
	 * @return the detallePeticionArticulos
	 */
	public HashMap getDetallePeticionArticulos() {
		return detallePeticionArticulos;
	}

	/**
	 * @param detallePeticionArticulos the detallePeticionArticulos to set
	 */
	public void setDetallePeticionArticulos(HashMap<String,Object> detallePeticionArticulos) {
		this.detallePeticionArticulos = detallePeticionArticulos;
	}

	/**
	 * @param key
	 * @return
	 */	
	public Object getDetallePeticionArticulos(String key) 
	{
		return detallePeticionArticulos.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setDetallePeticionArticulos(String key, Object value) 
	{
		this.detallePeticionArticulos.put(key, value);
	}
	
	/**
	 * @return the centroCosto
	 */
	public HashMap getCentroCosto() {
		return centroCosto;
	}

	/**
	 * @param centroCosto the centroCosto to set
	 */
	public void setCentroCosto(HashMap centroCosto) {
		this.centroCosto = centroCosto;
	}

	/**
	 * @param key
	 * @return
	 */	
	public Object getCentroCosto(String key) 
	{
		return centroCosto.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setCentroCosto(String key, Object value) 
	{
		this.centroCosto.put(key, value);
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
	 * @return the codigoCentroCostoDevuelve
	 */
	public String getCodigoCentroCostoDevuelve() {
		return codigoCentroCostoDevuelve;
	}

	/**
	 * @param codigoCentroCostoDevuelve the codigoCentroCostoDevuelve to set
	 */
	public void setCodigoCentroCostoDevuelve(String codigoCentroCostoDevuelve) {
		this.codigoCentroCostoDevuelve = codigoCentroCostoDevuelve;
	}

	/**
	 * @return the codigoCentroCostoEjecuta
	 */
	public String getCodigoCentroCostoEjecuta() {
		return codigoCentroCostoEjecuta;
	}

	/**
	 * @param codigoCentroCostoEjecuta the codigoCentroCostoEjecuta to set
	 */
	public void setCodigoCentroCostoEjecuta(String codigoCentroCostoEjecuta) {
		this.codigoCentroCostoEjecuta = codigoCentroCostoEjecuta;
	}

	/**
	 * @return the esPorPaciente
	 */
	public boolean isEsPorPaciente() {
		return esPorPaciente;
	}

	/**
	 * @param esPorPaciente the esPorPaciente to set
	 */
	public void setEsPorPaciente(boolean esPorPaciente) {
		this.esPorPaciente = esPorPaciente;
	}

	/**
	 * @return the motivoDevolucion
	 */
	public ArrayList getMotivoDevolucion() {
		return motivoDevolucion;
	}

	/**
	 * @param motivoDevolucion the motivoDevolucion to set
	 */
	public void setMotivoDevolucion(ArrayList motivoDevolucion) {
		this.motivoDevolucion = motivoDevolucion;
	}

	/**
	 * @return the motivoDevolucionSeleccionado
	 */
	public String getMotivoDevolucionSeleccionado() {
		return motivoDevolucionSeleccionado;
	}

	/**
	 * @param motivoDevolucionSeleccionado the motivoDevolucionSeleccionado to set
	 */
	public void setMotivoDevolucionSeleccionado(String motivoDevolucionSeleccionado) {
		this.motivoDevolucionSeleccionado = motivoDevolucionSeleccionado;
	}

	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	/**
	 * @return the almacenes
	 */
	public HashMap getAlmacenes() {
		return almacenes;
	}

	/**
	 * @param almacenes the almacenes to set
	 */
	public void setAlmacenes(HashMap almacenes) {
		this.almacenes = almacenes;
	}

	/**
	 * @param key
	 * @return
	 */	
	public Object getAlmacenes(String key) 
	{
		return almacenes.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setAlmacenes(String key, Object value) 
	{
		this.almacenes.put(key, value);
	}

	/**
	 * @return the posicionArticulo
	 */
	public int getPosicionArticulo() {
		return posicionArticulo;
	}

	/**
	 * @param posicionArticulo the posicionArticulo to set
	 */
	public void setPosicionArticulo(int posicionArticulo) {
		this.posicionArticulo = posicionArticulo;
	}

	/**
	 * @return the nombreMotivoDevolucionSeleccionado
	 */
	public String getNombreMotivoDevolucionSeleccionado() {
		return nombreMotivoDevolucionSeleccionado;
	}

	/**
	 * @param nombreMotivoDevolucionSeleccionado the nombreMotivoDevolucionSeleccionado to set
	 */
	public void setNombreMotivoDevolucionSeleccionado(
			String nombreMotivoDevolucionSeleccionado) {
		this.nombreMotivoDevolucionSeleccionado = nombreMotivoDevolucionSeleccionado;
	}

	/**
	 * @return the cantidad
	 */
	public String getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
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
	 * @return the codigosDevolucion
	 */
	public String getCodigosDevolucion() {
		return codigosDevolucion;
	}

	/**
	 * @param codigosDevolucion the codigosDevolucion to set
	 */
	public void setCodigosDevolucion(String codigosDevolucion) {
		this.codigosDevolucion = codigosDevolucion;
	}

	public HashMap<String, String> getListadoPedidos() {
		return listadoPedidos;
	}

	public void setListadoPedidos(HashMap<String, String> listadoPedidos) {
		this.listadoPedidos = listadoPedidos;
	}
	
	/**
	 * @return the listadoPeticiones
	 */
	public HashMap<String, Object> getListadoPeticiones() {
		return listadoPeticiones;
	}
	
	/**
	 * @param listadoPeticiones the listadoPeticiones to set
	 */
	public void setListadoPeticiones(HashMap<String, Object> listadoPeticiones) {
		this.listadoPeticiones = listadoPeticiones;
	}
	
	/**
	 * @param key
	 * @return
	 */	
	public Object getListadoPeticiones(String key) 
	{
		return listadoPeticiones.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setListadoPeticiones(String key, Object value) 
	{
		this.listadoPeticiones.put(key, value);
	}

	public boolean isPacienteDummy() {
		return pacienteDummy;
	}

	public void setPacienteDummy(boolean pacienteDummy) {
		this.pacienteDummy = pacienteDummy;
	}

	public boolean isPedidosVarios() {
		return pedidosVarios;
	}

	public void setPedidosVarios(boolean pedidosVarios) {
		this.pedidosVarios = pedidosVarios;
	}

	/**
	 * @return the devolverTodas
	 */
	/*public String getDevolverTodas() {
		return devolverTodas;
	}*/

	/**
	 * @param devolverTodas the devolverTodas to set
	 */
	/*public void setDevolverTodas(String devolverTodas) {
		this.devolverTodas = devolverTodas;
	}*/
	
	
}