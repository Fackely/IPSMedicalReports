
package com.servinte.axioma.dao.interfaz.odontologia.presupuesto;

import java.math.BigDecimal;

import com.servinte.axioma.orm.IncluServicioConvenio;

/**
 * 
 * Interfaz donde se define el comportamiento de los
 * DAO's para lo relacionado con los objetos {@link IncluServicioConvenio}
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */
public interface IIncluServicioConvenioDAO {

	
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
