package com.servinte.axioma.dao.impl.odontologia.contrato;

import com.servinte.axioma.dao.interfaz.odontologia.contrato.IFirmaContratoInstitucionDAO;
import com.servinte.axioma.orm.FirmasContratoOtrosiInst;
import com.servinte.axioma.orm.delegate.odontologia.contrato.FirmaContratoInstittucionDelegate;


/**
 * 
 * @author Edgar Carvajal
 *
 */
public class FirmaContratoInstitucionDAO implements IFirmaContratoInstitucionDAO{

	
	
	
	
	/**
	 *DELEGATE
	 */
	private FirmaContratoInstittucionDelegate delegateFirma; 
	
	
	
	/**
	 * CONSTRUTOR 
	 */
	public FirmaContratoInstitucionDAO()
	{
		delegateFirma= new FirmaContratoInstittucionDelegate();
	}
	
	
	
	
	@Override
	public FirmasContratoOtrosiInst buscarxId(Number id) {
		return 	delegateFirma.findById(id.longValue());
	}

	@Override
	public void eliminar(FirmasContratoOtrosiInst objeto) {
		delegateFirma.delete(objeto);
		
	}

	@Override
	public void insertar(FirmasContratoOtrosiInst objeto) {
		delegateFirma.attachDirty(objeto);
		
	}

	
	
	@Override
	public void modificar(FirmasContratoOtrosiInst objeto) {
		delegateFirma.attachDirty(objeto);
		
	}
	
	

}
