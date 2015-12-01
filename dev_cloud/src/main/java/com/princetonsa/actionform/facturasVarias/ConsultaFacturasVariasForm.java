package com.princetonsa.actionform.facturasVarias;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class ConsultaFacturasVariasForm extends ValidatorForm 
{
	
	
	private String estado;
	
	private String FechaInicial;
	
	private String FechaFinal;
	
	private String factura;
	
	private String estadosFactura;
	
	private String deudor;
	
	private String descripDeudor;
	
	private String tipoDeudor;
	
	private HashMap mapaResultadoFacturas;
	
	private HashMap detalleFactura;
	
	private String facturaVaria;
	
	private int indiceDetalle;
	
	private String tipoDeudorFac;
	
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
		this.maxPageItems = 20;
        this.linkSiguiente = "";
    	this.factura="";
		this.estadosFactura="";
		this.deudor="";
		this.tipoDeudor="";
		this.FechaInicial="";
		this.FechaFinal="";
		this.mapaResultadoFacturas=new HashMap();
		this.mapaResultadoFacturas.put("numRegistros","0");
		this.detalleFactura=new HashMap();
		this.detalleFactura.put("numRegistros","0");
		this.facturaVaria="";
		this.indiceDetalle=ConstantesBD.codigoNuncaValido;
		this.tipoDeudorFac="";
		this.patronOrdenar="";
		this.ultimoPatron="";
		this.descripDeudor = "";
	}
	
	
	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		if(this.estado.equals("buscar"))
		{
			
			{
				
				if(this.factura.equals("")&&this.estadosFactura.equals("")&&this.tipoDeudor.equals("")&&this.deudor.equals("")&&this.FechaInicial.equals("")&&this.FechaFinal.equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","Por lo menos parametrizar un campo"));
				}				
				if(!this.FechaInicial.equals("")&&this.FechaFinal.equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","La Fecha Final "));
				}
				if(!this.FechaFinal.equals("")&&this.FechaInicial.equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","La Fecha Inicial "));
				}
				
				if(!UtilidadTexto.isEmpty(this.FechaFinal) || !UtilidadTexto.isEmpty(this.FechaFinal))
				{
					boolean centinelaErrorFechas=false;
					if(!UtilidadFecha.esFechaValidaSegunAp(this.FechaInicial))
					{
						errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Inicial "+this.FechaInicial));
						centinelaErrorFechas=true;
					}
					if(!UtilidadFecha.esFechaValidaSegunAp(this.FechaFinal))
					{
						errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final "+this.FechaFinal));
						centinelaErrorFechas=true;
					}
					
					if(!centinelaErrorFechas)
					{
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.FechaInicial, this.FechaFinal))
						{
							errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "inicial "+this.FechaInicial, "final "+this.FechaFinal));
						}
					}
					if(!centinelaErrorFechas)
					{
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.FechaInicial, UtilidadFecha.getFechaActual()))
						{
							errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "inicial "+this.FechaInicial, "Actual "+UtilidadFecha.getFechaActual()));
						}
					}
					if(!centinelaErrorFechas)
					{
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.FechaFinal, UtilidadFecha.getFechaActual()))
						{
							errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final "+this.FechaFinal, "Actual "+UtilidadFecha.getFechaActual()));
						}
					}
					if(!centinelaErrorFechas)
					{
						if(UtilidadFecha.numeroMesesEntreFechasExacta(this.getFechaInicial(), this.getFechaFinal())>=6)
						{
							errores.add("", new ActionMessage("error.facturasVarias.consultaFacturasVarias", "para consultar Facturas Varias"));
						}
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
		return FechaFinal;
	}

	/**
	 * 
	 * @param fechaFinal
	 */
	public void setFechaFinal(String fechaFinal) {
		FechaFinal = fechaFinal;
	}

	/**
	 * 
	 * @return
	 */
	public String getFechaInicial() {
		return FechaInicial;
	}

	/**
	 * 
	 * @param fechaInicial
	 */
	public void setFechaInicial(String fechaInicial) {
		FechaInicial = fechaInicial;
	}

	/**
	 * 
	 * @return
	 */
	public String getFactura() {
		return factura;
	}

	/**
	 * 
	 * @param factura
	 */
	public void setFactura(String factura) {
		this.factura = factura;
	}

	/**
	 * 
	 * @return
	 */
	public String getDeudor() {
		return deudor;
	}

	/**
	 * 
	 * @param deudor
	 */
	public void setDeudor(String deudor) {
		this.deudor = deudor;
	}

	/**
	 * 
	 * @return
	 */
	public String getEstadosFactura() {
		return estadosFactura;
	}

	/**
	 * 
	 * @param estadosFactura
	 */
	public void setEstadosFactura(String estadosFactura) {
		this.estadosFactura = estadosFactura;
	}

	/**
	 * 
	 * @return
	 */
	public String getTipoDeudor() {
		return tipoDeudor;
	}

	/**
	 * 
	 * @param tipoDeudor
	 */
	public void setTipoDeudor(String tipoDeudor) {
		this.tipoDeudor = tipoDeudor;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaResultadoFacturas() {
		return mapaResultadoFacturas;
	}

	/**
	 * 
	 * @param mapaResultadoFacturas
	 */
	public void setMapaResultadoFacturas(HashMap mapaResultadoFacturas) {
		this.mapaResultadoFacturas = mapaResultadoFacturas;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */	
	public Object getMapaResultadoFacturas(String key) 
	{
		return mapaResultadoFacturas.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */	
	public void setMapaResultadoFacturas(String key,Object value) 
	{
		this.mapaResultadoFacturas.put(key, value);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getFacturaVaria() {
		return facturaVaria;
	}

	/**
	 * 
	 * @param facturaVaria
	 */
	public void setFacturaVaria(String facturaVaria) {
		this.facturaVaria = facturaVaria;
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
	public HashMap getDetalleFactura() {
		return detalleFactura;
	}

	/**
	 * 
	 * @param detalleFactura
	 */
	public void setDetalleFactura(HashMap detalleFactura) {
		this.detalleFactura = detalleFactura;
	}

	/**
	 * 
	 * @return
	 */
	public String getTipoDeudorFac() {
		return tipoDeudorFac;
	}

	/**
	 * 
	 * @param tipoDeudorFac
	 */
	public void setTipoDeudorFac(String tipoDeudorFac) {
		this.tipoDeudorFac = tipoDeudorFac;
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


	public String getDescripDeudor() {
		return descripDeudor;
	}


	public void setDescripDeudor(String descripDeudor) {
		this.descripDeudor = descripDeudor;
	}
	

}
