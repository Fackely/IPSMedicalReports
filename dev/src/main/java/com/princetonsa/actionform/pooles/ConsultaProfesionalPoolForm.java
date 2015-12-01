/*
 * @(#)ConsultaProfesionalPoolForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.actionform.pooles;

import java.util.HashMap;
import org.apache.struts.validator.ValidatorForm;

/**
 * Forma para manejo presentación de la funcionalidad 
 * Consulta Profesionlaes Pool. 
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 17/Mar/2006
 */
public class ConsultaProfesionalPoolForm extends ValidatorForm
{

	/**
	 * Estado en el que se encuentra el proceso.
	 */
	private String estado;
	
	/**
	 * Nombre del Pool que se deseaa buscar
	 */
	private String nombrePool;
	
	/**
	 * Codigo del pool que se desea buscar
	 */
	private int codigoPool;
	
	/**
	 * Entero con el codigo del medico
	 */
	private int codigoMedico;
	
	/**
	 * Mapa con el resultado de la busqueda de los pooles por profesional de la salud
	 */
	private HashMap mapaConsultaPoolProfesional;
	
	
	
	public void reset ()
	{
		
		this.estado="";
		this.mapaConsultaPoolProfesional = new HashMap();
		this.codigoPool=0;
		this.nombrePool="";
		this.codigoMedico=0;
	}	
	
	
	
	
	/**
	 * @return Returns the codigoMedico.
	 */
	public int getCodigoMedico()
	{
		return codigoMedico;
	}

	/**
	 * @param codigoMedico The codigoMedico to set.
	 */
	public void setCodigoMedico(int codigoMedico)
	{
		this.codigoMedico=codigoMedico;
	}

	/**
	 * @return Returns the codigoPool.
	 */
	public int getCodigoPool()
	{
		return codigoPool;
	}

	/**
	 * @param codigoPool The codigoPool to set.
	 */
	public void setCodigoPool(int codigoPool)
	{
		this.codigoPool=codigoPool;
	}

	/**
	 * @return Returns the estado.
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado)
	{
		this.estado=estado;
	}

	/**
	 * @return Returns the nombrePool.
	 */
	public String getNombrePool()
	{
		return nombrePool;
	}

	/**
	 * @param nombrePool The nombrePool to set.
	 */
	public void setNombrePool(String nombrePool)
	{
		this.nombrePool=nombrePool;
	}

	/**
	 * @return Returns the mapaConsultaPoolProfesional.
	 */
	public HashMap getMapaConsultaPoolProfesional()
	{
		return mapaConsultaPoolProfesional;
	}
	
	/**
	 * @param mapaConsultaPoolProfesional The mapaConsultaPoolProfesional to set.
	 */
	public void setMapaConsultaPoolProfesional(HashMap mapaConsultaPoolProfesional)
	{
		this.mapaConsultaPoolProfesional = mapaConsultaPoolProfesional;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaConsultaPoolProfesional(String key, Object value) 
	{
		mapaConsultaPoolProfesional.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaConsultaPoolProfesional(String key) 
	{
		return mapaConsultaPoolProfesional.get(key);
	}
	
}
