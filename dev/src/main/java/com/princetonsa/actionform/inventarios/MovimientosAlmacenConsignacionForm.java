package com.princetonsa.actionform.inventarios;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;

/**
 * Anexo 684
 * Creado el 9 de Octubre de 2008
 * @author Felipe Perez Granda
 * @mail lfperez@princetonsa.com
 */

public class MovimientosAlmacenConsignacionForm extends ValidatorForm 
{

	/**
	 * Loggers de la clase ReporteReferenciaExternaForm
	 */
	Logger logger = Logger.getLogger(MovimientosAlmacenConsignacionForm.class);
	private String estado;
	
	/**
     * Variable para manejar el mensaje
     */
    private ResultadoBoolean mensaje = new ResultadoBoolean(false);
	
    /**
     * HashMap de los Centros de Atencion 
     */
    private HashMap centroAtencion;
    
    /**
     * Codigo del Centro de Atencion seleccionado para realizar el filtrado
     */
    private String codigoCentroAtencion;
	
    /**
     * HashMap de los Almacenes
     */
    private HashMap almacenes;
    
    /**
     * HashMap de los proveedores
     */
    private HashMap proveedores;
    
    /**
     * Codigo del Almacen seleccionado para realizar el filtrado
     */
    private String almacen;
    
    /**
     * Nit es el numero de identificación del proveedor
     */
    private String nit;
    
    /**
     * Nit es el numero de identificación del proveedor
     */
    private String numIngreso;
    
    /**
     * Nit es el numero de identificación del proveedor
     */
    private String proveedor;
    
    /**
     * HashMap con el tipo de transacciones
     */
    private HashMap tipoTransaccion;
    
    /**
	 * String donde se almacena el valor del criterio seleccionado correspondiente a los codigos de las transacciones seleccionadas
	 */
	private String [] tipoTransaccionSeleccionado;
    
	/**
	 * Fecha Inicial de Transaccion
	 */
	private String fechaInicial;
	
	/**
	 * Fecha Final de Transaccion
	 */
	private String fechaFinal;
	
	/**
	 * Tipo de codigo de articulo seleccionado
	 */
	private String tipoCodigoArticulo;
	
	/**
	 * Tipo de reporte seleccionado para la impresion
	 */
	private String tipoReporte;
	
    /**
     * Tipo de Salida permite generar Archivo Plano o Impresion
     */
    private String tipoSalida;
    
    /**
	 * Path completo del archivo generado
	 */
	private String pathArchivoTxt;
	
	/**
	 * Controla si se genera el archivo o no?
	 */
	private boolean archivo;
 	
	/**
	 * Sin errores en el validate
	 */
	private boolean errores;
	
	/**
	 * Valida si se genero el archivo .zip para informar al usuario
	 */
	private boolean zip;
	
	/**
	 * HashMap con los resultados de la consulta de Movimientos Almacen Consignacion
	 */
	private HashMap movimientosAlmacenConsignacion;
	
	/**
	 * Atributo que le indica a la vista si se
	 * genero el archivo plano
	 */
	private boolean operacionTrue;
	
	/**
	 * Atributo que indica donde se almaceno el archivo,
	 * este es para mostrar la ruta excata donde se genero
	 * el archivo dentro del sistema de directorios del
	 * servidor 
	 */
	private String ruta;
	
	/**
	 * atributo que indica la direccion para poder
	 * descargar el archivo
	 */
	private String urlArchivo;
	
	/**
	 * Atributo que almacena si el archivo
	 * .ZIP si se genero
	 */
	private boolean existeArchivo=false; 
	
	/**
	 * Articulo para las búsquedas
	 */
	private String articulo;
    
