package com.servinte.axioma.mundo.interfaz.historiaClinica;

import java.util.ArrayList;

import com.servinte.axioma.orm.AntecedentesVarios;


/**
 * 
 * @author Cristhian Murillo
 *
 */
public interface IAntecedentesVariosMundo 
{
	

	/**
	 * Consulta los antecedentes varios de un paciente.
	 * 
	 * @param codPaciente
	 * @return ArrayList<AntecedentesVarios>
	 *
	 * @autor Cristhian Murillo
	 *
	*/
	public ArrayList<AntecedentesVarios> obtenerAntecedentesVariosPaciente(Integer codPaciente);
}
