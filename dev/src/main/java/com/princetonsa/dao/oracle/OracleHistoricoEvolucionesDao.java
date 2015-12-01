/*
 * @(#)OracleHistoricoEvolucionesDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.dao.oracle;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import com.princetonsa.dao.HistoricoEvolucionesDao;
import com.princetonsa.dao.sqlbase.SqlBaseHistoricoEvolucionesDao;

/**
 * Esta clase implementa <code>HistoricoEvolucionesDao</code>
 * y proporciona acceso a BD para <code>HistoricoEvoluciones</code>.
 *
 * @version Jun 10, 2003
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
 */

public class OracleHistoricoEvolucionesDao implements HistoricoEvolucionesDao 
{
	
	/**
	 * Implementación de existe historico evoluciones dada la cuenta
	 * para BD Oracle
	 * 
	 * @see com.princetonsa.dao.HistoricoEvolucionesDao#existeHistoricoEvoluciones(Connection, int)
	 */	
	public boolean existeHistoricoEvoluciones(Connection con, int idCuenta) 
	{
		return SqlBaseHistoricoEvolucionesDao.existeHistoricoEvoluciones(con, idCuenta);
	}

	/**
	 * Implementación de cargar historico evoluciones dada la cuenta
	 * para BD Oracle
	 * 
	 * @see com.princetonsa.dao.HistoricoEvolucionesDao#cargar(Connection, int)
	 */
	public ResultSetDecorator cargar(Connection con, int idCuenta) throws SQLException 
	{
		return SqlBaseHistoricoEvolucionesDao.cargar(con, idCuenta) ;
	}
	
	/**
	 * Implementación de cargar historico evoluciones dado el paciente
	 * para BD Oracle
	 * 
	 * @see com.princetonsa.dao.HistoricoEvolucionesDao#cargar(Connection, String , String , int ) throws SQLException
	 */
	public ResultSetDecorator cargar(Connection con, String codigoTipoIdentificacionPaciente, String numeroIdentificacionPaciente, int codigoTipoEvolucion) throws SQLException
	{
		return SqlBaseHistoricoEvolucionesDao.cargar(con, codigoTipoIdentificacionPaciente, numeroIdentificacionPaciente, codigoTipoEvolucion) ;
	}
	
	/**
	 * Implementación de cargar historico evoluciones dado el paciente y
	 * el tipo de evoluciones a cargar para BD Oracle
	 * 
	 * @see com.princetonsa.dao.HistoricoEvolucionesDao#cargar(Connection, String , String) throws SQLException
	 */
	public ResultSetDecorator cargar(Connection con, String codigoTipoIdentificacionPaciente, String numeroIdentificacionPaciente) throws SQLException
	{
		return SqlBaseHistoricoEvolucionesDao.cargar(con, codigoTipoIdentificacionPaciente, numeroIdentificacionPaciente);
	}
	/**
	 * Implementación de cargar historico evoluciones dada la cuenta
	 * y su cuenta asociada para BD Oracle
	 * 
	 * @see com.princetonsa.dao.HistoricoEvolucionesDao#cargar(Connection, int, int)
	 */
	public ResultSetDecorator cargarEvolucionesCuentaYAsocio(Connection con, int idCuenta, int idCuentaAsociada) throws SQLException 
	{
		return SqlBaseHistoricoEvolucionesDao.cargarEvolucionesCuentaYAsocio(con, idCuenta, idCuentaAsociada) ;
	}

}