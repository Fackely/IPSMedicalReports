package com.servinte.axioma.dao.interfaz.historiaClinica;

import java.util.ArrayList;

import com.servinte.axioma.orm.Antecedentes;

/**
 * 
 * @author Cristhian Murillo
 */
public interface IAntecedentesDAO 
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
