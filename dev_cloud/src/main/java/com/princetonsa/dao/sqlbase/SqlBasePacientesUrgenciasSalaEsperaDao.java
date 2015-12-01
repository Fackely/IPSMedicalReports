/*
 * @(#)SqlBasePacientesUrgenciasSalaEsperaDao.java
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ValoresPorDefecto;

/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 * para la los pacientes de urgencias con conducta a seguir Sala de Espera
 *
 * @version 1.0, Julio 24 / 2006
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 */
public class SqlBasePacientesUrgenciasSalaEsperaDao 
{
	/**	* Objeto para manejar los logs de esta clase	*/
	private static Logger logger = Logger.getLogger(SqlBasePacientesUrgenciasPorValorarDao.class);
	
			
//	" AND cue.estado_cuenta IN (" +ConstantesBD.codigoEstadoCuentaActiva + ", " + ConstantesBD.codigoEstadoCuentaAsociada +") " +
	

	
	
	/**
	 * Método para consultar los todos los datos de los pacientes de via de ingreso de
	 * urgencias con conducta a seguir "Sala de Espera"
	 * @param con
	 * @param codigoCentroAtencion
	 * @param institucion
	 * @param consultarPacientesUrgSalaEsperaStr 
	 * @return
	 * @throws SQLException
	 */
	public static ResultSetDecorator consultarPacientesUrgSalaEspera(Connection con, int codigoCentroAtencion, int institucion, String consultarPacientesUrgSalaEsperaStr) throws SQLException
	{
		try
		{
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consultarPacientesUrgSalaEsperaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setInt(1, codigoCentroAtencion);
			cargarStatement.setInt(2, institucion);
			cargarStatement.setInt(3, codigoCentroAtencion);
			cargarStatement.setInt(4, institucion);

			logger.info("Centro Atencion: " + codigoCentroAtencion);
			logger.info("Institucion: " + institucion);
			logger.info("CONSULTA >>>> " + consultarPacientesUrgSalaEsperaStr);
			return new ResultSetDecorator(cargarStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en consultarPacientesUrgSalaEsperaStr [SqlBasePacientesUrgenciasSalaEsperaDao]: "+e.toString());
			return null;
		}
	}
	
	
	/**
	 * 
	 * Ordenamiento pro tiempo Salade espera 
	 * @param con
	 * @param codigoCentroAtencion
	 * @param institucion
	 * @param consultarPacientesUrgSalaEsperaStr 
	 * @return
	 * @throws SQLException
	 */
	public static ResultSetDecorator consultarPacientesUrgSalaEsperaOrdenamientoPorTiempoSalaEspera(Connection con, int codigoCentroAtencion, int institucion, String consultarPacientesUrgSalaEsperaStr,Integer tipoOrderBy) throws SQLException
	{
		try
		{
			String consulta = consultarPacientesUrgSalaEsperaStr;
			
			
			if(tipoOrderBy==1){
				consulta+=" order by 1 asc ";
			}else{
				consulta+=" order by 1 desc ";
			}
			
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setInt(1, codigoCentroAtencion);
			cargarStatement.setInt(2, institucion);
			cargarStatement.setInt(3, codigoCentroAtencion);
			cargarStatement.setInt(4, institucion);

			logger.info("Centro Atencion: " + codigoCentroAtencion);
			logger.info("Institucion: " + institucion);
			logger.info("CONSULTA >>>> " + consultarPacientesUrgSalaEsperaStr);
			return new ResultSetDecorator(cargarStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en consultarPacientesUrgSalaEsperaStr [SqlBasePacientesUrgenciasSalaEsperaDao]: "+e.toString());
			return null;
		}
	}
}
