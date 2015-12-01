package com.servinte.axioma.dao.interfaz.historiaClinica;

import java.util.ArrayList;

import com.servinte.axioma.orm.AntecedentesVarios;

/**
 * 
 * @author Cristhian Murillo
 */
public interface IAntecedentesVariosDAO {
	
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
