package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;


public class SqlBaseBusquedaConciliacionesDao
{
	/**
	 * Variable para el manejo de logs
	 */
	private static Logger logger = Logger.getLogger(SqlBaseBusquedaConciliacionesDao.class);

	private static String consultarConciliacion="SELECT r.codigo AS codconciliacion, " +
													"r.respuesta_consecutivo AS conciliacion, " +
													"r.fecha_respuesta AS fechaconc, " +
													"r.hora_respuesta AS horaconc, " +
													"r.convenio AS convenio, " +
													"r.concepto_respuesta AS conceptoconc, " +
													"r.porcen_aceptado AS paceptado, " +
													"r.porcen_soportado AS psoportado, " +
													"getnombreconvenio(r.convenio) AS nombreconvenio, " +													
													"r.nro_acta AS nroacta, " +
													"r.representante_convenio As respresentanteconv, " +
													"r.cargo_rep_convenio AS cargorepconvenio, " +
													"r.representante_inst AS representanteinst, " +
													"r.cargo_rep_inst AS cargorepinst, " +
													"r.observaciones, " +
													"cr.tipo_concepto AS tipoconcepto, " +
													"cr.concepto_ajuste AS conceptoajuste " +
												"FROM glosas.respuesta_glosa r " +
												"LEFT OUTER JOIN glosas.concepto_resp_glosas cr ON(r.concepto_respuesta=cr.codigo AND r.institucion= cr.institucion) " +
												"WHERE r.conciliacion= '"+ConstantesBD.acronimoSi+"' " +
												"AND r.estado = '"+ConstantesIntegridadDominio.acronimoEstadoGlosaRegistrada+"' ";
	
	public static String [] indices = {"conciliacion_","fechaconc_","horaconc_",
		"convenio_","conceptoconc_","paceptado_",
		"psoportado_","nombreconvenio_","nroacta_",
		"respresentanteconv_","cargorepconvenio_","representanteinst_",
		"cargorepinst_","observaciones_"};
	
	public static HashMap consultarConciliaciones(HashMap<String, Object> criterios) 
	{
		HashMap resultados= new HashMap();
		
		Connection con;		
		con= UtilidadBD.abrirConexion();
		
		PreparedStatementDecorator ps;
		String cadena=consultarConciliacion;
		
		if(!(criterios.get("convenio")+"").equals("-1"))
			cadena += "AND r.convenio='"+criterios.get("convenio")+"' ";
		if(!(criterios.get("conciliacion")+"").equals(""))
			cadena += "AND r.respuesta_consecutivo='"+criterios.get("conciliacion")+"' ";
		if(!(criterios.get("fechaInicial")+"").equals("") && !(criterios.get("fechaFinal")+"").equals(""))
			cadena += "AND r.fecha_respuesta BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"")+"' ";
		if(!(criterios.get("conceptoConciliacion")+"").equals("-1"))
			cadena += "AND r.concepto_respuesta='"+criterios.get("conceptoConciliacion")+"' ";
		if(!(criterios.get("nroActa")+"").equals(""))
			cadena += "AND r.nro_acta='"+criterios.get("nroActa")+"' ";
				
		logger.info("\n\nCONSULTA GENERICA CONCILIACIONES ----->>>>>>>>>>>"+cadena);
		
		try 
		{	
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			
			resultados.put("INDICES", indices);
			
			UtilidadBD.cerrarConexion(con);
		} 
		catch (SQLException e) 
		{
			logger.info("\n ERROR. CONSULTANDO RESPUESTA GLOSAS ... BUSQUEDA GENERICA RESPUESTA GLOSAS--->>> "+e);
		}	
		
		return resultados;
	}
}