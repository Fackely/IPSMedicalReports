package com.princetonsa.actionform.inventarios;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.Listado;


public class FormaFarmaceuticaForm extends ActionForm
{

/////
	/**
	 * Variable para manejar el estado en el que se encuentra la funcionalidad
	 */
	private String estado;
	
	/**
	 * mapa para manejar las formas farmaceuticas.
	 */
	private HashMap formasFarma;
	
	/**
	 * mapa para manejar las categorias eliminadas.
	 */
	private HashMap formasEliminadas;
	
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
     * Mapa para almacenr las vias de administracion de una forma farmaceutica.
     */
    private HashMap viasAdminForma;
    
    
    
    //atributos para el pager*******************************
    private int maxPageItems;
    private String linkSiguiente;
    private int offset;
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();

		if(estado.equals("guardar"))
		{
			//*************VALIDACION DE CAMPOS REQUERIDOS***********************************************************
			int numRegistros = Integer.parseInt(this.getFormasFarma("numRegistros")+"");
			for(int k=0;k<numRegistros;k++)
		    {
				if(((this.getFormasFarma("consecutivo_"+k)+"").trim()).equals(""))  //El código es requerido
			          errores.add("CODIGO REQUERIDO", new ActionMessage("errors.required","El código para el registro N° "+(k+1)));
				if(((this.getFormasFarma("descripcion_"+k)+"").trim()).equals(""))  //descripcion es requerido
			          errores.add("DESCRIPCION REQUERIDO", new ActionMessage("errors.required","La descripción para el registro N° "+(k+1)));

		    }
			//*************************************************************************************************************
			//**************VALIDACION DE REGISTROS REPETIDOS*************************************************************
			errores = Listado.validarRegistrosRepetidos(errores, this.formasFarma, "consecutivo_", "", "códigos");
			//*************************************************************************************************************
			
			if(!errores.isEmpty())
				this.estado = "empezar";
		}	
		return errores;
	}
	
	/**
     * inicializar atributos de esta forma
     *
     */
    public void reset ()
    {
    	this.formasFarma=new HashMap();
    	this.formasEliminadas=new HashMap();
    	this.regEliminar=ConstantesBD.codigoNuncaValido;
        this.patronOrdenar = "";
        this.ultimoPatron = "";
        this.indexDetalle=ConstantesBD.codigoNuncaValido;
        this.viasAdminForma=new HashMap();
        this.offset = 0;
        this.linkSiguiente = "formasFarmaceuticas.jsp?pager.offset=0";
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

	

	public int getIndexDetalle() {
		return indexDetalle;
	}

	public void setIndexDetalle(int indexDetalle) {
		this.indexDetalle = indexDetalle;
	}

	/**
	 * @return the maxPageItems
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}

	/**
	 * @param maxPageItems the maxPageItems to set
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}

	/**
	 * @return the linkSiguiente
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	/**
	 * @param linkSiguiente the linkSiguiente to set
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * @return the formasEliminadas
	 */
	public HashMap getFormasEliminadas() {
		return formasEliminadas;
	}

	/**
	 * @param formasEliminadas the formasEliminadas to set
	 */
	public void setFormasEliminadas(HashMap formasEliminadas) {
		this.formasEliminadas = formasEliminadas;
	}
	
	/**
	 * @return elemento del mapa formasEliminadas
	 */
	public Object getFormasEliminadas(String key) {
		return formasEliminadas.get(key);
	}

	/**
	 * @param Asigna elemento al mapa formasEliminadas 
	 */
	public void setFormasEliminadas(String key,Object obj) {
		this.formasEliminadas.put(key,obj);
	}
	

	/**
	 * @return the formasFarma
	 */
	public HashMap getFormasFarma() {
		return formasFarma;
	}

	/**
	 * @param formasFarma the formasFarma to set
	 */
	public void setFormasFarma(HashMap formasFarma) {
		this.formasFarma = formasFarma;
	}
	
	
	/**
	 * @return Retorna elemento del mapa formasFarma
	 */
	public Object  getFormasFarma(String key) {
		return formasFarma.get(key);
	}

	/**
	 * @param Asigna elemento al mapa formasFarma 
	 */
	public void setFormasFarma(String key,Object obj) {
		this.formasFarma.put(key,obj);
	}
	
	
	
	
	
	
	

	/**
	 * @return the viasAdminForma
	 */
	public HashMap getViasAdminForma() {
		return viasAdminForma;
	}

	/**
	 * @param viasAdminForma the viasAdminForma to set
	 */
	public void setViasAdminForma(HashMap viasAdminForma) {
		this.viasAdminForma = viasAdminForma;
	}
	
	/**
	 * @return Retorna elemento del mapa viasAdminForma
	 */
	public Object getViasAdminForma(String key) {
		return viasAdminForma.get(key);
	}

	/**
	 * @param Asigna elemento al mapa viasAdminForma 
	 */
	public void setViasAdminForma(String key,Object obj) {
		this.viasAdminForma.put(key,obj);
	}
	
/////	
	
}
