package com.princetonsa.dao.sqlbase.glosas;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import com.princetonsa.dto.glosas.DtoGlosa;
import com.princetonsa.dto.glosas.DtoRespuestaFacturaGlosa;
import com.princetonsa.dto.glosas.DtoRespuestaSolicitudGlosa;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

public class SqlBaseConsultarImprimirRespuestasDao
{	
	/**
	 * Para el manejo de logs
	 */
	private static Logger logger = Logger.getLogger(SqlBaseConsultarImprimirGlosasDao.class);
	
	private static String strConsultarRespuestasGlosas = "SELECT DISTINCT " +
													"coalesce(rg.fecha_aprob_anul||'', '') AS fechaaprobanul, " +
													"coalesce(rg.usuario_aprob_anul,'') AS usuarioaprobanul, " +
													"coalesce(rg.motivo_aprob_anul,'') AS motivoaprovanul, " +
													"coalesce(rg.fecha_radicacion||'','') AS fecharadicacion, " +
													"coalesce(rg.numero_radicacion,'') AS numeroradicacion, " +
													"coalesce(rg.codigo,0) AS codrespuesta, " +
													"coalesce(rg.respuesta_consecutivo, '') AS resconsecutivo, " +
													"to_char(rg.fecha_respuesta,'dd/mm/yyyy') AS fecharespuesta, " +
													"getintegridaddominio(rg.estado) AS estado, " +
													"coalesce(rg.valor_respuesta,0) AS valrespuesta, " +
													"coalesce(g.codigo,0) AS codigoglosa, " +
													"coalesce(g.glosa_sistema,'') AS glosasistema, " +
													"coalesce(g.convenio,0) AS convenio , " +
													"coalesce(getnombreconvenio(g.convenio),'') AS nomconvenio, " +
													"coalesce(g.contrato,0) AS  contrato, " +
													"coalesce(getnumerocontrato(g.contrato),'') AS numcontrato, " +
													"coalesce(g.glosa_entidad,'') AS glosaentidad, " +
													"to_char(g.fecha_notificacion, 'dd/mm/yyyy') AS fechanoti, " +
													"g.valor_glosa AS valorglosa, " +
													"to_char(g.fecha_registro_glosa, 'dd/mm/yyyy') AS fecharegistroglosa, " +
													"g.observaciones AS observacionesglosa, " +
													"getintegridaddominio(g.estado) AS estadoglosa, " +
													"coalesce(ag.fue_auditada,' ') AS indicativofueauditada " +
												"FROM " +
													"respuesta_glosa rg " +
												"INNER JOIN " +
													"registro_glosas g ON (g.codigo=rg.glosa) " +
												"LEFT OUTER JOIN " +
													"auditorias_glosas ag ON (ag.glosa=g.codigo) " +
												" left outer join facturas fac on (fac.codigo=ag.codigo_factura) " +
												"WHERE ";
													
													
	
