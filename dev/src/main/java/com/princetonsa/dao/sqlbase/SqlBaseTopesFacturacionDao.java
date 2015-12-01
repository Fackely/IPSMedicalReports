/*
 * @(#)SqlBaseTopesFacturacionDao
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
import java.sql.PreparedStatement;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFecha;


import com.princetonsa.actionform.cargos.TopesFacturacionForm;
import com.princetonsa.dao.DaoFactory;

/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 * para los topes de facturación
 *
 * @version 1.0, Julio 12 / 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class SqlBaseTopesFacturacionDao
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseTopesFacturacionDao.class);
	
	/**
	 * Carga los datos para mostrarlos en el resumen o en el listado
	 */
	private final static String cargarDatosTope =	"SELECT " +
																		"tf.codigo AS codigo, " +
																		"tf.tipo_regimen AS acronimoTipoRegimen," +
																		"t.nombre AS nombreTipoRegimen," +
																		"tf.estrato_social AS codigoEstratoSocial, " +
																		"es.descripcion AS nombreEstratoSocial, " +
																		"tf.tipo_monto AS codigoTipoMonto, " +
																		"tm.nombre AS nombreTipoMonto, " +
																		"tf.tope_evento || '' AS topeEvento," +
																		"tf.tope_anio_calendario || '' AS topeAnioCalendario, " +
																		"tf.institucion AS codigoInstitucion, " +
																		"to_char(tf.vigencia_inicial,'dd/MM/yyyy') AS vigenciaInicial, " +
																		"ins.razon_social AS nombreInstitucion " +
																		" FROM topes_facturacion tf " +
																		"INNER JOIN tipos_regimen t ON (tf.tipo_regimen=t.acronimo) " +
																		"INNER JOIN estratos_sociales es ON (tf.estrato_social=es.codigo) " +
																		"INNER JOIN tipos_monto tm ON (tf.tipo_monto = tm.codigo) " +
																		"INNER JOIN instituciones ins ON (tf.institucion = ins.codigo) " ;
																		//"WHERE  tf.codigo=  ? " ;
	
	private final static String cargarCodigoUltimaInsercion = "SELECT MAX(codigo) AS codigo from topes_facturacion";
	
	/**
	 * Hace la modificación de los datos del tope de la facturación 
	 */
	private final static String modificarTope	=	"UPDATE " +
																		"topes_facturacion SET " +
																		"tope_evento = ?, " +
																		"tope_anio_calendario = ?, " +
																		"vigencia_inicial = ? " +
																		" WHERE " +
																		" codigo = ? ";
	
	/**
	 * Borra un tope de facturación
	 */
	private final static String borrarTope="DELETE FROM topes_facturacion WHERE codigo=? ";
	
	/**
	 * Sentencia para consultar los topes por evento y año calendario
	 * pasando como parámetros el tipo de regimen,
	 * la clasificación socioeconómica (estrato social) y el tipo de monto
	 */
	private static final String cargarTopesParaFacturacionStr="SELECT codigo FROM topes_facturacion WHERE tipo_regimen=? AND estrato_social=? AND tipo_monto=? AND vigencia_inicial<=? order by vigencia_inicial desc ";
	
	/**
	 * Inserta un tope de facturación
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param tipoRegimen, tipo de régimen
	 * @param estrato, clasificación socioEconómica
	 * @param tipoMonto, tipo de monto
	 * @param topeEvento, tope por evento
	 * @param topeAnioCalendario, tope por anio calendario
	 * @param institucion, institucion
	 * @return 0 o 1 si inserta
	 * @throws SQLException
	 */
	public static int  insertar(		Connection con,
												String tipoRegimen,
												int estrato, 
												int tipoMonto,
												double topeEvento,
												double topeAnioCalendario,
												int institucion,
												java.util.Date fechaVigenciaInicial,
												String insertarTopeStr) throws SQLException
	{
		int resp=0;

					if (con == null || con.isClosed()) 
					{
							DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
							con = myFactory.getConnection();
					}
					//PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarTopeStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					PreparedStatement ps= null;					
					try {
						ps = con.prepareStatement(insertarTopeStr);
						ps.setString(1, tipoRegimen);
						ps.setInt(2, estrato);
						ps.setInt(3,tipoMonto);
						ps.setDouble(4,topeEvento);
						ps.setDouble(5,topeAnioCalendario);
						ps.setInt(6,institucion);
						ps.setDate(7, new java.sql.Date(fechaVigenciaInicial.getTime()));
						resp=ps.executeUpdate();
					} 
					finally{
						if(ps != null){
							try {
								ps.close();
							} catch (Exception e) {}							
						}
					}

			return resp;
	}
	

	/**
	 *  Inserta un tope de facturación  de una transacción dado su estado.
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param tipoRegimen, tipo de régimen
	 * @param estrato, clasificación socioEconómica
	 * @param tipoMonto, tipo de monto
	 * @param topeEvento, tope por evento
	 * @param topeAnioCalendario, tope por anio calendario
	 * @param institucion, institucion
	 * @param estado
	 * @return  ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
	 * 				de lo contrario
	 */
	public static  ResultadoBoolean insertarTransaccional(	Connection con,
																						String tipoRegimen,
																						int estrato, 
																						int tipoMonto,
																						float topeEvento,
																						float topeAnioCalendario,
																						int institucion,
																						String estado,
																						String insertarTopeStr) throws SQLException
	{
		ResultadoBoolean resp=new ResultadoBoolean(false);
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		if( estado != null && estado.equals(ConstantesBD.inicioTransaccion) )
		{
		    if (!myFactory.beginTransaction(con))
		    {
		        return new ResultadoBoolean(false, "No se inicio bien la transacción");
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
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarTopeStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setString(1, tipoRegimen);
				ps.setInt(2, estrato);
				ps.setInt(3,tipoMonto);
				ps.setFloat(4,topeEvento);
				ps.setFloat(5,topeAnioCalendario);
				ps.setInt(6,institucion);
				int insert=ps.executeUpdate();	
				
				if( insert == 0 )
				{
					myFactory.abortTransaction(con);
					resp=new ResultadoBoolean(false," Error en la inserción de datos: SqlBaseTopeFacturacionDao: ");
				}
			}
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseTopeFacturacionDao");
			myFactory.abortTransaction(con);
			resp=new ResultadoBoolean(false," Error en la inserción de datos: SqlBaseTopeFacturacionDao: "+e);
			return resp;		
		}
		if( estado != null && estado.equals(ConstantesBD.finTransaccion) )
		{
		    myFactory.endTransaction(con);
		}
		return resp =new ResultadoBoolean(true);		
	}
	
	/**
	 * Método que  carga  los datos de un tope de facturación 
	 * según los datos que lleguen del  código de la tabla topes_facturacion 
	 * para mostrarlos en el resumen
	 * en una BD PostgresSQL o Hsqldb 
	 */
	public static ResultSetDecorator cargarResumen(Connection con, int codigo) throws SQLException
	{
				
			PreparedStatementDecorator cargarResumenStatement= new PreparedStatementDecorator(con.prepareStatement(cargarDatosTope +"WHERE  tf.codigo=  ? ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet) );
			cargarResumenStatement.setInt(1, codigo);
			return new ResultSetDecorator(cargarResumenStatement.executeQuery());
	}

	/**Carga el tope de facturación insertado**/
	public static ResultSetDecorator cargarUltimoCodigo(Connection con)
	{
		try
		{
			PreparedStatementDecorator cargarUltimoStatement= new PreparedStatementDecorator(con.prepareStatement(cargarCodigoUltimaInsercion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return  new ResultSetDecorator(cargarUltimoStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta del último código del tope: SqlBaseTopesFacturacionDao "+e.toString());
			return null;
		}
	}
	
	/**
	 * Modificar un tope de facturación
	 * @param con, Connection, conexión abierta con una fuente de datos+
	 * @param topeEvento,
	 * @param topeAnioCalendario
 	 * @return int, 0 si  no inserta, 1 si inserta
	 */
	public static int modificar(	Connection con, 
											int codigo,
											double topeEvento,
											double topeAnioCalendario,
											String fechaVigenciaInicial) 
	{
		int resp=0;	
		try{	
			if (con == null || con.isClosed()) 
				{
					throw new SQLException ("Error SQL: Conexión cerrada");
				}
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarTope,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setDouble(1,topeEvento);
				ps.setDouble(2,topeAnioCalendario);
				ps.setString(3, UtilidadFecha.conversionFormatoFechaABD(fechaVigenciaInicial));
				ps.setInt(4,codigo);
				
				
				ps.executeUpdate();
				resp=1;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la MODIFICACIÓN de datos: SqlBaseTopesFacturacionDao "+e.toString());
			resp=0;			
		}	
		return resp;	
	}

	/**
	 * Modificar un tope de facturación en una transacción
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param topeEvento,
	 * @param topeAnioCalendario
 	 * @param estado. String, estado dentro de la transacción 
	 * @return  ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
	 * 				de lo contrario
	 */
	public static ResultadoBoolean modificarTransaccional(	Connection con, 
																						int codigo,
																						float topeEvento,
																						float topeAnioCalendario,
																						String estado)throws SQLException 
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		ResultadoBoolean resp=new ResultadoBoolean(false);
	
		if( estado != null && estado.equals(ConstantesBD.inicioTransaccion) )
		{
		    if (!myFactory.beginTransaction(con))
		    {
		        return new ResultadoBoolean(false, "No se inicio bien la transacción");
		    }
		}	
		try
		{
			if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarTope,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			ps.setFloat(1,topeEvento);
			ps.setFloat(2,topeAnioCalendario);
			ps.setInt(3,codigo);
			
			ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseTopesFacturacionDao");
			myFactory.abortTransaction(con);
			resp=new ResultadoBoolean(false," Error en la inserción de datos: SqlBaseTopesFacturacionDao: "+e);
			return resp;
		}
		if( estado != null && estado.equals(ConstantesBD.finTransaccion) )
		{
		    myFactory.endTransaction(con);
		}
		return new ResultadoBoolean(true);
	}	
	
	/**
	 * Método que contiene el Resulset de los datos de la tabla topes_facturacion
	 * para posteriorm/t ser mostrados en el pager
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @return Resultset con todos los datos de la tabla estratos_sociales
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
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(cargarDatosTope,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("cargarDatosTope-->"+cargarDatosTope);
			respuesta=new ResultSetDecorator(ps.executeQuery());				
		}
		catch(SQLException e)
		{
			logger.warn("Error en el listado topes " +e.toString());
			respuesta=null;
		}
		return respuesta;
	}

	/**
	 * Método que elimina un tope de facturación  
	 */
	public static int eliminar(Connection con, int codigo)
	{
		int resp=1;	
		try{
				if (con == null || con.isClosed()) 
				{
					throw new SQLException ("Error SQL: Conexión cerrada");
				}
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(borrarTope,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1,codigo);
				ps.executeUpdate();
			}
			catch(SQLException e)
			{
				logger.warn(e+" Error en el borrado de datos: SqlBaseTopesFacturacionDao "+e.toString());
				resp=0;			
			}	
			return resp;	
	}
	
	/**
	 * 
	 * Carga codigo
	 * @param con
	 * @param tipoRegimen
	 * @param estratoSocial
	 * @param tipoMonto
	 * @return 
	 */
	public static int cargarTopeParaFacturacion(Connection con, String tipoRegimen, int estratoSocial, int tipoMonto, String fechaVigencia)
	{
		int retorna=ConstantesBD.codigoNuncaValido;
		try
		{
			PreparedStatementDecorator consultar= new PreparedStatementDecorator(con.prepareStatement(cargarTopesParaFacturacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("cargarTopesParaFacturacionStr->"+cargarTopesParaFacturacionStr+"  tiporeg->"+tipoRegimen+" estrato->"+estratoSocial+" tipoMonto->"+tipoMonto+" fechaVigencia->"+fechaVigencia);
			consultar.setString(1, tipoRegimen);
			consultar.setInt(2, estratoSocial);
			consultar.setInt(3, tipoMonto);
			consultar.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaVigencia)));
			
			ResultSetDecorator topesRs=new ResultSetDecorator(consultar.executeQuery());
			if(topesRs.next())
			{
				retorna= topesRs.getInt(1);
			}
			topesRs.close();
			consultar.close();
		}
		catch (SQLException e)
		{
			logger.error("Error consultando los topes para facturación "+e);
		}
		return retorna;
	}

	
	public static  ResultSetDecorator listadoPosteriores(Connection con) throws SQLException
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
			TopesFacturacionForm topes=new TopesFacturacionForm();
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cargarDatosTope+" WHERE tf.vigencia_inicial > current_date",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	
			respuesta=new ResultSetDecorator(ps.executeQuery());				
		}
		catch(SQLException e)
		{
			logger.warn("Error en el listado topes " +e.toString());
			respuesta=null;
		}
		return respuesta;
	}
}
