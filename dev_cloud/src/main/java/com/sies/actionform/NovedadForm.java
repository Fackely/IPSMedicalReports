/*
 * Created on 13/04/2005
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
public class NovedadForm extends ActionForm{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Codigo de la Novedad
	 */
	private int codigo;
	
	/**
	 * Nombre de la Novedad
	 */
	 private String nombre;
	 
	 /**
	  * Descripción de la Novedad
	  */
	private String descripcion;
	
	/**
	 * Indica si la novedad cuenta para novedad
	 */
	private boolean nomina;
	
	/**
	 * Estado de la Novedad
	 */
	private boolean activo;
	
	
	private Collection listado;
	
	private String estado;
	
	private String log;
	
	//private String temporizador;
    
    private String mensajeAccion;
	 
	
	
    /**
     * @return Returns the temporizador.
     */
    /*public String getTemporizador() {
        return temporizador;
    }*/
    /**
     * @param temporizador The temporizador to set.
     */
    /*public void setTemporizador(String temporizador) {
        this.temporizador = temporizador;
    }*/
	
	public void clean()
	{
		codigo=0;
		nombre="";
		descripcion="";
		activo=false;
		nomina=false;
		listado=new ArrayList();
		//temporizador=null;
        this.mensajeAccion="";
	}
	
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
		
		//System.out.println("estado forma="+estado);
		
		if (estado.equals("empezar"))
		{
			return null;
		}
		
		if (estado.equals("guardarNuevo") || estado.equals("guardarModificacion"))
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
			if(!errores.isEmpty())
			{
				if(estado.equals("guardarNuevo"))
				{
					estado="ingresar";
				}
				else
				{
					estado="modificar";
				}
			}
		}
		return errores;
	}

	/**
	 * @return Retorna activo
	 */
	public boolean getActivo()
	{
		return activo;
	}

	/**
	 * @param Asigna activo
	 */
	public void setActivo(boolean activo)
	{
		this.activo = activo;
	}

	/**
	 * @return Retorna codigo
	 */
	public int getCodigo()
	{
		return codigo;
	}

	/**
	 * @param Asigna codigo
	 */
	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
	}

	/**
	 * @return Retorna descripcion
	 */
	public String getDescripcion()
	{
		return descripcion;
	}

	/**
	 * @param Asigna descripcion
	 */
	public void setDescripcion(String descripcion)
	{
		this.descripcion = descripcion;
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
	 * @return Retorna listado
	 */
	public Collection getListado()
	{
		return listado;
	}

	/**
	 * @param Asigna listado
	 */
	public void setListado(Collection listado)
	{
		this.listado = listado;
	}

	/**
	 * @return Retorna log
	 */
	public String getLog()
	{
		return log;
	}

	/**
	 * @param Asigna log
	 */
	public void setLog(String log)
	{
		this.log = log;
	}

	/**
	 * @return Retorna mensajeAccion
	 */
	public String getMensajeAccion()
	{
		return mensajeAccion;
	}

	/**
	 * @param Asigna mensajeAccion
	 */
	public void setMensajeAccion(String mensajeAccion)
	{
		this.mensajeAccion = mensajeAccion;
	}

	/**
	 * @return Retorna nombre
	 */
	public String getNombre()
	{
		return nombre;
	}

	/**
	 * @param Asigna nombre
	 */
	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}

	/**
	 * @return Retorna nomina
	 */
	public boolean getNomina()
	{
		return nomina;
	}

	/**
	 * @param Asigna nomina
	 */
	public void setNomina(boolean nomina)
	{
		this.nomina = nomina;
	}
}
