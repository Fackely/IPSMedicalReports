package com.princetonsa.dao.sqlbase.glosas;

import java.util.HashMap;
import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;


public class SqlBaseAprobarAnularRespuestasDao
{
	/**
	 * Para el manejo de logs
	 */
	private static Logger logger = Logger.getLogger(SqlBaseAprobarAnularRespuestasDao.class);
	
	/**
	 * Cadena que realiza actualizacion de una factura
	 */
	private static String actualizarRespuesta="UPDATE respuesta_glosa " +
												"SET estado=?, " +
												"motivo_aprob_anul=?, " +
												"fecha_aprob_anul=CURRENT_DATE, " +
												"usuario_aprob_anul=?, " +
												"fecha_modifica=CURRENT_DATE, " +
												"hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
												"usuario_modifica=? " +
												"WHERE codigo=? ";
	
	/**
	 * Cadena para consultar la facturas asociadas a la Glosa
	 */
	private static String consultaNumFacturasGlosa="SELECT codigo_factura AS codFactura " +
													"FROM auditorias_glosas " +
													"WHERE glosa=? ";
	/**
	 * Cadena para consultar las facturas de la Respuesta
	 */
	private static String consultaFacturasRespuesta="SELECT codigo AS codigo, " +
														"factura AS codFactura, " +
														"factura_sistema AS facinterna " +
														"FROM fact_respuesta_glosa " +
														"WHERE respuesta_glosa=? ";
	
	
	/**
	 * Cadena para consultar el campo conciliacion de la respuesta
	 */
	private static String consultaConciliar="SELECT conciliacion AS conciliar " +
												"FROM respuesta_glosa " +
												"WHERE codigo=? ";
	
	/**
	 * Cadena para actualizar el estado de la Glosa
	 */
	private static String actualizarEstadoGlosa="UPDATE registro_glosas " +
													"SET estado=? " +
													"WHERE codigo=? ";
	
	/**
	 * Cadena para Consultar las facturas correspondientes a la Glosa
	 */
	private static String consultaFacturasGlosa="SELECT codigo AS codaudi, " +
											"codigo_factura As codfactura " +
											"FROM auditorias_glosas " +
											"WHERE glosa=? ";
	
	/**
	 * Cadena que consulta todos los det_audi_glosas por el codigo de la auditoria
	 */
	private static String consultaDetalleFacturaGlosa="SELECT codigo AS detAudiFact, " +
														"auditoria_glosa AS codaudi " +
														"FROM det_auditorias_glosas " +
														"WHERE auditoria_glosa=? "; 
	
	/**
	 * Cadena que consulta los conceptos por detalle factura glosa
	 */
	private static String consultaConceptosDetalleFacturaGlosa="SELECT count(*) AS numReg FROM conceptos_det_audi_glosas WHERE det_audi_fact=? ";
	
	/**
	 * Cadena que consulta los asocios por solicitud detalle factura glosa
	 */
	private static String consultaAsociosSolicitudDetalleFacturaGlosa="SELECT codigo AS codasocio, " +
																		"asocio_det_factura AS asociodet " +
																		"FROM asocios_auditorias_glosas " +
																		"WHERE det_auditoria_glosa=? ";
	
	/**
	 * Cadena que consulta los conceptos por asocios solicitud detalle factura glosa
	 */
	private static String consultaConceptosAsociosSolicitudDetalleFacturaGlosa="SELECT count(*) AS numReg FROM conceptos_aso_audi_glosas WHERE asocio_auditoria_glosa=? ";
	
	/**
	 * Cadena para consultar los conceptos del detalle de las facturas de la respuesta de glosa
	 */
	private static String consultarConceptosDetFacRespGlosa="SELECT count(*) AS numReg FROM det_fact_resp_glosa WHERE det_auditoria_glosa=? ";
	
	/**
	 * Cadena para consultar los conceptos de asocios de la respuesta de glosa
	 */
	private static String consultarConceptosAsociosRespGlosa="SELECT count(*) AS numReg FROM asocios_resp_glosa WHERE asocio_auditoria_glosa=? ";
	
	
	/**
	 * Cadena para consultar el estado de los ajustes relacionados con la Repuesta de Glosa para Factura Externa
	 */
	private static String consultarEstadoAjustesFacExterna="SELECT ae.codigo AS codigo, " +
													"ae.estado AS estado " +
													"FROM ajustes_empresa ae " +
														"INNER JOIN  det_fact_ext_resp_glosa dfe ON(dfe.ajuste=ae.codigo) " +
													"WHERE fact_respuesta_glosa=? ";
	
	/**
	 * Cadena para consultar el estado de los ajustes relacionados con la Repuesta de Glosa para el detalle Factura Interna
	 */
	private static String consultarEstadoAjustesDetFacInterna="SELECT ae.codigo AS codigo, " +
																"ae.estado AS estado " +
																"FROM ajustes_empresa ae " +
																	"INNER JOIN  det_fact_resp_glosa dfr ON(dfr.ajuste=ae.codigo) " +
																"WHERE fact_respuesta_glosa=? ";
	
