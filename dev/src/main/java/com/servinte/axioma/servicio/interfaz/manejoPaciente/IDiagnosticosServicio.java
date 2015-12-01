package com.servinte.axioma.servicio.interfaz.manejoPaciente;

import java.util.ArrayList;
import java.util.List;

import com.servinte.axioma.orm.Diagnosticos;

public interface IDiagnosticosServicio {

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
}
