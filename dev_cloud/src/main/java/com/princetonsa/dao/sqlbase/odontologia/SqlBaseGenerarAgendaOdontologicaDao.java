package com.princetonsa.dao.sqlbase.odontologia;

import java.sql.Connection;
import java.sql.Types;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.consultaExterna.DtoExcepcionesHorarioAtencion;
import com.princetonsa.dto.odontologia.DtoAgendaOdontologica;
import com.princetonsa.dto.odontologia.DtoExistenciaAgenOdon;
import com.princetonsa.dto.odontologia.DtoHorarioAtencion;
import com.princetonsa.mundo.parametrizacion.CentroAtencion;

/**
 * 
 * @author Víctor Hugo Gómez L. 
 *
 */ 
public class SqlBaseGenerarAgendaOdontologicaDao 
{
	/**
	 * mensajes de arror
	 * */
	static Logger logger = Logger.getLogger(SqlBaseGenerarAgendaOdontologicaDao.class);
	
	/**
	 *cadena que inserta una unidad de agenda odontologica
	 */
	private static final String strInsertAgendaOdontologica = "INSERT INTO odontologia.agenda_odontologica( " +
			"codigo_pk, " +
			"centro_atencion, " +
			"unidad_agenda, " +
			"consultorio, " +
			"dia, " +
			"fecha, " +
			"hora_inicio, " +
			"hora_fin, " +
			"codigo_medico, " +
			"usuario_modifica, " +
			"fecha_modifica, " +
			"hora_modifica, " +
			"cupos, " +
			"activo) " +
			"VALUES(?,?,?,?,?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?, '" + ConstantesBD.acronimoSi+ "')";
	
	/**
	 *cadena que busca los horarios de atencion 
	 */
	private static final String strConsultaHorarioAtencion = "SELECT " + 
			"ha.codigo as codigo, " +
			"ha.unidad_consulta as uni_consulta, " +
			"CASE WHEN ha.consultorio IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE ha.consultorio END as consultorio, " +
			"CASE WHEN ha.dia IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE ha.dia END as dia, " +
			"CASE WHEN ha.codigo_medico IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE ha.codigo_medico END as cod_med, " +
			"ha.hora_inicio||'' as hora_ini, " +
			"ha.hora_fin||'' as hora_fin, " +
			"ha.tiempo_sesion as tiempo_sesion, " +
			"ha.pacientes_sesion as pacs_sesion, " +
			"CASE WHEN ha.centro_atencion IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE ha.centro_atencion END as cen_aten " +
			"FROM consultaexterna.horario_atencion ha " + 
			"INNER JOIN consultaexterna.unidades_consulta uc ON (ha.unidad_consulta = uc.codigo) " + 
			"LEFT OUTER JOIN administracion.centro_atencion ca ON (ha.centro_atencion = ca.consecutivo) " + 
			"LEFT OUTER JOIN consultaexterna.consultorios c ON (ha.consultorio = c.codigo) " +
			"LEFT OUTER JOIN administracion.dias_semana ds ON (ha.dia = ds.codigo) " +
			"LEFT OUTER JOIN administracion.medicos m ON (ha.codigo_medico = m.codigo_medico) " + 
			"WHERE 1=1 ";
	
	/**
	 * cadena que consulta las agendas odontologicas
	 */
	private static final String strConsultaAgendaOdontologica = "SELECT " + 
			"ao.codigo_pk as codigo, " +
			"ao.centro_atencion as cen_aten, " +
			"ca.descripcion as desp_centro_atencion, " +
			"ao.unidad_agenda as uni_agenda, " +
			"uc.color as color_uni_agenda, " +
			"uc.descripcion as desp_unid_agend, " +
			"ao.consultorio as consultorio, " +
			"c.descripcion as desp_consultorio, " +
			"ao.dia as dia, " +
			"CASE WHEN ao.codigo_medico IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE ao.codigo_medico END as cod_med, " +
			"CASE WHEN ao.codigo_medico IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE m.convencion END as convencion_prof, " +
			"CASE WHEN ao.codigo_medico IS NOT NULL THEN getnombrepersona2(per.codigo) ELSE 'No Asignado' END as nombre_prof, " +
			"ao.fecha as fecha, " +
			"ao.hora_inicio||'' as hora_ini, " +
			"ao.hora_fin||'' as hora_fin, " +
			"ao.cupos as cupos " +
			"FROM odontologia.agenda_odontologica ao " + 
			"INNER JOIN consultaexterna.unidades_consulta uc ON (ao.unidad_agenda = uc.codigo) " + 
			"INNER JOIN administracion.centro_atencion ca ON (ao.centro_atencion = ca.consecutivo) " + 
			"INNER JOIN consultaexterna.consultorios c ON (ao.consultorio = c.codigo) " +
			"INNER JOIN administracion.dias_semana ds ON (ao.dia = ds.codigo) " +
			"LEFT OUTER JOIN administracion.personas per ON (ao.codigo_medico = per.codigo) " +
			"LEFT OUTER JOIN medicos m ON (m.codigo_medico=ao.codigo_medico) " + 
			"WHERE ao.activo = '"+ConstantesBD.acronimoSi+"' ";
	
