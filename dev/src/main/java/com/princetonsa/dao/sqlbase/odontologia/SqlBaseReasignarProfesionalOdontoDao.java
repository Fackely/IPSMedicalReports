/**
 * 
 */
package com.princetonsa.dao.sqlbase.odontologia;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.princetonsa.dto.odontologia.DtoReasignarProfesionalOdonto;

/**
 * @author Jairo Andrés Gómez
 * noviembre 9 / 2009
 *
 */
public class SqlBaseReasignarProfesionalOdontoDao {

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseReasignarProfesionalOdontoDao.class);
	
	private final static String sqlConsultarEspecialidadUnidadAgenda = "select especialidad AS especialidad " +
			"from consultaexterna.unidades_consulta " +
			"where codigo = ?";
	
	private final static String sqlConsultarServiciosAReasignar = 
		"SELECT aodo.codigo_pk                                            AS codigo           ," +
		"administracion.getnomcentroatencion(aodo.centro_atencion)       AS centroatencion    ," +
		"administracion.getnombremedico(aodo.codigo_medico)              AS profesionalsalud  ," +
		"aodo.fecha                                                      AS fechaagenda       ," +
		"aodo.hora_inicio                                                AS horainicio        ," +
		"aodo.hora_fin                                                   AS horafin           ," +
		"consultaexterna.getnombreunidadconsulta(aodo.unidad_agenda)     AS unidadagenda      ," +
		"aodo.unidad_agenda                                              AS codigounidadagenda," +
		"consultaexterna.getnombreconsultorio(aodo.consultorio)          AS consultorio " +
		"FROM odontologia.agenda_odontologica aodo " +
		"WHERE TO_CHAR(aodo.fecha, 'yyyy-mm-dd') BETWEEN ? AND ? " +
		"AND aodo.codigo_medico   = ? ";
	
	private final static String sqlConfirmacionDisponibilidadAgenda = " SELECT COUNT(codigo_pk) AS disponibilidad " +
			"FROM odontologia.agenda_odontologica aodo " +
			"WHERE aodo.codigo_medico           = ? " +
			"AND TO_CHAR(aodo.fecha,'yyyy-mm-dd') = ? " +
			"AND (? BETWEEN aodo.hora_inicio AND aodo.hora_fin " +
			"OR ? BETWEEN aodo.hora_inicio AND aodo.hora_fin)";
	
	private final static String sqlActualizarAgendaOdontologica = "UPDATE odontologia.agenda_odontologica " +
			"SET codigo_medico  = ?," +
			"  usuario_modifica = ?, " +
			"  fecha_modifica   = ?, " +
			"  hora_modifica    = ? " +
			"  WHERE codigo_pk  = ?";
	
	private final static String sqlInsertarLogReasignarProfesionalOdonto = 
		"INSERT INTO odontologia.log_reasignar_prof_odo " +
		"(CODIGO_PK, FECHA_PROCESO, HORA_PROCESO, USUARIO_PROCESO, CODIGO_AGENDA, CODIGO_MEDICO_ANTERIOR, " +
		"CODIGO_MEDICO_ASIGNADO, CENTRO_ATENCION, INSTITUCION, FECHA_AGENDA, HORA_AGENDA_INICIAL, HORA_AGENDA_FINAL, " +
		"UNIDAD_CONSULTA) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private final static String sqlConsultarLogReasignacionProfesional = 
		"SELECT administracion.getnomcentroatencion(reapro.centro_atencion) AS centroatencion            ," +
		"administracion.getnombremedico(reapro.codigo_medico_anterior)     AS profesionalsaludinicial   ," +
		"administracion.getnombremedico(reapro.codigo_medico_asignado)     AS profesionalsaludreasignado," +
		"reapro.fecha_proceso                                              AS fechaproceso              ," +
		"reapro.hora_proceso                                               AS horaproceso               ," +
		"reapro.usuario_proceso                                            AS usuarioproceso            ," +
		"reapro.fecha_agenda                                               AS fechaagenda               ," +
		"reapro.hora_agenda_inicial                                        AS horaagendainicial         ," +
		"reapro.hora_agenda_final                                          AS horaagendafinal           ," +
		"consultaexterna.getnombreunidadconsulta(reapro.unidad_consulta)   AS unidadconsulta " +
		"FROM odontologia.log_reasignar_prof_odo reapro " +
		"WHERE to_char(reapro.fecha_proceso,'yyyy-mm-dd') BETWEEN ? AND ? " +
		"AND reapro.centro_atencion = ? ";
	
	/**
	 * Método que consulta la especialidad de una unidad de agenda(consulta).
	 * @param connection
	 * @param unidadAgenda
	 * @return
	 */
	public static int consultarEspecialidadUnidadAgenda (Connection connection, int unidadAgenda){
		int especialidad = 0;
		try{
			PreparedStatementDecorator ps = new PreparedStatementDecorator(connection, sqlConsultarEspecialidadUnidadAgenda);
			ps.setInt(1, unidadAgenda);
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next()){
				especialidad = rs.getInt("especialidad");
			}
			
			rs.close();
			ps.close();
		}catch (Exception e) {
			logger.error("Ha ocurrido un error consultando la especialidad de la unidad de agenda--->"+e);
		}
		return especialidad;
	}
	
	/**
	 * Método que retorna el conjunto de registros resultado de la busqueda de servicios para reasignar profesional
	 * @param connection
	 * @param dtoReasignarProfesionalOdonto
	 * @return
	 */
	public static ArrayList<DtoReasignarProfesionalOdonto> consultarServiciosAReasignar (Connection connection, DtoReasignarProfesionalOdonto dtoReasignarProfesionalOdonto){
		
		ArrayList<DtoReasignarProfesionalOdonto> array = new ArrayList<DtoReasignarProfesionalOdonto>();
		DtoReasignarProfesionalOdonto dto;
		
		String consulta = sqlConsultarServiciosAReasignar;
		if(dtoReasignarProfesionalOdonto.getCentroAtencionBusqueda() != ConstantesBD.codigoNuncaValido)
			consulta += "AND aodo.centro_atencion = ? ";
		if(dtoReasignarProfesionalOdonto.getUnidadAgendaBusqueda() != 0)
			consulta += "AND aodo.unidad_agenda = ? ";
		consulta += "order by centroatencion, profesionalsalud";
		try{
			PreparedStatementDecorator ps = new PreparedStatementDecorator(connection, consulta);
			
			ps.setDate(1, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dtoReasignarProfesionalOdonto.getFechaInicialBusqueda())));
			ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dtoReasignarProfesionalOdonto.getFechaFinalBusqueda())));
			ps.setInt (3, dtoReasignarProfesionalOdonto.getProfesionalSaludInicial());
			
			int i = 4;
			if(dtoReasignarProfesionalOdonto.getCentroAtencionBusqueda() != ConstantesBD.codigoNuncaValido){
				ps.setInt(i, dtoReasignarProfesionalOdonto.getCentroAtencionBusqueda());
				i++;
			}
			if(dtoReasignarProfesionalOdonto.getUnidadAgendaBusqueda() != 0){
				ps.setInt (i, dtoReasignarProfesionalOdonto.getUnidadAgendaBusqueda());
			}
			logger.info("sqlConsultarServiciosAReasignar--->"+ps);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next()){
				dto = new DtoReasignarProfesionalOdonto();
				dto.setCodigoPkAgendaOdonto(rs.getInt("codigo"));
				dto.setCentroAtencionNombre(rs.getString("centroatencion"));
				dto.setProfesionalSaludNombre(rs.getString("profesionalsalud"));
				dto.setFechaAgenda(UtilidadFecha.conversionFormatoFechaAAp(rs.getDate("fechaagenda")));
				dto.setHoraAgendaInicial(rs.getString("horainicio"));
				dto.setHoraAgendaFinal(rs.getString("horafin"));
				dto.setUnidadConsultaNombre(rs.getString("unidadagenda"));
				dto.setUnidadConsulta(rs.getInt("codigounidadagenda"));
				dto.setConsultorioNombre(rs.getString("consultorio"));
				array.add(dto);
			}
			
			rs.close();
			ps.close();
		}catch (Exception e) {
			logger.error("Ha ocurrido un error consultando la especialidad de la unidad de agenda--->"+e);
			e.printStackTrace();
		}
		return array;
	}
	
	/**
	 * Método que consulta si un prodfesional de la salud ya está agendado en una fecha y una hora determinada.
	 * retorna true en caso de que este disponible
	 * @param connection
	 * @param codigoMedico
	 * @param fecha
	 * @param horaIni
	 * @param horaFin
	 * @return
	 */
	public static boolean confirmacionDisponibilidadAgenda (Connection connection, int codigoMedico, String fecha, String horaIni, String horaFin){
		boolean disponibilidad = false;
		try{
			PreparedStatementDecorator ps = new PreparedStatementDecorator(connection, sqlConfirmacionDisponibilidadAgenda);
			
			ps.setInt(1, codigoMedico);
			ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fecha)));
			ps.setString(3, horaIni);
			ps.setString(4, horaFin);
			logger.info("confirmacionDisponibilidadAgenda---->"+ps);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next()){
				if (rs.getInt("disponibilidad") == 0)
					disponibilidad = true;
			}
			
			rs.close();
			ps.close();
		}catch (Exception e) {
			logger.error("Ha ocurrido un error consultando la disponibilidad de agenda--->"+e);
		}
		return disponibilidad;
	}
	
	/**
	 * Método que actualiza el codigo del medico que esta realacionado con derterminada agenda
	 * @param connection
	 * @param infoFechaUsuario
	 * @param codigoMedico
	 * @param codigoPk
	 * @return
	 */
	public static boolean actualizarAgendaOdontologica(Connection connection, DtoInfoFechaUsuario infoFechaUsuario, int codigoMedico, int codigoPk){
		boolean resultado = false;
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(connection, sqlActualizarAgendaOdontologica);
			
			ps.setInt(1, codigoMedico);
			ps.setString(2, infoFechaUsuario.getUsuarioModifica());
			ps.setDate(3, Date.valueOf(infoFechaUsuario.getFechaModificaFromatoBD()));
			ps.setString(4, infoFechaUsuario.getHoraModifica());
			ps.setInt(5, codigoPk);
			
			if(ps.executeUpdate() > 0){
				resultado = true;
			}
			logger.info("actualizarAgendaOdontologica----->"+ps);
			ps.close();
		} catch (Exception e) {
			logger.info("error actualizando la agenda odontologica --->"+e);
		}
		return resultado;
	}
	
	/**
	 * Método que inserta un nuevo registro en el log de reasignacion de profesionales 
	 * @param connection
	 * @param dto
	 * @return
	 */
	public static boolean insertarLogReasignarProfesionalOdonto(Connection connection, DtoReasignarProfesionalOdonto dto){
		boolean resultado = false;
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(connection, sqlInsertarLogReasignarProfesionalOdonto);
			
			int secuencia = UtilidadBD.obtenerSiguienteValorSecuencia(connection,"odontologia.seq_log_reasignar_prof_odo");
			
			ps.setInt   (1, secuencia);
			ps.setDate  (2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dto.getFechaProceso())));
			ps.setString(3, dto.getHoraProceso());
			ps.setString(4, dto.getUsuarioProceso());
			ps.setInt   (5, dto.getCodigoAgenda());
			ps.setInt   (6, dto.getCodigoMedicoAnterior());
			ps.setInt   (7, dto.getCodigoMedicoAsignado());
			ps.setInt   (8, dto.getCentroAtencion());
			ps.setInt   (9, dto.getInstitucion());
			ps.setDate  (10, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dto.getFechaAgenda())));
			ps.setString(11, dto.getHoraAgendaInicial());
			ps.setString(12, dto.getHoraAgendaFinal());
			ps.setInt   (13, dto.getUnidadConsulta());
						
			if(ps.executeUpdate() > 0){
				resultado = true;
			}
			logger.info("insertarLogReasignarProfesionalOdonto----->"+ps);
			ps.close();
		} catch (Exception e) {
			logger.info("error insertando el log de reasignacion de profesional odontologico --->"+e);
		}
		return resultado;
	}
	
	/**
	 * Método que consulta la informacion almacenada en el log de reasignaciones de profesional
	 * @param connection
	 * @param dtoReasignarProfesionalOdonto
	 * @return
	 */
	public static ArrayList<DtoReasignarProfesionalOdonto> consultarLogReasignacionProfesional (Connection connection, DtoReasignarProfesionalOdonto dtoReasignarProfesionalOdonto){
		
		ArrayList<DtoReasignarProfesionalOdonto> array = new ArrayList<DtoReasignarProfesionalOdonto>();
		DtoReasignarProfesionalOdonto dto;
		
		String consulta = sqlConsultarLogReasignacionProfesional;
		if(dtoReasignarProfesionalOdonto.getProfesionalSaludInicial() != ConstantesBD.codigoNuncaValido)
			consulta += "AND reapro.codigo_medico_anterior = ? ";
		if(!dtoReasignarProfesionalOdonto.getUsuarioProceso().equals(ConstantesBD.codigoNuncaValido+""))
			consulta += "AND reapro.usuario_proceso = ? ";
		consulta += "order by centroatencion, profesionalsaludinicial, profesionalsaludreasignado";
		try{
			PreparedStatementDecorator ps = new PreparedStatementDecorator(connection, consulta);
			
			ps.setDate(1, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dtoReasignarProfesionalOdonto.getFechaInicialBusqueda())));
			ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dtoReasignarProfesionalOdonto.getFechaFinalBusqueda())));
			ps.setInt (3, dtoReasignarProfesionalOdonto.getCentroAtencionBusqueda());
			
			int i = 4;
			if(dtoReasignarProfesionalOdonto.getProfesionalSaludInicial() != ConstantesBD.codigoNuncaValido){
				ps.setInt(i, dtoReasignarProfesionalOdonto.getProfesionalSaludInicial());
				i++;
			}
			if(!dtoReasignarProfesionalOdonto.getUsuarioProceso().equals(ConstantesBD.codigoNuncaValido+"")){
				ps.setString (i, dtoReasignarProfesionalOdonto.getUsuarioProceso());
			}
			logger.info("sqlConsultarServiciosAReasignar--->"+ps);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next()){
				dto = new DtoReasignarProfesionalOdonto();
				dto.setCentroAtencionNombre(rs.getString("centroatencion"));
				dto.setProfesionalSaludNombre(rs.getString("profesionalsaludinicial"));
				dto.setProfesionalSaludNombreFinal(rs.getString("profesionalsaludreasignado"));
				dto.setFechaProceso(UtilidadFecha.conversionFormatoFechaAAp(rs.getDate("fechaproceso")));
				dto.setHoraProceso(rs.getString("horaproceso"));
				dto.setUsuarioProceso(rs.getString("usuarioproceso"));
				dto.setFechaAgenda(UtilidadFecha.conversionFormatoFechaAAp(rs.getDate("fechaagenda")));
				dto.setHoraAgendaInicial(rs.getString("horaagendainicial"));
				dto.setHoraAgendaFinal(rs.getString("horaagendafinal"));
				dto.setUnidadConsultaNombre(rs.getString("unidadconsulta"));
				array.add(dto);
			}
			
			rs.close();
			ps.close();
		}catch (Exception e) {
			logger.error("Ha ocurrido un error consultando el log de las reasignaciones de profesional--->"+e);
			e.printStackTrace();
		}
		return array;
	}
}
