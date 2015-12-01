
package com.servinte.axioma.mundo.interfaz.odontologia.presupuesto;

import java.math.BigDecimal;

import com.servinte.axioma.orm.IncluServicioConvenio;

/**
 * 
 * Interface que define las operaciones
 * para gestionar lo relacionado con el objeto
 * 
 * {@link IncluServicioConvenio}
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */
public interface IIncluServicioConvenioMundo {

	
	/**
	 * Método que permite actualizar el porcentaje del descuento
	 * odontológico cuando ha sido autorizado, en los servicios asociados a 
	 * la contratación de la inclusión 
	 * 
	 * @param codigoIncluPresuEncabezado
	 * @param porcentajeDescuentoOdontologico 
	 * @return
	 */
	public boolean actualizarPorcentajeDctoOdontologico (long codigoIncluPresuEncabezado, BigDecimal porcentajeDescuentoOdontologico);
}
