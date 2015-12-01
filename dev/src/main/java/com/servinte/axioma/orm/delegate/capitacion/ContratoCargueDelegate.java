package com.servinte.axioma.orm.delegate.capitacion;

import com.servinte.axioma.orm.ContratoCargue;
import com.servinte.axioma.orm.ContratoCargueHome;

/**
 * Clase que maneja el acceso a datos de la clase ContratoCargue
 * 
 * @author Ricardo Ruiz
 *
 */
public class ContratoCargueDelegate extends ContratoCargueHome{

	
	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro de Contrato Cargue
	 * 
	 * @param ContratoCargue
	 * @author Ricardo Ruiz
	 *
	 */
	public void guardarContratoCargue(ContratoCargue contratoCargue){
		super.attachDirty(contratoCargue);				
	}
}
