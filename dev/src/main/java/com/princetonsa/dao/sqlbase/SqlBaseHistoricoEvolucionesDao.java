/*
 * @(#)SqlBaseHistoricoEvolucionesDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */

package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import util.ConstantesBD;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a Histórico de Evoluciones
 *
 *	@version 1.0, Apr 14, 2004
 */
public class SqlBaseHistoricoEvolucionesDao 
{

	/**
	 * Cadena constante con el <i>statement</i> necesario para revisar hay historico de evoluciones
	 */
	private static final String existeHistoricoEvolucionesStr = "SELECT evol.codigo FROM solicitudes sol INNER JOIN evoluciones evol ON(evol.valoracion=sol.numero_solicitud) WHERE sol.cuenta =?";	

	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar todas las evoluciones asociadas a una cuenta.
	 */
	private static final String cargarEvolucionesCuentaStr = 	"SELECT evol.codigo, " +
																	"to_char(fecha_evolucion, 'YYYY-MM-DD') AS fechaEvolucion, " +
																	"hora_evolucion AS horaEvolucion, " +
																	"nombre AS centroCosto, " +
																	"primer_nombre || ' ' || segundo_nombre || ' ' || primer_apellido || ' ' || segundo_apellido AS nombreMedico " +
																"FROM " +
																	"solicitudes sol, " +
																	"evoluciones evol, " +
																	"personas per, " +
																	"centros_costo cc " +
																"WHERE  " +
																	"evol.valoracion = sol.numero_solicitud " +
																	"AND evol.codigo_medico = per.codigo  " +
																	"AND evol.centro_costo = cc.codigo " +
																	"AND sol.cuenta = ? " +
																"ORDER BY fecha_evolucion DESC, hora_evolucion DESC";

	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar todas las evoluciones asociadas a una cuenta y a la cuenta asociada.
	 */
	private static final String cargarEvolucionesCuentaYAsocioStr = "SELECT evol.codigo, " +
																		"to_char(fecha_evolucion, 'YYYY-MM-DD') AS fechaEvolucion, " +
																		"hora_evolucion AS horaEvolucion, " +
																		"nombre AS centroCosto, " +
																		"primer_nombre || ' ' || segundo_nombre || ' ' || primer_apellido || ' ' || segundo_apellido AS nombreMedico " +
																	"FROM " +
																		"solicitudes sol, " +
																		"evoluciones evol, " +
																		"personas per, " +
																		"centros_costo cc " +
																	"WHERE  " +
																		"evol.valoracion = sol.numero_solicitud " +
																		"AND evol.codigo_medico = per.codigo  " +
																		"AND evol.centro_costo = cc.codigo " +
																		"AND (sol.cuenta = ? or sol.cuenta = ? ) " +
																	"ORDER BY " +
																		"fecha_evolucion DESC, " +
																		"hora_evolucion DESC";

	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar todas las evoluciones asociadas a un paciente de un tipo de evolución particular (Hosp, urgencias).
	 */
	private static final String cargarEvolucionesPacienteDadoTipoEvolucionStr=	"select ev.codigo, " +
																					"to_char(ev.fecha_evolucion, 'YYYY-MM-DD') AS fechaEvolucion, " +
																					"ev.hora_evolucion AS horaEvolucion, " +
																					"ccosto.nombre AS centroCosto, " +
																					"per.primer_nombre || ' ' || per.segundo_nombre || ' ' || per.primer_apellido || ' ' || per.segundo_apellido AS nombreMedico " +
																				"from " +
																					"cuentas cue, " +
																					"solicitudes sol, " +
																					"evoluciones ev, " +
																					"centros_costo ccosto, " +
																					"personas per, " +
																					"personas per2 " +
																				"where " +
																					"per2.codigo=cue.codigo_paciente " +
																					"and cue.id=sol.cuenta " +
																					"and ev.valoracion=sol.numero_solicitud " +
																					"and ccosto.codigo=ev.centro_costo " +
																					"and ev.codigo_medico=per.codigo " +
																					"and per2.tipo_identificacion=? " +
																					"and per2.numero_identificacion=? " +
																					"and ev.tipo_evolucion=? " +
																				"ORDER BY " +
																					"fecha_evolucion DESC, " +
																					"hora_evolucion DESC"; 

	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar todas las evoluciones asociadas a un paciente.
	 */
	private static final String cargarEvolucionesPacienteStr=	"select ev.codigo, " +
																	"to_char(ev.fecha_evolucion, 'YYYY-MM-DD') AS fechaEvolucion, " +
																	"ev.hora_evolucion AS horaEvolucion, " +
																	"ccosto.nombre AS centroCosto, " +
																	"per.primer_nombre || ' ' || per.segundo_nombre || ' ' || per.primer_apellido || ' ' || per.segundo_apellido AS nombreMedico " +
																"from " +
																	"cuentas cue, " +
																	"solicitudes sol, " +
																	"evoluciones ev, " +
																	"centros_costo ccosto, " +
																	"personas per, " +
																	"personas per2 " +
																"where " +
																	"per2.codigo=cue.codigo_paciente " +
																	"and cue.id=sol.cuenta " +
																	"and ev.valoracion=sol.numero_solicitud " +
																	"and ccosto.codigo=ev.centro_costo " +
																	"and ev.codigo_medico=per.codigo " +
																	"and per2.tipo_identificacion=? " +
																	"and per2.numero_identificacion=? " +
																"ORDER BY " +
																	"fecha_evolucion DESC, " +
																	"hora_evolucion DESC"; 

