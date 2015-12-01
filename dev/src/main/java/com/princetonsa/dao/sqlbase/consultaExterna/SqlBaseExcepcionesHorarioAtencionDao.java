package com.princetonsa.dao.sqlbase.consultaExterna;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.consultaExterna.DtoExcepcionesHorarioAtencion;
import com.princetonsa.mundo.parametrizacion.CentroAtencion;

/**
 * Persistencia para los horarios de atención
 * @author axioma
 *
 */
public class SqlBaseExcepcionesHorarioAtencionDao {
	
	/**
	 * Inserta una nueva excepción de horarios de atención 
	 * @param dtoExcepcionesHorario DTO con los datos del horario de atención a ingresar 
	 * @param con Conexión con la BD
	 * @return true en caso de insertar correctamente, false de lo contrario
	 */
	public static boolean insertar(DtoExcepcionesHorarioAtencion dtoExcepcionesHorario, Connection con) {
		String sentencia=
				"INSERT INTO " +
					"consultaexterna.ex_horario_atencion " +
					"(" +
						"codigo_pk," +
						"centro_atencion," +
						"unidad_agenda," +
						"consultorio," +
						"profesional_salud," +
						"fecha_inicio," +
						"fecha_finalizacion," +
						"hora_inicio," +
						"hora_finalizacion," +
						"observaciones," +
						"institucion," +
						"fecha_modifica," +
						"hora_modifica," +
						"usuario_modifica," +
						"eliminado," +
						"fecha_eliminacion," +
						"hora_eliminacion," +
						"usuario_eliminacion," +
						"es_generar_agenda) " +
					"VALUES (?,?,?,?,?,?,?,?,?,?,?,current_date,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,'N',?,?,?,?)"; 
		boolean respuesta = false;
		PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
		
		int secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "consultaexterna.seq_ex_horario_atencion");
		
