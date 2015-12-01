package com.princetonsa.dao.sqlbase.glosas;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.Utilidades;
import com.princetonsa.dao.sqlbase.administracion.SqlBaseTiposRetencionDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.glosas.DtoDetalleFacturaGlosa;
import com.princetonsa.dto.glosas.DtoGlosa;

/**
 * @author Gio
 */
public class SqlBaseUtilidadesGlosasDao {
	
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseTiposRetencionDao.class);
	
	/**
	 * Cadena SQL para Obtener las glosas de una factura
	 */
	private static final String strObtenerGlosasFactura = "SELECT DISTINCT " +
																"ag.codigo, " +
																"rg.glosa_sistema, " +
																"rg.fecha_aprobacion, " +
																"rg.valor_glosa, " +
																"re.respuesta_consecutivo, " +
																"re.fecha_respuesta, " +
																"crg.descripcion as concepto_respuesta, " +
																"crg.concepto_ajuste as numero_ajuste," +
																"re.valor_respuesta," +
																"re.fecha_radicacion " +
															"FROM " +
																"auditorias_glosas ag " +
															"INNER JOIN " +
																"registro_glosas rg ON (rg.codigo=ag.glosa) " +
															"LEFT OUTER JOIN " +
																"respuesta_glosa re ON (re.glosa=rg.codigo) " +
															"LEFT OUTER JOIN " +
																"concepto_resp_glosas crg ON (crg.codigo=re.concepto_respuesta AND crg.institucion=re.institucion) " +
															" WHERE " +
																"ag.codigo_factura=? " +
															"ORDER BY " +
																"rg.glosa_sistema ";
	
	/**
	 * Cadena SQL para Obtener el detalle de una glosa
	 */
	private static final String strObtenerDetGlosaFactura = "SELECT DISTINCT " +
																"coalesce(dag.servicio, dag.articulo) as cod_articulo_servicio, " +
																"CASE WHEN dag.articulo is null THEN getnombreservicio(dag.servicio, "+ConstantesBD.codigoTarifarioCups+") ELSE getdescarticulosincodigo(dag.articulo) END as desc_articulo_servicio, " +
																"dag.solicitud, " +
																"dag.cantidad_glosa," +
																"dag.cantidad," +
																"dag.valor_glosa, " +
																"dag.valor, " +
																"dfrg.concepto_respuesta, " +
																"dfrg.valor_respuesta," +
																"dfrg.ajuste " +
															"FROM " +
																"det_auditorias_glosas dag " +
															"LEFT OUTER JOIN " +
																"det_fact_resp_glosa dfrg ON (dfrg.det_auditoria_glosa=dag.codigo) " +
															"WHERE " +
																"dag.codigo=? " +
															"ORDER BY " +
																"dag.solicitud ";
	
	/**
	 * Obtener las glosas de una factura
	 * @param con
	 * @param codigoFactura
	 * @return0
	 */
	public static ArrayList<DtoGlosa> obtenerGlosasFactura(Connection con,String codigoFactura) {
		ArrayList<DtoGlosa> glosas = new ArrayList<DtoGlosa>();
		
		logger.info("Codigo factura -> "+codigoFactura);
		logger.info("Consulta -> "+strObtenerGlosasFactura);
		
		try {
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(strObtenerGlosasFactura,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(codigoFactura));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next()) {
				DtoGlosa glosa = new DtoGlosa();
				glosa.setCodigo(rs.getString("codigo"));
				glosa.setGlosaSistema(rs.getString("glosa_sistema"));
				glosa.setFechaAprobacion(rs.getString("fecha_aprobacion"));
				glosa.setValorGlosa(rs.getDouble("valor_glosa"));
				glosa.getDtoRespuestaGlosa().setRespuestaConsecutivo(rs.getString("respuesta_consecutivo"));
				glosa.getDtoRespuestaGlosa().setFechaRespuesta(rs.getString("fecha_respuesta"));
				glosa.getDtoRespuestaGlosa().setValorRespuesta(rs.getDouble("valor_respuesta"));
				glosa.getDtoRespuestaGlosa().setFechaRadicacion(rs.getString("fecha_radicacion"));
				glosa.getDtoRespuestaGlosa().getConcepto().setDescripcion(rs.getString("concepto_respuesta"));
				glosa.getDtoRespuestaGlosa().getConcepto().setConceptoajuste(rs.getString("numero_ajuste"));
				glosas.add(glosa);
			}
			
		} catch (SQLException e){
			logger.error("ERROR", e);
		}
		return glosas;
	}

	/**
	 * 
	 * @param con
	 * @param codigoGlosa
	 * @return
	 */
	public static ArrayList<DtoDetalleFacturaGlosa> obtenerDetalleGlosaFactura(Connection con, String codigoGlosa) {
		ArrayList<DtoDetalleFacturaGlosa> detalleGlosaFactura = new ArrayList<DtoDetalleFacturaGlosa>();
		
		logger.info("Codigo factura -> "+codigoGlosa);
		logger.info("Consulta -> "+strObtenerDetGlosaFactura);
		
		try {
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(strObtenerDetGlosaFactura,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(codigoGlosa));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next()) {
				DtoDetalleFacturaGlosa detalle = new DtoDetalleFacturaGlosa();
				detalle.setDescripcionServicioArticulo(rs.getString("desc_articulo_servicio"));
				detalle.setCodigoServicioArticulo(rs.getString("cod_articulo_servicio"));
				detalle.setNumeroSolicitud(rs.getString("solicitud"));
				detalle.setCantidad(rs.getInt("cantidad"));
				detalle.setCantidadGlosa(rs.getInt("cantidad_glosa"));
				detalle.setValor(rs.getDouble("valor"));
				detalle.setValorGlosa(rs.getDouble("valor_glosa"));
				detalle.getConcepto().setDescripcion(rs.getString("concepto_respuesta"));
				detalleGlosaFactura.add(detalle);
			}
			
		} catch (SQLException e){
			logger.error("ERROR", e);
		}
		return detalleGlosaFactura;
	}
	
	/**
	 * 
	 * @param con
	 * @param informacionRespuesta
	 * @param codigoInstitucion
	 * @return
	 */
	public static double obtenerTotalSoportadoRespuesta(Connection con,
			int informacionRespuesta, int codigoInstitucion) {
		double totalSoportado = 0;
		String consulta = 	"SELECT " +
								"SUM(valor) as valor " +
							"FROM " +
								"(" +
									"SELECT " +
										"SUM(dfrg.valor_respuesta) as valor " +
									"FROM " +
										"fact_respuesta_glosa frg " +
									"INNER JOIN " +
										"det_fact_resp_glosa dfrg ON (dfrg.fact_respuesta_glosa=frg.codigo) " +
									"WHERE " +
										"frg.respuesta_glosa=? " +
										"AND dfrg.institucion=? " +
										"AND dfrg.ajuste IS NULL " +
									"UNION " +
									"SELECT " +
										"SUM(dferg.valor_respuesta) as valor " +
									"FROM " +
										"fact_respuesta_glosa frg " +
									"INNER JOIN " +
										"det_fact_ext_resp_glosa dferg ON (dferg.fact_respuesta_glosa=frg.codigo) " +
									"WHERE " +
										"frg.respuesta_glosa=? " +
										"AND dferg.institucion=? " +
										"AND dferg.ajuste IS NULL " +
								") AS t";
		try {
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,informacionRespuesta);
			pst.setInt(2,codigoInstitucion);
			pst.setInt(3,informacionRespuesta);
			pst.setInt(4,codigoInstitucion);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next()) {
				totalSoportado = rs.getDouble("valor");
			}
			
		} catch (SQLException e){
			logger.error("ERROR", e);
		}
		return totalSoportado;
	}

	/**
	 * 
	 * @param con
	 * @param informacionRespuesta
	 * @param codigoInstitucion
	 * @return
	 */
	public static double obtenerTotalAceptadoRespuesta(Connection con,
			int informacionRespuesta, int codigoInstitucion) {
		double totalAceptado = 0;
		String consulta = 	"SELECT " +
								"SUM(valor) as valor " +
							"FROM " +
								"(" +
									"SELECT " +
										"SUM(dfrg.valor_respuesta) as valor " +
									"FROM " +
										"fact_respuesta_glosa frg " +
									"INNER JOIN " +
										"det_fact_resp_glosa dfrg ON (dfrg.fact_respuesta_glosa=frg.codigo) " +
									"WHERE " +
										"frg.respuesta_glosa=? " +
										"AND dfrg.institucion=? " +
										"AND dfrg.ajuste IS NOT NULL " +
									"UNION " +
									"SELECT " +
										"SUM(dferg.valor_respuesta) as valor " +
									"FROM " +
										"fact_respuesta_glosa frg " +
									"INNER JOIN " +
										"det_fact_ext_resp_glosa dferg ON (dferg.fact_respuesta_glosa=frg.codigo) " +
									"WHERE " +
										"frg.respuesta_glosa=? " +
										"AND dferg.institucion=? " +
										"AND dferg.ajuste IS NOT NULL " +
								") AS t";
		try {
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,informacionRespuesta);
			pst.setInt(2,codigoInstitucion);
			pst.setInt(3,informacionRespuesta);
			pst.setInt(4,codigoInstitucion);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next()) {
				totalAceptado = rs.getDouble("valor");
			}
			
		} catch (SQLException e){
			logger.error("ERROR", e);
		}
		return totalAceptado;
	}
}
