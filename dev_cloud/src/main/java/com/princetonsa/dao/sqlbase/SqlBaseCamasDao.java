/*
 * @(#)SqlBaseCamasDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 * para camas
 *
 * @version 2.0, Junio 16 / 2004
 * @author <a href="mailto:juanda@PrincetonSA.com">Juan David Ramírez</a>
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class SqlBaseCamasDao
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private static Logger logger = Logger.getLogger(SqlBaseCamasDao.class);

	private static final String listarCamasStr = "SELECT c.codigo as codCama, c.numero_cama as numCama, c.descripcion as descripcionCama, c.estado as estadoCama, ec.nombre as nombreEstadoCama, c.centro_costo as codCentroCostoCama, cc.nombre as nombreCentroCostoCama, c.habitacion "
																		+ "FROM camas1 c " +
																		  "INNER JOIN centros_costo cc ON(cc.codigo=c.centro_costo) " +
																		  "INNER JOIN estados_cama ec ON(ec.codigo = c.estado) "
																		+ "WHERE "
																		+ "cc.centro_atencion = ?  " 
																		+ "ORDER BY c.centro_costo ASC, c.habitacion ASC, c.numero_cama ASC ";

	private static final String listarCamasPorCCostoStr = "SELECT c.codigo as codCama, c.numero_cama as numCama, c.descripcion as descripcionCama, c.estado as estadoCama, ec.nombre as nombreEstadoCama, c.centro_costo as codCentroCostoCama, cc.nombre as nombreCentroCostoCama, c.habitacion "
																		+ "FROM camas1 c, centros_costo cc, estados_cama ec "
																		+ "WHERE c.centro_costo = cc.codigo "
																		+ "AND ec.codigo = c.estado "
																		+ "AND c.centro_costo = ? "
																		+ "ORDER BY c.centro_costo ASC, c.habitacion ASC, c.numero_cama ASC ";

	
	
	private static final String consultaTrasladoCamas= 	"SELECT " +
																							"c.descripcion AS descripcion, " +
																							"c.numero_cama AS numero, " +
																							"tabla.paciente AS paciente, " +
																							"tabla.fecha AS fecha, " +
																							"tabla.hora AS hora, " +
																							"tabla.admision AS admision, " +
																							"tabla.cuenta AS cuenta, " +
																							"pers.primer_nombre || ' ' || " +
																							"pers.segundo_nombre ||' ' || " +
																							"pers.primer_apellido || ' ' || " +
																							"pers.segundo_apellido AS usuario, " +
																							"tabla.esInicial AS esInicial " +
																							"FROM " +
																							"(SELECT " +
																							"personas.primer_nombre || ' ' || " +
																							"personas.segundo_nombre || ' ' || " +
																							"personas.primer_apellido || ' ' || " +
																							"personas.segundo_apellido AS paciente, " +
																							"fecha_admision || '' AS fecha, " +
																							"hora_admision || '' AS hora, " +
																							"ah.codigo AS admision, " +
																							"ah.cuenta AS cuenta, " +
																							"ah.cama AS cama, " +
																							"ah.login_usuario AS usuario, " +
																							"" + ValoresPorDefecto.getValorTrueParaConsultas() + " AS esInicial " +
																							"FROM " +
																							"admisiones_hospi ah " +
																							"INNER JOIN cuentas cue " +
																							"ON(ah.cuenta=cue.id) " +
																							"INNER JOIN personas " +
																							"ON(cue.codigo_paciente=personas.codigo) " +
																							"UNION " +
																							"SELECT " +
																							"personas.primer_nombre || ' ' || " +
																							"personas.segundo_nombre || ' ' || " +
																							"personas.primer_apellido || ' ' || " +
																							"personas.segundo_apellido AS paciente, " +
																							"hcp.fecha_traslado || '' AS fecha, " +
																							"hcp.hora_traslado || '' AS hora, " +
																							"hcp.codigo_admision AS admision, " +
																							"ah.cuenta AS cuenta, " +
																							"hcp.codigo_cama AS cama, " +
																							"hcp.login_usuario AS usuario, " +
																							"" + ValoresPorDefecto.getValorFalseParaConsultas() + " AS esInicial " +
																							"FROM " +
																							"his_cama_pac hcp " +
																							"INNER JOIN personas " +
																							"ON (codigo_paciente=personas.codigo) " +
																							"INNER JOIN admisiones_hospi ah " +
																							"ON (hcp.codigo_admision=ah.codigo)) " +
																							"tabla INNER JOIN camas1 c " +
																							"ON(c.codigo=tabla.cama) " +
																							"INNER JOIN usuarios us " +
																							"ON(tabla.usuario=us.login) " +
																							"INNER JOIN personas pers " +
																							"ON(pers.codigo=us.codigo_persona) " +
																							"WHERE 1=1 ";	
	
	private static final String linksTrasladoCamasPorPacienteStr=	"SELECT " +
																												"cc.nombre AS centroCosto, " +
																												"c.numero_cama AS numero, " +
																												"ah.cuenta AS cuenta, " +
																												"ah.fecha_admision || ''  AS fechaIngreso, " +
																												"ah.hora_admision  || '' AS horaIngreso, " +
																												"ing.fecha_egreso  || '' AS fechaEgreso, " +
																												"ing.hora_egreso || '' AS horaEgreso, " +
																												"cue.estado_cuenta AS estadoCuenta " +
																												"FROM " +
																												"ingresos ing, " +
																												"admisiones_hospi ah " +
																												"INNER JOIN solicitudes sol " +
																												"ON (sol.cuenta=ah.cuenta) " +
																												"INNER JOIN tratantes_cuenta tc " +
																												"ON(tc.solicitud=sol.numero_solicitud) " +
																												"INNER JOIN centros_costo cc " +
																												"ON (cc.codigo=tc.centro_costo) " +
																												"INNER JOIN camas1 c " +
																												"ON(c.codigo=ah.cama) " +
																												"INNER JOIN cuentas cue " +
																												"ON(cue.id=ah.cuenta) " +
																												"WHERE " +
																												"ing.codigo_paciente =? " +
																												"AND ing.id=ah.cuenta " +
																												"ORDER BY fechaIngreso DESC" ;																				

	public static ResultSetDecorator listarCamas(Connection con,int centroAtencion) throws SQLException
	{
		ResultSetDecorator resultado;
		try{
			PreparedStatementDecorator listarCamasStmnt =  new PreparedStatementDecorator(con.prepareStatement(listarCamasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			listarCamasStmnt.setInt(1, centroAtencion);
			resultado=new ResultSetDecorator(listarCamasStmnt.executeQuery());
			return resultado;
		}
		catch(SQLException e)
		{
			logger.error("Error listando camas: "+e);
			return null;
		}		
	}

	public static ResultSetDecorator listarCamasCentroCosto(Connection con, int centroCosto)
	{
		ResultSetDecorator resultado;
		try{
			PreparedStatementDecorator listarCamasStmnt =  new PreparedStatementDecorator(con.prepareStatement(listarCamasPorCCostoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			listarCamasStmnt.setInt(1, centroCosto);
			resultado=new ResultSetDecorator(listarCamasStmnt.executeQuery());
			return resultado;
		}
		catch(SQLException e)
		{
			logger.error("Error listando camas por centro de costo: "+e);
			return null;
		}		
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
	public static Collection busquedaConsultaTrasladoCamas(	Connection con, 
																						String numeroCama, 
																						String fechaInicialTraslado, 
																						String fechaFinalTraslado,
																						String horaInicialTraslado, 
																						String horaFinalTraslado,
																						String usuario)
	{
		String consultaStr= consultaTrasladoCamas;
		consultaStr+=" AND c.numero_cama = '"+numeroCama+"' ";
		
		if((fechaInicialTraslado!=null && !fechaInicialTraslado.equals(""))
			&&(fechaFinalTraslado!=null && !fechaFinalTraslado.equals("")) )
		{
			fechaInicialTraslado= UtilidadFecha.conversionFormatoFechaABD(fechaInicialTraslado);
			fechaFinalTraslado= UtilidadFecha.conversionFormatoFechaABD(fechaFinalTraslado);
			consultaStr+="AND tabla.fecha >= '"+fechaInicialTraslado+"' AND tabla.fecha <= '"+fechaFinalTraslado+"' ";	
		}
		if((horaInicialTraslado!=null && !horaInicialTraslado.equals(""))
			&&(horaFinalTraslado!=null && !horaFinalTraslado.equals("")) )
		{
			consultaStr+="AND tabla.hora >= '"+horaInicialTraslado+"' AND tabla.hora <= '"+horaFinalTraslado+"' ";
		}
		if(usuario != null && !usuario.equals(""))
		{
			consultaStr+=	" AND (pers.primer_nombre || ' ' || " +
									"pers.segundo_nombre ||' ' || " +
									"pers.primer_apellido || ' ' || " +
									"pers.segundo_apellido = '"+usuario+"' AND tabla.esInicial=" + ValoresPorDefecto.getValorFalseParaConsultas() + ") ";
		}
		
		consultaStr+=" ORDER BY fecha, hora";
			
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator resultado=new ResultSetDecorator(ps.executeQuery());
			return UtilidadBD.resultSet2Collection(resultado);
		}
		catch(SQLException e)
		{
			logger.error("Error consultando el traslado de camas "+e);
			return null;
		}
	}
	
	/**
	 * Método que carga los links pertenecientes a la consulta de traslado 
	 * con búsqueda por  paciente
	 * @param con, conexión con la fuente de datos
	 * @param codigoPaciente, código del paciente cargado en la sesión
	 * @return
	 * @throws SQLException
	 */
	public static Collection linksConsultaTrasladoCamasPorPaciente(Connection con, int codigoPaciente) throws SQLException
	{
		ResultSetDecorator respuesta=null;
		if(con==null || con.isClosed())
		{
			try
			{
				DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
			catch(SQLException e)
			{
				logger.warn("No se pudo realizar la conexión "+e.toString());
				respuesta= null;
			}
		}
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(linksTrasladoCamasPorPacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoPaciente);
			respuesta=new ResultSetDecorator(ps.executeQuery());				
		}
		catch(SQLException e)
		{
			logger.warn("Error en el listado de los links " +e.toString());
			respuesta=null;
		}
		return UtilidadBD.resultSet2Collection(respuesta);
	}

	/**
	 * Método que obtiene los datos pertinentes desde el link 
	 * see=linksConsultaTrasladoCamasPorPaciente para cargar 
	 * los datos de la consulta de traslado de camas
	 * @param con, conexión con la fuente de datos
	 * @param cuenta, cuenta del paciente
	 * @return
	 */
	public static Collection busquedaConsultaTrasladoCamasPorPaciente (	Connection con,	
																															int cuenta ) throws SQLException 
	{
		ResultSetDecorator respuesta=null;
		if(con==null || con.isClosed())
		{
			try
			{
				DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
			catch(SQLException e)
			{
				logger.warn("No se pudo realizar la conexión "+e.toString());
				respuesta= null;
			}
		}
		String consultaPorPacienteStr= consultaTrasladoCamas +" AND cuenta = ? ORDER BY fecha, hora";
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consultaPorPacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,cuenta);
			respuesta=new ResultSetDecorator(ps.executeQuery());				
		}
		catch(SQLException e)
		{
			logger.warn("Error en el listado de los links " +e.toString());
			respuesta=null;
		}
		return UtilidadBD.resultSet2Collection(respuesta);
																														
	}

	

}	

