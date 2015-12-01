/**
 * 
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

import util.UtilidadFecha;

/**
 * @author Juan David Ramírez
 *
 */
public class IngresarPersonaCuadroForm extends ActionForm
{
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Manejar los estados de la clase
	 */
	private String estado;
	
	/**
	 * Código de la persona a agregar
	 */
	private int codigoPersona;
	
	/**
	 * Nombre de la persona que se desea ingresar al cuadro
	 */
	private String nombrePersona;

	/**
	 * Código de la persona a eliminar
	 */
	private int codigoPersonaEliminar;

	/**
	 * Cadena para el ingreso de la persona (Código o nombre)
	 */
	private String textProfesional;

	/**
	 * Fecha inicial cuadro de turnos seleccionado
	 */
	private String fechaInicialCuadro;
	
	/**
	 * Fecha final cuadro de turnos seleccionado
	 */
	private String fechaFinalCuadro;
	
	/**
	 * Fecha inicial de ingreso al cuadro
	 */
	private String fechaInicial;
	
	/**
	 * Fecha final de asignación al cuadro de turnos
	 */
	private String fechaFinal;
	
	/**
	 * Indica si el ingreso al cuadro de turnos es temporla o permanente
	 */
	private boolean temporal;
	
	/**
	 * Fecha para buscar el cuadro al cual se desea ingresar la persona
	 */
	private String fechaBusqueda;
	
	/**
	 * Listado de cuadros de turnos para eliminar
	 */
	private Collection<HashMap<String, Object>> listadoCuadros;

	/**
	 * Listado de cuadros de turnos para eliminar
	 */
	private Collection<HashMap<String, Object>> listadoPersonas;

	/**
	 * Código del cuadro de turnos al cual se va a ingresar la persona
	 */
	private int codigoCuadro;
	
	/***
	 * Nombre del cuadro de turnos al cual se le va a ingresar la persona
	 */
	private String nombreCuadro;
	
	public void reset()
	{
		estado = "";
		codigoPersona = 0;
		nombrePersona = "";
		textProfesional = "";
		fechaInicial = "";
		fechaFinal = "";
		temporal = false;
		fechaBusqueda = UtilidadFecha.getFechaActual();
		listadoCuadros = new ArrayList<HashMap<String, Object>>();
		codigoCuadro = 0;
	}
	
