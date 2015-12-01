/*
 * Creado el 3/01/2006
 * Jorge Armando Osorio Velasquez
 */
package com.princetonsa.actionform.inventarios;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;

public class ExistenciasInventariosForm extends ValidatorForm
{
	/**
	 * WorkFlow
	 */
	private String estado;

	/***
	 * Almacenes validos
	 */
	private HashMap almacenes;
	
	/**
     * Numero de items mostrados en la pagina
     */
    private int maxPageItems;
    
    /**
     * 
     */
    private int index;
    
    /**
     * 
     */
    private String nombreAlmacen;
    
    /**
     * 
     */
    private String nombreCentroAtencion;
    
    /**
     * 
     */
    private int codAlmacen;
    
    /**
     * 
     */
    private String clase;
    /**
     * 
     */
    private String grupo;
    /**
     * 
     */
    private String subgrupo;
    
    /**
     * 
     */
    private String articuloBusdqueda;
    
    /**
     * 
     */
    private HashMap mapaArticulos;
    
	/**
     * Patron por el que se desea ordenar el listado
     */
    private String patronOrdenar;
    
    /**
     * Ultimo Patron por el que se ordena.
     */
    private String ultimoPatron;
	/**
	 * M&eacute;todo para resetear las variables
	 *
	 */
    private String mostrarExt;
    
    /**
     * 
     */
    private String tipoBusqueda;
        
	public void reset()
	{
		this.maxPageItems=20;
		this.almacenes=new HashMap();
		this.index=ConstantesBD.codigoNuncaValido;
		this.nombreAlmacen="";
		this.codAlmacen=ConstantesBD.codigoNuncaValido;
		this.clase="";
		this.grupo="";
		this.subgrupo="";
		this.articuloBusdqueda="";
		this.mapaArticulos=new HashMap();
		this.patronOrdenar="";
		this.ultimoPatron="";
		this.nombreCentroAtencion="";
		this.mostrarExt="N";
		this.tipoBusqueda="";
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		return errores;
	}
	
	/**
	 * @return Retorna estado.
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * @param estado Asigna estado.
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	/**
	 * @return Retorna maxPageItems.
	 */
	public int getMaxPageItems()
	{
		return maxPageItems;
	}

	/**
	 * @param maxPageItems Asigna maxPageItems.
	 */
	public void setMaxPageItems(int maxPageItems)
	{
		this.maxPageItems = maxPageItems;
	}

	/**
	 * @return Retorna almacenes.
	 */
	public HashMap getAlmacenes()
	{
		return almacenes;
	}

	/**
	 * @param almacenes Asigna almacenes.
	 */
	public void setAlmacenes(HashMap almacenes)
	{
		this.almacenes = almacenes;
	}

	/**
	 * @return Retorna codAlmacen.
	 */
	public int getCodAlmacen()
	{
		return codAlmacen;
	}

	/**
	 * @param codAlmacen Asigna codAlmacen.
	 */
	public void setCodAlmacen(int codAlmacen)
	{
		this.codAlmacen = codAlmacen;
	}

	/**
	 * @return Retorna index.
	 */
	public int getIndex()
	{
		return index;
	}

	/**
	 * @param index Asigna index.
	 */
	public void setIndex(int index)
	{
		this.index = index;
	}

	/**
	 * @return Retorna nombreAlmacen.
	 */
	public String getNombreAlmacen()
	{
		return nombreAlmacen;
	}

	/**
	 * @param nombreAlmacen Asigna nombreAlmacen.
	 */
	public void setNombreAlmacen(String nombreAlmacen)
	{
		this.nombreAlmacen = nombreAlmacen;
	}

	/**
	 * @return Retorna clase.
	 */
	public String getClase()
	{
		return clase;
	}

	/**
	 * @param clase Asigna clase.
	 */
	public void setClase(String clase)
	{
		this.clase = clase;
	}

	/**
	 * @return Retorna grupo.
	 */
	public String getGrupo()
	{
		return grupo;
	}

	/**
	 * @param grupo Asigna grupo.
	 */
	public void setGrupo(String grupo)
	{
		this.grupo = grupo;
	}

	/**
	 * @return Retorna subgrupo.
	 */
	public String getSubgrupo()
	{
		return subgrupo;
	}

	/**
	 * @param subgrupo Asigna subgrupo.
	 */
	public void setSubgrupo(String subGrupo)
	{
		this.subgrupo = subGrupo;
	}

	/**
	 * @return Retorna articuloBusdqueda.
	 */
	public String getArticuloBusdqueda()
	{
		return articuloBusdqueda;
	}

	/**
	 * @param articuloBusdqueda Asigna articuloBusdqueda.
	 */
	public void setArticuloBusdqueda(String articuloBusdqueda)
	{
		this.articuloBusdqueda = articuloBusdqueda;
	}

	/**
	 * @return Retorna mapaArticulos.
	 */
	public HashMap getMapaArticulos()
	{
		return mapaArticulos;
	}

	/**
	 * @param mapaArticulos Asigna mapaArticulos.
	 */
	public void setMapaArticulos(HashMap mapaArticulos)
	{
		this.mapaArticulos = mapaArticulos;
	}

	/**
	 * @return Retorna patronOrdenar.
	 */
	public String getPatronOrdenar()
	{
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar Asigna patronOrdenar.
	 */
	public void setPatronOrdenar(String patronOrdenar)
	{
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @return Retorna ultimoPatron.
	 */
	public String getUltimoPatron()
	{
		return ultimoPatron;
	}

	/**
	 * @param ultimoPatron Asigna ultimoPatron.
	 */
	public void setUltimoPatron(String ultimoPatron)
	{
		this.ultimoPatron = ultimoPatron;
	}

	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}

	public void setMostrarExt(String mostrarExt) {
		this.mostrarExt = mostrarExt;
	}

	public String getMostrarExt() {
		return mostrarExt;
	}

	/**
	 * @return the tipoBusqueda
	 */
	public String getTipoBusqueda() {
		return tipoBusqueda;
	}

	/**
	 * @param tipoBusqueda the tipoBusqueda to set
	 */
	public void setTipoBusqueda(String tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}
	
}
