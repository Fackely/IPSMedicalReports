package com.princetonsa.dao.oracle.facturacion;

/**
 * @author Juan Alejandro Cardona
 * Fecha: Mayo de 2008
 */


import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.ConsumosPorFacturarPacientesHospitalizadosDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseConsumosPorFacturarPacientesHospitalizadosDao;


public class OracleConsumosPorFacturarPacientesHospitalizadosDao implements ConsumosPorFacturarPacientesHospitalizadosDao 
{

	/**
	 * 
	 */
	public HashMap generarArchivoPlano(Connection con, HashMap criterios)
    {
        return SqlBaseConsumosPorFacturarPacientesHospitalizadosDao.generarArchivoPlano(con, criterios, DaoFactory.ORACLE);
    }
	
	/**
	 * 
	 */
	public HashMap consultarCondicionesConsumosPacientesHospitalizados(Connection con, HashMap criterios)
    {
        return SqlBaseConsumosPorFacturarPacientesHospitalizadosDao.consultarCondicionesConsumosPacientesHospitalizados(con, criterios, DaoFactory.ORACLE);
    }

}