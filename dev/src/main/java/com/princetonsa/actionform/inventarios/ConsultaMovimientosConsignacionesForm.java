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

public class ConsultaMovimientosConsignacionesForm extends ValidatorForm 
{

	
	/**
	 * 
	 */
	private String estado;
	
	private String centroAtencion;
	
	private String almacen;
	
	private String proveedor;
	
	private String fechaInicial;
	
	private String fechaFinal;
	
	private String tipoCodigo;
	
	private String patronOrdenar;
	
	private String ultimoPatron;
	
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
	
	private HashMap mapaListadoMovimientos;
	
	private ResultadoBoolean mensaje = new ResultadoBoolean(false);
    
	private String pathArchivoTxt;
	
	private boolean archivo;
	
	private boolean zip;
	
	
	/**
	 * 
	 */
	public void reset(String centroAtencion, String institucion)
	{
		this.estado="";
		this.centroAtencion=centroAtencion;
		this.almacen="";
		this.proveedor="";
		this.fechaInicial="";
		this.fechaFinal="";
		this.tipoCodigo="";
		this.patronOrdenar="";
		this.ultimoPatron="";
		this.maxPageItems=10;
		this.linkSiguiente="";
		this.mapaListadoMovimientos = new HashMap();
		this.mapaListadoMovimientos.put("numRegistros", "0");
		this.pathArchivoTxt="";
		this.archivo=false;
		this.zip=false;
	}
	
	
	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		
		ActionErrors errores= new ActionErrors();
		
		if(this.estado.equals("buscar"))
		{
			
			if(this.centroAtencion.equals(""))
			{
				errores.add("codigo", new ActionMessage("errors.required","El Centro de Atencion "));
			}
			if(this.fechaInicial.equals(""))
			{
				errores.add("codigo", new ActionMessage("errors.required","La Fecha Inicial "));
			}
			if(this.fechaFinal.equals(""))
			{
				errores.add("codigo", new ActionMessage("errors.required","La Fecha Final "));
			}
			if(this.tipoCodigo.equals(""))
			{
				errores.add("codigo", new ActionMessage("errors.required","El tipo de Codigo "));
			}
			if(!UtilidadTexto.isEmpty(this.fechaInicial) || !UtilidadTexto.isEmpty(this.fechaFinal))
			{
				boolean centinelaErrorFechas=false;
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
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial, this.fechaFinal))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "inicial "+this.fechaInicial, "final "+this.fechaFinal));
					}
				}
				if(!centinelaErrorFechas)
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial, UtilidadFecha.getFechaActual()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "inicial "+this.fechaInicial, "Actual "+UtilidadFecha.getFechaActual()));
					}
				}
				if(!centinelaErrorFechas)
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaFinal, UtilidadFecha.getFechaActual()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final "+this.fechaFinal, "Actual "+UtilidadFecha.getFechaActual()));
					}
				}
				
			}
			
		}
	
		return errores;
		
	}
	
	

	
	/**
	 * 
	 * @return
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * 
	 * @param estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * 
	 * @return
	 */
	public String getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * 
	 * @param centroAtencion
	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * 
	 * @return
	 */
	public String getAlmacen() {
		return almacen;
	}

	/**
	 * 
	 * @param almacen
	 */
	public void setAlmacen(String almacen) {
		this.almacen = almacen;
	}

	/**
	 * 
	 * @return
	 */
	public String getProveedor() {
		return proveedor;
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
	public String getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * 
	 * @param fechaInicial
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * 
	 * @return
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * 
	 * @param fechaFinal
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * 
	 * @return
	 */
	public String getTipoCodigo() {
		return tipoCodigo;
	}

	/**
	 * 
	 * @param tipoCodigo
	 */
	public void setTipoCodigo(String tipoCodigo) {
		this.tipoCodigo = tipoCodigo;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaListadoMovimientos() {
		return mapaListadoMovimientos;
	}

	/**
	 * 
	 * @param mapaListadoMovimientos
	 */
	public void setMapaListadoMovimientos(HashMap mapaListadoMovimientos) {
		this.mapaListadoMovimientos = mapaListadoMovimientos;
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaListadoMovimientos(String key) 
	{
		return mapaListadoMovimientos.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaListadoMovimientos(String key,Object value) 
	{
		this.mapaListadoMovimientos.put(key, value);
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * 
	 * @param patronOrdenar
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * 
	 * @return
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}

	/**
	 * 
	 * @param ultimoPatron
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	/**
	 * 
	 * @return
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * 
	 * @param offset
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * 
	 * @return
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}

	/**
	 * 
	 * @param maxPageItems
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}

	/**
	 * 
	 * @return
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	/**
	 * 
	 * @param linkSiguiente
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * 
	 * @return
	 */
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	/**
	 * 
	 * @param mensaje
	 */
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * 
	 * @return
	 */
	public String getPathArchivoTxt() {
		return pathArchivoTxt;
	}

	/**
	 * 
	 * @param pathArchivoTxt
	 */
	public void setPathArchivoTxt(String pathArchivoTxt) {
		this.pathArchivoTxt = pathArchivoTxt;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isArchivo() {
		return archivo;
	}

	/**
	 * 
	 * @param archivo
	 */
	public void setArchivo(boolean archivo) {
		this.archivo = archivo;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isZip() {
		return zip;
	}

	/**
	 * 
	 * @param zip
	 */
	public void setZip(boolean zip) {
		this.zip = zip;
	}
	
	
	
}
