/*
 */
package com.princetonsa.dao.sqlbase.historiaClinica;

import groovy.sql.Sql;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;


/**
 * @author Jorge Armando Osorio Velasquez.
 */
public class SqlBaseInformacionRecienNacidosDao
{

	/**
	 * 
	 */
	public static Logger logger=Logger.getLogger(SqlBaseInformacionRecienNacidosDao.class);
	
	
	/**
	 * 
	 */
	public static String consultaAdmisionesSolicitudesCirugiaParto="SELECT " +
																		" DISTINCT " +
																		" case when ah.codigo is null then au.codigo else ah.codigo end as admision," +
																		" case when ah.codigo is null then to_char(au.fecha_admision,'dd/mm/yyyy') else to_char(ah.fecha_admision,'dd/mm/yyyy') end as fechaadmision," +
																		" case when ah.codigo is null then substr(au.hora_admision,0,6) else substr(ah.hora_admision,0,6) end as horaadmision," +
																		" s.consecutivo_ordenes_medicas as solicitud," +
																		" getnombrepersona(s.codigo_medico_interpretacion) as medicointerpreto," +
																		" c.id as cuenta," +
																		" s.numero_solicitud as numerosolicitud," +
																		" (SELECT scxs1.codigo FROM sol_cirugia_por_servicio scxs1 inner join servicios ser1 on(scxs1.servicio=ser1.codigo AND ser1.tipo_servicio='"+ConstantesBD.codigoServicioPartosCesarea+"')  WHERE scxs1.numero_solicitud=scxs.numero_solicitud "+ValoresPorDefecto.getValorLimit1()+" 1) as codigocirugia," +
																		" p.tipo_identificacion as tipoidmadre," +
																		" p.numero_identificacion as numeroidmadre," +
																		" getnombrepersona(p.codigo) as nombremadre," +
																		" p.codigo as codigopaciente " +
																	" from solicitudes s " +
																	" inner join solicitudes_cirugia sciru ON(sciru.numero_solicitud = s.numero_solicitud) " +
																	" inner join sol_cirugia_por_servicio scxs on(s.numero_solicitud=scxs.numero_solicitud) " +
																	" inner join servicios ser on(scxs.servicio=ser.codigo AND ser.tipo_servicio='"+ConstantesBD.codigoServicioPartosCesarea+"') " +
																	" inner join cuentas c on(s.cuenta=c.id) " +
																	" inner join personas p on(p.codigo=c.codigo_paciente) " +
																	" left outer join admisiones_hospi ah on(ah.cuenta=c.id) "+
																	" left outer join admisiones_urgencias au on (au.cuenta=c.id) " +
																	" where " +
																	"(sciru.ind_qx='"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia+"' " +
																	" OR sciru.ind_qx='"+ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento+"') AND " +	
																	"s.estado_historia_clinica<>"+ConstantesBD.codigoEstadoHCAnulada+" ";
	
	/**
	 * 
	 */
	private static String consultaHijosCirugia="SELECT " +
													" iph.consecutivo as consecutivo," +
													" iph.consecutivo_hijo as numerohijo," +
													" to_char(iph.fecha_nacimiento,'dd/mm/yyyy') as fechanacimiento," +
													" substr(iph.hora_nacimiento,0,6) as horanacimiento," +
													" sexo.nombre as sexo," +
													" getnombrepersona(codigo_profesional_atendio) as medicoatendio " +
											" from info_parto_hijos iph " +
											" left outer join sexo on(sexo.codigo=iph.sexo) " +
											" where cirugia=? ORDER BY iph.consecutivo_hijo";
	
	
	/**
	 * 
	 */
	
	private static String consultarHijo="SELECT " +
												" consecutivo as codigo, " +
												" case when fecha_nacimiento is null then '' else to_char(fecha_nacimiento,'dd/mm/yyyy') end as fechanacimiento," +
												//" substr(hora_nacimiento,0,6) as horanacimiento," +
												" hora_nacimiento as horanacimiento," +
												" case when sexo is null then '' else sexo||'' end as sexo," +
												" case when vivo is null then '' when vivo='true' then 'true' else 'false' end as vivo," +
												" diagnostico_rn as diagrn," +
												" case when cie_rn is null then '' else cie_rn||'' end as ciediagrn," +
												" getdiagnostico(diagnostico_rn,cie_rn) as nombrediagrn," +
												" case when fecha_muerte is not null then to_char(fecha_muerte,'dd/mm/yyyy') else '' end as fechamuerte," +
												//" case when hora_muerte is not null then substring(hora_muerte||'',0,6) else '' end as horamuerte," +
												" hora_muerte as horamuerte," +
												" diagnostico_muerte as diagmuerte," +
												" case when cie_muerte is null then '' else cie_muerte||'' end as  ciediagmuerte," +
												" getdiagnostico(diagnostico_muerte,cie_muerte) as nombrediagmuerte," +
												" momento_muerte as momentomuerte," +
												" fallece_sala_parto as fallecesalaparto," +
												" peso_edad_gestacion as pesoedadgestacion," +
												" vitamina_k as vitaminak," +
												" profilaxis_oftalmico as profilaxisoftalmico," +
												" case when hemoclasificacion is null then '' else hemoclasificacion||'' end as hemoclasificacion," +
												" sensibilizado as sensibilizado," +
												" defectos_congenitos as defectoscongenitos," +
												" acro_diag_def_cong as diagdefcong," +
												" case when tipo_cie_diag_def_cong is null then '' else tipo_cie_diag_def_cong||'' end as ciediagdefcong," +
												" getdiagnostico(acro_diag_def_cong,tipo_cie_diag_def_cong) as nomdiagdefcong," +
												" case when fecha_egreso is null then '' else to_char(fecha_egreso,'dd/mm/yyyy') end as fechaegreso," +
												" hora_egreso as horaegreso," +
												" condicion_egreso as condicionegreso," +
												" lactancia as lactancia," +
												" case when peso_egreso is null then '' else peso_egreso||'' end as pesoegreso," +
												" nuip as nuip," +
												" vacuna_polio as vacunapolio," +
												" vacuna_bcg as vacunabcg," +
												" vacuna_hepatitis_b as vacunahepatitisb," +
												" sano_enfermo as sanoenfermo," +
												" conducta_seguir as conductaseguir," +
												" case when codigo_profesional_atendio is null then '' else codigo_profesional_atendio||'' end as codprofatendio," +
												" conducta_seguir_ani as conductaseguirani," +
												" case when edad_gesta_examen is null then '' else edad_gesta_examen||'' end as edadgestacionexamen," +
												" observaciones_egreso as observacionesegreso, " +
												" numero_cert_nacimiento as numerocertificadonacimiento," +
												" finalizada as finalizada," +
												" case when codigo_enfermedad is null then '' else codigo_enfermedad end as codigoenfermedad " +
										" from info_parto_hijos " +
										" where consecutivo=?";
	
	
	
	/**
	 * cadena para actualizar el registro.
	 */
	private static String actualizarRegistro="UPDATE info_parto_hijos set " +
											" fecha_nacimiento=?," +
											" hora_nacimiento=?," +
											" sexo=?," +
											" vivo=?," +
											" diagnostico_rn=?," +
											" cie_rn=?," +
											" fecha_muerte=?," +
											" hora_muerte=?," +
											" diagnostico_muerte=?," +
											" cie_muerte=?," +
											" cirugia=?," +
											" fallece_sala_parto=?," +
											" momento_muerte=?," +
											" peso_edad_gestacion=?," +
											" vitamina_k=?," +
											" profilaxis_oftalmico=?," +
											" hemoclasificacion=?," +
											" sensibilizado=?," +
											" defectos_congenitos=?," +
											" acro_diag_def_cong=?," +
											" tipo_cie_diag_def_cong=?," +
											" fecha_egreso=?," +
											" hora_egreso=?," +
											" condicion_egreso=?," +
											" lactancia=?," +
											" peso_egreso=?," +
											" nuip=?," +
											" vacuna_polio=?," +
											" vacuna_bcg=?," +
											" vacuna_hepatitis_b=?," +
											" sano_enfermo=?," +
											" conducta_seguir=?," +
											" observaciones_egreso=?," +
											" codigo_profesional_atendio=?," +
											" conducta_seguir_ani=?," +
											" edad_gesta_examen=?," +
											" numero_cert_nacimiento=?," +
											" finalizada=?," +
											" codigo_enfermedad=? " +
										" WHERE consecutivo=?";
	
