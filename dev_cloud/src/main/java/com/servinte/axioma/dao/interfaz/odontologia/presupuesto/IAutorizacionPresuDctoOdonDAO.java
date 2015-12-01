
package com.servinte.axioma.dao.interfaz.odontologia.presupuesto;

import com.servinte.axioma.dao.impl.odontologia.presupuesto.AutorizacionPresuDctoOdonHibernateDAO;
import com.servinte.axioma.orm.AutorizacionPresuDctoOdon;


/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de {@link AutorizacionPresuDctoOdon}
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see AutorizacionPresuDctoOdonHibernateDAO
 */
public interface IAutorizacionPresuDctoOdonDAO {

	
	/**
	 * M�todo que permite realizar el registro del detalle de 
	 * la autorizaci�n de una solicitud de descuento odont�logica
	 * 
	 * @param autorizacionPresuDctoOdon
	 * @return true indicando si se ha registrado correctamente, false de lo contrario
	 */
	public boolean guardarAutorizacionPresuDctoOdon (AutorizacionPresuDctoOdon autorizacionPresuDctoOdon);
	

	/**
	 * M�todo que permite eliminar un detalle para una solicitud de descuento que ha sido autorizada
	 * 
	 * @param codigoAutorizacionPresuDctoOdon
	 * @return true indicando si se ha eliminado el registro correctamente, false de lo contrario
	 */
	public boolean eliminarAutorizacionPresuDctoOdon (long codigoAutorizacionPresuDctoOdon);
	
}
