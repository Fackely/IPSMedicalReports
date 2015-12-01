package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Collection;

import com.princetonsa.dao.CamasDao;
import com.princetonsa.dao.sqlbase.SqlBaseCamasDao;
	
/**
 * @author l-caball
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class PostgresqlCamasDao implements CamasDao
{
	

	public ResultSetDecorator listarCamas(Connection con,int centroAtencion) throws SQLException
	{
		return SqlBaseCamasDao.listarCamas(con,centroAtencion);
	}

	public ResultSetDecorator listarCamasCentroCosto(Connection con, int centroCosto) throws SQLException
	{
		return SqlBaseCamasDao.listarCamasCentroCosto(con, centroCosto);
	}
	
	

	
	/**
	 * Consultar los traslados de las camas
	 * @param con
	 * @param numeroCama
	 * @param fechaTraslado
	 * @param horaTraslado
	 * @param usuario
	 * @return
	 */
	public Collection consultaTrasladoCamas(	Connection con, 
																			String numeroCama, 
																			String fechaInicialTraslado, 
																			String fechaFinalTraslado,
																			String horaInicialTraslado, 
																			String horaFinalTraslado,
																			String usuario)
	{
			return SqlBaseCamasDao.busquedaConsultaTrasladoCamas(	 con, 
																									 numeroCama, 
																									 fechaInicialTraslado, 
																									 fechaFinalTraslado,
																									 horaInicialTraslado, 
																									 horaFinalTraslado,
																									 usuario);
	}

	/**
	 * Método que carga los links pertenecientes a la consulta de traslado 
	 * con búsqueda por  paciente
	 * @param con, conexión con la fuente de datos
	 * @param codigoPaciente, código del paciente cargado en la sesión
	 * @return
	 * @throws SQLException
	 */
	public Collection linksConsultaTrasladoCamasPorPaciente(Connection con, int codigoPaciente) throws SQLException
	{
		return SqlBaseCamasDao.linksConsultaTrasladoCamasPorPaciente(con,codigoPaciente);
	}
	
	/**
	 * Método que obtiene los datos pertinentes desde el link 
	 * see=linksConsultaTrasladoCamasPorPaciente para cargar 
	 * los datos de la consulta de traslado de camas
	 * @param con, conexión con la fuente de datos
	 * @param cuenta, cuenta del paciente
	 * @return
	 */
	public Collection busquedaConsultaTrasladoCamasPorPaciente (	Connection con,	int cuenta ) throws SQLException
	{ 
		return SqlBaseCamasDao.busquedaConsultaTrasladoCamasPorPaciente(con,cuenta);																												
	}


}
