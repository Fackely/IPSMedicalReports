
package com.servinte.axioma.mundo.interfaz.odontologia.presupuesto;

import com.servinte.axioma.mundo.impl.odontologia.presupuesto.AutorizacionPresuDctoOdonMundo;
import com.servinte.axioma.orm.AutorizacionPresuDctoOdon;


/**
 * 
 * Interface que define las operaciones para gestionar lo relacionado con la entidad
 * {@link AutorizacionPresuDctoOdon}
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see AutorizacionPresuDctoOdonMundo
 */
public interface IAutorizacionPresuDctoOdonMundo {

	/**
	 * M�todo que permite realizar el registro del detalle de 
	 * la autorizaci�n de una solicitud de descuento odont�logica
	 * 
	 * @param autorizacionPresuDctoOdon
	 * @param tipoDescuento tipo del descuento realizado, si es por Valor del presupuesto o por Atenci�n
	 * @param codigoDescuento c�digo del descuento asociado
	 * 
	 * @return true indicando si se ha registrado correctamente, false de lo contrario
	 */
	public boolean guardarAutorizacionPresuDctoOdon (AutorizacionPresuDctoOdon autorizacionPresuDctoOdon, long codigoDescuento, String tipoDescuento);
	
	
	
	/**
	 * M�todo que permite eliminar un detalle para una solicitud de descuento que ha sido autorizada
	 * 
	 * @param codigoAutorizacionPresuDctoOdon
	 * @return true indicando si se ha eliminado el registro correctamente, false de lo contrario.
	 */
	public boolean eliminarAutorizacionPresuDctoOdon (long codigoAutorizacionPresuDctoOdon);
}
