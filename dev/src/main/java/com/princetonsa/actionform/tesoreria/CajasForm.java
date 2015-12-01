/*
 * Created on 12/09/2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.actionform.tesoreria;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;

/**
 * @author artotor
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CajasForm extends ValidatorForm 
{
	//------------------------------------------------------------DEFINICION DE VARAIBLES---------------------------------------------------------------------------//
	/**
	 * Variable para manejar el estado en el que se encuentra la funcionalidad
	 */
	private String estado;
	
	/**
	 * Mapa paara manejar las cajas 
	 */
	private HashMap mapaCajas;

	/**
	 * Mapa paara manejar las Eliminadas
	 */
	private HashMap mapaCajasEliminado;
	/**
	 * Indice del registro que se desea eliminar
	 */
	private int regEliminar;
	
    
    /**
     * almacena el indice por el cual 
     * se va ordenar el HashMap
     */
    private String patronOrdenar;
    
    /**
     * almacena el ultimo indice por el 
     * cual se ordeno el HashMap
     */
    private String ultimoPatron;
    
    /**
     * Centro de atención que se selecciona, cuando empieza
     * la funcionalidad por defecto se selecciona el centro de atención del usuario  
     */
    private int centroAtencion;
    
    /**
     * Variable para manejar el mensaje de Operación realizada con exito
     */
    private ResultadoBoolean mensaje = new ResultadoBoolean(false);
    
	//----------------------------------------------------------FIN DEFINICION DE VARAIBLES-------------------------------------------------------------------------//
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		if(estado.equals("guardar"))
		{
			for(int k=0;k<Integer.parseInt(this.getMapaCajas("numeroregistros")+"");k++)
		    {
				if(((this.getMapaCajas("codigo_"+k)+"").trim()).equals(""))  //El código es requerido
			      {
			          errores.add("CODIGO REQUERIDO", new ActionMessage("errors.required","El Código para el registro "+(k+1)));  
			      }
				else
				{
					String codigo=this.getMapaCajas("codigo_"+k)+"";
					try
					{
						//verificar si es un numero valido.
						Integer.parseInt(codigo);
						
						//veririficar si ya existe
						for(int l=0;l<k;l++)
						{
							if(codigo.equals(this.getMapaCajas("codigo_"+l)+"") && !(this.getMapaCajas("codigo_"+k)+"").equals(""))  
				             {		                  		                  
				                  errores.add("YA EXISTE EL REGISTRO.", new ActionMessage("errors.yaExiste","El Código "+codigo));                 
				             }
						}
					}
					catch(NumberFormatException e)
					{
		                  errores.add("caract invalidos", new ActionMessage("error.errorEnBlanco","El Código del registro "+(k+1)+" debe ser un número entero positivo."));                 
					}
				}
				if(((this.getMapaCajas("descripcion_"+k)+"").trim()).equals(""))  //descripcion es requerido
			      {
			          errores.add("DESCRIPCION REQUERIDO", new ActionMessage("errors.required","La Descripción para el registro "+(k+1)));  
			      }
				if(((this.getMapaCajas("tipo_"+k)+"").trim()).equals("-1"))  //El código es requerido
			      {
			          errores.add("DESCRIPCION REQUERIDO", new ActionMessage("errors.required","El Tipo para el registro "+(k+1)));  
			      }
				
		    }
		}	
		return errores;
	}
	
	/**
     * inicializar atributos de esta forma
     *
     */
    public void reset ()
    {
    	this.centroAtencion = ConstantesBD.codigoNuncaValido;
    	this.mapaCajas=new HashMap();
    	this.mapaCajasEliminado=new HashMap();
    	this.regEliminar=ConstantesBD.codigoNuncaValido;
        this.patronOrdenar = "";
        this.ultimoPatron = "";
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
	 * @return Returns the mapaCajas.
	 */
	public HashMap getMapaCajas() {
		return mapaCajas;
	}
	/**
	 * @param mapaCajas The mapaCajas to set.
	 */
	public void setMapaCajas(HashMap mapaCajas) {
		this.mapaCajas = mapaCajas;
	}
	/**
	 * @return Returns the mapaCajas.
	 */
	public Object getMapaCajas(String key) {
		return mapaCajas.get(key);
	}
	/**
	 * @param mapaCajas The mapaCajas to set.
	 */
	public void setMapaCajas(String key,Object value) {
		this.mapaCajas.put(key,value);
	}	
	/**
	 * @return Returns the regEliminar.
	 */
	public int getRegEliminar() {
		return regEliminar;
	}
	/**
	 * @param regEliminar The regEliminar to set.
	 */
	public void setRegEliminar(int regEliminar) {
		this.regEliminar = regEliminar;
	}
		
	/**
	 * @return Returns the mapaCajasEliminado.
	 */
	public HashMap getMapaCajasEliminado() {
		return mapaCajasEliminado;
	}
	/**
	 * @param mapaCajasEliminado The mapaCajasEliminado to set.
	 */
	public void setMapaCajasEliminado(HashMap mapaCajasEliminado) {
		this.mapaCajasEliminado = mapaCajasEliminado;
	}
	/**
	 * @return Returns the mapaCajas.
	 */
	public Object getMapaCajasEliminado(String key) {
		return mapaCajasEliminado.get(key);
	}
	/**
	 * @param mapaCajas The mapaCajas to set.
	 */
	public void setMapaCajasEliminado(String key,Object value) {
		this.mapaCajasEliminado.put(key,value);
	}
	/**
	 * @return Returns the patronOrdenar.
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}
	/**
	 * @param patronOrdenar The patronOrdenar to set.
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}
	/**
	 * @return Returns the ultimoPatron.
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}
	/**
	 * @param ultimoPatron The ultimoPatron to set.
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	/**
	 * @return Retorna the centroAtencion.
	 */
	public int getCentroAtencion()
	{
		return centroAtencion;
	}

	/**
	 * @param centroAtencion The centroAtencion to set.
	 */
	public void setCentroAtencion(int centroAtencion)
	{
		this.centroAtencion = centroAtencion;
	}

	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}
}
