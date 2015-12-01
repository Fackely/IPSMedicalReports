/**
 * 
 */
package com.princetonsa.dao.sqlbase.odontologia;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoAgendaOdontologica;
import com.princetonsa.dto.odontologia.DtoCancelarAgendaOdonto;
import com.princetonsa.dto.odontologia.DtoCitaOdontologica;
import com.princetonsa.dto.odontologia.DtoDetCancelAgenOdo;


/**
 * @author Jairo Andrï¿½s Gï¿½mez
 * noviembre 19 / 2009
 *
 */
public class SqlBaseCancelarAgendaOdontoDao {
	
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	static Logger logger = Logger.getLogger(SqlBaseCancelarAgendaOdontoDao.class);
	
	private final static String sqlConsultarAgendaOdonto = 
		" SELECT codigo_pk                                         AS codigo_pk," +
		"administracion.getnomcentroatencion(ao.centro_atencion)   AS desc_centro_atencion," +
		"ao.fecha                                                  AS fecha               ," +
		"ao.hora_inicio ||' - ' ||ao.hora_fin                      AS rango_hora         ," +
		"administracion.getnombremedico(ao.codigo_medico)          AS nombre_medico     ," +
		"consultaexterna.getnombreunidadconsulta(ao.unidad_agenda) AS desc_unidad_agenda," +
		"consultaexterna.getnombreconsultorio(ao.consultorio)      AS desc_consultorio," +
		"ao.centro_atencion                                        AS centro_atencion   ," +
		"ao.unidad_agenda                                          AS unidad_agenda     ," +
		"ao.consultorio                                            AS consultorio       ," +
		"ao.dia                                                    AS dia               ," +
		"ao.hora_inicio                                            AS hora_inicio       ," +
		"ao.hora_fin                                               AS hora_fin          ," +
		"ao.codigo_medico                                          AS codigo_medico     ," +
		"ao.cupos                                                  AS cupos " +
		"FROM odontologia.agenda_odontologica ao WHERE ao.activo = '"+ConstantesBD.acronimoSi+"' ";
	
	private final static String sqlActualizarActivoAgenda = "UPDATE odontologia.agenda_odontologica SET activo = ? WHERE codigo_pk = ?";
	
	private final static String sqlInsertardetalleCancelacionAgenOdo = 
		"INSERT INTO odontologia.det_cancel_agen_odo (codigo_pk, cita_odontologica, hora_inicio, hora_fin, codigo_paciente, cancelacion_agenda, numero_solicitud) " +
		"VALUES (?, ?, ?, ?, ?, ?, ?)";
	
	private final static String sqlInsertarCancelaAgendaOdo = "INSERT INTO odontologia.cancelacion_agenda_odo " +
			"(codigo_pk, centro_atencion, unidad_agenda, consultorio, dia, fecha, hora_inicio, hora_fin, codigo_medico, " +
			"fecha_modifica, hora_modifica, usuario_modifica, cupos) " +
			"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private final static String sqlEliminarAgendaOdontologica = "DELETE FROM odontologia.agenda_odontologica WHERE codigo_pk = ?";
	
	private final static String sqlConsultarNoCitasXAgenda = " SELECT COUNT(codigo_pk) AS numero FROM odontologia.citas_odontologicas WHERE agenda = ?";
			
	private final static String sqlConsultaCancelacionAgenda = "SELECT ca.codigo_pk AS codigoPk, " +
																"ca.centro_atencion AS centroAtencion, " +
																"getnomcentroatencion(ca.centro_atencion) AS nomCentroAtencion, " +
																"ca.unidad_agenda AS unidadAgenda, " +
																"getnombreunidadconsulta(ca.unidad_agenda) AS nomUnidadAgenda, " +
																"ca.consultorio AS consultorio, " +
																"getnombreconsultorio(ca.consultorio) AS nomConsultorio, " +
																"ca.fecha AS fecha, " +
																"ca.hora_inicio AS horaInicio, " +
																"ca.hora_fin AS horaFin, " +
																"ca.codigo_medico AS codMedico, " +
																"getnombremedico(ca.codigo_medico) AS nomMedico, " +
																"ca.fecha_modifica AS fechaMod, " +
																"ca.hora_modifica AS horaMod, " +
																"getnombreusuario(ca.usuario_modifica) AS usuarioMod " +
																"FROM odontologia.cancelacion_agenda_odo ca " +
																"WHERE ca.codigo_pk=? ";
	