	/**
	 * 
	 */
	private static String consultaDiagEgreso="SELECT " +
												" codigo as codigo," +
												" acronimo_diagnostico as acrdiag," +
												" tipo_cie_diagnostico as ciediag," +
												" tipo_diagnostico as tipodiag," +
												" principal as principal," +
												" getdiagnostico(acronimo_diagnostico,tipo_cie_diagnostico) as nombreDiagnostico " +
											" from " +
												" diag_egreso_rn  " +
											" where codigo_iph=? and principal=?" +
											" order by principal desc, codigo asc";

	/**
	 * 
	 */
	private static String consultaReanimacion="SELECT " +
												" cr.codigo as campo," +
												" cr.descripcion as descripcioncampo," +
												" case when ipr.valor is null then '' else ipr.valor end as valor " +
											" from campos_reanimacion cr " +
											" left outer join info_parto_reanimacion ipr on(ipr.codigo_campo=cr.codigo and ipr.codigo_iph=?) " +
											" order by cr.codigo";
	
	/**
	 * 
	 */
	private static String consultaTamizacion="SELECT " +
												" tn.codigo as campo," +
												" tn.descripcion as descripcioncampo," +
												" case when iptn.valor is null then '' else iptn.valor end as valor " +
											" from campos_tamizacion_neonatal tn " +
											" left outer join info_part_tam_neo iptn on(iptn.codigo_campo=tn.codigo and iptn.codigo_iph=?) " +
											" order by tn.codigo";
	
	
	/**
	 * 
	 */
	private static String consultaAdapatacionNeonatalInmediata="SELECT " +
																	" caii.codigo_campo as campo," +
																	" cai.descripcion as descripcioncampo," +
																	" case when caiph.valor is null then '' else caiph.valor end as valor," +
																	" caii.institucion as institucion " +
																" from campos_adap_neo_inmediata cai " +
																" inner join camp_adp_inmed_inst caii on(caii.codigo_campo=cai.codigo and caii.activo='"+ConstantesBD.acronimoSi+"') " +
																" left outer join info_part_adap_neo_inmediata caiph on (caiph.codigo_campo=caii.codigo_campo and caiph.institucion=caii.institucion and caiph.codigo_iph=?) " +
																" where caii.institucion=? " +
																" order by cai.codigo"; 
	
	/**
	 * 
	 */
	private static String consultaExamenesFisicos="SELECT " +
														" efri.codigo_campo as campo," +
														" efr.descripcion as descripcioncampo," +
														" case when ipef.valor is null then '' else ipef.valor end as valor," +
														" case when ipef.descripcion is null then '' else ipef.descripcion end as descripcionvalor " +
												" from examenes_fisicos_rn efr " +
												" inner join exam_fis_rn_inst efri on(efri.codigo_campo=efr.codigo and efri.activo='"+ConstantesBD.acronimoSi+"') " +
												" left outer join info_part_exam_fisico ipef on(ipef.codigo_campo=efri.codigo_campo and ipef.institucion=efri.institucion and ipef.codigo_iph=?) " +
												" where efri.institucion=? " +
												" order by efri.codigo_campo ";
	
	/**
	 * 
	 */
	private static String consultaDiagnosticoRecienNacido="SELECT " +
																" cdrni.codigo_campo as campo," +
																" cdrn.descripcion as descripcioncampo, " +
																" case when ipdrn.valor is null then '' else ipdrn.valor end as valor " +
															" from campos_diag_rn cdrn " +
															" inner join camp_diag_rn_inst cdrni on(cdrn.codigo=cdrni.codigo_campo and cdrni.activo='"+ConstantesBD.acronimoSi+"') " +
															" left outer join info_part_diag_rn ipdrn on(cdrni.codigo_campo=ipdrn.codigo_campo and ipdrn.institucion=cdrni.institucion and ipdrn.codigo_iph=?) " +
															" where cdrni.institucion=? " +
															" order by cdrni.codigo_campo ";
	
	/**
	 * 
	 */
	private static String consultaSano="SELECT " +
												" csi.codigo_campo as campo," +
												" cs.descripcion as descripcioncampo, " +
												" case when ips.valor is null then '' else ips.valor end as valor  " +
									" from campos_sano cs " +
									" inner join campos_sano_inst csi on(cs.codigo=csi.codigo_campo and csi.activo='"+ConstantesBD.acronimoSi+"') " +
									" left outer join info_part_sano ips on(csi.codigo_campo=ips.codigo_campo and csi.institucion=ips.institucion and ips.codigo_iph=?) " +
									" where csi.institucion=? order by csi.codigo_campo";

	
	/**
	 * 
	 */
	private static String consultaApgar=" SELECT " +
											" cai.codigo_campo as campo," +
											" ca.descripcion as descripcioncampo, " +
											" case when ipca.valor is null then '' else ipca.valor||'' end as valor," +
											" case when ipca.riesgo is null then '' else ipca.riesgo end as riesgo  " +
										" from campos_apgar ca " +
										" inner join campos_apgar_inst cai on(ca.codigo=cai.codigo_campo and cai.activo='"+ConstantesBD.acronimoSi+"') " +
										" left outer join info_part_seccion_apgar ipca on(cai.codigo_campo=ipca.codigo_campo and cai.institucion=ipca.institucion and ipca.codigo_iph=?) " +
										" where cai.institucion=? " +
										" order by cai.codigo_campo";
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
		if(vo.containsKey("codigoPaciente"))
		{
			consulta=consulta+" AND c.codigo_paciente="+vo.get("codigoPaciente");
		}
		else if(vo.containsKey("fechaInicial")&&vo.containsKey("fechaFinal"))
		{
			consulta=consulta+" AND (ah.fecha_admision between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"' " +
									" OR " +
									" au.fecha_admision between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"' )";
		}
		consulta=consulta+" ORDER BY fechaadmision asc, horaadmision asc ";
		
