/*
 * Created on 12/04/2005
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
 * @author karenth
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class VacacionesForm extends ActionForm{
	
	/**
	 * Serial Verison UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constante para el manejo de la consulta por fechas
	 */
	public static final byte OPCION_CONSULTA_FECHAS=0;

	/**
	 * Constante para el manejo de la consulta por enfermera 
	 */
	public static final byte OPCION_CONSULTA_ENFERMERA=1;
	
	/**
	 * Código del profesional de la salud
	 */
	private int codigoPersona;
	
	
	/**
	 * Fecha de inicio que estaba en el form ante de la modificación
	 */
	private String fechaAnterior;
	
	/**
	 * Fecha de finalización que estaba el el form antes de la modificación
	 */
	private String fechaPosterior;
	
	/**
	 * Nombre de la enfermera, para visualizarse
	 */
	private String nombreEnfermera;
	
	/**
	 * Fecha de inicio de las vacaciones del profesional de la salud
	 */
	private String fechaInicio;
	
	/**
	 * Fecha de finalización de las vacaciones del profesional de la Salud 
	 */
	private String fechaFin;
	
	private String estado;
	
	private boolean modificando;
	
	private String textProfesional;
	
	private Collection<HashMap<String, Object>> listado;
	 
	/**
	 * Se guarda la información a poner en el log
	 */
	private String log;
	 
	private String mensajeResultado;
	
	private int opcionConsulta;
	
	/**
	 * Conservar rango de fechas de búsqueda
	 */
	private String fechaInicioBusqueda="";
	/**
	 * Conservar rango de fechas de búsqueda
	 */
	private String fechaFinBusqueda="";
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores=new ActionErrors();
		
		//System.out.println("Estado: "+estado);
		
		if((estado.equals("consultando") || estado.equals("editando")) && opcionConsulta==OPCION_CONSULTA_FECHAS)
		{
			boolean errorEnFechas=false;
			if(fechaInicio.trim().equalsIgnoreCase(""))
			{
				errorEnFechas=true;
				errores.add("fechaInicio", new ActionMessage("errors.required", "Fecha Inicio"));
			}
			else if(!UtilidadFecha.validarFecha(fechaInicio))
			{
				errorEnFechas=true;
				errores.add("fechaInicio", new ActionMessage("errors.formatoFechaInvalido", "Fecha Inicio"));
			}
			if(fechaFin.trim().equalsIgnoreCase(""))
			{
				errorEnFechas=true;
				errores.add("fechaInicio", new ActionMessage("errors.required", "Fecha Fin"));
			}
			else if(!UtilidadFecha.validarFecha(fechaFin))
			{
				errorEnFechas=true;
				errores.add("fechaFin", new ActionMessage("errors.formatoFechaInvalido", "Fecha Fin"));
			}
			if(!errorEnFechas)
			{
				if(UtilidadFecha.conversionFormatoFechaABD(fechaInicio).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaFin))>0)
				{
					errores.add("fechaIniMayor", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Inicio", "Fin"));
				}
			}
		}
		
		if(estado.equalsIgnoreCase("guardar"))
		{
			
		    if (this.codigoPersona==0)
		    {
		        errores.add("Nombre Enfermera", new ActionMessage("errors.required", "El campo Enfermera"));
		    }
		    
			boolean errorEnFechas=false;
			if(fechaInicio.trim().equalsIgnoreCase(""))
			{
				errorEnFechas=true;
				errores.add("fechaInicio", new ActionMessage("errors.required", "Fecha Inicio"));
			}
			else if(!UtilidadFecha.validarFecha(fechaInicio))
			{
				errorEnFechas=true;
				errores.add("fechaInicio", new ActionMessage("errors.formatoFechaInvalido", "Fecha Inicio"));
			}
			else
			{
                if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.getFechaInicio(), UtilidadFecha.getFechaActual()))
                {
                    errores.add("La fecha de inicio debe ser mayor que la fecha actual", new ActionMessage("errors.fechaAnteriorIgualActual", "de inicio", "actual"));
                }
			}
			if(fechaFin.trim().equalsIgnoreCase(""))
			{
				errorEnFechas=true;
				errores.add("fechaInicio", new ActionMessage("errors.required", "Fecha Fin"));
			}
			else if(!UtilidadFecha.validarFecha(fechaFin))
			{
				errorEnFechas=true;
				errores.add("fechaFin", new ActionMessage("errors.formatoFechaInvalido", "Fecha Fin"));
			}
			if(!errorEnFechas)
			{
				if(UtilidadFecha.conversionFormatoFechaABD(fechaInicio).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaFin))>0)
				{
					errores.add("fechaIniMayor", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Inicio", "Fin"));
				}
			}

		}
		
		if (estado.equals("guardarModificacion"))
		{
            if(UtilidadFecha.esFechaValidaSegunAp(this.getFechaInicio()))
            {
                if(UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getFechaInicio(), UtilidadFecha.getFechaActual()))
                {
                    errores.add("La fecha de inicio debe ser mayor que la fecha actual", new ActionMessage("errors.fechaAnteriorIgualActual", "de inicio", "actual"));
                }

                if(UtilidadFecha.esFechaValidaSegunAp(this.getFechaFin()))
                {
                    if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.getFechaFin(), this.getFechaInicio()))
                    {
                        errores.add("La fecha de fin debe ser mayor o igual que la fecha de inicio", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "de fin", "de inicio"));
                    }
                }
                else
                    errores.add("La fecha de fin es invalida", new ActionMessage("errors.formatoFechaInvalido", "de fin"));
            
            }
            else
                errores.add("La fecha de inicio es invalida", new ActionMessage("errors.formatoFechaInvalido", "de inicio"));
		}
		if(!errores.isEmpty())
		{
			mensajeResultado="";
			estado="modificarEleccion";
		}
		return errores;
	}
	
	/**
	 * Limpiar e inicializar atributos 
	 */
	public void clean()
	{
		this.codigoPersona=0;
		this.fechaInicio="";
		this.fechaFin="";
		this.listado=new ArrayList<HashMap<String, Object>>();
		this.mensajeResultado="";
		this.nombreEnfermera="";
		this.opcionConsulta=-1;
		
		this.fechaInicioBusqueda="";
		this.fechaFinBusqueda="";
		this.modificando=false;
	}

	/**
	 * @return Obtiene codigoPersona
	 */
	public int getCodigoPersona()
	{
		return codigoPersona;
	}

	/**
	 * @param codigoPersona Asigna codigoPersona
	 */
	public void setCodigoPersona(int codigoPersona)
	{
		this.codigoPersona = codigoPersona;
	}

	/**
	 * @return Obtiene fechaAnterior
	 */
	public String getFechaAnterior()
	{
		return fechaAnterior;
	}

	/**
	 * @param fechaAnterior Asigna fechaAnterior
	 */
	public void setFechaAnterior(String fechaAnterior)
	{
		this.fechaAnterior = fechaAnterior;
	}

	/**
	 * @return Obtiene fechaPosterior
	 */
	public String getFechaPosterior()
	{
		return fechaPosterior;
	}

	/**
	 * @param fechaPosterior Asigna fechaPosterior
	 */
	public void setFechaPosterior(String fechaPosterior)
	{
		this.fechaPosterior = fechaPosterior;
	}

	/**
	 * @return Obtiene nombreEnfermera
	 */
	public String getNombreEnfermera()
	{
		return nombreEnfermera;
	}

	/**
	 * @param nombreEnfermera Asigna nombreEnfermera
	 */
	public void setNombreEnfermera(String nombreEnfermera)
	{
		this.nombreEnfermera = nombreEnfermera;
	}

	/**
	 * @return Obtiene fechaInicio
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
	 * @return Obtiene fechaFin
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
	 * @return Obtiene estado
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
	 * @return Obtiene modificando
	 */
	public boolean getModificando()
	{
		return modificando;
	}

	/**
	 * @param modificando Asigna modificando
	 */
	public void setModificando(boolean modificando)
	{
		this.modificando = modificando;
	}

	/**
	 * @return Obtiene textProfesional
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

	/**
	 * @return Obtiene listado
	 */
	public Collection<HashMap<String, Object>> getListado()
	{
		return listado;
	}

	/**
	 * @param listado Asigna listado
	 */
	public void setListado(Collection<HashMap<String, Object>> listado)
	{
		this.listado = listado;
	}

	/**
	 * @return Obtiene log
	 */
	public String getLog()
	{
		return log;
	}

	/**
	 * @param log Asigna log
	 */
	public void setLog(String log)
	{
		this.log = log;
	}

	/**
	 * @return Obtiene mensajeResultado
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
	 * @return Obtiene opcionConsulta
	 */
	public int getOpcionConsulta()
	{
		return opcionConsulta;
	}

	/**
	 * @param opcionConsulta Asigna opcionConsulta
	 */
	public void setOpcionConsulta(int opcionConsulta)
	{
		this.opcionConsulta = opcionConsulta;
	}

	/**
	 * @return Obtiene fechaInicioBusqueda
	 */
	public String getFechaInicioBusqueda()
	{
		return fechaInicioBusqueda;
	}

	/**
	 * @param fechaInicioBusqueda Asigna fechaInicioBusqueda
	 */
	public void setFechaInicioBusqueda(String fechaInicioBusqueda)
	{
		this.fechaInicioBusqueda = fechaInicioBusqueda;
	}

	/**
	 * @return Obtiene fechaFinBusqueda
	 */
	public String getFechaFinBusqueda()
	{
		return fechaFinBusqueda;
	}

	/**
	 * @param fechaFinBusqueda Asigna fechaFinBusqueda
	 */
	public void setFechaFinBusqueda(String fechaFinBusqueda)
	{
		this.fechaFinBusqueda = fechaFinBusqueda;
	}
}
