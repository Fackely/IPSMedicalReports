package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;

/**
 * @author Mauricio Jaramillo
 * Fecha: Junio de 2008
 */

public class SqlBaseCostoInventarioPorFacturarDao
{

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseCostoInventarioPorFacturarDao.class); 
	
	/**
	 * Cadena que consulta los costos de inventario por facturar
	 */
	private static String strConCostoInventario = "SELECT "+
														/*"getnomcentroatencion(cl.centro_atencion) AS centroatencion, "+
														"getnaturalezaarticulo(cl.articulo) || ' ' || getcodintnaturalezaarticulo(a.naturaleza) AS tipoinventario, "+
														"cl.centro_costo_solicitado || ' ' || getnomcentrocosto(cl.centro_costo_solicitado) AS centrocosto, "+
														"sum(a.costo_promedio) AS costo, "+
														"sum(cl.valor_total_cargado) AS valorprecio "+*/
														"COUNT(1) "+
												  "FROM "+
												  		"consumos_liquidados cl "+
												  		"INNER JOIN articulo a ON (cl.articulo=a.codigo) "+
												  "WHERE "+
												  		"cl.tipo_solicitud IN ("+ConstantesBD.codigoTipoSolicitudMedicamentos+","+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+") ";
	
	/**
	 * Se modifico y no se llama este metodo por el cambio de la tarea 26828
	 * que pide quitar el mensaje de verificacion de numero de registros
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static HashMap consultarCostoInventarioPorFacturar(Connection con, HashMap criterios)
	{
		HashMap mapa = new HashMap<String, Object>();
		mapa.put("numRegistros","0");
		String consulta = strConCostoInventario;
		
		//Filtramos la consulta por la fecha de corte. No es necesario validar porque es requerida
		consulta += "AND to_char(cl.fecha_corte, 'YYYY/MM') = '"+criterios.get("anoCorte")+"/"+criterios.get("mesCorte")+"' ";
		
		//Filtramos la consulta por el centro de atencion. Como no es requerido se hace necesario validarlo
		if(UtilidadCadena.noEsVacio(criterios.get("centroAtencion")+""))
			consulta += "AND cl.centro_atencion = "+criterios.get("centroAtencion")+" ";
		
		//Filtramos la consulta por el centro de costo almacen. Como no es requerido se hace necesario validarlo
		if(UtilidadCadena.noEsVacio(criterios.get("centroCosto")+""))
			consulta += "AND cl.centro_costo_principal = "+criterios.get("centroCosto")+" ";
		
		//Filtramos la consulta por el centro de costo almacen. Como no es requerido se hace necesario validarlo
		if(UtilidadCadena.noEsVacio(criterios.get("tipoInventario")+""))
		{
			consulta += "AND a.naturaleza = '"+criterios.get("tipoInventario")+"' ";
			consulta += "AND a.institucion = "+criterios.get("institucion")+" ";
		}
		
		//Metemos el Group by y el Order by
		//consulta += "GROUP BY centroatencion, tipoinventario, centrocosto ORDER BY centroatencion, tipoinventario";
		
        try
        {
        	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            logger.info("\n\n====>Consulta Costos Inventarios Por Facturar: "+consulta);
            mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA DE COSTOS DE INVENTARIOS POR FACTURAR");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
	}
	
}
