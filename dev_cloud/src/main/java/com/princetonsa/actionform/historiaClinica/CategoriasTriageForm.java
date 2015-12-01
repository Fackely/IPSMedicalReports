/*
 * @author armando
 */
package com.princetonsa.actionform.historiaClinica;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;

/**
 * 
 * @author artotor
 *
 */
public class CategoriasTriageForm extends ValidatorForm 
{
	/**
	 * Variable para manejar el estado en el que se encuentra la funcionalidad
	 */
	private String estado;
	
	/**
	 * mapa para manejar las categorias.
	 */
	private HashMap categorias;
	
	/**
	 * mapa para manejar las categorias eliminadas.
	 */
	private HashMap categoriasEliminadas;
	
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
    private int indexDetalle;
    
    /**
     * Mapa para almacenr los destinos de las categorias.
     */
    private HashMap destinosCategoria;
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		if(estado.equals("volver"))
		{
			HashMap temp=new HashMap();
			temp=(HashMap)this.getCategorias("destino_"+this.indexDetalle);
			for(int j=0;j<Integer.parseInt(temp.get("numRegistros")+"");j++)
			{
				if((temp.get("destino_"+j)+"").trim().equals("-1")) 
				{
					 errores.add("DESTINO REQUERIDO", new ActionMessage("errors.required","El Destino para el registro "+(j+1)));  
				}
				String destino=temp.get("destino_"+j)+"";
				for(int j1=0;j1<j;j1++)
				{
					if(destino.equals(temp.get("destino_"+j1)+""))  
		             {		                  		                  
		                  errores.add("YA EXISTE EL REGISTRO.", new ActionMessage("errors.yaExiste","El registro ("+(j+1)+") Destino: "+temp.get("destino_"+j1)));                 
		             }
				}
			}
		}
		if(estado.equals("guardar"))
		{
			for(int k=0;k<Integer.parseInt(this.getCategorias("numRegistros")+"");k++)
		    {
				if(((this.getCategorias("codigo_"+k)+"").trim()).equals(""))  //El código es requerido
			      {
			          errores.add("CODIGO REQUERIDO", new ActionMessage("errors.required","El código para el registro "+(k+1)));  
			      }
				if(((this.getCategorias("descripcion_"+k)+"").trim()).equals(""))  //descripcion es requerido
			      {
			          errores.add("DESCRIPCION REQUERIDO", new ActionMessage("errors.required","La descripción para el registro "+(k+1)));  
			      }
				if(((this.getCategorias("color_"+k)+"").trim()).equals("-1")) 
			      {
			          errores.add("COLOR REQUERIDO", new ActionMessage("errors.required","El color para el registro "+(k+1)));  
			      }
				HashMap temp=new HashMap();
				temp=(HashMap)this.getCategorias("destino_"+k);
				if(Integer.parseInt(temp.get("numRegistros").toString())<=0)
				{
			          errores.add("DESTINO REQUERIDO", new ActionMessage("errors.required","El destino para el registro "+(k+1)));  
				}
				String codigo=this.getCategorias("codigo_"+k)+"";
				for(int l=0;l<k;l++)
				{
					if(codigo.equals(this.getCategorias("codigo_"+l)+"") && !(this.getCategorias("codigo_"+k)+"").equals(""))  
		             {		                  		                  
		                  errores.add("YA EXISTE EL REGISTRO.", new ActionMessage("errors.yaExiste","El registro ("+(k+1)+") Código: "+this.getCategorias("codigo_"+l)));                 
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
    	this.categorias=new HashMap();
    	this.categoriasEliminadas=new HashMap();
    	this.regEliminar=ConstantesBD.codigoNuncaValido;
        this.patronOrdenar = "";
        this.ultimoPatron = "";
        this.indexDetalle=ConstantesBD.codigoNuncaValido;
        this.destinosCategoria=new HashMap();
    }

	public HashMap getCategorias() {
		return categorias;
	}

	public void setCategorias(HashMap categorias) {
		this.categorias = categorias;
	}


	public Object getCategorias(String key) {
		return categorias.get(key);
	}

	public void setCategorias(String key,Object value) {
		this.categorias.put(key,value);
	}
	
	public HashMap getCategoriasEliminadas() {
		return categoriasEliminadas;
	}

	public void setCategoriasEliminadas(HashMap categoriasEliminadas) {
		this.categoriasEliminadas = categoriasEliminadas;
	}
	
	public Object getCategoriasEliminadas(String key) {
		return categoriasEliminadas.get(key);
	}

	public void setCategoriasEliminadas(String key,Object value) {
		this.categoriasEliminadas.put(key,value);
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
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

	public HashMap getDestinosCategoria() {
		return destinosCategoria;
	}

	public void setDestinosCategoria(HashMap destinosCategoria) {
		this.destinosCategoria = destinosCategoria;
	}
	public Object getDestinosCategoria(String key) {
		return destinosCategoria.get(key);
	}

	public void setDestinosCategoria(String key,Object value) {
		this.destinosCategoria.put(key,value);
	}

	public int getIndexDetalle() {
		return indexDetalle;
	}

	public void setIndexDetalle(int indexDetalle) {
		this.indexDetalle = indexDetalle;
	}

}
