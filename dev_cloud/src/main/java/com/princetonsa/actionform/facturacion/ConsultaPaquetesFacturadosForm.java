package com.princetonsa.actionform.facturacion;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

public class ConsultaPaquetesFacturadosForm extends ValidatorForm 
{

	
	private String estado;
	
	private String codigoConvenio;
	
	private String codigoPaquete;
	
	private String fechaInicial;
	
	private String fechaFinal;
	
	private HashMap mapaListadoPaquetes;
	
	private HashMap mapadetallePaquete;
	
	private HashMap mapaAsociosCirugia;
	
	private int indiceDetalle;
	
	private int indiceAsocio;
	
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
	
	
	/**
	 * 
	 *
	 */
	public void reset()
	{
		this.estado="";
		this.codigoConvenio="";
		this.codigoPaquete="";
		this.fechaInicial="";
		this.fechaFinal="";
		this.mapaListadoPaquetes= new HashMap();
		this.mapaListadoPaquetes.put("numRegistros", "0");
		this.mapadetallePaquete= new HashMap();
		this.mapadetallePaquete.put("numRegistros", "0");
		this.mapaAsociosCirugia= new HashMap();
		this.mapaAsociosCirugia.put("numRegistros", "0");
		this.indiceDetalle= ConstantesBD.codigoNuncaValido;
		this.indiceAsocio= ConstantesBD.codigoNuncaValido;
		this.patronOrdenar="";
		this.ultimoPatron="";
		this.maxPageItems=5;
		this.linkSiguiente="";
	}


	
	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		
		ActionErrors errores= new ActionErrors();
		
		if(this.estado.equals("buscar"))
		{
			
			if(this.fechaInicial.equals(""))
			{
				errores.add("codigo", new ActionMessage("errors.required","La Fecha Inicial "));
			}
			if(this.fechaFinal.equals(""))
			{
				errores.add("codigo", new ActionMessage("errors.required","La Fecha Final "));
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
	public String getCodigoConvenio() {
		return codigoConvenio;
	}

	/**
	 * 
	 * @param codigoConvenio
	 */
	public void setCodigoConvenio(String codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}

	/**
	 * 
	 * @return
	 */
	public int getIndiceDetalle() {
		return indiceDetalle;
	}

	/**
	 * 
	 * @param indiceDetalle
	 */
	public void setIndiceDetalle(int indiceDetalle) {
		this.indiceDetalle = indiceDetalle;
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
	public HashMap getMapadetallePaquete() {
		return mapadetallePaquete;
	}

	/**
	 * 
	 * @param mapadetallePaquete
	 */
	public void setMapadetallePaquete(HashMap mapadetallePaquete) {
		this.mapadetallePaquete = mapadetallePaquete;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaListadoPaquetes() {
		return mapaListadoPaquetes;
	}

	/**
	 * 
	 * @param mapaListadoPaquetes
	 */
	public void setMapaListadoPaquetes(HashMap mapaListadoPaquetes) {
		this.mapaListadoPaquetes = mapaListadoPaquetes;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaListadoPaquetes(String key) 
	{
		return mapaListadoPaquetes.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */	
	public void setMapaListadoPaquetes(String key,Object value) 
	{
		this.mapaListadoPaquetes.put(key, value);
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
	public String getCodigoPaquete() {
		return codigoPaquete;
	}

	/**
	 * 
	 * @param codigoPaquete
	 */
	public void setCodigoPaquete(String codigoPaquete) {
		this.codigoPaquete = codigoPaquete;
	}

	/**
	 * 
	 * @return
	 */
	public int getIndiceAsocio() {
		return indiceAsocio;
	}

	/**
	 * 
	 * @param indiceAsocio
	 */
	public void setIndiceAsocio(int indiceAsocio) {
		this.indiceAsocio = indiceAsocio;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaAsociosCirugia() {
		return mapaAsociosCirugia;
	}

	/**
	 * 
	 * @param mapaAsociosCirugia
	 */
	public void setMapaAsociosCirugia(HashMap mapaAsociosCirugia) {
		this.mapaAsociosCirugia = mapaAsociosCirugia;
	}
	
	
}
