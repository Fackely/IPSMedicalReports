/*
 * Marzo 28, del 2007 
 */
package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * @author Sebastián Gómez 
 *
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la funcionalidad consulta articulos fecha de vencimiento
 */
public class SqlBaseArticulosFechaVencimientoDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseArticulosFechaVencimientoDao.class);
	
	/**
	 * Seccion SELECt para consultar los articulos x fecha de corte
	 */
	private static final String consultarArticulosXFecha_SELECT = "SELECT "+
																		"al.articulo AS codigo_articulo, "+
																		"a.descripcion || ' Conc:' || a.concentracion || ' F.F:'|| getnomformafarmaceutica(a.forma_farmaceutica) AS descripcion_articulo, "+
																		"getnomunidadmedida(a.unidad_medida) As unidad_medida, "+
																		"CASE WHEN al.lote IS NULL THEN '' ELSE al.lote END as lote, "+
																		"to_char(al.fecha_vencimiento,'DD/MM/YYYY') AS fecha_vencimiento, "+
																		"sum(al.existencias) AS existencias "+ 
																	"FROM " +
																		"articulo_almacen_x_lote al "+ 
																	"INNER JOIN " +
																		"articulo a ON(a.codigo=al.articulo) "+ 
																	"WHERE "+  
																		"al.fecha_vencimiento IS NOT NULL and "+ 
																		"to_char(al.fecha_vencimiento, 'YYYY-MM-DD') <= ? and "+ 
																		"al.existencias > 0 "; 
	
	/**
	 * Sección group by y order by para consultar los articulos x fecha de corte
	 */
	private static final String consultarArticulosXFecha_GROUP_ORDER = "GROUP BY " +
																			"al.articulo, " +
																			"a.descripcion, " +
																			"unidad_medida, " +
																			"lote, " +
																			"fecha_vencimiento, " +
																			"a.concentracion, " +
																			"a.forma_farmaceutica "+ 
																		"ORDER BY " +
																			"al.fecha_vencimiento ";
	
	/**
	 * Seccion SELECt que consulta el detalle de los almacenes de un articulo, lote y fecha de vencimiento
	 */
	private static final String consultarDetalleAlmacenesArticulo_SELECT = "SELECT "+ 
																				"al.almacen AS codigo_almacen, "+
																				"getnomcentrocosto(al.almacen) AS nombre_almacen, "+
																				"getcentroatencioncc(al.almacen) AS nombre_centro_atencion, "+
																				"sum(al.existencias) AS existencias "+ 
																			"FROM " +
																				"articulo_almacen_x_lote al "+ 
																			"WHERE "+
																				"al.articulo = ? AND "+
																				"al.existencias > 0 AND "+ 
																				"to_char(al.fecha_vencimiento, 'YYYY-MM-DD') = ? ";
		
	/**
	 * Seccion ORDER BY que consulta el detalle de los almacenes de un articulo, lote y fecha de vencimiento
	 */
	private static final String consultarDetalleAlmacenesArticulo_ORDER = " GROUP BY " +
		"al.almacen "+
		"ORDER BY nombre_almacen,nombre_centro_atencion";
	
	/**
	 * Seccion SELECT para la consulta impresion de articulos x fecha
	 */
	private static final String consultaImpresionArticulosXFecha_SELECT = "SELECT "+ 
		"al.almacen AS codigo_almacen, "+
		"getnomcentrocosto(al.almacen) AS nombre_almacen, "+
		"getcentroatencioncc(al.almacen) AS nombre_centro_atencion, "+
		"al.articulo AS codigo_articulo, "+
		"a.descripcion || ' Conc:' || a.concentracion || ' F.F:'|| getnomformafarmaceutica(a.forma_farmaceutica) AS descripcion_articulo, "+
		"getnomunidadmedida(a.unidad_medida) As unidad_medida, "+
		"CASE WHEN al.lote IS NULL THEN '' ELSE al.lote END as lote, "+
		"to_char(al.fecha_vencimiento,'DD/MM/YYYY') AS fecha_vencimiento, "+
		"sum(al.existencias) AS existencias "+ 
		"FROM articulo_almacen_x_lote al "+ 
		"INNER JOIN articulo a ON(a.codigo=al.articulo) "+ 
		"WHERE "+ 
		"al.fecha_vencimiento IS NOT NULL and "+ 
		"to_char(al.fecha_vencimiento, 'YYYY-MM-DD') <= ? and "+ 
		"al.existencias > 0 ";
	
	/**
	 * Seccion GROUP BY y ORDER BY para la consulta impresion de articulos x fecha
	 */
	private static final String consultaImpresionArticulosXFecha_GROUP_ORDER = "GROUP BY " +
		" al.almacen,al.articulo,a.descripcion,a.concentracion,a.forma_farmaceutica,a.unidad_medida,al.lote,al.fecha_vencimiento "+ 
		"ORDER BY nombre_almacen,nombre_centro_atencion,descripcion_articulo,al.fecha_vencimiento";
	
	/**
	 * Método implementado para consultar las existencias de una articulo por fecha de vencimiento
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap consultarArticulosXFecha(Connection con, HashMap campos)
	{
		try
		{
			HashMap mapa = new HashMap();
			String consulta = consultarArticulosXFecha_SELECT;
			
			//se verifica si se va a hacer la consulta por codigo
			if(!campos.get("codigoArticulo").toString().equals(""))
			{	
				//Modificado por la Tarea 38488 lo cual argumenta que debe evaluar el parametro general Código Manual para Búsqueda de Artículos (Axioma, Interfaz)
		         if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(Utilidades.convertirAEntero(campos.get("institucion")+"")).equals(ConstantesIntegridadDominio.acronimoAxioma))
		         {
		             logger.info("===>Se realiza el filtro por el Código Axioma");
		        	 consulta+=" AND a.codigo = "+campos.get("codigoArticulo")+" ";
		         }
		         else if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(Utilidades.convertirAEntero(campos.get("institucion")+"")).equals(ConstantesIntegridadDominio.acronimoInterfaz))
		         {
		        	 logger.info("===>Se realiza el filtro por el Código Interfaz");
		        	 consulta+=" AND a.codigo_interfaz = '"+campos.get("codigoArticulo")+"' ";
		         }
			}
			//se verifica si se va a hacer la busqueda por descripcion
			if(!campos.get("descripcionArticulo").toString().equals(""))
				consulta += " AND UPPER(a.descripcion) like UPPER('%"+campos.get("descripcionArticulo")+"%') ";
			
			consulta += consultarArticulosXFecha_GROUP_ORDER;
			
			logger.info("SQL / "+consulta);
			logger.info("Param > "+campos.get("fecha"));
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDate(1, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(campos.get("fecha").toString())));
			
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			
			//********Se itera mapa para consultar el detalle de los almacenes**********
			for(int i=0;i<Utilidades.convertirAEntero(mapa.get("numRegistros").toString());i++)
			{
				consulta = consultarDetalleAlmacenesArticulo_SELECT ;
				
				if(mapa.get("lote_"+i).toString().equals(""))
					consulta += " AND al.lote IS NULL ";
				else
					consulta += " AND al.lote = '"+mapa.get("lote_"+i)+"' ";
				
				consulta += consultarDetalleAlmacenesArticulo_ORDER;
					
				logger.info("SQL / "+consulta);
				logger.info("Param > "+mapa.get("codigoArticulo_"+i));
				logger.info("Param > "+mapa.get("fechaVencimiento_"+i));
				
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1,Utilidades.convertirAEntero(mapa.get("codigoArticulo_"+i)+""));
				pst.setDate(2,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(mapa.get("fechaVencimiento_"+i).toString())));
				
				HashMap detalle = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
				int total = 0;
				//********se calcula el total de existencias por articulo*********************
				for(int j=0;j<Utilidades.convertirAEntero(detalle.get("numRegistros").toString());j++)
					total += Utilidades.convertirAEntero(detalle.get("existencias_"+j).toString());
				detalle.put("total", total+"");
				//*****************************************************************************
				mapa.put("detalle_"+i,detalle);
			}
			//**************************************************************************
			
			return mapa;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarArticulosXFecha: ",e);
			return null;
		}
	}
	
	/**
	 * Método implementado para consultar la impresion de las existencias de una articulo por fecha de vencimiento y por almacen
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap consultaImpresionArticulosXFecha(Connection con,HashMap campos)
	{
		try
		{
			String consulta = consultaImpresionArticulosXFecha_SELECT;
			
			//se verifica si se va a hacer la consulta por codigo
			if(!campos.get("codigoArticulo").toString().equals(""))
				consulta += " AND al.articulo = "+campos.get("codigoArticulo");
			//se verifica si se va a hacer la busqueda por descripcion
			if(!campos.get("descripcionArticulo").toString().equals(""))
				consulta += " AND UPPER(a.descripcion) like UPPER('%"+campos.get("descripcionArticulo")+"%') ";
			
			consulta += consultaImpresionArticulosXFecha_GROUP_ORDER;
			
			logger.info("---->"+consulta);
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDate(1, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(campos.get("fecha").toString())));
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,true);
			pst.close();
			return mapaRetorno;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en consultaImpresionArticulosXFecha: ",e);
			return null;
		}
	}
	
}
