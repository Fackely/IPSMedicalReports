package com.princetonsa.dao.sqlbase.glosas;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.glosas.DtoConceptoGlosa;
import com.princetonsa.dto.glosas.DtoConceptoRespuesta;
import com.princetonsa.dto.glosas.DtoDetalleAsociosGlosa;
import com.princetonsa.dto.glosas.DtoDetalleFacturaGlosa;
import com.princetonsa.dto.glosas.DtoFacturaGlosa;
import com.princetonsa.dto.glosas.DtoRespuestaFacturaGlosa;
import com.princetonsa.dto.glosas.DtoRespuestaSolicitudGlosa;
import com.princetonsa.dto.odontologia.DtoMotivosAtencion;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;


public class SqlBaseRegistrarConciliacionDao
{
	/**
	 * Manejo de Logs
	 */
	private static Logger logger = Logger.getLogger(SqlBaseRegistrarConciliacionDao.class);
	
	
	
	private static String consultarTiposConcepto="SELECT crg.codigo, crg.descripcion, crg.concepto_ajuste AS conceptoajus, cac.naturaleza, crg.tipo_concepto AS tipoconcepto " +
												"FROM glosas.concepto_resp_glosas crg " +
												"LEFT OUTER JOIN cartera.concepto_ajustes_cartera cac ON(crg.concepto_ajuste=cac.codigo AND crg.institucion= cac.institucion) " +
												"WHERE crg.tipo_concepto= '"+ConstantesIntegridadDominio.acronimoTipoGlosaRespuesta+"' ";
	
	/**
	 * Cadena para insertar una nueva conciliacion
	 */
	private static String insertarEncabezadoConciliacion="INSERT INTO respuesta_glosa " +
															"(codigo," + // 1
															"respuesta_consecutivo," + // 2
															"fecha_respuesta," + // 3
															"hora_respuesta," + //4
															"usuario_respuesta," + // 5
															"conciliacion," + 
															"observaciones," + // 6
															"estado," + //7
															"institucion," + // 8
															"fecha_modifica," +
															"hora_modifica," +
															"usuario_modifica," + // 9
															"convenio," + //10
															"nro_acta," + //11
															"representante_convenio," + //12 
															"cargo_rep_convenio," + //13
															"representante_inst," + //14
															"cargo_rep_inst," + //15
															"porcen_soportado," + //16
															"porcen_aceptado," + //17
															"concepto_respuesta) " + //18
														"VALUES (?," + // 1
															"?," + // 2
															"?," + // 3
															"?," + //4
															"?," + // 5
															"'"+ConstantesBD.acronimoSi+"'," + 
															"?," + // 6
															"?," + // 7
															"?," + // 8															
															"CURRENT_DATE," +
															""+ValoresPorDefecto.getSentenciaHoraActualBD()+"," +
															"?," + // 9
															"?," + // 10
															"?," + // 11
															"?," + // 12
															"?," + // 13
															"?," + // 14
															"?," + // 15
															"?," + // 16
															"?," + // 17
															"?) "; // 18
	
	/**
	 * Cadena para actualizar una nueva conciliacion
	 */
	private static String actualizarEncabezadoConciliacion="UPDATE respuesta_glosa SET " +
															"fecha_respuesta=?," + // 2
															"hora_respuesta=?," + //3
															"usuario_respuesta=?," +//4 
															"conciliacion= '"+ConstantesBD.acronimoSi+"'," + 
															"observaciones=?," + // 5
															"estado=?," + //6
															"institucion=?," + // 7
															"fecha_modifica= CURRENT_DATE," +
															"hora_modifica= "+ValoresPorDefecto.getSentenciaHoraActualBD()+"," +
															"usuario_modifica=?," + // 8
															"convenio=?," + //9
															"nro_acta=?," + //10
															"representante_convenio=?," + //11 
															"cargo_rep_convenio=?," + //12
															"representante_inst=?," + //13
															"cargo_rep_inst=?," + //14
															"porcen_soportado=?," + //15
															"porcen_aceptado=?," + //16
															"concepto_respuesta=? " + // 17
															"WHERE codigo=?"; //18

