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

public class SqlBaseBusquedaRespuestaGlosasDao
{
	/**
	 * Variable para el manejo de logs
	 */
	private static Logger logger = Logger.getLogger(SqlBaseBusquedaRespuestaGlosasDao.class);
	
	/**
	 * Variable para la consulta de las Glosas
	 */
	public static String consultaRespuestas="SELECT DISTINCT rsg.codigo AS codigoresp, " +
												"rsg.respuesta_consecutivo AS respuesta, " +
												"rg.codigo AS codigoglosa, " +
												"rg.glosa_sistema AS glosa, " +
												"rg.convenio AS codigoconvenio, " +
												"getnombreconvenio(rg.convenio) AS nombreconvenio, " +
												"getnumerocontrato(rg.contrato) AS numcontrato, " +
												"rg.glosa_entidad AS glosaentidad, " +
												"coalesce(to_char(rsg.fecha_respuesta,'"+ConstantesBD.formatoFechaAp+"'),'') AS fecharespuesta, " +
												"rsg.conciliacion AS conciliacion, " +
												"coalesce(to_char(rg.fecha_registro_glosa,'"+ConstantesBD.formatoFechaAp+"'),'') AS fecharegistroglosa, " +
												"rsg.observaciones AS observaciones, " +
												"coalesce(to_char(rg.fecha_notificacion,'"+ConstantesBD.formatoFechaAp+"'),'') AS fechanotificacion, " +
												"rg.valor_glosa AS valorglosa, " +
												"rg.contrato AS contrato, " +
												"ag.fue_auditada AS indicativoglosa, " +
												"rsg.valor_respuesta AS valorrespuesta " +
											"FROM respuesta_glosa rsg " +
												"INNER JOIN registro_glosas rg ON (rsg.glosa=rg.codigo) " +
												"INNER JOIN auditorias_glosas ag ON(rg.codigo=ag.glosa) " +
												"left outer join facturas fac on (fac.codigo=ag.codigo_factura) " +
											"WHERE 1=1 ";
	
	public static String [] indices = {"codigoresp_","respuesta_","codigoglosa_",
										"glosa_","codigoconvenio_","nombreconvenio_",
										"glosaentidad_","fecharespuesta_","conciliacion_",
										"fecharegistroglosa_","observaciones_","fechanotificacion_",
										"valorglosa_","contrato_","indicativoglosa_"};
	
	
	/**
	 * Metodo que consulta todas las respuestas segun valores del mapa criterios
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static HashMap consultarRespuestas(Connection con, HashMap criterios)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator ps;
		String cadena=consultaRespuestas;
		if(!(criterios.get("estadoglosa")+"").equals(""))
			cadena += "AND rg.estado='"+criterios.get("estadoglosa")+"' ";
		if(!(criterios.get("estadorespuesta")+"").equals(""))
			cadena += "AND rsg.estado='"+criterios.get("estadorespuesta")+"' ";
		if(!(criterios.get("respuesta")+"").equals(""))
			cadena += "AND rsg.respuesta_consecutivo='"+criterios.get("respuesta")+"' ";
		if(!(criterios.get("fecharespini")+"").equals("") && !(criterios.get("fecharespfin")+"").equals(""))
			cadena += "AND rsg.fecha_respuesta BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fecharespini")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fecharespfin")+"")+"' ";
		if(!(criterios.get("glosa")+"").equals(""))
			cadena += "AND rg.glosa_sistema='"+criterios.get("glosa")+"' ";
		if(!(criterios.get("glosaentidad")+"").equals(""))
			cadena += "AND rg.glosa_entidad='"+criterios.get("glosaentidad")+"' ";
		if(!(criterios.get("convenio")+"").equals("-1"))
			cadena += "AND rg.convenio='"+criterios.get("convenio")+"' ";
		if(!(criterios.get("fecharad")+"").equals(""))
			cadena += "AND rsg.fecha_radicacion='"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fecharad")+"")+"' ";
		if((criterios.get("nombreFormaRemite")+"").equals("formaRadicarRespuestas"))
			cadena += "AND ag.fue_auditada='GL' ";
		if(!UtilidadTexto.isEmpty(criterios.get("consecutivoFac")+""))
			cadena+="AND fac.consecutivo_factura='"+criterios.get("consecutivoFac")+"'";
		
		
		logger.info("\n\nCONSULTA GENERICA RESPUESTA GLOSAS SQL ----->>>>>>>>>>>"+cadena);
		
		try 
		{	
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			
			resultados.put("INDICES", indices);
		} 
		catch (SQLException e) 
		{
			logger.info("\n ERROR. CONSULTANDO RESPUESTA GLOSAS ... BUSQUEDA GENERICA RESPUESTA GLOSAS--->>> "+e);
		}
		logger.info("\n\nMAPA BUSQUEDA GENERICA RESPUESTAS GLOSAS>>>>>>>>>>>>>>>>>>>>"+resultados);
		return resultados;
	}
}