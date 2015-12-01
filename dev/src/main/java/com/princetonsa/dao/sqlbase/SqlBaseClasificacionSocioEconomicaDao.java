/*
 * @(#)SqlBaseClasificacionSocioEconomicaDao
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

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;

/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 * para las clasificaciones socieconómicas
 *
 * @version 1.0, Junio 30 / 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class SqlBaseClasificacionSocioEconomicaDao
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseClasificacionSocioEconomicaDao.class);
	
	/**
	 * Carga los datos para mostrarlos en el resumen
	 */
	private final static String cargarDatosClasificacion =	"SELECT " +
																							"es.codigo AS codigo, " +
																							"es.descripcion AS descripcion, " +
																							"es.tipo_regimen AS acronimoTipoRegimen," +
																							"t.nombre AS nombreTipoRegimen," +
																							"es.activo AS activa " +
																							" FROM estratos_sociales es, " +
																							"tipos_regimen t " +
																							"WHERE " +
																							"es.tipo_regimen= t.acronimo " +
																							"AND es.codigo=  ?" ;

	private final static String cargarCodigoUltimaInsercion = "SELECT MAX(codigo) AS codigo from estratos_sociales";
	
	/**
	 * Hace la modificación de los datos de la clasificación socioeconómica
	 */
	private final static String modificarClasificacion=	"UPDATE " +
																					"estratos_sociales SET " +
																					"descripcion= ?, " +
																					"tipo_regimen=?,"+
																					"activo= ?" +
																					"WHERE " +
																					"codigo = ?";

	/**
	 * Seleccionar todos los datos de la tabla  estratos_sociales para mostrarlos en el listado
	 */
	private final static String consultarClasificacion= 	"SELECT es.codigo AS codigo, " +
																						"es.descripcion AS descripcion, " +
																						"t.nombre AS tipoRegimen, " +
																						"es.activo AS activo " +
																						"FROM " +
																						"estratos_sociales es, " +
																						"tipos_regimen t " +
																						"WHERE " +
																						"es.tipo_regimen= t.acronimo " +
																						"AND es.institucion= ? " +
																						"ORDER BY  " +
																						"t.nombre, " +
																						"es.descripcion";
	

	/**
	 * Inserta una clasificación social
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param descripcion, String, descripcion de la calasificación social
	 * @param tipoRegimen, String, régimen de acuerdo a los previam/t
	 * 				ingresados en el sistema
	 * @param activa, boolean
	 * @param institucion, int, Codigo de la institucion a la que pertenece el usuario
 	 * @return int, 0 si  no inserta, 1 si inserta
	 */
	public static int  insertar(	Connection con,
												String descripcion,
												String tipoRegimen,
												boolean activa,
												int institucion,
												String insertarClasificacionStr)
	{
		int resp=0;
		try
			{
					if (con == null || con.isClosed()) 
					{
							DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
							con = myFactory.getConnection();
					}
					PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarClasificacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setString(1,descripcion);
					ps.setString(2, tipoRegimen);
					ps.setBoolean(3,activa);
					ps.setInt(4,institucion);
	
					resp=ps.executeUpdate();
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción de datos: SqlBaseClasificacionSocioEconomicaDao "+e.toString() );
					resp=0;
			}
			return resp;
	}
	
	/**
	 *  Inserta una clasificación socioeconómica dentro de una transacción dado su estado.
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param descripcion, String, descripcion de la calasificación social
	 * @param tipoRegimen, String, régimen de acuerdo a los previam/t
	 * 				ingresados en el sistema
	 * 				que utiliza la clasificacion social
	 * @param activa, boolean	 
	 * @param institucion, int, Codigo de la institucion a la que pertenece el usuario
 	 * @param estado. String, estado dentro de la transacción 
	 * @return  ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
	 * 				de lo contrario
	 */																					
	public static  ResultadoBoolean insertarTransaccional(	Connection con,
																								String descripcion,
																								String tipoRegimen,
																								boolean activa,
																								int institucion,
																								String estado,
																								String insertarClasificacionStr) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		ResultadoBoolean resp=new ResultadoBoolean(false);
		if( estado != null && estado.equals(ConstantesBD.inicioTransaccion) )
		{
		    if (!myFactory.beginTransaction(con))
		    {
		        return new ResultadoBoolean(false, "No se pudo iniciar la transacción");
		    }
		}
		try
		{
			if (con == null || con.isClosed()) 
			{
				//Como es transaccional NO voy a tratar de abrir una nueva conexión, sino que mandaré una excepction
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			else
			{	
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarClasificacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setString(1,descripcion);
				ps.setString(2, tipoRegimen);
				ps.setBoolean(3,activa);
				ps.setInt(4,institucion);
				
				int insert=ps.executeUpdate();	
				
				if( insert == 0 )
				{
					myFactory.abortTransaction(con);
					return new ResultadoBoolean(false," Error en la inserción de datos: SqlBaseClasificacionSocioEconomicaDao: ");
				}
			}
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseClasificacionSocioEconomicaDao");
			myFactory.abortTransaction(con);
			resp=new ResultadoBoolean(false," Error en la inserción de datos: SqlBaseClasificacionSocioEconomicaDao: "+e);
			return resp;		
		}
		if( estado != null && estado.equals(ConstantesBD.finTransaccion) )
		{
		    myFactory.endTransaction(con);
		}
		return new ResultadoBoolean(true);		
	}

	/**
	 * Método que  carga  los datos de una clasifcación socioeconómica 
	 * según los datos que lleguen del  código de la tabla estratos_sociales 
	 * para mostrarlos en el resumen
	 * en una BD PostgresSQL o Hsqldb 
	 */
	public static ResultSetDecorator cargarResumen(Connection con, int codigo) throws SQLException
	{
			PreparedStatementDecorator cargarResumenStatement= new PreparedStatementDecorator(con.prepareStatement(cargarDatosClasificacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarResumenStatement.setInt(1, codigo);
			return new ResultSetDecorator(cargarResumenStatement.executeQuery());
	}

	/**Carga el última clasificación socioeconómica insertada**/
	public static ResultSetDecorator cargarUltimoCodigo(Connection con)
	{
		try
		{
			PreparedStatementDecorator cargarUltimoStatement= new PreparedStatementDecorator(con.prepareStatement(cargarCodigoUltimaInsercion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return new ResultSetDecorator(cargarUltimoStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta del último código del estrato: SqlBaseClasificacionSocioEconomicaDao "+e.toString());
			return null;
		}
	}

	/**
	 * Modificar una clasificación socioeconómica
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param descripcion, String, descripcion de la calasificación social
	 * @param activa, boolean
 	 * @return int, 0 si  no inserta, 1 si inserta
	 */
	public static int modificar(	Connection con, 
													int codigo, 
													String descripcion,
													String tipoRegimen,
													boolean activa) 
	{
		
		int resp=0;	
		try{
				if (con == null || con.isClosed()) 
				{
					throw new SQLException ("Error SQL: Conexión cerrada");
				}
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarClasificacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

				ps.setString(1,descripcion);
				ps.setString(2,tipoRegimen);
				ps.setBoolean(3,activa);
				ps.setInt(4,codigo);
			
				ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la modificacion de datos: SqlBaseClasificacionSocioEconomicaDao "+e.toString());
			resp=0;			
		}	
		return resp;	
	}

	/**
	 * Modifica una clasificación socioeconómica dado su código con los 
	 * paramétros dados  dentro de una transacción dado su estado. 
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param descripcion, String, descripcion de la calasificación social
	 * @param activa, boolean	 
 	 * @param estado. String, estado dentro de la transacción 
	 * @return  ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
	 * 				de lo contrario
	 */
	public static ResultadoBoolean modificarTransaccional(	Connection con, 
																									int codigo, 
																									String descripcion,
																									String tipoRegimen,
																									boolean activa,
																									String estado) throws SQLException 
	{
		ResultadoBoolean resp=new ResultadoBoolean(false);
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	
		if( estado != null && estado.equals(ConstantesBD.inicioTransaccion) )
		{
		    if (!myFactory.beginTransaction(con))
		    {
		        return new ResultadoBoolean(false, "No se pudo iniciar la transacción");
		    }
		}	
		try
		{
			if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarClasificacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			ps.setString(1,descripcion);
			ps.setString(2,tipoRegimen);
			ps.setBoolean(3,activa);
			ps.setInt(4,codigo);

			ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseClasificacionSocioEconomicaDao");
			myFactory.abortTransaction(con);
			resp=new ResultadoBoolean(false," Error en la inserción de datos: SqlBaseClasificacionSocioEconomicaDao: "+e);
			return resp;
		}
		if( estado != null && estado.equals(ConstantesBD.finTransaccion) )
		{
		    myFactory.endTransaction(con);
		}
		return new ResultadoBoolean(true);		
	}	

	/**
	 * Método que contiene el Resulset de los datos de la tabla estratos_sociales
	 * para posteriorm/t ser mostrados en el pager
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param , int, institucion, Codigo de la institucion a la que pertenece el usuario para filtrar la consulta
	 * @return Resultset con todos los datos de la tabla estratos_sociales
	 * @throws SQLException
	 */
	public static  ResultSetDecorator listado(Connection con, int institucion) throws SQLException
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
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consultarClasificacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,institucion);
			respuesta=new ResultSetDecorator(ps.executeQuery());				
		}
		catch(SQLException e)
		{
			logger.warn("Error en el listado estratos " +e.toString());
			respuesta=null;
		}
		return respuesta;
	}

	/**
	 * Búsqueda Avanzada de un estrato 
	 * @param con
	 * @param descripcion
	 * @param nombreTipoRegimen
	 * @param activaAux
	 * @param  int, institucion, Codigo de la institucion a la que pertenece el usuario para filtrar la consulta
	 * @return ResulSet con el resultado de la búsqueda
	 * @throws SQLException
	 */
	public static  ResultSetDecorator busqueda(	Connection con,
																int codigo,
																String descripcion,
																String nombreTipoRegimen,
																int activaAux,
																int institucion) throws SQLException
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
				logger.warn("No se pudo realizar la conexión "+e.toString());
				respuesta= null;
			}
		}
		try
		{
			logger.info(">>> Código = "+codigo);
			consultaArmada=armarConsulta(codigo,descripcion,nombreTipoRegimen,activaAux, institucion);
			logger.info(">>> Consulta Armada = "+consultaArmada);
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consultaArmada,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			respuesta=new ResultSetDecorator(ps.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn("Error en la búsqueda avanzada del estrato " +e.toString());
			respuesta=null;
		}
		return respuesta;
	}
																
	/**
	 * Método que arma la consulta según los datos dados por el usuarios en 
	 * la búsqueda avanzada. 
	 * @param  int, codigo del estrato
	 * @param  String, descripcion
	 * @param  String, nombre del tipode regimen
	 * @param  int, activo o no el registro
	 * @param  int, institucion, Codigo de la institucion a la que pertenece el usuario para filtrar la consulta
	 */
	private static String armarConsulta  (	int codigo,
			 																String  descripcion,
																			String nombreTipoRegimen,
																			int activaAux,
																			int institucion)
	{
		String consulta=	"SELECT " +
									"es.codigo AS codigo, " +
									"es.descripcion AS descripcion, " +
									"t.nombre AS tipoRegimen, " +
									"es.activo AS activo " +
									"FROM estratos_sociales es " +
									" INNER JOIN tipos_regimen t " +
									"ON ( es.tipo_regimen = t.acronimo )"+
									"WHERE es.institucion= " + institucion;
		
		if(codigo > 0)
		{
			//consulta = consulta + " AND es.codigo LIKE  '%"+codigo+"%' ";
			/**
			 * Solución Tarea 1698
			 */
			consulta = consulta + " AND es.codigo = "+codigo+" ";
		}
		
		if(nombreTipoRegimen != null && !nombreTipoRegimen.equals(""))
		{
			consulta= consulta + "AND UPPER(t.nombre) LIKE UPPER('%" +nombreTipoRegimen+ "%') ";
		}

		//consulta= consulta + ")";
		 
		if(activaAux==1)
		{
			consulta = consulta + "AND es.activo= "+ValoresPorDefecto.getValorTrueParaConsultas()	;	
		}
		
		if(activaAux==2)
		{
			consulta = consulta + "AND es.activo= "+ValoresPorDefecto.getValorFalseParaConsultas()	;	
		}
		
		if(descripcion != null && !descripcion.equals(""))
		{
			consulta = consulta + " AND UPPER(es.descripcion) LIKE  UPPER('%"+descripcion+"%') ";
		}
		
		consulta= consulta + " ORDER BY es.codigo,t.nombre, es.descripcion";
		
									
		return consulta;
	}
}
