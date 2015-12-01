package com.princetonsa.actionform.inventarios;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;

public class MovimientosAlmacenesForm extends ValidatorForm 
{

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
     * Codigo del Almacen seleccionado para realizar el filtrado
     */
    private String almacen;
    
    /**
     * Indicativo E/S
     */
    private String indicativoES;
    
    /**
     * HashMap con el tipo de transacciones
     */
    private HashMap tipoTransaccion;
    
    /**
	 * String donde se almacena el valor del criterio seleccionado correspondiente a los codigos de las transacciones seleccionadas
	 */
	private String [] tipoTransaccionSeleccionado;
    
	/**
	 * Transaccion Inicial
	 */
	private String transaccionInicial;
	
	/**
	 * Transaccion Final
	 */
	private String transaccionFinal;
	
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
	 * HashMap con los resultados de la consulta de Movimientos Almacenes
	 */
	private HashMap movimientosAlmacenes;
	
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
    	this.indicativoES = "";
    	this.tipoTransaccion = new HashMap();
    	this.tipoTransaccion.put("numRegistros", "0");
    	this.tipoTransaccionSeleccionado = new String []{""};
    	this.transaccionInicial = "";
    	this.transaccionFinal = "";
    	this.fechaInicial = "";
    	this.fechaFinal = "";
    	this.tipoCodigoArticulo = "";
    	this.tipoSalida = "";
    	this.tipoReporte = "";
    	this.pathArchivoTxt = "";
	 	this.archivo = false;
	 	this.errores = false;
	 	this.zip = false;
	 	this.movimientosAlmacenes = new HashMap();
    	this.movimientosAlmacenes.put("numRegistros", "0");
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
			
			//Validamos que se haya filtrado la busqueda por fecha o por número de transacción
			if(UtilidadTexto.isEmpty((this.fechaInicial)+"") && UtilidadTexto.isEmpty((this.fechaFinal)+"") && UtilidadTexto.isEmpty((this.transaccionInicial)+"") && UtilidadTexto.isEmpty((this.transaccionFinal)+""))
			{
				errores.add("tipoReporte", new ActionMessage("error.facturasVarias.consultaPagosFacturasVarias", "criterio de búsqueda (Fechas de Transacción ó Números de Transacción)"));
				this.errores = true;
			}
			
			//Realizamos las validaciones requeridas de las fechas, siempre y cuando se hayan seleccionado
			if(!UtilidadTexto.isEmpty(this.fechaInicial.toString()) || !UtilidadTexto.isEmpty(this.fechaFinal.toString()))
			{
				boolean centinelaErrorFechas = false;
				if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaInicial.toString()))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Inicial de Transacción "+this.fechaInicial));
					centinelaErrorFechas = true;
					this.errores = true;
				}
				if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaFinal.toString()))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final de Transacción "+this.fechaFinal));
					centinelaErrorFechas=true;
					this.errores = true;
				}
				if(!centinelaErrorFechas)
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial.toString(), this.fechaFinal.toString()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial de Transacción "+this.fechaInicial, "Final de Transacción "+this.fechaFinal));
						this.errores = true;
					}
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaFinal.toString(), UtilidadFecha.getFechaActual().toString()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final de Transacción "+this.fechaFinal, "Actual "+UtilidadFecha.getFechaActual()));
						this.errores = true;
					}
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial.toString(), UtilidadFecha.getFechaActual().toString()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial de Transacción "+this.fechaInicial, "Actual "+UtilidadFecha.getFechaActual()));
						this.errores = true;
					}
				}
			}
			
			//Realizamos las validaciones requeridas de los números de transacción, siempre y cuando se hayan seleccionado
			if(UtilidadTexto.isEmpty((this.transaccionInicial)+"") && !UtilidadTexto.isEmpty((this.transaccionFinal)+""))
			{
				errores.add("transaccionInicial", new ActionMessage("errors.required", "El No. de Transacción Inicial "));
				this.errores = true;
			}
			if(!UtilidadTexto.isEmpty(this.transaccionInicial) && UtilidadTexto.isEmpty(this.transaccionFinal))
			{
				errores.add("transaccionFinal", new ActionMessage("errors.required", "El No. de Transacción Final "));
				this.errores = true;
			}
			if(!UtilidadTexto.isEmpty(this.transaccionInicial.toString()) || !UtilidadTexto.isEmpty(this.transaccionFinal.toString()))
			{
				if(Integer.parseInt(this.transaccionFinal.trim()) < Integer.parseInt(this.transaccionInicial.trim()))
				{
					errores.add("transaccionFinal",new ActionMessage("errors.debeSerNumeroMayor", "El No. de Transacción Final "+this.transaccionFinal, "el No. Transacción Inicial "+this.transaccionInicial));
					this.errores = true;
	            }
			}
			
			if(errores.isEmpty())
				this.errores = false;
			
		}
		return errores;
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
	 * @return the transaccionFinal
	 */
	public String getTransaccionFinal() {
		return transaccionFinal;
	}

	/**
	 * @param transaccionFinal the transaccionFinal to set
	 */
	public void setTransaccionFinal(String transaccionFinal) {
		this.transaccionFinal = transaccionFinal;
	}

	/**
	 * @return the transaccionInicial
	 */
	public String getTransaccionInicial() {
		return transaccionInicial;
	}

	/**
	 * @param transaccionInicial the transaccionInicial to set
	 */
	public void setTransaccionInicial(String transaccionInicial) {
		this.transaccionInicial = transaccionInicial;
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
	public HashMap getMovimientosAlmacenes() {
		return movimientosAlmacenes;
	}

	/**
	 * @param movimientosAlmacenes
	 */
	public void setMovimientosAlmacenes(HashMap movimientosAlmacenes) {
		this.movimientosAlmacenes = movimientosAlmacenes;
	}	

	/**
	 * @param key
	 * @return
	 */	
	public Object getMovimientosAlmacenes(String key){
		return movimientosAlmacenes.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setMovimientosAlmacenes(String key, Object value){
		this.movimientosAlmacenes.put(key, value);
	}

	/**
	 * @return the indicativoES
	 */
	public String getIndicativoES() {
		return indicativoES;
	}

	/**
	 * @param indicativoES the indicativoES to set
	 */
	public void setIndicativoES(String indicativoES) {
		this.indicativoES = indicativoES;
	}
	
}