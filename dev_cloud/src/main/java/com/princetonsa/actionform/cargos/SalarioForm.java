/*
 * @(#)RecargoTarifaForm.java
 * 
 * Created on 05-May-2004
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados
 * 
 * Lenguaje : Java
 * Compilador : J2SDK 1.4
 */
package com.princetonsa.actionform.cargos;

import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.facturacion.UtilidadesFacturacion;

/**
 * ActionForm, tiene la función de bean dentro de la forma, que contiene todos
 * los datos generales de un recargo de una tarifa. Y adicionalmente hace el manejo de reset 
 * de la forma y de validación de errores de datos. 
 * 
 * @version 1.0, 05-May-2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class SalarioForm extends ValidatorForm
{
	
	private String accion;
	private String estado;
	private String codigo;
	private String fechaInicial;
	private String fechaFinal;
	private String salario;
	
	private String log;
	
	 /**
     * Variable para manejar el paginador del listado de salarios mínimos
     */
    private int index;
	private int pager;
    private int offset;
    private String linkSiguiente;
	
	/**
	 * Almacena el resultado de la consulta general de salario
	 */
	private Collection coleccionSalario;
	
	/**
	 * Almacena los datos para la modificacion.
	 */
	private HashMap mapSalario;

	/**
	 * Almacena si se presentaron errores para validar la presentacion del resumen
	 */
	private boolean huboErrores;
	
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
		
		ActionErrors errores = new ActionErrors();
		if(this.estado.equals("guardar") && (this.accion.equals("insertar")||this.accion.equals("modificar")))
		{
			//Validamos el Campo Monto
			if(!UtilidadTexto.isNumber(this.salario))
			{
				errores.add("", new ActionMessage("errors.floatMayorQue", "El campo Monto","cero"));
				this.huboErrores = true;
			}
			else if(Double.parseDouble(this.salario)<=0 || Double.isNaN(Double.parseDouble(this.salario)))
			{
				errores.add("", new ActionMessage("errors.floatMayorQue", "El campo Monto","cero"));
				this.huboErrores = true;
			}
			
			//Validamos la Fecha Inicial
			if(this.fechaInicial.equals(""))
			{
				errores.add("", new ActionMessage("errors.required", "El campo Fecha Inicial"));
				this.huboErrores = true;
			}
			else if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaInicial))
			{
				errores.add("", new ActionMessage("errors.formatoFechaInvalido",fechaInicial));
				this.huboErrores = true;
			}
			
			//Validamos la Fecha Final
			if(this.fechaFinal.equals(""))
			{
				errores.add("", new ActionMessage("errors.required", "El campo Fecha Final"));
				this.huboErrores = true;
			}
			else if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaFinal))
			{
				errores.add("", new ActionMessage("errors.formatoFechaInvalido",fechaFinal));
				this.huboErrores = true;
			}
			
			if(errores.isEmpty())
			{
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.fechaFinal,this.fechaInicial))
				{
					errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual","final->"+fechaFinal,"inicial->"+fechaInicial));
					this.huboErrores = true;
				}
				/**
				 * MT 6854
				 * @author javrammo 
				 * Se agrega validacion para que no se tenga en cuenta el id del registro a modificar.
				 * Para la accion modifica llega el codigo, el resto no llega.
				 */
				Integer codigoRegistroParaNoTenerEnCuenta = null;
				if(NumberUtils.isDigits(this.codigo)){
					codigoRegistroParaNoTenerEnCuenta = Integer.parseInt(this.codigo);
				}				
				if(UtilidadesFacturacion.existeFechasEnSalarioMinimo(this.fechaInicial, this.fechaFinal, codigoRegistroParaNoTenerEnCuenta)){
					
					errores.add("", new ActionMessage("error.facturacion.fechasSalarioYaParametrizadas", this.fechaInicial, this.fechaFinal));
					this.huboErrores = true;
				}
				/**
				 * Fin MT 6854
				 */
			}
			
			if(errores.isEmpty())
				this.huboErrores = false;
			
		}
		
		return errores;
	}	
	
	/**
	 * Método que inicializa todos los atributos de la forma
	 */
	public void reset() 
	{
		
		this.accion="";
		this.codigo="";
		this.fechaFinal="";
		this.fechaInicial="";
		this.salario="0.0";
		this.coleccionSalario = null;
		this.mapSalario = new HashMap ();
		this.log="";
	}	
	
	
	/**
	 * Returns the codigo.
	 * @return String
	 */
	public String getCodigo()
	{
		return codigo;
	}

	/**
	 * Returns the fechaFinal.
	 * @return String
	 */
	public String getFechaFinal()
	{
		return fechaFinal;
	}

	/**
	 * Returns the fechaInicial.
	 * @return String
	 */
	public String getFechaInicial()
	{
		return fechaInicial;
	}

	/**
	 * Sets the codigo.
	 * @param codigo The codigo to set
	 */
	public void setCodigo(String codigo)
	{
		this.codigo = codigo;
	}

	/**
	 * Sets the fechaFinal.
	 * @param fechaFinal The fechaFinal to set
	 */
	public void setFechaFinal(String fechaFinal)
	{
		this.fechaFinal = fechaFinal;
	}

	/**
	 * Sets the fechaInicial.
	 * @param fechaInicial The fechaInicial to set
	 */
	public void setFechaInicial(String fechaInicial)
	{
		this.fechaInicial = fechaInicial;
	}

	/**
	 * Returns the estado.
	 * @return String
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * Sets the estado.
	 * @param estado The estado to set
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	/**
	 * Returns the accion.
	 * @return String
	 */
	public String getAccion()
	{
		return accion;
	}

	/**
	 * Sets the accion.
	 * @param accion The accion to set
	 */
	public void setAccion(String accion)
	{
		this.accion = accion;
	}

    /**
     * @return Retorna coleccionSalario.
     */
    public Collection getColeccionSalario() {
        return coleccionSalario;
    }
    /**
     * @param coleccionSalario Asigna coleccionSalario.
     */
    public void setColeccionSalario(Collection coleccionSalario) {
        this.coleccionSalario = coleccionSalario;
    }
    /**
     * @return Retorna mapSalario.
     */
    public HashMap getMapSalario() {
        return mapSalario;
    }
    /**
     * @param mapSalario Asigna mapSalario.
     */
    public void setMapSalario(HashMap mapSalario) {
        this.mapSalario = mapSalario;
    }
    
    /**
	 * Set del mapa de salario minimo
	 * @param key
	 * @param value
	 */

	public void setMapSalario(String key, Object value) 
	{
	    mapSalario.put(key, value);
	}

	/**
	 * Get del mapa de salario minimo
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getMapSalario(String key) 
	{
		return mapSalario.get(key);
	}
	/**
	 * @return Returns the salario.
	 */
	public String getSalario()
	{
		return salario;
	}
	/**
	 * @param salario The salario to set.
	 */
	public void setSalario(String salario)
	{
		this.salario = salario;
	}
    /**
     * @return Retorna log.
     */
    public String getLog() {
        return log;
    }
    /**
     * @param log Asigna log.
     */
    public void setLog(String log) {
        this.log = log;
    }
	/**
	 * @return Returns the index.
	 */
	public int getIndex()
	{
		return index;
	}
	/**
	 * @param index The index to set.
	 */
	public void setIndex(int index)
	{
		this.index = index;
	}
	/**
	 * @return Returns the linkSiguiente.
	 */
	public String getLinkSiguiente()
	{
		return linkSiguiente;
	}
	/**
	 * @param linkSiguiente The linkSiguiente to set.
	 */
	public void setLinkSiguiente(String linkSiguiente)
	{
		this.linkSiguiente = linkSiguiente;
	}
	/**
	 * @return Returns the offset.
	 */
	public int getOffset()
	{
		return offset;
	}
	/**
	 * @param offset The offset to set.
	 */
	public void setOffset(int offset)
	{
		this.offset = offset;
	}
	/**
	 * @return Returns the pager.
	 */
	public int getPager()
	{
		return pager;
	}
	/**
	 * @param pager The pager to set.
	 */
	public void setPager(int pager)
	{
		this.pager = pager;
	}

	/**
	 * @return the huboErrores
	 */
	public boolean isHuboErrores() {
		return huboErrores;
	}

	/**
	 * @param huboErrores the huboErrores to set
	 */
	public void setHuboErrores(boolean huboErrores) {
		this.huboErrores = huboErrores;
	}
}