	public static ArrayList<DtoGlosa> listarGlosas(Connection con,HashMap<String, Object> filtrosMap) {
		
		ArrayList<DtoGlosa> array = new ArrayList<DtoGlosa>();
		
		String cadena = strConsultarRespuestasGlosas;
		
		cadena += "rg.institucion="+filtrosMap.get("institucion")+" ";
		
		if (!filtrosMap.get("glosa").toString().equals(""))
			cadena+="AND g.glosa_sistema='"+filtrosMap.get("glosa")+"' ";
				
		if (!filtrosMap.get("glosaE").toString().equals(""))
			cadena+="AND g.glosa_entidad='"+filtrosMap.get("glosaE")+"' ";
		
		if (!filtrosMap.get("convenio").toString().equals(""))
			cadena+="AND g.convenio="+filtrosMap.get("convenio")+" ";
		
		if (!filtrosMap.get("contrato").toString().equals(""))
			cadena+="AND g.contrato="+filtrosMap.get("contrato")+" ";
		
		if (!filtrosMap.get("fechaNoti").toString().equals(""))
			cadena+="AND to_char(g.fecha_notificacion,'yyyy-mm-dd') ='"+UtilidadFecha.conversionFormatoFechaABD(filtrosMap.get("fechaNoti")+"")+"'";
		
		if (!filtrosMap.get("estadoRes").toString().equals(""))
			cadena+="AND rg.estado='"+filtrosMap.get("estadoRes").toString().split(ConstantesBD.separadorSplit)[0]+"' ";

		if (!filtrosMap.get("fechaResIni").toString().equals("")&&!filtrosMap.get("fechaResFin").toString().equals(""))
			cadena+="AND to_char(rg.fecha_respuesta,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(filtrosMap.get("fechaResIni")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(filtrosMap.get("fechaResFin")+"")+"' ";
			
		if (!filtrosMap.get("resIni").toString().equals("")&&!filtrosMap.get("fechaResFin").toString().equals(""))
			cadena+="AND g.respuesta_consecutivo BETWEEN '"+filtrosMap.get("resIni")+"' AND '"+filtrosMap.get("resFin")+"' ";
		
		if (!filtrosMap.get("indicativoGlosa").toString().equals(""))
			cadena+="AND ag.fue_auditada='"+filtrosMap.get("indicativoGlosa")+"' ";
		if(!UtilidadTexto.isEmpty(filtrosMap.get("consecutivoFact")+""))
			cadena+="AND fac.consecutivo_factura='"+filtrosMap.get("consecutivoFact")+"'";
		cadena += " ORDER BY COALESCE(rg.respuesta_consecutivo, '') ASC";
	
		
		try {
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(cadena));
			while(rs.next())
			{
				DtoGlosa dto = new DtoGlosa();
				dto.getDtoRespuestaGlosa().setCodigoPk(rs.getInt("codrespuesta"));
				dto.getDtoRespuestaGlosa().setRespuestaConsecutivo(rs.getString("resconsecutivo"));
				dto.getDtoRespuestaGlosa().setFechaRespuesta(rs.getString("fecharespuesta"));
				dto.getDtoRespuestaGlosa().setEstadoRespuesta(rs.getString("estado"));
				dto.getDtoRespuestaGlosa().setValorRespuesta(rs.getDouble("valrespuesta"));
				dto.getDtoRespuestaGlosa().setFechaAprobacionAnulacion(rs.getString("fechaaprobanul"));
				dto.getDtoRespuestaGlosa().setUsuarioAprobacionAnulacion(rs.getString("usuarioaprobanul"));
				dto.getDtoRespuestaGlosa().setMotivoAprobacionAnulacion(rs.getString("motivoaprovanul"));
				dto.getDtoRespuestaGlosa().setFechaRadicacion(rs.getString("fecharadicacion"));
				dto.getDtoRespuestaGlosa().setNumeroRadicacion(rs.getString("numeroradicacion"));
				dto.setCodigo(rs.getString("codigoglosa"));
				dto.setGlosaSistema(rs.getString("glosasistema"));
				dto.setCodigoConvenio(rs.getInt("convenio"));
				dto.setNombreConvenio(rs.getString("nomconvenio"));
				dto.setCodigoContrato(rs.getInt("contrato"));
				dto.setConsecutivoContrato(rs.getString("numcontrato"));
				dto.setGlosaEntidad(rs.getString("glosaentidad"));
				dto.setFechaNotificacion(rs.getString("fechanoti"));
				dto.setValorGlosa(rs.getDouble("valorglosa"));
				dto.setFechaRegistroGlosa(rs.getString("fecharegistroglosa"));
				dto.setObservaciones(rs.getString("observacionesglosa"));
				dto.setEstado(rs.getString("estadoglosa"));
				dto.setIndicativoFueAuditada(rs.getString("indicativofueauditada"));
				array.add(dto);
			}	
		}
		catch (SQLException e) {
			logger.warn("ERROR / listarGlosas / "+e);
		}
		
