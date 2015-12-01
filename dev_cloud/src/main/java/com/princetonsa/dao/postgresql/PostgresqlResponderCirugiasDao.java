/*
 * @(#)PostgresqlResponderCirugiasDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import com.princetonsa.dao.ResponderCirugiasDao;
import com.princetonsa.dao.sqlbase.SqlBaseResponderCirugiasDao;

/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 02 /Nov/ 2005
 */
public class PostgresqlResponderCirugiasDao implements ResponderCirugiasDao 
{
	/**
	 * Método que carga las peticiones de un paciente cargado en session
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public ResultSetDecorator cargarPeticionesPaciente(Connection con, int codigoPaciente, int institucionPaciente, int idIngreso, int consecutivoOrdenesMedicas)  throws SQLException
	{
		return SqlBaseResponderCirugiasDao.cargarPeticionesPaciente(con, codigoPaciente, institucionPaciente, idIngreso,consecutivoOrdenesMedicas);
	}
	
	/**
	 * Método para consultar las peticiones asociadas al usuario que ingreso en el sistema
	 * @param con
	 * @param usuario
	 * @param institucionUsuario
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarPeticionesMedico(Connection con, String usuario, int institucionUsuario, int centroCosto, int centroAtencion)  throws SQLException
	{
		return SqlBaseResponderCirugiasDao.cargarPeticionesMedico(con, usuario, institucionUsuario, centroCosto, centroAtencion);
	}
	
	/**
	 * Método para cargar los servicios de la peticion
	 * @param con
	 * @param codigoPeticion
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarServiciosPeticion(Connection con, int codigoPeticion)  throws SQLException
	{
		return SqlBaseResponderCirugiasDao.cargarServiciosPeticion(con, codigoPeticion);
	}
	
	
	/**
	 * Método que carga las peticiones de un paciente cargado en session
	 * que posean peticion
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public ResultSetDecorator cargarPeticionesPacienteConOrden(Connection con, int codigoPaciente, int institucionPaciente, int idCuenta, int consecutivoOrdenesMedicas)  throws SQLException
	{
		return SqlBaseResponderCirugiasDao.cargarPeticionesPacienteConOrden(con,codigoPaciente,institucionPaciente,idCuenta,consecutivoOrdenesMedicas); 
	}	
}