package com.princetonsa.dto.historiaClinica;

import java.io.Serializable;

import util.ConstantesBD;


public class DtoImagenBase implements Cloneable,Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int codigoPk;
	
	private String nombreImagen;
	
	private String institucion;
	
	private String imagen;
	
	private String esEliminar;
	
	/**
	 * 
	 */
	public void clean()
	{
		this.codigoPk = ConstantesBD.codigoNuncaValido;
		this.nombreImagen= new String("");
		this.institucion= new String("");
		this.imagen = new String("");
		this.esEliminar = new String("");
	}
	
	/***
	 * 
	 *  
	 */
	public Object clone(){
	        Object obj=null;
	        try{
	            obj=super.clone();
	        }catch(CloneNotSupportedException ex){
	           
	        }
	        return obj;
	    }
	
	
	/**
	 * 
	 */
	public DtoImagenBase()
	{
		this.clean();
	}


	/**
	 * @return the codigoPk
	 */
	public int getCodigoPk() {
		return codigoPk;
	}


	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}


	/**
	 * @return the nombreImagen
	 */
	public String getNombreImagen() {
		return nombreImagen;
	}


	/**
	 * @param nombreImagen the nombreImagen to set
	 */
	public void setNombreImagen(String nombreImagen) {
		this.nombreImagen = nombreImagen;
	}


	/**
	 * @return the institucion
	 */
	public String getInstitucion() {
		return institucion;
	}


	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}


	/**
	 * @return the imagen
	 */
	public String getImagen() {
		return imagen;
	}


	/**
	 * @param imagen the imagen to set
	 */
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}


	/**
	 * @return the esEliminar
	 */
	public String getEsEliminar() {
		return esEliminar;
	}


	/**
	 * @param esEliminar the esEliminar to set
	 */
	public void setEsEliminar(String esEliminar) {
		this.esEliminar = esEliminar;
	}
}
