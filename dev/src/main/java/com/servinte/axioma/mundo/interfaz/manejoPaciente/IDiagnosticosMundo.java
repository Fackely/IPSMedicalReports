package com.servinte.axioma.mundo.interfaz.manejoPaciente;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.servinte.axioma.orm.Diagnosticos;

public interface IDiagnosticosMundo 
{

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
	
	
	/**
	 * Realiza la b�squeda del �ltimo diagnostico asociado a los parametros
	 * @param parametros
	 * @return DtoDiagnostico
	 *
	 * @autor Cristhian Murillo
	 */
	public DtoDiagnostico ultimoDiagnostico(DtoDiagnostico parametros);
	
	
}
