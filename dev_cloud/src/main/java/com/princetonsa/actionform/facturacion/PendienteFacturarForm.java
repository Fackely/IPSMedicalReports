package com.princetonsa.actionform.facturacion;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class PendienteFacturarForm extends ValidatorForm 
{
	
	
	private String estado="";
	
	
	private String fechaCorte;
	
	private String medico;
	
	private HashMap consulta;
	
	private String patronOrdenar;
	
	private String ultimoPatron;
	
	private String linkSiguiente;
	
	private int maxPageItems;
	
	private int offset;
	
	
	/**
	 * 
	 *
	 */
	public void reset()
	{
		
		this.fechaCorte="";
		this.medico="";
		this.consulta = new HashMap();
		this.consulta.put("unmRegistro", "0");
		
		this.patronOrdenar="";
		this.ultimoPatron="";
		
		linkSiguiente="";this.maxPageItems=10;
		
		this.offset=0;
		
	}
	
	
	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		if(this.estado.equals("buscar"))
		{
		
			if(UtilidadTexto.isEmpty(this.getFechaCorte()))
			{
				errores.add("codigo", new ActionMessage("errors.required","La Fecha Corte "));
			}
			if(!UtilidadTexto.isEmpty(this.getFechaCorte()) || !UtilidadTexto.isEmpty(this.getFechaCorte()))
			{
				boolean centinelaErrorFechas=false;
				if(!UtilidadFecha.esFechaValidaSegunAp(this.getFechaCorte()))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Corte "+this.getFechaCorte()));
					centinelaErrorFechas=true;
				}
				if(!centinelaErrorFechas)
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getFechaCorte(), UtilidadFecha.getFechaActual()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Corte "+this.getFechaCorte(), "Actual "+UtilidadFecha.getFechaActual()));
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
	public String getFechaCorte() {
		return fechaCorte;
	}


	/**
	 * 
	 * @param fechaCorte
	 */
	public void setFechaCorte(String fechaCorte) {
		this.fechaCorte = fechaCorte;
	}


	/**
	 * 
	 * @return
	 */
	public HashMap getConsulta() {
		return consulta;
	}


	/**
	 * 
	 * @param consulta
	 */
	public void setConsulta(HashMap consulta) {
		this.consulta = consulta;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getConsulta(String key) 
	{
		return consulta.get(key);
	}
	
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setConsulta(String key,Object value) 
	{
		this.consulta.put(key, value);
	}

	
	/**
	 * 
	 * @return
	 */
	public String getMedico() {
		return medico;
	}

	
	/**
	 * 
	 * @param medico
	 */
	public void setMedico(String medico) {
		this.medico = medico;
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


	public String getLinkSiguiente() {
		return linkSiguiente;
	}


	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}


	public int getMaxPageItems() {
		return maxPageItems;
	}


	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}


	public int getOffset() {
		return offset;
	}


	public void setOffset(int offset) {
		this.offset = offset;
	}
	

}
