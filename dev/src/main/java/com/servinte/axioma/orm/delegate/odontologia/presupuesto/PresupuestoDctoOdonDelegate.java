/**
 * 
 */
package com.servinte.axioma.orm.delegate.odontologia.presupuesto;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.orm.PresupuestoDctoOdon;
import com.servinte.axioma.orm.PresupuestoDctoOdonHome;

/**
 * @author Juan David Ramírez
 * @since Jan 14, 2011
 */
public class PresupuestoDctoOdonDelegate extends PresupuestoDctoOdonHome
{
	
	/**
	 * 
	 * @param codigoPresupuestoDctoOdon
	 * @return
	 */
	public boolean actualizarPresupuestoDctoOdon (long codigoPresupuestoDctoOdon){
		
		boolean resultado = false;
		
		try {
			
			PresupuestoDctoOdon presupuestoDctoOdon = super.findById(codigoPresupuestoDctoOdon);
			
			if(presupuestoDctoOdon!=null){
				
				presupuestoDctoOdon.setAutorizacionPresuDctoOdon(null);
				
				super.merge(presupuestoDctoOdon);
				
				resultado = true;
			}
			
			return resultado;
			
		}catch (Exception e) {
		
			Log4JManager.error("Error relacionado con el detalle de la autorización del descuento odontológico, en presupuesto_dcto_odon", e);
			return resultado;
		}
		
	}
}
