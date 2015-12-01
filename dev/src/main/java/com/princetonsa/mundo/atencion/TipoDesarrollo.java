/*
 * @(#)TipoDesarrollo.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo.atencion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Esta clase Maneja la pareja codigo y nombre para un
 * tipo de desarrollo, adem�s tiene una lista de Valores
 * de desarrollo 
 *
 * @version 1.0, Jun 12, 2003
 */
public class TipoDesarrollo 
{
	/**
	 * Entero que maneja el c�digo del Tipo de Desarrollo
	 */
	private int codigo=0;
	
	/**
	 * String que maneja el nombre del Tipo de Desarrollo
	 */
	private String nombre="";
	
	/**
	 * Colecci�n que maneja la lista de valores de desarrollo
	 * que corresponden a este Tipo. Es privada, de esta manera
	 * lo que se puede guardar esta definido por este objeto y 
	 * si se cambia la implementaci�n (Ej a TreeSet), solo toca
	 * cambiarlo en este m�todo sin preocuparse de los dem�s
	 */
	Collection valoresDesarrollo=null;

	/**
	 * Constructora vacia del objeto TipoDesarrollo 
	 */
	public TipoDesarrollo ()
	{
		codigo=0;
		nombre="";
		valoresDesarrollo=new ArrayList();
	}

	/**
	 * Constructora del objeto TipoDesarrollo que 
	 * recibe el nombre y el codigo de este tipo de
	 * desarrollo 
	 */
	public TipoDesarrollo (int codigo, String nombre)
	{
		this.codigo=codigo;
		this.nombre=nombre;
		valoresDesarrollo=new ArrayList();
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
	/**
	 * M�todo que a�ade un objeto ValorDesarrollo
	 * a la lista
	 * 
	 * @param aAnadir Objeto de tipo ValorDesarrollo  a a�adir
	 */
	public void addValorDesarrollo (ValorDesarrollo aAnadir)
	{
		valoresDesarrollo.add(aAnadir);
	}
	
	/**
	 * M�todo que retorna el i�simo objeto ValorDesarrollo
	 * en la lista 
	 * 
	 * @param indice
	 * @return
	 */
	public ValorDesarrollo getValorDesarrollo (int indice)
	{
		return (ValorDesarrollo) ((ArrayList)valoresDesarrollo).get(indice);
	}
	
	/**
	 * M�todo que retorna el n�mero de valores de desarrollo en
	 * la lista
	 * 
	 * @return
	 */
	public int getNumeroValoresDesarrollo ()
	{
		return valoresDesarrollo.size();
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
	public void setNombre(String string) {
		nombre = string;
	}

	/**
	 * M�todo que retorna un iterador para la colecci�n
	 * de valores de desarrollo
	 * @return
	 */
	public Iterator getValoresDesarrolloIterator ()
	{
		return valoresDesarrollo.iterator();
	}

}
