package com.princetonsa.dao.oracle.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.manejoPaciente.ListadoCamasHospitalizacionDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseListadoCamasHospitalizacionDao;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.ListadoCamasHospitalizacion;

public class OracleListadoCamasHospitalizacionDao implements ListadoCamasHospitalizacionDao 
{
	/**
	 * 
	 * @param mundo
	 * @param oldQuery
	 * @param usuario
	 * @return
	 */
	public HashMap listadoCamasHospitalizacion(Connection con, ListadoCamasHospitalizacion mundo, String condiciones) 
   	{
		return SqlBaseListadoCamasHospitalizacionDao.listadoCamasHospitalizacion(con, mundo, condiciones);
	}
	
}
