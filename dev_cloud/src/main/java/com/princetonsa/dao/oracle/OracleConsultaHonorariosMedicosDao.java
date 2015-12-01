/*
 * Creado el 28/12/2005
 * Jorge Armando OSorio Velasquez	
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.ConsultaHonorariosMedicosDao;
import com.princetonsa.dao.sqlbase.SqlBaseConsultaHonorariosMedicosDao;

/**
 * 
 * @author Jorge Armando Osorio Velasquez
 * 
 * CopyRight Princeton S.A.
 * 28/12/2005
 */
public class OracleConsultaHonorariosMedicosDao implements ConsultaHonorariosMedicosDao
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
