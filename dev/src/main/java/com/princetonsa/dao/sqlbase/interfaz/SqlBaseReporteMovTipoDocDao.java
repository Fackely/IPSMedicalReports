package com.princetonsa.dao.sqlbase.interfaz;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.interfaz.DtoMovimientoTipoDocumento;
import java.sql.Connection;
import java.sql.Date;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Jairo Gómez Fecha Julio de 2009
 */

public class SqlBaseReporteMovTipoDocDao {

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseReporteMovTipoDocDao.class);
	
	private static final String consultaFacturasPacientesStr = 
					
					"SELECT tc.descripcion AS tipo_convenio    , " +
					  "SUM(f.valor_total)   AS valor_facturacion, " +
					  "SUM(CASE WHEN af.codigo IS NULL THEN 0 ELSE f.valor_total END) AS valor_anulacion " +
					   "FROM facturacion.facturas f " +
					"INNER JOIN facturacion.convenios c " +
					     "ON (c.codigo = f.convenio) " +
					"INNER JOIN facturacion.tipos_convenio tc " +
					     "ON(tc.codigo  = c.tipo_convenio " +
					"AND tc.institucion = c.institucion) " +
					"LEFT OUTER JOIN facturacion.anulaciones_facturas af " +
					     "ON(af.codigo = f.codigo " +
					"AND to_char(af.fecha_grabacion, 'YYYY-MM-DD') BETWEEN ? AND ?) " +
					  "WHERE to_char(f.fecha, 'YYYY-MM-DD') BETWEEN ? AND ? " +
					"GROUP BY tc.descripcion " +
					"ORDER BY tipo_convenio";
	
	private static final String consultaIngresosStr = 
					" SELECT centro_atencion, " +
					  "grupo                , " +
					  "nombre               , " +
					  "valor_ingreso        , " +
					  "valor_anulacion " +
					   "FROM " +
					  "(SELECT administracion.getnomcentroatencion(f.centro_aten) AS centro_atencion, " +
					    "'grupo servicio'                          AS grupo          , " +
					    "historiaclinica.getnombregruposervicio(s.grupo_servicio)  AS nombre         , " +
					    "SUM(df.valor_total)                       AS valor_ingreso  , " +
					    "SUM(CASE WHEN af.codigo IS NULL THEN 0 ELSE df.valor_total END) AS valor_anulacion " +
					     "FROM facturacion.facturas f " +
					  "INNER JOIN facturacion.det_factura_solicitud df " +
					       "ON(df.factura = f.codigo) " +
					  "INNER JOIN facturacion.servicios s " +
					       "ON(s.codigo = df.servicio) " +
					  "LEFT OUTER JOIN facturacion.anulaciones_facturas af " +
					       "ON(af.codigo = f.codigo " +
					  "AND to_char(af.fecha_grabacion, 'YYYY-MM-DD') BETWEEN ? AND ?) " +
					    "WHERE to_char(f.fecha, 'YYYY-MM-DD') BETWEEN ? AND ? " +
					 "GROUP BY f.centro_aten, " +
					    "s.grupo_servicio " +
					    "" +
					    "UNION " +
					    "" +
					   "SELECT administracion.getnomcentroatencion(f.centro_aten) AS centro_atencion, " +
					    "'clase inventario'                        AS grupo          , " +
					    "facturacion.getnombreclaseinventario(si.clase)        AS nombre         , " +
					    "SUM(df.valor_total)                       AS valor_ingreso  , " +
					    "SUM(CASE WHEN af.codigo IS NULL THEN 0 ELSE df.valor_total END) AS valor_anulacion " +
					     "FROM facturacion.facturas f " +
					  "INNER JOIN facturacion.det_factura_solicitud df " +
					       "ON(df.factura = f.codigo) " +
					  "INNER JOIN inventarios.articulo a " +
					       "ON(a.codigo = df.articulo) " +
					  "INNER JOIN inventarios.subgrupo_inventario si " +
					       "ON(si.codigo = a.subgrupo) " +
					  "LEFT OUTER JOIN facturacion.anulaciones_facturas af " +
					       "ON(af.codigo = f.codigo " +
					  "AND to_char(af.fecha_grabacion, 'YYYY-MM-DD') BETWEEN ? AND ?) " +
					    "WHERE to_char(f.fecha, 'YYYY-MM-DD') BETWEEN ? AND ? " +
					 "GROUP BY f.centro_aten, " +
					    "si.clase " +
					  ") t " +
					"ORDER BY t.centro_atencion, " +
					  "t.grupo DESC            , " +
					  "t.nombre";
	
	private static final String consultaFacturasVariasStr = 
					" SELECT cfv.descripcion AS concepto         , " +
					  "cfv.consecutivo       AS codigo_concepto  , " +
					  "SUM(fv.valor_factura) AS valor_facturacion, " +
					  "SUM( " +
					  "CASE " +
					    "WHEN fv.estado_factura = 'ANU' " +
					    "AND to_char(fv.fecha_anulacion, 'YYYY-MM-DD') BETWEEN ? AND ? " +
					    "THEN fv.valor_factura " +
					    "ELSE 0 " +
					  "END) AS valor_anulacion " +
					   "FROM facturasvarias.facturas_varias fv " +
					"INNER JOIN facturasvarias.conceptos_facturas_varias cfv " +
					     "ON(cfv.consecutivo = fv.concepto) " +
					  "WHERE to_char(fv.fecha, 'YYYY-MM-DD') BETWEEN ? AND ? " +
					"AND (fv.estado_factura = 'APR' " +
					"OR (fv.estado_factura  = 'ANU' " +
					"AND fv.anula_fac_apro  = 'S')) " +
					"GROUP BY cfv.consecutivo, " +
					  "cfv.descripcion";
	
	private static final String consultaValorAjustesFactVariasStr = 
					" SELECT SUM( " +
					  "CASE " +
					    "WHEN af.tipo_ajuste  = '" + ConstantesIntegridadDominio.acronimoDebito + "' " +
					    "AND af.valor_ajuste IS NOT NULL " +
					    "THEN af.valor_ajuste " +
					    "ELSE 0 " +
					  "END) AS valor_ajuste_debito, " +
					  "SUM( " +
					  "CASE " +
					    "WHEN af.tipo_ajuste  = '" + ConstantesIntegridadDominio.acronimoCredito + "' " +
					    "AND af.valor_ajuste IS NOT NULL " +
					    "THEN af.valor_ajuste " +
					    "ELSE 0 " +
					  "END) AS valor_ajuste_credito " +
					   "FROM ajus_facturas_varias af " +
					"INNER JOIN facturas_varias fv " +
					     "ON (fv.codigo_fac_var = af.factura) " +
					"INNER JOIN conceptos_facturas_varias cfv " +
					     "ON(cfv.consecutivo = fv.concepto ) " +
					  "WHERE af.estado       = 'APR' " +
					"AND to_char(af.fecha_aprob_anul, 'YYYY-MM-DD') BETWEEN ? AND ? " +
					"AND to_char(cfv.consecutivo, 'YYYY-MM-DD') = ? ";
	
	private static final String consultaRecibosCajaStr = 
					" SELECT fp.descripcion AS forma_pago  , " +
					  "SUM(dpr.valor)       AS valor_recibo, " +
					  "SUM( " +
					  "CASE " +
					    "WHEN arc.numero_recibo_caja IS NULL " +
					    "THEN 0 " +
					    "ELSE dpr.valor " +
					  "END) AS valor_anulacion " +
					   "FROM recibos_caja rc " +
					"INNER JOIN detalle_pagos_rc dpr " +
					     "ON(dpr.numero_recibo_caja = rc.numero_recibo_caja " +
					"AND dpr.institucion            = rc.institucion) " +
					"INNER JOIN formas_pago fp " +
					     "ON(fp.consecutivo = dpr.forma_pago) " +
					"LEFT OUTER JOIN anulacion_recibos_caja arc " +
					     "ON (arc.numero_recibo_caja = rc.numero_recibo_caja " +
					"AND arc.institucion             = rc.institucion " +
					"AND to_char(arc.fecha, 'YYYY-MM-DD') BETWEEN ? AND ?) " +
					  "WHERE to_char(rc.fecha, 'YYYY-MM-DD') BETWEEN ? AND ? " +
					"GROUP BY fp.descripcion " +
					"ORDER BY fp.descripcion";
	
	public static ArrayList<DtoMovimientoTipoDocumento> consultarFacturasPacientes (Connection con, HashMap criterios)
	{
		ArrayList<DtoMovimientoTipoDocumento> arrayList = new ArrayList<DtoMovimientoTipoDocumento>();
		
		try {
			logger.info("SQL / "+consultaFacturasPacientesStr);
			logger.info("Fecha Inicial "+criterios.get("FechaInicial"));
			logger.info("Fecha Final "+criterios.get("FechaFinal"));
			
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(
					consultaFacturasPacientesStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
//			Adición parametros
			
			ps.setString(1, UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaInicial").toString()));
			ps.setString(2, UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaFinal").toString()));
			ps.setString(3, UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaInicial").toString()));
			ps.setString(4, UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaFinal").toString()));
			
//			Ejecución consulta
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			DtoMovimientoTipoDocumento dtoMovimientoTipoDocumento;
			
//			recorrido y guardado de resultado
			
			while(rs.next())
			{
				dtoMovimientoTipoDocumento = new DtoMovimientoTipoDocumento();
				
//				Adición resultado al dto
				dtoMovimientoTipoDocumento.setTipoConenio(rs.getString(1));
				dtoMovimientoTipoDocumento.setValorFacturacion(rs.getDouble(2));
				dtoMovimientoTipoDocumento.setValorAnulacion(rs.getDouble(3));
				
//				Adición dto al ArrayList
				
				arrayList.add(dtoMovimientoTipoDocumento);
			}
			
//			se cierran los objetos
			ps.close();
			rs.close();
			
		} catch (Exception e) {
			logger.info("Error consultando Facturas Pacientes");
			e.printStackTrace();
		}
		return arrayList;
	}
	
	public static ArrayList<DtoMovimientoTipoDocumento> consultarIngresos (Connection con, HashMap criterios)
	{
		ArrayList<DtoMovimientoTipoDocumento> arrayList = new ArrayList<DtoMovimientoTipoDocumento>();
		
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(
					consultaIngresosStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
//			Adición parametros
			ps.setString(1, UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaInicial").toString()));
			ps.setString(2, UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaFinal").toString()));
			ps.setString(3, UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaInicial").toString()));
			ps.setString(4, UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaFinal").toString()));
			ps.setString(5, UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaInicial").toString()));
			ps.setString(6, UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaFinal").toString()));
			ps.setString(7, UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaInicial").toString()));
			ps.setString(8, UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaFinal").toString()));
			
//			Ejecución consulta
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			DtoMovimientoTipoDocumento dtoMovimientoTipoDocumento;
			
//			Almacenamiento de resultado en el dto 
			while (rs.next())
			{
				dtoMovimientoTipoDocumento = new DtoMovimientoTipoDocumento();
				
				dtoMovimientoTipoDocumento.setCentroAtencion(rs.getString(1));
				dtoMovimientoTipoDocumento.setClasificacion(rs.getString(2));
				dtoMovimientoTipoDocumento.setGrupoServicio(rs.getString(3));
				dtoMovimientoTipoDocumento.setValorIngreso(rs.getDouble(4));
				dtoMovimientoTipoDocumento.setValorAnulacion(rs.getDouble(5));
				
//				adicion dto al arreglo
				
				arrayList.add(dtoMovimientoTipoDocumento);
			}
			
//			se cierran los objetos
			ps.close();
			rs.close();
			
		} catch (Exception e) {
			logger.info("Error consultando los Ingresos");
			e.printStackTrace();
		}
		
		return arrayList;
	}
	
	public static ArrayList<DtoMovimientoTipoDocumento> consultarRecibosCaja (Connection con, HashMap criterios)
	{
		ArrayList<DtoMovimientoTipoDocumento> arrayList = new ArrayList<DtoMovimientoTipoDocumento>();
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(
					consultaRecibosCajaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
//			Adición parametros al preparedstatement
			ps.setString(1, UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaInicial").toString()));
			ps.setString(2, UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaFinal").toString()));
			ps.setString(3, UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaInicial").toString()));
			ps.setString(4, UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaFinal").toString()));
			
//			Ejecución consulta
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			DtoMovimientoTipoDocumento dtoMovimientoTipoDocumento;
			
//			Se almacena el resultado
			
			while(rs.next())
			{
				dtoMovimientoTipoDocumento = new DtoMovimientoTipoDocumento();
				
				dtoMovimientoTipoDocumento.setFormaPago(rs.getString(1));
				dtoMovimientoTipoDocumento.setValorRecibo(rs.getDouble(2));
				dtoMovimientoTipoDocumento.setValorAnulacion(rs.getDouble(3));
				
//				Adición dto al arreglo
				arrayList.add(dtoMovimientoTipoDocumento);
			}
			
//			se cierran los objetos
			ps.close();
			rs.close();
			
		} catch (Exception e) {
			logger.info("Error consultando los recibos caja");
			e.printStackTrace();
		}
		return arrayList;
	}
	
	public static ArrayList<DtoMovimientoTipoDocumento> consultarFacturasVarias (Connection con, HashMap criterios)
	{
		ArrayList<DtoMovimientoTipoDocumento> arrayList = new ArrayList<DtoMovimientoTipoDocumento>();
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(
					consultaFacturasVariasStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
//			Adicion parametros
			ps.setString(1, UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaInicial").toString()));
			ps.setString(2, UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaFinal").toString()));
			ps.setString(3, UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaInicial").toString()));
			ps.setString(4, UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaFinal").toString()));
			
//			Ejecución consulta
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			DtoMovimientoTipoDocumento dtoMovimientoTipoDocumento;
			
			while (rs.next())
			{
				dtoMovimientoTipoDocumento = new DtoMovimientoTipoDocumento();
				
				dtoMovimientoTipoDocumento.setConcepto(rs.getString(1));
				dtoMovimientoTipoDocumento.setCodigoConcepto(rs.getInt(2));
				dtoMovimientoTipoDocumento.setValorFacturacion(rs.getDouble(3));
				dtoMovimientoTipoDocumento.setValorAnulacion(rs.getDouble(4));
//				Llamado a funcion que consulta el valor de los ajustes debito y credito
				HashMap<String, Object> map = consultarValorAjustes(con, criterios, dtoMovimientoTipoDocumento.getCodigoConcepto());
				
				dtoMovimientoTipoDocumento.setValorAjusteDebito(Utilidades.convertirADouble(map.get("debito").toString()));
				dtoMovimientoTipoDocumento.setValorAjusteCredito(Utilidades.convertirADouble(map.get("credito").toString()));
				
				arrayList.add(dtoMovimientoTipoDocumento);
			}
			
//			se cierran los objetos
			ps.close();
			rs.close();
		} catch (Exception e) {
			logger.info("Error consultando las facturas Varias");
			e.printStackTrace();
		}
		return arrayList;
	}
	
	public static HashMap<String, Object> consultarValorAjustes (Connection con, HashMap criterios, int codigoConcepto)
	{
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(
					consultaValorAjustesFactVariasStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
//			Adicion parametros
			ps.setString(1, UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaInicial").toString()));
			ps.setString(2, UtilidadFecha.conversionFormatoFechaABD(criterios.get("FechaFinal").toString()));
			ps.setInt(3, codigoConcepto);
						
//			Ejecución consulta
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while (rs.next())
			{
				map.put("debito", rs.getDouble(1));
				map.put("credito", rs.getDouble(2));
			}
			
//			se cierran los objetos
			ps.close();
			rs.close();
		} catch (Exception e) {
			logger.info("Error consultando el valor de los ajustes por el concepto No " + codigoConcepto + "de la Fact. Varia");
			e.printStackTrace();
		}
		return map;
	}
}