	/**
	 * cadena de verificacion de excepciones de los centros de atencion
	 */
	private static final String strVerificarExcepcionCenAten = "SELECT " +
			"CASE WHEN COUNT(ea.centro_atencion)<=0 THEN '"+ConstantesBD.acronimoNo+"' ELSE '"+ConstantesBD.acronimoSi+"' END AS hay_excepcion " +
			"FROM consultaexterna.excepciones_agenda ea " +
			"WHERE ea.centro_atencion = ? AND ea.fecha = ? "; 
	
	/**
	 * cadena que verifica la existencia de la agena odontologica
	 */
	private static final String strVerificarExistenciaAgendaOdon = "SELECT " +
			"ag.codigo_pk as codigo_pk, " +
			"ag.hora_inicio as hora_ini, " +
			"ag.hora_fin as hora_fin " +
			"FROM odontologia.agenda_odontologica ag " +
			"WHERE ag.centro_atencion = ? " +
			"AND ag.unidad_agenda = ? " +
			"AND ag.consultorio = ? " +
			"AND ag.dia = ? " +
			"AND ag.fecha = ? " +
			"AND activo = '"+ConstantesBD.acronimoSi+"' ";
	
	/**
	 * cadena que consulta las descripciones de la agenda odontologica
	 */
	private static final String strConsultarAgendaOdontologica = "SELECT " +
			"ao.codigo_pk as codigo_pk, " +
			"ao.fecha as fecha, " + 
			"ca.descripcion as centro_atencion, " +
			"uc.descripcion as unidad_consulta, " +
			"con.descripcion as consultorio, " +
			"CASE WHEN ao.codigo_medico IS NOT NULL THEN getnombrepersona2(per.codigo) ELSE 'No Asignado' END as nombre_prof, " +
			"ao.hora_inicio AS horainicio, " +
			"ao.hora_fin AS horafin " +  
			"FROM odontologia.agenda_odontologica ao " +
			"INNER JOIN administracion.centro_atencion ca ON (ca.consecutivo = ao.centro_atencion) " + 
			"INNER JOIN consultaexterna.unidades_consulta uc ON (uc.codigo = ao.unidad_agenda) " +
			"INNER JOIN consultaexterna.consultorios con ON (con.codigo = ao.consultorio) " +
			"LEFT OUTER JOIN administracion.personas per ON (per.codigo = ao.codigo_medico) " +
			"WHERE codigo_pk = ?  ";
	
	/**
	 * cadena que consulta las descripciones de la agenda odontologica
	 */
	private static final String strConsultarHorarioAtencion = "SELECT " + 
			"ca.descripcion as centro_atencion, " +
			"uc.descripcion as unidad_consulta, " +
			"CASE WHEN ha.codigo_medico IS NOT NULL THEN getnombrepersona2(per.codigo) ELSE 'No Asignado' END as nombre_prof " +  
			"FROM consultaexterna.horario_atencion ha " +
			"INNER JOIN administracion.centro_atencion ca ON (ca.consecutivo = ha.centro_atencion) " + 
			"INNER JOIN consultaexterna.unidades_consulta uc ON (uc.codigo = ha.unidad_consulta) " +
			"LEFT OUTER JOIN administracion.personas per ON (per.codigo = ha.codigo_medico) " +
			"WHERE ha.codigo = ? ";
			/*"ha.centro_atencion = ? " +
			"AND ha.unidad_consulta   = ? " +
			"AND ha.dia             = ? " +
			"AND ha.hora_inicio     = ? " +
			"AND ha.hora_fin        =  ? ";
			*/
	/**
	 * Consulta que arroja los datos para verificar si un consultorio está utilizado en una agenda
	 */
	private static final String strConsultaConsultoriosDisponibles =
			"SELECT " +
				"count(0) as cantidad " +
			"FROM " +
				"odontologia.agenda_odontologica " +
			"WHERE " +
					"activo='"+ConstantesBD.acronimoSi+"' " +
				"AND " +
					"consultorio = ? ";
	
