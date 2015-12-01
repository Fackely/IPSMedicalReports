package com.princetonsa.dao.sqlbase.glosas;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

public class SqlBaseConsultarImprimirGlosasDao
{	
	/**
	 * Para el manejo de logs
	 */
	private static Logger logger = Logger.getLogger(SqlBaseConsultarImprimirGlosasDao.class);

	
	private static String strConsultarGlosas = "SELECT DISTINCT " +
													"g.codigo, " +
													"g.glosa_sistema AS glosasis, " +
													"to_char(g.fecha_registro_glosa,'dd/mm/yyyy') AS fecharegi, " +
													"getintegridaddominio(g.estado) AS estado, " +
													"g.convenio, " +
													"g.contrato, " +
													"g.glosa_entidad AS entidad, " +
													"to_char(g.fecha_notificacion,'dd/mm/yyyy') AS fechanoti," +
													"g.valor_glosa AS valor, " +
													"getnombreconvenio(g.convenio) AS nomconvenio, " +
													"ct.numero_contrato AS numcontrato, " +
													"coalesce(ag.fue_auditada,' ') AS indicativofueauditada " +
												"FROM " +
													"registro_glosas g " +
												"INNER JOIN " +
													"contratos ct ON (ct.codigo=g.contrato) " +
												"LEFT OUTER JOIN " +
													"auditorias_glosas ag ON (ag.glosa=g.codigo) " +
												" left outer join facturas fac on (fac.codigo=ag.codigo_factura) " +
												"WHERE ";
												
	
	/**
	 * 
	 * @param con
	 * @param listadoGlosasMap
	 * @param codigoInstitucionInt
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap listarGlosas(Connection con,HashMap<String, Object> filtrosMap) {
		
		HashMap listadoGlosas = new HashMap();
		listadoGlosas.put("numRegistros", 0);
		String cadena = strConsultarGlosas;
		
		cadena += "g.institucion="+filtrosMap.get("institucion")+" ";
		
		if (!filtrosMap.get("glosa").toString().equals(""))
			cadena+="AND g.glosa_sistema='"+filtrosMap.get("glosa")+"' ";
				
		if (!filtrosMap.get("glosaE").toString().equals(""))
			cadena+="AND g.glosa_entidad='"+filtrosMap.get("glosaE")+"' ";
		
		if (!filtrosMap.get("convenio").toString().equals(""))
			cadena+="AND g.convenio="+filtrosMap.get("convenio")+" ";
		
		if (!filtrosMap.get("contrato").toString().equals(""))
			cadena+="AND g.contrato="+filtrosMap.get("contrato")+" ";
		
		if (!filtrosMap.get("fechaNoti").toString().equals(""))
			cadena+="AND g.fecha_notificacion='"+UtilidadFecha.conversionFormatoFechaABD(filtrosMap.get("fechaNoti")+"")+"'";

		if (!filtrosMap.get("estadoGlosa").toString().equals(""))
			cadena+="AND g.estado='"+UtilidadFecha.conversionFormatoFechaABD(filtrosMap.get("estadoGlosa")+"")+"'";
		
		if (!filtrosMap.get("indicativoGlosa").toString().equals(""))
			cadena+="AND ag.fue_auditada='"+filtrosMap.get("indicativoGlosa")+"' ";
		
		if(!UtilidadTexto.isEmpty(filtrosMap.get("fechaRegIni").toString()) && !UtilidadTexto.isEmpty((filtrosMap.get("fechaRegFin").toString())))
			cadena+="AND to_char(g.fecha_registro_glosa, 'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(filtrosMap.get("fechaRegIni")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(filtrosMap.get("fechaRegFin")+"")+"' ";
		if(!UtilidadTexto.isEmpty(filtrosMap.get("consecutivoFact")+""))
			cadena+="AND fac.consecutivo_factura='"+filtrosMap.get("consecutivoFact")+"'";
		try {
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			listadoGlosas = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(cadena)));
			listadoGlosas.put("consultasql", cadena);
		}
		catch (SQLException e) {
			logger.warn("ERROR / guardarEncabezado / "+e);
		}
		
		logger.info("cadena ----->"+cadena);
		Utilidades.imprimirMapa(filtrosMap);
		return listadoGlosas;
		
	}
	
	public static HashMap consultarEstadoGlosa (Connection con)
	{
		return null;
	}
	
	/**
	 * Metodo para consultar el detalle de las facturas de una glosa
	 * @param con
	 * @param glosa
	 * @return
	 */
	public static HashMap consultarDetalleSolicitudesGlosa(Connection con, String codAuditoriaGlosa, int institucion)
	{
		HashMap detalleGlosaMap = new HashMap();
		
		String sql = "SELECT " +
						"dag.solicitud AS solicitud, " +
						"CASE WHEN dag.servicio IS NOT NULL THEN dag.servicio||' '||getnombreservicio(dag.servicio, "+ConstantesBD.codigoTarifarioCups+") ELSE getdescarticulo(dag.articulo) END AS descripcion, " +
						"dag.cantidad AS cantidad, " +
						"dag.valor AS valor, " +
						"dag.cantidad_glosa AS cantidadglosa, " +
						"dag.valor_glosa AS valorglosa, " +
						"glosas.getConceptosDetAudiGlosas2(dag.codigo, "+institucion+", '\n') AS conceptos " +
					"FROM " +
						"det_auditorias_glosas dag " +
					"WHERE " +
						"dag.auditoria_glosa="+codAuditoriaGlosa;
		
		logger.info("consultarDetalleSolicitudesGlosa / "+sql);
		
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(sql, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			detalleGlosaMap=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			detalleGlosaMap.put("consultasql", sql);
		} 
		catch (SQLException e) {	
			logger.info("ERROR / consultarDetalleSolicitudesGlosa / "+e);
		}
		
		
		Utilidades.imprimirMapa(detalleGlosaMap);
		return detalleGlosaMap;	
	}
	
