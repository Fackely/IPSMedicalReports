/*
 * @author artotor
 */
package com.princetonsa.actionform.manejoPaciente;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.servinte.axioma.dto.manejoPaciente.DtoFiltroBusquedaAvanzadaEgresoPorFacturar;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

/**
 * 
 * @author artotor
 *
 */
public class PacientesConEgresoPorFacturarForm extends ValidatorForm 
{
	
	/**
	 * 
	 */
	private String estado;
	
	/**
	 * 
	 */
	private HashMap pacientes;
	
	/**
	 * 
	 */
	private int maxPageItems;
	
	/**
	 * 
	 */
	private String patronOrdenar;
	
	/**
	 * 
	 */
	private String ultimoPatron;
	
	/**
	 * 
	 */
	private int index;
	
	
	private DtoFiltroBusquedaAvanzadaEgresoPorFacturar dtoFiltro;
	
	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		if(estado.equals("buscar"))
		{
			if(UtilidadTexto.isEmpty(dtoFiltro.getFechaAdmisionIncial()) && UtilidadTexto.isEmpty(dtoFiltro.getFechaAdmisionIncial()) && UtilidadTexto.isEmpty(dtoFiltro.getFechaEgresoInicial()) && UtilidadTexto.isEmpty(dtoFiltro.getFechaEgresoFinal()))
			{
				errores.add("fechas", new ActionMessage("errors.required", "Los rangos de Fechas de Admisión ó los rangos de Fechas de Egreso"));
			}
			if(UtilidadTexto.isEmpty(dtoFiltro.getFechaAdmisionIncial()) && !UtilidadTexto.isEmpty(dtoFiltro.getFechaAdmisionFinal()))
			{
				errores.add("getFechaAdmisionIncial", new ActionMessage("errors.required", "La Fecha Inicial de Admisión"));
			}
			if(!UtilidadTexto.isEmpty(dtoFiltro.getFechaAdmisionIncial()) && UtilidadTexto.isEmpty(dtoFiltro.getFechaAdmisionFinal()))
			{
				errores.add("getFechaAdmisionIncial", new ActionMessage("errors.required", "La Fecha Final de Admisión"));
			}
			if(UtilidadTexto.isEmpty(dtoFiltro.getFechaEgresoInicial()) && !UtilidadTexto.isEmpty(dtoFiltro.getFechaEgresoFinal()))
			{
				errores.add("getFechaAdmisionIncial", new ActionMessage("errors.required", "La Fecha Inicial de Egreso"));
			}
			if(!UtilidadTexto.isEmpty(dtoFiltro.getFechaEgresoInicial()) && UtilidadTexto.isEmpty(dtoFiltro.getFechaEgresoFinal()))
			{
				errores.add("getFechaAdmisionIncial", new ActionMessage("errors.required", "La Fecha Final de Egreso"));
			}
			if(!UtilidadTexto.isEmpty(dtoFiltro.getFechaAdmisionIncial()) && !UtilidadTexto.isEmpty(dtoFiltro.getFechaAdmisionFinal()))
			{
				boolean errorFecha=false;
				if(!UtilidadFecha.validarFecha(this.dtoFiltro.getFechaAdmisionIncial()))
				{
					errores.add("fecha de Inicial", new ActionMessage("errors.formatoFechaInvalido",dtoFiltro.getFechaAdmisionIncial()));
					errorFecha=true;
				}
				if(!UtilidadFecha.validarFecha(this.dtoFiltro.getFechaAdmisionFinal()))
				{
					errores.add("fecha de Fianl", new ActionMessage("errors.formatoFechaInvalido",this.dtoFiltro.getFechaAdmisionFinal()));
					errorFecha=true;
				}
				if(!errorFecha)
				{
					if((UtilidadFecha.conversionFormatoFechaABD(dtoFiltro.getFechaAdmisionIncial())).compareTo(UtilidadFecha.conversionFormatoFechaABD(dtoFiltro.getFechaAdmisionFinal()))>0)
					{
						errores.add("fecha de Inicial-Final", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Inicial de Admisión", "Final de Admisión"));
					}				
				}				
			}
			if(!UtilidadTexto.isEmpty(dtoFiltro.getFechaEgresoInicial()) && !UtilidadTexto.isEmpty(dtoFiltro.getFechaEgresoFinal()))
			{
				boolean errorFecha=false;
				if(!UtilidadFecha.validarFecha(this.dtoFiltro.getFechaEgresoInicial()))
				{
					errores.add("fecha de Inicial", new ActionMessage("errors.formatoFechaInvalido",dtoFiltro.getFechaEgresoInicial()));
					errorFecha=true;
				}
				if(!UtilidadFecha.validarFecha(this.dtoFiltro.getFechaEgresoFinal()))
				{
					errores.add("fecha de Fianl", new ActionMessage("errors.formatoFechaInvalido",this.dtoFiltro.getFechaEgresoFinal()));
					errorFecha=true;
				}
				if(!errorFecha)
				{
					if((UtilidadFecha.conversionFormatoFechaABD(dtoFiltro.getFechaEgresoInicial())).compareTo(UtilidadFecha.conversionFormatoFechaABD(dtoFiltro.getFechaEgresoFinal()))>0)
					{
						errores.add("fecha de Inicial-Final", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Inicial de Egreso", "Final de Egreso"));
					}				
				}
			}


		}
		return errores;
	}
	
	/**
	 *
	 */
	public void reset()
	{
		this.pacientes=new HashMap();
		this.pacientes.put("numRegistros","0");
		//en caso que no lo tome de valores por defecto.
		this.maxPageItems=20;
		this.patronOrdenar="";
		this.ultimoPatron="";
		this.index=ConstantesBD.codigoNuncaValido;
		this.dtoFiltro=new DtoFiltroBusquedaAvanzadaEgresoPorFacturar();
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public HashMap getPacientes() {
		return pacientes;
	}

	public void setPacientes(HashMap pacientes) {
		this.pacientes = pacientes;
	}
	public Object getPacientes(String key) {
		return pacientes.get(key);
	}

	public void setPacientes(String key,Object value) 
	{
		this.pacientes.put(key,value);
	}

	public int getMaxPageItems() {
		return maxPageItems;
	}

	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}

	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	public String getUltimoPatron() {
		return ultimoPatron;
	}

	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public DtoFiltroBusquedaAvanzadaEgresoPorFacturar getDtoFiltro() {
		return dtoFiltro;
	}

	public void setDtoFiltro(DtoFiltroBusquedaAvanzadaEgresoPorFacturar dtoFiltro) {
		this.dtoFiltro = dtoFiltro;
	}
}
