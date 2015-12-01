
package com.servinte.axioma.mundo.interfaz.consultaexterna;

import com.servinte.axioma.orm.MultasCitas;

/**
 * Define la lógica de negocio relacionada con las funcionalidades 
 * de las Multas asociadas a las citas
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */

public interface IMultasCitasMundo {

	
	/**
	 * Método que se encarga de buscar un registro de una
	 * Multa asociada a una Cita
	 * 
	 * @param codigoPk
	 * @return
	 */
	public MultasCitas obtenerMultaCitaPorCodigo(long codigoPk);
}
