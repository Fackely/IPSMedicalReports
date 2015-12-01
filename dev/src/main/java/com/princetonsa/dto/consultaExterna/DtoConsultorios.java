package com.princetonsa.dto.consultaExterna;

import java.io.Serializable;

public class DtoConsultorios implements Serializable {

	

	private int codigo; 
	private String descripcion;
	private int centroAtencion;
	
	
;
	
	 public DtoConsultorios(){
		 this.clean();
	
	 }
	

	 void clean(){
			this.codigo=0; 
			this.centroAtencion=0;
		    this.descripcion="";
		}


	/**
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}


	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}


	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}


	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	/**
	 * @return the centroAtencion
	 */
	public int getCentroAtencion() {
		return centroAtencion;
	}


	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}
}