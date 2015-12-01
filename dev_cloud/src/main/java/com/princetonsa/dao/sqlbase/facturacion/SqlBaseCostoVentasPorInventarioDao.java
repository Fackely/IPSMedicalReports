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
import util.UtilidadFecha;

/**
 * @author Mauricio Jllo
 * Fecha: Junio de 2008
 */

public class SqlBaseCostoVentasPorInventarioDao
{

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseCostoVentasPorInventarioDao.class); 
	
	/**
	 * Cadena que consulta los costos de inventario por facturar
	 */
	private static String strConCostoVentas = "SELECT "+
														"getnomcentroatencion(f.centro_aten) AS centroatencion, "+
														"getnomcentrocosto(sm.centro_costo_principal) AS almacen, "+
														"getcodintcentrocosto(s.centro_costo_solicitante) AS codinterfazsolicitante, "+
														"getnomcentrocosto(s.centro_costo_solicitante) AS solicitante, "+
														"getcodintnaturalezaarticulo(a.naturaleza) AS codinterfaznaturaleza, "+
														"getnaturalezaarticulo(dc.articulo) AS tipoinventario "+
												  "FROM "+
												  		"det_cargos dc "+
												  		"INNER JOIN articulo a ON (dc.articulo = a.codigo) "+
												  		"INNER JOIN solicitudes s ON (dc.solicitud = s.numero_solicitud) "+
												  		"INNER JOIN solicitudes_medicamentos sm ON (s.numero_solicitud = sm.numero_solicitud) "+
												  		"INNER JOIN facturas f ON (dc.codigo_factura = f.codigo) "+
												  "WHERE "+
												  		"dc.tipo_solicitud IN ("+ConstantesBD.codigoTipoSolicitudMedicamentos+","+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+") "+
														"AND f.estado_facturacion<>"+ConstantesBD.codigoEstadoFacturacionAnulada+" ";
	
	/**
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static HashMap consultarCostoVentasPorInventario(Connection con, HashMap criterios)
	{
		HashMap mapa = new HashMap<String, Object>();
		mapa.put("numRegistros","0");
		String consulta = strConCostoVentas;
		
		//Filtramos la consulta por la fecha inicial y la fecha final. No es necesario validar porque es requerida
		consulta += "AND f.fecha between '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"")+"' ";
		
		//Filtramos la consulta por el centro de atencion. Como no es requerido se hace necesario validarlo
		if(UtilidadCadena.noEsVacio(criterios.get("centroAtencion")+""))
			consulta += "AND f.centro_aten = "+criterios.get("centroAtencion")+" ";
		
		//Filtramos la consulta por el centro de costo almacen. Como no es requerido se hace necesario validarlo
		if(UtilidadCadena.noEsVacio(criterios.get("centroCostoAlmacen")+""))
			consulta += "AND sm.centro_costo_principal = "+criterios.get("centroCostoAlmacen")+" ";
		
		//Filtramos la consulta por el centro de costo solicitado. Como no es requerido se hace necesario validarlo
		if(UtilidadCadena.noEsVacio(criterios.get("centroCostoSolicitante")+""))
			consulta += "AND s.centro_costo_solicitante = "+criterios.get("centroCostoSolicitante")+" ";
		
		//Filtramos la consulta por el centro de costo almacen. Como no es requerido se hace necesario validarlo
		if(UtilidadCadena.noEsVacio(criterios.get("tipoInventario")+""))
		{
			consulta += "AND a.naturaleza = '"+criterios.get("tipoInventario")+"' ";
			consulta += "AND a.institucion = "+criterios.get("institucion")+" ";
		}
		
		//Metemos el Group by y el Order by
		consulta += "GROUP BY centroatencion, almacen, codinterfazsolicitante, solicitante, tipoinventario, codinterfaznaturaleza ORDER BY centroatencion, almacen, tipoinventario";
		
        try
        {
        	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            logger.info("\n\n====>Consulta Costos Inventarios Por Facturar: "+consulta);
            mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA DE COSTO DE VENTAS DE INVENTARIOS");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
	}
	
	

}