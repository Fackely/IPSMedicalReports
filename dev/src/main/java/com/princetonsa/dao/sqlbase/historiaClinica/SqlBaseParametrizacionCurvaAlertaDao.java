/*
 * @(#)SqlBaseParametrizacionCurvaAlertaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.sqlbase.historiaClinica;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;

/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 *
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class SqlBaseParametrizacionCurvaAlertaDao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseParametrizacionCurvaAlertaDao.class);
	
	/**
	 * listado
	 */
	private static String listado=" SELECT pcap.codigo as codigo, pcap.posicion as codigoposicion, p.nombre as nombreposicion, pcap.paridad AS codigoparidad, pa.nombre as nombreparidad, pcap.membrana as codigomembrana, pcap.rango_inicial as rangoinicial, pcap.rango_final as rangofinal, pcap.valor as valor, CASE WHEN pcap.activo='N' THEN 'false' ELSE 'true' END AS activo, 'true' AS estabd, 'false' AS eseliminada FROM param_curva_alert_partograma pcap INNER JOIN posiciones p ON (p.codigo= pcap.posicion) INNER JOIN paridades pa ON (pa.codigo= pcap.paridad) INNER JOIN membranas m ON (m.codigo= pcap.membrana) WHERE institucion=? order by nombreposicion,nombreparidad, pcap.membrana, rangoinicial, rangofinal";	

	/**
	 * eliminar
	 */
	private static String eliminarStr="DELETE FROM  param_curva_alert_partograma WHERE codigo=?"; 
	
	
	/**
	 * modifica
	 */
	private static String modificarStr="UPDATE param_curva_alert_partograma SET posicion=?, paridad=?, membrana=?, rango_inicial=?, rango_final=?, valor=?, activo=?, institucion=?	WHERE codigo=? ";
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap listado(	Connection con, 
	        						int codigoInstitucion
								 )
	{
		 PreparedStatementDecorator cargarStatement= null;
	    try
		{
	       cargarStatement= new PreparedStatementDecorator(con.prepareStatement(listado));
	       logger.info("\n\n listado--> "+listado);
			cargarStatement.setInt(1, codigoInstitucion);
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(cargarStatement.executeQuery()));
			cargarStatement.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de la listado: SqlBaseParametrizacionCurvaAlertaDao "+e.toString());
			return new HashMap();
		}finally {			
			if(cargarStatement!=null){
				try{
					cargarStatement.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseMotivosSircDao "+sqlException.toString() );
				}
			}		
		}
	}
	
	
	/**
	 * Elimina
	 * @param con
	 * @param estado
	 * @return
	 * @throws SQLException
	 */
	public static int eliminar (Connection con, String codigoPK)
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps=  new PreparedStatementDecorator(con.prepareStatement(eliminarStr));
			ps.setDouble(1, Utilidades.convertirADouble(codigoPK));
			return ps.executeUpdate();
		}
		catch (SQLException e) 
		{
			logger.warn(" Error en el eliminar codigo:"+codigoPK+" SqlBaseParametrizacionCurvaAlertaDao "+e.toString());
			return Utilidades.convertirAEntero(e.getSQLState());
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseMotivosSircDao "+sqlException.toString() );
				}
			}		
		}
	}
	
	/**
	 * modifica
	 * @param con
	 * @param estado
	 * @return
	 * @throws SQLException
	 */
	public static int modificar (	Connection con, 
									String codigoPosicion,
									String codigoParidad,
									String codigoMembrana,
									String rangoInicial,
									String rangoFinal,
									String valor,
									String activo,
									int codigoInstitucion,
									String codigoPK)
	{
		PreparedStatementDecorator ps=null;
		try
		{
			ps=  new PreparedStatementDecorator(con.prepareStatement(modificarStr));
			
			/**
			 * UPDATE param_curva_alert_partograma SET 
			 * posicion=?, 
			 * paridad=?, 
			 * membrana=?, 
			 * rango_inicial=?, 
			 * rango_final=?, 
			 * valor=?, 
			 * activo=?, 
			 * institucion=?	
			 * WHERE codigo=? 
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(codigoPosicion));
			ps.setInt(2, Utilidades.convertirAEntero(codigoParidad));
			ps.setInt(3, Utilidades.convertirAEntero(codigoMembrana));
			ps.setDouble(4, Utilidades.convertirADouble(rangoInicial));
			ps.setDouble(5, Utilidades.convertirADouble(rangoFinal));
			ps.setString(6, valor);
			
			if(activo.equals("true") || activo.equals("t"))
				ps.setString(7, ConstantesBD.acronimoSi);
			else
				ps.setString(7, ConstantesBD.acronimoNo);
			
			ps.setInt(8, codigoInstitucion);
			ps.setDouble(9, Utilidades.convertirADouble(codigoPK));
			return ps.executeUpdate();
		}
		catch (SQLException e) 
		{
			logger.warn(" Error en el eliminar codigo:"+codigoPK+" SqlBaseParametrizacionCurvaAlertaDao "+e.toString());
			return ConstantesBD.codigoNuncaValido;
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseMotivosSircDao "+sqlException.toString() );
				}
			}		
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPK
	 * @param codigoPosicion
	 * @param codigoParidad
	 * @param codigoMembrana
	 * @param rangoInicial
	 * @param rangoFinal
	 * @param valor
	 * @param activo
	 * @param codigoInstitucion
	 * @return
	 */
	public static boolean insertar (	Connection con,
										String codigoPosicion,
										String codigoParidad,
										String codigoMembrana,
										String rangoInicial,
										String rangoFinal,
										String valor,
										String activo,
										int codigoInstitucion,
										String insertarStr
									)
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps=  new PreparedStatementDecorator(con.prepareStatement(insertarStr));
			
			/**
			 * INSERT INTO param_curva_alert_partograma (
			 * codigo,
			 * posicion,
			 * paridad,
			 * membrana,
			 * rango_inicial,
			 * rango_final,
			 * valor,
			 * activo,
			 * institucion) VALUES ('seq_param_alert_partograma'), ?, ?, ?, ?, ?, ?, ?, ?) 
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(codigoPosicion));
			ps.setInt(2, Utilidades.convertirAEntero(codigoParidad));
			ps.setInt(3, Utilidades.convertirAEntero(codigoMembrana));
			ps.setDouble(4, Utilidades.convertirADouble(rangoInicial));
			ps.setDouble(5, Utilidades.convertirADouble(rangoFinal));
			ps.setString(6, valor);
			
			if(activo.equals("true") || activo.equals("t"))
				ps.setString(7, ConstantesBD.acronimoSi);
			else
				ps.setString(7, ConstantesBD.acronimoNo);
			ps.setInt(8, codigoInstitucion);
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e) 
		{
			logger.warn(" Error en el insertar SqlBaseSignosSintomaXSistemaDao "+e.toString());
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseMotivosSircDao "+sqlException.toString() );
				}
			}		
		}
		return false;
	}
	
}