	private static String consultarFacturasPorGlosa="SELECT f.codigo AS codfactura, " +
													"f.consecutivo_factura AS consecutivofac, " +
													"ag.fecha_elaboracion_fact AS fechaelabfactura, " +
													"ag.fecha_radicacion_cxc AS fecharadcxc, " +
													"ag.codigo AS codauditoria, " +
													"rg.codigo AS codglosa, " +
													"rg.glosa_sistema AS glosasis, " +
													"rg.glosa_entidad AS glosaentidad, " +
													"f.tipo_factura_sistema AS factinterna, " +
													" coalesce(f.valor_total,0)" +
													"+" +
													"coalesce(f.ajustes_debito,0)" +
														"-" +
														"coalesce(f.ajustes_credito,0)" +
															"+" +
															"coalesce(cc.ajuste_debito,0)" +
																"-" +
																"coalesce(cc.ajuste_credito,0)" +
																	"-" +
																	"coalesce(cc.pagos,0) AS saldofactura, " +
													"ag.valor_glosa_factura AS valorglosafac, " +
													"administracion.getnombrepersona(f.cod_paciente) AS paciente, " +
													"re.porcen_aceptado AS porcenaceptado, " +
													"re.porcen_soportado AS porcensoportado, " +
													"'"+ConstantesBD.acronimoNo+"' AS eliminar, "  +
													"'"+ConstantesBD.acronimoNo+"' AS seleccionado, " +
													"'"+ConstantesBD.acronimoSi+"' AS mostrar, " +
													"re.estado AS estadorespuesta "  +
													"FROM glosas.registro_glosas rg " +													
													"INNER JOIN glosas.auditorias_glosas ag ON(rg.codigo= ag.glosa) " +
													"INNER JOIN facturacion.facturas f ON(ag.codigo_factura=f.codigo) " +
													"INNER JOIN cuentas_cobro cc ON (f.numero_cuenta_cobro=cc.numero_cuenta_cobro AND f.institucion=cc.institucion) " +
													"LEFT OUTER JOIN glosas.respuesta_glosa re ON(re.glosa=rg.codigo) " +
													"WHERE ag.reiterada='S' AND " +
													"rg.estado= '"+ConstantesIntegridadDominio.acronimoEstadoGlosaAprobada+"' ";
	
	private static String  insertarFacturasRespGlosa="INSERT INTO glosas.fact_respuesta_glosa "+
															"(codigo," + // 1
															"respuesta_glosa," + // 2
															"factura," + // 3
															"auditoria_glosa," + // 4
															"factura_sistema," + //5
															"valor_aceptado," + //6
															"valor_soportado," + //7
															"institucion," + //8
															"concepto_respuesta) " + //9
														"VALUES (?," + // 1
															"?," + // 2
															"?," + // 3
															"?," + // 4
															"?," + // 5
															"?," + // 6
															"?," + // 7
															"?," + // 8
															"?) "; // 9
	
	private static String consultaDetalleFacGlosa="SELECT cc.codigo AS codigo, " +
														"cc.descripcion AS descripcion, " +
														"ag.codigo_factura AS fac, " +
														"cc.tipo_concepto AS tipoconcepto, " +
														"dag.codigo AS coddetaudi, " +
														"dag.auditoria_glosa AS codaudi, " +
														"dag.det_factura_solicitud AS detfactsolicitud, " +
														"dag.valor_glosa AS valorglosa, " +
														"dag.valor AS valorsolicitud, " +
														"dag.cantidad_glosa AS cantidadglosa, " +
														"f.numero_cuenta_cobro AS cuentacobro, " +
														"dfs.pool AS pool, " +
														"dag.solicitud AS numsolicitud, " +
														"CASE " +
														"WHEN dag.servicio IS NULL " +
														"THEN getdescarticulo(dag.articulo) " +
														" ELSE '(' " +
														" || getcodigopropservicio2(dag.servicio,?) " +
														"|| ') ' " +
														"|| getnombreservicio(dag.servicio, "+ConstantesBD.codigoTarifarioCups+") " +
														"END AS descripcionarticuloservicio, " +
														"CASE " +
														"WHEN dfs.tipo_cargo =1 " +
														"THEN administracion.getnomcentrocosto(s.centro_costo_solicitado) " +
														" ELSE '(' " +
														" || administracion.getnomcentrocosto(s.centro_costo_solicitante) " +
														"|| ') ' "+
														"END AS centrocosto, " +
														"dfs.codigo_medico AS codigomedico, " +
														"CASE WHEN s.tipo="+ConstantesBD.codigoTipoSolicitudCirugia+" THEN 'true' ELSE 'false' END AS escirugia " +
													"FROM auditorias_glosas ag " +
														"INNER JOIN facturacion.facturas f ON(ag.codigo_factura= f.codigo) " +
														"INNER JOIN det_auditorias_glosas dag ON (dag.auditoria_glosa=ag.codigo) " +
														"INNER JOIN solicitudes s ON (dag.solicitud=s.numero_solicitud) " +
														"INNER JOIN facturacion.det_factura_solicitud dfs ON(dag.det_factura_solicitud=dfs.codigo) " +
														"INNER JOIN conceptos_det_audi_glosas cdag ON (cdag.det_audi_fact=dag.codigo) " +
														"INNER JOIN concepto_glosas cc ON (cdag.concepto_glosa=cc.codigo AND cdag.institucion=cc.institucion) " +														
													"WHERE ag.codigo=? " ;

	private static String insertarDetalleFacturaConciliacion="INSERT INTO glosas.det_fact_resp_glosa " +
																"(codigo, " + //1
																"fact_respuesta_glosa, " + //2
																"det_auditoria_glosa, " + //3
																"det_factura_solicitud, " + //4
																"concepto_glosa, " + //5
																"concepto_respuesta, " + //6
																"institucion, " + //7
																"ajuste, " + //8
																"cantidad_respuesta, " + //9
																"valor_aceptado, " + //10
																"valor_soportado," +
																"motivo) " + //11
																"VALUES(?," + //1
																"?," + //2
																"?," + //3
																"?," + //4
																"?," + //5
																"?," + //6
																"?," + //7
																"?," + //8
																"?," + //9
																"?," + //10
																"?," +
																"?)"; //11
	
