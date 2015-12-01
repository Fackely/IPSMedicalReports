package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.ConsultarAdmisionDao;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class ConsultarAdmision {
	
	/**
	 * Objeto para manejar la conexion a la base de datos
	 */
	ConsultarAdmisionDao objetoDao;
	
	/**
	 * 
	 * @param tipoBD
	 */
	private void init(String tipoBD) 
	{
		if(objetoDao==null)
		{
			objetoDao=DaoFactory.getDaoFactory(tipoBD).getConsultarAdmisionDao();
		}
	}
	
	public ConsultarAdmision()
	{
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Consultar entidad responsable
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public HashMap<String, Object> consultarEntidadResponsable(Connection con,int codigoIngreso,int codCuenta)
	{
		return objetoDao.consultarEntidadResponsable(con, codigoIngreso, codCuenta);
	}
	
	/**
	 * Consulta las cuentas del paciente
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public HashMap<String, Object> consultarIngresoCuentaPaciente(Connection con, int codigoPersona)
	{
		return objetoDao.consultarIngresoCuentaPaciente(con, codigoPersona);
	}
	
	
	/**
	 *Consultar informacion del paciente 
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public HashMap<String, Object> consultarPaciente(Connection con,int codigoPersona)
	{
		return objetoDao.consultarPaciente(con, codigoPersona);
	}
	
	/**
	 * Consultar informacion del responsable del paciente
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public HashMap<String, Object> consultarResponsablePaciente(Connection con,	int cuenta)
	{
		return objetoDao.consultarResponsablePaciente(con, cuenta);
	}
	
	
}
