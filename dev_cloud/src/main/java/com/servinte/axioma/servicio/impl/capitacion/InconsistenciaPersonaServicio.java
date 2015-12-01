package com.servinte.axioma.servicio.impl.capitacion;

import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IInconsistenciaPersonaMundo;
import com.servinte.axioma.orm.InconsistenciaPersona;
import com.servinte.axioma.servicio.interfaz.capitacion.IInconsistenciaPersonaServicio;

public class InconsistenciaPersonaServicio implements IInconsistenciaPersonaServicio{

	IInconsistenciaPersonaMundo mundo;
	
	
	/**
	 * Metodo Constructor de la clase
	 * @author Camilo Gómez
	 */
	public InconsistenciaPersonaServicio(){
		mundo = CapitacionFabricaMundo.crearInconsistenciaPersonaMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro de InconsistenciaPersona
	 * 
	 * @param InconsistenciaPersona 
	 * @return boolean
	 * @author Camilo Gómez
	 *
	 */
	public boolean guardarInconsistenciaPersona(InconsistenciaPersona inconsistenciaPersona){
		return mundo.guardarInconsistenciaPersona(inconsistenciaPersona);
	}
	
}
