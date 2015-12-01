package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public interface ConsultarAdmisionDao {
	 
	/**
	 * Consultar Ingreso cuenta del paciente
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap<String, Object> consultarIngresoCuentaPaciente(Connection con, int codigoPersona);
	
	/**
	 * Consultar paciente
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public abstract HashMap<String, Object> consultarPaciente(Connection con, int codigoPersona);
	
	/**
	 * Consulta Entidades responsables por paciente
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public abstract HashMap<String, Object> consultarEntidadResponsable(Connection con, int codigoIngreso, int codCuenta);
	
	/**
	 * Consultar responsable paciente
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public abstract HashMap<String, Object> consultarResponsablePaciente(Connection con, int cuenta);

}
