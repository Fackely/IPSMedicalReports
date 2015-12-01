package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * @author Mauricio Jaramillo
 */

public class SqlBaseMovimientoAlmacenesDao 
{

   /**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseMovimientoAlmacenesDao.class);
	
	/**
	 * Cadena SELECT para ejecutar la consulta de Movimientos de Almacenes: Cadena Común
	 */
	private static String strConSelCamposComunes = "SELECT " +
														"vmi.documento AS codigomovimiento, "+
														"vmi.codigotransaccion AS codigotransaccion, "+
														"getconsecutivotipotransaccion(vmi.codigotransaccion) AS numtransaccion, " +
														"getdesctipotransinventario(getconsecutivotipotransaccion(vmi.codigotransaccion)) AS destransaccion, " +
														"to_char(vmi.fechaatencion::date, 'DD/MM/YYYY') AS fechacierre, " +
														"sum(vmi.cantidadentrada::integer) AS cantidad, " +
														"sum(vmi.costounitario::numeric) AS valorunitario, " +
														"sum(vmi.valorentrada::numeric) AS valortotal, ";
	
	/**
	 * Cadena SELECT para ejecutar la consulta de Movimientos de Almacenes: Cadena agregada por el tipo de reporte: Detallado por Tipo de Transacción
	 */
	private static String strConSelDetalladoTipoTransaccion  = 	"vmi.almacen AS codalmacen, " +
																"UPPER(getnomcentrocosto(vmi.almacen::integer)) AS nomalmacen, " +
																"getCodArtAxiomaInterfazTipo(vmi.articulo::integer, ?) AS codigoarticulo, " +
																//Validamos la concentración y la forma farmaceutica ya que pueden ser nulas o vacias,
																//por ende no se mostrarian en tales casos
																"(CASE WHEN getconcentracionarticulo(vmi.articulo::integer) IS NOT NULL " +
																"AND getconcentracionarticulo(vmi.articulo::integer) != '' " +
																"THEN " +
																	"(CASE WHEN getformafarmaceuticaarticulo(vmi.articulo::integer) IS NOT NULL " +
																	"AND getformafarmaceuticaarticulo(vmi.articulo::integer) != '' " +
																	"THEN " +
																		"getdescripcionarticulo(vmi.articulo::integer) " +
																		"|| ' - Conc: ' || getconcentracionarticulo(vmi.articulo::integer) " +
																		"|| ' - F.F.: '|| getformafarmaceuticaarticulo(vmi.articulo::integer) " +
																	"ELSE " +
																		"getdescripcionarticulo(vmi.articulo::integer) " +
																		"|| ' - Conc: ' || getconcentracionarticulo(vmi.articulo::integer) " +
																	"END) " +
																"ELSE " +
																	"(CASE WHEN getformafarmaceuticaarticulo(vmi.articulo::integer) IS NOT NULL " +
																	"AND getformafarmaceuticaarticulo(vmi.articulo::integer) != '' " +
																	"THEN " +
																		"getdescripcionarticulo(vmi.articulo::integer) " +
																		"|| ' - F.F.: '|| getformafarmaceuticaarticulo(vmi.articulo::integer) " +
																	"ELSE " +
																		"getdescripcionarticulo(vmi.articulo::integer) " +
																	"END) " +
																"END) AS desarticulo, "+
																"getunidadmedidaarticulo(vmi.articulo::integer) AS unidadmedida, " +
																"gettipoconceptotransinv(vmi.codigotransaccion) AS tipoconceptoinv, " +
																"UPPER(getDescripConceptoTipoTransInv(gettipoconceptotransinv(vmi.codigotransaccion))) AS descptipoconcepto ";
	
