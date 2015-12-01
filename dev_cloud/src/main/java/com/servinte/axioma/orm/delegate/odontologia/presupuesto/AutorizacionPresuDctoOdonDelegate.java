
package com.servinte.axioma.orm.delegate.odontologia.presupuesto;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.orm.AutorizacionPresuDctoOdon;
import com.servinte.axioma.orm.AutorizacionPresuDctoOdonHome;

/**
 * Clase que se encarga de manejar las transaccciones relacionadas
 * con el objeto {@link AutorizacionPresuDctoOdon}.
 * 
 * @author Jorge Armando Agudelo Quintero
 */
public class AutorizacionPresuDctoOdonDelegate extends AutorizacionPresuDctoOdonHome{

	
	/**
	 * Método que permite realizar el registro del detalle de 
	 * la autorización de una solicitud de descuento odontólogica
	 * 
	 * @param autorizacionPresuDctoOdon
	 * @return true indicando si se ha registrado correctamente, false de lo contrario.
	 */
	public boolean guardarAutorizacionPresuDctoOdon (AutorizacionPresuDctoOdon autorizacionPresuDctoOdon){
		
		try {
			
			super.attachDirty(autorizacionPresuDctoOdon);
			
			return true;
			
		} catch (Exception e) {
		
			return false;
		}
	}
	
	
	/**
	 * Método que permite eliminar un detalle para una solicitud de descuento que ha sido autorizada
	 * 
	 * @param codigoAutorizacionPresuDctoOdon
	 * @return true indicando si se ha eliminado el registro correctamente, false de lo contrario.
	 */
	public boolean eliminarAutorizacionPresuDctoOdon (long codigoAutorizacionPresuDctoOdon){
		
		boolean resultado = false;
		
		try {
			
			AutorizacionPresuDctoOdon autorizacionPresuDctoOdon = super.findById(codigoAutorizacionPresuDctoOdon);
			
			if(autorizacionPresuDctoOdon!=null){
				
				//autorizacionPresuDctoOdon.getPresupuestoDctoOdon().setAutorizacionPresuDctoOdon(null);
				
				PresupuestoDctoOdonDelegate presupuestoDctoOdonDelegate = new PresupuestoDctoOdonDelegate();
				
				presupuestoDctoOdonDelegate.actualizarPresupuestoDctoOdon(autorizacionPresuDctoOdon.getPresupuestoDctoOdon().getCodigoPk());
				
				super.delete(autorizacionPresuDctoOdon);
				
				resultado = true;
			}
			
			return resultado;
			
		}catch (Exception e) {
		
			Log4JManager.error("Error relacionado con el detalle de la autorización del descuento odontológico", e);
			return resultado;
		}
	}
	
}