	/**
	 * Metodo para consultar el detalle de las facturas de una glosa
	 * @param con
	 * @param glosa
	 * @return
	 */
	public static HashMap consultarDetalleFacturasGlosa(Connection con, String glosa, int institucion)
	{
		HashMap detalleGlosaMap = new HashMap();
		
		String sql = "SELECT " +
						"ag.codigo AS codauditoriaglosa, " +
						"ag.fecha_elaboracion_fact AS fechaelaboracion, " +
						"coalesce(ag.numero_cuenta_cobro,0) AS cuentacobro, " +
						"ag.fecha_radicacion_cxc AS fecharadicacion, " +
						"coalesce(ag.saldo_factura,0) AS saldofactura, " +
						"glosas.getConceptosAudiGlosas(ag.codigo, "+institucion+", '\n') AS conceptos, " +
						"coalesce(ag.valor_glosa_factura,0) AS valorglosa, " +
						"coalesce(ag.fue_auditada,' ') AS indicativofueauditada, " +
						"f.consecutivo_factura AS factura, " +
						"f.codigo " +
					"FROM " +
						"glosas.auditorias_glosas ag " +
					"INNER JOIN " +
						"facturacion.facturas f ON (f.codigo = ag.codigo_factura) " +
					"WHERE " +
						"ag.glosa="+glosa;
		
		logger.info("consultarDetalleFacturasGlosa / "+sql);
		
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(sql, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			detalleGlosaMap=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			detalleGlosaMap.put("consultasql", sql);
		} 
		catch (SQLException e) {	
			logger.info("ERROR / consultarDetalleFacturasGlosa / "+e);
		}
		
		return detalleGlosaMap;	
	}
	
	/**
	 * Metodo para consultar la información de una glosa
	 * @param con
	 * @param glosa
	 * @return
	 */
	public static HashMap consultarGlosa(Connection con, String glosa)
	{
		HashMap glosaMap = new HashMap();
		
		String sql = "SELECT " +
						"rg.codigo AS codigoglosa, " +
						"rg.glosa_sistema AS glosasistema, " +
						"rg.fecha_registro_glosa AS fecharegglosa, " +
						"getintegridaddominio(rg.estado) AS estado, " +
						"getnombreconvenio(rg.convenio) AS convenio, " +
						"rg.glosa_entidad AS glosaentidad, " +
						"rg.fecha_notificacion AS fechanotificacion, " +
						"rg.valor_glosa AS valorglosa, " +
						"rg.observaciones AS observaciones, " +
						"rg.fecha_confirmacion AS fechaconfirmacion, " +
						"rg.fecha_anulacion AS fechaanulacion, " +
						"rg.usuario_confirmacion AS usuarioconfirmacion, " +
						"rg.usuario_anulacion AS usuarioanulacion, " +
						"rg.motivo_anulacion AS motivoanulacion, " +
						"rg.fecha_aprobacion AS fechaaprobacion, " +
						"rg.usuario_aprobacion AS usuarioaprobacion, " +
						"rg.contrato AS contrato, " +
						"c.numero_contrato AS numcontrato, " +
						"rsg.fecha_respuesta AS fecharespuesta, " +
						"rsg.usuario_respuesta AS usuariorespuesta, " +
						"rsg.fecha_radicacion AS fecharadicacion, " +
						"rsg.numero_radicacion AS numeroradicacion, " +
						"coalesce (ag.fue_auditada, ' ') AS indicativofueauditada " +
					"FROM " +
						"registro_glosas rg " +
					"LEFT OUTER JOIN " +
						"auditorias_glosas ag ON (ag.glosa = rg.codigo) " +
					"LEFT OUTER JOIN " +
						"respuesta_glosa rsg ON (rsg.glosa = rg.codigo AND (rsg.estado='"+ConstantesIntegridadDominio.acronimoEstadoRespuestaGlosaRegistrada+"' OR rsg.estado='"+ConstantesIntegridadDominio.acronimoEstadoRespuestaGlosaAprobada+"' OR rsg.estado='"+ConstantesIntegridadDominio.acronimoEstadoRespuestaGlosaRadicada+"')) " +
					"INNER JOIN " +
						"contratos c ON (c.codigo = rg.contrato) "+
					"WHERE " +
						"rg.codigo="+glosa;
		
		logger.info("consultarGlosa / "+sql);
		
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(sql, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			glosaMap=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), false, true);
		} 
		catch (SQLException e) {	
			logger.info("ERROR / consultarGlosa / "+e);
		}
		
		return glosaMap;
	}


}