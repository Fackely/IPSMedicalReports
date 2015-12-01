package com.princetonsa.actionform.facturasVarias;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

/**
 * @author Mauricio Jllo
 * Fecha: Agosto de 2008
 */
public class ConsultaMovimientoDeudorForm extends ValidatorForm
{

	private String estado;

	/**
	 * Fecha Inicial
	 */
	private String fechaInicial;
	
	/**
	 * Fecha Final
	 */
	private String fechaFinal;
	
	/**
	 * String para el tipo de deudor seleccionado
	 */
	private String tipoDeudor;
	
	/**
	 * ArrayList con la toda la información de empresas o deudores parametrizada en el sistema
	 */
	private ArrayList<HashMap<String,Object>> deudores;
	
	/**
	 * Código del deudor seleccionado
	 */
	private String codigoDeudor;
	
	private String descripDeudor;
	/**
	 * HashMap con los resultados de la consulta de movimientos por deudor
	 */
	private HashMap consultaMovimientoDeudor;
	
	/**
	 * HashMap con los resultados de la consulta del detalle de los movimientos por deudor
	 */
	private HashMap consultaDetalleMovimientoDeudor;
	
	/****************************************
     * ATRIBUTOS PARA EL PAGER
     ****************************************/
	
	/**
     * Para controlar la página actual del pager.
     */
    private int offset;
    
    /**
     * Para controlar el link siguiente del pager 
     */
    private String linkSiguiente;
    
    /**
     * Atributo para el manejo de la paginacion con memoria 
     */
    private int currentPageNumber;
    
    /****************************************
     * FIN ATRIBUTOS PARA EL PAGER
     ****************************************/
    
    /**
     * Posicion del Registro Seleccionado
     */
    private int posicion;
    
    /**
     * Patron para realizar el ordenamiento
     */
    private String patronOrdenar;
	
    /**
     * Ultimo patron por el que se realizo el ordenamiento
     */
    private String ultimoPatron;
	
    
	
	
	
	
	
	/**
	 * Metodo reset
	 */
	public void reset()
	{
		this.fechaInicial = "";
    	this.fechaFinal = "";
    	this.tipoDeudor = "";
    	this.deudores = new ArrayList<HashMap<String,Object>>();
    	this.codigoDeudor = "";
    	this.descripDeudor="";
    	this.consultaMovimientoDeudor = new HashMap();
    	this.consultaMovimientoDeudor.put("numRegistros", "0");
    	this.consultaDetalleMovimientoDeudor = new HashMap();
    	this.consultaDetalleMovimientoDeudor.put("numRegistros", "0");
    	this.posicion = ConstantesBD.codigoNuncaValido;
    	//PARA EL MANEJO DEL PAGER
    	this.currentPageNumber = 1;
        this.linkSiguiente = "";
        this.offset = 0;
        this.patronOrdenar = "";
    	this.ultimoPatron = "";
	}
	
	/**
	 * Método que resetea el mapa y la variable posición
	 */
	public void resetDatosVolver()
	{
		this.consultaMovimientoDeudor = new HashMap();
    	this.consultaMovimientoDeudor.put("numRegistros", "0");
    	this.consultaDetalleMovimientoDeudor = new HashMap();
    	this.consultaDetalleMovimientoDeudor.put("numRegistros", "0");
    	this.posicion = ConstantesBD.codigoNuncaValido;
	}
	
