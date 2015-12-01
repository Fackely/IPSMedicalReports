package com.princetonsa.dao.sqlbase.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoAgendaOdontologica;
import com.princetonsa.dto.odontologia.DtoCuposAgendaOdo;

/**
 * 
 * @author V�ctor Hugo G�mez L.
 *
 */

public class SqlBaseAgendaOdontologicaDao 
{
	/**
	 * mensajes de error
	 * */
	static Logger logger = Logger.getLogger(SqlBaseAgendaOdontologicaDao.class);
	
	/**
	 * cadena que consulta una agenda odontologica
	 */
	private static final String strConsultarAgendaOdon = "SELECT " + 
			"ao.codigo_pk as codigo, " +
			"ao.centro_atencion as cen_aten, " +
			"ca.descripcion as desp_centro_atencion, " +
			"ao.unidad_agenda as uni_agenda, " +
			"uc.descripcion as desp_unid_agend, " +
			"coalesce(uc.color,' ') as color, " +
			"ao.consultorio as consultorio, " +
			"c.descripcion as desp_consultorio, " +
			"ao.dia as dia, " +
			"CASE WHEN ao.codigo_medico IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE ao.codigo_medico END as cod_med, " +
			"CASE WHEN ao.codigo_medico IS NOT NULL THEN getNombrePersona(per.codigo) ELSE ' ' END as nombre_prof, " +
			"CASE WHEN ao.codigo_medico IS NOT NULL THEN esp.codigo ELSE "+ConstantesBD.codigoNuncaValido+" END as cod_esp_med, " +
			"CASE WHEN ao.codigo_medico IS NOT NULL THEN esp.nombre ELSE 'No Asignado' END as nom_esp_med, " +
			"to_char(ao.fecha,'"+ConstantesBD.formatoFechaBD+"') as fecha, " +
			"ao.hora_inicio||'' as hora_ini, " +
			"ao.hora_fin||'' as hora_fin, " +
			"ao.cupos as cupos, " +
			"uc.especialidad as cod_espec_unid_agend " +
			"FROM odontologia.agenda_odontologica ao " + 
			"INNER JOIN consultaexterna.unidades_consulta uc ON (ao.unidad_agenda = uc.codigo) " +
			"INNER JOIN administracion.especialidades esp ON (esp.codigo = uc.especialidad) " +
			"INNER JOIN administracion.centro_atencion ca ON (ao.centro_atencion = ca.consecutivo) " + 
			"INNER JOIN consultaexterna.consultorios c ON (ao.consultorio = c.codigo) " +
			"INNER JOIN administracion.dias_semana ds ON (ao.dia = ds.codigo) " +
			"LEFT OUTER JOIN administracion.personas per ON (ao.codigo_medico = per.codigo) " +
			"WHERE ao.codigo_pk = ? ";
	
	
	/**
	 * cadena de consulta de uniades de consulta
	 */
	private static final String strConsultarUnidadesConsulta = "SELECT DISTINCT " +
			"uc.codigo as codigo, " +
			"uc.descripcion as desp_uni_con, " +
			"coalesce(uc.color,' ') as color " +
			"FROM consultaexterna.unidades_consulta uc " +
			"INNER JOIN odontologia.agenda_odontologica ao ON (ao.unidad_agenda = uc.codigo) " +
			"WHERE uc.activa = "+ValoresPorDefecto.getValorTrueParaConsultas()+" " +
			"AND uc.tipo_atencion = '"+ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica+"' " +
			"AND uc.codigo IN ("+ConstantesBD.separadorSplit+") " +
			"AND ao.fecha = ? ";
	
	/**
	 * sentencia sql que actualiza el numero de cupos de la agenda 
	 */
	private static final String strModificarNumeroCuposAgendaOdo = "UPDATE odontologia.agenda_odontologica SET " +
			"cupos = ?, " +
			"usuario_modifica = ?, " +
			"fecha_modifica = CURRENT_DATE, " +
			"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" "+
			"WHERE codigo_pk = ? ";
	
	/**
	 * Cadena para obtener los cupos de la agenda odontologica
	 */
	private static final String strObtenerCuposAgenda = "SELECT ao.cupos as cupos from odontologia.agenda_odontologica ao WHERE ao.codigo_pk = ?";
	
