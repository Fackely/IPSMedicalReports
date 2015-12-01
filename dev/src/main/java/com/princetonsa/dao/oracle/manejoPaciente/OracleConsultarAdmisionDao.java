package com.princetonsa.dao.oracle.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.manejoPaciente.ConsultarAdmisionDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseConsultarAdmisionDao;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class OracleConsultarAdmisionDao implements ConsultarAdmisionDao 
{

	public HashMap<String, Object> consultarEntidadResponsable(Connection con,int codigoIngreso, int codCuenta) 
	{
		return SqlBaseConsultarAdmisionDao.consultarEntidadResponsable(con, codigoIngreso,codCuenta);
	}

	public HashMap<String, Object> consultarIngresoCuentaPaciente(Connection con, int codigoPersona) 
	{
		return SqlBaseConsultarAdmisionDao.consultarIngresoCuentaPaciente(con, codigoPersona);
	}

	public HashMap<String, Object> consultarPaciente(Connection con,int codigoPersona) 
	{
		return SqlBaseConsultarAdmisionDao.consultarPaciente(con, codigoPersona);
	}

	public HashMap<String, Object> consultarResponsablePaciente(Connection con,	int cuenta) 
	{
		return SqlBaseConsultarAdmisionDao.consultarResponsablePaciente(con, cuenta);
	}

}
