package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;


public class SqlBaseBusquedaGlosasDao
{
	/**
	 * Variable para manejo de loggs
	 */
	private static Logger logger= Logger.getLogger(SqlBaseBusquedaGlosasDao.class);
	
	/**
	 * Variable para la consulta de las Glosas
	 */
	public static String consultaGlosas;
	
	/**
	 * Indices de la consulta GLosas
	 */
	private static String [] indicesGlosas ={"codigoglosa_","glosasis_","fechareg_","conveniog_","contratog_","glosaent_","fechanot_","valorg_","obser_","codigoconvenio_","indicativoglosa_"};
	
	/**
	 * Metodo que consulta las glosas segun parametros
	 * @param connection
	 * @param criterios
	 * @return
	 */
	public static HashMap consultarGlosas (Connection connection, HashMap criterios)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator ps;
		
		logger.info("\n\nMAPAAAA CRITERIOS>>>>>>>>>"+criterios);
		
		consultaGlosas="SELECT DISTINCT gs.codigo AS codigoglosa, " +
								"gs.glosa_sistema AS glosasis, " +
								"gs.fecha_registro_glosa AS fechareg, " +
								"getnombreconvenio(gs.convenio) AS conveniog, " +
								"c.codigo AS contratog, " +
								"c.numero_contrato AS numcontrato, " +
								"gs.glosa_entidad AS glosaent, " +
								"gs.fecha_notificacion AS fechanot, " +
								"gs.valor_glosa AS valorg, " +
								"ag.fue_auditada AS indicativoglosa, " +
								"gs.observaciones AS obser, " +
								"gs.convenio AS codigoconvenio, " +
								"gs.estado AS estado " +
						"FROM registro_glosas gs " +
								"LEFT OUTER JOIN auditorias_glosas ag ON (gs.codigo=ag.glosa) " +
								"LEFT OUTER JOIN contratos c ON (gs.contrato=c.codigo) " +
								"left outer join facturas fac on (fac.codigo=ag.codigo_factura) " +
								//
						"WHERE 1=1 ";
						if(!criterios.get("estado").toString().equals(""))
						{
							if(!(criterios.get("nombreFormaRemite")+"").equals("formaAprobarGlosas"))
								consultaGlosas+="AND gs.estado='"+criterios.get("estado").toString()+"' ";
							else 
							{
								consultaGlosas+="AND (gs.estado=('"+ConstantesIntegridadDominio.acronimoEstadoGlosaConfirmada+"') OR  " +
										"gs.estado=('"+ConstantesIntegridadDominio.acronimoEstadoGlosaRegistrada+"')) ";
							}
						}
						if(!criterios.get("fechaRegIni").toString().equals("") && !criterios.get("fechaRegFin").toString().equals(""))
							consultaGlosas+="AND gs.fecha_registro_glosa BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaRegIni").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaRegFin").toString())+"' ";
						if(!criterios.get("glosa").toString().equals(""))
								consultaGlosas+="AND gs.glosa_sistema='"+criterios.get("glosa").toString()+"' ";
						if(!criterios.get("glosaEntidad").toString().equals(""))
								consultaGlosas+="AND gs.glosa_entidad='"+criterios.get("glosaEntidad").toString()+"' ";
						if(!criterios.get("convenio").toString().equals("-1") && !criterios.get("convenio").toString().equals(""))
							consultaGlosas+="AND gs.convenio="+criterios.get("convenio").toString()+" ";
						if(!criterios.get("fechaRad").toString().equals(""))
							consultaGlosas+="AND gs.fecha_notificacion='"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaRad").toString())+"' ";
						if((criterios.get("nombreFormaRemite")+"").equals("formaRegistrarModificarGlosas") || (criterios.get("nombreFormaRemite")+"").equals("formaDetalleGlosas"))
							consultaGlosas+="AND ag.fue_auditada='GL' ";
						if(!UtilidadTexto.isEmpty(criterios.get("consecutivoFactura")+""))
							consultaGlosas+="AND fac.consecutivo_factura='"+criterios.get("consecutivoFactura")+"'";
						
						if((criterios.get("nombreFormaRemite")+"").equals("formaRegistrarModificarGlosas"))
						{
							consultaGlosas+="UNION ALL " +
											"SELECT DISTINCT gs.codigo AS codigoglosa, " +
											"gs.glosa_sistema AS glosasis, " +
											"gs.fecha_registro_glosa AS fechareg, " +
											"getnombreconvenio(gs.convenio) AS conveniog, " +
											"c.codigo AS contratog, " +
											"c.numero_contrato AS numcontrato, " +
											"gs.glosa_entidad AS glosaent, " +
											"gs.fecha_notificacion AS fechanot, " +
											"gs.valor_glosa AS valorg, " +
											"ag.fue_auditada AS indicativoglosa, " +
											"gs.observaciones AS obser, " +
											"gs.convenio AS codigoconvenio, " +
											"gs.estado AS estado " +
											"FROM registro_glosas gs " +
											"LEFT OUTER JOIN auditorias_glosas ag ON (gs.codigo=ag.glosa) " +
											"LEFT OUTER JOIN contratos c ON (gs.contrato=c.codigo) " +
											"left outer join facturas fac on (fac.codigo=ag.codigo_factura) " +
											"WHERE 1=1 " +
											"AND gs.estado='REGI'" +
											"AND ag.fue_auditada IS NULL ";
							
							if(!criterios.get("fechaRegIni").toString().equals("") && !criterios.get("fechaRegFin").toString().equals(""))
								consultaGlosas+="AND gs.fecha_registro_glosa BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaRegIni").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaRegFin").toString())+"' ";
							if(!criterios.get("glosa").toString().equals(""))
									consultaGlosas+="AND gs.glosa_sistema='"+criterios.get("glosa").toString()+"' ";
							if(!criterios.get("glosaEntidad").toString().equals(""))
									consultaGlosas+="AND gs.glosa_entidad='"+criterios.get("glosaEntidad").toString()+"' ";
							if(!criterios.get("convenio").toString().equals("-1") && !criterios.get("convenio").toString().equals(""))
								consultaGlosas+="AND gs.convenio="+criterios.get("convenio").toString()+" ";
							if(!criterios.get("fechaRad").toString().equals(""))
								consultaGlosas+="AND gs.fecha_notificacion='"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaRad").toString())+"' ";

							if(!UtilidadTexto.isEmpty(criterios.get("consecutivoFactura")+""))
								consultaGlosas+="AND fac.consecutivo_factura='"+criterios.get("consecutivoFactura")+"'";
						}

						
			
		String consulta = consultaGlosas;
		if((criterios.get("nombreFormaRemite")+"").equals("formaRegistrarModificarGlosas"))
			consulta += obtenerWhere(criterios);
		
		logger.info("\n\nCONSULTAAA GLOSAS SQL->>"+consulta);
		try 
		{	
			ps =  new PreparedStatementDecorator(connection.prepareStatement(consultaGlosas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			
			resultados.put("INDICES_MAPA", indicesGlosas);
		} 
		catch (SQLException e) 
		{
			logger.info("\n ERROR. CONSULTANDO GLOSAS ... BUSQUEDA GLOSAS--->>> "+e);
		}
		return resultados;
	}
	

	public static String obtenerWhere (HashMap<String, Object> criterios)
	{
		String where = "";
				
		logger.info("\n\nCRITERIOS-->"+criterios);
		
		if(criterios.containsKey("numConvenios"))
		{
			where += "AND";
			where += ("(");
			for(int i=0;i<Utilidades.convertirAEntero(criterios.get("numConvenios")+"");i++)
			{
				where += " gs.convenio="+criterios.get("convenio_"+i)+" ";
				if ((i+1)<Utilidades.convertirAEntero(criterios.get("numConvenios")+""))
					where += " OR ";
			}
			where += ") ";
		}
		return where;
	}
	
}