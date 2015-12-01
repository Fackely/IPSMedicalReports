/*
 * @(#)OracleCuposExtraDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import com.princetonsa.dao.CuposExtraDao;
import com.princetonsa.dao.sqlbase.SqlBaseCuposExtrasDao;


/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 09 /May/ 2006
 */
public class OracleCuposExtraDao implements CuposExtraDao 
{

	/**
	 * Cadena con el statement necesario para insertar los cupos extra modificados
	 */
	private static final String insertarCuposExtraStr=" INSERT INTO cupos_extra " +
													  " (codigo, " +
													  " fecha_proceso," +
													  " hora_proceso, "+
													  " codigo_agenda, "+
													  " cupos_disponibles, "+
													  " cupos_extra," +
													  " usuario) " +
													  " VALUES (seq_cupos_extra.nextval, ? , ? , ? , ? , ?, ? )";
 	
	
	/**
	 * M�todo para insertar
	 * @param con
	 * @param mapaCuposExtra
	 * @param usuario
	 * @param cadenaInsertar
	 * @return
	 */
	public int insertarCuposExtra(Connection con, HashMap mapaCuposExtra, String usuario) throws SQLException
	{
		return SqlBaseCuposExtrasDao.insertarCuposExtra(con, mapaCuposExtra, usuario, insertarCuposExtraStr);
	}
	
	/**
	 * M�todo para consultar las agendas disponibles segun unos parametros
	 * de b�squeda especificados
	 * @param con
	 * @param fechaIncial
	 * @param fechaFinal
	 * @param codigoUnidadConsulta
	 * @param codigoConsultorio
	 * @param codigoDiaSemana
	 * @param codigoMedico
	 * @return
	 */
	public HashMap busquedaAgendaGenerada (Connection con, String fechaIncial, String fechaFinal, int codigoUnidadConsulta, int codigoConsultorio, int codigoDiaSemana, int codigoMedico, String minutosEspera, int centroAtencion, String centrosAtencion, String unidadesAgenda) throws SQLException
	{
		return SqlBaseCuposExtrasDao.busquedaAgendaGenerada(con, fechaIncial, fechaFinal, codigoUnidadConsulta, codigoConsultorio, codigoDiaSemana, codigoMedico, minutosEspera, centroAtencion, centrosAtencion, unidadesAgenda);
	}
	
	/**
	 * M�todo para actualizar el numero de cupos de una agenda determinada
	 * @param con
	 * @param numeroCupos
	 * @param codigoAgenda
	 * @return
	 */
	public int actualizarCuposEnAgenda(Connection con , int numeroCupos, int codigoAgenda)throws SQLException
	{
		return SqlBaseCuposExtrasDao.actualizarCuposEnAgenda(con, numeroCupos, codigoAgenda);
	}
	
	
	
}
