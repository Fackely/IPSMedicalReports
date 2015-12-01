/**
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
 * @author axioma
 *
 */
public class SqlBaseServiciosSircDao {

	
	private static Logger logger = Logger.getLogger(SqlBaseServiciosSircDao.class);
	
	/**
	 * 
	 */
	private static String cadenaInsertarServicioSirc="INSERT INTO historiaclinica.servicios_sirc (codigo,descripcion,institucion,activo) VALUES(?,?,?,?) ";
	
	/**
	 * 
	 */
	private static String cadenaConsultarServicioSirc="SELECT codigo,descripcion,institucion,activo,'BD' as tiporegistro FROM historiaclinica.servicios_sirc WHERE institucion = ?";
	
	/**
	 * 
	 */
	private static String cadenaModificarServicioSirc="UPDATE historiaclinica.servicios_sirc set descripcion=?, activo=? WHERE codigo=? AND institucion=? ";
	
	/**
	 * 
	 */
	private static String cadenaEliminarServicioSirc="DELETE FROM historiaclinica.servicios_sirc where codigo=? AND institucion=? ";
	
	
	/**
	 * Cadena de Consulta de Servicios Sirc Detalle 
	 * */
	private static String cadenaConsultaServiciosSircDetalle = "SELECT sd.servicio_sirc, sd.institucion, sd.servicio," +
															   "'(' || getcodigoespecialidad(sd.servicio) || '-' || sd.servicio || ') ' || getnombreservicio(sd.servicio,"+ConstantesBD.codigoTarifarioCups+") AS descripcion, " +		
															   "'BD' as tiporegistro " +
															   "FROM historiaclinica.det_servicios_sirc sd " +
															   "INNER JOIN view_servicios va ON(va.servicio=sd.servicio AND va.tipo_tarifario=0) " +
															   "WHERE servicio_sirc=? AND institucion=? ";
	

