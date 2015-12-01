package com.princetonsa.actionform.facturacion;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;

public class VentasCentroCostoForm extends ValidatorForm 
{

	
	/**
	 * 
	 */
	private String estado;
	
	private String centroAtencion;
	
	private String fechaInicial;
	
	private String fechaFinal;
	
	private String centroCosto;
	
	private HashMap mapaConsultaVentas;
	
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
		this.fechaInicial="";
		this.fechaFinal="";
		this.centroCosto="";
		this.mapaConsultaVentas=new HashMap();
		this.mapaConsultaVentas.put("numRegistros", "0");
		this.pathArchivoTxt="";
		this.archivo=false;
		this.zip=false;
	}

	
	
	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		
		if(estado.equals("imprimir")||estado.equals("generarArchivo"))
		{
			if(this.centroAtencion.equals(""))
			{
				errores.add("", new ActionMessage("errors.required","El centro de atención "));
			}
			if(this.fechaInicial.equals(""))
			{
				errores.add("", new ActionMessage("errors.required","La fecha Inicial "));
			}
			if(this.fechaFinal.equals(""))
			{
				errores.add("", new ActionMessage("errors.required","La fecha final "));
			}
			if(!UtilidadTexto.isEmpty(this.getFechaInicial()) || !UtilidadTexto.isEmpty(this.getFechaFinal()))
			{
				boolean centinelaErrorFechas=false;
				if(!UtilidadFecha.esFechaValidaSegunAp(this.getFechaInicial()))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Inicial "+this.getFechaInicial()));
					centinelaErrorFechas=true;
				}
				if(!UtilidadFecha.esFechaValidaSegunAp(this.getFechaFinal()))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final "+this.getFechaFinal()));
					centinelaErrorFechas=true;
				}
				
				if(!centinelaErrorFechas)
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getFechaInicial(), this.getFechaFinal()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "inicial "+this.getFechaInicial(), "final "+this.getFechaFinal()));
					}
				}
				if(!centinelaErrorFechas)
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getFechaInicial(), UtilidadFecha.getFechaActual()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "inicial "+this.getFechaInicial(), "Actual "+UtilidadFecha.getFechaActual()));
					}
				}
				if(!centinelaErrorFechas)
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getFechaFinal(), UtilidadFecha.getFechaActual()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final "+this.getFechaFinal(), "Actual "+UtilidadFecha.getFechaActual()));
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
	public String getCentroCosto() {
		return centroCosto;
	}

	/**
	 * 
	 * @param centroCosto
	 */
	public void setCentroCosto(String centroCosto) {
		this.centroCosto = centroCosto;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaConsultaVentas() {
		return mapaConsultaVentas;
	}

	/**
	 * 
	 * @param mapaConsultaVentas
	 */
	public void setMapaConsultaVentas(HashMap mapaConsultaVentas) {
		this.mapaConsultaVentas = mapaConsultaVentas;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaConsultaVentas(String key) {
		return mapaConsultaVentas.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaConsultaVentas(String key, Object value) {
		this.mapaConsultaVentas.put(key, value);
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
