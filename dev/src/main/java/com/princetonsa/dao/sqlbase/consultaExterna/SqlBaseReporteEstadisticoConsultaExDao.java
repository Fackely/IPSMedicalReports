package com.princetonsa.dao.sqlbase.consultaExterna;

import java.util.HashMap;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;
import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * @author Jose Eduardo Arias Doncel
 * */
public class SqlBaseReporteEstadisticoConsultaExDao
{	
	//*************************************************************************************************
		
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseReporteEstadisticoConsultaExDao.class);
	
	/**
	 * Especialidades 
	 * */
	private static String especialidades =  
										"select "+ 
										"'0' as grupo,"+
										"'1' as orden,"+
										"sc.codigo_cita AS codigocita,"+
										"e.nombre AS indicador,"+
										"getnombremes(to_char(sc.fecha_cita,'MM')) || ' ' || to_char(sc.fecha_cita,'YYYY')  as fechacita,"+
										"to_char(sc.fecha_cita,'MM') as numeromes "+
										"from "+
										"servicios_cita sc "+ 
										"inner join cita c ON(c.codigo = sc.codigo_cita) "+
										"inner join especialidades e ON(e.codigo = sc.especialidad) "+
										"inner join centros_costo cc ON (cc.codigo = sc.centro_costo)"+//Cambio Anexo 809
										"inner join valoraciones_consulta vc ON (vc.numero_solicitud = sc.numero_solicitud) "+
										"where  "+
										" 1=2  "+
										" order by 6 ASC "; 
	
	/**	  
	 * 1 vez
	 * */
	private static String primeravez = 
								"(select "+ 
								"'0' as grupo, "+
								"'1' as orden, "+
								"sc.codigo_cita AS codigocita, "+
								"'1 VEZ' AS indicador, "+
								"getnombremes(to_char(sc.fecha_cita,'MM')) || ' ' || to_char(sc.fecha_cita,'YYYY')  as fechacita, "+
								"to_char(sc.fecha_cita,'MM') as numeromes "+
								"from  "+
								"servicios_cita sc "+ 
								"inner join cita c ON(c.codigo = sc.codigo_cita) "+
								"inner join  unidades_consulta uc ON(uc.especialidad = sc.especialidad) "+ //Cambio Anexo 809
								"inner join especialidades e ON(e.codigo = sc.especialidad) "+ 
								"inner join centros_costo cc ON (cc.codigo = sc.centro_costo)"+
								"inner join valoraciones_consulta vc ON (vc.numero_solicitud = sc.numero_solicitud) "+
								"where  "+
								" 1=2 "+								
								"UNION ALL "+								
								"select  "+
								"'0' as grupo, "+
								"'1' as orden, "+
								"sc.codigo_cita AS codigocita, "+
								"'CONTROL' AS indicador, "+
								"getnombremes(to_char(sc.fecha_cita,'MM')) || ' ' || to_char(sc.fecha_cita,'YYYY')  as fechacita, "+
								"to_char(sc.fecha_cita,'MM') as numeromes "+
								"from  "+
								"servicios_cita sc "+ 
								"inner join cita c ON(c.codigo = sc.codigo_cita) "+
								"inner join  unidades_consulta uc ON(uc.especialidad = sc.especialidad) "+ //Cambio Anexo 809
								"inner join especialidades e ON(e.codigo = sc.especialidad) "+
								"inner join centros_costo cc ON (cc.codigo = sc.centro_costo)"+
								"inner join valoraciones_consulta vc ON (vc.numero_solicitud = sc.numero_solicitud) "+
								"where  "+
								" 1=3 "+
								" ) "+
								"order by 6 ASC ";  
	
	/**
	 * Promedios
	 * */
