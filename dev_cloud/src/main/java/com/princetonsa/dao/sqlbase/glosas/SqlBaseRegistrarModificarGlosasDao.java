package com.princetonsa.dao.sqlbase.glosas;

import java.math.BigDecimal;
import java.sql.Connection;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.glosas.DtoObsFacturaGlosas;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * 
 * @author Angela María Angel amangel@princetonsa.com
 *
 */

public class SqlBaseRegistrarModificarGlosasDao
{
	/**
	 * Para el manejo de logs
	 */
	private static Logger logger = Logger.getLogger(SqlBaseConfirmarAnularGlosasDao.class);
	
	/**
	 * Variable para la consulta del detalle de las Facturas
	 */
	public static String consultaDetalleGlosas;
	
	/**
	 * Consulta para Actualizar la Glosa Modificada
	 */
	private static String actualizarGlosa="UPDATE registro_glosas " +
											"SET fecha_registro_glosa=?, " +
											"convenio=?, " +
											"glosa_entidad=?, " +
											"fecha_notificacion=?, " +
											"valor_glosa=?, " +
											"observaciones=?, " +
											"usuario_glosa=?, " +
											"estado='"+ConstantesIntegridadDominio.acronimoEstadoRegistrado+"', " +
											"usuario_modifica=?, " +
											"fecha_modifica=CURRENT_DATE, " +
											"contrato=? " +
											"WHERE codigo=? ";
		
	/**
	 * Consulta para Insertar una nueva Glosa 
	 */
	private static String insertarNuevaGlosa="INSERT INTO registro_glosas (" +
												"codigo," +
												"glosa_sistema," +
												"fecha_registro_glosa," +
												"convenio," +
												"glosa_entidad," +
												"fecha_notificacion," +
												"valor_glosa," +
												"observaciones, " +
												"usuario_glosa, " +
												"fecha_modifica," +
												"hora_modifica," +
												"usuario_modifica," +
												"institucion," +
												"estado," +
												"contrato) " +
												"VALUES(?,?,CURRENT_DATE,?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?," +
												"'"+ConstantesIntegridadDominio.acronimoEstadoRegistrado+"'," +
												"?) ";
	
	/**
	 * Cadena que consulta el historico de una factura
	 */
	private static String consultaHistorico="SELECT rg.glosa_sistema AS glosa, " +
											"rg.glosa_entidad AS glosaent, " +
											"rg.fecha_notificacion AS fechanot, " +
											"rg.fecha_registro_glosa AS fechareg, " +
											"rsg.respuesta_consecutivo AS respuesta, " +
											"to_char(rsg.fecha_radicacion, '"+ConstantesBD.formatoFechaBD+"') AS fecharadiresp, " +
											"rg.valor_glosa AS valorglosa, " +
											"ag.fue_auditada AS indicativo, " +
											"ag.codigo_factura AS codfactura, " +
											"getintegridaddominio(rg.estado) AS estado " +
											"FROM auditorias_glosas ag " +
											"INNER JOIN registro_glosas rg ON (rg.codigo=ag.glosa) " +
											"LEFT OUTER JOIN respuesta_glosa rsg ON (rg.codigo=rsg.glosa) " +
											"WHERE ag.codigo_factura=? " +
													"AND rg.estado not in ('"+ConstantesIntegridadDominio.acronimoEstadoGlosaAnulada+"') " ;
													
		
	
	/**
	 * Cadena para consultar las fechas de radicacion de las Respuestas de las Glosas asociadas a una Factura 
	 */
	private static String consultaFechaRadicacion="SELECT to_char(rg.fecha_notificacion, '"+ConstantesBD.formatoFechaBD+"') AS fechaglosa, " +
														"to_char(rsg.fecha_radicacion, '"+ConstantesBD.formatoFechaBD+"') AS fecharesp " +
														"FROM registro_glosas rg " +
														"INNER JOIN respuesta_glosa rsg ON (rg.codigo=rsg.glosa) " +
														"INNER JOIN fact_respuesta_glosa f ON (f.respuesta_glosa=rsg.codigo)" +
														"WHERE f.factura=? " +
														"AND rsg.estado='RADIC' ";
		
