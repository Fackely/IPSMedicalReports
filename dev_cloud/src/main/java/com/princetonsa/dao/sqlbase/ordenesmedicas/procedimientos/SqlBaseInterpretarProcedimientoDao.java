package com.princetonsa.dao.sqlbase.ordenesmedicas.procedimientos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.ordenesmedicas.cirugias.HojaQuirurgica;


public class SqlBaseInterpretarProcedimientoDao
{

	/**
	 * 
	 */
	public static Logger logger=Logger.getLogger(SqlBaseInterpretarProcedimientoDao.class);
	
	/**
	 * Primera parte consulta de procedimientos
	 */
	private static String consultaProcedimientos_parte1="SELECT " +
													" s.numero_solicitud as solicitud," +
													" s.centro_costo_solicitante as ccsolicitante, " +
													" to_char(s.fecha_solicitud,'dd/mm/yyyy') as fechasolicitud," +
													" substr(s.hora_solicitud,0,6) as horasolicitud," +
													" s.consecutivo_ordenes_medicas as orden," +
													" to_char(sp.codigo_servicio_solicitado,'') as servicio," +
													" rs.descripcion as procedimiento," +
													" c.via_ingreso as codigoviaingreso," +
													" getnombreviaingreso(c.via_ingreso) as viaingreso," +
													" getnombrepersona(c.codigo_paciente) as nombrepaciente, " +													
													" s.estado_historia_clinica as codigoestadohc, " +
													" getestadosolhis(s.estado_historia_clinica) as estadohc," +
													" getcamacuenta(c.id,c.via_ingreso) as cama, " +
													" ser.requiere_interpretacion as requiereinterpretacion, " ;
													
													
	
	/**
	 * Segunda parte consulta de procedimientosd
	 */
	private static String consultaProcedimientos_parte2 = " s.tipo As tiposolicitud," +
		" '' As fecharespuesta, " +
		" '' As diagnosticorespuesta, " +
		" '"+ConstantesBD.codigoNuncaValido+"' As codigorespuesta, " +
		" '' As horarespuesta, " +
		" "+ConstantesBD.codigoNuncaValido+" As peticion," +
		" c.codigo_paciente As codigopaciente " +
	" from solicitudes s " +
	" inner join sol_procedimientos sp ON	(sp.numero_solicitud=s.numero_solicitud) " +
	" inner join servicios ser on (ser.codigo=sp.codigo_servicio_solicitado) " +
	" inner join cuentas c on(c.id=s.cuenta and c.estado_cuenta <> '"+ConstantesBD.codigoEstadoCuentaCerrada+"' and c.estado_cuenta <> '"+ConstantesBD.codigoEstadoCuentaFacturada+"') " +
	" inner join referencias_servicio rs on(rs.servicio=sp.codigo_servicio_solicitado and rs.tipo_tarifario='"+ConstantesBD.codigoTarifarioCups+"') " +
	" left outer join his_camas_cuentas hcc on(hcc.cuenta=s.cuenta) ";

	
	private static String strConsultaNoCruentos=" SELECT " +
														" s.numero_solicitud as solicitud," +
														" s.centro_costo_solicitante as ccsolicitante, " +
														" case when tiene_cita='"+ConstantesBD.acronimoSi+"' then to_char(sc.fecha_cita,'dd/mm/yyyy') else to_char(s.fecha_solicitud,'dd/mm/yyyy') end as fechasolicitud, " +
														" case when tiene_cita='"+ConstantesBD.acronimoSi+"' then sc.hora_inicio_cita else substr(s.hora_solicitud,0,6) end as horasolicitud," +
														" s.consecutivo_ordenes_medicas as orden," +
														" '"+ConstantesBD.codigoNuncaValido+"' as servicio," +
														" '' as procedimiento," +
														" c.via_ingreso as codigoviaingreso, " +
														" getnombreviaingreso(c.via_ingreso) as viaingreso," +
														" getnombrepersona(c.codigo_paciente) as nombrepaciente, " +
														" s.estado_historia_clinica as codigoestadohc, " +
														" getestadosolhis(s.estado_historia_clinica) as estadohc," +
														" getcamacuenta(c.id,c.via_ingreso) as cama, " +
														" '' as requiereinterpretacion, " +
														" 'true' as puedointerpretar, " +
														" s.tipo As tiposolicitud," +
														" coalesce (to_char(scx.fecha_salida_sala,'dd/mm/yyyy'),'') as fecharespuesta," +
														" '' As diagnosticorespuesta, " +
														" '"+ConstantesBD.codigoNuncaValido+"' As codigorespuesta, " +
														" coalesce (substr(scx.hora_salida_sala,0,6),'') as horarespuesta," +
														" scx.codigo_peticion As peticion, " +
														" c.codigo_paciente As codigopaciente " +
													" FROM solicitudes s " +
														" inner join solicitudes_cirugia scx ON (scx.numero_solicitud=s.numero_solicitud) " +
														" inner join sol_cirugia_por_servicio scxs on (scxs.numero_solicitud=s.numero_solicitud ) " +
														" inner join cuentas c on(c.id=s.cuenta and c.estado_cuenta <> '"+ConstantesBD.codigoEstadoCuentaCerrada+"') " +
														" left outer join servicios_cita sc on (sc.numero_solicitud=s.numero_solicitud) " +
														" left outer join his_camas_cuentas hcc on(hcc.cuenta=s.cuenta) "  +
													" WHERE scx.ind_qx='"+ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento+"'" +
															" AND getesfinalizadahqxhanes(s.numero_solicitud)=1 ";
													

