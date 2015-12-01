/*
 * SqlBaseDevolucionPedidosDao.java 
 * Autor			:  mdiaz
 * Creado el	:  16-sep-2004
 * 
 * Lenguaje		: Java
 * Compilador	: J2SDK 1.4.1_01
 * 
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 * */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * descripcion de esta clase
 *
 * @version 1.0, 16-sep-2004
 * @author <a href="mailto:miguel@PrincetonSA.com">Miguel Arturo Diaz</a>
 */
public class SqlBaseDevolucionPedidosDao {
	/**
	 * Manejador de logs de la clase
	 */
	public static Logger logger = Logger.getLogger(SqlBaseDevolucionPedidosDao.class);
	
	/**
	 * Cadena que consulta la fecha y hora despacho de un pedido
	 */
	private static final String consultarFechaHoraDespachoStr = "SELECT " +
		"to_char(fecha,'DD/MM/YYYY') as fecha, " +
		"hora as hora " +
		"FROM despacho_pedido WHERE pedido = ?";
	
	/**
	 *Cadena que realiza la busqueda avanzada de despachos
	 */
	private static final String consultarDespachosStr = "SELECT " +
		"p.codigo," +
		"to_char(p.fecha,'DD/MM/YYYY') as fecha_pedido," +
		"p.hora as hora_pedido," +
		"p.usuario as usuario_pedido," +
		"to_char(dp.fecha,'DD/MM/YYYY') as fecha_despacho," +
		"dp.hora as hora_despacho," +
		"dp.usuario as usuario_despacho " +
		"FROM pedido p " +
		"INNER JOIN despacho_pedido dp ON(dp.pedido=p.codigo) " +
		"WHERE ";
	
 
	public static Connection refreshDBConnection(Connection con){
		
		try{
		  if (con == null || con.isClosed()){
			  DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			  con = myFactory.getConnection();
		  }
		}
		catch(SQLException e){
				logger.warn(" Error al intentar reabrir la conexion con la base de datos [Clase: SqlBaseDevolucionPedidos ]" + e);
		}
    
		return con;		 		
	}
	

	
	public static boolean beginTransaction(Connection con){
    boolean result = false;
		
    DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		try{
			if (myFactory.beginTransaction(con)){
				result = true;
			}
			else{
				result =  false;
			}
		}
		catch (SQLException e){
			result = false;
		}
		
		if( result == false){
			logger.warn("La transaccion no pudo ser iniciadad para el objeto Devoluciones de Pedidos");
		}
		
		return result;
	}
	
	
	public static boolean endTransaction(Connection con){
    boolean result = false;
		
    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		try{
			myFactory.endTransaction(con);
			 result = true;
		}
		catch (SQLException e){
			result = false;
		}

		if( result == false){
			logger.warn("La transaccion no pudo ser finalizada para el objeto Devoluciones de Pedidos");
		}
		
		return result;
	}
	
	
	public static boolean abortTransaction(Connection con){
	  boolean result = false;
		
	  DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		try{
			myFactory.abortTransaction(con);
			result = true;
		}
		catch (SQLException e){
			result = false;
		}
	
		if( result == false){
			logger.warn("La transaccion no pudo ser abortada para el objeto Devoluciones de Pedidos");
		}
		
		return result;
	}
	


	public static int getSequenceLastValue(Connection con, String sequenceName)
	{
		int result = -1;
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		try
		{
			result = myFactory.obtenerUltimoValorSecuencia(con, sequenceName);	
		}
		catch (SQLException e)
		{
			logger.error("No pudo ser obtenida una secuencia para el objeto Devoluciones de Pedidos: \n " + e);
			result = -1;
		}
		return result;
	}

	
	
	

	/**
	 * Retorna el siguiente valor de la secuencia
	 * @param con
	 * @param sequenceName
	 * @return siguiente valor de la secuencia
	 * @author Cristhian Murillo
	 */
	public static int getSequenceNextValue(Connection con, String sequenceName)
	{
		int result = -1;
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		result = myFactory.incrementarValorSecuencia(con, sequenceName);
		return result;
	}

	
	
