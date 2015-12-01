/*
 * @(#)SqlBaseMotivoAnulacionFacturasDao.java
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
import util.ValoresPorDefecto;
import com.princetonsa.dao.DaoFactory;

/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 * para los motivos de anulacion de facturas
 *
 * @version 1.0, May 05 / 2005
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 */
public class SqlBaseMotivoAnulacionFacturasDao 
{
    
    /**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseMotivoAnulacionFacturasDao.class);

	/**
	 * Hace la modificación de los datos de los motivos de anulacion de facturas
	 */
	private final static String modificarMotivosAnulacionFacturasStr=" UPDATE motivos_anul_fact SET " +
															  		 " descripcion = ?, activo = ? WHERE codigo = ?";
	
	
	
	
	/**
	 * Inserta un motivo de anulacion de facturas dependiendo de la institución
	 * @param con
	 * @param descripcion
	 * @param activo
	 * @param institucion
	 * @return
	 */
	public static int  insertar(Connection con, String descripcion, boolean activo, int codigoInstitucion, String insertarMotivoStr)
	{
	   int resp=0;
		try
			{
					if (con == null || con.isClosed()) 
					{
							DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
							con = myFactory.getConnection();
					}
					PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarMotivoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setString(1,descripcion);
					ps.setBoolean(2,activo);
					ps.setInt(3, codigoInstitucion);
	
					resp=ps.executeUpdate();
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción de datos: SqlBaseMotivoAnulacionFacturasDao "+e.toString() );
					resp=0;
			}
			return resp;
	}
	
	/**
	 * Consulta la información de los motivos de anulacion de facturas según la institución
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public static ResultSetDecorator cargarMotivosAnulacion(Connection con, int codigoInstitucion, String consultaStandarMotivosAnulacionFacturasStr)
	{
		try
		{
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consultaStandarMotivosAnulacionFacturasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setInt(1, codigoInstitucion);
			
			return new ResultSetDecorator(cargarStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de la descripción de motivos anulacion de facturas: SqlBaseMotivoAnulacionFacturasDao "+e.toString());
			return null;
		}
	}
	
	/**
	 * Modifica un motivo de anulacion de facturas dado su codigo
	 * @param con
	 * @param descripcion
	 * @param activo
	 * @param codigo
	 * @return
	 */
	public static int modificar(Connection con, String descripcion, boolean activo, int codigo) 
	{
		int resp=0;	
		try{
				if (con == null || con.isClosed()) 
				{
					throw new SQLException ("Error SQL: Conexión cerrada");
				}
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarMotivosAnulacionFacturasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

				ps.setString(1, descripcion);
				ps.setBoolean(2, activo);
				ps.setInt(3, codigo);
			
				resp=ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la modificación de datos: SqlBaseMotivoAnulacionFacturasDao "+e.toString());
			resp=0;			
		}	
		return resp;	
	}

	
	/**
	 * Consulta la información motivos de anulacion de facturas según la institución
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public static ResultSetDecorator cargarMotivos(Connection con, int codigoInstitucion, String consultaStandarMotivosAnulacionFacturasStr)
	{
		try
		{
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consultaStandarMotivosAnulacionFacturasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setInt(1, codigoInstitucion);
			
			return new ResultSetDecorator(cargarStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de la descripción de motivos de anulacion de facturas: SqlBaseMotivoAnulacionFacturasDao "+e.toString());
			return null;
		}
	}
	
	
}