		try {
			psd.setInt(1, secuencia);
			psd.setInt(2, dtoExcepcionesHorario.getDtoCentroAtencion().getConsecutivo());
			psd.setInt(3, dtoExcepcionesHorario.getUnidadAgenda());
			psd.setInt(4, dtoExcepcionesHorario.getConsultorio());
			psd.setInt(5,dtoExcepcionesHorario.getCodigoMedico());
			psd.setDate(6,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dtoExcepcionesHorario.getFechaInicio())));
			psd.setDate(7,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dtoExcepcionesHorario.getFechaFin())));
			psd.setString(8,dtoExcepcionesHorario.getHoraInicio());
			psd.setString(9,dtoExcepcionesHorario.getHoraFin());
			psd.setString(10,dtoExcepcionesHorario.getObservaciones());
			psd.setInt(11,dtoExcepcionesHorario.getInstitucion());
			psd.setString(12,dtoExcepcionesHorario.getUsuarioModifica());
			psd.setNull(13,Types.DATE);
			psd.setNull(14,Types.VARCHAR);
			psd.setNull(15,Types.VARCHAR);
			psd.setString(16, dtoExcepcionesHorario.getEsGenerar());
			
			if(psd.executeUpdate()>0)
			{
				respuesta = true;
			}
			UtilidadBD.cerrarObjetosPersistencia(psd, null, null);
		} catch (SQLException e) 
		{
			Log4JManager.error("error ingresando datos ....................", e);
			return respuesta;
		}
		return respuesta;
		
	}

	
	/**
	 * Método que elimina un registro de Excepciones Horario Atención
	 * @param dtoEnvioEmailAutomatico DTO con los datos del horario de atención eliminado
	 * @param con Conexión con la BD
	 * @return true en caso de eliminar correctamente, false de lo contratrio
	 */
	public static boolean eliminar(DtoExcepcionesHorarioAtencion listaDtoExcepciones,
			Connection con)
	{
		boolean respuesta = false;
		String sentencia="update  consultaexterna.ex_horario_atencion set  fecha_modifica = current_date, hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", usuario_modifica= ?,eliminado='S', fecha_eliminacion = current_date, hora_eliminacion = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", usuario_eliminacion = ? where codigo_pk = ?";
		PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
		try
		{
			psd.setString(1, listaDtoExcepciones.getUsuarioModifica());
			psd.setString(2, listaDtoExcepciones.getUsuarioEliminacion());
			psd.setInt(3, listaDtoExcepciones.getCodigoPk());
			
			if (psd.executeUpdate()>0)
			{
				respuesta = true;
			}
			
			UtilidadBD.cerrarObjetosPersistencia(psd, null, null);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error eliminando  datos ....................", e);
			return respuesta;
		}
	
		return respuesta;
	}
	
	
	public static boolean modificar(DtoExcepcionesHorarioAtencion listaDtoExcepciones,
			Connection con){
		
		boolean respuesta = false;
		String sentencia=
			"UPDATE " +
				"consultaexterna.ex_horario_atencion " +
			"SET " +
				"centro_atencion=?, " +
				"unidad_agenda=?, " +
				"consultorio=?, " +
				"profesional_salud=?, " +
				"fecha_inicio=?, " +
				"fecha_finalizacion=?, " +
				"hora_inicio=?, " +
				"hora_finalizacion=?, " +
				"observaciones=?, " +
				"institucion=?, " +
				"fecha_modifica=current_date, " +
				"hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+"," +
				"usuario_modifica=?, " +
				"eliminado=?, " +
				"fecha_eliminacion=?, " +
				"hora_eliminacion=?, " +
				"usuario_eliminacion=?, " +
				"es_generar_agenda=? " +
			"WHERE " +
				"codigo_pk= ?"; 
		
		int secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "consultaexterna.seq_log_ex_horario_atencion");
		String sentenciaLog=
				"INSERT INTO " +
					"consultaexterna.log_ex_horario_atencion " +
					"(" +
						"codigo_pk, " +
						"ex_horario_atencion, " +
						"centro_atencion, " +
						"unidad_agenda, " +
						"consultorio, " +
						"profesional_salud, " +
						"fecha_inicio, " +
						"fecha_finalizacion, " +
						"hora_inicio, " +
						"hora_finalizacion, " +
						"observaciones, " +
						"fecha_modifica, " +
						"hora_modifica, " +
						"usuario_modifica" +
					")" +
					"SELECT " +
						"?, " +
						"codigo_pk, " +
						"centro_atencion, " +
						"unidad_agenda, " +
						"consultorio, " +
						"profesional_salud, " +
						"fecha_inicio, " +
						"fecha_finalizacion, " +
						"hora_inicio, " +
						"hora_finalizacion, " +
						"observaciones, " +
						"fecha_modifica, " +
						"hora_modifica, " +
						"usuario_modifica " +
					"FROM " +
						"consultaexterna.ex_horario_atencion " +
					"WHERE codigo_pk=?";
				
		PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentenciaLog);
		try
		{
			psd.setInt(1, secuencia);
			psd.setInt(2, listaDtoExcepciones.getCodigoPk());
			
			if (psd.executeUpdate()>0)
			{
				respuesta = true;
			}
			
			UtilidadBD.cerrarObjetosPersistencia(psd, null, null);
			if (respuesta)
			{
				psd=new PreparedStatementDecorator(con, sentencia);
				psd.setInt(1, listaDtoExcepciones.getDtoCentroAtencion().getConsecutivo());
				psd.setInt(2, listaDtoExcepciones.getUnidadAgenda());
				psd.setInt(3, listaDtoExcepciones.getConsultorio());
				psd.setInt (4,listaDtoExcepciones.getCodigoMedico());
				psd.setDate(5,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(listaDtoExcepciones.getFechaInicio())));
				psd.setDate(6,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(listaDtoExcepciones.getFechaFin())));
				psd.setString(7, listaDtoExcepciones.getHoraInicio());
				psd.setString(8, listaDtoExcepciones.getHoraFin());
				psd.setString(9, listaDtoExcepciones.getObservaciones());
				psd.setInt(10, listaDtoExcepciones.getInstitucion());
				psd.setString(11, listaDtoExcepciones.getUsuarioModifica());
				psd.setString(12, listaDtoExcepciones.getEliminado());
				psd.setNull(13,Types.DATE);
				psd.setNull(14,Types.VARCHAR);
				psd.setNull(15,Types.VARCHAR);
				psd.setString(16,listaDtoExcepciones.getEsGenerar());
				psd.setInt(17, listaDtoExcepciones.getCodigoPk());
				
				if(psd.executeUpdate()>0)
				{
					respuesta = true;
				}
				UtilidadBD.cerrarObjetosPersistencia(psd, null, null);
			}
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error modificando datos ....................", e);
			return respuesta;
		}
	
		return respuesta;
		
	}
	
	public static ArrayList<DtoExcepcionesHorarioAtencion> listar(Connection con,
			int codigoInstitucion, int centroAtencion, int profesional) {
		String sentencia="SELECT " +
								"eha.codigo_pk as codigo_pk, "+
							    "eha.centro_atencion as centro_atencion, "+
							    "eha.unidad_agenda as unidad_agenda, "  +
							    "consultaexterna.getnombreunidadconsulta(unidad_agenda) as desc_unidad_agenda, "+
							    "eha.consultorio as consultorio, "+
							    "getnombreconsultorio(eha.consultorio) AS nombre_consultorio, " +
							    "eha.profesional_salud as profesional_salud, "+
							    "getNombreUsuario2(u.login) AS nombre_usuario, " +
							    "to_char(eha.fecha_inicio,'"+ConstantesBD.formatoFechaAp+"') as fecha_inicio, "+
							    "to_char(eha.fecha_finalizacion,'"+ConstantesBD.formatoFechaAp+"') as fecha_finalizacion, "+
							    "eha.hora_inicio as hora_inicio, "+
							    "eha.hora_finalizacion as hora_finalizacion, "+
							    "eha.observaciones as observaciones, "+
							    "eha.institucion as institucion, "+
							    "eha.fecha_modifica as fecha_modifica, "+
							    "eha.hora_modifica as hora_modifica, "+
							    "eha.usuario_modifica as usuario_modifica, "+
							    "eha.eliminado as eliminado, "+
							    "eha.fecha_eliminacion as fecha_eliminacion, "+
							    "eha.hora_eliminacion as hora_eliminacion, "+
							    "eha.usuario_eliminacion as usuario_eliminacion, " +
							    "eha.es_generar_agenda AS es_generar_agenda "+
							    "FROM "+
							    	"consultaexterna.ex_horario_atencion eha "+
							    "INNER JOIN " +
									"administracion.usuarios u " +
										"ON(eha.profesional_salud=u.codigo_persona) " +
							    "WHERE " +
							    		"eha.institucion = ? " +
							    	"AND " +
							    		"eha.centro_atencion= ? " +
							    	"AND " +
							    		"eha.eliminado = '"+ConstantesBD.acronimoNo+"' " +
							    	"AND " +
							    		"eha.profesional_salud=? " +
							    "ORDER BY desc_unidad_agenda ";
							 
		PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
		try {
			psd.setInt(1, codigoInstitucion);
			psd.setInt(2,centroAtencion );
			psd.setInt(3, profesional);
			
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			
			ArrayList<DtoExcepcionesHorarioAtencion>  resultado=new ArrayList<DtoExcepcionesHorarioAtencion> ();
			
			while(rsd.next())
			{
				DtoExcepcionesHorarioAtencion dto=new DtoExcepcionesHorarioAtencion ();
				dto.setCodigoPk(rsd.getInt("codigo_pk"));
				CentroAtencion dtoCentroAtencion=new CentroAtencion();
				dtoCentroAtencion.setConsecutivo(rsd.getInt("centro_atencion"));
				dto.setCentroAtencion(dtoCentroAtencion);
				dto.setUnidadAgenda(rsd.getInt("unidad_agenda"));
				dto.setDescripcionUniAgen(rsd.getString("desc_unidad_agenda"));
				dto.setConsultorio(rsd.getInt("consultorio"));
				dto.setDescripcionConsultorio(rsd.getString("nombre_consultorio"));
				dto.setCodigoMedico(rsd.getInt("profesional_salud"));
				dto.setNombreMedico(rsd.getString("nombre_usuario"));
				dto.setFechaInicio(rsd.getString("fecha_inicio"));
				dto.setFechaFin(rsd.getString("fecha_finalizacion"));
				dto.setHoraInicio(rsd.getString("hora_inicio"));
				dto.setHoraFin(rsd.getString("hora_finalizacion"));
				dto.setObservaciones(rsd.getString("observaciones"));
				dto.setInstitucion(rsd.getInt("institucion"));
				dto.setUsuarioModifica(rsd.getString("usuario_modifica"));
				dto.setEliminado(rsd.getString("eliminado"));
				dto.setFechaEliminacion(rsd.getString("fecha_eliminacion"));
				dto.setHoraEliminacion(rsd.getString("hora_eliminacion"));
				dto.setUsuarioEliminacion(rsd.getString("usuario_eliminacion"));
				dto.setEsGenerar(rsd.getString("es_generar_agenda"));
				
				
				resultado.add(dto);
			}
			
			UtilidadBD.cerrarObjetosPersistencia(psd, rsd, null);
			return resultado;
		} catch (SQLException e) {
			Log4JManager.error("Error consultando la BD ", e);
		}
		return null;
		
	}

	public static void cargar (Connection con, DtoExcepcionesHorarioAtencion listaDtoExcepciones) {
		String sentencia="SELECT " +
								"eha.codigo_pk as codigo_pk , "+
							    "eha.centro_atencion as centro_atencion ,"+
							    "eha.unidad_agenda as unidad_agenda,"  +
							    "consultaexterna.getnombreunidadconsulta(unidad_agenda) as desc_unidad_agenda, "+
							    "eha.consultorio as consultorio,"+
							    "getnombreconsultorio(eha.consultorio) AS nombre_consultorio, " +
							    "eha.profesional_salud as profesional_salud ,"+
							    "getNombreUsuario2(u.login) AS nombre_usuario, " +
							    "eha.fecha_inicio as fecha_inicio , "+
							    "eha.fecha_finalizacion as fecha_finalizacion,"+
							    "eha.hora_inicio as hora_inicio,"+
							    "eha.hora_finalizacion as hora_finalizacion ,"+
							    "eha.observaciones as observaciones ,"+
							    "eha.institucion as institucion ,"+
							    "eha.fecha_modifica as fecha_modifica,"+
							    "eha.hora_modifica as hora_modifica,"+
							    "eha.usuario_modifica as usuario_modifica,"+
							    "eha.eliminado as eliminado ,"+
							    "eha.fecha_eliminacion as fecha_eliminacion ,"+
							    "eha.hora_eliminacion as hora_eliminacion ,"+
							    "eha.usuario_eliminacion as usuario_eliminacion, " +
							    "eha.es_generar_agenda AS es_generar_agenda "+
							    "FROM "+
							    "consultaexterna.ex_horario_atencion eha "+
							    "INNER JOIN " +
								"administracion.usuarios u ON(eha.profesional_salud=u.codigo_persona) " +
							    "where eha.codigo_pk = ?";
							 
		
		PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
		try {
			psd.setInt(1, listaDtoExcepciones.getCodigoPk());
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			while(rsd.next())
			{
				
				listaDtoExcepciones.setCodigoPk(rsd.getInt("codigo_pk"));
				CentroAtencion dtoCentroAtencion=new CentroAtencion();
				dtoCentroAtencion.setConsecutivo(rsd.getInt("centro_atencion"));
				listaDtoExcepciones.setCentroAtencion(dtoCentroAtencion);
				listaDtoExcepciones.setUnidadAgenda(rsd.getInt("unidad_agenda"));
				listaDtoExcepciones.setDescripcionUniAgen(rsd.getString("desc_unidad_agenda"));
				listaDtoExcepciones.setConsultorio(rsd.getInt("consultorio"));
				listaDtoExcepciones.setDescripcionConsultorio(rsd.getString("nombre_consultorio"));
				listaDtoExcepciones.setCodigoMedico(rsd.getInt("profesional_salud"));
				listaDtoExcepciones.setNombreMedico(rsd.getString("nombre_usuario"));
				listaDtoExcepciones.setFechaInicio(rsd.getString("fecha_inicio"));
				listaDtoExcepciones.setFechaFin(rsd.getString("fecha_finalizacion"));
				listaDtoExcepciones.setHoraInicio(rsd.getString("hora_inicio"));
				listaDtoExcepciones.setHoraFin(rsd.getString("hora_finalizacion"));
				listaDtoExcepciones.setObservaciones(rsd.getString("observaciones"));
				listaDtoExcepciones.setInstitucion(rsd.getInt("institucion"));
				listaDtoExcepciones.setUsuarioModifica(rsd.getString("usuario_modifica"));
				listaDtoExcepciones.setEliminado(rsd.getString("eliminado"));
				listaDtoExcepciones.setFechaEliminacion(rsd.getString("fecha_eliminacion"));
				listaDtoExcepciones.setHoraEliminacion(rsd.getString("hora_eliminacion"));
				listaDtoExcepciones.setUsuarioEliminacion(rsd.getString("usuario_eliminacion"));
				listaDtoExcepciones.setEsGenerar(rsd.getString("es_generar_agenda"));
			}
			UtilidadBD.cerrarObjetosPersistencia(psd, rsd, null);
		} catch (SQLException e) {
			Log4JManager.error("Error consultando la BD ", e);
		}
	}
	
}