/*
 * @(#)SqlBaseDevolucionAFarmaciaDao.java
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
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dao.DaoFactory;

/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 * para la devolución a Farmacia
 *
 * @version 1.0, Septiembre 29 / 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class SqlBaseDevolucionAFarmaciaDao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseDevolucionAFarmaciaDao.class);

	/**
	 * Obtiene el último cod de la sequence para poder hacer la inserción del DETALLE de la devolucion
	 */
	private final static String ultimaSequenciaDevolucionStr= "SELECT MAX(codigo) AS sequenciaDevolucion FROM devolucion_med ";
	
	/**
	 * indica si existe o no una devolucion para un articulo - numero Solicitud especificos
	 */
	private final static String existeDevolucionParaNumeroSolicitudArticuloStr="select codigo from detalle_devol_med where numero_solicitud= ? and articulo=? and cantidad>0 "; 
	
	
	/**
	 * Inserta una devolución de medicamentos
	 * @param con, Connection
	 * @param motivo, String
	 * @param fecha, String
	 * @param hora, String
	 * @param usuario, String
	 * @param estadoDevolucion, int
	 * @return int (0 -ultimoCodigoSequence) 
	 */
	public static int insertarDevolucionMedicamentos		(	Connection con, String observaciones,  
	        																			String fecha, String hora, 
																						String usuario, int estadoDevolucion, 
																						int centroCostoDevuelve, int farmacia, int tipoDevolucion, String insertarDevolucionMedicamentosStr,
                                                                                        String motivo, int institucion)
	{
	    int resp=0;
	    
	    try
		{		
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}

			
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarDevolucionMedicamentosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("-->"+insertarDevolucionMedicamentosStr);
			logger.info("-->"+observaciones);
			logger.info("-->"+fecha);
			logger.info("-->"+hora);
			logger.info("-->"+usuario);
			logger.info("-->"+estadoDevolucion);
			logger.info("-->"+centroCostoDevuelve);
			logger.info("-->"+farmacia);
			logger.info("-->"+tipoDevolucion);
			logger.info("-->"+motivo);
			logger.info("-->"+institucion);
			ps.setString(1,observaciones);
			ps.setString(2,fecha);
			ps.setString(3,hora);
			ps.setString(4,usuario);
			ps.setInt(5,estadoDevolucion);
			ps.setInt(6,centroCostoDevuelve);
			ps.setInt(7,farmacia);
			ps.setInt(8,tipoDevolucion);
            ps.setString(9, motivo);
            ps.setInt(10, institucion);
			
			
			if (ps.executeUpdate()>0)
			{
				return cargarUltimoCodigoSequence(con);
			}
			else
			{
				return 0;
			}
		}
	    catch(SQLException e)
		  {
		      logger.error(e+" Error en la inserción de datos: SqlBaseDevolucionAFarmaciaDao ",e );
		      resp=0;
		  }
	    return resp;
	}
	
	/**
	 * Carga la ultima devolucion  insertada   (table= devolucion_med))
	 * @param con
	 * @return
	 */
	public static int cargarUltimoCodigoSequence(Connection con)
	{
		int ultimoCodigoSequence=0;
		try
		{
			PreparedStatementDecorator cargarUltimoStatement= new PreparedStatementDecorator(con.prepareStatement(ultimaSequenciaDevolucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs= new ResultSetDecorator(cargarUltimoStatement.executeQuery());
			if(rs.next())
			{
				ultimoCodigoSequence=rs.getInt("sequenciaDevolucion");
				return ultimoCodigoSequence;
			}
			else
			{
				return 0;
			}
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta del último código del despacho: SqlBaseDevolucionAFarmaciaDao "+e.toString());
			return 0;
		}
	}
	
	/**
	 * Inserta el DETALLE de la devolución del medicamento
	 * @param con, Connection
	 * @param devolucion, Código de la tabla devolucion_med
	 * @param numeroSolicitud, Número de la solicitud
	 * @param articulo, código del artículo a devolver
	 * @param cantidad, cantidad
	 * @param tipoDevolucion, (Automática - Manual)
	 * @return
	 */
	public static int insertarDetalleDevolucionMedicamentos		(	Connection con, int  devolucion,  
			        												int numeroSolicitud, int articulo, 
																	int cantidad, String insertarDetalleDevolucionMedicamentoStr,
																	String lote, String fechaVencimientoLote)
	{
	    int resp=0;
	    
	    try
		{		
	    	PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarDetalleDevolucionMedicamentoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    	logger.info("--->"+insertarDetalleDevolucionMedicamentoStr);
	    	ps.setInt(1,devolucion);
			ps.setInt(2,numeroSolicitud);
			ps.setInt(3,articulo);
			ps.setInt(4,cantidad);
			
			if(UtilidadTexto.isEmpty(lote))
				ps.setObject(5, null);
			else
				ps.setString(5, lote);
			if(UtilidadTexto.isEmpty(fechaVencimientoLote) || !UtilidadFecha.esFechaValidaSegunAp(fechaVencimientoLote))
				ps.setObject(6, null);
			else
				ps.setString(6, UtilidadFecha.conversionFormatoFechaABD(fechaVencimientoLote));
			
			logger.info("-->"+devolucion);
			logger.info("-->"+numeroSolicitud);
			logger.info("-->"+articulo);
			logger.info("-->"+cantidad);
			logger.info("-->"+lote);
			logger.info("-->"+fechaVencimientoLote);
			
			resp=ps.executeUpdate();
			
		}
	    catch(SQLException e)
	    {
	    	logger.warn(e+" Error en la inserción de datos: SqlBaseDevolucionAFarmaciaDao "+e.toString() );
	    	resp=0;
	    }
	    return resp;
	}

	/**
	 * Indica si existe o no devolucuion para un numero de solicutd -Articulo especificos
	 * @param con
	 * @param numeroSolicitudStr
	 * @param codigoArticuloStr
	 * @return
	 */
	public static boolean existeDevolucionParaNumeroSolicitudArticulo(Connection con, String numeroSolicitudStr, String codigoArticuloStr)
	{
		boolean existe = false;
		try
		{
			PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(existeDevolucionParaNumeroSolicitudArticuloStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statement.setString(1, numeroSolicitudStr);
			statement.setString(2, codigoArticuloStr);
			
			ResultSetDecorator rs= new ResultSetDecorator(statement.executeQuery());
			if(rs.next())
				existe=true;
			return existe;
			
		}
		catch(SQLException e)
		{
			logger.warn("Error en la evaluacion de  existeDevolucionParaNumeroSolicitudArticulo: SqlBaseDevolucionAFarmaciaDao "+e.toString());
			return false;
		}
	}
	
}
