
package com.servinte.axioma.orm.delegate.consultaexterna;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.orm.MultasCitas;
import com.servinte.axioma.orm.MultasCitasHome;

/**
 * 
 * Esta clase se encarga de manejar las transaccciones relacionadas
 * con el objeto {@link MultasCitas}
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */

public class MultasCitasDelegate extends MultasCitasHome {

	
	/**
	 * Método que se encarga de buscar un registro de una
	 * Multa asociada a una Cita
	 * 
	 * @param codigoPk
	 * @return
	 */
	public MultasCitas obtenerMultaCitaPorCodigo(long codigoPk) {
		
		try {
			
			return super.findById(codigoPk);
			
		} catch (Exception e) {
			
			Log4JManager.info("Ocurrio un error en la búsqueda de la multa" + e);
			return null;
		}
	}
	
}
