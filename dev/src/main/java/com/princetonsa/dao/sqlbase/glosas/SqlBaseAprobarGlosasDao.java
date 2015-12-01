package com.princetonsa.dao.sqlbase.glosas;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

public class SqlBaseAprobarGlosasDao
{	
	/**
	 * Para el manejo de logs
	 */
	private static Logger logger = Logger.getLogger(SqlBaseAprobarGlosasDao.class);
	
	/**
	 * Consulta de convenios
	 */
	private static String consultaConvenios="SELECT c.codigo, c.nombre FROM convenios c ORDER BY c.nombre ";
	
	/**
	 * Consultar Auditoria de la Glosa
	 */
	private static String auditoriaGlosas= "SELECT ag.codigo AS codauditoria, ag.valor_glosa_factura AS valorfactura FROM auditorias_glosas ag WHERE ag.glosa=? ";
	
	/**
	 * Consulta para evaluar concordancia de valor glosa
	 */
	private static String consultaAudiSum="SELECT sum(dag.valor_glosa) AS sumaglosa FROM det_auditorias_glosas dag WHERE dag.auditoria_glosa=? ";
		
	/**
	 * Consulta para Actualizar la Glosa por Aprobacion
	 */
	private static String actualizarAprobarGlosa="UPDATE registro_glosas " +
											"SET fecha_aprobacion=CURRENT_DATE, " +
											"estado='"+ConstantesIntegridadDominio.acronimoEstadoGlosaAprobada+"', " +
											"hora_aprobacion="+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
											"usuario_aprobacion=? " +
											"WHERE glosa_sistema=? ";	
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
			logger.info("\n ERROR. CONSULTANDO CONVENIOS... APROBAR GLOSAS--->>> "+e);
		}
		return resultados;
	}
	
	/**
	 * Metodo para validar la glosa por factura, que tenga detalle
	 * y que la suma de sus detalles sea igual a su valor
	 * @param con
	 * @param codGlosa
	 * @param valor
	 * @return
	 */
	public static boolean validarAprobarGlosa(Connection con, String codGlosa, String valor)
	{  
		HashMap<String, Object> auditoria = new HashMap<String, Object>();
		auditoria.put("numRegistros", 0);
		PreparedStatementDecorator ps;
		ResultSetDecorator rs;
		String suma="",cont="";
					
		try 
		{	
			ps =  new PreparedStatementDecorator(con.prepareStatement(auditoriaGlosas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codGlosa);
			auditoria = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
		} 
		catch (SQLException e) 
		{
			logger.info("\n ERROR. CONSULTANDO AUDITORIA GLOSAS--->>> "+e);
		}
		
		logger.info("\n\nAUDITORIA------>"+auditoria);
		
		for (int i=0; i<Utilidades.convertirAEntero(auditoria.get("numRegistros").toString()); i++)
		{
			try 
			{
				ps =  new PreparedStatementDecorator(con.prepareStatement(consultaAudiSum,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setString(1, auditoria.get("codauditoria_"+i)+"");
				rs =new ResultSetDecorator(ps.executeQuery());
				logger.info("\n\nconsulta consultaAudiSum--->"+consultaAudiSum);
				if(rs.next())
				{
					if(rs.getDouble("sumaglosa") != Utilidades.convertirADouble(auditoria.get("valorfactura_"+i).toString())){
						return false;
					}					
				} 
				else 
					return false;
			}	
			catch (SQLException e) 
			{
				logger.info("\n ERROR. CONSULTANDO DETALLE AUDITORIA GLOSAS--->>> "+e);
			}
		}	
		return true;
	}
	
	/**
	 * Metodo actualizar la Glosa
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static boolean guardar(Connection con, HashMap criterios)
	{
		logger.info("\n\nMAPA CRITERIOS SQLBASE GUARDAR>>>>>>>>>>>>>>>>>"+criterios);
		PreparedStatementDecorator ps;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(actualizarAprobarGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setString(1, criterios.get("usuario")+"");
			ps.setString(2, criterios.get("codGlosa")+"");
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. ACTUALIZANDO GLOSA A APROBAR------>>>>>>"+e);
			e.printStackTrace();
		}		
		return false;
	}
}