	/**
	 * Cadena SELECT para ejecutar la consulta de Movimientos de Almacenes: Cadena agregada por el tipo de reporte: Detallado por Proveedor y Tipo de Transacción
	 */
	private static String strConSelDetalladoProveedorTransaccion  = "vmi.almacen AS codalmacen, " +
																	"UPPER(getnomcentrocosto(vmi.almacen::integer)) AS nomalmacen, " +
																	"getCodArtAxiomaInterfazTipo(vmi.articulo::integer, ?) AS codigoarticulo, " +
																	"vmi.almacendestino AS codalmacendestino, " +
																	"CASE WHEN vmi.almacendestino<>'' AND vmi.almacendestino IS NOT NULL THEN getnomcentrocosto(vmi.almacendestino::integer) ELSE '' END AS nomalmacendestino, " +
																	//Validamos la concentración y la forma farmaceutica ya que pueden ser nulas o vacias,
																	//por ende no se mostrarian en tales casos
																	"(CASE WHEN getconcentracionarticulo(vmi.articulo::integer) IS NOT NULL " +
																	"AND getconcentracionarticulo(vmi.articulo::integer) != '' " +
																	"THEN " +
																		"(CASE WHEN getformafarmaceuticaarticulo(vmi.articulo::integer) IS NOT NULL " +
																		"AND getformafarmaceuticaarticulo(vmi.articulo::integer) != '' " +
																		"THEN " +
																			"getdescripcionarticulo(vmi.articulo::integer) " +
																			"|| ' - Conc: ' || getconcentracionarticulo(vmi.articulo::integer) " +
																			"|| ' - F.F.: '|| getformafarmaceuticaarticulo(vmi.articulo::integer) " +
																		"ELSE " +
																			"getdescripcionarticulo(vmi.articulo::integer) " +
																			"|| ' - Conc: ' || getconcentracionarticulo(vmi.articulo::integer) " +
																		"END) " +
																	"ELSE " +
																		"(CASE WHEN getformafarmaceuticaarticulo(vmi.articulo::integer) IS NOT NULL " +
																		"AND getformafarmaceuticaarticulo(vmi.articulo::integer) != '' " +
																		"THEN " +
																			"getdescripcionarticulo(vmi.articulo::integer) " +
																			"|| ' - F.F.: '|| getformafarmaceuticaarticulo(vmi.articulo::integer) " +
																		"ELSE " +
																			"getdescripcionarticulo(vmi.articulo::integer) " +
																		"END) " +
																	"END) AS desarticulo, "+
																	"vmi.usuariomovimiento AS usuario, " +
																	"CASE WHEN vmi.tercero IS NOT NULL AND vmi.tercero != '' THEN getnumeroidentificaciontercero(vmi.tercero::integer) ELSE '' END AS proveedor, " +
																	"CASE WHEN vmi.tercero IS NOT NULL AND vmi.tercero != '' THEN getdescripciontercero(vmi.tercero::integer)  ELSE '' END AS razonsocial, " +
																	"gettipoconceptotransinv(vmi.codigotransaccion) AS tipoconceptoinv, " +
																	"UPPER (getDescripConceptoTipoTransInv(gettipoconceptotransinv(vmi.codigotransaccion))) AS descptipoconcepto ";
	
	/**
	 * Cadena SELECT para ejecutar la consulta de Movimientos de Almacenes: Cadena agregada por el tipo de reporte: Totalizado por Almacén
	 */
	private static String strConSelTotalizadoPorAlmacen = "CASE WHEN vmi.tercero IS NOT NULL AND vmi.tercero != '' THEN getnumeroidentificaciontercero(vmi.tercero::integer) ELSE '' END AS proveedor, "+
														  "CASE WHEN vmi.tercero IS NOT NULL AND vmi.tercero != '' THEN getdescripciontercero(vmi.tercero::integer)  ELSE '' END AS razonsocial, "+
														  "vmi.usuariomovimiento AS usuario ";
	