	private static String cosultaExAgendaOdontologica=
			"SELECT " +
				"eha.codigo_pk AS codigo, " +
				"eha.centro_atencion AS codcentroat, " +
				"getnomcentroatencion(eha.centro_atencion) AS nomcentroat, " +
				"eha.unidad_agenda AS coduniagenda, " +
				"getnombreunidadconsulta(eha.unidad_agenda) AS nomuniagenda, " +
				"eha.consultorio AS codconsultorio, " +
				"getnombreconsultorio(eha.consultorio) AS nomconsultorio, " +
				"eha.profesional_salud AS codprofesional, " +
				"getnombremedico(eha.profesional_salud) AS nomprofesional, " +
				"eha.fecha_inicio AS fechaini, " +
				"eha.fecha_finalizacion AS fechafin, " +
				"eha.hora_inicio AS horaini, " +
				"eha.hora_finalizacion AS horafin, " +
				"eha.observaciones AS observ, " +
				"eha.es_generar_agenda AS es_generar_agenda " +
			"FROM " +
				"consultaexterna.ex_horario_atencion eha " +
			"WHERE " +
				"eha.eliminado= '"+ConstantesBD.acronimoNo+"' ";
	
	
	public static ArrayList<DtoExcepcionesHorarioAtencion> cosultaExAgendaOdontologica(
			Connection con, String centroAtencion, String unidadAgenda,
			String consultorio, String diaSemana, String profesionalSalud,
			String fechaInicial, String fechaFinal, Boolean esGenerar)
	{
		ArrayList<DtoExcepcionesHorarioAtencion> lista = new ArrayList<DtoExcepcionesHorarioAtencion>();
		
		String consulta = cosultaExAgendaOdontologica;
		String where = "";
		String order = "";
		try{
			where += "AND eha.centro_atencion IN ("+centroAtencion+") ";
			where += "AND eha.unidad_agenda IN ("+unidadAgenda+") ";
			
			if(!consultorio.equals("") && !consultorio.equals(ConstantesBD.codigoNuncaValido+""))
			{
				where += "AND eha.consultorio = "+consultorio+" ";
			}
						
			if(!profesionalSalud.equals("") && !profesionalSalud.equals(ConstantesBD.codigoNuncaValido+"")) // no asignado
			{
				where += "AND eha.profesional_salud = "+profesionalSalud+" ";
			}

			if(!fechaInicial.equals("") && !fechaFinal.equals(""))
			{
				where +=
					"AND (" +
						"( to_char(eha.fecha_inicio,'"+ConstantesBD.formatoFechaBD+"') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"' )" +
						"OR " +
						"( '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' BETWEEN to_char(eha.fecha_inicio,'"+ConstantesBD.formatoFechaBD+"') AND to_char(eha.fecha_finalizacion,'"+ConstantesBD.formatoFechaBD+"') )" +
					")";
			}
			
			if(esGenerar!=null)
			{
				if(esGenerar)
				{
					where+="AND es_generar_agenda = '"+ConstantesBD.acronimoSi+"'";
				}
				else
				{
					where+="AND es_generar_agenda = '"+ConstantesBD.acronimoNo+"'";
				}
			}
			
			order +="ORDER BY eha.fecha_inicio ";
			consulta += where + order;
			
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con,consulta);
			//Log4JManager.info("\n\nconsulta excepcion agenda:::::"+ps);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoExcepcionesHorarioAtencion dto=new DtoExcepcionesHorarioAtencion();
				//DtoAgendaOdontologica dto = new DtoAgendaOdontologica();
				dto.setCodigoPk(rs.getInt("codigo"));
				CentroAtencion dtoCentroAtencion=new CentroAtencion();
				dtoCentroAtencion.setConsecutivo(rs.getInt("codcentroat"));
				dtoCentroAtencion.setDescripcion(rs.getString("nomcentroat"));
				dto.setCentroAtencion(dtoCentroAtencion);
				dto.setUnidadAgenda(rs.getInt("coduniagenda"));
				dto.setDescripcionUniAgen(rs.getString("nomuniagenda"));
				dto.setConsultorio(rs.getInt("codconsultorio"));
				dto.setDescripcionConsultorio(rs.getString("nomconsultorio"));
				dto.setCodigoMedico(rs.getInt("codprofesional"));
				dto.setNombreMedico(rs.getString("nomprofesional"));
				dto.setObservaciones(rs.getString("observ"));
				dto.setHoraInicio(rs.getString("horaini"));
				dto.setHoraFin(rs.getString("horafin"));
				dto.setFechaInicio(rs.getString("fechaini"));
				dto.setFechaFin(rs.getString("fechafin"));
				dto.setEsGenerar(rs.getString("es_generar_agenda"));
				lista.add(dto);
			}
			rs.close();
			ps.close();
		}catch (Exception e) {
			Log4JManager.error("Error listando las excepciones al horario de atención", e);
		}
		return lista;
	}
	
	
	/**
	 * Se obtiene un listado de los horarios de atención que cumplen con los parámetros 
	 * seleccionados para la generación de la agenda odontológica
	 * @param con Conexión con la BD
	 * @param centroAtencion Ce
	 * @param unidadConsulta
	 * @param consultorio
	 * @param diaSemana
	 * @param codigoMedico
	 * @return Lista de horarios de atención
	 */
	public static ArrayList<DtoHorarioAtencion> obtenerHorariosAtencion(Connection con, 
			String centroAtencion, 
			String unidadConsulta, 
			String consultorio,
			String diaSemana,
			String codigoMedico)
	{
		ArrayList<DtoHorarioAtencion> array = new ArrayList<DtoHorarioAtencion>();
		String consulta = strConsultaHorarioAtencion;
		String where = "";
		String order = "";
		try{
			where += "AND ha.centro_atencion IN ("+centroAtencion+") ";
			where += "AND ha.unidad_consulta IN ("+unidadConsulta+") ";
			
			if(Utilidades.convertirAEntero(consultorio)>0)
				where += "AND ha.consultorio = "+consultorio+" ";
			
			if(!diaSemana.equals(ConstantesBD.codigoNuncaValido+""))
				where += "AND ha.dia = "+diaSemana+" ";
			
			if(codigoMedico.equals(ConstantesBD.codigoNuncaValido+"")) // no asignado
				where += "AND ha.codigo_medico IS NULL ";
			else
				if(!codigoMedico.equals(""))
					where += "AND ha.codigo_medico = "+codigoMedico+" ";
			
			order +="ORDER BY ha.hora_inicio, ca.descripcion, uc.descripcion ASC ";
			
			consulta += where + order;
			//logger.info("Consulta: "+consulta);
			//logger.info("centro Atencion: "+centroAtencion+" | unidad agenda: "+unidadConsulta+" | consultorio: "+consulta+" | dia: "+diaSemana+" | medico: "+codigoMedico);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consulta);
			logger.info("consulta:::::"+consulta);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoHorarioAtencion horarioAten = new DtoHorarioAtencion();
				int dia = ConstantesBD.codigoNuncaValido;
				horarioAten.setCodigo(rs.getInt("codigo"));
				horarioAten.setUnidadConsulta(rs.getInt("uni_consulta"));
				horarioAten.setConsultorio(rs.getInt("consultorio"));
				horarioAten.setDia(rs.getInt("dia"));
				horarioAten.setCodigoMedico(rs.getInt("cod_med"));
				horarioAten.setHoraInicio(rs.getString("hora_ini"));
				horarioAten.setHoraFin(rs.getString("hora_fin"));
				horarioAten.setTiempoSesion(rs.getInt("tiempo_sesion"));
				horarioAten.setPacientesSesion(rs.getInt("pacs_sesion"));
				horarioAten.setCentroAtencion(rs.getInt("cen_aten"));
				array.add(horarioAten);
			}
			rs.close();
			ps.close();
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Consulta: "+consulta);
			logger.info("centro Atencion: "+centroAtencion+" | unidad agenda: "+unidadConsulta+" | consultorio: "+consulta+" | dia: "+diaSemana+" | medico: "+codigoMedico);
		}
		return array;
	}
	
	
	/**
	 * se obtiene un listado de los horarios de atencion para la regeneracin de agenda odontologica
	 * @param con
	 * @param codigoHorarioAtencion
	 * @return
	 */
	public static ArrayList<DtoHorarioAtencion> obtenerHorariosAtencion(Connection con, String codigoHorarioAtencion)
	{
		ArrayList<DtoHorarioAtencion> array = new ArrayList<DtoHorarioAtencion>();
		String consulta = strConsultaHorarioAtencion;
		String where = "";
		String order = "";
		try{
			where += "AND ha.codigo IN ("+codigoHorarioAtencion+") ";
			order +="ORDER BY ha.hora_inicio, ca.descripcion, uc.descripcion ASC ";
			consulta += where + order;
			//logger.info("Consulta: "+consulta);
			//logger.info("Codigos Horarios Atencion: "+codigoHorarioAtencion);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoHorarioAtencion horarioAten = new DtoHorarioAtencion();
				int dia = ConstantesBD.codigoNuncaValido;
				horarioAten.setCodigo(rs.getInt("codigo"));
				horarioAten.setUnidadConsulta(rs.getInt("uni_consulta"));
				horarioAten.setConsultorio(rs.getInt("consultorio"));
				horarioAten.setDia(rs.getInt("dia"));
				horarioAten.setCodigoMedico(rs.getInt("cod_med"));
				horarioAten.setHoraInicio(rs.getString("hora_ini"));
				horarioAten.setHoraFin(rs.getString("hora_fin"));
				horarioAten.setTiempoSesion(rs.getInt("tiempo_sesion"));
				horarioAten.setPacientesSesion(rs.getInt("pacs_sesion"));
				horarioAten.setCentroAtencion(rs.getInt("cen_aten"));
				array.add(horarioAten);
			}
			rs.close();
			ps.close();
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Error Consulta: "+consulta);
			logger.info("Codigos Horarios Atencion: "+codigoHorarioAtencion);
		}
		return array;
	}
	
	/**
	 * verifica las excepciones del centro de atencion para la generacion de la agenda
	 * @param con
	 * @param fecha
	 * @param centroAtencion
	 * @return
	 */
	public static String verificarExcepcionCentroAtencion(Connection con, String fecha, int centroAtencion)
	{
		String result = ConstantesBD.acronimoSi;
		try{
			//logger.info("Consulta: "+strVerificarExcepcionCenAten);
			//logger.info("fecha: "+fecha+" centro atencion: "+centroAtencion);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, strVerificarExcepcionCenAten);
			ps.setInt(1, centroAtencion);
			ps.setString(2, UtilidadFecha.conversionFormatoFechaABD(fecha));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				result = rs.getString("hay_excepcion");
			rs.close();
			ps.close();
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Consulta: "+strVerificarExcepcionCenAten);
			logger.info("fecha: "+fecha+" centro atencion: "+centroAtencion);
		}
		return result;
	}
	
	/**
	 * se verifica si la agenda odontologica ya fue generada 
	 * @param con
	 * @param centroAtencion
	 * @param unidadAgenda
	 * @param consultorio
	 * @param diaSemana
	 * @param fecha
	 * @param horaIni
	 * @param horaFin
	 * @return ArrayList<DtoExistenciaAgenOdon>
	 */
	public static ArrayList<DtoExistenciaAgenOdon> verificarExistenciaAgendaOdontologica(Connection con, 
			int centroAtencion,
			int unidadAgenda,
			int consultorio,
			int diaSemana,
			String fecha)
	{
		ArrayList<DtoExistenciaAgenOdon> array = new ArrayList<DtoExistenciaAgenOdon>();
		try{
			//logger.info("Consulta: "+strVerificarExistenciaAgendaOdon);
			//logger.info("centro atencion: "+centroAtencion+" | unidad agenda: "+unidadAgenda+" | consultorio: "+consultorio+" | dia: "+diaSemana+" | fecha: "+fecha);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con ,strVerificarExistenciaAgendaOdon );
			ps.setInt(1, centroAtencion);
			ps.setInt(2, unidadAgenda);
			ps.setInt(3, consultorio);
			ps.setInt(4, diaSemana);
			ps.setString(5, UtilidadFecha.conversionFormatoFechaABD(fecha));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoExistenciaAgenOdon dto = new DtoExistenciaAgenOdon();
				dto.setCodigoPk(rs.getInt("codigo_pk"));
				dto.setHoraIni(rs.getString("hora_ini"));
				dto.setHoraFin(rs.getString("hora_fin"));
				array.add(dto);
			}
			logger.info("verificarExistenciaAgendaOdontologica \n\n\n\n"+ps+"\n\n\n\n");
			rs.close();
			ps.close();
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Consulta: "+strVerificarExistenciaAgendaOdon);
			logger.info("centro atencion: "+centroAtencion+" | unidad agenda: "+unidadAgenda+" | consultorio: "+consultorio+" | dia: "+diaSemana+" | fecha: "+fecha);
		}
		return array;
	}
	
	/**
	 * se verifica si la agenda odontologica ya fue generada 
	 * @param con
	 * @param centroAtencion
	 * @param unidadAgenda
	 * @param consultorio
	 * @param diaSemana
	 * @param fecha
	 * @param horaIni
	 * @param horaFin
	 * @return ArrayList<DtoExistenciaAgenOdon>
	 */
	public static String isInsertAgendaOdontologica(Connection con, DtoAgendaOdontologica dto)
	{
		String result = ConstantesBD.acronimoSi;
		String consulta = "SELECT " +
				"CASE WHEN COUNT(ag.codigo_pk) > 0 THEN '"+ConstantesBD.acronimoNo+"' ELSE '"+ConstantesBD.acronimoSi+"' END AS is_insertar, " +
				"COUNT(ag.codigo_pk) AS numAgendas " +
				"FROM odontologia.agenda_odontologica ag " +
				"WHERE ag.centro_atencion = ? " +
				"AND ag.unidad_agenda = ? " +
				"AND ag.dia = ? " +
				"AND ag.fecha = ? " +
				"AND " +
				"(" +
						"ag.hora_inicio >= ? AND ag.hora_inicio < ? " +
					"OR " +
						"ag.hora_fin > ? AND ag.hora_fin <= ? " +
				")" +
				"AND ag.activo = ? ";
		if(dto.getCodigoMedico()>0)
		{
			consulta+="AND ag.codigo_medico = ? ";
		}
		else
		{
			consulta+="AND ag.codigo_medico IS NULL ";
		}
		try{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consulta);
			ps.setInt(1, dto.getCentroAtencion());
			ps.setInt(2, dto.getUnidadAgenda());
			ps.setInt(3, dto.getDia());
			ps.setString(4, UtilidadFecha.conversionFormatoFechaABD(dto.getFecha()));
			ps.setString(5, dto.getHoraInicio());
			ps.setString(6, dto.getHoraFin());
			ps.setString(7, dto.getHoraInicio());
			ps.setString(8, dto.getHoraFin());
			ps.setString(9, ConstantesBD.acronimoSi);
			if(dto.getCodigoMedico()!=ConstantesBD.codigoNuncaValido)
			{
				ps.setInt(10, dto.getCodigoMedico());
			}
/*			if(dto.getConsultorio()>0)
			{
				ps.setInt(10, dto.getConsultorio());
			}*/
			Log4JManager.info(ps);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				logger.info("paso por aqui "+rs.getString("numAgendas"));
				result = rs.getString("is_insertar")+ConstantesBD.separadorSplit+rs.getInt("numAgendas");
				logger.info("\n\nlo q retorna:: "+result);
			}
			
			logger.info("ps: "+ps+"; result: "+result);
			rs.close();
			ps.close();
			
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Consulta: "+consulta);
			logger.info("centro atencion: "+dto.getCentroAtencion()+" | unidad agenda: "+dto.getUnidadAgenda()+" | consultorio: "+dto.getConsultorio()+" | dia: "+dto.getDia()+" | fecha: "+dto.getFecha()+" hora inui: "+dto.getHoraInicio()+" hora fin: "+dto.getHoraFin()+" cod medico: "+dto.getCodigoMedico());
		}
		return result;
	}
	
	
	/**
	 * insertar agenda odontologica
	 * @param con
	 * @param dto
	 * @return
	 */
	public static int insertarAgendaOdontologica(Connection con, DtoAgendaOdontologica dto)
	{
		try
		{
			int codigoPK = UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_agenda_odontologia");
			logger.info("centro atencion: "+dto.getCentroAtencion()+" | unida agenda: "+dto.getUnidadAgenda()+" cosnultorio: "+dto.getConsultorio()+" | dia: "+dto.getDia()+" | fecha: "+dto.getFecha()+" | hora ini:"+dto.getHoraInicio()+" | hora fin: "+dto.getHoraFin()+" | codigo medi: "+dto.getCodigoMedico()+" | cupos:"+dto.getCupos());
			logger.info("codigo pk: [agenda odontologica] "+codigoPK);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strInsertAgendaOdontologica);
			ps.setInt(1, codigoPK);
			ps.setInt(2, dto.getCentroAtencion());
			ps.setInt(3, dto.getUnidadAgenda());
			ps.setInt(4, dto.getConsultorio());
			ps.setInt(5, dto.getDia());
			ps.setString(6, UtilidadFecha.conversionFormatoFechaABD(dto.getFecha()));
			ps.setString(7, dto.getHoraInicio());
			ps.setString(8, dto.getHoraFin());
			if(dto.getCodigoMedico()!=ConstantesBD.codigoNuncaValido)
				ps.setInt(9, dto.getCodigoMedico());
			else
				ps.setNull(9, Types.INTEGER);
			ps.setString(10, dto.getUsuarioModifica());
			ps.setInt(11, dto.getCupos());
			
			logger.info("\n\nConsulta: "+ps);
			
			if(ps.executeUpdate()>0){
				logger.info("codigo pk a retornar: "+codigoPK);
				return codigoPK;
			}ps.close();
		}catch (Exception e) {
			logger.info("Consulta: "+strInsertAgendaOdontologica);
			logger.info("centro atencion: "+dto.getCentroAtencion()+" | unida agenda: "+dto.getUnidadAgenda()+" cosnultorio: "+dto.getConsultorio()+" | dia: "+dto.getDia()+" | fecha: "+dto.getFecha()+" | hora ini:"+dto.getHoraInicio()+" | hora fin: "+dto.getHoraFin()+" | codigo medi: "+dto.getCodigoMedico()+" | cupos:"+dto.getCupos());
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * actualizar los tiempos de la agenda odontologica
	 * @param con
	 * @param dto
	 * @param horaIni
	 * @param horaFin
	 * @return
	 */
	public static boolean actualizarAgendaOdontologica(Connection con, DtoAgendaOdontologica dto, boolean horaIni, boolean horaFin)
	{
		boolean band = false;
		String consulta = "UPDATE odontologia.agenda_odontologica SET ";
		String campo = "";
		String where = "";
		try{
			if(horaIni)
			{
				campo += "hora_inicio = '"+dto.getHoraInicio()+"' ";
				band = true;
			}
			if(horaFin)
			{
				if(band)
					campo +=" , ";
				campo += "hora_fin = '"+dto.getHoraFin()+"' ";
			}
			where += "WHERE codigo_pk = ? ";
			consulta +=campo+where;
			logger.info("Consulta: "+consulta+" codigoPk:"+dto.getCodigoPk());
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, dto.getCodigoPk());
			if(pst.executeUpdate()>0){
				pst.close();
				return true;
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Consulta: "+consulta+" codigoPk:"+dto.getCodigoPk());
		}
		return false;
	}
	
	/**
	 * metodo que consulta los datos descriptivos de una agenda odontologica
	 * @param con
	 * @param dto
	 * @return
	 */
	public static DtoAgendaOdontologica consultarAgendaOdontologica(Connection con, DtoAgendaOdontologica dto)
	{
		try{
			/*logger.info("\n\n\n\n");
			logger.info("Consulta: "+strConsultarAgendaOdontologica);
			logger.info("Codigo PK: "+dto.getCodigoPk());
			logger.info("\n\n\n\n");*/
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strConsultarAgendaOdontologica);
			ps.setInt(1, dto.getCodigoPk());
			logger.info("\n\nla consultarAgendaOdontologica--------->:\n\n "+ps+"\n\n\n\n");
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				dto.setCodigoPk(rs.getInt("codigo_pk"));
				dto.setFecha(rs.getString("fecha"));
				dto.setDescripcionCentAten(rs.getString("centro_atencion"));
				dto.setDescripcionUniAgen(rs.getString("unidad_consulta"));
				dto.setDescripcionConsultorio(rs.getString("consultorio"));
				dto.setNombreMedico(rs.getString("nombre_prof"));
				dto.setHoraInicio(rs.getString("horainicio"));
				dto.setHoraFin(rs.getString("horafin"));
			}
			rs.close();
			ps.close();
		}catch (Exception e) {
			logger.info("Error Consulta: "+e);
			logger.info("Codigo PK: "+dto.getCodigoPk());
			e.printStackTrace();
		}
		return dto;
	}
	
	/**
	 * metodo que consulta los datos descriptivos de una agenda odontologica pero a partir del horario
	 * @param con
	 * @param dto
	 * @return
	 */
	public static DtoAgendaOdontologica consultarHorarioAtencion(Connection con, DtoAgendaOdontologica dto)
	{
		try{
			/*logger.info("\n\n\n\n");
			logger.info("Consulta: "+strConsultarHorarioAtencion);
			logger.info("centro atencion: "+dto.getCentroAtencion()+" unidad consulta: "+dto.getUnidadAgenda()+ " dia: "+dto.getDia()+" hora ini: "+dto.getHoraInicio()+" hora fin: "+dto.getHoraFin());
			logger.info("\n\n\n\n");*/
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultarHorarioAtencion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, dto.getCodigoHorarioAtencion());
			/*ps.setInt(1, dto.getCentroAtencion());
			ps.setInt(2, dto.getUnidadAgenda());
			ps.setInt(3, dto.getDia());
			ps.setString(4, dto.getHoraInicio());
			ps.setString(5, dto.getHoraFin());*/
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				dto.setDescripcionCentAten(rs.getString("centro_atencion"));
				dto.setDescripcionUniAgen(rs.getString("unidad_consulta"));
				dto.setNombreMedico(rs.getString("nombre_prof"));
			}
			rs.close();
			ps.close();
		}catch (Exception e) {
			logger.info("Error Consulta: "+strConsultarHorarioAtencion);
			logger.info("Codigo PK: "+dto.getCodigoPk());
			e.printStackTrace();
		}
		return dto;
	}
	
	/**
	 * se obtiene un listado de los horarios de atencion que cumple con los parametros 
	 * selecionados para la generacion de la agenda odontologica
	 * @param con
	 * @param centroAtencion
	 * @param unidadConsulta
	 * @param consultorio
	 * @param diaSemana
	 * @param codigoMedico
	 * @return
	 */
	public static ArrayList<DtoAgendaOdontologica> cosultaAgendaOdontologica(Connection con, 
			String centroAtencion, 
			String unidadConsulta, 
			String consultorio,
			String diaSemana,
			String codigoMedico,
			String fechaIni,
			String fechaFin)
	{
		ArrayList<DtoAgendaOdontologica> array = new ArrayList<DtoAgendaOdontologica>();
		String consulta = strConsultaAgendaOdontologica;
		String where = "";
		String order = "";
		try{
			where += "AND ao.centro_atencion IN ("+centroAtencion+") ";
			where += "AND ao.unidad_agenda IN ("+unidadConsulta+") ";
			
			if(!consultorio.equals(ConstantesBD.codigoNuncaValido+""))
				where += "AND ao.consultorio = "+consultorio+" ";
			
			if(!diaSemana.equals(ConstantesBD.codigoNuncaValido+""))
				where += "AND ao.dia = "+diaSemana+" ";
			
			if(codigoMedico.equals(ConstantesBD.codigoNuncaValido+"")) // no asignado
				where += "AND ao.codigo_medico IS NULL ";
			else
				if(!codigoMedico.equals(""))
					where += "AND ao.codigo_medico = "+codigoMedico+" ";

			if(!fechaIni.equals("") && !fechaFin.equals(""))
				where += "AND to_char(ao.fecha,'"+ConstantesBD.formatoFechaBD+"') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaIni)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFin)+"' ";
			
			if(!fechaIni.equals("") && fechaFin.equals(""))
				where += "AND to_char(ao.fecha,'"+ConstantesBD.formatoFechaBD+"') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaIni)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaIni)+"' ";
			
			order +="ORDER BY ao.fecha, ao.hora_inicio, ca.descripcion, uc.descripcion ASC ";
			consulta += where + order;
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consulta);
			logger.info("\n\n **************** " +
					"Consulta generar agenda: "+ps+"\n\n\n\n");
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoAgendaOdontologica dtoAgeOdo = new DtoAgendaOdontologica();
				dtoAgeOdo.setCodigoPk(rs.getInt("codigo"));
				dtoAgeOdo.setCentroAtencion(rs.getInt("cen_aten"));
				dtoAgeOdo.setDescripcionCentAten(rs.getString("desp_centro_atencion"));
				dtoAgeOdo.setUnidadAgenda(rs.getInt("uni_agenda"));
				dtoAgeOdo.setColorUniAgen(rs.getString("color_uni_agenda"));
				dtoAgeOdo.setDescripcionUniAgen(rs.getString("desp_unid_agend"));
				dtoAgeOdo.setConsultorio(rs.getInt("consultorio"));
				dtoAgeOdo.setDescripcionConsultorio(rs.getString("desp_consultorio"));
				dtoAgeOdo.setDia(rs.getInt("dia"));
				dtoAgeOdo.setCodigoMedico(rs.getInt("cod_med"));
				dtoAgeOdo.setNombreMedico(rs.getString("nombre_prof"));
				dtoAgeOdo.setConvencion(rs.getInt("convencion_prof"));
				dtoAgeOdo.setFecha(rs.getString("fecha"));
				dtoAgeOdo.setHoraInicio(rs.getString("hora_ini"));
				dtoAgeOdo.setHoraFin(rs.getString("hora_fin"));
				dtoAgeOdo.setCupos(rs.getInt("cupos"));
				array.add(dtoAgeOdo);
			}
			rs.close();
			ps.close();
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Consulta: "+consulta);
			logger.info("centro Atencion: "+centroAtencion+" | unidad agenda: "+unidadConsulta+" | consultorio: "+consulta+" | dia: "+diaSemana+" | medico: "+codigoMedico);
		}
		return array;
	}
	
	/**
	 * Método que identifica si un consultorio está ocupado
	 * @param con
	 * @param consultorio
	 * @return
	 */
	public static boolean existeConsultoriosAgendados (Connection con, 
			int consultorio, int dia, String fecha, String horaInicio, String horaFin, int centroAtencion)
	{
		String consulta = strConsultaConsultoriosDisponibles;
		try{
			if(dia>0)
			{
				logger.info(">>> Agrego filtro día");
				consulta += " AND dia = ?";
			}
			if(!fecha.equals(""))
			{
				logger.info(">>> Agrego filtro fecha");
				consulta += " AND fecha = ?";
			}
			if(!horaInicio.equals("") && !horaFin.equals(""))
			{
				logger.info(">>> Agrego filtro horaInicio y horaFin");//hora_fin
				consulta += " AND ((hora_inicio between ? AND ? )" +
						" OR (hora_fin between ? AND ? ))";
			}
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con, consulta);
			pst.setInt(1, consultorio);
			int cont=2;
			if(dia>0)
			{
				pst.setInt(cont, dia);
				cont++;
			}
			if(!fecha.equals(""))
			{
				pst.setString(cont, fecha);
				cont++;
			}
			if(!horaInicio.equals("") && !horaFin.equals(""))
			{
				pst.setString(cont, horaInicio);
				cont++;
				pst.setString(cont, horaFin);
				cont++;
				pst.setString(cont, horaInicio);
				cont++;
				pst.setString(cont, horaFin);
				cont++;
			}
			
			logger.info(">>>Consulta: "+pst);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				if(rs.getInt("cantidad")>0)
				{
					rs.close();
					pst.close();
					return true;
				}	
			}
			
			rs.close();
			pst.close();
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Consulta: "+consulta);
			//logger.info("centro Atencion: "+centroAtencion+" | unidad agenda: "+unidadConsulta+" | consultorio: "+consulta+" | dia: "+diaSemana+" | medico: "+codigoMedico);
		}
		return false;
	}
	
}
