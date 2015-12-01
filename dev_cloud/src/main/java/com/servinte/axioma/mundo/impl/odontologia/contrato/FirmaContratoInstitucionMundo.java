package com.servinte.axioma.mundo.impl.odontologia.contrato;

import com.servinte.axioma.dao.fabrica.odontologia.contrato.ContratoOdontologicoFabricaDAO;
import com.servinte.axioma.dao.interfaz.odontologia.contrato.IFirmaContratoInstitucionDAO;
import com.servinte.axioma.mundo.interfaz.odontologia.contrato.IFirmaContratoInstitucionMundo;
import com.servinte.axioma.orm.FirmasContratoOtrosiInst;


/**
 * 
 * @author axioma
 *
 */
public class FirmaContratoInstitucionMundo implements IFirmaContratoInstitucionMundo

{

	
	
	
	private IFirmaContratoInstitucionDAO firmaDAO;
	
	
	
	
	
	/**
	 * CONSTRUTOR 
	 */
	public FirmaContratoInstitucionMundo()
	{
		firmaDAO=ContratoOdontologicoFabricaDAO.crearFirmasInstitucion();
	}

	
	
	
	


	@Override
	public FirmasContratoOtrosiInst buscarxId(Number id) {
	
		return firmaDAO.buscarxId(id);
	}



	@Override
	public void eliminar(FirmasContratoOtrosiInst objeto) {
		
		firmaDAO.eliminar(objeto);
	}



	@Override
	public void insertar(FirmasContratoOtrosiInst objeto) {
		firmaDAO.insertar(objeto);
		
	}



	@Override
	public void modificar(FirmasContratoOtrosiInst objeto) {
		firmaDAO.modificar(objeto);
		
	}
	
	

}