	private final static String sqlConsultaDetCancelacionAgenda = "SELECT dca.codigo_pk AS codigoPk, " +
																	"dca.cita_odontologica AS citaOdontologica, " +
																	"dca.hora_inicio AS horaIni, " +
																	"dca.hora_fin AS horaFin, " +
																	"dca.codigo_paciente AS codPaciente, " +
																	"getnombrepersona2(dca.codigo_paciente) AS nomPaciente, " +
																	"p.numero_identificacion AS idPaciente, " +
																	"p.telefono_fijo AS telPaciente, " +
																	"dca.cancelacion_agenda AS cancelacionAgenda, " +
																	"dca.numero_solicitud AS numSolicitud, " +
																	"co.hora_inicio AS hora, " +
																	"ao.fecha AS fecha " +
																	"FROM odontologia.det_cancel_agen_odo dca " +
																	"INNER JOIN personas p ON(p.codigo=dca.codigo_paciente) " +
																	"INNER JOIN odontologia.citas_odontologicas co ON(co.codigo_pk=dca.cita_odontologica) " +
																	"INNER JOIN odontologia.agenda_odontologica ao ON(co.agenda=ao.codigo_pk) " +
																	"WHERE dca.cancelacion_agenda=? ";
	
	
	/**
	 * Mï¿½todo que permite consultar la cancelacion de agenda odontologica 
	 * @return
	 */
	public static ArrayList<DtoCancelarAgendaOdonto> consultarCancelacionAgendaOdonto(int codigoPk)
	{	
		ArrayList<DtoCancelarAgendaOdonto> lista = new ArrayList<DtoCancelarAgendaOdonto>();
		
		try 
		{
			Connection con=UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con,sqlConsultaCancelacionAgenda);
			ps.setInt(1, codigoPk);			
			
			logger.info("\n\nConsulta cancelacion agenda:::: "+ps);
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next()){
				DtoCancelarAgendaOdonto dto = new DtoCancelarAgendaOdonto();
				dto.setCodigoPk(rs.getInt("codigopk"));
				dto.setCentroAtencion(rs.getInt("centroatencion"));
				dto.setNombreCentroAtencion(rs.getString("nomcentroatencion"));
				dto.setUnidadAgenda(rs.getInt("unidadagenda"));
				dto.setNombreUnidadAgenda(rs.getString("nomunidadagenda"));
				dto.setConsultorio(rs.getInt("consultorio"));
				dto.setNombreConsultorio(rs.getString("nomconsultorio"));
				dto.setCodigoMedico(rs.getInt("codmedico"));
				dto.setNombreMedico(rs.getString("nommedico"));
				dto.setFechaModificacion(rs.getString("fechamod"));
				dto.setHoraModificacion(rs.getString("horamod"));
				dto.setUsuarioModificacion(rs.getString("usuariomod"));
				lista.add(dto);
			}
			
			ArrayList<DtoDetCancelAgenOdo> listaDet = new ArrayList<DtoDetCancelAgenOdo>();
			
