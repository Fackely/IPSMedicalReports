package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.Utilidades;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * @author Mauricio Jaramillo H.
 * Fecha: Agosto de 2008
 */

public class SqlBaseConsultaCostoArticulosDao
{

	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseConsultaCostoArticulosDao.class);
	
	/**
	 * Cadena SELECT para consultar los costos de articulos
	 */
	private static String strConSelCostoArticulos = "SELECT " +
														"getCodArtAxiomaInterfazTipo(aa.articulo, ?) AS codigoarticulo, " +
														"a.descripcion AS descripcionarticulo, " +
														"getnomunidadmedida(a.unidad_medida) AS unidadmedida, " +
														"aa.almacen AS codalmacen, " +
														"cc.nombre AS nomalmacen, " +
														"a.costo_promedio AS costopromedio " +
													"FROM " +
														"articulos_almacen aa " +
														"INNER JOIN centros_costo cc ON (aa.almacen = cc.codigo) " +
														"INNER JOIN articulo a ON (aa.articulo = a.codigo) " +
														"INNER JOIN subgrupo_inventario si ON (a.subgrupo = si.codigo) " +
													"WHERE ";
	
	/**
	 * Método para armar las condiciones de la
	 * consulta para ser mandada al BIRT y poder
	 * ejecutar el reporte 
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static String consultarCondicionesCostoArticulos(Connection con, HashMap criterios)
	{
		String condiciones = "";
		
		//**********************INICIO ARMANDO LAS CONDICIONES******************************
		//Filtramos la consulta por el centro de atención. Como es requerido no se hace necesario validarlo
		condiciones += "cc.centro_atencion = "+criterios.get("centroAtencion")+" ";
		//Filtramos la consulta por el almacén. Como no es requerido se hace necesario validarlo
		if(UtilidadCadena.noEsVacio(criterios.get("codAlmacen")+""))
			condiciones += "AND aa.almacen = "+criterios.get("codAlmacen")+" ";
		//Filtramos la consulta por la clase de inventrio. Comoo no es requerida se debe validar
		if(UtilidadCadena.noEsVacio(criterios.get("clase")+""))
			condiciones += "AND si.clase = "+criterios.get("clase")+" ";
		//Filtramos la consulta por el grupo. Comoo no es requerida se debe validar
		if(UtilidadCadena.noEsVacio(criterios.get("grupo")+""))
			condiciones += "AND si.grupo = "+criterios.get("grupo")+" ";
		//Filtramos la consulta por el subgrupo. Comoo no es requerida se debe validar
		if(UtilidadCadena.noEsVacio(criterios.get("subGrupo")+""))
			condiciones += "AND si.subgrupo = "+criterios.get("subGrupo")+" ";		
		//Filtramos la consulta por el subgrupo. Comoo no es requerida se debe validar
		if(UtilidadCadena.noEsVacio(criterios.get("articulo")+""))
			condiciones += "AND a.codigo = "+criterios.get("articulo")+" ";
		
		//Agregamos el Order By a la Consulta
		condiciones += "ORDER BY a.descripcion ";
		//**********************FIN ARMANDO LAS CONDICIONES*********************************
			
		return condiciones;
	}

	/**
	 * Método encargado de consultar los costos
	 * de articulos por almacén y devolver los
	 * resultados en un mapa 
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static HashMap consultarCostoArticulos(Connection con, HashMap criterios)
	{
		HashMap mapa = new HashMap<String, Object>();
	    mapa.put("numRegistros","0");
	    String consulta = strConSelCostoArticulos+consultarCondicionesCostoArticulos(con, criterios);
	    try
	    {
	    	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    	ps.setInt(1, Utilidades.convertirAEntero(criterios.get("tipoCodigo")+""));
	    	logger.info("====>Consulta de Costo de Artículos: "+consulta);
	        mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
	    }
	    catch (SQLException e)
	    {
	        logger.error("ERROR EJECUNTANDO LA CONSULTA DE MOVIMIENTOS DEUDORES POR FACTURA");
	        e.printStackTrace();
	    }
	    return (HashMap)mapa.clone();
	}
	
	
	
}