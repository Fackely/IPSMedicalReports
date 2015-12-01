package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;

import com.princetonsa.dao.sqlbase.facturacion.SqlBaseCostoInventarioPorFacturarDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * @author Mauricio Jllo
 * Fecha: Junio de 2008
 */

public class SqlBaseCostoInventariosPorAlmacenDao
{

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseCostoInventarioPorFacturarDao.class);
	
	/**
	 * Cadena para la consulta de los costos de inventario por almacen
	 */
	private static String strConInventarioXAlmacen = "SELECT "+
														"cc.centro_atencion AS codigocentroatencion, " +
														"getnomcentroatencion(cc.centro_atencion) AS nombrecentroatencion, " +
														"aa.almacen AS codigoalmacen, " +
														"getnomcentrocosto(aa.almacen) AS nombrealmacen, " +
														"CASE WHEN sum(a.costo_promedio*aa.existencias) <= 0 THEN 0 ELSE sum(a.costo_promedio*aa.existencias) END AS costopromedio " +
													"FROM " +
														"articulos_almacen aa " +
														"INNER JOIN articulo a ON (aa.articulo = a.codigo) " +
														"INNER JOIN centros_costo cc ON (aa.almacen = cc.codigo) " +
													"WHERE ";

	/**
	 * Metodo para consultar los costo de inventario
	 * por almacen
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static HashMap consultarCostoInventarioPorAlmacen(Connection con, HashMap criterios)
	{
		HashMap mapa = new HashMap<String, Object>();
		mapa.put("numRegistros","0");
		
		String consulta = strConInventarioXAlmacen;
		consulta += "aa.institucion = "+criterios.get("institucion")+" ";
		if(UtilidadCadena.noEsVacio(criterios.get("centroAtencion")+""))
			consulta += "AND cc.centro_atencion = "+criterios.get("centroAtencion")+" ";
		if(UtilidadCadena.noEsVacio(criterios.get("centroCosto")+""))
			consulta += "AND aa.almacen = "+criterios.get("centroCosto")+" ";
		consulta += "GROUP BY aa.almacen, cc.centro_atencion ORDER BY nombrecentroatencion, nombrealmacen";
			
		try
        {
        	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            logger.info("\n====>Consulta Costos de Inventarios Por Almacen: "+consulta);
            mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA DE COSTOS DE INVENTARIOS POR ALMACEN");
            e.printStackTrace();
        }
		return (HashMap)mapa.clone();
	}


	
}
