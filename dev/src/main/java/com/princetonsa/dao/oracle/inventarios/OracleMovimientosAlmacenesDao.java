package com.princetonsa.dao.oracle.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import util.ConstantesIntegridadDominio;
import util.UtilidadCadena;
import util.UtilidadFecha;

import com.princetonsa.dao.inventarios.MovimientosAlmacenesDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseMovimientoAlmacenesDao;

public class OracleMovimientosAlmacenesDao implements MovimientosAlmacenesDao 
{
	
	/**
	 * 
	 */
	public HashMap consultarTransacciones(Connection con, String entradaSalida) 
	{
		return SqlBaseMovimientoAlmacenesDao.consultarTransacciones(con, entradaSalida);
	}
	
	/**
	 * 
	 */
	public HashMap consultarMovimientos(Connection con, HashMap criterios) 
   	{
		return SqlBaseMovimientoAlmacenesDao.consultarMovimientos(con, criterios);
	}

	/**
     * 
     */
    public void insertarLog(Connection con, HashMap criterios)
    {
    	SqlBaseMovimientoAlmacenesDao.insertarLog(con, criterios);
    }
	
    
	
	/**
	 * 
	 * @param centroAtencion
	 * @param almacen
	 * @param indicativoES
	 * @param codigoTransacciones
	 * @param transaccionInicial
	 * @param transaccionFinal
	 * @param tipoReporte
	 * @param codigoAImprimir
	 * @return
	 */
	public String obtenerSqlReporte(String centroAtencion, String almacen,
			String indicativoES, String codigoTransacciones,
			String transaccionInicial, String transaccionFinal,
			String tipoReporte, String codigoAImprimir){
		
		String sql="";
		String condiciones="";
		
		//******************************INICIO VALIDACIONES EN EL WHERE DE LA CONSULTA***************************
		//Filtramos por el campo centro de atencion. Como es requerido no es necesario validarlo
		condiciones = "cc.centro_atencion = "+centroAtencion+" ";
		//Validamos el campo Almacen. Ya que no es requerido
		if(UtilidadCadena.noEsVacio(almacen))
			condiciones += "AND vmi.almacen = '"+almacen+"' ";
		//Validamos si se consultaron transacciones de tipo Entrada ó Salida
		if(UtilidadCadena.noEsVacio(indicativoES))
			condiciones += "AND tti.tipos_conceptos_inv = "+indicativoES+" ";
		//Validamos el campo Tipos de Transacción que se alla señalado uno por lo menos. Ya que no es requerido
		if(UtilidadCadena.noEsVacio(codigoTransacciones))
			condiciones += "AND vmi.codigotransaccion IN ("+codigoTransacciones+") ";
		//Validamos los campos números de Transacción. Ya que son exlcuyentes con las fechas por lo consiguiente no son requeridos
		if(UtilidadCadena.noEsVacio(transaccionInicial) && UtilidadCadena.noEsVacio(transaccionFinal))
			condiciones += "AND vmi.codigomovimiento BETWEEN '"+transaccionInicial+"' AND '"+transaccionFinal+"' ";
		//Validamos los campos fecha de Transacción. Ya que son exlcuyentes con los números de transacción por lo consiguiente no son requeridos		
		if(UtilidadCadena.noEsVacio(transaccionInicial) && UtilidadCadena.noEsVacio(transaccionFinal))
			condiciones += "AND vmi.fechaatencion BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(transaccionInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(transaccionFinal)+"' ";
		//******************************FIN VALIDACIONES EN EL WHERE DE LA CONSULTA*******************************
		
		//Validamos que tipo de reporte se esta usando para llamar al DataSet correcto
		if(tipoReporte.equals(ConstantesIntegridadDominio.acronimoDetalladoTipoTransaccion))
			sql = "SELECT " +
					"vmi.documento AS codigomovimiento, " +
					"vmi.codigotransaccion AS codigotransaccion, " +
					"vmi.almacen AS codalmacen, " +
					"UPPER (getnomcentrocosto(vmi.almacen)) AS nomalmacen, " +
					"getconsecutivotipotransaccion(vmi.codigotransaccion) AS numtransaccion, " +
					"getdesctipotransinventario(getconsecutivotipotransaccion(vmi.codigotransaccion)) AS destransaccion, " +
					"vmi.fechaatencion AS fechacierre, " +
					"getCodArtAxiomaInterfazTipo(vmi.articulo, "+codigoAImprimir+") AS codigoarticulo, " +
					"( CASE WHEN getconcentracionarticulo(vmi.articulo) IS NOT NULL AND getconcentracionarticulo(vmi.articulo) != '' THEN ( CASE WHEN getformafarmaceuticaarticulo(vmi.articulo) IS NOT NULL AND getformafarmaceuticaarticulo(vmi.articulo) != '' THEN getdescripcionarticulo(vmi.articulo) || ' - Conc: ' || getconcentracionarticulo(vmi.articulo) || ' - F.F.: '|| getformafarmaceuticaarticulo(vmi.articulo) ELSE getdescripcionarticulo(vmi.articulo) || ' - Conc: ' || getconcentracionarticulo(vmi.articulo) END) ELSE ( CASE WHEN getformafarmaceuticaarticulo(vmi.articulo) IS NOT NULL AND getformafarmaceuticaarticulo(vmi.articulo) != '' THEN getdescripcionarticulo(vmi.articulo) || ' - F.F.: '|| getformafarmaceuticaarticulo(vmi.articulo) ELSE getdescripcionarticulo(vmi.articulo) END) END) AS desarticulo, " +
					"getporcentajeivaarticulo(vmi.articulo) AS porcentaje, " +
					"getunidadmedidaarticulo(vmi.articulo) AS unidadmedida, " +
					"CASE WHEN vmi.cantidadentrada='0' THEN sum(coalesce(vmi.cantidadsalida, '0')) ELSE sum(coalesce(vmi.cantidadentrada, '0')) END AS cantidad, " +
					"sum(vmi.costounitario) AS valorunitario, " +
					"sum(vmi.valorentrada) AS valortotal, " +
					"coalesce(getporcentajeivaarticulo(vmi.articulo), 0) AS porcentaje, " +
					"gettipoconceptotransinv(vmi.codigotransaccion) AS tipoconceptoinv, " +
					"UPPER (getDescripConceptoTipoTransInv(gettipoconceptotransinv(vmi.codigotransaccion))) AS descptipoconcepto " +
				"FROM " +
					"view_movimientos_inventarios vmi " +
				"INNER JOIN " +
					"centros_costo cc ON (cc.codigo = vmi.almacen) " +
				"INNER JOIN " +
					"tipos_trans_inventarios tti ON (tti.codigo = vmi.codigotransaccion) " +
				"WHERE "+condiciones+" "+
				"GROUP BY " +
					"vmi.documento, vmi.almacen, vmi.codigotransaccion, vmi.fechaatencion, vmi.articulo,vmi.cantidadentrada " +
				"ORDER BY " +
					"vmi.codigotransaccion, " +
					"getnomcentrocosto(vmi.almacen), " +
					"getconsecutivotipotransaccion(vmi.codigotransaccion), " +
					"vmi.fechaatencion";
		
		else if(tipoReporte.equals(ConstantesIntegridadDominio.acronimoDetalladoProveedorTransaccion))
			sql = "SELECT " +
					"vmi.documento AS codigomovimiento, " +
					"vmi.codigotransaccion AS codigotransaccion, " +
					"vmi.almacen AS codalmacen, " +
					"UPPER (getnomcentrocosto(vmi.almacen)) AS nomalmacen, " +
					"vmi.almacendestino AS codalmacendestino, " +
					"CASE WHEN vmi.almacendestino<>'' AND vmi.almacendestino IS NOT NULL THEN getnomcentrocosto(vmi.almacendestino) ELSE '' END AS nomalmacendestino, " +
					"getconsecutivotipotransaccion(vmi.codigotransaccion) AS numtransaccion, " +
					"getdesctipotransinventario(getconsecutivotipotransaccion(vmi.codigotransaccion)) AS destransaccion, " +
					"vmi.fechaatencion AS fechacierre, " +
					"vmi.usuariomovimiento AS usuario, " +
					"getCodArtAxiomaInterfazTipo(vmi.articulo, "+codigoAImprimir+") AS codigoarticulo, " +
					"CASE WHEN vmi.tercero IS NOT NULL AND vmi.tercero != '' THEN getnumeroidentificaciontercero(vmi.tercero) ELSE '' END AS proveedor, " +
					"CASE WHEN vmi.tercero IS NOT NULL AND vmi.tercero != '' THEN getnumeroidentificaciontercero(vmi.tercero) || ' - ' || getdescripciontercero(vmi.tercero) ELSE 'SIN PROVEEDOR REGISTRADO' END AS razonsocial, " +
					"( CASE WHEN getconcentracionarticulo(vmi.articulo) IS NOT NULL AND getconcentracionarticulo(vmi.articulo) != '' THEN ( CASE WHEN getformafarmaceuticaarticulo(vmi.articulo) IS NOT NULL AND getformafarmaceuticaarticulo(vmi.articulo) != '' THEN getdescripcionarticulo(vmi.articulo) || ' - Conc: ' || getconcentracionarticulo(vmi.articulo) || ' - F.F.: '|| getformafarmaceuticaarticulo(vmi.articulo) ELSE getdescripcionarticulo(vmi.articulo) || ' - Conc: ' || getconcentracionarticulo(vmi.articulo) END) ELSE ( CASE WHEN getformafarmaceuticaarticulo(vmi.articulo) IS NOT NULL AND getformafarmaceuticaarticulo(vmi.articulo) != '' THEN getdescripcionarticulo(vmi.articulo) || ' - F.F.: '|| getformafarmaceuticaarticulo(vmi.articulo) ELSE getdescripcionarticulo(vmi.articulo) END) END) AS desarticulo, " +
					"getunidadmedidaarticulo(vmi.articulo) AS unidadmedida, " +
					"CASE WHEN vmi.cantidadentrada='0' THEN sum(coalesce(vmi.cantidadsalida, '0')) ELSE sum(coalesce(vmi.cantidadentrada, '0')) END AS cantidad, " +
					"sum(vmi.costounitario) AS valorunitario, " +
					"sum(vmi.valorentrada) AS valortotal, " +
					"coalesce(getporcentajeivaarticulo(vmi.articulo), 0) AS porcentaje, " +
					"gettipoconceptotransinv(vmi.codigotransaccion) AS tipoconceptoinv, " +
					"UPPER (getDescripConceptoTipoTransInv(gettipoconceptotransinv(vmi.codigotransaccion))) AS descptipoconcepto " +
				"FROM " +
					"view_movimientos_inventarios vmi " +
				"INNER JOIN " +
					"centros_costo cc ON (cc.codigo = vmi.almacen) " +
				"INNER JOIN " +
					"tipos_trans_inventarios tti ON (tti.codigo = vmi.codigotransaccion) " +
				"WHERE "+condiciones+" " +
				"GROUP BY " +
					"vmi.documento, vmi.almacen, vmi.almacendestino, vmi.codigotransaccion, vmi.fechaatencion, vmi.usuariomovimiento, vmi.articulo, vmi.tercero,vmi.cantidadentrada " +
				"ORDER BY " +
					"vmi.codigotransaccion, " +
					"getnomcentrocosto(vmi.almacen), " +
					"getconsecutivotipotransaccion(vmi.codigotransaccion), " +
					"vmi.fechaatencion";
		
		else if(tipoReporte.equals(ConstantesIntegridadDominio.acronimoTotalizadoAlmacenTransaccion))
			sql = "SELECT " +
					"vmi.documento AS codigomovimiento, " +
					"vmi.codigotransaccion AS codigotransaccion, " +
					"vmi.almacen AS codalmacen, " +
					"UPPER (getnomcentrocosto(vmi.almacen)) AS nomalmacen, " +
					"getconsecutivotipotransaccion(vmi.codigotransaccion) AS numtransaccion, " +
					"getdesctipotransinventario(getconsecutivotipotransaccion(vmi.codigotransaccion)) AS destransaccion, " +
					"vmi.fechaatencion AS fechacierre, " +
					"CASE WHEN vmi.tercero IS NOT NULL AND vmi.tercero != '' THEN getnumeroidentificaciontercero(vmi.tercero) ELSE '' END AS proveedor, " +
					"CASE WHEN vmi.tercero IS NOT NULL AND vmi.tercero != '' THEN getdescripciontercero(vmi.tercero)  ELSE '' END AS razonsocial, " +
					"vmi.usuariomovimiento AS usuario, " +
					"CASE WHEN vmi.cantidadentrada='0' THEN sum(coalesce(vmi.cantidadsalida, '0')) ELSE sum(coalesce(vmi.cantidadentrada, '0')) END AS cantidad, " +
					"sum(vmi.costounitario) AS valorunitario, +" +
					"sum(vmi.valorentrada) AS valortotal " +
				"FROM " +
					"view_movimientos_inventarios vmi " +
				"INNER JOIN " +
					"centros_costo cc ON (cc.codigo = vmi.almacen) " +
				"INNER JOIN " +
					"tipos_trans_inventarios tti ON (tti.codigo = vmi.codigotransaccion) " +
				"WHERE "+condiciones+"  " +
				"GROUP BY " +
					"vmi.documento, vmi.codigotransaccion, vmi.almacen, vmi.fechaatencion, vmi.tercero, vmi.usuariomovimiento,vmi.cantidadentrada " +
				"ORDER BY " +
					"vmi.codigotransaccion, " +
					"getconsecutivotipotransaccion(vmi.codigotransaccion), " +
					"vmi.fechaatencion";
		
		else if(tipoReporte.equals(ConstantesIntegridadDominio.acronimoEntradasSalidasTotalizadasAlmacen))
			sql = "SELECT " +
					"vmi.documento AS codigomovimiento, " +
					"vmi.codigotransaccion AS codigotransaccion, " +
					"vmi.almacen AS codalmacen, " +
					"UPPER (getnomcentrocosto(vmi.almacen)) AS nomalmacen, " +
					"getconsecutivotipotransaccion(vmi.codigotransaccion) AS numtransaccion, " +
					"getdesctipotransinventario(getconsecutivotipotransaccion(vmi.codigotransaccion)) AS destransaccion, " +
					"vmi.fechaatencion AS fechacierre, " +
					"sum(vmi.cantidadentrada) AS cantidadentrada, " +
					"sum(vmi.valorentrada) AS valorentrada, " +
					"sum(vmi.cantidadsalida) AS cantidadsalida, " +
					"sum(vmi.valorsalida) AS valorsalida " +
				"FROM " +
					"view_movimientos_inventarios vmi " +
				"INNER JOIN " +
					"centros_costo cc ON (cc.codigo = vmi.almacen) " +
				"INNER JOIN " +
					"tipos_trans_inventarios tti ON (tti.codigo = vmi.codigotransaccion) " +
				"WHERE "+condiciones+" " +
				"GROUP BY " +
					"vmi.documento,vmi.almacen, vmi.codigotransaccion, vmi.fechaatencion " +
				"ORDER BY " +
					"vmi.codigotransaccion, " +
					"getnomcentrocosto(vmi.almacen), " +
					"vmi.fechaatencion";
		
		else if(tipoReporte.equals(ConstantesIntegridadDominio.acronimoEntradasSalidasArticulo))
			sql = "SELECT " +
					"vmi.almacen AS codalmacen, " +
					"UPPER (getnomcentrocosto(vmi.almacen)) AS nomalmacen, " +
					"vmi.articulo AS articulo, " +
					"getCodArtAxiomaInterfazTipo(vmi.articulo, "+codigoAImprimir+") AS codaxiomainterfaz, " +
					"( CASE WHEN getconcentracionarticulo(vmi.articulo) IS NOT NULL AND getconcentracionarticulo(vmi.articulo) != '' THEN ( CASE WHEN getformafarmaceuticaarticulo(vmi.articulo) IS NOT NULL AND getformafarmaceuticaarticulo(vmi.articulo) != '' THEN getdescripcionarticulo(vmi.articulo) || ' - Conc: ' || getconcentracionarticulo(vmi.articulo) || ' - F.F.: '|| getformafarmaceuticaarticulo(vmi.articulo) ELSE getdescripcionarticulo(vmi.articulo) || ' - Conc: ' || getconcentracionarticulo(vmi.articulo) END) ELSE ( CASE WHEN getformafarmaceuticaarticulo(vmi.articulo) IS NOT NULL AND getformafarmaceuticaarticulo(vmi.articulo) != '' THEN getdescripcionarticulo(vmi.articulo) || ' - F.F.: '|| getformafarmaceuticaarticulo(vmi.articulo) ELSE getdescripcionarticulo(vmi.articulo) END) END) AS desarticulo, " +
					"getunidadmedidaarticulo(vmi.articulo) AS unidadarticulo, " +
					"sum(vmi.cantidadentrada) AS entradas, " +
					"sum(vmi.cantidadsalida) AS salidas, " +
					"vmi.costounitario AS valorunitario, " +
					"getporcentajeivaarticulo(vmi.articulo) AS porcentaje " +
				"FROM " +
					"view_movimientos_inventarios vmi " +
					"INNER JOIN centros_costo cc ON (cc.codigo = vmi.almacen) " +
					"INNER JOIN tipos_trans_inventarios tti ON (tti.codigo = vmi.codigotransaccion) " +
				"WHERE " +condiciones+" " +
				"GROUP BY " +
					"vmi.almacen, vmi.articulo, vmi.costounitario " +
				"ORDER BY " +
					"getnomcentrocosto(vmi.almacen), " +
					"getdescripcionarticulo(vmi.articulo) ";
		
		
		return sql;
	}
    
}