	public static ResultSetDecorator buscar(Connection con, String[] selectedColumns, String from, String where, String orderBy){
		PreparedStatementDecorator busqueda;
		ResultSetDecorator rs = null;
		String selectQuery = "";
		int k;
		
		logger.info("where-->"+where);
		
	  try
		{
			con = refreshDBConnection(con);
			selectQuery = "SELECT ";
			for(k=0; k<(selectedColumns.length-1); k++){
				selectQuery += selectedColumns[k] + ", ";
			}
			selectQuery  += selectedColumns[k];
			selectQuery = selectQuery +" FROM  "+from +(( ( where.equals("") )? " " : " WHERE " +where)  +(( ( orderBy.equals("") )? " " : " ORDER BY ( " +orderBy + ") ")));

			logger.info("--->"+selectQuery);
			busqueda =  new PreparedStatementDecorator(con.prepareStatement(selectQuery,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs = new ResultSetDecorator(busqueda.executeQuery());
		}
		catch (Exception e)
		{
			logger.error("Error realizando la búsqueda de Devolucion Pedidos [ Tabla: Devolucion_Pedidos ]: ");
			logger.error(selectQuery);
			logger.error("",e);
		}
    
		return rs;
	}
	
	/**
	 * Método que consulta la fecha y hora de un pedido
	 * @param con
	 * @param codigoPedido
	 * @return
	 */
	public static String consultarFechaHoraDespacho(Connection con,String codigoPedido)
	{
		try
		{
			String fechaHora = "";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarFechaHoraDespachoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setObject(1,codigoPedido);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				fechaHora = rs.getString("fecha") + ConstantesBD.separadorSplit + rs.getString("hora");
			
			return fechaHora;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarFechaHoraDespacho :"+e);
			return "";
		}
	}
	
	/**
	 * Método implementado para realizar la busqueda avanzada de despachos
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap consultarDespachos(Connection con,HashMap campos)
	{
		try
		{
			logger.info("Centro de Costo=> "+campos.get("codigoCentroCosto"));
			String consulta = consultarDespachosStr + " p.centro_costo_solicitante = "+campos.get("codigoCentroCosto") +" AND p.centro_costo_solicitado="+campos.get("codigoFarmacia")+" AND p.es_qx = '"+ConstantesBD.acronimoNo+"' ";
			
			if(!campos.get("pedidoInicial").toString().equals("")&&!campos.get("pedidoFinal").toString().equals(""))
			{
				consulta += " AND p.codigo BETWEEN "+campos.get("pedidoInicial")+" AND "+campos.get("pedidoFinal");
			}
			if(!campos.get("fechaPedidoInicial").toString().equals("")&&!campos.get("fechaPedidoFinal").toString().equals(""))
			{
				consulta += " AND p.fecha BETWEEN '"+
					UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaPedidoInicial").toString())+"' AND '"+
					UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaPedidoFinal").toString())+"' ";
			}
			if(!campos.get("fechaDespachoInicial").toString().equals("")&&!campos.get("fechaDespachoFinal").toString().equals(""))
			{
				consulta += " AND dp.fecha BETWEEN '"+
					UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaDespachoInicial").toString())+"' AND '"+
					UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaDespachoFinal").toString())+"' ";
			}
			if(!campos.get("usuarioPedido").toString().equals(""))
				consulta += " AND p.usuario = '"+campos.get("usuarioPedido").toString()+"' ";
			if(!campos.get("usuarioDespacho").toString().equals(""))
				consulta += " AND dp.usuario = '"+campos.get("usuarioDespacho").toString()+"' ";
			//if(!campos.get("auto_subcontratacion").toString().equals(null))
				consulta +=" AND p.auto_por_subcontratacion = '"+ConstantesBD.acronimoNo+"'";
			consulta += " ORDER BY p.codigo";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)),true,false);
	        st.close();
			return mapaRetorno;

		}
		catch(SQLException e)
		{
			logger.error("Error en consultarDespachos de SqlBaseDEvolucionPedidosDao: "+e);
			return null;
		}
	}
	
	
}
