package com.servinte.axioma.servicio.impl.manejoPaciente;

import java.util.ArrayList;
import java.util.List;

import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IDiagnosticosMundo;
import com.servinte.axioma.orm.Diagnosticos;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IDiagnosticosServicio;

public class DiagnosticosServicio implements IDiagnosticosServicio{

	IDiagnosticosMundo  mundo;
	
	
	public DiagnosticosServicio(){
		mundo = ManejoPacienteFabricaMundo.crearDiagnosticosMundo();
	}
	/**
	 * Este Método se encarga de consultar los diagnósticos
	 * registrados en el sistema
	 * 
	 * @return ArrayList<Diagnosticos>
	 * 
	 * @author, Fabián Becerra
	 *
	 */
	public ArrayList<Diagnosticos> consultarDiagnosticos(){
		return mundo.consultarDiagnosticos();
	}
	
	/**
	 * Este Método se encarga los acronimos de los diagnosticos
	 * 
	 * @return List<String>
	 * 
	 * @author, Fabián Becerra
	 *
	 */
	public List<String> obtenerAcronimosDiagnosticosEnSistema(){
		return mundo.obtenerAcronimosDiagnosticosEnSistema();
	}
	
}
