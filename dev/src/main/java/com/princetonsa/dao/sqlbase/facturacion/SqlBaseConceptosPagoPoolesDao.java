package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;




/**
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 */
public class SqlBaseConceptosPagoPoolesDao
{
	/**
	 * para manejar el log en la clase
	 */
	static Logger logger = Logger.getLogger(SqlBaseConceptosPagoPoolesDao.class);
	
	/*-----------------------------------------------------
	 * 		ATRIBUTOS DE CONCEPTO DE PAGO DE POOLES
	 * ---------------------------------------------------*/	
	
	
	/**
	 * Cadena de insercion de Conceptos de Pago de Pooles 
	 */
	private static final String strCadenaInsercionConceptosPagoPooles = "INSERT INTO conceptos_pagos_pooles (codigo, descripcion, tipo_concepto, institucion, usuario_modifica, fecha_modifica, hora_modifica) values (?, ?, ?, ?, ?, ?, ?)";
	
	
	/**
	 * Cadena de Eliminacion de Conceptos de Pago de Pooles 
	 */
	private static final String strCadenaEliminacionConceptosPagoPooles = "DELETE FROM conceptos_pagos_pooles WHERE codigo=? AND institucion=?";
	
	
	/**
	 * Cadena de Modificacion de Conceptos de Pago de Pooles
	 */
	private static final String strCadenaModificacionConceptosPagoPooles ="UPDATE conceptos_pagos_pooles SET  descripcion=?, tipo_concepto=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=? ";
	
	
	/**
	 * Cadena de Consulta de Conceptos de Pago de Pooles
	 */
	private static final String strCadenaConsultaConceptosPagoPooles  = "SELECT " +
			"cpp.codigo AS codigoconcepto," +
			"cpp.codigo AS codigoconceptoold," +
			"cpp.descripcion AS descripcionconcepto," +
			"cpp.institucion AS institucion," +
			"cpp.tipo_concepto AS tipoconcepto," +
			 "'"+ConstantesBD.acronimoSi+"' AS estabd " +
			"FROM conceptos_pagos_pooles cpp " +
			"WHERE cpp.institucion = ?";
	
	
	/**
	 * Cadena de indices para le ordenamiento
	 */
	private static final String [] indicesMapa ={"codigoconcepto_","descripcionconcepto_","tipoconcepto_","institucion_","permisomodificarcodigo_","estabd_","codigoconceptoold_"};
	/*-----------------------------------------------------
	 * 		FIN ATRIBUTOS DE CONCEPTO DE PAGO DE POOLES
	 * ---------------------------------------------------*/	
	
	
	/*-----------------------------------------------------
	 * 		METODOS DE CONCEPTO DE PAGO DE POOLES
	 * ---------------------------------------------------*/	
	
