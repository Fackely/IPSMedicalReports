/*
 * @(#)SqlBaseExcepcionesServiciosDao
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
 * para las excepciones de servicios
 *
 * @version 1.0, Julio 16 / 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class SqlBaseExcepcionesServiciosDao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseExcepcionesServiciosDao.class);

	/**
	 *  Inserta una excepcion de servicio
	 */
	private final static String insertarExc = 	"INSERT INTO excepciones_servicios " +
																	"(servicio, contrato ) VALUES ( ?, ? )";
	
	/**
	 * Carga los datos para mostrarlos en el resumen o en el listado o para hacer el filtro de entrada
	 */
	private final static String cargarDatosExc =	"SELECT " +
																		"es.servicio AS codigoServicio, " +
																		"ref.descripcion AS descripcionServicio, " +
																		"es.contrato AS codigoContrato, " +
																		"con.numero_contrato AS numeroContrato,  " +
																		"con.convenio AS codigoConvenio, " +
																		"conv.nombre AS nombreConvenio, " +
																		"s.especialidad AS especialidad,  " +
																		"(s.especialidad ||'-'|| es.servicio) AS codigoAxioma, " +
																		"to_char(con.fecha_inicial,'dd/mm/yyyy') AS fechaInicial, " +
																		"to_char(con.fecha_final,'dd/mm/yyyy') AS fechaFinal," +
																		"CASE WHEN s.espos="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'POS' ELSE 'NOPOS' END AS espos "+
																		"FROM excepciones_servicios es " +
																		"INNER JOIN contratos con ON (es.contrato = con.codigo) " +
																		"INNER JOIN convenios conv ON (con.convenio= conv.codigo) " +
																		"INNER JOIN referencias_servicio ref ON(es.servicio=ref.servicio " +
																		"AND ref.tipo_tarifario= "+ConstantesBD.codigoTarifarioCups+") " +
																		"INNER JOIN servicios s ON (es.servicio= s.codigo) ";

	/**
	 * Modifica el código del servicio dependiendo del contrato y del servicio
	 */
	private final static String modificarExc="UPDATE excepciones_servicios SET servicio = ? WHERE contrato = ? AND servicio = ?";
	
	/**
	 * Borra una excepción de servicio según el contrato y el cod del servicio
	 */
	private final static String borrarExc="DELETE FROM excepciones_servicios WHERE servicio=? AND contrato=?";
	
	/**
	 * Para evitar la repetición de inserciones
	 */
	private final static String verificarRepetidas="SELECT servicio AS servicio, contrato AS contrato FROM excepciones_servicios WHERE servicio=? AND contrato=? ";
	
	/**
	 * Inserción de una excepción de servicio
	 * @param con, connection con la fuente de datos
	 * @param servicio, código del servicio
	 * @param contrato, coigo del contrato
	 * @return 0 -1 (insert)
	 */
	public static int  insertar(		Connection con, int servicio, int contrato)
	{
			int resp=0;
			try
			{
				if (con == null || con.isClosed()) 
				{
					DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
					con = myFactory.getConnection();
				}
				PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarExc,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1,servicio);
				ps.setInt(2, contrato);
				resp=ps.executeUpdate();
			}
			catch(SQLException e)
			{
				logger.warn(e+" Error en la inserción de datos: SqlBaseExcepcionesServiciosDao "+e.toString() );
				resp=0;
			}
			return resp;
	}
	
	/**
	 * Insertar Transaccional
	 * @param con
	 * @param servicio
	 * @param contrato
	 * @param estado
	 * @return
	 */
	public static  ResultadoBoolean insertarTransaccional(	Connection con,
																						int servicio, 
																						int contrato,
																						String estado) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		if( estado != null && estado.equals(ConstantesBD.inicioTransaccion) )
		{
		    if (!myFactory.beginTransaction(con))
		    {
		        return new ResultadoBoolean (false, "No se pudo iniciar Transacción");
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
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarExc,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1,servicio);
				ps.setInt(2,contrato);
				int insert=ps.executeUpdate();	
				
				if( insert == 0 )
				{
					myFactory.abortTransaction(con);
					return new ResultadoBoolean(false," Error en la inserción de datos: SqlBaseExcepcionesServiciosDao ");
				}
			}
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseExcepcionesServiciosDao");
			myFactory.abortTransaction(con);
			return new ResultadoBoolean(false," Error en la inserción de datos: SqlBaseExcepcionesServiciosDao: "+e);
		}
		if( estado != null && estado.equals(ConstantesBD.finTransaccion) )
		{
		    myFactory.endTransaction(con);
		}
		return new ResultadoBoolean(true);		
	}
		
	/**
	 * Método que  carga  los datos de un resumen de excepcion de servicio
	 * en una BD PostgresSQL o Hsqldb 
	 */
	public static ResultSetDecorator cargarResumen(Connection con, int servicio, int contrato) throws SQLException
	{
			PreparedStatementDecorator cargarResumenStatement= new PreparedStatementDecorator(con.prepareStatement(cargarDatosExc+ "WHERE es.servicio = ? AND es.contrato= ? ORDER BY numeroContrato" ,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarResumenStatement.setInt(1, servicio);
			cargarResumenStatement.setInt(2, contrato);
			return new ResultSetDecorator(cargarResumenStatement.executeQuery());
	}
	
	public static ResultSetDecorator hayRepetidas (Connection con, int servicio, int contrato ) 
	{
		try
		{
			PreparedStatementDecorator verificarStatement= new PreparedStatementDecorator(con.prepareStatement(verificarRepetidas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			verificarStatement.setInt(1, servicio);
			verificarStatement.setInt(2, contrato);
			return new ResultSetDecorator(verificarStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de datos repetidos: SqlBaseExcepcionesServiciosDao "+e.toString());
			return null;
		}
	}
	
	/**
	 * Método que elimina una excepcion de servicio  
	 * si el contrato tiene  fechas >= al sistema 
	 */
	public static int eliminar(Connection con, int servicio, int contrato)
	{
		int resp=0;	
		try{
				if (con == null || con.isClosed()) 
				{
					throw new SQLException ("Error SQL: Conexión cerrada");
				}
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(borrarExc,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

				ps.setInt(1,servicio);
				ps.setInt(2,contrato);
				
				ps.executeUpdate();
			}
			catch(SQLException e)
			{
				logger.warn(e+" Error en el borrado de datos: SqlBaseExcepcionesServiciosDao "+e.toString());
				resp=0;			
			}	
			return resp;	
	}

	/**
	 * Modifica el serviio de la excepción de un servicio
	 * @param con
	 * @param servicioNuevo
	 * @param contrato
	 * @param servicioAntiguo
	 * @return
	 */
	public static int modificar(	Connection con, 
											int servicioNuevo, 
											int contrato, 
											int servicioAntiguo)
	{
		int resp=0;	
		try{
				if (con == null || con.isClosed()) 
				{
					throw new SQLException ("Error SQL: Conexión cerrada");
				}
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarExc,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

				ps.setInt(1, servicioNuevo);
				ps.setInt(2,contrato);
				ps.setInt(3,servicioAntiguo);
				
				ps.executeUpdate();
				resp=1;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseExcepcionesServicioDao "+e.toString());
			resp=0;			
		}	
		return resp;	
	}
	
	
	/**
	 * Modificación transaccional
	 * @param con
	 * @param servicioNuevo
	 * @param contrato
	 * @param servicioAntiguo
	 * @param estado
	 * @return ResultadoBoolean
	 */	
	public static ResultadoBoolean modificarTransaccional(	Connection con, 
																						int servicioNuevo, 
																						int contrato, 
																						int servicioAntiguo, 
																						String estado) throws SQLException 
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		ResultadoBoolean resp=new ResultadoBoolean(false);
	
		if( estado != null && estado.equals(ConstantesBD.inicioTransaccion) )
		{
		    if (!myFactory.beginTransaction(con))
		    {
		        return new ResultadoBoolean (false, "No se pudo iniciar Transacción");
		    }
		}	
		try
		{
			if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarExc,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, servicioNuevo);
			ps.setInt(2,contrato);
			ps.setInt(3,servicioAntiguo);
			
			ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseExcepcionesServicioDao");
			myFactory.abortTransaction(con);
			resp=new ResultadoBoolean(false," Error en la inserción de datos: SqlBaseExcepcionesServicioDao: "+e);
			return resp;
		}
		if( estado != null && estado.equals(ConstantesBD.finTransaccion) )
		{
	    	myFactory.endTransaction(con);
		}
		return new ResultadoBoolean(true);
	}
	
	/**
	 * Método que contiene el Resulset de los datos de la tabla excepciones_servicios
	 * para posteriorm/t ser mostrados en el pager
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @return Resultset con todos los datos de la tabla excepciones_servicios
	 * @throws SQLException
	 */
	public static  ResultSetDecorator listado(Connection con) throws SQLException
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
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(cargarDatosExc+ " ORDER BY numeroContrato",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			respuesta=new ResultSetDecorator(ps.executeQuery());				
		}
		catch(SQLException e)
		{
			logger.warn("Error en el listado excepciones servicios " +e.toString());
			respuesta=null;
		}
		return respuesta;
	}
	
	/**
	 * Método que contiene el Resulset de todas las excepciones de servicios buscados
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @return Resultset con todos los datos de la tabla empresas
	 * @throws SQLException
	 */
	public static  ResultSetDecorator busqueda(	Connection con, 
														String nombreConvenio, 
														String descripcionServicio,
														String numeroContrato,
														int esposAux
														) throws SQLException
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
			consultaArmada=armarConsulta(	 nombreConvenio, descripcionServicio, numeroContrato, esposAux);
																	 
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consultaArmada,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			respuesta=new ResultSetDecorator(ps.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn("Error en la búsqueda avanzada de la excepción de servicio" +e.toString());
			respuesta=null;
		}
		return respuesta;
	}

	/**
	 * Método que arma la consulta según los datos dados por el usuarios en 
	 * la búsqueda avanzada. 
	 */
	private static String armarConsulta  (	String nombreConvenio, 
															String descripcionServicio,
															String numeroContrato,
															int esposAux)
	{
		String consulta=	" SELECT es.servicio AS codigoServicio, " +
												"ref.descripcion AS descripcionServicio, " +
												"con.numero_contrato AS numeroContrato,  " +
												"conv.nombre AS nombreConvenio, " +
												"s.especialidad AS especialidad,  " +
												"(s.especialidad ||'-'|| es.servicio) AS codigoAxioma, " +
												"to_char(con.fecha_inicial,'dd/mm/yyyy') AS fechaInicial, " +
												"to_char(con.fecha_final,'dd/mm/yyyy') AS fechaFinal, " +
												"CASE WHEN s.espos="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'POS' ELSE 'NOPOS' END AS espos "+
												"FROM excepciones_servicios es " +
												"INNER JOIN contratos con ON (es.contrato = con.codigo ";
										
		if(numeroContrato != null && !numeroContrato.equals(""))
		{
			consulta = consulta + "AND UPPER(con.numero_contrato) LIKE  UPPER('%"+numeroContrato+"%')  ";
		}

		consulta= consulta + ") INNER JOIN convenios conv ON (con.convenio= conv.codigo ";

		if(nombreConvenio != null && !nombreConvenio.equals(""))
		{
			consulta = consulta + "AND UPPER(conv.nombre) LIKE  UPPER('%"+nombreConvenio+"%')  ";
		}
		
		consulta = consulta + ") INNER JOIN referencias_servicio ref ON(es.servicio=ref.servicio AND ref.tipo_tarifario=0 "; 			
		
		if(descripcionServicio!=null && !descripcionServicio.equals(""))
		{
			consulta= consulta + "AND UPPER(ref.descripcion) LIKE UPPER('%"+descripcionServicio+"%') ";
		}
		
		consulta= consulta + ") INNER JOIN servicios s ON (es.servicio= s.codigo  ";
		
		if(esposAux==1)
		{
		    consulta= consulta+ "AND s.espos="+ValoresPorDefecto.getValorTrueParaConsultas();
		}
		else if(esposAux==2)
		{
		    consulta= consulta+ "AND s.espos="+ValoresPorDefecto.getValorFalseParaConsultas()+" ";
		}
		
		consulta= consulta+" ) ORDER BY numeroContrato ";
		return consulta;	
	}	
}
