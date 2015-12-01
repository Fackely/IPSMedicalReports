/**
 * 
 */
package com.servinte.axioma.mundo.interfaz.capitacion;

import java.sql.SQLException;
import java.util.ArrayList;

import com.princetonsa.dto.comun.DtoDatosGenericos;


/**
 * Esta clase se encarga de ejecutar los métodos de negocio para la entidad AntecedentesPacienteMundo 
 * @author Cristhian Murillo
 */
public interface IAntecedentesPacienteMundo 
{
	
	
	/**
	 * Retorna todos los antecedentes del paciente.
	 * 
	 * @param codigoPaciente
	 * @param codInstitucion
	 * 
	 * @throws SQLException 
	 *
	 * @autor Cristhian Murillo
	 *
	 */
	public ArrayList<DtoDatosGenericos> obtenerListaTodosAntecedentesPaciente(int codigoPaciente, int codInstitucion) throws SQLException;

}
