package com.servinte.axioma.mundo.interfaz.capitacion;

import com.servinte.axioma.orm.ContratoCargue;

/**
 * Interface que maneja el acceso a datos de la clase ContratoCargue
 * 
 * @author Ricardo Ruiz
 *
 */
public interface IContratoCargueMundo {

	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro de Contrato Cargue
	 * 
	 * @param ContratoCargue
	 * @author Ricardo Ruiz
	 *
	 */
	public void guardarContratoCargue(ContratoCargue contratoCargue);
}
