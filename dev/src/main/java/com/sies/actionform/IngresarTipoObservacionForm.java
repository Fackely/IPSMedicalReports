/*
 * Creado el 14 de Diciembre de 2006
 */
package com.sies.actionform;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.servinte.axioma.orm.TipoObservacion;



/**
 * SiEs
 * @author Juan David Ramírez L.
 * 14/12/2006
 */
public class IngresarTipoObservacionForm extends ActionForm
{
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Manejo de estados de la clase
	 */
	private String estado;
	
	/**
	 * Conservar el estado anterior
	 */
	private String estadoAnterior;
	
	/**
	 * Código del tipo
	 */
	private int codigo;
	
	/**
	 * Descripción del tipo
	 */
	private String descripcion;
	
	/**
	 * Listado de los elementos a mostrar
	 */
	private List<TipoObservacion> listado;
	
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errores=new ActionErrors();
		return errores;
	}

	public void reset()
	{
		this.codigo=0;
		this.descripcion=new String();
		this.listado=null;
	}
	
	/**
	 * @return estado
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * @param estado Asigna estado
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	/**
	 * @return codigo
	 */
	public int getCodigo()
	{
		return codigo;
	}

	/**
	 * @param codigo Asigna codigo
	 */
	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
	}

	/**
	 * @return descripcion
	 */
	public String getDescripcion()
	{
		return descripcion;
	}

	/**
	 * @param descripcion Asigna descripcion
	 */
	public void setDescripcion(String descripcion)
	{
		this.descripcion = descripcion;
	}

	/**
	 * @return listado
	 */
	public List<TipoObservacion> getListado()
	{
		return listado;
	}

	/**
	 * @param listado Asigna listado
	 */
	public void setListado(List<TipoObservacion> listado)
	{
		this.listado = listado;
	}

	/**
	 * @return Retorna estadoAnterior
	 */
	public String getEstadoAnterior()
	{
		return estadoAnterior;
	}

	/**
	 * @param Asigna estadoAnterior
	 */
	public void setEstadoAnterior(String estadoAnterior)
	{
		this.estadoAnterior = estadoAnterior;
	}
}
