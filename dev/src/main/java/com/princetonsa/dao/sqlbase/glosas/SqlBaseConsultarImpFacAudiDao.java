package com.princetonsa.dao.sqlbase.glosas;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;

public class SqlBaseConsultarImpFacAudiDao
{
	/**
	 * Para el manejo de logs
	 */
	private static Logger logger = Logger.getLogger(SqlBaseConfirmarAnularGlosasDao.class);
	
	/**
	 * Consulta Convenios
	 */
	public static String consultaConvenios="";
		
	/**
	 * Metodo encargado de consultar las facturas auditadas
	 * @param connection
	 * @param consulta --> consulta a realizar
	 * @return HashMap
	 * -----------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -----------------------------
	 * codigoGlosa_,fechaAuditoriaBd_,
	 * fechaAuditoria_,nombreGonvenio_,
	 * preGlosa_, valorPreGlosa_,
	 * fechaElaboracionBd_,fechaElaboracion_,
	 * consecutivoFactura_, nombrePaciente_,
	 * valorTotal_
	 */
	public static HashMap consultaFacturasAuditadas (Connection connection, String consulta)
	{
		logger.info("\n entro a  consultaFacturasAuditadas  consulta-->"+consulta);
		HashMap result = new HashMap ();
		result.put("numRegistros", 0); 
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			result=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true); 
		} 
		catch (SQLException e)
		{
			logger.info("\n problema en  consultaFacturasAuditadas -->"+e);
		}
		return result;
	}
	
	/**
	 * Variable para la consulta del detalle de las Facturas
	 */
	public static String consultaDetalleFacturas="SELECT DISTINCT getnombreconvenio(f.convenio) AS nomconvenio, " +
			"f.consecutivo_factura AS codfactura, " +
			"ag.valor_glosa_factura AS valorglosafactura, " +
			"getconsecutivosolicitud(dag.solicitud) AS solicitud, " +
			"CASE WHEN dag.servicio IS NULL THEN dag.articulo || ' ' || getdescripcionarticulo(dag.articulo) " +
				"ELSE dag.servicio || ' ' || getnombreservicio(dag.servicio,"+ConstantesBD.codigoTarifarioCups+") " +
			"END AS servicio, " +
			"dfs.cantidad_cargo AS cantidadsol, " +
			"((coalesce(f.valor_total,0)-coalesce(f.ajustes_credito,0))+coalesce(f.ajustes_debito,0)) AS valorfactura, " +
			"dag.cantidad_glosa AS cantidadglosa, " +
			"dag.valor_glosa AS valorglosa, " +
			"getconceptosdetaudiglosas(cast(dag.codigo as INTEGER)) AS concepto, " +
			"rg.glosa_sistema AS preglosa, " +
			"rg.estado AS estado " +
		"FROM facturas f " +
			"INNER JOIN auditorias_glosas ag ON (f.codigo=ag.codigo_factura) " +
			"INNER JOIN registro_glosas rg ON (ag.glosa=rg.codigo) " +
			"INNER JOIN det_auditorias_glosas dag ON (dag.auditoria_glosa=ag.codigo) " +
			"INNER JOIN det_factura_solicitud dfs ON (dfs.factura=f.codigo) " +
		"WHERE rg.codigo=? ORDER BY getconsecutivosolicitud(dag.solicitud) ";
	
	/**
	 * Indices de la consulta del detalle de las Facturas
	 */
	private static String [] indicesDetFacturas ={"nomconvenio_","codfactura_","valorglosafactura_","solicitud_","servicio_","cantidadsol_","valorfactura_","cantidadglosa_","valorglosa_","concepto_"};
	
	/**
	 * Actualiza Log de consulta
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static boolean guardar(Connection con, HashMap criterios, String guardarLog)
	{
		logger.info("\n\nMAPA CRITERIOS SQLBASE GUARDAR LOG>>>>>>>>>>>>>>>>>"+criterios);
		PreparedStatementDecorator ps;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(guardarLog,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setString(1, criterios.get("usuario")+"");
			ps.setString(2, criterios.get("reporte")+"");
			ps.setString(3, criterios.get("tipoSalida")+"");
			ps.setString(4, criterios.get("criteriosConsulta")+"");
			ps.setString(5, criterios.get("ruta")+"");
				
			if(ps.executeUpdate()>0)
				return true;
				
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. ACTUALIZANDO LOG DE CONSULTA------>>>>>>"+e);
			e.printStackTrace();
		}
		return false;
	}
	
	public static HashMap consultarFactura(Connection con, String consFactura)
	{
		PreparedStatementDecorator ps;
		
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		
		logger.info("\n\nCONSECUTIVO FACTURA----->>>>>>>>>>>"+consFactura);
		
		consultaDetalleFacturas="SELECT DISTINCT getnombreconvenio(f.convenio) AS nomconvenio, " +
										"f.consecutivo_factura AS codfactura, " +
										"ag.valor_glosa_factura AS valorglosafactura, " +
										"getconsecutivosolicitud(dag.solicitud) AS solicitud, " +
										"CASE WHEN dag.servicio IS NULL THEN dag.articulo || ' ' || getdescripcionarticulo(dag.articulo) " +
											"ELSE dag.servicio || ' ' || getnombreservicio(dag.servicio,"+ConstantesBD.codigoTarifarioCups+") " +
										"END AS servicio, " +
										"dag.cantidad_glosa AS cantidadglosa, " +
										"dag.valor_glosa AS valorglosa, " +
										"getconceptosdetaudiglosas(cast(dag.codigo as INTEGER)) AS concepto, " +
										"rg.glosa_sistema AS preglosa, " +
										"rg.estado AS estado, " +
										"dag.valor AS valor, " +
										"dag.cantidad AS cantidad " +
								"FROM facturas f " +
										"INNER JOIN auditorias_glosas ag ON (f.codigo=ag.codigo_factura) " +
										"INNER JOIN registro_glosas rg ON (ag.glosa=rg.codigo) " +
										"INNER JOIN det_auditorias_glosas dag ON (dag.auditoria_glosa=ag.codigo) " +
										"INNER JOIN det_factura_solicitud dfs ON (dfs.factura=f.codigo) " +
								"WHERE rg.codigo="+consFactura+" ORDER BY getconsecutivosolicitud(dag.solicitud) ";
		
		logger.info("\n\nCONSULTA DETALLE FACTURAS SQL ----->>>>>>>>>>>"+consultaDetalleFacturas);
		
		try 
		{	
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultaDetalleFacturas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			
		} 
		catch (SQLException e) 
		{
			logger.info("\n ERROR. CONSULTANDO DETALLE FACTURA ... BUSQUEDA DETALLE FACTURA--->>> "+e);
		}
		return resultados;
	}

	/**
	 * @return the consultaDetalleFacturas
	 */
	public static String getConsultaDetalleFacturas() {
		return consultaDetalleFacturas;
	}

	/**
	 * @param consultaDetalleFacturas the consultaDetalleFacturas to set
	 */
	public static void setConsultaDetalleFacturas(String consultaDetalleFacturas) {
		SqlBaseConsultarImpFacAudiDao.consultaDetalleFacturas = consultaDetalleFacturas;
	}	
}