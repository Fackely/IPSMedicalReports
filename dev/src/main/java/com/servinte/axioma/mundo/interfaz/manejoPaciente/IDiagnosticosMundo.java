package com.servinte.axioma.mundo.interfaz.manejoPaciente;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.servinte.axioma.orm.Diagnosticos;

public interface IDiagnosticosMundo 
{

	/**
	 * Este Método se encarga de consultar los diagnósticos
	 * registrados en el sistema
	 * 
	 * @return ArrayList<Diagnosticos>
	 * 
	 * @author, Fabián Becerra
	 *
	 */
	public ArrayList<Diagnosticos> consultarDiagnosticos();
	
	/**
	 * Este Método se encarga los acronimos de los diagnosticos
	 * 
	 * @return List<String>
	 * 
	 * @author, Fabián Becerra
	 *
	 */
	public List<String> obtenerAcronimosDiagnosticosEnSistema();
	
	
	/**
	 * Realiza la búsqueda del último diagnostico asociado a los parametros
	 * @param parametros
	 * @return DtoDiagnostico
	 *
	 * @autor Cristhian Murillo
	 */
	public DtoDiagnostico ultimoDiagnostico(DtoDiagnostico parametros);
	
	
}
