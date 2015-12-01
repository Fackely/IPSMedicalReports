/*
 * Creado en 11/12/2005
 *
 * Karenth Marín
 * Si.Es.
 */
package com.sies.actionform;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import util.UtilidadCadena;
import util.UtilidadFecha;

/**
 * @author Karenth Marín
 *
 * Si.Es.
 */
public class GenerarReporteNovedadesForm extends ActionForm
{

	/**
	 * Default serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Para manejar el estado en el que va a realizar la accion
	 */
    private String estado; 
	    
    /**
     * Para el manejo de la fecha
     */
    private String fechaProgramacion;
	    
    /**
     * para manejar la fecha de inicio
     */
    private String fechaInicio;
	    
    /**
     * Para manejar la fecha de Finalización de la asociación
     */
    private String fechaFin;
	    
    /**
     * Para guardar el codigo del profesional
     */
    private int codigoProfesional;
	    
    /**
     * Para guardar el nomnbre del profesional
     */
    private String nombreProfesional;
	    
    /**
     * Muestra las novedades
     */
    private int codigoNovedad;
	    
    /**
     * Para listar las novedades existentes
     */
    private Collection listado;
	    
    /**
     * Cadena para el ingreso de la enfermara(Codigo o nombre)
     */
    private String textProfesional;
	
	
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
		 * @return Retorna fechaFin.
		 */
		public String getFechaFin()
		{
			return fechaFin;
		}
		/**
		 * @param fechaFin Asigna fechaFin.
		 */
		public void setFechaFin(String fechaFin)
		{
			this.fechaFin = fechaFin;
		}
		/**
		 * @return Retorna fechaInicio.
		 */
		public String getFechaInicio()
		{
			return fechaInicio;
		}
		/**
		 * @param fechaInicio Asigna fechaInicio.
		 */
		public void setFechaInicio(String fechaInicio)
		{
			this.fechaInicio = fechaInicio;
		}
		
		public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
		{
			ActionErrors errores= new ActionErrors();
			
			//System.out.println("Estado Forma: "+estado);
			
			if (estado.equals("generarReporte"))
			{
				if(UtilidadCadena.noEsVacio(fechaInicio))
				{
					if(!UtilidadFecha.esFechaValidaSegunAp(fechaInicio))
						errores.add("La Fecha de Inicio debe estar en formato dd/mm/aa", new ActionMessage("errors.formatoFechaInvalido", "de inicio"));
				}
				if(UtilidadCadena.noEsVacio(fechaFin))
				{
					if(!UtilidadFecha.esFechaValidaSegunAp(fechaFin))
						errores.add("La Fecha de Fin debe estar en formato dd/mm/aa", new ActionMessage("errors.formatoFechaInvalido", "de fin"));
				}
				if(UtilidadCadena.noEsVacio(fechaInicio) && UtilidadCadena.noEsVacio(fechaFin) && UtilidadFecha.esFechaValidaSegunAp(fechaInicio) && UtilidadFecha.esFechaValidaSegunAp(fechaFin))
				{
		            if (UtilidadFecha.esFechaMenorQueOtraReferencia(fechaFin, fechaInicio))
		            {
		            	errores.add("La Fecha de fin del Reporte debe ser mayor que la fecha de inicio", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "de Inicio" , "de Fin" ));
		            }			                
			    }
				if(codigoNovedad!=0 && codigoProfesional==0)
				{
					errores.add("Codigo Enfermera", new ActionMessage("errors.required", "El campo Enfermera es requerido"));
				}
			}		
			
			return errores;
		}
		
		public void clean()
	    {
	        estado="empezar";
	        codigoProfesional=0;
	        nombreProfesional="";
	        codigoNovedad=0;
	        fechaInicio="";
	        fechaFin="";
	        fechaProgramacion="";
	                
	    }
		/**
		 * @return Retorna codigoProfesional.
		 */
		public int getCodigoProfesional()
		{
			return codigoProfesional;
		}
		/**
		 * @param codigoProfesional Asigna codigoProfesional.
		 */
		public void setCodigoProfesional(int codigoProfesional)
		{
			this.codigoProfesional = codigoProfesional;
		}
		/**
		 * @return Retorna nombreProfesional.
		 */
		public String getNombreProfesional()
		{
			return nombreProfesional;
		}
		/**
		 * @param nombreProfesional Asigna nombreProfesional.
		 */
		public void setNombreProfesional(String nombreProfesional)
		{
			this.nombreProfesional = nombreProfesional;
		}
		
		public String getTextProfesional() 
		{
			return textProfesional;
		}
		public void setTextProfesional(String textProfesional) 
		{
			this.textProfesional = textProfesional;
		}
		
		public Collection getListado() 
		{
			return listado;
		}
		
		public void setListado(Collection listado) 
		{
			this.listado = listado;
		}
		
		public int getCodigoNovedad() 
		{
			return codigoNovedad;
		}
		
		public void setCodigoNovedad(int codigoNovedad) 
		{
			this.codigoNovedad = codigoNovedad;
		}
		
		public String getFechaProgramacion() 
		{
			return fechaProgramacion;
		}
		
		public void setFechaProgramacion(String fechaProgramacion) 
		{
			this.fechaProgramacion = fechaProgramacion;
		}
}