	/**
	* Control de Errores (Validaciones)
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		if(this.estado.equals("buscar"))
		{
			
			if(UtilidadTexto.isEmpty(this.getFechaInicial()) && UtilidadTexto.isEmpty(this.getFechaFinal()))
			{
				errores.add("", new ActionMessage("error.errorEnBlanco", "Es requerido seleccionar las fechas para realizar la búsqueda." ));
			}
			else
			{
				//Validaciones de las fechas si no vienen vacias
				if(!UtilidadTexto.isEmpty(this.fechaInicial.toString()) || !UtilidadTexto.isEmpty(this.fechaFinal.toString()))
				{
					if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaInicial.toString()))
					{
						errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Inicial "+this.fechaInicial));
					}
					if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaFinal.toString()))
					{
						errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final "+this.fechaFinal));
					}
					if(errores.isEmpty())
					{
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial.toString(), this.fechaFinal.toString()))
							errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+this.fechaInicial, "Final "+this.fechaFinal));
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaFinal.toString(), UtilidadFecha.getFechaActual().toString()))
							errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final "+this.fechaFinal, "Actual "+UtilidadFecha.getFechaActual()));
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial.toString(), UtilidadFecha.getFechaActual().toString()))
							errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+this.fechaInicial, "Actual "+UtilidadFecha.getFechaActual()));
						if(UtilidadFecha.numeroMesesEntreFechasExacta(this.fechaInicial, this.fechaFinal) >= 6)
							errores.add("", new ActionMessage("error.facturacion.maximoRangoFechas", "para Consultar Movimientos por Deudor", "6", "180"));
					}
				}
			}
		}
		return errores;
	}
	
	
	
	public String getDescripDeudor() {
		return descripDeudor;
	}

	public void setDescripDeudor(String descripDeudor) {
		this.descripDeudor = descripDeudor;
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
	 * @return the consultaMovimientoDeudor
	 */
	public HashMap getConsultaMovimientoDeudor() {
		return consultaMovimientoDeudor;
	}

	/**
	 * @param consultaMovimientoDeudor the consultaMovimientoDeudor to set
	 */
	public void setConsultaMovimientoDeudor(HashMap consultaMovimientoDeudor) {
		this.consultaMovimientoDeudor = consultaMovimientoDeudor;
	}

	/**
	 * @param key
	 * @return
	 */	
	public Object getConsultaMovimientoDeudor(String key){
		return consultaMovimientoDeudor.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setConsultaMovimientoDeudor(String key, Object value){
		this.consultaMovimientoDeudor.put(key, value);
	}
	
	/**
	 * @return the consultaDetalleMovimientoDeudor
	 */
	public HashMap getConsultaDetalleMovimientoDeudor() {
		return consultaDetalleMovimientoDeudor;
	}

	/**
	 * @param consultaDetalleMovimientoDeudor the consultaDetalleMovimientoDeudor to set
	 */
	public void setConsultaDetalleMovimientoDeudor(HashMap consultaDetalleMovimientoDeudor) {
		this.consultaDetalleMovimientoDeudor = consultaDetalleMovimientoDeudor;
	}

	/**
	 * @param key
	 * @return
	 */	
	public Object getConsultaDetalleMovimientoDeudor(String key){
		return consultaDetalleMovimientoDeudor.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setConsultaDetalleMovimientoDeudor(String key, Object value){
		this.consultaDetalleMovimientoDeudor.put(key, value);
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
	 * @return the tipoDeudor
	 */
	public String getTipoDeudor() {
		return tipoDeudor;
	}

	/**
	 * @param tipoDeudor the tipoDeudor to set
	 */
	public void setTipoDeudor(String tipoDeudor) {
		this.tipoDeudor = tipoDeudor;
	}
	
	/**
	 * @return the codigoDeudor
	 */
	public String getCodigoDeudor() {
		return codigoDeudor;
	}

	/**
	 * @param codigoDeudor the codigoDeudor to set
	 */
	public void setCodigoDeudor(String codigoDeudor) {
		this.codigoDeudor = codigoDeudor;
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
	 * @param deudores the deudores to set
	 */
	public void setDeudores(ArrayList<HashMap<String, Object>> deudores) {
		this.deudores = deudores;
	}
	
	/**
	 * @return the deudores
	 */
	public ArrayList<HashMap<String, Object>> getDeudores() {
		return deudores;
	}

	/**
	 * @return the currentPageNumber
	 */
	public int getCurrentPageNumber() {
		return currentPageNumber;
	}

	/**
	 * @param currentPageNumber the currentPageNumber to set
	 */
	public void setCurrentPageNumber(int currentPageNumber) {
		this.currentPageNumber = currentPageNumber;
	}
	
}