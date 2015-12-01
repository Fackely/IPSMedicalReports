/*
 * Creado el 28/12/2005
 * Jorge Armando Osorio Velasquez
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.ConsultaHonorariosMedicosDao;
import com.princetonsa.dao.sqlbase.SqlBaseConsultaHonorariosMedicosDao;

public class PostgresqlConsultaHonorariosMedicosDao implements ConsultaHonorariosMedicosDao
{
	/**
	 * Metodo que consulta los profesionales de la salud.
	 * @param con
	 * @param vo
	 * @return
	 */
	public HashMap consultarProfesionalesSalud(Connection con, HashMap vo)
	{
		return SqlBaseConsultaHonorariosMedicosDao.consultarProfesionalesSalud(con,vo);
	}

    
    /**
     * 
     * @param con
     * @param institucion
     * @param profesional
     * @param restricciones 
     * @return
     */
    public HashMap consultarHonorariosProfesional(Connection con, int institucion, int profesional, HashMap restricciones)
    {
        return SqlBaseConsultaHonorariosMedicosDao.consultarHonorariosProfesional(con,institucion,profesional,restricciones);
    }
}
