/*
 * Created on 19/09/2005
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.actionform.tesoreria;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ResultadoBoolean;

/**
 * @author artotor
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FormasPagoForm extends ValidatorForm 
{
	/**
	 * Variable para manejar el estado de la funcionalidad
	 */
	private String estado;
	
	/**
	 * Variable que indica el numero de registros que se tienen(Cargados en el mapa)
	 */
	private int numeroRegistro;
	
	/**
	 * Mapa que contiene los registros
	 */
	private HashMap mapaFormasPago;
	
	/**
	 * Mapa que contiene los registros a eliminar. 
	 */
	private HashMap mapaFormasPagoEliminado;
	
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
     * Variable para manejar el mensaje de Operación realizada con exito
     */
    private ResultadoBoolean mensaje = new ResultadoBoolean(false);
    
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		if(estado.equals("guardar"))
		{
			
			for(int k=0;k<this.numeroRegistro;k++)
		    {
				/*
				if(((this.getMapaFormasPago("codigo_"+k)+"").trim()).equals(""))  //El código es requerido
			      {
			          errores.add("CODIGO REQUERIDO", new ActionMessage("errors.required","El Código para el registro "+(k+1)));  
			      }
			      */
				if(((this.getMapaFormasPago("descripcion_"+k)+"").trim()).equals(""))  //descripcion es requerido
			      {
			          errores.add("DESCRIPCION REQUERIDO", new ActionMessage("errors.required","La Descripción para el registro "+(k+1)));  
			      }
				else if((this.getMapaFormasPago("descripcion_"+k)+"").length()>256)
				{
			          errores.add("", new ActionMessage("errors.maxlength","La Descripción para el registro "+(k+1),"256"));  
				}
				if(((this.getMapaFormasPago("tipodetalle_"+k)+"").trim()).equals("-1")||((this.getMapaFormasPago("tipodetalle_"+k)+"").trim()).equals(""))  //El código es requerido
			      {
			          errores.add("tipo REQUERIDO", new ActionMessage("errors.required","El Tipo para el registro "+(k+1)));  
			      }
				String codigo=this.getMapaFormasPago("codigo_"+k)+"";
				for(int l=0;l<k;l++)
				{
					if(codigo.equals(this.getMapaFormasPago("codigo_"+l)+"") && !(this.getMapaFormasPago("codigo_"+k)+"").equals(""))  
		             {		                  		                  
		                  errores.add("YA EXISTE EL REGISTRO.", new ActionMessage("errors.yaExiste","El Código "+codigo));                 
		             }
				}
		    }
		}	
		return errores;
	}
    
	/**
	 * 
	 */
	public void reset() 
	{
		this.numeroRegistro=0;
		this.mapaFormasPago=new HashMap();
		this.mapaFormasPagoEliminado=new HashMap();
		this.patronOrdenar="";
		this.ultimoPatron="";
		this.regEliminar=0;
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
	 * @return Returns the mapaFormasPago.
	 */
	public HashMap getMapaFormasPago() {
		return mapaFormasPago;
	}
	/**
	 * @param mapaFormasPago The mapaFormasPago to set.
	 */
	public void setMapaFormasPago(HashMap mapaFormasPago) {
		this.mapaFormasPago = mapaFormasPago;
	}
	
	/**
	 * @return Returns the mapaFormasPago.
	 */
	public Object getMapaFormasPago(String key) {
		return mapaFormasPago.get(key);
	}
	/**
	 * @param mapaFormasPago The mapaFormasPago to set.
	 */
	public void setMapaFormasPago(String key,Object value) {
		this.mapaFormasPago.put(key,value);
	}
	
	/**
	 * @return Returns the mapaFormasPagoEliminado.
	 */
	public HashMap getMapaFormasPagoEliminado() {
		return mapaFormasPagoEliminado;
	}
	/**
	 * @param mapaFormasPagoEliminado The mapaFormasPagoEliminado to set.
	 */
	public void setMapaFormasPagoEliminado(HashMap mapaFormasPagoEliminado) {
		this.mapaFormasPagoEliminado = mapaFormasPagoEliminado;
	}
	
	/**
	 * @return Returns the mapaFormasPagoEliminado.
	 */
	public Object getMapaFormasPagoEliminado(String key) {
		return mapaFormasPagoEliminado.get(key);
	}
	/**
	 * @param mapaFormasPagoEliminado The mapaFormasPagoEliminado to set.
	 */
	public void setMapaFormasPagoEliminado(String key,Object value) {
		this.mapaFormasPagoEliminado.put(key,value);
	}
	/**
	 * @return Returns the numeroRegistro.
	 */
	public int getNumeroRegistro() {
		return numeroRegistro;
	}
	/**
	 * @param numeroRegistro The numeroRegistro to set.
	 */
	public void setNumeroRegistro(int numeroRegistro) {
		this.numeroRegistro = numeroRegistro;
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

	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}
}
