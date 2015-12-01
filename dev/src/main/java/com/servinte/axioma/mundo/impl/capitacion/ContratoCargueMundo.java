package com.servinte.axioma.mundo.impl.capitacion;

import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.IContratoCargueDAO;
import com.servinte.axioma.mundo.interfaz.capitacion.IContratoCargueMundo;
import com.servinte.axioma.orm.ContratoCargue;

public class ContratoCargueMundo implements IContratoCargueMundo{
	
	IContratoCargueDAO dao ;
	
	
	/**
	 * Método constructor de la clase
	 * 
	 * @author Ricardo Ruiz
	 */
	public ContratoCargueMundo(){
		dao = CapitacionFabricaDAO.crearContratoCargueDAO();
	}


	@Override
	public void guardarContratoCargue(ContratoCargue contratoCargue) {
		dao.guardarContratoCargue(contratoCargue);
	}

}