	/**
	 * Cadena SELECT para ejecutar la consulta de Movimientos de Almacenes: Cadena única por el tipo de reporte: Entradas y Salidas Totalizadas por Almacén
	 */
	private static String strConSelEntradasSalidasPorAlmacen = "SELECT " +
																	"vmi.documento AS codigomovimiento, "+
																	"vmi.codigotransaccion AS codigotransaccion, " +
																	"vmi.almacen AS codalmacen, " +
																	"UPPER(getnomcentrocosto(vmi.almacen::integer)) AS nomalmacen, " +
																	"getconsecutivotipotransaccion(vmi.codigotransaccion) AS numtransaccion, " +
																	"getdesctipotransinventario(getconsecutivotipotransaccion(vmi.codigotransaccion)) AS destransaccion, " +
																	"to_char(vmi.fechaatencion::date, 'DD/MM/YYYY') AS fechacierre, " +
																	"sum(vmi.cantidadentrada::integer) AS cantidadentrada, " +
																	"sum(vmi.valorentrada::numeric) AS valorentrada, " +
																	"sum(vmi.cantidadsalida::integer) AS cantidadsalida, " +
																	"sum(vmi.valorsalida::numeric) AS valorsalida ";
	
	/**
	 * Cadena SELECT para ejecutar la consulta de Movimientos Almacenes: Cadena única por el tipo de reporte: Entradas y Salidas Artículos
	 */
	private static String strConSelEntradasSalidasArticulos = "SELECT "+
																"vmi.almacen AS codalmacen, " +
        													  	"UPPER(getnomcentrocosto(vmi.almacen::integer)) AS nomalmacen, " +
        													  	"vmi.articulo AS articulo, " +
        													  	"getCodArtAxiomaInterfazTipo(vmi.articulo::integer, ?) AS codaxiomainterfaz, " +
        													  	"( CASE WHEN getconcentracionarticulo(vmi.articulo::integer) IS NOT NULL AND getconcentracionarticulo(vmi.articulo::integer) != '' THEN ( CASE WHEN getformafarmaceuticaarticulo(vmi.articulo::integer) IS NOT NULL AND getformafarmaceuticaarticulo(vmi.articulo::integer) != '' THEN getdescripcionarticulo(vmi.articulo::integer) || ' - Conc: ' || getconcentracionarticulo(vmi.articulo::integer) || ' - F.F.: '|| getformafarmaceuticaarticulo(vmi.articulo::integer) ELSE getdescripcionarticulo(vmi.articulo::integer) || ' - Conc: ' || getconcentracionarticulo(vmi.articulo::integer) END) ELSE ( CASE WHEN getformafarmaceuticaarticulo(vmi.articulo::integer) IS NOT NULL AND getformafarmaceuticaarticulo(vmi.articulo::integer) != '' THEN getdescripcionarticulo(vmi.articulo::integer) || ' - F.F.: '|| getformafarmaceuticaarticulo(vmi.articulo::integer) ELSE getdescripcionarticulo(vmi.articulo::integer) END) END) AS desarticulo, " +
        													  	"getunidadmedidaarticulo(vmi.articulo::integer) AS unidadarticulo, " +
        													  	"sum(vmi.cantidadentrada::integer) AS entradas, " +
        													  	"sum(vmi.cantidadsalida::integer) AS salidas, " +
        													  	"vmi.costounitario::numeric AS valorunitario ";
	
	/**
	 * Cadena FROM y palabra WHERE para ejcutar la consulta de Movimientos de Almacenes
	 */
	private static String strConFromMovimientoAlmacenes = "FROM " +
																"view_movimientos_inventarios vmi " +
																"INNER JOIN centros_costo cc ON (cc.codigo = vmi.almacen::integer) " +
																"INNER JOIN tipos_trans_inventarios tti ON (tti.codigo = vmi.codigotransaccion) " +
															"WHERE ";

	/**
	 * Cadena GROUP BY y ORDER BY para ejecutar el tipo de reporte: Detallado por Tipo de Transacción
	 */	
	private static String strConGroupDetalladoTipoTransaccion = "GROUP BY " +
																	"vmi.documento, vmi.almacen, vmi.codigotransaccion, vmi.fechaatencion, vmi.articulo " +
																"ORDER BY " +
																	"vmi.codigotransaccion, nomalmacen, numtransaccion, fechacierre";
	
