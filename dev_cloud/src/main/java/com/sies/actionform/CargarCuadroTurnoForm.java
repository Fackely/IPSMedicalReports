/*
 * Creado en 29/11/2006
 *
 * Juan David Ramírez
 * Si.Es.
 */
package com.sies.actionform;

import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import util.UtilidadCadena;
import util.UtilidadFecha;

/**
 * @author Juan David Ramírez
 *
 * Si.Es.
 */
public class CargarCuadroTurnoForm extends GenerarCuadroTurnosForm 
{
	/**
	 * Manejo versión serial
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Observaciones del turno anterior
	 */
	private String observacionesTurno;
	
	/**
	 * FechaCambioObservacion
	 */
	private String fechaCambio;
	
	/**
	 * Indice de la persona a la cual se le realizará el cambio de turno
	 */
	private int indicePersona;
	
	/**
	 * Nombre del profesional para realizar las búsquedas
	 */
	private String nombreProfesional;
	
	/**
	 * Para la búsqueda
	 */
	private String textProfesional;
	
	/**
	 * Índice del día que se está trabajando
	 */
	private int dia;
	
	/**
	 * Listado de opciones para el cambio de turno
	 */
	private Collection<HashMap<String, Object>> listadoOpciones;
	
	/**
	 * Este atributo me permite tener en memoria los turnos de las personas
	 * para no tener la necesidad de volver a la base de datos
	 */
	private Collection<HashMap<String, Object>> listadoTurnosEnfermeras;
	
	/**
	 * Para buscar los cuadros de turnos desde un listado
	 */
	private String fechaBusqueda;
	
	public void clean()
	{
		super.clean();
		this.codigoTurnoObservacion=0;
		this.nombreProfesional="";
		this.textProfesional="";
		this.fechaBusqueda=UtilidadFecha.getFechaActual();
	}

	public ActionErrors validate(@SuppressWarnings("unused")
	ActionMapping mapping, @SuppressWarnings("unused")
	HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		//System.out.println("Estado CargarCuadroTurnoForm Forma: "+estado);
		
		if (estado.equals("consultar") || estado.equals("realizarObservacion") || estado.equals("consultarPorCategoria"))
		{
	        if (codigoCategoria==-1)
	        {
	            errores.add("No ha Elegido Ninguna Categoría", new ActionMessage("errors.required", "El campo Categoria"));
	        }
		}
		if (estado.equals("consultarPorEnfermera") || estado.equals("realizarObservacion") || estado.equals("consultarPorCategoria"))
		{
			boolean error=false;
			if(!UtilidadCadena.noEsVacio(this.getFechaInicio()))
			{
	            errores.add("No ha Elegido Fecha de Inicio", new ActionMessage("errors.required", "El campo Fecha de inicio"));
				error=true;
			}
			else if(!UtilidadFecha.esFechaValidaSegunAp(this.getFechaInicio()))
	        {
	            errores.add("La fecha de inicio es invalida", new ActionMessage("errors.formatoFechaInvalido", "de inicio"));					
                error=true;
	        }
			
			if(!UtilidadCadena.noEsVacio(this.getFechaFin()))
			{
	            errores.add("No ha Elegido Fecha de Fin", new ActionMessage("errors.required", "El campo Fecha de finalización"));
				error=true;
			}
			else if(!UtilidadFecha.esFechaValidaSegunAp(this.getFechaFin()))
	        {
                errores.add("La fecha de fin es invalida", new ActionMessage("errors.formatoFechaInvalido", "de finalización"));
                error=true;
	        }
			
            if(!error && UtilidadFecha.esFechaMenorQueOtraReferencia(this.getFechaFin(), this.getFechaInicio()))
            {
                errores.add("La fecha de fin debe ser mayor o igual que la fecha de inicio", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "de finalización", "de inicio"));
            }
		}
		if (estado.equals("consultarPorEnfermera"))
		{
	        if (codigoEnfermera==-1)
	        {
	            errores.add("No ha Elegido Ninguna Enfermera", new ActionMessage("errors.required", "El campo Enfermera"));
	        }
		}
		return errores;
	}

	/**
	 * @return fechaCambio
	 */
	public String getFechaCambio()
	{
		return fechaCambio;
	}

	/**
	 * @param fechaCambio Asigna fechaCambio
	 */
	public void setFechaCambio(String fechaCambio)
	{
		this.fechaCambio = fechaCambio;
	}

	/**
	 * @return indicePersona
	 */
	public int getIndicePersona()
	{
		return indicePersona;
	}

	/**
	 * @param indicePersona Asigna indicePersona
	 */
	public void setIndicePersona(int indicePersona)
	{
		this.indicePersona = indicePersona;
	}

	/**
	 * @return observacionesTurno
	 */
	public String getObservacionesTurno()
	{
		return observacionesTurno;
	}

	/**
	 * @param observacionesTurno Asigna observacionesTurno
	 */
	public void setObservacionesTurno(String observacionesTurno)
	{
		this.observacionesTurno = observacionesTurno;
	}

	/**
	 * @return listadoOpciones
	 */
	public Collection<HashMap<String, Object>> getListadoOpciones()
	{
		return listadoOpciones;
	}

	/**
	 * @param listadoOpciones Asigna listadoOpciones
	 */
	public void setListadoOpciones(Collection<HashMap<String, Object>> listadoOpciones)
	{
		this.listadoOpciones = listadoOpciones;
	}

	/**
	 * @return novedad
	 */
	public int getNovedad()
	{
		return novedad;
	}

	/**
	 * @param novedad Asigna novedad
	 */
	public void setNovedad(int novedad)
	{
		this.novedad = novedad;
	}

	/**
	 * @return Retorna nombreProfesional
	 */
	public String getNombreProfesional()
	{
		return nombreProfesional;
	}

	/**
	 * @param Asigna nombreProfesional
	 */
	public void setNombreProfesional(String nombreProfesional)
	{
		this.nombreProfesional = nombreProfesional;
	}

	/**
	 * @return Retorna textProfesional
	 */
	public String getTextProfesional()
	{
		return textProfesional;
	}

	/**
	 * @param Asigna textProfesional
	 */
	public void setTextProfesional(String textProfesional)
	{
		this.textProfesional = textProfesional;
	}

	/**
	 * @return Retorna dia
	 */
	public int getDia()
	{
		return dia;
	}

	/**
	 * @param Asigna dia
	 */
	public void setDia(int dia)
	{
		this.dia = dia;
	}

	/**
	 * @return Retorna listadoTurnosEnfermeras
	 */
	public Collection<HashMap<String, Object>> getListadoTurnosEnfermeras()
	{
		return listadoTurnosEnfermeras;
	}

	/**
	 * @param Asigna listadoTurnosEnfermeras
	 */
	public void setListadoTurnosEnfermeras(
			Collection<HashMap<String, Object>> listadoTurnosEnfermeras)
	{
		this.listadoTurnosEnfermeras = listadoTurnosEnfermeras;
	}

	/**
	 * @return Obtiene fechaBusqueda
	 */
	public String getFechaBusqueda()
	{
		return fechaBusqueda;
	}

	/**
	 * @param fechaBusqueda Asigna fechaBusqueda
	 */
	public void setFechaBusqueda(String fechaBusqueda)
	{
		this.fechaBusqueda = fechaBusqueda;
	}
}
