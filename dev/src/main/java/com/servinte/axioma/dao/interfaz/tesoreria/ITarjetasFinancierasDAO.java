package com.servinte.axioma.dao.interfaz.tesoreria;

import java.util.ArrayList;

import com.princetonsa.dto.tesoreria.DtoTarjetasFinancieras;

public interface ITarjetasFinancierasDAO {
	/**
	 * Listar todas las entidades financieras activas existentes en el sistema
	 * @author Juan David Ramírez
	 * @since 18 Septiembre 2010
	 * @return Lista con los tipos de tarjetas financieras activas en el sistema
	 */
	public ArrayList<DtoTarjetasFinancieras> listarTarjetasFinancieras();
}
