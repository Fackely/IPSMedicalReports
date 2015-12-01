package com.servinte.axioma.mundo.impl.historiaClinica;

import java.util.ArrayList;

import com.servinte.axioma.dao.fabrica.historiaClinica.HistoriaClinicaFabricaDAO;
import com.servinte.axioma.dao.interfaz.historiaClinica.ICausasExternasDAO;
import com.servinte.axioma.mundo.interfaz.historiaClinica.ICausasExternasMundo;
import com.servinte.axioma.orm.CausasExternas;

public class CausasExternasMundo implements ICausasExternasMundo{
	
	
	ICausasExternasDAO dao;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Fabián Becerra
	 */
	public CausasExternasMundo(){
		dao = HistoriaClinicaFabricaDAO.crearCausasExternasDAO();
	}	
	
	/**
	 * Este Método se encarga de consultar las causas externas
	 * registradas en el sistema
	 * 
	 * @return ArrayList<CausasExternas>
	 * 
	 * @author, Fabián Becerra
	 *
	 */
	public ArrayList<CausasExternas> consultarCausasExternas(){
		return dao.consultarCausasExternas();
	}

}
