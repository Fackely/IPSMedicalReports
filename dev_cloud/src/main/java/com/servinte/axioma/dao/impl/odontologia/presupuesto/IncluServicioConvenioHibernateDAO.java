
package com.servinte.axioma.dao.impl.odontologia.presupuesto;

import java.math.BigDecimal;

import com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IIncluServicioConvenioDAO;
import com.servinte.axioma.orm.delegate.odontologia.presupuesto.IncluServicioConvenioDelegate;

/**
 * Implementaci&oacute;n de la interfaz 
 * {@link IIncluServicioConvenioDAO}.
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see IncluServicioConvenioDelegate
 */

/**
 * @author axioma
 *
 */
public class IncluServicioConvenioHibernateDAO implements IIncluServicioConvenioDAO {

	
	IncluServicioConvenioDelegate incluServicioConvenioDelegate;
	
	
	/**
	 * Constructor de la Clase
	 */
	public IncluServicioConvenioHibernateDAO() {
		
		incluServicioConvenioDelegate = new IncluServicioConvenioDelegate();
	}


	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IIncluServicioConvenioDAO#actualizarPorcentajeDctoOdontologico(long, java.math.BigDecimal)
	 */
	@Override
	public boolean actualizarPorcentajeDctoOdontologico(long codigoIncluPresuEncabezado, BigDecimal porcentajeDescuentoOdontologico) {
		
		return incluServicioConvenioDelegate.actualizarPorcentajeDctoOdontologico(codigoIncluPresuEncabezado, porcentajeDescuentoOdontologico);
	}
}
