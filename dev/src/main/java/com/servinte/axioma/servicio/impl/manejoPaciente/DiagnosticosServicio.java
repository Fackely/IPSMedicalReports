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
	 * Este M�todo se encarga de consultar los diagn�sticos
	 * registrados en el sistema
	 * 
	 * @return ArrayList<Diagnosticos>
	 * 
	 * @author, Fabi�n Becerra
	 *
	 */
	public ArrayList<Diagnosticos> consultarDiagnosticos(){
		return mundo.consultarDiagnosticos();
	}
	
	/**
	 * Este M�todo se encarga los acronimos de los diagnosticos
	 * 
	 * @return List<String>
	 * 
	 * @author, Fabi�n Becerra
	 *
	 */
	public List<String> obtenerAcronimosDiagnosticosEnSistema(){
		return mundo.obtenerAcronimosDiagnosticosEnSistema();
	}
	
}
