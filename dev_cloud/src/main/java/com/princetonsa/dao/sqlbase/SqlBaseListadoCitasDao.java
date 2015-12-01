/*
 * Created on 02-abr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoCollectionDB;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * @author juanda
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SqlBaseListadoCitasDao
{
	/**
	 * Para hacer logs de la clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseListadoCitasDao.class);
	
	private static final String consultaCitasPorAtenderMedico_SELECT = "SELECT "+ 
		"c.codigo AS codigo_cita, "+
		"a.codigo_medico AS codigomedico, "+ 
		"c.estado_liquidacion AS codigo_estado_liquidacion, "+  
		"c.unidad_consulta AS codigo_unidad_agenda, "+
		"c.codigo_paciente AS codigo_paciente, "+  
		"c.estado_cita AS codigo_estado_cita, "+  
		"c.prioritaria AS prioritaria, "+
		"getnombreunidadconsulta(c.unidad_consulta) AS nombre_unidad_agenda, "+
		"coalesce(sc.numero_solicitud,0) AS numero_solicitud, "+
		"coalesce(sc.observaciones,'') AS observaciones, "+  
		"sc.servicio AS codigo_servicio, "+
		"'(' || getcodigoespecialidad(sc.servicio) || '-' || sc.servicio || ') ' || getnombreservicio(sc.servicio,"+ConstantesBD.codigoTarifarioCups+") AS nombre_servicio, "+
		"gettiposervicio(sc.servicio) AS codigo_tipo_servicio, " +
		"tieneServicioCondiciones(sc.servicio,?) AS tiene_condiciones, " +
		"coalesce(sc.especialidad,"+ConstantesBD.codigoEspecialidadMedicaNinguna+") as codigo_especialidad, "+
		"sc.estado AS estado_servicio, "+
		"CASE WHEN sc.numero_solicitud IS NULL THEN 0 ELSE getCuentaXSolicitud(sc.numero_solicitud) END AS cuenta, " +
		"sc.centro_costo AS codigo_centro_costo, "+  
		"a.codigo AS codigo_agenda, "+  
		"a.hora_inicio || '' AS hora_inicio_cita, "+  
		"a.hora_fin || '' AS hora_fin_cita, "+  
		"CASE WHEN a.codigo_medico IS NOT NULL THEN "+ValoresPorDefecto.getValorTrueParaConsultas()+" ELSE "+ValoresPorDefecto.getValorFalseParaConsultas()+" END AS pertenece_medico, "+ 
		"ec.nombre AS nombre_estado_cita, "+  
		"p.numero_identificacion AS numero_identificacion_paciente, "+  
		"p.tipo_identificacion AS tipo_identificacion_paciente, "+  
		"p.primer_nombre || ' ' || coalesce(p.segundo_nombre,'') || ' ' || "+ 
		"p.primer_apellido || ' ' || coalesce(p.segundo_apellido,'') as nombre_completo_paciente, "+  
		"p.sexo AS sexo, "+ 
		"'false'  AS pyp, ";
			 
	/*"SELECT c.codigo AS codigo_cita, "+ 	
	"c.estado_liquidacion AS codigo_estado_liquidacion,  "+
	"CASE WHEN c.numero_solicitud IS NULL THEN 0 ELSE c.numero_solicitud END AS numeroSolicitud,  "+
	"uc.codigo AS codigoUnidadConsulta,  "+
	"uc.descripcion AS nombreUnidadConsulta,  "+
	"a.hora_inicio || '' AS horaInicioCita,  "+
	"a.hora_fin || '' AS horaFinCita,  "+
	"p.numero_identificacion AS numeroIdentificacionPaciente,  "+
	"p.tipo_identificacion AS tipoIdentificacionPaciente,  "+
	"c.codigo_paciente AS codigoPaciente,  "+
	"p.primer_nombre || ' ' || " +
	"(CASE WHEN p.segundo_nombre IS NULL THEN '' ELSE p.segundo_nombre END) || ' ' || " +
	"p.primer_apellido || ' ' || " +
	"(CASE WHEN p.segundo_apellido IS NULL THEN '' ELSE p.segundo_apellido END) as nombreCompletoPaciente,  "+
	"p.sexo AS sexo,  "+
	"ec.codigo AS codigoEstadoCita,  "+
	"ec.nombre AS nombreEstadoCita,  "+
	"c.observaciones AS observaciones,  "+
	"getReqPacCuentaDadaSol(c.numero_solicitud)  AS indicadorRequisitos,  "+
	"CASE WHEN c.numero_solicitud IS NULL THEN 0 ELSE getCuentaXSolicitud(c.numero_solicitud) END AS cuenta,  "+
	"a.codigo AS codigo_agenda,  "+
	"CASE WHEN a.codigo_medico IS NOT NULL THEN "+ValoresPorDefecto.getValorTrueParaConsultas()+" ELSE "+ValoresPorDefecto.getValorFalseParaConsultas()+" END AS pertenece_medico, "+
	"'false'  AS pyp  "+
	"FROM cita c INNER JOIN agenda a ON(a.codigo=c.codigo_agenda)  "+
	"	INNER JOIN estados_cita ec ON(ec.codigo=c.estado_cita)  "+
	"	INNER JOIN unidades_consulta uc ON(uc.codigo=c.unidad_consulta)  "+
	"	INNER JOIN personas p ON(p.codigo=c.codigo_paciente)  ";*/
	
	/**
	 * Seccion WHERE 01 (sin reservas) para la consulta de citas por atender 
	 */
	private static final String consultaCitasPorAtenderMedico_WHERE_01 =
	"WHERE "+ 
	"( "+ 
		"c.estado_cita IN ("+ConstantesBD.codigoEstadoCitaAsignada+"," +ConstantesBD.codigoEstadoCitaReprogramada + ") AND " +
		"c.estado_liquidacion = '" +ConstantesBD.codigoEstadoLiquidacionLiquidada+"' "+ 
	") AND (s.estado_historia_clinica = "+ConstantesBD.codigoEstadoHCSolicitada+" OR s.estado_historia_clinica = "+ConstantesBD.codigoEstadoHCAnulada+" OR (s.estado_historia_clinica = "+ConstantesBD.codigoEstadoHCRespondida+" AND gettiposervicio(sc.servicio) in ('"+ConstantesBD.codigoServicioNoCruentos+"','"+ConstantesBD.codigoServicioProcedimiento+"')) ) ";
	
	
	private static final String consultaCitasPorAtenderMedico_WHERE_02 =
	"WHERE "+ 
	"( "+ 
		"( "+ 
			"c.estado_cita IN (" +ConstantesBD.codigoEstadoCitaAsignada+"," +ConstantesBD.codigoEstadoCitaReprogramada + ") AND " +
			"c.estado_liquidacion = '" +ConstantesBD.codigoEstadoLiquidacionLiquidada+"' "+ 
		") "+ 
		"OR    " +
		"(" +
			"c.estado_cita IN (" +ConstantesBD.codigoEstadoCitaReservada+"," +ConstantesBD.codigoEstadoCitaReprogramada + "," +ConstantesBD.codigoEstadoCitaAsignada + ") AND "+
		    "c.estado_liquidacion = '" +ConstantesBD.codigoEstadoLiquidacionSinLiquidar+"' AND " +
			"c.convenio IS NOT NULL AND " +
			"c.contrato IS NOT NULL AND " +
			"c.estrato_social IS NOT NULL AND " +
			"c.tipo_afiliado IS NOT NULL " +
		") " +
	") AND (sc.numero_solicitud IS NULL OR s.estado_historia_clinica = "+ConstantesBD.codigoEstadoHCSolicitada+" OR s.estado_historia_clinica = "+ConstantesBD.codigoEstadoHCAnulada+" OR (s.estado_historia_clinica = "+ConstantesBD.codigoEstadoHCRespondida+" AND gettiposervicio(sc.servicio) in ('"+ConstantesBD.codigoServicioNoCruentos+"','"+ConstantesBD.codigoServicioProcedimiento+"'))) ";
	/**
	 * Secciï¿½n WHERE 03 final para la consulta de citas por atender
	 */
	/*private static final String consultaCitasPorAtenderMedico_WHERE_03 =
	"	AND  "+
	"	(  "+
	"		a.codigo_medico = ? OR  "+
	"		(  "+
	"			a.codigo_medico IS NULL AND  "+
	"			(SELECT count(1) FROM especialidades_medicos WHERE codigo_medico=? AND activa_sistema=true AND codigo_especialidad IN( SELECT uc.especialidad FROM unidades_consulta uc WHERE uc.codigo=c.unidad_consulta))>0  "+
	"		)  "+
	"	)  "+
	"	AND a.fecha = ?  "+
	"	AND a.centro_atencion = ?  "+
	"	ORDER BY horaInicioCita ASC ";*/	
	
	/**
	 * Consulta los Servicios Asociados a al Cita
	 * */
	
	private static final String strConsultaServiciosCita = "SELECT " +
														   "sc.codigo, " +
														   "sc.codigo_cita AS codigocita," +
														   "sc.servicio," +
														   "sc.servicio || ' - ' || getnombreservicio(sc.servicio,"+ConstantesBD.codigoTarifarioCups+") AS nombreservicio, "+
														   "gettiposervicio(sc.servicio) AS codigotiposervicio, " +
														   "CASE WHEN gettiposervicio(sc.servicio) =  '"+ConstantesBD.codigoServicioProcedimiento+"' THEN " +
														   " res.codigo  ELSE "+ConstantesBD.codigoNuncaValido+" END AS codigoresprocedimiento," +
														   "sc.estado," +
														   "sc.centro_costo AS centrocosto," +
														   "sc.numero_solicitud AS numerosolicitud," +
														   "getconsecutivosolicitud(sc.numero_solicitud) AS orden," +
														   "s.estado_historia_clinica AS codigoestadosolhis," +
														   "CASE WHEN sc.numero_solicitud IS NULL THEN ' ' ELSE getestadosolhis(s.estado_historia_clinica) END AS nombreestadosolhis," +
														   "CASE WHEN sc.numero_solicitud IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE s.tipo END AS tiposolicitud," +
														   "coalesce(sc.especialidad,"+ConstantesBD.codigoNuncaValido+") AS codigoespecialidad, "+
														   "s.pyp," +
														   "coalesce(sc.control_post_operatorio_cx,"+ConstantesBD.codigoNuncaValido+") as indipo, " +
														   "coalesce(c.observaciones_cancelacion,'') as observacionescancelacion, " +
														   "coalesce(m.descripcion,'') as motivocancelacion, " +
														   "coalesce(c.motivo_noatencion,'') AS motivo_noatencion," +
														   "sc.observaciones AS observaciones, "+	
														   "c.usuario_cancela AS usuario_cancela," +
														   "to_char(c.fecha_cancela, 'dd/mm/yyyy') AS fecha_cancela," +
														   "c.hora_cancela AS hora_cancela "+
														   "FROM " +
														   "servicios_cita sc " +
														   "INNER JOIN cita c ON(sc.codigo_cita=c.codigo) "+
														   "LEFT OUTER JOIN motivos_cancelacion_cita m ON(m.codigo=c.motivo_cancelacion) "+
														   "LEFT OUTER JOIN solicitudes s ON (s.numero_solicitud = sc.numero_solicitud) " +
														   "LEFT OUTER JOIN res_sol_proc res ON (res.numero_solicitud = sc.numero_solicitud) " +
														   "WHERE sc.codigo_cita = ? "; 


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap listarCitasPorAtenderMedico(	Connection con, HashMap campos)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		HashMap mapa = new HashMap();
		try
		{
			String cadena = "";
			cadena = consultaCitasPorAtenderMedico_SELECT;
			
			String validacionCitas = "" +
				"case  " +
				"  when a.codigo_medico is not null then 'S' " +
				"  when a.codigo_medico is null and gettiposervicio(sc.servicio) = 'C' and sc.especialidad IN ("+campos.get("especialidades")+") then 'S' " +
				"  when a.codigo_medico is null and (gettiposervicio(sc.servicio) = 'P' or gettiposervicio(sc.servicio) = 'D') and sc.numero_solicitud is null and sc.centro_costo = ? then 'S' " +
				"  when a.codigo_medico is null and (gettiposervicio(sc.servicio) = 'P' or gettiposervicio(sc.servicio) = 'D') and sc.numero_solicitud is not null and s.centro_costo_solicitado = ? then 'S'" +
				" else 'N' " +
				"end As mostrarcita ";
			
			String where = "FROM cita c "+ 
				"INNER JOIN servicios_cita sc ON(sc.codigo_cita = c.codigo) "+ 
				"INNER JOIN agenda a ON(a.codigo=c.codigo_agenda) "+  
				"INNER JOIN estados_cita ec ON(ec.codigo=c.estado_cita) "+  
				"INNER JOIN personas p ON(p.codigo=c.codigo_paciente) " +
				"INNER JOIN consultaexterna.unidades_consulta uc ON (c.unidad_consulta=uc.codigo) "+
				"LEFT OUTER JOIN solicitudes s ON (s.numero_solicitud = sc.numero_solicitud) ";
			
			cadena += validacionCitas+where;
			
			//VERIFICACION DE VALORES POR DEFECTO
			if(UtilidadTexto.getBoolean(ValoresPorDefecto.getCrearCuentaAtencionCitas(Integer.parseInt(campos.get("institucion").toString()))))
				cadena += consultaCitasPorAtenderMedico_WHERE_02;
			else
				cadena += consultaCitasPorAtenderMedico_WHERE_01;
			
			cadena += " AND sc.estado = '"+ConstantesIntegridadDominio.acronimoEstadoActivo+"' AND  "+
			//cadena += 
			//	" AND  "+
				"( "+
					//Se buscan las citas por el medico cargado en sesion
					"a.codigo_medico = "+campos.get("codigoMedico")+" OR "+  
					"( "+  
						//Pero tambiï¿½n se toman citas que no tengan mï¿½dico en la agenda...
						"a.codigo_medico IS NULL AND "+  
						//Siempre y cuando la especialidad del servicio concuerde con sus especialidades o 
						//el centro de costo del servicio concuerde con el centro de costo del medico
						"( "+
							"sc.especialidad IN ("+campos.get("especialidades")+") OR (sc.especialidad IS NULL and sc.centro_costo = "+campos.get("codigoCentroCosto")+" ) " +
						") " +
					") " +
				") "+  
				"AND to_char(a.fecha, '"+ConstantesBD.formatoFechaBD+"') = '"+UtilidadFecha.conversionFormatoFechaABD(campos.get("fecha").toString())+"' "+ 
				"AND a.centro_atencion = "+campos.get("codigoCentroAtencion")+" " +
				"AND uc.tipo_atencion='"+ConstantesIntegridadDominio.acronimoTipoAtencionGeneral+"' "+
				"ORDER BY hora_inicio_cita ASC, codigo_cita ASC";
			
			pst =  con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setObject(1,campos.get("institucion"));
			pst.setInt(2, Utilidades.convertirAEntero(campos.get("codigoCentroCosto")+""));
			pst.setInt(3, Utilidades.convertirAEntero(campos.get("codigoCentroCosto")+""));
			rs=pst.executeQuery();
			
			int cont=0;
			mapa.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					String alias=rsm.getColumnLabel(i).toLowerCase();
					int index=alias.indexOf("_");
					while(index>0)
					{
						index=alias.indexOf("_");
						try{
							String caracter=alias.charAt(index+1)+"";
							{
								alias=alias.replaceFirst("_"+caracter, caracter.toUpperCase());
							}
		}
						catch(IndexOutOfBoundsException e)
		{			
							break;
						}
					}
					mapa.put(alias+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapa.put("numRegistros", cont+"");
		}
		catch (Exception e) {
			Log4JManager.error("Error listarCitasPorAtenderMedico : " + e);
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
			} catch (Exception e) {
				Log4JManager.error("Error cerrando PreparedStatementDecorator - ResultSetDecorator :" + e);
			}
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param codPaciente
	 * @param codMedico
	 * @param fechaInicio
	 * @param fechaFin
	 * @param horaInicio
	 * @param horaFin
	 * @param unidadConsulta
	 * @param estadoLiquidacion
	 * @param consultorio
	 * @param estadosCita
	 * @param centroAtencion
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ResultadoCollectionDB listarCitas(Connection con,int codPaciente,int codMedico,String fechaInicio, String fechaFin, String horaInicio,String horaFin,int unidadConsulta, String estadoLiquidacion,int consultorio,String[] estadosCita, String centroAtencion, String postO, String tipoOrdenamiento, String sql) throws SQLException
	{
		
		PreparedStatement pst= null;
		ResultSet rs=null;
		Collection coleccion=new ArrayList();
		try{
		String condiciones=new String("");
		String condicionesEstadoCita="";
		// Comentado x rendimiento
		//condiciones+=" WHERE c.codigo_paciente=pp.codigo AND c.codigo_agenda=a.codigo AND  c.unidad_consulta=uc.codigo AND (pm.codigo=a.codigo_medico OR a.codigo_medico IS NULL) AND c.estado_cita=ec.codigo AND el.acronimo=c.estado_liquidacion AND a.consultorio=co.codigo";
		
		condiciones+=" WHERE ";
		
		if(fechaInicio!=null && fechaFin!=null && fechaInicio.equals(fechaFin) && !fechaInicio.equals("") && !fechaFin.equals("")){
			fechaInicio=UtilidadFecha.conversionFormatoFechaABD(fechaInicio);
			fechaFin=UtilidadFecha.conversionFormatoFechaABD(fechaFin);
			condiciones+=" AND (to_char(a.fecha, 'YYYY-MM-DD') = '"+fechaInicio +"' or a.fecha IS NULL)";
		}
		else
		{
			if(!fechaInicio.equals(""))
			{
				fechaInicio=UtilidadFecha.conversionFormatoFechaABD(fechaInicio);
				condiciones+=" AND (to_char(a.fecha, 'YYYY-MM-DD') >= '" +fechaInicio +"' or a.fecha IS NULL)";
			}
			if(!fechaFin.equals(""))
			{
				fechaFin=UtilidadFecha.conversionFormatoFechaABD(fechaFin);
				condiciones+="AND (to_char(a.fecha, 'YYYY-MM-DD') <= '" +fechaFin +"' or a.fecha IS NULL)";
			}
		}
		
		if(!horaInicio.equals(""))
			condiciones+=" AND (a.hora_inicio|| ''>='" + horaInicio +"' or a.hora_inicio IS NULL)";
		
		if(!horaFin.equals(""))
			condiciones+="AND (a.hora_fin || ''<='" + horaFin +"' or a.hora_fin IS NULL)";
		
		if(Integer.parseInt(centroAtencion)>0) 
			condiciones+=" AND a.centro_atencion="+centroAtencion;
		
		if(codPaciente!=-1) 
			condiciones+=" AND c.codigo_paciente="+codPaciente; 
		
		if(consultorio!=-1)
			condiciones+=" AND a.consultorio="+consultorio+" ";
		
		if(codMedico!=-1)
		{
			if(codMedico==0)
				condiciones+=" AND pm.codigo IS NULL";
			else
				condiciones+=" AND pm.codigo="+codMedico;
		}
		
		if(unidadConsulta>0)
			condiciones+=" AND uc.codigo="+unidadConsulta;
		
		if(!estadoLiquidacion.equals("-1")&& !estadoLiquidacion.equals(""))
			condiciones+=" AND c.estado_liquidacion='"+estadoLiquidacion+"'";
		
		
		
		if(postO.equals(ConstantesBD.acronimoNo)){
			/* Se documenta por la tarea 30750. Esta condición depende del check de Citas Control Post Operatorio.
			 * No hay ningún INNER en la consulta que se refiera a el objeto sc, podría aplicar 
			 * el objeto c = citas o uc = unidades consulta.
			*/
			//condiciones+=" AND sc.codigo is null ";
		}
			
		
		String estadosCitaTmp="";
		boolean todoEstado=false;
		logger.info("longitud de los estados cita desde sqlbase: "+estadosCita.length);
		if(estadosCita.length>0)
		{
			for(int i=0;i<estadosCita.length;i++)
			{
				if(estadosCita[i]!=null && !estadosCita[i].equals(""))
				{
					if(!estadosCita[i].equals("-1"))
						estadosCitaTmp += (estadosCitaTmp.equals("")?"":",") + estadosCita[i];
					else if(estadosCita[i].equals("-1"))
						todoEstado=true;
					
				}
			}
			if(!todoEstado)
			{//FIXME 
				if(estadosCitaTmp.length()>0){
					condiciones+=" AND ( ";
					for (int i = 0; i < estadosCitaTmp.split(",").length; i++) {
						if(!UtilidadTexto.isEmpty(estadosCitaTmp.split(",")[i])){
							condicionesEstadoCita+=" c.estado_cita ="+estadosCitaTmp.split(",")[i]+" ";
						}
						
						if(i!=estadosCitaTmp.split(",").length-1){
							condicionesEstadoCita+=" OR ";
						}
					}
					
					condiciones+=condicionesEstadoCita+" )";
				}
			}
		}
		if(tipoOrdenamiento.equals("fechaDescendente"))
			condiciones+=" ORDER BY fechaInicio DESC, horaInicio desc";
		else
			condiciones+=" ORDER BY nombrecentroatencion,uc.descripcion, fechaInicio, horaInicio ASC";
		
		if(condiciones.contains("WHERE AND") || condiciones.contains("WHERE  AND") || condiciones.contains("WHEREAND")){
			condiciones=condiciones.replace("WHERE AND", "WHERE ");
			condiciones=condiciones.replace("WHERE  AND", "WHERE ");
			condiciones=condiciones.replace("WHEREAND", "WHERE ");
		}
		
		if(condiciones.contains("WHERE ORDER") || condiciones.contains("WHERE  ORDER")|| condiciones.contains("WHEREORDER")){
			condiciones=condiciones.replace("WHERE ORDER", "ORDER ");
			condiciones=condiciones.replace("WHERE  ORDER", "ORDER ");
			condiciones=condiciones.replace("WHEREORDER", "ORDER ");
			Log4JManager.error("######## ERROR CONSULTA CITAS   codigoPaciente="+codPaciente+"  codigoMedico="+codMedico+" fechaInicio="+fechaInicio+" fechaFin="+fechaFin+" horaInicio="+horaInicio+" horaFin="+horaFin);
			return new ResultadoCollectionDB(true, "", coleccion);	
		}
		
		
		logger.info("\n\n Listado Citas-->"+sql+condiciones+"\n\n");
			pst= con.prepareStatement(sql+condiciones,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs = pst.executeQuery();		
			if(rs!=null && rs.getMetaData()!=null){
				ResultSetMetaData ars_rsm=rs.getMetaData();
				while(rs.next())
				{
					int numColumnas=ars_rsm==null?0:Utilidades.convertirAEntero(ars_rsm.getColumnCount()+"");
					HashMap mapa=new HashMap();
					for(int i=1;i<=ars_rsm.getColumnCount();i++)
					{
						mapa.put((ars_rsm.getColumnLabel(i)).toLowerCase()+"",rs.getObject(ars_rsm.getColumnLabel(i))==null||rs.getObject(ars_rsm.getColumnLabel(i)).toString().equals(" ")?"":rs.getObject(ars_rsm.getColumnLabel(i)));
					}
					coleccion.add(mapa);
				}
			}
		}
		catch (Exception e) {
			Log4JManager.error("ERROR listarCitas", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sqle) {
				Log4JManager.error("ERROR listarCitas", sqle);
			}
		}
		return new ResultadoCollectionDB(true, "", coleccion);	
	}
	
	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	public static ResultadoCollectionDB listarCitas(Connection con,int codPaciente, int idCuenta, String sql) throws SQLException
	{
		PreparedStatement pst = null;
		ResultSet rs= null;
		Collection coleccion=new ArrayList();
		try{
		String condiciones=new String("");
		condiciones+=" WHERE c.codigo_paciente=pp.codigo AND c.codigo_agenda=a.codigo AND  c.unidad_consulta=uc.codigo AND (pm.codigo=a.codigo_medico OR a.codigo_medico IS NULL) AND c.estado_cita=ec.codigo AND el.acronimo=c.estado_liquidacion AND a.consultorio=co.codigo";
		if(codPaciente!=-1){
			condiciones+=" AND c.codigo_paciente="+codPaciente;
		}

		if(idCuenta!=-1){
			condiciones+=" AND s.cuenta="+idCuenta;
		}
			
		condiciones+=" ORDER BY nombreUnidadConsulta, a.fecha || '' , a.hora_inicio || ''  ASC";
			pst= con.prepareStatement(sql+condiciones,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs = pst.executeQuery();		
			if(rs!=null && rs.getMetaData()!=null){
				ResultSetMetaData ars_rsm=rs.getMetaData();
				while(rs.next())
				{
					int numColumnas=ars_rsm==null?0:Utilidades.convertirAEntero(ars_rsm.getColumnCount()+"");
					HashMap mapa=new HashMap();
					for(int i=1;i<=ars_rsm.getColumnCount();i++)
					{
						mapa.put((ars_rsm.getColumnLabel(i)).toLowerCase()+"",rs.getObject(ars_rsm.getColumnLabel(i))==null||rs.getObject(ars_rsm.getColumnLabel(i)).toString().equals(" ")?"":rs.getObject(ars_rsm.getColumnLabel(i)));
					}
					coleccion.add(mapa);
				}
			}
		}
		catch (Exception e) {
			Log4JManager.error("ERROR listarCitas", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sqle) {
				Log4JManager.error("ERROR listarCitas", sqle);
			}
		}
		return new ResultadoCollectionDB(true, "", coleccion);	
				
	}
	
	
	/**
	 * Consulta los servicios asociados a la cita
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static HashMap serviciosCita(Connection con, HashMap parametros)
	{
		PreparedStatementDecorator ps;
		HashMap mapa = new HashMap();
		mapa.put("numRegistros","0");
		
		try
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultaServiciosCita,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("--->"+strConsultaServiciosCita+"---"+parametros.get("codigocita"));
			ps.setObject(1,parametros.get("codigocita"));
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			ps.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
				
		return  mapa;	
	}
	
	
	/**
	 * Mï¿½todo implementado para obtener le fecha de la primera cita del paciente
	 * en estado asignada o reservada
	 * @param con
	 * @param codigoPaciente
	 * @param tipoBD
	 * @return
	 */
	public static String obtenerFechaPrimeraCitaPaciente(Connection con,int codigoPaciente,int tipoBD)
	{
		String fecha = "";
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			//se modifica la consulta para que no valide el estado , y que busque todas las citas asociadas a el MT4527
			String consulta = " SELECT " +
									"to_char(a.fecha,'"+ConstantesBD.formatoFechaAp+"') AS fecha  " +
								"FROM " +
									"cita c " +
								"INNER JOIN " +
									"agenda a ON(a.codigo = c.codigo_agenda) " +
								"WHERE " +
									"c.codigo_paciente = "+codigoPaciente+//" and " +
									//"c.estado_cita in ("+ConstantesBD.codigoEstadoCitaAsignada+","+ConstantesBD.codigoEstadoCitaReservada+") " +
									"ORDER BY " +
									"a.fecha  asc" ;
				
			
			/*if(tipoBD==DaoFactory.POSTGRESQL)
				consulta += "order by a.fecha "+ValoresPorDefecto.getValorLimit1()+" 1";
			else if(tipoBD==DaoFactory.ORACLE)
				consulta += " and rownum = 1 order by a.fecha";*/
			
			logger.info("\n\n\n SQL / obtenerFechaPrimeraCitaAsignadaReservadaPaciente / "+consulta);
			
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs = pst.executeQuery();
			if(rs.next()){
				fecha = rs.getString("fecha");
		}
		}
		catch (Exception e) {
			Log4JManager.error("Error obtenerFechaPrimeraCitaAsignadaReservadaPaciente : " + e);
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
			} catch (Exception e) {
				Log4JManager.error("Error cerrando PreparedStatementDecorator - ResultSetDecorator :" + e);
			}
		}
		
		return fecha;
	}
}