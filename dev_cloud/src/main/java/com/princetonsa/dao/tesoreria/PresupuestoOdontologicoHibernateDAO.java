package com.princetonsa.dao.tesoreria;

import com.princetonsa.dto.odontologia.DtoFormatoImpresionContratoOdontologico;
import com.servinte.axioma.orm.delegate.odontologia.contrato.PresupuestoOdontologicoDelegate;

/**
 * 
 * @author Cristhian Murillo
 */
public class PresupuestoOdontologicoHibernateDAO
{

	private PresupuestoOdontologicoDelegate delegate = new PresupuestoOdontologicoDelegate();
	
	
	public DtoFormatoImpresionContratoOdontologico obtenerContratoOdontologico(
			DtoFormatoImpresionContratoOdontologico infoPresupuesto) {
		
		return delegate.obtenerContratoOdontologico(infoPresupuesto);
	}
	
}
