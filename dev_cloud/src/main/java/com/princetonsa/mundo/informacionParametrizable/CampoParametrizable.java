/*
 * @(#)CampoParametrizable.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01 
 *
 */

package com.princetonsa.mundo.informacionParametrizable;

import util.InfoDatosInt;

/**
 * Clase para manejar un campo parametrizable
 * 
 * @version 1.0 Nov 5, 2003
 */
public class CampoParametrizable extends InfoDatosInt
{
	/**
	 * Número que ocupa este campo en su sección
	 */
	private int orden;
	
	/**
	 * Constructor que recibe todos los posibles atributos
	 * de un campo parametrizable
	 * 
	 * @param codigoCampo Código del campo parametrizable 
	 * @param ordenCampo Orden que ocupa este campo dentro de su sección
	 * @param nombre Nombre del campo
	 * @param descripcion Valor llenado por el usuario
	 */
	public CampoParametrizable (int codigoCampo, int ordenCampo, String nombre, String descripcion)
	{
		this.setCodigo(codigoCampo);
		orden=ordenCampo;
		this.setNombre(nombre);
		this.setDescripcion(descripcion);
	}

	/**
	 * @return
	 */
	public int getOrden()
	{
		return orden;
	}

	/**
	 * @param i
	 */
	public void setOrden(int i)
	{
		orden = i;
	}

}
