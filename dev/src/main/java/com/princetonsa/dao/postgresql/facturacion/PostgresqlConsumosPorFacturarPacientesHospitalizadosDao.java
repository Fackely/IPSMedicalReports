package com.princetonsa.dao.postgresql.facturacion;

/**
 * @author Juan Alejandro Cardona
 * Fecha: Septiembre 2008
 */


import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.ConsumosPorFacturarPacientesHospitalizadosDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseConsumosPorFacturarPacientesHospitalizadosDao;

public class PostgresqlConsumosPorFacturarPacientesHospitalizadosDao implements	ConsumosPorFacturarPacientesHospitalizadosDao 
{
	/**
	 * 
	 */
	public HashMap generarArchivoPlano(Connection con, HashMap criterios)
    {
        return SqlBaseConsumosPorFacturarPacientesHospitalizadosDao.generarArchivoPlano(con, criterios, DaoFactory.POSTGRESQL);
    }
	
	/**
	 * 
	 */
	public HashMap consultarCondicionesConsumosPacientesHospitalizados(Connection con, HashMap criterios)
    {
        return SqlBaseConsumosPorFacturarPacientesHospitalizadosDao.consultarCondicionesConsumosPacientesHospitalizados(con, criterios, DaoFactory.POSTGRESQL);
    }
	
}