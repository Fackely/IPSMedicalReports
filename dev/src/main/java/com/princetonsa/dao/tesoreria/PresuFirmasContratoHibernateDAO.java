package com.princetonsa.dao.tesoreria;

import java.util.ArrayList;

import com.servinte.axioma.orm.PresuFirmasContrato;
import com.servinte.axioma.orm.delegate.odontologia.contrato.PresuFirmasContratoDelegate;

/**
 * 
 * @author Cristhian Murillo
 */
public class PresuFirmasContratoHibernateDAO
{

	private PresuFirmasContratoDelegate delegate = new PresuFirmasContratoDelegate();
	
	

	/**
	 * Retorna las firmas de un presupuesto odontologico
	 * 
	 * @param codPresoOdonto
	 * @return
	 */
	public ArrayList<PresuFirmasContrato> obtenerFirmasPorPresuOdonto(long codPresoOdonto){
		return delegate.obtenerFirmasPorPresuOdonto(codPresoOdonto);
	}
	
}
