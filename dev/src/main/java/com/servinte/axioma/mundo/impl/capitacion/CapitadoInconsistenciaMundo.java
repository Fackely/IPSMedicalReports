package com.servinte.axioma.mundo.impl.capitacion;

import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.ICapitadoInconsistenciaDAO;
import com.servinte.axioma.mundo.interfaz.capitacion.ICapitadoInconsistenciaMundo;
import com.servinte.axioma.orm.CapitadoInconsistencia;

public class CapitadoInconsistenciaMundo implements ICapitadoInconsistenciaMundo{

	ICapitadoInconsistenciaDAO dao;
	
	/**
	 * Metodo Constructor de la clase
	 * @author Camilo Gómez
	 */
	public CapitadoInconsistenciaMundo (){
		dao= CapitacionFabricaDAO.crearCapitadoInconsistenciaDAO();
	}
	
	
	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro de CapitadoInconsistencia
	 * 
	 * @param CapitadoInconsistencia 
	 * @return boolean
	 * @author Camilo Gómez
	 *
	 */
	public boolean guardarCapitadoInconsistencia(CapitadoInconsistencia capitadoInconsistencia){
		return dao.guardarCapitadoInconsistencia(capitadoInconsistencia);
	}
	
	
	/**
	 * Implementacion del findById
	 * @param id
	 * @return CapitadoInconsistencia
	 */
	public CapitadoInconsistencia obtenerCapitadoInconsistenciaPorId(Long id) {
		return dao.obtenerCapitadoInconsistenciaPorId(id);
	}
	
	
}