		logger.info("\n\n consulta -->"+consulta+"\n\n");
		PreparedStatementDecorator ps= null;
		
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
			
		}
		return resultado;
	}

	
	/**
	 * 
	 * @param con
	 * @param cirugia
	 * @return
	 */
	public static HashMap consultarHijosCirugia(Connection con, String cirugia)
	{
		HashMap resultado=new HashMap();
		resultado.put("numRegistros", "0");
		String consulta=consultaHijosCirugia;
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setInt(1, Utilidades.convertirAEntero(cirugia));
			resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
		
		}
		return resultado;
	}


	/**
	 * 
	 * @param con
	 * @param codigoInfoParto
	 * @return
	 */
	public static HashMap consultarHijo(Connection con, String codigoInfoParto,String institucion)
	{
		HashMap resultado=new HashMap();
		resultado.put("numRegistros", "0");
		String consulta=consultarHijo;
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setDouble(1, Utilidades.convertirADouble(codigoInfoParto));
			resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),false,false);
			
			//consultar los diagnosticos del egreso
			cargarDiagnosticosEgreso(con,resultado,codigoInfoParto);
			
			//consultar los campos y sus valors(si los tiene) de ranimacion en la seccion de informacion general
			cargarCamposReanimacion(con,resultado,codigoInfoParto);
			
			//consultar los campos y sus valors(si los tiene) de tamizacion en la seccion de informacion general
			cargarCamposTamizacion(con,resultado,codigoInfoParto);
			
			//consultar seccion adaptacion neonatal inmediata
			cargarSeccionAdaptacionNeonatalInmediata(con,resultado,codigoInfoParto,institucion);
			
			
			cargarSeccionExamenesFisicos(con,resultado,codigoInfoParto,institucion);
			
			cargarSeccionDiagnosticoRecienNacido(con,resultado,codigoInfoParto,institucion);
			
			cargarSeccionSano(con, resultado, codigoInfoParto, institucion);
			
			cargarSeccionApgar(con, resultado, codigoInfoParto, institucion);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
			

		}
		return resultado;
	}

	

	private static void cargarSeccionExamenesFisicos(Connection con, HashMap resultado, String codigoInfoParto, String institucion)
	{
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		try
		{
			int cont=0;
			HashMap temp=new HashMap();
			temp.put("numRegistros", "0");
			 ps= new PreparedStatementDecorator(con.prepareStatement(consultaExamenesFisicos));
			
			ps.setDouble(1, Utilidades.convertirADouble(codigoInfoParto));
			ps.setInt(2, Utilidades.convertirAEntero(institucion));
			rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				temp.put("campo_"+cont, rs.getString("campo"));
				temp.put("descripcioncampo_"+cont, rs.getString("descripcioncampo"));
				temp.put("valor_"+cont, rs.getString("valor"));		
				temp.put("descripcionvalor_"+cont, rs.getString("descripcionvalor"));
				temp.put("institucion_"+cont, institucion);
				cont++;
			}
			temp.put("numRegistros", cont+"");
			resultado.put("examenesfisicos",temp);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
		}
	}
	
	
	private static void cargarSeccionApgar(Connection con, HashMap resultado, String codigoInfoParto, String institucion)
	{
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		try
		{
			int cont=0;
			HashMap temp=new HashMap();
			temp.put("numRegistros", "0");
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaApgar));
			ps.setDouble(1, Utilidades.convertirADouble(codigoInfoParto));
			ps.setInt(2, Utilidades.convertirAEntero(institucion));
			rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				temp.put("campo_"+cont, rs.getString("campo"));
				temp.put("descripcioncampo_"+cont, rs.getString("descripcioncampo"));
				temp.put("valor_"+cont, rs.getString("valor"));		
				temp.put("riesgo_"+cont, rs.getString("riesgo"));
				temp.put("institucion_"+cont, institucion);
				cont++;
			}
			temp.put("numRegistros", cont+"");
			resultado.put("apgar",temp);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
		}
	}
	
	
	private static void cargarSeccionSano(Connection con, HashMap resultado, String codigoInfoParto, String institucion)
	{
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		try
		{
			int cont=0;
			HashMap temp=new HashMap();
			temp.put("numRegistros", "0");
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaSano));
			ps.setDouble(1, Utilidades.convertirADouble(codigoInfoParto));
			ps.setInt(2, Utilidades.convertirAEntero(institucion));
			rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				temp.put("campo_"+cont, rs.getString("campo"));
				temp.put("descripcioncampo_"+cont, rs.getString("descripcioncampo"));
				temp.put("valor_"+cont, rs.getString("valor"));		
				temp.put("institucion_"+cont, institucion);
				cont++;
			}
			temp.put("numRegistros", cont+"");
			resultado.put("sano",temp);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
		}
	}
	
	private static void cargarSeccionDiagnosticoRecienNacido(Connection con,HashMap resultado,String codigoInfoParto,String institucion)
	{
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		try
		{
			int cont=0;
			HashMap temp=new HashMap();
			temp.put("numRegistros", "0");
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaDiagnosticoRecienNacido));
			ps.setDouble(1, Utilidades.convertirADouble(codigoInfoParto));
			ps.setInt(2, Utilidades.convertirAEntero(institucion));
			rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				temp.put("campo_"+cont, rs.getString("campo"));
				temp.put("descripcioncampo_"+cont, rs.getString("descripcioncampo"));
				temp.put("valor_"+cont, rs.getString("valor"));		
				temp.put("institucion_"+cont, institucion);
				cont++;
			}
			temp.put("numRegistros", cont+"");
			resultado.put("diagreciennacido",temp);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
		}
	}


	/**
	 * 
	 * @param con
	 * @param resultado
	 * @param codigoInfoParto
	 * @param institucion 
	 */
	private static void cargarSeccionAdaptacionNeonatalInmediata(Connection con, HashMap resultado, String codigoInfoParto, String institucion) throws SQLException
	{
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		try{
			
			int cont=0;
			HashMap temp=new HashMap();
			temp.put("numRegistros", "0");
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaAdapatacionNeonatalInmediata));
			ps.setDouble(1, Utilidades.convertirADouble(codigoInfoParto));
			ps.setInt(2, Utilidades.convertirAEntero(institucion));
			rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				temp.put("campo_"+cont, rs.getString("campo"));
				temp.put("descripcioncampo_"+cont, rs.getString("descripcioncampo"));
				temp.put("valor_"+cont, rs.getString("valor"));		
				temp.put("institucion_"+cont, rs.getString("institucion"));
				cont++;
			}
			temp.put("numRegistros", cont+"");
			resultado.put("secadaptaneonainmediata",temp);
		}catch(SQLException exception){
			exception.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
		}
		
	}


	/**
	 * 
	 * @param con
	 * @param resultado
	 * @param codigoInfoParto
	 */
	private static void cargarCamposTamizacion(Connection con, HashMap resultado, String codigoInfoParto) throws SQLException
	{
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		try{
			int cont=0;
			HashMap temp=new HashMap();
			temp.put("numRegistros", "0");
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaTamizacion));
			ps.setDouble(1, Utilidades.convertirADouble(codigoInfoParto));
			
			rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				temp.put("campo_"+cont, rs.getString("campo"));
				temp.put("descripcioncampo_"+cont, rs.getString("descripcioncampo"));
				temp.put("valor_"+cont, rs.getString("valor"));		
				cont++;
			}
			temp.put("numRegistros", cont+"");
			resultado.put("tamizacion",temp);
		}catch(SQLException sqlException){
			sqlException.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
		}
	}


	/**
	 * 
	 * @param con
	 * @param resultado
	 * @param codigoInfoParto
	 * @throws SQLException 
	 */
	private static void cargarCamposReanimacion(Connection con, HashMap resultado, String codigoInfoParto) throws SQLException
	{
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		try{
			int cont=0;
			HashMap temp=new HashMap();
			temp.put("numRegistros", "0");
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaReanimacion));
			ps.setDouble(1, Utilidades.convertirADouble(codigoInfoParto));
			
			rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				temp.put("campo_"+cont, rs.getString("campo"));
				temp.put("descripcioncampo_"+cont, rs.getString("descripcioncampo"));
				temp.put("valor_"+cont, rs.getString("valor"));		
				cont++;
			}
			temp.put("numRegistros", cont+"");
			resultado.put("reanimacion",temp);
			
		}catch(SQLException sqlException){
			sqlException.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
		}
		
		
	}


	/**
	 * 
	 * @param con
	 * @param resultado
	 * @param codigoInfoParto
	 */
	private static void cargarDiagnosticosEgreso(Connection con, HashMap resultado, String codigoInfoParto) throws SQLException
	{
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		try{
			
			int cont=0;
			
			HashMap temp=new HashMap();
			temp.put("numRegistros", "0");
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaDiagEgreso));
			ps.setDouble(1, Utilidades.convertirADouble(codigoInfoParto));
			ps.setString(2, ConstantesBD.acronimoNo);
			rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				temp.put("checkRel_"+cont, "true");
				temp.put("acronimoRel_"+cont, rs.getString("acrdiag"));
				temp.put("tipoCieRel_"+cont, rs.getString("ciediag"));
				temp.put("nombreRel_"+cont, rs.getString("nombreDiagnostico"));		
				//new ini
				temp.put("relacionado_"+cont, rs.getString("acrdiag") + ConstantesBD.separadorSplit + rs.getString("ciediag") + ConstantesBD.separadorSplit + rs.getString("nombreDiagnostico")  );
				temp.put("checkbox_"+cont, "true");
				//new fin
				
				cont++;
			}
			temp.put("numRegistros", cont+"");
			ps.setString(2, ConstantesBD.acronimoSi);
			rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				temp.put("acronimoPrincipal", rs.getString("acrdiag"));
				temp.put("tipoCiePrincipal", rs.getString("ciediag"));
				temp.put("tipoDiagPrincipal", rs.getString("tipodiag"));			
				temp.put("nombreAcronimoPrincipal", rs.getString("nombreDiagnostico"));		
				//new ini
				temp.put("principal", rs.getString("acrdiag") + ConstantesBD.separadorSplit + rs.getString("ciediag") + ConstantesBD.separadorSplit + rs.getString("nombreDiagnostico")  );
				//new fin
			}
			else
			{
				temp.put("acronimoPrincipal", "");
				temp.put("tipoCiePrincipal", "");
				temp.put("tipoDiagPrincipal", "");			
				temp.put("nombreAcronimoPrincipal", "");
				temp.put("principal", "");
			}
			resultado.put("diagegreso",temp);
		} catch(SQLException sqlException){
			sqlException.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
		}
	}



	/**
	 * Metodo para actualizar la informacion del recien nacido.
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean actualizarInformacionRecienNacido(Connection con, HashMap vo, String cadenaInsercionDx)
	{
		boolean enTransaccion=UtilidadBD.iniciarTransaccion(con);
		{
			PreparedStatementDecorator ps= null;
			
			try
			{
				ps= new PreparedStatementDecorator(con.prepareStatement(actualizarRegistro));
				
				
				if(!(vo.get("fechanacimiento")+"").equals(""))
					ps.setDate(1,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechanacimiento")+"")));
				else
					ps.setNull(1, Types.DATE);
				
				ps.setString(2,vo.get("horanacimiento")+"");
				
				if(!(vo.get("sexo")+"").equals(""))
					ps.setInt(3,Utilidades.convertirAEntero(vo.get("sexo")+""));
				else
					ps.setNull(3, Types.INTEGER);

				
				if(!(vo.get("vivo")+"").equals(""))
					ps.setBoolean(4,UtilidadTexto.getBoolean(vo.get("vivo")+""));
				else
					ps.setObject(4, null);

				if(!(vo.get("diagrn")+"").equals(""))
					ps.setString(5,vo.get("diagrn")+"");
				else
					ps.setNull(5,Types.VARCHAR);
				
				if(!(vo.get("ciediagrn")+"").equals(""))
					ps.setInt(6,Utilidades.convertirAEntero(vo.get("ciediagrn")+""));
				else
					ps.setNull(6, Types.INTEGER);

				if(!(vo.get("fechamuerte")+"").equals(""))
					ps.setDate(7, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechamuerte")+"")));
				else
					ps.setNull(7, Types.DATE);
				if(!(vo.get("horamuerte")+"").equals(""))
					ps.setString(8,vo.get("horamuerte")+"");
				else
					ps.setNull(8,Types.VARCHAR);
				if(!(vo.get("diagmuerte")+"").equals(""))
					ps.setString(9,vo.get("diagmuerte")+"");
				else
					ps.setNull(9,Types.VARCHAR);
				if(Utilidades.convertirAEntero(vo.get("ciediagmuerte")+"")>0)
					ps.setInt(10,Utilidades.convertirAEntero(vo.get("ciediagmuerte")+""));
				else
					ps.setNull(10,Types.INTEGER);
				ps.setInt(11,Utilidades.convertirAEntero(vo.get("codigocirugia")+""));
				if(!(vo.get("fallecesalaparto")+"").equals(""))
					ps.setString(12,vo.get("fallecesalaparto")+"");
				else
					ps.setNull(12,Types.CHAR);
				if(!(vo.get("momentomuerte")+"").equals(""))
					ps.setString(13,vo.get("momentomuerte")+"");
				else
					ps.setNull(13,Types.VARCHAR);
				if(!(vo.get("pesoedadgestacion")+"").equals(""))
					ps.setString(14,vo.get("pesoedadgestacion")+"");
				else
					ps.setNull(14, Types.VARCHAR);
				if(!(vo.get("vitaminak")+"").equals(""))
					ps.setString(15,vo.get("vitaminak")+"");
				else
					ps.setNull(15,Types.CHAR);
				if(!(vo.get("profilaxisoftalmico")+"").equals(""))
					ps.setString(16,vo.get("profilaxisoftalmico")+"");
				else
					ps.setNull(16,Types.CHAR);
				if(Utilidades.convertirAEntero(vo.get("hemoclasificacion")+"")>0)
					ps.setInt(17,Utilidades.convertirAEntero(vo.get("hemoclasificacion")+""));
				else
					ps.setNull(17,Types.INTEGER);
				if(!(vo.get("sensibilizado")+"").equals(""))
					ps.setString(18,vo.get("sensibilizado")+"");
				else
					ps.setNull(18,Types.CHAR);
				if(!(vo.get("defectoscongenitos")+"").equals(""))
					ps.setString(19,vo.get("defectoscongenitos")+"");
				else
					ps.setNull(19, Types.VARCHAR);
				if(!(vo.get("diagdefcong")+"").equals(""))
					ps.setString(20,vo.get("diagdefcong")+"");
				else
					ps.setNull(20,Types.VARCHAR);
				if(Utilidades.convertirAEntero(vo.get("ciediagdefcong")+"")>0)
					ps.setInt(21,Utilidades.convertirAEntero(vo.get("ciediagdefcong")+""));
				else
					ps.setNull(21,Types.INTEGER);

				if(!(vo.get("fechaegreso")+"").equals(""))
					ps.setDate(22,Date.valueOf(((vo.get("fechaegreso")+"").trim().equals("")?null:UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaegreso")+""))));
				else
					ps.setNull(22, Types.DATE);
				if(!(vo.get("horaegreso")+"").equals(""))
					ps.setString(23,vo.get("horaegreso")+"");
				else
					ps.setNull(23,Types.VARCHAR);
				if(!(vo.get("condicionegreso")+"").equals(""))
					ps.setString(24,vo.get("condicionegreso")+"");
				else
					ps.setNull(24,Types.VARCHAR);
				if(!(vo.get("lactancia")+"").equals(""))
					ps.setString(25,vo.get("lactancia")+"");
				else
					ps.setNull(25,Types.VARCHAR);
				if(Utilidades.convertirADouble(vo.get("pesoegreso")+"")>0)
					ps.setDouble(26,Utilidades.convertirADouble(vo.get("pesoegreso")+""));
				else
					ps.setNull(26,Types.NUMERIC);
				if(!(vo.get("nuip")+"").equals(""))
					ps.setString(27,vo.get("nuip")+"");
				else
					ps.setNull(27,Types.VARCHAR);
				if(!(vo.get("vacunapolio")+"").equals(""))
					ps.setString(28,vo.get("vacunapolio")+"");
				else
					ps.setNull(28,Types.CHAR);
				if(!(vo.get("vacunabcg")+"").equals(""))
					ps.setString(29,vo.get("vacunabcg")+"");
				else
					ps.setNull(29,Types.CHAR);
				if(!(vo.get("vacunahepatitisb")+"").equals(""))
					ps.setString(30,vo.get("vacunahepatitisb")+"");
				else
					ps.setNull(30,Types.CHAR);
				if(!(vo.get("sanoenfermo")+"").equals(""))
					ps.setString(31,vo.get("sanoenfermo")+"");
				else
					ps.setNull(31,Types.CHAR);
				if(!(vo.get("conductaseguir")+"").equals(""))
					ps.setString(32,vo.get("conductaseguir")+"");
				else
					ps.setNull(32,Types.VARCHAR);
				if(!(vo.get("observacionesegreso")+"").equals(""))
					ps.setString(33,vo.get("observacionesegreso")+"");
				else
					ps.setNull(33,Types.VARCHAR);
				if(Utilidades.convertirAEntero(vo.get("codprofatendio")+"")>0)
					ps.setInt(34,Utilidades.convertirAEntero(vo.get("codprofatendio")+""));
				else
					ps.setNull(34,Types.INTEGER);
				if(!(vo.get("conductaseguir_ani")+"").equals(""))
					ps.setString(35,vo.get("conductaseguir_ani")+"");
				else
					ps.setNull(35,Types.VARCHAR);
				if(Utilidades.convertirADouble(vo.get("edadgestacionexamen")+"")>0)
					ps.setDouble(36,Utilidades.convertirADouble(vo.get("edadgestacionexamen")+""));
				else
					ps.setNull(36,Types.NUMERIC);
				if(!(vo.get("numerocertificadonacimiento")+"").equals(""))
					ps.setString(37,vo.get("numerocertificadonacimiento")+"");
				else
					ps.setNull(37,Types.VARCHAR);
				if(!(vo.get("finalizada")+"").equals(""))
					ps.setString(38,vo.get("finalizada")+"");
				else
					ps.setNull(38,Types.CHAR);
				if(!(vo.get("codigoenfermedad")+"").equals(""))
					ps.setString(39, vo.get("codigoenfermedad")+"");
				else
					ps.setNull(39,Types.VARCHAR);
				ps.setDouble(40,Utilidades.convertirADouble(vo.get("codigo")+""));

				
				//ps.executeUpdate( );
				
				///new ini
				int resp = ps.executeUpdate();
				String uniCodCieDiag = "";
				String ficha = "";
				String solicitud = "";
				PersonaBasica paciente = (PersonaBasica)vo.get("paciente");
				UsuarioBasico usuario = (UsuarioBasico)vo.get("usuario");
				if (resp >0)
				{
					if ( vo.get("diagrn") != "" )
					{
						uniCodCieDiag = vo.get("diagrn") +ConstantesBD.separadorSplit + vo.get("ciediagrn");
						ficha = vo.get("ficha")+"";
					}
					else
					{
						uniCodCieDiag = vo.get("diagmuerte") +ConstantesBD.separadorSplit + vo.get("ciediagmuerte");
						ficha = vo.get("fichamuerte")+"";
					}
					
					solicitud =  vo.get("solicitud")+"";
					Utilidades.actualizarDatosEpidemiologia(con, uniCodCieDiag, ficha, solicitud, paciente, usuario);
				}
				///new fin
				
				
				eliminarDiagnosticosEgreso(con,vo.get("codigo")+"");
				insertarDiagnosticosEgreso(con,(HashMap)vo.get("diagegreso"),vo.get("codigo")+"", paciente , usuario, solicitud, cadenaInsercionDx);
				
				eliminarInfoReanimacion(con,vo.get("codigo")+"");
				insertarInfoReanimacion(con,(HashMap)vo.get("reanimacion"),vo.get("codigo")+"");

				eliminarInfoTamizacionNeonatal(con,vo.get("codigo")+"");
				insertarInfoTamizacionNeonatal(con,(HashMap)vo.get("tamizacion"),vo.get("codigo")+"");
				
				eliminarSeccionAdaptacionNeonatalInmediata(con,vo.get("codigo")+"");
				insertarSeccionAdaptacionNeonatalInmediata(con,(HashMap)vo.get("secadaptaneonainmediata"),vo.get("codigo")+"");
				
				eliminarSeccionExamenesFisicos(con,vo.get("codigo")+"");
				insertarSeccionExamenesFisicos(con,(HashMap)vo.get("examenesfisicos"),vo.get("codigo")+"");
				
				eliminarSeccionDiagnosticoRecienNacido(con,vo.get("codigo")+"");
				insertarSeccionDiagnosticoRecienNacido(con,(HashMap)vo.get("diagreciennacido"),vo.get("codigo")+"");
				
				eliminarSeccionSano(con,vo.get("codigo")+"");
				insertarSeccionSano(con,(HashMap)vo.get("sano"),vo.get("codigo")+"");
				
				eliminarSeccionApgar(con,vo.get("codigo")+"");
				insertarSeccionApgar(con,(HashMap)vo.get("apgar"),vo.get("codigo")+"");
			}
			catch (SQLException e)
			{   enTransaccion=false;
				e.printStackTrace();
			}finally {			
				if(ps!=null){
					try{
						ps.close();					
					}catch(SQLException sqlException){
						logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
					}
				}
				
			}
		}
		if(enTransaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
		}
		return enTransaccion;
	}



	private static void insertarSeccionApgar(Connection con, HashMap map, String codigoIPH) throws SQLException 
	{
		PreparedStatementDecorator ps= null;
		
		try{
			
			String cadena="INSERT INTO info_part_seccion_apgar (codigo_iph,codigo_campo,valor,institucion,riesgo) values(?,?,?,?,?)";
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			for(int i=0;i<Utilidades.convertirAEntero(map.get("numRegistros")+"");i++)
			{
				ps.setDouble(1, Utilidades.convertirADouble(codigoIPH));
				ps.setDouble(2, Utilidades.convertirADouble(map.get("campo_"+i)+""));
				//ps.setString(3, map.get("valor_"+i)+"");
				ps.setString(3, UtilidadTexto.isEmpty(map.get("valor_"+i)+"") ? " " : (map.get("valor_"+i)+""));
				ps.setInt(4, Utilidades.convertirAEntero(map.get("institucion_"+i)+""));
				//ps.setString(5, map.get("riesgo_"+i)+"");
				ps.setString(5, UtilidadTexto.isEmpty(map.get("riesgo_"+i)+"") ? " " : (map.get("riesgo_"+i)+""));
				ps.executeUpdate();				
				
			}
		}catch(SQLException exception){
			exception.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
		}
	}


	private static void eliminarSeccionApgar(Connection con, String codigoIPH) throws SQLException 
	{
		PreparedStatementDecorator ps= null;
		
		try{
			ps= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM info_part_seccion_apgar where codigo_iph ="+codigoIPH));
			ps.executeUpdate();
			
		}catch(SQLException sqlException){
				sqlException.printStackTrace();	
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
			
		}
	}


	private static void insertarSeccionSano(Connection con, HashMap map, String codigoIPH) throws SQLException 
	{
		PreparedStatementDecorator ps= null;
		
		try {
			
			String cadena="INSERT INTO info_part_sano (codigo_iph,codigo_campo,valor,institucion) values(?,?,?,?)";
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			for(int i=0;i<Utilidades.convertirAEntero(map.get("numRegistros")+"");i++)
			{
				ps.setDouble(1, Utilidades.convertirADouble(codigoIPH));
				ps.setDouble(2, Utilidades.convertirADouble(map.get("campo_"+i)+""));
				//ps.setString(3, map.get("valor_"+i)+"");
				ps.setString(3, UtilidadTexto.isEmpty(map.get("valor_"+i)+"") ? " " : (map.get("valor_"+i)+""));
				ps.setInt(4, Utilidades.convertirAEntero(map.get("institucion_"+i)+""));
				ps.executeUpdate();
			}
		}catch(SQLException sqlException){
			throw sqlException;
		}finally {			
			if(ps!=null){
				try{
					ps.close();		
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
			
		}
	}


	private static void eliminarSeccionSano(Connection con, String codigoIPH) throws SQLException 
	{
		PreparedStatementDecorator ps= null;
		try{
			ps= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM info_part_sano where codigo_iph ="+codigoIPH));
			ps.executeUpdate();
			
		}catch(SQLException sqlException){
			throw sqlException;
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
			
		}
	}


	/**
	 * 
	 * @param con
	 * @param map
	 * @param string
	 */
	private static void insertarSeccionDiagnosticoRecienNacido(Connection con, HashMap map, String codigoIPH) throws SQLException 
	{
		PreparedStatementDecorator ps= null;
		try {
			String cadena="INSERT INTO info_part_diag_rn (codigo_iph,codigo_campo,valor,institucion) values(?,?,?,?)";
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			for(int i=0;i<Utilidades.convertirAEntero(map.get("numRegistros")+"");i++)
			{
				ps.setDouble(1, Utilidades.convertirADouble(codigoIPH));
				ps.setDouble(2, Utilidades.convertirADouble(map.get("campo_"+i)+""));
				//ps.setString(3, map.get("valor_"+i)+"");
				ps.setString(3, UtilidadTexto.isEmpty(map.get("valor_"+i)+"") ? " " : (map.get("valor_"+i)+""));
				ps.setInt(4, Utilidades.convertirAEntero(map.get("institucion_"+i)+""));
				ps.executeUpdate();
			}
			
		} catch(SQLException sqlException){
			throw sqlException;
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
			
		}
	}


	/**
	 * 
	 * @param con
	 * @param string
	 */
	private static void eliminarSeccionDiagnosticoRecienNacido(Connection con, String codigoIPH) throws SQLException 
	{
		PreparedStatementDecorator ps= null;
		try {
			
			ps= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM info_part_diag_rn where codigo_iph ="+codigoIPH));
			ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
			
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
			
		}
	}


	private static void insertarSeccionExamenesFisicos(Connection con, HashMap map, String codigoIPH) throws SQLException 
	{
		PreparedStatementDecorator ps= null;
		try{
			String cadena="INSERT INTO info_part_exam_fisico (codigo_iph,codigo_campo,valor,institucion,descripcion) values(?,?,?,?,?)";
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			for(int i=0;i<Utilidades.convertirAEntero(map.get("numRegistros")+"");i++)
			{
				
				ps.setDouble(1, Utilidades.convertirADouble(codigoIPH));
				ps.setDouble(2, Utilidades.convertirADouble(map.get("campo_"+i)+""));
				//ps.setString(3, map.get("valor_"+i)+"");
				ps.setString(3, UtilidadTexto.isEmpty(map.get("valor_"+i)+"") ? " " : (map.get("valor_"+i)+""));
				ps.setInt(4, Utilidades.convertirAEntero(map.get("institucion_"+i)+""));
				ps.setString(5, map.get("descripcionvalor_"+i)+"");
				ps.executeUpdate();
				
				
			}
			
		}catch(SQLException sqlException){
			throw sqlException;
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
			
		}
	}


	private static void eliminarSeccionExamenesFisicos(Connection con, String codigoIPH) throws SQLException 
	{
		PreparedStatementDecorator ps= null;
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM info_part_exam_fisico where codigo_iph ="+codigoIPH));
			ps.executeUpdate();
			
		} catch (SQLException e) {
			throw e;
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
		
		}
	}


	/**
	 * 
	 * @param con
	 * @param map
	 * @param string
	 */
	private static void insertarSeccionAdaptacionNeonatalInmediata(Connection con, HashMap map, String codigoIPH) throws SQLException 
	{
		PreparedStatementDecorator ps=  null;
		try {
			
			String cadena="INSERT INTO info_part_adap_neo_inmediata (codigo_iph,codigo_campo,valor,institucion) values(?,?,?,?)";
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			for(int i=0;i<Utilidades.convertirAEntero(map.get("numRegistros")+"");i++)
			{
				ps.setDouble(1, Utilidades.convertirADouble(codigoIPH));
				ps.setDouble(2, Utilidades.convertirADouble(map.get("campo_"+i)+""));
				//ps.setString(3, map.get("valor_"+i)+"");
				ps.setString(3, UtilidadTexto.isEmpty(map.get("valor_"+i)+"") ? " " : (map.get("valor_"+i)+""));
				ps.setInt(4, Utilidades.convertirAEntero(map.get("institucion_"+i)+""));
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			throw e;
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
			
		}

	}


	/**
	 * 
	 * @param con
	 * @param string
	 */
	private static void eliminarSeccionAdaptacionNeonatalInmediata(Connection con, String codigoIPH) throws SQLException 
	{
		PreparedStatementDecorator ps=  null; 
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM info_part_adap_neo_inmediata where codigo_iph ="+codigoIPH));
			ps.executeUpdate();
			
		} catch (SQLException  e) {
			throw e;
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
			
		}
	}


	/**
	 * 
	 * @param con
	 * @param map
	 * @param codigoInfoParto
	 * @throws SQLException
	 */
	private static void insertarInfoTamizacionNeonatal(Connection con, HashMap map, String codigoInfoParto) throws SQLException 
	{
		PreparedStatementDecorator ps=  null;
		try {
			String cadena="INSERT INTO info_part_tam_neo (codigo_iph,codigo_campo,valor) values(?,?,?)";
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			for(int i=0;i<Utilidades.convertirAEntero(map.get("numRegistros")+"");i++)
			{
				ps.setDouble(1, Utilidades.convertirADouble(codigoInfoParto));
				ps.setDouble(2, Utilidades.convertirADouble(map.get("campo_"+i)+""));
				//ps.setString(3, map.get("valor_"+i)+"");
				ps.setString(3, UtilidadTexto.isEmpty(map.get("valor_"+i)+"") ? " " : (map.get("valor_"+i)+""));
				ps.executeUpdate();
			}
			
		} catch (SQLException e) {
			throw e;
			
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
		}
	}


	/**
	 * 
	 * @param con
	 * @param map
	 * @param codigoInfoParto
	 * @throws SQLException
	 */
	private static void insertarInfoReanimacion(Connection con, HashMap map, String codigoInfoParto) throws SQLException 
	{
		PreparedStatementDecorator ps=  null;
		try {
			
			String cadena="INSERT INTO info_parto_reanimacion (codigo_iph,codigo_campo,valor) values(?,?,?)";
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			for(int i=0;i<Utilidades.convertirAEntero(map.get("numRegistros")+"");i++)
			{
				ps.setDouble(1, Utilidades.convertirADouble(codigoInfoParto));
				ps.setDouble(2, Utilidades.convertirADouble(map.get("campo_"+i)+""));
				//ps.setString(3, map.get("valor_"+i)+"");
				ps.setString(3, UtilidadTexto.isEmpty(map.get("valor_"+i)+"") ? " " : (map.get("valor_"+i)+""));
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			throw e;
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
		}
	}

	/**
	 * 
	 * @param con
	 * @param codigoIPH
	 * @throws SQLException
	 */
	private static void eliminarInfoTamizacionNeonatal(Connection con, String codigoIPH) throws SQLException
	{
		PreparedStatementDecorator ps= null;
		try {
			
			ps= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM info_part_tam_neo where codigo_iph ="+codigoIPH));
			ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
		}
	}


	/**
	 * 
	 * @param con
	 * @param codigoIPH
	 * @throws SQLException
	 */
	private static void eliminarInfoReanimacion(Connection con, String codigoIPH) throws SQLException
	{
		PreparedStatementDecorator ps= null;
		try {
			
			ps= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM info_parto_reanimacion where codigo_iph ="+codigoIPH));
			ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
		}
	}


	/**
	 * 
	 * @param con
	 * @param string
	 */
	private static void eliminarDiagnosticosEgreso(Connection con, String codigoIPH) throws SQLException
	{
		PreparedStatementDecorator ps= null;
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM diag_egreso_rn WHERE codigo_iph="+codigoIPH));
			ps.executeUpdate();
			
		} catch (SQLException e) {
			throw e;
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
			
		}
	}



	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean insertarInformacionGeneral(Connection con, HashMap vo, String insertarInfoParto, String cadenaInsercionDx)
	{
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs= null;
		boolean enTransaccion=UtilidadBD.iniciarTransaccion(con);
		{
			try
			{
				ps= new PreparedStatementDecorator(con.prepareStatement(insertarInfoParto));
				ps.setString(1,vo.get("codigocirugia")+"");
				
				if(!(vo.get("fechanacimiento")+"").equals(""))
					ps.setDate(2,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechanacimiento")+"")));
				else
					ps.setNull(2, Types.DATE);
				
				ps.setString(3,vo.get("horanacimiento")+"");
				
				if(!(vo.get("sexo")+"").equals(""))
					ps.setInt(4,Utilidades.convertirAEntero(vo.get("sexo")+""));
				else
					ps.setNull(4, Types.INTEGER);

				if(!(vo.get("vivo")+"").equals(""))
					ps.setBoolean(5,UtilidadTexto.getBoolean(vo.get("vivo")+""));
				else
					ps.setObject(5, null);

				ps.setString(6,vo.get("diagrn")+"");
				
				if(!(vo.get("ciediagrn")+"").equals(""))
					ps.setInt(7,Utilidades.convertirAEntero(vo.get("ciediagrn")+""));
				else
					ps.setNull(7, Types.INTEGER);

				if(!(vo.get("fechamuerte")+"").equals(""))
					ps.setDate(8,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechamuerte")+"")));
				else
					ps.setNull(8, Types.DATE);

				ps.setString(9,vo.get("horamuerte")+"");
				
				if(!(vo.get("diagmuerte")+"").equals(""))
					ps.setString(10,vo.get("diagmuerte")+"");
				else
					ps.setNull(10,Types.VARCHAR);
				if(Utilidades.convertirAEntero(vo.get("ciediagmuerte")+"")!=ConstantesBD.codigoNuncaValido)
					ps.setInt(11,Utilidades.convertirAEntero(vo.get("ciediagmuerte")+""));
				else
					ps.setNull(11,Types.INTEGER);
				ps.setInt(12,Utilidades.convertirAEntero(vo.get("codigocirugia")+""));
				if(UtilidadCadena.noEsVacio(vo.get("fallecesalaparto")+""))
					ps.setString(13,vo.get("fallecesalaparto")+"");
				else
					ps.setNull(13,Types.CHAR);
				if(UtilidadCadena.noEsVacio(vo.get("momentomuerte")+""))
					ps.setString(14,vo.get("momentomuerte")+"");
				else
					ps.setNull(14,Types.VARCHAR);
				ps.setString(15,vo.get("pesoedadgestacion")+"");
				ps.setString(16,vo.get("vitaminak")+"");
				ps.setString(17,vo.get("profilaxisoftalmico")+"");
				if(Utilidades.convertirAEntero(vo.get("hemoclasificacion")+"")!=ConstantesBD.codigoNuncaValido)
					ps.setInt(18,Utilidades.convertirAEntero(vo.get("hemoclasificacion")+""));
				else
					ps.setNull(18,Types.INTEGER);
				ps.setString(19,vo.get("sensibilizado")+"");
				ps.setString(20,vo.get("defectoscongenitos")+"");
				if(!(vo.get("diagdefcong")+"").equals(""))
					ps.setString(21,vo.get("diagdefcong")+"");
				else
					ps.setNull(21,Types.VARCHAR);
				if(Utilidades.convertirAEntero(vo.get("ciediagdefcong")+"")!=ConstantesBD.codigoNuncaValido)
					ps.setInt(22,Utilidades.convertirAEntero(vo.get("ciediagdefcong")+""));
				else
					ps.setNull(22,Types.INTEGER);

				if(!(vo.get("fechaegreso")+"").equals(""))
					ps.setDate(23,Date.valueOf(((vo.get("fechaegreso")+"").trim().equals("")?null:UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaegreso")+""))));
				else
					ps.setNull(23, Types.DATE);
				
				ps.setString(24,vo.get("horaegreso")+"");
				ps.setString(25,vo.get("condicionegreso")+"");
				ps.setString(26,vo.get("lactancia")+"");
				if(Utilidades.convertirAEntero(vo.get("pesoegreso")+"")!=ConstantesBD.codigoNuncaValido)
					ps.setInt(27,Utilidades.convertirAEntero(vo.get("pesoegreso")+""));
				else
					ps.setNull(27,Types.NUMERIC);
				ps.setString(28,vo.get("nuip")+"");
				ps.setString(29,vo.get("vacunapolio")+"");
				ps.setString(30,vo.get("vacunabcg")+"");
				ps.setString(31,vo.get("vacunahepatitisb")+"");
				ps.setString(32,vo.get("sanoenfermo")+"");
				ps.setString(33,vo.get("conductaseguir")+"");
				ps.setString(34,vo.get("observacionesegreso")+"");
				ps.setString(35,UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaproceso")+""));
				ps.setString(36,vo.get("horaproceso")+"");
				ps.setString(37,vo.get("usuarioproceso")+"");
				if(Utilidades.convertirAEntero(vo.get("codprofatendio")+"")!=ConstantesBD.codigoNuncaValido)
					ps.setInt(38,Utilidades.convertirAEntero(vo.get("codprofatendio")+""));
				else
					ps.setNull(38,Types.INTEGER);
				ps.setString(39,vo.get("conductaseguir_ani")+"");
				
				if(Utilidades.convertirAEntero(vo.get("edadgestacionexamen")+"")!=ConstantesBD.codigoNuncaValido)
					ps.setDouble(40,Utilidades.convertirAEntero(vo.get("edadgestacionexamen")+""));
				else
					ps.setNull(40, Types.NUMERIC);
				ps.setString(41,vo.get("numerocertificadonacimiento")+"");
				ps.setString(42,vo.get("finalizada")+"");
				ps.setString(43,vo.get("codigoenfermedad")+"");

				///new ini
				int resp = ps.executeUpdate();
				String uniCodCieDiag = "";
				String ficha = "";
				String solicitud = "";
				PersonaBasica paciente = (PersonaBasica)vo.get("paciente");
				UsuarioBasico usuario = (UsuarioBasico)vo.get("usuario");
				if (resp >0)
				{
					if ( vo.get("diagrn") != "" )
					{
						uniCodCieDiag = vo.get("diagrn") +ConstantesBD.separadorSplit + vo.get("ciediagrn");
						ficha = vo.get("ficha")+"";
					}
					else
					{
						uniCodCieDiag = vo.get("diagmuerte") +ConstantesBD.separadorSplit + vo.get("ciediagmuerte");
						ficha = vo.get("fichamuerte")+"";
					}
					
					solicitud =  vo.get("solicitud")+"";
					if (!ficha.equals(""))
					{
						Utilidades.actualizarDatosEpidemiologia(con, uniCodCieDiag, ficha, solicitud, paciente, usuario);
					}
				}
				///new fin

				
				String codigoInfoParto="";
				ps= new PreparedStatementDecorator(con.prepareStatement("SELECT max(consecutivo) as codigo from info_parto_hijos"));
				rs=new ResultSetDecorator(ps.executeQuery());
				if(rs.next())
				{
					codigoInfoParto=rs.getString("codigo");
					
					insertarDiagnosticosEgreso(con,(HashMap)vo.get("diagegreso"),codigoInfoParto, paciente , usuario, solicitud,cadenaInsercionDx);
					
					insertarInfoReanimacion(con,(HashMap)vo.get("reanimacion"),codigoInfoParto);

					insertarInfoTamizacionNeonatal(con,(HashMap)vo.get("tamizacion"),codigoInfoParto);
					
					insertarSeccionAdaptacionNeonatalInmediata(con,(HashMap)vo.get("secadaptaneonainmediata"),codigoInfoParto);
					
					insertarSeccionExamenesFisicos(con,(HashMap)vo.get("examenesfisicos"),codigoInfoParto);
					
					insertarSeccionDiagnosticoRecienNacido(con,(HashMap)vo.get("diagreciennacido"),codigoInfoParto);
					
					insertarSeccionSano(con,(HashMap)vo.get("sano"),codigoInfoParto);
					
					insertarSeccionApgar(con,(HashMap)vo.get("apgar"),codigoInfoParto);
					
				}
				else
				{
					enTransaccion=false;
				}
				
			
			}
			catch (SQLException e)
			{
				enTransaccion=false;
				e.printStackTrace();
			}finally {			
				if(ps!=null){
					try{
						ps.close();					
					}catch(SQLException sqlException){
						logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
					}
				}
				if(rs!=null){
					try{
						rs.close();					
					}catch(SQLException sqlException){
						logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
					}
				}
			}
		}
		if(enTransaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
		}
		return enTransaccion;
	}


	/**
	 * 
	 * @param con
	 * @param map
	 * @param codigoInfoParto
	 */
	private static void insertarDiagnosticosEgreso(Connection con, HashMap map, String codigoInfoParto, PersonaBasica paciente, UsuarioBasico usuario, String solicitud, String cadenaInsercionDx) throws SQLException 
	{
		PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsercionDx));
		
		try {
			
			//if(map.containsKey("acronimoPrincipal")&&map.containsKey("tipoCiePrincipal")&&map.containsKey("tipoDiagPrincipal")&&!(map.get("acronimoPrincipal")+"").equals(""))
			//01/03/2007
			if(map.containsKey("acronimoPrincipal")&&Utilidades.convertirAEntero(map.get("tipoCiePrincipal")+"")>0&&!(map.get("acronimoPrincipal")+"").equals(""))
			{
				
				ps.setDouble(1, Utilidades.convertirADouble(codigoInfoParto));
				ps.setString(2, map.get("acronimoPrincipal")+"");
				ps.setInt(3, Utilidades.convertirAEntero((map.get("tipoCiePrincipal")+"").trim().equals("")?null:map.get("tipoCiePrincipal")+""));
				
				//ps.setString(4, map.get("tipoDiagPrincipal")+"");
				if (map.containsKey("tipoDiagPrincipal"))
				{
					ps.setString(4, map.get("tipoDiagPrincipal")+"");
				}
				else
				{
					ps.setString(4, "");
				}
				
				ps.setString(5, ConstantesBD. acronimoSi);
				//ps.executeUpdate();
				
				//new ini
				String uniCodCieDiag = "";
				String ficha = "";
				uniCodCieDiag = map.get("acronimoPrincipal") +ConstantesBD.separadorSplit + map.get("tipoCiePrincipal");
				
				if (map.containsKey("valorFichaDxPrincipal")&&!(map.get("valorFichaDxPrincipal")+"").equals(""))
				{
					ficha = map.get("valorFichaDxPrincipal")+"";
				}		
				
				int resp = ps.executeUpdate();
				if ( resp > 0 && !ficha.equals("") )
				{
					Utilidades.actualizarDatosEpidemiologia(con, uniCodCieDiag, ficha, solicitud, paciente, usuario);;
				}
				//new fin			
			}
			
			
			for(int i=0;i<Utilidades.convertirAEntero(map.get("numRegistros")+"");i++)
			{
				if(UtilidadTexto.getBoolean( map.get("checkRel_"+i)+""))
				{ 
					
					String[] uniCodCieDiagEgr =  map.get("relacionado_"+i).toString().split(ConstantesBD.separadorSplit);
					map.put("acronimoRel_"+i, uniCodCieDiagEgr[0]);
					map.put("tipoCieRel_"+i, uniCodCieDiagEgr[1]);
					
					ps.setString(1, codigoInfoParto);
					ps.setString(2, map.get("acronimoRel_"+i)+"");
					ps.setString(3, map.get("tipoCieRel_"+i)+"");
					ps.setObject(4, null);
					ps.setString(5, ConstantesBD. acronimoNo);
					//ps.executeUpdate();
					//new ini
					String uniCodCieDiag = "";
					String ficha= "";
					uniCodCieDiag = map.get("acronimoRel_"+i) +ConstantesBD.separadorSplit + map.get("tipoCieRel_"+i);
					
					if (map.containsKey("valorFichaDxRelacionado_"+i)&&!(map.get("valorFichaDxRelacionado_")+"").equals(""))
					{
						ficha = map.get("valorFichaDxRelacionado_"+i)+"";
					}				
					int resp = ps.executeUpdate();
					if ( resp > 0 && !ficha.equals("") )
					{
						Utilidades.actualizarDatosEpidemiologia(con, uniCodCieDiag, ficha, solicitud, paciente, usuario);;
					}
					//new fin					
				}
			}
		} catch (SQLException e) {
			throw e;
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
		
		}
		
	}




	/***
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap consultarAdaptacionNeonatalInmediata(Connection con, String codigoInstitucion)
	{
		HashMap resultado=new HashMap();
		try
		{
			//consultar seccion adaptacion neonatal inmediata
			cargarSeccionAdaptacionNeonatalInmediata(con,resultado,"-1",codigoInstitucion);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return (HashMap)resultado.get("secadaptaneonainmediata");
	}
	
	/***
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap consultarExamenesFisicos(Connection con, String codigoInstitucion)
	{
		HashMap resultado=new HashMap();
		try
		{
			cargarSeccionExamenesFisicos(con,resultado,"-1",codigoInstitucion);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return (HashMap)resultado.get("examenesfisicos");
	}

	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap consultarDiagnosticoRecienNacido(Connection con, String codigoInstitucion)
	{
		HashMap resultado=new HashMap();
		try
		{
			cargarSeccionDiagnosticoRecienNacido(con,resultado,"-1",codigoInstitucion);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return (HashMap)resultado.get("diagreciennacido");
	}
	

	public static HashMap consultarApgar(Connection con, String codigoInstitucion)
	{
		HashMap resultado=new HashMap();
		try
		{
			cargarSeccionApgar(con,resultado,"-1",codigoInstitucion);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return (HashMap)resultado.get("apgar");
	}
	
	public static HashMap consultarSano(Connection con, String codigoInstitucion)
	{
		HashMap resultado=new HashMap();
		try
		{
			cargarSeccionSano(con,resultado,"-1",codigoInstitucion);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return (HashMap)resultado.get("sano");
	}
	


	


	/**
	 * 
	 * @param con
	 * @return
	 */
	public static HashMap consultarReanimacion(Connection con)
	{
		HashMap resultado=new HashMap();
		try
		{
			cargarCamposReanimacion(con,resultado,"-1");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return (HashMap)resultado.get("reanimacion");

	}


	/**
	 * 
	 * @param con
	 * @return
	 */
	public static HashMap consultarTamizacionNeonatal(Connection con)
	{
		HashMap resultado=new HashMap();
		try
		{
			//consultar los campos y sus valors(si los tiene) de tamizacion en la seccion de informacion general
			cargarCamposTamizacion(con,resultado,"-1");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return (HashMap)resultado.get("tamizacion");
	}


	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static boolean esInformacionHijoFinalizada(Connection con, String consecutivo)
	{
		String cadena="select finalizada from info_parto_hijos where consecutivo="+consecutivo;
		PreparedStatementDecorator ps=  null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return UtilidadTexto.getBoolean(rs.getString(1));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param codCx
	 * @return
	 */
	 public static Vector obtenerConsecutivosInfoRecienNacidoDadoCx(Connection con, String codCx, String buscarFinalizada)
     {
         String cadena= " SELECT consecutivo as conse FROM info_parto_hijos  where cirugia=?";
         PreparedStatementDecorator ps =  null;
         ResultSetDecorator rs  = null;
         if(!buscarFinalizada.equals(""))
                 cadena+=" AND finalizada = '"+buscarFinalizada+"'";
         Vector vector= new Vector();
         try
         {
                 ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
                 ps.setInt(1, Utilidades.convertirAEntero(codCx));
                 rs =new ResultSetDecorator(ps.executeQuery());
                 while(rs.next())
                         vector.add(rs.getString("conse"));
         }
         catch (SQLException e)
         {
                 e.printStackTrace();
         }finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
		}
         return vector;
     }

 
}
