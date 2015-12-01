/*
 * Creado el 12/04/2005
 * Juan David Ramírez López
 */
package com.princetonsa.actionform.parametrizacion;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import util.UtilidadFecha;

/**
 * @author Juan David Ramírez
 * 
 * CopyRight Princeton S.A.
 * 12/04/2005
 */
public class DiasFestivosForm extends ActionForm
{
	/**
	 * Manejador del flujo de la funcionalidad
	 */
	private String estado;
	
	/**
	 * Fecha del día festivo
	 */
	private String fecha;
	
	/**
	 * Tipo del dia Festivo
	 */
	private int tipo;
	
	/**
	 * Descripción del día festivo
	 */
	private String descripcion;
	
	/**
	 * Fecha aterior para logs
	 */
	private String fechaAnt;
	
	/**
	 * Descripción anterior para Logs
	 */
	private String descAnterior;
	
	/**
	 * Tipo anterior para logs
	 */
	private String tipoAnterior;
	
	/**
	 * Año para listar los dias festivos
	 */
	private String anio;
	
	/**
	 * Manejo de los listados de días festivos
	 */
	private Collection listado;
	
	/**
	 * Utilizada para ordenar
	 */
	private String columna;

	/**
	 * Utilizada para ordenar (Ultima preopiedad por la que se ordenó)
	 */
	private String ultimaColumna;

	/**
	 * Metodo para inizializar los
	 * atributos de la Forma
	 */
	public void reset()
	{
		fecha=UtilidadFecha.getFechaActual();
		tipo=0;
		descripcion="";
		anio=UtilidadFecha.getFechaActual().split("/")[2];
	}

	/**
	 * Metodo para validar las entradas
	 * de datos en el JSP
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errores=new ActionErrors();
		
		if(estado.equals("guardar"))
		{
			if(fecha.trim().equals(""))
			{
				errores.add("fecha", new ActionMessage("errors.required","El campo Fecha"));
			}
			if(descripcion.trim().equals(""))
			{
				errores.add("desc", new ActionMessage("errors.required","El campo Descripción"));
			}
			if(tipo==0)
			{
				errores.add("tipo", new ActionMessage("errors.required","El campo Tipo"));
			}
		}
		return errores;
	}
	/**
	 * @return Retorna descripcion.
	 */
	public String getDescripcion()
	{
		return descripcion;
	}
	/**
	 * @param descripcion Asigna descripcion.
	 */
	public void setDescripcion(String descripcion)
	{
		this.descripcion = descripcion;
	}
	/**
	 * @return Retorna fecha.
	 */
	public String getFecha()
	{
		return fecha;
	}
	/**
	 * @param fecha Asigna fecha.
	 */
	public void setFecha(String fecha)
	{
		this.fecha = fecha;
	}
	/**
	 * @return Retorna tipo.
	 */
	public int getTipo()
	{
		return tipo;
	}
	/**
	 * @param tipo Asigna tipo.
	 */
	public void setTipo(int tipo)
	{
		this.tipo = tipo;
	}
	/**
	 * @return Retorna estado.
	 */
	public String getEstado()
	{
		return estado;
	}
	/**
	 * @param estado Asigna estado.
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}
	/**
	 * @return Retorna fechaAnt.
	 */
	public String getFechaAnt()
	{
		return fechaAnt;
	}
	/**
	 * @param fechaAnt Asigna fechaAnt.
	 */
	public void setFechaAnt(String fechaAnt)
	{
		this.fechaAnt = fechaAnt;
	}
	/**
	 * @return Retorna descAnterior.
	 */
	public String getDescAnterior()
	{
		return descAnterior;
	}
	/**
	 * @param descAnterior Asigna descAnterior.
	 */
	public void setDescAnterior(String descAnterior)
	{
		this.descAnterior = descAnterior;
	}
	/**
	 * @return Retorna tipoAnterior.
	 */
	public String getTipoAnterior()
	{
		return tipoAnterior;
	}
	/**
	 * @param tipoAnterior Asigna tipoAnterior.
	 */
	public void setTipoAnterior(String tipoAnterior)
	{
		this.tipoAnterior = tipoAnterior;
	}
	/**
	 * @return Retorna anio.
	 */
	public String getAnio()
	{
		return anio;
	}
	/**
	 * @param anio Asigna anio.
	 */
	public void setAnio(String anio)
	{
		this.anio = anio;
	}
	/**
	 * @return Retorna listado.
	 */
	public Collection getListado()
	{
		return listado;
	}
	/**
	 * @param listado Asigna listado.
	 */
	public void setListado(Collection listado)
	{
		this.listado = listado;
	}

	/**
	 * @return Retorna columna.
	 */
	public String getColumna()
	{
		return columna;
	}

	/**
	 * @param columna Asigna columna.
	 */
	public void setColumna(String columna)
	{
		this.columna = columna;
	}

	/**
	 * @return Retorna ultimaColumna.
	 */
	public String getUltimaColumna()
	{
		return ultimaColumna;
	}

	/**
	 * @param ultimaColumna Asigna ultimaColumna.
	 */
	public void setUltimaColumna(String ultimaColumna)
	{
		this.ultimaColumna = ultimaColumna;
	}
}
