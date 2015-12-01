package com.servinte.axioma.mundo.interfaz.historiaClinica;

import java.util.ArrayList;

import com.servinte.axioma.orm.CausasExternas;

public interface ICausasExternasMundo {
	
	/**
	 * Este Método se encarga de consultar las causas externas
	 * registradas en el sistema
	 * 
	 * @return ArrayList<CausasExternas>
	 * 
	 * @author, Fabián Becerra
	 *
	 */
	public ArrayList<CausasExternas> consultarCausasExternas();

}
