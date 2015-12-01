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
 * @author Mauricio Jaramillo Henao
 * Fecha: Agosto de 2008
 */

public class SqlBaseConsultaSaldosCierresInventariosDao
{

	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseConsultaSaldosCierresInventariosDao.class);
	
	/**
	 * Cadena SELECT para consultar los saldos de cierres de inventarios
	 */
	private static String strConSelSaldosCierresInventarios = "SELECT " +
																	"dci.almacen AS codalmacen, " +
																	"getnomcentrocosto(dci.almacen) AS nomalmacen, " +
																	"a.codigo AS codarticulo, " +
																	"coalesce(a.codigo_interfaz, '') AS codinterfaz, " +
																	"(" +
																		"CASE WHEN a.concentracion IS NOT NULL AND a.concentracion != '' THEN " +
																		"(" +
																			"CASE WHEN a.forma_farmaceutica IS NOT NULL AND a.forma_farmaceutica != '' THEN " +
																				"a.descripcion || ' - Conc: ' || a.concentracion || ' - F.F.: '|| a.forma_farmaceutica " +
																			"ELSE " +
																				"a.descripcion || ' - Conc: ' || a.concentracion " +
																			"END" +
																		") " +
																		"ELSE " +
																		"(" +
																			"CASE WHEN a.forma_farmaceutica IS NOT NULL AND a.forma_farmaceutica != '' THEN " +
																				"a.descripcion || ' - F.F.: '|| a.forma_farmaceutica " +
																			"ELSE " +
																				"a.descripcion " +
																			"END" +
																		") " +
																	"END) AS desarticulo, " +
																	"getunidadmedidaarticulo(a.codigo) AS unidadmedida, " +
																	"coalesce(dci.lote, '') AS lote, " +
																	"coalesce(to_char(dci.fecha_vencimiento, 'DD/MM/YYYY'), '') AS fechavencimiento, " +
																	"getsaldoanteriorcierreinv(?, ?, a.codigo, dci.almacen, ?, "+ConstantesBD.codigoTipoCierreInventarioSaldoInicialStr+", "+ConstantesBD.codigoTipoCierreInventarioAnualStr+", "+ConstantesBD.codigoTipoCierreInventarioMensualStr+",dci.lote) AS saldoanterior, " +
																	"dci.cantidad_total_entradas AS cantidadentradas, " +
																	"dci.cantidad_total_salidas AS cantidadsalidas, " +
																	"dci.costo_unitario_final_mes AS costounitario " +
															"FROM " +
																"cierre_inventarios ci " +
																"INNER JOIN det_cierre_inventarios dci ON (ci.codigo = dci.codigo_cierre) " +
																"INNER JOIN articulo a ON (dci.articulo = a.codigo) " +
																"INNER JOIN subgrupo_inventario si ON (a.subgrupo = si.codigo) " +
															"WHERE "; 
	
	/**
	 * Cadena GROUP BY para consultar los saldos de cierres de inventarios
	 */
	private static String strConGroupSaldosCierresInventarios = "GROUP BY " +
																	"dci.almacen, a.codigo, a.codigo_interfaz, a.concentracion, a.forma_farmaceutica, a.descripcion, dci.lote, dci.fecha_vencimiento, dci.cantidad_total_entradas, dci.cantidad_total_salidas, dci.costo_unitario_final_mes " +
																"ORDER BY " +
																	"nomalmacen, desarticulo";
	
	/**
	 * Método encargado de consultar los saldos
	 * de cierres de inventarios y devolver los
	 * resultados en un mapa
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static HashMap consultarSaldosCierresInventarios(Connection con, HashMap criterios)
	{
		HashMap mapa = new HashMap<String, Object>();
        mapa.put("numRegistros","0");
        String consulta = strConSelSaldosCierresInventarios+consultarCondicionesSaldosCierresInventarios(con, criterios)+strConGroupSaldosCierresInventarios;
        try
        {
        	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
        	ps.setInt(1, Utilidades.convertirAEntero(criterios.get("anio")+""));
        	ps.setInt(2, Utilidades.convertirAEntero(criterios.get("mes")+""));
        	ps.setInt(3, Utilidades.convertirAEntero(criterios.get("institucion")+""));
        	logger.info("====>Consulta Saldos Cierres de Inventarios: "+consulta+ "==>Año: "+criterios.get("anio")+" ==>Mes: "+criterios.get("mes"));
            mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA DE MOVIMIENTOS DEUDORES POR FACTURA");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
	}
	
	/**
	 * Método para armar las condiciones de la
	 * consulta para ser mandada al BIRT y poder
	 * ejecutar el reporte 
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static String consultarCondicionesSaldosCierresInventarios(Connection con, HashMap criterios)
	{
		String condiciones = "";
		
		//**********************INICIO ARMANDO LAS CONDICIONES******************************
        //Filtramos la consulta por el Centro de Atención. Como es requerido no es necesario validarlo
		//condiciones = "ci.centro_atencion = "+criterios.get("centroAtencion")+" ";
		//Filtramos la consulta por el Año de Cierre y el Mes de Cierre. Como son requeridos no es necesario validarlo
		condiciones += "ci.anio_cierre = "+criterios.get("anio")+" AND ci.mes_cierre = "+criterios.get("mes")+" ";
		//Filtramos la consulta por el Almacén. Como no es requerido toca validarlo si no fue seleccionado
		if(UtilidadCadena.noEsVacio(criterios.get("codAlmacen")+""))
			condiciones += "AND dci.almacen = "+criterios.get("codAlmacen")+" ";
		//Filtramos la consulta por la clase de inventrio. Comoo no es requerida se debe validar
		if(UtilidadCadena.noEsVacio(criterios.get("clase")+""))
			condiciones += "AND si.clase = "+criterios.get("clase")+" ";
		//Filtramos la consulta por el grupo. Comoo no es requerida se debe validar
		if(UtilidadCadena.noEsVacio(criterios.get("grupo")+""))
			condiciones += "AND si.grupo = "+criterios.get("grupo")+" ";
		//Filtramos la consulta por el subgrupo. Comoo no es requerida se debe validar
		if(UtilidadCadena.noEsVacio(criterios.get("subGrupo")+""))
			condiciones += "AND si.codigo = "+criterios.get("subGrupo")+" ";		
		//Filtramos la consulta por el subgrupo. Comoo no es requerida se debe validar
		if(UtilidadCadena.noEsVacio(criterios.get("articulo")+""))
			condiciones += "AND a.codigo = "+criterios.get("articulo")+" ";
		//**********************FIN ARMANDO LAS CONDICIONES*********************************
			
		return condiciones;
	}
	
	
	
}