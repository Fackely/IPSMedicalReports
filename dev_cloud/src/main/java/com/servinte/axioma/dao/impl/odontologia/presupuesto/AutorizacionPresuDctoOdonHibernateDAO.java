/**
 * @autor Jorge Armando Agudelo Quintero
 */
package com.servinte.axioma.dao.impl.odontologia.presupuesto;

import com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IAutorizacionPresuDctoOdonDAO;
import com.servinte.axioma.orm.AutorizacionPresuDctoOdon;
import com.servinte.axioma.orm.delegate.odontologia.presupuesto.AutorizacionPresuDctoOdonDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link IAutorizacionPresuDctoOdonDAO}
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see AutorizacionPresuDctoOdonDelegate
 */

public class AutorizacionPresuDctoOdonHibernateDAO implements IAutorizacionPresuDctoOdonDAO {

	
	AutorizacionPresuDctoOdonDelegate autorizacionPresuDctoOdonDelegate;
	
	
	/**
	 * Constructor de la clase
	 */
	public AutorizacionPresuDctoOdonHibernateDAO() {
		
		autorizacionPresuDctoOdonDelegate =  new AutorizacionPresuDctoOdonDelegate();
	}
	
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IAutorizacionPresuDctoOdonDAO#guardarAutorizacionPresuDctoOdon(java.lang.Object)
	 */
	@Override
	public boolean guardarAutorizacionPresuDctoOdon(AutorizacionPresuDctoOdon autorizacionPresuDctoOdon) {
	
		return autorizacionPresuDctoOdonDelegate.guardarAutorizacionPresuDctoOdon(autorizacionPresuDctoOdon);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IAutorizacionPresuDctoOdonDAO#eliminarAutorizacionPresuDctoOdon(long)
	 */
	@Override
	public boolean eliminarAutorizacionPresuDctoOdon(long codigoAutorizacionPresuDctoOdon) {
		
		return autorizacionPresuDctoOdonDelegate.eliminarAutorizacionPresuDctoOdon(codigoAutorizacionPresuDctoOdon);
	}

}
