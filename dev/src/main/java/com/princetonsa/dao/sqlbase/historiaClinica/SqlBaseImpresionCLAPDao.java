package com.princetonsa.dao.sqlbase.historiaClinica;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.ConstantesBDHistoriaClinica;

public class SqlBaseImpresionCLAPDao
{
	
	/**
	 * 
	 */
	public static Logger logger=Logger.getLogger(SqlBaseImpresionCLAPDao.class);
	
	
	/**
	 * 
	 */
	public static String consultaAdmisionesSolicitudesCirugiaParto="SELECT " +
																		" case when ah.codigo is null then au.codigo else ah.codigo end as admision," +
																		" case when ah.codigo is null then to_char(au.fecha_admision,'dd/mm/yyyy') else to_char(ah.fecha_admision,'dd/mm/yyyy') end as fechaadmision," +
																		" case when ah.codigo is null then substr(au.hora_admision,0,6) else substr(ah.hora_admision,0,6) end as horaadmision," +
																		" s.consecutivo_ordenes_medicas as solicitud," +
																		" getnombrepersona(s.codigo_medico_interpretacion) as medicointerpreto," +
																		" c.id as cuenta," +
																		" s.numero_solicitud as numerosolicitud," +
																		" scxs.codigo as codigocirugia," +
																		" p.tipo_identificacion as tipoidmadre," +
																		" p.numero_identificacion as numeroidmadre," +
																		" getnombrepersona(p.codigo) as nombremadre " +
																	" from solicitudes s " +
																	" inner join sol_cirugia_por_servicio scxs on(s.numero_solicitud=scxs.numero_solicitud) " +
																	" inner join servicios ser on(scxs.servicio=ser.codigo AND ser.tipo_servicio='"+ConstantesBD.codigoServicioPartosCesarea+"') " +
																	" inner join cuentas c on(s.cuenta=c.id) " +
																	" inner join personas p on(p.codigo=c.codigo_paciente) " +
																	" left outer join admisiones_hospi ah on(ah.cuenta=c.id) "+
																	" left outer join admisiones_urgencias au on (au.cuenta=c.id) " +
																	" where s.estado_historia_clinica="+ConstantesBD.codigoEstadoHCInterpretada+" ";


	
	/**
	 * 
	 */
	private static String cadenaInsertarLog="INSERT INTO logimpresionclap (codigo,codigo_paciente,cuenta,fecha,hora,login_usuario,codigo_centro_atencion,nombre_centro_atencion ) values (?,?,?,?,?,?,?,?)";
	
	
	/**
	 * 
	 */
	private static String cadenaConsultaInformacionParto="SELECT consultas_prenatales as numeroconsultas,corticoides_antenatales as corticoidesantenatales,inicio_t_parto as inciiotparto,semana_gestacional as semanasgestacional,case when dias_gestacional is null then 0 else dias_gestacional end as diasgestacional,por_gestacional as porgestacional,presentacion as presentacion,acompanante,terminacion,episiotomia ,enema_rasurado as enemarasurado,desgarros,grado_desgarros as gradosdesgarro,oxitocicos_alumbramiento as oxitocicosalumbramiento,placenta,ligadura_cordon,case when pc.consecutivo_info_parto is null then 'false' else 'true' end as partograma from informacion_parto ip left outer join partograma_clap pc on(pc.consecutivo_info_parto=ip.consecutivo) where cirugia=?";
	
	
	/**
	 * 
	 */
	private static String cadenaConsultaInfoRecienNacido="SELECT " +
																" consecutivo as codigoiph, " +
																" s.nombre as sexo," +
																" to_char(fecha_nacimiento,'dd/mm/yyyy') as fechanacimiento," +
																" substr(hora_nacimiento,0,6) as horanacimiento," +
																" case when vivo='true' then 'S' else 'N' end as vivo," +
																" edad_gesta_examen as edadgestacionalconfiable," + 
																" peso_edad_gestacion as pesoedadgestacional," +
																" sensibilizado as sensibilizado," +
																" ts.nombre as hemoclasificacion," +
																" case when fallece_sala_parto is null then 'N' else fallece_sala_parto end as fallecesalaparto," +
																" conducta_seguir as conductaseguir," +
																" defectos_congenitos as defectoscongenitos," +
																" acro_diag_def_cong as codigocupsdefcong," +
																" vitamina_k as vitaminak," +
																" profilaxis_oftalmico as profilaxisoftalmica," +
																" getDatosMedicoEspecialidades((select login from usuarios where codigo_persona=codigo_profesional_atendio "+ValoresPorDefecto.getValorLimit1()+" 1),'-') as profesionalatendio," +
																" getocupacion(m.ocupacion_medica) as ocupacionprofesional," +
																" to_char(fecha_egreso,'dd/mm/yyyy') as fechaegreso," +
																" substr(hora_egreso,0,6) as horaegreso," +
																" condicion_egreso as codicionegreso," +
																" '' as edad," +
																" lactancia as lactancia," +
																" peso_egreso as pesoegreso," +
																" vacuna_polio as vpolio," +
																" vacuna_bcg as vbcg," +
																" vacuna_hepatitis_b as vhepatitisb," +
																" nuip as nuip," +
																" codigo_enfermedad as codigoenfermedad," +
																" sano_enfermo as sano," +
																" conducta_seguir_ani as conductaseguirani " +
														" from info_parto_hijos iph " +
														" LEFT OUTER JOIN tipos_sangre ts on(iph.hemoclasificacion=ts.codigo) " +  //TOCA UN LEFT OUTER JOIN POR LAS INCONSISTENCIAS CON LA INFORMACION VIEJA
														" LEFT OUTER JOIN medicos m on(m.codigo_medico=iph.codigo_profesional_atendio)  " + //TOCA UN LEFT OUTER JOIN POR LAS INCONSISTENCIAS CON LA INFORMACION VIEJA
														" inner join sexo s on(iph.sexo=s.codigo) " +
														" where cirugia = ?";
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap consultarSolicitudes(Connection con, HashMap vo)
	{
		HashMap resultado=new HashMap();
		resultado.put("numRegistros", "0");
		String consulta=consultaAdmisionesSolicitudesCirugiaParto;
		PreparedStatementDecorator ps= null;
		if(vo.containsKey("codigoPaciente"))
		{
			consulta=consulta+" AND c.codigo_paciente="+vo.get("codigoPaciente");
		}
		consulta=consulta+" ORDER BY ah.fecha_admision asc,ah.hora_admision asc ";
		
		logger.info("CONSULTA -->"+consulta);
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}	if (ps != null){
			try{
				ps.close();
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
			}
		}
		return resultado;
	}


	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean generarRegistroLogImpresion(Connection con, HashMap vo)
	{
		PreparedStatementDecorator ps=null;
		Integer nextVal = new Integer(0);
		PreparedStatement pst= null;
		ResultSet rs = null;
		try
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarLog));
			
			String consultaSecuencia = " select  historiaclinica.seq_logimpresionclap.nextval from dual  "; 
			
			 pst = con.prepareStatement(consultaSecuencia);
			 rs= pst.executeQuery();
			
			if(rs.next()){
				nextVal=rs.getInt("nextval");
			}
			
			ps.setInt(1,nextVal);
			ps.setInt(2,Utilidades.convertirAEntero(vo.get("paciente")+""));
			ps.setInt(3,Utilidades.convertirAEntero(vo.get("cuenta")+""));
			ps.setDate(4,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fecha")+"")));
			ps.setString(5,vo.get("hora")+"");
			ps.setString(6,vo.get("usuario")+"");
			ps.setInt(7,Utilidades.convertirAEntero(vo.get("codigocentroatencion")+""));
			ps.setString(8,vo.get("centroatencion")+"");
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			logger.error("ERROR GENERANDO EL LOG TIPO BD DE LA IMPRESION DEL CLAP");
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
			if (pst != null){
				try{
					pst.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
			
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
		}
		return false;
	}
	
	
	

	/**
	 * 
	 * @param con
	 * @param codigoCirugia
	 * @return
	 */
	@SuppressWarnings("unused")
	public static HashMap consultarInformacionRecienNacido(Connection con, String codigoCirugia, String codigoPaciente)
	{
		// cadenaConsultaInfoRecienNacido
		HashMap resultado=new HashMap();
		resultado.put("numRegistros", "0");
		String consulta=cadenaConsultaInfoRecienNacido;
		consulta=consulta+" ORDER BY fecha_nacimiento asc,hora_nacimiento asc ";
		logger.info(consulta.replace("?", codigoCirugia));
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs = null;
		
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setInt(1, Utilidades.convertirAEntero(codigoCirugia));
			resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			for(int i=0;i<Utilidades.convertirAEntero(resultado.get("numRegistros")+"");i++)
			{
				String fechaNacimiento="";
				String fur="";
				ps= new PreparedStatementDecorator(con.prepareStatement("select numero_embarazo from informacion_parto where cirugia="+codigoCirugia));
				 rs=new ResultSetDecorator(ps.executeQuery());
				if(rs.next())
				{
					if(rs.getObject(1)!=null&&!rs.getString(1).trim().equals(""))
					{
						ps= new PreparedStatementDecorator(con.prepareStatement("SELECT to_char(fur,'dd/mm/yyyy') from hoja_obstetrica where paciente='"+codigoPaciente+"' and embarazo ='"+rs.getString(1)+"'"));
						ResultSetDecorator rs1=new ResultSetDecorator(ps.executeQuery());
						if(rs1.next())
							fur=rs1.getString(1);
					}
				}
				fechaNacimiento=resultado.get("fechanacimiento_"+i)+"";

				if(fechaNacimiento!=null&&!fechaNacimiento.equalsIgnoreCase("null")&&!fechaNacimiento.trim().equals(""))
				{
					String[] vFN=fechaNacimiento.split("/");
					String[] vFA=UtilidadFecha.getFechaActual().split("/");
					resultado.put("edad_"+i, UtilidadFecha.calcularEdadDetallada(Utilidades.convertirAEntero(vFN[2]), Utilidades.convertirAEntero(vFN[1]), Utilidades.convertirAEntero(vFN[0]), Utilidades.convertirAEntero(vFA[0]), Utilidades.convertirAEntero(vFA[1]), Utilidades.convertirAEntero(vFA[2])));
				}
				
				ps= new PreparedStatementDecorator(con.prepareStatement("SELECT valor from info_part_exam_fisico where codigo_iph = '"+resultado.get("codigoiph_"+i)+"' and codigo_campo='"+ConstantesBDHistoriaClinica.codigoCampoExamenFisicoPeso+"' "));
				rs=new ResultSetDecorator(ps.executeQuery());
				if(rs.next())
				{
					resultado.put("peso_"+i, rs.getString(1)+"");
				}
				else
				{
					resultado.put("peso_"+i,"");
				}
				
				ps= new PreparedStatementDecorator(con.prepareStatement("SELECT valor from info_part_exam_fisico where codigo_iph = '"+resultado.get("codigoiph_"+i)+"' and codigo_campo='"+ConstantesBDHistoriaClinica.codigoCampoExamenFisicoTalla+"' "));
				rs=new ResultSetDecorator(ps.executeQuery());
				if(rs.next())
				{
					resultado.put("talla_"+i, rs.getString(1)+"");
				}
				else
				{
					resultado.put("talla_"+i,"");
				}

				ps= new PreparedStatementDecorator(con.prepareStatement("SELECT valor from info_part_exam_fisico where codigo_iph = '"+resultado.get("codigoiph_"+i)+"' and codigo_campo='"+ConstantesBDHistoriaClinica.codigoCampoExamenFisicoPerimetroCefalico+"' "));
				rs=new ResultSetDecorator(ps.executeQuery());
				if(rs.next())
				{
					resultado.put("perimetrocefalico_"+i, rs.getString(1)+"");
				}
				else
				{
					resultado.put("perimetrocefalico_"+i,"");
				}
				//cargar la secciona apgar.
				ps= new PreparedStatementDecorator(con.prepareStatement("SELECT ca.descripcion as campo,ipsa.valor as valor,ipsa.riesgo as riesgo from info_part_seccion_apgar ipsa inner join campos_apgar ca on(ipsa.codigo_campo=ca.codigo) where ipsa.codigo_iph='"+resultado.get("codigoiph_"+i)+"'"));
				resultado.put("apgar_"+i, UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())));
				
				//cargar la secciona reanimacion.
				ps= new PreparedStatementDecorator(con.prepareStatement("SELECT cr.descripcion as campo,ipr.valor as valor from info_parto_reanimacion ipr  inner join campos_reanimacion cr on(ipr.codigo_campo=cr.codigo) where ipr.codigo_iph='"+resultado.get("codigoiph_"+i)+"'"));
				resultado.put("reanimacion_"+i, UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())));

				//cargar la secciona tamizacion.
				ps= new PreparedStatementDecorator(con.prepareStatement("SELECT ctn.descripcion as campo,iptn.valor as valor from info_part_tam_neo iptn inner join campos_tamizacion_neonatal ctn on(iptn.codigo_campo=ctn.codigo) where iptn.codigo_iph='"+resultado.get("codigoiph_"+i)+"'"));
				resultado.put("tamizacion_"+i, UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())));
				
				

			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
		}
		return resultado;
	}

	