	/**
	 * Cadena que consulta la fecha de radicacion de la cuentad e cobro asociada a uan Factura
	 */
	private static String consultaFechaRadiCC="SELECT to_char(cc.fecha_radicacion, '"+ConstantesBD.formatoFechaBD+"') AS fechaRad, " +
												"to_char(rg.fecha_notificacion, '"+ConstantesBD.formatoFechaBD+"') AS fechaglosa " +
												"FROM registro_glosas rg " +
												"INNER JOIN auditorias_glosas ag ON(ag.glosa=rg.codigo) " +
												"INNER JOIN facturas f ON (f.codigo=ag.codigo_factura) " +
												"INNER JOIN cuentas_cobro cc ON(f.numero_cuenta_cobro=cc.numero_cuenta_cobro) " +
												"WHERE f.codigo=? ";
												
	
	/**
	 * Cadena que consulta una factura por consecutivo
	 */
	private static String consultaUnicaFactura="SELECT f.codigo AS codfactura, " +
												" f.consecutivo_factura AS factura, " +
												" to_char(f.fecha, '"+ConstantesBD.formatoFechaBD+"') AS fechaelab, " +
												" f.numero_cuenta_cobro AS cuentacobro, " +
												" to_char(cc.fecha_radicacion, '"+ConstantesBD.formatoFechaBD+"') AS fecharadicacion, " +
												" sc.contrato AS contrato, " +
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
												"f.tipo_factura_sistema AS tipofactura " +
											"FROM facturas f " +
												"INNER JOIN cuentas_cobro cc ON (f.numero_cuenta_cobro=cc.numero_cuenta_cobro AND f.institucion=cc.institucion) " +
												"INNER JOIN sub_cuentas sc ON (f.sub_cuenta=sc.sub_cuenta) " +
											"WHERE f.consecutivo_factura=? " +
												"AND (coalesce(f.valor_total,0)" +
												"+" +
												"coalesce(f.ajustes_debito,0)" +
													"-" +
													"coalesce(f.ajustes_credito,0)" +
														"+" +
														"coalesce(cc.ajuste_debito,0)" +
															"-" +
															"coalesce(cc.ajuste_credito,0)" +
																"-" +
																"coalesce(cc.pagos,0))>0 " +
												"AND f.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionFacturada+" " +
												"AND cc.estado="+ConstantesBD.codigoEstadoCarteraRadicada+" ";
	
	/**
	 * Cadena que realiza insercion de una nueva factura
	 */
	private static String insertarFactura="INSERT INTO auditorias_glosas (" +
											"codigo, " +
											"codigo_factura, " +
											"fecha_modificacion, " +
											"hora_modificacion, " +
											"usuario_modificacion, " +
											"contrato, " +
											"valor_glosa_factura, " +
											"saldo_factura, " +
											"fecha_elaboracion_fact, " +
											"numero_cuenta_cobro, " +
											"institucion, " +
											"fecha_radicacion_cxc, " +
											"glosa, " +
											"fue_auditada) " +
											"VALUES(?,?,CURRENT_DATE,?,?,?,?,?,?,?,?,?,?,'GL') ";	
	
	/**
	 * Cadena que inserta los conceptos por Auditoria
	 */
	private static String insertarConceptoFactura="INSERT INTO conceptos_audi_glosas (" +
													"codigo, " +
													"auditoria_glosa, " +
													"concepto_glosa, " +
													"institucion) " +
													"VALUES(?,?,?,?) ";
	
	/**
	 * Cadena que realiza actualizacion de una factura
	 */
	private static String actualizarFactura="UPDATE auditorias_glosas " +
												"SET valor_glosa_factura=? " +
												"WHERE codigo=? ";
	
	/**
	 * Cadena que actualiza el valor de la Glosa 
	 */
	private static String actualizarValorGlosa="UPDATE registro_glosas " +
												"SET valor_glosa=? " +
												"WHERE codigo=? ";
	
	/**
	 * Cadena que elimina una factura
	 */
	private static String eliminarFactura="DELETE FROM auditorias_glosas " +
											"WHERE codigo=?";
	
	/**
	 * Cadena que consulta los contratos de todos los detalles glosa factura
	 */
	private static String consultaContratosFacturas="SELECT codigo AS codigo, " +
															"codigo_factura AS codfactura, " +
															"contrato AS contrato " +
													"FROM auditorias_glosas ag " +
													"WHERE ag.glosa=? ";
	
	/**
	 * Cadena que consulta todos los det_audi_glosas por el codigo de la auditoria
	 */
	private static String consultaDetalleFacturaGlosa="SELECT codigo, auditoria_glosa FROM det_auditorias_glosas WHERE auditoria_glosa=? ";
	
	/**
	 * Cadena que consulta todos los asocios por solicitud detalle factura glosa
	 */
	private static String consultaAsociosDetalleFacturaGlosa="SELECT codigo, det_auditoria_glosa FROM asocios_auditorias_glosas WHERE det_auditoria_glosa=? ";
	
	/**
	 * Cadena para eliminar los conceptos por detalle factura glosa
	 */
	private static String eliminarConceptosDetalleFacturaGlosa="DELETE FROM conceptos_det_audi_glosas WHERE det_audi_fact=? ";
	
	/**
	 * Cadena para eliminar los conceptos por asocios solicitud detalle factura glosa
	 */
	private static String eliminarConceptosAsociosSolicitudDetalleFacturaGlosa="DELETE FROM conceptos_aso_audi_glosas WHERE asocio_auditoria_glosa=? ";
	
