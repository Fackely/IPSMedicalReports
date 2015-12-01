package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

public interface ConsultaIngresosHospitalDiaDao 
{

	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public abstract HashMap<String, Object> consultarIngresosHospitalDia(Connection con,Object atributos,boolean porPaciente);

	/**
	 * 
	 * @param con
	 * @param ingreso
	 * @param cuenta
	 * @return
	 */
	public abstract HashMap<String, Object> consultaDetalleIngreso(Connection con, int ingreso, int cuenta);

}
