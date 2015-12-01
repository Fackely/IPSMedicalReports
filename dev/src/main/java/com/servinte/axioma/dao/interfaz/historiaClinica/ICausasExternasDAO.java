package com.servinte.axioma.dao.interfaz.historiaClinica;

import java.util.ArrayList;

import com.servinte.axioma.orm.CausasExternas;

public interface ICausasExternasDAO {
	
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
