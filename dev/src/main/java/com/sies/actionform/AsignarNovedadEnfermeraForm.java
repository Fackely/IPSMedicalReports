/*
 * Created on 10/06/2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
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
 * @author Juan David Ramírez López
 *
 * SiEs 12 Diciembre 2006
 */
public class AsignarNovedadEnfermeraForm extends ActionForm
{
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Código de la asociación
	 */
	private int codigoAsociacion;
	
	/**
	 * Para la busqueda del profesional
	 */
	private String textProfesional;

	/**
	 * Log
	 */
	private String log;
	
	/**
	 * Para manejar el estado de la funcionalidad
	 */
	private String estado;
	
	/**
	 * Código de la persona a que se le va a hacer la asignación
	 */
	private int codigoProfesional;
	
	/**
	 * Código de la novedad que se a manejar
	 */
	private int codigoNovedad;
	
	/**
	 * Prioridad, indica si la novedad asignada, puede ser o no obviada
	 */
	private boolean prioridad;
	
	/**
	 * fecha en cual se registra la novedad
	 */
	private String fechaRegistro;
	
	/**
	 * Fecha para la cual se programa la novedad
	 */
	private String fechaProgramacion;
	
	/**
	 * Para manejar la fecha de inicio de la programacion
	 */
	private String fechaInicio;
	
	/**
	 * Para manejar la fecha fin de la programación
	 */
	private String fechaFin;
	
	/**
	 * Para manejar la fecha fin de la programación en Modificación
	 */
	private String fechaFinAnterior;

	/**
	 * Para manejar la fecha inicio de la programación en Modificación
	 */
	private String fechaInicioAnterior;

	/**
	 * Indica si la asociacion de la novedad está activa aún 
	 */
	private boolean activoAsociacion;
	
	/**
	 * La explicación de la asociación de la novedad 
	 */
	private String observacion;
	
	/**
	 * estado antes de la consulta
	 */
	private String estadoAnterior;
	
	/**
	 * guarda el nombre de la enfermera
	 */
	private String nombreProfesional;

	/**
	 * Colección para el manejo de listados 
	 */
	private Collection<HashMap<String, Object>> listado;
	
	/**
	 * Listado de fechas
	 */
	private ArrayList<String> listadoFechas;
	
	/**
	 * Agrupar las novedades para que se muestren como un solo registro
	 */
	boolean agrupar=true;
	
	/**
	 * Manejo de fechas de finalización
	 */
	HashMap<String, String> fechasFinalizacion=new HashMap<String, String>();
	
	/**
	 * Mensaje con el resultado de la ultima acción
	 */
	private String mensajeResultado;
	
	public ActionErrors validate(@SuppressWarnings("unused")
	ActionMapping mapping, @SuppressWarnings("unused")
	HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		if (estado.equalsIgnoreCase("empezar"))
		{
			estadoAnterior=estado;
			return null;
		}
		
		if (estado.equalsIgnoreCase("guardar") || estado.equalsIgnoreCase("guardarModificacion"))
		{
			if (this.codigoNovedad==-1)
			{
				errores.add("Novedad", new ActionMessage("error.novedad.seleccion"));
			}
			if (this.codigoProfesional==-1)
			{
				errores.add("Enfermera", new ActionMessage("error.enfermera.seleccion"));
			}
			
			boolean errorFechas=false;
			if (this.fechaInicio.equals(""))
			{
				errorFechas=true;
				errores.add("Fecha Inicio", new ActionMessage("errors.required", "El campo fecha de inicio"));
			}
			else if(!UtilidadFecha.validarFecha(fechaInicio))
			{
				errores.add("La fecha de inicio es inválida", new ActionMessage("errors.formatoFechaInvalido", "de inicio"));
				errorFechas=true;
			}
			if (this.fechaFin.equals(""))
			{
				errorFechas=true;
				errores.add("Fecha Fin", new ActionMessage("errors.required", "El campo fecha de fin"));
			}
			else if(!UtilidadFecha.validarFecha(fechaFin))
			{
				errores.add("La fecha de fin es inválida", new ActionMessage("errors.formatoFechaInvalido", "de fin"));
				errorFechas=true;
			}
			
			if(!errorFechas)
			{
				String fechaActual=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
				if(UtilidadFecha.conversionFormatoFechaABD(this.getFechaInicio()).compareTo(fechaActual)<0)
				{
					errores.add("La fecha de inicio debe ser mayor o igual que la fecha actual", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia", "Inicio", "Actual"));
				}

				if(UtilidadFecha.esFechaValidaSegunAp(this.getFechaFin()))
				{
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.getFechaFin(), this.getFechaInicio()))
					{
						errores.add("La fecha de fin debe ser mayor o igual que la fecha de inicio", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia", "de fin", "de Inicio"));
					}
				}
			}
		}
		
		if (estado.equals("guardarModificacion"))
		{
			if (this.codigoNovedad==-1)
			{
				errores.add("Novedad", new ActionMessage("error.novedad.seleccion"));
			}
			if (this.codigoProfesional==-1)
			{
				errores.add("Enfermera", new ActionMessage("error.enfermera.seleccion"));
			}
			
			if (this.fechaProgramacion.equals(""))
			{
				errores.add("Fecha Programacion", new ActionMessage("errors.required", "El campo Fecha de Programación"));
			} 

			if(UtilidadFecha.esFechaValidaSegunAp(this.getFechaProgramacion()))
			{
				String fechaActual=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
				if(UtilidadFecha.conversionFormatoFechaABD(this.getFechaProgramacion()).compareTo(fechaActual)<0)
				{
					errores.add("La fecha de programación debe ser mayor que la fecha actual", new ActionMessage("errors.fechaAnteriorIgualActual", "de programación", "actual"));
				}

			}
			else
			{
				errores.add("La fecha de programación es inválida", new ActionMessage("errors.formatoFechaInvalido", "de programación"));
			}
		}
		
		if(!errores.isEmpty())
		{
			mensajeResultado="";
			estado=estadoAnterior;
		}
		
		return errores;
 	}
 
	
	public void clean()
	{
		this.codigoProfesional=0;
		this.codigoNovedad=-1;
		this.prioridad=false;
		this.fechaRegistro="";
		this.fechaProgramacion="";
		this.activoAsociacion=true;
		this.observacion="";
		this.nombreProfesional="";
		this.listado=new ArrayList<HashMap<String, Object>>();
		this.codigoAsociacion=0;
		this.estadoAnterior="";
		this.mensajeResultado="";
		this.fechaInicio="";
		this.fechaFin="";
		this.listadoFechas=new ArrayList<String>();
		this.agrupar=true;
	}


