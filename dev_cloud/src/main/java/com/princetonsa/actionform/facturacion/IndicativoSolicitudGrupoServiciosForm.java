package com.princetonsa.actionform.facturacion;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class IndicativoSolicitudGrupoServiciosForm extends ValidatorForm
{
	/**
	 * 
	 */
	private String estado;

	/**
	 * 
	 */
	private HashMap gruposServicios;

	/**
	 * 
	 */
	private String grupoServicio;
	
	/**
	 * 
	 */
	private HashMap procedimientos;
	
	/**
	 * 
	 */
	private boolean activar;
	
	/**
	 * 
	 *
	 */
	public void reset()
	{
		this.gruposServicios=new HashMap();
		this.gruposServicios.put("numRegistros", "0");
		this.grupoServicio="";
		this.procedimientos=new HashMap();
		this.procedimientos.put("numRegistros", "0");
		this.activar=false;
	}
	
	/**
	 *Se tiene en cuenta para futuras validaciones 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		return errores;
	}

	public String getEstado()
	{
		return estado;
	}

	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	public HashMap getGruposServicios()
	{
		return gruposServicios;
	}

	public void setGruposServicios(HashMap gruposServicios)
	{
		this.gruposServicios = gruposServicios;
	}
	
	public Object getGruposServicios(String key)
	{
		return gruposServicios.get(key);
	}

	public void setGruposServicios(String key,Object value)
	{
		this.gruposServicios.put(key, value);
	}

	public String getGrupoServicio()
	{
		return grupoServicio;
	}

	public void setGrupoServicio(String grupoServicio)
	{
		this.grupoServicio = grupoServicio;
	}

	public HashMap getProcedimientos()
	{
		return procedimientos;
	}

	public void setProcedimientos(HashMap procedimientos)
	{
		this.procedimientos = procedimientos;
	}

	public Object getProcedimientos(String key)
	{
		return procedimientos.get(key);
	}

	public void setProcedimientos(String key,Object value)
	{
		this.procedimientos.put(key, value);
	}

	public boolean isActivar()
	{
		return activar;
	}

	public void setActivar(boolean activar)
	{
		this.activar = activar;
	}
	 
}
