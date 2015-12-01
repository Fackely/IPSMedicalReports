/*
 * @(#)PostgresqlAuxiliarDiagnosticosDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import com.princetonsa.dao.AuxiliarDiagnosticosDao;
import org.apache.log4j.Logger;

import util.ConstantesBD;

/**
 * Esta clase implementa el contrato estipulado en <code>AuxiliarDiagnosticosDao</code>, proporcionando los servicios
 * de acceso a una base de datos PostgreSQL requeridos por la clase <code>AuxiliarDiagnosticos</code>
 *
 * @version 1.0, Jun 16, 2003
 */

public class PostgresqlAuxiliarDiagnosticosDao implements AuxiliarDiagnosticosDao
{
	/**
	 * Objeto para manejar los problemas que se presenten en esta
	 * clase
	 */
	private Logger logger = Logger.getLogger(PostgresqlAuxiliarDiagnosticosDao.class);
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para saber 
	 * si existe una valoracion o no. Da el número de la solicitud si esta existe
	 * si no retorna -1
	 */
	private static final String existeValoracionStr="" +
	"select dx.num_sol as numeroSolicitud from (select MIN(sol.numero_solicitud) as num_sol from solicitudes sol, valoraciones val where sol.numero_solicitud=val.numero_solicitud and sol.cuenta=?) dx where dx.num_sol is not null";

	/**
	 * Cadena constante con el <i>statement</i> necesario para saber 
	 * si existe una evolucion o no. Da el número de la solicitud si esta existe
	 * si no retorna -1
	 */
	private static final String obtenerFechaYHoraUltimaEvolucionStr="SELECT evol.codigo as codigoUltimaEvolucion, evol.fecha_evolucion as fechaEvolucion, evol.hora_evolucion as horaEvolucion from evoluciones evol, solicitudes sol where  sol.cuenta=? and sol.numero_solicitud=evol.valoracion order by evol.fecha_evolucion desc, evol.hora_evolucion desc, codigoUltimaEvolucion desc ";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar 
	 * todos los diagnosticos para una valoración particular. 
	 */	
	private static final String cargarDiagnosticosValoracionStr="" +
	"SELECT vald.acronimo_diagnostico as acronimoDiagnostico, diag.nombre as diagnostico, vald.tipo_cie_diagnostico as tipoCieDiagnostico, vald.principal, vald.definitivo, vald.numero from val_diagnosticos vald, diagnosticos diag where vald.acronimo_diagnostico=diag.acronimo and vald.tipo_cie_diagnostico=diag.tipo_cie and vald.valoracion=? order by vald.numero";

	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar 
	 * todos los diagnosticos de valoraciones para una cuenta particular.
	 * Se usa un UNION con una consulta que nunca da resultados, para
	 * eliminar los repetidos
	 */	
	private static final String cargarDiagnosticosValoracion2Str="" +
	"SELECT vald.acronimo_diagnostico as acronimoDiagnostico, diag.nombre as diagnostico, vald.tipo_cie_diagnostico as tipoCieDiagnostico, vald.principal, vald.definitivo, vald.numero from val_diagnosticos vald, diagnosticos diag, solicitudes sol where vald.acronimo_diagnostico=diag.acronimo and vald.valoracion=sol.numero_solicitud and vald.tipo_cie_diagnostico=diag.tipo_cie and sol.cuenta=?  UNION select '1' as acronimoDiagnostico, '' as diagnostico, 9 as tipoCieDiagnostico, true as principal, true as definitivo, 1 as numero from tipos_personas where codigo=-1 order by numero ";

	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar 
	 * todos los diagnosticos para una evolución particular. 
	 */	
	private static final String cargarDiagnosticosEvolucionStr="" +
	"SELECT evold.acronimo_diagnostico as acronimoDiagnostico, diag.nombre as diagnostico, evold.tipo_cie_diagnostico as tipoCieDiagnostico, evold.principal, evold.definitivo, evold.numero from evol_diagnosticos evold, diagnosticos diag where evold.acronimo_diagnostico=diag.acronimo and evold.tipo_cie_diagnostico=diag.tipo_cie and evold.evolucion=?";

