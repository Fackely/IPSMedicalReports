/*
 * Created on 18/05/2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sies.actionform;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author karenth
 *
 */
public class AsignarRestriccionesCategoriaForm extends ActionForm
{
    /**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;


	/**
     * Codigo asignado por el sistema a la asociación
     */ 
    private int codigoAsociacion;
    
    
    /**
     * Hash Map donde se hacen todas las modificaciones antes de guardar modificaciones
     */
    private HashMap asociacion;
    
    /**
     * Hash map donde se guardan lo que hay en las base de datos antes de comenzar,
     *  para los logs de las modificaciones
     */
    private HashMap asociacionBD;
    
    
    /**
     * Codigo de la categoria
     */
    private int codigoCategoria;
    
    /**
     * fecha de inicio de la asociación de la restriccion a la categoria
     */
    private String fechaInicio;
    
    /**
     * fecha de finalización de la asociación de la restriccion a la categoria
     */
    private String fechaFin;
    
    /**
     * Lo que se va a meter en el textarea
     */
    private String asignaciones;
        
    
    /**
     * Para guardar el resultado de la consulta 
     */
    private Collection listado;
    
    /**
     * Para guardar el resultado de la consulta 
     */
    private Collection listadoResCat;
    
    /**
     * Para guardar el resultado de la consulta de las restricciones
     * que se encuentran en el sistema
     */
    private Collection listadoRestriccion;
    
    /**
     * Listado de las categorías en orden alfabético
     */
    private Collection<HashMap<String, Object>> listadoCategorias;
    
    /**
     * Para manejar los logs de modificación
     */
    private String log;
    
    /**
     * Para el manejo de estado en el jsp
     */
    private String estado;
    
    /**
     * Mensaje de resultado al guardar restricciones
     */
    private String mensajeResultado;
    
   /**
    * Para manejar la salida inesperada del sistema
    */    
    //private String temporizador;
    
    
    
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

 		if(estado.equals("guardar"))
 		{
			Collection listadoRestriccion=this.getListadoRestriccion();
			Iterator iterador=listadoRestriccion.iterator();
			int numRestr=listadoRestriccion.size();
			for(int y=0;y<numRestr;y++)
			{
				HashMap<String, Object> restriccionBD=(HashMap<String, Object>)iterador.next();
				int codigo=(Integer)restriccionBD.get("codigo");
				boolean esTexto=(Boolean)restriccionBD.get("texto");
				// Este temporal se utiliza para preguntar si este campo se va a trabajar en la base de datos
				String tempo=(String)getAsociacionNueva("valor_"+codigoCategoria+"_"+codigo);
				//System.out.println("tempo "+tempo);
				if(esTexto)
				{
					if(tempo.trim().equalsIgnoreCase(""))
					{
						errores.add("requrido_"+codigo, new ActionMessage("errors.required", (String)getAsociacionNueva("descripcion_"+codigoCategoria+"_"+codigo)));
					}
					else
					{
						try
						{
							Integer.parseInt(tempo);
						}
						catch (NumberFormatException e)
						{
							errores.add("no_entero_"+codigo, new ActionMessage("errors.integer", (String)getAsociacionNueva("descripcion_"+codigoCategoria+"_"+codigo)));
						}
					}
				}
			}
 		}
 		
