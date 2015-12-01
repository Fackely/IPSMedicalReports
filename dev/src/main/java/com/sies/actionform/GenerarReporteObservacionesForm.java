/*
 * Creado en 11/12/2005
 *
 * Karenth Marín
 * Si.Es.
 */
package com.sies.actionform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;


import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import util.UtilidadFecha;

/**
 * @author Karenth Marín
 *
 * Si.Es.
 */
public class GenerarReporteObservacionesForm extends ActionForm
{

	/**
	 * Default serail verison UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Para manejar el estado en el que va a realizar la accion
	 */
	private String estado; 
	    
	/**
	 * para manejar la fecha de inicio
	 */
	private String fechaInicio;
	
	/**
	 * Para manejar la fecha de Finalización de la asociación
	 */
	private String fechaFin;
	
	/**
	 * Para manejar la fecha de programacion
	 */
	private String fechaProgramacion;
	
	/**
	 * Para recibir el listado de la consulta
	 */
	private Collection<HashMap<String, Object>> listado;
	
	/**
	 * Manejo de las categorías
	 */
	private Collection<HashMap<String, Object>> categorias;
	
	/**
	 * Para guaradar el codigo de la categoria
	 */
	private int codigoCategoria;
	
	/**
	 * Para guardar el nombre de la categoria
	 */
	private String nombreCategoria;
	
	/**
	 * Para guardar el tipo de observacion
	 */
	private int codigoObservacion;
	
	/**
	 * Guarda el tipo de observacion
	 */
	private String tipoObservacion;
	
	/**
	 * Guarda el nombre de la persona
	 */
	private String nombrePersona;
	
	/**
	 * Guarda el tipo de turno
	 */
	private String simboloTurno;
	
	/**
	 * Guarda la descripcion de la observacion
	 */
	private String descripcionObservacion;
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		boolean fechaI=UtilidadFecha.validarFecha(fechaInicio);
		boolean fechaF=UtilidadFecha.validarFecha(fechaFin);
		
		//System.out.println("Estado Forma: "+estado);
		
