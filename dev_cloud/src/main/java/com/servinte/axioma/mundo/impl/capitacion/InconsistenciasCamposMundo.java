package com.servinte.axioma.mundo.impl.capitacion;

import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.IInconsistenciasCamposDAO;
import com.servinte.axioma.mundo.interfaz.capitacion.IInconsistenciasCamposMundo;
import com.servinte.axioma.orm.InconsistenciasCampos;

public class InconsistenciasCamposMundo implements IInconsistenciasCamposMundo{

	IInconsistenciasCamposDAO dao;
	
	/**
	 * Metodo Constructor de la clase
	 * @author Camilo Gómez
	 */
	public InconsistenciasCamposMundo(){
		dao = CapitacionFabricaDAO.crearInconsistenciasCamposDAO(); 
	}
	
	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro de InconsistenciasCampos
	 * 
	 * @param InconsistenciasCampos 
	 * @return boolean
	 * @author Camilo Gómez
	 *
	 */
	public boolean guardarInconsistenciasCampos(InconsistenciasCampos inconsistenciasCampos){
		return dao.guardarInconsistenciasCampos(inconsistenciasCampos);
	}
	
}