	/**
	 * Cadena constante con el <i>statement</i> exactamente igual al
	 *  anterior, pero sin manejar el 1 (Para que la unión no tenga problema)
	 */	
	private static final String cargarDiagnosticosEvolucion2Str="" +
	"SELECT evold.acronimo_diagnostico as acronimoDiagnostico, diag.nombre as diagnostico, evold.tipo_cie_diagnostico as tipoCieDiagnostico, evold.principal, evold.definitivo, 1 as numero from evol_diagnosticos evold, diagnosticos diag where evold.acronimo_diagnostico=diag.acronimo and evold.tipo_cie_diagnostico=diag.tipo_cie and evold.evolucion=? order by evold.evolucion";

	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar 
	 * todos el diagnostico de complicación de la última evolución. 
	 */	
	private static final String cargarDiagnosticoComplicacionStr="" +
	"SELECT diag.nombre, evol.diagnostico_complicacion as acronimoDiagnostico, evol.diagnostico_complicacion_cie as tipoCieDiagnostico from evoluciones evol, diagnosticos diag where evol.diagnostico_complicacion=diag.acronimo and evol.diagnostico_complicacion_cie=diag.tipo_cie and evol.codigo=( SELECT max(ev.codigo) from evoluciones ev, solicitudes sol where ev.valoracion=sol.numero_solicitud and sol.cuenta=? )";
	//"SELECT diag.nombre, evol.diagnostico_complicacion as acronimoDiagnostico, evol.diagnostico_complicacion_cie as tipoCieDiagnostico from evoluciones evol, diagnosticos diag where evol.diagnostico_complicacion=diag.acronimo and evol.diagnostico_complicacion_cie=diag.tipo_cie and evol.numero_solicitud=(SELECT max(numero_solicitud) from solicitudes_evolucion where id_cuenta=?)";

	private String fechaUltimaEvolucion;
	private String horaUltimaEvolucion;


	/**
	 * Dada una fecha y hora, busca todos los diagnosticos presuntivos
	 * de las valoraciones que tengan fecha y hora mayor a la especificada
	 * (Para buscar los diagnosticos de interconsulta) 
	 */
	private String cargarDiagnosticosEvolucionExtraStr= "SELECT valdiag.acronimo_diagnostico as acronimoDiagnostico, diag.nombre as diagnostico, valdiag.tipo_cie_diagnostico as tipoCieDiagnostico, valdiag.principal, valdiag.definitivo, 1 as numero from valoraciones val, solicitudes sol, val_diagnosticos valdiag, diagnosticos diag where val.numero_solicitud=sol.numero_solicitud and val.numero_solicitud=valdiag.valoracion and diag.tipo_cie=valdiag.tipo_cie_diagnostico and diag.acronimo=valdiag.acronimo_diagnostico and sol.cuenta =? and ( val.fecha_valoracion > ? or (val.fecha_valoracion = ? and val.hora_valoracion>=?) ) order by val.fecha_valoracion asc, val.hora_valoracion asc";