	/**
	 * Cadena GROUP BY y ORDER BY para ejecutar el tipo de reporte: Detallado por Proveedor y Tipo de Transacción
	 */	
	private static String strConGroupDetalladoProveedorTipoTransaccion = "GROUP BY " +
																			"vmi.documento, vmi.almacen, vmi.almacendestino, vmi.codigotransaccion, vmi.fechaatencion, vmi.usuariomovimiento, vmi.articulo, vmi.tercero " +
																		"ORDER BY " +
																			"vmi.codigotransaccion, numtransaccion, fechacierre";

	/**
	 * Cadena GROUP BY y ORDER BY para ejecutar el tipo de reporte: Totalizado por Almacén
	 */
	private static String strConGroupTotalizadoPorAlmacen = "GROUP BY " +
																"vmi.documento, vmi.codigotransaccion, vmi.fechaatencion, vmi.tercero, vmi.usuariomovimiento " +
															"ORDER BY " +
																"vmi.codigotransaccion, numtransaccion, fechacierre";
	
	/**	
	 * Cadena GROUP BY y ORDER BY para ejecutar el tipo de reporte: Entradas y Salidas Totalizadas por Almacén
	 */
	private static String strConGroupEntradasSalidasPorAlmacen = "GROUP BY " +
																	"vmi.documento, vmi.almacen, vmi.codigotransaccion, vmi.fechaatencion " +
																"ORDER BY " +
																	"vmi.codigotransaccion, nomalmacen, vmi.fechaatencion";
	
	/**
	 * Cadena GROUP BY y ORDER BY para ejecutar el tipo de reportes: Entradas y Salidas por Artículo
	 */
	private static String strConGroupEntradasSalidasPorArticulo = "GROUP BY " +
    																	"vmi.almacen, vmi.articulo, vmi.costounitario " +
    															  "ORDER BY " +
    															  		"nomalmacen, desarticulo";
	