	private static String consultarAsociosDetFacturaResp="SELECT " +
														  "aag.codigo AS codigoasocio, " +
														  "aag.asocio_det_factura AS asociodetfactura, " +
														  "aag.det_auditoria_glosa AS detauditoriaglosa, " +
														  "aag.tipo_asocio AS tipoasocio, " +
														  "aag.codigo_medico AS codigomedico, " +
														  "aag.cantidad_glosa AS cantidadglosa, " +
														  "aag.valor_glosa AS valorglosa, " +
														  "aag.servicio_asocio AS servicioasocio, " +
														  "aag.cantidad AS cantidad, " +
														  "aag.valor AS valor, " +
														  "aag.servicio_asocio AS servicioasocio, " +
														  "dfs.codigo AS detfactsolicitud, " +
														  "adf.consecutivo AS consecutivoasodetfact, " +
														  "adf.codigo_medico AS codigomedicoaso, " +
														  "adf.pool AS poolaso, " +
														  "adf.porcentaje_pool AS porcentajepoolaso, " +
														  "aag.valor AS valorasocio, " +
														  "arg.valor_respuesta AS valorresp, " +
														  "cg.descripcion AS descconceptoglosa, " +
														  "cg.codigo AS codconcglosa " +
														  "FROM glosas.asocios_auditorias_glosas aag " +
														  "INNER JOIN glosas.conceptos_aso_audi_glosas caag ON(caag.asocio_auditoria_glosa=aag.codigo) " +
														  "INNER JOIN glosas.concepto_glosas cg ON(caag.concepto_glosa=cg.codigo AND caag.institucion=cg.institucion) " +
														  "INNER JOIN asocios_det_factura adf ON (aag.asocio_det_factura=adf.consecutivo) " +
														  "INNER JOIN det_factura_solicitud dfs on (dfs.codigo = adf.codigo) " +
														  "LEFT OUTER JOIN asocios_resp_glosa arg ON (arg.asocio_auditoria_glosa=aag.codigo) " +
														  "WHERE  aag.det_auditoria_glosa=?" ;
	
	private static String consultaFacturasConciliacion=" SELECT frg.codigo AS codigofcatresp, " +
																"f.codigo AS codfactura, " +
																"f.consecutivo_factura AS factura, " +
																"f.fecha AS fechafactura, " +
																"ag.fecha_radicacion_cxc AS fecharadcc, " +
																"administracion.getcentroatencioncc(f.centro_aten) AS centroatencion, " +
																"f.valor_total AS valorfactura, " +
																"administracion.getnombrepersona(f.cod_paciente) AS paciente, " +
																"manejopaciente.getnombreviaingreso(f.via_ingreso) AS viaingreso, " +
																"frg.valor_aceptado AS valoraceptadofact, " +
																"frg.valor_soportado AS valorsoportadofact, " +
																"frg.concepto_respuesta AS concepto, " +
																"ag.valor_glosa_factura AS valorglosafact, " +
																"ag.codigo AS codauditoria, " +
																"rg.codigo AS codglosa, " +
																"rg.glosa_sistema AS glosasistema, " +
																"rg.glosa_entidad AS glosaentidad, " +
																" coalesce(f.valor_total,0)" +
																"+" +
																"coalesce(f.ajustes_debito,0)" +
																	"-" +
																	"coalesce(f.ajustes_credito,0)" +
																		"+" +
																		"coalesce(cc.ajuste_debito,0)" +
																			"-" +
																			"coalesce(cc.ajuste_credito,0)" +
																				"-" +
																				"coalesce(cc.pagos,0) AS saldofactura, " +
																"f.tipo_factura_sistema AS factinterna, " +
																"'"+ConstantesBD.acronimoNo+"' AS eliminar, " +
																"re.porcen_aceptado AS porcenaceptado, " +
																"re.porcen_soportado AS porcensoportado, " +
																"crg.tipo_concepto AS tipoconcepto, " +
																"crg.concepto_ajuste AS conceptoajus " +
																"FROM glosas.fact_respuesta_glosa frg " +
																"INNER JOIN glosas.respuesta_glosa re ON(frg.respuesta_glosa=re.codigo)" +
																"INNER JOIN facturacion.facturas f ON(frg.factura=f.codigo)" +
																"INNER JOIN cuentas_cobro cc ON (f.numero_cuenta_cobro=cc.numero_cuenta_cobro AND f.institucion=cc.institucion) " +
																"INNER JOIN glosas.auditorias_glosas ag ON(frg.auditoria_glosa=ag.codigo) " +
																"INNER JOIN glosas.registro_glosas rg ON(ag.glosa=rg.codigo) " +
																"INNER JOIN glosas.concepto_resp_glosas crg ON(crg.codigo=re.concepto_respuesta AND crg.institucion=re.institucion) " +
																"WHERE frg.respuesta_glosa= ?";
	