	/**
	 * Cadena de Insercion de Servicios Sirc Detalle 
	 * */
	private static String cadenaInsercionServiciosSircDetalle = "INSERT INTO historiaclinica.det_servicios_sirc (servicio_sirc,institucion, servicio) " +
																"VALUES (?,?,?) ";
	
	
	/**
	 * Cadena de Eliminacion de Servicios Sirc Detalle 
	 * */
	private static String cadenaEliminacionServiciosSircDetalle = "DELETE FROM historiaclinica.det_servicios_sirc " +
																  "WHERE servicio_sirc=? AND institucion=?  "; 																   
	
	
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @param cadena
	 * @return
	 */
	public static boolean insertarServicioSirc(Connection con, HashMap vo) {
		boolean resultado=false;
		PreparedStatementDecorator ps= null;
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarServicioSirc));
			
			/**
			 * INSERT INTO historiaclinica.servicios_sirc (codigo,descripcion,institucion,activo) VALUES(?,?,?,?) 
			 */
			logger.info(cadenaInsertarServicioSirc);
			
			
			ps.setDouble(1, Utilidades.convertirADouble(vo.get("codigo")+""));
			ps.setString(2, vo.get("descripcion")+"");
			ps.setInt(3, Utilidades.convertirAEntero(vo.get("institucion")+""));
			ps.setString(4, vo.get("activo")+"");
			Utilidades.imprimirMapa(vo);
			resultado =  ps.executeUpdate()>0;
			
			
		} catch (SQLException e) {
			logger.error("error ", e);
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseReferenciaDao "+sqlException.toString() );
			}
		}
		return resultado;
	}

	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap consultarServicioSirc(Connection con, HashMap vo) {
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		
		 
		String cadena=cadenaConsultarServicioSirc;		
		
		if(vo.containsKey("codigo"))
		{
			cadena+=" AND codigo="+vo.get("codigo");
		}
		PreparedStatementDecorator ps= null;
		try
		{
			logger.info("SQL "+cadena+" order by descripcion");
			logger.error(vo.get("institucion").toString());
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena+" order by descripcion"));
			ps.setInt(1,Utilidades.convertirAEntero(vo.get("institucion").toString()));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		
		}
		catch (SQLException e)
		{
			logger.error("error ", e);
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseReferenciaDao "+sqlException.toString() );
			}
		}
		
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarServicioSirc(Connection con, HashMap vo) {
		boolean resultado=false;
		PreparedStatementDecorator ps= null;
		try {
			
			String sql = "SELECT " +
							"reg, " +
							"codigo," +
							"descripcion," +
							"institucion," +
							"activo," +
							"tiporegistro " +
						"FROM " +
							"(" +
								"SELECT " +
									"rownum AS reg, " +
									"codigo," +
									"descripcion," +
									"institucion," +
									"activo," +
									"tiporegistro " +
								"FROM " +
									"(" +
										"SELECT " +
											"codigo," +
											"descripcion," +
											"institucion," +
											"activo," +
											"'BD' as tiporegistro " +
										"FROM " +
											"historiaclinica.servicios_sirc " +
										"WHERE " +
											"institucion = 2 order by descripcion" +
									") tabla " +
							") tablaExt " +
						"WHERE " +
							"reg < 5000;";
			
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificarServicioSirc));			
			
			/**
			 * UPDATE historiaclinica.servicios_sirc set descripcion=?, activo=? WHERE codigo=? AND institucion=? 
			 */
			
			ps.setString(1, vo.get("descripcion")+"");
			ps.setString(2, vo.get("activo")+"");
			
			ps.setDouble(3, Utilidades.convertirADouble(vo.get("codigo")+""));
			ps.setInt(4, Utilidades.convertirAEntero(vo.get("institucion")+""));
			resultado = ps.executeUpdate()>0;
		
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseReferenciaDao "+sqlException.toString() );
			}
		}
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean eliminarRegistro(Connection con, int codigo, int institucion ) {
		boolean resultado = false;
		PreparedStatementDecorator ps= null;
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminarServicioSirc));
			
			/**
			 * DELETE FROM historiaclinica.servicios_sirc where codigo=? AND institucion=? 
			 */
			
			ps.setDouble(1,Utilidades.convertirADouble(codigo+""));
			ps.setInt(2,institucion);
			resultado =  ps.executeUpdate()>0;
		
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseReferenciaDao "+sqlException.toString() );
			}
		}
		return resultado;
	}
	
	
	/**
	 * Consulta de Servicios Sirc Detalle
	 * @param Connection con
	 * @param HashMap vo 
	 * **/
	public static HashMap consultarServicioSircDetalle(Connection con, HashMap vo)
	{
		HashMap mapa = new HashMap();
		String cadena = cadenaConsultaServiciosSircDetalle;
		PreparedStatementDecorator ps= null;
		try
		{					
			if(vo.containsKey("servicio"))
				cadena+=" AND servicio="+vo.get("servicio");
			
			cadena+=" ORDER BY descripcion DESC ";
			
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setDouble(1,Utilidades.convertirADouble(vo.get("servicio_sirc").toString()));
			ps.setInt(2,Utilidades.convertirAEntero(vo.get("institucion").toString()));
		
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		
		}	
		catch(SQLException e)
		{
			e.printStackTrace();
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseReferenciaDao "+sqlException.toString() );
			}
		}
		
		return mapa;		
	}
	
	/**
	 * Inserta un registro Detalle de Servicios Sirc
	 * HashMap vo 
	 * **/
	public static boolean insertarServiciosSircDetalle(Connection con, HashMap vo)
	{
		boolean resultado = false;
		PreparedStatementDecorator ps= null;
		try
		{
			Utilidades.imprimirMapa(vo);

			ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsercionServiciosSircDetalle));
			
			/**
			 * INSERT INTO historiaclinica.det_servicios_sirc (servicio_sirc,institucion, servicio) 
			 */
			
			ps.setDouble(1,Utilidades.convertirADouble(vo.get("servicio_sirc").toString()));
			ps.setInt(2,Utilidades.convertirAEntero(vo.get("institucion").toString()));
			ps.setInt(3,Utilidades.convertirAEntero(vo.get("servicio").toString()));			
			resultado =  ps.executeUpdate()>0;	
		

		}	
		catch(SQLException e)
		{
			e.printStackTrace();
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseReferenciaDao "+sqlException.toString() );
			}
		}
		
		return resultado;
	}
	
	/**
	 * Eliminar un registro Detalle de Servicios Sirc
	 * @param Connection con
	 * @param HashMap vo
	 * */
	public static boolean eliminarServiciosSircDetalle(Connection con, HashMap vo)
	{
		boolean resultado = false;
		String cadena = cadenaEliminacionServiciosSircDetalle;
		PreparedStatementDecorator ps= null;
		try
		{
			if(vo.containsKey("servicio"))
				cadena+= " AND servicio = "+vo.get("servicio");
			
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena));			
			ps.setDouble(1,Utilidades.convertirADouble(vo.get("servicio_sirc").toString()));
			ps.setInt(2,Utilidades.convertirAEntero(vo.get("institucion").toString()));
			resultado = ps.executeUpdate()>0;
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseReferenciaDao "+sqlException.toString() );
			}
		}
		return resultado;	
	}
}