	/**
	 * Implementación de existe historico evoluciones dada la cuenta
	 * para BD Genérica
	 * 
	 * @see com.princetonsa.dao.HistoricoEvolucionesDao#existeHistoricoEvoluciones(Connection, int)
	 */	
	public static boolean existeHistoricoEvoluciones(Connection con, int idCuenta) 
	{
		try
		{
			PreparedStatementDecorator existe =  new PreparedStatementDecorator(con.prepareStatement(existeHistoricoEvolucionesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			existe.setInt(1, idCuenta);
			ResultSetDecorator rs = new ResultSetDecorator(existe.executeQuery());
			if (rs.next())
			{
				rs.close();
				return true;
			}
			else
			{
				rs.close();
				return false;
			}
		}
		catch(SQLException e)
		{
			return false;
		}
	}

	/**
	 * Implementación de cargar historico evoluciones dada la cuenta
	 * para BD Genérica
	 * 
	 * @see com.princetonsa.dao.HistoricoEvolucionesDao#cargar(Connection, int)
	 */
	public static ResultSetDecorator cargar(Connection con, int idCuenta) throws SQLException 
	{
		PreparedStatementDecorator cargar =  new PreparedStatementDecorator(con.prepareStatement(cargarEvolucionesCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cargar.setInt(1, idCuenta);
		return new ResultSetDecorator(cargar.executeQuery());
	}
	
	/**
	 * Implementación de cargar historico evoluciones dada la cuenta
	 * y su cuenta asociada para BD Genérica
	 * 
	 * @see com.princetonsa.dao.HistoricoEvolucionesDao#cargar(Connection, int, int)
	 */
	public static ResultSetDecorator cargarEvolucionesCuentaYAsocio(Connection con, int idCuenta, int idCuentaAsociada) throws SQLException 
	{
		PreparedStatementDecorator cargar =  new PreparedStatementDecorator(con.prepareStatement(cargarEvolucionesCuentaYAsocioStr ,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cargar.setInt(1, idCuenta);
		cargar.setInt(2, idCuentaAsociada);
		return new ResultSetDecorator(cargar.executeQuery());
	}
	
	/**
	 * Implementación de cargar historico evoluciones dado el paciente
	 * para BD Genérica
	 * 
	 * @see com.princetonsa.dao.HistoricoEvolucionesDao#cargar(Connection, String , String , int ) throws SQLException
	 */
	public static ResultSetDecorator cargar(Connection con, String codigoTipoIdentificacionPaciente, String numeroIdentificacionPaciente, int codigoTipoEvolucion) throws SQLException
	{
		PreparedStatementDecorator cargar =  new PreparedStatementDecorator(con.prepareStatement(cargarEvolucionesPacienteDadoTipoEvolucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cargar.setString(1, codigoTipoIdentificacionPaciente);
		cargar.setString(2, numeroIdentificacionPaciente);
		cargar.setInt(3, codigoTipoEvolucion);
		
		return new ResultSetDecorator(cargar.executeQuery());
	}
	
	/**
	 * Implementación de cargar historico evoluciones dado el paciente y
	 * el tipo de evoluciones a cargar para BD Genérica
	 * 
	 * @see com.princetonsa.dao.HistoricoEvolucionesDao#cargar(Connection, String , String) throws SQLException
	 */
	public static ResultSetDecorator cargar(Connection con, String codigoTipoIdentificacionPaciente, String numeroIdentificacionPaciente) throws SQLException
	{
		PreparedStatementDecorator cargar =  new PreparedStatementDecorator(con.prepareStatement(cargarEvolucionesPacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cargar.setString(1, codigoTipoIdentificacionPaciente);
		cargar.setString(2, numeroIdentificacionPaciente);

		return new ResultSetDecorator(cargar.executeQuery());
	}

}
