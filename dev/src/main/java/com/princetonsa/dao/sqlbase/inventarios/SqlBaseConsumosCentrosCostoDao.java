package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

public class SqlBaseConsumosCentrosCostoDao
{
	private static Logger logger = Logger.getLogger(SqlBaseEquivalentesDeInventarioDao.class);
	
	public static String cadena;
	
	private static String [] indicesMap={"codigo_","nombre_"};
	
	private static String [] indicesMapP={"nombre_","nom_clase","codigo_","codigopedido_","fecha_p","hora_p","articulo_","codigo_interfaz_articulo","descripcion_","cantidad_","costo_unitario_","valor_total","costo_clase_"};
	
	private static String consultaStr="SELECT cc.codigo, cc.nombre FROM centro_atencion ca INNER JOIN "+
										"centros_costo cc ON (ca.consecutivo = cc.centro_atencion) where " +
											"cc.centro_atencion = ?";
	
	private static String consultaStrC="SELECT codigo, nombre FROM clase_inventario";
	
	
	public static HashMap<String, Object> consultaCentroCosto (Connection con, int centroAtencion)
	{
		HashMap<String, Object> resultadosCC = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst;
		 
		try{
			pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1, centroAtencion);
			
			resultadosCC = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			
			resultadosCC.put("INDICES",indicesMap);
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de Centros de Costo");
		}
		return resultadosCC;
	}
	
	public static HashMap<String, Object> consultaClases (Connection con)
	{
		HashMap<String, Object> resultadosC = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst;
		 
		try{
			pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStrC, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			resultadosC = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			
			resultadosC.put("INDICES",indicesMap);
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de Clases");
		}
		return resultadosC;
	}
	
	
	
	
	
	public static HashMap<String, Object> consultaPedidos (Connection con, String fechaFinal, String fechaInicial, int centroAtencion, String almacen, String centroCosto, String clase, String sql, String sqlOrder)
	{
		HashMap<String, Object> resultadosP = new HashMap<String, Object>();
		
		
		Statement st;
		cadena = sql + 
				"WHERE "+
					"to_char(p.fecha, 'YYYY-MM-DD') BETWEEN '"+fechaInicial+"' AND '"+fechaFinal+"' AND p.estado = 2 ";
		
		
		if(centroAtencion != -1){
			cadena += " AND cc.centro_atencion = "+centroAtencion;
		}
		
		if(!almacen.equals("-1")){
			cadena += " AND ap.codigo = "+almacen;
		}
		
		logger.info("12 centroCosto - "+centroCosto);
		
		if(!centroCosto.equals("-1")){
			cadena += " AND p.centro_costo_solicitante = "+centroCosto;
		}
		
		if(!clase.equals("-1")){
			cadena += " AND va.clase = "+clase;
		}
		
		cadena += ") t "+sqlOrder;
		
		
		logger.info("Cadena >> "+cadena);

		try{			
			st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			
			resultadosP = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(cadena)), true, true);
			
			resultadosP.put("INDICES",indicesMapP);
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de Pedidos");
		}		
		return resultadosP;
	}
	
}