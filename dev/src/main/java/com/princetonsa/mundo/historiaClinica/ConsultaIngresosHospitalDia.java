/**
 * 
 */
package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.ConsultaIngresosHospitalDiaDao;

/**
 * @author axioma
 *
 */
public class ConsultaIngresosHospitalDia 
{
	
	private ConsultaIngresosHospitalDiaDao objetoDao;
	
	/**
	 * 
	 *
	 */
	public ConsultaIngresosHospitalDia()
	{
		this.init(System.getProperty("TIPOBD"));
	}

	/**
	 * 
	 * @param tipoBD
	 */
	private void init(String tipoBD) 
	{
		objetoDao=DaoFactory.getDaoFactory(tipoBD).getConsultaIngresosHospitalDiaDao();
	}

	/**
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public HashMap<String, Object> consultarIngresosHospitalDia(Connection con,Object atributos,boolean porPaciente) 
	{
		return objetoDao.consultarIngresosHospitalDia(con,atributos,porPaciente);
	}

	/**
	 * 
	 * @param con
	 * @param ingreso
	 * @param cuenta
	 * @return
	 */
	public HashMap<String, Object> consultaDetalleIngreso(Connection con, int ingreso, int cuenta) 
	{
		return objetoDao.consultaDetalleIngreso(con,ingreso,cuenta);
	}

}
