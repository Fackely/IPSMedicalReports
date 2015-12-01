/*
 * Created on 21/02/2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sies.actionform;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author karenth
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CategoriaForm extends ActionForm{

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Código de la categoría
	 */
	private int codigo;
	
	/**
	 * Nombre de la categoría
	 */
	private String nombre;
	
	/**
	 * Descricpión de la categoría
	 */
	private String descripcion;

	/**
	 * Collection para manejar listados varios
	 */
	private Collection listado;
	
	/**
	 * Estado de la funcionalidad
	 */
	private String estado;
	
	/**
	 * Estado de la categoría (Activo / Inactivo)
	 */
	private boolean activo;

	/**
	 * Log de modificación
	 */
	private String log;
	
	/**
	 * Centro de costo de la categoría
	 */
	private int centroCosto;

	/**
	 * Listado de las enfermeras sin categoría
	 */
	private Collection enfermerasSinCategoria;
	
	/**
	 * Mensaje que informa acerca del resultado de una accion específica
	 */
	private String mensajeAccion;
	
	/**
	 * Color de la categoría
	 */
	private String color;
	
	/**
	 * Validate the properties that have been set from this HTTP request, and
	 * return an <code>ActionErrors</code> object that encapsulates any
	 * validation errors that have been found.  If no errors are found, return
	 * <code>null</code> or an <code>ActionErrors</code> object with no recorded
	 * error messages.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		if (estado.equalsIgnoreCase("empezar"))
		{
			return null;
		}
		
		if (estado.equalsIgnoreCase("guardarNuevo") || estado.equalsIgnoreCase("guardarModificacion") || estado.equalsIgnoreCase("confirmarModificacion"))
		{
			if (this.nombre.equals(""))
			{
				errores.add("Campo Nombre vacio", new ActionMessage("errors.required", "El campo Nombre"));
			}
			/*
			if (this.descripcion.equals(""))
			{
				errores.add("Campo Descripcion vacio", new ActionMessage("errors.required", "El campo Descripción"));
			}
			*/
			if (this.centroCosto<1)
			{
				errores.add("Campo Descripción vacio", new ActionMessage("errors.required", "El campo Centro de Costo"));
			}
			
			if(!errores.isEmpty())
			{
				if(estado.equals("guardarNuevo"))
					estado="ingresar";
				else
					estado="modificar";
			}
		}
		
		return errores;
	}

	/**
	 * Metodo que pone todlos los atributos del jsp
	 */
	public void clean()
	{
		this.codigo=0;
		this.nombre="";
		this.descripcion="";
		this.activo=true;
		this.listado=new ArrayList();
		this.enfermerasSinCategoria=new ArrayList();
		this.mensajeAccion="";
		this.centroCosto=-1;
		this.color="#FFFFFF";
	}

	/**
	 * @return activo
	 */
	public boolean getActivo()
	{
		return activo;
	}

	/**
	 * @param activo Asigna activo
	 */
	public void setActivo(boolean activo)
	{
		this.activo = activo;
	}

	/**
	 * @return centroCosto
	 */
	public int getCentroCosto()
	{
		return centroCosto;
	}

	/**
	 * @param centroCosto Asigna centroCosto
	 */
	public void setCentroCosto(int centroCosto)
	{
		this.centroCosto = centroCosto;
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
	 * @return descripcion
	 */
	public String getDescripcion()
	{
		return descripcion;
	}

	/**
	 * @param descripcion Asigna descripcion
	 */
	public void setDescripcion(String descripcion)
	{
		this.descripcion = descripcion;
	}

	/**
	 * @return enfermerasSinCategoria
	 */
	public Collection getEnfermerasSinCategoria()
	{
		return enfermerasSinCategoria;
	}

	/**
	 * @param enfermerasSinCategoria Asigna enfermerasSinCategoria
	 */
	public void setEnfermerasSinCategoria(Collection enfermerasSinCategoria)
	{
		this.enfermerasSinCategoria = enfermerasSinCategoria;
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
	 * @return nombre
	 */
	public String getNombre()
	{
		return nombre;
	}

	/**
	 * @param nombre Asigna nombre
	 */
	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}

	/**
	 * @return color
	 */
	public String getColor()
	{
		return color;
	}

	/**
	 * @param color Asigna color
	 */
	public void setColor(String color)
	{
		this.color = color;
	}

	/**
	 * @return mensajeAccion
	 */
	public String getMensajeAccion()
	{
		return mensajeAccion;
	}

	/**
	 * @param mensajeAccion Asigna mensajeAccion
	 */
	public void setMensajeAccion(String mensajeAccion)
	{
		this.mensajeAccion = mensajeAccion;
	}
}