	@Override
	public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1)
	{
		if(estado.equalsIgnoreCase("guardar"))
		{
			ActionErrors errores=new ActionErrors();
			if(codigoPersona==0)
			{
				errores.add("persona", new ActionMessage("errors.required", "La persona"));
			}
			if(codigoCuadro==0)
			{
				errores.add("cuadro", new ActionMessage("errors.required", "El cuadro de Turnos"));
			}
			if(fechaInicial.equals(""))
			{
				errores.add("fehcaInicial", new ActionMessage("errors.required", "La fecha Inicial"));
			}
			else if(UtilidadFecha.conversionFormatoFechaABD(fechaInicial).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaInicialCuadro))<0)
			{
				errores.add("fehcaInicial", new ActionMessage("errors.fechaAnteriorIgualActual", "Inicial: "+fechaInicial, "Inicial Cuadro: "+fechaInicialCuadro));
			}
			if(fechaFinal.equals(""))
			{
				errores.add("fehcaFinal", new ActionMessage("errors.required", "La fecha Final"));
			}
			else if(UtilidadFecha.conversionFormatoFechaABD(fechaFinal).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaFinalCuadro))>0)
			{
				errores.add("fechaFinal", new ActionMessage("errors.fechaPosteriorIgualActual", "Final: "+fechaFinal, "Final Cuadro: "+fechaFinalCuadro));
			}
			else if(UtilidadFecha.conversionFormatoFechaABD(fechaInicial).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaFinal))>0)
			{
				errores.add("fechas", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial: "+fechaInicial, "Final: "+fechaFinal));
			}
			return errores;
		}
		return null;
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
	 * @return Retorna codigoPersona
	 */
	public int getCodigoPersona()
	{
		return codigoPersona;
	}

	/**
	 * @param Asigna codigoPersona
	 */
	public void setCodigoPersona(int codigoPersona)
	{
		this.codigoPersona = codigoPersona;
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
	 * @return Retorna fechaFinal
	 */
	public String getFechaFinal()
	{
		return fechaFinal;
	}

	/**
	 * @param Asigna fechaFinal
	 */
	public void setFechaFinal(String fechaFinal)
	{
		this.fechaFinal = fechaFinal;
	}

	/**
	 * @return Retorna fechaInicial
	 */
	public String getFechaInicial()
	{
		return fechaInicial;
	}

	/**
	 * @param Asigna fechaInicial
	 */
	public void setFechaInicial(String fechaInicial)
	{
		this.fechaInicial = fechaInicial;
	}

	/**
	 * @return Retorna temporal
	 */
	public boolean getTemporal()
	{
		return temporal;
	}

	/**
	 * @param Asigna temporal
	 */
	public void setTemporal(boolean temporal)
	{
		this.temporal = temporal;
	}

	/**
	 * @return Retorna fechaBusquedaEliminar
	 */
	public String getFechaBusqueda()
	{
		return fechaBusqueda;
	}

	/**
	 * @param Asigna fechaBusquedaEliminar
	 */
	public void setFechaBusqueda(String fechaBusqueda)
	{
		this.fechaBusqueda = fechaBusqueda;
	}

	/**
	 * @return Retorna codigoCuadro
	 */
	public int getCodigoCuadro()
	{
		return codigoCuadro;
	}

	/**
	 * @param Asigna codigoCuadro
	 */
	public void setCodigoCuadro(int codigoCuadroTurnos)
	{
		this.codigoCuadro = codigoCuadroTurnos;
	}

	/**
	 * @return Retorna listadoCuadros
	 */
	public Collection<HashMap<String, Object>> getListadoCuadros()
	{
		return listadoCuadros;
	}

	/**
	 * @param Asigna listadoCuadros
	 */
	public void setListadoCuadros(Collection<HashMap<String, Object>> listadoCuadros)
	{
		this.listadoCuadros = listadoCuadros;
	}

	/**
	 * @return Retorna listadoPersonas
	 */
	public Collection<HashMap<String, Object>> getListadoPersonas()
	{
		return listadoPersonas;
	}

	/**
	 * @param listadoPersonas Asigna listadoPersonas
	 */
	public void setListadoPersonas(Collection<HashMap<String, Object>> listadoPersonas)
	{
		this.listadoPersonas = listadoPersonas;
	}

	/**
	 * @return Retorna fechaFinalCuadro
	 */
	public String getFechaFinalCuadro()
	{
		return fechaFinalCuadro;
	}

	/**
	 * @param fechaFinalCuadro Asigna fechaFinalCuadro
	 */
	public void setFechaFinalCuadro(String fechaFinalCuadro)
	{
		this.fechaFinalCuadro = fechaFinalCuadro;
	}

	/**
	 * @return Retorna fechaInicialCuadro
	 */
	public String getFechaInicialCuadro()
	{
		return fechaInicialCuadro;
	}

	/**
	 * @param fechaInicialCuadro Asigna fechaInicialCuadro
	 */
	public void setFechaInicialCuadro(String fechaInicialCuadro)
	{
		this.fechaInicialCuadro = fechaInicialCuadro;
	}

	/**
	 * @return Retorna codigoPersonaEliminar
	 */
	public int getCodigoPersonaEliminar()
	{
		return codigoPersonaEliminar;
	}

	/**
	 * @param codigoPersonaEliminar Asigna codigoPersonaEliminar
	 */
	public void setCodigoPersonaEliminar(int codigoPersonaEliminar)
	{
		this.codigoPersonaEliminar = codigoPersonaEliminar;
	}

	/**
	 * @return Obtiene nombreCuadro
	 */
	public String getNombreCuadro()
	{
		return nombreCuadro;
	}

	/**
	 * @param nombreCuadro Asigna nombreCuadro
	 */
	public void setNombreCuadro(String nombreCuadro)
	{
		this.nombreCuadro = nombreCuadro;
	}
}