	/**
	 * Cadena para eliminar los asocios por solicitud detalle factura glosa 
	 */
	private static String eliminarAsociosSolicitudDetalleFacturaGlosa="DELETE FROM asocios_auditorias_glosas WHERE det_auditoria_glosa=? ";
	
	/**
	 * Cadena para eliminar las solicitudes detalle Factura glosa
	 */
	private static String eliminarDetalleFacturaGlosa="DELETE FROM det_auditorias_glosas WHERE auditoria_glosa=? ";
	
	/**
	 * Cadena para eliminar los conceptos de la Auditoria glosa
	 */
	private static String eliminarConceptosAudiGlosa="DELETE FROM conceptos_audi_glosas WHERE auditoria_glosa=? ";
	
	/**
	 * Cadena para consultar los conceptos segun el estado y el tipo concepto
	 */
	private static String consultaConceptos="SELECT codigo AS codconcepto, " +
											"descripcion AS descconcepto, " +
											"tipo_concepto AS tipoconcepto " +
											"FROM concepto_glosas ";
	
	/**
	 * Cadena para consultar todos los conceptos de cada factura GLosa por solicitud o asocio solicitud
	 */
	private static String consultaTodosConceptosFacturas = "SELECT codigocc AS codigo, descripcioncc AS descripcion, codfac AS fac, tipoconceptocc AS tipoconcepto FROM " +
															"(" +
															"(SELECT cc.codigo AS codigocc, " +
																"cc.descripcion AS descripcioncc, " +
																"ag.codigo_factura AS codfac, " +
																"cc.tipo_concepto AS tipoconceptocc " +
															"FROM auditorias_glosas ag " +
																"INNER JOIN conceptos_audi_glosas cag ON (cag.auditoria_glosa=ag.codigo) " +
																"INNER JOIN concepto_glosas cc ON (cag.concepto_glosa=cc.codigo AND cag.institucion=cc.institucion) " +
															"WHERE ag.glosa=? GROUP BY cc.codigo, cc.descripcion, ag.codigo_factura, cc.tipo_concepto) " +
														"UNION " +
															"(SELECT cc.codigo AS codigocc, " +
																"cc.descripcion AS descripcioncc, " +
																"ag.codigo_factura AS codfac, " +
																"cc.tipo_concepto AS tipoconceptocc " +
															"FROM auditorias_glosas ag " +
																"INNER JOIN det_auditorias_glosas dag ON (dag.auditoria_glosa=ag.codigo) " +
																"INNER JOIN conceptos_det_audi_glosas cdag ON (cdag.det_audi_fact=dag.codigo) " +
																"INNER JOIN concepto_glosas cc ON (cdag.concepto_glosa=cc.codigo AND cdag.institucion=cc.institucion) " +
															"WHERE ag.glosa=? GROUP BY cc.codigo, cc.descripcion, ag.codigo_factura, cc.tipo_concepto) " +
														"UNION " +
															"(SELECT cc.codigo AS codigocc, " +
																"cc.descripcion AS descripcioncc, " +
																"ag.codigo_factura AS codfac, " +
																"cc.tipo_concepto AS tipoconceptocc " +
															"FROM auditorias_glosas ag " +
																"INNER JOIN det_auditorias_glosas dag ON (dag.auditoria_glosa=ag.codigo) " +
																"INNER JOIN asocios_auditorias_glosas aag ON (aag.det_auditoria_glosa=dag.codigo) " +
																"INNER JOIN conceptos_aso_audi_glosas caag ON (caag.asocio_auditoria_glosa=aag.codigo) " +
																"INNER JOIN concepto_glosas cc ON (caag.concepto_glosa=cc.codigo AND caag.institucion=cc.institucion) " +
															"WHERE ag.glosa=? GROUP BY cc.codigo, cc.descripcion, ag.codigo_factura, cc.tipo_concepto) " +
														") t GROUP BY codigocc, descripcioncc, codfac, tipoconceptocc ORDER BY descripcioncc ";
						
	
		
