/*
 * @(#)CategoriaTriage.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo.atencion;

/**
 * 
 * @author sanmoy
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CategoriaTriage
{
	/**
	 * Identificador (código) de la categoría de triage
	 */
  	private int codigo = -1;
  	
  	/**
  	 * Nombre de la categoría triage.
  	 */
	private String nombre = "";
	
	/**
	 * Color(nombre del color) que se maneja para esa categoría triage. 
	 */
	private String color = "";
  
  	/**
  	 *  Comentario u observaciones sobre la categoría triage
  	 */
  	private String descripcion = "";
  
  
  	public CategoriaTriage()
  	{
  	}
  
	/**
	 * Constructor con todos los parametros
	 */
	public CategoriaTriage(String codigo, String nombre, String color, String descripcion) 
	{
		this.codigo = Integer.parseInt(codigo);
		this.nombre = nombre;
		this.color = color;
		this.descripcion = descripcion;
	}
	/**
	 * Constructor con todos los parametros
	 */
	public CategoriaTriage(int codigo, String nombre, String color, String descripcion) 
	{
		this.codigo = codigo;
		this.nombre = nombre;
		this.color = color;
		this.descripcion = descripcion;
	}
	
	/**
	 * @return
	 */

	/**
	 * @return
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @return
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @return
	 */
	public String getNombre() {
		return nombre;
	}


	/**
	 * @param string
	 */
	public void setColor(String string) {
		color = string;
	}

	/**
	 * @param string
	 */
	public void setDescripcion(String string) {
		descripcion = string;
	}

	/**
	 * @param string
	 */
	public void setNombre(String string) {
		nombre = string;
	}

	/**
	 * @return
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @param i
	 */
	public void setCodigo(int i) {
		codigo = i;
	}

}