	/**
	 * Metodo reset
	 */
	public void reset()
	{
		this.centroAtencion = new HashMap();
    	this.centroAtencion.put("numRegistros", "0");
    	this.codigoCentroAtencion = "";
    	this.almacenes = new HashMap();
    	this.almacenes.put("numRegistros", "0");
    	this.almacen = "";
    	this.nit = "";
    	this.proveedor = "";
    	this.proveedores = new HashMap();
    	this.proveedores.put("numRegistros", "0");
    	this.tipoTransaccion = new HashMap();
    	this.tipoTransaccion.put("numRegistros", "0");
    	this.tipoTransaccionSeleccionado = new String []{""};
    	this.fechaInicial = "";
    	this.fechaFinal = "";
    	this.tipoCodigoArticulo = "";
    	this.tipoSalida = "";
    	this.tipoReporte = "";
    	this.pathArchivoTxt = "";
	 	this.archivo = false;
	 	this.errores = false;
	 	this.zip = false;
	 	this.movimientosAlmacenConsignacion = new HashMap();
    	this.movimientosAlmacenConsignacion.put("numRegistros", "0");
    	this.articulo="-1";
	}

	

	/**
	* Control de Errores (Validaciones)
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		if(this.estado.equals("buscar"))
		{
			//Validamos los campos requeridos
			if(this.codigoCentroAtencion.trim().equals("") || this.codigoCentroAtencion.trim().equals("null"))
			{
				errores.add("centroAtencion", new ActionMessage("errors.required", "El Centro de Atención "));
				this.errores = true;
			}
			if(this.tipoSalida.trim().equals("") || this.tipoSalida.trim().equals("null"))
			{
				errores.add("tipoSalida", new ActionMessage("errors.required", "El Tipo de Salida "));
				this.errores = true;
			}
			if(this.tipoCodigoArticulo.trim().equals("") || this.tipoCodigoArticulo.trim().equals("null"))
			{
				errores.add("tipoCodigoArticulo", new ActionMessage("errors.required", "El Tipo Código Artículo "));
				this.errores = true;
			}
			if(this.tipoReporte.trim().equals("") || this.tipoReporte.trim().equals("null"))
			{
				errores.add("tipoReporte", new ActionMessage("errors.required", "El Tipo Reporte "));
				this.errores = true;
			}
			logger.info("===> Aquí vamos a validar las fechas !!!");
			logger.info("===> fecha Inicial: "+this.getFechaInicial());
			logger.info("===> fecha Final: "+this.getFechaFinal());
			
			//Validamos is la fecha inicial viene vacía
			if(this.getFechaInicial().equals(""))
			{
				logger.info("===> La fecha inicial está vacía");
				errores.add("", new ActionMessage("errors.required", "La Fecha Inicial "+this.fechaInicial));
				this.errores = true;
			}
			
			//Validamos is la fecha final viene vacía
			if(this.getFechaFinal().equals(""))
			{
				logger.info("===> La fecha final está vacía");
				errores.add("", new ActionMessage("errors.required", "La Fecha Final "+this.fechaFinal));
				this.errores = true;
			}
						
			//Realizamos las validaciones requeridas de las fechas, siempre y cuando se hayan seleccionado
			if(!UtilidadTexto.isEmpty(this.fechaInicial.toString()) || !UtilidadTexto.isEmpty(this.fechaFinal.toString()))
			{
				boolean centinelaErrorFechas = false;
				if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaInicial.toString()))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Inicial "+this.fechaInicial));
					centinelaErrorFechas = true;
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
				}
			}
			
			if(errores.isEmpty())
				this.errores = false;
			
		}
		return errores;
	}

	/**
	 * Get de Proveedores
	 * @return proveedores
	 */
	public HashMap getProveedores() {
		return proveedores;
	}

	/**
	 * Set de provedores
	 * @param proveedores
	 */
	public void setProveedores(HashMap proveedores) {
		this.proveedores = proveedores;
	}

	/**
	 * @return the almacen
	 */
	public String getAlmacen() {
		return almacen;
	}

