/*
 * @(#)SqlBaseExcepcionesTarifasDao.java
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
import java.util.Vector;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;

/**
 * Implementaci�n sql gen�rico de todas las funciones de acceso a la fuente de datos
 * para un  las Excepciones de Tarifas
 *
 * @version 1.0, Octubre 20 / 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson R�os</a>
 */
public class SqlBaseExcepcionesTarifasDao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseExcepcionesTarifasDao.class);
	
	/**
	 * Obtiene el �ltimo cod de la sequence para poder cargar el resumen de la excepci�n
	 */
	private final static String ultimaSequenciaStr= "SELECT MAX(codigo) AS seq_excepciones_tarifas FROM excepciones_tarifas ";
	
	 /**
	  * Consulta para cargar el resumen
	  */	
	private static String cargarResumenPorCodigo = 	"SELECT et.codigo AS codigo, " +
																				"et.porcentaje AS porcentaje, " +
																				"et.valor_ajuste AS valorAjuste, " +
																				"et.nueva_tarifa AS nuevaTarifa, " +
																				"CASE WHEN et.via_ingreso IS NULL THEN 0 ELSE et.via_ingreso END  AS codigoViaIngreso, " +
																				"CASE WHEN vi.nombre IS NULL THEN 'TODAS' ELSE vi.nombre END AS nombreViaIngreso, " +
																				"et.especialidad AS codigoEspecialidad, " +
																				"e.nombre AS nombreEspecialidad, " +
																				"CASE WHEN et.servicio IS NULL THEN 0 ELSE et.servicio END AS codigoServicio, " +
																				"CASE WHEN rs.descripcion IS NULL THEN 'TODOS' ELSE rs.descripcion END AS nombreServicio, " +
																				"et.contrato AS codigoContrato," +
																				"c.numero_contrato AS numeroContrato  " +
																				"FROM excepciones_tarifas et " +
																				//toca buscarlos con left outer join debido a que en el disenio se grabaron nulos para la opcion todos, 
																				//dado que hacian referencia a funcionalidades antiguas ENTONCES NO SE PUDO COLOCAR TODOS=0 EN BD (reset-forms)
																				"INNER JOIN especialidades e ON (et.especialidad = e.codigo) " +
																				"INNER JOIN contratos c ON (et.contrato = c.codigo) " +
																				"LEFT OUTER JOIN  vias_ingreso vi ON (et.via_ingreso = vi.codigo ) " +
																				"LEFT OUTER JOIN referencias_servicio rs ON (et.servicio = rs.servicio AND rs.tipo_tarifario = "+ConstantesBD.codigoTarifarioCups +" ) " +
																				"WHERE  ";
	
	/**
	 * Elimina una excepci�n dado su c�digo
	 */
	private static String eliminarExcepcionStr="DELETE FROM excepciones_tarifas where codigo=? ";
	
		/**
		 * Inserta una excepci�n a una tarifa 
		 * @param con, Connection, conexi�n abierta con la fuente de datos
		 * @param porcentaje. double, porcentaje de descuento o aumento de la tarifa
		 * @param valorAjuste. double, valor de ajuste de la tarifa
		 * @param nuevaTarifa. double, nueva tarifa
		 * @param codigoServicio. int, c�digo del servicio asociado esta tarifa, 0 es para todos
		 * @param codigoEspecialidad. int, c�digo de la especialidad asociado esta tarifa, 0 es para todas
		 * @param codigoViaIngreso. int, c�digo de la via de ingreso asociado esta tarifa, 0 es para todas
		 * @param codigoContrato. int, c�digo del contrato para el cual es v�lida esta excepci�n
		 * @return int 
		 * @see com.princetonsa.dao.ExcepcionTarifaDao#insertar(java.sql.Connection, double, double, double, int, int, int, int)
		 */
		public static boolean  insertar(		Connection con,
															double porcentaje,
															double valorAjuste,
															double nuevaTarifa,
															int codigoServicio,
															int codigoEspecialidad,
															int codigoViaIngreso,
															int codigoContrato,
															String insertarStr)
		{		 
			try
			{			
				PreparedStatementDecorator insertar =  new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				insertar.setDouble(1, porcentaje);
				insertar.setDouble(2, valorAjuste);
				insertar.setDouble(3, nuevaTarifa);
				insertar.setInt(6, codigoContrato);
				
				if( codigoViaIngreso == 0 ) // Todas
				{
					insertar.setObject(4, null);
				}
				else
				{
					insertar.setInt(4, codigoViaIngreso);
				}
				
				insertar.setInt(5, codigoEspecialidad);
				
				if( codigoServicio == 0 ) // Todos
				{
					insertar.setObject(7, null);
				}
				else
				{
					insertar.setInt(7, codigoServicio);
				}
						
				int insert = insertar.executeUpdate();
				
				if( insert > 0 )
					return true;
				else
					return false;
			}
			catch(SQLException e)
			{
				logger.warn("No se insert� la excepcion de la tarifa"+e);
				return false;
			}
		}
		
		/**
		 * Dice si existe o no una excepcion de tarifa dada por la via de ingreso, la especialidad, el contrato y el codigo del servicio asociado
		 * @param con, Connection, conexi�n abierta con la fuente de datos
		 * @param codigoViaIngreso. int, c�digo del esquema tarifario asociado a esta tarifa
		 * @param codigoEspecialidad, int, c�digo de la especialidad de la excepcion
		 * @param codigoContrato, int, c�digo del contrato asociado a la excepcion
		 * @param codigoServicio. int, c�digo del servicio asociado a esta tarifa
		 * @return boolean 
		 * @throws SQLException
		 * @see com.princetonsa.dao.ExcepcionTarifaDao#existeTarifaDefinida(java.sql.Connection, int, int, int, int)
		 */
		public static boolean  existeTarifaDefinida(	Connection con, 
																			int codigoViaIngreso, 
																			int codigoEspecialidad, 
																			int codigoContrato, 
																			int codigoServicio,
																			int codigoExcepcion) 
		{
				try
				{
					String consulta = 	"SELECT COUNT(*) AS numExcepcionesDefinidas " +
					 							"FROM excepciones_tarifas et " +
												"WHERE et.contrato =  "+codigoContrato;
					
					if( codigoViaIngreso == 0 ) // todas
						consulta += " AND et.via_ingreso IS NULL ";
					else if(codigoViaIngreso == -1)
					{
						//la misma de BD
						if(codigoExcepcion>0)
							consulta+= "AND et.via_ingreso = (select via_ingreso FROM excepciones_tarifas WHERE codigo="+codigoExcepcion+")";
					}	
					else
						consulta += " AND et.via_ingreso = "+codigoViaIngreso+" ";
					
					if( codigoEspecialidad == 0 ) // todas
						consulta += " AND et.especialidad IS NULL ";
					else if(codigoEspecialidad == -1)
					{
						//la misma de BD
						if(codigoExcepcion>0)
							consulta+= "AND et.especialidad = (select especialidad FROM excepciones_tarifas WHERE codigo="+codigoExcepcion+")";
					}	
					else
						consulta += " AND et.especialidad = "+codigoEspecialidad+" ";
					
					if( codigoServicio == 0 )
						consulta += " AND et.servicio IS NULL ";
					else if(codigoServicio == -1)
					{
						//la misma de BD
						if(codigoExcepcion>0)
							consulta+= "AND et.servicio = (select servicio FROM excepciones_tarifas WHERE codigo="+codigoExcepcion+")";
					}
					else
						consulta += " AND et.servicio = "+codigoServicio+" ";
					
					if(codigoExcepcion>0)
						consulta += "AND et.codigo <>"+codigoExcepcion;
					
					PreparedStatementDecorator existeTarifaSttmnt =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	
					ResultSetDecorator rs = new ResultSetDecorator(existeTarifaSttmnt.executeQuery());
					
					
					rs.next();
					if(rs.getInt("numExcepcionesDefinidas")>0)
						return true;
					else
						return false;
				}
				catch(SQLException e)
				{
					logger.warn("error en la consulta de la existencia de las excepciones de tarifas"+e);
					return false;
				}
		}		
		
		/**
		 * Cargar la ultima excepcion; insertada  
		 * @param con Connection con la fuente de datos
		 * @return int ultimoCodigoSequence, 0 no efectivo.
		 */
		public static int cargarUltimoCodigoSequence(Connection con)
		{
			int ultimoCodigoSequence=0;
			try
			{
				PreparedStatementDecorator cargarUltimoStatement= new PreparedStatementDecorator(con.prepareStatement(ultimaSequenciaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ResultSetDecorator rs= new ResultSetDecorator(cargarUltimoStatement.executeQuery());
				if(rs.next())
				{
					ultimoCodigoSequence=rs.getInt("seq_excepciones_tarifas");
					return ultimoCodigoSequence;
				}
				else
				{
					return 0;
				}
			}
			catch(SQLException e)
			{
				logger.warn(e+" Error en la consulta del �ltimo c�digo de las excepciones de tarifas: SqlBaseExcepcionesTarifasDao "+e.toString());
				return 0;
			}
		}
		
		/**
		 * M�todo que  carga  los datos de una excepci�n para mostrarlos en el resumen
		 * en una BD PostgresSQL o Hsqldb 
		 */
		public static ResultSetDecorator cargarResumen(Connection con) 
		{
			try
			{	
				int ultimoCodigo= cargarUltimoCodigoSequence(con);
				PreparedStatementDecorator cargarResumenStatement= new PreparedStatementDecorator(con.prepareStatement(cargarResumenPorCodigo + " et.codigo = ? ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				cargarResumenStatement.setInt(1, ultimoCodigo);
				return new ResultSetDecorator(cargarResumenStatement.executeQuery());
			}
			catch(SQLException e)
			{
				logger.warn(e+" Error en la consulta del resumen de las excepciones de tarifas: SqlBaseExcepcionesTarifasDao "+e.toString());
				return null;
			}
		}
		
		/**
		 * M�todo que  carga  el resumen de la modificaci�n de N excepciones 
		 * en una BD PostgresSQL o Hsqldb 
		 */
		public static ResultSetDecorator cargarResumenModificacion(Connection con, Vector codigos) 
		{
			try
			{	
				String consulta = 	"SELECT et.codigo AS codigo, " +
											"CASE WHEN et.porcentaje<0 THEN '-' ELSE '+' END AS signoPorcentaje, " +	
											"CASE WHEN et.porcentaje<0 THEN (et.porcentaje*-1) ELSE et.porcentaje END AS porcentaje, " +
											"CASE WHEN et.valor_ajuste<0 THEN '-' ELSE '+' END AS signoValorAjuste, " +
											"CASE WHEN et.valor_ajuste<0 THEN (et.valor_ajuste*-1) ELSE et.valor_ajuste END AS valorAjuste, " +
											"et.nueva_tarifa AS nuevaTarifa, " +
											"CASE WHEN et.via_ingreso IS NULL THEN 0 ELSE et.via_ingreso END AS codigoViaIngreso, " +
											"CASE WHEN vi.nombre IS NULL THEN 'Todas' ELSE vi.nombre END AS nombreViaIngreso, " +
											"et.especialidad AS codigoEspecialidad, " +
											"e.nombre AS nombreEspecialidad, " +
											"CASE WHEN et.servicio IS NULL THEN 0 ELSE  et.servicio END  AS codigoServicio, " +
											"CASE WHEN rs.descripcion IS NULL THEN 'TODOS' ELSE rs.descripcion END AS nombreServicio, " +
											"et.contrato AS codigoContrato,  " +
											"c.numero_contrato AS numeroContrato, " +
											"con.nombre AS nombreConvenio " +
											"FROM excepciones_tarifas et " +
											"INNER JOIN especialidades e ON (et.especialidad = e.codigo) " +
											"INNER JOIN contratos c ON (et.contrato = c.codigo) " +
											"INNER JOIN convenios con ON (con.codigo=c.convenio) " +
											"LEFT OUTER JOIN vias_ingreso vi ON (et.via_ingreso = vi.codigo ) " +
											"LEFT OUTER JOIN referencias_servicio rs ON (et.servicio = rs.servicio AND rs.tipo_tarifario ="+ConstantesBD.codigoTarifarioCups+" "+
											" ) WHERE 1=1 ";
											
				
				if(codigos.size()==1)
					consulta+= " AND et.codigo ="+codigos.get(0).toString();
				else
				{	
					consulta+= " AND et.codigo IN( ";
					for(int i=0; i<codigos.size(); i++)
					{	
						consulta+= ""+codigos.get(i).toString();
						if(i!=codigos.size()-1)
							consulta+=", ";
						else
							consulta+=" )";
					}
				}
				
				PreparedStatementDecorator cargarResumenStatement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				return new ResultSetDecorator(cargarResumenStatement.executeQuery());
			}
			catch(SQLException e)
			{
				logger.warn(e+" Error en la consulta del resumen de las excepciones modificadas de tarifas: SqlBaseExcepcionesTarifasDao "+e.toString());
				return null;
			}
		}
		
		
		
		
		/**
		 * M�todo que contiene el Resulset de todas las excepciones  buscadas
		 * @param con, Connection, conexi�n abierta con una fuente de datos
		 * @return Resultset con todos los datos de la tabla empresas
		 * @throws SQLException
		 */
		public static  ResultSetDecorator busqueda(Connection con,
																	int codigoContrato,
																	int codigoViaIngreso,
																	int codigoServicio,
																	String nombreServicio,
																	int codigoEspecialidad,
																	String nombreEspecialidad,
																	double porcentaje,
																	double valorAjuste,
																	double nuevaTarifa) throws SQLException
		{
			
			ResultSetDecorator respuesta=null;
			String consultaArmada="";
			if(con==null || con.isClosed())
			{
				try
				{
					DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
					con = myFactory.getConnection();
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo realizar la conexi�n "+e.toString());
					respuesta= null;
				}
			}
			try
			{
				consultaArmada=armarConsulta(codigoContrato, codigoViaIngreso, codigoServicio, nombreServicio, codigoEspecialidad, nombreEspecialidad, porcentaje, valorAjuste, nuevaTarifa);
				PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consultaArmada,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				respuesta=new ResultSetDecorator(ps.executeQuery());
			}
			catch(SQLException e)
			{
				logger.warn("Error en la b�squeda avanzada de la excepci�n de tarifas " +e.toString());
				respuesta=null;
			}
			return respuesta;
		}
		
		/**
		 * M�todo que arma la consulta seg�n los datos dados por el usuarios en 
		 * la b�squeda avanzada. 
		 */
		private static String armarConsulta  (	int codigoContrato,
																int codigoViaIngreso,
																int codigoServicio,
																String nombreServicio,
																int codigoEspecialidad,
																String nombreEspecialidad,
																double porcentaje,
																double valorAjuste,
																double nuevaTarifa)
		{
			String consulta= 	"SELECT et.codigo AS codigo, " +
										"CASE WHEN et.porcentaje<0 THEN '-' ELSE '+' END AS signoPorcentaje, " +	
										"CASE WHEN et.porcentaje<0 THEN (et.porcentaje*-1) ELSE et.porcentaje END AS porcentaje, " +
										"CASE WHEN et.valor_ajuste<0 THEN '-' ELSE '+' END AS signoValorAjuste, " +
										"CASE WHEN et.valor_ajuste<0 THEN (et.valor_ajuste*-1) ELSE et.valor_ajuste END AS valorAjuste, " +
										"et.nueva_tarifa AS nuevaTarifa, " +
										"CASE WHEN et.via_ingreso IS NULL THEN 0 ELSE et.via_ingreso END AS codigoViaIngreso, " +
										"CASE WHEN vi.nombre IS NULL THEN 'Todas' ELSE vi.nombre END AS nombreViaIngreso, " +
										"et.especialidad AS codigoEspecialidad, " +
										"e.nombre AS nombreEspecialidad, " +
										"CASE WHEN et.servicio IS NULL THEN 0 ELSE  et.servicio END  AS codigoServicio, " +
										"CASE WHEN rs.descripcion IS NULL THEN 'TODOS' ELSE rs.descripcion END AS nombreServicio, " +
										"et.contrato AS codigoContrato,  " +
										"c.numero_contrato AS numeroContrato, " +
										"con.nombre AS nombreConvenio " +
										"FROM excepciones_tarifas et " +
										"INNER JOIN especialidades e ON (et.especialidad = e.codigo ";
										
			if(codigoEspecialidad>0)
				consulta+=" AND e.codigo = "+codigoEspecialidad;
			if(nombreEspecialidad!=null && !nombreEspecialidad.equals(""))
				consulta+=" AND UPPER(e.nombre) LIKE UPPER('%"+nombreEspecialidad+"%') "; 
			
			consulta+=" )   INNER JOIN contratos c ON (et.contrato = c.codigo ";
			
			if(codigoContrato>0)
				consulta+=" AND c.codigo ="+codigoContrato ;
			
			consulta+=" ) INNER JOIN convenios con ON (con.codigo=c.convenio) ";
			
			consulta+=" LEFT OUTER JOIN vias_ingreso vi ON (et.via_ingreso = vi.codigo ) ";
			consulta+=" LEFT OUTER JOIN referencias_servicio rs ON (et.servicio = rs.servicio AND rs.tipo_tarifario ="+ConstantesBD.codigoTarifarioCups;
			consulta+=" ) WHERE 1=1 ";
			
			if(porcentaje!=0.0)
				consulta+=" AND et.porcentaje = "+porcentaje;
			else if(valorAjuste!=0.0)
				consulta+= " AND et.valor_ajuste = "+valorAjuste;
			else if(nuevaTarifa!=0.0)
				consulta+=" AND et.nueva_tarifa = "+nuevaTarifa ;
			
			if(codigoViaIngreso>0)
				consulta+=" AND vi.codigo = "+codigoViaIngreso;
			if(codigoServicio>0)
				consulta+=" AND rs.servicio = "+codigoServicio;
			if(nombreServicio!=null && !nombreServicio.equals(""))
				consulta+=" AND UPPER(rs.descripcion) LIKE UPPER('%"+nombreServicio+"%') ";
			
			consulta+=" ORDER BY nombreViaIngreso ASC, codigoServicio ASC ";
			
			
			return consulta;
		}

		/**
		 * M�todo que elimina una excepci�n de tarifa seg�n su c�digo  
		 */
		public static boolean eliminar(Connection con, int codigoExcepcion)
		{
			int resp=0;	
			try{
					if (con == null || con.isClosed()) 
					{
						throw new SQLException ("Error SQL: Conexi�n cerrada");
					}
					PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarExcepcionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1,codigoExcepcion);
					
					resp=ps.executeUpdate();
					if(resp>0)
						return true;
					else
						return false;
				}
				catch(SQLException e)
				{
					logger.warn(e+" Error en la inserci�n de datos: SqlBaseExcepcionesTarifasDao "+e.toString());
					return false;			
				}	
		}
		
		/**
		 * Modifica una excepci�n dado su c�digo con los param�tros dados.
		 * @param con, Connection, conexi�n abierta con una fuente de datos
		 * @param codigo, int, codigo de la excepci�n
		 * @param codigoViaIngreso, int, codigo v�a de ingreso
		 * @param codigoEspecialidad
		 * @paramcodigoServicio
		 * @param porcentaje
		 * @param valorAjuste
		 * @param nuevaTarifa
		 * @return  boolean
		 */		
		public static boolean modificar(		Connection con, 
															int codigo, 
															int codigoViaIngreso,
															int codigoEspecialidad,
															int codigoServicio,
															double porcentaje,
															double valorAjuste, 
															double nuevaTarifa)
		{
			int resp=0;	
			try{
					if (con == null || con.isClosed()) 
					{
						throw new SQLException ("Error SQL: Conexi�n cerrada");
					}
					
					int contador=0;
					String modificarExcepcionStr="UPDATE excepciones_tarifas SET ";
					
					if(codigoViaIngreso>0)
					{	
						modificarExcepcionStr+=" via_ingreso = "+codigoViaIngreso;
						contador++;
					}	
					else if(codigoViaIngreso==0)
					{	
						modificarExcepcionStr+=" via_ingreso = null ";
						contador++;
					}	
					if(codigoEspecialidad>=0 )
					{
						if(contador>0)
							modificarExcepcionStr+=" , ";
						modificarExcepcionStr+=" especialidad = "+codigoEspecialidad;
						contador++;
					}
					if(codigoServicio>0)
					{
						if(contador>0)
							modificarExcepcionStr+=" , ";
						modificarExcepcionStr+=" servicio = "+codigoServicio;
						contador++;
					}
					else if(codigoServicio==0)
					{
						if(contador>0)
							modificarExcepcionStr+=" , ";
						modificarExcepcionStr+=" servicio = null ";
						contador++;
					}
					if(porcentaje!=0.0)
					{
						if(contador>0)
							modificarExcepcionStr+=" , ";
						modificarExcepcionStr+=" porcentaje = "+porcentaje+",  valor_ajuste = 0.0, nueva_tarifa = 0.0 ";
						contador++;
					}
					else if(valorAjuste!=0.0)
					{
						if(contador>0)
							modificarExcepcionStr+=" , ";
						modificarExcepcionStr+=" valor_ajuste = "+valorAjuste+", porcentaje=0.0,  nueva_tarifa = 0.0";
						contador++;
					}	
					else if(nuevaTarifa!=0.0)
					{
						if(contador>0)
							modificarExcepcionStr+=" , ";
						modificarExcepcionStr+=" nueva_tarifa = "+nuevaTarifa+", porcentaje=0.0,  valor_ajuste = 0.0";
						contador++;
					}
					
					modificarExcepcionStr+=" WHERE codigo = "+codigo;
					
					
					PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarExcepcionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					resp=ps.executeUpdate();
					if(resp>0)
						return true;
					else
						return false;
			}
			catch(SQLException e)
			{
				logger.warn(e+" Error en la modificaci�n de datos: SqlBaseExcepcionesTarifasDao "+e.toString());
				return false;			
			}	
		}
}