		filtrosMap.put("cadenasql",cadena);
		logger.info("cadena ----->"+cadena);
		Utilidades.imprimirMapa(filtrosMap);
		return array;		
	}

	/**
	 * Método para consultar las respuestas de las facturas de una glosa
	 * @param con
	 * @param glosa
	 * @return
	 */
	public static DtoGlosa consultarRespuestaFacturasGlosa(Connection con,DtoGlosa glosa) {
		ArrayList<DtoRespuestaFacturaGlosa> array = new ArrayList<DtoRespuestaFacturaGlosa>();
		String consultaSql = 	"SELECT " +
									"frg.codigo AS codigofactrespuestaglosa, " +
									"frg.valor_fact_resp AS valorrespuestaglosa, " +
									"frg.factura AS codigofactura, " +
									"f.consecutivo_factura AS consecutivofactura, " +
									"to_char(f.fecha, 'dd/mm/yyyy') AS fechafactura, " +
									"coalesce(f.numero_cuenta_cobro,0) AS cuentacobro, " +
									"coalesce(cc.fecha_radicacion||'', '') AS fecharadicacion, " +
									"coalesce(f.valor_total,0) + coalesce(f.ajustes_debito,0) - coalesce(f.ajustes_credito,0) + coalesce(cc.ajuste_debito,0) - coalesce(cc.ajuste_credito,0) - coalesce(cc.pagos,0) AS saldofactura, " +
									"'' AS conceptoglosa, " +
									"coalesce(ag.valor_glosa_factura,0) AS valorglosa " +
								"FROM " +
									"fact_respuesta_glosa frg " +
								"INNER JOIN " +
									"facturas f ON (f.codigo=frg.factura) " +
								"INNER JOIN " +
									"auditorias_glosas ag ON (ag.codigo=frg.auditoria_glosa) " +
								"LEFT OUTER JOIN " +
									"cuentas_cobro cc ON (cc.numero_cuenta_cobro=f.numero_cuenta_cobro) " +
								"WHERE " +
									"frg.respuesta_glosa="+glosa.getDtoRespuestaGlosa().getCodigoPk();
		
		logger.info("SQL / consultarRespuestaFacturasGlosa / "+consultaSql);
		
		try {
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consultaSql));
			while(rs.next())
			{
				DtoRespuestaFacturaGlosa dto = new DtoRespuestaFacturaGlosa();
				dto.setCodigoFactRespuestaGlosa(rs.getString("codigofactrespuestaglosa"));
				dto.setCodigoFactura(rs.getInt("codigofactura"));
				dto.setConceptoGlosa(rs.getString("conceptoglosa"));
				dto.setConsecutivoFactura(rs.getString("consecutivofactura"));
				dto.setCuentaCobro(rs.getString("cuentacobro"));
				dto.setFechaFactura(rs.getString("fechafactura"));
				dto.setFechaRadicacion(rs.getString("fecharadicacion"));
				dto.setSaldoFactura(rs.getString("saldofactura"));
				dto.setValorGlosa(rs.getString("valorglosa"));
				dto.setValorRespuesta(rs.getString("valorrespuestaglosa"));
				array.add(dto);
			}
			glosa.setRespuestasFacturas(array);
			glosa.setSqlImpresionDetalleRespuesta(consultaSql);
		}
		catch (SQLException e) {
			logger.warn("ERROR / consultarRespuestaFacturasGlosa / "+e);
		}
								
		return glosa;
	}
	
	/**
	 * 
	 * @param con
	 * @param dtoRespuestaFactura
	 * @return
	 */
	public static DtoRespuestaFacturaGlosa consultarRespuestaSolicitudesGlosa(Connection con, DtoRespuestaFacturaGlosa dtoRespuestaFactura, int institucion){
		ArrayList<DtoRespuestaSolicitudGlosa> array = new ArrayList<DtoRespuestaSolicitudGlosa>();
		String consultaSql = 	"SELECT " +
									"dag.solicitud AS solicitud, " +
									"s.consecutivo_ordenes_medicas AS consecutivoSol, "+
									"CASE WHEN dag.servicio IS NOT NULL THEN dag.servicio||' '||getnombreservicio(dag.servicio, 0) ELSE getdescarticulo(dag.articulo) END AS descripcion, " +
									"getnomcentrocosto(s.centro_costo_solicitado) AS centrocosto, " +
									"getDescripcionConceptoGlosa(dfrg.concepto_glosa, "+institucion+") AS conceptoglosa, " +
									"dag.cantidad_glosa AS cantidadglosa, " +
									"dag.valor_glosa AS valorglosa, " +
									"crg.descripcion AS conceptorespuesta, " +
									"coalesce(dfrg.motivo, '') AS motivo, " +
									"dfrg.cantidad_respuesta AS cantidadrespuesta, " +
									"dfrg.valor_respuesta AS valorrespuesta, " +
									"ta.descripcion AS tipoajuste, " +
									"CASE WHEN ae.valor_ajuste!=2 THEN ae.valor_ajuste||'' ELSE '' END AS valorajuste " +
								"FROM " +
									"det_fact_resp_glosa dfrg " +
								"LEFT OUTER JOIN " +
									"det_auditorias_glosas dag ON (dag.codigo=dfrg.det_auditoria_glosa) " +
								"LEFT OUTER JOIN " +
									"solicitudes s ON (s.numero_solicitud=dag.solicitud) " +
								"LEFT OUTER JOIN " +
									"ajustes_empresa ae ON (ae.codigo=dfrg.ajuste) " +
								"LEFT OUTER JOIN " +
									"cartera.tipos_ajuste ta ON(ta.codigo=ae.tipo_ajuste) " +
								"LEFT OUTER JOIN " +
									"glosas.concepto_resp_glosas crg ON(dfrg.concepto_respuesta=crg.codigo AND dfrg.institucion=crg.institucion) " +
								"WHERE " +
									"dfrg.fact_respuesta_glosa="+dtoRespuestaFactura.getCodigoFactRespuestaGlosa();
		
		logger.info("SQL / consultarRespuestaSolicitudesGlosa / "+consultaSql);
		
		try {
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consultaSql));
			while(rs.next())
			{
				DtoRespuestaSolicitudGlosa dto = new DtoRespuestaSolicitudGlosa();
				dto.setSolicitud(rs.getString("consecutivoSol"));
				dto.setDescripcionServicioArticulo(rs.getString("descripcion"));
				dto.setCentroCosto(rs.getString("centrocosto"));
				dto.setConceptoGlosa(rs.getString("conceptoglosa"));
				dto.setCantidadGlosa(rs.getString("cantidadglosa"));
				dto.setValorGlosa(rs.getString("valorglosa"));
				dto.setConceptoRespuesta(rs.getString("conceptorespuesta"));
				dto.setMotivo(rs.getString("motivo"));
				dto.setCantidadRespuesta(rs.getString("cantidadrespuesta"));
				dto.setValorRespuesta(rs.getString("valorrespuesta"));
				String ajuste="";
				if(!rs.getString("valorajuste").equals(""))
				{
					if(rs.getString("tipoajuste").equals(ConstantesBD.codigoAjusteCreditoFactura) || rs.getString("tipoajuste").equals(ConstantesBD.codigoAjusteCreditoCuentaCobro))
						ajuste += "C- ";
					else
						ajuste+="D- ";
					logger.info("\n\nvalor ajuste: "+rs.getString("valorajuste"));
					ajuste += rs.getString("valorajuste");
				}
				dto.setAjuste(ajuste);
				array.add(dto);
			}
			dtoRespuestaFactura.setRespuestasSolicitudes(array);
			dtoRespuestaFactura.setSqlImpresionRespuestaGlosaFactura(consultaSql);
		}
		catch (SQLException e) {
			logger.warn("ERROR / consultarRespuestaSolicitudesGlosa / "+e);
		}
		
		return dtoRespuestaFactura;
	}
}