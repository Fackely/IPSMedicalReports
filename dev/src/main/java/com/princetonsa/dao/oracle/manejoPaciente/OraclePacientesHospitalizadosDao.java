package com.princetonsa.dao.oracle.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.manejoPaciente.PacientesHospitalizadosDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBasePacientesHospitalizadosDao;

/**
 * @author Mauricio Jllo
 * Fecha: Mayo de 2008
 */

public class OraclePacientesHospitalizadosDao implements PacientesHospitalizadosDao
{

	/**
	 * 
	 */
	public HashMap consultarPacientesHospitalizados(Connection con, HashMap vo)
    {
        return SqlBasePacientesHospitalizadosDao.consultarPacientesHospitalizados(con, vo);
    }
	
	/**
	 * 
	 */
	public HashMap obtenerCentrosCosto(Connection con, int institucion, String tipoArea, String tipoPaciente, String codigoCentroAtencion)
    {
        return SqlBasePacientesHospitalizadosDao.obtenerCentrosCosto(con, institucion, tipoArea, tipoPaciente, codigoCentroAtencion);
    }
	
}
