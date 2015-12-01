/*
 * @(#)SqlBaseServiciosCamas1Dao.java
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

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;

/**
 * Implementaci�n sql gen�rico de todas las funciones de acceso a la fuente de datos
 * para el manejo de los servicios camas
 *
 * @version 1.0, Mayo 24 / 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson R�os</a>
 */
public class SqlBaseServiciosCamas1Dao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseServiciosCamas1Dao.class);
    
	/**
	 * Carga los detalles de los servicios de una cama si es de uci
	 */
	private final static String detalleServiciosCamaCasoUciStr=	"SELECT "+
																								"sc.codigo AS codigoTablaServiciosCama, " +
																								"sc.servicio AS codigoServicio, " +
																								"rs.descripcion AS nombreServicio, " +
																								"CASE WHEN sc.tipo_monitoreo IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE sc.tipo_monitoreo END AS codigoTipoMonitoreo, " +
																								"CASE WHEN sc.tipo_monitoreo IS NULL THEN ' ' ELSE tm.nombre END AS nombreTipoMonitoreo " +
																								"FROM servicios_cama sc, " +
																								"referencias_servicio rs, " +
																								"tipo_monitoreo tm "+
																								"WHERE " +
																								"rs.servicio= sc.servicio " +
																								"AND tm.codigo=sc.tipo_monitoreo " +
																								"AND sc.codigo_cama=? " +
																								"AND rs.tipo_tarifario = "+ConstantesBD.codigoTarifarioCups;
	
	/**
	 * Carga los detalles de los servicios de una cama si es de uci
	 */
	private final static String detalleServiciosCamaCasoNoUciStr=	"SELECT "+
																								"sc.codigo AS codigoTablaServiciosCama, " +
																								"sc.servicio AS codigoServicio, " +
																								"rs.descripcion AS nombreServicio " +
																								"FROM servicios_cama sc, " +
																								"referencias_servicio rs " +
																								"WHERE " +
																								"rs.servicio= sc.servicio " +
																								"AND sc.codigo_cama=? " +
																								"AND rs.tipo_tarifario = "+ConstantesBD.codigoTarifarioCups;
	
	/**
	 * Actualiza el codigo del servicio y el tipo de monitoreo
	 */
	private final static String modificarServicioCamaStr="UPDATE servicios_cama SET servicio=?,  tipo_monitoreo=? WHERE codigo=? ";
	
	/**
	 * Elimina un servicio de cama dado su codigo y la institucion
	 */
	private final static String eliminarServicioCamaStr="DELETE FROM servicios_cama WHERE codigo=?";
	
	
	/**
	 * Detalle de los servicios de una cama filtrado por la institucion, numeroCama, habitacion
	 * @param con
	 * @param codigoCama
	 * @return
	 * @throws SQLException
	 */
	public static ResultSetDecorator detalleServiciosCama(Connection con, int codigoCama, boolean esUci) 
	{
	    try
	    {
	        String consulta="";
	        if(esUci)
	            consulta=detalleServiciosCamaCasoUciStr;
	        else
	            consulta=detalleServiciosCamaCasoNoUciStr;
	            
			PreparedStatementDecorator detalleStatement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			detalleStatement.setInt(1, codigoCama);
			return new ResultSetDecorator(detalleStatement.executeQuery());
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+" Error en cargar detalleCama: SqlBaseServiciosCamas1Dao "+e.toString());
	        return null;
	    }
	}
	
	/**
	 * Metodo que modifica un servicio de cama dado el codigo (axioma) del servicio de cama
	 * @param con
	 * @param codigoServicioNuevo
	 * @param tipoMonitoreo
	 * @param codigoSequenciaTablaServicioCama
	 * @return
	 */
	public static boolean modificarServiciosCama(	Connection con, 
																			int codigoServicioNuevo, 
																			int tipoMonitoreo,
																			int codigoSequenciaTablaServicioCama)
																		
	{
		int resp=0;	
		try
		{
			if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexi�n cerrada");
			}
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarServicioCamaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoServicioNuevo);
			if(tipoMonitoreo==ConstantesBD.codigoNuncaValido)
			    ps.setObject(2, null);
			else
			    ps.setInt(2, tipoMonitoreo);
			ps.setInt(3,codigoSequenciaTablaServicioCama);
			
			resp=ps.executeUpdate();
			
			if(resp>0)
			{
			    return true;
			}
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la modificacion de datos: SqlBaseServiciosCamas1Dao "+e.toString());
			resp=0;			
		}	
		return false;	
	}
	
	/**
	 * Metodo que elimina un servicio de cama dado el codigo (axioma) de la tabla servicios cama
	 * @param con
	 * @param codigoTablaServiciosCama
	 * @return
	 */
	public static boolean eliminarServiciosCama(		Connection con, 
																			int codigoTablaServiciosCama)
																		
	{
		int resp=0;	
		try
		{
			if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexi�n cerrada");
			}
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarServicioCamaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoTablaServiciosCama);
			
			resp=ps.executeUpdate();
			
			if(resp>0)
			{
			    return true;
			}
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en elinar datos: SqlBaseServiciosCamas1Dao "+e.toString());
			resp=0;			
		}	
		return false;	
	}
	
	
	/**
	 * Inserta un servicio cama 
	 * @param con
	 * @param codigoCama
	 * @param codigoServicio
	 * @param codigoTipoMonitoreo
	 * @param ingresarServicioCamaStr
	 * @return
	 */
	public static boolean  insertarServicioCama(	Connection con,
																		int codigoCama,
																		int codigoServicio,
																		int codigoTipoMonitoreo, 
																		String ingresarServicioCamaStr)
	{
		int resp=0;
		try
		{
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(ingresarServicioCamaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoCama);
			if(codigoServicio>0)
				ps.setInt(2, codigoServicio);
			else
				ps.setObject(2,null);
			if(codigoTipoMonitoreo>0)
			    ps.setInt(3, codigoTipoMonitoreo);
			else
			    ps.setObject(3, null);
			logger.info("\n\nconsulta::: "+ingresarServicioCamaStr+" cod cama: "+codigoCama+" cod servicio: "+codigoServicio+" tipo monitoreo: "+codigoTipoMonitoreo);			
			resp=ps.executeUpdate();
			if(resp>0)
			    return true;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserci�n de datos: SqlBaseServiciosCamas1Dao "+e.toString() );
			resp=0;
		}
		return false;
	}
	
}