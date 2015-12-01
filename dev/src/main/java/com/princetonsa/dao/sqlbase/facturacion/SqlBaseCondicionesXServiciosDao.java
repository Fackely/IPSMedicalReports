package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;


/**
 * @author Andrés Eugenio Silva Monsalve 
 *
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la parametrización de Condiciones por Servicios
 */
public class SqlBaseCondicionesXServiciosDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseCondicionesXServiciosDao.class);
	
	/**
	 * Cadena que consulta el consecutivo de la Condicion de Servicios x servicio
	 */
	private static final String consultarConsecutivoXServicioStr = "SELECT "+ 
		"css.consecutivo, "+
		"'true' AS existe_bd, " +
		"'false' AS eliminar "+
		"FROM condi_serv_servicios css "+ 
		"INNER JOIN facturacion.condiciones_servicios cs ON(cs.consecutivo=css.consecutivo) "+ 
		"WHERE css.servicio = ? AND cs.institucion = ?";
	
	/**
	 * Cadena implementada para consultar los servicios parametrizados por consecutivo de hoja de gastos
	 */
	private static final String consultarServiciosXConsecutivoStr = "SELECT " +
		"getcodigoespecialidad(css.servicio) || '-' || css.servicio || ' ' || getnombreservicio(css.servicio, "+ConstantesBD.codigoTarifarioCups+") AS descripcion_servicio, "+	
		"css.servicio AS codigo, "+
		"'true' AS existe_bd, " +
		"'false' AS eliminar "+
		"FROM condi_serv_servicios css "+
		"WHERE css.consecutivo = ?";
	
	/**
	 * Cadena implementada para consultar de las condiciones parametrizadas por consecutivo de condiciones por servicios
	 */
	private static final String consultarCondicionesXConsecutivoStr = "SELECT "+ 
		"csc.condicion AS codigo_condicion, "+
		"ect.descrip_examenct AS descripcion_condicion, "+
		"'true' AS existe_bd, " +
		"'false' AS eliminar "+
		"FROM condi_serv_condiciones csc "+
		"INNER JOIN facturacion.examen_conditoma ect ON(ect.codigo_examenct = csc.condicion)"+
		"WHERE csc.consecutivo = ?";
	
	/**
	 * Cadena que inserta el encabezado de las condiciones por servicios
	 */
	private static final String insertarEncabezadoCondicionesXServiciosStr = "INSERT INTO facturacion.condiciones_servicios (consecutivo,institucion) VALUES ";
	
	/**
	 * Cadena que eliminar el encabezado de las condiciones por servicios
	 */
	private static final String eliminarEncabezadoCondicionesXServiciosStr = "DELETE FROM facturacion.condiciones_servicios WHERE consecutivo = ? ";
	
	/**
	 * Cadena usada para insertar un servicio en las condiciones por servicios
	 */
	private static final String insertarServicioStr = "INSERT INTO condi_serv_servicios (servicio,consecutivo) VALUES (?,?)";
	
	/**
	 * Cadena usada para eliminar un servicio en las condiciones por servicios
	 */
	private static final String eliminarServicioStr = "DELETE FROM condi_serv_servicios WHERE servicio = ? ";
	
	/**
	 * Cadena usada para insertar una condicion en las condiciones por servicios
	 */
	private static final String insertarCondicionStr = "INSERT INTO condi_serv_condiciones (condicion,consecutivo) VALUES (?,?)";
	
	/**
	 * Cadena usada para modificar una condicion en las condiciones por servicios
	 * private static final String modificarCondicionStr = "UPDATE condi_serv_condiciones SET cantidad = ? WHERE articulo = ? AND consecutivo = ?";
	 */
	
	
	/**
	 * Cadena usada para eliminar una condicion en las condiciones por servicios
	 */
	private static final String eliminarCondicionStr = "DELETE FROM condi_serv_condiciones WHERE condicion = ? AND consecutivo = ?";
	
	/**
	 * Cadena que elimina los servicios de una parametrizacion de las condiciones por servicios
	 */
	private static final String eliminarServiciosXConsecutivoStr = "DELETE FROM condi_serv_servicios WHERE consecutivo = ?";
	
	/**
	 * Cadena que elimina las condiciones de una parametrizacion en las condiciones por servicios
	 */
	private static final String eliminarCondicionesXConsecutivoStr = "DELETE FROM condi_serv_condiciones WHERE consecutivo = ?";
	
	/**
	 * Cadena que consulta las condiciones para la toma de un servicio
	 */
	private static final String obtenerCondicionesTomaXServicioStr = "SELECT "+ 
		"ec.codigo_examenct AS codigo, "+
		"ec.descrip_examenct AS descripcion "+ 
		"FROM condi_serv_servicios css "+ 
		"INNER JOIN facturacion.condiciones_servicios cs ON(cs.consecutivo = css.consecutivo) "+
		"INNER JOIN condi_serv_condiciones csc ON(csc.consecutivo=cs.consecutivo) "+ 
		"INNER JOIN facturacion.examen_conditoma ec ON(ec.codigo_examenct=csc.condicion) "+ 
		"WHERE "+ 
		"css.servicio = ? AND cs.institucion = ? "+ 
		"ORDER BY descripcion";
	
	/**
	 * Método implementado para consultar el consecutivo x servicio
	 * de la parametrizacion de hoja de gastos
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int consultarConsecutivoXServicio(Connection con,HashMap campos)
	{
		try
		{
			int consecutivo = 0;
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarConsecutivoXServicioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("codigoServicio").toString()));
			pst.setInt(2,Utilidades.convertirAEntero(campos.get("institucion").toString()));
			logger.info("\n\nconsulta-->"+consultarConsecutivoXServicioStr+"\n");
			logger.info("\n\nSQLBASE Codigo de Servicio--->   "+campos.get("codigoServicio"));
			logger.info("\n\nSQLBASE Institucion--->   "+campos.get("institucion"));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				consecutivo = rs.getInt("consecutivo");
			
			return consecutivo;
		}
		catch(SQLException e)
		{
			logger.error("Error consultando el consecutivo x servicio :"+e);
			return 0;
		}
	}
	
	/**
	 * Método implementado para consultar la parametrizacion de una Condicion por Servicio por consecutivo
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap consultarCondicionesXServicioXConsecutivo(Connection con,HashMap campos)
	{
		try
		{
			HashMap respuesta = new HashMap();
			logger.info("Consulta    "+consultarServiciosXConsecutivoStr);
			
			//Consulta de los servicios asociados a la condicionXservicios
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarServiciosXConsecutivoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,Utilidades.convertirADouble(campos.get("consecutivo").toString()));
			
			logger.info("\n\nSQLBASE Codigo del Consecutivo en tabla condi_servi_servicios--->   "+campos.get("consecutivo"));
			
			respuesta.put("mapaServicios",UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true));
			
			
			//Consulta de las condiciones asociados a la condicionXservicios
			pst =  new PreparedStatementDecorator(con.prepareStatement(consultarCondicionesXConsecutivoStr));
			pst.setDouble(1,Utilidades.convertirADouble(campos.get("consecutivo").toString()));
			
			logger.info("\n\nSQLBASE Codigo del Consecutivo en tabla condi_servi_condiciones--->   "+campos.get("consecutivo"));
			
			respuesta.put("mapaCondiciones",UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true));
			
			return respuesta;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarCondicionesXServiciosXConsecutivo: "+e);
			return null;
		}
	}
	
	
	/**
	 * Método implementado para guardar la Condicion por servicio
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int guardar(Connection con,HashMap campos)
	{
		try
		{
			//********SE TOMA LA INFORMACION ENVIADA**************************
			int consecutivo = Integer.parseInt(campos.get("consecutivo").toString());
			int institucion = Integer.parseInt(campos.get("institucion").toString());
			
			String secuencia = campos.get("secuencia").toString();
			HashMap mapaServicios = (HashMap) campos.get("mapaServicios");
			HashMap mapaCondiciones = (HashMap) campos.get("mapaCondiciones");
			int numServicios = Integer.parseInt(campos.get("numServicios").toString());
			int numCondiciones = Integer.parseInt(campos.get("numCondiciones").toString());
			int resp0 = 0, resp1 = 0, resp2 = 0;
			//****************************************************************
			PreparedStatementDecorator pst = null;
			
			//se inicia la transaccion---------------------------------------------------
			boolean inicioTransaccion = UtilidadBD.iniciarTransaccion(con);
			if(!inicioTransaccion)
			{
				logger.error("Error al iniciar la transacción en método guardar");
				return 0;
			}
			
			int numServiciosAEliminar = obtenerNumRegistrosAEliminar(mapaServicios,numServicios);
			int numCondicionesAEliminar = obtenerNumRegistrosAEliminar(mapaCondiciones,numCondiciones);
			
			//Se verifica si es eliminacion de toda la parametrizacion------------------------------------------------------------------
			if(consecutivo>0&&numServicios==numServiciosAEliminar&&numCondiciones==numCondicionesAEliminar)
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarCondicionesXConsecutivoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setDouble(1,Utilidades.convertirADouble(consecutivo+""));
				resp0 = pst.executeUpdate();
				
				if(resp0>0)
				{
					pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarServiciosXConsecutivoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setDouble(1,Utilidades.convertirADouble(consecutivo+""));
					resp1 = pst.executeUpdate();
				}
				
				if(resp1>0)
				{
					pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarEncabezadoCondicionesXServiciosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setDouble(1,Utilidades.convertirADouble(consecutivo+""));
					resp2 = pst.executeUpdate();
				}
			}
			else
			{
				///si no hay consecutivo quiere decir que es una parametrizacion nueva---------------------
				if(consecutivo<=0)
				{
					String consulta = insertarEncabezadoCondicionesXServiciosStr + " ("+secuencia+",?)";
					logger.info("consulta--->"+consulta);
					pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setInt(1, institucion);
					resp0 = pst.executeUpdate();
					if(resp0>0)
						consecutivo = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(con, "seq_condiciones_servicios");
				}
				else
					resp0 = 1;
				
				//ingresar/eliminar servicios-------------------------------------
				if(resp0>0)
				{
					resp1 = guardarServicios(con,mapaServicios,numServicios,consecutivo);
				}
				
				//ingresar/modificar/eliminar articulos-------------------------------
				if(resp1>0)
				{
					resp2 = guardarCondiciones(con,mapaCondiciones,numCondiciones,consecutivo);
				}
			}
			
			
			if(resp0<=0||resp1<=0||resp2<=0)
			{
				UtilidadBD.abortarTransaccion(con);
				consecutivo = 0;
			}
			else
				UtilidadBD.finalizarTransaccion(con);
			
			
			return consecutivo;
		}
		catch(SQLException e)
		{
			logger.error("Error en el método guardar: "+e);
			return 0;
		}
	}

	/**
	 * Método que obtiene el número de servicios que se van a eliminar
	 * @param mapa
	 * @param numRegistros
	 * @return
	 */
	private static int obtenerNumRegistrosAEliminar(HashMap mapa, int numRegistros) 
	{
		int cantidad = 0;
		
		for(int i=0;i<numRegistros;i++)
		{
			logger.info("\n\nEstado eliminar_  ---->   "+mapa.get("eliminar_"+i)+"\n\n");
			
			if(UtilidadTexto.getBoolean(mapa.get("eliminar_"+i).toString()))
				cantidad ++;
		}
		
		return cantidad;
	}

	/**
	 * Método implementado para guardar los articulos
	 * @param con
	 * @param mapaCondiciones
	 * @param numCondiciones
	 * @param consecutivo
	 * @return
	 */
	private static int guardarCondiciones(Connection con, HashMap mapaCondiciones, int numCondiciones, int consecutivo) 
	{
		int resp = numCondiciones>0?1:0;
		PreparedStatementDecorator pst = null;
		
		try
		{
			for(int i=0;i<numCondiciones;i++)
			{
				//se verifica si es un registro nuevo para insertarlo
				if(!UtilidadTexto.getBoolean(mapaCondiciones.get("existeBd_"+i).toString()))
				{
					//**************INSERCION DE CONDICION**************************
					pst =  new PreparedStatementDecorator(con.prepareStatement(insertarCondicionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					logger.info("insertarCondicionStr->"+insertarCondicionStr+"  "+mapaCondiciones.get("codigoCondicion_"+i).toString()+"   "+consecutivo);
					pst.setInt(1,Utilidades.convertirAEntero(mapaCondiciones.get("codigoCondicion_"+i).toString()));
					pst.setDouble(2, Utilidades.convertirADouble(consecutivo+""));
					if(pst.executeUpdate()<=0)
					{
						resp = 0;
						i = numCondiciones;
					}
					//************************************************************
				}
				else if(UtilidadTexto.getBoolean(mapaCondiciones.get("eliminar_"+i).toString()))
				{
					//************ELIMINAR CONDICION******************************
					pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarCondicionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setInt(1,Utilidades.convertirAEntero(mapaCondiciones.get("codigoCondicion_"+i).toString()));
					pst.setDouble(2, Utilidades.convertirADouble(consecutivo+""));
					if(pst.executeUpdate()<=0)
					{
						resp = 0;
						i = numCondiciones;
					}
					//***********************************************************
				}
				/*else
				{
					//************MODIFICAR CONDICION******************************
					pst =  new PreparedStatementDecorator(con.prepareStatement(modificarArticuloStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setObject(1,mapaCondiciones.get("cantidad_"+i));
					pst.setObject(2,mapaCondiciones.get("codigoCondicion_"+i));
					pst.setInt(3, consecutivo);
					if(pst.executeUpdate()<=0)
					{
						resp = 0;
						i = numCondiciones;
					}
					//************************************************************
				}*/
					
			}
			
			return resp;
		}
		catch(SQLException e)
		{
			logger.error("Error en guardar servcios: "+e);
			return 0;
		}
	}

	/**
	 * Método implementado para guardar los servicios 
	 * @param con
	 * @param mapaServicios
	 * @param numServicios
	 * @param consecutivo 
	 * @return
	 */
	private static int guardarServicios(Connection con, HashMap mapaServicios, int numServicios, int consecutivo) 
	{
		int resp = numServicios>0?1:0;
		PreparedStatementDecorator pst = null;
		
		try
		{
			for(int i=0;i<numServicios;i++)
			{
				//se verifica si es un registro nuevo para insertarlo
				if(!UtilidadTexto.getBoolean(mapaServicios.get("existeBd_"+i).toString()))
				{
					//**************INSERCION DE SERVICIO**************************
					pst =  new PreparedStatementDecorator(con.prepareStatement(insertarServicioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setInt(1,Utilidades.convertirAEntero(mapaServicios.get("codigo_"+i).toString()));
					pst.setDouble(2, Utilidades.convertirADouble(consecutivo+""));
					if(pst.executeUpdate()<=0)
					{
						resp = 0;
						i = numServicios;
					}
					//************************************************************
				}
				else if(UtilidadTexto.getBoolean(mapaServicios.get("eliminar_"+i).toString()))
				{
					//************ELIMINAR SERVICIO******************************
					pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarServicioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setInt(1,Utilidades.convertirAEntero(mapaServicios.get("codigo_"+i).toString()));
					//pst.setObject(2,,mapaServicios.get("consecutivo_"+i));
					if(pst.executeUpdate()<=0)
					{
						resp = 0;
						i = numServicios;
					}
					//***********************************************************
				}
					
			}
			
			return resp;
		}
		catch(SQLException e)
		{
			logger.error("Error en guardar servcios: "+e);
			return 0;
		}
		
		
	}
	
	
	/**
	 * Método que consulta las condiciones para la toma del servicio
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerCondicionesTomaXServicio(Connection con,HashMap campos)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			pst =  con.prepareStatement(obtenerCondicionesTomaXServicioStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet);
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("codigoServicio").toString()));
			pst.setInt(2,Utilidades.convertirAEntero(campos.get("institucion").toString()));
			
			rs = pst.executeQuery();
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo", rs.getObject("codigo"));
				elemento.put("descripcion", rs.getObject("descripcion"));
				
				resultados.add(elemento);
			}
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerCondicionesTomaXServicio", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return resultados;
	}
	
}