	private static String consultaSolConciliacion="  SELECT glosas.getdescripcionconceptoglosa(dfrg.concepto_glosa, ?) AS conceptoglosa, " +
														"CASE " +
														"WHEN dfs.articulo IS NULL " +
														"THEN 'SERV' " +
														"ELSE 'ARTI' " +
														"END AS esarticulo, " +
														"CASE " +
														"WHEN dag.servicio IS NULL " +
														"THEN getdescarticulo(dag.articulo) " +
														" ELSE '(' " +
														" || getcodigopropservicio2(dag.servicio,?) " +
														"|| ') ' " +
														"|| getnombreservicio(dag.servicio, "+ConstantesBD.codigoTarifarioCups+") " +
														"dag.cantidad_glosa AS cantidad, " +
														"dag.valor_glosa AS valorglosasoli, " +
														"dfrg.valor_soportado AS valorsoportadosol, " +
														"dfrg.valor_aceptado AS valoraceptadosol, " +
														"dag.solicitud AS solicitud, " +
														"dfrg.concepto_respuesta AS conceptoconc, " +
														"dfrg.motivo AS motivo, " +
														"crg.tipo_concepto AS tipoconcepto, " +
														"crg.concepto_ajuste AS conceptoajuste " +
														"FROM glosas.fact_respuesta_glosa frg " +
														"INNER JOIN glosas.det_fact_resp_glosa dfrg " +
														"ON(dfrg.fact_respuesta_glosa=frg.codigo) " +
														"INNER JOIN glosas.det_auditorias_glosas dag " +
														"ON(dfrg.det_auditoria_glosa=dag.codigo) " +
														"INNER JOIN det_factura_solicitud dfs " +
														"ON (dfs.codigo = dag.det_factura_solicitud) " +
														"INNER JOIN glosas.concepto_resp_glosas crg ON(dfrg.concepto_respuesta=crg.codigo AND dfrg.institucion=crg.institucion) " +
														"WHERE frg.codigo= ?";

	private static String consultaConceptosGlosaFact="SELECT cag.codigo AS codigo, " +
														"cag.concepto_glosa AS conceptoglosa, " +
														"ag.valor_glosa_factura AS valorglosa, " +
														"cg.descripcion AS descripcion " +
														"FROM glosas.auditorias_glosas ag " +
														"INNER JOIN glosas.conceptos_audi_glosas cag ON(cag.auditoria_glosa=ag.codigo) " +
														"INNER JOIN glosas.concepto_glosas cg ON(cag.concepto_glosa=cg.codigo AND cag.institucion=cg.institucion) " +
														"WHERE ag.codigo=? ";

	public static ArrayList<DtoFacturaGlosa> consultarConceptosGlosaFact(String auditoria) 
	{		
		ArrayList<DtoFacturaGlosa> listaConceptoGlosaFact= new ArrayList<DtoFacturaGlosa>();
		
		Connection con;		
		con= UtilidadBD.abrirConexion();
				
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con,consultaConceptosGlosaFact);
						
			ps.setString(1, auditoria);
			
