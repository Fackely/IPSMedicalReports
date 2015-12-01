package com.princetonsa.dao.sqlbase.glosas;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.Utilidades;
import com.princetonsa.dto.glosas.DtoGlosa;

public class SqlBaseConsultarImprimirGlosasSinRespuestaDao {
	
	private static Logger logger = Logger.getLogger(SqlBaseConsultarImprimirGlosasDao.class);

	// Cadena(sql) de Busqueda con todos los parametros digenciados
	
	private static String strConsultarGlosasSinRespuestaC1="SELECT DISTINCT "+
		"coalesce(reglosa.convenio,"+ConstantesBD.codigoNuncaValido+") AS convenio , " +
		"coalesce(getnombreconvenio(reglosa.convenio),'') AS nomconvenio,"+
		"coalesce(reglosa.contrato,"+ConstantesBD.codigoNuncaValido+") AS  contrato,"+
		"coalesce(getnumerocontrato(reglosa.contrato),'') AS numcontrato, "+
		"coalesce(reglosa.codigo,"+ConstantesBD.codigoNuncaValido+") AS codigoglosa,"+
		"coalesce(reglosa.glosa_entidad,'') AS glosaentidad,"+
		"coalesce(to_char(reglosa.fecha_notificacion,'"+ConstantesBD.formatoFechaBD+"'),'') AS fechanoti,"+
		"coalesce(reglosa.glosa_sistema,'') AS glosasistema,"+
		"getintegridaddominio(reglosa.estado) AS estadoglosa, "+
		"reglosa.valor_glosa AS valorglosa,"+
		"coalesce(respglos.codigo,"+ConstantesBD.codigoNuncaValido+") AS codrespuesta,"+ 
		"coalesce(respglos.respuesta_consecutivo, '') AS resconsecutivo,"+
	    "coalesce((to_date('"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()) +"','yyyy-mm-dd')- reglosa.fecha_notificacion),0) AS diasinresp, " +
	    "coalesce(audiglo.fue_auditada,'' ) AS indicativofueauditada "+ 
		"FROM registro_glosas reglosa " +
		"INNER JOIN respuesta_glosa respglos ON (reglosa.codigo=respglos.glosa) " +
		"INNER JOIN auditorias_glosas audiglo ON (audiglo.glosa=reglosa.codigo) " +
		"inner join facturas fac on (fac.codigo=audiglo.codigo_factura) " +
		"where 1=1 ";		

		private static String strConsultarGlosasSinRespuestaC2=
									"SELECT DISTINCT "+
									"coalesce(reglosa.convenio,"+ConstantesBD.codigoNuncaValido+") AS convenio , " +
									"coalesce(getnombreconvenio(reglosa.convenio),'') AS nomconvenio,"+
									"coalesce(reglosa.contrato,"+ConstantesBD.codigoNuncaValido+") AS  contrato,"+
									"coalesce(getnumerocontrato(reglosa.contrato),'') AS numcontrato, "+
									"coalesce(reglosa.codigo,"+ConstantesBD.codigoNuncaValido+") AS codigoglosa,"+
									"coalesce(reglosa.glosa_entidad,'') AS glosaentidad,"+
									"coalesce(to_char(reglosa.fecha_notificacion,'"+ConstantesBD.formatoFechaBD+"'),'') AS fechanoti,"+
									"coalesce(reglosa.glosa_sistema,'') AS glosasistema,"+
									"getintegridaddominio(reglosa.estado) AS estadoglosa, "+
									"reglosa.valor_glosa AS valorglosa,"+
									ConstantesBD.codigoNuncaValido+" AS codrespuesta,"+ 
									"''"+" AS resconsecutivo,"+
								    "coalesce((to_date('"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()) +"','yyyy-mm-dd') - reglosa.fecha_notificacion),0) AS diasinresp, "+
								    "coalesce(audiglo.fue_auditada,'' ) AS indicativofueauditada "+
									"FROM registro_glosas reglosa " +
									"INNER JOIN auditorias_glosas audiglo ON (audiglo.glosa=reglosa.codigo) " +
									"inner join facturas fac on (fac.codigo=audiglo.codigo_factura) " +
									"where 1=1" ;

	/**
	 * @param consecutivoFactura 
	 * 
	 * */
	public static ArrayList<DtoGlosa> consultarListadoGlosas(Connection con, String filtroConvenio, String filtroContrato, String fechaInicial, String fechaFinal, String indicativo, String consecutivoFactura) 
	{		
		ArrayList<DtoGlosa> arregloGlosas=new ArrayList<DtoGlosa>();
		String  cadenaConsulta1= strConsultarGlosasSinRespuestaC1;
		String  cadenaConsulta2= strConsultarGlosasSinRespuestaC2;
		int convenio=0;
		PreparedStatementDecorator busqueda;
		
			
		try 
		{
			if(!filtroConvenio.equals("") || !fechaInicial.equals("") || (Utilidades.convertirAEntero(filtroContrato) > 0) || !fechaFinal.equals("")  || !indicativo.equals("")  || !consecutivoFactura.equals("") )
			{
				cadenaConsulta1=cadenaConsulta1+" and respglos.estado!= '"+ConstantesIntegridadDominio.acronimoEstadoRespuestaGlosaRadicada+ "' AND respglos.estado!= '"+ConstantesIntegridadDominio.acronimoEstadoRespuestaGlosaAprobada +"' AND reglosa.estado in ('"+ConstantesIntegridadDominio.acronimoEstadoGlosaConfirmada+"','"+ConstantesIntegridadDominio.acronimoEstadoGlosaAprobada+"' ) ";
				cadenaConsulta2=cadenaConsulta2+" AND  reglosa.estado in ('"+ConstantesIntegridadDominio.acronimoEstadoGlosaConfirmada+"','"+ConstantesIntegridadDominio.acronimoEstadoGlosaAprobada+"' )  AND (SELECT count(codigo) FROM respuesta_glosa WHERE glosa= reglosa.codigo) = 0 ";
			}
			if(!fechaInicial.equals("") && !fechaFinal.equals(""))		
			{
				cadenaConsulta1=cadenaConsulta1+" AND to_char(reglosa.fecha_notificacion,'yyyy-mm-dd') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' ";
				cadenaConsulta2=cadenaConsulta2+" (to_char(reglosa.fecha_notificacion,'yyyy-mm-dd') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"')";
			}
			if(!filtroConvenio.equals(""))
			{
				cadenaConsulta1=cadenaConsulta1+" AND reglosa.convenio=  "+filtroConvenio;
				cadenaConsulta2=cadenaConsulta2+" AND reglosa.convenio=  "+filtroConvenio;
			}
			if(Utilidades.convertirAEntero(filtroContrato) > 0)
	    	{
				cadenaConsulta1=cadenaConsulta1+"  AND reglosa.contrato= "+filtroContrato;
				cadenaConsulta2=cadenaConsulta2+"  AND reglosa.contrato= "+filtroContrato;
	    	}
			
			if (!indicativo.equals(""))
			{
				cadenaConsulta1=cadenaConsulta1+" and audiglo.fue_auditada='"+indicativo+"'";
				cadenaConsulta2=cadenaConsulta2+" and audiglo.fue_auditada='"+indicativo+"'";
			}
			
			if (!consecutivoFactura.equals("")) 
			{
				cadenaConsulta1=cadenaConsulta1+" AND fac.consecutivo_factura='"+consecutivoFactura+"'";
				cadenaConsulta2=cadenaConsulta2+" AND fac.consecutivo_factura='"+consecutivoFactura+"'";
			}
			
			busqueda=new PreparedStatementDecorator(con,cadenaConsulta1+" UNION "+cadenaConsulta2);
			
			ResultSetDecorator rs = new ResultSetDecorator(busqueda.executeQuery());
			
			while(rs.next())
			{
				DtoGlosa dto = new DtoGlosa();
				dto.getDtoRespuestaGlosa().setCodigoPk(rs.getInt("codrespuesta"));
				dto.getDtoRespuestaGlosa().setRespuestaConsecutivo(rs.getString("resconsecutivo"));
				dto.setCodigo(rs.getString("codigoglosa"));
				dto.setGlosaSistema(rs.getString("glosasistema"));
				dto.setCodigoConvenio(rs.getInt("convenio"));
				dto.setNombreConvenio(rs.getString("nomconvenio"));
				dto.setCodigoContrato(rs.getInt("contrato"));
				dto.setConsecutivoContrato(rs.getString("numcontrato"));
				dto.setGlosaEntidad(rs.getString("glosaentidad"));
				dto.setFechaNotificacion(rs.getString("fechanoti"));
				dto.setValorGlosa(rs.getDouble("valorglosa"));
				dto.setEstado(rs.getString("estadoglosa"));
				dto.setIndicativoFueAuditada(rs.getString("indicativofueauditada"));
				dto.setDiasInResp(rs.getInt("diasinresp"));
				arregloGlosas.add(dto);
			}
	
		}
		catch (SQLException e) {
			logger.warn("ERROR / listarGlosas / "+e);
		}
	
		logger.info("\n\n La Consulta quedo asi: "+cadenaConsulta1+cadenaConsulta2);		
		Utilidades.imprimirArrayList(arregloGlosas);
		return arregloGlosas;
	}


	public static String cadenaConsultaGlosasSinResp(HashMap parametros) 
	{
		String  cadenaConsulta1= strConsultarGlosasSinRespuestaC1;
		String  cadenaConsulta2= strConsultarGlosasSinRespuestaC2;
		
		String filtroContrato=parametros.get("contrato")+"";
		String filtroConvenio=parametros.get("convenio")+"";
		String fechaInicial=parametros.get("fechaIni")+"";
		String fechaFinal=parametros.get("fechaFin")+"";
		String indicativo=parametros.get("indicativo")+"";
		
		
		
		if(!filtroConvenio.equals("") || !fechaInicial.equals("") || (Utilidades.convertirAEntero(filtroContrato) > 0) || !fechaFinal.equals("")  || !indicativo.equals(""))//  || !consecutivoFactura.equals("") )
		{
			cadenaConsulta1=cadenaConsulta1+" and respglos.estado!= '"+ConstantesIntegridadDominio.acronimoEstadoRespuestaGlosaRadicada+ "' AND respglos.estado!= '"+ConstantesIntegridadDominio.acronimoEstadoRespuestaGlosaAprobada +"' AND reglosa.estado in ('"+ConstantesIntegridadDominio.acronimoEstadoGlosaConfirmada+"','"+ConstantesIntegridadDominio.acronimoEstadoGlosaAprobada+"' ) ";
			cadenaConsulta2=cadenaConsulta2+" AND  reglosa.estado in ('"+ConstantesIntegridadDominio.acronimoEstadoGlosaConfirmada+"','"+ConstantesIntegridadDominio.acronimoEstadoGlosaAprobada+"' )  AND (SELECT count(codigo) FROM respuesta_glosa WHERE glosa= reglosa.codigo) = 0 ";
		}
		if(!fechaInicial.equals("") && !fechaFinal.equals(""))		
		{
			cadenaConsulta1=cadenaConsulta1+" AND to_char(reglosa.fecha_notificacion,'yyyy-mm-dd') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' ";
			cadenaConsulta2=cadenaConsulta2+" (to_char(reglosa.fecha_notificacion,'yyyy-mm-dd') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"')";
		}
		if(!filtroConvenio.equals(""))
		{
			cadenaConsulta1=cadenaConsulta1+" AND reglosa.convenio=  "+filtroConvenio;
			cadenaConsulta2=cadenaConsulta2+" AND reglosa.convenio=  "+filtroConvenio;
		}
		if(Utilidades.convertirAEntero(filtroContrato) > 0)
    	{
			cadenaConsulta1=cadenaConsulta1+"  AND reglosa.contrato= "+filtroContrato;
			cadenaConsulta2=cadenaConsulta2+"  AND reglosa.contrato= "+filtroContrato;
    	}
		
		if (!indicativo.equals(""))
		{
			cadenaConsulta1=cadenaConsulta1+" and audiglo.fue_auditada='"+indicativo+"'";
			cadenaConsulta2=cadenaConsulta2+" and audiglo.fue_auditada='"+indicativo+"'";
		}
		/*
		if (!consecutivoFactura.equals("")) 
		{
			cadenaConsulta1=cadenaConsulta1+" AND fac.consecutivo_factura='"+consecutivoFactura+"'";
			cadenaConsulta2=cadenaConsulta2+" AND fac.consecutivo_factura='"+consecutivoFactura+"'";
		}
		*/
		
		return cadenaConsulta1+" UNION "+cadenaConsulta2;
	}
	
	
	
	/**
	 * Atualiza el log de consulta
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static boolean guardarLog(Connection con, HashMap criterios, String guardarLog)
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
	
	
	

}
