package com.princetonsa.dao.oracle.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.ConsumosFacturadosDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseConsumosFacturadosDao;

/**
 * @author Mauricio Jllo.
 * Fecha: Mayo de 2008
 */

public class OracleConsumosFacturadosDao implements ConsumosFacturadosDao
{

	/**
	 * 
	 */
	public HashMap generarArchivoPlano(Connection con, HashMap vo)
    {
        return SqlBaseConsumosFacturadosDao.generarArchivoPlano(con, vo, DaoFactory.ORACLE);
    }

	@Override
	public String obtenerCondiciones(String codigoCentroAtencion,
			String fechaInicial, String fechaFinal,
			String convenioSeleccionado, String montoBaseInicial,
			String montoBaseFinal, String tope) {
		return SqlBaseConsumosFacturadosDao.obtenerCondiciones(codigoCentroAtencion, fechaInicial, fechaFinal, convenioSeleccionado, montoBaseInicial, montoBaseFinal, tope);
	}
	
}
