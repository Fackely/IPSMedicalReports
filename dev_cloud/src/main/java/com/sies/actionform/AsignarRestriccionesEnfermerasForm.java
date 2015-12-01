/*
 * Created on 8/06/2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sies.actionform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 * @author karenth
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AsignarRestriccionesEnfermerasForm extends ActionForm
{

    /**
	 * Default serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
     * código de la asociación de enfermeras en restricciones
     */
    private int codigoAsociacion;
    
    /**
     * para manejar el estado de la forma
     */
    private String estado;
    
    /**
     * Fecha de inicio de la asociación
     */
    private String fechaInicio;
    
    /**
     * Fecha de Finalización de la asociación
     */
    private String fechaFin;
    
    /**
     * Para manejar resultados de consultas 
     */
    private Collection listado;
    
    /**
     * Código de la persona
     */
    private int codigo;
    
    /**
     * Para guaradar el resultado de la consulta de las restricciones
     */
    private Collection listadoRestriccion;
    
    /**
     * Para guardar el resultado de la consulta de la restricciones
     *  que estan asocidas a una enfermera
     */
    private Collection listadoResEnf;
    
    /**
     * Hash Map donde se hacen todas las modificaciones antes de guardar modificaciones
     */
    private HashMap asociacion;
    
    /**
     * Mensaje de resultado al guardar restricciones
     */
    private String mensajeResultado;
    
    /**
     * Nombre de la persona
     */
    private String nombrePersona;
    
    private String textProfesional;

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
 	{
        ActionErrors errores= new ActionErrors();
 		if (estado.equalsIgnoreCase("asignando"))
 		{/*
 			if(codigo==0)
 			{
 				errores.add("Persona requerida", new ActionMessage("errors.required", "Persona"));
 			}
 			*/
 		}
 		return errores;
 	}
    
    public void clean()
    {
    	this.codigoAsociacion=0;
        this.fechaInicio="";
        this.fechaFin="";
        this.listado=new ArrayList();
        this.listadoResEnf=new ArrayList();
        this.listadoRestriccion=new ArrayList();
        this.estado="";
        this.codigo=0;
        this.asociacion=new HashMap();
        this.mensajeResultado="";
        
    }
        
	/**
	 * @return nombrePersona
	 */
	public String getNombrePersona()
	{
		return nombrePersona;
	}
	/**
	 * @param nombrePersona Asigna nombrePersona
	 */
	public void setNombrePersona(String nombrePersona)
	{
		this.nombrePersona = nombrePersona;
	}
	/**
	 * @return asociacion
	 */
	public HashMap getAsociacion()
	{
		return asociacion;
	}
	/**
	 * @param asociacion Asigna asociacion
	 */
	public void setAsociacion(HashMap asociacion)
	{
		this.asociacion = asociacion;
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
	 * @return codigoAsociacion
	 */
	public int getCodigoAsociacion()
	{
		return codigoAsociacion;
	}
	/**
	 * @param codigoAsociacion Asigna codigoAsociacion
	 */
	public void setCodigoAsociacion(int codigoAsociacion)
	{
		this.codigoAsociacion = codigoAsociacion;
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
	 * @return fechaFin
	 */
	public String getFechaFin()
	{
		return fechaFin;
	}
	/**
	 * @param fechaFin Asigna fechaFin
	 */
	public void setFechaFin(String fechaFin)
	{
		this.fechaFin = fechaFin;
	}
	/**
	 * @return fechaInicio
	 */
	public String getFechaInicio()
	{
		return fechaInicio;
	}
	/**
	 * @param fechaInicio Asigna fechaInicio
	 */
	public void setFechaInicio(String fechaInicio)
	{
		this.fechaInicio = fechaInicio;
	}
	/**
	 * @return listado
	 */
	public Collection getListado()
	{
		return listado;
	}
	/**
	 * @param listado Asigna listado
	 */
	public void setListado(Collection listado)
	{
		this.listado = listado;
	}
	/**
	 * @return listadoResEnf
	 */
	public Collection getListadoResEnf()
	{
		return listadoResEnf;
	}
	/**
	 * @param listadoResEnf Asigna listadoResEnf
	 */
	public void setListadoResEnf(Collection listadoResEnf)
	{
		this.listadoResEnf = listadoResEnf;
	}
	/**
	 * @return listadoRestriccion
	 */
	public Collection getListadoRestriccion()
	{
		return listadoRestriccion;
	}
	/**
	 * @param listadoRestriccion Asigna listadoRestriccion
	 */
	public void setListadoRestriccion(Collection listadoRestriccion)
	{
		this.listadoRestriccion = listadoRestriccion;
	}
	/**
	 * @return mensajeResultado
	 */
	public String getMensajeResultado()
	{
		return mensajeResultado;
	}
	/**
	 * @param mensajeResultado Asigna mensajeResultado
	 */
	public void setMensajeResultado(String mensajeResultado)
	{
		this.mensajeResultado = mensajeResultado;
	}
	/**
	 * @return textProfesional
	 */
	public String getTextProfesional()
	{
		return textProfesional;
	}
	/**
	 * @param textProfesional Asigna textProfesional
	 */
	public void setTextProfesional(String textProfesional)
	{
		this.textProfesional = textProfesional;
	}
}