	/**
	 * Cadena para consultar un registro de cupos agenda odo
	 */
	private static final String strConsultarCuposAgendaOdo = "SELECT " +
		"cao.codigo_pk as codigo_pk," +
		"cao.agenda_odontologica as agenda_odontologica," +
		"cao.hora as hora," +
		"cao.cupos_disponibles as cupos_disponibles," +
		"to_char(cao.fecha_modifica,'"+ConstantesBD.formatoFechaBD+"') as fecha_modifica," +
		"cao.hora_modifica as hora_modifica," +
		"cao.usuario_modifica as usuario_modifica " +
		"from odontologia.cupos_agenda_odo cao " +
		"WHERE cao.agenda_odontologica = ? and cao.hora = ?";
	
	/**
	 * Cadena para insertar un registro de cupos agenda odo 
	 */
	private static final String strInsertarCuposAgendaOdo = "INSERT INTO odontologia.cupos_agenda_odo (codigo_pk,agenda_odontologica,hora,cupos_disponibles,fecha_modifica,hora_modifica,usuario_modifica) values (?,?,?,?,current_date,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?)";
	
	
	/**
	 * Cadena para insertar el log de cupos de la genda odontologica
	 */
	private static final String strInsertarLogCuposAgendaOdo  = "INSERT INTO odontologia.log_cupos_agenda_odo (codigo_pk,cupo_agenda_odo,cupos_disponibles,fecha_modifica,hora_modifica,usuario_modifica) VALUES(?,?,?,current_date,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?) ";
	
	/**
	 * se obtiene un listado de los horarios de atencion que cumple con los parametros selecionados
	 * @param con
	 * @param centroAtencion
	 * @param unidadConsulta
	 * @param consultorio
	 * @param diaSemana
	 * @param codigoMedico
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<DtoAgendaOdontologica> cosultarAgendaOdontologica(Connection con, 
			String centroAtencion, 
			String unidadConsulta, 
			String codigoMedico,
			String fecha,
			String tipoCita)
	{
		ArrayList<DtoAgendaOdontologica> array = new ArrayList<DtoAgendaOdontologica>();
		String consulta = "SELECT " + 
					"ao.codigo_pk as codigo, " +
					"ao.centro_atencion as cen_aten, " +
					"ca.descripcion as desp_centro_atencion, " +
					"ao.unidad_agenda as uni_agenda, " +
					"uc.descripcion as desp_unid_agend, " +
					"coalesce(uc.color,' ') as color, " +
					"ao.consultorio as consultorio, " +
					"c.descripcion as desp_consultorio, " +
					"ao.dia as dia, " +
					"CASE WHEN ao.codigo_medico IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE ao.codigo_medico END as cod_med, " +
					"CASE WHEN ao.codigo_medico IS NOT NULL THEN getnombrepersona2(per.codigo) ELSE ' ' END as nombre_prof, " +
					"CASE WHEN ao.codigo_medico IS NOT NULL THEN esp.codigo ELSE "+ConstantesBD.codigoNuncaValido+" END as cod_esp_med, " +
					"CASE WHEN ao.codigo_medico IS NOT NULL THEN esp.nombre ELSE ' ' END as nom_esp_med, " +
					"to_char(ao.fecha,'"+ConstantesBD.formatoFechaBD+"') as fecha, " +
					"ao.hora_inicio||'' as hora_ini, " +
					"ao.hora_fin||'' as hora_fin, " +
					"ao.cupos as cupos," +
					"uc.especialidad as cod_espec_unid_agend, " +
					"administracion.getnombreespecialidad(uc.especialidad) as nom_espec_unid_agend " +
					"FROM odontologia.agenda_odontologica ao " + 
					"INNER JOIN consultaexterna.unidades_consulta uc ON (ao.unidad_agenda = uc.codigo) " +
					"INNER JOIN administracion.especialidades esp ON (esp.codigo = uc.especialidad) " +
					"INNER JOIN administracion.centro_atencion ca ON (ao.centro_atencion = ca.consecutivo) " + 
					"INNER JOIN consultaexterna.consultorios c ON (ao.consultorio = c.codigo) " +
					"INNER JOIN administracion.dias_semana ds ON (ao.dia = ds.codigo) " +
					"LEFT OUTER JOIN administracion.personas per ON (ao.codigo_medico = per.codigo) " +
					"WHERE ao.activo = '"+ConstantesBD.acronimoSi+"' ";
		String where = "";
		String order = "";
		try{
			where += "AND ao.centro_atencion IN ("+centroAtencion+") ";
			where += "AND ao.unidad_agenda IN ("+unidadConsulta+") ";
			
			/*if(!consultorio.equals(ConstantesBD.codigoNuncaValido+""))
				where += "AND ao.consultorio = "+consultorio+" ";*/
			
