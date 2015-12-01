/*
 * @(#)TipoAntecedenteFamiliar.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo.antecedentes;

/**
 * Clase para el manejo de un antecedente familiar : código, nombre,
 * observacion, parentesco con el familiar.
 *
 * @version 1.0, Julio 31, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class TipoAntecedenteFamiliar
{
	/**
	 * Código del antecedente familiar en la base de datos
	 */
	private int codigo;
	
	/**
	 * Nombre del antecedente familiar en la base de datos
	 */
	private String nombre;
	
	/**
	 * Cadena con las observaciones
	 */
	private String observaciones;
	
	/**
	 * Parentesco del paciente con la persona que tiene este antecedente.
	 */
	private String parentesco;
	
	
	/**Atributo que informa si este tipo de enfermedad familiar ya se ha ingresado a la base de datos o no*/
	private boolean ingresadoBD;
	
	/**
	 * Constructora de la clase. Inicializa todos los atributos
	 */
	public TipoAntecedenteFamiliar()
	{
		codigo = 0;
		nombre = "";
		observaciones = "";
		parentesco = "";
	}
	
	/**
	 * Constructora de la clase. Inicializa todos los atributos en los valores
	 * dados.
	 * @param int, codigo
	 * @param String, nombre
	 * @param String, observaciones
	 * @param String, parentesco
	 */
	public TipoAntecedenteFamiliar(int codigo, String nombre, String observaciones, String parentesco)
	{
		this.codigo = codigo;
		this.nombre = nombre;
		this.observaciones = observaciones;
		this.parentesco = parentesco;
	}
	
	/**
	 * Retorna el código del antecedente familiar en la base de datos
	 * @return int, codigo
	 */
	public int getCodigo()
	{
		return codigo;
	}

	/**
	 * Asigna el código del antecedente familiar en la base de datos
	 * @param int, codigo
	 */
	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
	}

	/**
	 * Retorna el nombre del antecedente familiar en la base de datos
	 * @return String, nombre
	 */
	public String getNombre()
	{
		return nombre;
	}

	/**
	 * Asigna el nombre del antecedente familiar en la base de datos
	 * @param String, nombre
	 */
	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}

	/**
	 * Retorna la cadena con las observaciones
	 * @return String, observaciones
	 */
	public String getObservaciones()
	{
		return observaciones;
	}

	/**
	 * Asigna la cadena con las observaciones
	 * @param String, observaciones
	 */
	public void setObservaciones(String observaciones)
	{
		this.observaciones = observaciones;
	}

	/**
	 * Retorna el parentesco del paciente con la persona que tiene este
	 * antecedente.
	 * @return String, parentesco
	 */
	public String getParentesco()
	{
		return parentesco;
	}

	/**
	 * Asigna el parentesco del paciente con la persona que tiene este
	 * antecedente.
	 * @param parentesco The parentesco to set
	 */
	public void setParentesco(String parentesco)
	{
		this.parentesco = parentesco;
	}

	/**
	 * Returns the ingresadoBD.
	 * @return boolean
	 */
	public boolean isIngresadoBD() {
		return ingresadoBD;
	}

	/**
	 * Sets the ingresadoBD.
	 * @param ingresadoBD The ingresadoBD to set
	 */
	public void setIngresadoBD(boolean ingresadoBD) {
		this.ingresadoBD = ingresadoBD;
	}

}
