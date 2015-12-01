
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
	 * M�todo que permite actualizar el porcentaje del descuento
	 * odontol�gico cuando ha sido autorizado, en los servicios asociados a 
	 * la contrataci�n de la inclusi�n 
	 * 
	 * @param codigoIncluPresuEncabezado
	 * @param porcentajeDescuentoOdontologico 
	 * @return
	 */
	public boolean actualizarPorcentajeDctoOdontologico (long codigoIncluPresuEncabezado, BigDecimal porcentajeDescuentoOdontologico);
}