			logger.info("\n\nconsulta:: "+ps);
			
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());						
			
			while(rs.next())
			{
				DtoFacturaGlosa dto= new DtoFacturaGlosa();
				dto.getConceptoGlosa().setCodigo(rs.getString("codigo"));
				dto.getConceptoGlosa().setCodigoConcepto(rs.getString("conceptoglosa"));
				dto.getConceptoGlosa().setDescripcion(rs.getString("descripcion"));
				dto.setValorGlosaFactura(rs.getDouble("valorglosa"));
				
				listaConceptoGlosaFact.add(dto);
			}
			rs.close();
			ps.close();
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR CONSULTANDO CONCEPTOS FACTURA GLOSA.------>>>>>>"+e);
			e.printStackTrace();
		}	
		
		return listaConceptoGlosaFact;
	}
	
	public static ArrayList<DtoRespuestaSolicitudGlosa> consultaSolResp(int respuesta, String codTarifario, int institucion) 
	{
		ArrayList<DtoRespuestaSolicitudGlosa> listaSolConc= new ArrayList<DtoRespuestaSolicitudGlosa>();
		
		Connection con;		
		con= UtilidadBD.abrirConexion();
				
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con,consultaSolConciliacion);
						
			ps.setInt(1, institucion);
			ps.setString(2, codTarifario);
			ps.setInt(3, respuesta);
			
			logger.info("\n\nconsulta:: "+ps);
			
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());						
			
			while(rs.next())
			{
				DtoRespuestaSolicitudGlosa dto= new DtoRespuestaSolicitudGlosa();
				dto.setSolicitud(rs.getString("solicitud"));	
				dto.setDescripcionServicioArticulo(rs.getString("descripcionarticuloservicio"));
				dto.setCantidadGlosa(rs.getString("cantidad"));
				dto.setValorGlosa(rs.getString("valorglosasoli"));
				dto.setConceptoGlosa(rs.getString("conceptoglosa"));
				dto.setConceptoRespuesta(rs.getString("conceptoconc")+ConstantesBD.separadorSplit+rs.getString("tipoconcepto")+ConstantesBD.separadorSplit+rs.getString("conceptoajuste"));
				dto.setMotivo(rs.getString("motivo"));
				dto.setValorAceptado(rs.getString("valoraceptadosol"));
				dto.setValorSoportado(rs.getString("valorsoportadosol"));
				listaSolConc.add(dto);
			}
			rs.close();
			ps.close();
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR CONSULTANDO SOLICITUDES CONCILIACION.------>>>>>>"+e);
			e.printStackTrace();
		}	
		
		return listaSolConc;
	}
	
	public static ArrayList<DtoRespuestaFacturaGlosa> consultaFacturasConciliacion(String codconciliacion) 
	{
		ArrayList<DtoRespuestaFacturaGlosa> listaFacturasConc= new ArrayList<DtoRespuestaFacturaGlosa>();
		
		Connection con;		
		con= UtilidadBD.abrirConexion();
				
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con,consultaFacturasConciliacion);
						
			ps.setInt(1, Utilidades.convertirAEntero(codconciliacion));
			
			logger.info("\n\nconsulta:: "+ps);
			
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());						
			
			while(rs.next())
			{
				DtoRespuestaFacturaGlosa dto= new DtoRespuestaFacturaGlosa();
			
				dto.setCodigoFactura(rs.getInt("codfactura"));
				dto.setConsecutivoFactura(rs.getString("factura"));
				dto.setFechaFactura(rs.getString("fechafactura"));
				dto.setFechaRadicacion(rs.getString("fecharadcc"));
				dto.getGlosa().setCodigo(rs.getString("codglosa"));
				dto.getGlosa().setGlosaSistema(rs.getString("glosasistema"));
				dto.setSaldoFactura(rs.getString("saldofactura"));
				dto.setValorGlosaFactura(rs.getString("valorglosafact"));
				dto.getFactura().setNombrePaciente(rs.getString("paciente"));
				dto.getFactura().setTipoFacturaSistema(rs.getBoolean("factinterna"));
				dto.getGlosa().setGlosaEntidad(rs.getString("glosaentidad"));
				dto.setPorcentajeAceptado(rs.getDouble("porcenaceptado"));
				dto.setPorcentajeSoportado(rs.getDouble("porcensoportado"));
				dto.setValorAceptado(rs.getDouble("valoraceptadofact"));
				dto.setValorSoportado(rs.getDouble("valorsoportadofact"));
				dto.setAuditoria(rs.getString("codauditoria"));
				dto.setRespuesta(Utilidades.convertirAEntero(codconciliacion));
				dto.setEliminar(rs.getString("eliminar"));
				dto.setCodigoFactRespuestaGlosa(rs.getString("codigofcatresp"));
				dto.setConcepto(rs.getString("concepto")+ConstantesBD.separadorSplit+rs.getString("tipoconcepto")+ConstantesBD.separadorSplit+rs.getString("conceptoajus"));				
				listaFacturasConc.add(dto);
			}
			rs.close();
			ps.close();
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR CONSULTANDO FACTURAS CONCILIACION.------>>>>>>"+e);
			e.printStackTrace();
		}	
		
		return listaFacturasConc;
	}
	
	public static ArrayList<DtoDetalleAsociosGlosa> consultarAsociosDetFacturaResp(int detAuditoriaGlosa)
	{
		ArrayList<DtoDetalleAsociosGlosa> listaDtoAsocios= new ArrayList<DtoDetalleAsociosGlosa>();
		
		Connection con;		
		con= UtilidadBD.abrirConexion();
				
		try
		{
			logger.info("\n\nconsulta::::::: "+consultarAsociosDetFacturaResp+" det auditoria: "+detAuditoriaGlosa);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarAsociosDetFacturaResp,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						
			ps.setInt(1, detAuditoriaGlosa);
			
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());						
			
			while(rs.next())
			{
				DtoDetalleAsociosGlosa dto= new DtoDetalleAsociosGlosa();
				dto.setCodigoAsocio(rs.getInt("codigoasocio"));
				dto.setCodigoAsocioDetalleFactura(rs.getString("asociodetfactura"));
				dto.setCodigoDetalleGlosa(rs.getString("detauditoriaglosa"));
				dto.setTipoAsocio(rs.getString("tipoasocio"));
				dto.setCodigoProfesional(rs.getInt("codigomedico"));
				dto.setCantidadGlosa(rs.getInt("cantidadglosa"));
				dto.setValorGlosa(rs.getInt("valorglosa"));
				dto.setCodigoServicioAsocio(rs.getInt("servicioasocio"));
				dto.setCantidad(rs.getInt("cantidad"));
				dto.setValor(rs.getInt("valor"));
				dto.setServicioAsocio(rs.getString("servicioasocio"));
				dto.getDetalleFacturaGlosa().setCodigoDetalleFacturaSolicitud(rs.getString("detfactsolicitud"));
				dto.setConsecutivoAsoDetFact(rs.getString("consecutivoasodetfact"));
				dto.setCodigoMedico(rs.getString("codigomedicoaso"));
				dto.setPoolAsocio(rs.getString("poolaso"));
				dto.setValorStr(rs.getString("valorasocio"));
				dto.setPorcentajePool(rs.getString("porcentajepoolaso"));
				dto.getConceptoGlosa().setDescripcion(rs.getString("descconceptoglosa"));
				dto.getConceptoGlosa().setCodigo(rs.getString("codconcglosa"));
				listaDtoAsocios.add(dto);
			}
			rs.close();
			ps.close();
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR CONSULTANDO ASOCIOS GLOSA.------>>>>>>"+e);
			e.printStackTrace();
		}	
		
		return listaDtoAsocios;
	}
	
	public static String insertarDetalleFacturaConciliacion(Connection con, DtoDetalleFacturaGlosa dto, int institucion, DtoRespuestaFacturaGlosa dtoFactResp, double ajuste) 
	{		
		PreparedStatementDecorator ps;		
						
		try
		{
			ps= new PreparedStatementDecorator(con,insertarDetalleFacturaConciliacion);	

			logger.info("\n\nconsulta: "+insertarDetalleFacturaConciliacion);			
			
			int valorseq=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_fact_respuesta_glosa");
			ps.setInt(1, valorseq);
			ps.setInt(2, Utilidades.convertirAEntero(dtoFactResp.getCodigoFactRespuestaGlosa()));
			ps.setInt(3, Utilidades.convertirAEntero(dto.getConcepto().getCodigoDetalleFacturaGlosa()));
			ps.setInt(4, Utilidades.convertirAEntero(dto.getCodigoDetalleFacturaSolicitud()));
			ps.setString(5, dto.getConcepto().getCodigo());
			ps.setString(6, dtoFactResp.getConcepto().split(ConstantesBD.separadorSplit)[0]);
			ps.setInt(7, institucion);
			if(ajuste > 0)
				ps.setInt(8, (int)ajuste);
			else
				ps.setNull(8, Types.INTEGER);
			ps.setInt(9, dto.getCantidadGlosa());
			ps.setInt(10, Utilidades.convertirAEntero(dtoFactResp.getValorAceptado()+""));
			ps.setInt(11, Utilidades.convertirAEntero(dtoFactResp.getValorSoportado()+""));
			ps.setString(12, dto.getMotivo());
			logger.info("consulta:::::: "+ps);
			if(ps.executeUpdate()>0)
			{				
				ps.close();
				return valorseq+"";
			}
			ps.close();						
		}
		catch (SQLException e)
		{
			logger.info("\n\nerror insertando detalle factura conciliacion------>>>>>>"+e);
			e.printStackTrace();
		}		
		return "";
	}
	
	public static ArrayList<DtoDetalleFacturaGlosa> consultaConceptosGlosa(String auditoria, String codigoTarifario) {
		ArrayList<DtoDetalleFacturaGlosa> listaDtoDetFacGlosa= new ArrayList<DtoDetalleFacturaGlosa>();
		
		Connection con;		
		con= UtilidadBD.abrirConexion();
				
		try
		{
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con,consultaDetalleFacGlosa);
						
			ps.setString(1, codigoTarifario);
			ps.setInt(2, Utilidades.convertirAEntero(auditoria));
			
			logger.info("\n\nconsulta::::::: "+ps);
			
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());						
			
			while(rs.next())
			{
				DtoDetalleFacturaGlosa dto= new DtoDetalleFacturaGlosa();
				dto.getConcepto().setCodigo(rs.getString("codigo"));
				dto.getConcepto().setDescripcion(rs.getString("descripcion"));
				dto.getConcepto().setCodigoFacturaGlosa(rs.getString("fac"));
				dto.getConcepto().setCodigoDetalleFacturaGlosa(rs.getString("coddetaudi"));
				dto.getConcepto().setTipo(rs.getString("tipoconcepto"));
				dto.setCodigoAuditoria(rs.getString("codaudi"));
				dto.setNumeroSolicitud(rs.getString("numsolicitud"));
				dto.setDescripcionServicioArticulo(rs.getString("descripcionarticuloservicio"));
				dto.setCodigoDetalleFacturaSolicitud(rs.getString("detfactsolicitud"));
				dto.setValorGlosa(rs.getDouble("valorglosa"));
				dto.setCantidadGlosa(rs.getInt("cantidadglosa"));
				dto.getFactura().setNumeroCuentaCobro(rs.getDouble("cuentacobro"));
				dto.setPool(rs.getString("pool"));
				dto.setCodigoMedico(rs.getString("codigomedico"));
				dto.setEsCirugia(rs.getString("escirugia"));
				dto.setValorStr(rs.getString("valorglosa"));
				dto.setCentroCosto(rs.getString("centrocosto"));
				listaDtoDetFacGlosa.add(dto);
			}
			ps.close();
			rs.close();
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR CONSULTANDO DETALLE FACTURA GLOSA.------>>>>>>"+e);
			e.printStackTrace();
		}	
		
		return listaDtoDetFacGlosa;
	}
	
	public static String insertarFacturasRespGlosa(DtoRespuestaFacturaGlosa dto, int institucion) 
	{		
		Connection con;		
		con= UtilidadBD.abrirConexion();
		PreparedStatementDecorator ps;		
						
		try
		{
			ps= new PreparedStatementDecorator(con,insertarFacturasRespGlosa);	
			
			int valorseq=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_fact_respuesta_glosa");
			ps.setInt(1, valorseq);
			ps.setInt(2, dto.getRespuesta());			
			ps.setInt(3, dto.getCodigoFactura());			
			ps.setString(4, dto.getAuditoria());	
			if(dto.getFactura().getTipoFacturaSistema())
				ps.setString(5, ConstantesBD.acronimoSi);
			else
				ps.setString(5, ConstantesBD.acronimoNo);
			ps.setDouble(6, dto.getValorAceptado());			
			ps.setDouble(7, dto.getValorSoportado());		
			ps.setInt(8,institucion);
			ps.setString(9,dto.getConcepto().split(ConstantesBD.separadorSplit)[0]);

			logger.info("\n\nconsulta::"+ps);
											
			if(ps.executeUpdate()>0)
			{
				ps.close();
				UtilidadBD.cerrarConexion(con);
				return valorseq+"";
			}			
		}
		catch (SQLException e)
		{
			logger.info("\n\nerror insertando detalle conciliacion------>>>>>>"+e);
			e.printStackTrace();
		}
		
		return "";
	}
	
	public static ArrayList<DtoFacturaGlosa> consultarFacturasGlosa(HashMap<String, Object> mapa) 
	{
		ArrayList<DtoFacturaGlosa> listaDtoFacturaGlosa= new ArrayList<DtoFacturaGlosa>();
		
		Connection con;		
		con= UtilidadBD.abrirConexion();
		
		String consulta= consultarFacturasPorGlosa;
				 		
		if(!mapa.get("glosa").equals("") || !mapa.get("glosaEnt").equals(""))
			consulta += "AND rg.convenio = "+mapa.get("convenio")+" ";		
		if(!mapa.get("glosa").equals(""))
			consulta += "AND rg.glosa_sistema = '"+mapa.get("glosa")+"' ";
		if(!mapa.get("glosaEnt").equals(""))
			consulta += "AND rg.glosa_entidad = "+mapa.get("glosaEnt")+" ";
		if(!mapa.get("facturaIni").equals("") && !mapa.get("facturaFin").equals(""))
			consulta += "AND f.consecutivo_factura between "+mapa.get("facturaIni")+" AND "+mapa.get("facturaFin")+" ";
		if(!mapa.get("fechaElabIni").equals("") && !mapa.get("fechaElabFin").equals(""))
			consulta += "AND to_char(ag.fecha_elaboracion_fact,'"+ConstantesBD.formatoFechaAp+"') between '"+mapa.get("fechaElabIni")+"' AND '"+mapa.get("fechaElabFin")+"' ";
		
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			logger.info("\n\nconsulta::::::: "+consulta);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
						
			while(rs.next())
			{
				DtoFacturaGlosa dto= new DtoFacturaGlosa();
				dto.setCodigoFactura(rs.getString("codfactura"));
				dto.setConsecutivoFactura(rs.getString("consecutivofac"));
				dto.setFechaElaboracionFactura(rs.getString("fechaelabfactura"));
				dto.setFechaRadicacion(rs.getString("fecharadcxc"));
				dto.setCodigoGlosa(rs.getString("codglosa"));
				dto.getGlosa().setGlosaSistema(rs.getString("glosasis"));
				dto.setSaldoFactura(rs.getDouble("saldofactura"));
				dto.setSaldoFacturaStr(rs.getString("saldofactura"));
				dto.setValorGlosaFactura(rs.getDouble("valorglosafac"));
				dto.setValorGlosaFacturaStr(rs.getString("valorglosafac"));
				dto.getFactura().setNombrePaciente(rs.getString("paciente"));
				dto.getFactura().setTipoFacturaSistema(rs.getBoolean("factinterna"));
				dto.getGlosa().setGlosaEntidad(rs.getString("glosaentidad"));
				dto.setPorcentajeAceptado(rs.getDouble("porcenaceptado"));
				dto.setPorcentajeSoportado(rs.getDouble("porcensoportado"));
				dto.setCodigo(rs.getString("codauditoria"));
				dto.setEliminar(rs.getString("eliminar"));
				dto.setSeleccionado(rs.getString("seleccionado"));
				dto.getGlosa().setEstadoRespuesta(rs.getString("estadorespuesta"));
				listaDtoFacturaGlosa.add(dto);
			}
			ps.close();
			rs.close();
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR CONSULTANDO FACTURAS POR GLOSA.------>>>>>>"+e);
			e.printStackTrace();
		}	
		
		return listaDtoFacturaGlosa;
	}
	
	public static int guardarConciliacion(HashMap<String, Object> criterios) 
	{
		int conciliacion=0;
		
		Connection con;		
		con= UtilidadBD.abrirConexion();
		PreparedStatementDecorator ps;		
		
		logger.info("\n\ncriterios conciliacion: "+criterios);
		
		if((criterios.get("nuevo")+"").equals(ConstantesBD.acronimoSi))
		{		
			try
			{
				ps= new PreparedStatementDecorator(con.prepareStatement(insertarEncabezadoConciliacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	
	
				conciliacion=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_respuesta_glosa");
				ps.setInt(1, conciliacion);
				ps.setString(2, criterios.get("conciliacion")+"");
				ps.setString(3, UtilidadFecha.conversionFormatoFechaABD(criterios.get("fecha")+""));
				ps.setString(4, criterios.get("hora")+"");
				ps.setString(5, criterios.get("usuario")+"");
				ps.setString(6, criterios.get("observ")+"");
				ps.setString(7, ConstantesIntegridadDominio.acronimoEstadoGlosaRegistrada);
				ps.setInt(8, Utilidades.convertirAEntero(criterios.get("institucion")+""));
				ps.setString(9, criterios.get("usuario")+"");
				ps.setInt(10, Utilidades.convertirAEntero(criterios.get("convenio")+""));
				ps.setString(11, criterios.get("nroActa")+"");
				ps.setString(12, criterios.get("repConvenio")+"");
				ps.setString(13, criterios.get("cargoRConvenio")+"");
				ps.setString(14, criterios.get("repInstitucion")+"");
				ps.setString(15, criterios.get("cargoRInst")+"");
				ps.setInt(16, Utilidades.convertirAEntero(criterios.get("pSoportado")+""));
				ps.setInt(17, Utilidades.convertirAEntero(criterios.get("pAceptado")+""));
				ps.setString(18, criterios.get("concConciliacion")+"");
												
				if(ps.executeUpdate()>0)
				{
					ps.close();
					UtilidadBD.cerrarConexion(con);
					return conciliacion;
				}
				
			}
			catch (SQLException e)
			{
				logger.info("\n\nerror insertando conciliacion------>>>>>>"+e);
				e.printStackTrace();
			}
		}
		else
		{	
			try {
				
				ps =  new PreparedStatementDecorator(con.prepareStatement(actualizarEncabezadoConciliacion, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
								
				ps.setString(1, UtilidadFecha.conversionFormatoFechaABD(criterios.get("fecha")+""));
				ps.setString(2, criterios.get("hora")+"");
				ps.setString(3, criterios.get("usuario")+"");
				ps.setString(4, criterios.get("observ")+"");
				ps.setString(5, ConstantesIntegridadDominio.acronimoEstadoGlosaRegistrada);
				ps.setInt(6, Utilidades.convertirAEntero(criterios.get("institucion")+""));
				ps.setString(7, criterios.get("usuario")+"");
				ps.setInt(8, Utilidades.convertirAEntero(criterios.get("convenio")+""));
				ps.setString(9, criterios.get("nroActa")+"");
				ps.setString(10, criterios.get("repConvenio")+"");
				ps.setString(11, criterios.get("cargoRConvenio")+"");
				ps.setString(12, criterios.get("repInstitucion")+"");
				ps.setString(13, criterios.get("cargoRInst")+"");
				ps.setInt(14, Utilidades.convertirAEntero(criterios.get("pSoportado")+""));
				ps.setInt(15, Utilidades.convertirAEntero(criterios.get("pAceptado")+""));
				ps.setString(16, criterios.get("concConciliacion")+"");
				ps.setString(17, criterios.get("codConciliacion")+"");
				
				if(ps.executeUpdate()>0)
				{
					ps.close();
					UtilidadBD.cerrarConexion(con);
					return Utilidades.convertirAEntero(criterios.get("codConciliacion")+"");
				}
				ps.close();
					
			}
			catch (SQLException e) 
			{	
				logger.info("\n\nerror actualizando conciliacion "+e);		
			}
		}

		return ConstantesBD.codigoNuncaValido;
	}
	
	public static ArrayList<DtoConceptoRespuesta> consultarTiposConcepto()
	{
		ArrayList<DtoConceptoRespuesta> listaDtoConcepto= new ArrayList<DtoConceptoRespuesta>();
					
		Connection con;		
		con= UtilidadBD.abrirConexion();
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarTiposConcepto,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			logger.info("\n\nconsulta::::::: "+consultarTiposConcepto);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
						
			while(rs.next())
			{
				DtoConceptoRespuesta dto= new DtoConceptoRespuesta();
				dto.setCodigo(rs.getString("codigo"));
				dto.setDescripcion(rs.getString("descripcion"));
				dto.setConceptoajuste(rs.getString("conceptoajus"));
				dto.setNaturalezaajuste(rs.getString("naturaleza"));
				dto.setTipo(rs.getString("tipoconcepto"));
				listaDtoConcepto.add(dto);
			}
			ps.close();
			rs.close();
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR CONSULTANDO CONCEPTOS DE RESPUESTA.------>>>>>>"+e);
			e.printStackTrace();
		}	
		
		return listaDtoConcepto;
	}
}