		if (estado.equals("generarReporte"))
		{
			if (this.fechaInicio.equals(""))
		    {
		        errores.add("Fecha Inicio", new ActionMessage("errors.required", "El campo Fecha de Inicio"));
		    } 
		    if (this.fechaFin.equals(""))
		    {
		        errores.add("Fecha Fin", new ActionMessage("errors.required", "El campo Fecha de Finalización"));
		    } 
		    if (!this.fechaInicio.equals("") && !this.fechaFin.equals(""))
		    {
		        if (!fechaI)
		        {
		            errores.add("La Fecha de Inicio debe estar en formato dd/mm/aa", new ActionMessage("errors.formatoFechaInvalido", "de inicio"));
		        }
			
		        if (!fechaF)
		        {
		            errores.add("La Fecha de Fin debe estar en formato dd/mm/aa", new ActionMessage("errors.formatoFechaInvalido", "de finalización"));
		        }
		        
		        /**if (codigoObservacion!=0 && codigoCategoria == 0)
		        {
		            errores.add("No ha Elegido Ninguna Categoria", new ActionMessage("errors.required", "El campo Categoria es requerido"));
		        }*/
		        if (fechaI && fechaF)
		        {
		            if (UtilidadFecha.conversionFormatoFechaABD(fechaInicio).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaFin))>=0)
		            {
		            	errores.add("La Fecha de finalización del Reporte debe ser mayor que la fecha de incio", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "de Inicio" , "de Finalización" ));
		            }			                
	            }
	            
		    }
		}		
		
		return errores;
	}
	
	public void clean()
    {
		
        estado="empezar";
        fechaInicio="";
        fechaFin="";
        fechaProgramacion="";
        listado=new ArrayList<HashMap<String, Object>>();
        codigoCategoria=0;
        nombreCategoria="";
        codigoObservacion=0;
                
    }

	/**
	 * @return Retorna codigoCategoria
	 */
	public int getCodigoCategoria()
	{
		return codigoCategoria;
	}

	/**
	 * @param Asigna codigoCategoria
	 */
	public void setCodigoCategoria(int codigoCategoria)
	{
		this.codigoCategoria = codigoCategoria;
	}

	/**
	 * @return Retorna codigoObservacion
	 */
	public int getCodigoObservacion()
	{
		return codigoObservacion;
	}

	/**
	 * @param Asigna codigoObservacion
	 */
	public void setCodigoObservacion(int codigoObservacion)
	{
		this.codigoObservacion = codigoObservacion;
	}

	/**
	 * @return Retorna descripcionObservacion
	 */
	public String getDescripcionObservacion()
	{
		return descripcionObservacion;
	}

	/**
	 * @param Asigna descripcionObservacion
	 */
	public void setDescripcionObservacion(String descripcionObservacion)
	{
		this.descripcionObservacion = descripcionObservacion;
	}

	/**
	 * @return Retorna estado
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * @param Asigna estado
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	/**
	 * @return Retorna fechaFin
	 */
	public String getFechaFin()
	{
		return fechaFin;
	}

	/**
	 * @param Asigna fechaFin
	 */
	public void setFechaFin(String fechaFin)
	{
		this.fechaFin = fechaFin;
	}

	/**
	 * @return Retorna fechaInicio
	 */
	public String getFechaInicio()
	{
		return fechaInicio;
	}

	/**
	 * @param Asigna fechaInicio
	 */
	public void setFechaInicio(String fechaInicio)
	{
		this.fechaInicio = fechaInicio;
	}

	/**
	 * @return Retorna fechaProgramacion
	 */
	public String getFechaProgramacion()
	{
		return fechaProgramacion;
	}

	/**
	 * @param Asigna fechaProgramacion
	 */
	public void setFechaProgramacion(String fechaProgramacion)
	{
		this.fechaProgramacion = fechaProgramacion;
	}

	/**
	 * @return Retorna listado
	 */
	public Collection<HashMap<String, Object>> getListado()
	{
		return listado;
	}

	/**
	 * @param Asigna listado
	 */
	public void setListado(Collection<HashMap<String, Object>> listado)
	{
		this.listado = listado;
	}
	
	/**
	 * @return Retorna categorias
	 */
	public Collection<HashMap<String, Object>> getCategorias()
	{
		return categorias;
	}

	/**
	 * @param Asigna categorias
	 */
	public void setCategorias(Collection<HashMap<String, Object>> categorias)
	{
		this.categorias = categorias;
	}

	/**
	 * @return Retorna nombreCategoria
	 */
	public String getNombreCategoria()
	{
		return nombreCategoria;
	}

	/**
	 * @param Asigna nombreCategoria
	 */
	public void setNombreCategoria(String nombreCategoria)
	{
		this.nombreCategoria = nombreCategoria;
	}

	/**
	 * @return Retorna nombrePersona
	 */
	public String getNombrePersona()
	{
		return nombrePersona;
	}

	/**
	 * @param Asigna nombrePersona
	 */
	public void setNombrePersona(String nombrePersona)
	{
		this.nombrePersona = nombrePersona;
	}

	/**
	 * @return Retorna simboloTurno
	 */
	public String getSimboloTurno()
	{
		return simboloTurno;
	}

	/**
	 * @param Asigna simboloTurno
	 */
	public void setSimboloTurno(String simboloTurno)
	{
		this.simboloTurno = simboloTurno;
	}

	/**
	 * @return Retorna tipoObservacion
	 */
	public String getTipoObservacion()
	{
		return tipoObservacion;
	}

	/**
	 * @param Asigna tipoObservacion
	 */
	public void setTipoObservacion(String tipoObservacion)
	{
		this.tipoObservacion = tipoObservacion;
	}
}
