
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