	/**
	 * @return activoAsociacion
	 */
	public boolean getActivoAsociacion()
	{
		return activoAsociacion;
	}


	/**
	 * @param activoAsociacion Asigna activoAsociacion
	 */
	public void setActivoAsociacion(boolean activoAsociacion)
	{
		this.activoAsociacion = activoAsociacion;
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
	 * @return codigoNovedad
	 */
	public int getCodigoNovedad()
	{
		return codigoNovedad;
	}


	/**
	 * @param codigoNovedad Asigna codigoNovedad
	 */
	public void setCodigoNovedad(int codigoNovedad)
	{
		this.codigoNovedad = codigoNovedad;
	}


	/**
	 * @return codigoProfesional
	 */
	public int getCodigoProfesional()
	{
		return codigoProfesional;
	}


	/**
	 * @param codigoProfesional Asigna codigoProfesional
	 */
	public void setCodigoProfesional(int codigoProfesional)
	{
		this.codigoProfesional = codigoProfesional;
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
	 * @return estadoAnterior
	 */
	public String getEstadoAnterior()
	{
		return estadoAnterior;
	}


	/**
	 * @param estadoAnterior Asigna estadoAnterior
	 */
	public void setEstadoAnterior(String estadoAnterior)
	{
		this.estadoAnterior = estadoAnterior;
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
	 * @return fechaProgramacion
	 */
	public String getFechaProgramacion()
	{
		return fechaProgramacion;
	}


	/**
	 * @param fechaProgramacion Asigna fechaProgramacion
	 */
	public void setFechaProgramacion(String fechaProgramacion)
	{
		this.fechaProgramacion = fechaProgramacion;
	}


	/**
	 * @return fechaRegistro
	 */
	public String getFechaRegistro()
	{
		return fechaRegistro;
	}


	/**
	 * @param fechaRegistro Asigna fechaRegistro
	 */
	public void setFechaRegistro(String fechaRegistro)
	{
		this.fechaRegistro = fechaRegistro;
	}


	/**
	 * @return listado
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
	 * @return log
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
	 * @return nombreProfesional
	 */
	public String getNombreProfesional()
	{
		return nombreProfesional;
	}

	/**
	 * @param nombreProfesional Asigna nombreProfesional
	 */
	public void setNombreProfesional(String nombreProfesional)
	{
		this.nombreProfesional = nombreProfesional;
	}

	/**
	 * @return observacion
	 */
	public String getObservacion()
	{
		return observacion;
	}

	/**
	 * @param observacion Asigna observacion
	 */
	public void setObservacion(String observacion)
	{
		this.observacion = observacion;
	}

	/**
	 * @return prioridad
	 */
	public boolean getPrioridad()
	{
		return prioridad;
	}

	/**
	 * @param prioridad Asigna prioridad
	 */
	public void setPrioridad(boolean prioridad)
	{
		this.prioridad = prioridad;
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

	/**
	 * @return listadoFechas
	 */
	public ArrayList<String> getListadoFechas()
	{
		return listadoFechas;
	}

	/**
	 * @param listadoFechas Asigna listadoFechas
	 */
	public void setListadoFechas(ArrayList<String> listadoFechas)
	{
		this.listadoFechas = listadoFechas;
	}

	/**
	 * @return agrupar
	 */
	public boolean getAgrupar()
	{
		return agrupar;
	}

	/**
	 * @param agrupar Asigna agrupar
	 */
	public void setAgrupar(boolean agrupar)
	{
		this.agrupar = agrupar;
	}


	/**
	 * @return fechasFinalizacion
	 */
	public HashMap<String, String> getFechasFinalizacion()
	{
		return fechasFinalizacion;
	}


	/**
	 * @param fechasFinalizacion Asigna fechasFinalizacion
	 */
	public void setFechasFinalizacion(HashMap<String, String> fechasFinalizacion)
	{
		this.fechasFinalizacion = fechasFinalizacion;
	}


	/**
	 * @return Retorna fechaFinAnterior
	 */
	public String getFechaFinAnterior()
	{
		return fechaFinAnterior;
	}


	/**
	 * @param Asigna fechaFinAnterior
	 */
	public void setFechaFinAnterior(String fechaFinAnterior)
	{
		this.fechaFinAnterior = fechaFinAnterior;
	}


	/**
	 * @return Retorna fechaInicioAnterior
	 */
	public String getFechaInicioAnterior()
	{
		return fechaInicioAnterior;
	}


	/**
	 * @param Asigna fechaInicioAnterior
	 */
	public void setFechaInicioAnterior(String fechaInicioAnterior)
	{
		this.fechaInicioAnterior = fechaInicioAnterior;
	}
}