	/**
	 * Metodo encargado de hacer la insercion el la BD
	 * El HashMap parametros debe contener los siguientes
	 * campos.
	 * --------------------------------------------------
	 * 				KEY'S DE PARAMETROS
	 * --------------------------------------------------
	 *--codigoConcepto --> Requerido.
	 *--descripcionConcepto --> Requerido.
	 *--tipoConcepto --> Requerido.
	 *--institucion --> Requerido.
	 *--usuario --> Requerido
	 *@param connection
	 *@param parametros 
	 **/
	public static boolean insertarConceptosPagoPooles (Connection connection, HashMap parametros)
	{
		String cadena = strCadenaInsercionConceptosPagoPooles;
		logger.info("cadena de insercion de insertarConceptosPagoPooles ==> "+cadena);
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO conceptos_pagos_pooles 
			 * (codigo, descripcion, tipo_concepto, institucion, usuario_modifica, fecha_modifica, hora_modifica) values (?, ?, ?, ?, ?, ?, ?)";
			 */
			
			ps.setString(1, parametros.get("codigoconcepto").toString());
			ps.setString(2, parametros.get("descripcionconcepto").toString());
			ps.setString(3, parametros.get("tipoconcepto").toString());
			ps.setInt(4, Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			ps.setString(5, parametros.get("usuario").toString());
			ps.setDate(6,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(7,UtilidadFecha.getHoraActual());

			if (ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		
		return false;
	}
	
	
	/**
	 * Este metodo esta encargado de Modificar los datos ya existentes en ls BD
	 * Los key's del HashMap parametros son:
	 * -------------------------------
	 * 		 KEY'S PARAMETROS
	 * -------------------------------
	 * --
	 * @param connection
	 * @param parametros
	 * @return
	 */
	public static boolean modificarConceptosPagosPooles (Connection  connection, HashMap parametros)
	{
		String cadena = strCadenaModificacionConceptosPagoPooles,where=" WHERE codigo=? AND institucion=?";
		//se verifica si tiene permisos de modificar el codigo o no
		if (parametros.containsKey("permisomodificarcodigo") && parametros.get("permisomodificarcodigo").toString().equals("true"))
			cadena+=", codigo='"+parametros.get("codigoconcepto").toString()+"'";
		
		cadena+=where;
		//logger.info("valor de los parametros ==> "+parametros);
		logger.info("cadena de modificacion de insertarConceptosPagoPooles ==> "+cadena);
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			ps.setString(1, parametros.get("descripcionconcepto").toString());
			ps.setString(2, parametros.get("tipoconcepto").toString());
			ps.setString(3, parametros.get("usuario").toString());
			ps.setDate(4,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(5,UtilidadFecha.getHoraActual());
			ps.setString(6, parametros.get("codigoconceptoold").toString());
			ps.setInt(7, Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			
			if(ps.executeUpdate()>0)
				return true;
		
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
		
		return false;
	}
	
	
	
	/**
	 * Este metodo se encarga de Consultar los datos de la tabla conceptos_pagos_pooles
	 * Los key's del HashMap parametros son:
	 * ---------------------------------
	 * 		KEY'S DE PARAMETROS
	 * ---------------------------------
	 * --institucion --> Requerido
	 * 
	 * Los key del mapa que devuelve son:
	 * -------------------------------
	 *  KEY'S DEL MAPA QUE DEVUELVE
	 * ------------------------------
	 * --codigoConcepto, descripcionConcepto, 
	 *   tipoConcepto, estabd, INDICES_MAPA.
	 * @param connection
	 * @param parametros
	 * @return
	 **/
	public static HashMap consultarConceptosPagosPooles (Connection connection, HashMap parametros)
	{
		HashMap mapa = new HashMap ();
		String cadena = strCadenaConsultaConceptosPagoPooles;
		
		if (parametros.containsKey("codigoconcepto"))
			cadena+=" AND cpp.codigo='"+parametros.get("codigoconcepto").toString()+"'";
		
		
		cadena+=" ORDER BY cpp.descripcion";
		logger.info("cadena de consutla de ConceptosPagoPooles ==> "+cadena);
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1,Utilidades.convertirAEntero( parametros.get("institucion").toString()));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));	
			
		}
		catch (Exception e)
		{
			e.printStackTrace(); 	
		}
		
				
		mapa.put("INDICES_MAPA",indicesMapa);
		
		return mapa;
	}
	
	/**
	 * Metodo encargado de eliminar un registro dentro
	 * del a tabla conceptos_pagos_pooles.
	 * Los key's de paramtros son:
	 * ---------------------------
	 * 	  KEY'S DE PARAMETROS
	 * ---------------------------
	 * --codigo --> Requerido
	 * --institucion --> Requerido
	 * @param connection
	 * @param parametros
	 * @return
	 */
	public static boolean eliminarConceptosPagosPooles (Connection connection, HashMap parametros)
	{
		String cadena = strCadenaEliminacionConceptosPagoPooles;
		//se verifica si tiene permisos de modificar el codigo o no
			//	logger.info("valor del hassmap  al entrar a eliminarConceptosPagosPooles "+parametros);
		logger.info("cadena de modificacion de eliminarConceptosPagosPooles ==> "+cadena);
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, parametros.get("codigoconcepto").toString());
			ps.setInt(2, Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			
			
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (Exception e) 
		{
			e.printStackTrace(); 	
		}
		
		return false;
	}
	
	
	/*-----------------------------------------------------
	 * 		FIN METODOS DE CONCEPTO DE PAGO DE POOLES
	 * ---------------------------------------------------*/	
	
	
	
}