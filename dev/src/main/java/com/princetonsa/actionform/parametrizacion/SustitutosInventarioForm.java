/*
 * Created on 31-ago-2004
 *
 * Jorge Armando Osorio Velasquez
 * Princeton
 */
package com.princetonsa.actionform.parametrizacion;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;


import util.ConstantesBD;



/**
 * @author armando
 * Clase para manejar los atributos necesarios en vista
 * para la funcionalidad de control específicada en
 * SustitutosInventarioAction
 * 
 * Princeton 31-ago-2004
 */
public class SustitutosInventarioForm extends ValidatorForm{
	

	/**
	 * Código del artículo principal
	 */
	private int codPrincipalBusqueda;
	
	/**
	 * Código del artículo principal
	 */
	private int codPrincipal;
	/**
	 * Código del artículo sustituto
	 */
	private int codSustituto;
	/**
	 * Estado de la aplicacion
	 */
	private String estado;
	/**
	 * Collecion para tomar los datos de la consulta avanzada
	 */
	private Collection coleccion;
	/**
	 * Centienela que indica si se esta modificando  o consultando.
	 */
	private boolean enModificar;
	/**
	 * Codigo del sustituto que se va a modificar
	 */
	private int codSustitutoOld;
	/**
	 * Variable para manejar los logs.
	 */
	private String logHistorial;
	
	/**
	 * Valrible que almacena la ultima propiedad por la que se ordeno.
	 */
	private String ultimaPropiedad;
	/**
	 * Columna por la que se desea ordenar
	 */
	private String columna;
	/**
	 * Reset NO estandar, para limpiar al terminar todo el proceso, NO al
	 * cambiar de página. Limpia TODOS los datos menos el estado y el
	 * mensaje de resumen
	 */
	
	public void reset ()
	{
	    accionAFinalizar="";
		codPrincipal=-1;
		codPrincipalBusqueda=-1;
		codSustituto=-1;
		codSustitutoOld=-1;
		coleccion=new ArrayList();
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
	  public ActionErrors validate (ActionMapping mapping, HttpServletRequest request)
	    {
	    ActionErrors errors= new ActionErrors();
		if (estado!=null&&accionAFinalizar!=null&&estado.equals("salir")&&(accionAFinalizar.equals("ingresar")||accionAFinalizar.equals("modificar")))
		{

		    if (this.codPrincipal==ConstantesBD.codigoNuncaValido||this.codPrincipal<=0)
		    {
		        errors.add("errors.seleccion1", new ActionMessage("errors.invalid", "Código de articulo principal"));
		    }
		    if (this.codSustituto==ConstantesBD.codigoNuncaValido||this.codSustituto<=0)
		    {
		        errors.add("errors.seleccion1", new ActionMessage("errors.invalid", "Código de articulo Sustituto"));
		    }
		    if(this.codPrincipal==this.codSustituto&&(!(this.codPrincipal==ConstantesBD.codigoNuncaValido||this.codPrincipal<=0)&&!(this.codSustituto==ConstantesBD.codigoNuncaValido||this.codSustituto<=0)))
		    {
		        errors.add("errors.iguales", new ActionMessage("errors.invalid","El Artículo no puede se Sustituto de si mismo"));
		    }
		    
		}
		else if(estado!=null&&accionAFinalizar!=null&&estado.equals("salir")&&accionAFinalizar.equals("consultar"))
		{
		    if (this.codPrincipalBusqueda==ConstantesBD.codigoNuncaValido||this.codPrincipalBusqueda<=0)
		    {
		        errors.add("errors.seleccion1", new ActionMessage("errors.invalid", "Código de articulo principal"));
		        codPrincipalBusqueda=-1;
		    }
		    
		}
		return errors;
	}
	
	
    /**
     * @return Retorna el codPrincipal.
     */
	
    public int getCodPrincipal() {
        return codPrincipal;
    }
    /**
     * @param codPrincipal Asigna el codPrincipal.
     */
    public void setCodPrincipal(int codPrincipal) {
        this.codPrincipal = codPrincipal;
    }
    /**
     * @return Retorna el codSustituto.
     */
    public int getCodSustituto() {
        return codSustituto;
    }
    /**
     * @param codSustituto Asigna el codSustituto.
     */
    public void setCodSustituto(int codSustituto) {
        this.codSustituto = codSustituto;
    }

    /**
     * @return Retorna el estado.
     */
    public String getEstado() {
        return estado;
    }
    public String accionAFinalizar;
    /**
     * @param estado Asigna el estado.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @return Retorna el accionAFinalizar.
     */
    public String getAccionAFinalizar() {
        return accionAFinalizar;
    }
    /**
     * @param accionAFinalizar Asigna el accionAFinalizar.
     */
    public void setAccionAFinalizar(String accionAFinalizar) {
        this.accionAFinalizar = accionAFinalizar;
    }
    /**
     * @return Retorna el coleccion.
     */
    public Collection getColeccion() {
        return coleccion;
    }
    /**
     * @param coleccion Asigna el coleccion.
     */
    public void setColeccion(Collection coleccion) {
        this.coleccion = coleccion;
    }
    /**
     * @return Retorna el enModificar.
     */
    public boolean getEnModificar() {
        return enModificar;
    }
    /**
     * @param enModificar Asigna el enModificar.
     */
    public void setEnModificar(boolean enModificar) {
        this.enModificar = enModificar;
    }
    /**
     * @return Retorna el codSustitutoOld.
     */
    public int getCodSustitutoOld() {
        return codSustitutoOld;
    }
    /**
     * @param codSustitutoOld Asigna el codSustitutoOld.
     */
    public void setCodSustitutoOld(int codSustitutoOld) {
        this.codSustitutoOld = codSustitutoOld;
    }
    /**
     * @return Retorna el logHistorial.
     */
    public String getLogHistorial() {
        return logHistorial;
    }
    /**
     * @param logHistorial Asigna el logHistorial.
     */
    public void setLogHistorial(String logHistorial) {
        this.logHistorial = logHistorial;
    }
    /**
     * @return Retorna el columna.
     */
    public String getColumna() {
        return columna;
    }
    /**
     * @param columna Asigna el columna.
     */
    public void setColumna(String columna) {
        this.columna = columna;
    }
    /**
     * @return Retorna el ultimaPropiedad.
     */
    public String getUltimaPropiedad() {
        return ultimaPropiedad;
    }
    /**
     * @param ultimaPropiedad Asigna el ultimaPropiedad.
     */
    public void setUltimaPropiedad(String ultimaPropiedad) {
        this.ultimaPropiedad = ultimaPropiedad;
    }
	/**
	 * @return Retorna el codPrincipalBusqueda.
	 */
	public int getCodPrincipalBusqueda() {
		return codPrincipalBusqueda;
	}
	/**
	 * @param codPrincipalBusqueda Asigna el codPrincipalBusqueda.
	 */
	public void setCodPrincipalBusqueda(int codPrincipalBusqueda) {
		this.codPrincipalBusqueda = codPrincipalBusqueda;
	}
}