	/**
	 * 
	 */
	private static String cadenaInterpretar="UPDATE solicitudes SET interpretacion =?, fecha_interpretacion=?,hora_interpretacion=?,codigo_medico_interpretacion=?, estado_historia_clinica=? where numero_solicitud=?";
	
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap listadoSolicitudesProcedimientosInterpretar(Connection con, HashMap vo)
	{
		HashMap mapa=new HashMap();
		boolean permitirIntepretarMultiple = UtilidadTexto.getBoolean(vo.get("permitirInterpretarMultiple").toString());
		mapa.put("numRegistros", "0");
		
		String cadena=consultaProcedimientos_parte1;
		
		
		///**********FILTRO PARA LAS ORDENES DE PROCEDIMIENTOS***********************************************
		if(!permitirIntepretarMultiple)
		{
			cadena += " case when ((sp.respuesta_multiple='"+ConstantesBD.acronimoSi+"' AND sp.finalizada_respuesta='"+ConstantesBD.acronimoSi+"' ) OR sp.respuesta_multiple='"+ConstantesBD.acronimoNo+"') then 'true' else 'false' end as puedointerpretar, " ;
		}
		else
		{
			cadena += " 'true'  as puedointerpretar, " ;
		}
		//********************************************************************************************************************************************
		
		cadena += consultaProcedimientos_parte2 + " WHERE 1=1 ";
		
		String cadena2=strConsultaNoCruentos;
		
		if(vo.containsKey("paciente")&&Utilidades.convertirAEntero(vo.get("paciente")+"")>0)
		{
			cadena+=" and c.codigo_paciente='"+vo.get("paciente")+"'";
			cadena2+=" and c.codigo_paciente='"+vo.get("paciente")+"'";
		}
		if(!(vo.get("fechaInicialFiltro")+"").equals("")&&!(vo.get("fechaFinalFiltro")+"").equals(""))
		{
			cadena+=" AND s.fecha_solicitud  between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicialFiltro")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinalFiltro")+"")+"'";
			cadena2+=" AND s.fecha_solicitud  between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicialFiltro")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinalFiltro")+"")+"'";
		}
		if(!(vo.get("centroCostroSolicitanteFiltro")+"").equals(""))
		{
			cadena+=" AND s.centro_costo_solicitante ='"+(vo.get("centroCostroSolicitanteFiltro")+"")+"'";
			cadena2+=" AND s.centro_costo_solicitante ='"+(vo.get("centroCostroSolicitanteFiltro")+"")+"'";
		}
		
		if(!(vo.get("areaFiltro")+"").equals(""))
		{
			cadena+=" AND c.area = '"+(vo.get("areaFiltro")+"")+"'";
			cadena2+=" AND c.area = '"+(vo.get("areaFiltro")+"")+"'";
		}
		if(!(vo.get("pisoFiltro")+"").equals(""))
		{
			cadena+=" AND hcc.codigopkpiso = '"+(vo.get("pisoFiltro")+"")+"'";
			cadena2+=" AND hcc.codigopkpiso = '"+(vo.get("pisoFiltro")+"")+"'";
		}
		if(!(vo.get("habitacionFiltro")+"").equals(""))
		{
			cadena+=" AND hcc.codigopkhabitacion = '"+(vo.get("habitacionFiltro")+"")+"'";
			cadena2+=" AND hcc.codigopkhabitacion = '"+(vo.get("habitacionFiltro")+"")+"'";
		}
		if(!(vo.get("camaFiltro")+"").equals(""))
		{
			cadena+=" AND hcc.codigocama = '"+(vo.get("camaFiltro")+"")+"'";
			cadena2+=" AND hcc.codigocama = '"+(vo.get("camaFiltro")+"")+"'";
		}
		
		//debe validarse con el centro de costo de la cuenta.
		//String subCadena=(vo.get("centroCostoIntentaAcceso")+"")+" IN (SELECT tc.centro_costo from tratantes_cuenta tc where tc.solicitud=s.numero_solicitud and tc.cuenta=s.cuenta and (tc.fecha_inicio<CURRENT_DATE or (tc.fecha_inicio=CURRENT_DATE and tc.hora_inicio<="+ValoresPorDefecto.getSentenciaHoraActualBD()+")) and (tc.fecha_fin IS NULL or tc.fecha_fin>CURRENT_DATE or (tc.fecha_fin=CURRENT_DATE and tc.hora_fin>="+ValoresXDefecto+") )  )                    ";
		//cadena+=" AND (s.centro_costo_solicitante ='"+(vo.get("centroCostoSolicitante")+"")+"' OR ("+subCadena+") )";
		
		
		//validacion de los tratantes. Y ADJUNTOS
		cadena+=" AND " +
				"( " +
					" c.area IN ( "+Utilidades.obtenerCentrosCostoUsuario(con,vo.get("loginUsurio")+"") +"  ) " +
					" OR " +
					" c.area IN ( select adjuntos_cuenta.centro_costo from adjuntos_cuenta where adjuntos_cuenta.cuenta=c.id and fecha_fin is null and hora_fin is null )" +
				" ) ";
	//añadido
		cadena2+=" AND " +
		"( " +
			" c.area IN ( "+Utilidades.obtenerCentrosCostoUsuario(con,vo.get("loginUsurio")+"") +"  ) " +
			" OR " +
			" c.area IN ( select adjuntos_cuenta.centro_costo from adjuntos_cuenta where adjuntos_cuenta.cuenta=c.id and fecha_fin is null and hora_fin is null )" +
		" ) ";
		
		if(!permitirIntepretarMultiple)
		{
			/**
			 * Si el parámetro no está activo solo se muestran procedimientos sin respuesta multiple o con respuesta múltiple finalizada
			 */
			cadena+=" AND s.estado_historia_clinica = '"+ConstantesBD.codigoEstadoHCRespondida+"' AND ((sp.respuesta_multiple='"+ConstantesBD.acronimoSi+"' AND sp.finalizada_respuesta='"+ConstantesBD.acronimoSi+"' ) OR sp.respuesta_multiple='"+ConstantesBD.acronimoNo+"')  ";
		}
		else
		{
			/**
			 * Si el parámetro está activo se permite consultar procedimientos en estado respondido
			 * o interpretados que aun no tengan la respuesta múltiple finalizada.
			 */
			cadena+=" AND s.estado_historia_clinica = '"+ConstantesBD.codigoEstadoHCRespondida+"'   ";
		}
		//*****************************************************************************************************
		//añadido
		cadena2+=" AND s.estado_historia_clinica = '"+ConstantesBD.codigoEstadoHCRespondida+"' ";

		
		if(vo.containsKey("requiereInt"))
			if(vo.get("requiereInt").equals(ConstantesBD.acronimoSi))
				cadena+=" AND ser.requiere_interpretacion = '"+ConstantesBD.acronimoSi+"' ";

		
		String cadenaTotal = " SELECT " +
										" solicitud, ccsolicitante, fechasolicitud, horasolicitud, orden, servicio, procedimiento, codigoviaingreso, " +
										" viaingreso, nombrepaciente, codigoestadohc, estadohc, cama, puedointerpretar, tiposolicitud,fecharespuesta," +
										" horarespuesta, peticion,codigopaciente " +
								" FROM (" +cadena+" " +
									"UNION " +
											" "+cadena2+" "+
								 
											" ) s";
		
		
		try
		{
			logger.info("\nCONSULTA DE SOLICITUDES\n"+cadenaTotal);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaTotal+" ORDER BY s.fechasolicitud,s.horasolicitud ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			String subQuery="";
			for(int i=0;i<Utilidades.convertirAEntero(mapa.get("numRegistros")+"");i++)
			{
				if (!(mapa.get("tiposolicitud_"+i)+"").equals(ConstantesBD.codigoTipoSolicitudCirugia+""))
				{
					subQuery="SELECT nombre_archivo as nombrearchivo , nombre_original as nombreoriginal from doc_adj_solicitud where numero_solicitud='"+mapa.get("solicitud_"+i)+"'";
					ps= new PreparedStatementDecorator(con.prepareStatement(subQuery,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					mapa.put("adjuntos_"+i, UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())));
				}
				else
				{
					HashMap adj = new HashMap();
					adj.put("numRegistros", "0");
					mapa.put("adjuntos_"+i, adj);
				}
				
						
				if (!(mapa.get("tiposolicitud_"+i)+"").equals(ConstantesBD.codigoTipoSolicitudCirugia+""))
				{
					subQuery="SELECT rsp.codigo as codigorespuesta,to_char(rsp.fecha_grabacion,'dd/mm/yyyy') as fecharespuesta,substr(rsp.hora_grabacion,0,6) as horarespuesta,case when dp.acronimo is null then '' else dp.acronimo end as diagnosticorespuesta from res_sol_proc rsp left outer join diag_procedimientos dp on (dp.codigo_respuesta=rsp.codigo and dp.principal="+ValoresPorDefecto.getValorTrueParaConsultas()+") where rsp.numero_solicitud='"+mapa.get("solicitud_"+i)+"' order by rsp.fecha_grabacion desc,rsp.hora_grabacion desc ";
					logger.info("\n l asubconsulta de  procedimientos es -->"+subQuery);
					ps= new PreparedStatementDecorator(con.prepareStatement(subQuery,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ResultSetDecorator rsTemp=new ResultSetDecorator(ps.executeQuery());
					if(rsTemp.next())
					{
						mapa.put("codigorespuesta_"+i, rsTemp.getString("codigorespuesta"));
						mapa.put("fecharespuesta_"+i, rsTemp.getString("fecharespuesta"));
						mapa.put("horarespuesta_"+i, rsTemp.getString("horarespuesta"));
						mapa.put("diagnosticorespuesta_"+i, rsTemp.getString("diagnosticorespuesta"));
					}
				
				}
				else
				{
					subQuery="SELECT  scxc.codigo As codigo_cx_por_serv, scxc.servicio As servicio,  getnombreservicio(scxc.servicio,"+ConstantesBD.codigoTarifarioCups+") As procedimiento FROM sol_cirugia_por_servicio scxc"+
							 " WHERE scxc.numero_solicitud="+mapa.get("solicitud_"+i)+"";
					logger.info("\n l asubconsulta de  nocruentos es -->"+subQuery);
					ps= new PreparedStatementDecorator(con.prepareStatement(subQuery,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
					HashMap servicios = new HashMap  ();
					servicios=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
					
					for (int j=0;j<Utilidades.convertirAEntero(servicios.get("numRegistros")+"");j++ )
					{
						servicios.put("descQx_"+j, HojaQuirurgica.consultarDescripcionesQx(con, servicios.get("codigoCxPorServ_"+j)+""));
					}
					
					mapa.put("servicio_"+i, servicios);
				}
				
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param interpretacion
	 * @param codigoPersona
	 * @return
	 */
	public static boolean interpretarSolicitud(Connection con, String numeroSolicitud, String interpretacion, int codigoPersona)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInterpretar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE solicitudes SET 
			 * interpretacion =?, 
			 * fecha_interpretacion=?,
			 * hora_interpretacion=?,
			 * codigo_medico_interpretacion=?, 
			 * estado_historia_clinica=? 
			 * where numero_solicitud=?
			 */
			
			ps.setString(1, interpretacion);
			ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(3, UtilidadFecha.getHoraActual());
			ps.setInt(4, Utilidades.convertirAEntero(codigoPersona+""));
			ps.setInt(5, ConstantesBD.codigoEstadoHCInterpretada);
			ps.setInt(6, Utilidades.convertirAEntero(numeroSolicitud));
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

}
