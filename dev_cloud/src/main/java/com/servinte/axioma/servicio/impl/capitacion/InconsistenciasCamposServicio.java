package com.servinte.axioma.servicio.impl.capitacion;

import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IInconsistenciasCamposMundo;
import com.servinte.axioma.orm.InconsistenciasCampos;
import com.servinte.axioma.servicio.interfaz.capitacion.IInconsistenciasCamposServicio;

public class InconsistenciasCamposServicio implements IInconsistenciasCamposServicio{

	IInconsistenciasCamposMundo mundo;
	
	/**
	 * metodo Constructor de la clase
	 * @author Camilo G�mez
	 */
	public InconsistenciasCamposServicio (){
		mundo = CapitacionFabricaMundo.crearInconsistenciasCamposMundo();
	}
	
	/**
	 * 
	 * Este M�todo se encarga de insertar en la base de datos
	 * un registro de InconsistenciasCampos
	 * 
	 * @param InconsistenciasCampos 
	 * @return boolean
	 * @author Camilo G�mez
	 *
	 */
	public boolean guardarInconsistenciasCampos(InconsistenciasCampos inconsistenciasCampos){
		return mundo.guardarInconsistenciasCampos(inconsistenciasCampos);
	}
}
