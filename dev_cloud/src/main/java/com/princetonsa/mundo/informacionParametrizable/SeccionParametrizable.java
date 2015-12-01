/*
 * @(#)SeccionParametrizable.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01 
 *
 */

package com.princetonsa.mundo.informacionParametrizable;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import util.InfoDatosInt;

/**
 * Clase para manejar una sección parametrizable y 
 * su lista de campos
 * 
 * @version 1.0, Oct 29, 2003
 */
public class SeccionParametrizable
{

	/**
	 * Código de esta sección parametrizable
	 */
	private int codigo;

	/**
	 * Nombre de esta sección parametrizable
	 */
	private String nombre;
	
	/**
	 * Conjunto de campos existentes en esta
	 * sección parametrizable
	 */
	private Collection camposParametrizables;
	
	/**
	 * Constructor Vacío de la Clase
	 */
	public SeccionParametrizable()
	{
		clean();
	}

	/**
	 * Constructor sencillo de la clase, recibe
	 * código y nombre de la sección que se
	 * crea
	 * 
	 * @param codigo Código de la sección
	 * que se crea
	 * @param nombre Nombre de la sección
	 * que se crea
	 */
	public SeccionParametrizable(int codigo, String nombre)
	{
		clean();
		this.codigo=codigo;
		this.nombre=nombre;
	}
	
	/**
	 * Método que limpia este objeto
	 *
	 */
	public void clean()
	{
		codigo=0;
		nombre="";
		camposParametrizables=new LinkedList();
	}
	
	/**
	 * Método que agrega un campo parametrizable a
	 * esta sección
	 * 
	 * @param campo Campo a agregar a esta sección
	 * parametrizable
	 */
	public void agregarCampoParametrizable (InfoDatosInt campo)
	{
		camposParametrizables.add(campo);
	}
	
	/**
	 * Iterador que permite recorrer todos los campos 
	 * parametrizables de esta sección
	 * @return
	 */
	public Iterator getIteradorCamposParametrizables ()
	{
		return camposParametrizables.iterator();
	}
	
	/**
	 * Método que retorna el número de campos parametrizables
	 * de esta sección
	 * 
	 * @return
	 */
	public int getNumeroCamposParametrizables()
	{
		return camposParametrizables.size();
	}
	
	
	
	/**
	 * @return
	 */
	public int getCodigo()
	{
		return codigo;
	}

	/**
	 * @return
	 */
	public String getNombre()
	{
		return nombre;
	}

}