	/**
	 * Cadena que consulta el numero de Glosas en estado Respondida o Conciliada asociadas a una factura especifica
	 */
	private static String consultarNumeroGlosasPorFactura="SELECT count(rg.codigo) AS codglosa " +
															"FROM auditorias_glosas ag " +
															"INNER JOIN registro_glosas rg ON (rg.codigo=ag.glosa) " +
															"WHERE ag.codigo_factura=? " +
																"AND ag.fue_auditada= 'GL' " +
																"AND rg.estado = ('"+ConstantesIntegridadDominio.acronimoEstadoGlosaRespondida+"') OR " +
																		"estado = ('"+ConstantesIntegridadDominio.acronimoEstadoGlosaConciliada+"') ";
	
	
	/**
	 * Metodo que consulta las Glosas en estado Respondida o Conciliada asociadas a una factura especifica
	 * @param con
	 * @param codFatura
	 * @return
	 */
	public static int consultarNumeroGlosasPorFactura(Connection con, String codFactura)
	{		
		PreparedStatementDecorator ps;
		int resultado= 0;
		ResultSetDecorator rs;
		
		try 
		{	
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultarNumeroGlosasPorFactura,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setString(1, codFactura);
			
			rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next()){
				resultado = rs.getInt("codglosa");
			}
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO NUM GLOSAS POR FACTURA------>>>>>>"+e);
			e.printStackTrace();
		}
		return resultado;	
	}
	
	
	/**
	 * Metodo que consulta todos los conceptos de las facturas asociadas
	 * a una Glosa, desde sus solicitudes hasta sus asocios
	 * @param con
	 * @param codigoGlosa  
	 * @return
	 */
	public static HashMap consultarTodosConceptosFacturas(Connection con, String codigoGlosa)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator ps;
		
		try
		{			
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaTodosConceptosFacturas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigoGlosa);
			ps.setString(2, codigoGlosa);
			ps.setString(3, codigoGlosa);
					
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			logger.info("\n\nquery:: "+consultaTodosConceptosFacturas+"  audi: "+codigoGlosa+"\n\nresultado: "+resultados);
			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO CONCEPTOS DE GLOSA------>>>>>>"+e);
			e.printStackTrace();
		}
		return resultados;
	}
	
	/**
	 * Metodo que consulta los conceptos segun el estado y el tipo concepto
	 * @param con
	 * @return
	 */
	public static HashMap consultarConceptos(Connection con, String estado, String tipoConcepto)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		String cadena = consultaConceptos;
		if(!tipoConcepto.equals(""))
			cadena += "WHERE tipo_concepto='"+tipoConcepto+"' ";
		PreparedStatementDecorator ps;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
	
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO CONCEPTOS DE GLOSA------>>>>>>"+e);
			e.printStackTrace();
		}
		return resultados;	
	}
	
	/**
	 * Metodo que consulta todos los detalles de solicitudes por factura glosa
	 * @param con
	 * @param codAudi
	 * @return
	 */
	public static HashMap consultarDetalleFacturaGlosa(Connection con, String codAudi)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator ps;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaDetalleFacturaGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codAudi);
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);				
			
			logger.info("\n\nCONSULTANDO DETALLE FACTURA GLOSA ----->>>>>>>>>>>"+consultaDetalleFacturaGlosa.replace("?", codAudi));
	
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO DETALLE FACTURA GLOSA------>>>>>>"+e);
			e.printStackTrace();
		}
		return resultados;
	}
	
	/**
	 * Metodo que consulta todos los asocios detalles de solicitudes por factura glosa
	 * @param con
	 * @param codAudi
	 * @return
	 */
	public static HashMap consultarAsociosDetalleFacturaGlosa(Connection con, String codDetAudi)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator ps;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaAsociosDetalleFacturaGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codDetAudi);
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);						
	
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO ASOCIOS DETALLE FACTURA GLOSA------>>>>>>"+e);
			e.printStackTrace();
		}
		return resultados;
	}
	
	/**
	 * Metodo que elimina todos los conceptos asociados a un detalle factura solicitud
	 * @param con
	 * @param codDetAudi
	 * @return
	 */
	public static boolean eliminarConceptosDetalleFacturaGlosa(Connection con, String codDetAudi)
	{
		PreparedStatementDecorator ps;		
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(eliminarConceptosDetalleFacturaGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codDetAudi);
			
			ps.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. ELIMINANDO CONCEPTOS DETALLE FACTURA GLOSA SQL------>>>>>>"+e);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Metodo que elimina todos los conceptos de asocios detalle factura solicitud
	 * @param con
	 * @param codAsoDetAudi
	 * @return
	 */
	public static boolean eliminarConceptosAsociosDetalleFacturaGlosa(Connection con, String codAsoDetAudi)
	{
		PreparedStatementDecorator ps;		
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(eliminarConceptosAsociosSolicitudDetalleFacturaGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codAsoDetAudi);
			
			ps.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. ELIMINANDO CONCEPTOS ASOCIOS DETALLE FACTURA GLOSA SQL------>>>>>>"+e);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Metodo que elimina todos los asocios detalle factura solicitud
	 * @param con
	 * @param codDetAudi
	 * @return
	 */
	public static boolean eliminarAsociosDetalleFacturaGlosa(Connection con, String codDetAudi)
	{
		PreparedStatementDecorator ps;		
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(eliminarAsociosSolicitudDetalleFacturaGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codDetAudi);
			
			ps.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. ELIMINANDO ASOCIOS DETALLE FACTURA GLOSA SQL------>>>>>>"+e);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Metodo que elimina todos los detalle factura solicitud
	 * @param con
	 * @param codAudi
	 * @return
	 */
	public static boolean eliminarDetalleFacturaGlosa(Connection con, String codAudi)
	{
		PreparedStatementDecorator ps;		
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(eliminarDetalleFacturaGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codAudi);
			
			ps.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. ELIMINANDO DETALLE FACTURA GLOSA SQL------>>>>>>"+e);			
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Metodo que elimina los conceptos de la Auditoria Glosa
	 * @param con
	 * @param codAudi
	 * @return
	 */
	public static boolean eliminarConceptosAudiGlosa(Connection con, String codAudi)
	{
		PreparedStatementDecorator ps;		
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(eliminarConceptosAudiGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codAudi);
			
			ps.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. ELIMINANDO CONCEPTOS AUDITORIA GLOSA SQL------>>>>>>"+e);			
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Metodo que elimina la Auditoria Glosa
	 * @param con
	 * @param codAudi
	 * @return
	 */
	public static boolean eliminarAuditoriaGlosa(Connection con, String codAudi)
	{
		PreparedStatementDecorator ps;		
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(eliminarFactura,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codAudi);
			
			ps.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. ELIMINANDO FACTURA GLOSA SQL------>>>>>>"+e);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Metodo que consulta todos los contratos por detalle factura de Glosa
	 * @param con
	 * @param codigoGlosa
	 * @return
	 */
	public static HashMap consultaContratosFactura(Connection con, String codigoGlosa)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator ps;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaContratosFacturas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigoGlosa);
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);						
	
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO DETALLE GLOSA------>>>>>>"+e);
			e.printStackTrace();
		}
		return resultados;
	}
	
	/**
	 * Metodo actualizar la glosa modificada
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static int guardar(Connection con, HashMap criterios, String bandera)
	{
		PreparedStatementDecorator ps;
				
		
		if(bandera.equals("UPDATE"))
		{
			try
			{
				ps= new PreparedStatementDecorator(con.prepareStatement(actualizarGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setString(1, UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechareg")+""));
				ps.setString(2, criterios.get("convenio")+"");
				ps.setString(3, criterios.get("glosaent")+"");
				ps.setString(4, UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechanot")+""));
				ps.setString(5, criterios.get("valor")+"");
				ps.setString(6, criterios.get("observ")+"");
				ps.setString(7, criterios.get("usuario")+"");
				ps.setString(8, criterios.get("usuario")+"");
				if(Utilidades.convertirAEntero(criterios.get("contrato")+"")>0)
					ps.setString(9, criterios.get("contrato")+"");
				else
					ps.setNull(9, java.sql.Types.INTEGER);
				ps.setString(10, criterios.get("codglosa")+"");
				
				logger.info("\n\nACTUALIZANDO GLOSA ----->>>>>>>>>>>"+actualizarGlosa);
				
				if(ps.executeUpdate()>0)
					return Utilidades.convertirAEntero(criterios.get("codglosa")+"");
				
			}
			catch (SQLException e)
			{
				logger.info("\n\nERROR. ACTUALIZANDO GLOSA MODIFICADA------>>>>>>"+e);
				e.printStackTrace();
			}
		}
		else if(bandera.equals("INSERT"))
		{
			try
			{
				ps= new PreparedStatementDecorator(con.prepareStatement(insertarNuevaGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	
								
				int valorseq=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_registro_glosas");
				ps.setInt(1, valorseq);
				ps.setString(2, criterios.get("consecutivo")+"");
				ps.setString(3, criterios.get("convenio")+"");
				ps.setString(4, criterios.get("glosaent")+"");
				ps.setString(5, UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechanot")+""));
				ps.setString(6, criterios.get("valor")+"");
				ps.setString(7, criterios.get("observ")+"");
				ps.setString(8, criterios.get("usuario")+"");
				ps.setString(9, criterios.get("usuario")+"");
				ps.setString(10, criterios.get("institucion")+"");
				logger.info("\n\nCONTRATO SQL >>>>>>>>>>>>"+criterios.get("contrato")+"");
				if(Utilidades.convertirAEntero(criterios.get("contrato")+"")>0)
					ps.setString(11, criterios.get("contrato")+"");
				else
					ps.setNull(11, java.sql.Types.INTEGER);
			
				logger.info("\n\nINSERTANDO NUEVA GLOSA----->>>>>>>>>>>"+insertarNuevaGlosa);
				
				if(ps.executeUpdate()>0)
					return valorseq;
				
			}
			catch (SQLException e)
			{
				logger.info("\n\nERROR. INSERTANDO NUEVA GLOSA------>>>>>>"+e);
				e.printStackTrace();
			}
		}
		return 0;
	}

	/**
	 * Metodo que consulta el detalle de la Glosa
	 * @param con
	 * @param criteriosDetalle
	 * @param forma
	 * @return
	 */
	public static HashMap consultarDetalleGlosa(Connection con,	int codGlosa, int institucion) 
	{
		PreparedStatementDecorator ps;
		HashMap<String, Object> resultados = new HashMap<String, Object>();
			
		consultaDetalleGlosas="SELECT rg.codigo AS codglosa, " +
									" ag.codigo AS codigoaudi, " +
									" ag.codigo_factura AS codfactura, " +
									" f.consecutivo_factura AS factura, " +
									" to_char(ag.fecha_elaboracion_fact, '"+ConstantesBD.formatoFechaBD+"') AS fechaelab, " +
									" f.numero_cuenta_cobro AS cuentacobro, " +
									" to_char(cc.fecha_radicacion, '"+ConstantesBD.formatoFechaBD+"') AS fecharadicacion, " +
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
									" ag.valor_glosa_factura AS valorglosa, " +
									" ag.fue_auditada AS fueauditada, " +
									"glosas.getconceptosaudiglosas(ag.codigo,?,?) AS conceptos, " +
									" f.tipo_factura_sistema AS tipoFactura " +
							"FROM auditorias_glosas ag " +
									"INNER JOIN registro_glosas rg ON (ag.glosa=rg.codigo) " +
									"INNER JOIN facturas f ON (ag.codigo_factura=f.codigo) " +
									"INNER JOIN cuentas_cobro cc ON (f.numero_cuenta_cobro=cc.numero_cuenta_cobro AND f.institucion=cc.institucion) " +
							"WHERE rg.codigo=? " +
									"AND (coalesce(f.valor_total,0)" +
									"+" +
									"coalesce(f.ajustes_debito,0)" +
										"-" +
										"coalesce(f.ajustes_credito,0)" +
											"+" +
											"coalesce(cc.ajuste_debito,0)" +
												"-" +
												"coalesce(cc.ajuste_credito,0)" +
													"-" +
													"coalesce(cc.pagos,0))>0 " +
									"AND f.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionFacturada+" " +
									"AND cc.estado="+ConstantesBD.codigoEstadoCarteraRadicada+" ";
				
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaDetalleGlosas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
			ps.setString(2, "-");
			ps.setInt(3, codGlosa);
						
			logger.info("\n\nCONSULTA DETALLE GLOSA ----->>>>>>>>>>>"+consultaDetalleGlosas);
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);						

		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO DETALLE GLOSA------>>>>>>"+e);
			e.printStackTrace();
		}
		return resultados;
	}
	
	/**
	 * Metodo que consulta el historico de factura en todas las glosas
	 * @param con
	 * @param codFactura
	 * @return
	 */
	public static HashMap accionHistoricoGlosa(Connection con, String codFactura)
	{
		HashMap<String, Object> resultados= new HashMap<String, Object>();
		PreparedStatementDecorator ps;
		
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaHistorico,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codFactura);
						
			logger.info("\n\nCONSULTA HISTORICO----->>>>>>>>>>>"+consultaHistorico.replace("?", codFactura));
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);						
			
			logger.info("\n\nMAPA RESULTADOS------>>>>>>>>>>>"+resultados);
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO HISTORICO------>>>>>>"+e);
			e.printStackTrace();
		}
		return resultados;
	}
	
	/**
	 * Metodo que consulta las fechas de las Glosas asociadas a una Factura
	 * @param con
	 * @param codFactura
	 * @return
	 */
	public static HashMap consultaFechaRadicacion(Connection con, String codFactura, int BD)
	{
		HashMap<String, Object> resultados= new HashMap<String, Object>();
		PreparedStatementDecorator ps;
		
		String consulta=consultaFechaRadicacion;
		
		if(BD == DaoFactory.ORACLE){
			consulta += ""+ValoresPorDefecto.getValorLimit1()+" 1 ";
			consulta += "ORDER BY fechaglosa DESC ";
		}
		else if(BD == DaoFactory.POSTGRESQL){
			consulta += "ORDER BY fechaglosa DESC ";
			consulta += ""+ValoresPorDefecto.getValorLimit1()+" 1 ";
		}
		
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaFechaRadicacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codFactura);
						
			logger.info("\n\nCONSULTA FECHA----->>>>>>>>>>>"+consulta.replace("?", codFactura));
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO FECHA------>>>>>>"+e);
			e.printStackTrace();
		}
		return resultados;
	}
	
	/**
	 * Metodo que consulta las fechas de Radicacion de las cuentas de cobro asociadas a una Factura
	 * @param con
	 * @param codFactura
	 * @return
	 */
	public static HashMap consultaFechaRadiCC(Connection con, String codFactura, String codGlosa, int BD)
	{
		HashMap<String, Object> resultados= new HashMap<String, Object>();
		PreparedStatementDecorator ps;
		
		String consulta=consultaFechaRadiCC;
		;
		
		if(BD == DaoFactory.ORACLE){
			consulta += ""+ValoresPorDefecto.getValorLimit1()+" 1 ORDER BY fechaRad DESC ";
		}
		else if(BD == DaoFactory.POSTGRESQL){
			consulta += "ORDER BY fechaRad DESC "+ValoresPorDefecto.getValorLimit1()+" 1 ";
		}
		
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codFactura);
						
			logger.info("\n\nCONSULTA FECHA CUENTA COBRO----->>>>>>>>>>>"+consulta);
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO FECHA CUENTA COBRO------>>>>>>"+e);
			e.printStackTrace();
		}
		return resultados;
	}
	
	/**
	 * Metodo que consulta Unica factura por consecutivo
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static HashMap consultarUnicaFactura(Connection con, HashMap criterios)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator ps;
		String cadena = consultaUnicaFactura;
		cadena += "AND f.convenio="+criterios.get("convenio")+" ";
		if(!(criterios.get("contrato")+"").equals(ConstantesBD.codigoNuncaValido))
			cadena += "AND sc.contrato="+criterios.get("contrato")+" "; 
		
		logger.info("\n\nCONSULTA UNICA FACTURA SQL----->>>>>>>>>>>"+cadena.replace("?", criterios.get("factura")+""));
		
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, criterios.get("factura")+"");
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), false, true);
			logger.info("\n\nunica facturaaa::::"+resultados);
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO UNICA FACTURA SQL------>>>>>>"+e);
			e.printStackTrace();
		}
		return resultados;
	}
	
	/**
	 * Metodo que guarda las facturas de la Glosa
	 * @param con
	 *c @param criterios
	 * @param operacion
	 * @return
	 */
	public static int guardarFacturas(Connection con, HashMap criterios, String operacion)
	{
		if(operacion.equals("INSERTAR"))
		{
			PreparedStatementDecorator ps;
			
			try
			{
				int valorseq=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_auditorias_glosas");
				
				ps= new PreparedStatementDecorator(con.prepareStatement(insertarFactura,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, valorseq);
				ps.setString(2, criterios.get("codigofactura")+"");
				ps.setString(3,UtilidadFecha.getHoraActual());
				ps.setString(4, criterios.get("usuario")+"");
				ps.setString(5, criterios.get("contrato")+"");
				ps.setString(6, criterios.get("valorglosafact")+"");
				ps.setString(7, criterios.get("saldofact")+"");
				ps.setString(8, UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaelab")+""));
				ps.setString(9, criterios.get("cuentacobro")+"");
				ps.setString(10, criterios.get("institucion")+"");
				ps.setString(11, UtilidadFecha.conversionFormatoFechaABD(criterios.get("fecharad")+""));
				ps.setString(12, criterios.get("glosa")+"");
							
				logger.info("\n\nINSERTAR FACTURA SQL----->>>>>>>>>>>"+insertarFactura);
				logger.info("\n\nMAPA CRITERIOS INSERTAR FACTURA GLOSA>>>>>>>>>>>"+criterios);
				logger.info("\n\nSECUENCIA DE INSERTAR FACTURA GLOSA>>>>>>>>>>>"+valorseq);
				
				boolean valorGlosa=actualizarValorGlosa(con, criterios);
				
				if(valorGlosa)
					if(ps.executeUpdate()>0)
						return valorseq;
			}
			catch (SQLException e)
			{
				logger.info("\n\nERROR. INSERTANDO FACTURA SQL------>>>>>>"+e);
				e.printStackTrace();
			}				
		}
		else if(operacion.equals("ACTUALIZAR"))
		{
			PreparedStatementDecorator ps;
			
			try
			{
				ps= new PreparedStatementDecorator(con.prepareStatement(actualizarFactura,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				logger.info("\n\nVALOR GLOSA------>"+criterios.get("valorglosafact"));
				
				if(!(criterios.get("valorglosafact")+"").equals(""))
					ps.setString(1, criterios.get("valorglosafact")+"");
				else
					ps.setNull(1, java.sql.Types.INTEGER);
				
				ps.setString(2, criterios.get("codigoaudi")+"");
								
				logger.info("\n\nACTUALIZAR FACTURA SQL----->>>>>>>>>>>"+actualizarFactura);
				
				boolean valorGlosa=actualizarValorGlosa(con, criterios);
				
				if(valorGlosa)
					if(ps.executeUpdate()>0)
						return Utilidades.convertirAEntero(criterios.get("codigoaudi")+"");
			}
			catch (SQLException e)
			{
				logger.info("\n\nERROR. ACTUALIZANDO FACTURA SQL------>>>>>>"+e);
				e.printStackTrace();
			}
		}	
		return -1;
	}
	
	/**
	 * Metodo que actualiza el valor de la Glosa
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static boolean actualizarValorGlosa(Connection con, HashMap criterios)
	{
		PreparedStatementDecorator ps;
			try
			{	
				ps= new PreparedStatementDecorator(con.prepareStatement(actualizarValorGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setString(1, criterios.get("valorglosa")+"");
				ps.setString(2, criterios.get("glosa")+"");
								
				if(ps.executeUpdate()>0)
					return true;
			}
			catch (SQLException e)
			{
				logger.info("\n\nERROR. ACTUALIZANDO VALOR GLOSA SQL------>>>>>>"+e);
				e.printStackTrace();
			}
		return false;
	}
	
	/**
	 * Metodo que guarda los conceptos de la Auditoria
	 * @param con
	 * @param criterios
	 * @param operacion
	 * @return
	 */
	public static boolean guardarConceptoFactura(Connection con, HashMap criterios)
	{
		logger.info("\n\nMAPA CRITERIOS DE GUARDAR CONCEPTO AUDI>>>>>>>>>"+criterios);		
		{
			PreparedStatementDecorator ps;	
			
			try
			{
				int valorseq=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_conceptos_audi_glosas");
				
				ps= new PreparedStatementDecorator(con.prepareStatement(insertarConceptoFactura,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, valorseq);
				ps.setString(2, criterios.get("codigoaudi")+"");
				ps.setString(3, criterios.get("codconcepto")+"");
				ps.setString(4, criterios.get("institucion")+"");				
							
				logger.info("\n\nINSERTAR CONCEPTO FACTURA SQL----->>>>>>>>>>>"+insertarConceptoFactura);
				
				if(ps.executeUpdate()>0)
					return true;
			}
			catch (SQLException e)
			{
				logger.info("\n\nERROR. INSERTANDO CONCEPTO FACTURA SQL------>>>>>>"+e);
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param codigo
	 * @return
	 */
	public static String obtenerFechaAprobacionGlosa(String codigo)
	{
		String consulta="select to_char(fecha_aprobacion, 'DD/MM/YYYY')||'' from glosas.registro_glosas where codigo="+codigo;
		String resultado="";
		try
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("\n\nobtenerFechaAprobacionGlosa----->>>>>>>>>>>"+consulta);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				resultado= rs.getString(1)+"";
			UtilidadBD.closeConnection(con);
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. consultando la fecha de aprob------>>>>>>"+e);
			e.printStackTrace();
		}
		return resultado;
	}


	public static ArrayList<DtoObsFacturaGlosas> consultarObsAuditoriaGlosa(Connection con, int codigoFactura) 
	{
		ArrayList<DtoObsFacturaGlosas> observaciones=new ArrayList<DtoObsFacturaGlosas>();
		String consulta="select " +
								" codigo," +
								" codigo_factura as codigofactura," +
								" observacion," +
								" to_char(fecha_observacion,'dd/mm/yyyy') as fecha," +
								" hora_observacion as hora," +
								" usuario_observacion as usuario," +
								" getnombreusuario(usuario_observacion) as nomusuario " +
						" from glosas.obs_factura_glosas where codigo_factura=?";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoFactura);
			logger.info(ps);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoObsFacturaGlosas obs=new DtoObsFacturaGlosas();
				obs.setCodigo(rs.getBigDecimal("codigo"));
				obs.setCodigoFactura(rs.getInt("codigofactura"));
				obs.setFechaObservacion(rs.getString("fecha"));
				obs.setHoraObservacion(rs.getString("hora"));
				obs.setUsuarioObservacion(rs.getString("usuario"));
				obs.setNombreUsuario(rs.getString("nomusuario"));
				obs.setObservacion(rs.getString("observacion"));
				observaciones.add(obs);
			}
			rs.close();
			ps.close();
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. consultando la fecha de aprob------>>>>>>"+e);
			e.printStackTrace();
		}
		return observaciones;
	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean insertarObservacion(Connection con,DtoObsFacturaGlosas dto)
	{
		PreparedStatementDecorator ps;	
		String cadena="insert into glosas.obs_factura_glosas (codigo,codigo_factura,observacion,fecha_observacion,hora_observacion,usuario_observacion) values(?,?,?,?,?,?)";
		try
		{
			int valorseq=UtilidadBD.obtenerSiguienteValorSecuencia(con, "glosas.seq_obsfacglosa");
			
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setBigDecimal(1, new BigDecimal(valorseq));
			ps.setInt(2, dto.getCodigoFactura());
			ps.setString(3, dto.getObservacion());
			ps.setString(4, UtilidadFecha.conversionFormatoFechaABD(dto.getFechaObservacion()));
			ps.setString(5, dto.getHoraObservacion());
			ps.setString(6, dto.getUsuarioObservacion());
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return true;
			}
			ps.close();
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. INSERTANDO CONCEPTO FACTURA SQL------>>>>>>"+e);
			e.printStackTrace();
		}
		return false;
	}


	/**
	 * 
	 * @param con
	 * @param codigoGlosa
	 * @return
	 */
	public static String consultarEstadoGlosa(Connection con, String codigoGlosa) 
	{
		String consulta=" select estado from registro_glosas  where codigo="+codigoGlosa;
		String resultado="";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			logger.info(ps);
			if(rs.next())
				resultado= rs.getString(1)+"";
			rs.close();
			ps.close();
		}
		catch (SQLException e)
		{
			logger.info("error",e);
		}
		return resultado;
	}
}