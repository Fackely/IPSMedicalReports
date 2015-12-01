package com.servinte.axioma.mundo.impl.capitacion;

import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.IInconsistenciaPersonaDAO;
import com.servinte.axioma.mundo.interfaz.capitacion.IInconsistenciaPersonaMundo;
import com.servinte.axioma.orm.InconsistenciaPersona;

public class InconsistenciaPersonaMundo implements IInconsistenciaPersonaMundo{

	IInconsistenciaPersonaDAO dao;
	
	
	/**
	 * Metodo Constructor de la clase
	 * @author Camilo G�mez
	 */
	public InconsistenciaPersonaMundo(){
		dao = CapitacionFabricaDAO.crearInconsistenciaPersonaDAO();
	}
	
	
	/**
	 * 
	 * Este M�todo se encarga de insertar en la base de datos
	 * un registro de InconsistenciaPersona
	 * 
	 * @param InconsistenciaPersona 
	 * @return boolean
	 * @author Camilo G�mez
	 *
	 */
	public boolean guardarInconsistenciaPersona(InconsistenciaPersona inconsistenciaPersona){
		return dao.guardarInconsistenciaPersona(inconsistenciaPersona);
	}
}
