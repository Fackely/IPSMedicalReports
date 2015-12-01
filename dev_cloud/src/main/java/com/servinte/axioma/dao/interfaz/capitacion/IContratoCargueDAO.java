package com.servinte.axioma.dao.interfaz.capitacion;

import com.servinte.axioma.orm.ContratoCargue;

/**
 * Interface que maneja el acceso a datos de la clase ContratoCargue
 * 
 * @author Ricardo Ruiz
 *
 */
public interface IContratoCargueDAO {

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
