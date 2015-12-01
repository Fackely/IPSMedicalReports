package com.servinte.axioma.mundo.impl.tesoreria;

import java.util.ArrayList;

import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IConceptosIngTesoreriaDAO;
import com.servinte.axioma.mundo.interfaz.tesoreria.IConceptosIngTesoreriaMundo;
import com.servinte.axioma.orm.ConceptosIngTesoreria;

public class ConceptosIngTesoreriaMundo implements IConceptosIngTesoreriaMundo {

	private IConceptosIngTesoreriaDAO conceptoDAO;
	
	
	public ConceptosIngTesoreriaMundo(){
		this.setConceptoDAO(TesoreriaFabricaDAO.crearConceptosIngTesoreria());
		
	}
	
	@Override
	public ArrayList<ConceptosIngTesoreria> obtenerConceptosTipoIngAnticiposConvOdont() {
		return conceptoDAO.obtenerConceptosTipoIngAnticiposConvOdont();
	}

	public void setConceptoDAO(IConceptosIngTesoreriaDAO conceptoDAO) {
		this.conceptoDAO = conceptoDAO;
	}

	public IConceptosIngTesoreriaDAO getConceptoDAO() {
		return conceptoDAO;
	}

}
