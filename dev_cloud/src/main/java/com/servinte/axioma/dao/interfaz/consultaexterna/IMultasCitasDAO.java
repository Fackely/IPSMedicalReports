
package com.servinte.axioma.dao.interfaz.consultaexterna;

import com.servinte.axioma.orm.MultasCitas;

/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de {@link MultasCitas}
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */

public interface IMultasCitasDAO {

	
	/**
	 * Método que se encarga de buscar un registro de una
	 * Multa asociada a una Cita
	 * 
	 * @param codigoPk
	 * @return
	 */
	public MultasCitas obtenerMultaCitaPorCodigo(long codigoPk);
}