/*	private static String promedio =  
								"select orden, sum(codigocita2) AS codigocita3, sum(codigocita2)/diashabiles as promedio,s.fechacita,numeromes,indicador  from "+
								"(select "+  
								"'1' as orden, "+ 
								"count(sc.codigo_cita) AS codigocita2, "+ 
								"(diasdelmes(to_char(sc.fecha_cita,'MM'),to_char(sc.fecha_cita,'YYYY') || '-' || to_char(sc.fecha_cita,'MM'))-getDiasHabilesMesAno(to_char(sc.fecha_cita,'YYYY') || '-' || to_char(sc.fecha_cita,'MM'))) AS diashabiles, "+
								"getnombremes(to_char(sc.fecha_cita,'MM')) || ' ' || to_char(sc.fecha_cita,'YYYY')  as fechacita, "+
								"to_char(sc.fecha_cita,'MM') as numeromes, "+
								"'PROMEDIO ATENCION DIA' as indicador "+  
								"from   "+
								"servicios_cita sc "+ 
								"inner join cita c ON(c.codigo = sc.codigo_cita) "+ 
								"inner join  unidades_consulta uc ON(uc.especialidad = sc.especialidad) "+ //Cambio Anexo 809
								"inner join especialidades e ON(e.codigo = sc.especialidad) "+ 
								"inner join centros_costo cc ON (cc.codigo = sc.centro_costo) "+
								"inner join valoraciones_consulta vc ON (vc.numero_solicitud = sc.numero_solicitud) "+
								"where 1=2 "+ 
								"group by indicador,orden,fecha_cita,numeromes order by 5 ASC ) s " +
								"group by s.orden,s.diashabiles,s.fechacita,s.numeromes,s.indicador ";
								*/
	private static String promedio=""+
						" SELECT \"orden\"                    , "+
						"  SUM(\"codigocita2\")             AS codigocita3, "+
						"  SUM(\"codigocita2\")/\"diashabiles\" AS promedio   , "+
						"  s.\"fechacita\"                      AS fechacita  , "+
						"  s.\"numeromes\"                      AS numeromes  , "+
						"  s.\"indicador\"                      AS indicador "+
						"   FROM "+
						"  (SELECT COUNT(\"codigocita2\") AS codigocita2, "+
						"    \"diashabiles\"              AS diashabiles, "+
						"    \"fechacita\"                AS fechacita, "+
						"    \"numeromes\"                AS numeromes, "+
						"    \"indicador\"                AS indicador, "+
						"    \"orden\"                    AS orden "+
						"     FROM "+
						"    (SELECT '1'      AS orden      , "+
						"      sc.codigo_cita AS codigocita2, "+
						"      (diasdelmes(TO_CHAR(sc.fecha_cita,'MM'),TO_CHAR(sc.fecha_cita,'YYYY'))-getDiasHabilesMesAno(TO_CHAR(sc.fecha_cita,'YYYY') "+
						"      || '-' "+
						"      || TO_CHAR(sc.fecha_cita,'MM'))) AS diashabiles, "+
						"      getnombremes(TO_CHAR(sc.fecha_cita,'MM')) "+
						"      || ' ' "+
						"      || TO_CHAR(sc.fecha_cita,'YYYY') AS fechacita, "+
						"      TO_CHAR(sc.fecha_cita,'MM')      AS numeromes, "+
						"      'PROMEDIO ATENCION DIA'          AS indicador "+
						"       FROM servicios_cita sc "+
						"    INNER JOIN cita c "+
						"         ON(c.codigo = sc.codigo_cita) "+
						"    INNER JOIN unidades_consulta uc "+
						"         ON(uc.especialidad = sc.especialidad) "+
						"    INNER JOIN especialidades e "+
						"         ON(e.codigo = sc.especialidad) "+
						"    INNER JOIN centros_costo cc "+
						"         ON (cc.codigo = sc.centro_costo) "+
						"    INNER JOIN valoraciones_consulta vc "+
						"         ON (vc.numero_solicitud = sc.numero_solicitud) "+
						"      WHERE 1=2 "+
						"    ) t "+
						" GROUP BY \"indicador\", "+
						"    \"orden\"          , "+
						"    \"fechacita\"      , "+
						"    \"numeromes\"      , "+
						"    \"diashabiles\" "+
						" ORDER BY \"numeromes\" ASC "+
						"  ) s "+
						"GROUP BY s.\"orden\", "+
						"  s.\"diashabiles\" , "+
						"  s.\"fechacita\"   , "+
						"  s.\"numeromes\"   , "+
						"  s.\"indicador\"";


	/**
	 *  Motivo Cancelacion Cita por Especialidad
	 * */
	private static String StrMotivoCancelacionCitaPac = "" +
								"select * from ( "+
								"(select " +
								"e.codigo AS codigo_especialidad, "+
								"e.nombre AS descripcion_especialidad," +
								"mcc.codigo AS codigo_motivo, "+
								"mcc.descripcion AS descripcion_motivo, "+
								"count(sc.codigo_cita) AS codigo_cita "+
								"from "+
								"servicios_cita sc "+
								"inner join cita c ON (c.codigo = sc.codigo_cita) "+
								"inner join agenda a ON (a.codigo = c.codigo_agenda) "+
								"inner join centros_costo cc ON (cc.codigo = sc.centro_costo) "+
								"inner join motivos_cancelacion_cita mcc ON (mcc.codigo = c.motivo_cancelacion) "+
								"inner join  unidades_consulta uc ON(uc.especialidad = sc.especialidad) "+ //Cambio Anexo 809
								"inner join especialidades e ON(e.codigo = sc.especialidad) "+
								"where "+
								" 1=2 " +
								"group by e.codigo,e.nombre,mcc.codigo,mcc.descripcion " +
								")" +
								"UNION " +
								"(" +
								"select " +
								"e.codigo AS codigo_especialidad, "+
								"e.nombre AS descripcion_especialidad," +
								ConstantesBD.codigoNuncaValido+" AS codigo_motivo, " +
								"'"+ConstantesBD.codigoNuncaValido+"' AS descripcion_motivo, "+
								"count(sc.codigo_cita) AS codigo_cita "+
								"from "+
								"servicios_cita sc "+
								"inner join cita c ON (c.codigo = sc.codigo_cita) "+
								"inner join agenda a ON (a.codigo = c.codigo_agenda) "+
								"inner join centros_costo cc ON (cc.codigo = sc.centro_costo) "+								
								"inner join  unidades_consulta uc ON(uc.especialidad = sc.especialidad) "+ //Cambio Anexo 809
								"inner join especialidades e ON(e.codigo = sc.especialidad) "+
								"where "+
								" 1=3 " +
								"group by e.codigo,e.nombre " +
								")) s "+
								"order by descripcion_especialidad,descripcion_motivo ";
	
	/**
	 *  Motivo Cancelacion Cita por Institucion
	 * */
	private static String StrMotivoCancelacionCitaInst = "" +
								"select * from ( "+
								"(select " +
								"e.codigo AS codigo_especialidad, "+
								"e.nombre AS descripcion_especialidad," +
								"a.codigo_medico," +
								"getnombrepersona2(a.codigo_medico) as descripcion_medico," +								
								"mcc.codigo AS codigo_motivo, "+
								"mcc.descripcion AS descripcion_motivo, "+
								"count(sc.codigo_cita) AS codigo_cita "+
								"from "+
								"servicios_cita sc "+
								"inner join cita c ON (c.codigo = sc.codigo_cita) "+
								"inner join agenda a ON (a.codigo = c.codigo_agenda) "+
								"inner join centros_costo cc ON (cc.codigo = sc.centro_costo) "+
								"inner join motivos_cancelacion_cita mcc ON (mcc.codigo = c.motivo_cancelacion) "+
								"inner join  unidades_consulta uc ON(uc.especialidad = sc.especialidad) "+ //Cambio Anexo 809
								"inner join especialidades e ON(e.codigo = sc.especialidad) "+
								"where "+
								" 1=2 " +
								"group by e.codigo,e.nombre,mcc.codigo,mcc.descripcion,a.codigo_medico " +
								")" +
								"UNION " +
								"(" +
								"select " +
								"e.codigo AS codigo_especialidad, "+
								"e.nombre AS descripcion_especialidad," +
								"a.codigo_medico," +
								"getnombrepersona(a.codigo_medico) as descripcion_medico," +
								ConstantesBD.codigoNuncaValido+" AS codigo_motivo, " +
								"'"+ConstantesBD.codigoNuncaValido+"' AS descripcion_motivo, "+
								"count(sc.codigo_cita) AS codigo_cita "+
								"from "+
								"servicios_cita sc "+
								"inner join cita c ON (c.codigo = sc.codigo_cita) "+
								"inner join agenda a ON (a.codigo = c.codigo_agenda) "+
								"inner join centros_costo cc ON (cc.codigo = sc.centro_costo) "+								
								"inner join  unidades_consulta uc ON(uc.especialidad = sc.especialidad) "+//Cambio Anexo 809
								"inner join especialidades e ON(e.codigo = sc.especialidad) "+
								"where "+
								" 1=3 " +
								"group by e.codigo,e.nombre,a.codigo_medico " +
								")) s "+
								"order by descripcion_especialidad,descripcion_medico,descripcion_motivo ";	
	
	//*************************************************************************************************
	/**
	 * Arma la sentencia del Where a partir de los filtros de la busqueda
	 * @param particulares TODO
	 * @param HashMap parametros
	 * */
	public static HashMap getCondicionesBuquedaReporte(HashMap parametros, String particulares)
	{ 
		HashMap respuesta = new HashMap();
		String where = " 1=1 ",parametrosbusqueda = "",where0 ="",where1 ="",where2 ="", cadena = "";
		int codigoReporte = Utilidades.convertirAEntero(parametros.get("codigoReporte").toString());
		
		//Validación Reporte
		switch (codigoReporte) 
		{
		
			//******************************************************************************************
			//******************************************************************************************
			case ConstantesBD.codigoReporteIndicadoresGestionConsEx:				
			
				//El Centro de Atención siempre es requerido
				if(parametros.containsKey("codigoCentroAtencion") 
						&& !parametros.get("codigoCentroAtencion").toString().equals(""))
				{
					where += " AND cc.centro_atencion = "+parametros.get("codigoCentroAtencion").toString().split(ConstantesBD.separadorSplit)[0];
					parametrosbusqueda += " CENTRO ATENCIÓN ["+parametros.get("codigoCentroAtencion").toString().split(ConstantesBD.separadorSplit)[1]+"]     ";
				}
				
				//El Rango de Fechas siempre es requerido
				if(parametros.containsKey("fechaInicial") 
						&& !parametros.get("fechaInicial").toString().equals(""))
				{
					where += " AND to_char(sc.fecha_cita, 'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicial").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinal").toString())+"' ";
					parametrosbusqueda += " RANGO DE FECHAS ["+parametros.get("fechaInicial").toString()+" - "+parametros.get("fechaFinal").toString()+" ]     ";			
				}
				
				//Especialidades 
				if(parametros.containsKey("codigoEspecialidad") 
						&& !parametros.get("codigoEspecialidad").toString().equals(""))
				{
					where += " AND sc.especialidad IN ("+parametros.get("codigoEspecialidad").toString()+") ";
					parametrosbusqueda += " ESPECIALIDADES ["+parametros.get("nombreEspecialidad").toString()+" ]     ";			
				}			
				
				where +=" AND  c.estado_cita = "+ConstantesBD.codigoEstadoCitaAtendida+" ";
				
				//Numero de dataset
				respuesta.put("numdataset",4);				
								
				respuesta.put("dataset0","especialidades");
				respuesta.put("dataset1","1vez");
				respuesta.put("dataset2","particulares");			
				respuesta.put("dataset3","porcentajes");
				
				//especialidades
				where0 = where ;
				cadena = especialidades.replace("1=2",where0);
				respuesta.put("where0",cadena);
				
				//1 vez
				where1 = where +" AND  vc.primera_vez ="+ValoresPorDefecto.getValorTrueParaConsultas()+" ";
				where2 = where +" AND  vc.primera_vez ="+ValoresPorDefecto.getValorFalseParaConsultas()+" ";
				cadena = primeravez.replace("1=2",where1);				
				cadena = cadena.replace("1=3",where2);
				respuesta.put("where1",cadena);
				
				//particulares
				cadena = particulares.replace("1=2",where);				
				cadena = cadena.replace("1=3",where);
				respuesta.put("where2",cadena);
				
				//porcentajes
				cadena = promedio.replace("1=2",where);				
				cadena = cadena.replace("1=3",where);
				cadena = cadena.replace("1=4",where);
				respuesta.put("where3",cadena);								
											
				//Parametros			
				respuesta.put("parametrosbusqueda",parametrosbusqueda);
				respuesta.put("nombrearchivoreporte","IndicadoresGestionConsultaEx.rptdesign");			
				respuesta.put("reemplazarTodo",ConstantesBD.acronimoSi);
			break;
			
			//******************************************************************************************
			//******************************************************************************************
			
			case ConstantesBD.codigoReporteOportuConsulExEspecialidad:
								
				//El Centro de Atención siempre es requerido
				if(parametros.containsKey("codigoCentroAtencion") 
						&& !parametros.get("codigoCentroAtencion").toString().equals(""))
				{
					where += " AND cc.centro_atencion = "+parametros.get("codigoCentroAtencion").toString().split(ConstantesBD.separadorSplit)[0];
					parametrosbusqueda += " CENTRO ATENCIÓN ["+parametros.get("codigoCentroAtencion").toString().split(ConstantesBD.separadorSplit)[1]+"]      ";
				}
				
				//El Rango de Fechas siempre es requerido
				if(parametros.containsKey("fechaInicial") 
						&& !parametros.get("fechaInicial").toString().equals(""))
				{
					where += " AND to_char(sc.fecha_cita, 'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicial").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinal").toString())+"' ";
					parametrosbusqueda += " RANGO DE FECHAS ["+parametros.get("fechaInicial").toString()+" - "+parametros.get("fechaFinal").toString()+" ]     ";
				}
				
				//Especialidades 
				if(parametros.containsKey("codigoEspecialidad") 
						&& !parametros.get("codigoEspecialidad").toString().equals(""))
				{
					where += " AND sc.especialidad IN ("+parametros.get("codigoEspecialidad").toString()+") ";
					parametrosbusqueda += " ESPECIALIDADES ["+parametros.get("nombreEspecialidad").toString()+" ]     ";			
				}
				
				//Tipo Consulta
				if(parametros.containsKey("codigoTipoConsulta") 
						&& !parametros.get("codigoTipoConsulta").toString().equals(""))
				{					
					parametrosbusqueda += " TIPO CONSULTA ["+ValoresPorDefecto.getIntegridadDominio(parametros.get("codigoTipoConsulta").toString())+" ]     ";
					
					if(parametros.get("codigoTipoConsulta").toString().equals(ConstantesIntegridadDominio.acronimoControl))					
						where += " AND vc.primera_vez = "+ValoresPorDefecto.getValorFalseParaConsultas()+" ";
					else if (parametros.get("codigoTipoConsulta").toString().equals(ConstantesIntegridadDominio.acronimoPrimeraVez))				
						where += " AND vc.primera_vez = "+ValoresPorDefecto.getValorTrueParaConsultas()+" ";				
				}
				
										
				respuesta.put("dataset","dataset");
				respuesta.put("where",where);
				respuesta.put("parametrosbusqueda",parametrosbusqueda);
				respuesta.put("nombrearchivoreporte","OportunidadConsultaExternaEsp.rptdesign");
				
			break;
			
			//******************************************************************************************
			//******************************************************************************************
			
			case ConstantesBD.codigoReporteOportuConsulExProfesionalSalud:			
				
				//El Centro de Atención siempre es requerido
				if(parametros.containsKey("codigoCentroAtencion") 
						&& !parametros.get("codigoCentroAtencion").toString().equals(""))
				{
					where += " AND cc.centro_atencion = "+parametros.get("codigoCentroAtencion").toString().split(ConstantesBD.separadorSplit)[0];
					parametrosbusqueda += " CENTRO ATENCIÓN ["+parametros.get("codigoCentroAtencion").toString().split(ConstantesBD.separadorSplit)[1]+"]     ";
				}
				
				//El Rango de Fechas siempre es requerido
				if(parametros.containsKey("fechaInicial") 
						&& !parametros.get("fechaInicial").toString().equals(""))
				{
					where += " AND to_char(sc.fecha_cita, 'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicial").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinal").toString())+"' ";
					parametrosbusqueda += " RANGO DE FECHAS ["+parametros.get("fechaInicial").toString()+" - "+parametros.get("fechaFinal").toString()+" ]     ";			
				}
				
				//Codigo Especialidades 
				if(parametros.containsKey("codigoEspecialidad") 
						&& !parametros.get("codigoEspecialidad").toString().equals(""))
				{
					where += " AND sc.especialidad IN ("+parametros.get("codigoEspecialidad").toString()+") ";
					parametrosbusqueda += " ESPECIALIDADES ["+parametros.get("nombreEspecialidad").toString()+" ]     ";			
				}
				
				//Tipo Consulta
				if(parametros.containsKey("codigoTipoConsulta") 
						&& !parametros.get("codigoTipoConsulta").toString().equals(""))
				{					
					parametrosbusqueda += " TIPO CONSULTA ["+ValoresPorDefecto.getIntegridadDominio(parametros.get("codigoTipoConsulta").toString())+" ]     ";
					
					if(parametros.get("codigoTipoConsulta").toString().equals(ConstantesIntegridadDominio.acronimoControl))					
						where += " AND vc.primera_vez = "+ValoresPorDefecto.getValorFalseParaConsultas()+" ";
					else if (parametros.get("codigoTipoConsulta").toString().equals(ConstantesIntegridadDominio.acronimoPrimeraVez))				
						where += " AND vc.primera_vez = "+ValoresPorDefecto.getValorTrueParaConsultas()+" ";				
				}
								
				//Profesional de la salud
				if(parametros.containsKey("codigoProfesionalSalud") 
						&& !parametros.get("codigoProfesionalSalud").toString().equals("")
							&& !parametros.get("codigoProfesionalSalud").toString().equals(ConstantesBD.codigoNuncaValido+""))
				{
					where += " AND a.codigo_medico IN ("+parametros.get("codigoProfesionalSalud").toString()+") ";
					parametrosbusqueda += " PROFESIONAL SALUD ["+parametros.get("nombreProfesionalSalud").toString()+" ]     ";			
				}
				
									
				respuesta.put("dataset","dataset");
				respuesta.put("where",where);
				respuesta.put("parametrosbusqueda",parametrosbusqueda);
				respuesta.put("nombrearchivoreporte","OportunidadEspecialidadProfesionalSalud.rptdesign");
				
				
			break;
			
			//******************************************************************************************
			//******************************************************************************************
			
			case ConstantesBD.codigoReporteIndicadoresOportuEspecMes:
								
				//El Centro de Atención siempre es requerido
				if(parametros.containsKey("codigoCentroAtencion") 
						&& !parametros.get("codigoCentroAtencion").toString().equals(""))
				{
					where += " AND cc.centro_atencion = "+parametros.get("codigoCentroAtencion").toString().split(ConstantesBD.separadorSplit)[0];
					parametrosbusqueda += " CENTRO ATENCIÓN ["+parametros.get("codigoCentroAtencion").toString().split(ConstantesBD.separadorSplit)[1]+"]     ";
				}
				
				//El Rango de Fechas siempre es requerido
				if(parametros.containsKey("fechaInicial") 
						&& !parametros.get("fechaInicial").toString().equals(""))
				{
					where += " AND to_char(sc.fecha_cita, 'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicial").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinal").toString())+"' ";
					parametrosbusqueda += " RANGO DE FECHAS ["+parametros.get("fechaInicial").toString()+" - "+parametros.get("fechaFinal").toString()+" ]     ";			
				}
				
				//Especialidades 
				if(parametros.containsKey("codigoEspecialidad") 
						&& !parametros.get("codigoEspecialidad").toString().equals(""))
				{
					where += " AND sc.especialidad IN ("+parametros.get("codigoEspecialidad").toString()+") ";
					parametrosbusqueda += " ESPECIALIDADES ["+parametros.get("nombreEspecialidad").toString()+" ]     ";			
				}				
				 
				where +=" AND  c.estado_cita = "+ConstantesBD.codigoEstadoCitaAtendida+" ";
								
				respuesta.put("dataset","dataset");
				respuesta.put("where",where);
				respuesta.put("parametrosbusqueda",parametrosbusqueda);
				respuesta.put("nombrearchivoreporte","IndicadoresOportEspecMes.rptdesign");
				
			break;
			
			//******************************************************************************************
			//******************************************************************************************
			
			case ConstantesBD.codigoReporteIndiceCancelacionCitaMes:
				
				//El Centro de Atención siempre es requerido
				if(parametros.containsKey("codigoCentroAtencion") 
						&& !parametros.get("codigoCentroAtencion").toString().equals(""))
				{
					where += " AND cc.centro_atencion = "+parametros.get("codigoCentroAtencion").toString().split(ConstantesBD.separadorSplit)[0];
					parametrosbusqueda += " CENTRO ATENCIÓN ["+parametros.get("codigoCentroAtencion").toString().split(ConstantesBD.separadorSplit)[1]+"]     ";
				}
				 
				//El Rango de Fechas siempre es requerido
				if(parametros.containsKey("fechaInicial") 
						&& !parametros.get("fechaInicial").toString().equals(""))
				{
					where += " AND to_char(sc.fecha_cita, 'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicial").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinal").toString())+"' ";
					parametrosbusqueda += " RANGO DE FECHAS ["+parametros.get("fechaInicial").toString()+" - "+parametros.get("fechaFinal").toString()+" ]     ";			
				}
				
				//Especialidades 
				if(parametros.containsKey("codigoEspecialidad") 
						&& !parametros.get("codigoEspecialidad").toString().equals(""))
				{
					where += " AND sc.especialidad IN ("+parametros.get("codigoEspecialidad").toString()+") ";
					parametrosbusqueda += " ESPECIALIDADES ["+parametros.get("nombreEspecialidad").toString()+" ]     ";			
				}
				
				//Cita Cancelada por
				if(parametros.containsKey("codigoCanceladaPor") 
						&& !parametros.get("codigoCanceladaPor").toString().equals(""))
				{
					if(parametros.get("codigoCanceladaPor").toString().equals(ConstantesBD.codigoEstadoCitaCanceladaInstitucion+""))
					{	
						parametrosbusqueda += " CITA CANCELADA POR [Institución]     ";
						
						//where0 += " AND mcc.tipo_cancelacion = "+parametros.get("codigoCanceladaPor").toString()+" ";															
						where1 += " AND ((c.estado_cita IN ("+ConstantesBD.codigoEstadoCitaCanceladaInstitucion+")) " +
								    " OR ( c.estado_cita IN("+
															ConstantesBD.codigoEstadoCitaReservada+","+
															ConstantesBD.codigoEstadoCitaAsignada+","+
															ConstantesBD.codigoEstadoCitaReprogramada+")" +							    
								    "   )  )";
						
					}	
					else
					{	
						parametrosbusqueda += " CITA CANCELADA POR [Paciente]     ";
						
						//where0 += " AND mcc.tipo_cancelacion = "+parametros.get("codigoCanceladaPor").toString()+" ";															
						where1 += " AND ((c.estado_cita IN ("+ConstantesBD.codigoEstadoCitaCanceladaPaciente+")) " +
								    " OR ( c.estado_cita IN("+
															ConstantesBD.codigoEstadoCitaReservada+","+
															ConstantesBD.codigoEstadoCitaAsignada+","+
															ConstantesBD.codigoEstadoCitaReprogramada+")" +							    
								    "   )  )";
					}
					
				}
				
				//En la vista del reporte existen dos dataset, para cada uno se crea el where correspondiente
				if(parametros.get("codigoCanceladaPor").toString().equals(ConstantesBD.codigoEstadoCitaCanceladaInstitucion+""))
				{
					respuesta.put("numdataset",2);
					respuesta.put("dataset0","especialidades");								
					respuesta.put("dataset1","totalconsultasprogramadas");
					
					where0 = where + where0 + " AND  c.estado_cita IN("+					
						ConstantesBD.codigoEstadoCitaCanceladaInstitucion+") ";
					
					where1 = where + where1 +" AND  c.estado_cita IN("+
						ConstantesBD.codigoEstadoCitaReservada+","+
						ConstantesBD.codigoEstadoCitaAsignada+","+
						ConstantesBD.codigoEstadoCitaReprogramada+","+
						ConstantesBD.codigoEstadoCitaCanceladaInstitucion+") ";
									
					respuesta.put("where0",where0);				
					respuesta.put("where1",where1);				
					respuesta.put("parametrosbusqueda",parametrosbusqueda);
					respuesta.put("nombrearchivoreporte","IndiceCancelacionMes.rptdesign");
				}
				else
				{
					respuesta.put("numdataset",2);
					respuesta.put("dataset0","especialidades");								
					respuesta.put("dataset1","totalconsultasprogramadas");
					
					where0 = where + where0 + " AND  c.estado_cita IN("+					
						ConstantesBD.codigoEstadoCitaCanceladaPaciente+") ";
					
					where1 = where + where1 +" AND  c.estado_cita IN("+
						ConstantesBD.codigoEstadoCitaReservada+","+
						ConstantesBD.codigoEstadoCitaAsignada+","+
						ConstantesBD.codigoEstadoCitaReprogramada+","+
						ConstantesBD.codigoEstadoCitaCanceladaPaciente+") ";
									
					respuesta.put("where0",where0);				
					respuesta.put("where1",where1);				
					respuesta.put("parametrosbusqueda",parametrosbusqueda);
					respuesta.put("nombrearchivoreporte","IndiceCancelacionMes.rptdesign");
				}
				
			break;
			
			//******************************************************************************************
			//******************************************************************************************
			
			//Motivo Cancelación Cita por Paciente
			case ConstantesBD.codigoReporteMotivoCancelacionCitaPaciente:
				
				where0 = " 1=1 ";
				
				//El Centro de Atención siempre es requerido
				if(parametros.containsKey("codigoCentroAtencion") 
						&& !parametros.get("codigoCentroAtencion").toString().equals(""))
				{
					where += " AND cc.centro_atencion = "+parametros.get("codigoCentroAtencion").toString().split(ConstantesBD.separadorSplit)[0];
					where0 += " AND cc.centro_atencion = "+parametros.get("codigoCentroAtencion").toString().split(ConstantesBD.separadorSplit)[0];
					parametrosbusqueda += " CENTRO ATENCIÓN ["+parametros.get("codigoCentroAtencion").toString().split(ConstantesBD.separadorSplit)[1]+"]     ";
				}
				
				//El Rango de Fechas siempre es requerido
				if(parametros.containsKey("fechaInicial") 
						&& !parametros.get("fechaInicial").toString().equals(""))
				{
					where += " AND to_char(sc.fecha_cita, 'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicial").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinal").toString())+"' ";
					where0 += " AND to_char(sc.fecha_cita, 'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicial").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinal").toString())+"' ";
					parametrosbusqueda += " RANGO DE FECHAS ["+parametros.get("fechaInicial").toString()+" - "+parametros.get("fechaFinal").toString()+" ]     ";			
				}
				
				//Especialidades 
				if(parametros.containsKey("codigoEspecialidad") 
						&& !parametros.get("codigoEspecialidad").toString().equals(""))
				{
					where += " AND sc.especialidad IN ("+parametros.get("codigoEspecialidad").toString()+") ";
					where0 += " AND sc.especialidad IN ("+parametros.get("codigoEspecialidad").toString()+") ";
					parametrosbusqueda += " ESPECIALIDADES ["+parametros.get("nombreEspecialidad").toString()+" ]     ";			
				}
				
				//Cita Cancelada por
				if(parametros.containsKey("codigoCanceladaPor") 
						&& !parametros.get("codigoCanceladaPor").toString().equals(""))
				{
					if(parametros.get("codigoCanceladaPor").toString().equals(ConstantesBD.codigoEstadoCitaCanceladaInstitucion+""))
						parametrosbusqueda += " CITA CANCELADA POR [Institución]     "; 
					else
						parametrosbusqueda += " CITA CANCELADA POR [Paciente]     ";
										
					where += " AND mcc.tipo_cancelacion = "+parametros.get("codigoCanceladaPor").toString()+" ";			
				}
				
				//Motivo de Cancelacion				
				if(parametros.containsKey("codigoMotivoCancelacion") 
						&& !parametros.get("codigoMotivoCancelacion").toString().equals(""))
				{
					parametrosbusqueda += " MOTIVO CANCELACIÓN ["+parametros.get("nombreMotivoCancelacion").toString()+" ]     ";
					where += " AND mcc.codigo IN("+parametros.get("codigoMotivoCancelacion").toString()+") ";			
				}
				
				where += " AND c.estado_cita IN ("+ConstantesBD.codigoEstadoCitaCanceladaInstitucion+","+
					 					    ConstantesBD.codigoEstadoCitaCanceladaPaciente+") ";
				
				where0 += " AND c.estado_cita IN ("+ConstantesBD.codigoEstadoCitaReprogramada+") ";
								
				respuesta.put("parametrosbusqueda",parametrosbusqueda);
				respuesta.put("where",where);			
				respuesta.put("where0",where0);
			break;
			
			//******************************************************************************************
			//******************************************************************************************
			
			//Motivo Cancelación Cita por Institucion
			case ConstantesBD.codigoReporteMotivoCancelacionCitaInstitucion:
				
				where0 = " 1=1 ";
				
				//El Centro de Atención siempre es requerido
				if(parametros.containsKey("codigoCentroAtencion") 
						&& !parametros.get("codigoCentroAtencion").toString().equals(""))
				{
					where += " AND cc.centro_atencion = "+parametros.get("codigoCentroAtencion").toString().split(ConstantesBD.separadorSplit)[0];
					where0 += " AND cc.centro_atencion = "+parametros.get("codigoCentroAtencion").toString().split(ConstantesBD.separadorSplit)[0];
					parametrosbusqueda += " CENTRO ATENCIÓN ["+parametros.get("codigoCentroAtencion").toString().split(ConstantesBD.separadorSplit)[1]+"]     ";
				}
				
				//El Rango de Fechas siempre es requerido
				if(parametros.containsKey("fechaInicial") 
						&& !parametros.get("fechaInicial").toString().equals(""))
				{
					where += " AND to_char(sc.fecha_cita, 'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicial").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinal").toString())+"' ";
					where0 += " AND to_char(sc.fecha_cita, 'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicial").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinal").toString())+"' ";
					parametrosbusqueda += " RANGO DE FECHAS ["+parametros.get("fechaInicial").toString()+" - "+parametros.get("fechaFinal").toString()+" ]     ";			
				}				
				
				//Cita Cancelada por
				if(parametros.containsKey("codigoCanceladaPor") 
						&& !parametros.get("codigoCanceladaPor").toString().equals(""))
				{
					if(parametros.get("codigoCanceladaPor").toString().equals(ConstantesBD.codigoEstadoCitaCanceladaInstitucion+""))
						parametrosbusqueda += " CITA CANCELADA POR [Institución]     ";
					else
						parametrosbusqueda += " CITA CANCELADA POR [Paciente]     ";
										
					where += " AND mcc.tipo_cancelacion = "+parametros.get("codigoCanceladaPor").toString()+" ";			
				}
				
				//Motivo de Cancelacion				
				if(parametros.containsKey("codigoMotivoCancelacion") 
						&& !parametros.get("codigoMotivoCancelacion").toString().equals(""))
				{
					parametrosbusqueda += " MOTIVO CANCELACIÓN ["+parametros.get("nombreMotivoCancelacion").toString()+" ]     ";
					where += " AND mcc.codigo IN("+parametros.get("codigoMotivoCancelacion").toString()+") ";			
				}
				
				//Profesional de la salud
				if(parametros.containsKey("codigoProfesionalSalud") 
						&& !parametros.get("codigoProfesionalSalud").toString().equals("")
							&& !parametros.get("codigoProfesionalSalud").toString().equals(ConstantesBD.codigoNuncaValido+""))
				{
					where += " AND a.codigo_medico IN ("+parametros.get("codigoProfesionalSalud").toString()+") ";
					parametrosbusqueda += " PROFESIONAL SALUD ["+parametros.get("nombreProfesionalSalud").toString()+" ]     ";			
					
					where0 += " AND a.codigo_medico IN ("+parametros.get("codigoProfesionalSalud").toString()+") ";					
				}
				
				where += " AND c.estado_cita IN ("+ConstantesBD.codigoEstadoCitaCanceladaInstitucion+","+
					 					    ConstantesBD.codigoEstadoCitaCanceladaPaciente+") ";
				
				where0 += " AND c.estado_cita IN ("+ConstantesBD.codigoEstadoCitaReprogramada+") ";
								
				respuesta.put("parametrosbusqueda",parametrosbusqueda);
				respuesta.put("where",where);			
				respuesta.put("where0",where0);
				
			break;
			
			//******************************************************************************************
			//******************************************************************************************
			
			case ConstantesBD.codigoReporteMotivoCancelacionCitaMes:
				
				//El Centro de Atención siempre es requerido
				if(parametros.containsKey("codigoCentroAtencion") 
						&& !parametros.get("codigoCentroAtencion").toString().equals(""))
				{
					where += " AND cc.centro_atencion = "+parametros.get("codigoCentroAtencion").toString().split(ConstantesBD.separadorSplit)[0];
					parametrosbusqueda += " CENTRO ATENCIÓN ["+parametros.get("codigoCentroAtencion").toString().split(ConstantesBD.separadorSplit)[1]+"]     ";
				}
				
				//El Rango de Fechas siempre es requerido
				if(parametros.containsKey("fechaInicial") 
						&& !parametros.get("fechaInicial").toString().equals(""))
				{
					where += " AND to_char(sc.fecha_cita, 'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicial").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinal").toString())+"' ";
					parametrosbusqueda += " RANGO DE FECHAS ["+parametros.get("fechaInicial").toString()+" - "+parametros.get("fechaFinal").toString()+" ]     ";			
				}
				
				//Cita Cancelada por
				if(parametros.containsKey("codigoCanceladaPor") 
						&& !parametros.get("codigoCanceladaPor").toString().equals(""))
				{
					if(parametros.get("codigoCanceladaPor").toString().equals(ConstantesBD.codigoEstadoCitaCanceladaInstitucion+""))
						parametrosbusqueda += " CITA CANCELADA POR [Institución]     ";
					else
						parametrosbusqueda += " CITA CANCELADA POR [Paciente]     ";
										
					where += " AND mcc.tipo_cancelacion = "+parametros.get("codigoCanceladaPor").toString()+" ";
				}	
				
				//Motivo de Cancelacion
				if(parametros.containsKey("codigoMotivoCancelacion") 
						&& !parametros.get("codigoMotivoCancelacion").toString().equals(""))
				{
					parametrosbusqueda += " MOTIVO CANCELACIÓN ["+parametros.get("nombreMotivoCancelacion").toString()+" ]     ";
					where += " AND c.motivo_cancelacion IN ("+parametros.get("codigoMotivoCancelacion").toString()+") ";
				}
								
				respuesta.put("dataset","dataset");
				respuesta.put("where",where);
				respuesta.put("parametrosbusqueda",parametrosbusqueda);
				respuesta.put("nombrearchivoreporte","MotivoCancelacionCitaMes.rptdesign");
				
			break;
			
			//******************************************************************************************
			//******************************************************************************************

			default:
				respuesta.put("dataset","dataset");
				respuesta.put("where"," 1=2 ");
				respuesta.put("parametrosbusqueda",parametrosbusqueda);
				respuesta.put("nombrearchivoreporte","");
			break;
		}	
				
		return respuesta;
	} 
	
	//************************************************************************************************
	
	/**
	 * @param Connection con
	 * @param String where  
	 * */
	public static HashMap getCargarDatosBasicosCancelacionCitas(Connection con,HashMap parametros)
	{
		String cadena = "";
		
		if(parametros.get("conProfesionales").toString().equals(ConstantesBD.acronimoNo))
			cadena = StrMotivoCancelacionCitaPac.replace("1=2",parametros.get("where").toString());
		else
			cadena = StrMotivoCancelacionCitaInst.replace("1=2",parametros.get("where").toString());
		
		cadena = cadena.replace("1=3",parametros.get("where0").toString());
		
		logger.info("valor de la cadena sql >> "+cadena);
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			ps.close();
			return mapaRetorno;

		}
		catch (SQLException e) 
		{
			logger.error("Error en getCargarDatosBasicosCancelacionCitas: "+e);
			return null;
		}
	}	
}