 		return errores;
 	}
    
     
   
    
     
  public void clean()
  {
      codigoAsociacion=0;
      codigoCategoria=-1;
      fechaInicio="";
      fechaFin="";
      asignaciones="";
      asociacion=new HashMap();
      asociacionBD=new HashMap();
      listado=new ArrayList();
      log="";
      estado="empezar";
      //temporizador=null;
      listadoRestriccion=new ArrayList();
      this.mensajeResultado="";
  }
    
    
 /**
  * @return Returns the codigo_categoria.
  */
 public int getCodigoCategoria() {
     return codigoCategoria;
 }
 /**
  * @param codigo_categoria The codigo_categoria to set.
  */
 public void setCodigoCategoria(int codigoCategoria) {
     this.codigoCategoria = codigoCategoria;
 }
 
 public String getMensajeResultado() {
     return this.mensajeResultado;
 }
 public void setMensajeResultado(String mensajeResultado) {
     this.mensajeResultado=mensajeResultado;
 }

 /**
  * @return Returns the fecha_fin.
  */
 public String getFechaFin() {
     return fechaFin;
 }
 /**
  * @param fecha_fin The fecha_fin to set.
  */
 public void setFechaFin(String fechaFin) {
     this.fechaFin = fechaFin;
 }
 /**
  * @param fecha_inicio The fecha_inicio to set.
  */
 public void setFechaInicio(String fechaInicio) {
     this.fechaInicio = fechaInicio;
 }
 /**
  * @return Returns the estado.
  */
 public String getEstado() {
     return estado;
 }
 /**
  * @param estado The estado to set.
  */
 public void setEstado(String estado) {
     this.estado = estado;
 }
 /**
  * @return Returns the asignaciones.
  */
 public String getAsignaciones() {
     return asignaciones;
 }
 /**
  * @param asignaciones The asignaciones to set.
  */
 public void setAsignaciones(String asignaciones) {
     this.asignaciones = asignaciones;
 }
 /**
  * @return Returns the listado.
  */
 public Collection getListado() {
     return listado;
 }
 /**
  * @param listado The listado to set.
  */
 public void setListado(Collection listado) {
     this.listado = listado;
 }
 
 
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
 /**
  * @return Returns the log.
  */
 public String getLog() {
     return log;
 }
 /**
  * @param log The log to set.
  */
 public void setLog(String log) {
     this.log = log;
 }
 
    
    
    

	/**
	 * @return Returns the asociacion.
	 */
	public Object getAsociacionNueva(String key) {
	    return asociacion.get(key);
	}
	/**
	 * @param asociacion The asociacion to set.
	 */
	public void setAsociacionNueva(String key, Object value) {
	    this.asociacion.put(key, value);
	}

	
	
    
    /**
     * @return Returns the asociacion.
     */
    public HashMap getAsociacion() {
        return asociacion;
    }
    /**
     * @param asociacion The asociacion to set.
     */
    public void setAsociacion(HashMap asociacion) {
        this.asociacion = asociacion;
    }
    /**
     * @return Returns the asociacionBD.
     */
    public HashMap getAsociacionBD() {
        return asociacionBD;
    }
    /**
     * @param asociacionBD The asociacionBD to set.
     */
    public void setAsociacionBD(HashMap asociacionBD) {
        this.asociacionBD = asociacionBD;
    }
    /**
     * @return Returns the codigoAsociacion.
     */
    public int getCodigoAsociacion() {
        return codigoAsociacion;
    }
    /**
     * @param codigoAsociacion The codigoAsociacion to set.
     */
    public void setCodigoAsociacion(int codigoAsociacion) {
        this.codigoAsociacion = codigoAsociacion;
    }
    /**
     * @return Returns the listadoRestriccion.
     */
    public Collection getListadoRestriccion() {
        return listadoRestriccion;
    }
    /**
     * @param listadoRestriccion The listadoRestriccion to set.
     */
    public void setListadoRestriccion(Collection listadoRestriccion) {
        this.listadoRestriccion = listadoRestriccion;
    }
    /**
     * @return Returns the fechaInicio.
     */
    public String getFechaInicio() {
        return fechaInicio;
    }
    /**
     * @return Returns the listadoResCat.
     */
    public Collection getListadoResCat() {
        return listadoResCat;
    }
    /**
     * @param listadoResCat The listadoResCat to set.
     */
    public void setListadoResCat(Collection listadoResCat) {
        this.listadoResCat = listadoResCat;
    }

	/**
	 * @return Obtiene listadoCategorias
	 */
	public Collection<HashMap<String, Object>> getListadoCategorias()
	{
		return listadoCategorias;
	}

	/**
	 * @param listadoCategorias Asigna listadoCategorias
	 */
	public void setListadoCategorias(Collection<HashMap<String, Object>> listadoCategorias)
	{
		this.listadoCategorias = listadoCategorias;
	}
}
