/*
 * @author armando
 */
package com.princetonsa.actionform.pyp;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;

/**
 * 
 * @author armando
 *
 */
public class TiposProgramaForm extends ValidatorForm 
{
	
	/**
	 * Variable para manejar el estado en el que se encuentra la funcionalidad
	 */
	private String estado;
	
	/***
	 * 
	 */
	private HashMap tiposPrograma;
	
	/**
	 * 
	 */
	private HashMap tiposProgamaEliminados;
	
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
     * 
     */
    private String procesoRealizado;

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		if(estado.equals("guardar"))
		{
			for(int k=0;k<Integer.parseInt(this.getTiposPrograma("numRegistros")+"");k++)
		    {
				if(((this.getTiposPrograma("codigo_"+k)+"").trim()).equals(""))  //El código es requerido
			      {
			          errores.add("CODIGO REQUERIDO", new ActionMessage("errors.required","El código para el registro "+(k+1)));  
			      }
				if(((this.getTiposPrograma("descripcion_"+k)+"").trim()).equals(""))  //descripcion es requerido
			      {
			          errores.add("DESCRIPCION REQUERIDO", new ActionMessage("errors.required","La descripción para el registro "+(k+1)));  
			      }
				String codigo=this.getTiposPrograma("codigo_"+k)+"";
				for(int l=0;l<k;l++)
				{
					if(codigo.equals(this.getTiposPrograma("codigo_"+l)+"") && !(this.getTiposPrograma("codigo_"+k)+"").equals(""))  
		             {		                  		                  
		                  errores.add("YA EXISTE EL REGISTRO.", new ActionMessage("errors.yaExiste","El código "+codigo));                 
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
    	this.tiposPrograma=new HashMap();
    	this.tiposPrograma.put("numRegistros","0");
    	this.tiposProgamaEliminados=new HashMap();
    	this.tiposProgamaEliminados.put("numRegistros","0");
    	this.regEliminar=ConstantesBD.codigoNuncaValido;
        this.patronOrdenar = "";
        this.ultimoPatron = "";
        this.procesoRealizado = ConstantesBD.acronimoNo;
    }
    
	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	public HashMap getTiposPrograma() {
		return tiposPrograma;
	}


	public void setTiposPrograma(HashMap tiposProgama) {
		this.tiposPrograma = tiposProgama;
	}

	public Object getTiposPrograma(String key) {
		return tiposPrograma.get(key);
	}


	public void setTiposPrograma(String key,Object value) {
		this.tiposPrograma.put(key,value);
	}


	public HashMap getTiposProgamaEliminados() {
		return tiposProgamaEliminados;
	}


	public void setTiposProgamaEliminados(HashMap tiposProgamaEliminados) {
		this.tiposProgamaEliminados = tiposProgamaEliminados;
	}


	public Object getTiposProgamaEliminados(String key) {
		return tiposProgamaEliminados.get(key);
	}


	public void setTiposProgamaEliminados(String key,Object value) {
		this.tiposProgamaEliminados.put(key,value);
	}

	public String getPatronOrdenar() {
		return patronOrdenar;
	}


	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}


	public int getRegEliminar() {
		return regEliminar;
	}


	public void setRegEliminar(int regEliminar) {
		this.regEliminar = regEliminar;
	}


	public String getUltimoPatron() {
		return ultimoPatron;
	}


	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	/**
	 * @return the procesoRealizado
	 */
	public String getProcesoRealizado() {
		return procesoRealizado;
	}

	/**
	 * @param procesoRealizado the procesoRealizado to set
	 */
	public void setProcesoRealizado(String procesoRealizado) {
		this.procesoRealizado = procesoRealizado;
	}
}