//	-------------------------------------- MANIZALES ---------------------------------------------------//
	
	
	/**
	 * Metodo que consulta la información obstetrica del paciente
	 * @param con -> Conexion
	 * @param parametros -> Mapa que contiene los parametros de la consulta
	 */
	public static HashMap consultarInformacionObstetrica(Connection con, HashMap parametros)
	{
		HashMap resultado=new HashMap();
		resultado.put("numRegistros", "0");
		
		String consulta="";
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		
		int nroConsulta=UtilidadCadena.vInt(parametros.get("nroConsulta")+"");
		
		switch (nroConsulta)
		{
		//-----Se consulta la información General de la hoja obstetrica para el paciente----//
		case 1: 
			consulta="SELECT ho.embarazo AS embarazo, to_char(ho.fur,'DD/MM/YYYY') AS fur, ho.confiable AS confiable, to_char(ho.fpp,'DD/MM/YYYY') AS fpp, " +
					 "		 ho.vigente_antitetanica AS vigente_antitetanica, ho.dosis_antitetanica AS dosis_antitetanica, " +
					 "		 ho.mes_gestacion_antitetanica AS mes_gestacion,ho.antirubeola AS antirubeola, "+
					"CASE WHEN ho.peso IS NULL THEN '' ELSE ho.peso || '' END AS peso, " +
					"CASE WHEN ho.talla IS NULL THEN '' ELSE ho.talla || '' END AS talla, " +
					"CASE WHEN ho.embarazo_deseado IS NULL THEN '' ELSE ho.embarazo_deseado END AS embarazo_deseado," +
					"CASE WHEN ho.embarazo_planeado IS NULL THEN '' ELSE ho.embarazo_planeado END AS embarazo_planeado "+
					 "			FROM hoja_obstetrica ho "+ 
					 " 				WHERE ho.paciente="+parametros.get("codigoPaciente")+ 
					 " 					  AND ho.embarazo="+parametros.get("numeroEmbarazo");
			break;
			
		//---- Se consulta los encabezados historicos de resumen gestacional para mostrar en Consulta y Controles -------//
		case 2:
			consulta="SELECT rg.codigo AS codigo_enca, to_char(rg.fecha,'DD/MM/YYYY') AS fecha, " +
					"		 rg.edad_gestacional AS edad_gestacional "+ 
					"	FROM resumen_gestacional rg "+
					"		INNER JOIN hoja_obstetrica ho ON (ho.codigo=rg.hoja_obstetrica) "+
					"			WHERE ho.paciente="+parametros.get("codigoPaciente")+
					"				AND ho.embarazo="+parametros.get("numeroEmbarazo")+			
					"					ORDER BY rg.fecha,rg.hora";
			break;
		//---- Se consulta el detalle de la informacion historica de resumen gestacional para mostrar en Consulta y Controles -------//
		case 3:
			consulta="SELECT drg.codigo_resumen_gest AS codigo_enca, drg.tipo_resumen_gest AS tipo_resumen_gest, " +
					 "		 trg.nombre AS nombre_resumen_gest, drg.valor AS valor "+
					 "	FROM detalle_resumen_gest drg "+
					 "		INNER JOIN resumen_gest_institucion rgi ON (drg.tipo_resumen_gest=rgi.codigo) "+
					 "		INNER JOIN tipos_resumen_gestacional trg ON (rgi.tipo_resumen_gestacional=trg.codigo) "+
					 "			WHERE drg.codigo_resumen_gest IN ("+parametros.get("codigosEnca")+")" +
					 		"		ORDER BY drg.codigo_resumen_gest";
			break;
		
		default :
			logger.warn(" [ERROR] No esta indicando ningun tipo de consulta el rango normal es [1-4]"+ nroConsulta + "\n\n" );
			return null;
			
		}//switch
		
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error en consultarInformacionObstetrica  Nro Consulta ["+nroConsulta+"] "+e.toString());
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
		}
		
		return resultado;
	}


}
