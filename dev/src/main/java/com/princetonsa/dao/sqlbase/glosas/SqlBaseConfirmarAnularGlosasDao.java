package com.princetonsa.dao.sqlbase.glosas;

import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;


public class SqlBaseConfirmarAnularGlosasDao
{
	/**
	 * Para el manejo de logs
	 */
	private static Logger logger = Logger.getLogger(SqlBaseConfirmarAnularGlosasDao.class);
	
	/**
	 * Consulta de convenios
	 */
	private static String consultaConvenios="SELECT c.codigo, c.nombre FROM convenios c ORDER BY c.nombre ";
	
	/**
	 * Consulta para evaluar concordancia de valor glosa
	 */
	private static String consultaGlosaSum="SELECT sum(ag.valor_glosa_factura) AS sumaglosa FROM auditorias_glosas ag WHERE ag.glosa=? ";
	
	/**
	 * Consulta si la glosa tiene detalle
	 */
	private static String consultaDetalleGlosa="SELECT count(*) AS numreg FROM auditorias_glosas ag WHERE ag.glosa=? ";
	
	/**
	 * Consulta para Actualizar la Glosa por Confirmacion
	 */
	private static String actualizarConfirmarGlosa="UPDATE registro_glosas " +
											"SET fecha_confirmacion=CURRENT_DATE, " +
											"estado='"+ConstantesIntegridadDominio.acronimoEstadoGlosaConfirmada+"', " +
											"hora_confirmacion="+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
											"usuario_confirmacion=? " +
											"WHERE codigo=? ";
	
	/**
	 * Consulta para Actualizar la Glosa por Anular
	 */
	private static String actualizarAnularGlosa="UPDATE registro_glosas " +
											"SET fecha_anulacion=CURRENT_DATE, " +
											"estado='"+ConstantesIntegridadDominio.acronimoEstadoGlosaAnulada+"', " +
											"hora_anulacion="+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
											"usuario_anulacion=?, " +
											"motivo_anulacion=? " +
											"WHERE codigo=? ";
	
	/**
	 * Cadena para Consultar las faturas correspondientes a la Glosa
	 */
	private static String consultaFacturasGlosa="SELECT codigo AS codaudi, " +
											"codigo_factura As codfactura " +
											"FROM auditorias_glosas " +
											"WHERE glosa=? ";
	
	/**
	 * Cadena que consulta el numero de soliditudes asociadas a una factura de la Glosa
	 */
	private static String consultaDetalleFacturaGlosa="SELECT count(codigo) AS codsol " +														
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
	private static String consultaConceptosAsociosSolicitudDetalleFacturaGlosa="SELECT count(*) AS numReg FROM conceptos_aso_audi_glosas WHERE asocio_auditoria_glosa ";
	

	/**
	 * Metodo para Consultar las solicitudes asociadas a una factura de la Glosa
	 * @param connection
	 * @return
	 */
	public static int consultaDetalleFacturaGlosa(Connection con, String codFac)
	{
		PreparedStatementDecorator ps;
		ResultSetDecorator rs;
		int cont=0;
		try 
		{	
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultaDetalleFacturaGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			logger.info("\n\nCONSULTA DETALLE FACTURA GLOSA--->"+consultaDetalleFacturaGlosa.replace("?", codFac));
			
			ps.setString(1, codFac);
			
			rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next()){
				cont = rs.getInt("codsol");
			}
			
		} 
		catch (SQLException e) 
		{
			logger.info("\n ERROR. CONSULTANDO DETALLE FACTURA GLOSA--->>> "+e);
		}
		return cont;
	}
	
	/**
	 * Metodo para Consultar las facturas correspondientes a la Glosa
	 * @param connection
	 * @return
	 */
	public static HashMap consultarFacturasGlosa(Connection con, String codGlosa)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator ps;
						
		try 
		{	
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultaFacturasGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setString(1, codGlosa);
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			
			logger.info("\n\nCONSULTA FACTURAS GLOSA------>"+consultaFacturasGlosa.replace("?", codGlosa));
		} 
		catch (SQLException e) 
		{
			logger.info("\n ERROR. CONSULTANDO DETALLE GLOSAS ... BUSQUEDA GLOSAS--->>> "+e);
		}
		return resultados;
	}
	
	/**
	 * Metodo que consulta los Convenios
	 * @param connection
	 * @return
	 */
	public static HashMap consultarConvenios (Connection connection)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator ps;
						
		try 
		{	
			ps =  new PreparedStatementDecorator(connection.prepareStatement(consultaConvenios,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			
		} 
		catch (SQLException e) 
		{
			logger.info("\n ERROR. CONSULTANDO GLOSAS ... BUSQUEDA GLOSAS--->>> "+e);
		}
		return resultados;
	}
	
	/**
	 * Metodo para validar la glosa, que tenga detalle
	 * y que la suma de sus detalles sea igual a su valor
	 * @param con
	 * @param codGlosa
	 * @param valor
	 * @return
	 */
	public static boolean validarAnuConfGlosa(Connection con, String codGlosa, String valor)
	{
		PreparedStatementDecorator ps,ps1;
		ResultSetDecorator rs,rs1;
		String suma="",cont="";
		try 
		{	
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultaGlosaSum,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setString(1, codGlosa);
			
			rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next()){
				suma = rs.getString("sumaglosa");
			}
			
		} 
		catch (SQLException e) 
		{
			logger.info("\n ERROR. VALIDANDO GLOSAS SUM--->>> "+e);
		}
		try 
		{	
			ps1 =  new PreparedStatementDecorator(con.prepareStatement(consultaDetalleGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps1.setString(1, codGlosa);
			
			rs1=  new ResultSetDecorator(ps1.executeQuery());
			
			if(rs1.next()){
				cont = rs1.getString("numreg");
			}
			
		} 
		catch (SQLException e) 
		{
			logger.info("\n ERROR. VALIDANDO GLOSAS DETALLE--->>> "+e);
		}
		if(Utilidades.convertirAEntero(suma)==Utilidades.convertirAEntero(valor) && Utilidades.convertirAEntero(cont)>0)
			return true;
		return false;
	}
	
	/**
	 * Metodo actualizar la glosa segun estado
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static boolean guardar(Connection con, HashMap criterios)
	{
		logger.info("\n\nMAPA CRITERIOS SQLBASE GUARDAR>>>>>>>>>>>>>>>>>"+criterios);
		PreparedStatementDecorator ps;
		if(criterios.get("anularConfirmar").toString().equals("CONF"))
		{
			try
			{
				ps= new PreparedStatementDecorator(con.prepareStatement(actualizarConfirmarGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
				ps.setString(1, criterios.get("usuario")+"");
				ps.setString(2, criterios.get("codGlosa")+"");
				
				if(ps.executeUpdate()>0)
					return true;
				
			}
			catch (SQLException e)
			{
				logger.info("\n\nERROR. ACTUALIZANDO GLOSA A CONFIRMAR------>>>>>>"+e);
				e.printStackTrace();
			}
		}
		else
		{
			try
			{
				ps= new PreparedStatementDecorator(con.prepareStatement(actualizarAnularGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
				ps.setString(1, criterios.get("usuario")+"");
				ps.setString(2, criterios.get("motivo")+"");
				ps.setString(3, criterios.get("codGlosa")+"");
				
				if(ps.executeUpdate()>0)
					return true;
				
			}
			catch (SQLException e)
			{
				logger.info("\n\nERROR. ACTUALIZANDO GLOSA A ANULAR------>>>>>>"+e);
				e.printStackTrace();
			}
		}
		return false;
	}
}