	/**
	 * Cadena para insertar el Log de Movimientos Almacenes
	 */
	private static String strInsLogMovimientosAlmacenes = "INSERT INTO log_movimientos_almacenes " +
														  	"(" +
																"codigo, " +
																"fecha_grabacion, " +
																"hora_grabacion, " +
																"usuario, " +
																"centro_atencion, " +
																"almacen, " +
																"fecha_inicial, " +
																"fecha_final, " +
																"indicativo, " +
																"transaccion_inicial, " +
																"transaccion_final, " +
																"tipo_codigo, " +
																"nombre_reporte, " +
																"tipo_salida, " +
																"ruta_archivo_plano " +
															")" +
														  "VALUES " +
														  	"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	
	/**
	 * Metodo que consulta las Tipos de Transacciones
	 * @param con
	 * @return
	 */
	public static HashMap consultarTransacciones(Connection con, String entradaSalida) 
	{
		HashMap mapa = new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			String cadena = "SELECT consecutivo AS consecutivo, descripcion AS descripcion, codigo AS codigo, coalesce(codigo_interfaz, '') AS codigointerfaz FROM tipos_trans_inventarios WHERE tipos_conceptos_inv = "+entradaSalida+" ORDER BY descripcion";  
			logger.info("====>Consulta Transacciones Inventarios: "+cadena);
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarTransacciones: "+e);
		}
		return mapa;
	}
	
	/**
	 * Metodo que consulta los movimientos con respecto a los tipos de transaccion
	 * @param mundo
	 * @param oldQuery
	 * @param usuario
	 * @return
	 */
	public static HashMap consultarMovimientos(Connection con, HashMap criterios)
	{
		HashMap mapa = new HashMap<String, Object>();
        mapa.put("numRegistros","0");
        String consulta = "", groupBy = "", codigoAImprimir = "";
         
        //Validaciones que tipo de codigo se selecciono para imprimir en el reporte por Tipo de Transacción
        logger.info("====>Tipo de Articulo a Imprimir: "+criterios.get("tipoCodigoArticulo"));
		if((criterios.get("tipoCodigoArticulo")+"").equals(ConstantesIntegridadDominio.acronimoAxioma))
			codigoAImprimir = "1";
		else if((criterios.get("tipoCodigoArticulo")+"").equals(ConstantesIntegridadDominio.acronimoInterfaz))
			codigoAImprimir = "2";
		else if((criterios.get("tipoCodigoArticulo")+"").equals(ConstantesIntegridadDominio.acronimoAmbos))
			codigoAImprimir = "3";
        
		/*
         * Seleccionamos la consulta correspondiente según el tipo de reporte seleccionado
         * con el fin de concatenar o armar la consulta, utilizando la cadena del select
         * común más los atributos particulares, más la cadena FROM y la cadena GROUP
         * correspondiente. También validamos si el tipo de reporte utiliza la variable para
         * saber que código se va imprimir: Axioma, Interfaz, Ambos
         */
        logger.info("====>Tipo de Reporte: "+criterios.get("tipoReporte"));
        if(criterios.get("tipoReporte").equals(ConstantesIntegridadDominio.acronimoDetalladoTipoTransaccion))
        {
        	consulta = strConSelCamposComunes+strConSelDetalladoTipoTransaccion+strConFromMovimientoAlmacenes;
        	groupBy = strConGroupDetalladoTipoTransaccion;
        }
        else if(criterios.get("tipoReporte").equals(ConstantesIntegridadDominio.acronimoDetalladoProveedorTransaccion))
        {
        	consulta = strConSelCamposComunes+strConSelDetalladoProveedorTransaccion+strConFromMovimientoAlmacenes;
        	groupBy = strConGroupDetalladoProveedorTipoTransaccion;
        }
        else if(criterios.get("tipoReporte").equals(ConstantesIntegridadDominio.acronimoTotalizadoAlmacenTransaccion))
        {
        	consulta = strConSelCamposComunes+strConSelTotalizadoPorAlmacen+strConFromMovimientoAlmacenes;
        	groupBy = strConGroupTotalizadoPorAlmacen;
        }
        else if(criterios.get("tipoReporte").equals(ConstantesIntegridadDominio.acronimoEntradasSalidasTotalizadasAlmacen))
        {
        	consulta = strConSelEntradasSalidasPorAlmacen+strConFromMovimientoAlmacenes;
        	groupBy = strConGroupEntradasSalidasPorAlmacen;
        }
        else if(criterios.get("tipoReporte").equals(ConstantesIntegridadDominio.acronimoEntradasSalidasArticulo))
        {
        	consulta = strConSelEntradasSalidasArticulos+strConFromMovimientoAlmacenes;
        	groupBy = strConGroupEntradasSalidasPorArticulo;
        }
        
        //***************INICIO FILTROS GENERALES PARA CADA UNO DE LOS TIPOS DE REPORTE********************************
        //Filtramos la consulta por el centro de atencion. Como es requerido no se valida
		consulta += "cc.centro_atencion = "+criterios.get("centroAtencion")+" ";
		//Filtramos la consulta por el almacen. Como no es requerido se debe validar
		if(UtilidadCadena.noEsVacio(criterios.get("almacen")+""))
			consulta += "AND vmi.almacen = '"+criterios.get("almacen")+"'";
		//Filtramos la consulta por el tipo de transacciones escogidas en el filtro
		logger.info("====>Tipos de Transacciones Escogidas: "+criterios.get("tiposTransacciones"));
		if(UtilidadCadena.noEsVacio(criterios.get("tiposTransacciones")+""))
			consulta += "AND vmi.codigotransaccion IN ("+criterios.get("tiposTransacciones")+") ";
		//Filtramos por el indicativo de entrada y de salida 
		if(UtilidadCadena.noEsVacio(criterios.get("indicativoES")+""))
			consulta += "AND tti.tipos_conceptos_inv = "+criterios.get("indicativoES")+" ";
		//Filtramos la consulta por el rango de números de transacción. Como no es requerido se debe validar
		if(UtilidadCadena.noEsVacio(criterios.get("transaccionInicial")+"") && UtilidadCadena.noEsVacio(criterios.get("transaccionFinal")+""))
			consulta += "AND vmi.documento BETWEEN '"+criterios.get("transaccionInicial")+"' AND '"+criterios.get("transaccionFinal")+"' ";
		//Filtramos la consulta por las fechas de transacción. Como no es requerido se debe validar
		if(UtilidadCadena.noEsVacio(criterios.get("fechaInicial")+"") && UtilidadCadena.noEsVacio(criterios.get("fechaFinal")+""))
			consulta += "AND vmi.fechaatencion BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"")+"' ";
		//*****************FIN FILTROS GENERALES PARA CADA UNO DE LOS TIPOS DE REPORTE*********************************
        	
		//Concatenamos el Group By a la consulta
		consulta += groupBy;
		try
        {
        	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        	//Validamos el tipo de reporte con el fin de saber que consultas son las que necesitan el parámetro
        	if(criterios.get("tipoReporte").equals(ConstantesIntegridadDominio.acronimoDetalladoTipoTransaccion))
        		ps.setObject(1, codigoAImprimir);
        	else if(criterios.get("tipoReporte").equals(ConstantesIntegridadDominio.acronimoDetalladoProveedorTransaccion))
        		ps.setObject(1, codigoAImprimir);
        	else if(criterios.get("tipoReporte").equals(ConstantesIntegridadDominio.acronimoEntradasSalidasArticulo))
        		ps.setObject(1, codigoAImprimir);
            logger.info("===>Consulta Movimientos Almacenes: "+consulta+ " ===>Codigo Imprimir: "+codigoAImprimir);
            mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA DE MOVIMIENTOS ALMACENES");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
	}
	
	/**
	 * Método encargado de insertar el log tipo
	 * base de datos de la funcionalidad
	 * @param con
	 * @param criterios
	 */
	public static void insertarLog(Connection con, HashMap criterios)
	{
		try
        {
        	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strInsLogMovimientosAlmacenes, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
        	ps.setInt(1, Utilidades.convertirAEntero(UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_log_movimientos_almacenes")+""));
        	ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
            ps.setString(3, UtilidadFecha.getHoraActual(con));
            ps.setString(4, criterios.get("usuario")+"");
            ps.setString(5, criterios.get("centroAtencion")+"");
            ps.setString(6, criterios.get("almacen")+"");
            
            if(UtilidadCadena.noEsVacio(criterios.get("fechaInicial")+""))
            	ps.setDate(7, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"")));
            else
            	ps.setNull(7, Types.DATE);
            
            if(UtilidadCadena.noEsVacio(criterios.get("fechaFinal")+""))
            	ps.setDate(8, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"")));
            else
            	ps.setNull(8, Types.DATE);
            
            if(UtilidadCadena.noEsVacio(criterios.get("indicativoEs")+""))
            	ps.setString(9, criterios.get("indicativoEs")+"");
            else
            	ps.setNull(9, Types.VARCHAR);
            
            if(UtilidadCadena.noEsVacio(criterios.get("transaccionInicial")+""))
            	ps.setString(10, criterios.get("transaccionInicial")+"");
            else
            	ps.setNull(10, Types.VARCHAR);
            
            if(UtilidadCadena.noEsVacio(criterios.get("transaccionFinal")+""))
            	ps.setString(11, criterios.get("transaccionFinal")+"");
            else
            	ps.setNull(11, Types.VARCHAR);
            
            ps.setString(12, criterios.get("tipoCodigoArticulo")+"");
            ps.setString(13, criterios.get("nombreReporte")+"");
            ps.setString(14, criterios.get("tipoSalida")+"");
            
            if(UtilidadCadena.noEsVacio(criterios.get("archivoPlano")+""))
            	ps.setString(15, criterios.get("archivoPlano")+"");
            else
            	ps.setNull(15, Types.VARCHAR);
            
            if(ps.executeUpdate()>0)
            	logger.info("====>SE INSERTO CORRECTAMENTE EL LOG DE MOVIMIENTOS ALMACENES");
            
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA INSERCION EN EL LOG DE MOVIMIENTOS ALMACENES");
            e.printStackTrace();
        }
	}
	

}