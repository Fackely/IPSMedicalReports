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
 * Clase para manejar una secci�n parametrizable y 
 * su lista de campos
 * 
 * @version 1.0, Oct 29, 2003
 */
public class SeccionParametrizable
{

	/**
	 * C�digo de esta secci�n parametrizable
	 */
	private int codigo;

	/**
	 * Nombre de esta secci�n parametrizable
	 */
	private String nombre;
	
	/**
	 * Conjunto de campos existentes en esta
	 * secci�n parametrizable
	 */
	private Collection camposParametrizables;
	
	/**
	 * Constructor Vac�o de la Clase
	 */
	public SeccionParametrizable()
	{
		clean();
	}

	/**
	 * Constructor sencillo de la clase, recibe
	 * c�digo y nombre de la secci�n que se
	 * crea
	 * 
	 * @param codigo C�digo de la secci�n
	 * que se crea
	 * @param nombre Nombre de la secci�n
	 * que se crea
	 */
	public SeccionParametrizable(int codigo, String nombre)
	{
		clean();
		this.codigo=codigo;
		this.nombre=nombre;
	}
	
	/**
	 * M�todo que limpia este objeto
	 *
	 */
	public void clean()
	{
		codigo=0;
		nombre="";
		camposParametrizables=new LinkedList();
	}
	
	/**
	 * M�todo que agrega un campo parametrizable a
	 * esta secci�n
	 * 
	 * @param campo Campo a agregar a esta secci�n
	 * parametrizable
	 */
	public void agregarCampoParametrizable (InfoDatosInt campo)
	{
		camposParametrizables.add(campo);
	}
	
	/**
	 * Iterador que permite recorrer todos los campos 
	 * parametrizables de esta secci�n
	 * @return
	 */
	public Iterator getIteradorCamposParametrizables ()
	{
		return camposParametrizables.iterator();
	}
	
	/**
	 * M�todo que retorna el n�mero de campos parametrizables
	 * de esta secci�n
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
