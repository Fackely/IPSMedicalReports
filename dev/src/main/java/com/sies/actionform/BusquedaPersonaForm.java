/*
 * Creado 4 Ene 2007
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
 * 
 * SiEs
 * @author Juan David Ramírez L.
 *
 */
public class BusquedaPersonaForm extends ActionForm
{	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Listado de personas que arroja la consulta
	 */
	private Collection<HashMap<String, Object>> listadoPersonas;

	/**
	 * Creiterio de búsqueda
	 * true --> Código
	 * false --> Nombre
	 */
	private boolean criterio;
	
	/**
	 * texto buscado
	 */
	private String textoBusqueda;
	
	/**
	 * Indica que se deben buscar las personas que no tienen turnos en las fechas solicitadas
	 */
	private boolean personasSinCuadro;
	
	/**
	 * Fecha inicial para filtrar la búsqueda
	 */
	private String fechaInicial;
	
	/**
	 * Fecha final para filtrar la búsqueda
	 */
	private String fechaFinal;
	
	/**
	 * Filtrar por centros de costo del usuario
	 */
	private boolean filtroCentroCosto;

	/**
	 * Filtrar por centros de costo del usuario
	 */
	private boolean filtroCentroAtencion;

	/**
	 * Código del cuadro de turnos para filtrar la búsqueda
	 */
	private int codigoCuadro;
	
	public BusquedaPersonaForm()
	{
		filtroCentroCosto=false;
		filtroCentroAtencion=true;
	}
	
	/**
	 * @return listadoPersonas
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
	 * @return criterio
	 */
	public boolean getCriterio()
	{
		return criterio;
	}

	/**
	 * @param criterio Asigna criterio
	 */
	public void setCriterio(boolean criterio)
	{
		this.criterio = criterio;
	}

	/**
	 * @return textoBusqueda
	 */
	public String getTextoBusqueda()
	{
		return textoBusqueda;
	}

	/**
	 * @param textoBusqueda Asigna textoBusqueda
	 */
	public void setTextoBusqueda(String textoBusqueda)
	{
		this.textoBusqueda = textoBusqueda;
	}
	
	/**
	 * Método para obtener el número de resultados
	 * @return Numero de elementos del listado
	 */
	public int getSizeListado()
	{
		if(listadoPersonas!=null)
		{
			return listadoPersonas.size();
		}
		else
		{
			return 0;
		}
	}
	
	@Override
	public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1)
	{
		ActionErrors errores=new ActionErrors();
		if(criterio)
		{
			try
			{
				Integer.parseInt(textoBusqueda);
			}
			catch (NumberFormatException e) {
				listadoPersonas=new ArrayList<HashMap<String, Object>>();
				errores.add("codigo", new ActionMessage("errors.integer", "El Código"));
			}
		}
		else if(textoBusqueda==null || textoBusqueda.length()<3)
		{
			listadoPersonas=new ArrayList<HashMap<String, Object>>();
			errores.add("criterio", new ActionMessage("errors.criterioBusquedaNombre"));
		}
		if(personasSinCuadro)
		{
			if(fechaInicial==null || fechaInicial.equalsIgnoreCase(""))
			{
				errores.add("fecha inicial", new ActionMessage("errors.required", "La fecha Inicial"));
			}
			if(fechaFinal==null || fechaFinal.equalsIgnoreCase(""))
			{
				errores.add("fecha final", new ActionMessage("errors.required", "La fecha Final"));
			}
			if(codigoCuadro==0)
			{
				errores.add("Codigo cuadro", new ActionMessage("errors.required", "El código del cuadro"));
			}
		}
		return errores;
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
	 * @return Retorna personasSinCuadro
	 */
	public boolean getPersonasSinCuadro()
	{
		return personasSinCuadro;
	}

	/**
	 * @param Asigna personasSinCuadro
	 */
	public void setPersonasSinCuadro(boolean personasSinCuadro)
	{
		this.personasSinCuadro = personasSinCuadro;
	}

	/**
	 * @return Retorna filtroCentroCosto
	 */
	public boolean getFiltroCentroCosto()
	{
		return filtroCentroCosto;
	}

	/**
	 * @param Asigna filtroCentroCosto
	 */
	public void setFiltroCentroCosto(boolean filtroCentroCosto)
	{
		this.filtroCentroCosto = filtroCentroCosto;
	}

	/**
	 * @return Retorna filtroCentroAtencion
	 */
	public boolean getFiltroCentroAtencion()
	{
		return filtroCentroAtencion;
	}

	/**
	 * @param Asigna filtroCentroAtencion
	 */
	public void setFiltroCentroAtencion(boolean filtroCentroAtencion)
	{
		this.filtroCentroAtencion = filtroCentroAtencion;
	}

	/**
	 * @return Retorna codigoCuadro
	 */
	public int getCodigoCuadro()
	{
		return codigoCuadro;
	}

	/**
	 * @param codigoCuadro Asigna codigoCuadro
	 */
	public void setCodigoCuadro(int codigoCuadro)
	{
		this.codigoCuadro = codigoCuadro;
	}
}
