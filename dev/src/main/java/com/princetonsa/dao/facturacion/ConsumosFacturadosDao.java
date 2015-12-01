package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Mauricio Jllo.
 * Fecha: Mayo de 2008
 */

public interface ConsumosFacturadosDao
{

	/**
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap generarArchivoPlano(Connection con, HashMap vo);

	/**
	 * 
	 * @param codigoCentroAtencion
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param convenioSeleccionado
	 * @param montoBaseInicial
	 * @param montoBaseFinal
	 * @param tope
	 * @return
	 */
	public abstract String obtenerCondiciones(String codigoCentroAtencion,
			String fechaInicial, String fechaFinal,
			String convenioSeleccionado, String montoBaseInicial,
			String montoBaseFinal, String tope);
	
}
