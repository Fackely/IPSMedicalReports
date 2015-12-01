/*
 * Created on 20/09/2005
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

import util.ResultadoBoolean;

/**
 * @author artotor
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EntidadesFinancierasForm extends ValidatorForm 
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
	private HashMap mapaEntidadesFinancieras;
	
	/**
	 * Mapa que contiene los registros a eliminar. 
	 */
	private HashMap mapaEntidadesFinancierasEliminado;
	
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
				if(((this.getMapaEntidadesFinancieras("codigo_"+k)+"").trim()).equals(""))  //El código es requerido
			      {
			          errores.add("CODIGO REQUERIDO", new ActionMessage("errors.required","El Código para el registro "+(k+1)));  
			      }
				if(((this.getMapaEntidadesFinancieras("codigotercero_"+k)+"").trim()).equals("")||((this.getMapaEntidadesFinancieras("codigotercero_"+k)+"").trim()).equals("-1"))  //descripcion es requerido
			      {
			          errores.add("Tercero Requerido", new ActionMessage("errors.required","El Tercero para el registro "+(k+1)));  
			      }
				if(((this.getMapaEntidadesFinancieras("codigotipo_"+k)+"").trim()).equals("")||((this.getMapaEntidadesFinancieras("codigotipo_"+k)+"").trim()).equals("-1"))  //El código es requerido
			      {
			          errores.add("Tipo requerido", new ActionMessage("errors.required","El Tipo para el registro "+(k+1)));  
			      }
				String codigo=this.getMapaEntidadesFinancieras("codigo_"+k)+"";
				String tercero=this.getMapaEntidadesFinancieras("codigotercero_"+k)+"";
				String tipo=this.getMapaEntidadesFinancieras("codigotipo_"+k)+"";
				for(int l=0;l<k;l++)
				{
					if(codigo.equals(this.getMapaEntidadesFinancieras("codigo_"+l)+"") && !(this.getMapaEntidadesFinancieras("codigo_"+k)+"").equals(""))  
		             {		                  		                  
		                  errores.add("YA EXISTE EL REGISTRO.", new ActionMessage("errors.yaExiste","El Código "+codigo));                 
		             }
					if((tercero.equals(this.getMapaEntidadesFinancieras("codigotercero_"+l)+"") && !(this.getMapaEntidadesFinancieras("codigotercero_"+k)+"").equals(""))&&
						(tipo.equals(this.getMapaEntidadesFinancieras("codigotipo_"+l)+"") && !(this.getMapaEntidadesFinancieras("codigotipo_"+k)+"").equals(""))
					)  
		             {		                  		                  
		                  errores.add("YA EXISTE EL REGISTRO.", new ActionMessage("errors.yaExiste","La relación Tercero-Tipo del registro "+(k+1)));                 
		             }
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
    	this.numeroRegistro=0;
		this.mapaEntidadesFinancieras=new HashMap();
		this.mapaEntidadesFinancierasEliminado=new HashMap();
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
	 * @return Returns the mapaEntidaadesFinanceras.
	 */
	public HashMap getMapaEntidadesFinancieras() 
	{
		return mapaEntidadesFinancieras;
	}
	/**
	 * @param mapaEntidaadesFinanceras The mapaEntidaadesFinanceras to set.
	 */
	public void setMapaEntidadesFinancieras(HashMap mapaEntidaadesFinanceras) 
	{
		this.mapaEntidadesFinancieras = mapaEntidaadesFinanceras;
	}
	/**
	 * @return Returns the mapaEntidaadesFinanceras.
	 */
	public Object getMapaEntidadesFinancieras(String key) 
	{
		return mapaEntidadesFinancieras.get(key);
	}
	/**
	 * @param mapaEntidaadesFinanceras The mapaEntidaadesFinanceras to set.
	 */
	public void setMapaEntidadesFinancieras(String key,Object value) 
	{
		this.mapaEntidadesFinancieras.put(key,value);
	}
	/**
	 * @return Returns the mapaEntidaadesFinancerasEliminado.
	 */
	public HashMap getMapaEntidadesFinancierasEliminado() {
		return mapaEntidadesFinancierasEliminado;
	}
	/**
	 * @param mapaEntidaadesFinancerasEliminado The mapaEntidaadesFinancerasEliminado to set.
	 */
	public void setMapaEntidadesFinancierasEliminado(HashMap mapaEntidaadesFinancerasEliminado) 
	{
		this.mapaEntidadesFinancierasEliminado = mapaEntidaadesFinancerasEliminado;
	}
	/**
	 * @return Returns the mapaEntidaadesFinancerasEliminado.
	 */
	public Object getMapaEntidadesFinancierasEliminado(String key) 
	{
		return mapaEntidadesFinancierasEliminado.get(key);
	}
	/**
	 * @param mapaEntidaadesFinancerasEliminado The mapaEntidaadesFinancerasEliminado to set.
	 */
	public void setMapaEntidadesFinancierasEliminado(String key,Object value) 
	{
		this.mapaEntidadesFinancierasEliminado.put(key,value);
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

	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}
}
