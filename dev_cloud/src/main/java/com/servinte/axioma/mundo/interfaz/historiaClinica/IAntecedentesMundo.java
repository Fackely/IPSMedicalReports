package com.servinte.axioma.mundo.interfaz.historiaClinica;

import java.util.ArrayList;

import com.servinte.axioma.orm.Antecedentes;


/**
 * 
 * @author Cristhian Murillo
 *
 */
public interface IAntecedentesMundo 
{
	
	/**
	 * Consulta todos los antecedentes del sistema
	 * 
	 * @param codPaciente
	 * @return ArrayList<Antecedentes>
	 *
	 * @autor Cristhian Murillo
	 *
	*/
	public ArrayList<Antecedentes> listarAntecedentes();
	
	
}
