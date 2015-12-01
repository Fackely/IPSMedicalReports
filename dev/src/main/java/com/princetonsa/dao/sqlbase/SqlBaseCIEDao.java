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
 * Implementaci�n sql gen�rico de todas las funciones de acceso a la fuente de datos
 * para un la vigencia de Disagn�sticos
 *
 * @version 1.0, Agosto 17 / 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson R�os</a>
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
	 * Hace la modificaci�n de los datos del convenio
	 */
	private final static String modificarCIEStr=	"UPDATE tipos_cie SET vigencia = ? WHERE codigo = ? ";
	
	/**
	 * Seleccionar todos los datos de la tabla tipos_cie para mostrarlos en el listado
	 */
	private final static String listadoCIEStr= "SELECT codigo AS codigo, codigo_real AS codigoReal, vigencia AS vigencia FROM tipos_cie WHERE codigo > 0 ORDER BY vigencia"; 
	
	/**
	 * Borra una vigencia de diagn�stico 
	 */
	private final static String borrarCIEStr="DELETE FROM tipos_cie WHERE codigo = ?";

	/**
	 * Inserta una vigencia de diagn�stico
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param codigoReal, String, para capturar el tipo de CIE
	 * @param vigencia, String, para ingresarl la fecha desde la cu�l estar� vigente el CIE
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
					logger.warn(e+" Error en la inserci�n de datos: SqlBaseCIEDao "+e.toString() );
					resp=0;
			}
			return resp;
	}

	/**
	 * Inserta una vigencia de diagn�stico En una transacci�n dado su estado
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param codigoReal, String, para capturar el tipo de CIE
	 * @param vigencia, String, para ingresarl la fecha desde la cu�l estar� vigente el CIE
	 * @param estado. String, estado dentro de la transacci�n
	 * @return  ResultadoBoolean, true si la inserci�n fue exitosa, false y con la descripci�n 
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
		        return new ResultadoBoolean(false, "No se pudo iniciar la transacci�n");
		    }
		}
		try
		{
			if (con == null || con.isClosed()) 
			{
				//Como es transaccional NO voy a tratar de abrir una nueva conexi�n, sino que mandar� una excepction
				throw new SQLException ("Error SQL: Conexi�n cerrada");
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
					return new ResultadoBoolean(false," Error en la inserci�n de datos: PostgresCIEDao: ");		
				}
			}
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserci�n de datos: SqlBaseCIEDao");
			myFactory.abortTransaction(con);
			return new ResultadoBoolean(false," Error en la inserci�n de datos: PostgresCIEDao: "+e);		
		}
		if( estado != null && estado.equals(ConstantesBD.finTransaccion) )
		{
		    myFactory.endTransaction(con);
		}
		return new ResultadoBoolean(true);
	}
	
	/**
	 * M�todo que  carga  los datos de la vigencia de diagn�stico seg�n el  
	 * c�digo  que llega de la tabla tipos_cie para mostrarlos en el resumen
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
			logger.warn(e+" Error en cargar los datos: SqlBaseCIEDao  :"+e.toString()+" --- Con el c�digo" +codigo);
			return null;			
		}
	}
	
	/**
	 * M�todo que  Verifica la existencia de un tipo de CIE (codigo_real), ya que no pueden estar duplicados
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
			logger.warn(e+" Error en consulta datos: SqlBaseCIEDao  :"+e.toString()+" - Con el c�digo " +codigoReal);
			return null;			
		}
	}
	
	/**
	 * M�todo que  Verifica la existencia de una fecha inicio vigencia de CIE, ya que no pueden estar duplicados
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
	 * Modifica una vigencia de diagn�stico
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param codigo, int, para capturar el c�digo de la tabla
	 * @param vigencia, String, para ingresar la fecha desde la cu�l estar� vigente el CIE
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
					throw new SQLException ("Error SQL: Conexi�n cerrada");
				}
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarCIEStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

				ps.setString(1, UtilidadFecha.conversionFormatoFechaABD(vigencia));
				ps.setInt(2,codigo);
			
				resp=ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserci�n de datos: SqlBaseCIEDao "+e.toString());
			resp=0;			
		}	
		return resp;	
	}

	/**
	 * Modifica una vigencia de diagn�stico en una transacci�n seg�n su estado
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param codigo, int, para capturar el c�digo de la tabla
	 * @param vigencia, String, para ingresar la fecha desde la cu�l estar� vigente el CIE
	 * @param estado. String, estado dentro de la transacci�n 
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
		        return new ResultadoBoolean(false, "No se pudo iniciar la transacci�n");
		    }
		}	
		try
		{
			if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexi�n cerrada");
			}
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarCIEStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			ps.setString(1, UtilidadFecha.conversionFormatoFechaABD(vigencia));
			ps.setInt(2,codigo);

			ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la modificaci�n de datos: SqlBaseCIEDao");
			myFactory.abortTransaction(con);
			return new ResultadoBoolean(false," Error en la modificaci�n de datos: SqlBaseCIEDao: "+e);
		}
		if( estado != null && estado.equals(ConstantesBD.finTransaccion) )
		{
		    myFactory.endTransaction(con);
		}
		return new ResultadoBoolean(true);
	}
	
	/**
	 * M�todo que borra una vigencia de diagn�stico seg�n su c�digo
	 */
	public static int borrarCIE(Connection con, int codigo)
	{
		int resp=0;	
		try{
				if (con == null || con.isClosed()) 
				{
					throw new SQLException ("Error SQL: Conexi�n cerrada");
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
	 * M�todo que contiene el Resulset de los datos de la tabla tipos_cie
	 * para posteriorm/t ser mostrados en el pager
	 * @param con, Connection, conexi�n abierta con una fuente de datos
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
				logger.warn("No se pudo realizar la conexi�n "+e.toString());
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
			logger.warn("Error en el listado vigencia de diagn�sticos " +e.toString());
			respuesta=null;
		}
		return respuesta;
	}
}
