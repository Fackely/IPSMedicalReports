package com.servinte.axioma.dao.impl.odontologia.contrato;

import com.servinte.axioma.dao.interfaz.odontologia.contrato.IContratoOdontologicoDAO;
import com.servinte.axioma.orm.ContratoOdontologico;
import com.servinte.axioma.orm.delegate.odontologia.contrato.ContratoOdontologicoDelegate;


/**
 * 
 * @author Edgar Carvajal
 *
 */
public class ContratoOdontologicoDAO  implements IContratoOdontologicoDAO{

	
	/**
	 * 
	 */
	private ContratoOdontologicoDelegate delegateI;
	
	
	
	/**
	 * CONSTRUTOR 
	 */
	public ContratoOdontologicoDAO()
	{
		delegateI = new ContratoOdontologicoDelegate();
	}
	
	
	@Override
	public ContratoOdontologico consultarAvanzadaContratoOdon(ContratoOdontologico dtoContrato) 
	{
		return delegateI.consultarAvanzadaContratoOdon(dtoContrato);
	}

	
	
	@Override
	public ContratoOdontologico buscarxId(Number id) {
		
		return delegateI.findById(id.longValue());
	}

	@Override
	public void eliminar(ContratoOdontologico objeto) {
		delegateI.delete(objeto);
		
	}

	@Override
	public void insertar(ContratoOdontologico objeto) 
	{
		delegateI.attachDirty(objeto);
	}

	
	@Override
	public void modificar(ContratoOdontologico objeto) 
	{
		delegateI.attachDirty(objeto);
		
	}

}
