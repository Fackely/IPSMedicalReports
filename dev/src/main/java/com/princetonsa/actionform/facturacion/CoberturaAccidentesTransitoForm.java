/*
 * CoberturaAccidentesTransitoForm.java 
 * Autor			:  mdiaz
 * Creado el	:  24-nov-2004
 * 
 * Lenguaje		: Java
 * Compilador	: J2SDK 1.4.1_01
 * 
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 */

package com.princetonsa.actionform.facturacion;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;

/**
 * descripcion de esta clase
 * 
 * @version 1.0, 24-nov-2004
 * @author <a href="mailto:miguel@PrincetonSA.com">Miguel Arturo Diaz </a>
 */
public class CoberturaAccidentesTransitoForm extends ActionForm
{
	/**
	 * Estado en el que se encuentra el proceso.
	 */
	private String estado;

	/**
	 * codigo de la institucion que agrupa estos registros ( es parametrizado
	 * por institución )
	 */
	private int institucion = 0;

	/**
	 * HashMap para almacenar los registros
	 */
	private HashMap registros;
	
	/**
	 * Indice del registro eliminado
	 */
	private int eliminado;
	
	/**
	 * Manejo del paginador
	 */
	private int offset;
	
	/**
	 * Numero de elementos en el paginados
	 */
	private final int maxPageItems=8;
	
	/**
	 * 
	 */
	private String isGuardo;
	
	/**
	 * Método para validar la inserción de datos
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
	    boolean hayError=false; 
		if(estado.equals("guardar"))
		{
			ActionErrors errores=new ActionErrors();
			
			for(int i=0; i<((Integer)getRegistro("numRegistros")).intValue();i++)
			{
				String valor=getRegistro("cobertura_"+i)+"";
				if(valor.equals(""))
				{
				    errores.add("", new ActionMessage("errors.required","El campo cobertura del responsable "+getRegistro("responsable_"+i)+" "));
				    hayError=true;
				}
				if(!hayError)
				{
					try
					{
						Float.parseFloat(valor);
					}
					catch(NumberFormatException e)
					{
						String error="La cobertura ("+valor+") del responsable "+getRegistro("responsable_"+i);
						errores.add("notNumber_"+i,new ActionMessage("errors.integer",error));
					}
				}
			}
			return errores;
		}
		return null;
	}
	
	/**
	 * Constructor vacio
	 *
	 */
	public CoberturaAccidentesTransitoForm() {
		super();
		reset();
	}

	public void reset()
	{
		this.institucion = 0;
		this.registros = new HashMap();
		this.estado="";
		this.eliminado=0;
		this.isGuardo=ConstantesBD.acronimoNo;
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
	 * @return Retorna institucion.
	 */
	public int getInstitucion()
	{
		return institucion;
	}
	/**
	 * @param institucion Asigna institucion.
	 */
	public void setInstitucion(int institucion)
	{
		this.institucion = institucion;
	}
	/**
	 * @return Retorna registros.
	 */
	public HashMap getRegistros()
	{
		return registros;
	}
	/**
	 * @param registros Asigna registros.
	 */
	public void setRegistros(HashMap registros)
	{
		this.registros = registros;
	}
	/**
	 * @return Retorna registros.
	 */
	public Object getRegistro(String key)
	{
		return registros.get(key);
	}
	/**
	 * @param registros Asigna registros.
	 */
	public void setRegistro(String key, Object value)
	{
		this.registros.put(key, value);
	}
	/**
	 * @return Retorna eliminado.
	 */
	public int getEliminado()
	{
		return eliminado;
	}
	/**
	 * @param eliminado Asigna eliminado.
	 */
	public void setEliminado(int eliminado)
	{
		this.eliminado = eliminado;
	}
	/**
	 * @return Retorna offset.
	 */
	public int getOffset()
	{
		return offset;
	}
	/**
	 * @return Retorna maxPageItems.
	 */
	public int getMaxPageItems()
	{
		return maxPageItems;
	}
	/**
	 * @param offset Asigna offset.
	 */
	public void setOffset(int offset)
	{
		this.offset = offset;
	}

	/**
	 * @return the isGuardo
	 */
	public String getIsGuardo() {
		return isGuardo;
	}

	/**
	 * @param isGuardo the isGuardo to set
	 */
	public void setIsGuardo(String isGuardo) {
		this.isGuardo = isGuardo;
	}
}