	/**
	 * Cadena para consultar el estado de los ajustes relacionados con la Repuesta de Glosa para asocios de Factura Interna
	 */
	private static String consultarEstadoAjustesAsoFacInterna="SELECT ae.codigo AS codigo, " +
																"ae.estado As estado " +
																"FROM ajustes_empresa ae " +
																	"INNER JOIN  asocios_resp_glosa arg ON(arg.ajuste=ae.codigo) " +
																"WHERE det_fact_resp_glosa=? ";
	
	
	/**
	 * Metodo para consultar el estado de los ajustes de asocios de facturas internas
	 * @param con
	 * @param codDetFactRespGlosa
	 * @return
	 */
	public static HashMap consultarEstadoAjustesAsoFacInterna(Connection con, String codDetFactRespGlosa)	
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator ps;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultarEstadoAjustesAsoFacInterna,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codDetFactRespGlosa);			
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO ESTADO AJUSTES ASOCIOS FACTURA INTERNA------>>>>>>"+e);
			e.printStackTrace();
		}		
		return resultados;	
	}
	
	/**
	 * Metodo para consultar el estado de los ajustes de solicitudes de facturas internas
	 * @param con
	 * @param codFactRespGlosa
	 * @return
	 */
	public static HashMap consultarEstadoAjustesDetFacInterna(Connection con, String codFactRespGlosa)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator ps;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultarEstadoAjustesDetFacInterna,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codFactRespGlosa);			
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO ESTADO AJUSTES SOLICITUDES FACTURA INTERNA------>>>>>>"+e);
			e.printStackTrace();
		}		
		return resultados;	
	}
	
	/**
	 * Metodo para consultar el estado de los ajustes de facturas externas
	 * @param con
	 * @param codFactRespGlosa
	 * @return
	 */
	public static HashMap consultarEstadoAjustesFacExterna(Connection con, String codFactRespGlosa)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator ps;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultarEstadoAjustesFacExterna,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codFactRespGlosa);			
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO ESTADO AJUSTES FACTURA EXTERNA------>>>>>>"+e);
			e.printStackTrace();
		}		
		return resultados;	
	}
	
	/**
	 * Metodo para consultar los conceptos de asocios de la respuesta de glosa
	 * @param con
	 * @param codasocio
	 * @return
	 */
	public static int consultarConceptosAsociosRespGlosa(Connection con, String codasocio)
	{
		PreparedStatementDecorator ps;
		int resultado= 0;
		ResultSetDecorator rs;
		
		try 
		{	
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultarConceptosDetFacRespGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setString(1, codasocio);
			
			rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next()){
				resultado = rs.getInt("numReg");
			}
			
		} 
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO NUM CONCEPTOS ASOCIOSN DET FACTURA RESPUESTA GLOSA------>>>>>>"+e);
			e.printStackTrace();
		}
		return resultado;	
	}
	
	/**
	 * Metodo para consultar los conceptos del detalle de las facturas de la respuesta de glosa
	 * @param con
	 * @param detAudiFact
	 * @return
	 */
	public static int consultarConceptosDetFacRespGlosa(Connection con, String detAudiFact)
	{
		PreparedStatementDecorator ps;
		int resultado= 0;
		ResultSetDecorator rs;
		
		try 
		{	
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultarConceptosDetFacRespGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setString(1, detAudiFact);
			
			rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next()){
				resultado = rs.getInt("numReg");
			}
			
		} 
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO NUM CONCEPTOS DET FACTURA RESPUESTA GLOSA------>>>>>>"+e);
			e.printStackTrace();
		}
		return resultado;	
	}
	
	/**
	 * Cadena que consulta los conceptos por asocios solicitud detalle factura glosa
	 * @param con
	 * @param detAudiFact
	 * @return
	 */
	public static int consultaConceptosAsociosSolicitudDetalleFacturaGlosa(Connection con, String codAsocio)
	{
		PreparedStatementDecorator ps;
		int resultado= 0;
		ResultSetDecorator rs;
		
		try 
		{	
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultaConceptosAsociosSolicitudDetalleFacturaGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setString(1, codAsocio);
			
			rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next()){
				resultado = rs.getInt("numReg");
			}
			
		} 
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO NUM CONCEPTOS ASOCIOS DET FACTURA GLOSA------>>>>>>"+e);
			e.printStackTrace();
		}
		return resultado;	
	}
	
	/**
	 * Cadena que consulta los asocios por solicitud detalle factura glosa
	 * @param con
	 * @param detAudiFact
	 * @return
	 */
	public static HashMap consultaAsociosSolicitudDetalleFacturaGlosa(Connection con, String detAudiFact)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator ps;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaAsociosSolicitudDetalleFacturaGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, detAudiFact);			
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO ASOCIOS POR SOLICITUD DETALLE FACTURA GLOSA------>>>>>>"+e);
			e.printStackTrace();
		}		
		return resultados;	
	}
	
	/**
	 * Metodo que consulta los conceptos por detalle factura glosa
	 * @param con
	 * @param detAudiFact
	 * @return
	 */
	public static int consultaConceptosDetalleFacturaGlosa(Connection con, String detAudiFact)
	{
		PreparedStatementDecorator ps;
		int resultado= 0;
		ResultSetDecorator rs;
		
		try 
		{	
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultaConceptosDetalleFacturaGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setString(1, detAudiFact);
			
			rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next()){
				resultado = rs.getInt("numReg");
			}
			
		} 
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO NUM CONCEPTOS DET FACTURA GLOSA------>>>>>>"+e);
			e.printStackTrace();
		}
		return resultado;	
	}
	
	/**
	 * Metodo que consulta todos los det_audi_glosas por el codigo de la auditoria
	 * @param con
	 * @param codGlosa
	 * @return
	 */
	public static HashMap consultaDetalleFacturaGlosa(Connection con, String codAudi)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator ps;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaDetalleFacturaGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codAudi);			
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO DETALLE FACTURA GLOSA------>>>>>>"+e);
			e.printStackTrace();
		}		
		return resultados;	
	}
	
	/**
	 * Metodo para Consultar las facturas correspondientes a la Glosa
	 * @param con
	 * @param codGlosa
	 * @return
	 */
	public static HashMap consultaFacturasGlosa(Connection con, String codGlosa)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator ps;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaFacturasGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codGlosa);			
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO FACTURAS GLOSA------>>>>>>"+e);
			e.printStackTrace();
		}		
		return resultados;	
	}
	
	/**
	 * Metodo que actualiza la Glosa y la Repuesta segun estado 
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static boolean guardar(Connection con, HashMap criterios)
	{
		PreparedStatementDecorator ps;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(actualizarRespuesta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			if((criterios.get("check")+"").equals("APR"))
			{
				ps.setString(1, ConstantesIntegridadDominio.acronimoEstadoGlosaAprobada);
				ps.setNull(2, Types.VARCHAR);
			}
			else if((criterios.get("check")+"").equals("ANU"))
			{
				ps.setString(1, ConstantesIntegridadDominio.acronimoEstadoGlosaAnulada);
				ps.setString(2, criterios.get("motivo")+"");
			}
			ps.setString(3, criterios.get("usuario")+"");
			ps.setString(4, criterios.get("usuario")+"");
			ps.setString(5, criterios.get("codrespuesta")+"");			
			
			
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. ACTUALIZANDO RESPUESTA SQL------>>>>>>"+e);
			e.printStackTrace();
		}		
		return false;
	}
	
	/**
	 * Metodo que consulta el campo conciliacion de la respuesta
	 * @param con
	 * @param codrespuesta
	 * @return
	 */
	public static String consultarConciliar(Connection con, String codrespuesta)
	{
		PreparedStatementDecorator ps;
		ResultSetDecorator rs;
		String conciliar="";
		try
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultaConciliar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setString(1, codrespuesta);
			
			rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next()){
				conciliar = rs.getString("conciliar");
			}
		}
		catch(SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO CONCILIAR RESPUESTA SQL------>>>>>>"+e);
			e.printStackTrace();
		}
		return conciliar;
	}
	
	/**
	 * Metodo que actualiza el estado de la Glosa
	 * @param con
	 * @param codrespuesta
	 * @param conciliar
	 * @return
	 */
	public static boolean guardarEstadoGlosa(Connection con, String codglosa, String conciliar, String estadoResp)
	{
		PreparedStatementDecorator ps;
		try
		{
			if(estadoResp.equals("APR"))
			{
				ps= new PreparedStatementDecorator(con.prepareStatement(actualizarEstadoGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
				if(conciliar.equals("S"))
					ps.setString(1, ConstantesIntegridadDominio.acronimoEstadoGlosaConciliada);
				else if(conciliar.equals("N"))
					ps.setString(1, ConstantesIntegridadDominio.acronimoEstadoGlosaRespondida);
				ps.setString(2, codglosa);
			
			
			if(ps.executeUpdate()>0)
				return true;
			}
			else
				return true;
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. ACTUALIZANDO ESTADO GLOSA SQL------>>>>>>"+e);
			e.printStackTrace();
		}		
		return false;
	}
	
	/**
	 * Metodo que consulta todas las facturas de la Glosa
	 * @param con
	 * @param codglosa
	 * @return
	 */
	public static HashMap consultaNumFacturasGlosa(Connection con, String codglosa)
	{		
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator ps;
		
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaNumFacturasGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codglosa);			
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO FACTURAS GLOSA------>>>>>>"+e);
			e.printStackTrace();
		}		
		return resultados;	
	}
	
	/**
	 * Metodo que valida si las faturas de la Glosa corresponden tambien a la Respuesta 
	 * @param con
	 * @param codrespuesta
	 * @param codfactura
	 * @return
	 */
	public static HashMap validarRespuesta(Connection con, String codrespuesta)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator ps;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaFacturasRespuesta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codrespuesta);			
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO FACTURAS GLOSA------>>>>>>"+e);
			e.printStackTrace();
		}		
		return resultados;
				
	}
}