			if(lista.size()>0)
			{
				listaDet=consultarDetCancelacionAgendaOdonto(lista.get(0).getCodigoPk());
				lista.get(0).setDetCancelacionAgendaOdo(listaDet);
			}			
			rs.close();
			ps.close();
			UtilidadBD.cerrarConexion(con);
		} catch (Exception e) {
			logger.error("error consultando la cancelacion de agenda odontolï¿½gica--->"+e);
			e.printStackTrace();
		}
		return lista;
	}
	
	/**
	 * Mï¿½todo que permite consultar el detalle de la cancelacion de agenda odontologica 
	 * @return
	 */
	public static ArrayList<DtoDetCancelAgenOdo> consultarDetCancelacionAgendaOdonto(int codigo)
	{	
		ArrayList<DtoDetCancelAgenOdo> lista = new ArrayList<DtoDetCancelAgenOdo>();
			
		try 
		{
			Connection con=UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con,sqlConsultaDetCancelacionAgenda);
			ps.setInt(1, codigo);
			logger.info("\n\nConsulta det cancelacion agenda:::: "+ps);
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next()){
				DtoDetCancelAgenOdo dto = new DtoDetCancelAgenOdo();
				dto.setCodigoPk(rs.getInt("codigopk"));
				dto.setCitaOdontologica(rs.getInt("citaodontologica"));
				dto.setHoraInicio(rs.getString("horaini"));
				dto.setHoraFin(rs.getString("horafin"));
				dto.setCodigoPaciente(rs.getInt("codpaciente"));
				dto.setNomPaciente(rs.getString("nompaciente"));
				dto.setIdPaciente(rs.getString("idpaciente"));
				dto.setTelefonoPaciente(rs.getString("telpaciente"));
				dto.setCancelacionAgenda(rs.getInt("cancelacionagenda"));
				dto.setNumeroSolicitud(rs.getInt("numsolicitud"));
				dto.setHora(rs.getString("hora"));
				dto.setFecha(rs.getString("fecha"));
				lista.add(dto);
			}		
						
			rs.close();
			ps.close();
			UtilidadBD.closeConnection(con);
		} catch (Exception e) {
			logger.error("error consultando la cancelacion de agenda odontolï¿½gica--->"+e);
			e.printStackTrace();
		}
		return lista;
	}
	
	
	
	/**
	 * Mï¿½todo que permite consultar la agenda odontologica generada
	 * @param connection
	 * @param dtoCancelarAgendaOdonto
	 * pueden venir vacios siempre y cuando vengan los valores en el dto, 
	 * en caso contrario debe traer los ids separados por comas
	 * @param centrosAtencion 
	 * @param unidadesAgenda
	 * @return
	 */
	public static ArrayList<DtoAgendaOdontologica> consultarAgendaOdonto(Connection connection, DtoCancelarAgendaOdonto dtoCancelarAgendaOdonto, String centrosAtencion, String unidadesAgenda){
		ArrayList<DtoAgendaOdontologica> list = new ArrayList<DtoAgendaOdontologica>();
		DtoAgendaOdontologica dtoAgenda = null;
		String consulta = sqlConsultarAgendaOdonto;
		if (dtoCancelarAgendaOdonto.getHoraInicio().equals("") && dtoCancelarAgendaOdonto.getHoraFin().equals(""))
			consulta += "AND TO_CHAR(fecha,'yyyy-mm-dd') BETWEEN ? AND ? ";
		else
			consulta += "AND TO_CHAR(fecha,'yyyy-mm-dd') BETWEEN ? AND ?  AND hora_inicio >= ? AND hora_fin <= ?";
		
		//"AND TO_CHAR(fecha,'yyyy-mm-dd') ||' ' || hora_inicio BETWEEN ? AND ? " +
			//		"AND TO_CHAR(fecha,'yyyy-mm-dd') ||' ' || hora_fin BETWEEN ? AND ? ";
		
		if (dtoCancelarAgendaOdonto.getCentroAtencion() != ConstantesBD.codigoNuncaValido)
			consulta += "AND ao.centro_atencion = ? ";
		else
			consulta += "AND ao.centro_atencion IN (" + centrosAtencion + ") ";
		if (dtoCancelarAgendaOdonto.getUnidadAgenda() != ConstantesBD.codigoNuncaValido)
			consulta += "AND ao.unidad_agenda   = ? ";
		else
			consulta += "AND ao.unidad_agenda   IN (" + unidadesAgenda + ") ";
		if (dtoCancelarAgendaOdonto.getConsultorio() != ConstantesBD.codigoNuncaValido)
			consulta += "AND ao.consultorio     = ? ";
		if (dtoCancelarAgendaOdonto.getDia() != ConstantesBD.codigoNuncaValido)
			consulta += "AND ao.dia             = ? ";
		if (dtoCancelarAgendaOdonto.getCodigoMedico() != ConstantesBD.codigoNuncaValido)
			consulta += "AND ao.codigo_medico   = ? ";
		logger.info("consulta---->"+consulta);
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(connection,consulta);
			int contador = 1;
			if (dtoCancelarAgendaOdonto.getHoraInicio().equals("") && dtoCancelarAgendaOdonto.getHoraFin().equals("")){
				ps.setDate(contador, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dtoCancelarAgendaOdonto.getFechaInicial())));
				contador++;
				ps.setDate(contador, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dtoCancelarAgendaOdonto.getFechaFinal())));
				contador++;
			}
			else{
				String fechaInicial = UtilidadFecha.conversionFormatoFechaABD(dtoCancelarAgendaOdonto.getFechaInicial());
				String fechaFinal = UtilidadFecha.conversionFormatoFechaABD(dtoCancelarAgendaOdonto.getFechaFinal());
				ps.setString(contador, fechaInicial);
				contador++;logger.info("contador"+contador);
				ps.setString(contador, fechaFinal);
				contador++;
				ps.setString(contador, dtoCancelarAgendaOdonto.getHoraInicio());
				contador++;
				ps.setString(contador, dtoCancelarAgendaOdonto.getHoraFin());
				contador++;
			}
			if (dtoCancelarAgendaOdonto.getCentroAtencion() != ConstantesBD.codigoNuncaValido){
				ps.setInt(contador, dtoCancelarAgendaOdonto.getCentroAtencion());
				contador++;
			}
			if (dtoCancelarAgendaOdonto.getUnidadAgenda() != ConstantesBD.codigoNuncaValido){
				ps.setInt(contador, dtoCancelarAgendaOdonto.getUnidadAgenda());
				contador++;
			}
			if (dtoCancelarAgendaOdonto.getConsultorio() != ConstantesBD.codigoNuncaValido){
				ps.setInt(contador, dtoCancelarAgendaOdonto.getConsultorio());
				contador++;
			}
			if (dtoCancelarAgendaOdonto.getDia() != ConstantesBD.codigoNuncaValido){
				ps.setInt(contador, dtoCancelarAgendaOdonto.getDia());
				contador++;
			}
			if (dtoCancelarAgendaOdonto.getCodigoMedico() != ConstantesBD.codigoNuncaValido){
				ps.setInt(contador, dtoCancelarAgendaOdonto.getCodigoMedico());
				contador++;
			}
			logger.info("sqlConsultarAgendaOdonto-->>"+ps);
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next()){
				dtoAgenda = new DtoAgendaOdontologica();
				dtoAgenda.setCodigoPk(rs.getInt("codigo_pk"));
				dtoAgenda.setDescripcionCentAten(rs.getString("desc_centro_atencion"));
				dtoAgenda.setFecha(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fecha")));
				dtoAgenda.setHoraModifica(rs.getString("rango_hora"));
				dtoAgenda.setNombreMedico(rs.getString("nombre_medico"));
				dtoAgenda.setDescripcionUniAgen(rs.getString("desc_unidad_agenda"));
				dtoAgenda.setDescripcionConsultorio(rs.getString("desc_consultorio"));
				dtoAgenda.setCentroAtencion(rs.getInt("centro_atencion"));
				dtoAgenda.setUnidadAgenda(rs.getInt("unidad_agenda"));
				dtoAgenda.setConsultorio(rs.getInt("consultorio"));
				dtoAgenda.setDia(rs.getInt("dia"));
				dtoAgenda.setHoraInicio(rs.getString("hora_inicio"));
				dtoAgenda.setHoraFin(rs.getString("hora_fin"));
				dtoAgenda.setCodigoMedico(rs.getInt("codigo_medico"));
				dtoAgenda.setCupos(rs.getInt("cupos"));
				list.add(dtoAgenda);
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			logger.error("error consultando la agenda odontolï¿½gica--->"+e);
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * Mï¿½todo que actualiza el campo activo de la tabla odontologia.agenda_odontologica
	 * @param connection
	 * @param codigo_pk
	 * @return
	 */
	public static boolean actualizarActivoAgenda(Connection connection, int codigo_pk){
		boolean resultado = false;
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(connection, sqlActualizarActivoAgenda);
			ps.setString(1, ConstantesBD.acronimoNo);
			ps.setInt(2, codigo_pk);
			logger.info("sqlActualizarActivoAgenda-->>"+ps);
			if(ps.executeUpdate() > 0)
			{
				logger.info(">>> Entré a Ejecutar update");
				resultado = true;
			}
				
			ps.close();
		//} catch (Exception e) {
		} catch (SQLException e) {
			logger.error("error actualizando el campo activo de la agenda odontologica-->"+e);
			e.printStackTrace();
		}
		return resultado;
	}
	
	/**
	 * Mï¿½todo que inserta el detalle de la cancelacion de agenda odontologica
	 * @param connection
	 * @param dtoCita
	 * @return
	 */
	public static boolean insertardetalleCancelacionAgenOdo(Connection connection, DtoCitaOdontologica dtoCita, int codigoCancelacion, int numeroSolicitud){
		boolean resultado = false;
		logger.info("entre a insertar el detalle en el sqlbase");
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(connection, sqlInsertardetalleCancelacionAgenOdo);
			int codigoPk = UtilidadBD.obtenerSiguienteValorSecuencia(connection,"odontologia.seq_det_cancel_agen_odo");
			ps.setInt(1, codigoPk);
			ps.setInt(2, dtoCita.getCodigoPk());
			ps.setString(3, dtoCita.getHoraInicio());
			ps.setString(4, dtoCita.getHoraFinal());
			ps.setInt(5, dtoCita.getCodigoPaciente());
			ps.setInt(6, codigoCancelacion);
			if (numeroSolicitud != ConstantesBD.codigoNuncaValido)
				ps.setInt(7, numeroSolicitud);
			else
				ps.setNull(7, java.sql.Types.INTEGER);
			logger.info("sqlInsertardetalleCancelacionAgenOdo-->>"+ps);
			if (ps.executeUpdate() > 0){
				resultado = true;
			}
			ps.close();
		} catch (Exception e) {
			logger.error("se ha geneardo un error insertando el detalle de la cancelacion de agenda odonto::"+e);
			e.printStackTrace();
		}
		return resultado;
	}
	
	/**
	 * Mï¿½todo que inserta en odontologia.cancelacion_agenda_odo
	 * @param connection
	 * @param dtoAgenda
	 * @return
	 */
	public static ResultadoBoolean insertarCancelaAgendaOdo(Connection connection, DtoAgendaOdontologica dtoAgenda){
		ResultadoBoolean resultado = new ResultadoBoolean(false);
		try {
			logger.info(">>> El codigo del médico es: "+dtoAgenda.getCodigoMedico());
			PreparedStatementDecorator ps = new PreparedStatementDecorator(connection, sqlInsertarCancelaAgendaOdo);
			int codigoPk = UtilidadBD.obtenerSiguienteValorSecuencia(connection,"odontologia.seq_cancelacion_agenda_odo");
			ps.setInt(1, codigoPk);
			ps.setInt(2, dtoAgenda.getCentroAtencion());
			ps.setInt(3, dtoAgenda.getUnidadAgenda());
			ps.setInt(4, dtoAgenda.getConsultorio());
			ps.setInt(5, dtoAgenda.getDia());
			ps.setDate(6, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dtoAgenda.getFecha())));
			ps.setString(7, dtoAgenda.getHoraInicio());
			ps.setString(8, dtoAgenda.getHoraFin());
			/**
			 * Solución Tarea = 139569
			 * Cuando el código del médico = 0, siginifica que la agenda está registrada a profesional no asignado, por tal motivo se inserta un null
			 */
			if(dtoAgenda.getCodigoMedico()>0)
				ps.setInt(9, dtoAgenda.getCodigoMedico());
			else
				ps.setNull(9,Types.INTEGER);
			ps.setDate(10, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dtoAgenda.getFechaModifica())));
			ps.setString(11, dtoAgenda.getHoraModifica());
			ps.setString(12, dtoAgenda.getUsuarioModifica());
			ps.setInt(13, dtoAgenda.getCupos());
			if (ps.executeUpdate() > 0){
				logger.info(">>> Entré a Ejecutar insert");
				resultado = new ResultadoBoolean(true,codigoPk+"");
			}
			ps.close();
		} catch (Exception e) {
			logger.error("se ha geneardo un error insertando la agenda odonto::"+e);
			e.printStackTrace();
		}
		return resultado;
	}
	
	/**
	 * Mï¿½todo utilizado para eliminar el registro de las agendas que no tiene citas asignada y son canceladas
	 * @param connection
	 * @param codigoAgenda
	 * @return
	 */
	public static boolean eliminarAgendaOdontologica(Connection connection, int codigoAgenda){
		boolean resultado = false;
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(connection, sqlEliminarAgendaOdontologica);
			ps.setInt(1, codigoAgenda);
			logger.info("sqlEliminarAgendaOdontologica-->>"+ps);
			if (ps.executeUpdate() > 0){
				resultado = true;
			}
			ps.close();
		} catch (Exception e) {
			logger.error("se ha geneardo un error eliminando el registro de la agenda::"+e);
			e.printStackTrace();
		}
		return resultado;
	}
	
	/**
	 * Mï¿½todo que devuelve el numero de citas por agenda no reservadas ni asignadas
	 * @param connection
	 * @param codigoAgenda
	 * @return
	 */
	public static int consultarNoCitasXAgenda (Connection connection, int codigoAgenda){
		int noRegistros = 0;
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(connection, sqlConsultarNoCitasXAgenda);
			ps.setInt(1, codigoAgenda);
			logger.info("sqlConsultarNoCitasXAgenda-->"+ps);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				noRegistros = rs.getInt("numero");
		} catch (Exception e) {
			logger.info("se ha generado un error consultando el numero de citas no reservadas ni asignadas"+e);
		}
		return noRegistros;
	}
}