	/**
	 * @param almacen the almacen to set
	 */
	public void setAlmacen(String almacen) {
		this.almacen = almacen;
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
	public Object getAlmacenes(String key){
		return almacenes.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setAlmacenes(String key, Object value){
		this.almacenes.put(key, value);
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
	public Object getCentroAtencion(String key){
		return centroAtencion.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setCentroAtencion(String key, Object value){
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
	 * @return the tipoCodigoArticulo
	 */
	public String getTipoCodigoArticulo() {
		return tipoCodigoArticulo;
	}

	/**
	 * @param tipoCodigoArticulo the tipoCodigoArticulo to set
	 */
	public void setTipoCodigoArticulo(String tipoCodigoArticulo) {
		this.tipoCodigoArticulo = tipoCodigoArticulo;
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
	 * @return the tipoTransaccion
	 */
	public HashMap getTipoTransaccion() {
		return tipoTransaccion;
	}

	/**
	 * @param tipoTransaccion the tipoTransaccion to set
	 */
	public void setTipoTransaccion(HashMap tipoTransaccion) {
		this.tipoTransaccion = tipoTransaccion;
	}

	/**
	 * @param key
	 * @return
	 */	
	public Object getTipoTransaccion(String key){
		return tipoTransaccion.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setTipoTransaccion(String key, Object value){
		this.tipoTransaccion.put(key, value);
	}
	
	/**
	 * @return the tipoTransaccionSeleccionado
	 */
	public String[] getTipoTransaccionSeleccionado() {
		return tipoTransaccionSeleccionado;
	}

	/**
	 * @param tipoTransaccionSeleccionado the tipoTransaccionSeleccionado to set
	 */
	public void setTipoTransaccionSeleccionado(String[] tipoTransaccionSeleccionado) {
		this.tipoTransaccionSeleccionado = tipoTransaccionSeleccionado;
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
	 * @return
	 */
	public HashMap getMovimientosAlmacenConsignacion() {
		return movimientosAlmacenConsignacion;
	}

	/**
	 * @param movimientosAlmacenes
	 */
	public void setMovimientosAlmacenConsignacion(HashMap movimientosAlmacenConsignacion) {
		this.movimientosAlmacenConsignacion = movimientosAlmacenConsignacion;
	}	

	/**
	 * @param key
	 * @return
	 */	
	public Object getMovimientosAlmacenConsignacion(String key){
		return movimientosAlmacenConsignacion.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setMovimientosAlmacenConsignacion(String key, Object value){
		this.movimientosAlmacenConsignacion.put(key, value);
	}

	/**
	 * 
	 * @param nit
	 */
	public void setNit(String nit) {
		this.nit = nit;
	}

	/**
	 * 
	 * @return nit
	 */
	public String getNit() {
		return nit;
	}

	/**
	 * 
	 * @param proveedor
	 */
	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}

	/**
	 * 
	 * @return
	 */
	public String getProveedor() {
		return proveedor;
	}

	public void setNumIngreso(String numIngreso) {
		this.numIngreso = numIngreso;
	}

	public String getNumIngreso() {
		return numIngreso;
	}

	//---------------operacion true ------------------------------------------
	public boolean isOperacionTrue() {
		return operacionTrue;
	}
	public void setOperacionTrue(boolean operacionTrue) {
		this.operacionTrue = operacionTrue;
	}
	//--------------------------------------------------------------------------
	//-------ruta --------------------------------------------------------------
	public String getRuta() {
		return ruta;
	}
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}
	//----------------------------------------------------------------------------
	//-------------url archivo----------------------------------------------------
	public String getUrlArchivo() {
		return urlArchivo;
	}
	public void setUrlArchivo(String urlArchivo) {
		this.urlArchivo = urlArchivo;
	}
	//-----------------------------------------------------------------------------
	//--------existe archivo ------------------------------------------------------
	public boolean isExisteArchivo() {
		return existeArchivo;
	}
	public void setExisteArchivo(boolean existeArchivo) {
		this.existeArchivo = existeArchivo;
	}
	//------------------------------------------------------------------------------
	
	/*
	 * Artículo Búsqueda
	 */
	public String getArticulo() {
		return articulo;
	}
	public void setArticulo(String articulo) {
		this.articulo = articulo;
	}
}