/*
 * @(#)CategoriaAlergia.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo.antecedentes;

import java.util.ArrayList;

import util.InfoDatosBD;

/**
 * Clase para el manejo de una categoria de alergias: código, nombre y colección
 * de alergias.
 *
 * @version 1.0, Julio 31, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 * @see util.InfoDatosBD
 */
public class CategoriaAlergia
{
	/**
	 * Código de la categoria de alergia en la base de datos
	 */
	private int codigo;
	
	/**
	 * Nombre de la categoría de alergia.
	 */
	private String nombre;
	
	/**
	 * Arreglo con las alergias predefinidas en la base de datos que pertenecen
	 * a esta categoria. Cada alergia es representada por medio de
	 * "InfoDatosBD", en donde se almacena su código dentro de la base de
	 * datos, su nombre y la descripción u observación asociada.
	 */
	private ArrayList tiposAlergiasPredefinidas;
	
	/**
	 * Arreglo con las alergias que no hacen parte de las predefinidas en  para
	 * esta categoria en la base de datos, pero que se adicionaron con la opción
	 * de "otra alergia dentro de la categoria". Cada alergia es representada
	 * por medio de "InfoDatosBD", en donde se almacena su código dentro de la
	 * base de datos, su nombre y la descripción u observación asociada.
	 */
	private ArrayList tiposAlergiasAdicionales;
	
	/**
	 * Constructora de una categoria de alergia, inicializa todos los
	 * parametros.
	 */
	public CategoriaAlergia()
	{
		codigo = 0;
		nombre = "";
		tiposAlergiasPredefinidas = new ArrayList();
		tiposAlergiasAdicionales = new ArrayList();
	}

	/**
	 * Constructora de una categoria de alergia, inicializa todos los
	 * parametros con los dados.
	 * @param	int, codigo
	 * @param	String,	nombre
	 * @param	ArrayList, alergias predefinidas
	 * @param	ArrayList, alergias adicionales
	 */	
	public CategoriaAlergia(int codigo, String nombre, ArrayList tiposAlergiasPredefinidas, ArrayList tiposAlergiasAdicionales)
	{
		this.codigo = codigo;
		this.nombre = nombre;
		this.tiposAlergiasPredefinidas = tiposAlergiasPredefinidas;
		this.tiposAlergiasAdicionales = tiposAlergiasAdicionales;
	}
	
	/**
	 * Retorna el código de la categoria de alergia en la base de datos
	 * @return 	int, codigo
	 */
	public int getCodigo()
	{
		return codigo;
	}

	/**
	 * Asigna el código de la categoria de alergia en la base de datos
	 * @param 	int, codigo
	 */
	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
	}

	/**
	 * Retorna el nombre de la categoría de alergia.
	 * @return	String, nombre de la categoria
	 */
	public String getNombre()
	{
		return nombre;
	}

	/**
	 * Asigna el nombre de la categoría de alergia.
	 * @param	String, nombre de la categoria
	 */
	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}

	/**
	 * Retorna la lista con las alergias predefinidas en la base de datos que
	 * pertenecen a esta categoria. Cada alergia es representada por medio de
	 * "InfoDatosBD", en donde se almacena su código dentro de la base de
	 * datos, su nombre y la descripción u observación asociada.
	 * @return 	ArrayList (InfoDatosBD), alergias predefinidas
	 */
	public ArrayList getTiposAlergiasPredefinidas()
	{
		return tiposAlergiasPredefinidas;
	}

	/**
	 * Asigna la lista con las alergias predefinidas en la base de datos que
	 * pertenecen a esta categoria. Cada alergia es representada por medio de
	 * "InfoDatosBD", en donde se almacena su código dentro de la base de
	 * datos, su nombre y la descripción u observación asociada.
	 * @param	ArrayList (InfoDatosBD), alergias predefinidas
	 */
	public void setTiposAlergiasPredefinidas(ArrayList tiposAlergiasPredefinidas)
	{
		this.tiposAlergiasPredefinidas = tiposAlergiasPredefinidas;
	}
	
	/**
	 * Retorna la alergia dado el indice dentro de la lista
	 * @param 	int, indice
	 * @return 		InfoDatosBD, alergia predefinida
	 */
	public InfoDatosBD getAlergiaPredefinida(int indice)
	{
		return (InfoDatosBD)this.tiposAlergiasPredefinidas.get(indice);
	}
	
	/**
	 * Adiciona una alergia a esta categoria
	 * @param 	InfoDatosBD, alergia predefinida a agregar
	 */
	public void setAlergiaPredefinida(InfoDatosBD alergia)
	{
		this.tiposAlergiasPredefinidas.add(alergia);
	}

	/**
	 * Retorna la lista con las alergias que no hacen parte de las predefinidas
	 * en  para esta categoria en la base de datos, pero que se adicionaron con
	 * la opción de "otra alergia dentro de la categoria". Cada alergia es
	 * representada por medio de "InfoDatosBD", en donde se almacena su código
	 * dentro de la base de datos, su nombre y la descripción u observación
	 * asociada.
	 * @return 	ArrayList (InfoDatosBD), alergias adicionales
	 */
	public ArrayList getTiposAlergiasAdicionales()
	{
		return tiposAlergiasAdicionales;
	}

	/**
	 * Asigna la lista con las alergias que no hacen parte de las predefinidas
	 * en  para esta categoria en la base de datos, pero que se adicionaron con
	 * la opción de "otra alergia dentro de la categoria". Cada alergia es
	 * representada por medio de "InfoDatosBD", en donde se almacena su código
	 * dentro de la base de datos, su nombre y la descripción u observación
	 * asociada.
	 * @param 	ArrayList (InfoDatosBD), alergias adicionales
	 */
	public void setTiposAlergiasAdicionales(ArrayList tiposAlergiasAdicionales)
	{
		this.tiposAlergiasAdicionales = tiposAlergiasAdicionales;
	}

	/**
	 * Retorna la alergia dado el indice dentro de la lista
	 * @param 	int, indice
	 * @return 		InfoDatosBD, alergia no predefinida en la bd
	 */
	public InfoDatosBD getAlergiaAdicional(int indice)
	{
		return (InfoDatosBD)this.tiposAlergiasAdicionales.get(indice);
	}
	
	/**
	 * Adiciona una alergia a esta categoria
	 * @param 	InfoDatosBD, alergia no predefinida en la bd a agregar
	 */
	public void setAlergiaAdicional(InfoDatosBD alergia)
	{
		this.tiposAlergiasAdicionales.add(alergia);
	}
}