	/**
	 * Método privado que dado un número de cuenta me dice si existe
	 * al menos una valoración en una cuenta determinada. Si existe,
	 * retorna el número de la más reciente 
	 * 
	 * @param con Conexión con BD Postgresql
	 * @param numeroCuenta Número de cuenta con el que se quiere
	 * buscar la valoración más reciente
	 * @return
	 * @throws SQLException
	 */
	private int existeValoracion (Connection con, int numeroCuenta)  throws SQLException
	{
		PreparedStatementDecorator existeValoracionStatement= new PreparedStatementDecorator(con.prepareStatement(existeValoracionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
		existeValoracionStatement.setInt(1, numeroCuenta);
		
		ResultSetDecorator rs=new ResultSetDecorator(existeValoracionStatement.executeQuery());

		if (rs.next())
		{
			return rs.getInt("numeroSolicitud");
		}
		else
		{
			return -1;
		}
		
	}
	
	/**
	 * Método privado que dado un número de cuenta me dice si existe
	 * al menos una evolución en una cuenta determinada. Si existe,
	 * retorna el número de la más reciente (En términos de la fecha y
	 * hora de evolución)
	 * 
	 * @param con Conexión con BD Postgresql
	 * @param numeroCuenta Número de cuenta con el que se quiere
	 * buscar la valoración más reciente
	 * @return
	 * @throws SQLException
	 */
	private int existeEvolucion (Connection con, int numeroCuenta)  throws SQLException
	{
		PreparedStatementDecorator obtenerFechaYHoraUltimaEvolucionStatement= new PreparedStatementDecorator(con.prepareStatement(obtenerFechaYHoraUltimaEvolucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		obtenerFechaYHoraUltimaEvolucionStatement.setInt(1, numeroCuenta);
		ResultSetDecorator rs=new ResultSetDecorator(obtenerFechaYHoraUltimaEvolucionStatement.executeQuery());
		if (rs.next())
		{
			int res=rs.getInt("codigoUltimaEvolucion");
			fechaUltimaEvolucion=rs.getString("fechaEvolucion");
			horaUltimaEvolucion=rs.getString("horaEvolucion");
			rs.close();
			return res;
		}
		else
		{
			rs.close();
			return -1;
		}
		
	}

	/**
	 *	Método que carga todos los posibles diagnosticos
	 *  
	 * @see com.princetonsa.dao.AuxiliarDiagnosticosDao#cargarDiagnosticos (Connection , int , boolean ) throws SQLException
	 */
	public ResultSetDecorator cargarDiagnosticos (Connection con, int numeroCuenta, boolean esEvolucion) throws SQLException
	{
		int numeroSolicitudValoracion=0, numeroSolicitudEvolucion=0;
		numeroSolicitudValoracion=existeValoracion(con, numeroCuenta);
		numeroSolicitudEvolucion=existeEvolucion(con, numeroCuenta);
		
		
		if (numeroSolicitudValoracion<1&&numeroSolicitudEvolucion<1)
		{
			return null;
		}
		else
		{
			if (numeroSolicitudEvolucion<1)
			{
				//No existe evolucion, luego retornamos el ResultSetDecorator sacado
				//de los diagnosticos
				//logger.info("\n\n\n cargar Diagnosticos EVOLUCION desde valoracion");
				return this.cargarDiagnosticosValoracion(con, numeroSolicitudValoracion, numeroCuenta, esEvolucion);
			}
			else
			{
				logger.info("\n\n\n cargar Diagnosticos EVOLUCION desde evol");	
				return this.cargarDiagnosticosEvolucion(con, numeroSolicitudEvolucion, numeroCuenta, esEvolucion);
			}
			
		}
	}

	/**
	 * Método privado que retorna un ResultSetDecorator con los diagnosticos
	 * de la valoración inicial (Caso en que AuxiliarDiagnosticos se 
	 * use con interconsulta) o todos los diagnosticos de la cuenta
	 * (caso en que se use con evolución)
	 * 
	 * @param con Conexión con la BD Postgresql
	 * @param numeroSolicitud Número de la valoración (solicitud) a la
	 * que se desea cargar todos los diagnosticos
	 * @return
	 * @throws SQLException
	 */
	private ResultSetDecorator cargarDiagnosticosValoracion (Connection con, int numeroSolicitud, int numeroCuenta, boolean esEvolucion) throws SQLException
	{
		try
		{
			if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			
			PreparedStatementDecorator cargarDiagnosticosStatement;
			if (esEvolucion)
			{
				cargarDiagnosticosStatement= new PreparedStatementDecorator(con.prepareStatement(cargarDiagnosticosValoracion2Str,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				cargarDiagnosticosStatement.setInt(1, numeroCuenta);
				logger.info("cargarDiagnosticosValoracion2Str EVOLUCION--->"+cargarDiagnosticosValoracion2Str+" -numCuenta="+numeroCuenta);
				return new ResultSetDecorator(cargarDiagnosticosStatement.executeQuery());
			}
			else
			{
				cargarDiagnosticosStatement= new PreparedStatementDecorator(con.prepareStatement(cargarDiagnosticosValoracionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				cargarDiagnosticosStatement.setInt(1, numeroSolicitud);
				logger.info("cargarDiagnosticosValoracionStr EVOLUCION--->"+cargarDiagnosticosValoracionStr+" -numSolicitud="+numeroSolicitud);
				return new ResultSetDecorator(cargarDiagnosticosStatement.executeQuery());
			}
			
		} 
		catch (SQLException e) 
		{
			logger.warn(e);	
			throw e;			
		}
	}

	/**
	 * Método privado que retorna un ResultSetDecorator con los diagnosticos
	 * de una evolución dado su código (si AuxiliarEvolucion esta 
	 * trabajando con interconsulta) o los diagnosticos de la última
	 * evolución más  el conjunto de los diagnósticos de todas las
	 * interconsultas seguidas que NO tengan evolución después 
	 * 
	 * @param con Conexión con la BD Postgresql
	 * @param numeroSolicitud Número de la evolución (solicitud) a la
	 * que se desea cargar todos los diagnosticos
	 * @return
	 * @throws SQLException
	 */
	private ResultSetDecorator cargarDiagnosticosEvolucion (Connection con, int numeroSolicitud, int idCuenta, boolean esEvolucion) throws SQLException
	{
		try
		{
			if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			
			
			PreparedStatementDecorator cargarDiagnosticosStatement;
			if (esEvolucion)
			{
				String consultaReal="(" + cargarDiagnosticosEvolucion2Str + ") UNION (" + cargarDiagnosticosEvolucionExtraStr + ")";


				
				cargarDiagnosticosStatement= new PreparedStatementDecorator(con.prepareStatement(consultaReal,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				cargarDiagnosticosStatement.setInt(1, numeroSolicitud);
				cargarDiagnosticosStatement.setInt(2, idCuenta);
				cargarDiagnosticosStatement.setString(3, fechaUltimaEvolucion);
				cargarDiagnosticosStatement.setString(4, fechaUltimaEvolucion);
				cargarDiagnosticosStatement.setString(5, horaUltimaEvolucion);
				
				logger.info("consultaReal-->"+consultaReal+" -numSol="+numeroSolicitud+" fechaUltEvol->"+fechaUltimaEvolucion+" horaUl->"+horaUltimaEvolucion);
				
				return new ResultSetDecorator(cargarDiagnosticosStatement.executeQuery());
			}
			else
			{
				cargarDiagnosticosStatement= new PreparedStatementDecorator(con.prepareStatement(cargarDiagnosticosEvolucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				cargarDiagnosticosStatement.setInt(1, numeroSolicitud);

				return new ResultSetDecorator(cargarDiagnosticosStatement.executeQuery());
			}
			
			
		} 
		catch (SQLException e) 
		{
			logger.warn(e);
			throw e;			
		}
	}

	/**
	 * Método que busca el diagnostico de complicación
	 * 
	 * @param con Conexión con la BD Postgresql
	 * @param numeroCuenta Número de la cuenta en la
	 * que se desea búscar el diagnostico de complicación
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarDiagnosticoComplicacion(Connection con, int numeroCuenta) throws SQLException
	{
		PreparedStatementDecorator cargarDiagnosticoComplicacionStatament= new PreparedStatementDecorator(con.prepareStatement(cargarDiagnosticoComplicacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cargarDiagnosticoComplicacionStatament.setInt(1, numeroCuenta);
		logger.info("cargarDiagnosticoComplicacionStr-->"+cargarDiagnosticoComplicacionStr+" -->numCuenta="+numeroCuenta);
		
		return new ResultSetDecorator(cargarDiagnosticoComplicacionStatament.executeQuery());
	}

}
