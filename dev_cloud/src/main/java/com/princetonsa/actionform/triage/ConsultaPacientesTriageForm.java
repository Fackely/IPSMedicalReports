/*
 * @(#)ConsultaPacientesTraigeForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.actionform.triage;

import java.util.HashMap;
import org.apache.struts.validator.ValidatorForm;

/**
 * Forma para manejo presentación de la funcionalidad 
 * Consulta de pacientes Pendientes por Atención de Triage
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 01 /Jun/ 2006
 */
public class ConsultaPacientesTriageForm extends ValidatorForm
{

	/**
	 * Estado en el que se encuentra el proceso.
	 */
	private  String estado = "";
	
	/**
	 * almacena los datos de la consulta
	 */
	private HashMap mapaPacientesTriage;
	
 	/**
	 * Patron de ordenamiento por columnas
	 */
	private String patronOrdenar;
	
	/**
	 * String ultimo patron de ordenamiento
	 */
	private String ultimoPatron;
	
	/**
	 * Posicion del mapa a eliminar
	 */
	private int posicionMapa;
	
 	/**
 	 * Reset generla de la Forma
 	 */
	public void reset ()
	{
		this.mapaPacientesTriage = new HashMap ();
		this.estado = "";
	 	this.patronOrdenar = "";
	 	this.ultimoPatron = "";
	 	this.posicionMapa = 0;
	}
	
	/**
	 * Reset único para los mapas
	 */
	public void resetMapa()
	{
		this.mapaPacientesTriage = new HashMap ();
	}

	
	
	/**
	 * @return Returns the posicionMapa.
	 */
	public int getPosicionMapa()
	{
		return posicionMapa;
	}

	/**
	 * @param posicionMapa The posicionMapa to set.
	 */
	public void setPosicionMapa(int posicionMapa)
	{
		this.posicionMapa=posicionMapa;
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
	 * @return Returns the patronOrdenar.
	 */
	public String getPatronOrdenar()
	{
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar The patronOrdenar to set.
	 */
	public void setPatronOrdenar(String patronOrdenar)
	{
		this.patronOrdenar=patronOrdenar;
	}

	/**
	 * @return Returns the ultimoPatron.
	 */
	public String getUltimoPatron()
	{
		return ultimoPatron;
	}

	/**
	 * @param ultimoPatron The ultimoPatron to set.
	 */
	public void setUltimoPatron(String ultimoPatron)
	{
		this.ultimoPatron=ultimoPatron;
	}
	
	/**
	 * @return Returns the mapaPacientesTriage
	 */
	public HashMap getMapaPacientesTriage()
	{
		return mapaPacientesTriage;
	}
	
	/**
	 * @param mapaPacientesTriage The mapaPacientesTriage to set.
	 */
	public void setMapaPacientesTriage(HashMap mapaPacientesTriage)
	{
		this.mapaPacientesTriage = mapaPacientesTriage;
	}
	
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaPacientesTriage(String key) 
	{
		return mapaPacientesTriage.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaPacientesTriage(String key, Object value) 
	{
		mapaPacientesTriage.put(key, value);
	}
}