			/*if(!diaSemana.equals(ConstantesBD.codigoNuncaValido+""))
				where += "AND ao.dia = "+diaSemana+" ";*/
			
			/*
			if(codigoMedico.equals(ConstantesBD.codigoNuncaValido+"")) // no asignado
				where += "AND ao.codigo_medico IS NULL ";
			else*/
			if(!codigoMedico.equals("") && !codigoMedico.equals("Seleccione"))
				where += "AND ao.codigo_medico = "+codigoMedico+" ";

			if(!fecha.equals("") )
				where += "AND to_char(ao.fecha,'"+ConstantesBD.formatoFechaBD+"') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fecha)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fecha)+"' ";
			
			//*********filtros dependiendo del tipo de cita***************************************
			if(tipoCita.equals(ConstantesIntegridadDominio.acronimoTipoAtencionValoracionInicial) || 
				tipoCita.equals(ConstantesIntegridadDominio.acronimoRevaloracion) || 
				tipoCita.equals(ConstantesIntegridadDominio.acronimoAuditoria) ||
				tipoCita.equals(ConstantesIntegridadDominio.acronimoPrioritaria)||
				tipoCita.equals(ConstantesIntegridadDominio.acronimoRemisionInterconsulta
						))
			{
				where += " AND (" +
					"(SELECT count(1) from consultaexterna.servicios_unidades_consulta suc inner join servicios s ON(s.codigo=suc.codigo_servicio) WHERE suc.unidad_consulta = uc.codigo and s.tipo_servicio = '"+ConstantesBD.codigoServicioInterconsulta+"' ) > 0 " +
					"OR  " +
					"(SELECT count(1) from odontologia.serv_adicionales_profesionales sap inner join servicios s ON(s.codigo=sap.codigo_servicio) WHERE sap.codigo_medico = ao.codigo_medico and s.tipo_servicio = '"+ConstantesBD.codigoServicioInterconsulta+"' ) > 0 " +
					") ";
			}
			else if(tipoCita.equals(ConstantesIntegridadDominio.acronimoTratamiento) ||
					tipoCita.equals(ConstantesIntegridadDominio.acronimoControlCitaOdon))
			{
				where += " AND (" +
				"(SELECT count(1) from consultaexterna.servicios_unidades_consulta suc inner join servicios s ON(s.codigo=suc.codigo_servicio) WHERE suc.unidad_consulta = uc.codigo and s.tipo_servicio = '"+ConstantesBD.codigoServicioProcedimiento+"' ) > 0 " +
				"OR  " +
				"(SELECT count(1) from odontologia.serv_adicionales_profesionales sap inner join servicios s ON(s.codigo=sap.codigo_servicio) WHERE sap.codigo_medico = ao.codigo_medico and s.tipo_servicio = '"+ConstantesBD.codigoServicioProcedimiento+"' ) > 0 " +
				") ";
			}
			//************************************************************************************
			
