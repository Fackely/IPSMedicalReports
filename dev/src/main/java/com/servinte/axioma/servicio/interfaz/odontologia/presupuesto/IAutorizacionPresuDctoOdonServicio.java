
package com.servinte.axioma.servicio.interfaz.odontologia.presupuesto;

import com.servinte.axioma.orm.AutorizacionPresuDctoOdon;
import com.servinte.axioma.servicio.impl.odontologia.presupuesto.AutorizacionPresuDctoOdonServicio;



/**
 * Servicio que le delega al negocio las operaciones relacionados con la
 * entidad {@link AutorizacionPresuDctoOdon}
 * 
 * @author Jorge Armando Agudelo Quintero
 * 
 * @see AutorizacionPresuDctoOdonServicio
 */

public interface IAutorizacionPresuDctoOdonServicio {

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
	 * @return true indicando si se ha eliminado el registro correctamente, false de lo contrario
	 */
	public boolean eliminarAutorizacionPresuDctoOdon (long codigoAutorizacionPresuDctoOdon);
}
