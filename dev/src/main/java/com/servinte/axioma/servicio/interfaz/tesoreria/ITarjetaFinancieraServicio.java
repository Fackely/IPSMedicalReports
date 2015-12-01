package com.servinte.axioma.servicio.interfaz.tesoreria;

import java.util.ArrayList;

import com.princetonsa.dto.tesoreria.DtoTarjetasFinancieras;

/**
 * Interface para
 * @author Juan David Ramírez
 * @since 18 Septiembre 2010
 * @version 1.0.0
 */
public interface ITarjetaFinancieraServicio {

	/**
	 * Listar todas las entidades financieras activas existentes en el sistema
	 * @author Juan David Ramírez
	 * @since 18 Septiembre 2010
	 * @return Lista con los tipos de tarjetas financieras activas en el sistema
	 */
	public ArrayList<DtoTarjetasFinancieras> listarTarjetasFinancieras();
}