			order +=" ORDER BY ao.fecha, ao.hora_inicio, c.descripcion, uc.descripcion, ca.descripcion ASC ";
			consulta += where + order;
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consulta);
			logger.info("\n\n CONSULTA AGENDA ODONTOLOGICA \n "+ps+"\n\n\n\n\n");
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoAgendaOdontologica dtoAgeOdo = new DtoAgendaOdontologica();
				logger.info("rs.getInt(codigo) "+rs.getInt("codigo"));
				dtoAgeOdo.setCodigoPk(rs.getInt("codigo"));
				dtoAgeOdo.setCentroAtencion(rs.getInt("cen_aten"));
				dtoAgeOdo.setDescripcionCentAten(rs.getString("desp_centro_atencion"));
				dtoAgeOdo.setUnidadAgenda(rs.getInt("uni_agenda"));
				dtoAgeOdo.setDescripcionUniAgen(rs.getString("desp_unid_agend"));
				dtoAgeOdo.setColorUniAgen(rs.getString("color").trim());
				dtoAgeOdo.setConsultorio(rs.getInt("consultorio"));
				dtoAgeOdo.setDescripcionConsultorio(rs.getString("desp_consultorio"));
				dtoAgeOdo.setDia(rs.getInt("dia"));
				dtoAgeOdo.setCodigoMedico(rs.getInt("cod_med"));
				dtoAgeOdo.setNombreMedico(rs.getString("nombre_prof").equals(" ")?"":rs.getString("nombre_prof"));
				dtoAgeOdo.setEspecialidadMedico(new InfoDatosInt(rs.getInt("cod_esp_med"),rs.getString("nom_esp_med").equals(" ")?"":rs.getString("nom_esp_med")));
				dtoAgeOdo.setFecha(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fecha")));
				dtoAgeOdo.setHoraInicio(rs.getString("hora_ini"));
				dtoAgeOdo.setHoraFin(rs.getString("hora_fin"));
				dtoAgeOdo.setCupos(rs.getInt("cupos"));
				dtoAgeOdo.setEspecialidadUniAgen(rs.getInt("cod_espec_unid_agend"));
				dtoAgeOdo.setNombreEspecialidadUniAgen(rs.getString("nom_espec_unid_agend"));
				logger.info("dtoAgeOdo.getCodigoPk() "+dtoAgeOdo.getCodigoPk());
				if(dtoAgeOdo.getCodigoPk()>0)
				{
					HashMap<String, Integer> cuposDisponibles=new HashMap<String, Integer>();
					HashMap parametros = new HashMap();
					parametros.put("codigo_agenda", dtoAgeOdo.getCodigoPk());

					// consulto los cupos
					String sentenciaCupos=
							"SELECT " +
								"hora AS hora, " +
								"cupos_disponibles AS cupos_disponibles " +
							"FROM " +
								"odontologia.cupos_agenda_odo " +
							"WHERE " +
								"agenda_odontologica=?";
					PreparedStatementDecorator psdCupos=new PreparedStatementDecorator(con, sentenciaCupos);
					psdCupos.setInt(1, dtoAgeOdo.getCodigoPk());
					logger.info(psdCupos);
					ResultSetDecorator rsCupos=new ResultSetDecorator(psdCupos.executeQuery());
					while(rsCupos.next())
					{
						cuposDisponibles.put(rsCupos.getString("hora"), rsCupos.getInt("cupos_disponibles"));
					}

					dtoAgeOdo.setCitasOdontologicas(SqlBaseCitaOdontologicaDao.consultarCitaOdontologicaXAgenda(con, parametros));
					dtoAgeOdo.setCuposEspacioTiempo(cuposDisponibles);
					
				}
				array.add(dtoAgeOdo);
			}
			rs.close();
			ps.close();
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Consulta: "+consulta);
			logger.info("centro Atencion: "+centroAtencion+" | unidad agenda: "+unidadConsulta+" | medico: "+codigoMedico+" | fecha : "+fecha+" | tipo cita: "+tipoCita);
		}
		return array;
	}
	
	
	
	/**
	 * Convenciones de Colores por Unidad de Agenda
	 * @param con
	 * @param unidadesAgenda
	 * @return
	 */
	public static ArrayList<InfoDatosString> convencionColorUnidaAgenda(Connection con, String unidadesAgenda, String fecha)
	{
		ArrayList<InfoDatosString> array = new ArrayList<InfoDatosString>();
		String consulta =  strConsultarUnidadesConsulta;
		try{
			if(!unidadesAgenda.equals(""))
				consulta = consulta.replace(ConstantesBD.separadorSplit, unidadesAgenda);
			else
				consulta = consulta.replace(ConstantesBD.separadorSplit, ConstantesBD.codigoNuncaValido+"");
			
			logger.info("Consulta: "+consulta);
			logger.info("fecha: "+fecha);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDate(1, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fecha)));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				InfoDatosString datConvCol = new InfoDatosString(rs.getInt("codigo")+"",
						rs.getString("desp_uni_con"),
						rs.getString("color").trim());
				array.add(datConvCol);
			}
			rs.close();
			ps.close();
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Consulta: "+consulta);
		}
		return array;
		
	}
	
	/**
	 * se obtiene una agenda odontologica 
	 * @param con
	 * @param incluirInactivos 
	 * @param codigo_pk
	 * @return
	 */
	public static DtoAgendaOdontologica consultaAgendaOdontologica(Connection con, int codigoPK, boolean incluirInactivos)
	{
		DtoAgendaOdontologica dtoAgeOdo = new DtoAgendaOdontologica();
		try{
			String sentenciaSQL=strConsultarAgendaOdon;
			if(!incluirInactivos)
			{
				sentenciaSQL += "AND ao.activo = '"+ConstantesBD.acronimoSi+"' ";
			}
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, sentenciaSQL);
			ps.setInt(1, codigoPK);
			logger.info(ps);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				dtoAgeOdo.setCodigoPk(rs.getInt("codigo"));
				logger.info("centro Antencion >> "+rs.getInt("cen_aten"));
				dtoAgeOdo.setCentroAtencion(rs.getInt("cen_aten"));
				dtoAgeOdo.setDescripcionCentAten(rs.getString("desp_centro_atencion"));
				dtoAgeOdo.setUnidadAgenda(rs.getInt("uni_agenda"));
				dtoAgeOdo.setDescripcionUniAgen(rs.getString("desp_unid_agend"));
				dtoAgeOdo.setColorUniAgen(rs.getString("color").trim());
				dtoAgeOdo.setConsultorio(rs.getInt("consultorio"));
				dtoAgeOdo.setDescripcionConsultorio(rs.getString("desp_consultorio"));
				dtoAgeOdo.setDia(rs.getInt("dia"));
				dtoAgeOdo.setCodigoMedico(rs.getInt("cod_med"));
				dtoAgeOdo.setNombreMedico(rs.getString("nombre_prof").equals(" ")?"":rs.getString("nombre_prof"));
				dtoAgeOdo.setEspecialidadMedico(new InfoDatosInt(rs.getInt("cod_esp_med"),rs.getString("nom_esp_med").equals(" ")?"":rs.getString("nom_esp_med")));
				dtoAgeOdo.setFecha(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fecha")));
				dtoAgeOdo.setHoraInicio(rs.getString("hora_ini"));
				dtoAgeOdo.setHoraFin(rs.getString("hora_fin"));
				dtoAgeOdo.setCupos(rs.getInt("cupos"));
				dtoAgeOdo.setEspecialidadUniAgen(rs.getInt("cod_espec_unid_agend"));
			}
			rs.close();
			ps.close();
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Consulta: "+strConsultarAgendaOdon);
			logger.info("codigo_pk: "+codigoPK);
		}
		return dtoAgeOdo;
	}
	
	/**
	 * modificar el numero de cupos de la agenda odontologica
	 * @param con
	 * @param codigoPK
	 * @param horaFin 
	 * @param usuarioModifica
	 * @param institucion 
	 * @param cupos
	 * @return
	 */
	public static int modificarCuposAgendaOdontologica(Connection con, 
			int codigoPK,
			String horaInicio,
			String horaFin,
			String usuarioModifica,
			boolean disminuir,
			int institucion)
	{
	
		int resp0 = 0;
		
		int minutos=Utilidades.convertirAEntero(ValoresPorDefecto.getMultiploMinGeneracionCita(institucion));
		
		String horaTrabajar=horaInicio;
		String horaAnt=horaInicio;
		logger.info("Entro a modificar CUPOS  Hora Inicio >>"+horaInicio +" hora Fin>>"+horaFin);
		try
		{				
			while(horaTrabajar.compareTo(horaFin)<0 && horaAnt.compareTo(horaTrabajar)<=0)
			{					
				DtoCuposAgendaOdo cupo = new DtoCuposAgendaOdo();
				//*******Se verifica si ya existe un registro de cupos agenda od******************+
				PreparedStatementDecorator pst = new PreparedStatementDecorator(con,strConsultarCuposAgendaOdo);
				pst.setInt(1,codigoPK);
				pst.setString(2,horaTrabajar);
				logger.info(" PASO 1 consulta CUPOS Agenda "+ pst);
				ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
				if(rs.next())
				{
					cupo.setCodigoPk(rs.getBigDecimal("codigo_pk"));
					cupo.setAgendaOdontologica(rs.getBigDecimal("agenda_odontologica"));
					cupo.setHora(rs.getString("hora"));
					cupo.setCuposDisponibles(rs.getInt("cupos_disponibles"));
					cupo.setFechaModifica(rs.getString("fecha_modifica"));
					cupo.setHoraModifica(rs.getString("hora_modifica"));
					cupo.getUsuarioModifica().setLoginUsuario(rs.getString("usuario_modifica"));
				}
				UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
				//***********************************************************************************
				
				logger.info("codigoPk Cupos Agenda Odon "+cupo.getCodigoPk());
				if(cupo.getCodigoPk().doubleValue()>0)
				{
					
					//*************************SE ACTUALIZA EL REGISTRO ACTUAL DE CUPOS AGENDA ODO***********************
					//1) Se inserta el log
					pst = new PreparedStatementDecorator(con,strInsertarLogCuposAgendaOdo);
					
					int secuenciaLog = UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_log_cupos_agenda_odo");
					pst.setInt(1, secuenciaLog);
					pst.setBigDecimal(2, cupo.getCodigoPk());
					pst.setInt(3, cupo.getCuposDisponibles());
					pst.setString(4,cupo.getUsuarioModifica().getLoginUsuario());
					
					resp0 = pst.executeUpdate();
					UtilidadBD.cerrarObjetosPersistencia(pst, null, null);
					
					if(resp0>0)
					{
						//2) Se actualiza el registro de cupos agenda odo
						String consulta = "";
						
						if(disminuir)
						{
							consulta = "UPDATE odontologia.cupos_agenda_odo SET cupos_disponibles = cupos_disponibles - 1, fecha_modifica = current_date, hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+",usuario_modifica = ? WHERE codigo_pk = ?";
						}
						else
						{
							consulta = "UPDATE odontologia.cupos_agenda_odo SET cupos_disponibles = cupos_disponibles + 1, fecha_modifica = current_date, hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+",usuario_modifica = ? WHERE codigo_pk = ?";
						}
						
						pst = new PreparedStatementDecorator(con,consulta);
						pst.setString(1,usuarioModifica);
						pst.setBigDecimal(2, cupo.getCodigoPk());
						logger.info("Modificacion >> "+pst);
						resp0 = pst.executeUpdate();
						UtilidadBD.cerrarObjetosPersistencia(pst, null, null);
					}
					
					
					//****************************************************************************************************
				}
				else
				{
					//*******************SE INSERTA EL REGISTRO DE CUPOS AGENDA ODO******************************************
					//1) Se consulta el cupo de la agenda
					pst = new PreparedStatementDecorator(con,strObtenerCuposAgenda);
					pst.setInt(1, codigoPK);
					logger.info("Si no existe...Obtener cupos Agenda "+pst);
					rs = new ResultSetDecorator(pst.executeQuery());
					
					if(rs.next())
					{
						cupo.setCuposDisponibles(rs.getInt("cupos")-1);
					}
					UtilidadBD.cerrarObjetosPersistencia(pst, null, null);
					
					if(cupo.getCuposDisponibles()>=0)
					{
						cupo.setCodigoPk(new BigDecimal(UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_cupos_agenda_odo")));
						cupo.setAgendaOdontologica(new BigDecimal(codigoPK));
						cupo.setHora(horaTrabajar);
						
						pst = new PreparedStatementDecorator(con,strInsertarCuposAgendaOdo);
						pst.setBigDecimal(1, cupo.getCodigoPk());
						pst.setBigDecimal(2, cupo.getAgendaOdontologica());
						pst.setString(3,cupo.getHora());
						pst.setInt(4,cupo.getCuposDisponibles());
						pst.setString(5,usuarioModifica);
						logger.info("Insertar Cupos Agenda >>"+pst);
						resp0 = pst.executeUpdate();
						logger.info("Valor Insert >>"+ resp0 );
						UtilidadBD.cerrarObjetosPersistencia(pst, null, null);
						
					}
					
					
					//*********************************************************************************************************
				}
				horaAnt=horaTrabajar;
				horaTrabajar=UtilidadFecha.incrementarMinutosAHora(horaTrabajar, minutos);
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			logger.info("Consulta: "+strConsultarAgendaOdon);
			logger.info("codigo_pk: "+codigoPK);
		}
		return resp0;
	}
	
}
