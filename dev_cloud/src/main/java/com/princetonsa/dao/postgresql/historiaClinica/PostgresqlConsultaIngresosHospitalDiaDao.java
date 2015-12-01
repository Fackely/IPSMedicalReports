/**
 * 
 */
package com.princetonsa.dao.postgresql.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.historiaClinica.ConsultaIngresosHospitalDiaDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseConsultaIngresosHospitalDiaDao;

/**
 * @author axioma
 *
 */
public class PostgresqlConsultaIngresosHospitalDiaDao implements ConsultaIngresosHospitalDiaDao 
{

	/**
	 * 
	 */
	public HashMap<String, Object> consultarIngresosHospitalDia(Connection con,Object atributos,boolean porPaciente)
	{
		return SqlBaseConsultaIngresosHospitalDiaDao.consultarIngresosHospitalDia(con,atributos,porPaciente);
	}

	/**
	 * 
	 */
	public HashMap<String, Object> consultaDetalleIngreso(Connection con, int ingreso, int cuenta) 
	{
		return SqlBaseConsultaIngresosHospitalDiaDao.consultaDetalleIngreso(con,ingreso,cuenta);
	}

}
