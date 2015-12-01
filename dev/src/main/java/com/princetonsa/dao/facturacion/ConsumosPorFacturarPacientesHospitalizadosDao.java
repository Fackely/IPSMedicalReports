package com.princetonsa.dao.facturacion;

/**
 * @author Juan Alejandro Cardona
 * Fecha: Septiembre de 2008
 */

import java.sql.Connection;
import java.util.HashMap;

public interface ConsumosPorFacturarPacientesHospitalizadosDao {

	/**
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap generarArchivoPlano(Connection con, HashMap vo);

	/**
	 * @param con
	 * @param criterios
	 * @return
	 */
	public abstract HashMap<String, Object> consultarCondicionesConsumosPacientesHospitalizados(Connection con, HashMap<String, Object> criterios);
	
}