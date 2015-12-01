/*
 * @(#)SqlBaseCIEDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFecha;

import com.princetonsa.dao.DaoFactory;

/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 * para un la vigencia de Disagnósticos
 *
 * @version 1.0, Agosto 17 / 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class SqlBaseCIEDao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseCIEDao.class);
	
	/**
	 * Carga los datos para mostrarlos en el resumen
	 */
	private final static String cargarDatosCIEStr = "SELECT codigo AS codigo, codigo_real AS codigoReal, vigencia AS vigencia FROM tipos_cie WHERE codigo = ? ";
	
	/**
	 * Hace la modificación de los datos del convenio
	 */
	private final static String modificarCIEStr=	"UPDATE tipos_cie SET vigencia = ? WHERE codigo = ? ";
	
	/**
	 * Seleccionar todos los datos de la tabla tipos_cie para mostrarlos en el listado
	 */
	private final static String listadoCIEStr= "SELECT codigo AS codigo, codigo_real AS codigoReal, vigencia AS vigencia FROM tipos_cie WHERE codigo > 0 ORDER BY vigencia"; 
	
	/**
	 * Borra una vigencia de diagnóstico 
	 */
	private final static String borrarCIEStr="DELETE FROM tipos_cie WHERE codigo = ?";

	/**
	 * Inserta una vigencia de diagnóstico
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param codigoReal, String, para capturar el tipo de CIE
	 * @param vigencia, String, para ingresarl la fecha desde la cuál estará vigente el CIE
	 * @return int, 0 si  no inserta, 1 si inserta
	 */
	public static int  insertar(	Connection con,
											String codigoReal,
											String vigencia,
											String insertarCIEStr)
	{
		int resp=0;
		try
			{
					if (con == null || con.isClosed()) 
					{
							DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
							con = myFactory.getConnection();
					}
					PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarCIEStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setString(1,codigoReal );
					ps.setString(2,UtilidadFecha.conversionFormatoFechaABD(vigencia));
					
					resp=ps.executeUpdate();
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción de datos: SqlBaseCIEDao "+e.toString() );
					resp=0;
			}
			return resp;
	}

	/**
	 * Inserta una vigencia de diagnóstico En una transacción dado su estado
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param codigoReal, String, para capturar el tipo de CIE
	 * @param vigencia, String, para ingresarl la fecha desde la cuál estará vigente el CIE
	 * @param estado. String, estado dentro de la transacción
	 * @return  ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
	 * 				de lo contrario
	 */
	public static  ResultadoBoolean insertarTransaccional(	Connection con,
																						String codigoReal,
																						String vigencia,
																						String estado, String insertarCIEStr) throws SQLException 
	{
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
				//Como es transaccional NO voy a tratar de abrir una nueva conexión, sino que mandaré una excepction
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			else
			{	
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarCIEStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setString(1,codigoReal );
				ps.setString(2,UtilidadFecha.conversionFormatoFechaABD(vigencia));
				
				int insert=ps.executeUpdate();	
				
				if( insert == 0 )
				{
					myFactory.abortTransaction(con);
					return new ResultadoBoolean(false," Error en la inserción de datos: PostgresCIEDao: ");		
				}
			}
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseCIEDao");
			myFactory.abortTransaction(con);
			return new ResultadoBoolean(false," Error en la inserción de datos: PostgresCIEDao: "+e);		
		}
		if( estado != null && estado.equals(ConstantesBD.finTransaccion) )
		{
		    myFactory.endTransaction(con);
		}
		return new ResultadoBoolean(true);
	}
	
	/**
	 * Método que  carga  los datos de la vigencia de diagnóstico según el  
	 * código  que llega de la tabla tipos_cie para mostrarlos en el resumen
	 * en una BD PostgresSQL o Hsqldb 
	 */
	public static ResultSetDecorator cargarResumen(Connection con, int codigo) 
	{
		try
		{
			PreparedStatementDecorator cargarResumenStatement= new PreparedStatementDecorator(con.prepareStatement(cargarDatosCIEStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarResumenStatement.setInt(1, codigo);
			return new ResultSetDecorator(cargarResumenStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en cargar los datos: SqlBaseCIEDao  :"+e.toString()+" --- Con el código" +codigo);
			return null;			
		}
	}
	
	/**
	 * Método que  Verifica la existencia de un tipo de CIE (codigo_real), ya que no pueden estar duplicados
	 * en una BD PostgresSQL o Hsqldb 
	 */
	public static ResultSetDecorator existeTipoCIE(Connection con, String codigoReal) 
	{
		String existeTipoCIEStr= "SELECT codigo_real FROM tipos_cie WHERE UPPER(codigo_real) = UPPER('"+codigoReal+"')";
		
		try
		{
			PreparedStatementDecorator existeStatement= new PreparedStatementDecorator(con.prepareStatement(existeTipoCIEStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return new ResultSetDecorator(existeStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en consulta datos: SqlBaseCIEDao  :"+e.toString()+" - Con el código " +codigoReal);
			return null;			
		}
	}
	
	/**
	 * Método que  Verifica la existencia de una fecha inicio vigencia de CIE, ya que no pueden estar duplicados
	 * en una BD PostgresSQL o Hsqldb 
	 */
	public static ResultSetDecorator existeFechaInicioVigenciaCIE(Connection con, String vigencia) 
	{
		String existeFechaStr= "SELECT codigo_real FROM tipos_cie WHERE vigencia = ?";
		
		try
		{
			PreparedStatementDecorator existeStatement= new PreparedStatementDecorator(con.prepareStatement(existeFechaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			existeStatement.setDate(1,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vigencia)));
			return new ResultSetDecorator(existeStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en consulta datos: SqlBaseCIEDao  :"+e.toString()+"- Con la fecha vigencia " +vigencia);
			return null;			
		}
	}	
	
	
	/**
	 * Modifica una vigencia de diagnóstico
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param codigo, int, para capturar el código de la tabla
	 * @param vigencia, String, para ingresar la fecha desde la cuál estará vigente el CIE
	 * @return int, 0 si  no inserta, 1 si inserta
	 */
	public static int modificar(	Connection con, 
											int codigo,
											String vigencia ) 
	{
		int resp=0;	
		try
		{
				if (con == null || con.isClosed()) 
				{
					throw new SQLException ("Error SQL: Conexión cerrada");
				}
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarCIEStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

				ps.setString(1, UtilidadFecha.conversionFormatoFechaABD(vigencia));
				ps.setInt(2,codigo);
			
				resp=ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseCIEDao "+e.toString());
			resp=0;			
		}	
		return resp;	
	}

	/**
	 * Modifica una vigencia de diagnóstico en una transacción según su estado
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param codigo, int, para capturar el código de la tabla
	 * @param vigencia, String, para ingresar la fecha desde la cuál estará vigente el CIE
	 * @param estado. String, estado dentro de la transacción 
	 * @return int, 0 si  no inserta, 1 si inserta
	 */
	public static ResultadoBoolean modificarTransaccional(	Connection con, 
																						int codigo,
																						String vigencia,
																						String estado) throws SQLException
	{
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
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarCIEStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			ps.setString(1, UtilidadFecha.conversionFormatoFechaABD(vigencia));
			ps.setInt(2,codigo);

			ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la modificación de datos: SqlBaseCIEDao");
			myFactory.abortTransaction(con);
			return new ResultadoBoolean(false," Error en la modificación de datos: SqlBaseCIEDao: "+e);
		}
		if( estado != null && estado.equals(ConstantesBD.finTransaccion) )
		{
		    myFactory.endTransaction(con);
		}
		return new ResultadoBoolean(true);
	}
	
	/**
	 * Método que borra una vigencia de diagnóstico según su código
	 */
	public static int borrarCIE(Connection con, int codigo)
	{
		int resp=0;	
		try{
				if (con == null || con.isClosed()) 
				{
					throw new SQLException ("Error SQL: Conexión cerrada");
				}
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(borrarCIEStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

				ps.setInt(1,codigo);
				
				resp=ps.executeUpdate();
			}
			catch(SQLException e)
			{
				logger.warn(e+" Error en el borrado de datos: SqlBaseCIEDao "+e.toString());
				resp=0;			
			}	
			return resp;	
	}
	
	
	/**
	 * Método que contiene el Resulset de los datos de la tabla tipos_cie
	 * para posteriorm/t ser mostrados en el pager
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @return Resultset con todos los datos de la tabla tipos_cie
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
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(listadoCIEStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			respuesta=new ResultSetDecorator(ps.executeQuery());				
		}
		catch(SQLException e)
		{
			logger.warn("Error en el listado vigencia de diagnósticos " +e.toString());
			respuesta=null;
		}
		return respuesta;
	}
}
