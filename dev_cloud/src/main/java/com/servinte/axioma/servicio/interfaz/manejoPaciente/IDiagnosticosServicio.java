package com.servinte.axioma.servicio.interfaz.manejoPaciente;

import java.util.ArrayList;
import java.util.List;

import com.servinte.axioma.orm.Diagnosticos;

public interface IDiagnosticosServicio {

	/**
	 * Este M�todo se encarga de consultar los diagn�sticos
	 * registrados en el sistema
	 * 
	 * @return ArrayList<Diagnosticos>
	 * 
	 * @author, Fabi�n Becerra
	 *
	 */
	public ArrayList<Diagnosticos> consultarDiagnosticos();
	
	/**
	 * Este M�todo se encarga los acronimos de los diagnosticos
	 * 
	 * @return List<String>
	 * 
	 * @author, Fabi�n Becerra
	 *
	 */
	public List<String> obtenerAcronimosDiagnosticosEnSistema();
}
